package ch.globaz.al.businessimpl.rafam.handlers;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce;
import ch.globaz.al.businessimpl.rafam.ContextAnnonceRafam;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Gère les annonces de type Naissance (code 01).
 * 
 * @author jts
 * 
 */
public class AnnonceNaissanceHandler extends AnnonceHandlerAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant toutes les données nécessaire à la gestion des annonces RAFam
     */
    public AnnonceNaissanceHandler(ContextAnnonceRafam context) {
        this.context = context;
    }

    @Override
    protected void doAnnulation() throws JadeApplicationException, JadePersistenceException {
        if (!ALImplServiceLocator.getAnnonceRafamBusinessService().isNaissanceHorlogere(context)) {
            super.doAnnulation();
        }
    }

    @Override
    protected void doCreation() throws JadeApplicationException, JadePersistenceException {

        if (!ALImplServiceLocator.getAnnonceRafamBusinessService().isNaissanceHorlogere(context)) {
            if (hasNaissance()) {
                super.doCreation();
            }
        }
    }

    @Override
    protected void doModificationStandard() throws JadeApplicationException, JadePersistenceException {

        if (!ALImplServiceLocator.getAnnonceRafamBusinessService().isNaissanceHorlogere(context)) {
            if (hasNaissance()) {
                if (getLastAnnonce().isNew()) {
                    doCreation();
                }
            } else {
                if (!getLastAnnonce().isNew()
                        && !RafamTypeAnnonce._68C_ANNULATION.equals(RafamTypeAnnonce
                                .getRafamTypeAnnonce(getLastAnnonce().getTypeAnnonce())) && naissanceTypeHasChanged()) {
                    doAnnulation();
                }
            }
        }
    }

    @Override
    protected RafamFamilyAllowanceType getType() throws JadeApplicationException {
        return RafamFamilyAllowanceType.NAISSANCE;
    }

    /**
     * 
     * @return le type d'allocation de naissance géré par cette classe
     */
    protected String getTypeAllocationNaissance() {
        return ALCSDroit.NAISSANCE_TYPE_NAIS;
    }

    /**
     * Vérifie si l'enfant à une allocation de naissance active et non versée
     * 
     * @return <code>true</code> si une allocation de naissance est active
     */
    private boolean hasNaissance() {
        if (getTypeAllocationNaissance().equals(
                context.getDroit().getEnfantComplexModel().getEnfantModel().getTypeAllocationNaissance())
                && !context.getDroit().getEnfantComplexModel().getEnfantModel().getAllocationNaissanceVersee()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean isCurrentAllowanceTypeActive() {
        return hasNaissance();
    }

    /**
     * Vérifie si le type de prestation de naissance (NAIS ou ACCE) contenu dans le droit à changé par rapport au type
     * en cours de traitement
     * 
     * @return <code>true</code> si le type à changé
     */
    private boolean naissanceTypeHasChanged() {
        return !getTypeAllocationNaissance().equals(
                context.getDroit().getEnfantComplexModel().getEnfantModel().getTypeAllocationNaissance());
    }
}
