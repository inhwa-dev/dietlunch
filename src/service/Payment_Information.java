import com.project.dto.DietDTO;
import com.project.dto.MemberDTO;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.UUID; //??
// 사이트 -> 로그인(memberId,PW) -> 사이트(price) -> 카드결제 대행(paymentToken) -> 사이트(subscriptionId, autoRenew, nextBillingDate) -> 유저? 흐름 예상
public class Payment_Information {
    public static void main(String[] args){
        //Subscription객체 생성 -> 최초결제 확인 subscribe() -> 아닐경우 자동결제 날짜 확인 dateCheck()-> 자동결제 여부 확인  billing() ->
        System.out.println("테스트용, 고객의 계좌금액을 적어주세요: ");
        Scanner sc = new Scanner(System.in);
        int amountPrice = sc.nextInt();
        //여기에 실제로 데이터베이스의 정보를 읽는 기능이 있다면 코드가 따로 바뀌어야 하겠지.
        Subscription subscription = new Subscription(null, null, 0, null);

        subscription.setPrice(amountPrice);

        Test1_Payment PaymentGate = new Test1_Payment();
        SubscriptionService sub = new SubscriptionService(PaymentGate, subscription);
        if(subscription.getSubscriptionId() == null){
           subscription = sub.subscribe();
        } 
        if(dateCheck(subscription)){
            subscription = billing(subscription, PaymentGate);
        }
        

        System.out.println("결제완료된 고객정보: " + subscription.getMemberId() + ", " + subscription.getPrice() + ", 다음 결제일" + subscription.getNextBillingDate().getYear() + "년 " + subscription.getNextBillingDate().getMonth() + " "
            + subscription.getNextBillingDate().getDayOfMonth() + "일");
        sc.close();
    }

    //====월간 구독 해지 함수
    //public void cancelSubscription(Subscription subscription){
    //    subscription.cancel();
    //}

    //====자동 결제용 함수? 날짜 확인 함수가 필요할듯?

    public static Subscription billing(Subscription subscription, PaymentGateway paymentGateway1) {
        PaymentGateway paymentGateway = paymentGateway1;//paymentGateway 여기서 임시로 사용할 클래스?
        //자동결제 확인 자동결제 상태면 !true -> false여서 isSuccess로 넘어간다.
        if (!subscription.isAutoRenew()) {
            return subscription;
        }
        
        PaymentResult result = paymentGateway.charge(subscription);

        if (result.isSuccess()) {
            System.out.println("월간 결제 성공");
            // 다음 결제일 갱신
            subscription.setNextBillingDate(
                subscription.getNextBillingDate().plusMonths(1)
            );
            System.out.println("다음 결제일은 " + subscription.getNextBillingDate().getYear() + "년 " + subscription.getNextBillingDate().getMonth() + "월"
            + subscription.getNextBillingDate().getDayOfMonth() + "일 입니다.");
            return subscription;
        } else {
            System.out.println("월간 결제 실패");
            subscription.cancel();
            return subscription;
        }
    }
    /*==================================================================================== */
    //날짜 체크용 함수
    public static boolean dateCheck(Subscription subscription){
        LocalDate today = LocalDate.now();
            // 2. 목표로 하는 특정 날짜 설정 (예: 2026년 10월 5일)
        LocalDate targetDate = subscription.getNextBillingDate(); //Subscription클래스에서 다음 결제일 반환
        
        // 3. 날짜 비교 후 코드 실행
        if (today.isEqual(targetDate)) {
            System.out.println("목표 날짜가 되었습니다! 코드를 실행합니다.");
            return true;
        } else {return false;}
    }
}
//결제에 필요한 정보모아둔 클래스
/*memberId, price를 가져와서 어느고객인지 확인하기 + 고객이 얼마나 내야하는지 알 수 있음
autoRenew를 이용해서 자동결제 여부를 확인하고 자동결제 함수를 사용할지 아니면 최초 결제함수를 사용할지 결정해야함
nextBillingDate를 체크해서 subscriptionId 초기화?를 하거나 paymentToken초기화를 한다.
그외 기타등등 데이터를 반환하는 함수포함
*/
class Subscription {
    private int price; //가격, 초기값 = member DTO에서 가져오면 되나?
    private String memberId; //유저 아이디 = memberDTO에서 가져오면됨 어떻게 가져올까?
    private String subscriptionId; //구독 아이디, 초기값 = 널? (이거 왜 있지? 그냥 토큰이 고유값이어서 그거 쓰면 될것 같은데)

    // 실제 카드번호가 아니라 PG에서 발급한 토큰, 초기값 = 널, 결제가 한번 되면 이값을 받음
    //private String paymentToken;
    private boolean autoRenew = false; //자동 갱신 관련, 초기값 = 널?, 결제가 한번 되면 이값을 받음
    private LocalDate nextBillingDate; //다음 결제 날짜, 초기값 = 널?, 결제가 한번 되면 이값을 받음

    // 생성자
    public Subscription(
            String memberId,
            String subscriptionId,
            //String paymentToken,
            int price,
            LocalDate nextBillingDate
    ) {
        this.memberId = memberId; //회원가입하면 만들어질 듯
        this.subscriptionId = subscriptionId; //SubscriptionService의 subscribe 메서드에서 발급
        //this.paymentToken = paymentToken; //2026-10-13때 부터 수정됨 일단 Token관련 부분 전부 주석처리
        this.price = price; 
        this.autoRenew = true;
        this.nextBillingDate = nextBillingDate; //SubscriptionService의 subscribe 메서드에서 발급
    }
    //자동 갱신 해제
    public void cancel() {
        this.autoRenew = false;
    }
    //autoRenew조작용 메서드
    public void autoRenewOn(){
        this.autoRenew = true;
    }
    //현재 구독 활성 여부 체크
    public boolean isAutoRenew() {
        return autoRenew; 
    }
    //카드 결제를 위한 토큰 반환. 절대 카드 관련 정보를 직접 저장하지 않는다.
    //public String getPaymentToken() {
    //    return paymentToken;
    //}
    //고객이 결제해야할 가격 반환
    public int getPrice() {
        return price;
    }
    public void setPrice(int price){
        this.price = price;
    }
    //멤버 아이디 반환
    public String getMemberId(){
        return memberId;
    }
    //다음 자동결제일 반환
    public LocalDate getNextBillingDate(){
        return nextBillingDate;
    }
    //다음 자동결제일 조정용 메서드
    public void setNextBillingDate(LocalDate nextBillingDate){
        this.nextBillingDate = nextBillingDate;
    }
    public String getSubscriptionId(){
        return subscriptionId;
    }
}

//아직 하지 못한 것 데이터베이스에서 정보를 가져와서 사용하는 코드 (json이던, sql이던)
//결제 대행창 인터페이스(토큰, 가격)
/*charge 함수가 있음*/
interface PaymentGateway {
    public abstract PaymentResult charge(
        //String paymentToken, int price, Subscription subscription
        int price, Subscription subscription
    );
    public abstract PaymentResult charge(
        Subscription subscription
    );
}
//인터페이스 구현
class Test1_Payment implements PaymentGateway{
    @Override 
    //PaymentGateway -> PaymentResult (String, int)데이터가 옮겨가야함, 카드대행사에서 데이터를 받아야함
    //여기 int price는 카드사에서 고객의 통장에서 결제된 금액을 보내주는 용도일까?
    //=====================아직 미완성, 어떻게 만들지는 상의해보자=======================
    public PaymentResult charge(//String paymentToken, int price, Subscription subscription
        int price, Subscription subscription
    )
    {
        //MealPlanSystem에서 결정된 가격을 여기서 사용
        int UNIFIED_PRICE = 8600;
        if(price < UNIFIED_PRICE){
            throw new RuntimeException("결제 불가, 계좌금액 부족");
        }
        subscription.setPrice(price - UNIFIED_PRICE);
        price = UNIFIED_PRICE;

        return new PaymentResult(//paymentToken, 
            price, subscription);
    }
    //charge함수 두 종류로 overload해야할 듯 (토큰 = 이미 할당된 사람 있음, 가격 = 이미 확정된 사람있음, Subscription아무튼 넣어두기)
    public PaymentResult charge(Subscription subscription){
        //여기에 paymentToken, price이미 결정된 사람은 조작이 필요없다.
        int UNIFIED_PRICE = 8600;
        int price = subscription.getPrice();
        if(price < UNIFIED_PRICE){
            throw new RuntimeException("결제 불가, 계좌금액 부족");
        }
        subscription.setPrice(price - UNIFIED_PRICE);
        return new PaymentResult(//null, 
            UNIFIED_PRICE, subscription);
    }
    //============================ 아직 미완 ======================
}
//PaymentResult class구현 필요, charge, isSuccess 메서드 구현 필요
/*하고 싶은 것 */
class PaymentResult{
    //PaymentGateway클래스의 charge함수(카드사에서 주는 데이터를 받는 함수)에서 받아온 paymentToken을 Subscription의 paymentToken과 비교
    //String paymentToken;
    //PaymentGateway클래스의 charge함수에서 받아온 price(카드사에서 준 고객의 통장?)를 Subscription의 paymentToken과 비교
    int price;
    //해당 클래스(특히 isSuccess메서드)를 호출한 클래스의 데이터를 담아두는 필드
    Subscription subscription;
    //생성자
    PaymentResult(//String paymentToken, 
        int price, Subscription subscription){
        //this.paymentToken = paymentToken;
        this.price = price;
        this.subscription = subscription;
    }
    //charge로 받은 데이터를 isSuccess에서 판독용으로 사용해야 할듯
    //미리 설정해둔 price보다 값이 큰지, 서버에서 가지고 있는 토큰과 지금 가지고 있는 토큰이 같은지 확인?
    //if(paymentToken == Subscription sub.getPaymentToken and Subscription sub.getPrice() > price)
    //ㄴ>이경우에는 isSuccess함수가 Subscription 객체를 받아야함?
    //그냥 if()에다가 카드사에서 받은 정보와 현재 객체의 필드하고 비교?
    public boolean isSuccess(){
        
        //여기서 Subscription 에서 가져온 값이랑 비교를 해야할 듯
        //if(paymentToken.equals(subscription.getPaymentToken())){
        //    if(price == subscription.getPrice()){ //결제가격확인 
        //        return false;
        //    }
        //    return true;
        //}else{
        //    return false;
        //}
        //가격만 비교해서 결제 성공, 실패 정함
        if(price == subscription.getPrice()){
            return false;
        }
        return true;
    }
    //받아온 토큰 반환용 메서드
    //public String getPaymentToken(){
    //    return paymentToken;
    //}
}

//결제 서비스
/* 최초 결제를 담당하는 메서드가 있으니 Subscription클래스의 데이터와 PaymentGateway에서 받아오는 데이터를 이용해야함
*/
class SubscriptionService {
    Subscription subscription;
    PaymentGateway paymentGateway; // 다른 곳에서 정의된 것임
    //생성자
    public SubscriptionService(PaymentGateway paymentGateway, Subscription subscription) {
        this.paymentGateway = paymentGateway;
        this.subscription = subscription;
    }
    //최초 결제시 사용하는 함수 -> 최초 결제인지 아닌지 확인할 방법은? 해당 메서드 바깥쪽에 if문 하나 있어야 할듯
    public Subscription subscribe(){
        //카드 결제사에서 데이터를 받아오는 메서드
        PaymentResult result =
                paymentGateway.charge(
                        //paymentToken = null,
                        subscription.getPrice(),
                        subscription
                );
        //결제 성공여부 판독
        if (!result.isSuccess()) {
            throw new RuntimeException("결제 실패");
        }

        // 결제 성공 후 구독 생성, 자동으로 랜덤한 코드가 만들어지는 듯
        String subscriptionId = UUID.randomUUID().toString();
        System.out.println("subID 생성:" + subscriptionId);
        String memberId = subscription.getMemberId();
        //String paymentToken = result.getPaymentToken();
        int price = subscription.getPrice();

        //모든 과정이 끝나고 이를 받아줄 Subscription 객체 필요
        return new Subscription(
                memberId,
                subscriptionId,
                //paymentToken,
                price,
                LocalDate.now().plusMonths(1) //현재 날짜에서 아마 1개월 설정인듯
        );
    }

}
//만들어야 하는 함수: 데이터베이스에서 Subscription클래스가 사용할 데이터 몇개 가져오는 함수(데이터 베이스가 어떤 구조인지 물어보고 해봐야겠다.)
//                   결제 성공한 고객 데이터 데이터베이스에 저장하는 함수
/*만들어진 것: 회원데이터를 관리하는 Subscription클래스
              자동 결제용 함수 billing, 최초결제용 메서드 SubscriptionService클래스의 subscribe 메서드
              결제 결과 확인용 PaymentResult클래스
              자동결제일을 감지하는 지속되는 dateCheck함수
              charge함수 대강 구현
*/
