package cursor.part0223;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Тесты для страницы загрузки VS Code: https://code.visualstudio.com/download
 * Проверка кнопки загрузки .deb (Debian, Ubuntu).
 */
public class VsCodeDownloadTest {

    private static final String DOWNLOAD_PAGE = "https://code.visualstudio.com/download";

    /** Кнопка загрузки .deb для Linux (Debian, Ubuntu). */
    private static final String DEB_BUTTON_ID = "download-alt-main-linux64_deb";

    @Test
    void debDownloadButtonIsVisible() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "800x600";

        open(DOWNLOAD_PAGE);

        SelenideElement debButton = $("#" + DEB_BUTTON_ID);
        debButton.shouldBe(visible);
        debButton.shouldHave(attribute("data-os", "linux64_deb"));
        debButton.shouldHave(attribute("aria-label", "Linux Debian Ubuntu download"));
    }

    @Test
    void debButtonHasExpectedLabels() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "800x600";

        open(DOWNLOAD_PAGE);

        SelenideElement debButton = $("#" + DEB_BUTTON_ID);
        debButton.shouldBe(visible);
        debButton.shouldHave(text(".deb"));
        debButton.shouldHave(text("Debian, Ubuntu"));
    }

    @Test
    void debDownloadButtonClick() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "800x600";

        open(DOWNLOAD_PAGE);

        SelenideElement debButton = $("#" + DEB_BUTTON_ID);
        debButton.shouldBe(visible);
        debButton.click();

        // После клика остаёмся на странице загрузки (или начинается скачивание)
        $("body").shouldHave(text("Download Visual Studio Code"));
    }
}
