package com.goodjob.core.domain.member;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true)
@ComponentScan
@EnableAutoConfiguration
@Configuration
class MemberConfigurationLoader {
}