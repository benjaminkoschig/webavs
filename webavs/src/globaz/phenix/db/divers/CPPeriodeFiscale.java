package globaz.phenix.db.divers;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;

public class CPPeriodeFiscale extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static CPPeriodeFiscale _returnPeriodeFiscale(BSession session, BTransaction transaction, int annee)
            throws Exception {
        CPPeriodeFiscale periodeFiscale = null;
        CPPeriodeFiscaleManager periodeFiscaleManager = new CPPeriodeFiscaleManager();
        periodeFiscaleManager.setSession(session);
        periodeFiscaleManager.setForAnneeDecisionDebut(annee + "");
        // Valeur normal (si periode non extraordinaire)
        switch (annee) {
            case 2000:
                periodeFiscaleManager.setForNumIfd("30");
                periodeFiscaleManager.setForAnneeRevenuDebut("1997");
                break;
            case 1999:
                periodeFiscaleManager.setForNumIfd("29");
                periodeFiscaleManager.setForAnneeRevenuDebut("1995");
                break;
            case 1998:
                periodeFiscaleManager.setForNumIfd("29");
                periodeFiscaleManager.setForAnneeRevenuDebut("1995");
                break;
            case 1997:
                periodeFiscaleManager.setForNumIfd("28");
                periodeFiscaleManager.setForAnneeRevenuDebut("1993");
                break;
            case 1996:
                periodeFiscaleManager.setForNumIfd("28");
                periodeFiscaleManager.setForAnneeRevenuDebut("1993");
                break;
            case 1995:
                periodeFiscaleManager.setForNumIfd("27");
                periodeFiscaleManager.setForAnneeRevenuDebut("1991");
                break;
        }
        periodeFiscaleManager.find(transaction);
        if (periodeFiscaleManager.size() >= 1) {
            periodeFiscale = (CPPeriodeFiscale) periodeFiscaleManager.getFirstEntity();
        }
        return periodeFiscale;
    }

    private java.lang.String anneeDecisionDebut = "";
    private java.lang.String anneeDecisionFin = "";
    private java.lang.String anneeRevenuDebut = "";
    private java.lang.String anneeRevenuFin = "";
    private java.lang.String idIfd = "";

    // code systeme

    private java.lang.String numIfd = "";

    /**
     * Commentaire relatif au constructeur CPPeriodeFiscale
     */
    public CPPeriodeFiscale() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdIfd(_incCounter(transaction, idIfd));

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPPEFIP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idIfd = statement.dbReadNumeric("ICIIFD");
        numIfd = statement.dbReadNumeric("ICNIFD");
        anneeRevenuDebut = statement.dbReadNumeric("ICANRD");
        anneeRevenuFin = statement.dbReadNumeric("ICANRF");
        anneeDecisionDebut = statement.dbReadNumeric("ICANDD");
        anneeDecisionFin = statement.dbReadNumeric("ICANDF");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité composant une clé alternée
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     * @param alternateKey
     *            int le numéro de la clé alternée à utiliser
     */
    @Override
    protected void _writeAlternateKey(globaz.globall.db.BStatement statement, int alternateKey) throws Exception {
        // Traitement par défaut : pas de clé alternée
        // Recherche par décisoin et genre de cotisation
        if (alternateKey == 1) {
            statement.writeKey("ICANRD", _dbWriteNumeric(statement.getTransaction(), getAnneeRevenuDebut(), ""));
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("ICIIFD", _dbWriteNumeric(statement.getTransaction(), getIdIfd(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("ICIIFD", _dbWriteNumeric(statement.getTransaction(), getIdIfd(), "idIfd"));
        statement.writeField("ICNIFD", _dbWriteNumeric(statement.getTransaction(), getNumIfd(), "numIfd"));
        statement.writeField("ICANRD",
                _dbWriteNumeric(statement.getTransaction(), getAnneeRevenuDebut(), "anneeRevenuDebut"));
        statement.writeField("ICANRF",
                _dbWriteNumeric(statement.getTransaction(), getAnneeRevenuFin(), "anneeRevenuFin"));
        statement.writeField("ICANDD",
                _dbWriteNumeric(statement.getTransaction(), getAnneeDecisionDebut(), "anneeDecisionDebut"));
        statement.writeField("ICANDF",
                _dbWriteNumeric(statement.getTransaction(), getAnneeDecisionFin(), "anneeDecisionFin"));
    }

    public java.lang.String getAnneeDecisionDebut() {
        return anneeDecisionDebut;
    }

    public java.lang.String getAnneeDecisionFin() {
        return anneeDecisionFin;
    }

    public java.lang.String getAnneeRevenuDebut() {
        return anneeRevenuDebut;
    }

    public java.lang.String getAnneeRevenuFin() {
        return anneeRevenuFin;
    }

    public java.lang.String getDebutRevenu1() {
        return "01.01." + anneeRevenuDebut;
    }

    public java.lang.String getDebutRevenu2() {
        return "01.01." + anneeRevenuFin;
    }

    public java.lang.String getFinRevenu1() {
        return "31.12." + anneeRevenuDebut;
    }

    public java.lang.String getFinRevenu2() {
        return "31.12." + anneeRevenuFin;
    }

    /**
     * Getter
     */
    public java.lang.String getIdIfd() {
        return idIfd;
    }

    public java.lang.String getNumIfd() {
        return numIfd;
    }

    public void setAnneeDecisionDebut(java.lang.String newAnneeDecisionDebut) {
        anneeDecisionDebut = newAnneeDecisionDebut;
    }

    public void setAnneeDecisionFin(java.lang.String newAnneeDecisionFin) {
        anneeDecisionFin = newAnneeDecisionFin;
    }

    public void setAnneeRevenuDebut(java.lang.String newAnneeRevenuDebut) {
        anneeRevenuDebut = newAnneeRevenuDebut;
    }

    public void setAnneeRevenuFin(java.lang.String newAnneeRevenuFin) {
        anneeRevenuFin = newAnneeRevenuFin;
    }

    /**
     * Setter
     */
    public void setIdIfd(java.lang.String newIdIfd) {
        idIfd = newIdIfd;
    }

    public void setNumIfd(java.lang.String newNumIfd) {
        numIfd = newNumIfd;
    }
}
