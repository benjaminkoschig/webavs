package globaz.pavo.db.inscriptions.declaration;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CIDeclarationSummaryHTMLOutputCotPers implements ICIDeclarationOutput {

    private FWCurrency gtMontantCtrl = new FWCurrency(0L);
    private Long gtNbCtrl = new Long(0L);
    private TreeMap<?, ?> hMontantInscriptionsCI = null;
    private TreeMap<?, ?> hMontantInscriptionsErreur = null;
    private TreeMap<?, ?> hMontantInscriptionsNegatives = null;
    private TreeMap<?, ?> hMontantInscriptionsSuspens = null;
    private TreeMap<?, ?> hMontantInscritionsTraites = null;
    private TreeMap<?, ?> hNbrInscriptionsCI = null;
    private TreeMap<?, ?> hNbrInscriptionsErreur = null;
    private TreeMap<?, ?> hNbrInscriptionsNegatives = null;
    private TreeMap<?, ?> hNbrInscriptionsSuspens = null;
    private TreeMap<?, ?> hNbrInscriptionsTraites = null;
    private BSession session = null;

    private boolean simulation = false;
    private TreeMap<?, ?> tableJournaux = null;

    @Override
    public String getOutputFile() {
        try {

            File f = File.createTempFile(getSession().getLabel("SUMMARY_DATEN_SUM") + JACalendar.today().toStrAMJ()
                    + "_", ".html");
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
    public void setData(Object params) {

        ArrayList<?> paramList = (ArrayList<?>) params;
        hNbrInscriptionsTraites = (TreeMap<?, ?>) paramList.get(0);
        hMontantInscritionsTraites = (TreeMap<?, ?>) paramList.get(1);
        hNbrInscriptionsErreur = (TreeMap<?, ?>) paramList.get(2);
        hMontantInscriptionsErreur = (TreeMap<?, ?>) paramList.get(3);
        hNbrInscriptionsSuspens = (TreeMap<?, ?>) paramList.get(4);
        hMontantInscriptionsSuspens = (TreeMap<?, ?>) paramList.get(5);
        hNbrInscriptionsCI = (TreeMap<?, ?>) paramList.get(6);
        hMontantInscriptionsCI = (TreeMap<?, ?>) paramList.get(7);
        tableJournaux = (TreeMap<?, ?>) paramList.get(8);
        gtMontantCtrl = (FWCurrency) paramList.get(9);
        gtNbCtrl = (Long) paramList.get(10);
        hNbrInscriptionsNegatives = (TreeMap<?, ?>) paramList.get(13);
        hMontantInscriptionsNegatives = (TreeMap<?, ?>) paramList.get(14);
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

            // pour grand total :
            FWCurrency gtMontantErr = new FWCurrency();
            FWCurrency gtMontantSuspens = new FWCurrency();
            FWCurrency gtMontantCI = new FWCurrency();
            FWCurrency gtMontantTotal = new FWCurrency();
            FWCurrency gtMontantNegatives = new FWCurrency();
            long gtNbErr = 0;
            long gtNbSuspens = 0;
            long gtNbCI = 0;
            long gtNbTotal = 0;
            long gtNbNegatives = 0;

            Set<?> keySet = tableJournaux.keySet();

            Iterator<?> it = keySet.iterator();

            ps.println("<html>");
            ps.println("<head>");
            ps.println("<title>" + getSession().getLabel("DT_RESUME_TITRE_PRINC") + "</title>");
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

            ps.println("<table>");
            if (it.hasNext()) {
                String itKey = (String) it.next();
                Long tmpNbr = new Long(0L);
                // Erreurs
                FWCurrency tmpMontant = new FWCurrency(0L);
                tmpNbr = (Long) hNbrInscriptionsErreur.get(itKey);
                tmpMontant = (FWCurrency) hMontantInscriptionsErreur.get(itKey);
                gtNbErr += tmpNbr.longValue();
                gtMontantErr.add(tmpMontant);

                // Suspens
                tmpNbr = (Long) hNbrInscriptionsSuspens.get(itKey);
                tmpMontant = (FWCurrency) hMontantInscriptionsSuspens.get(itKey);
                gtNbSuspens += tmpNbr.longValue();
                gtMontantSuspens.add(tmpMontant);

                // CI
                tmpNbr = (Long) hNbrInscriptionsCI.get(itKey);
                tmpMontant = (FWCurrency) hMontantInscriptionsCI.get(itKey);
                gtNbCI += tmpNbr.longValue();
                gtMontantCI.add(tmpMontant);

                // Total
                tmpNbr = (Long) hNbrInscriptionsTraites.get(itKey);
                tmpMontant = (FWCurrency) hMontantInscritionsTraites.get(itKey);
                gtNbTotal += tmpNbr.longValue();
                gtMontantTotal.add(tmpMontant);

                // Inscriptions négatives
                tmpNbr = (Long) hNbrInscriptionsNegatives.get(itKey);
                tmpMontant = (FWCurrency) hMontantInscriptionsNegatives.get(itKey);
                gtNbNegatives += tmpNbr.longValue();
                gtMontantNegatives.add(tmpMontant);

            }
            // Grand Total
            // -----------

            ps.print("<tr class='affilieTr' ");
            // si pas encore de page break, on en fait un

            ps.println(" ><td class='affilieTd' colspan='3'>TOTAL</td></tr>");

            ps.println("<td>" + getSession().getLabel("DT_RESUME_NB_INS_ERREUR") + ":</td>");
            ps.println("<td align='right'>" + gtNbErr + "</td>");
            ps.println("<td>&nbsp;</td><td align='right'>" + gtMontantErr.toStringFormat() + "</td>");
            ps.println("</tr>");

            ps.println("<tr>");
            ps.println("<td>" + getSession().getLabel("DT_RESUME_NB_INS_SUSPENS") + ":</td> ");
            ps.println("<td align='right'>" + gtNbSuspens + "</td>");
            ps.println("<td>&nbsp;</td><td align='right'>" + gtMontantSuspens.toStringFormat() + "</td>");
            ps.println("</tr>");

            ps.println("<tr>");
            ps.println("<td>" + getSession().getLabel("DT_RESUME_NB_INS_CI") + ":</td> ");
            ps.println("<td class='total' align='right'>" + gtNbCI + "</td>");
            ps.println("<td>&nbsp;</td><td class='total' align='right'>" + gtMontantCI.toStringFormat() + "</td>");
            ps.println("</tr>");

            ps.println("<tr>");
            ps.println("<td>" + getSession().getLabel("DT_RESUME_NB_INS_TOTAL") + ":</td> ");
            ps.println("<td align='right'>" + gtNbTotal + "</td>");
            ps.println("<td>&nbsp;</td><td align='right'>" + gtMontantTotal.toStringFormat() + "</td>");
            ps.println("</tr>");

            ps.println("<tr>");
            ps.println("<td>" + getSession().getLabel("MSG_DT_INSCRIPTIONS_NEGATIVES_TOTAL") + ":</td> ");
            ps.println("<td align='right'>" + gtNbNegatives + "</td>");
            ps.println("<td>&nbsp;</td><td align='right'>" + gtMontantNegatives.toStringFormat() + "</td>");
            ps.println("</tr>");

            ps.println("<tr><td>");
            ps.println("</td></tr>");

            ps.println("<tr>");
            ps.println("<td>" + getSession().getLabel("DT_RESUME_NB_CTRL") + ":</td> ");
            ps.println("<td align='right'>" + gtNbCtrl.longValue() + "</td>");
            ps.println("<td>&nbsp;</td><td align='right'>" + gtMontantCtrl.toStringFormat() + "</td>");
            ps.println("</tr>");
            ps.println("<tr>");
            ps.println("<td>");
            ps.println("</td>");
            ps.println("<tr>");
            ps.println("<tr>");
            ps.println("<td>");
            ps.println("Ref : 0161CCI");
            ps.println("</td>");
            ps.println("</tr>");
            ps.println("</table>");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            ps.println("</body></html>");
            ps.close();
        }
    }

}