package com.goodjob.api.controller.member;

import com.goodjob.core.domain.article.service.ArticleService;
import com.goodjob.core.domain.comment.service.CommentService;
import com.goodjob.core.domain.member.dto.request.JoinRequestDto;
import com.goodjob.core.domain.member.dto.request.LoginRequestDto;
import com.goodjob.core.domain.member.dto.response.MemberContentDto;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.service.MemberService;
import com.goodjob.common.rsData.RsData;
import com.goodjob.core.global.rq.Rq;
import com.goodjob.resume.facade.PredictionFacade;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.goodjob.common.cookie.constant.CookieType.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final Rq rq;
    private final MemberService memberService;
    private final ArticleService articleService;
    private final CommentService commentService;
    private final PredictionFacade predictionFacade;

    @GetMapping("/join")
    @PreAuthorize("isAnonymous()")
    public String showJoin(JoinRequestDto joinRequestDto) {
        return "member/join";
    }

    @PostMapping("/join")
    @PreAuthorize("isAnonymous()")
    public String join(@Valid JoinRequestDto joinRequestDto,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/join";
        }

        if (!joinRequestDto.getPassword().equals(joinRequestDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "", "패스워드가 일치하지 않습니다.");

            return "member/join";
        }

        RsData<Member> joinRsData = memberService.join(joinRequestDto);
        if (joinRsData.isFail()) {
            return rq.historyBack(joinRsData);
        }

        return rq.redirectWithMsg("/member/login", joinRsData);
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String showLogin() {
        String referer = rq.getReferer();
        rq.setCookie("previousUrl", referer);

        return "member/login";
    }

    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String login(@Valid LoginRequestDto loginRequestDto) {
        RsData<Map<String, String>> loginRsData = memberService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        if (loginRsData.isFail()) {
            return rq.historyBack(loginRsData);
        }

        Map<String, String> tokens = loginRsData.getData();

        rq.setCookie(ACCESS_TOKEN.value(), tokens.get(ACCESS_TOKEN.value()));
        rq.setRefreshCookie(REFRESH_TOKEN.value(), tokens.get(REFRESH_TOKEN.value()));

        // 로그인 후 이전페이지로 이동하기 위한 url
        Cookie previousUrlCookie = rq.getCookie(PREVIOUS_URL.value());

        if (previousUrlCookie != null) {
            String previousUrl = rq.getPreviousUrl(previousUrlCookie);

            return rq.redirectWithMsg(previousUrl, loginRsData);
        }

        return rq.redirectWithMsg("/", loginRsData);
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public String logout() {
        rq.logout(rq.getMember().getId());

        return rq.redirectWithMsg("/", "로그아웃 되었습니다.");
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public String showMe(Model model) {
        Long id = rq.getMember().getId();

        MemberContentDto memberContent = memberService.getMemberContent(id);

        model.addAttribute("memberContent", memberContent);

        return "member/me";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable Long id) {
        memberService.softDelete(id);
        rq.logout(id);

        return rq.redirectWithMsg("/", "회원탈퇴 요청이 완료되었습니다.");
    }

    @GetMapping("/applyMentor")
    public String showApplyMentor() {
        return "member/applyMentor";
    }

    @PostMapping("/applyMentor")
    public String applyMentor(@RequestParam(name = "isMentor", required = false) boolean isMentor) {
        if (isMentor) {
            RsData<String> memberRsData = memberService.upgradeToMentorMembership(rq.getMember());

            return rq.redirectWithMsg("/mentoring/list", memberRsData);
        }

        return "redirect:/mentoring/list";
    }
}
