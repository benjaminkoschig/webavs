package ch.globaz.vulpecula.repositoriesjade.decompte.converters;

import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.models.decomptes.CotisationDecompteComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.CotisationDecompteSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.CotisationDecompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.external.models.CotisationComplexModel;
import ch.globaz.vulpecula.external.repositoriesjade.naos.converters.CotisationConverter;

/**
 * Convertisseur d'objet {@link CotisationDecompte} <--> {@link CotisationDecompteComplexModel}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 6 janv. 2014
 * 
 */
public final class CotisationDecompteConverter {
    public static CotisationDecompteSimpleModel convertToPersistence(final CotisationDecompte cotisationDecompte,
            DecompteSalaire decompteSalaire) {
        CotisationDecompteSimpleModel cotisationDecompteSimpleModel = new CotisationDecompteSimpleModel();

        cotisationDecompteSimpleModel.setId(cotisationDecompte.getId());
        cotisationDecompteSimpleModel.setIdCotisation(cotisationDecompte.getCotisation().getId());
        cotisationDecompteSimpleModel.setIdLigneDecompte(decompteSalaire.getId());
        cotisationDecompteSimpleModel.setTaux(String.valueOf(cotisationDecompte.getTauxAsValue()));
        if (cotisationDecompte.getMasse() != null) {
            cotisationDecompteSimpleModel.setMasse(cotisationDecompte.getMasse().getValue());
        }
        cotisationDecompteSimpleModel.setMasseForcee(cotisationDecompte.getMasseForcee());
        cotisationDecompteSimpleModel.setSpy(cotisationDecompte.getSpy());

        return cotisationDecompteSimpleModel;
    }

    public static List<CotisationDecompte> convertToDomain(
            List<CotisationDecompteComplexModel> cotisationsDecompteComplexModel, DecompteSalaire decompteSalaire) {
        List<CotisationDecompte> cotisationDecomptes = new ArrayList<CotisationDecompte>();
        for (CotisationDecompteComplexModel cotisationDecompteComplexModel : cotisationsDecompteComplexModel) {
            cotisationDecomptes.add(convertToDomain(cotisationDecompteComplexModel, decompteSalaire));
        }
        return cotisationDecomptes;
    }

    public static CotisationDecompte convertToDomain(
            final CotisationDecompteComplexModel cotisationDecompteComplexModel, final DecompteSalaire decompteSalaire) {
        CotisationDecompteSimpleModel cotisationDecompteSimpleModel = cotisationDecompteComplexModel
                .getCotisationDecompteSimpleModel();
        CotisationComplexModel cotisationComplexModel = cotisationDecompteComplexModel.getCotisationComplexModel();

        CotisationDecompte cotisationDecompte = convertToDomain(cotisationDecompteSimpleModel);
        cotisationDecompte.setCotisation(CotisationConverter.convertToDomain(cotisationComplexModel));

        return cotisationDecompte;
    }

    public static CotisationDecompte convertToDomain(final CotisationDecompteSimpleModel cotisationDecompteSimpleModel) {
        CotisationDecompte cotisationDecompte = new CotisationDecompte();

        cotisationDecompte.setId(cotisationDecompteSimpleModel.getId());
        cotisationDecompte.setTaux(new Taux(cotisationDecompteSimpleModel.getTaux()));
        if (!JadeStringUtil.isEmpty(cotisationDecompteSimpleModel.getMasse())) {
            cotisationDecompte.setMasse(new Montant(cotisationDecompteSimpleModel.getMasse()));
        }
        cotisationDecompte.setMasseForcee(cotisationDecompteSimpleModel.getMasseForcee());

        cotisationDecompte.setSpy(cotisationDecompteSimpleModel.getSpy());

        return cotisationDecompte;
    }
}
