package ch.globaz.vulpecula.repositoriesjade.postetravail;

import java.util.List;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurComplexModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurSearchComplexModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.repositories.postetravail.TravailleurRepository;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.converters.TravailleurConverter;

/***
 * Implémentation Jade de {@link TravailleurRepository}
 * 
 */
public class TravailleurRepositoryJade extends
        RepositoryJade<Travailleur, TravailleurComplexModel, TravailleurSimpleModel> implements TravailleurRepository {

    @Override
    public List<Travailleur> findAll() {
        TravailleurSearchComplexModel travailleurSearchComplexModel = new TravailleurSearchComplexModel();
        return searchAndFetch(travailleurSearchComplexModel);
    }

    @Override
    public Travailleur findById(final String id) {
        TravailleurSearchComplexModel searchComplexModel = new TravailleurSearchComplexModel();
        searchComplexModel.setForIdTravailleur(id);
        Travailleur travailleur = searchAndFetchFirst(searchComplexModel);
        loadRelations(travailleur);
        return travailleur;
    }

    private void loadRelations(final Travailleur travailleur) {
        if (travailleur != null) {
            Adresse adresse = VulpeculaRepositoryLocator.getAdresseRepository().findAdressePrioriteCourrierByIdTiers(
                    travailleur.getIdTiers());
            travailleur.setAdressePrincipale(adresse);
        }
    }

    @Override
    public Travailleur findByIdTiers(final String idTiers) {
        TravailleurSearchComplexModel searchComplexModel = new TravailleurSearchComplexModel();
        searchComplexModel.setForIdTiers(idTiers);
        Travailleur travailleur = searchAndFetchFirst(searchComplexModel);
        loadRelations(travailleur);
        return travailleur;
    }

    @Override
    public DomaineConverterJade<Travailleur, TravailleurComplexModel, TravailleurSimpleModel> getConverter() {
        return TravailleurConverter.getInstance();
    }
}
