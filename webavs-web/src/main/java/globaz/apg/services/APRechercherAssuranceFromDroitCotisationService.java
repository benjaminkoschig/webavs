package globaz.apg.services;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.IAFAssurance;
import globaz.naos.api.IAFCotisation;
import globaz.naos.application.AFApplication;
import globaz.prestation.tools.PRSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Recherche les assurances selon l'affilie et la période du droit.
 * 
 * @author jje
 * 
 */
public class APRechercherAssuranceFromDroitCotisationService {

    /**
     * 
     * Recherche les assurances selon l'id affilie et l'id du droit. Les assurances retournées correspondent à la
     * période du droit.
     * 
     * @param idDroit
     * @param idAffilieEmployeur
     * @return
     * @throws Exception
     */
    public static List<IAFAssurance> rechercher(String idDroit, String idAffilieEmployeur) throws Exception {
        // TODO: ->service cotisation, si le temps du mandat D00065 le permet
        BISession session = BSessionUtil.getSessionFromThreadContext();
        return rechercher(idDroit, idAffilieEmployeur, session);
    }

    /**
     * 
     * Recherche les assurances selon l'id affilie et l'id du droit. Les assurances retournées correspondent à la
     * période du droit.
     * 
     * @param idDroit
     * @param idAffilieEmployeur
     * @return
     * @throws Exception
     */
    public static List<IAFAssurance> rechercher(String idDroit, String idAffilieEmployeur, BISession session)
            throws Exception {

        APRechercherAssuranceFromDroitCotisationService.validerParametres(idDroit, idAffilieEmployeur);

        // la cotisation doit être validée au moment du droit
        APDroitLAPG droit = new APDroitLAPG();
        droit.setSession((BSession) session);
        droit.setIdDroit(idDroit);
        droit.retrieve();

        // recherche de toutes les cotisations de l'affiliation
        IAFCotisation cotisation = (IAFCotisation) session.getAPIFor(IAFCotisation.class);
        cotisation.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));
        Hashtable<String, String> paramsAffiliation = new Hashtable<String, String>();
        paramsAffiliation.put(IAFCotisation.FIND_FOR_AFFILIATION_ID, idAffilieEmployeur);
        paramsAffiliation.put("_setForInactiveString", "false");
        IAFCotisation[] cotisations = cotisation.findCotisations(paramsAffiliation);

        List<IAFAssurance> assurancesList = new ArrayList<IAFAssurance>();

        // pour toutes les cotisations
        if ((cotisations != null) && (cotisations.length > 0)) {

            HashMap<String, List<IAFCotisation>> hashMapAssuranceCotisation = new HashMap<String, List<IAFCotisation>>();
            for (int i = 0; i < cotisations.length; i++) {

                String courantKey = cotisations[i].getAssuranceId();

                if (!hashMapAssuranceCotisation.containsKey(courantKey)) {
                    hashMapAssuranceCotisation.put(courantKey, new ArrayList<IAFCotisation>());
                }
                hashMapAssuranceCotisation.get(courantKey).add(cotisations[i]);

            }

            for (Map.Entry<String, List<IAFCotisation>> aMapEntry : hashMapAssuranceCotisation.entrySet()) {

                String theSmallestDateDebut = "31.12.2999";
                String theGreatestDateFin = "01.01.1900";
                String theIdAssurance = aMapEntry.getKey();

                for (IAFCotisation aCotisation : aMapEntry.getValue()) {

                    if ((!JadeStringUtil.isBlankOrZero(aCotisation.getDateFin()) && BSessionUtil
                            .compareDateFirstLowerOrEqual((BSession) session, aCotisation.getDateFin(),
                                    droit.getDateDebutDroit()))
                            || BSessionUtil.compareDateFirstGreaterOrEqual((BSession) session,
                                    aCotisation.getDateDebut(), droit.getDateFinDroit())) {
                        continue;

                    }

                    if (BSessionUtil.compareDateFirstLower((BSession) session, aCotisation.getDateDebut(),
                            theSmallestDateDebut)) {
                        theSmallestDateDebut = aCotisation.getDateDebut();

                    }

                    if (JadeStringUtil.isBlankOrZero(aCotisation.getDateFin())) {
                        theGreatestDateFin = "31.12.2999";
                    }

                    if (BSessionUtil.compareDateFirstGreater((BSession) session, aCotisation.getDateFin(),
                            theGreatestDateFin)) {
                        theGreatestDateFin = aCotisation.getDateFin();

                    }

                }

                // pour chaque cotisation validée au moment du droit
                if (BSessionUtil.compareDateFirstLowerOrEqual((BSession) session, theSmallestDateDebut,
                        droit.getDateDebutDroit())
                        && BSessionUtil.compareDateFirstGreaterOrEqual((BSession) session, theGreatestDateFin,
                        droit.getDateFinDroit())) {

                    // on cherche l'assurance
                    IAFAssurance assurance = (IAFAssurance) ((BSession) session).getAPIFor(IAFAssurance.class);
                    assurance.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));
                    assurance.setAssuranceId(theIdAssurance);
                    assurance.retrieve(((BSession) session).getCurrentThreadTransaction());

                    if (!assurance.isNew()) {
                        assurancesList.add(assurance);
                    }
                }

            }

        }

        return assurancesList;
    }

    /**
     *
     * Recherche les assurances selon l'id affilie et l'id du droit. Les assurances retournées correspondent à la
     * période du droit.
     *
     * @param idDroit
     * @param idAffilieEmployeur
     * @return
     * @throws Exception
     */
    public static Map<IAFAssurance, String> rechercherAvecDateDebut(String idDroit, String idAffilieEmployeur, BISession session)
            throws Exception {

        APRechercherAssuranceFromDroitCotisationService.validerParametres(idDroit, idAffilieEmployeur);

        // la cotisation doit être validée au moment du droit
        APDroitLAPG droit = new APDroitLAPG();
        droit.setSession((BSession) session);
        droit.setIdDroit(idDroit);
        droit.retrieve();

        // recherche de toutes les cotisations de l'affiliation
        IAFCotisation cotisation = (IAFCotisation) session.getAPIFor(IAFCotisation.class);
        cotisation.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));
        Hashtable<String, String> paramsAffiliation = new Hashtable<String, String>();
        paramsAffiliation.put(IAFCotisation.FIND_FOR_AFFILIATION_ID, idAffilieEmployeur);
        paramsAffiliation.put("_setForInactiveString", "false");
        IAFCotisation[] cotisations = cotisation.findCotisations(paramsAffiliation);

        Map<IAFAssurance, String> assurancesList = new HashMap();

        // pour toutes les cotisations
        if ((cotisations != null) && (cotisations.length > 0)) {

            HashMap<String, List<IAFCotisation>> hashMapAssuranceCotisation = new HashMap<String, List<IAFCotisation>>();
            for (int i = 0; i < cotisations.length; i++) {

                String courantKey = cotisations[i].getAssuranceId();

                if (!hashMapAssuranceCotisation.containsKey(courantKey)) {
                    hashMapAssuranceCotisation.put(courantKey, new ArrayList<IAFCotisation>());
                }
                hashMapAssuranceCotisation.get(courantKey).add(cotisations[i]);

            }

            for (Map.Entry<String, List<IAFCotisation>> aMapEntry : hashMapAssuranceCotisation.entrySet()) {

                String theSmallestDateDebut = "31.12.2999";
                String theGreatestDateFin = "01.01.1900";
                String theIdAssurance = aMapEntry.getKey();

                for (IAFCotisation aCotisation : aMapEntry.getValue()) {

                    if ((!JadeStringUtil.isBlankOrZero(aCotisation.getDateFin()) && BSessionUtil
                            .compareDateFirstLowerOrEqual((BSession) session, aCotisation.getDateFin(),
                                    droit.getDateDebutDroit()))
                            || BSessionUtil.compareDateFirstGreaterOrEqual((BSession) session,
                                aCotisation.getDateDebut(), droit.getDateFinDroit())) {
                        continue;

                    }

                    if (BSessionUtil.compareDateFirstLower((BSession) session, aCotisation.getDateDebut(),
                            theSmallestDateDebut)) {
                        theSmallestDateDebut = aCotisation.getDateDebut();

                    }

                    if (JadeStringUtil.isBlankOrZero(aCotisation.getDateFin())) {
                        theGreatestDateFin = "31.12.2999";
                    }

                    if (BSessionUtil.compareDateFirstGreater((BSession) session, aCotisation.getDateFin(),
                            theGreatestDateFin)) {
                        theGreatestDateFin = aCotisation.getDateFin();

                    }

                }

                // pour chaque cotisation validée au moment du droit
                if (BSessionUtil.compareDateFirstLowerOrEqual((BSession) session, theSmallestDateDebut,
                        droit.getDateFinDroit())
                        && BSessionUtil.compareDateFirstGreaterOrEqual((BSession) session, theGreatestDateFin,
                        droit.getDateDebutDroit())) {

                    // on cherche l'assurance
                    IAFAssurance assurance = (IAFAssurance) ((BSession) session).getAPIFor(IAFAssurance.class);
                    assurance.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));
                    assurance.setAssuranceId(theIdAssurance);
                    assurance.retrieve(((BSession) session).getCurrentThreadTransaction());

                    if (!assurance.isNew()) {
                        assurancesList.put(assurance, theSmallestDateDebut);
                    }
                }

            }

        }

        return assurancesList;
    }


    private static void validerParametres(String idDroit, String idAffilieEmployeur) {

        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            throw new IllegalArgumentException(
                    "APRechercherAssuranceFromDroitCotisationService.validerParametres() : idDroit");
        }

        if (JadeStringUtil.isBlankOrZero(idAffilieEmployeur)) {
            throw new IllegalArgumentException(
                    "APRechercherAssuranceFromDroitCotisationService.validerParametres() : idAffilieEmployeur");
        }
    }

}
