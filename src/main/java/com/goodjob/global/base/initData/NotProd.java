package com.goodjob.global.base.initData;

import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.member.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Profile("local")
public class NotProd {

    @Bean
    CommandLineRunner initData(MemberRepository memberRepository) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                Member member1 = Member
                        .builder()
                        .account("test-account")
                        .password("1234")
                        .nickname("test")
                        .build();

                memberRepository.save(member1);
            }
        };
    }
}
