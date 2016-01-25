package ch.globaz.al.businessimpl.services.declarationVersement;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import java.util.ArrayList;
import java.util.HashSet;
import ch.globaz.al.business.constantes.ALCSDeclarationVersement;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.declarationVersement.ALDeclarationVersementException;
import ch.globaz.al.business.exceptions.document.ALDocumentAddressException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleComplexModel;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleSearchComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.declarationVersement.DeclarationVersementDetailleFrontaliersNonActifService;
import ch.globaz.al.business.services.documents.DocumentDataContainer;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Implémentation des services pour les documents de déclarations de versement liéés aux frontaliers et aux non actifs
 * 
 * @author PTA
 * 
 */

public class DeclarationVersementDetailleFrontaliersNonActifServiceImpl extends
        DeclarationVersementDetailleAbstractServiceImpl implements
        DeclarationVersementDetailleFrontaliersNonActifService {

    /**
     * 
     * @param idDossier
     *            identifiant du dossier
     * @param dateDebut
     *            date de début
     * @param dateFin
     *            date de fin
     * @param dateImpression
     *            date du document
     * @return DocumentData
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    @Override
    public ArrayList<DocumentDataContainer> getDeclarationVersement(String idDossier, String dateDebut, String dateFin,
            String dateImpression, ProtocoleLogger logger, String langueDocument, Boolean textImpot)
            throws JadePersistenceException, JadeApplicationException {

        // contrôle des paramètres
        if (idDossier == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementFrontalierServiceImpl# getDeclarationVersement: idDossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateDebut)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementFrontalierServiceImpl# getDeclarationVersement:  " + dateDebut
                            + " is not a valid globaz's date ");
        }

        if (!JadeDateUtil.isGlobazDate(dateFin)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementFrontalierServiceImpl# getDeclarationVersement:  " + dateFin
                            + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(dateImpression)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementFrontalierServiceImpl# getDeclarationVersement:  " + dateImpression
                            + " is not a valid globaz's date ");
        }
        if (logger == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementFrontalierServiceImpl# getDeclarationVersement:  logger is null");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementGlobalDirectServiceImpl# getDeclarationVersement: language  " + langueDocument
                            + " is not  valid ");
        }
        // // initialisation du container
        // DocumentDataContainer declaVerseContainer = new DocumentDataContainer();

        // Lecture du dossier complex
        DossierComplexModel dossier = ALServiceLocator.getDossierComplexModelService().read(idDossier);

        // reprendre le type déclaration selon l'activité de l'allocataire
        String typeDeclaration = null;
        if (JadeStringUtil.equals(dossier.getDossierModel().getActiviteAllocataire(), ALCSDossier.ACTIVITE_NONACTIF,
                false)) {
            typeDeclaration = ALCSDeclarationVersement.DECLA_VERS_IND_NON_ACT;
        } else {
            typeDeclaration = ALCSDeclarationVersement.DECLA_VERS_IND_FRONT;
        }

        // type de boni
        HashSet<String> typeBoni = new HashSet<String>();
        typeBoni.add(ALCSPrestation.BONI_INDIRECT);
        typeBoni.add(ALCSPrestation.BONI_RESTITUTION);

        DeclarationVersementDetailleSearchComplexModel detPrestaSearch = getPrestationDetailles(idDossier, dateDebut,
                dateFin, typeBoni, langueDocument);

        ArrayList<DeclarationVersementDetailleDossierTiers> listPrestDossier = loadDataDocument(detPrestaSearch,
                dossier);
        ArrayList<DocumentDataContainer> listDocumentDataContainer = new ArrayList<DocumentDataContainer>();

        // parcourir les prestations pour les ajouter au documentData
        for (int i = 0; i < listPrestDossier.size(); i++) {
            // initialiser conteneur
            DocumentDataContainer declaVerseContainer = new DocumentDataContainer();
            DocumentData document = new DocumentData();

            // ArrayList temporaire
            DeclarationVersementDetailleComplexModel declarDetail = (DeclarationVersementDetailleComplexModel) detPrestaSearch
                    .getSearchResults()[i];

            document = initDocument();

            String activiteAllocataire = ALServiceLocator.getDossierModelService().read(idDossier)
                    .getActiviteAllocataire();

            this.setIdEntete(document, activiteAllocataire, ALConstDocument.DOCUMENT_DECLARATION_VERSEMENT,
                    langueDocument);
            this.setIdSignature(document, activiteAllocataire, ALConstDocument.DOCUMENT_DECLARATION_VERSEMENT,
                    langueDocument);
            setTable(document, idDossier, dateDebut, dateFin, typeBoni,/* listDeclaratPrestation */
                    listPrestDossier.get(i).getListPrestatiion(), langueDocument);

            // texte destination prestations
            document.addData("declaration_versement_a",
                    this.getText("al.declarationVersement.prestation.alloue", langueDocument));

            // adresse de l'affilié pour les frontaliers et les nons actifs
            // recherche l'identifiant du tiers affillié
            String idTiersAffilie = AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(
                    dossier.getDossierModel().getNumeroAffilie());

            try {
                this.addDateAdresse(document, dateImpression, idTiersAffilie, langueDocument, dossier.getDossierModel()
                        .getNumeroAffilie());
            }

            catch (ALDocumentAddressException e) {
                logger.getErrorsLogger(idDossier, "Dossier #" + idDossier).addMessage(
                        new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR,
                                DeclarationVersementGlobalDirectServiceImpl.class.getName(), e.getMessage()));

                declaVerseContainer.setProtocoleLogger(logger);
                return listDocumentDataContainer;
            }
            if (textImpot) {
                document.addData("declaration_versement_destination_text",
                        this.getText("al.declarationVersement.prestation.destinationImpot", langueDocument));
            }

            setInfos(document, dossier, dateDebut, dateFin, langueDocument);

            // ajouter la collection vide des copie (pas de copie pour ce type)
            // Collection table = new Collection("declaration_Avis_A");
            // document.add(table);

            logger.getInfosLogger(idDossier, "Dossier #" + idDossier).addMessage(
                    new JadeBusinessMessage(JadeBusinessMessageLevels.INFO,
                            DeclarationVersementGlobalDirectServiceImpl.class.getName(), "document généré"));
            // ajouter le document
            declaVerseContainer.setDocument(document);

            // ajouter au logger
            declaVerseContainer.setProtocoleLogger(logger);
            listDocumentDataContainer.add(declaVerseContainer);
        }
        return listDocumentDataContainer;
    }

}
