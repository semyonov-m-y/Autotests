import io.qameta.allure.Description;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import ru.sberbank.ClientsLogin;

import static ru.sberbank.CommonConstants.*;

public class Tests {

    @Before
    public void setFilter() {
        RestAssured.filters(new AllureRestAssured());
    }

    @Tag("ECORP-T653")
    @Test
    @DisplayName("ECORP-T653 (1.0) Взаимодействие с ФП ЗоВо. Работает корректно")
    @Description("Проверка взаимодействия с ФП ЗоВо")
    public void healthCheckTest() {
        Steps steps = new Steps();

        ValidatableResponse healthCheck = steps.responseHealthCheck();
        Assert.assertEquals(HEALTH_CHECK_FAIL, true, healthCheck.extract().body().jsonPath().get(SUCCESS));
        Assert.assertEquals(HEALTH_CHECK_NOT_OK, OK, healthCheck.extract().body().jsonPath().get(BODY));

        ValidatableResponse environmentProduct = steps.responseEnvironmentProduct();
        Assert.assertEquals(ENVIRONMENT_PRODUCT_FAIL, true, environmentProduct.extract().body().jsonPath().get(SUCCESS));
        Assert.assertEquals(WRONG_SUBSYSTEM, SUBSYSTEM_CODE, environmentProduct.extract().body().jsonPath().get(BODY_SUBSYSTEM));
        Assert.assertEquals(WRONG_DEPLOYMENT_UNIT, DEPLOYMENT_UNIT, environmentProduct.extract().body().jsonPath().get(BODY_DEPLOYMENT_UNIT));
    }

    @Tag("ECORP-T1349")
    @Test
    @DisplayName("ECORP-T1349 (1.0) Проверка получения getHistory")
    @Description("Проверка получения getHistory")
    public void getHistoryTest() {
        ValidatableResponse history = new Steps().getHistory(String.valueOf(ClientsLogin.TUKSA));
        Assert.assertEquals(GET_HISTORY_FAIL, true, history.extract().body().jsonPath().get(SUCCESS));
    }

    @Tag("ECORP-T1345")
    @Test
    @DisplayName("ECORP-T1345 (1.0) Проверка получения ConsentFlow")
    @Description("Проверка получения ConsentFlow")
    public void consentFlowTest() {
        ValidatableResponse consentFlow = new Steps().getConsentFlow(String.valueOf(ClientsLogin.TUKSA));
        Assert.assertEquals(EXTERNAL_ENTER, true, consentFlow.extract().body().jsonPath().get(SUCCESS));
    }

    @Tag("ECORP-T1348")
    @Test
    @DisplayName("ECORP-T1348 (1.0) Проверка получения PaymentFlow")
    @Description("Проверка получения PaymentFlow")
    public void paymentFlowTest() {
        ValidatableResponse paymentFlow = new Steps().getPaymentFlow(String.valueOf(ClientsLogin.TUKSA));
        Assert.assertEquals(PAYMENT_FLOW_FAIL, true, paymentFlow.extract().body().jsonPath().get(SUCCESS));
    }

    @Tag("ECORP-T1347")
    @Test
    @DisplayName("ECORP-T1347 (1.0) Проверка получения getPerson")
    @Description("Проверка получения getPerson")
    public void getPersonTest() {
        ValidatableResponse person = new Steps().getPerson(String.valueOf(ClientsLogin.TUKSA));
        Assert.assertEquals(GET_PERSON_FAIL, true, person.extract().body().jsonPath().get(SUCCESS));
        Assert.assertEquals(WRONG_STATUS_CODE, "200", person.extract().body().jsonPath().get(BODY_SIMPLE_STATUS_CODE));
        Assert.assertEquals(WRONG_STATUS_DESC, PROFILE, person.extract().body().jsonPath().get(BODY_SIMPLE_STATUS_DESC));
    }

    @Tag("ECORP-T1350")
    @Test
    @DisplayName("ECORP-T1350 (1.0) Проверка клиента не резидента РФ")
    @Description("Проверка клиента не резидента РФ")
    public void clientIsNotResidentTest() {
        ValidatableResponse resident = new Steps().getHistory(String.valueOf(ClientsLogin.HEROBOR));
        Assert.assertEquals(GET_HISTORY_FAIL, true, resident.extract().body().jsonPath().get(SUCCESS));
        Assert.assertEquals(PASSPORT_OF_CITIZEN, PASSPORT_FOREIGNER, resident.extract().body().jsonPath().get(BODY_STATUS_CODE));
        Assert.assertEquals(WRONG_STATUS_DESC, WRONG_PASSPORT, resident.extract().body().jsonPath().get(BODY_STATUS_DESC));
    }
}
