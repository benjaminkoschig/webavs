package globaz.apg.helpers.droits;

import ch.globaz.common.exceptions.Exceptions;
import ch.globaz.common.exceptions.ThrowingRunnable;
import ch.globaz.common.util.Instances;
import globaz.apg.db.droits.APDroitProcheAidant;
import globaz.apg.vb.droits.APEnfantPatViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeStringUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class CareLeaveEventIdHelper {

    APDroitProcheAidant.CareLeaveEventId resolveCareLeaveEventId(LocalDate dateMinPeriodeDroit, Integer nbMoisPourleDelai,
                                                                 List<APDroitProcheAidant.NbJourDateMin> nbJourDateMins) {

        List<APDroitProcheAidant.NbJourDateMin> sorted =
                nbJourDateMins.stream()
                              .map(jourDateMin -> {
                                  LocalDate delai = APDroitProcheAidant
                                          .calculerDelai(jourDateMin.getDateDebutMin(), nbMoisPourleDelai);
                                  return jourDateMin.setDateDebutMin(delai);
                              })
                              .sorted(Comparator
                                              .comparing(APDroitProcheAidant.NbJourDateMin::getDateDebutMin))
                              .collect(Collectors.toList());

        return sorted.stream()
                     .filter(jourDateMin -> dateMinPeriodeDroit.isBefore(jourDateMin.getDateDebutMin()) || dateMinPeriodeDroit
                             .isEqual(jourDateMin.getDateDebutMin()))
                     .findFirst()
                     .map(o -> APDroitProcheAidant.CareLeaveEventId.of(o.getDateDebutMin(), o.getCareLeaveEventId()))
                     .orElseGet(() -> {
                         int careLeaveEventId = 1;
                         if (!sorted.isEmpty()) {
                             careLeaveEventId = sorted.get(sorted.size() - 1).getCareLeaveEventId() + 1;
                         }
                         return APDroitProcheAidant.CareLeaveEventId.ofCreated(dateMinPeriodeDroit, careLeaveEventId);
                     });
    }

    protected void retrieve(FWViewBeanInterface viewBean, BISession session) {
        Instances.of(viewBean).when(APEnfantPatViewBean.class, vb -> {
            if (vb.getTypeDemande().isProcheAidant()) {
                APDroitProcheAidant apDroitProcheAidant = APDroitProcheAidant.retrieve(vb.getDroitDTO().getIdDroit(), session);
                vb.setNumeroDelaiCadre(apDroitProcheAidant.getCareLeaveEventID());
            }
        });
    }

    public Optional<APDroitProcheAidant.CareLeaveEventId> updateCareLeveEventId(final FWViewBeanInterface viewBean, final BISession session)  {
        if (viewBean instanceof APEnfantPatViewBean) {
            APEnfantPatViewBean vb = (APEnfantPatViewBean) viewBean;
            if (vb.getTypeDemande().isProcheAidant()) {
                APDroitProcheAidant apDroitProcheAidant = APDroitProcheAidant.retrieve(vb.getDroitDTO().getIdDroit(), session);
                if (apDroitProcheAidant.hasSpy()) {
                    if (!JadeStringUtil.isBlankOrZero(vb.getNumeroDelaiCadre())) {
                        apDroitProcheAidant.setCareLeaveEventID(vb.getNumeroDelaiCadre());
                    } else {
                        LocalDate dateMinDuDroit =
                                apDroitProcheAidant.rechercherNbJourDateMinPourLeDroit()
                                                   .map(APDroitProcheAidant.NbJourDateMin::getDateDebutMin)
                                                   .orElse(LocalDate.now());

                        List<APDroitProcheAidant.NbJourDateMin> nbJourDateMins = apDroitProcheAidant.loadNbJourDateMinByEventId();

                        APDroitProcheAidant.CareLeaveEventId careLeaveEventId = resolveCareLeaveEventId(
                                dateMinDuDroit,
                                apDroitProcheAidant.trouverNombreMoisMax(),
                                nbJourDateMins);
                        apDroitProcheAidant.setCareLeaveEventID(careLeaveEventId.getId().toString());
                        return Optional.of(careLeaveEventId);
                    }
                    Exceptions.checkedToUnChecked((ThrowingRunnable<Exception>) apDroitProcheAidant::update);
                }
            }
        }
        return  Optional.empty();
    }
}
