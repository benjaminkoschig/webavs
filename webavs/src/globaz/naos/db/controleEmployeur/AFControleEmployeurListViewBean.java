package globaz.naos.db.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;

public class AFControleEmployeurListViewBean extends AFControleEmployeurManager implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String prochainControle = "";

    public String getAffiliationId(int index) {

        return ((AFControleEmployeur) getEntity(index)).getAffiliationId();
    }

    public String getControleEmployeurId(int index) {

        return ((AFControleEmployeur) getEntity(index)).getControleEmployeurId();
    }

    public String getControleur(int index) {

        return ((AFControleEmployeur) getEntity(index)).getControleur();
    }

    public String getControleurVisa(int index) {

        return ((AFControleEmployeur) getEntity(index)).getControleurVisa();
    }

    public String getDateDebutControle(int index) {

        return ((AFControleEmployeur) getEntity(index)).getDateDebutControle();
    }

    public String getDateEffective(int index) {

        return ((AFControleEmployeur) getEntity(index)).getDateEffective();
    }

    public String getDateFinControle(int index) {

        return ((AFControleEmployeur) getEntity(index)).getDateFinControle();
    }

    public String getDatePrevue(int index) {

        return ((AFControleEmployeur) getEntity(index)).getDatePrevue();
    }

    public String getDescription(int index) {
        return ((AFControleEmployeur) getEntity(index)).getAffiliation().getAffilieNumero() + " "
                + ((AFControleEmployeur) getEntity(index)).getAffiliation().getTiersNom();
    }

    public String getGenreControle(int index) {

        return ((AFControleEmployeur) getEntity(index)).getGenreControle();
    }

    public String getNouveauNumRapport(int index) {
        return ((AFControleEmployeur) getEntity(index)).getNouveauNumRapport();
    }

    public String getProchainControle() {
        return prochainControle;
    }

    public void setProchainControle(String prochainControle) {
        this.prochainControle = prochainControle;
    }
}
