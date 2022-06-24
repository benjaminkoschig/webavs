package ch.globaz.eform.utils;

import ch.globaz.common.util.Dates;
import ch.globaz.eform.business.models.GFFormulaireModel;
import ch.globaz.eform.business.models.sedex.GFSedexModel;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;

public class GFFileUtils {
    public static void downloadFile(HttpServletResponse response, String name, byte buf[]) throws IOException {
        OutputStream os = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment;filename=" + name);
        os.write(buf);
        os.close();
    }

    public static String generateFilePath(GFFormulaireModel dbModel) {
        LocalDate date = Dates.toDate(dbModel.getDate());

        return date == null ? "" : date.getYear() + File.separator + date.getMonth().getValue() + File.separator + date.getDayOfMonth() + File.separator;
    }

    public static String generateFilePath(GFSedexModel model) {
        LocalDate date = model.getMessageDate();

        return date == null ? "" : date.getYear() + File.separator + date.getMonth().getValue() + File.separator + date.getDayOfMonth() + File.separator;
    }
}
