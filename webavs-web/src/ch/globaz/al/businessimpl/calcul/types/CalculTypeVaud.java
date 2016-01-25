package ch.globaz.al.businessimpl.calcul.types;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.calcul.ALCalculModeException;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.businessimpl.calcul.context.ContextCalcul;
import ch.globaz.al.businessimpl.calcul.modes.CalculMode;
import ch.globaz.al.businessimpl.calcul.modes.CalculModeAbstract;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Type de calcul sp�cifique au canton de Vaud.
 * 
 * Il fonctionne de la m�me mani�re que le type standard mais traite, en plus, la famille nombreuse.
 * 
 * Ce type est valide pour les calculs r�pondant aux conditions suivantes :
 * <ul>
 * <li>date de calcul ant�rieur au 01.01.2009 (non LAFam)</li>
 * <li>tarif vaudois</li>
 * </ul>
 * 
 * @author jts
 * @see ch.globaz.al.businessimpl.calcul.types.CalculTypeStandard
 */
public class CalculTypeVaud extends CalculTypeStandard {

    /**
     * Montant de famille nombreuse accord� par enfant
     */
    protected static final double MONTANT_FNB = 170.0;

    /**
     * Nombre d'enfant d'une famille ne donnant pas droit � une prestation de famille nombreuse
     */
    protected static final int NB_DROITS_SANS_FNB = 2;

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.calcul.types.CalculTypeStandard#compute(ch.
     * globaz.al.business.models.dossier.DossierComplexModelAbstract, ch.globaz.al.businessimpl.calcul.modes.CalculMode,
     * java.lang.String)
     */
    @Override
    public ArrayList<CalculBusinessModel> compute(ContextCalcul context, CalculMode calcMode)
            throws JadeApplicationException, JadePersistenceException {

        if (context == null) {
            throw new ALCalculModeException("CalculTypeVaud#compute : context is null");
        }

        if (calcMode == null) {
            throw new ALCalculModeException("CalculTypeVaud#compute : calcMode is null");
        }

        // calcul standard...
        ArrayList<CalculBusinessModel> res = super.compute(context, calcMode);

        // ... + FNB
        computeFNB(context.getDossier().getDossierModel(), res, ((CalculModeAbstract) calcMode).getNombre());

        return res;
    }

    /**
     * Calcul de la famille nombreuse
     * 
     * @param dossier
     *            Dossier pour lequel les droits sont calcul�s
     * @param droitsCalcules
     *            Liste des droits calcul�
     * @param nbPrestations
     *            Nombre de prestations calcul�es
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private void computeFNB(DossierModel dossier, ArrayList<CalculBusinessModel> droitsCalcules, int nbPrestations)
            throws JadeApplicationException {

        // le calcul des prestations "sp�ciales" ne doit pas se faire si on est
        // en train de calculer le montant des droits de l'autre conjoint dans
        // le cas d'une intercantonale
        if (JadeThread.currentContext().getTemporaryAttribute("TARIF_AUTRE_PARENT") == null) {
            if (dossier == null) {
                throw new ALCalculModeException("CalculTypeVaud#computeFNB : dossier is null");
            }

            if (droitsCalcules == null) {
                throw new ALCalculModeException("CalculTypeVaud#computeFNB : droitsCalcules is null");
            }

            if (nbPrestations < 0) {
                throw new ALCalculModeException("CalculTypeVaud#computeFNB : nbPrestations must be >= 0");
            }

            // si une FNB est due
            if (nbPrestations > NB_DROITS_SANS_FNB) {
                double fnb = (nbPrestations - NB_DROITS_SANS_FNB) * MONTANT_FNB;

                ALImplServiceLocator.getCalculMontantsService().addDroitCalculeSpecial(String.valueOf(fnb),
                        droitsCalcules, null, ALCSDroit.TYPE_FNB, ALCSTarif.CATEGORIE_VD, ALCSTarif.CATEGORIE_VD,
                        ALCSTarif.CATEGORIE_VD);
            }
        }
    }
}