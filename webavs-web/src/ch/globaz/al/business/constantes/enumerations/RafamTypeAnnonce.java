package ch.globaz.al.business.constantes.enumerations;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.i18n.JadeI18n;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;

/**
 * 
 * @author jts
 * 
 */
public enum RafamTypeAnnonce {
    _68A_CREATION("68a"),
    _68B_MUTATION("68b"),
    _68C_ANNULATION("68c"),
    _69A_RECEIPT("69a"),
    _69B_SYNCHRO_UPI("69b"),
    _69C_REGISTER_STATUS("69c"),
    _69D_NOTICE("69d");

    public static RafamTypeAnnonce getRafamTypeAnnonce(String code) throws JadeApplicationException {

        if ("68a".equals(code)) {
            return _68A_CREATION;
        } else if ("68b".equals(code)) {
            return _68B_MUTATION;
        } else if ("68c".equals(code)) {
            return _68C_ANNULATION;
        } else if ("69a".equals(code)) {
            return _69A_RECEIPT;
        } else if ("69b".equals(code)) {
            return _69B_SYNCHRO_UPI;
        } else if ("69c".equals(code)) {
            return _69C_REGISTER_STATUS;
        } else if ("69d".equals(code)) {
            return _69D_NOTICE;
        } else {
            throw new ALRafamException("RafamTypeAnnonce#getRafamTypeAnnonce : this type is not supported");
        }
    }

    private String code;

    RafamTypeAnnonce(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getCodeLibelle() {
        return "al.enum.rafam.type." + getCode();
    }

    public String getLibelle() {
        return JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), getCodeLibelle());
    }
}
