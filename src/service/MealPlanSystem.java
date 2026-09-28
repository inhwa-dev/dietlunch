public class MealPlanSystem {

    // 식단 타입
    enum DietType {
        BULK_UP("벌크업"),
        DIET("다이어트"),
        MAINTAIN("유지");

        private final String label;

        DietType(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    // 도시락 메뉴 (상품)
    static class Meal {
        private String name;
        private int kcal;
        private int protein; // g
        private int carbs; // 탄수화물 g
        private int fat; // 지방 g
        private int sodium; // 나트륨 mg
        private int price; // 원

        public Meal(String name, int kcal, int protein, int carbs, int fat, int sodium, int price) {
            this.name = name;
            this.kcal = kcal;
            this.protein = protein;
            this.carbs = carbs;
            this.fat = fat;
            this.sodium = sodium;
            this.price = price;
        }

        @Override
        public String toString() {
            return String.format(
                    "%-24s | %4dkcal | 탄%3dg 단%3dg 지%3dg | 나트륨%5dmg | %,d원",
                    name, kcal, carbs, protein, fat, sodium, price);
        }
    }

    // 식단 타입 -> 메뉴 3개 매핑
    private final Map<DietType, List<Meal>> mealsByType = new HashMap<>();

    public MealPlanSystem() {
        // 가격은 기존 9개 메뉴 평균값(8,600원)으로 통일
        final int UNIFIED_PRICE = 8600;

        // Meal(이름, 칼로리kcal, 단백질g, 탄수화물g, 지방g, 나트륨mg, 가격)
        mealsByType.put(DietType.BULK_UP, Arrays.asList(
                new Meal("닭가슴살 스테이크 도시락", 750, 55, 70, 20, 900, UNIFIED_PRICE), // 현미밥, 브로콜리, 고구마
                new Meal("소불고기 덮밥", 820, 48, 90, 25, 1000, UNIFIED_PRICE), // 잡곡밥, 계란후라이, 나물반찬
                new Meal("연어 스테이크 & 아보카도 샐러드", 780, 50, 55, 35, 750, UNIFIED_PRICE) // 퀴노아 포함
        ));

        mealsByType.put(DietType.DIET, Arrays.asList(
                new Meal("닭가슴살 샐러드 도시락", 380, 35, 25, 12, 600, UNIFIED_PRICE), // 방울토마토, 오이, 발사믹 드레싱
                new Meal("두부 스테이크 & 채소볶음", 420, 28, 35, 15, 700, UNIFIED_PRICE), // 현미밥 소량
                new Meal("오징어 초무침 & 곤약면", 350, 25, 30, 10, 850, UNIFIED_PRICE) // 저탄수 구성
        ));

        mealsByType.put(DietType.MAINTAIN, Arrays.asList(
                new Meal("잡곡밥 & 제육볶음", 600, 32, 65, 20, 950, UNIFIED_PRICE), // 나물 반찬 3종
                new Meal("닭갈비 도시락", 620, 38, 60, 22, 1050, UNIFIED_PRICE), // 현미밥, 김치, 계란찜
                new Meal("비빔밥 스타일 도시락", 580, 30, 70, 15, 900, UNIFIED_PRICE) // 각종 나물, 고추장 소스, 계란후라이
        ));
    }

    // 특정 식단 타입의 메뉴 3개 조회
    public List<Meal> getMeals(DietType type) {
        return mealsByType.get(type);
    }

    // 식단 타입 + 메뉴 번호(1~3)로 선택
    public Meal selectMeal(DietType type, int choice) {
        List<Meal> meals = mealsByType.get(type);
        if (choice < 1 || choice > meals.size()) {
            throw new IllegalArgumentException("1~" + meals.size() + " 사이의 번호를 선택하세요.");
        }
        return meals.get(choice - 1);
    }

    // 콘솔에서 테스트해보기 위한 메인
    public static void main(String[] args) {
        MealPlanSystem system = new MealPlanSystem();
        Scanner sc = new Scanner(System.in);

        System.out.println("식단을 선택하세요: 1.벌크업 2.다이어트 3.유지");
        int typeInput = sc.nextInt();
        DietType type = switch (typeInput) {
            case 1 -> DietType.BULK_UP;
            case 2 -> DietType.DIET;
            case 3 -> DietType.MAINTAIN;
            default -> throw new IllegalArgumentException("잘못된 입력입니다.");
        };

        List<Meal> meals = system.getMeals(type);
        System.out.println("\n[" + type.getLabel() + " 식단 메뉴]");
        for (int i = 0; i < meals.size(); i++) {
            System.out.println((i + 1) + ". " + meals.get(i));
        }

        System.out.print("\n메뉴 번호를 선택하세요 (1~3): ");
        int choice = sc.nextInt();
        Meal selected = system.selectMeal(type, choice);

        System.out.println("\n선택 완료 -> " + selected);
        sc.close();
    }
}