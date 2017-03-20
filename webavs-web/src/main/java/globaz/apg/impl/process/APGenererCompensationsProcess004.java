package globaz.apg.impl.process;

import globaz.apg.exceptions.APTechnicalException;

/**
 * <p>
 * Sp�cifique CCCVS (InfoRom 463)
 * </p>
 * <p>
 * Le m�tier souhaiterait ne compenser automatiquement que les sections qui sont en "Poursuites", sinon payer tout le
 * reste (mettre quand m�me � disposition les sections non sold�es et l'utilisateur peut faire une compensation s'il le
 * d�sire).
 * </p>
 * <p>
 * Pr�cision du besoin : <br/>
 * Lors de la g�n�ration des compensations d�un lot, il est demand� que seules les sections qui sont en �Poursuites�
 * soient automatiquement compens�es. Pour les autres cas, le paiement doit �tre effectu� ; les autres sections non
 * sold�es doivent �tre remont�es automatiquement pour que l�utilisateur ait la possibilit� de les compenser
 * manuellement.
 * </p>
 * <p>
 * L�objectif de cette demande est d��viter de payer des prestations APG/AMAT � des assur�s qui sont en poursuite.
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