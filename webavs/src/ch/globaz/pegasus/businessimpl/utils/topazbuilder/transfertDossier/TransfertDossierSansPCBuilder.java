package ch.globaz.pegasus.businessimpl.utils.topazbuilder.transfertDossier;

import globaz.babel.api.ICTDocument;
import globaz.globall.db.BSessionUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import java.util.Map;
import ch.globaz.babel.business.services.BabelServiceLocator;
import ch.globaz.common.business.models.CTDocumentImpl;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.constantes.transfertdossier.TransfertDossierBuilderType;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.transfertdossier.TransfertDossierException;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PegasusUtil;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class TransfertDossierSansPCBuilder extends TransfertDossierAbstractBuilder {

    private ICTDocument babelDoc = null;
    private Map<Langues, CTDocumentImpl> babelPageGardeDocuments = null;
    private String dateSurDocument = null;
    private String dateTransfert = null;

    @Override
    public JadePrintDocumentContainer build(JadePublishDocumentInfo pubInfo) throws TransfertDossierException {

        JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

        loadDBEntity();
        // crée document
        allDoc = new SingleTransfertSansPCBuilder().build(babelDoc, babelPageGardeDocuments, allDoc, idGestionnaire,
                requerant, derniereLocalite, idNouvelleCaisse, annexes, copies, dateSurDocument);

        String title = "Demande de transfert (" + PegasusUtil.formatNomPrenom(requerant.getTiers()) + ")";

        preparePubInfo(pubInfo);
        pubInfo.setDocumentTitle(title);
        pubInfo.setDocumentSubject(title);

        allDoc.setMergedDocDestination(pubInfo);

        return allDoc;
    }

    public String getDateSurDocument() {
        return dateSurDocument;
    }

    public String getDateTransfert() {
        return dateTransfert;
    }

    private void loadDBEntity() throws TransfertDossierException {

        try {

            Demande demande = PegasusServiceLocator.getDemandeService().read(idDemandePC);
            requerant = demande.getDossier().getDemandePrestation().getPersonneEtendue();

            // recherche localité
            derniereLocalite = TIBusinessServiceLocator.getAdresseService().readLocalite(idDernierDomicileLegal);

        } catch (DroitException e) {
            throw new TransfertDossierException("Error while loading data for droit!", e);
        } catch (JadePersistenceException e) {
            throw new TransfertDossierException("A persistence error happened!", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TransfertDossierException("Service not available!", e);
        } catch (JadeApplicationException e) {
            throw new TransfertDossierException("A Jade error happened!", e);
        }

        // Chargement BAbel
        try {
            Map<Langues, CTDocumentImpl> documentsBabel;
            documentsBabel = BabelServiceLocator.getPCCatalogueTexteService().searchForTransfertDossierPC(
                    TransfertDossierBuilderType.DEMANDE_INITIALE_SANS_PC);

            Langues langueTiers = Langues.getLangueDepuisCodeIso(requerant.getTiers().getLangue());

            if (null == langueTiers) {
                langueTiers = Langues.getLangueDepuisCodeIso(BSessionUtil.getSessionFromThreadContext().getUserInfo()
                        .getLanguage());
            }
            babelDoc = documentsBabel.get(langueTiers);

            babelPageGardeDocuments = BabelServiceLocator.getPCCatalogueTexteService().searchForPageGardeCopiePC();

        } catch (Exception e) {
            throw new TransfertDossierException("Error while loading catalogue Babel!", e);
        }

    }

    @Override
    public void loadParameters(Map<String, String> parameters, List<String> annexes, List<String> copies) {
        super.loadParameters(parameters, annexes, copies);
        dateTransfert = parameters.get(TransfertDossierAbstractBuilder.DATE_TRANSFERT);
        dateSurDocument = parameters.get(TransfertDossierAbstractBuilder.DATE_SUR_DOCUMENT);
    }

    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    public void setDateTransfert(String dateTransfert) {
        this.dateTransfert = dateTransfert;
    }

}
