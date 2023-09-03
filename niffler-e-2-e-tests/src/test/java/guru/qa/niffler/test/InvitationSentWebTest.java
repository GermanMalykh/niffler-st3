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

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;
import static io.qameta.allure.Allure.step;

public class InvitationSentWebTest extends BaseWebTest {

    @BeforeEach
    void doLogin(@User(userType = INVITATION_SENT) UserJson userForTest) {
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

    @RepeatedTest(3)
    @AllureId("200")
    @DisplayName("Отображение информации об отправленной заявке на дружбу")
    void invitationShouldBeDisplayedInTable0() {
        step("Переходим к списку пользователей", () -> {
            $("[data-tooltip-id='people']").click();
        });
        step("Проверяем, что в таблице присутствует информация об отправленной заявке на дружбу", () -> {
            $$("table.abstract-table tr")
                    .filterBy(text("Pending invitation"))
                    .shouldHave(sizeGreaterThan(0));
        });
    }

}
