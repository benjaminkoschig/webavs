package ch.globaz.pegasus.business.constantes;

import ch.globaz.jade.process.business.enumProcess.JadeProcessUtilsEnumProcess;

public enum EPCRenteAdaptation {

    ERROR("globaz.jade.process.entity.state.error", 2),
    NEW("globaz.jade.process.entity.state.error", 2),
    NOT_FOUND("globaz.jade.process.entity.state.error", 2),

    NOT_RETURN("globaz.jade.process.entity.state.comput", 1),

    RENTE_CHANGE("globaz.jade.process.entity.state.comput", 1),
    SYNCHRONIZE("globaz.jade.process.entity.state.comput", 1),
    SYNCHRONIZE_RENTE_CHANGE("globaz.jade.process.entity.state.comput", 1),
    UNABELD_TO_MAP("globaz.jade.process.entity.state.comput", 1),
    WAIT("globaz.jade.process.entity.state.comput", 1);

    private final String idLabel;
    private final int numCode;

    EPCRenteAdaptation(String idLabe, int num) {
        idLabel = idLabe;
        numCode = num;
    }

    public String toLabel() {
        return JadeProcessUtilsEnumProcess.getLabel(idLabel);
    }

    public void valueOf() {
        // TODO Auto-generated method stub

    }

}
