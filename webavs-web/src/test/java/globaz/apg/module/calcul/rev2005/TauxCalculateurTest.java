package globaz.apg.module.calcul.rev2005;

import ch.globaz.common.domaine.Montant;
import globaz.apg.module.calcul.APSituationProfessionnelleHelper;
import globaz.framework.util.FWCurrency;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class TauxCalculateurTest {

    @Test
    public void calculerTaux_avecDeuxSituationJourIndemnise5Et15_taux025(){
        List<APSituationProfessionnelleHelper> list = Arrays.asList(
                creerSiuationProfessionnelle(5, 100.0),
                creerSiuationProfessionnelle(15, 100.0));
        assertThat(AAPModuleCalculSalaireJournalier.TauxCalculateur.of(list).getMontantJournalierTotal()).isEqualTo(new Montant(200));
        assertThat(AAPModuleCalculSalaireJournalier.TauxCalculateur.of(list).calculTaux(list.get(0), true).doubleValue()).isEqualTo(0.25);
        assertThat(AAPModuleCalculSalaireJournalier.TauxCalculateur.of(list).calculTaux(list.get(1), true).doubleValue()).isEqualTo(0.75);
    }

    @Test
    public void calculerTaux_avecDeuxSituationJourIndemnise10Et10salaires100et300_taux025(){
        List<APSituationProfessionnelleHelper> list = Arrays.asList(
                creerSiuationProfessionnelle(10, 100.0),
                creerSiuationProfessionnelle(10, 300.0));
        assertThat(AAPModuleCalculSalaireJournalier.TauxCalculateur.of(list).getMontantJournalierTotal()).isEqualTo(new Montant(400));
        assertThat(AAPModuleCalculSalaireJournalier.TauxCalculateur.of(list).calculTaux(list.get(0), true).doubleValue()).isEqualTo(0.25);
        assertThat(AAPModuleCalculSalaireJournalier.TauxCalculateur.of(list).calculTaux(list.get(1), true).doubleValue()).isEqualTo(0.75);
    }

    @Test
    public void calculerTaux_avecDeuxSituationJourIndemnise5Et15salaires300et100_taux050(){
        List<APSituationProfessionnelleHelper> list = Arrays.asList(
                creerSiuationProfessionnelle(5, 300.0),
                creerSiuationProfessionnelle(15, 100.0));
        assertThat(AAPModuleCalculSalaireJournalier.TauxCalculateur.of(list).getMontantJournalierTotal()).isEqualTo(new Montant(400));
        assertThat(AAPModuleCalculSalaireJournalier.TauxCalculateur.of(list).calculTaux(list.get(0), true).doubleValue()).isEqualTo(0.5);
        assertThat(AAPModuleCalculSalaireJournalier.TauxCalculateur.of(list).calculTaux(list.get(1), true).doubleValue()).isEqualTo(0.5);
    }

    @Test
    public void calculerTaux_avecUneSituationJourIndemnise_taux100(){
        List<APSituationProfessionnelleHelper> list = Arrays.asList(
                creerSiuationProfessionnelle(5, 300.0));
        assertThat(AAPModuleCalculSalaireJournalier.TauxCalculateur.of(list).getMontantJournalierTotal()).isEqualTo(new Montant(300));
        assertThat(AAPModuleCalculSalaireJournalier.TauxCalculateur.of(list).calculTaux(list.get(0), true).doubleValue()).isEqualTo(1);
    }

    @Test
    public void calculerTaux_avecUneSituationJourIndemniseA0_taux100(){
        List<APSituationProfessionnelleHelper> list = Arrays.asList(
                creerSiuationProfessionnelle(0, 300.0));
        assertThat(AAPModuleCalculSalaireJournalier.TauxCalculateur.of(list).calculTaux(list.get(0), false).doubleValue()).isEqualTo(1);
    }

    @Test
    public void calculerTaux_avecUneSituationJourIndemniseNull_taux100(){
        List<APSituationProfessionnelleHelper> list = Arrays.asList(
                creerSiuationProfessionnelle(null, 300.0));
        assertThat(AAPModuleCalculSalaireJournalier.TauxCalculateur.of(list).calculTaux(list.get(0), false).doubleValue()).isEqualTo(1);
    }


    private APSituationProfessionnelleHelper creerSiuationProfessionnelle(Integer nbJour, Double salaireJournalier) {
        APSituationProfessionnelleHelper sit = new APSituationProfessionnelleHelper().setNbJourIndemnise(nbJour);
        sit.setRevenuMoyenDeterminantSansArrondi(new FWCurrency(salaireJournalier));
        sit.setSalaireJournalier(new FWCurrency(salaireJournalier));
        return sit;
    }


}