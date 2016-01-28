package globaz.pyxis.api.osiris;

import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TITiers;

public class TITiersAdministrationOSI {

    /**
     * Return le tiers administration.
     * 
     * @param pyxisSession
     * @param idTiersAdministration
     * @return
     * @throws Exception
     */
    private static TIAdministrationViewBean getAdministration(BISession pyxisSession, String idTiersAdministration)
            throws Exception {
        TIAdministrationManager manager = new TIAdministrationManager();
        manager.setISession(pyxisSession);
        manager.setForIdTiersAdministration(idTiersAdministration);

        manager.find();

        if (manager.hasErrors() || manager.isEmpty()) {
            return null;
        } else {
            return (TIAdministrationViewBean) manager.getFirstEntity();
        }
    }

    /**
     * Return le code et libellé de l'administration séparé par un espace. (getCodeAdministration() + " " + getNom() du
     * Tiers).
     * 
     * @param pyxisSession
     * @param idTiersAdministration
     * @return
     */
    public static String getAdministrationCodeEtLibelle(BISession pyxisSession, String idTiersAdministration) {
        try {
            TIAdministrationViewBean administration = getAdministration(pyxisSession, idTiersAdministration);

            if (administration == null) {
                return "";
            } else {
                String admin = administration.getCodeAdministration() + " - " + administration.getNom();

                if (!JadeStringUtil.isBlank(getNomComplement(administration))) {
                    admin += " " + getNomComplement(administration);
                }

                return admin;
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Return le libellé de l'administration. (getNom() du Tiers).
     * 
     * @param pyxisSession
     * @param idTiersAdministration
     * @return
     */
    public static String getAdministrationLibelle(BISession pyxisSession, String idTiersAdministration) {
        try {
            TIAdministrationViewBean administration = getAdministration(pyxisSession, idTiersAdministration);

            if (administration == null) {
                return "";
            } else {
                return administration.getNom();
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Return le numéro de l'administration. (getCodeAdministration() du Tiers).
     * 
     * @param pyxisSession
     * @param idTiersAdministration
     * @return
     */
    public static String getAdministrationNumero(BISession pyxisSession, String idTiersAdministration) {
        try {
            TIAdministrationViewBean administration = getAdministration(pyxisSession, idTiersAdministration);

            if (administration == null) {
                return null;
            } else {
                return administration.getCodeAdministration();
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param administration
     * @return la description 3 et 4 du tiers
     */
    public static String getNomComplement(TITiers administration) {
        String d3 = administration.getDesignation3();
        String d4 = administration.getDesignation4();
        if (d3 == null) {
            d3 = "";
        } else {
            d3 = d3.trim();
        }

        if (d4 == null) {
            d4 = "";
        } else {
            d4 = d4.trim();
        }

        String tmp = d3;
        if (!JadeStringUtil.isBlank(d4)) {

            if (!JadeStringUtil.isBlank(d3)) {
                tmp += " ";
            }
            tmp += d4;
        }

        return tmp;
    }
}
