package globaz.phenix.process.excelml;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.pavo.db.compte.CICompteIndividuelUtil;
import globaz.phenix.db.principale.CPCotisation;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliationCalcul;
import globaz.phenix.db.principale.CPDecisionAffiliationCalculManager;
import globaz.phenix.db.principale.CPDecisionForCompareCIManager;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.db.principale.CPDonneesCalcul;
import globaz.phenix.db.principale.CPSortie;
import globaz.phenix.db.principale.CPSortieManager;
import java.math.BigDecimal;

public class CPProcessDifferenceExcelMl extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = "2010";
    private String fromAnneeDecision = "";
    private String montantMinimum = "2";
    private String toAnneeDecision = "";

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        if (JadeStringUtil.isEmpty(montantMinimum)) {
            montantMinimum = "0";
        }
        CPDecisionForCompareCIManager mgr = new CPDecisionForCompareCIManager();
        mgr.setSession(getSession());
        mgr.changeManagerSize(BManager.SIZE_NOLIMIT);
        mgr.find();
        System.out.println(mgr.size());
        System.out.println("Numéro affilie; Montant CI; Montant CP;");
        // LE TRUNK DOIT COMPILER SVP ! DDE
        // for (int i = 0; i < mgr.size(); i++) {
        // CPDecisionForCompareCI idAffilie = (CPDecisionForCompareCI) mgr.get(i);
        // BigDecimal montantCI = this.retourneMontantCI(idAffilie.getIdAffilie());
        // BigDecimal montantCp = this.retourneMontantAffilieEnCours(idAffilie.getIdAffilie());
        // if (montantCI.compareTo(montantCp) != 0) {
        // System.out.println(this.retourneNumeroAffilie(idAffilie.getIdAffilie()) + "; " + montantCI + "; "
        // + montantCp + ";");
        // }
        // }

        return false;
    }

    public String getAnnee() {
        return annee;
    }

    @Override
    protected String getEMailObject() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getFromAnneeDecision() {
        return fromAnneeDecision;
    }

    public String getMontantMinimum() {
        return montantMinimum;
    }

    public String getToAnneeDecision() {
        return toAnneeDecision;
    }

    /**
     * Méthode qui indique si la décision est après l'affiliation
     * 
     * @param decision
     * @return vrai si la décision est après l'affiliation
     */
    public boolean isDecisionApresAffiliation(CPDecisionAffiliationCalcul decision) throws Exception {
        AFAffiliation affilie = new AFAffiliation();
        affilie.setSession(getSession());
        affilie.setAffiliationId(decision.getIdAffiliation());
        affilie.retrieve();
        if (affilie.isNew()) {
            return true;
        }
        if (JadeStringUtil.isBlankOrZero(affilie.getDateFin())) {
            return false;
        }
        if (BSessionUtil
                .compareDateFirstGreaterOrEqual(getSession(), decision.getDebutDecision(), affilie.getDateFin())) {
            return true;
        }
        return false;

    }

    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Auto-generated method stub
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Méthode qui retourne le montant qu'il devrait y avoir au CI
     * 
     * @param idAffilie
     * @return
     * @throws Exception
     */
    public BigDecimal retourneMontantAffilieEnCours(String idAffilie) throws Exception {
        BigDecimal montantCPBD = new BigDecimal("0");
        BigDecimal montantCP = new BigDecimal("0");
        String montantCiDecision = "";
        CPDecisionAffiliationCalculManager manager = new CPDecisionAffiliationCalculManager();
        manager.setSession(getSession());
        manager.setFromAnneeDecision(annee);
        manager.setTillAnneeDecision(annee);
        manager.setIsActiveOrRadie(Boolean.TRUE);
        manager.setForIdDonneesCalcul(CPDonneesCalcul.CS_REV_CI);
        manager.setForExceptTypeDecision(CPDecision.CS_REMISE);
        manager.setForExceptSpecification(CPDecision.CS_SALARIE_DISPENSE);
        manager.setInEtat(CPDecision.CS_FACTURATION + ", " + CPDecision.CS_PB_COMPTABILISATION + ", "
                + CPDecision.CS_REPRISE + ", " + CPDecision.CS_SORTIE);

        manager.setForIdAffilie(idAffilie);
        manager.changeManagerSize(0);
        manager.find();
        for (int j = 0; j < manager.size(); j++) {
            float revenuCiImputation = 0;
            CPDecisionAffiliationCalcul decision = (CPDecisionAffiliationCalcul) manager.get(j);
            if (JadeStringUtil.isEmpty(decision.getMontant())) {
                montantCiDecision = "0";
            } else {
                montantCiDecision = decision.getMontant();
            }
            if (CPDecision.CS_SALARIE_DISPENSE.equalsIgnoreCase(decision.getSpecification())
                    || CPDecision.CS_REMISE.equalsIgnoreCase(decision.getTypeDecision())
                    || CPDecision.CS_SORTIE.equals(decision.getEtat()) || isDecisionApresAffiliation(decision)) {
                montantCP = new BigDecimal("0");

            } else if (CPDecision.CS_IMPUTATION.equalsIgnoreCase(decision.getTypeDecision())) {
                montantCP = new BigDecimal(JANumberFormatter.deQuote("-" + montantCiDecision));

            } else {
                CPDonneesBase base = new CPDonneesBase();
                base.setSession(getSession());
                base.setIdDecision(decision.getIdDecision());
                base.retrieve();
                if (!base.isNew() && !JadeStringUtil.isIntegerEmpty(base.getCotisationSalarie())) {

                    // recherche coti payé en tant que salarié
                    float cotiEncode = Float.parseFloat(JANumberFormatter.deQuote(base.getCotisationSalarie()));
                    CPCotisation coti = CPCotisation._returnCotisation(getSession(), decision.getIdDecision(),
                            CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
                    if (coti != null) {
                        revenuCiImputation = (Float.parseFloat(JANumberFormatter.deQuote(coti.getMontantAnnuel())) - cotiEncode)
                                * (float) 9.9;
                        revenuCiImputation = JANumberFormatter.round(revenuCiImputation, 1, 2, JANumberFormatter.NEAR);
                        revenuCiImputation = Float.parseFloat(JANumberFormatter.deQuote(montantCiDecision))
                                - revenuCiImputation;
                    } else {
                        revenuCiImputation = cotiEncode * (float) 9.9;
                    }
                }
                montantCP = new BigDecimal(Float.parseFloat(JANumberFormatter.deQuote(montantCiDecision))
                        - revenuCiImputation);
            }
            // Si la décision est en cours de sortie => additionner le montant de la sortie
            CPSortieManager sortieManager = new CPSortieManager();
            sortieManager.setSession(getSession());
            sortieManager.setForIdDecision(decision.getIdDecision());
            // TODO ignorer les sorties historisées
            sortieManager.setForAnnee(annee);
            sortieManager.find();
            if (sortieManager.size() > 0) {
                montantCP = new BigDecimal("0");
            }
            for (int i = 0; i < sortieManager.size(); i++) {
                String montantSortie = ((CPSortie) sortieManager.getEntity(i)).getMontantCI();
                if (JadeStringUtil.isEmpty(montantSortie)) {
                    montantSortie = "0";
                }
                montantCP = montantCP.subtract(new BigDecimal(JANumberFormatter.deQuote(montantSortie)));
            }
            montantCPBD = montantCPBD.add(montantCP);

        }

        return montantCPBD;
    }

    /**
     * Méthode qui retourne la somme CI pour un idAffilie
     * 
     * @param idAffilie
     * @return le montant CI
     */
    public BigDecimal retourneMontantCI(String idAffilie) {
        CICompteIndividuelUtil util = new CICompteIndividuelUtil();
        util.setSession(getSession());
        return util.getSommeParAnneeIdAffilie(idAffilie, annee);

    }

    public String retourneNumeroAffilie(String idAffilie) throws Exception {
        AFAffiliation aff = new AFAffiliation();
        aff.setSession(getSession());
        aff.setAffiliationId(idAffilie);
        aff.retrieve();
        return aff.getAffilieNumero();

    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setFromAnneeDecision(String fromAnneeDecision) {
        this.fromAnneeDecision = fromAnneeDecision;
    }

    public void setMontantMinimum(String montantMinimum) {
        this.montantMinimum = montantMinimum;
    }

    public void setToAnneeDecision(String toAnneeDecision) {
        this.toAnneeDecision = toAnneeDecision;
    }

}
