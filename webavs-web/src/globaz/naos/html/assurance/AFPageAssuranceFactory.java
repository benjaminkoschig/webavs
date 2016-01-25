package globaz.naos.html.assurance;

import globaz.naos.db.assurance.AFAssurance;

/**
 * Insérez la description du type ici. Date de création : (29.04.2003 08:14:45)
 * 
 * @author: Administrator
 */
class AFPageAssuranceFactory {

    /**
     * Commentaire relatif au constructeur AFPageAssuranceFactory.
     */
    public AFPageAssuranceFactory() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 08:15:38)
     * 
     * @return globaz.naos.html.assurance.AFPageAssurances
     */
    public AFPageAssurances creerPage(AFAssurance[] assurances, int numPage, boolean plusDePages) {
        AFPageAssurances resultat = new AFPageAssurances();
        for (int i = 0; i < assurances.length; i++) {
            resultat.ajouter(assurances[i]);
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
