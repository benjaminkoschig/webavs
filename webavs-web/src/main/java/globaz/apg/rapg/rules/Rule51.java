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
 * Si l'assuré a déjà bénéficié d'un déménagement dans les 6 mois
 * avant et / ou après le droit actuel -> erreur </br>
 * <strong>Champs concerné(s) :</strong></br>
 *
 * @author eniv
 */
public class Rule51 extends Rule {

    /**
     * @param errorCode
     */
    public Rule51(String errorCode) {
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

        if (serviceType.equals(APGenreServiceAPG.Demenagement.getCodePourAnnonce())) {
            String dateDebutPeriodeControle = JadeDateUtil.addMonths(startOfPeriod, -6);
            String dateFinPeriodeControle = JadeDateUtil.addMonths(startOfPeriod, 6);
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

            List<APDroitAvecParent> droitsTries = new ArrayList<>();
            try {
                manager.find();
                List<APDroitAvecParent> tousLesDroits = manager.getContainer();
                droitsTries = skipDroitParent(tousLesDroits);
                droitsTries = skipDroitLuiMeme(droitsTries, champsAnnonce.getIdDroit());
            } catch (Exception e) {
                throwRuleExecutionException(e);
            }
            if (!droitsTries.isEmpty()) {
                return false;
            }

        }

        return true;
    }

}
