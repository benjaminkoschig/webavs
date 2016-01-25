package ch.globaz.al.businessimpl.processus.processusimpl;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

/**
 * Processus permettant de traiter un deuxième lot de paiement direct avec récap prov un mois donné.
 * 
 * Exécute tous les traitement sauf la génération de prestation
 * 
 * @author gmo
 * 
 */
public class PaiementDirectRecapProvPartiel extends PaiementDirectRecapProv {

    public PaiementDirectRecapProvPartiel() {
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
