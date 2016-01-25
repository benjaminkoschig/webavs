package ch.globaz.al.businessimpl.processus.processusimpl;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.businessimpl.processus.BusinessProcessus;
import ch.globaz.al.businessimpl.processus.traitementimpl.ImpressionAvisEcheances;

/**
 * 
 * Classe d'implémentation du processus des échéances
 * 
 * @author GMO
 * 
 */
public class EcheancesProcessus extends BusinessProcessus {

    /**
     * Constructeur du processus
     */
    public EcheancesProcessus() {
        super();

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.processus.BusinessProcessus#getLabelProcessus()
     */
    @Override
    public String getCSProcessus() {
        return ALCSProcessus.NAME_PROCESSUS_ECHEANCES;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.processus.BusinessProcessus#initialize()
     */
    @Override
    public void initialize() throws JadePersistenceException, JadeApplicationException {
        ImpressionAvisEcheances traitement1 = new ImpressionAvisEcheances();
        addTraitement(traitement1, true);

    }

}
