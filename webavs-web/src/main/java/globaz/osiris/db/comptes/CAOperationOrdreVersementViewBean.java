package globaz.osiris.db.comptes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiement;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiementManager;
import globaz.pyxis.db.divers.TIApplication;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CAOperationOrdreVersementViewBean extends CAOperationOrdreVersement implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Object[] METHODES_ADMINISTRATION = new Object[] { new String[] { "idTiersDepuisPyxis",
            "idTiers" } };

    private static final Object[] METHODES_ADRESSE = new Object[] { new String[] { "setIdAdressePaiementDepuisPyxis",
            "idAvoirPaiementUnique" } };
    private static final Object[] METHODES_TIERS = new Object[] { new String[] { "idTiersDepuisPyxis", "idTiers" } };
    private String idAdressePaiementDepuisPyxis = new String();

    private String idTiersAdressePaiementDepuisPyxis = new String();
    private boolean retourDepuisPyxis = false;

    /**
     * Constructor for CAOperationOrdreVersementViewBean.
     */
    public CAOperationOrdreVersementViewBean() {
        super();
    }

    /**
     * @return the idAdressePaiementDepuisPyxis
     */
    public String getIdAdressePaiementDepuisPyxis() {
        return idAdressePaiementDepuisPyxis;
    }

    /**
     * @return the idTiersAdressePaiementDepuisPyxis
     */
    public String getIdTiersAdressePaiementDepuisPyxis() {
        return idTiersAdressePaiementDepuisPyxis;
    }

    /**
     * getter pour l'attribut methodes selection beneficiaire (pour les administrations)
     * 
     * @return la valeur courante de l'attribut methodes selection beneficiaire
     */
    public Object[] getMethodesSelectionAdministration() {
        return CAOperationOrdreVersementViewBean.METHODES_ADMINISTRATION;
    }

    /**
     * getter pour l'attribut methodes selection adresse
     * 
     * @return la valeur courante de l'attribut methodes selection adresse
     */
    public Object[] getMethodesSelectionAdresse() {
        return CAOperationOrdreVersementViewBean.METHODES_ADRESSE;
    }

    /**
     * getter pour l'attribut methodes selection beneficiaire (pour les tiers)
     * 
     * @return la valeur courante de l'attribut methodes selection beneficiaire
     */
    public Object[] getMethodesSelectionTiers() {
        return CAOperationOrdreVersementViewBean.METHODES_TIERS;
    }

    /**
     * @return the retourDepuisPyxis
     */
    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    /**
     * @param idAdressePaiementDepuisPyxis
     *            the idAdressePaiementDepuisPyxis to set
     */
    public void setIdAdressePaiementDepuisPyxis(String idAdressePaiementDepuisPyxis) {
        setIdAdressePaiement(idAdressePaiementDepuisPyxis);
        setRetourDepuisPyxis(true);
    }

    /**
     * setter pour l'attribut id tiers depuis pyxis
     * 
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiersDepuisPyxis(String idTiers) {

        TIAvoirPaiementManager mgr = new TIAvoirPaiementManager();
        mgr.setSession(getSession());
        mgr.setForIdTiers(idTiers);
        mgr.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
        mgr.setForIdApplication(TIApplication.CS_DEFAUT);

        try {
            mgr.find();
            if (!mgr.isEmpty()) {
                TIAvoirPaiement e = (TIAvoirPaiement) mgr.getFirstEntity();
                setIdAdressePaiement(e.getIdAdrPmtIntUnique());
                setRetourDepuisPyxis(true);
            } else {
                setIdAdressePaiement("0");
                setRetourDepuisPyxis(true);
            }
        } catch (Exception e1) {
        }

    }

    /**
     * @param retourDepuisPyxis
     *            the retourDepuisPyxis to set
     */
    public void setRetourDepuisPyxis(boolean retourDepuisPyxis) {
        this.retourDepuisPyxis = retourDepuisPyxis;
    }

}
