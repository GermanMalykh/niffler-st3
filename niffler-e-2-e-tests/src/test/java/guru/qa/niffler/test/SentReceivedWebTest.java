package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECEIVED;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;
import static io.qameta.allure.Allure.step;

public class SentReceivedWebTest extends BaseWebTest {

    @BeforeEach
    void doLogin(@User(userType = INVITATION_SENT) UserJson userInvSent,
                 @User(userType = INVITATION_RECEIVED) UserJson userInvRec) {
        SelenideLogger.addListener("allure", new AllureSelenide());
        step("Переходим на главную страницу", () -> {
            Selenide.open("http://127.0.0.1:3000/main");
        });
        step("Выполняем авторизацию для пользователя с отправленной заявкой на дружбу", () -> {
            $("a[href*='redirect']").click();
            $("input[name='username']").setValue(userInvSent.getUsername());
            $("input[name='password']").setValue(userInvSent.getPassword());
            $("button[type='submit']").click();
        });
    }

    @RepeatedTest(3)
    @AllureId("500")
    @DisplayName("Отображение отправки/получения заявки на дружбу")
    void sendInvitationAndInvitationReceivedWebTest(@User(userType = INVITATION_RECEIVED) UserJson userInvRec) {
        step("Переходим к списку пользователей", () -> {
            $("[data-tooltip-id='people']").click();
        });
        step("Проверяем, что в таблице присутствует информация об отправленной заявке на дружбу", () -> {
            $$("table.abstract-table tr")
                    .filterBy(text("Pending invitation"))
                    .shouldHave(sizeGreaterThan(0));
        });
        step("Выполнить выход из системы", () -> {
            $("[data-tooltip-id='logout']").click();
        });
        step("Выполняем авторизацию для пользователя с полученной заявкой на дружбу", () -> {
            $("a[href*='redirect']").click();
            $("input[name='username']").setValue(userInvRec.getUsername());
            $("input[name='password']").setValue(userInvRec.getPassword());
            $("button[type='submit']").click();
        });
        step("Переходим к списку пользователей", () -> {
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
