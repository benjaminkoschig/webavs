package ch.globaz.al.businessimpl.services.courrier;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import java.util.ArrayList;
import java.util.Date;
import ch.globaz.al.business.constantes.ALCSCopie;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.copies.ALCopieBusinessException;
import ch.globaz.al.business.exceptions.document.ALDocumentAddressException;
import ch.globaz.al.business.exceptions.document.ALDocumentException;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.courrier.LettreAccompagnementCopieService;
import ch.globaz.al.business.services.documents.DocumentDataContainer;
import ch.globaz.al.businessimpl.documents.AbstractDocument;
import ch.globaz.al.businessimpl.services.declarationVersement.DeclarationVersementGlobalDirectServiceImpl;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Implémentation des services liées aux lettres d'accompagnement des copies de document
 * 
 * @author PTA
 * 
 */

public class LettreAccompagnementCopieServiceImpl extends AbstractDocument implements LettreAccompagnementCopieService {

    @Override
    public DocumentDataContainer loadData(ProtocoleLogger logger, String IdTiersDestinataireCopie, String typeCopie,
            String idDossier, String langueDocument) throws JadePersistenceException, JadeApplicationException {
        return loadData(logger, IdTiersDestinataireCopie, typeCopie, idDossier, langueDocument, null);
    }

    @Override
    public DocumentDataContainer loadData(ProtocoleLogger logger, String IdTiersDestinataireCopie, String typeCopie,
            String idDossier, String langueDocument, String dateImpression) throws JadePersistenceException,
            JadeApplicationException {

        // vérification des paramètres
        if (JadeStringUtil.isEmpty(IdTiersDestinataireCopie)) {
            throw new ALDocumentException("LettreAccompagnementCopie#loadData: idTiersDestinataireCopie is null");
        }
        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSCopie.GROUP_COPIE_TYPE, typeCopie)) {
                throw new ALDocumentException("LettreAccompagnementCopie#loadData: typeCopie '" + typeCopie
                        + "' is not valid");
            }
        } catch (Exception e) {
            throw new ALDocumentException("LettreAccompagnementCopie#loadData: typeCopie: " + typeCopie
                    + " is not a valid code");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALCopieBusinessException("LettreAccompagnementCopieServiceImpl# loadData: language  "
                    + langueDocument + " is not  valid ");
        }

        DocumentDataContainer docDataContainer = new DocumentDataContainer();

        DocumentData lettreAccompagnement = new DocumentData();

        setIdDocument(lettreAccompagnement);

        // chargement de l'entête de la caisse AF
        this.setIdEntete(lettreAccompagnement, ALServiceLocator.getDossierModelService().read(idDossier)
                .getActiviteAllocataire(), ALConstDocument.LETTRE_ACCOMPAGNEMENT, langueDocument);

        // chargement de la date courante pour ajouter l'adresse du tiers
        // destinataire de
        // la copie

        String idTiersAffilie = AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(
                ALServiceLocator.getDossierModelService().read(idDossier).getNumeroAffilie());
        try {
            if (JadeStringUtil.equals(idTiersAffilie, IdTiersDestinataireCopie, false)) {
                this.addDateAdresse(lettreAccompagnement,
                        dateImpression != null ? dateImpression : JadeDateUtil.getGlobazFormattedDate(new Date()),
                        IdTiersDestinataireCopie, langueDocument,
                        ALServiceLocator.getDossierModelService().read(idDossier).getNumeroAffilie());
            } else {
                this.addDateAdresse(lettreAccompagnement,
                        dateImpression != null ? dateImpression : JadeDateUtil.getGlobazFormattedDate(new Date()),
                        IdTiersDestinataireCopie, langueDocument, null);
            }
        } catch (ALDocumentAddressException e) {

            logger.getErrorsLogger(idDossier, "Dossier #" + idDossier).addMessage(
                    new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR,
                            DeclarationVersementGlobalDirectServiceImpl.class.getName(), IdTiersDestinataireCopie
                                    + this.getText("al.protocoles.erreurs.lettreAccompagnement.tiers.sansAdresse",
                                            langueDocument) + " " + e.getMessage()));

            docDataContainer.setProtocoleLogger(logger);
            throw e;
            // return docDataContainer;
        }

        // chargement du texte lié au type de copie avis échéance
        setTexteAccompagnement(lettreAccompagnement, typeCopie, langueDocument);

        // chargement du texte de salutations
        setSalutations(lettreAccompagnement, langueDocument);

        // chargement de la signature de la caisse
        // this.setIdSignature(lettreAccompagnement);
        this.setIdSignature(lettreAccompagnement, ALServiceLocator.getDossierModelService().read(idDossier)
                .getActiviteAllocataire(), ALConstDocument.LETTRE_ACCOMPAGNEMENT, langueDocument);

        // chargement du texte lié à annexe
        // pour lettre accompagnement liés à avis
        setTexteAnnexe(lettreAccompagnement, langueDocument);

        docDataContainer.setDocument(lettreAccompagnement);

        return docDataContainer;
    }

    @Override
    public ArrayList<DocumentData> loadDataLettresAccompagnement(ProtocoleLogger logger, DossierComplexModel dossier,
            ArrayList<String> listIdTiers, String langueDocument) throws JadePersistenceException,
            JadeApplicationException {

        // vérification des paramètres
        if (dossier == null) {
            throw new ALCopieBusinessException(
                    "LettreAccompagnementCopieServiceImpl#loadDataLettresAccompagnement: dossier is null");
        }
        if (listIdTiers == null) {
            throw new ALCopieBusinessException(
                    "LettreAccompagnementCopieServiceImpl#loadDataLettresAccompagnement: listIdTiers is null");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALCopieBusinessException(
                    "LettreAccompagnementCopieServiceImpl# loadDataLettresAccompagnement: language  " + langueDocument
                            + " is not  valid ");
        }

        ArrayList<DocumentData> documentList = new ArrayList<DocumentData>();

        for (int i = 2; i <= listIdTiers.size(); i++) {
            // Contient les données à imprimer sur le document
            DocumentDataContainer docDataContainer = new DocumentDataContainer();

            // ID du tiers destinataire qui recevra une page de garde
            String idTiersDestinataire = listIdTiers.get(i - 1);

            docDataContainer = ALServiceLocator.getLettreAccompagnementCopieService().loadData(logger,
                    idTiersDestinataire, ALCSCopie.TYPE_DECISION, dossier.getDossierModel().getIdDossier(),
                    langueDocument);

            documentList.add(docDataContainer.getDocument());
        }

        return documentList;
    }

    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException("LettreAccompagnementCopieServiceImpl#setIdDocument : document is null");
        }

        document.addData("AL_idDocument", "AL_lettreAccompagnement");
    }

    /**
     * méthode qui ajoute les salutations
     * 
     * @param lettreAccompagnement
     *            lettre d'accompagnement à traiter
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private void setSalutations(DocumentData lettreAccompagnement, String langueDocument)
            throws JadeApplicationException {
        lettreAccompagnement.addData("accomp_salutation_texte",
                this.getText("al.accompagnement.salutation.texte", langueDocument));
    }

    /**
     * méthode qui ajoute le texte à la lettre d'accompagnement liées aux avis d'échéance
     * 
     * @param lettreAccompagnement
     *            lettre d'accompagnement
     * @param typeCopie
     *            type de copie
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private void setTexteAccompagnement(DocumentData lettreAccompagnement, String typeCopie, String langueDocument)
            throws JadeApplicationException {
        // texte pour les echeances
        if (JadeStringUtil.equals(typeCopie, ALCSCopie.TYPE_ECHEANCE, false)) {
            lettreAccompagnement.addData("accomp_titre_texte",
                    this.getText("al.accompagnement.titreAvisEcheance.texte", langueDocument));
            lettreAccompagnement.addData("accomp_info_texte",
                    this.getText("al.accompagnement.infoAvisEcheance.texte", langueDocument));
            // texte pour les déclarations
        } else if (JadeStringUtil.equals(typeCopie, ALCSCopie.TYPE_DECLARATION, false)) {
            lettreAccompagnement.addData("accomp_titre_texte",
                    this.getText("al.accompagnement.titreInfoDeclarationVersement.texte", langueDocument));
            lettreAccompagnement.addData("accomp_info_texte",
                    this.getText("al.accompagnement.infoDeclarationVersement.texte", langueDocument));
            // texte pour les décisions

        } else if (JadeStringUtil.equals(typeCopie, ALCSCopie.TYPE_DECISION, false)) {

            lettreAccompagnement.addData("accomp_titre_texte",
                    this.getText("al.accompagnement.titreInfoDecision.texte", langueDocument));
            lettreAccompagnement.addData("accomp_info_texte",
                    this.getText("al.accompagnement.infoDecision.texte", langueDocument));

        } else {
            throw new ALDocumentException("LettreAccompagnementCopieServiceImpl# setTexteAccompagnment: " + typeCopie
                    + " doesn't exist");

        }

    }

    /**
     * Méthode qui ajoute le texte concernant les annexes
     * 
     * @param lettreAccompagnement
     *            lettre à traiter
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private void setTexteAnnexe(DocumentData lettreAccompagnement, String langueDocument)
            throws JadeApplicationException {

        lettreAccompagnement.addData("accomp_annexe", this.getText("al.accompagnement.annexe.label", langueDocument));
        lettreAccompagnement.addData("accomp_annexe_texte",
                this.getText("al.accompagnement.annexeMentionnee.texte", langueDocument));
    }

}
