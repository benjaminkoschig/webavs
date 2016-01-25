package ch.globaz.vulpecula.businessimpl.services.caissemaladie;

import java.util.List;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.caissemaladie.AffiliationCaisseMaladieService;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.domain.models.caissemaladie.SuiviCaisseMaladie;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.repositories.caissemaladie.AffiliationCaisseMaladieRepository;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class AffiliationCaisseMaladieServiceImpl implements AffiliationCaisseMaladieService {

    private AffiliationCaisseMaladieRepository repository;

    public AffiliationCaisseMaladieServiceImpl(AffiliationCaisseMaladieRepository repository) {
        this.repository = repository;
    }

    @Override
    public void create(AffiliationCaisseMaladie affiliationCaisseMaladie) throws UnsatisfiedSpecificationException {
        List<AffiliationCaisseMaladie> listCaisseMaladie = VulpeculaRepositoryLocator
                .getAffiliationCaisseMaladieRepository().findByIdTravailleur(
                        affiliationCaisseMaladie.getIdTravailleur());
        PosteTravail poste = VulpeculaRepositoryLocator.getPosteTravailRepository()
                .findByIdPosteTravailWithDependencies(affiliationCaisseMaladie.getIdPosteTravail());
        affiliationCaisseMaladie.setPosteTravail(poste);
        affiliationCaisseMaladie.validate(listCaisseMaladie);
        repository.create(affiliationCaisseMaladie);
        for (SuiviCaisseMaladie suiviCaisseMaladie : affiliationCaisseMaladie.getSuiviDocument()) {
            VulpeculaRepositoryLocator.getSuiviCaisseMaladieRepository().create(suiviCaisseMaladie);
        }
    }

    private List<SuiviCaisseMaladie> retrieveListeSuiviDocumentExistant(
            AffiliationCaisseMaladie affiliationCaisseMaladie) {
        return VulpeculaRepositoryLocator.getSuiviCaisseMaladieRepository().findByIdTravailleurAndCaisseMaladie(
                affiliationCaisseMaladie.getIdTravailleur(), affiliationCaisseMaladie.getIdCaisseMaladie());
    }

    @Override
    public void update(AffiliationCaisseMaladie affiliationCaisseMaladie) throws UnsatisfiedSpecificationException {
        List<AffiliationCaisseMaladie> listCaisseMaladie = VulpeculaRepositoryLocator
                .getAffiliationCaisseMaladieRepository().findByIdTravailleur(
                        affiliationCaisseMaladie.getIdTravailleur());
        PosteTravail poste = VulpeculaRepositoryLocator.getPosteTravailRepository()
                .findByIdPosteTravailWithDependencies(affiliationCaisseMaladie.getIdPosteTravail());
        affiliationCaisseMaladie.setPosteTravail(poste);
        affiliationCaisseMaladie.validate(listCaisseMaladie);
        if (affiliationCaisseMaladie.isModifiable()) {
            repository.update(affiliationCaisseMaladie);
            gestionSuivi(affiliationCaisseMaladie);
        }
    }

    private void gestionSuivi(AffiliationCaisseMaladie affiliationCaisseMaladie) {
        List<SuiviCaisseMaladie> suiviDocumentExistant = retrieveListeSuiviDocumentExistant(affiliationCaisseMaladie);
        for (SuiviCaisseMaladie suiviExistant : suiviDocumentExistant) {
            VulpeculaRepositoryLocator.getSuiviCaisseMaladieRepository().deleteById(suiviExistant.getId());
        }
        for (SuiviCaisseMaladie suiviCaisseMaladie : affiliationCaisseMaladie.getSuiviDocument()) {
            VulpeculaRepositoryLocator.getSuiviCaisseMaladieRepository().create(suiviCaisseMaladie);
        }
    }

    @Override
    public void delete(AffiliationCaisseMaladie affiliationCaisseMaladie) throws UnsatisfiedSpecificationException {
        if (affiliationCaisseMaladie.isSupprimable()) {
            repository.delete(affiliationCaisseMaladie);
            // Si il n'existe plus d'affiliationCaisseMaladie pour la même caisse et le même travailleur on supprimme le
            // suivi des documents.
            List<AffiliationCaisseMaladie> listeAffiliation = VulpeculaRepositoryLocator
                    .getAffiliationCaisseMaladieRepository().findByIdTravailleur(
                            affiliationCaisseMaladie.getIdTravailleur());
            boolean existeEncoreLaMemeCaisseMaladie = false;
            for (AffiliationCaisseMaladie affiliation : listeAffiliation) {
                if (affiliation.getIdCaisseMaladie().equals(affiliationCaisseMaladie.getIdCaisseMaladie())) {
                    existeEncoreLaMemeCaisseMaladie = true;
                }
            }
            if (!existeEncoreLaMemeCaisseMaladie) {
                List<SuiviCaisseMaladie> listeSuivi = VulpeculaRepositoryLocator.getSuiviCaisseMaladieRepository()
                        .findByIdTravailleurAndCaisseMaladie(affiliationCaisseMaladie.getIdTravailleur(),
                                affiliationCaisseMaladie.getIdCaisseMaladie());
                for (SuiviCaisseMaladie suiviCaisseMaladie : listeSuivi) {
                    VulpeculaRepositoryLocator.getSuiviCaisseMaladieRepository().delete(suiviCaisseMaladie);
                }
            }
        }
    }

    @Override
    public void createForPosteTravail(PosteTravail poste) {
        if (!poste.getIdTiersCM().equals("-1")) {
            AffiliationCaisseMaladie caisseMaladie = new AffiliationCaisseMaladie();
            caisseMaladie.setMoisDebut(poste.getDebutActivite());
            caisseMaladie.setMoisFin(poste.getFinActivite());
            caisseMaladie.setTravailleur(poste.getTravailleur());
            caisseMaladie.setIdPosteTravail(poste.getId());
            // Si aucune caisse idTiersCM = -1, aucune caisse n'a été sélectionné, donc pas d'entrée
            Administration adminCaisse = VulpeculaRepositoryLocator.getAdministrationRepository().findById(
                    poste.getIdTiersCM());
            caisseMaladie.setCaisseMaladie(adminCaisse);
            VulpeculaRepositoryLocator.getAffiliationCaisseMaladieRepository().create(caisseMaladie);
        }
    }

}
