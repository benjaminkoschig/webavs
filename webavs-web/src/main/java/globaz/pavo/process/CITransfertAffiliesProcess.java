package globaz.pavo.process;

import globaz.commons.nss.NSUtil;
import globaz.draco.db.preimpression.DSPreImpressionDeclarationListViewBean;
import globaz.draco.db.preimpression.DSPreImpressionViewBean;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.hermes.service.HEAttestationCAService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIExceptions;
import globaz.pavo.service.dto.CIDataRemiseCA;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe permet d'effectuer le transfert d'un affilié source à un affilié de destination.
 * 
 * @author
 * 
 */
public class CITransfertAffiliesProcess extends BProcess {

    private static final String NUMINFOROM = "0310CCI";

    private static final long serialVersionUID = -6136832491759903542L;

    private static final String TRANSFERT_ASSURES = "Transfert_des_assures_entre_affilies";

    private AFAffiliation affilieDst = null;

    private AFAffiliation affilieSrc = null;
    private int caseOnErrors;
    private boolean hasExceptions = true;
    private String dateDeFin = "";
    private JADate dateJa = null;
    private CITransfertAffiliesProcessCSV doc = new CITransfertAffiliesProcessCSV();
    private boolean imprimerAttestation = false;
    private String numeroAffilieDst = "";
    private String numeroAffilieSrc = "";

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        dateJa = new JADate(dateDeFin);

        DSPreImpressionDeclarationListViewBean mgrDs = new DSPreImpressionDeclarationListViewBean();
        mgrDs.setSession(getSession());

        CIApplication ciapp = new CIApplication();

        affilieSrc = (ciapp.getAffilieByNoIncludeRadie(getSession(), numeroAffilieSrc, true, false,
                String.valueOf(dateJa.getYear())));

        affilieDst = (ciapp.getAffilieByNo(getSession(), numeroAffilieDst, true, false,
                String.valueOf(dateJa.getMonth()), "", String.valueOf(dateJa.getYear()),
                String.valueOf(dateJa.getDay()), ""));

        mgrDs.setForAffiliesNumero(affilieSrc.getAffiliationId());
        mgrDs.setAnnee(String.valueOf(dateJa.getYear()));
        // trier par nom d'assurés
        mgrDs.setDateProdNNSS(true);
        mgrDs.find(BManager.SIZE_NOLIMIT);
        setProgressScaleValue(mgrDs.size());

        if (mgrDs.size() > 0) {

            List<JadePublishDocument> documentsToPrintlist = new ArrayList<JadePublishDocument>();

            for (int i = 0; i < mgrDs.size(); i++) {
                setProgressCounter(i);
                if (isAborted()) {
                    return false;
                }

                DSPreImpressionViewBean assure = (DSPreImpressionViewBean) mgrDs.get(i);
                String processStatus = "OK";
                String errorMessage = "";
                boolean isSuccess = true;

                try {
                    setProgressDescription(assure.getNumeroAvs() + "<br>" + i + "/" + mgrDs.size() + "<br>");

                    // Ajout d'une exception
                    CIExceptions exceptionCI = addExceptions(assure.getNumeroAvs(), assure.getCompteIndividuelId(),
                            affilieDst.getTiers().getLangue());

                    // Est-ce qu'on veut imprimer l'attestation
                    if (imprimerAttestation) {
                        imprimeAttestation(documentsToPrintlist, exceptionCI);
                    }

                    if (isOnError()) {
                        isSuccess = false;
                    }

                } catch (Exception e) {
                    JadeLogger
                            .error(getSession().getLabel("TRANSF_ASSURE_IMPOSSIBLE") + " " + assure.getNomPrenom(), e);
                    // passer les propriétés pour le CSV
                    processStatus = "KO";
                    errorMessage = getTransaction().getErrors().toString();
                    isSuccess = false;
                    // compter le nombre d'erreurs
                    caseOnErrors++;

                }

                finally {
                    // formatter la date pour qu'elle soit au bon format dans le CSV
                    String dateForCSV = "";
                    if (!JadeStringUtil.isBlankOrZero(assure.getDateEngagement())) {
                        String dateYear = assure.getDateEngagement().substring(0, 4);
                        String dateMonth = assure.getDateEngagement().substring(4, 6);
                        String dateDay = assure.getDateEngagement().substring(6, 8);
                        dateForCSV = JACalendar.format(dateDay + dateMonth + dateYear);
                    }
                    // enregistrer dans le CSV
                    doc.populateSheet(assure.getNomPrenom(), NSUtil.formatAVSNewNum(assure.getNumeroAvs()), dateForCSV,
                            numeroAffilieSrc, numeroAffilieDst, processStatus, errorMessage);
                    if (isSuccess) {
                        getTransaction().commit();
                    } else {
                        getTransaction().rollback();
                    }
                    getTransaction().clearErrorBuffer();
                    getSession().getErrors();

                }
            }
            // utilisation d'un register document
            registerDocuments(documentsToPrintlist);
            JadePublishDocumentInfo docInfoMergedDocument = createDocumentInfo();
            docInfoMergedDocument.setArchiveDocument(false);
            docInfoMergedDocument.setPublishDocument(true);
            this.mergePDF(docInfoMergedDocument, false, 500, false, "numero.affilie.formatte");
            createDocumentCsv();
        } else {
            getTransaction().addErrors(getSession().getLabel("AFFILIE_AUCUNE_INSC"));
            hasExceptions = false;
        }
        return !isOnError() && !isAborted() && !getTransaction().hasErrors();
    }

    @Override
    protected void _validate() throws Exception {

        dateJa = new JADate(dateDeFin);
        BSessionUtil.checkDateGregorian(getSession(), dateJa);
        if (JadeStringUtil.isBlankOrZero(dateDeFin)) {
            this._addError(getSession().getLabel("TRANSF_AFFILIE_DATE"));
            abort();
            return;
        }

        if (JadeStringUtil.isBlankOrZero(numeroAffilieSrc)) {
            this._addError(getSession().getLabel("TRANSF_ERROR_NUMERO_AFFILIE_SOURCE"));
            abort();
            return;
        }

        if (JadeStringUtil.isBlankOrZero(numeroAffilieDst)) {
            this._addError(getSession().getLabel("TRANSF_ERROR_NUMERO_AFFILIE_DEST"));
            abort();
            return;
        }

        CIApplication app = new CIApplication();
        // appel de la méthode permettant de setter un affilié radié, selon BZ 8594
        affilieSrc = (app.getAffilieByNoIncludeRadie(getSession(), numeroAffilieSrc, true, false,
                String.valueOf(dateJa.getYear())));
        if (affilieSrc == null) {
            this._addError(getSession().getLabel("TRANSF_AFFILIE_SOURCE") + " " + numeroAffilieSrc + " "
                    + getSession().getLabel("TRANSF_AFFILIE_SOURCE_INVALIDE"));
            abort();
            return;
        }

        affilieDst = (app.getAffilieByNo(getSession(), numeroAffilieDst, true, false, "", "",
                String.valueOf(dateJa.getYear()), "", ""));
        if (affilieDst == null) {
            this._addError(getSession().getLabel("TRANSF_AFFILIE_DEST") + " " + numeroAffilieDst + " "
                    + getSession().getLabel("TRANSF_AFFILIE_DEST_INVALIDE"));
            abort();
            return;
        }
    }

    /**
     * Ajout d'une exception CI en base
     * 
     * @param numeroAvs
     * @param idCompteIndividuel
     * @param langueCorrespondance
     * @return
     * @throws Exception
     */
    private CIExceptions addExceptions(String numeroAvs, String idCompteIndividuel, String langueCorrespondance)
            throws Exception {
        CIExceptions exceptionCI = new CIExceptions();
        exceptionCI.setAffilie(numeroAffilieDst);
        exceptionCI.setNumeroAvs(numeroAvs);
        exceptionCI.setIdCompteIndividuel(idCompteIndividuel);
        exceptionCI.setDateEngagement(dateDeFin);
        // si on n'a pas de langue de correspondance on prend celle du tiers.
        if (JadeStringUtil.isBlankOrZero(exceptionCI.getLangueCorrespondance())) {
            exceptionCI.setLangueCorrespondance(langueCorrespondance);
        }
        try {
            exceptionCI.add(getTransaction());

            if (getTransaction().hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }

        } catch (Exception e) {
            throw new Exception("Unabled to add Exception CI", e);
        }

        return exceptionCI;
    }

    // création d'un CSV pour la gestion des cas passés ou non
    private void createDocumentCsv() throws Exception {

        doc.setFilename(CITransfertAffiliesProcess.TRANSFERT_ASSURES);
        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(CIApplication.DEFAULT_APPLICATION_PAVO);
        docInfo.setDocumentTitle(CITransfertAffiliesProcess.TRANSFERT_ASSURES);
        docInfo.setDocumentTypeNumber(CITransfertAffiliesProcess.NUMINFOROM); // TODO numéro inforom
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        this.registerAttachedDocument(docInfo, doc.getOutputFile());

    }

    public String getDateDeFin() {
        return dateDeFin;
    }

    @Override
    protected String getEMailObject() {
        if (isAborted() || !hasExceptions) {
            return getSession().getLabel("TRANSFERT_AFFILIE_ECHEC") + " " + affilieSrc.getAffilieNumero() + " "
                    + affilieSrc.getTiers().getPrenomNom() + " " + getSession().getLabel("TO_AFFILIE_DST") + " "
                    + affilieDst.getAffilieNumero() + " " + affilieDst.getTiers().getPrenomNom();
        } else if (caseOnErrors >= 1) {
            return getSession().getLabel("TRANSFERT_AFFILIE") + " " + affilieSrc.getAffilieNumero() + " "
                    + affilieSrc.getTiers().getPrenomNom() + " " + dateDeFin + " "
                    + getSession().getLabel("TO_AFFILIE_DST") + " " + affilieDst.getAffilieNumero() + " "
                    + affilieDst.getTiers().getPrenomNom() + " "
                    + getSession().getLabel("TRANSFERT_AFFILIE_EXEC_CORRECTE") + " " + caseOnErrors + " "
                    + getSession().getLabel("TRANSFERT_AFFILIE_AVERTISSEMENTS");
        } else {
            return getSession().getLabel("TRANSFERT_AFFILIE") + " " + affilieSrc.getAffilieNumero() + " "
                    + affilieSrc.getTiers().getPrenomNom() + " " + dateDeFin + " "
                    + getSession().getLabel("TO_AFFILIE_DST") + " " + affilieDst.getAffilieNumero() + " "
                    + affilieDst.getTiers().getPrenomNom() + " " + getSession().getLabel("TRANSFERT_AFFILIE_SUCCES");
        }
    }

    public boolean getImprimerAttestation() {
        return imprimerAttestation;
    }

    public String getNumeroAffilieDst() {
        return numeroAffilieDst;
    }

    public String getNumeroAffilieSrc() {
        return numeroAffilieSrc;
    }

    /**
     * @param documentsToPrintlist
     * @param exceptionCI
     * @throws Exception
     */
    private void imprimeAttestation(List<JadePublishDocument> documentsToPrintlist, CIExceptions exceptionCI)
            throws Exception {

        CIDataRemiseCA dataRemiseCA = new CIDataRemiseCA();
        dataRemiseCA.peupler(exceptionCI);

        BSession sessionHermes = (BSession) exceptionCI.getSessionAnnonce(exceptionCI.getSession());
        String userEmail = exceptionCI.getSession().getUserEMail();

        JadePublishDocument document = HEAttestationCAService.getFirstAttestationRemiseCa(dataRemiseCA, sessionHermes,
                userEmail);

        // ajouter le document à la liste de JadePublish
        documentsToPrintlist.add(document);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setDateDeFin(String dateDeFin) {
        this.dateDeFin = dateDeFin;
    }

    public void setImprimerAttestation(boolean imprimerAttestation) {
        this.imprimerAttestation = imprimerAttestation;
    }

    public void setNumeroAffilieDst(String numeroAffilieDst) {
        this.numeroAffilieDst = numeroAffilieDst;
    }

    public void setNumeroAffilieSrc(String numeroAffilieSrc) {
        this.numeroAffilieSrc = numeroAffilieSrc;
    }

}
