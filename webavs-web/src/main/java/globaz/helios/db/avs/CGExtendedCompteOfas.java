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
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la base de donn�es
     * 
     * @exception java.lang.Exception
     *                si la lecture des propri�t�s a �chou�e
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
     * Valide le contenu de l'entit� (notamment les champs obligatoires)
     */
    // protected void _validate(globaz.globall.db.BStatement statement) throws
    // java.lang.Exception {}
    /**
     * Sauvegarde les valeurs des propri�t�s propres de l'entit� composant la cl� primaire
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propri�t�s a �chou�e
     */
    // protected void _writePrimaryKey(globaz.globall.db.BStatement statement)
    // throws java.lang.Exception {}
    /**
     * Sauvegarde les valeurs des propri�t�s propres de l'entit� dans la base de donn�es
     * 
     * @param statement
     *            l'instruction � utiliser
     * @exception java.lang.Exception
     *                si la sauvegarde des propri�t�s a �chou�e
     */
    // protected void _writeProperties(globaz.globall.db.BStatement statement)
    // throws java.lang.Exception {}
    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.07.2003 13:26:59)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleSecteurDe() {
        return libelleSecteurDe;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.07.2003 13:26:24)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleSecteurFr() {
        return libelleSecteurFr;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.07.2003 13:27:12)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleSecteurIt() {
        return libelleSecteurIt;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.07.2003 13:32:29)
     * 
     * @return java.lang.String
     */
    public java.lang.String getSecteur() {
        return secteur;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.07.2003 13:26:59)
     * 
     * @param newLibelleSecteurDe
     *            java.lang.String
     */
    public void setLibelleSecteurDe(java.lang.String newLibelleSecteurDe) {
        libelleSecteurDe = newLibelleSecteurDe;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.07.2003 13:26:24)
     * 
     * @param newLibelleSecteurFr
     *            java.lang.String
     */
    public void setLibelleSecteurFr(java.lang.String newLibelleSecteurFr) {
        libelleSecteurFr = newLibelleSecteurFr;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.07.2003 13:27:12)
     * 
     * @param newLibelleSecteurIt
     *            java.lang.String
     */
    public void setLibelleSecteurIt(java.lang.String newLibelleSecteurIt) {
        libelleSecteurIt = newLibelleSecteurIt;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.07.2003 13:32:29)
     * 
     * @param newSecteur
     *            java.lang.String
     */
    public void setSecteur(java.lang.String newSecteur) {
        secteur = newSecteur;
    }
}
