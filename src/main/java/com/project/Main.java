
package com.project;

import com.project.dto.*;
import com.project.service.*;
import com.project.service.MealPlanSystem.DietType;
import java.util.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MemberService service = new MemberService();

        while (true) {
            System.out.println("\n1. 회원가입  2. 로그인  3. 종료");
            int menu = sc.nextInt();

            if (menu == 1) {
                System.out.print("아이디: ");
                String id = sc.next();
                System.out.print("비밀번호: ");
                String pw = sc.next();
                System.out.print("키(cm): ");
                double h = sc.nextDouble();
                System.out.print("몸무게(kg): ");
                double w = sc.nextDouble();
                System.out.print("목표(BULK/DIET/KEEP): ");
                String goal = sc.next().toUpperCase();
                System.out.print("목표 칼로리: ");
                int cal = sc.nextInt();

                if (service.signup(id, pw, h, w, goal, cal))
                    System.out.println("회원가입 성공!");
                else
                    System.out.println("이미 존재하는 아이디입니다.");

            } else if (menu == 2) {
                System.out.print("아이디: ");
                String id = sc.next();
                System.out.print("비밀번호: ");
                String pw = sc.next();

                MemberDTO member = service.login(id, pw);

                if (member != null) {
                    System.out.println(member.getMemberId() + "님 로그인 성공!");

                    MealPlanSystem meals = new MealPlanSystem();

                    DietType type = switch (member.getTargetGoal()) {
                        case "BULK" -> DietType.BULK_UP;
                        case "DIET" -> DietType.DIET;
                        default -> DietType.MAINTAIN;
                    };

                    MatchService match = new MatchService();
                    List<DietDTO> recommended =
                        match.getMatchingDiets(member, meals.getDietList());

                    System.out.println("\n맞춤 추천 도시락");

                    if (recommended.isEmpty()) {
                        System.out.println("조건에 맞는 도시락이 없습니다.");
                    } else {
                        for (DietDTO d : recommended) {
                            System.out.println(d.getDietName() + " / "
                                + d.getTotalCalories() + "kcal / "
                                + d.getPrice() + "원");
                        }
                    }

                    System.out.print("\n식단 스케줄을 생성할까요? (1. 예 / 2. 아니요): ");
int answer = sc.nextInt();

if (answer == 1) {
    MealScheduler scheduler = new MealScheduler(meals);
    System.out.print("며칠 동안의 식단을 만들까요?: ");
    int days = sc.nextInt();

    if (days > 0)
        scheduler.printSchedule(scheduler.createSchedule(type, LocalDate.now(), days));
    else
        System.out.println("1일 이상 입력하세요.");
}

// 결제 기능
System.out.print("\n결제를 진행할까요? (1. 예 / 2. 아니요): ");
int payAnswer = sc.nextInt();


if (payAnswer == 1) {
    Payment_Information.startPayment(member.getMemberId());
}

                } else {
                    System.out.println("로그인 실패!");
                }

            } else if (menu == 3) {
                System.out.println("프로그램 종료");
                break;
            }
        }
    }
}
