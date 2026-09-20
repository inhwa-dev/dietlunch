package com.project.dto;

public class MemberDTO {
    private String memberId;      // 유저 고유 ID
    private double height;        // 키 (cm)
    private double weight;        // 몸무게 (kg)
    private String targetGoal;    // 목표 (예: "BULK", "DIET", "KEEP") 
    private int targetCalories;   // 목표 섭취 칼로리

    // IDE 자동 생성 기능(Alt+Insert 등)을 이용해 Getter/Setter를 추가하십시오.
    // (만약 팀 전체가 Lombok을 쓴다면 클래스 위에 @Data 어노테이션만 붙이시면 됩니다.)
}
