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

public class FAInteretDifference25PourCentProcess extends FAInteretGenericProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see FAInteretGenericProcess#addRemarqueEnteteFacture(String)
     */
    @Override
    protected void addRemarqueEnteteFacture(String idEntete) throws Exception {
        // Inforom321 : Suppression de la remarque
        // FAEnteteFacture entete = new FAEnteteFacture();
        // entete.setSession(this.getSession());
        //
        // entete.setIdEntete(idEntete);
        //
        // entete.retrieve(this.getTransaction());
        //
        // if (entete.hasErrors() || entete.isNew()) {
        // throw new Exception(this.getSession().getLabel("ENTETE_FACTURE_NON_TROUVEE"));
        // }
        // FWCurrency totalFacture = new FWCurrency(entete.getTotalFacture());
        // if (totalFacture.isPositive()) {
        // entete.setRemarque(this.getSession().getLabel("INTERETS_FACTURES_RECEPTION_PAIEMENT"));
        //
        // entete.update(this.getTransaction());
        // }
    }

    /**
     * @see FAInteretGenericProcess#getDateDebutLigneDetailInteret(FASumMontantSoumisParPlan montantSoumisPlanEntete)
     */
    @Override
    protected String getDateDebutLigneDetailInteret(JACalendarGregorian calendar,
            FASumMontantSoumisParPlan montantSoumisPlanEntete) throws Exception {
        return "01.01." + (Integer.parseInt(montantSoumisPlanEntete.getAnneeCotisation()) + 2);
    }

    /**
     * @see FAInteretGenericProcess#getDateFinCalcul(int count, int total, FASumMontantSoumisParPlan
     *      montantSoumisPlanEntete, FASumMontantSoumisParPlan next) throws Exception
     */
    @Override
    protected JADate getDateFinCalcul(int count, int total, FASumMontantSoumisParPlan montantSoumisPlanEntete,
            FASumMontantSoumisParPlan next) throws Exception {
        return new JADate(getPassage().getDateFacturation());
    }

    /**
     * @see FAInteretGenericProcess#getDefaultMotifCalcul()
     */
    @Override
    protected String getDefaultMotifCalcul() {
        // Inforom321 modifié de CS_ATTENTE_PAIEMENT à CS_EXEMPTE
        return CAInteretMoratoire.CS_EXEMPTE;
    }

    /**
     * @see FAInteretGenericProcess#getIdGenreInteret()
     */
    @Override
    protected String getIdGenreInteret() {
        return CAGenreInteret.CS_TYPE_COTISATIONS_PERSONNELLES;
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
        idSousTypeIn.add(APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS);
        idSousTypeIn.add(APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS_ETUDIANT);
        manager.setForIdSousTypeIn(idSousTypeIn);

        manager.setForReferenceExterneCotPers(true);
        manager.setForAnneeCotisationInferieur("" + (JACalendar.today().getYear() - 1));

        return manager;
    }

    /**
     * @see FAInteretGenericProcess#isLigneCandidatAInteret(JACalendarGregorian calendar, FASumMontantSoumisParPlan
     *      montantSoumisPlanEntete) throws Exception
     */
    @Override
    protected boolean isLigneCandidatAInteret(JACalendarGregorian calendar,
            FASumMontantSoumisParPlan montantSoumisPlanEntete) throws Exception {
        if (montantSoumisPlanEntete.isMontantNegatif()) {
            return false;
        }

        if (!new FWCurrency(montantSoumisPlanEntete.getMontantDejaFacture()).isZero()
                || CAInteretMoratoire.CS_AFFILIATION_RETROACTIVE.equals(montantSoumisPlanEntete
                        .getTypeCalculInteretMoratoire())) {
            // Vérifier si la différence entre provisoire et défintif est
            // supérieure à 25% du définitif
            float provisoire = new FWCurrency(montantSoumisPlanEntete.getMontantDejaFacture()).floatValue();
            float definitif = new FWCurrency(montantSoumisPlanEntete.getMontantInitial()).floatValue();
            float difference = definitif - provisoire;

            // Différence supérieure à zéro
            if (difference > 0) {
                // Calculer 25% de la cotisation définitive
                float f25 = definitif * 0.25f;

                return (difference >= f25);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * @see FAInteretGenericProcess#isMontantSoumis(FWCurrency limiteExempte, FWCurrency montantInteretCumule)
     */
    @Override
    protected boolean isMontantSoumis(FWCurrency limiteExempte, FWCurrency montantInteretCumule) {
        // Inforom321 Modification du return false; par :
        return (montantInteretCumule.compareTo(limiteExempte) == 1);
    }

}
