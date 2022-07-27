package globaz.apg.acorweb.mapper;

import ch.admin.zas.xmlns.in_apg._0.BasesCalculAMat;
import ch.admin.zas.xmlns.in_apg._0.GarantieIJ;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.util.Dates;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.db.droits.APSituationFamilialeMat;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@AllArgsConstructor
public class APBaseCalculAmatMapper {

    private static final Logger LOG = LoggerFactory.getLogger(APBaseCalculAmatMapper.class);
    private final APDroitMaternite droit;

    public BasesCalculAMat map(final BSession session) {
        BasesCalculAMat basesCalcul = new BasesCalculAMat();

        basesCalcul.setGenreCarte(1);
//        basesCalcul.setCantonImpot();
//        basesCalcul.setTauxImpot();
//        basesCalcul.setAFac();
//        basesCalcul.setExemptionCotisation();
        if(!JadeStringUtil.isBlankOrZero(droit.getDroitAcquis())) {
            GarantieIJ garantie = new GarantieIJ();
            garantie.setMontant(Double.valueOf(droit.getDroitAcquis()));
            garantie.setSource(Integer.valueOf(session.getCode(droit.getCsProvenanceDroitAcquis())));
//  TODO            garantie.setNumeroReference(); --> obligatoire !
            basesCalcul.setGarantieIJ(garantie);
        }
        try {
            basesCalcul.setLimiteTransfert(Double.valueOf(session.getApplication().getProperty(
                    APApplication.PROPERTY_MONTANT_MINIMUM_PAYE_ASSURE)));
        } catch (Exception e) {
            LOG.error("Impossible de récupérer la propriété "+APApplication.PROPERTY_MONTANT_MINIMUM_PAYE_ASSURE, e);
            throw new CommonTechnicalException("Impossible de récupérer la propriété "+APApplication.PROPERTY_MONTANT_MINIMUM_PAYE_ASSURE , e);
        }

        basesCalcul.setDateDebutConge(Dates.toXMLGregorianCalendar(Dates.toDate(droit.getDateDebutDroit())));
        basesCalcul.setDateDecesEnfant(Dates.toXMLGregorianCalendar(Dates.toDate(droit.getDateFinDroit())));
        basesCalcul.setDateRepriseActivite(Dates.toXMLGregorianCalendar(Dates.toDate(droit.getDateRepriseActiv())));

        List<APSituationFamilialeMat> situationsFamilialeAPG = APLoader.loadSituationFamillialeMat(droit.getIdDroit(), session);
        basesCalcul.setAdoption(situationsFamilialeAPG.stream().allMatch(s -> s.getIsAdoption()));

//        basesCalcul.setAssuranceGE();
        basesCalcul.setNombreJoursHospitalisationEnfant(Integer.valueOf(droit.getJoursSupplementaires()));

        return basesCalcul;
    }

}
