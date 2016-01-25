package globaz.osiris.db.ordres;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APISynchronisable;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.external.IntAdressePaiement;
import globaz.osiris.translation.CACodeSystem;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementDataManager;
import java.io.Serializable;

public class CAOrdreVersement extends BEntity implements Serializable, APISynchronisable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String BVR = "210002";
    public final static String CODE_ISO_MONNAIE_CHF = "CHF";
    public final static String CODE_PAYS_SUISSE = "CH";
    public static final String FIELD_CODEISOMONBON = "CODEISOMONBON";
    public static final String FIELD_CODEISOMONDEP = "CODEISOMONDEP";
    public static final String FIELD_CODEISOPAYS = "CODEISOPAYS";
    public static final String FIELD_COURSCONVERSION = "COURSCONVERSION";
    public static final String FIELD_ESTBLOQUE = "ESTBLOQUE";
    public static final String FIELD_ESTRETIRE = "ESTRETIRE";
    public static final String FIELD_IDADRESSEPAIEMENT = "IDADRESSEPAIEMENT";
    public static final String FIELD_IDBANQUE = "IDBANQUE";
    public static final String FIELD_IDORDRE = "IDORDRE";
    public static final String FIELD_IDORDREGROUPE = "IDORDREGROUPE";
    public static final String FIELD_IDORGANEEXECUTION = "IDORGANEEXECUTION";
    public static final String FIELD_MOTIF = "MOTIF";
    public static final String FIELD_NATUREORDRE = "NATUREORDRE";
    public static final String FIELD_NOCOMPTE = "NOCOMPTE";
    public static final String FIELD_NOMCACHE = "NOMCACHE";
    public static final String FIELD_NUMTRANSACTION = "NUMTRANSACTION";
    public static final String FIELD_REFERENCEBVR = "REFERENCEBVR";

    public static final String FIELD_TYPEORDRE = "TYPEORDRE";

    public static final String FIELD_TYPEVIREMENT = "TYPEVIREMENT";
    public static final String FIELD_VALEURCONVERSION = "VALEURCONVERSION";

    public static final String TABLE_CAOPOVP = "CAOPOVP";
    public final static String VIREMENT = "210001";

    private IntAdressePaiement _adressePaiement = null;
    private TIAdressePaiementData _adressePaiementData = null;
    private CAOrdreGroupe _ordreGroupe = null;
    private String codeISOMonnaieBonification = new String();
    private String codeISOMonnaieDepot = new String();
    private String codeISOPays = new String();
    private String coursConversion = new String();
    private FWParametersSystemCodeManager csMonnaies = null;
    // code systeme
    private FWParametersSystemCode csNatureOrdre = null;
    private FWParametersSystemCodeManager csNatureOrdres = null;
    private FWParametersSystemCode csTypeVirement = null;
    private FWParametersSystemCodeManager csTypeVirements = null;
    private Boolean estBloque = new Boolean(false);
    private Boolean estRetire = new Boolean(false);
    private String idAdressePaiement = new String();
    private String idBanque = new String();

    private String idOrdre = new String();
    private String idOrdreGroupe = new String();
    private String idOrganeExecution = new String();

    private FWMemoryLog memoryLog;

    private String motif = new String();
    private String natureOrdre = new String();
    private String noCompte = new String();
    private String nomCache = new String();
    private String numTransaction = new String();
    private String referenceBVR = new String();
    private String typeOrdre = new String();

    private String typeVirement = new String();

    private FWParametersUserCode ucNatureOrdre = null;
    private FWParametersUserCode ucTypeVirement = null;
    private String valeurConversion = new String();

    /**
     * Commentaire relatif au constructeur CAOrdreVersement
     */
    public CAOrdreVersement() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.02.2002 10:22:41)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) {
        setTypeOrdre(APIOperation.CAOPERATIONORDREVERSEMENT);

        // Synchroniser les données externes
        synchroniser();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.02.2002 13:42:22)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) {

        // Synchroniser les données externes
        synchroniser();

        // Si l'idOrdreGroupe == 0 on ne fait pas tout ce qui suit
        if (!"0".equalsIgnoreCase(getIdOrdreGroupe())) {

            // Si l'on décide de bloquer l'odre de versement,
            // il faut le retirer de la liste de l'ordre de groupement et
            // remettre à jour le nombre de transaction + montant du groupement.
            if (getEstBloque().booleanValue()) {

                // On supprime l'ordre de versement du groupement en settant
                // l'id du groupe à 0.
                // l'élément ne doit pas être supprimé.
                String previousIdOrdreGroupe = getIdOrdreGroupe();
                setIdOrdreGroupe("0");

                try {
                    // récupération de l'ordre de groupement
                    CAOrdreGroupe ordreGroupe = new CAOrdreGroupe();
                    ordreGroupe.setSession(getSession());
                    ordreGroupe.setIdOrdreGroupe(previousIdOrdreGroupe);
                    ordreGroupe.retrieve(transaction);
                    if ((ordreGroupe == null) || ordreGroupe.isNew()) {
                        _addError(transaction, "L'ordre de groupe n'existe pas pour ce versement !!! idOrdreGroupe = "
                                + previousIdOrdreGroupe);
                        return;
                    }

                    FWCurrency total = ordreGroupe.getTotalToCurrency();
                    String nbrTrans = ordreGroupe.getNombreTransactions();
                    int iNbrTrans = 1;
                    if (nbrTrans != null) {
                        iNbrTrans = Integer.valueOf(nbrTrans).intValue();
                    }

                    iNbrTrans--;

                    CAOperation oper = new CAOperation();
                    oper.setSession(getSession());
                    oper.setIdOperation(getIdOrdre());
                    oper.retrieve(transaction);

                    total.sub(oper.getMontant());

                    ordreGroupe.setTotal(total.toString());
                    ordreGroupe.setNombreTransactions(String.valueOf(iNbrTrans));
                    ordreGroupe.update(transaction);
                } catch (Exception e) {
                    e.printStackTrace();
                    _addError(transaction, e.getMessage());
                }

            }
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return CAOrdreVersement.TABLE_CAOPOVP;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        codeISOMonnaieBonification = statement.dbReadString(CAOrdreVersement.FIELD_CODEISOMONBON);
        codeISOMonnaieDepot = statement.dbReadString(CAOrdreVersement.FIELD_CODEISOMONDEP);
        coursConversion = statement.dbReadNumeric(CAOrdreVersement.FIELD_COURSCONVERSION, 2);
        estBloque = statement.dbReadBoolean(CAOrdreVersement.FIELD_ESTBLOQUE);
        estRetire = statement.dbReadBoolean(CAOrdreVersement.FIELD_ESTRETIRE);
        idAdressePaiement = statement.dbReadNumeric(CAOrdreVersement.FIELD_IDADRESSEPAIEMENT);
        idOrdre = statement.dbReadNumeric(CAOrdreVersement.FIELD_IDORDRE);
        idOrdreGroupe = statement.dbReadNumeric(CAOrdreVersement.FIELD_IDORDREGROUPE);
        idOrganeExecution = statement.dbReadNumeric(CAOrdreVersement.FIELD_IDORGANEEXECUTION);
        motif = statement.dbReadString(CAOrdreVersement.FIELD_MOTIF);
        natureOrdre = statement.dbReadNumeric(CAOrdreVersement.FIELD_NATUREORDRE);
        nomCache = statement.dbReadString(CAOrdreVersement.FIELD_NOMCACHE);
        numTransaction = statement.dbReadNumeric(CAOrdreVersement.FIELD_NUMTRANSACTION);
        referenceBVR = statement.dbReadString(CAOrdreVersement.FIELD_REFERENCEBVR);
        typeVirement = statement.dbReadNumeric(CAOrdreVersement.FIELD_TYPEVIREMENT);
        valeurConversion = statement.dbReadDateAMJ(CAOrdreVersement.FIELD_VALEURCONVERSION);
        typeOrdre = statement.dbReadString(CAOrdreVersement.FIELD_TYPEORDRE);
        codeISOPays = statement.dbReadString(CAOrdreVersement.FIELD_CODEISOPAYS);
        idBanque = statement.dbReadNumeric(CAOrdreVersement.FIELD_IDBANQUE);
        noCompte = statement.dbReadString(CAOrdreVersement.FIELD_NOCOMPTE);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdOrdre(), getSession().getLabel("7260"));
        _propertyMandatory(statement.getTransaction(), getTypeOrdre(), getSession().getLabel("7261"));

        if (!getTypeOrdre().equals(APIOperation.CAOPERATIONORDRERECOUVREMENT)
                && !getTypeOrdre().equals(APIOperation.CAOPERATIONORDREVERSEMENT)) {
            _addError(statement.getTransaction(), getSession().getLabel("7262"));
        }

    }

    /**
     * Validation des données Date de création : (30.01.2002 07:52:07)
     */
    public void _valider() {

        // Vérifier l'id paiement
        // if (JadeStringUtil.isIntegerEmpty(getIdAdressePaiement()))
        // getMemoryLog().logMessage("5137", null, FWMessage.ERREUR,
        // getClass().getName());

        // Vérifier le code ISO pour la monnaie de bonification
        if (JadeStringUtil.isBlank(getCodeISOMonnaieBonification())) {
            getMemoryLog().logMessage("5138", null, FWMessage.ERREUR, this.getClass().getName());
        }

        // Vérifier le code ISO pour la monnaie de dépôt
        if (JadeStringUtil.isBlank(getCodeISOMonnaieDepot())) {
            getMemoryLog().logMessage("5139", null, FWMessage.ERREUR, this.getClass().getName());
        }

        // Vérifier la nature de l'ordre
        if (JadeStringUtil.isIntegerEmpty(getNatureOrdre())) {
            getMemoryLog().logMessage("5140", null, FWMessage.ERREUR, this.getClass().getName());
        }

        // Vérifier le type de virement
        if (JadeStringUtil.isIntegerEmpty(getTypeVirement())) {
            getMemoryLog().logMessage("5141", null, FWMessage.ERREUR, this.getClass().getName());
        }

        // Vérifier le numéro de la transaction
        if (!JadeStringUtil.isIntegerEmpty(getNumTransaction())) {
            float _noTr = 0f;
            try {
                _noTr = Float.parseFloat(getNumTransaction());
            } catch (Exception e) {
            }
            if (_noTr < 0) {
                getMemoryLog().logMessage("5148", getNumTransaction(), FWMessage.ERREUR, this.getClass().getName());
            }
        }

        // S'il n'y a pas d'erreur
        if (getMemoryLog().getErrorLevel().compareTo(FWMessage.ERREUR) < 0) {

            // Contrôle de la nature de l'ordre
            if (getCsNatureOrdres().getCodeSysteme(getNatureOrdre()) == null) {
                getMemoryLog().logMessage("5142", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Contrôle du type de virement
            if (getCsTypeVirements().getCodeSysteme(getTypeVirement()) == null) {
                getMemoryLog().logMessage("5143", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Vérifier la référence BVR
            if (JadeStringUtil.isBlank(getReferenceBVR()) && getTypeVirement().equals(CAOrdreVersement.BVR)) {
                getMemoryLog().logMessage("5144", null, FWMessage.ERREUR, this.getClass().getName());
            }
            if (!JadeStringUtil.isBlank(getReferenceBVR()) && !getTypeVirement().equals(CAOrdreVersement.BVR)) {
                getMemoryLog().logMessage("5145", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Vérifier l'adresse de paiement
            if (getAdressePaiement() != null) {
                // Contrôler que l'adresse de paiement soit en Suisse
                if (!getAdressePaiement().getCodeISOPays().equals(CAOrdreVersement.CODE_PAYS_SUISSE)) {
                    if (getCodeISOMonnaieBonification().equals(CAOrdreVersement.CODE_ISO_MONNAIE_CHF)) {
                        getMemoryLog().logMessage("7393", null, FWMessage.ERREUR, this.getClass().getName());
                    }
                }
            }

            // Véfifier l'ordre groupé
            if (!JadeStringUtil.isIntegerEmpty(getIdOrdreGroupe())) {
                if (this.getOrdreGroupe() == null) {
                    getMemoryLog().logMessage("5147", getIdOrdreGroupe(), FWMessage.ERREUR, this.getClass().getName());
                }
            }

        }

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CAOrdreVersement.FIELD_IDORDRE,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrdre(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CAOrdreVersement.FIELD_CODEISOMONBON,
                this._dbWriteString(statement.getTransaction(), getCodeISOMonnaieBonification(), "codeISOMonBon"));
        statement.writeField(CAOrdreVersement.FIELD_CODEISOMONDEP,
                this._dbWriteString(statement.getTransaction(), getCodeISOMonnaieDepot(), "CodeIsoMonDep"));
        statement.writeField(CAOrdreVersement.FIELD_COURSCONVERSION,
                this._dbWriteNumeric(statement.getTransaction(), getCoursConversion(), "coursConversion"));
        statement.writeField(CAOrdreVersement.FIELD_ESTBLOQUE, this._dbWriteBoolean(statement.getTransaction(),
                getEstBloque(), BConstants.DB_TYPE_BOOLEAN_CHAR, "estBloque"));
        statement.writeField(CAOrdreVersement.FIELD_ESTRETIRE, this._dbWriteBoolean(statement.getTransaction(),
                getEstRetire(), BConstants.DB_TYPE_BOOLEAN_CHAR, "estRetire"));
        statement.writeField(CAOrdreVersement.FIELD_IDADRESSEPAIEMENT,
                this._dbWriteNumeric(statement.getTransaction(), getIdAdressePaiement(), "idAdressePaiement"));
        statement.writeField(CAOrdreVersement.FIELD_IDORDRE,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrdre(), "idOrdre"));
        statement.writeField(CAOrdreVersement.FIELD_IDORDREGROUPE,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrdreGroupe(), "idOrdreGroupe"));
        statement.writeField(CAOrdreVersement.FIELD_IDORGANEEXECUTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrganeExecution(), "idOrganeExecution"));
        statement.writeField(CAOrdreVersement.FIELD_MOTIF,
                this._dbWriteString(statement.getTransaction(), getMotif(), "motif"));
        statement.writeField(CAOrdreVersement.FIELD_NATUREORDRE,
                this._dbWriteNumeric(statement.getTransaction(), getNatureOrdre(), "natureOrdre"));
        statement.writeField(CAOrdreVersement.FIELD_NOMCACHE,
                this._dbWriteString(statement.getTransaction(), getNomCache(), "nomCache"));
        statement.writeField(CAOrdreVersement.FIELD_NUMTRANSACTION,
                this._dbWriteNumeric(statement.getTransaction(), getNumTransaction(), "numTransaction"));
        statement.writeField(CAOrdreVersement.FIELD_REFERENCEBVR,
                this._dbWriteString(statement.getTransaction(), getReferenceBVR(), "referenceBVR"));
        statement.writeField(CAOrdreVersement.FIELD_TYPEVIREMENT,
                this._dbWriteNumeric(statement.getTransaction(), getTypeVirement(), "typeVirement"));
        statement.writeField(CAOrdreVersement.FIELD_VALEURCONVERSION,
                this._dbWriteDateAMJ(statement.getTransaction(), getValeurConversion(), "valeurConversion"));
        statement.writeField(CAOrdreVersement.FIELD_TYPEORDRE,
                this._dbWriteString(statement.getTransaction(), getTypeOrdre(), "typeOrdre"));
        statement.writeField(CAOrdreVersement.FIELD_CODEISOPAYS,
                this._dbWriteString(statement.getTransaction(), getCodeISOPays(), "codeISOPays"));
        statement.writeField(CAOrdreVersement.FIELD_IDBANQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdBanque(), "idBanque"));
        statement.writeField(CAOrdreVersement.FIELD_NOCOMPTE,
                this._dbWriteString(statement.getTransaction(), getNoCompte(), "noCompte"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.02.2002 11:01:56)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntAdressePaiement
     */
    public IntAdressePaiement getAdressePaiement() {

        // Si l'ordre groupé n'existe pas, retourner null
        if (JadeStringUtil.isIntegerEmpty(getIdAdressePaiement())) {
            return null;
        }

        // Si log pas déjà chargé
        if (_adressePaiement == null) {
            // Instancier une nouvelle adresse de paiement
            try {
                CAApplication currentApplication = CAApplication.getApplicationOsiris();
                _adressePaiement = (IntAdressePaiement) GlobazServer.getCurrentSystem()
                        .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                        .getImplementationFor(getSession(), IntAdressePaiement.class);

                // Récupérer l'adresse
                _adressePaiement.retrieve(getIdAdressePaiement());
                if (_adressePaiement.isNew()) {
                    _adressePaiement = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _adressePaiement = null;
            }
        }

        return _adressePaiement;

    }

    public TIAdressePaiementData getAdressePaiementData() {

        // Si l'ordre groupé n'existe pas, retourner null
        if (JadeStringUtil.isIntegerEmpty(getIdAdressePaiement())) {
            return null;
        }

        // Si log pas déjà chargé
        if (_adressePaiementData == null) {
            // Instancier une nouvelle adresse de paiement
            try {
                TIAdressePaiementDataManager manager = new TIAdressePaiementDataManager();
                manager.setSession(getSession());
                manager.setForIdUnique(getIdAdressePaiement());
                manager.find(getSession().getCurrentThreadTransaction());

                if (manager.size() > 0) {
                    _adressePaiementData = (TIAdressePaiementData) manager.getFirstEntity();
                }

            } catch (Exception e) {
                _addError(null, e.getMessage());
                _adressePaiementData = null;
            }
        }

        return _adressePaiementData;

    }

    /**
     * Getter
     */
    public String getCodeISOMonnaieBonification() {
        return codeISOMonnaieBonification;
    }

    public String getCodeISOMonnaieDepot() {
        return codeISOMonnaieDepot;
    }

    /**
     * @return code ISO du pays
     */
    public String getCodeISOPays() {
        return codeISOPays;
    }

    public String getCoursConversion() {
        return coursConversion;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2002 17:08:25)
     * 
     * @return globaz.globall.parameters.FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsMonnaies() {
        // liste déjà chargée ?
        if (csMonnaies == null) {
            // liste pas encore chargée, on la charge
            csMonnaies = new FWParametersSystemCodeManager();
            csMonnaies.setSession(getSession());
            csMonnaies.getListeCodesSup("OSIMONNAI", getSession().getIdLangue());
        }
        return csMonnaies;
    }

    public FWParametersSystemCode getCsNatureOrdre() {

        if (csNatureOrdre == null) {
            // liste pas encore chargee, on la charge
            csNatureOrdre = new globaz.globall.parameters.FWParametersSystemCode();
            csNatureOrdre.getCode(getNatureOrdre());
        }
        return csNatureOrdre;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2002 17:08:25)
     * 
     * @return globaz.globall.parameters.FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsNatureOrdres() {
        if (csNatureOrdres == null) {
            csNatureOrdres = CACodeSystem.getNatureVersementsManager(getSession());
        }
        return csNatureOrdres;
    }

    public FWParametersSystemCode getCsTypeVirement() {

        if (csTypeVirement == null) {
            // liste pas encore chargee, on la charge
            csTypeVirement = new FWParametersSystemCode();
            csTypeVirement.getCode(getTypeVirement());
        }
        return csTypeVirement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2002 17:08:25)
     * 
     * @return globaz.globall.parameters.FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsTypeVirements() {
        // liste déjà chargée ?
        if (csTypeVirements == null) {
            // liste pas encore chargée, on la charge
            csTypeVirements = new FWParametersSystemCodeManager();
            csTypeVirements.setSession(getSession());
            csTypeVirements.getListeCodesSup("OSITYPOVE", getSession().getIdLangue());
        }
        return csTypeVirements;
    }

    public Boolean getEstBloque() {
        return estBloque;
    }

    public Boolean getEstRetire() {
        return estRetire;
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    /**
     * @return id de la banque
     */
    public String getIdBanque() {
        return idBanque;
    }

    public String getIdOrdre() {
        return idOrdre;
    }

    public String getIdOrdreGroupe() {
        return idOrdreGroupe;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.02.2002 08:29:08)
     * 
     * @return globaz.osiris.db.utils.FWMemoryLog
     */
    public FWMemoryLog getMemoryLog() {
        if (memoryLog == null) {
            memoryLog = new FWMemoryLog();
            memoryLog.setSession(getSession());
        }
        return memoryLog;
    }

    public String getMotif() {
        return motif;
    }

    public String getNatureOrdre() {
        return natureOrdre;
    }

    /**
     * @return numéro du compte
     */
    public String getNoCompte() {
        return noCompte;
    }

    public String getNomCache() {
        return nomCache;
    }

    public String getNumTransaction() {
        return numTransaction;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 13:04:26)
     * 
     * @return globaz.osiris.db.ordres.CAOrdreGroupe
     */
    public CAOrdreGroupe getOrdreGroupe() {

        // Si l'ordre groupé n'existe pas, retourner null
        if (JadeStringUtil.isIntegerEmpty(getIdOrdreGroupe())) {
            return null;
        }

        // Si log pas déjà chargé
        if (_ordreGroupe == null) {
            // Instancier un nouvel ordre groupé
            _ordreGroupe = new CAOrdreGroupe();
            _ordreGroupe.setSession(getSession());

            // Récupérer le log en question
            _ordreGroupe.setIdOrdreGroupe(getIdOrdreGroupe());
            try {
                _ordreGroupe.retrieve();
                if (_ordreGroupe.isNew() || _ordreGroupe.hasErrors()) {
                    return null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                return null;
            }
        }

        return _ordreGroupe;
    }

    public CAOrdreGroupe getOrdreGroupe(String idOrdreGroupe) {

        // Si l'ordre groupé n'existe pas, retourner null
        if (JadeStringUtil.isIntegerEmpty(idOrdreGroupe)) {
            return null;
        }

        // Instancier un nouvel ordre groupé
        CAOrdreGroupe ordreGroupe = new CAOrdreGroupe();
        ordreGroupe.setSession(getSession());

        // Récupérer le log en question
        ordreGroupe.setIdOrdreGroupe(idOrdreGroupe);
        try {
            ordreGroupe.retrieve();
            if (ordreGroupe.isNew() || ordreGroupe.hasErrors()) {
                return null;
            }
        } catch (Exception e) {
            _addError(null, e.getMessage());
            return null;
        }

        return ordreGroupe;
    }

    public String getReferenceBVR() {
        return referenceBVR;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.02.2002 10:25:40)
     * 
     * @return String
     */
    public String getTypeOrdre() {
        return typeOrdre;
    }

    public String getTypeVirement() {
        return typeVirement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2002 16:36:04)
     * 
     * @return globaz.globall.parameters.FWParametersUserCode
     */
    public FWParametersUserCode getUcNatureOrdre() {

        if (ucNatureOrdre == null) {
            // liste pas encore chargee, on la charge
            ucNatureOrdre = new FWParametersUserCode();
            ucNatureOrdre.setSession(getSession());

            // Récupérer le code système dans la langue de l'utilisateur
            ucNatureOrdre.setIdCodeSysteme(getNatureOrdre());
            ucNatureOrdre.setIdLangue(getSession().getIdLangue());
            try {
                ucNatureOrdre.retrieve();
                if (ucNatureOrdre.isNew() || ucNatureOrdre.hasErrors()) {
                    _addError(null, getSession().getLabel("7324"));
                }
            } catch (Exception e) {
                _addError(null, getSession().getLabel("7324"));
            }
        }

        return ucNatureOrdre;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2002 16:36:04)
     * 
     * @return globaz.globall.parameters.FWParametersUserCode
     */
    public FWParametersUserCode getUcTypeVirement() {

        if (ucTypeVirement == null) {
            // liste pas encore chargee, on la charge
            ucTypeVirement = new FWParametersUserCode();
            ucTypeVirement.setSession(getSession());

            // Récupérer le code système dans la langue de l'utilisateur
            ucTypeVirement.setIdCodeSysteme(getTypeVirement());
            ucTypeVirement.setIdLangue(getSession().getIdLangue());
            try {
                ucTypeVirement.retrieve();
                if (ucTypeVirement.isNew() || ucTypeVirement.hasErrors()) {
                    _addError(null, getSession().getLabel("7324"));
                }
            } catch (Exception e) {
                _addError(null, getSession().getLabel("7324"));
            }
        }

        return ucTypeVirement;
    }

    public String getValeurConversion() {
        return valeurConversion;
    }

    /**
     * Setter
     */
    public void setCodeISOMonnaieBonification(String newCodeISOMonnaieBonification) {
        codeISOMonnaieBonification = newCodeISOMonnaieBonification;
    }

    public void setCodeISOMonnaieDepot(String newCodeISOMonnaieDepot) {
        codeISOMonnaieDepot = newCodeISOMonnaieDepot;
    }

    /**
     * @param string
     */
    public void setCodeISOPays(String string) {
        codeISOPays = string;
    }

    public void setCoursConversion(String newCoursConversion) {
        coursConversion = newCoursConversion;
    }

    public void setEstBloque(Boolean newEstBloque) {
        estBloque = newEstBloque;
    }

    public void setEstRetire(Boolean newEstRetire) {
        estRetire = newEstRetire;
    }

    public void setIdAdressePaiement(String newIdAdressePaiement) {
        idAdressePaiement = newIdAdressePaiement;
        _adressePaiement = null;
    }

    /**
     * @param string
     */
    public void setIdBanque(String string) {
        idBanque = string;
    }

    public void setIdOrdre(String newIdOrdre) {
        idOrdre = newIdOrdre;
    }

    public void setIdOrdreGroupe(String newIdOrdreGroupe) {
        idOrdreGroupe = newIdOrdreGroupe;
        _ordreGroupe = null;
    }

    public void setIdOrganeExecution(String newIdOrganeExecution) {
        idOrganeExecution = newIdOrganeExecution;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.02.2002 08:30:10)
     * 
     * @param newMemoryLog
     *            globaz.osiris.db.utils.FWMemoryLog
     */
    public void setMemoryLog(FWMemoryLog newMemoryLog) {
        memoryLog = newMemoryLog;
    }

    public void setMotif(String newMotif) {
        motif = newMotif;
    }

    public void setNatureOrdre(String newNatureOrdre) {
        natureOrdre = newNatureOrdre;
        csNatureOrdre = null;
        ucNatureOrdre = null;
    }

    /**
     * @param string
     */
    public void setNoCompte(String string) {
        noCompte = string;
    }

    public void setNomCache(String newNomCache) {
        if (newNomCache.length() >= 40) {
            nomCache = newNomCache.substring(0, 40);
        } else {
            nomCache = newNomCache;
        }
    }

    public void setNumTransaction(String newNumTransaction) {
        numTransaction = newNumTransaction;
    }

    public void setReferenceBVR(String newReferenceBVR) {
        referenceBVR = newReferenceBVR;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.02.2002 10:25:59)
     * 
     * @param newTypeOrdre
     *            String
     */
    public void setTypeOrdre(String newTypeOrdre) {
        typeOrdre = newTypeOrdre;
    }

    public void setTypeVirement(String newTypeVirement) {
        typeVirement = newTypeVirement;
        csTypeVirement = null;
        ucTypeVirement = null;
    }

    public void setValeurConversion(String newValeurConversion) {
        valeurConversion = newValeurConversion;
    }

    /**
     * Synchroniser le nom cache Date de création : (22.02.2002 13:39:08)
     */
    @Override
    public void synchroniser() {

        // Synchroniser le nom cache
        if (!JadeStringUtil.isIntegerEmpty(getIdOrdreGroupe())) {
            try {
                if (!JadeStringUtil.isBlank(getAdressePaiement().getAdresseCourrier().getAutreNom())) {
                    setNomCache(getAdressePaiement().getAdresseCourrier().getAutreNom());
                } else {
                    setNomCache(getAdressePaiement().getNomTiersAdrPmt());
                }
            } catch (NullPointerException e) {
                setNomCache("");
            }
        } else {
            setNomCache("");
        }
    }

}
