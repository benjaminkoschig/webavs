package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune;

import java.util.HashSet;
import java.util.Set;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCBienImmoAnnexe;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCBienImmoNonHabitable;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCBienImmoPrincipal;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCDessaisissementFortune;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.business.models.dessaisissement.CalculContrePresation;
import ch.globaz.pegasus.business.models.dessaisissement.CalculContreprestationContainer;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortune;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortuneSearch;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.IStrategieDessaisissable;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategiesFactory;

public class StrategieDessaisissementFortune extends StrategieCalculFortune {

    private float calculeContrePrestationAuto(CalculDonneesCC donnee) throws CalculException {
        CalculContreprestationContainer container = new CalculContreprestationContainer();

        container.setDateDebut(donnee.getDateDebutDonneeFinanciere());
        container.setDateValeur(donnee.getDateDebutDonneeFinanciere());
        container.setDateFin(donnee.getDateFinDonneeFinanciere());
        container.setCharges("0");

        String csTypeDonneeFinanciere = donnee.getCsTypeDonneeFinanciere();
        if (IPCBienImmoAnnexe.CS_TYPE_DONNEE_FINANCIERE.equals(csTypeDonneeFinanciere)) {
            container.setDeductionMontantDessaisi(donnee.getBienImmoAnnexeMontantDetteHypothecaire());
            container.setMontantBrut(donnee.getBienImmoAnnexeMontantValeurVenale());
            container.setRendementFortune(donnee.getBienImmoAnnexeMontantValeurLocative());
        } else if (IPCBienImmoNonHabitable.CS_TYPE_DONNEE_FINANCIERE.equals(csTypeDonneeFinanciere)) {
            container.setDeductionMontantDessaisi(donnee.getBienImmoNonHabitableMontantDetteHypothecaire());
            container.setMontantBrut(donnee.getBienImmoNonHabitableMontantValeurVenale());
            container.setRendementFortune("0");
        } else if (IPCBienImmoPrincipal.CS_TYPE_DONNEE_FINANCIERE.equals(csTypeDonneeFinanciere)) {
            container.setDeductionMontantDessaisi(donnee.getBienImmoPrincipalMontantDetteHypothecaire());
            container.setMontantBrut(donnee.getBienImmoPrincipalMontantValeurFiscale());
            container.setRendementFortune(donnee.getBienImmoPrincipalMontantValeurLocative());
        } else {
            throw new CalculException("Invalid type donnee financiere header : " + csTypeDonneeFinanciere);
        }

        float result = 0f;
        try {
            CalculContrePresation ccp = PegasusImplServiceLocator.getDessaisissementFortuneService()
                    .calculContrePresation(container);
            float sommeContrePrestation = ccp.getTotalArrondi().floatValue();
            result += Math.max(0, sommeContrePrestation);
        } catch (Exception e) {
            throw new CalculException("An error happened during calcul contre-prestation", e);
        }
        return result;
    }

    private float calculeContrePrestationManuelle(CalculDonneesCC donnee) throws CalculException {
        DessaisissementFortuneSearch dessaisissementSearch = new DessaisissementFortuneSearch();
        dessaisissementSearch.setForIdDonneeFinanciereHeader(donnee.getIdDonneeFinanciereHeader());
        float result = 0f;
        try {
            dessaisissementSearch = PegasusImplServiceLocator.getDessaisissementFortuneService().search(
                    dessaisissementSearch);

            if (dessaisissementSearch.getSize() > 0) {

                DessaisissementFortune dessaisissementFortune = (DessaisissementFortune) dessaisissementSearch
                        .getSearchResults()[0];

                if (dessaisissementFortune.getSimpleDessaisissementFortune().getIsContrePrestation()) {

                    CalculContrePresation ccp = PegasusImplServiceLocator.getDessaisissementFortuneService()
                            .calculContrePresation(dessaisissementFortune);
                    result += ccp.getTotalArrondi().floatValue();
                }
            } else {
                throw new CalculException("Could find dessaisissementFortune for given idDonneeFinanciereHeader : "
                        + donnee.getIdDonneeFinanciereHeader());
            }
        } catch (Exception e) {
            throw new CalculException("An error happened during calcul contre-prestation", e);
        }
        return result;
    }

    @Override
    protected TupleDonneeRapport calculeFortune(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        String dateValidite = (String) context.get(Attribut.DATE_DEBUT_PERIODE);

        final String csTypeDF = donnee.getCsTypeDonneeFinanciere();

        float totalDF = 0.0f;
        // Si dessaisissement de fortune
        if (IPCDessaisissementFortune.CS_TYPE_DONNEE_FINANCIERE.equals(csTypeDF)) {

            // calcul de la contre-prestation
            totalDF += calculeContrePrestationManuelle(donnee);
            // Si la contreprestation est a 0, on est dans le cas sans cp
            if (totalDF == 0.0f) {
                // cas de dessaisissement manuel
                totalDF = checkAmoutAndParseAsFloat(donnee.getDessaisissementFortuneMontant())
                        - checkAmoutAndParseAsFloat(donnee.getDessaisissementFortuneDeductions());

                if (IPCDessaisissementFortune.CS_MOTIF_DESSAISI_CONSOMMATION_EXCESSIVE.equals(donnee.getDessaisissementFortuneType())) {
                    // Si l'on est sur un déssaisissement de fortune pour motif consommation excessive
                    // On fixe les clé pour Registre PC
                    this.ecraseChildExistant(resultatExistant, IPCValeursPlanCalcul.CLE_IS_DIVESTED_WEALTH,
                            Boolean.TRUE);
                    // 2 = consommation excessive
                    // TODO mettre un enum
                    this.ecraseChildExistant(resultatExistant, IPCValeursPlanCalcul.CLE_TYPE_DIVESTED_WEALTH,
                            "2");

                }

            }

        } else {
            // cas de dessaisissement auto
            IStrategieDessaisissable strategie = (IStrategieDessaisissable) StrategiesFactory.getFortuneFactory()
                    .getStrategie(csTypeDF, dateValidite);
            totalDF = strategie.calculeMontantDessaisi(donnee, context);

            final Set<String> listeTypes = new HashSet<String>(3) {
                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                {
                    add(IPCBienImmoPrincipal.CS_TYPE_DONNEE_FINANCIERE);
                    add(IPCBienImmoAnnexe.CS_TYPE_DONNEE_FINANCIERE);
                    add(IPCBienImmoNonHabitable.CS_TYPE_DONNEE_FINANCIERE);
                }
            };

            if (listeTypes.contains(donnee.getCsTypeDonneeFinanciere())) {
                // calcul de la contre prestation
                totalDF += calculeContrePrestationAuto(donnee);
            }

            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_IS_DIVESTED_WEALTH,
                    donnee.getIsDessaisissementFortune());

            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_TYPE_DIVESTED_WEALTH,
                    donnee.getTypeDessaisissementFortune());

        }
        TupleDonneeRapport tupleRoot = this.getOrCreateChild(resultatExistant,
                IPCValeursPlanCalcul.CLE_INTER_FORTUNES_DESSAISIES, 0f);

        TupleDonneeRapport tuple = this.getOrCreateChild(tupleRoot, donnee.getIdDonneeFinanciereHeader(), 0f);

        TupleDonneeRapport tupleDate = this.getOrCreateChild(tuple,
                IPCValeursPlanCalcul.CLE_INTER_FORTUNE_DESSAISIE_DATE, 0f);
        tupleDate.setLegende(donnee.getDateDebutDonneeFinanciere());
        this.getOrCreateChild(tuple, IPCValeursPlanCalcul.CLE_INTER_FORTUNE_DESSAISIE_MONTANT, totalDF);

        return resultatExistant;
    }

}
