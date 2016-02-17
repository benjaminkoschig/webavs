package globaz.osiris.db.comptes;

import globaz.apg.application.APApplication;
import globaz.aquila.api.helper.ICOEtapeHelper;
import globaz.corvus.application.REApplication;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.ij.application.IJApplication;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIEtape;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APISection;
import globaz.osiris.api.APISynchronisable;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.extrait.CAExtraitCompteManager;
import globaz.osiris.db.contentieux.CACompteAnnexeAvecSectionsPoursuiteManager;
import globaz.osiris.db.contentieux.CAMotifContentieux;
import globaz.osiris.db.contentieux.CAMotifContentieuxManager;
import globaz.osiris.db.contentieux.CAMotifContentieuxUtil;
import globaz.osiris.db.sections.CASectionsACompenserManager;
import globaz.osiris.exceptions.CATechnicalException;
import globaz.osiris.external.IntRole;
import globaz.osiris.external.IntTiers;
import globaz.osiris.translation.CACodeSystem;
import globaz.pyxis.constantes.IConstantes;
import globaz.webavs.common.ICommonConstantes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import ch.globaz.al.business.constantes.ALCSTiers;

/**
 * Insérez la description du type ici. Date de création : (10.12.2001 12:56:55)
 * 
 * @author: Administrator
 */

public class CACompteAnnexe extends BEntity implements Serializable, APISynchronisable, APICompteAnnexe {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String CATEGORIE_COMPTE_STANDARD = "0";

    public static final String CS_AF = "227015";
    public static final String CS_APG = "227016";
    public static final String CS_IJAI = "227021";

    public static final String CS_PREST_CONVENTIONNELLE = "227029";
    public static final String CS_RETENU_SURRENTE = "227028";

    // Mode de traitement des bulletins neutres - D0009
    public final static String CS_BN_DEFAUT = "259001";
    public final static String CS_BN_VENTILATION = "259002";
    public final static String CS_BN_CREDIT = "259003";
    public final static String CS_BN_INACTIF = "259004";

    public static final String FIELD_ASURVEILLER = "ASURVEILLER";
    public static final String FIELD_BLOQUERAMENDESTATUTAIRE = "BLOQAMENDESTAT";
    public static final String FIELD_BLOQUERFRAISPOURSUITE = "BLOQUERFRAISPOURS";
    public static final String FIELD_BLOQUERTAXESOMMATION = "BLOQUERTAXESOM";
    public static final String FIELD_CONTDATEDEBBLOQUE = "CONTDATEDEBBLOQUE";
    public static final String FIELD_CONTDATEFINBLOQUE = "CONTDATEFINBLOQUE";
    public static final String FIELD_CONTESTBLOQUE = "CONTESTBLOQUE";
    public static final String FIELD_DESCRIPTION = "DESCRIPTION";
    public static final String FIELD_DESCUPCASE = "DESCUPCASE";
    public static final String FIELD_ESTCONFIDENTIEL = "ESTCONFIDENTIEL";
    public static final String FIELD_ESTVERROUILLE = "ESTVERROUILLE";
    public static final String FIELD_IDCATEGORIE = "IDCATEGORIE";
    public static final String FIELD_IDCOMPTEANNEXE = "IDCOMPTEANNEXE";
    public static final String FIELD_IDCONTMOTIFBLOQUE = "IDCONTMOTIFBLOQUE";
    public static final String FIELD_IDEXTERNEROLE = "IDEXTERNEROLE";
    public static final String FIELD_IDGENRECOMPTE = "IDGENRECOMPTE";
    public static final String FIELD_IDJOURNAL = "IDJOURNAL";
    public static final String FIELD_IDROLE = "IDROLE";
    public static final String FIELD_IDTIERS = "IDTIERS";

    public static final String FIELD_IDTRI = "AAITRN";

    public static final String FIELD_INFORMATION = "INFORMATION";
    public static final String FIELD_QUALITEDEBITEUR = "QUALITEDEBITEUR";

    public static final String FIELD_REMARQUE = "REMARQUE";
    public static final String FIELD_SOLDE = "SOLDE";
    public static final String FIELD_MODEBULLETINNEUTRE = "MODEBN";

    public static final String GENRE_COMPTE_STANDARD = "0";
    public static final String LABEL_OPERATION_AUXILIAIRE_NON_INSEREE = "OPERATION_AUXILIAIRE_NON_INSEREE";
    private static final int NUM_CONSULAT_LENGTH = 3;

    public static final String TABLE_CACPTAP = "CACPTAP";
    private Boolean aSurveiller = new Boolean(false);

    private Boolean bloquerAmendeStatutaire = new Boolean(false);

    private Boolean bloquerFraisPoursuite = new Boolean(false);
    private Boolean bloquerTaxeSommation = new Boolean(false);
    private CARole caRole;
    private String contDateDebBloque = new String();
    private String contDateFinBloque = new String();
    private Boolean contEstBloque = new Boolean(false);
    private FWParametersSystemCode csMotifContentieuxSuspendu = null;
    private FWParametersSystemCodeManager csMotifContentieuxSuspendus = null;
    private String description = new String();
    private String descriptionUpperCase = new String();
    private Boolean estConfidentiel = new Boolean(false);
    private Boolean estVerrouille = new Boolean(false);
    private String idCategorie = new String();
    private String idCompteAnnexe = new String();
    private String idContMotifBloque = new String();
    private String idExterneRole = new String();
    private String idGenreCompte = CACompteAnnexe.GENRE_COMPTE_STANDARD;
    private String idJournal = new String();
    private String idRole = new String();
    private String idTiers = new String();
    private String idTri = new String();
    private String information = new String();
    protected IntRole intRole;
    private IntTiers intTiers = null;
    private CAJournal journal = null;
    private String qualiteDebiteur = new String();
    private String remarque = new String();
    private String solde = "0.00";
    // D0009
    private String modeBulletinNeutre = new String();

    private FWParametersUserCode ucMotifContentieuxSuspendu = null;

    /**
     * Commentaire relatif au constructeur CACompteAnnexe
     */
    public CACompteAnnexe() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2002 11:26:09)
     */
    @Override
    protected void _afterRetrieveWithResultSet(globaz.globall.db.BStatement statement) {
        // Synchroniser automatiquement le nom du compte annexe
        // synchroniser();
    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        // incrémente le prochain numéro
        setIdCompteAnnexe(this._incCounter(transaction, idCompteAnnexe));
        // Synchroniser automatiquement le compte annexe
        synchroniser();
        // Formatter l'id externe automatiquement
        if (!JadeStringUtil.isBlank(getIdExterneRole())) {
            CAApplication currentApplication = CAApplication.getApplicationOsiris();
            IntRole role = (IntRole) GlobazServer.getCurrentSystem()
                    .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                    .getImplementationFor(transaction.getSession(), IntRole.class);
            setIdExterneRole(role.formatIdExterneRole(getIdRole(), getIdExterneRole()));
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.01.2002 15:18:10)
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws Exception {
        if (hasOperations() || hasSections()) {
            _addError(transaction, getSession().getLabel("7064"));
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 17:29:32)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Formatter l'id externe automatiquement
        if (!JadeStringUtil.isBlank(getIdExterneRole())) {
            CAApplication currentApplication = CAApplication.getApplicationOsiris();
            IntRole role = (IntRole) GlobazServer.getCurrentSystem()
                    .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                    .getImplementationFor(transaction.getSession(), IntRole.class);
            setIdExterneRole(role.formatIdExterneRole(getIdRole(), getIdExterneRole()));
        }
    }

    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws Exception {
        // Synchroniser automatiquement le compte annexe
        if (!JACalendar.today().equals(new JADate(getSpy().getDate()))) {
            synchroniser();
        }
    }

    @Override
    public String _getDefaultDomainFromRole() {
        String domaine;
        if (IntRole.ROLE_AFFILIE.equals(idRole) || IntRole.ROLE_AFFILIE_PARITAIRE.equals(idRole)
                || IntRole.ROLE_AFFILIE_PERSONNEL.equals(idRole)) {
            domaine = ICommonConstantes.CS_APPLICATION_COTISATION;
        } else if (IntRole.ROLE_APG.equals(idRole)) {
            domaine = APApplication.CS_DOMAINE_ADRESSE_APG;
        } else if (IntRole.ROLE_AF.equals(idRole)) {
            domaine = ALCSTiers.DOMAINE_AF;
        } else if (IntRole.ROLE_IJAI.equals(idRole)) {
            domaine = IJApplication.CS_DOMAINE_ADRESSE_IJAI;
        } else if (IntRole.ROLE_RENTIER.equals(idRole) || IntRole.ROLE_PCF.equals(idRole)) {
            domaine = REApplication.CS_DOMAINE_ADRESSE_CORVUS;
            // } else if (IntRole.ROLE_BENEFICIAIRE_PRESTATIONS_CONVENTIONNELLES.equals(idRole)) {
            // domaine = REApplication.CS_DOMAINE_ADRESSE_CORV;
        } else {
            domaine = IConstantes.CS_APPLICATION_DEFAUT;
        }
        return domaine;
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return CACompteAnnexe.TABLE_CACPTAP;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idCompteAnnexe = statement.dbReadNumeric(CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
        description = statement.dbReadString(CACompteAnnexe.FIELD_DESCRIPTION);
        idTiers = statement.dbReadNumeric(CACompteAnnexe.FIELD_IDTIERS);
        idRole = statement.dbReadNumeric(CACompteAnnexe.FIELD_IDROLE);
        idExterneRole = statement.dbReadString(CACompteAnnexe.FIELD_IDEXTERNEROLE);
        solde = statement.dbReadNumeric(CACompteAnnexe.FIELD_SOLDE, 2);
        estVerrouille = statement.dbReadBoolean(CACompteAnnexe.FIELD_ESTVERROUILLE);
        estConfidentiel = statement.dbReadBoolean(CACompteAnnexe.FIELD_ESTCONFIDENTIEL);
        idJournal = statement.dbReadNumeric(CACompteAnnexe.FIELD_IDJOURNAL);
        contEstBloque = statement.dbReadBoolean(CACompteAnnexe.FIELD_CONTESTBLOQUE);
        idContMotifBloque = statement.dbReadNumeric(CACompteAnnexe.FIELD_IDCONTMOTIFBLOQUE);
        contDateDebBloque = statement.dbReadDateAMJ(CACompteAnnexe.FIELD_CONTDATEDEBBLOQUE);
        contDateFinBloque = statement.dbReadDateAMJ(CACompteAnnexe.FIELD_CONTDATEFINBLOQUE);
        idGenreCompte = statement.dbReadNumeric(CACompteAnnexe.FIELD_IDGENRECOMPTE);
        idCategorie = statement.dbReadNumeric(CACompteAnnexe.FIELD_IDCATEGORIE);
        idTri = statement.dbReadNumeric(CACompteAnnexe.FIELD_IDTRI);
        bloquerTaxeSommation = statement.dbReadBoolean(CACompteAnnexe.FIELD_BLOQUERTAXESOMMATION);
        bloquerAmendeStatutaire = statement.dbReadBoolean(CACompteAnnexe.FIELD_BLOQUERAMENDESTATUTAIRE);
        bloquerFraisPoursuite = statement.dbReadBoolean(CACompteAnnexe.FIELD_BLOQUERFRAISPOURSUITE);
        qualiteDebiteur = statement.dbReadNumeric(CACompteAnnexe.FIELD_QUALITEDEBITEUR);
        remarque = statement.dbReadString(CACompteAnnexe.FIELD_REMARQUE);
        aSurveiller = statement.dbReadBoolean(CACompteAnnexe.FIELD_ASURVEILLER);
        information = statement.dbReadNumeric(CACompteAnnexe.FIELD_INFORMATION);
        descriptionUpperCase = statement.dbReadString(CACompteAnnexe.FIELD_DESCUPCASE);
        // D0009
        modeBulletinNeutre = statement.dbReadNumeric(CACompteAnnexe.FIELD_MODEBULLETINNEUTRE);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdCompteAnnexe(), getSession().getLabel("7060"));
        _propertyMandatory(statement.getTransaction(), getIdRole(), getSession().getLabel("7061"));
        _propertyMandatory(statement.getTransaction(), getIdExterneRole(), getSession().getLabel("7062"));
        _propertyMandatory(statement.getTransaction(), getIdTiers(), getSession().getLabel("7063"));
        // Contrôler l'id externe
        try {
            CAApplication currentApplication = CAApplication.getApplicationOsiris();
            IntRole role = (IntRole) GlobazServer.getCurrentSystem()
                    .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                    .getImplementationFor(statement.getTransaction().getSession(), IntRole.class);
            role.checkIdExterneRoleFormat(getIdRole(), getIdExterneRole());
        } catch (Exception e) {
            _addError(statement.getTransaction(), e.getMessage());
        }
        // Contrôles sur le contentieux au niveau de compte annexe
        if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            if (!getContEstBloque().booleanValue()) {
                // La date de début doit ètre vide
                if (!JAUtil.isDateEmpty(getContDateDebBloque())) {
                    _addError(statement.getTransaction(), getSession().getLabel("7371"));
                }
                // La date de fin doit être vide
                if (!JAUtil.isDateEmpty(getContDateFinBloque())) {
                    _addError(statement.getTransaction(), getSession().getLabel("7372"));
                }
                // Le motif de blocage doit être vide
                if (!JadeStringUtil.isIntegerEmpty(getIdContMotifBloque())) {
                    _addError(statement.getTransaction(), getSession().getLabel("7092"));
                }
            } else {
                // La date de début doit être saisie
                if (JAUtil.isDateEmpty(getContDateDebBloque())) {
                    _addError(statement.getTransaction(), getSession().getLabel("7352"));
                }
                _checkDate(statement.getTransaction(), getContDateDebBloque(), getSession().getLabel("7352"));
                // La date de fin doit être saisie
                if (JAUtil.isDateEmpty(getContDateFinBloque())) {
                    _addError(statement.getTransaction(), getSession().getLabel("7353"));
                }
                _checkDate(statement.getTransaction(), getContDateFinBloque(), getSession().getLabel("7353"));
                // Contrôler que la date de début soit plus petite que la date
                // de fin
                int res;
                JACalendarGregorian calDate = new JACalendarGregorian();
                try {
                    res = calDate.compare(getContDateDebBloque(), getContDateFinBloque());
                    if (res > 1) {
                        _addError(statement.getTransaction(), getSession().getLabel("7370"));
                    }
                } catch (Exception e) {
                }
                // Le motif de blocage doit être saisie
                if (JadeStringUtil.isIntegerEmpty(getIdContMotifBloque())) {
                    _addError(statement.getTransaction(), getSession().getLabel("7090"));
                }
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.04.2002 10:33:12)
     * 
     * @param alternateKey
     *            int
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _writeAlternateKey(globaz.globall.db.BStatement statement, int alternateKey)
            throws java.lang.Exception {
        // Clé alternée numéro 1 : idRole et idExterneRole
        switch (alternateKey) {
            case AK_IDEXTERNE:
                statement.writeKey(CACompteAnnexe.FIELD_IDROLE,
                        this._dbWriteNumeric(statement.getTransaction(), getIdRole(), ""));
                statement.writeKey(CACompteAnnexe.FIELD_IDEXTERNEROLE,
                        this._dbWriteString(statement.getTransaction(), getIdExterneRole(), ""));
                break;
            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(CACompteAnnexe.FIELD_IDCOMPTEANNEXE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCompteAnnexe(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(CACompteAnnexe.FIELD_IDCOMPTEANNEXE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCompteAnnexe(), "idCompteAnnexe"));
        statement.writeField(CACompteAnnexe.FIELD_DESCRIPTION,
                this._dbWriteString(statement.getTransaction(), getDescription(), "description"));
        statement.writeField(CACompteAnnexe.FIELD_IDTIERS,
                this._dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
        statement.writeField(CACompteAnnexe.FIELD_IDROLE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRole(), "idRole"));
        statement.writeField(CACompteAnnexe.FIELD_IDEXTERNEROLE,
                this._dbWriteString(statement.getTransaction(), getIdExterneRole(), "idExterneRole"));
        statement.writeField(CACompteAnnexe.FIELD_SOLDE,
                this._dbWriteNumeric(statement.getTransaction(), getSolde(), "solde"));
        statement.writeField(CACompteAnnexe.FIELD_ESTVERROUILLE, this._dbWriteBoolean(statement.getTransaction(),
                getEstVerrouille(), BConstants.DB_TYPE_BOOLEAN_CHAR, "estVerrouille"));
        statement.writeField(CACompteAnnexe.FIELD_ESTCONFIDENTIEL, this._dbWriteBoolean(statement.getTransaction(),
                getEstConfidentiel(), BConstants.DB_TYPE_BOOLEAN_CHAR, "estConfidentiel"));
        statement.writeField(CACompteAnnexe.FIELD_IDJOURNAL,
                this._dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        statement.writeField(CACompteAnnexe.FIELD_CONTESTBLOQUE, this._dbWriteBoolean(statement.getTransaction(),
                getContEstBloque(), BConstants.DB_TYPE_BOOLEAN_CHAR, "contEstBloque"));
        statement.writeField(CACompteAnnexe.FIELD_IDCONTMOTIFBLOQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdContMotifBloque(), "idContMotifBloque"));
        statement.writeField(CACompteAnnexe.FIELD_CONTDATEDEBBLOQUE,
                this._dbWriteDateAMJ(statement.getTransaction(), getContDateDebBloque(), "contDateDebBloque"));
        statement.writeField(CACompteAnnexe.FIELD_CONTDATEFINBLOQUE,
                this._dbWriteDateAMJ(statement.getTransaction(), getContDateFinBloque(), "contDateFinBloque"));
        statement.writeField(CACompteAnnexe.FIELD_IDGENRECOMPTE,
                this._dbWriteNumeric(statement.getTransaction(), getIdGenreCompte(), "idGenreCompte"));
        statement.writeField(CACompteAnnexe.FIELD_IDCATEGORIE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCategorie(), "idCategorie"));
        statement.writeField(CACompteAnnexe.FIELD_IDTRI,
                this._dbWriteNumeric(statement.getTransaction(), getIdTri(), "idTri"));
        statement.writeField(CACompteAnnexe.FIELD_BLOQUERTAXESOMMATION, this._dbWriteBoolean(
                statement.getTransaction(), getBloquerTaxeSommation(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                "bloquerTaxeSom"));
        statement.writeField(CACompteAnnexe.FIELD_BLOQUERAMENDESTATUTAIRE, this._dbWriteBoolean(
                statement.getTransaction(), getBloquerAmendeStatutaire(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                "bloqAmendeStat"));
        statement.writeField(CACompteAnnexe.FIELD_BLOQUERFRAISPOURSUITE, this._dbWriteBoolean(
                statement.getTransaction(), getBloquerFraisPoursuite(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                "bloquerFraisPours"));
        statement.writeField(CACompteAnnexe.FIELD_QUALITEDEBITEUR,
                this._dbWriteNumeric(statement.getTransaction(), getQualiteDebiteur(), "qualiteDebiteur"));
        statement.writeField(CACompteAnnexe.FIELD_REMARQUE,
                this._dbWriteString(statement.getTransaction(), getRemarque(), "remarque"));
        statement.writeField(CACompteAnnexe.FIELD_ASURVEILLER, this._dbWriteBoolean(statement.getTransaction(),
                isASurveiller(), BConstants.DB_TYPE_BOOLEAN_CHAR, "aSurveiller"));
        statement.writeField(CACompteAnnexe.FIELD_DESCUPCASE,
                this._dbWriteString(statement.getTransaction(), getDescriptionUpperCase(), "descriptionUpCase"));
        // D0009
        statement.writeField(CACompteAnnexe.FIELD_MODEBULLETINNEUTRE,
                this._dbWriteNumeric(statement.getTransaction(), getModeBulletinNeutre(), "modeBulletinNeutre"));
    }

    /**
     * Insérer un opération dans le compte S'il s'agit d'une écriture ou d'une opération auxiliaire, on met à jour le
     * solde du compte annexe
     * 
     * @return FWMessage null s'il n'y a pas d'erreur, sinon un message d'erreur
     * @param oper
     *            globaz.osiris.db.comptes.CAOperation L'opération à insérer
     */
    public FWMessage addOperation(CAOperation oper) {
        // Initialiser un nouveau message
        FWMessage msg = null;

        try {
            if ((oper.isInstanceOrSubClassOf(APIOperation.CAECRITURE))
                    || (oper.isInstanceOrSubClassOf(APIOperation.CAAUXILIAIRE))) {
                FWCurrency solde = new FWCurrency(getSolde());
                solde.add(oper.getMontant());
                setSolde(solde.toString());
            }
        } catch (Exception e) {
            msg = new FWMessage();

            if (oper.isInstanceOrSubClassOf(APIOperation.CAAUXILIAIRE)) {
                msg.setMessageId(CACompteAnnexe.LABEL_OPERATION_AUXILIAIRE_NON_INSEREE);
            } else {
                msg.setMessageId("5131");
            }

            msg.setComplement(e.getMessage());
            msg.setIdSource("CACompteAnnexe");
            msg.setTypeMessage(FWMessage.ERREUR);
        }

        return msg;
    }

    /**
     * Cette méthode permet de créer un compte annexe sans passer par la création d'écritures
     * 
     * @param session
     * @param transaction
     * @param idTiers
     * @param idRole
     * @param idExterneRole
     * @return APICompteAnnexe
     * @throws Exception
     *             retourne une exception en cas d'erreur
     */
    @Override
    public APICompteAnnexe createCompteAnnexe(BISession session, BITransaction transaction, String idTiers,
            String idRole, String idExterneRole) throws Exception {
        // Vérifier les paramètres
        if ((idTiers == null) || JadeStringUtil.isIntegerEmpty(idTiers)) {
            throw new Exception(((BSession) session).getLabel("5045"));
        }
        if ((idRole == null) || JadeStringUtil.isIntegerEmpty(idRole)) {
            throw new Exception(((BSession) session).getLabel("5046"));
        }
        if ((idExterneRole == null) || JadeStringUtil.isBlank(idExterneRole)) {
            throw new Exception(((BSession) session).getLabel("5047"));
        }

        // Instancier une nouvelle classe
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(((BSession) session));

        // Vérifier si le compte annexe existe déjà
        compteAnnexe.setIdRole(idRole);
        compteAnnexe.setIdExterneRole(idExterneRole);
        compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
        compteAnnexe.retrieve(transaction);
        if (!compteAnnexe.isNew()) {
            return compteAnnexe;
        }

        // Alimenter les zones par défaut
        compteAnnexe.setIdJournal("0");
        compteAnnexe.setIdTiers(idTiers);
        compteAnnexe.setModeBulletinNeutre(CACompteAnnexe.CS_BN_DEFAUT);
        compteAnnexe.add(transaction);
        if (compteAnnexe.hasErrors() || compteAnnexe.isNew()) {
            throw new Exception(((BSession) session).getLabel("5048"));
        }
        return compteAnnexe;

    }

    /**
     * Renvoie true si l'amende statutaire doit être bloquée pour ce compte annexe (pas de calcul de l'amende
     * statutaire)
     * 
     * @return Boolean bloquerAmendeStatutaire
     */
    public Boolean getBloquerAmendeStatutaire() {
        return bloquerAmendeStatutaire;
    }

    /**
     * Renvoie true si les frais de poursuite sont bloquer pour ce compte annexe (pas de frais de poursuite)
     * 
     * @return Boolean bloquerFraisPoursuite
     */
    public Boolean getBloquerFraisPoursuite() {
        return bloquerFraisPoursuite;
    }

    /**
     * Renvoie true si la taxe de sommation doit être bloquer pour ce compte annexe (pas de taxe de sommation)
     * 
     * @return Boolean bloquerTaxeSommation
     */
    public Boolean getBloquerTaxeSommation() {
        return bloquerTaxeSommation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.03.2002 13:29:20)
     * 
     * @return globaz.osiris.db.comptes.CARole
     */
    public CARole getCARole() {
        if (caRole == null) {
            caRole = new CARole();
            caRole.setISession(getSession());
            caRole.setIdRole(getIdRole());
            try {
                caRole.retrieve();
                if (caRole.isNew()) {
                    caRole = null;
                }

            } catch (Exception e) {
                caRole = null;
            }
        }

        return caRole;
    }

    /**
     * Returns the contDateDebBloque.
     * 
     * @return String
     */
    @Override
    public String getContDateDebBloque() {
        return contDateDebBloque;
    }

    /**
     * Returns the contDateFinBloque.
     * 
     * @return String
     */
    @Override
    public String getContDateFinBloque() {
        return contDateFinBloque;
    }

    /**
     * Returns the contEstBloque.
     * 
     * @return Boolean
     */
    @Override
    public Boolean getContEstBloque() {
        return contEstBloque;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2002 13:02:45)
     * 
     * @return String
     */
    public FWParametersSystemCode getCsMotifContentieuxSuspendu() {
        if (csMotifContentieuxSuspendu == null) {
            // liste pas encore chargee, on la charge
            csMotifContentieuxSuspendu = new FWParametersSystemCode();
            csMotifContentieuxSuspendu.getCode(getIdContMotifBloque());
        }
        return csMotifContentieuxSuspendu;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2002 13:05:28)
     * 
     * @return globaz.globall.parameters.FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsMotifContentieuxSuspendus() {
        // liste déjà chargée ?
        if (csMotifContentieuxSuspendus == null) {
            // liste pas encore chargée, on la charge
            csMotifContentieuxSuspendus = new FWParametersSystemCodeManager();
            csMotifContentieuxSuspendus.setSession(getSession());
            csMotifContentieuxSuspendus.getListeCodesSup("OSIMOTCTX", getSession().getIdLangue());
        }
        return csMotifContentieuxSuspendus;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public String getDescriptionUpperCase() {
        return descriptionUpperCase;
    }

    @Override
    public Boolean getEstConfidentiel() {
        return estConfidentiel;
    }

    @Override
    public Boolean getEstVerrouille() {
        return estVerrouille;
    }

    /**
     * @return
     */
    @Override
    public String getIdCategorie() {
        return idCategorie;
    }

    /**
     * Getter
     */
    @Override
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * Returns the idContMotifBloque.
     * 
     * @return String
     */
    @Override
    public String getIdContMotifBloque() {
        return idContMotifBloque;
    }

    @Override
    public String getIdExterneRole() {
        return idExterneRole;
    }

    /**
     * Genre du compte. Standard ou auxilliaire ou ?
     * 
     * @return
     */
    public String getIdGenreCompte() {
        if (JadeStringUtil.isBlank(idGenreCompte)) {
            return CACompteAnnexe.GENRE_COMPTE_STANDARD;
        } else {
            return idGenreCompte;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.01.2002 16:38:56)
     * 
     * @return String
     */
    @Override
    public String getIdJournal() {
        return idJournal;
    }

    @Override
    public String getIdRole() {
        return idRole;
    }

    @Override
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return
     */
    @Override
    public String getIdTri() {
        return idTri;
    }

    /**
     * Retourne l'idTri formaté. Ajout d'un slash 3 caractères avant idTri.lentgh.
     * 
     * @return
     */
    public String getIdTriFormate() {
        if (getIdTri().length() >= CACompteAnnexe.NUM_CONSULAT_LENGTH) {
            String consulat = getIdTri().substring(getIdTri().length() - CACompteAnnexe.NUM_CONSULAT_LENGTH,
                    getIdTri().length());
            String satelitte = getIdTri().substring(0, getIdTri().length() - CACompteAnnexe.NUM_CONSULAT_LENGTH);

            if (JadeStringUtil.isBlank(satelitte)) {
                return consulat;
            } else {
                return consulat + " / " + satelitte;
            }
        } else {
            return null;
        }
    }

    public String getInformation() {
        return information;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.01.2002 16:39:34)
     * 
     * @return globaz.osiris.db.comptes.CAJournal
     */
    public CAJournal getJournal() {
        // récupérer si null
        if (journal == null) {
            journal = new CAJournal();
            journal.setISession(getSession());
            journal.setIdJournal(getIdJournal());
            try {
                journal.retrieve();
                if (journal.isNew()) {
                    journal = null;
                }

            } catch (Exception e) {
                journal = null;
            }
        }

        return journal;
    }

    /**
     * Retourne la liste des catégories possibles séparées par des ","
     * 
     * @param session
     * @return
     * @throws Exception
     */
    public String getListeCategories(BSession session) throws Exception {
        /*
         * Code obselete
         * FWParametersSystemCodeManager manager = CACodeSystem.getCategories(session);
         * String res = "";
         * for (int i = 0; i < manager.size(); i++) {
         * FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);
         * res += "," + code.getIdCode();
         * }
         */
        return "";
    }

    /**
     * Retourne une liste de comptes annexes (utilisé dans l'écran "Recherche du compte annexe par section" pour
     * afficher la liste déroulante qui permet de switcher entre les différents comptes annexes"
     * 
     * @return
     */
    public Object[] getListeCompteAnnexe() {
        BTransaction trans = null;
        ArrayList<String[]> comptesAnnexes = new ArrayList<String[]>();
        CACompteAnnexeManager compteAnnexeMan = new CACompteAnnexeManager();
        try {
            trans = new BTransaction(getSession());
            trans.openTransaction();
            compteAnnexeMan.setSession(getSession());
            compteAnnexeMan.setForSelectionRole(CARole.listeIdsRolesPourUtilisateurCourant(getSession()));
            compteAnnexeMan.setLikeNumNom(getIdExterneRole());
            compteAnnexeMan.setForIdGenreCompte(getIdGenreCompte());
            compteAnnexeMan.setForIdCategorie(getListeCategories(getSession()));
            compteAnnexeMan.find(trans);
            for (int i = 0; i < compteAnnexeMan.size(); i++) {
                CACompteAnnexe compteAnnexe = (CACompteAnnexe) compteAnnexeMan.getEntity(i);
                comptesAnnexes.add(new String[] { compteAnnexe.getIdCompteAnnexe(), compteAnnexe.getIdExterneRole(),
                        compteAnnexe.getRole().getDescription() });
            }
            trans.commit();
        } catch (Exception inEx) {
            // laisser tel quel
        } finally {
            if (trans != null) {
                try {
                    trans.closeTransaction();
                } catch (Exception inEx) {
                    // laisser tel quel
                }
            }
        }
        return comptesAnnexes.toArray();
    }

    /**
     * Method getListeSections. Retourne la les des sections associée au compte annexe.
     * 
     * @param ordre
     * @return Collection
     * @throws Exception
     */
    public Collection<CASection> getListeSections(int ordre) throws Exception {
        // Initialiser un vecteur pour le retour des valeurs
        Collection<CASection> v = new ArrayList<CASection>();
        // récupérer les factures
        CASectionManager mgr = new CASectionManager();
        mgr.setSession(getSession());
        mgr.setForIdCompteAnnexe(getIdCompteAnnexe());
        mgr.setForSoldeNot("0");
        // Choix du tri
        switch (ordre) {
            case PC_ORDRE_PLUS_ANCIEN:
                mgr.setOrderBy(CASectionManager.ORDER_DATE);
                break;
            case PC_ORDRE_PLUS_RECENT:
                mgr.setOrderBy(CASectionManager.ORDER_DATE_DESCEND);
                break;
            case PC_ORDRE_MONTANT_PLUS_ELEVE:
                mgr.setOrderBy(CASectionManager.ORDER_SOLDE_DESCEND);
                break;
            case PC_ORDRE_MONTANT_PLUS_PETIT:
                mgr.setOrderBy(CASectionManager.ORDER_SOLDE);
                break;
            default:
                throw new Exception(getSession().getLabel("5051") + String.valueOf(ordre));
        }
        // Exécuter la requête
        mgr.find(globaz.globall.db.BManager.SIZE_NOLIMIT);
        // Parcourir le manager pour déterminer les factures à compenser
        for (int i = 0; i < mgr.size(); i++) {
            // Récupére la section
            CASection sec = (CASection) mgr.getEntity(i);
            v.add(sec);
        }
        return v;
    }

    /**
     * Renvoie le code système qui détermine la mode de traitement du bulletin neutre
     * 
     * @return String ModeBulletinNeutre
     */
    public String getModeBulletinNeutre() {
        return modeBulletinNeutre;
    }

    /**
     * Renvoie le code système qui détermine la qualité du débiteur
     * 
     * @return String QualiteDebiteur
     */
    public String getQualiteDebiteur() {
        return qualiteDebiteur;
    }

    /**
     * Renvoie la remarque concernant ce compte annexe
     * 
     * @return String remarque
     */
    public String getRemarque() {
        return remarque;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.12.2001 08:57:19)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntRole
     */
    @Override
    public IntRole getRole() {
        if (intRole == null) {
            try {
                CAApplication currentApplication = CAApplication.getApplicationOsiris();
                intRole = (IntRole) GlobazServer.getCurrentSystem()
                        .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                        .getImplementationFor(getSession(), IntRole.class);
                intRole.setISession(getSession());
                if (!JadeStringUtil.isIntegerEmpty(getIdTiers())) {
                    intRole.retrieve(getIdTiers(), getIdRole(), null);
                } else {
                    intRole.retrieve(getIdRole(), getIdExterneRole());
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                return null;
            }
        }
        return intRole;
    }

    @Override
    public String getSolde() {
        return JANumberFormatter.deQuote(solde);
    }

    /**
     * Renvoie la valeur de la propriété soldeAt, il s'agit du solde du compte annexe jusqu'à la date passée en
     * paramètre (comprise) Date de création : (17.09.2002 09:46:41)
     * 
     * @return String
     * @param date
     *            String
     */
    @Override
    public String getSoldeAt(String date) {
        globaz.framework.util.FWCurrency solde = new FWCurrency();
        try {
            solde.add(getSolde());
            CAOperationManager operMan = new CAOperationManager();
            operMan.setForIdCompteAnnexe(getIdCompteAnnexe());
            operMan.setSession(getSession());
            operMan.find();
            if (!operMan.isEmpty()) {
                CAOperation oper;
                for (int i = 0; i < operMan.size(); i++) {
                    oper = (CAOperation) operMan.getEntity(i);
                    if (BSessionUtil.compareDateFirstGreater(getSession(), oper.getDate(), date)
                            && oper.getEstActive().booleanValue()) {
                        solde.sub(oper.getMontant());
                    }
                }
            }
        } catch (Exception e) {
        }
        return solde.toString();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.12.2001 11:29:50)
     * 
     * @return String
     */
    @Override
    public String getSoldeFormate() {
        return globaz.globall.util.JANumberFormatter.formatNoRound(getSolde(), 2);
    }

    /**
     * Renvoie la valeur de la propriété soldeInitialAt, il s'agit du solde du compte annexe jusqu'à la date passée en
     * paramètre (non comprise) Date de création : (17.09.2002 09:46:41)
     * 
     * @return String
     * @param date
     *            String
     */
    @Override
    public String getSoldeInitialAt(String date, String forSelectionSections) {
        FWCurrency solde = new FWCurrency();
        try {
            solde.add(getSolde());

            CAOperationManager operMan = new CAOperationManager();
            operMan.setForIdCompteAnnexe(getIdCompteAnnexe());
            operMan.setSession(getSession());

            ArrayList<String> etatIn = new ArrayList<String>();
            etatIn.add(APIOperation.ETAT_COMPTABILISE);
            etatIn.add(APIOperation.ETAT_PROVISOIRE);
            operMan.setForEtatIn(etatIn);

            ArrayList<String> idTypeOperationLikeIn = new ArrayList<String>();
            idTypeOperationLikeIn.add(APIOperation.CAECRITURE);
            idTypeOperationLikeIn.add(APIOperation.CAAUXILIAIRE);
            operMan.setForIdTypeOperationLikeIn(idTypeOperationLikeIn);

            operMan.find(BManager.SIZE_NOLIMIT);

            if (!operMan.isEmpty()) {
                for (int i = 0; i < operMan.size(); i++) {
                    CAOperation oper = (CAOperation) operMan.getEntity(i);
                    if ((testForSelectionSections(oper, forSelectionSections))
                            && (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), oper.getDate(), date))) {
                        if ((!JadeStringUtil.isIntegerEmpty(oper.getIdSection()) && !oper.getIdSection().equals("0"))) {
                            solde.sub(oper.getMontant());
                        }
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return solde.toString();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.08.2002 14:16:25)
     * 
     * @return globaz.framework.util.FWCurrency
     */
    public FWCurrency getSoldeToCurrency() {
        return new FWCurrency(getSolde());
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.12.2001 08:37:36)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntTiers
     */
    @Override
    public IntTiers getTiers() {
        if (intTiers == null) {
            try {
                CAApplication currentApplication = (CAApplication) GlobazServer.getCurrentSystem().getApplication(
                        CAApplication.DEFAULT_APPLICATION_OSIRIS);
                String externalApplicationName = currentApplication.getCAParametres().getApplicationExterne();
                BIApplication externalApplication = GlobazServer.getCurrentSystem().getApplication(
                        externalApplicationName);
                intTiers = (IntTiers) externalApplication.getImplementationFor(getSession(), IntTiers.class);
                intTiers.retrieve(idTiers);
                if (intTiers.isNew()) {
                    intTiers = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                intTiers = null;
            }
        }
        return intTiers;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.12.2001 10:23:42)
     * 
     * @return String
     */
    @Override
    public String getTitulaireEntete() {
        String str = "";
        final String RET = "\r\n";
        str = getRole().getDescription(getSession().getIdLangueISO());
        str += " ";
        str += getIdExterneRole();
        str += RET;
        // MKA 21.04.2010 modif pour affichage : Si l'adresse de domicile n'est
        // pas renseignée
        // on récupère designation 1 et 2 depuis Tiers
        if (JadeStringUtil.isBlank(getTiers().getTitulaireNomLieu())) {
            str += getTiers().getDesignation1() + " " + getTiers().getDesignation2();
        } else {
            str += getTiers().getTitulaireNomLieu();
        }
        return str;
    }

    /**
     * Même méthode que getTitutaireEnTête sauf que n'affiche que le numéro le nom et la localité
     * 
     * @return
     */
    public String getTitulaireEnteteForCompteAnnexeParSection() {
        String str = "";
        final String RET = "\r\n";
        str = getRole().getDescription(getSession().getIdLangueISO());
        str += " ";
        str += getIdExterneRole();
        str += RET;
        // MKA 21.04.2010 modif pour affichage : Si l'adresse de domicile n'est
        // pas renseignée
        // on récupère designation 1 et 2 depuis Tiers
        if (JadeStringUtil.isBlank(getTiers().getTitulaireNomLocalite())) {
            str += getTiers().getDesignation1() + " " + getTiers().getDesignation2();
        } else {
            str += getTiers().getTitulaireNomLocalite();
        }
        return str;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2002 13:47:48)
     * 
     * @return globaz.globall.parameters.FWParametersUserCode
     */
    public FWParametersUserCode getUcMotifContentieuxSuspendu() {
        if (ucMotifContentieuxSuspendu == null) {
            // liste pas encore chargee, on la charge
            ucMotifContentieuxSuspendu = new FWParametersUserCode();
            ucMotifContentieuxSuspendu.setSession(getSession());
        }
        if (!JadeStringUtil.isIntegerEmpty(getIdContMotifBloque())) {
            // Récupérer le code système dans la langue de l'utilisateur
            ucMotifContentieuxSuspendu.setIdCodeSysteme(getIdContMotifBloque());
            ucMotifContentieuxSuspendu.setIdLangue(getSession().getIdLangue());
            try {
                ucMotifContentieuxSuspendu.retrieve();
                if (ucMotifContentieuxSuspendu.isNew()) {
                    _addError(null, getSession().getLabel("7324"));
                }
            } catch (Exception e) {
                _addError(null, getSession().getLabel("7324"));
            }
        }
        return ucMotifContentieuxSuspendu;
    }

    /**
     * Test la présence d'un motif de blocage contentieux actif
     * 
     * @param date
     *            de référence
     * @param codeSystemMotif
     *            : paramètre facultatif.
     * @return True si un motif non échu existe.
     */
    private boolean hasMotifActifAquila(String date, String codeSystem) {
        boolean motifExistant = false;
        CAMotifContentieuxManager motifMgr = new CAMotifContentieuxManager();
        motifMgr.setSession(getSession());
        motifMgr.setForIdCompteAnnexe(getIdCompteAnnexe());
        motifMgr.setFromDateBetweenDebutFin(date);
        if (!JadeStringUtil.isBlank(codeSystem)) {
            motifMgr.setForIdMotifBlocage(codeSystem);
        }
        try {
            motifMgr.find();
            if (!motifMgr.isEmpty()) {
                motifExistant = true;
            }
        } catch (Exception e) {
        }

        return motifExistant;
    }

    /**
     * @param date
     * @param codeSystem
     * @return
     */
    private boolean hasMotifActifOsiris(String date, String codeSystem) {
        if (JadeStringUtil.isIntegerEmpty(getIdContMotifBloque())) {
            return false;
        }
        if ((!JadeStringUtil.isBlank(codeSystem) && !codeSystem.equalsIgnoreCase(getIdContMotifBloque()))) {
            return false;
        }
        try {
            if (BSessionUtil.compareDateBetweenOrEqual(getSession(), getContDateDebBloque(), getContDateFinBloque(),
                    date)) {
                return true;
            }
        } catch (Exception e) {
        }

        return false;
    }

    /**
     * @throws CATechnicalException
     * @see APICompteAnnexe.hasMotifContentieuxForYear(String idModifBlocage, String year)
     */
    @Override
    public boolean hasMotifContentieuxForYear(String idModifBlocage, String year) throws CATechnicalException {
        return CAMotifContentieuxUtil.hasMotifContentieuxForYear(getSession(), getIdCompteAnnexe(), null,
                idModifBlocage, year);
    }

    /**
     * Retourne vrai si le compte annexe contient des opérations Date de création : (22.03.2002 09:49:14)
     * 
     * @return boolean vrai si le compte annexe contient des opérations
     */
    @Override
    public boolean hasOperations() {
        // Charger un manager
        CAOperationManager mgr = new CAOperationManager();
        mgr.setSession(getSession());
        mgr.setForIdCompteAnnexe(getIdCompteAnnexe());
        // Compter le nombre de correspondances
        try {
            if (mgr.getCount() != 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            _addError(null, e.getMessage());
            return false;
        }
    }

    /**
     * Retourne vrai si le compte annexe est lié à des sections Date de création : (22.03.2002 09:49:14)
     * 
     * @return boolean vrai si le compte annexe est lié à des sections
     */
    @Override
    public boolean hasSections() {
        // Charger un manager
        CASectionManager mgr = new CASectionManager();
        mgr.setSession(getSession());
        mgr.setForIdCompteAnnexe(getIdCompteAnnexe());
        // Compter le nombre de correspondances
        try {
            if (mgr.getCount() != 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            _addError(null, e.getMessage());
            return false;
        }
    }

    /**
     * @return
     */
    public Boolean isASurveiller() {
        return aSurveiller;
    }

    /**
     * Cette méthode retourne "true" si un compte annexe à des sections au contentieux
     * 
     * @param boolean soldeOuvert, true pour ne prendre que les sections qui ont un solde > 0. False, prend toutes les
     *        sections indépendamment du solde
     * @param String
     *            annee (pas utilisé pour le moment)
     * @return boolean true, si le compte annexe à des sections au contentieux
     */
    public boolean isCompteAnnexeAvecSectionsPoursuite(boolean soldeOuvert, String annee) {
        // TODO sch 14 nov. 07 La date n'est pas encore prise en charge, voir si
        // utilisée par la suite et quelle date doit être testée ?

        boolean isCompteannexeAvecSectionsContentieux = false;
        // ancien contentieux
        if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            CACompteAnnexeAvecSectionsPoursuiteManager manager = new CACompteAnnexeAvecSectionsPoursuiteManager();
            manager.setSession(getSession());
            manager.setForIdCompteAnnexe(getIdCompteAnnexe());
            manager.setTypeEtapeIn(APIEtape.ETAPE_POURSUITE_FORMAT);
            if (soldeOuvert) {
                manager.setForSelectionSections("3");
            }
            try {
                manager.changeManagerSize(BManager.SIZE_NOLIMIT);
                manager.find();
                if (!manager.isEmpty()) {
                    if (manager.size() > 0) {
                        isCompteannexeAvecSectionsContentieux = true;
                    }
                }
            } catch (Exception e) {
                return isCompteannexeAvecSectionsContentieux;
            }
        } else {
            // contentieux Aquila
            CASectionManager manager = new CASectionManager();
            manager.setSession(getSession());
            manager.setForIdCompteAnnexe(getIdCompteAnnexe());
            if (soldeOuvert) {
                manager.setForSelectionSections("3");
            }
            manager.setLastEtatAquilaNotIn(ICOEtapeHelper.ETAPE_POURSUITE_SQL_NOT_IN_FORMAT);
            try {
                manager.changeManagerSize(BManager.SIZE_NOLIMIT);
                manager.find();
                if (!manager.isEmpty()) {
                    if (manager.size() > 0) {
                        isCompteannexeAvecSectionsContentieux = true;
                    }
                }
            } catch (Exception e) {
                return isCompteannexeAvecSectionsContentieux;
            }
        }

        return isCompteannexeAvecSectionsContentieux;
    }

    /**
     * Le compte annexe est-il de type auxiliaire ?
     * 
     * @return
     */
    @Override
    public boolean isCompteAuxiliaire() {
        if (getIdGenreCompte().equals(CACodeSystem.COMPTE_AUXILIAIRE)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Cette méthode retourne true si le compte est bloqué (par n'importe quel motif de blocage) et si date du jour se
     * situe entre date debut et date fin de blocage
     * 
     * @param date
     * @return boolean true si motif existant et date du jour se situant entre date debut et date fin de blocage
     */
    public boolean isCompteBloque(String date) {
        if (getContEstBloque().booleanValue()) {
            if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
                return hasMotifActifOsiris(date, "");
            } else {
                return hasMotifActifAquila(date, "");
            }
        }
        return false;
    }

    /**
     * Renvoie true si le compte est bloque avec un motif à contentieux actif à la date donnée
     * 
     * @param date
     *            au format String (xx.xx.xxxx)
     */
    public boolean isCompteBloqueEtActif(String date) throws Exception {
        if (getContEstBloque().booleanValue() && !JadeStringUtil.isIntegerEmpty(getIdContMotifBloque())) // bloqué
        {
            int dateFacturation = Integer.parseInt(new JADate(date).toStrAMJ());
            int debutBloque = Integer.parseInt(new JADate(getContDateDebBloque()).toStrAMJ());
            int finBloque = Integer.parseInt(new JADate(getContDateFinBloque()).toStrAMJ());
            if (finBloque == 0) {
                finBloque = Integer.MAX_VALUE;
            }
            if ((debutBloque < dateFacturation) && (dateFacturation < finBloque)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Le compte annexe est-il de type standard ?
     * 
     * @return
     */
    @Override
    public boolean isCompteStantard() {
        if (getIdGenreCompte().equals(CACompteAnnexe.GENRE_COMPTE_STANDARD)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isEnFaillte(String annee) throws CATechnicalException {

        boolean isCompteAnnexeBloquerPourFaillite = CAMotifContentieuxUtil.hasMotifsContentieuxForYear(
                getSession(),
                idCompteAnnexe,
                null,
                new HashSet<String>(Arrays.asList(CAMotifContentieux.CS_MOTIF_BLOCAGE_FAILLITE,
                        CAMotifContentieux.CS_MOTIF_BLOCAGE_DEC_JUD_FAILLITE_AJOURNEMENT_FAILLITE,
                        CAMotifContentieux.CS_MOTIF_BLOCAGE_DEC_JUD_FAILLITE_BENEF_INVENTAIRE,
                        CAMotifContentieux.CS_MOTIF_BLOCAGE_DEC_JUD_FAILLITE_IND_SNC,
                        CAMotifContentieux.CS_MOTIF_BLOCAGE_DEC_JUD_FAILLITE_RI,
                        CAMotifContentieux.CS_MOTIF_BLOCAGE_DEC_JUD_FAILLITE_SOC,
                        CAMotifContentieux.CS_MOTIF_BLOCAGE_DEC_JUD_FAILLITE_SURSIS_CONCORDATAIRE)), annee);

        if (isCompteAnnexeBloquerPourFaillite) {
            return isCompteAnnexeBloquerPourFaillite;
        }

        try {
            CASectionManager sectionManager = new CASectionManager();
            sectionManager.setSession(getSession());
            sectionManager.setForIdCompteAnnexe(idCompteAnnexe);
            sectionManager.find(BManager.SIZE_NOLIMIT);

            if (sectionManager.size() == 0) {
                return false;
            }

            int i = 0;
            boolean isSectionBloquerPourFaillite = false;

            do {
                CASection uneSection = (CASection) sectionManager.get(i);

                isSectionBloquerPourFaillite = CAMotifContentieuxUtil.hasMotifsContentieuxForYear(
                        getSession(),
                        null,
                        uneSection.getIdSection(),
                        new HashSet<String>(Arrays.asList(CAMotifContentieux.CS_MOTIF_BLOCAGE_FAILLITE,
                                CAMotifContentieux.CS_MOTIF_BLOCAGE_DEC_JUD_FAILLITE_AJOURNEMENT_FAILLITE,
                                CAMotifContentieux.CS_MOTIF_BLOCAGE_DEC_JUD_FAILLITE_BENEF_INVENTAIRE,
                                CAMotifContentieux.CS_MOTIF_BLOCAGE_DEC_JUD_FAILLITE_IND_SNC,
                                CAMotifContentieux.CS_MOTIF_BLOCAGE_DEC_JUD_FAILLITE_RI,
                                CAMotifContentieux.CS_MOTIF_BLOCAGE_DEC_JUD_FAILLITE_SOC,
                                CAMotifContentieux.CS_MOTIF_BLOCAGE_DEC_JUD_FAILLITE_SURSIS_CONCORDATAIRE)), annee);
                i++;
            } while ((sectionManager.size() > i) && !isSectionBloquerPourFaillite);

            return isSectionBloquerPourFaillite;
        } catch (Exception ex) {
            throw new CATechnicalException("Error while loading sections", ex);
        }
    }

    public boolean isModeParDefautBulletinNeutreisInactif() {
        return getModeParDefautBulletinNeutre().equalsIgnoreCase(CS_BN_INACTIF);
    }

    public String getModeParDefautBulletinNeutre() {
        return CAApplication.getApplicationOsiris().getCAParametres().getModeParDefautBulletinNeutre();
    }

    /**
     * Cette méthode retourne un true si le code système (motif) passé en paramètre existe comme motif de blocage de
     * contentieux et que la date du jour se situe entre la date de début et de fin de blocage. par défaut cette méthode
     * retourne false
     * 
     * @param codeSystem
     *            du motif de blocage du contentieux 2040..
     * @return boolean true si motif existant et date du jour se situant entre date debut et date fin de blocage
     */
    public boolean isMotifExistant(String codeSystem) {
        if (getContEstBloque().booleanValue()) {
            if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
                return hasMotifActifOsiris(JACalendar.todayJJsMMsAAAA(), codeSystem);
            } else {
                return hasMotifActifAquila(JACalendar.todayJJsMMsAAAA(), codeSystem);
            }
        }
        return false;
    }

    public boolean isVerrouille() {
        return getEstVerrouille().booleanValue();
    }

    @Override
    public Collection<CASection> propositionCompensation(int type, int ordre, String montantACompenser)
            throws Exception {
        return this.propositionCompensation(type, ordre, montantACompenser, true);
    }

    /**
     * @see APICompteAnnexe#propositionCompensation(int, int, String)
     */
    public Collection<CASection> propositionCompensation(int type, int ordre, String montantACompenser,
            boolean checkMontant) throws Exception {
        FWCurrency cSoldeCa = getSoldeToCurrency();

        // Vérifier le solde du compte en cas de compensation interne
        if (type == APICompteAnnexe.PC_TYPE_INTERNE_ZERO) {
            if (!cSoldeCa.isZero()) {
                throw new Exception(getSession().getLabel("5052"));
            }
        }

        FWCurrency cComp = new FWCurrency();
        if ((type == APICompteAnnexe.PC_TYPE_MONTANT) || (type == APICompteAnnexe.PC_TYPE_FACTURATION)) {
            cComp = new FWCurrency(montantACompenser);
        }

        Collection<CASection> result = new ArrayList<CASection>();

        CASectionsACompenserManager mgr = new CASectionsACompenserManager();
        mgr.setSession(getSession());
        mgr.setForIdCompteAnnexe(getIdCompteAnnexe());
        mgr.setForSoldeNot("0");

        // ne pas proposer les sections en mode MODE_COMP_DEC_PERIODIQUE
        // car elles sont traitées à part par la méthode FAPassageCompenserNewProcess.compenserDecomptePeriodique
        // (InfoRom384)
        mgr.setModeCompensationNotIn(APISection.MODE_BLOQUER_COMPENSATION + ", " + APISection.MODE_COMP_DEC_PERIODIQUE);

        switch (ordre) {
            case PC_ORDRE_PLUS_ANCIEN:
                mgr.setOrderBy(CASectionManager.ORDER_DATE);
                break;
            case PC_ORDRE_PLUS_RECENT:
                mgr.setOrderBy(CASectionManager.ORDER_DATE_DESCEND);
                break;
            case PC_ORDRE_MONTANT_PLUS_ELEVE:
                mgr.setOrderBy(CASectionManager.ORDER_SOLDE_DESCEND);
                break;
            case PC_ORDRE_MONTANT_PLUS_PETIT:
                mgr.setOrderBy(CASectionManager.ORDER_SOLDE);
                break;
            default:
                throw new Exception(getSession().getLabel("5051") + String.valueOf(ordre));
        }

        mgr.find(BManager.SIZE_NOLIMIT);

        // TODO dda : 18 sept. 2008 simplifié processus !!! 1 seule boucle pas
        // de break et continue.

        if ((type == APICompteAnnexe.PC_TYPE_INTERNE_DEBITEUR) || (type == APICompteAnnexe.PC_TYPE_INTERNE_CREANCIER)) {
            for (int i = 0; i < mgr.size(); i++) {
                // Sortir si le montant à compenser est supérieur ou égal au
                // solde (val.absolue)
                double dc = Math.abs(cComp.doubleValue());
                double ds = Math.abs(cSoldeCa.doubleValue());
                if (dc >= ds) {
                    break;
                }

                CASection sec = (CASection) mgr.getEntity(i);

                FWCurrency cSolde = sec.getSoldeToCurrency();

                // Vérifier si elle est candidate (section négative si débiteur,
                // positive si créancier)
                if ((type == APICompteAnnexe.PC_TYPE_INTERNE_DEBITEUR) && cSolde.isNegative()) {
                    cComp.add(cSolde);
                    result.add(sec);
                } else if ((type == APICompteAnnexe.PC_TYPE_INTERNE_CREANCIER) && cSolde.isPositive()) {
                    cComp.add(cSolde);
                    result.add(sec);
                }

            }
        }

        for (int i = 0; i < mgr.size(); i++) {
            CASection sec = (CASection) mgr.getEntity(i);

            // En cas de compensation interne, on fournit toutes les sections
            if (type == APICompteAnnexe.PC_TYPE_INTERNE_ZERO) {
                result.add(sec);
            } else if ((type == APICompteAnnexe.PC_TYPE_FACTURATION)
                    && (sec.getCategorieSection().equals(CACompteAnnexe.CS_APG)
                            || sec.getCategorieSection().equals(CACompteAnnexe.CS_IJAI)
                            || sec.getCategorieSection().equals(CACompteAnnexe.CS_RETENU_SURRENTE)
                            || sec.getCategorieSection().equals(CACompteAnnexe.CS_PREST_CONVENTIONNELLE) || sec
                            .getCategorieSection().equals(CACompteAnnexe.CS_AF))) {
                // Pour la facturation, on ignore les APG et les IJAI
                continue;
            } else if ((type == APICompteAnnexe.PC_TYPE_FACTURATION)
                    && sec.getIdModeCompensation().equals(APISection.MODE_REPORT)) {
                // Pour la facturation, on ignore les sections de type report
                continue;
            } else {
                if (checkMontant) {
                    // Pour les compensations avec montant, vérifier le montant
                    // à compenser
                    FWCurrency cSolde = sec.getSoldeToCurrency();
                    // Solde non nul et de signe opposé
                    if (cSolde.signum() != cComp.signum()) {
                        // Si le solde est <= au montant à compenser,
                        // compensation totale
                        cSolde.negate();
                        if ((cSolde.isPositive() && (cSolde.compareTo(cComp) <= 0))
                                || (cSolde.isNegative() && (cSolde.compareTo(cComp) >= 0))) {
                            result.add(sec);
                            cComp.sub(cSolde);
                            // Si le solde est supérieur au montant à compenser,
                            // compensation partielle s'il reste quelque chose
                        } else if (!cComp.isZero()) {
                            result.add(sec);
                            break;
                        }
                    }
                } else {
                    // Pour les compensations avec montant, vérifier le montant
                    // à compenser
                    FWCurrency cSolde = sec.getSoldeToCurrency();
                    // Solde non nul et de signe opposé
                    if (cSolde.signum() != cComp.signum()) {
                        result.add(sec);
                    }
                }
            }

            // Sortir s'il n'y a plus rien à compenser
            if ((type != APICompteAnnexe.PC_TYPE_INTERNE_ZERO) && cComp.isZero()) {
                break;
            }
        }

        return result;
    }

    /**
     * Supprime une opération S'il s'agit d'une écriture ou d'une opération auxiliaire, on met à jour le solde du compte
     * annexe.
     * 
     * @return FWMessage null s'il n'y a pas d'erreur, sinon un message d'erreur
     * @param oper
     *            globaz.osiris.db.comptes.CAOperation L'opération à supprimer
     */
    public FWMessage removeOperation(CAOperation oper) {
        // Initialiser un nouveau message
        FWMessage msg = null;

        if ((oper.isInstanceOrSubClassOf(APIOperation.CAECRITURE))
                || (oper.isInstanceOrSubClassOf(APIOperation.CAAUXILIAIRE))) {
            try {
                FWCurrency solde = new FWCurrency(getSolde());
                solde.sub(oper.getMontant());
                setSolde(solde.toString());
            } catch (Exception e) {
                msg = new FWMessage();
                msg.setMessageId("5168");
                msg.setComplement(e.getMessage());
                msg.setIdSource("CACompteAnnexe");
                msg.setTypeMessage(FWMessage.ERREUR);
            }
        }
        return msg;
    }

    /**
     * @param boolean1
     */
    public void setASurveiller(Boolean boolean1) {
        aSurveiller = boolean1;
    }

    /**
     * Set le blocage de l'amende statutaire, par défaut: false
     * 
     * @param bloquerAmendeStatutaire
     */
    public void setBloquerAmendeStatutaire(Boolean bloquerAmendeStatutaire) {
        this.bloquerAmendeStatutaire = bloquerAmendeStatutaire;
    }

    /**
     * Set le blocage des frais de poursuites, par défaut : false
     * 
     * @param bloquerFraisPoursuite
     */
    public void setBloquerFraisPoursuite(Boolean bloquerFraisPoursuite) {
        this.bloquerFraisPoursuite = bloquerFraisPoursuite;
    }

    /**
     * Set le blocage de la taxe de sommation, par défaut : false
     * 
     * @param bloquerTaxeSommation
     */
    public void setBloquerTaxeSommation(Boolean bloquerTaxeSommation) {
        this.bloquerTaxeSommation = bloquerTaxeSommation;
    }

    /**
     * Sets the contDateDebBloque.
     * 
     * @param contDateDebBloque
     *            The contDateDebBloque to set
     */
    @Override
    public void setContDateDebBloque(String contDateDebBloque) {
        this.contDateDebBloque = contDateDebBloque;
    }

    /**
     * Sets the contDateFinBloque.
     * 
     * @param contDateFinBloque
     *            The contDateFinBloque to set
     */
    @Override
    public void setContDateFinBloque(String contDateFinBloque) {
        this.contDateFinBloque = contDateFinBloque;
    }

    /**
     * Sets the contEstBloque.
     * 
     * @param contEstBloque
     *            The contEstBloque to set
     */
    @Override
    public void setContEstBloque(Boolean contEstBloque) {
        this.contEstBloque = contEstBloque;
    }

    public void setDescription(String newDescription) {
        if (!JadeStringUtil.isBlank(newDescription) && (newDescription.length() >= 50)) {
            description = newDescription.substring(0, 50);
        } else {
            description = newDescription;
        }
    }

    public void setDescriptionUpperCase(String descriptionUpperCase) {
        if (!JadeStringUtil.isBlank(descriptionUpperCase) && (descriptionUpperCase.length() >= 50)) {
            this.descriptionUpperCase = descriptionUpperCase.substring(0, 50);
        } else {
            this.descriptionUpperCase = descriptionUpperCase;
        }
    }

    public void setEstConfidentiel(Boolean newEstConfidentiel) {
        estConfidentiel = newEstConfidentiel;
    }

    public void setEstVerrouille(Boolean newEstVerrouille) {
        estVerrouille = newEstVerrouille;
    }

    /**
     * @param string
     */
    public void setIdCategorie(String s) {
        idCategorie = s;
    }

    /**
     * Setter
     */
    @Override
    public void setIdCompteAnnexe(String newIdCompteAnnexe) {
        idCompteAnnexe = newIdCompteAnnexe;
    }

    /**
     * Sets the idContMotifBloque.
     * 
     * @param idContMotifBloque
     *            The idContMotifBloque to set
     */
    @Override
    public void setIdContMotifBloque(String idContMotifBloque) {
        this.idContMotifBloque = idContMotifBloque;
    }

    @Override
    public void setIdExterneRole(String newIdExterneRole) {
        idExterneRole = newIdExterneRole;
    }

    /**
     * Set le genre du compte. Par défaut standard.
     * 
     * @param string
     */
    public void setIdGenreCompte(String s) {
        idGenreCompte = s;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.01.2002 16:38:56)
     * 
     * @param newIdJournal
     *            String
     */
    public void setIdJournal(String newIdJournal) {
        idJournal = newIdJournal;
        journal = null;
    }

    @Override
    public void setIdRole(String newIdRole) {
        idRole = newIdRole;
        caRole = null;
        intRole = null;
    }

    public void setIdTiers(String newIdTiers) {
        idTiers = newIdTiers;
        intTiers = null;
    }

    /**
     * @param string
     */
    @Override
    public void setIdTri(String s) {
        idTri = s;
    }

    /**
     * Set le mode de traitement du bulletin neutre (défaut, ventilation, crédit)
     * 
     * @param string
     */

    public void setModeBulletinNeutre(String modeBulletinNeutre) {
        this.modeBulletinNeutre = modeBulletinNeutre;
    }

    /**
     * Set la qualité du débiteur
     * 
     * @param string
     */
    public void setQualiteDebiteur(String string) {
        qualiteDebiteur = string;
    }

    /**
     * Set la remarque sur le compte annexe
     * 
     * @param string
     */
    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public void setSolde(String newSolde) {
        solde = newSolde;
    }

    /**
     * Synchroniser la description du compte annexe avec le tiers Date de création : (29.01.2002 16:50:50)
     */
    @Override
    public void synchroniser() {
        IntTiers monTiers = getTiers();
        // Instancier un nouveau tiers
        if (monTiers == null) {
            _addError(null, getSession().getLabel("7067"));
        } else {
            // Mettre à jour la description avec le nom du tiers
            setDescription(monTiers.getNom());

            setDescriptionUpperCase(JadeStringUtil.convertSpecialChars(monTiers.getNom()).toUpperCase());

            // mise à jour du idCetgorie
            setIdCategorie(getRole().getIdCategorie(getIdExterneRole()));
        }
    }

    /**
     * Synchroniser le compteur en fonction des opérations liées Date de création : (06.02.2002 13:37:51)
     */
    public void synchronizeFromOperations(BTransaction tr) {
        // Initialisation
        String _lastIdOperation = "0";
        // Vider les variables de cumul
        setSolde("0.00");
        // Instancer un manager pour récupérer les types d'opération
        CAEcritureManager mgr = new CAEcritureManager();
        mgr.setSession(getSession());
        mgr.setForIdCompteAnnexe(getIdCompteAnnexe());
        // Récupérer les opérations
        while (true) {
            // Récupérer une série d'opérations
            try {
                mgr.clear();
                mgr.find(tr);
            } catch (Exception e) {
                _addError(null, e.getMessage());
                return;
            }
            // Sortir s'il n'y a aucune opération trouvée
            if (mgr.size() == 0) {
                break;
            }
            // Récupérer les opérations
            for (int i = 0; i < mgr.size(); i++) {
                // Récupérer une opération et la convertir dans le type
                // d'opération
                CAOperation _operX = (CAOperation) mgr.getEntity(i);
                CAOperation _oper = _operX.getOperationFromType(tr);
                // Si l'opération n'a pas été convertie
                if (_oper == null) {
                    _addError(null, getSession().getLabel("5013") + " " + _operX.getIdOperation());
                    return;
                } else {
                    // Instancier un objet en fonction du type d'opération
                    _oper.setSession(getSession());
                    // Conserver le dernier ID
                    _lastIdOperation = _oper.getIdOperation();
                    // Si l'opération est active
                    if (_oper.getEstActive().booleanValue()) {
                        // Mettre à jour
                        FWMessage msg = addOperation(_oper);
                        if (msg != null) {
                            _addError(null, msg.getMessageText());
                            return;
                        }
                    }
                }
            }
            // Charger les opérations suivantes sauf si vide
            if (JadeStringUtil.isIntegerEmpty(_lastIdOperation)) {
                break;
            }
            mgr.setAfterIdOperation(_lastIdOperation);
        }
    }

    /**
     * Test la section et le mode de sélections des sections pour savoir si le solde de l'opération doit-être soustrait
     * au solde du compte annexe pour l'affichage du solde initial.
     * 
     * @param operation
     * @param forSelectionSections
     * @return
     * @throws Exception
     */
    private boolean testForSelectionSections(CAOperation operation, String forSelectionSections) throws Exception {
        if (forSelectionSections.equals(CAExtraitCompteManager.SOLDE_ALL)) {
            return true;
        } else if ((forSelectionSections.equals(CAExtraitCompteManager.SOLDE_OPEN))
                && (operation.getSection().getSoldeToCurrency().isZero())) {
            return false;
        } else if ((forSelectionSections.equals(CAExtraitCompteManager.SOLDE_CLOSED))
                && (!operation.getSection().getSoldeToCurrency().isZero())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @see globaz.globall.db.BEntity#toString()
     */
    public String toMyString() {
        try {
            return "[" + getIdCompteAnnexe() + "] " + getIdExterneRole() + " " + getDescription();
        } catch (Exception e) {
            return super.toString();
        }
    }
}
