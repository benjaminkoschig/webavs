package ch.globaz.vulpecula.repositoriesjade.congepaye.converters;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.vulpecula.business.models.congepaye.CompteurComplexModel;
import ch.globaz.vulpecula.business.models.congepaye.CompteurSearchSimpleModel;
import ch.globaz.vulpecula.business.models.congepaye.CompteurSimpleModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailComplexModel;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.congepaye.Compteur;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.converters.PosteTravailConverter;

public class CompteurConverter implements DomaineConverterJade<Compteur, CompteurComplexModel, CompteurSimpleModel> {

    @Override
    public Compteur convertToDomain(CompteurComplexModel compteurComplexModel) {
        CompteurSimpleModel compteurSimpleModel = compteurComplexModel.getCompteurSimpleModel();
        PosteTravailComplexModel posteTravailComplexModel = compteurComplexModel.getPosteTravailComplexModel();

        Compteur compteur = convertToDomain(compteurSimpleModel);
        compteur.setPosteTravail(PosteTravailConverter.getInstance().convertToDomain(posteTravailComplexModel));
        return compteur;
    }

    @Override
    public CompteurSimpleModel convertToPersistence(Compteur compteur) {
        CompteurSimpleModel compteurSimpleModel = new CompteurSimpleModel();
        compteurSimpleModel.setId(compteur.getId());
        compteurSimpleModel.setIdPosteTravail(compteur.getIdPosteTravail());
        compteurSimpleModel.setCumulCotisation(compteur.getCumulCotisation().getValueNormalisee());
        compteurSimpleModel.setMontantRestant(compteur.getMontantRestant().getValueNormalisee());
        compteurSimpleModel.setAnnee(String.valueOf(compteur.getAnneeAsValue()));
        compteurSimpleModel.setSpy(compteur.getSpy());
        return compteurSimpleModel;
    }

    @Override
    public Compteur convertToDomain(CompteurSimpleModel compteurSimpleModel) {
        Compteur compteur = new Compteur();
        compteur.setId(compteurSimpleModel.getId());
        compteur.setAnnee(new Annee(compteurSimpleModel.getAnnee()));
        compteur.setCumulCotisation(new Montant(compteurSimpleModel.getCumulCotisation()));
        compteur.setMontantRestant(new Montant(compteurSimpleModel.getMontantRestant()));
        compteur.setSpy(compteurSimpleModel.getSpy());
        return compteur;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new CompteurSearchSimpleModel();
    }

}
