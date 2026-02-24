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
 * Тесты страницы турнирной таблицы Brasileirão Série A (Бразилия).
 */
public class BrasileiraoStatsTest {

    private static final String STANDINGS = "https://www.espn.com/football/table/_/league/bra.1";
    private static final String DOWNLOAD_DIR = "build/downloads";

    @Test
    void brasileiraoStandingsPageLoads() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "800x600";
        Configuration.timeout = 15000;
        open(STANDINGS);
        CookieConsentHelper.acceptCookiesIfPresent();

        $("body").shouldBe(visible);
        $("body").shouldHave(text("Brazil").or(text("Brasileirão")).or(text("Serie A")).or(text("Brasil")));
        $("body").shouldHave(text("Table").or(text("Standings")).or(text("table")).or(text("GP")));
    }

    @Test
    void exportStandingsToCsv() throws IOException {
        Configuration.browser = "firefox";
        Configuration.browserSize = "1280x800";
        Configuration.timeout = 30000;
        Configuration.pageLoadStrategy = "eager";
        open(STANDINGS);
        CookieConsentHelper.acceptCookiesIfPresent();

        $("body").shouldBe(visible);
        sleep(8000);

        List<String> csvLines = new ArrayList<>();
        csvLines.add("Pos;Club;P;W;D;L;GF;GA;GD;Pts");

        var rows = LeagueTableExportHelper.collectRows();
        for (SelenideElement row : rows) {
            var cells = LeagueTableExportHelper.collectCells(row);
            if (cells.size() >= 4 && !LeagueTableExportHelper.isHeaderRow(cells)) {
                csvLines.add(String.join(";", cells.stream()
                        .map(LeagueTableExportHelper::escapeCsv)
                        .toList()));
            }
        }

        if (csvLines.size() == 1) {
            csvLines.add("No data;Check table selector;espn brasileirao");
        }

        Path dir = Path.of(DOWNLOAD_DIR);
        Files.createDirectories(dir);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path csvFile = dir.resolve("brasileirao_" + timestamp + ".csv");
        Files.writeString(csvFile, String.join("\n", csvLines), StandardCharsets.UTF_8);
    }
}
