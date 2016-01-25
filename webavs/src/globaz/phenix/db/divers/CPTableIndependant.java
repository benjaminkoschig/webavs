package globaz.phenix.db.divers;

import globaz.globall.db.BProcess;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.util.JANumberFormatter;

public class CPTableIndependant extends globaz.globall.db.BEntity implements java.io.Serializable {
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
        return Float.parseFloat(FWFindParameter.findParameter(transaction, "10500070", "COTMININDE", date, "", 0));
    }

    /**
     * retourne le revenu ci min pour personne avec activité pour une date
     * 
     * @param transaction
     * @param date
     * @return float
     */
    public static float getRevenuCiMin(BTransaction transaction, String date) throws Exception {
        return Float.parseFloat(FWFindParameter.findParameter(transaction, "10500040", "REVCIMINIM", date, "", 0));
    }

    /**
     * retourne le revenu min pour un indépendant
     * 
     * @param transaction
     * @param date
     * @return float
     */
    public static float getRevenuMin(BTransaction transaction, String date) throws Exception {
        return Float.parseFloat(FWFindParameter.findParameter(transaction, "10500100", "REVMINIMID", date, "", 0));
    }

    private java.lang.String anneeInd = "";

    // code systeme

    private java.lang.String idTableInd = "";

    private java.lang.String revenuInd = "";

    private java.lang.String taux = "";

    /**
     * Commentaire relatif au constructeur CPTableIndependant
     */
    public CPTableIndependant() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdTableInd(_incCounter(transaction, idTableInd));

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPTINDP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idTableInd = statement.dbReadNumeric("JBITIN");
        anneeInd = statement.dbReadNumeric("JBANNE");
        revenuInd = statement.dbReadNumeric("JBMREV", 2);
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
        statement.writeKey("JBITIN", _dbWriteNumeric(statement.getTransaction(), getIdTableInd(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("JBITIN", _dbWriteNumeric(statement.getTransaction(), getIdTableInd(), "idTableInd"));
        statement.writeField("JBANNE", _dbWriteNumeric(statement.getTransaction(), getAnneeInd(), "anneeInd"));
        statement.writeField("JBMREV", _dbWriteNumeric(statement.getTransaction(), getRevenuInd(), "revenuInd"));
        statement.writeField("JBTAUX", _dbWriteNumeric(statement.getTransaction(), getTaux(), "taux"));
    }

    public java.lang.String getAnneeInd() {
        return anneeInd;
    }

    /**
     * Getter
     */
    public java.lang.String getIdTableInd() {
        return idTableInd;
    }

    public java.lang.String getRevenuInd() {
        /*
         * return JANumberFormatter.fmt( revenuInd.toString(), true, false, true, 0);
         */
        return revenuInd;

    }

    /**
     * retourne le revenu max pour indépendant pour une année
     * 
     * @param process
     * @param annee
     * @return float
     */
    public float getRevenuMax(BProcess process, String annee) throws Exception {
        // Recherche du revenu maximum indépendant pour l'année de décision
        CPTableIndependantManager manager = new CPTableIndependantManager();
        manager.setSession(process.getSession());
        manager.setFromAnneeInd(annee);
        manager.orderByAnneeDescendant();
        manager.orderByRevenuDescendant();
        manager.find();
        if (manager.size() == 0) {
            _addError(process.getTransaction(), getSession().getLabel("CP_MSG_0022"));
            return 0;
        } else {
            return Float.parseFloat(JANumberFormatter.deQuote(((CPTableIndependant) manager.getEntity(0))
                    .getRevenuInd()));
        }
    }

    public java.lang.String getTaux() {
        return JANumberFormatter.fmt(taux, true, false, true, 3);
    }

    public void setAnneeInd(java.lang.String newAnneeInd) {
        anneeInd = newAnneeInd;
    }

    /**
     * Setter
     */
    public void setIdTableInd(java.lang.String newIdTableInd) {
        idTableInd = newIdTableInd;
    }

    public void setRevenuInd(java.lang.String newRevenuInd) {
        revenuInd = JANumberFormatter.deQuote(newRevenuInd);
    }

    public void setTaux(java.lang.String newTaux) {
        taux = JANumberFormatter.deQuote(newTaux);
    }
}
