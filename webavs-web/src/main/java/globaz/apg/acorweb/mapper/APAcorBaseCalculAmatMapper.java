package globaz.apg.acorweb.mapper;

import acor.xsd.in.apg.BasesCalculAMat;
import acor.xsd.in.apg.GarantieIJ;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.util.Dates;
import globaz.apg.application.APApplication;
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
        if(droit.getIsSoumisImpotSource()) {
            basesCalcul.setCantonImpot(PRConverterUtils.formatRequiredInteger(PRACORConst.csCantonToAcor(droit.getCsCantonDomicile())));
            try {
                basesCalcul.setTauxImpot(Double.parseDouble(droit.getTauxImpotSource()));
            }catch(NullPointerException | NumberFormatException e){
                basesCalcul.setTauxImpot(0.00);
            }
        }
        // TODO : récupérer canton impot
//        basesCalcul.setAFac();
//        basesCalcul.setExemptionCotisation();
        if(!JadeStringUtil.isBlankOrZero(droit.getDroitAcquis())) {
            GarantieIJ garantie = new GarantieIJ();
            garantie.setMontant(Double.valueOf(droit.getDroitAcquis()));
            garantie.setSource(Integer.valueOf(session.getCode(droit.getCsProvenanceDroitAcquis())));
//          TODO : a voir si le champ est nécessaire (ou obligatoire)
//          garantie.setNumeroReference();
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
