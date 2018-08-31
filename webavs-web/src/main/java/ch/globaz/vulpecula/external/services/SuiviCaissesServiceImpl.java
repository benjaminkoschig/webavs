package ch.globaz.vulpecula.external.services;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.external.models.affiliation.SuiviCaisse;

public class SuiviCaissesServiceImpl implements SuiviCaissesService {

    @Override
    public List<SuiviCaisse> findByIdEmployeurAndDate(BSession session, String idEmployeur, Date dateReference) {
        List<SuiviCaisse> listeSuivi = new ArrayList<SuiviCaisse>();
        List<AFSuiviCaisseAffiliation> listeAFSuivi = AFSuiviCaisseAffiliation.listAllCaisse(idEmployeur, session);
        for (AFSuiviCaisseAffiliation afSuiviCaisseAffiliation : listeAFSuivi) {
            SuiviCaisse suivi = new SuiviCaisse();
            Date dateDebut = new Date(afSuiviCaisseAffiliation.getDateDebut());
            Date dateFin = null;
            if (!JadeStringUtil.isEmpty(afSuiviCaisseAffiliation.getDateFin())) {
                dateFin = new Date(afSuiviCaisseAffiliation.getDateFin());
            }

            if (dateDebut.after(dateReference)) {
                continue;
            }

            if (dateFin != null && dateFin.before(dateReference)) {
                continue;
            }

            suivi.setId(afSuiviCaisseAffiliation.getSuiviCaisseId());
            suivi.setIdTiersCaisse(afSuiviCaisseAffiliation.getIdTiersCaisse());
            suivi.setGenreCaisse(afSuiviCaisseAffiliation.getGenreCaisse());
            suivi.setPeriode(new Periode(afSuiviCaisseAffiliation.getDateDebut(), afSuiviCaisseAffiliation.getDateFin()));
            if (!JadeStringUtil.isBlankOrZero(afSuiviCaisseAffiliation.getIdTiersCaisse())) {
                suivi.setCaisse(VulpeculaRepositoryLocator.getAdministrationRepository().findById(
                        afSuiviCaisseAffiliation.getIdTiersCaisse()));
            }
            listeSuivi.add(suivi);
        }
        return listeSuivi;
    }

}
