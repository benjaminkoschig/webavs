package ch.globaz.pegasus.rpc.domaine;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.constantes.EPCRegionLoyer;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.domaine.pca.Calcul;
import ch.globaz.pegasus.business.domaine.pca.PcaEtatCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.rpc.businessImpl.converter.Converter2469_101;
import ch.globaz.pegasus.utils.PCApplicationUtil;
import globaz.jade.log.JadeLogger;
import org.apache.commons.lang.math.Fraction;

public class RpcCalcul {

    private final Calcul calcul;
    private final boolean isCoupleSepare;

    public RpcCalcul(Calcul calcul, boolean isCoupleSepare) {
        this.calcul = calcul;
        this.isCoupleSepare = IPCValeursPlanCalcul.STATUS_CALCUL_SEPARE_MALADIE.equals(calcul.getTypeSeparation());
    }
    
    /**
     * FC7
     */
    public Montant getMontantSansAssuranceMaladie() {

        Montant partcanton = getPartCantonaleAnnuelle();
        if (partcanton != null && partcanton.greater(Montant.ZERO)) {
            return calcul.getTotalFederalMensuel().annualise();
        }
        return calcul.getTotalCaisseCompDeduitMensuel().annualise();
    }

    /**
     * FC8
     */
    public Montant getMontantAvecAssuranceMaladie(boolean isReforme) {
        Montant partcanton = getPartCantonaleAnnuelle();
        Montant primeMaladie;
        if (isReforme) {
            primeMaladie = calcul.getMontantPrimeMaladie();
        } else {
            primeMaladie = calcul.getTotalPrimeMaladie();
        }
        if (partcanton != null && partcanton.greater(Montant.ZERO)) {
            return calcul.getTotalFederalMensuel().annualise().add(primeMaladie);
        }
        return calcul.getTotalCaisseCompDeduitMensuel().annualise().add(primeMaladie);
    }

    /**
     * FC9
     */
    public int getPlafonnementDesPC() {
        if (calcul.getCasMinimumGaranti() == 1) {
            return Converter2469_101.XSD_ELLIMIT_PLAFONNEMENT_CAS_MINIMUM;
        }
        // TODO cas de plaffonnement non detectable
        return Converter2469_101.XSD_ELLIMIT_PAS_DE_PLAFONNEMENT;

    }

    /**
     * FC12
     */
    public Montant getAutreFortunes() {
        return divideIfCoupleSepare(toZeroIfNegate(calcul.getFortuneSousTotal()
                .substract(calcul.getFortuneImmobilierTotal()).substract(calcul.getFortuneDessaisieTotal())));
    }

    /**
     * FC13
     */
    public Montant getFortuneDessaisie() {
        return divideIfCoupleSepare(calcul.getFortuneDessaisieTotal());
        // return (calcul.getFortuneDessaisieTotal());
    }

    /**
     * FC15
     */
    public Montant getAutresDettes() {
        // return (calcul.getFortuneAutresDettesTotal());
        return divideIfCoupleSepare(calcul.getFortuneAutresDettesTotal());
    }

    /**
     * FC16
     */
    public Montant getFranchiseSurFortune() {
        return divideIfCoupleSepare(calcul.getFortuneDeductionLegaleTotal());
        // return (calcul.getFortuneDeductionLegaleTotal());
    }

    /**
     * FC18
     */
    public Montant getFortuneAPrendreEnCompte() {
        return divideIfCoupleSepare(calcul.getFortuneTotalNetTotal());
        // return (calcul.getFortuneTotalNetTotal());
        // FIXME eclaircir la spec
        // return new Montant(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_SOUS_TOTAL));
    }
    
    
    /**
     * FC27
     */
    public Montant getRentGrossTotal() {
        return calcul.getLoyerMontantBrut()
                .add(calcul.getLoyerMontantNet())
                .add(calcul.getLoyerCharge())
                .add(calcul.getLoyerFraisDeChauffage())
                .add(calcul.getLoyerPensionNonReconnue())
                .add(calcul.getLoyerValeurLocativeAppHabite());
    }

    /**
     * FC20
     */
    public Montant getRevenusDeLaFortune() {
        Montant sum = calcul.getRevenusInteretFortuneMobilierTotal()
                .add(calcul.getRevenusInteretDessaisissementFortuneTotal());
        // 8690.7179.41 getRevenusImposableFortuneTotal
        // getRevenusInteretTitre
        return divideByTwoIfCoupleSepare(sum);
    }

    /**
     * FC24
     */
    public Montant getRevenusDeLaFortunePrisEnCompte() {
        return divideByTwoIfCoupleSepare(calcul.getRevenusImposableFortuneTotal());
    }

    /**
     * FC41
     */
    public Montant getRevenusTotalAPrendreEnCompte() {
        // DIVISION PAR DEUX NECESSAIRE
        return divideByTwoIfCoupleSepare(calcul.getRevenusActiviteLucrativeRevenuPrisEnCompte()
                .add(calcul.getRevenusActiviteLucrativeRevenuPrivilegie()));
    }
    
    /**
     * FC25
     */
    public double getPartDesRevenusdeLaFortunePrisEnCompte() {
        return calcul.getFractionRevenusImposableFortuneTotal();
    }

    /**
     * FC33
     */
    public Montant getBesoinsVitaux() {
        return calcul.getDepensesBesoinsVitauxTotal();
    }

    /**
     * FC??
     */
    public Montant getImmobilier() {
        return calcul.getFortuneImmobilierTotal();
    }

    /**
     * FC??
     */
    public Montant getLoyers() {
        return calcul.getDepenseGroupeLoyerTotal();
    }

    /**
     * FC10
     */
    public Montant getFortuneImmobiliere() {
        Montant somme = calcul.getFortuneImmoBiensNonHabitablePrincipale();
        somme = somme.add(calcul.getFortuneImmoBiensNonHabitables());

        return divideIfCoupleSepare(somme);
        // return (somme);
    }

    /**
     * FC14
     */
    public Montant getDettesHypothequaires() {
        return divideIfCoupleSepare(calcul.getFortuneDetteHypothequaireTotal());
        // return (calcul.getFortuneDetteHypothequaireTotal());
    }


    /**
     * FC21
     */
    public Montant getRevenusFortuneImmobiliere() {
        Montant sum = calcul.getRevenusLoyerSousLocationNet();
        if(sum.isZero()) {
            sum = sum.add(calcul.getRevenusLoyerSousLocation());
        }
        sum = sum.add(calcul.getRevenusRevenusLocations());
        sum = sum.add(calcul.getRevenusBiensImmoNonHabitable());

        return divideByTwoIfCoupleSepare(sum).addAnnuelPeriodicity();
    }

    /**
     * FC30
     */
    public Montant getInteretsHypothequaires() {
        return calcul.getDepensesFraisImmoInteretHypothequaire();
    }

    /**
     * FC31
     */
    public Montant getFraisEntretien() {
        return calcul.getDepensesFraisImmoEntretienImmeuble();
    }

    /**
     * Help pour charge de la valeur FC21
     */
    public Montant getDepensesFraisImmoInteretHypothequaire() {
        return calcul.getDepensesFraisImmoInteretHypothequaire();
    }

    /**
     * FC32
     */
    public Montant getInteretsHypothequairesFraisMaintenance() {
        return calcul.getDepensesFraisImmoTotalPlafonne();
    }

    /**
     * FC11
     */
    public Montant getValeurImmeubleHabitation() {
        return divideIfCoupleSepare(calcul.getFortuneImmoBiensHabitationPrincipale());
        // return (calcul.getFortuneImmoBiensHabitationPrincipale());
    }

    /**
     * FC17
     */
    public Montant getFranchiseImmeubleHabitation() {
        return divideIfCoupleSepare(calcul.getFortuneImmoDeductionForfaitaire());
        // return (calcul.getFortuneImmoDeductionForfaitaire());
    }

    /**
     * @deprecated
     *             cette valeur n'est pas exploitable car regroupe FC22 et FC23 + l'immobilier secondaire
     */

    @Deprecated
    public Montant getRevenuValeurLocativeAppHabitePrincipale() {
        if (isCoupleSepare) {
            return calcul.getRevenuValeurLocativeAppHabitePrincipale();
        }
        if (!calcul.getRevenuFortuneMobiliereTotal().isZero()) {
            return calcul.getRevenuValeurLocativeAppHabitePrincipale();
        }
        return Montant.ZERO_ANNUEL;
    }
    
    /**
     * FC22
     */
    public Montant getDepensesLoyerValeurLocativeAppHabite() {
        if(getValeurImmeubleHabitation().isZero() || calcul.isHomeDroitHabitation()) {
            return Montant.ZERO; 
        } else {
            return calcul.getRevenuValeurLocativeAppHabite();
        }
    }
    
    
    /**
     * FC23
     */
    public Montant getUsufruit() {
        
        if(!calcul.isHomeDroitHabitation()) {
            Montant usuFruit = divideByTwoIfCoupleSepare(calcul.getRevenuDroitHabitation());
            if(getValeurImmeubleHabitation().isZero()) {
                return usuFruit.add(calcul.getRevenuValeurLocativeAppHabite());
            }
            return usuFruit;
        }
        return Montant.ZERO; 
        
    }

    /**
     * FC19
     * plafonn? F?d?ral (sans d?plafonnement loyer)
     */
    public Montant getLoyerBrutEnCompte() {
        Montant loyerEnCompte = calcul.getDepensesLoyerTotal();
        Montant plafond = getLoyerMaximum();
        if (loyerEnCompte.greater(plafond)) {
            return plafond;
        }
        return loyerEnCompte;
    }

    /**
     * FC28
     */
    public Montant getPartLoyerTotatBrut() {
        Montant loyer = calcul.getDepensesLoyerTotalNonPlafonne();
        Montant plafond = getLoyerMaximum();
        return loyer;
    }

    /**
     * FC29
     * plafonn? F?d?ral (sans d?plafonnement loyer)
     */
    public Montant getLoyerMaximum() {
        Montant plafondFed = getPlafondFederal();
        try {
            if (!PCApplicationUtil.isCantonVD() && !PCApplicationUtil.isCantonVS()  && plafondFed != null && plafondFed.greater(Montant.ZERO)) {
                return plafondFed;
            }
        } catch (CalculException e) {
            JadeLogger.error(RpcCalcul.class, e.toString());
        }
        return calcul.getLegendeDepensesLoyerPlafonne();
    }

    /**
     * E5
     */
    public Montant getPrestationLamalLca() {
        return calcul.getRevenusAutreIJLCA();
    }

    /**
     * E6
     */
    public Montant getRevenusBrutActiviteLucrative() {
        Montant somme = calcul.getRevenusActiviteLucrativeDependante();
        somme = somme.add(calcul.getRevenusActiviteLucrativeIndependante());
        somme = somme.add(calcul.getRevenusActiviteLucrativeIndependanteAgricole());
        return somme;
    }
    
    public Montant getRevenusActiviteLucrativeIndependanteAgricole() {
        return calcul.getRevenusActiviteLucrativeIndependanteAgricole();
    }

    /**
     * E10
     */
    public Montant getRenteLPP() {
        return calcul.getRevenusRenteAutreRenteLPP();
    }

    /**
     * E11
     */
    public Montant getRenteEtrangere() {
        return calcul.getRevenusRenteAutreRenteEtrangere();
    }

    /**
     * E28
     */
    public Montant getRevenuHypothetiqueBrut() {
        return calcul.getRevenusActiviteLucrativeRevenuHypothetique();
    }

    /**
     * E??
     */
    public Montant getTaxesHome() {
        return calcul.getDepensesLoyerTaxesPensionReconnue();
    }

    /**
     * E2
     */
    public Montant getRenteAvsAi() {
        return calcul.getRevenusRenteAvsAiTotal();
    }

    /**
     * E3
     */
    public Montant getAPI() {
        return calcul.getRevenusAPIAVSAI();
    }

    /**
     * E4
     */
    public Montant getIndemnitesJournalieres() {
        Montant somme = calcul.getRevenusAutreIJChomage();
        somme = somme.add(calcul.getRevenusAutreIJLAA());
        somme = somme.add(calcul.getRevenusAutreIJLAM());
        somme = somme.add(calcul.getRevenusAutreIJLamal());
        somme = somme.add(calcul.getRevenusAutreIJLCA());
        somme = somme.add(calcul.getRevenusAutreIJAI());
        return somme;
    }

    /**
     * E15, E16, E17, E18
     */
    // FIXME Detail inconnu?
    public Montant getTaxeHomeHotellerie() {
        return calcul.getDepensesLoyerTaxesPensionReconnue();
    }

    /**
     * E19
     */
    // FIXME Detail inconnu?
    public Montant getTaxeHomeTotal() {
        return calcul.getDepensesLoyerTaxesPensionReconnue();
    }

    /**
     * E23
     */
    public Montant getDepensesPersonnelles() {
        return calcul.getDepensesPersonnellesTotal();
    }


    /**
     * Depalfonnement de loyer
     *
     * @return la cl? additionnele de plafond f?d?ral
     */
    public Montant getPlafondFederal() {
        return calcul.getPlafondFederal();
    }

    /**
     * * Depalfonnement de loyer
     *
     * @return la part cantonale annuelel
     */
    public Montant getPartCantonaleAnnuelle() {
        return calcul.getPartCantonaleAnnuelle();
    }

    public Montant getTotalCalcul() {
        return calcul.getTotalCalculDeduit();
    }

    public PcaEtatCalcul getEtatCalculFederal() {
        return calcul.getEtatCalculFederal();
    }

    public String getId() {
        return calcul.getId();
    }

    private Montant divideByTwoIfCoupleSepare(Montant sum) {
        if (isCoupleSepare) {
            return sum.divide(2).arrondiAUnIntierInferior();
        }
        return sum;
    }

    private Montant divideIfCoupleSepare(Montant sum) {
        if (isCoupleSepare) {
            if(calcul.hasPartCoupleSepare()) {
                try {
                    Fraction fraction = Fraction.getFraction(calcul.getPartCoupleSepare());
                    return sum.multiply(fraction.doubleValue()).arrondiAUnIntierInferior();
                } catch (NumberFormatException e) {
                    return sum.divide(2).arrondiAUnIntierInferior();
                }
            } else {
                return sum.divide(2).arrondiAUnIntierInferior();
            }

        }
        return sum;
    }

    private Montant toZeroIfNegate(Montant montant) {
        if (montant.isNegative()) {
            return Montant.ZERO_ANNUEL;
        }
        return montant;
    }

    public boolean isCoupleSepare() {
        return isCoupleSepare;
    }

    public EPCRegionLoyer getLoyerRegion(){
        return calcul.getLoyerRegion();
    }

    /**
     * Localit? du requ?rant pris dans les loyers ou le bien d'habitation principal
      * @return l'id localit? du r?qu?rant
     */
    public String getLocalite(){
        return calcul.getLocalite();
    }


    /**
     * FC41
     */
    public boolean isWheelchairSurcharge(){
        return !calcul.getLoyerFauteuilRoulant().isZero();
    }

    /**
     * FC44
     */
    public Montant getDettesHypothequairesRealProperty() {
        return divideIfCoupleSepare(calcul.getFortuneDetteHypothequairePasPrincipale()
                .add(calcul.getFortuneDetteHypothequaireTotalNothabited()));
    }

    /**
     * FC43
     */
    public Montant getDettesHypothequairesSelfinhabited() {
        return divideIfCoupleSepare(calcul.getFortuneDetteHypothequairePrincipale());
    }

    /**
     * FC45
     *
     */


    /**
     * FC46
     */
    public String getTypeDivestedWealth() {
        return calcul.getTypeDivestedWealth();
    }


    public boolean isDivestedWealth() {
        return calcul.isDivestedWealth();
    }

    /**
     * FC49
     */
    public long getFamilySize() {
        return calcul.getFamilySize();
    }

}
