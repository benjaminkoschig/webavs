package globaz.prestation.acor.acor2020.mapper;

import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFPeriode;
import globaz.hera.api.ISFSituationFamiliale;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PRAccorPeriodeMapper {

    public static ISFPeriode[] recupererPeriodesMembre(ISFSituationFamiliale situationFamiliale,ISFMembreFamilleRequerant membre) {
        ISFPeriode[] periodes;
        try {
           return situationFamiliale.getPeriodes(membre.getIdMembreFamille());

//            // On filtre les périodes qui ne sont pas connues d'ACOR
//            List<ISFPeriode> listPeriode = new ArrayList<>();
//            for (int i = 0; i < periodesToFilre.length; i++) {
//               // if (!getTypePeriode(periodesToFilre[i]).isEmpty()) {
//                    listPeriode.add(periodesToFilre[i]);
//               // }
//            }
//            periodes = listPeriode.toArray(new ISFPeriode[listPeriode.size()]);

        } catch (Exception e) {
            LOG.error("Erreur lors de la récupération des périodes par membres.", e);
            periodes = new ISFPeriode[0];
        }
        return periodes;
    }


    private static String getTypePeriode(ISFPeriode periode) {

        if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_DOMICILE)) {
            return "do";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_TRAVAILLE)) {
            return "tr";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_NATIONALITE)) {
            return "na";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_AFFILIATION)) {
            return "af";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_COTISATION)) {
            return "ex";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_ASSURANCE_ETRANGERE)) {
            return "ae";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_ENFANT)) {
            return "rc";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE)) {
            return "et";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_IJ)) {
            return "ij";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_GARDE_BTE)) {
            return "be";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_INCARCERATION)) {
            return "in";
        } else {
            return "";
        }
    }
}
