package com.goodjob.mentoring.domain.mentoring.repository;


import com.goodjob.mentoring.domain.mentoring.entity.Mentoring;

import java.util.List;

public interface MentoringRepositoryCustom {
    List<Mentoring> findQslBySearch(String category, String kw);
}
