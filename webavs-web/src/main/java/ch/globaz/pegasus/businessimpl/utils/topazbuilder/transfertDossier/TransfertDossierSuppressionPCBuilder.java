package ch.globaz.pegasus.businessimpl.utils.topazbuilder.transfertDossier;

import globaz.docinfo.TIDocumentInfoHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRStringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.babel.business.services.BabelServiceLocator;
import ch.globaz.common.business.models.CTDocumentImpl;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.constantes.transfertdossier.TransfertDossierBuilderType;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.transfertdossier.TransfertDossierException;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PegasusUtil;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;

public class TransfertDossierSuppressionPCBuilder extends TransfertDossierAbstractBuilder {

    public static final String DATE_DECISION = "DATE_DECISION";
    public static final String DATE_SUPPRESSION = "DATE_SUPPRESSION";
    public static final String ID_DROIT = "ID_DROIT";
    public static final String MOTIF_CONTACT = "MOTIF_CONTACT";
    public static final String MOTIF_TRANSFERT = "MOTIF_TRANSFERT";
    public static final String NO_VERSION = "NO_VERSION";

    private Map<Langues, CTDocumentImpl> documentsBabelPageGardeCopiePC = new HashMap<Langues, CTDocumentImpl>();
    private Map<Langues, CTDocumentImpl> documentsBabelTransfertDossier = new HashMap<Langues, CTDocumentImpl>();
    private String dateDecision = null;
    private String dateSuppression = null;
    private AdresseTiersDetail derniereAdresse;
    private String idDroit = null;
    private String motifContact = null;
    private String motifTransfert;
    private List<PCAccordee> pcAccordees;

    @Override
    public JadePrintDocumentContainer build(JadePublishDocumentInfo pubInfo) throws TransfertDossierException,
            DecisionException {

        JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

        loadDBEntity();
        // crée document
        allDoc = new SingleTransfertSuppressionPCBuilder().build(documentsBabelTransfertDossier,
                documentsBabelPageGardeCopiePC, allDoc, idGestionnaire, requerant, idNouvelleCaisse, motifTransfert,
                dateDecision, dateSuppression, derniereAdresse, motifContact, annexes, copies, pcAccordees, idDroit);
        String title = getSession().getLabel("PROCESS_TRANSFERT_DOSSIER_SUPPRESSION");
        String nomPrenomTiers = PegasusUtil.formatNomPrenom(requerant.getTiers());
        title = PRStringUtils.replaceString(title, "{0}", nomPrenomTiers);

        preparePubInfo(pubInfo);
        pubInfo.setDocumentTitle(title);
        pubInfo.setDocumentSubject(title);
        loadPixisInfoForPubInfo(requerant.getTiers().getIdTiers(), pubInfo);
        allDoc.setMergedDocDestination(pubInfo);

        return allDoc;
    }

    /**
     * Chargement du container de transfert pour le remplissage de pixis
     * 
     * @param idTiers
     *            l'identifiant du tiers concerné pour les informations
     * @throws DecisionException
     *             si un problème survient lors du remplissge
     */
    private void loadPixisInfoForPubInfo(String idTiers, JadePublishDocumentInfo pubInfo) throws DecisionException {
        try {
            TIDocumentInfoHelper.fill(pubInfo, idTiers, getSession(), null, null, null);
        } catch (Exception e) {
            throw new DecisionException(
                    "An error happened during filling the document with pyxis informations for the following idTiers:["
                            + idTiers + "]", e);
        }
    }

    @Override
    protected void preparePubInfo(JadePublishDocumentInfo pubInfo) {
        super.preparePubInfo(pubInfo);
        pubInfo.setDocumentType(IPRConstantesExternes.PC_REF_INFOROM_DECISION_TRANSFERT_DOSSIER);
        pubInfo.setDocumentTypeNumber(IPRConstantesExternes.PC_REF_INFOROM_DECISION_TRANSFERT_DOSSIER);
        pubInfo.setArchiveDocument(true);
    }

    private void loadDBEntity() throws TransfertDossierException {

        try {

            Demande demande = PegasusServiceLocator.getDemandeService().read(idDemandePC);
            requerant = demande.getDossier().getDemandePrestation().getPersonneEtendue();

            derniereAdresse = PegasusUtil.getAdresseCascadeByType(requerant.getTiers().getIdTiers(),
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                    SingleTransfertSuppressionPCBuilder.listOrderAdresseTiers);

            // chargement plan de calcul
            // // chargement
            PCAccordeeSearch pcAccordeeSearchModel = new PCAccordeeSearch();
            pcAccordeeSearchModel.setForIdDroit(idDroit);
            pcAccordeeSearchModel.setWhereKey("forCalculAnciennesPCA");

            pcAccordeeSearchModel = PegasusImplServiceLocator.getPCAccordeeService().search(pcAccordeeSearchModel);
            if (pcAccordeeSearchModel.getSize() == 0) {
                throw new TransfertDossierException("No pcAccordee found!");
            }
            pcAccordees = new ArrayList<PCAccordee>();
            for (JadeAbstractModel absDonnee : pcAccordeeSearchModel.getSearchResults()) {
                PCAccordee pcAccordee = (PCAccordee) absDonnee;
                pcAccordee.loadPlanCalculs();
                pcAccordees.add(pcAccordee);
            }

        } catch (DroitException e) {
            throw new TransfertDossierException("Error while loading data for droit!", e);
        } catch (JadePersistenceException e) {
            throw new TransfertDossierException("A persistence error happened!", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TransfertDossierException("Service not available!", e);
        } catch (JadeApplicationException e) {
            throw new TransfertDossierException("A Jade error happened!", e);
        }
        // Chargement Babel
        try {
            documentsBabelTransfertDossier = BabelServiceLocator.getPCCatalogueTexteService()
                    .searchForTransfertDossierPC(TransfertDossierBuilderType.DEMANDE_EN_COURS);
            documentsBabelPageGardeCopiePC = BabelServiceLocator.getPCCatalogueTexteService()
                    .searchForPageGardeCopiePC();
        } catch (Exception e) {
            throw new TransfertDossierException("Error while loading catalogue Babel!", e);
        }

    }

    @Override
    public void loadParameters(Map<String, String> parameters, List<String> annexes, List<String> copies) {
        super.loadParameters(parameters, annexes, copies);
        motifTransfert = parameters.get(TransfertDossierSuppressionPCBuilder.MOTIF_TRANSFERT);
        motifContact = parameters.get(TransfertDossierSuppressionPCBuilder.MOTIF_CONTACT);
        dateDecision = parameters.get(TransfertDossierSuppressionPCBuilder.DATE_DECISION);
        dateSuppression = parameters.get(TransfertDossierSuppressionPCBuilder.DATE_SUPPRESSION);
        idDroit = parameters.get(TransfertDossierSuppressionPCBuilder.ID_DROIT);
    }

}
