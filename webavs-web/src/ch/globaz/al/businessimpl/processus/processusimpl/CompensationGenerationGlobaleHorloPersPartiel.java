package ch.globaz.al.businessimpl.processus.processusimpl;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.businessimpl.processus.traitementimpl.PreparationCompensationTraitement;
import ch.globaz.al.businessimpl.processus.traitementimpl.SimulationCompensationTraitement;

public class CompensationGenerationGlobaleHorloPersPartiel extends CompensationGenerationGlobaleHorloPers {
    public CompensationGenerationGlobaleHorloPersPartiel() {
        super();
        setPartiel(true);
    }

    @Override
    public void initialize() throws JadeApplicationException, JadePersistenceException {
        super.initialize();
        // le partiel des horlogères est totalement différent du principal
        getListeTraitements().clear();
        SimulationCompensationTraitement traitement1 = new SimulationCompensationTraitement();
        traitement1.setProcessusConteneur(this);
        PreparationCompensationTraitement traitement2 = new PreparationCompensationTraitement();
        traitement2.setProcessusConteneur(this);
        addTraitement(traitement1, true);
        addTraitement(traitement2, true);

    }
}
