package ch.globaz.al.businessimpl.copies.defaut;

import globaz.hermes.utils.DateUtils;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.models.dossier.CopieComplexModel;
import ch.globaz.al.business.models.dossier.DossierDecisionComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.pyxis.business.model.CompositionTiersSimpleModel;
import ch.globaz.pyxis.business.model.CompositionTiersSimpleModelSearch;

/**
 * Classe contenant les m�thodes commune � toutes les classes d'initialisation des copies par d�faut
 * 
 * @author jts
 * 
 */
public abstract class DefaultCopiesLoaderAbstract {

    /**
     * Contexte contenant les informations permettant de g�n�rer la liste des copies par d�faut
     */
    protected ContextDefaultCopiesLoader context = null;

    /**
     * Initialise une copie avec les informations par d�faut pour l'affili�
     * 
     * @return nouvelle copie
     * @throws JadePersistenceException
     *             Exception lev�e si l'id tiers de l'affili� n'a pas pu �tre r�cup�r�
     * @throws JadeApplicationException
     *             Exception lev�e si l'id tiers de l'affili� n'a pas pu �tre r�cup�r�
     */
    protected CopieComplexModel getCopieAffilie() throws JadePersistenceException, JadeApplicationException {

        CopieComplexModel copie = new CopieComplexModel();

        copie.getCopieModel().setIdDossier(context.getDossier().getDossierModel().getIdDossier());
        copie.getCopieModel().setIdTiersDestinataire(
                AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(
                        context.getDossier().getDossierModel().getNumeroAffilie()));
        copie.getCopieModel().setTypeCopie(context.getTypeCopie());

        return copie;
    }

    /**
     * Initialise une copie avec les informations par d�faut pour l'agence communale
     * 
     * @return nouvelle copie
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws
     */
    protected CopieComplexModel getCopieAgenceCommunale() throws JadePersistenceException, JadeApplicationException {

        CopieComplexModel copie = new CopieComplexModel();

        String idTiersDestinataire = "0";

        DossierDecisionComplexModel dossierDecision = null;
        if (context.getDossier() instanceof DossierDecisionComplexModel) {
            dossierDecision = (DossierDecisionComplexModel) context.getDossier();
        } else {
            dossierDecision = ALServiceLocator.getDossierDecisionComplexeModelService().read(
                    context.getDossier().getId());
        }

        CompositionTiersSimpleModelSearch lienTiersSearch = new CompositionTiersSimpleModelSearch();
        lienTiersSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        lienTiersSearch.setForIdTiersParent(dossierDecision.getTiersLiaisonComplexModel().getTiersReference()
                .getIdTiers());
        lienTiersSearch.setForTypeLien(ALCSTiers.TYPE_LIAISON_AG_COMMUNALE);

        lienTiersSearch = (CompositionTiersSimpleModelSearch) JadePersistenceManager.search(lienTiersSearch);

        // Si la date de d�but de validit� est vide alors cette variable contient la date de fin de validit� du
        // dossier
        // Dans un dossier, une des deux dates est forc�ment renseign�e
        String debutOrFinValiditeDossierAF = context.getDossier().getDossierModel().getDebutValidite();
        if (JadeStringUtil.isBlankOrZero(debutOrFinValiditeDossierAF)) {
            debutOrFinValiditeDossierAF = context.getDossier().getDossierModel().getFinValidite();
        }

        for (JadeAbstractModel abstractModel : lienTiersSearch.getSearchResults()) {

            CompositionTiersSimpleModel lienTiers = (CompositionTiersSimpleModel) abstractModel;

            String debutLienAgenceCommunale = lienTiers.getDebutRelation();
            String finLienAgenceCommunale = lienTiers.getFinRelation();

            debutLienAgenceCommunale = DateUtils.convertDate(debutLienAgenceCommunale, DateUtils.AAAAMMJJ,
                    DateUtils.JJMMAAAA_DOTS);
            finLienAgenceCommunale = DateUtils.convertDate(finLienAgenceCommunale, DateUtils.AAAAMMJJ,
                    DateUtils.JJMMAAAA_DOTS);

            boolean isAgenceCommunaleForPeriodeValiditeDossier = JadeDateUtil.isDateBefore(debutLienAgenceCommunale,
                    debutOrFinValiditeDossierAF)
                    || debutLienAgenceCommunale.equalsIgnoreCase(debutOrFinValiditeDossierAF);

            isAgenceCommunaleForPeriodeValiditeDossier = isAgenceCommunaleForPeriodeValiditeDossier
                    && (JadeStringUtil.isBlankOrZero(finLienAgenceCommunale)
                            || JadeDateUtil.isDateAfter(finLienAgenceCommunale, debutOrFinValiditeDossierAF) || finLienAgenceCommunale
                                .equalsIgnoreCase(debutOrFinValiditeDossierAF));

            isAgenceCommunaleForPeriodeValiditeDossier = isAgenceCommunaleForPeriodeValiditeDossier
                    || (JadeStringUtil.isBlankOrZero(debutLienAgenceCommunale) && JadeStringUtil
                            .isBlankOrZero(finLienAgenceCommunale));

            if (isAgenceCommunaleForPeriodeValiditeDossier) {
                idTiersDestinataire = lienTiers.getIdTiersEnfant();
                break;
            }

        }

        if (JadeNumericUtil.isEmptyOrZero(idTiersDestinataire)) {
            return null;
        } else {
            copie.getCopieModel().setIdDossier(context.getDossier().getDossierModel().getIdDossier());
            copie.getCopieModel().setIdTiersDestinataire(idTiersDestinataire);
            copie.getCopieModel().setTypeCopie(context.getTypeCopie());
            return copie;
        }
    }

    /**
     * Initialise une copie avec les informations par d�faut pour l'allocataire
     * 
     * @return nouvelle copie
     */
    protected CopieComplexModel getCopieAllocataire() {

        CopieComplexModel copie = new CopieComplexModel();

        copie.getCopieModel().setIdDossier(context.getDossier().getDossierModel().getIdDossier());

        // setter le tiers b�nficiaire si autre 0, sinon tiers Allocataire
        copie.getCopieModel().setIdTiersDestinataire(
                JadeStringUtil.isBlankOrZero(context.getDossier().getDossierModel().getIdTiersBeneficiaire()) ? context
                        .getDossier().getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                        .getIdTiers() : context.getDossier().getDossierModel().getIdTiersBeneficiaire());

        copie.getCopieModel().setTypeCopie(context.getTypeCopie());
        copie.getCopieModel().setImpressionBatch(false);

        return copie;
    }

    /**
     * Charge la liste des copies par d�faut
     * 
     * @return liste des copies par d�faut
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public abstract ArrayList<CopieComplexModel> getListCopies() throws JadePersistenceException,
            JadeApplicationException;
}
