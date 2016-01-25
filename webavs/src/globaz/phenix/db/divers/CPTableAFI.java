package globaz.phenix.db.divers;

import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.util.JANumberFormatter;

public class CPTableAFI extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * retourne la cotisation min pour indépendant pour une date
     * 
     * @param transaction
     * @param date
     * @return float
     */
    public static float getCotisationMinimum(BTransaction transaction, String date) throws Exception {
        return Float.parseFloat(FWFindParameter.findParameter(transaction, "10500140", "COTMINIAFI", date, "", 2));
    }

    private java.lang.String anneeAfi = "";
    private java.lang.String idTableAfi = "";
    private java.lang.String revenuAfi = "";

    // code systeme

    private java.lang.String taux = "";

    /**
     * Commentaire relatif au constructeur CPTableIndependant
     */
    public CPTableAFI() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdTableAfi(_incCounter(transaction, idTableAfi));

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPTAFIP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idTableAfi = statement.dbReadNumeric("JBITIN");
        anneeAfi = statement.dbReadNumeric("JBANNE");
        revenuAfi = statement.dbReadNumeric("JBMREV", 2);
        taux = statement.dbReadNumeric("JBTAUX", 5);
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
        statement.writeKey("JBITIN", _dbWriteNumeric(statement.getTransaction(), getIdTableAfi(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("JBITIN", _dbWriteNumeric(statement.getTransaction(), getIdTableAfi(), "idTableAfi"));
        statement.writeField("JBANNE", _dbWriteNumeric(statement.getTransaction(), getAnneeAfi(), "anneeAfi"));
        statement.writeField("JBMREV", _dbWriteNumeric(statement.getTransaction(), getRevenuAfi(), "revenuAfi"));
        statement.writeField("JBTAUX", _dbWriteNumeric(statement.getTransaction(), getTaux(), "taux"));
    }

    public java.lang.String getAnneeAfi() {
        return anneeAfi;
    }

    /**
     * Getter
     */
    public java.lang.String getIdTableAfi() {
        return idTableAfi;
    }

    public java.lang.String getRevenuAfi() {
        /*
         * return JANumberFormatter.fmt( revenuAfi.toString(), true, false, true, 0);
         */
        return revenuAfi;

    }

    public java.lang.String getTaux() {
        return JANumberFormatter.fmt(taux, true, false, true, 3);
    }

    public void setAnneeAfi(java.lang.String newAnneeInd) {
        anneeAfi = newAnneeInd;
    }

    /**
     * Setter
     */
    public void setIdTableAfi(java.lang.String newIdTableInd) {
        idTableAfi = newIdTableInd;
    }

    public void setRevenuAfi(java.lang.String newRevenuInd) {
        revenuAfi = JANumberFormatter.deQuote(newRevenuInd);
    }

    public void setTaux(java.lang.String newTaux) {
        taux = JANumberFormatter.deQuote(newTaux);
    }
}
