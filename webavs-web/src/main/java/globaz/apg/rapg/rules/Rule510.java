package globaz.apg.rapg.rules;

import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.db.droits.APDroitAPGJointTiersManager;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.globall.db.BManager;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.common.domaine.Date;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Si le champ « serviceType » = 40
 * et/ou 41 et le champ « numberOfDays » est > 300 jours -> erreur </br><strong>Champs concerné(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule510 extends Rule {

    /**
     * @param errorCode
     */
    public Rule510(String errorCode) {
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
        services.add("40");
        services.add("41");
        // Récupération des codes systèmes pour les genre de services recherchés
        String csGenreService_40 = APGenreServiceAPG.ServiceCivilNormal.getCodeSysteme();
        String csGenreService_41 = APGenreServiceAPG.ServiceCivilTauxRecrue.getCodeSysteme();
        // Genre de service recherché
        List<String> forInGenreService = new ArrayList<String>();
        forInGenreService.add(csGenreService_40);
        forInGenreService.add(csGenreService_41);

        validateFields(champsAnnonce);
        String idDroitCourant = champsAnnonce.getIdDroit();
        String nss = champsAnnonce.getInsurant();
        String startOfPeriod = champsAnnonce.getStartOfPeriod();
        List<String> etatIndesirable = initEtatDroitIndesirable();

        // Période de contrôle 2 années civiles (du 01.01 au 31.12)
        int anneeFinPeriodeControle = Integer.parseInt(startOfPeriod.substring(6));
        int anneeDebutPeriodeControle = anneeFinPeriodeControle - 2;
        anneeFinPeriodeControle += 2;
        String dateDebutPeriodeControle = "01.01." + anneeDebutPeriodeControle;
        String dateFinPeriodeControle = "31.12." + anneeFinPeriodeControle;

        JadePeriodWrapper periodeControle = new JadePeriodWrapper(dateDebutPeriodeControle, dateFinPeriodeControle);

        if (services.contains(serviceType)) {
            try {

                List<APDroitAvecParent> droitsSansParents = retrieveDroitsSansParents(forInGenreService,
                        etatIndesirable, nss, dateDebutPeriodeControle, dateFinPeriodeControle);
                // Récupération des prestations dans la période
                List<APPrestation> prestations = initPrestationsDesDroits(droitsSansParents, periodeControle);
                Map<String, APPrestation> prestationsPourCalculTotalJours = new HashMap<String, APPrestation>();
                List<Map<String, APPrestation>> liste = new ArrayList<Map<String, APPrestation>>();
                // Ajout de la première prestations dans la map
                prestationsPourCalculTotalJours.put(prestations.get(0).getId(), prestations.get(0));
                for (int i = 1; i < prestations.size(); i++) {
                    Date dateFinPrestActuellePlusUn = new Date(prestations.get(i - 1).getDateFin()).addDays(1);
                    Date dateDebutPrestSuivante = new Date(prestations.get(i).getDateDebut());
                    if (dateFinPrestActuellePlusUn.equals(dateDebutPrestSuivante)) {
                        prestationsPourCalculTotalJours.put(prestations.get(i).getId(), prestations.get(i));
                    } else {
                        liste.add(prestationsPourCalculTotalJours);
                        prestationsPourCalculTotalJours = new HashMap<String, APPrestation>();
                        prestationsPourCalculTotalJours.put(prestations.get(i).getId(), prestations.get(i));
                    }
                }
                liste.add(prestationsPourCalculTotalJours);

                Map<String, APPrestation> mapAvecDroitCourant = getMapAvecDroitCourant(liste, idDroitCourant);

                if (mapAvecDroitCourant != null) {
                    return false;
                }
            } catch (Exception e) {
                throwRuleExecutionException(e);
            }
        }
        return true;
    }

    private Map<String, APPrestation> getMapAvecDroitCourant(List<Map<String, APPrestation>> liste,
            String idDroitCourant) {
        for (Map<String, APPrestation> map : liste) {
            for (Map.Entry<String, APPrestation> entry : map.entrySet()) {
                APPrestation prestation = entry.getValue();
                if (prestation.getIdDroit().equals(idDroitCourant) && isNbresJoursDepasses(map)) {
                    return map;
                }
            }
        }
        return null;
    }

    private boolean isNbresJoursDepasses(Map<String, APPrestation> map) {
        int nbJours = 0;
        for (Map.Entry<String, APPrestation> entry : map.entrySet()) {
            APPrestation prestation = entry.getValue();
            nbJours += Integer.valueOf(prestation.getNombreJoursSoldes());
        }
        if (nbJours > 350) {
            return true;
        } else {
            return false;
        }
    }

    private Map<String, APPrestation> ajouterPrestationPourCalculDuTotalJours(
            Map<String, APPrestation> prestationsPourCalculTotalJours, APPrestation prestation, APPrestation prestation2) {
        if (!prestationsPourCalculTotalJours.containsKey(prestation.getId())) {
            prestationsPourCalculTotalJours.put(prestation.getId(), prestation);
        }
        if (!prestationsPourCalculTotalJours.containsKey(prestation2.getId())) {
            prestationsPourCalculTotalJours.put(prestation2.getId(), prestation2);
        }
        return prestationsPourCalculTotalJours;
    }

    private List<APDroitAvecParent> retrieveDroitsSansParents(List<String> forInGenreService,
            List<String> etatIndesirable, String nss, String dateDebutPeriodeControle, String dateFinPeriodeControle)
            throws Exception {
        // On récupère d'abord tous les droits avec leurs parents
        List<APDroitAvecParent> droitsAvecParents = retrieveDroits(forInGenreService, etatIndesirable, nss,
                dateDebutPeriodeControle, dateFinPeriodeControle);
        // Ensuite on vas filter les parents des droits
        return skipDroitParent(droitsAvecParents);
    }

    /**
     * Recherche toutes les prestations des droits passés en paramètres puis retourne celles qui sont dans la période de
     * contrôle passé en paramètre triées par date de début la plus ancienne à la plus récente
     * 
     * @param List<APDroitAvecParent> droits
     * @param JadePeriodWrapper periodeControle
     * @return Liste contenant les périodes des droits passés en paramètre et qui sont dans la période de contrôle
     * @throws Exception
     */
    private List<APPrestation> initPrestationsDesDroits(List<APDroitAvecParent> droits,
            JadePeriodWrapper periodeControle) throws Exception {
        List<APPrestation> allPrestations = new ArrayList<APPrestation>();
        APPrestationManager prestationManager = new APPrestationManager();
        prestationManager.setSession(getSession());
        prestationManager.setForGenre(APTypeDePrestation.STANDARD.getCodesystemString());
        prestationManager.setForType(IAPPrestation.CS_TYPE_NORMAL);
        prestationManager.setForContenuAnnonce(IAPAnnonce.CS_DEMANDE_ALLOCATION);

        for (APDroitAvecParent droit : droits) {
            prestationManager.setForIdDroit(droit.getIdDroit());
            prestationManager.find();

            for (Object p : prestationManager.getContainer()) {
                APPrestation prestation = (APPrestation) p;
                if (isPrestationDansPeriodeControle(periodeControle, prestation)) {
                    allPrestations.add(prestation);
                }
            }
        }

        return sortPrestationsByDate(allPrestations);
    }

    private List<APPrestation> sortPrestationsByDate(List<APPrestation> allPrestations) {
        Collections.sort(allPrestations, new Comparator<APPrestation>() {
            @Override
            public int compare(APPrestation o1, APPrestation o2) {
                return new Date(o1.getDateDebut()).compareTo(new Date(o2.getDateDebut()));
            }
        });
        return allPrestations;
    }

    private List<APDroitAvecParent> retrieveDroits(List<String> forInGenreService, List<String> etatIndesirable,
            String nss, String dateDebutPeriodeControle, String dateFinPeriodeControle) throws Exception {
        // On recherche tous les droits liés à ce NSS pour la période donnée
        APDroitAPGJointTiersManager manager = new APDroitAPGJointTiersManager();
        manager.setSession(getSession());
        manager.setForCsGenreServiceIn(forInGenreService);
        manager.setForEtatDroitNotIn(etatIndesirable);
        manager.setLikeNumeroAvs(nss);
        manager.setForDroitContenuDansDateDebut(dateDebutPeriodeControle);
        manager.setForDroitContenuDansDateFin(dateFinPeriodeControle);
        manager.find(BManager.SIZE_NOLIMIT);
        return manager.getContainer();
    }

    /**
     * Ne pas traiter les droits en état refusé ou transféré
     * 
     * @return list contenant les droit non désirés
     */
    private List<String> initEtatDroitIndesirable() {
        List<String> etatIndesirable = new ArrayList<String>();
        etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
        etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);
        return etatIndesirable;
    }

    private void validateFields(APChampsAnnonce champsAnnonce) throws APRuleExecutionException {
        if (JadeStringUtil.isIntegerEmpty(champsAnnonce.getIdDroit())) {
            throwRuleExecutionException(new NumberFormatException(
                    "Can not retrieve a valid id for the current APDroitLAPG"));
        }

        testDateNotEmptyAndValid(champsAnnonce.getStartOfPeriod(), "startOfPeriod");
        validNotEmpty(champsAnnonce.getInsurant(), "nss");
    }

    private List<APPrestation> retrievePrestationsFromDroit(String idDroit) throws Exception {
        List<APPrestation> prestations = new ArrayList<APPrestation>();
        APPrestationManager prestationManager = new APPrestationManager();
        prestationManager.setSession(getSession());
        prestationManager.setForGenre(APTypeDePrestation.STANDARD.getCodesystemString());
        prestationManager.setForType(IAPPrestation.CS_TYPE_NORMAL); // pas les prestations annulées
        prestationManager.setForContenuAnnonce(IAPAnnonce.CS_DEMANDE_ALLOCATION);
        prestationManager.setForIdDroit(idDroit);
        prestationManager.setOrderBy(APPrestation.FIELDNAME_DATEDEBUT);
        prestationManager.find(BManager.SIZE_NOLIMIT);
        for (Object p : prestationManager.getContainer()) {
            prestations.add((APPrestation) p);
        }
        return prestations;
    }

    private boolean isPrestationDansPeriodeControle(JadePeriodWrapper periodeControle, APPrestation prestation) {
        return periodeControle.isDateDansLaPeriode(prestation.getDateDebut())
                && periodeControle.isDateDansLaPeriode(prestation.getDateFin());
    }
}
