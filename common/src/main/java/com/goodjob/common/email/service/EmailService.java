package com.goodjob.common.email.service;

import com.goodjob.common.email.constant.EmailType;
import com.goodjob.common.email.entity.SendEmailLog;
import com.goodjob.common.email.mailSender.EmailSenderService;
import com.goodjob.common.email.repository.SendEmailLogRepository;
import com.goodjob.common.redis.RedisUt;
import com.goodjob.common.rsData.RsData;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EmailService {
    private final SendEmailLogRepository sendEmailLogRepository;
    private final EmailSenderService emailSenderService;
    private final RedisUt redisUt;
    private static final long VERIFICATION_CODE_TTL = 1000L * 60 * 5;

    public void sendEmail(EmailType emailType, SendEmailLog sendEmailLog, String verificationCode) {
        if (emailType.equals(EmailType.JOIN)) {
            // 레디스에 verifyCode 저장
            redisUt.setValue(sendEmailLog.getEmail(), verificationCode, VERIFICATION_CODE_TTL);
        }

        sendEmailLogRepository.save(sendEmailLog);

        RsData trySendRs = trySend(sendEmailLog);
        setCompleted(sendEmailLog, trySendRs.getResultCode(), trySendRs.getMsg());
    }

    @Async
    @Transactional
    public void sendJoinEmail(String email) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String verificationCode = uuid.substring(0, 15);

        String title = getEmailTitle(EmailType.JOIN);
        String body = getEmailBody(EmailType.JOIN, verificationCode, null);

        SendEmailLog sendEmailLog = getSendEmailLog(email, title, body, null);
        sendEmail(EmailType.JOIN, sendEmailLog, verificationCode);
    }

    @Async
    @Transactional
    public void sendPasswordEmail(String username, String email, String password) {
        String title = getEmailTitle(EmailType.PASSWORD);
        String body = getEmailBody(EmailType.PASSWORD, null, password);

        SendEmailLog sendEmailLog = getSendEmailLog(email, title, body, username);
        sendEmail(EmailType.PASSWORD, sendEmailLog, null);
    }

    public RsData verifyCode(String email, String providedCode) {
        // 이메일이 오류 없이 보내졌는지 확인
        RsData emailResultCodeRsData = checkEmailResultCode(email);

        if (emailResultCodeRsData.isFail()) {
            return emailResultCodeRsData;
        }

        // 레디스에서 이메일을 키값으로 검증코드 반환
        try {
            String originalCode = redisUt.getValue(email);

            if (originalCode.equals(providedCode)) {
                return RsData.of("S-1", "인증 코드가 확인되었습니다.");
            }

            return RsData.of("F-1", "잘못된 인증 코드입니다.");
        } catch (NullPointerException e) {
            return RsData.of("F-1", "이메일 정보가 잘못되었습니다. 올바른 이메일을 입력하여 다시 시도해주세요.");
        }
    }

    public Optional<SendEmailLog> findByEmail(String email) {
        return sendEmailLogRepository.findByEmail(email);
    }

    public Optional<SendEmailLog> findByUsername(String username) {
        return sendEmailLogRepository.findByUsername(username);
    }

    private RsData checkEmailResultCode(String email) {
        Optional<SendEmailLog> opSendEmailLog = findByEmail(email);
        if (opSendEmailLog.isEmpty()) {
            return RsData.of("F-1", "이메일 정보가 잘못되었습니다. 올바른 이메일을 입력하여 다시 시도해주세요.");
        }

        SendEmailLog sendEmailLog = opSendEmailLog.get();
        String resultCode = sendEmailLog.getResultCode();

        if (resultCode.startsWith("F-")) {
            return RsData.of(resultCode, sendEmailLog.getMessage());
        }

        return RsData.of(resultCode, "메일이 성공적으로 발송되었습니다.");
    }

    private RsData trySend(SendEmailLog sendEmailLog) {
        try {
            emailSenderService.send(sendEmailLog.getEmail(), "no-reply@goodjob.com",
                    sendEmailLog.getSubject(), sendEmailLog.getBody());

            return RsData.of("S-1", "메일이 발송되었습니다.");
        } catch (MailException e) {
            log.error("MailException= {}", e.getMessage());
            return RsData.of("F-1", e.getMessage());
        } catch (MessagingException e) {
            log.error("MessagingException= {}", e.getMessage());
            return RsData.of("F-1", e.getMessage());
        }
    }

    private void setCompleted(SendEmailLog sendEmailLog, String resultCode, String message) {
        if (resultCode.startsWith("S-")) {
            sendEmailLog.setSendEndDate(LocalDateTime.now());
        } else {
            sendEmailLog.setFailDate(LocalDateTime.now());
        }

        sendEmailLog.setResultCode(resultCode);
        sendEmailLog.setMessage(message);

        sendEmailLogRepository.save(sendEmailLog);
    }

    private String getEmailTitle(EmailType emailType) {
        switch (emailType) {
            case JOIN: {
                return "[이메일인증] GoodJob 이메일 인증 코드입니다. 코드를 입력하여 회원가입을 완료해주세요.";
            }
            case PASSWORD: {
                return "[임시비밀번호발급] GoodJob 임시비밀번호 발급 메일입니다. 로그인 후 비밀번호를 변경해주세요.";
            }
            default: {
                return null;
            }
        }
    }

    private String getEmailBody(EmailType emailType, String verificationCode, String password) {
        switch (emailType) {
            case JOIN:
                return "<html>"
                        + "<head>"
                        + "<title>이메일 인증 코드</title>"
                        + "</head>"
                        + "<body>"
                        + "<h2>안녕하세요!</h2>"
                        + "<p>회원가입을 완료하기 위한 이메일 인증 코드입니다.</p>"
                        + "<p>아래의 인증 코드를 입력하여 회원가입을 완료해주세요.</p>"
                        + "<p>인증 코드: <strong>" + verificationCode + "</strong></p>"
                        + "<p>감사합니다.</p>"
                        + "</body>"
                        + "</html>";
            case PASSWORD:
                return "<html>"
                        + "<head>"
                        + "<title>임시비밀번호발급</title>"
                        + "</head>"
                        + "<body>"
                        + "<h2>임시비밀번호확인</h2>"
                        + "<p>회원님의 임시비밀번호입니다.</p>"
                        + "<p>임시비밀번호: <strong>" + password + "</strong></p>"
                        + "<p>확인 후 꼭 메일을 삭제해주세요.</p>"
                        + "</body>"
                        + "</html>";
            default:
                return null;
        }
    }

    private SendEmailLog getSendEmailLog(String email, String subject, String body, String username) {
        SendEmailLog sendEmailLog = SendEmailLog
                .builder()
                .email(email)
                .subject(subject)
                .body(body)
                .username(username)
                .build();

        return sendEmailLog;
    }
}
