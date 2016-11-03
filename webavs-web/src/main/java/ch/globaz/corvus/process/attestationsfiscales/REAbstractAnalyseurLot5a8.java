package ch.globaz.corvus.process.attestationsfiscales;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * <p>
 * Classe abstraite regroupant le fonctionnement commun aux analyseurs (5 a 8) de lot pour les attestations fiscales.
 * <p>
 * <p>
 * Par d�faut on prend que les cas ayant eu une d�cision dans l'ann�e, ayant du retro et, en fonction du champ
 * (retroSurUneAnneeUniquement) du retro que sur l'ann�e fiscales ou sur plusieurs ann�es
 * <ul>
 * 
 * </ul>
 * On ne doit pas sortir d'attestation.
 * </p>
 */

public abstract class REAbstractAnalyseurLot5a8 extends REAbstractAnalyseurLot {

    private final static Logger logger = LoggerFactory.getLogger(REAbstractAnalyseurLot5a8.class);
    private boolean retroSurUneAnneeUniquement;

    public REAbstractAnalyseurLot5a8(String annee, boolean retroSurUneAnneeUniquement,
            DomaineCodePrestation... typesRentesVoulues) {
        /*
         * On passe toujours true pour n'avoir que les d�cisions dans l'ann�e
         */
        super(annee, true, typesRentesVoulues);
        this.retroSurUneAnneeUniquement = retroSurUneAnneeUniquement;
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
    @Override
    public final boolean isFamilleDansLot(REFamillePourAttestationsFiscales famille) {
        if (REAttestationsFiscalesUtils.hasRenteBloquee(famille, getAnnee())
                || REAttestationsFiscalesUtils.hasImpotSourceVerseeDansAnnee(famille, getAnnee())
                // Il doit y avoir du r�tro, si pas de r�tro on retourne false
                || !REAttestationsFiscalesUtils.hasRetro(famille, getAnneeAsInteger())
                || REAttestationsFiscalesUtils.hasRenteFinissantDansAnnee(famille, getAnnee())
                || REAttestationsFiscalesUtils.hasPersonneDecedeeDurantAnneeFiscale(famille, getAnnee())
                || REAttestationsFiscalesUtils.hasRenteQuiSeChevauchent(famille, getAnneeAsInteger())) {
            return false;
        }
        // 1 : on exclus la famille si elle poss�de une d�cision en d�cembre de l'ann�e fiscale
        boolean result = !REAttestationsFiscalesUtils.hasDecisionEnDecembre(famille, getAnneeAsInteger());

        // 2 : on contr�le si la famille poss�de le r�tro d�sir� (une ou plusieurs ann�es)
        if (result) {
            result = REAttestationsFiscalesUtils.analyserRetro(famille, isRetroSurUneAnneeUniquement(),
                    getAnneeAsInteger());
        }

        // 3 : on contr�le si on poss�dent le bon type de rente (SUR ou AVS/AI)
        if (result) {
            result = controllerDateDecisionEtTypeDeRente(famille);
        }

        if (result) {
            logger.info(formatLogFamilleAcceptee(famille, getNumeroAnalyseur()));
        }
        return result;

    }

    public boolean isRetroSurUneAnneeUniquement() {
        return retroSurUneAnneeUniquement;
    }
}
