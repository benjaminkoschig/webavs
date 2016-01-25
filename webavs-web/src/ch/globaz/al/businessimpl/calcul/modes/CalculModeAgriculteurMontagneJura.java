package ch.globaz.al.businessimpl.calcul.modes;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.tarif.TarifComplexSearchModel;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Mode de calcul pour les agriculteurs de montagne du canton du Jura avant le 01.01.2009 (LAFam). Avant cette date les
 * allocataires bénéficiait d'une prestation de ménage de 15 CHF. Cette classe redéfini la méthode
 * <code>setMontantsForDroit</code> de façon à forcer le montant des prestations de ménage à un montant de 15 CHF
 * 
 * @author jts
 * 
 */
public class CalculModeAgriculteurMontagneJura extends CalculModeAgriculture {

    /**
     * Montant du ménage jurassien pour l'agriculture
     */
    protected static final String MONTANT_MENAGE_JURA = "15.0";

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.calcul.modes.CalculModeAbstract#setMontantsForDroit
     * (ch.globaz.al.business.models.dossier.DossierComplexModelRoot,
     * ch.globaz.al.business.models.droit.DroitComplexModel, ch.globaz.al.business.models.tarif.TarifComplexSearchModel)
     */
    @Override
    protected boolean setMontantsForDroit(DossierComplexModelRoot dossier, DroitComplexModel droit,
            TarifComplexSearchModel tarifs) throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeAbstract#setMontantsForDroit : dossier is null");
        }

        if (droit == null) {
            throw new ALCalculException("CalculModeAbstract#setMontantsForDroit : droit is null");
        }

        if (tarifs == null) {
            throw new ALCalculException("CalculModeAbstract#setMontantsForDroit : tarifs is null");
        }

        if (tarifs.getSize() > 0) {
            if (ALCSDroit.TYPE_MEN.equals(droit.getDroitModel().getTypeDroit())) {
                ALImplServiceLocator.getCalculMontantsService().addDroitCalculeSpecial(MONTANT_MENAGE_JURA,
                        droitsCalcules, droit, ALCSDroit.TYPE_MEN_LJA, ALCSTarif.CATEGORIE_LJU,
                        ALCSTarif.CATEGORIE_LJU, ALCSTarif.CATEGORIE_LJU);
            } else {
                ALImplServiceLocator.getCalculMontantsService().addDroitCalculeActif(
                        context.getDossier().getDossierModel(), droit, tarifs, usedTarif, droitsCalcules,
                        String.valueOf(rang + 1), tarifs.getForValidite());
            }

            return true;

        } else {
            droitsCalcules = ALImplServiceLocator.getCalculMontantsService().addDroitCalculeInactif(droit,
                    droitsCalcules, tarifs.getForValidite());
            return false;
        }
    }
}
