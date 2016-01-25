package ch.globaz.al.businessimpl.processus.processusimpl;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Date;
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
import ch.globaz.al.businessimpl.processus.traitementimpl.PaiementDirectTraitement;
import ch.globaz.al.businessimpl.processus.traitementimpl.PreparationPaiementDirectTraitement;
import ch.globaz.al.businessimpl.processus.traitementimpl.SimulationPaiementDirectTraitement;

/**
 * 
 * Classe d'impl�mentation du processus des paiements directs avec impression r�cap provisoire
 * 
 * @author GMO
 * 
 */
public class PaiementDirectRecapProv extends BusinessProcessus {

    /**
     * constructeur du processus des paiements directs
     */
    public PaiementDirectRecapProv() {
        super();
    }

    @Override
    public String getCSProcessus() {
        return ALCSProcessus.NAME_PROCESSUS_PAIEMENT_DIRECT_RECAP_PROV;
    }

    @Override
    public void initialize() throws JadeApplicationException, JadePersistenceException {
        ProcessusDatasCriteria dataCriterias = new ProcessusDatasCriteria();
        PeriodeAFModel periodeModel;

        periodeModel = ALServiceLocator.getPeriodeAFModelService().read(getProcessusPeriodiqueModel().getIdPeriode());

        dataCriterias.periodeCriteria = periodeModel.getDatePeriode();

        dataCriterias.bonificationCriteria = ALCSPrestation.BONI_DIRECT;
        dataCriterias.cotisationCriteria = ALConstPrestations.TYPE_DIRECT;
        dataCriterias.fullDateCriteria = JadeDateUtil.getGlobazFormattedDate(new Date());
        setDataCriterias(dataCriterias);

        // initialisation des traitements du processus
        GenerationPrestationsTraitement traitement1 = new GenerationPrestationsTraitement();
        SimulationPaiementDirectTraitement traitement2 = new SimulationPaiementDirectTraitement();
        ImpressionRecapsProvisoiresTraitement traitement3 = new ImpressionRecapsProvisoiresTraitement();
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
