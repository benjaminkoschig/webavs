package globaz.pavo.db.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;

public class CICompteIndividuelAttestationViewBean extends CICompteIndividuel implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateCot = "";
    private String eMailAddress = "";
    private String langueImp = "";
    private String likeInIdAffiliation = "";

    public CICompteIndividuelAttestationViewBean() throws java.lang.Exception {
    }

    @Override
    public void _afterRetrieve(BTransaction transaction) throws Exception {
        // Intialiser la langue avec celle du premier tiers correspondant au NSS du CI
        TITiersViewBean tiers = null;
        TIPersonneAvsManager mng = new TIPersonneAvsManager();
        mng.setSession(getSession());
        mng.setForNumAvsHistorique(getNssFormate());
        mng.find();
        if (mng.getSize() > 0) {
            tiers = (TITiersViewBean) mng.getFirstEntity();
            setLangueImp(tiers.getLangue());
        }
        // Si tiers ou langue vide, prendre la langue de la session
        if ((tiers == null) || tiers.isNew() || JadeStringUtil.isEmpty(tiers.getLangue())) {
            setLangueImp(TITiers.langueISOtoCodeSystem(getSession().getIdLangueISO()));
        }
    }

    public String getDateCot() {
        return dateCot;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getLangueImp() {
        return langueImp;
    }

    @Override
    public String getLikeInIdAffiliation() {
        return likeInIdAffiliation;
    }

    public void setDateCot(String dateCot) {
        this.dateCot = dateCot;
    }

    public void setEMailAddress(String mailAddress) {
        eMailAddress = mailAddress;
    }

    public void setLangueImp(String langueImp) {
        this.langueImp = langueImp;
    }

    @Override
    public void setLikeInIdAffiliation(String likeInIdAffiliation) {
        this.likeInIdAffiliation = likeInIdAffiliation;
    }

}
