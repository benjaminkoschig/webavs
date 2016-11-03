package ch.globaz.corvus.process.attestationsfiscales;

import globaz.corvus.api.retenues.IRERetenues;
import globaz.corvus.utils.codeprestation.enums.RECodePrestationResolver;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.beans.PRPeriode;
import globaz.prestation.tools.PRDateFormater;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.prestation.domaine.CodePrestation;

public class REAttestationsFiscalesUtils {

    private static final int YEAR_MONTH_MAX_VALUE = 999999;

    private static final Logger logger = LoggerFactory.getLogger(REAttestationsFiscalesUtils.class);

    /**
     * <p>
     * D�fini si une retenue de type "Imp�t � la source" a �t� en cours durant l'ann�e fiscale voulue.
     * </p>
     * </p>
     * Si c'est le cas, aucune attestation ne devrait sortir.
     * </p>
     * 
     * @param famille
     * @param annee
     *            l'ann�e fiscale voulue
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
     * Test si l'<code>annee</code> est contenue dans la p�riode
     * 
     * @param dateDeDebut la date de d�but de la p�riode
     * @param dateDeFin la date de fin de la p�riode
     * @param anneeReference l'ann�e de r�f�rence
     * @throws IllegalArgumentException Si le param�tre <code>dateDeDebut</code> est vide OU si le param�tre
     *             <code>anneeReference</code>
     * @throws NumberFormatException Si la r�cup�ration des ann�es sous forme d'entiers des param�tres
     *             <code>dateDeDebut, dateDeFin, anneeReference</code> �choue
     * @return <code>true</code> si la p�riode d�termin�e par <code>dateDeDebut, dateDeFin</code> chevauche ou est
     *         comprise dans l'ann�e de r�f�rence <code>anneeReference</code>
     */
    public static boolean isPeriodeDeRetenueDansAnnee(String dateDeDebut, String dateDeFin, String anneeReference) {
        if (JadeStringUtil.isBlank(dateDeDebut)) {
            throw new IllegalArgumentException("La date 'dateDeDebut' doit �tre renseign�");
        }
        if (JadeStringUtil.isBlank(anneeReference)) {
            throw new IllegalArgumentException("L'ann�e de r�f�rence 'anneeReference' doit �tre renseign�");
        }

        boolean hasDateDeFin = !JadeStringUtil.isBlank(dateDeFin);
        int annee = Integer.valueOf(anneeReference);
        int anneeDeDebut = Integer.valueOf(dateDeDebut.substring(dateDeDebut.lastIndexOf(".") + 1));
        int anneeDeFin = 0;
        if (hasDateDeFin) {
            anneeDeFin = Integer.valueOf(dateDeFin.substring(dateDeFin.lastIndexOf(".") + 1));
        }

        if (hasDateDeFin) {
            // Si ann�e de d�but est plus grande que l'ann�e de r�f�rence on est pas compris dans la p�riode
            if (anneeDeDebut > annee) {
                return false;
                // Si ann�e de d�but == ann�e de r�f�rence on a obligatoirement une retenue
            } else if (anneeDeDebut == annee) {
                return true;
            }
            // Date de d�but plus petite que l'ann�e de r�f�rence
            else {
                if (hasDateDeFin) {
                    return anneeDeFin >= annee;
                }
                // Si ann�e de d�but < que l'ann�e de r�f�rence et pas de date de fin c'est que la retennue est toujours
                // active
                else {
                    return true;
                }
            }
        } else {
            return anneeDeDebut <= annee;
        }
    }

    public static boolean hasPersonneDecedeeDurantAnneeFiscale(REFamillePourAttestationsFiscales famille, String annee) {
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
     * Parcours les rentes de la famille � la recherche de rente bloqu�e au 31 d�cembre de l'ann�e fiscale voulue.<br/>
     * Toute rente bloqu�e ayant une date de fin ant�rieur ou comprise dans l'ann�e fiscale sera consid�r�e comme valide
     * (non-bloquante pour la g�n�ration d'une attestation fiscale).
     * </p>
     * <p>
     * Si une rente bloqu�e est trouv�e, aucune attestation fiscale ne doit sortir pour ce cas.
     * </p>
     * 
     * @param famille
     * @param annee
     *            l'ann�e fiscale voulue
     * @return
     */
    public static boolean hasRenteBloquee(REFamillePourAttestationsFiscales famille, String annee) {
        for (RERentePourAttestationsFiscales uneRente : famille.getRentesDeLaFamille()) {
            // rente bloqu�e avec date de fin vide ou date de fin plus grande que l'ann�e fiscales -> true
            if (uneRente.isRenteBloquee()
                    && (JadeStringUtil.isBlank(uneRente.getDateFinDroit()) || JadeDateUtil.isDateMonthYearAfter(
                            uneRente.getDateFinDroit(), "12." + annee))) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasRenteQuiSeChevauchent(REFamillePourAttestationsFiscales famille, int annee) {
        Map<String, LinkedList<PRPeriode>> periodesParTiersEtRentes = new HashMap<String, LinkedList<PRPeriode>>();
        SimpleDateFormat reader = new SimpleDateFormat("MM.yyyy");
        SimpleDateFormat writter = new SimpleDateFormat("yyyyMM");
        /*
         * Regroupement des p�riodes par tiers b�n�ficiaire et genre de rente
         */
        for (RERentePourAttestationsFiscales rente : famille.getRentesDeLaFamille()) {
            /*
             * Si la date de fin est plus petite ou �gale � la date de d�but, on ignore cette rente
             */
            if (!JadeStringUtil.isBlankOrZero(rente.getDateDebutDroit())
                    && !JadeStringUtil.isBlankOrZero(rente.getDateFinDroit())) {
                try {
                    int ddd = Integer.valueOf(writter.format(reader.parse(rente.getDateDebutDroit())));
                    int ddf = Integer.valueOf(writter.format(reader.parse(rente.getDateFinDroit())));
                    if (ddf <= ddd) {
                        continue;
                    }
                } catch (Exception e) {
                    logger.warn("Exception lors du contr�le du chevauchement de p�riode pour la rente accord�e avec l'id =["
                            + rente.getIdRenteAccordee() + "]");
                    continue;
                }
            }

            String cle = rente.getIdTiersBeneficiaire() + "-" + rente.getCodePrestation();
            PRPeriode periode = new PRPeriode(rente.getDateDebutDroit(), rente.getDateFinDroit());

            if (!periodesParTiersEtRentes.containsKey(cle)) {
                periodesParTiersEtRentes.put(cle, new LinkedList<PRPeriode>());
            }
            periodesParTiersEtRentes.get(cle).add(periode);
        }

        for (String cle : periodesParTiersEtRentes.keySet()) {
            LinkedList<PRPeriode> periodes = periodesParTiersEtRentes.get(cle);

            /*
             * 1er test :
             * Est-ce qu'il y a 2 rentes sans date de fin -> chevauchement obligatoire
             */
            int ctr = 0;
            for (PRPeriode periode : periodes) {
                if (JadeStringUtil.isBlankOrZero(periode.getDateDeFin())) {
                    ctr++;
                }
            }
            if (ctr > 1) {
                return true;
            }

            /*
             * 2�me test, on analyse les p�riodes
             */
            Collections.sort(periodes);
            PRPeriode previous = null;
            for (PRPeriode periode : periodes) {

                if (previous == null) {
                    previous = periode;
                } else {
                    try {
                        int dateDebut = Integer.valueOf(writter.format(reader.parse(periode.getDateDeDebut())));
                        int dateFin = Integer.valueOf(writter.format(reader.parse(previous.getDateDeFin())));

                        if (dateDebut <= dateFin) {
                            return true;
                        } else {
                            previous = periode;
                        }
                    } catch (Exception e) {
                        logger.warn("Exception lors du contr�le du chevauchement des p�riodes pour le tiers r�qu�rant avec l'id =["
                                + famille.getTiersRequerant().getIdTiers()
                                + "]. P�riodes ayant poy� probl�me : 1=["
                                + previous == null ? "" : previous.toString() + "], 2=[" + periode.toString() + "]");
                    }
                }
            }
        }
        return false;
    }

    /**
     * <p>
     * Retournera <code>true</code> si la rente principale de cette famille se termine dans l'ann�e, sans une autre
     * rente principale pour reprendre le droit.
     * </p>
     * 
     * @param famille
     * @param annee
     * @return
     */
    public static boolean hasRenteFinissantDansAnnee(REFamillePourAttestationsFiscales famille, String annee) {
        Map<String, SortedSet<JadePeriodWrapper>> periodesDesRentesParTiers = new HashMap<String, SortedSet<JadePeriodWrapper>>();

        // regroupement des p�riodes par tiers
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

        // parcours des p�riodes � la recherche de trous dans l'ann�e fiscale (elle sont ordonn�es par anciennet�)
        for (SortedSet<JadePeriodWrapper> periodesDuTiers : periodesDesRentesParTiers.values()) {
            JadePeriodWrapper periodeGlobaleDuTiers = null;
            for (Iterator<JadePeriodWrapper> iterator = periodesDuTiers.iterator(); iterator.hasNext();) {
                JadePeriodWrapper unePeriodeDuTiers = iterator.next();

                if (!isDateDefinPlusPetiteQueDateDeDebut(unePeriodeDuTiers)) {

                    if (periodeGlobaleDuTiers == null) {
                        periodeGlobaleDuTiers = new JadePeriodWrapper(unePeriodeDuTiers.getDateDebut(),
                                unePeriodeDuTiers.getDateFin());
                    } else {
                        JadePeriodWrapper unionDesPeriodes = periodeGlobaleDuTiers.union(unePeriodeDuTiers);
                        // Si l'union n'a rien donn�e (p�riode qui ne se suivent pas) et que la p�riode globale
                        // se termine dans l'ann�e fiscale, c'est qu'il y a un trou ou une fin de droit -> return true
                        if (unionDesPeriodes == null) {
                            if (periodeGlobaleDuTiers.isDateDansLaPeriode("01.01." + annee)
                                    && !periodeGlobaleDuTiers.isDateDansLaPeriode("01.01."
                                            + (Integer.parseInt(annee) + 1))) {
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
            // on regarde si la p�riode globale de ce tiers s'est termin�e durant l'ann�e fiscale
            if (periodeFiscale.isDateDansLaPeriode(dateFinPeriodeGlobale)) {
                return true;
            }
        }

        if (!hasAuMoinsUnePeriodeValable) {
            // FIXME pourquoi retourner (p�riode finis dans l'ann�e) true si aucune p�riode trouv�e
            return true;
        }
        if (hasUniquementRenteComplementaireAvecDateFin(famille, annee)) {
            return true;
        }
        return false;
    }

    /**
     * Test si la date de fin est plus petite que la date de d�but
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
            throw new IllegalArgumentException("Wrong dateDebut format, expected [MM.yyyy], received value ["
                    + periode.getDateDebut() + "]", e);
        }

        try {
            dateFin = reader.parse(periode.getDateFin());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Wrong dateFin format, expected [MM.yyyy], received value ["
                    + periode.getDateFin() + "]", e);
        }

        SimpleDateFormat writer = new SimpleDateFormat("yyyyMM");

        int debut = Integer.valueOf(writer.format(dateDebut));
        int fin = Integer.valueOf(writer.format(dateFin));

        return fin < debut;
    }

    /**
     * Retourne <code>true</code> si la famille n'a pas de d�cision en cours d'ann�e MAIS poss�de au moins une d�cision
     * en d�cembre
     * 
     * @param famille La famille � analyser
     * @param annee L'ann�e fiscales
     * @return <code>true</code> si la famille n'a pas de d�cision en cours d'ann�e MAIS poss�de au moins une d�cision
     *         en d�cembre
     */
    public static boolean hasSeulementDecisionEnDecembre(REFamillePourAttestationsFiscales famille, int annee) {
        List<RERentePourAttestationsFiscales> rentesSansDecembre = rechercherLesrentesAvecRetro(famille, annee, true);
        List<RERentePourAttestationsFiscales> rentesAvecDecembre = rechercherLesrentesAvecRetro(famille, annee, false);

        return rentesSansDecembre.size() != rentesAvecDecembre.size();
    }

    /**
     * Retourne <code><code>true</code> si au moins une des d�cision dans l'ann�e pr�sente du r�tro sur une ou plusieurs
     * ann�es
     * 
     * @return
     */
    public static boolean hasRetro(REFamillePourAttestationsFiscales famille, int annee) {
        return !rechercherLesrentesAvecRetro(famille, annee, true).isEmpty();
    }

    /**
     * Recherche toutes les rentes qui poss�dent du r�tro. Les rentes de types API sont exclues de l'analyse
     * Les r�gles d'analyse du r�tro son centralis�s dans cette m�thode
     * 
     * @param famille la famille � analyser
     * @param annee l'ann�e fiscale
     * @param skipDecisionDecembre si les d�cision du mois de d�cembre de l'ann�e fiscale doivent �tre ignor�es
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
                // Analyse de la date de d�cision
                String dateDecision = rente.getDateDecision();

                if (JadeStringUtil.isBlankOrZero(dateDecision)) {
                    continue;
                }

                int anneeMoisDeLaDecisionInteger = Integer.valueOf(yearMonthFormatter.format(dayMonthYearReader
                        .parse(dateDecision)));

                // Si la date de d�cision n'est pas dans l'ann�e fiscales on exclu la rente
                if (Integer.valueOf(yearFormatter.format(dayMonthYearReader.parse(dateDecision))) != annee) {
                    continue;
                }

                // Si le mois de d�cision est d�cembre on ignore on exclu la rente
                if (Integer.valueOf(monthFormatter.format(dayMonthYearReader.parse(dateDecision))) == 12) {
                    if (skipDecisionDecembre) {
                        continue;
                    }
                }

                // On recherche la date de d�but de droit la plus
                String dateDebutDroit = rente.getDateDebutDroit();
                // Si la date de d�but de droit est vide on l'ignore !
                if (JadeStringUtil.isBlankOrZero(dateDebutDroit)) {
                    continue;
                }

                int anneeMoisDebutRenteInteger = Integer.valueOf(yearMonthFormatter.format(monthYearReader
                        .parse(dateDebutDroit)));

                // Si la rente ne contient pas de retro on n'en tiens pas compte
                if (anneeMoisDebutRenteInteger > anneeMoisDeLaDecisionInteger) {
                    continue;
                }

                listeRentes.add(rente);

            } catch (ParseException e) {
                logger.error(
                        "Exception thrown when parsing RERentePourAttestationsFiscales idRenteAccordee=["
                                + rente.getIdRenteAccordee() + "] : " + e.toString(), e);
            }
        }

        return listeRentes;

    }

    /**
     * Recherche si la famille poss�de une d�cision au mois de d�cembre de l'ann�e fiscale
     * Les rentes API ne sont pas prisent en compte
     * 
     * @param famille la famille � analyser
     * @param annee l'ann�e fiscale
     * @return
     */
    public static boolean hasDecisionEnDecembre(REFamillePourAttestationsFiscales famille, int annee) {
        SimpleDateFormat dayMonthYearReader = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormatter = new SimpleDateFormat("MM");

        for (RERentePourAttestationsFiscales rente : famille.getRentesDeLaFamille()) {
            try {
                CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(rente
                        .getCodePrestation()));

                // On exclus les types API
                if (codePrestation.isAPI()) {
                    continue;
                }

                // Analyse de la date de d�cision
                String dateDecision = rente.getDateDecision();

                if (JadeStringUtil.isBlankOrZero(dateDecision)) {
                    continue;
                }

                // Si la date de d�cision n'est pas dans l'ann�e fiscale elle nous int�resse pas
                if (Integer.valueOf(yearFormatter.format(dayMonthYearReader.parse(dateDecision))) != annee) {
                    continue;
                }

                // Est-ce que le mois de d�cision est d�cembre
                if (Integer.valueOf(monthFormatter.format(dayMonthYearReader.parse(dateDecision))) == 12) {
                    return true;
                }

            } catch (ParseException e) {
                logger.error(
                        "Exception thrown when parsing RERentePourAttestationsFiscales idRenteAccordee=["
                                + rente.getIdRenteAccordee() + "] : " + e.toString(), e);
            }
        }
        return false;

    }

    /**
     * Recherche si les rentes de la famille poss�dent une d�cision avec du r�tro sur une ou plusieurs ann�es.
     * Cette m�thode s'appuie sur la m�thode {@link REAttestationsFiscalesUtils.rechercherLesrentesAvecRetro()} Cette
     * m�thode ignore les d�cision du mois de d�cembre de l'ann�e fiscale
     * 
     * @param famille La famille � analyser
     * @param surPlusieursAnnee Si le r�tro doit s'�tendre sur plusieurs ann�es
     * @param annee L'ann�e fiscale
     * @return <code>true</code> si au moins une d�cision avec r�tro est retrouv�e
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
                // On recherche la date de d�but de droit la plus
                String dateDebutDroit = rente.getDateDebutDroit();
                int anneeMoisDebutRenteInteger = Integer.valueOf(yearMonthFormatter.format(monthYearReader
                        .parse(dateDebutDroit)));

                if (anneeMoisDebutRenteInteger < dateDebutPlusAncienne) {
                    dateDebutPlusAncienne = anneeMoisDebutRenteInteger;
                }

            } catch (ParseException e) {
                logger.error(
                        "Exception thrown when parsing RERentePourAttestationsFiscales dateDebutDroit with idRenteAccordee=["
                                + rente.getIdRenteAccordee() + "] : " + e.toString(), e);
            }

        }

        // Si aucune date de d�but de rente avec r�tro n'�t� trouv�e
        if (dateDebutPlusAncienne == YEAR_MONTH_MAX_VALUE) {
            logger.warn("Erreur lors de la recherche de la data de dateDebutPlusAncienne pour les rentes de la famille du tiers requ�rant =["
                    + famille.getTiersRequerant().getIdTiers() + "]");
            return false;
        }

        /*
         * Ann�e de d�but de la rente la plus ancienne
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

            CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(uneRente
                    .getCodePrestation()));

            if (codePrestation.isSurvivant()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAvecDecisionPendantAnneeFiscale(REFamillePourAttestationsFiscales famille, String annee) {
        //
        for (RERentePourAttestationsFiscales uneRente : famille.getRentesDeLaFamille()) {
            if (!JadeStringUtil.isBlank(uneRente.getDateDecision())
                    && (Integer.parseInt(annee) == Integer.parseInt((PRDateFormater
                            .convertDate_JJxMMxAAAA_to_AAAA(uneRente.getDateDecision()))))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSansDecisionPendantEtApresAnneeFiscale(REFamillePourAttestationsFiscales famille,
            String annee) {

        for (RERentePourAttestationsFiscales uneRente : famille.getRentesDeLaFamille()) {
            if (!JadeStringUtil.isBlank(uneRente.getDateDecision())
                    && (Integer.parseInt(annee) <= Integer.parseInt((PRDateFormater
                            .convertDate_JJxMMxAAAA_to_AAAA(uneRente.getDateDecision()))))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retourne <code>true</code> si la famille ne poss�de pas de rente principale et que toute les rentes
     * compl�mentaire ont une date de fin
     * 
     * @param famille Les donn�es de la famille � analyser
     * @param annee L'ann�e concern�e
     * @return <code>true</code> si la famille ne poss�de pas de rente principale et que toute les rentes
     *         compl�mentaire ont une date de fin
     */
    public static boolean hasUniquementRenteComplementaireAvecDateFin(REFamillePourAttestationsFiscales famille,
            String annee) {
        // En premier lieu on regarde d�j� si une ou es rentes principales sont pr�sentes
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
     * Retourne le tiers de correspondance si le tiers de correspondance � peu �tre retrouv� selon diff�rentes r�gles et
     * s'il
     * poss�de une adresse valide.
     * 
     * Cette m�thode s'appuye sur la m�thode {@link REAttestationsFiscalesUtils}.getTiersCorrespondance(famille, annee)
     * 
     * @param uneFamille la famille � analyser
     * @param annee L'ann�e fiscale
     * @return <code>true</code> si le tiers de correspondance � peu �tre retrouv� selon diff�rentes r�gles et s'il
     */
    public static RETiersPourAttestationsFiscales getTiersCorrespondanceAvecAdresseValide(
            REFamillePourAttestationsFiscales uneFamille, String annee) {
        RETiersPourAttestationsFiscales tiersCorrespondance = REAttestationsFiscalesUtils.getTiersCorrespondance(
                uneFamille, annee);
        if (tiersCorrespondance != null) {
            if (!JadeStringUtil.isBlank(tiersCorrespondance.getCodeIsoLangue())
                    && !JadeStringUtil.isBlank(tiersCorrespondance.getAdresseCourrierFormatee())) {
                return tiersCorrespondance;
            }
        }
        return null;
    }

    /**
     * M�thode utilitaire pour retrouver le tiers de correspondance.
     * G�re les cas particulier li�s aux rentes de survivants
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
        // Si des rentes de survivants sont vers�es � la famille une analyse doit �tre faite pour d�terminer le tiers
        // pour correspondance
        if (analyseurLot2.isFamilleDansLot(uneFamille) || analyseurLot4.isFamilleDansLot(uneFamille)
                || analyseurLot7.isFamilleDansLot(uneFamille) || analyseurLot8.isFamilleDansLot(uneFamille)) {
            RETiersPourAttestationsFiscales tiersBeneficiaireRentePrinciapel = getBeneficiaireRenteSurvivant(uneFamille);
            if (tiersBeneficiaireRentePrinciapel != null) {
                return tiersBeneficiaireRentePrinciapel;
            }
        }
        return uneFamille.getTiersRequerant();
    }

    /**
     * Cette m�thode est responsable de retrouver le tiers de correspondance dans le cas ou des rentes de survivant sont
     * vers�es � la famille
     * 
     * @param famille la famille � traiter
     * @return le tiers b�n�ficiaire � utiliser pour la correspondance
     */
    private static RETiersPourAttestationsFiscales getBeneficiaireRenteSurvivant(
            REFamillePourAttestationsFiscales famille) {
        RETiersPourAttestationsFiscales tiersBeneficiaire = null;
        int codePrestationDuTiersBeneficiaire = Integer.MAX_VALUE;

        // Avant de r�cup�rer le tiers b�n�ficiaire dans la famille, il faut regarder si le tiers requ�rant poss�de une
        // rente principale
        for (RERentePourAttestationsFiscales rente : famille.getTiersRequerant().getRentes()) {
            if (RECodePrestationResolver.isPrestationPrincipale(rente.getCodePrestation())) {
                tiersBeneficiaire = famille.getTiersRequerant();
            }

        }

        // Si le tiers requ�rant n'a pas de rente principale, on va regarder dans la famille si quelqu'un touche une
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

        // Si le tiers requ�rant et la famille n'ont pas de rente principale, on continue la recherche sur une rente de
        // survivant
        if (tiersBeneficiaire == null) {
            for (RETiersPourAttestationsFiscales unTiers : famille.getTiersBeneficiaires()) {
                if (JadeStringUtil.isBlank(unTiers.getAdresseCourrierFormatee())) {
                    continue;
                }
                for (RERentePourAttestationsFiscales uneRente : unTiers.getRentes()) {
                    CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(uneRente
                            .getCodePrestation()));
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
}
