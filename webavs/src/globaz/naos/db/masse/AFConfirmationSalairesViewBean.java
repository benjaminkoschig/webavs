/*
 * Créé le 12 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.db.masse;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pyxis.db.tiers.TITiers;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFConfirmationSalairesViewBean extends AFAbstractViewBean {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private AFAffiliation _affiliation = null;
    private TITiers _tiers = null;

    // champs transmis au process
    private String affiliationId;
    private String dateEnvoi = JACalendar.todayJJsMMsAAAA();
    private String email;
    private String fromIdExterneRole = "";
    private String planAffiliationId;
    private String tillIdExterneRole = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Rechercher l'affiliation du nombre d'Assuré en fonction de son ID.
     * 
     * @return l'affiliation
     */
    public AFAffiliation getAffiliation() {
        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getAffiliationId())) {
            return null;
        }

        if (_affiliation == null) {
            _affiliation = new AFAffiliation();
            _affiliation.setISession(getSession());
            _affiliation.setAffiliationId(getAffiliationId());

            try {
                _affiliation.retrieve();
                /*
                 * if (_affiliation.hasErrors()) _affiliation = null;
                 */
            } catch (Exception e) {
                setMessage(e.getMessage());
                setMsgType(FWViewBeanInterface.ERROR);
                _affiliation = null;
            }
        }

        return _affiliation;
    }

    /**
     * getter pour l'attribut affiliation id
     * 
     * @return la valeur courante de l'attribut affiliation id
     */
    public String getAffiliationId() {
        return affiliationId;
    }

    /**
     * getter pour l'attribut date envoi
     * 
     * @return la valeur courante de l'attribut date envoi
     */
    public String getDateEnvoi() {
        return dateEnvoi;
    }

    /**
     * getter pour l'attribut email
     * 
     * @return la valeur courante de l'attribut email
     */
    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            try {
                email = getSession().getUserEMail();
            } catch (Exception e) {
                setMessage("Impossible de trouver l'adresse e-mail de l'utilisateur");
                setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        return email;
    }

    public String getFromIdExterneRole() {
        return fromIdExterneRole;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return affiliationId;
    }

    /**
     * getter pour l'attribut plan affiliation id
     * 
     * @return la valeur courante de l'attribut plan affiliation id
     */
    public String getPlanAffiliationId() {
        return planAffiliationId;
    }

    /**
     * Rechercher le tiers du nombre d'Assuré en fonction affiliation.
     * 
     * @return le tiers
     */
    public TITiers getTiers() {
        // Si pas d'identifiant => pas d'objet
        if (_tiers == null) {
            if (_affiliation == null) {
                getAffiliation();

                if (_affiliation == null) {
                    return null;
                }
            }

            _tiers = new TITiers();
            _tiers.setISession(getSession());
            _tiers.setIdTiers(_affiliation.getIdTiers());

            try {
                _tiers.retrieve();
                /*
                 * if (_tiers.getSession().hasErrors()) _tiers = null;
                 */
            } catch (Exception e) {
                setMessage(e.getMessage());
                setMsgType(FWViewBeanInterface.ERROR);
                _tiers = null;
            }
        }

        return _tiers;
    }

    public String getTillIdExterneRole() {
        return tillIdExterneRole;
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

    /**
     * setter pour l'attribut date envoi
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateEnvoi(String string) {
        dateEnvoi = string;
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

    public void setFromIdExterneRole(String fromIdExterneRole) {
        this.fromIdExterneRole = fromIdExterneRole;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        affiliationId = newId;
    }

    /**
     * setter pour l'attribut plan affiliation id
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setPlanAffiliationId(String string) {
        planAffiliationId = string;
    }

    public void setTillIdExterneRole(String tillIdExterneRole) {
        this.tillIdExterneRole = tillIdExterneRole;
    }

}
