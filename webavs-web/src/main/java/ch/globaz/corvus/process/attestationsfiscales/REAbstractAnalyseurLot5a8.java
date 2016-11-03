package ch.globaz.corvus.process.attestationsfiscales;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * <p>
 * Classe abstraite regroupant le fonctionnement commun aux analyseurs (5 a 8) de lot pour les attestations fiscales.
 * <p>
 * <p>
 * Par défaut on prend que les cas ayant eu une décision dans l'année, ayant du retro et, en fonction du champ
 * (retroSurUneAnneeUniquement) du retro que sur l'année fiscales ou sur plusieurs années
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
         * On passe toujours true pour n'avoir que les décisions dans l'année
         */
        super(annee, true, typesRentesVoulues);
        this.retroSurUneAnneeUniquement = retroSurUneAnneeUniquement;
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
    @Override
    public final boolean isFamilleDansLot(REFamillePourAttestationsFiscales famille) {
        if (REAttestationsFiscalesUtils.hasRenteBloquee(famille, getAnnee())
                || REAttestationsFiscalesUtils.hasImpotSourceVerseeDansAnnee(famille, getAnnee())
                // Il doit y avoir du rétro, si pas de rétro on retourne false
                || !REAttestationsFiscalesUtils.hasRetro(famille, getAnneeAsInteger())
                || REAttestationsFiscalesUtils.hasRenteFinissantDansAnnee(famille, getAnnee())
                || REAttestationsFiscalesUtils.hasPersonneDecedeeDurantAnneeFiscale(famille, getAnnee())
                || REAttestationsFiscalesUtils.hasRenteQuiSeChevauchent(famille, getAnneeAsInteger())) {
            return false;
        }
        // 1 : on exclus la famille si elle possède une décision en décembre de l'année fiscale
        boolean result = !REAttestationsFiscalesUtils.hasDecisionEnDecembre(famille, getAnneeAsInteger());

        // 2 : on contrôle si la famille possède le rétro désiré (une ou plusieurs années)
        if (result) {
            result = REAttestationsFiscalesUtils.analyserRetro(famille, isRetroSurUneAnneeUniquement(),
                    getAnneeAsInteger());
        }

        // 3 : on contrôle si on possèdent le bon type de rente (SUR ou AVS/AI)
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
