/*
 * Créé le 09 septembre 05
 */
package globaz.ij.vb.basesindemnisation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.ij.calendar.IJCalendar;
import globaz.ij.calendar.IJMois;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.regles.IJBaseIndemnisationRegles;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.Calendar;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author scr
 */
public class IJBaseIndemnisationViewBean extends IJBaseIndemnisation implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Vector MONTHS = new Vector();

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final Vector YEARS = new Vector();
    static {
        // menu deroulant mois
        for (int idMois = 1; idMois <= 12; ++idMois) {
            MONTHS.add(new String[] { pad0(idMois), pad0(idMois) });
        }

        // menu deroulant annees
        Calendar calendar = Calendar.getInstance();

        for (int i = calendar.get(Calendar.YEAR) - 6; i < (calendar.get(Calendar.YEAR) + 3); i++) {
            YEARS.add(new String[] { String.valueOf(i), String.valueOf(i) });
        }
    }

    private static String pad0(int val) {
        if (val < 10) {
            return "0" + val;
        } else {
            return String.valueOf(val);
        }
    }

    private Calendar dateDebut;
    private String dateDebutPeriodeEtendue = null;

    private String dateFinPeriodeEtendue = null;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private boolean dernierJourDuMois;

    private transient IJCalendar ijCalendar;
    private Boolean isPeriodeEtendue = null;
    private int jourFin;
    private String nbPostit = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private IJPrononce prononce = null;

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        nbPostit = statement.dbReadNumeric(IJBaseIndemnisationListViewBean.FIELDNAME_COUNT_POSTIT);
        super._readProperties(statement);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        if (getIsPeriodeEtendue().booleanValue()) {

            setDateDebutPeriode(getDateDebutPeriodeEtendue());
            setDateFinPeriode(getDateFinPeriodeEtendue());

            super._validate(statement);

            // Date fin > Date début
            if (BSessionUtil.compareDateFirstGreater(getSession(), getDateDebutPeriode(), getDateFinPeriode())) {
                _addError(statement.getTransaction(), "Date de fin doit être postérieure à la date de début");
            }

            JACalendar cal = new JACalendarGregorian();

            long nbJoursBI = cal.daysBetween(getDateDebutPeriode(), getDateFinPeriode());
            nbJoursBI++;
            int nbJoursCouvert = 0;
            if (!JadeStringUtil.isEmpty(getNombreJoursCouverts())) {
                nbJoursCouvert = Integer.parseInt(getNombreJoursCouverts());

            }

            int nbJoursTotal = nbJoursCouvert;
            if (nbJoursTotal > nbJoursBI) {
                _addError(statement.getTransaction(),
                        getSession().getLabel("JSP_JOURS_ATTESTE_ET_INT_PLUS_GRAND_NB_JOURS_BI"));
            }

        } else {

            super._validate(statement);

            if (!JadeStringUtil.isIntegerEmpty(getNombreJoursExterne())
                    || !JadeStringUtil.isIntegerEmpty(getNombreJoursInterne())) {
                // nb jours couvert + nb de jours d'interruption doit être <=
                // nombre de jours de la BI
                int jourdebut = Integer.parseInt(getJourDebut());
                int jourFin = Integer.parseInt(getJourFin());
                int nbJoursBI = (jourFin - jourdebut) + 1;

                int nbJoursCouvert = 0;
                if (!JadeStringUtil.isEmpty(getNombreJoursCouverts())) {
                    nbJoursCouvert = Integer.parseInt(getNombreJoursCouverts());
                }
                // int nbJoursInterruption = 0;
                // if(!JadeStringUtil.isEmpty(getNombreJoursInterruption())){
                // nbJoursInterruption =
                // Integer.parseInt(getNombreJoursInterruption());
                // }
                // int nbJoursTotal = nbJoursCouvert + nbJoursInterruption;

                int nbJoursTotal = nbJoursCouvert;
                if (nbJoursTotal > nbJoursBI) {
                    _addError(statement.getTransaction(),
                            getSession().getLabel("JSP_JOURS_ATTESTE_ET_INT_PLUS_GRAND_NB_JOURS_BI"));
                }
            }
        }
    }

    public String getCsTypeHebergement() {

        retrievePronoce();

        if (prononce != null) {
            return prononce.getCsTypeHebergement();
        } else {
            return "";
        }
    }

    /**
     * Donne le libelle du type d'hebergement definit dans le pronoce
     * 
     * @return
     */
    public String getCsTypeHebergementLibelle() {

        retrievePronoce();

        if (prononce != null) {
            return getSession().getCodeLibelle(prononce.getCsTypeHebergement());
        } else {
            return "";
        }
    }

    @Override
    public String getCsTypeIJ() {

        retrievePronoce();

        if (prononce != null) {
            return prononce.getCsTypeIJ();
        } else {
            return "";
        }
    }

    /**
     * Donne le libelle du type d'IJ definit dans le pronoce
     * 
     * @return
     */
    public String getCsTypeIJLibelle() {

        retrievePronoce();

        if (prononce != null) {
            return getSession().getCodeLibelle(prononce.getCsTypeIJ());
        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut date debut.
     * 
     * @return la valeur courante de l'attribut date debut
     */
    public Calendar getDateDebut() {
        return dateDebut;
    }

    public String getDateDebutPeriodeEtendue() {
        if (JadeStringUtil.isBlankOrZero(dateDebutPeriodeEtendue)) {
            return getDateDebutPeriode();
        } else {
            return dateDebutPeriodeEtendue;
        }
    }

    public String getDateFinPeriodeEtendue() {
        if (JadeStringUtil.isBlankOrZero(dateFinPeriodeEtendue)) {
            return getDateFinPeriode();
        } else {
            return dateFinPeriodeEtendue;
        }
    }

    /**
     * getter pour l'attribut days.
     * 
     * @return la valeur courante de l'attribut days
     */
    public Vector getDaysDebut() {
        Vector days = new Vector();

        for (int idDay = 1; idDay <= jourFin; ++idDay) {
            days.add(new String[] { pad0(idDay), pad0(idDay) });
        }

        return days;
    }

    /**
     * getter pour l'attribut days.
     * 
     * @return la valeur courante de l'attribut days
     */
    public Vector getDaysFin() {
        Vector days = new Vector();

        for (int idDay = dateDebut.get(Calendar.DAY_OF_MONTH); idDay <= dateDebut
                .getActualMaximum(Calendar.DAY_OF_MONTH); ++idDay) {
            days.add(new String[] { pad0(idDay), pad0(idDay) });
        }

        return days;
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     * 
     * @return le détail du requérant formaté
     * @throws Exception
     */
    public String getDetailRequerant() {

        String nationalite = "";
        String result = "";

        try {

            if (!"999".equals(getSession().getCode(
                    getSession().getSystemCode(
                            "CIPAYORI",
                            loadPrononce(null).loadDemande(null).loadTiers()
                                    .getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = getSession().getCodeLibelle(
                        getSession().getSystemCode(
                                "CIPAYORI",
                                loadPrononce(null).loadDemande(null).loadTiers()
                                        .getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            }

            result = loadPrononce(null).loadDemande(null).loadTiers()
                    .getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)
                    + " / "
                    + loadPrononce(null).loadDemande(null).loadTiers().getProperty(PRTiersWrapper.PROPERTY_NOM)
                    + " "
                    + loadPrononce(null).loadDemande(null).loadTiers().getProperty(PRTiersWrapper.PROPERTY_PRENOM)
                    + " / "
                    + loadPrononce(null).loadDemande(null).loadTiers()
                            .getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE)
                    + " / "
                    + getSession().getCodeLibelle(
                            loadPrononce(null).loadDemande(null).loadTiers().getProperty(PRTiersWrapper.PROPERTY_SEXE))
                    + " / " + nationalite;

        } catch (Exception e) {
        }

        return result;
    }

    /**
     * getter pour l'attribut ij dateDebut.
     * 
     * @return la valeur courante de l'attribut ij dateDebut
     */
    public IJCalendar getIjCalendar() {
        return ijCalendar;
    }

    public Boolean getIsPeriodeEtendue() {
        if (isPeriodeEtendue == null) {

            JADate dd;
            JADate df;
            try {
                dd = new JADate(getDateDebutPeriode());
                df = new JADate(getDateFinPeriode());
            } catch (JAException e) {
                return null;
            }

            if (df.getYear() != dd.getYear()) {
                isPeriodeEtendue = Boolean.TRUE;
            } else if (df.getMonth() != dd.getMonth()) {
                isPeriodeEtendue = Boolean.TRUE;
            } else {
                isPeriodeEtendue = Boolean.FALSE;
            }
        }

        return isPeriodeEtendue;

    }

    /**
     * 
     * @return true si le prononce lie a la base d'indemnisation est soumis a l'imposition directe
     */
    public boolean getIsSoumisImpositionSource() {

        retrievePronoce();

        if (prononce != null) {
            return prononce.getSoumisImpotSource().booleanValue();
        } else {
            return false;
        }
    }

    /**
     * getter pour l'attribut jour debut.
     * 
     * @return la valeur courante de l'attribut jour debut
     */
    public String getJourDebut() {
        return pad0(getJourDebutChecked());
    }

    /**
     * getter pour l'attribut jour debut checked.
     * 
     * @return la valeur courante de l'attribut jour debut checked
     */
    public int getJourDebutChecked() {
        if (dateDebut.get(Calendar.DAY_OF_MONTH) > dateDebut.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            dateDebut.set(Calendar.DAY_OF_MONTH, dateDebut.getActualMaximum(Calendar.DAY_OF_MONTH));
        }

        if (dateDebut.get(Calendar.DAY_OF_MONTH) < 1) {
            dateDebut.set(Calendar.DAY_OF_MONTH, 1);
        }

        return dateDebut.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * getter pour l'attribut jour fin.
     * 
     * @return la valeur courante de l'attribut jour fin
     */
    public String getJourFin() {
        return pad0(getJourFinChecked());
    }

    /**
     * getter pour l'attribut jour fin checked.
     * 
     * @return la valeur courante de l'attribut jour fin checked
     */
    public int getJourFinChecked() {
        if (jourFin > dateDebut.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            // le jour de fin ne peut pas etre plus grand que le nombre de jours
            // dans le mois
            jourFin = dateDebut.getActualMaximum(Calendar.DAY_OF_MONTH);
        } else if (jourFin < 1) {
            // le jour de fin doit etre plus grand que 0
            jourFin = 1;
        } else if (dernierJourDuMois) {
            jourFin = dateDebut.getActualMaximum(Calendar.DAY_OF_MONTH);
            dernierJourDuMois = false;
        }

        return jourFin;
    }

    /**
     * getter pour l'attribut mois.
     * 
     * @return la valeur courante de l'attribut mois
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJMois getMois() throws Exception {
        return ijCalendar.getMois();
    }

    /**
     * @return
     */
    public String getMonth() {
        return pad0(dateDebut.get(Calendar.MONTH) + 1);
    }

    /**
     * getter pour l'attribut months.
     * 
     * @return la valeur courante de l'attribut months
     */
    public Vector getMonths() {
        return MONTHS;
    }

    public String getNbPostit() {
        return nbPostit;
    }

    /**
     * getter pour l'attribut type libelle.
     * 
     * @return la valeur courante de l'attribut type libelle
     */
    public String getTypeLibelle() {
        return getSession().getCodeLibelle(getCsTypeBase());
    }

    /**
     * @return
     */
    public String getYear() {
        return String.valueOf(dateDebut.get(Calendar.YEAR));
    }

    /**
     * @return
     */
    public Vector getYears() {
        return YEARS;
    }

    public boolean hasPostit() {
        return Integer.parseInt(nbPostit) > 0;
    }

    public boolean isDernierJourDuMois() {
        return dernierJourDuMois;
    }

    /**
     * getter pour l'attribut modifier permis.
     * 
     * @return la valeur courante de l'attribut modifier permis
     */
    public boolean isModifierPermis() {
        return IJBaseIndemnisationRegles.isModifierPermis(this);
    }

    /**
     * getter pour l'attribut supprimer permis.
     * 
     * @return la valeur courante de l'attribut supprimer permis
     */
    public boolean isSupprimerPermis() {
        return IJBaseIndemnisationRegles.isSupprimerPermis(this);
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void resetCalendar() throws Exception {
        ijCalendar = new IJCalendar(getSession(), dateDebut.get(Calendar.MONTH) + 1, dateDebut.get(Calendar.YEAR),
                getJourDebutChecked(), getJourFinChecked());
        setDateDebutPeriode(ijCalendar.getMois().getDateDebut().toStr("."));
        setDateFinPeriode(ijCalendar.getMois().getDateFin().toStr("."));
    }

    /**
     * retrouve le prononce lie a la base d'indemnisation
     */
    private void retrievePronoce() {

        if (prononce == null) {
            if (!JadeStringUtil.isIntegerEmpty(getIdPrononce())) {
                prononce = new IJPrononce();
                prononce.setSession(getSession());
                prononce.setIdPrononce(getIdPrononce());

                try {
                    prononce.retrieve();
                } catch (Exception e) {
                    prononce = null;
                }
            }
        }
    }

    /**
     * setter pour l'attribut date debut.
     * 
     * @param dateDebut
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebut(Calendar dateDebut) {
        this.dateDebut = dateDebut;
        jourFin = dateDebut.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public void setDateDebutPeriodeEtendue(String dateDebutPeriodeEtendue) {
        this.dateDebutPeriodeEtendue = dateDebutPeriodeEtendue;
    }

    public void setDateFinPeriodeEtendue(String dateFinPeriodeEtendue) {
        this.dateFinPeriodeEtendue = dateFinPeriodeEtendue;
    }

    public void setDernierJourDuMois(boolean dernierJourDuMois) {
        this.dernierJourDuMois = dernierJourDuMois;
    }

    /**
     * setter pour l'attribut ij dateDebut.
     * 
     * @param calendar
     *            une nouvelle valeur pour cet attribut
     */
    public void setIjCalendar(IJCalendar calendar) {
        ijCalendar = calendar;
        if (!getIsPeriodeEtendue().booleanValue()) {
            setDateDebutPeriode(ijCalendar.getMois().getDateDebut().toStr("."));
            setDateFinPeriode(ijCalendar.getMois().getDateFin().toStr("."));
        }
    }

    public void setIsPeriodeEtendue(Boolean isPeriodeEtendue) {
        this.isPeriodeEtendue = isPeriodeEtendue;
    }

    /**
     * setter pour l'attribut jour debut.
     * 
     * @param jourDebut
     *            une nouvelle valeur pour cet attribut
     */
    public void setJourDebut(String jourDebut) {
        dateDebut.set(Calendar.DAY_OF_MONTH, Integer.parseInt(jourDebut));
    }

    /**
     * setter pour l'attribut jour fin.
     * 
     * @param jourFin
     *            une nouvelle valeur pour cet attribut
     */
    public void setJourFin(String jourFin) {
        this.jourFin = Integer.parseInt(jourFin);
    }

    /**
     * @param string
     */
    public void setMonth(String string) {
        if (jourFin == dateDebut.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            dernierJourDuMois = true;
        } else {
            dernierJourDuMois = false;
        }

        dateDebut.set(Calendar.MONTH, Integer.parseInt(string) - 1);
    }

    /**
     * @param string
     */
    public void setYear(String string) {
        dateDebut.set(Calendar.YEAR, Integer.parseInt(string));
    }

}
