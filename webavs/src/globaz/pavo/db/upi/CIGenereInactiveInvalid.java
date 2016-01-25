package globaz.pavo.db.upi;

import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class CIGenereInactiveInvalid {
    private ArrayList errors = null;
    private boolean inactive = false;
    private boolean invalid = false;
    private BSession session = null;

    public ArrayList getErrors() {
        return errors;
    }

    public String getOutputFile() {
        try {
            File f = null;
            if (isInactive()) {
                f = File.createTempFile(getSession().getLabel("TITRE_LISTE_INACTIVE") + JACalendar.today().toStrAMJ()
                        + "_", ".html");
            } else if (isInvalid()) {
                f = File.createTempFile(getSession().getLabel("TITRE_LISTE_INVALID") + JACalendar.today().toStrAMJ()
                        + "_", ".html");
            }
            FileOutputStream out = new FileOutputStream(f);
            write(out);
            out.close();
            return f.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return "";
        }
    }

    public BSession getSession() {
        return session;
    }

    public boolean isInactive() {
        return inactive;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setErrors(ArrayList errors) {
        this.errors = errors;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * Ecrit le contenu du fichier html
     * 
     * @param out
     */
    private void write(FileOutputStream out) {
        PrintStream ps = new PrintStream(out);
        try {

            ps.println("<html>");
            ps.println("<head>");
            ps.println("<title>" + getSession().getLabel("TITRE_LISTE_INACTIVE") + "</title>");
            ps.println("<style>");
            ps.println("	td {padding-left:15mm;font-family:Arial;font-size:10pt}");
            ps.println("	.nomTd {padding-left:10mm;padding-top : 2mm;}");
            ps.println("	.mbr {page-break-after:always}");
            ps.println("	.mbrBefore {page-break-before:always}");
            ps.println("	.affilieTd {padding-left:10mm;font-weight:bold;padding-top:5mm;padding-bottom:2mm}");
            ps.println("	.infoTd{color :gray}");
            ps.println("	.errTd{color :red}");
            ps.println("	.title{padding-left:10mm;font-size:12pt;font-weight:bold;}");
            ps.println("	.total{border-bottom:solid 1px black;}");

            ps.println("</style>");
            ps.println("</head>");
            ps.println("<body>");
            if (isInactive()) {
                ps.println("<h3>" + getSession().getLabel("TITRE_LISTE_INACTIVE") + "</h3>");
            } else if (isInvalid()) {
                ps.println("<h3>" + getSession().getLabel("TITRE_LISTE_INVALID") + "</h3>");
            }

            ps.println("<table>");
            /********************
             * Titres des colonnes
             ****************** */
            ps.println("<tr class='nomTr'><td class='nomTdFirst'>");
            ps.println(getSession().getLabel("DT_DETAIL_COL1"));
            ps.println("</td>");
            ps.println("<td class='nomTdFirst'>");
            ps.println(getSession().getLabel("ABSTI_NOM_EMPLOYEUR"));
            ps.println("</td>");
            ps.println("<td class='nomTdFirst'>");
            ps.println(getSession().getLabel("LISTE_DATENAISSANCE"));
            ps.println("</td>");
            ps.println("<td class='nomTdFirst'>");
            ps.println(getSession().getLabel("LISTE_SEXE"));
            ps.println("</td>");
            ps.println("<td class='nomTdFirst'>");
            ps.println(getSession().getLabel("LISTE_PAYS_AC"));
            ps.println("</td></tr>");
            for (int i = 0; i < errors.size(); i++) {
                ArrayList erreursIndividuelles = (ArrayList) errors.get(i);
                ps.println("<tr>");
                for (int j = 0; j < erreursIndividuelles.size(); j++) {
                    ps.println("<td>");
                    ps.println((String) erreursIndividuelles.get(j));
                    ps.println("</td>");
                }
                ps.println("</tr>");
            }

            ps.println("</table>");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            ps.println("</body></html>");
            ps.close();
        }
    }

}
