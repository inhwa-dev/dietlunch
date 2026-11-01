package com.project.service;

import com.project.dto.DietDTO;
import com.project.dto.MemberDTO;
import java.util.List;
import java.util.stream.Collectors;

public class MatchService {

    public List<DietDTO> getMatchingDiets(MemberDTO member, List<DietDTO> all) {
        // 1차: 목표 / 끼니 기준 칼로리 / 탄단지 비율 모두 만족
        List<DietDTO> matching = all.stream()
            .filter(d -> d.getDietType().equalsIgnoreCase(member.getTargetGoal()))
            .filter(d -> Math.abs(d.getTotalCalories() - member.getTargetCalories() / 3.0) <= 200)
            .filter(d -> checkRatio(d, member.getTargetGoal()))
            .collect(Collectors.toList());

        if (!matching.isEmpty()) {
            return matching;
        }

        // 2차: 입력된 목표에 맞는 상품이 있지만 영양비율 기준이 너무 엄격한 경우,
        //      목표와 칼로리를 우선하여 추천이 완전히 비지 않도록 보완
        matching = all.stream()
            .filter(d -> d.getDietType().equalsIgnoreCase(member.getTargetGoal()))
            .filter(d -> Math.abs(d.getTotalCalories() - member.getTargetCalories() / 3.0) <= 200)
            .collect(Collectors.toList());

        if (!matching.isEmpty()) {
            return matching;
        }

        // 3차: 목표 칼로리가 상품 데이터의 범위를 벗어나면 동일 목표의 메뉴 제시
        return all.stream()
            .filter(d -> d.getDietType().equalsIgnoreCase(member.getTargetGoal()))
            .collect(Collectors.toList());
    }

    private boolean checkRatio(DietDTO d, String goal) {
        double total = (d.getCarbs() * 4.0) + (d.getProtein() * 4.0) + (d.getFat() * 9.0);
        if (total <= 0) return false;

        double carbRatio = (d.getCarbs() * 4.0) / total;
        double proteinRatio = (d.getProtein() * 4.0) / total;

        return goal.equalsIgnoreCase("BULK") ? (carbRatio >= 0.45 && proteinRatio >= 0.25) :
               goal.equalsIgnoreCase("DIET") ? (proteinRatio >= 0.40 && carbRatio <= 0.40) : true;
    }
}
