package globaz.naos.db.decisionCotisations;

import globaz.globall.db.BSession;
import globaz.jade.common.Jade;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

public class AFDecisionCotisationsHtmlOut {

    private String filename = "";
    private ArrayList liste = null;
    private BSession session = null;
    private boolean simulation = false;

    public String getFilename() {
        return filename;
    }

    public String getOutputFile() {
        try {

            // File f = File.createTempFile("DOC_"+
            // JACalendar.today().toStrAMJ() + "_",".html");

            // File f = new
            // File(Jade.getInstance().getSharedDir()+"DOC_CIPROCESS.html");

            File f = new File(Jade.getInstance().getSharedDir() + getFilename());

            System.out.print("\n fichier de logg => " + f.getAbsolutePath() + f.getName() + "\n");

            // Adresse serveur => Jade.getInstance().getSharedDir()

            // File f = new File("d:\\temp");

            // f.deleteOnExit();

            FileOutputStream out = new FileOutputStream(f);
            write(out);
            out.close();
            return f.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return "";
        }
    }

    /**
     * Returns the session.
     * 
     * @return BSession
     */
    public BSession getSession() {
        return session;
    }

    /**
     * Returns the simulation.
     * 
     * @return boolean
     */
    public boolean isSimulation() {
        return simulation;
    }

    public void setData(Object obj) {
        liste = (ArrayList) obj;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Sets the session.
     * 
     * @param session
     *            The session to set
     */
    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * Sets the simulation.
     * 
     * @param simulation
     *            The simulation to set
     */
    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }

    private void write(FileOutputStream out) {

        AFDecisionCotisationsLog log;
        PrintStream ps = new PrintStream(out);
        try {

            ps.println("<html>");
            ps.println("<head>");
            ps.println("<title>" + getSession().getLabel("TITLE_DECISION_COTISATIONS_LOG") + "</title>");
            ps.println("<style>");
            ps.println("	table {border-collapse: collapse;}");
            ps.println("	td {padding: 4px;font-family:Arial;font-size:9pt}");
            ps.println("	th {text-align:center;padding-bottom:2mm}");
            ps.println("	.titre {font-family:Arial;font-size:15pt}");
            ps.println("	.mgs {color :red;text-align:left}");
            ps.println("	.nomTd {padding-left:3mm;padding-top : 2mm;}");
            ps.println("    .nomTdFirst {padding-left:14mm;padding-top : 2mm;}");
            ps.println("	.mhr {page-break-after:always}");
            ps.println("	.affilieTd {padding: 5px;font-weight:bold;}");
            ps.println("	.infoTd{color :gray}");
            ps.println("	.errTd{color :blue;text-align:left;}");
            ps.println("	.title{padding-left:14mm;font-size:12pt;font-weight:bold;}");
            ps.println("</style>");
            ps.println("</head>");
            ps.println("<body>");

            ps.println("<table>");

            ps.println("<span class=\"titre\"> " + getSession().getLabel("TITLE_DECISION_COTISATIONS_LOG") + " </span>");

            ps.println("</br>");
            ps.println("</br>");

            ps.println("<thead>");
            ps.println("<tr>");
            ps.println("<th>" + getSession().getLabel("COL1_DECISION_COTISATIONS_LOG") + " </th>");
            ps.println("<th>" + getSession().getLabel("COL2_DECISION_COTISATIONS_LOG") + " </th>");
            ps.println("<th>" + getSession().getLabel("COL4_DECISION_COTISATIONS_LOG") + " </th>");
            ps.println("<th>" + getSession().getLabel("COL3_DECISION_COTISATIONS_LOG") + " </th>");
            ps.println("</tr>");
            ps.println("</thead>");

            for (Iterator it = liste.iterator(); it.hasNext();) {
                ps.println("<tr class = \"errTd\">");
                log = (AFDecisionCotisationsLog) it.next();
                ps.println("<td>" + log.getNumeroAffilie() + "</td>");
                ps.println("<td>" + log.getNomAffilie() + " " + log.getPrenomAffilie() + "  </td>");
                // ps.println("<td>"+ log.getPrenomAffilie()+"  </td>");
                ps.println("<td>" + log.getPlan() + "  </td>");
                ps.println("<td class = \"mgs\">" + log.getMotifRefus() + "  </td>");

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