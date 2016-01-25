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
public enum RafamReturnCode {
    ANNULEE(3),
    EN_ATTENTE(4),
    EN_ERREUR(1),
    NON_TRAITE(99),
    RAPPEL(5),
    REJETEE(2),
    TRAITE(0);

    public static RafamReturnCode getRafamReturnCode(String code) throws JadeApplicationException {

        switch (Integer.parseInt(code)) {
            case 0:
                return TRAITE;
            case 1:
                return EN_ERREUR;
            case 2:
                return REJETEE;
            case 3:
                return ANNULEE;
            case 4:
                return EN_ATTENTE;
            case 5:
                return RAPPEL;
            case 99:
                return NON_TRAITE;
            default:
                throw new ALRafamException("RafamReturnCode#getRafamReturnCode : this type is not supported");
        }

    }

    private int etat;

    RafamReturnCode(int etat) {
        this.etat = etat;
    }

    public String getCode() {
        return String.valueOf(etat);
    }

    public String getCodeLibelle() {
        return "al.enum.rafam.returnCode.code" + getCode();
    }

    public String getLibelle() {
        return JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), getCodeLibelle());
    }
}
