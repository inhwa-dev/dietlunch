import com.project.dto.DietDTO;
import com.project.dto.MemberDTO;

import java.util.*;

/**
 * 식단/상품 시스템 (위승현 담당)
 * - DietDTO.dietType 값은 MemberDTO.targetGoal과 매칭되도록 "BULK" / "DIET" / "KEEP" 세 가지로 통일
 * - 각 타입마다 도시락 메뉴 3개씩, 총 9개 등록
 * - 회원(MemberDTO)의 targetGoal을 받아서 맞는 식단 목록을 추천
 */
public class MealPlanSystem {

    // 식단 분류 상수 (MemberDTO.targetGoal과 동일한 문자열을 사용)
    public static final String BULK = "BULK";
    public static final String DIET = "DIET";
    public static final String KEEP = "KEEP";

    // 가격은 9개 메뉴 평균값으로 통일
    private static final int UNIFIED_PRICE = 8600;

    // 식단 타입 -> 메뉴 3개 매핑
    private final Map<String, List<DietDTO>> dietsByType = new LinkedHashMap<>();

    public MealPlanSystem() {
        // DietDTO(dietId, dietName, totalCalories, protein, carbs, fat, dietType, price)
        dietsByType.put(BULK, Arrays.asList(
                new DietDTO("BULK-01", "닭가슴살 스테이크 도시락", 750, 55, 70, 20, BULK, UNIFIED_PRICE), // 현미밥, 브로콜리, 고구마
                new DietDTO("BULK-02", "소불고기 덮밥", 820, 48, 90, 25, BULK, UNIFIED_PRICE), // 잡곡밥, 계란후라이, 나물반찬
                new DietDTO("BULK-03", "연어 스테이크 & 아보카도 샐러드", 780, 50, 55, 35, BULK, UNIFIED_PRICE) // 퀴노아 포함
        ));

        dietsByType.put(DIET, Arrays.asList(
                new DietDTO("DIET-01", "닭가슴살 샐러드 도시락", 380, 35, 25, 12, DIET, UNIFIED_PRICE), // 방울토마토, 오이, 발사믹 드레싱
                new DietDTO("DIET-02", "두부 스테이크 & 채소볶음", 420, 28, 35, 15, DIET, UNIFIED_PRICE), // 현미밥 소량
                new DietDTO("DIET-03", "오징어 초무침 & 곤약면", 350, 25, 30, 10, DIET, UNIFIED_PRICE) // 저탄수 구성
        ));

        dietsByType.put(KEEP, Arrays.asList(
                new DietDTO("KEEP-01", "잡곡밥 & 제육볶음", 600, 32, 65, 20, KEEP, UNIFIED_PRICE), // 나물 반찬 3종
                new DietDTO("KEEP-02", "닭갈비 도시락", 620, 38, 60, 22, KEEP, UNIFIED_PRICE), // 현미밥, 김치, 계란찜
                new DietDTO("KEEP-03", "비빔밥 스타일 도시락", 580, 30, 70, 15, KEEP, UNIFIED_PRICE) // 각종 나물, 고추장 소스, 계란후라이
        ));
    }

    // 특정 식단 타입의 메뉴 3개 조회
    public List<DietDTO> getDiets(String dietType) {
        List<DietDTO> diets = dietsByType.get(dietType);
        if (diets == null) {
            throw new IllegalArgumentException("존재하지 않는 dietType 입니다: " + dietType);
        }
        return diets;
    }

    // 식단 타입 + 메뉴 번호(1~3)로 선택
    public DietDTO selectDiet(String dietType, int choice) {
        List<DietDTO> diets = getDiets(dietType);
        if (choice < 1 || choice > diets.size()) {
            throw new IllegalArgumentException("1~" + diets.size() + " 사이의 번호를 선택하세요.");
        }
        return diets.get(choice - 1);
    }

    // 회원(MemberDTO)의 targetGoal에 맞는 식단 추천
    // - dietType이 같은 메뉴들을 목표 칼로리(targetCalories)에 가까운 순으로 정렬해서 반환
    public List<DietDTO> recommendForMember(MemberDTO member) {
        List<DietDTO> candidates = new ArrayList<>(getDiets(member.getTargetGoal()));
        candidates.sort(Comparator.comparingInt(
                d -> Math.abs(d.getTotalCalories() - member.getTargetCalories())
        ));
        return candidates;
    }

    // 콘솔에서 테스트해보기 위한 메인
    public static void main(String[] args) {
        MealPlanSystem system = new MealPlanSystem();

        // 예시 회원: 벌크업 목표, 목표 칼로리 800kcal
        MemberDTO member = new MemberDTO("mhs01", 178.0, 68.0, MealPlanSystem.BULK, 800);

        System.out.println("회원 정보 -> " + member);

        List<DietDTO> recommended = system.recommendForMember(member);
        System.out.println("\n[" + member.getTargetGoal() + " 추천 식단 (목표 칼로리에 가까운 순)]");
        for (int i = 0; i < recommended.size(); i++) {
            System.out.println((i + 1) + ". " + recommended.get(i));
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("\n메뉴 번호를 선택하세요 (1~" + recommended.size() + "): ");
        int choice = sc.nextInt();
        DietDTO selected = recommended.get(choice - 1);

        System.out.println("\n선택 완료 -> " + selected);
        sc.close();
    }
} 
