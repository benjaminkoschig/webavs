package ch.globaz.pegasus.business.domaine.pca;

import ch.globaz.pegasus.business.constantes.EPCRegionLoyer;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import org.apache.commons.lang.math.Fraction;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCBienImmoPrincipal;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

public class Calcul {
    private final TupleDonneeRapport tuple;
    private final int size;
    private final String id;
    private int nbPersonne;

    public Calcul(TupleDonneeRapport tuple, int size, String id) {
        this.tuple = tuple;
        this.size = size;
        this.id = id;
    }

    public int getNbPersonne() {
        return nbPersonne;
    }

    public void setNbPersonne(int nbPersonne) {
        this.nbPersonne = nbPersonne;
    }

    public TupleDonneeRapport getTuple() {
        return tuple;
    }

    public int getSize() {
        return size;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Calcul [tuple=" + tuple + ", size=" + size + ", id=" + id + "]";
    }

    /**
     * CLE_TOTAL_CC_DEDUIT_MENSUEL
     */
    public Montant getTotalCaisseCompDeduitMensuel() {
        return Montant.newMensuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_CC_DEDUIT_MENSUEL));
    }

    /**
     * CLE_TOTAL_CC_MENSUEL_CALCULE_FEDERAL
     */
    public Montant getTotalFederalMensuel() {
        return Montant.newMensuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_CC_MENSUEL_CALCULE_FEDERAL));
    }

    /**
     * MONTANT_VERSE_CAISSE_MALADIE
     */
    public Montant getMontantPrimeMaladie() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.MONTANT_VERSE_CAISSE_MALADIE));
    }

    /**
     * CLE_TOTAL_PRIMEMAL_TOTAL
     */
    public Montant getTotalPrimeMaladie() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_PRIMEMAL_TOTAL));
    }

    /**
     * FC9
     * que fédéral
     */
    public Float getCasMinimumGaranti() {
        return tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_CC_MENSUEL_MINIMAL_APPLIQUE_FEDERAL);
    }

    /**
     * CLE_FORTU_FOR_MOBI_AUTRE
     */
    public Montant getFortuneMobilierAutre() {
        return new Montant(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_AUTRE));
    }

    /**
     * CLE_FORTU_SOUS_TOTAL
     */
    public Montant getFortuneSousTotal() {
        return new Montant(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_SOUS_TOTAL));
    }

    /**
     * CLE_FORTU_FOR_DESS_TOTAL
     */
    public Montant getFortuneDessaisieTotal() {
        return new Montant(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_DESS_TOTAL));
    }

    /**
     * CLE_FORTU_AUT_DETT_TOTAL
     */
    public Montant getFortuneAutresDettesTotal() {
        return new Montant(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_AUT_DETT_TOTAL));
    }

    /**
     * CLE_FORTU_DED_LEGA_TOTAL
     */
    public Montant getFortuneDeductionLegaleTotal() {
        return new Montant(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DED_LEGA_TOTAL));
    }

    /**
     * CLE_FORTU_TOTALNET_TOTAL
     */
    public Montant getFortuneTotalNetTotal() {
        return new Montant(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL));
        // return new Montant(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_SOUS_TOTAL));
    }

    /**
     * CLE_REVEN_INTFORMO_TOTAL
     */
    public Montant getRevenusInteretFortuneMobilierTotal() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_TOTAL));
    }

    /**
     * CLE_REVEN_INTFORMO_INTERETS_COMPTES_BANCAIRE
     */
    public Montant getRevenusInteretsComteBancaire() {
        return Montant.newAnnuel(tuple
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_INTERETS_COMPTES_BANCAIRE));
    }

    /**
     * CLE_REVEN_INTDESFO_TOTAL
     */
    public Montant getRevenusInteretDessaisissementFortuneTotal() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_INTDESFO_TOTAL));
    }

    /**
     * CLE_REVEN_INTFORMO_TOTAL
     */
    public Montant getRevenusInteretCompteBancaireCCP() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_INTERETS_COMPTEBANCAIRECPP));
    }

    /**
     * CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE
     */
    public Montant getRevenusActiviteLucrativeRevenuPrivilegie() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE));
    }

    /**
     * CLE_REVEN_ACT_LUCR_REVENU_PRIS_EN_COMPTE
     */
    public Montant getRevenusActiviteLucrativeRevenuPrisEnCompte() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIS_EN_COMPTE));
    }


    /**
     * CLE_REVEN_AUTREREV_ALLOCATIONS_FAMILLIALES
     */
    public Montant getRevenusAllocationsFamilliales() {
        return Montant
                .newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_ALLOCATIONS_FAMILLIALES));
    }

    /**
     * CLE_REVEN_IMP_FORT_TOTAL
     */
    public double getFractionRevenusImposableFortuneTotal() {
        return Fraction.getFraction(tuple.getLegendeEnfant(IPCValeursPlanCalcul.CLE_REVEN_IMP_FORT_TOTAL))
                .doubleValue();
    }

    /**
     * CLE_REVEN_IMP_FORT_TOTAL
     */
    public Montant getRevenusImposableFortuneTotal() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_IMP_FORT_TOTAL));
    }

    /**
     * CLE_REVEN_INTFORMO_RENDEMENT_TITRES
     */
    public Montant getRevenusInteretTitre() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_RENDEMENT_TITRES));
    }

    /**
     * CLE_DEPEN_BES_VITA_TOTAL
     */
    public Montant getDepensesBesoinsVitauxTotal() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_BES_VITA_TOTAL));
    }

    /**
     * CLE_FORTU_FOR_IMMO_TOTAL
     */
    public Montant getFortuneImmobilierTotal() {
        return new Montant(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_TOTAL));
    }

    /**
     * CLE_DEPEN_GR_LOYER_LOYER_NET
     */
    public Montant getDepensesLoyerNet() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_LOYER_NET));
    }

    /**
     * CLE_DEPEN_GR_LOYER_ACCOMPTE_CHARGES
     */
    public Montant getDepensesLoyerCharge() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_ACCOMPTE_CHARGES));
    }

    /**
     * CLE_DEPEN_GR_LOYER_LOYER_BRUT
     */
    public Montant getDepensesLoyerBrut() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_LOYER_BRUT));
    }

    /**
     * CLE_DEPEN_GR_LOYER_LOYER_BRUT
     */
    public Montant getLoyerBrut() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_LOYER_MONTANT_BRUT));
    }


    /**
     * CLE_FORTU_FOR_IMMO_BIENS_NON_HABIT_PRINCIPALE
     */
    public Montant getFortuneImmoBiensNonHabitablePrincipale() {
        return new Montant(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABIT_PRINCIPALE));
    }

    /**
     * CLE_FORTU_FOR_IMMO_BIENS_NON_HABITABLES
     */
    public Montant getFortuneImmoBiensNonHabitables() {
        return new Montant(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABITABLES));
    }

    /**
     * CLE_FORTU_DETE_HYP_TOTAL
     */
    public Montant getFortuneDetteHypothequaireTotal() {
        return new Montant(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_TOTAL));
    }

    /**
     * CLE_FORTU_DETE_HYP_REAL_PROPERTY
     */
    public Montant getFortuneDetteHypothequairePrincipale() {
        return new Montant(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_REAL_PROPERTY));
    }

    /**
     * CLE_FORTU_DETE_HYP_SELF_INHABITED
     */
    public Montant getFortuneDetteHypothequairePasPrincipale() {
        return new Montant(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_SELF_INHABITED));
    }

    /**
     * CLE_FORTU_DETE_HYP_NOT_HABITED
     */
    public Montant getFortuneDetteHypothequaireTotalNothabited() {
        return new Montant(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_NOT_HABITED));
    }

    /**
     * CLE_FAMILY_SIZE
     */
    public long getFamilySize() {
        String familySize = String.valueOf(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_FAMILY_SIZE));
        familySize = familySize.substring(0, familySize.indexOf("."));
        return Long.parseLong(familySize);
    }

    /**
     * CLE_REVEN_LOYER_SOUS_LOCATION_NET
     */
    public Montant getRevenusLoyerSousLocationNet() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_LOYER_SOUS_LOCATION_NET));
    }

    /**
     * CLE_REVEN_RENFORMO_REVENUS_SOUS_LOCATIONS
     */
    public Montant getRevenusLoyerSousLocation() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_REVENUS_SOUS_LOCATIONS));
    }

    /**
     * CLE_REVEN_RENFORMO_REVENUS_LOCATIONS
     */
    public Montant getRevenusRevenusLocations() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_REVENUS_LOCATIONS));
    }

    /**
     * CLE_REVEN_RENFORMO_BIENS_IMMO_NON_HABITABLES
     */
    public Montant getRevenusBiensImmoNonHabitable() {
        return Montant.newAnnuel(tuple
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_BIENS_IMMO_NON_HABITABLES));
    }

    /**
     * CLE_DEPEN_FRAISIMM_INTERET_HYPOTHECAIRE
     */
    public Montant getDepensesFraisImmoInteretHypothequaire() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_INTERET_HYPOTHECAIRE));
    }

    /**
     * CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE
     */
    public Montant getDepensesFraisImmoEntretienImmeuble() {
        return Montant.newAnnuel(tuple
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE));
    }

    /**
     * CLE_DEPEN_FRAISIMM_TOTAL_PLAFONNE
     */
    public Montant getDepensesFraisImmoTotalPlafonne() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL_PLAFONNE));
    }

    /**
     * CLE_FORTU_FOR_IMMO_BIENS_IMMO_HABIT_PRINCIPALE
     */
    public Montant getFortuneImmoBiensHabitationPrincipale() {
        return new Montant(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_IMMO_HABIT_PRINCIPALE));
    }

    /**
     * CLE_FORTU_FOR_IMMO_DEDUCTION_FOFAITAIRE
     */
    public Montant getFortuneImmoDeductionForfaitaire() {
        return new Montant(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_DEDUCTION_FOFAITAIRE));
    }

    /**
     * CLE_FORTU_FOR_IMMO_BIENS_PRINCIPAL_DEDUIT
     */
    public Montant getFortuneImmoBienPrincipalDeduit() {
        return new Montant(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_PRINCIPAL_DEDUIT));
    }

    /**
     * CLE_DEPEN_GR_LOYER_VALEUR_LOCATIVE_APP_HABITE
     */
    public Montant getDepensesLoyerValeurLocativeAppHabite() {
        return Montant.newAnnuel(tuple
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_VALEUR_LOCATIVE_APP_HABITE));
    }


    /**
     * CLE_REVEN_RENFORMO_VALEUR_LOCATIVE
     */
    public Montant getRevenuValeurLocativeAppHabite() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_VALEUR_LOCATIVE));
    }

    /**
     * CLE_INTER_HABITATION_PRINCIPALE
     */
    public Montant getRevenuValeurLocativeAppHabitePrincipale() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_VALEUR_LOCATIVE));
    }

    /**
     * CLE_REVEN_RENFORMO_TOTAL
     */
    public Montant getRevenuFortuneMobiliereTotal() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_TOTAL));
    }

    /**
     * CLE_REVEN_RENFORMO_DROIT_HABITATION
     */
    public Montant getRevenuDroitHabitation() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_DROIT_HABITATION));
    }

    /**
     * CLE_DEPEN_GR_LOYER_TOTAL
     */
    public Montant getDepensesLoyerTotal() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL));
    }

    /**
     * CLE_DEPEN_GR_LOYER_TOTAL_NON_PLAFONNE
     */
    public Montant getDepensesLoyerTotalNonPlafonne() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL_NON_PLAFONNE));
    }

    /**
     * CLE_DEPEN_GR_LOYER_PLAFOND
     */
    public Montant getLegendeDepensesLoyerPlafonne() {
        return Montant.newAnnuel(tuple.getLegendeEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_PLAFOND));
    }

    /**
     * CLE_REVEN_AUTREREV_IJ_LCA
     */
    public Montant getRevenusAutreIJLCA() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LCA));
    }

    /**
     * CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE
     */
    public Montant getRevenusActiviteLucrativeDependante() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE));
    }

    /**
     * CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE
     */
    public Montant getRevenusActiviteLucrativeIndependante() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE));
    }

    /**
     * CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE
     */
    public Montant getRevenusActiviteLucrativeIndependanteAgricole() {
        return Montant.newAnnuel(tuple
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE));
    }

    /**
     * CLE_REVEN_RENAUTRE_RENTE_LPP
     */
    public Montant getRevenusRenteAutreRenteLPP() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_LPP));
    }

    /**
     * CLE_REVEN_RENAUTRE_RENTE_ETRANGERE
     */
    public Montant getRevenusRenteAutreRenteEtrangere() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_ETRANGERE));
    }

    /**
     * CLE_REVEN_RENAUTRE_RENTE_ETRANGERE_TAUX_CHANGE
     *
     * @return
     */
    public TupleDonneeRapport getTauxAutresRentesEtranger() {
        TupleDonneeRapport tauxRentesEtranger = new TupleDonneeRapport();
        if (tuple.getEnfants().containsKey(IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_ETRANGERE_TAUX_CHANGE)) {
            tauxRentesEtranger = tuple.getEnfants().get(
                    IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_ETRANGERE_TAUX_CHANGE);
        }
        return tauxRentesEtranger;
    }

    /**
     * CLE_REVEN_ACT_LUCR_REVENU_HYPOTHETIQUE
     */
    public Montant getRevenusActiviteLucrativeRevenuHypothetique() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_HYPOTHETIQUE));
    }

    /**
     * CLE_DEPEN_GR_LOYER_TAXES_PENSION_RECONNUE
     */
    public Montant getDepensesLoyerTaxesPensionReconnue() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TAXES_PENSION_RECONNUE));
    }

    /**
     * CLE_REVEN_RENAVSAI_TOTAL
     */
    public Montant getRevenusRenteAvsAiTotal() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENAVSAI_TOTAL));
    }

    /**
     * CLE_REVEN_ALLOCAPI_AVS_AI
     */
    public Montant getRevenusAPIAVSAI() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_AVS_AI));
    }

    /**
     * CLE_REVEN_AUTREREV_IJ_CHOMAGE
     */
    public Montant getRevenusAutreIJChomage() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_CHOMAGE));
    }

    /**
     * CLE_REVEN_AUTREREV_IJ_LAA
     */
    public Montant getRevenusAutreIJLAA() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LAA));
    }

    /**
     * CLE_REVEN_AUTREREV_IJ_LAM
     */
    public Montant getRevenusAutreIJLAM() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LAM));
    }

    /**
     * CLE_REVEN_AUTREREV_IJ_LAMAL
     */
    public Montant getRevenusAutreIJLamal() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LAMAL));
    }

    /**
     * CLE_REVEN_AUTREREV_IJAI
     */
    public Montant getRevenusAutreIJAI() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJAI));
    }

    /**
     * CLE_DEPEN_DEPPERSO_TOTAL
     */
    public Montant getDepensesPersonnellesTotal() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_DEPPERSO_TOTAL));
    }
    /**
     * CLE_INTER_LOYER_MONTANT_BRUT
     */
    public Montant getLoyerMontantBrut() {
        return getLoyerMontant(IPCValeursPlanCalcul.CLE_INTER_LOYER_MONTANT_BRUT, IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_LOYER_BRUT);
    }

    /**
     * CLE_INTER_LOYER_MONTANT_NET
     */
    public Montant getLoyerMontantNet() {
        return getLoyerMontant(IPCValeursPlanCalcul.CLE_INTER_LOYER_MONTANT_NET, IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_LOYER_NET);
    }

    /**
     * 64039077
     */
    public Montant getLoyerCharge() {
        return getLoyerMontant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_ACCOMPTE_CHARGES, IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_ACCOMPTE_CHARGES);
    }

    /**
     * CLE_INTER_LOYER_IS_FAUTEUIL_ROULANT
     */
    public Montant getLoyerFauteuilRoulant() {
        TupleDonneeRapport tupleLoyers = tuple.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_LOYERS);
        if (tupleLoyers != null) {
            Montant valeur = Montant.ZERO;
            for (TupleDonneeRapport tupleLoyer : tupleLoyers.getEnfants().values()) {
                valeur = valeur.add(Montant.newAnnuel(tupleLoyer.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_LOYER_IS_FAUTEUIL_ROULANT)));
            }
            return valeur;
        }
        return Montant.ZERO;
    }

    /**
     * 64039083
     */
    public Montant getLoyerFraisDeChauffage() {
        return getLoyerMontant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_FRAIS_CHAUFFAGE, IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_FRAIS_CHAUFFAGE);
    }

    /**
     * LOYER_TAXE_JOURNALIERE_PENSION_NON_RECONNUE
     */
    public Montant getLoyerPensionNonReconnue() {
        return getLoyerMontant(IPCValeursPlanCalcul.CLE_INTER_LOYER_TAXE_JOURNALIERE_PENSION_NON_RECONNUE, IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TAXES_PENSION_NON_RECONNUE);
    }


    private Montant getLoyerMontant(String type, String typeCompare) {
        // si les montants au prorata sont à zéro ne pas prendre en compte le loyer
        if(Montant.newAnnuel(tuple.getValeurEnfant(typeCompare)).isZero()) {
           return Montant.ZERO;
        }

        // récupération des valeurs avant du tuple LOYER -> valeurs avant prorata
        TupleDonneeRapport tupleLoyers = tuple.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_LOYERS);
        if (tupleLoyers != null) {
            Montant valeur = Montant.ZERO;
            for (TupleDonneeRapport tupleLoyer : tupleLoyers.getEnfants().values()) {
                valeur = valeur.add(Montant.newAnnuel(tupleLoyer.getValeurEnfant(type)));
            }
            return valeur;
        }
        return Montant.ZERO;
    }

    /**
     * LOYER_TAXE_JOURNALIERE_PENSION_NON_RECONNUE
     */

    public Montant getLoyerValeurLocativeAppHabite() {
        TupleDonneeRapport tupleHabitat = tuple.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_HABITATION_PRINCIPALE);
        if(tupleHabitat != null){
            Montant valeur = Montant.newAnnuel(tupleHabitat.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_VALEUR_LOCATIVE_APP_HABITE));
            Montant valeurLoc = Montant.newAnnuel(tuple
                    .getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_VALEUR_LOCATIVE_APP_HABITE));
            Montant charges = Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_CHARGES_FORFAITAIRES));
            // si les montants au prorata sont à zéro ne pas prendre en compte le loyer
            if(valeurLoc.isZero() && charges.isZero()){
                return Montant.ZERO;
            }
            // les charges de la valeur locative ne sont pas enregistrés ! Obligation de calculer avec prorata inverse
            if(!valeurLoc.substract(valeur).isZero()) {
                return valeur.add(calcCharge(valeurLoc, valeur, charges));
            }
            return valeur.add(charges);
        }
        return Montant.ZERO;
    }

    private Montant calcCharge(Montant valeurLoc, Montant valeurLocTot, Montant charges) {
        return !valeurLoc.isZero() ? charges.multiply(valeurLocTot).divide(valeurLoc) : charges;
    }

    /**
     * CLE_INTER_LOYER_MONTANT_NET
     */
    public Montant getDepenseGroupeLoyerTotal() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL));
    }

    /**
     * CLE_TOTAL_CC_DEDUIT
     */
    public Montant getTotalCalculDeduit() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_CC_DEDUIT));
    }

    /**
     * CLE_TOTAL_CC_STATUS_FEDERAL
     */
    public PcaEtatCalcul getEtatCalculFederal() {
        String etatCalculFederal = tuple.getLegendeEnfant(IPCValeursPlanCalcul.CLE_TOTAL_CC_STATUS_FEDERAL);
        if (etatCalculFederal != null) {
            return PcaEtatCalcul.fromValue(etatCalculFederal);
        }
        return PcaEtatCalcul.fromValue(tuple.getLegendeEnfant(IPCValeursPlanCalcul.CLE_TOTAL_CC_STATUS));
    }

    /**
     * CLE_DEPEN_GR_LOYER_PLAFOND_FEDERAL
     */
    public Montant getPlafondFederal() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_PLAFOND_FEDERAL));
    }

    /**
     * Mensuel cantonal Anualisé
     */
    public Montant getPartCantonaleAnnuelle() {
        return Montant.newMensuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_PART_CANTONALE))
                .annualise();
    }

    /**
     * CLE_TOTAL_TYPE_SEPARATION_CC
     */
    public String getTypeSeparation() {
        return tuple.getLegendeEnfant(IPCValeursPlanCalcul.CLE_TOTAL_TYPE_SEPARATION_CC);
    }

    /**
     * CLE_DEPEN_GR_LOYER_CHARGES_FORFAITAIRES
     */
    public Montant getDepensesLoyerChargesForfaitaires() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_CHARGES_FORFAITAIRES));
    }

    /**
     * CLE_DEPEN_GR_LOYER_DROIT_HABITATION
     */
    public Montant getDepensesLoyerDroitHabitation() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_DROIT_HABITATION));
    }

    /**
     * CLE_DEPEN_GR_LOYER_FRAIS_CHAUFFAGE
     */
    public Montant getDepensesLoyerFraisDeChauffage() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_FRAIS_CHAUFFAGE));
    }

    /**
     * CLE_DEPEN_GR_LOYER_TAXES_PENSION_NON_RECONNUE
     */
    public Montant getDepensesLoyerPensionNonReconnue() {
        return Montant.newAnnuel(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TAXES_PENSION_NON_RECONNUE));
    }


    public boolean isHomeDroitHabitation() {
        String sommeHomes = tuple.getLegendeEnfant(IPCValeursPlanCalcul.CLE_DEPEN_TAXEHOME_TOTAL);
        TupleDonneeRapport tupleHabitat = tuple.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_HABITATION_PRINCIPALE);
        if (tupleHabitat != null) {
            return (!"null".equals(sommeHomes)) && tupleHabitat.getLegende().equals(IPCBienImmoPrincipal.CS_TYPE_DROIT_HABITATION);
        }
        return false;
    }

    /**
     * CLE_REGION
     */
    public EPCRegionLoyer getLoyerRegion() {
        String regionLoyer = null;
        TupleDonneeRapport tupleHab = tuple.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_HABITATION_PRINCIPALE);
        if(tupleHab != null) {
            regionLoyer = tupleHab.getLegendeEnfant(IPCValeursPlanCalcul.PLAFOND_LOYER_ZONE);
            if(regionLoyer != null){
                return EPCRegionLoyer.fromValue(regionLoyer);
            }
        }
        TupleDonneeRapport tupleLoyers = tuple.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_LOYERS);
        if (tupleLoyers != null) {
            for (TupleDonneeRapport tupleLoyer : tupleLoyers.getEnfants().values()) {
                if(IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(tupleLoyer.getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_LOYER_ROLE_PROPRIETAIRE))) {
                    regionLoyer = tupleLoyer.getLegendeEnfant(IPCValeursPlanCalcul.PLAFOND_LOYER_ZONE);
                }
            }
            if (regionLoyer == null) {
                for (TupleDonneeRapport tupleLoyer : tupleLoyers.getEnfants().values()) {
                    if(IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(tupleLoyer.getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_LOYER_ROLE_PROPRIETAIRE))) {
                        regionLoyer = tupleLoyer.getLegendeEnfant(IPCValeursPlanCalcul.PLAFOND_LOYER_ZONE);
                    }
                }
            }
        }
        if(regionLoyer == null) {
            regionLoyer = tuple.getLegendeEnfant(IPCValeursPlanCalcul.PLAFOND_LOYER_ZONE);
        }

        return EPCRegionLoyer.fromValue(regionLoyer);
    }


    /**
     * CLE_IS_DIVESTED_WEALTH
     */
    public boolean isDivestedWealth() {
        return tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_IS_DIVESTED_WEALTH).toString().equals("1.0");
    }

    /**
     * CLE_TYPE_DIVESTED_WEALTH
     */
    public String getTypeDivestedWealth() {
        String typeDivestedWealth = String.valueOf(tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_TYPE_DIVESTED_WEALTH));
        return typeDivestedWealth.substring(0, typeDivestedWealth.indexOf("."));
    }

    public boolean isWheelchairSurcharge() {
        return tuple.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_LOYER_IS_FAUTEUIL_ROULANT).toString().equals("1.0");
    }

    public String getRequestDateofReceipt() {
        return "";
//        return tuple.getValeurEnfant(IPCValeursPlanCalcul.DATE)
    }
}
