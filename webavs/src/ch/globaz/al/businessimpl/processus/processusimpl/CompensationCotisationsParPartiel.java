package ch.globaz.al.businessimpl.processus.processusimpl;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

public class CompensationCotisationsParPartiel extends CompensationCotisationsPar {

    /**
     * Constructeur
     */
    public CompensationCotisationsParPartiel() {
        super();
        setPartiel(true);
    }

    @Override
    public void initialize() throws JadeApplicationException, JadePersistenceException {
        super.initialize();
        // on enl�ve le premier traitement soit la g�n�ration globale.
        removeTraitement(0);
    }
}
