package globaz.musca.process.interet.generic;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.musca.db.interet.generic.montantsoumis.FASumMontantSoumisParPlan;
import globaz.musca.db.interet.generic.montantsoumis.FASumMontantSoumisParPlanManager;
import globaz.osiris.api.APISection;
import globaz.osiris.db.interet.util.CAInteretUtil;
import globaz.osiris.db.interets.CAGenreInteret;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAPlanCalculInteret;
import java.util.ArrayList;

public class FAInteretDecompteFinalProcess extends FAInteretGenericProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see FAInteretGenericProcess#addRemarqueEnteteFacture(String)
     */
    @Override
    protected void addRemarqueEnteteFacture(String idEntete) throws Exception {
        // Do nothing.
    }

    /**
     * @see FAInteretGenericProcess#getDateDebutLigneDetailInteret(FASumMontantSoumisParPlan montantSoumisPlanEntete)
     */
    @Override
    protected String getDateDebutLigneDetailInteret(JACalendarGregorian calendar,
            FASumMontantSoumisParPlan montantSoumisPlanEntete) throws Exception {
        return montantSoumisPlanEntete.getDateDebutCalcul();
    }

    /**
     * @see FAInteretGenericProcess#getDateFinCalcul(int count, int total, FASumMontantSoumisParPlan
     *      montantSoumisPlanEntete, FASumMontantSoumisParPlan next) throws Exception
     */
    @Override
    protected JADate getDateFinCalcul(int count, int total, FASumMontantSoumisParPlan montantSoumisPlanEntete,
            FASumMontantSoumisParPlan next) throws Exception {
        return montantSoumisPlanEntete.getDateReceptionDecompteAsJADate();
    }

    /**
     * @see FAInteretGenericProcess#getDefaultMotifCalcul()
     */
    @Override
    protected String getDefaultMotifCalcul() {
        return CAInteretMoratoire.CS_EXEMPTE;
    }

    /**
     * @see FAInteretGenericProcess#getIdGenreInteret()
     */
    @Override
    protected String getIdGenreInteret() {
        return CAGenreInteret.CS_TYPE_DECOMPTE_FINAL;
    }

    /**
     * @see FAInteretGenericProcess#getLimiteExempte(BSession osirisSession) throws Exception
     */
    @Override
    protected FWCurrency getLimiteExempte(BSession osirisSession) throws Exception {
        return new FWCurrency(CAInteretUtil.getLimiteExempteInteretMoratoire(osirisSession, getTransaction()));
    }

    /**
     * @see FAInteretGenericProcess#getSumMontantSoumisParPlanManager(CAPlanCalculInteret plan)
     */
    @Override
    protected FASumMontantSoumisParPlanManager getSumMontantSoumisParPlanManager(CAPlanCalculInteret plan) {
        FASumMontantSoumisParPlanManager manager = new FASumMontantSoumisParPlanManager();
        manager.setSession(getSession());

        manager.setForIdPassage(getPassage().getIdPassage());
        manager.setForIdPlan(plan.getIdPlanCalculInteret());

        manager.setForMontantPositif(true);

        ArrayList<String> idSousTypeIn = new ArrayList<String>();
        idSousTypeIn.add(APISection.ID_CATEGORIE_SECTION_DECOMPTE_FINAL);
        idSousTypeIn.add(APISection.ID_CATEGORIE_SECTION_BOUCLEMENT_ACOMPTE);
        idSousTypeIn.add(APISection.ID_CATEGORIE_SECTION_LTN);
        manager.setForIdSousTypeIn(idSousTypeIn);

        return manager;
    }

    /**
     * @see FAInteretGenericProcess#isLigneCandidatAInteret(JACalendarGregorian calendar, FASumMontantSoumisParPlan
     *      montantSoumisPlanEntete) throws Exception
     */
    @Override
    protected boolean isLigneCandidatAInteret(JACalendarGregorian calendar,
            FASumMontantSoumisParPlan montantSoumisPlanEntete) throws Exception {
        return (montantSoumisPlanEntete.isMontantPositif())
                && (calendar.compare(montantSoumisPlanEntete.getDateReceptionDecompteAsJADate(),
                        montantSoumisPlanEntete.getDateLimite()) == JACalendar.COMPARE_FIRSTUPPER);
    }

    /**
     * @see FAInteretGenericProcess#isMontantSoumis(FWCurrency limiteExempte, FWCurrency montantInteretCumule)
     */
    @Override
    protected boolean isMontantSoumis(FWCurrency limiteExempte, FWCurrency montantInteretCumule) {
        return (montantInteretCumule.compareTo(limiteExempte) == 1);
    }
}
