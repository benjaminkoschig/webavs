package ch.globaz.corvus.process.attestationsfiscales;

import globaz.jade.client.util.JadeNumericUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import ch.globaz.prestation.domaine.CodePrestation;
import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * <p>
 * Classe abstraite regroupant le fonctionnement commun � tous les analyseurs de lot pour les attestations fiscales.
 * </p>
 */
public abstract class REAbstractAnalyseurLot {

    private final String annee;
    private final int anneeAsInteger;
    private final boolean seulementFamilleAvecDecisionDansAnneeFiscale;
    private final Set<DomaineCodePrestation> typesRentesVoulues;

    /**
     * Construit un analyseur de lot pour la g�n�ration des attestations fiscales.<br/>
     * 
     * 
     * @param annee
     *            l'ann�e fiscale voulue sur 4 digit (par exemple : <code>"2011"</code>)
     * @param seulementFamilleAvecDecisionDansAnneeFiscale
     *            d�fini si seules les familles, dont une ou plusieurs rentes ont une d�cision dans l'ann�e fiscale,
     *            doivent �tre comprises dans ce lot
     * @throws IllegalArgumentException si l'annee n'est pas valide
     */
    public REAbstractAnalyseurLot(String annee, boolean seulementFamilleAvecDecisionDansAnneeFiscale,
            DomaineCodePrestation... typesRentesVoulues) {
        if (!JadeNumericUtil.isIntegerPositif(annee)) {
            throw new IllegalArgumentException("L'ann�e [" + annee + "] n'est pas valide");
        }
        this.annee = annee;
        anneeAsInteger = Integer.valueOf(annee);
        this.seulementFamilleAvecDecisionDansAnneeFiscale = seulementFamilleAvecDecisionDansAnneeFiscale;
        this.typesRentesVoulues = Collections.unmodifiableSet(new HashSet<DomaineCodePrestation>(Arrays
                .asList(typesRentesVoulues)));
    }

    /**
     * Cette m�thode doit �tre impl�ment�e sp�cifiquement par les modules d'analyses afin de d�terminer si une famille
     * fait partie du lot en question
     * 
     * @param famille La famille � analyser
     * @return <code>true</code> si la famille remplis les conditions pour faire partir du lot
     */
    public abstract boolean isFamilleDansLot(REFamillePourAttestationsFiscales famille);

    /**
     * Doit retourner le num�ro de l'analyseur
     * 
     * @return le num�ro de l'analyseur
     */
    public abstract int getNumeroAnalyseur();

    /**
     * Analyse si au moins une des rentes de la famille est du type attendu par le module ET la date des d�cisions en
     * fonction
     * du champ bool�en (seulementFamilleAvecDecisionDansAnneeFiscale)
     * 
     * @param famille La famille � analyser
     * @return true si au moins une des rentes de la famille est du type attendu et que les dates de d�cision sont
     *         correcte pour le module d'analyse
     */
    protected boolean controllerDateDecisionEtTypeDeRente(REFamillePourAttestationsFiscales famille) {
        if ((isSeulementFamilleAvecDecisionDansAnneeFiscale() && REAttestationsFiscalesUtils
                .isAvecDecisionPendantAnneeFiscale(famille, getAnnee()))
                || (!isSeulementFamilleAvecDecisionDansAnneeFiscale() && REAttestationsFiscalesUtils
                        .isSansDecisionPendantEtApresAnneeFiscale(famille, getAnnee()))) {
            for (RERentePourAttestationsFiscales uneRente : famille.getRentesDeLaFamille()) {
                CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(uneRente
                        .getCodePrestation()));

                if (codePrestation.isAPI()) {
                    continue;
                }

                if (getTypesRentesVoulues().contains(codePrestation.getDomaineCodePrestation())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Retourne l'ann�e fiscale dans laquelle cet analyseur doit travailler
     * 
     * @return une ann�e, sur 4 digit (par exemple : <code>"2011"</code>)
     */
    public final String getAnnee() {
        return annee;
    }

    /**
     * Retourne l'ann�e fiscales sous forme d'Integer
     * 
     * @return l'ann�e fiscales sous forme d'Integer
     */
    public int getAnneeAsInteger() {
        return anneeAsInteger;
    }

    /**
     * Retourne les types de rentes (AVS, car les API ne nous int�resse pas � ce niveau) que ce lot est charg� de
     * traiter
     * 
     * @return un {@link Set} non modifiable contenant les types de rentes AVS trait�s par ce lot
     */
    public final Set<DomaineCodePrestation> getTypesRentesVoulues() {
        return typesRentesVoulues;
    }

    public final boolean isSeulementFamilleAvecDecisionDansAnneeFiscale() {
        return seulementFamilleAvecDecisionDansAnneeFiscale;
    }

    /**
     * Log en info la rente qui � �t� accept�e de l'analyse
     * 
     * @param rente La rente exclue
     * @param motif Le motif de l'exclusion
     */
    protected String formatLogFamilleAcceptee(REFamillePourAttestationsFiscales famille, int numeroAnalyseur) {
        StringBuilder message = new StringBuilder();
        message.append("La famille suivante � �t� accept�e par le module [" + numeroAnalyseur
                + "]. D�tails de la rente : ");
        message.append(formatDetailsFamille(famille));
        return message.toString();
    }

    /**
     * Format un message avec les informations importante de la famille
     * 
     * @param famille La famille en question
     * @return une string format�e avec les informations de la famille et des rentes
     */
    private String formatDetailsFamille(REFamillePourAttestationsFiscales famille) {
        StringBuilder message = new StringBuilder();

        RETiersPourAttestationsFiscales tiersRequerant = famille.getTiersRequerant();
        if (tiersRequerant != null) {
            message.append(tiersRequerant.getNumeroAvs()).append(" / ");
            message.append(tiersRequerant.getNom()).append(" ").append(tiersRequerant.getPrenom()).append(" / ");
            message.append(tiersRequerant.getDateNaissance()).append(" - ");

            message.append("idTiersRequerant=");
            message.append(famille.getTiersRequerant().getIdTiers());
            message.append(",");
        }
        // B�n�ficiaires de la famille
        message.append(" idTiersB�n�ficiaires=[");
        Iterator<RETiersPourAttestationsFiscales> iteratorTiers = famille.getTiersBeneficiaires().iterator();
        while (iteratorTiers.hasNext()) {
            RETiersPourAttestationsFiscales tiers = iteratorTiers.next();
            message.append(tiers.getIdTiers());

            if (iteratorTiers.hasNext()) {
                message.append(", ");
            }
        }
        message.append("]");

        message.append(", rentesDeLaFamille=[");
        Iterator<RERentePourAttestationsFiscales> iterator = famille.getRentesDeLaFamille().iterator();
        while (iterator.hasNext()) {
            message.append("[" + formatDetailsRente(iterator.next()) + "]");
            if (iterator.hasNext()) {
                message.append(", ");
            }
        }
        message.append("]");
        return message.toString();
    }

    private String formatDetailsRente(RERentePourAttestationsFiscales rente) {
        StringBuilder message = new StringBuilder();
        message.append("codePresation=");
        message.append(rente.getCodePrestation());
        message.append(", idTiersB�n�ficiaire=");
        message.append(rente.getIdTiersBeneficiaire());
        message.append(", dateDebutDroit=");
        message.append(rente.getDateDebutDroit());
        message.append(", dateDecision=");
        message.append(rente.getDateDecision());
        return message.toString();
    }
}
