/*
 * Créé le 24 nov. 05
 */
package globaz.apg.vb.annonces;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * @author dvh
 */
public class APAnnonceSedexViewBean extends APAnnonceAPGViewBean implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private APChampsAnnonce champsAnnonce;
    private String onlyNss;

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        champsAnnonce = toChampsAnnonce();

        APPrestationManager mgr = new APPrestationManager();
        mgr.setForIdAnnonce(getChampsAnnonce().getMessageId());
        mgr.setSession(getSession());
        mgr.find();

        if (mgr.getSize() > 0) {
            APPrestation prest = (APPrestation) mgr.getFirstEntity();
            getChampsAnnonce().setIdDroit(prest.getIdDroit());
            APDroitLAPG droit = new APDroitLAPG();
            droit.setIdDroit(prest.getIdDroit());
            droit.setSession(getSession());
            droit.retrieve();
            if (droit != null) {
                getChampsAnnonce().setIdDroitParent(droit.getIdDroitParent());
            }
        }

        if (!JadeStringUtil.isBlank(getNumeroAssure())) {
            PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNumeroAssure());
            if (tiers != null) {
                getChampsAnnonce().setInsurantBirthDate(tiers.getDateNaissance());
                getChampsAnnonce().setInsurantSexe(tiers.getSexe());
            }
        }
    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        fromChampsAnnonce(champsAnnonce);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        fromChampsAnnonce(champsAnnonce);

        super._validate(statement);
    }

    /**
     * @return the champsAnnonce
     */
    public APChampsAnnonce getChampsAnnonce() {
        return champsAnnonce;
    }

    /**
     * @return the onlyNss
     */
    public String getOnlyNss() {
        return onlyNss;
    }

    public boolean isOnlyNss() {
        return "true".equals(onlyNss);
    }

    /**
     * @param champsAnnonce
     *            the champsAnnonce to set
     */
    public void setChampsAnnonce(APChampsAnnonce champsAnnonce) {
        this.champsAnnonce = champsAnnonce;
    }

    /**
     * @param onlyNss
     *            the onlyNss to set
     */
    public void setOnlyNss(String onlyNss) {
        this.onlyNss = onlyNss;
    }

}
