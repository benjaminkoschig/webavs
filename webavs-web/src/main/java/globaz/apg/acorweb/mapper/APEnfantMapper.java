package globaz.apg.acorweb.mapper;

import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.EnfantSexType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.EnfantType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.FamilleType;
import ch.globaz.common.util.Dates;
import ch.globaz.common.util.NSSUtils;
import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.db.droits.APSituationFamilialeMat;
import globaz.commons.nss.NSUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class APEnfantMapper {

    PRTiersWrapper tiers;
    List<APSituationFamilialeMat> situationsFamiliale;

    public List<EnfantType> map() {
        return situationsFamiliale.stream()
                .filter(s -> IAPDroitMaternite.CS_TYPE_ENFANT.equals(s.getType()))
                .map(s2 -> createEnfantType(s2)).collect(Collectors.toList());
    }

    private EnfantType createEnfantType(APSituationFamilialeMat situationFamiliale) {
        EnfantType enfant = new EnfantType();
        enfant.setNom(situationFamiliale.getNom());
        enfant.setPrenom(situationFamiliale.getPrenom());
        enfant.setNavs(Long.valueOf(NSUtil.unFormatAVS(situationFamiliale.getNoAVS())));
        enfant.setNavsParent1(Long.valueOf(NSUtil.unFormatAVS(tiers.getNSS())));
        enfant.setEtatCivil(Short.valueOf(PRACORConst.CA_CELIBATAIRE));
        enfant.setSexe(EnfantSexType.UNKNOWN);
        Optional<APSituationFamilialeMat> parent2 = situationsFamiliale.stream()
                .filter(s -> IAPDroitMaternite.CS_TYPE_PERE.equals(s.getType()) || IAPDroitMaternite.CS_TYPE_CONJOINT.equals(s.getType()))
                .findFirst();
        if(parent2.isPresent()) {
            enfant.setNavsParent2(Long.valueOf(NSUtil.unFormatAVS(parent2.get().getNoAVS())));
        }
        enfant.setDateNaissance(Dates.toXMLGregorianCalendar(situationFamiliale.getDateNaissance()));
        return  enfant;
    }

}

