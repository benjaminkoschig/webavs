package globaz.prestation.interfaces.tiers;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.translation.FWTranslation;
import globaz.globall.api.BIEntity;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersCodeManager;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.shared.GlobazValueObject;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.naos.api.IAFAffiliation;
import globaz.osiris.external.IntRole;
import globaz.prestation.enums.CommunePolitique;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRSession;
import globaz.pyxis.adresse.formater.ITIAdresseFormater;
import globaz.pyxis.adresse.formater.TIAdresseFormater;
import globaz.pyxis.api.ITIAbstractAdresseData;
import globaz.pyxis.api.ITIAdministration;
import globaz.pyxis.api.ITIApplication;
import globaz.pyxis.api.ITIAvoirAdresse;
import globaz.pyxis.api.ITILocalite;
import globaz.pyxis.api.ITIPays;
import globaz.pyxis.api.ITIPersonne;
import globaz.pyxis.api.ITIPersonneAvs;
import globaz.pyxis.api.ITIPersonneAvsAdresse;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.api.ITITiersAdresse;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.adressecourrier.TIPaysManager;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementDataManager;
import globaz.pyxis.db.tiers.TIAdministrationAdresse;
import globaz.pyxis.db.tiers.TIAdministrationAdresseManager;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.pyxis.util.TIAdressePmtResolver;
import globaz.pyxis.util.TINSSFormater;

/**
 * Utilitaire pour accéder aux données des tiers depuis les modules des prestations.
 *
 * @author SCR
 */
public class PRTiersHelper {

    private static class PRAdressePmtKey {

        public String idApplication = "";
        public String idExterne = "";
        public String idTiersAdrPmt = "";

        public PRAdressePmtKey(String idApplication, String idExterne, String idTiersAdrPmt) {
            this.idApplication = idApplication;
            this.idExterne = idExterne;
            this.idTiersAdrPmt = idTiersAdrPmt;
        }

        @Override
        public int hashCode() {
            return toString().hashCode();
        }

        @Override
        public String toString() {
            StringBuilder toStringBuilder = new StringBuilder();
            toStringBuilder.append(PRAdressePmtKey.class.getName()).append("(");
            toStringBuilder.append(idApplication).append(",");
            toStringBuilder.append(idExterne).append(",");
            toStringBuilder.append(idTiersAdrPmt);
            toStringBuilder.append(")");
            return toStringBuilder.toString();
        }
    }

    public static final String AUCUN_TRIBUNAL_TROUVE = "Aucun tribunal trouvé pour ce canton";
    /** Table contenant tous les codes cantons de l'OFAS */
    private static Map<String, String> cantonOFAS = new HashMap<String, String>();
    private static Map<String, Map<String, String>> CANTONS_PAR_LANGUE = null;

    public static final String CS_ADRESSE_COURRIER = "508001";
    public static final String CS_ADRESSE_DOMICILE = "508008";
    /** Code système de Tiers pour la modification */
    public static final String CS_TIERS_MODIFICATION_INCONNUE = "506007";
    /** Cet idPays spécifie que le tiers est un étranger sans spécifier le pays */
    public static final String ID_PAYS_BIDON = "999";
    public static final String ID_PAYS_SUISSE = "100";
    private static final Map<String, Vector<String[]>> LISTE_PAYS = new HashMap<String, Vector<String[]>>();
    private static final String ID_CANTON_PROP = "default.canton.caisse.location";
    public static final String TYPE_DE_CAISSE = "TYPE_de_CAISSE";
    public static final String CAISSE_CANT = "Caisse_CANT";
    public static final String CAISSE_PROF = "Caisse_PROF";
    private static Map<String, Map<String, String>> PAYS_PAR_LANGUE = null;
    private static final Logger LOG = LoggerFactory.getLogger(PRTiersHelper.class);

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    static {
        PRTiersHelper.cantonOFAS.put("001", ""); // CS_ZURICH
        PRTiersHelper.cantonOFAS.put("002", ""); // CS_BERNE
        PRTiersHelper.cantonOFAS.put("003", ""); // CS_LUCERNE
        PRTiersHelper.cantonOFAS.put("004", ""); // CS_URI
        PRTiersHelper.cantonOFAS.put("005", ""); // CS_SCHWYZ
        PRTiersHelper.cantonOFAS.put("006", ""); // CS_OBWALD
        PRTiersHelper.cantonOFAS.put("007", ""); // CS_NIDWALD
        PRTiersHelper.cantonOFAS.put("008", ""); // CS_GLARIS
        PRTiersHelper.cantonOFAS.put("009", ""); // CS_ZOUG
        PRTiersHelper.cantonOFAS.put("010", ""); // CS_FRIBOURG
        PRTiersHelper.cantonOFAS.put("011", ""); // CS_SOLEURE
        PRTiersHelper.cantonOFAS.put("012", ""); // CS_BALE_VILLE
        PRTiersHelper.cantonOFAS.put("013", ""); // CS_BALE_CAMPAGNE
        PRTiersHelper.cantonOFAS.put("014", ""); // CS_SCHAFFOUSE
        PRTiersHelper.cantonOFAS.put("015", ""); // CS_APPENZELL_AR
        PRTiersHelper.cantonOFAS.put("016", ""); // CS_APPENZELL_AI
        PRTiersHelper.cantonOFAS.put("017", ""); // CS_SAINT_GALL
        PRTiersHelper.cantonOFAS.put("018", ""); // CS_GRISONS
        PRTiersHelper.cantonOFAS.put("019", ""); // CS_ARGOVIE
        PRTiersHelper.cantonOFAS.put("020", ""); // CS_THURGOVIE
        PRTiersHelper.cantonOFAS.put("021", ""); // CS_TESSIN
        PRTiersHelper.cantonOFAS.put("022", ""); // CS_VAUD
        PRTiersHelper.cantonOFAS.put("023", ""); // CS_VALAIS
        PRTiersHelper.cantonOFAS.put("024", ""); // CS_NEUCHATEL
        PRTiersHelper.cantonOFAS.put("025", ""); // CS_GENEVE
        PRTiersHelper.cantonOFAS.put("050", ""); // CS_JURA
    }

    private static void addRole(BISession session, BITransaction transaction, String idTiers) throws Exception {
        ITIRole role = (ITIRole) session.getAPIFor(ITIRole.class);
        role.setIdTiers(idTiers);
        role.setISession(PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        if ("IJ".equals(session.getApplicationId())) {
            role.setRole(IntRole.ROLE_IJAI);
        } else if ("APG".equals(session.getApplicationId())) {
            role.setRole(IntRole.ROLE_APG);
        }
        role.add(transaction);

    }

    public static final String addTiers(BISession session, BITransaction transaction, IPRTiers tiers, int truc)
            throws Exception {
        ITIPersonneAvs personneAvs = (ITIPersonneAvs) session.getAPIFor(ITIPersonneAvs.class);

        personneAvs.setTypeTiers(ITITiers.CS_TIERS);
        personneAvs.setNumAvsActuel(tiers.getNoAVS());
        personneAvs.setDesignation1(tiers.getNom());
        personneAvs.setDesignation2(tiers.getPrenom());
        personneAvs.setLangue(ITITiers.CS_FRANCAIS);

        if (!JadeStringUtil.isEmpty(tiers.getSexe())) {
            personneAvs.setSexe(tiers.getSexe());

            if (ITIPersonne.CS_FEMME.equals(tiers.getSexe())) {
                personneAvs.setTitreTiers(ITITiers.CS_MADAME);
            } else {
                personneAvs.setTitreTiers(ITITiers.CS_MONSIEUR);
            }
        } else {
            personneAvs.setSexe(ITIPersonne.CS_HOMME);
            personneAvs.setTitreTiers(ITITiers.CS_MONSIEUR);
        }

        personneAvs.setDateNaissance(tiers.getDateNaissance());
        personneAvs.setDateDeces(tiers.getDateDeces());
        personneAvs.setIdPays(tiers.getIdPays());

        personneAvs.setLangue(tiers.getLangue());

        personneAvs.setISession(PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));
        personneAvs.add(transaction);

        PRTiersHelper.addRole(session, transaction, personneAvs.getIdTiers());

        return personneAvs.getIdTiers();
    }

    public static final String addTiers(BISession session, String noAVS, String nom, String prenom, String sexe,
            String dateNaissance, String dateDeces, String pays, String canton, String langue, String etatCivil)
            throws Exception {
        ITIPersonneAvs personneAvs = (ITIPersonneAvs) session.getAPIFor(ITIPersonneAvs.class);

        personneAvs.setTypeTiers(ITITiers.CS_TIERS);
        personneAvs.setNumAvsActuel(noAVS);
        personneAvs.setDesignation1(nom);
        personneAvs.setDesignation2(prenom);
        personneAvs.setLangue(ITITiers.CS_FRANCAIS);
        if (!JadeStringUtil.isEmpty(sexe)) {
            personneAvs.setSexe(sexe);

            if (ITIPersonne.CS_FEMME.equals(sexe)) {
                personneAvs.setTitreTiers(ITITiers.CS_MADAME);
            } else {
                personneAvs.setTitreTiers(ITITiers.CS_MONSIEUR);
            }
        } else {
            personneAvs.setSexe(ITIPersonne.CS_HOMME);
            personneAvs.setTitreTiers(ITITiers.CS_MONSIEUR);
        }

        personneAvs.setDateNaissance(dateNaissance);
        personneAvs.setDateDeces(dateDeces);
        personneAvs.setIdPays(pays);

        // personneAvs.setIdCanton(canton);
        personneAvs.setLangue(langue);

        personneAvs.setEtatCivil(etatCivil);

        personneAvs.setPersonnePhysique(Boolean.TRUE);

        personneAvs.setISession(PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        if (((BSession) session).getCurrentThreadTransaction() != null) {
            personneAvs.add(((BSession) session).getCurrentThreadTransaction());
        } else {
            // HACK: création d'une transaction pour insérer un tiers
            BITransaction transaction = ((BSession) session).newTransaction();

            try {
                personneAvs.add(transaction);
                PRTiersHelper.addRole(session, transaction, personneAvs.getIdTiers());

            } catch (Exception e) {
                transaction.setRollbackOnly();
            } finally {
                if (transaction.isRollbackOnly()) {
                    transaction.closeTransaction();
                } else {
                    transaction.commit();
                }
            }
        }

        return personneAvs.getIdTiers();
    }

    public static final PRTiersWrapper[] getAdministrationActiveForGenre(BISession session, String genre)
            throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(genre)) {
            return null;
        }

        ITIAdministration administaration = (ITIAdministration) session.getAPIFor(ITIAdministration.class);

        Hashtable<String, Object> criteres = new Hashtable<String, Object>();
        criteres.put(ITIAdministration.FIND_FOR_GENRE_ADMINISTRATION, genre);
        criteres.put(ITITiers.FIND_FOR_INLCURE_INACTIF, new Boolean(false)); // que les administrations actives

        administaration.setISession(PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        Object[] obj = administaration.find(criteres);

        if ((obj == null) || (obj.length == 0)) {
            return null;
        } else {
            PRTiersWrapper[] result = new PRTiersWrapper[obj.length];

            for (int i = 0; i < obj.length; i++) {
                result[i] = new PRTiersWrapper((GlobazValueObject) obj[i], PRTiersWrapper.TI_PERSONNE_AVS);
            }

            return result;
        }
    }

    public static PRTiersWrapper[] getAdministrationActiveForGenreAndCode(BISession session, String genre, String code)
            throws Exception {

        if (JadeStringUtil.isEmpty(genre) || JadeStringUtil.isEmpty(code)) {
            return null;
        }

        ITIAdministration administration = (ITIAdministration) session.getAPIFor(ITIAdministration.class);

        Hashtable<String, Object> criteres = new Hashtable<String, Object>();
        criteres.put(ITIAdministration.FIND_FOR_GENRE_ADMINISTRATION, genre);
        criteres.put(ITIAdministration.FIND_FOR_CODE_ADMINISTRATION, code);

        administration.setISession(PRSession.connectSession(BSessionUtil.getSessionFromThreadContext(),
                TIApplication.DEFAULT_APPLICATION_PYXIS));

        Object[] obj = administration.find(criteres);

        if ((obj == null) || (obj.length == 0)) {
            return null;
        } else {
            PRTiersWrapper[] result = new PRTiersWrapper[obj.length];

            for (int i = 0; i < obj.length; i++) {
                result[i] = new PRTiersWrapper((GlobazValueObject) obj[i], PRTiersWrapper.TI_PERSONNE_AVS);
            }

            return result;
        }

    }

    /**
     * getter pour les administrations d'un genre
     *
     * @deprecated replaced by getAdministrationActiveForGenre
     */
    @Deprecated
    public static final PRTiersWrapper[] getAdministrationForGenre(BISession session, String genre) throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(genre)) {
            return null;
        }

        ITIAdministration administaration = (ITIAdministration) session.getAPIFor(ITIAdministration.class);

        Hashtable<String, Object> criteres = new Hashtable<String, Object>();
        criteres.put(ITIAdministration.FIND_FOR_GENRE_ADMINISTRATION, genre);

        administaration.setISession(PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        Object[] obj = administaration.find(criteres);

        if ((obj == null) || (obj.length == 0)) {
            return null;
        } else {
            PRTiersWrapper[] result = new PRTiersWrapper[obj.length];

            for (int i = 0; i < obj.length; i++) {
                result[i] = new PRTiersWrapper((GlobazValueObject) obj[i], PRTiersWrapper.TI_PERSONNE_AVS);
            }

            return result;
        }
    }

    public static final PRTiersWrapper getAdministrationParId(BISession session, String idTiers) throws Exception {
        if (JadeStringUtil.isEmpty(idTiers)) {
            return null;
        }

        ITIAdministration administaration = (ITIAdministration) session.getAPIFor(ITIAdministration.class);

        Hashtable<String, Object> criteres = new Hashtable<String, Object>();
        criteres.put(ITITiers.FIND_FOR_IDTIERS, idTiers);

        administaration.setISession(PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        Object[] result = administaration.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else {
            return new PRTiersWrapper((GlobazValueObject) result[0], PRTiersWrapper.TI_PERSONNE_AVS);
        }
    }

    /**
     * Va chercher dans les tiers standard, puis les administrations si non trouvé afin de retourner la langue de l'id
     * tiers passé en paramètre.
     *
     * @param session La session.
     * @param idTiers L'id tiers que l'on souhaite chercher sa langue.
     * @return La langue ISO.
     */
    public static final String getIsoLangFromIdTiers(final BSession session, final String idTiers) {
        PRTiersWrapper tiers;
        try {
            tiers = PRTiersHelper.getTiersParId(session, idTiers);

            if (null == tiers) {
                tiers = PRTiersHelper.getAdministrationParId(session, idTiers);
            }
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }

        String codeIsoLangue = session.getCode(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
        return PRUtil.getISOLangueTiers(codeIsoLangue);
    }

    /**
     * <p>
     * Retourne une adresse de courrier valide pour un tiers.
     * </p>
     * <p>
     * Cette méthode utilise le système de cascade mis en place par pyxis, elle recherche une adresse d'abord pour le
     * domaine donnée en paramètre, puis pour le domaine standard, etc.
     * </p>
     *
     * @param session
     * @param idTiers
     * @param idAffilie
     * @param csDomaine
     *            définira le domaine dans lequel commencer la recherche d'adresse
     *
     * @return une adresse ou chaîne vide s'il n'y a pas d'adresse pour ce tiers.
     *
     * @throws Exception
     */
    public static final String getAdresseCourrierFormatee(BISession session, String idTiers, String idAffilie,
            String csDomaine) throws Exception {

        return PRTiersHelper.getAdresseGeneriqueFormatee(session, idTiers, IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                idAffilie, csDomaine);

    }

    /**
     * <p>
     * retourne une adresse de courrier valide pour un tiers. Attention, spécifique aux rentes
     * </p>
     * <p>
     * Cette méthode utilise le système de cascade mis en place par pyxis en fonction d'une paramètre dans le fichier
     * corvus.properties, dans le cas ou la properties est à false, elle recherche une adresse d'abord pour le domaine
     * passé en paramètre, puis pour le domaine standard, etc.
     * </p>
     *
     * @param session
     * @param idTiers
     * @param idAffilie
     * @param csDomaine
     *            définira le domaine dans lequel commencer la recherche d'adresse
     *
     * @return une adresse ou chaîne vide s'il n'y a pas d'adresse pour ce tiers.
     *
     * @throws Exception
     */
    public static final String getAdresseCourrierFormateeRente(BISession session, String idTiers, String csDomaine,
            String idExterne, String langue, ITIAdresseFormater formater, String date) throws Exception {

        String prop = null;

        try {
            BSession sessionTransmise = (BSession) session;

            if (sessionTransmise.getApplicationId().toLowerCase().equals("corvus")) {
                prop = sessionTransmise.getApplication().getProperty("isWantAdresseCourrier");
            } else {
                prop = null;
            }
        } catch (Exception e) {
            prop = null;
        }
        if ((prop != null) && prop.equals("true")) {
            return PRTiersHelper.getAdresseGeneriqueFormateeRente(session, idTiers,
                    IConstantes.CS_AVOIR_ADRESSE_COURRIER, csDomaine, idExterne, formater, langue, date, false);
        } else {
            return PRTiersHelper.getAdresseGeneriqueFormateeRente(session, idTiers,
                    IConstantes.CS_AVOIR_ADRESSE_COURRIER, csDomaine, idExterne, formater, langue, date, true);
        }
    }

    /**
     * <p>
     * retourne une adresse de domicile valide pour un tiers
     * </p>
     * <p>
     * Cette méthode utilise le système de cascade mis en place par pyxis, elle recherche une adresse d'abord pour le
     * domaine de l'application passé en paramètre, puis pour le domaine standard, etc.
     * </p>
     *
     * @param session
     * @param idTiers
     *
     * @return une adresse ou chaîne vide s'il n'y a pas d'adresse pour ce tiers.
     *
     * @throws Exception
     */
    @Deprecated
    public static final String getAdresseDomicileFormatee(BISession session, String idTiers) throws Exception {
        return PRTiersHelper.getAdresseGeneriqueFormatee(session, idTiers, IConstantes.CS_AVOIR_ADRESSE_COURRIER, null,
                "");
    }

    /**
     * retourne une adresse de domicile
     *
     * @param session
     * @param idTiers
     * @return
     * @throws Exception
     */
    public static final String getAdresseFormatee(BISession session, String idTiers) throws Exception {
        return PRTiersHelper.getAdresseGeneriqueFormatee(session, idTiers, IConstantes.CS_AVOIR_ADRESSE_DOMICILE, null,
                "");
    }

    /**
     * <p>
     * retourne une adresse valide pour un tiers.
     * </p>
     * <p>
     * Cette méthode utilise le système de cascade mis en place par pyxis, elle recherche une adresse d'abord pour le
     * domaine passé en paramètre, puis pour le domaine standard, etc.
     * </p>
     *
     * @param session
     *            session en cours
     * @param idTiers
     *            Id du tiers dont il faut retourner l'adresse
     * @param typeAdresse
     *            Type d'adresse, si c'est pour le courrier ou le domicile
     * @param idAffilie
     * @param csDomaine
     *            définira le domaine dans lequel commencer la recherche d'adresse
     *
     * @return une adresse ou chaîne vide s'il n'y a pas d'adresse pour ce tiers.
     *
     * @throws Exception
     */
    public static final String getAdresseGeneriqueFormatee(BISession session, String idTiers, String typeAdresse,
            String idAffilie, String csDomaine) throws Exception {

        Hashtable<String, Object> params = new Hashtable<String, Object>();
        params.put(ITITiers.FIND_FOR_IDTIERS, idTiers);

        ITITiers helper = (ITITiers) session.getAPIFor(ITITiers.class);
        helper.setISession(PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        ITITiers[] result = helper.findTiers(params);

        if ((result == null) || (result.length == 0)) {
            return "";
        } else {

            ITITiers tiers = result[0];

            tiers.setISession(helper.getISession());

            IPRAffilie aff = PRAffiliationHelper.getEmployeurParIdAffilie(session, null, idAffilie, idTiers);

            String nAff = "";

            if (aff != null) {
                nAff = aff.getNumAffilie();
            } else {
                nAff = "";
            }

            return tiers.getAdresseAsString(typeAdresse, csDomaine, JACalendar.todayJJsMMsAAAA(), nAff);
        }
    }

    /**
     * <p>
     * retourne une adresse valide pour un tiers.
     * </p>
     * <p>
     * Cette méthode utilise le système de cascade mis en place par pyxis, elle recherche une adresse d'abord pour le
     * domaine passé en paramètre, puis pour le domaine standard, etc.
     * </p>
     *
     * @param session
     *            session en cours
     * @param idTiers
     *            Id du tiers dont il faut retourner l'adresse
     * @param typeAdresse
     *            Type d'adresse, si c'est pour le courrier ou le domicile
     * @param idAffilie
     * @param csDomaine
     *            définira le domaine dans lequel commencer la recherche d'adresse
     *
     * @return une adresse ou chaîne vide s'il n'y a pas d'adresse pour ce tiers.
     *
     * @throws Exception
     */
    public static final String getAdresseGeneriqueFormateeRente(BISession session, String idTiers, String typeAdresse,
            String csDomaine, String idExterne, ITIAdresseFormater formater, String langue, String date, boolean herite)
            throws Exception {
        TITiers t = new TITiers();
        t.setIdTiers(idTiers);
        t.setSession((BSession) session);

        if (!JadeStringUtil.isBlankOrZero(idExterne)) {
            // Traitement de l'idExterne, si c'est un idAffilie, on le change en numAffilie
            IAFAffiliation affiliationIfc = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);
            Hashtable<String, Object> criteres = new Hashtable<String, Object>();

            criteres.put(IAFAffiliation.FIND_FOR_AFFILIATIONID, idExterne);

            IAFAffiliation[] result = affiliationIfc.findAffiliation(criteres);

            // si existe, on change l'idExterne
            if (result.length > 0) {
                idExterne = result[0].getAffilieNumero();
            }
        }

        if (null == formater) {
            formater = new TIAdresseFormater();
        }

        if ("".equals(date)) {
            date = JACalendar.todayJJsMMsAAAA();
        }

        // bz-5554
        if (JadeStringUtil.isBlankOrZero(idExterne)) {
            idExterne = null;
        }

        return t.getAdresseAsString(typeAdresse, csDomaine, idExterne, date, formater, herite, langue);
    }

    /**
     * <p>
     * Retourne l'adresse de paiement d'un tiers. Retourne en premier l'adresse du domaine passé en paramètre si elle
     * existe, sinon l'adresse standard
     * </p>
     * <p>
     * Retourne new TIAdressePaiementData() si aucune adresse
     * </p>
     *
     * @param BSession
     * @param idTiersAdressePmt
     * @param domainePaiement
     * @param idExterne
     *            (numAffilie ou idAffilie)
     * @param dateJJsMMsAAAA
     *
     * @throws Exception
     *
     */
    public static final TIAdressePaiementData getAdressePaiementData(BSession session, BTransaction transaction,
            String idTiersAdressePmt, String domainePaiement, String idExterne, String dateJJsMMsAAAA)
            throws Exception {

        TIAdressePaiementDataManager mgr = new TIAdressePaiementDataManager();

        // si pas d'id Tiers renseigné, -> retourne une adresse vide !!!
        if (JadeStringUtil.isBlankOrZero(idTiersAdressePmt)) {
            return new TIAdressePaiementData();
        }

        if (!JadeStringUtil.isBlankOrZero(idExterne)) {
            // Traitement de l'idExterne, si c'est un idAffilie, on le change en numAffilie
            IAFAffiliation affiliationIfc = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);
            Hashtable<String, Object> criteres = new Hashtable<String, Object>();

            criteres.put(IAFAffiliation.FIND_FOR_AFFILIATIONID, idExterne);

            IAFAffiliation[] result = affiliationIfc.findAffiliation(criteres);

            // si existe, on change l'idExterne
            if (result.length > 0) {
                idExterne = result[0].getAffilieNumero();
            }
        }

        if (null == dateJJsMMsAAAA) {
            dateJJsMMsAAAA = "";
        }

        if (JadeStringUtil.isEmpty(domainePaiement)) {
            throw new Exception(
                    "Erreur dans la recherche de l'adresse de paiement : Pas de domaine passé en paramètre.");
        } else {

            mgr.setSession(session);
            mgr.setForIdTiers(idTiersAdressePmt);
            if (JadeStringUtil.isEmpty(dateJJsMMsAAAA)) {
                mgr.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
            } else {
                mgr.setForDateEntreDebutEtFin(dateJJsMMsAAAA);
            }
            mgr.find(BManager.SIZE_NOLIMIT);

            java.util.Collection<BIEntity> c = TIAdressePmtResolver.resolve(mgr, domainePaiement, idExterne);

            if (c.size() > 0) {
                return (TIAdressePaiementData) c.toArray()[0];
            }

            // Adresse pas encore trouvée ?
            // ERREUR !! Aucune adresse de paiement pour ce tiers !!
            return new TIAdressePaiementData();
        }
    }

    private static final String getAdresseRecours(TIAdministrationAdresse admAdr) {

        String adresseTribunal = "";

        if (!JadeStringUtil.isEmpty(admAdr.getDesignation1_tiers())) {
            adresseTribunal += admAdr.getDesignation1_tiers();
        }
        if (!JadeStringUtil.isEmpty(admAdr.getDesignation2_tiers())) {
            adresseTribunal += " " + admAdr.getDesignation2_tiers();
        }
        if (!JadeStringUtil.isBlank(admAdr.getCasePostaleComp())) {
            adresseTribunal += ", " + admAdr.getCasePostaleComp();
        }
        if (!JadeStringUtil.isEmpty(admAdr.getRue())) {
            adresseTribunal += ", " + admAdr.getRue();
        }

        if (!JadeStringUtil.isEmpty(admAdr.getNumero())) {
            adresseTribunal += " " + admAdr.getNumero();
        }
        if (!JadeStringUtil.isEmpty(admAdr.getNpa())) {
            adresseTribunal += ", " + admAdr.getNpa();
        }
        if (!JadeStringUtil.isEmpty(admAdr.getLocalite())) {
            adresseTribunal += " " + admAdr.getLocalite();
        }

        return adresseTribunal;

    }

    /**
     * Retourne une string de l'adresse du tribunal pour un prononce (tribunal du canton de domicile du tiers)
     */
    public static final String getAdresseTribunalPourOfficeAI(BISession session, String csOfficeAI, String idTiers,
            String dateForTribunal) throws Exception {

        String adresseTribunal = "";

        TIAdministrationAdresseManager admAdrMgr = new TIAdministrationAdresseManager();
        admAdrMgr.setSession((BSession) session);
        admAdrMgr.setForGenreAdministration("509002");

        PRTiersWrapper[] officesAI = null;
        PRTiersWrapper officeAI = null;
        officesAI = PRTiersHelper.getAdministrationActiveForGenre(session, "509004");

        if (officesAI != null) {
            for (int i = 0; i < officesAI.length; i++) {
                if (officesAI[i].getProperty(PRTiersWrapper.PROPERTY_CODE_ADMINISTRATION).equals(csOfficeAI)) {
                    officeAI = officesAI[i];
                }
            }
        }

        if (null != officeAI) {
            admAdrMgr.setForCantonAdministration(officeAI.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON));

            if (null != dateForTribunal && JadeDateUtil.isGlobazDate(dateForTribunal)) {
                admAdrMgr.setForDateEntreDebutEtFin(dateForTribunal);
            }

            admAdrMgr.find(BManager.SIZE_NOLIMIT);

            if (admAdrMgr.size() == 0) {

                adresseTribunal = PRTiersHelper.AUCUN_TRIBUNAL_TROUVE;

            } else if (admAdrMgr.size() == 1) {

                TIAdministrationAdresse admAdr = (TIAdministrationAdresse) admAdrMgr.get(0);

                if (!JadeStringUtil.isEmpty(admAdr.getDesignation1_tiers())) {
                    adresseTribunal += admAdr.getDesignation1_tiers();
                }
                if (!JadeStringUtil.isEmpty(admAdr.getDesignation2_tiers())) {
                    adresseTribunal += " " + admAdr.getDesignation2_tiers();
                }

                if (!JadeStringUtil.isEmpty(admAdr.getAttention())) {
                    adresseTribunal += ", " + admAdr.getAttention();
                }
                if (!JadeStringUtil.isEmpty(admAdr.getRue())) {
                    adresseTribunal += ", " + admAdr.getRue();
                }
                if (!JadeStringUtil.isEmpty(admAdr.getNumero())) {
                    adresseTribunal += " " + admAdr.getNumero();
                }
                if (!JadeStringUtil.isBlank(admAdr.getCasePostaleComp())) {
                    adresseTribunal += ", " + admAdr.getCasePostaleComp();
                }
                if (!JadeStringUtil.isEmpty(admAdr.getNpa())) {
                    adresseTribunal += ", " + admAdr.getNpa();
                }
                if (!JadeStringUtil.isEmpty(admAdr.getLocalite())) {
                    adresseTribunal += " " + admAdr.getLocalite();
                }

            } else {
                String casePostal = "";
                boolean premiereIteration = true;
                PRTiersWrapper tiersDem = PRTiersHelper.getTiersParId(session, idTiers);

                for (int i = 0; i < admAdrMgr.getSize(); i++) {
                    TIAdministrationAdresse admAdr = (TIAdministrationAdresse) admAdrMgr.get(i);

                    if (premiereIteration) {
                        casePostal = admAdr.getCasePostaleComp();
                        premiereIteration = false;
                    }

                    if (admAdr.getLangue().equals(tiersDem.getProperty(PRTiersWrapper.PROPERTY_LANGUE))) {

                        adresseTribunal = "";
                        if (!JadeStringUtil.isEmpty(admAdr.getDesignation1_tiers())) {
                            adresseTribunal += admAdr.getDesignation1_tiers();
                        }
                        if (!JadeStringUtil.isEmpty(admAdr.getDesignation2_tiers())) {
                            adresseTribunal += " " + admAdr.getDesignation2_tiers();
                        }

                        if (!JadeStringUtil.isEmpty(admAdr.getAttention())) {
                            adresseTribunal += ", " + admAdr.getAttention();
                        }

                        if (!JadeStringUtil.isEmpty(admAdr.getRue())) {
                            adresseTribunal += ", " + admAdr.getRue();
                        }
                        if (!JadeStringUtil.isEmpty(admAdr.getNumero())) {
                            adresseTribunal += " " + admAdr.getNumero();
                        }

                        if (!JadeStringUtil.isBlank(admAdr.getCasePostaleComp())) {
                            adresseTribunal += ", " + admAdr.getCasePostaleComp();
                        }
                        if (!JadeStringUtil.isBlank(casePostal)
                                && JadeStringUtil.isBlank(admAdr.getCasePostaleComp())) {
                            adresseTribunal += ", " + casePostal;
                        }

                        if (!JadeStringUtil.isEmpty(admAdr.getNpa())) {
                            adresseTribunal += ", " + admAdr.getNpa();
                        }
                        if (!JadeStringUtil.isEmpty(admAdr.getLocalite())) {
                            adresseTribunal += " " + admAdr.getLocalite();
                        }

                    }

                }

            }
        } else {
            adresseTribunal = PRTiersHelper.AUCUN_TRIBUNAL_TROUVE;
        }

        return adresseTribunal;
    }

    /**
     * Retourne une string de l'adresse du tribunal pour un tiers (tribunal du canton de domicile du tiers)
     */
    public static final String getAdresseTribunalPourTiers(BISession session, PRTiersWrapper tiers) throws Exception {

        String adresseTribunal = "";
        String adresseTribunalFR = "";
        String adresseTribunalDE = "";
        String adresseTribunalAutre = "";
        String canton = "";
        String cantonTypeCant = "";
        String cantonTypeProf = "";
        String typeCaisse = "";

        TIAdministrationAdresseManager admAdrMgr = new TIAdministrationAdresseManager();
        admAdrMgr.setSession((BSession) session);
        admAdrMgr.setForGenreAdministration("509002");

        PRTiersWrapper tiers2 = PRTiersHelper.getTiersAdresseDomicileParId(session,
                tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS), JACalendar.today().toString());
        BSession sessionApp = (BSession) session;

        cantonTypeCant = sessionApp.getApplication().getProperty(ID_CANTON_PROP);
        typeCaisse = sessionApp.getApplication().getProperty(TYPE_DE_CAISSE);
        if (tiers2 == null) {
            adresseTribunal = PRTiersHelper.AUCUN_TRIBUNAL_TROUVE;
        } else {
            if (JadeStringUtil.isBlankOrZero(tiers2.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON))) {
                cantonTypeProf = PRTiersHelper.getCanton(session,
                        tiers2.getProperty(PRTiersWrapper.PROPERTY_NPA)
                                + (tiers2.getProperty(PRTiersWrapper.PROPERTY_NPA_SUP).length() > 1
                                        ? tiers2.getProperty(PRTiersWrapper.PROPERTY_NPA_SUP)
                                        : "0" + tiers2.getProperty(PRTiersWrapper.PROPERTY_NPA_SUP)));
            } else {
                cantonTypeProf = tiers2.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON);
            }
            if (typeCaisse != null) {
                if (typeCaisse.equals(CAISSE_CANT)) {
                    canton = cantonTypeCant;
                } else if (typeCaisse.equals(CAISSE_PROF)) {
                    canton = cantonTypeProf;
                } else {
                    canton = cantonTypeProf;
                }
            }

            admAdrMgr.setForCantonAdministration(canton);
            admAdrMgr.find();

            if (admAdrMgr.size() == 0) {

                adresseTribunal = PRTiersHelper.AUCUN_TRIBUNAL_TROUVE;

            } else if (admAdrMgr.size() == 1) {

                TIAdministrationAdresse admAdr = (TIAdministrationAdresse) admAdrMgr.get(0);
                adresseTribunal = PRTiersHelper.getAdresseRecours(admAdr);

            } else {
                // Si canton bilingue, 002 Berne, 010 Fribourg, 023 Valais , test selon la langue de l'assuré
                if (canton.equals(IConstantes.CS_LOCALITE_CANTON_BERNE)
                        || canton.equals(IConstantes.CS_LOCALITE_CANTON_FRIBOURG)
                        || canton.equals(IConstantes.CS_LOCALITE_CANTON_VALAIS)) {
                    for (int i = 0; i < admAdrMgr.getSize(); i++) {
                        TIAdministrationAdresse admAdr = (TIAdministrationAdresse) admAdrMgr.get(i);

                        // Si assuré FR, recours FR sinon DE
                        if (IConstantes.CS_TIERS_LANGUE_FRANCAIS
                                .equals(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE))) {

                            if (JadeStringUtil.isBlank(adresseTribunalFR)) {

                                if (admAdr.getLangue().equals(IConstantes.CS_TIERS_LANGUE_FRANCAIS)) {
                                    adresseTribunalFR = PRTiersHelper.getAdresseRecours(admAdr);
                                } else if (admAdr.getLangue().equals(IConstantes.CS_TIERS_LANGUE_ALLEMAND)) {
                                    adresseTribunalDE = PRTiersHelper.getAdresseRecours(admAdr);
                                } else {
                                    adresseTribunalAutre = PRTiersHelper.getAdresseRecours(admAdr);
                                }

                            }
                            // Si assuré DE, recours DE sinon FR
                        } else if (IConstantes.CS_TIERS_LANGUE_ALLEMAND
                                .equals(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE))
                                || IConstantes.CS_TIERS_LANGUE_ROMANCHE
                                        .equals(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE))) {

                            if (JadeStringUtil.isBlank(adresseTribunalDE)) {

                                if (admAdr.getLangue().equals(IConstantes.CS_TIERS_LANGUE_FRANCAIS)) {
                                    adresseTribunalFR = PRTiersHelper.getAdresseRecours(admAdr);
                                } else if (admAdr.getLangue().equals(IConstantes.CS_TIERS_LANGUE_ALLEMAND)) {
                                    adresseTribunalDE = PRTiersHelper.getAdresseRecours(admAdr);
                                } else {
                                    adresseTribunalAutre = PRTiersHelper.getAdresseRecours(admAdr);
                                }

                            }
                            // Si assuré autre, recours FR sinon DE
                        } else {

                            if (!JadeStringUtil.isBlank(adresseTribunalFR)) {

                                if (admAdr.getLangue().equals(IConstantes.CS_TIERS_LANGUE_FRANCAIS)) {
                                    adresseTribunalFR = PRTiersHelper.getAdresseRecours(admAdr);
                                } else if (admAdr.getLangue().equals(IConstantes.CS_TIERS_LANGUE_ALLEMAND)) {
                                    adresseTribunalDE = PRTiersHelper.getAdresseRecours(admAdr);
                                } else {
                                    adresseTribunalAutre = PRTiersHelper.getAdresseRecours(admAdr);
                                }

                            }
                        }

                    }// fin boucle for

                    if (IConstantes.CS_TIERS_LANGUE_FRANCAIS
                            .equals(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE))) {

                        if (!JadeStringUtil.isBlank(adresseTribunalFR)) {
                            adresseTribunal = adresseTribunalFR;
                        } else if (!JadeStringUtil.isBlank(adresseTribunalDE)) {
                            adresseTribunal = adresseTribunalDE;
                        } else if (!JadeStringUtil.isBlank(adresseTribunalAutre)) {
                            adresseTribunal = adresseTribunalAutre;
                        } else {
                            adresseTribunal = PRTiersHelper.AUCUN_TRIBUNAL_TROUVE;
                        }

                    } else if (IConstantes.CS_TIERS_LANGUE_ALLEMAND
                            .equals(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE))
                            || IConstantes.CS_TIERS_LANGUE_ROMANCHE
                                    .equals(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE))) {

                        if (!JadeStringUtil.isBlank(adresseTribunalDE)) {
                            adresseTribunal = adresseTribunalDE;
                        } else if (!JadeStringUtil.isBlank(adresseTribunalFR)) {
                            adresseTribunal = adresseTribunalFR;
                        } else if (!JadeStringUtil.isBlank(adresseTribunalAutre)) {
                            adresseTribunal = adresseTribunalAutre;
                        } else {
                            adresseTribunal = PRTiersHelper.AUCUN_TRIBUNAL_TROUVE;
                        }

                    } else {
                        if (!JadeStringUtil.isBlank(adresseTribunalAutre)) {
                            adresseTribunal = adresseTribunalAutre;
                        } else if (!JadeStringUtil.isBlank(adresseTribunalFR)) {
                            adresseTribunal = adresseTribunalFR;
                        } else if (!JadeStringUtil.isBlank(adresseTribunalDE)) {
                            adresseTribunal = adresseTribunalDE;
                        } else {
                            adresseTribunal = PRTiersHelper.AUCUN_TRIBUNAL_TROUVE;
                        }
                    }
                } else {// Sinon test standard

                    boolean premiereIteration = true;
                    for (int i = 0; i < admAdrMgr.getSize(); i++) {

                        TIAdministrationAdresse admAdr = (TIAdministrationAdresse) admAdrMgr.get(i);

                        if (premiereIteration) {
                            adresseTribunal = PRTiersHelper.getAdresseRecours(admAdr);
                            premiereIteration = false;
                        }

                        if (admAdr.getLangue().equals(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE))) {
                            adresseTribunal = PRTiersHelper.getAdresseRecours(admAdr);
                        }
                    }

                    if (JadeStringUtil.isBlank(adresseTribunal)) {
                        adresseTribunal = PRTiersHelper.AUCUN_TRIBUNAL_TROUVE;
                    }
                }
            }
        }

        return adresseTribunal;
    }

    public static final int getAgeAvs(String csSexe, String dateNaissance) {

        int anneeNaissance;
        if (JadeDateUtil.isGlobazDate(dateNaissance)) {
            anneeNaissance = Integer.parseInt(dateNaissance.substring(6));
        } else if (JadeDateUtil.isGlobazDateMonthYear(dateNaissance)) {
            anneeNaissance = Integer.parseInt(dateNaissance.substring(3));
        } else {
            // si la date de naissance est invalide, on retournera les âges AVS actuels
            anneeNaissance = 2011;
        }

        if (csSexe.equalsIgnoreCase(ITIPersonne.CS_FEMME)) {
            // si c'est une femme
            if (anneeNaissance <= 1938) {
                return 62;
            } else {
                if (anneeNaissance <= 1941) {
                    return 63;
                } else {
                    return 64;
                }
            }
        } else {
            // sinon, ou par défaut, c'est un homme...
            return 65;
        }
    }

    public static final PRTiersWrapper[] getCaisseCompensationForCode(BISession session, String code) throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(code)) {
            return null;
        }

        ITIAdministration administaration = (ITIAdministration) session.getAPIFor(ITIAdministration.class);

        Hashtable<String, Object> criteres = new Hashtable<String, Object>();
        criteres.put(ITIAdministration.FIND_FOR_GENRE_ADMINISTRATION, "509001");
        criteres.put(ITIAdministration.FIND_FOR_CODE_ADMINISTRATION, code);

        administaration.setISession(PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        Object[] obj = administaration.find(criteres);

        if ((obj == null) || (obj.length == 0)) {
            return null;
        } else {
            PRTiersWrapper[] result = new PRTiersWrapper[obj.length];

            for (int i = 0; i < obj.length; i++) {
                result[i] = new PRTiersWrapper((GlobazValueObject) obj[i], PRTiersWrapper.TI_PERSONNE_AVS);
            }

            return result;
        }
    }

    public static final PRTiersWrapper[] getCaisseForCode(BISession session, String code) throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(code)) {
            return null;
        }

        ITIAdministration administaration = (ITIAdministration) session.getAPIFor(ITIAdministration.class);

        Hashtable<String, Object> criteres = new Hashtable<String, Object>();
        criteres.put(ITIAdministration.FIND_FOR_CODE_ADMINISTRATION, code);

        administaration.setISession(PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        Object[] obj = administaration.find(criteres);

        if ((obj == null) || (obj.length == 0)) {
            return null;
        } else {
            PRTiersWrapper[] result = new PRTiersWrapper[obj.length];

            for (int i = 0; i < obj.length; i++) {
                result[i] = new PRTiersWrapper((GlobazValueObject) obj[i], "TIPersonneAvs");
            }

            return result;
        }
    }

    /**
     * recherche le canton dans lequel se trouve la ville dont le no postal est transmis en argument.
     *
     * @param session
     * @param npa
     *
     * @return le code système du canton, ou <code>null</code> si non trouvé
     *
     * @throws Exception
     *             si erreur dans pyxis.
     */
    public static final String getCanton(BISession session, String npa) throws Exception {
        if (JadeStringUtil.isEmpty(npa)) {
            return null;
        }

        ITILocalite localite = (ITILocalite) session.getAPIFor(ITILocalite.class);
        Hashtable<String, Object> criteres = new Hashtable<String, Object>();

        criteres.put(ITILocalite.FIND_FOR_NPA_LIKE, npa);
        criteres.put(ITILocalite.FIND_FOR_IDPAYS, PRTiersHelper.ID_PAYS_SUISSE);

        Object[] result = localite.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else {
            // sinon on retourne le premier qui ressemble.
            // les suivants sont généralement des sous-localités ou des numéros synonymes.
            return (String) ((GlobazValueObject) result[0]).getProperty("idCanton");
        }
    }

    /**
     * <p>
     * Retourne une {@link Map} ayant comme clé les noms des cantons dans la langue de l'utilisateur (défini dans la
     * session) et comme valeurs les codes systèmes des cantons.
     * </p>
     * <p>
     * La map sera triée par ordre alphabétique des noms de cantons (par exemple lors de l'utilisation de
     * {@link Map#values()})
     * </p>
     *
     * @param session
     *            une session utilisateur (définir la langue des noms des cantons)
     * @return une {@link Map} ayant comme clé les noms des cantons dans la langue de l'utilisateur (défini dans la
     *         session) et comme valeurs les codes systèmes des cantons
     * @throws Exception
     *             Si un problème survient lors de la récupération des noms et codes systèmes des cantons en base de
     *             données
     */
    public static final Map<String, String> getCantonAsMap(BSession session) throws Exception {
        String langueUtilisateur = session.getIdLangue();

        if (PRTiersHelper.CANTONS_PAR_LANGUE == null) {
            PRTiersHelper.CANTONS_PAR_LANGUE = new HashMap<String, Map<String, String>>();
        }

        if (!PRTiersHelper.CANTONS_PAR_LANGUE.containsKey(langueUtilisateur)) {

            Map<String, String> cantons = new TreeMap<String, String>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return JadeStringUtil.convertSpecialChars(o1).compareTo(JadeStringUtil.convertSpecialChars(o2));
                }
            });

            FWParametersCodeManager parametersCodeManager = FWTranslation.getSystemCodeList("PYCANTON", session);

            for (int i = 0; i < parametersCodeManager.size(); i++) {
                FWParametersSystemCode codeSysteme = (FWParametersSystemCode) parametersCodeManager.getEntity(i);
                cantons.put(codeSysteme.getCurrentCodeUtilisateur().getLibelle(),
                        codeSysteme.getCurrentCodeUtilisateur().getIdCodeSysteme());
            }

            PRTiersHelper.CANTONS_PAR_LANGUE.put(langueUtilisateur, cantons);
        }

        return PRTiersHelper.CANTONS_PAR_LANGUE.get(langueUtilisateur);
    }

    /**
     * <p>
     * recherche le (code OFAS du) canton dans lequel se trouve la ville dont le no postal est transmis en argument.
     * </p>
     *
     * @param session
     * @param npa
     *
     * @return le code système du canton, ou <code>null</code> si non trouvé
     *
     * @throws Exception
     */
    public static final String getCodeOFASCanton(BISession session, String npa) throws Exception {
        if (JadeStringUtil.isEmpty(npa)) {
            return null;
        }

        ITILocalite localite = (ITILocalite) session.getAPIFor(ITILocalite.class);
        Hashtable<String, Object> criteres = new Hashtable<String, Object>();

        criteres.put(ITILocalite.FIND_FOR_NPA_LIKE, npa);
        criteres.put(ITILocalite.FIND_FOR_IDPAYS, PRTiersHelper.ID_PAYS_SUISSE);

        Object[] result = localite.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else {
            // sinon on retourne le premier qui ressemble.
            // les suivants sont généralement des sous-localités ou des numéros synonymes.
            return (String) ((GlobazValueObject) result[0]).getProperty("codeOFASCanton");
        }
    }

    /**
     * <p>
     * Retourne l'âge d'arriver à l'AVS d'un tiers.<br/>
     * Exemple : date naissance du tiers (masculin) 19.06.1947 -> âge d'arrivé à l'AVS : 01.07.2012
     * </p>
     *
     * @param session
     *            une session utilisateur
     * @param idTiers
     *            l'ID du tiers dont on aimerait connaître l'âge d'arrivé à l'AVS
     * @return la date d'arrivée à l'âge AVS au format dd.mm.yyyy
     * @throws Exception
     *             si le sexe ou la date de naissance du tiers n'est pas défini
     */
    public static String getDateAgeVieillesseTiers(BSession session, String idTiers) throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, idTiers);
        String dateNaissance = tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);

        if (!JadeDateUtil.isGlobazDate(dateNaissance)) {
            throw new Exception(
                    "Erreur : la date de naissance du tiers n'est pas renseignée ou est invalide. idTiers = " + idTiers
                            + " nss = " + tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
        }

        String csSexe = tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE);
        if (JadeStringUtil.isBlankOrZero(csSexe)) {
            throw new Exception("Erreur : Le sexe du tiers n'est pas renseigné. idTiers = " + idTiers + " nss = "
                    + tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
        }

        return PRTiersHelper.getDateDebutDroitAVS(dateNaissance, csSexe);
    }

    /**
     * <p>
     * Retourne le 1er du mois durant lequel le tiers pourra prétendre à une rente AVS
     * </p>
     * <p>
     * Exemple : <br/>
     * Date de naissance : 08.04.1950 -> retournera 01.05.2015 pour un homme (65ans + un mois)
     * </p>
     *
     * @param dateNaissance
     *            une date au format <code>"JJ.MM.AAAA"</code>
     * @param csSexe
     *            {@link ITIPersonne#CS_HOMME} ou {@link ITIPersonne#CS_FEMME}
     * @return une date au format <code>"JJ.MM.AAAA"</code>, ou une chaîne vide si la date de naissance ou le sexe n'est
     *         pas définit/dans le bon format
     */
    public static String getDateDebutDroitAVS(String dateNaissance, String csSexe) {

        if (!JadeDateUtil.isGlobazDate(dateNaissance) || JadeStringUtil.isBlank(csSexe)) {
            return "";
        }

        int anneeNaissance = Integer.parseInt(dateNaissance.substring(6));
        int ageAvs = 0;

        if (ITIPersonne.CS_FEMME.equalsIgnoreCase(csSexe)) {
            // si c'est une femme
            if (anneeNaissance <= 1938) {
                ageAvs = anneeNaissance + 62;
            } else {
                if (anneeNaissance <= 1941) {
                    ageAvs = anneeNaissance + 63;
                } else {
                    ageAvs = anneeNaissance + 64;
                }
            }
        } else {
            // sinon, ou par défaut, c'est un homme...
            ageAvs = anneeNaissance + 65;
        }

        int month = (Integer.parseInt(dateNaissance.substring(3, 5)) + 1);

        if (month == 13) {
            month = 1;
            ageAvs++;
        }

        // on construit la date d'age AVS
        return "01." + (month < 10 ? "0" + month : month) + "." + ageAvs;
    }

    /**
     * <p>
     * retourne un vecteur de tableaux String[2]{codePays, NomPays}. La langue utilisée est celle transmise dans la
     * session.
     * </p>
     *
     * @param session
     *
     * @return la valeur courante de l'attribut pays
     *
     * @throws Exception
     * @deprecated veuillez utiliser {@link #getPaysAsMap(BSession)} et la libraire {@link Gson}
     */
    @Deprecated
    public static final Vector<String[]> getPays(BISession session) throws Exception {
        String idLangue = session.getIdLangue();
        Vector<String[]> pays = PRTiersHelper.LISTE_PAYS.get(idLangue);

        if (pays == null) {
            ITIPays tiPays = (ITIPays) session.getAPIFor(ITIPays.class);

            pays = new Vector<String[]>();
            tiPays.setISession(PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

            Object[] result = tiPays.find(new Hashtable<String, Object>());

            for (int idPays = 0; idPays < result.length; ++idPays) {
                pays.add(new String[] { (String) ((GlobazValueObject) result[idPays]).getProperty("idPays"),
                        (String) ((GlobazValueObject) result[idPays]).getProperty("libelle") });
            }

            PRTiersHelper.LISTE_PAYS.put(idLangue, pays);
        }

        return pays;
    }

    /**
     * <p>
     * Retourne une {@link Map} avec comme clé les noms des pays dans la langue de l'utilisateur et comme valeurs les ID
     * des pays.
     * </p>
     * <p>
     * La map sera triée par ordre alphabétique (si utilisation de {@link Map#values()})
     * </p>
     * <p>
     * Pour une utilisation optimale dans un écran, vous pouvez convertir cette map en un objet JSON grâce à la
     * librairie {@link Gson} (avec la méthode {@link Gson#toJson(com.google.gson.JsonElement)
     * toJson(Map&lt;String,String&gt;)})
     * </p>
     *
     * @param session
     *            une session utilisateur (qui déterminera la langue pour les noms des pays)
     * @return une {@link Map} avec comme clé les noms des pays dans la langue de l'utilisateur et comme valeurs les ID
     *         des pays
     * @throws Exception
     *             si un problème survient lors de la récupération des pays depuis la base de données
     */
    public static final Map<String, String> getPaysAsMap(BSession session) throws Exception {
        String langueUtilisateur = JadeStringUtil.toUpperCase(session.getIdLangueISO());

        if (PRTiersHelper.PAYS_PAR_LANGUE == null) {
            PRTiersHelper.PAYS_PAR_LANGUE = new HashMap<String, Map<String, String>>();

            Map<String, String> paysEnFrancais = new TreeMap<String, String>();
            Map<String, String> paysEnAllemand = new TreeMap<String, String>();
            Map<String, String> paysEnItalien = new TreeMap<String, String>();

            TIPaysManager paysManager = new TIPaysManager();
            paysManager.setISession(PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));
            paysManager.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < paysManager.size(); i++) {
                TIPays unPays = (TIPays) paysManager.get(i);

                if (!JadeStringUtil.isBlank(unPays.getLibelleFr())) {
                    paysEnFrancais.put(unPays.getLibelleFr(), unPays.getIdPays());
                }

                if (!JadeStringUtil.isBlank(unPays.getLibelleDe())) {
                    paysEnAllemand.put(unPays.getLibelleDe(), unPays.getIdPays());
                }

                if (!JadeStringUtil.isBlank(unPays.getLibelleIt())) {
                    paysEnItalien.put(unPays.getLibelleIt(), unPays.getIdPays());
                }
            }

            PRTiersHelper.PAYS_PAR_LANGUE.put("FR", paysEnFrancais);
            PRTiersHelper.PAYS_PAR_LANGUE.put("DE", paysEnAllemand);
            PRTiersHelper.PAYS_PAR_LANGUE.put("IT", paysEnItalien);
        }

        return PRTiersHelper.PAYS_PAR_LANGUE.get(langueUtilisateur);
    }

    /**
     * <p>
     * cherche une "personneAVS" par son id
     * </p>
     *
     * @param session
     * @param idTiers
     *            l'id du tiers à chercher
     *
     * @return un VO de la personneAVS
     *
     * @throws Exception
     */
    public static final PRTiersWrapper getPersonneAVS(BISession session, String idTiers) throws Exception {
        if (JadeStringUtil.isEmpty(idTiers)) {
            return null;
        }

        ITIPersonneAvs personneAVS = (ITIPersonneAvs) session.getAPIFor(ITIPersonneAvs.class);
        Hashtable<String, Object> criteres = new Hashtable<String, Object>();

        criteres.put(ITIAbstractAdresseData.FIND_FOR_IDTIERS, idTiers);
        personneAVS.setISession(PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        Object[] result = personneAVS.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else {
            return new PRTiersWrapper((GlobazValueObject) result[0], PRTiersWrapper.TI_PERSONNE_AVS);
        }
    }

    /**
     * <p>
     * Recherche un "tiers" (sans adresse) à partir de son numéro AVS.
     * </p>
     *
     * @param session
     * @param noAvs
     *            Le noAVS du tiers à rechercher
     *
     * @return Le tiers, si trouvé ou null si non trouvé.
     *
     * @throws Exception
     *             si problème avec PYXIS
     */
    public static final PRTiersWrapper getTiers(BISession session, String noAvs) throws Exception {
        if (JadeStringUtil.isEmpty(noAvs)) {
            return null;
        }

        TIPersonneAvsManager mgr = new TIPersonneAvsManager();
        if (!session.isConnected()) {
            session = PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS);
        }
        mgr.setISession(session);
        mgr.setForNumAvsActuel(noAvs);
        mgr.setForIncludeInactif(false);
        mgr.find();
        if (mgr.size() == 0) {
            return null;
        } else {
            return new PRTiersWrapper((TITiersViewBean) mgr.getFirstEntity());
        }
    }

    /**
     * <p>
     * Recherche un "tiers" et son adresse de domicile à partir de son identifiant.
     * </p>
     *
     * @param session
     *            la session
     * @param idTiers
     *            l'identifiant du tiers à rechercher
     *
     * @return Le tiers, si trouvé, ou <code>null</code> si non trouvé.
     *
     * @throws Exception
     *             si la recherche échoue.
     */
    public static final PRTiersWrapper getTiersAdresseDomicileParId(BISession session, String idTiers)
            throws Exception {
        if (JadeStringUtil.isEmpty(idTiers)) {
            return null;
        }

        ITIPersonneAvsAdresse personneAVS = (ITIPersonneAvsAdresse) session.getAPIFor(ITIPersonneAvsAdresse.class);
        Hashtable<String, Object> criteres = new Hashtable<String, Object>();

        criteres.put(ITIAbstractAdresseData.FIND_FOR_IDTIERS, idTiers);

        // BZ 5124 SPECIFIQUE FER CIAM, ON NE DOIT PAS TENIR COMPTE DE L'ADRESSE DE DOMICILE, MAIS DE COURRIER DANS LE
        // DOMAINE RENTES
        String prop = null;

        try {
            BSession sessionTransmise = (BSession) session;

            if (sessionTransmise.getApplicationId().toLowerCase().equals("corvus")) {
                prop = sessionTransmise.getApplication().getProperty("isWantAdresseCourrier");
            } else {
                prop = null;
            }
        } catch (Exception e) {
            prop = null;
        }

        if ((prop != null) && prop.equals("true")) {
            criteres.put(ITIAbstractAdresseData.FIND_FOR_TYPE_ADRESSE, PRTiersHelper.CS_ADRESSE_COURRIER);
            criteres.put(ITIAbstractAdresseData.FIND_FOR_IDAPPLICATION,
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
        } else {
            criteres.put(ITIAbstractAdresseData.FIND_FOR_TYPE_ADRESSE, PRTiersHelper.CS_ADRESSE_DOMICILE);
        }

        personneAVS.setISession(PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        Object[] result = personneAVS.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else {
            return new PRTiersWrapper((GlobazValueObject) result[0], PRTiersWrapper.TI_PERSONNE_AVS_ADRESSE);
        }
    }

    public static final PRTiersWrapper getTiersAdresseDomicileParId(BISession session, String idTiers, String date)
            throws Exception {
        if (JadeStringUtil.isEmpty(idTiers)) {
            return null;
        }

        ITIPersonneAvsAdresse personneAVS = (ITIPersonneAvsAdresse) session.getAPIFor(ITIPersonneAvsAdresse.class);
        Hashtable<String, Object> criteres = new Hashtable<String, Object>();

        criteres.put(ITIAbstractAdresseData.FIND_FOR_IDTIERS, idTiers);

        // BZ 5124 SPECIFIQUE FER CIAM, ON NE DOIT PAS TENIR COMPTE DE L'ADRESSE DE DOMICILE, MAIS DE COURRIER DANS LE
        // DOMAINE RENTES
        String prop = null;

        try {
            BSession sessionTransmise = (BSession) session;

            if (sessionTransmise.getApplicationId().toLowerCase().equals("corvus")) {
                prop = sessionTransmise.getApplication().getProperty("isWantAdresseCourrier");
            } else {
                prop = null;
            }
        } catch (Exception e) {
            prop = null;
        }

        if ((prop != null) && prop.equals("true")) {
            criteres.put(ITIAbstractAdresseData.FIND_FOR_TYPE_ADRESSE, PRTiersHelper.CS_ADRESSE_COURRIER);
            criteres.put(ITIAbstractAdresseData.FIND_FOR_IDAPPLICATION,
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
        } else {
            criteres.put(ITIAbstractAdresseData.FIND_FOR_TYPE_ADRESSE, PRTiersHelper.CS_ADRESSE_DOMICILE);
        }

        criteres.put(ITIAbstractAdresseData.FIND_FOR_DATE_ENTRE_DEBUT_ET_FIN, date);
        personneAVS.setISession(PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        Object[] result = personneAVS.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else {
            return new PRTiersWrapper((GlobazValueObject) result[0], PRTiersWrapper.TI_PERSONNE_AVS_ADRESSE);
        }
    }

    public static final PRTiersWrapper[] getTiersAdresseLikeNoAVS(BISession session, String likeNoAVS)
            throws Exception {
        return PRTiersHelper.getTiersAdresseLikeNoAVSForceFormat(session, likeNoAVS, null);

    }

    public static final PRTiersWrapper[] getTiersAdresseLikeNoAVS(BISession session, String likeNoAVS, String date)
            throws Exception {
        if (JadeStringUtil.isEmpty(likeNoAVS)) {
            return null;
        }

        Hashtable<String, Object> criteres = new Hashtable<String, Object>();
        ITIPersonneAvsAdresse tiers = (ITIPersonneAvsAdresse) session.getAPIFor(ITIPersonneAvsAdresse.class);

        criteres.put(ITIPersonneAvsAdresse.FIND_FOR_NUM_AVS_LIKE, likeNoAVS);
        criteres.put(ITIAbstractAdresseData.FIND_FOR_TYPE_ADRESSE, ITIAvoirAdresse.CS_COURRIER);
        criteres.put(ITIAbstractAdresseData.FIND_FOR_IDAPPLICATION, ITIApplication.CS_DEFAUT);
        criteres.put(ITIAbstractAdresseData.FIND_FOR_DATE_ENTRE_DEBUT_ET_FIN, date);
        criteres.put(ITITiersAdresse.FIND_FOR_INLCURE_INACTIF, new Boolean(false)); // seul. les tiers actifs

        Object[] obj = tiers.find(criteres);

        if ((obj == null) || (obj.length == 0)) {
            return null;
        } else {
            PRTiersWrapper[] result = new PRTiersWrapper[obj.length];

            for (int i = 0; i < obj.length; i++) {
                result[i] = new PRTiersWrapper((GlobazValueObject) obj[i], PRTiersWrapper.TI_PERSONNE_AVS_ADRESSE);
            }

            return result;
        }
    }

    /**
     * @param session
     * @param likeNoAVS
     * @param isNNSS
     *            valeur possible : <code>true</code>, <code>false</code> ou <code>null</code>. Permet de forcer le
     *            formattage du NSS pour la recherche.
     * @return
     * @throws Exception
     */
    public static final PRTiersWrapper[] getTiersAdresseLikeNoAVSForceFormat(BISession session, String likeNoAVS,
            Boolean isNNSS) throws Exception {

        if (JadeStringUtil.isEmpty(likeNoAVS)) {
            return null;
        }

        Hashtable<String, Object> cr = new Hashtable<String, Object>();
        cr.put(ITIPersonneAvsAdresse.FIND_FOR_NUM_AVS_LIKE, likeNoAVS);

        // BZ 5124 SPECIFIQUE FER CIAM, ON NE DOIT PAS TENIR COMPTE DE L'ADRESSE DE DOMICILE, MAIS DE COURRIER DANS LE
        // DOMAINE RENTES
        String prop = null;

        try {
            BSession sessionTransmise = (BSession) session;

            if (sessionTransmise.getApplicationId().toLowerCase().equals("corvus")) {
                prop = sessionTransmise.getApplication().getProperty("isWantAdresseCourrier");
            } else {
                prop = null;
            }
        } catch (Exception e) {
            prop = null;
        }

        if ((prop != null) && prop.equals("true")) {
            cr.put(ITIAbstractAdresseData.FIND_FOR_TYPE_ADRESSE, PRTiersHelper.CS_ADRESSE_COURRIER);
            cr.put(ITIAbstractAdresseData.FIND_FOR_IDAPPLICATION,
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
        } else {
            cr.put(ITIPersonneAvsAdresse.FIND_FOR_SINGLE_ADRESSE_MODE, new Boolean(true)); // seul. l'adr de domicile
        }

        cr.put(ITITiersAdresse.FIND_FOR_INLCURE_INACTIF, new Boolean(false)); // seul. les tiers actifs
        // courante de chaque tiers
        cr.put("changeManagerSize", "600"); // retourne max. de 600 occurance
        if ((isNNSS != null) && isNNSS.booleanValue()) {
            cr.put("setTypeRechercheAvs", TINSSFormater.TYPE_EAN13);
        } else if ((isNNSS != null) && !isNNSS.booleanValue()) {
            cr.put("setTypeRechercheAvs", TINSSFormater.TYPE_NAVS);
        }

        ITIPersonneAvsAdresse mgr = (ITIPersonneAvsAdresse) session.getAPIFor(ITIPersonneAvsAdresse.class);
        Object[] obj = mgr.find(cr);

        if ((obj == null) || (obj.length == 0)) {
            return null;
        } else {
            PRTiersWrapper[] result = new PRTiersWrapper[obj.length];

            for (int i = 0; i < obj.length; i++) {
                result[i] = new PRTiersWrapper((GlobazValueObject) obj[i], PRTiersWrapper.TI_PERSONNE_AVS_ADRESSE);
            }

            return result;
        }
    }

    public static final PRTiersWrapper[] getTiersAdresseLikeNoAVSForceSingleAdrMode(BISession session, String likeNoAVS,
            Boolean isNNSS) throws Exception {

        if (JadeStringUtil.isEmpty(likeNoAVS)) {
            return null;
        }

        Hashtable<String, Object> cr = new Hashtable<String, Object>();
        cr.put(ITIPersonneAvsAdresse.FIND_FOR_NUM_AVS_LIKE, likeNoAVS);
        cr.put(ITIPersonneAvsAdresse.FIND_FOR_SINGLE_ADRESSE_MODE, new Boolean(true)); // seul. l'adr de domicile
        cr.put(ITITiersAdresse.FIND_FOR_INLCURE_INACTIF, new Boolean(false)); // seul. les tiers actifs
        // courante de chaque tiers
        cr.put("changeManagerSize", "600"); // retourne max. de 600 occurance
        if ((isNNSS != null) && isNNSS.booleanValue()) {
            cr.put("setTypeRechercheAvs", TINSSFormater.TYPE_EAN13);
        } else if ((isNNSS != null) && !isNNSS.booleanValue()) {
            cr.put("setTypeRechercheAvs", TINSSFormater.TYPE_NAVS);
        }

        ITIPersonneAvsAdresse mgr = (ITIPersonneAvsAdresse) session.getAPIFor(ITIPersonneAvsAdresse.class);
        Object[] obj = mgr.find(cr);

        if ((obj == null) || (obj.length == 0)) {
            return null;
        } else {
            PRTiersWrapper[] result = new PRTiersWrapper[obj.length];

            for (int i = 0; i < obj.length; i++) {
                result[i] = new PRTiersWrapper((GlobazValueObject) obj[i], PRTiersWrapper.TI_PERSONNE_AVS_ADRESSE);
            }

            return result;
        }
    }

    /**
     * <p>
     * Recherche un "tiers" à partir de son identifiant.
     * </p>
     *
     * @param session
     *            la session
     * @param idTiers
     *            l'identifiant du tiers à rechercher
     *
     * @return Le tiers, si trouvé, ou <code>null</code> si non trouvé.
     *
     * @throws Exception
     *             si la recherche échoue.
     */
    public static final PRTiersWrapper getTiersAdresseParId(BISession session, String idTiers) throws Exception {
        if (JadeStringUtil.isEmpty(idTiers)) {
            return null;
        }

        PRTiersWrapper retValue = null;
        GlobazValueObject vo = PRTiersHelper.getTiersAdresseVOParId(session, idTiers);

        if (vo != null) {
            retValue = new PRTiersWrapper(vo, PRTiersWrapper.TI_PERSONNE_AVS_ADRESSE);
        }

        return retValue;
    }

    /**
     * <p>
     * cherche une "personneAVS" par son identifiant
     * </p>
     *
     * @param session
     * @param idTiers
     *
     * @return
     *
     * @throws Exception
     */
    public static final GlobazValueObject getTiersAdresseVOParId(BISession session, String idTiers) throws Exception {
        if (JadeStringUtil.isEmpty(idTiers)) {
            return null;
        }

        ITIPersonneAvsAdresse personneAVS = (ITIPersonneAvsAdresse) session.getAPIFor(ITIPersonneAvsAdresse.class);
        Hashtable<String, Object> criteres = new Hashtable<String, Object>();

        criteres.put(ITIAbstractAdresseData.FIND_FOR_IDTIERS, idTiers);
        personneAVS.setISession(PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        Object[] result = personneAVS.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else {
            return (GlobazValueObject) result[0];
        }
    }

    public static final PRTiersWrapper getTiersParId(BISession session, String idTiers) throws Exception {
        if (JadeStringUtil.isEmpty(idTiers)) {
            return null;
        }

        if (!session.isConnected()) {
            session = PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS);
        }

        TITiersViewBean t = new TITiersViewBean();
        t.setISession(session);
        t.setIdTiers(idTiers);
        t.retrieve();
        if (t.isNew()) {
            return null;
        } else {
            return new PRTiersWrapper(t);
        }
    }

    public static final PRTiersWrapper getTiersById(BISession session, String idTiers) throws Exception {
        if (JadeStringUtil.isEmpty(idTiers)) {
            return null;
        }

        if (!session.isConnected()) {
            session = PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS);
        }

        TITiersViewBean t = new TITiersViewBean();
        t.setISession(session);
        t.setIdTiers(idTiers);
        t.retrieve(null, true, false);
        if (t.isNew()) {
            return null;
        } else {
            return new PRTiersWrapper(t);
        }
    }

    public static final boolean isCodeOFASCanton(String code) throws Exception {
        return PRTiersHelper.cantonOFAS.containsKey(code);
    }

    /**
     * <p>
     * retourne vrai si l'assuré portant l'identifiant transmis est en age AVS à une date donnée.
     * </p>
     *
     * @param session
     * @param idTiers
     *            l'identifiant de l'assuré dont on veut savoir s'il est rentier AVS
     * @param date
     *            la date à laquelle on veut savoir si l'assuré était, est ou sera rentier AVS.
     *
     * @return
     *
     * @throws Exception
     */
    public static final boolean isRentier(BISession session, String idTiers, String date) throws Exception {
        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, idTiers);
        String dateNaissance = tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);

        if (JAUtil.isDateEmpty(dateNaissance)) {
            return false; // on part du principe que si la date n'est pas renseignée, ce n'est pas un retraité.
        }

        int anneeNaissance = JACalendar.getYear(dateNaissance);
        String csSexe = tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE);

        if (JadeStringUtil.isEmpty(csSexe)) {
            throw new Exception("Erreur : Le sexe du tiers n'est pas renseigné. idTiers = " + idTiers + " nss = "
                    + tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
        }

        int ageAvs = anneeNaissance
                + PRTiersHelper.getAgeAvs(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE), dateNaissance);

        // on construit la date d'age avs
        JADate dateAVS = new JADate(JACalendar.getDay(dateNaissance), JACalendar.getMonth(dateNaissance), ageAvs);

        return ((BSession) session).getApplication().getCalendar().compare(new JADate(date),
                dateAVS) != JACalendar.COMPARE_FIRSTLOWER;
    }

    /**
     * Retourne la commune politique en fonction de l'<code>idTiers</code> et <strong>pour l'année transmise en
     * argument.</strong></br>
     * Si le tiers n'a pas pu être retrouvé en fonction de l'<code>idTiers</code> une
     * <code>TiersNotFoundException</code> sera lancée</br>
     *
     * Si le tiers à bien été retrouvé mais que sa commune politique n'est pas renseignée, ce sera la clé
     * {@link CommunePolitique}.CommunePolitique traduite qui sera retournée</br>
     *
     * @param idTiers L'idTiers à rechercher en DB
     * @param session La session à utiliser pour les accès DB
     * @return La commune politique dans la langue de l'utilisateur si la commune politique à pu être retrouvée sinon
     *         retourne la traduction du label {@link CommunePolitique}.COMMUNE_NOT_FOUND
     * @throws IllegalArgumentException Si l'<code>idTiers</code> est null ou vide OU si la <code>session</code> est
     *             <code>null</code>
     */

    /**
     * Cette méthode fait appel à <code>getCommunePolitique(Set<String> setIdTiers, Date date, BSession session)</code>
     *
     * @param idTiers Ul'idTiers pour lequel la commune politique doit être recherchée
     * @param date La date pour la recherche des périodes dans les liens entre tiers. <strong>La date doit contenir le
     *            jours, le mois et l'année</strong>
     * @param session La session à utiliser. Doit posséder une transaction ouverte
     * @return Une Map contenant les idTiers comme clé et les codes des communes politiques comme valeur.
     * @throws Exception en cas d'arguments incorrectes..
     */
    public static String getCommunePolitique(String idTiers, Date date, BSession session)
            throws IllegalArgumentException {

        Set<String> setIdTiers = new HashSet<String>();
        setIdTiers.add(idTiers);

        Map<String, String> cpMap = getCommunePolitique(setIdTiers, date, session);
        return cpMap.get(idTiers);
    }

    /**
     * Récupère les codes des communes politiques pour la liste d'idTiers <code>idTiers</code></br>
     * La recherche est réalisée part lot de mille idTiers(limitation de la clause SQL 'IN'</br>
     * </br>
     * Si le tiers à bien été retrouvé mais que sa commune politique n'est pas renseignée, ce sera la clé
     * {@link CommunePolitique}.LABEL_COMMUNE_POLITIQUE_NOT_FOUND traduite qui sera retournée</br>
     * </br>
     * Si le tiers à bien été retrouvé mais que plusieurs commune politique sont renseignées pour une même date
     * (chevauchement), ce sera la clé {@link CommunePolitique}.LABEL_COMMUNE_POLITIQUE_PLUSIEURS_RESULTAT traduite qui
     * sera retournée</br>
     *
     *
     * @param idTiers Une list d'idTiers pour lesquels la commune politique doit être recherchée
     * @param date La date pour la recherche des périodes dans les liens entre tiers. <strong>La date doit contenir le
     *            jours, le mois et l'année</strong>
     * @param session La session à utiliser. Doit posséder une transaction ouverte
     * @return Une Map contenant les idTiers comme clé et les codes des communes politiques comme valeur.
     * @throws Exception en cas d'arguments incorrectes..
     */
    public static Map<String, String> getCommunePolitique(Set<String> setIdTiers, Date date, BSession session)
            throws IllegalStateException {

        Map<String, CommunePolitiqueBean> mapCommuneBeanParIdTiers = findCommunePolitique(setIdTiers, date, session);
        Map<String, String> mapCommuneParIdTiers = new HashMap<String, String>();

        for (Map.Entry<String, CommunePolitiqueBean> entry : mapCommuneBeanParIdTiers.entrySet()) {
            mapCommuneParIdTiers.put(entry.getKey(), entry.getValue().getCode());
        }

        return mapCommuneParIdTiers;
    }

    /**
     * Récupère un objet {@link CommunePolitiqueBean} pour la liste d'idTiers <code>idTiers</code></br>
     * La recherche est réalisée part lot de mille idTiers(limitation de la clause SQL 'IN'</br>
     * </br>
     * Si le tiers à bien été retrouvé mais que sa commune politique n'est pas renseignée, ce sera la clé
     * {@link CommunePolitique}.LABEL_COMMUNE_POLITIQUE_NOT_FOUND traduite qui sera retournée</br>
     * </br>
     * Si le tiers à bien été retrouvé mais que plusieurs commune politique sont renseignées pour une même date
     * (chevauchement), ce sera la clé {@link CommunePolitique}.LABEL_COMMUNE_POLITIQUE_PLUSIEURS_RESULTAT traduite qui
     * sera retournée</br>
     *
     *
     * @param idTiers Une list d'idTiers pour lesquels la commune politique doit être recherchée
     * @param date La date pour la recherche des périodes dans les liens entre tiers. <strong>La date doit contenir le
     *            jours, le mois et l'année</strong>
     * @param session La session à utiliser. Doit posséder une transaction ouverte
     * @return Une Map contenant les idTiers comme clé et les {@link CommunePolitiqueBean} comme valeur.
     * @throws Exception en cas d'arguments incorrectes..
     */
    public static Map<String, CommunePolitiqueBean> findCommunePolitique(Set<String> setIdTiers, Date date,
            BSession session) throws IllegalStateException {
        Map<String, CommunePolitiqueBean> mapCommuneParIdTiers = new HashMap<String, CommunePolitiqueBean>();

        if (setIdTiers == null) {
            throw new IllegalArgumentException("Argument [idTiers] can not be null or empty");
        }
        if (setIdTiers.size() == 0) {
            return mapCommuneParIdTiers;
        }
        if (date == null) {
            throw new IllegalArgumentException("Argument [date] can not be null");
        }
        if (session == null) {
            throw new IllegalArgumentException("Argument [session] can not be null");
        }

        BTransaction transaction = session.getCurrentThreadTransaction();
        if (transaction == null) {
            throw new IllegalArgumentException("Argument [session] a null transaction");
        }
        if (!transaction.isOpened()) {
            throw new IllegalArgumentException("Argument [session] a non-opened transaction");
        }

        String schema = JadePersistenceUtil.getDbSchema();
        String dateFormatee = new SimpleDateFormat("yyyyMMdd").format(date);

        CommunePolitiqueBean communeNonTrouvee = new CommunePolitiqueBean(
                session.getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_NOT_FOUND.getKey()),
                session.getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_NOT_FOUND_NOM_LONG.getKey()), 1);
        CommunePolitiqueBean plusieursCommuneTrouvees = new CommunePolitiqueBean(
                session.getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_PLUSIEURS_RESULTAT.getKey()),
                session.getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_PLUSIEURS_RESULTAT_NOM_LONG.getKey()), 2);

        List<String> listIdTiers = new ArrayList<String>(setIdTiers);
        StringBuilder startQuery = new StringBuilder();
        startQuery.append("SELECT tiers.HTITIE, administration.HBCADM, tiersAdmin.HTLDE1, tiersAdmin.HTLDE2 ");
        startQuery.append("FROM " + schema + ".TITIERP tiers ");
        startQuery.append(
                "inner join " + schema + ".TICTIEP as compositionTiers ON tiers.HTITIE = compositionTiers.HTITIP ");
        startQuery.append("inner join " + schema
                + ".TITIERP as tiersCompose ON (compositionTiers.HTITIE = tiersCompose.HTITIE) ");
        startQuery.append(
                "inner join " + schema + ".TIADMIP as administration ON  tiersCompose.HTITIE = administration.HTITIE ");
        startQuery.append(
                "inner join " + schema + ".TITIERP as tiersAdmin ON administration.HTITIE = tiersAdmin.HTITIE ");
        startQuery.append("WHERE ((" + dateFormatee
                + " BETWEEN compositionTiers.HGDDRE AND compositionTiers.HGDFRE) OR (compositionTiers.HGDFRE=0 AND compositionTiers.HGDDRE<="
                + dateFormatee + ")) and (compositionTiers.HGTTLI="
                + CommunePolitique.CS_PYXIS_COMMUNE_POLITIQUE.getKey() + ") AND tiers.HTITIE IN (");

        String stopQuery = ")";

        int loopNumber = (setIdTiers.size() / 1000) + 1;

        for (int loop = 0; loop < loopNumber; loop++) {

            BPreparedStatement stat = null;
            try {
                stat = new BPreparedStatement(transaction);
                StringBuilder query = new StringBuilder();
                int start = loop * 1000;
                int stop = start + 1000;
                if (stop > listIdTiers.size()) {
                    stop = listIdTiers.size();
                }

                for (int ct = start; ct < stop; ct++) {
                    query.append(listIdTiers.get(ct) + ",");
                }
                query.replace(query.length() - 1, query.length(), "");// remove la dernière virgule
                String finalQuery = startQuery.toString() + query + stopQuery;

                stat.prepareStatement(finalQuery);
                ResultSet resultSet = stat.executeQuery();
                while (resultSet.next()) {
                    String idTiers = String.valueOf(resultSet.getInt(1));
                    String codeCommune = resultSet.getString(2).trim();
                    String nomCommune = resultSet.getString(3).trim().replace('¬', '\'').replace('¢', '"');
                    nomCommune += " " + resultSet.getString(4).trim().replace('¬', '\'').replace('¢', '"');

                    CommunePolitiqueBean commune = new CommunePolitiqueBean(codeCommune, nomCommune, 0);

                    /*
                     * Si une commune est déjà renseignée pour cette id tiers
                     */
                    if (mapCommuneParIdTiers.containsKey(idTiers)) {
                        // si la valeur est identique on la garde
                        if (commune.equals(mapCommuneParIdTiers.get(idTiers))) {
                            // Nothing to do
                        } else {
                            mapCommuneParIdTiers.remove(idTiers);
                            mapCommuneParIdTiers.put(idTiers, plusieursCommuneTrouvees);
                        }
                    }
                    /*
                     * Si aucune commune politique n'est renseigné pour cette idTiers
                     */
                    else {
                        if (JadeStringUtil.isBlank(codeCommune)) {
                            commune = communeNonTrouvee;
                        }
                        mapCommuneParIdTiers.put(idTiers.trim(), commune);
                    }
                }

            } catch (Exception e) {
                // What can i do with this here ?! Nothing, so y re-throw them
                throw new IllegalStateException("Exception occur when executing request : " + e.toString(), e);
            } finally {
                if (stat != null) {
                    stat.closePreparedStatement();
                }
            }
        }

        /*
         * Due au clause Where de la requête, certains idTiers inclus dans la requêtes ne sont potentiellement pas
         * remonté.
         * Dans ce cas le label LABEL_COMMUNE_POLITIQUE_NOT_FOUND est inséré à la place de la commune politique (qui
         * n'existe pas)
         */
        for (String id : listIdTiers) {
            if (!mapCommuneParIdTiers.containsKey(id)) {
                mapCommuneParIdTiers.put(id, communeNonTrouvee);
            }
        }

        return mapCommuneParIdTiers;
    }

    public static TIAdministrationViewBean resolveAdminFromTiersLanguage(PRTiersWrapper tiers,

            TIAdministrationManager tiAdministrationMgr) {

        TIAdministrationViewBean tiAdministration = null;

        // On prend l'entité égale à la langue du tiers
        for (Iterator<TIAdministrationViewBean> iterAdministration = tiAdministrationMgr.iterator(); iterAdministration
                .hasNext();) {

            TIAdministrationViewBean tiAdmin = iterAdministration.next();

            if ((tiers != null && tiAdmin.getLangue().equals(tiers.getLangue()))) {
                tiAdministration = tiAdmin;
                break;
            }

        }

        if (tiAdministration == null) {
            tiAdministration = (TIAdministrationViewBean) tiAdministrationMgr.getFirstEntity();
        }
        return tiAdministration;
    }

}
