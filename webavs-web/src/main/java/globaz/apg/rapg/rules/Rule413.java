package globaz.apg.rapg.rules;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitAPGJointTiers;
import globaz.apg.db.droits.APDroitAPGJointTiersManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Si les champs « serviceType » =
 * 41 et « numberOfDays » > 145 jours -> erreur </br><strong>Champs concerné(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule413 extends Rule {

    private static final int NB_JOUR_MAX = 124;

    public Rule413(String errorCode) {
        super(errorCode, false);
        // super(errorCode, false, Arrays.asList(APGenreServiceAPG.ServiceCivilTauxRecrue), 0, NB_JOUR_MAX);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {

        String serviceType = champsAnnonce.getServiceType();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        if (APGenreServiceAPG.ServiceCivilTauxRecrue.getCodePourAnnonce().equals(serviceType)) {
            int nombreJoursEffectue = 0;
            int nombreJoursMax = NB_JOUR_MAX;

            String nss = champsAnnonce.getInsurant();
            validNotEmpty(nss, "NSS");

            List<String> forIn = new ArrayList<String>();
            forIn.add(APGenreServiceAPG.ServiceCivilTauxRecrue.getCodeSysteme());

            // Ne pas traiter les droits en état refusé ou transféré
            List<String> etatIndesirable = new ArrayList<String>();
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);

            APDroitAPGJointTiersManager manager = new APDroitAPGJointTiersManager();
            manager.setSession(getSession());
            manager.setForCsGenreServiceIn(forIn);
            manager.setLikeNumeroAvs(nss);
            manager.setForEtatDroitNotIn(etatIndesirable);
            List<APDroitAvecParent> droitsTries = null;
            try {
                manager.find();
                List<APDroitAvecParent> tousLesDroits = manager.getContainer();
                droitsTries = skipDroitParent(tousLesDroits);
            } catch (Exception e) {
                throwRuleExecutionException(e);
            }
            // On va trouver tous les droits, même celui qu'on vient de créer,
            // on additionne tous les jours soldées
            for (int i = 0; i < droitsTries.size(); i++) {
                APDroitAPGJointTiers droit = (APDroitAPGJointTiers) droitsTries.get(i);

                // On cumul les jours soldés
                if (!JadeStringUtil.isBlank(droit.getNbrJourSoldes())
                        && droit.getIdDroit().equals(champsAnnonce.getIdDroit())) {
                    nombreJoursEffectue += Integer.parseInt(droit.getNbrJourSoldes());
                }
            }

            if (nombreJoursEffectue > nombreJoursMax) {
                return false;
            }

        }
        return true;
    }
}
