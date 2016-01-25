/**
 * 
 */
package ch.globaz.amal.process.repriseFinAnnee;

import globaz.jade.context.JadeThread;

/**
 * @author dhi
 * 
 */
public enum AMProcessRepriseFinAnneeEnum {
    ANNEE("amal.process.repriseFinAnnee.anneeSubside");

    private final String idLabel;

    AMProcessRepriseFinAnneeEnum(String idLabel) {
        this.idLabel = idLabel;
    }

    public String toLabel() {
        return JadeThread.getMessage(idLabel);
    }

}
