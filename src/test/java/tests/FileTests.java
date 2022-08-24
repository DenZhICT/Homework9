package tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import domain.Anime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public class FileTests {
    ZipFileOpener zipFile = new ZipFileOpener();
    private String zipArchiveName = "SomeFiles.zip", pdfFileName = "888.pdf", xlsxFileName = "razch.xlsx", csvFileName = "dog.csv", jsonFileName = "anime.json";

    @DisplayName("PDF file parse from .zip archive")
    @Test
    void checkPDFFile() throws Exception {
        try (InputStream isPDF = zipFile.getFile(zipArchiveName, pdfFileName)) {
            PDF pdfFile = new PDF(isPDF);
            assertThat(pdfFile.text).contains("Типовой расчет по математике");
        }
    }

    @DisplayName("XLSX file parse from .zip archive")
    @Test
    void checkXLSXFile() throws Exception {
        try (InputStream isXlSX = zipFile.getFile(zipArchiveName, xlsxFileName)) {
            XLS xlsFile = new XLS(isXlSX);
            assertThat(xlsFile.excel.getSheetAt(0).getRow(8).getCell(25).getStringCellValue()).contains("dMnh");
        }
    }

    @DisplayName("CSV file parse from .zip archive")
    @Test
    void checkCSVFile() throws Exception {
        try (InputStream isCSV = zipFile.getFile(zipArchiveName, csvFileName)) {
            CSVReader csvFile = new CSVReader(new InputStreamReader(isCSV, UTF_8));
            List<String[]> csvContent = csvFile.readAll();
            assertThat(csvContent).contains(new String[]{"Name", "Friend", "Age"}, new String[]{"Alex", "Yes", "17"});
        }
    }

    @DisplayName("JSON file parse")
    @Test
    void checkJsonFile() throws Exception {
        try (InputStream isJSON = FileTests.class.getClassLoader().getResourceAsStream(jsonFileName)) {
            ObjectMapper objectMapper = new ObjectMapper();
            Anime anime = objectMapper.readValue(isJSON, Anime.class);
            assertThat(anime.name).isEqualTo("BNA");
            assertThat(anime.isItReleased).isEqualTo(true);
            assertThat(anime.mainCharacters[1]).isEqualTo("Shiro Ogami");
        }
    }
}
