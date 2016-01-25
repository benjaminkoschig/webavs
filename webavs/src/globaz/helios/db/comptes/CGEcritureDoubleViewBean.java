package globaz.helios.db.comptes;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BTransaction;
import globaz.helios.db.ecritures.CGGestionEcritureViewBean;
import globaz.helios.helpers.ecritures.CGGestionEcritureAdd;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;

/**
 * Insérez la description du type ici. Date de création : (08.04.2003 13:30:54)
 * 
 * @author: Administrator
 */
public class CGEcritureDoubleViewBean extends CGEnteteEcritureViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String saisieEcran = "false";

    /**
     * Commentaire relatif au constructeur CGEcritureDoubleViewBean.
     */
    public CGEcritureDoubleViewBean() {
        super();
    }

    @Override
    protected boolean _autoInherits() {
        return false;
    }

    @Override
    protected void _init() {
        setIdTypeEcriture(CGEcritureViewBean.CS_TYPE_ECRITURE_DOUBLE);
    }

    /**
     * Utiliser pour l'ajout des écritures en comptabilité générale depuis le module de comptabilité auxiliaire.
     * 
     * @param transaction
     * @throws Exception
     */
    public void addEcriture(BITransaction transaction) throws Exception {
        retrieveIdCentreChargeCrediteFromNumero();
        retrieveIdCentreChargeDebiteFromNumero();

        CGExerciceComptableViewBean exercice = new CGExerciceComptableViewBean();
        exercice.setIdExerciceComptable(retrieveJournal().getIdExerciceComptable());
        exercice.retrieve(transaction);

        CGGestionEcritureViewBean ecritures = new CGGestionEcritureViewBean();
        ecritures.setSession(getSession());

        if (JadeStringUtil.isBlank(getDateValeur())) {
            ecritures.setDateValeur(getDate());
        } else {
            ecritures.setDateValeur(getDateValeur());
        }

        ecritures.setIdJournal(getIdJournal());
        ecritures.setPiece(getPiece());
        ecritures.setRemarque(getRemarque());

        ArrayList ecrituresList = new ArrayList();

        CGEcritureViewBean ecritureCrebit = new CGEcritureViewBean();
        CGEcritureViewBean ecritureDebit = new CGEcritureViewBean();

        ecritureCrebit.setLibelle(getLibelle());
        ecritureDebit.setLibelle(getLibelle());

        ecritureCrebit.setCodeDebitCredit(CodeSystem.CS_CREDIT);
        ecritureCrebit.setMontant(getMontant());
        ecritureCrebit.setMontantMonnaie(getMontantMonnaie());
        ecritureCrebit.setCoursMonnaie(getCours());

        ecritureDebit.setCodeDebitCredit(CodeSystem.CS_DEBIT);
        ecritureDebit.setMontant(getMontant());
        ecritureDebit.setMontantMonnaie(getMontantMonnaie());
        ecritureDebit.setCoursMonnaie(getCours());

        ecritureCrebit.setIdExerciceComptable(exercice.getIdExerciceComptable());
        ecritureDebit.setIdExerciceComptable(exercice.getIdExerciceComptable());
        ecritureCrebit.setIdExterneCompte(getIdExterneCompteCredite());
        ecritureDebit.setIdExterneCompte(getIdExterneCompteDebite());

        ecritureCrebit.setIdCompte(getIdCompte(transaction, getIdExterneCompteCredite(),
                exercice.getIdExerciceComptable()));
        ecritureDebit.setIdCompte(getIdCompte(transaction, getIdExterneCompteDebite(),
                exercice.getIdExerciceComptable()));

        ecritureCrebit.setIdCentreCharge(getIdCentreChargeCredite());
        ecritureDebit.setIdCentreCharge(getIdCentreChargeDebite());

        ecrituresList.add(ecritureCrebit);
        ecrituresList.add(ecritureDebit);

        ecritures.setEcritures(ecrituresList);

        CGGestionEcritureAdd.addEcritures(getSession(), (BTransaction) transaction, ecritures, true);
    }

    /**
     * Retrouve l'idcompte en fonction de l'idexterne et de l'exercice comptable.
     * 
     * @param transaction
     * @param idExterne
     * @param idExerciceComptable
     * @return
     * @throws Exception
     */
    private String getIdCompte(BITransaction transaction, String idExterne, String idExerciceComptable)
            throws Exception {
        CGPlanComptableManager manager = new CGPlanComptableManager();
        manager.setSession(getSession());

        manager.setForIdExterne(idExterne);
        manager.setForIdExerciceComptable(idExerciceComptable);

        manager.find(transaction);

        if (manager.hasErrors() || manager.isEmpty()) {
            throw new Exception(getSession().getLabel("AUCUN_COMPTE_RESOLU"));
        }

        return ((CGPlanComptableViewBean) manager.getFirstEntity()).getIdCompte();

    }

    public String getSaisieEcran() {
        return saisieEcran;
    }

    public void setSaisieEcran(String saisieEcran) {
        this.saisieEcran = saisieEcran;
    }
}
