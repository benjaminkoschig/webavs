package globaz.apg.rapg.rules;

import java.util.ArrayList;
import java.util.List;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitAPGJointTiersManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeDateUtil;

/**
 * <strong>Règles de validation des plausibilités RAPG</br>
 * Description :</strong></br>
 * Si le champ « serviceType » = 15
 * et/ou 16 et le champ « numberOfDays » est > 42 jours -> erreur </br>
 * <strong>Champs concerné(s) :</strong></br>
 *
 * @author eniv
 */
public class Rule10001 extends Rule {

    private static final int NB_JOUR_MAX = 31;

    /**
     * @param errorCode
     */
    public Rule10001(String errorCode) {
        super(errorCode, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.apg.businessimpl.plausibilites.Rule#check(ch.globaz.apg.business.models.plausibilites.ChampsAnnonce)
     */
    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {
        String startOfPeriod = champsAnnonce.getStartOfPeriod();

        String serviceType = champsAnnonce.getServiceType();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        if (serviceType.equals(APGenreServiceAPG.Demenagement.getCodePourAnnonce())) {
            String dateDebutPeriodeControle = JadeDateUtil.addMonths(startOfPeriod, -6);
            String dateFinPeriodeControle = startOfPeriod;
            List<String> forIn = new ArrayList<String>();
            forIn.add(APGenreServiceAPG.Demenagement.getCodeSysteme());
            String nss = champsAnnonce.getInsurant();
            validNotEmpty(nss, "NSS");
            APDroitAPGJointTiersManager manager = new APDroitAPGJointTiersManager();
            manager.setSession(getSession());
            manager.setForCsGenreServiceIn(forIn);
            manager.setLikeNumeroAvs(nss);
            manager.setForDroitContenuDansDateDebut(dateDebutPeriodeControle);
            manager.setForDroitContenuDansDateFin(dateFinPeriodeControle);

            // Ne pas traiter les droits en état refusé ou transféré
            List<String> etatIndesirable = new ArrayList<String>();
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);
            manager.setForEtatDroitNotIn(etatIndesirable);

            List<APDroitAvecParent> droitsTries = null;
            try {
                manager.find();
                List<APDroitAvecParent> tousLesDroits = manager.getContainer();
                droitsTries = skipDroitParent(tousLesDroits);
            } catch (Exception e) {
                throwRuleExecutionException(e);
            }
            if (!manager.isEmpty()) {
                return false;
            }

        }

        return true;
    }

}
