package globaz.naos.db.decisionCotisations;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.util.AFUtil;
import globaz.pyxis.db.tiers.TITiers;

public class AFDecisionCotisationsViewBean extends AFAbstractViewBean {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private AFAffiliation _affiliation = null;
    private TITiers _tiers = null;

    // champs transmis au process
    private String affiliationId;
    private String dateDebut = "";
    private String dateEnvoi = "";
    private String dateFin = "";
    private String dateImprime = "";
    private String dateRetour = "";
    private String email;
    private String fromIdExterneRole = "";
    private String periode = "";
    private String planAffiliationId;
    private String tillIdExterneRole = "";

    private String year;

    /**
     * Cr?e une nouvelle instance de la classe AFAnnonceSalairesViewBean.
     */
    public AFDecisionCotisationsViewBean() {
        super();
    }

    /**
     * Rechercher l'affiliation du nombre d'Assur? en fonction de son ID.
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

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut affiliation id
     * 
     * @return la valeur courante de l'attribut affiliation id
     */
    public String getAffiliationId() {
        return affiliationId;
    }

    public String getDateDebut() {
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
        try {
            if ((JadeStringUtil.isEmpty(dateDebut) && JadeStringUtil.isEmpty(dateFin))) {

                // setYear("" + new JADate(new
                // JACalendarGregorian().addYears(JACalendar.todayJJsMMsAAAA(),
                // 1)).getYear());
                setDateFin("31.12." + getYear());
                setDateDebut("01.01." + getYear());

            }
        } catch (Exception e) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(e.getMessage());
        }
        return dateFin;
    }

    public String getDateImprime() {
        return dateImprime;
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

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut plan affiliation id
     * 
     * @return la valeur courante de l'attribut plan affiliation id
     */
    public String getPlanAffiliationId() {
        return planAffiliationId;
    }

    /**
     * Rechercher le tiers du nombre d'Assur? en fonction affiliation.
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

        if (!JadeStringUtil.isEmpty(string)) {
            dateEnvoi = "01.01." + string;
        }
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDateImprime(String newDateImprime) {
        dateImprime = newDateImprime;
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

    public void setYear(String year) {
        this.year = year;
        // mise ? jour des dates de d?but et de fin
        if ((year != null) && (year.length() == 4)) {
            setDateDebut("01.01." + year);
            setDateFin("31.12." + year);
        }
    }

}
