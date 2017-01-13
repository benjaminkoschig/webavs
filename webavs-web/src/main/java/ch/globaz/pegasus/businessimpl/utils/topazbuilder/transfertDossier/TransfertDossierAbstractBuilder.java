package ch.globaz.pegasus.businessimpl.utils.topazbuilder.transfertDossier;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.Date;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.services.transfertDossier.ITransfertDossierBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.AbstractPegasusBuilder;
import ch.globaz.pyxis.business.model.LocaliteSimpleModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public abstract class TransfertDossierAbstractBuilder extends AbstractPegasusBuilder implements
        ITransfertDossierBuilder {

    public static final String DATE_SUR_DOCUMENT = "date sur document";
    public static final String DATE_TRANSFERT = "date transfert";
    public static final String ID_DEMANDE_PC = "IdDemandePC";
    public static final String ID_DERNIER_DOMICILE_LEGAL = "IdDernierDomicileLegal";
    public static final String ID_GESTIONNAIRE = "IdGestionnaire";
    public static final String ID_NOUVELLE_CAISSE = "IdNouvelleCaisse";
    public static final String MAIL_GEST = "MailGest";
    protected List<String> annexes;
    protected List<String> copies;
    protected LocaliteSimpleModel derniereLocalite = null;
    protected String idDemandePC = null;
    protected String idDernierDomicileLegal = null;
    protected String idGestionnaire = null;
    protected String idNouvelleCaisse;
    protected String mailGest = null;
    protected PersonneEtendueComplexModel requerant = null;
    protected String TOPAZ_DOCUMENT_TYPE = null;

    public TransfertDossierAbstractBuilder() {
        super();
    }

    public List<String> getAnnexes() {
        return annexes;
    }

    public List<String> getCopies() {
        return copies;
    }

    public LocaliteSimpleModel getDerniereLocalite() {
        return derniereLocalite;
    }

    public String getIdDemandePC() {
        return idDemandePC;
    }

    public String getIdDernierDomicileLegal() {
        return idDernierDomicileLegal;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdNouvelleCaisse() {
        return idNouvelleCaisse;
    }

    public String getMailGest() {
        return mailGest;
    }

    @Override
    public void loadParameters(Map<String, String> parameters, List<String> annexes, List<String> copies) {
        idDemandePC = parameters.get(TransfertDossierAbstractBuilder.ID_DEMANDE_PC);
        idDernierDomicileLegal = parameters.get(TransfertDossierAbstractBuilder.ID_DERNIER_DOMICILE_LEGAL);
        idGestionnaire = parameters.get(TransfertDossierAbstractBuilder.ID_GESTIONNAIRE);
        idNouvelleCaisse = parameters.get(TransfertDossierAbstractBuilder.ID_NOUVELLE_CAISSE);
        mailGest = parameters.get(TransfertDossierAbstractBuilder.MAIL_GEST);

        this.annexes = annexes;
        this.copies = copies;
    }

    protected void preparePubInfo(JadePublishDocumentInfo pubInfo) {

        pubInfo.setOwnerEmail(mailGest);
        pubInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, mailGest);
        pubInfo.setArchiveDocument(false);
        pubInfo.setPublishDocument(true);
        pubInfo.setDocumentType(TOPAZ_DOCUMENT_TYPE);
        pubInfo.setDocumentTypeNumber(TOPAZ_DOCUMENT_TYPE);
        pubInfo.setDocumentDate(JadeDateUtil.getFormattedDate(new Date()));
    }

    public void setAnnexes(List<String> annexes) {
        this.annexes = annexes;
    }

    public void setCopies(List<String> copies) {
        this.copies = copies;
    }

    public void setDerniereLocalite(LocaliteSimpleModel derniereLocalite) {
        this.derniereLocalite = derniereLocalite;
    }

    public void setIdDemandePC(String idDemandePC) {
        this.idDemandePC = idDemandePC;
    }

    public void setIdDernierDomicileLegal(String idDernierDomicileLegal) {
        this.idDernierDomicileLegal = idDernierDomicileLegal;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdNouvelleCaisse(String idNouvelleCaisse) {
        this.idNouvelleCaisse = idNouvelleCaisse;
    }

    public void setMailGest(String mailGest) {
        this.mailGest = mailGest;
    }

}