package guru.qa.niffler.test;

import com.codeborne.selenide.Condition;
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

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;
import static io.qameta.allure.Allure.step;

public class FriendsWebTest extends BaseWebTest {

    static {
        Configuration.browser = "firefox";
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin(@User(userType = WITH_FRIENDS) UserJson userForTest) {
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
    @AllureId("100")
    @DisplayName("Отображение друга в списке друзей")
    void friendShouldBeDisplayedInTable0() {
        step("Переходим к списку друзей", () -> {
            $("[data-tooltip-id='friends']").click();
        });
        step("Проверяем, что таблица не пустая", () -> {
            $$("tbody tr").shouldHave(sizeGreaterThan(0));
        });
        step("Проверяем, что у друга есть надпись маркера дружбы", () -> {
            $$("tbody tr td").last().shouldHave(Condition.text("You are friends"));
        });
    }

    @Test
    @AllureId("101")
    @DisplayName("Отображение друга в списке друзей")
    void friendShouldBeDisplayedInTable1() {
        step("Переходим к списку друзей", () -> {
            $("[data-tooltip-id='friends']").click();
        });
        step("Проверяем, что таблица не пустая", () -> {
            $$("tbody tr").shouldHave(sizeGreaterThan(0));
        });
        step("Проверяем, что у друга есть надпись маркера дружбы", () -> {
            $$("tbody tr td").last().shouldHave(Condition.text("You are friends"));
        });
    }

    @Test
    @AllureId("102")
    @DisplayName("Отображение друга в списке пользователей")
    void friendShouldBeDisplayedInTable2() {
        step("Переходим к списку пользователей", () -> {
            $("[data-tooltip-id='people']").click();
        });
        step("Проверяем, что что у друга есть надпись маркера дружбы", () -> {
            $$("table.abstract-table tr")
                    .filterBy(text("You are friends"))
                    .shouldHave(sizeGreaterThan(0));
        });
    }
}
