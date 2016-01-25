package globaz.helios.db.modeles;

import globaz.globall.db.BSession;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExerciceComptableManager;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGMandatManager;
import globaz.helios.translation.CodeSystem;
import globaz.jade.log.JadeLogger;
import java.math.BigDecimal;

/**
 * Insérez la description du type ici. Date de création : (09.09.2002 15:09:50)
 * 
 * @author: Administrator
 */

public class CGEnteteModeleEcritureViewBean extends CGEnteteModeleEcriture implements
        globaz.framework.bean.FWViewBeanInterface {

    // pour traitement modele ecriture double

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String cours = "";
    private CGExerciceComptable exerciceComptableForDate = null;
    private String idCentreChargeCredite = "";
    private String idCentreChargeDebite = "";
    private String idCompteCredite = "";
    private String idCompteDebite = "";
    private java.lang.String idExterneCompteCredite = "";

    private java.lang.String idExterneCompteDebite = "";
    private String montant = "";

    private String montantMonnaie = "";

    /**
     * Commentaire relatif au constructeur CGModeleEcritureViewBean.
     */
    public CGEnteteModeleEcritureViewBean() {
        super();
    }

    @Override
    protected void _afterRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        if (CGEcritureViewBean.CS_TYPE_ECRITURE_DOUBLE.equals(getIdTypeEcriture())) {

            CGLigneModeleEcriture ecrDebit = new CGLigneModeleEcriture();
            CGLigneModeleEcriture ecrCredit = new CGLigneModeleEcriture();

            CGLigneModeleEcritureListViewBean mgr = new CGLigneModeleEcritureListViewBean();
            mgr.setSession(getSession());
            mgr.setForIdEnteteModeleEcriture(getIdEnteteModeleEcriture());
            mgr.find(transaction, 2);

            CGLigneModeleEcriture ligne = (CGLigneModeleEcriture) mgr.getEntity(0);
            if (CodeSystem.CS_DEBIT.equals(ligne.getCodeDebitCredit())
                    || CodeSystem.CS_EXTOURNE_DEBIT.equals(ligne.getCodeDebitCredit())) {

                ecrDebit = (CGLigneModeleEcriture) mgr.getEntity(0);
                ecrCredit = (CGLigneModeleEcriture) mgr.getEntity(1);
            } else {
                ecrDebit = (CGLigneModeleEcriture) mgr.getEntity(1);
                ecrCredit = (CGLigneModeleEcriture) mgr.getEntity(0);
            }

            // debit
            setIdCompteDebite(ecrDebit.getIdCompte());
            setIdCentreChargeDebite(ecrDebit.getIdCentreCharge());

            // credit
            setIdCompteCredite(ecrCredit.getIdCompte());
            setIdCentreChargeCredite(ecrCredit.getIdCentreCharge());

            BigDecimal dummy = new BigDecimal(0);

            if (ecrDebit.getMontantMonnaie() != null && ecrDebit.getMontantMonnaie().trim().length() > 0
                    && dummy.compareTo(new BigDecimal(ecrDebit.getMontantMonnaie())) != 0) {
                setMontantMonnaie(ecrDebit.getMontantMonnaie());
            } else {
                setMontantMonnaie(ecrCredit.getMontantMonnaie());
            }

            if (ecrDebit.getCoursMonnaie() != null && ecrDebit.getCoursMonnaie().trim().length() > 0
                    && dummy.compareTo(new BigDecimal(ecrDebit.getCoursMonnaie())) != 0) {
                setCours(ecrDebit.getCoursMonnaie());
            } else {
                setCours(ecrCredit.getCoursMonnaie());
            }

            if (ecrDebit.getMontant() != null && ecrDebit.getMontant().trim().length() > 0
                    && dummy.compareTo(new BigDecimal(ecrDebit.getMontant())) != 0) {
                setMontant(ecrDebit.getMontant());
            } else {
                setMontant(ecrCredit.getMontant());
            }

        }
    }

    /**
     * Returns the cours.
     * 
     * @return String
     */
    public String getCours() {
        return cours;
    }

    public CGExerciceComptable getExerciceForDate(String dateValeur) {
        if (exerciceComptableForDate == null) {
            CGExerciceComptableManager mgr = new CGExerciceComptableManager();
            mgr.setForIdMandat(getIdMandat());
            mgr.setSession(getSession());
            mgr.setOrderBy(CGExerciceComptableManager.TRI_DATE_FIN_DESC);
            mgr.setBetweenDateDebutDateFin(dateValeur);

            try {
                mgr.find(null, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (mgr.size() > 0) {
                exerciceComptableForDate = (CGExerciceComptable) mgr.getFirstEntity();
            }
        }

        return exerciceComptableForDate;
    }

    /**
     * Returns the idCentreChargeCredite.
     * 
     * @return String
     */
    public String getIdCentreChargeCredite() {
        return idCentreChargeCredite;
    }

    /**
     * Returns the idCentreChargeDebite.
     * 
     * @return String
     */
    public String getIdCentreChargeDebite() {
        return idCentreChargeDebite;
    }

    /**
     * Returns the idCompteCredite.
     * 
     * @return String
     */
    public String getIdCompteCredite() {
        return idCompteCredite;
    }

    /**
     * Returns the idCompteDebite.
     * 
     * @return String
     */
    public String getIdCompteDebite() {
        return idCompteDebite;
    }

    /**
     * Returns the idExterneCompteCredite.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdExterneCompteCredite() {
        return idExterneCompteCredite;
    }

    /**
     * Returns the idExterneCompteDebite.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdExterneCompteDebite() {
        return idExterneCompteDebite;
    }

    public CGExerciceComptable getLastExercice() {
        CGExerciceComptableManager mgr = new CGExerciceComptableManager();
        mgr.setForIdMandat(getIdMandat());
        mgr.setSession(getSession());
        mgr.setOrderBy(CGExerciceComptableManager.TRI_DATE_FIN_DESC);
        try {
            mgr.find(null, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mgr.size() > 0) {
            return (CGExerciceComptable) mgr.getEntity(0);
        } else {
            return null;
        }
    }

    public String getModeleIdMandat() {
        CGModeleEcriture modele = new CGModeleEcriture();
        modele.setSession(getSession());

        modele.setIdModeleEcriture(getIdModeleEcriture());

        try {
            modele.retrieve();
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        if (modele.hasErrors() || modele.isNew()) {
            return "";
        } else {
            return modele.getIdMandat();
        }
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
     * Sets the cours.
     * 
     * @param cours
     *            The cours to set
     */
    public void setCours(String cours) {
        this.cours = cours;
    }

    public void setDefaultIdMandat(BSession session) {
        CGMandatManager manager = new CGMandatManager();
        manager.setSession(session);

        try {
            manager.find();
        } catch (Exception e) {
            return;
        }

        if (manager.hasErrors() || manager.size() == 0) {
            return;
        } else {
            setIdMandat(((CGMandat) manager.getFirstEntity()).getIdMandat());
        }
    }

    /**
     * Sets the idCentreChargeCredite.
     * 
     * @param idCentreChargeCredite
     *            The idCentreChargeCredite to set
     */
    public void setIdCentreChargeCredite(String idCentreChargeCredite) {
        this.idCentreChargeCredite = idCentreChargeCredite;
    }

    /**
     * Sets the idCentreChargeDebite.
     * 
     * @param idCentreChargeDebite
     *            The idCentreChargeDebite to set
     */
    public void setIdCentreChargeDebite(String idCentreChargeDebite) {
        this.idCentreChargeDebite = idCentreChargeDebite;
    }

    /**
     * Sets the idCompteCredite.
     * 
     * @param idCompteCredite
     *            The idCompteCredite to set
     */
    public void setIdCompteCredite(String idCompteCredite) {
        this.idCompteCredite = idCompteCredite;
    }

    /**
     * Sets the idCompteDebite.
     * 
     * @param idCompteDebite
     *            The idCompteDebite to set
     */
    public void setIdCompteDebite(String idCompteDebite) {
        this.idCompteDebite = idCompteDebite;
    }

    /**
     * Sets the idExterneCompteCredite.
     * 
     * @param idExterneCompteCredite
     *            The idExterneCompteCredite to set
     */
    public void setIdExterneCompteCredite(java.lang.String idExterneCompteCredite) {
        this.idExterneCompteCredite = idExterneCompteCredite;
    }

    /**
     * Sets the idExterneCompteDebite.
     * 
     * @param idExterneCompteDebite
     *            The idExterneCompteDebite to set
     */
    public void setIdExterneCompteDebite(java.lang.String idExterneCompteDebite) {
        this.idExterneCompteDebite = idExterneCompteDebite;
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

}
