package cursor.part00230;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.List;

import static com.codeborne.selenide.Selenide.$$;

/**
 * Вспомогательный класс для выгрузки турнирных таблиц европейских лиг в CSV.
 */
public final class LeagueTableExportHelper {

    private LeagueTableExportHelper() {}

    public static ElementsCollection collectRows() {
        var rows = $$("table tbody tr");
        if (rows.size() == 0) rows = $$("table tr");
        if (rows.size() == 0) rows = $$("[role='row']");
        if (rows.size() == 0) rows = $$("div[class*='table_row']");
        if (rows.size() == 0) rows = $$("div[class*='tableRow']");
        if (rows.size() == 0) rows = $$("div[class*='standings-row']");
        if (rows.size() == 0) rows = $$("tr[class*='row']");
        return rows;
    }

    public static List<String> collectCells(SelenideElement row) {
        var td = row.$$("td").stream().map(el -> el.getText().trim()).filter(s -> !s.isEmpty()).toList();
        var th = row.$$("th").stream().map(el -> el.getText().trim()).filter(s -> !s.isEmpty()).toList();
        var div = row.$$("div[class*='cell']").stream().map(el -> el.getText().trim()).filter(s -> !s.isEmpty()).toList();
        if (!td.isEmpty()) return td;
        if (!th.isEmpty()) return th;
        return div;
    }

    public static boolean isHeaderRow(List<String> cells) {
        if (cells.isEmpty()) return true;
        String first = cells.get(0).toLowerCase();
        String second = cells.size() > 1 ? cells.get(1).toLowerCase() : "";
        return first.equals("#") || first.equals("pos") || first.equals("pos.") || first.equals("№")
                || second.equals("club") || second.equals("team") || second.equals("клуб");
    }

    public static String escapeCsv(String s) {
        return s.contains(";") || s.contains("\"") ? "\"" + s.replace("\"", "\"\"") + "\"" : s;
    }
}
