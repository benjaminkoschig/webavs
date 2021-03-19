package ch.globaz.pegasus.process.adaptation;

import ch.globaz.pegasus.primeassurancemaladie.PrimeAssuranceMaladieFromCSV;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityPropertySavable;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.process.util.AbstractEntity;

public abstract class PCProcessDroitUpdateAbsract extends AbstractEntity implements
        JadeProcessEntityPropertySavable<PCProcessAdapationEnum>, JadeProcessEntityNeedProperties {
    protected Droit currentDroit = null;
    protected Map<PCProcessAdapationEnum, String> dataToSave = new HashMap<PCProcessAdapationEnum, String>();
    protected Droit droitACalculer = null;
    protected Boolean hasDeleteVersionDroit = false;
    protected Map<Enum<?>, String> properties = null;

    public void fillDroitToUpdate() throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DemandeException, DossierException {

        // remplie le droit courant et définit le droit à calculer
        Droit[] droits = PCAdaptationUtils.findUpdatableDroits(entity.getIdRef(),
                "01." + properties.get(PCProcessAdapationEnum.DATE_ADAPTATION).trim(),
                IPCDroits.CS_MOTIF_DROIT_ADAPTATION, dataToSave);

        currentDroit = droits[0];
        droitACalculer = droits[1];
        if (dataToSave.containsKey(PCProcessAdapationEnum.HAS_DELETE_VERSION_DROIT)
                && "true".equalsIgnoreCase(dataToSave.get(PCProcessAdapationEnum.HAS_DELETE_VERSION_DROIT))) {
            hasDeleteVersionDroit = true;
        }
    }

    /**
     * Méthode pour récupérer uniquement les droits validés et ne pas supprimer les droits en cours (Au calcul / Calculé / Enregistré)
     *
     * @param listePrimeFromCsv
     * @throws DroitException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DemandeException
     * @throws DossierException
     */
    public void fillDroitToUpdateForPrimeLAMal(Map<String, PrimeAssuranceMaladieFromCSV> listePrimeFromCsv) throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DemandeException, DossierException {

        // remplie le droit courant et définit le droit à calculer
        Droit[] droits = PCAdaptationUtils.findUpdatableDroitsForPrimeLAMal(entity.getIdRef(),
                "01." + properties.get(PCProcessAdapationEnum.DATE_ADAPTATION).trim(),
                IPCDroits.CS_MOTIF_DROIT_ADAPTATION, listePrimeFromCsv);

        currentDroit = droits[0];
        droitACalculer = droits[1];
        if (dataToSave.containsKey(PCProcessAdapationEnum.HAS_DELETE_VERSION_DROIT)
                && "true".equalsIgnoreCase(dataToSave.get(PCProcessAdapationEnum.HAS_DELETE_VERSION_DROIT))) {
            hasDeleteVersionDroit = true;
        }
    }

    public Map<Enum<?>, String> getProperties() {
        return properties;
    }

    @Override
    public Map<PCProcessAdapationEnum, String> getValueToSave() {
        return dataToSave;
    }

    @Override
    public void setProperties(Map<Enum<?>, String> map) {
        properties = map;
    }

    public void setValueToSave(Map<PCProcessAdapationEnum, String> map) {
        dataToSave = map;
    }
}
