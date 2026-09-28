package com.project.dto;

public class MemberDTO {
    private String memberId;      // 유저 고유 ID
    private double height;        // 키 (cm)
    private double weight;        // 몸무게 (kg)
    private String targetGoal;    // 목표 (예: "BULK", "DIET", "KEEP") 
    private int targetCalories;   // 목표 섭취 칼로리
    public String getMemberId() {
        return memberId;
    }
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        this.height = height;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public String getTargetGoal() {
        return targetGoal;
    }
    public void setTargetGoal(String targetGoal) {
        this.targetGoal = targetGoal;
    }
    public int getTargetCalories() {
        return targetCalories;
    }
    public void setTargetCalories(int targetCalories) {
        this.targetCalories = targetCalories;
    }

    // IDE 자동 생성 기능(Alt+Insert 등)을 이용해 Getter/Setter를 추가하십시오.
    // (만약 팀 전체가 Lombok을 쓴다면 클래스 위에 @Data 어노테이션만 붙이시면 됩니다.)
}
