/*
 * Créé le 9 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.db.acompte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.util.AFUtil;
import globaz.pyxis.db.tiers.TITiers;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Un viewBean leger pour stocker les options de lancement du process de generation des suivi des annonces des salaires.
 * </p>
 * 
 * @author vre
 */
public class AFBouclementAcompteImprimerViewBean extends AFAbstractViewBean {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private AFAffiliation _affiliation = null;
    private TITiers _tiers = null;

    // champs transmis au process
    private String affiliationId;
    private String dateDebut = "";
    private String dateEnvoi = JACalendar.todayJJsMMsAAAA();
    private String dateFin = "";
    private String dateRetour = "";
    private String email;
    private String fromIdExterneRole = "";
    private String periode = "";
    private String planAffiliationId;
    private String tillIdExterneRole = "";
    private String year = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFAnnonceSalairesViewBean.
     */
    public AFBouclementAcompteImprimerViewBean() {
        super();
    }

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
     * @return
     */
    public String getDateDebut() {
        try {
            if ((JadeStringUtil.isEmpty(dateDebut) && JadeStringUtil.isEmpty(dateFin))) {

                String today = JACalendar.todayJJsMMsAAAA();
                setYear(today.substring(6));
                setDateFin("31.12." + today.substring(6));
                setDateDebut("01.01." + today.substring(6));
            }
        } catch (Exception e) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(e.getMessage());
        }
        return dateDebut;
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
     * @return
     */
    public String getDateFin() {
        try {
            if ((JadeStringUtil.isEmpty(dateDebut) && JadeStringUtil.isEmpty(dateFin))) {

                String today = JACalendar.todayJJsMMsAAAA();
                setYear(today.substring(6));
                setDateFin("31.12." + today.substring(6));
                setDateDebut("01.01." + today.substring(6));
            }
        } catch (Exception e) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(e.getMessage());
        }
        return dateFin;
    }

    public String getDateRetour() {
        if (JadeStringUtil.isEmpty(dateRetour)) {
            if ((getDateFin() != null) && (getDateFin().indexOf("31.12.") != -1)) {
                // date de fin au 31.12, recherche de l'année
                int annee = Integer.parseInt(getDateFin().substring(6));
                dateRetour = "30.01." + (annee + 1);
            } else {
                // date différente, on prends 14 jours de plus de la date
                // d'envoi
                dateRetour = AFUtil.getDateRetour14(dateEnvoi);
            }
        }
        return dateRetour;
    }

    /**
     * getter pour l'attribut email
     * 
     * @return la valeur courante de l'attribut email
     */
    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            try {
                email = getISession().getUserEMail();
            } catch (Exception e) {
                setMessage("Impossible de trouver l'adresse e-mail de l'utilisateur");
                setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        return email;
    }

    /**
     * @return
     */
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

    public String getPeriode() {
        return periode;
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

    /**
     * @return
     */
    public String getTillIdExterneRole() {
        return tillIdExterneRole;
    }

    public String getYear() {
        return year;
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
     * @param string
     */
    public void setDateDebut(String string) {
        dateDebut = string;
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
     * @param string
     */
    public void setDateFin(String string) {
        dateFin = string;
    }

    public void setDateRetour(String dateRetour) {
        this.dateRetour = dateRetour;
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
     * @param string
     */
    public void setFromIdExterneRole(String string) {
        fromIdExterneRole = string;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        affiliationId = newId;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
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

    /**
     * @param string
     */
    public void setTillIdExterneRole(String string) {
        tillIdExterneRole = string;
    }

    public void setYear(String year) {
        this.year = year;
        // mise à jour des dates de début et de fin
        if ((year != null) && (year.length() == 4)) {
            setDateDebut("01.01." + year);
            setDateFin("31.12." + year);
        }
    }

}
