package ch.globaz.perseus.process.traitementAdaptation.analyseDesDecisions;

import globaz.jade.client.util.JadeStringUtil;
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
import ch.globaz.perseus.process.traitementAdaptation.PFProcessTraitementAdaptationEnum;
import ch.globaz.perseus.process.traitementAdaptation.PFTypePopulationEnum;

public class PFProcessTraitementAdaptationAnalyseDesDecisionsEntityHandler implements JadeProcessEntityInterface,
        JadeProcessEntityPropertySavable<PFProcessTraitementAdaptationEnum> {

    private String id;
    private Map<PFProcessTraitementAdaptationEnum, String> mapValueToSave = new HashMap<PFProcessTraitementAdaptationEnum, String>();

    public PFProcessTraitementAdaptationAnalyseDesDecisionsEntityHandler() {

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
     * |<TYPE_POPULATION, "DATE_FIN_ANNEE_PASSE" > | L'entité à traiter |
     * |<ID_NEW_DECISION, 7846 > ..................| L'entité à traiter |
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

        if (JadeStringUtil.isEmpty(demandeTraitementMasse.getSimpleDemande().getDateFin())) {
            if (demandeTraitementMasse.getCsTypeDecision().equals(CSTypeDecision.OCTROI_COMPLET.getCodeSystem())
                    || (demandeTraitementMasse.getCsTypeDecision()
                            .equals(CSTypeDecision.OCTROI_PARTIEL.getCodeSystem()) && !demandeTraitementMasse
                            .getSimpleDemande().getFromRI())) {

                if (hasRetenuNonIS(demandeTraitementMasse.getSimpleDemande().getId())) {
                    mapValueToSave.put(PFProcessTraitementAdaptationEnum.CAS_AVEC_RETENU,
                            PFTypePopulationEnum.SANS_DATE_DE_FIN.toString());
                    JadeThread
                            .logWarn(this.getClass().getName(), "perseus.traitementAdaptation.warning.retenue.manuel");
                }
            } else {
                JadeThread.logError(this.getClass().getName(),
                        "perseus.traitementAdaptation.error.typepartielri.manuel");
            }

            mapValueToSave.put(PFProcessTraitementAdaptationEnum.TYPE_POPULATION,
                    PFTypePopulationEnum.SANS_DATE_DE_FIN.toString());
        } else {
            mapValueToSave.put(PFProcessTraitementAdaptationEnum.TYPE_POPULATION,
                    PFTypePopulationEnum.DATE_FIN_ANNEE_PASSE.toString());
        }

        mapValueToSave.put(PFProcessTraitementAdaptationEnum.CS_TYPE_DECISION,
                demandeTraitementMasse.getCsTypeDecision());
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
    public Map<PFProcessTraitementAdaptationEnum, String> getValueToSave() {
        return mapValueToSave;
    }

}
