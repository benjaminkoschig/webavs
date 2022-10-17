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
import globaz.prestation.acor.PRAcorTechnicalException;
import globaz.prestation.acor.web.mapper.PRConverterUtils;
import globaz.prestation.db.tauxImposition.PRTauxImposition;
import globaz.prestation.db.tauxImposition.PRTauxImpositionManager;
import globaz.prestation.tauxImposition.api.IPRTauxImposition;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
public class APAcorBaseCalculMapper {

    private final APDroitAPG droit;
    private final List<APSituationProfessionnelle> situationsProfessionnelles;

    public BasesCalculAPG map(final BSession session) {
        BasesCalculAPG basesCalcul = new BasesCalculAPG();

        basesCalcul.setGenreCarte(1);
        mapImpotSourceInformation(session, basesCalcul, droit);
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
            LOG.error("Impossible de r�cup�rer la situation familliale.", e);
            throw new PRAcorTechnicalException("Impossible de r�cup�rer la situation familliale" , e);
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
            LOG.error("Impossible de r�cup�rer la propri�t� "+APApplication.PROPERTY_MONTANT_MINIMUM_PAYE_ASSURE, e);
            throw new CommonTechnicalException("Impossible de r�cup�rer la propri�t� "+APApplication.PROPERTY_MONTANT_MINIMUM_PAYE_ASSURE , e);
        }
    }

    public static void mapDroitAcquisInformation(BSession session, BasesCalculCommunes basesCalcul, APDroitLAPG droit) {
        if(!JadeStringUtil.isBlankOrZero(droit.getDroitAcquis())) {
            GarantieIJ garantie = new GarantieIJ();
            try {
                garantie.setMontant(Double.parseDouble(droit.getDroitAcquis()));
                if(!JadeStringUtil.isEmpty(droit.getCsProvenanceDroitAcquis())) {
                    String code = session.getCode(droit.getCsProvenanceDroitAcquis());
                    if(!JadeStringUtil.isEmpty(code)) {
                        garantie.setSource(Integer.parseInt(code));
                    }
                }
                garantie.setNumeroReference(droit.getReference());
                basesCalcul.setGarantieIJ(garantie);
            } catch (NumberFormatException e){
                throw new PRAcorTechnicalException("Erreur lors du mapping des droits acquis du droit APG ou Maternit�.", e);
            }
        }
    }

    public static void mapImpotSourceInformation(BSession session, BasesCalculCommunes basesCalcul, APDroitLAPG droit) {
        if(Boolean.TRUE.equals(droit.getIsSoumisImpotSource())) {
            if(Objects.nonNull(droit.getTauxImpotSource()) && StringUtils.isNumeric(droit.getTauxImpotSource())) {
                basesCalcul.setTauxImpot(convertTauxImpot(droit.getTauxImpotSource()));
                basesCalcul.setCantonImpot(PRConverterUtils.formatRequiredInteger(PRACORConst.csCantonToAcor(droit.getCsCantonDomicile())));
            }
        }
    }

    private static double convertTauxImpot(String taux) {
        return Double.parseDouble(taux) / 100;
    }

    private List<APBaseCalcul> loadBasesCalcul(BSession session) {
        try {
            return APBasesCalculBuilder.of(session, droit).createBasesCalcul();
        } catch (Exception e) {
            LOG.error("Impossible de r�cup�rer les bases de Calcul.", e);
            throw new CommonTechnicalException("Impossible de r�cup�rer les bases de Calcul.", e);
        }
    }
}
