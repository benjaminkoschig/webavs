package ch.globaz.corvus.process.attestationsfiscales;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import ch.globaz.prestation.domaine.CodePrestation;
import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * <p>
 * Classe abstraite regroupant le fonctionnement commun � tous les analyseurs de lot pour les attestations fiscales.
 * <p>
 * <p>
 * Quelque soit le lot, si dans les rentes composant l'attestation fiscale on trouve l'une de ces conditions :
 * <ul>
 * <li>Une retenue sur paiement de type "Imp�t � la source" en cours dans l'ann�e demand�e</li>
 * <li>Une rente bloqu�e au 31 d�cembre de l'ann�e demand�e</li>
 * </ul>
 * On ne doit pas sortir d'attestation.
 * </p>
 * <p>
 * Les diff�rents lots sont :
 * <ul>
 * <li>
 * Lot 1 : Attestations fiscales des rentes vieillesse et d'invalidit� qui n'ont pas eu de d�cisions durant l'ann�e
 * demand�e</li>
 * <li>Lot 2 : Attestations fiscales des rentes de survivant ou d'orphelins qui n'ont pas eu de d�cisions durant l'ann�e
 * demand�e</li>
 * <li>
 * Lot 3 : Attestations fiscales des rentes de vieillesse et d'invalidit� qui ont eu une ou des d�cision durant l'ann�e
 * demand�e</li>
 * <li>
 * Lot 4 : Attestations fiscales des rentes de survivant ou d'orphelins qui ont eu une ou des d�cision durant l'ann�e
 * demand�e</li>
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
     * Construit un analyseur de lot pour la g�n�ration des attestations fiscales.<br/>
     * C'est ici que doit �tre d�fini l'ann�e fiscale voulue
     * 
     * @param annee
     *            l'ann�e fiscale voulue sur 4 digit (par exemple : <code>"2011"</code>)
     * @param seulementFamilleAvecDecisionDansAnneeFiscale
     *            d�fini si seules les familles, dont une ou plusieurs rentes ont une d�cision dans l'ann�e fiscale,
     *            doivent �tre comprises dans ce lot
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
     * Retourne l'ann�e fiscale dans laquelle cet analyseur doit travailler
     * 
     * @return une ann�e, sur 4 digit (par exemple : <code>"2011"</code>)
     */
    public final String getAnnee() {
        return annee;
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

    /**
     * <p>
     * D�fini si la famille fait parti de ce lot
     * </p>
     * <p>
     * Une famille regroupe un parent (le tiers dans la base de calcul) et les b�n�ficiaires des rentes li�es � cette
     * base de calcul. Si dans une famille les deux conjoints re�oivent une rente (r�partie sur deux demandes), il y a
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
