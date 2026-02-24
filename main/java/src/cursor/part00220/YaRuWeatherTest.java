package cursor.part00220;

import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Тесты погоды Яндекса. Проверку «я не робот» на ya.ru обойти нельзя — это капча.
 * Тесты, открывающие главную ya.ru, отключены (@Disabled): там часто показывают капчу для автоматизации.
 * Рабочие тесты идут напрямую на https://yandex.ru/pogoda/
 */
public class YaRuWeatherTest {

    private static final String YA_RU = "https://ya.ru/";
    private static final String POGODA_BASE = "https://yandex.ru/pogoda/";

    @BeforeEach
    void setUp() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "1280x800";
    }

    /** Закрывает баннер cookie/согласия, если есть кнопка «Принять» (или аналог). Клик только если элемент найден и виден. */
    private void acceptCookiesIfPresent() {
        var byAccept = org.openqa.selenium.By.xpath(
                "//button[contains(.,'Принять') or contains(.,'Согласен') or contains(.,'Всё понятно') or contains(.,'OK')] | " +
                "//a[contains(.,'Принять') or contains(.,'Согласен') or contains(.,'Всё понятно')] | " +
                "//*[@role='button' and (contains(.,'Принять') or contains(.,'Согласен') or contains(.,'OK'))]");
        try {
            $(byAccept).shouldBe(visible, Duration.ofSeconds(2)).click(ClickOptions.usingJavaScript());
            sleep(1000);
        } catch (com.codeborne.selenide.ex.ElementNotFound e) {
            // баннера нет — ничего не делаем
        }
    }

    @Test
    @Disabled("ya.ru показывает капчу «я не робот» при доступе через WebDriver; обойти нельзя")
    void weatherWidgetIsVisibleOnMainPage() {
        open(YA_RU);

        // Виджет погоды на главной (ссылка на pogoda)
        SelenideElement weatherLink = $("a[href*='pogoda']").shouldBe(visible);
        weatherLink.shouldHave(attribute("href"));
    }

    @Test
    @Disabled("ya.ru показывает капчу при автоматизации")
    void clickWeatherOpensPogodaPage() {
        open(YA_RU);

        SelenideElement weatherLink = $("a[href*='pogoda']").shouldBe(visible);
        weatherLink.click();

        sleep(2000);
        // Должны оказаться на странице погоды
        $("body").shouldHave(text("Погода").or(text("погода")).or(text("Прогноз")));
    }

    @Test
    void weeklyForecastTabIsVisibleAndClickable() {
        // Прямой переход на страницу погоды (Москва по умолчанию) — без ya.ru, без капчи
        open(POGODA_BASE);

        sleep(3000);
        acceptCookiesIfPresent();

        // Вкладка "10 дней" — прогноз на ближайшую неделю (клик через JS: элемент часто перекрыт оверлеем)
        SelenideElement tenDaysTab = $x("//a[contains(.,'10 дней') or contains(@href,'10-days')]")
                .shouldBe(visible);
        tenDaysTab.scrollIntoView(true);
        sleep(500);
        tenDaysTab.click(ClickOptions.usingJavaScript());
        sleep(2000);

        // Проверяем, что появился контент недельного прогноза (дни, температура)
        $("body").shouldHave(text("°").or(text("º")).or(text("Пн").or(text("Вт")).or(text("Ср"))));
    }

    @Test
    @Disabled("ya.ru показывает капчу при автоматизации")
    void fullFlowFromYaRuToWeeklyForecast() {
        open(YA_RU);

        // Клик по виджету погоды
        SelenideElement weatherLink = $("a[href*='pogoda']").shouldBe(visible);
        weatherLink.click();

        sleep(3000);

        // Ищем вкладку "10 дней" на странице погоды
        SelenideElement tenDaysTab = $x("//a[contains(.,'10 дней') or contains(@href,'10-days')]");
        tenDaysTab.shouldBe(visible).click();

        sleep(2000);

        // Уточнённый прогноз на неделю должен отображаться
        $("body").shouldHave(text("Пн").or(text("Вт")).or(text("Прогноз")).or(text("°")));
    }
}
