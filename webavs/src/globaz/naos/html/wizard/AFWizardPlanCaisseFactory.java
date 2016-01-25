package globaz.naos.html.wizard;

import globaz.naos.db.planCaisse.AFPlanCaisse;

/**
 * Creation des different page de la Liste HTML des Plan de Caisse.
 * 
 * @author: sau
 */
class AFWizardPlanCaisseFactory {

    /**
     * Constructeur d'AFWizardPlanCaisseFactory.
     */
    public AFWizardPlanCaisseFactory() {
        super();
    }

    /**
     * Definir les parametre des différents pages.
     * 
     * @param planCaisse
     * @param numPage
     * @param plusDePages
     * @return
     */
    public AFWizardPlanCaisse creerPage(AFPlanCaisse[] planCaisse, int numPage, boolean plusDePages) {
        AFWizardPlanCaisse resultat = new AFWizardPlanCaisse();
        for (int i = 0; i < planCaisse.length; i++) {
            resultat.ajouter(planCaisse[i]);
        }
        resultat.setNumPage(numPage);
        if (numPage > 0) {
            resultat.setPrevPage(numPage - 1);
        }
        if (plusDePages) {
            resultat.setNextPage(numPage + 1);
        }
        return resultat;
    }
}
