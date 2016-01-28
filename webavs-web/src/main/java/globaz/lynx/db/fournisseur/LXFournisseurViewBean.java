package globaz.lynx.db.fournisseur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.lynx.service.tiers.LXTiersService;

/**
 * Le viewBean de l'entité Fournisseur.
 * 
 * @author sco
 */
public class LXFournisseurViewBean extends LXFournisseur implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final Object[] METHODES_SELECT_TIERS = new Object[] {
            new String[] { "setIdTiersDepuisPyxis", "idTiers" },
            new String[] { "setNomDepuisPyxis", "designation1_tiers" },
            new String[] { "setComplementDepuisPyxis", "designation2_tiers" } };

    private boolean retourDepuisPyxis = false;

    /**
     * Constructeur de LXFournisseurViewBean.
     */
    public LXFournisseurViewBean() {
        super();
    }

    /**
     * Return l'adresse. Si non résolu, vide car utilisé par l'écran.
     * 
     * @return
     */
    public String getAdresse() {
        try {
            return LXTiersService.getAdresseFournisseurAsString(getSession(), null, getIdTiers());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Return l'adresse. Si non résolu, vide car utilisé par l'écran.
     * 
     * @return
     */
    public String getAdressePaiement() {
        try {
            return LXTiersService.getAdresseFournisseurPaiementAsString(getSession(), null, getIdTiers());
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    /**
     * Mise à jour du nom depuis le module pyxis.
     * 
     * @param idTiers
     */
    public void setComplementDepuisPyxis(String complement) {
        setComplement(complement);
        retourDepuisPyxis = true;
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
