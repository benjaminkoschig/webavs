package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Créé le 26 janv. 06
 * 
 * @author dch
 * 
 *         Représente une en-tête de prestation (JAFPEPR)
 */
public class ALDossier extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDossier = "";
    private String idAllocataire = "";
    private String idAffiliation = "";
    private String idCategorie = "";
    private String typeAllocataire = "";
    private String etat = "";
    private String debutValidite = "";
    private String nombreJourDebut = "";
    private String finValidite = "";
    private String nombreJourFin = "";
    private String debutActivite = "";
    private String finActivite = "";
    private String tauxOccupation = "";
    private String uniteCalcul = "";
    private String tauxReduction = "";
    private String motifReduction = "";
    private String numeroAdressePostale = "";
    private String retenueImpot = "";
    private String impressionDecision = "";
    private String utilisateur = "";
    private String copieDecision1 = "";
    private String copieDecision2 = "";
    private String copieDecision3 = "";
    private String copieDecision4 = "";
    private String copieDecision5 = "";
    private String typeBonification = "";

    /**
     * Renvoie le nom de la table.
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "JAFPDOS";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la base de données.
     * 
     * @exception java.lang.Exception si la lecture des propriétés a échouée
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDossier = statement.dbReadNumeric("EID");
        idAllocataire = statement.dbReadNumeric("EIDP");
        idAffiliation = statement.dbReadNumeric("EIDF");
        idCategorie = statement.dbReadString("EIDC");
        typeAllocataire = statement.dbReadString("ETYPAL");
        etat = statement.dbReadString("EETAT");
        debutValidite = statement.dbReadNumeric("EDVAL");
        nombreJourDebut = statement.dbReadNumeric("ENBJD");
        finValidite = statement.dbReadNumeric("EFVAL");
        nombreJourFin = statement.dbReadNumeric("ENBJF");
        debutActivite = statement.dbReadNumeric("EDACT");
        finActivite = statement.dbReadNumeric("EFACT");
        tauxOccupation = statement.dbReadNumeric("ETOCC");
        uniteCalcul = statement.dbReadString("EUNICA");
        tauxReduction = statement.dbReadNumeric("EREDUC");
        motifReduction = statement.dbReadString("EMOTRD");
        numeroAdressePostale = statement.dbReadNumeric("EADRP");
        retenueImpot = statement.dbReadString("EIMPOT");
        impressionDecision = statement.dbReadString("EIMDEC");
        utilisateur = statement.dbReadString("EUTIL");
        copieDecision1 = statement.dbReadString("ECOP1");
        copieDecision2 = statement.dbReadString("ECOP2");
        copieDecision3 = statement.dbReadString("ECOP3");
        copieDecision4 = statement.dbReadString("ECOP4");
        copieDecision5 = statement.dbReadString("ECOP5");
        typeBonification = statement.dbReadString("EBONIF");
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
        statement.writeKey("EID", _dbWriteNumeric(statement.getTransaction(), idDossier, ""));
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité dans la base de données.
     * 
     * @param statement l'instruction à utiliser
     * @exception java.lang.Exception si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("EID", _dbWriteNumeric(statement.getTransaction(), idDossier, ""));
        statement.writeField("EIDP", _dbWriteNumeric(statement.getTransaction(), idAllocataire, ""));
        statement.writeField("EIDF", _dbWriteNumeric(statement.getTransaction(), idAffiliation, ""));
        statement.writeField("EIDC", _dbWriteString(statement.getTransaction(), idCategorie, ""));
        statement.writeField("ETYPAL", _dbWriteString(statement.getTransaction(), typeAllocataire, ""));
        statement.writeField("EETAT", _dbWriteString(statement.getTransaction(), etat, ""));
        statement.writeField("EDVAL", _dbWriteNumeric(statement.getTransaction(), debutValidite, ""));
        statement.writeField("ENBJD", _dbWriteNumeric(statement.getTransaction(), nombreJourDebut, ""));
        statement.writeField("EFVAL", _dbWriteNumeric(statement.getTransaction(), finValidite, ""));
        statement.writeField("ENBJF", _dbWriteNumeric(statement.getTransaction(), nombreJourFin, ""));
        statement.writeField("EDACT", _dbWriteNumeric(statement.getTransaction(), debutActivite, ""));
        statement.writeField("EFACT", _dbWriteNumeric(statement.getTransaction(), finActivite, ""));
        statement.writeField("ETOCC", _dbWriteNumeric(statement.getTransaction(), tauxOccupation, ""));
        statement.writeField("EUNICA", _dbWriteString(statement.getTransaction(), uniteCalcul, ""));
        statement.writeField("EREDUC", _dbWriteNumeric(statement.getTransaction(), tauxReduction, ""));
        statement.writeField("EMOTRD", _dbWriteString(statement.getTransaction(), motifReduction, ""));
        statement.writeField("EADRP", _dbWriteNumeric(statement.getTransaction(), numeroAdressePostale, ""));
        statement.writeField("EIMPOT", _dbWriteString(statement.getTransaction(), retenueImpot, ""));
        statement.writeField("EIMDEC", _dbWriteString(statement.getTransaction(), impressionDecision, ""));
        statement.writeField("EUTIL", _dbWriteString(statement.getTransaction(), utilisateur, ""));
        statement.writeField("ECOP1", _dbWriteString(statement.getTransaction(), copieDecision1, ""));
        statement.writeField("ECOP2", _dbWriteString(statement.getTransaction(), copieDecision2, ""));
        statement.writeField("ECOP3", _dbWriteString(statement.getTransaction(), copieDecision3, ""));
        statement.writeField("ECOP4", _dbWriteString(statement.getTransaction(), copieDecision4, ""));
        statement.writeField("ECOP5", _dbWriteString(statement.getTransaction(), copieDecision5, ""));
        statement.writeField("EBONIF", _dbWriteString(statement.getTransaction(), typeBonification, ""));
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
    public String getCopieDecision1() {
        return copieDecision1;
    }

    /**
     * @return
     */
    public String getCopieDecision2() {
        return copieDecision2;
    }

    /**
     * @return
     */
    public String getCopieDecision3() {
        return copieDecision3;
    }

    /**
     * @return
     */
    public String getCopieDecision4() {
        return copieDecision4;
    }

    /**
     * @return
     */
    public String getCopieDecision5() {
        return copieDecision5;
    }

    /**
     * @return
     */
    public String getDebutActivite() {
        return debutActivite;
    }

    /**
     * @return
     */
    public String getDebutValidite() {
        return debutValidite;
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
    public String getFinActivite() {
        return finActivite;
    }

    /**
     * @return
     */
    public String getFinValidite() {
        return finValidite;
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
    public String getIdAllocataire() {
        return idAllocataire;
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
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return
     */
    public String getImpressionDecision() {
        return impressionDecision;
    }

    /**
     * @return
     */
    public String getMotifReduction() {
        return motifReduction;
    }

    /**
     * @return
     */
    public String getNombreJourDebut() {
        return nombreJourDebut;
    }

    /**
     * @return
     */
    public String getNombreJourFin() {
        return nombreJourFin;
    }

    /**
     * @return
     */
    public String getNumeroAdressePostale() {
        return numeroAdressePostale;
    }

    /**
     * @return
     */
    public String getRetenueImpot() {
        return retenueImpot;
    }

    /**
     * @return
     */
    public String getTauxOccupation() {
        return tauxOccupation;
    }

    /**
     * @return
     */
    public String getTauxReduction() {
        return tauxReduction;
    }

    /**
     * @return
     */
    public String getTypeAllocataire() {
        return typeAllocataire;
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
    public String getUniteCalcul() {
        return uniteCalcul;
    }

    /**
     * @return
     */
    public String getUtilisateur() {
        return utilisateur;
    }

    /**
     * @param string
     */
    public void setCopieDecision1(String string) {
        copieDecision1 = string;
    }

    /**
     * @param string
     */
    public void setCopieDecision2(String string) {
        copieDecision2 = string;
    }

    /**
     * @param string
     */
    public void setCopieDecision3(String string) {
        copieDecision3 = string;
    }

    /**
     * @param string
     */
    public void setCopieDecision4(String string) {
        copieDecision4 = string;
    }

    /**
     * @param string
     */
    public void setCopieDecision5(String string) {
        copieDecision5 = string;
    }

    /**
     * @param string
     */
    public void setDebutActivite(String string) {
        debutActivite = string;
    }

    /**
     * @param string
     */
    public void setDebutValidite(String string) {
        debutValidite = string;
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
    public void setFinActivite(String string) {
        finActivite = string;
    }

    /**
     * @param string
     */
    public void setFinValidite(String string) {
        finValidite = string;
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
    public void setIdAllocataire(String string) {
        idAllocataire = string;
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
    public void setIdDossier(String string) {
        idDossier = string;
    }

    /**
     * @param string
     */
    public void setImpressionDecision(String string) {
        impressionDecision = string;
    }

    /**
     * @param string
     */
    public void setMotifReduction(String string) {
        motifReduction = string;
    }

    /**
     * @param string
     */
    public void setNombreJourDebut(String string) {
        nombreJourDebut = string;
    }

    /**
     * @param string
     */
    public void setNombreJourFin(String string) {
        nombreJourFin = string;
    }

    /**
     * @param string
     */
    public void setNumeroAdressePostale(String string) {
        numeroAdressePostale = string;
    }

    /**
     * @param string
     */
    public void setRetenueImpot(String string) {
        retenueImpot = string;
    }

    /**
     * @param string
     */
    public void setTauxOccupation(String string) {
        tauxOccupation = string;
    }

    /**
     * @param string
     */
    public void setTauxReduction(String string) {
        tauxReduction = string;
    }

    /**
     * @param string
     */
    public void setTypeAllocataire(String string) {
        typeAllocataire = string;
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
    public void setUniteCalcul(String string) {
        uniteCalcul = string;
    }

    /**
     * @param string
     */
    public void setUtilisateur(String string) {
        utilisateur = string;
    }
}