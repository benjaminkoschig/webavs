package globaz.naos.html.wizard;

import globaz.jade.log.JadeLogger;
import globaz.naos.db.planCaisse.AFPlanCaisse;
import globaz.naos.translation.CodeSystem;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour la Construction d'une Page de la liste HTML des Plan de Caisse.
 * 
 * @author sau
 */
class AFWizardPlanCaisse {

    private List listPlanCaisse = new ArrayList();
    private int nextPage = -1;
    private int numPage = -1;
    private int prevPage = -1;

    private boolean toDisplay = false;

    /**
     * Constructeur d'AFWizardPlanCaisse.
     */
    public AFWizardPlanCaisse() {
    }

    /**
     * Construire la List HTML des Plan de Caisse.
     * 
     * @param planCaisseSelected
     * @return
     */
    public String afficher(List planCaisseSelected) {
        StringBuffer resultat = new StringBuffer();
        for (int i = 0; i < listPlanCaisse.size(); i++) {
            resultat.append(afficherPlanCaisse((AFPlanCaisse) listPlanCaisse.get(i), planCaisseSelected));
            resultat.append("\n");
        }
        resultat.append("\n");
        resultat.append(afficherPiedDePage());
        return resultat.toString();
    }

    private String afficherPiedDePage() {
        StringBuffer resultat = new StringBuffer();
        resultat.append("<TR><TD></TD><TD></TD><TD></TD><TD>");
        if (getPrevPage() >= 0) {
            resultat.append("<B><A href=\"javascript:showPartie('planCaissePage");
            resultat.append(getPrevPage());
            resultat.append("', 'planCaissePage");
            resultat.append(getNumPage());
            resultat.append("')\">&lt;-</A></B> ");
        }
        resultat.append(" ");
        resultat.append(numPage + 1);
        if (getNextPage() >= 0) {
            resultat.append(" <B><A href=\"javascript:showPartie('planCaissePage");
            resultat.append(getNextPage());
            resultat.append("', 'planCaissePage");
            resultat.append(getNumPage());
            resultat.append("')\">-&gt;</A></B>");
        }
        resultat.append("</TR></TD>");

        return resultat.toString();
    }

    private String afficherPlanCaisse(AFPlanCaisse planCaisse, List planCaisseSelected) {
        StringBuffer resultat = new StringBuffer();

        try {
            if (planCaisse != null) {
                resultat.append("<TR>\n<TD height=\"25\">\n<input type=\"checkbox\" id=\"check"
                        + planCaisse.getPlanCaisseId() + "\" onClick=\"updateRadio('" + planCaisse.getPlanCaisseId()
                        + "');\" name=\"planCaisseIdChk");
                resultat.append(planCaisse.getPlanCaisseId());

                boolean checked = false;
                String princStr = "";
                for (int i = 0; i < planCaisseSelected.size(); i++) {
                    String[] planCaisseId = (String[]) planCaisseSelected.get(i);
                    if (planCaisse.getPlanCaisseId().equals(planCaisseId[0])) {
                        checked = true;
                        if (CodeSystem.TYPE_ADHESION_CAISSE_PRINCIPALE.equals(planCaisseId[1])) {
                            // caisse principale
                            princStr = " checked";
                        }
                        break;
                    }
                }
                if (checked) {
                    resultat.append("\" checked><input type='hidden' name='planCaisseId" + planCaisse.getPlanCaisseId()
                            + "' value='on'></TD>\n");

                    if (planCaisseSelected.size() > 0) {
                        if (planCaisse.getPlanCaisseId().equals(
                                ((String[]) planCaisseSelected.get(planCaisseSelected.size() - 1))[0])) {
                            toDisplay = true;
                        }
                    }
                } else {
                    resultat.append("\"><input type='hidden' name='planCaisseId" + planCaisse.getPlanCaisseId()
                            + "' value='off'></TD>\n");
                }

                // Création de la zone libellé assurance
                resultat.append("<TD><INPUT size=\"5\" maxlength=\"5\" type=\"text\"");
                resultat.append(" value=\"");
                resultat.append(planCaisse.getAdministration().getCodeAdministration());
                resultat.append("\" tabindex=\"-1\" class=\"disabled\" readOnly></TD>\n");
                resultat.append("<TD><INPUT size=\"40\"  type=\"text\"");
                resultat.append(" value=\"");
                resultat.append(planCaisse.getAdministration().getNom());
                resultat.append("\" tabindex=\"-1\" class=\"disabled\" readOnly></TD>\n");
                resultat.append("<TD><INPUT size=\"40\"  type=\"text\"");
                resultat.append(" value=\"");
                resultat.append(planCaisse.getLibelle());
                resultat.append("\" tabindex=\"-1\" class=\"disabled\" readOnly></TD>\n");
                resultat.append("</TD>\n");
                resultat.append("<TD><input type=\"radio\" name=\"caissePrincipale\" value=\""
                        + planCaisse.getPlanCaisseId() + "\" id=\"princ" + planCaisse.getPlanCaisseId() + "\""
                        + princStr + ">");
                if (!checked) {
                    resultat.append("<script>document.all('princ'+" + planCaisse.getPlanCaisseId()
                            + ").style.display='none';</script>");
                }
                resultat.append("</TD>");
                resultat.append("</TR>\n");
            } else {
                resultat.append("<TR><TD height=\"25\"></TD></TR>");
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return resultat.toString();
    }

    /**
     * Ajouter un Plan de Caisse.
     * 
     * @param planCaisse
     */
    public void ajouter(AFPlanCaisse planCaisse) {
        listPlanCaisse.add(planCaisse);
    }

    public int getNextPage() {
        return nextPage;
    }

    public int getNumPage() {
        return numPage;
    }

    public int getPrevPage() {
        return prevPage;
    }

    public boolean isToDisplay() {
        return toDisplay;
    }

    public void setNextPage(int newNextPage) {
        nextPage = newNextPage;
    }

    public void setNumPage(int newNumPage) {
        numPage = newNumPage;
    }

    public void setPrevPage(int newPrevPage) {
        prevPage = newPrevPage;
    }
}
