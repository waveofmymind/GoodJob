package com.goodjob.core.global.base.coin;

import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.global.base.rsData.RsData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoinUt {
    /**
     * TODO: 이력서, 예상 질문 요청시 코인갯수 확인
     * 프리미엄 권한 갖는 회원인 경우 코인제한 없애기 -> 업그레이드에서?
     *  코인이 충분한 경우에만 서비스 제공.
     *  코인 없는 경우 코인부족 알림창, 매일 4시 지급 알림(결제하기)
     */

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
