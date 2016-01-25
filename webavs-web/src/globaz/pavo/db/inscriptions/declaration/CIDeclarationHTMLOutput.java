package globaz.pavo.db.inscriptions.declaration;

import globaz.commons.nss.NSUtil;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.util.CIUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author oca
 * 
 *         format de sortie pour déclaration de salaire : fichier plat A FAire
 */
public class CIDeclarationHTMLOutput implements ICIDeclarationOutput {

    private TreeMap<?, ?> map = null;
    private BSession session = null;
    private boolean simulation = false;

    @Override
    public String getOutputFile() {
        try {

            File f = File.createTempFile(getSession().getLabel("SUMMARY_DATEN_TITLE") + "_"
                    + JACalendar.today().toStrAMJ() + "_", ".html");
            f.deleteOnExit();
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
    @Override
    public BSession getSession() {
        return session;
    }

    /**
     * Returns the simulation.
     * 
     * @return boolean
     */
    @Override
    public boolean isSimulation() {
        return simulation;
    }

    @Override
    public void setData(Object obj) {
        map = (TreeMap<?, ?>) obj;
    }

    /**
     * Sets the session.
     * 
     * @param session
     *            The session to set
     */
    @Override
    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * Sets the simulation.
     * 
     * @param simulation
     *            The simulation to set
     */
    @Override
    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }

    private void write(FileOutputStream out) {
        PrintStream ps = new PrintStream(out);
        try {

            Set<?> set = map.keySet();
            Iterator<?> it = set.iterator();
            String prevAffilieAnnee = "";
            boolean first = true;

            ps.println("<html>");
            ps.println("<head>");
            ps.println("<title>" + getSession().getLabel("DT_DETAIL_TITRE_PRINC") + "</title>");
            ps.println("<style>");
            ps.println("	td {padding-left:3mm;font-family:Courier;font-size:9pt}");
            ps.println("	.nomTd {padding-left:3mm;padding-top : 2mm;}");
            ps.println("    .nomTdFirst {padding-left:14mm;padding-top : 2mm;}");
            ps.println("	.mhr {page-break-after:always}");
            ps.println("	.affilieTd {padding-left:14mm;font-weight:bold;padding-top:5mm;padding-bottom:2mm}");
            ps.println("	.infoTd{color :gray}");
            ps.println("	.errTd{color :red}");
            ps.println("	.title{padding-left:14mm;font-size:12pt;font-weight:bold;}");
            ps.println("</style>");
            ps.println("</head>");
            ps.println("<body>");

            ps.println("<span class='title'>" + getSession().getLabel("DT_DETAIL_TITRE") + " "
                    + (isSimulation() ? getSession().getLabel("DT_SIMULATION") : "") + "</span>");
            ps.println("<table>");

            while (it.hasNext()) {
                String key = (String) it.next();
                CIDeclarationRecord rec = (CIDeclarationRecord) map.get(key);
                ArrayList<?> errors = rec.getErrors();
                ArrayList<?> info = rec.getInfo();
                ArrayList<?> ciAdd = rec.getCiAdd();
                String numeroAffilie = rec.getNumeroAffilie();

                if ((errors.size() != 0) || (info.size() != 0) || (ciAdd.size() != 0)) {

                    // header affilié / année
                    if (!prevAffilieAnnee.equals(rec.getNumeroAffilie() + rec.getAnnee())) {
                        ps.println("<tr class='affilieTr'><td class='affilieTd' colspan='5'>");
                        if (first) {
                            first = false;
                        } else {
                            ps.println("<hr class='mhr'>");
                        }
                        ps.println(CIUtil.formatNumeroAffilie(getSession(), numeroAffilie) + " - "
                                + rec.getNomAffilie() + "</td></tr>");

                        ps.println("<tr><td class='nomTdFirst' colspan='4'>"
                                + getSession().getLabel("DT_ANNEE_COTISATION") + ":  <b>" + rec.getAnnee()
                                + "</b></td></tr>");
                        ps.println("<tr class='nomTr'>");
                        ps.println("<td class='nomTdFirst'>" + getSession().getLabel("DT_DETAIL_COL1") + "</td>");
                        ps.println("<td class='nomTd'>" + getSession().getLabel("DT_DETAIL_COL2") + "</td>");
                        ps.println("<td class='nomTd' align='right'>" + getSession().getLabel("DT_DETAIL_COL3")
                                + "</td>");
                        ps.println("<td class='nomTd' align='right' >" + getSession().getLabel("DT_DETAIL_COL4")
                                + "</td>");
                        ps.println("<td class='nomTd' align='right'>" + getSession().getLabel("DT_DETAIL_COL5")
                                + "</td>");

                        ps.println("</tr>");

                    }

                    prevAffilieAnnee = rec.getNumeroAffilie() + rec.getAnnee();
                    String moisDebut = (rec.getMoisDebut() < 10) ? "0" + rec.getMoisDebut() : rec.getMoisDebut() + "";
                    String moisFin = (rec.getMoisFin() < 10) ? "0" + rec.getMoisFin() : rec.getMoisFin() + "";
                    String signe = " ";
                    if (!rec.isMontantPositif()) {
                        signe = "-";
                    }
                    FWCurrency cur = new FWCurrency(rec.getMontantEcr());

                    ps.println("<tr class='nomTr'>");
                    ps.println("<td class='nomTdFirst'>" + NSUtil.formatAVSUnknown(rec.getNumeroAvs()) + "</td>");
                    ps.println("<td class='nomTd'>" + rec.getNomPrenom() + "</td>");
                    ps.println("<td class='nomTd' align='right'><font name='courier'>" + moisDebut + "-" + moisFin
                            + " " + rec.getAnnee() + "</font></td>");

                    if (JadeStringUtil.isEmpty(rec.getGenreEcriture())) {
                        ps.println("<td class='nomTd' align='right'>" + ((rec.isMontantPositif()) ? "0" : "1")
                        // +((CIUtil.isRetraite(rec.getNumeroAvs(),Integer.parseInt(rec.getAnnee()))&&(!CIDeclaration.isAvs0(rec.getNumeroAvs()))
                        // )?"7":"1")+"</td>");
                                + "1");
                    } else {
                        ps.println("<td class='nomTd' align='right'>" + ((rec.isMontantPositif()) ? "0" : "1")
                        // +((CIUtil.isRetraite(rec.getNumeroAvs(),Integer.parseInt(rec.getAnnee()))&&(!CIDeclaration.isAvs0(rec.getNumeroAvs()))
                        // )?"7":"1")+"</td>");
                                + rec.getGenreEcriture());
                    }
                    ps.println("<td class='nomTd' align='right'>" + signe + cur.toStringFormat() + "</td>");
                    ps.println("</tr>");

                }

                if (errors.size() != 0) {
                    Iterator<?> itErr = errors.iterator();
                    while (itErr.hasNext()) {
                        ps.println("<tr class='errTr'>");
                        ps.println("<td class='errTd' align='right'> ** " + getSession().getLabel("DT_DETAIL_ERREUR")
                                + " : </td>");
                        ps.println("<td class='errTd' colspan='4'>" + (String) itErr.next() + "</td>");
                        ps.println("</tr>");
                    }
                }

                if (info.size() != 0) {
                    Iterator<?> itInfo = info.iterator();
                    while (itInfo.hasNext()) {
                        ps.println("<tr class='infoTr'>");
                        ps.println("<td  class='infoTd' align='right'>"
                                + getSession().getLabel("DT_DETAIL_AVERTISSEMENT") + " : </td>");
                        ps.println("<td  class='infoTd'  colspan='4' >" + (String) itInfo.next() + "</td>");
                        ps.println("</tr>");
                    }
                }
                if (ciAdd.size() != 0) {
                    Iterator<?> itCiAdd = ciAdd.iterator();
                    while (itCiAdd.hasNext()) {
                        ps.println("<tr class='infoTr'>");
                        ps.println("<td  class='infoTd' align='right'>" + getSession().getLabel("DT_MSG_INFORMATION")
                                + " : </td>");
                        ps.println("<td  class='infoTd'  colspan='4' >" + (String) itCiAdd.next() + "</td>");
                        ps.println("</tr>");
                    }

                }

                if (!it.hasNext()) {
                    ps.println("<tr class='affilieTr'><td class='affilieTd' colspan='5'><hr></td></tr>");
                }

            }
            ps.println("<tr>");
            ps.println("<td>");
            ps.println("Ref : 0161CCI");
            ps.println("</td>");
            ps.println("</tr>");
            ps.println("</table>");
            // ps.println("Ref : 0160CCI");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ps.println("</body></html>");
            ps.close();
        }
    }

}