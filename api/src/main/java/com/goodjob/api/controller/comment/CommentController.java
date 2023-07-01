package com.goodjob.api.controller.comment;


import com.goodjob.core.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.core.domain.comment.dto.request.CommentRequestDto;
import com.goodjob.core.domain.comment.dto.response.CommentResponseDto;
import com.goodjob.core.domain.comment.entity.Comment;
import com.goodjob.core.domain.comment.service.CommentService;
import com.goodjob.core.global.base.rsData.RsData;
import com.goodjob.core.global.rq.Rq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final Rq rq;

    @PostMapping("/create/{id}")
    public String createComment(@PathVariable("id") Long id, @Valid CommentRequestDto commentRequestDto) {

        RsData<Comment> commentRsData = commentService.createComment(rq.getMember(), id, commentRequestDto);

        if(commentRsData.isFail()) {
            return rq.historyBack(commentRsData);
        }

        return rq.redirectWithMsg("/article/detail/%s".formatted(id), commentRsData);
    }


    @PostMapping("/update/{id}")
    public String updateComment(@Valid CommentRequestDto commentRequestDto, @PathVariable("id") Long id) {
        RsData<Comment> updateRsData = commentService.updateComment(rq.getMember(), id, commentRequestDto);

        if(updateRsData.isFail()) {
            return rq.historyBack(updateRsData);
        }

        Comment comment = updateRsData.getData();

        return rq.redirectWithMsg("/article/detail/%s".formatted(comment.getArticle().getId()), updateRsData);
    }

    @GetMapping("/delete/{id}")
    public String deleteComment(@PathVariable("id") Long id) {
        RsData<Comment> commentRsData = commentService.deleteComment(rq.getMember(), id);

        if(commentRsData.isFail()) {
            return rq.historyBack(commentRsData);
        }

        Comment comment = commentRsData.getData();

        return rq.redirectWithMsg("/article/detail/%s".formatted(comment.getArticle().getId()), commentRsData);
    }

    @GetMapping("/show/list")
    public String showComments(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<CommentResponseDto> paging = commentService.findAllByMemberIdToPage(page, rq.getMember().getId());
        model.addAttribute("paging", paging);

        return "member/myArticles";
    }
}
