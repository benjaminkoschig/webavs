package globaz.aquila.service.taxes;

import globaz.aquila.db.access.batch.COCalculTaxe;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * Insérez la description du type ici. Date de création : (12.06.2002 09:25:59)
 * 
 * @author Administrator
 */
public class COTaxe implements Serializable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = 1524280621154805206L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private COCalculTaxe calculTaxe;
    private String idCalculTaxe;
    private boolean imputerTaxe;
    private String libelle = "";
    private String montantBase;
    private String montantTaxe;

    private String taux;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Commentaire relatif au constructeur CATaxe.
     */
    public COTaxe() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut id calcul taxe.
     * 
     * @return la valeur courante de l'attribut id calcul taxe
     */
    public String getIdCalculTaxe() {
        return idCalculTaxe;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public String getLibelle(BSession session) {
        if (JadeStringUtil.isBlank(libelle)) {
            try {
                return loadCalculTaxe(session).getDescription(session.getIdLangueISO());
            } catch (Exception e) {
                return "";
            }
        } else {
            return libelle;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 09:46:24)
     * 
     * @return java.lang.String
     */
    public String getMontantBase() {
        return montantBase;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 09:47:10)
     * 
     * @return java.lang.String
     */
    public String getMontantTaxe() {
        return montantTaxe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.06.2002 11:07:14)
     * 
     * @return globaz.framework.util.FWCurrency
     */
    public FWCurrency getMontantTaxeToCurrency() {
        return new FWCurrency(getMontantTaxe());
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 09:46:43)
     * 
     * @return java.lang.String
     */
    public String getTaux() {
        return taux;
    }

    /**
     * getter pour l'attribut imputer taxe.
     * 
     * @return la valeur courante de l'attribut imputer taxe
     */
    public boolean isImputerTaxe() {
        return imputerTaxe;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public COCalculTaxe loadCalculTaxe(BSession session) throws COTaxeException {
        if ((calculTaxe == null) && !JadeStringUtil.isEmpty(idCalculTaxe)) {
            calculTaxe = new COCalculTaxe();
            calculTaxe.setIdCalculTaxe(idCalculTaxe);
            calculTaxe.setSession(session);
            try {
                calculTaxe.retrieve();
            } catch (Exception e) {
                throw new COTaxeException("Error loadCalculTaxe : " + e.toString());
            }
        }

        return calculTaxe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.06.2002 16:23:16)
     * 
     * @param newCalculTaxe
     *            globaz.osiris.db.contentieux.CACalculTaxe
     */
    public void setCalculTaxe(COCalculTaxe newCalculTaxe) {
        idCalculTaxe = newCalculTaxe.getIdCalculTaxe();
    }

    /**
     * setter pour l'attribut id calcul taxe.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdCalculTaxe(String string) {
        idCalculTaxe = string;
    }

    /**
     * setter pour l'attribut imputer taxe.
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setImputerTaxe(boolean b) {
        imputerTaxe = b;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setLibelle(String string) {
        libelle = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 09:46:24)
     * 
     * @param newMontantBase
     *            java.lang.String
     */
    public void setMontantBase(String newMontantBase) {
        montantBase = newMontantBase;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 09:47:10)
     * 
     * @param newMontantTaxe
     *            java.lang.String
     */
    public void setMontantTaxe(String newMontantTaxe) {
        montantTaxe = newMontantTaxe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 09:46:43)
     * 
     * @param newTaux
     *            java.lang.String
     */
    public void setTaux(String newTaux) {
        taux = newTaux;
    }

}
