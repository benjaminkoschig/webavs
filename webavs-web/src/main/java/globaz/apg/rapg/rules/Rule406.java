package globaz.apg.rapg.rules;

import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.db.droits.APDroitAPGJointTiers;
import globaz.apg.db.droits.APDroitAPGJointTiersManager;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadePeriodWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Période de contrôle : 2 années
 * civiles Si le champ « serviceType » = 11, le champ « numberOfDays » > 159 jours et la période de service ne chevauche
 * pas Noël -> erreur Si le champ « serviceType » = 11, le champ « numberOfDays » > 187 jours et la période de service
 * </br><strong>Champs concerné(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule406 extends Rule {

    private static final int NB_JOUR_MAX = 159;

    public Rule406(String errorCode) {
        super(errorCode, false);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {

        // Si école de recrue
        if (APGenreServiceAPG.MilitaireEcoleDeRecrue.getCodePourAnnonce().equals(champsAnnonce.getServiceType())) {

            int nbMaxJoursSoldesEcoleRecrue = NB_JOUR_MAX;

            String nss = champsAnnonce.getInsurant();
            validNotEmpty(nss, "NSS");

            List<String> forIn = new ArrayList<String>();
            forIn.add(APGenreServiceAPG.MilitaireEcoleDeRecrue.getCodeSysteme());

            // Ne pas traiter les droits en état refusé ou transféré
            List<String> etatIndesirable = new ArrayList<String>();
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);

            // Période de contrôle 2 années civiles (du 01.01 au 31.12)
            String startOfPeriod = champsAnnonce.getStartOfPeriod();
            int anneeFinPeriodeControle = Integer.parseInt(startOfPeriod.substring(6)) + 2;
            int anneeDebutPeriodeControle = anneeFinPeriodeControle - 4;

            String dateDebutPeriodeControle = "01.01." + anneeDebutPeriodeControle;
            String dateFinPeriodeControle = "31.12." + anneeFinPeriodeControle;

            JadePeriodWrapper periodeControle = new JadePeriodWrapper(dateDebutPeriodeControle, dateFinPeriodeControle);

            List<APDroitAvecParent> droitsSansParents = null;
            try {
                APDroitAPGJointTiersManager manager = new APDroitAPGJointTiersManager();
                manager.setSession(getSession());
                manager.setForCsGenreServiceIn(forIn);
                manager.setLikeNumeroAvs(nss);
                manager.setForEtatDroitNotIn(etatIndesirable);
                manager.setForDroitContenuDansDateDebut(dateDebutPeriodeControle);
                manager.setForDroitContenuDansDateFin(dateFinPeriodeControle);
                manager.find();
                List<APDroitAvecParent> tousLesDroits = manager.getContainer();
                droitsSansParents = skipDroitParent(tousLesDroits);
            } catch (Exception e) {
                throwRuleExecutionException(e);
            }
            // On recherche les prestations standard de chaque droit sans parents
            APPrestationManager prestationManager = new APPrestationManager();
            prestationManager.setSession(getSession());
            prestationManager.setForGenre(APTypeDePrestation.STANDARD.getCodesystemString());
            prestationManager.setForType(IAPPrestation.CS_TYPE_NORMAL); // pas les prestations annulées
            prestationManager.setForContenuAnnonce(IAPAnnonce.CS_DEMANDE_ALLOCATION);

            int nombreDeJoursSoldes = 0;

            for (Object d : droitsSansParents) {
                APDroitAvecParent droit = (APDroitAvecParent) d;
                String csGenreService = ((APDroitAPGJointTiers) droit).getCsGenreService();

                if (isGenreService11(csGenreService)) {

                    prestationManager.setForIdDroit(droit.getIdDroit());
                    try {
                        prestationManager.find();
                    } catch (Exception e) {
                        throwRuleExecutionException(e);
                    }
                    for (Object p : prestationManager.getContainer()) {
                        APPrestation prestation = (APPrestation) p;
                        if (isPrestationDansPeriodeControle(periodeControle, prestation)) {
                            nombreDeJoursSoldes += Integer.valueOf(prestation.getNombreJoursSoldes());
                        }
                    }
                }
            }
            if (nombreDeJoursSoldes > nbMaxJoursSoldesEcoleRecrue) {
                return false;
            }

        }

        return true;
    }

    private boolean isPrestationDansPeriodeControle(JadePeriodWrapper periodeControle, APPrestation prestation) {
        return periodeControle.isDateDansLaPeriode(prestation.getDateDebut())
                && periodeControle.isDateDansLaPeriode(prestation.getDateFin());
    }

    private boolean isGenreService11(String csGenreService) {
        if (APGenreServiceAPG.MilitaireEcoleDeRecrue.getCodeSysteme().equals(csGenreService)) {
            return true;
        }
        return false;
    }
}
