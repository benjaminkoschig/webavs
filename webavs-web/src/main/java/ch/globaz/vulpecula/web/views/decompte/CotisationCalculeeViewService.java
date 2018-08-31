package ch.globaz.vulpecula.web.views.decompte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.CotisationCalculee;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.external.models.affiliation.PlanCaisse;
import ch.globaz.vulpecula.util.I18NUtil;

/**
 * @author Arnaud Geiser (AGE) | Créé le 16 mai 2014
 * 
 */
public final class CotisationCalculeeViewService {
    public static final CotisationCalculeeViewService INSTANCE = new CotisationCalculeeViewService();

    private CotisationCalculeeViewService() {
    }

    public static CotisationCalculeeViewService getInstance() {
        return INSTANCE;
    }

    public Map<CotisationCalculeeView, List<CotisationCalculeeView>> getCotisationsCalculee(final String idDecompte) {
        Map<CotisationCalculeeView, List<CotisationCalculeeView>> map = new TreeMap<CotisationCalculeeView, List<CotisationCalculeeView>>();

        Decompte decompte = VulpeculaRepositoryLocator.getDecompteRepository().findById(idDecompte);
        decompte.setLignes(VulpeculaRepositoryLocator.getDecompteSalaireRepository().findByIdDecompteWithDependencies(
                idDecompte));

        Taux totalTaux = Taux.ZERO();
        Montant totalCotisation = Montant.ZERO;

        for (Map.Entry<PlanCaisse, List<CotisationCalculee>> entry : decompte
                .getTableCotisationsCalculeesGroupByCaisse().entrySet()) {
            List<CotisationCalculeeView> value = new ArrayList<CotisationCalculeeView>();
            Taux sousTotalTauxPlanCaisse = Taux.ZERO();
            Montant sousTotalCotisationPlanCaisse = Montant.ZERO;
            for (CotisationCalculee cotisationCalculee : entry.getValue()) {
                String libelle = cotisationCalculee.getCotisationLibelle(I18NUtil.getUserLocale());
                String montant = cotisationCalculee.getMontantAsValue();
                String taux = cotisationCalculee.getTauxAsValue();
                String cotisation = cotisationCalculee.getCotisationCalculee();
                CotisationCalculeeView cotisationCalculeeView = new CotisationCalculeeView(libelle, montant, taux,
                        cotisation);
                value.add(cotisationCalculeeView);
                sousTotalTauxPlanCaisse = sousTotalTauxPlanCaisse.addTaux(cotisationCalculee.getTaux());
                sousTotalCotisationPlanCaisse = sousTotalCotisationPlanCaisse.add(cotisationCalculee
                        .getMontantCalculee().normalize());
                totalTaux = totalTaux.addTaux(cotisationCalculee.getTaux());
                totalCotisation = totalCotisation.add(cotisationCalculee.getMontantCalculee().normalize());
            }
            CotisationCalculeeView planCaisse = new CotisationCalculeeView(entry.getKey().getLibelle(), "",
                    sousTotalTauxPlanCaisse.getValue(), sousTotalCotisationPlanCaisse.getValueNormalisee());
            map.put(planCaisse, value);
        }

        final CotisationCalculeeView cotiTotal = new CotisationCalculeeView("Total", "", totalTaux.getValue(),
                totalCotisation.getValueNormalisee());
        map.put(new CotisationCalculeeView("TOTAL", "", "", ""), Arrays.asList(cotiTotal));
        return map;
    }
}
