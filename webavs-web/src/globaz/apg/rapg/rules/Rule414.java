package globaz.apg.rapg.rules;

import globaz.apg.db.droits.APDroitAPGJointTiers;
import globaz.apg.db.droits.APDroitAPGJointTiersManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadePeriodWrapper;
import java.util.Arrays;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Toute combinaison de jours de
 * service sur une même période pour les genres de services suivants est interdite :</br> 1) code 30 et codes 10, 11,
 * 12, 13, 14 -> erreur</br> 2) code 50 et codes 10, 11, 12, 13, 14 -> erreur</br> ou</br> 3) code 10 et codes 30, 50 ->
 * erreur</br> 4) code 11 et codes 30, 50 -> erreur</br> 5) code 12 et codes 30, 50 -> erreur</br> 6) code 13 et codes
 * 30, 50 -> erreur</br> 7) code 14 et codes 30, 50 -> erreur</br> </br><strong>Champs concerné(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule414 extends Rule {

    public Rule414(String errorCode) {
        super(errorCode, false);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {

        if (APGenreServiceAPG.CoursMoniteursJeunesTireurs.getCodePourAnnonce().equals(champsAnnonce.getServiceType())
                || APGenreServiceAPG.JeunesseEtSportFormationCadres.getCodePourAnnonce().equals(
                        champsAnnonce.getServiceType())) {

            APDroitAPGJointTiersManager manager = new APDroitAPGJointTiersManager();
            manager.setSession(getSession());
            manager.setForDroitContenuDansDateDebut(champsAnnonce.getStartOfPeriod());
            manager.setForDroitContenuDansDateFin(champsAnnonce.getEndOfPeriod());
            manager.setForCsGenreServiceIn(Arrays.asList(APGenreServiceAPG.MilitaireRecrutement.getCodeSysteme(),
                    APGenreServiceAPG.MilitaireEcoleDeRecrue.getCodeSysteme(),
                    APGenreServiceAPG.MilitaireServiceNormal.getCodeSysteme(),
                    APGenreServiceAPG.MilitairePaiementGallons.getCodeSysteme(),
                    APGenreServiceAPG.MilitaireSousOfficierEnServiceLong.getCodeSysteme()));
            manager.setLikeNumeroAvs(champsAnnonce.getInsurant());
            try {
                manager.find();
            } catch (Exception e) {
                throwRuleExecutionException(e);
            }
            JadePeriodWrapper periodeAnnonce = new JadePeriodWrapper(champsAnnonce.getStartOfPeriod(),
                    champsAnnonce.getEndOfPeriod());

            for (int i = 0; i < manager.size(); i++) {
                APDroitAPGJointTiers unDroit = (APDroitAPGJointTiers) manager.get(i);
                if (!unDroit.getIdDroit().equals(champsAnnonce.getIdDroit())) {
                    for (JadePeriodWrapper unePeriodeDuDroit : unDroit.getPeriodes()) {
                        switch (periodeAnnonce.comparerChevauchement(unePeriodeDuDroit)) {
                            case LesPeriodesSeChevauchent:
                                return false;
                        }
                    }
                }
            }
        }

        return true;
    }
}
