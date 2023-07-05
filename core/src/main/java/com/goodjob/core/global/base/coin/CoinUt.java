package com.goodjob.core.global.base.coin;

import com.goodjob.core.domain.member.entity.Member;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoinUt {

    public final static int MAX_COIN_COUNT = 10;

    @Transactional
    public boolean isServiceAvailable(Member member) {
        // 유료 회원인 경우 코인 없이 이용 가능
        if (!member.getUserRole().equals("free")) {
            return true;
        }

        Integer coin = member.getCoin();

        if (coin < 1) {
            return false;
        }

        member.deductCoin(); // 코인감소

        return true;
    }

}
