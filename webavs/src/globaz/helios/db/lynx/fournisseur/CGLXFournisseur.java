package globaz.helios.db.lynx.fournisseur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.tiers.TITiers;

/**
 * La classe permettant de retrouver une entité Fournisseur (retrieve only). <br>
 * Copie de LX afin d'éviter les références récursives entre projet.
 * 
 * @author sco
 */
public class CGLXFournisseur extends BEntity {

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
    // Copy from LXTiersService
    public static final String ROLE_FOURNISSEUR = "517043";

    public static final String TABLE_LXFOURP = "LXFOURP";

    private static TITiers getTiers(BSession session, BTransaction transaction, String idTiers) throws Exception {
        TITiers tiers = new TITiers();
        tiers.setSession(session);

        tiers.setIdTiers(idTiers);

        tiers.retrieve(transaction);

        if (transaction != null && transaction.hasErrors()) {
            throw new Exception(transaction.getErrors().toString());
        }

        if (tiers.isNew()) {
            throw new Exception(session.getLabel("FOURNISSEUR_TIERS_NOT_FOUND"));
        }

        return tiers;
    }

    private String complement = new String();
    private String csCategorie = new String();
    private String csMotifBlocage = new String();
    private Boolean estBloque = new Boolean(false);
    private String idExterne = new String();
    private String idFournisseur = new String();
    private String idTiers = new String();
    private String nom = new String();

    private String noTva = new String();

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        if (JadeStringUtil.isBlank(getNom())) {
            setNom(getTiers(getSession(), transaction, idTiers).getDesignation1());
        }
        if (JadeStringUtil.isBlank(getComplement())) {
            setComplement(getTiers(getSession(), transaction, idTiers).getDesignation2());
        }
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
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Do nohing
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
        // Do nohing
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
