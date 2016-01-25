package globaz.helios.db.comptes;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * @author user
 * @version 1.1
 * 
 */
public class CGExtendedGrandLivre extends globaz.globall.db.BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String code = new String();
    private String codeDebitCredit = new String();
    private String codeIsoContreCompte = new String();
    private String codeIsoMonnaie = new String();
    private String coursMonnaie = new String();
    private String coursMonnaieContreEcriture = new String();
    private String date = new String();
    private String idCentreCharge = new String();
    private String idCompte = new String();
    private String idContreEcriture = new String();
    private String idContrepartieAvoir = new String();
    private String idContrepartieDoit = new String();
    private String idEcriture = new String();
    private String idExerciceComptable = new String();
    private String idExtCpteContrEcri = new String();
    private String idExterne = new String();
    private String idGenre = new String();
    private String idLivre = new String();
    private String idMandat = new String();
    private String idNature = new String();
    private String idNatureContreCompte = new String();
    private String idPeriodeComptable = new String();
    private Boolean isProvisoire = new Boolean(false);
    private String libDeCentreCharge = new String();
    private String libDeCptContreEcri = new String();
    private String libelle = new String();
    private String libelleDe = new String();
    private String libelleFr = new String();
    private String libelleIt = new String();
    private String libFrCentreCharge = new String();
    private String libFrCptContreEcri = new String();
    private String libItCentreCharge = new String();
    private String libItCptContreEcri = new String();
    private String montant = new String();
    private String montantContreEcriture = new String();
    private String montantMonnaie = new String();
    private String montantMonnaieContreEcriture = new String();
    private String nombreAvoir = new String();
    private String nombreDoit = new String();
    private String numero = new String();
    private String piece = new String();

    /**
     * Commentaire relatif au constructeur CGCompteOfas
     */
    public CGExtendedGrandLivre() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
    }

    @Override
    protected String _getFields(BStatement statement) {

        return _getCollection() + "CGPLANP.idExterne, " + _getCollection() + "CGPLANP.libelleFr, " + _getCollection()
                + "CGPLANP.libelleDe, " + _getCollection() + "CGPLANP.libelleIt, " + _getCollection()
                + "CGECRIP.date, " + _getCollection() + "CGJOURP.numero, " + _getCollection() + "CGECRIP.idEcriture, "
                + "CONTRE_ECRITURE.idEcriture AS idContreEcriture, " + _getCollection()
                + "CGECREP.idContrepartieDoit, " + _getCollection() + "CGECREP.idContrepartieAvoi, " + _getCollection()
                + "CGECREP.nombreDoit, " + _getCollection() + "CGECREP.nombreAvoir, "
                + "PC_CPT_CTR_ECRIT.idExterne AS idExtCpteContrEcri, "
                + "PC_CPT_CTR_ECRIT.libelleFr AS libFrCptContreEcri, "
                + "PC_CPT_CTR_ECRIT.libelleDe AS libDeCptContreEcri, "
                + "PC_CPT_CTR_ECRIT.libelleIt AS libItCptContreEcri, " + "COMPTE_CONTRE_ECRI.idGenre, "
                + "CONTRE_ECRITURE.idCentreCharge, " + _getCollection() + "CGCCHAP.libelleFR AS libFrCentreCharge, "
                + _getCollection() + "CGCCHAP.libelleDe AS libDeCentreCharge, " + _getCollection()
                + "CGCCHAP.libelleIt AS libItCentreCharge, " + _getCollection() + "CGECRIP.libelle, "
                + _getCollection() + "CGECRIP.piece, " + _getCollection() + "CGECRIP.idLivre, " + _getCollection()
                + "CGPERIP.code, " + _getCollection() + "CGPERIP.idPeriodeComptable, " + _getCollection()
                + "CGCOMTP.idCompte, " + _getCollection() + "CGCOMTP.IDNATURE, "
                + "COMPTE_CONTRE_ECRI.IDNATURE AS idNatureCtrCompte, " + _getCollection() + "CGCOMTP.CODEISOMONNAIE, "
                + _getCollection() + "CGECRIP.coursMonnaie, " + _getCollection() + "CGECRIP.codeDebitCredit, "
                + _getCollection() + "CGECRIP.montant, " + _getCollection() + "CGECRIP.montantMonnaie, "
                + _getCollection() + "CGECRIP.estProvisoire, " + _getCollection() + "CGEXERP.idExerComptable, "
                + "COMPTE_CONTRE_ECRI.codeIsoMonnaie AS codeIsoContreCpt, "
                + "CONTRE_ECRITURE.montant AS montantCtrEcri, "
                + "CONTRE_ECRITURE.montantMonnaie AS montantMECtrEcri, "
                + "CONTRE_ECRITURE.coursMonnaie AS coursMonnaieCtrEcr, " + _getCollection() + "CGEXERP.idMandat ";
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return _getCollection() + "CGCOMTP";
    }

    /**
     * cfcp.CGCOMTP* read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {

        idExterne = statement.dbReadString("IDEXTERNE");
        libelleFr = statement.dbReadString("LIBELLEFR");
        libelleDe = statement.dbReadString("LIBELLEDE");
        libelleIt = statement.dbReadString("LIBELLEIT");
        date = statement.dbReadNumeric("DATE");
        numero = statement.dbReadNumeric("NUMERO");
        idEcriture = statement.dbReadNumeric("IDECRITURE");
        idContreEcriture = statement.dbReadNumeric("IDCONTREECRITURE");
        idContrepartieDoit = statement.dbReadNumeric("IDCONTREPARTIEDOIT");
        idContrepartieAvoir = statement.dbReadNumeric("IDCONTREPARTIEAVOI");
        nombreDoit = statement.dbReadNumeric("NOMBREDOIT");
        nombreAvoir = statement.dbReadNumeric("NOMBREAVOIR");
        idExtCpteContrEcri = statement.dbReadString("IDEXTCPTECONTRECRI");

        libFrCptContreEcri = statement.dbReadString("LIBFRCPTCONTREECRI");
        libDeCptContreEcri = statement.dbReadString("LIBDECPTCONTREECRI");
        libItCptContreEcri = statement.dbReadString("LIBITCPTCONTREECRI");
        idGenre = statement.dbReadNumeric("IDGENRE");
        idCentreCharge = statement.dbReadNumeric("IDCENTRECHARGE");
        libFrCentreCharge = statement.dbReadString("LIBFRCENTRECHARGE");
        libDeCentreCharge = statement.dbReadString("LIBDECENTRECHARGE");
        libItCentreCharge = statement.dbReadString("LIBITCENTRECHARGE");
        libelle = statement.dbReadString("LIBELLE");
        piece = statement.dbReadString("PIECE");
        idLivre = statement.dbReadNumeric("IDLIVRE");
        code = statement.dbReadString("CODE");
        idCompte = statement.dbReadNumeric("IDCOMPTE");
        idNature = statement.dbReadNumeric("IDNATURE");
        codeIsoMonnaie = statement.dbReadString("CODEISOMONNAIE");
        coursMonnaie = statement.dbReadNumeric("COURSMONNAIE");
        coursMonnaieContreEcriture = statement.dbReadNumeric("COURSMONNAIECTRECR");
        codeDebitCredit = statement.dbReadNumeric("CODEDEBITCREDIT");
        montant = statement.dbReadNumeric("MONTANT");
        montantMonnaie = statement.dbReadNumeric("MONTANTMONNAIE");
        isProvisoire = statement.dbReadBoolean("ESTPROVISOIRE");
        idExerciceComptable = statement.dbReadNumeric("IDEXERCOMPTABLE");
        idMandat = statement.dbReadNumeric("IDMANDAT");
        idPeriodeComptable = statement.dbReadNumeric("IDPERIODECOMPTABLE");

        codeIsoContreCompte = statement.dbReadString("CODEISOCONTRECPT");
        montantContreEcriture = statement.dbReadNumeric("MONTANTCTRECRI");
        montantMonnaieContreEcriture = statement.dbReadNumeric("MONTANTMECTRECRI");
        idNatureContreCompte = statement.dbReadNumeric("IDNATURECTRCOMPTE");

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
     * Returns the code.
     * 
     * @return String
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the codeDebitCredit.
     * 
     * @return String
     */
    public String getCodeDebitCredit() {
        return codeDebitCredit;
    }

    /**
     * Returns the codeIsoContreCompte.
     * 
     * @return String
     */
    public String getCodeIsoContreCompte() {
        return codeIsoContreCompte;
    }

    /**
     * Returns the codeIsoMonnaie.
     * 
     * @return String
     */
    public String getCodeIsoMonnaie() {
        return codeIsoMonnaie;
    }

    /**
     * Returns the coursMonnaie.
     * 
     * @return String
     */
    public String getCoursMonnaie() {
        return coursMonnaie;
    }

    /**
     * Returns the coursMonnaieContreEcriture.
     * 
     * @return String
     */
    public String getCoursMonnaieContreEcriture() {
        return coursMonnaieContreEcriture;
    }

    /**
     * Returns the date.
     * 
     * @return String
     */
    public String getDate() {
        return date;
    }

    /**
     * Returns the idCentreCharge.
     * 
     * @return String
     */
    public String getIdCentreCharge() {
        return idCentreCharge;
    }

    /**
     * Returns the idCompte.
     * 
     * @return String
     */
    public String getIdCompte() {
        return idCompte;
    }

    /**
     * Returns the idContreEcriture.
     * 
     * @return String
     */
    public String getIdContreEcriture() {
        return idContreEcriture;
    }

    /**
     * Returns the idContrepartieAvoir.
     * 
     * @return String
     */
    public String getIdContrepartieAvoir() {
        return idContrepartieAvoir;
    }

    /**
     * Returns the idContrepartieDoit.
     * 
     * @return String
     */
    public String getIdContrepartieDoit() {
        return idContrepartieDoit;
    }

    /**
     * Returns the idEcriture.
     * 
     * @return String
     */
    public String getIdEcriture() {
        return idEcriture;
    }

    /**
     * Returns the idExerciceComptable.
     * 
     * @return String
     */
    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * Returns the idExtCpteContrEcri.
     * 
     * @return String
     */
    public String getIdExtCpteContrEcri() {
        return idExtCpteContrEcri;
    }

    /**
     * Returns the idExterne.
     * 
     * @return String
     */
    public String getIdExterne() {
        return idExterne;
    }

    /**
     * Returns the idGenre.
     * 
     * @return String
     */
    public String getIdGenre() {
        return idGenre;
    }

    /**
     * Returns the idLivre.
     * 
     * @return String
     */
    public String getIdLivre() {
        return idLivre;
    }

    /**
     * Returns the idMandat.
     * 
     * @return String
     */
    public String getIdMandat() {
        return idMandat;
    }

    /**
     * Returns the idNature.
     * 
     * @return String
     */
    public String getIdNature() {
        return idNature;
    }

    /**
     * Returns the idNatureContreCompte.
     * 
     * @return String
     */
    public String getIdNatureContreCompte() {
        return idNatureContreCompte;
    }

    /**
     * Returns the idPeriodeComptable.
     * 
     * @return String
     */
    public String getIdPeriodeComptable() {
        return idPeriodeComptable;
    }

    /**
     * Returns the libDeCentreCharge.
     * 
     * @return String
     */
    public String getLibDeCentreCharge() {
        return libDeCentreCharge;
    }

    /**
     * Returns the libDeCptContreEcri.
     * 
     * @return String
     */
    public String getLibDeCptContreEcri() {
        return libDeCptContreEcri;
    }

    /**
     * Returns the libelle.
     * 
     * @return String
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Returns the libelleDe.
     * 
     * @return String
     */
    public String getLibelleDe() {
        return libelleDe;
    }

    /**
     * Returns the libelleFr.
     * 
     * @return String
     */
    public String getLibelleFr() {
        return libelleFr;
    }

    /**
     * Returns the libelleIt.
     * 
     * @return String
     */
    public String getLibelleIt() {
        return libelleIt;
    }

    /**
     * Returns the libFrCentreCharge.
     * 
     * @return String
     */
    public String getLibFrCentreCharge() {
        return libFrCentreCharge;
    }

    /**
     * Returns the libFrCptContreEcri.
     * 
     * @return String
     */
    public String getLibFrCptContreEcri() {
        return libFrCptContreEcri;
    }

    /**
     * Returns the libItCentreCharge.
     * 
     * @return String
     */
    public String getLibItCentreCharge() {
        return libItCentreCharge;
    }

    /**
     * Returns the libItCptContreEcri.
     * 
     * @return String
     */
    public String getLibItCptContreEcri() {
        return libItCptContreEcri;
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
     * Returns the montantContreEcriture.
     * 
     * @return String
     */
    public String getMontantContreEcriture() {
        return montantContreEcriture;
    }

    /**
     * Returns the montantMonnaie.
     * 
     * @return String
     */
    public String getMontantMonnaie() {
        return montantMonnaie;
    }

    /**
     * Returns the montantMonnaieContreEcriture.
     * 
     * @return String
     */
    public String getMontantMonnaieContreEcriture() {
        return montantMonnaieContreEcriture;
    }

    /**
     * Returns the nombreAvoir.
     * 
     * @return String
     */
    public String getNombreAvoir() {
        return nombreAvoir;
    }

    /**
     * Returns the nombreDoit.
     * 
     * @return String
     */
    public String getNombreDoit() {
        return nombreDoit;
    }

    /**
     * Returns the numero.
     * 
     * @return String
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Returns the piece.
     * 
     * @return String
     */
    public String getPiece() {
        return piece;
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
     * Sets the code.
     * 
     * @param code
     *            The code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Sets the codeDebitCredit.
     * 
     * @param codeDebitCredit
     *            The codeDebitCredit to set
     */
    public void setCodeDebitCredit(String codeDebitCredit) {
        this.codeDebitCredit = codeDebitCredit;
    }

    /**
     * Sets the codeIsoContreCompte.
     * 
     * @param codeIsoContreCompte
     *            The codeIsoContreCompte to set
     */
    public void setCodeIsoContreCompte(String codeIsoContreCompte) {
        this.codeIsoContreCompte = codeIsoContreCompte;
    }

    /**
     * Sets the codeIsoMonnaie.
     * 
     * @param codeIsoMonnaie
     *            The codeIsoMonnaie to set
     */
    public void setCodeIsoMonnaie(String codeIsoMonnaie) {
        this.codeIsoMonnaie = codeIsoMonnaie;
    }

    /**
     * Sets the coursMonnaie.
     * 
     * @param coursMonnaie
     *            The coursMonnaie to set
     */
    public void setCoursMonnaie(String coursMonnaie) {
        this.coursMonnaie = coursMonnaie;
    }

    /**
     * Sets the coursMonnaieContreEcriture.
     * 
     * @param coursMonnaieContreEcriture
     *            The coursMonnaieContreEcriture to set
     */
    public void setCoursMonnaieContreEcriture(String coursMonnaieContreEcriture) {
        this.coursMonnaieContreEcriture = coursMonnaieContreEcriture;
    }

    /**
     * Sets the date.
     * 
     * @param date
     *            The date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Sets the idCentreCharge.
     * 
     * @param idCentreCharge
     *            The idCentreCharge to set
     */
    public void setIdCentreCharge(String idCentreCharge) {
        this.idCentreCharge = idCentreCharge;
    }

    /**
     * Sets the idCompte.
     * 
     * @param idCompte
     *            The idCompte to set
     */
    public void setIdCompte(String idCompte) {
        this.idCompte = idCompte;
    }

    /**
     * Sets the idContreEcriture.
     * 
     * @param idContreEcriture
     *            The idContreEcriture to set
     */
    public void setIdContreEcriture(String idContreEcriture) {
        this.idContreEcriture = idContreEcriture;
    }

    /**
     * Sets the idContrepartieAvoir.
     * 
     * @param idContrepartieAvoir
     *            The idContrepartieAvoir to set
     */
    public void setIdContrepartieAvoir(String idContrepartieAvoir) {
        this.idContrepartieAvoir = idContrepartieAvoir;
    }

    /**
     * Sets the idContrepartieDoit.
     * 
     * @param idContrepartieDoit
     *            The idContrepartieDoit to set
     */
    public void setIdContrepartieDoit(String idContrepartieDoit) {
        this.idContrepartieDoit = idContrepartieDoit;
    }

    /**
     * Sets the idEcriture.
     * 
     * @param idEcriture
     *            The idEcriture to set
     */
    public void setIdEcriture(String idEcriture) {
        this.idEcriture = idEcriture;
    }

    /**
     * Sets the idExerciceComptable.
     * 
     * @param idExerciceComptable
     *            The idExerciceComptable to set
     */
    public void setIdExerciceComptable(String idExerciceComptable) {
        this.idExerciceComptable = idExerciceComptable;
    }

    /**
     * Sets the idExtCpteContrEcri.
     * 
     * @param idExtCpteContrEcri
     *            The idExtCpteContrEcri to set
     */
    public void setIdExtCpteContrEcri(String idExterneCompteContreEcriture) {
        idExtCpteContrEcri = idExterneCompteContreEcriture;
    }

    /**
     * Sets the idExterne.
     * 
     * @param idExterne
     *            The idExterne to set
     */
    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    /**
     * Sets the idGenre.
     * 
     * @param idGenre
     *            The idGenre to set
     */
    public void setIdGenre(String idGenre) {
        this.idGenre = idGenre;
    }

    /**
     * Sets the idLivre.
     * 
     * @param idLivre
     *            The idLivre to set
     */
    public void setIdLivre(String idLivre) {
        this.idLivre = idLivre;
    }

    /**
     * Sets the idMandat.
     * 
     * @param idMandat
     *            The idMandat to set
     */
    public void setIdMandat(String idMandat) {
        this.idMandat = idMandat;
    }

    /**
     * Sets the idNature.
     * 
     * @param idNature
     *            The idNature to set
     */
    public void setIdNature(String idNature) {
        this.idNature = idNature;
    }

    /**
     * Sets the idNatureContreCompte.
     * 
     * @param idNatureContreCompte
     *            The idNatureContreCompte to set
     */
    public void setIdNatureContreCompte(String idNatureContreCompte) {
        this.idNatureContreCompte = idNatureContreCompte;
    }

    /**
     * Sets the idPeriodeComptable.
     * 
     * @param idPeriodeComptable
     *            The idPeriodeComptable to set
     */
    public void setIdPeriodeComptable(String idPeriodeComptable) {
        this.idPeriodeComptable = idPeriodeComptable;
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
     * Sets the libDeCentreCharge.
     * 
     * @param libDeCentreCharge
     *            The libDeCentreCharge to set
     */
    public void setLibDeCentreCharge(String libelleDeCentreCharge) {
        libDeCentreCharge = libelleDeCentreCharge;
    }

    /**
     * Sets the libDeCptContreEcri.
     * 
     * @param libDeCptContreEcri
     *            The libDeCptContreEcri to set
     */
    public void setLibDeCptContreEcri(String libelleDeCompteContreEcriture) {
        libDeCptContreEcri = libelleDeCompteContreEcriture;
    }

    /**
     * Sets the libelle.
     * 
     * @param libelle
     *            The libelle to set
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Sets the libelleDe.
     * 
     * @param libelleDe
     *            The libelleDe to set
     */
    public void setLibelleDe(String libelleDe) {
        this.libelleDe = libelleDe;
    }

    /**
     * Sets the libelleFr.
     * 
     * @param libelleFr
     *            The libelleFr to set
     */
    public void setLibelleFr(String libelleFr) {
        this.libelleFr = libelleFr;
    }

    /**
     * Sets the libelleIt.
     * 
     * @param libelleIt
     *            The libelleIt to set
     */
    public void setLibelleIt(String libelleIt) {
        this.libelleIt = libelleIt;
    }

    /**
     * Sets the libFrCentreCharge.
     * 
     * @param libFrCentreCharge
     *            The libFrCentreCharge to set
     */
    public void setLibFrCentreCharge(String libelleFrCentreCharge) {
        libFrCentreCharge = libelleFrCentreCharge;
    }

    /**
     * Sets the libFrCptContreEcri.
     * 
     * @param libFrCptContreEcri
     *            The libFrCptContreEcri to set
     */
    public void setLibFrCptContreEcri(String libelleFrCompteContreEcriture) {
        libFrCptContreEcri = libelleFrCompteContreEcriture;
    }

    /**
     * Sets the libItCentreCharge.
     * 
     * @param libItCentreCharge
     *            The libItCentreCharge to set
     */
    public void setLibItCentreCharge(String libelleItCentreCharge) {
        libItCentreCharge = libelleItCentreCharge;
    }

    /**
     * Sets the libItCptContreEcri.
     * 
     * @param libItCptContreEcri
     *            The libItCptContreEcri to set
     */
    public void setLibItCptContreEcri(String libelleItCompteContreEcriture) {
        libItCptContreEcri = libelleItCompteContreEcriture;
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
     * Sets the montantContreEcriture.
     * 
     * @param montantContreEcriture
     *            The montantContreEcriture to set
     */
    public void setMontantContreEcriture(String montantContreEcriture) {
        this.montantContreEcriture = montantContreEcriture;
    }

    /**
     * Sets the montantMonnaie.
     * 
     * @param montantMonnaie
     *            The montantMonnaie to set
     */
    public void setMontantMonnaie(String montantMonnaie) {
        this.montantMonnaie = montantMonnaie;
    }

    /**
     * Sets the montantMonnaieContreEcriture.
     * 
     * @param montantMonnaieContreEcriture
     *            The montantMonnaieContreEcriture to set
     */
    public void setMontantMonnaieContreEcriture(String montantMonnaieContreEcriture) {
        this.montantMonnaieContreEcriture = montantMonnaieContreEcriture;
    }

    /**
     * Sets the nombreAvoir.
     * 
     * @param nombreAvoir
     *            The nombreAvoir to set
     */
    public void setNombreAvoir(String nombreAvoir) {
        this.nombreAvoir = nombreAvoir;
    }

    /**
     * Sets the nombreDoit.
     * 
     * @param nombreDoit
     *            The nombreDoit to set
     */
    public void setNombreDoit(String nombreDoit) {
        this.nombreDoit = nombreDoit;
    }

    /**
     * Sets the numero.
     * 
     * @param numero
     *            The numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * Sets the piece.
     * 
     * @param piece
     *            The piece to set
     */
    public void setPiece(String piece) {
        this.piece = piece;
    }

}
