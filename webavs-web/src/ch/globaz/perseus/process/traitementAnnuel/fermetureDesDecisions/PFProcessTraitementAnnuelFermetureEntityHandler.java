package ch.globaz.perseus.process.traitementAnnuel.fermetureDesDecisions;

import globaz.globall.db.BSessionUtil;
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
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.process.traitementAnnuel.PFProcessTraitementAnnuelEnum;

public class PFProcessTraitementAnnuelFermetureEntityHandler implements JadeProcessEntityInterface,
        JadeProcessEntityPropertySavable<PFProcessTraitementAnnuelEnum>, JadeProcessEntityDataFind {

    /**
     * id de la décision conservé dans getIdRef
     */
    private String id;
    private Map<PFProcessTraitementAnnuelEnum, String> mapSavedValue = new HashMap<PFProcessTraitementAnnuelEnum, String>();
    private Map<PFProcessTraitementAnnuelEnum, String> mapValueToSave = new HashMap<PFProcessTraitementAnnuelEnum, String>();
    private String moisDernierPaiement;

    public PFProcessTraitementAnnuelFermetureEntityHandler(String moisProchainPaiement) {
        moisDernierPaiement = moisProchainPaiement;
    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        Demande demande = PerseusServiceLocator.getDemandeService().read(id);

        if (PerseusServiceLocator.getDemandeService().iSConjointDossier(demande)) {
            String erreurConjoint = BSessionUtil.getSessionFromThreadContext().getLabel(
                    "PROCESS_PF_TRAITEMENTADAPTATION_ERROR_CONJOINT");
            JadeThread.logError(this.getClass().getName(), erreurConjoint
                    + mapSavedValue.toString()
                    + ", NSS du conjoint à double : "
                    + demande.getSituationFamiliale().getConjoint().getMembreFamille().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel());
        } else {
            fermerDemandeOriginal(demande);
        }
    }

    private void fermerDemandeOriginal(Demande demande) throws JadePersistenceException, DemandeException,
            JadeApplicationServiceNotAvailableException, SituationFamilleException {
        // fermer la demande
        String dateFin = "";
        dateFin = moisDernierPaiement;

        dateFin = "31." + dateFin;

        demande.getSimpleDemande().setDateFin(dateFin);
        PerseusServiceLocator.getDemandeService().update(demande);

        mapValueToSave.put(PFProcessTraitementAnnuelEnum.DEMANDE_FERME,
                PFProcessTraitementAnnuelEnum.DEMANDE_FERME.toString());
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
