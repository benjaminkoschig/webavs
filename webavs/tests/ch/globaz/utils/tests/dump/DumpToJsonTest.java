package ch.globaz.utils.tests.dump;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import org.junit.Test;

public class DumpToJsonTest {

    @Test
    public void fileTest() throws FileNotFoundException {
        String path = "c://dev//" + "test.json";
        File f = new File(path);
        PrintWriter out = new PrintWriter(f);
        out.write("done");
        out.flush();
        out.close();

    }
}
