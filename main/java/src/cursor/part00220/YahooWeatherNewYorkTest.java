package cursor.part00220;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Тесты страницы Yahoo Погода для New York.
 * https://weather.yahoo.com/us/ny/new-york
 */
public class YahooWeatherNewYorkTest {

    private static final String WEATHER_PAGE = "https://weather.yahoo.com/us/ny/new-york";

    @BeforeEach
    void setUp() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "800x600";
    }

    @Test
    void weatherLocationIsVisible() {
        open(WEATHER_PAGE);

        SelenideElement locationHeading = $("h1");
        locationHeading.shouldBe(visible);
        locationHeading.shouldHave(text("New York"));
    }

    @Test
    void weatherShowsNewYork() {
        open(WEATHER_PAGE);

        $("body").shouldHave(text("New York"));
        $("body").shouldHave(text("°").or(text("º")));
    }

    @Test
    void forecastDaysAreVisible() {
        open(WEATHER_PAGE);

        sleep(2000);

        // Блок "Next 8 days" или прогноз на неделю
        SelenideElement forecastSection = $x("//*[contains(.,'Next 8 days') or contains(.,'Today')]");
        forecastSection.shouldBe(visible);

        // Проверяем наличие дней (Today, Tomorrow или др.)
        $("body").shouldHave(text("Today").or(text("Tomorrow")));
    }

    @Test
    void forecastDayClick() {
        open(WEATHER_PAGE);

        sleep(2000);

        // Клик по одному из дней прогноза (Today, Tomorrow и т.д.)
        SelenideElement dayLink = $x("//*[contains(.,'Tomorrow') or contains(.,'Today')][self::a or self::button or self::div]");
        dayLink.shouldBe(visible).click();

        sleep(1500);

        // Страница остаётся с прогнозом
        $("body").shouldHave(text("New York"));
    }
}
