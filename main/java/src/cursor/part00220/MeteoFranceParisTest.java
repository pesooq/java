package cursor.part00220;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Тесты страницы Météo-France для Paris.
 * Météo-France — самый популярный погодный сервис во Франции.
 * https://meteofrance.com/previsions-meteo-france/paris/75000
 */
public class MeteoFranceParisTest {

    private static final String WEATHER_PAGE = "https://meteofrance.com/previsions-meteo-france/paris/75000";

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
        locationHeading.shouldHave(text("Paris"));
    }

    @Test
    void weatherShowsParis() {
        open(WEATHER_PAGE);

        $("body").shouldHave(text("Paris"));
        $("body").shouldHave(text("°").or(text("º")).or(text("deg")));
    }

    @Test
    void forecastDaysAreVisible() {
        open(WEATHER_PAGE);

        sleep(2000);

        // Блок "Tendance pour les jours suivants" или "prévisions"
        SelenideElement forecastSection = $x("//*[contains(.,'Tendance') or contains(.,'prévisions') or contains(.,'jours')]");
        forecastSection.shouldBe(visible);

        $("body").shouldHave(text("Paris"));
    }

    @Test
    void forecastLinkClick() {
        open(WEATHER_PAGE);

        sleep(2000);

        // Клик по ссылке прогноза или "LIRE LE BULLETIN"
        SelenideElement forecastLink = $x("//a[contains(.,'LIRE LE BULLETIN') or contains(.,'prévisions') or contains(@href,'previsions')]");
        forecastLink.shouldBe(visible).click();

        sleep(1500);

        $("body").shouldHave(text("Paris").or(text("Météo")));
    }
}
