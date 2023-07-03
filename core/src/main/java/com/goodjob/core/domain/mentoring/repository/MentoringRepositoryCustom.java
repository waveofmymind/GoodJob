package com.goodjob.core.domain.mentoring.repository;


import com.goodjob.core.domain.article.entity.Article;
import com.goodjob.core.domain.mentoring.entity.Mentoring;

import java.util.List;
import java.util.Optional;

public interface MentoringRepositoryCustom {
    List<Mentoring> findQslBySearch(String category, String kw);
}
