package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * Créé le 18 janv. 06
 * 
 * @author dch
 * 
 *         Représente une en-tête de prestation (JAFPEPR)
 */
public class ALEntetePrestation extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idEntetePrestation = "";
    private String idDossier = "";
    private String idRecap = "";
    private String idAffiliation = "";
    private String idCategorieTarif = "";
    private String numeroAVS = "";
    private String etatPrestation = "";
    private String periodeDe = "";
    private String periodeA = "";
    private String jourDebutMutation = "";
    private String moisDebutMutation = "";
    private String jourFinMutation = "";
    private String moisFinMutation = "";
    private String unite = "";
    private String nombreUnite = "";
    private String typeAllocation = "";
    private String tauxAllocation = "";
    private String nombreEnfants = "";
    private String dateVersementCompensation = "";
    private String idPassageGeneration = "";
    private String idPassageFacturation = "";
    private String idPassageComptabiliteGenerale = "";
    private String idPassageComptabiliteIndividuelle = "";
    private String numeroLotVersement = "";
    private String montant = "";
    private String montantSupplementaire = "";
    private String nomClasseGeneration = "";
    private String numeroSecuriteSociale = "";
    private String idErreurGeneration = "";
    private String typeBonification = "";
    private String mouchardDB = "";

    /**
     * Effectue des traitements avant une suppression de la BD.
     * Lorsque l'on supprime une en-tête, il faut supprimer tous les détails correspondants.
     * 
     * @exception java.lang.Exception en cas d'erreur fatale
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws java.lang.Exception {
        // manager pour charger les détails
        ALDetailPrestationManager details = new ALDetailPrestationManager();

        // détail pour itérer
        ALDetailPrestation detail = null;

        // initilisation du manager
        details.setSession(getSession());
        details.setIdEntetePrestation(idEntetePrestation);

        // on charge tous les détails
        details.find(transaction, 0);

        // on itère sur les détails pour les supprimer
        for (int i = 0; i < details.size(); i++) {
            // traitement du i-ème détail
            detail = (ALDetailPrestation) details.get(i);

            // effacement (PAN! Dans les dents!)
            detail.delete();
        }
    }

    /**
     * Renvoie le nom de la table.
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "JAFPEPR";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la base de données.
     * 
     * @exception java.lang.Exception si la lecture des propriétés a échouée
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idEntetePrestation = statement.dbReadNumeric("MID");
        idDossier = statement.dbReadNumeric("MIDD");
        idRecap = statement.dbReadNumeric("MIDREC");
        idAffiliation = statement.dbReadNumeric("MNOAF");
        idCategorieTarif = statement.dbReadString("MIDTR");
        numeroAVS = statement.dbReadNumeric("MNOAV");
        etatPrestation = statement.dbReadString("METAT");
        periodeDe = statement.dbReadNumeric("MPERD");
        periodeA = statement.dbReadNumeric("MPERA");
        jourDebutMutation = statement.dbReadNumeric("MDMUTJ");
        moisDebutMutation = statement.dbReadNumeric("MDMUTM");
        jourFinMutation = statement.dbReadNumeric("MFMUTJ");
        moisFinMutation = statement.dbReadNumeric("MFMUTM");
        unite = statement.dbReadString("MUNIT");
        nombreUnite = statement.dbReadNumeric("MNBR");
        typeAllocation = statement.dbReadString("MTYPA");
        tauxAllocation = statement.dbReadNumeric("MTAUX");
        nombreEnfants = statement.dbReadNumeric("MNBENF");
        dateVersementCompensation = statement.dbReadNumeric("MDVC");
        idPassageGeneration = statement.dbReadNumeric("MPSGGN");
        idPassageFacturation = statement.dbReadNumeric("MPSGF");
        idPassageComptabiliteGenerale = statement.dbReadNumeric("MPSGCG");
        idPassageComptabiliteIndividuelle = statement.dbReadNumeric("MPSGCC");
        numeroLotVersement = statement.dbReadNumeric("MNLOT");
        montant = statement.dbReadNumeric("MMONT");
        montantSupplementaire = statement.dbReadNumeric("MMSUP");
        nomClasseGeneration = statement.dbReadString("MGENTY");
        numeroSecuriteSociale = statement.dbReadNumeric("MNSSOC");
        idErreurGeneration = statement.dbReadString("MIDERR");
        typeBonification = statement.dbReadString("MBONI");
        mouchardDB = statement.dbReadString("MSPY");
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
        statement.writeKey("MID", _dbWriteNumeric(statement.getTransaction(), idEntetePrestation, ""));
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité dans la base de données.
     * 
     * @param statement l'instruction à utiliser
     * @exception java.lang.Exception si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MID", _dbWriteNumeric(statement.getTransaction(), idEntetePrestation, ""));
        statement.writeField("MIDD", _dbWriteNumeric(statement.getTransaction(), idDossier, ""));
        statement.writeField("MIDREC", _dbWriteNumeric(statement.getTransaction(), idRecap, ""));
        statement.writeField("MNOAF", _dbWriteNumeric(statement.getTransaction(), idAffiliation, ""));
        statement.writeField("MIDTR", _dbWriteString(statement.getTransaction(), idCategorieTarif, ""));
        statement.writeField("MNOAV", _dbWriteNumeric(statement.getTransaction(), numeroAVS, ""));
        statement.writeField("METAT", _dbWriteString(statement.getTransaction(), etatPrestation, ""));
        statement.writeField("MPERD", _dbWriteNumeric(statement.getTransaction(), periodeDe, ""));
        statement.writeField("MPERA", _dbWriteNumeric(statement.getTransaction(), periodeA, ""));
        statement.writeField("MDMUTJ", _dbWriteNumeric(statement.getTransaction(), jourDebutMutation, ""));
        statement.writeField("MDMUTM", _dbWriteNumeric(statement.getTransaction(), moisDebutMutation, ""));
        statement.writeField("MFMUTJ", _dbWriteNumeric(statement.getTransaction(), jourFinMutation, ""));
        statement.writeField("MFMUTM", _dbWriteNumeric(statement.getTransaction(), moisFinMutation, ""));
        statement.writeField("MUNIT", _dbWriteString(statement.getTransaction(), unite, ""));
        statement.writeField("MNBR", _dbWriteNumeric(statement.getTransaction(), nombreUnite, ""));
        statement.writeField("MTYPA", _dbWriteString(statement.getTransaction(), typeAllocation, ""));
        statement.writeField("MTAUX", _dbWriteNumeric(statement.getTransaction(), tauxAllocation, ""));
        statement.writeField("MNBENF", _dbWriteNumeric(statement.getTransaction(), nombreEnfants, ""));
        statement.writeField("MDVC", _dbWriteNumeric(statement.getTransaction(), dateVersementCompensation, ""));
        statement.writeField("MPSGGN", _dbWriteNumeric(statement.getTransaction(), idPassageGeneration, ""));
        statement.writeField("MPSGF", _dbWriteNumeric(statement.getTransaction(), idPassageFacturation, ""));
        statement.writeField("MPSGCG", _dbWriteNumeric(statement.getTransaction(), idPassageComptabiliteGenerale, ""));
        statement.writeField("MPSGCC",
                _dbWriteNumeric(statement.getTransaction(), idPassageComptabiliteIndividuelle, ""));
        statement.writeField("MNLOT", _dbWriteNumeric(statement.getTransaction(), numeroLotVersement, ""));
        statement.writeField("MMONT", _dbWriteNumeric(statement.getTransaction(), montant, ""));
        statement.writeField("MMSUP", _dbWriteNumeric(statement.getTransaction(), montantSupplementaire, ""));
        statement.writeField("MGENTY", _dbWriteString(statement.getTransaction(), nomClasseGeneration, ""));
        statement.writeField("MNSSOC", _dbWriteNumeric(statement.getTransaction(), numeroSecuriteSociale, ""));
        statement.writeField("MIDERR", _dbWriteString(statement.getTransaction(), idErreurGeneration, ""));
        statement.writeField("MBONI", _dbWriteString(statement.getTransaction(), typeBonification, ""));
        statement.writeField("MSPY", _dbWriteString(statement.getTransaction(), mouchardDB, ""));
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
    public String getDateVersementCompensation() {
        return dateVersementCompensation;
    }

    /**
     * @return
     */
    public String getEtatPrestation() {
        return etatPrestation;
    }

    /**
     * @return
     */
    public String getIdEntetePrestation() {
        return idEntetePrestation;
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
    public String getIdCategorieTarif() {
        return idCategorieTarif;
    }

    /**
     * @return
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return
     */
    public String getIdErreurGeneration() {
        return idErreurGeneration;
    }

    /**
     * @return
     */
    public String getIdPassageComptabiliteGenerale() {
        return idPassageComptabiliteGenerale;
    }

    /**
     * @return
     */
    public String getIdPassageComptabiliteIndividuelle() {
        return idPassageComptabiliteIndividuelle;
    }

    /**
     * @return
     */
    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    /**
     * @return
     */
    public String getIdPassageGeneration() {
        return idPassageGeneration;
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
    public String getJourDebutMutation() {
        return jourDebutMutation;
    }

    /**
     * @return
     */
    public String getJourFinMutation() {
        return jourFinMutation;
    }

    /**
     * @return
     */
    public String getMoisDebutMutation() {
        return moisDebutMutation;
    }

    /**
     * @return
     */
    public String getMoisFinMutation() {
        return moisFinMutation;
    }

    /**
     * @return
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return
     */
    public String getMontantSupplementaire() {
        return montantSupplementaire;
    }

    /**
     * @return
     */
    public String getMouchardDB() {
        return mouchardDB;
    }

    /**
     * @return
     */
    public String getNombreEnfants() {
        return nombreEnfants;
    }

    /**
     * @return
     */
    public String getNombreUnite() {
        return nombreUnite;
    }

    /**
     * @return
     */
    public String getNomClasseGeneration() {
        return nomClasseGeneration;
    }

    /**
     * @return
     */
    public String getNumeroAVS() {
        return numeroAVS;
    }

    /**
     * @return
     */
    public String getNumeroLotVersement() {
        return numeroLotVersement;
    }

    /**
     * @return
     */
    public String getNumeroSecuriteSociale() {
        return numeroSecuriteSociale;
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
    public String getTauxAllocation() {
        return tauxAllocation;
    }

    /**
     * @return
     */
    public String getTypeAllocation() {
        return typeAllocation;
    }

    /**
     * @return
     */
    public String getTypeBonification() {
        return typeBonification;
    }

    /**
     * @return
     */
    public String getUnite() {
        return unite;
    }

    /**
     * @param string
     */
    public void setDateVersementCompensation(String string) {
        dateVersementCompensation = string;
    }

    /**
     * @param string
     */
    public void setEtatPrestation(String string) {
        etatPrestation = string;
    }

    /**
     * @param string
     */
    public void setIdEntetePrestation(String string) {
        idEntetePrestation = string;
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
    public void setIdCategorieTarif(String string) {
        idCategorieTarif = string;
    }

    /**
     * @param string
     */
    public void setIdDossier(String string) {
        idDossier = string;
    }

    /**
     * @param string
     */
    public void setIdErreurGeneration(String string) {
        idErreurGeneration = string;
    }

    /**
     * @param string
     */
    public void setIdPassageComptabiliteGenerale(String string) {
        idPassageComptabiliteGenerale = string;
    }

    /**
     * @param string
     */
    public void setIdPassageComptabiliteIndividuelle(String string) {
        idPassageComptabiliteIndividuelle = string;
    }

    /**
     * @param string
     */
    public void setIdPassageFacturation(String string) {
        idPassageFacturation = string;
    }

    /**
     * @param string
     */
    public void setIdPassageGeneration(String string) {
        idPassageGeneration = string;
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
    public void setJourDebutMutation(String string) {
        jourDebutMutation = string;
    }

    /**
     * @param string
     */
    public void setJourFinMutation(String string) {
        jourFinMutation = string;
    }

    /**
     * @param string
     */
    public void setMoisDebutMutation(String string) {
        moisDebutMutation = string;
    }

    /**
     * @param string
     */
    public void setMoisFinMutation(String string) {
        moisFinMutation = string;
    }

    /**
     * @param string
     */
    public void setMontant(String string) {
        montant = string;
    }

    /**
     * @param string
     */
    public void setMontantSupplementaire(String string) {
        montantSupplementaire = string;
    }

    /**
     * @param string
     */
    public void setMouchardDB(String string) {
        mouchardDB = string;
    }

    /**
     * @param string
     */
    public void setNombreEnfants(String string) {
        nombreEnfants = string;
    }

    /**
     * @param string
     */
    public void setNombreUnite(String string) {
        nombreUnite = string;
    }

    /**
     * @param string
     */
    public void setNomClasseGeneration(String string) {
        nomClasseGeneration = string;
    }

    /**
     * @param string
     */
    public void setNumeroAVS(String string) {
        numeroAVS = string;
    }

    /**
     * @param string
     */
    public void setNumeroLotVersement(String string) {
        numeroLotVersement = string;
    }

    /**
     * @param string
     */
    public void setNumeroSecuriteSociale(String string) {
        numeroSecuriteSociale = string;
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
    public void setTauxAllocation(String string) {
        tauxAllocation = string;
    }

    /**
     * @param string
     */
    public void setTypeAllocation(String string) {
        typeAllocation = string;
    }

    /**
     * @param string
     */
    public void setTypeBonification(String string) {
        typeBonification = string;
    }

    /**
     * @param string
     */
    public void setUnite(String string) {
        unite = string;
    }
}