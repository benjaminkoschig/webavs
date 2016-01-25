package globaz.lynx.db.societesdebitrice;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.helios.db.comptes.CGMandat;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.service.tiers.LXTiersService;

/**
 * Le viewBean de l'entité Société débitrice.
 * 
 * @author sco
 */
public class LXSocieteDebitriceViewBean extends LXSocieteDebitrice implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final Object[] METHODES_SELECT_TIERS = new Object[] {
            new String[] { "setIdTiersDepuisPyxis", "idTiers" }, new String[] { "setNomDepuisPyxis", "nom" }, };

    private boolean retourDepuisPyxis = false;

    /**
     * Constructeur de LXSocieteDebitriceViewBean.
     */
    public LXSocieteDebitriceViewBean() {
        super();
    }

    /**
     * Return l'adresse. Si non résolu, vide car utilisé par l'écran.
     * 
     * @return
     */
    public String getAdresse() {
        try {
            return LXTiersService.getAdresseSocieteAsString(getSession(), null, getIdTiers());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Return le libelle du mandat, si non résolu, vide car utilisé par l'écran
     * 
     * @return
     */
    public String getLibelleMandat() {

        String libelleMandat = "";
        if (!JadeStringUtil.isIntegerEmpty(getIdMandat())) {

            try {

                libelleMandat = CGMandat.getLibelle(getSession(), getIdMandat());

            } catch (Exception e) {
                return "";
            }

        }
        return libelleMandat;
    }

    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    /**
     * Mise à jour de l'id tiers depuis le module pyxis.
     * 
     * @param idTiers
     */
    public void setIdTiersDepuisPyxis(String idTiers) {
        setIdTiers(idTiers);
        retourDepuisPyxis = true;
    }

    /**
     * Mise à jour du nom depuis le module pyxis.
     * 
     * @param idTiers
     */
    public void setNomDepuisPyxis(String nom) {
        setNom(nom);
        retourDepuisPyxis = true;
    }

    public void setRetourDepuisPyxis(boolean retourDepuisPyxis) {
        this.retourDepuisPyxis = retourDepuisPyxis;
    }
}
