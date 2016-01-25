/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.qd;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import ch.globaz.perseus.business.constantes.CSEtatDemande;
import ch.globaz.perseus.business.constantes.CSTypeDemande;
import ch.globaz.perseus.business.constantes.CSVariableMetier;
import ch.globaz.perseus.business.constantes.IPFConstantes;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.qd.QDException;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.qd.CSTypeQD;
import ch.globaz.perseus.business.models.qd.QD;
import ch.globaz.perseus.business.models.qd.QDAnnuelle;
import ch.globaz.perseus.business.models.qd.QDAnnuelleSearchModel;
import ch.globaz.perseus.business.models.qd.QDSearchModel;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamilleSearchModel;
import ch.globaz.perseus.business.models.variablemetier.VariableMetier;
import ch.globaz.perseus.business.models.variablemetier.VariableMetierSearch;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.qd.QDAnnuelleService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * @author DDE
 * 
 */
public class QDAnnuelleServiceImpl extends PerseusAbstractServiceImpl implements QDAnnuelleService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.qd.QDAnnuelleService#createOrRead(ch.globaz.perseus.business.models
     * .pcfaccordee.PCFAccordee, java.lang.String)
     */
    @Override
    public QDAnnuelle createOrRead(Demande demande, String excedantRevenu, String annee) throws QDException,
            JadePersistenceException {
        if (demande == null) {
            throw new QDException("Unable to create or read QDAnnuelle, the demande is null !");
        }
        if (JadeStringUtil.isEmpty(annee)) {
            throw new QDException("Unable to create or read QDAnnuelle, annee is empty !");
        }
        QDAnnuelleSearchModel qdAnnuelleSearchModel = new QDAnnuelleSearchModel();
        qdAnnuelleSearchModel.setForAnnee(annee);
        qdAnnuelleSearchModel.setForIdDossier(demande.getDossier().getId());
        qdAnnuelleSearchModel = search(qdAnnuelleSearchModel);
        QDAnnuelle qdAnnuelle = new QDAnnuelle();
        try {

            if (qdAnnuelleSearchModel.getSize() > 0) {
                qdAnnuelle = (QDAnnuelle) qdAnnuelleSearchModel.getSearchResults()[0];
                // On met à jour l'excédant de revenu si la demande n'est pas rétroactive
                if (JadeStringUtil.isEmpty(demande.getSimpleDemande().getDateFin())) {
                    qdAnnuelle.getSimpleQDAnnuelle().setExcedantRevenu(excedantRevenu);
                }
                qdAnnuelle.setSimpleQDAnnuelle(PerseusImplServiceLocator.getSimpleQDAnnuelleService().update(
                        qdAnnuelle.getSimpleQDAnnuelle()));
            } else {
                // Si il n'existe pas encore de QDAnnuelle pour la pcfaccordee
                qdAnnuelle.setDossier(demande.getDossier());
                qdAnnuelle.getSimpleQDAnnuelle().setAnnee(annee);
                qdAnnuelle.getSimpleQDAnnuelle().setIdDossier(qdAnnuelle.getDossier().getId());
                qdAnnuelle.getSimpleQDAnnuelle().setExcedantRevenu(excedantRevenu);
                qdAnnuelle.getSimpleQDAnnuelle().setExcedantRevenuCompense("0");
                qdAnnuelle.setSimpleQDAnnuelle(PerseusImplServiceLocator.getSimpleQDAnnuelleService().create(
                        qdAnnuelle.getSimpleQDAnnuelle()));
            }

            // Chargement des variables métiers pour les montants des QD
            VariableMetierSearch variableMetierSearch = new VariableMetierSearch();
            variableMetierSearch.setForDateValable("01." + qdAnnuelle.getSimpleQDAnnuelle().getAnnee());
            variableMetierSearch = PerseusServiceLocator.getVariableMetierService().search(variableMetierSearch);
            Hashtable<CSVariableMetier, VariableMetier> variablesMetier = new Hashtable<CSVariableMetier, VariableMetier>();
            for (JadeAbstractModel model : variableMetierSearch.getSearchResults()) {
                VariableMetier variableMetier = (VariableMetier) model;
                variablesMetier.put(CSVariableMetier.getEnumFromCodeSystem(variableMetier.getSimpleVariableMetier()
                        .getCsTypeVariableMetier()), variableMetier);
            }

            // Chargement de toutes les qds existantes
            QDSearchModel qdSearchModel = new QDSearchModel();
            qdSearchModel.setForIdQdAnnuelle(qdAnnuelle.getId());
            qdSearchModel = PerseusServiceLocator.getQDService().search(qdSearchModel);
            List<String> qdExistantes = new ArrayList<String>();
            // Mettre dans une liste une combinaison de idMembreFamille + csTypeQd pour contrôler plus tard si elle
            // existe
            for (JadeAbstractModel model : qdSearchModel.getSearchResults()) {
                QD qd = (QD) model;
                qdExistantes.add(qd.getMembreFamille().getId() + qd.getSimpleQD().getTypeQD().getCodeSystem());
            }

            // Pour chaque membre de la famille créer les QDs nécessaires
            Integer nbEnfantsMoins16 = 0;
            EnfantFamilleSearchModel enfantFamilleSearchModel = new EnfantFamilleSearchModel();
            enfantFamilleSearchModel.setForIdSituationFamiliale(demande.getSituationFamiliale().getId());
            enfantFamilleSearchModel = PerseusImplServiceLocator.getEnfantFamilleService().search(
                    enfantFamilleSearchModel);
            // Pour chacun des enfants
            for (JadeAbstractModel model : enfantFamilleSearchModel.getSearchResults()) {
                EnfantFamille enfantFamille = (EnfantFamille) model;
                String dateNaissance = enfantFamille.getEnfant().getMembreFamille().getPersonneEtendue().getPersonne()
                        .getDateNaissance();
                int ageEnfant = JadeDateUtil
                        .getNbYearsBetween(dateNaissance, demande.getSimpleDemande().getDateDebut());

                // Si l'enfant à moins de 16 ans
                if (ageEnfant < IPFConstantes.AGE_16ANS) {
                    // Création des qd frais de garde
                    if (!qdExistantes.contains(enfantFamille.getEnfant().getMembreFamille().getId()
                            + CSTypeQD.FRAIS_GARDE.getCodeSystem())) {
                        PerseusServiceLocator.getQDService().create(qdAnnuelle,
                                enfantFamille.getEnfant().getMembreFamille(), CSTypeQD.FRAIS_GARDE, variablesMetier,
                                false);
                    }
                    nbEnfantsMoins16++;
                }
                // Si l'enfant à moins de 25 ans
                if (ageEnfant < IPFConstantes.AGE_25ANS) {
                    // Création des qd frais de maladie
                    if (!qdExistantes.contains(enfantFamille.getEnfant().getMembreFamille().getId()
                            + CSTypeQD.FRAIS_MALADIE.getCodeSystem())) {
                        PerseusServiceLocator.getQDService().create(qdAnnuelle,
                                enfantFamille.getEnfant().getMembreFamille(), CSTypeQD.FRAIS_MALADIE, variablesMetier,
                                false);
                    }
                }
            }

            // Si il y'a des enfants de moins de 6 ans, créer les qds frais maladie pour les parents
            if (nbEnfantsMoins16 > 0) {
                // Création de la qd frais maladie pour le requérant
                if (!qdExistantes.contains(demande.getSituationFamiliale().getRequerant().getMembreFamille().getId()
                        + CSTypeQD.FRAIS_MALADIE.getCodeSystem())) {
                    PerseusServiceLocator.getQDService().create(qdAnnuelle,
                            demande.getSituationFamiliale().getRequerant().getMembreFamille(), CSTypeQD.FRAIS_MALADIE,
                            variablesMetier, false);
                }

                // Si il y'a un conjoint pour lui aussi
                if (!JadeStringUtil.isEmpty(demande.getSituationFamiliale().getConjoint().getId())) {
                    if (!qdExistantes.contains(demande.getSituationFamiliale().getConjoint().getMembreFamille().getId()
                            + CSTypeQD.FRAIS_MALADIE.getCodeSystem())) {
                        PerseusServiceLocator.getQDService().create(qdAnnuelle,
                                demande.getSituationFamiliale().getConjoint().getMembreFamille(),
                                CSTypeQD.FRAIS_MALADIE, variablesMetier, false);
                    }
                }
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new QDException("Service not available exception : " + e.getMessage(), e);
        } catch (SituationFamilleException e) {
            throw new QDException("SituationFamilleException during create or read QDAnnuelle : " + e.getMessage(), e);
        } catch (VariableMetierException e) {
            throw new QDException("VariableMetierException : " + e.getMessage(), e);
        }

        return qdAnnuelle;
    }

    @Override
    public QDAnnuelle createOrRead(PCFAccordee pcfAccordee, String annee) throws QDException, JadePersistenceException {
        if (pcfAccordee == null) {
            throw new QDException("Unable to create or read QDAnnuelle, the pcfAccordee is null !");
        }

        return this.createOrRead(pcfAccordee.getDemande(), pcfAccordee.getSimplePCFAccordee().getExcedantRevenu(),
                annee);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.qd.QDAnnuelleService#createSpecificFraisGarde(ch.globaz.perseus.business
     * .models.demande.Demande, java.lang.String, java.lang.String)
     */
    @Override
    public QDAnnuelle createSpecificFraisGarde(Demande demande, String excedantRevenu, String annee) throws Exception {
        if (demande == null) {
            throw new QDException("Unable to create or read QDAnnuelle, the demande is null !");
        }
        if (JadeStringUtil.isEmpty(annee)) {
            throw new QDException("Unable to create or read QDAnnuelle, annee is empty !");
        }
        if (!CSTypeDemande.PC_AVS_AI.getCodeSystem().equals(demande.getSimpleDemande().getTypeDemande())) {
            throw new QDException("Demande is not of type PC AVS/AI, unable to create specific qd frais de garde");
        }

        QDAnnuelleSearchModel qdAnnuelleSearchModel = new QDAnnuelleSearchModel();
        qdAnnuelleSearchModel.setForAnnee(annee);
        qdAnnuelleSearchModel.setForIdDossier(demande.getDossier().getId());
        qdAnnuelleSearchModel = search(qdAnnuelleSearchModel);
        QDAnnuelle qdAnnuelle = new QDAnnuelle();

        if (qdAnnuelleSearchModel.getSize() == 0) {

            try {
                // Création de la QD annuelle
                qdAnnuelle.setDossier(demande.getDossier());
                qdAnnuelle.getSimpleQDAnnuelle().setAnnee(annee);
                qdAnnuelle.getSimpleQDAnnuelle().setIdDossier(qdAnnuelle.getDossier().getId());
                qdAnnuelle.getSimpleQDAnnuelle().setExcedantRevenu(excedantRevenu);
                qdAnnuelle.getSimpleQDAnnuelle().setExcedantRevenuCompense("0");
                qdAnnuelle.setSimpleQDAnnuelle(PerseusImplServiceLocator.getSimpleQDAnnuelleService().create(
                        qdAnnuelle.getSimpleQDAnnuelle()));

                // Chargement des variables métiers pour les montants des QD
                VariableMetierSearch variableMetierSearch = new VariableMetierSearch();
                variableMetierSearch.setForDateValable("01." + qdAnnuelle.getSimpleQDAnnuelle().getAnnee());
                variableMetierSearch = PerseusServiceLocator.getVariableMetierService().search(variableMetierSearch);
                Hashtable<CSVariableMetier, VariableMetier> variablesMetier = new Hashtable<CSVariableMetier, VariableMetier>();
                for (JadeAbstractModel model : variableMetierSearch.getSearchResults()) {
                    VariableMetier variableMetier = (VariableMetier) model;
                    variablesMetier.put(CSVariableMetier.getEnumFromCodeSystem(variableMetier.getSimpleVariableMetier()
                            .getCsTypeVariableMetier()), variableMetier);
                }

                EnfantFamilleSearchModel enfantFamilleSearchModel = new EnfantFamilleSearchModel();
                enfantFamilleSearchModel.setForIdSituationFamiliale(demande.getSituationFamiliale().getId());
                enfantFamilleSearchModel = PerseusImplServiceLocator.getEnfantFamilleService().search(
                        enfantFamilleSearchModel);

                // Pour chacun des enfants
                for (JadeAbstractModel model : enfantFamilleSearchModel.getSearchResults()) {
                    EnfantFamille enfantFamille = (EnfantFamille) model;
                    String dateNaissance = enfantFamille.getEnfant().getMembreFamille().getPersonneEtendue()
                            .getPersonne().getDateNaissance();
                    int ageEnfant = JadeDateUtil.getNbYearsBetween(dateNaissance, demande.getSimpleDemande()
                            .getDateDebut());

                    // Si l'enfant à moins de 16 ans
                    if (ageEnfant < IPFConstantes.AGE_16ANS) {
                        // Création des qd frais de garde
                        PerseusServiceLocator.getQDService().create(qdAnnuelle,
                                enfantFamille.getEnfant().getMembreFamille(), CSTypeQD.FRAIS_GARDE, variablesMetier,
                                false);
                    }
                }

                // Validation de la demande
                demande.getSimpleDemande().setCsEtatDemande(CSEtatDemande.VALIDE.getCodeSystem());
                demande = PerseusServiceLocator.getDemandeService().update(demande);

            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new QDException("Service not available exception : " + e.getMessage(), e);
            } catch (SituationFamilleException e) {
                throw new QDException("SituationFamilleException during create or read QDAnnuelle : " + e.getMessage(),
                        e);
            } catch (VariableMetierException e) {
                throw new QDException("VariableMetierException : " + e.getMessage(), e);
            } catch (DemandeException e) {
                throw new QDException("DemandeException : " + e.getMessage(), e);
            }
        } else {
            // Validation de la demande
            demande.getSimpleDemande().setCsEtatDemande(CSEtatDemande.VALIDE.getCodeSystem());
            try {
                demande = PerseusServiceLocator.getDemandeService().update(demande);
            } catch (DemandeException e) {
                throw new QDException("DemandeException : " + e.getMessage(), e);
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new QDException("Service not available exception : " + e.getMessage(), e);
            }
        }

        return qdAnnuelle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.qd.QDAnnuelleService#delete(ch.globaz.perseus.business.models.qd.
     * QDAnnuelle)
     */
    @Override
    public QDAnnuelle delete(QDAnnuelle qdAnnuelle) throws QDException, JadePersistenceException {
        throw new QDException("Method non implémentée");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.qd.QDAnnuelleService#getMontantDisponible(ch.globaz.perseus.business
     * .models.qd.QDAnnuelle, java.lang.String, ch.globaz.perseus.business.models.qd.CSTypeQD)
     */
    @Override
    public float getMontantDisponible(QDAnnuelle qdAnnuelle, String idMembreFamille, CSTypeQD typeQD)
            throws QDException, JadePersistenceException {
        throw new QDException("Method non implémentée");
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.qd.QDAnnuelleService#read(java.lang.String, java.lang.String)
     */
    @Override
    public QDAnnuelle read(String idDossier, String annee) throws QDException, JadePersistenceException {
        throw new QDException("Method non implémentée");
    }

    @Override
    public QDAnnuelleSearchModel search(QDAnnuelleSearchModel searchModel) throws QDException, JadePersistenceException {
        if (searchModel == null) {
            throw new QDException("Unable to search QDAnnuelle, the searchModel passed is null !");
        }

        return (QDAnnuelleSearchModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.qd.QDAnnuelleService#update(ch.globaz.perseus.business.models.qd.
     * QDAnnuelle)
     */
    @Override
    public QDAnnuelle update(QDAnnuelle qdAnnuelle) throws QDException, JadePersistenceException {
        if (qdAnnuelle == null) {
            throw new QDException("Unable to update qdAnnuelle, the model passed is null!");
        }
        try {
            qdAnnuelle.setSimpleQDAnnuelle(PerseusImplServiceLocator.getSimpleQDAnnuelleService().update(
                    qdAnnuelle.getSimpleQDAnnuelle()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new QDException("Service not available " + e.getMessage(), e);
        }

        return qdAnnuelle;
    }

}
