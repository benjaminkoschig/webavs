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
import ch.globaz.al.businessimpl.processus.traitementimpl.GenerationPrestationsTraitement;
import ch.globaz.al.businessimpl.processus.traitementimpl.ImpressionRecapsProvisoiresTraitement;

public class PaiementDirectGenerationGlobaleHorlo extends BusinessProcessus {

    @Override
    public String getCSProcessus() {
        return ALCSProcessus.NAME_PROCESSUS_DIRECT_GENERATION_GLOBALE;
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
        GenerationPrestationsTraitement traitement1 = new GenerationPrestationsTraitement();
        traitement1.setProcessusConteneur(this);
        ImpressionRecapsProvisoiresTraitement traitement2 = new ImpressionRecapsProvisoiresTraitement();
        traitement2.setProcessusConteneur(this);
        addTraitement(traitement1, true);
        addTraitement(traitement2, true);

    }

}
