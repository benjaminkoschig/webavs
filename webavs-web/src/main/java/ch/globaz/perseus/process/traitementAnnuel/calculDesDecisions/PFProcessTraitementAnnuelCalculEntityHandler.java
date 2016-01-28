package ch.globaz.perseus.process.traitementAnnuel.calculDesDecisions;

import globaz.globall.db.BSessionUtil;
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
import ch.globaz.perseus.business.constantes.CSTypeDemande;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.process.traitementAnnuel.PFProcessTraitementAnnuelEnum;

public class PFProcessTraitementAnnuelCalculEntityHandler implements JadeProcessEntityInterface,
        JadeProcessEntityPropertySavable<PFProcessTraitementAnnuelEnum>, JadeProcessEntityDataFind {

    /**
     * id de la décision conservé dans getIdRef
     */
    private String id;
    private Map<PFProcessTraitementAnnuelEnum, String> mapSavedValue = new HashMap<PFProcessTraitementAnnuelEnum, String>();
    private Map<PFProcessTraitementAnnuelEnum, String> mapValueToSave = new HashMap<PFProcessTraitementAnnuelEnum, String>();
    private String moisProchainPaiement;

    public PFProcessTraitementAnnuelCalculEntityHandler(String moisProchainPaiement) {
        this.moisProchainPaiement = moisProchainPaiement;
    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        Demande demande = PerseusServiceLocator.getDemandeService().read(id);

        // But : Faire un checkcalcul en prenant la différence entre la date de naissance et la date du prochain
        // paiement
        String dateDebut = demande.getSimpleDemande().getDateDebut();
        demande.getSimpleDemande().setDateDebut("01." + moisProchainPaiement);

        if (mapSavedValue.get(PFProcessTraitementAnnuelEnum.TYPE_POPULATION).equals(
                PFProcessTraitementAnnuelEnum.OCTROI.toString())) {

            if (!PerseusServiceLocator.getDemandeService().checkCalculable(demande)) {
                demande.getSimpleDemande().setDateDebut(dateDebut); // On remet notre date avec la valeur initiale
                mapValueToSave.put(PFProcessTraitementAnnuelEnum.CAS_NON_CALCULABLE, demande.getSimpleDemande()
                        .getIdDemande());
                String erreurDemandeNonCalculable = BSessionUtil.getSessionFromThreadContext().getLabel(
                        "SERVICE_TRAITEMENT_DEMANDE_NON_CALCULABLE");
                JadeThread.logError(this.getClass().getName(), erreurDemandeNonCalculable + mapSavedValue.toString());
            } else {
                demande.getSimpleDemande().setDateDebut(dateDebut); // On remet notre date avec la valeur initiale
                Demande demandeCopie = copieDemande(demande, CSTypeDemande.REVISION_EXTRAORDINAIRE.getCodeSystem());

                mapValueToSave.put(PFProcessTraitementAnnuelEnum.ID_DEMANDE_COPIE, demandeCopie.getId());

                if (!JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                    // Calcul nouvelle demande
                    PerseusServiceLocator.getPCFAccordeeService().calculer(demandeCopie.getId());
                }
            }
        } else if (mapSavedValue.get(PFProcessTraitementAnnuelEnum.TYPE_POPULATION).equals(
                PFProcessTraitementAnnuelEnum.PARTIEL.toString())) {
            String warnCasPartiel = BSessionUtil.getSessionFromThreadContext().getLabel(
                    "SERVICE_TRAITEMENT_ANNUEL_WARN_CAS_PARTIEL");

            JadeThread.logWarn(this.getClass().getName(), warnCasPartiel);
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

    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        id = entity.getIdRef();
    }

    @Override
    public Map<PFProcessTraitementAnnuelEnum, String> getValueToSave() {
        return mapValueToSave;
    }

    @Override
    public void setData(Map hashMap) {
        mapSavedValue = hashMap;
    }

}
