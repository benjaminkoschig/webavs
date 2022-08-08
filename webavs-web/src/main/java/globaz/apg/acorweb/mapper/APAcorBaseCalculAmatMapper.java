package globaz.apg.acorweb.mapper;

import acor.xsd.in.apg.BasesCalculAMat;
import acor.xsd.in.apg.GarantieIJ;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.util.Dates;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.db.droits.APSituationFamilialeMat;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.web.mapper.PRConverterUtils;
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
        APAcorBaseCalculMapper.setImpotSourceInformation(basesCalcul, droit);
//        basesCalcul.setAFac();
//        basesCalcul.setExemptionCotisation();
        APAcorBaseCalculMapper.setDroitAcquisInformation(session, basesCalcul, droit);
        APAcorBaseCalculMapper.setLimiteTransferInformation(session, basesCalcul);

        basesCalcul.setDateDebutConge(Dates.toXMLGregorianCalendar(Dates.toDate(droit.getDateDebutDroit())));
        basesCalcul.setDateRepriseActivite(Dates.toXMLGregorianCalendar(Dates.toDate(droit.getDateRepriseActiv())));

        List<APSituationFamilialeMat> situationsFamilialeAPG = APLoader.loadSituationFamillialeMat(droit.getIdDroit(), session);
        basesCalcul.setAdoption(situationsFamilialeAPG.stream().allMatch(APSituationFamilialeMat::getIsAdoption));

//        basesCalcul.setAssuranceGE();
        basesCalcul.setNombreJoursHospitalisationEnfant(Integer.valueOf(droit.getJoursSupplementaires()));

        return basesCalcul;
    }

}
