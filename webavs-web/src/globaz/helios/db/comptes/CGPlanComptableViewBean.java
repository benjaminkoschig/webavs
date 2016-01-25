package globaz.helios.db.comptes;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.helios.db.avs.CGCompteCompteOfas;
import globaz.helios.db.avs.CGCompteCompteOfasManager;
import globaz.helios.db.avs.CGCompteOfas;
import globaz.helios.db.avs.CGCompteOfasManager;
import globaz.helios.db.avs.CGPlanComptableAVS;
import globaz.helios.db.avs.CGPlanComptableAVSManager;
import globaz.helios.db.avs.CGSecteurAVS;
import globaz.helios.db.avs.CGSecteurAVSManager;
import globaz.helios.db.classifications.CGClasseCompte;
import globaz.helios.db.classifications.CGClasseCompteManager;
import globaz.helios.db.classifications.CGClassification;
import globaz.helios.db.classifications.CGClassificationManager;
import globaz.helios.db.classifications.CGLiaisonCompteClasse;
import globaz.helios.db.classifications.CGLiaisonCompteClasse_ClassManager;
import globaz.helios.db.interfaces.CGLibelle;
import globaz.helios.db.interfaces.CGLibelleInterface;
import globaz.helios.db.interfaces.ITreeListable;
import globaz.helios.db.utils.CGCompteAVS;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import java.util.HashSet;

/**
 * Classe : CGPlanComptableViewBean
 * 
 * Description : Classe définissant un plan comptable. Utilisé comme classe de liaison en le compte et l'exercice
 * comptable.
 * 
 * Date de création: 29 oct. 03
 * 
 * @author scr
 * 
 */
public class CGPlanComptableViewBean extends CGCompte implements ITreeListable, CGLibelleInterface,
        java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static int AK_EXTERNE = 1;

    public final static String FIELD_IDEXTERNE = "IDEXTERNE";
    public final static String FIELD_LIBELLE_DE = "LIBELLEDE";
    public final static String FIELD_LIBELLE_FR = "LIBELLEFR";
    public final static String FIELD_LIBELLE_IT = "LIBELLEIT";

    private final static String LABEL_PREFIXE = "PLAN_COMPTABLE_";

    public final static String TABLE_CGPLANP = "CGPLANP";

    private Boolean aReouvrir = new Boolean(false);

    private boolean autoInherit = true;
    private String avoir = "";
    private String avoirMonnaie = "";
    private String avoirProvisoire = "";

    private String avoirProvisoireMonnaie = "";

    private String budget = "";
    private CGCompteAVS compteAVS = null;

    private String doit = "";
    private String doitMonnaie = "";

    private String doitProvisoire = "";
    private String doitProvisoireMonnaie = "";

    private Boolean estCompteAvs = null;
    private Boolean estPeriode = new Boolean(false);
    private String idCentreCharge = "0";
    private String idCompteOfas = null;
    private String idExterne = new String();
    private String idExterneSave = "";
    private String idSolde = "";
    private String libelleDe = new String();
    private String libelleFr = new String();
    private String libelleIt = new String();
    private String solde = "";

    private String soldeMonnaie = "";
    private String soldeProvisoire = "";

    private String soldeProvisoireMonnaie = "";

    /**
     * Commentaire relatif au constructeur CGPlanComptable
     */
    public CGPlanComptableViewBean() {
        super();
    }

    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        idExterneSave = idExterne;
        if ((isEstCompteAvs() != null) && isEstCompteAvs().booleanValue()) {
            if (_autoInherits()) {
                String idClassificationSecteur = "";
                String idClassificationCompte = "";
                CGClassificationManager classificationManager = new CGClassificationManager();
                classificationManager.setSession(getSession());
                classificationManager.setForIdMandat(getExerciceComptable().getMandat().getIdMandat());
                classificationManager.find(transaction);
                for (int i = 0; i < classificationManager.size(); i++) {
                    CGClassification classification = (CGClassification) classificationManager.getEntity(i);
                    if (classification.getIdTypeClassification().equals(CGClassification.CS_TYPE_AVS_COMPTE)) {
                        idClassificationCompte = classification.getIdClassification();
                    } else if (classification.getIdTypeClassification().equals(CGClassification.CS_TYPE_AVS_SECTEUR)) {
                        idClassificationSecteur = classification.getIdClassification();
                    }
                }
                // Ouverture Compte Ofas
                CGCompteOfas compteOfas = new CGCompteOfas();
                compteOfas = ouvertureCompteOFAS(this, transaction);
                if (!compteOfas.isNew()) {
                    ouvertureCompteOFASFictif(compteOfas, this, transaction);
                    ouvertureLiaisonCompteCompteOFAS(this, compteOfas, transaction);
                }

                CGCompteAVS compteAvs = new CGCompteAVS(idExterne);

                // Compte AVS : creation du lien classe - compte dans la classification par secteur
                compteAvsLienClasseCompteSecteur(transaction, compteAvs, idClassificationSecteur);

                // Compte AVS : création du lien classe - compte dans la classification par compte
                compteAvsLienClasseCompteCompte(transaction, compteAvs, idClassificationCompte);

            }

        } else {

            if (CGMandat.CS_PC_USAM.equals(getExerciceComptable().getMandat().getIdTypePlanComptable())) {
                // Plan comptable "USAM / KEFIR" : création de lien classe - compte
                // pas encore definit

            } else {
                if (_autoInherits()) {

                    CGClassificationManager classificationManager = new CGClassificationManager();
                    classificationManager.setSession(getSession());
                    classificationManager.setForIdMandat(getExerciceComptable().getMandat().getIdMandat());
                    classificationManager.setForIdTypeClassification(CGClassification.CS_TYPE_DOMAINE);
                    classificationManager.find(transaction, 2);

                    if (classificationManager.size() != 1) {
                        throw new Exception("Plusieurs classification de type domaine existent pour le mandat : "
                                + getExerciceComptable().getMandat().getIdMandat());
                    }

                    CGClassification classification = (CGClassification) classificationManager.getEntity(0);

                    // Plan comptable "par domaine" : création de lien classe - compte
                    compteLienClasseCompteDomaine(transaction, getIdDomaine(), getIdGenre(),
                            classification.getIdClassification());
                }
            }

        }
    }

    /**
     * @see globaz.globall.db.BEntity#_afterDelete(BTransaction)
     * 
     *      Après l'effacement du plan comptable, contrôle s'il existe d'autres plan comptable associé au compte à
     *      effacer. Si oui, on les supprime également.
     * 
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        CGPlanComptableManager mgr = new CGPlanComptableManager();
        mgr.setSession(getSession());
        mgr.setForIdCompte(getIdCompte());
        mgr.find(transaction);

        if (mgr.size() > 0) {
            CGPlanComptableViewBean entity = (CGPlanComptableViewBean) mgr.getEntity(0);
            // Dernier entity à effacer. Sette le flag isAuthoinherit a true. De cette manière,
            // le parent du plan comptable (cad le compte sera lui aussi effacé.)
            // en d'autre mots, après l'effacement du dernier plan comptable, on efface le compte
            // associés à ces différents plan comptable.
            if (mgr.size() == 1) {
                entity.wantAutoInherit(true);
            } else {
                entity.wantAutoInherit(false);
            }
            entity.delete(transaction);
        }
    }

    @Override
    protected void _afterRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        super._afterRetrieve(transaction);
        idExterneSave = idExterne;
    }

    @Override
    protected void _afterUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        idExterneSave = idExterne;
    }

    @Override
    protected boolean _autoInherits() {
        return autoInherit;
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        /* le plan comptable n'existe pas */
        CGPlanComptableViewBean compte = new CGPlanComptableViewBean();
        compte.setSession(getSession());
        compte.setIdExterne(getIdExterne());
        compte.setIdExerciceComptable(getIdExerciceComptable());
        compte.setAlternateKey(CGPlanComptableViewBean.AK_EXTERNE);
        compte.retrieve(transaction);
        if (!compte.isNew()) {
            _addError(transaction, label("VALID_ER_2"));
        }

        // Si le compte existe dans l'exercice précédent, idCompte du plan comptable doit être le même.
        CGPlanComptableManager pcMgr = new CGPlanComptableManager();
        pcMgr.setSession(getSession());

        CGExerciceComptable exercice = getExerciceComptable();
        CGExerciceComptable exercicePrecedent = exercice.retrieveLastExerciceContigu(transaction);
        boolean accountAlreadyExist = false;

        if ((exercicePrecedent != null) && !exercicePrecedent.isNew()) {
            pcMgr.setForIdExerciceComptable(exercicePrecedent.getIdExerciceComptable());
            pcMgr.setForIdExterne(getIdExterne());
            pcMgr.setForIdMandat(getIdMandat());
            pcMgr.find(transaction);
            // Si le compte a déjà été créé dans l'exercice précédent, l'id du compte
            // doit être le même.
            if (pcMgr.size() > 0) {
                setIdCompte(((CGPlanComptableViewBean) pcMgr.getEntity(0)).getIdCompte());
                accountAlreadyExist = true;
            } else {
                setIdCompte(this._incCounter(transaction, "0"));
            }
        } else {
            setIdCompte(this._incCounter(transaction, "0"));
        }

        if (_autoInherits()) {
            CGMandat mandat = getExerciceComptable().getMandat();
            setIdMandat(mandat.getIdMandat());
            setEstCompteAvs(mandat.isEstComptabiliteAVS().booleanValue());
        }

        if ((isEstCompteAvs() != null) && isEstCompteAvs().booleanValue()) {
            if (_autoInherits()) {
                CGCompteAVS compteAvs = new CGCompteAVS(idExterne);
                JadeLogger.info(this, "creation du compte AVS : " + compteAvs.getCompte());

                // Compte AVS : vérification du numéro de compte
                compteAvsVerificationNumeroCompte(transaction, compteAvs);

                // Compte AVS : compléter le libellé
                compteAvsCompleterLibelle(transaction, compteAvs);

                // Compte AVS : préparation à l'ouverture du compte
                compteAvsPreparationOuvertureCompte(transaction, compteAvs);
            }
        }
        // mandat non avs
        else {
            // Pour les comptes de bilan, on accepte uniquement les genres : actif, passif, ouverture et clôture
            if (CGCompte.CS_COMPTE_BILAN.equals(getIdDomaine())) {

                if (!CGCompte.CS_GENRE_ACTIF.equals(getIdGenre()) && !CGCompte.CS_GENRE_PASSIF.equals(getIdGenre())
                        && !CGCompte.CS_GENRE_OUVERTURE.equals(getIdGenre())
                        && !CGCompte.CS_GENRE_CLOTURE.equals(getIdGenre())) {

                    _addError(transaction, label("GENRE_ERROR") + getIdExterne());
                    throw (new Exception(label("GENRE_ERROR")));
                }

            }
            // Pour les autres comptes, on accepte les autres genres
            else {
                if (!CGCompte.CS_GENRE_CHARGE.equals(getIdGenre()) && !CGCompte.CS_GENRE_PRODUIT.equals(getIdGenre())
                        && !CGCompte.CS_GENRE_RESULTAT.equals(getIdGenre())) {

                    _addError(transaction, label("GENRE_ERROR") + getIdExterne());
                    throw (new Exception(label("GENRE_ERROR")));
                }
            }
        }

        // Le compte existe déjà dans l'exercice précédent pour le même mandat -> on ne le recréé pas.
        if (accountAlreadyExist) {
            wantAutoInherit(false);
        }
    }

    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // L'utilisateur rentre une remarque
        if (hasRemarque()) {

            // La remarque n'existe pas en DB
            if (!hasCGRemarque()) {
                CGRemarque remTemp = new CGRemarque();
                remTemp.setSession(getSession());
                remTemp.setRemarque(getRemarque());
                remTemp.add(transaction);
                CGCompte compte = new CGCompte();
                compte.setSession(getSession());
                compte.setIdCompte(getIdCompte());
                compte.retrieve(transaction);
                compte.setIdRemarque(remTemp.getIdRemarque());
                compte.update(transaction);
            }
            // La remarque existe et on vérifie que c'est bien la même, sinon on la met à jour
            else {
                CGRemarque remTemp = retrieveCGRemarque();
                // La remarque n'existe plus en BD, donc on la créée
                if (remTemp.isNew()) {
                    remTemp.setRemarque(getRemarque());
                    remTemp.setSession(getSession());
                    remTemp.add(transaction);
                    CGCompte compte = new CGCompte();
                    compte.setSession(getSession());
                    compte.setIdCompte(getIdCompte());
                    compte.retrieve(transaction);
                    compte.setIdRemarque(remTemp.getIdRemarque());
                    compte.update(transaction);
                }
                // Si c'est pas la même, on update !
                else if (!remTemp.getRemarque().equals(getRemarque())) {
                    remTemp.setRemarque(getRemarque());
                    remTemp.update(transaction);
                }
            }
        }

        if (getExerciceComptable().getMandat().isEstComptabiliteAVS().booleanValue() && !"".equals(idExterneSave)) {
            if (!getIdExterne().substring(0, 3).equals(idExterneSave.substring(0, 3))
                    || !getIdExterne().substring(5, 9).equals(idExterneSave.substring(5, 9))) {
                throw new Exception(label("MODIF_NON_AUTHORISEE"));
            }
        }
    }

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String table1 = _getCollection() + "CGPLANP";
        String table2 = _getCollection() + "CGCOMTP";
        String table3 = _getCollection() + "CGSOLDP";
        String from = " " + table1;
        from += " INNER JOIN " + table2 + " ON (" + table1 + ".IDCOMPTE=" + table2 + ".IDCOMPTE)";
        from += " LEFT OUTER JOIN " + table3 + " ON (" + table1 + ".IDCOMPTE=" + table3 + ".IDCOMPTE and ";
        from += table3 + ".ESTPERIODE="
                + this._dbWriteBoolean(statement.getTransaction(), isEstPeriode(), BConstants.DB_TYPE_BOOLEAN_CHAR)
                + " and " + table1 + ".IDEXERCOMPTABLE = " + table3 + ".IDEXERCOMPTABLE )";

        return from;

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return CGPlanComptableViewBean.TABLE_CGPLANP;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idExerciceComptable = statement.dbReadNumeric("IDEXERCOMPTABLE");
        idCompte = statement.dbReadNumeric("IDCOMPTE");
        idExterne = statement.dbReadString("IDEXTERNE");
        libelleFr = statement.dbReadString("LIBELLEFR");
        libelleDe = statement.dbReadString("LIBELLEDE");
        libelleIt = statement.dbReadString("LIBELLEIT");
        aReouvrir = statement.dbReadBoolean("AREOUVRIR");

        super._readProperties(statement);

        // CGSoldeExrcice
        solde = statement.dbReadNumeric("SOLDE", 2);
        soldeProvisoire = statement.dbReadNumeric("SOLDEPROVISOIRE", 2);
        avoir = statement.dbReadNumeric("AVOIR", 2);
        avoirProvisoire = statement.dbReadNumeric("AVOIRPROVISOIRE", 2);
        doit = statement.dbReadNumeric("DOIT", 2);
        doitProvisoire = statement.dbReadNumeric("DOITPROVISOIRE", 2);
        idSolde = statement.dbReadNumeric("IDSOLDE");

        soldeProvisoireMonnaie = statement.dbReadNumeric("SOLDEPROVIMONNAIE", 2);
        soldeMonnaie = statement.dbReadNumeric("SOLDEMONNAIE", 2);
        avoirMonnaie = statement.dbReadNumeric("AVOIRMONNAIE", 2);
        budget = statement.dbReadNumeric("BUDGET", 2);
        avoirProvisoireMonnaie = statement.dbReadNumeric("AVOIRPROVIMONNAIE", 2);
        doitMonnaie = statement.dbReadNumeric("DOITMONNAIE", 2);
        doitProvisoireMonnaie = statement.dbReadNumeric("DOITPROVIMONNAIE", 2);

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {

        /* idExterne -obligatoire */
        if (JadeStringUtil.isBlank(getIdExterne())) {
            _addError(statement.getTransaction(), label("VALID_ER_1"));
        }

        // libelle dans la langue de l'application obligatoire
        if (CGLibelle.isAppLanguageLibelleEmpty(this)) {
            _addError(statement.getTransaction(), label("VALID_ER_3") + " compte : " + getIdExterne());
        }

        /* idExterne - verifie structure compte */
        if (!checkIdExterne()) {
            _addError(statement.getTransaction(), label("VALID_ER_4"));
        }

        if (getExerciceComptable().getMandat().isEstComptabiliteAVS().booleanValue()) {
            CGCompteAVS compteAvs = new CGCompteAVS(idExterne);
            if (!compteAvs.hasValidFormat()) {
                _addError(statement.getTransaction(), label("VALID_ER_5"));
            }
        }
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité composant une clé alternée
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     * @param alternateKey
     *            int le numéro de la clé alternée à utiliser
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        if (alternateKey == CGPlanComptableViewBean.AK_EXTERNE) {
            statement.writeKey(_getCollection() + _getTableName() + "." + "IDEXERCOMPTABLE",
                    this._dbWriteNumeric(statement.getTransaction(), getIdExerciceComptable(), ""));
            statement.writeKey(_getCollection() + _getTableName() + "." + "IDEXTERNE",
                    this._dbWriteString(statement.getTransaction(), getIdExterne(), ""));
            // statement.writeKey(_getCollection() + "CGSOLDP." +
            // "IDCENTRECHARGE",_dbWriteNumeric(statement.getTransaction(), getIdCentreCharge(),""));
        }
    }

    /**
	 *
	 */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(_getCollection() + _getTableName() + "." + "IDEXERCOMPTABLE",
                this._dbWriteNumeric(statement.getTransaction(), getIdExerciceComptable(), ""));
        statement.writeKey(_getCollection() + _getTableName() + "." + "IDCOMPTE",
                this._dbWriteNumeric(statement.getTransaction(), getIdCompte(), ""));
        // statement.writeKey(_getCollection() + "CGSOLDP." +
        // "IDCENTRECHARGE",_dbWriteNumeric(statement.getTransaction(), getIdCentreCharge(),""));
        // statement.writeKey(_getCollection() + "CGSOLDP" + "." +
        // "ESTPERIODE",_dbWriteBoolean(statement.getTransaction(),isEstPeriode(),BConstants.DB_TYPE_BOOLEAN_CHAR,""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        if (_getAction() == BEntity.ACTION_COPY) {
            super._writeProperties(statement);
        }

        statement.writeField("IDEXERCOMPTABLE",
                this._dbWriteNumeric(statement.getTransaction(), getIdExerciceComptable(), "idExerciceComptable"));
        statement.writeField("IDCOMPTE", this._dbWriteNumeric(statement.getTransaction(), getIdCompte(), "idCompte"));
        statement.writeField("IDEXTERNE", this._dbWriteString(statement.getTransaction(), getIdExterne(), "idExterne"));
        statement.writeField("LIBELLEFR", this._dbWriteString(statement.getTransaction(), getLibelleFr(), "libelleFr"));
        statement.writeField("LIBELLEDE", this._dbWriteString(statement.getTransaction(), getLibelleDe(), "libelleDe"));
        statement.writeField("LIBELLEIT", this._dbWriteString(statement.getTransaction(), getLibelleIt(), "libelleIt"));
        statement.writeField("AREOUVRIR", this._dbWriteBoolean(statement.getTransaction(), isAReouvrir(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "aReouvrir"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 11:40:16)
     * 
     * @return boolean
     * @param id
     *            String
     */
    public boolean checkIdExterne() throws Exception {
        boolean statut = false;

        CGMandat mandat = getExerciceComptable().getMandat();

        // Nomenclature
        if (mandat.getIdTypePlanComptable().equals(CGMandat.CS_PC_AVS)) {
            statut = CGCompteAVS.hasValidFormat(idExterne);
        } else if (mandat.getIdTypePlanComptable().equals(CGMandat.CS_PC_USAM)) {
            statut = CGCompte.hasValidFormat(idExterne, 4);
        } else if (mandat.getIdTypePlanComptable().equals(CGMandat.CS_PC_4CHIFFRES)) {
            statut = CGCompte.hasValidFormat(idExterne, 4);
        } else if (mandat.getIdTypePlanComptable().equals(CGMandat.CS_PC_5CHIFFRES)) {
            statut = CGCompte.hasValidFormat(idExterne, 5);
        } else if (mandat.getIdTypePlanComptable().equals(CGMandat.CS_PC_6CHIFFRES)) {
            statut = CGCompte.hasValidFormat(idExterne, 6);
        } else if (mandat.isTypePlanComptableOfas()) {
            statut = CGCompte.hasValidFormat(idExterne, 7);
        } else if (mandat.getIdTypePlanComptable().equals(CGMandat.CS_PC_AUTRE)) {
            statut = !JadeStringUtil.isBlank(idExterne);
            if (statut) {
                statut = JadeStringUtil.isDigit(idExterne);
            }
        }

        // existe dans notre base
        // A FAIRE

        return statut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 14:17:26)
     */
    public void compteAvsCompleterLibelle(BTransaction transaction, CGCompteAVS compteAvs) throws Exception {
        // Compte AVS : compléter le libellé
        CGPlanComptableAVS plan = new CGPlanComptableAVS();
        plan.setSession(getSession());
        if (JadeStringUtil.isBlank(libelleDe + libelleFr + libelleIt)) {
            plan.setNumeroCompteAVS(compteAvs.getCompteOFAS());
            plan.retrieve(transaction);
            if (!JadeStringUtil.isBlank(plan.getLibelleDe() + plan.getLibelleFr() + plan.getLibelleIt())) {
                libelleDe = plan.getLibelleDe();
                libelleFr = plan.getLibelleFr();
                libelleIt = plan.getLibelleIt();

            } else {
                plan.setNumeroCompteAVS(compteAvs.getCompte());
                plan.retrieve(transaction);
                if (!JadeStringUtil.isBlank(plan.getLibelleDe() + plan.getLibelleFr() + plan.getLibelleIt())) {
                    libelleDe = plan.getLibelleDe();
                    libelleFr = plan.getLibelleFr();
                    libelleIt = plan.getLibelleIt();
                } else {
                    throw new Exception(label("NO_LIBELLE") + "Cpte : " + compteAvs.getFullCompteNoFormat());
                }
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 14:17:26)
     */
    public CGCompteOfas compteAvsCreationCompteOFAS(BTransaction transaction, CGCompteAVS compteAvs) throws Exception {
        CGCompteOfas cptOFAS = null;
        CGCompteOfasManager cptOFASManager = new CGCompteOfasManager();
        cptOFASManager.setForIdExterne(compteAvs.getCompteOFASFormat());
        cptOFASManager.setForIdMandat(getExerciceComptable().getIdMandat());
        cptOFASManager.setSession(getSession());
        cptOFASManager.find(transaction);
        if (cptOFASManager.size() == 0) {
            // si il n'existe pas

            cptOFAS = new CGCompteOfas();

            cptOFAS.setIdExterne(compteAvs.getCompteOFASFormat());
            /*
             * n'existe plus ? -oca 10.12.02
             * 
             * cptOFAS.setIdDomaine(compte.getIdDomaine()); cptOFAS.setIdNature(compte.getIdNature());
             * cptOFAS.setIdGenre(compte.getIdGenre());
             * 
             * 
             * if (compteAvs.isCompteAffilies()) { cptOFAS.setIdNature(CGCompteOfas.CS_CC_AFFILIES); } else {
             * cptOFAS.setIdNature(CGCompteOfas.CS_STANDARD); }
             */

            cptOFAS.setNonAnnoncable(new Boolean(false));

            CGPlanComptableAVS plan = new CGPlanComptableAVS();
            plan.setSession(getSession());
            plan.setNumeroCompteAVS(compteAvs.getCompte());
            plan.retrieve(transaction);
            cptOFAS.setLibelleDe(plan.getLibelleDe());
            cptOFAS.setLibelleFr(plan.getLibelleFr());
            cptOFAS.setLibelleIt(plan.getLibelleIt());
            cptOFAS.setSession(getSession());
            cptOFAS.setIdMandat(getExerciceComptable().getIdMandat());

            cptOFAS.add(transaction);
        } else {
            cptOFAS = (CGCompteOfas) cptOFASManager.getEntity(0);
        }

        return cptOFAS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 14:17:26)
     */
    public void compteAvsLienClasseCompteCompte(BTransaction transaction, CGCompteAVS compteAvs,
            String idClassificationCompte) throws Exception {

        String ERREUR = label("ERROR_1");
        CGLiaisonCompteClasse lien = new CGLiaisonCompteClasse();
        CGClasseCompte classe = null;
        CGClasseCompteManager classeManager = new CGClasseCompteManager();
        boolean found = false;
        classeManager.setForIdClassification(idClassificationCompte);
        classeManager.setForNoClasse(compteAvs.getCompte());
        classeManager.setSession(getSession());
        classeManager.find(transaction);
        if (classeManager.size() == 0) {

            // la classe n'existe pas, création
            classe = new CGClasseCompte();
            classe.setNoClasse(compteAvs.getCompte());

            CGPlanComptableAVSManager planManager = new CGPlanComptableAVSManager();
            planManager.setForType(CGPlanComptableAVS.TYPE_COMPTE);
            planManager.setForNumeroCompteAVS(compteAvs.getCompte());
            planManager.setSession(getSession());
            planManager.find(transaction);
            found = false;
            if (planManager.size() > 0) {
                CGPlanComptableAVS plan = (CGPlanComptableAVS) planManager.getEntity(0);
                if (!plan.hasAllLibelleEmpty()) {
                    found = true;
                    classe.setLibelleFr(plan.getLibelleFr());
                    classe.setLibelleDe(plan.getLibelleDe());
                    classe.setLibelleIt(plan.getLibelleIt());
                }
            }
            if (!found) {
                throw new Exception(ERREUR);
            }

            // verification de la hierarchie

            // Niveau 1
            CGClasseCompte classe1 = null;
            classeManager.setForIdClassification(idClassificationCompte);
            classeManager.setForNoClasse(compteAvs.getCompte().substring(0, 1));
            classeManager.setSession(getSession());
            classeManager.find(transaction);
            if (classeManager.size() == 0) {
                classe1 = new CGClasseCompte();
                planManager = new CGPlanComptableAVSManager();
                planManager.setForNumeroCompteAVS(compteAvs.getCompte().substring(0, 1));
                planManager.setForType(CGPlanComptableAVS.TYPE_CLASSE);
                planManager.setSession(getSession());
                planManager.find(transaction);
                found = false;
                // 1er niveau
                if (planManager.size() > 0) {
                    CGPlanComptableAVS p = (CGPlanComptableAVS) planManager.getEntity(0);
                    if (!p.hasAllLibelleEmpty()) {
                        found = true;
                        classe1.setLibelleDe(p.getLibelleDe());
                        classe1.setLibelleFr(p.getLibelleFr());
                        classe1.setLibelleIt(p.getLibelleIt());

                    }
                } else {
                    CGSecteurAVS secteurAVS = new CGSecteurAVS();
                    secteurAVS.setSession(getSession());
                    secteurAVS.setIdMandat(getIdMandat());
                    secteurAVS.setIdSecteurAVS(compteAvs.getSecteur4());
                    secteurAVS.retrieve(transaction);
                    if ((secteurAVS != null) && !secteurAVS.isNew()) {
                        if (!JadeStringUtil.isBlank(secteurAVS.getLibelleDe() + secteurAVS.getLibelleFr()
                                + secteurAVS.getLibelleIt())) {
                            found = true;
                            classe1.setLibelleDe(secteurAVS.getLibelleDe());
                            classe1.setLibelleFr(secteurAVS.getLibelleFr());
                            classe1.setLibelleIt(secteurAVS.getLibelleIt());
                        }
                    }
                }

                if (!found) {
                    throw new Exception(ERREUR);
                }
                classe1.setIdClassification(idClassificationCompte);
                classe1.setImprimerTitre(new Boolean(true));
                classe1.setImprimerTotal(new Boolean(true));
                classe1.setImprimerResultat(new Boolean(false));
                classe1.setNoClasse(compteAvs.getCompte().substring(0, 1));
                classe1.setIdSuperClasse("0");
                // classe1.setIdClassification()
                classe1.setSession(getSession());
                classe1.add(transaction);
            } else {
                // 1er niveau exite
                classe1 = (CGClasseCompte) classeManager.getEntity(0);
            }

            // niveau 2
            CGClasseCompte classe2 = null;
            classeManager.setForIdClassification(idClassificationCompte);
            classeManager.setForNoClasse(compteAvs.getClasse());
            classeManager.setSession(getSession());
            classeManager.find(transaction);
            if (classeManager.size() == 0) {
                classe2 = new CGClasseCompte();

                planManager = new CGPlanComptableAVSManager();
                planManager.setForNumeroCompteAVS(compteAvs.getClasse());
                planManager.setForType(CGPlanComptableAVS.TYPE_CLASSE);
                planManager.setSession(getSession());
                planManager.find(transaction);
                found = false;

                if (planManager.size() > 0) {
                    CGPlanComptableAVS p = (CGPlanComptableAVS) planManager.getEntity(0);
                    if (!p.hasAllLibelleEmpty()) {
                        found = true;
                        classe2.setLibelleDe(p.getLibelleDe());
                        classe2.setLibelleFr(p.getLibelleFr());
                        classe2.setLibelleIt(p.getLibelleIt());
                    }
                }
                if (!found) {
                    throw new Exception(ERREUR);
                }

                classe2.setIdClassification(idClassificationCompte);
                classe2.setImprimerTitre(new Boolean(true));
                classe2.setImprimerTotal(new Boolean(true));
                classe2.setImprimerResultat(new Boolean(false));
                classe2.setNoClasse(compteAvs.getClasse());
                classe2.setIdSuperClasse(classe1.getIdClasseCompte());
                classe2.setSession(getSession());
                classe2.add(transaction);
            } else {
                classe2 = (CGClasseCompte) classeManager.getEntity(0);
            }
            // fin hierarchie

            classe.setNoClasse(compteAvs.getCompte());
            classe.setIdClassification(idClassificationCompte);
            classe.setNumeroOrdre("0");
            classe.setImprimerTitre(new Boolean(true));
            classe.setImprimerTotal(new Boolean(true));
            classe.setImprimerResultat(new Boolean(false));
            classe.setIdSuperClasse(classe2.getIdClasseCompte());
            classe.setSession(getSession());
            classe.add(transaction);

        } else {
            // existe deja
            classe = (CGClasseCompte) classeManager.getEntity(0);
        }

        lien.setIdClasseCompte(classe.getIdClasseCompte());
        lien.setIdCompte(getIdCompte());
        lien.setSession(getSession());
        lien.add(transaction);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 14:17:26)
     */
    public void compteAvsLienClasseCompteSecteur(BTransaction transaction, CGCompteAVS compteAvs,
            String idClassificationSecteur) throws Exception {

        String ERREUR = label("ERROR_2");
        CGLiaisonCompteClasse lien = new CGLiaisonCompteClasse();
        CGClasseCompte classe = null;
        CGClasseCompteManager classeManager = new CGClasseCompteManager();
        boolean found = false;

        classeManager.setForNoClasse(compteAvs.getClasseSecteur());
        classeManager.setForIdClassification(idClassificationSecteur);
        classeManager.setSession(getSession());
        classeManager.find(transaction);
        if (classeManager.size() == 0) {

            // la classe n'existe pas, création
            classe = new CGClasseCompte();

            CGPlanComptableAVS plan = new CGPlanComptableAVS();
            CGPlanComptableAVSManager planManager = new CGPlanComptableAVSManager();
            planManager.setForNumeroCompteAVS(compteAvs.getSecteur3() + "." + compteAvs.getCompte());
            planManager.setForType(CGPlanComptableAVS.TYPE_COMPTE);
            planManager.setSession(getSession());
            planManager.find(transaction);
            found = false;
            if (planManager.size() > 0) {
                plan = (CGPlanComptableAVS) planManager.getEntity(0);

                if (!plan.hasAllLibelleEmpty()) {
                    classe.setLibelleDe(plan.getLibelleDe());
                    classe.setLibelleFr(plan.getLibelleFr());
                    classe.setLibelleIt(plan.getLibelleIt());
                    found = true;
                }
            }
            if (!found) {
                planManager = new CGPlanComptableAVSManager();
                planManager.setForNumeroCompteAVS(compteAvs.getCompte());
                planManager.setSession(getSession());
                planManager.find(transaction);
                if (planManager.size() > 0) {
                    plan = (CGPlanComptableAVS) planManager.getEntity(0);

                    if (!plan.hasAllLibelleEmpty()) {
                        classe.setLibelleDe(plan.getLibelleDe());
                        classe.setLibelleFr(plan.getLibelleFr());
                        classe.setLibelleIt(plan.getLibelleIt());
                        found = true;
                    }
                }
            }

            if (!found) {
                throw new Exception(ERREUR);
            }

            // hierarchie
            // ==========

            // 1er niveau :
            classeManager = new CGClasseCompteManager();
            classeManager.setForIdClassification(idClassificationSecteur);
            classeManager.setForNoClasse(compteAvs.getSecteur1());
            classeManager.setSession(getSession());
            classeManager.find(transaction);
            found = false;
            CGClasseCompte classe1 = null;
            if (classeManager.size() == 0) {

                // 1er niveau n'existe pas : creation
                classe1 = new CGClasseCompte();
                planManager = new CGPlanComptableAVSManager();
                planManager.setForType(CGPlanComptableAVS.TYPE_SECTEUR);
                planManager.setForNumeroCompteAVS(compteAvs.getSecteur1());
                planManager.setSession(getSession());
                planManager.find(transaction);
                if (planManager.size() > 0) {
                    CGPlanComptableAVS p = (CGPlanComptableAVS) planManager.getEntity(0);
                    if (!p.hasAllLibelleEmpty()) {
                        found = true;
                        classe1.setLibelleDe(p.getLibelleDe());
                        classe1.setLibelleFr(p.getLibelleFr());
                        classe1.setLibelleIt(p.getLibelleIt());
                    }
                } else {
                    CGSecteurAVS secteurAVS = new CGSecteurAVS();
                    secteurAVS.setSession(getSession());
                    secteurAVS.setIdMandat(getIdMandat());
                    secteurAVS.setIdSecteurAVS(compteAvs.getSecteur4());
                    secteurAVS.retrieve(transaction);
                    if ((secteurAVS != null) && !secteurAVS.isNew()) {
                        if (!JadeStringUtil.isBlank(secteurAVS.getLibelleDe() + secteurAVS.getLibelleFr()
                                + secteurAVS.getLibelleIt())) {
                            found = true;
                            classe1.setLibelleDe(secteurAVS.getLibelleDe());
                            classe1.setLibelleFr(secteurAVS.getLibelleFr());
                            classe1.setLibelleIt(secteurAVS.getLibelleIt());
                        }
                    }
                }

                if (!found) {
                    throw new Exception(ERREUR);
                }
                classe1.setIdClassification(idClassificationSecteur);
                classe1.setNoClasse(compteAvs.getSecteur1());
                classe1.setImprimerTitre(new Boolean(true));
                classe1.setImprimerTotal(new Boolean(true));
                classe1.setImprimerResultat(new Boolean(false));
                classe1.setIdSuperClasse("0");
                classe1.setSession(getSession());
                classe1.add(transaction);
            } else {
                // 1eme niveau existe
                classe1 = (CGClasseCompte) classeManager.getEntity(0);
            }

            // 2er niveau :
            classeManager = new CGClasseCompteManager();
            classeManager.setForIdClassification(idClassificationSecteur);
            classeManager.setForNoClasse(compteAvs.getSecteur4());
            classeManager.setSession(getSession());
            classeManager.find(transaction);
            found = false;
            CGClasseCompte classe2 = null;
            if (classeManager.size() == 0) {
                // 2er niveau n'existe pas : creation
                classe2 = new CGClasseCompte();

                // cherche libelle
                CGSecteurAVSManager secteurManager = new CGSecteurAVSManager();
                secteurManager.setForIdSecteurAVS(compteAvs.getSecteur4());
                secteurManager.setSession(getSession());
                secteurManager.find(transaction);
                found = false;
                CGSecteurAVS secteur = null;
                if (secteurManager.size() == 0) {

                    secteurManager = new CGSecteurAVSManager();
                    secteurManager.setForIdSecteurAVS(compteAvs.getSecteur4());
                    secteurManager.setSession(getSession());
                    secteurManager.find(transaction);
                    if (secteurManager.size() > 0) {
                        found = true;
                    }
                } else {
                    found = true;
                }
                if (!found) {
                    // aucun libelle trouvé
                    throw new Exception(ERREUR);
                } else {
                    secteur = (CGSecteurAVS) secteurManager.getEntity(0);
                    if (!secteur.hasAllLibelleEmpty()) {
                        classe2.setLibelleDe(secteur.getLibelleDe());
                        classe2.setLibelleFr(secteur.getLibelleFr());
                        classe2.setLibelleIt(secteur.getLibelleIt());
                    }
                }

                classe2.setImprimerTitre(new Boolean(true));
                classe2.setImprimerTotal(new Boolean(true));

                String numSecteur = secteur.getIdSecteurAVS();
                if ((numSecteur.equals("1000")) || (numSecteur.equals("1990")) || (numSecteur.equals("2990"))
                        || (numSecteur.equals("3990")) || (numSecteur.equals("4990")) || (numSecteur.startsWith("999"))) {
                    classe2.setImprimerResultat(new Boolean(false));
                } else {
                    classe2.setImprimerResultat(new Boolean(true));
                }
                classe2.setIdClassification(idClassificationSecteur);
                classe2.setIdSuperClasse(classe1.getIdClasseCompte());
                classe2.setNoClasse(compteAvs.getSecteur4());
                classe2.setSession(getSession());
                classe2.add(transaction);
            } else {
                classe2 = (CGClasseCompte) classeManager.getEntity(0);

            }

            // fin hierarchie

            classe.setIdClassification(idClassificationSecteur);
            classe.setNoClasse(compteAvs.getClasseSecteur());
            classe.setNumeroOrdre("0");
            classe.setImprimerTitre(new Boolean(true));
            classe.setImprimerTotal(new Boolean(true));
            classe.setImprimerResultat(new Boolean(false));
            classe.setIdSuperClasse(classe2.getIdClasseCompte());
            classe.setSession(getSession());
            classe.add(transaction);

        } else {
            // la classe existe deja
            classe = (CGClasseCompte) classeManager.getEntity(0);
        }
        lien.setSession(getSession());
        lien.setIdClasseCompte(classe.getIdClasseCompte());
        lien.setIdCompte(getIdCompte());
        lien.add(transaction);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 14:17:26)
     */
    public void compteAvsLienCompteCompteOFAS(BTransaction transaction, CGCompte compte, CGCompteOfas compteOfas)
            throws Exception {
        CGCompteCompteOfas lien = new CGCompteCompteOfas();
        lien.setIdCompte(compte.getIdCompte());
        lien.setIdCompteOfas(compteOfas.getIdCompteOfas());
        lien.setSession(getSession());
        lien.add(transaction);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 14:17:26)
     */
    public void compteAvsLienCompteCompteOFAS(BTransaction transaction, CGCompteOfas compteOfas) throws Exception {
        CGCompteCompteOfas lien = new CGCompteCompteOfas();
        lien.setIdCompte(getIdCompte());
        lien.setIdCompteOfas(compteOfas.getIdCompteOfas());
        lien.setSession(getSession());
        lien.add(transaction);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 14:17:26)
     */
    public void compteAvsPreparationOuvertureCompte(BTransaction transaction, CGCompteAVS compteAvs) throws Exception {
        // Compte AVS : compléter le libellé

        if (compteAvs.isCompteBilan()) {
            setIdDomaine(CGCompte.CS_COMPTE_BILAN);
        } else if (compteAvs.isCompteExploitation()) {
            setIdDomaine(CGCompte.CS_COMPTE_EXPLOITATION);
        } else if (compteAvs.isCompteAdministration()) {
            setIdDomaine(CGCompte.CS_COMPTE_ADMINISTRATION);
        } else if (compteAvs.isCompteInvestissement()) {
            setIdDomaine(CGCompte.CS_COMPTE_INVESTISSEMENT);
        }

        if (compteAvs.isCompteActif()) {
            setIdGenre(CGCompte.CS_GENRE_ACTIF);
        } else if (compteAvs.isComptePassif()) {
            setIdGenre(CGCompte.CS_GENRE_PASSIF);
        } else if ((compteAvs.isCompteDepenseExploitation()) || (compteAvs.isCompteChargeAdministration())
                || (compteAvs.isCompteDepenseInvestissement())) {
            setIdGenre(CGCompte.CS_GENRE_CHARGE);
        } else if ((compteAvs.isCompteRecetteExploitation()) || (compteAvs.isCompteProduitAdministration())
                || (compteAvs.isCompteRecetteInvestissement())) {
            setIdGenre(CGCompte.CS_GENRE_PRODUIT);
        } else if (compteAvs.isCompteCloture()) {
            setIdGenre(CGCompte.CS_GENRE_CLOTURE);
        } else if (compteAvs.isCompteOuverture()) {
            setIdGenre(CGCompte.CS_GENRE_OUVERTURE);
        } else if (compteAvs.isCompteResultat()) {
            setIdGenre(CGCompte.CS_GENRE_RESULTAT);
        }
        // setIdMandat(getExerciceComptable().getIdMandat());
        setIdExerciceComptable(exerciceComptable.getIdExerciceComptable());
        setExerciceComptable(exerciceComptable);
        setNumeroCompteAVS(compteAvs.getCompte());
        setIdSecteurAVS(compteAvs.getSecteur4());

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 14:17:26)
     */
    public void compteAvsVerificationNumeroCompte(BTransaction transaction, CGCompteAVS compteAvs) throws Exception {
        // Compte AVS : vérification du numéro de compte
        CGPlanComptableAVSManager planManager = new CGPlanComptableAVSManager();

        planManager.setForNumeroCompteAVS(compteAvs.getCompte());
        planManager.setSession(getSession());
        planManager.find(transaction);
        if (planManager.size() < 1) {
            throw new Exception(label("ERROR_3"));
        }

    }

    /**
     * Method compteLienClasseCompteDomaine.
     * 
     * création de lien classe - compte pour mandat non avs
     * 
     * @param transaction
     * @param idExterne
     * @param idClassificationSecteur
     * @throws Exception
     */
    public void compteLienClasseCompteDomaine(BTransaction transaction, String csDomaineCompte, String csGenreCompte,
            String idClassificationDomaine) throws Exception {

        CGLiaisonCompteClasse lien = new CGLiaisonCompteClasse();
        CGClasseCompte classe = null;
        CGClasseCompteManager classeManager = new CGClasseCompteManager();

        classeManager.setForNoClasse(csDomaineCompte + "." + csGenreCompte.substring(3, 6));
        classeManager.setForIdClassification(idClassificationDomaine);
        classeManager.setSession(getSession());
        classeManager.find(transaction);
        if (classeManager.size() == 0) {
            // la classe n'existe pas, création
            classe = new CGClasseCompte();

            FWParametersSystemCode cs = new FWParametersSystemCode();
            cs.setSession(getSession());
            cs.getCode(csGenreCompte);

            classe.setLibelleDe(cs.getCodeUtilisateur("D").getLibelle());
            classe.setLibelleFr(cs.getCodeUtilisateur("F").getLibelle());
            classe.setLibelleIt(cs.getCodeUtilisateur("I").getLibelle());

            // hierarchie
            // ==========

            // 1er niveau :
            classeManager = new CGClasseCompteManager();
            classeManager.setForIdClassification(idClassificationDomaine);
            classeManager.setForNoClasse(csDomaineCompte);
            classeManager.setSession(getSession());
            classeManager.find(transaction);

            CGClasseCompte classe1 = null;
            if (classeManager.size() == 0) {

                // 1er niveau n'existe pas : creation
                classe1 = new CGClasseCompte();
                classe1.setSession(getSession());

                // Si pas de nouvelle instantiation, getCode génère une erreur !!!
                cs = new FWParametersSystemCode();
                cs.setSession(getSession());
                cs.getCode(csDomaineCompte);

                classe1.setLibelleDe(cs.getCodeUtilisateur("D").getLibelle());
                classe1.setLibelleFr(cs.getCodeUtilisateur("F").getLibelle());
                classe1.setLibelleIt(cs.getCodeUtilisateur("I").getLibelle());

                classe1.setIdClassification(idClassificationDomaine);
                classe1.setNoClasse(csDomaineCompte);
                classe1.setImprimerTitre(new Boolean(true));
                classe1.setImprimerTotal(new Boolean(true));
                classe1.setImprimerResultat(new Boolean(false));
                classe1.setIdSuperClasse("0");
                classe1.add(transaction);
            } else {
                // 1eme niveau existe
                classe1 = (CGClasseCompte) classeManager.getEntity(0);
            }

            // fin hierarchie

            classe.setIdClassification(idClassificationDomaine);
            classe.setNoClasse(csDomaineCompte + "." + csGenreCompte.substring(3, 6));
            classe.setNumeroOrdre("0");
            classe.setImprimerTitre(new Boolean(true));
            classe.setImprimerTotal(new Boolean(true));
            classe.setImprimerResultat(new Boolean(false));
            classe.setIdSuperClasse(classe1.getIdClasseCompte());
            classe.setSession(getSession());
            classe.add(transaction);

        } else {
            // la classe existe deja
            classe = (CGClasseCompte) classeManager.getEntity(0);
        }

        lien.setSession(getSession());
        lien.setIdClasseCompte(classe.getIdClasseCompte());
        lien.setIdCompte(getIdCompte());
        lien.add(transaction);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 14:17:26)
     */
    public void comptePlanComptableDomaine(BTransaction transaction, CGCompteAVS compteAvs) throws Exception {

        CGLiaisonCompteClasse lien = new CGLiaisonCompteClasse();
        CGClasseCompte classe = null;
        CGClasseCompte classe1 = null; // 1er niveau
        CGClasseCompteManager classeManager = new CGClasseCompteManager();

        classeManager.setForNoClasse(compteAvs.getGenre().substring(1, 4));
        classeManager.find(transaction);
        if (classeManager.size() == 0) {

            // la classe n'existe pas, création
            classe = new CGClasseCompte();
            classe.setNoClasse(compteAvs.getGenre().substring(1, 4));

            // hierachie
            // 1er niveau
            classeManager = new CGClasseCompteManager();
            classeManager.setForNoClasse(getIdDomaine().substring(1, 4));
            classeManager.find(transaction);
            if (classeManager.size() == 0) {

                // la classe n'existe pas, création
                classe1 = new CGClasseCompte();
                classe1.setNoClasse(getIdDomaine().substring(1, 4));
                classe1.setNumeroOrdre("0");
                classe1.setImprimerResultat(new Boolean(true));
                classe1.setImprimerTitre(new Boolean(true));
                classe1.setImprimerTotal(new Boolean(false));
                classe1.setIdSuperClasse("0");
                classe1.setSession(getSession());
                classe1.add(transaction);

            } else {
                classe1 = (CGClasseCompte) classeManager.getEntity(0);
            }

            // fin hierarchie

            classe.setLibelleFr("");
            classe.setLibelleDe("");
            classe.setLibelleIt("");
            classe.setNumeroOrdre("0");
            classe.setImprimerResultat(new Boolean(false));
            classe.setImprimerTotal(new Boolean(true));
            classe.setImprimerTitre(new Boolean(true));
            classe.setIdSuperClasse(classe1.getIdClasseCompte());
            classe.setSession(getSession());
            classe.add(transaction);

        } else {
            // existe deja
            classe = (CGClasseCompte) classeManager.getEntity(0);
        }

        lien.setIdClasseCompte(classe.getIdClasseCompte());
        lien.setIdCompte(getIdCompte());
        lien.setSession(getSession());
        lien.add(transaction);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:54:04)
     * 
     * @return int
     */
    public String getAvoir() {
        return avoir;
    }

    /**
     * Returns the avoirMonnaie.
     * 
     * @return String
     */
    public String getAvoirMonnaie() {
        return avoirMonnaie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:54:18)
     * 
     * @return int
     */
    public String getAvoirProvisoire() {
        return avoirProvisoire;
    }

    /**
     * Returns the avoirProvisoireMonnaie.
     * 
     * @return String
     */
    public String getAvoirProvisoireMonnaie() {
        return avoirProvisoireMonnaie;
    }

    /**
     * Returns the budget.
     * 
     * @return String
     */
    public String getBudget() {
        return budget;
    }

    @Override
    public BManager[] getChilds() {
        CGLiaisonCompteClasse_ClassManager liaisonManager = new CGLiaisonCompteClasse_ClassManager();
        liaisonManager.setForIdCompte(getIdCompte());
        return new BManager[] { liaisonManager };
    }

    public CGCompteAVS getCompteAVS() {
        if (compteAVS == null) {
            compteAVS = new CGCompteAVS(idCompte);
        }
        return compteAVS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:53:39)
     * 
     * @return String
     */
    public String getDoit() {
        return doit;
    }

    /**
     * Returns the doitMonnaie.
     * 
     * @return String
     */
    public String getDoitMonnaie() {
        return doitMonnaie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:53:56)
     * 
     * @return String
     */
    public String getDoitProvisoire() {
        return doitProvisoire;
    }

    /**
     * Returns the doitProvisoireMonnaie.
     * 
     * @return String
     */
    public String getDoitProvisoireMonnaie() {
        return doitProvisoireMonnaie;
    }

    public HashSet<String> getExceptListDomaine() {
        HashSet<String> except = new HashSet<String>();
        except.add(CGCompte.CS_COMPTE_TOUS);
        return except;
    }

    public HashSet<String> getExceptListGenre() {
        HashSet<String> except = new HashSet<String>();
        except.add(CGCompte.CS_GENRE_TOUS);
        return except;
    }

    /**
     * Returns the idCentreCharge.
     * 
     * @return String
     */
    public String getIdCentreCharge() {
        return idCentreCharge;
    }

    public String getIdCompteOfas() {
        if (JadeStringUtil.isBlank(idCompteOfas)) {
            CGCompteCompteOfas compteOfasRelation = new CGCompteCompteOfas();
            compteOfasRelation.setSession(getSession());
            compteOfasRelation.setAlternateKey(CGCompteCompteOfas.AK_IDCOMPTE);
            compteOfasRelation.setIdCompte(getIdCompte());

            try {
                compteOfasRelation.retrieve();

                idCompteOfas = compteOfasRelation.getIdCompteOfas();
            } catch (Exception e) {
                idCompteOfas = new String();
            }
        }

        return idCompteOfas;
    }

    public String getIdExterne() {
        return idExterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 18:21:21)
     * 
     * @return String
     */
    public String getIdSolde() {
        return idSolde;
    }

    @Override
    public String getLibelle() {
        return CGLibelle.getLibelleApp(this);
    }

    @Override
    public String getLibelleDe() {
        return libelleDe;
    }

    @Override
    public String getLibelleFr() {
        return libelleFr;
    }

    @Override
    public String getLibelleIt() {
        return libelleIt;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 15:51:47)
     * 
     * @return String
     */
    public String getSolde() {
        return solde;
    }

    /**
     * Returns the soldeMonnaie.
     * 
     * @return String
     */
    public String getSoldeMonnaie() {
        return soldeMonnaie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 15:51:47)
     * 
     * @return String
     */
    public String getSoldeProvisoire() {
        return soldeProvisoire;
    }

    /**
     * Returns the soldeProvisoireMonnaie.
     * 
     * @return String
     */
    public String getSoldeProvisoireMonnaie() {
        return soldeProvisoireMonnaie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 17:31:27)
     * 
     * @return boolean
     */
    public boolean hasAllLibelleEmpty() {

        return JadeStringUtil.isBlank(libelleDe + libelleFr + libelleIt);
    }

    public Boolean isAReouvrir() {
        return aReouvrir;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2003 17:29:06)
     * 
     * @return boolean
     */
    public boolean isAutoInherit() {
        return autoInherit;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 13:25:11)
     * 
     * @return boolean
     */
    public Boolean isEstCompteAvs() {
        return estCompteAvs;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.12.2002 17:24:49)
     * 
     * @return Boolean
     */
    public Boolean isEstPeriode() {
        return estPeriode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.04.2003 17:02:21)
     * 
     * @return boolean
     */
    public boolean isSoldeAvoir() {
        return (new FWCurrency(getSolde())).isNegative();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.04.2003 17:02:21)
     * 
     * @return boolean
     */
    public boolean isSoldeDoit() {
        return (new FWCurrency(getSolde())).isPositive();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.04.2003 17:02:21)
     * 
     * @return boolean
     */
    public boolean isSoldeProvisoireAvoir() {
        return (new FWCurrency(getSoldeProvisoire())).isNegative();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.04.2003 17:02:21)
     * 
     * @return boolean
     */
    public boolean isSoldeProvisoireDoit() {
        return (new FWCurrency(getSoldeProvisoire())).isPositive();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.04.2003 17:02:21)
     * 
     * @return boolean
     */
    public boolean isSoldeProvisoireZero() {
        return (new FWCurrency(getSoldeProvisoire())).isZero();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.04.2003 17:02:21)
     * 
     * @return boolean
     */
    public boolean isSoldeZero() {
        return (new FWCurrency(getSolde())).isZero();
    }

    private String label(String code) {
        return getSession().getLabel(CGPlanComptableViewBean.LABEL_PREFIXE + code);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.07.2003 16:12:56)
     * 
     * @param compte
     *            globaz.helios.db.comptes.CGCompte
     */
    public CGCompteOfas ouvertureCompteOFAS(CGPlanComptableViewBean plan, BTransaction transaction) throws Exception {
        CGCompteAVS compteAvs = new CGCompteAVS(plan.getIdExterne());

        CGCompteOfas cptOFAS = new CGCompteOfas();

        CGCompteOfasManager cptOFASManager = new CGCompteOfasManager();
        cptOFASManager.setForIdExterne(compteAvs.getCompteOFASFormat());
        cptOFASManager.setForIdMandat(getIdMandat());
        cptOFASManager.setSession(getSession());
        cptOFASManager.find(transaction);

        if (cptOFASManager.size() == 0) {

            cptOFAS = new CGCompteOfas();

            cptOFAS.setIdExterne(compteAvs.getCompteOFASFormat());

            if (compteAvs.isCompteAffilies()) {
                cptOFAS.setIdNature(CGCompteOfas.CS_NATURE_CC_AFFILIES);
            } else {
                cptOFAS.setIdNature(CGCompteOfas.CS_NATURE_STANDARD);
            }
            if (plan.getIdGenre().equals(CGCompte.CS_GENRE_ACTIF)) {
                cptOFAS.setIdGenre(CGCompteOfas.CS_GENRE_ACTIF);
            } else if (plan.getIdGenre().equals(CGCompte.CS_GENRE_CHARGE)) {
                cptOFAS.setIdGenre(CGCompteOfas.CS_GENRE_CHARGE);
            } else if (plan.getIdGenre().equals(CGCompte.CS_GENRE_CLOTURE)) {
                cptOFAS.setIdGenre(CGCompteOfas.CS_GENRE_CLOTURE);
            } else if (plan.getIdGenre().equals(CGCompte.CS_GENRE_OUVERTURE)) {
                cptOFAS.setIdGenre(CGCompteOfas.CS_GENRE_OUVERTURE);
            } else if (plan.getIdGenre().equals(CGCompte.CS_GENRE_PASSIF)) {
                cptOFAS.setIdGenre(CGCompteOfas.CS_GENRE_PASSIF);
            } else if (plan.getIdGenre().equals(CGCompte.CS_GENRE_PRODUIT)) {
                cptOFAS.setIdGenre(CGCompteOfas.CS_GENRE_PRODUIT);
            } else if (plan.getIdGenre().equals(CGCompte.CS_GENRE_RESULTAT)) {
                cptOFAS.setIdGenre(CGCompteOfas.CS_GENRE_RESULTAT);
            }

            if (plan.getIdDomaine().equals(CGCompte.CS_COMPTE_ADMINISTRATION)) {
                cptOFAS.setIdDomaine(CGCompteOfas.CS_DOMAINE_ADMINISTRATION);
            } else if (plan.getIdDomaine().equals(CGCompte.CS_COMPTE_BILAN)) {
                cptOFAS.setIdDomaine(CGCompteOfas.CS_DOMAINE_BILAN);
            } else if (plan.getIdDomaine().equals(CGCompte.CS_COMPTE_EXPLOITATION)) {
                cptOFAS.setIdDomaine(CGCompteOfas.CS_DOMAINE_EXPLOITATION);
            } else if (plan.getIdDomaine().equals(CGCompte.CS_COMPTE_INVESTISSEMENT)) {
                cptOFAS.setIdDomaine(CGCompteOfas.CS_DOMAINE_INVESTISSEMENT);
            }

            cptOFAS.setNonAnnoncable(new Boolean(false));

            CGPlanComptableAVS planAvs = new CGPlanComptableAVS();
            planAvs.setSession(getSession());
            planAvs.setNumeroCompteAVS(compteAvs.getCompte());
            planAvs.retrieve(transaction);
            cptOFAS.setLibelleDe(planAvs.getLibelleDe());
            cptOFAS.setLibelleFr(planAvs.getLibelleFr());
            cptOFAS.setLibelleIt(planAvs.getLibelleIt());
            cptOFAS.setSession(getSession());
            cptOFAS.setIdMandat(getIdMandat());

            cptOFAS.add(transaction);
            // System.out.println("Compte Ofas "+cptOFAS.getIdExterne()+" créé.");
        } else if (cptOFASManager.size() == 1) {
            cptOFAS = (CGCompteOfas) cptOFASManager.getEntity(0);
            // System.out.println("Compte Ofas "+cptOFAS.getIdExterne()+" déjà existant.");
        } else {
            throw new Exception(label("MORE_COMPTE_OFAS"));
        }

        return cptOFAS;

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.07.2003 16:12:56)
     * 
     * @param compte
     *            globaz.helios.db.comptes.CGCompte
     */
    public void ouvertureCompteOFASFictif(CGCompteOfas compteOfas, CGPlanComptableViewBean plan,
            BTransaction transaction) throws Exception {
        int secteur = new Integer(compteOfas.getIdExterne().substring(0, 3)).intValue();
        int classe = new Integer(compteOfas.getIdExterne().substring(4, 5)).intValue();

        String idExterneFictif = "";

        switch (classe) {
            case 5:
                if ((secteur == 380) || (secteur == 381) || (secteur == 382) || (secteur == 383) || (secteur == 910)
                        || (secteur == 920) || (secteur == 930) || (secteur == 940)
                        || ((secteur >= 400) && (secteur < 900))) {
                    idExterneFictif = String.valueOf(secteur) + ".5999";
                }
                break;
            case 6:
                if ((secteur == 380) || (secteur == 381) || (secteur == 382) || (secteur == 383) || (secteur == 920)
                        || (secteur == 930) || (secteur == 940) || ((secteur >= 400) && (secteur < 900))) {
                    idExterneFictif = String.valueOf(secteur) + ".6999";
                }
                break;
            case 7:
                if ((secteur == 950) || ((secteur >= 400) && (secteur < 900))) {
                    idExterneFictif = String.valueOf(secteur) + ".7999";
                }
                break;
            case 8:
                if ((secteur == 950) || ((secteur >= 400) && (secteur < 900))) {
                    idExterneFictif = String.valueOf(secteur) + ".8999";
                }
                break;
        }

        if (!"".equals(idExterneFictif)) {
            CGCompteOfasManager manager = new CGCompteOfasManager();
            manager.setSession(getSession());
            manager.setForIdMandat(getIdMandat());
            manager.setForIdExterne(idExterneFictif);
            manager.find(transaction);
            if (manager.size() == 0) {

                CGCompteOfas cptOFAS = new CGCompteOfas();

                cptOFAS.setIdExterne(idExterneFictif);
                cptOFAS.setIdNature(CGCompteOfas.CS_NATURE_FICTIF);

                if (plan.getIdGenre().equals(CGCompte.CS_GENRE_ACTIF)) {
                    cptOFAS.setIdGenre(CGCompteOfas.CS_GENRE_ACTIF);
                } else if (plan.getIdGenre().equals(CGCompte.CS_GENRE_CHARGE)) {
                    cptOFAS.setIdGenre(CGCompteOfas.CS_GENRE_CHARGE);
                } else if (plan.getIdGenre().equals(CGCompte.CS_GENRE_CLOTURE)) {
                    cptOFAS.setIdGenre(CGCompteOfas.CS_GENRE_CLOTURE);
                } else if (plan.getIdGenre().equals(CGCompte.CS_GENRE_OUVERTURE)) {
                    cptOFAS.setIdGenre(CGCompteOfas.CS_GENRE_OUVERTURE);
                } else if (plan.getIdGenre().equals(CGCompte.CS_GENRE_PASSIF)) {
                    cptOFAS.setIdGenre(CGCompteOfas.CS_GENRE_PASSIF);
                } else if (plan.getIdGenre().equals(CGCompte.CS_GENRE_PRODUIT)) {
                    cptOFAS.setIdGenre(CGCompteOfas.CS_GENRE_PRODUIT);
                } else if (plan.getIdGenre().equals(CGCompte.CS_GENRE_RESULTAT)) {
                    cptOFAS.setIdGenre(CGCompteOfas.CS_GENRE_RESULTAT);
                }

                if (plan.getIdDomaine().equals(CGCompte.CS_COMPTE_ADMINISTRATION)) {
                    cptOFAS.setIdDomaine(CGCompteOfas.CS_DOMAINE_ADMINISTRATION);
                } else if (plan.getIdDomaine().equals(CGCompte.CS_COMPTE_BILAN)) {
                    cptOFAS.setIdDomaine(CGCompteOfas.CS_DOMAINE_BILAN);
                } else if (plan.getIdDomaine().equals(CGCompte.CS_COMPTE_EXPLOITATION)) {
                    cptOFAS.setIdDomaine(CGCompteOfas.CS_DOMAINE_EXPLOITATION);
                } else if (plan.getIdDomaine().equals(CGCompte.CS_COMPTE_INVESTISSEMENT)) {
                    cptOFAS.setIdDomaine(CGCompteOfas.CS_DOMAINE_INVESTISSEMENT);
                }

                cptOFAS.setNonAnnoncable(new Boolean(false));
                if (secteur == 5) {
                    cptOFAS.setLibelleFr("Charges administratives");
                    cptOFAS.setLibelleDe("Verwaltungaufwand");
                    cptOFAS.setLibelleIt("Carichi amministrativi");
                } else if (secteur == 6) {
                    cptOFAS.setLibelleFr("Produits administratifs");
                    cptOFAS.setLibelleDe("Verwaltungsprodukte");
                    cptOFAS.setLibelleIt("Prodotti amministrativi");
                } else if (secteur == 7) {
                    cptOFAS.setLibelleFr("Dépenses d'investissement");
                    cptOFAS.setLibelleDe("Investitionsausgaben");
                    cptOFAS.setLibelleIt("Spese d'investimento");
                } else if (secteur == 8) {
                    cptOFAS.setLibelleFr("Produits d'investissement");
                    cptOFAS.setLibelleDe("Investitionsprodukte");
                    cptOFAS.setLibelleIt("Proditti d'investimento");
                }

                cptOFAS.setSession(getSession());
                cptOFAS.setIdMandat(getIdMandat());

                cptOFAS.add(transaction);
                JadeLogger.info(this, "Compte Ofas Fictif " + cptOFAS.getIdExterne() + " créé.");

            }
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.07.2003 16:12:56)
     * 
     * @param compte
     *            globaz.helios.db.comptes.CGCompte
     */
    public void ouvertureLiaisonCompteCompteOFAS(CGPlanComptableViewBean plan, CGCompteOfas compteOfas,
            BTransaction transaction) throws Exception {

        CGCompteCompteOfasManager liaisonManager = new CGCompteCompteOfasManager();
        liaisonManager.setForIdCompte(plan.getIdCompte());
        liaisonManager.setForIdCompteOfas(compteOfas.getIdCompteOfas());
        liaisonManager.setSession(getSession());
        liaisonManager.find(transaction);

        if (liaisonManager.size() == 0) {
            CGCompteCompteOfas liaison = new CGCompteCompteOfas();
            liaison.setSession(getSession());
            liaison.setIdCompte(plan.getIdCompte());
            liaison.setIdCompteOfas(compteOfas.getIdCompteOfas());
            liaison.add(transaction);
            JadeLogger
                    .info(this, "Liaison compte Ofas " + compteOfas.getIdExterne() + " - compte " + plan.getIdExterne()
                            + " créée.");
        } else if (liaisonManager.size() == 1) {
            JadeLogger.info(this,
                    "Liaison compte Ofas " + compteOfas.getIdExterne() + " - compte " + plan.getIdExterne()
                            + " déjà existante.");
        } else {
            throw new Exception(label("MORE_LIAISON"));
        }

    }

    public void setAReouvrir(Boolean newAReouvrir) {
        aReouvrir = newAReouvrir;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:54:04)
     * 
     * @param newAvoir
     *            int
     */
    public void setAvoir(String newAvoir) {
        avoir = newAvoir;
    }

    /**
     * Sets the avoirMonnaie.
     * 
     * @param avoirMonnaie
     *            The avoirMonnaie to set
     */
    public void setAvoirMonnaie(String avoirMonnaie) {
        this.avoirMonnaie = avoirMonnaie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:54:18)
     * 
     * @param newAvoirProvisoire
     *            int
     */
    public void setAvoirProvisoire(String newAvoirProvisoire) {
        avoirProvisoire = newAvoirProvisoire;
    }

    /**
     * Sets the avoirProvisoireMonnaie.
     * 
     * @param avoirProvisoireMonnaie
     *            The avoirProvisoireMonnaie to set
     */
    public void setAvoirProvisoireMonnaie(String avoirProvisoireMonnaie) {
        this.avoirProvisoireMonnaie = avoirProvisoireMonnaie;
    }

    /**
     * Sets the budget.
     * 
     * @param budget
     *            The budget to set
     */
    public void setBudget(String budget) {
        this.budget = budget;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:53:39)
     * 
     * @param newDoit
     *            String
     */
    public void setDoit(String newDoit) {
        doit = newDoit;
    }

    /**
     * Sets the doitMonnaie.
     * 
     * @param doitMonnaie
     *            The doitMonnaie to set
     */
    public void setDoitMonnaie(String doitMonnaie) {
        this.doitMonnaie = doitMonnaie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:53:56)
     * 
     * @param newDoitProvisoire
     *            String
     */
    public void setDoitProvisoire(String newDoitProvisoire) {
        doitProvisoire = newDoitProvisoire;
    }

    /**
     * Sets the doitProvisoireMonnaie.
     * 
     * @param doitProvisoireMonnaie
     *            The doitProvisoireMonnaie to set
     */
    public void setDoitProvisoireMonnaie(String doitProvisoireMonnaie) {
        this.doitProvisoireMonnaie = doitProvisoireMonnaie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 13:25:11)
     * 
     * @param newEstCompteAvs
     *            boolean
     */
    public void setEstCompteAvs(boolean newEstCompteAvs) {
        estCompteAvs = new Boolean(newEstCompteAvs);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.12.2002 17:24:49)
     * 
     * @param newEstPeriode
     *            Boolean
     */
    public void setEstPeriode(Boolean newEstPeriode) {
        estPeriode = newEstPeriode;
    }

    /**
     * Sets the idCentreCharge.
     * 
     * @param idCentreCharge
     *            The idCentreCharge to set
     */
    public void setIdCentreCharge(String idCentreCharge) {
        this.idCentreCharge = idCentreCharge;
    }

    public void setIdExterne(String newIdExterne) {
        idExterne = newIdExterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 18:21:21)
     * 
     * @param newIdSolde
     *            String
     */
    public void setIdSolde(String newIdSolde) {
        idSolde = newIdSolde;
    }

    public void setLibelleDe(String newLibelleDe) {
        libelleDe = newLibelleDe;
    }

    public void setLibelleFr(String newLibelleFr) {
        libelleFr = newLibelleFr;
    }

    public void setLibelleIt(String newLibelleIt) {
        libelleIt = newLibelleIt;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 15:51:02)
     * 
     * @param newSolde
     *            String
     */
    public void setSolde(String newSolde) {
        solde = newSolde;
    }

    /**
     * Sets the soldeMonnaie.
     * 
     * @param soldeMonnaie
     *            The soldeMonnaie to set
     */
    public void setSoldeMonnaie(String soldeMonnaie) {
        this.soldeMonnaie = soldeMonnaie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 15:51:47)
     * 
     * @param newSoldeProvisoir
     *            String
     */
    public void setSoldeProvisoire(String newSoldeProvisoire) {
        soldeProvisoire = newSoldeProvisoire;
    }

    /**
     * Sets the soldeProvisoireMonnaie.
     * 
     * @param soldeProvisoireMonnaie
     *            The soldeProvisoireMonnaie to set
     */
    public void setSoldeProvisoireMonnaie(String soldeProvisoireMonnaie) {
        this.soldeProvisoireMonnaie = soldeProvisoireMonnaie;
    }

    /**
     * @see globaz.globall.db.BEntity#toString()
     */
    public String toMyString() {
        return "[" + getIdExterne() + "]" + getLibelleFr() + "\n";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2003 17:29:06)
     * 
     * @param newAutoInherit
     *            boolean
     */
    public void wantAutoInherit(boolean newAutoInherit) {
        autoInherit = newAutoInherit;
    }

}
