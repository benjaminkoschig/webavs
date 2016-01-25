package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.allocationFamilliale.AllocationFamilliale;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.autreApi.AutreApi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.avsAi.ApiAvsAi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.assuranceRenteViagere.AssuranceRenteViagere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.assuranceVie.AssuranceVie;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreDetteProuvee.AutreDetteProuvee;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreFortuneMobiliere.AutreFortuneMobiliere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente.AutreRente;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.betail.Betail;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonHabitable.BienImmobilierNonHabitable;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonPrincipale.BienImmobilierNonPrincipale;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierServantHbitationPrincipale.BienImmobilierServantHabitationPrincipale;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.capitalLpp.CapitalLpp;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.compteBancairePostal.CompteBancairePostal;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.contratEntretienViager.ContratEntretienViager;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.cotisationPsal.CotisationPsal;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.dessaisissementFortune.DessaisissementFortune;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.dessaisissementRevenu.DessaisissementRevenu;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.iJAi.IjAi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.indeminteJournaliereApg.IndemniteJournaliereApg;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.loyer1.Loyer;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.marchandiseStock.MarchandiseStock;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.numeraire.Numeraire;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.pensionAlimentaire.PensionAlimentaire;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.pretEnversTiers.PretEnversTiers;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.renteAvsAi.RenteAvsAi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.revenuActiviteLucrativeIndependante.RevenuActiviteLucrativeIndependante;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.revenueActiviteLucrativeDependante.RevenuActiviteLucrativeDependante;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.revenueHypothtique.RevenuHypothtique;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.taxeJournalierHome.TaxeJournaliereHome;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.titre.Titre;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.vehicule.Vehicule;

public class DonneesFinancieresContainerTest {

    @Test
    public void testGetAllBiensImmobilier() throws Exception {

        DonneesFinancieresContainer container = buildContainer();

        assertEquals(3, container.getAllBiensImmobilier().size());
    }

    private DonneesFinancieresContainer buildContainer() {
        DonneesFinancieresContainer container = new DonneesFinancieresContainer();
        container.add(mock(AllocationFamilliale.class));
        container.add(mock(ApiAvsAi.class));
        container.add(mock(AssuranceRenteViagere.class));
        container.add(mock(AssuranceVie.class));
        container.add(mock(AutreApi.class));
        container.add(mock(AutreDetteProuvee.class));
        container.add(mock(AutreFortuneMobiliere.class));
        container.add(mock(AutreRente.class));
        container.add(mock(Betail.class));
        container.add(mock(BienImmobilierNonHabitable.class));
        container.add(mock(BienImmobilierNonPrincipale.class));
        container.add(mock(BienImmobilierServantHabitationPrincipale.class));
        container.add(mock(CapitalLpp.class));
        container.add(mock(CompteBancairePostal.class));
        container.add(mock(ContratEntretienViager.class));
        container.add(mock(CotisationPsal.class));
        container.add(mock(DessaisissementRevenu.class));
        container.add(mock(DessaisissementFortune.class));
        container.add(mock(IjAi.class));
        container.add(mock(IndemniteJournaliereApg.class));
        container.add(mock(Loyer.class));
        container.add(mock(MarchandiseStock.class));
        container.add(mock(Numeraire.class));
        container.add(mock(PensionAlimentaire.class));
        container.add(mock(PretEnversTiers.class));
        container.add(mock(RenteAvsAi.class));
        container.add(mock(RevenuActiviteLucrativeIndependante.class));
        container.add(mock(RevenuActiviteLucrativeDependante.class));
        container.add(mock(RevenuHypothtique.class));
        container.add(mock(TaxeJournaliereHome.class));
        container.add(mock(Titre.class));
        container.add(mock(Vehicule.class));
        return container;
    }

    @Test
    public void testGetAllDonneesWithInteret() throws Exception {
        DonneesFinancieresContainer container = buildContainer();

        assertEquals(5, container.getAllDonneesWithInteret().size());
    }

}
