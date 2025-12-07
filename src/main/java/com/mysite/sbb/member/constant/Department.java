package com.mysite.sbb.member.constant;

import lombok.Getter;

@Getter
public enum Department {
    // HTML의 <option value="DIET"> 처럼 value 값과 영문 철자가 똑같아야 합니다.

    DIET("다이어트 (체지방 감량)"),
    BULKUP("벌크업 (근육량 증가)"),
    PROFILE("바디프로필 (단기간 집중)"),
    REHAB("체형교정/재활 (건강 회복)"),
    STAMINA("체력증진 (생존 운동)"),
    POWERLIFTING("파워리프팅 (3대 중량 증가)");

    private final String description;

    Department(String description) {
        this.description = description;
    }
}