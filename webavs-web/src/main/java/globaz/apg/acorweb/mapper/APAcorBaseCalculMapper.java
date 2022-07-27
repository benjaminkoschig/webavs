package globaz.apg.acorweb.mapper;

import ch.admin.zas.xmlns.in_apg._0.BasesCalculAPG;
import ch.admin.zas.xmlns.in_apg._0.GarantieIJ;
import ch.globaz.common.exceptions.CommonTechnicalException;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitAPG;
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
        basesCalcul.setCantonImpot(PRConverterUtils.formatRequiredInteger(PRACORConst.csCantonToAcor(droit.getCsCantonDomicile())));
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
            LOG.error("Impossible de r�cup�rer la propri�t� "+APApplication.PROPERTY_MONTANT_MINIMUM_PAYE_ASSURE, e);
            throw new CommonTechnicalException("Impossible de r�cup�rer la propri�t� "+APApplication.PROPERTY_MONTANT_MINIMUM_PAYE_ASSURE , e);
        }

        basesCalcul.setGenreService(Integer.valueOf(session.getCode(droit.getGenreService())));

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
            throw new CommonTechnicalException("Impossible de r�cup�rer la situation familliale" , e);
        }

        List<APBaseCalcul> apBases = loadBasesCalcul(session);
        for(APBaseCalcul apBase : apBases) {
            basesCalcul.getPeriode().add(new APAcorPeriodeMapper(apBase, situationsProfessionnelles).map());
        }

        return basesCalcul;
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
