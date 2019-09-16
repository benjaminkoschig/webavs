package ch.globaz.corvus.process.attestationsfiscales;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.db.attestationsFiscales.REAttestationFiscaleRentAccordOrdreVerse;
import globaz.corvus.db.attestationsFiscales.REAttestationFiscaleRentAccordOrdreVerseManager;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeNumericUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.prestation.domaine.CodePrestation;
import globaz.corvus.api.retenues.IRERetenues;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.utils.codeprestation.enums.RECodePrestationResolver;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.beans.PRPeriode;
import globaz.prestation.tools.PRDateFormater;

public class REAttestationsFiscalesUtils {

    private static final int YEAR_MONTH_MAX_VALUE = 999999;

    private static final Logger logger = LoggerFactory.getLogger(REAttestationsFiscalesUtils.class);

    /**
     * <p>
     * Défini si une retenue de type "Impôt à la source" a été en cours durant l'année fiscale voulue.
     * </p>
     * </p>
     * Si c'est le cas, aucune attestation ne devrait sortir.
     * </p>
     *
     * @param famille
     * @param annee   l'année fiscale voulue
     * @return
     */
    public static boolean hasImpotSourceVerseeDansAnnee(REFamillePourAttestationsFiscales famille, String annee) {
        for (RERentePourAttestationsFiscales uneRente : famille.getRentesDeLaFamille()) {
            for (RERetenuePourAttestationsFiscales uneRetenue : uneRente.getRetenues()) {
                if (!IRERetenues.CS_TYPE_IMPOT_SOURCE.equals(uneRetenue.getCsType())) {
                    continue;
                }
                try {
                    if (isPeriodeDeRetenueDansAnnee(uneRetenue.getDateDebut(), uneRetenue.getDateFin(), annee)) {
                        return true;
                    }
                } catch (Exception exception) {
                    JadeLogger.error(REAttestationsFiscalesUtils.class, exception);
                }
            }
        }
        return false;
    }

    /**
     * Test si l'<code>annee</code> est contenue dans la période
     *
     * @param dateDeDebut    la date de début de la période
     * @param dateDeFin      la date de fin de la période
     * @param anneeReference l'année de référence
     * @return <code>true</code> si la période déterminée par <code>dateDeDebut, dateDeFin</code> chevauche ou est
     * comprise dans l'année de référence <code>anneeReference</code>
     * @throws IllegalArgumentException Si le paramètre <code>dateDeDebut</code> est vide OU si le paramètre
     *                                  <code>anneeReference</code>
     * @throws NumberFormatException    Si la récupération des années sous forme d'entiers des paramètres
     *                                  <code>dateDeDebut, dateDeFin, anneeReference</code> échoue
     */
    public static boolean isPeriodeDeRetenueDansAnnee(String dateDeDebut, String dateDeFin, String anneeReference) {
        if (JadeStringUtil.isBlank(dateDeDebut)) {
            throw new IllegalArgumentException("La date 'dateDeDebut' doit être renseigné");
        }
        if (JadeStringUtil.isBlank(anneeReference)) {
            throw new IllegalArgumentException("L'année de référence 'anneeReference' doit être renseigné");
        }

        boolean hasDateDeFin = !JadeStringUtil.isBlank(dateDeFin);
        int annee = Integer.valueOf(anneeReference);
        int anneeDeDebut = Integer.valueOf(dateDeDebut.substring(dateDeDebut.lastIndexOf(".") + 1));
        int anneeDeFin = 0;
        if (hasDateDeFin) {
            anneeDeFin = Integer.valueOf(dateDeFin.substring(dateDeFin.lastIndexOf(".") + 1));
        }

        if (hasDateDeFin) {
            // Si année de début est plus grande que l'année de référence on est pas compris dans la période
            if (anneeDeDebut > annee) {
                return false;
                // Si année de début == année de référence on a obligatoirement une retenue
            } else if (anneeDeDebut == annee) {
                return true;
            }
            // Date de début plus petite que l'année de référence
            else {
                if (hasDateDeFin) {
                    return anneeDeFin >= annee;
                }
                // Si année de début < que l'année de référence et pas de date de fin c'est que la retennue est toujours
                // active
                else {
                    return true;
                }
            }
        } else {
            return anneeDeDebut <= annee;
        }
    }

    public static boolean hasPersonneDecedeeDurantAnneeFiscale(REFamillePourAttestationsFiscales famille,
                                                               String annee) {
        JadePeriodWrapper anneeFiscale = new JadePeriodWrapper("01.01." + annee, "31.12." + annee);
        for (RETiersPourAttestationsFiscales unTiers : famille.getTiersBeneficiaires()) {
            if (anneeFiscale.isDateDansLaPeriode(unTiers.getDateDeces())) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * Parcours les rentes de la famille à la recherche de rente bloquée au 31 décembre de l'année fiscale voulue.<br/>
     * Toute rente bloquée ayant une date de fin antérieur ou comprise dans l'année fiscale sera considérée comme valide
     * (non-bloquante pour la génération d'une attestation fiscale).
     * </p>
     * <p>
     * Si une rente bloquée est trouvée, aucune attestation fiscale ne doit sortir pour ce cas.
     * </p>
     *
     * @param famille
     * @param annee   l'année fiscale voulue
     * @return
     */
    public static boolean hasRenteBloquee(REFamillePourAttestationsFiscales famille, String annee) {
        for (RERentePourAttestationsFiscales uneRente : famille.getRentesDeLaFamille()) {
            // rente bloquée avec date de fin vide ou date de fin plus grande que l'année fiscales -> true
            if (uneRente.isRenteBloquee() && (JadeStringUtil.isBlank(uneRente.getDateFinDroit())
                    || JadeDateUtil.isDateMonthYearAfter(uneRente.getDateFinDroit(), "12." + annee))) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * Retournera <code>true</code> si la rente principale de cette famille se termine dans l'année, sans une autre
     * rente principale pour reprendre le droit.
     * </p>
     *
     * @param famille
     * @param annee
     * @return
     */
    public static boolean hasRenteFinissantDansAnnee(REFamillePourAttestationsFiscales famille, String annee) {
        Map<String, SortedSet<JadePeriodWrapper>> periodesDesRentesParTiers = new HashMap<String, SortedSet<JadePeriodWrapper>>();

        // regroupement des périodes par tiers
        for (RERentePourAttestationsFiscales uneRente : famille.getRentesDeLaFamille()) {
            // uniquement les rentes principales

            if (RECodePrestationResolver.isPrestationPrincipale(uneRente.getCodePrestation())) {
                SortedSet<JadePeriodWrapper> periodeDuTiers;
                if (periodesDesRentesParTiers.containsKey(uneRente.getIdTiersBeneficiaire())) {
                    periodeDuTiers = periodesDesRentesParTiers.get(uneRente.getIdTiersBeneficiaire());
                } else {
                    periodeDuTiers = new TreeSet<JadePeriodWrapper>();
                    periodesDesRentesParTiers.put(uneRente.getIdTiersBeneficiaire(), periodeDuTiers);
                }

                JadePeriodWrapper periodeDeCetteRente = new JadePeriodWrapper(uneRente.getDateDebutDroit(),
                        uneRente.getDateFinDroit());
                periodeDuTiers.add(periodeDeCetteRente);
            }
        }
        boolean hasAuMoinsUnePeriodeValable = false;

        // parcours des périodes à la recherche de trous dans l'année fiscale (elle sont ordonnées par ancienneté)
        for (SortedSet<JadePeriodWrapper> periodesDuTiers : periodesDesRentesParTiers.values()) {
            JadePeriodWrapper periodeGlobaleDuTiers = null;
            for (Iterator<JadePeriodWrapper> iterator = periodesDuTiers.iterator(); iterator.hasNext(); ) {
                JadePeriodWrapper unePeriodeDuTiers = iterator.next();

                if (!isDateDefinPlusPetiteQueDateDeDebut(unePeriodeDuTiers)) {

                    if (periodeGlobaleDuTiers == null) {
                        periodeGlobaleDuTiers = new JadePeriodWrapper(unePeriodeDuTiers.getDateDebut(),
                                unePeriodeDuTiers.getDateFin());
                    } else {
                        JadePeriodWrapper unionDesPeriodes = periodeGlobaleDuTiers.union(unePeriodeDuTiers);
                        // Si l'union n'a rien donnée (période qui ne se suivent pas) et que la période globale
                        // se termine dans l'année fiscale, c'est qu'il y a un trou ou une fin de droit -> return true
                        if (unionDesPeriodes == null) {
                            if (periodeGlobaleDuTiers.isDateDansLaPeriode("01.01." + annee) && !periodeGlobaleDuTiers
                                    .isDateDansLaPeriode("01.01." + (Integer.parseInt(annee) + 1))) {
                                return true;
                            }
                        } else {
                            periodeGlobaleDuTiers = unionDesPeriodes;
                        }
                    }
                }
            }

            JadePeriodWrapper periodeFiscale = new JadePeriodWrapper("01.01." + annee, "31.12." + annee);

            String dateFinPeriodeGlobale = null;

            if (periodeGlobaleDuTiers != null) {
                hasAuMoinsUnePeriodeValable = true;
                if (JadeDateUtil.isGlobazDateMonthYear(periodeGlobaleDuTiers.getDateFin())) {
                    dateFinPeriodeGlobale = "01." + periodeGlobaleDuTiers.getDateFin();
                } else {
                    dateFinPeriodeGlobale = periodeGlobaleDuTiers.getDateFin();
                }
            }
            // on regarde si la période globale de ce tiers s'est terminée durant l'année fiscale
            if (periodeFiscale.isDateDansLaPeriode(dateFinPeriodeGlobale)) {
                return true;
            }
        }

        if (!hasAuMoinsUnePeriodeValable) {
            return hasUniquementRenteComplementaireAvecDateFin(famille, annee);
        }
        if (hasUniquementRenteComplementaireAvecDateFin(famille, annee)) {
            return true;
        }
        return false;
    }

    /**
     * Test si la date de fin est plus petite que la date de début
     * Format de Date attendu MM.yyyy
     *
     * @param periode
     * @return
     */
    private static boolean isDateDefinPlusPetiteQueDateDeDebut(JadePeriodWrapper periode) {
        if (JadeStringUtil.isBlankOrZero(periode.getDateFin())) {
            return false;
        }
        SimpleDateFormat reader = new SimpleDateFormat("MM.yyyy");
        Date dateDebut = null;
        Date dateFin = null;
        try {
            dateDebut = reader.parse(periode.getDateDebut());
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                    "Wrong dateDebut format, expected [MM.yyyy], received value [" + periode.getDateDebut() + "]", e);
        }

        try {
            dateFin = reader.parse(periode.getDateFin());
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                    "Wrong dateFin format, expected [MM.yyyy], received value [" + periode.getDateFin() + "]", e);
        }

        SimpleDateFormat writer = new SimpleDateFormat("yyyyMM");

        int debut = Integer.valueOf(writer.format(dateDebut));
        int fin = Integer.valueOf(writer.format(dateFin));

        return fin < debut;
    }

    /**
     * Retourne <code>true</code> si la famille n'a pas de décision en cours d'année MAIS possède au moins une décision
     * en décembre
     *
     * @param famille La famille à analyser
     * @param annee   L'année fiscales
     * @return <code>true</code> si la famille n'a pas de décision en cours d'année MAIS possède au moins une décision
     * en décembre
     */
    public static boolean hasSeulementDecisionEnDecembre(REFamillePourAttestationsFiscales famille, int annee) {
        List<RERentePourAttestationsFiscales> rentesSansDecembre = rechercherLesrentesAvecRetro(famille, annee, true);
        List<RERentePourAttestationsFiscales> rentesAvecDecembre = rechercherLesrentesAvecRetro(famille, annee, false);

        return rentesSansDecembre.size() != rentesAvecDecembre.size();
    }

    /**
     * Retourne <code><code>true</code> si au moins une des décision dans l'année présente du rétro sur une ou plusieurs
     * années
     *
     * @return
     */
    public static boolean hasRetro(REFamillePourAttestationsFiscales famille, int annee) {
        return !rechercherLesrentesAvecRetro(famille, annee, true).isEmpty();
    }

    /**
     * Recherche toutes les rentes qui possèdent du rétro. Les rentes de types API sont exclues de l'analyse
     * Les règles d'analyse du rétro son centralisés dans cette méthode
     *
     * @param famille              la famille à analyser
     * @param annee                l'année fiscale
     * @param skipDecisionDecembre si les décision du mois de décembre de l'année fiscale doivent être ignorées
     * @return
     */
    private static List<RERentePourAttestationsFiscales> rechercherLesrentesAvecRetro(
            REFamillePourAttestationsFiscales famille, int annee, boolean skipDecisionDecembre) {

        List<RERentePourAttestationsFiscales> listeRentes = new LinkedList<RERentePourAttestationsFiscales>();

        SimpleDateFormat monthYearReader = new SimpleDateFormat("MM.yyyy");
        SimpleDateFormat dayMonthYearReader = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat yearMonthFormatter = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormatter = new SimpleDateFormat("MM");

        for (RERentePourAttestationsFiscales rente : famille.getRentesDeLaFamille()) {

            CodePrestation codePrestation = CodePrestation
                    .getCodePrestation(Integer.parseInt(rente.getCodePrestation()));

            // On exclus les types API
            if (codePrestation.isAPI()) {
                continue;
            }

            try {
                // Analyse de la date de décision
                String dateDecision = rente.getDateDecision();

                if (JadeStringUtil.isBlankOrZero(dateDecision)) {
                    continue;
                }

                int anneeMoisDeLaDecisionInteger = Integer
                        .valueOf(yearMonthFormatter.format(dayMonthYearReader.parse(dateDecision)));

                // Si la date de décision n'est pas dans l'année fiscales on exclu la rente
                if (Integer.valueOf(yearFormatter.format(dayMonthYearReader.parse(dateDecision))) != annee) {
                    continue;
                }

                // Si le mois de décision est décembre on ignore on exclu la rente
                if (Integer.valueOf(monthFormatter.format(dayMonthYearReader.parse(dateDecision))) == 12) {
                    if (skipDecisionDecembre) {
                        continue;
                    }
                }

                // On recherche la date de début de droit la plus
                String dateDebutDroit = rente.getDateDebutDroit();
                // Si la date de début de droit est vide on l'ignore !
                if (JadeStringUtil.isBlankOrZero(dateDebutDroit)) {
                    continue;
                }

                int anneeMoisDebutRenteInteger = Integer
                        .valueOf(yearMonthFormatter.format(monthYearReader.parse(dateDebutDroit)));

                // Si la rente ne contient pas de retro on n'en tiens pas compte
                if (anneeMoisDebutRenteInteger > anneeMoisDeLaDecisionInteger) {
                    continue;
                }

                listeRentes.add(rente);

            } catch (ParseException e) {
                logger.error("Exception thrown when parsing RERentePourAttestationsFiscales idRenteAccordee=["
                        + rente.getIdRenteAccordee() + "] : " + e.toString(), e);
            }
        }

        return listeRentes;

    }

    /**
     * Recherche si la famille possède une décision au mois de décembre de l'année fiscale
     * Les rentes API ne sont pas prisent en compte
     *
     * @param famille la famille à analyser
     * @param annee   l'année fiscale
     * @return
     */
    public static boolean hasDecisionEnDecembre(REFamillePourAttestationsFiscales famille, int annee) {
        SimpleDateFormat dayMonthYearReader = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormatter = new SimpleDateFormat("MM");

        for (RERentePourAttestationsFiscales rente : famille.getRentesDeLaFamille()) {
            try {
                CodePrestation codePrestation = CodePrestation
                        .getCodePrestation(Integer.parseInt(rente.getCodePrestation()));

                // On exclus les types API
                if (codePrestation.isAPI()) {
                    continue;
                }

                // Analyse de la date de décision
                String dateDecision = rente.getDateDecision();

                if (JadeStringUtil.isBlankOrZero(dateDecision)) {
                    continue;
                }

                // Si la date de décision n'est pas dans l'année fiscale elle nous intéresse pas
                if (Integer.valueOf(yearFormatter.format(dayMonthYearReader.parse(dateDecision))) != annee) {
                    continue;
                }

                // Est-ce que le mois de décision est décembre
                if (Integer.valueOf(monthFormatter.format(dayMonthYearReader.parse(dateDecision))) == 12) {
                    return true;
                }

            } catch (ParseException e) {
                logger.error("Exception thrown when parsing RERentePourAttestationsFiscales idRenteAccordee=["
                        + rente.getIdRenteAccordee() + "] : " + e.toString(), e);
            }
        }
        return false;

    }

    /**
     * Recherche si les rentes de la famille possèdent une décision avec du rétro sur une ou plusieurs années.
     * Cette méthode s'appuie sur la méthode {@link REAttestationsFiscalesUtils.rechercherLesrentesAvecRetro()} Cette
     * méthode ignore les décision du mois de décembre de l'année fiscale
     *
     * @param famille           La famille à analyser
     * @param surPlusieursAnnee Si le rétro doit s'étendre sur plusieurs années
     * @param annee             L'année fiscale
     * @return <code>true</code> si au moins une décision avec rétro est retrouvée
     */
    public static boolean analyserRetro(REFamillePourAttestationsFiscales famille,
                                        boolean rechercherRetroSurUneAnneeUniquement, int annee) {

        List<RERentePourAttestationsFiscales> listeRentes = rechercherLesrentesAvecRetro(famille, annee, true);

        if (listeRentes == null || listeRentes.isEmpty()) {
            return false;
        }

        SimpleDateFormat monthYearReader = new SimpleDateFormat("MM.yyyy");
        SimpleDateFormat yearMonthFormatter = new SimpleDateFormat("yyyyMM");

        int dateDebutPlusAncienne = YEAR_MONTH_MAX_VALUE;
        for (RERentePourAttestationsFiscales rente : listeRentes) {

            try {
                // On recherche la date de début de droit la plus
                String dateDebutDroit = rente.getDateDebutDroit();
                int anneeMoisDebutRenteInteger = Integer
                        .valueOf(yearMonthFormatter.format(monthYearReader.parse(dateDebutDroit)));

                if (anneeMoisDebutRenteInteger < dateDebutPlusAncienne) {
                    dateDebutPlusAncienne = anneeMoisDebutRenteInteger;
                }

            } catch (ParseException e) {
                logger.error(
                        "Exception thrown when parsing RERentePourAttestationsFiscales dateDebutDroit with idRenteAccordee=["
                                + rente.getIdRenteAccordee() + "] : " + e.toString(),
                        e);
            }

        }

        // Si aucune date de début de rente avec rétro n'été trouvée
        if (dateDebutPlusAncienne == YEAR_MONTH_MAX_VALUE) {
            logger.warn(
                    "Erreur lors de la recherche de la data de dateDebutPlusAncienne pour les rentes de la famille du tiers requérant =["
                            + famille.getTiersRequerant().getIdTiers() + "]");
            return false;
        }

        /*
         * Année de début de la rente la plus ancienne
         */
        int anneeRentePlusAncienne = dateDebutPlusAncienne / 100;

        if (rechercherRetroSurUneAnneeUniquement) {
            return anneeRentePlusAncienne == annee;
        } else {
            return anneeRentePlusAncienne < annee;
        }
    }

    public static boolean isAttestationRenteSurvivant(REFamillePourAttestationsFiscales uneFamille) {
        for (RERentePourAttestationsFiscales uneRente : uneFamille.getRentesDeLaFamille()) {

            CodePrestation codePrestation = CodePrestation
                    .getCodePrestation(Integer.parseInt(uneRente.getCodePrestation()));

            if (codePrestation.isSurvivant()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAvecDecisionPendantAnneeFiscale(REFamillePourAttestationsFiscales famille, String annee) {
        //
        for (RERentePourAttestationsFiscales uneRente : famille.getRentesDeLaFamille()) {
            if (!JadeStringUtil.isBlank(uneRente.getDateDecision()) && (Integer.parseInt(annee) == Integer
                    .parseInt((PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(uneRente.getDateDecision()))))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSansDecisionPendantEtApresAnneeFiscale(REFamillePourAttestationsFiscales famille,
                                                                   String annee) {

        for (RERentePourAttestationsFiscales uneRente : famille.getRentesDeLaFamille()) {
            if (!JadeStringUtil.isBlank(uneRente.getDateDecision()) && (Integer.parseInt(annee) <= Integer
                    .parseInt((PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(uneRente.getDateDecision()))))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retourne <code>true</code> si la famille ne possède pas de rente principale et que toute les rentes
     * complémentaire ont une date de fin
     *
     * @param famille Les données de la famille à analyser
     * @param annee   L'année concernée
     * @return <code>true</code> si la famille ne possède pas de rente principale et que toute les rentes
     * complémentaire ont une date de fin
     */
    public static boolean hasUniquementRenteComplementaireAvecDateFin(REFamillePourAttestationsFiscales famille,
                                                                      String annee) {
        // En premier lieu on regarde déjà si une ou es rentes principales sont présentes
        for (RERentePourAttestationsFiscales rente : famille.getRentesDeLaFamille()) {
            if (RECodePrestationResolver.isPrestationPrincipale(rente.getCodePrestation())) {
                return false;
            }
        }
        // Pas de rente principale, on regarde si toutes les RA ont une date de fin
        for (RERentePourAttestationsFiscales rente : famille.getRentesDeLaFamille()) {
            if (JadeStringUtil.isBlankOrZero(rente.getDateFinDroit())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retourne le tiers de correspondance si le tiers de correspondance à peu être retrouvé selon différentes règles et
     * s'il
     * possède une adresse valide.
     * <p>
     * Cette méthode s'appuye sur la méthode {@link REAttestationsFiscalesUtils}.getTiersCorrespondance(famille, annee)
     *
     * @param uneFamille la famille à analyser
     * @param annee      L'année fiscale
     * @return <code>true</code> si le tiers de correspondance à peu être retrouvé selon différentes règles et s'il
     */
    public static RETiersPourAttestationsFiscales getTiersCorrespondanceAvecAdresseValide(
            REFamillePourAttestationsFiscales uneFamille, String annee) {
        RETiersPourAttestationsFiscales tiersCorrespondance = REAttestationsFiscalesUtils
                .getTiersCorrespondance(uneFamille, annee);
        if (tiersCorrespondance != null) {
            if (!JadeStringUtil.isBlank(tiersCorrespondance.getCodeIsoLangue())
                    && !JadeStringUtil.isBlank(tiersCorrespondance.getAdresseCourrierFormatee())) {
                return tiersCorrespondance;
            }
        }
        return null;
    }

    /**
     * Méthode utilitaire pour retrouver le tiers de correspondance.
     * Gère les cas particulier liés aux rentes de survivants
     *
     * @param uneFamille
     * @return
     */
    public static RETiersPourAttestationsFiscales getTiersCorrespondance(REFamillePourAttestationsFiscales uneFamille,
                                                                         String annee) {
        REAnalyseurLot2 analyseurLot2 = new REAnalyseurLot2(annee);
        REAnalyseurLot4 analyseurLot4 = new REAnalyseurLot4(annee);
        REAnalyseurLot7 analyseurLot7 = new REAnalyseurLot7(annee);
        REAnalyseurLot8 analyseurLot8 = new REAnalyseurLot8(annee);
        // Si des rentes de survivants sont versées à la famille une analyse doit être faite pour déterminer le tiers
        // pour correspondance
        if (analyseurLot2.isFamilleDansLot(uneFamille) || analyseurLot4.isFamilleDansLot(uneFamille)
                || analyseurLot7.isFamilleDansLot(uneFamille) || analyseurLot8.isFamilleDansLot(uneFamille)) {
            RETiersPourAttestationsFiscales tiersBeneficiaireRentePrinciapel = getBeneficiaireRenteSurvivant(
                    uneFamille);
            if (tiersBeneficiaireRentePrinciapel != null) {
                return tiersBeneficiaireRentePrinciapel;
            }
        }
        return uneFamille.getTiersRequerant();
    }

    /**
     * Cette méthode est responsable de retrouver le tiers de correspondance dans le cas ou des rentes de survivant sont
     * versées à la famille
     *
     * @param famille la famille à traiter
     * @return le tiers bénéficiaire à utiliser pour la correspondance
     */
    private static RETiersPourAttestationsFiscales getBeneficiaireRenteSurvivant(
            REFamillePourAttestationsFiscales famille) {
        RETiersPourAttestationsFiscales tiersBeneficiaire = null;
        int codePrestationDuTiersBeneficiaire = Integer.MAX_VALUE;

        // Avant de récupérer le tiers bénéficiaire dans la famille, il faut regarder si le tiers requérant possède une
        // rente principale
        for (RERentePourAttestationsFiscales rente : famille.getTiersRequerant().getRentes()) {
            if (RECodePrestationResolver.isPrestationPrincipale(rente.getCodePrestation())) {
                tiersBeneficiaire = famille.getTiersRequerant();
            }

        }

        // Si le tiers requérant n'a pas de rente principale, on va regarder dans la famille si quelqu'un touche une
        // rente principale
        if (tiersBeneficiaire == null) {
            for (RERentePourAttestationsFiscales rente : famille.getRentesDeLaFamille()) {
                if (RECodePrestationResolver.isPrestationPrincipale(rente.getCodePrestation())) {
                    String idTiersBeneficiaire = rente.getIdTiersBeneficiaire();
                    for (RETiersPourAttestationsFiscales tiers : famille.getTiersBeneficiaires()) {
                        if (idTiersBeneficiaire.equals(tiers.getIdTiers())) {
                            tiersBeneficiaire = tiers;
                            break;
                        }
                    }
                    if (tiersBeneficiaire == null) {
                        System.err.println();
                    }
                }
            }
        }

        // Si le tiers requérant et la famille n'ont pas de rente principale, on continue la recherche sur une rente de
        // survivant
        if (tiersBeneficiaire == null) {
            for (RETiersPourAttestationsFiscales unTiers : famille.getTiersBeneficiaires()) {
                if (JadeStringUtil.isBlank(unTiers.getAdresseCourrierFormatee())) {
                    continue;
                }
                for (RERentePourAttestationsFiscales uneRente : unTiers.getRentes()) {
                    CodePrestation codePrestation = CodePrestation
                            .getCodePrestation(Integer.parseInt(uneRente.getCodePrestation()));
                    if (codePrestation.isSurvivant()) {
                        if ((tiersBeneficiaire == null)
                                || (codePrestation.getCodePrestation() < codePrestationDuTiersBeneficiaire)) {
                            tiersBeneficiaire = unTiers;
                            codePrestationDuTiersBeneficiaire = codePrestation.getCodePrestation();
                        }
                    }
                }
            }
        }
        return tiersBeneficiaire;
    }


    /**
     * Vérifie qu'il n'y a que des rentes avec des dates de fin inférieure à la date de début
     *
     * @param famille
     * @return boolean
     */
    public static boolean hasRenteDateFinAvantDebutUniquement(REFamillePourAttestationsFiscales famille) {
        SimpleDateFormat reader1 = new SimpleDateFormat("MM.yyyy");
        List<RERentePourAttestationsFiscales> listPeriodeInverse = new ArrayList<>();
        if (famille.getRentesDeLaFamille().isEmpty()) {
            return false;
        }
        for (RERentePourAttestationsFiscales rente : famille.getRentesDeLaFamille()) {
            if (!JadeStringUtil.isBlankOrZero(rente.getDateDebutDroit())
                    && !JadeStringUtil.isBlankOrZero(rente.getDateFinDroit())) {
                try {
                    Date dateDebut = reader1.parse(rente.getDateDebutDroit());
                    Date dateFin = reader1.parse(rente.getDateFinDroit());
                    if (dateFin.before(dateDebut)) {
                        listPeriodeInverse.add(rente);
                    }
                } catch (Exception e) {
                    logger.warn("Exception lors du contrôle du chevauchement de période pour la rente accordée avec l'id =["
                            + rente.getIdRenteAccordee() + "]");
                    continue;
                }
            }
        }

        // s'il n'y a que des periode avec des dates de fin avant date de début
        return listPeriodeInverse.size() == famille.getRentesDeLaFamille().size();
    }

    public static boolean isAjournementMontant0(REFamillePourAttestationsFiscales uneFamille, int i) throws Exception {
        boolean isAjournement = false;
        for (RERentePourAttestationsFiscales att : uneFamille.getRentesDeLaFamille()) {
            String montant = att.getMontantPrestation();
            String idRenteAccord = att.getIdRenteAccordee();
            RERenteAccordeeManager mgr = new RERenteAccordeeManager();
            mgr.setSession(BSessionUtil.getSessionFromThreadContext());
            mgr.setForIdRenteAccordee(idRenteAccord);
            mgr.find();
            RERenteAccordee re = null;
            if (mgr.size() > 0) {
                re = (RERenteAccordee) mgr.getFirstEntity();
            }
            if (JadeStringUtil.isBlankOrZero(montant) && re.contientCodeCasSpecial("08")) {
                isAjournement = true;
            } else {
                isAjournement = false;
            }
        }

        return isAjournement;
    }

    public static boolean hasDecisionEnDecembreAndCreancier(REFamillePourAttestationsFiscales uneFamille, BSession session, String annee) throws Exception {
        int anneeNum = Integer.parseInt(annee);
        for (RERentePourAttestationsFiscales att : uneFamille.getRentesDeLaFamille()) {
            if (JadeStringUtil.isBlankOrZero(att.getDateDecision())) {
                continue;
            }
            Calendar dateDecision = JadeDateUtil.getGlobazCalendar(att.getDateDecision());
            if (dateDecision.get(Calendar.MONTH) == Calendar.DECEMBER) {
                Calendar dateDebutRente = JadeDateUtil.getGlobazCalendar("01."+att.getDateDebutDroit());
                if (dateDebutRente.get(Calendar.YEAR) == anneeNum && hasCreancier(att.getIdRenteAccordee(),session)) {
                    return true;
                }
                if (dateDebutRente.get(Calendar.YEAR) < anneeNum) {
                    return true;
                }
            }

        }
        return false;
    }
    private static boolean hasCreancier(String idRenteAccordee, BSession session) throws Exception {
        REAttestationFiscaleRentAccordOrdreVerseManager mgr = new REAttestationFiscaleRentAccordOrdreVerseManager();
        mgr.setSession(session);
        mgr.setForIdRenteAccordee(idRenteAccordee);
        mgr.find(BManager.SIZE_NOLIMIT);
        if (mgr.size() > 0) {
            for (int i = 0; i < mgr.size(); i++) {
                REAttestationFiscaleRentAccordOrdreVerse ovs = (REAttestationFiscaleRentAccordOrdreVerse) mgr
                        .get(i);
                if (ovs.hasVersementCreancier() || !JadeStringUtil.isBlankOrZero(ovs.getMontantDette())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * On contrôle s'il existe un chevauchement de rente pour cette famille : même type de rente et période qui se chevauche.
     *
     * @param uneFamille : la famille pour laquelle on teste le chevauchement.
     * @param annee : l'année analysée.
     * @return vrai s'il y a un chevauchement.
     */
    public static boolean hasRenteQuiSeChevauchent(REFamillePourAttestationsFiscales uneFamille, int annee) {
        List<RERentePourAttestationsFiscales> allRentes = uneFamille.getRentesDeLaFamille();
        RERentePourAttestationsFiscales rente1;
        RERentePourAttestationsFiscales rente2;
        LocalDate anneeAnalyse = LocalDate.ofYearDay(annee,1);
        for (int i = 0; i < allRentes.size(); i++) {
            for (int j = i + 1; j < allRentes.size(); j++) {
                rente1 = allRentes.get(i);
                rente2 = allRentes.get(j);
                if (hasSameCodePrestation(rente1, rente2) && hasPeriodOverlap(rente1, rente2, anneeAnalyse)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Vérifie si les deux rentes sont de même type : si elles ont le même code prestation.
     *
     * @param rente1 : première rente à comparer.
     * @param rente2 : seconde rente à comparer.
     * @return vrai si les deux rentes sont de même type, non sinon.
     */
    private static boolean hasSameCodePrestation(RERentePourAttestationsFiscales rente1, RERentePourAttestationsFiscales rente2) {
        return Objects.equals(rente1.getCodePrestation(), rente2.getCodePrestation());
    }

    /**
     * Vérifie si les deux rentes ont une période qui se chevauche.
     *
     * @param rente1 : première rente à comparer.
     * @param rente2 : seconde rente à comparer.
     * @param annee : l'année analysée.
     * @return vrai si les deux rentes ont une période qui se chevauche, non sinon.
     */
    private static boolean hasPeriodOverlap(RERentePourAttestationsFiscales rente1, RERentePourAttestationsFiscales rente2, LocalDate annee) {
        // Si la période de fin est null pour les deux rentes, il y a chevauchement.
        if (StringUtils.isEmpty(rente1.getDateFinDroit()) && StringUtils.isEmpty(rente2.getDateFinDroit())) {
            return true;
        }

        LocalDate rente1DateDebut = getDateDebut(rente1.getDateDebutDroit());
        LocalDate rente2DateDebut = getDateDebut(rente2.getDateDebutDroit());
        LocalDate rente1DateFin = getDateFin(rente1.getDateFinDroit(), annee);
        LocalDate rente2DateFin = getDateFin(rente2.getDateFinDroit() , annee);

        if (rente1DateDebut.isAfter(rente1DateFin) || rente2DateDebut.isAfter(rente2DateFin)) {
            return false;
        }

        if (!rente1DateDebut.isAfter(rente2DateFin) && !rente2DateDebut.isAfter(rente1DateFin)) {
            return true;
        }
        return false;
    }

    private static LocalDate getDateDebut(String dateDebutDroit) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate renteDateDebut = LocalDate.parse("01." + dateDebutDroit , formatter);
        return renteDateDebut;
    }

    private static LocalDate getDateFin(String dateFinDroit, LocalDate annee) {
        if (StringUtils.isNotEmpty(dateFinDroit)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate renteDateFin = LocalDate.parse("01." + dateFinDroit , formatter);
                return renteDateFin;
        }
        return LocalDate.of(annee.getYear(), Month.DECEMBER,31);
    }
}
