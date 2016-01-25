/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.document;

import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.CSTypeDemande;
import ch.globaz.perseus.business.constantes.document.DocumentTypes;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.DecisionForTypeDocument;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.decision.DecisionBuilder;
import ch.globaz.perseus.business.services.document.DecisionBuilderProviderService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.decision.DecisionAjoutAidesCategoriellesBuilder;
import ch.globaz.perseus.businessimpl.services.decision.DecisionChangementConditionsPersonnellesBuilder;
import ch.globaz.perseus.businessimpl.services.decision.DecisionNonEntreeEnMatiereBuilder;
import ch.globaz.perseus.businessimpl.services.decision.DecisionOctroiHorsRiBuilder;
import ch.globaz.perseus.businessimpl.services.decision.DecisionOctroiPartielBuilder;
import ch.globaz.perseus.businessimpl.services.decision.DecisionOctroiRiSansProjetBuilder;
import ch.globaz.perseus.businessimpl.services.decision.DecisionProjetBuilder;
import ch.globaz.perseus.businessimpl.services.decision.DecisionRefusBuilder;
import ch.globaz.perseus.businessimpl.services.decision.DecisionRenonciationBuilder;
import ch.globaz.perseus.businessimpl.services.decision.DecisionSuiteAuProjetBuilder;
import ch.globaz.perseus.businessimpl.services.decision.DecisionSuppressionEconomiqueBuilder;
import ch.globaz.perseus.businessimpl.services.decision.DecisionSuppressionPersonnelleBuilder;
import ch.globaz.perseus.businessimpl.services.decision.DecisionSuppressionRenonciationVolontaireBuilder;

/**
 * @author MBO
 * 
 */
public class DecisionBuilderProviderServiceImpl extends PerseusAbstractServiceImpl implements
        DecisionBuilderProviderService {

    private DocumentTypes defineTypeDocumentForDecision(String idDecision) throws Exception {
        try {
            // Récupération de la décision actuelle
            DecisionForTypeDocument doc = PerseusServiceLocator.getDecisionForTypeDocumentService().read(idDecision);
            // Récupération de l'id de la demande actuelle
            Demande demande = PerseusServiceLocator.getDemandeService().read(doc.getSimpleDemande().getIdDemande());
            // Récupération de la demande précédente
            Demande demandePrecedante = PerseusServiceLocator.getDemandeService().getDemandePrecedante(demande);
            // Récupération de la demande précédente
            PCFAccordee pcfAncienne = null;
            if (demandePrecedante != null) {
                pcfAncienne = PerseusServiceLocator.getPCFAccordeeService().readForDemande(demandePrecedante.getId());
            }

            // Récupération du type de décision
            String typeDecision = doc.getSimpleDecision().getCsTypeDecision();
            Boolean etatRi = doc.getSimpleDemande().getFromRI();

            /**
             * CONDITION DE GENERATION DE DOCUMENT D'OCTROI
             */
            if (CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(typeDecision)) {
                if (CSTypeDemande.AIDES_CATEGORIELLES.getCodeSystem().equals(
                        demande.getSimpleDemande().getTypeDemande())) {
                    /**
                     * CONDITION DE GENERATION DE DOCUMENT D'OCTROI Décision - Ajout d'aides catégorielles
                     */
                    // Modèle de document: 6
                    return DocumentTypes.OCTROI_AJOUT_AIDES_CATEGORIELLES;
                }

                if (CSTypeDemande.REVISION_EXTRAORDINAIRE.getCodeSystem().equals(
                        doc.getSimpleDemande().getTypeDemande())) {
                    /**
                     * CONDITION DE GENERATION DE DOCUMENT D'OCTROI Décision - Annonce de changement
                     */
                    // Modèle de document: 7
                    return DocumentTypes.OCTROI_ANNONCE_CHANGEMENT;
                } else {
                    if (etatRi == false) {
                        /**
                         * CONDITION DE GENERATION DE DOCUMENT D'OCTROI SANS RI Décision - Bénéficiaire hors RI
                         */
                        // Modèle de document: 1
                        return DocumentTypes.OCTROI_HORS_RI;
                    } else {

                        // Récupération du projet lié à la décision
                        DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
                        decisionSearchModel.setForIdDemande(doc.getSimpleDemande().getId());
                        decisionSearchModel.setForNumeroDecision(doc.getSimpleDecision().getNumeroDecision());
                        decisionSearchModel.setForCsTypeDecision(CSTypeDecision.PROJET.getCodeSystem());
                        decisionSearchModel.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
                        boolean hasProjet = PerseusServiceLocator.getDecisionService().count(decisionSearchModel) > 0;

                        if (hasProjet == false) {
                            /**
                             * CONDITION DE GENERATION DE DOCUMENT D'OCTROI avec RI Décision - Sans projet de décision
                             */
                            // Modèle de document: 2
                            return DocumentTypes.OCTROI_RI_SANS_PROJET;
                        } else {
                            /**
                             * CONDITION DE GENERATION DE DOCUMENT D'OCTROI avec RI Décision - Avec projet de décision
                             */
                            // Modèle de document: 5
                            return DocumentTypes.OCTROI_RI_AVEC_PROJET;
                        }
                    }
                }
            }

            /**
             * CONDITION DE GENERATION DE DOCUMENT D'OCTROI PARTIEL Décision de refus - Condition economique pas remplis
             */

            if (CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(typeDecision)) {
                if (null == pcfAncienne) {
                    // Modèle de document: 3
                    return DocumentTypes.OCTROI_PARTIEL;
                } else {
                    // Récupération du projet lié à la décision
                    DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
                    decisionSearchModel.setForIdDemande(demandePrecedante.getSimpleDemande().getId());
                    decisionSearchModel.setForCsTypeDecision(CSTypeDecision.OCTROI_PARTIEL.getCodeSystem());
                    decisionSearchModel.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
                    boolean hasDemandePartiel = PerseusServiceLocator.getDecisionService().count(decisionSearchModel) > 0;
                    if (hasDemandePartiel) {
                        // Modèle de document: 3
                        return DocumentTypes.OCTROI_PARTIEL;
                    } else {
                        // Modèle de document 8
                        return DocumentTypes.OCTROI_PARTIEL_MODIF_ECONOMIQUE;
                    }

                }
            }

            /**
             * CONDITION DE GENERATION DE DOCUMENT PROJET DE DECISION Projet de décision
             */

            if (CSTypeDecision.PROJET.getCodeSystem().equals(typeDecision) && (etatRi == true)) {
                // Modèle de document: 4
                return DocumentTypes.PROJET;
            }

            /**
             * CONDITION DE GENERATION DE DOCUMENT DE REFUS ET SUPPRESSION - Condition economique plus remplies,
             * condition personnelles plus remplies
             */

            if (CSTypeDecision.REFUS_SANS_CALCUL.getCodeSystem().equals(doc.getSimpleDecision().getCsTypeDecision())) {
                if (null == pcfAncienne) {
                    // Modèle de document: 9
                    return DocumentTypes.REFUS;
                } else {
                    // Modèle de document: 10
                    return DocumentTypes.SUPPRESSION;
                }
            }

            /**
             * CONDITION DE GENERATION DE DOCUMENT DE NON ENTREE EN MATIERE -
             */
            if (CSTypeDecision.NON_ENTREE_MATIERE.getCodeSystem().equals(doc.getSimpleDecision().getCsTypeDecision())) {
                // Modèle de document:
                return DocumentTypes.NON_ENTREE_MATIERE;
            }

            /**
             * CONDITION DE GENERATION DE DOCUMENT DE RENONCIATION
             */

            if (CSTypeDecision.RENONCIATION.getCodeSystem().equals(doc.getSimpleDecision().getCsTypeDecision())) {
                // Modèle de document: 11
                return DocumentTypes.RENONCIATION;
            }
            if (CSTypeDecision.SUPPRESSION.getCodeSystem().equals(doc.getSimpleDecision().getCsTypeDecision())) {
                return DocumentTypes.SUPPRESSION_VOLONTAIRE;
            }

            else {
                throw new DecisionException(
                        "DecisionBuilderProviderServiceImpl - Pas de modèle correspondant aux conditions de détection");
            }
        } catch (Exception e) {
            throw new DecisionException(
                    "Erreur dans le choix de la décision à générer, dans : DecisionBuilderProviderServiceImpl"
                            + e.toString());
        }
    }

    @Override
    public DecisionBuilder getBuilderFor(String decisionId) throws Exception {
        switch (defineTypeDocumentForDecision(decisionId)) {

            case OCTROI_AJOUT_AIDES_CATEGORIELLES:
                return new DecisionAjoutAidesCategoriellesBuilder();

            case OCTROI_PARTIEL:
                return new DecisionOctroiPartielBuilder();

            case OCTROI_HORS_RI:
                return new DecisionOctroiHorsRiBuilder();

            case OCTROI_RI_SANS_PROJET:
                return new DecisionOctroiRiSansProjetBuilder();

            case PROJET:
                return new DecisionProjetBuilder();

            case OCTROI_RI_AVEC_PROJET:
                return new DecisionSuiteAuProjetBuilder();

            case RENONCIATION:
                return new DecisionRenonciationBuilder();

            case REFUS:
                return new DecisionRefusBuilder();

            case SUPPRESSION:
                return new DecisionSuppressionPersonnelleBuilder();

            case OCTROI_ANNONCE_CHANGEMENT:
                return new DecisionChangementConditionsPersonnellesBuilder();

            case OCTROI_PARTIEL_MODIF_ECONOMIQUE:
                return new DecisionSuppressionEconomiqueBuilder();

            case SUPPRESSION_VOLONTAIRE:
                return new DecisionSuppressionRenonciationVolontaireBuilder();

            case NON_ENTREE_MATIERE:
                return new DecisionNonEntreeEnMatiereBuilder();

            default:
                throw new DecisionException(
                        "DecisionBuilderProviderServiceImpl - Pas de modèle correspondant aux conditions de détection");
        }
    }
}
