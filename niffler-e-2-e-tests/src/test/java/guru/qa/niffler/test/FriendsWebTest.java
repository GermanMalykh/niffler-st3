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
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

public class FriendsWebTest extends BaseWebTest {

    static {
        Configuration.browser = "firefox";
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        SelenideLogger.addListener("allure", new AllureSelenide());
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userForTest.getUsername());
        $("input[name='password']").setValue(userForTest.getPassword());
        $("button[type='submit']").click();
    }

    @Test
    @AllureId("100")
    void friendShouldBeDisplayedInTable0(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        $("[data-tooltip-id='friends']").click();
        $$("tbody tr").shouldHave(sizeGreaterThan(0));
        $$("tbody tr td").last().shouldHave(Condition.text("You are friends"));
    }

    @Test
    @AllureId("101")
    void friendShouldBeDisplayedInTable1(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        $("[data-tooltip-id='friends']").click();
        $$("tbody tr").shouldHave(sizeGreaterThan(0));
        $$("tbody tr td").last().shouldHave(Condition.text("You are friends"));
    }

    @Test
    @AllureId("102")
    void friendShouldBeDisplayedInTable2(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        $("[data-tooltip-id='friends']").click();
        $$("tbody tr").shouldHave(sizeGreaterThan(0));
        $$("tbody tr td").last().shouldHave(Condition.text("You are friends"));
    }
}
