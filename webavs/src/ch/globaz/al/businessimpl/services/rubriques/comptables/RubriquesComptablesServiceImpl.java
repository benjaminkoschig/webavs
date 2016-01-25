package ch.globaz.al.businessimpl.services.rubriques.comptables;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.constantes.ALConstRubriques;
import ch.globaz.al.business.exceptions.rubriques.ALRubriquesException;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.tarif.CategorieTarifComplexModel;
import ch.globaz.al.business.models.tarif.CategorieTarifComplexSearchModel;
import ch.globaz.al.business.models.tarif.CategorieTarifModel;
import ch.globaz.al.business.models.tarif.LegislationTarifModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.param.business.service.ParamServiceLocator;

/**
 * Implémentation du service générique permettant de récupérer une rubrique comptable
 * 
 * @author jts
 * 
 */
public class RubriquesComptablesServiceImpl extends ALAbstractBusinessServiceImpl implements RubriquesComptablesService {

    /**
     * 
     * @param dossier
     *            Dossier pour lequel le tarif doit être récupéré
     * @param detail
     *            detail de la prestation pour laquelle le tarif doit être récupéré
     * @param date
     *            Date pour laquelle effectuer la récupération
     * @return Code du canton (ex JU, NE, ...)
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected String getCanton(DossierModel dossier, DetailPrestationModel detail, String date)
            throws JadeApplicationException, JadePersistenceException {

        if (ALServiceLocator.getTarifBusinessService().isTarifCantonal(detail.getCategorieTarif())) {
            return JadeCodesSystemsUtil.getCode(ALImplServiceLocator.getCalculService().getCantonForTarif(
                    detail.getCategorieTarif()));
        } else {
            AssuranceInfo assurance = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(dossier, date);
            return JadeCodesSystemsUtil.getCode(ALImplServiceLocator.getAffiliationService()
                    .convertCantonNaos2CantonAF(assurance.getCanton()));
        }

    }

    /**
     * Recherche la catégorie de tarif
     * 
     * @param categorie
     *            Catégorie de tarif ( {@link ch.globaz.al.business.constantes.ALCSTarif#GROUP_CATEGORIE} )
     * 
     * @return Modèle de la catégorie de tarif correspondant à <code>categorie</code>
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected CategorieTarifComplexModel getCategorieTarif(String categorie) throws JadeApplicationException,
            JadePersistenceException {

        if (JadeStringUtil.isEmpty(categorie)) {
            throw new ALRubriquesException(
                    "RubriquesComptablesCCJUServiceImpl#getRubriqueRestitution : categorie is null or empty");
        }

        CategorieTarifComplexModel categorieTarif = null;

        if (ALCSTarif.CATEGORIE_LJU.equals(categorie)) {
            categorieTarif = new CategorieTarifComplexModel();
            LegislationTarifModel legislation = new LegislationTarifModel();
            legislation.setTypeLegislation(ALCSTarif.LEGISLATION_CANTONAL);
            categorieTarif.setLegislationTarifModel(legislation);
            CategorieTarifModel cattar = new CategorieTarifModel();
            cattar.setCategorieTarif(ALCSTarif.CATEGORIE_LJU);
            categorieTarif.setCategorieTarifModel(cattar);
            return categorieTarif;
        } else {

            CategorieTarifComplexSearchModel searchModel = new CategorieTarifComplexSearchModel();
            searchModel.setForCategorieTarif(categorie);
            searchModel = ALImplServiceLocator.getCategorieTarifComplexModelService().search(searchModel);

            if (searchModel.getSize() > 1) {
                throw new ALRubriquesException(
                        "RubriquesComptablesCCJUServiceImpl#getRubriqueRestitution : several legislation found");
            } else if (searchModel.getSize() == 0) {
                throw new ALRubriquesException(
                        "RubriquesComptablesCCJUServiceImpl#getRubriqueRestitution : no legislation found");
            } else {
                categorieTarif = ((CategorieTarifComplexModel) searchModel.getSearchResults()[0]);
            }
        }
        return categorieTarif;
    }

    protected String getRubrique(String date, String rubr) throws JadePersistenceException, JadeApplicationException {
        return (ParamServiceLocator.getParameterModelService()
                .getParameterByName(ALConstParametres.APPNAME, rubr, date)).getValeurAlphaParametre();
    }

    /**
     * Récupère la rubrique ADI
     * 
     * @param dossier
     *            Dossier pour lequel la rubrique doit être déterminée
     * @param entete
     *            En-tête pour laquelle la rubrique doit être déterminée
     * @param detail
     *            Détail de la prestation pour laquelle la rubrique doit être déterminée
     * @param date
     *            Date pour laquelle la rubrique doit être déterminée
     * 
     * @return Constante correspondant à la rubrique
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    protected String getRubriqueADI(DossierModel dossier, EntetePrestationModel entete, DetailPrestationModel detail,
            String date) throws JadeApplicationException, JadePersistenceException {
        return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_ADI);
    }

    /**
     * Récupère la rubrique paysan
     * 
     * @param dossier
     *            Dossier pour lequel la rubrique doit être déterminée
     * @param entete
     *            En-tête pour laquelle la rubrique doit être déterminée
     * @param detail
     *            Détail de la prestation pour laquelle la rubrique doit être déterminée
     * @param date
     *            Date pour laquelle la rubrique doit être déterminée
     * 
     * @return Constante correspondant à la rubrique
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected String getRubriqueAgriculteur(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {
        return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_AGRICULTEUR);
    }

    /**
     * Récupère la rubrique indépendant collaborateur agricole
     * 
     * @param dossier
     *            Dossier pour lequel la rubrique doit être déterminée
     * @param entete
     *            En-tête pour laquelle la rubrique doit être déterminée
     * @param detail
     *            Détail de la prestation pour laquelle la rubrique doit être déterminée
     * @param date
     *            Date pour laquelle la rubrique doit être déterminée
     * 
     * @return Constante correspondant à la rubrique
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected String getRubriqueCollaborateurAgricole(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {
        return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_COLLABORATEUR_AGRICOLE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.rubriques.comptables. RubriquesComptablesService
     * #getRubriqueComptable(ch.globaz.al.business.models.dossier.DossierModel,
     * ch.globaz.al.business.models.prestation.EntetePrestationModel, java.lang.String)
     */
    @Override
    public String getRubriqueComptable(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {

        if (ALCSPrestation.BONI_RESTITUTION.equals(entete.getBonification())) {
            return getRubriqueRestitution(dossier, entete, detail, date);
        } else if (ALCSDossier.ACTIVITE_COLLAB_AGRICOLE.equals(dossier.getActiviteAllocataire())) {
            return getRubriqueCollaborateurAgricole(dossier, entete, detail, date);
        } else if (ALCSDossier.ACTIVITE_AGRICULTEUR.equals(dossier.getActiviteAllocataire())) {
            return getRubriqueAgriculteur(dossier, entete, detail, date);
        } else if (ALCSDossier.ACTIVITE_PECHEUR.equals(dossier.getActiviteAllocataire())) {
            return getRubriquePecheur(dossier, entete, detail, date);
        } else if (ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE.equals(dossier.getActiviteAllocataire())) {
            return getRubriqueTravailleurAgricole(dossier, entete, detail, date);
        } else if (ALCSDossier.ACTIVITE_INDEPENDANT.equals(dossier.getActiviteAllocataire())) {
            return getRubriqueIndependant(dossier, entete, detail, date);
        } else if (ALCSDossier.ACTIVITE_NONACTIF.equals(dossier.getActiviteAllocataire())) {
            return getRubriqueNonActif(dossier, entete, detail, date);
        } else {
            return getRubriqueSalarie(dossier, entete, detail, date);
        }
    }

    /**
     * Récupère la rubrique indépendant
     * 
     * @param dossier
     *            Dossier pour lequel la rubrique doit être déterminée
     * @param entete
     *            En-tête pour laquelle la rubrique doit être déterminée
     * @param detail
     *            Détail de la prestation pour laquelle la rubrique doit être déterminée
     * @param date
     *            Date pour laquelle la rubrique doit être déterminée
     * 
     * @return Constante correspondant à la rubrique
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected String getRubriqueIndependant(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {
        if (ALCSPrestation.STATUT_ADI.equals(entete.getStatut())) {
            return getRubriqueADI(dossier, entete, detail, date);
        } else {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_INDEPENDANT);
        }
    }

    /**
     * Récupère la rubrique non-actifs
     * 
     * @param dossier
     *            Dossier pour lequel la rubrique doit être déterminée
     * @param entete
     *            En-tête pour laquelle la rubrique doit être déterminée
     * @param detail
     *            Détail de la prestation pour laquelle la rubrique doit être déterminée
     * @param date
     *            Date pour laquelle la rubrique doit être déterminée
     * 
     * @return Constante correspondant à la rubrique
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    protected String getRubriqueNonActif(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {
        if (ALCSPrestation.STATUT_ADI.equals(entete.getStatut())) {
            return getRubriqueADI(dossier, entete, detail, date);
        } else {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_NON_ACTIF);
        }
    }

    /**
     * Récupère la rubrique pour un allocataire pêcheur
     * 
     * @param dossier
     *            Dossier pour lequel la rubrique doit être déterminée
     * @param entete
     *            En-tête pour laquelle la rubrique doit être déterminée
     * @param detail
     *            Détail de la prestation pour laquelle la rubrique doit être déterminée
     * @param date
     *            Date pour laquelle la rubrique doit être déterminée
     * 
     * @return Constante correspondant à la rubrique
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    protected String getRubriquePecheur(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {
        return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_AGRICULTEUR);
    }

    /**
     * Récupère la rubrique restitution
     * 
     * @param dossier
     *            Dossier pour lequel la rubrique doit être déterminée
     * @param entete
     *            En-tête pour laquelle la rubrique doit être déterminée
     * @param detail
     *            Détail de la prestation pour laquelle la rubrique doit être déterminée
     * @param date
     *            Date pour laquelle la rubrique doit être déterminée
     * 
     * @return Constante correspondant à la rubrique
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected String getRubriqueRestitution(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {
        return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_RESTITUTION);
    }

    /**
     * Récupère la rubrique standard AF
     * 
     * @param dossier
     *            Dossier pour lequel la rubrique doit être déterminée
     * @param entete
     *            En-tête pour laquelle la rubrique doit être déterminée
     * @param detail
     *            Détail de la prestation pour laquelle la rubrique doit être déterminée
     * @param date
     *            Date pour laquelle la rubrique doit être déterminée
     * 
     * @return Constante correspondant à la rubrique
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected String getRubriqueSalarie(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {
        if (ALCSPrestation.STATUT_ADI.equals(entete.getStatut())) {
            return getRubriqueADI(dossier, entete, detail, date);
        } else {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_SALARIE);
        }
    }

    /**
     * Récupère la rubrique travailleur agricole
     * 
     * @param dossier
     *            Dossier pour lequel la rubrique doit être déterminée
     * @param entete
     *            En-tête pour laquelle la rubrique doit être déterminée
     * @param detail
     *            Détail de la prestation pour laquelle la rubrique doit être déterminée
     * @param date
     *            Date pour laquelle la rubrique doit être déterminée
     * 
     * @return Constante correspondant à la rubrique
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected String getRubriqueTravailleurAgricole(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {
        return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_TRAVAILLEUR_AGRICOLE);
    }
}