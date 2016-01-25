package globaz.webavs.common;

import globaz.globall.format.IFormatData;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * 
 * 
 * @author: oca
 */
public class CommonNSSFormater implements IFormatData {
    private static final String ERROR = "Le numéro d'AVS n'a pas le bon format";
    private static final String ERROR_PAYS = "Le numéro d'AVS ne correspond pas au pays";

    /**
     * Build EAN13 digit code
     * 
     * @param num
     * @return
     */
    public static String buildDigitCode(String num) throws Exception {
        String code = "";

        if (num == null) {
            throw new Exception("Error occured while build digit code (code is null)");
        }
        if (num.length() < 2) {
            throw new Exception("Error occured while build digit code (invalid length)");
        }
        int sum1 = computeSum(1, num);
        int sum2 = computeSum(0, num);
        sum1 = sum1 * 3;
        if (((sum1 + sum2) % 10) == 0) {
            return "0";
        } else {
            code = "" + (10 - ((sum1 + sum2) % 10));
        }
        return code;
    }

    /**
     * For internal use
     * 
     * @param i
     * @param code
     * @return
     */
    private static int computeSum(int start, String num) throws Exception {
        int res = 0;
        for (int i = start; i < num.length(); i += 2) {
            num.charAt(i);
            int digit = Integer.parseInt(num.charAt(i) + "");
            res += digit;
        }
        return res;
    }

    /**
     * Commentaire relatif au constructeur CFNumAffilie.
     */
    public CommonNSSFormater() {
        super();
    }

    /**
     * check si le numéro. si 11 position sans point -> check du numéro AVS si 13 position sans point -> check du NNSS
     * 
     * Ceci permet la saisie des nouveau et des anciens numéro pour la phase transitoire ou les deux formats sont
     * authorisés. oca
     */
    @Override
    public String check(Object value) throws Exception {
        TITiersViewBean vBean = null;
        String unformated = "";
        try {
            vBean = (TITiersViewBean) value;
            // test numéro AVS seulement si personne physique
            if (vBean.getPersonnePhysique().booleanValue()) {
                unformated = unformat(vBean.getNumAvsActuel());
                // "00000000000" est un numéro valide pour l'encodage du 'père
                // inconnu' (cas prestation)
                if (!"00000000000".equals(unformated)) {
                    if (unformated.length() == 13) {
                        checkNss(unformated);
                    } else if ((unformated.length() == 11) || (unformated.length() == 8)) {
                        checkAvs(value);
                    } else {
                        throw new Exception(ERROR);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(ERROR);
        }
        return "";
    }

    public String checkAvs(Object value) throws Exception {
        TITiersViewBean vBean = null;

        String unformated = "";

        try {
            vBean = (TITiersViewBean) value;
            // test numéro AVS seulement si personne physique
            if (vBean.getPersonnePhysique().booleanValue()) {
                int sexe = 0;
                if (TITiersViewBean.CS_HOMME.equals(vBean.getSexe())) {
                    sexe = 1;
                } else if (TITiersViewBean.CS_FEMME.equals(vBean.getSexe())) {
                    sexe = 2;
                }
                vBean.setNumAvsActuel(deleteSpace(vBean.getNumAvsActuel()));
                unformated = unformat(vBean.getNumAvsActuel());
                // "00000000000" est un numéro valide pour l'encodage du 'père
                // inconnu' (cas prestation)
                if (!"00000000000".equals(unformated)) {

                    JAUtil.checkAvs(unformated, sexe);
                }
            }
        } catch (Exception e) {
            throw new Exception(ERROR);
        }

        // check pays suisse
        String nationalite = vBean.getIdPays();
        /*
         * Pour supporter les num à 8 chiffres
         */
        if (unformated.length() == 11) {
            if (!JadeStringUtil.isEmpty(nationalite)) {
                if (TIPays.CS_SUISSE.equals(nationalite)) {
                    // suisse
                    if (unformated.charAt(9) > '4') {
                        throw new Exception(ERROR_PAYS);
                    }
                } else {
                    // etranger
                    if (unformated.charAt(9) < '5') {
                        throw new Exception(ERROR_PAYS);
                    }
                }
            }
        }

        return "";
    }

    /*
     * Attention unformated.length DOIT valoir 13 si on appelle cette méthode !!!
     */
    public void checkNss(String unformated) throws Exception {
        String actualCode = unformated.substring(12);
        String computedCode = buildDigitCode(unformated.substring(0, 12));
        if (!actualCode.equals(computedCode)) {
            throw new Exception(ERROR);
        }
    }

    public String deleteSpace(String value) throws Exception {
        String res = "";
        if (value != null) {
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) != ' ') {
                    res += value.charAt(i);
                }
            }
        }
        return res;
    }

    /**
     * format le numéro au format : xxx.xxxx.xxxx.xx
     */
    @Override
    public String format(String value) throws Exception {
        String str = "";
        value = unformat(value); // retire les éventuels '.'
        // formatage 'par palier' pour permettre le formatage
        // de numéro avs incomplet (utile pour les recherches)

        if (value != null) {
            for (int i = 0; i < value.length(); i++) {
                str += value.charAt(i);

                switch (i) {
                    case 2:
                    case 6:
                    case 10:
                        str += ".";
                        break;
                }
            }
        }
        return str;
    }

    @Override
    public String unformat(String value) throws Exception {
        String str = "";
        if (value != null) {
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) != '.') {
                    str += value.charAt(i);
                }
            }
        }
        return str;
    }

}
