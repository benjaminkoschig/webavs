package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Créé le 20 janv. 06
 * 
 * @author dch
 * 
 *         Représente une prestation pour le paiement, faisant une jointure sur plusieurs tables
 *         (JAFPRCP, AFAFFIP, TITIERP, JAFPEPR, JAFPHPR, JAFPRUBR)
 */
public class ALPrestationPaiement extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idRecap = "";
    private String etatRecap = "";
    private String numeroFacture = "";
    private String periodeRecapA = "";
    private String periodeRecapDe = "";
    private String numeroAffilie = "";
    private String idTiers = "";
    private String denomination1 = "";
    private String denomination2 = "";
    private String categorieRubrique = "";
    private String chiffreStatistique = "";
    private String idAffiliation = "";
    private String totalMontant = "";
    private String msgErreur = "";

    /**
     * Renvoie le nom de la table.
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la base de données.
     * 
     * @exception java.lang.Exception si la lecture des propriétés a échouée
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idRecap = statement.dbReadNumeric("LID");
        etatRecap = statement.dbReadString("LETAT");
        numeroFacture = statement.dbReadNumeric("LNOFA");
        periodeRecapA = statement.dbReadNumeric("LPERA");
        periodeRecapDe = statement.dbReadNumeric("LPERD");
        numeroAffilie = statement.dbReadString("MALNAF");
        idTiers = statement.dbReadNumeric("HTITIE");
        denomination1 = statement.dbReadString("HTLDE1");
        denomination2 = statement.dbReadString("HTLDE2");
        categorieRubrique = statement.dbReadString("A7CAT");
        chiffreStatistique = statement.dbReadString("A7CHST");
        idAffiliation = statement.dbReadNumeric("LNOAF");
        totalMontant = statement.dbReadNumeric("TOTMONT");
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
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité dans la base de données.
     * 
     * @param statement l'instruction à utiliser
     * @exception java.lang.Exception si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
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
    public String getCategorieRubrique() {
        return categorieRubrique;
    }

    /**
     * @return
     */
    public String getChiffreStatistique() {
        return chiffreStatistique;
    }

    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return
     */
    public String getDenomination1() {
        return denomination1;
    }

    /**
     * @return
     */
    public String getDenomination2() {
        return denomination2;
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
    public String getNumeroAffilie() {
        return numeroAffilie;
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
    public String getPeriodeRecapA() {
        return periodeRecapA;
    }

    /**
     * @return
     */
    public String getPeriodeRecapDe() {
        return periodeRecapDe;
    }

    /**
     * @return
     */
    public String getTotalMontant() {
        return totalMontant;
    }

    /**
     * @param string
     */
    public void setCategorieRubrique(String string) {
        categorieRubrique = string;
    }

    /**
     * @param string
     */
    public void setChiffreStatistique(String string) {
        chiffreStatistique = string;
    }

    public void setIdTiers(String s) {
        idTiers = s;
    }

    /**
     * @param string
     */
    public void setDenomination1(String string) {
        denomination1 = string;
    }

    /**
     * @param string
     */
    public void setDenomination2(String string) {
        denomination2 = string;
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
    public void setNumeroAffilie(String string) {
        numeroAffilie = string;
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
    public void setPeriodeRecapA(String string) {
        periodeRecapA = string;
    }

    /**
     * @param string
     */
    public void setPeriodeRecapDe(String string) {
        periodeRecapDe = string;
    }

    /**
     * @param string
     */
    public void setTotalMontant(String string) {
        totalMontant = string;
    }

    /**
     * @return
     */
    public String getEtatRecap() {
        return etatRecap;
    }

    /**
     * @param string
     */
    public void setEtatRecap(String string) {
        etatRecap = string;
    }

    /**
     * @return
     */
    public String getMsgErreur() {
        return msgErreur;
    }

    /**
     * @param string
     */
    public void setMsgErreur(String string) {
        msgErreur = string;
    }

    /**
     * @return
     */
    public String getIdAffiliation() {
        return idAffiliation;
    }

    /**
     * @param string
     */
    public void setIdAffiliation(String string) {
        idAffiliation = string;
    }
}