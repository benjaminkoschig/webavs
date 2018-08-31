package ch.globaz.vulpecula.domain.repositories.registre;

import globaz.jade.exception.JadePersistenceException;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.registre.ParametreCotisationAssociation;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface ParametreCotisationAssociationRepository extends Repository<ParametreCotisationAssociation> {
    List<ParametreCotisationAssociation> findAll();

    List<ParametreCotisationAssociation> findCotisationsForFourchette(ParametreCotisationAssociation cotisationCM);

    List<ParametreCotisationAssociation> findForFourchetteAndIdCotisation(
            String idCotisationAssociationProfessionnelle, Montant montant);

    List<ParametreCotisationAssociation> findForFourchetteInferieuresAndIdCotisation(
            String idCotisationAssociationProfessionnelle, Montant masseSalariale);

    boolean isAuMoinsUnParametreForIdCotisation(String idCotisationAssociationProfessionnelle)
            throws JadePersistenceException;
}
