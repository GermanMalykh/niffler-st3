package guru.qa.niffler.test;

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
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;

public class InvitationSentWebTest extends BaseWebTest {

    static {
        Configuration.browser = "firefox";
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin(@User(userType = INVITATION_SENT) UserJson userForTest) {
        SelenideLogger.addListener("allure", new AllureSelenide());
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userForTest.getUsername());
        $("input[name='password']").setValue(userForTest.getPassword());
        $("button[type='submit']").click();
    }

    @Test
    @AllureId("200")
    void invitationShouldBeDisplayedInTable0(@User(userType = INVITATION_SENT) UserJson userForTest) {
        $("[data-tooltip-id='people']").click();
        $$("table.abstract-table tr")
                .filterBy(text("Pending invitation"))
                .shouldHave(sizeGreaterThan(0));
    }

    @Test
    @AllureId("201")
    void invitationShouldBeDisplayedInTable1(@User(userType = INVITATION_SENT) UserJson userForTest) {
        $("[data-tooltip-id='people']").click();
        $$("table.abstract-table tr")
                .filterBy(text("Pending invitation"))
                .shouldHave(sizeGreaterThan(0));
    }

    @Test
    @AllureId("202")
    void invitationShouldBeDisplayedInTable2(@User(userType = INVITATION_SENT) UserJson userForTest) {
        $("[data-tooltip-id='people']").click();
        $$("table.abstract-table tr")
                .filterBy(text("Pending invitation"))
                .shouldHave(sizeGreaterThan(0));
    }
}
