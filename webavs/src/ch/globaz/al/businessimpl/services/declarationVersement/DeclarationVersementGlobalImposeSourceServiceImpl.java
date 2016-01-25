package ch.globaz.al.businessimpl.services.declarationVersement;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.HashSet;
import ch.globaz.al.business.constantes.ALCSDeclarationVersement;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.declarationVersement.ALDeclarationVersementException;
import ch.globaz.al.business.exceptions.document.ALDocumentAddressException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleSearchComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.declarationVersement.DeclarationVersementGlobalImposeSourceService;
import ch.globaz.al.business.services.documents.DocumentDataContainer;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Implémentation du service pour la création d'attestation d'un dossier imposé à la source
 * 
 * @author PTA
 * 
 */
public class DeclarationVersementGlobalImposeSourceServiceImpl extends DeclarationVersementGlobalAbstractServiceImpl
        implements DeclarationVersementGlobalImposeSourceService {

    @Override
    public ArrayList<DocumentDataContainer> getDeclarationVersement(String idDossier, String dateDebut, String dateFin,
            String dateImpression, ProtocoleLogger logger, String langueDocument, Boolean textImpot)
            throws JadePersistenceException, JadeApplicationException {
        // contrôle des paramètres

        if (idDossier == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementImposeSourceServiceImpl# getDeclarationVersement: idDossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateDebut)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementImposeSourceServiceImpl# getDeclarationVersement:  " + dateDebut
                            + " is not a valid globaz's date ");
        }

        if (!JadeDateUtil.isGlobazDate(dateFin)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementImposeSourceServiceImpl# getDeclarationVersement:  " + dateFin
                            + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(dateImpression)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementImposeSourceServiceImpl# getDeclarationVersement:  " + dateImpression
                            + " is not a valid globaz's date ");
        }
        if (logger == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementImposeSourceServiceImpl# getDeclarationVersement:  logger is null");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementImposeSourceServiceImpl# getDeclarationVersement: language  " + langueDocument
                            + " is not  valid ");
        }

        // Dossier complex
        DossierComplexModel dossier = ALServiceLocator.getDossierComplexModelService().read(idDossier);

        HashSet<String> typeBonif = new HashSet<String>();
        typeBonif.add(ALCSPrestation.BONI_DIRECT);
        typeBonif.add(ALCSPrestation.BONI_RESTITUTION);

        DeclarationVersementDetailleSearchComplexModel detPrestaSearch = getPrestationDetailles(idDossier, dateDebut,
                dateFin, typeBonif, langueDocument);

        ArrayList<DeclarationVersementDetailleDossierTiers> listPrestDossier = this.loadDataDocument(detPrestaSearch,
                dossier);
        ArrayList<DocumentDataContainer> listDocumentDataContainer = new ArrayList<DocumentDataContainer>();

        for (int i = 0; i < listPrestDossier.size(); i++) {
            // initialisation du declarationContainer
            DocumentDataContainer declaVerseContainer = new DocumentDataContainer();
            DocumentData document = new DocumentData();

            document = initDocument();
            String activiteAllocataire = ALServiceLocator.getDossierModelService().read(idDossier)
                    .getActiviteAllocataire();
            this.setIdEntete(document, activiteAllocataire, ALConstDocument.DOCUMENT_DECLARATION_VERSEMENT,
                    langueDocument);
            this.setIdSignature(document, activiteAllocataire, ALConstDocument.DOCUMENT_DECLARATION_VERSEMENT,
                    langueDocument);

            // charger les données liés aux prestations
            setTable(document, idDossier, dossier.getDossierModel().getIdTiersBeneficiaire(), dossier
                    .getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(), dateDebut, dateFin,
                    typeBonif, listPrestDossier.get(i).getListPrestatiion(),
                    ALCSDeclarationVersement.DECLA_VERS_DIR_IMP_SOURCE, langueDocument);

            // this.setTable(document, idDossier, dossier.getDossierModel().getIdTiersBeneficiaire(), dossier
            // .getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(), dateDebut, dateFin,
            // typeBonif, ALConstDeclarationVersement.DECLA_VERSE_IMP_SOURCE, langueDocument);

            // sette les infos du dossier
            setInfos(document, dossier, dateDebut, dateFin, langueDocument);

            // recherche l'identifiant du tiers affillié
            String idTiersAffilie = AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(
                    dossier.getDossierModel().getNumeroAffilie());
            try {
                this.addDateAdresse(document, dateImpression, idTiersAffilie, langueDocument, dossier.getDossierModel()
                        .getNumeroAffilie());
            } catch (ALDocumentAddressException e) {
                logger.getErrorsLogger(idDossier, "Dossier #" + idDossier).addMessage(
                        new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR,
                                DeclarationVersementGlobalDirectServiceImpl.class.getName(), e.getMessage()));

                declaVerseContainer.setProtocoleLogger(logger);
                listDocumentDataContainer.add(declaVerseContainer);
                return listDocumentDataContainer;
            }

            if (textImpot) {
                document.addData("declaration_versement_destination_text",
                        this.getText("al.declarationVersement.prestation.destinationImpot", langueDocument));
            }

            // ajouter la collection vide des copie (pas de copie pour cet type)
            Collection table = new Collection("declaration_Avis_A");
            document.add(table);

            logger.getInfosLogger(idDossier, "Dossier #" + idDossier).addMessage(
                    new JadeBusinessMessage(JadeBusinessMessageLevels.INFO,
                            DeclarationVersementGlobalDirectServiceImpl.class.getName(), "document généré"));
            // ajout du logger
            declaVerseContainer.setProtocoleLogger(logger);
            // ajout du document
            declaVerseContainer.setDocument(document);
            listDocumentDataContainer.add(declaVerseContainer);
        }

        return listDocumentDataContainer;
    }

    @Override
    protected DeclarationVersementDetailleSearchComplexModel getPrestationDetailles(String idDossier, String dateDebut,
            String dateFin, HashSet<String> typeBoni, String langueDocument) throws JadeApplicationException,
            JadePersistenceException {

        // contrôle des paramètres
        if (idDossier == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementDetailleAbstractServiceImpl# getDetailsPrestations: idDossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateDebut)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementDetailleAbstractServiceImpl# getDetailsPrestations:  " + dateDebut
                            + " is not a valid globaz's date ");
        }

        if (!JadeDateUtil.isGlobazDate(dateFin)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementDetailleAbstractServiceImpl# getDetailsPrestations:  " + dateFin
                            + " is not a valid globaz's date ");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDeclarationVersementException(
                    "declarationVersementDetailleAbstractServiceImpl# getDetailsPrestations: language  "
                            + langueDocument + " is not  valid ");
        }

        DeclarationVersementDetailleSearchComplexModel declarationsDetaille = new DeclarationVersementDetailleSearchComplexModel();

        declarationsDetaille.setInBonification(typeBoni);
        declarationsDetaille.setForDateDebut(dateDebut);
        declarationsDetaille.setForDateFin(dateFin);
        declarationsDetaille.setForEtat(ALCSPrestation.ETAT_CO);
        declarationsDetaille.setForIdDossier(idDossier);
        declarationsDetaille.setForTiers(ALServiceLocator.getDossierComplexModelService().read(idDossier)
                .getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire());
        declarationsDetaille.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        declarationsDetaille.setOrderKey("tiersBeneficiaire");
        declarationsDetaille = ALImplServiceLocator.getDeclarationVersementDetailleComplexModelService().search(
                declarationsDetaille);

        return declarationsDetaille;
    }
}
