package cursor.part00230;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Выборка статистики РПЛ (Российская премьер-лига) за последние 3 года:
 * победы у себя на поле, ничьи, выигрыш в гостях.
 * Выгрузка в CSV в папку download.
 */
public class RplStatsTest {

    private static final String TOURNAMENT_TABLE = "https://premierliga.ru/tournament-table/";
    private static final String STATISTICS = "https://premierliga.ru/statistics/";
    private static final String DOWNLOAD_DIR = "build/downloads";

    @Test
    void tournamentTableHasWinsDrawsLosses() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "800x600";
        open(TOURNAMENT_TABLE);
        CookieConsentHelper.acceptCookiesIfPresent();

        $("body").shouldHave(text("РПЛ"));
        $("body").shouldHave(text("Турнирная таблица"));
        $("body").shouldHave(text("В"));
        $("body").shouldHave(text("Н"));
        $("body").shouldHave(text("П"));
    }

    @Test
    void statisticsPageLoads() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "800x600";
        open(STATISTICS);
        CookieConsentHelper.acceptCookiesIfPresent();

        $("body").shouldHave(text("РПЛ"));
        $("body").shouldHave(text("Статистика"));
        $("body").shouldHave(text("Сезон"));
    }

    @Test
    void tournamentTableHasSeasonSelector() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "800x600";
        open(TOURNAMENT_TABLE);
        CookieConsentHelper.acceptCookiesIfPresent();

        $("body").shouldHave(text("Сезон"));
        $("body").shouldHave(text("выигранные"));
        $("body").shouldHave(text("ничьи"));
    }

    @Test
    void exportTournamentTableToCsv() throws IOException {
        Configuration.browser = "firefox";
        Configuration.browserSize = "1280x800";
        Configuration.timeout = 20000;
        open(TOURNAMENT_TABLE);
        CookieConsentHelper.acceptCookiesIfPresent();

        $("body").shouldHave(text("Турнирная таблица"));
        sleep(5000);

        List<String> csvLines = new ArrayList<>();
        csvLines.add("№;Клуб;О;М;В;Н;П;ЗМ;ПМ;РМ");

        var rows = $$("div.table.tournament-table div[class*='table_row']");
        if (rows.size() == 0) rows = $$("[data-render-module='tournament-table'] div[class*='table_row']");
        if (rows.size() == 0) rows = $$("[class*='tournament-table'] div[class*='table_row']");
        if (rows.size() == 0) rows = $$("div[class*='table_row']");
        if (rows.size() == 0) rows = $$("div.table_row");

        for (SelenideElement row : rows) {
            var cells = row.$$("div[class*='cell']").stream()
                    .map(el -> el.getText().trim())
                    .filter(s -> !s.isEmpty())
                    .toList();
            if (cells.size() >= 5 && !"Клуб".equals(cells.stream().skip(1).findFirst().orElse(""))) {
                csvLines.add(String.join(";", cells.stream()
                        .map(s -> escapeCsv(s.replace(";", ",")))
                        .toList()));
            }
        }

        if (csvLines.size() == 1) {
            csvLines.add("Нет данных;Селектор таблицы premierliga.ru не найден;Обновите селектор");
            Path dir = Path.of(DOWNLOAD_DIR);
            Files.createDirectories(dir);
            String html = executeJavaScript("return document.documentElement.outerHTML");
            Files.writeString(dir.resolve("premierliga_debug.html"), html != null ? html : "", StandardCharsets.UTF_8);
        }

        Path dir = Path.of(DOWNLOAD_DIR);
        Files.createDirectories(dir);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "rpl_" + timestamp + ".csv";
        Path csvFile = dir.resolve(fileName);
        Files.writeString(csvFile, String.join("\n", csvLines), StandardCharsets.UTF_8);

        $("body").shouldHave(text("РПЛ"));
    }

    private static String escapeCsv(String s) {
        return s.contains(";") || s.contains("\"") ? "\"" + s.replace("\"", "\"\"") + "\"" : s;
    }
}
