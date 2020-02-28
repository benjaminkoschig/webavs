package ch.globaz.al.businessimpl.calcul.modes;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.tarif.TarifComplexSearchModel;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDeepCopy;

/**
 * Mode de calcul pour les caisses horlog�re d�s le 01.01.2012. Ce mode g�re le suppl�ment horloger de CHF 30.- auquel
 * ont droit les allocataires pour chacun de leurs enfants
 * 
 * 
 * @author jts
 */
public class CalculModeHorloger extends CalculModeAbstract {

    /** Date de d'entr�e en vigueur de ce mode de calcul */
    public final static String DATE_DEBUT_SUP_HORLO = "01.01.2012";

    /**
     * Calcul le montant du suppl�ment horloger
     * 
     * @param dossier
     *            Dossier pour lequel le calcul est ex�cut�
     * @param droit
     *            Droit pour lequel le montant compl�mentaire doit �tre d�termin�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected void computeComplementHorloger(DossierComplexModelRoot dossier, DroitComplexModel droit,
            String dateCalcul, String typeResident, List<CalculBusinessModel> droitsCalcules)
            throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeHorloger#computeComplementHorloger : dossier is null");
        }

        if (droit == null) {
            throw new ALCalculException("CalculModeHorloger#computeComplementHorloger : droit is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeHorloger#computeComplementHorloger : " + dateCalcul
                    + " is not a valid date");
        }

        if (JadeStringUtil.isEmpty(typeResident)) {
            throw new ALCalculException("CalculModeHorloger#computeComplementHorloger : typeResident is null or empty");
        }

        // calcul du suppl�ment horloger si le droit est de type ENF ou FORM et que le calcul du suppl�ment est actif
        // pour ce droit
        if ((ALCSDroit.TYPE_ENF.equals(droit.getDroitModel().getTypeDroit()) || ALCSDroit.TYPE_FORM.equals(droit
                .getDroitModel().getTypeDroit())) && droit.getDroitModel().getSupplementActif()) {

            CalculBusinessModel droitCalcule = droitsCalcules.get(droitsCalcules.size() - 1);

            if (droitCalcule.isActif()) {

                DossierModel cloneDossier = (DossierModel) ALDeepCopy.copy(dossier.getDossierModel());
                cloneDossier.setMontantForce(null);
                cloneDossier.setTarifForce(null);
                cloneDossier.setSpy(null);
                cloneDossier.setCreationSpy(null);

                DroitComplexModel cloneDroit = (DroitComplexModel) ALDeepCopy.copy(droit);
                cloneDroit.getDroitModel().setMontantForce(null);
                cloneDroit.getDroitModel().setForce(false);
                cloneDroit.getDroitModel().setTarifForce(null);
                cloneDroit.setSpy(null);

                TarifComplexSearchModel tarifs = getMontantSupplementHorloger(droit, dateCalcul, typeResident, droit
                        .getDroitModel().getTypeDroit());

                boolean hide = !(droitCalcule.getDroit().getDroitModel().getForce() && JadeNumericUtil
                        .isEmptyOrZero(droitCalcule.getDroit().getDroitModel().getMontantForce()));

                if (tarifs.getSize() > 0) {
                    ALImplServiceLocator.getCalculMontantsService().addDroitCalculeActif(cloneDossier, cloneDroit,
                            tarifs, ALCSTarif.CATEGORIE_SUP_HORLO, droitsCalcules, droitCalcule.getRang(), dateCalcul,
                            hide);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.calcul.modes.CalculModeAbstract#computeDroit(ch.globaz.al.business.models.dossier.
     * DossierComplexModelRoot, ch.globaz.al.business.models.droit.DroitComplexModel, java.lang.String,
     * java.lang.String)
     */
    @Override
    protected boolean computeDroit(DossierComplexModelRoot dossier, DroitComplexModel droit, String dateCalcul,
            String typeResident) throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeHorloger#computeDroit : dossier is null");
        }

        if (droit == null) {
            throw new ALCalculException("CalculModeHorloger#computeDroit : droit is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeHorloger#computeDroit : " + dateCalcul + " is not a valid date");
        }

        if (JadeStringUtil.isEmpty(typeResident)) {
            throw new ALCalculException("CalculModeHorloger#computeDroit : typeResident is null or empty");
        }

        boolean res = super.computeDroit(dossier, droit, dateCalcul, typeResident);
        computeComplementHorloger(dossier, droit, dateCalcul, typeResident, droitsCalcules);
        return res;
    }

    /**
     * Recherche le montant du tarif de suppl�ment horloger
     * 
     * @param dateCalcul
     *            Date pour laquelle le calcul est ex�cut�
     * @param droit
     *            Droit pour lequel le tarif doit �tre recherch�
     * @param typeResident
     *            Type de r�sident de l'allocataire du dossier {@link ch.globaz.al.business.constantes.ALCSAllocataire}
     * @param typePrestation
     *            Type de prestation recherch� {@link ch.globaz.al.business.constantes.ALCSPrestation}
     * 
     * @return R�sultat de la recherche
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected TarifComplexSearchModel getMontantSupplementHorloger(DroitComplexModel droit, String dateCalcul,
            String typeResident, String typePrestation) throws JadeApplicationException, JadePersistenceException {

        if (droit == null) {
            throw new ALCalculException("CalculModeHorloger#getMontantSupplementHorloger : droit is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeHorloger#getMontantSupplementHorloger : " + dateCalcul
                    + " is not a valid date");
        }

        if (JadeStringUtil.isEmpty(typeResident)) {
            throw new ALCalculException(
                    "CalculModeHorloger#getMontantSupplementHorloger : typeResident is null or empty");
        }

        if (JadeStringUtil.isEmpty(typePrestation)) {
            throw new ALCalculException(
                    "CalculModeHorloger#getMontantSupplementHorloger : typePrestation is null or empty");
        }

        TarifComplexSearchModel tarifs = new TarifComplexSearchModel();

        // l�gislation
        HashSet<String> legislations = new HashSet<String>();
        legislations.add(ALCSTarif.LEGISLATION_CAISSE);
        tarifs.setInLegislations(legislations);

        // cat�gories de tarif
        HashSet<String> categories = new HashSet<String>();
        categories.add(ALCSTarif.CATEGORIE_SUP_HORLO);
        tarifs.setInCategoriesTarif(categories);

        // type r�sident
        tarifs.setForCategorieResident(typeResident);

        // type prestation
        tarifs.setForTypePrestation(typePrestation);

        // validit�
        tarifs.setForValidite(dateCalcul);

        // AGE
        int age = ALImplServiceLocator.getCalculService().getAgeForCalcul(
                droit.getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne().getDateNaissance(),
                dateCalcul);

        tarifs.setForCritereAge(String.valueOf(age));

        // capable exercer (tarif identique dans les deux cas)
        tarifs.setForCapableExercer(true);

        return ALImplServiceLocator.getTarifComplexModelService().search(tarifs);
    }
}
