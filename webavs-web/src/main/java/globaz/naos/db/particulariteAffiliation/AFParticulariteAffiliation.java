package globaz.naos.db.particulariteAffiliation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.helper.IAFParticulariteAffiliationHelper;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TITiers;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * La classe définissant l'entité ParticulariteAffiliation.
 * 
 * @author administrator
 */
public class AFParticulariteAffiliation extends BEntity {

    private static final long serialVersionUID = 3705816441707194759L;
    public final static String CS_1ERE_SOMMATION = CodeSystem.PARTIC_AFFILIE_1_SOMMATION;
    public final static String CS_2EME_SOMMATION = CodeSystem.PARTIC_AFFILIE_2_SOMMATION;
    public final static String CS_2EME_SOMMATION_TAXATION = CodeSystem.PARTIC_AFFILIE_2_SOMMATION_TAXATION;
    public final static String CS_ACTIVITE_ACCESSOIRE = CodeSystem.PARTIC_AFFILIE_ACTI_ACCESS;
    public final static String CS_BAREME_DEGRESSIF = CodeSystem.PARTIC_AFFILIE_BAREME_DEGRESSIF;
    public final static String CS_EXEMPTION_DE_COTISATIONS = CodeSystem.PARTIC_AFFILIE_EXEMPT_COTI;
    public final static String CS_FORCER_CALCUL_TSE = CodeSystem.PARTIC_AFFILIE_FORCER_TSE;
    // code systemes
    public final static String CS_SURSIS_AU_PAIEMENT_DE_COTISATION = CodeSystem.PARTIC_AFFILIE_SURSIS;

    /**
     * Revoie true si le genre de particularité transmis existe à une date donnée
     * 
     * @param session
     *            , idAffiliation, genreParticularite, date
     * @return boolean
     */
    public static boolean existeParticularite(BSession session, String idAffiliation, String genreParticularite,
            String dateRef) throws Exception {

        // le début de la période = 31.12.(annéePériode-1)
        int anneeDebutPeriode = Integer.parseInt(dateRef.substring(6));
        String dateDebutPeriode = "01.01." + anneeDebutPeriode;
        String dateFinPeriode = "31.12." + anneeDebutPeriode;
        // Recherche des particularite de l'affiliation qui ont le genre reçu en
        // paramètre
        AFParticulariteAffiliationManager particulariteManager = new AFParticulariteAffiliationManager();
        particulariteManager.setForAffiliationId(idAffiliation);
        particulariteManager.setForParticularite(genreParticularite);
        particulariteManager.setSession(session);
        particulariteManager.find();
        for (int i = 0; i < particulariteManager.size(); i++) {
            AFParticulariteAffiliation particularite = (AFParticulariteAffiliation) particulariteManager.getEntity(i);
            if (JAUtil.isDateEmpty(particularite.getDateFin())) {
                if (BSessionUtil.compareDateFirstLowerOrEqual(session, particularite.getDateDebut(), dateFinPeriode)) {
                    return true;
                }
            } else {
                if (BSessionUtil.compareDateBetweenOrEqual(session, particularite.getDateDebut(),
                        particularite.getDateFin(), dateDebutPeriode)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Revoie true si le genre de particularité transmis existe et n'a pas de date de fin
     * 
     * @param session ,idAffiliation, genreParticularite
     * @return boolean
     */
    public static boolean existeParticulariteActive(BSession session, String idAffiliation, String genreParticularite)
            throws Exception {

        // Recherche des particularite de l'affiliation qui ont le genre
        // reçu en paramètre
        AFParticulariteAffiliationManager particulariteManager = new AFParticulariteAffiliationManager();
        particulariteManager.setForAffiliationId(idAffiliation);
        particulariteManager.setForParticularite(genreParticularite);
        particulariteManager.setSession(session);
        particulariteManager.find();
        for (int i = 0; i < particulariteManager.size(); i++) {
            AFParticulariteAffiliation particularite = (AFParticulariteAffiliation) particulariteManager.getEntity(i);
            if (JAUtil.isDateEmpty(particularite.getDateFin())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Revoie true si le genre de particularité transmis existe
     * 
     * @param session ,idAffiliation, genreParticularite
     * @return boolean
     */
    public static boolean existeParticularite(BSession session, String idAffiliation, String genreParticularite)
            throws Exception {

        // Recherche des particularite de l'affiliation qui ont le genre
        // reçu en paramètre
        AFParticulariteAffiliationManager particulariteManager = new AFParticulariteAffiliationManager();
        particulariteManager.setForAffiliationId(idAffiliation);
        particulariteManager.setForParticularite(genreParticularite);
        particulariteManager.setSession(session);
        particulariteManager.find();
        if (particulariteManager.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * Remonte toutes les particularités donnée en paramètre pour des idAffiliation
     * 
     * @param session ,idAffiliation, genreParticularite
     * @return Une map avec comme id l'idAffiliation
     */
    public static Map<String, List<String>> findParticularites(BSession session, Collection<String> idsAffiliation,
            String... genreParticularite) {

        Map<String, List<String>> map = new HashMap<String, List<String>>();

        AFParticulariteAffiliationManager particulariteManager = new AFParticulariteAffiliationManager();
        particulariteManager.setInAffiliationIds(idsAffiliation);
        particulariteManager.setInParticularites(Arrays.asList(genreParticularite));
        particulariteManager.setSession(session);
        try {
            particulariteManager.find(BManager.SIZE_NOLIMIT);
            List<AFParticulariteAffiliation> entities = particulariteManager.toList();

            for (AFParticulariteAffiliation par : entities) {
                if (!map.containsKey(par.getAffiliationId())) {
                    map.put(par.getAffiliationId(), new ArrayList<String>());
                }
                map.get(par.getAffiliationId()).add(par.getParticularite());
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Revoie true si le genre de particularité transmis existe à une date donnée
     * 
     * @param transaction
     *            , idAffiliation, genreParticularite, date
     * @return boolean
     */
    public static boolean existeParticularite(BTransaction transaction, String idAffiliation,
            String genreParticularite, String dateRef) throws Exception {
        if (transaction != null) {
            // Recherche des particularite de l'affiliation qui ont le genre
            // reçu en paramètre
            AFParticulariteAffiliationManager particulariteManager = new AFParticulariteAffiliationManager();
            particulariteManager.setForAffiliationId(idAffiliation);
            particulariteManager.setForParticularite(genreParticularite);
            particulariteManager.setSession(transaction.getSession());
            particulariteManager.find(transaction);
            for (int i = 0; i < particulariteManager.size(); i++) {
                AFParticulariteAffiliation particularite = (AFParticulariteAffiliation) particulariteManager
                        .getEntity(i);
                if (JAUtil.isDateEmpty(particularite.getDateFin())) {
                    if (BSessionUtil.compareDateFirstLowerOrEqual(transaction.getSession(),
                            particularite.getDateDebut(), dateRef)) {
                        return true;
                    }
                } else {
                    if (BSessionUtil.compareDateBetweenOrEqual(transaction.getSession(), particularite.getDateDebut(),
                            particularite.getDateFin(), dateRef)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Revoie true si le genre de particularité transmis existe à une date donnée
     * 
     * @param transaction
     *            , idAffiliation, genreParticularite, date
     * @return boolean
     */
    public static boolean existeParticulariteDateDonnee(BSession session, String idAffiliation,
            String genreParticularite, String dateRef) throws Exception {
        if (AFParticulariteAffiliation.returnNbParticularitePeriode(session, idAffiliation, genreParticularite,
                dateRef, dateRef, "") > 0) {
            return true;
        }
        return false;
    }

    /**
     * Revoie true si le genre de particularité transmis existe à une date donnée
     * 
     * @param transaction
     *            , idAffiliation, genreParticularite, date
     * @return boolean
     */
    public static boolean existeParticulariteOrganesExternes(BTransaction transaction, String idAffiliation,
            String dateRef) throws Exception {
        // Recherche des particularite de l'affiliation qui ont le genre reçu en
        // paramètre
        if (AFParticulariteAffiliation.existeParticulariteOrganesExternes(transaction, idAffiliation, dateRef, dateRef,
                "") == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Revoie true si le genre de particularité transmis existe à une date donnée
     * 
     * @param transaction
     *            , idAffiliation, genreParticularite, date
     * @return boolean
     */
    public static AFParticulariteAffiliation existeParticulariteOrganesExternes(BTransaction transaction,
            String idAffiliation, String dateDeb, String dateFin, String particulariteIdlue) throws Exception {
        // Recherche des particularite de l'affiliation qui ont le genre reçu en
        // paramètre
        AFParticulariteAffiliationManager particulariteManager = new AFParticulariteAffiliationManager();
        particulariteManager.setForAffiliationId(idAffiliation);
        particulariteManager.setSession(transaction.getSession());
        particulariteManager.setExceptParaticulariteId(particulariteIdlue);
        particulariteManager.find(transaction);
        for (int i = 0; i < particulariteManager.getSize(); i++) {
            AFParticulariteAffiliation particularite = (AFParticulariteAffiliation) particulariteManager.getEntity(i);
            if (AFParticulariteAffiliation.particulariteOrganeExterne(particularite.getParticularite())) {
                if (JadeStringUtil.isBlankOrZero(dateFin)) {
                    if (JadeStringUtil.isBlankOrZero(particularite.getDateFin())
                            || BSessionUtil.compareDateBetweenOrEqual(transaction.getSession(),
                                    particularite.getDateDebut(), particularite.getDateFin(), dateDeb)) {
                        return particularite;
                    }
                } else {
                    // Si date saisie non vide
                    if (JadeStringUtil.isBlankOrZero(particularite.getDateFin())) {
                        if (BSessionUtil.compareDateFirstGreaterOrEqual(transaction.getSession(), dateFin,
                                particularite.getDateDebut())) {
                            return particularite;
                        }
                    } else if (BSessionUtil.compareDateBetweenOrEqual(transaction.getSession(),
                            particularite.getDateDebut(), particularite.getDateFin(), dateFin)
                            || BSessionUtil.compareDateBetweenOrEqual(transaction.getSession(),
                                    particularite.getDateDebut(), particularite.getDateFin(), dateDeb)) {
                        return particularite;
                    }
                }
            }
        }
        return null;
    }

    // private FWParametersSystemCode csParticularite = null;

    /**
     * Revoie true si le genre de particularité transmis existe à une date donnée
     * 
     * @param transaction
     *            , idAffiliation, genreParticularite, date
     * @return boolean
     */
    public static String existeParticulariteOrganesExternesId(BTransaction transaction, String idAffiliation,
            String dateRef) throws Exception {
        String idTiers = "";

        // Recherche des particularite de l'affiliation qui ont le genre reçu en
        // paramètre
        AFParticulariteAffiliationManager particulariteManager = new AFParticulariteAffiliationManager();
        particulariteManager.setForAffiliationId(idAffiliation);
        particulariteManager.setSession(transaction.getSession());
        particulariteManager.setDateDebutLessOrEqual(dateRef);
        particulariteManager.setDateFinGreatOrEqual(dateRef);
        particulariteManager.find(transaction);
        if (particulariteManager.getSize() > 0) {
            AFParticulariteAffiliation particularite = (AFParticulariteAffiliation) particulariteManager
                    .getFirstEntity();
            if (particularite.getParticularite().equals(CodeSystem.PARTIC_AFFILIE_REFUGIE)) {
                return idTiers = AFParticulariteAffiliation.findParameterNaos(transaction,
                        CodeSystem.PARTIC_AFFILIE_REFUGIE, "TIERS1");
            }
            if (particularite.getParticularite().equals(CodeSystem.PARTIC_AFFILIE_NON_ACTIF_BENEFICIAIRE)) {
                return idTiers = AFParticulariteAffiliation.findParameterNaos(transaction,
                        CodeSystem.PARTIC_AFFILIE_NON_ACTIF_BENEFICIAIRE, "TIERS3");
            }
            if (particularite.getParticularite().equals(CodeSystem.PARTIC_AFFILIE_NON_ACTIF_ASSISSTE)) {
                return idTiers = AFParticulariteAffiliation.findParameterNaos(transaction,
                        CodeSystem.PARTIC_AFFILIE_NON_ACTIF_ASSISSTE, "TIERS2");
            }
            if (particularite.getParticularite().equals(CodeSystem.PARTIC_AFFILIE_NON_ACTIF_BENEFICIAIRE_FAMILLE)) {
                return idTiers = AFParticulariteAffiliation.findParameterNaos(transaction,
                        CodeSystem.PARTIC_AFFILIE_NON_ACTIF_BENEFICIAIRE_FAMILLE, "TIERS4");
            }
            return idTiers;
        }
        return "";

    }

    /**
     * Retourne le nombre de particularité pour una affiliation pour une période Tient compte du fait que la date de fin
     * peut être vide
     * 
     * @param session
     * @param idAffiliation
     * @param genreParticularite
     * @param dateDebut
     * @param dateFin
     * @return
     * @throws Exception
     */
    public static int returnNbParticularitePeriode(BSession session, String idAffiliation, String genreParticularite,
            String dateDebut, String dateFin, String particulariteIdEncours) throws Exception {

        AFParticulariteAffiliationManager particulariteManager = new AFParticulariteAffiliationManager();
        particulariteManager.setForAffiliationId(idAffiliation);
        particulariteManager.setForParticularite(genreParticularite);
        particulariteManager.setSession(session);
        particulariteManager.setExceptParaticulariteId(particulariteIdEncours);
        particulariteManager.find();
        int nb = 0;
        for (int i = 0; i < particulariteManager.getSize(); i++) {
            AFParticulariteAffiliation particularite = (AFParticulariteAffiliation) particulariteManager.getEntity(i);
            // Si date saisie est vide => contrôle existance particularité avec date vide ou date de début comprise dans
            // une particualrité
            if (JadeStringUtil.isBlankOrZero(dateFin)) {
                if (JadeStringUtil.isBlankOrZero(particularite.getDateFin())
                        || BSessionUtil.compareDateBetweenOrEqual(session, particularite.getDateDebut(),
                                particularite.getDateFin(), dateDebut)) {
                    nb++;
                }
            } else {
                // Si date saisie non vide
                if (JadeStringUtil.isBlankOrZero(particularite.getDateFin())) {
                    if (BSessionUtil.compareDateFirstGreaterOrEqual(session, dateFin, particularite.getDateDebut())) {
                        nb++;
                    }
                } else if (BSessionUtil.compareDateBetweenOrEqual(session, particularite.getDateDebut(),
                        particularite.getDateFin(), dateFin)
                        || BSessionUtil.compareDateBetweenOrEqual(session, particularite.getDateDebut(),
                                particularite.getDateFin(), dateDebut)) {
                    nb++;
                }
            }
        }
        return nb;
    }

    public static String findParameterNaos(BTransaction transaction, String idCodeSysteme, String idCleDifferre)
            throws Exception {
        String paramAlpha = "";
        FWFindParameterManager param = new FWFindParameterManager();
        param.setSession(transaction.getSession());
        param.setIdCodeSysteme(idCodeSysteme);
        param.setIdCleDiffere(idCleDifferre);
        param.setIdApplParametre("NAOS");
        param.setIdActeurParametre("0");
        param.setPlageValDeParametre("0");
        param.find();
        if (param.getSize() > 0) {
            FWFindParameter parametre = (FWFindParameter) param.getEntity(param.getSize() - 1);
            paramAlpha = parametre.getValeurAlphaParametre();
        }
        return paramAlpha;
    }

    protected static boolean particulariteOrganeExterne(String particularite) {
        return (particularite.equals(CodeSystem.PARTIC_AFFILIE_REFUGIE))
                || (particularite.equals(CodeSystem.PARTIC_AFFILIE_NON_ACTIF_BENEFICIAIRE))
                || (particularite.equals(CodeSystem.PARTIC_AFFILIE_NON_ACTIF_ASSISSTE))
                || (particularite.equals(CodeSystem.PARTIC_AFFILIE_NON_ACTIF_BENEFICIAIRE_FAMILLE));
    }

    private AFAffiliation _affiliation = null;
    private TITiers _tiers = null;
    // Foreign key
    private java.lang.String affiliationId = new String();
    private java.lang.String champAlphanumerique = new String();

    private java.lang.String champNumerique = new String();

    // Fields
    private java.lang.String dateDebut = new String();

    private java.lang.String dateFin = new String();

    private java.lang.String particularite = new String();

    // DB
    // Primary Key
    private java.lang.String particulariteId = new String();

    private java.lang.String selection = new String();

    /**
     * Constructeur d'AFParticulariteAffiliation.
     */
    public AFParticulariteAffiliation() {
        super();
        setMethodsToLoad(IAFParticulariteAffiliationHelper.METHODS_TO_LOAD);
    }

    /**
     * Effectue des traitements après un ajout dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_afterAdd(BTransaction)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) {
    }

    /**
     * Effectue des traitements après une lecture dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_afterRetrieveWithResultSet(globaz.globall.db.BStatement)
     */
    @Override
    protected void _afterRetrieveWithResultSet(BStatement statement) throws Exception {
    }

    /**
     * Effectue des traitements avant un ajout dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // incrémente de +1 le numéro
        setParticulariteId(this._incCounter(transaction, "0"));
    }

    /**
     * Effectue des traitements avant une mise à jour dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "AFPARTP";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        particulariteId = statement.dbReadNumeric("MFIPAR");
        affiliationId = statement.dbReadNumeric("MAIAFF");
        particularite = statement.dbReadNumeric("MFTPAR");
        dateDebut = statement.dbReadDateAMJ("MFDDEB");
        dateFin = statement.dbReadDateAMJ("MFDFIN");
        champNumerique = statement.dbReadNumeric("MFMNUM");
        champAlphanumerique = statement.dbReadString("MFLALP");
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        boolean validationOK = true;

        validationOK &= _propertyMandatory(statement.getTransaction(), getDateDebut(), getSession().getLabel("20"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getAffiliationId(),
                getSession().getLabel("AFFILIATION_ID_ERREUR"));
        // Test validité des dates
        validationOK &= _checkRealDate(statement.getTransaction(), getDateDebut(), getSession().getLabel("160"));
        if (validationOK) {
            // *******************************************************************
            // Date Debut
            // *******************************************************************
            // Contrôle que la date de début > 01.01.1948
            String dateLimiteInf = "01.01.1900";
            // Test Date Debut >= Date Limite Inferieur
            if (BSessionUtil.compareDateFirstLower(getSession(), getDateDebut(), dateLimiteInf)) {
                _addError(statement.getTransaction(), getSession().getLabel("60"));
                validationOK = false;
            }

            // *******************************************************************
            // Date Fin
            // *******************************************************************

            if (validationOK && !JadeStringUtil.isBlankOrZero(getDateFin())) {
                // Validité Date
                validationOK &= _checkRealDate(statement.getTransaction(), getDateFin(), getSession().getLabel("180"));
                // Date Fin > Date Debut
                if (validationOK
                        && !BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDebut(), getDateFin())) {
                    _addError(statement.getTransaction(), getSession().getLabel("90"));
                    validationOK = false;
                }
            }
        }
        // PO 3352 - NE pas avoir RI et PC pour la même période
        // test si la particularité n'est pas déjà présente
        if (AFParticulariteAffiliation.particulariteOrganeExterne(getParticularite())) {
            if (AFParticulariteAffiliation.returnNbParticularitePeriode(getSession(), getAffiliationId(),
                    getParticularite(), getDateDebut(), getDateFin(), getParticulariteId()) > 0) {
                _addError(statement.getTransaction(), getSession().getLabel("PLAUSI_PARTICULARITE_EXISTANTE"));
            } else {
                AFParticulariteAffiliation particulariteOrganesExterne = AFParticulariteAffiliation
                        .existeParticulariteOrganesExternes(statement.getTransaction(), getAffiliationId(),
                                getDateDebut(), getDateFin(), getParticulariteId());
                if (particulariteOrganesExterne != null) {
                    String libError = getSession().getLabel("PLAUSI_PARTICULARITE_ORGANES_EXTERNE")
                            + getSession().getCodeLibelle(particulariteOrganesExterne.getParticularite());
                    if ("DE".equalsIgnoreCase(getSession().getIdLangueISO().toUpperCase())) {
                        libError += " " + getSession().getLabel("PLAUSI_PARTICULARITE_ORGANES_EXTERNE1");
                    }
                    _addError(statement.getTransaction(), libError);

                }
            }
        }
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MFIPAR", this._dbWriteNumeric(statement.getTransaction(), getParticulariteId(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MFIPAR",
                this._dbWriteNumeric(statement.getTransaction(), getParticulariteId(), "ParticulariteId"));
        statement.writeField("MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getAffiliationId(), "AffiliationId"));
        statement.writeField("MFTPAR",
                this._dbWriteNumeric(statement.getTransaction(), getParticularite(), "Particularite"));
        statement.writeField("MFDDEB", this._dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "DateDebut"));
        statement.writeField("MFDFIN", this._dbWriteDateAMJ(statement.getTransaction(), getDateFin(), "DateFin"));
        statement.writeField("MFMNUM",
                this._dbWriteNumeric(statement.getTransaction(), getChampNumerique(), "ChampNumerique"));
        statement.writeField("MFLALP",
                this._dbWriteString(statement.getTransaction(), getChampAlphanumerique(), "ChampAlphanumerique"));
    }

    /**
     * Methode utilisée par les API.
     * 
     * @param params
     * @return
     * @throws Exception
     */
    public BManager find(Hashtable<?, ?> params) throws Exception {
        BManager manager = getManager();
        manager.setSession(getSession());
        if (params != null) {
            Enumeration<?> methods = params.keys();
            while (methods.hasMoreElements()) {
                String methodName = (String) methods.nextElement();
                String value = (String) params.get(methodName);
                Method m = manager.getClass().getMethod(methodName, new Class[] { String.class });
                if (m != null) {
                    m.invoke(manager, new Object[] { value });
                }
            }
        }
        manager.find();
        return manager;
    }

    /**
     * Rechercher l'Affiliation de la Particularité d'affiliation en fonction de son ID.
     * 
     * @return l'Affiliation
     */
    public AFAffiliation getAffiliation() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getAffiliationId())) {
            return null;
        }

        if (_affiliation == null) {

            _affiliation = new AFAffiliation();
            _affiliation.setSession(getSession());
            _affiliation.setAffiliationId(getAffiliationId());
            try {
                _affiliation.retrieve();
                /*
                 * if (_affiliation.hasErrors()) _affiliation = null;
                 */
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _affiliation = null;
            }
        }
        return _affiliation;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getAffiliationId() {
        return affiliationId;
    }

    public java.lang.String getChampAlphanumerique() {
        return champAlphanumerique;
    }

    public java.lang.String getChampNumerique() {
        return champNumerique;
    }

    public java.lang.String getDateDebut() {
        return dateDebut;
    }

    public java.lang.String getDateFin() {
        return dateFin;
    }

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    protected BManager getManager() {
        return new AFParticulariteAffiliationManager();
    }

    public java.lang.String getParticularite() {
        return particularite;
    }

    public java.lang.String getParticulariteId() {
        return particulariteId;
    }

    public java.lang.String getSelection() {
        return selection;
    }

    /**
     * Rechercher le tier de la Particularité d'affiliation en fonction de l'affiliation.
     * 
     * @return le tier
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
            _tiers.setSession(getSession());
            _tiers.setIdTiers(_affiliation.getIdTiers());
            try {
                _tiers.retrieve();
                /*
                 * if (_tiers.getSession().hasErrors()) _tiers = null;
                 */
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _tiers = null;
            }
        }
        return _tiers;
    }

    /*
     * public FWParametersSystemCode getCsParticularite() { if (csParticularite == null) { // liste pas encore chargée,
     * on la charge csParticularite = new FWParametersSystemCode(); csParticularite.getCode(getParticularite()); }
     * return csParticularite; }
     */

    // *******************************************************
    // Setter
    // *******************************************************

    public void setAffiliationId(java.lang.String newAffiliationId) {
        affiliationId = newAffiliationId;
    }

    public void setChampAlphanumerique(java.lang.String newChampAlphanumerique) {
        champAlphanumerique = newChampAlphanumerique;
    }

    public void setChampNumerique(java.lang.String newChampNumerique) {
        champNumerique = newChampNumerique;
    }

    public void setDateDebut(java.lang.String newDateDebut) {
        dateDebut = newDateDebut;
    }

    public void setDateFin(java.lang.String newDateFin) {
        dateFin = newDateFin;
    }

    public void setParticularite(java.lang.String newParticularite) {
        particularite = newParticularite;
    }

    public void setParticulariteId(java.lang.String newParticulariteId) {
        particulariteId = newParticulariteId;
    }

    public void setSelection(java.lang.String newSelection) {
        selection = newSelection;
    }

    public String getParticulariteLibelle() {
        return getSession().getCodeLibelle(particularite);
    }

}
