package com.project.service;

import com.project.dto.DietDTO;
import com.project.dto.MemberDTO;
import java.util.List;
import java.util.stream.Collectors;

public class MatchService {

    public List<DietDTO> getMatchingDiets(MemberDTO m, List<DietDTO> all) {
        // for/if문 대신 Stream API로 조건 3가지를 한 번에 압축 필터링
        return all.stream()
            .filter(d -> d.getDietType().equalsIgnoreCase(m.getTargetGoal())) // 1. 목적 일치 (dietType == targetGoal)
            .filter(d -> d.getTotalCalories() >= m.getTargetCalories() * 0.9 && d.getTotalCalories() <= m.getTargetCalories() * 1.1) // 2. 목표 칼로리 ±10%
            .filter(d -> checkRatio(d, m.getTargetGoal())) // 3. 탄단지 비율 검증
            .collect(Collectors.toList());
    }

    private boolean checkRatio(DietDTO d, String goal) {
        // 탄수화물(4kcal), 단백질(4kcal), 지방(9kcal) 총합 계산
        double tot = (d.getCarbs() * 4) + (d.getProtein() * 4) + (d.getFat() * 9);
        if (tot == 0) return false; // 데이터 오류 방지
        
        double c = (d.getCarbs() * 4) / tot, p = (d.getProtein() * 4) / tot;

        // if-else문을 삼항 연산자(? :)로 한 줄 최적화
        return goal.equalsIgnoreCase("BULK") ? (c >= 0.45 && p >= 0.25) :
               goal.equalsIgnoreCase("DIET") ? (p >= 0.40 && c <= 0.40) : true;
    }
}