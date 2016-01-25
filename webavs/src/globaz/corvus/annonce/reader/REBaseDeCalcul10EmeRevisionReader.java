package globaz.corvus.annonce.reader;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Cette classe possède des méthodes utilitaires pour lire les valeurs qui formatées contenues dans les base de calculs
 * 10ème révision
 * des rentes
 * 
 * @author lga
 * 
 */
public class REBaseDeCalcul10EmeRevisionReader extends REAbstractBaseDeCalculReader {

    /**
     * REBACAL.YIDABE
     * Retourne la valeur entière du nombre d'année de bonification pout tâche éducative
     * Format : AAMM (AA -> nombre d'année, MM -> fraction d'année, ex : 0466 = 4 année et 2/3)
     * Retourne la valeur AA
     * 
     * @param anneeBonifTacheEduc le nombre d'années de bonification pout tâche éducative
     * @return la valeur entière du nombre d'année de bonification pout tâche éducative ou null si
     *         <code>anneeBonifTacheEduc</code> est null, vide ou ne possède pas le bon format (4 caractère numérique)
     */
    public Integer readNombreAnneeBTE_valeurEntiere(String anneeBonifTacheEduc) {
        Integer result = null;
        if (!JadeStringUtil.isBlank(anneeBonifTacheEduc)) {
            String[] valeurs = anneeBonifTacheEduc.split("\\.");
            if (valeurs.length == 2 && JadeNumericUtil.isNumeric(valeurs[0])) {
                if (convertToInteger(valeurs[0]) > 0) {
                    result = convertToInteger(valeurs[0]);
                }
            }
        }
        return result;
    }

    /**
     * REBACAL.YIDABE
     * Retourne la fraction d'année de bonification pout tâche éducative
     * Format : AAMM (AA -> nombre d'année, MM -> fraction d'année, ex : 0466 = 4 année et 2/3)
     * Retourne la valeur MM
     * 
     * @param anneeBonifTacheEduc la fraction d'années de bonification pout tâche éducative
     * @return Retourne la fraction d'année de bonification pout tâche éducative ou null si
     *         <code>anneeBonifTacheEduc</code> est
     *         null, vide ou n'a pas le bon format (4 caractère numérique)
     */
    public Integer readNombreAnneeBTE_valeurDecimal(String anneeBonifTacheEduc) {
        Integer result = null;
        if (!JadeStringUtil.isBlank(anneeBonifTacheEduc)) {
            String[] valeurs = anneeBonifTacheEduc.split("\\.");
            if (valeurs.length == 2 && JadeNumericUtil.isNumeric(valeurs[1])) {
                if (convertToInteger(valeurs[1]) > 0) {
                    result = convertToInteger(valeurs[1]);
                }
            }
        }
        return result;
    }

    /**
     * REBACAL.YIDABA
     * Retourne la valeur entière du nombre d'année de BTA
     * Format : AAMM (AA -> nombre d'année, MM -> fraction d'année, ex : 0466 = 4 année et 2/3)
     * Retourne la valeur AA
     * 
     * @param bta le nombre d'années de bonification pout tâche éducative
     * @return la valeur entière du nombre d'année de BTA ou null si <code>bta</code> est null, vide ou ne possède pas
     *         le bon format (4 caractère numérique)
     */
    @Override
    public Integer readNbreAnneeBTA_valeurEntiere(String bta) {
        Integer result = null;
        if (!JadeStringUtil.isBlank(bta)) {
            String[] valeurs = bta.split("\\.");
            if (valeurs.length == 2 && JadeNumericUtil.isNumeric(valeurs[0])) {
                if (convertToInteger(valeurs[0]) > 0) {
                    result = convertToInteger(valeurs[0]);
                }
            }
        }
        return result;
    }

    /**
     * REBACAL.YIDABA
     * Retourne la fraction d'année de BTA
     * Format : AAMM (AA -> nombre d'année, MM -> fraction d'année, ex : 0466 = 4 année et 2/3)
     * Retourne la valeur MM
     * 
     * @param bta la fraction d'années de bta
     * @return Retourne la fraction d'année de bonification pout tâche éducative ou null si <code>bta</code> est
     *         null, vide ou n'a pas le bon format (4 caractère numérique)
     */
    @Override
    public Integer readNbreAnneeBTA_valeurDecimal(String bta) {
        Integer result = null;
        if (!JadeStringUtil.isBlank(bta)) {
            String[] valeurs = bta.split("\\.");
            if (valeurs.length == 2 && JadeNumericUtil.isNumeric(valeurs[1])) {
                if (convertToInteger(valeurs[1]) > 0) {
                    result = convertToInteger(valeurs[1]);
                }
            }
        }
        return result;
    }

    /**
     * REBACAL.YINOAI
     * Format xxx (x est un numérique) 3 positions
     * En 9ème le code est sur 2 positions, en 10ème un 3 est ajouté devant
     * 
     * @param codeOfficeAi
     * @return
     */
    public Integer readOfficeAICompetent(String codeOfficeAi) {
        Integer value = null;
        if (JadeNumericUtil.isInteger(codeOfficeAi) && codeOfficeAi.length() <= 3) {
            value = Integer.valueOf(codeOfficeAi);
        }
        return value;
    }
}
