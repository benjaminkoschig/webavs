package globaz.lynx.db.fournisseur;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.application.LXApplication;
import globaz.lynx.db.informationcomptable.LXInformationComptableManager;
import globaz.lynx.db.section.LXSectionManager;
import globaz.lynx.service.tiers.LXTiersService;
import globaz.lynx.utils.LXFournisseurUtil;
import globaz.lynx.utils.LXUtils;
import globaz.pyxis.db.tiers.TIRole;
import globaz.pyxis.db.tiers.TIRoleManager;

/**
 * La classe définissant l'entité Fournisseur.
 * 
 * @author sco
 */
public class LXFournisseur extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_CSCATEGORIE = "CSCATEGORIE";
    public static final String FIELD_CSMOTIFBLOCAGE = "CSMOTIFBLOCAGE";
    public static final String FIELD_ESTBLOQUE = "ESTBLOQUE";
    public static final String FIELD_IDEXTERNE = "IDEXTERNE";
    public static final String FIELD_IDFOURNISSEUR = "IDFOURNISSEUR";
    public static final String FIELD_IDTIERS = "IDTIERS";
    public static final String FIELD_NOTVA = "NOTVA";

    public static final String TABLE_LXFOURP = "LXFOURP";

    public static final String TOUS = "0";

    private String complement = "";
    private String csCategorie = "";
    private String csMotifBlocage = "";
    private Boolean estBloque = new Boolean(false);
    private String idExterne = "";
    private String idFournisseur = "";
    private String idTiers = "";
    private String nom = "";
    private String noTva = "";

    /**
     * @see globaz.globall.db.BEntity#_afterAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {

        String today = JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY);

        // Ajout du rôle fournisseur dans tiers s'il n'existe pas encore
        TIRoleManager rMgr = new TIRoleManager();
        rMgr.setSession(getSession());
        rMgr.setForIdTiers(getIdTiers());
        rMgr.setForRole(LXTiersService.ROLE_FOURNISSEUR);
        rMgr.setForDateEntreDebutEtFin(today);
        if (rMgr.getCount() == 0) {
            // aucune rôle n'existe, en créer un
            TIRole role = new TIRole();
            role.setSession(getSession());
            role.setIdTiers(getIdTiers());
            role.setRole(LXTiersService.ROLE_FOURNISSEUR);
            role.setDebutRole(today);
            role.add(transaction);
        }

    }

    /**
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {

        // Suppression du role fournisseur dans tiers
        // On ne supprime pas réellement, on set uniquement la fin de validité
        TIRoleManager rMgr = new TIRoleManager();
        rMgr.setSession(getSession());
        rMgr.setForIdTiers(getIdTiers());
        rMgr.setForRole(LXTiersService.ROLE_FOURNISSEUR);
        rMgr.setForDateEntreDebutEtFin(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
        rMgr.find(transaction);

        if (rMgr.size() == 1) {
            TIRole role = (TIRole) rMgr.getFirstEntity();
            role.setFinRole(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
            role.update(transaction);
        } else {
            _addError(transaction, getSession().getLabel("FOURNISSEUR_DEPENDANCE_SECTION"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        if (JadeStringUtil.isBlank(getNom())) {
            setNom(LXTiersService.getNom(getSession(), transaction, getIdTiers()));
        }
        if (JadeStringUtil.isBlank(getComplement())) {
            setComplement(LXTiersService.getPrenom(getSession(), transaction, getIdTiers()));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdFournisseur(_incCounter(transaction, idFournisseur));

        // Test de l'unicité de l'id externe
        if (LXFournisseurUtil.isIdExterneExist(getSession(), transaction, getIdExterne())) {
            _addError(transaction, getSession().getLabel("VAL_UNICITE_ID_EXTERNE_FOUR"));
        }

        checkIdExterne(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        LXInformationComptableManager managerInfo = new LXInformationComptableManager();
        managerInfo.setSession(getSession());
        managerInfo.setForIdFournisseur(getIdFournisseur());
        managerInfo.find(transaction);
        if (!managerInfo.isEmpty()) {
            _addError(transaction, getSession().getLabel("FOURNISSEUR_DEPENDANCE_INFO_COMPT"));
        }

        LXSectionManager managerSection = new LXSectionManager();
        managerSection.setSession(getSession());
        managerSection.setForIdFournisseur(getIdFournisseur());
        managerSection.find(transaction);
        if (!managerSection.isEmpty()) {
            _addError(transaction, getSession().getLabel("FOURNISSEUR_DEPENDANCE_SECTION"));
        }

    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        // Test de l'unicité de l'id externe
        if (!LXFournisseurUtil.isIdExterneUnique(getSession(), transaction, getIdExterne())) {
            _addError(transaction, getSession().getLabel("VAL_UNICITE_ID_EXTERNE_FOUR"));
        }

        checkIdExterne(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_LXFOURP;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdFournisseur(statement.dbReadNumeric(FIELD_IDFOURNISSEUR));
        setIdExterne(statement.dbReadString(FIELD_IDEXTERNE));
        setCsCategorie(statement.dbReadNumeric(FIELD_CSCATEGORIE));
        setIdTiers(statement.dbReadNumeric(FIELD_IDTIERS));
        setNoTva(statement.dbReadString(FIELD_NOTVA));
        setEstBloque(statement.dbReadBoolean(FIELD_ESTBLOQUE));
        setCsMotifBlocage(statement.dbReadNumeric(FIELD_CSMOTIFBLOCAGE));

        try {
            // Recuperation du nom et complement sur la table des tiers
            setNom(statement.dbReadString("HTLDE1"));
            setComplement(statement.dbReadString("HTLDE2"));
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
        statement.writeKey(FIELD_IDFOURNISSEUR, _dbWriteNumeric(statement.getTransaction(), getIdFournisseur(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELD_IDFOURNISSEUR,
                _dbWriteNumeric(statement.getTransaction(), getIdFournisseur(), "idFournisseur"));
        statement.writeField(FIELD_IDEXTERNE, _dbWriteString(statement.getTransaction(), getIdExterne(), "idExterne"));
        statement.writeField(FIELD_CSCATEGORIE,
                _dbWriteNumeric(statement.getTransaction(), getCsCategorie(), "csCategorie"));
        statement.writeField(FIELD_IDTIERS, _dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
        statement.writeField(FIELD_NOTVA, _dbWriteString(statement.getTransaction(), getNoTva(), "noTva"));
        statement.writeField(
                FIELD_ESTBLOQUE,
                _dbWriteBoolean(statement.getTransaction(), getEstBloque(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "estBloque"));
        statement.writeField(FIELD_CSMOTIFBLOCAGE,
                _dbWriteNumeric(statement.getTransaction(), getCsMotifBlocage(), "motifBlocage"));
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
            application.getNumeroFournisseurFormatter().checkIdExterne(getSession(), getIdExterne());
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
        }
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getComplement() {
        return complement;
    }

    public String getCsCategorie() {
        return csCategorie;
    }

    public String getCsMotifBlocage() {
        return csMotifBlocage;
    }

    public Boolean getEstBloque() {
        return estBloque;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNom() {
        return nom;
    }

    /**
     * Return une concaténation séparé d'un espace du nom et du complément
     */
    public String getNomComplet() {
        return LXUtils.getNomComplet(getNom(), getComplement());
    }

    public String getNoTva() {
        return noTva;
    }

    public Boolean isEstBloque() {
        return estBloque;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public void setCsCategorie(String csCategorie) {
        this.csCategorie = csCategorie;
    }

    public void setCsMotifBlocage(String csMotifBlocage) {
        this.csMotifBlocage = csMotifBlocage;
    }

    public void setEstBloque(Boolean estBloque) {
        this.estBloque = estBloque;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNoTva(String noTva) {
        this.noTva = noTva;
    }
}
