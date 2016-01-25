package ch.globaz.al.businessimpl.processus.processusimpl;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.processus.BusinessProcessus;
import ch.globaz.al.businessimpl.processus.ProcessusDatasCriteria;
import ch.globaz.al.businessimpl.processus.traitementimpl.GenerationPrestationsTraitement;
import ch.globaz.al.businessimpl.processus.traitementimpl.ImpressionRecapsProvisoiresTraitement;
import ch.globaz.al.businessimpl.processus.traitementimpl.ImpressionRecapsTraitement;
import ch.globaz.al.businessimpl.processus.traitementimpl.PreparationCompensationTraitement;
import ch.globaz.al.businessimpl.processus.traitementimpl.SimulationCompensationTraitement;

public class CompensationCotisationsRecapProv extends BusinessProcessus {

    @Override
    public String getCSProcessus() {
        return ALCSProcessus.NAME_PROCESSUS_COMPENSATION_RECAP_PROV;
    }

    @Override
    public void initialize() throws JadeApplicationException, JadePersistenceException {
        ProcessusDatasCriteria dataCriterias = new ProcessusDatasCriteria();
        PeriodeAFModel periodeModel;

        periodeModel = ALServiceLocator.getPeriodeAFModelService().read(getProcessusPeriodiqueModel().getIdPeriode());

        dataCriterias.periodeCriteria = periodeModel.getDatePeriode();

        dataCriterias.bonificationCriteria = ALCSPrestation.BONI_INDIRECT;
        dataCriterias.cotisationCriteria = ALConstPrestations.TYPE_INDIRECT_GROUPE;
        setDataCriterias(dataCriterias);

        // initialisation des traitements du processus
        GenerationPrestationsTraitement traitement1 = new GenerationPrestationsTraitement();
        traitement1.setProcessusConteneur(this);
        SimulationCompensationTraitement traitement2 = new SimulationCompensationTraitement();
        traitement2.setProcessusConteneur(this);
        ImpressionRecapsProvisoiresTraitement traitement3 = new ImpressionRecapsProvisoiresTraitement();
        traitement3.setProcessusConteneur(this);
        PreparationCompensationTraitement traitement4 = new PreparationCompensationTraitement();
        traitement4.setProcessusConteneur(this);
        ImpressionRecapsTraitement traitement5 = new ImpressionRecapsTraitement();
        traitement5.setProcessusConteneur(this);
        addTraitement(traitement1, true);
        addTraitement(traitement2, true);
        addTraitement(traitement3, true);
        addTraitement(traitement4, true);
        addTraitement(traitement5, true);

    }

}
