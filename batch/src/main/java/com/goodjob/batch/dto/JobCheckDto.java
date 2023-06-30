package com.goodjob.batch.dto;

public record JobCheckDto(
        String company, String subject,
        String url, String sector,
        int sectorCode, String createDate, int career) {
}
