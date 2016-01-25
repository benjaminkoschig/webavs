package ch.globaz.al.businessimpl.processus.processusimpl;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.processus.BusinessProcessus;
import ch.globaz.al.businessimpl.processus.ProcessusDatasCriteria;
import ch.globaz.al.businessimpl.processus.traitementimpl.GenerationPrestationsTraitement;
import ch.globaz.al.businessimpl.processus.traitementimpl.ImpressionRecapsProvisoiresTraitement;

public class CompensationGenerationGlobaleHorlo extends BusinessProcessus {

    @Override
    public String getCSProcessus() {
        return ALCSProcessus.NAME_PROCESSUS_FACTURATION_HORLO;
    }

    @Override
    public void initialize() throws JadeApplicationException, JadePersistenceException {
        // Initialisation du filtre des donn�es � traiter par les traitements du
        // processus

        ProcessusDatasCriteria dataCriterias = new ProcessusDatasCriteria();
        PeriodeAFModel periodeModel;

        periodeModel = ALServiceLocator.getPeriodeAFModelService().read(getProcessusPeriodiqueModel().getIdPeriode());

        dataCriterias.periodeCriteria = periodeModel.getDatePeriode();
        dataCriterias.cotisationCriteria = ALConstPrestations.TYPE_INDIRECT_GROUPE;
        setDataCriterias(dataCriterias);

        // initialisation des traitements du processus
        GenerationPrestationsTraitement traitement1 = new GenerationPrestationsTraitement();
        traitement1.setProcessusConteneur(this);
        ImpressionRecapsProvisoiresTraitement traitement2 = new ImpressionRecapsProvisoiresTraitement();
        traitement2.setProcessusConteneur(this);
        addTraitement(traitement1, true);
        addTraitement(traitement2, true);

    }

}
