package com.goodjob.api.controller.likes;


import com.goodjob.core.domain.likes.dto.request.LikesRequestDto;
import com.goodjob.core.domain.likes.entity.Likes;
import com.goodjob.core.domain.likes.service.LikesService;
import com.goodjob.core.global.base.rsData.RsData;
import com.goodjob.core.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikesController {

    private final LikesService likesService;
    private final Rq rq;

    @PostMapping("/like/article/{id}")
    public String likeArticle(@PathVariable("id") Long id) {
        LikesRequestDto likesRequestDto = new LikesRequestDto(id, null, null);

        RsData<Likes> likesRsData = likesService.likeArticle(rq.getMember(), likesRequestDto);

        if(likesRsData.isFail()) {
            return rq.historyBack(likesRsData);
        }

        return rq.redirectWithMsg("/article/detail/%s".formatted(id), likesRsData);
    }

    @PostMapping("/like/comment/{id}")
    public String likeComment(@PathVariable("id") Long id) {
        LikesRequestDto likesRequestDto = new LikesRequestDto(null, id, null);

        RsData<Likes> likesRsData = likesService.likeComment(rq.getMember(), likesRequestDto);

        if(likesRsData.isFail()) {
            return rq.historyBack(likesRsData);
        }

        return rq.redirectWithMsg("/article/detail/%s".formatted(likesRsData.getData().getComment().getArticle().getId()), likesRsData);
    }

    @PostMapping("/like/subComment/{id}")
    public String likeSubComment(@PathVariable("id") Long id) {
        LikesRequestDto likesRequestDto = new LikesRequestDto(null, null, id);

        RsData<Likes> likesRsData = likesService.likeSubComment(rq.getMember(), likesRequestDto);

        if(likesRsData.isFail()) {
            return rq.historyBack(likesRsData);
        }

        return rq.redirectWithMsg("/article/detail/%s".formatted(likesRsData.getData().getSubComment().getComment().getArticle().getId()), likesRsData);
    }
}
