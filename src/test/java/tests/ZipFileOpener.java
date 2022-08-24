package tests;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipFile;

public class ZipFileOpener {
    public InputStream getFile(String archiveName, String fileName) throws Exception {
        ZipFile arch = new ZipFile(new File("src/test/resources/" + archiveName));
        InputStream is = arch.getInputStream(arch.getEntry(fileName));
        return is;
    }
}
