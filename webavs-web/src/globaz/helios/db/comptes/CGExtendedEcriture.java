package globaz.helios.db.comptes;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.helios.translation.CodeSystem;

/**
 * Insert the type's description here. Creation date: (04.07.2003 08:55:54)
 * 
 * @author: Administrator
 */
public class CGExtendedEcriture extends globaz.globall.db.BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String codeDebitCredit;
    private java.lang.String codeIsoMonnaieCompte;
    private java.lang.String codePeriodeComptable;
    private java.lang.String coursMonnaie;
    private java.lang.String date;
    private java.lang.String idCentreCharge;
    private java.lang.String idCompte;
    private java.lang.String idEcriture;
    private java.lang.String idEnteteEcriture;
    private java.lang.String idExerciceComptable;
    private java.lang.String idExternePlanComptable;
    private java.lang.String idGenre;
    private java.lang.String idLivre;
    private java.lang.String idMandat;
    private java.lang.String idNature;
    private Boolean isProvisoire = new Boolean(false);
    private java.lang.String libelle;
    private java.lang.String libelleDeCentreCharge;
    private java.lang.String libelleDePlanComptable;
    private java.lang.String libelleFrCentreCharge;
    private java.lang.String libelleFrPlanComptable;
    private java.lang.String libelleItCentreCharge;
    private java.lang.String libelleItPlanComptable;
    private java.lang.String montant;
    private java.lang.String montantMonnaie;
    private java.lang.String numeroJournal;
    private java.lang.String piece;

    /**
     * CGExtendedEcriture constructor comment.
     */
    public CGExtendedEcriture() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return _getCollection() + "CGECRIP.idEcriture, " + _getCollection() + "CGECRIP.date, " + _getCollection()
                + "CGECRIP.codeDebitCredit, " + _getCollection() + "CGECRIP.estProvisoire, " + _getCollection()
                + "CGECRIP.coursMonnaie, " + _getCollection() + "CGECRIP.montantMonnaie, " + _getCollection()
                + "CGJOURP.numero, " + _getCollection() + "CGPLANP.libelleFr AS LIBFRPLANCOMPTABLE, "
                + _getCollection() + "CGPLANP.libelleIt AS LIBITPLANCOMPTABLE, " + _getCollection()
                + "CGPLANP.libelleDe AS LIBDEPLANCOMPTABLE, " + _getCollection() + "CGPERIP.code, " + _getCollection()
                + "CGPLANP.idExterne, " + _getCollection() + "CGECRIP.idEnteteEcriture, " + _getCollection()
                + "CGECRIP.libelle, " + _getCollection() + "CGECRIP.piece, " + _getCollection() + "CGECRIP.idLivre, "
                + _getCollection() + "CGECRIP.montant, " + _getCollection() + "CGCOMTP.codeIsoMonnaie, "
                + _getCollection() + "CGPERIP.code, " + _getCollection() + "CGECRIP.idCentreCharge, "
                + _getCollection() + "CGCOMTP.idGenre, " + _getCollection() + "CGCOMTP.idNature, " + _getCollection()
                + "CGCOMTP.idCompte, " + _getCollection() + "CGECRIP.idMandat, " + _getCollection()
                + "CGECRIP.idExerComptable, " + _getCollection() + "CGCCHAP.libelleIt AS LIBITCENTRECHARGE, "
                + _getCollection() + "CGCCHAP.libelleDe AS LIBDECENTRECHARGE, " + _getCollection()
                + "CGCCHAP.libelleFr AS LIBFRCENTRECHARGE ";
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return _getCollection() + "CGECRIP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la base de données
     * 
     * @exception java.lang.Exception
     *                si la lecture des propriétés a échouée
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idEcriture = statement.dbReadNumeric("IDECRITURE");
        date = statement.dbReadDateAMJ("DATE");
        codeDebitCredit = statement.dbReadString("CODEDEBITCREDIT");
        isProvisoire = statement.dbReadBoolean("ESTPROVISOIRE");
        coursMonnaie = statement.dbReadNumeric("COURSMONNAIE");
        montantMonnaie = statement.dbReadNumeric("MONTANTMONNAIE");
        numeroJournal = statement.dbReadNumeric("NUMERO");
        libelleFrPlanComptable = statement.dbReadString("LIBFRPLANCOMPTABLE");
        libelleDePlanComptable = statement.dbReadString("LIBDEPLANCOMPTABLE");
        libelleItPlanComptable = statement.dbReadString("LIBITPLANCOMPTABLE");
        codePeriodeComptable = statement.dbReadString("CODE");
        idExternePlanComptable = statement.dbReadString("IDEXTERNE");
        idEnteteEcriture = statement.dbReadString("IDENTETEECRITURE");
        libelle = statement.dbReadString("LIBELLE");
        piece = statement.dbReadString("PIECE");
        idLivre = statement.dbReadNumeric("IDLIVRE");
        montant = statement.dbReadNumeric("MONTANT");
        codeIsoMonnaieCompte = statement.dbReadString("CODEISOMONNAIE");
        codePeriodeComptable = statement.dbReadString("CODE");
        idCentreCharge = statement.dbReadNumeric("IDCENTRECHARGE");
        idGenre = statement.dbReadNumeric("IDGENRE");
        idNature = statement.dbReadNumeric("IDNATURE");
        idCompte = statement.dbReadNumeric("IDCOMPTE");
        idExerciceComptable = statement.dbReadNumeric("IDEXERCOMPTABLE");
        idMandat = statement.dbReadNumeric("IDMANDAT");
        libelleFrCentreCharge = statement.dbReadString("LIBFRCENTRECHARGE");
        libelleDeCentreCharge = statement.dbReadString("LIBDECENTRECHARGE");
        libelleItCentreCharge = statement.dbReadString("LIBITCENTRECHARGE");
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
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
    }

    public BManager[] getChilds() {
        return null;
    }

    /**
     * Returns the codeDebitCredit.
     * 
     * @return java.lang.String
     */
    public java.lang.String getCodeDebitCredit() {
        return codeDebitCredit;
    }

    /**
     * Returns the codeIsoMonnaieCompte.
     * 
     * @return java.lang.String
     */
    public java.lang.String getCodeIsoMonnaieCompte() {
        return codeIsoMonnaieCompte;
    }

    /**
     * Returns the codePeriodeComptable.
     * 
     * @return java.lang.String
     */
    public java.lang.String getCodePeriodeComptable() {
        return codePeriodeComptable;
    }

    /**
     * Returns the coursMonnaie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getCoursMonnaie() {
        return coursMonnaie;
    }

    /**
     * Returns the date.
     * 
     * @return java.lang.String
     */
    public java.lang.String getDate() {
        return date;
    }

    /**
     * Returns the idCentreCharge.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdCentreCharge() {
        return idCentreCharge;
    }

    /**
     * Returns the idCompte.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdCompte() {
        return idCompte;
    }

    /**
     * Returns the idEcriture.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdEcriture() {
        return idEcriture;
    }

    /**
     * Returns the idEnteteEcriture.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdEnteteEcriture() {
        return idEnteteEcriture;
    }

    /**
     * Returns the idExerciceComptable.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * Returns the idExternePlanComptable.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdExternePlanComptable() {
        return idExternePlanComptable;
    }

    /**
     * Returns the idGenre.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdGenre() {
        return idGenre;
    }

    /**
     * Returns the idLivre.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdLivre() {
        return idLivre;
    }

    /**
     * Returns the idMandat.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdMandat() {
        return idMandat;
    }

    /**
     * Returns the idNature.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdNature() {
        return idNature;
    }

    /**
     * Returns the libelle.
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelle() {
        return libelle;
    }

    /**
     * Returns the libelleDeCentreCharge.
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleDeCentreCharge() {
        return libelleDeCentreCharge;
    }

    /**
     * Returns the libelleDePlanComptable.
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleDePlanComptable() {
        return libelleDePlanComptable;
    }

    /**
     * Returns the libelleFrCentreCharge.
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleFrCentreCharge() {
        return libelleFrCentreCharge;
    }

    /**
     * Returns the libelleFrPlanComptable.
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleFrPlanComptable() {
        return libelleFrPlanComptable;
    }

    /**
     * Returns the libelleItCentreCharge.
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleItCentreCharge() {
        return libelleItCentreCharge;
    }

    /**
     * Returns the libelleItPlanComptable.
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleItPlanComptable() {
        return libelleItPlanComptable;
    }

    /**
     * Returns the montant.
     * 
     * @return java.lang.String
     */
    public java.lang.String getMontant() {
        return montant;
    }

    /**
     * Returns the montantMonnaie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getMontantMonnaie() {
        return montantMonnaie;
    }

    /**
     * Returns the numeroJournal.
     * 
     * @return java.lang.String
     */
    public java.lang.String getNumeroJournal() {
        return numeroJournal;
    }

    /**
     * Returns the piece.
     * 
     * @return java.lang.String
     */
    public java.lang.String getPiece() {
        return piece;
    }

    public boolean isAvoir() {
        if (CodeSystem.CS_CREDIT.equals(codeDebitCredit) || (CodeSystem.CS_EXTOURNE_CREDIT.equals(codeDebitCredit))) {
            return true;
        }
        return false;
    }

    public boolean isDoit() {
        if (CodeSystem.CS_DEBIT.equals(codeDebitCredit) || (CodeSystem.CS_EXTOURNE_DEBIT.equals(codeDebitCredit))) {
            return true;
        }
        return false;
    }

    /**
     * Returns the isProvisoire.
     * 
     * @return Boolean
     */
    public Boolean isProvisoire() {
        return isProvisoire;
    }

    /**
     * Sets the codeDebitCredit.
     * 
     * @param codeDebitCredit
     *            The codeDebitCredit to set
     */
    public void setCodeDebitCredit(java.lang.String codeDebitCredit) {
        this.codeDebitCredit = codeDebitCredit;
    }

    /**
     * Sets the codeIsoMonnaieCompte.
     * 
     * @param codeIsoMonnaieCompte
     *            The codeIsoMonnaieCompte to set
     */
    public void setCodeIsoMonnaieCompte(java.lang.String codeIsoMonnaieCompte) {
        this.codeIsoMonnaieCompte = codeIsoMonnaieCompte;
    }

    /**
     * Sets the codePeriodeComptable.
     * 
     * @param codePeriodeComptable
     *            The codePeriodeComptable to set
     */
    public void setCodePeriodeComptable(java.lang.String codePeriodeComptable) {
        this.codePeriodeComptable = codePeriodeComptable;
    }

    /**
     * Sets the coursMonnaie.
     * 
     * @param coursMonnaie
     *            The coursMonnaie to set
     */
    public void setCoursMonnaie(java.lang.String coursMonnaie) {
        this.coursMonnaie = coursMonnaie;
    }

    /**
     * Sets the date.
     * 
     * @param date
     *            The date to set
     */
    public void setDate(java.lang.String date) {
        this.date = date;
    }

    /**
     * Sets the idCentreCharge.
     * 
     * @param idCentreCharge
     *            The idCentreCharge to set
     */
    public void setIdCentreCharge(java.lang.String idCentreCharge) {
        this.idCentreCharge = idCentreCharge;
    }

    /**
     * Sets the idCompte.
     * 
     * @param idCompte
     *            The idCompte to set
     */
    public void setIdCompte(java.lang.String idCompte) {
        this.idCompte = idCompte;
    }

    /**
     * Sets the idEcriture.
     * 
     * @param idEcriture
     *            The idEcriture to set
     */
    public void setIdEcriture(java.lang.String idEcriture) {
        this.idEcriture = idEcriture;
    }

    /**
     * Sets the idEnteteEcriture.
     * 
     * @param idEnteteEcriture
     *            The idEnteteEcriture to set
     */
    public void setIdEnteteEcriture(java.lang.String idEnteteEcriture) {
        this.idEnteteEcriture = idEnteteEcriture;
    }

    /**
     * Sets the idExerciceComptable.
     * 
     * @param idExerciceComptable
     *            The idExerciceComptable to set
     */
    public void setIdExerciceComptable(java.lang.String idExerciceComptable) {
        this.idExerciceComptable = idExerciceComptable;
    }

    /**
     * Sets the idExternePlanComptable.
     * 
     * @param idExternePlanComptable
     *            The idExternePlanComptable to set
     */
    public void setIdExternePlanComptable(java.lang.String idExternePlanComptable) {
        this.idExternePlanComptable = idExternePlanComptable;
    }

    /**
     * Sets the idGenre.
     * 
     * @param idGenre
     *            The idGenre to set
     */
    public void setIdGenre(java.lang.String idGenre) {
        this.idGenre = idGenre;
    }

    /**
     * Sets the idLivre.
     * 
     * @param idLivre
     *            The idLivre to set
     */
    public void setIdLivre(java.lang.String idLivre) {
        this.idLivre = idLivre;
    }

    /**
     * Sets the idMandat.
     * 
     * @param idMandat
     *            The idMandat to set
     */
    public void setIdMandat(java.lang.String idMandat) {
        this.idMandat = idMandat;
    }

    /**
     * Sets the idNature.
     * 
     * @param idNature
     *            The idNature to set
     */
    public void setIdNature(java.lang.String idNature) {
        this.idNature = idNature;
    }

    /**
     * Sets the isProvisoire.
     * 
     * @param isProvisoire
     *            The isProvisoire to set
     */
    public void setIsProvisoire(Boolean isProvisoire) {
        this.isProvisoire = isProvisoire;
    }

    /**
     * Sets the libelle.
     * 
     * @param libelle
     *            The libelle to set
     */
    public void setLibelle(java.lang.String libelle) {
        this.libelle = libelle;
    }

    /**
     * Sets the libelleDeCentreCharge.
     * 
     * @param libelleDeCentreCharge
     *            The libelleDeCentreCharge to set
     */
    public void setLibelleDeCentreCharge(java.lang.String libelleDeCentreCharge) {
        this.libelleDeCentreCharge = libelleDeCentreCharge;
    }

    /**
     * Sets the libelleDePlanComptable.
     * 
     * @param libelleDePlanComptable
     *            The libelleDePlanComptable to set
     */
    public void setLibelleDePlanComptable(java.lang.String libelleDePlanComptable) {
        this.libelleDePlanComptable = libelleDePlanComptable;
    }

    /**
     * Sets the libelleFrCentreCharge.
     * 
     * @param libelleFrCentreCharge
     *            The libelleFrCentreCharge to set
     */
    public void setLibelleFrCentreCharge(java.lang.String libelleFrCentreCharge) {
        this.libelleFrCentreCharge = libelleFrCentreCharge;
    }

    /**
     * Sets the libelleFrPlanComptable.
     * 
     * @param libelleFrPlanComptable
     *            The libelleFrPlanComptable to set
     */
    public void setLibelleFrPlanComptable(java.lang.String libelleFrPlanComptable) {
        this.libelleFrPlanComptable = libelleFrPlanComptable;
    }

    /**
     * Sets the libelleItCentreCharge.
     * 
     * @param libelleItCentreCharge
     *            The libelleItCentreCharge to set
     */
    public void setLibelleItCentreCharge(java.lang.String libelleItCentreCharge) {
        this.libelleItCentreCharge = libelleItCentreCharge;
    }

    /**
     * Sets the libelleItPlanComptable.
     * 
     * @param libelleItPlanComptable
     *            The libelleItPlanComptable to set
     */
    public void setLibelleItPlanComptable(java.lang.String libelleItPlanComptable) {
        this.libelleItPlanComptable = libelleItPlanComptable;
    }

    /**
     * Sets the montant.
     * 
     * @param montant
     *            The montant to set
     */
    public void setMontant(java.lang.String montant) {
        this.montant = montant;
    }

    /**
     * Sets the montantMonnaie.
     * 
     * @param montantMonnaie
     *            The montantMonnaie to set
     */
    public void setMontantMonnaie(java.lang.String montantMonnaie) {
        this.montantMonnaie = montantMonnaie;
    }

    /**
     * Sets the numeroJournal.
     * 
     * @param numeroJournal
     *            The numeroJournal to set
     */
    public void setNumeroJournal(java.lang.String numeroJournal) {
        this.numeroJournal = numeroJournal;
    }

    /**
     * Sets the piece.
     * 
     * @param piece
     *            The piece to set
     */
    public void setPiece(java.lang.String piece) {
        this.piece = piece;
    }

}
