package ch.globaz.corvus.process.attestationsfiscales;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
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
public abstract class REAbstractAnalyseurLot1a4 extends REAbstractAnalyseurLot {

    private final static Logger logger = LoggerFactory.getLogger(REAbstractAnalyseurLot5a8.class);

    public REAbstractAnalyseurLot1a4(String annee, boolean seulementFamilleAvecDecisionDansAnneeFiscale,
            DomaineCodePrestation... typesRentesVoulues) {
        super(annee, seulementFamilleAvecDecisionDansAnneeFiscale, typesRentesVoulues);
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
                // Il ne doit pas y avoir de rétro, si rétro on retourne false -> ok
                || REAttestationsFiscalesUtils.hasRetro(famille, getAnneeAsInteger())
                || REAttestationsFiscalesUtils.hasRenteFinissantDansAnnee(famille, getAnnee())
                || REAttestationsFiscalesUtils.hasPersonneDecedeeDurantAnneeFiscale(famille, getAnnee())
                || REAttestationsFiscalesUtils.hasRenteQuiSeChevauchent(famille, getAnneeAsInteger())) {
            return false;
        }
        boolean result = controllerDateDecisionEtTypeDeRente(famille);
        if (result) {
            logger.info(formatLogFamilleAcceptee(famille, getNumeroAnalyseur()));
        }
        return result;
    }

}
