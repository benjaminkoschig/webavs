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
        // on enlève le premier traitement soit la génération globale.
        removeTraitement(0);
    }
}
