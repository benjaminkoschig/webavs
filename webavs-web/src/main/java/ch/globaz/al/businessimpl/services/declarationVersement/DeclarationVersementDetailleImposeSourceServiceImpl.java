/**
 * 
 */
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
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.declarationVersement.ALDeclarationVersementException;
import ch.globaz.al.business.exceptions.document.ALDocumentAddressException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleSearchComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.declarationVersement.DeclarationVersementDetailleImposeSourceService;
import ch.globaz.al.business.services.documents.DocumentDataContainer;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * @author pta
 * 
 */
public class DeclarationVersementDetailleImposeSourceServiceImpl extends
        DeclarationVersementDetailleAbstractServiceImpl implements DeclarationVersementDetailleImposeSourceService {

    @Override
    public ArrayList<DocumentDataContainer> getDeclarationVersement(String idDossier, String dateDebut, String dateFin,
            String dateImpression, ProtocoleLogger logger, String langueDocument, Boolean textImpot)
            throws JadePersistenceException, JadeApplicationException {
        // contr?le des param?tres
        if (idDossier == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementGlobalDirectServiceImpl# getDeclarationVersement: idDossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateDebut)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementGlobalDirectServiceImpl# getDeclarationVersement:  " + dateDebut
                            + " is not a valid globaz's date ");
        }

        if (!JadeDateUtil.isGlobazDate(dateFin)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementGlobalDirectServiceImpl# getDeclarationVersement:  " + dateFin
                            + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(dateImpression)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementGlobalDirectServiceImpl# getDeclarationVersement:  " + dateImpression
                            + " is not a valid globaz's date ");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementGlobalDirectServiceImpl# getDeclarationVersement: language  " + langueDocument
                            + " is not  valid ");
        }
        if (logger == null) {
            throw new ALDeclarationVersementException(
                    "	DeclarationVersementGlobalDirectServiceImpl# getDeclarationVersement: logger is null");
        }

        // Dossier complex
        DossierComplexModel dossier = ALServiceLocator.getDossierComplexModelService().read(idDossier);

        // type de boni
        HashSet<String> typeBoni = new HashSet<String>();
        typeBoni.add(ALCSPrestation.BONI_DIRECT);
        typeBoni.add(ALCSPrestation.BONI_RESTITUTION);

        DeclarationVersementDetailleSearchComplexModel detPrestaSearch = getPrestationDetailles(idDossier, dateDebut,
                dateFin, typeBoni, langueDocument);

        ArrayList<DeclarationVersementDetailleDossierTiers> listPrestDossier = loadDataDocument(detPrestaSearch,
                dossier);

        ArrayList<DocumentDataContainer> listDocumentDataContainer = new ArrayList<DocumentDataContainer>();
        for (int i = 0; i < listPrestDossier.size(); i++) {
            // initialiser conteneur
            DocumentDataContainer declaVerseContainer = new DocumentDataContainer();
            DocumentData document = new DocumentData();

            document = initDocument();
            String activiteAllocataire = ALServiceLocator.getDossierModelService().read(idDossier)
                    .getActiviteAllocataire();

            this.setIdEntete(document, activiteAllocataire, ALConstDocument.DOCUMENT_DECLARATION_VERSEMENT,
                    langueDocument);
            this.setIdSignature(document, activiteAllocataire, ALConstDocument.DOCUMENT_DECLARATION_VERSEMENT,
                    langueDocument);
            setTable(document, idDossier, dateDebut, dateFin, typeBoni, /* listDeclaratPrestation */
                    listPrestDossier.get(i).getListPrestatiion(), langueDocument);

            // texte destination prestations
            document.addData("declaration_versement_a",
                    this.getText("al.declarationVersement.prestation.alloue", langueDocument));

            try {
                // adresse de l'affili? pour les impos?s ? la source
                this.addDateAdresse(document, dateImpression, AFBusinessServiceLocator.getAffiliationService()
                        .findIdTiersForNumeroAffilie(dossier.getDossierModel().getNumeroAffilie()), langueDocument,
                        dossier.getDossierModel().getNumeroAffilie());

            } catch (ALDocumentAddressException e) {
                logger.getErrorsLogger(idDossier, "Dossier #" + idDossier).addMessage(
                        new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR,
                                DeclarationVersementGlobalDirectServiceImpl.class.getName(), e.getMessage()));

                declaVerseContainer.setProtocoleLogger(logger);

                listDocumentDataContainer.add(declaVerseContainer);
                return listDocumentDataContainer;
            }
            // infos g?n?rales relatives au dossier
            setInfos(document, dossier, dateDebut, dateFin, langueDocument);

            if (textImpot) {
                document.addData("declaration_versement_destination_text",
                        this.getText("al.declarationVersement.prestation.destinationImpot", langueDocument));
            }

            logger.getInfosLogger(idDossier, "Dossier #" + idDossier).addMessage(
                    new JadeBusinessMessage(JadeBusinessMessageLevels.INFO,
                            DeclarationVersementGlobalDirectServiceImpl.class.getName(), "document g?n?r?"));

            // ajoute le documentData au
            declaVerseContainer.setDocument(document);
            // ajoute le protocole logger
            declaVerseContainer.setProtocoleLogger(logger);

            listDocumentDataContainer.add(declaVerseContainer);
        }
        return listDocumentDataContainer;
    }

    @Override
    protected DeclarationVersementDetailleSearchComplexModel getPrestationDetailles(String idDossier, String dateDebut,
            String dateFin, HashSet<String> typeBoni, String langueDocument) throws JadeApplicationException,
            JadePersistenceException {

        // contr?le des param?tres
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
