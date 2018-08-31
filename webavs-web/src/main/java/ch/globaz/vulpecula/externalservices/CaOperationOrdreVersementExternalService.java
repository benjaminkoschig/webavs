package ch.globaz.vulpecula.externalservices;

import globaz.globall.context.BJadeThreadActivator;
import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.globall.util.JACalendar;
import globaz.jade.context.JadeThread;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPassageManager;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperationOrdreVersement;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServiceMilitaire;

public class CaOperationOrdreVersementExternalService extends BAbstractEntityExternalService {

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        if (entity instanceof CAOperationOrdreVersement) {
            CAOperationOrdreVersement caOperationOrdreVersement = (CAOperationOrdreVersement) entity;
            if ("205006".equals(caOperationOrdreVersement.getUcEtat().getIdCodeSysteme())) {

                CAJournal journal = caOperationOrdreVersement.getJournal();

                FAPassageManager faPassageManager = new FAPassageManager();
                faPassageManager.setSession(entity.getSession());
                faPassageManager.setForIdJournal(journal.getIdJournal());
                faPassageManager.find();

                String csCategorySection = caOperationOrdreVersement.getSection().getCategorieSection();

                if (faPassageManager.size() > 0) {
                    FAPassage faPassage = (FAPassage) faPassageManager.getFirstEntity();
                    String idPassage = faPassage.getIdPassage();

                    if (JadeThread.currentContext() == null) {
                        BJadeThreadActivator.startUsingContext(entity.getSession().getCurrentThreadTransaction());
                    }

                    if (APISection.ID_CATEGORIE_SECTION_PREST_CONV_ABSENCE_JUSTIFIEE.equals(csCategorySection)) {
                        List<AbsenceJustifiee> listAJ = VulpeculaRepositoryLocator.getAbsenceJustifieeRepository()
                                .findByIdPassage(idPassage);
                        updatePrestation(listAJ);
                    } else if (APISection.ID_CATEGORIE_SECTION_PREST_CONV_SERVICE_MILITAIRE.equals(csCategorySection)) {
                        List<ServiceMilitaire> listSM = VulpeculaRepositoryLocator.getServiceMilitaireRepository()
                                .findByIdPassage(idPassage);
                        updatePrestation(listSM);
                    } else if (APISection.ID_CATEGORIE_SECTION_PREST_CONV_CONGE_PAYE.equals(csCategorySection)) {
                        List<CongePaye> listCP = VulpeculaRepositoryLocator.getCongePayeRepository().findByIdPassage(
                                idPassage);
                        updatePrestation(listCP);
                    }
                }
            }
        }
    }

    private void updatePrestation(List<?> listPrest) {
        Date dateTransmission = new Date(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
        for (Object object : listPrest) {
            if (object instanceof AbsenceJustifiee) {
                AbsenceJustifiee aj = (AbsenceJustifiee) object;
                aj.setDateVersement(dateTransmission);
                VulpeculaRepositoryLocator.getAbsenceJustifieeRepository().update(aj);
            } else if (object instanceof ServiceMilitaire) {
                ServiceMilitaire sm = (ServiceMilitaire) object;
                sm.setDateVersement(dateTransmission);
                VulpeculaRepositoryLocator.getServiceMilitaireRepository().update(sm);
            } else if (object instanceof CongePaye) {
                CongePaye cp = (CongePaye) object;
                cp.setDateVersement(dateTransmission);
                VulpeculaRepositoryLocator.getCongePayeRepository().update(cp);
            }
        }
    }

    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeRetrieve(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void validate(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

}
