package globaz.naos.html.assurance;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.planAssurance.AFPlanAssurance;
import globaz.naos.db.planAssurance.AFPlanAssuranceListViewBean;
import java.util.Vector;

class AFPageAssurances {

    private Vector lesAssurances = new Vector();
    private int nextPage = -1;
    private int numPage = -1;
    private int prevPage = -1;

    public AFPageAssurances() {

    }

    public String afficher(String planId) {
        StringBuffer resultat = new StringBuffer();
        for (int i = 0; i < lesAssurances.size(); i++) {
            resultat.append(afficherAssurance((AFAssurance) lesAssurances.get(i), planId));
            resultat.append("\n");
        }
        resultat.append("\n");
        resultat.append(afficherPiedDePage());
        return resultat.toString();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 09:16:49)
     * 
     * @return java.lang.String
     * @param globaz
     *            .naos.html.assurance.afficherAssurance
     */
    private String afficherAssurance(AFAssurance assurance, String PlanId) {
        StringBuffer resultat = new StringBuffer();

        AFPlanAssuranceListViewBean planAssurance = new AFPlanAssuranceListViewBean();
        planAssurance.setForPlanId(PlanId);
        planAssurance.setSession(assurance.getSession());
        try {
            // si planId est égal à nul c'est que nous sommes en méthode _add
            if (JadeStringUtil.isEmpty(PlanId)) {
                resultat.append("<TR><TD><input name=\"");
                resultat.append(assurance.getAssuranceId());
                resultat.append("\" type=\"checkbox\"></TD>");
            } else {
                planAssurance.find();
                // String tmp = "1";
                boolean found = false;
                resultat.append("<TR><TD><input name=\"");
                resultat.append(assurance.getAssuranceId());

                for (int i = 0; i < planAssurance.size(); i++) {
                    AFPlanAssurance plan = (AFPlanAssurance) planAssurance.getEntity(i);
                    // Recheche pour savoir si on doit cocher la checkbox
                    if (plan.getAssuranceId().equals(assurance.getAssuranceId())) {
                        found = true;
                        break;
                    }
                }
                // si trouvé
                if (found) {
                    resultat.append("\" type=\"checkbox\" checked=true></TD>");
                    // si pas trouvé
                } else {
                    resultat.append("\" type=\"checkbox\"></TD>");
                }
                /*
                 * resultat.append("<TR><TD><input name=\""); resultat.append(assurance.getAssuranceId());
                 * resultat.append("\" type=\"checkbox\" checked=true></TD>"); //sortie directe break;
                 * 
                 * }else{ if(tmp.equalsIgnoreCase("1")){ resultat.append("<TR><TD><input name=\"");
                 * resultat.append(assurance.getAssuranceId()); resultat.append("\" type=\"checkbox\"></TD>"); // tmp
                 * mis à 2 pour pas créer plusieur case checkbox pour une assurance tmp_ tmp="2"; } }
                 * 
                 * }
                 */
            }
            // création de la zone libellé assurance
            resultat.append("<TD><INPUT size=\"80\" maxlength=\"80\" type=\"text\"");
            resultat.append(" value=\"");
            resultat.append(assurance.getAssuranceLibelle());
            resultat.append("\" tabindex=\"-1\" readOnly>");
            resultat.append("</TD></TR>");
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return resultat.toString();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 09:24:18)
     * 
     * @return java.lang.String
     */
    private String afficherPiedDePage() {
        StringBuffer resultat = new StringBuffer();
        resultat.append("<TR><TD>");
        if (getPrevPage() >= 0) {
            resultat.append("<B><A href=\"javascript:showPartie('assPage");
            resultat.append(getPrevPage());
            resultat.append("', 'assPage");
            resultat.append(getNumPage());
            resultat.append("')\">&lt;-</A></B> ");
        }
        resultat.append(" ");
        resultat.append(numPage + 1);
        if (getNextPage() >= 0) {
            resultat.append(" <B><A href=\"javascript:showPartie('assPage");
            resultat.append(getNextPage());
            resultat.append("', 'assPage");
            resultat.append(getNumPage());
            resultat.append("')\">-&gt;</A></B>");
        }
        resultat.append("</TR></TD>");

        return resultat.toString();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 09:09:45)
     * 
     * @param assurance
     *            globaz.naos.db.assurance.AFAssurance
     */
    public void ajouter(AFAssurance assurance) {
        lesAssurances.add(assurance);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 08:44:20)
     * 
     * @return int
     */
    public int getNextPage() {
        return nextPage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 08:43:50)
     * 
     * @return int
     */
    public int getNumPage() {
        return numPage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 08:44:06)
     * 
     * @return int
     */
    public int getPrevPage() {
        return prevPage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 08:44:20)
     * 
     * @param newNextPage
     *            int
     */
    public void setNextPage(int newNextPage) {
        nextPage = newNextPage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 08:43:50)
     * 
     * @param newNumPage
     *            int
     */
    public void setNumPage(int newNumPage) {
        numPage = newNumPage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 08:44:06)
     * 
     * @param newPrevPage
     *            int
     */
    public void setPrevPage(int newPrevPage) {
        prevPage = newPrevPage;
    }
}
