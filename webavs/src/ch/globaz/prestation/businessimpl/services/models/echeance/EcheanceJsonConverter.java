package ch.globaz.prestation.businessimpl.services.models.echeance;

import java.util.Date;
import ch.globaz.common.ajax.EcheanceJson;
import ch.globaz.common.domaine.Echeance.CodeSystemeEnumUtils;
import ch.globaz.common.domaine.Echeance.Echeance;
import ch.globaz.common.domaine.Echeance.EcheanceDomaine;
import ch.globaz.common.domaine.Echeance.EcheanceEtat;
import ch.globaz.common.domaine.Echeance.EcheanceType;
import ch.globaz.pyxis.domaine.Tiers;

public class EcheanceJsonConverter {

    public static EcheanceJson convert(Echeance echeance) {
        EcheanceJson json = new EcheanceJson();
        json.setId(echeance.getId());
        json.setIdExterne(echeance.getIdExterne());
        json.setIdTiers(echeance.getTiers().getId());
        json.setDomaine(CodeSystemeEnumUtils.i18nLibelle(echeance.getDomaine()));
        json.setEtat(CodeSystemeEnumUtils.i18nLibelle(echeance.getEtat()));
        json.setType(CodeSystemeEnumUtils.i18nLibelle(echeance.getType()));
        json.setDateTraitement(ConverterUtils.formatWithNull(echeance.getDateTraitement()));
        json.setDateEcheance(ConverterUtils.format(echeance.getDateEcheance(), echeance));
        json.setCodeEtat(echeance.getEtat().toString());
        json.setCodeType(echeance.getType().toString());
        json.setCodeDomaine(echeance.getDomaine().toString());

        json.setNom(echeance.getTiers().getDesignation1());
        json.setPrenom(echeance.getTiers().getDesignation2());

        json.setLibelle(echeance.getLibelle());
        json.setDescription(echeance.getDescription());
        json.setSpy(echeance.getSpy());
        return json;
    }

    public static Echeance convert(EcheanceJson echeanceJson) {
        Tiers tiers = new Tiers();
        tiers.setId(echeanceJson.getIdTiers());

        EcheanceDomaine domaine = EcheanceDomaine.valueOf(echeanceJson.getCodeDomaine());
        EcheanceType type = EcheanceType.valueOf(echeanceJson.getCodeType());
        EcheanceEtat etat = EcheanceEtat.valueOf(echeanceJson.getCodeEtat());
        Date dateEcheance = ConverterUtils.parseDate(echeanceJson.getDateEcheance(), echeanceJson);
        Date dateTraitement = ConverterUtils.parseDateWithNull(echeanceJson.getDateTraitement(), echeanceJson);

        Echeance echeance = new Echeance(echeanceJson.getId(), echeanceJson.getIdExterne(), tiers, dateEcheance,
                domaine, etat, type, echeanceJson.getLibelle(), echeanceJson.getDescription(), dateTraitement);

        echeance.setSpy(echeanceJson.getSpy());

        return echeance;
    }

}
