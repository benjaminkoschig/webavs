package globaz.helios.db.utils;

import java.util.Vector;

public class CGPolice extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static Vector getPoliceList(globaz.globall.db.BTransaction transaction) {

        Vector vList = new Vector();

        // ajoute un blanc
        String[] list = new String[2];
        list[0] = "";
        list[1] = "";
        vList.add(list);

        try {
            CGPoliceManager manager = new CGPoliceManager();
            manager.find(transaction);

            for (int i = 0; i < manager.size(); i++) {
                list = new String[2];
                CGPolice entity = (CGPolice) manager.getEntity(i);
                list[0] = entity.getIdPolice();
                list[1] = entity.getLibelleList();
                vList.add(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // si probleme, retourne list vide.
        }
        return vList;
    }

    private java.lang.Boolean enGras = new Boolean(false);
    private java.lang.Boolean enItallique = new Boolean(false);
    private java.lang.Boolean enSouligne = new Boolean(false);
    private java.lang.String idPolice = new String();
    private java.lang.String nomPolice = new String();

    // code systeme

    private java.lang.String taille = new String();

    /**
     * Commentaire relatif au constructeur AJPolice
     */
    public CGPolice() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CGPOLIP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idPolice = statement.dbReadNumeric("IDPOLICE");
        nomPolice = statement.dbReadString("NOMPOLICE");
        taille = statement.dbReadNumeric("TAILLE");
        enGras = statement.dbReadBoolean("ENGRAS");
        enSouligne = statement.dbReadBoolean("ENSOULIGNE");
        enItallique = statement.dbReadBoolean("ENITALLIQUE");
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
        statement.writeKey("IDPOLICE", _dbWriteNumeric(statement.getTransaction(), getIdPolice(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDPOLICE", _dbWriteNumeric(statement.getTransaction(), getIdPolice(), "idpolice"));
        statement.writeField("NOMPOLICE", _dbWriteString(statement.getTransaction(), getNomPolice(), "nompolice"));
        statement.writeField("TAILLE", _dbWriteNumeric(statement.getTransaction(), getTaille(), "taille"));
        statement.writeField("ENGRAS", _dbWriteBoolean(statement.getTransaction(), isEnGras(), "engras"));
        statement.writeField("ENSOULIGNE", _dbWriteBoolean(statement.getTransaction(), isEnSouligne(), "ensouligne"));
        statement
                .writeField("ENITALLIQUE", _dbWriteBoolean(statement.getTransaction(), isEnItallique(), "enitallique"));
    }

    /**
     * Getter
     */
    public java.lang.String getIdPolice() {
        return idPolice;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.10.2002 14:21:56)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleList() {
        return getNomPolice() + " " + getTaille() + " "; // a faire : gestion
        // gas, italic,
        // souligné
    }

    public java.lang.String getNomPolice() {
        return nomPolice;
    }

    public java.lang.String getTaille() {
        return taille;
    }

    public java.lang.Boolean isEnGras() {
        return enGras;
    }

    public java.lang.Boolean isEnItallique() {
        return enItallique;
    }

    public java.lang.Boolean isEnSouligne() {
        return enSouligne;
    }

    public void setEnGras(java.lang.Boolean newEnGras) {
        enGras = newEnGras;
    }

    public void setEnItallique(java.lang.Boolean newEnItallique) {
        enItallique = newEnItallique;
    }

    public void setEnSouligne(java.lang.Boolean newEnSouligne) {
        enSouligne = newEnSouligne;
    }

    /**
     * Setter
     */
    public void setIdPolice(java.lang.String newIdPolice) {
        idPolice = newIdPolice;
    }

    public void setNomPolice(java.lang.String newNomPolice) {
        nomPolice = newNomPolice;
    }

    public void setTaille(java.lang.String newTaille) {
        taille = newTaille;
    }
}
