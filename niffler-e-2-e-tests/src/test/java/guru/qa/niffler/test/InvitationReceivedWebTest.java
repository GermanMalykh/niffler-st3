package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECEIVED;
import static io.qameta.allure.Allure.*;

public class InvitationReceivedWebTest extends BaseWebTest {

    static {
        Configuration.browser = "firefox";
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin(@User(userType = INVITATION_RECEIVED) UserJson userForTest) {
        SelenideLogger.addListener("allure", new AllureSelenide());
        step("Переходим на главную страницу", () -> {
            Selenide.open("http://127.0.0.1:3000/main");
        });
        step("Выполняем авторизацию", () -> {
            $("a[href*='redirect']").click();
            $("input[name='username']").setValue(userForTest.getUsername());
            $("input[name='password']").setValue(userForTest.getPassword());
            $("button[type='submit']").click();
        });
    }

    @Test
    @AllureId("300")
    @DisplayName("Отображение новой заявки в друзья в списке друзей")
    void friendRequestShouldBeDisplayedInTable0() {
        step("Переходим к списку друзей", () -> {
            $("[data-tooltip-id='friends']")
                    .$(".header__sign").shouldBe(visible).click();
        });
        step("Проверяем, что таблица не пустая", () -> {
            $$("tbody tr").shouldHave(sizeGreaterThan(0));
        });
        step("Проверяем, что кнопка для принятия заявки отображается в интерфейсе", () -> {
            $("div[data-tooltip-id='submit-invitation']").shouldBe(visible);
        });
        step("Проверяем, что кнопка для отклонения заявки отображается в интерфейсе", () -> {
            $("div[data-tooltip-id='decline-invitation']").shouldBe(visible);
        });
    }

    @Test
    @AllureId("301")
    @DisplayName("Отображение новой заявки в друзья в списке друзей")
    void friendRequestShouldBeDisplayedInTable1() {
        step("Переходим к списку друзей", () -> {
            $("[data-tooltip-id='friends']")
                    .$(".header__sign").shouldBe(visible).click();
        });
        step("Проверяем, что таблица не пустая", () -> {
            $$("tbody tr").shouldHave(sizeGreaterThan(0));
        });
        step("Проверяем, что кнопка для принятия заявки отображается в интерфейсе", () -> {
            $("div[data-tooltip-id='submit-invitation']").shouldBe(visible);
        });
        step("Проверяем, что кнопка для отклонения заявки отображается в интерфейсе", () -> {
            $("div[data-tooltip-id='decline-invitation']").shouldBe(visible);
        });
    }

    @Test
    @AllureId("302")
    @DisplayName("Отображение новой заявки в друзья в списке пользователей")
    void friendRequestShouldBeDisplayedInTable2() {
        step("Переходим к списку всех пользователей", () -> {
            $("[data-tooltip-id='people']").click();
        });
        step("Проверяем, что кнопка для принятия заявки отображается в интерфейсе", () -> {
            $$("div[data-tooltip-id='submit-invitation']")
                    .shouldHave(size(1));
        });
        step("Проверяем, что кнопка для отклонения заявки отображается в интерфейсе", () -> {
            $$("div[data-tooltip-id='decline-invitation']")
                    .shouldHave(size(1));
        });
    }
}
