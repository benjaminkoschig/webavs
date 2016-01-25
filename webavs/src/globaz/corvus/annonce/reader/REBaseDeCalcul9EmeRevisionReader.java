package globaz.corvus.annonce.reader;

import globaz.corvus.annonce.REIllegalNSSFormatException;
import globaz.corvus.annonce.RENSS;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Cette classe poss�de des m�thodes utilitaires pour lire les valeurs qui format�es contenues dans les base de calculs
 * 9�me r�vision des rentes
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
     * Format DB : num�rique 1 position
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
     * Retourne la valeur enti�re du nombre d'ann�e de bonification pout t�che �ducative
     * Format en DB : AA (AA -> nombre d'ann�e, ex : 04 = 4 ann�es)
     * Format dans REBaseCalcul : AA (AA -> nombre d'ann�e, ex : 04 = 4 ann�es)
     * 
     * @param anneeBonifTacheEduc le nombre d'ann�es de bonification pout t�che �ducative
     * @return le nombre d'ann�e de bonification pout t�che �ducative ou null si <code>anneeBonifTacheEduc</code> est
     *         null, vide ou n'a pas
     *         le bon format (2 caract�re num�rique)
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
     * Retourne la valeur enti�re du nombre d'ann�e de bonification pout t�che �ducative
     * <strong>En 9�me r�vision la valeur est cod�e sur 2 position et ne contient que le nombre d'ann�e, pas de valeur
     * d�cimal</strong>
     * Format : AA (AA -> nombre d'ann�e, ex : 14 = 14 ann�es)
     * 
     * @param anneeBonifTacheEduc le nombre d'ann�es de bonification pout t�che �ducative
     * @return toujours <code>null</code> car en 9�me r�vision la valeur est cod�e sur 2 position et ne contient que le
     *         nombre d'ann�e
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
     * Format xx (x est un num�rique) 2 positions
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
