package ch.globaz.al.businessimpl.processus.processusimpl;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.businessimpl.processus.ProcessusDatasCriteria;

public class CompensationCotisationsPers extends CompensationCotisations {
    /**
     * Constructeur du processus
     */
    public CompensationCotisationsPers() {
        super();
    }

    @Override
    public String getCSProcessus() {
        return ALCSProcessus.NAME_PROCESSUS_COMPENSATION_PERS;
    }

    @Override
    public void initialize() throws JadeApplicationException, JadePersistenceException {
        // m�me initialisation que le processus
        super.initialize();

        ProcessusDatasCriteria criterias = super.getDataCriterias();
        criterias.cotisationCriteria = ALConstPrestations.TYPE_COT_PERS;
        setDataCriterias(criterias);
        // on retire le dernier traitement ( pas d'impression de r�caps pour les pers)
        // FIXME: v�rifier avec CCVD
        removeTraitement(getListeTraitements().size() - 1);

    }
}
