package ch.globaz.al.businessimpl.generation.prestations;

import ch.globaz.al.properties.ALProperties;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.droit.DroitModel;
import ch.globaz.al.business.models.prestation.DetailPrestationGenComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationGenComplexSearchModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextDossier;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextPrestation;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Classe fournissant les m�thodes communes � toutes les classes de g�n�ration de prestations
 * 
 * @author jts
 * 
 */
public abstract class GenPrestationAbstract implements GenPrestation {

    /**
     * Contexte contenant les informations n�cessaires � la g�n�ration
     */
    protected ContextAffilie context = null;

    /**
     * Constructeur
     * 
     * @param context
     *            Contexte contenant les informations n�cessaires � la g�n�ration
     */
    public GenPrestationAbstract(ContextAffilie context) {
        this.context = context;
    }

    /**
     * Ajoute un d�tail de prestation dans le contexte
     * 
     * @param context
     *            Contexte contenant les informations de la g�n�ration en cours
     * @param droitCalcule
     *            droit pour lequel ajouter un d�tail de prestation
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected void addDetailPrestation(ContextAffilie context, CalculBusinessModel droitCalcule)
            throws JadeApplicationException, JadePersistenceException {

        if (this.context == null) {
            throw new ALGenerationException("GenPrestationAbstract#addDetailPrestation : context is null");
        }

        DetailPrestationModel detail = initDetailPrestation(this.context, droitCalcule);

        context.getContextDossier().addDetailPrestation(detail, droitCalcule.getDroit());

        checkExportPrestation(this.context, droitCalcule);
    }

    /**
     * 
     * @param context
     *            Context contenant les informations n�cessaires � la g�n�ration
     * @param oldPrest
     *            Prestation � extourner
     * @return Context mis � jour
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected ContextAffilie addExtourne(ContextAffilie context, DetailPrestationGenComplexModel oldPrest)
            throws JadeApplicationException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        DetailPrestationModel detail = new DetailPrestationModel();

        if (ALImplServiceLocator.getDossierBusinessService().isModePaiementDirect(
                context.getContextDossier().getDossier().getDossierModel())) {

            DroitModel droit = ALImplServiceLocator.getDroitModelService().read(oldPrest.getIdDroit());

            // ajout de l'id du tiers b�n�ficiaire si n�cessaire
            if (JadeNumericUtil.isIntegerPositif(droit.getIdTiersBeneficiaire())) {
                detail.setIdTiersBeneficiaire(droit.getIdTiersBeneficiaire());
            } else {
                detail.setIdTiersBeneficiaire(context.getContextDossier().getDossier().getDossierModel()
                        .getIdTiersBeneficiaire());
            }
        }

        detail.setIdDroit(oldPrest.getIdDroit());
        detail.setAgeEnfant(oldPrest.getAgeEnfant());
        detail.setNumeroCompte(oldPrest.getNumeroCompte());
        detail.setRang(oldPrest.getRang());
        detail.setTypePrestation(oldPrest.getTypePrestation());

        detail.setMontant((new BigDecimal(oldPrest.getMontant()).negate()).toString());
        detail.setMontantCaisse(!JadeNumericUtil.isEmptyOrZero(oldPrest.getMontantCaisse()) ? (new BigDecimal(oldPrest
                .getMontantCaisse()).negate()).toString() : null);
        detail.setMontantCanton(!JadeNumericUtil.isEmptyOrZero(oldPrest.getMontantCanton()) ? (new BigDecimal(oldPrest
                .getMontantCanton()).negate()).toString() : null);
        detail.setCategorieTarif(oldPrest.getCategorieTarif());
        detail.setCategorieTarifCaisse(oldPrest.getCategorieTarifCaisse());
        detail.setCategorieTarifCanton(oldPrest.getCategorieTarifCanton());
        detail.setPeriodeValidite(oldPrest.getPeriodeValidite());

        detail.setTarifForce(oldPrest.getTarifForce());

        if(ALProperties.IMPOT_A_LA_SOURCE.getBooleanValue()
            && !JadeStringUtil.isBlankOrZero(oldPrest.getMontantIS()) ) {
            detail.setMontantIS(new BigDecimal(oldPrest.getMontantIS()).negate().toString());
            detail.setNumeroCompteIS(oldPrest.getNumeroCompteIS());
        }

        context.getContextDossier().addDetailPrestation(detail,
                ALServiceLocator.getDroitComplexModelService().read(oldPrest.getIdDroit()));

        return context;
    }

    /**
     * V�rifie si le <code>droit</code> est exportable pour la <code>date</code> indiqu�e. Si ce n'est pas le cas, un
     * avertissement est loggu�
     * 
     * @param context
     *            Context affili� de la g�n�ration en cours
     * @param droitCalcule
     *            Le droit � contr�ler
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected void checkExportPrestation(ContextAffilie context, CalculBusinessModel droitCalcule)
            throws JadePersistenceException, JadeApplicationException {

        if (!droitCalcule.isExportable()) {

            StringBuffer nomDroit = new StringBuffer();
            if (droitCalcule.getDroit().getEnfantComplexModel() != null) {
                nomDroit.append(
                        droitCalcule.getDroit().getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers()
                                .getDesignation1())
                        .append(" ")
                        .append(droitCalcule.getDroit().getEnfantComplexModel().getPersonneEtendueComplexModel()
                                .getTiers().getDesignation2());
            } else {
                nomDroit.append(JadeCodesSystemsUtil.getCode(droitCalcule.getDroit().getDroitModel().getTypeDroit()));
            }

            List<String> csvVal = new ArrayList<>();
            csvVal.add(context.getContextDossier().getDossier().getId());
            csvVal.add(context.getNumAffilie());

            context.getLogger()
                    .getWarningsLogger(
                            context.getContextDossier().getDossier().getId() + " - " + context.getNumAffilie(),
                            context.getContextDossier().getDossier().getAllocataireComplexModel()
                                    .getPersonneEtendueComplexModel().getTiers().getDesignation1()
                                    + " "
                                    + context.getContextDossier().getDossier().getAllocataireComplexModel()
                                            .getPersonneEtendueComplexModel().getTiers().getDesignation2(), csvVal)
                    .addMessage(
                            new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, ContextPrestation.class.getName(),
                                    "al.generation.warning.cantExportPrest", new String[] { nomDroit.toString() }));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.generation.prestations.GenPrestation#execute
     * (ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie)
     */
    @Override
    public void execute() throws JadeApplicationException, JadePersistenceException {

        if (context == null) {
            throw new ALGenerationException("GenPrestationAbstract#execute : context is null");
        }

        List<CalculBusinessModel> calcul = context.getContextDossier().getCalcul();

        while (calcul != null) {
            generatePrestation(context, calcul);
            calcul = context.getContextDossier().getCalcul();
        }
    }

    /**
     * G�n�re les prestations correspondant au <code>calcul</code> en fonction des param�tres contenus dans le
     * <code>context</code>
     * 
     * @param context
     *            Context de g�n�ration
     * @param calcul
     *            R�sultat du calcul
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected void generatePrestation(ContextAffilie context, List<CalculBusinessModel> calcul)
            throws JadeApplicationException, JadePersistenceException {

        if (context == null) {
            throw new ALGenerationException("GenPrestationAbstract#generatePrestation : context is null");
        }

        if (calcul == null) {
            throw new ALGenerationException("GenPrestationAbstract#generatePrestation : calcul is null");
        }

        for (int i = 0; i < calcul.size(); i++) {

            CalculBusinessModel droitCalcule = calcul.get(i);

            if (!JadeNumericUtil.isZeroValue(droitCalcule.getCalculResultMontantEffectif())) {
                addDetailPrestation(context, droitCalcule);
            }
        }
    }

    public boolean hasExistingPrestations() throws JadeApplicationServiceNotAvailableException,
            JadeApplicationException, JadePersistenceException {
        ContextDossier contextDossier = context.getContextDossier();
        return ALImplServiceLocator.getDetailPrestationGenComplexModelService().hasExistingPrestations(
                contextDossier.getDossier().getId(), contextDossier.getDebutPeriode(), contextDossier.getFinPeriode(),
                contextDossier.getIdDroit());
    }

    /**
     * @param context
     *            Context de g�n�ration
     * @param droitCalcule
     *            Droit li� � la prestation � initialiser
     * @return La prestation initialis�e
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected DetailPrestationModel initDetailPrestation(ContextAffilie context, CalculBusinessModel droitCalcule)
            throws JadeApplicationException, JadePersistenceException {

        if (context == null) {
            throw new ALGenerationException("GenPrestationAbstract#initDetailPrestation : context is null");
        }

        if (droitCalcule == null) {
            throw new ALGenerationException("GenPrestationAbstract#initDetailPrestation : droitCalcule is null");
        }

        DetailPrestationModel detail = new DetailPrestationModel();
        detail.setPeriodeValidite(context.getContextDossier().getCurrentPeriode());

        setAgeEnfant(droitCalcule, detail);

        if (droitCalcule.getDroit() != null) {
            detail.setIdDroit(droitCalcule.getDroit().getId());
        }

        // b�n�ficiaire
        if (ALImplServiceLocator.getDossierBusinessService().isModePaiementDirect(
                context.getContextDossier().getDossier().getDossierModel())) {
            if ((droitCalcule.getDroit() != null)
                    && JadeNumericUtil.isIntegerPositif(droitCalcule.getDroit().getDroitModel()
                            .getIdTiersBeneficiaire())) {
                detail.setIdTiersBeneficiaire(droitCalcule.getDroit().getDroitModel().getIdTiersBeneficiaire());
            } else {
                detail.setIdTiersBeneficiaire(context.getContextDossier().getDossier().getDossierModel()
                        .getIdTiersBeneficiaire());
            }
        }

        detail.setRang(droitCalcule.getRang());
        detail.setTypePrestation(droitCalcule.getType());

        detail.setMontant(droitCalcule.getCalculResultMontantEffectif());
        detail.setCategorieTarif(droitCalcule.getTarif());

        detail.setMontantCaisse(droitCalcule.getCalculResultMontantEffectifCaisse());
        detail.setCategorieTarifCaisse(droitCalcule.getTarifCaisse());

        detail.setMontantCanton(droitCalcule.getCalculResultMontantEffectifCanton());
        detail.setCategorieTarifCanton(droitCalcule.getTarifCanton());

        detail.setTarifForce(new Boolean(droitCalcule.isTarifForce()));
        // Le num�ro de compte est d�fini dans ContextPrestation, il est
        // n�cessaire de conna�tre l'en-t�te pour d�terminer la rubrique

        if(ALProperties.IMPOT_A_LA_SOURCE.getBooleanValue()) {
            detail.setMontantIS(droitCalcule.getCalculResultMontantIS());
        }
        return detail;
    }

    /**
     * Recherche des prestation existantes selon les informations contenues dans le context de g�n�ration
     * 
     * @param context
     *            Contexte de la g�n�ration
     * @return R�sultat de la recherche
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected DetailPrestationGenComplexSearchModel searchExistingPrest(ContextAffilie context)
            throws JadeApplicationException, JadePersistenceException {
        // recherche de prestations existante pour dans la p�riode en cours de
        // g�n�ration
        DetailPrestationGenComplexSearchModel search = new DetailPrestationGenComplexSearchModel();
        search.setForIdDossier(context.getContextDossier().getDossier().getId());
        search.setForPeriodeDebut(context.getContextDossier().getDebutPeriode());
        search.setForPeriodeFin(context.getContextDossier().getFinPeriode());
        search.setForIdDroit(context.getContextDossier().getIdDroit());
        // recherche les prest dont l'�tat est diff�rent de TMP
        search.setForEtatPrestation(ALCSPrestation.ETAT_TMP);
        // recherche les montants sup�rieur � 0
        search.setForMontant("0");
        search.setWhereKey("PrestationExistante");
        search.setOrderKey("PrestationExistante");

        return ALImplServiceLocator.getDetailPrestationGenComplexModelService().search(search);
    }

    /**
     * D�finit l'�ge de l'enfant dans le d�tail de prestation en fonction de la p�riode en cours de g�n�ration et de la
     * date de naissance. Si le droit n'est pas de type enfant ou formation, rien n'est fait
     * 
     * @param droitCalcule
     *            Droit contenant l'enfant
     * @param detail
     *            D�tail de la prestation dans laquelle d�finir l'�ge
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected void setAgeEnfant(CalculBusinessModel droitCalcule, DetailPrestationModel detail)
            throws JadeApplicationException, JadePersistenceException {

        if (droitCalcule == null) {
            throw new ALGenerationException("GenPrestationAbstract#setAgeEnfant : droitCalcule is null");
        }

        if (detail == null) {
            throw new ALGenerationException("GenPrestationAbstract#setAgeEnfant : detail is null");
        }

        if (ALCSDroit.TYPE_ENF.equals(droitCalcule.getType()) || ALCSDroit.TYPE_FORM.equals(droitCalcule.getType())) {
            detail.setAgeEnfant(ALServiceLocator.getPrestationBusinessService().getAgeEnfantDetailPrestation(
                    droitCalcule.getDroit(), detail));
        }
    }
}