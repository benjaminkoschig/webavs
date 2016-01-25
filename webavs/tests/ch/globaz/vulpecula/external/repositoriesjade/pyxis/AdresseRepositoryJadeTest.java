package ch.globaz.vulpecula.external.repositoriesjade.pyxis;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import ch.globaz.pyxis.business.model.AdressePaiementComplexModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.external.models.pyxis.AvoirAdressePaiement;

public class AdresseRepositoryJadeTest {
    private AdresseRepositoryJade adresseRepository;

    @Before
    public void setUp() {
        adresseRepository = spy(new AdresseRepositoryJade());
    }

    @Test
    public void findByIdTiers_GivenNoAdresses_ShouldReturnAnEmptyAdressePaiementObject() {
        List<AdressePaiementComplexModel> adresses = new ArrayList<AdressePaiementComplexModel>();
        doReturn(adresses).when(adresseRepository).getAdressesPaiements(anyString(), Matchers.any(Date.class));

        AvoirAdressePaiement adressePaiement = adresseRepository.findByIdTiers("1", new Date("10.10.2014"),
                Arrays.asList("68901001"));

        assertNull(adressePaiement.getId());
    }

    @Test
    public void findByIdTiers_GivenAdresseOfDomainePC_ShouldReturnTheOneObject() {
        List<AdressePaiementComplexModel> adresses = new ArrayList<AdressePaiementComplexModel>();
        adresses.add(createAdressePaiementWith("5", AvoirAdressePaiement.CS_DOMAINE_PRESTATIONS_CONVENTIONNELLES));
        doReturn(adresses).when(adresseRepository).getAdressesPaiements(anyString(), Matchers.any(Date.class));

        AvoirAdressePaiement adressePaiement = adresseRepository.findByIdTiers("1", new Date("10.10.2014"),
                Arrays.asList(AvoirAdressePaiement.CS_DOMAINE_PRESTATIONS_CONVENTIONNELLES));

        assertThat(adressePaiement.getId(), is("5"));
    }

    @Test
    public void findByIdTiers_GivenTwoAdressesOfDomainePCAndStandard_ShouldReturnThisObject() {
        List<AdressePaiementComplexModel> adresses = new ArrayList<AdressePaiementComplexModel>();
        adresses.add(createAdressePaiementWith("4", AvoirAdressePaiement.CS_DOMAINE_STANDARD));
        adresses.add(createAdressePaiementWith("5", AvoirAdressePaiement.CS_DOMAINE_PRESTATIONS_CONVENTIONNELLES));
        doReturn(adresses).when(adresseRepository).getAdressesPaiements(anyString(), Matchers.any(Date.class));

        AvoirAdressePaiement adressePaiement = adresseRepository.findByIdTiers("1", new Date("10.10.2014"),
                Arrays.asList(AvoirAdressePaiement.CS_DOMAINE_PRESTATIONS_CONVENTIONNELLES));

        assertThat(adressePaiement.getId(), is("5"));
    }

    @Test
    public void findByIdTiers_GivenTwoAdressesOfDomainePCAndStandard_ShouldReturnStandardAdresse() {
        List<AdressePaiementComplexModel> adresses = new ArrayList<AdressePaiementComplexModel>();
        adresses.add(createAdressePaiementWith("4", AvoirAdressePaiement.CS_DOMAINE_STANDARD));
        adresses.add(createAdressePaiementWith("5", AvoirAdressePaiement.CS_DOMAINE_PRESTATIONS_CONVENTIONNELLES));
        doReturn(adresses).when(adresseRepository).getAdressesPaiements(anyString(), Matchers.any(Date.class));

        AvoirAdressePaiement adressePaiement = adresseRepository.findByIdTiers("1", new Date("10.10.2014"),
                Arrays.asList(AvoirAdressePaiement.CS_DOMAINE_STANDARD));

        assertThat(adressePaiement.getId(), is("4"));
    }

    @Test
    public void findByIdTiersWithPCAndStandard_GivenTwoAdressesOfDomainePCAndStandard_ShouldReturnPCAdresse() {
        List<AdressePaiementComplexModel> adresses = new ArrayList<AdressePaiementComplexModel>();
        adresses.add(createAdressePaiementWith("4", AvoirAdressePaiement.CS_DOMAINE_STANDARD));
        adresses.add(createAdressePaiementWith("5", AvoirAdressePaiement.CS_DOMAINE_PRESTATIONS_CONVENTIONNELLES));
        doReturn(adresses).when(adresseRepository).getAdressesPaiements(anyString(), Matchers.any(Date.class));

        AvoirAdressePaiement adressePaiement = adresseRepository.findByIdTiers("1", new Date("10.10.2014"), Arrays
                .asList(AvoirAdressePaiement.CS_DOMAINE_PRESTATIONS_CONVENTIONNELLES,
                        AvoirAdressePaiement.CS_DOMAINE_STANDARD));

        assertThat(adressePaiement.getId(), is("5"));
    }

    @Test
    public void findByIdTiersWithStandardAndPC_GivenTwoAdressesOfDomainePCAndStandard_ShouldReturnStandardAdresse() {
        List<AdressePaiementComplexModel> adresses = new ArrayList<AdressePaiementComplexModel>();
        adresses.add(createAdressePaiementWith("4", AvoirAdressePaiement.CS_DOMAINE_STANDARD));
        adresses.add(createAdressePaiementWith("5", AvoirAdressePaiement.CS_DOMAINE_PRESTATIONS_CONVENTIONNELLES));
        doReturn(adresses).when(adresseRepository).getAdressesPaiements(anyString(), Matchers.any(Date.class));

        AvoirAdressePaiement adressePaiement = adresseRepository.findByIdTiers("1", new Date("10.10.2014"), Arrays
                .asList(AvoirAdressePaiement.CS_DOMAINE_STANDARD,
                        AvoirAdressePaiement.CS_DOMAINE_PRESTATIONS_CONVENTIONNELLES));

        assertThat(adressePaiement.getId(), is("4"));
    }

    @Test
    public void findByIdTiers_GivenTwoAdressesOfDomainePC_ShouldReturnTheAdressWhichContainedTheDate() {
        List<AdressePaiementComplexModel> adresses = new ArrayList<AdressePaiementComplexModel>();
        adresses.add(createAdressePaiementWith("5", AvoirAdressePaiement.CS_DOMAINE_PRESTATIONS_CONVENTIONNELLES,
                "31.12.2015", null));
        adresses.add(createAdressePaiementWith("4", AvoirAdressePaiement.CS_DOMAINE_PRESTATIONS_CONVENTIONNELLES,
                "01.01.2014", "31.12.2015"));
        doReturn(adresses).when(adresseRepository).getAdressesPaiements(anyString(), Matchers.any(Date.class));

        AvoirAdressePaiement adressePaiement = adresseRepository.findByIdTiers("1", new Date("01.06.2014"),
                Arrays.asList(AvoirAdressePaiement.CS_DOMAINE_PRESTATIONS_CONVENTIONNELLES));

        assertThat(adressePaiement.getId(), is("4"));
    }

    public AdressePaiementComplexModel createAdressePaiementWith(String id, String application) {
        return createAdressePaiementWith(id, application, "01.01.1990", null);
    }

    public AdressePaiementComplexModel createAdressePaiementWith(String id, String application, String dateDebut,
            String dateFin) {
        AdressePaiementComplexModel adressePaiementComplexModel = new AdressePaiementComplexModel();
        adressePaiementComplexModel.getAdressePaiement().setId(id);
        adressePaiementComplexModel.getAvoirPaiement().setId(id);
        adressePaiementComplexModel.getAvoirPaiement().setIdApplication(application);
        adressePaiementComplexModel.getAvoirPaiement().setDateDebutRelation(dateDebut);
        adressePaiementComplexModel.getAvoirPaiement().setDateFinRelation(dateFin);
        return adressePaiementComplexModel;
    }
}
