package ch.globaz.al.businessimpl.processus.processusimpl;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.businessimpl.processus.traitementimpl.PaiementDirectTraitement;
import ch.globaz.al.businessimpl.processus.traitementimpl.PreparationPaiementDirectTraitement;
import ch.globaz.al.businessimpl.processus.traitementimpl.SimulationPaiementDirectTraitement;

public class PaiementDirectGenerationGlobaleHorloPartiel extends PaiementDirectGenerationGlobaleHorlo {

    public PaiementDirectGenerationGlobaleHorloPartiel() {
        super();
        setPartiel(true);
    }

    @Override
    public void initialize() throws JadeApplicationException, JadePersistenceException {
        super.initialize();
        // le partiel des horlogères est totalement différent du principal
        getListeTraitements().clear();

        // initialisation des traitements du processus

        SimulationPaiementDirectTraitement traitement1 = new SimulationPaiementDirectTraitement();
        PreparationPaiementDirectTraitement traitement2 = new PreparationPaiementDirectTraitement();
        PaiementDirectTraitement traitement3 = new PaiementDirectTraitement();
        traitement1.setProcessusConteneur(this);
        traitement2.setProcessusConteneur(this);
        traitement3.setProcessusConteneur(this);
        addTraitement(traitement1, true);
        addTraitement(traitement2, true);
        addTraitement(traitement3, true);

    }

}
