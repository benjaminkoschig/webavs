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
            if (uneRente.isRenteBloquee() && (JadeStringUtil.isBlank(uneRente.getDateFinDroit())
                    || JadeDateUtil.isDateMonthYearAfter(uneRente.getDateFinDroit(), "12." + annee))) {
                return true;
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

            // on regarde si la p�riode globale de ce tiers s'est termin�e durant l'ann�e fiscale
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
     * Retourne <code>true</code> si le tiers de correspondance � peu �tre retrouv� selon diff�rentes r�gles et s'il
     * poss�de une adresse valide.
     *
     * Cette m�thode s'appuye sur la m�thode {@link REAttestationsFiscalesUtils}.getTiersCorrespondance(famille, annee)
     *
     * @param uneFamille la famille � analyser
     * @param annee L'ann�e fiscale
     * @return <code>true</code> si le tiers de correspondance � peu �tre retrouv� selon diff�rentes r�gles et s'il
     */
    public static boolean hasTiersCorrespondanceAvecAdresseValide(REFamillePourAttestationsFiscales uneFamille,
            String annee) {
        RETiersPourAttestationsFiscales tiersCorrespondance = REAttestationsFiscalesUtils
                .getTiersCorrespondance(uneFamille, annee);
        return tiersCorrespondance != null && !JadeStringUtil.isBlank(tiersCorrespondance.getAdresseCourrierFormatee());
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
        // Si des rentes de survivants sont vers�es � la famille une analyse doit �tre faite pour d�terminer le tiers
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
