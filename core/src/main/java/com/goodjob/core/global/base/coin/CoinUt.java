package com.goodjob.core.global.base.coin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CoinUt {

    public final static int MAX_COIN_COUNT = 10;
    private final MemberService memberService;

    private final ObjectMapper objectMapper;

    public boolean isServiceAvailable(Member member) {
        // 유료 회원인 경우 코인 없이 이용 가능
        if (!member.isFree()) {
            return true;
        }

        Integer coin = member.getCoin();

        if (coin < 1) {
            return false;
        }

        memberService.deductCoin(member); // 코인 감소

        return true;
    }

    @Scheduled(cron = "${scheduler.cron.job}", zone = "Asia/Seoul")
    public void updateCoins() {
        log.info("코인 초기화");
        // 모든 free 회원의 coin을 10으로 초기화
        memberService.updateCoins();
    }

    @KafkaListener(topics = "TOPIC_ERROR", groupId = "errorgroup")
    public void recoverCoin(String memberId) throws JsonProcessingException {
        Long id = Long.parseLong(objectMapper.readValue(memberId, String.class));
        memberService.recoverCoins(id);
    }
    
}
