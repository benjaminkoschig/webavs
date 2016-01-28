package globaz.corvus.process;

import globaz.corvus.db.demandes.REDemandeRente;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.ged.PRGedHelper;

public abstract class REAbstractInfoComplPrintProcess extends AbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDocument = "";
    private String eMailAddress = "";
    private String idAgenceCommunale = "";
    private String idDemandeRente = "";
    private String idTiersDemande = "";
    private Boolean isAgenceTrouve = Boolean.FALSE;
    private Boolean isCopieAgenceCommunale = Boolean.FALSE;
    private Boolean isSendToGed = Boolean.FALSE;

    private void fillDoInfo(JadePublishDocumentInfo docInfo) throws Exception {
        TIDocumentInfoHelper.fill(docInfo, getIdTiersDemande(), getSession(), null, null, null);
    }

    protected JadePublishDocumentInfo generateAndFillDocInfo(String documentTitleAndSubject, String noDocumentInfoRom)
            throws Exception {
        return this.generateAndFillDocInfo(documentTitleAndSubject, noDocumentInfoRom, false);
    }

    protected JadePublishDocumentInfo generateAndFillDocInfo(String documentTitleAndSubject, String noDocumentInfoRom,
            boolean archive) throws Exception {
        JadePublishDocumentInfo docInfo = JadePublishDocumentInfoProvider.newInstance(this);

        docInfo.setDocumentTitle(documentTitleAndSubject);
        docInfo.setDocumentSubject(documentTitleAndSubject);
        docInfo.setOwnerEmail(getEMailAddress());
        docInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
        docInfo.setArchiveDocument(archive);
        // bz-5941
        try {
            if (archive) {
                // bz-5941
                PRGedHelper h = new PRGedHelper();
                // Traitement uniquement pour la caisse concernée (CCB)
                if (h.isExtraNSS(getSession())) {
                    docInfo = h.setNssExtraFolderToDocInfo(getSession(), docInfo, getIdTiersDemande());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        docInfo.setPublishDocument(false);
        docInfo.setDocumentType(noDocumentInfoRom);
        docInfo.setDocumentTypeNumber(noDocumentInfoRom);

        // Correction pour la mise en GED FPV, la date ne peut pas être vide !!!!
        if (!JadeDateUtil.isGlobazDate(getDateDocument())) {

            // La date est vide, on va la récupérer depuis les infos complémentaire
            String date = null;

            try {
                REDemandeRente dem = new REDemandeRente();
                dem.setSession(getSession());
                dem.setIdDemandeRente(getIdDemandeRente());
                dem.retrieve();

                if (!dem.isNew()) {
                    PRInfoCompl ic = new PRInfoCompl();
                    ic.setSession(getSession());
                    ic.setIdInfoCompl(dem.getIdInfoComplementaire());
                    ic.retrieve();
                    if (!ic.isNew()) {
                        date = ic.getDateInfoCompl();
                    }
                }
            } catch (Exception e) {
                JadeCodingUtil.catchException(this.getClass().getSimpleName(), "generateAndFillDocInfo", e);
            }

            // S'il y a eu un problème, on assure le coup
            if (!JadeDateUtil.isGlobazDate(date)) {
                date = JACalendar.todayJJsMMsAAAA();
            }
            docInfo.setDocumentDate(date); // la date du jour si aucune date renseignée

        } else {
            docInfo.setDocumentDate(getDateDocument()); // ou la date renseignée
        }

        fillDoInfo(docInfo);

        return docInfo;
    }

    protected JadePublishDocumentInfo generateDestinationDocInfo(String documentTitleAndSubject) {
        JadePublishDocumentInfo destination = JadePublishDocumentInfoProvider.newInstance(this);

        destination.setDocumentTitle(documentTitleAndSubject);
        destination.setDocumentSubject(documentTitleAndSubject);
        destination.setOwnerEmail(getEMailAddress());
        destination.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
        destination.setArchiveDocument(false);
        destination.setPublishDocument(true);

        return destination;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getIdAgenceCommunale() {
        return idAgenceCommunale;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public String getIdTiersDemande() {
        return idTiersDemande;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    public boolean isAgenceTrouve() {
        return isAgenceTrouve.booleanValue();
    }

    public boolean isCopieAgenceCommunale() {
        return isCopieAgenceCommunale.booleanValue();
    }

    public boolean isSendToGed() {
        return getIsSendToGed().booleanValue();
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setEMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    public void setIdAgenceCommunale(String idAgenceCommunale) {
        this.idAgenceCommunale = idAgenceCommunale;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIdTiersDemande(String idTiers) {
        idTiersDemande = idTiers;
    }

    public void setIsAgenceTrouve(Boolean isAgenceTrouve) {
        this.isAgenceTrouve = isAgenceTrouve;
    }

    public void setIsCopieAgenceCommunale(Boolean isCopieAgenceCommunale) {
        this.isCopieAgenceCommunale = isCopieAgenceCommunale;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }
}
