package globaz.ij.db.prononces;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.clone.factory.IPRCloneable;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import java.text.MessageFormat;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJSituationProfessionnelle extends BEntity implements IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String FIELDNAME_ALLOCATION_MAX = "XHBALM";

    /**
     */
    public static final String FIELDNAME_ANNEE_CORRESPONDANTE = "XHDACO";

    /**
     */
    public static final String FIELDNAME_AUTRE_REMUNERATION = "XHMARE";

    /**
     */
    public static final String FIELDNAME_CS_PERIODICITE_AUTRE_REMUNERATION = "XHTPAR";

    /**
     */
    public static final String FIELDNAME_CS_PERIODICITE_SALAIRE = "XHTPSA";

    /**
     */
    public static final String FIELDNAME_CS_PERIODICITE_SALAIRE_NATURE = "XHTPSN";

    /**
     */
    public static final String FIELDNAME_ID_EMPLOYEUR = "XHIEMP";

    /**
     */
    public static final String FIELDNAME_ID_GRANDE_IJ = "XHIGIJ";

    /**
     */
    public static final String FIELDNAME_ID_SITUATION_PROFESSIONNELLE = "XHISIP";

    /**
     */
    public static final String FIELDNAME_IS_INDEPENDANT = "XHBIND";

    /**
     */
    public static final String FIELDNAME_NB_HEURES_SEMAINE = "XHMHSE";

    /**
     */
    public static final String FIELDNAME_POURCENT_AUTRE_REMUNERATION = "XHBPAR";

    /**
     */
    public static final String FIELDNAME_SALAIRE = "XHMSAL";

    /**
     */
    public static final String FIELDNAME_SALAIRE_NATURE = "XHMSNA";

    /**
     */
    public static final String FIELDNAME_SOUMIS_COTISATION_AC = "XHBCAC";

    /**
     */
    public static final String FIELDNAME_SOUMIS_COTISATION_AVS_AI = "XHBCAV";

    /**
     */
    public static final String FIELDNAME_VERSEMENT_EMPLOYEUR = "XHBVEM";

    /**
     */
    public static final String TABLE_NAME = "IJSITPRF";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Boolean allocationMax = Boolean.FALSE;
    private String anneeCorrespondante = "";
    private String autreRemuneration = "";
    private String csPeriodiciteAutreRemuneration = "";
    private String csPeriodiciteSalaire = "";
    private String csPeriodiciteSalaireNature = "";
    private transient IJEmployeur employeur;
    private String idEmployeur = "";
    private String idPrononce = "";
    private String idSituationProfessionnelle = "";
    private Boolean isIndependant = Boolean.FALSE;
    private String nbHeuresSemaine = "";
    private Boolean pourcentAutreRemuneration = Boolean.FALSE;
    private String salaire = "";
    private String salaireNature = "";
    private Boolean soumisCotisationAC = Boolean.FALSE;
    private Boolean soumisCotisationAVSAI = Boolean.FALSE;

    private Boolean versementEmployeur = Boolean.TRUE;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        super._afterDelete(transaction);

        if (loadEmployeur() != null) {
            loadEmployeur().delete(transaction);
        }
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // sauver l'employeur.
        IJEmployeur employeur = loadEmployeur(); // pour eviter une erreur lors
        // de l'assignation de l'id
        // employeur

        String noAffilieParDefaut = transaction.getSession().getApplication().getProperty("noAffilieParDefaut");

        // si on propose un employeur par defaut et que l'on n'a pas choisi
        // d'employeur,
        // on utilise l'employeur par defaut
        if (!JadeStringUtil.isEmpty(noAffilieParDefaut) && JadeStringUtil.isEmpty(employeur.getIdAffilie())
                && JadeStringUtil.isEmpty(employeur.getIdTiers())) {

            IPRAffilie affilie = PRAffiliationHelper.getEmployeurParNumAffilie(getSession(), noAffilieParDefaut);

            if (affilie != null) {
                employeur.setIdAffilie(affilie.getIdAffilie());
                employeur.setIdTiers(affilie.getIdTiers());

                employeur = loadEmployeur();
            } else {
                _addError(transaction, MessageFormat.format(getSession().getLabel("EMPLOYEUR_PAR_DEFAUT_INCONNU"),
                        new Object[] { noAffilieParDefaut }));
            }
        }

        employeur.add(transaction);
        idEmployeur = employeur.getIdEmployeur();

        // increment compteur
        idSituationProfessionnelle = _incCounter(transaction, "0");
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        // mettre a jour l'employeur
        loadEmployeur().update(transaction);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idSituationProfessionnelle = statement.dbReadNumeric(FIELDNAME_ID_SITUATION_PROFESSIONNELLE);
        nbHeuresSemaine = statement.dbReadNumeric(FIELDNAME_NB_HEURES_SEMAINE);
        csPeriodiciteAutreRemuneration = statement.dbReadNumeric(FIELDNAME_CS_PERIODICITE_AUTRE_REMUNERATION);
        csPeriodiciteSalaire = statement.dbReadNumeric(FIELDNAME_CS_PERIODICITE_SALAIRE);
        salaire = statement.dbReadNumeric(FIELDNAME_SALAIRE);
        autreRemuneration = statement.dbReadNumeric(FIELDNAME_AUTRE_REMUNERATION);
        idPrononce = statement.dbReadNumeric(FIELDNAME_ID_GRANDE_IJ);
        allocationMax = statement.dbReadBoolean(FIELDNAME_ALLOCATION_MAX);
        salaireNature = statement.dbReadNumeric(FIELDNAME_SALAIRE_NATURE);
        csPeriodiciteSalaireNature = statement.dbReadNumeric(FIELDNAME_CS_PERIODICITE_SALAIRE_NATURE);
        versementEmployeur = statement.dbReadBoolean(FIELDNAME_VERSEMENT_EMPLOYEUR);
        anneeCorrespondante = statement.dbReadNumeric(FIELDNAME_ANNEE_CORRESPONDANTE);
        soumisCotisationAVSAI = statement.dbReadBoolean(FIELDNAME_SOUMIS_COTISATION_AVS_AI);
        idEmployeur = statement.dbReadNumeric(FIELDNAME_ID_EMPLOYEUR);
        soumisCotisationAC = statement.dbReadBoolean(FIELDNAME_SOUMIS_COTISATION_AC);
        pourcentAutreRemuneration = statement.dbReadBoolean(FIELDNAME_POURCENT_AUTRE_REMUNERATION);
        isIndependant = statement.dbReadBoolean(FIELDNAME_IS_INDEPENDANT);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // l'année correspondante est obligatoire pour le calcul avec ACOR
        _propertyMandatory(statement.getTransaction(), anneeCorrespondante, getSession().getLabel("ANNEE_REQUISE"));

        // on ne peut saisir plus de trois situations professionnelles dans ACOR
        if (_getAction() == ACTION_ADD) {
            IJSituationProfessionnelleManager situations = new IJSituationProfessionnelleManager();

            situations.setForIdPrononce(idPrononce);
            situations.setSession(getSession());

            if (situations.getCount() > 2) {
                _addError(statement.getTransaction(), getSession().getLabel("NOMBRE_SITPRO_TROP_GRAND"));
            }
        }
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_SITUATION_PROFESSIONNELLE,
                _dbWriteNumeric(statement.getTransaction(), idSituationProfessionnelle));
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID_SITUATION_PROFESSIONNELLE,
                _dbWriteNumeric(statement.getTransaction(), idSituationProfessionnelle, "idSituationProfessionnelle"));
        statement.writeField(FIELDNAME_NB_HEURES_SEMAINE,
                _dbWriteNumeric(statement.getTransaction(), nbHeuresSemaine, "nbHeuresSemaine"));
        statement.writeField(
                FIELDNAME_CS_PERIODICITE_AUTRE_REMUNERATION,
                _dbWriteNumeric(statement.getTransaction(), csPeriodiciteAutreRemuneration,
                        "csPeriodiciteAutreRemuneration"));
        statement.writeField(FIELDNAME_CS_PERIODICITE_SALAIRE,
                _dbWriteNumeric(statement.getTransaction(), csPeriodiciteSalaire, "csPeriodiciteSalaire"));
        statement.writeField(FIELDNAME_SALAIRE, _dbWriteNumeric(statement.getTransaction(), salaire, "salaire"));
        statement.writeField(FIELDNAME_AUTRE_REMUNERATION,
                _dbWriteNumeric(statement.getTransaction(), autreRemuneration, "autreRemuneration"));
        statement.writeField(FIELDNAME_ID_GRANDE_IJ,
                _dbWriteNumeric(statement.getTransaction(), idPrononce, "idPrononce"));
        statement.writeField(
                FIELDNAME_ALLOCATION_MAX,
                _dbWriteBoolean(statement.getTransaction(), allocationMax, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "allocationMax"));
        statement.writeField(FIELDNAME_SALAIRE_NATURE,
                _dbWriteNumeric(statement.getTransaction(), salaireNature, "salaireNature"));
        statement.writeField(FIELDNAME_CS_PERIODICITE_SALAIRE_NATURE,
                _dbWriteNumeric(statement.getTransaction(), csPeriodiciteSalaireNature, "csPeriodiciteSalaireNature"));
        statement.writeField(
                FIELDNAME_VERSEMENT_EMPLOYEUR,
                _dbWriteBoolean(statement.getTransaction(), versementEmployeur, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "versementEmployeur"));
        statement.writeField(FIELDNAME_ANNEE_CORRESPONDANTE,
                _dbWriteNumeric(statement.getTransaction(), anneeCorrespondante, "anneeCorrespondante"));
        statement.writeField(
                FIELDNAME_SOUMIS_COTISATION_AVS_AI,
                _dbWriteBoolean(statement.getTransaction(), soumisCotisationAVSAI, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "soumisCotisationAVSAI"));
        statement.writeField(FIELDNAME_ID_EMPLOYEUR,
                _dbWriteNumeric(statement.getTransaction(), idEmployeur, "idEmployeur"));
        statement.writeField(
                FIELDNAME_SOUMIS_COTISATION_AC,
                _dbWriteBoolean(statement.getTransaction(), soumisCotisationAC, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "soumisCotisationAC"));
        statement.writeField(
                FIELDNAME_POURCENT_AUTRE_REMUNERATION,
                _dbWriteBoolean(statement.getTransaction(), pourcentAutreRemuneration, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "pourcentAutreRemuneration"));
        statement.writeField(
                FIELDNAME_IS_INDEPENDANT,
                _dbWriteBoolean(statement.getTransaction(), isIndependant, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "isIndependant"));
    }

    /**
     * DOCUMENT ME!
     * 
     * @param action
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public IPRCloneable duplicate(int action) throws Exception {
        IJSituationProfessionnelle clone = new IJSituationProfessionnelle();

        clone.setAllocationMax(getAllocationMax());
        clone.setAnneeCorrespondante(getAnneeCorrespondante());
        clone.setAutreRemuneration(getAutreRemuneration());
        clone.setCsPeriodiciteAutreRemuneration(getCsPeriodiciteAutreRemuneration());
        clone.setCsPeriodiciteSalaire(getCsPeriodiciteSalaire());
        clone.setCsPeriodiciteSalaireNature(getCsPeriodiciteSalaireNature());
        clone.setIdEmployeur(getIdEmployeur());
        clone.setIdPrononce(getIdPrononce());
        clone.setNbHeuresSemaine(getNbHeuresSemaine());
        clone.setPourcentAutreRemuneration(getPourcentAutreRemuneration());
        clone.setSalaire(getSalaire());
        clone.setSalaireNature(getSalaireNature());
        clone.setSoumisCotisationAC(getSoumisCotisationAC());
        clone.setSoumisCotisationAVSAI(getSoumisCotisationAVSAI());
        clone.setVersementEmployeur(getVersementEmployeur());
        clone.isIndependant = isIndependant;

        return clone;
    }

    /**
     * getter pour l'attribut allocation max
     * 
     * @return la valeur courante de l'attribut allocation max
     */
    public Boolean getAllocationMax() {
        return allocationMax;
    }

    /**
     * getter pour l'attribut annee correspondante
     * 
     * @return la valeur courante de l'attribut annee correspondante
     */
    public String getAnneeCorrespondante() {
        return anneeCorrespondante;
    }

    /**
     * getter pour l'attribut autre remuneration
     * 
     * @return la valeur courante de l'attribut autre remuneration
     */
    public String getAutreRemuneration() {
        return autreRemuneration;
    }

    /**
     * getter pour l'attribut cs periodicite autre remuneration
     * 
     * @return la valeur courante de l'attribut cs periodicite autre remuneration
     */
    public String getCsPeriodiciteAutreRemuneration() {
        return csPeriodiciteAutreRemuneration;
    }

    /**
     * getter pour l'attribut cs periodicite salaire
     * 
     * @return la valeur courante de l'attribut cs periodicite salaire
     */
    public String getCsPeriodiciteSalaire() {
        return csPeriodiciteSalaire;
    }

    /**
     * getter pour l'attribut cs periodicite salaire nature
     * 
     * @return la valeur courante de l'attribut cs periodicite salaire nature
     */
    public String getCsPeriodiciteSalaireNature() {
        return csPeriodiciteSalaireNature;
    }

    /**
     * getter pour l'attribut id employeur
     * 
     * @return la valeur courante de l'attribut id employeur
     */
    public String getIdEmployeur() {
        return idEmployeur;
    }

    /**
     * getter pour l'attribut id grande IJ
     * 
     * @return la valeur courante de l'attribut id grande IJ
     */
    public String getIdPrononce() {
        return idPrononce;
    }

    /**
     * getter pour l'attribut id situation professionnelle
     * 
     * @return la valeur courante de l'attribut id situation professionnelle
     */
    public String getIdSituationProfessionnelle() {
        return idSituationProfessionnelle;
    }

    /**
     * getter pour l'attribut is independant
     * 
     * @return la valeur courante de l'attribut is independant
     */
    public Boolean getIsIndependant() {
        return isIndependant;
    }

    /**
     * getter pour l'attribut nb heures semaine
     * 
     * @return la valeur courante de l'attribut nb heures semaine
     */
    public String getNbHeuresSemaine() {
        return nbHeuresSemaine;
    }

    /**
     * getter pour l'attribut pourcent autre remuneration
     * 
     * @return la valeur courante de l'attribut pourcent autre remuneration
     */
    public Boolean getPourcentAutreRemuneration() {
        return pourcentAutreRemuneration;
    }

    /**
     * getter pour l'attribut salaire
     * 
     * @return la valeur courante de l'attribut salaire
     */
    public String getSalaire() {
        return salaire;
    }

    /**
     * getter pour l'attribut salaire nature
     * 
     * @return la valeur courante de l'attribut salaire nature
     */
    public String getSalaireNature() {
        return salaireNature;
    }

    /**
     * getter pour l'attribut soumis cotisation AC
     * 
     * @return la valeur courante de l'attribut soumis cotisation AC
     */
    public Boolean getSoumisCotisationAC() {
        return soumisCotisationAC;
    }

    /**
     * getter pour l'attribut soumis cotisation AVSAI
     * 
     * @return la valeur courante de l'attribut soumis cotisation AVSAI
     */
    public Boolean getSoumisCotisationAVSAI() {
        return soumisCotisationAVSAI;
    }

    /**
     * getter pour l'attribut unique primary key
     * 
     * @return la valeur courante de l'attribut unique primary key
     */
    @Override
    public String getUniquePrimaryKey() {
        return getIdSituationProfessionnelle();
    }

    /**
     * getter pour l'attribut versement employeur
     * 
     * @return la valeur courante de l'attribut versement employeur
     */
    public Boolean getVersementEmployeur() {
        return versementEmployeur;
    }

    /**
     * Charge l'employeur avec lequel cette situation professionnelle est liée. Cette méthode recharge automatiquement
     * l'employeur si (et seulement si) la valeur de id employeur de ce bean a été modifiée.
     * 
     * @return l'employeur lié à ce droit (jamais null).
     * 
     * @throws Exception
     *             si l'employeur ne peut être chargé.
     */
    public IJEmployeur loadEmployeur() throws Exception {
        // si l'employeur est null, instancier
        if (employeur == null) {
            employeur = new IJEmployeur();
        }

        // on s'assure que la session est la bonne (pour les cas où on
        // chargerait le tiers...)
        employeur.setSession(getSession());

        // si l'employeur est différent, charger l'employeur
        if (!idEmployeur.equals(employeur.getIdEmployeur())) {
            employeur.setIdEmployeur(idEmployeur);
            employeur.retrieve(getSession().getCurrentThreadTransaction());
        }

        return employeur;
    }

    /**
     * setter pour l'attribut allocation max
     * 
     * @param allocationMax
     *            une nouvelle valeur pour cet attribut
     */
    public void setAllocationMax(Boolean allocationMax) {
        this.allocationMax = allocationMax;
    }

    /**
     * setter pour l'attribut annee correspondante
     * 
     * @param anneeCorrespondante
     *            une nouvelle valeur pour cet attribut
     */
    public void setAnneeCorrespondante(String anneeCorrespondante) {
        this.anneeCorrespondante = anneeCorrespondante;
    }

    /**
     * setter pour l'attribut autre remuneration
     * 
     * @param autreRemuneration
     *            une nouvelle valeur pour cet attribut
     */
    public void setAutreRemuneration(String autreRemuneration) {
        this.autreRemuneration = autreRemuneration;
    }

    /**
     * setter pour l'attribut cs periodicite autre remuneration
     * 
     * @param csPeriodiciteAutreRemuneration
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsPeriodiciteAutreRemuneration(String csPeriodiciteAutreRemuneration) {
        this.csPeriodiciteAutreRemuneration = csPeriodiciteAutreRemuneration;
    }

    /**
     * setter pour l'attribut cs periodicite autre salaire
     * 
     * @param csPeriodiciteAutreSalaire
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsPeriodiciteSalaire(String csPeriodiciteAutreSalaire) {
        csPeriodiciteSalaire = csPeriodiciteAutreSalaire;
    }

    /**
     * setter pour l'attribut cs periodicite salaire nature
     * 
     * @param csPeriodiciteSalaireNature
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsPeriodiciteSalaireNature(String csPeriodiciteSalaireNature) {
        this.csPeriodiciteSalaireNature = csPeriodiciteSalaireNature;
    }

    /**
     * setter pour l'attribut id employeur
     * 
     * @param idEmployeur
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    /**
     * setter pour l'attribut id grande IJ
     * 
     * @param idGrandeIJ
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrononce(String idGrandeIJ) {
        idPrononce = idGrandeIJ;
    }

    /**
     * setter pour l'attribut id situation professionnelle
     * 
     * @param idSituationProfessionnelle
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSituationProfessionnelle(String idSituationProfessionnelle) {
        this.idSituationProfessionnelle = idSituationProfessionnelle;
    }

    /**
     * setter pour l'attribut is independant
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsIndependant(Boolean boolean1) {
        isIndependant = boolean1;
    }

    /**
     * setter pour l'attribut nb heures semaine
     * 
     * @param nbHeuresSemaines
     *            une nouvelle valeur pour cet attribut
     */
    public void setNbHeuresSemaine(String nbHeuresSemaines) {
        nbHeuresSemaine = nbHeuresSemaines;
    }

    /**
     * setter pour l'attribut autre salaire setter pour l'attribut pourcent autre remuneration
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setPourcentAutreRemuneration(Boolean boolean1) {
        pourcentAutreRemuneration = boolean1;
    }

    /**
     * setter pour l'attribut salaire horaire
     * 
     * @param autreSalaire
     *            une nouvelle valeur pour cet attribut
     */
    public void setSalaire(String autreSalaire) {
        salaire = autreSalaire;
    }

    /**
     * setter pour l'attribut salaire nature
     * 
     * @param salaireNature
     *            une nouvelle valeur pour cet attribut
     */
    public void setSalaireNature(String salaireNature) {
        this.salaireNature = salaireNature;
    }

    /**
     * setter pour l'attribut soumis cotisation AC
     * 
     * @param soumisCotisationAC
     *            une nouvelle valeur pour cet attribut
     */
    public void setSoumisCotisationAC(Boolean soumisCotisationAC) {
        this.soumisCotisationAC = soumisCotisationAC;
    }

    /**
     * setter pour l'attribut soumis cotisation AVSAI
     * 
     * @param soumisCotisationAVSAI
     *            une nouvelle valeur pour cet attribut
     */
    public void setSoumisCotisationAVSAI(Boolean soumisCotisationAVSAI) {
        this.soumisCotisationAVSAI = soumisCotisationAVSAI;
    }

    /**
     * setter pour l'attribut unique primary key
     * 
     * @param pk
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdSituationProfessionnelle(pk);
    }

    /**
     * setter pour l'attribut versement employeur
     * 
     * @param versementEmployeur
     *            une nouvelle valeur pour cet attribut
     */
    public void setVersementEmployeur(Boolean versementEmployeur) {
        this.versementEmployeur = versementEmployeur;
    }
}
