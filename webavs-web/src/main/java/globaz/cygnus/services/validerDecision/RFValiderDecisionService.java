package globaz.cygnus.services.validerDecision;

import globaz.cygnus.db.decisions.RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager;
import globaz.cygnus.exceptions.RFBusinessException;
import globaz.cygnus.topaz.decision.RFGenererDecisionServiceOO;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.JadeLogger;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ch.globaz.topaz.datajuicer.DocumentData;

public class RFValiderDecisionService extends RFValiderDecisionMiseAJourEntitiesService {

    public void envoyerMail(String eMailAdresse, String objetMail, String message, String docPath) throws Exception {
        JadeSmtpClient.getInstance().sendMail(eMailAdresse, objetMail, message,
                JadeStringUtil.isBlankOrZero(docPath) ? null : new String[] { docPath });
    }

    private JadePrintDocumentContainer genererDocumentDecisionOo(boolean miseEnGed, boolean isSimulation, String idLot,
            AbstractJadeJob process, FWMemoryLog memoryLog, RFGenererDecisionServiceOO rfGenererDecisionOO,
            boolean isDecisionValide) throws Exception {
        return rfGenererDecisionOO.startGenererDecisionServiceOO(process, memoryLog, miseEnGed, isSimulation, idLot,
                isDecisionValide);
    }

    protected JadePrintDocumentContainer getDocumentDecisionOo(BSession session, String emailAdress,
            ArrayList<RFDecisionDocumentData> decisionArray, StringBuffer PDFDecisionURL, String dateSurDocument,
            DocumentData docData, boolean miseEnGed, boolean isSimulation, String idLot, boolean isDecisionValide,
            AbstractJadeJob process, FWMemoryLog memoryLog) throws Exception {

        RFGenererDecisionServiceOO rfGenererDecisionOo = new RFGenererDecisionServiceOO();

        initialiseDocumentDecisionOo(session, emailAdress, decisionArray, PDFDecisionURL, dateSurDocument, docData,
                rfGenererDecisionOo);

        return genererDocumentDecisionOo(miseEnGed, isSimulation, idLot, process, memoryLog, rfGenererDecisionOo,
                isDecisionValide);
    }

    protected JadePrintDocumentContainer getDocumentDecisionOoAvasad(BSession session, String emailAdress,
            ArrayList<RFDecisionDocumentData> decisionArray, StringBuffer PDFDecisionURL, String dateSurDocument,
            DocumentData docData, boolean miseEnGed, boolean isSimulation, String idLot, boolean isDecisionValide,
            AbstractJadeJob process, FWMemoryLog memoryLog, boolean isAvasad) throws Exception {

        RFGenererDecisionServiceOO rfGenererDecisionOo = new RFGenererDecisionServiceOO(isAvasad);

        initialiseDocumentDecisionOo(session, emailAdress, decisionArray, PDFDecisionURL, dateSurDocument, docData,
                rfGenererDecisionOo);

        return genererDocumentDecisionOo(miseEnGed, isSimulation, idLot, process, memoryLog, rfGenererDecisionOo,
                isDecisionValide);
    }

    private void initialiseDocumentDecisionOo(BSession session, String emailAdress,
            ArrayList<RFDecisionDocumentData> decisionArray, StringBuffer PDFDecisionURL, String dateSurDocument,
            DocumentData docData, RFGenererDecisionServiceOO rfGenererDecisionOo) {
        rfGenererDecisionOo.setSession(session);
        rfGenererDecisionOo.setEmail(emailAdress);
        rfGenererDecisionOo.setDecisionArray(decisionArray);
        rfGenererDecisionOo.setDocData(docData);
        rfGenererDecisionOo.setPdfDecisionURL(PDFDecisionURL);
        rfGenererDecisionOo.setDateDocument(dateSurDocument);
    }

    public void miseAJourEntites(ArrayList<RFDecisionDocumentData> decisionArray, String dateSurDocument,
            boolean isSimulation, String idGestionnaire, BSession session, BTransaction transaction) throws Exception {
        if (!isSimulation) {
            updateDecisionsQdsDemandesRfmAccordees(decisionArray, dateSurDocument, session, idGestionnaire, transaction);
        }

    }

    protected void rechercherDecisions(
            RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager decisionJointTiersManager,
            List<RFDecisionDocumentData> decisionArray, BSession session) throws Exception {

        RFRechercherDecisionsService rfRecDecSer = new RFRechercherDecisionsService();
        rfRecDecSer.rechercherDecisions(decisionJointTiersManager, decisionArray, session);
        rechercherTiersParDecisions(decisionArray, session);
        trierDecisionsParNss(decisionArray);

    }

    protected void rechercherTiersParDecisions(List<RFDecisionDocumentData> decisionArray, BSession session)
            throws Exception {
        // Parcours de chaque décision de la liste pour récupérer l'id tiers et rechercher le NSS correspondant
        List<RFDecisionDocumentData> decisionsSansAdresses = new ArrayList<RFDecisionDocumentData>();
        for (int i = 0; i < decisionArray.size(); i++) {
            RFDecisionDocumentData decision = decisionArray.get(i);
            try {

                PRTiersWrapper tiersWrapper = PRTiersHelper.getTiersParId(session, decision.getIdTiers());

                decision.setNss(tiersWrapper.getNSS());
                decision.setNom(tiersWrapper.getNom());
                decision.setPrenom(tiersWrapper.getPrenom());
                decision.setTitre(tiersWrapper.getTitre());
                decision.setAdresse(PRTiersHelper.getAdresseCourrierFormatee(session, decision.getIdTiers(), "",
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE));
                decision.setLangueTiers(session.getCode(tiersWrapper.getLangue()));
                if (JadeStringUtil.isBlank(decision.getAdresse())) {
                    decisionsSansAdresses.add(decision);
                }
            } catch (Exception e) {
                throw new Exception("error in  'RFValiderDecisionProcess : initialiserValidationDecision' "
                        + e.getMessage());
            }
        }
        if (decisionsSansAdresses.size() > 0) {
            StringBuilder message = new StringBuilder();
            message.append(session.getLabel("RF_PROCESS_LOT_COMPTABILISER_ADRESSE_COURRIER_PAS_BONNE"));
            for (RFDecisionDocumentData dec : decisionsSansAdresses) {
                message.append(MessageFormat.format(
                        session.getLabel("RF_PROCESS_LOT_COMPTABILISER_ADRESSE_COURRIER_PAS_BONNE_UNITAIRE"),
                        dec.getPrenom(), dec.getNom(), dec.getNss(), dec.getIdTiers(), dec.getIdDecision()));
            }
            JadeLogger
                    .error(this,
                            decisionsSansAdresses.size()
                                    + " décisions sans adresse de courrier détectées dans le lot lors de la comptabilisation. Détail envoyé à l'utilisateur "
                                    + session.getUserEMail() + ".");
            String msgString = message.toString();
            JadeSmtpClient.getInstance().sendMail(session.getUserEMail(),
                    session.getLabel("RF_PROCESS_LOT_COMPTABILISER_ADRESSE_COURRIER_PAS_BONNE_EMAIL_SUBJECT"),
                    msgString, null);
            throw new RFBusinessException(decisionsSansAdresses.size()
                    + " décisions sans adresse de courrier détectées dans le lot");

        }
    }

    protected void trierDecisionsParNss(List<RFDecisionDocumentData> decisionArray) {
        // Parcours de la liste pour ordrer les decisions par NSS
        Comparator<RFDecisionDocumentData> compareListDecision = new Comparator<RFDecisionDocumentData>() {
            @Override
            public int compare(RFDecisionDocumentData o1, RFDecisionDocumentData o2) {
                return o1.getNss().compareTo(o2.getNss());
            }
        };

        Collections.sort(decisionArray, compareListDecision);
    }

}
