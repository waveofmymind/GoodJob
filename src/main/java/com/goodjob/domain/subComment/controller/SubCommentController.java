package com.goodjob.domain.subComment.controller;

import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.service.ArticleService;
import com.goodjob.domain.comment.controller.CommentController;
import com.goodjob.domain.comment.dto.request.CommentRequestDto;
import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.comment.service.CommentService;
import com.goodjob.domain.subComment.dto.request.SubCommentRequestDto;
import com.goodjob.domain.subComment.entity.SubComment;
import com.goodjob.domain.subComment.service.SubCommentService;
import com.goodjob.global.base.rq.Rq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subComment")
public class SubCommentController {
    private final Rq rq;
    private final SubCommentService subCommentService;
    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createSubComment(Model model, @PathVariable("id") Long id, @Valid SubCommentRequestDto subCommentRequestDto, BindingResult bindingResult) {
        Comment comment = commentService.getComment(id);

        subCommentService.createSubComment(rq.getMember(), comment, subCommentRequestDto);

        return String.format("redirect:/article/detail/%s", comment.getArticle().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/{id}")
    public String updateSubComment(@Valid SubCommentRequestDto subCommentRequestDto, BindingResult bindingResult,
                                   @PathVariable("id") Long id, Principal principal) {
//        if (bindingResult.hasErrors()) {
//            return "answer_form";
//        }
        SubComment subComment = subCommentService.getSubComment(id);
//        if (!commentResponseDto.getMember().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
        subCommentService.updateComment(subComment, subCommentRequestDto.getContent());
        return String.format("redirect:/article/detail/%s", subComment.getComment().getArticle().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteSubComment(Principal principal, @PathVariable("id") Long id) {
        SubComment subComment = subCommentService.getSubComment(id);
//        if (!commentResponseDto.getMember().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
        subCommentService.deleteSubComment(subComment);
        return String.format("redirect:/article/detail/%s", subComment.getComment().getArticle().getId());
    }
}
