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
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Période de contrôle : 5 années
 * civiles (jusqu'au 31.12 de l'année de l'annonce et depuis le 01.01 de l'année de l'annonce -4) </br> Champ «
 * serviceType » = 20 ou 21 </br> Durant la période de contrôle, pour un type de service 20, si la somme des jours
 * soldés pour les prestations de type (21,20,11 et 13) est < 40 --> erreur </br> Durant la période de contrôle, pour un
 * type de service 21, si la somme des jours soldés pour les prestations de type (11 et 13) est >= 40 --> erreur
 * 
 * @author mmo
 */
public class Rule506 extends Rule {

    public Rule506(String errorCode) {
        super(errorCode, true);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {

        String serviceType = champsAnnonce.getServiceType();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);

        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        boolean isServiceType20 = APGenreServiceAPG.ProtectionCivileServiceNormale.getCodePourAnnonce().equals(
                serviceType);
        boolean isServiceType21 = APGenreServiceAPG.ProtectionCivileFormationDeBase.getCodePourAnnonce().equals(
                serviceType);

        if (isServiceType20 || isServiceType21) {

            String idDroitCourant = champsAnnonce.getIdDroit();
            if (JadeStringUtil.isIntegerEmpty(idDroitCourant)) {
                throwRuleExecutionException(new NumberFormatException(
                        "Can not retrieve a valid id for the current APDroitLAPG"));
            }

            String nss = champsAnnonce.getInsurant();
            String startOfPeriod = champsAnnonce.getStartOfPeriod();

            testDateNotEmptyAndValid(startOfPeriod, "startOfPeriod");
            validNotEmpty(nss, "nss");

            // Récupération des codes systèmes pour les genre de services recherchés
            String csGenreService_11 = APGenreServiceAPG.MilitaireEcoleDeRecrue.getCodeSysteme();
            String csGenreService_13 = APGenreServiceAPG.MilitaireRecrutement.getCodeSysteme();

            // Genre de service recherché
            List<String> forInGenreService = new ArrayList<String>();
            forInGenreService.add(csGenreService_11);
            forInGenreService.add(csGenreService_13);

            if (isServiceType20) {
                String csGenreService_20 = APGenreServiceAPG.ProtectionCivileServiceNormale.getCodeSysteme();
                String csGenreService_21 = APGenreServiceAPG.ProtectionCivileFormationDeBase.getCodeSysteme();

                forInGenreService.add(csGenreService_20);
                forInGenreService.add(csGenreService_21);
            }

            // Ne pas traiter les droits en état refusé ou transféré
            List<String> etatIndesirable = new ArrayList<String>();
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);

            // Période de contrôle 5 années civiles (du 01.01 au 31.12)
            int anneeFinPeriodeControle = Integer.parseInt(startOfPeriod.substring(6));
            int anneeDebutPeriodeControle = anneeFinPeriodeControle - 4;

            String dateDebutPeriodeControle = "01.01." + anneeDebutPeriodeControle;
            String dateFinPeriodeControle = "31.12." + anneeFinPeriodeControle;

            JadePeriodWrapper periodeControle = new JadePeriodWrapper(dateDebutPeriodeControle, dateFinPeriodeControle);

            try {

                // On recherche tous les droits liés à ce NSS pour la période donnée
                APDroitAPGJointTiersManager manager = new APDroitAPGJointTiersManager();
                manager.setSession(getSession());
                manager.setForCsGenreServiceIn(forInGenreService);
                manager.setForEtatDroitNotIn(etatIndesirable);
                manager.setLikeNumeroAvs(nss);
                manager.setForDroitContenuDansDateDebut(dateDebutPeriodeControle);
                manager.setForDroitContenuDansDateFin(dateFinPeriodeControle);
                manager.find();

                // On à récupéré tous les droits avec leurs parents
                List<APDroitAvecParent> droitsAvecParents = manager.getContainer();
                // On vas filter les parents des droits
                List<APDroitAvecParent> droitsSansParents = skipDroitParent(droitsAvecParents);

                // On recherche les prestations standard de chaque droit sans parents
                APPrestationManager prestationManager = new APPrestationManager();
                prestationManager.setSession(getSession());
                prestationManager.setForGenre(APTypeDePrestation.STANDARD.getCodesystemString());
                prestationManager.setForType(IAPPrestation.CS_TYPE_NORMAL); // pas les prestations annulées
                prestationManager.setForContenuAnnonce(IAPAnnonce.CS_DEMANDE_ALLOCATION);

                // Cumul du nombre de jours soldées pour les genres de service 11 et 13
                int nbJoursSoldes = 0;
                for (Object d : droitsSansParents) {
                    APDroitAvecParent droit = (APDroitAvecParent) d;
                    String csGenreService = ((APDroitAPGJointTiers) droit).getCsGenreService();

                    if (isGenreService11ou13(csGenreService)) {

                        prestationManager.setForIdDroit(droit.getIdDroit());
                        prestationManager.find();

                        for (Object p : prestationManager.getContainer()) {
                            APPrestation prestation = (APPrestation) p;
                            if (isPrestationDansPeriodeControle(periodeControle, prestation)) {
                                nbJoursSoldes += Integer.valueOf(prestation.getNombreJoursSoldes());
                            }
                        }
                    }
                }

                if (isServiceType20) {
                    // Recherche d'une prestation standard dans la période avec genre de service 20 ou 21
                    for (Object d : droitsSansParents) {
                        APDroitAvecParent droit = (APDroitAvecParent) d;

                        // On skip le droit courant
                        if (idDroitCourant.equals(droit.getIdDroit())) {
                            continue;
                        }
                        // Genre service 20 ou 21
                        String csGenreService = ((APDroitAPGJointTiers) droit).getCsGenreService();
                        if (isGenreService20ou21(csGenreService)) {

                            prestationManager.setForIdDroit(droit.getIdDroit());
                            prestationManager.find();

                            for (Object p : prestationManager.getContainer()) {
                                APPrestation prestation = (APPrestation) p;
                                if (isPrestationDansPeriodeControle(periodeControle, prestation)) {
                                    return true;
                                }
                            }
                        }
                    }
                    return nbJoursSoldes >= 40;
                } else if (isServiceType21) {
                    return nbJoursSoldes < 40;
                }

            } catch (Exception e) {
                throwRuleExecutionException(e);
            }
        }
        return true;
    }

    private boolean isPrestationDansPeriodeControle(JadePeriodWrapper periodeControle, APPrestation prestation) {
        return periodeControle.isDateDansLaPeriode(prestation.getDateDebut())
                && periodeControle.isDateDansLaPeriode(prestation.getDateFin());
    }

    private boolean isGenreService20ou21(String csGenreService) {
        if (APGenreServiceAPG.ProtectionCivileServiceNormale.getCodeSysteme().equals(csGenreService)) {
            return true;
        }
        if (APGenreServiceAPG.ProtectionCivileFormationDeBase.getCodeSysteme().equals(csGenreService)) {
            return true;
        }
        return false;
    }

    private boolean isGenreService11ou13(String csGenreService) {
        if (APGenreServiceAPG.MilitaireEcoleDeRecrue.getCodeSysteme().equals(csGenreService)) {
            return true;
        }
        if (APGenreServiceAPG.MilitaireRecrutement.getCodeSysteme().equals(csGenreService)) {
            return true;
        }
        return false;
    }

}
