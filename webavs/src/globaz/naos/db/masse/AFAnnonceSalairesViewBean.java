/*
 * Créé le 9 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.db.masse;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.translation.CodeSystem;
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
public class AFAnnonceSalairesViewBean extends AFAbstractViewBean {

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

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFAnnonceSalairesViewBean.
     */
    public AFAnnonceSalairesViewBean() {
        super();
    }

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

    public String getDateDebut() {
        try {
            if ((JadeStringUtil.isEmpty(dateDebut) && JadeStringUtil.isEmpty(dateFin))
                    && !JadeStringUtil.isEmpty(getAffiliationId())) {

                String today = JACalendar.todayJJsMMsAAAA();
                AFAffiliationManager affiliationManager = new AFAffiliationManager();
                affiliationManager.setSession(getSession());
                affiliationManager.setForAffiliationId(getAffiliationId());
                affiliationManager.setForTillDateDebut(today);
                affiliationManager.setOrder("MADDEB DESC");
                affiliationManager.find();

                if (affiliationManager.size() > 0) {
                    AFAffiliation affiliation = (AFAffiliation) affiliationManager.getFirstEntity();

                    if (JadeStringUtil.isIntegerEmpty(affiliation.getDateFin())
                            || BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), affiliation.getDateFin(),
                                    today)) {

                        if (affiliation.getPeriodicite().equals(CodeSystem.PERIODICITE_MENSUELLE)) {
                            setDateFin(AFUtil.getDateEndOfMonth(today));
                            setDateDebut(AFUtil.getDateBeginingOfMonth(dateFin));

                        } else if (affiliation.getPeriodicite().equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
                            setDateFin(AFUtil.getDateEndOfTrim(today));
                            setDateDebut(AFUtil.getDateBeginingOfMonth(AFUtil.getDateEndOfPreviousMonth(2, dateFin)));

                        } else {
                            setDateFin(AFUtil.getDateEndOfYear(today));
                            setDateDebut("01.01." + dateFin.substring(6));
                        }
                    } else {

                        if (affiliation.getPeriodicite().equals(CodeSystem.PERIODICITE_MENSUELLE)) {

                            setDateFin(AFUtil.getDateEndOfMonth(affiliation.getDateFin()));

                            if (BSessionUtil.compareDateFirstLower(getSession(), today, dateFin)) {
                                setDateFin(AFUtil.getDateEndOfNextMonth(dateFin));
                            }
                            setDateDebut(AFUtil.getDateBeginingOfMonth(dateFin));

                        } else if (affiliation.getPeriodicite().equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {

                            setDateFin(AFUtil.getDateEndOfTrim(affiliation.getDateFin()));

                            if (BSessionUtil.compareDateFirstLower(getSession(), today, dateFin)) {
                                setDateFin(AFUtil.getDateEndOfTrim(dateFin));
                            }
                            setDateDebut(AFUtil.getDateBeginingOfMonth(AFUtil.getDateEndOfPreviousMonth(2, dateFin)));

                        } else {
                            setDateFin(AFUtil.getDateEndOfYear(affiliation.getDateFin()));

                            if (BSessionUtil.compareDateFirstLower(getSession(), today, dateFin)) {
                                setDateFin(AFUtil.getDateEndOfYear(dateFin));
                            }
                            setDateDebut("01.01." + dateFin.substring(6));
                        }
                    }
                }
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

    public String getDateFin() {
        return dateFin;
    }

    public String getDateRetour() {
        if (JadeStringUtil.isEmpty(dateRetour)) {
            dateRetour = AFUtil.getDateRetour14(dateEnvoi);
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

    public String getFromIdExterneRole() {
        return fromIdExterneRole;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
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

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
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

    public void setTillIdExterneRole(String tillIdExterneRole) {
        this.tillIdExterneRole = tillIdExterneRole;
    }
}
