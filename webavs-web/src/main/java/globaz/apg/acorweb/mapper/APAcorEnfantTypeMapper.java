package globaz.apg.acorweb.mapper;

import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.EnfantSexType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.EnfantType;
import ch.globaz.common.util.Dates;
import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.db.droits.APSituationFamilialeMat;
import globaz.commons.nss.NSUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.web.mapper.PRConverterUtils;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.impl.PRNSS13ChiffresUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class APAcorEnfantTypeMapper {

    private final PRTiersWrapper tiers;
    private final List<APSituationFamilialeMat> situationsFamiliale;

    public List<EnfantType> map() {
        return situationsFamiliale.stream()
                .filter(s -> IAPDroitMaternite.CS_TYPE_ENFANT.equals(s.getType()))
                .map(this::createEnfantType).collect(Collectors.toList());
    }

    private EnfantType createEnfantType(APSituationFamilialeMat situationFamiliale) {
        EnfantType enfant = new EnfantType();
        enfant.setNom(situationFamiliale.getNom());
        enfant.setPrenom(situationFamiliale.getPrenom());
        if(StringUtils.isNotEmpty(situationFamiliale.getNoAVS())) {
            enfant.setNavs(Long.parseLong(NSUtil.unFormatAVS(situationFamiliale.getNoAVS())));
        }else{
            enfant.setNavs(0);
        }
        enfant.setNavsParent1(Long.valueOf(NSUtil.unFormatAVS(tiers.getNSS())));
        enfant.setParent1Inconnu(false);
        enfant.setEtatCivil(Short.parseShort(PRACORConst.CA_CELIBATAIRE));
        enfant.setSexe(EnfantSexType.UNKNOWN);
        enfant.setNavsParent2(PRConverterUtils.formatNssToLong(PRNSS13ChiffresUtils.getNSSErrone(0)));
        enfant.setParent2Inconnu(true);
        enfant.setDateNaissance(Dates.toXMLGregorianCalendar(situationFamiliale.getDateNaissance()));
        return enfant;
    }

}

