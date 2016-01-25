package globaz.cfc.format;

import globaz.globall.format.IFormatData;
import globaz.globall.util.JAUtil;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * le numéro AVS est controler dans ts les cas pour la CFC Date de création : (14.02.2003 14:25:28)
 * 
 * @author: Administrator
 */
public class CFNumAvs implements IFormatData {
    private static final String ERROR = "Le numéro d'AVS n'a pas le bon format";
    private static final String ERROR_PAYS = "Le numéro d'AVS ne correspond pas au pays";

    /**
     * Commentaire relatif au constructeur CFNumAffilie.
     */
    public CFNumAvs() {
        super();
    }

    @Override
    public String check(Object value) throws Exception {
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

                    JAUtil.checkAvs(vBean.getNumAvsActuel(), sexe);
                }
            }
        } catch (Exception e) {
            throw new Exception(ERROR);
        }

        // check pays suisse
        String nationalite = vBean.getIdPays();
        /*
         * Pour supporter les num à 8 chiffre
         */
        if (unformated.length() == 11) {

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

        return "";
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

    @Override
    public String format(String value) throws Exception {
        String str = "";
        value = unformat(value); // retire les éventuels '.'
        // formatage 'par palier' pour permettre le formatage
        // de numéro avs incomplet (utile pour les recherches)

        if (value != null) {
            for (int i = 0; i < value.length(); i++) {
                str += value.charAt(i);

                /*
                 * Pour supporter les num a 8 chiffre
                 */
                if ((i == 7) && (value.length() < 9)) {
                    i++;
                }
                switch (i) {
                    case 2:
                    case 4:
                    case 7:
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
