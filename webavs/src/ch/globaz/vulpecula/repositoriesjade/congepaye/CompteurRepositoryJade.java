package ch.globaz.vulpecula.repositoriesjade.congepaye;

import globaz.jade.client.util.JadeListUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.business.models.congepaye.CompteurComplexModel;
import ch.globaz.vulpecula.business.models.congepaye.CompteurSearchComplexModel;
import ch.globaz.vulpecula.business.models.congepaye.CompteurSimpleModel;
import ch.globaz.vulpecula.business.models.congepaye.LigneCompteurSearchSimpleModel;
import ch.globaz.vulpecula.business.models.congepaye.LigneCompteurSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.congepaye.Compteur;
import ch.globaz.vulpecula.domain.models.congepaye.LigneCompteur;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.domain.repositories.congepaye.CompteurRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.congepaye.converters.CompteurConverter;
import ch.globaz.vulpecula.repositoriesjade.congepaye.converters.LigneCompteurConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class CompteurRepositoryJade extends RepositoryJade<Compteur, CompteurComplexModel, CompteurSimpleModel>
        implements CompteurRepository {

    @Override
    public Compteur findById(String id) {
        CompteurSearchComplexModel searchModel = new CompteurSearchComplexModel();
        searchModel.setForId(id);
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public DomaineConverterJade<Compteur, CompteurComplexModel, CompteurSimpleModel> getConverter() {
        return new CompteurConverter();
    }

    @Override
    public Compteur findByPosteTravailAndAnnee(String idPosteTravail, String annee) {
        CompteurSearchComplexModel searchModel = new CompteurSearchComplexModel();
        searchModel.setForIdPosteTravail(idPosteTravail);
        searchModel.setForAnnee(annee);
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public List<Compteur> findByPosteTravailAndPeriode(String idPosteTravail, String anneeDebut, String anneeFin) {
        CompteurSearchComplexModel searchModel = new CompteurSearchComplexModel();
        searchModel.setForIdPosteTravail(idPosteTravail);
        searchModel.setForAnneeDebut(anneeDebut);
        searchModel.setForAnneeFin(anneeFin);
        return searchAndFetch(searchModel);
    }

    @Override
    public Compteur findByPosteTravailAndAnnee(String idPosteTravail, Annee annee) {
        return findByPosteTravailAndAnnee(idPosteTravail, String.valueOf(annee.getValue()));
    }

    /**
     * Mise à jour du compteur passé en paramètre.
     * La mise à jour d'un compteur ajoute également les nouvelles lignes en base de données mais NE MODIFIE PAS les
     * existantes !
     * 
     * @return Compteur à mettre à jour
     */
    @Override
    public Compteur update(Compteur compteur) {
        CompteurSimpleModel compteurSimpleModel = getConverter().convertToPersistence(compteur);
        try {
            JadePersistenceManager.update(compteurSimpleModel);
            compteur.setSpy(compteurSimpleModel.getSpy());

            for (LigneCompteur ligneCompteur : compteur.getLignes()) {
                LigneCompteurConverter converter = LigneCompteurConverter.getInstance();
                LigneCompteurSimpleModel ligneCompteurSimpleModel = converter.convertToPersistence(ligneCompteur);
                JadePersistenceManager.add(ligneCompteurSimpleModel);
            }
        } catch (JadePersistenceException e) {
            logger.error(e.getMessage());
        }
        return compteur;
    }

    @Override
    public List<Compteur> findByIdPosteTravail(String idPosteTravail) {
        CompteurSearchComplexModel compteurSearchComplexModel = new CompteurSearchComplexModel();
        compteurSearchComplexModel.setForIdPosteTravail(idPosteTravail);
        return searchAndFetch(compteurSearchComplexModel);
    }

    @Override
    public List<Compteur> findCompteursForAnneeMoins5(Convention convention, Annee annee) {
        CompteurSearchComplexModel compteurSearchComplexModel = new CompteurSearchComplexModel();
        if (convention == null) {
            logger.debug("La convention est null, tous les compteurs seront repris");
        } else {
            compteurSearchComplexModel.setForIdConvention(convention.getId());
        }
        compteurSearchComplexModel.setForAnnee(annee.previous(5).toString());
        compteurSearchComplexModel.setMontantRestantNotZero();
        return searchAndFetch(compteurSearchComplexModel);
    }

    @Override
    public List<Compteur> findByIdPosteTravailWithDependencies(String idPosteTravail) {
        List<Compteur> compteurs = findByIdPosteTravail(idPosteTravail);
        List<String> ids = new ArrayList<String>();

        for (Compteur compteur : compteurs) {
            ids.add(compteur.getId());
        }

        return findByIdPosteTravailWithDependencies(compteurs, ids);
    }

    public List<Compteur> findByIdPosteTravailWithDependencies(List<Compteur> compteurs, List<String> ids) {
        List<LigneCompteurSimpleModel> lignesCompteursSimpleModel = RepositoryJade.searchByLot(ids,
                new SearchLotExecutor<LigneCompteurSimpleModel>() {
                    @Override
                    public List<LigneCompteurSimpleModel> execute(final List<String> ids) {
                        LigneCompteurSearchSimpleModel searchModel = new LigneCompteurSearchSimpleModel();
                        searchModel.setForIdCompteurIn(ids);
                        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                        try {
                            JadePersistenceManager.search(searchModel);
                        } catch (JadePersistenceException ex) {
                            logger.error(ex.getMessage());
                        }
                        List<JadeAbstractModel> models = Arrays.asList(searchModel.getSearchResults());
                        List<LigneCompteurSimpleModel> lignesCompteursSimpleModel = (List<LigneCompteurSimpleModel>) (List<?>) models;
                        return lignesCompteursSimpleModel;
                    }
                });

        Map<String, List<LigneCompteurSimpleModel>> map = JadeListUtil.groupBy(lignesCompteursSimpleModel,
                new JadeListUtil.Key<LigneCompteurSimpleModel>() {
                    @Override
                    public String exec(final LigneCompteurSimpleModel e) {
                        return String.valueOf(e.getIdCompteur());
                    }
                });

        for (Compteur compteur : compteurs) {
            if (map.containsKey(compteur.getId())) {
                List<LigneCompteurSimpleModel> lignesCompteurSimpleModel = map.get(compteur.getId());
                List<LigneCompteur> ligneCompteurs = LigneCompteurConverter.getInstance().convertToDomain(
                        lignesCompteurSimpleModel);
                compteur.setLignes(ligneCompteurs);
            }
        }
        return compteurs;
    }
}
