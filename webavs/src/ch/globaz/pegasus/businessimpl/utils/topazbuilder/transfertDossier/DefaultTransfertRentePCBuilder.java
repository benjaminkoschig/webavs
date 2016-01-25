package ch.globaz.pegasus.businessimpl.utils.topazbuilder.transfertDossier;

import globaz.babel.api.ICTDocument;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRStringUtils;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import ch.globaz.babel.business.services.BabelServiceLocator;
import ch.globaz.common.business.language.LanguageResolver;
import ch.globaz.common.business.models.CTDocumentImpl;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.exceptions.models.transfertdossier.TransfertDossierException;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PegasusUtil;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.AbstractPegasusBuilder;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.TiersService;
import ch.globaz.pyxis.businessimpl.service.TiersServiceImpl;

public class DefaultTransfertRentePCBuilder extends AbstractPegasusBuilder {

    private ICTDocument babelDoc = null;
    private String dateTransfert = null;
    private String idCaisseAgence = null;
    private String idDemandePC = null;
    private Boolean isCaisseAcceptant = null;
    private String noCaisseAgence = null;
    private PersonneEtendueComplexModel requerant = null;

    public JadePrintDocumentContainer build(JadePublishDocumentInfo pubInfo, String idDemandePC, String email,
            String idGestionnaire, String idAgence, String noAgence, String dateTransfert, String dateAnnonce)
            throws TransfertDossierException {

        idCaisseAgence = idAgence;
        this.idDemandePC = idDemandePC;
        noCaisseAgence = noAgence;
        JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

        loadDBEntity();
        // crée document
        String noDocument = isCaisseAcceptant ? IPRConstantesExternes.PC_REF_INFOROM_TRANSFERT_RENTE_CAISSE_ACCEPTANT
                : IPRConstantesExternes.PC_REF_INFOROM_TRANSFERT_RENTE_CAISSE_REFUSANT;

        allDoc = new SingleTransfertRentePCBuilder().build(babelDoc, allDoc, idGestionnaire, requerant, idAgence,
                isCaisseAcceptant, dateTransfert, dateAnnonce);

        String title = BSessionUtil.getSessionFromThreadContext().getLabel("PROCESS_DEMANDE_TRANSFERT_DE_RENTE");
        String nomPrenomTiers = PegasusUtil.formatNomPrenom(requerant.getTiers());
        title = PRStringUtils.replaceString(title, "{0}", nomPrenomTiers);

        pubInfo.setOwnerEmail(email);
        pubInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, email);
        pubInfo.setArchiveDocument(false);
        pubInfo.setPublishDocument(true);
        pubInfo.setDocumentType(noDocument);
        pubInfo.setDocumentTypeNumber(noDocument);
        pubInfo.setDocumentDate(JadeDateUtil.getFormattedDate(new Date()));

        pubInfo.setDocumentTitle(title);
        pubInfo.setDocumentSubject(title);

        allDoc.setMergedDocDestination(pubInfo);

        return allDoc;
    }

    private boolean determineIsCaisseAcceptant() throws TransfertDossierException {
        try {
            List listIdRefusant = Arrays.asList((BSessionUtil.getSessionFromThreadContext().getApplication()
                    .getProperty(EPCProperties.LIST_ID_CAISSE_REFUSANT.getProperty()).split(",")));

            return listIdRefusant.indexOf(noCaisseAgence) == -1;

        } catch (Exception e) {
            throw new TransfertDossierException("Couldn't determine if caisse was acceptant!", e);
        }
    }

    private void loadDBEntity() throws TransfertDossierException {

        try {

            Demande demande = PegasusServiceLocator.getDemandeService().read(idDemandePC);
            requerant = demande.getDossier().getDemandePrestation().getPersonneEtendue();
            isCaisseAcceptant = determineIsCaisseAcceptant();

        } catch (JadePersistenceException e) {
            throw new TransfertDossierException("A persistence error happened!", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TransfertDossierException("Service not available!", e);
        } catch (JadeApplicationException e) {
            throw new TransfertDossierException("A Jade error happened!", e);
        }

        // Chargement Babel
        try {

            // on recherche la caisse à qui on envoie le document
            TiersService ts = new TiersServiceImpl();
            TiersSimpleModel tiersCaisse = ts.read(idCaisseAgence);

            Langues langueCaisse = LanguageResolver.resolveISOCode(tiersCaisse.getLangue());
            Map<Langues, CTDocumentImpl> documentsBabel = BabelServiceLocator.getPCCatalogueTexteService()
                    .searchForTransfertRente();
            babelDoc = documentsBabel.get(langueCaisse);
        } catch (Exception e) {
            throw new TransfertDossierException("Error while loading catalogue Babel!", e);
        }

    }

}
