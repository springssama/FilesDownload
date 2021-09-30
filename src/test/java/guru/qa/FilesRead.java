package guru.qa;

import com.codeborne.xlstest.XLS;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import org.junit.jupiter.api.Test;
import com.codeborne.pdftest.PDF;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FilesRead {
    @Test
    void readTxtFile() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("test.txt")) {
            assertThat(new String(is.readAllBytes())).contains("Test data");
        }
    }

    @Test
    void readPDFFile() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("test.pdf")) {
            PDF file = new PDF(is);
            assertThat(file.creator).contains("TextEdit");
            assertThat(file.text).contains("Test data");
        }
    }

    @Test
    void readXLSXFile() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("test.xlsx")) {
            XLS parsed = new XLS(is);
            assertThat(parsed.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue()).contains("Image");
        }
    }

    @Test
    void readZipFile() throws Exception {
        ZipFile zipFile = new ZipFile("./src/test/resources/test.zip");
        if (zipFile.isEncrypted()) {
            zipFile.setPassword("123456qwe");
        }
        zipFile.extractAll("./src/test/resources/test");

        FileHeader file = zipFile.getFileHeader("test.txt");
        assertThat(file.getFileName()).contains("test.txt");
    }

    @Test
    void readDocFile() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("test.docx")) {
            XWPFDocument document = new XWPFDocument(is);
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            assertThat(paragraphs.get(0).getText()).contains("Lorem ipsum");
        }
    }
}
