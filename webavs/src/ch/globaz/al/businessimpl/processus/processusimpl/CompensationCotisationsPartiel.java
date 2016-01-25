package ch.globaz.al.businessimpl.processus.processusimpl;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

/**
 * Processus permettant de traiter un deuxi�me lot de compensation un mois donn�.
 * 
 * Ex�cute tous les traitement sauf la g�n�ration de prestation
 * 
 * @author gmo
 * 
 */
public class CompensationCotisationsPartiel extends CompensationCotisations {

    /**
     * Constructeur
     */
    public CompensationCotisationsPartiel() {
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
