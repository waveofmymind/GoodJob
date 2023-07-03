package com.goodjob.api.controller.member;

import com.goodjob.core.domain.article.entity.Article;
import com.goodjob.core.domain.article.service.ArticleService;
import com.goodjob.core.domain.comment.entity.Comment;
import com.goodjob.core.domain.comment.service.CommentService;
import com.goodjob.core.domain.member.dto.request.EditRequestDto;
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
import java.util.Optional;

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
        RsData loginRsData = memberService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        if (loginRsData.isFail()) {
            return rq.historyBack(loginRsData);
        }

        rq.setCookie("accessToken", (String) loginRsData.getData());

        Cookie previousUrlCookie = rq.getCookie("previousUrl");

        if (previousUrlCookie != null) {
            String previousUrl = rq.getPreviousUrl(previousUrlCookie);

            return rq.redirectWithMsg(previousUrl, loginRsData);
        }

        log.debug("로그인 요청한 유저id ={}", rq.getMember().getId());
        return rq.redirectWithMsg("/", loginRsData);
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public String logout() {
        String userId = String.valueOf(rq.getMember().getId());
        // 레디스에서 리프레시토큰삭제
        redisUt.deleteToken(userId);
        // 쿠키삭제
        rq.expireCookie("accessToken");

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

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public String showEdit(EditRequestDto editRequestDto) {
        return "member/edit";
    }

    @PatchMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public RsData<String> edit(EditRequestDto editRequestDto) {
        return memberService.update(rq.getMember(), editRequestDto);
    }

    @GetMapping("/edit/confirm/password")
    public String showConfirmPassword() {
        return "member/confirm-password";
    }

    @PostMapping("/edit/confirm/password")
    public String confirmPassword(String passwordToEdit) {
        String memberPassword = rq.getMember().getPassword();
        RsData<String> matchRsData = memberService.matchPassword(passwordToEdit, memberPassword);

        if (matchRsData.isFail()) {
            return rq.redirectWithMsg("/member/me", matchRsData);
        }

        return "redirect:/member/edit";
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable Long id) {
        memberService.delete(id);

        return "member/join";
    }

    @GetMapping("/show/comments")
    public String showComments() {
        return "member/myComments";
    }

    @PostMapping("/join/valid/username")
    @ResponseBody
    public RsData<String> checkDuplicateUsername(String username) {
        if (username.length() < 4) {
            return RsData.of("F-1", "4자 이상 입력하세요.");
        }

        Optional<Member> opMember = memberService.findByUsername(username);
        if (opMember.isPresent()) {
            return RsData.of("F-1", "이미 사용중인 아이디입니다.");
        }

        return RsData.of("S-1", "사용 가능한 아이디입니다.");
    }

    @PostMapping("/join/valid/nickname")
    @ResponseBody
    public RsData checkDuplicateNickname(String nickname) {
        if (nickname.length() < 2) {
            return RsData.of("F-1", "2자 이상 입력하세요.");
        }

        Optional<Member> opMember = memberService.findByNickName(nickname);
        if (opMember.isPresent()) {
            return RsData.of("F-1", "이미 사용중인 닉네임입니다.");
        }

        return RsData.of("S-1", "사용 가능한 닉네임입니다.");
    }

    @GetMapping("/applyMentor")
    public String applyMentor() {
        memberService.applyMentor(rq.getMember());

        return "redirect:/mentoring/list";
    }
}
