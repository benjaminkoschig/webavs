package globaz.naos.db.affiliation;

import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFFicheCartothequeViewBean extends AFAbstractViewBean {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private AFAffiliation affiliation;
    private String affiliationId;
    private String dateSituation;

    private String email;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFFicheCartothequeViewBean.
     */
    public AFFicheCartothequeViewBean() {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut affiliation.
     * 
     * @return la valeur courante de l'attribut affiliation
     */
    public AFAffiliation getAffiliation() {
        return affiliation;
    }

    /**
     * getter pour l'attribut affiliation id.
     * 
     * @return la valeur courante de l'attribut affiliation id
     */
    public String getAffiliationId() {
        return affiliationId;
    }

    /**
     * getter pour l'attribut date situation.
     * 
     * @return la valeur courante de l'attribut date situation
     */
    public String getDateSituation() {
        if (JadeStringUtil.isEmpty(dateSituation)) {
            // date vide, date du jour ou début d'affiliation si > date du jour
            String today = JACalendar.todayJJsMMsAAAA();
            try {
                if (BSessionUtil.compareDateFirstGreater(getSession(), getAffiliation().getDateDebut(), today)) {
                    dateSituation = getAffiliation().getDateDebut();
                } else if (!JadeStringUtil.isEmpty(getAffiliation().getDateFin())
                        && BSessionUtil.compareDateFirstLower(getSession(), getAffiliation().getDateFin(), today)) {
                    dateSituation = getAffiliation().getDateFin();
                } else {
                    dateSituation = today;
                }
            } catch (Exception ex) {
                dateSituation = today;
            }
        }
        return dateSituation;
    }

    /**
     * getter pour l'attribut email.
     * 
     * @return la valeur courante de l'attribut email
     */
    public String getEmail() {
        return email;
    }

    /**
     * getter pour l'attribut id.
     * 
     * @return la valeur courante de l'attribut id
     */
    @Override
    public String getId() {
        return affiliationId;
    }

    /**
     * getter pour l'attribut selected id.
     * 
     * @return la valeur courante de l'attribut selected id
     */
    public String getSelectedId() {
        return affiliationId;
    }

    /**
     * getter pour l'attribut tiers.
     * 
     * @return DOCUMENT ME!
     */
    public TITiersViewBean getTiers() {
        return affiliation.getTiers();
    }

    /**
     * setter pour l'attribut affiliation.
     * 
     * @param affiliation
     *            une nouvelle valeur pour cet attribut
     */
    public void setAffiliation(AFAffiliation affiliation) {
        this.affiliation = affiliation;
    }

    /**
     * setter pour l'attribut affiliation id.
     * 
     * @param affiliationId
     *            une nouvelle valeur pour cet attribut
     */
    public void setAffiliationId(String affiliationId) {
        this.affiliationId = affiliationId;
    }

    /**
     * setter pour l'attribut date situation.
     * 
     * @param dateSituation
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateSituation(String dateSituation) {
        this.dateSituation = dateSituation;
    }

    /**
     * setter pour l'attribut email.
     * 
     * @param email
     *            une nouvelle valeur pour cet attribut
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * setter pour l'attribut id.
     * 
     * @param id
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setId(String id) {
        affiliationId = id;
    }

    /**
     * setter pour l'attribut selected id.
     * 
     * @param selectedId
     *            une nouvelle valeur pour cet attribut
     */
    public void setSelectedId(String selectedId) {
        affiliationId = selectedId;
    }
}
