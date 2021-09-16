package globaz.corvus.db.rentesaccordees;

import globaz.corvus.utils.beneficiaire.principal.REBeneficiairePrincipal;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRDateValidator;
import ch.globaz.prestation.domaine.CodePrestation;

public class REPrestationsAccordees extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int ALTERNATE_KEY_ID_INFO_COMPTA = 1;
    public static final String FIELDNAME_CODE_PRESTATION = "ZTLCPR";
    public static final String FIELDNAME_CS_ETAT = "ZTTETA";

    public static final String FIELDNAME_DATE_DEBUT_DROIT = "ZTDDDR";
    public static final String FIELDNAME_DATE_ECHEANCE = "ZTDECH";
    public static final String FIELDNAME_DATE_FIN_DROIT = "ZTDFDR";
    public static final String FIELDNAME_FRACTION_RENTE = "ZTLFRR";
    public static final String FIELDNAME_QUOTITE_RENTE = "QUOTITE_RENTE";
    public static final String FIELDNAME_GENRE_PRESTATION_ACCORDEE = "ZTTGEN";
    public static final String FIELDNAME_ID_CALCUL_INTERET_MORATOIRE = "ZTICIM";
    public static final String FIELDNAME_ID_DEMANDE_PRINCIPALE_ANNULANTE = "ZTIDPA";
    public static final String FIELDNAME_ID_ENTETE_BLOCAGE = "ZTIEBK";
    public static final String FIELDNAME_ID_INFO_COMPTA = "ZTIICT";
    public static final String FIELDNAME_ID_PRESTATION_ACCORDEE = "ZTIPRA";
    public static final String FIELDNAME_ID_TIERS_BENEFICIAIRE = "ZTITBE";
    public static final String FIELDNAME_IS_ATTENTE_MAJ_BLOCAGE = "ZTBAMB";
    public static final String FIELDNAME_IS_ATTENTE_MAJ_RETENUE = "ZTBAMR";
    public static final String FIELDNAME_IS_ERREUR = "ZTBERR";
    public static final String FIELDNAME_IS_PRESTATION_BLOQUEE = "ZTBPRB";
    public static final String FIELDNAME_IS_RETENUES = "ZTBRET";
    public static final String FIELDNAME_MONTANT_PRESTATION = "ZTMPRE";

    public static final String FIELDNAME_REFERENCE_PMT = "ZTLRFP";
    public static final String FIELDNAME_SOUS_TYPE_GENRE_PRESTATION = "ZTLSCP";

    public static final String FIELDNAME_TYPE_MAJ = "ZTTTMA";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    public static final int GROUPE_API_AI = 600;
    public static final int GROUPE_API_AVS = 500;
    public static final int GROUPE_PC_AI = 1000;
    public static final int GROUPE_PC_AVS = 800;

    public static final int GROUPE_REO_AI = 400;
    public static final int GROUPE_REO_AVS = 200;
    public static final int GROUPE_RFM_AI = 900;
    public static final int GROUPE_RFM_AVS = 700;
    public static final int GROUPE_RO_AI = 300;
    public static final int GROUPE_RO_AVS = 100;

    public static final String TABLE_NAME_PRESTATIONS_ACCORDEES = "REPRACC";

    public static int getGroupeGenreRente(final String codePrestationString) {
        int result = 0;

        CodePrestation codePrestation = CodePrestation.parse(codePrestationString);

        if (codePrestation.isVieillesse() || codePrestation.isSurvivant()) {

            if (codePrestation.isRenteOrdinaire()) {
                result = REPrestationsAccordees.GROUPE_RO_AVS;
            } else if (codePrestation.isRenteExtraordinaire()) {
                result = REPrestationsAccordees.GROUPE_REO_AVS;
            }

        } else if (codePrestation.isAI()) {

            if (codePrestation.isRenteOrdinaire()) {
                result = REPrestationsAccordees.GROUPE_RO_AI;
            } else if (codePrestation.isRenteExtraordinaire()) {
                result = REPrestationsAccordees.GROUPE_REO_AI;
            }

        } else if (codePrestation.isAPIAVS()) {

            result = REPrestationsAccordees.GROUPE_API_AVS;

        } else if (codePrestation.isAPIAI()) {

            result = REPrestationsAccordees.GROUPE_API_AI;

        } else if (codePrestation.isPC()) {

            if (codePrestation.isPrestationComplementaireAUneRenteAI()) {
                result = REPrestationsAccordees.GROUPE_PC_AI;
            } else if (codePrestation.isPrestationComplementaireAUneRenteAVS()) {
                result = REPrestationsAccordees.GROUPE_PC_AVS;
            }

        } else if (codePrestation.isRFM()) {

            if (codePrestation.isPrestationComplementaireAUneRenteAVS()) {
                result = REPrestationsAccordees.GROUPE_RFM_AVS;
            } else if (codePrestation.isPrestationComplementaireAUneRenteAI()) {
                result = REPrestationsAccordees.GROUPE_RFM_AI;
            }

        }

        return result;
    }

    private String codePrestation = "";
    private String csEtat = "";
    private String csGenre = "";
    private String dateDebutDroit = "";

    private String dateEcheance = "";
    private String dateFinDroit = "";
    private String fractionRente = "";
    private String quotiteRente = "";
    private String idCalculInteretMoratoire = "";
    private String idDemandePrincipaleAnnulante = "";
    private String idEnteteBlocage = "";
    private String idInfoCompta = "";
    protected String idPrestationAccordee = "";
    private String idTiersBeneficiaire = "";
    private transient REInformationsComptabilite infoCompta = null;

    private Boolean isAttenteMajBlocage = Boolean.FALSE;
    private Boolean isAttenteMajRetenue = Boolean.FALSE;
    private Boolean isErreur = Boolean.FALSE;
    private Boolean isPrestationBloquee = Boolean.FALSE;
    private Boolean isRetenues = Boolean.FALSE;
    private String montantPrestation = "";

    private String referencePmt = "";
    private String sousTypeGenrePrestation;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String typeDeMiseAJours = "";

    /**
     * @param transaction
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(final BTransaction transaction) throws Exception {
        idPrestationAccordee = this._incCounter(transaction, idPrestationAccordee,
                REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(final BStatement statement) throws Exception {

        idPrestationAccordee = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        csGenre = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_GENRE_PRESTATION_ACCORDEE);

        idTiersBeneficiaire = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        codePrestation = statement.dbReadString(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
        sousTypeGenrePrestation = statement.dbReadString(REPrestationsAccordees.FIELDNAME_SOUS_TYPE_GENRE_PRESTATION);
        fractionRente = statement.dbReadString(REPrestationsAccordees.FIELDNAME_FRACTION_RENTE);
        quotiteRente = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_QUOTITE_RENTE);
        montantPrestation = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
        dateDebutDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT));
        dateFinDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT));
        csEtat = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_CS_ETAT);
        idDemandePrincipaleAnnulante = statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_DEMANDE_PRINCIPALE_ANNULANTE);
        idInfoCompta = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA);
        isRetenues = statement.dbReadBoolean(REPrestationsAccordees.FIELDNAME_IS_RETENUES);
        isErreur = statement.dbReadBoolean(REPrestationsAccordees.FIELDNAME_IS_ERREUR);
        isPrestationBloquee = statement.dbReadBoolean(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE);
        referencePmt = statement.dbReadString(REPrestationsAccordees.FIELDNAME_REFERENCE_PMT);
        idCalculInteretMoratoire = statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_CALCUL_INTERET_MORATOIRE);
        idEnteteBlocage = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_ENTETE_BLOCAGE);
        dateEcheance = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_ECHEANCE));

        isAttenteMajBlocage = statement.dbReadBoolean(REPrestationsAccordees.FIELDNAME_IS_ATTENTE_MAJ_BLOCAGE);
        isAttenteMajRetenue = statement.dbReadBoolean(REPrestationsAccordees.FIELDNAME_IS_ATTENTE_MAJ_RETENUE);
        typeDeMiseAJours = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_TYPE_MAJ);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(final BStatement statement) throws Exception {

        if (!JadeStringUtil.isBlankOrZero(dateEcheance)) {
            if (!PRDateValidator.isDateFormat_MMxAAAA(getDateEcheance())) {
                _addError(statement.getTransaction(), getSession().getLabel("ERREUR_DATE_ECHEANCE_NON_VALIDE"));
                setMsgType(FWViewBeanInterface.ERROR);
                setMessage(getSession().getLabel("ERREUR_DATE_ECHEANCE_NON_VALIDE"));
            }
        }

    }

    @Override
    protected void _writeAlternateKey(final BStatement statement, final int alternateKey) throws Exception {
        switch (alternateKey) {
            case ALTERNATE_KEY_ID_INFO_COMPTA:
                statement.writeKey(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA,
                        this._dbWriteNumeric(statement.getTransaction(), getIdInfoCompta(), "idInfoCompta"));
                break;
            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(final globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE,
                this._dbWriteNumeric(statement.getTransaction(), idPrestationAccordee, "idPrestationAccordee"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(final BStatement statement) throws Exception {

        statement.writeField(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE,
                this._dbWriteNumeric(statement.getTransaction(), idPrestationAccordee, "idPrestationAccordee"));
        statement.writeField(REPrestationsAccordees.FIELDNAME_GENRE_PRESTATION_ACCORDEE,
                this._dbWriteNumeric(statement.getTransaction(), csGenre, "csGenre"));

        statement.writeField(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE,
                this._dbWriteNumeric(statement.getTransaction(), idTiersBeneficiaire, "idTiersBeneficiaire"));
        statement.writeField(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION,
                this._dbWriteString(statement.getTransaction(), codePrestation, "codePrestation"));
        statement.writeField(REPrestationsAccordees.FIELDNAME_FRACTION_RENTE,
                this._dbWriteString(statement.getTransaction(), fractionRente, "fractionRente"));
        statement.writeField(REPrestationsAccordees.FIELDNAME_QUOTITE_RENTE,
                this._dbWriteNumeric(statement.getTransaction(), quotiteRente, "quotiteRente"));
        statement.writeField(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION,
                this._dbWriteNumeric(statement.getTransaction(), montantPrestation, "monatantPrestation"));
        statement.writeField(
                REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT,
                this._dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateDebutDroit), "dateDebutDroit"));
        statement.writeField(
                REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT,
                this._dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateFinDroit), "dateFinDroit"));
        statement.writeField(REPrestationsAccordees.FIELDNAME_CS_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), csEtat, "csEtat"));
        statement.writeField(REPrestationsAccordees.FIELDNAME_ID_DEMANDE_PRINCIPALE_ANNULANTE, this._dbWriteNumeric(
                statement.getTransaction(), idDemandePrincipaleAnnulante, "idDemandePrincipaleAnnulante"));
        statement.writeField(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA,
                this._dbWriteNumeric(statement.getTransaction(), idInfoCompta, "idInfoCompta"));
        statement.writeField(REPrestationsAccordees.FIELDNAME_IS_RETENUES, this._dbWriteBoolean(
                statement.getTransaction(), isRetenues, BConstants.DB_TYPE_BOOLEAN_CHAR, "isRetenues"));
        statement.writeField(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE,
                this._dbWriteBoolean(statement.getTransaction(), isPrestationBloquee, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "isPrestationBloquee"));
        statement
                .writeField(REPrestationsAccordees.FIELDNAME_IS_ERREUR, this._dbWriteBoolean(
                        statement.getTransaction(), isErreur, BConstants.DB_TYPE_BOOLEAN_CHAR, "isErreur"));
        statement.writeField(REPrestationsAccordees.FIELDNAME_REFERENCE_PMT,
                this._dbWriteString(statement.getTransaction(), referencePmt, "referencePmt"));
        statement.writeField(REPrestationsAccordees.FIELDNAME_ID_CALCUL_INTERET_MORATOIRE,
                this._dbWriteNumeric(statement.getTransaction(), idCalculInteretMoratoire, "idCalculInteretMoratoire"));
        statement.writeField(
                REPrestationsAccordees.FIELDNAME_DATE_ECHEANCE,
                this._dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateEcheance), "dateEcheance"));
        statement.writeField(REPrestationsAccordees.FIELDNAME_ID_ENTETE_BLOCAGE,
                this._dbWriteNumeric(statement.getTransaction(), idEnteteBlocage, "idEnteteBlocage"));

        statement.writeField(REPrestationsAccordees.FIELDNAME_IS_ATTENTE_MAJ_BLOCAGE,
                this._dbWriteBoolean(statement.getTransaction(), isAttenteMajBlocage, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "isAttenteMajBlocage"));
        statement.writeField(REPrestationsAccordees.FIELDNAME_IS_ATTENTE_MAJ_RETENUE,
                this._dbWriteBoolean(statement.getTransaction(), isAttenteMajRetenue, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "isAttenteMajRetenue"));
        statement.writeField(REPrestationsAccordees.FIELDNAME_TYPE_MAJ,
                this._dbWriteNumeric(statement.getTransaction(), typeDeMiseAJours, "typeDeMiseAJours"));

        statement.writeField(REPrestationsAccordees.FIELDNAME_SOUS_TYPE_GENRE_PRESTATION,
                this._dbWriteString(statement.getTransaction(), sousTypeGenrePrestation, "sousTypeGenrePrestation"));

    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // Auto-generated method stub
        return super.clone();
    }

    /**
     * @return
     */
    public String getCodePrestation() {
        return codePrestation;
    }

    /**
     * @return
     */
    public String getCsEtat() {
        return csEtat;
    }

    public String getCsGenre() {
        return csGenre;
    }

    /**
     * @return
     */
    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    /**
     * @return
     */
    public String getDateFinDroit() {
        return dateFinDroit;
    }

    /**
     * @return
     */
    public String getFractionRente() {
        return fractionRente;
    }

    public String getFractionRenteWithZeroWhenBlank() {

        if (JadeStringUtil.isBlank(fractionRente)) {
            return "0";
        }

        return fractionRente;
    }

    public String getQuotiteRente(){
        return quotiteRente;
    }

    public int getGroupeGenreRente() {

        return REPrestationsAccordees.getGroupeGenreRente(getCodePrestation());
    }

    /**
     * @return
     */
    public String getIdCalculInteretMoratoire() {
        return idCalculInteretMoratoire;
    }

    /**
     * @return
     */
    public String getIdDemandePrincipaleAnnulante() {
        return idDemandePrincipaleAnnulante;
    }

    public String getIdEnteteBlocage() {
        return idEnteteBlocage;
    }

    /**
     * @return
     */
    public String getIdInfoCompta() {
        return idInfoCompta;
    }

    public String getIdPrestationAccordee() {
        return idPrestationAccordee;
    }

    /**
     * @return
     */
    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public Boolean getIsAttenteMajBlocage() {
        return isAttenteMajBlocage;
    }

    public Boolean getIsAttenteMajRetenue() {
        return isAttenteMajRetenue;
    }

    public Boolean getIsErreur() {
        return isErreur;
    }

    public Boolean getIsPrestationBloquee() {
        return isPrestationBloquee;
    }

    /**
     * @return the isRetenu
     */
    public Boolean getIsRetenues() {
        return isRetenues;
    }

    /**
     * @return
     */
    public String getMontantPrestation() {
        return montantPrestation;
    }

    /**
     * @return the referencePmt
     */
    public String getReferencePmt() {
        return referencePmt;
    }

    /**
     * @return the sousTypeGenrePrestation
     */
    public final String getSousTypeGenrePrestation() {
        return sousTypeGenrePrestation;
    }

    public String getTypeDeMiseAJours() {
        return typeDeMiseAJours;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#hasCreationSpy()
     */
    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public REInformationsComptabilite loadInformationsComptabilite() throws Exception {

        if ((infoCompta != null) && !infoCompta.isNew()) {
            return infoCompta;
        } else {

            infoCompta = new REInformationsComptabilite();
            infoCompta.setSession(getSession());
            infoCompta.setIdInfoCompta(getIdInfoCompta());
            infoCompta.retrieve();

            PRAssert.notIsNew(infoCompta, null);
            return infoCompta;
        }
    }

    /**
     * Permet de définir le groupe Level en fonction de la rente en cours.
     * 
     * @return 1, 2, 3, 4, ou 5 (selon methode getGroupLevel de la classe REBeneficiairePrincipal
     *         retourne 0 si une exception est subvenue lors de la récupération du group level
     */
    public int getGroupLevelRente() {
        try {
            return REBeneficiairePrincipal.getGroupLevel(getSession(), getSession().getCurrentThreadTransaction(),
                    getIdPrestationAccordee());
        } catch (Exception e) {
            // Cette méthode est utilisée au chargement d'un écran des rentes. Pour éviter de lancer une exception au
            // chargement, on
            // retourne un group level à 0.
            JadeLogger.error(this, e.getMessage());
            return 0;
        }
    }

    /**
     * Permet d'obtenir le label de warning de l'âge des 25 ans dépassés pour le tiers
     * 
     * @return label de warning de l'âge des 25 ans dépassés
     * @throws Exception
     */
    public String get25AnsWarningLabel() {
        return getSession().getLabel("WARNING_AGE_25_ANS_DEPASSES");
    }

    /**
     * @param string
     */
    public void setCodePrestation(final String string) {
        codePrestation = string;
    }

    /**
     * @param string
     */
    public void setCsEtat(final String string) {
        csEtat = string;
    }

    public void setCsGenre(final String csGenre) {
        this.csGenre = csGenre;
    }

    /**
     * @param string
     */
    public void setDateDebutDroit(final String string) {
        dateDebutDroit = string;
    }

    public void setDateEcheance(final String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    /**
     * @param string
     */
    public void setDateFinDroit(final String string) {
        dateFinDroit = string;
    }

    /**
     * @param string
     */
    public void setFractionRente(final String string) {
        fractionRente = string;
    }

    public void setQuotiteRente(final String quotite){
        this.quotiteRente = quotite;
    }

    /**
     * @param string
     */
    public void setIdCalculInteretMoratoire(final String string) {
        idCalculInteretMoratoire = string;
    }

    /**
     * @param string
     */
    public void setIdDemandePrincipaleAnnulante(final String string) {
        idDemandePrincipaleAnnulante = string;
    }

    public void setIdEnteteBlocage(final String idEnteteBlocage) {
        this.idEnteteBlocage = idEnteteBlocage;
    }

    /**
     * @param string
     */
    public void setIdInfoCompta(final String string) {
        idInfoCompta = string;
    }

    public void setIdPrestationAccordee(final String idPrestationAccordee) {
        this.idPrestationAccordee = idPrestationAccordee;
    }

    /**
     * @param string
     */
    public void setIdTiersBeneficiaire(final String string) {
        idTiersBeneficiaire = string;
    }

    public void setIsAttenteMajBlocage(final Boolean isAttenteMajBlocage) {
        this.isAttenteMajBlocage = isAttenteMajBlocage;
    }

    public void setIsAttenteMajRetenue(final Boolean isAttenteMajRetenue) {
        this.isAttenteMajRetenue = isAttenteMajRetenue;
    }

    public void setIsErreur(final Boolean isErreur) {
        this.isErreur = isErreur;
    }

    public void setIsPrestationBloquee(final Boolean isPrestationBloquee) {
        this.isPrestationBloquee = isPrestationBloquee;
    }

    /**
     * @param isRetenu
     *            the isRetenu to set
     */
    public void setIsRetenues(final Boolean isRetenues) {
        this.isRetenues = isRetenues;
    }

    /**
     * @param string
     */
    public void setMontantPrestation(final String string) {
        montantPrestation = string;
    }

    /**
     * @param referencePmt
     *            the referencePmt to set
     */
    public void setReferencePmt(final String referencePmt) {
        this.referencePmt = referencePmt;
    }

    /**
     * @param sousTypeGenrePrestation
     *            the sousTypeGenrePrestation to set
     */
    public final void setSousTypeGenrePrestation(final String sousTypeGenrePrestation) {
        this.sousTypeGenrePrestation = sousTypeGenrePrestation;
    }

    public void setTypeDeMiseAJours(final String typeDeMiseAJours) {
        this.typeDeMiseAJours = typeDeMiseAJours;
    }

}
