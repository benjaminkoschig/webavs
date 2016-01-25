package globaz.hercule.db.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BTransaction;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.jade.log.JadeLogger;

public class CEControleEmployeurListViewBean extends CEControleEmployeurManager implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String prochainControle = "";

    /**
     * @see globaz.globall.db.BManager#_afterFind(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterFind(BTransaction transaction) throws Exception {

        if (size() == 1) {
            CEControleEmployeur controle = (CEControleEmployeur) getFirstEntity();
            setProchainControle(CEControleEmployeurService.findAnneeCouvertureActiveByNumAffilie(getSession(),
                    controle.getNumAffilie()));
        }
    }

    public String getAffiliationId(int index) {
        return ((CEControleEmployeur) getEntity(index)).getAffiliationId();
    }

    public String getControleEmployeurId(int index) {
        return ((CEControleEmployeur) getEntity(index)).getIdControleEmployeur();
    }

    public String getControleur(int index) {
        return ((CEControleEmployeur) getEntity(index)).getIdReviseur();
    }

    public String getControleurVisa(int index) {
        return ((CEControleEmployeur) getEntity(index)).getControleurVisa();
    }

    public String getDateDebutControle(int index) {
        return ((CEControleEmployeur) getEntity(index)).getDateDebutControle();
    }

    public String getDateEffective(int index) {
        return ((CEControleEmployeur) getEntity(index)).getDateEffective();
    }

    public String getDateFinControle(int index) {
        return ((CEControleEmployeur) getEntity(index)).getDateFinControle();
    }

    public String getDatePrevue(int index) {
        return ((CEControleEmployeur) getEntity(index)).getDatePrevue();
    }

    public String getGenreControle(int index) {
        return ((CEControleEmployeur) getEntity(index)).getGenreControle();
    }

    public String getIdAffiliation(int index) {
        return ((CEControleEmployeur) getEntity(index)).getAffiliationId();
    }

    public String getIdTiers(int index) {
        return ((CEControleEmployeur) getEntity(index)).getIdTiers();
    }

    public String getInfoTiers(int index) {
        return ((CEControleEmployeur) getEntity(index)).getInfoTiers();
    }

    public String getNomAffilie(int index) {
        String description = "";
        try {
            description = ((CEControleEmployeur) getEntity(index)).getAffiliation().getTiersNom();
        } catch (Exception e) {
            JadeLogger.warn(this, e);
        }
        return description;
    }

    public String getNouveauNumRapport(int index) {
        return ((CEControleEmployeur) getEntity(index)).getNouveauNumRapport();
    }

    public String getNumAffilie(int index) {
        return ((CEControleEmployeur) getEntity(index)).getNumAffilie();
    }

    public String getProchainControle() {
        return prochainControle;
    }

    public void setProchainControle(String prochainControle) {
        this.prochainControle = prochainControle;
    }
}
