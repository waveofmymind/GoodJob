package com.goodjob.core.domain.hashTag.service;


import com.goodjob.core.domain.article.entity.Article;
import com.goodjob.core.domain.hashTag.entity.HashTag;
import com.goodjob.core.domain.hashTag.repository.HashTagRepository;
import com.goodjob.core.domain.keyword.entity.Keyword;
import com.goodjob.core.domain.keyword.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashTagService {
    private final KeywordService keywordService;
    private final HashTagRepository hashTagRepository;

    @Transactional
    public void applyHashTags(Article article, String keywordContentsStr) {
        List<HashTag> oldHashTags = article.getHashTagList();

        List<String> keywordContents = Arrays.stream(keywordContentsStr.split(","))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        List<HashTag> needToDelete = new ArrayList<>();

        for (HashTag oldHashTag : oldHashTags) {
            boolean contains = keywordContents.stream().anyMatch(s -> s.equals(oldHashTag.getKeyword().getContent()));

            if (contains == false) {
                needToDelete.add(oldHashTag);
            }
        }

        for(HashTag hashTag : needToDelete) {
            article.getHashTagList().remove(hashTag);
            hashTagRepository.delete(hashTag);
        }


        keywordContents.forEach(keywordContent -> {
            saveHashTag(article, keywordContent);
        });
    }

    private HashTag saveHashTag(Article article, String keywordContent) {
        Keyword keyword = keywordService.save(keywordContent);

        Optional<HashTag> opHashTag = hashTagRepository.findByArticleIdAndKeywordId(article.getId(), keyword.getId());

        if (opHashTag.isPresent()) {
            return opHashTag.get();
        }

        HashTag hashTag = HashTag.builder()
                .article(article)
                .keyword(keyword)
                .build();

        hashTagRepository.save(hashTag);

        return hashTag;
    }
}
