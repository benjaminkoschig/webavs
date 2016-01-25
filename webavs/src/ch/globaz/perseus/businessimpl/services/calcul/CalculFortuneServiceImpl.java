/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.calcul;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Calendar;
import ch.globaz.perseus.business.calcul.InputCalcul;
import ch.globaz.perseus.business.calcul.OutputCalcul;
import ch.globaz.perseus.business.calcul.OutputData;
import ch.globaz.perseus.business.constantes.CSTypeGarde;
import ch.globaz.perseus.business.constantes.CSVariableMetier;
import ch.globaz.perseus.business.exceptions.calcul.CalculException;
import ch.globaz.perseus.business.models.donneesfinancieres.DetteType;
import ch.globaz.perseus.business.models.donneesfinancieres.Fortune;
import ch.globaz.perseus.business.models.donneesfinancieres.FortuneType;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.services.calcul.CalculFortuneService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * @author DDE
 * 
 */
public class CalculFortuneServiceImpl extends PerseusAbstractServiceImpl implements CalculFortuneService {

    private OutputCalcul calculerDettes(InputCalcul inputCalcul, OutputCalcul outputCalcul) throws CalculException {

        Float a3 = new Float(0);
        a3 += inputCalcul.getDonneesFinancieresRegroupees().getElementDette(DetteType.DETTES_HYPOTHECAIRES).getValeur();
        a3 = this.roundFloat(a3);
        outputCalcul.addDonnee(OutputData.DETTE_HYPOTHECAIRES, a3);

        Float a4 = new Float(0);
        a4 += inputCalcul.getDonneesFinancieresRegroupees().getElementDette(DetteType.AUTRES_DETTES).getValeur();
        a4 = this.roundFloat(a4);
        outputCalcul.addDonnee(OutputData.DETTE_AUTRES_DETTES, a4);

        Float a5 = new Float(0);
        // En fonction de si le parent est seul ou s'il s'agit d'un couple
        if (JadeStringUtil.isEmpty(inputCalcul.getDemande().getSituationFamiliale().getConjoint().getId())) {
            a5 += inputCalcul.getVariableMetier(CSVariableMetier.FORTUNE_DEPASSANT_PARENT_SEUL).getMontant();
        } else {
            a5 += inputCalcul.getVariableMetier(CSVariableMetier.FORTUNE_DEPASSANT_COUPLE).getMontant();
        }
        a5 = this.roundFloat(a5);
        outputCalcul.addDonnee(OutputData.DETTE_DEDUCTION_LEGALE, a5);

        return outputCalcul;
    }

    private OutputCalcul calculerFortuneEnfants(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {

        // Données à calculer
        Float fortuneEnfants = new Float(0);

        // Récupération des données financières
        // Reprendre la fortune pour chacun des enfants
        for (EnfantFamille ef : inputCalcul.getListeEnfants()) {
            if (CSTypeGarde.GARDE_PARTAGEE.getCodeSystem().equals(ef.getSimpleEnfantFamille().getCsGarde())) {
                fortuneEnfants += inputCalcul
                        .getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                        .getElementFortune(FortuneType.FORTUNE_ENFANT).getValeur() / 2;
            } else {
                fortuneEnfants += inputCalcul
                        .getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                        .getElementFortune(FortuneType.FORTUNE_ENFANT).getValeur();
            }
        }

        fortuneEnfants = this.roundFloat(fortuneEnfants);
        outputCalcul.addDonnee(OutputData.FORTUNE_ENFANTS, fortuneEnfants);

        return outputCalcul;
    }

    private OutputCalcul calculerFortuneImmobiliere(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {

        // Récupération des données financières
        Float immeubleHabite = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementFortune(FortuneType.IMMEUBLE_HABITE).getValeur();
        immeubleHabite = this.roundFloat(immeubleHabite);
        Float biensEtrangers = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementFortune(FortuneType.BIENS_ETRANGERS).getValeur();
        biensEtrangers = this.roundFloat(biensEtrangers);
        Float autresImmeubles = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementFortune(FortuneType.AUTRES_IMMEUBLES).getValeur();
        autresImmeubles = this.roundFloat(autresImmeubles);

        // Champ à calculer
        Float immeubleHabiteDeduc = new Float(0);
        Float immeubleHabiteModif = new Float(0);
        Float fortuneImmobiliere = new Float(0);

        // Calcul de la valeur de l'immeuble habité
        immeubleHabiteDeduc = inputCalcul.getVariableMetier(CSVariableMetier.VALEUR_IMMEUBLE_DEPASSANT).getMontant();
        if (immeubleHabite > immeubleHabiteDeduc) {
            immeubleHabiteModif = immeubleHabite - immeubleHabiteDeduc;
        } else {
            immeubleHabiteModif = new Float(0);
        }

        // Calcul de la fortune mobilière
        fortuneImmobiliere += immeubleHabiteModif;
        fortuneImmobiliere += autresImmeubles;
        fortuneImmobiliere += biensEtrangers;

        // Enregistrement des résultats du calcul
        outputCalcul.addDonnee(OutputData.FORTUNE_IMMEUBLE_HABITE, immeubleHabite);
        outputCalcul.addDonnee(OutputData.FORTUNE_IMMEUBLE_HABITE_DEDUC, immeubleHabiteDeduc);
        outputCalcul.addDonnee(OutputData.FORTUNE_IMMEUBLE_HABITE_MODIF, immeubleHabiteModif);
        outputCalcul.addDonnee(OutputData.FORTUNE_BIENS_ETRANGERS, biensEtrangers);
        outputCalcul.addDonnee(OutputData.FORTUNE_AUTRES_IMMEUBLES, autresImmeubles);
        outputCalcul.addDonnee(OutputData.FORTUNE_IMMOBILIERE, fortuneImmobiliere);

        return outputCalcul;
    }

    private OutputCalcul calculerFortuneMobiliere(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {
        // Reprises des champs depuis les données financières
        Float liquidite = inputCalcul.getDonneesFinancieresRegroupees().getElementFortune(FortuneType.LIQUIDITE)
                .getValeur();
        liquidite = this.roundFloat(liquidite);
        Float rachatAssuranceVie = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementFortune(FortuneType.RACHAT_ASSURANCE_VIE).getValeur();
        rachatAssuranceVie = this.roundFloat(rachatAssuranceVie);
        Float hoirie = inputCalcul.getDonneesFinancieresRegroupees().getElementFortune(FortuneType.HOIRIE).getValeur();
        hoirie = this.roundFloat(hoirie);
        Float cessionRequerant = inputCalcul.getDonneesFinancieresRequerant().getElementFortune(FortuneType.CESSION)
                .getValeur();
        cessionRequerant = this.roundFloat(cessionRequerant);
        Float cessionConjoint = inputCalcul.getDonneesFinancieresConjoint().getElementFortune(FortuneType.CESSION)
                .getValeur();
        cessionConjoint = this.roundFloat(cessionConjoint);
        Float autreBien = inputCalcul.getDonneesFinancieresRegroupees().getElementFortune(FortuneType.AUTRE_BIEN)
                .getValeur();
        autreBien = this.roundFloat(autreBien);

        // Définition des champs qui devront être calculés
        Float fortuneMobiliere = new Float(0);
        Float cessionDeducRequerant = new Float(0);
        Float cessionModifRequerant = new Float(0);
        Float cessionDeducConjoint = new Float(0);
        Float cessionModifConjoint = new Float(0);

        // Calcul de la cession du requérant
        if (cessionRequerant > 0) {
            Fortune cessionElement = inputCalcul.getDonneesFinancieresRequerant()
                    .getElementFortune(FortuneType.CESSION);
            int anneeActuelle = Calendar.getInstance().get(Calendar.YEAR);
            int anneeCession = JadeDateUtil.getGlobazCalendar(cessionElement.getDateCession()).get(Calendar.YEAR);
            int nbAnneesDeduc = anneeActuelle - anneeCession - 1;
            if (nbAnneesDeduc < 0) {
                nbAnneesDeduc = 0;
            }

            cessionDeducRequerant = nbAnneesDeduc
                    * inputCalcul.getVariableMetier(CSVariableMetier.DEDUCTION_ANNUELLE_CESSION).getMontant();
            cessionDeducRequerant = this.roundFloat(cessionDeducRequerant);
            if (cessionRequerant > cessionDeducRequerant) {
                cessionModifRequerant = cessionRequerant - cessionDeducRequerant;
            } else {
                cessionModifRequerant = new Float(0);
            }
        }
        // Calcul de la cession du conjoint
        if (cessionConjoint > 0) {
            Fortune cessionElement = inputCalcul.getDonneesFinancieresConjoint().getElementFortune(FortuneType.CESSION);
            int anneeActuelle = Calendar.getInstance().get(Calendar.YEAR);
            int anneeCession = JadeDateUtil.getGlobazCalendar(cessionElement.getDateCession()).get(Calendar.YEAR);
            int nbAnneesDeduc = anneeActuelle - anneeCession - 1;
            if (nbAnneesDeduc < 0) {
                nbAnneesDeduc = 0;
            }

            cessionDeducConjoint = nbAnneesDeduc
                    * inputCalcul.getVariableMetier(CSVariableMetier.DEDUCTION_ANNUELLE_CESSION).getMontant();
            cessionDeducConjoint = this.roundFloat(cessionDeducConjoint);
            if (cessionConjoint > cessionDeducConjoint) {
                cessionModifConjoint = cessionConjoint - cessionDeducConjoint;
            } else {
                cessionModifConjoint = new Float(0);
            }
        }

        // calcul de la fortune mobilière
        fortuneMobiliere += liquidite;
        fortuneMobiliere += rachatAssuranceVie;
        fortuneMobiliere += hoirie;
        fortuneMobiliere += cessionModifRequerant;
        fortuneMobiliere += cessionModifConjoint;
        fortuneMobiliere += autreBien;

        // Enregistrement dans le calcul de sortie
        outputCalcul.addDonnee(OutputData.FORTUNE_LIQUIDITE, liquidite);
        outputCalcul.addDonnee(OutputData.FORTUNE_RACHAT_ASSURANCE_VIE, rachatAssuranceVie);
        outputCalcul.addDonnee(OutputData.FORTUNE_HOIRIE, hoirie);
        outputCalcul.addDonnee(OutputData.FORTUNE_CESSION_REQUERANT, cessionRequerant);
        outputCalcul.addDonnee(OutputData.FORTUNE_CESSION_DEDUC_REQUERANT, cessionDeducRequerant);
        outputCalcul.addDonnee(OutputData.FORTUNE_CESSION_MODIF_REQUERANT, cessionModifRequerant);
        outputCalcul.addDonnee(OutputData.FORTUNE_CESSION_CONJOINT, cessionConjoint);
        outputCalcul.addDonnee(OutputData.FORTUNE_CESSION_DEDUC_CONJOINT, cessionDeducConjoint);
        outputCalcul.addDonnee(OutputData.FORTUNE_CESSION_MODIF_CONJOINT, cessionModifConjoint);
        outputCalcul.addDonnee(OutputData.FORTUNE_AUTRE_BIEN, autreBien);

        outputCalcul.addDonnee(OutputData.FORTUNE_MOBILIERE, fortuneMobiliere);

        return outputCalcul;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.calcul.CalculFortuneService#calculerFortuneNet(ch.globaz.perseus.business
     * .calcul.InputCalcul, ch.globaz.perseus.business.calcul.OutputCalcul)
     */
    @Override
    public OutputCalcul calculerFortuneNette(InputCalcul inputCalcul, OutputCalcul outputCalcul) throws CalculException {

        outputCalcul = calculerFortuneMobiliere(inputCalcul, outputCalcul);
        outputCalcul = calculerFortuneImmobiliere(inputCalcul, outputCalcul);
        outputCalcul = calculerFortuneEnfants(inputCalcul, outputCalcul);
        outputCalcul = calculerDettes(inputCalcul, outputCalcul);

        Float fortuneNette = new Float(0);
        fortuneNette += outputCalcul.getDonnee(OutputData.FORTUNE_MOBILIERE);
        fortuneNette += outputCalcul.getDonnee(OutputData.FORTUNE_IMMOBILIERE);
        fortuneNette += outputCalcul.getDonnee(OutputData.FORTUNE_ENFANTS);
        fortuneNette -= outputCalcul.getDonnee(OutputData.DETTE_HYPOTHECAIRES);
        fortuneNette -= outputCalcul.getDonnee(OutputData.DETTE_AUTRES_DETTES);
        fortuneNette -= outputCalcul.getDonnee(OutputData.DETTE_DEDUCTION_LEGALE);

        if (fortuneNette < 0) {
            fortuneNette = new Float(0);
        }

        outputCalcul.addDonnee(OutputData.FORTUNE_NETTE, fortuneNette);

        return outputCalcul;
    }

}
