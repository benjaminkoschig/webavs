package globaz.naos.html.wizard;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.planCaisse.AFPlanCaisse;
import globaz.naos.translation.CodeSystem;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;

/**
 * Classe pour la Construction de la liste HTML des Plan de Caisse.
 * 
 * @author: sau
 */
public class AFWizardPlanCaisseDirector {

    private List listPlanCaisse;
    private int maxParPage;
    private String noCaisse = null;
    private List planCaisseIdSelected = new ArrayList();

    private BSession session;

    /**
     * Constructeur d'AFWizardPlanCaisseDirector.
     */
    private AFWizardPlanCaisseDirector() {
        super();
    }

    /**
     * Constructeur d'AFWizardPlanCaisseDirector.
     * 
     * @param newMaxParPage
     */
    public AFWizardPlanCaisseDirector(HttpSession newSession, int newMaxParPage) {
        super();
        try {
            session = (BSession) CodeSystem.getSession(newSession);
        } catch (Exception e) {
            session = null;
        }
        maxParPage = newMaxParPage;
        listPlanCaisse = new ArrayList(20);
    }

    /**
     * 
     * Ajouter un Plan de Caisse.
     * 
     * @param planCaisse
     */
    public void ajouter(AFPlanCaisse planCaisse) {
        listPlanCaisse.add(planCaisse);
    }

    private void creerPages(AFWizardPlanCaisse[] pages) {
        AFWizardPlanCaisseFactory factory = new AFWizardPlanCaisseFactory();

        for (int i = 0; i < pages.length; i++) {
            AFPlanCaisse[] currPlanCaisse = getPlanCaisseForPage(i);
            boolean plusDePages = (i != pages.length - 1);
            pages[i] = factory.creerPage(currPlanCaisse, i, plusDePages);
        }
    }

    private AFPlanCaisse[] getPlanCaisseForPage(int index) {
        int indexDebut = maxParPage * index;
        int indexFinPage = indexDebut + (maxParPage - 1);

        int indexFin = Math.min(indexFinPage, listPlanCaisse.size() - 1);
        int qteAssurances = (indexFinPage - indexDebut) + 1;

        AFPlanCaisse[] resultat = new AFPlanCaisse[qteAssurances];
        for (int i = indexDebut; i <= indexFinPage; i++) {

            int indexResult = i - indexDebut;
            if (i <= indexFin) {
                AFPlanCaisse planCaisseEnCours = (AFPlanCaisse) listPlanCaisse.get(i);
                resultat[indexResult] = planCaisseEnCours;
            } else {
                resultat[indexResult] = null;
            }
        }
        return resultat;
    }

    public List getPlanCaisseIdSelected() {
        return planCaisseIdSelected;
    }

    public void setFromNoCaisse(String no) {
        noCaisse = no;
    }

    public void setPlanCaisseIdSelected(List list) {
        planCaisseIdSelected = list;
    }

    /**
     * Afficher la Liste HTML.
     * 
     * @return
     */
    public String terminer() {
        String hiddenStyle = " style=\"display:none\"";
        int numAssurances = listPlanCaisse.size();

        int numPages = numAssurances / maxParPage;
        if ((numAssurances % maxParPage) > 0) {
            numPages++;
        }

        AFWizardPlanCaisse[] lesPages = new AFWizardPlanCaisse[numPages];
        creerPages(lesPages);

        // --> les pages créées;
        StringBuffer resultat = new StringBuffer();
        if (lesPages.length > 0) {
            for (int i = 0; i < lesPages.length; i++) {
                String currPage = lesPages[i].afficher(planCaisseIdSelected);

                boolean display = false;
                if (!JadeStringUtil.isEmpty(noCaisse)) {
                    // filtre sur la caisse n'afficher que la première page
                    if (i == 0) {
                        display = true;
                    }
                } else {
                    if ((planCaisseIdSelected.size() == 0) && (i == 0)) {
                        display = true;
                    } else {
                        display = lesPages[i].isToDisplay();
                    }
                }

                resultat.append("<TABLE id=\"planCaissePage" + i + "\"" + (display ? "" : hiddenStyle) + ">");
                resultat.append("<TR><TD height=\"25\"></TD><TD><b>");
                resultat.append(session.getLabel("1500"));
                resultat.append("</B></TD><TD><B>");
                resultat.append(session.getLabel("1510"));
                resultat.append("</B></TD><TD><B>");
                resultat.append(session.getLabel("1520"));
                resultat.append("</B></TD><TD><B>");
                resultat.append(session.getLabel("1521"));
                resultat.append("</B></TD></TR>");
                resultat.append(currPage);
                resultat.append("</TABLE>");
                resultat.append("\n");
            }
        } else {
            resultat.append("<TABLE id=\"planCaissePage\">");
            for (int i = 0; i < maxParPage + 1; i++) {
                if (i == (maxParPage / 2)) {
                    resultat.append("<TR height=\"25\"><TD>");
                    resultat.append(session.getLabel("1530"));
                    resultat.append("</TD></TR>");
                } else {
                    resultat.append("<TR height=\"25\"><TD></TD></TR>");
                }
            }
            resultat.append("</TABLE>");
            resultat.append("\n");
        }
        return resultat.toString();
    }
}
