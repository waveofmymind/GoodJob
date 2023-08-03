package com.goodjob.common.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class ProbabilisticEarlyRecomputationConfig {

    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    public RandomDoubleGenerator randomDoubleGenerator(Random random) {
        return new RandomDoubleGenerator(random);
    }

    @RequiredArgsConstructor
    public static class RandomDoubleGenerator  {
        private final Random random;
        public double nextDouble() {
            return random.nextDouble();
        }
    }

}
