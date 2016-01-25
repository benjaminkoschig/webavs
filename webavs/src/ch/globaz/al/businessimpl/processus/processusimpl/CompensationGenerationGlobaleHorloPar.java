package ch.globaz.al.businessimpl.processus.processusimpl;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.businessimpl.processus.ProcessusDatasCriteria;

public class CompensationGenerationGlobaleHorloPar extends CompensationGenerationGlobaleHorlo {
    /**
     * Constructeur du processus
     */
    public CompensationGenerationGlobaleHorloPar() {
        super();
    }

    @Override
    public String getCSProcessus() {
        return ALCSProcessus.NAME_PROCESSUS_FACTURATION_HORLO_PAR;
    }

    @Override
    public void initialize() throws JadeApplicationException, JadePersistenceException {
        // même initialisation que le processus
        super.initialize();

        ProcessusDatasCriteria criterias = super.getDataCriterias();
        criterias.cotisationCriteria = ALConstPrestations.TYPE_COT_PAR;
        setDataCriterias(criterias);

    }
}
