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
import ch.globaz.al.businessimpl.processus.traitementimpl.PaiementDirectTraitement;
import ch.globaz.al.businessimpl.processus.traitementimpl.PreparationPaiementDirectTraitement;
import ch.globaz.al.businessimpl.processus.traitementimpl.SimulationPaiementDirectTraitement;

/**
 * 
 * Classe d'implémentation du processus des paiements directs
 * 
 * @author GMO
 * 
 */
public class PaiementDirectSansRecapProcessus extends BusinessProcessus {

    /**
     * constructeur du processus des paiements directs
     */
    public PaiementDirectSansRecapProcessus() {
        super();
    }

    @Override
    public String getCSProcessus() {
        return ALCSProcessus.NAME_PROCESSUS_PAIEMENT_DIRECT_SANS_RECAP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.processus.BusinessProcessus#initialize()
     */
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
        PreparationPaiementDirectTraitement traitement3 = new PreparationPaiementDirectTraitement();
        PaiementDirectTraitement traitement4 = new PaiementDirectTraitement();
        addTraitement(traitement1, true);
        addTraitement(traitement2, true);
        addTraitement(traitement3, true);
        addTraitement(traitement4, true);

    }
}
