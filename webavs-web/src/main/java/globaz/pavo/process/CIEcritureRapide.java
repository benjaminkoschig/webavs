package globaz.pavo.process;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CIEcritureRapide extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (KBNANN) */
    private String annee = new String();
    /** (KBTBRA) */
    private String brancheEconomique = new String();
    /** (KBICHO) */
    private String caisseChomage = new String();
    private String caisseTenantCI = new String();
    /** (KBTCAT) */
    private String categoriePersonnel = new String();
    /** (KBTCOD) */
    private String code = new String();
    private String codePrecedant = new String();
    /** (KBTSPE) */
    private String codeSpecial = new String();
    /** (KAIIND) */
    private String compteIndividuelId = new String();
    /** (KBDCEN) */
    private String dateAnnonceCentrale = new String();
    /** (KBDADD) */
    private String dateCiAdditionnel = new String();
    private String dateCloture = new String();
    private String dateOrdre = new String();
    /** (KBIECR) */
    private String ecritureId = new String();
    /** (KBIERE) */
    private String ecritureReference = new String();
    /** (KBITIE) */
    private String employeur = new String();
    private String espionSaisie = new String();
    /** (KBTEXT) */
    private String extourne = new String();
    /** (KBTGEN) */
    private String genreEcriture = new String();
    /** (KCID) */
    private String idJournal = new String();
    /** (KBIDLO) */
    private String idLog = new String();
    /** (KIIREM) */
    private String idRemarque = new String();
    /** (KBTCPT) */
    private String idTypeCompte = new String();
    private String idTypeComptePrecedant = new String();
    /** (KBNMOD) */
    private String moisDebut = new String();
    private String moisDebutPrecedant = new String();
    /** (KBNMOF) */
    private String moisFin = new String();
    private String moisFinPrecedant = new String();
    /** (KBMMON) */
    private String montant = new String();
    private String montantPrecedant = new String();
    private String motifCloture = new String();
    /** (KBLESP) */
    private boolean noSum = false;
    /** (KBNBTA) */
    private String partBta = new String();
    /** (KBIPAR) */
    private String partenaireId = new String();
    /** (KBTPAR) */
    private String particulier = new String();
    /** (KKIRAO) */
    private String rassemblementOuvertureId = new String();

    /**
     * Constructor for CIEcritureRapide.
     */
    public CIEcritureRapide() {
        super();
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "CIECRIP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        ecritureId = statement.dbReadNumeric("KBIECR");
        compteIndividuelId = statement.dbReadNumeric("KAIIND");
        rassemblementOuvertureId = statement.dbReadNumeric("KKIRAO");
        idJournal = statement.dbReadNumeric("KCID");
        idRemarque = statement.dbReadNumeric("KIIREM");
        ecritureReference = statement.dbReadNumeric("KBIERE");
        employeur = statement.dbReadNumeric("KBITIE");
        partenaireId = statement.dbReadNumeric("KBIPAR");
        extourne = statement.dbReadNumeric("KBTEXT");
        // On sauve l'extourne dans un backup pour mise à jour
        // previousExtourne = statement.dbReadNumeric("KBTEXT");
        genreEcriture = statement.dbReadNumeric("KBTGEN");
        particulier = statement.dbReadNumeric("KBTPAR");
        partBta = statement.dbReadNumeric("KBNBTA");
        codeSpecial = statement.dbReadNumeric("KBTSPE");
        moisDebut = statement.dbReadNumeric("KBNMOD");
        moisDebutPrecedant = moisDebut;
        moisFin = statement.dbReadNumeric("KBNMOF");
        moisFinPrecedant = moisFin;
        annee = statement.dbReadNumeric("KBNANN");
        montant = statement.dbReadNumeric("KBMMON", 2);
        montantPrecedant = montant;
        // On sauve le montant dans un backup pour mise à jour
        // previousMontant = statement.dbReadNumeric("KBMMON",2);
        code = statement.dbReadNumeric("KBTCOD");
        codePrecedant = code;
        brancheEconomique = statement.dbReadNumeric("KBTBRA");
        idTypeCompte = statement.dbReadNumeric("KBTCPT");
        idTypeComptePrecedant = idTypeCompte;
        dateAnnonceCentrale = statement.dbReadDateAMJ("KBDCEN");
        caisseChomage = statement.dbReadNumeric("KBICHO");
        categoriePersonnel = statement.dbReadNumeric("KBTCAT");
        dateCiAdditionnel = statement.dbReadDateAMJ("KBDADD");
        idLog = statement.dbReadNumeric("KBIDLO");
        espionSaisie = statement.dbReadString("KBLESP");
        caisseTenantCI = statement.dbReadNumeric("KBCAIT");

        dateCloture = statement.dbReadDateAMJ("KKDCLO");
        dateOrdre = statement.dbReadDateAMJ("KKDORD");
        motifCloture = statement.dbReadString("KKTARC");
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * Returns the annee.
     * 
     * @return String
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * Returns the brancheEconomique.
     * 
     * @return String
     */
    public String getBrancheEconomique() {
        return brancheEconomique;
    }

    /**
     * Returns the caisseChomage.
     * 
     * @return String
     */
    public String getCaisseChomage() {
        return caisseChomage;
    }

    /**
     * Returns the caisseTenantCI.
     * 
     * @return String
     */
    public String getCaisseTenantCI() {
        return caisseTenantCI;
    }

    /**
     * Returns the categoriePersonnel.
     * 
     * @return String
     */
    public String getCategoriePersonnel() {
        return categoriePersonnel;
    }

    /**
     * Returns the code.
     * 
     * @return String
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the codePrecedant.
     * 
     * @return String
     */
    public String getCodePrecedant() {
        return codePrecedant;
    }

    /**
     * Returns the codeSpecial.
     * 
     * @return String
     */
    public String getCodeSpecial() {
        return codeSpecial;
    }

    /**
     * Returns the compteIndividuelId.
     * 
     * @return String
     */
    public String getCompteIndividuelId() {
        return compteIndividuelId;
    }

    /**
     * Returns the dateAnnonceCentrale.
     * 
     * @return String
     */
    public String getDateAnnonceCentrale() {
        return dateAnnonceCentrale;
    }

    /**
     * Returns the dateCiAdditionnel.
     * 
     * @return String
     */
    public String getDateCiAdditionnel() {
        return dateCiAdditionnel;
    }

    /**
     * Returns the dateCloture.
     * 
     * @return String
     */
    public String getDateCloture() {
        return dateCloture;
    }

    /**
     * Returns the dateOrdre.
     * 
     * @return String
     */
    public String getDateOrdre() {
        return dateOrdre;
    }

    /**
     * Returns the ecritureId.
     * 
     * @return String
     */
    public String getEcritureId() {
        return ecritureId;
    }

    /**
     * Returns the ecritureReference.
     * 
     * @return String
     */
    public String getEcritureReference() {
        return ecritureReference;
    }

    /**
     * Returns the employeur.
     * 
     * @return String
     */
    public String getEmployeur() {
        return employeur;
    }

    /**
     * Returns the espionSaisie.
     * 
     * @return String
     */
    public String getEspionSaisie() {
        return espionSaisie;
    }

    /**
     * Returns the extourne.
     * 
     * @return String
     */
    public String getExtourne() {
        return extourne;
    }

    /**
     * Returns the genreEcriture.
     * 
     * @return String
     */
    public String getGenreEcriture() {
        return genreEcriture;
    }

    /**
     * Returns the idJournal.
     * 
     * @return String
     */
    public String getIdJournal() {
        return idJournal;
    }

    /**
     * Returns the idLog.
     * 
     * @return String
     */
    public String getIdLog() {
        return idLog;
    }

    /**
     * Returns the idRemarque.
     * 
     * @return String
     */
    public String getIdRemarque() {
        return idRemarque;
    }

    /**
     * Returns the idTypeCompte.
     * 
     * @return String
     */
    public String getIdTypeCompte() {
        return idTypeCompte;
    }

    /**
     * Returns the idTypeComptePrecedant.
     * 
     * @return String
     */
    public String getIdTypeComptePrecedant() {
        return idTypeComptePrecedant;
    }

    /**
     * Returns the moisDebut.
     * 
     * @return String
     */
    public String getMoisDebut() {
        return moisDebut;
    }

    /**
     * Returns the moisDebutPrecedant.
     * 
     * @return String
     */
    public String getMoisDebutPrecedant() {
        return moisDebutPrecedant;
    }

    /**
     * Returns the moisFin.
     * 
     * @return String
     */
    public String getMoisFin() {
        return moisFin;
    }

    /**
     * Returns the moisFinPrecedant.
     * 
     * @return String
     */
    public String getMoisFinPrecedant() {
        return moisFinPrecedant;
    }

    /**
     * Returns the montant.
     * 
     * @return String
     */
    public String getMontant() {
        return montant;
    }

    /**
     * Returns the montantPrecedant.
     * 
     * @return String
     */
    public String getMontantPrecedant() {
        return montantPrecedant;
    }

    /**
     * Returns the motifCloture.
     * 
     * @return String
     */
    public String getMotifCloture() {
        return motifCloture;
    }

    /**
     * Returns the partBta.
     * 
     * @return String
     */
    public String getPartBta() {
        return partBta;
    }

    /**
     * Returns the partenaireId.
     * 
     * @return String
     */
    public String getPartenaireId() {
        return partenaireId;
    }

    /**
     * Returns the particulier.
     * 
     * @return String
     */
    public String getParticulier() {
        return particulier;
    }

    /**
     * Returns the rassemblementOuvertureId.
     * 
     * @return String
     */
    public String getRassemblementOuvertureId() {
        return rassemblementOuvertureId;
    }

    /**
     * Returns the noSum.
     * 
     * @return boolean
     */
    public boolean isNoSum() {
        return noSum;
    }

    /**
     * Sets the annee.
     * 
     * @param annee
     *            The annee to set
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /**
     * Sets the brancheEconomique.
     * 
     * @param brancheEconomique
     *            The brancheEconomique to set
     */
    public void setBrancheEconomique(String brancheEconomique) {
        this.brancheEconomique = brancheEconomique;
    }

    /**
     * Sets the caisseChomage.
     * 
     * @param caisseChomage
     *            The caisseChomage to set
     */
    public void setCaisseChomage(String caisseChomage) {
        this.caisseChomage = caisseChomage;
    }

    /**
     * Sets the caisseTenantCI.
     * 
     * @param caisseTenantCI
     *            The caisseTenantCI to set
     */
    public void setCaisseTenantCI(String caisseTenantCI) {
        this.caisseTenantCI = caisseTenantCI;
    }

    /**
     * Sets the categoriePersonnel.
     * 
     * @param categoriePersonnel
     *            The categoriePersonnel to set
     */
    public void setCategoriePersonnel(String categoriePersonnel) {
        this.categoriePersonnel = categoriePersonnel;
    }

    /**
     * Sets the code.
     * 
     * @param code
     *            The code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Sets the codePrecedant.
     * 
     * @param codePrecedant
     *            The codePrecedant to set
     */
    public void setCodePrecedant(String codePrecedant) {
        this.codePrecedant = codePrecedant;
    }

    /**
     * Sets the codeSpecial.
     * 
     * @param codeSpecial
     *            The codeSpecial to set
     */
    public void setCodeSpecial(String codeSpecial) {
        this.codeSpecial = codeSpecial;
    }

    /**
     * Sets the compteIndividuelId.
     * 
     * @param compteIndividuelId
     *            The compteIndividuelId to set
     */
    public void setCompteIndividuelId(String compteIndividuelId) {
        this.compteIndividuelId = compteIndividuelId;
    }

    /**
     * Sets the dateAnnonceCentrale.
     * 
     * @param dateAnnonceCentrale
     *            The dateAnnonceCentrale to set
     */
    public void setDateAnnonceCentrale(String dateAnnonceCentrale) {
        this.dateAnnonceCentrale = dateAnnonceCentrale;
    }

    /**
     * Sets the dateCiAdditionnel.
     * 
     * @param dateCiAdditionnel
     *            The dateCiAdditionnel to set
     */
    public void setDateCiAdditionnel(String dateCiAdditionnel) {
        this.dateCiAdditionnel = dateCiAdditionnel;
    }

    /**
     * Sets the dateCloture.
     * 
     * @param dateCloture
     *            The dateCloture to set
     */
    public void setDateCloture(String dateCloture) {
        this.dateCloture = dateCloture;
    }

    /**
     * Sets the dateOrdre.
     * 
     * @param dateOrdre
     *            The dateOrdre to set
     */
    public void setDateOrdre(String dateOrdre) {
        this.dateOrdre = dateOrdre;
    }

    /**
     * Sets the ecritureId.
     * 
     * @param ecritureId
     *            The ecritureId to set
     */
    public void setEcritureId(String ecritureId) {
        this.ecritureId = ecritureId;
    }

    /**
     * Sets the ecritureReference.
     * 
     * @param ecritureReference
     *            The ecritureReference to set
     */
    public void setEcritureReference(String ecritureReference) {
        this.ecritureReference = ecritureReference;
    }

    /**
     * Sets the employeur.
     * 
     * @param employeur
     *            The employeur to set
     */
    public void setEmployeur(String employeur) {
        this.employeur = employeur;
    }

    /**
     * Sets the espionSaisie.
     * 
     * @param espionSaisie
     *            The espionSaisie to set
     */
    public void setEspionSaisie(String espionSaisie) {
        this.espionSaisie = espionSaisie;
    }

    /**
     * Sets the extourne.
     * 
     * @param extourne
     *            The extourne to set
     */
    public void setExtourne(String extourne) {
        this.extourne = extourne;
    }

    /**
     * Sets the genreEcriture.
     * 
     * @param genreEcriture
     *            The genreEcriture to set
     */
    public void setGenreEcriture(String genreEcriture) {
        this.genreEcriture = genreEcriture;
    }

    /**
     * Sets the idJournal.
     * 
     * @param idJournal
     *            The idJournal to set
     */
    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    /**
     * Sets the idLog.
     * 
     * @param idLog
     *            The idLog to set
     */
    public void setIdLog(String idLog) {
        this.idLog = idLog;
    }

    /**
     * Sets the idRemarque.
     * 
     * @param idRemarque
     *            The idRemarque to set
     */
    public void setIdRemarque(String idRemarque) {
        this.idRemarque = idRemarque;
    }

    /**
     * Sets the idTypeCompte.
     * 
     * @param idTypeCompte
     *            The idTypeCompte to set
     */
    public void setIdTypeCompte(String idTypeCompte) {
        this.idTypeCompte = idTypeCompte;
    }

    /**
     * Sets the idTypeComptePrecedant.
     * 
     * @param idTypeComptePrecedant
     *            The idTypeComptePrecedant to set
     */
    public void setIdTypeComptePrecedant(String idTypeComptePrecedant) {
        this.idTypeComptePrecedant = idTypeComptePrecedant;
    }

    /**
     * Sets the moisDebut.
     * 
     * @param moisDebut
     *            The moisDebut to set
     */
    public void setMoisDebut(String moisDebut) {
        this.moisDebut = moisDebut;
    }

    /**
     * Sets the moisDebutPrecedant.
     * 
     * @param moisDebutPrecedant
     *            The moisDebutPrecedant to set
     */
    public void setMoisDebutPrecedant(String moisDebutPrecedant) {
        this.moisDebutPrecedant = moisDebutPrecedant;
    }

    /**
     * Sets the moisFin.
     * 
     * @param moisFin
     *            The moisFin to set
     */
    public void setMoisFin(String moisFin) {
        this.moisFin = moisFin;
    }

    /**
     * Sets the moisFinPrecedant.
     * 
     * @param moisFinPrecedant
     *            The moisFinPrecedant to set
     */
    public void setMoisFinPrecedant(String moisFinPrecedant) {
        this.moisFinPrecedant = moisFinPrecedant;
    }

    /**
     * Sets the montant.
     * 
     * @param montant
     *            The montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * Sets the montantPrecedant.
     * 
     * @param montantPrecedant
     *            The montantPrecedant to set
     */
    public void setMontantPrecedant(String montantPrecedant) {
        this.montantPrecedant = montantPrecedant;
    }

    /**
     * Sets the motifCloture.
     * 
     * @param motifCloture
     *            The motifCloture to set
     */
    public void setMotifCloture(String motifCloture) {
        this.motifCloture = motifCloture;
    }

    /**
     * Sets the noSum.
     * 
     * @param noSum
     *            The noSum to set
     */
    public void setNoSum(boolean noSum) {
        this.noSum = noSum;
    }

    /**
     * Sets the partBta.
     * 
     * @param partBta
     *            The partBta to set
     */
    public void setPartBta(String partBta) {
        this.partBta = partBta;
    }

    /**
     * Sets the partenaireId.
     * 
     * @param partenaireId
     *            The partenaireId to set
     */
    public void setPartenaireId(String partenaireId) {
        this.partenaireId = partenaireId;
    }

    /**
     * Sets the particulier.
     * 
     * @param particulier
     *            The particulier to set
     */
    public void setParticulier(String particulier) {
        this.particulier = particulier;
    }

    /**
     * Sets the rassemblementOuvertureId.
     * 
     * @param rassemblementOuvertureId
     *            The rassemblementOuvertureId to set
     */
    public void setRassemblementOuvertureId(String rassemblementOuvertureId) {
        this.rassemblementOuvertureId = rassemblementOuvertureId;
    }

}
