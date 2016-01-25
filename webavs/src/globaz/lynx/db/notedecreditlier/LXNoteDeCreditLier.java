package globaz.lynx.db.notedecreditlier;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.helpers.utils.LXHelperUtils;
import globaz.lynx.utils.LXConstants;
import globaz.lynx.utils.LXNoteDeCreditUtil;

public class LXNoteDeCreditLier extends LXOperation {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idExterneSection;
    private String idFournisseur;
    private String idSociete;

    /**
     * @see globaz.globall.db.BEntity#_afterAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {

        // Creation de la note de crédit liée coté note de crédit de base, cad
        // meme idSection
        // (donc idSectionLiee puisqu'il correspond a l'id Section de la NDC de
        // base)
        LXNoteDeCreditUtil.createNoteDeCredit(getSession(), getIdSectionLiee(), getIdSection(), getMontant(),
                getIdOperationLiee(), getIdJournal(), getIdOperationSrc());
    }

    /**
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {

        LXOperation ndcLiee = LXNoteDeCreditUtil.getNoteCreditLieeSurSectionLiee(getSession(), transaction,
                getIdOperationSrc(), getIdOperationLiee(), getIdSection());
        ndcLiee.delete(transaction);

        if (ndcLiee.hasErrors()) {
            throw new Exception(ndcLiee.getErrors().toString());
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_afterUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {

        LXOperation ndcLiee = LXNoteDeCreditUtil.getNoteCreditLieeSurSectionLiee(getSession(), transaction,
                getIdOperationSrc(), getIdOperationLiee(), getIdSection());
        ndcLiee.setMontant(getMontant());
        ndcLiee.update();

    }

    /**
     * @see globaz.lynx.db.operation.LXOperation#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        if (!checkEtat(transaction)) {
            return;
        }

        // On set le type d'operation créée
        setCsTypeOperation(LXOperation.CS_TYPE_NOTEDECREDIT_LIEE);
        setCsCodeIsoMonnaie(LXConstants.CODE_ISO_CHF);
        setCsEtatOperation(LXOperation.CS_ETAT_COMPTABILISE);
        setIdOperation(null);

        FWCurrency montantNegatif = setMontantNegatif();

        if (!testMontantDisponible(transaction, montantNegatif)) {
            return;
        }

        // Test si le montant de la note n'est pas supérieur a la facture
        LXOperation operationLiee = LXNoteDeCreditUtil
                .getOperationLiee(getSession(), transaction, getIdOperationLiee());

        FWCurrency currency3 = new FWCurrency();
        // On ajoute le montant de base de la facture liée
        currency3.add(operationLiee.getMontant());

        FWCurrency montantDejaLie = LXNoteDeCreditUtil.getMontantFactureDejaUtilise(getSession(), getIdOperationLiee(),
                operationLiee.getIdSection());
        // On retranche les montants déjà utilisés sur la facture
        currency3.add(montantDejaLie);

        if (montantNegatif.compareTo(currency3) > 0) {
            _addError(transaction, getSession().getLabel("VAL_MONTANT_INF_FACTURE"));
            return;
        }

        setIdSectionLiee(operationLiee.getIdSection());

        super._beforeAdd(transaction);
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        checkEtat(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        if (!checkEtat(transaction)) {
            return;
        }

        FWCurrency montantNegatif = setMontantNegatif();

        if (!testMontantDisponible(transaction, montantNegatif)) {
            return;
        }

        // Test si le montant de la note n'est pas supérieur a la facture
        FWCurrency currency3 = new FWCurrency();

        LXOperation operationLiee = LXNoteDeCreditUtil
                .getOperationLiee(getSession(), transaction, getIdOperationLiee());
        // On ajoute le montant de base de la facture liée
        currency3.add(operationLiee.getMontant());

        FWCurrency montantDejaLie = LXNoteDeCreditUtil.getMontantFactureDejaUtilise(getSession(), getIdOperationLiee(),
                operationLiee.getIdSection());
        // On retranche les montants déjà utilisés sur la facture
        currency3.add(montantDejaLie);

        LXOperation operationLieeOriginal = LXHelperUtils.getOperation(getSession(), null, getIdOperation());
        // On ajoute le montant précédement lié de cet note de crédit liée
        currency3.sub(operationLieeOriginal.getMontant());

        if (montantNegatif.compareTo(currency3) > 0) {
            _addError(transaction, getSession().getLabel("VAL_MONTANT_INF_FACTURE"));
        }

        super._beforeUpdate(transaction);
    }

    /**
     * @see globaz.lynx.db.operation.LXOperation#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        try {
            setIdExterneSection(statement.dbReadNumeric(LXSection.FIELD_IDEXTERNE));
            setIdFournisseur(statement.dbReadNumeric(LXSection.FIELD_IDFOURNISSEUR));
            setIdSociete(statement.dbReadNumeric(LXSection.FIELD_IDSOCIETE));
        } catch (Exception e) {
        }
        super._readProperties(statement);
    }

    /**
     * @see globaz.lynx.db.operation.LXOperation#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        // Test si idSectionLiee est bien renseigné
        if (JadeStringUtil.isBlank(getIdSectionLiee())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_SECTION_LIEE_INEXISTANTE"));
        }

        // Test si le montant est renseigné
        if (JadeStringUtil.isBlank(getMontant())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_MONTANT_INEXISTANTE"));
        }

        // Test si le montant est supérieur a zero
        FWCurrency currency = new FWCurrency();
        currency.add(getMontant());
        if (currency.isZero()) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_MONTANT_EGAL_ZERO"));
        }

        super._validate(statement);
    }

    /**
     * L'état permet-il une mise à jour ?
     * 
     * @param transaction
     * @return
     */
    private boolean checkEtat(BTransaction transaction) {
        if (CS_ETAT_PREPARE.equals(getCsEtatOperation()) || CS_ETAT_SOLDE.equals(getCsEtatOperation())) {
            _addError(transaction, getSession().getLabel("LIER_NOTEDECREDIT_TRAITEE"));
            return false;
        } else {
            return true;
        }
    }

    public String getIdExterneSection() {
        return idExterneSection;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getIdSociete() {
        return idSociete;
    }

    public void setIdExterneSection(String idExterneSection) {
        this.idExterneSection = idExterneSection;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setIdSociete(String idSociete) {
        this.idSociete = idSociete;
    }

    /**
     * On vérifie que le montant est bien négatif
     * 
     * @return
     */
    private FWCurrency setMontantNegatif() {
        FWCurrency tmp = new FWCurrency();
        tmp.add(getMontant());

        if (tmp.isPositive()) {
            tmp.negate();
            setMontant(tmp.toString());
        }

        return tmp;
    }

    /**
     * Test si le montant de la note n'est pas supérieur au montant disponible
     * 
     * @param transaction
     * @param tmp
     * @return
     * @throws Exception
     */
    private boolean testMontantDisponible(BTransaction transaction, FWCurrency tmp) throws Exception {
        FWCurrency currency2 = new FWCurrency();
        currency2.add(LXNoteDeCreditUtil.getMontantRestantNoteDeCredit(getSession(), getIdSection(),
                getIdOperationSrc(), getIdOperation()));
        if (tmp.isNegative()) {
            tmp.negate();
        }

        if (tmp.compareTo(currency2) > 0) {
            _addError(transaction, getSession().getLabel("VAL_MONTANT_INF_DISPO"));
            return false;
        } else {
            return true;
        }
    }

}
