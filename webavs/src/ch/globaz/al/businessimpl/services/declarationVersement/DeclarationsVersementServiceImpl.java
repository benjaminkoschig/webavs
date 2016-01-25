package ch.globaz.al.businessimpl.services.declarationVersement;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;
import java.util.ArrayList;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALCSAllocataire;
import ch.globaz.al.business.constantes.ALCSDeclarationVersement;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstDeclarationVersement;
import ch.globaz.al.business.constantes.ALConstProtocoles;
import ch.globaz.al.business.exceptions.declarationVersement.ALDeclarationVersementException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierDeclarationVersementComplexModel;
import ch.globaz.al.business.models.dossier.DossierDeclarationVersementComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.declarationVersement.DeclarationVersementService;
import ch.globaz.al.business.services.declarationVersement.DeclarationsVersementService;
import ch.globaz.al.business.services.documents.DocumentDataContainer;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Classe d'implémentation du service DeclarationVersementService
 * 
 * @author PTA
 * 
 */
public class DeclarationsVersementServiceImpl implements DeclarationsVersementService {

    @Override
    public JadePrintDocumentContainer getDeclarationsFrontaliers(String periodeDe, String periodeA, String dateImpr,
            String typeDocument, String numDossier, String numAffilie, Boolean textImpot)
            throws JadeApplicationException, JadePersistenceException {
        // contrôle des paramètres
        if (!JadeDateUtil.isGlobazDate(periodeDe)) {
            throw new ALDeclarationVersementException("DeclarationsVersementServiceImpl# getDeclarationsFrontalier "
                    + periodeDe + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(periodeA)) {
            throw new ALDeclarationVersementException("DeclarationsVersementServiceImpl# getDeclarationsFrontalier "
                    + periodeA + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(dateImpr)) {
            throw new ALDeclarationVersementException("DeclarationsVersementServiceImpl# getDeclarationsFrontalier "
                    + dateImpr + " is not a valid globaz's date ");
        }
        if (!JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_DET, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_GLOB, false)) {
            throw new ALDeclarationVersementException("DeclarationsVersementServiceImpl# getDeclarationsFrontalier "
                    + typeDocument + " is not a valid document's type");
        }

        DossierDeclarationVersementComplexSearchModel dossierSearch = new DossierDeclarationVersementComplexSearchModel();
        // si num dossier ou num Affilie
        if (!JadeStringUtil.isBlankOrZero(numDossier)) {
            dossierSearch.setForIdDossier(numDossier);
        } else if (!JadeStringUtil.isBlankOrZero(numAffilie)) {
            dossierSearch.setForNumAffilie(numAffilie);
        }

        dossierSearch.setForPermis(ALCSAllocataire.PERMIS_G);
        dossierSearch.setForDateDebut(periodeDe);
        dossierSearch.setForDateFin(periodeA);
        dossierSearch.setForTypeBonification(ALCSPrestation.BONI_INDIRECT);
        dossierSearch.setForEtatPrestation(ALCSPrestation.ETAT_CO);

        dossierSearch.setOrderKey("affilieAlloc");

        dossierSearch = ALImplServiceLocator.getDossierDeclarationVersementComplexModelService().search(dossierSearch);

        return getDocuments(dossierSearch, periodeDe, periodeA, dateImpr, typeDocument,
                ALCSDeclarationVersement.DECLA_VERS_IND_FRONT, textImpot);
    }

    @Override
    public JadePrintDocumentContainer getDeclarationsImposeSource(String periodeDe, String periodeA, String dateImpr,
            String typeDocument, String numDossier, String numAffilie, Boolean textImpot)
            throws JadeApplicationException, JadePersistenceException {

        // contrôle des paramètres

        if (!JadeDateUtil.isGlobazDate(periodeDe)) {
            throw new ALDeclarationVersementException("DeclarationsVersementServiceImpl# getDeclarationsImposeSource "
                    + periodeDe + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(periodeA)) {
            throw new ALDeclarationVersementException("DeclarationsVersementServiceImpl# getDeclarationsImposeSource "
                    + periodeA + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(dateImpr)) {
            throw new ALDeclarationVersementException("DeclarationsVersementServiceImpl# getDeclarationsImposeSource "
                    + dateImpr + " is not a valid globaz's date ");
        }
        if (!JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_DET, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_GLOB, false)) {
            throw new ALDeclarationVersementException("DeclarationsVersementServiceImpl# getDeclarationsImposeSource "
                    + typeDocument + " is not a valid document's type");
        }

        DossierDeclarationVersementComplexSearchModel dossierSearch = new DossierDeclarationVersementComplexSearchModel();

        // si num dossier ou num Affilie
        if (!JadeStringUtil.isBlankOrZero(numDossier)) {
            dossierSearch.setForIdDossier(numDossier);
        } else if (!JadeStringUtil.isBlankOrZero(numAffilie)) {
            dossierSearch.setForNumAffilie(numAffilie);
        }
        dossierSearch.setForImpotSource(Boolean.TRUE);
        dossierSearch.setForNotIdTiersBeneficiaire("0");
        dossierSearch.setForDateDebut(periodeDe);
        dossierSearch.setForDateFin(periodeA);
        dossierSearch.setForTypeBonification(ALCSPrestation.BONI_DIRECT);
        dossierSearch.setForEtatPrestation(ALCSPrestation.ETAT_CO);

        dossierSearch = ALImplServiceLocator.getDossierDeclarationVersementComplexModelService().search(dossierSearch);

        return getDocuments(dossierSearch, periodeDe, periodeA, dateImpr, typeDocument,
                ALCSDeclarationVersement.DECLA_VERS_DIR_IMP_SOURCE, textImpot);

    }

    @Override
    public JadePrintDocumentContainer getDeclarationsNonActifPaiementsIndirects(String periodeDe, String periodeA,
            String dateImpr, String typeDocument, String numDossier, String numAffilie, Boolean textImpot)
            throws JadeApplicationException, JadePersistenceException {

        // contrôle des paramètres

        if (!JadeDateUtil.isGlobazDate(periodeDe)) {
            throw new ALDeclarationVersementException(
                    "DeclarationsVersementServiceImpl# getDeclarationsNonActifsPaiementsDirect " + periodeDe
                            + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(periodeA)) {
            throw new ALDeclarationVersementException(
                    "DeclarationsVersementServiceImpl# getDeclarationsNonActifsPaiementsDirect " + periodeA
                            + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(dateImpr)) {
            throw new ALDeclarationVersementException(
                    "DeclarationsVersementServiceImpl# getDeclarationsNonActifsPaiementsDirect " + dateImpr
                            + " is not a valid globaz's date ");
        }
        if (!JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_DET, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_GLOB, false)) {
            throw new ALDeclarationVersementException(
                    "DeclarationsVersementServiceImpl# getDeclarationsNonActifPaiementsIndirects " + typeDocument
                            + " is not a valid document's type");
        }

        DossierDeclarationVersementComplexSearchModel dossierSearch = new DossierDeclarationVersementComplexSearchModel();

        // si num dossier ou num Affilie
        if (!JadeStringUtil.isBlankOrZero(numDossier)) {
            dossierSearch.setForIdDossier(numDossier);
        } else if (!JadeStringUtil.isBlankOrZero(numAffilie)) {
            dossierSearch.setForNumAffilie(numAffilie);
        }
        dossierSearch.setForActiviteAlloc(ALCSDossier.ACTIVITE_NONACTIF);
        dossierSearch.setForDateDebut(periodeDe);
        dossierSearch.setForDateFin(periodeA);
        dossierSearch.setForTypeBonification(ALCSPrestation.BONI_INDIRECT);
        dossierSearch.setForEtatPrestation(ALCSPrestation.ETAT_CO);
        dossierSearch = ALImplServiceLocator.getDossierDeclarationVersementComplexModelService().search(dossierSearch);

        return getDocuments(dossierSearch, periodeDe, periodeA, dateImpr, typeDocument,
                ALCSDeclarationVersement.DECLA_VERS_IND_NON_ACT, textImpot);

    }

    @Override
    public JadePrintDocumentContainer getDeclarationsVersementAffilie(String numAffilie, String periodeDe,
            String periodeA, String dateImpr, String typeDocument, Boolean textImpot) throws JadeApplicationException,
            JadePersistenceException {

        // contrôle des paramètres

        if (!JadeDateUtil.isGlobazDate(periodeDe)) {
            throw new ALDeclarationVersementException(
                    "DeclarationsVersementServiceImpl# getDeclarationsNonActifsPaiementsDirect " + periodeDe
                            + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(periodeA)) {
            throw new ALDeclarationVersementException(
                    "DeclarationsVersementServiceImpl# getDeclarationsNonActifsPaiementsDirect " + periodeA
                            + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(dateImpr)) {
            throw new ALDeclarationVersementException(
                    "DeclarationsVersementServiceImpl# getDeclarationsNonActifsPaiementsDirect " + dateImpr
                            + " is not a valid globaz's date ");
        }
        if (!JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_DET, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_GLOB, false)) {
            throw new ALDeclarationVersementException(
                    "DeclarationsVersementServiceImpl# getDeclarationsVersementAffilie " + typeDocument
                            + " is not a valid document's type");
        }

        DossierDeclarationVersementComplexSearchModel dossierSearch = new DossierDeclarationVersementComplexSearchModel();
        dossierSearch.setWhereKey("declarationPonctuelle");

        dossierSearch.setForNumAffilie(numAffilie);
        dossierSearch.setForDateDebut(periodeDe);
        dossierSearch.setForDateFin(periodeA);
        dossierSearch.setForEtatPrestation(ALCSPrestation.ETAT_CO);
        dossierSearch = ALImplServiceLocator.getDossierDeclarationVersementComplexModelService().search(dossierSearch);

        return getDocuments(dossierSearch, periodeDe, periodeA, dateImpr, typeDocument,
                ALCSDeclarationVersement.DECLA_VERS_DEMANDE, textImpot);

    }

    @Override
    public JadePrintDocumentContainer getDeclarationsVersementDemande(String idDossier, String numAffilie,
            String periodeDe, String periodeA, String dateImpr, String typeDocument, Boolean textImpot)
            throws JadeApplicationException, JadePersistenceException {
        // contrôle des paramètres
        if (!JadeDateUtil.isGlobazDate(periodeDe)) {
            throw new ALDeclarationVersementException(
                    "DeclarationsVersementServiceImpl# getDeclarationsVersementDemande " + periodeDe
                            + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(periodeA)) {
            throw new ALDeclarationVersementException(
                    "DeclarationsVersementServiceImpl# getDeclarationsVersementDemande " + periodeA
                            + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(dateImpr)) {
            throw new ALDeclarationVersementException(
                    "DeclarationsVersementServiceImpl# getDeclarationsVersementDemande " + dateImpr
                            + " is not a valid globaz's date ");

        }
        if (JadeStringUtil.isBlankOrZero(idDossier) && JadeStringUtil.isBlankOrZero(numAffilie)) {
            throw new ALDeclarationVersementException(
                    "DeclarationsVersementServiceImpl# getDeclarationsVersementDemande numAffilie is empty and numDossier is too empty, "
                            + "one field must be full for this type of declaration (sur demande");
        }
        // if (!JadeStringUtil.isBlankOrZero(idDossier) && !JadeStringUtil.isBlankOrZero(numAffilie)) {
        // throw new ALDeclarationVersementException(
        // "DeclarationsVersementServiceImpl# getDeclarationsVersementDemande numAffilie is full and numDossier is full too, "
        // + "one field must be full for this type of declaration (sur demande");
        // }
        if (!JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_DET, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_GLOB, false)) {
            throw new ALDeclarationVersementException(
                    "DeclarationsVersementServiceImpl# getDeclarationsVersementDossier " + typeDocument
                            + " is not a valid document's type");
        }

        DossierDeclarationVersementComplexSearchModel dossierSearch = new DossierDeclarationVersementComplexSearchModel();
        dossierSearch.setWhereKey("declarationPonctuelle");

        if (!JadeStringUtil.isBlankOrZero(idDossier)) {
            dossierSearch.setForIdDossier(idDossier);
        } else if (!JadeStringUtil.isBlankOrZero(numAffilie)) {
            dossierSearch.setForNumAffilie(numAffilie);
        }
        dossierSearch.setForDateDebut(periodeDe);
        dossierSearch.setForDateFin(periodeA);

        dossierSearch.setForEtatPrestation(ALCSPrestation.ETAT_CO);

        dossierSearch = ALImplServiceLocator.getDossierDeclarationVersementComplexModelService().search(dossierSearch);

        return getDocuments(dossierSearch, periodeDe, periodeA, dateImpr, typeDocument,
                ALCSDeclarationVersement.DECLA_VERS_DEMANDE, textImpot);
    }

    @Override
    public JadePrintDocumentContainer getDeclarationsVersementDirect(String periodeDe, String periodeA,
            String dateImpr, String typeDocument, String numDossier, String numAffilie, Boolean textImpot)
            throws JadeApplicationException, JadePersistenceException {

        // contrôle des paramètres

        if (!JadeDateUtil.isGlobazDate(periodeDe)) {
            throw new ALDeclarationVersementException(
                    "DeclarationsVersementServiceImpl# getDeclarationsVersementDirect " + periodeDe
                            + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(periodeA)) {
            throw new ALDeclarationVersementException(
                    "DeclarationsVersementServiceImpl# getDeclarationsVersementDirect " + periodeA
                            + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(dateImpr)) {
            throw new ALDeclarationVersementException(
                    "DeclarationsVersementServiceImpl# getDeclarationsVersementDirect " + dateImpr
                            + " is not a valid globaz's date ");
        }
        if (!JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_DET, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_GLOB, false)) {
            throw new ALDeclarationVersementException(
                    "DeclarationsVersementServiceImpl# getDeclarationsVersementDirect " + typeDocument
                            + " is not a valid document's type");
        }

        DossierDeclarationVersementComplexSearchModel dossierSearch = new DossierDeclarationVersementComplexSearchModel();
        dossierSearch.setForImpotSource(Boolean.FALSE);
        // si num dossier ou num Affilie
        if (!JadeStringUtil.isBlankOrZero(numDossier)) {
            dossierSearch.setForIdDossier(numDossier);
        } else if (!JadeStringUtil.isBlankOrZero(numAffilie)) {
            dossierSearch.setForNumAffilie(numAffilie);
        }

        dossierSearch.setForNotIdTiersBeneficiaire("0");
        dossierSearch.setForDateDebut(periodeDe);
        dossierSearch.setForDateFin(periodeA);
        dossierSearch.setForTypeBonification(ALCSPrestation.BONI_DIRECT);
        dossierSearch.setForEtatPrestation(ALCSPrestation.ETAT_CO);

        dossierSearch = ALImplServiceLocator.getDossierDeclarationVersementComplexModelService().search(dossierSearch);

        return getDocuments(dossierSearch, periodeDe, periodeA, dateImpr, typeDocument,
                ALCSDeclarationVersement.DECLA_VERS_DIR_NON_IMP_SOUR, textImpot);
    }

    /**
     * Méthode qui permet de générerer la création de document en faisant appel au service adéquat
     * 
     * @param dossierSearch
     *            selon DossierDeclarationVersementComplexSearchModel
     * @param periodeDe
     *            début de la période
     * @param periodeA
     *            Fin de la période
     * @param dateImpr
     *            date du document
     * @param typeDocument
     *            type de déclaration de versement (global ou détaillé)
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private JadePrintDocumentContainer getDocuments(DossierDeclarationVersementComplexSearchModel dossierSearch,
            String periodeDe, String periodeA, String dateImpr, String typeDocument, String typeDeclaration,
            Boolean textImpot) throws JadeApplicationException, JadePersistenceException {
        // contrôle des paramètres
        if (dossierSearch == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationsVersementServiceImpl#getDocuments: dossierSearch is null");
        }

        if (!JadeDateUtil.isGlobazDate(periodeDe)) {
            throw new ALDeclarationVersementException("DeclarationsVersementServiceImpl#getDocuments: " + periodeDe
                    + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(periodeA)) {
            throw new ALDeclarationVersementException("DeclarationsVersementServiceImpl#getDocuments:  " + periodeA
                    + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(dateImpr)) {
            throw new ALDeclarationVersementException("DeclarationsVersementServiceImpl#getDocuments:  " + dateImpr
                    + " is not a valid globaz's date ");
        }
        if (!JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_DET, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_GLOB, false)) {
            throw new ALDeclarationVersementException("DeclarationsVersementServiceImpl# getDocuments " + typeDocument
                    + " is not a valid document's type");
        }

        JadePrintDocumentContainer container = new JadePrintDocumentContainer();

        JadePrintDocumentContainer containerCopie = new JadePrintDocumentContainer();

        // logger pour les déclarations de versement
        ProtocoleLogger logger = new ProtocoleLogger();

        DocumentDataContainer docContainerCopie = new DocumentDataContainer();

        /*
         * nombre d'attestation
         */

        int compteurAttestation = 0;
        for (int i = 0; i < dossierSearch.getSize(); i++) {
            try {

                DossierDeclarationVersementComplexModel dossier = (DossierDeclarationVersementComplexModel) dossierSearch
                        .getSearchResults()[i];

                DossierComplexModel dossierComplex = ALServiceLocator.getDossierComplexModelService().read(
                        dossier.getIdDossier());
                // langue pour les documents
                String langueDocument = null;
                // Si langue reprise langue affilié
                if (dossierComplex.getAllocataireComplexModel().getAllocataireModel().getLangueAffilie()) {
                    langueDocument = ALServiceLocator.getLangueAllocAffilieService().langueTiersAffilie(
                            dossier.getNumeroAffilie());
                }// si reprise langue allocataire
                else {
                    langueDocument = ALServiceLocator.getLangueAllocAffilieService().langueTiersAlloc(
                            dossier.getIdTiersAllocataire(), dossier.getNumeroAffilie());
                }

                // appel du provider pour faire appel au bon service de génération
                // des attestations
                DeclarationVersementService decVer = DeclarationVersementProvider.getDeclarationVersementService(
                        dossier, typeDocument, typeDeclaration);

                // DeclarationVersementDetailleDossierTiers
                ArrayList<DocumentDataContainer> listdocContainer = decVer.getDeclarationVersement(
                        dossier.getIdDossier(), periodeDe, periodeA, dateImpr, logger, langueDocument, textImpot);
                // ajout du document au container
                // compteur de document
                compteurAttestation++;
                for (DocumentDataContainer docContainer : listdocContainer) {
                    container.addDocument(docContainer.getDocument(), null);
                    logger = docContainer.getProtocoleLogger();

                }
            } catch (Exception e) {

                DossierDeclarationVersementComplexModel dossierInError = (DossierDeclarationVersementComplexModel) dossierSearch
                        .getSearchResults()[i];

                String langueDocument = ALServiceLocator.getLangueAllocAffilieService().langueTiersAffilie(
                        ALServiceLocator.getDossierModelService().read(dossierInError.getIdDossier())
                                .getNumeroAffilie());

                // TODO:logger proprement
                logger.getErrorsLogger(dossierInError.getIdDossier(), "Dossier #" + dossierInError).addMessage(
                        new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR,
                                DeclarationVersementGlobalDirectServiceImpl.class.getName(), dossierInError
                                        .getIdDossier()
                                        + JadeI18n.getInstance().getMessage(langueDocument,
                                                "al.protocoles.erreurs.declarationVersement.dossierId", null)
                                        + " "
                                        + e.getMessage()));

                // normalement on a du logger l'erreur dans le protocole
            }

        }
        // ajouter le conteneur copie au conteneur des déclarations originales
        containerCopie.copyDocsTo(container);

        // liste des paramètres pour protocole Declaration versement
        HashMap<String, String> params = new HashMap<String, String>();
        // infos liées au protocole de dclaration de versement
        // passage pour l'instant en dur, à voir par la suite
        params.put(ALConstProtocoles.INFO_PASSAGE, "12345");
        params.put(ALConstProtocoles.INFO_PROCESSUS, "al.protocoles.declarationVersement.info.processus.val");
        params.put(ALConstProtocoles.INFO_TRAITEMENT,
                "al.protocoles.declarationVersement.info.traitement.versementDirect.val");
        params.put(ALConstProtocoles.INFO_PERIODE, periodeDe + "-" + periodeA);

        docContainerCopie.setProtocoleLogger(logger);

        container.addDocument(
                ALImplServiceLocator.getProtocoleInfoDeclarationVersementService().getDocumentData(
                        docContainerCopie.getProtocoleLogger(), params), null);

        return container;

        // TODO suuprimer
        // JadePrintDocumentContainer infoLogger = new JadePrintDocumentContainer();
        // infoLogger.addDocument(
        // ALImplServiceLocator.getProtocoleInfoDeclarationVersementService().getDocumentData(
        // docContainerCopie.getProtocoleLogger(), params), null);
        // return infoLogger;

    }
}
