package ch.globaz.eform.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class GFFileUtils {

    public static void downloadFile(HttpServletResponse response, String name, byte buf[]) throws IOException {
        OutputStream os = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment;filename=" + name);
        os.write(buf);
        os.close();
    }
}
