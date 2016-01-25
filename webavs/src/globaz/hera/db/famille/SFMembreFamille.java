package globaz.hera.db.famille;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.commons.nss.NSUtil;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAStringFormatter;
import globaz.globall.util.JAUtil;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.interfaces.tiers.SFTiersHelper;
import globaz.hera.interfaces.tiers.SFTiersWrapper;
import globaz.hera.tools.SFStringUtils;
import globaz.hera.tools.nss.INSSViewBean;
import globaz.hera.tools.nss.SFUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

/**
 * SFMBRFAM avec un lien sur les tables de tiers dans le cas d'une recherche sur un idTiers
 * 
 * @author MMU
 */
public class SFMembreFamille extends BEntity implements INSSViewBean {

    private static final long serialVersionUID = 1L;
    public static final int ALTERNATE_KEY_IDTIERS = 1;
    public static final int ALTERNATE_KEY_NOMPRENOMDATENAI = 2;
    public static final String FIELD_AVS_IDTIERS = "HTITIE";
    public static final String FIELD_AVS_NOAVS = "HXNAVS";
    public static final String FIELD_CANTON = "WGTCDO";
    public static final String FIELD_DATEDECES = "WGDDEC";
    public static final String FIELD_DATENAISSANCE = "WGDNAI";
    public static final String FIELD_DOMAINE_APPLICATION = "WGTDOA";
    public static final String FIELD_IDMEMBREFAMILLE = "WGIMEF";
    public static final String FIELD_IDTIERS = "WGITIE";
    public static final String FIELD_NATIONALITE = "WGTNAT";
    public static final String FIELD_NOM = "WGLNOM";
    public static final String FIELD_NOM_FOR_SEARCH = "WGLNOU";
    /**
     * Pays de domicile
     * Renseigné si canton est vide
     */
    public static final String FIELD_PAYS_DE_DOMICILE = "WGTPDO";
    public static final String FIELD_PERS_CANTON = "HPTCAN";
    public static final String FIELD_PERS_DATEDEC = "HPDDEC";
    public static final String FIELD_PERS_DATENAI = "HPDNAI";
    public static final String FIELD_PERS_IDTIERS = "HTITIE";
    public static final String FIELD_PERS_SEX = "HPTSEX";
    public static final String FIELD_PRENOM = "WGLPRE";
    public static final String FIELD_PRENOM_FOR_SEARCH = "WGLPRU";
    public static final String FIELD_SEX = "WGTSEX";

    public static final String FIELD_TI_IDTIERS = "HTITIE";
    public static final String FIELD_TI_NOM = "HTLDE1";
    public static final String FIELD_TI_NOM_FOR_SEARCH = "HTLDU1";
    public static final String FIELD_TI_PAYS = "HNIPAY";
    public static final String FIELD_TI_PRENOM = "HTLDE2";
    public static final String FIELD_TI_PRENOM_FOR_SEARCH = "HTLDU2";
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HIST = "TIHAVSP";
    public static final String TABLE_NAME = "SFMBRFAM";
    public static final String TABLE_PERS = "TIPERSP";
    public static final String TABLE_RELATION_FAMILIALE = "SFREFARE";
    public static final String TABLE_TIERS = "TITIERP";

    private String csCantonDomicile = "";
    private String csDomaineApplication = "";
    private String csNationalite = "";
    private String csSexe = "";
    private String dateDeces = "";
    private String dateNaissance = "";
    private String idAssure = "";
    private String idMembreFamille = "";
    private String idTiers = "";
    private String nom = "";
    // checkAVS vérifie aussi la présence du n°AVS
    private String nss = ""; // initialisé pour pas que le checkAVS braille :
    private String pays = "";
    private String prenom = "";
    private String provenance = "";
    private String relationAuRequerant = "";

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdMembreFamille(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        return SFMembreFamille.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idMembreFamille = statement.dbReadNumeric(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
        idTiers = statement.dbReadNumeric(SFMembreFamille.FIELD_IDTIERS);
        csDomaineApplication = statement.dbReadNumeric(SFMembreFamille.FIELD_DOMAINE_APPLICATION);

        if (!isLoadedFromManager() && !JadeStringUtil.isIntegerEmpty(idTiers)) {
            // load from Tiers
            SFTiersWrapper tw = SFTiersHelper.getTiersAdresseDomicileParId(getSession(), getIdTiers(),
                    new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime()));

            nom = tw.getProperty(SFTiersWrapper.PROPERTY_NOM);
            prenom = tw.getProperty(SFTiersWrapper.PROPERTY_PRENOM);
            nss = tw.getProperty(SFTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            dateNaissance = tw.getProperty(SFTiersWrapper.PROPERTY_DATE_NAISSANCE);
            dateDeces = tw.getProperty(SFTiersWrapper.PROPERTY_DATE_DECES);
            csSexe = tw.getProperty(SFTiersWrapper.PROPERTY_SEXE);

            csCantonDomicile = tw.getProperty(SFTiersWrapper.PROPERTY_ID_CANTON);

            if (JadeStringUtil.isIntegerEmpty(csCantonDomicile)) {
                csCantonDomicile = statement.dbReadNumeric(SFMembreFamille.FIELD_CANTON);
            }
            // Nationalite
            csNationalite = tw.getProperty(SFTiersWrapper.PROPERTY_ID_PAYS);

            /*
             * On récupère le pays que si il n'y a pas de canton renseigné dans les tiers et dans la sit fam
             * Dans ce cas on recherche en premier le pays dans les tiers et ensuite dans la sit fam s'il n'est pas
             * renseigné dans les tiers
             */
            pays = tw.getProperty(SFTiersWrapper.PROPERTY_ID_PAYS_DOMICILE);   // statement.dbReadNumeric(SFMembreFamille.FIELD_TI_PAYS);

            if (JadeStringUtil.isIntegerEmpty(pays)) {
                pays = statement.dbReadNumeric(SFMembreFamille.FIELD_PAYS_DE_DOMICILE);
            }

        } else {

            if (JadeStringUtil.isIntegerEmpty(idTiers)) {
                nom = statement.dbReadString(SFMembreFamille.FIELD_NOM);
                prenom = statement.dbReadString(SFMembreFamille.FIELD_PRENOM);
                dateNaissance = statement.dbReadDateAMJ(SFMembreFamille.FIELD_DATENAISSANCE);
                dateDeces = statement.dbReadDateAMJ(SFMembreFamille.FIELD_DATEDECES);
                csSexe = statement.dbReadNumeric(SFMembreFamille.FIELD_SEX);

                csCantonDomicile = statement.dbReadNumeric(SFMembreFamille.FIELD_CANTON);
                pays = statement.dbReadNumeric(SFMembreFamille.FIELD_PAYS_DE_DOMICILE);

                csNationalite = statement.dbReadNumeric(SFMembreFamille.FIELD_NATIONALITE);
            }

            else { // on va rechercher dans les tables tiers
                nom = statement.dbReadString(SFMembreFamille.FIELD_TI_NOM);
                prenom = statement.dbReadString(SFMembreFamille.FIELD_TI_PRENOM);
                nss = statement.dbReadString(SFMembreFamille.FIELD_AVS_NOAVS);
                dateNaissance = statement.dbReadDateAMJ(SFMembreFamille.FIELD_PERS_DATENAI);
                dateDeces = statement.dbReadDateAMJ(SFMembreFamille.FIELD_PERS_DATEDEC);
                csSexe = statement.dbReadNumeric(SFMembreFamille.FIELD_PERS_SEX);

                // point ouvert 935
                // le canton n'est pas utilise dans les tiers pour une personne avs

                csCantonDomicile = statement.dbReadNumeric(SFMembreFamille.FIELD_CANTON);
                pays = statement.dbReadNumeric(SFMembreFamille.FIELD_PAYS_DE_DOMICILE);
            }
        }
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {

        if (JadeStringUtil.isBlankOrZero(getCsDomaineApplication())) {
            _addError(statement.getTransaction(), getSession().getLabel("ERROR_DOMAINE_NON_RENSEIGNE"));
        }

        if (!JadeStringUtil.isEmpty(getNSS()) && !JadeStringUtil.isIntegerEmpty(getNSS())) {

            if (NSUtil.unFormatAVS(getNSS()).length() > 11) {
                if (!NSUtil.nssCheckDigit(getNSS())) {
                    _addError(statement.getTransaction(), getSession().getLabel("ERROR_NUM_AVS"));
                }
            } else {
                _checkAVS(statement.getTransaction(), getNSS(), getSession().getLabel("ERROR_NUM_AVS"));
            }

        }

        // La date de début doit être renseignée
        if (JadeStringUtil.isEmpty(idTiers)) {
            if (!JAUtil.isDateEmpty(dateNaissance)) {
                _checkDate(statement.getTransaction(), dateNaissance, "DATE_INVALIDE");
            } else {
                _addError(statement.getTransaction(), getSession().getLabel("VALIDATE_DATE_NAISSANCE_OBLIGATOIRE"));
            }

            // La date de fin doit être postérieure à la date de début
            if (!JAUtil.isDateEmpty(dateDeces)) {
                if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateNaissance, dateDeces)) {
                    _addError(statement.getTransaction(), getSession().getLabel("VALIDATE_NAISSANCE_AVANT_DECES"));
                }
            }
        }
    }

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        if (alternateKey == SFMembreFamille.ALTERNATE_KEY_IDTIERS) {
            statement.writeKey(SFMembreFamille.FIELD_IDTIERS,
                    this._dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
            statement
                    .writeKey(SFMembreFamille.FIELD_DOMAINE_APPLICATION, this._dbWriteNumeric(
                            statement.getTransaction(), getCsDomaineApplication(), "csDomaineApplication"));
        } else if (alternateKey == SFMembreFamille.ALTERNATE_KEY_NOMPRENOMDATENAI) {
            /*
             * statement.writeKey(FIELD_NOM, _dbWriteString(statement.getTransaction(), nom, "nom"));
             * statement.writeKey(FIELD_PRENOM, _dbWriteString(statement.getTransaction(), prenom, "prenom"));
             * statement.writeKey(FIELD_DATENAISSANCE, _dbWriteDateAMJ(statement.getTransaction(), dateNaissance,
             * "dateNaissance")); statement.writeKey(FIELD_CANTON, _dbWriteNumeric(statement.getTransaction(),
             * csCantonDomicile, "csCantonDomicile")); statement.writeKey(FIELD_PAYS,
             * _dbWriteNumeric(statement.getTransaction(), pays, "pays")); statement.writeKey(FIELD_NATIONALITE,
             * _dbWriteNumeric(statement.getTransaction(), csNationalite, "csNationalite"));
             */
        } else {
            super._writeAlternateKey(statement, alternateKey);
        }

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(SFMembreFamille.FIELD_IDMEMBREFAMILLE,
                this._dbWriteNumeric(statement.getTransaction(), idMembreFamille, "idMembreFamille"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(SFMembreFamille.FIELD_IDMEMBREFAMILLE,
                this._dbWriteNumeric(statement.getTransaction(), idMembreFamille, "idMembreFamille"));
        statement.writeField(SFMembreFamille.FIELD_IDTIERS,
                this._dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
        statement.writeField(SFMembreFamille.FIELD_CANTON,
                this._dbWriteNumeric(statement.getTransaction(), csCantonDomicile, "csCantonDomicile"));
        statement.writeField(SFMembreFamille.FIELD_DOMAINE_APPLICATION,
                this._dbWriteNumeric(statement.getTransaction(), csDomaineApplication, "csDomaineApplication"));

        // Si le numéro de tiers est renseigné, on ne stoque aucune de ses
        // données tiers
        if (JadeStringUtil.isIntegerEmpty(idTiers)) {
            statement
                    .writeField(SFMembreFamille.FIELD_NOM, this._dbWriteString(statement.getTransaction(), nom, "nom"));
            statement.writeField(SFMembreFamille.FIELD_PRENOM,
                    this._dbWriteString(statement.getTransaction(), prenom, "prenom"));
            statement.writeField(SFMembreFamille.FIELD_NOM_FOR_SEARCH, this._dbWriteString(statement.getTransaction(),
                    SFStringUtils.upperCaseWithoutSpecialChars(nom), "nom"));
            statement.writeField(SFMembreFamille.FIELD_PRENOM_FOR_SEARCH, this._dbWriteString(
                    statement.getTransaction(), SFStringUtils.upperCaseWithoutSpecialChars(prenom), "prenom"));
            statement.writeField(SFMembreFamille.FIELD_DATENAISSANCE,
                    this._dbWriteDateAMJ(statement.getTransaction(), dateNaissance, "dateNaissance"));
            statement.writeField(SFMembreFamille.FIELD_DATEDECES,
                    this._dbWriteDateAMJ(statement.getTransaction(), dateDeces, "dateDeces"));
            statement.writeField(SFMembreFamille.FIELD_SEX,
                    this._dbWriteNumeric(statement.getTransaction(), csSexe, "sex"));

            // Si le canton est renseigné on ne renseigne pas le pays
            if (JadeStringUtil.isBlankOrZero(csCantonDomicile)) {
                statement.writeField(SFMembreFamille.FIELD_PAYS_DE_DOMICILE,
                        this._dbWriteNumeric(statement.getTransaction(), pays, "pays"));
            } else {
                statement.writeField(SFMembreFamille.FIELD_PAYS_DE_DOMICILE,
                        this._dbWriteNumeric(statement.getTransaction(), null, "pays"));
            }

            statement.writeField(SFMembreFamille.FIELD_NATIONALITE,
                    this._dbWriteNumeric(statement.getTransaction(), csNationalite, "csNationalite"));
        } else {
            statement.writeField(SFMembreFamille.FIELD_NOM,
                    this._dbWriteString(statement.getTransaction(), null, "nom"));
            statement.writeField(SFMembreFamille.FIELD_PRENOM,
                    this._dbWriteString(statement.getTransaction(), null, "prenom"));
            statement.writeField(SFMembreFamille.FIELD_NOM_FOR_SEARCH,
                    this._dbWriteString(statement.getTransaction(), null, "nom"));
            statement.writeField(SFMembreFamille.FIELD_PRENOM_FOR_SEARCH,
                    this._dbWriteString(statement.getTransaction(), null, "prenom"));
            statement.writeField(SFMembreFamille.FIELD_DATENAISSANCE,
                    this._dbWriteDateAMJ(statement.getTransaction(), null, "dateNaissance"));
            statement.writeField(SFMembreFamille.FIELD_DATEDECES,
                    this._dbWriteDateAMJ(statement.getTransaction(), null, "dateDeces"));
            statement.writeField(SFMembreFamille.FIELD_SEX,
                    this._dbWriteNumeric(statement.getTransaction(), null, "sex"));
            statement.writeField(SFMembreFamille.FIELD_PAYS_DE_DOMICILE,
                    this._dbWriteNumeric(statement.getTransaction(), pays, "pays"));
            statement.writeField(SFMembreFamille.FIELD_NATIONALITE,
                    this._dbWriteNumeric(statement.getTransaction(), null, "csNationalite"));
        }
    }

    /**
     * Effectue une jointure sur les tiers pour être synchro avec le nom, prenom, numAvs des tiers
     */
    public static String createFromClause(String schema) {
        return SFMembreFamille.createFromClause(schema, SFMembreFamille.TABLE_NAME, SFMembreFamille.TABLE_TIERS,
                SFMembreFamille.TABLE_PERS, SFMembreFamille.TABLE_AVS);
    }

    public static String createFromClause(String schema, String aliasMembre, String aliasTiTiers, String aliasTiPers,
            String aliasTiAvs) {
        String createFrom = schema + SFMembreFamille.TABLE_NAME + " AS " + aliasMembre + " LEFT JOIN " + schema
                + SFMembreFamille.TABLE_TIERS + " AS " + aliasTiTiers + " ON (" + aliasMembre + "."
                + SFMembreFamille.FIELD_IDTIERS + " = " + aliasTiTiers + "." + SFMembreFamille.FIELD_TI_IDTIERS + ")"
                + " LEFT JOIN " + schema + SFMembreFamille.TABLE_PERS + " AS " + aliasTiPers + " ON (" + aliasMembre
                + "." + SFMembreFamille.FIELD_IDTIERS + " = " + aliasTiPers + "." + SFMembreFamille.FIELD_PERS_IDTIERS
                + ")"

                + " LEFT JOIN " + schema + SFMembreFamille.TABLE_AVS + " AS " + aliasTiAvs + " ON (" + aliasMembre
                + "." + SFMembreFamille.FIELD_IDTIERS + " = " + aliasTiAvs + "." + SFMembreFamille.FIELD_AVS_IDTIERS
                + ")";

        return createFrom;
    }

    public static String createJoinClause(String schema) {
        return SFMembreFamille.createJoinClause(schema, SFMembreFamille.TABLE_NAME, SFMembreFamille.TABLE_TIERS,
                SFMembreFamille.TABLE_PERS, SFMembreFamille.TABLE_AVS);
    }

    public static String createJoinClause(String schema, String aliasMembre, String aliasTiTiers, String aliasTiPers,
            String aliasTiAvs) {
        String createJoin = " LEFT JOIN " + schema + SFMembreFamille.TABLE_TIERS + " AS " + aliasTiTiers + " ON ("
                + aliasMembre + "." + SFMembreFamille.FIELD_IDTIERS + " = " + aliasTiTiers + "."
                + SFMembreFamille.FIELD_TI_IDTIERS + ")" + " LEFT JOIN " + schema + SFMembreFamille.TABLE_PERS + " AS "
                + aliasTiPers + " ON (" + aliasMembre + "." + SFMembreFamille.FIELD_IDTIERS + " = " + aliasTiPers + "."
                + SFMembreFamille.FIELD_PERS_IDTIERS + ")"

                + " LEFT JOIN " + schema + SFMembreFamille.TABLE_AVS + " AS " + aliasTiAvs + " ON (" + aliasMembre
                + "." + SFMembreFamille.FIELD_IDTIERS + " = " + aliasTiAvs + "." + SFMembreFamille.FIELD_AVS_IDTIERS
                + ")";

        return createJoin;
    }

    /**
     * @return Le Cs du canton de domicile ou le Cs du canton de la caisse (si vide)
     */
    @Override
    public String getCsCantonDomicile() {
        if (JadeStringUtil.isEmpty(csCantonDomicile)) {
            try {
                csCantonDomicile = CaisseHelperFactory.getInstance().getCsDefaultCantonCaisse(
                        getSession().getApplication());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return csCantonDomicile;
    }

    public String getCsDomaineApplication() {
        return csDomaineApplication;
    }

    @Override
    public String getCsEtatCivil() {
        return getEtatCivil(null);
    }

    @Override
    public String getCsNationalite() {
        return csNationalite;
    }

    @Override
    public String getCsSexe() {
        return csSexe;
    }

    @Override
    public String getDateDeces() {
        return dateDeces;
    }

    @Override
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * Renvoie l'état civil d'une personne à une date donnée
     * 
     * @param date
     *            au format "jj.mm.aaaa", peut être null et le dernier état civil sera retourné,
     * @return code system: ISFSituationFamiliale.CS_ETAT_CIVIL_... ou null si non-définissable ou en cas d'erreur
     */
    public String getEtatCivil(String date) {
        /*
         * L'état civil d'un membre se calcul ainsi: - On recherche toutes les relation conjugales (excepté indéfinie et
         * enfant commun) et on recupère la plus ancienne relation (si une date est donnée, on récupère la relation dont
         * la date de début et la plus ancienne avant la date)
         * 
         * - si aucune relation n'existe et que le membre est un requérent alors il est célibataire, s'il n'est pas
         * requérant alors on ne peut rien dire sur son état civil
         * 
         * - Si la dernière relation est divorcé alors il est divorcé (ou partenariat dissous judiciairement si
         * personnes du meme sexe) - Si la dernière relation est séparé (de droit ou de fait) alors il est séparé (ou
         * partenariat dissous judiciairement si personnes du meme sexe) - Si la dernière relation est un mariage on
         * vérifie s'il n'est pas veuf - si le conjoint a une date de décès et aucune date n'est donnée en paramètre,
         * alors il est décédé - si le conjoint a une date de décès mais que celle-ci soit après (>)la date donnée, il
         * est marié, (ou partenariat enregistre si personnes du meme sexe) - sinon il est veuf (ou partenariat dissous
         * par deces si personnes du meme sexe)
         * 
         * Remarque: On ne vérifie pas si le membre est mort, on retourne sa dernière relation
         */
        if (isNew()) {
            return null;
        }

        SFApercuRelationConjointManager relMgr = new SFApercuRelationConjointManager();
        relMgr.setSession(getSession());
        relMgr.setForIdConjoint(getIdMembreFamille());
        relMgr.setUntilDateDebut(date);
        relMgr.setNoWantEnfantCommun(true);
        relMgr.setNoWantRelationIndefinie(true);
        relMgr.setOrderByDateDebutDsc(true);
        try {
            relMgr.find();
        } catch (Exception e) {
            return null;
        }

        // A-t-il des relations ?
        if (!relMgr.isEmpty()) {
            SFApercuRelationConjoint relation = (SFApercuRelationConjoint) relMgr.getFirstEntity();
            if (ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(relation.getTypeRelation())) {
                // Vérifie s'il est pas veuf
                String dateDecesConjoint = relation.getDateDecesConjoint(getIdMembreFamille());
                if (JAUtil.isDateEmpty(dateDecesConjoint)) {
                    // Si le conjoint n'est pas mort, alors ils sont mariés ou
                    // partenariat enregistre

                    if (isPartenariatEntrePersonneDuMemeSexe(relation.getIdConjoint1(), relation.getIdConjoint2())) {
                        return ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_ENREGISTRE;
                    } else {
                        return ISFSituationFamiliale.CS_ETAT_CIVIL_MARIE;
                    }

                } else {
                    // il est mort
                    if (JAUtil.isDateEmpty(date)) {
                        // et aucune date n'est fournie, il est veuf ou
                        // partebariat dissous par deces
                        if (isPartenariatEntrePersonneDuMemeSexe(relation.getIdConjoint1(), relation.getIdConjoint2())) {
                            return ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_DISSOUS_DECES;
                        } else {
                            return ISFSituationFamiliale.CS_ETAT_CIVIL_VEUF;
                        }
                    } else { // une date est donnée
                        try {
                            // mort avant la date donnée
                            if (BSessionUtil.compareDateFirstLower(getSession(), date, dateDecesConjoint)) {
                                if (isPartenariatEntrePersonneDuMemeSexe(relation.getIdConjoint1(),
                                        relation.getIdConjoint2())) {
                                    return ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_ENREGISTRE;
                                } else {
                                    return ISFSituationFamiliale.CS_ETAT_CIVIL_MARIE;
                                }
                            } else {

                                if (isPartenariatEntrePersonneDuMemeSexe(relation.getIdConjoint1(),
                                        relation.getIdConjoint2())) {
                                    return ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_DISSOUS_DECES;
                                } else {
                                    return ISFSituationFamiliale.CS_ETAT_CIVIL_VEUF;
                                }
                            }
                        } catch (Exception e) {
                            return null;
                        }
                    }
                }
            } else if (ISFSituationFamiliale.CS_REL_CONJ_DIVORCE.equals(relation.getTypeRelation())) {
                if (isPartenariatEntrePersonneDuMemeSexe(relation.getIdConjoint1(), relation.getIdConjoint2())) {
                    return ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_DISSOUS_JUDICIAIREMENT;
                } else {
                    return ISFSituationFamiliale.CS_ETAT_CIVIL_DIVORCE;
                }
            }

            else if (ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(relation.getTypeRelation())) {
                if (isPartenariatEntrePersonneDuMemeSexe(relation.getIdConjoint1(), relation.getIdConjoint2())) {
                    return ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_SEPARE_DE_FAIT;
                } else {
                    return ISFSituationFamiliale.CS_ETAT_CIVIL_SEPARE_DE_FAIT;
                }
            } else if (ISFSituationFamiliale.CS_REL_CONJ_SEPARE_JUDICIAIREMENT.equals(relation.getTypeRelation())) {
                if (isPartenariatEntrePersonneDuMemeSexe(relation.getIdConjoint1(), relation.getIdConjoint2())) {
                    return ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_SEPARE_JUDICIAIREMENT;
                } else {
                    return ISFSituationFamiliale.CS_ETAT_CIVIL_SEPARE_JUDICIAIREMENT;
                }
            } else {
                return null;
            }

        } else {
            // Est-ce un requerant ?
            int nbReq = 0;
            SFApercuRequerantManager reqMgr = new SFApercuRequerantManager();
            reqMgr.setSession(getSession());
            reqMgr.setForIdMembreFamille(getIdMembreFamille());
            try {
                nbReq = reqMgr.getCount();
            } catch (Exception e1) {
                return null;
            }
            if (nbReq > 0) {
                // C'est un requerant, il est célibataire
                return ISFSituationFamiliale.CS_ETAT_CIVIL_CELIBATAIRE;
            } else {
                // C'est n'est pas un requerant, on ne peut pas définir son état
                // civil
                return null;
            }
        }
    }

    @Override
    public String getIdAssure() {
        return idAssure;
    }

    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    public String getIdTiers() {
        if (JadeStringUtil.isEmpty(idTiers) && SFUtil.PROVENANCE_TIERS.equals(provenance)) {
            idTiers = idAssure;
        }
        return idTiers;
    }

    public String getLibellePays() {
        return getSession().getCodeLibelle(
                getSession().getSystemCode("CIPAYORI", JadeStringUtil.isEmpty(csNationalite) ? pays : csNationalite));
    }

    public String getLibelleSexe() {
        return getSession().getCodeLibelle(csSexe);
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public String getNss() {
        return nss;

    }

    public String getNSS() {
        return nss;
    }

    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNss(), isNNSS().equals("true") ? true : false);
    }

    /**
     * Retourne le pays de domicile
     * 
     * @return
     */
    public String getPays() {
        return pays;
    }

    @Override
    public String getPrenom() {
        return prenom;
    }

    @Override
    public String getProvenance() {
        return provenance;
    }

    /**
     * @param string
     * @return Renvoie le type de relation du membre de famille avec le requerant CodeSystem (enfant/conjoint) ou null
     *         s'ils n'ont pas de relation ou en cas d'erreur ISFSituationFamiliale.CS_TYPE_RELATION_...
     */
    public String getRelationAuMembre(String idMembre) {
        /* Vérifie si c'est un conjoint du membre */
        if (isEnfant(null, idMembre)) {
            return ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT;
        } else if (isConjoint(null, idMembre)) {
            return ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT;
        } else {
            return null;
        }

    }

    /**
     * ATTENTION: La relation n'est jamais calculée, mais retourne uniquement la valeur settée
     */
    public String getRelationAuRequerant() {
        return relationAuRequerant;
    }

    /**
     * @param string
     * @return Renvoie le type de relation du membre de famille avec le requerant CodeSystem (enfant/conjoint) ou null
     *         si relation indefinie ISFSituationFamiliale.CS_TYPE_RELATION_...
     */
    public String getRelationAuRequerant(String idRequerant) {
        // Recherche l'idMembre du requerant
        SFApercuRequerant req = new SFApercuRequerant();
        req.setSession(getSession());
        req.setIdRequerant(idRequerant);
        try {
            req.retrieve();
        } catch (Exception e) {
            return null;
        }
        String idMembreReq = req.getIdMembreFamille();

        return getRelationAuMembre(idMembreReq);
    }

    public Vector getTiPays() throws Exception {
        return SFTiersHelper.getPays(getSession());
    }

    /**
     * Retourne la liste de tous les pays sans la Suisse et avec un élément vide au départ
     * 
     * @return la liste de tous les pays SANS la Suisse et avec un élément vide au départ
     * @throws Exception
     */
    public Vector getPaysDomicile() throws Exception {
        Vector<String[]> allPays = getTiPays();
        Vector<String[]> pays = new Vector<String[]>();
        pays.add(new String[] { "", "" });
        for (String[] strings : allPays) {
            if (strings[0] != null && !strings[0].equals("100")) {
                pays.add(strings);
            }
        }
        return pays;
    }

    public String getVisibleNoAvs() {
        return JAStringFormatter.unFormatAVS(JadeStringUtil.isEmpty(nss) ? "00000000000" : nss, "");
    }

    /**
     * Determine si le MembreFamille donné en parametre est un parent du membre (this)
     * 
     * @param transaction
     *            peut être null
     * @param conjoint
     * @return false en cas d'erreur
     */
    public boolean isConjoint(BITransaction transaction, String idMembreFamilleConjoint) {
        String idConjoint1 = idMembreFamilleConjoint;
        String idConjoint2 = getIdMembreFamille();

        // Vérifie si le membre est aussi un enfant
        SFConjointManager conjointManager = new SFConjointManager();
        conjointManager.setSession(getSession());
        conjointManager.setForIdsConjoints(idConjoint1, idConjoint2);
        try {
            conjointManager.find(transaction);
        } catch (Exception e) {
            return false;
        }
        if (conjointManager.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Determine si le MembreFamille donné en parametre est un parent du membre (this)
     * 
     * @param transaction
     *            peut être null
     * @param parent
     * @return false en cas d'erreur
     */
    public boolean isEnfant(BITransaction transaction, String idMembreFamilleParent) {

        // Vérifie si le membre est aussi un enfant
        SFEnfant enfant = new SFEnfant();
        enfant.setSession(getSession());
        enfant.setAlternateKey(SFEnfant.ALTERNATE_KEY_IDMEMBREFAMILLE);
        enfant.setIdMembreFamille(getIdMembreFamille());
        try {
            enfant.retrieve(transaction);
        } catch (Exception e) {
            return false;
        }
        if (enfant.isNew()) {
            return false;
        }

        // Recherche les parents de l'enfant
        SFConjoint conjoints = enfant.getConjoints(transaction);

        return idMembreFamilleParent.equals(conjoints.getIdConjoint1())
                || idMembreFamilleParent.equals(conjoints.getIdConjoint2());
    }

    public String isNNSS() {

        if (JadeStringUtil.isBlankOrZero(getNss())) {
            return "";
        }
        if (getNss().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    private boolean isPartenariatEntrePersonneDuMemeSexe(String idMF1, String idMF2) {

        try {
            SFMembreFamille mf1 = new SFMembreFamille();
            mf1.setSession(getSession());
            mf1.setIdMembreFamille(idMF1);
            mf1.retrieve();

            SFMembreFamille mf2 = new SFMembreFamille();
            mf2.setSession(getSession());
            mf2.setIdMembreFamille(idMF2);
            mf2.retrieve();

            String sexeMF1 = mf1.getCsSexe();
            String sexeMF2 = mf2.getCsSexe();

            return !JadeStringUtil.isIntegerEmpty(sexeMF1) && !JadeStringUtil.isIntegerEmpty(sexeMF2)
                    && sexeMF1.equals(sexeMF2);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void setCsCantonDomicile(String string) {
        csCantonDomicile = string;
    }

    public void setCsDomaineApplication(String csDomaineApplication) {
        this.csDomaineApplication = csDomaineApplication;
    }

    @Override
    public void setCsEtatCivil(String s) {
    }

    @Override
    public void setCsNationalite(String string) {
        csNationalite = string;
    }

    @Override
    public void setCsSexe(String sex) {
        csSexe = sex;
    }

    @Override
    public void setDateDeces(String string) {
        dateDeces = string;
    }

    @Override
    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    @Override
    public void setIdAssure(String string) {
        idAssure = string;
        if (SFUtil.PROVENANCE_TIERS.equals(getProvenance())) {
            idTiers = string;
        }
    }

    public void setIdMembreFamille(String idMembreFamille) {
        this.idMembreFamille = idMembreFamille;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    @Override
    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public void setNss(String string) {
        nss = string;
    }

    /**
     * Set le pays de domicile
     * 
     * @param string
     */
    public void setPays(String string) {
        pays = string;
    }

    @Override
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Override
    public void setProvenance(String string) {
        provenance = string;
    }

    public String setRelationAuRequerant(String rel) {
        return relationAuRequerant = rel;
    }
}