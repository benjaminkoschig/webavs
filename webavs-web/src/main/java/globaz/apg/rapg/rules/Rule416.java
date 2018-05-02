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
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Si le champ « serviceType » = 40
 * et/ou 41 et le champ « numberOfDays » est > 450 jours -> erreur </br><strong>Champs concerné(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule416 extends Rule {

    private static final int NB_JOUR_MAX = 450;

    /**
     * @param errorCode
     */
    public Rule416(String errorCode) {
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
        String serviceType = champsAnnonce.getServiceType();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        List<String> services = new ArrayList<String>();
        services.add(APGenreServiceAPG.ServiceCivilNormal.getCodePourAnnonce());
        services.add(APGenreServiceAPG.ServiceCivilTauxRecrue.getCodePourAnnonce());

        if (services.contains(serviceType)) {
            int totalDeJours = 0;
            String nss = champsAnnonce.getInsurant();
            validNotEmpty(nss, "NSS");

            List<String> forIn = new ArrayList<String>();
            forIn.add(APGenreServiceAPG.ServiceCivilNormal.getCodeSysteme());
            forIn.add(APGenreServiceAPG.ServiceCivilTauxRecrue.getCodeSysteme());

            APDroitAPGJointTiersManager manager = new APDroitAPGJointTiersManager();
            manager.setSession(getSession());
            manager.setForCsGenreServiceIn(forIn);
            manager.setLikeNumeroAvs(nss);

            // Ne pas traiter les droits en état refusé ou transféré
            List<String> etatIndesirable = new ArrayList<String>();
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);
            manager.setForEtatDroitNotIn(etatIndesirable);
            List<APDroitAvecParent> droitsSansParents = null;
            try {
                manager.find();
                List<APDroitAvecParent> tousLesDroits = manager.getContainer();
                droitsSansParents = skipDroitParent(tousLesDroits);
            } catch (Exception e) {
                throwRuleExecutionException(e);
            }

            for (Object d : droitsSansParents) {
                APDroitAPGJointTiers droit = (APDroitAPGJointTiers) d;
                if (!JadeStringUtil.isEmpty(droit.getNbrJourSoldes())) {
                    totalDeJours += Integer.valueOf(droit.getNbrJourSoldes());
                }

            }

            if (totalDeJours > NB_JOUR_MAX) {
                return false;
            }
        }
        return true;
    }

    private boolean isGenreService40ou41(String csGenreService) {
        if (APGenreServiceAPG.ServiceCivilNormal.getCodeSysteme().equals(csGenreService)) {
            return true;
        }
        if (APGenreServiceAPG.ServiceCivilTauxRecrue.getCodeSysteme().equals(csGenreService)) {
            return true;
        }
        return false;
    }
}
