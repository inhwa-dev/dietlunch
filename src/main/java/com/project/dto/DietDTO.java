package com.project.dto;

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
    
    public DietDTO(String id, String name, int cal, int p,
                   int c, int f, String type, int price) {
        dietId = id;
        dietName = name;
        totalCalories = cal;
        protein = p;
        carbs = c;
        fat = f;
        dietType = type;
        this.price = price;
    }

    public String getDietId() { return dietId; }
    public String getDietName() { return dietName; }
    public int getTotalCalories() { return totalCalories; }
    public int getProtein() { return protein; }
    public int getCarbs() { return carbs; }
    public int getFat() { return fat; }
    public String getDietType() { return dietType; }
    public int getPrice() { return price; }

}