package com.goodjob.article.domain.keyword.service;



import com.goodjob.article.domain.keyword.entity.Keyword;
import com.goodjob.article.domain.keyword.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordRepository keywordRepository;

    public Keyword save(String keywordContent) {
        Optional<Keyword> optKeyword = keywordRepository.findByContent(keywordContent);

        if (optKeyword.isPresent()) {
            return optKeyword.get();
        }

        Keyword keyword = Keyword
                .builder()
                .content(keywordContent.toLowerCase())
                .build();

        keywordRepository.save(keyword);

        return keyword;
    }
}
