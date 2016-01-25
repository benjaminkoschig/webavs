package globaz.naos.html.assurance;

import globaz.naos.db.assurance.AFAssurance;
import java.util.Vector;

/**
 * Insérez la description du type ici. Date de création : (29.04.2003 07:57:54)
 * 
 * @author: Administrator
 */
public class AFPageAssurancesDirector {

    private Vector lesAssurances;
    private int maxParPage;

    private java.lang.String planId = new String();

    /**
     * Commentaire relatif au constructeur AFPageAssurancesDirector.
     */
    private AFPageAssurancesDirector() {
        super();
    }

    /**
     * Commentaire relatif au constructeur AFPageAssurancesDirector.
     */
    public AFPageAssurancesDirector(int newMaxParPage) {
        super();
        maxParPage = newMaxParPage;
        lesAssurances = new Vector();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 07:59:50)
     * 
     * @param assurance
     *            globaz.naos.db.assurance.AFAssurance
     */
    public void ajouter(AFAssurance assurance) {
        lesAssurances.add(assurance);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 08:12:56)
     * 
     * @param pages
     *            globaz.naos.db.assurance.AFAssurance[]
     */
    public void creerPages(AFPageAssurances[] pages) {
        AFPageAssuranceFactory factory = new AFPageAssuranceFactory();

        for (int i = 0; i < pages.length; i++) {
            AFAssurance[] currAssurances = getAssurancesForPage(i);
            boolean plusDePages = (i != pages.length - 1);
            pages[i] = factory.creerPage(currAssurances, i, plusDePages);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 08:18:59)
     * 
     * @return globaz.naos.db.assurance.AFAssurance[]
     * @param index
     *            int
     */
    public AFAssurance[] getAssurancesForPage(int index) {
        int indexDebut = maxParPage * index;
        int indexFin = indexDebut + (maxParPage - 1);

        indexFin = Math.min(indexFin, lesAssurances.size() - 1);
        int qteAssurances = (indexFin - indexDebut) + 1;

        AFAssurance[] resultat = new AFAssurance[qteAssurances];
        // System.out.println("Assurances: " + lesAssurances.size() +
        // ". IndexDebut: " + indexDebut + ". IndexFin: " + indexFin);
        for (int i = indexDebut; i <= indexFin; i++) {
            AFAssurance assuranceEnCours = (AFAssurance) lesAssurances.get(i);
            int indexResult = i - indexDebut;
            resultat[indexResult] = assuranceEnCours;
        }

        return resultat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 15:50:05)
     * 
     * @return java.lang.String
     */
    public java.lang.String getPlanId() {
        return planId;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 15:50:05)
     * 
     * @param newPlanId
     *            java.lang.String
     */
    public void setPlanId(java.lang.String newPlanId) {
        planId = newPlanId;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 08:05:29)
     * 
     * @return java.lang.String
     */
    public String terminer() {
        String hiddenStyle = " style=\"display:none\"";
        int numAssurances = lesAssurances.size();
        int numPages = 1 + (int) ((float) numAssurances / (float) maxParPage);
        AFPageAssurances[] lesPages = new AFPageAssurances[numPages];
        creerPages(lesPages);
        // --> les pages créées;
        StringBuffer resultat = new StringBuffer();
        for (int i = 0; i < lesPages.length; i++) {
            resultat.append("<TABLE id=\"assPage" + i + "\"" + (i > 0 ? hiddenStyle : "") + ">");
            resultat.append(lesPages[i].afficher(planId));
            resultat.append("</TABLE>");
            resultat.append("\n");
        }
        return resultat.toString();
    }
}
