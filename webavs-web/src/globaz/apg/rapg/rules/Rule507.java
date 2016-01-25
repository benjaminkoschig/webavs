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
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Période de contrôle : 1 année
 * civile</br> Si, durant une période d'une année :</br> 1) le champ serviceType = 30 et le champ</br> numberOfDays est
 * > 20 jours -> erreur (à examiner)</br> 2) le champ serviceType = 50 et le champ</br> numberOfDays est > 20 jours ->
 * erreur (à examiner)</br> </br><strong>Champs concerné(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule507 extends Rule {

    public Rule507(String errorCode) {
        super(errorCode, true);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {
        String serviceType = champsAnnonce.getServiceType();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        List<String> typeServices = new ArrayList<String>();
        typeServices.add("30");
        typeServices.add("50");

        // On ne fait le test que si le type de service est renseigné et qu'il nous intéresse
        if (!JadeStringUtil.isEmpty(serviceType) && typeServices.contains(typeServices)) {

            String dateDebutPeriode = champsAnnonce.getStartOfPeriod();
            testDateNotEmptyAndValid(dateDebutPeriode, "dateDebutPeriode");
            String nss = champsAnnonce.getInsurant();
            validNotEmpty(nss, "nss");

            String annee = dateDebutPeriode.substring(6);

            APDroitAPGJointTiersManager manager = new APDroitAPGJointTiersManager();
            manager.setSession(getSession());
            manager.setForCsGenreServiceIn(typeServices);
            manager.setForDroitContenuDansDateDebut("01.01." + annee);
            manager.setForDroitContenuDansDateFin("31.12." + annee);
            manager.setLikeNumeroAvs(nss);

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

            int joursService30 = 0;
            int joursService50 = 0;

            for (int i = 0; i < droitsTries.size(); i++) {
                APDroitAPGJointTiers droit = (APDroitAPGJointTiers) droitsTries.get(i);
                // Service 30
                if (APGenreServiceAPG.JeunesseEtSportFormationCadres.getCodeSysteme().equals(droit.getCsGenreService())) {
                    joursService30 += getNombreJoursSolde(droit);
                }
                // Service 50
                if (APGenreServiceAPG.CoursMoniteursJeunesTireurs.getCodeSysteme().equals(droit.getCsGenreService())) {
                    joursService50 += getNombreJoursSolde(droit);
                }
            }

            if ((joursService30 > 20) || (joursService50 > 20)) {
                return false;
            }
        }
        return true;
    }

    private int getNombreJoursSolde(APDroitAPGJointTiers droit) {
        int result = 0;
        if (!JadeStringUtil.isEmpty(droit.getNbrJourSoldes())) {
            try {
                result = Integer.valueOf(droit.getNbrJourSoldes());
            } catch (NumberFormatException exception) {
                // Nothing to do. On ne devrait jamais arrivé ici... mais bon..
            }
        }
        return result;
    }
}
