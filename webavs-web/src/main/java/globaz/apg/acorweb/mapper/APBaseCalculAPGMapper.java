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
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@AllArgsConstructor
public class APBaseCalculAPGMapper {

    private static final Logger LOG = LoggerFactory.getLogger(APBaseCalculAPGMapper.class);
    private final APDroitAPG droit;
    private final List<APSituationProfessionnelle> situationsProfessionnelles;

    public BasesCalculAPG map(final BSession session) {
        BasesCalculAPG basesCalcul = new BasesCalculAPG();

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
            LOG.error("Impossible de récupérer la situation familliale.", e);
            throw new CommonTechnicalException("Impossible de récupérer la situation familliale" , e);
        }

        List<APBaseCalcul> apBases = loadBasesCalcul(session);
        for(APBaseCalcul apBase : apBases) {
            basesCalcul.getPeriode().add(new APPeriodeMapper(apBase, situationsProfessionnelles).map());
        }

        return basesCalcul;
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
