package org.fffd.l23o6.util.strategy.payment;



public class AlipayStrategy {
    public static final AlipayStrategy INSTANCE = new AlipayStrategy();

    private AlipayStrategy() {

    }

    private static final String APPID = "9021000123602110";
    // 沙箱唯一的APPID

    private static final String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC9yVHvqRRg1bw40MrVxCknxC/YuheEbt3CK9eMESz99krOisJvqVMfiHFHqsrI5hgoyiIZP26OUUnWjKE7kgOpbL94vmmkgzbikcp7gc4FcFmaRZm4M+BSuBdiH3c7IeiqJd1/NCWNXWcNYoAxChQ3HFvrEpSxdWP26jomTg3MfqsfCx69PGVSLH18h1kBcz2g/f9wsZE6jX0bkCJf8OE36otk4QI5KGb85Vmr5fNWOgWPi+AlkRL3PLZtKyVCZcJ99cLbsacauajYqqVomGiAHKjo51/wJAra4RhFB1Ydcz1Bhx7weHOHkB7Vtx2d+sbRatVQHybLhSezTQ2k5z4rAgMBAAECggEAIWFyhuu/SNTvGTMoKfcpofPw0nbQwhtZ59gcPmea+URDnhZr4oXut+IgwpE4v6Mw5qO+4Z/1ixSjHxh3F2o0OK2G8+7R1/y4P5uS+Kw5F13OdML/KZX+Q65bdofASedJ6/ti/Qen/zhJcuI7V+TE73iOdkc7XgcewQQrnCcoaColKZHE/1F8EORjcqEEoUYhRI65A6TQykMF/nt/2ip+wvWgs2R64aD0ytJVRPVkO7G4BuENiQtpTqlqq0a+5NQeb4r6mUwHNKHBZ7f/MrQ8A4c+UKfhDU6Flxi40hpPo6DcvA9UW8M0M76EdwhXHXfrZfkRV/xKRn0AtNYWNOJhMQKBgQD7jc+TTYYwaOwLNdFV/jlceh93quhHCVWKWxWDlDaWYecLeqmtZb3VIlEQ+ShrBkHinuWREeGrhamyb1cE0PAZHy/CFiEIgQvfUOxU9FmJ5iVxkNOL36NPRZFVuGxsfhde6XtKIbmiyg/6i3va+8dpeam/uTJxZS8cJACnqj8s8wKBgQDBJAim4mi77oyucHP6vpWROsS7u55UGHtvVOOSQVwP81e+stLUrde9DgMJcPc0S3vzk+d7eFBo11c+m29Lmq3BRjk9ryRzrarmNqDSq6WRp0kDsFSj0q7oL9U8eqKlnudPHx+y5JBmYkBJejTnwn3J/YKD69vkCwH1ZiZ3A4yX6QKBgFj++NsyaDEkTa5Bogf8YyrWgd+CdZ78LGlAhEIC47UcefNTuEgCYMDQ3UFWBPioRBL7CFjkrKRUK49XWL/5c5hPskPGNHWfwiWFqbb8o9jntYOSK2Wm/04CXMKo9qMDkR0hGV9GwzhWqISJaI422Xv1cRrFWJVJqPsZuamXz7YFAoGAGXrtgUbBVnnEKYdAZcYa6Uv9hF7eJm4MasWiODtTL++Ele31joVYvuJIWEiE9Gs48QuEMG/Q++Pc6K+M4UaIwcTH7HFct74jm7d9RA3NDAgrw8bRg8NZu9wDn7T2HC94iKGshudMfZVcsdTjgefQvIMrYsgM/GrPWVaeoHjhZXECgYEAoZPsOqMKuVy9M71E0DyDTfFaZdIbrdHiKB4fm2+5xCFzX5TR156fTTLDsSH3Lt6nTkV21/taJvNzBYs8ERp/RcnNf8lKolJS8Btm926tYjFikl60pURdJ9JJqDzxCzyFw2dlbc4iO9pvR5NIXp+BXO5niJvyUr4+SOln73xfxKM=";
    // 商户私钥，RSA2 加密算法默认生成格式为 PKCS8

    private static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsIQbxNSyyr68Hg/94Vxil0MLzXnGFlHRMEqYqJ7E22kTAlZN7BSxvsyfmqVLR5uBV4r7o9DmBJpUZJkKEs8mgYbzKnxqnLB4Tf97ulpGn/jRsxzHWWPmZNsvkpm3+ImzPUndDKff2lqrJsOVDxlFzJmnunsk1dj02WV6Mev0MitIVLC+2bplT7k5lkLXNmS3N+aaWUeBRm8JlwRaT3vTcyNUwZ9g2nK2XsVAdopzBLIuJuNDhguluoxGRO4/HoctNBVDOwcQopxK4PWhdssmwR1uS1dzMYTNpANgNM+JlbAJwWmIxAmiPXwe4VsBRB6zE4iuMY2I5QfyEmbeqg8QwQIDAQAB";
    // 支付宝公钥

    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    // 支付宝网关

    private static final String SIGN_TYPE = "RSA2";
    // 签名方式

    private static final String FORMAT = "json";

    private static final String CHARSET = "utf-8";
    // 字符串编码格式




}
