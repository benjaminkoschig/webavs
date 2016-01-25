package ch.globaz.perseus.businessimpl.services.bvr;

import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.utils.PFUserHelper;
import globaz.prestation.tools.PRStringUtils;
import java.util.HashMap;
import ch.globaz.perseus.business.exceptions.PerseusException;
import ch.globaz.perseus.business.services.bvr.BvrService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;

public class BvrServiceImpl extends PerseusAbstractServiceImpl implements BvrService {

    private HashMap<Integer, Integer> chiffreCle = null;
    private int[][] tableau = null;

    private HashMap<Integer, Integer> initialiseChiffreCle() {
        chiffreCle = new HashMap<Integer, Integer>();
        chiffreCle.put(0, 0);
        chiffreCle.put(1, 9);
        chiffreCle.put(2, 8);
        chiffreCle.put(3, 7);
        chiffreCle.put(4, 6);
        chiffreCle.put(5, 5);
        chiffreCle.put(6, 4);
        chiffreCle.put(7, 3);
        chiffreCle.put(8, 2);
        chiffreCle.put(9, 1);
        return chiffreCle;
    }

    private int[][] intialisationTableau() {
        int[][] tableau = { { 0, 9, 4, 6, 8, 2, 7, 1, 3, 5 }, { 9, 4, 6, 8, 2, 7, 1, 3, 5, 0 },
                { 4, 6, 8, 2, 7, 1, 3, 5, 0, 9 }, { 6, 8, 2, 7, 1, 3, 5, 0, 9, 4 }, { 8, 2, 7, 1, 3, 5, 0, 9, 4, 6 },
                { 2, 7, 1, 3, 5, 0, 9, 4, 6, 8 }, { 7, 1, 3, 5, 0, 9, 4, 6, 8, 2 }, { 1, 3, 5, 0, 9, 4, 6, 8, 2, 7 },
                { 3, 5, 0, 9, 4, 6, 8, 2, 7, 1 }, { 5, 0, 9, 4, 6, 8, 2, 7, 1, 3 } };
        return tableau;

    }

    // Methode permettant d'obtenir l'adresse de paiement d'un tiers et de voir s'il a un CCP
    @Override
    public boolean validationCCP(String idAdressePaiement, String idApplication)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {

        AdresseTiersDetail adressePaiement = PFUserHelper.getAdressePaiementAssure(idAdressePaiement, idApplication,
                JACalendar.todayJJsMMsAAAA());

        if (adressePaiement.getFields() != null) {
            if (JadeStringUtil.isEmpty(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_CCP))) {
                return false;
            } else {
                return true;
            }
        }

        return false;
    }

    private int validationNum(String numeroBVR, int repport, int incr) {

        for (int i = incr; i <= numeroBVR.length() - 2; i++) {
            repport = tableau[repport][Integer.parseInt(String.valueOf(numeroBVR.charAt(i)))];
        }

        return repport;

    }

    @Override
    public String validationNumeroBVR(String numeroBVR) throws PerseusException {
        boolean isnumBRVOk = false;

        // Chaine commençant par soit un chiffre entre 0 et 9 ou soit un caractère blanc. Permet d'éviter tout caractère
        // non-désiré.
        String pattern = "^[0-9|\\s]*$";

        if (!JadeStringUtil.isBlankOrZero(numeroBVR) && numeroBVR.matches(pattern)) {

            numeroBVR = PRStringUtils.replaceString(numeroBVR, " ", "");
            tableau = intialisationTableau();
            chiffreCle = initialiseChiffreCle();

            String lastNumberOFBVRReference = JadeStringUtil.substring(numeroBVR, numeroBVR.length() - 1);
            int chCle = validationNum(numeroBVR, 0, 0);

            if (Integer.parseInt(lastNumberOFBVRReference) == chiffreCle.get(chCle)) {
                isnumBRVOk = true;
            }
        }

        if (isnumBRVOk) {
            return numeroBVR;
        } else {
            return "";
        }
    }

}
