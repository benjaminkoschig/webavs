package globaz.lynx.db.societesdebitrice;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.application.LXApplication;
import globaz.lynx.db.informationcomptable.LXInformationComptableManager;
import globaz.lynx.db.journal.LXJournalManager;
import globaz.lynx.db.organeexecution.LXOrganeExecutionManager;
import globaz.lynx.db.section.LXSectionManager;
import globaz.lynx.service.tiers.LXTiersService;
import globaz.lynx.utils.LXSocieteDebitriceUtil;
import globaz.lynx.utils.LXUtils;
import globaz.pyxis.db.tiers.TIRole;
import globaz.pyxis.db.tiers.TIRoleManager;

/**
 * La classe définissant l'entité Société débitrice.
 * 
 * @author sco
 */
public class LXSocieteDebitrice extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_IDEXTERNE = "IDEXTERNE";
    public static final String FIELD_IDMANDAT = "IDMANDAT";
    // Colonnes de la table
    public static final String FIELD_IDSOCIETE = "IDSOCIETE";
    public static final String FIELD_IDTIERS = "IDTIERS";
    public static final String FIELD_LECTUREOPTIQUE = "LECTUREOPTIQUE";

    // Nom de la table
    public static final String TABLE_LXSOCIP = "LXSOCIP";

    private String idExterne = "";
    private String idMandat = "";
    private String idSociete = "";
    private String idTiers = "";
    private Boolean lectureOptique = new Boolean(false);
    private String nom = "";

    /**
     * @see globaz.globall.db.BEntity#_afterAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        String today = JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY);

        // Ajout du rôle societe debitrice dans tiers s'il n'existe pas encore
        TIRoleManager rMgr = new TIRoleManager();
        rMgr.setSession(getSession());
        rMgr.setForIdTiers(getIdTiers());
        rMgr.setForRole(LXTiersService.ROLE_SOCIETE_DEBITRICE);
        rMgr.setForDateEntreDebutEtFin(today);
        if (rMgr.getCount() == 0) {
            // aucune rôle n'existe, en créer un
            TIRole role = new TIRole();
            role.setSession(getSession());
            role.setIdTiers(getIdTiers());
            role.setRole(LXTiersService.ROLE_SOCIETE_DEBITRICE);
            role.setDebutRole(today);
            role.add(transaction);
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        if (JadeStringUtil.isBlank(getNom())) {
            setNom(LXTiersService.getNomComplet(getSession(), transaction, getIdTiers()));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdSociete(this._incCounter(transaction, idSociete));

        // Test de l'unicité de l'id externe
        if (LXSocieteDebitriceUtil.isIdExterneExist(getSession(), transaction, getIdExterne())) {
            _addError(transaction, getSession().getLabel("VAL_UNICITE_ID_EXTERNE_SOCI"));
        }

        checkIdExterne(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {

        // On regarde si il n'y a pas d'informations comptable qui font
        // référence a cette société
        LXInformationComptableManager managerInfo = new LXInformationComptableManager();
        managerInfo.setSession(getSession());
        managerInfo.setForIdSociete(getIdSociete());
        managerInfo.find(transaction);
        if (!managerInfo.isEmpty()) {
            _addError(transaction, getSession().getLabel("SOCIETE_DEPENDANCE_INFO_COMPTABLE"));
        }

        // On regarde si il n'y a pas d'organe d'exécution qui font référence a
        // cette société
        LXOrganeExecutionManager managerOrgane = new LXOrganeExecutionManager();
        managerOrgane.setSession(getSession());
        managerOrgane.setForIdSocieteDebitrice(getIdSociete());
        managerOrgane.find(transaction);
        if (!managerOrgane.isEmpty()) {
            _addError(transaction, getSession().getLabel("SOCIETE_DEPENDANCE_ORGANE"));
        }

        // On regarde si il n'y a pas de journaux qui font référence a cette
        // société
        LXJournalManager managerJournal = new LXJournalManager();
        managerJournal.setSession(getSession());
        managerJournal.setForIdSociete(getIdSociete());
        managerJournal.find(transaction);
        if (!managerOrgane.isEmpty()) {
            _addError(transaction, getSession().getLabel("SOCIETE_DEPENDANCE_JOURNAL"));
        }

        LXSectionManager managerSection = new LXSectionManager();
        managerSection.setSession(getSession());
        managerSection.setForIdSociete(getIdSociete());
        managerSection.find(transaction);
        if (!managerSection.isEmpty()) {
            _addError(transaction, getSession().getLabel("SOCIETE_DEPENDANCE_SECTION"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        // Test de l'unicité de l'id externe
        if (!LXSocieteDebitriceUtil.isIdExterneUnique(getSession(), transaction, getIdExterne())) {
            _addError(transaction, getSession().getLabel("VAL_UNICITE_ID_EXTERNE_SOCI"));
        }

        checkIdExterne(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return LXSocieteDebitrice.TABLE_LXSOCIP;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdSociete(statement.dbReadNumeric(LXSocieteDebitrice.FIELD_IDSOCIETE));
        setIdExterne(statement.dbReadString(LXSocieteDebitrice.FIELD_IDEXTERNE));
        setIdMandat(statement.dbReadNumeric(LXSocieteDebitrice.FIELD_IDMANDAT));
        setIdTiers(statement.dbReadNumeric(LXSocieteDebitrice.FIELD_IDTIERS));
        setLectureOptique(statement.dbReadBoolean(LXSocieteDebitrice.FIELD_LECTUREOPTIQUE));

        try {
            // Recuperation du nom et complement sur la table des tiers
            setNom(LXUtils.getNomComplet(statement.dbReadString("HTLDE1"), statement.dbReadString("HTLDE2")));
        } catch (Exception e) {
            // nothing
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        // Controle de l'id externe
        if (JadeStringUtil.isBlank(getIdExterne())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_EXTERNE"));
        }
        // Controle de l'id mandat
        if (JadeStringUtil.isIntegerEmpty(getIdMandat())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_MANDAT"));
        }
        // Controle de l'id tiers
        if (JadeStringUtil.isIntegerEmpty(getIdTiers())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_TIERS"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(LXSocieteDebitrice.FIELD_IDSOCIETE,
                this._dbWriteNumeric(statement.getTransaction(), getIdSociete(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(LXSocieteDebitrice.FIELD_IDSOCIETE,
                this._dbWriteNumeric(statement.getTransaction(), getIdSociete(), "idSociete"));
        statement.writeField(LXSocieteDebitrice.FIELD_IDEXTERNE,
                this._dbWriteString(statement.getTransaction(), getIdExterne(), "idExterne"));
        statement.writeField(LXSocieteDebitrice.FIELD_IDMANDAT,
                this._dbWriteNumeric(statement.getTransaction(), getIdMandat(), "idMandat"));
        statement.writeField(LXSocieteDebitrice.FIELD_IDTIERS,
                this._dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
        statement.writeField(LXSocieteDebitrice.FIELD_LECTUREOPTIQUE, this._dbWriteBoolean(statement.getTransaction(),
                getLectureOptique(), BConstants.DB_TYPE_BOOLEAN_CHAR, "lectureOptique"));

    }

    /**
     * Contrôle du formatage de l'id externe.
     * 
     * @param transaction
     * @throws Exception
     */
    private void checkIdExterne(BTransaction transaction) throws Exception {
        LXApplication application = (LXApplication) GlobazServer.getCurrentSystem().getApplication(
                LXApplication.DEFAULT_APPLICATION_LYNX);
        try {
            application.getNumeroSocieteDebitriceFormatter().checkIdExterne(getSession(), getIdExterne());
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
        }
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdMandat() {
        return idMandat;
    }

    public String getIdSociete() {
        return idSociete;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public Boolean getLectureOptique() {
        return lectureOptique;
    }

    public String getNom() {
        return nom;
    }

    public Boolean isLectureOptique() {
        return lectureOptique;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdMandat(String idMandat) {
        this.idMandat = idMandat;
    }

    public void setIdSociete(String idSociete) {
        this.idSociete = idSociete;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setLectureOptique(Boolean lectureOptique) {
        this.lectureOptique = lectureOptique;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

}
