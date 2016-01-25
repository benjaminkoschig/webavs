/*
 * Créé le 2 mai 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.db.annonceAffilie;

import globaz.naos.db.AFAbstractViewBean;

/**
 * @author sda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFImpressionMutationViewBean extends AFAbstractViewBean {// AFImpressionAnnonceBatch
    // implements
    // FWViewBeanInterface
    // {
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateAnnonce = "";
    private String eMailAddress = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut from date.
     * 
     * @return la valeur courante de l'attribut from date
     */
    public String getDateAnnonce() {
        return dateAnnonce;
    }

    /**
     * getter pour l'attribut EMail address.
     * 
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    /**
     * setter pour l'attribut from date.
     * 
     * @param fromDate
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateAnnonce(String date) {
        dateAnnonce = date;
    }

    /**
     * setter pour l'attribut EMail address.
     * 
     * @param mailAddress
     *            une nouvelle valeur pour cet attribut
     */
    public void setEMailAddress(String mailAddress) {
        eMailAddress = mailAddress;
    }

}
