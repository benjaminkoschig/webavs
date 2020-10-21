package ch.globaz.pegasus.business.domaine.parametre.forfaitPrime;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;

public class ForfaitsPrimeAssuranceMaladieTest {

    @Test
    public void testComputeAge() throws Exception {
        ForfaitsPrimeAssuranceMaladie forfaitsPrimeAssuranceMaladie = new ForfaitsPrimeAssuranceMaladie();

        ForfaitPrimeAssuranceMaladie forfaitPrimeAssuranceMaladie = new ForfaitPrimeAssuranceMaladie();

        assertThat(forfaitsPrimeAssuranceMaladie.computeAge(new Date("10.01.2017"), new Date("10.01.2010"))).isEqualTo(
                7);

        assertThat(forfaitsPrimeAssuranceMaladie.computeAge(new Date("9.01.2011"), new Date("10.01.2010")))
                .isEqualTo(1);

        assertThat(forfaitsPrimeAssuranceMaladie.computeAge(new Date("10.01.2012"), new Date("10.01.2010"))).isEqualTo(
                2);

        assertThat(forfaitsPrimeAssuranceMaladie.computeAge(new Date("10.01.2011"), new Date("10.01.2010"))).isEqualTo(
                1);

    }

    @Test
    public void testResolveTypePrimeEnfant() throws Exception {
        ForfaitsPrimeAssuranceMaladie forfaitsPrimeAssuranceMaladie = new ForfaitsPrimeAssuranceMaladie();
        assertThat(forfaitsPrimeAssuranceMaladie.resolveTypePrime(5)).isEqualTo(TypePrime.ENFANT);
        assertThat(forfaitsPrimeAssuranceMaladie.resolveTypePrime(18)).isEqualTo(TypePrime.ENFANT);
    }

    @Test
    public void testResolveTypePrimeJeunneAdulte() throws Exception {
        ForfaitsPrimeAssuranceMaladie forfaitsPrimeAssuranceMaladie = new ForfaitsPrimeAssuranceMaladie();
        assertThat(forfaitsPrimeAssuranceMaladie.resolveTypePrime(19)).isEqualTo(TypePrime.JEUNE_ADULTE);
        assertThat(forfaitsPrimeAssuranceMaladie.resolveTypePrime(25)).isEqualTo(TypePrime.JEUNE_ADULTE);

    }

    @Test
    public void testResolveTypePrimAdulte() throws Exception {
        ForfaitsPrimeAssuranceMaladie forfaitsPrimeAssuranceMaladie = new ForfaitsPrimeAssuranceMaladie();
        assertThat(forfaitsPrimeAssuranceMaladie.resolveTypePrime(26)).isEqualTo(TypePrime.ADULTE);
        assertThat(forfaitsPrimeAssuranceMaladie.resolveTypePrime(90)).isEqualTo(TypePrime.ADULTE);
    }

    @Test
    public void testFiltreByAgeInt() throws Exception {
        ForfaitsPrimeAssuranceMaladie forfaits = new ForfaitsPrimeAssuranceMaladie();
        forfaits.add(build("01.01.2017", "31.12.2017", TypePrime.JEUNE_ADULTE, "1"));
        forfaits.add(build("01.01.2016", "31.12.2016", TypePrime.JEUNE_ADULTE, "2"));
        forfaits.add(build("01.01.2015", "31.12.2015", TypePrime.JEUNE_ADULTE, "3"));
        forfaits.add(build("01.01.2017", "31.12.2017", TypePrime.ADULTE, "4"));
        forfaits.add(build("01.01.2016", "31.12.2016", TypePrime.ADULTE, "5"));
        forfaits.add(build("01.01.2015", "31.12.2015", TypePrime.ADULTE, "6"));
        forfaits.add(build("01.01.2017", "31.12.2017", TypePrime.ENFANT, "7"));
        forfaits.add(build("01.01.2016", "31.12.2016", TypePrime.ENFANT, "8"));
        forfaits.add(build("01.01.2015", "31.12.2015", TypePrime.ENFANT, "9"));

        assertThat(forfaits.filtreByAge(30).resolveMostRecent().getId()).isEqualTo("4");
        assertThat(forfaits.filtreByAge(20).resolveMostRecent().getId()).isEqualTo("1");
        assertThat(forfaits.filtreByAge(5).resolveMostRecent().getId()).isEqualTo("7");
    }

    private ForfaitPrimeAssuranceMaladie build(String dateDebut, String dateFin, TypePrime typePrime, String id) {
        ForfaitPrimeAssuranceMaladie forfaitPrimeAssuranceMaladie = new ForfaitPrimeAssuranceMaladie();
        forfaitPrimeAssuranceMaladie.setDateDebut(new Date());
        forfaitPrimeAssuranceMaladie.setDateFin(new Date());
        forfaitPrimeAssuranceMaladie.setMontantPrimeMoy(new Montant(1000));
        forfaitPrimeAssuranceMaladie.setMontantPrimeReductionMaxCanton(new Montant(900));
        forfaitPrimeAssuranceMaladie.setType(typePrime);
        forfaitPrimeAssuranceMaladie.setId(id);
        return forfaitPrimeAssuranceMaladie;
    }

}
