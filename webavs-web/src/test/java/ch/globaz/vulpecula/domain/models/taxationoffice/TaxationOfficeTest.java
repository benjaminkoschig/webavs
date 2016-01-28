package ch.globaz.vulpecula.domain.models.taxationoffice;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;

public class TaxationOfficeTest {
    private TaxationOffice taxationOffice;

    @Before
    public void setUp() {
        taxationOffice = new TaxationOffice();
    }

    @Test
    public void getPeriodeDebutAsSwissValue_GivenEmptyTaxationOffice_ShouldBeNull() {
        assertNull(taxationOffice.getPeriodeDebutAsSwissValue());
    }

    @Test
    public void getEmployeurAffilieNumero_GivenEmptyTaxationOffice_ShouldBeNull() {
        assertNull(taxationOffice.getEmployeurAffilieNumero());
    }

    @Test
    public void getEmployeurAffilieNumero_GivenTaxationOfficeWithEmployeur01_0000_ShouldBe01_0000() {
        Decompte decompte = mock(Decompte.class);
        when(decompte.getEmployeurAffilieNumero()).thenReturn("01-0000");
        taxationOffice.setDecompte(decompte);

        assertEquals("01-0000", taxationOffice.getEmployeurAffilieNumero());
    }

    @Test
    public void getAdressePrincipaleFormatee_GivenTaxationOfficeWithoutDecompte_ShouldBeNull() {
        assertNull(taxationOffice.getAdressePrincipaleFormattee());
    }

    @Test
    public void getMontant_GivenEmptyTaxation_ShouldBe0() {
        assertEquals("0.00", taxationOffice.getMontant());
    }

    @Test
    public void getMontant_GivenTaxationWithEmptyLigne_ShouldBe0() {
        LigneTaxation ligneTaxation = new LigneTaxation();
        taxationOffice.add(ligneTaxation);
        assertEquals("0.00", taxationOffice.getMontant());
    }

    @Test
    public void getMontant_GivenTaxationWithLigneOf100_ShouldBe100() {
        LigneTaxation ligneTaxation = new LigneTaxation();
        ligneTaxation.setMontant(Montant.valueOf(100));
        taxationOffice.add(ligneTaxation);
        assertEquals("100.00", taxationOffice.getMontant());
    }

    @Test
    public void getMontant_GivenTaxationWithLigneOf100And150_ShouldBe250() {
        LigneTaxation ligneTaxation = new LigneTaxation();
        ligneTaxation.setMontant(Montant.valueOf(100));
        LigneTaxation ligneTaxation2 = new LigneTaxation();
        ligneTaxation2.setMontant(Montant.valueOf(150));

        taxationOffice.add(ligneTaxation);
        taxationOffice.add(ligneTaxation2);

        assertEquals("250.00", taxationOffice.getMontant());
    }

    @Test
    public void getTaux_GivenEmptyTaxation_ShouldBe0() {
        assertEquals(new Taux(0), taxationOffice.getTaux());
    }

    @Test
    public void getTaux_GivenTaxationWithTauxOf5And15_ShouldBe20() {
        LigneTaxation ligneTaxation = new LigneTaxation();
        ligneTaxation.setTaux(new Taux(5));
        LigneTaxation ligneTaxation2 = new LigneTaxation();
        ligneTaxation2.setTaux(new Taux(15));

        taxationOffice.add(ligneTaxation);
        taxationOffice.add(ligneTaxation2);

        assertEquals(new Taux(20), taxationOffice.getTaux());
    }

    @Test
    public void valide_GivenEmptyTaxation_ShouldNotChanged() {
        taxationOffice.setEtat(EtatTaxation.SAISI);
        taxationOffice.valide();

        assertEquals(EtatTaxation.SAISI, taxationOffice.getEtat());
    }

    @Test
    public void valide_GivenTaxationValideAndIdPassageFacturationOf20_ShouldChangedOnValide() {
        taxationOffice.setEtat(EtatTaxation.SAISI);
        taxationOffice.setIdPassageFacturation("20");
        taxationOffice.valide();

        assertEquals(EtatTaxation.VALIDE, taxationOffice.getEtat());
    }
}
