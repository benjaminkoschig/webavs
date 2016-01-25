package ch.globaz.perseus.process.traitementAdaptation.calculDesDecisions;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityDataFind;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityPropertySavable;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.CSTypeDemande;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.process.traitementAdaptation.PFProcessTraitementAdaptationEnum;

public class PFProcessTraitementAdaptationCalculEntityHandler implements JadeProcessEntityInterface,
        JadeProcessEntityPropertySavable<PFProcessTraitementAdaptationEnum>, JadeProcessEntityDataFind {

    /**
     * id de la décision conservé dans getIdRef
     */
    private String id;
    private Map<PFProcessTraitementAdaptationEnum, String> mapSavedValue = new HashMap<PFProcessTraitementAdaptationEnum, String>();
    private Map<PFProcessTraitementAdaptationEnum, String> mapValueToSave = new HashMap<PFProcessTraitementAdaptationEnum, String>();
    private String moisProchainPaiement;

    public PFProcessTraitementAdaptationCalculEntityHandler(String moisProchainPaiement) {
        this.moisProchainPaiement = moisProchainPaiement;
    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        Demande demande = PerseusServiceLocator.getDemandeService().read(id);

        String dateDebut = demande.getSimpleDemande().getDateDebut();
        demande.getSimpleDemande().setDateDebut("01." + moisProchainPaiement);

        if (!PerseusServiceLocator.getDemandeService().checkCalculable(demande)) {
            demande.getSimpleDemande().setDateDebut(dateDebut);
            String erreurDemandeNonCalculable = BSessionUtil.getSessionFromThreadContext().getLabel(
                    "SERVICE_TRAITEMENT_DEMANDE_NON_CALCULABLE");

            String detailDeLErreur = "";

            if (mapSavedValue.containsKey(PFProcessTraitementAdaptationEnum.TYPE_POPULATION)) {
                detailDeLErreur = PFProcessTraitementAdaptationEnum.TYPE_POPULATION.toString() + "="
                        + mapSavedValue.get(PFProcessTraitementAdaptationEnum.TYPE_POPULATION) + ", ";
            }

            if (mapSavedValue.containsKey(PFProcessTraitementAdaptationEnum.DEMANDE_FERME)) {
                detailDeLErreur = detailDeLErreur + PFProcessTraitementAdaptationEnum.DEMANDE_FERME.toString() + "="
                        + mapSavedValue.get(PFProcessTraitementAdaptationEnum.DEMANDE_FERME) + ", ";
            }

            if (mapSavedValue.get(PFProcessTraitementAdaptationEnum.CS_TYPE_DECISION).equals(
                    CSTypeDecision.OCTROI_COMPLET.toString())) {

                detailDeLErreur = detailDeLErreur + "OCTROI_COMPLET";
            } else {
                detailDeLErreur = detailDeLErreur + "OCTROI_PARTIEL";
            }

            JadeThread.logError(this.getClass().getName(), erreurDemandeNonCalculable + " {" + detailDeLErreur + "}");
        } else {
            demande.getSimpleDemande().setDateDebut(dateDebut);
            String csTypeDemande = determinerTypeNouvelleDemande(
                    mapSavedValue.get(PFProcessTraitementAdaptationEnum.CS_TYPE_DECISION), demande.getSimpleDemande()
                            .getDateFin());

            Demande demandeCopie = copieDemande(demande, csTypeDemande);

            mapValueToSave.put(PFProcessTraitementAdaptationEnum.ID_DEMANDE_COPIE, demandeCopie.getId());

            if (!JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                // Calcul nouvelle demande
                PerseusServiceLocator.getPCFAccordeeService().calculer(demandeCopie.getId());
            }
        }
    }

    private Demande copieDemande(Demande demande, String csTypeDemande) throws PaiementException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException, DemandeException,
            JadeNoBusinessLogSessionError {
        Demande demandeCopie = new Demande();
        try {
            demandeCopie = (Demande) JadePersistenceUtil.clone(demande);
            String dateDebut = "01." + moisProchainPaiement;
            demandeCopie.getSimpleDemande().setDateDebut(dateDebut);
            demandeCopie.getSimpleDemande().setDateFin("");
            demandeCopie.getSimpleDemande().setDateDepot(dateDebut);
            demandeCopie.getSimpleDemande().setTypeDemande(csTypeDemande);

            demandeCopie = PerseusServiceLocator.getDemandeService().copier(demande, demandeCopie);

        } catch (JadeCloneModelException e) {
            String erreurDemandeNonCopie = BSessionUtil.getSessionFromThreadContext().getLabel(
                    "PROCESS_PF_TRAITEMENTADAPTATION_ERROR_DEMANDE_NON_COPIE");

            JadeThread.logError(this.getClass().getName(), erreurDemandeNonCopie + mapSavedValue.toString());
        }
        return demandeCopie;
    }

    private String determinerTypeNouvelleDemande(String csTypeDecision, String dateFin) throws PaiementException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Si la date de fin de la décision est le 31.12 de l'année précédent est de type partiel
        // alors révision périodique sinon extraordinaire
        String dateFinPartiel = JadeDateUtil.addYears("31.12." + moisProchainPaiement.substring(3), -1);
        if (dateFinPartiel.equals(dateFin) && CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(csTypeDecision)) {
            return CSTypeDemande.REVISION_PERIODIQUE.getCodeSystem();
        } else {
            return CSTypeDemande.REVISION_EXTRAORDINAIRE.getCodeSystem();
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
