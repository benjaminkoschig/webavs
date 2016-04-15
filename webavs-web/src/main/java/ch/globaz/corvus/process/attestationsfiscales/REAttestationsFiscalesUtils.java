package ch.globaz.corvus.process.attestationsfiscales;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import ch.globaz.prestation.domaine.CodePrestation;
import globaz.corvus.api.retenues.IRERetenues;
import globaz.corvus.utils.codeprestation.enums.RECodePrestationResolver;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.tools.PRDateFormater;

public class REAttestationsFiscalesUtils {

    /**
     * <p>
     * Défini si une retenue de type "Impôt à la source" a été en cours durant l'année fiscale voulue.
     * </p>
     * </p>
     * Si c'est le cas, aucune attestation ne devrait sortir.
     * </p>
     *
     * @param famille
     * @param annee
     *            l'année fiscale voulue
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
     * @param dateDeDebut la date de début de la période
     * @param dateDeFin la date de fin de la période
     * @param anneeReference l'année de référence
     * @throws IllegalArgumentException Si le paramètre <code>dateDeDebut</code> est vide OU si le paramètre
     *             <code>anneeReference</code>
     * @throws NumberFormatException Si la récupération des années sous forme d'entiers des paramètres
     *             <code>dateDeDebut, dateDeFin, anneeReference</code> échoue
     * @return <code>true</code> si la période déterminée par <code>dateDeDebut, dateDeFin</code> chevauche ou est
     *         comprise dans l'année de référence <code>anneeReference</code>
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
     * Parcours les rentes de la famille à la recherche de rente bloquée au 31 décembre de l'année fiscale voulue.<br/>
     * Toute rente bloquée ayant une date de fin antérieur ou comprise dans l'année fiscale sera considérée comme valide
     * (non-bloquante pour la génération d'une attestation fiscale).
     * </p>
     * <p>
     * Si une rente bloquée est trouvée, aucune attestation fiscale ne doit sortir pour ce cas.
     * </p>
     *
     * @param famille
     * @param annee
     *            l'année fiscale voulue
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
            CodePrestation codePrestation = CodePrestation
                    .getCodePrestation(Integer.parseInt(uneRente.getCodePrestation()));

            if (codePrestation.isRentePrincipale()) {
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

        // parcours des périodes à la recherche de trous dans l'année fiscale (elle sont ordonnées par ancienneté)
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
            if (JadeDateUtil.isGlobazDateMonthYear(periodeGlobaleDuTiers.getDateFin())) {
                dateFinPeriodeGlobale = "01." + periodeGlobaleDuTiers.getDateFin();
            } else {
                dateFinPeriodeGlobale = periodeGlobaleDuTiers.getDateFin();
            }

            // on regarde si la période globale de ce tiers s'est terminée durant l'année fiscale
            if (periodeFiscale.isDateDansLaPeriode(dateFinPeriodeGlobale)) {
                return true;
            }
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

    public static boolean hasRetroDansAnneeFiscale(REFamillePourAttestationsFiscales famille, String annee) {
        for (RERentePourAttestationsFiscales uneRente : famille.getRentesDeLaFamille()) {

            CodePrestation codePrestation = CodePrestation
                    .getCodePrestation(Integer.parseInt(uneRente.getCodePrestation()));

            if (codePrestation.isAPI()) {
                continue;
            }

            if (!annee.equals(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(uneRente.getDateDecision()))) {
                continue;
            }

            String moisDecision = JadeDateUtil.convertDateMonthYear(uneRente.getDateDecision());
            if (JadeDateUtil.isGlobazDateMonthYear(uneRente.getDateDebutDroit())
                    && !JadeDateUtil.isDateMonthYearBefore(moisDecision, uneRente.getDateDebutDroit())) {
                return true;
            }
        }
        return false;
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
     * @param annee L'année concernée
     * @return <code>true</code> si la famille ne possède pas de rente principale et que toute les rentes
     *         complémentaire ont une date de fin
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
     * Retourne <code>true</code> si le tiers de correspondance à peu être retrouvé selon différentes règles et s'il
     * possède une adresse valide.
     *
     * Cette méthode s'appuye sur la méthode {@link REAttestationsFiscalesUtils}.getTiersCorrespondance(famille, annee)
     *
     * @param uneFamille la famille à analyser
     * @param annee L'année fiscale
     * @return <code>true</code> si le tiers de correspondance à peu être retrouvé selon différentes règles et s'il
     */
    public static boolean hasTiersCorrespondanceAvecAdresseValide(REFamillePourAttestationsFiscales uneFamille,
            String annee) {
        RETiersPourAttestationsFiscales tiersCorrespondance = REAttestationsFiscalesUtils
                .getTiersCorrespondance(uneFamille, annee);
        return tiersCorrespondance != null && !JadeStringUtil.isBlank(tiersCorrespondance.getAdresseCourrierFormatee());
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
        // Si des rentes de survivants sont versées à la famille une analyse doit être faite pour déterminer le tiers
        // pour correspondance
        if (analyseurLot2.isFamilleDansLot(uneFamille) || analyseurLot4.isFamilleDansLot(uneFamille)) {
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
}
