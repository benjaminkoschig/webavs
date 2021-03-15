/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense;

import ch.globaz.pegasus.business.constantes.TypeDeplafonnement;
import globaz.jade.client.util.JadeNumericUtil;
import ch.globaz.pegasus.business.constantes.IPCHabitat;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.ChargesLoyer;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import org.apache.commons.lang.StringUtils;

/**
 * @author ECO
 * 
 */
public class StrategieLoyer extends StrategieCalculDepense {

    private static final float NB_MOIS = 12;

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense. StrategieCalculDepense
     * #calculeDepense(ch.globaz.pegasus.business.models.calcul.CalculDonneesCC,
     * ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext,
     * ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport)
     */
    @Override
    protected TupleDonneeRapport calculeDepense(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        // récupération des données
        String csTypeLoyer = donnee.getLoyerCsTypeLoyer();
        float loyerMontantMensuel = Float.parseFloat(donnee.getLoyerMontant());

        String csDeplafonnementAppartementPartage = donnee.getLoyerCsDeplafonnementAppartementPartage();

        float montantChargesMensuels = 0f;
        String chargeStr = donnee.getLoyerMontantCharges();
        if (JadeNumericUtil.isNumeric(chargeStr)) {
            montantChargesMensuels = Float.parseFloat(chargeStr);
        }

        Boolean isFauteuilRoulant = donnee.getLoyerIsFauteuilRoulant();
        if (isFauteuilRoulant == null) {
            isFauteuilRoulant = false;
        }
        Boolean isTenueMenage = donnee.getLoyerIsTenueMenage();
        if (isTenueMenage == null) {
            isTenueMenage = false;
        }

        if (isTenueMenage) {
            this.getOrCreateChild(resultatExistant,
                    IPCValeursPlanCalcul.CLE_INTER_IS_FORFAIT_REVENU_NATURE_TENUE_MENAGE, 1f);
        }

        // // calcul du montant de charge
        // float montantChargesAnnuels = 0f;
        // String cleDepenseMontantCharges = null;
        // if (IPCHabitat.CS_LOYER_NET_AVEC_CHARGE_FORFAITAIRES.equals(csTypeLoyer)) {
        // montantChargesAnnuels = Float.parseFloat(((ControlleurVariablesMetier) context
        // .get(Attribut.CS_FORFAIT_FRAIS_CHAUFFAGE)).getValeurCourante());
        // cleDepenseMontantCharges = IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_FRAIS_CHAUFFAGE;
        // } else if (IPCHabitat.CS_VALEUR_LOCATIVE_CHEZ_PROPRIETAIRE.equals(csTypeLoyer)) {
        // // verification integrité données
        // if (context.contains(Attribut.HAS_BIEN_IMMO_PRINCIPAL)) {
        // throw new CalculBusinessException("pegasus.calcul.habitat.integrity");
        // }
        // context.put(Attribut.HAS_HABITAT_CHEZ_PROPRIETAIRE, true);
        //
        // montantChargesAnnuels = Float.parseFloat(((ControlleurVariablesMetier) context
        // .get(Attribut.CS_FORFAIT_CHARGES)).getValeurCourante());
        // cleDepenseMontantCharges = IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_FRAIS_CHAUFFAGE;
        // } else if (IPCHabitat.CS_LOYER_NET_AVEC_CHARGE.equals(csTypeLoyer)) {
        // // annualise le montant des charges
        // montantChargesAnnuels = montantChargesMensuels * StrategieLoyer.NB_MOIS;
        // cleDepenseMontantCharges = IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_ACCOMPTE_CHARGES;
        // }

        // calcul du montant annuel
        float montantLoyerAnnualise = Math.round(loyerMontantMensuel * StrategieLoyer.NB_MOIS);

        // calcul de la taxe journalière de la pension non reconnue
        float montantTaxeJournalierePNReconnue = 0f;
        String montantTaxeJournalierePNReconnueStr = donnee.getLoyerTaxeJournalierePensionNonReconnue();
        int duree = (Integer) context.get(CalculContext.Attribut.DUREE_ANNEE);
        if (JadeNumericUtil.isNumeric(montantTaxeJournalierePNReconnueStr)) {
            float tauxPension = Float.parseFloat(((ControlleurVariablesMetier) context
                    .get(Attribut.TAUX_PENSION_NON_RECONNUE)).getValeurCourante());
            montantTaxeJournalierePNReconnue = Float.parseFloat(montantTaxeJournalierePNReconnueStr) * tauxPension
                    * duree;
        }

        // création des clés temporaires
        TupleDonneeRapport tupleLoyers = this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_INTER_LOYERS,
                0f);
        TupleDonneeRapport tupleLoyerActuel = this.getOrCreateChild(tupleLoyers, IPCValeursPlanCalcul.CLE_INTER_LOYERS_ID+donnee.getIdDonneeFinanciereHeader(),
                0f);
        if (IPCHabitat.CS_LOYER_BRUT_CHARGES_COMPRISES.equals(csTypeLoyer)) {
            this.getOrCreateChild(tupleLoyerActuel, IPCValeursPlanCalcul.CLE_INTER_LOYER_MONTANT_BRUT,
                    montantLoyerAnnualise);
        } else {
            // loyer déjà anualisé pour ce type
            if (IPCHabitat.CS_VALEUR_LOCATIVE_CHEZ_PROPRIETAIRE.equals(csTypeLoyer)) {
                montantLoyerAnnualise = loyerMontantMensuel;
            }
            this.getOrCreateChild(tupleLoyerActuel, IPCValeursPlanCalcul.CLE_INTER_LOYER_MONTANT_NET,
                    montantLoyerAnnualise);
        }

        this.getOrCreateChild(tupleLoyerActuel, IPCValeursPlanCalcul.CLE_INTER_LOYER_PROPRIETAIRE,
                donnee.getIdMembreFamilleSF());
        TupleDonneeRapport nodeRolePropLoyer = this.getOrCreateChild(tupleLoyerActuel,
                IPCValeursPlanCalcul.CLE_INTER_LOYER_ROLE_PROPRIETAIRE, donnee.getCsRoleFamille());
        nodeRolePropLoyer.setLegende(donnee.getCsRoleFamille());

        this.getOrCreateChild(tupleLoyerActuel, IPCValeursPlanCalcul.CLE_INTER_LOYER_NB_PERSONNES,
                donnee.getLoyerNbPersonnes());
        this.getOrCreateChild(tupleLoyerActuel,
                IPCValeursPlanCalcul.CLE_INTER_LOYER_TAXE_JOURNALIERE_PENSION_NON_RECONNUE,
                montantTaxeJournalierePNReconnue);
        if (isFauteuilRoulant) {
            this.getOrCreateChild(tupleLoyerActuel, IPCValeursPlanCalcul.CLE_INTER_LOYER_IS_FAUTEUIL_ROULANT, 1f);
        }

        // S160704_002 : déplafonnement appartement partagé
        if (StringUtils.isNotEmpty(csDeplafonnementAppartementPartage) && !StringUtils.equals(csDeplafonnementAppartementPartage, "0")) {
            String csDeplafonnement;
            if (context.contains(Attribut.REFORME)) {
                csDeplafonnement = TypeDeplafonnement.mapVariableMetierDeplafonnement.get(csDeplafonnementAppartementPartage);
            } else {
                csDeplafonnement = csDeplafonnementAppartementPartage;
            }
            TupleDonneeRapport nodeCsAppartementProtege = this.getOrCreateChild(tupleLoyerActuel, IPCValeursPlanCalcul.CLE_INTER_LOYER_CS_DEPLAFONNEMENT_APPARTEMENT_PARTAGE, csDeplafonnement);
            nodeCsAppartementProtege.setLegende(csDeplafonnement);
        } else {
            TupleDonneeRapport nodeCsAppartementProtege = this.getOrCreateChild(tupleLoyerActuel,
                    IPCValeursPlanCalcul.CLE_INTER_LOYER_CS_DEPLAFONNEMENT_APPARTEMENT_PARTAGE, "0");
            nodeCsAppartementProtege.setLegende("0");
        }

        // charges
        ChargesLoyer charges = getMontantChargesLoyer(csTypeLoyer, context, montantChargesMensuels);
        this.getOrCreateChild(tupleLoyerActuel, charges.getCleDepenseMontantCharges(), charges.getMontantCharges());

        // plafond des loyers réforme PC
        if(context.contains(Attribut.REFORME) && !tupleLoyerActuel.containsValeurEnfant(IPCValeursPlanCalcul.PLAFOND_LOYER_LOCALITE)) {
            this.getOrCreateChild(tupleLoyerActuel, IPCValeursPlanCalcul.PLAFOND_LOYER_LOCALITE, donnee.getIdLocalite());
            this.getOrCreateChild(tupleLoyerActuel, IPCValeursPlanCalcul.PLAFOND_LOYER_NBTOTALFAMILLE, donnee.getNbTotalFamille());
            this.getOrCreateChild(tupleLoyerActuel, IPCValeursPlanCalcul.PLAFOND_LOYER_DATEDEBUT, 0f).setLegende(donnee.getDateDebutDonneeFinanciere());
        }

        return resultatExistant;
    }

}
