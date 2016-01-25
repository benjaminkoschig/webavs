package ch.globaz.common.FusionTiersMultiple;

import globaz.globall.util.JACalendar;

public class TIListeTiersMultiple extends globaz.globall.db.BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateAffiliation = new String();
    private String dateNaissance = new String();
    private String dateRadiation = new String();
    private String idTiers = new String();
    private String nbrPrestation = new String();
    private String nom = new String();
    private String nss = new String();
    private String numAffilie = new String();
    private String prenom = new String();

    /**
     * Commentaire relatif au constructeur FAModulePassage
     */
    public TIListeTiersMultiple() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idTiers = statement.dbReadNumeric("HTITIE");
        nom = statement.dbReadString("HTLDU1");
        prenom = statement.dbReadString("HTLDU2");
        dateNaissance = statement.dbReadDateAMJ("HPDNAI", JACalendar.FORMAT_DDsMMsYYYY);
        nss = statement.dbReadString("HXNAVS");
        numAffilie = statement.dbReadString("MALNAF");
        dateAffiliation = statement.dbReadDateAMJ("MADDEB", JACalendar.FORMAT_DDsMMsYYYY);
        dateRadiation = statement.dbReadDateAMJ("MADFIN", JACalendar.FORMAT_DDsMMsYYYY);
        nbrPrestation = statement.dbReadNumeric("PREST");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    @Override
    public String getCollection() {
        // TODO Auto-generated method stub
        return super.getCollection();
    }

    public String getDateAffiliation() {
        return dateAffiliation;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDateRadiation() {
        return dateRadiation;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNbrPrestation() {
        return nbrPrestation;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getPrenom() {
        return prenom;
    }

}
