package ch.globaz.corvus.process.attestationsfiscales;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import ch.globaz.prestation.domaine.CodePrestation;
import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * <p>
 * Classe abstraite regroupant le fonctionnement commun à tous les analyseurs de lot pour les attestations fiscales.
 * <p>
 * <p>
 * Quelque soit le lot, si dans les rentes composant l'attestation fiscale on trouve l'une de ces conditions :
 * <ul>
 * <li>Une retenue sur paiement de type "Impôt à la source" en cours dans l'année demandée</li>
 * <li>Une rente bloquée au 31 décembre de l'année demandée</li>
 * </ul>
 * On ne doit pas sortir d'attestation.
 * </p>
 * <p>
 * Les différents lots sont :
 * <ul>
 * <li>
 * Lot 1 : Attestations fiscales des rentes vieillesse et d'invalidité qui n'ont pas eu de décisions durant l'année
 * demandée</li>
 * <li>Lot 2 : Attestations fiscales des rentes de survivant ou d'orphelins qui n'ont pas eu de décisions durant l'année
 * demandée</li>
 * <li>
 * Lot 3 : Attestations fiscales des rentes de vieillesse et d'invalidité qui ont eu une ou des décision durant l'année
 * demandée</li>
 * <li>
 * Lot 4 : Attestations fiscales des rentes de survivant ou d'orphelins qui ont eu une ou des décision durant l'année
 * demandée</li>
 * </li>
 * </ul>
 * </p>
 * 
 * @author PBA
 * @see REAnalyseurLot1
 * @see REAnalyseurLot2
 * @see REAnalyseurLot3
 * @see REAnalyseurLot4
 */
public abstract class REAbstractAnalyseurLot {

    private final String annee;
    private final boolean seulementFamilleAvecDecisionDansAnneeFiscale;
    private final Set<DomaineCodePrestation> typesRentesVoulues;

    /**
     * Construit un analyseur de lot pour la génération des attestations fiscales.<br/>
     * C'est ici que doit être défini l'année fiscale voulue
     * 
     * @param annee
     *            l'année fiscale voulue sur 4 digit (par exemple : <code>"2011"</code>)
     * @param seulementFamilleAvecDecisionDansAnneeFiscale
     *            défini si seules les familles, dont une ou plusieurs rentes ont une décision dans l'année fiscale,
     *            doivent être comprises dans ce lot
     */
    public REAbstractAnalyseurLot(String annee, boolean seulementFamilleAvecDecisionDansAnneeFiscale,
            DomaineCodePrestation... typesRentesVoulues) {
        super();

        this.annee = annee;
        this.seulementFamilleAvecDecisionDansAnneeFiscale = seulementFamilleAvecDecisionDansAnneeFiscale;
        this.typesRentesVoulues = Collections.unmodifiableSet(new HashSet<DomaineCodePrestation>(Arrays
                .asList(typesRentesVoulues)));
    }

    /**
     * Retourne l'année fiscale dans laquelle cet analyseur doit travailler
     * 
     * @return une année, sur 4 digit (par exemple : <code>"2011"</code>)
     */
    public final String getAnnee() {
        return annee;
    }

    /**
     * Retourne les types de rentes (AVS, car les API ne nous intéresse pas à ce niveau) que ce lot est chargé de
     * traiter
     * 
     * @return un {@link Set} non modifiable contenant les types de rentes AVS traités par ce lot
     */
    public final Set<DomaineCodePrestation> getTypesRentesVoulues() {
        return typesRentesVoulues;
    }

    /**
     * <p>
     * Défini si la famille fait parti de ce lot
     * </p>
     * <p>
     * Une famille regroupe un parent (le tiers dans la base de calcul) et les bénéficiaires des rentes liées à cette
     * base de calcul. Si dans une famille les deux conjoints reçoivent une rente (répartie sur deux demandes), il y a
     * aura deux {@link REFamillePourAttestationsFiscales}, une pour chaque conjoint.
     * </p>
     * 
     * @param famille
     * @return
     * @see #isDansLot(REFamillePourAttestationsFiscales)
     * @see REAbstractAnalyseurLot
     */
    public final boolean isFamilleDansLot(REFamillePourAttestationsFiscales famille) {
        if (REAttestationsFiscalesUtils.hasRenteBloquee(famille, annee)
                || REAttestationsFiscalesUtils.hasImpotSourceVerseeDansAnnee(famille, annee)
                || REAttestationsFiscalesUtils.hasRetroDansAnneeFiscale(famille, annee)
                || REAttestationsFiscalesUtils.hasRenteFinissantDansAnnee(famille, annee)
                || REAttestationsFiscalesUtils.hasPersonneDecedeeDurantAnneeFiscale(famille, annee)) {
            return false;
        }
        if ((isSeulementFamilleAvecDecisionDansAnneeFiscale() && REAttestationsFiscalesUtils
                .isAvecDecisionPendantAnneeFiscale(famille, annee))
                || (!isSeulementFamilleAvecDecisionDansAnneeFiscale() && REAttestationsFiscalesUtils
                        .isSansDecisionPendantEtApresAnneeFiscale(famille, annee))) {
            for (RERentePourAttestationsFiscales uneRente : famille.getRentesDeLaFamille()) {
                CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(uneRente
                        .getCodePrestation()));

                if (codePrestation.isAPI()) {
                    continue;
                }

                if (typesRentesVoulues.contains(codePrestation.getDomaineCodePrestation())) {
                    return true;
                }
            }
        }
        return false;
    }

    public final boolean isSeulementFamilleAvecDecisionDansAnneeFiscale() {
        return seulementFamilleAvecDecisionDansAnneeFiscale;
    }
}
