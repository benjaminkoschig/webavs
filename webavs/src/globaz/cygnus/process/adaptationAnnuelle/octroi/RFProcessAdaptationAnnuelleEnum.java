package globaz.cygnus.process.adaptationAnnuelle.octroi;

import globaz.jade.context.JadeThread;

public enum RFProcessAdaptationAnnuelleEnum {

    EMAIL("email"),
    GESTIONNAIRE("gestionnaire");

    private final String idLabel;

    RFProcessAdaptationAnnuelleEnum(String idLabel) {
        this.idLabel = idLabel;
    }

    public String toLabel() {
        return JadeThread.getMessage(idLabel);
    }
}
