import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MealScheduler {

    // 식사 시간
    enum MealTime {
        BREAKFAST("아침"),
        LUNCH("점심"),
        DINNER("저녁");

        private final String label; // 표시될 이름

        MealTime(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    // 하루 식단 스케줄
    static class DailySchedule {

        private LocalDate date; // 식단 날짜

        private MealPlanSystem.Meal breakfast; 
        private MealPlanSystem.Meal lunch;
        private MealPlanSystem.Meal dinner;

        public DailySchedule(
                LocalDate date,
                MealPlanSystem.Meal breakfast,
                MealPlanSystem.Meal lunch,
                MealPlanSystem.Meal dinner) {

            this.date = date;
            this.breakfast = breakfast;
            this.lunch = lunch;
            this.dinner = dinner;
        }

        public LocalDate getDate() {
            return date;
        }

        public MealPlanSystem.Meal getBreakfast() {
            return breakfast;
        }

        public MealPlanSystem.Meal getLunch() {
            return lunch;
        }

        public MealPlanSystem.Meal getDinner() {
            return dinner;
        }

        // 하루 총 칼로리
        public int getTotalCalories() {

            return breakfast.getKcal()
                    + lunch.getKcal()
                    + dinner.getKcal();
        }

        // 하루 총 단백질
        public int getTotalProtein() {

            return breakfast.getProtein()
                    + lunch.getProtein()
                    + dinner.getProtein();
        }

        // 하루 총 탄수화물
        public int getTotalCarbs() {

            return breakfast.getCarbs()
                    + lunch.getCarbs()
                    + dinner.getCarbs();
        }

        // 하루 총 지방
        public int getTotalFat() {

            return breakfast.getFat()
                    + lunch.getFat()
                    + dinner.getFat();
        }

        // 하루 총 나트륨
        public int getTotalSodium() {

            return breakfast.getSodium()
                    + lunch.getSodium()
                    + dinner.getSodium();
        }

        @Override
        public String toString() {

            return String.format(
                    "%s (%s)%n" +
                    "  아침 : %s%n" +
                    "  점심 : %s%n" +
                    "  저녁 : %s%n" +
                    "  ----------------------------%n" +
                    "  하루 총 칼로리 : %d kcal%n" +
                    "  하루 총 단백질 : %d g%n" +
                    "  하루 총 탄수화물 : %d g%n" +
                    "  하루 총 지방 : %d g%n" +
                    "  하루 총 나트륨 : %d mg%n",

                    date.format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    ),

                    getKoreanDay(date.getDayOfWeek()),

                    breakfast,
                    lunch,
                    dinner,

                    getTotalCalories(),
                    getTotalProtein(),
                    getTotalCarbs(),
                    getTotalFat(),
                    getTotalSodium()
            );
        }

        private String getKoreanDay(DayOfWeek day) {

            return switch (day) {

                case MONDAY -> "월요일";
                case TUESDAY -> "화요일";
                case WEDNESDAY -> "수요일";
                case THURSDAY -> "목요일";
                case FRIDAY -> "금요일";
                case SATURDAY -> "토요일";
                case SUNDAY -> "일요일";
            };
        }
    }


    // 식단 시스템
    private final MealPlanSystem mealPlanSystem;


    public MealScheduler(MealPlanSystem mealPlanSystem) {

        this.mealPlanSystem = mealPlanSystem;
    }


    // 스케줄 생성
    public List<DailySchedule> createSchedule(
            MealPlanSystem.DietType dietType,
            LocalDate startDate,
            int days) {

        if (startDate == null) {

            throw new IllegalArgumentException(
                    "시작 날짜를 입력해주세요."
            );
        }

        if (days <= 0) {

            throw new IllegalArgumentException(
                    "스케줄 기간은 1일 이상이어야 합니다."
            );
        }


        // 선택한 목표의 식단 가져오기
        List<MealPlanSystem.Meal> meals =
                mealPlanSystem.getMeals(dietType);


        if (meals == null || meals.size() < 3) {

            throw new IllegalArgumentException(
                    "하루 3끼를 구성하기 위한 식단이 부족합니다."
            );
        }


        List<DailySchedule> schedules =
                new ArrayList<>();

        // 날짜별 스케줄 생성
        for (int day = 0; day < days; day++) {

            LocalDate date =
                    startDate.plusDays(day);


            /*
             * 메뉴 3개를 하루 3끼에 배정
             *
             * day 0
             * 아침 → 메뉴 1
             * 점심 → 메뉴 2
             * 저녁 → 메뉴 3
             *
             * day 1
             * 아침 → 메뉴 2
             * 점심 → 메뉴 3
             * 저녁 → 메뉴 1
             *
             * day 2
             * 아침 → 메뉴 3
             * 점심 → 메뉴 1
             * 저녁 → 메뉴 2
             *
             * 이후 다시 반복
             */

            int breakfastIndex = day % 3;
            int lunchIndex = (day + 1) % 3;
            int dinnerIndex = (day + 2) % 3;


            MealPlanSystem.Meal breakfast =
                    meals.get(breakfastIndex);

            MealPlanSystem.Meal lunch =
                    meals.get(lunchIndex);

            MealPlanSystem.Meal dinner =
                    meals.get(dinnerIndex);


            DailySchedule dailySchedule =
                    new DailySchedule(
                            date,
                            breakfast,
                            lunch,
                            dinner
                    );


            schedules.add(dailySchedule);
        }


        return schedules;
    }

    // 스케줄 출력
    public void printSchedule(
            List<DailySchedule> schedules) {

        System.out.println();
        System.out.println(
                "============================================"
        );

        System.out.println(
                "              식단 스케줄"
        );

        System.out.println(
                "============================================"
        );


        for (DailySchedule schedule : schedules) {

            System.out.println(schedule);
        }


        System.out.println(
                "============================================"
        );
    }