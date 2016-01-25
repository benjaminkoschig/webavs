package globaz.corvus.annonce.reader;

import globaz.corvus.annonce.REIllegalNSSFormatException;
import globaz.corvus.annonce.RENSS;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Cette classe possède des méthodes utilitaires pour lire les valeurs qui formatées contenues dans les base de calculs
 * 9ème révision des rentes
 * 
 * @author lga
 * 
 */
public class REBaseDeCalcul9EmeRevisionReader extends REAbstractBaseDeCalculReader {

    public RENSS readNouveauNoAssureAyantDroit(String nouveauNoAssureAyantDroit) throws REIllegalNSSFormatException {
        return RENSS.convertFormattedNSS(nouveauNoAssureAyantDroit);
    }

    /**
     * REAAL3B.ZCMREV
     * Format DB : numérique 1 position
     * 
     * @param revenuPrisEnCompte
     * @return
     */
    public Integer readRevenuPrisEnCompte(String revenuPrisEnCompte) {
        return convertToInteger(revenuPrisEnCompte);
    }

    /**
     * REBACAL.YIBIRL
     * Format : Boolean
     * 
     * @param isLimiteRevenu
     * @return
     */
    public Boolean readIsLimiteRevenu(Boolean isLimiteRevenu) {
        return isLimiteRevenu;
    }

    /**
     * REBACAL.YIBIRG
     * Format : Boolean
     * 
     * @param isMinimumGaranti
     * @return
     */
    public Boolean readIsMinimumGaranti(Boolean isMinimumGaranti) {
        return isMinimumGaranti;
    }

    /**
     * REBACAL.YIMRAM
     * Format en DB : montant
     * 
     * @param revenuAnnuelMoyen
     * @return
     */
    public Integer readRevenuAnnuelMoyen(String revenuAnnuelMoyen) {
        return convertToInteger(revenuAnnuelMoyen);
    }

    /**
     * REBACAL.YIDABE
     * Retourne la valeur entière du nombre d'année de bonification pout tâche éducative
     * Format en DB : AA (AA -> nombre d'année, ex : 04 = 4 années)
     * Format dans REBaseCalcul : AA (AA -> nombre d'année, ex : 04 = 4 années)
     * 
     * @param anneeBonifTacheEduc le nombre d'années de bonification pout tâche éducative
     * @return le nombre d'année de bonification pout tâche éducative ou null si <code>anneeBonifTacheEduc</code> est
     *         null, vide ou n'a pas
     *         le bon format (2 caractère numérique)
     */
    public Integer readNombreAnneeBTE_valeurEntiere(String anneeBonifTacheEduc) {
        Integer result = null;
        if (!JadeStringUtil.isBlank(anneeBonifTacheEduc)) {
            result = convertToInteger(anneeBonifTacheEduc);
        }
        return result;
    }

    /**
     * REBACAL.YIDABE
     * Retourne la valeur entière du nombre d'année de bonification pout tâche éducative
     * <strong>En 9ème révision la valeur est codée sur 2 position et ne contient que le nombre d'année, pas de valeur
     * décimal</strong>
     * Format : AA (AA -> nombre d'année, ex : 14 = 14 années)
     * 
     * @param anneeBonifTacheEduc le nombre d'années de bonification pout tâche éducative
     * @return toujours <code>null</code> car en 9ème révision la valeur est codée sur 2 position et ne contient que le
     *         nombre d'année
     */
    public Integer readNombreAnneeBTE_valeurDecimal(String anneeBonifTacheEduc) {
        return null;
    }

    /**
     * 
     * @param bonificationTacheEducative
     * @return
     */
    public Integer readBonificationTacheEducative(String bonificationTacheEducative) {
        return convertToInteger(bonificationTacheEducative);
    }

    /**
     * REBACAL.YINOAI
     * Format xx (x est un numérique) 2 positions
     * 
     * @param codeOfficeAi
     * @return
     */
    public Integer readOfficeAICompetent(String codeOfficeAi) {
        Integer value = null;
        if (JadeNumericUtil.isInteger(codeOfficeAi)) {
            if (codeOfficeAi.length() == 2) {
                value = Integer.valueOf(codeOfficeAi);
            } else if (codeOfficeAi.length() == 3) {
                if (codeOfficeAi.startsWith("3")) {
                    value = Integer.valueOf(codeOfficeAi.substring(1, 3));
                }
            }
        }
        return value;
    }
}
