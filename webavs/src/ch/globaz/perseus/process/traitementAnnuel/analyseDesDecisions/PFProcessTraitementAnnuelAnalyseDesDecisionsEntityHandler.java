package ch.globaz.perseus.process.traitementAnnuel.analyseDesDecisions;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityPropertySavable;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.CSTypeRetenue;
import ch.globaz.perseus.business.exceptions.models.pcfaccordee.PCFAccordeeException;
import ch.globaz.perseus.business.exceptions.models.retenue.RetenueException;
import ch.globaz.perseus.business.models.demande.DemandeTraitementMasse;
import ch.globaz.perseus.business.models.demande.DemandeTraitementMasseSearchModel;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.retenue.SimpleRetenue;
import ch.globaz.perseus.business.models.retenue.SimpleRetenueSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;
import ch.globaz.perseus.process.traitementAdaptation.PFTypePopulationEnum;
import ch.globaz.perseus.process.traitementAnnuel.PFProcessTraitementAnnuelEnum;

public class PFProcessTraitementAnnuelAnalyseDesDecisionsEntityHandler implements JadeProcessEntityInterface,
        JadeProcessEntityPropertySavable<PFProcessTraitementAnnuelEnum> {

    private String id;
    private Map<PFProcessTraitementAnnuelEnum, String> mapValueToSave = new HashMap<PFProcessTraitementAnnuelEnum, String>();

    public PFProcessTraitementAnnuelAnalyseDesDecisionsEntityHandler() {

    }

    // Traitement à effectuer pour chaque entité (tuple de population). Donc cas par cas !
    // Ca fait partie du step
    /**
     * La tronche du map en BD :
     * ------------------
     * |MAP | Id Entity |
     * -----------------
     * |<TYPE_POPULATION, "SANS_DATE_DE_FIN" > ....| L'entité à traiter |
     * |<ID_NEW_DECISION, 12345 > .................| L'entité à traiter |
     * ------------------
     */
    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        // mettre le calcul pour la demande
        DemandeTraitementMasseSearchModel demandeTraitementMasseSearchModel = new DemandeTraitementMasseSearchModel();

        demandeTraitementMasseSearchModel.setForIdDemande(id);

        List<String> forListCsTypes = new ArrayList<String>();
        forListCsTypes.add(CSTypeDecision.OCTROI_COMPLET.getCodeSystem());
        forListCsTypes.add(CSTypeDecision.OCTROI_PARTIEL.getCodeSystem());
        demandeTraitementMasseSearchModel.setForListCsTypes(forListCsTypes);

        demandeTraitementMasseSearchModel = (DemandeTraitementMasseSearchModel) JadePersistenceManager
                .search(demandeTraitementMasseSearchModel);

        // On a qu'une seule demande par id demande
        DemandeTraitementMasse demandeTraitementMasse = (DemandeTraitementMasse) demandeTraitementMasseSearchModel
                .getSearchResults()[0];

        if (demandeTraitementMasse.getCsTypeDecision().equals(CSTypeDecision.OCTROI_COMPLET.getCodeSystem())) {
            mapValueToSave.put(PFProcessTraitementAnnuelEnum.TYPE_POPULATION,
                    PFProcessTraitementAnnuelEnum.OCTROI.toString());

            if (hasRetenuNonIS(demandeTraitementMasse.getSimpleDemande().getId())) {
                mapValueToSave.put(PFProcessTraitementAnnuelEnum.CAS_AVEC_RETENU,
                        PFTypePopulationEnum.SANS_DATE_DE_FIN.toString());
                JadeThread.logWarn(this.getClass().getName(), "perseus.traitementAdaptation.warning.retenue.manuel");
            }
        } else if (demandeTraitementMasse.getCsTypeDecision().equals(CSTypeDecision.OCTROI_PARTIEL.getCodeSystem())) {
            mapValueToSave.put(PFProcessTraitementAnnuelEnum.TYPE_POPULATION,
                    PFProcessTraitementAnnuelEnum.PARTIEL.toString());
        }

    }

    private boolean hasRetenuNonIS(String idDemande) throws PCFAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, RetenueException {
        // indique si le cas est concerné par une retenue
        boolean isCasAvecRetenu = false;
        PCFAccordee pcf = PerseusServiceLocator.getPCFAccordeeService().readForDemande(idDemande);

        if (null != pcf) {
            SimpleRetenueSearchModel retenueSearch = new SimpleRetenueSearchModel();
            retenueSearch.setForIdPcfAccordee(pcf.getSimplePCFAccordee().getIdPCFAccordee());
            retenueSearch = PerseusImplServiceLocator.getSimpleRetenueService().search(retenueSearch);

            for (JadeAbstractModel model : retenueSearch.getSearchResults()) {
                SimpleRetenue retenu = (SimpleRetenue) model;
                if (!CSTypeRetenue.IMPOT_SOURCE.getCodeSystem().equals(retenu.getCsTypeRetenue())) {
                    isCasAvecRetenu = true;
                }
            }
        }
        return isCasAvecRetenu;
    }

    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        id = entity.getIdRef();
    }

    @Override
    public Map<PFProcessTraitementAnnuelEnum, String> getValueToSave() {
        return mapValueToSave;
    }

}
