package cursor.part0223;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Отдельные тесты для кнопки загрузки .deb (Debian, Ubuntu)
 * на странице https://code.visualstudio.com/download
 */
public class DebDownloadTest {

    private static final String DOWNLOAD_PAGE = "https://code.visualstudio.com/download";
    private static final String DEB_BUTTON_ID = "download-alt-main-linux64_deb";

    @Test
    void debButtonIsVisible() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "800x600";
        open(DOWNLOAD_PAGE);

        SelenideElement debButton = $("#" + DEB_BUTTON_ID);
        debButton.shouldBe(visible);
        debButton.shouldHave(attribute("data-os", "linux64_deb"));
        debButton.shouldHave(attribute("aria-label", "Linux Debian Ubuntu download"));
    }

    @Test
    void debButtonHasLabels() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "800x600";
        open(DOWNLOAD_PAGE);

        SelenideElement debButton = $("#" + DEB_BUTTON_ID);
        debButton.shouldBe(visible);
        debButton.shouldHave(text(".deb"));
        debButton.shouldHave(text("Debian, Ubuntu"));
    }

    @Test
    void debButtonClick() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "800x600";

        String downloadDir = System.getProperty("java.io.tmpdir") + "/selenide-deb-downloads";
        Path downloadPath = Path.of(downloadDir);
        try {
            Files.createDirectories(downloadPath);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }

        FirefoxOptions options = new FirefoxOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.setCapability("pageLoadStrategy", "eager"); // явно для GeckoDriver
        // При закрытии не показывать диалог — сразу выходить и отменять загрузки (без Robot/рабочего стола)
        options.addPreference("browser.warnOnQuit", false);
        options.addPreference("browser.download.folderList", 2);
        options.addPreference("browser.download.dir", downloadDir);
        options.addPreference("browser.download.manager.showWhenStarting", false);
        options.addPreference("browser.download.manager.showAlertOnComplete", false);
        options.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/x-deb,application/vnd.debian.binary-package");
        Configuration.browserCapabilities = options;

        open(DOWNLOAD_PAGE);

        SelenideElement debButton = $("#" + DEB_BUTTON_ID);
        debButton.shouldBe(visible);
        // Клик через JS, чтобы не ждать навигацию по ссылке загрузки (избежать Navigation timed out)
        executeJavaScript("arguments[0].click();", debButton);

        // После клика страница может уйти на загрузку; даём время начать запись в папку
        sleep(3000);

        // Закрываем браузер без диалога (browser.warnOnQuit = false)
        closeWebDriver();
    }
}
