/**
 * 
 */
package ch.globaz.amal.process.repriseAdresses;

import globaz.jade.context.JadeThread;

/**
 * @author dhi
 * 
 */
public enum AMProcessRepriseAdressesEnum {
    FILENAME("amal.process.repriseAdresses.nomFichier");

    private final String idLabel;

    AMProcessRepriseAdressesEnum(String idLabel) {
        this.idLabel = idLabel;
    }

    public String toLabel() {
        return JadeThread.getMessage(idLabel);
    }

}
