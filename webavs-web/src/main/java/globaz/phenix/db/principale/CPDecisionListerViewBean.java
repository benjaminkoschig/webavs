package globaz.phenix.db.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.phenix.db.divers.CPPeriodeFiscale;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPDecisionListerViewBean extends BEntity implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // IAACTI décision active
    private Boolean active = new Boolean(true);
    private java.lang.String anneeDecision = "";
    private java.lang.String cotisationAnnuelle = "";
    private java.lang.String dateFacturation = "";
    private java.lang.String dateInformation = "";
    private java.lang.String debutAffiliation = "";
    private java.lang.String debutDecision = "";
    private java.lang.String dernierEtat = "";
    private java.lang.String finAffiliation = "";
    private java.lang.String finDecision = "";
    private java.lang.String genreAffilie = "";
    private java.lang.String idAffiliation = "";
    private java.lang.String idDecision = "";
    private java.lang.String idIfdDefinitif = "";
    private java.lang.String idPassage = "";
    private java.lang.String idTiers = "";
    private java.lang.String miseEnCompte = "";
    private String modifiable = "";
    private java.lang.String nomPrenom = "";
    // Champ pour option rechercher
    private java.lang.String numAffilie = "";
    private java.lang.String revenuFortuneDeterminant = "";
    private java.lang.String specification = "";
    private java.lang.String typeDecision = "";
    private String idDemandeIssuPortail = "";

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "CPDECIP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idDecision = statement.dbReadNumeric("IAIDEC");
        idTiers = statement.dbReadNumeric("HTITIE");
        dernierEtat = statement.dbReadNumeric("IATETA");
        typeDecision = statement.dbReadNumeric("IATTDE");
        genreAffilie = statement.dbReadNumeric("IATGAF");
        dateInformation = statement.dbReadDateAMJ("IADINF");
        anneeDecision = statement.dbReadNumeric("IAANNE");
        debutDecision = statement.dbReadDateAMJ("IADDEB");
        finDecision = statement.dbReadDateAMJ("IADFIN");
        specification = statement.dbReadNumeric("IATSPE");
        cotisationAnnuelle = statement.dbReadNumeric("ISMCAN");
        revenuFortuneDeterminant = statement.dbReadNumeric("IHMDCA", 2);
        miseEnCompte = statement.dbReadNumeric("IDCOT1", 2);
        debutAffiliation = statement.dbReadDateAMJ("MADDEB");
        finAffiliation = statement.dbReadDateAMJ("MADFIN");
        idIfdDefinitif = statement.dbReadNumeric("ICIIFD");
        active = statement.dbReadBoolean("IAACTI");
        modifiable = statement.dbReadNumeric("UBBMOD");
        dateFacturation = statement.dbReadDateAMJ("IADFAC");
        idAffiliation = statement.dbReadNumeric("MAIAFF");
        idPassage = statement.dbReadNumeric("EBIPAS");
        numAffilie = statement.dbReadString("MALNAF");
        nomPrenom = statement.dbReadString("HTLDE1") + " " + statement.dbReadString("HTLDE2");
        idDemandeIssuPortail = statement.dbReadString("EBIDDP");
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * @return
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Returns the anneeDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getAnneeDecision() {
        return anneeDecision;
    }

    /**
     * Returns le mois de radiation.
     * 
     * @return java.lang.String
     */
    public java.lang.String getAnneeMoisRadiation() {
        try {
            if (BSessionUtil.compareDateFirstGreater(getSession(), getDebutAffiliation(), getDebutDecision())) {
                return Integer.toString(JACalendar.getMonth(getDebutAffiliation())) + "/" + getAnneeDecision();
            } else {
                return getAnneeDecision() + "/" + Integer.toString(JACalendar.getMonth(getFinAffiliation()));
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Returns the cotisationAnnuelle.
     * 
     * @return java.lang.String
     */
    public java.lang.String getCotisationAnnuelle() throws Exception {
        if ((cotisationAnnuelle != null) && !JadeStringUtil.isEmpty(cotisationAnnuelle)) {
            return JANumberFormatter.fmt(cotisationAnnuelle, true, true, false, 2);
        } else {
            CPCotisation coti = CPCotisation._returnCotisation(getSession(), getIdDecision(), "");
            if ((coti != null) && !JadeStringUtil.isNull(coti.getMontantAnnuel())) {
                return coti.getMontantAnnuel();
            }
        }
        return "";
    }

    public java.lang.String getDateFacturation() {
        return dateFacturation;
    }

    /**
     * Returns the dateInformation.
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateInformation() {
        return dateInformation;
    }

    /**
     * Returns the debutAffiliation.
     * 
     * @return java.lang.String
     */
    public java.lang.String getDebutAffiliation() {
        return debutAffiliation;
    }

    /**
     * Returns the debutDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getDebutDecision() {
        return debutDecision;
    }

    /**
     * Returns the etat.
     * 
     * @return java.lang.String
     */
    public java.lang.String getDernierEtat() {
        return dernierEtat;
    }

    /**
     * Retourne le libellé du dernier état de la décision Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getDernierEtatLibelle() {
        try {
            return globaz.phenix.translation.CodeSystem.getLibelle(getSession(), getDernierEtat());
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return "";
        }
    }

    /*
     * Retourne la déscription de la décision sour la forme Année décision - Type de décision - Genre de décision Ex:
     * 200 - Provisoire - Indépendant
     */
    public java.lang.String getDescriptionDecision() {
        String libGenre = "";
        String libDecision = "";
        try {
            libGenre = globaz.phenix.translation.CodeSystem.getLibelle(getSession(), genreAffilie);
            libDecision = globaz.phenix.translation.CodeSystem.getLibelle(getSession(), typeDecision);
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        return anneeDecision + " - " + libDecision + " - " + libGenre;
    }

    /**
     * Returns the finAffiliation.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFinAffiliation() {
        return finAffiliation;
    }

    /**
     * Returns the finDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFinDecision() {
        return finDecision;
    }

    /**
     * Returns the genreAffilie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getGenreAffilie() {
        return genreAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getGenreAffilieLibelle() {
        try {
            return globaz.phenix.translation.CodeSystem.getCodeUtilisateur(getSession(), getGenreAffilie());
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return "";
        }
    }

    public java.lang.String getIdAffiliation() {
        return idAffiliation;
    }

    /**
     * Returns the idDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdDecision() {
        return idDecision;
    }

    /**
     * Returns the idIfdDefinitif.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdIfdDefinitif() {
        return idIfdDefinitif;
    }

    public java.lang.String getIdPassage() {
        return idPassage;
    }

    /**
     * Returns the idTiers.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdTiers() {
        return idTiers;
    }

    /**
     * Returns the miseEnCompte.
     * 
     * @return java.lang.String
     */
    public java.lang.String getMiseEnCompte() {
        if (CPDecision.CS_IMPUTATION.equals(getTypeDecision())) {
            return JANumberFormatter.fmt(miseEnCompte, true, false, true, 2);
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getModifiable() {
        return modifiable;
    }

    public java.lang.String getNomPrenom() {
        return nomPrenom;
    }

    public java.lang.String getNumAffilie() {
        return numAffilie;
    }

    /*
     * Retourne le N° Ifd de base pour la communication fiscale
     */
    public java.lang.String getNumIfd() {
        String numIfd = "";
        CPPeriodeFiscale periode = new CPPeriodeFiscale();
        periode.setSession(getSession());
        periode.setIdIfd(getIdIfdDefinitif());
        try {
            periode.retrieve();
            if (!periode.isNew()) {
                numIfd = periode.getNumIfd();
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        return numIfd;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getPeriodeDecision() {
        try {
            return JACalendar.getDay(getDebutDecision()) + "." + JACalendar.getMonth(getDebutDecision()) + "-"
                    + getFinDecision();
        } catch (Exception e) {
            return "";
        }
    }

    /*
     * Retourne la périodicité (annuelle, trimestrielle...)
     */
    public String getPeriodicite() {
        String periodicite = "";
        AFAffiliation affi = new AFAffiliation();
        affi.setSession(getSession());
        affi.setIdTiers(getIdTiers());
        try {
            affi = affi._getDerniereAffiliation();
            if ((affi != null) && !affi.isNew()) {
                periodicite = globaz.phenix.translation.CodeSystem.getLibelle(getSession(), affi.getPeriodicite());
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        return periodicite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getRevenuCi() {
        CPDonneesCalcul donnee = new CPDonneesCalcul();
        donnee.setSession(getSession());
        String montantDonnees = donnee.getMontant(getIdDecision(), CPDonneesCalcul.CS_REV_CI);
        if (JadeStringUtil.isEmpty(montantDonnees) || JadeStringUtil.isBlank(montantDonnees)) {
            return "0";
        } else {
            return montantDonnees;
        }
    }

    /**
     * Returns the revenuFortuneDeterminant.
     * 
     * @return java.lang.String
     */
    public java.lang.String getRevenuFortuneDeterminant() {
        return JANumberFormatter.fmt(revenuFortuneDeterminant, true, false, false, 0);
    }

    /**
     * Returns the specification.
     * 
     * @return java.lang.String
     */
    public java.lang.String getSpecification() {
        return specification;
    }

    /**
     * Affiche le libellé de la spécification (motif) Date de création : (23.03.2004 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getSpecificationLibelle() {
        try {
            return globaz.phenix.translation.CodeSystem.getLibelle(getSession(), getSpecification());
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return "";
        }
    }

    /**
     * Returns the typeDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getTypeDecision() {
        return typeDecision;
    }

    /**
     * Retourne le libellé du type de décision Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getTypeDecisionLibelle() {
        try {
            return globaz.phenix.translation.CodeSystem.getLibelle(getSession(), getTypeDecision());
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return "";
        }
    }

    /**
     * Test si la décision est comptabilisée ou migrée Date de création : (25.01.2006 16:18:35)
     * 
     * @return java.lang.boolean
     */
    public boolean isComptabilise() {
        try {
            if (CPDecision.CS_FACTURATION.equalsIgnoreCase(getDernierEtat())
                    || CPDecision.CS_REPRISE.equalsIgnoreCase(getDernierEtat())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return false;
        }
    }

    /**
     * Test si la période d'affiliation est inclu dans celle de la décision Se baser uniquement sur mois et année (à
     * cause des caisses qui affilient au jour près et non au 1 ou 31 du mois) Date de création : (07.02.2004 16:18:35)
     * 
     * @return java.lang.boolean
     */
    public boolean isInclusSortie() {
        try {
            if ((!JAUtil.isDateEmpty(getFinAffiliation()) && (BSessionUtil.compareDateBetween(getSession(),
                    JACalendar.format(getDebutDecision(), JACalendar.FORMAT_MMYYYY),
                    JACalendar.format(getFinDecision(), JACalendar.FORMAT_MMYYYY),
                    JACalendar.format(getFinAffiliation(), JACalendar.FORMAT_MMYYYY))))
                    || (BSessionUtil.compareDateBetween(getSession(),
                            JACalendar.format(getDebutDecision(), JACalendar.FORMAT_MMYYYY),
                            JACalendar.format(getFinDecision(), JACalendar.FORMAT_MMYYYY),
                            JACalendar.format(getDebutAffiliation(), JACalendar.FORMAT_MMYYYY)))) {
                return true;
            }
            return false;
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return false;
        }
    }

    /**
     * Indique si le cas est radié (si l'affiliation est en dehors de l'affiliation Date de création : (03.05.2002
     * 16:18:35)
     * 
     * @return java.lang.String
     */
    public boolean isRadie() {
        try {
            int anneeFin = JACalendar.getYear(getFinAffiliation());
            int anneeDec = Integer.parseInt(getAnneeDecision());
            // Si début décision > fin affiliation
            if ((anneeFin != 0)
                    && BSessionUtil.compareDateFirstGreater(getSession(), getDebutDecision(), getFinAffiliation())) {
                return true;
            }
            // Si fin décision < début affiliation
            if (BSessionUtil.compareDateFirstLower(getSession(), getFinDecision(), getDebutAffiliation())) {
                return true;
            }
            // Si erreur affiliation (à tort, cas de reprise)
            if ((anneeFin < anneeDec) && (anneeFin != 0)) {
                return true;
            }
            // Si affiliation à tort
            if (BSessionUtil.compareDateEqual(getSession(), getDebutAffiliation(), getFinAffiliation())) {
                return true;
            }
            // bPAs radié
            return false;
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return false;
        }
    }

    /**
     * Test si la décision est comptabilisée ou migrée Date de création : (25.01.2006 16:18:35)
     * 
     * @return java.lang.boolean
     */
    public boolean isValide() {
        try {
            if (CPDecision.CS_VALIDATION.equalsIgnoreCase(getDernierEtat())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return false;
        }
    }

    public void setIdPassage(java.lang.String idPassage) {
        this.idPassage = idPassage;
    }


    public String getIdDemandeIssuPortail() {
        return idDemandeIssuPortail;
    }

    public void setIdDemandeIssuPortail(String idDemandeIssuPortail) {
        this.idDemandeIssuPortail = idDemandeIssuPortail;
    }

    public Boolean isDecisionFromPortail(){
        if(!JadeStringUtil.isBlankOrZero(getIdDemandeIssuPortail())){
            return true;
        }
        return false;
    }
}
