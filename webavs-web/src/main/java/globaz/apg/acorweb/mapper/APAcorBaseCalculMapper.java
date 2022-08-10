package globaz.apg.acorweb.mapper;

import acor.xsd.in.apg.BasesCalculAPG;
import acor.xsd.in.apg.BasesCalculCommunes;
import acor.xsd.in.apg.GarantieIJ;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.eavs.utils.StringUtils;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APSituationFamilialeAPG;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APBasesCalculBuilder;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.web.mapper.PRConverterUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@AllArgsConstructor
@Slf4j
public class APAcorBaseCalculMapper {

    private final APDroitAPG droit;
    private final List<APSituationProfessionnelle> situationsProfessionnelles;

    public BasesCalculAPG map(final BSession session) {
        BasesCalculAPG basesCalcul = new BasesCalculAPG();

        basesCalcul.setGenreCarte(1);
        mapImpotSourceInformation(basesCalcul, droit);
//        basesCalcul.setAFac();
//        basesCalcul.setExemptionCotisation();
        mapDroitAcquisInformation(session, basesCalcul, droit);
        mapLimiteTransferInformation(session, basesCalcul);

        basesCalcul.setGenreService(Integer.parseInt(session.getCode(droit.getGenreService())));

        if(!JadeStringUtil.isEmpty(droit.getNoCompte())) {
            basesCalcul.setNumeroReference(Integer.valueOf(droit.getNoCompte()));
        }
        if(!JadeStringUtil.isEmpty(droit.getNoControlePers())) {
            basesCalcul.setNumeroControle(Integer.valueOf(droit.getNoControlePers()));
        }

        try {
            APSituationFamilialeAPG situationFamilialeAPG = droit.loadSituationFamilliale();
            basesCalcul.setFraisGarde(Double.valueOf(situationFamilialeAPG.getFraisGarde()));
        } catch (Exception e) {
            LOG.error("Impossible de récupérer la situation familliale.", e);
            throw new CommonTechnicalException("Impossible de récupérer la situation familliale" , e);
        }

        List<APBaseCalcul> apBases = loadBasesCalcul(session);
        for(APBaseCalcul apBase : apBases) {
            basesCalcul.getPeriode().add(new APAcorPeriodeMapper(apBase, situationsProfessionnelles).map());
        }

        return basesCalcul;
    }

    public static void mapLimiteTransferInformation(BSession session, BasesCalculCommunes basesCalcul) {
        try {
            basesCalcul.setLimiteTransfert(Double.valueOf(session.getApplication().getProperty(
                    APApplication.PROPERTY_MONTANT_MINIMUM_PAYE_ASSURE)));
        } catch (Exception e) {
            LOG.error("Impossible de récupérer la propriété "+APApplication.PROPERTY_MONTANT_MINIMUM_PAYE_ASSURE, e);
            throw new CommonTechnicalException("Impossible de récupérer la propriété "+APApplication.PROPERTY_MONTANT_MINIMUM_PAYE_ASSURE , e);
        }
    }

    public static void mapDroitAcquisInformation(BSession session, BasesCalculCommunes basesCalcul, APDroitLAPG droit) {
        if(!JadeStringUtil.isBlankOrZero(droit.getDroitAcquis())) {
            GarantieIJ garantie = new GarantieIJ();
            garantie.setMontant(Double.parseDouble(droit.getDroitAcquis()));
            garantie.setSource(Integer.parseInt(session.getCode(droit.getCsProvenanceDroitAcquis())));
//  TODO            garantie.setNumeroReference(); --> obligatoire !
            basesCalcul.setGarantieIJ(garantie);
        }
    }

    public static void mapImpotSourceInformation(BasesCalculCommunes basesCalcul, APDroitLAPG droit) {
        if(droit.getIsSoumisImpotSource()) {
            basesCalcul.setCantonImpot(PRConverterUtils.formatRequiredInteger(PRACORConst.csCantonToAcor(droit.getCsCantonDomicile())));
            if(droit.getTauxImpotSource() != null && StringUtils.isNumeric(droit.getTauxImpotSource())) {
                basesCalcul.setTauxImpot(Double.parseDouble(droit.getTauxImpotSource()));
            }else {
                basesCalcul.setTauxImpot(0.00);
            }
        }
    }

    private List<APBaseCalcul> loadBasesCalcul(BSession session) {
        try {
            return APBasesCalculBuilder.of(session, droit).createBasesCalcul();
        } catch (Exception e) {
            LOG.error("Impossible de récupérer les bases de Calcul.", e);
            throw new CommonTechnicalException("Impossible de récupérer les bases de Calcul.", e);
        }
    }
}
