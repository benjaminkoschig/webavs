package globaz.musca.db.facturation;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.api.IFAEnteteFacture;
import globaz.musca.api.IFAPrintDoc;
import globaz.musca.application.FAApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import globaz.osiris.api.APISection;
import globaz.osiris.api.APISectionDescriptor;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoireManager;
import globaz.osiris.external.IntRole;
import globaz.pyxis.api.ITIPersonneAvs;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.api.osiris.TITiersOSI;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiement;
import globaz.webavs.common.ICommonConstantes;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Vector;

public class FAEnteteFacture extends BEntity implements IFAEnteteFacture, Serializable, IFAPrintDoc {

    private static final long serialVersionUID = -3034150363772847158L;

    /**
     * Clé alternée: idPassage, idRole, idExterneRole, idTypeFacture, idExterneFacture
     */
    public static final int ALT_KEY_PASSAGE_ROLE = 1;

    public final static String CS_AUCUN_RECOUVREMENT = "907001";
    public final static String CS_MODE_AUTOMATIQUE = "907005";
    // Mode de recouvrement
    public final static String CS_MODE_BVR = "907002";
    public final static String CS_MODE_FORCEE_REMBOURSEMENT = "907008";
    public final static String CS_MODE_IMP_PASIMPZERO = "914003";
    public final static String CS_MODE_IMP_SEPAREE = "914002";
    public final static String CS_MODE_IMP_STANDARD = "914001";
    public final static String CS_MODE_IMP_NOT_IMPRIMABLE = "914004";
    public final static String CS_MODE_RECOUVREMENT_DIRECT = "907004";

    public final static String CS_MODE_REMBOURSEMENT = "907003";
    public final static String CS_MODE_RETENU = "907006";
    public final static String CS_MODE_RETENU_COMPTE_ANNEX_BLOQUE = "907007";

    // Remboursement de cotisation
    public final static String CS_REMBOURSEMENT_COTI = "519010";
    // ::BTC:: ATTENTION Remboursement de cotisation
    public final static String CS_REMBOURSEMENT_COTI2 = "519006";
    // Tri décompte passageVB
    public final static String CS_TRI_DEBITEUR = "909001";
    // Tri décompte
    public final static String CS_TRI_DECOMPTE_POSITIF = "910001";
    public final static String CS_TRI_DECOMPTE_RECOUVREMENT = "910002";
    public final static String CS_TRI_DECOMPTE_SANSRECOUVREMENT = "910003";
    public final static String CS_TRI_DECOMPTE_TOUS = "910008";
    public final static String CS_TRI_DECOMPTE_ZERO = "910004";
    public final static String CS_TRI_MONTANT = "909004";
    public final static String CS_TRI_NOM = "909002";
    public final static String CS_TRI_NOTECREDIT = "910005";
    public final static String CS_TRI_NOTECREDIT_REMBOURSEMENT = "910006";
    public final static String CS_TRI_NOTECREDIT_SANSREMBOURSEMENT = "910007";
    public final static String CS_TRI_NUMERO_DECOMTPE = "909003";
    public final static String FIELD_AAP_ADRESSE_PAYEMENT = "HCIADP";
    public final static String FIELD_AAP_DOMAINE_APPLICATION = "HFIAPP";
    public final static String FIELD_AAP_FIN_VALIDITE = "HCDFRE";
    public final static String FIELD_ESTRENTIERNA = "ESTRENTIER";
    public static final String FIELD_HTLDE1 = "HTLDE1";

    public static final String FIELD_HTLDE2 = "HTLDE2";

    public final static String FIELD_ID_CONTROLE = "IDCONTROLE";
    public final static String FIELD_ID_DOMAINE_COURRIER = "EBIDOC";
    public final static String FIELD_ID_DOMAINE_LVS = "EBIDOL";
    public final static String FIELD_ID_DOMAINE_REMBOURSEMENT = "EBIDOR";
    public final static String FIELD_ID_TYPE_COURRIER = "EBITYC";
    public static final String FIELD_IDADRESSE = "IDADRESSE";
    public static final String FIELD_IDADRESSEPAIEMENT = "IDADRESSEPAIEMENT";

    public static final String FIELD_IDENTETEFACTURE = "IDENTETEFACTURE";
    public static final String FIELD_IDEXTERNEFACTURE = "IDEXTERNEFACTURE";
    public static final String FIELD_IDEXTERNEROLE = "IDEXTERNEROLE";
    public static final String FIELD_IDMODERECOUVREMENT = "IDMODERECOUVREMENT";
    public static final String FIELD_IDPASSAGE = "IDPASSAGE";
    public static final String FIELD_IDREMARQUE = "IDREMARQUE";

    public static final String FIELD_IDROLE = "IDROLE";
    public static final String FIELD_IDSOUINTMOR = "IDSOUINTMOR";
    public static final String FIELD_IDSOUSTYPE = "IDSOUSTYPE";
    public static final String FIELD_IDTIERS = "IDTIERS";
    public static final String FIELD_IDTYPEFACTURE = "IDTYPEFACTURE";
    public static final String FIELD_LIBELLE = "LIBELLE";
    public static final String FIELD_MODIMP = "MODIMP";
    public static final String FIELD_MOTIFINTMOR = "MOTIFINTMOR";

    public static final String FIELD_NONIMPRIMABLE = "NONIMPRIMABLE";
    public static final String FIELD_NUMCOMMUNE = "NUMCOMMUNE";

    public static final String FIELD_REFCOLLABORATEUR = "REFCOLLABORATEUR";
    public static final String FIELD_REFERENCE_FACTURE = "REFERENCEFACTURE";

    public static final String FIELD_EBILL_TRANSACTION_ID = "EBILLTRANSACTIONID";
    public static final String FIELD_TOTALFACTURE = "TOTALFACTURE";
    public final static String TABLE_ADRESSE_PAYEMENT = "TIADRPP"; // AP

    public final static String TABLE_AVOIR_ADRESSE_PAYEMENT = "TIAPAIP"; // AAP

    public static final String TABLE_FAENTFP = "FAENTFP";

    public final static String TABLE_FIELDS = FAEnteteFacture.TABLE_FAENTFP + "."
            + FAEnteteFacture.FIELD_IDENTETEFACTURE + ", " + FAEnteteFacture.TABLE_FAENTFP + "."
            + FAEnteteFacture.FIELD_IDPASSAGE + ", " + FAEnteteFacture.TABLE_FAENTFP + "."
            + FAEnteteFacture.FIELD_IDREMARQUE + ", " + "" + FAEnteteFacture.TABLE_FAENTFP + "."
            + FAEnteteFacture.FIELD_IDTIERS + ", " + FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_IDROLE
            + ", " + FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_IDEXTERNEROLE + ", " + ""
            + FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_IDADRESSE + ", "
            + FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_IDTYPEFACTURE + ", "
            + FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_IDSOUSTYPE + ", "
            + FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_IDEXTERNEFACTURE + ", "
            + FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_NUMCOMMUNE + ", "
            + FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_NONIMPRIMABLE + ", "
            + FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_TOTALFACTURE + ", "
            + FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_IDSOUINTMOR + ", "
            + FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_MOTIFINTMOR + ", "
            + FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_IDADRESSEPAIEMENT + ", "
            + FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_IDMODERECOUVREMENT + ", "
            + FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_LIBELLE + ", "
            + FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_REFCOLLABORATEUR + ", "
            + FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_REFERENCE_FACTURE + ", "
            + FAEnteteFacture.TABLE_FAENTFP + ".PSPY AS PSPY, " + FAEnteteFacture.TABLE_FAENTFP + "."
            + FAEnteteFacture.FIELD_ID_DOMAINE_COURRIER + ", " + FAEnteteFacture.TABLE_FAENTFP + "."
            + FAEnteteFacture.FIELD_ID_TYPE_COURRIER + ", " + FAEnteteFacture.TABLE_FAENTFP + "."
            + FAEnteteFacture.FIELD_ID_DOMAINE_LVS + ", " + FAEnteteFacture.TABLE_FAENTFP + "."
            + FAEnteteFacture.FIELD_ID_DOMAINE_REMBOURSEMENT + ", " + FAEnteteFacture.TABLE_FAENTFP + "."
            + FAEnteteFacture.FIELD_ESTRENTIERNA + ", " + FAEnteteFacture.TABLE_FAENTFP + "."
            + FAEnteteFacture.FIELD_ID_CONTROLE + ", " + FAEnteteFacture.TABLE_FAENTFP + "."
            + FAEnteteFacture.FIELD_EBILL_TRANSACTION_ID;

    private ITITiers _tiers = null;
    private String dateReceptionDS = "";
    private String espion = "";
    private Boolean estRentierNa = new Boolean(false);
    private Boolean forceDelete = new Boolean(false);
    private String idAdresse = "";
    private String idAdressePaiement = "";
    // id affiliation
    private String idAffiliation = null;
    private String idControle = "";
    private String idCSModeImpression = FAEnteteFacture.CS_MODE_IMP_STANDARD;
    private String idDomaineCourrier = "";
    private String idDomaineLSV = "";
    private String idDomaineRemboursement = "";
    private String idEntete = "";
    private String idExterneFacture = "";
    private String idExterneRole = "";
    private String idModeRecouvrement = "";
    private String idPassage = "";
    private String idRemarque = "";
    private String idRole = IntRole.ROLE_AFFILIE;
    private String idSoumisInteretsMoratoires = "";
    private String idSousType = "";
    private String idTiAdressePaiement = "";
    private String idTiers = "";
    private String idTypeCourrier = "";
    private String idTypeFacture = "1"; // Le type de facture par défaut est 1
    private String langueTiers = null;
    private String libelle = "";
    private String motifInteretsMoratoires = "";
    private Boolean nonImprimable = new Boolean(false);
    private String numCommune = "";
    // REMOVED private FAPassageViewBean passageVB = null;
    private FAPassage passage = null;
    private boolean processusMasse = false;
    private String referenceFacture = "";
    private String remarque = "";
    private String eBillTransactionID = "";

    private String tierDesignation1 = "";

    private String tierDesignation2 = "";

    private String totalFacture = "";

    private boolean useEntityForLSV = false;

    /**
     * Commentaire relatif au constructeur FAEnteteFacture
     */
    public FAEnteteFacture() {
        super();
    }

    /*
     * Traitement après suppression
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws java.lang.Exception {
        // Suppression des remarques
        if ((!globaz.jade.client.util.JadeStringUtil.isBlank(idRemarque)) && (!idRemarque.equalsIgnoreCase("0"))) {
            FARemarque rema = new FARemarque();
            rema.setSession(transaction.getSession());
            rema.setIdRemarque(getIdRemarque());
            try {
                rema.retrieve(transaction);
                rema.delete(transaction);
            } catch (Exception e) {
                _addError(transaction,
                        "Erreur lors de la suppression de la remarque: idEntete " + getIdEntete() + e.getMessage());
            }
        }

        // Suppression des afacts
        FAAfactManager afactManager = new FAAfactManager();
        afactManager.setSession(getSession());
        afactManager.setForIdEnteteFacture(getIdEntete());
        try {
            afactManager.find(transaction);
            for (int i = 0; i < afactManager.size(); i++) {
                FAAfact afact = (FAAfact) afactManager.getEntity(i);
                try {
                    afact.setForceDelete(new Boolean("true"));
                    afact.delete(transaction);
                } catch (Exception e) {
                    _addError(transaction,
                            "Erreur lors de la suppression des afacts: idEntete " + getIdEntete() + e.getMessage());
                }
            }
        } catch (Exception e) {
            _addError(transaction, "Erreur lors de la lecture des afacts: idEntete " + getIdEntete() + e.getMessage());
        }
    }

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        if (JadeStringUtil.isEmpty(remarque)) {
            // Recherche de la remarque
            setRemarque(FARemarque.getRemarque(getIdRemarque(), transaction));
        }
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdEntete(this._incCounter(transaction, "0"));
        // Valeurs par défaut
        initDefaultValues();
        // Mise à jour de la remarque
        /* ADDED */String remarque = this.getRemarque(transaction);
        /* CHANGED */if (!JadeStringUtil.isBlank(remarque)) {
            FARemarque rem = new FARemarque();
            rem.setSession(transaction.getSession());
            /* CHANGED */rem.setTexte(remarque);
            try {
                rem.add(transaction);
                setIdRemarque(rem.getIdRemarque());
            } catch (Exception e) {
                _addError(transaction, "Erreur lors de l'ajout de la remarque. idEntete " + getIdEntete());
            }
        }
        // Contrôle s'il y a déjà le même décompte dans le passage
        // inutile de faire dans le cas du process de masse de la facturation pétiodique car ce test est déjà effectué
        // dans le processus
        if (isProcessusMasse() == false) {
            FAEnteteFactureManager entete = new FAEnteteFactureManager();
            entete.setSession(getSession());
            entete.setForIdPassage(idPassage);
            entete.setForIdExterneFacture(idExterneFacture);
            entete.setForIdExterneRole(idExterneRole);
            entete.setForIdRole(idRole);
            entete.setForIdTiers(idTiers);
            if (entete.getCount() > 0) {
                _addError(transaction, "Le décompte " + idExterneFacture
                        + " existe déja dans le passage pour l'affilié " + idExterneRole);
            }
        }
    }

    /**
     * Effectue des traitements avant une suppression de la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements avant la suppression de l'entité de la BD
     * <p>
     * L'exécution de la suppression n'est pas effectuée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_beforeDelete()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws java.lang.Exception {

        /**
         * Un IM référence le décompte qui a permis son calcul (décompte principal)
         * 
         * Pour certaines caisses (ccvd par exemple) l'IM génère en plus un décompte 5000 avec un afact qui représente
         * le montant de l'IM
         * 
         * Pour d'autres caisses (ccju par exemple) les afacts représentant l'IM sont directement ajoutés au décompte
         * principal
         * 
         * Afin de pouvoir supprimer le décomtpe 5000 l'utilisateur doit obligatoirement exempté l'IM
         * 
         * Ce code supprime un décompte et l'IM qui lui est lié Pour autant que cet IM soit exempté
         * 
         */
        try {
            BSession sessionForLabel = (BSession) GlobazSystem.getApplication(FAApplication.DEFAULT_APPLICATION_MUSCA)
                    .newSession();

            CAInteretMoratoireManager imManager = new CAInteretMoratoireManager();
            imManager.setSession(getSession());
            imManager.setForIdJournalFacturation(getIdPassage());
            imManager.setForIdSectionFacture(getIdEntete());
            imManager.find(transaction, BManager.SIZE_NOLIMIT);

            for (int i = 1; i <= imManager.size(); i++) {
                CAInteretMoratoire im = (CAInteretMoratoire) imManager.getEntity(i - 1);

                if (im == null) {
                    throw new Exception(FWMessageFormat.format(sessionForLabel.getLabel("IM_A_INDICE_MANAGER_NULL"),
                            i - 1));
                }
                if (!CAInteretMoratoire.CS_EXEMPTE.equalsIgnoreCase(im.getMotifcalcul())) {
                    throw new Exception(FWMessageFormat.format(
                            sessionForLabel.getLabel("EXEMPTER_IM_AVANT_SUPPRESSION_DECOMPTE"), getIdEntete()));
                }
                im.setIdJournalFacturation("");
                im.delete(transaction);
            }
        } catch (Exception e) {
            _addError(transaction, FAEnteteFacture.class.getName() + " - " + e.toString());
        }

        // Vérifier l'état du passageVB
        if (!getForceDelete().booleanValue()
                && _getPassage(transaction).getStatus().equals(FAPassage.CS_ETAT_COMPTABILISE)) {
            _addError(transaction, "Impossible de supprimer une facture comptabilisée " + getIdExterneRole());
        } else {
            FAAfactManager afaManager = new FAAfactManager();
            afaManager.setISession(getSession());
            afaManager.setForIdPassage(getIdPassage());
            afaManager.setForIdEnteteFacture(getIdEntete());
            afaManager.find(transaction);
            if (afaManager.getSize() > 0) {
                if (!getForceDelete().booleanValue()) {
                    _addError(transaction, "Le décompte contient des afacts, suppression impossible. idEntete"
                            + getIdEntete());
                } else {
                    for (int i = 0; i < afaManager.size(); i++) {
                        FAAfact afa = (FAAfact) afaManager.getEntity(i);
                        afa.setForceDelete(new Boolean("true"));
                        afa.delete(transaction);
                    }
                }
            }
        }
        // Po 9266
        if (AFUtil.returnNombreReleve(getSession(), getIdEntete(), getIdExterneRole(),
                CodeSystem.ETATS_RELEVE_COMPTABILISER) > 0) {
            _addError(transaction, getSession().getLabel("CONTROLE_SUP_RELEVE"));
        } else if (AFUtil.returnNombreReleve(getSession(), getIdEntete(), getIdExterneRole(),
                CodeSystem.ETATS_RELEVE_FACTURER) > 0) {
            _addError(transaction, getSession().getLabel("CONTROLE_SUP_RELEVE1"));
        }

    }

    /*
     * Traitement avant mise à jour
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws java.lang.Exception {
        // Mise à jour de la remarque
        FARemarque rem = new FARemarque();
        rem.setSession(getSession());
        rem.setTexte(this.getRemarque());
        // rem.setTexte(FARemarque.getRemarque(getIdRemarque(), transaction));
        try {
            if ((!globaz.jade.client.util.JadeStringUtil.isBlank(getIdRemarque()) && (!getIdRemarque()
                    .equalsIgnoreCase("0")))) {
                rem.setIdRemarque(getIdRemarque());
                transaction.disableSpy();
                rem.update(transaction);
                transaction.enableSpy();
            } else if (!globaz.jade.client.util.JadeStringUtil.isBlank(this.getRemarque())) {
                rem.add(transaction);
                setIdRemarque(rem.getIdRemarque());
            }
            FAEnteteFacture entete = new FAEnteteFacture();
            entete.setSession(transaction.getSession());
            entete.setIdEntete(getIdEntete());
            entete.retrieve();
        } catch (Exception e) {
            _addError(transaction, "Erreur lors de la mise à jour de la remarque. idEntete " + getIdEntete());
        }
        if (getIdModeRecouvrement().equals(FAEnteteFacture.CS_MODE_FORCEE_REMBOURSEMENT)
                && JadeStringUtil.isBlankOrZero(getIdAdressePaiement())) {
            _addError(transaction, "Il n'y a pas d'adresse de paiement pour l'affilié. Num Affilié "
                    + getIdExterneRole() + " Num Facture " + getIdExterneFacture());
        }
    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return FAEnteteFacture.TABLE_FIELDS
                + ", TITIERP.HTLDE1, TITIERP.HTLDE2, FAREMAP.IDREMARQUE, FAREMAP.TEXTE, FAENTFP.MODIMP ";
    }

    /**
     * Renvoie la clause FROM
     * 
     * @return la clause FROM
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + FAEnteteFacture.TABLE_FAENTFP + " AS " + FAEnteteFacture.TABLE_FAENTFP
                + " LEFT JOIN " + _getCollection() + "TITIERP AS TITIERP ON (FAENTFP.IDTIERS=TITIERP.HTITIE) "
                + "LEFT JOIN " + _getCollection() + "FAREMAP AS FAREMAP ON (FAENTFP.IDREMARQUE=FAREMAP.IDREMARQUE) ";
    }

    // CHANGED FAPassageViewBean by FAPassage
    protected FAPassage _getPassage(BTransaction transaction) {
        // Chargement si nécessaire
        if (passage == null) {
            try {
                passage = new FAPassageViewBean();
                passage.setSession(getSession());
                passage.setIdPassage(getIdPassage());
                passage.retrieve(transaction);
            } catch (Exception e) {
                _addError(transaction, "Exception raised in FAEnteteFacture._getPassage(): " + e.getMessage());
            }
        }
        return passage;
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return FAEnteteFacture.TABLE_FAENTFP;
    }

    protected ITITiers _getTiers() {
        return this._getTiers(null);
    }

    protected ITITiers _getTiers(BTransaction transaction) {
        // Si cache vide
        if (_tiers == null) {
            try {
                FAApplication app = (FAApplication) GlobazServer.getCurrentSystem().getApplication(
                        FAApplication.DEFAULT_APPLICATION_MUSCA);
                /* ADDED */String[] methods = new String[] { "getIdTiers", "getDesignation1", "getDesignation2",
                        "getLangue", "getNumAvsActuel" };
                if (transaction == null) {
                    /* CHANGED */_tiers = app.getTiersByRole(getSession(), getIdRole(), getIdExterneRole(),
                            getIdTiers(), methods);
                } else {
                    /* CHANGED */_tiers = app.getTiersByRole(transaction, getIdRole(), getIdExterneRole(),
                            getIdTiers(), methods);
                }
            } catch (Exception e) {
                _addError(transaction, e.getMessage());
            }
        }
        return _tiers;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idEntete = statement.dbReadNumeric(FAEnteteFacture.FIELD_IDENTETEFACTURE);
        idPassage = statement.dbReadNumeric(FAEnteteFacture.FIELD_IDPASSAGE);
        idRemarque = statement.dbReadNumeric(FAEnteteFacture.FIELD_IDREMARQUE);
        idTiers = statement.dbReadNumeric(FAEnteteFacture.FIELD_IDTIERS);
        idRole = statement.dbReadNumeric(FAEnteteFacture.FIELD_IDROLE);
        idExterneRole = statement.dbReadString(FAEnteteFacture.FIELD_IDEXTERNEROLE);
        idAdresse = statement.dbReadNumeric(FAEnteteFacture.FIELD_IDADRESSE);
        idTypeFacture = statement.dbReadNumeric(FAEnteteFacture.FIELD_IDTYPEFACTURE);
        idSousType = statement.dbReadNumeric(FAEnteteFacture.FIELD_IDSOUSTYPE);
        idExterneFacture = statement.dbReadString(FAEnteteFacture.FIELD_IDEXTERNEFACTURE);
        numCommune = statement.dbReadNumeric(FAEnteteFacture.FIELD_NUMCOMMUNE);
        nonImprimable = statement.dbReadBoolean(FAEnteteFacture.FIELD_NONIMPRIMABLE);
        totalFacture = statement.dbReadNumeric(FAEnteteFacture.FIELD_TOTALFACTURE, 2);
        idSoumisInteretsMoratoires = statement.dbReadNumeric(FAEnteteFacture.FIELD_IDSOUINTMOR);
        motifInteretsMoratoires = statement.dbReadString(FAEnteteFacture.FIELD_MOTIFINTMOR);
        idAdressePaiement = statement.dbReadNumeric(FAEnteteFacture.FIELD_IDADRESSEPAIEMENT);
        idModeRecouvrement = statement.dbReadNumeric(FAEnteteFacture.FIELD_IDMODERECOUVREMENT);
        dateReceptionDS = statement.dbReadString(FAEnteteFacture.FIELD_REFCOLLABORATEUR);
        tierDesignation1 = statement.dbReadString(FAEnteteFacture.FIELD_HTLDE1);
        tierDesignation2 = statement.dbReadString(FAEnteteFacture.FIELD_HTLDE2);
        libelle = statement.dbReadString(FAEnteteFacture.FIELD_LIBELLE);
        remarque = FARemarque.getRemarque(statement);
        idDomaineCourrier = statement.dbReadNumeric(FAEnteteFacture.FIELD_ID_DOMAINE_COURRIER);
        idTypeCourrier = statement.dbReadNumeric(FAEnteteFacture.FIELD_ID_TYPE_COURRIER);
        idDomaineLSV = statement.dbReadNumeric(FAEnteteFacture.FIELD_ID_DOMAINE_LVS);
        idDomaineRemboursement = statement.dbReadNumeric(FAEnteteFacture.FIELD_ID_DOMAINE_REMBOURSEMENT);
        estRentierNa = statement.dbReadBoolean(FAEnteteFacture.FIELD_ESTRENTIERNA);
        idControle = statement.dbReadNumeric(FAEnteteFacture.FIELD_ID_CONTROLE);
        idCSModeImpression = statement.dbReadNumeric(FAEnteteFacture.FIELD_MODIMP);
        referenceFacture = statement.dbReadString(FAEnteteFacture.FIELD_REFERENCE_FACTURE);
        eBillTransactionID = statement.dbReadString(FAEnteteFacture.FIELD_EBILL_TRANSACTION_ID);

        if (isUseEntityForLSV()) {
            idTiAdressePaiement = statement.dbReadNumeric(FAEnteteFacture.FIELD_AAP_ADRESSE_PAYEMENT);
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires) Pour le contôle de la saisie du n° du tiers en
     * fontion du rôle voir la méthode _validate() définie dans FAEnteteFactureViewBean()
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Vérification des champs obligatoires
        _propertyMandatory(statement.getTransaction(), idEntete,
                "L'identifiant de l'entête de facture doit être renseigné");
        _propertyMandatory(statement.getTransaction(), idRole, "Le rôle doit être renseigné");
        _propertyMandatory(statement.getTransaction(), idExterneRole, "Le numéro du débiteur doit être renseigné");
        _propertyMandatory(statement.getTransaction(), idExterneFacture, "Le numéro de facture doit être renseigné");
        _propertyMandatory(statement.getTransaction(), idPassage, "Le numéro de passageVB doit être renseigné");
        // acr: si déjà des erreurs à ce niveau, ça ne sert à rien de rechercher
        // le débiteur
        if (!statement.getTransaction().hasErrors()) {
            // Récupérer le tiers
            ITITiers tiers = this._getTiers(statement.getTransaction());
            if (tiers.isNew()) {
                _addError(statement.getTransaction(), "Le débiteur " + idExterneRole + " n'existe pas");
            } else {
                setIdTiers(tiers.getIdTiers());
            }
        }
        if (!JadeStringUtil.isEmpty(getIdExterneFacture())) {
            int longDecompte;
            longDecompte = getIdExterneFacture().length();
            if (longDecompte < 9) {
                _addError(statement.getTransaction(),
                        "Il manque un ou plusieurs chiffre(s) dans le numéro de décompte " + getIdExterneFacture());
            }
            if (longDecompte > 9) {
                _addError(statement.getTransaction(), "Il y a trop de chiffre(s) dans le numéro de décompte "
                        + getIdExterneFacture());
            }
            String typeDecompte = "";
            typeDecompte = getIdExterneFacture().substring(4, 6);
            typeDecompte = "2270" + typeDecompte;
            boolean existe = isCategorieSection(typeDecompte);
            if (!existe) {
                _addError(statement.getTransaction(), "Ce type de décompte n'existe pas! " + getIdExterneFacture());
            }

        }
        // Récupérer un descripteur de section
        FAApplication app = (FAApplication) GlobazServer.getCurrentSystem().getApplication(
                FAApplication.DEFAULT_APPLICATION_MUSCA);
        APISectionDescriptor d = app.getSectionDescriptor(getSession());
        // Contrôler la section
        d.setSection(getIdExterneFacture(), getIdTypeFacture(), getIdSousType(),
                _getPassage(statement.getTransaction()).getDateFacturation(), "", "");
        // S'il y a des erreurs
        if (d.hasErrors()) {
            _addError(statement.getTransaction(), d.getErrors());
            // S'il n'y a pas d'erreur
        } else {
            // Mise à jour du sous type si vide
            if (JadeStringUtil.isIntegerEmpty(getIdSousType())) {
                setIdSousType(d.getIdCategorie());
            }
            // Mise à jour du type si vide
            if (JadeStringUtil.isIntegerEmpty(getIdTypeFacture())) {
                setIdTypeFacture(d.getIdTypeSection());
            }
        }
        // Vérifier type et sous type
        _propertyMandatory(statement.getTransaction(), idTypeFacture, "Le type de section doit être renseigné");
        _propertyMandatory(statement.getTransaction(), idSousType, "Le type de décompte doit être renseigné");

        if (!JadeStringUtil.isIntegerEmpty(getIdSousType())) {
            if (getIdSousType().equals(APISection.ID_CATEGORIE_SECTION_DECOMPTE_FINAL)
                    || getIdSousType().equals(APISection.ID_CATEGORIE_SECTION_BOUCLEMENT_ACOMPTE)
                    || getIdSousType().equals(APISection.ID_CATEGORIE_SECTION_LTN)) {
                if (!("9").equals(getIdExterneFacture().substring(6, 7))) {
                    if (getDateReceptionDS().equals("") || getDateReceptionDS().equals(null)) {
                        _addError(statement.getTransaction(),
                                "La date de réception doit être saisie pour ce type de décompte " + getIdExterneRole()
                                        + " " + getIdExterneFacture());
                    }
                }
            }
        }
    }

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        if (alternateKey == FAEnteteFacture.ALT_KEY_PASSAGE_ROLE) {
            // idPassage, idRole, idExterneRole, idTypeFacture, idExterneFacture
            statement.writeKey(FAEnteteFacture.FIELD_IDPASSAGE,
                    this._dbWriteNumeric(statement.getTransaction(), idPassage));
            statement.writeKey(FAEnteteFacture.FIELD_IDROLE, this._dbWriteNumeric(statement.getTransaction(), idRole));
            statement.writeKey(FAEnteteFacture.FIELD_IDEXTERNEROLE,
                    this._dbWriteString(statement.getTransaction(), idExterneRole));
            statement.writeKey(FAEnteteFacture.FIELD_IDTYPEFACTURE,
                    this._dbWriteNumeric(statement.getTransaction(), idTypeFacture));
            statement.writeKey(FAEnteteFacture.FIELD_IDEXTERNEFACTURE,
                    this._dbWriteString(statement.getTransaction(), idExterneFacture));
        } else {
            super._writeAlternateKey(statement, alternateKey);
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FAEnteteFacture.FIELD_IDENTETEFACTURE,
                this._dbWriteNumeric(statement.getTransaction(), getIdEntete(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FAEnteteFacture.FIELD_IDENTETEFACTURE,
                this._dbWriteNumeric(statement.getTransaction(), getIdEntete(), "idEntete"));
        statement.writeField(FAEnteteFacture.FIELD_IDPASSAGE,
                this._dbWriteNumeric(statement.getTransaction(), getIdPassage(), "idPassage"));
        statement.writeField(FAEnteteFacture.FIELD_IDREMARQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRemarque(), "idRemarque"));
        statement.writeField(FAEnteteFacture.FIELD_IDTIERS,
                this._dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
        statement.writeField(FAEnteteFacture.FIELD_IDROLE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRole(), "idRole"));
        statement.writeField(FAEnteteFacture.FIELD_IDEXTERNEROLE,
                this._dbWriteString(statement.getTransaction(), getIdExterneRole(), "idExterneRole"));
        statement.writeField(FAEnteteFacture.FIELD_IDADRESSE,
                this._dbWriteNumeric(statement.getTransaction(), getIdAdresse(), "idAdresse"));
        statement.writeField(FAEnteteFacture.FIELD_IDTYPEFACTURE,
                this._dbWriteNumeric(statement.getTransaction(), getIdTypeFacture(), "idTypeFacture"));
        statement.writeField(FAEnteteFacture.FIELD_IDSOUSTYPE,
                this._dbWriteNumeric(statement.getTransaction(), getIdSousType(), "idSousType"));
        statement.writeField(FAEnteteFacture.FIELD_IDEXTERNEFACTURE,
                this._dbWriteString(statement.getTransaction(), getIdExterneFacture(), "idExterneFacture"));
        statement.writeField(FAEnteteFacture.FIELD_NUMCOMMUNE,
                this._dbWriteNumeric(statement.getTransaction(), getNumCommune(), "numCommune"));
        statement.writeField(FAEnteteFacture.FIELD_NONIMPRIMABLE, this._dbWriteBoolean(statement.getTransaction(),
                isNonImprimable(), BConstants.DB_TYPE_BOOLEAN_CHAR, "nonImprimable"));
        statement.writeField(FAEnteteFacture.FIELD_TOTALFACTURE,
                this._dbWriteNumeric(statement.getTransaction(), getTotalFacture(), "totalFacture"));
        statement.writeField(FAEnteteFacture.FIELD_IDSOUINTMOR, this._dbWriteNumeric(statement.getTransaction(),
                getIdSoumisInteretsMoratoires(), "idSoumisInteretsMoratoires"));
        statement.writeField(FAEnteteFacture.FIELD_MOTIFINTMOR, this._dbWriteString(statement.getTransaction(),
                getMotifInteretsMoratoires(), "motifInteretsMoratoires"));
        statement.writeField(FAEnteteFacture.FIELD_IDADRESSEPAIEMENT,
                this._dbWriteNumeric(statement.getTransaction(), getIdAdressePaiement(), "idAdressePaiement"));
        statement.writeField(FAEnteteFacture.FIELD_IDMODERECOUVREMENT,
                this._dbWriteNumeric(statement.getTransaction(), getIdModeRecouvrement(), "idModeRecouvrement"));
        statement.writeField(FAEnteteFacture.FIELD_REFCOLLABORATEUR,
                this._dbWriteString(statement.getTransaction(), getDateReceptionDS(), "referenceCollaborateur"));
        statement.writeField(FAEnteteFacture.FIELD_LIBELLE,
                this._dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField(FAEnteteFacture.FIELD_ID_DOMAINE_COURRIER,
                this._dbWriteNumeric(statement.getTransaction(), getIdDomaineCourrier(), "idDomaineCourrier"));
        statement.writeField(FAEnteteFacture.FIELD_ID_TYPE_COURRIER,
                this._dbWriteNumeric(statement.getTransaction(), getIdTypeCourrier(), "idTypeCourrier"));
        statement.writeField(FAEnteteFacture.FIELD_ID_DOMAINE_LVS,
                this._dbWriteNumeric(statement.getTransaction(), getIdDomaineLSV(), "idDomaineLSV"));
        statement
                .writeField(FAEnteteFacture.FIELD_ID_DOMAINE_REMBOURSEMENT, this._dbWriteNumeric(
                        statement.getTransaction(), getIdDomaineRemboursement(), "idDomaineRemboursement"));
        statement.writeField(FAEnteteFacture.FIELD_ESTRENTIERNA, this._dbWriteBoolean(statement.getTransaction(),
                getEstRentierNa(), BConstants.DB_TYPE_BOOLEAN_CHAR, "estRentierNa"));
        statement.writeField(FAEnteteFacture.FIELD_ID_CONTROLE,
                this._dbWriteNumeric(statement.getTransaction(), getIdControle(), "idControle"));
        statement.writeField(FAEnteteFacture.FIELD_MODIMP,
                this._dbWriteNumeric(statement.getTransaction(), getIdCSModeImpression(), "idCSModeImpression"));
        statement.writeField(FAEnteteFacture.FIELD_REFERENCE_FACTURE,
                this._dbWriteString(statement.getTransaction(), getReferenceFacture(), "referenceFacture"));
        statement.writeField(FAEnteteFacture.FIELD_EBILL_TRANSACTION_ID,
                this._dbWriteString(statement.getTransaction(), geteBillTransactionID(), "eBillTransactionID"));

    }

    /*
     * Ajoute un afact à l'entête de facture Return true si la mise à jour a été effectué
     */
    public Boolean addAfact(BTransaction trans, FAAfact afact) {
        if ((!afact.isNonComptabilisable().booleanValue()) && (!afact.isAQuittancer().booleanValue())) {
            FAEnteteFacture entete = new FAEnteteFacture();
            entete.setSession(trans.getSession());
            entete.setIdEntete(afact.getIdEnteteFacture());
            try {
                entete.retrieve(trans);
                // Cumul du montant
                FWCurrency c = new FWCurrency(entete.getTotalFacture());
                // On ne somme que les afacts qui ne sont pas en suspend
                c.add(afact.getMontantFacture());
                entete.setTotalFacture(c.toString());
                entete.wantCallValidate(false);
                entete.update(trans);
                entete.wantCallValidate(true);
                return new Boolean(true);
            } catch (Exception e) {
                JadeLogger.error(this, e);
                return new Boolean(false);
            }
        } else {
            return new Boolean(false);
        }
    }

    /*
     * Retourne l'adresse de domicile du tiers Ex: Soit le n° avs, le n° affilié ou le n° de contribuable
     */
    public String getAdresseCourrier(BTransaction trans, String date) throws Exception {
        String courrier = "";
        String domaineCourrier = "";
        if (!JadeStringUtil.isIntegerEmpty(getIdTypeCourrier())) {
            courrier = getIdTypeCourrier();
        } else {
            courrier = IConstantes.CS_AVOIR_ADRESSE_COURRIER;
        }
        if (!JadeStringUtil.isIntegerEmpty(getIdDomaineCourrier())) {
            domaineCourrier = getIdDomaineCourrier();
        } else {
            domaineCourrier = IConstantes.CS_APPLICATION_FACTURATION;
        }
        // Récupérer le tiers
        ITITiers tiers = this._getTiers();
        if (tiers == null) {
            return "";
        } else {
            return tiers.getAdresseAsString(courrier, domaineCourrier, date, getIdExterneRole());
        }

    }

    /*
     * Retourne l'adresse de domicile du tiers Ex: Soit le n° avs, le n° affilié ou le n° de contribuable
     */
    public String getAdresseDomicile(BTransaction trans, String date) throws Exception {
        // Récupérer le tiers
        ITITiers tiers = this._getTiers();
        if (tiers == null) {
            return "";
        } else {
            return tiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, IConstantes.CS_APPLICATION_DEFAUT,
                    date, getIdExterneRole());
        }
    }

    /*
     * Retourne l'adresse de domicile du tiers Ex: Soit le n° avs, le n° affilié ou le n° de contribuable
     */
    public String getAdressePaiement(BTransaction trans, String date) throws Exception {
        // l'adresse de paiement est l'adresse de courrier
        // Récupérer le tiers
        // mmu, 4.11.05, Ajouté la transaction à la méthode pour éviter de
        // générer trop de connection
        ITITiers tiers = this._getTiers(trans);
        if (tiers == null) {
            return "";
        } else {
            String idApplication = JadeStringUtil.isIntegerEmpty(getIdDomaineRemboursement()) ? FAEnteteFacture.CS_REMBOURSEMENT_COTI
                    : getIdDomaineRemboursement();
            return tiers.getAdressePaiementAsString(idApplication, date);
        }
    }

    /*
     * Retourne l'adresse de domicile du tiers Ex: Soit le n° avs, le n° affilié ou le n° de contribuable
     */
    public String getAdressePrincipale(BTransaction trans, String date) throws Exception {
        // l'adresse de paiement est l'adresse de courrier
        String result = getAdresseCourrier(trans, date);
        if (!JadeStringUtil.isBlank(result)) {
            return result;
        } else {
            return getAdresseDomicile(trans, date);
        }
    }

    /**
     * @return
     */
    @Override
    public String getDateReceptionDS() {
        return dateReceptionDS;
    }

    @Override
    public String getDescriptionDecompte() {
        return this.getDescriptionDecompte(getSession().getIdLangueISO());
    }

    /**
     * Affiche la description du décompte dans la langue déterminé par la langue du tiers Date de création : (01.03.2003
     * 10:50:06)
     * 
     * @return String
     */
    public String getDescriptionDecompte(String langueTiers) {
        String s = "";
        try {
            /* CHANGED */if (isNew()) {
                FAEnteteFacture entete = new FAEnteteFacture();
                entete.setSession(getSession());
                entete.setIdEntete(getIdEntete());
                entete.retrieve();
                setIdExterneFacture(entete.getIdExterneFacture());
            }
            /* ADDED */if (!JadeStringUtil.isBlank(getIdExterneFacture())) {
                s = getIdExterneFacture();
                FAApplication app = (FAApplication) getSession().getApplication();
                APISectionDescriptor d = app.getSectionDescriptor(getSession());
                d.setSection(getIdExterneFacture(), getIdTypeFacture(), getIdSousType(), "", "", "");
                // paramétrer avec la langue du tiers

                s = s + " " + d.getDescription(langueTiers);
                /* ADDED */}
        } catch (Exception e) {
            _addError(null, e.getMessage());
        }
        return s;
    }

    /**
     * Affiche la description du décompte dans la langue déterminé par la langue du tiers Date de création : (01.03.2003
     * 10:50:06)
     * 
     * @return String
     */
    public String getDescriptionDecompteComp(String langueTiers) {
        String s = "";
        try {
            /* CHANGED */if (isNew()) {
                setIdExterneFacture(getIdExterneFacture());
            }
            /* ADDED */if (!JadeStringUtil.isBlank(getIdExterneFacture())) {
                s = getIdExterneFacture();
                FAApplication app = (FAApplication) getSession().getApplication();
                APISectionDescriptor d = app.getSectionDescriptor(getSession());
                d.setSection(getIdExterneFacture(), getIdTypeFacture(), getIdSousType(), "", "", "");
                // paramétrer avec la langue du tiers

                s = s + " " + d.getDescription(langueTiers);
                /* ADDED */}
        } catch (Exception e) {
            _addError(null, e.getMessage());
        }
        return s;
    }

    /**
     * Retourne la description du Role (Affilié,...)
     * 
     * @return String
     */
    public String getDescriptionRole() {
        return getSession().getCodeLibelle(getIdRole());
    }

    @Override
    public String getDescriptionTiers() {
        if (!JadeStringUtil.isEmpty(getIdExterneRole()) || !JadeStringUtil.isBlank(getIdExterneRole())) {
            return getDescriptionRole() + " " + getIdExterneRole() + ", " + getNomTiers();
        } else {
            FAEnteteFacture entete = new FAEnteteFacture();
            try {
                entete.setSession(getSession());
                entete.setIdEntete(getIdEntete());
                entete.retrieve();
                setIdExterneRole(entete.getIdExterneRole());
                setIdRole(entete.getIdRole());
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
            return entete.getDescriptionRole() + " " + entete.getIdExterneRole() + ", " + entete.getNomTiers();
        }

    }

    /**
     * Retourne la description du tiers selon le standard Osiris: Role du débiteur, id du role, Nom prénom, Localité
     * Date de création : (11.04.2003 08:24:17)
     * 
     * @return String
     */
    public String getDescriptionTiersForList() {
        StringBuffer description = new StringBuffer("");
        String descriptionTiers = getDescriptionTiers();
        String localiteTiers = getLocaliteTiers();

        if (!JadeStringUtil.isBlank(descriptionTiers)) {
            description.append(descriptionTiers);
        }
        if (!JadeStringUtil.isBlank(localiteTiers)) {
            description.append(", " + localiteTiers);
        }
        return description.toString();
    }

    public void getDonnee() {
        if (!JadeStringUtil.isEmpty(idEntete) && !JadeStringUtil.isBlank(idEntete)) {
            FAEnteteFacture entete = new FAEnteteFacture();
            try {
                entete.setSession(getSession());
                entete.setIdEntete(getIdEntete());
                entete.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
            if (idExterneRole.equals("") || idExterneRole.equals(null)) {
                setIdExterneRole(entete.getIdExterneRole());
            } else {
                setIdExterneRole(idExterneRole);
            }
            if (idExterneFacture.equals("") || idExterneFacture.equals(null)) {
                setIdExterneFacture(entete.getIdExterneFacture());
            } else {
                setIdExterneFacture(idExterneFacture);
            }
            if (idModeRecouvrement.equals("907005")) {
                setIdModeRecouvrement(entete.getIdModeRecouvrement());
            } else {
                setIdModeRecouvrement(idModeRecouvrement);
            }
            if (idRole.equals("") || idRole.equals(null)) {
                setIdRole(entete.getIdRole());
            } else {
                setIdRole(idRole);
            }
            if (idRemarque.equals("") || idRemarque.equals(null)) {
                setIdRemarque(entete.getIdRemarque());
            } else {
                setIdRemarque(idRemarque);
            }
            if (remarque.equals("") || remarque.equals(null)) {
                setRemarque(entete.getRemarque());
            } else {
                setRemarque(remarque);
            }

            if (idTiers.equals("") || idTiers.equals(null)) {
                setIdTiers(entete.getIdTiers());
            } else {
                setIdTiers(idTiers);
            }
            if (idAdresse.equals("") || idAdresse.equals(null)) {
                setIdAdresse(entete.getIdAdresse());
            } else {
                setIdAdresse(idAdresse);
            }
            if (dateReceptionDS.equals("") || dateReceptionDS.equals(null)) {
                setDateReceptionDS(entete.getDateReceptionDS());
            } else {
                setDateReceptionDS(dateReceptionDS);
            }

            setIdTypeFacture(entete.getIdTypeFacture());
            setIdSousType(entete.getIdSousType());
            setNonImprimable(entete.isNonImprimable());
            setTotalFacture(entete.getTotalFacture());
            if (idSoumisInteretsMoratoires.equals("907005")) {
                setIdSoumisInteretsMoratoires(entete.getIdSoumisInteretsMoratoires());
            } else {
                setIdSoumisInteretsMoratoires(idSoumisInteretsMoratoires);
            }
            if (motifInteretsMoratoires.equals("") || motifInteretsMoratoires.equals(null)) {
                setMotifInteretsMoratoires(entete.getMotifInteretsMoratoires());
            } else {
                setMotifInteretsMoratoires(motifInteretsMoratoires);
            }
            setIdAdressePaiement(entete.getIdAdressePaiement());
            if (idPassage.equals("") || idPassage.equals(null)) {
                setIdPassage(entete.getIdPassage());
            } else {
                setIdPassage(idPassage);
            }
            if (libelle.equals("") || libelle.equals(null)) {
                setLibelle(entete.getLibelle());
            } else {
                setLibelle(libelle);
            }
        }
    }

    @Override
    public String getEspion() {
        return espion;
    }

    public Boolean getEstRentierNa() {
        return estRentierNa;
    }

    /**
     * Returns the forceDelete.
     * 
     * @return Boolean
     */
    public Boolean getForceDelete() {
        return forceDelete;
    }

    @Override
    public String getIdAdresse() {
        return idAdresse;
    }

    @Override
    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdControle() {
        return idControle;
    }

    public String getIdCSModeImpression() {
        return idCSModeImpression;
    }

    public String getIdDomaineCourrier() {
        return idDomaineCourrier;
    }

    public String getIdDomaineLSV() {
        return idDomaineLSV;
    }

    public String getIdDomaineRemboursement() {
        return idDomaineRemboursement;
    }

    /**
     * Getter
     */
    @Override
    public String getIdEntete() {
        return idEntete;
    }

    @Override
    public String getIdExterneFacture() {
        return idExterneFacture;
    }

    @Override
    public String getIdExterneRole() {
        return idExterneRole;
    }

    @Override
    public String getIdModeRecouvrement() {
        return idModeRecouvrement;
    }

    @Override
    public String getIdPassage() {
        return idPassage;
    }

    @Override
    public String getIdRemarque() {
        return idRemarque;
    }

    @Override
    public String getIdRole() {
        return idRole;
    }

    @Override
    public String getIdSoumisInteretsMoratoires() {
        return idSoumisInteretsMoratoires;
    }

    @Override
    public String getIdSousType() {
        return idSousType;
    }

    public String getIdTiAdressePaiement() {
        return idTiAdressePaiement;
    }

    @Override
    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTypeCourrier() {
        return idTypeCourrier;
    }

    @Override
    public String getIdTypeFacture() {
        return idTypeFacture;
    }

    /**
     * Indique si la section est en poursuites (toujours false pour IM sur facturation)
     */
    public boolean getImIsEnPoursuites() {
        return false;
    }

    public String getISOLangueTiers() {
        // Récupérer le tiers
        ITITiers tiers = this._getTiers();
        if (tiers == null) {
            return "FR";
        } else if (tiers.getLangue().equalsIgnoreCase(IConstantes.CS_TIERS_LANGUE_FRANCAIS)) {
            return "FR";
        } else if (tiers.getLangue().equalsIgnoreCase(IConstantes.CS_TIERS_LANGUE_ALLEMAND)) {
            return "DE";
        } else if (tiers.getLangue().equalsIgnoreCase(IConstantes.CS_TIERS_LANGUE_ITALIEN)) {
            return "IT";
        } else {
            return "FR"; // default
        }
    }

    public String getLangueTiers() {
        /*
         * cette méthode convertit ISOLangueTiers comme ceci: FR -> F, DE -> D, IT -> I
         */
        if (langueTiers == null) {
            langueTiers = getISOLangueTiers(); // .substring(0,1);
        }
        return langueTiers;
    }

    /**
     * @return
     */
    public String getLibelle() {
        return libelle;
    }

    public Vector<?> getListTypeFacture(BTransaction transaction) {
        // Instancier un vecteur
        Vector<?> v = new Vector<Object>();
        // Récupérer la liste des types de factures
        try {

        } catch (Exception e) {
            _addError(transaction, "FAEnteteFacture.getListTypeFacture(): Exception raised : " + e.getMessage());
        }
        // Retourner le vecteur
        return v;
    }

    /**
     * Retourne la localité du tiers ou null si introuvable Date de création : (10.04.2003 14:46:45)
     * 
     * @return String
     */
    public String getLocaliteTiers() {

        // Récupérer le tiers
        ITITiers tiers = this._getTiers();
        if (tiers == null) {
            return "";
        } else {
            try {
                String localite = tiers.getLocalite();
                return localite;
            } catch (Exception e) {
                return "";
            }

        }
    }

    @Override
    public String getMotifInteretsMoratoires() {
        return motifInteretsMoratoires;
    }

    @Override
    public String getNomTiers() {
        // Récupérer le tiers
        ITITiers tiers = this._getTiers();
        if (tiers != null) {
            tierDesignation1 = tiers.getDesignation1();
            tierDesignation2 = tiers.getDesignation2();
        }
        return getTierDesignation1() + " " + getTierDesignation2();
    }

    @Override
    public String getNumCommune() {
        return numCommune;
    }

    /*
     * Retourne le numéro du tiers selon le rôle Ex: Soit le n° avs, le n° affilié ou le n° de contribuable
     */
    public String getNumeroAVSTiers(BTransaction trans) {
        // Récupérer le tiers
        ITIPersonneAvs tiers = (ITIPersonneAvs) this._getTiers();
        if (tiers == null) {
            return "";
        } else {
            return tiers.getNumAvsActuel();
        }
    }

    /**
     * renvoie le passage associé à l'entete de facture
     * 
     * @return null en cas d'erreur
     */
    public FAPassage getPassage() {
        // Chargement si nécessaire
        if (passage == null) {
            try {
                passage = new FAPassageViewBean();
                passage.setSession(getSession());
                passage.setIdPassage(getIdPassage());
                passage.retrieve();
            } catch (Exception e) {
                passage = null;
            }
        }
        return passage;
    }

    public String getReferenceFacture() {
        return referenceFacture;
    }

    /**
     * @see globaz.musca.api.IFAEnteteFacture#getRemarque() Retourne le texte de la remarque avec l'idremarque (FAREMAP)
     *      contenue dans l'entête.
     */
    @Override
    public String getRemarque() {
        return remarque;
    }

    /**
     * @see IFAEnteteFacture#getRemarque() Retourne le texte de la remarque avec l'idremarque (FAREMAP) contenue dans
     *      l'entête.
     */
    @Override
    public String getRemarque(BITransaction transaction) {
        FARemarque rema = null;
        String remarque = "";
        /* CHANGED */if (!JadeStringUtil.isIntegerEmpty(getIdRemarque())) {
            rema = new FARemarque();
            rema.setSession(((BTransaction) transaction).getSession());
            rema.setIdRemarque(getIdRemarque());
            try {
                rema.retrieve(transaction);
                remarque = rema.getTexte();
            } catch (Exception e) {
                _addError((BTransaction) transaction, "Erreur lors de la récupération de la remarque du décompte. ");
            }
            /* CHANGED */} else if (!JadeStringUtil.isIntegerEmpty(getPassage().getIdRemarque())) {
            rema = new FARemarque();
            rema.setSession(((BTransaction) transaction).getSession());
            // REMOVED FAPassage passage = new FAPassage();
            // REMOVED passage.setSession(((BTransaction)
            // transaction).getSession());
            // REMOVED passage.setIdPassage(getIdPassage());
            try {
                // REMOVED passage.retrieve();
                /* CHANGED */rema.setIdRemarque(getPassage().getIdRemarque());
                rema.retrieve();
                remarque = rema.getTexte();
            } catch (Exception e) {
                _addError((BTransaction) transaction, "Erreur lors de la récupération de la remarque du passage. ");
            }
        }
        return remarque;
    }

    public String getTierDesignation1() {
        return tierDesignation1;
    }

    public String getTierDesignation2() {
        return tierDesignation2;
    }

    /**
     * rechercher l'id de l'adresse de paiement. Si aucun domaine d'application n'est renseigné prend le domaine de
     * remboursement directe (519010)
     * 
     * @param date
     * @param domaine
     *            application
     */
    public String getTiersIdAdressePaiement(BTransaction trans, String date, String domaine) throws Exception {
        String domainePaiement = "";
        if (domaine.equals(TITiersOSI.DOMAINE_REMBOURSEMENT)) {
            if (!JadeStringUtil.isIntegerEmpty(getIdDomaineRemboursement())) {
                domainePaiement = getIdDomaineRemboursement();
            } else {
                domainePaiement = TITiersOSI.DOMAINE_REMBOURSEMENT;
            }
        }
        if (domaine.equals(TITiersOSI.DOMAINE_RECOUVREMENT)) {
            if (!JadeStringUtil.isIntegerEmpty(getIdDomaineLSV())) {
                domainePaiement = getIdDomaineLSV();
            } else {
                domainePaiement = TITiersOSI.DOMAINE_RECOUVREMENT;
            }
        }

        // l'adresse de paiement est l'adresse de courrier
        // Récupérer le tiers
        ITITiers tiers = this._getTiers();
        if (tiers == null) {
            return "";
        } else {
            String idAdrPaiement = tiers.findIdAdressePaiement(domainePaiement, getIdExterneRole(), date);
            if (domaine.equals(TITiersOSI.DOMAINE_RECOUVREMENT) && !JadeStringUtil.isIntegerEmpty(idAdrPaiement)
                    && !getIdDomaineLSV().equals(TITiersOSI.DOMAINE_STANDARD)
                    && !getIdDomaineLSV().equals(ICommonConstantes.CS_APPLICATION_COTISATION)) {
                TIAvoirPaiement adressePaiement = new TIAvoirPaiement();
                adressePaiement.setSession(getSession());
                adressePaiement.setIdAdrPmtIntUnique(idAdrPaiement);
                adressePaiement.retrieve();
                if (adressePaiement.getIdApplication().equals(TITiersOSI.DOMAINE_STANDARD)
                        || adressePaiement.getIdApplication().equals(ICommonConstantes.CS_APPLICATION_COTISATION)) {
                    return "";
                }
            }
            return idAdrPaiement;
        }
    }

    @Override
    public String getTotalFacture() {
        return JANumberFormatter.fmt(totalFacture.toString(), true, true, false, 2);
    }

    public FWCurrency getTotalFactureCurrency() {
        return new FWCurrency(totalFacture);
    }

    public String getTypeDescription() {
        return getSession().getCodeLibelle(idTypeFacture);
    }

    /**
     * Initisalise l'entête avec les valeurs du plan (des plans) de l'affiliation.<br>
     * Note: à utiliser uniquement si l'idTiers, l'idAffiliation et le numéro de décompte sont renseignés
     */
    public void initDefaultPlanValue() throws Exception {
        if (JadeStringUtil.isEmpty(getIdTiers()) || JadeStringUtil.isEmpty(getIdExterneRole())
                || JadeStringUtil.isEmpty(getIdExterneFacture())) {
            return;
        }
        AFAffiliation affToUse = AFAffiliationUtil.loadAffiliation(getSession(), getIdTiers(), getIdExterneRole(),
                getIdExterneFacture(), getIdRole());
        if (affToUse != null) {
            AFAffiliationUtil util = new AFAffiliationUtil(affToUse);
            setIdDomaineCourrier(util.getAdresseDomaineCourrier(CodeSystem.ROLE_AFFILIE));
            setIdDomaineLSV(util.getAdresseDomaineRecouvrement(CodeSystem.ROLE_AFFILIE));
            setIdDomaineRemboursement(util.getAdresseDomaineRemboursement(CodeSystem.ROLE_AFFILIE));
            setNonImprimable(util.isPlanBloque());
        }

    }

    /**
     * Initisalise l'entête avec les valeurs du plan (des plans) de l'affiliation.<br>
     * Note: à utiliser uniquement si l'idTiers, l'idAffiliation et le numéro de décompte sont renseignés
     */
    public void initDefaultPlanValue(String genreAffilie) throws Exception {
        if (JadeStringUtil.isEmpty(getIdTiers()) || JadeStringUtil.isEmpty(getIdExterneRole())
                || JadeStringUtil.isEmpty(getIdExterneFacture())) {
            return;
        }
        AFAffiliation affToUse = AFAffiliationUtil.loadAffiliation(getSession(), getIdTiers(), getIdExterneRole(),
                getIdExterneFacture(), genreAffilie);
        if (affToUse != null) {
            if (!JadeStringUtil.isBlankOrZero(genreAffilie)) {
                AFAffiliationUtil util = new AFAffiliationUtil(affToUse);
                setIdDomaineCourrier(util.getAdresseDomaineCourrier(genreAffilie));
                setIdDomaineLSV(util.getAdresseDomaineRecouvrement(genreAffilie));
                setIdDomaineRemboursement(util.getAdresseDomaineRemboursement(genreAffilie));
                setNonImprimable(util.isPlanBloque());
            }
            // sinon par défaut on prend le role affilié
            else {
                AFAffiliationUtil util = new AFAffiliationUtil(affToUse);
                setIdDomaineCourrier(util.getAdresseDomaineCourrier(CodeSystem.ROLE_AFFILIE));
                setIdDomaineLSV(util.getAdresseDomaineRecouvrement(CodeSystem.ROLE_AFFILIE));
                setIdDomaineRemboursement(util.getAdresseDomaineRemboursement(CodeSystem.ROLE_AFFILIE));
                setNonImprimable(util.isPlanBloque());
            }
        }
    }

    /**
     * Prépare les valeurs par défaut du bean avant un ajout Date de création : (22.02.2003 15:31:42)
     */
    public void initDefaultValues() {
        // Intérêts moratoires
        if (JadeStringUtil.isIntegerEmpty(getIdSoumisInteretsMoratoires())) {
            setIdSoumisInteretsMoratoires("229001");
        }
        // Mode de recouvrement
        if (JadeStringUtil.isIntegerEmpty(getIdModeRecouvrement())) {
            setIdModeRecouvrement(FAEnteteFacture.CS_MODE_AUTOMATIQUE);
        }
        // Rôle
        if (JadeStringUtil.isIntegerEmpty(getIdRole())) {
            setIdRole(CaisseHelperFactory.CS_AFFILIE_PARITAIRE);
        }
    }

    /**
     * Initisalise l'entête avec les valeurs du plan de l'affiliation
     * 
     * @param planAffiliationId
     *            l'ID du plan d'affiliation sélectionné
     */
    public void initPlanValue(String planAffiliationId) throws Exception {
        if (JadeStringUtil.isEmpty(planAffiliationId)) {
            // pas d'IS plan, on utilise la méthode globale
            this.initDefaultPlanValue();
        } else {
            // association des attributs du plan
            AFPlanAffiliation plan = new AFPlanAffiliation();
            plan.setSession(getSession());
            plan.setPlanAffiliationId(planAffiliationId);
            plan.retrieve();
            if (!plan.isNew()) {
                setIdDomaineCourrier(plan.getDomaineCourrier());
                setIdDomaineLSV(plan.getDomaineRecouvrement());
                setIdDomaineRemboursement(plan.getDomaineRemboursement());
                setNonImprimable(plan.isBlocageEnvoi());
            }
        }

    }

    public void initTiers() {
        this.initTiers(null);
    }

    public void initTiers(BTransaction transaction) {
        this._getTiers(transaction);
    }

    public boolean isCategorieSection(String value) {
        return (Arrays.asList(APISection.CATEGORIE_SECTION).contains(value));
    }

    /**
     * nonImprimable signifie en fait impression séparée
     * 
     * Avant le mandat InfoRom336 cette méthode retournait l'attribut nonImprimable qui était un booléen représenté par
     * une case à cocher à l'écran
     * 
     * Dans le cadre du mandat InfoRom336, cette case à cocher a été remplacée par une liste déroulante contenant les
     * différents modes d'impression.
     * 
     * nonImprimable est utilisé à beaucoup d'endroits dans le code et le supprimer totalement aurait eu beaucoup
     * d'impacts.
     * 
     * La modification de la méthode ci-dessous uniquement, a permis de supprimer l'ancien système du front (case à
     * cocher supprimée de l'écran) tout en le faisant cohabiter avec le nouveau en back (viewBean à DB)
     * 
     * 
     */
    @Override
    public Boolean isNonImprimable() {
        if (JadeStringUtil.isBlankOrZero(getIdCSModeImpression())) {
            return nonImprimable;
        } else if (FAEnteteFacture.CS_MODE_IMP_SEPAREE.equalsIgnoreCase(getIdCSModeImpression())) {
            return new Boolean(true);
        } else {
            return new Boolean(false);
        }
    }

    public boolean isProcessusMasse() {
        return processusMasse;
    }

    public boolean isUseEntityForLSV() {
        return useEntityForLSV;
    }

    /*
     * Ajoute un afact à l'entête de facture Return true si la mise à jour a été effectué
     */
    public Boolean removeAfact(BTransaction trans, FAAfact afact) {
        if ((!afact.isNonComptabilisable().booleanValue()) && (!afact.isAQuittancer().booleanValue())) {
            FAEnteteFacture entete = new FAEnteteFacture();
            entete.setSession(trans.getSession());
            entete.setIdEntete(afact.getIdEnteteFacture());
            try {
                entete.retrieve(trans);
                // Cumul du montant
                FWCurrency total = new FWCurrency(JANumberFormatter.deQuote(entete.getTotalFacture()));
                FWCurrency remove = new FWCurrency(JANumberFormatter.deQuote(afact.getMontantFacture()));
                total.sub(remove);
                entete.setTotalFacture(total.toString());
                entete.update(trans);
                return new Boolean(true);
            } catch (Exception e) {
                return new Boolean(false);
            }
        } else {
            return new Boolean(false);
        }
    }

    /**
     * @param string
     */
    @Override
    public void setDateReceptionDS(String string) {
        dateReceptionDS = string;
    }

    @Override
    public void setEspion(String newEspion) {
        espion = newEspion;
    }

    public void setEstRentierNa(Boolean estRentierNa) {
        this.estRentierNa = estRentierNa;
    }

    /**
     * Sets the forceDelete.
     * 
     * @param forceDelete
     *            The forceDelete to set
     */
    public void setForceDelete(Boolean forceDelete) {
        this.forceDelete = forceDelete;
    }

    @Override
    public void setIdAdresse(String newIdAdresse) {
        idAdresse = newIdAdresse;
    }

    @Override
    public void setIdAdressePaiement(String newIdAdressePaiement) {
        idAdressePaiement = newIdAdressePaiement;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdControle(String idControle) {
        this.idControle = idControle;
    }

    public void setIdCSModeImpression(String idCSModeImpression) {
        this.idCSModeImpression = idCSModeImpression;
    }

    public void setIdDomaineCourrier(String string) {
        idDomaineCourrier = string;
    }

    public void setIdDomaineLSV(String string) {
        idDomaineLSV = string;
    }

    public void setIdDomaineRemboursement(String string) {
        idDomaineRemboursement = string;
    }

    /**
     * Setter
     */
    @Override
    public void setIdEntete(String newIdEntete) {
        idEntete = newIdEntete;
    }

    @Override
    public void setIdExterneFacture(String newIdExterneFacture) {
        idExterneFacture = newIdExterneFacture;
    }

    @Override
    public void setIdExterneRole(String newIdExterneRole) {
        idExterneRole = newIdExterneRole;
    }

    @Override
    public void setIdModeRecouvrement(String newIdModeRecouvrement) {
        idModeRecouvrement = newIdModeRecouvrement;
    }

    @Override
    public void setIdPassage(String newIdPassage) {
        idPassage = newIdPassage;
        // RAZ cache si id ne correspond pas
        /* CHANGED */if ((passage != null) && !passage.getIdPassage().equals(newIdPassage)) {
            /* CHANGED */passage = null;
        }

    }

    @Override
    public void setIdRemarque(String newIdRemarque) {
        idRemarque = newIdRemarque;
    }

    @Override
    public void setIdRole(String newIdRole) {
        idRole = newIdRole;
    }

    @Override
    public void setIdSoumisInteretsMoratoires(String newIdSoumisInteretsMoratoires) {
        idSoumisInteretsMoratoires = newIdSoumisInteretsMoratoires;
    }

    @Override
    public void setIdSousType(String newIdSousType) {
        idSousType = newIdSousType;
    }

    @Override
    public void setIdTiers(String newIdTiers) {
        idTiers = newIdTiers;
        // RAZ cache si id ne correspond pas
        if ((_tiers != null) && !_tiers.getIdTiers().equals(newIdTiers)) {
            _tiers = null;
        }
    }

    public void setIdTypeCourrier(String string) {
        idTypeCourrier = string;
    }

    @Override
    public void setIdTypeFacture(String newIdTypeFacture) {
        idTypeFacture = newIdTypeFacture;
    }

    /**
     * @param string
     */
    public void setLibelle(String string) {
        libelle = string;
    }

    @Override
    public void setMotifInteretsMoratoires(String newMotifInteretsMoratoires) {
        motifInteretsMoratoires = newMotifInteretsMoratoires;
    }

    @Override
    public void setNonImprimable(Boolean newNonImprimable) {
        // this.nonImprimable = newNonImprimable;
        if (newNonImprimable.booleanValue()) {
            idCSModeImpression = FAEnteteFacture.CS_MODE_IMP_SEPAREE;
        } else {
            idCSModeImpression = FAEnteteFacture.CS_MODE_IMP_STANDARD;
        }
    }

    public void seteBillTransactionID(String eBillTransactionID) {
        this.eBillTransactionID = eBillTransactionID;
    }

    public String geteBillTransactionID() {
        return eBillTransactionID;
    }

    public void addEBillTransactionID(BTransaction transaction) throws Exception {
        seteBillTransactionID(this._incCounter(transaction, "", FIELD_EBILL_TRANSACTION_ID));
    }

    @Override
    public void setNumCommune(String newNumCommune) {
        numCommune = newNumCommune;
    }

    public void setPassage(FAPassage passage) {
        this.passage = passage;
    }

    public void setPlanAffiliationId(String planAffiliationId) throws Exception {
        initPlanValue(planAffiliationId);
    }

    public void setProcessusMasse(boolean processusMasse) {
        this.processusMasse = processusMasse;
    }

    public void setReferenceFacture(String referenceFacture) {
        this.referenceFacture = referenceFacture;
    }

    @Override
    public void setRemarque(String newRemarque) {
        remarque = newRemarque;
    }

    @Override
    public void setTotalFacture(String newTotalFacture) {
        totalFacture = JANumberFormatter.deQuote(newTotalFacture);
    }

    public void setUseEntityForLSV(boolean b) {
        useEntityForLSV = b;
    }

    public String toMyString() {
        return "ID : " + getIdEntete() + " Non Imprimable : " + isNonImprimable() + ", " + getSpy();
    }

    /*
     * Recalcule le total de l'entête de facture pour modification ou ajout d'afact Return true si la mise à jour a été
     * effectué
     */
    public Boolean updateTotal(BTransaction transaction, FAAfact afact) {
        if (!afact.isNonComptabilisable().booleanValue()) {
            // Cumul des afacts
            FAAfactManager afactManager = new FAAfactManager();
            afactManager.setSession(transaction.getSession());
            afactManager.setForIdEnteteFacture(afact.getIdEnteteFacture());
            afactManager.setForAQuittancer(new Boolean(false));

            // Mise à jour du décompte
            FAEnteteFacture entete = new FAEnteteFacture();
            entete.setSession(transaction.getSession());
            entete.setIdEntete(afact.getIdEnteteFacture());
            try {
                String total = afactManager.getSum("montantfacture", transaction).toString();

                entete.retrieve(transaction);
                entete.setTotalFacture(total);
                transaction.disableSpy();
                entete.update(transaction);
                return new Boolean(true);
            } catch (Exception e) {
                _addError(transaction, "Erreur lors de la mise à jour du total du décompte. idEntete " + getIdEntete());
                return new Boolean(false);
            } finally {
                transaction.enableSpy();
            }

        } else {
            return new Boolean(false);
        }
    }
}
