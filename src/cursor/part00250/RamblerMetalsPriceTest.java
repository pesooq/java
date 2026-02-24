package cursor.part00250;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Выборка цен на драгметаллы (золото, платина, серебро) на Рамблере.
 * Раздел рынков и инвестиций с новостями и данными по металлам.
 */
public class RamblerMetalsPriceTest {

    private static final String MARKETS_PAGE = "https://finance.rambler.ru/markets/";
    private static final String FINANCE_MAIN = "https://finance.rambler.ru/";

    @Test
    void ramblerMarketsPage() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "800x600";
        open(MARKETS_PAGE);
        CookieConsentHelper.acceptCookiesIfPresent();

        $("body").shouldBe(visible);
        $("body").shouldHave(text("Рамблер"));
    }

    @Test
    void ramblerFinanceMain() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "800x600";
        Configuration.timeout = 15000;
        open(FINANCE_MAIN);
        CookieConsentHelper.acceptCookiesIfPresent();

        $("body").shouldBe(visible);
    }
}
