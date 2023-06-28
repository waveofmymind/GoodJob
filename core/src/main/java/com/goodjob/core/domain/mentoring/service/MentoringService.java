package com.goodjob.core.domain.mentoring.service;


import com.goodjob.core.domain.article.dto.request.ArticleRequestDto;
import com.goodjob.core.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.core.domain.article.entity.Article;
import com.goodjob.core.domain.article.mapper.ArticleMapper;
import com.goodjob.core.domain.article.repository.ArticleRepository;
import com.goodjob.core.domain.comment.entity.Comment;
import com.goodjob.core.domain.file.service.FileService;
import com.goodjob.core.domain.hashTag.service.HashTagService;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.mentoring.dto.request.MentoringRequestDto;
import com.goodjob.core.domain.mentoring.entity.Mentoring;
import com.goodjob.core.domain.mentoring.repository.MentoringRepository;
import com.goodjob.core.domain.subComment.entity.SubComment;
import com.goodjob.core.global.base.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentoringService {
    private final MentoringRepository mentoringRepository;

    public Page<Mentoring> findAll(int page) {
        Pageable pageable = PageRequest.of(page, 10);

        List<Mentoring> mentorings = mentoringRepository.findAll();


        return convertToPage(mentorings, pageable);
    }

    private Page<Mentoring> convertToPage(List<Mentoring> mentorings, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), mentorings.size());

        List<Mentoring> content = mentorings.subList(start, end);
        return new PageImpl<>(content, pageable, mentorings.size());
    }

    public RsData<Mentoring> getMentoring(Long id) {
        Optional<Mentoring> mentoringOp = mentoringRepository.findById(id);

        if(mentoringOp.isEmpty()) {
            return RsData.of("F-1", "해당 멘토링이 존재하지 않습니다.");
        }

        Mentoring mentoring = mentoringOp.get();

        return RsData.of("S-1", "게시글에 대한 정보를 가져옵니다.", mentoring);
    }

    public RsData<Mentoring> createMentoring(Member member, MentoringRequestDto mentoringRequestDto) {
        if(mentoringRequestDto.getTitle().trim().equals("")) {
            return RsData.of("F-1", "제목을 입력해야 합니다.");
        }

        if(mentoringRequestDto.getContent().trim().equals("")) {
            return RsData.of("F-2", "내용을 입력해야 합니다.");
        }

        if(mentoringRequestDto.getTitle().trim().length() > 30) {
            return RsData.of("F-3", "제목은 30자 이내로 작성해야 합니다.");
        }

        Mentoring mentoring = Mentoring
                .builder()
                .member(member)
                .title(mentoringRequestDto.getTitle())
                .content(mentoringRequestDto.getContent())
                .build();

        mentoringRepository.save(mentoring);

        return RsData.of("S-1", "게시글을 성공적으로 생성하였습니다.", mentoring);
    }

    public RsData<Mentoring> findById(Long id) {
        Optional<Mentoring> mentoringOp = mentoringRepository.findById(id);

        if(!mentoringOp.isPresent()) {
            return RsData.of("F-1", "해당 멘토링이 존재하지 않습니다.");
        }

        return RsData.of("S-1", "멘토링을 성공적으로 가져왔습니다.", mentoringOp.get());
    }
}