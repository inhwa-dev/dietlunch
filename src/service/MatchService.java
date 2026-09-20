package com.project.service;

import com.project.dto.DietDTO;
import com.project.dto.MemberDTO;
import java.util.ArrayList;
import java.util.List;

public class MatchService {

    /**
     * 회원의 목표와 정보를 바탕으로 적절한 식단 리스트를 매칭하여 반환합니다.
     * @param member 회원 정보 (키, 몸무게, 목표, 목표칼로리 등)
     * @param allDiets DB에 등록된 전체 식단/도시락 리스트
     * @return 조건에 부합하는 추천 식단 리스트
     */
    public List<DietDTO> getMatchingDiets(MemberDTO member, List<DietDTO> allDiets) {
        List<DietDTO> recommendedDiets = new ArrayList<>();
        
        // 1. 회원의 목표(BULK, DIET, KEEP) 가져오기
        String targetGoal = member.getTargetGoal();
        
        // 2. 회원의 목표 섭취 칼로리 가져오기 (허용 오차범위 세팅, 예: ±10%)
        int targetCal = member.getTargetCalories();
        int minCal = (int) (targetCal * 0.9);
        int maxCal = (int) (targetCal * 1.1);

        // 3. 전체 식단 리스트를 돌면서 조건에 맞는 도시락만 필터링
        for (DietDTO diet : allDiets) {
            
            // 조건 A: 회원의 목적(벌크업/다이어트)과 도시락의 분류가 일치하는가?
            boolean isGoalMatch = diet.getDietType().equals(targetGoal);
            
            // 조건 B: 도시락의 칼로리가 회원의 목표 칼로리 오차범위 내에 들어오는가?
            boolean isCalorieMatch = diet.getTotalCalories() >= minCal && diet.getTotalCalories() <= maxCal;

            // 두 조건을 모두 만족하면 추천 리스트에 추가
            if (isGoalMatch && isCalorieMatch) {
                recommendedDiets.add(diet);
            }
        }
        
        return recommendedDiets;
    }
}