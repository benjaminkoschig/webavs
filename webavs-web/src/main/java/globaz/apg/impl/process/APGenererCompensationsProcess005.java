package globaz.apg.impl.process;

import globaz.apg.exceptions.APTechnicalException;
import globaz.osiris.exceptions.CATechnicalException;

/**
 * <p>
 * Spécifique CCCVS (BZ 8605), spécialisation du processus 004
 * </p>
 * <p>
 * Le métier souhaiterait ne compenser automatiquement que les sections qui sont en "Poursuites" ou en faillite, sinon
 * payer tout le reste (mettre quand même à disposition les sections non soldées et l'utilisateur peut faire une
 * compensation s'il le désire).
 * </p>
 * <p>
 * Précision du besoin : <br/>
 * Lors de la génération des compensations d’un lot, il est demandé que les sections dont le blocage est de type
 * faillite, ou qui sont en ‘Poursuites’, soient automatiquement compensées. Pour les autres cas, le paiement doit être
 * effectué ; les autres sections non soldées doivent être remontées automatiquement pour que l’utilisateur ait la
 * possibilité de les compenser manuellement.
 * </p>
 * <p>
 * L’objectif de cette demande est d’éviter de payer des prestations APG/AMAT à des assurés qui sont en poursuite, ou
 * dont l'entreprise a fait faillite.
 * </p>
 * 
 * @author PBA
 */
public class APGenererCompensationsProcess005 extends APGenererCompensationsProcess004 {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected boolean isAffilieEnFaillite(SectionEtCompteAnnexe sectionEtCompteAnnexe, String annee)
            throws APTechnicalException {
        try {
            return (sectionEtCompteAnnexe.getCompteAnnexe() != null)
                    && sectionEtCompteAnnexe.getCompteAnnexe().isEnFaillte(annee);
        } catch (CATechnicalException ex) {
            throw new APTechnicalException("Error while using module Osiris", ex);
        }
    }

    @Override
    public boolean isModulePorterEnCompte() {
        return false;
    }
}