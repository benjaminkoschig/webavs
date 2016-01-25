package globaz.phenix.db.divers;

import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;

public class CPTableNonActif extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * retourne le revenu min pour personne avec activité pour une annee
     * 
     * @param session
     * @param annee
     * @return float
     */
    public static float getCotisationMin(BSession session, String annee) throws Exception {
        CPTableNonActifManager manager = new CPTableNonActifManager();
        manager.setTilAnneeVigueur(annee);
        manager.setSession(session);
        manager.orderByAnneeDescendant();
        manager.orderByRevenuAscendant();
        manager.find();
        return Float.parseFloat(((CPTableNonActif) manager.getEntity(0)).getCotisationAnnuelle());
    }

    /**
     * Method getRevenuMin. Retourne le revenu minimal pour une année
     * 
     * @param session
     * @param annee
     * @return String
     */
    public static String getRevenuMin(BSession session, String annee) throws Exception {
        CPTableNonActifManager manager = new CPTableNonActifManager();
        manager.setSession(session);
        manager.setTilAnneeVigueur(annee);
        manager.setFromStandardRevenu("0");
        manager.orderByAnneeDescendant();
        manager.orderByRevenuAscendant();
        manager.find();
        if (manager.getFirstEntity() != null) {
            return (JANumberFormatter.deQuote(((CPTableNonActif) manager.getFirstEntity()).getRevenu()));
        } else {
            return "";
        }
    }

    private java.lang.String anneeVigueur = "";
    private java.lang.String cotisationAnnuelle = "";
    private java.lang.String cotisationMensuelle = "";
    private java.lang.String idTableNac = "";

    // code systeme

    private java.lang.String revenu = "";

    private java.lang.String revenuCi = "";

    /**
     * Commentaire relatif au constructeur TableIndependant
     */
    public CPTableNonActif() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdTableNac(_incCounter(transaction, idTableNac));

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPTNACP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idTableNac = statement.dbReadNumeric("JAITNA");
        anneeVigueur = statement.dbReadNumeric("JAANNE");
        revenu = statement.dbReadNumeric("JAMREV", 2);
        cotisationMensuelle = statement.dbReadNumeric("JAMCOM", 2);
        cotisationAnnuelle = statement.dbReadNumeric("JAMCOA", 2);
        revenuCi = statement.dbReadNumeric("JAMRCI", 2);
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
        statement.writeKey("JAITNA", _dbWriteNumeric(statement.getTransaction(), getIdTableNac(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("JAITNA", _dbWriteNumeric(statement.getTransaction(), getIdTableNac(), "idTableNac"));
        statement.writeField("JAANNE", _dbWriteNumeric(statement.getTransaction(), getAnneeVigueur(), "anneeVigueur"));
        statement.writeField("JAMREV", _dbWriteNumeric(statement.getTransaction(), getRevenu(), "revenu"));
        statement.writeField("JAMCOM",
                _dbWriteNumeric(statement.getTransaction(), getCotisationMensuelle(), "cotisationMensuelle"));
        statement.writeField("JAMCOA",
                _dbWriteNumeric(statement.getTransaction(), getCotisationAnnuelle(), "cotisationAnnuelle"));
        statement.writeField("JAMRCI", _dbWriteNumeric(statement.getTransaction(), getRevenuCi(), "revenuCi"));
    }

    public java.lang.String getAnneeVigueur() {
        return anneeVigueur;
    }

    public java.lang.String getCotisationAnnuelle() {
        return JANumberFormatter.fmt(cotisationAnnuelle, true, true, false, 2);
    }

    public java.lang.String getCotisationMensuelle() {
        return JANumberFormatter.fmt(cotisationMensuelle, true, true, false, 2);
    }

    /**
     * Getter
     */
    public java.lang.String getIdTableNac() {
        return idTableNac;
    }

    public java.lang.String getRevenu() {
        return JANumberFormatter.fmt(revenu, true, false, false, 0);
    }

    public java.lang.String getRevenuCi() {
        return JANumberFormatter.fmt(revenuCi, true, false, false, 0);
    }

    public void setAnneeVigueur(java.lang.String newAnneeVigueur) {
        anneeVigueur = newAnneeVigueur;
    }

    public void setCotisationAnnuelle(java.lang.String newCotisationAnnuelle) {
        cotisationAnnuelle = JANumberFormatter.deQuote(newCotisationAnnuelle);
    }

    public void setCotisationMensuelle(java.lang.String newCotisationMensuelle) {
        cotisationMensuelle = JANumberFormatter.deQuote(newCotisationMensuelle);
    }

    /**
     * Setter
     */
    public void setIdTableNac(java.lang.String newIdTableNac) {
        idTableNac = newIdTableNac;
    }

    public void setRevenu(java.lang.String newRevenu) {
        revenu = JANumberFormatter.deQuote(newRevenu);
    }

    public void setRevenuCi(java.lang.String newRevenuCi) {
        revenuCi = JANumberFormatter.deQuote(newRevenuCi);
    }

}
