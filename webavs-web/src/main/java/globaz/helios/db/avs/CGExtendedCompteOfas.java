package globaz.helios.db.avs;

import globaz.globall.db.BStatement;
import globaz.helios.db.interfaces.CGLibelleInterface;

public class CGExtendedCompteOfas extends CGCompteOfas implements java.io.Serializable, CGLibelleInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String libelleSecteurDe = "";
    private java.lang.String libelleSecteurFr = "";
    private java.lang.String libelleSecteurIt = "";
    private java.lang.String secteur;

    @Override
    protected String _getFields(BStatement statement) {
        return super._getFields(statement) + ", substr(" + _getCollection() + _getTableName()
                + ".idexterne, 1, 3) as secteur, " + _getCollection() + "cgsectp.libellefr as libellefrsect, "
                + _getCollection() + "cgsectp.libellede as libelledesect, " + _getCollection()
                + "cgsectp.libelleit as libelleitsect";
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    /*
     * protected String _getFrom(globaz.globall.db.BStatement statement) { String table1 = _getCollection() +"CGOFCPP";
     * String table2 = _getCollection() +"CGSECTP"; String from=" "+table1; from += " LEFT JOIN "
     * +table2+"on (cast(substr(idexterne,1,3) as integer) * 10 = idsecteuravs)" ; return from; }
     */
    /**
     * Renvoie le nom de la table
     * 
     * @return le nom de la table
     */
    @Override
    protected java.lang.String _getTableName() {
        return "CGOFCPP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la base de données
     * 
     * @exception java.lang.Exception
     *                si la lecture des propriétés a échouée
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws java.lang.Exception {
        super._readProperties(statement);
        secteur = statement.dbReadNumeric("SECTEUR");
        libelleSecteurFr = statement.dbReadString("LIBELLEFRSECT");
        libelleSecteurDe = statement.dbReadString("LIBELLEDESECT");
        libelleSecteurIt = statement.dbReadString("LIBELLEITSECT");
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    // protected void _validate(globaz.globall.db.BStatement statement) throws
    // java.lang.Exception {}
    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité composant la clé primaire
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     */
    // protected void _writePrimaryKey(globaz.globall.db.BStatement statement)
    // throws java.lang.Exception {}
    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité dans la base de données
     * 
     * @param statement
     *            l'instruction à utiliser
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     */
    // protected void _writeProperties(globaz.globall.db.BStatement statement)
    // throws java.lang.Exception {}
    /**
     * Insérez la description de la méthode ici. Date de création : (10.07.2003 13:26:59)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleSecteurDe() {
        return libelleSecteurDe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.07.2003 13:26:24)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleSecteurFr() {
        return libelleSecteurFr;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.07.2003 13:27:12)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleSecteurIt() {
        return libelleSecteurIt;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.07.2003 13:32:29)
     * 
     * @return java.lang.String
     */
    public java.lang.String getSecteur() {
        return secteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.07.2003 13:26:59)
     * 
     * @param newLibelleSecteurDe
     *            java.lang.String
     */
    public void setLibelleSecteurDe(java.lang.String newLibelleSecteurDe) {
        libelleSecteurDe = newLibelleSecteurDe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.07.2003 13:26:24)
     * 
     * @param newLibelleSecteurFr
     *            java.lang.String
     */
    public void setLibelleSecteurFr(java.lang.String newLibelleSecteurFr) {
        libelleSecteurFr = newLibelleSecteurFr;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.07.2003 13:27:12)
     * 
     * @param newLibelleSecteurIt
     *            java.lang.String
     */
    public void setLibelleSecteurIt(java.lang.String newLibelleSecteurIt) {
        libelleSecteurIt = newLibelleSecteurIt;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.07.2003 13:32:29)
     * 
     * @param newSecteur
     *            java.lang.String
     */
    public void setSecteur(java.lang.String newSecteur) {
        secteur = newSecteur;
    }
}
