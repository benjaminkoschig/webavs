package ch.globaz.vulpecula.web.views.association;

import java.util.List;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.association.AssociationCotisation;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.external.exceptions.ViewException;
import ch.globaz.vulpecula.web.gson.AssociationsGSON;
import com.google.gson.Gson;

public class AssociationViewService {
    public List<CotisationAssociationProfessionnelle> findCotisationsAssociationsProfessionnelles(String idAssociation,
            String genre) {
        return VulpeculaServiceLocator.getCotisationAssociationProfessionnelleService()
                .findCotisationsByAssociationGenre(idAssociation,
                        GenreCotisationAssociationProfessionnelle.fromValue(genre));
    }

    public String findDefaultMasseSalariale(String idCotisationAP) {
        return VulpeculaServiceLocator.getCotisationAssociationProfessionnelleService()
                .findCotisationsById(idCotisationAP).getMasseSalarialeDefaut().getValue();
    }

    public boolean create(String associationsJson) throws ViewException {
        Gson gson = new Gson();
        AssociationsGSON associationsGSON = gson.fromJson(associationsJson, AssociationsGSON.class);
        List<AssociationCotisation> associationsCotisations = associationsGSON.convertToDomain();
        try {
            VulpeculaServiceLocator.getAssociationCotisationService().create(associationsGSON.idEmployeur,
                    associationsCotisations);
        } catch (UnsatisfiedSpecificationException e) {
            throw new ViewException(e);
        }
        return true;
    }
}
