package globaz.helios.db.comptes;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.helios.db.interfaces.CGLibelle;
import globaz.helios.db.interfaces.CGNeedExerciceComptable;
import globaz.helios.db.interfaces.CGRemarqueInterface;
import globaz.helios.db.interfaces.ITreeListable;
import globaz.helios.translation.CodeSystem;
import java.math.BigDecimal;

public class CGEcritureSeekerViewBean extends CGNeedExerciceComptable implements ITreeListable, java.io.Serializable,
        CGRemarqueInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String code = new String();
    private java.lang.String codeDebitCredit = new String();
    private String codeIsoMonnaie = new String();
    private java.lang.String coursMonnaie = new String();
    private java.lang.String date = new String();
    private java.lang.String dateValeur = new String();
    private java.lang.Boolean estActive = new Boolean(false);
    private java.lang.Boolean estErreur = new Boolean(false);
    private java.lang.Boolean estPointee = new Boolean(false);
    private java.lang.Boolean estProvisoire = new Boolean(true);
    private java.lang.String idCentreCharge = new String();
    private java.lang.String idCompte = new String();
    private java.lang.String idEcriture = new String();
    private java.lang.String idEnteteEcriture = new String();
    private String idExterne = new String();
    private java.lang.String idJournal = new String();
    private java.lang.String idLivre = new String();
    private java.lang.String idLog = new String();
    private java.lang.String idMandat = new String();
    private java.lang.String idNature = new String();
    private java.lang.String idRemarque = "0";
    private java.lang.String libelle = new String();
    private java.lang.String montant = new String();
    private java.lang.String montantMonnaie = new String();
    private java.lang.String piece = new String();
    private String planLibelleDe = new String();

    private String planLibelleFr = new String();
    private String planLibelleIt = new String();

    private java.lang.String referenceExterne = new String();

    private java.lang.String remarque = "";

    /**
     * Commentaire relatif au constructeur CGEcriture
     */
    public CGEcritureSeekerViewBean() {
        super();
    }

    @Override
    protected void _afterRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        super._afterRetrieve(transaction);
        if (hasCGRemarque()) {
            CGRemarque rem = retrieveCGRemarque();
            if (rem.isNew()) {
                setIdRemarque("0");
                // Sinon ca boucle en récursivité, 'impossible' à éviter
                // update(transaction);
            } else {
                setRemarque(rem.getRemarque());
            }
        }
    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        String fields = _getCollection() + "CGPLANP.IDEXTERNE, " + _getCollection() + "CGPLANP.LIBELLEFR, "
                + _getCollection() + "CGPLANP.LIBELLEDE, " + _getCollection() + "CGPLANP.LIBELLEIT, "
                + _getCollection() + "CGCOMTP.CODEISOMONNAIE, " + _getCollection() + "CGCOMTP.IDNATURE, "
                + _getCollection() + "CGPERIP.CODE, " + _getCollection() + _getTableName() + ".idecriture, "
                + _getCollection() + _getTableName() + ".idcompte, " + _getCollection() + _getTableName()
                + ".identeteecriture, " + _getCollection() + _getTableName() + ".idjournal, " + _getCollection()
                + _getTableName() + ".idexercomptable, " + _getCollection() + _getTableName() + ".idremarque, "
                + _getCollection() + _getTableName() + ".idcentrecharge, " + _getCollection() + _getTableName()
                + ".idmandat, " + _getCollection() + _getTableName() + ".date, " + _getCollection() + _getTableName()
                + ".datevaleur, " + _getCollection() + _getTableName() + ".piece, " + _getCollection()
                + _getTableName() + ".libelle, " + _getCollection() + _getTableName() + ".montant, " + _getCollection()
                + _getTableName() + ".montantmonnaie, " + _getCollection() + _getTableName() + ".coursmonnaie, "
                + _getCollection() + _getTableName() + ".codedebitcredit, " + _getCollection() + _getTableName()
                + ".referenceexterne, " + _getCollection() + _getTableName() + ".estpointee, " + _getCollection()
                + _getTableName() + ".estprovisoire, " + _getCollection() + _getTableName() + ".esterreur, "
                + _getCollection() + _getTableName() + ".estactive, " + _getCollection() + _getTableName() + ".idlog, "
                + _getCollection() + _getTableName() + ".idlivre, " + _getCollection() + _getTableName() + ".pspy";

        return fields;
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CGECRIP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idExterne = statement.dbReadString("IDEXTERNE");
        planLibelleFr = statement.dbReadString("LIBELLEFR");
        planLibelleDe = statement.dbReadString("LIBELLEDE");
        planLibelleIt = statement.dbReadString("LIBELLEIT");
        codeIsoMonnaie = statement.dbReadString("CODEISOMONNAIE");
        idEcriture = statement.dbReadNumeric("IDECRITURE");
        idCompte = statement.dbReadNumeric("IDCOMPTE");
        code = statement.dbReadString("CODE");
        idNature = statement.dbReadNumeric("IDNATURE");
        idEnteteEcriture = statement.dbReadNumeric("IDENTETEECRITURE");
        idJournal = statement.dbReadNumeric("IDJOURNAL");
        idExerciceComptable = statement.dbReadNumeric("IDEXERCOMPTABLE");
        idRemarque = statement.dbReadNumeric("IDREMARQUE");
        idCentreCharge = statement.dbReadNumeric("IDCENTRECHARGE");
        idMandat = statement.dbReadNumeric("IDMANDAT");
        date = statement.dbReadDateAMJ("DATE");
        dateValeur = statement.dbReadDateAMJ("DATEVALEUR");
        piece = statement.dbReadString("PIECE");
        libelle = statement.dbReadString("LIBELLE");
        montant = statement.dbReadNumeric("MONTANT", 2);
        montantMonnaie = statement.dbReadNumeric("MONTANTMONNAIE", 2);
        coursMonnaie = statement.dbReadNumeric("COURSMONNAIE", 5);
        codeDebitCredit = statement.dbReadNumeric("CODEDEBITCREDIT");
        referenceExterne = statement.dbReadString("REFERENCEEXTERNE");
        estPointee = statement.dbReadBoolean("ESTPOINTEE");
        estProvisoire = statement.dbReadBoolean("ESTPROVISOIRE");
        estActive = statement.dbReadBoolean("ESTACTIVE");
        estErreur = statement.dbReadBoolean("ESTERREUR");
        idLog = statement.dbReadNumeric("IDLOG");
        idLivre = statement.dbReadNumeric("IDLIVRE");

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
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */

    @Override
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

    public java.lang.String getCodeDebitCredit() {
        return codeDebitCredit;
    }

    /**
     * Returns the codeIsoMonnaie.
     * 
     * @return String
     */
    public String getCodeIsoMonnaie() {
        return codeIsoMonnaie;
    }

    public java.lang.String getCoursMonnaie() {
        return coursMonnaie;
    }

    public java.lang.String getDate() {
        return date;
    }

    public java.lang.String getDateValeur() {
        return dateValeur;
    }

    /**
     * Returns the estActive.
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getEstActive() {
        return estActive;
    }

    /**
     * Returns the estErreur.
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getEstErreur() {
        return estErreur;
    }

    /**
     * Returns the estPointee.
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getEstPointee() {
        return estPointee;
    }

    /**
     * Returns the estProvisoire.
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getEstProvisoire() {
        return estProvisoire;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.03.2003 14:42:14)
     * 
     * @return java.lang.String
     */

    public java.lang.String getIdCentreCharge() {
        return idCentreCharge;
    }

    public java.lang.String getIdCompte() {
        return idCompte;
    }

    /**
     * Getter
     */
    public java.lang.String getIdEcriture() {
        return idEcriture;
    }

    public java.lang.String getIdEnteteEcriture() {
        return idEnteteEcriture;
    }

    @Override
    public java.lang.String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * Returns the idExterne.
     * 
     * @return String
     */
    public String getIdExterne() {
        return idExterne;
    }

    public java.lang.String getIdJournal() {
        return idJournal;
    }

    public java.lang.String getIdLivre() {
        return idLivre;
    }

    public java.lang.String getIdLog() {
        return idLog;
    }

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

    public java.lang.String getIdRemarque() {
        return idRemarque;
    }

    @Override
    public java.lang.String getLibelle() {
        return libelle;
    }

    public String getLibelleCompte() {
        String langue = getSession().getIdLangueISO();
        if (langue != null) {
            if ("IT".equalsIgnoreCase(langue)) {
                return planLibelleIt;
            } else if ("DE".equalsIgnoreCase(langue)) {
                return planLibelleDe;
            } else {
                return planLibelleFr;
            }
        }
        return "";
    }

    /**
     * Returns the montant.
     * 
     * @return java.lang.String
     */
    public java.lang.String getMontant() {
        return montant;
    }

    public java.lang.String getMontantAffiche() {
        BigDecimal bdMontant = JAUtil.createBigDecimal(montant);
        if (bdMontant != null) {
            if (CodeSystem.CS_DEBIT.equals(codeDebitCredit) || CodeSystem.CS_EXTOURNE_DEBIT.equals(codeDebitCredit)) {
                String result = bdMontant.toString();
                return result;
            } else {
                String result = bdMontant.negate().toString();
                return result;
            }
        } else {
            return CGLibelle.LIBELLE_ERROR;
        }
    }

    public java.lang.String getMontantAfficheMonnaie() {
        BigDecimal bdMontant = JAUtil.createBigDecimal(montantMonnaie);
        if (bdMontant != null) {
            if (CodeSystem.CS_DEBIT.equals(codeDebitCredit) || CodeSystem.CS_EXTOURNE_DEBIT.equals(codeDebitCredit)) {
                return bdMontant.toString();
            } else {
                return bdMontant.negate().toString();
            }
        } else {
            return CGLibelle.LIBELLE_ERROR;
        }
    }

    public java.lang.String getMontantBase() {

        return montant;
    }

    public java.lang.String getMontantBaseMonnaie() {
        return montantMonnaie;
    }

    /**
     * Returns the montantMonnaie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getMontantMonnaie() {
        return montantMonnaie;
    }

    public java.lang.String getPiece() {
        return piece;
    }

    public java.lang.String getReferenceExterne() {
        return referenceExterne;
    }

    @Override
    public java.lang.String getRemarque() {
        return remarque;
    }

    @Override
    public boolean hasCGRemarque() {
        return (getIdRemarque() != null && !"0".equals(getIdRemarque()));
    }

    @Override
    public boolean hasRemarque() {
        return (getRemarque() != null && !"".equals(getRemarque()));
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

    public java.lang.Boolean isEstActive() {
        return estActive;
    }

    public java.lang.Boolean isEstErreur() {
        return estErreur;
    }

    public java.lang.Boolean isEstPointee() {
        return estPointee;
    }

    public java.lang.Boolean isEstProvisoire() {
        return estProvisoire;
    }

    @Override
    public CGRemarque retrieveCGRemarque() {
        if (hasCGRemarque()) {
            CGRemarque rem = new CGRemarque();
            try {
                rem.setSession(getSession());
                rem.setIdRemarque(getIdRemarque());
                rem.retrieve();
                return rem;
            } catch (Exception e) {
                return rem;
            }
        } else {
            return null;
        }
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

    public void setCodeDebitCredit(java.lang.String newCodeDebitCredit) {
        codeDebitCredit = newCodeDebitCredit;
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

    public void setCoursMonnaie(java.lang.String newCoursMonnaie) {
        coursMonnaie = newCoursMonnaie;
    }

    public void setDate(java.lang.String newDate) {
        date = newDate;
    }

    public void setDateValeur(java.lang.String newDateValeur) {
        dateValeur = newDateValeur;
    }

    /**
     * Sets the estActive.
     * 
     * @param estActive
     *            The estActive to set
     */
    public void setEstActive(java.lang.Boolean estActive) {
        this.estActive = estActive;
    }

    public void setEstErreur(java.lang.Boolean newEstErreur) {
        estErreur = newEstErreur;
    }

    public void setEstPointee(java.lang.Boolean newEstPointee) {
        estPointee = newEstPointee;
    }

    /**
     * Sets the estProvisoire.
     * 
     * @param estProvisoire
     *            The estProvisoire to set
     */
    public void setEstProvisoire(java.lang.Boolean estProvisoire) {
        this.estProvisoire = estProvisoire;
    }

    public void setIdCentreCharge(java.lang.String newIdCentreCharge) {
        idCentreCharge = newIdCentreCharge;
    }

    public void setIdCompte(java.lang.String newIdCompte) {
        idCompte = newIdCompte;
    }

    /**
     * Setter
     */
    public void setIdEcriture(java.lang.String newIdEcriture) {
        idEcriture = newIdEcriture;
    }

    public void setIdEnteteEcriture(java.lang.String newIdEnteteEcriture) {
        idEnteteEcriture = newIdEnteteEcriture;
    }

    @Override
    public void setIdExerciceComptable(java.lang.String newIdExerciceComptable) {
        idExerciceComptable = newIdExerciceComptable;
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

    public void setIdJournal(java.lang.String newIdJournal) {
        idJournal = newIdJournal;
    }

    public void setIdLivre(java.lang.String newIdLivre) {
        idLivre = newIdLivre;
    }

    public void setIdLog(java.lang.String newIdLog) {
        idLog = newIdLog;
    }

    public void setIdMandat(java.lang.String newIdMandat) {
        idMandat = newIdMandat;
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

    public void setIdRemarque(java.lang.String newIdRemarque) {
        idRemarque = newIdRemarque;
    }

    public void setLibelle(java.lang.String newLibelle) {
        libelle = newLibelle;
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

    public void setPiece(java.lang.String newPiece) {
        piece = newPiece;
    }

    public void setReferenceExterne(java.lang.String newReferenceExterne) {
        referenceExterne = newReferenceExterne;
    }

    @Override
    public void setRemarque(java.lang.String newRemarque) {
        remarque = newRemarque;
    }

    /**
     * @see globaz.helios.db.interfaces.CGRemarqueInterface#updateRemarque(BTransaction)
     */
    @Override
    public void updateRemarque(BTransaction transaction) throws Exception {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.11.2002 13:48:22)
     * 
     * @param newEstActive
     *            java.lang.Boolean
     */
    public void wantEstActive(java.lang.Boolean newEstActive) {
        estActive = newEstActive;
    }

    public void wantEstProvisoire(java.lang.Boolean newEstProvisoire) {
        estProvisoire = newEstProvisoire;
    }

}
