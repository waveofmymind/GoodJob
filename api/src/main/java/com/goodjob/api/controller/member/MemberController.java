package com.goodjob.api.controller.member;

import com.goodjob.core.domain.article.entity.Article;
import com.goodjob.core.domain.article.service.ArticleService;
import com.goodjob.core.domain.comment.entity.Comment;
import com.goodjob.core.domain.comment.service.CommentService;
import com.goodjob.core.domain.member.dto.request.JoinRequestDto;
import com.goodjob.core.domain.member.dto.request.LoginRequestDto;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.service.MemberService;
import com.goodjob.core.global.base.redis.RedisUt;
import com.goodjob.core.global.base.rsData.RsData;
import com.goodjob.core.global.rq.Rq;
import com.goodjob.resume.dto.response.ResponsePredictionDto;
import com.goodjob.resume.facade.PredictionFacade;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.goodjob.core.global.base.cookie.constant.CookieType.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

    private final Rq rq;
    private final MemberService memberService;
    private final ArticleService articleService;
    private final CommentService commentService;
    private final PredictionFacade predictionFacade;
    private final RedisUt redisUt;

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

        log.debug("로그인 요청한 유저 accessToken ={}", loginRsData.getData());
        return rq.redirectWithMsg("/", loginRsData);
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public String logout() {
        String userId = String.valueOf(rq.getMember().getId());

        // 레디스에서 리프레시토큰삭제
        redisUt.delete(userId);

        rq.logout();

        log.debug("로그아웃한 유저id ={}", userId);
        return rq.redirectWithMsg("/", "로그아웃 되었습니다.");
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public String showMe(Model model) {
        Long id = rq.getMember().getId();

        List<Article> articles = articleService.findAllByMemberId(id);
        List<Comment> comments = commentService.findAllByMemberId(id);
        List<ResponsePredictionDto> predictions = predictionFacade.getPredictions(id);

        model.addAttribute("articles", articles);
        model.addAttribute("comments", comments);
        model.addAttribute("predictions", predictions);

        return "member/me";
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable Long id) {
        memberService.delete(id);

        return "member/join";
    }

    @GetMapping("/applyMentor")
    public String showApplyMentor() {
        return "member/applyMentor";
    }

    @PostMapping("/applyMentor")
    public String applyMentor(@RequestParam(name = "isMentor", required = false) boolean isMentor) {
        if(isMentor) {
            RsData<String> memberRsData = memberService.upgradeToMentorMembership(rq.getMember());

            return rq.redirectWithMsg("/mentoring/list", memberRsData);
        }

        return "redirect:/mentoring/list";
    }
}
