package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * Créé le 10 févr. 06
 * 
 * @author dch
 * 
 *         Représente un détail de prestation (JAFPHPR)
 */
public class ALDetailPrestation extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDetailPrestation = "";
    private String idDroit = "";
    private String idEntetePrestation = "";
    private String idCategorie = "";
    private String idDetailTarif = "";
    private String numeroAVS = "";
    private String typePrestation = "";
    private String moisValidite = "";
    private String anneeValidite = "";
    private String montant = "";
    private String montantSupplement = "";
    private String montantUnitaire = "";
    private String montantSupplementUnitaire = "";
    private String anneeMAJ = "";
    private String moisMAJ = "";
    private String jourMAJ = "";
    private String idAdressePaiement = "";

    /**
     * Effectue des traitements avant une suppression de la BD.
     * Lorsque l'on supprime un détail, il faut supprimer tous les historiques de rubriques correspondants.
     * 
     * @exception java.lang.Exception en cas d'erreur fatale
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws java.lang.Exception {
        // manager pour charger les historiques de rubriques
        ALHistoriqueRubriquesManager historiquesRubriques = new ALHistoriqueRubriquesManager();

        // historique rubriques pour itérer
        ALHistoriqueRubriques historiqueRubrique = null;

        // initilisation du manager
        historiquesRubriques.setSession(getSession());
        historiquesRubriques.setIdDetailPrestation(idDetailPrestation);

        // on charge tous les historiques rubriques
        historiquesRubriques.find(transaction, 0);

        // on itère sur les historiques rubriques pour les supprimer
        for (int i = 0; i < historiquesRubriques.size(); i++) {
            // traitement du i-ème historique rubriques
            historiqueRubrique = (ALHistoriqueRubriques) historiquesRubriques.get(i);

            // effacement (PAN! Dans les dents!)
            historiqueRubrique.delete();
        }
    }

    /**
     * Renvoie le nom de la table.
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "JAFPHPR";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la base de données.
     * 
     * @exception java.lang.Exception si la lecture des propriétés a échouée
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDetailPrestation = statement.dbReadNumeric("NID");
        idDroit = statement.dbReadNumeric("NIDD");
        idEntetePrestation = statement.dbReadNumeric("NIDE");
        idCategorie = statement.dbReadString("NIDC");
        idDetailTarif = statement.dbReadNumeric("NIDDT");
        numeroAVS = statement.dbReadNumeric("NNOAV");
        typePrestation = statement.dbReadString("NTPRS");
        moisValidite = statement.dbReadNumeric("NMOIS");
        anneeValidite = statement.dbReadNumeric("NANNE");
        montant = statement.dbReadNumeric("NMONT");
        montantSupplement = statement.dbReadNumeric("NMSUP");
        montantUnitaire = statement.dbReadNumeric("NMTU");
        montantSupplementUnitaire = statement.dbReadNumeric("NMSPU");
        anneeMAJ = statement.dbReadNumeric("NMAJA");
        moisMAJ = statement.dbReadNumeric("NMAJM");
        jourMAJ = statement.dbReadNumeric("NMAJJ");
        idAdressePaiement = statement.dbReadNumeric("NADP");
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
        statement.writeKey("NID", _dbWriteNumeric(statement.getTransaction(), idDetailPrestation, ""));
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité dans la base de données.
     * 
     * @param statement l'instruction à utiliser
     * @exception java.lang.Exception si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("NID", _dbWriteNumeric(statement.getTransaction(), idDetailPrestation, ""));
        statement.writeField("NIDD", _dbWriteNumeric(statement.getTransaction(), idDroit, ""));
        statement.writeField("NIDE", _dbWriteNumeric(statement.getTransaction(), idEntetePrestation, ""));
        statement.writeField("NIDC", _dbWriteString(statement.getTransaction(), idCategorie, ""));
        statement.writeField("NIDDT", _dbWriteNumeric(statement.getTransaction(), idDetailTarif, ""));
        statement.writeField("NNOAV", _dbWriteNumeric(statement.getTransaction(), numeroAVS, ""));
        statement.writeField("NTPRS", _dbWriteString(statement.getTransaction(), typePrestation, ""));
        statement.writeField("NMOIS", _dbWriteNumeric(statement.getTransaction(), moisValidite, ""));
        statement.writeField("NANNE", _dbWriteNumeric(statement.getTransaction(), anneeValidite, ""));
        statement.writeField("NMONT", _dbWriteNumeric(statement.getTransaction(), montant, ""));
        statement.writeField("NMSUP", _dbWriteNumeric(statement.getTransaction(), montantSupplement, ""));
        statement.writeField("NMTU", _dbWriteNumeric(statement.getTransaction(), montantUnitaire, ""));
        statement.writeField("NMSPU", _dbWriteNumeric(statement.getTransaction(), montantSupplementUnitaire, ""));
        statement.writeField("NMAJA", _dbWriteNumeric(statement.getTransaction(), anneeMAJ, ""));
        statement.writeField("NMAJM", _dbWriteNumeric(statement.getTransaction(), moisMAJ, ""));
        statement.writeField("NMAJJ", _dbWriteNumeric(statement.getTransaction(), jourMAJ, ""));
        statement.writeField("NADP", _dbWriteNumeric(statement.getTransaction(), idAdressePaiement, ""));
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
    public String getAnneeMAJ() {
        return anneeMAJ;
    }

    /**
     * @return
     */
    public String getAnneeValidite() {
        return anneeValidite;
    }

    /**
     * @return
     */
    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    /**
     * @return
     */
    public String getIdCategorie() {
        return idCategorie;
    }

    /**
     * @return
     */
    public String getIdDetailPrestation() {
        return idDetailPrestation;
    }

    /**
     * @return
     */
    public String getIdDetailTarif() {
        return idDetailTarif;
    }

    /**
     * @return
     */
    public String getIdDroit() {
        return idDroit;
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
    public String getJourMAJ() {
        return jourMAJ;
    }

    /**
     * @return
     */
    public String getMoisMAJ() {
        return moisMAJ;
    }

    /**
     * @return
     */
    public String getMoisValidite() {
        return moisValidite;
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
    public String getMontantSupplement() {
        return montantSupplement;
    }

    /**
     * @return
     */
    public String getMontantSupplementUnitaire() {
        return montantSupplementUnitaire;
    }

    /**
     * @return
     */
    public String getMontantUnitaire() {
        return montantUnitaire;
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
    public String getTypePrestation() {
        return typePrestation;
    }

    /**
     * @param string
     */
    public void setAnneeMAJ(String string) {
        anneeMAJ = string;
    }

    /**
     * @param string
     */
    public void setAnneeValidite(String string) {
        anneeValidite = string;
    }

    /**
     * @param string
     */
    public void setIdAdressePaiement(String string) {
        idAdressePaiement = string;
    }

    /**
     * @param string
     */
    public void setIdCategorie(String string) {
        idCategorie = string;
    }

    /**
     * @param string
     */
    public void setIdDetailPrestation(String string) {
        idDetailPrestation = string;
    }

    /**
     * @param string
     */
    public void setIdDetailTarif(String string) {
        idDetailTarif = string;
    }

    /**
     * @param string
     */
    public void setIdDroit(String string) {
        idDroit = string;
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
    public void setJourMAJ(String string) {
        jourMAJ = string;
    }

    /**
     * @param string
     */
    public void setMoisMAJ(String string) {
        moisMAJ = string;
    }

    /**
     * @param string
     */
    public void setMoisValidite(String string) {
        moisValidite = string;
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
    public void setMontantSupplement(String string) {
        montantSupplement = string;
    }

    /**
     * @param string
     */
    public void setMontantSupplementUnitaire(String string) {
        montantSupplementUnitaire = string;
    }

    /**
     * @param string
     */
    public void setMontantUnitaire(String string) {
        montantUnitaire = string;
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
    public void setTypePrestation(String string) {
        typePrestation = string;
    }
}