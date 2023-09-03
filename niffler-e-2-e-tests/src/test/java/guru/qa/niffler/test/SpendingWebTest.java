package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import guru.qa.niffler.db.SpendDb;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;

@Disabled
public class SpendingWebTest {
    private final String USERNAME = "German";
    private final String PASSWORD = "12345";
    private final String CATEGORY_NAME = "Рыбалка";
    private final String DESCRIPTION = "Рыбалка на Ладоге";
    private final double AMOUNT = 14000.00;

    static {
        Configuration.browser = "firefox";
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        step("Переходим на главную страницу", () -> {
            Selenide.open("http://127.0.0.1:3000/main");
        });
        step("Выполняем авторизацию", () -> {
            $("a[href*='redirect']").click();
            $("input[name='username']").setValue(USERNAME);
            $("input[name='password']").setValue(PASSWORD);
            $("button[type='submit']").click();
        });
    }

    @Category(
            category = CATEGORY_NAME,
            username = USERNAME
    )
    @Spend(
            username = USERNAME,
            description = DESCRIPTION,
            category = CATEGORY_NAME,
            amount = AMOUNT,
            currency = CurrencyValues.RUB
    )

    @Test
    @AllureId("400")
    @DisplayName("Удаление записи по расходам из таблицы расходов")
    void spendingShouldBeDeletedAfterDeleteAction(SpendJson createdSpend) {
        step("Выбираем запись в таблице расходов", () -> {
            $(".spendings__content tbody")
                    .$$("tr")
                    .find(text(createdSpend.getDescription()))
                    .$("td")
                    .scrollTo()
                    .click();
        });
        step("Удаляем выбранную запись", () -> {
            $(byText("Delete selected")).click();
        });
        step("Проверяем что в таблице не осталось записей", () -> {
            $(".spendings__content tbody")
                    .$$("tr")
                    .shouldHave(size(0));
        });
    }

    @AfterEach
    void cleaningData() {
        step("Удаляем категорию из БД", () -> {
            SpendDb.removeCategory(CATEGORY_NAME);
        });
    }

}
