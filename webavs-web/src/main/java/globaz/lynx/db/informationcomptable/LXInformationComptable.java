package globaz.lynx.db.informationcomptable;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.utils.LXUtils;

public class LXInformationComptable extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_CSCODEISOMONNAIE = "CSCODEISOMONNAIE";
    public static final String FIELD_CSCODETVA = "CSCODETVA";
    public static final String FIELD_ECHEANCE = "ECHEANCE";
    public static final String FIELD_IDCOMPTECHARGE = "IDCOMPTECHARGE";
    public static final String FIELD_IDCOMPTECREANCE = "IDCOMPTECREANCE";
    public static final String FIELD_IDCOMPTEESCOMPTE = "IDCOMPTEESCOMPTE";
    public static final String FIELD_IDCOMPTETVA = "IDCOMPTETVA";
    public static final String FIELD_IDFOURNISSEUR = "IDFOURNISSEUR";
    public static final String FIELD_IDINFORMATION = "IDINFORMATION";
    public static final String FIELD_IDSOCIETE = "IDSOCIETE";
    public static final String FIELD_LIBELLEFACTURE = "LIBELLEFACTURE";

    public static final String TABLE_LXINFCP = "LXINFCP";

    private String csCodeIsoMonnaie = "";
    private String csCodeTva = "";
    private String echeance = "";
    private String idCompteCharge = "";
    private String idCompteCreance = "";
    private String idCompteEscompte = "";
    private String idCompteTva = "";
    private String idFournisseur = "";
    private String idInformation = "";
    private String idSociete = "";
    private String libelleFacture = "";
    private String nomSociete = "";

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdInformation(_incCounter(transaction, getIdInformation()));

        // Vérification de l'unicité du couple idSociete / idFournisseur
        LXInformationComptableManager manager = new LXInformationComptableManager();
        manager.setSession(getSession());
        manager.setForIdFournisseur(getIdFournisseur());
        manager.setForIdSociete(getIdSociete());
        manager.find(transaction);

        if (manager.size() > 0) {
            _addError(transaction, getSession().getLabel("VAL_UNICITE_FOURNISSEUR_SOCIETE"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {

        LXInformationComptable infoCompt = new LXInformationComptable();
        infoCompt.setIdInformation(getIdInformation());
        infoCompt.retrieve(transaction);

        if (!infoCompt.getIdSociete().equals(getIdSociete())) {

            LXInformationComptable infoComptBis = new LXInformationComptable();
            infoComptBis.setIdFournisseur(getIdFournisseur());
            infoComptBis.setIdSociete(getIdSociete());
            infoComptBis.retrieve(transaction);

            if (JadeStringUtil.isIntegerEmpty(infoComptBis.getIdInformation())) {
                _addError(transaction, getSession().getLabel("INFO_COMPTABLE_UPDATE_IMPOSSIBLE"));
            }
        }

    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_LXINFCP;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdInformation(statement.dbReadNumeric(FIELD_IDINFORMATION));
        setIdSociete(statement.dbReadNumeric(FIELD_IDSOCIETE));
        setIdFournisseur(statement.dbReadNumeric(FIELD_IDFOURNISSEUR));
        setEcheance(statement.dbReadNumeric(FIELD_ECHEANCE));
        setLibelleFacture(statement.dbReadString(FIELD_LIBELLEFACTURE));
        setIdCompteCreance(statement.dbReadNumeric(FIELD_IDCOMPTECREANCE));
        setIdCompteCharge(statement.dbReadNumeric(FIELD_IDCOMPTECHARGE));
        setCsCodeIsoMonnaie(statement.dbReadNumeric(FIELD_CSCODEISOMONNAIE));
        setIdCompteTva(statement.dbReadNumeric(FIELD_IDCOMPTETVA));
        setCsCodeTva(statement.dbReadNumeric(FIELD_CSCODETVA));
        setIdCompteEscompte(statement.dbReadNumeric(FIELD_IDCOMPTEESCOMPTE));
        try {
            // Recuperation du nom et complement sur la table des tiers
            setNomSociete(LXUtils.getNomComplet(statement.dbReadString("HTLDE1"), statement.dbReadString("HTLDE2")));
        } catch (Exception e) {
            // nothing
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        // Controle de l'id societe
        if (JadeStringUtil.isIntegerEmpty(getIdSociete())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_SOCIETE"));
        }
        // Controle de l'id fournisseur
        if (JadeStringUtil.isIntegerEmpty(getIdFournisseur())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_FOURNISSEUR"));
        }
        // Controle de l'id d'un compte de créance
        if (JadeStringUtil.isIntegerEmpty(getIdCompteCreance())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_COMPTE_CREANCE"));
        }

    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELD_IDINFORMATION, _dbWriteNumeric(statement.getTransaction(), getIdInformation(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELD_IDINFORMATION,
                _dbWriteNumeric(statement.getTransaction(), getIdInformation(), "idInformation"));
        statement.writeField(FIELD_IDSOCIETE, _dbWriteNumeric(statement.getTransaction(), getIdSociete(), "idSociete"));
        statement.writeField(FIELD_IDFOURNISSEUR,
                _dbWriteNumeric(statement.getTransaction(), getIdFournisseur(), "idFournisseur"));
        statement.writeField(FIELD_ECHEANCE, _dbWriteNumeric(statement.getTransaction(), getEcheance(), "echeance"));
        statement.writeField(FIELD_LIBELLEFACTURE,
                _dbWriteString(statement.getTransaction(), getLibelleFacture(), "libelleFacture"));
        statement.writeField(FIELD_IDCOMPTECREANCE,
                _dbWriteNumeric(statement.getTransaction(), getIdCompteCreance(), "idFournisseur"));
        statement.writeField(FIELD_IDCOMPTECHARGE,
                _dbWriteNumeric(statement.getTransaction(), getIdCompteCharge(), "idCompteCreance"));
        statement.writeField(FIELD_CSCODEISOMONNAIE,
                _dbWriteNumeric(statement.getTransaction(), getCsCodeIsoMonnaie(), "csCodeIsoMonnaie"));
        statement.writeField(FIELD_IDCOMPTETVA,
                _dbWriteNumeric(statement.getTransaction(), getIdCompteTva(), "idCompteTva"));
        statement.writeField(FIELD_CSCODETVA, _dbWriteNumeric(statement.getTransaction(), getCsCodeTva(), "csCodeTva"));
        statement.writeField(FIELD_IDCOMPTEESCOMPTE,
                _dbWriteNumeric(statement.getTransaction(), getIdCompteEscompte(), "idCompteEscompte"));
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getCsCodeIsoMonnaie() {
        return csCodeIsoMonnaie;
    }

    public String getCsCodeTva() {
        return csCodeTva;
    }

    public String getEcheance() {
        return echeance;
    }

    public String getIdCompteCharge() {
        return idCompteCharge;
    }

    public String getIdCompteCreance() {
        return idCompteCreance;
    }

    public String getIdCompteEscompte() {
        return idCompteEscompte;
    }

    public String getIdCompteTva() {
        return idCompteTva;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public String getIdInformation() {
        return idInformation;
    }

    public String getIdSociete() {
        return idSociete;
    }

    public String getLibelleFacture() {
        return libelleFacture;
    }

    public String getNomSociete() {
        return nomSociete;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setCsCodeIsoMonnaie(String csCodeIsoMonnaie) {
        this.csCodeIsoMonnaie = csCodeIsoMonnaie;
    }

    public void setCsCodeTva(String csCodeTva) {
        this.csCodeTva = csCodeTva;
    }

    public void setEcheance(String echeance) {
        this.echeance = echeance;
    }

    public void setIdCompteCharge(String idCompteCharge) {
        this.idCompteCharge = idCompteCharge;
    }

    public void setIdCompteCreance(String idCompteCreance) {
        this.idCompteCreance = idCompteCreance;
    }

    public void setIdCompteEscompte(String idCompteEscompte) {
        this.idCompteEscompte = idCompteEscompte;
    }

    public void setIdCompteTva(String idCompteTva) {
        this.idCompteTva = idCompteTva;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public void setIdInformation(String idInformation) {
        this.idInformation = idInformation;
    }

    public void setIdSociete(String idSociete) {
        this.idSociete = idSociete;
    }

    public void setLibelleFacture(String libelleFacture) {
        this.libelleFacture = libelleFacture;
    }

    public void setNomSociete(String nomSociete) {
        this.nomSociete = nomSociete;
    }
}
