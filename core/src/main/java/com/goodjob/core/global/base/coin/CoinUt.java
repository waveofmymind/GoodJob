package com.goodjob.core.global.base.coin;

import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CoinUt {

    public final static int MAX_COIN_COUNT = 10;
    private final MemberService memberService;

    public boolean isServiceAvailable(Member member) {
        // 유료 회원인 경우 코인 없이 이용 가능
        if (!member.getMembership().equals("free")) {
            return true;
        }

        Integer coin = member.getCoin();

        if (coin < 1) {
            return false;
        }

        memberService.deductCoin(member); // 코인 감소

        return true;
    }

    @Scheduled(cron = "${scheduler.cron.job}", zone = "Asia/Seoul")
    public void updateCoins() {
        log.info("코인 초기화");
        // 모든 free 회원의 coin을 10으로 초기화
        memberService.updateCoins();
    }
}
