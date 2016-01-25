package ch.globaz.vulpecula.repositoriesjade.decompte.converters;

import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.models.decomptes.AbsenceComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.AbsenceSimpleModel;
import ch.globaz.vulpecula.domain.models.decompte.Absence;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.TypeAbsence;

/**
 * Convertisseur d'objet {@link Absence} <--> {@link AbsenceComplexModel}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 6 janv. 2014
 * 
 */
public final class AbsenceConverter {

    public static AbsenceSimpleModel convertToPersistence(Absence absence, DecompteSalaire decompteSalaire) {
        AbsenceSimpleModel absenceSimpleModel = new AbsenceSimpleModel();
        absenceSimpleModel.setId(absence.getId());
        absenceSimpleModel.setIdLigneDecompte(decompteSalaire.getId());
        absenceSimpleModel.setType(absence.getTypeAsValue());
        absenceSimpleModel.setSpy(absence.getSpy());
        return absenceSimpleModel;
    }

    public static List<Absence> convertToDomain(List<AbsenceComplexModel> absencesComplexModel,
            DecompteSalaire decompteSalaire) {
        List<Absence> absences = new ArrayList<Absence>();
        for (AbsenceComplexModel absenceComplexModel : absencesComplexModel) {
            Absence absence = convertToDomain(absenceComplexModel, decompteSalaire);
            absences.add(absence);
        }
        return absences;
    }

    public static Absence convertToDomain(AbsenceComplexModel absenceComplexModel, DecompteSalaire decompteSalaire) {
        AbsenceSimpleModel absenceSimpleModel = absenceComplexModel.getAbsenceSimpleModel();

        Absence absence = convertToDomain(absenceSimpleModel);

        return absence;
    }

    public static Absence convertToDomain(AbsenceSimpleModel absenceSimpleModel) {
        // Les absences sont rattachées aux lignes de décomptes grâce à un left
        // outer join, de ce fait, certaine absence non existante tenteront
        // d'être créees.
        if (JadeStringUtil.isEmpty(absenceSimpleModel.getId())) {
            return null;
        }
        Absence absence = new Absence();
        absence.setId(absenceSimpleModel.getId());
        absence.setType(TypeAbsence.fromValue(absenceSimpleModel.getType()));
        absence.setSpy(absenceSimpleModel.getSpy());
        return absence;
    }
}
