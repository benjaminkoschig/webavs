package globaz.helios.tools;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class WriterHelper {
    static PrintWriter out;

    public static void close() {
        out.flush();
        out.close();
    }

    public static void log(String message) {
        out.println(TimeHelper.getCurrentTime() + ";" + message);
    }

    public static void setOutputStream(OutputStream os) {
        out = new PrintWriter(os);
    }
}
