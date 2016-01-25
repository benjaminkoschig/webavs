package ch.globaz.perseus.process.traitementAdaptation.fermetureDesDecisions;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityDataFind;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityPropertySavable;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.process.traitementAdaptation.PFProcessTraitementAdaptationEnum;
import ch.globaz.perseus.process.traitementAdaptation.PFTypePopulationEnum;

public class PFProcessTraitementAdaptationFermetureEntityHandler implements JadeProcessEntityInterface,
        JadeProcessEntityPropertySavable<PFProcessTraitementAdaptationEnum>, JadeProcessEntityDataFind {

    /**
     * id de la décision conservé dans getIdRef
     */
    private String id;
    private Map<PFProcessTraitementAdaptationEnum, String> mapSavedValue = new HashMap<PFProcessTraitementAdaptationEnum, String>();
    private Map<PFProcessTraitementAdaptationEnum, String> mapValueToSave = new HashMap<PFProcessTraitementAdaptationEnum, String>();
    private String moisProchainPaiement;

    public PFProcessTraitementAdaptationFermetureEntityHandler(String moisProchainPaiement) {
        this.moisProchainPaiement = moisProchainPaiement;
    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        Demande demande = PerseusServiceLocator.getDemandeService().read(id);
        String typeDeDecision;
        if (mapSavedValue.get(PFProcessTraitementAdaptationEnum.CS_TYPE_DECISION).equals(
                CSTypeDecision.OCTROI_COMPLET.toString())) {
            typeDeDecision = "OCTROI_COMPLET";
        } else {
            typeDeDecision = "OCTROI_PARTIEL";
        }

        if (PerseusServiceLocator.getDemandeService().iSConjointDossier(demande)) {
            String erreurConjoint = BSessionUtil.getSessionFromThreadContext().getLabel(
                    "PROCESS_PF_TRAITEMENTADAPTATION_ERROR_CONJOINT");
            JadeThread.logError(
                    this.getClass().getName(),
                    erreurConjoint
                            + " NSS du conjoint à double : "
                            + demande.getSituationFamiliale().getConjoint().getMembreFamille().getPersonneEtendue()
                                    .getPersonneEtendue().getNumAvsActuel() + ". Type de population : "
                            + mapSavedValue.get(PFProcessTraitementAdaptationEnum.TYPE_POPULATION) + ", "
                            + typeDeDecision);

        } else {

            siSansDateDeFinAlorsFermerDemandeOriginal(demande);

        }
    }

    private void siSansDateDeFinAlorsFermerDemandeOriginal(Demande demande) throws DemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, SituationFamilleException {
        if (mapSavedValue.get(PFProcessTraitementAdaptationEnum.TYPE_POPULATION).equals(
                PFTypePopulationEnum.SANS_DATE_DE_FIN.toString())) {
            // fermer la demande
            String dateFin = "01." + moisProchainPaiement;
            dateFin = JadeDateUtil.addDays(dateFin, -1);
            demande.getSimpleDemande().setDateFin(dateFin);
            PerseusServiceLocator.getDemandeService().update(demande);

            mapValueToSave.put(PFProcessTraitementAdaptationEnum.DEMANDE_FERME,
                    PFProcessTraitementAdaptationEnum.DEMANDE_FERME.toString());
        }
    }

    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        id = entity.getIdRef();
    }

    @Override
    public Map<PFProcessTraitementAdaptationEnum, String> getValueToSave() {
        return mapValueToSave;
    }

    @Override
    public void setData(Map hashMap) {
        mapSavedValue = hashMap;
    }

}
