package globaz.osiris.db.ordres;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BConstants;
import globaz.globall.db.GlobazServer;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APISynchronisable;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.external.IntAdressePaiement;
import globaz.osiris.translation.CACodeSystem;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementDataManager;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (13.12.2001 13:55:34)
 * 
 * @author: Administrator
 */
public class CAOrdreRecouvrement extends globaz.globall.db.BEntity implements java.io.Serializable, APISynchronisable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
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

    public final static java.lang.String TYPE_ORDRE_RECOUVREMENT = "R";
    // Constantes
    public final static String VIREMENT = "210001";
    private IntAdressePaiement _adressePaiement = null;
    private TIAdressePaiementData _adressePaiementData = null;
    private CAOrdreGroupe _ordreGroupe = null;
    private java.lang.String codeISOMonnaieBonification = new String();
    private java.lang.String codeISOMonnaieDepot = new String();
    private String codeISOPays = new String();
    private java.lang.String coursConversion = new String();
    private FWParametersSystemCodeManager csMonnaies = null;
    // code systeme
    private FWParametersSystemCode csNatureOrdre = null;
    private FWParametersSystemCodeManager csNatureOrdres = null;
    private FWParametersSystemCode csTypeVirement = null;
    private FWParametersSystemCodeManager csTypeVirements = null;
    private java.lang.Boolean estBloque = new Boolean(false);
    private java.lang.Boolean estRetire = new Boolean(false);
    private java.lang.String idAdressePaiement = new String();
    private String idBanque = new String();
    private java.lang.String idOrdre = new String();
    private java.lang.String idOrdreGroupe = new String();

    private java.lang.String idOrganeExecution = new String();
    private FWMemoryLog memoryLog;
    private java.lang.String motif = new String();
    private java.lang.String motifRefus = new String();
    private java.lang.String natureOrdre = new String();
    private String noCompte = new String();
    private java.lang.String nomCache = new String();

    private java.lang.String numTransaction = new String();
    private java.lang.String referenceBVR = new String();
    private java.lang.String typeOrdre = new String();
    private java.lang.String typeVirement = new String();
    private FWParametersUserCode ucNatureOrdre = null;
    private FWParametersUserCode ucTypeVirement = null;
    private java.lang.String valeurConversion = new String();

    /**
     * Commentaire relatif au constructeur CAOrdreVersement
     */
    public CAOrdreRecouvrement() {
        super();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.02.2002 10:22:41)
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) {
        setTypeOrdre(CAOrdreRecouvrement.TYPE_ORDRE_RECOUVREMENT);

        // Synchroniser les donn�es externes
        synchroniser();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.02.2002 13:42:22)
     */
    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) {

        // Synchroniser les donn�es externes
        synchroniser();
        // Si l'idOrdreGroupe == 0 on ne fait pas tout ce qui suit
        if (!"0".equalsIgnoreCase(getIdOrdreGroupe())) {

            // Si l'on d�cide de bloquer l'odre de versement,
            // il faut le retirer de la liste de l'ordre de groupement et
            // remettre � jour le nombre de transaction + montant du groupement.
            if (getEstBloque().booleanValue()) {

                // On supprime l'ordre de versement du groupement en settant
                // l'id du groupe � 0.
                // l'�l�ment ne doit pas �tre supprim�.
                String previousIdOrdreGroupe = getIdOrdreGroupe();
                setIdOrdreGroupe("0");

                try {
                    // r�cup�ration de l'ordre de groupement
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
        return "CAOPOVP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        codeISOMonnaieBonification = statement.dbReadString(CAOrdreRecouvrement.FIELD_CODEISOMONBON);
        codeISOMonnaieDepot = statement.dbReadString(CAOrdreRecouvrement.FIELD_CODEISOMONDEP);
        coursConversion = statement.dbReadNumeric(CAOrdreRecouvrement.FIELD_COURSCONVERSION, 2);
        estBloque = statement.dbReadBoolean(CAOrdreRecouvrement.FIELD_ESTBLOQUE);
        estRetire = statement.dbReadBoolean(CAOrdreRecouvrement.FIELD_ESTRETIRE);
        idAdressePaiement = statement.dbReadNumeric(CAOrdreRecouvrement.FIELD_IDADRESSEPAIEMENT);
        idOrdre = statement.dbReadNumeric(CAOrdreRecouvrement.FIELD_IDORDRE);
        idOrdreGroupe = statement.dbReadNumeric(CAOrdreRecouvrement.FIELD_IDORDREGROUPE);
        idOrganeExecution = statement.dbReadNumeric(CAOrdreRecouvrement.FIELD_IDORGANEEXECUTION);
        motif = statement.dbReadString(CAOrdreRecouvrement.FIELD_MOTIF);
        natureOrdre = statement.dbReadNumeric(CAOrdreRecouvrement.FIELD_NATUREORDRE);
        nomCache = statement.dbReadString(CAOrdreRecouvrement.FIELD_NOMCACHE);
        numTransaction = statement.dbReadNumeric(CAOrdreRecouvrement.FIELD_NUMTRANSACTION);
        referenceBVR = statement.dbReadString(CAOrdreRecouvrement.FIELD_REFERENCEBVR);
        typeVirement = statement.dbReadNumeric(CAOrdreRecouvrement.FIELD_TYPEVIREMENT);
        valeurConversion = statement.dbReadDateAMJ(CAOrdreRecouvrement.FIELD_VALEURCONVERSION);
        typeOrdre = statement.dbReadString(CAOrdreRecouvrement.FIELD_TYPEORDRE);
        codeISOPays = statement.dbReadString(CAOrdreRecouvrement.FIELD_CODEISOPAYS);
        idBanque = statement.dbReadNumeric(CAOrdreRecouvrement.FIELD_IDBANQUE);
        noCompte = statement.dbReadString(CAOrdreRecouvrement.FIELD_NOCOMPTE);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdOrdre(), getSession().getLabel("7260"));
        _propertyMandatory(statement.getTransaction(), getTypeOrdre(), getSession().getLabel("7261"));

        if (!getTypeOrdre().equals(CAOrdreRecouvrement.TYPE_ORDRE_RECOUVREMENT)) {
            _addError(statement.getTransaction(), getSession().getLabel("7262"));
        }

    }

    /**
     * Validation des donn�es Date de cr�ation : (30.01.2002 07:52:07)
     */
    public void _valider() {

        // V�rifier l'id paiement
        if (JadeStringUtil.isIntegerEmpty(getIdAdressePaiement())) {
            getMemoryLog().logMessage("5137", null, FWMessage.ERREUR, this.getClass().getName());
        }

        // V�rifier la nature de l'ordre
        if (JadeStringUtil.isIntegerEmpty(getNatureOrdre())) {
            getMemoryLog().logMessage("5140", null, FWMessage.ERREUR, this.getClass().getName());
        }

        // V�rifier le num�ro de la transaction
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

            // Contr�le de la nature de l'ordre
            if (getCsNatureOrdres().getCodeSysteme(getNatureOrdre()) == null) {
                getMemoryLog().logMessage("5142", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // V�rifier l'adresse de paiement
            if (getAdressePaiement() == null) {
                getMemoryLog().logMessage("5146", getIdAdressePaiement(), FWMessage.ERREUR, this.getClass().getName());
            } else {
                setCodeISOPays(getAdressePaiement().getCodeISOPays());
                setNoCompte(getAdressePaiement().getNumCompte());
                setIdBanque(getAdressePaiement().getIdBanque());
            }

            // V�fifier l'ordre group�
            if (!JadeStringUtil.isIntegerEmpty(getIdOrdreGroupe())) {
                if (getOrdreGroupe() == null) {
                    getMemoryLog().logMessage("5147", getIdOrdreGroupe(), FWMessage.ERREUR, this.getClass().getName());
                }
            }

        }

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(CAOrdreRecouvrement.FIELD_IDORDRE,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrdre(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(CAOrdreRecouvrement.FIELD_CODEISOMONBON,
                this._dbWriteString(statement.getTransaction(), getCodeISOMonnaieBonification(), "codeISOMonBon"));
        statement.writeField(CAOrdreRecouvrement.FIELD_CODEISOMONDEP,
                this._dbWriteString(statement.getTransaction(), getCodeISOMonnaieDepot(), "codeISOMonDep"));
        statement.writeField(CAOrdreRecouvrement.FIELD_COURSCONVERSION,
                this._dbWriteNumeric(statement.getTransaction(), getCoursConversion(), "coursConversion"));
        statement.writeField(CAOrdreRecouvrement.FIELD_ESTBLOQUE, this._dbWriteBoolean(statement.getTransaction(),
                getEstBloque(), BConstants.DB_TYPE_BOOLEAN_CHAR, "estBloque"));
        statement.writeField(CAOrdreRecouvrement.FIELD_ESTRETIRE, this._dbWriteBoolean(statement.getTransaction(),
                getEstRetire(), BConstants.DB_TYPE_BOOLEAN_CHAR, "estRetire"));
        statement.writeField(CAOrdreRecouvrement.FIELD_IDADRESSEPAIEMENT,
                this._dbWriteNumeric(statement.getTransaction(), getIdAdressePaiement(), "idAdressePaiement"));
        statement.writeField(CAOrdreRecouvrement.FIELD_IDORDRE,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrdre(), "idOrdre"));
        statement.writeField(CAOrdreRecouvrement.FIELD_IDORDREGROUPE,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrdreGroupe(), "idOrdreGroupe"));
        statement.writeField(CAOrdreRecouvrement.FIELD_IDORGANEEXECUTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrganeExecution(), "idOrganeExecution"));
        statement.writeField(CAOrdreRecouvrement.FIELD_MOTIF,
                this._dbWriteString(statement.getTransaction(), getMotif(), "motif"));
        statement.writeField(CAOrdreRecouvrement.FIELD_NATUREORDRE,
                this._dbWriteNumeric(statement.getTransaction(), getNatureOrdre(), "natureOrdre"));
        statement.writeField(CAOrdreRecouvrement.FIELD_NOMCACHE,
                this._dbWriteString(statement.getTransaction(), getNomCache(), "nomCache"));
        statement.writeField(CAOrdreRecouvrement.FIELD_NUMTRANSACTION,
                this._dbWriteNumeric(statement.getTransaction(), getNumTransaction(), "numTransaction"));
        statement.writeField(CAOrdreRecouvrement.FIELD_REFERENCEBVR,
                this._dbWriteString(statement.getTransaction(), getReferenceBVR(), "referenceBVR"));
        statement.writeField(CAOrdreRecouvrement.FIELD_TYPEVIREMENT,
                this._dbWriteNumeric(statement.getTransaction(), getTypeVirement(), "typeVirement"));
        statement.writeField(CAOrdreRecouvrement.FIELD_VALEURCONVERSION,
                this._dbWriteDateAMJ(statement.getTransaction(), getValeurConversion(), "valeurConversion"));
        statement.writeField(CAOrdreRecouvrement.FIELD_TYPEORDRE,
                this._dbWriteString(statement.getTransaction(), getTypeOrdre(), "typeOrdre"));
        statement.writeField(CAOrdreRecouvrement.FIELD_CODEISOPAYS,
                this._dbWriteString(statement.getTransaction(), getCodeISOPays(), "codeISOPays"));
        statement.writeField(CAOrdreRecouvrement.FIELD_IDBANQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdBanque(), "idBanque"));
        statement.writeField(CAOrdreRecouvrement.FIELD_NOCOMPTE,
                this._dbWriteString(statement.getTransaction(), getNoCompte(), "noCompte"));
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.02.2002 11:01:56)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntAdressePaiement
     */
    public IntAdressePaiement getAdressePaiement() {

        // Si l'ordre group� n'existe pas, retourner null
        if (JadeStringUtil.isIntegerEmpty(getIdAdressePaiement())) {
            return null;
        }

        // Si log pas d�j� charg�
        if (_adressePaiement == null) {
            // Instancier une nouvelle adresse de paiement
            try {
                CAApplication currentApplication = CAApplication.getApplicationOsiris();
                _adressePaiement = (IntAdressePaiement) GlobazServer.getCurrentSystem()
                        .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                        .getImplementationFor(getSession(), IntAdressePaiement.class);

                // R�cup�rer l'adresse
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

        // Si l'ordre group� n'existe pas, retourner null
        if (JadeStringUtil.isIntegerEmpty(getIdAdressePaiement())) {
            return null;
        }

        // Si log pas d�j� charg�
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
    public java.lang.String getCodeISOMonnaieBonification() {
        return codeISOMonnaieBonification;
    }

    public java.lang.String getCodeISOMonnaieDepot() {
        return codeISOMonnaieDepot;
    }

    /**
     * @return
     */
    public String getCodeISOPays() {
        return codeISOPays;
    }

    public java.lang.String getCoursConversion() {
        return coursConversion;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.01.2002 17:08:25)
     * 
     * @return globaz.globall.parameters.FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsMonnaies() {
        // liste d�j� charg�e ?
        if (csMonnaies == null) {
            // liste pas encore charg�e, on la charge
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.01.2002 17:08:25)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.01.2002 17:08:25)
     * 
     * @return globaz.globall.parameters.FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsTypeVirements() {
        // liste d�j� charg�e ?
        if (csTypeVirements == null) {
            // liste pas encore charg�e, on la charge
            csTypeVirements = new FWParametersSystemCodeManager();
            csTypeVirements.setSession(getSession());
            csTypeVirements.getListeCodesSup("OSITYPOVE", getSession().getIdLangue());
        }
        return csTypeVirements;
    }

    public java.lang.Boolean getEstBloque() {
        return estBloque;
    }

    public java.lang.Boolean getEstRetire() {
        return estRetire;
    }

    public java.lang.String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    /**
     * @return
     */
    public String getIdBanque() {
        return idBanque;
    }

    public java.lang.String getIdOrdre() {
        return idOrdre;
    }

    public java.lang.String getIdOrdreGroupe() {
        return idOrdreGroupe;
    }

    public java.lang.String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.02.2002 08:29:08)
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

    public java.lang.String getMotif() {
        return motif;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (31.12.2002 11:18:27)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMotifRefus() {
        return motifRefus;
    }

    public java.lang.String getNatureOrdre() {
        return natureOrdre;
    }

    /**
     * @return
     */
    public String getNoCompte() {
        return noCompte;
    }

    public java.lang.String getNomCache() {
        return nomCache;
    }

    public java.lang.String getNumTransaction() {
        return numTransaction;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 13:04:26)
     * 
     * @return globaz.osiris.db.ordres.CAOrdreGroupe
     */
    public CAOrdreGroupe getOrdreGroupe() {

        // Si l'ordre group� n'existe pas, retourner null
        if (JadeStringUtil.isIntegerEmpty(getIdOrdreGroupe())) {
            return null;
        }

        // Si log pas d�j� charg�
        if (_ordreGroupe == null) {
            // Instancier un nouvel ordre group�
            _ordreGroupe = new CAOrdreGroupe();
            _ordreGroupe.setSession(getSession());

            // R�cup�rer le log en question
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

    public java.lang.String getReferenceBVR() {
        return referenceBVR;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.02.2002 10:25:40)
     * 
     * @return java.lang.String
     */
    public String getTypeOrdre() {
        return typeOrdre;
    }

    public java.lang.String getTypeVirement() {
        return typeVirement;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (17.01.2002 16:36:04)
     * 
     * @return globaz.globall.parameters.FWParametersUserCode
     */
    public FWParametersUserCode getUcNatureOrdre() {

        if (ucNatureOrdre == null) {
            // liste pas encore chargee, on la charge
            ucNatureOrdre = new FWParametersUserCode();
            ucNatureOrdre.setSession(getSession());

            // R�cup�rer le code syst�me dans la langue de l'utilisateur
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (17.01.2002 16:36:04)
     * 
     * @return globaz.globall.parameters.FWParametersUserCode
     */
    public FWParametersUserCode getUcTypeVirement() {

        if (ucTypeVirement == null) {
            // liste pas encore chargee, on la charge
            ucTypeVirement = new FWParametersUserCode();
            ucTypeVirement.setSession(getSession());

            // R�cup�rer le code syst�me dans la langue de l'utilisateur
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

    public java.lang.String getValeurConversion() {
        return valeurConversion;
    }

    /**
     * Setter
     */
    public void setCodeISOMonnaieBonification(java.lang.String newCodeISOMonnaieBonification) {
        codeISOMonnaieBonification = newCodeISOMonnaieBonification;
    }

    public void setCodeISOMonnaieDepot(java.lang.String newCodeISOMonnaieDepot) {
        codeISOMonnaieDepot = newCodeISOMonnaieDepot;
    }

    /**
     * @param string
     */
    public void setCodeISOPays(String string) {
        codeISOPays = string;
    }

    public void setCoursConversion(java.lang.String newCoursConversion) {
        coursConversion = newCoursConversion;
    }

    public void setEstBloque(java.lang.Boolean newEstBloque) {
        estBloque = newEstBloque;
    }

    public void setEstRetire(java.lang.Boolean newEstRetire) {
        estRetire = newEstRetire;
    }

    public void setIdAdressePaiement(java.lang.String newIdAdressePaiement) {
        idAdressePaiement = newIdAdressePaiement;
        _adressePaiement = null;
    }

    /**
     * @param string
     */
    public void setIdBanque(String string) {
        idBanque = string;
    }

    public void setIdOrdre(java.lang.String newIdOrdre) {
        idOrdre = newIdOrdre;
    }

    public void setIdOrdreGroupe(java.lang.String newIdOrdreGroupe) {
        idOrdreGroupe = newIdOrdreGroupe;
        _ordreGroupe = null;
    }

    public void setIdOrganeExecution(java.lang.String newIdOrganeExecution) {
        idOrganeExecution = newIdOrganeExecution;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.02.2002 08:30:10)
     * 
     * @param newMemoryLog
     *            globaz.osiris.db.utils.FWMemoryLog
     */
    public void setMemoryLog(FWMemoryLog newMemoryLog) {
        memoryLog = newMemoryLog;
    }

    public void setMotif(java.lang.String newMotif) {
        motif = newMotif;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (31.12.2002 11:18:27)
     * 
     * @param newMotifRefus
     *            java.lang.String
     */
    public void setMotifRefus(java.lang.String newMotifRefus) {
        motifRefus = newMotifRefus;
    }

    public void setNatureOrdre(java.lang.String newNatureOrdre) {
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

    public void setNomCache(java.lang.String newNomCache) {
        if (newNomCache.length() >= 40) {
            nomCache = newNomCache.substring(0, 40);
        } else {
            nomCache = newNomCache;
        }
    }

    public void setNumTransaction(java.lang.String newNumTransaction) {
        numTransaction = newNumTransaction;
    }

    public void setReferenceBVR(java.lang.String newReferenceBVR) {
        referenceBVR = newReferenceBVR;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.02.2002 10:25:59)
     * 
     * @param newTypeOrdre
     *            java.lang.String
     */
    public void setTypeOrdre(String newTypeOrdre) {
        typeOrdre = newTypeOrdre;
    }

    public void setTypeVirement(java.lang.String newTypeVirement) {
        typeVirement = newTypeVirement;
        csTypeVirement = null;
        ucTypeVirement = null;
    }

    public void setValeurConversion(java.lang.String newValeurConversion) {
        valeurConversion = newValeurConversion;
    }

    /**
     * Synchroniser le nom cache Date de cr�ation : (22.02.2002 13:39:08)
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
