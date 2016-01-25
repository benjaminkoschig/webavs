package ch.globaz.al.businessimpl.calcul.modes;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Mode de calcul pour les agriculteurs de plaine du canton du Jura avant le 01.01.2009 (LAFam). Avant cette date les
 * enfants des allocataires correspondant � ces crit�res b�n�ficiait d'une prestation compl�mentaire de 9 CHF
 * 
 * @author jts
 */
public class CalculModeAgriculteurPlaineJura extends CalculModeAgriculture {

    /**
     * Montant compl�mentaire pour l'agriculture de plaine dans le Jura
     */
    private static final String MONTANT_COMPLEMENT_JURA = "9.0";

    /**
     * D�termine si le droit donne droit � la prestation compl�mentaire jurassienne de 9 CHF et l'ajoute � la liste des
     * droits calcul�s si c'est le cas. Le droit est d� pour les prestations enfant et formation
     * 
     * @param dossier
     *            Dossier pour lequel le calcul est ex�cut�
     * @param droit
     *            Droit pour lequel le montant compl�mentaire doit �tre d�termin�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private void computeComplementJura(DossierModel dossier, DroitComplexModel droit) throws JadeApplicationException {

        // le calcul des prestations "sp�ciales" ne doit pas se faire si on est
        // en train de calculer le montant des droits de l'autre conjoint dans
        // le cas d'une intercantonale et si le droit est de type m�nage
        if ((JadeThread.currentContext().getTemporaryAttribute("TARIF_AUTRE_PARENT") == null)
                && !ALServiceLocator.getDroitBusinessService().isTypeMenage(droit.getDroitModel())) {

            ALImplServiceLocator.getCalculMontantsService().addDroitCalculeSpecial(MONTANT_COMPLEMENT_JURA,
                    droitsCalcules, droit, ALCSDroit.TYPE_ENF_LJA, ALCSTarif.CATEGORIE_LJU, ALCSTarif.CATEGORIE_LJU,
                    ALCSTarif.CATEGORIE_LJU);
        }
    }

    /**
     * Parcours les droits contenus dans <code>droits</code> et ex�cute le calcul pour chacun d'eux puis ex�cute le
     * calcul du compl�ment jurassien
     * 
     * @param dossier
     *            Dossier pour lequel le calcul est ex�cut�
     * @param dateCalcul
     *            Date pour laquelle le calcul est ex�cut�
     * @param typeResident
     *            Type de r�sident de l'allocataire du dossier {@link ch.globaz.al.business.constantes.ALCSAllocataire}
     * @param droits
     *            Liste des droits
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see CalculModeAgriculteurPlaineJura#computeComplementJura(DossierModel, DroitComplexModel)
     */
    @Override
    protected void loopDroits(DossierComplexModelRoot dossier, DroitComplexSearchModel droits, String dateCalcul,
            String typeResident) throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculTypeTravailleurAgricoleJura#loopDroits : dossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeAbstract#loopDroits : " + dateCalcul + " is not a valid date");
        }

        if (typeResident == null) {
            throw new ALCalculException("CalculTypeTravailleurAgricoleJura#loopDroits : typeResident is null");
        }

        if (droits == null) {
            throw new ALCalculException("CalculTypeTravailleurAgricoleJura#loopDroits : droits is null");
        }

        // parcours des droits
        for (int i = 0; i < droits.getSize(); i++) {

            // calcul de la prestation "standard"
            if (computeDroit(dossier, (DroitComplexModel) droits.getSearchResults()[i], dateCalcul, typeResident)) {

                // si le droit est actif on y ajoute le compl�ment jurassien
                computeComplementJura(dossier.getDossierModel(), (DroitComplexModel) droits.getSearchResults()[i]);
            }
        }
    }
}
