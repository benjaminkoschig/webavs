/**
 *
 */
package ch.globaz.vulpecula.process.decompte;

import globaz.jade.context.JadeThread;

/**
 * Nom des champs à persister
 * @author sel
 * 
 */
public enum PTProcessDecompteProperty {
    beforeNoAffilie("beforeNoAffilie"),
    beforeNoAffilieLibelle("vulpecula.process.decompte.beforeNoAffilieLibelle"),
    ANNEE("vulpecula.process.decompte.ANNEE"),
    CONVENTIONS("vulpecula.process.decompte.conventions"),
    DATE_DECOMPTE("vulpecula.process.decompte.DATE_DECOMPTE"),
    DATE_PERIODE_A("vulpecula.process.decompte.DATE_PERIODE_A"),
    DATE_PERIODE_DE("vulpecula.process.decompte.DATE_PERIODE_DE"),
    PERIODICITE("vulpecula.process.decompte.PERIODICITE"),
    employeurNumero("vulpecula.process.decompte.employeurNumero"),
    employeurNumeroLibelle("vulpecula.process.decompte.employeurNumeroLibelle"),
    fromNoAffilie("vulpecula.process.decompte.fromNoAffilie"),
    fromNoAffilieLibelle("vulpecula.process.decompte.fromNoAffilieLibelle"),
    TYPE_DECOMPTE("vulpecula.process.decompte.TYPE_DECOMPTE"),
    EBUSINESS("vulpecula.process.decompte.EBUSINESS"),
    ID_DECOMPTE("ID_DECOMPTE");

    private final String idLabel;

    PTProcessDecompteProperty(final String idLabel) {
        this.idLabel = idLabel;
    }

    public String toLabel() {
        return JadeThread.getMessage(idLabel);
    }
}