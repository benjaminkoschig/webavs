/*
 * Créé le 28 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.db.affiliation;

import globaz.naos.db.AFAbstractViewBean;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFAttestationAffiliationViewBean extends AFAbstractViewBean {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private AFAffiliation affiliation;
    private String affiliationId;
    private String brancheEconomique;
    private String email;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFAttestationAffiliationViewBean.
     */
    public AFAttestationAffiliationViewBean() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut affiliation
     * 
     * @return la valeur courante de l'attribut affiliation
     */
    public AFAffiliation getAffiliation() {
        return affiliation;
    }

    /**
     * getter pour l'attribut affiliation id
     * 
     * @return la valeur courante de l'attribut affiliation id
     */
    public String getAffiliationId() {
        return affiliationId;
    }

    public String getBrancheEconomique() {
        return brancheEconomique;
    }

    /**
     * getter pour l'attribut email
     * 
     * @return la valeur courante de l'attribut email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return getAffiliationId();
    }

    /**
     * getter pour l'attribut selected id
     * 
     * @return la valeur courante de l'attribut selected id
     */
    public String getSelectedId() {
        return getAffiliationId();
    }

    /**
     * getter pour l'attribut tiers
     * 
     * @return DOCUMENT ME!
     */
    public TITiersViewBean getTiers() {
        return affiliation.getTiers();
    }

    /**
     * setter pour l'attribut affiliation
     * 
     * @param affiliation
     *            une nouvelle valeur pour cet attribut
     */
    public void setAffiliation(AFAffiliation affiliation) {
        this.affiliation = affiliation;
    }

    /**
     * setter pour l'attribut affiliation id
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setAffiliationId(String string) {
        affiliationId = string;
    }

    public void setBrancheEconomique(String brancheEconomique) {
        this.brancheEconomique = brancheEconomique;
    }

    /**
     * setter pour l'attribut email
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEmail(String string) {
        email = string;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String string) {
        setAffiliationId(string);
    }

    /**
     * setter pour l'attribut selected id
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setSelectedId(String string) {
        setAffiliationId(string);
    }
}
