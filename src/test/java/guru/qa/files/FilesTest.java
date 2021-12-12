package guru.qa.files;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilesTest {

    @BeforeAll
    static void beforeAll() {
        Configuration.startMaximized = true;
    }

    //загрузка картинки по абсолютному пути
    @Test
    @DisplayName("Загрузка файла по абсолютному пути")
    void downloadButtonShouldDisplayedAfterUploadImage() {
        Selenide.open("https://promo.com/tools/image-resizer/");
        File myPicture = new File("/home/sin/IdeaProjects/Working-with-files/src/test/resources/trees.jpeg");
        $(".dropzone-module--dropzoneFile--z_3We").find("input[type='file']").uploadFile(myPicture);

        //sleep для того, чтобы дождаться загрузки картинки
        sleep(10000);

        //проверить наличие элемента
        $(".index-module--buttonsWrapper--Sm9vv").findAll("button[type='button']").find(text("Download")).should(exist);
    }

    //загрузка файла по относительному пути
    @Test
    @DisplayName("Загрузка файла по относительному пути")
    void downloadButtonShouldDisplayedAfterUploadImageClassPath() {
        Selenide.open("https://promo.com/tools/image-resizer/");
        $(".dropzone-module--dropzoneFile--z_3We").find("input[type='file']").uploadFromClasspath("trees.jpeg");

        sleep(10000);

        //проверить наличие элемента
        $(".index-module--buttonsWrapper--Sm9vv").findAll("button[type='button']").find(text("Download")).should(exist);
    }

    //скачивание xls файла
    @Test
    @DisplayName("Скачивание XLS-файла")
    void downloadFileXlsTest() throws IOException {
        Selenide.open("http://www.24copy.ru/skachat-prajjs.html");
        File downloadXLS = $(byText("Общий прайс-лист на полиграфию.xls")).download();
        XLS parsedXLS = new XLS(downloadXLS);
        System.out.println();
        //проверка на соответствие текста в файле
        boolean checkText = parsedXLS.excel.getSheetAt(0).getRow(6).getCell(0).getStringCellValue().contains("Наш адрес: 177405");
        assertTrue(checkText);
    }

    //скачивание pdf файла
    @Test
    @DisplayName("Скачивание PDF-файла")
    void downloadFilePdfTest() throws IOException {
        Selenide.open("http://www.24copy.ru/skachat-prajjs.html");
        File downloadPDF = $(byText("Общий прайс-лист на полиграфию.pdf")).download();
        PDF parsedPDF = new PDF(downloadPDF);
        Assertions.assertEquals(23, parsedPDF.numberOfPages);
    }


    //скачивание zip архива
    @Test
    @DisplayName("Скачивание ZIP-архива")
    void downloadFileZipTest() throws FileNotFoundException {
        Selenide.open("https://ganimedsb.ru/prajs-list/prajs-zip.html");
        $(".content").find(byText("Скачать")).download();
    }

    //скачивание csv файла
    @Test
    @DisplayName("Скачивание CSV-файла")
    void downloadFileCsvTest() throws FileNotFoundException {
        Selenide.open("https://ecolan37.ru/csv/");
        $("#right").find(byText("Скачать")).download();
    }

    //скачивание mp3
    @Test
    @DisplayName("Скачивание mp3")
    void downloadFileMP3Test() throws FileNotFoundException {
        Selenide.open("https://audionerd.ru/music/wav");
        File download = $(".div_for_track_Item__info").find(byText("Скачать")).download();
        sleep(30000);
    }
    
    //парсинг csv
    @Test
    @DisplayName("Парсинг CSV файла")
    void parseCsvFileTest() throws IOException, CsvException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("example.csv")) {
            Reader reader = new InputStreamReader(is);
            CSVReader csvReader = new CSVReader(reader);
            List<String[]> string = csvReader.readAll();

            //проверка на число строк в файле
            assertEquals(4, string.size());
        }
    }
    //парсинг zip
    @Test
    @DisplayName("Парсинг ZIP архива")
    void parseZipArchive() throws IOException, CsvException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("ZIP.zip")) {
            ZipInputStream zip = new ZipInputStream(is);
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                System.out.println(entry.getName());
            }
        }

    }
}







