package com.goodjob.core.domain.mentoring.service;


import com.goodjob.core.domain.mentoring.dto.request.MentoringRequestDto;
import com.goodjob.core.domain.mentoring.entity.Mentoring;
import com.goodjob.core.domain.mentoring.repository.MentoringRepository;
import com.goodjob.common.rsData.RsData;
import com.goodjob.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MentoringService {
    private final MentoringRepository mentoringRepository;

    public List<Mentoring> findAll() {
        return mentoringRepository.findAll();
    }

    public Page<Mentoring> findAll(int page, String category, String query) {
        Pageable pageable = PageRequest.of(page, 12);

        List<Mentoring> mentorings = mentoringRepository.findQslBySearch(category, query);


        return convertToPage(mentorings, pageable);
    }

    private Page<Mentoring> convertToPage(List<Mentoring> mentorings, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), mentorings.size());

        List<Mentoring> content = mentorings.subList(start, end);
        return new PageImpl<>(content, pageable, mentorings.size());
    }

    public RsData<Mentoring> getMentoring(Long id) {
        RsData<Mentoring> mentoringRsData = findById(id);

        if(mentoringRsData.isFail()) {
            return mentoringRsData;
        }

        Mentoring mentoring = mentoringRsData.getData();

        return RsData.of("S-1", "멘토링에 대한 정보를 가져옵니다.", mentoring);
    }

    public RsData<Mentoring> createMentoring(Member member, MentoringRequestDto mentoringRequestDto) {
        Mentoring mentoring = Mentoring
                .builder()
                .member(member)
                .title(mentoringRequestDto.getTitle())
                .content(mentoringRequestDto.getContent())
                .job(mentoringRequestDto.getJob())
                .career(mentoringRequestDto.getCareer())
                .currentJob(mentoringRequestDto.getCurrentJob())
                .preferredTime(mentoringRequestDto.getPreferredTime())
                .build();

        mentoringRepository.save(mentoring);

        return RsData.of("S-1", "멘토링을 성공적으로 생성하였습니다.", mentoring);
    }

    public RsData<Mentoring> findById(Long id) {
        Optional<Mentoring> mentoringOp = mentoringRepository.findById(id);

        if(mentoringOp.isEmpty()) {
            return RsData.of("F-1", "해당 멘토링이 존재하지 않습니다.");
        }

        return RsData.of("S-1", "멘토링을 성공적으로 가져왔습니다.", mentoringOp.get());
    }

    @Transactional
    public RsData<Mentoring> updateMentoring(Member member, Long id, MentoringRequestDto mentoringRequestDto) {
        RsData<Mentoring> mentoringRsData = getMentoring(id);

        if(mentoringRsData.isFail()) {
            return mentoringRsData;
        }

        Mentoring mentoring = mentoringRsData.getData();

        if(mentoring.getMember().getId() != member.getId()) {
            return RsData.of("F-3", "수정 권한이 없습니다.");
        }
        mentoring.update(mentoringRequestDto);

        return RsData.of("S-1", "멘토링이 수정되었습니다.", mentoring);
    }

    @Transactional
    public RsData<Mentoring> deleteMentoring(Member member, Long id) {
        RsData<Mentoring> mentoringRsData = getMentoring(id);

        if(mentoringRsData.isFail()) {
            return mentoringRsData;
        }

        Mentoring mentoring = mentoringRsData.getData();

        if(mentoring.getMember().getId() != member.getId()) {
            return RsData.of("F-3", "삭제 권한이 없습니다.");
        }

        mentoring.setDeleted(true);

        return RsData.of("S-1", "멘토링이 삭제되었습니다.", mentoring);
    }
}
