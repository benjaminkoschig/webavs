package globaz.apg.acorweb.mapper;

import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.FamilleType;
import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.db.droits.APSituationFamilialeMat;

import java.util.List;
import java.util.stream.Collectors;

public class APFamilleMapper {

    List<APSituationFamilialeMat> situationsFamiliale;

    List<FamilleType> map(){
        return situationsFamiliale.stream()
                .filter(s -> IAPDroitMaternite.CS_TYPE_CONJOINT.equals(s.getType()))
                .map(s -> createAssureType()).collect(Collectors.toList());
    }

    private FamilleType createAssureType() {
        FamilleType famille = new FamilleType();
        famille.setLien(Short.valueOf(PRACORConst.CA_MARIE));
//        famille.setDebut();
        return  famille;
    }

}
