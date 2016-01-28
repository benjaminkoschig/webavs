package globaz.osiris.db.interets;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.process.interet.util.FAInteretGenericUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CASection;
import java.util.ArrayList;

/**
 * Insérez la description du type ici. Date de création : (30.12.2002 09:13:16)
 * 
 * @author: Administrator
 */
public class CAInteretMoratoire extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int AK_FACTURE_JOURNAL = 1;
    public static final int AK_INTERETSMORATOIRES = 1;
    public static final String CS_A_CONTROLER = "229005";
    public static final String CS_AFFILIATION_RETROACTIVE = "253001";
    public static final String CS_AUTOMATIQUE = "229001";
    public static final String CS_EXEMPTE = "229003";
    public static final String CS_MANUEL = "229004";
    private static final String CS_NATURERUBRIQUE_MORATOIRE = "200009";
    private static final String CS_NATURERUBRIQUE_REMUNERATOIRE = "200010";
    public static final String CS_PARAMETRES_IM = "10200030";
    public static final String CS_REPRISE_IMPOT = "253002";
    public static final String CS_SOUMIS = "229002";
    public static final String FIELD_DATECALCUL = "DATECALCUL";
    public static final String FIELD_DATEDECISION = "DATEDECISION";
    public static final String FIELD_DATEFACTURATION = "DATEFACTURATION";
    public static final String FIELD_DATELETTRE = "DATELETTRE";
    public static final String FIELD_DATERECOURS = "DATERECOURS";

    public static final String FIELD_IDELEFAC = "IDELEFAC";

    public static final String FIELD_IDGENREINTERET = "IDGENREINTERET";
    public static final String FIELD_IDINTERETMORATOIRE = "IDINTERETMORATOIRE";

    public static final String FIELD_IDJOUFAC = "IDJOUFAC";
    public static final String FIELD_IDJOURNALCALCUL = "IDJOURNALCALCUL";
    public static final String FIELD_IDPLAN = "IDPLAN";
    public static final String FIELD_IDRUBRIQUE = "IDRUBRIQUE";
    public static final String FIELD_IDSECTION = "IDSECTION";
    public static final String FIELD_IDSECTIONFACTURE = "IDSECTIONFACTURE";

    public static final String FIELD_MOTIFCALCUL = "MOTIFCALCUL";

    public static final String FIELD_NUMEROFACTUREGROUPE = "NUMEROFACTUREGRP";
    public static final String FIELD_REMARQUE = "REMARQUE";

    public static final String STATUS_BLOQUE = "902002";
    public static final String STATUS_COMPTABILISE = "902003";

    public static final String TABLE_CAIMDCP = "CAIMDCP";

    /** (DATECALCUL) */
    private String dateCalcul = new String();

    /** (DATEDECISION) */
    private String dateDecision = new String();

    /** (DATEFACTURATION) */
    private String dateFacturation = new String();
    /** (DATELETTRE) */
    private String dateLettre = new String();
    /** (DATERECOURS) */
    private String dateRecours = new String();
    private String domaine = CAApercuInteretMoratoire.DOMAINE_CA;
    private ArrayList elementEntete = null;
    private FAEnteteFacture enteteFacture = null;
    private String idCompteAnnexe = new String();
    /** (IDELEFAC) */
    private String idElementFacturation = new String();
    private String idExterneRoleEcran = "";
    private String idExterneRubriqueEcran = "";
    private String idExterneSectionEcran = "";
    private String idGenreInteret = new String();

    /** Fichier CAIMDCP */
    /** (IDINTERETMORATOIRE) */
    private String idInteretMoratoire = new String();

    /** (IDJOURNALCALCUL) */
    private String idJournalCalcul = new String();
    /** (IDJOUFAC) */
    private String idJournalFacturation = new String();
    private String idPlan = new String();
    private String idRoleEcran;

    private String idRubrique = new String();
    /** (IDSECTION) */
    private String idSection = new String();
    /** (IDSECTIONFACTURE) */
    private String idSectionFacture = new String();

    private CAJournal journal = null;
    /** (MOTIFCALCUL) */
    private String motifcalcul = new String();

    private String numeroFactureGroupe;

    private FAPassage passage = null;
    private CAPlanCalculInteret plan = null;
    private String remarque = new String();
    private CARubrique rubrique = null;

    private CASection section = null;

    private String warningVerifierCompensation = "";

    public static String getStatus(FAPassage passage, String motifCalcul) {
        // si la décision d'IM ne possede pas de facture on ne suit pas les
        // règles liés au passage
        if ((passage != null) && passage.isEstVerrouille().booleanValue()) { // bloqué
            return CAInteretMoratoire.STATUS_BLOQUE;
        } else {
            if (motifCalcul.equals(CAInteretMoratoire.CS_SOUMIS) || motifCalcul.equals(CAInteretMoratoire.CS_MANUEL)) {
                if ((passage != null) && passage.getStatus().equals(CAInteretMoratoire.STATUS_COMPTABILISE)) {
                    return CAInteretMoratoire.STATUS_COMPTABILISE;
                }
            }
        }

        return motifCalcul;
    }

    // code systeme
    /**
     * Commentaire relatif au constructeur CAInteretMoratoire
     */
    public CAInteretMoratoire() {
        super();
    }

    /**
     * Effectue des traitements après une suppression de la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements après la suppression de l'entité de la BD
     * <p>
     * La transaction n'est pas validée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_afterDelete()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws java.lang.Exception {
        // Supprimer les lignes
        CADetailInteretMoratoireManager mgr = new CADetailInteretMoratoireManager();
        mgr.setSession(getSession());
        mgr.setForIdInteretMoratoire(getIdInteretMoratoire());
        mgr.find(transaction);
        for (int i = 0; i < mgr.size(); i++) {
            CADetailInteretMoratoire ligne = (CADetailInteretMoratoire) mgr.getEntity(i);
            ligne.delete(transaction);
        }
    }

    /**
     * Effectue des traitements avant un ajout dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements avant l'ajout de l'entité dans la BD
     * <p>
     * L'exécution de l'ajout n'est pas effectuée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_beforeAdd()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incrémente le prochain numéro
        setIdInteretMoratoire(this._incCounter(transaction, "0"));
        // Rubrique par défaut
        if (JadeStringUtil.isIntegerEmpty(getIdRubrique())) {
            CAGenreInteret gi = new CAGenreInteret();
            gi.setSession(getSession());
            gi.setAlternateKey(CAGenreInteret.AK_PLAN_TYPE);
            gi.setIdPlanCalculInteret(getIdPlan());
            gi.setIdTypeInteret(getIdGenreInteret());
            gi.retrieve(transaction);
            if (!gi.isNew()) {
                setIdRubrique(gi.getIdRubrique());
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
        // Refuser si déjà comptabiliser
        if (!JadeStringUtil.isIntegerEmpty(getIdJournalFacturation())
                && !getIdJournalFacturation().equals(getIdJournalCalcul())) {
            _addError(transaction, getSession().getLabel("7354"));
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return CAInteretMoratoire.TABLE_CAIMDCP;
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idInteretMoratoire = statement.dbReadNumeric(CAInteretMoratoire.FIELD_IDINTERETMORATOIRE);
        idSection = statement.dbReadNumeric(CAInteretMoratoire.FIELD_IDSECTION);
        idSectionFacture = statement.dbReadNumeric(CAInteretMoratoire.FIELD_IDSECTIONFACTURE);
        idJournalCalcul = statement.dbReadNumeric(CAInteretMoratoire.FIELD_IDJOURNALCALCUL);
        dateCalcul = statement.dbReadDateAMJ(CAInteretMoratoire.FIELD_DATECALCUL);
        dateLettre = statement.dbReadDateAMJ(CAInteretMoratoire.FIELD_DATELETTRE);
        dateDecision = statement.dbReadDateAMJ(CAInteretMoratoire.FIELD_DATEDECISION);
        dateFacturation = statement.dbReadDateAMJ(CAInteretMoratoire.FIELD_DATEFACTURATION);
        dateRecours = statement.dbReadDateAMJ(CAInteretMoratoire.FIELD_DATERECOURS);
        motifcalcul = statement.dbReadNumeric(CAInteretMoratoire.FIELD_MOTIFCALCUL);
        idElementFacturation = statement.dbReadNumeric(CAInteretMoratoire.FIELD_IDELEFAC);
        idJournalFacturation = statement.dbReadNumeric(CAInteretMoratoire.FIELD_IDJOUFAC);
        idRubrique = statement.dbReadNumeric(CAInteretMoratoire.FIELD_IDRUBRIQUE);
        idGenreInteret = statement.dbReadNumeric(CAInteretMoratoire.FIELD_IDGENREINTERET);
        idPlan = statement.dbReadNumeric(CAInteretMoratoire.FIELD_IDPLAN);
        remarque = statement.dbReadString(CAInteretMoratoire.FIELD_REMARQUE);
        numeroFactureGroupe = statement.dbReadNumeric(CAInteretMoratoire.FIELD_NUMEROFACTUREGROUPE);
    }

    private void _setCompensationAQuittancer(String idEnteteFactureContenantInteret, BSession theMuscaSession,
            BTransaction theTransaction) throws Exception {

        if (JadeStringUtil.isBlankOrZero(idEnteteFactureContenantInteret)) {
            throw new Exception(
                    CAInteretMoratoire.class.getName()
                            + " can't _setCompensationAQuittancer because parameter idEnteteFactureContenantInteret is blank or zero");
        }

        FAAfactManager manager = new FAAfactManager();
        manager.setSession(theMuscaSession);
        manager.setForIdEnteteFacture(idEnteteFactureContenantInteret);
        manager.setForIsAfactCompensation(true);
        manager.find(theTransaction, BManager.SIZE_NOLIMIT);

        warningVerifierCompensation = "";
        boolean isACompensationToQuittancer = false;
        for (int i = 0; i < manager.size(); i++) {
            FAAfact anAfact = (FAAfact) manager.getEntity(i);
            anAfact.setSession(theMuscaSession);
            anAfact.setAQuittancer(true);
            anAfact.update(theTransaction);
            isACompensationToQuittancer = true;
        }

        if (isACompensationToQuittancer) {
            warningVerifierCompensation = theMuscaSession
                    .getLabel("COMPENSATION_A_QUITTANCER_SUITE_A_MODIFICATION_INTERET");
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
        _propertyMandatory(statement.getTransaction(), getIdInteretMoratoire(), getSession().getLabel("7355"));

        _propertyMandatory(statement.getTransaction(), getIdJournalCalcul(), getSession().getLabel("7356"));

        _propertyMandatory(statement.getTransaction(), getIdGenreInteret(), getSession().getLabel("7357"));
        _propertyMandatory(statement.getTransaction(), getMotifcalcul(), getSession().getLabel("7358"));

        _propertyMandatory(statement.getTransaction(), getIdPlan(), getSession().getLabel("7360"));

        _propertyMandatory(statement.getTransaction(), getDateCalcul(), getSession().getLabel("7361"));
        _checkDate(statement.getTransaction(), getDateCalcul(), getSession().getLabel("7361"));

        verifRubrique(statement);

        // Erreurs conceranant les types d'interet tardifs
        if (getIdGenreInteret().equals(CAGenreInteret.CS_TYPE_TARDIF)) {
            // Au moins section ou facture
            if (JadeStringUtil.isIntegerEmpty(getIdSection()) && JadeStringUtil.isIntegerEmpty(getIdSectionFacture())) {
                _addError(statement.getTransaction(), getSession().getLabel("7080"));
            }
            // Au moins section ou section facture
            if (JadeStringUtil.isIntegerEmpty(getIdSection()) && JadeStringUtil.isIntegerEmpty(getIdSectionFacture())) {
                _addError(statement.getTransaction(), getSession().getLabel("7366"));
            }
        }
        // Vérifier les dates
        if (!JAUtil.isDateEmpty(getDateDecision())) {
            _checkDate(statement.getTransaction(), getDateDecision(), getSession().getLabel("7362"));
        }
        if (!JAUtil.isDateEmpty(getDateFacturation())) {
            _checkDate(statement.getTransaction(), getDateFacturation(), getSession().getLabel("7363"));
        }
        if (!JAUtil.isDateEmpty(getDateLettre())) {
            _checkDate(statement.getTransaction(), getDateLettre(), getSession().getLabel("7364"));
        }
        if (!JAUtil.isDateEmpty(getDateRecours())) {
            _checkDate(statement.getTransaction(), getDateRecours(), getSession().getLabel("7365"));
        }
        // Vérifier la section si saisie
        if (!JadeStringUtil.isIntegerEmpty(getIdSection()) && !getIdSection().equals(getIdSectionFacture())
                && (getSection() == null)) {
            _addError(statement.getTransaction(), getSession().getLabel("5126"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writeAlternateKey(BStatement, int)
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        if (alternateKey == CAInteretMoratoire.AK_FACTURE_JOURNAL) {
            statement.writeKey(CAInteretMoratoire.FIELD_IDSECTIONFACTURE,
                    this._dbWriteNumeric(statement.getTransaction(), getIdSectionFacture(), ""));
            statement.writeKey(CAInteretMoratoire.FIELD_IDJOUFAC,
                    this._dbWriteNumeric(statement.getTransaction(), getIdJournalFacturation(), ""));
        } else {
            throw new Exception("Unknown alternate key " + alternateKey);
        }
    }

    /**
	 *
	 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CAInteretMoratoire.FIELD_IDINTERETMORATOIRE,
                this._dbWriteNumeric(statement.getTransaction(), getIdInteretMoratoire(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CAInteretMoratoire.FIELD_IDINTERETMORATOIRE,
                this._dbWriteNumeric(statement.getTransaction(), getIdInteretMoratoire(), "idInteretMoratoire"));
        statement.writeField(CAInteretMoratoire.FIELD_IDSECTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdSection(), "idSection"));
        statement.writeField(CAInteretMoratoire.FIELD_IDSECTIONFACTURE,
                this._dbWriteNumeric(statement.getTransaction(), getIdSectionFacture(), "idSectionFacture"));
        statement.writeField(CAInteretMoratoire.FIELD_IDJOURNALCALCUL,
                this._dbWriteNumeric(statement.getTransaction(), getIdJournalCalcul(), "idJournalCalcul"));
        statement.writeField(CAInteretMoratoire.FIELD_DATECALCUL,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateCalcul(), "dateCalcul"));
        statement.writeField(CAInteretMoratoire.FIELD_DATELETTRE,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateLettre(), "dateLettre"));
        statement.writeField(CAInteretMoratoire.FIELD_DATEDECISION,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateDecision(), "dateDecision"));
        statement.writeField(CAInteretMoratoire.FIELD_DATEFACTURATION,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateFacturation(), "dateFacturation"));
        statement.writeField(CAInteretMoratoire.FIELD_DATERECOURS,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateRecours(), "dateRecours"));
        statement.writeField(CAInteretMoratoire.FIELD_MOTIFCALCUL,
                this._dbWriteNumeric(statement.getTransaction(), getMotifcalcul(), "motifcalcul"));
        statement.writeField(CAInteretMoratoire.FIELD_IDELEFAC,
                this._dbWriteNumeric(statement.getTransaction(), getIdElementFacturation(), "idElementFacturation"));
        statement.writeField(CAInteretMoratoire.FIELD_IDJOUFAC,
                this._dbWriteNumeric(statement.getTransaction(), getIdJournalFacturation(), "idJournalFacturation"));
        statement.writeField(CAInteretMoratoire.FIELD_IDGENREINTERET,
                this._dbWriteNumeric(statement.getTransaction(), getIdGenreInteret(), "idGenreInteret"));
        statement.writeField(CAInteretMoratoire.FIELD_IDRUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), "idRubrique"));
        statement.writeField(CAInteretMoratoire.FIELD_IDPLAN,
                this._dbWriteNumeric(statement.getTransaction(), getIdPlan(), "idPlan"));
        statement.writeField(CAInteretMoratoire.FIELD_REMARQUE,
                this._dbWriteString(statement.getTransaction(), getRemarque(), "remarque"));
        statement.writeField(CAInteretMoratoire.FIELD_NUMEROFACTUREGROUPE,
                this._dbWriteNumeric(statement.getTransaction(), getNumeroFactureGroupe(), "numeroFactureGroupe"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.05.2003 18:40:40)
     * 
     * @return globaz.framework.util.FWCurrency
     */
    public FWCurrency calculeTotalInteret() {
        try {
            CADetailInteretMoratoireManager mgr = new CADetailInteretMoratoireManager();
            mgr.setSession(getSession());
            mgr.setForIdInteretMoratoire(getIdInteretMoratoire());
            return new FWCurrency(mgr.getSum(CADetailInteretMoratoire.FIELD_MONTANTINTERET).doubleValue());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public CACompteAnnexe getCompteAnnexe() {
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setISession(getSession());
        compteAnnexe.setIdCompteAnnexe(getIdCompteAnnexe());
        try {
            compteAnnexe.retrieve();
            if (compteAnnexe.isNew()) {
                compteAnnexe = null;
            }
        } catch (Exception e) {
            compteAnnexe = null;
        }

        return compteAnnexe;
    }

    public String getDateCalcul() {
        return dateCalcul;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public String getDateFacturation() {
        return dateFacturation;
    }

    public String getDateLettre() {
        return dateLettre;
    }

    public String getDateRecours() {
        return dateRecours;
    }

    /**
     * @return
     */
    public String getDomaine() {
        return domaine;
    }

    /**
     * @return
     */
    public ArrayList getElementEntete() {
        return elementEntete;
    }

    public FAEnteteFacture getEnteteFacture() {
        if (enteteFacture == null) {
            try {
                enteteFacture = new FAEnteteFacture();
                enteteFacture.setSession(getSession());
                enteteFacture.setIdEntete(getIdSectionFacture());
                enteteFacture.retrieve();
            } catch (Exception e) {
                enteteFacture = null;
            }
        }
        return enteteFacture;
    }

    public String getIdCompteAnnexe() {
        if (!isDomaineCA()) {
            CACompteAnnexe cpt = new CACompteAnnexe();
            cpt.setSession(getSession());
            cpt.setIdExterneRole(getEnteteFacture().getIdExterneRole());
            cpt.setIdRole(getEnteteFacture().getIdRole());
            cpt.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
            try {
                cpt.retrieve();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return cpt.getIdCompteAnnexe();
        }
        if (JadeStringUtil.isBlank(idCompteAnnexe)) {
            try {
                idCompteAnnexe = getSection().getCompteAnnexe().getIdCompteAnnexe();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return idCompteAnnexe;
    }

    public String getIdElementFacturation() {
        return idElementFacturation;
    }

    /**
     * @return
     */
    public String getIdExterneRoleEcran() {
        if (JadeStringUtil.isBlank(idExterneRoleEcran)) {
            idExterneRoleEcran = isDomaineCA() ? getCompteAnnexe().getIdExterneRole() : getEnteteFacture()
                    .getIdExterneRole();
        }
        return idExterneRoleEcran;
    }

    public String getIdExterneRubriqueEcran() {
        // if (JadeStringUtil.isBlank(idExterneRubriqueEcran)) {
        if (!JadeStringUtil.isIntegerEmpty(idRubrique)) {
            try {
                idExterneRubriqueEcran = getRubrique().getIdExterne();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            idExterneRubriqueEcran = "";
        }
        // }

        return idExterneRubriqueEcran;
    }

    public String getIdExterneSectionEcran() {
        if (JadeStringUtil.isBlank(idExterneSectionEcran)) {
            try {
                idExterneSectionEcran = isDomaineCA() ? getSection().getIdExterne() : getEnteteFacture()
                        .getIdExterneFacture();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return idExterneSectionEcran;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.05.2003 13:35:22)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdGenreInteret() {
        return idGenreInteret;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdInteretMoratoire() {
        return idInteretMoratoire;
    }

    public String getIdJournalCalcul() {
        return idJournalCalcul;
    }

    public String getIdJournalFacturation() {
        return idJournalFacturation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.05.2003 13:57:54)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdPlan() {
        return idPlan;
    }

    /**
     * @return
     */
    public String getIdRoleEcran() {
        if (JadeStringUtil.isBlank(idRoleEcran)) {
            idExterneRoleEcran = isDomaineCA() ? getCompteAnnexe().getIdRole() : getEnteteFacture().getIdRole();
        }
        return idExterneRoleEcran;
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * Contient soit l'idSection (intérêt tardif), soit l'idEntête facture (IM arriéré) Date de création : (17.05.2003
     * 13:35:41)
     * 
     * @return java.lang.String
     */
    public String getIdSection() {
        return idSection;
    }

    public String getIdSectionFacture() {
        return idSectionFacture;
    }

    public CAJournal getJournal() {
        if (journal == null) {
            journal = new CAJournal();
            journal.setISession(getSession());
            journal.setIdJournal(getIdJournalCalcul());
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

    public String getJournalLibelle() {
        String libelle = "";
        if (isDomaineCA()) {
            CAJournal journal = getJournal();
            if (journal != null) {
                libelle = journal.getLibelle();
            }
        } else {
            FAPassage passage = null;
            try {
                passage = new FAPassage();
                passage.setSession(getSession());
                passage.setIdPassage(getIdJournalFacturation());
                passage.retrieve();
                libelle = passage.getLibelle();
            } catch (Exception e) {
                enteteFacture = null;
                libelle = "";
            }
        }
        return libelle;
    }

    public String getMotifcalcul() {
        return motifcalcul;
    }

    public String getNumeroFactureGroupe() {
        return numeroFactureGroupe;
    }

    public FAPassage getPassage() {
        if (passage == null) {
            if (JadeStringUtil.isBlank(getIdJournalFacturation()) || getIdJournalFacturation().equals("0")) {
                return null;
            }
            passage = new FAPassage();
            passage.setISession(getSession());
            passage.setIdPassage(getIdJournalFacturation());
            try {
                passage.retrieve();
            } catch (Exception e) {
                return null;
            }
        }
        return passage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.05.2003 18:33:22)
     * 
     * @return globaz.osiris.db.interets.CAPlanCalculInteret
     * @param transaction
     *            globaz.globall.db.BTransaction
     */
    public CAPlanCalculInteret getPlan(BTransaction transaction) {
        // Charger si vide
        if ((plan == null) && !JadeStringUtil.isIntegerEmpty(getIdPlan())) {
            try {
                section = new CASection();
                plan.setSession(getSession());
                plan.setIdPlanCalculInteret(getIdPlan());
                plan.retrieve(transaction);
                if (plan.isNew()) {
                    plan = null;
                }
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.addErrors(e.getMessage());
                }
                e.printStackTrace();
                plan = null;
            }
        }
        return plan;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.05.2003 15:19:00)
     * 
     * @return java.lang.String
     */
    public java.lang.String getRemarque() {
        return remarque;
    }

    public CARubrique getRubrique() {
        // Charger si vide
        if ((rubrique == null) && !JadeStringUtil.isBlank(idRubrique)) {
            try {
                rubrique = new CARubrique();
                rubrique.setSession(getSession());
                rubrique.setIdRubrique(idRubrique);
                rubrique.retrieve();
                if (rubrique.isNew()) {
                    rubrique = null;
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
                rubrique = null;
            }
        }

        return rubrique;
    }

    /**
     * ATTENTION ! a utiliser que pour les IM tardif, sinon, c'est la mauvaise section qui est renvoyée !!
     * 
     * Voir getIdSection()
     * 
     * Date de création : (17.05.2003 13:23:38)
     * 
     * @return CASection
     * @param transaction
     *            globaz.globall.db.BTransaction
     */
    public CASection getSection() {
        // Charger si vide
        if ((section == null) && !JadeStringUtil.isIntegerEmpty(getIdSection())) {
            try {
                section = new CASection();
                section.setSession(getSession());
                section.setIdSection(getIdSection());
                section.retrieve();

                if (section.isNew()) {
                    section = null;
                }

            } catch (Exception e) {
                JadeLogger.error(this, e);
                section = null;
            }
        }
        return section;
    }

    /**
     * Retourne le status de l'intéret
     */
    public String getStatus() {
        return CAInteretMoratoire.getStatus(getPassage(), getMotifcalcul());
    }

    public String getWarningVerifierCompensation() {
        return warningVerifierCompensation;
    }

    // Inforom321 - suppression de ATTENTE_PAIEMENT
    // public boolean isAttentePaiement() {
    // return this.getMotifcalcul().equalsIgnoreCase(CAInteretMoratoire.CS_ATTENTE_PAIEMENT);
    // }

    public boolean isAControler() {
        return getMotifcalcul().equalsIgnoreCase(CAInteretMoratoire.CS_A_CONTROLER);
    }

    public boolean isDomaineCA() {
        return domaine.equals("CA");
    }

    public boolean isExempte() {
        return getMotifcalcul().equalsIgnoreCase(CAInteretMoratoire.CS_EXEMPTE);
    }

    public boolean isManuel() {
        return getMotifcalcul().equalsIgnoreCase(CAInteretMoratoire.CS_MANUEL);
    }

    public boolean isSoumis() {
        return getMotifcalcul().equalsIgnoreCase(CAInteretMoratoire.CS_SOUMIS);
    }

    public boolean isTardif() {
        return getIdGenreInteret().equals(CAGenreInteret.CS_TYPE_TARDIF);
    }

    public void setDateCalcul(String newDateCalcul) {
        dateCalcul = newDateCalcul;
    }

    public void setDateDecision(String newDateDecision) {
        dateDecision = newDateDecision;
    }

    public void setDateFacturation(String newDateFacturation) {
        dateFacturation = newDateFacturation;
    }

    public void setDateLettre(String newDateLettre) {
        dateLettre = newDateLettre;
    }

    public void setDateRecours(String newDateRecours) {
        dateRecours = newDateRecours;
    }

    /**
     * @param string
     */
    public void setDomaine(String string) {
        domaine = string;
    }

    public void setEnteteFacture(FAEnteteFacture newEnteteFacture) {
        enteteFacture = newEnteteFacture;
    }

    public void setIdElementFacturation(String newIdElementFacturation) {
        idElementFacturation = newIdElementFacturation;
    }

    /**
     * @param string
     */
    public void setIdExterneRoleEcran() {
        try {
            idExterneRoleEcran = getSection().getCompteAnnexe().getIdExterneRole();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setIdExterneRoleEcran(String string) {
        idExterneRoleEcran = string;
    }

    /**
     * @param string
     */
    public void setIdExterneRubriqueEcran(String string) {
        idExterneRubriqueEcran = string;
    }

    /**
     * @param string
     */
    public void setIdExterneSectionEcran(String string) {
        idExterneSectionEcran = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.05.2003 13:35:22)
     * 
     * @param newIdGenreInteret
     *            java.lang.String
     */
    public void setIdGenreInteret(java.lang.String newIdGenreInteret) {
        idGenreInteret = newIdGenreInteret;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    public void setIdInteretMoratoire(String newIdInteretMoratoire) {
        idInteretMoratoire = newIdInteretMoratoire;
    }

    public void setIdJournalCalcul(String newIdJournalCalcul) {
        idJournalCalcul = newIdJournalCalcul;
    }

    public void setIdJournalFacturation(String newIdJournalFacturation) {
        idJournalFacturation = newIdJournalFacturation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.05.2003 13:57:54)
     * 
     * @param newIdPlan
     *            java.lang.String
     */
    public void setIdPlan(java.lang.String newIdPlan) {
        idPlan = newIdPlan;
    }

    /**
     * @param string
     */
    public void setIdRoleEcran(String string) {
        idRoleEcran = string;
    }

    public void setIdRubrique(java.lang.String newIdRubrique) {
        idRubrique = newIdRubrique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.05.2003 13:35:41)
     * 
     * @param newIdRubrique
     *            java.lang.String
     */
    public void setIdSection(String newIdSection) {
        idSection = newIdSection;
        section = null;
    }

    public void setIdSectionFacture(String newIdSectionFacture) {
        idSectionFacture = newIdSectionFacture;
    }

    public void setMotifcalcul(String newMotifcalcul) {
        motifcalcul = newMotifcalcul;
    }

    public void setNumeroFactureGroupe(String numeroFactureGroupe) {
        this.numeroFactureGroupe = numeroFactureGroupe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.05.2003 15:19:00)
     * 
     * @param newRemarque
     *            java.lang.String
     */
    public void setRemarque(java.lang.String newRemarque) {
        remarque = newRemarque;
    }

    /**
     * Répercute les modification faites manuellement aux détails d'intérêts moratoires sur les afact de factures
     * correspondant
     */
    public void updateAfact(BTransaction transaction) throws Exception {
        // On vérifie que idJournalFacturation et idSectionFacture soit
        // renseingés
        if (JadeStringUtil.isIntegerEmpty(getIdJournalFacturation())
                || JadeStringUtil.isIntegerEmpty(getIdSectionFacture())) {
            return;
        }

        BSession muscaSession = (BSession) GlobazServer.getCurrentSystem()
                .getApplication(globaz.musca.application.FAApplication.DEFAULT_APPLICATION_MUSCA)
                .newSession(getSession());

        FAAfactManager manager = new FAAfactManager();
        manager.setSession(muscaSession);

        manager.setForIdPassage(getIdJournalFacturation());
        manager.setForReferenceExterne(getIdInteretMoratoire());

        manager.find(transaction);

        if (manager.isEmpty()) {
            if (isSoumis() || isManuel()) {
                FAInteretGenericUtil.facturer(muscaSession, transaction, getSession(), getPassage(),
                        getIdSectionFacture(), this, null);

                manager.find(transaction);

                if (manager.getSize() != 1) {
                    throw new Exception("no or more than one afact for interet(id:" + getIdInteretMoratoire()
                            + ") not implemented in CAInteretMoratoire.updateAfact");
                }

                _setCompensationAQuittancer(((FAAfact) manager.getEntity(0)).getIdEnteteFacture(), muscaSession,
                        transaction);
            }

            return;
        } else {
            // Bug 5771
            FAAfact afact = null;
            for (int i = 0; i < manager.getSize(); i++) {
                afact = (FAAfact) manager.getEntity(i);
                _setCompensationAQuittancer(afact.getIdEnteteFacture(), muscaSession, transaction);
                // Si le motif du calcul de la décision n'est plus soumis on
                // supprime l'afact correspondant
                if (!isSoumis() && !isManuel()) {
                    if (CAApplication.getApplicationOsiris().getCAParametres().isInteretSurCotArrSurSectSeparee()) {
                        afact._getEnteteFacture(transaction).setNonImprimable(new Boolean(true));
                        afact._getEnteteFacture(transaction).update(transaction);
                    }

                    afact.delete(transaction);
                } else {
                    // on verifie la présence de la décision
                    this.retrieve(transaction);
                    // si la décision a été supprimée, on supprime l'afact
                    if (isNew()) {
                        if (CAApplication.getApplicationOsiris().getCAParametres().isInteretSurCotArrSurSectSeparee()) {
                            afact._getEnteteFacture(transaction).setNonImprimable(new Boolean(true));
                            afact._getEnteteFacture(transaction).update(transaction);
                        }

                        afact.delete(transaction);
                    } else {
                        // sinon on met à jour le montant de l'afact
                        afact.setMontantFacture(calculeTotalInteret().toString());
                        afact.update(transaction);
                    }
                }
            }
        }
    }

    /**
     * Répercute les modification faites manuellement aux détails d'intérêts moratoires sur l'entête de facture
     * correspondante
     */
    public void updateEntete(BTransaction transaction) throws Exception {
        // On vérifie que idJournalFacturation et idSectionFacture soit
        // renseingés
        if (JadeStringUtil.isIntegerEmpty(getIdJournalFacturation())
                || JadeStringUtil.isIntegerEmpty(getIdSectionFacture())) {
            return;
        }

        BSession muscaSession = (BSession) GlobazServer.getCurrentSystem()
                .getApplication(globaz.musca.application.FAApplication.DEFAULT_APPLICATION_MUSCA)
                .newSession(getSession());

        FAAfactManager manager = new FAAfactManager();
        manager.setSession(muscaSession);

        manager.setForIdPassage(getIdJournalFacturation());
        manager.setForReferenceExterne(getIdInteretMoratoire());

        manager.find(transaction);
        // Pour éviter un conflit de spy si l'entête est déjà mise à jour lors
        // de la suppression d'un afact.
        enteteFacture = null;
        FAEnteteFacture entity = getEnteteFacture();

        if (manager.isEmpty()) {
            if (isExempte() && getIdGenreInteret().equals(CAGenreInteret.CS_TYPE_COTISATIONS_PERSONNELLES)) {
                if (entity != null) {
                    entity.setRemarque("");
                    entity.update(transaction);
                }
            }

            return;
        }
        return;
    }

    /**
     * Vérifie si la rubrique à été renseignée
     * 
     * @param transaction
     */
    private void verifRubrique(BStatement statement) {
        BTransaction transaction = statement.getTransaction();

        // Si le champ de rubrique (idExterneRubriqueEcran) n'a pas été
        // renseignée
        if (JadeStringUtil.isBlank(getIdExterneRubriqueEcran())) {
            setIdRubrique("");
            _addError(transaction, getSession().getLabel("7359"));
        } else {
            // création d'un objet temporaire pour la vérification
            CARubrique RubTemp = new CARubrique();
            RubTemp.setSession(getSession());
            RubTemp.setAlternateKey(APIRubrique.AK_IDEXTERNE);
            RubTemp.setIdExterne(getIdExterneRubriqueEcran());

            // Lecture
            try {
                RubTemp.retrieve(transaction);
                // si la rubrique n'existe pas
                if (RubTemp.isNew()) {
                    setIdRubrique("");
                    _addError(transaction, getSession().getLabel("5115"));

                    // si la nature de la rubrique n'est pas 200009 ou 200010
                } else if (!RubTemp.getNatureRubrique().equalsIgnoreCase(
                        CAInteretMoratoire.CS_NATURERUBRIQUE_REMUNERATOIRE)
                        && !RubTemp.getNatureRubrique()
                                .equalsIgnoreCase(CAInteretMoratoire.CS_NATURERUBRIQUE_MORATOIRE)) {
                    setIdRubrique("");
                    _addError(transaction, getSession().getLabel("7382"));

                } else {
                    setIdRubrique(RubTemp.getIdRubrique());
                    rubrique = RubTemp;
                }
            } catch (Exception e) {
                setIdRubrique("");
                return;
            }
        }
    }

}
