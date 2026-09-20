public class DietDTO {
    private String dietId;        // 식단/상품 고유 ID
    private String dietName;      // 도시락 이름 (예: "닭가슴살 고구마 야채 세트")
    private int totalCalories;    // 총 칼로리 (kcal)
    private int protein;          // 단백질 (g)
    private int carbs;            // 탄수화물 (g)
    private int fat;              // 지방 (g)
    private String dietType;      // 식단 분류 (예: "BULK", "DIET", "KEEP") - MemberDTO의 targetGoal과 매칭될 값
    private int price;            // 가격 (결제 시스템 담당 배성우를 위해 필요)

    // 기본 생성자, 모든 필드를 포함하는 생성자
    // Getter 및 Setter 메서드 필수
}
