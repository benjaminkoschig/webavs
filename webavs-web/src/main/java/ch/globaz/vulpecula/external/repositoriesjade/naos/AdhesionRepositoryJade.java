package ch.globaz.vulpecula.external.repositoriesjade.naos;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.naos.translation.CodeSystem;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.external.models.AdhesionComplexModel;
import ch.globaz.vulpecula.external.models.AdhesionSearchComplexModel;
import ch.globaz.vulpecula.external.models.affiliation.Adhesion;
import ch.globaz.vulpecula.external.repositories.affiliation.AdhesionRepository;
import ch.globaz.vulpecula.external.repositoriesjade.naos.converters.AdhesionConverter;

/**
 * @author Arnaud Geiser (AGE) | Créé le 7 févr. 2014
 * 
 */
public class AdhesionRepositoryJade implements AdhesionRepository {

    @Override
    public List<Adhesion> findByIdAffilie(String idAffilie) {
        List<Adhesion> adhesions = new ArrayList<Adhesion>();
        try {
            AdhesionSearchComplexModel searchModel = new AdhesionSearchComplexModel();
            searchModel.setForIdAffilie(idAffilie);
            searchModel = (AdhesionSearchComplexModel) JadePersistenceManager.search(searchModel);
            for (JadeAbstractModel model : searchModel.getSearchResults()) {
                AdhesionComplexModel adhesionComplexModel = (AdhesionComplexModel) model;
                Adhesion adhesion = AdhesionConverter.convertToDomain(adhesionComplexModel);
                adhesions.add(adhesion);
            }
        } catch (JadePersistenceException ex) {
            ex.printStackTrace();
        }
        return adhesions;
    }

    @Override
    public Adhesion findCaisseMetier(String idAffilie) {
        AdhesionSearchComplexModel searchModel = new AdhesionSearchComplexModel();
        searchModel.setForIdAffilie(idAffilie);
        searchModel.setForTypeCaisse(CodeSystem.TYPE_ADHESION_CAISSE_PRINCIPALE);

        try {
            searchModel = (AdhesionSearchComplexModel) JadePersistenceManager.search(searchModel);
        } catch (JadePersistenceException e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
        if (searchModel.getSize() > 0) {
            return AdhesionConverter.convertToDomain((AdhesionComplexModel) searchModel.getSearchResults()[0]);
        } else {
            return null;
        }
    }

    @Override
    public List<Adhesion> findAdhesionsActivesByIdAffilie(String idAffilie) {
        List<Adhesion> adhesions = findByIdAffilie(idAffilie);
        List<Adhesion> adhesionsActives = new ArrayList<Adhesion>();

        for (Adhesion adhesion : adhesions) {
            if (adhesion.isActif()) {
                adhesionsActives.add(adhesion);
            }
        }

        return adhesionsActives;
    }

}
