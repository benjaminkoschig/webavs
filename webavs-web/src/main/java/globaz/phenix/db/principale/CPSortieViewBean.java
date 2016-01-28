package globaz.phenix.db.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAPassage;
import globaz.pyxis.db.tiers.TITiersViewBean;

public class CPSortieViewBean extends CPSortie implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String libellePassage = "";

    public CPSortieViewBean() {
        super();
    }

    @Override
    public void _afterRetrieve(BTransaction transaction) throws Exception {
        // --- Initialise le viewBean
        _init();
    }

    @Override
    public void _init() {
        try {
            FAPassage passage = new FAPassage();
            passage.setSession(getSession());
            passage.setIdPassage(getIdPassage());
            passage.retrieve();
            if (passage != null && !passage.isNew()) {
                setLibellePassage(passage.getLibelle());
            } else {
                setLibellePassage("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLibellePassage("");
        }
    }

    /**
     * Returns the libellePassage.
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibellePassage() {
        return libellePassage;
    }

    @Override
    public String getNomTiers() {
        String nomPrenom = "";
        try {
            if (!JadeStringUtil.isIntegerEmpty(getIdTiers())) {
                TITiersViewBean tiers = new TITiersViewBean();
                tiers.setSession(getSession());
                tiers.setIdTiers(getIdTiers());
                tiers.retrieve();
                if (tiers != null && !tiers.isNew()) {
                    nomPrenom = tiers.getPrenomNom();
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        return nomPrenom;
    }

    /**
     * Sets the libellePassage.
     * 
     * @param libellePassage
     *            The libellePassage to set
     */
    public void setLibellePassage(java.lang.String libellePassage) {
        this.libellePassage = libellePassage;
    }

}