package globaz.apg.impl.process;

import globaz.apg.exceptions.APTechnicalException;

/**
 * <p>
 * Spécifique CCCVS (InfoRom 463)
 * </p>
 * <p>
 * Le métier souhaiterait ne compenser automatiquement que les sections qui sont en "Poursuites", sinon payer tout le
 * reste (mettre quand même à disposition les sections non soldées et l'utilisateur peut faire une compensation s'il le
 * désire).
 * </p>
 * <p>
 * Précision du besoin : <br/>
 * Lors de la génération des compensations d’un lot, il est demandé que seules les sections qui sont en ‘Poursuites’
 * soient automatiquement compensées. Pour les autres cas, le paiement doit être effectué ; les autres sections non
 * soldées doivent être remontées automatiquement pour que l’utilisateur ait la possibilité de les compenser
 * manuellement.
 * </p>
 * <p>
 * L’objectif de cette demande est d’éviter de payer des prestations APG/AMAT à des assurés qui sont en poursuite.
 * </p>
 * 
 * @author PBA
 */
public class APGenererCompensationsProcess004 extends APGenererCompensationsProcessAvecSectionCompensable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected boolean isAffilieEnFaillite(SectionEtCompteAnnexe sectionEtCompteAnnexe, String annee)
            throws APTechnicalException {
        return false;
    }

    @Override
    protected boolean isSectionACompenserAutomatiquement(SectionEtCompteAnnexe sectionEtCompteAnnexe) {
        return sectionEtCompteAnnexe.getSection().isSectionAuContentieux()
                && sectionEtCompteAnnexe.getSection().isSectionAuxPoursuitesNotRadiee(true);
    }

    @Override
    protected boolean isSectionAVerifier(SectionEtCompteAnnexe sectionEtCompteAnnexe) {
        return sectionEtCompteAnnexe.getSection().isSectionAuContentieux()
                && !sectionEtCompteAnnexe.getSection().isSectionAuxPoursuitesNotRadiee(true);
    }

    @Override
    public boolean isModulePorterEnCompte() {
        return false;
    }
}