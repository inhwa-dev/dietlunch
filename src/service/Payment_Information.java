
import java.time.LocalDate; //뭔지 알아봐야 할 듯
import java.util.UUID; //??
// 사이트 -> 로그인(memberId,PW) -> 사이트(price) -> 카드결제 대행(paymentToken) -> 사이트(subscriptionId, autoRenew, nextBillingDate) -> 유저? 흐름 예상
public class Payment_Information {
    public static void main(String[] args){

    }

    //====월간 구독 해지 함수
    public void cancelSubscription(Subscription subscription){
        subscription.cancel();
    }

    //====자동 결제용 함수? 날짜 확인 코드가 필요할듯?
    public void billing(Subscription subscription) {
        //자동결제 확인
        if (!subscription.isAutoRenew()) {
            return;
        }

        PaymentResult result =
                paymentGateway.charge(
                        subscription.getPaymentToken(),
                        subscription.getPrice()
                );

        if (result.isSuccess()) {
            System.out.println("월간 결제 성공");

            // 다음 결제일 갱신
            // subscription.setNextBillingDate(
            //     subscription.getNextBillingDate().plusMonths(1)
            // );
        } else {
            System.out.println("월간 결제 실패");
        }
    }
}
//아직 하지 못한 것 데이터베이스에서 정보를 가져와서 사용하는 코드 (json이던, sql이던)
//결제 대행창 인터페이스(토큰, 가격)
interface PaymentGateway {
    public abstract PaymentResult charge(
        String paymentToken, int price
    );
}
//인터페이스 구현
class Test1_Payment implements PaymentGateway{
    @Override 
    //PaymentGateway -> PaymentResult (String, int)데이터가 옮겨가야함, 카드대행사에서 데이터를 받아야함
    public PaymentResult charge(String paymentToken, int price){
        //대충 paymentToken = 카드대행사에서 받아온 토큰;
        //대충 price = 카드사에서 받은 금액?// 코드;
        return new PaymentResult(paymentToken, price);
    }
    
}
//PaymentResult class구현 필요, charge, isSuccess 메서드 구현 필요
class PaymentResult{
    String paymentToken;
    int price;
    //생성자
    PaymentResult(String paymentToken,int price){
        this.paymentToken = paymentToken; //charge 함수에서 받아온 (카드사에서 준 값일거임) 값일 경우가 대부분
        this.price = price;
    }
    //charge로 받은 데이터를 isSuccess에서 판독용으로 사용해야 할듯
    //미리 설정해둔 price보다 값이 큰지, 서버에서 가지고 있는 토큰과 지금 가지고 있는 토큰이 같은지 확인?
    //if(paymentToken == Subscription sub.getPaymentToken and Subscription sub.getPrice() > price)
    //ㄴ>이경우에는 isSuccess함수가 Subscription 객체를 받아야함?
    //그냥 if()에다가 카드사에서 받은 정보와 현재 객체의 필드하고 비교?
    public boolean isSuccess(){
        int payPrice = 9900; //여기는 결제가격일듯
        //여기서 Subscription 에서 가져온 값이랑 비교를 해야할 듯
        if(paymentToken.equalsIgnoreCase()){
            if(price < payPrice){ //결제가격확인
                continue;
            }
            return true;
        }else{
            return false;
        }
    }
}
//결제에 필요한 정보
class Subscription {

    private Long memberId; //유저 아이디
    private String subscriptionId; //구독 아이디, 초기값 = 널?

    // 실제 카드번호가 아니라 PG에서 발급한 토큰, 초기값 = 널
    private String paymentToken;

    private int price; //가격, 초기값 = 널?
    private boolean autoRenew; //자동 갱신 관련, 초기값 = 널?
    private LocalDate nextBillingDate; //다음 결제 날짜, 초기값 = 널?

   // 생성자
    public Subscription(
            Long memberId,
            String subscriptionId,
            String paymentToken,
            int price,
            LocalDate nextBillingDate
    ) {
        this.memberId = memberId; //회원가입하면 만들어질 듯
        this.subscriptionId = subscriptionId; //SubscriptionService의 subscribe 메서드에서 발급
        this.paymentToken = paymentToken; //카드 대행 사이트에서 받아와야함. (API나 그런 것으로)
        this.price = price; //SubscriptionService의 subscribe 메서드에서 발급?
        this.autoRenew = true;
        this.nextBillingDate = nextBillingDate; //SubscriptionService의 subscribe 메서드에서 발급
    }

    public void cancel() {
        this.autoRenew = false; //자동 갱신 해제
    }

    public boolean isAutoRenew() {
        return autoRenew; //현재 구독 활성 여부
    }

    public String getPaymentToken() {
        return paymentToken; //카드 결제를 위한 토큰 반환. 절대 카드 관련 정보를 직접 저장하지 않는다.
    }

    public int getPrice() {
        return price; //가격 반환
    }
}
//결제 서비스
class SubscriptionService {

    private final PaymentGateway paymentGateway; // 다른 곳에서 정의된 것임
    //생성자
    public SubscriptionService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }
    //결제시 사용하는 함수
    public Subscription subscribe(Long memberId,String paymentToken){
        //int price = 9900; //가격인듯

        // 최초 결제용 함수
        PaymentResult result =
                paymentGateway.charge(
                        paymentToken,
                        price
                );

        if (!result.isSuccess()) {
            throw new RuntimeException("결제 실패");
        }

        // 결제 성공 후 구독 생성, 자동으로 랜덤한 코드가 만들어지는 듯
        String subscriptionId = UUID.randomUUID().toString();
        //모든 과정이 끝나고 이를 받아줄 Subscription 객체 필요
        return new Subscription(
                memberId,
                subscriptionId,
                paymentToken,
                price,
                LocalDate.now().plusMonths(1) //아마 1개월 설정인듯
        );
    }
}
