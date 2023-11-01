import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.Cookies;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;

import static ru.sberbank.CommonConstants.*;

public class Steps {

    private static String hostFP;
    private static Cookies cookies;

    @Step(value = "Отправить логин пользователя для регистрации в МП {0}")
    public ValidatableResponse login(String loggin) {
        return RestAssured.given()
                .relaxedHTTPSValidation()
                .log().all()
                .contentType(TEXT_XML).body(EMPTY_BODY)
                .when().post("https://ift-csa-server.testonline.sberbank.ru:4456/CSAMAPI/registerApp.do?operation=register&login=" + loggin + "&version=9.20&appType=android&appVersion=12.5.0.3520&deviceName=Android&devID=7AC30337-31A1-4F9A-BBDE-67DB31338F54")
                .then().log().all().statusCode(200);
    }

    @Step("Заполняем СМС для регистрации в МП")
    public ValidatableResponse mGuidSms(String mGUID, String loggin) {
        return RestAssured.given()
                .relaxedHTTPSValidation()
                .log().all()
                .contentType(TEXT_XML).body(EMPTY_BODY)
                .when().post("https://ift-csa-server.testonline.sberbank.ru:4456/CSAMAPI/registerApp.do?operation=confirm&login=" + loggin + "&version=9.20&appType=android&appVersion=12.5.0.3520&deviceName=Android&devID=7AC30337-31A1-4F9A-BBDE-67DB31338F54&mGUID=" + mGUID + "&smsPassword=55098")
                .then().log().all().statusCode(200);
    }

    @Step("Заполняем пароль для регистрации в МП")
    public ValidatableResponse responsePin(String mGUID, String loggin) {
        return RestAssured.given()
                .relaxedHTTPSValidation()
                .log().all()
                .contentType(TEXT_XML).body(EMPTY_BODY)
                .when().post("https://ift-csa-server.testonline.sberbank.ru:4456/CSAMAPI/registerApp.do?operation=createPIN&login=" + loggin + "&version=9.20&appType=android&appVersion=10.6.0&deviceName=Android&devID=7AC30337-31A1-4F9A-BBDE-67DB31338F54&mGUID=" + mGUID + "&password=55098&isLightScheme=false&mobileSdkData=1")
                .then().log().all().statusCode(200);
    }

    @Step("Отправляем полученный токен для регистрации в МП")
    public ValidatableResponse responseToken(String host, String token) {
        return RestAssured.given()
                .relaxedHTTPSValidation()
                .log().all()
                .contentType(APPLICATION_JSON).body(EMPTY_BODY)
                .when().post("http" + host + "/mobile9/postCSALogin.do?token=" + token)
                .then().log().all().statusCode(200);
    }

    @Step("Получаем токен ЕФС для МП")
    public ValidatableResponse responseTokenUfs(String host, Cookies cookies) {
        return RestAssured.given()
                .cookies(cookies)
                .relaxedHTTPSValidation()
                .log().all()
                .contentType(APPLICATION_JSON).body(EMPTY_BODY)
                .header(X_GW_HOST, "127.0.0.1:8080")
                .header(X_FORWARDED_FOR, "255.255.255.0")
                .when().post("http" + host + "/mobile9/private/unifiedClientSession/getToken.do?systemName=ufs")
                .then().log().all().statusCode(200);
    }

    @Step("Создаем сессию ЕФС в МП")
    public ValidatableResponse responseCreateSession(String tokenUfs, String hostUfs) {
        return RestAssured.given()
                .relaxedHTTPSValidation()
                .log().all()
                .contentType(APPLICATION_JSON).body("{\"token\": \"" + tokenUfs + "\"}")
                .header(X_GW_HOST, "127.0.0.1:8080")
                .header(X_FORWARDED_FOR, "255.255.255.0")
                .when().post("http://" + hostUfs + "/sm-uko/v2/session/create")
                .then().log().all().statusCode(200);
    }

    @Step("Отправляем healthcheck для ФП События по КИ")
    public ValidatableResponse responseHealthCheck() {
        return RestAssured.given()
                .relaxedHTTPSValidation()
                .log().all()
                .header(USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0")
                .header(HOST, "tkli-efs8637.vm.mos.cloud.sbrf.ru:9080")
                .header(ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header(ACCEPT_ENCODING, "gzip, deflate")
                .header(ACCEPT_LANGUAGE, "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
                .header(CONNECTION, "keep-alive")
                .header(UPGRADE_INSECURE_REQUESTS, "1")
                .header(X_GW_HOST, "127.0.0.1:8080")
                .header(X_FORWARDED_FOR, "255.255.255.0")
                .when().get("http://tkli-efs8637.vm.mos.cloud.sbrf.ru:9080/credithistory-app/v1.0/healthcheck")
                .then().log().all().statusCode(200);
    }

    @Step("Отправляем environment/product для ФП События по КИ")
    public ValidatableResponse responseEnvironmentProduct() {
        return RestAssured.given()
                .relaxedHTTPSValidation()
                .log().all()
                .header(USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0")
                .header(HOST, "tkli-efs8637.vm.mos.cloud.sbrf.ru:9080")
                .header(ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header(ACCEPT_ENCODING, "gzip, deflate")
                .header(ACCEPT_LANGUAGE, "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
                .header(CONNECTION, "keep-alive")
                .header(UPGRADE_INSECURE_REQUESTS, "1")
                .header(X_GW_HOST, "127.0.0.1:8080")
                .header(X_FORWARDED_FOR, "255.255.255.0")
                .when().get("http://tkli-efs8637.vm.mos.cloud.sbrf.ru:9080/credithistory-app/v1.0/environment/product")
                .then().log().all().statusCode(200);
    }

    @Step("Отправляем запрос в ФП Отчёт по кредитной истории")
    public ValidatableResponse getHistory(String userLogin) {
        return RestAssured.given()
                .cookies(getCookie(userLogin))
                .relaxedHTTPSValidation()
                .log().all()
                .when().get("http://" + hostFP + ".vm.mos.cloud.sbrf.ru:9080/credithistory-app/v1.0/rest/getHistory")
                .then().log().all().statusCode(200);
    }

    @Step("Создание сессии ЕФС")
    public ValidatableResponse getSession(String userLogin) {
        ValidatableResponse logIn = login(userLogin);
        Assert.assertEquals(REGISTRATION_FAIL, ZERO, logIn.extract().body().xmlPath().get(RESPONSE_STATUS_CODE));

        //Извлекаем mGUID
        String mGUID = logIn.extract().response().body().xmlPath().getString("response.confirmRegistrationStage.mGUID");

        //Вводим СМС
        ValidatableResponse smsResponse = mGuidSms(mGUID, userLogin);
        Assert.assertEquals(WRONG_SMS, ZERO, smsResponse.extract().body().xmlPath().get(RESPONSE_STATUS_CODE));

        //Создаем пароль
        ValidatableResponse pinResponse = responsePin(mGUID, userLogin);
        Assert.assertEquals(WRONG_PASSWORD, ZERO, pinResponse.extract().body().xmlPath().get(RESPONSE_STATUS_CODE));

        //Забираем хост
        String host = pinResponse.extract().response().body().xmlPath().getString("response.loginData.host");

        switch (host) {
            case "ift-node1.testonline.sberbank.ru":
                host = "s://ift-node1-mp.testonline.sberbank.ru:4477";
                break;
            case "ift-node0.testonline.sberbank.ru":
                host = "s://ift-node1-mp.testonline.sberbank.ru:4477";
                break;
            case "ift-node5.testonline.sberbank.ru":
                host = "://10.116.232.202:9081";
                break;
        }

        //Забираем токен
        String token = pinResponse.extract().response().body().xmlPath().getString("response.loginData.token");

        //Отдаем токен и хост
        ValidatableResponse tokenResponse = responseToken(host, token);
        Assert.assertEquals(WRONG_TOKEN_OR_HOST, ZERO, tokenResponse.extract().body().xmlPath().get("response.status.code"));

        Cookies cookies = tokenResponse.extract().response().getDetailedCookies();

        //Получение токена ЕФС
        ValidatableResponse ufsTokenResponse = responseTokenUfs(host, cookies);
        Assert.assertEquals(WRONG_UFS_TOKEN, ZERO, ufsTokenResponse.extract().body().xmlPath().get(RESPONSE_STATUS_CODE));

        String tokenUfs = ufsTokenResponse.extract().response().xmlPath().getString("response.token");
        String hostUfs = ufsTokenResponse.extract().response().xmlPath().getString("response.host");

        //Определяем хост для ФП
        hostFP = hostUfs;
        switch (hostFP) {
            case "nginx-and-ift-b1.testonline.sberbank.ru":
                hostFP="tkli-efs8637";
            case "nginx-and-ift-b3.testonline.sberbank.ru":
                hostFP="tkli-efs8639";
                break;

        }

        switch (hostUfs){
            case "nginx-and-ift-b1.testonline.sberbank.ru":
                hostUfs="tklia-efs000420.vm.mos.cloud.sbrf.ru:9080";
            case "nginx-and-ift-b3.testonline.sberbank.ru":
                hostUfs="tklia-efs000420.vm.mos.cloud.sbrf.ru:9080";
                break;
        }

        //Создание сессии ЕФС
        ValidatableResponse createSessionResponse = responseCreateSession(tokenUfs, hostUfs);
        Assert.assertEquals(CREATE_UFS_SESSION_FAIL, true,
                createSessionResponse.extract().body().jsonPath().get(SUCCESS));

        return createSessionResponse;
    }

    @Step("Создание сессии ЕФС")
    public Cookies getCookie(String userLogin) {
        Cookies cookiesE = getSession(userLogin).extract().response().getDetailedCookies();

        return cookiesE;
    }

    @Step("Печать отчёта")
    public ValidatableResponse printReport(String userLogin) {
        return RestAssured.given()
                .cookies(getCookie(userLogin))
                .relaxedHTTPSValidation()
                .log().all()
                .when().get("http://" + getHostFP() + ".vm.mos.cloud.sbrf.ru:9080/credithistory-app/v1.0/rest/print/report")
                .then().log().all().statusCode(200);
    }

    @Step("Получение единого согласия")
    public ValidatableResponse getConsentFlow(String userLogin) {
        return RestAssured.given()
                .cookies(getCookie(userLogin))
                .relaxedHTTPSValidation()
                .log().all()
                .when().get("http://" + getHostFP() + ".vm.mos.cloud.sbrf.ru:9080/credithistory-app/v1.0/consent?cmd=START&name=ConsentFlow")
                .then().log().all().statusCode(200);
    }

    @Step("Получение флоу платежа")
    public ValidatableResponse getPaymentFlow(String userLogin) {
        return RestAssured.given()
                .cookies(getCookie(userLogin))
                .relaxedHTTPSValidation()
                .log().all()
                .when().get("http://" + getHostFP() + ".vm.mos.cloud.sbrf.ru:9080/credithistory-app/v1.0/payment?cmd=START&name=PaymentFlow")
                .then().log().all().statusCode(200);
    }

    @Step("Получение персоны")
    public ValidatableResponse getPerson(String userLogin) {
        return RestAssured.given()
                .cookies(getCookie(userLogin))
                .relaxedHTTPSValidation()
                .log().all()
                .when().get("http://" + getHostFP() + ".vm.mos.cloud.sbrf.ru:9080/credithistory-app/v1.0/rest/test/getPerson")
                .then().log().all().statusCode(200);
    }

    public static String getHostFP() {
        return hostFP;
    }
}
