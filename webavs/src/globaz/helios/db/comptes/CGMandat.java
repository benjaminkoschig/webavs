package globaz.helios.db.comptes;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.helios.db.avs.CGSecteurAVS;
import globaz.helios.db.avs.CGSecteurAVSManager;
import globaz.helios.db.bouclement.CGBouclement;
import globaz.helios.db.bouclement.CGBouclementManager;
import globaz.helios.db.classifications.CGClassification;
import globaz.helios.db.classifications.CGClassificationManager;
import globaz.helios.db.classifications.CGDefinitionListe;
import globaz.helios.db.interfaces.CGLibelle;
import globaz.helios.db.interfaces.CGLibelleInterface;
import globaz.helios.db.interfaces.ITreeListable;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;

/**
 * @author gpi
 * @version 1.1 To change this generated comment edit the template variable "typecomment":
 *          Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *          Window>Preferences>Java>Code Generation.
 */
public class CGMandat extends BEntity implements ITreeListable, java.io.Serializable, CGLibelleInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String CS_PC_4CHIFFRES = "715006";
    /**
     * Type plan comptable :
     */
    public static final String CS_PC_5CHIFFRES = "715003";
    /**
     * Type plan comptable :
     */
    public static final String CS_PC_6CHIFFRES = "715004";
    /**
     * Type plan comptable :
     */
    public static final String CS_PC_AUTRE = "715005";
    /**
     * Type plan comptable :
     */
    public static final String CS_PC_AVS = "715001";
    public static final String CS_PC_OFAS = "715007";
    /**
     * Type plan comptable :
     */
    public static final String CS_PC_USAM = "715002";
    /**
     * Recherche mandat :
     */
    public static final String CS_RECHERCHE_NOM = "720001";
    /**
     * Recherche mandat :
     */
    public static final String CS_RECHERCHE_NUMERO = "720002";
    public static final String FIELD_CONTROLECOMPTE1106 = "CONTROLECOMPTE1106";
    public static final String FIELD_ESTCOMPTABILITEAVS = "ESTCOMPTABILITEAVS";
    public static final String FIELD_ESTMANDATCONSOLIDA = "ESTMANDATCONSOLIDA";
    public static final String FIELD_ESTVERROUILLE = "ESTVERROUILLE";
    public static final String FIELD_IDCLASSIFICATIONPR = "IDCLASSIFICATIONPR";
    public static final String FIELD_IDMANDAT = "IDMANDAT";
    public static final String FIELD_IDTIERS = "IDTIERS";
    public static final String FIELD_IDTYPEPLANCOMPTA = "IDTYPEPLANCOMPTA";
    public static final String FIELD_IMPRIMERJOURNALAUT = "IMPRIMERJOURNALAUT";

    public static final String FIELD_IMPRIMERJOURNALMAN = "IMPRIMERJOURNALMAN";
    public static final String FIELD_LIBELLEDE = "LIBELLEDE";

    public static final String FIELD_LIBELLEFR = "LIBELLEFR";

    public static final String FIELD_LIBELLEIT = "LIBELLEIT";
    public static final String FIELD_NOAGENCE = "NOAGENCE";
    public static final String FIELD_NOCAISSE = "NOCAISSE";
    /**
     * Pour recherche par nom :
     */
    public static final String FIELD_NOM_DE = FIELD_LIBELLEDE;
    /**
     * Pour recherche par nom :
     */
    public static final String FIELD_NOM_FR = FIELD_LIBELLEFR;
    /**
     * Pour recherche par nom :
     */
    public static final String FIELD_NOM_IT = FIELD_LIBELLEIT;
    /**
     * Pour recherche par numéro :
     */
    public static final String FIELD_NUMERO = FIELD_IDMANDAT;
    public static final String FIELD_UTILISE650DCMF = "UTILISE650DCMF";
    public static final String FIELD_UTILISELIVRES = "UTILISELIVRES";
    public static final String FIELD_VENTILERCOMPTE1102 = "VENTILERCOMPTE1102";
    private final static String LABEL_PREFIXE = "MANDAT_";
    public static final String MANDAT_AVS_DEFAULT_NUMBER = "900";
    public static final String TABLE_NAME = "CGMANDP";

    /**
     * Method getLibelle.
     * 
     * @param idMandat
     *            id du mandat
     * @return String
     */
    public static String getLibelle(BSession session, String idMandat) {

        CGMandat mandat = new CGMandat();
        mandat.setSession(session);
        String libelle = "";
        mandat.setIdMandat(idMandat);
        try {
            mandat.retrieve();
            if (!mandat.isNew()) {
                libelle = mandat.getLibelle();
            }

        } catch (Exception e) {
            e.printStackTrace();
            libelle = "";
            JadeLogger.error(e, "Exception : CGMandat.getLibelle(int) : " + mandat.getErrors().toString());
        }

        return libelle;
    }

    private Boolean controleCompte1106 = new Boolean(false);
    private String dateDebut = "";
    private String dateFin = "";
    private Boolean estComptabiliteAVS = new Boolean(false);

    private Boolean estMandatConsolidation = new Boolean(false);

    private Boolean estVerrouille = new Boolean(false);
    private String idClassificationPrincipale = "";
    private String idMandat = new String();
    private String idTiers = new String();

    private String idTypePlanComptable = "";
    private Boolean imprimerJournalAutomatique = new Boolean(false);

    private Boolean imprimerJournalManuel = new Boolean(false);

    private String libelleDe = new String();

    private String libelleFr = new String();
    private String libelleIt = new String();
    private String noAgence = null;

    private String noCaisse = null;
    private Boolean utilise605Dcmf = new Boolean(false);

    private Boolean utiliseLivres = new Boolean(false);

    private Boolean ventilerCompte1102 = new Boolean(false);

    /**
     * Commentaire relatif au constructeur AJMandat
     */
    public CGMandat() {
        super();
    }

    @Override
    protected void _afterDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // il faut egalement supprimer les secteurs avs et le bouclement
        // associés au mandat
        CGSecteurAVSManager secteurMgr = new CGSecteurAVSManager();
        secteurMgr.setSession(getSession());
        secteurMgr.setForIdMandat(getIdMandat());
        secteurMgr.find(transaction);
        for (int i = 0; i < secteurMgr.size(); i++) {
            CGSecteurAVS secteur = (CGSecteurAVS) secteurMgr.getEntity(i);
            secteur.delete(transaction);
        }

        CGBouclementManager bclMgr = new CGBouclementManager();
        bclMgr.setSession(getSession());
        bclMgr.setForIdMandat(getIdMandat());
        bclMgr.find(transaction);
        for (int i = 0; i < bclMgr.size(); i++) {
            CGBouclement bcl = (CGBouclement) secteurMgr.getEntity(i);
            bcl.delete(transaction);
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(BTransaction)
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) {
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(BTransaction)
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // on peut supprimer si :
        // 1) le mandat est pas verrouillé
        // 2) aucun exercice comptable associé au mandat

        if (isEstVerrouille().booleanValue()) {
            _addError(transaction, label("SUPPR_ERROR"));
        } else {
            // le mandat n'est pas verrouillé
            if (hasExercices(transaction)) {
                // il y a des exercices, et ils ne sont pas tous cloturé
                _addError(transaction, label("SUPPR_ERROR"));
            }
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(BStatement)
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idMandat = statement.dbReadNumeric(FIELD_IDMANDAT);
        libelleFr = statement.dbReadString(FIELD_LIBELLEFR);
        libelleDe = statement.dbReadString(FIELD_LIBELLEDE);
        libelleIt = statement.dbReadString(FIELD_LIBELLEIT);
        idTiers = statement.dbReadNumeric(FIELD_IDTIERS);
        estMandatConsolidation = statement.dbReadBoolean(FIELD_ESTMANDATCONSOLIDA);
        estComptabiliteAVS = statement.dbReadBoolean(FIELD_ESTCOMPTABILITEAVS);
        ventilerCompte1102 = statement.dbReadBoolean(FIELD_VENTILERCOMPTE1102);
        controleCompte1106 = statement.dbReadBoolean(FIELD_CONTROLECOMPTE1106);
        utilise605Dcmf = statement.dbReadBoolean(FIELD_UTILISE650DCMF);
        imprimerJournalAutomatique = statement.dbReadBoolean(FIELD_IMPRIMERJOURNALAUT);
        imprimerJournalManuel = statement.dbReadBoolean(FIELD_IMPRIMERJOURNALMAN);
        estVerrouille = statement.dbReadBoolean(FIELD_ESTVERROUILLE);
        utiliseLivres = statement.dbReadBoolean(FIELD_UTILISELIVRES);
        idTypePlanComptable = statement.dbReadNumeric(FIELD_IDTYPEPLANCOMPTA);
        idClassificationPrincipale = statement.dbReadNumeric(FIELD_IDCLASSIFICATIONPR);
        noCaisse = statement.dbReadNumeric(FIELD_NOCAISSE);
        noAgence = statement.dbReadNumeric(FIELD_NOAGENCE);

    }

    /**
     * @see globaz.globall.db.BEntity#_validate(BStatement)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {

        // id mandat obligatoire
        if ((idMandat == null) || (JadeStringUtil.isIntegerEmpty(idMandat))) {
            _addError(statement.getTransaction(), label("VALID_ERR_1"));
        }

        // nom du mandat dans la langue de l'application obligatoire
        if (CGLibelle.isAppLanguageLibelleEmpty(this)) {
            _addError(statement.getTransaction(), label("VALID_ERR_2"));
        }

        // adresse obligatoire
        if ((idTiers == null) || (JadeStringUtil.isIntegerEmpty(idTiers))) {
            _addError(statement.getTransaction(), label("VALID_ERR_3"));
        }

        // type de plan comptable : obligatoire
        if ((idTypePlanComptable == null) || (JadeStringUtil.isIntegerEmpty(idTypePlanComptable))) {
            _addError(statement.getTransaction(), label("VALID_ERR_4"));
        }

        // classification principale plausible
        // A FAIRE

        // Controls pour mandats AVS
        if ((isVentilerCompte1102().booleanValue()) && (!isEstComptabiliteAVS().booleanValue())) {
            _addError(statement.getTransaction(), label("VALID_ERR_5"));
        }

        if ((isControleCompte1106().booleanValue()) && (!isEstComptabiliteAVS().booleanValue())) {
            _addError(statement.getTransaction(), label("VALID_ERR_6"));
        }

        if ((isUtilise605Dcmf().booleanValue()) && (!isEstComptabiliteAVS().booleanValue())) {
            _addError(statement.getTransaction(), label("VALID_ERR_7"));
        }

        if (((isEstComptabiliteAVS().booleanValue()) && (!CS_PC_AVS.equals(idTypePlanComptable)))
                || ((!isEstComptabiliteAVS().booleanValue()) && (CS_PC_AVS.equals(idTypePlanComptable)))) {
            _addError(statement.getTransaction(), label("VALID_ERR_8"));
        }

        // Controls pour dates :
        if (JAUtil.isDateEmpty(dateDebut)) {
            _addError(statement.getTransaction(), label("VALID_ERR_9"));
        }

        if (JAUtil.isDateEmpty(dateFin)) {
            _addError(statement.getTransaction(), label("VALID_ERR_10"));
        }

        try {
            if (!BSessionUtil.compareDateFirstLower(getSession(), dateDebut, dateFin)) {
                _addError(statement.getTransaction(), label("VALID_ERR_11"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            _addError(statement.getTransaction(), e.getMessage());
        }

    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(BStatement)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELD_IDMANDAT, _dbWriteNumeric(statement.getTransaction(), getIdMandat(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(BStatement)
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(FIELD_IDMANDAT, _dbWriteNumeric(statement.getTransaction(), getIdMandat(), "idmandat"));
        statement.writeField(FIELD_IDTYPEPLANCOMPTA,
                _dbWriteNumeric(statement.getTransaction(), getIdTypePlanComptable(), "idPlanComptable"));
        statement.writeField(FIELD_LIBELLEFR, _dbWriteString(statement.getTransaction(), getLibelleFr(), "libellefr"));
        statement.writeField(FIELD_LIBELLEDE, _dbWriteString(statement.getTransaction(), getLibelleDe(), "libellede"));
        statement.writeField(FIELD_LIBELLEIT, _dbWriteString(statement.getTransaction(), getLibelleIt(), "libelleit"));
        statement.writeField(FIELD_IDTIERS, _dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idtiers"));
        statement.writeField(
                FIELD_ESTMANDATCONSOLIDA,
                _dbWriteBoolean(statement.getTransaction(), isEstMandatConsolidation(),
                        BConstants.DB_TYPE_BOOLEAN_CHAR, "estmandatconsolidation"));
        statement.writeField(
                FIELD_ESTCOMPTABILITEAVS,
                _dbWriteBoolean(statement.getTransaction(), isEstComptabiliteAVS(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "estcomptabiliteavs"));
        statement.writeField(
                FIELD_VENTILERCOMPTE1102,
                _dbWriteBoolean(statement.getTransaction(), isVentilerCompte1102(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "ventilercompte1102"));
        statement.writeField(
                FIELD_CONTROLECOMPTE1106,
                _dbWriteBoolean(statement.getTransaction(), isControleCompte1106(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "controlecompte1106"));
        statement.writeField(
                FIELD_UTILISE650DCMF,
                _dbWriteBoolean(statement.getTransaction(), isUtilise605Dcmf(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "utilise650dcmf"));
        statement.writeField(
                FIELD_IMPRIMERJOURNALAUT,
                _dbWriteBoolean(statement.getTransaction(), isImprimerJournalAutomatique(),
                        BConstants.DB_TYPE_BOOLEAN_CHAR, "imprimerjournalautomatique"));
        statement.writeField(
                FIELD_IMPRIMERJOURNALMAN,
                _dbWriteBoolean(statement.getTransaction(), isImprimerJournalManuel(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "imprimerjournalmanuel"));
        statement.writeField(
                FIELD_ESTVERROUILLE,
                _dbWriteBoolean(statement.getTransaction(), isEstVerrouille(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "estverrouille"));
        statement.writeField(
                FIELD_UTILISELIVRES,
                _dbWriteBoolean(statement.getTransaction(), isUtiliseLivres(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "utiliselivres"));
        statement.writeField(FIELD_IDCLASSIFICATIONPR,
                _dbWriteNumeric(statement.getTransaction(), getIdClassificationPrincipale(), "idClassification"));
        statement.writeField(FIELD_NOCAISSE, _dbWriteNumeric(statement.getTransaction(), getNoCaisse(), "noCaisse"));
        statement.writeField(FIELD_NOAGENCE, _dbWriteNumeric(statement.getTransaction(), getNoAgence(), "noAgence"));
    }

    /**
     * @see globaz.helios.db.interfaces.ITreeListable#getChilds()
     */
    @Override
    public BManager[] getChilds() throws Exception {

        CGExerciceComptableManager exercice = new CGExerciceComptableManager();
        exercice.setForIdMandat(getIdMandat());

        CGClassificationManager classification = new CGClassificationManager();
        classification.setForIdMandat(getIdMandat());

        CGBouclementManager bouclementManager = new CGBouclementManager();
        bouclementManager.setForIdMandat(getIdMandat());

        CGSecteurAVSManager secteurManager = new CGSecteurAVSManager();
        secteurManager.setForIdMandat(getIdMandat());

        // CGCompteOfasManager cptOfasManager = new CGCompteOfasManager();
        // cptOfasManager.setForIdMandat(getIdMandat());

        return new BManager[] { exercice, classification, bouclementManager, secteurManager };

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.09.2002 15:08:28)
     * 
     * @return String
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.09.2002 15:09:01)
     * 
     * @return String
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.09.2002 14:57:34)
     * 
     * @return String
     */
    public String getIdClassificationPrincipale() {
        return idClassificationPrincipale;
    }

    /**
     * Method getIdMandat.
     * 
     * @return String
     */
    public String getIdMandat() {
        return idMandat;
    }

    /**
     * Method getIdTiers.
     * 
     * @return String
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.09.2002 13:16:40)
     * 
     * @return String
     */
    public String getIdTypePlanComptable() {
        return idTypePlanComptable;
    }

    /**
     * @see globaz.helios.db.interfaces.CGLibelleInterface#getLibelle()
     */
    @Override
    public String getLibelle() {
        return CGLibelle.getLibelleApp(this);
    }

    /**
     * @see globaz.helios.db.interfaces.CGLibelleInterface#getLibelleDe()
     */
    @Override
    public String getLibelleDe() {
        return libelleDe;
    }

    /**
     * @see globaz.helios.db.interfaces.CGLibelleInterface#getLibelleFr()
     */
    @Override
    public String getLibelleFr() {
        return libelleFr;
    }

    /**
     * @see globaz.helios.db.interfaces.CGLibelleInterface#getLibelleIt()
     */
    @Override
    public String getLibelleIt() {
        return libelleIt;
    }

    public String getNoAgence() {
        return noAgence;
    }

    public String getNoCaisse() {
        return noCaisse;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.09.2002 15:07:56)
     * 
     * @return boolean
     */
    private boolean hasExercices(BTransaction transaction) throws Exception {

        // Des exercices associées ?
        CGExerciceComptableManager mgr = new CGExerciceComptableManager();
        mgr.setSession(getSession());
        mgr.setForIdMandat(getIdMandat());
        mgr.find(transaction);

        if (mgr.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method isControleCompte1106.
     * 
     * @return Boolean
     */
    public Boolean isControleCompte1106() {
        return controleCompte1106;
    }

    /**
     * Method isEstComptabiliteAVS.
     * 
     * @return Boolean
     */
    public Boolean isEstComptabiliteAVS() {
        return estComptabiliteAVS;
    }

    /**
     * Method isEstMandatConsolidation.
     * 
     * @return Boolean
     */
    public Boolean isEstMandatConsolidation() {
        return estMandatConsolidation;
    }

    /**
     * Method isEstVerrouille.
     * 
     * @return Boolean
     */
    public Boolean isEstVerrouille() {
        return estVerrouille;

    }

    /**
     * Method isImprimerJournalAutomatique.
     * 
     * @return Boolean
     */
    public Boolean isImprimerJournalAutomatique() {
        return imprimerJournalAutomatique;
    }

    /**
     * Method isImprimerJournalManuel.
     * 
     * @return Boolean
     */
    public Boolean isImprimerJournalManuel() {
        return imprimerJournalManuel;
    }

    public boolean isMandatConsolidation() {
        return isEstMandatConsolidation().booleanValue();
    }

    public boolean isTypePlanComptableOfas() {
        return getIdTypePlanComptable().equals(CGMandat.CS_PC_OFAS);
    }

    /**
     * Method isUtilise605Dcmf.
     * 
     * @return Boolean
     */
    public Boolean isUtilise605Dcmf() {
        return utilise605Dcmf;
    }

    /**
     * Method isUtiliseLivres.
     * 
     * @return Boolean
     */
    public Boolean isUtiliseLivres() {
        return utiliseLivres;
    }

    /**
     * Method isVentilerCompte1102.
     * 
     * @return Boolean
     */
    public Boolean isVentilerCompte1102() {
        return ventilerCompte1102;
    }

    private String label(String code) {
        return getSession().getLabel(LABEL_PREFIXE + code);
    }

    /**
     * Method ouvrir.
     * 
     * @param transaction
     *            transaction
     * @param dateDebut
     *            date de début
     * @param dateFin
     *            date de fin
     * @throws Exception
     *             exception
     */
    public void ouvrir(BTransaction transaction, String dateDebut, String dateFin) throws Exception {

        CGClassification classification = null;
        CGDefinitionListe liste = null;
        CGBouclement bouclement = null;

        if (isEstComptabiliteAVS().booleanValue()) {

            // creation d'un exercice comptable initial
            CGExerciceComptable exerciceComptable = new CGExerciceComptable();

            exerciceComptable.setDateDebut(dateDebut);
            exerciceComptable.setDateFin(dateFin);
            exerciceComptable.setIdMandat(idMandat);
            exerciceComptable.setMandat(this);
            exerciceComptable.setEstNouveauMandat(true);
            exerciceComptable.setSession(getSession());
            exerciceComptable.add(transaction);

            // A) MANDAT AVS
            // ==========

            // 1a) création d'une classification par défaut
            classification = new CGClassification();

            // On interdit la création automatique d'une définition de liste par
            // défaut,
            // car on va la créer nous même
            classification.setIsCreateDefaultDefinitionListe(new Boolean(false));

            classification.setSession(getSession());
            classification.setIdMandat(idMandat);
            classification.setIdTypeClassification(CGClassification.CS_TYPE_AVS_COMPTE);
            classification.setLibelleFr("Classification par compte");
            classification.setLibelleIt("Classificazione per il cliente");
            classification.setLibelleDe("Kontosklassifikation");
            classification.add(transaction);
            String idClassificationCompte = classification.getIdClassification();

            classification = new CGClassification();

            // On interdit la création automatique d'une définition de liste par
            // défaut,
            // car on va la créer nous même
            classification.setIsCreateDefaultDefinitionListe(new Boolean(false));

            classification.setSession(getSession());
            classification.setIdMandat(idMandat);
            classification.setIdTypeClassification(CGClassification.CS_TYPE_AVS_SECTEUR);
            classification.setLibelleFr("Classification par secteur");
            classification.setLibelleIt("Classificazione per il settore");
            classification.setLibelleDe("Bereichsklassifikation");
            classification.add(transaction);
            String idClassificationSecteur = classification.getIdClassification();

            setIdClassificationPrincipale(idClassificationSecteur);

            // 2a) création d'une liste par défaut
            liste = new CGDefinitionListe();
            liste.setSession(getSession());
            liste.setLibelleFr("Liste par compte");
            liste.setLibelleDe("Liste nach Konto");
            liste.setLibelleIt("Lista dal cliente");
            liste.setIdClassification(idClassificationCompte);
            liste.add(transaction);

            liste = new CGDefinitionListe();
            liste.setSession(getSession());
            liste.setLibelleFr("Liste par secteur");
            liste.setLibelleDe("Liste nach Buchungskreis");
            liste.setLibelleIt("Lista dal settore");
            liste.setIdClassification(idClassificationSecteur);
            liste.add(transaction);

            // 3a) création d'une définition de bouclement par défaut
            bouclement = new CGBouclement();
            bouclement.setSession(getSession());
            bouclement.setIdMandat(idMandat);
            bouclement.setIdTypeBouclement(CGBouclement.CS_BOUCLEMENT_MENSUEL_AVS);
            bouclement.setLibelleFr("Bouclement mensuel");
            bouclement.setLibelleDe("Monatliches Abschluss");
            bouclement.setLibelleIt("Fine mensile");

            bouclement.setIsImprimerBalance(new Boolean(true));
            bouclement.setIsImprimerBalanceMvt(new Boolean(false));
            bouclement.setIsImprimerBilan(new Boolean(true));
            bouclement.setIsImprimerGrandLivre(new Boolean(false));
            bouclement.setIsImprimerPP(new Boolean(true));
            bouclement.setIsImprimerReleveAvs(new Boolean(true));

            bouclement.add(transaction);

            bouclement = new CGBouclement();
            bouclement.setSession(getSession());
            bouclement.setIdMandat(idMandat);
            bouclement.setIdTypeBouclement(CGBouclement.CS_BOUCLEMENT_ANNUEL_AVS);
            bouclement.setLibelleFr("Bouclement annuel");
            bouclement.setLibelleDe("Jährlich Abschluss");
            bouclement.setLibelleIt("Fine annuale ");

            bouclement.setIsImprimerBalance(new Boolean(true));
            bouclement.setIsImprimerBalanceMvt(new Boolean(true));
            bouclement.setIsImprimerBilan(new Boolean(true));
            bouclement.setIsImprimerGrandLivre(new Boolean(true));
            bouclement.setIsImprimerPP(new Boolean(true));
            bouclement.setIsImprimerReleveAvs(new Boolean(true));

            bouclement.add(transaction);

            // 4a) Ouverture des secteurs comptables AVS de base

            String[] ids = { "1000", "1990", "2000", "2110", "2120", "2130", "2140", "2160", "2990", "9000", "9100",
                    "9990" };

            for (int i = 0; i < ids.length; i++) {
                CGSecteurAVS secteurAvs = new CGSecteurAVS();
                secteurAvs.setSession(getSession());
                secteurAvs.setIdSecteurAVS(ids[i]);
                secteurAvs.setIdTypeTache(CGSecteurAVS.CS_TACHE_FEDERAL);
                // permet la création automatique du plan comptable et du
                // secteur selon secteur.xml
                secteurAvs.setIsCreationAutomatique(new Boolean(true));
                secteurAvs.ouvrir(transaction, exerciceComptable, idClassificationCompte, idClassificationSecteur);
            }
        } else {

            // B) MANDAT NON AVS
            // ===============
            // 3b) création d'une définition de bouclement par défaut
            bouclement = new CGBouclement();
            bouclement.setSession(getSession());

            if (isTypePlanComptableOfas()) {
                bouclement.setIdTypeBouclement(CGBouclement.CS_BOUCLEMENT_ANNUEL_AVS);
            } else {
                bouclement.setIdTypeBouclement(CGBouclement.CS_BOUCLEMENT_ANNUEL);
            }

            bouclement.setLibelleFr("Bouclement annuel");
            bouclement.setLibelleDe("Jährlich Abschluss");
            bouclement.setLibelleIt("Fine mensile");

            bouclement.setIdMandat(idMandat);
            bouclement.add(transaction);

            if (isTypePlanComptableOfas()) {
                CGBouclement bouclementMensuel = new CGBouclement();
                bouclementMensuel.setSession(getSession());

                bouclementMensuel.setIdTypeBouclement(CGBouclement.CS_BOUCLEMENT_MENSUEL_AVS);
                bouclementMensuel.setLibelleFr("Bouclement mensuel");
                bouclementMensuel.setLibelleDe("Monatliches Abschluss");
                bouclementMensuel.setLibelleIt("Fine mensile");

                bouclementMensuel.setIdMandat(idMandat);
                bouclementMensuel.add(transaction);
            }

            // creation d'un exercice comptable initial
            CGExerciceComptable exerciceComptable = new CGExerciceComptable();

            exerciceComptable.setDateDebut(dateDebut);
            exerciceComptable.setDateFin(dateFin);
            exerciceComptable.setIdMandat(idMandat);
            exerciceComptable.setMandat(this);
            exerciceComptable.setEstNouveauMandat(true);
            exerciceComptable.setSession(getSession());
            exerciceComptable.add(transaction);

            classification = new CGClassification();

            // On interdit la création automatique d'une définition de liste par
            // défaut,
            // car on va la créer nous même
            classification.setIsCreateDefaultDefinitionListe(new Boolean(false));

            classification.setSession(getSession());
            classification.setLibelleFr("Classification principale");
            classification.setLibelleIt("Classificazione principale");
            classification.setLibelleDe("Hauptklassifikation");
            classification.setIdMandat(idMandat);

            if (getIdTypePlanComptable() == CGMandat.CS_PC_USAM) {
                // 1b) création d'une classification par défaut
                classification.setIdTypeClassification(CGClassification.CS_TYPE_USAM);
                // TODO
                // To complete with PLAN COMPTABLE USAM .....
            } else {
                // 1b) création d'une classification par défaut
                classification.setIdTypeClassification(CGClassification.CS_TYPE_DOMAINE);
            }
            classification.add(transaction);
            setIdClassificationPrincipale(classification.getIdClassification());

            // 2b) création d'une liste par défaut
            liste = new CGDefinitionListe();
            liste.setSession(getSession());
            liste.setLibelleFr("Liste par défaut");
            liste.setLibelleDe("Abgerundete Liste");
            liste.setLibelleIt("Lista di difetto");
            liste.setIdClassification(classification.getIdClassification());
            liste.add(transaction);

            if (!isTypePlanComptableOfas()) {
                CGPeriodeComptableManager periodeManager = new CGPeriodeComptableManager();
                periodeManager.setForIdExerciceComptable(exerciceComptable.getIdExerciceComptable());
                periodeManager.setSession(getSession());
                periodeManager.find(transaction);
                if (periodeManager.size() != 1) {
                    // ne doit jammais arriver
                    _addError(transaction, label("ERROR_CREATION"));
                } else {
                    CGPeriodeComptable periode = (CGPeriodeComptable) periodeManager.getEntity(0);
                    periode.setSession(getSession());
                    periode.setIdBouclement(bouclement.getIdBouclement());
                    periode.update(transaction);
                }
            }
        }
    }

    /**
     * Method setControleCompte1106.
     * 
     * @param newControleCompte1106
     *            Boolean
     */
    public void setControleCompte1106(Boolean newControleCompte1106) {
        controleCompte1106 = newControleCompte1106;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.09.2002 15:08:28)
     * 
     * @param newDateDebut
     *            String
     */
    public void setDateDebut(String newDateDebut) {
        dateDebut = newDateDebut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.09.2002 15:09:01)
     * 
     * @param newDateFin
     *            String
     */
    public void setDateFin(String newDateFin) {
        dateFin = newDateFin;
    }

    /**
     * Method setEstComptabiliteAVS.
     * 
     * @param newEstComptabiliteAVS
     *            Boolean
     */
    public void setEstComptabiliteAVS(Boolean newEstComptabiliteAVS) {
        estComptabiliteAVS = newEstComptabiliteAVS;
    }

    /**
     * Method setEstMandatConsolidation.
     * 
     * @param newEstMandatConsolidation
     *            Boolean
     */
    public void setEstMandatConsolidation(Boolean newEstMandatConsolidation) {
        estMandatConsolidation = newEstMandatConsolidation;
    }

    /**
     * Method setEstVerrouille.
     * 
     * @param newEstVerrouille
     *            Boolean
     */
    public void setEstVerrouille(Boolean newEstVerrouille) {
        estVerrouille = newEstVerrouille;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.09.2002 14:57:34)
     * 
     * @param newIdClassificationPrincipale
     *            String
     */
    public void setIdClassificationPrincipale(String newIdClassificationPrincipale) {
        idClassificationPrincipale = newIdClassificationPrincipale;
    }

    /**
     * Method setIdMandat.
     * 
     * @param newIdMandat
     *            String
     */
    public void setIdMandat(String newIdMandat) {
        idMandat = newIdMandat;
    }

    /**
     * Method setIdTiers.
     * 
     * @param newIdTiers
     *            String
     */
    public void setIdTiers(String newIdTiers) {
        idTiers = newIdTiers;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.09.2002 13:16:40)
     * 
     * @param newIdTypePlanComptable
     *            String
     */
    public void setIdTypePlanComptable(String newIdTypePlanComptable) {
        idTypePlanComptable = newIdTypePlanComptable;
    }

    /**
     * Method setImprimerJournalAutomatique.
     * 
     * @param newImprimerJournalAutomatique
     *            Boolean
     */
    public void setImprimerJournalAutomatique(Boolean newImprimerJournalAutomatique) {
        imprimerJournalAutomatique = newImprimerJournalAutomatique;
    }

    /**
     * Method setImprimerJournalManuel.
     * 
     * @param newImprimerJournalManuel
     *            Boolean
     */
    public void setImprimerJournalManuel(Boolean newImprimerJournalManuel) {
        imprimerJournalManuel = newImprimerJournalManuel;
    }

    /**
     * Method setLibelleDe.
     * 
     * @param newLibelleDe
     *            String
     */
    public void setLibelleDe(String newLibelleDe) {
        libelleDe = newLibelleDe;
    }

    /**
     * Method setLibelleFr.
     * 
     * @param newLibelleFr
     *            String
     */
    public void setLibelleFr(String newLibelleFr) {
        libelleFr = newLibelleFr;
    }

    /**
     * Method setLibelleIt.
     * 
     * @param newLibelleIt
     *            String
     */
    public void setLibelleIt(String newLibelleIt) {
        libelleIt = newLibelleIt;
    }

    public void setNoAgence(String noAgence) {
        this.noAgence = noAgence;
    }

    public void setNoCaisse(String noCaisse) {
        this.noCaisse = noCaisse;
    }

    /**
     * Method setUtilise605Dcmf.
     * 
     * @param newUtilise605Dcmf
     *            Boolean
     */
    public void setUtilise605Dcmf(Boolean newUtilise605Dcmf) {
        utilise605Dcmf = newUtilise605Dcmf;
    }

    /**
     * Method setUtiliseLivres.
     * 
     * @param newUtiliseLivres
     *            Boolean
     */
    public void setUtiliseLivres(Boolean newUtiliseLivres) {
        utiliseLivres = newUtiliseLivres;
    }

    /**
     * Method setVentilerCompte1102.
     * 
     * @param newVentilerCompte1102
     *            Boolean
     */
    public void setVentilerCompte1102(Boolean newVentilerCompte1102) {
        ventilerCompte1102 = newVentilerCompte1102;
    }
}
