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

public class CGAdvancedEcritureViewBean extends CGNeedExerciceComptable implements ITreeListable, java.io.Serializable,
        CGRemarqueInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeDebitCredit = new String();
    private String codeIsoMonnaie = new String();
    private String coursMonnaie = new String();
    private String date = new String();
    private String dateValeur = new String();
    private Boolean estActive = new Boolean(false);
    private Boolean estErreur = new Boolean(false);
    private Boolean estPointee = new Boolean(false);
    private Boolean estProvisoire = new Boolean(true);
    private String idCentreCharge = new String();
    private String idCompte = new String();
    private String idEcriture = new String();
    private String idEnteteEcriture = new String();
    private String idExterne = new String();
    private String idJournal = new String();
    private String idLivre = new String();
    private String idLog = new String();
    private String idMandat = new String();
    private String idNature = new String();
    private String idRemarque = "0";
    private String idTypeEcriture = new String();
    private String libelle = new String();
    private String montant = new String();
    private String montantMonnaie = new String();
    private String piece = new String();
    private String planLibelleDe = new String();
    private String planLibelleFr = new String();

    private String planLibelleIt = new String();
    private String referenceExterne = new String();

    private String remarque = "";

    /**
     * Commentaire relatif au constructeur CGEcriture
     */
    public CGAdvancedEcritureViewBean() {
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
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return CGEcritureViewBean.TABLE_CGECRIP;
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
        idTypeEcriture = statement.dbReadNumeric("IDTYPEECRITURE");
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

    public String getCodeDebitCredit() {
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

    public String getCoursMonnaie() {
        return coursMonnaie;
    }

    public String getDate() {
        return date;
    }

    public String getDateValeur() {
        return dateValeur;
    }

    /**
     * Returns the estActive.
     * 
     * @return Boolean
     */
    public Boolean getEstActive() {
        return estActive;
    }

    /**
     * Returns the estErreur.
     * 
     * @return Boolean
     */
    public Boolean getEstErreur() {
        return estErreur;
    }

    /**
     * Returns the estPointee.
     * 
     * @return Boolean
     */
    public Boolean getEstPointee() {
        return estPointee;
    }

    /**
     * Returns the estProvisoire.
     * 
     * @return Boolean
     */
    public Boolean getEstProvisoire() {
        return estProvisoire;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.03.2003 14:42:14)
     * 
     * @return String
     */

    public String getIdCentreCharge() {
        return idCentreCharge;
    }

    public String getIdCompte() {
        return idCompte;
    }

    /**
     * Getter
     */
    public String getIdEcriture() {
        return idEcriture;
    }

    public String getIdEnteteEcriture() {
        return idEnteteEcriture;
    }

    @Override
    public String getIdExerciceComptable() {
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

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdLivre() {
        return idLivre;
    }

    public String getIdLog() {
        return idLog;
    }

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

    public String getIdRemarque() {
        return idRemarque;
    }

    public String getIdTypeEcriture() {
        return idTypeEcriture;
    }

    @Override
    public String getLibelle() {
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
     * @return String
     */
    public String getMontant() {
        return montant;
    }

    public String getMontantAffiche() {
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

    public String getMontantAfficheMonnaie() {
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

    public String getMontantBase() {

        return montant;
    }

    public String getMontantBaseMonnaie() {
        return montantMonnaie;
    }

    /**
     * Returns the montantMonnaie.
     * 
     * @return String
     */
    public String getMontantMonnaie() {
        return montantMonnaie;
    }

    public String getPiece() {
        return piece;
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    @Override
    public String getRemarque() {
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

    public Boolean isEstActive() {
        return estActive;
    }

    public Boolean isEstErreur() {
        return estErreur;
    }

    public Boolean isEstPointee() {
        return estPointee;
    }

    public Boolean isEstProvisoire() {
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

    public void setCodeDebitCredit(String newCodeDebitCredit) {
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

    public void setCoursMonnaie(String newCoursMonnaie) {
        coursMonnaie = newCoursMonnaie;
    }

    public void setDate(String newDate) {
        date = newDate;
    }

    public void setDateValeur(String newDateValeur) {
        dateValeur = newDateValeur;
    }

    /**
     * Sets the estActive.
     * 
     * @param estActive
     *            The estActive to set
     */
    public void setEstActive(Boolean estActive) {
        this.estActive = estActive;
    }

    public void setEstErreur(Boolean newEstErreur) {
        estErreur = newEstErreur;
    }

    public void setEstPointee(Boolean newEstPointee) {
        estPointee = newEstPointee;
    }

    /**
     * Sets the estProvisoire.
     * 
     * @param estProvisoire
     *            The estProvisoire to set
     */
    public void setEstProvisoire(Boolean estProvisoire) {
        this.estProvisoire = estProvisoire;
    }

    public void setIdCentreCharge(String newIdCentreCharge) {
        idCentreCharge = newIdCentreCharge;
    }

    public void setIdCompte(String newIdCompte) {
        idCompte = newIdCompte;
    }

    /**
     * Setter
     */
    public void setIdEcriture(String newIdEcriture) {
        idEcriture = newIdEcriture;
    }

    public void setIdEnteteEcriture(String newIdEnteteEcriture) {
        idEnteteEcriture = newIdEnteteEcriture;
    }

    @Override
    public void setIdExerciceComptable(String newIdExerciceComptable) {
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

    public void setIdJournal(String newIdJournal) {
        idJournal = newIdJournal;
    }

    public void setIdLivre(String newIdLivre) {
        idLivre = newIdLivre;
    }

    public void setIdLog(String newIdLog) {
        idLog = newIdLog;
    }

    public void setIdMandat(String newIdMandat) {
        idMandat = newIdMandat;
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

    public void setIdRemarque(String newIdRemarque) {
        idRemarque = newIdRemarque;
    }

    public void setIdTypeEcriture(String idTypeEcriture) {
        this.idTypeEcriture = idTypeEcriture;
    }

    public void setLibelle(String newLibelle) {
        libelle = newLibelle;
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
     * Sets the montantMonnaie.
     * 
     * @param montantMonnaie
     *            The montantMonnaie to set
     */
    public void setMontantMonnaie(String montantMonnaie) {
        this.montantMonnaie = montantMonnaie;
    }

    public void setPiece(String newPiece) {
        piece = newPiece;
    }

    public void setReferenceExterne(String newReferenceExterne) {
        referenceExterne = newReferenceExterne;
    }

    @Override
    public void setRemarque(String newRemarque) {
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
     *            Boolean
     */
    public void wantEstActive(Boolean newEstActive) {
        estActive = newEstActive;
    }

    public void wantEstProvisoire(Boolean newEstProvisoire) {
        estProvisoire = newEstProvisoire;
    }

}
