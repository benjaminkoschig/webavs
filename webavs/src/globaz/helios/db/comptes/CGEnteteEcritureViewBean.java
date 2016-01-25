package globaz.helios.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.helios.api.ICGJournal;
import globaz.helios.application.CGApplication;
import globaz.helios.db.interfaces.CGLibelle;
import globaz.helios.db.interfaces.ITreeListable;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.HashSet;

public class CGEnteteEcritureViewBean extends BEntity implements ITreeListable, java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static java.lang.String AFFILIE_AVOIR = "-3";
    public final static java.lang.String AFFILIE_DOIT = "-2";
    public final static java.lang.String AFFILIE_DOIT_AVOIR = "-4";
    public final static java.lang.String AFFILIE_ERREUR = "-1";
    // code systeme
    private final static String LABEL_PREFIXE = "ENTETE_ECRITURE_";
    private String cours = ""; // pour traitement ecriture double

    private String date = new String();
    private String dateValeur = new String();

    // deprecated field, but not to delete as it might be used by external
    // application
    private java.lang.String doEcriture = "";
    private HashSet ExceptTypeEcriture = new HashSet();
    // traitement
    // ecriture
    // double
    private Boolean flagCentreChargeCompteCredite = new Boolean(false); // pour
    // traitement
    // ecriture
    // double
    // traitement
    // ecriture
    // double
    private Boolean flagCentreChargeCompteDebite = new Boolean(false); // pour
    // double
    private Boolean flagMonnaieEtrangere = new Boolean(false); // pour
    private String idCentreChargeCredite = ""; // pour traitement ecriture
    private String idCentreChargeDebite = ""; // pour traitement ecriture double
    private java.lang.String idCompteAffillie = "";

    private String idCompteCredite = ""; // pour traitement ecriture double
    private String idCompteDebite = ""; // pour traitement ecriture double
    private String idContrepartieAvoir = new String();
    private String idContrepartieDoit = new String();
    private String idEnteteEcriture = "";
    private java.lang.String idExterneCompteCredite = "";
    private java.lang.String idExterneCompteDebite = "";
    private String idFournisseur = new String();
    private String idJournal = new String();
    private java.lang.String idLivre = "";
    private String idMessage = ""; // pour traitement ecriture double
    private String idSecteurAvs = new String();
    private String idSection = new String();
    private String idTypeEcriture = "";
    private CGJournal journal = null;

    private String libelle = new String();
    private String montant = ""; // pour traitement ecriture double

    private String montantMonnaie = ""; // pour traitement ecriture double
    private String nextId = new String();

    private String nombreAvoir = new String();
    private String nombreDoit = new String();

    private String numeroCentreChargeCredite;

    private String numeroCentreChargeDebite;
    private String piece = new String();
    private Boolean quittancer = new Boolean(false); // pour traitement ecriture
    private String remarque = ""; // pour traitement ecriture double
    // double
    private String soldeCompteCredite = ""; // pour traitement ecriture double
    private String soldeCompteDebite = ""; // pour traitement ecriture double
    private String totalAvoir = new String();

    private String totalDoit = new String();

    /**
     * Commentaire relatif au constructeur CGEnteteEcriture
     */
    public CGEnteteEcritureViewBean() {
        super();
        ExceptTypeEcriture.add(CGEcritureViewBean.CS_TYPE_ECRITURE_DETTE_AVOIR);
    }

    @Override
    protected void _afterRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        if (CGEcritureViewBean.CS_TYPE_ECRITURE_DOUBLE.equals(getIdTypeEcriture())) {
            CGEcritureViewBean ecrDebit = new CGEcritureViewBean();
            CGEcritureViewBean ecrCredit = new CGEcritureViewBean();

            // retrieve
            ecrDebit.setSession(getSession());
            ecrDebit.setIdEcriture(idContrepartieDoit);
            ecrDebit.retrieve(transaction);

            ecrCredit.setSession(getSession());
            ecrCredit.setIdEcriture(idContrepartieAvoir);
            ecrCredit.retrieve(transaction);

            // debit
            setIdCompteDebite(ecrDebit.getIdCompte());
            setIdCentreChargeDebite(ecrDebit.getIdCentreCharge());

            // credit
            setIdCompteCredite(ecrCredit.getIdCompte());
            setIdCentreChargeCredite(ecrCredit.getIdCentreCharge());

            BigDecimal dummy = new BigDecimal(0);

            if (ecrDebit.getMontantBaseMonnaie() != null && ecrDebit.getMontantBaseMonnaie().trim().length() > 0
                    && dummy.compareTo(new BigDecimal(ecrDebit.getMontantBaseMonnaie())) != 0) {
                setMontantMonnaie(ecrDebit.getMontantAfficheMonnaie());
            } else {
                setMontantMonnaie(ecrCredit.getMontantAfficheMonnaie());
            }

            if (ecrDebit.getCoursMonnaie() != null && ecrDebit.getCoursMonnaie().trim().length() > 0
                    && dummy.compareTo(new BigDecimal(ecrDebit.getCoursMonnaie())) != 0) {
                setCours(ecrDebit.getCoursMonnaie());
            } else {
                setCours(ecrCredit.getCoursMonnaie());
            }

            // identique (debit)
            setMontant(ecrDebit.getMontantAffiche());
            setRemarque(ecrDebit.getRemarque());
            setIdMessage(getIdMessage());
        }

    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        if (nextId == null || JadeStringUtil.isBlank(nextId)) {
            // incrémente de +1 le numéro
            setIdEnteteEcriture(_incCounter(transaction, "0"));
        } else {
            setIdEnteteEcriture(nextId);
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
        // Suppression
        // si l'utilisateur est le proprietaire
        // et le journal est de type manuel
        // et le journal n'est pas comptabilisé
        boolean proprietaire = (getSession().getUserId().equals(retrieveJournal().getProprietaire()));
        boolean comptabilise = ICGJournal.CS_ETAT_COMPTABILISE.equals(retrieveJournal().getIdEtat());
        boolean manuel = retrieveJournal().isEstPublic().booleanValue();
        boolean condition1 = (proprietaire || manuel) && !comptabilise;

        // ou si l'utilisateur a les droits de chef comptable
        boolean isChefComptable = false;
        if ((JadeAdminServiceLocatorProvider.getLocator().getRoleUserService().findAllIdRoleForIdUser(getSession()
                .getUserId())) != null) {
            isChefComptable = CGApplication.isUserChefComptable(getSession());
        }

        if (!condition1 && !isChefComptable) {
            _addError(transaction, label("SUPPRESSION_NON_AUTHORISE"));
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CGECREP";
    }

    @Override
    protected void _init() {

        if (JadeStringUtil.isBlank(getIdTypeEcriture())) {
            setIdTypeEcriture(CGEcritureViewBean.CS_TYPE_ECRITURE_DOUBLE);
        }
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idEnteteEcriture = statement.dbReadNumeric("IDENTETEECRITURE");
        idTypeEcriture = statement.dbReadNumeric("IDTYPEECRITURE");
        date = statement.dbReadDateAMJ("DATE");
        libelle = statement.dbReadString("LIBELLE");
        dateValeur = statement.dbReadDateAMJ("DATEVALEUR");
        piece = statement.dbReadString("PIECE");
        idContrepartieDoit = statement.dbReadNumeric("IDCONTREPARTIEDOIT");
        idContrepartieAvoir = statement.dbReadNumeric("IDCONTREPARTIEAVOI");
        idCompteAffillie = statement.dbReadNumeric("IDCOMPTEAFFILLIE");
        totalDoit = statement.dbReadNumeric("TOTALDOIT", 2);
        totalAvoir = statement.dbReadNumeric("TOTALAVOIR", 2);
        nombreDoit = statement.dbReadNumeric("NOMBREDOIT");
        nombreAvoir = statement.dbReadNumeric("NOMBREAVOIR");
        idJournal = statement.dbReadNumeric("IDJOURNAL");
        idSecteurAvs = statement.dbReadNumeric("IDSECTEURAVS");
        idFournisseur = statement.dbReadNumeric("IDFOURNISSEUR");
        idSection = statement.dbReadNumeric("IDSECTION");

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            _addError(statement.getTransaction(), getSession().getLabel("JOURNAL_INEXISTANT"));
            return;
        }

        if (JAUtil.isDateEmpty(getDate())) {
            _addError(statement.getTransaction(), label("DATE_NON_RENSEIGNE"));
            return;
        }

        if (getDateValeur() == null || getDateValeur().equals("")) {
            setDateValeur(getDate());
        }

        if (CGEcritureViewBean.CS_TYPE_ECRITURE_DOUBLE.equals(getIdTypeEcriture())) {

            if (JadeStringUtil.isBlank(getIdCompteDebite())) {
                if (JadeStringUtil.isIntegerEmpty(getIdExterneCompteDebite())) {
                    _addError(statement.getTransaction(), label("COMPTE_DEBIT_ERROR_1"));
                    return;
                } else {

                    CGPlanComptableViewBean entity = new CGPlanComptableViewBean();
                    entity.setSession(statement.getTransaction().getSession());
                    entity.setIdExerciceComptable(retrieveJournal().getIdExerciceComptable());
                    entity.setIdExterne(getIdExterneCompteDebite());
                    entity.setAlternateKey(CGPlanComptableViewBean.AK_EXTERNE);
                    entity.retrieve(statement.getTransaction());
                    if (entity == null || entity.isNew()) {
                        _addError(statement.getTransaction(), label("COMPTE_DEBIT_ERROR_2") + "No "
                                + getIdExterneCompteDebite());
                    } else {
                        setIdCompteDebite(entity.getIdCompte());
                    }
                }
            }

            if (JadeStringUtil.isBlank(getIdCompteCredite())) {
                if (JadeStringUtil.isIntegerEmpty(getIdExterneCompteCredite())) {
                    _addError(statement.getTransaction(), label("COMPTE_CREDIT_ERROR_1"));
                    return;
                } else {
                    CGPlanComptableViewBean entity = new CGPlanComptableViewBean();
                    entity.setSession(statement.getTransaction().getSession());
                    entity.setIdExerciceComptable(retrieveJournal().getIdExerciceComptable());
                    entity.setIdExterne(getIdExterneCompteCredite());
                    entity.setAlternateKey(CGPlanComptableViewBean.AK_EXTERNE);
                    entity.retrieve(statement.getTransaction());
                    if (entity == null || entity.isNew()) {
                        _addError(statement.getTransaction(), label("COMPTE_CREDIT_ERROR_2") + "No "
                                + getIdExterneCompteCredite());
                    } else {
                        setIdCompteCredite(entity.getIdCompte());
                    }
                }
            }

            // Vérification des comptes au niveau de la monnaie
            CGCompte compteCredite = new CGCompte();
            compteCredite.setSession(getSession());
            compteCredite.setIdCompte(getIdCompteCredite());
            try {
                compteCredite.retrieve(statement.getTransaction());
            } catch (Exception e) {
                e.printStackTrace();
                _addError(statement.getTransaction(), label("COMPTE_CREDIT_ERROR_3"));
                return;
            }
            CGCompte compteDebite = new CGCompte();
            compteDebite.setSession(getSession());
            compteDebite.setIdCompte(getIdCompteDebite());
            try {
                compteDebite.retrieve(statement.getTransaction());
            } catch (Exception e) {
                e.printStackTrace();
                _addError(statement.getTransaction(), label("COMPTE_CREDIT_ERROR_3"));
                return;
            }
            if (compteDebite.isNew()) {
                _addError(statement.getTransaction(), label("COMPTE_DEBIT_INEXISTANT"));
                return;
            }
            if (compteCredite.isNew()) {
                _addError(statement.getTransaction(), label("COMPTE_CREDIT_INEXISTANT"));
                return;
            }

            // Si un des 2 comptes est de nature monnaie étrangère...
            // effectue les validations suivantes.
            if (compteDebite.getIdNature().equals(CGCompte.CS_MONNAIE_ETRANGERE)
                    || compteCredite.getIdNature().equals(CGCompte.CS_MONNAIE_ETRANGERE)) {

                boolean isMontantMonnaieEtrangere = false;
                boolean isCours = false;
                boolean isMontantCHF = false;
                // un montant en monnaie étrangère est saisie
                if (getMontantMonnaie() != null && getMontantMonnaie().trim().length() > 0
                        && !getMontantMonnaie().startsWith("0.00") && !getMontantMonnaie().equals("0")) {

                    if (compteDebite.getIdNature().equals(CGCompte.CS_MONNAIE_ETRANGERE)
                            && compteCredite.getIdNature().equals(CGCompte.CS_MONNAIE_ETRANGERE)
                            && (!compteDebite.getCodeISOMonnaie().equals(compteCredite.getCodeISOMonnaie()))) {

                        _addError(statement.getTransaction(), label("COMPTE_C_D_MONNAIE"));
                        _addError(statement.getTransaction(), label("COMPTE_C") + compteCredite.getCodeISOMonnaie());
                        _addError(statement.getTransaction(), label("COMPTE_D") + compteDebite.getCodeISOMonnaie());
                        return;
                    }
                    isMontantMonnaieEtrangere = true;
                }
                // le cours est saisie
                if (getCours() != null && getCours().trim().length() > 0 && !getCours().startsWith("0.00")
                        && !getCours().equals("0")) {

                    if (compteDebite.getIdNature().equals(CGCompte.CS_MONNAIE_ETRANGERE)
                            && compteCredite.getIdNature().equals(CGCompte.CS_MONNAIE_ETRANGERE)
                            && (!compteDebite.getCodeISOMonnaie().equals(compteCredite.getCodeISOMonnaie()))) {

                        _addError(statement.getTransaction(), label("COMPTE_C_D_MONNAIE"));
                        _addError(statement.getTransaction(), label("COMPTE_C") + compteCredite.getCodeISOMonnaie());
                        _addError(statement.getTransaction(), label("COMPTE_D") + compteDebite.getCodeISOMonnaie());
                        return;
                    }
                    isCours = true;
                }
                // un montant chf est saisi
                if (getMontant() != null && getMontant().trim().length() > 0 && !getMontant().startsWith("0.00")
                        && !getMontant().equals("0")) {
                    isMontantCHF = true;
                }

                // Règle de plausibilités :
                // montant CHF & cours --> ok
                // montant CHF & montant Monnaie --> ok
                // montant Monnaie & cours --> ok
                // montant CHF & cours & montant monnaie --> ok
                // montant CHF sur comptes CHF ok
                // montant CHF & cours == 0 & montant Monnaie == 0 --> ok

                // Il doit être possible de saisir un montant CHF uniquement sur
                // un compte en monnaie étrangère.
                // plausi: montant CHF & cours == 0 & montant Monnaie == 0 -->
                // ok
                if (isMontantCHF && getCours() != null && getMontantMonnaie() != null
                        && JadeStringUtil.isIntegerEmpty(getCours())
                        && JadeStringUtil.isIntegerEmpty(getMontantMonnaie())) {
                    ;// OK
                } else if (isMontantCHF && isCours) {
                    ;// OK
                } else if (isMontantCHF && isMontantMonnaieEtrangere) {
                    ;// OK
                } else if (isMontantMonnaieEtrangere && isCours) {
                    ;// OK
                } else if (isMontantCHF && isMontantMonnaieEtrangere && isCours) {
                    ;// OK
                } else {
                    _addError(statement.getTransaction(), label("ECR_CPT_MON_ETR_DATA_MISSING"));
                }
            }
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDENTETEECRITURE", _dbWriteNumeric(statement.getTransaction(), getIdEnteteEcriture(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDENTETEECRITURE",
                _dbWriteNumeric(statement.getTransaction(), getIdEnteteEcriture(), "idEnteteEcriture"));
        statement.writeField("IDTYPEECRITURE",
                _dbWriteNumeric(statement.getTransaction(), getIdTypeEcriture(), "idTypeEcriture"));
        statement.writeField("DATE", _dbWriteDateAMJ(statement.getTransaction(), getDate(), "date"));
        statement.writeField("LIBELLE", _dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField("DATEVALEUR", _dbWriteDateAMJ(statement.getTransaction(), getDateValeur(), "dateValeur"));
        statement.writeField("PIECE", _dbWriteString(statement.getTransaction(), getPiece(), "piece"));
        statement.writeField("IDCONTREPARTIEDOIT",
                _dbWriteNumeric(statement.getTransaction(), getIdContrepartieDoit(), "idContrepartieDoit"));
        statement.writeField("IDCONTREPARTIEAVOI",
                _dbWriteNumeric(statement.getTransaction(), getIdContrepartieAvoir(), "idContrepartieAvoir"));
        statement.writeField("IDCOMPTEAFFILLIE",
                _dbWriteNumeric(statement.getTransaction(), getIdCompteAffillie(), "idCompteAffillie"));
        statement.writeField("TOTALDOIT", _dbWriteNumeric(statement.getTransaction(), getTotalDoit(), "totalDoit"));
        statement.writeField("TOTALAVOIR", _dbWriteNumeric(statement.getTransaction(), getTotalAvoir(), "totalAvoir"));
        statement.writeField("NOMBREDOIT", _dbWriteNumeric(statement.getTransaction(), getNombreDoit(), "nombreDoit"));
        statement.writeField("NOMBREAVOIR",
                _dbWriteNumeric(statement.getTransaction(), getNombreAvoir(), "nombreAvoir"));
        statement.writeField("IDJOURNAL", _dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        statement.writeField("IDSECTEURAVS",
                _dbWriteNumeric(statement.getTransaction(), getIdSecteurAvs(), "idSecteurAvs"));
        statement.writeField("IDFOURNISSEUR",
                _dbWriteNumeric(statement.getTransaction(), getIdFournisseur(), "idFournisseur"));
        statement.writeField("IDSECTION", _dbWriteNumeric(statement.getTransaction(), getIdSection(), "idSection"));
    }

    public String generateNextId(globaz.globall.db.BTransaction transaction) throws Exception {
        nextId = _incCounter(transaction, "0");
        return nextId;
    }

    @Override
    public BManager[] getChilds() throws Exception {
        BManager[] managers = new BManager[1];
        CGEcritureListViewBean manager = new CGEcritureListViewBean();
        manager.setForIdEnteteEcriture(getIdEnteteEcriture());

        managers[0] = manager;
        return managers;
    }

    public String getCours() {
        return cours;
    }

    public String getDate() {
        return date;
    }

    public String getDateValeur() {
        return dateValeur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.12.2002 13:10:12)
     * 
     * @deprecated
     * @return java.lang.String
     */
    @Deprecated
    public java.lang.String getDoEcriture() {
        return doEcriture;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.11.2002 16:32:28)
     * 
     * @return java.util.Vector
     */
    public HashSet getExceptTypeEcriture() {
        return ExceptTypeEcriture;
    }

    public String getIdCentreChargeCredite() {
        return idCentreChargeCredite;
    }

    public String getIdCentreChargeDebite() {
        return idCentreChargeDebite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.07.2003 16:13:30)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdCompteAffillie() {
        return idCompteAffillie;
    }

    public String getIdCompteCredite() {
        return idCompteCredite;
    }

    public String getIdCompteDebite() {
        return idCompteDebite;
    }

    public String getIdContrepartieAvoir() {
        return idContrepartieAvoir;
    }

    public String getIdContrepartieDoit() {
        return idContrepartieDoit;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdEnteteEcriture() {
        return idEnteteEcriture;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.02.2003 16:10:31)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdExterneCompteCredite() {
        return idExterneCompteCredite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.02.2003 16:09:57)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdExterneCompteDebite() {
        return idExterneCompteDebite;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public String getIdJournal() {
        return idJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.11.2002 11:47:45)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdLivre() {
        return idLivre;
    }

    public String getIdMessage() {
        return idMessage;
    }

    public String getIdSecteurAvs() {
        return idSecteurAvs;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getIdTypeEcriture() {
        return idTypeEcriture;
    }

    public String getJournalLibelle() {
        try {
            if (journal == null) {
                journal = new CGJournal();
                journal.setIdJournal(idJournal);
                journal.setSession(getSession());
                journal.retrieve();
            }
            return journal.getLibelle();
        } catch (Exception e) {
            e.printStackTrace();
            return CGLibelle.LIBELLE_ERROR;
        }
    }

    @Override
    public String getLibelle() {
        return libelle;
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantAffiche() {
        return montant;
    }

    public String getMontantAfficheMonnaie() {
        return montantMonnaie;
    }

    public String getMontantMonnaie() {
        return montantMonnaie;
    }

    public String getNombreAvoir() {
        return nombreAvoir;
    }

    public String getNombreDoit() {
        return nombreDoit;
    }

    public String getNumeroCentreChargeCredite() {
        return numeroCentreChargeCredite;
    }

    public String getNumeroCentreChargeDebite() {
        return numeroCentreChargeDebite;
    }

    public String getPiece() {
        return piece;
    }

    public String getRemarque() {
        return remarque;
    }

    public String getSoldeCompteCredite() {
        return soldeCompteCredite;
    }

    public String getSoldeCompteDebite() {
        return soldeCompteDebite;
    }

    public String getTotalAvoir() {
        return totalAvoir;
    }

    public String getTotalDoit() {
        return totalDoit;
    }

    public Boolean isFlagCentreChargeCompteCredite() {
        return flagCentreChargeCompteCredite;
    }

    public Boolean isFlagCentreChargeCompteDebite() {
        return flagCentreChargeCompteDebite;
    }

    public Boolean isFlagMonnaieEtrangere() {
        return flagMonnaieEtrangere;
    }

    public Boolean isQuittancer() {
        return quittancer;
    }

    private String label(String code) {
        return getSession().getLabel(LABEL_PREFIXE + code);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.12.2002 13:10:32)
     * 
     * @return globaz.helios.db.comptes.CGEcritureViewBean
     */
    public CGEcritureViewBean retrieveContrepartieAvoir() throws Exception {

        CGEcritureViewBean ecriture = new CGEcritureViewBean();

        if (getNombreAvoir() == null || getNombreAvoir().equals("")) {
            throw (new Exception(label("CONTREPART_INCORRECTE")));
        }

        // si plus d'une ecriture a l'avoir, on retourne null
        if (Integer.parseInt(getNombreAvoir()) > 1) {
            return null;
        }

        ecriture.setSession(getSession());
        ecriture.setIdEcriture(idContrepartieAvoir);
        try {
            ecriture.retrieve();
        } catch (Exception e) {
            ecriture = null;
            throw (new Exception(label("CONTREPART_INEXISTANTE")));
        }
        return ecriture;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.12.2002 13:10:32)
     * 
     * @return globaz.helios.db.comptes.CGEcritureViewBean
     */
    public CGEcritureViewBean retrieveContrepartieDoit() throws Exception {

        if (getNombreDoit() == null || getNombreDoit().equals("")) {
            throw (new Exception(label("CONTREPART_INCORRECTE")));
        }
        // si plus d'une ecriture au doit, on retourne null
        if (Integer.parseInt(getNombreDoit()) > 1) {
            return null;
        }

        CGEcritureViewBean ecriture = new CGEcritureViewBean();
        ecriture.setSession(getSession());
        ecriture.setIdEcriture(idContrepartieDoit);
        try {
            ecriture.retrieve();
        } catch (Exception e) {
            ecriture = null;
            throw (new Exception(label("CONTREPART_INEXISTANTE")));
        }
        return ecriture;
    }

    public void retrieveIdCentreChargeCrediteFromNumero() throws Exception {
        if (!JadeStringUtil.isBlank(getNumeroCentreChargeCredite())) {
            CGCentreChargeManager centreChargeManager = new CGCentreChargeManager();
            centreChargeManager.setSession(getSession());
            centreChargeManager.setForIdMandat(retrieveJournal().getExerciceComptable().getIdMandat());
            centreChargeManager.setForNumero(getNumeroCentreChargeCredite());

            centreChargeManager.find();

            if (!centreChargeManager.hasErrors() && !centreChargeManager.isEmpty()) {
                setIdCentreChargeCredite(((CGCentreCharge) centreChargeManager.getFirstEntity()).getIdCentreCharge());
            }
        }
    }

    public void retrieveIdCentreChargeDebiteFromNumero() throws Exception {
        if (!JadeStringUtil.isBlank(getNumeroCentreChargeDebite())) {
            CGCentreChargeManager centreChargeManager = new CGCentreChargeManager();
            centreChargeManager.setSession(getSession());
            centreChargeManager.setForIdMandat(retrieveJournal().getExerciceComptable().getIdMandat());
            centreChargeManager.setForNumero(getNumeroCentreChargeDebite());

            centreChargeManager.find();

            if (!centreChargeManager.hasErrors() && !centreChargeManager.isEmpty()) {
                setIdCentreChargeDebite(((CGCentreCharge) centreChargeManager.getFirstEntity()).getIdCentreCharge());
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.02.2003 15:49:49)
     * 
     * @return globaz.helios.db.comptes.CGModeleEcriture
     */
    public CGJournal retrieveJournal() {
        try {
            if (journal == null) {
                journal = new CGJournal();
                journal.setIdJournal(idJournal);
                journal.setSession(getSession());
                journal.retrieve();
            }
            return journal;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setCompteCredite(String value) {
        idCompteCredite = value;
    }

    public void setCours(String value) {
        cours = value;
    }

    public void setDate(String newDate) {
        date = newDate;
    }

    public void setDateValeur(String newDateValeur) {
        dateValeur = newDateValeur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.12.2002 13:10:12)
     * 
     * @param newDoEcriture
     *            java.lang.String
     * @deprecated
     */
    @Deprecated
    public void setDoEcriture(java.lang.String newDoEcriture) {
        doEcriture = newDoEcriture;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.11.2002 16:32:28)
     * 
     * @param newExceptTypeEcriture
     *            java.util.Vector
     */
    public void setExceptTypeEcriture(HashSet newExceptTypeEcriture) {
        ExceptTypeEcriture = newExceptTypeEcriture;
    }

    public void setFlagCentreChargeCompteCredite(Boolean value) {
        flagCentreChargeCompteCredite = value;
    }

    public void setFlagCentreChargeCompteDebite(Boolean value) {
        flagCentreChargeCompteDebite = value;
    }

    public void setFlagMonnaieEtrangere(Boolean value) {
        flagMonnaieEtrangere = value;
    }

    public void setIdCentreChargeCredite(String value) {
        idCentreChargeCredite = value;
    }

    public void setIdCentreChargeDebite(String value) {
        idCentreChargeDebite = value;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.07.2003 16:13:30)
     * 
     * @param newIdCompteAffillie
     *            java.lang.String
     */
    public void setIdCompteAffillie(java.lang.String newIdCompteAffillie) {
        idCompteAffillie = newIdCompteAffillie;
    }

    public void setIdCompteCredite(String value) {
        idCompteCredite = value;
    }

    public void setIdCompteDebite(String value) {
        idCompteDebite = value;
    }

    public void setIdContrepartieAvoir(String newIdContrepartieAvoir) {
        idContrepartieAvoir = newIdContrepartieAvoir;
    }

    public void setIdContrepartieDoit(String newIdContrepartieDoit) {
        idContrepartieDoit = newIdContrepartieDoit;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    public void setIdEnteteEcriture(String newIdEnteteEcriture) {
        idEnteteEcriture = newIdEnteteEcriture;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.02.2003 16:10:31)
     * 
     * @param newIdExterneCompteCredite
     *            java.lang.String
     */
    public void setIdExterneCompteCredite(java.lang.String newIdExterneCompteCredite) {
        idExterneCompteCredite = newIdExterneCompteCredite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.02.2003 16:09:57)
     * 
     * @param newIdExterneCompteDebite
     *            java.lang.String
     */
    public void setIdExterneCompteDebite(java.lang.String newIdExterneCompteDebite) {
        idExterneCompteDebite = newIdExterneCompteDebite;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public void setIdJournal(String newIdJournal) {
        idJournal = newIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.11.2002 11:47:45)
     * 
     * @param newIdLivre
     *            java.lang.String
     */
    public void setIdLivre(java.lang.String newIdLivre) {
        idLivre = newIdLivre;
    }

    public void setIdMessage(String value) {
        idMessage = value;
    }

    public void setIdSecteurAvs(String newIdSecteurAvs) {
        idSecteurAvs = newIdSecteurAvs;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setIdTypeEcriture(String newIdTypeEcriture) {
        idTypeEcriture = newIdTypeEcriture;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.02.2003 15:49:49)
     * 
     * @param newJournal
     *            globaz.helios.db.comptes.CGModeleEcriture
     */
    public void setJournal(CGJournal newJournal) {
        journal = newJournal;
    }

    public void setLibelle(String newLibelle) {
        libelle = newLibelle;
    }

    public void setMontant(String value) {
        montant = JANumberFormatter.deQuote(value);
    }

    public void setMontantMonnaie(String value) {
        montantMonnaie = value;
    }

    public void setNombreAvoir(String newNombreAvoir) {
        nombreAvoir = newNombreAvoir;
    }

    public void setNombreDoit(String newNombreDoit) {
        nombreDoit = newNombreDoit;
    }

    public void setNumeroCentreChargeCredite(String numeroCentreChargeCredite) {
        this.numeroCentreChargeCredite = numeroCentreChargeCredite;
    }

    public void setNumeroCentreChargeDebite(String numeroCentreChargeDebite) {
        this.numeroCentreChargeDebite = numeroCentreChargeDebite;
    }

    public void setPiece(String newPiece) {
        piece = newPiece;
    }

    public void setQuittancer(Boolean value) {
        quittancer = value;
    }

    public void setRemarque(String value) {
        remarque = value;
    }

    public void setSoldeCompteCredite(String value) {
        soldeCompteCredite = value;
    }

    public void setSoldeCompteDebite(String value) {
        soldeCompteDebite = value;
    }

    public void setTotalAvoir(String newTotalAvoir) {
        totalAvoir = newTotalAvoir;
    }

    public void setTotalDoit(String newTotalDoit) {
        totalDoit = newTotalDoit;
    }

}
