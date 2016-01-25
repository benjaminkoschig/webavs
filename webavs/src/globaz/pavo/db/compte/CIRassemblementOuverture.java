package globaz.pavo.db.compte;

import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.pavo.application.CIApplication;
import globaz.pavo.util.CIUtil;
import globaz.pyxis.api.ITIAdministration;
import java.util.ArrayList;

/**
 * Object représentant le rassemblement et l'ouverture d'un compte individuel. Date de création : (12.11.2002 13:17:39)
 * 
 * @author: David Girardin
 */
public class CIRassemblementOuverture extends BEntity {

    private static final long serialVersionUID = 6703369755506994956L;
    public final static String CS_CI_ADDITIONNEL = "318006";
    public final static String CS_CI_ADDITIONNEL_SUSPENS = "318008";
    // Type d''enregistrement
    public final static String CS_CLOTURE = "318000";
    public final static String CS_CLOTURE_OUVERTURE = "318001";
    public final static String CS_CLOTURE_REENVOI = "";
    public final static String CS_EXTRAIT = "318004";
    public final static String CS_OUVERTURE = "318005";
    public final static String CS_RASSEMBLEMENT = "318003";
    public final static String CS_RASSEMBLEMENT_REENVOI = "";
    public final static String CS_SAISIE_MANUELLE = "318002";
    public final static String CS_SPLITTING = "318007";
    public final static String CS_SPLITTING_REENVOI = "";

    private String caisseAgence = new String();
    private String caisseAgenceNom = new String();
    /** (KKICCO) */
    private String caisseCommettante = new String();
    /** (KKDSRC) */
    private String caisseTenantCI = new String();
    private CICompteIndividuel ci = null;
    /** (KAIIND) */
    private String compteIndividuelId = new String();
    /** (KKDCLO) */
    private String dateCloture = new String();
    /** (KKDORD) */
    private String dateOrdre = new String();
    /** (KKDREV) */
    private String dateRevocation = new String();
    private String dateSecondRci = new String();
    private String isCiAdditionnel = "False";
    private String isEcran = "False";
    /** (KKTARC) */
    private String motifArc = new String();
    /** (KKIRAO) */
    private String rassemblementOuvertureId = new String();
    /** (KKTCTE */
    private String realCaisse = new String();
    /** (KKLREF) */
    private String referenceInterne = new String();

    /** (KKTENR) */
    private String typeEnregistrement = new String();

    // code systeme
    /**
     * Commentaire relatif au constructeur CIRassemblementOuverture
     */
    public CIRassemblementOuverture() {
        super();
        setTypeEnregistrement(CIRassemblementOuverture.CS_SAISIE_MANUELLE);
    }

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws java.lang.Exception {
        // no caisse
        if (!JAUtil.isIntegerEmpty(getCaisseCommettante())) {
            if (CIApplication.DEFAULT_APPLICATION_PAVO.equalsIgnoreCase(getSession().getApplicationId())) {
                CIApplication application = (CIApplication) getSession().getApplication();
                if (isLoadedFromManager()) {
                    caisseAgence = application.getAdministration(getSession(), getCaisseCommettante(),
                            new String[] { "getCodeAdministration" }).getCodeAdministration();
                } else {
                    ITIAdministration admin = application.getAdministration(getSession(), getCaisseCommettante(),
                            new String[] { "getCodeAdministration", "getNom" });
                    caisseAgence = admin.getCodeAdministration();
                    caisseAgenceNom = admin.getNom();
                }
            }
        }
    }

    /**
     * Reset de l'id si entity en erreur
     */
    @Override
    protected void _alwaysAfterAdd(BTransaction transaction) throws java.lang.Exception {
        if (transaction.hasErrors()) {
            setRassemblementOuvertureId("");
        }
    }

    /**
     * Effectue des traitements avant un ajout dans la BD
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        if (JAUtil.isIntegerEmpty(rassemblementOuvertureId)) {
            setRassemblementOuvertureId(this._incCounter(transaction, "0"));
        }
    }

    /**
     * Teste si la modification est autorisée.
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // modif possible pour les anciens clients et CS, car les clôtures ne
        // sont pas complètes
        if (!CIUtil.isCIAddionelNonConforme(getSession())) {
            if (!CIRassemblementOuverture.CS_SAISIE_MANUELLE.equals(getTypeEnregistrement())
                    && !CIRassemblementOuverture.CS_EXTRAIT.equals(getTypeEnregistrement())) {
                // modification impossible
                _addError(transaction, getSession().getLabel("MSG_RASSEMB_MOD_ETAT"));
            }
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CIRAOUP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        rassemblementOuvertureId = statement.dbReadNumeric("KKIRAO");
        compteIndividuelId = statement.dbReadNumeric("KAIIND");
        motifArc = statement.dbReadNumeric("KKTARC");
        dateCloture = statement.dbReadDateAMJ("KKDCLO");
        caisseCommettante = statement.dbReadNumeric("KKICCO");
        referenceInterne = statement.dbReadString("KKLREF");
        dateOrdre = statement.dbReadDateAMJ("KKDORD");
        dateRevocation = statement.dbReadDateAMJ("KKDREV");
        typeEnregistrement = statement.dbReadNumeric("KKTENR");
        dateSecondRci = statement.dbReadDateAMJ("KKDSRC");
        CIApplication app = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                CIApplication.DEFAULT_APPLICATION_PAVO);
        if (!app.isAnnoncesWA()) {
            caisseTenantCI = statement.dbReadNumeric("KKTCTE");
            realCaisse = statement.dbReadNumeric("KKCTCI");
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {
        // caisse / agence
        if (JAUtil.isIntegerEmpty(caisseCommettante)) {
            if (JAUtil.isStringEmpty(caisseAgence)) {
                if (!CIUtil.isCIAddionelNonConforme(getSession()) || !("True".equals(getIsCiAdditionnel()))) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_RASSEMB_VAL_ADMIN"));
                } else if ("True".equals(isEcran)) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_RASSEMB_VAL_ADMIN"));
                }
            } else {
                if (caisseAgence.trim().length() <= 7) {
                    // recherche de l'id tiers
                    int sep = caisseAgence.indexOf('.');
                    String caisse = null;
                    String agence = null;
                    if (sep > 0) {
                        caisse = caisseAgence.substring(0, sep);
                        agence = caisseAgence.substring(sep + 1);
                    } else {
                        caisse = caisseAgence;
                    }
                    try {
                        CIApplication application = (CIApplication) getSession().getApplication();
                        caisseCommettante = application.getAdministration(getSession(), caisse, agence,
                                new String[] { "getIdTiersAdministration" }).getIdTiersAdministration();
                    } catch (Exception ex) {
                        // impossible d'appliquer le changement
                    }
                    if ((caisseCommettante == null) || (caisseCommettante.length() == 0)) {
                        if (!CIUtil.isCIAddionelNonConforme(getSession())) {
                            _addError(statement.getTransaction(), getSession().getLabel("MSG_RASSEMB_VAL_ADMIN"));
                        } else if ("True".equals(isEcran)) {
                            _addError(statement.getTransaction(), getSession().getLabel("MSG_RASSEMB_VAL_ADMIN"));
                        }
                    }
                }
            }
        }
        // motif
        if (JAUtil.isStringEmpty(motifArc)) {
            if (!CIUtil.isCIAddionelNonConforme(getSession())) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_RASSEMB_VAL_MOTIF"));
            } else if ("True".equals(isEcran)) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_RASSEMB_VAL_MOTIF"));
            }
        }
        // Dates
        _checkDate(statement.getTransaction(), dateCloture, getSession().getLabel("MSG_RASSEMB_VAL_CLOTURE"));
        _checkDate(statement.getTransaction(), dateOrdre, getSession().getLabel("MSG_RASSEMB_VAL_ORDRE"));
        if (!JAUtil.isStringEmpty(dateRevocation)) {
            _checkDate(statement.getTransaction(), dateRevocation, getSession().getLabel("MSG_RASSEMB_VAL_REVOC"));
        }
        if (!JAUtil.isStringEmpty(dateSecondRci)) {
            _checkDate(statement.getTransaction(), dateSecondRci, getSession().getLabel("MSG_RASSEMB_VAL_RCI"));
        }
        if (!statement.getTransaction().hasErrors()) {
            // périodes de dates
            try {
                // date de l'ordre
                if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), "01.01.1948", dateOrdre)) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_RASSEMB_VAL_ORDREP"));
                }
                if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateOrdre, JACalendar.todayJJsMMsAAAA())) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_RASSEMB_VAL_ORDREG"));
                }
                // date de la révocation
                if (!JAUtil.isStringEmpty(dateRevocation)) {
                    if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), "01.01.1948", dateRevocation)) {
                        _addError(statement.getTransaction(), getSession().getLabel("MSG_RASSEMB_VAL_ORDREP"));
                    }
                    if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateRevocation,
                            JACalendar.todayJJsMMsAAAA())) {
                        _addError(statement.getTransaction(), getSession().getLabel("MSG_RASSEMB_VAL_ORDREG"));
                    }
                }
            } catch (Exception e) {
                _addError(statement.getTransaction(), e.getMessage());
            }
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("KKIRAO",
                this._dbWriteNumeric(statement.getTransaction(), getRassemblementOuvertureId(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("KKIRAO", this._dbWriteNumeric(statement.getTransaction(), getRassemblementOuvertureId(),
                "rassemblementOuvertureId"));
        statement.writeField("KAIIND",
                this._dbWriteNumeric(statement.getTransaction(), getCompteIndividuelId(), "compteIndividuelId"));
        statement.writeField("KKTARC", this._dbWriteNumeric(statement.getTransaction(), getMotifArc(), "motifArc"));
        statement.writeField("KKDCLO",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateCloture(), "dateCloture"));
        statement.writeField("KKICCO",
                this._dbWriteNumeric(statement.getTransaction(), getCaisseCommettante(), "caisseCommettante"));
        statement.writeField("KKLREF",
                this._dbWriteString(statement.getTransaction(), getReferenceInterne(), "referenceInterne"));
        statement.writeField("KKDORD", this._dbWriteDateAMJ(statement.getTransaction(), getDateOrdre(), "dateOrdre"));
        statement.writeField("KKDREV",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateRevocation(), "dateRevocation"));
        statement.writeField("KKTENR",
                this._dbWriteNumeric(statement.getTransaction(), getTypeEnregistrement(), "typeEnregistrement"));
        statement.writeField("KKDSRC",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateSecondRci(), "dateSecondRci"));
        CIApplication app = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                CIApplication.DEFAULT_APPLICATION_PAVO);
        if (!app.isAnnoncesWA()) {
            statement.writeField("KKTCTE",
                    this._dbWriteNumeric(statement.getTransaction(), getCaisseTenantCI(), "caisseTenantCI"));
            statement.writeField("KKCTCI",
                    this._dbWriteNumeric(statement.getTransaction(), getRealCaisse(), "realCaisseTenantCI"));
        }
    }

    /**
     * Créer un ci additionnel par rapport à cette clôture.
     * 
     * @param transaction
     *            la transaction à utiliser
     * @param compte
     *            le CI concerné
     * @return le nombre d'écritures qui sont concernées par cette clôture.
     * @exception si
     *                des erreurs surviennent Date de création : (06.01.2003 08:41:18)
     */
    public ArrayList addCIAdditionnel(BTransaction transaction, ArrayList listEcritures, boolean testOnly)
            throws Exception {
        // date
        JADate dateCloture = new JADate(getDateCloture());
        // resultat
        ArrayList ecritures;
        if (listEcritures != null) {
            ecritures = listEcritures;
        } else {
            ecritures = new ArrayList();
            // chercher écritures concernée
            CIEcritureManager ecrituresMgr = new CIEcritureManager();
            ecrituresMgr.setSession(getSession());
            ecrituresMgr.setForCompteIndividuelId(getCompteIndividuelId());
            ecrituresMgr.setForRassemblementOuvertureId("0");
            ecrituresMgr.setForIdTypeCompte(CIEcriture.CS_CI);
            ecrituresMgr.orderByAnnee();
            ecrituresMgr.find(transaction);
            for (int i = 0; i < ecrituresMgr.size(); i++) {
                ecritures.add(ecrituresMgr.getEntity(i));
            }
        }
        ArrayList result = new ArrayList();
        for (int i = 0; i < ecritures.size(); i++) {
            // l'écriture est-elle concernée (+ ev. éclatement)?
            CIEcriture ecriture = ((CIEcriture) ecritures.get(i)).aCloturer(transaction, dateCloture, true);
            if (ecriture != null) {
                // l'écriture est à clôturer + ci additionnel
                result.add(ecriture);
            }
        }
        if (!testOnly && (result.size() != 0)) {
            // si écritures touvées -> créer un ci additionel
            CIRassemblementOuverture ciAdditionnel = null;
            CIRassemblementOuvertureManager ciAddMgr = new CIRassemblementOuvertureManager();
            ciAddMgr.setSession(getSession());
            ciAddMgr.setForCompteIndividuelId(getCompteIndividuelId());
            ciAddMgr.setForDateOrdre(JACalendar.todayJJsMMsAAAA());
            ciAddMgr.find(transaction);
            for (int j = 0; j < ciAddMgr.size(); j++) {
                CIRassemblementOuverture ciAdd = (CIRassemblementOuverture) ciAddMgr.getEntity(j);
                if (CIRassemblementOuverture.CS_CI_ADDITIONNEL_SUSPENS.equals(ciAdd.getTypeEnregistrementWA())) {
                    // entrée existe déjà
                    ciAdditionnel = ciAdd;
                }
            }
            if (ciAdditionnel == null) {
                ciAdditionnel = new CIRassemblementOuverture();
                ciAdditionnel.setSession(getSession());
                ciAdditionnel.setDateOrdre(JACalendar.todayJJsMMsAAAA());
                CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                        CIApplication.DEFAULT_APPLICATION_PAVO);
                if (application.isAnnoncesWA()) {
                    ciAdditionnel.setTypeEnregistrement(getTypeEnregistrement().substring(0, 4)
                            + CIRassemblementOuverture.CS_CI_ADDITIONNEL_SUSPENS.substring(4));
                } else {
                    ciAdditionnel.setTypeEnregistrement(CIRassemblementOuverture.CS_CI_ADDITIONNEL_SUSPENS);
                    // Pour les caisses fusionnées, on répond sous la principale
                    if (application.isCaisseFusion()) {
                        ciAdditionnel.setCaisseTenantCI(application.getProperty(CIApplication.CODE_AGENCE));
                    } else {
                        ciAdditionnel.setCaisseTenantCI(getCaisseTenantCI());
                    }
                }
                ciAdditionnel.setDateCloture(getDateCloture());
                ciAdditionnel.setMotifArc(getMotifArc());
                ciAdditionnel.setCaisseCommettante(getCaisseCommettante());
                ciAdditionnel.setReferenceInterne(getReferenceInterne());
                ciAdditionnel.setCompteIndividuelId(getCompteIndividuelId());
                ciAdditionnel.setIsCiAdditionnel("True");
                ciAdditionnel.add(transaction);
            }
            // deuxième boucle pour la modification des écritures
            for (int i = 0; i < result.size(); i++) {
                CIEcriture ecriture = (CIEcriture) result.get(i);
                ecriture.setRassemblementOuvertureId(ciAdditionnel.getRassemblementOuvertureId());
                ecriture.setDateCiAdditionnel(JACalendar.todayJJsMMsAAAA());
                if (!ecriture.isUpdating()) {
                    // si écriture nouvelle, l'ajout se fera plus tard
                    ecriture.simpleUpdate(transaction);
                }
                // créer entrée table lien
                CIEcritureAnnonce lien = new CIEcritureAnnonce();
                lien.setSession(getSession());
                lien.setIdEcritureAssure(ecriture.getEcritureId());
                lien.setIdRassemblement(ciAdditionnel.getRassemblementOuvertureId());
                lien.add(transaction);
            }
        }
        return result;
    }

    public void annonceEcritures(BTransaction transaction, BISession rSession, BITransaction rTransaction)
            throws Exception {
        this.annonceEcritures(transaction, rSession, rTransaction, null);
    }

    /**
     * Annonce le CI depuis cette annonce
     */
    public void annonceEcritures(BTransaction transaction, BISession rSession, BITransaction rTransaction, String assure)
            throws Exception {

        CICompteIndividuel ci = new CICompteIndividuel();
        ci.setSession(getSession());
        ci.setCompteIndividuelId(getCompteIndividuelId());
        ci.retrieve(transaction);

        if (!ci.isNew() && !transaction.hasErrors()) {
            // if(!transaction.hasErrors()) {
            CIAnnonceCIAdditionnel ciAdd = new CIAnnonceCIAdditionnel(ci);
            ciAdd.initAnnonce(rSession, rTransaction);
            ciAdd.annonceCI(this, transaction, assure);
        }
    }

    /**
     * Annonce le CI depuis cette annonce
     * 
     * @param assure
     *            True pour indiqué que le ci de l'assurà doit être annoncé, False pour celui de son conjoint. Laisser à
     *            null si inaplicable.
     */
    public void annonceEcritures(BTransaction transaction, String assure) throws Exception {

        BITransaction remoteTransaction = null;
        try {
            BISession remoteSession = ((CIApplication) getSession().getApplication()).getSessionAnnonce(getSession());
            remoteTransaction = ((BSession) remoteSession).newTransaction();
            remoteTransaction.openTransaction();

            this.annonceEcritures(transaction, remoteSession, remoteTransaction, assure);
            if (remoteTransaction.isRollbackOnly() || transaction.isRollbackOnly()) {
                remoteTransaction.rollback();
            } else {
                remoteTransaction.commit();
            }
        } finally {
            if (remoteTransaction != null) {
                remoteTransaction.closeTransaction();
            }
        }

    }

    // test si le CI peut être annonce à l'aide de cette annonce
    public boolean canAnnonceCI() {
        // if(CIUtil.isSpecialist(getSession())) {
        if (CIRassemblementOuverture.CS_CLOTURE.equals(getTypeEnregistrementWA())
                || CIRassemblementOuverture.CS_RASSEMBLEMENT.equals(getTypeEnregistrementWA())
                || CIRassemblementOuverture.CS_CI_ADDITIONNEL.equals(getTypeEnregistrementWA())
                || CIRassemblementOuverture.CS_CI_ADDITIONNEL_SUSPENS.equals(getTypeEnregistrementWA())
                || CIRassemblementOuverture.CS_SPLITTING.equals(getTypeEnregistrementWA())) {
            return true;
        }
        // }
        return false;
    }

    /**
     * Clôture les écritues du CI spécifié.
     * 
     * @param transaction
     *            la transaction à utiliser
     * @param compte
     *            le CI concerné
     * @return le nombre d'écritures qui sont concernées par cette clôture.
     * @exception si
     *                des erreurs surviennent Date de création : (06.01.2003 08:41:18)
     */
    public ArrayList cloturerEcritures(BTransaction transaction) throws Exception {
        // date
        JADate dateCloture = new JADate(getDateCloture());
        // resultat
        ArrayList result = new ArrayList();
        // chercher écritures concernées
        CIEcritureManager ecritures = new CIEcritureManager();
        ecritures.setSession(getSession());
        ecritures.setForCompteIndividuelId(getCompteIndividuelId());
        ecritures.setForRassemblementOuvertureId("0");
        ecritures.setForIdTypeCompte(CIEcriture.CS_CI);
        ecritures.changeManagerSize(BManager.SIZE_NOLIMIT);
        ecritures.orderByAnnee();
        ecritures.find(transaction);
        // int count = 0;
        for (int i = 0; i < ecritures.size(); i++) {
            // l'écriture est-elle concernée (+ ev. éclatement)?
            CIEcriture ecritureCI = (CIEcriture) ecritures.getEntity(i);
            CIEcriture ecriture = ecritureCI.aCloturer(transaction, dateCloture, true);
            if (ecriture != null) {
                // l'écriture est à clôturer
                // count++;
                result.add(ecriture);
                ecriture.setRassemblementOuvertureId(getRassemblementOuvertureId());
                ecriture.simpleUpdate(transaction);
                // créer entrée table lien
                CIEcritureAnnonce lien = new CIEcritureAnnonce();
                lien.setSession(getSession());
                lien.setIdEcritureAssure(ecriture.getEcritureId());
                lien.setIdRassemblement(getRassemblementOuvertureId());
                lien.add(transaction);
            }
            if ((ecriture == null) || (ecriture != ecritureCI)) {
                // écriture aprés clôture ou écriture éclatée. Devient en
                // suspens
                // ecritureCI.setIdTypeCompte(ecritureCI.CS_CI_SUSPENS);
                // maj
                ecritureCI.simpleUpdate(transaction);
                ecritureCI.getWrapperUtil().reouvreCI(transaction);
            }
        }
        CIEcritureManager tempMgr = new CIEcritureManager();
        tempMgr.setSession(getSession());
        tempMgr.setForCompteIndividuelId(getCompteIndividuelId());
        tempMgr.setForIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
        tempMgr.changeManagerSize(BManager.SIZE_NOLIMIT);
        tempMgr.orderByAnnee();
        tempMgr.find(transaction);

        for (int i = 0; i < tempMgr.size(); i++) {

            CIEcriture ecr = (CIEcriture) tempMgr.getEntity(i);
            // ecr.setSession(getSession());
            if (ecr.apresCloture(dateCloture)) {
                ecr.setAncienTypeCompteTemp(CIEcriture.CS_TEMPORAIRE_SUSPENS);
                ecr.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE_SUSPENS);
                ecr.simpleUpdate(transaction);
                // ecr.getWrapperUtil().reouvreCI(transaction);
            }

        }
        return result;
    }

    public String getCaisseAgenceCommettante() {
        return caisseAgence;
    }

    public String getCaisseCommettante() {
        return caisseCommettante;
    }

    /**
     * @return
     */
    public String getCaisseTenantCI() {
        return caisseTenantCI;
    }

    /**
     * Renvoie le numéro d'agence qui tient le ci
     * */
    public String getCaisseTenantCIWA() {
        if ((typeEnregistrement != null) && (typeEnregistrement.length() == 6)) {
            String agence = typeEnregistrement.substring(3, 4);
            if (!JAUtil.isIntegerEmpty(agence)) {
                try {
                    CIApplication app = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                            CIApplication.DEFAULT_APPLICATION_PAVO);
                    if (app.isAnnoncesWA()) {
                        return app.getProperty(CIApplication.CODE_CAISSE) + "." + typeEnregistrement.substring(3, 4);
                    } else {
                        String caisseRetour = getCaisseTenantCI();
                        if (caisseRetour == null) {
                            caisseRetour = "0";
                        }
                        return app.getProperty(CIApplication.CODE_CAISSE) + "." + getCaisseTenantCI();
                    }

                } catch (Exception e) {
                    return "";
                }
            }
        }
        return "";
    }

    public CICompteIndividuel getCi() {
        if (null == ci) {
            ci = new CICompteIndividuel();
            ci.setSession(getSession());
            ci.setCompteIndividuelId(getCompteIndividuelId());
            try {
                ci.retrieve();
            } catch (Exception e) {
            }

        }
        return ci;
    }

    public String getCompteIndividuelId() {
        return compteIndividuelId;
    }

    public String getDateCloture() {
        return dateCloture;
    }

    public String getDateClotureFormat() {
        if (dateCloture.length() == 10) {
            return dateCloture.substring(3);
        }
        return dateCloture;
    }

    public String getDateOrdre() {
        return dateOrdre;
    }

    public String getDateRevocation() {
        return dateRevocation;
    }

    public String getDateSecondRci() {
        return dateSecondRci;
    }

    /**
     * @return
     */
    public String getIsCiAdditionnel() {
        return isCiAdditionnel;
    }

    /**
     * @return
     */
    public String getIsEcran() {
        return isEcran;
    }

    public String getMotifArc() {
        return motifArc;
    }

    public String getNomCaisseCommettante() {
        return caisseAgenceNom;
    }

    // retourne le nom de l'assuré, pour éviter de passer par la FK dans le
    // détail d'un rao
    public String getNomPrenom() {
        CICompteIndividuel ci = new CICompteIndividuel();
        ci.setSession(getSession());
        ci.setCompteIndividuelId(getCompteIndividuelId());
        try {
            ci.retrieve();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ci.getNomPrenom();
    }

    // retourne le numéro AVS, pour éviter de passer par la FK dans le détail
    // d'un rao
    public String getNumeroAvs() {
        CICompteIndividuel ci = new CICompteIndividuel();
        ci.setSession(getSession());
        ci.setCompteIndividuelId(getCompteIndividuelId());
        try {
            ci.retrieve();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ci.getNumeroAvs();
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getRassemblementOuvertureId() {
        return rassemblementOuvertureId;
    }

    /**
     * @return the realCaisse
     */
    public String getRealCaisse() {
        return realCaisse;
    }

    public String getReferenceInterne() {
        return referenceInterne;
    }

    public String getTypeEnregistrement() {
        return typeEnregistrement;
    }

    /**
     * Renvoie le type d'enregistrement en mode WA de la caisse teant le CI
     * */
    public String getTypeEnregistrementWA() {
        try {
            CIApplication app = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            if ((typeEnregistrement != null) && (typeEnregistrement.length() == 6)) {
                if (app.isAnnoncesWA()) {
                    return typeEnregistrement.substring(0, 3) + "0" + typeEnregistrement.substring(4);
                }
            }
        } catch (Exception e) {
            return typeEnregistrement;
        }
        return typeEnregistrement;
    }

    public String giveFormatCaisseFusion() {
        String caisseRetour = "";
        try {
            if (!CIRassemblementOuverture.CS_OUVERTURE.equals(getTypeEnregistrement())
                    && !CIRassemblementOuverture.CS_SAISIE_MANUELLE.equals(getTypeEnregistrement())) {
                String caisseReel = getRealCaisse();
                String agenceTenant = getCaisseTenantCI();
                return caisseReel + "." + agenceTenant;
            }
        } catch (Exception e) {
            // Si la valeur n'est pas à jour en db(reprise) => on évite de faire planter l'écran
        }
        return caisseRetour;
    }

    public boolean isCaisseDiffferente() {
        boolean retour = false;
        try {
            retour = CIUtil.isCaisseDifferente(getSession());
        } catch (Exception e) {
            // potentiellement propertie inexistante => false
            retour = false;
        }
        return retour;
    }

    /**
     * Indique si l'annonce est une ouverture. Date de création : (18.07.2003 08:03:11)
     * 
     * @return boolean
     */
    public boolean isCloture() {
        // todo remplacer par type après reprise correcte
        try {
            int arc = Integer.parseInt(getMotifArc());
            if (0 == arc) {
                if (CIRassemblementOuverture.CS_CLOTURE.equals(typeEnregistrement)
                        || CIRassemblementOuverture.CS_CLOTURE_OUVERTURE.equals(typeEnregistrement)) {
                    return true;
                } else {
                    return false;
                }
            }
            if (((arc >= 70) && (arc < 92)) || (arc == 2)) {
                if (!JAUtil.isStringEmpty(getDateCloture())) {
                    return true;
                }
            }
        } catch (Exception ex) {
            // impossible de parser le motif
        }
        return false;
    }

    /**
     * Indique si l'annonce est une ouverture. Date de création : (18.07.2003 08:03:11)
     * 
     * @return boolean
     */
    public boolean isClotureAI() {
        // todo remplacer par type après reprise correcte
        if ("75".equals(getMotifArc()) || "85".equals(getMotifArc())) {
            return true;
        }
        return false;
    }

    /**
     * Indique si l'annonce est une ouverture. Date de création : (18.07.2003 08:03:11)
     * 
     * @return boolean
     */
    public boolean isClotureAVS() {
        // todo remplacer par type après reprise correcte
        if ("71".equals(getMotifArc()) || "81".equals(getMotifArc())) {
            return true;
        }
        return false;
    }

    /**
     * Indique si l'annonce est une ouverture. Date de création : (18.07.2003 08:03:11)
     * 
     * @return boolean
     */
    public boolean isClotureForCiAdditionnel() {
        // todo remplacer par type après reprise correcte
        try {
            int arc = Integer.parseInt(getMotifArc());
            if (0 == arc) {
                if (CIRassemblementOuverture.CS_CLOTURE.equals(typeEnregistrement)
                        || CIRassemblementOuverture.CS_CLOTURE_OUVERTURE.equals(typeEnregistrement)) {
                    return true;
                } else {
                    return false;
                }
            }
            if ((((arc >= 70) && (arc < 92)) || (arc == 2))
                    && !CIRassemblementOuverture.CS_CI_ADDITIONNEL.equals(typeEnregistrement)) {
                if (!JAUtil.isStringEmpty(getDateCloture())) {
                    return true;
                }
            }
        } catch (Exception ex) {
            // impossible de parser le motif
        }
        return false;
    }

    /**
     * Indique si l'annonce est une ouverture. Date de création : (18.07.2003 08:03:11)
     * 
     * @return boolean
     */
    public boolean isOuverture() {
        // todo remplacer par type après reprise correcte
        try {
            int arc = Integer.parseInt(getMotifArc());
            if (arc < 70) {
                return true;
            }
        } catch (Exception ex) {
            // impossible de parser le motif
        }
        return false;
    }

    /**
     * Effectue un rassemblement sur les écritues du CI spécifié.
     * 
     * @param transaction
     *            la transaction à utiliser
     * @param compte
     *            le CI concerné
     * @return le nombre d'écritures qui sont concernées par cette clôture.
     * @exception si
     *                des erreurs surviennent Date de création : (06.01.2003 08:41:18)
     */
    public ArrayList rassemblerEcritures(BTransaction transaction) throws Exception {
        // resultat
        ArrayList result = new ArrayList();
        // chercher écritures concernée
        CIEcritureManager ecritures = new CIEcritureManager();
        ecritures.setSession(getSession());
        ecritures.setForCompteIndividuelId(getCompteIndividuelId());
        ecritures.setForRassemblementOuvertureId("0");
        ecritures.setForIdTypeCompte(CIEcriture.CS_CI);
        ecritures.orderByAnnee();
        ecritures.find(transaction, BManager.SIZE_NOLIMIT);
        // int count = 0;
        for (int i = 0; i < ecritures.size(); i++) {
            CIEcriture ecriture = (CIEcriture) ecritures.getEntity(i);
            CIEcritureAnnonce lien = new CIEcritureAnnonce();
            lien.setSession(getSession());
            lien.setIdEcritureAssure(ecriture.getEcritureId());
            lien.setIdRassemblement(getRassemblementOuvertureId());
            try {
                lien.add(transaction);
            } catch (Exception addException) {
                // arrive actuellement lors d'un doublon (rattrapage d'un cas)
                // ignorer de problème d'ajout mais continuer d'établir la liste
                // d'inscriptions à envoyer
                // addException.printStackTrace();
            }
            result.add(ecriture);
        }
        return result;
    }

    public ArrayList rassemblerEcrituresConjoint(BTransaction transaction, String idCiConjoint) throws Exception {
        ArrayList result = new ArrayList();
        CIEcritureManager ecritures = new CIEcritureManager();
        ecritures.setSession(getSession());
        ecritures.setForCompteIndividuelId(idCiConjoint);
        ecritures.setForRassemblementOuvertureId("0");
        ecritures.setForIdTypeCompte(CIEcriture.CS_CI);
        ecritures.orderByAnnee();
        ecritures.find(transaction, BManager.SIZE_NOLIMIT);
        for (int i = 0; i < ecritures.size(); i++) {
            CIEcriture ecriture = (CIEcriture) ecritures.getEntity(i);
            CIEcritureAnnonce lien = new CIEcritureAnnonce();
            lien.setSession(getSession());
            lien.setIdEcritureConjoint(ecriture.getEcritureId());
            lien.setIdRassemblement(getRassemblementOuvertureId());
            try {
                lien.add(transaction);
            } catch (Exception addException) {
                // arrive actuellement lors d'un doublon (rattrapage d'un cas)
                // ignorer de problème d'ajout mais continuer d'établir la liste
                // d'inscriptions à envoyer
                // addException.printStackTrace();
            }
            result.add(ecriture);
        }
        return result;
    }

    /**
     * Spécifie le no de la caisse et de l'agence au format "CCC.AAA". Date de création : (15.01.2003 15:44:00)
     * 
     * @param caisse
     *            la caisse et l'agence
     */
    public void setCaisseAgenceCommettante(String caisse) {
        caisseAgence = caisse;
    }

    public void setCaisseCommettante(String newCaisseCommettante) {
        caisseCommettante = newCaisseCommettante;
    }

    /**
     * @param string
     */
    public void setCaisseTenantCI(String string) {
        caisseTenantCI = string;
    }

    public void setCompteIndividuelId(String newCompteIndividuelId) {
        compteIndividuelId = newCompteIndividuelId;
    }

    public void setDateCloture(String newDateCloture) {
        // if(newDateCloture!=null && newDateCloture.length()==7) {
        // format mm.aaaa
        // newDateCloture = "01."+newDateCloture;
        // }
        dateCloture = newDateCloture;
    }

    public void setDateOrdre(String newDateOrdre) {
        dateOrdre = newDateOrdre;
    }

    public void setDateRevocation(String newDateRevocation) {
        dateRevocation = newDateRevocation;
    }

    public void setDateSecondRci(String newDateSecondRci) {
        dateSecondRci = newDateSecondRci;
    }

    /**
     * @param string
     */
    public void setIsCiAdditionnel(String string) {
        isCiAdditionnel = string;
    }

    /**
     * @param string
     */
    public void setIsEcran(String string) {
        isEcran = string;
    }

    public void setMotifArc(String newMotifArc) {
        motifArc = newMotifArc;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    public void setRassemblementOuvertureId(String newRassemblementOuvertureId) {
        rassemblementOuvertureId = newRassemblementOuvertureId;
    }

    /**
     * @param realCaisse
     *            the realCaisse to set
     */
    public void setRealCaisse(String realCaisse) {
        this.realCaisse = realCaisse;
    }

    public void setReferenceInterne(String newReferenceInterne) {
        referenceInterne = newReferenceInterne;
    }

    public void setTypeEnregistrement(String newTypeEnregistrement) {
        typeEnregistrement = newTypeEnregistrement;
    }

    public String toMyString() {
        return "Clôture: " + getDateCloture() + ", Révocation: " + getDateRevocation();
    }
}
