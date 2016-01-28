package globaz.apg.rapg.rules;

import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.db.droits.APDroitAPGJointTiers;
import globaz.apg.db.droits.APDroitAPGJointTiersManager;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadePeriodWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>R�gles de validation des plausibilit�s RAPG</br> Description :</strong></br> P�riode de contr�le : 2 ann�es
 * civiles (jusqu'au 31.12 de l'ann�e de l'annonce et depuis le 01.01 de l'ann�e de l'annonce -1) </br> Champ �
 * serviceType � = 10 ou 11 </br> Si durant la p�riode de contr�le une prestation chevauche No�l il faut appliquer la
 * r�gle suivante : </br> Nombre de jours sold�s des prestations > 314 jours -> erreur </br> sinon il faut appliquer la
 * r�gle suivante : </br> Nombre de jours sold�s des prestations > 300 jours -> erreur </br>
 * 
 * @author mmo
 */
public class Rule407 extends Rule {

    public Rule407(String errorCode) {
        super(errorCode, false);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {

        String serviceType = champsAnnonce.getServiceType();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);

        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        if (isRule407ToCheckForServiceType(serviceType)) {

            String nss = champsAnnonce.getInsurant();
            String startOfPeriod = champsAnnonce.getStartOfPeriod();
            String endOfPeriod = champsAnnonce.getEndOfPeriod();

            testDateNotEmptyAndValid(startOfPeriod, "startOfPeriod");
            testDateNotEmptyAndValid(endOfPeriod, "endOfPeriod");
            validNotEmpty(nss, "nss");

            // R�cup�ration des codes syst�mes pour les genre de services recherch�s
            String csGenreService_10 = APGenreServiceAPG.MilitaireServiceNormal.getCodeSysteme();
            String csGenreService_11 = APGenreServiceAPG.MilitaireEcoleDeRecrue.getCodeSysteme();

            // Genre de service recherch�
            List<String> forInGenreService = new ArrayList<String>();
            forInGenreService.add(csGenreService_10);
            forInGenreService.add(csGenreService_11);

            // Ne pas traiter les droits en �tat refus� ou transf�r�
            List<String> etatIndesirable = new ArrayList<String>();
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);

            // P�riode de contr�le 2 ann�es civiles (du 01.01 au 31.12)
            int anneeFinPeriodeControle = Integer.parseInt(startOfPeriod.substring(6));
            int anneeDebutPeriodeControle = anneeFinPeriodeControle - 1;

            String dateDebutPeriodeControle = "01.01." + anneeDebutPeriodeControle;
            String dateFinPeriodeControle = "31.12." + anneeFinPeriodeControle;

            JadePeriodWrapper periodeControle = new JadePeriodWrapper(dateDebutPeriodeControle, dateFinPeriodeControle);

            try {

                // On recherche tous les droits li�s � ce NSS pour la p�riode donn�e
                APDroitAPGJointTiersManager manager = new APDroitAPGJointTiersManager();
                manager.setSession(getSession());
                manager.setForCsGenreServiceIn(forInGenreService);
                manager.setForEtatDroitNotIn(etatIndesirable);
                manager.setLikeNumeroAvs(nss);
                manager.setForDroitContenuDansDateDebut(dateDebutPeriodeControle);
                manager.setForDroitContenuDansDateFin(dateFinPeriodeControle);
                manager.find();
                // On � r�cup�r� tous les droits avec leurs parents
                List<APDroitAvecParent> droitsAvecParents = manager.getContainer();
                // On vas filter les parents des droits
                List<APDroitAvecParent> droitsSansParents = skipDroitParent(droitsAvecParents);

                // On recherche les prestations standard de chaque droit sans parents
                APPrestationManager prestationManager = new APPrestationManager();
                prestationManager.setSession(getSession());
                prestationManager.setForGenre(IAPPrestation.CS_GENRE_STANDARD);
                prestationManager.setForType(IAPPrestation.CS_TYPE_NORMAL); // pas les prestations annul�es
                prestationManager.setForContenuAnnonce(IAPAnnonce.CS_DEMANDE_ALLOCATION);

                int nbJoursSoldes = 0;
                boolean isAPeriodeServicePendantNoel = false;
                for (int i = 0; i < droitsSansParents.size(); i++) {
                    APDroitAPGJointTiers droit = (APDroitAPGJointTiers) droitsSansParents.get(i);

                    prestationManager.setForIdDroit(droit.getIdDroit());
                    prestationManager.find();

                    // Pour chaque prestation enti�rement comprise dans la p�riode de contr�le, on additionne le nombre
                    // de jours sold�s et on regarde
                    // si la p�riode de la prestation chevauche No�l
                    for (Object o : prestationManager.getContainer()) {
                        APPrestation prestation = (APPrestation) o;

                        boolean isPrestationDansPeriodeControle = periodeControle.isDateDansLaPeriode(prestation
                                .getDateDebut()) && periodeControle.isDateDansLaPeriode(prestation.getDateFin());
                        if (isPrestationDansPeriodeControle) {
                            int nbJoursPrestation = Integer.parseInt(prestation.getNombreJoursSoldes());

                            if (nbJoursPrestation >= 1) {
                                nbJoursSoldes += nbJoursPrestation;
                                if (isPeriodePendantNoel(prestation.getDateDebut(), prestation.getDateFin())) {
                                    isAPeriodeServicePendantNoel = true;
                                }

                            }
                        }

                    }
                }

                return isRule407Respected(nbJoursSoldes, isAPeriodeServicePendantNoel);

            } catch (Exception e) {
                throwRuleExecutionException(e);
            }
        }

        return true;

    }

    private boolean isRule407Respected(int nombreJoursSolde, boolean periodeServiceChevaucheNoel) {

        boolean ruleNotRespected = ((nombreJoursSolde > 300) && !periodeServiceChevaucheNoel)
                || ((nombreJoursSolde > 314) && periodeServiceChevaucheNoel);

        return !ruleNotRespected;
    }

    private boolean isRule407ToCheckForServiceType(String serviceType) {
        return APGenreServiceAPG.MilitaireServiceNormal.getCodePourAnnonce().equals(serviceType)
                || APGenreServiceAPG.MilitaireEcoleDeRecrue.getCodePourAnnonce().equals(serviceType);
    }

}
