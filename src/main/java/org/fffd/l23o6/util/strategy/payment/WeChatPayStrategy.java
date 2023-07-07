package org.fffd.l23o6.util.strategy.payment;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
// import com.wechat.pay.java.service.payments.h5.model.Amount;
import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
// import com.wechat.pay.java.service.payments.h5.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.AmountReq;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.Refund;



public class WeChatPayStrategy extends PaymentStrategy{
    public static final WeChatPayStrategy INSTANCE = new WeChatPayStrategy();

    private WeChatPayStrategy () {

    }

    /** 商户号 */
    public static String merchantId = "";
    /** 商户API私钥路径 */
    public static String privateKeyPath = "";
    /** 商户证书序列号 */
    public static String merchantSerialNumber = "";
    /** 商户APIV3密钥 */
    public static String apiV3key = "";

    public static String wechatPayCertificatePath = "";


    @Override
    public boolean pay(Long orderId, double price) {
        Config config = new RSAAutoCertificateConfig.Builder()
                .merchantId(merchantId)
                .privateKeyFromPath(privateKeyPath)
                .merchantSerialNumber(merchantSerialNumber)
                .apiV3Key(apiV3key)
                .build();
        // 使用自动更新平台证书的RSA配置
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错

        // 构建 service
        NativePayService service = new NativePayService.Builder().config(config).build();

        // request.setXxx(val) 设置所需参数，具体参数可见 Request定义
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();  // 总金额 说明：订单总金额，单位为分
        int price_integer = (int) Math.round(price * 100);
        amount.setTotal(price_integer);

        request.setAmount(amount);
        request.setOutTradeNo(orderId + "");
        request.setAppid("wxa9d9651ae******");
        request.setMchid("190000****");
        request.setDescription("测试商品标题");
        request.setNotifyUrl("https://notify_url");
        // 调用下单方法，得到应答
        PrepayResponse response = service.prepay(request);
        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
        System.out.println(response.getCodeUrl());

        return true;
    }

    @Override
    public boolean refund(Long orderId, double price) {
        // 初始化商户配置
        RSAConfig config =
                new RSAConfig.Builder()
                        .merchantId(merchantId)
                        // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
                        .privateKeyFromPath(privateKeyPath)
                        .merchantSerialNumber(merchantSerialNumber)
                        .wechatPayCertificatesFromPath(wechatPayCertificatePath)
                        .build();

        // 初始化服务
        RefundService service = new RefundService.Builder().config(config).build();
        // ... 调用接口
        try {
            Refund response = createRefund(service, orderId, price);
            System.out.println("Refund: " + response.getOutRefundNo() + "money: " + response.getAmount());
            return true;
        } catch (Exception e) {
            System.out.println("refund failed");
            e.printStackTrace();
        }
        return false;
    }

    public static Refund createRefund(RefundService service, Long orderId, double price) {
        CreateRequest request = new CreateRequest();
        AmountReq amount = new AmountReq();  // 总金额 说明：订单总金额，单位为分
        Long price_integer = Math.round(price * 100);
        amount.setTotal(price_integer);

        request.setAmount(amount);
        request.setOutTradeNo(orderId + "");
        request.setReason("i do not like the goods");
        request.setNotifyUrl("https://notify_url");

        return service.create(request);
    }
}
