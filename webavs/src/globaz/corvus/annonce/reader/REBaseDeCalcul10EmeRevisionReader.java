package globaz.corvus.annonce.reader;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Cette classe poss�de des m�thodes utilitaires pour lire les valeurs qui format�es contenues dans les base de calculs
 * 10�me r�vision
 * des rentes
 * 
 * @author lga
 * 
 */
public class REBaseDeCalcul10EmeRevisionReader extends REAbstractBaseDeCalculReader {

    /**
     * REBACAL.YIDABE
     * Retourne la valeur enti�re du nombre d'ann�e de bonification pout t�che �ducative
     * Format : AAMM (AA -> nombre d'ann�e, MM -> fraction d'ann�e, ex : 0466 = 4 ann�e et 2/3)
     * Retourne la valeur AA
     * 
     * @param anneeBonifTacheEduc le nombre d'ann�es de bonification pout t�che �ducative
     * @return la valeur enti�re du nombre d'ann�e de bonification pout t�che �ducative ou null si
     *         <code>anneeBonifTacheEduc</code> est null, vide ou ne poss�de pas le bon format (4 caract�re num�rique)
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
     * Retourne la fraction d'ann�e de bonification pout t�che �ducative
     * Format : AAMM (AA -> nombre d'ann�e, MM -> fraction d'ann�e, ex : 0466 = 4 ann�e et 2/3)
     * Retourne la valeur MM
     * 
     * @param anneeBonifTacheEduc la fraction d'ann�es de bonification pout t�che �ducative
     * @return Retourne la fraction d'ann�e de bonification pout t�che �ducative ou null si
     *         <code>anneeBonifTacheEduc</code> est
     *         null, vide ou n'a pas le bon format (4 caract�re num�rique)
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
     * Retourne la valeur enti�re du nombre d'ann�e de BTA
     * Format : AAMM (AA -> nombre d'ann�e, MM -> fraction d'ann�e, ex : 0466 = 4 ann�e et 2/3)
     * Retourne la valeur AA
     * 
     * @param bta le nombre d'ann�es de bonification pout t�che �ducative
     * @return la valeur enti�re du nombre d'ann�e de BTA ou null si <code>bta</code> est null, vide ou ne poss�de pas
     *         le bon format (4 caract�re num�rique)
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
     * Retourne la fraction d'ann�e de BTA
     * Format : AAMM (AA -> nombre d'ann�e, MM -> fraction d'ann�e, ex : 0466 = 4 ann�e et 2/3)
     * Retourne la valeur MM
     * 
     * @param bta la fraction d'ann�es de bta
     * @return Retourne la fraction d'ann�e de bonification pout t�che �ducative ou null si <code>bta</code> est
     *         null, vide ou n'a pas le bon format (4 caract�re num�rique)
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
     * Format xxx (x est un num�rique) 3 positions
     * En 9�me le code est sur 2 positions, en 10�me un 3 est ajout� devant
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
