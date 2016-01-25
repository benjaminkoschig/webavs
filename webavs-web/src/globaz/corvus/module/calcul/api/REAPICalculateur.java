package globaz.corvus.module.calcul.api;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.demandes.REPeriodeAPI;
import globaz.corvus.db.demandes.REPeriodeAPIManager;
import globaz.corvus.db.parametrages.ReParametrageAPI;
import globaz.corvus.db.parametrages.ReParametrageAPIManager;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author SCR Calculateur de prestation API
 */
public class REAPICalculateur {

    List<REMontantReferenceAPI> montantsAPI = null;
    BSession session = null;

    public REAPICalculateur(BSession session) {
        this.session = session;
    }

    /**
     * Cette méthode calcul le montant d'une prestation API par période.<br/>
     * La description du calcul se trouve dans le document : <a
     * href="X:\Clients\WebPrestation\analyse\Corvus\RENValidationBatch.doc">
     * X:\Clients\WebPrestation\analyse\Corvus\RENValidationBatch.doc </a>
     * 
     * @param demandeRenteAPI
     * @return un tableau avec tous les montants API par année. Ordonnée par type de prestation + date début, croissant.
     * @throws Exception
     */
    public REMontantPrestationAPIParPeriode[] calculerPrestationAPI(REDemandeRenteAPI demandeRenteAPI,
            String idTiersBeneficiaire) throws Exception {

        // Récupération des périodes API
        // Les périodes sont ordonnées par date de début d'invalidité (ordre croissant)
        REPeriodeAPIManager mgr = new REPeriodeAPIManager();
        mgr.setSession(session);
        mgr.setForIdDemandeRente(demandeRenteAPI.getIdDemandeRente());
        mgr.find(BManager.SIZE_NOLIMIT);

        loadMontantsAPI();
        // BZ 4862
        JADate today = new JADate(REPmtMensuel.getDateDernierPmt(session));
        // date fin max de l'année en cours
        String dernierJourAnneeEnCours = "31.12." + today.getYear();

        List<REMontantPrestationAPIParPeriode> sousPeriodes = new ArrayList<REMontantPrestationAPIParPeriode>();
        SortedSet<REMontantPrestationAPIParPeriode> result = new TreeSet<REMontantPrestationAPIParPeriode>(
                new REPeriodeAPIComparator());

        // Subdivision des périodes en sous périodes si nécessaire.
        //
        // Montant référence par année :
        // [ 1000.- ][ 1100.- ][ 1200.- ] --> t
        //
        // Périodes API
        // [ ][ ]
        // Périodes subdivisée
        // [ ][ ][ ]

        JACalendar cal = new JACalendarGregorian();

        PRTiersWrapper tiersBeneficiaire = PRTiersHelper.getTiersParId(session, idTiersBeneficiaire);
        String dateDeces = tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_DATE_DECES);

        for (int i = 0; i < mgr.size(); i++) {
            REPeriodeAPI periode = (REPeriodeAPI) mgr.getEntity(i);

            // Check des dates de début et fin par rapport à la date de dépôt(-1 an)
            String dateDebutInvalidite = periode.getDateDebutInvalidite();
            String dateFinInvalidite = periode.getDateFinInvalidite();

            if (JadeStringUtil.isBlankOrZero(dateFinInvalidite)) {
                dateFinInvalidite = dernierJourAnneeEnCours;
            }

            // période d'invalidité : [******]
            // date de dépôt : |
            //
            // Cas 1) [******] |
            // la date de dépôt est plus récente que la date de fin de la période
            //
            // Cas 2) [******|
            // la date de dépôt correspond à la date de fin de la période
            //
            // Cas 3) [***|***]
            // la date de dépôt est contenue dans la période
            //
            // Cas 4) |******]
            // la date de dépôt correspond à la date de début de la période
            //
            // Cas 5) | [******]
            // la date de dépôt est plus ancienne que la date de début de la période
            //
            // Si Cas 1 --> ok
            // Si cas 2 --> OK
            // Si cas 3 --> OK
            // Si cas 4 --> OK
            // Si cas 5 --> OK

            int annee = new JADate(periode.getDateDebutInvalidite()).getYear();

            REMontantPrestationAPIParPeriode plusGrandePeriodeCommune = loadPGPC(session, annee, periode);
            if (plusGrandePeriodeCommune != null) {
                if (!JadeStringUtil.isBlankOrZero(dateDeces)
                        && (JadeDateUtil.isDateBefore(dateDeces, plusGrandePeriodeCommune.getDateFin()) || JadeStringUtil
                                .isBlankOrZero(plusGrandePeriodeCommune.getDateFin()))) {
                    plusGrandePeriodeCommune.setDateFin(dateDeces);
                }
                sousPeriodes.add(plusGrandePeriodeCommune);
            }

            while ((cal.compare("31.12." + String.valueOf(annee), plusGrandePeriodeCommune.getDateFin()) == JACalendar.COMPARE_EQUALS)
                    && (cal.compare("31.12." + String.valueOf(annee), dernierJourAnneeEnCours) == JACalendar.COMPARE_FIRSTLOWER)) {
                plusGrandePeriodeCommune = loadPGPC(session, ++annee, periode);
                if (plusGrandePeriodeCommune != null) {

                    if (!JadeStringUtil.isBlankOrZero(plusGrandePeriodeCommune.getDateFin())
                            && (cal.compare(plusGrandePeriodeCommune.getDateDebut(),
                                    plusGrandePeriodeCommune.getDateFin()) == JACalendar.COMPARE_FIRSTUPPER)) {
                        continue;
                    }

                    // Si la dernière période API n'a pas de date de fin,
                    // la dernière des plusGrandePeriodeCommune ne doit pas en avoir non plus !!!
                    if (JadeStringUtil.isBlankOrZero(periode.getDateFinInvalidite())) {
                        if (!JadeStringUtil.isBlankOrZero(plusGrandePeriodeCommune.getDateFin())
                                && (cal.compare(plusGrandePeriodeCommune.getDateFin(), dernierJourAnneeEnCours) == JACalendar.COMPARE_EQUALS)) {
                            plusGrandePeriodeCommune.setDateFin("");
                        }
                    }

                    sousPeriodes.add(plusGrandePeriodeCommune);
                }
            }
        }

        // si sous-périodes est vide, ajouter le message d'erreur :
        if (sousPeriodes.isEmpty()) {
            throw new Exception(session.getLabel("JSP_CADR_ERROR_PERIODE"));
        }

        // Calcul des montants pour chaque sous période.
        for (Iterator<REMontantPrestationAPIParPeriode> iter = sousPeriodes.iterator(); iter.hasNext();) {
            REMontantPrestationAPIParPeriode element = iter.next();
            // Recherche des montants API en fonction de l'année
            REMontantReferenceAPI montantRefAPI = getMontantAPI(element.getDateDebut());

            REPeriodeAPI periodeAPI = new REPeriodeAPI();
            periodeAPI.setSession(session);
            periodeAPI.setIdPeriodeAPI(element.getIdPeriode());
            periodeAPI.retrieve();

            // Si le type de prestation est en-dessous de 89 et si le degré
            // d'impotence est faible, multiplication de la rente maximale par
            // 0,2.
            // Si le type de prestation est au-dessus ou égal à 89 et si le
            // degré d'impotence est faible, multiplication de la rente minimale
            // par 0,2.
            // Si le type de prestation est en-dessous de 89 et si le degré
            // d'impotence est moyen, multiplication de la rente maximale par
            // 0,5.
            // Si le type de prestation est au-dessus ou égal à 89 et si le
            // degré d'impotence est moyen, multiplication de la rente minimale
            // par 0,5.
            // Si le type de prestation est en-dessous de 89 et si le degré
            // d'impotence est grave, multiplication de la rente maximale par
            // 0,8.
            // Si le type de prestation est au-dessus ou égal à 89 et si le
            // degré d'impotence est grave, multiplication de la rente minimale
            // par 0,8.

            BigDecimal renteMax = montantRefAPI.getRenteMaximale();
            BigDecimal renteMin = montantRefAPI.getRenteMinimale();
            BigDecimal montant = new BigDecimal(0);

            int typePrestation = 0;
            try {
                typePrestation = Integer.valueOf(periodeAPI.getTypePrestation());
            } catch (Exception e) {
                typePrestation = 0;
            }

            if (typePrestation == 0) {
                throw new Exception(session.getLabel("VALID_CALCUL_PERIODE_API_ERREUR_PERIODE_SANS_TYPE_PRESTATION"));
            }

            int typePrestationPrecedent = 0;
            try {
                typePrestationPrecedent = Integer.valueOf(periodeAPI.getTypePrestationHistorique());
            } catch (Exception e) {
                typePrestationPrecedent = 0;
            }

            boolean isDateDebutPeriodeGreaterEgalJuillet2014 = JadeDateUtil.isDateAfter(
                    periodeAPI.getDateDebutInvalidite(), "30.06.2014");

            if ((typePrestation == 94 || typePrestation == 95)
                    && (typePrestationPrecedent == 91 || typePrestationPrecedent == 95)) {
                montant = renteMin.multiply(new BigDecimal(0.2));
                montant = montant.divide(BigDecimal.valueOf(2));

                if (!isDateDebutPeriodeGreaterEgalJuillet2014) {
                    element.setCodeCasSpecial("91");
                }

            } else if (typePrestation == 97 && typePrestationPrecedent == 86
                    && isDateDebutPeriodeGreaterEgalJuillet2014) {
                montant = renteMax.multiply(new BigDecimal(0.5));
                element.setCodeCasSpecial("40");
            } else if ((typePrestation < 89)
                    && IREDemandeRente.CS_DEGRE_IMPOTENCE_FAIBLE.equals(periodeAPI.getCsDegreImpotence())) {
                montant = renteMax.multiply(new BigDecimal(0.2));
            } else if ((typePrestation >= 89)
                    && IREDemandeRente.CS_DEGRE_IMPOTENCE_FAIBLE.equals(periodeAPI.getCsDegreImpotence())) {
                montant = renteMin.multiply(new BigDecimal(0.2));
            } else if ((typePrestation < 89)
                    && IREDemandeRente.CS_DEGRE_IMPOTENCE_MOYEN.equals(periodeAPI.getCsDegreImpotence())) {
                montant = renteMax.multiply(new BigDecimal(0.5));
            } else if ((typePrestation >= 89)
                    && IREDemandeRente.CS_DEGRE_IMPOTENCE_MOYEN.equals(periodeAPI.getCsDegreImpotence())) {
                montant = renteMin.multiply(new BigDecimal(0.5));
            } else if ((typePrestation < 89)
                    && IREDemandeRente.CS_DEGRE_IMPOTENCE_GRAVE.equals(periodeAPI.getCsDegreImpotence())) {
                montant = renteMax.multiply(new BigDecimal(0.8));
            } else if ((typePrestation >= 89)
                    && IREDemandeRente.CS_DEGRE_IMPOTENCE_GRAVE.equals(periodeAPI.getCsDegreImpotence())) {
                montant = renteMin.multiply(new BigDecimal(0.8));
            }

            // InfoRom403 : dans le cas d'API 91/92/93, si la période chevauche les dates 01.01.2011 - 31.12.2012,
            // on créer deux sous période en splittant à la date du 01.01.2012 pour un calcul du montant différent
            switch (typePrestation) {
                case REGenresPrestations.RENTE_API_DOMICILE_OU_HOME_DEGRES_FAIBLE_IMPOTENCE_AI:
                case REGenresPrestations.RENTE_API_DOMICILE_OU_HOME_DEGRES_MOYEN_IMPOTENCE_AI:
                case REGenresPrestations.RENTE_API_DOMICILE_OU_HOME_DEGRES_GRAVE_IMPOTENCE_AI:
                    int annee = Integer.parseInt(element.getDateDebut().substring(6));
                    // BZ8008
                    if (annee >= 2012) {
                        montant = montant.divide(BigDecimal.valueOf(2));
                    }
                    break;
            }

            REMontantPrestationAPIParPeriode pp = new REMontantPrestationAPIParPeriode();
            pp.setDateDebut(element.getDateDebut());
            pp.setDateFin(element.getDateFin());
            pp.setIdPeriode(element.getIdPeriode());
            pp.setTypePrestation(String.valueOf(typePrestation));
            pp.setCsGenreDroitApi(periodeAPI.getCsGenreDroitApi());
            pp.setCodeCasSpecial(element.getCodeCasSpecial());

            // 2 décimal après la virgule
            // si chiffres apres la virgule, arrondir au franc sup.
            montant = montant.setScale(2, BigDecimal.ROUND_HALF_UP);
            montant = JANumberFormatter.round(montant, 1, 2, JANumberFormatter.SUP);
            pp.setMontant(montant);
            result.add(pp);
        }

        // Fusions des périodes avec les mêmes montants...
        SortedSet<REMontantPrestationAPIParPeriode> fusions = new TreeSet<REMontantPrestationAPIParPeriode>(
                new REPeriodeAPIComparator());

        REMontantPrestationAPIParPeriode elm1 = null;
        REMontantPrestationAPIParPeriode elm2 = null;

        Iterator<REMontantPrestationAPIParPeriode> iterator = result.iterator();

        REMontantPrestationAPIParPeriode elmDeFusion = null;

        while (iterator.hasNext()) {
            if (elmDeFusion == null) {
                elm1 = iterator.next();
            } else {
                elm1 = new REMontantPrestationAPIParPeriode(elmDeFusion);
            }

            if (iterator.hasNext()) {
                elm2 = iterator.next();

                elmDeFusion = merge(elm1, elm2);
                if (elmDeFusion == null) {
                    fusions.add(elm1);
                    elmDeFusion = new REMontantPrestationAPIParPeriode(elm2);
                }
            } else {
                fusions.add(elm1);
            }

        }
        if (elmDeFusion != null) {
            fusions.add(elmDeFusion);
        }
        return fusions.toArray(new REMontantPrestationAPIParPeriode[fusions.size()]);
    }

    private REMontantReferenceAPI getMontantAPI(String date) throws Exception {
        JADate jDate = new JADate(date);
        int year = jDate.getYear();

        for (Iterator<REMontantReferenceAPI> iter = montantsAPI.iterator(); iter.hasNext();) {
            REMontantReferenceAPI element = iter.next();
            if (year == element.getAnnee()) {
                return element;
            }
        }
        throw new Exception("Aucun montant API ne correspond à la date : " + date);
    }

    /**
     * @return max year
     * @throws Exception
     */
    private int loadMontantsAPI() throws Exception {

        int result = -1;
        if (montantsAPI != null) {
            return result;
        } else {

            montantsAPI = new ArrayList<REMontantReferenceAPI>();

            ReParametrageAPIManager mgr = new ReParametrageAPIManager();

            mgr.setSession(session);

            mgr.find(BManager.SIZE_NOLIMIT);

            for (Iterator<ReParametrageAPI> iter = mgr.iterator(); iter.hasNext();) {

                ReParametrageAPI element = iter.next();

                int yyyyDebut = (new JADate(element.getDateDebut())).getYear();
                int yyyyFin = (new JADate(element.getDateFin())).getYear();

                if (result < yyyyFin) {
                    result = yyyyFin;
                }

                if (yyyyDebut == yyyyFin) {
                    REMontantReferenceAPI mntAPI = new REMontantReferenceAPI(yyyyDebut, new BigDecimal(
                            element.getMontantApiMin()), new BigDecimal(element.getMontantApiMax()));
                    montantsAPI.add(mntAPI);
                } else {
                    for (int i = yyyyDebut; i <= yyyyFin; i++) {
                        REMontantReferenceAPI mntAPI = new REMontantReferenceAPI(i, new BigDecimal(
                                element.getMontantApiMin()), new BigDecimal(element.getMontantApiMax()));
                        montantsAPI.add(mntAPI);
                    }

                }
            }
            return result;
        }
    }

    /**
     * Charge la plus grande période commune (pgpc) pour une période de base donnée (pd) en fonction d'une année
     * complète.
     * 
     * <pre>
     * Cas 0 : Tous les cas ou il n'y a pas de date de fin dans la période fournie... 
     *        
     *          a)  [       2005         ]        
     *                      [  pd     
     *                      [   pgpc     ]
     *                        
     *          b)  [       2005         ]        
     *                                      [  pd     
     *                                      pgpc == null
     *                                   
     *          c)              [       2005         ]        
     *                  [  pd     
     *                          [ pgpc               ]
     *        
     *        
     * 
     *       
     *         Cas 1   [       2005         ] 
     *                      [  pd     ]
     *                      [   pgpc  ]
     * 
     * 
     *         Cas 2     [       2005         ] 
     *               [  pd     ]
     *                   [ pgpc]
     * 
     *         
     *         Cas 3     [       2005         ] 
     *                                 [  pd     ]
     *                                 [ pgpc ]
     *         
     * 
     *          Cas 4     [       2005         ] 
     *                  [                    pd     ]
     *                    [         pgpc       ]
     *         
     * 
     *          Cas 5     [       2005         ] 
     *                                             [ pd  ]
     *                      pgpc == null
     * </pre>
     * 
     * @param session
     * @param transaction
     * @param période
     * @param annee
     * @return pgpc
     * @throws Exception
     */

    private REMontantPrestationAPIParPeriode loadPGPC(BSession session, int annee, REPeriodeAPI periode)
            throws Exception {

        JACalendar cal = new JACalendarGregorian();

        REMontantPrestationAPIParPeriode pgpc = new REMontantPrestationAPIParPeriode();
        pgpc.setIdPeriode(periode.getIdPeriodeAPI());
        String ddAnnee = "01.01." + String.valueOf(annee);
        String dfAnnee = "31.12." + String.valueOf(annee);

        // Cas 0
        if (JadeStringUtil.isBlankOrZero(periode.getDateFinInvalidite())) {

            pgpc.setDateFin(dfAnnee);

            if ((cal.compare(periode.getDateDebutInvalidite(), dfAnnee) == JACalendar.COMPARE_FIRSTUPPER)) {

                return null;
            } else if ((cal.compare(periode.getDateDebutInvalidite(), ddAnnee) == JACalendar.COMPARE_FIRSTLOWER)) {
                pgpc.setDateDebut(ddAnnee);
            } else {
                pgpc.setDateDebut(periode.getDateDebutInvalidite());
            }
        }

        // Cas 1
        else if (((cal.compare(periode.getDateDebutInvalidite(), ddAnnee) == JACalendar.COMPARE_FIRSTUPPER) || (cal
                .compare(periode.getDateDebutInvalidite(), ddAnnee) == JACalendar.COMPARE_EQUALS))
                && !JadeStringUtil.isBlankOrZero(periode.getDateFinInvalidite())
                && ((cal.compare(periode.getDateFinInvalidite(), dfAnnee) == JACalendar.COMPARE_FIRSTLOWER) || (cal
                        .compare(periode.getDateFinInvalidite(), dfAnnee) == JACalendar.COMPARE_EQUALS))) {

            pgpc.setDateDebut(periode.getDateDebutInvalidite());
            pgpc.setDateFin(periode.getDateFinInvalidite());

        }

        // Cas 2
        else if ((cal.compare(periode.getDateDebutInvalidite(), ddAnnee) == JACalendar.COMPARE_FIRSTLOWER)
                &&

                !JadeStringUtil.isBlankOrZero(periode.getDateFinInvalidite())
                &&

                ((cal.compare(periode.getDateFinInvalidite(), dfAnnee) == JACalendar.COMPARE_FIRSTLOWER) || (cal
                        .compare(periode.getDateFinInvalidite(), dfAnnee) == JACalendar.COMPARE_EQUALS))) {

            pgpc.setDateDebut(ddAnnee);
            pgpc.setDateFin(periode.getDateFinInvalidite());

        }

        // Cas 3
        else if (((cal.compare(periode.getDateDebutInvalidite(), dfAnnee) == JACalendar.COMPARE_FIRSTLOWER) || (cal
                .compare(periode.getDateDebutInvalidite(), dfAnnee) == JACalendar.COMPARE_EQUALS))
                && ((cal.compare(periode.getDateDebutInvalidite(), ddAnnee) == JACalendar.COMPARE_FIRSTUPPER) || (cal
                        .compare(periode.getDateDebutInvalidite(), ddAnnee) == JACalendar.COMPARE_EQUALS))
                && !JadeStringUtil.isBlankOrZero(periode.getDateFinInvalidite())
                && ((cal.compare(periode.getDateFinInvalidite(), dfAnnee) == JACalendar.COMPARE_FIRSTUPPER) || (cal
                        .compare(periode.getDateFinInvalidite(), dfAnnee) == JACalendar.COMPARE_EQUALS))) {

            pgpc.setDateDebut(periode.getDateDebutInvalidite());
            pgpc.setDateFin(dfAnnee);

        }

        // Cas 4
        else if ((cal.compare(periode.getDateDebutInvalidite(), ddAnnee) == JACalendar.COMPARE_FIRSTLOWER)
                && !JadeStringUtil.isBlankOrZero(periode.getDateFinInvalidite())
                && (cal.compare(periode.getDateFinInvalidite(), dfAnnee) == JACalendar.COMPARE_FIRSTUPPER)) {

            pgpc.setDateDebut(ddAnnee);
            pgpc.setDateFin(dfAnnee);

        } else {
            return null;
        }
        return pgpc;
    }

    private REMontantPrestationAPIParPeriode merge(REMontantPrestationAPIParPeriode elm1,
            REMontantPrestationAPIParPeriode elm2) {

        REMontantPrestationAPIParPeriode result = new REMontantPrestationAPIParPeriode(elm1);

        // Si elm identique, on fusionne...
        if (elm1.getMontant().equals(elm2.getMontant()) && elm1.getTypePrestation().equals(elm2.getTypePrestation())
                && elm1.getIdPeriode().equals(elm2.getIdPeriode())
                && elm1.getCsGenreDroitApi().equalsIgnoreCase(elm2.getCsGenreDroitApi())) {

            result.setDateFin(elm2.getDateFin());
            return result;
        }
        return null;

    }
}
