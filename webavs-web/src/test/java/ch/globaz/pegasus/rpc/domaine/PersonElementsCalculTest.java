package ch.globaz.pegasus.rpc.domaine;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import ch.globaz.common.domaine.Canton;
import ch.globaz.pegasus.business.domaine.membreFamille.DonneesPersonnelles;
import ch.globaz.pegasus.business.domaine.membreFamille.MembreFamille;
import ch.globaz.pegasus.rpc.businessImpl.converter.RpcBusinessException;

public class PersonElementsCalculTest {

    @Test
    public void testCompensationAgencyNeed() throws Exception {
        DonneesPersonnelles donneesPersonnelles = new DonneesPersonnelles();
        donneesPersonnelles.setNoCaisseAvs("22.122");
        MembreFamille membreFamille = new MembreFamille();
        membreFamille.setDonneesPersonnelles(donneesPersonnelles);
        PersonElementsCalcul persCalEl = new PersonElementsCalcul();
        persCalEl.setMembreFamille(membreFamille);
        assertThat(persCalEl.hasCompensationAgency()).isEqualTo(true);
    }

    @Test
    public void testIsValidLivingAddress() throws Exception {
        PersonElementsCalcul persCalEl = new PersonElementsCalcul();
        RpcAddress address = new RpcAddress();
        persCalEl.setLivingAddress(address);
        assertThat(persCalEl.isValidLivingAddress()).isEqualTo(false);
        address = new RpcAddress(new Canton("ET"), "1000000", null, null, null, null);
        persCalEl.setLivingAddress(address);
        assertThat(persCalEl.isValidLivingAddress()).isEqualTo(false);
        address = new RpcAddress(new Canton("JU"), "2340", null, null, null, null);
        persCalEl.setLivingAddress(address);
        assertThat(persCalEl.isValidLivingAddress()).isEqualTo(true);
    }

    @Test
    public void testIsValidLegalAddress() throws Exception {
        PersonElementsCalcul persCalEl = new PersonElementsCalcul();
        MembreFamille membreFamille = new MembreFamille();
        persCalEl.setMembreFamille(membreFamille);
        RpcAddress address = new RpcAddress();
        persCalEl.setLegalAddress(address);
        // l'adresse legale est OBLIGATOIRE et remonte une exxception dans le process pour signaler dans le protocole et
        // mettre l'annonce en erreur
        try {
            assertThat(persCalEl.isValidLegalAddress()).isEqualTo(false);
        } catch (RpcBusinessException e) {
            assertThat(true);
        }
        // une adresse a l'étranger est a ce stade autorisée mais c'est le converter qui sautera une exception
        address = new RpcAddress(new Canton("ET"), "1000000", null, null, null, null);
        persCalEl.setLegalAddress(address);
        assertThat(persCalEl.isValidLegalAddress()).isEqualTo(true);
        address = new RpcAddress(new Canton("JU"), "2340", null, null, null, null);
        persCalEl.setLegalAddress(address);
        assertThat(persCalEl.isValidLegalAddress()).isEqualTo(true);
    }

    @Test
    public void testSplitNumeroCaisse() throws Exception {
        PersonElementsCalcul persCalEl = new PersonElementsCalcul();
        MembreFamille membreFamille = new MembreFamille();
        membreFamille.setDonneesPersonnelles(new DonneesPersonnelles());
        membreFamille.getDonneesPersonnelles().setNoCaisseAvs(null);
        persCalEl.setMembreFamille(membreFamille);
        assertThat(persCalEl.getNumeroOffice()).isNull();
        membreFamille.getDonneesPersonnelles().setNoCaisseAvs("");
        assertThat(persCalEl.getNumeroOffice()).isNull();

        membreFamille.getDonneesPersonnelles().setNoCaisseAvs("150");
        assertThat(persCalEl.getNumeroOffice()).isEqualTo(150);

        assertThat(persCalEl.getNumeroAgence()).isNull();

        membreFamille.getDonneesPersonnelles().setNoCaisseAvs("150.22");
        assertThat(persCalEl.getNumeroAgence()).isEqualTo(22);
    }
}
