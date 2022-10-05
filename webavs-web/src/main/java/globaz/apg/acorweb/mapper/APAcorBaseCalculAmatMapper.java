package globaz.apg.acorweb.mapper;

import acor.xsd.in.apg.BasesCalculAMat;
import ch.globaz.common.util.Dates;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.db.droits.APSituationFamilialeMat;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.globall.db.BSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@AllArgsConstructor
@Slf4j
public class APAcorBaseCalculAmatMapper {

    private final APDroitMaternite droit;

    public BasesCalculAMat map(final BSession session) {
        BasesCalculAMat basesCalcul = new BasesCalculAMat();

        basesCalcul.setGenreCarte(1);
        APAcorBaseCalculMapper.mapImpotSourceInformation(session, basesCalcul, droit );

        APAcorBaseCalculMapper.mapDroitAcquisInformation(session, basesCalcul, droit);
        APAcorBaseCalculMapper.mapLimiteTransferInformation(session, basesCalcul);

        basesCalcul.setDateDebutConge(Dates.toXMLGregorianCalendar(Dates.toDate(droit.getDateDebutDroit())));
        basesCalcul.setDateRepriseActivite(Dates.toXMLGregorianCalendar(Dates.toDate(droit.getDateRepriseActiv())));

        List<APSituationFamilialeMat> situationsFamilialeAPG = APLoader.loadSituationFamillialeMat(droit.getIdDroit(), session);
        basesCalcul.setAdoption(situationsFamilialeAPG.stream().allMatch(APSituationFamilialeMat::getIsAdoption));

        basesCalcul.setNombreJoursHospitalisationEnfant(Integer.valueOf(droit.getJoursSupplementaires()));

        return basesCalcul;
    }

}
