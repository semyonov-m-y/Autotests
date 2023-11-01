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

import java.util.ArrayList;

import static ru.sberbank.CommonConstants.*;

public class Tests {

    @Before
    public void setFilter() {
        RestAssured.filters(new AllureRestAssured());
    }

    @Tag("ECORP-T993")
    @Test
    @DisplayName("ECORP-T993 (1.0) Проверка ДУЛ клиента. Не гражданин РФ")
    @Description("Проверка операции для клиента - не гражданина РФ")
    public void clientIsNotResidentTest() {
        ValidatableResponse getEventsResponse = new Steps().getEvent(String.valueOf(ClientsLogin.HEROBOR));
        Assert.assertEquals(GET_EVENTS_FAIL, true,
                getEventsResponse.extract().body().jsonPath().get(SUCCESS));
        Assert.assertEquals(PASSPORT_OF_CITIZEN, PASSPORT_FOREIGNER,
                getEventsResponse.extract().body().jsonPath().get(BODY_STATUS_CODE));
        Assert.assertEquals(WRONG_ERROR_TITLE, NOT_RESIDENT_ERROR_TITLE,
                getEventsResponse.extract().body().jsonPath().get(ERROR_TITLE));
        Assert.assertEquals(WRONG_ERROR_MESSAGE, NOT_RESIDENT_ERROR_MESSAGE,
                getEventsResponse.extract().body().jsonPath().get(ERROR_MESSAGE));
    }

    @Tag("ECORP-T978")
    @Test
    @DisplayName("ECORP-T978 (1.0) Взаимодействие с ФП ЗоВо. Работает корректно")
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

    @Tag("ECORP-T973")
    @Test
    @DisplayName("ECORP-T973 (1.0) Взаимодействие с ФП Авторизация. Проверка получения ленты событий с возможностью" +
            "купить при наличии привилегий по коду")
    @Description("Проверка получения ленты событий")
    public void getEventTest() {
        ValidatableResponse getEventsResponse = new Steps().getEvent(String.valueOf(ClientsLogin.TUKSA));

        Assert.assertEquals(GET_EVENTS_FAIL, true,
                getEventsResponse.extract().body().jsonPath().get(SUCCESS));

        ArrayList arrayList = new ArrayList();
        arrayList.add(BUY_NOTIFICATIONS);

        Assert.assertEquals(getEventsResponse.extract().body().jsonPath().get(EVENT_BUY_BUTTON), arrayList);
    }

    @Tag("ECORP-T995")
    @Test
    @DisplayName("ECORP-T995 (1.0) Проверка наличия согласия клиента (есть согласие)")
    @Description("Проверка наличия согласия клиента")
    public void checkUnifiedConsent() {
        Steps steps = new Steps();
        ValidatableResponse CRHEventConsentResponse = steps.responseGetCRHEventConsent(String.valueOf(ClientsLogin.TUKSA));
        Assert.assertEquals(CREATE_UFS_SESSION_FAIL, true,
                CRHEventConsentResponse.extract().body().jsonPath().get(SUCCESS));

        String pid = CRHEventConsentResponse.extract().body().jsonPath().get("body.pid");

        //ValidatableResponse onEnter = steps.responseOnEnter(pid);
        ValidatableResponse onReturn = steps.responseOnReturn(pid);

    }
}
