package cursor.part00250;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

/**
 * Подтверждение согласия с cookies (I agree, Accept) на сайтах драгметаллов.
 */
public final class CookieConsentHelper {

    private CookieConsentHelper() {}

    /**
     * Пытается нажать кнопку согласия с cookies. Вызывать после open().
     */
    public static void acceptCookiesIfPresent() {
        sleep(2000);
        try {
            if (clickByText("I agree")) return;
            if (clickByText("Accept")) return;
            if (clickByText("Accept all")) return;
            if (clickByText("Accept All")) return;
            if (clickByText("Agree")) return;
            if (clickByText("Allow all")) return;
            if (clickByText("Allow All")) return;
            if (clickByText("Zustimmen")) return;
            if (clickByText("Accepter")) return;
            if (clickByText("Aceptar")) return;
            if (clickByText("Accetta")) return;
            if (clickByText("Принять")) return;
            if (clickByText("Согласен")) return;
            if (clickBySelector("[id*='accept'][id*='cookie']")) return;
            if (clickBySelector("[aria-label*='ccept'][aria-label*='ookie']")) return;
            if (clickBySelector("button[data-testid*='accept']")) return;
            if (clickBySelector("[class*='cookie'] button")) return;
        } catch (Exception ignored) {
            // баннер может отсутствовать
        }
    }

    private static boolean clickByText(String searchText) {
        try {
            var btns = $$("button, a, [role='button']").filter(text(searchText));
            if (btns.size() > 0 && btns.first().isDisplayed()) {
                btns.first().click();
                sleep(500);
                return true;
            }
        } catch (Exception ignored) {}
        return false;
    }

    private static boolean clickBySelector(String selector) {
        try {
            var el = $(selector);
            if (el.exists() && el.isDisplayed()) {
                el.click();
                sleep(500);
                return true;
            }
        } catch (Exception ignored) {}
        return false;
    }
}
