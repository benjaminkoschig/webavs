package globaz.corvus.process;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.topaz.REAttestationProlongationEtudeOO;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.prestation.ged.PRGedHelper;
import globaz.prestation.utils.ged.PRGedUtils;
import java.util.ArrayList;
import java.util.List;

public class REGenererAttestationProlongationEtudeProcess extends AbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JADate dateEcheanceEtude;
    private String emailAdresse = "";
    private StringBuffer errorBuffer = new StringBuffer("");
    private String idTiers = "";
    private Boolean isSendToGed = Boolean.FALSE;

    private REAttestationProlongationEtudeOO createDocument(RERenteAccJoinTblTiersJoinDemandeRente ra,
            RERenteAccJoinTblTiersJoinDemandeRente ra2) throws Exception {

        REAttestationProlongationEtudeOO attestationProlongationEtude = new REAttestationProlongationEtudeOO();

        attestationProlongationEtude.setSession(getSession());
        attestationProlongationEtude.setIdTiers(getIdTiers());
        attestationProlongationEtude.setRa(ra);
        if (ra2 != null) {
            attestationProlongationEtude.setRa2(ra2);
        }
        attestationProlongationEtude.setDateEcheance(JACalendar.format(getDateEcheanceEtude(),
                JACalendar.FORMAT_MMsYYYY));

        attestationProlongationEtude.generationLettre();

        return attestationProlongationEtude;
    }

    public JADate getDateEcheanceEtude() {
        return dateEcheanceEtude;
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("ATTESTATION_PROLONGATION_ETUDE");
    }

    public String getEmailAdresse() {
        return emailAdresse;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    @Override
    public String getName() {
        return getSession().getLabel("ATTESTATION_PROLONGATION_ETUDE");
    }

    @Override
    public void run() {
        try {

            setIsSendToGed(PRGedUtils
                    .isDocumentInGed(IRENoDocumentInfoRom.ATTESTATION_PROLONGATION_ETUDE, getSession()));

            JadePublishDocumentInfo pubDestination = JadePublishDocumentInfoProvider.newInstance(this);
            pubDestination.setDocumentTitle(getSession().getLabel("ATTESTATION_PROLONGATION_ETUDE"));
            pubDestination.setDocumentSubject(getSession().getLabel("ATTESTATION_PROLONGATION_ETUDE"));
            pubDestination.setOwnerEmail(getEmailAdresse());
            pubDestination.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEmailAdresse());
            pubDestination.setArchiveDocument(false);
            pubDestination.setPublishDocument(true);

            JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();
            allDoc.setMergedDocDestination(pubDestination);

            JadePublishDocumentInfo pubInfos = JadePublishDocumentInfoProvider.newInstance(this);
            pubInfos.setDocumentTitle(getSession().getLabel("ATTESTATION_PROLONGATION_ETUDE"));
            pubInfos.setDocumentSubject(getSession().getLabel("ATTESTATION_PROLONGATION_ETUDE"));
            pubInfos.setOwnerEmail(getEmailAdresse());
            pubInfos.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEmailAdresse());
            pubInfos.setArchiveDocument(getIsSendToGed().booleanValue());
            try {
                if (getIsSendToGed().booleanValue()) {
                    // bz-5941
                    PRGedHelper h = new PRGedHelper();
                    // Traitement uniquement pour la caisse concernée (CCB)
                    if (h.isExtraNSS(getSession())) {
                        pubInfos = h.setNssExtraFolderToDocInfo(getSession(), pubInfos, getIdTiers());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            pubInfos.setPublishDocument(false);
            pubInfos.setDocumentType(IRENoDocumentInfoRom.ATTESTATION_PROLONGATION_ETUDE);
            pubInfos.setDocumentTypeNumber(IRENoDocumentInfoRom.ATTESTATION_PROLONGATION_ETUDE);
            pubInfos.setDocumentDate(JACalendar.todayJJsMMsAAAA());
            TIDocumentInfoHelper.fill(pubInfos, getIdTiers(), getSession(), null, null, null);

            RERenteAccJoinTblTiersJoinDemRenteManager renteAccMgr = new RERenteAccJoinTblTiersJoinDemRenteManager();
            renteAccMgr.setSession(getSession());
            renteAccMgr.setForIdTiersBeneficiaire(getIdTiers());
            renteAccMgr.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", "
                    + IREPrestationAccordee.CS_ETAT_PARTIEL);
            renteAccMgr.find();

            if (!renteAccMgr.isEmpty()) {

                String tempIDAdressePaiement = "";
                boolean needToSplitDocument = false;
                List<RERenteAccJoinTblTiersJoinDemandeRente> rentesAccordeesTiersBenef = new ArrayList<RERenteAccJoinTblTiersJoinDemandeRente>();
                for (int i = 0; i < renteAccMgr.size(); i++) {

                    RERenteAccJoinTblTiersJoinDemandeRente ra = (RERenteAccJoinTblTiersJoinDemandeRente) renteAccMgr
                            .get(i);

                    if (JadeStringUtil.isBlankOrZero(tempIDAdressePaiement)) {
                        tempIDAdressePaiement = ra.getIdTiersAdressePmt();
                    }

                    if (!tempIDAdressePaiement.equals(ra.getIdTiersAdressePmt())) {
                        needToSplitDocument = true;
                    }

                    if (!JadeStringUtil.isEmpty(ra.getDateFinDroit())) {
                        continue;
                    }

                    rentesAccordeesTiersBenef.add(ra);

                }
                generateDocumentsFromRaData(allDoc, pubInfos, needToSplitDocument, rentesAccordeesTiersBenef);

            }

            if (errorBuffer.length() > 0) {
                getLogSession().addMessage(
                        new JadeBusinessMessage(JadeBusinessMessageLevels.WARN,
                                "REGenerationAttestationProlongationEtudeOO", errorBuffer.toString()));
                List<String> emails = new ArrayList<String>();
                emails.add(getEmailAdresse());
                sendCompletionMail(emails);
            } else {
                this.createDocuments(allDoc);
            }
        } catch (Exception e) {
            getLogSession().addMessage(
                    new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR,
                            "REGenerationAttestationProlongationEtudeOO", e.toString()));
        }
    }

    private void generateDocumentsFromRaData(JadePrintDocumentContainer allDoc, JadePublishDocumentInfo pubInfos,
            boolean needToSplitDocument, List<RERenteAccJoinTblTiersJoinDemandeRente> rentesAccordeesTiersBenef)
            throws Exception {
        if (needToSplitDocument || rentesAccordeesTiersBenef.size() < 2) {
            for (RERenteAccJoinTblTiersJoinDemandeRente ra : rentesAccordeesTiersBenef) {
                callDocumentGenerator(allDoc, pubInfos, ra, null);
            }
        } else {
            // On ne créé qu'un document si les adresses de paiement sont les mêmes. Le document devra contenir
            // 2 objets (un objet par rente)
            callDocumentGenerator(allDoc, pubInfos, rentesAccordeesTiersBenef.get(0), rentesAccordeesTiersBenef.get(1));
        }
    }

    private void callDocumentGenerator(JadePrintDocumentContainer allDoc, JadePublishDocumentInfo pubInfos,
            RERenteAccJoinTblTiersJoinDemandeRente ra, RERenteAccJoinTblTiersJoinDemandeRente ra2) throws Exception {
        REAttestationProlongationEtudeOO attProEtude = this.createDocument(ra, ra2);
        allDoc.addDocument(attProEtude.getDocumentData(), pubInfos);

        if (!JadeStringUtil.isBlankOrZero(attProEtude.getErrorBuffer().toString())) {
            errorBuffer.append(attProEtude.getErrorBuffer() + "\n");
        }
    }

    public void setDateEcheanceEtude(JADate dateEcheanceEtude) {
        this.dateEcheanceEtude = dateEcheanceEtude;
    }

    public void setEmailAdresse(String emailAdresse) {
        this.emailAdresse = emailAdresse;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }
}
