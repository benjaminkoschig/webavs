package ch.globaz.prestation.businessimpl.services.models.echeance;

import globaz.jade.client.util.JadeStringUtil;
import java.util.Date;
import ch.globaz.common.domaine.Echeance.CodeSystemeEnumUtils;
import ch.globaz.common.domaine.Echeance.Echeance;
import ch.globaz.common.domaine.Echeance.EcheanceDomaine;
import ch.globaz.common.domaine.Echeance.EcheanceEtat;
import ch.globaz.common.domaine.Echeance.EcheanceType;
import ch.globaz.prestation.business.models.echance.SimpleEcheance;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.domaine.Tiers;

public class EcheanceConverter {

    Echeance fromPersistence(SimpleEcheance simpleEcheance, TiersSimpleModel simpleTiers) {
        EcheanceType type = CodeSystemeEnumUtils.parse(simpleEcheance.getCsTypeEcheance(), EcheanceType.class);
        EcheanceEtat etat = CodeSystemeEnumUtils.parse(simpleEcheance.getCsEtat(), EcheanceEtat.class);
        EcheanceDomaine domaine = CodeSystemeEnumUtils.parse(simpleEcheance.getCsDomaine(), EcheanceDomaine.class);
        Date dateEcheance;
        dateEcheance = ConverterUtils.parseDate(simpleEcheance.getDateEcheance(), simpleEcheance);
        Date dateTraitement = null;

        dateTraitement = ConverterUtils.parseDateWithNull(simpleEcheance.getDateDeTraitement(), simpleEcheance);

        Tiers tiers = null;
        if (!JadeStringUtil.isBlankOrZero(simpleEcheance.getIdTiers())) {
            tiers = new Tiers();
            tiers.setId(Long.valueOf(simpleEcheance.getIdTiers()));
            if (simpleTiers != null) {
                tiers.setDesignation1(simpleTiers.getDesignation1());
                tiers.setDesignation2(simpleTiers.getDesignation2());
            }
        }
        Echeance echeance = new Echeance(simpleEcheance.getId(), simpleEcheance.getIdExterne(), tiers, dateEcheance,
                domaine, etat, type, simpleEcheance.getLibelle(), simpleEcheance.getRemarque(), dateTraitement);
        echeance.setSpy(simpleEcheance.getSpy());
        return echeance;
    }

    Echeance fromPersistence(SimpleEcheance simpleEcheance) {
        return this.fromPersistence(simpleEcheance, null);
    }

    SimpleEcheance toPersistence(Echeance echeance) {

        SimpleEcheance simpleEcheance = new SimpleEcheance();
        simpleEcheance.setId(echeance.getId());
        simpleEcheance.setIdExterne(echeance.getIdExterne());
        if (echeance.getTiers() != null) {
            simpleEcheance.setIdTiers(String.valueOf(echeance.getTiers().getId()));
        }
        simpleEcheance.setCsDomaine(echeance.getDomaine().getCodeSysteme());
        simpleEcheance.setCsEtat(echeance.getEtat().getCodeSysteme());
        simpleEcheance.setCsTypeEcheance(echeance.getType().getCodeSysteme());

        simpleEcheance.setDateDeTraitement(ConverterUtils.formatWithNull(echeance.getDateTraitement()));
        simpleEcheance.setDateEcheance(ConverterUtils.format(echeance.getDateEcheance(), echeance));

        simpleEcheance.setLibelle(echeance.getLibelle());
        simpleEcheance.setRemarque(echeance.getDescription());
        simpleEcheance.setSpy(echeance.getSpy());
        return simpleEcheance;
    }

}
