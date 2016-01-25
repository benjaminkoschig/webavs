package globaz.helios.db.avs;

import globaz.globall.db.BManager;
import globaz.helios.db.interfaces.CGLibelle;
import globaz.helios.db.interfaces.CGLibelleInterface;
import globaz.helios.db.interfaces.ITreeListable;
import globaz.jade.client.util.JadeStringUtil;

public class CGPlanComptableAVS extends globaz.globall.db.BEntity implements ITreeListable, CGLibelleInterface,
        java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String TYPE_CLASSE = "2";
    public final static String TYPE_COMPTE = "3";
    public final static String TYPE_SECTEUR = "1";
    private java.lang.Boolean estClasse = new Boolean(false);
    private java.lang.String libelleDe = new String();

    private java.lang.String libelleFr = new String();

    private java.lang.String libelleIt = new String();
    private java.lang.String numeroCompteAVS = new String();
    private java.lang.String type = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CGPlanComptableAVS
     */
    public CGPlanComptableAVS() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CGPCAVP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        numeroCompteAVS = statement.dbReadNumeric("NUMERO");
        estClasse = statement.dbReadBoolean("ESTCLASSE");
        libelleFr = statement.dbReadString("LIBELLEFR");
        libelleDe = statement.dbReadString("LIBELLEDE");
        libelleIt = statement.dbReadString("LIBELLEIT");
        type = statement.dbReadNumeric("TYPE");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("NUMERO", _dbWriteNumeric(statement.getTransaction(), getNumeroCompteAVS(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("NUMERO",
                _dbWriteNumeric(statement.getTransaction(), getNumeroCompteAVS(), "numeroCompteAVS"));
        statement.writeField("ESTCLASSE", _dbWriteBoolean(statement.getTransaction(), isEstClasse(), "estClasse"));
        statement.writeField("LIBELLEFR", _dbWriteString(statement.getTransaction(), getLibelleFr(), "libelleFr"));
        statement.writeField("LIBELLEDE", _dbWriteString(statement.getTransaction(), getLibelleDe(), "libelleDe"));
        statement.writeField("LIBELLEIT", _dbWriteString(statement.getTransaction(), getLibelleIt(), "libelleIt"));
        statement.writeField("TYPE", _dbWriteNumeric(statement.getTransaction(), getType(), "type"));

    }

    @Override
    public BManager[] getChilds() {
        return null;
    }

    @Override
    public String getLibelle() {
        return CGLibelle.getLibelleApp(this);
    }

    @Override
    public java.lang.String getLibelleDe() {
        return libelleDe;
    }

    @Override
    public java.lang.String getLibelleFr() {
        return libelleFr;
    }

    @Override
    public java.lang.String getLibelleIt() {
        return libelleIt;
    }

    /**
     * Getter
     */
    public java.lang.String getNumeroCompteAVS() {
        return numeroCompteAVS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 16:01:49)
     * 
     * @return java.lang.String
     */
    public java.lang.String getType() {
        return type;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 17:31:27)
     * 
     * @return boolean
     */
    public boolean hasAllLibelleEmpty() {

        return JadeStringUtil.isBlank(libelleDe + libelleFr + libelleIt);
    }

    public java.lang.Boolean isEstClasse() {
        return estClasse;
    }

    public void setEstClasse(java.lang.Boolean newEstClasse) {
        estClasse = newEstClasse;
    }

    public void setLibelleDe(java.lang.String newLibelleDe) {
        libelleDe = newLibelleDe;
    }

    public void setLibelleFr(java.lang.String newLibelleFr) {
        libelleFr = newLibelleFr;
    }

    public void setLibelleIt(java.lang.String newLibelleIt) {
        libelleIt = newLibelleIt;
    }

    /**
     * Setter
     */
    public void setNumeroCompteAVS(java.lang.String newNumeroCompteAVS) {
        numeroCompteAVS = newNumeroCompteAVS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 16:01:49)
     * 
     * @param newType
     *            java.lang.String
     */
    public void setType(java.lang.String newType) {
        type = newType;
    }
}
