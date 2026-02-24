package cursor.part00220;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Тесты страницы Bureau of Meteorology (BOM) для Сиднея.
 * Официальный и самый популярный погодный сервис Австралии.
 * https://www.bom.gov.au/nsw/forecasts/sydney.shtml
 */
public class BomGovAuSydneyTest {

    private static final String WEATHER_PAGE = "https://www.bom.gov.au/nsw/forecasts/sydney.shtml";
    private static final int DELAY_MS = 5000;   // при ошибках увеличить до 10000

    @BeforeEach
    void setUp() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "800x600";
    }

    @Test
    void weatherLocationIsVisible() {
        open(WEATHER_PAGE);
        sleep(DELAY_MS);

        $("body").shouldHave(text("Sydney"));
    }

    @Test
    void weatherShowsSydney() {
        open(WEATHER_PAGE);
        sleep(DELAY_MS);

        $("body").shouldHave(text("Sydney"));
        $("body").shouldHave(text("forecast").or(text("Forecast")).or(text("°")).or(text("weather")));
    }

    @Test
    void forecastDaysAreVisible() {
        open(WEATHER_PAGE);
        sleep(DELAY_MS);

        $("body").shouldHave(text("Sydney"));
        $("body").shouldHave(text("Monday").or(text("Tuesday")).or(text("Today")).or(text("Forecast")));
    }

    @Test
    void forecastLinkClick() {
        open(WEATHER_PAGE);
        sleep(DELAY_MS);

        SelenideElement forecastLink = $x("//a[contains(@href,'forecast') or contains(@href,'detailed')]");
        forecastLink.shouldBe(visible).click();

        sleep(DELAY_MS);

        $("body").shouldHave(text("Sydney").or(text("Bureau")));
    }
}
