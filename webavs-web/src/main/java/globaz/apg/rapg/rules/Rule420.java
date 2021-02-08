package globaz.apg.rapg.rules;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.*;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;

import java.util.*;

/**
 *Si entre le premier et le dernier jour de congé de paternité ou de congé de proche-aidant :
 * Le nombre de jours avec code de service ? 91
 * + nombre de jours avec code de service = 91
 * > Nombre de jours entre la période de début et de fin du congé de paternité ?  erreur
 * Si nombre de jours avec type de service ? 92
 * + nombre de jours avec type de service = 92
 * > Nombre de jours entre la période de début et de fin du congé de paternité ?  erreur
 * @author von
 */
public class Rule420 extends Rule {

    private static final int NOMBRE_MOIS_MAX = 6;


    /**
     * @param errorCode
     */
    public Rule420(String errorCode) {
        super(errorCode, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.apg.businessimpl.plausibilites.Rule#check(ch.globaz.apg.business.models.plausibilites.ChampsAnnonce)
     */
    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException {

        String serviceType = champsAnnonce.getServiceType();
        String dateNaissance = champsAnnonce.getNewbornDateOfBirth();
        String dateFin = champsAnnonce.getEndOfPeriod();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        String nss = champsAnnonce.getInsurant();
        validNotEmpty(nss, "NSS");
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        if(serviceType.equals(APGenreServiceAPG.Paternite.getCodePourAnnonce())){
            int nombreMois = JadeDateUtil.getNbMonthsBetween(dateNaissance,dateFin);
            if(nombreMois>NOMBRE_MOIS_MAX){
                return false;
            }

        }

        return true;
    }



}
