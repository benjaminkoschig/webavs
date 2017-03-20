package globaz.apg.impl.process;

import globaz.apg.exceptions.APTechnicalException;
import globaz.osiris.exceptions.CATechnicalException;

/**
 * <p>
 * Sp�cifique CCCVS (BZ 8605), sp�cialisation du processus 004
 * </p>
 * <p>
 * Le m�tier souhaiterait ne compenser automatiquement que les sections qui sont en "Poursuites" ou en faillite, sinon
 * payer tout le reste (mettre quand m�me � disposition les sections non sold�es et l'utilisateur peut faire une
 * compensation s'il le d�sire).
 * </p>
 * <p>
 * Pr�cision du besoin : <br/>
 * Lors de la g�n�ration des compensations d�un lot, il est demand� que les sections dont le blocage est de type
 * faillite, ou qui sont en �Poursuites�, soient automatiquement compens�es. Pour les autres cas, le paiement doit �tre
 * effectu� ; les autres sections non sold�es doivent �tre remont�es automatiquement pour que l�utilisateur ait la
 * possibilit� de les compenser manuellement.
 * </p>
 * <p>
 * L�objectif de cette demande est d��viter de payer des prestations APG/AMAT � des assur�s qui sont en poursuite, ou
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