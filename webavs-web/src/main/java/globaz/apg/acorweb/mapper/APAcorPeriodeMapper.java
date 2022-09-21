package globaz.apg.acorweb.mapper;

import acor.xsd.in.apg.PeriodeAPG;
import ch.globaz.common.util.Dates;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APBaseCalculSituationProfessionnel;
import lombok.AllArgsConstructor;

import java.util.List;


@AllArgsConstructor
public class APAcorPeriodeMapper {
    private final APBaseCalcul baseCalcul;
    private final List<APSituationProfessionnelle> situationProfessionnelles;

    public PeriodeAPG map() {
        PeriodeAPG periode = new PeriodeAPG();
        periode.setDebut(Dates.toXMLGregorianCalendar(Dates.toDate(baseCalcul.getDateDebut())));
        periode.setFin(Dates.toXMLGregorianCalendar(Dates.toDate(baseCalcul.getDateFin())));
        periode.setNombreJours(baseCalcul.getNombreJoursSoldes());
        periode.setNombreEnfants(baseCalcul.getNombreEnfants());
        periode.setIndemniteExploitation(situationProfessionnelles.stream()
                .filter(sit -> baseCalcul.getBasesCalculSituationProfessionnel().stream()
                        .anyMatch(b -> ((APBaseCalculSituationProfessionnel) b).getIdSituationProfessionnelle().equals(sit.getIdSituationProf())))
                .anyMatch(s -> s.getIsAllocationExploitation()));
        return periode;
    }
}
