package cursor.part00220;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Тесты страницы 中国天气网 (weather.com.cn) для Пекина.
 * Самый популярный погодный сайт Китая.
 * https://www.weather.com.cn/weather/101010100.shtml
 */
public class WeatherComCnBeijingTest {

    private static final String WEATHER_PAGE = "https://www.weather.com.cn/weather/101010100.shtml";

    @BeforeEach
    void setUp() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "800x600";
    }

    @Test
    void weatherLocationIsVisible() {
        open(WEATHER_PAGE);

        $("body").shouldHave(text("北京"));
    }

    @Test
    void weatherShowsBeijing() {
        open(WEATHER_PAGE);

        $("body").shouldHave(text("北京"));
        $("body").shouldHave(text("℃").or(text("°")).or(text("º")));
    }

    @Test
    void forecastDaysAreVisible() {
        open(WEATHER_PAGE);

        sleep(2000);

        // Блок прогноза: "今天", "明天" или "7天"
        $("body").shouldHave(text("今天").or(text("明天")).or(text("7天")));
    }

    @Test
    void forecastLinkClick() {
        open(WEATHER_PAGE);

        sleep(2000);

        // Клик по ссылке "8-15天" или "7天" для расширенного прогноза
        SelenideElement forecastLink = $x("//a[contains(@href,'weather15d') or contains(@href,'weather40d')]");
        forecastLink.shouldBe(visible).click();

        sleep(2000);

        $("body").shouldHave(text("北京").or(text("天气")));
    }
}
