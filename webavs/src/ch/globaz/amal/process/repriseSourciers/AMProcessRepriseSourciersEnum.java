/**
 * 
 */
package ch.globaz.amal.process.repriseSourciers;

import globaz.jade.context.JadeThread;

/**
 * @author dhi
 * 
 */
public enum AMProcessRepriseSourciersEnum {
    ANNEE("amal.process.repriseSourciers.anneeSubside"),
    ANNEE_TAXATION("amal.process.repriseSourciers.anneeTaxation"),
    CHECKBOXVALIDATE("amal.process.repriseSourciers.fileToValidate"),
    DATE_AVIS_TAXATION("amal.process.repriseSourciers.dateAvisTaxation"),
    FILENAME("amal.process.repriseSourciers.nomFichier");

    private final String idLabel;

    AMProcessRepriseSourciersEnum(String idLabel) {
        this.idLabel = idLabel;
    }

    public String toLabel() {
        return JadeThread.getMessage(idLabel);
    }

}
