package cursor.part00250;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Выборка цен на драгметаллы (золото, платина, серебро) в Яндексе.
 * Страница биржевых товаров с котировками по месяцам, данные по средней цене.
 */
public class YandexMetalsPriceTest {

    private static final String COMMODITIES_PAGE = "https://yandex.ru/finance/commodities";

    @Test
    void goldPricePage() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "800x600";
        open("https://yandex.ru/finance/commodities/GC");
        CookieConsentHelper.acceptCookiesIfPresent();

        $("body").shouldHave(text("Золото"));
        $("body").shouldHave(text("GC"));
    }

    @Test
    void silverPricePage() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "800x600";
        open("https://yandex.ru/finance/commodities/SI");
        CookieConsentHelper.acceptCookiesIfPresent();

        $("body").shouldHave(text("Серебро"));
        $("body").shouldHave(text("SI"));
    }

    @Test
    void platinumPricePage() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "800x600";
        open("https://yandex.ru/finance/commodities/PL");
        CookieConsentHelper.acceptCookiesIfPresent();

        $("body").shouldHave(text("Платина"));
        $("body").shouldHave(text("PL"));
    }

    @Test
    void commoditiesListShowsAllMetals() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "800x600";
        open(COMMODITIES_PAGE);
        CookieConsentHelper.acceptCookiesIfPresent();

        $("body").shouldHave(text("Биржевые товары"));
        $("body").shouldHave(text("Золото"));
        $("body").shouldHave(text("Серебро"));
        $("body").shouldHave(text("Платина"));
    }
}
