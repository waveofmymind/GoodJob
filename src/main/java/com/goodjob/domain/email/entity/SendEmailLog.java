package com.goodjob.domain.email.entity;

import com.goodjob.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.checkerframework.common.aliasing.qual.Unique;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendEmailLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String resultCode;
    private String message;
    private String email;
    private String subject;
    private String body;
    private LocalDateTime sendEndDate;
    private LocalDateTime failDate;
    @Unique
    private String verificationCode;
}
