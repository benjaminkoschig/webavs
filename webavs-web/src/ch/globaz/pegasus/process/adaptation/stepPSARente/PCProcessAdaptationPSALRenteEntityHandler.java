package ch.globaz.pegasus.process.adaptation.stepPSARente;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.process.AdaptationException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere;
import ch.globaz.pegasus.business.models.process.adaptation.AdaptationPsal;
import ch.globaz.pegasus.business.models.revenusdepenses.CotisationsPsal;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;
import ch.globaz.pegasus.process.adaptation.PCProcessDroitUpdateAbsract;

public class PCProcessAdaptationPSALRenteEntityHandler extends PCProcessDroitUpdateAbsract {

    private Map<String, List<AdaptationPsal>> psal;

    public PCProcessAdaptationPSALRenteEntityHandler(Map<String, List<AdaptationPsal>> psal) {
        this.psal = psal;
    }

    private ModificateurDroitDonneeFinanciere createDroitModificateur(AdaptationPsal psal, Droit droitACalculer) {
        ModificateurDroitDonneeFinanciere droitModificateur = new ModificateurDroitDonneeFinanciere();
        droitModificateur.setSimpleDroit(droitACalculer.getSimpleDroit());
        droitModificateur.setSimpleVersionDroit(droitACalculer.getSimpleVersionDroit());
        droitModificateur.setSimpleDroitMembreFamille(psal.getSimpleDroitMembreFamille());
        return droitModificateur;
    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        if ("true".equalsIgnoreCase(properties.get(PCProcessAdapationEnum.HAS_ADAPTATION_DES_PSAL))) {
            fillDroitToUpdate();

            if (psal.containsKey(entity.getIdRef())) {
                updatePsal(droitACalculer, psal.get(entity.getIdRef()));

            }
        }
    }

    private void updatePsal(Droit droit, List<AdaptationPsal> listPsalToUpdate) throws CotisationsPsalException,
            DroitException, DonneeFinanciereException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, AdaptationException {

        for (AdaptationPsal adaptationPsal : listPsalToUpdate) {
            ModificateurDroitDonneeFinanciere droitModificateur = createDroitModificateur(adaptationPsal, droit);

            CotisationsPsal oldCotisationsPsal = new CotisationsPsal();
            oldCotisationsPsal.setSimpleCotisationsPsal(adaptationPsal.getSimpleCotisationsPsal());
            oldCotisationsPsal.setSimpleDonneeFinanciereHeader(adaptationPsal.getSimpleDonneeFinanciereHeader());
            CotisationsPsal newCotisationsPsal;
            try {
                newCotisationsPsal = (CotisationsPsal) JadePersistenceUtil.clone(oldCotisationsPsal);
            } catch (JadeCloneModelException e) {
                throw new AdaptationException("Unalbe to clone the psal", e);
            }

            newCotisationsPsal.getSimpleCotisationsPsal().setMontantCotisationsAnnuelles(
                    properties.get(PCProcessAdapationEnum.PSAL_MONTANT_NOUVEAU));

            newCotisationsPsal.getSimpleDonneeFinanciereHeader().setDateDebut(
                    properties.get(PCProcessAdapationEnum.DATE_ADAPTATION));

            newCotisationsPsal = PegasusServiceLocator.getDroitService().createAndCloseCotisationsPsal(
                    droitModificateur, newCotisationsPsal, oldCotisationsPsal, false);
            dataToSave.put(PCProcessAdapationEnum.ID_PSAL_UPDATED, adaptationPsal.getSimpleDonneeFinanciereHeader()
                    .getIdDonneeFinanciereHeader());

        }
    }

}
