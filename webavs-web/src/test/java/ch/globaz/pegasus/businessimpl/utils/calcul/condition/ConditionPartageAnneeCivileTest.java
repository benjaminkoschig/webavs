package ch.globaz.pegasus.businessimpl.utils.calcul.condition;

import static org.assertj.core.api.Assertions.*;
import java.util.Collection;
import org.junit.Test;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;

public class ConditionPartageAnneeCivileTest {

    @Test
    // On devrait pas avoir de date car le prochain paiement est durant la m�me ann�e
    public void testCalculateDatesMemeAnnee() throws Exception {
        String dateDebut = "01.05.2016";
        DonneesHorsDroitsProvider containerGlobal = DonneesHorsDroitsProvider.getInstance();
        containerGlobal.setDateProchainPaiement("01.06.2016");

        Collection<String> conditionDates = new ConditionPartageAnneeCivile().calculateDates(dateDebut, null,
                containerGlobal);
        assertThat(conditionDates).isEmpty();
    }

    @Test
    // La date doit �tre celle du d�but d'ann�e suivante l'ann�e de la date de d�but
    public void testCalculateDatesAnneeSuivante() throws Exception {
        String dateDebut = "01.05.2016";
        DonneesHorsDroitsProvider containerGlobal = DonneesHorsDroitsProvider.getInstance();
        containerGlobal.setDateProchainPaiement("01.02.2017");

        Collection<String> conditionDates = new ConditionPartageAnneeCivile().calculateDates(dateDebut, null,
                containerGlobal);

        assertThat(conditionDates).hasSize(1);
        assertThat(conditionDates).contains("01.01.2017");

    }

    @Test
    // Les dates doivent figur�s dans la liste des futures dates
    public void testCalculateDatesPlusieursAnneesSuivantes() throws Exception {
        String dateDebut = "01.05.2016";
        DonneesHorsDroitsProvider containerGlobal = DonneesHorsDroitsProvider.getInstance();
        containerGlobal.setDateProchainPaiement("01.01.2019");

        Collection<String> conditionDates = new ConditionPartageAnneeCivile().calculateDates(dateDebut, null,
                containerGlobal);
        assertThat(conditionDates).hasSize(3);
        assertThat(conditionDates).contains("01.01.2017", "01.01.2018", "01.01.2019");
    }

}
