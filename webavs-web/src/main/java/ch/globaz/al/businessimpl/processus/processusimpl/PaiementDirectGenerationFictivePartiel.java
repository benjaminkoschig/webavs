package ch.globaz.al.businessimpl.processus.processusimpl;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

public class PaiementDirectGenerationFictivePartiel extends PaiementDirectGenerationFictive {
    /**
     * Constructeur
     */
    public PaiementDirectGenerationFictivePartiel() {
        super();
        setPartiel(true);
    }

    @Override
    public void initialize() throws JadeApplicationException, JadePersistenceException {
        super.initialize();
        // on enl�ve les 2 premiers traitements soit la g�n�ration fictive et globale
        removeTraitement(0);
        removeTraitement(0);
    }
}
