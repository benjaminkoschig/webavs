package ch.globaz.common.domaine.Echeance;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;

public class CodeSystemeEnumUtils {

    private CodeSystemeEnumUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Retourne l'�num�r� correspondant au code syst�me. Renvoie une {@link IllegalArgumentException} si la cha�ne de
     * caract�re pass�e en param�tre est invalide (null ou vide) ou si le code syst�me ne correspond pas � un valeur de
     * cette �num�r�.
     * 
     * @param <T>
     * @param codeSysteme
     * @return
     */
    public static <T> T parse(String codeSysteme, Class<? extends CodeSysteme<?>> enumm) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("[codeSysteme:" + codeSysteme
                    + "] must be made only of digits for this enum " + enumm);
        }

        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return CodeSystemeEnumUtils.valueOf(intCodeSysteme, enumm);
    }

    /**
     * Retourne l'�num�r� correspondant au code syst�me. Renvoie une {@link IllegalArgumentException} si le param�tre
     * est null ou si le code syst�me ne correspond pas � un valeur de cette �num�r�.
     * 
     * @param string
     * @return
     */
    public static <T> T valueOf(Integer codeSysteme, Class<? extends CodeSysteme<?>> enumClass) {
        if (codeSysteme == null) {
            throw new IllegalArgumentException("[codeSysteme:" + codeSysteme + "] can't be null, for this enum "
                    + enumClass);
        }
        for (CodeSysteme<?> type : enumClass.getEnumConstants()) {
            if (type.getCodeSysteme().equals(codeSysteme.toString())) {
                return (T) type;
            }
        }
        throw new IllegalArgumentException("[codeSysteme:" + codeSysteme
                + "] doesn't match any known value, for this enum " + enumClass);
    }

    /**
     * Permet de trouver le libelle internationaliser Attention cette fonction fonction seulement quand on utilise la
     * nouvelle persistence
     * 
     * @param codeSystement
     * @return le libelle dans la langue de l'utilisateur
     */
    public static String i18nLibelle(CodeSysteme codeSystement) {
        return BSessionUtil.getSessionFromThreadContext().getCodeLibelle(codeSystement.getCodeSysteme());
    }
}
