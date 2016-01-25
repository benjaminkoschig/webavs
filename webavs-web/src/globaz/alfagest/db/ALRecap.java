package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Créé le 18 janv. 06
 * 
 * @author dch
 * 
 *         Représente une recap (JAFPRCP)
 */
public class ALRecap extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idRecap = "";
    private String idAffiliation = "";
    private String periodeDe = "";
    private String periodeA = "";
    private String communication = "";
    private String etat = "";
    private String numeroFacture = "";
    private String typeBonification = "";

    /**
     * Renvoie le nom de la table.
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "JAFPRCP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la base de données.
     * 
     * @exception java.lang.Exception si la lecture des propriétés a échouée
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idRecap = statement.dbReadNumeric("LID");
        idAffiliation = statement.dbReadNumeric("LNOAF");
        periodeDe = statement.dbReadNumeric("LPERD");
        periodeA = statement.dbReadNumeric("LPERA");
        communication = statement.dbReadString("LCOMM");
        etat = statement.dbReadString("LETAT");
        numeroFacture = statement.dbReadNumeric("LNOFA");
        typeBonification = statement.dbReadString("LBONI");
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires).
     * 
     * @exception java.lang.Exception en cas d'erreur
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité composant la clé primaire.
     * 
     * @exception java.lang.Exception si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("LID", _dbWriteNumeric(statement.getTransaction(), idRecap, ""));
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité dans la base de données.
     * 
     * @param statement l'instruction à utiliser
     * @exception java.lang.Exception si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("LID", _dbWriteNumeric(statement.getTransaction(), idRecap, ""));
        statement.writeField("LNOAF", _dbWriteNumeric(statement.getTransaction(), idAffiliation, ""));
        statement.writeField("LPERD", _dbWriteNumeric(statement.getTransaction(), periodeDe, ""));
        statement.writeField("LPERA", _dbWriteNumeric(statement.getTransaction(), periodeA, ""));
        statement.writeField("LCOMM", _dbWriteString(statement.getTransaction(), communication, ""));
        statement.writeField("LETAT", _dbWriteString(statement.getTransaction(), etat, ""));
        statement.writeField("LNOFA", _dbWriteNumeric(statement.getTransaction(), numeroFacture, ""));
        statement.writeField("LBONI", _dbWriteString(statement.getTransaction(), typeBonification, ""));
    }

    /**
     * Renvoie si l'entité contient un espion.
     */
    @Override
    public boolean hasSpy() {
        return false;
    }

    /**
     * @return
     */
    public String getCommunication() {
        return communication;
    }

    /**
     * @return
     */
    public String getEtat() {
        return etat;
    }

    /**
     * @return
     */
    public String getIdAffiliation() {
        return idAffiliation;
    }

    /**
     * @return
     */
    public String getIdRecap() {
        return idRecap;
    }

    /**
     * @return
     */
    public String getNumeroFacture() {
        return numeroFacture;
    }

    /**
     * @return
     */
    public String getPeriodeA() {
        return periodeA;
    }

    /**
     * @return
     */
    public String getPeriodeDe() {
        return periodeDe;
    }

    /**
     * @return
     */
    public String getTypeBonification() {
        return typeBonification;
    }

    /**
     * @param string
     */
    public void setCommunication(String string) {
        communication = string;
    }

    /**
     * @param string
     */
    public void setEtat(String string) {
        etat = string;
    }

    /**
     * @param string
     */
    public void setIdAffiliation(String string) {
        idAffiliation = string;
    }

    /**
     * @param string
     */
    public void setIdRecap(String string) {
        idRecap = string;
    }

    /**
     * @param string
     */
    public void setNumeroFacture(String string) {
        numeroFacture = string;
    }

    /**
     * @param string
     */
    public void setPeriodeA(String string) {
        periodeA = string;
    }

    /**
     * @param string
     */
    public void setPeriodeDe(String string) {
        periodeDe = string;
    }

    /**
     * @param string
     */
    public void setTypeBonification(String string) {
        typeBonification = string;
    }
}