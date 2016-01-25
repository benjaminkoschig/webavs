package ch.globaz.al.businessimpl.processus.processusimpl;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Date;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.processus.BusinessProcessus;
import ch.globaz.al.businessimpl.processus.ProcessusDatasCriteria;
import ch.globaz.al.businessimpl.processus.traitementimpl.GenerationFictivePrestationsTraitement;
import ch.globaz.al.businessimpl.processus.traitementimpl.GenerationPrestationsTraitement;
import ch.globaz.al.businessimpl.processus.traitementimpl.ImpressionRecapsTraitement;
import ch.globaz.al.businessimpl.processus.traitementimpl.PaiementDirectTraitement;
import ch.globaz.al.businessimpl.processus.traitementimpl.PreparationPaiementDirectTraitement;
import ch.globaz.al.businessimpl.processus.traitementimpl.SimulationPaiementDirectTraitement;

public class PaiementDirectGenerationFictiveRecap extends BusinessProcessus {

    @Override
    public String getCSProcessus() {
        return ALCSProcessus.NAME_PROCESSUS_DIRECT_GENERATION_FICTIVE_RECAP;
    }

    @Override
    public void initialize() throws JadeApplicationException, JadePersistenceException {
        // Initialisation du filtre des données à traiter par les traitements du
        // processus

        ProcessusDatasCriteria dataCriterias = new ProcessusDatasCriteria();
        PeriodeAFModel periodeModel;

        periodeModel = ALServiceLocator.getPeriodeAFModelService().read(getProcessusPeriodiqueModel().getIdPeriode());

        dataCriterias.periodeCriteria = periodeModel.getDatePeriode();
        dataCriterias.cotisationCriteria = ALConstPrestations.TYPE_DIRECT;
        dataCriterias.fullDateCriteria = JadeDateUtil.getGlobazFormattedDate(new Date());
        setDataCriterias(dataCriterias);

        // initialisation des traitements du processus
        GenerationFictivePrestationsTraitement traitement1 = new GenerationFictivePrestationsTraitement();
        // initialisation des traitements du processus
        GenerationPrestationsTraitement traitement2 = new GenerationPrestationsTraitement();
        SimulationPaiementDirectTraitement traitement3 = new SimulationPaiementDirectTraitement();
        PreparationPaiementDirectTraitement traitement4 = new PreparationPaiementDirectTraitement();
        ImpressionRecapsTraitement traitement5 = new ImpressionRecapsTraitement();
        PaiementDirectTraitement traitement6 = new PaiementDirectTraitement();
        addTraitement(traitement1, true);
        addTraitement(traitement2, true);
        addTraitement(traitement3, true);
        addTraitement(traitement4, true);
        addTraitement(traitement5, true);
        addTraitement(traitement6, true);

    }

}
