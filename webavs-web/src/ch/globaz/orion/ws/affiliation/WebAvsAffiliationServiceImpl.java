package ch.globaz.orion.ws.affiliation;

import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationManager;
import globaz.orion.process.EBDanPreRemplissage;
import globaz.orion.utils.EBDanUtils;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.orion.ws.service.AppAffiliationService;
import ch.globaz.orion.ws.service.UtilsService;

@WebService(endpointInterface = "ch.globaz.orion.ws.affiliation.WebAvsAffiliationService")
public class WebAvsAffiliationServiceImpl implements WebAvsAffiliationService {

    @Override
    public Integer findCategorieAffiliation(String numeroAffilie, String dateDebutPeriode, String dateFinPeriode) {
        Checkers.checkNotEmpty(numeroAffilie, "NumeroAffilie");
        Checkers.checkDateAvs(dateDebutPeriode, "DateDebutPeriode", false);
        Checkers.checkDateAvs(dateFinPeriode, "DateFinPeriode", false);

        return AppAffiliationService.findCategorieAffiliation(UtilsService.initSession(), numeroAffilie,
                dateDebutPeriode, dateFinPeriode);

    }

    @Override
    public List<Integer> findActiveSuiviCaisse(String numeroAffilie, String annee) {
        Checkers.checkNotNull(numeroAffilie, "numeroAffilie");
        Checkers.checkNotNull(annee, "annee");
        List<Integer> list = new ArrayList<Integer>();
        AFAffiliation aff;
        try {
            aff = EBDanUtils.findAffilie(UtilsService.initSession(), numeroAffilie, "31.12." + annee, "01.01." + annee);

            String idTierLpp = findCaisseValide(annee, aff, EBDanPreRemplissage.GENRE_CAISSE_LPP);
            if (!JadeStringUtil.isBlankOrZero(idTierLpp)) {
                list.add(Integer.valueOf(idTierLpp));
            } else {
                list.add(0);
            }
            String idTierLaa = findCaisseValide(annee, aff, EBDanPreRemplissage.GENRE_CAISSE_LAA);
            if (!JadeStringUtil.isBlankOrZero(idTierLaa)) {
                list.add(Integer.valueOf(idTierLaa));
            } else {
                list.add(0);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    private String findCaisseValide(String annee, AFAffiliation aff, String typeCaisse) throws Exception {
        // On s'occupe du suivi LPP
        AFSuiviCaisseAffiliationManager caisseLppManager = new AFSuiviCaisseAffiliationManager();
        caisseLppManager.setSession(UtilsService.initSession());
        caisseLppManager.setForAffiliationId(aff.getAffiliationId());
        caisseLppManager.setForGenreCaisse(typeCaisse);
        caisseLppManager.setOrder("MYDDEB desc");
        caisseLppManager.setForAnnee(annee);
        caisseLppManager.find();

        if (!caisseLppManager.isEmpty()) {
            AFSuiviCaisseAffiliation caisse = (AFSuiviCaisseAffiliation) caisseLppManager.getFirstEntity();
            return caisse.getIdTiersCaisse();
        }
        return null;
    }
}
