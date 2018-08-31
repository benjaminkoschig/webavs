package ch.globaz.vulpecula.repositoriesjade.postetravail;

import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurComplexModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurSearchComplexModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
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
    public Travailleur update(Travailleur entity) {
        Travailleur travailleur = super.update(entity);
        // On va annoncer la modification à la table de synchronisation
        try {
            VulpeculaServiceLocator.getTravailleurService().notifierSynchronisationEbu(travailleur.getId(),
                    travailleur.getCorrelationId());
        } catch (JadePersistenceException e) {
            logger.error(e.getMessage());
        }
        return travailleur;
    }

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
    public Travailleur findByNss(final String nss) {
        TravailleurSearchComplexModel searchComplexModel = new TravailleurSearchComplexModel();
        searchComplexModel.setForNumAvs(nss);
        Travailleur travailleur = searchAndFetchFirst(searchComplexModel);
        loadRelations(travailleur);
        return travailleur;
    }

    @Override
    public DomaineConverterJade<Travailleur, TravailleurComplexModel, TravailleurSimpleModel> getConverter() {
        return TravailleurConverter.getInstance();
    }

    @Override
    public Travailleur findByNomPrenomDateNaissance(String nom, String prenom, String dateNaissance) {
        TravailleurSearchComplexModel searchModel = new TravailleurSearchComplexModel();
        List<Travailleur> resultList;
        searchModel.setLikeNom(nom);
        searchModel.setLikePrenom(prenom);
        searchModel.setForDateNaissance(dateNaissance);
        resultList = searchAndFetch(searchModel);
        Travailleur travailleur = null;

        if (resultList.size() == 1) {
            travailleur = resultList.get(0);
            loadRelations(travailleur);
        } else if (resultList.size() > 1) {

            travailleur = null;
            // mieux avec Exception !!
        }
        return travailleur;

    }

    @Override
    public Travailleur findByCorrelationId(String correlationId) {
        TravailleurSearchComplexModel searchModel = new TravailleurSearchComplexModel();
        List<Travailleur> resultList;
        searchModel.setForCorrelationId(correlationId);
        resultList = searchAndFetch(searchModel);
        Travailleur travailleur = null;

        if (resultList.size() == 1) {
            travailleur = resultList.get(0);
            loadRelations(travailleur);
        } else if (resultList.size() > 1) {
            travailleur = null;
            // mieux avec Exception !!
        }
        return travailleur;
    }

    @Override
    public List<Travailleur> findByNomPrenomDateNaissanceV2(String nom, String prenom, String dateNaissance) {
        TravailleurSearchComplexModel searchModel = new TravailleurSearchComplexModel();
        List<Travailleur> resultList;
        List<Travailleur> returnList = new ArrayList<Travailleur>();
        searchModel.setLikeNom(nom);
        searchModel.setLikePrenom(prenom);
        searchModel.setForDateNaissance(dateNaissance);
        resultList = searchAndFetch(searchModel);

        if (!resultList.isEmpty()) {
            for (Travailleur travailleur : resultList) {
                loadRelations(travailleur);
                returnList.add(travailleur);
            }
            return returnList;
        } else {
            return null;
        }
    }

    @Override
    public int countForNomPrenomDateNaissance(String nom, String prenom, String dateNaissance) {
        TravailleurSearchComplexModel searchModel = new TravailleurSearchComplexModel();
        List<Travailleur> resultList;
        searchModel.setLikeNom(nom);
        searchModel.setLikePrenom(prenom);
        searchModel.setForDateNaissance(dateNaissance);
        resultList = searchAndFetch(searchModel);

        return resultList.size();
    }

}
