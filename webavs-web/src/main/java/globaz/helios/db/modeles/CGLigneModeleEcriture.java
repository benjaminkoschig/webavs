package globaz.helios.db.modeles;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExerciceComptableManager;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;
import java.math.BigDecimal;

public class CGLigneModeleEcriture extends BEntity implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String COURS_MONNAIE_0 = "0.00000";

    private String codeDebitCredit = new String();
    private CGCompte compte = null;
    private String coursMonnaie = COURS_MONNAIE_0;
    private String idCentreCharge = new String();
    private String idCompte = new String();
    private String idConstantBouclement = new String();
    private String idEnteteModeleEcriture = new String();
    private String idExterneCompte;
    private String idLigneModeleEcriture = new String();
    private String libelle = new String();
    private String montant = new String();
    private String montantMonnaie = new String();

    private String piece = new String();

    /**
     * Commentaire relatif au constructeur CGModeleEcriture
     */
    public CGLigneModeleEcriture() {
        super();
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // incrémente de +1 le numéro
        setIdLigneModeleEcriture(_incCounter(transaction, "0"));

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CGLMODP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idLigneModeleEcriture = statement.dbReadNumeric("IDLIGNEMODELEECRIT");
        idEnteteModeleEcriture = statement.dbReadNumeric("IDENTETEMODECRIT");
        idCentreCharge = statement.dbReadNumeric("IDCENTRECHARGE");
        idCompte = statement.dbReadNumeric("IDCOMPTE");
        libelle = statement.dbReadString("LIBELLE");
        piece = statement.dbReadString("PIECE");
        montant = statement.dbReadNumeric("MONTANT");
        montantMonnaie = statement.dbReadNumeric("MONTANTMONNAIE");
        coursMonnaie = statement.dbReadNumeric("COURSMONNAIE");
        codeDebitCredit = statement.dbReadNumeric("CODEDEBITCREDIT");
        idConstantBouclement = statement.dbReadNumeric("IDCONSTANTEBOUCL");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (JadeStringUtil.isBlank(getCodeDebitCredit())) {
            throw new Exception(getSession().getLabel("LIGNE_MODELE_ECR_CODE_DEB_CRED_ERROR"));
        }

        if (JadeStringUtil.isBlank(getIdCompte())) {
            throw new Exception(getSession().getLabel("ECRITURE_COMPTE_ERROR_1"));
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDLIGNEMODELEECRIT",
                _dbWriteNumeric(statement.getTransaction(), getIdLigneModeleEcriture(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDLIGNEMODELEECRIT",
                _dbWriteNumeric(statement.getTransaction(), getIdLigneModeleEcriture(), "idLigneModeleEcriture"));
        statement.writeField("IDENTETEMODECRIT",
                _dbWriteNumeric(statement.getTransaction(), getIdEnteteModeleEcriture(), "idEnteteModeleEcriture"));
        statement.writeField("IDCENTRECHARGE",
                _dbWriteNumeric(statement.getTransaction(), getIdCentreCharge(), "idCentreCharge"));
        statement.writeField("IDCOMPTE", _dbWriteNumeric(statement.getTransaction(), getIdCompte(), "idCompte"));
        statement.writeField("LIBELLE", _dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField("PIECE", _dbWriteString(statement.getTransaction(), getPiece(), "piece"));
        statement.writeField("MONTANT", _dbWriteNumeric(statement.getTransaction(), getMontant(), "montant"));
        statement.writeField("MONTANTMONNAIE",
                _dbWriteNumeric(statement.getTransaction(), getMontantMonnaie(), "montantMonnaie"));
        statement.writeField("COURSMONNAIE",
                _dbWriteNumeric(statement.getTransaction(), getCoursMonnaie(), "coursMonnaie"));
        statement.writeField("CODEDEBITCREDIT",
                _dbWriteNumeric(statement.getTransaction(), getCodeDebitCredit(), "codeDebitCredit"));
        statement.writeField("IDCONSTANTEBOUCL",
                _dbWriteNumeric(statement.getTransaction(), getIdConstantBouclement(), "idConstantBouclement"));
    }

    /**
     * Returns the codeDebitCredit.
     * 
     * @return String
     */
    public String getCodeDebitCredit() {
        return codeDebitCredit;
    }

    public String getCodeDebitCreditLibelle() {
        return getSession().getCodeLibelle(getCodeDebitCredit());
    }

    /**
     * Return le compte CG. Si non résolu ou id non setter return null.
     * 
     * @return
     */
    public CGCompte getCompte() {
        if ((!JadeStringUtil.isIntegerEmpty(getIdCompte())) && (compte == null)) {
            compte = new CGCompte();
            compte.setSession(getSession());
            compte.setIdCompte(getIdCompte());

            try {
                compte.retrieve();
            } catch (Exception e) {
                return null;
            }

            if (compte.isNew()) {
                compte = null;
            }
        }

        return compte;
    }

    public String getCompteLibelle() {
        try {
            if (!JadeStringUtil.isIntegerEmpty(getIdCompte())) {
                CGEnteteModeleEcriture enteteModele = new CGEnteteModeleEcriture();
                enteteModele.setSession(getSession());
                enteteModele.setIdEnteteModeleEcriture(getIdEnteteModeleEcriture());
                enteteModele.retrieve();
                if (enteteModele.isNew()) {
                    return null;
                }

                CGExerciceComptableManager exManager = new CGExerciceComptableManager();
                exManager.setSession(getSession());
                exManager.setForIdMandat(enteteModele.getIdMandat());
                exManager.setOrderBy(CGExerciceComptableManager.TRI_DATE_FIN_DESC);
                exManager.find(null, 1);
                if (exManager.size() == 0) {
                    return null;
                }

                CGExerciceComptable ex = (CGExerciceComptable) exManager.getFirstEntity();
                return getCompteLibelle(ex.getIdExerciceComptable());
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public String getCompteLibelle(String idExerciceComptable) {
        try {
            if (!JadeStringUtil.isIntegerEmpty(getIdCompte())) {
                CGPlanComptableViewBean pc = new CGPlanComptableViewBean();
                pc.setSession(getSession());

                pc.setIdExerciceComptable(idExerciceComptable);
                pc.setIdCompte(getIdCompte());
                pc.retrieve();

                return pc.getLibelle();
            } else {
                return new String();
            }
        } catch (Exception e) {
            return new String();
        }
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
     * Return l'entête. Si non résolu ou id non setter return null.
     * 
     * @return
     */
    public CGEnteteModeleEcriture getEntete() {
        if (!JadeStringUtil.isIntegerEmpty(getIdEnteteModeleEcriture())) {
            CGEnteteModeleEcriture entete = new CGEnteteModeleEcriture();
            entete.setSession(getSession());
            entete.setIdEnteteModeleEcriture(getIdEnteteModeleEcriture());

            try {
                entete.retrieve();
            } catch (Exception e) {
                return null;
            }

            if (entete.isNew()) {
                return null;
            }

            return entete;
        } else {
            return null;
        }
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
     * Returns the idConstantBouclement.
     * 
     * @return String
     */
    public String getIdConstantBouclement() {
        return idConstantBouclement;
    }

    /**
     * Returns the idEnteteModeleEcriture.
     * 
     * @return String
     */
    public String getIdEnteteModeleEcriture() {
        return idEnteteModeleEcriture;
    }

    public String getIdExterne() {
        try {
            if (!JadeStringUtil.isIntegerEmpty(getIdCompte())) {
                CGEnteteModeleEcriture enteteModele = new CGEnteteModeleEcriture();
                enteteModele.setSession(getSession());
                enteteModele.setIdEnteteModeleEcriture(getIdEnteteModeleEcriture());
                enteteModele.retrieve();
                if (enteteModele.isNew()) {
                    return null;
                }

                CGExerciceComptableManager exManager = new CGExerciceComptableManager();
                exManager.setSession(getSession());
                exManager.setForIdMandat(enteteModele.getIdMandat());
                exManager.setOrderBy(CGExerciceComptableManager.TRI_DATE_FIN_DESC);
                exManager.find(null, 1);
                if (exManager.size() == 0) {
                    return null;
                }

                CGExerciceComptable ex = (CGExerciceComptable) exManager.getFirstEntity();
                return getIdExterne(ex.getIdExerciceComptable());
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public String getIdExterne(String idExerciceComptable) {
        try {
            if (!JadeStringUtil.isIntegerEmpty(getIdCompte())) {
                CGPlanComptableViewBean pc = new CGPlanComptableViewBean();
                pc.setSession(getSession());

                pc.setIdExerciceComptable(idExerciceComptable);
                pc.setIdCompte(getIdCompte());
                pc.retrieve();

                return pc.getIdExterne();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public String getIdExterneCompte() {
        return idExterneCompte;
    }

    /**
     * Returns the idLigneModeleEcriture.
     * 
     * @return String
     */
    public String getIdLigneModeleEcriture() {
        return idLigneModeleEcriture;
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
     * Returns the montant.
     * 
     * @return String
     */
    public String getMontant() {
        return montant;
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
     * Returns the piece.
     * 
     * @return String
     * @deprecated Utiliser la pièce du modèle.
     */
    @Deprecated
    public String getPiece() {
        return piece;
    }

    public boolean isAvoir() {
        if (CodeSystem.CS_CREDIT.equals(codeDebitCredit) || (CodeSystem.CS_EXTOURNE_CREDIT.equals(codeDebitCredit))) {
            return true;
        }
        return false;
    }

    public boolean isCompteCentreCharge(String idExerciceComptable) {
        try {
            if (!JadeStringUtil.isIntegerEmpty(getIdCompte())) {
                CGPlanComptableViewBean pc = new CGPlanComptableViewBean();
                pc.setSession(getSession());

                pc.setIdExerciceComptable(idExerciceComptable);
                pc.setIdCompte(getIdCompte());
                pc.retrieve();

                return CGCompte.CS_CENTRE_CHARGE.equals(pc.getIdNature());
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDoit() {
        if (CodeSystem.CS_DEBIT.equals(codeDebitCredit) || (CodeSystem.CS_EXTOURNE_DEBIT.equals(codeDebitCredit))) {
            return true;
        }
        return false;
    }

    /**
     * L'entité est-elle égale à l'entité passée en paramètre ? <br/>
     * Utiliser pour n'effectuer que les mise à jours (depuis écran) nécessaire.
     * 
     * @param compareWith
     * @return
     */
    public boolean isEqualsTo(CGLigneModeleEcriture compareWith) {
        if (!getIdCompte().equals(compareWith.getIdCompte())) {
            return false;
        } else if (!getIdExterneCompte().equals(compareWith.getIdExterneCompte())) {
            return false;
        } else if (!getIdCentreCharge().equals(compareWith.getIdCentreCharge())) {
            return false;
        } else if (!getLibelle().equals(compareWith.getLibelle())) {
            return false;
        } else if (!getCodeDebitCredit().equals(compareWith.getCodeDebitCredit())) {
            return false;
        } else if (new FWCurrency(getMontant()).compareTo(new FWCurrency(compareWith.getMontant())) != 0) {
            return false;
        } else if (new FWCurrency(getMontantMonnaie()).compareTo(new FWCurrency(compareWith.getMontantMonnaie())) != 0) {
            return false;
        } else if (new BigDecimal(getCoursMonnaie()).compareTo(new BigDecimal(compareWith.getCoursMonnaie())) != 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isForCompteAffillie() throws Exception {
        CGMandat mandat = getEntete().getMandat();

        if ((mandat != null) && (mandat.isEstComptabiliteAVS().booleanValue())) {
            return "110".equals(getIdExterne().substring(5, 8));
        }
        return false;
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
     * Sets the coursMonnaie.
     * 
     * @param coursMonnaie
     *            The coursMonnaie to set
     */
    public void setCoursMonnaie(String coursMonnaie) {
        this.coursMonnaie = coursMonnaie;
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
     * Sets the idConstantBouclement.
     * 
     * @param idConstantBouclement
     *            The idConstantBouclement to set
     */
    public void setIdConstantBouclement(String idConstantBouclement) {
        this.idConstantBouclement = idConstantBouclement;
    }

    /**
     * Sets the idEnteteModeleEcriture.
     * 
     * @param idEnteteModeleEcriture
     *            The idEnteteModeleEcriture to set
     */
    public void setIdEnteteModeleEcriture(String idEnteteModeleEcriture) {
        this.idEnteteModeleEcriture = idEnteteModeleEcriture;
    }

    public void setIdExterneCompte(String idExterneCompte) {
        this.idExterneCompte = idExterneCompte;
    }

    /**
     * Sets the idLigneModeleEcriture.
     * 
     * @param idLigneModeleEcriture
     *            The idLigneModeleEcriture to set
     */
    public void setIdLigneModeleEcriture(String idLigneModeleEcriture) {
        this.idLigneModeleEcriture = idLigneModeleEcriture;
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

    /**
     * Sets the piece.
     * 
     * @param piece
     *            The piece to set
     * @deprecated Utiliser la pièce du modèle.
     */
    @Deprecated
    public void setPiece(String piece) {
        this.piece = piece;
    }
}
