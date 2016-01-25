package ch.globaz.al.businessimpl.processus.traitementimpl;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.Date;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.constantes.ALConstDeclarationVersement;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.processus.BusinessTraitement;
import ch.globaz.al.utils.ALDateUtils;

/**
 * Traitement implémentant les déclarations de versement
 * 
 * @author PTA
 *         FIXME pas utilisé
 * 
 */
public class ImpressionDeclarationVersementTraitement extends BusinessTraitement {

    /**
     * Date de la déclaration de versement
     */
    private String dateDeclaration = null;
    /**
     * début de la période
     */
    private String debutPeriode = null;
    /**
     * Container des déclarations de versement
     */
    JadePrintDocumentContainer[] declarationVersement = new JadePrintDocumentContainer[1];
    /**
     * Fin de la période
     */
    private String finPeriode = null;

    /**
     * numAffilie
     */

    private String numAffilie = null;
    /**
     * numdossier
     */
    private String numDossier = null;
    /**
     * Période de la prestation
     */
    private String periodePrestation = null;
    /**
     * texte impot
     */
    private Boolean textImpot = null;
    /**
     * Type de déclaration de versement (impôt, frontalier,..)
     */
    private String typeDeclaration = null;
    /**
     * Type de document
     */
    private String typeDocument = null;

    /**
     * Constructeur
     */
    public ImpressionDeclarationVersementTraitement() {
        super();
    }

    @Override
    protected void execute() throws JadePersistenceException, JadeApplicationException {

        periodePrestation = getProcessusConteneur().getDataCriterias().periodeCriteria;
        typeDeclaration = ALConstDeclarationVersement.DECLA_VERSE_ATTEST_IMPOT;
        dateDeclaration = JadeDateUtil.getGlobazFormattedDate(new Date());
        setTypeDocument(ALConstDeclarationVersement.DECLA_TYPE_DOC_GLOB);

        JadePrintDocumentContainer container = new JadePrintDocumentContainer();
        // 4 traitements possibles
        if (JadeStringUtil.equals(typeDeclaration, ALConstDeclarationVersement.DECLA_VERSE_ATTEST_IMPOT, false)) {
            container = ALServiceLocator.getDeclarationsVersementService().getDeclarationsVersementDirect(
                    ALDateUtils.getDateDebutAnnee(periodePrestation), ALDateUtils.getDateFinAnnee(periodePrestation),
                    dateDeclaration, getTypeDocument(), getNumAffilie(), getNumDossier(), getTextImpot());
        }
        if (JadeStringUtil.equals(typeDeclaration, ALConstDeclarationVersement.DECLA_VERSE_FRONTALIER, false)) {
            container = ALServiceLocator.getDeclarationsVersementService().getDeclarationsFrontaliers(
                    ALDateUtils.getDateDebutAnnee(periodePrestation), ALDateUtils.getDateFinAnnee(periodePrestation),
                    dateDeclaration, getTypeDocument(), getNumAffilie(), getNumDossier(), getTextImpot());
        }

        if (JadeStringUtil.equals(typeDeclaration, ALConstDeclarationVersement.DECLA_VERSE_IMP_SOURCE, false)) {
            container = ALServiceLocator.getDeclarationsVersementService().getDeclarationsImposeSource(
                    ALDateUtils.getDateDebutMoisPourPeriode(periodePrestation),
                    ALDateUtils.getDateFinMoisPourPeriode(periodePrestation), dateDeclaration, getTypeDocument(),
                    getNumAffilie(), getNumDossier(), getTextImpot());
        }

        if (JadeStringUtil.equals(typeDeclaration, ALConstDeclarationVersement.DECLA_VERSE_NON_ACTIF, false)) {
            container = ALServiceLocator.getDeclarationsVersementService().getDeclarationsNonActifPaiementsIndirects(
                    ALDateUtils.getDateDebutAnnee(periodePrestation), ALDateUtils.getDateFinAnnee(periodePrestation),
                    dateDeclaration, getTypeDocument(), getNumAffilie(), getNumDossier(), getTextImpot());
        }

        JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();
        pubInfo.setOwnerEmail(JadeThread.currentUserEmail());
        pubInfo.setOwnerId(JadeThread.currentUserId());
        pubInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        pubInfo.setDocumentTitle(JadeThread.getMessage("al.declarationVersement.titre.attestation"));
        pubInfo.setDocumentSubject(JadeThread.getMessage("al.declarationVersement.titre.attestation"));
        pubInfo.setPublishDocument(true);
        container.setMergedDocDestination(pubInfo);

        declarationVersement[0] = container;

    }

    @Override
    protected void executeBack() {
        // DO NOTHING
    }

    @Override
    public String getCSTraitement() {
        return ALCSProcessus.NAME_TRAITEMENT_IMPRESSION_DECLARATION_VERSEMENT;
    }

    public String getDateDeclaration() {
        return dateDeclaration;
    }

    /**
     * @return the debutPeriode
     */
    public String getDebutPeriode() {
        return debutPeriode;
    }

    public JadePrintDocumentContainer[] getDeclarationVersement() {
        return declarationVersement;
    }

    /**
     * @return the finPeriode
     */
    public String getFinPeriode() {
        return finPeriode;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getNumDossier() {
        return numDossier;
    }

    public String getPeriodePrestation() {
        return periodePrestation;
    }

    @Override
    public JadePrintDocumentContainer[] getProtocole() {
        return declarationVersement;
    }

    @Override
    public ProtocoleLogger getProtocoleLogger() {
        // Pas de logger pour ce traitement
        return null;
    }

    @Override
    public JadePublishDocumentInfo getPubInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public Boolean getTextImpot() {
        return textImpot;
    }

    public String getTypeDeclaration() {
        return typeDeclaration;
    }

    /**
     * @return the typeDocument
     */
    public String getTypeDocument() {
        return typeDocument;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    public void setDateDeclaration(String dateDeclaration) {
        this.dateDeclaration = dateDeclaration;
    }

    /**
     * @param debutPeriode
     *            the debutPeriode to set
     */
    public void setDebutPeriode(String debutPeriode) {
        this.debutPeriode = debutPeriode;
    }

    public void setDeclarationVersement(JadePrintDocumentContainer[] declarationVersement) {
        this.declarationVersement = declarationVersement;
    }

    /**
     * @param finPeriode
     *            the finPeriode to set
     */
    public void setFinPeriode(String finPeriode) {
        this.finPeriode = finPeriode;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setNumDossier(String numDossier) {
        this.numDossier = numDossier;
    }

    public void setPeriodePrestation(String periodePrestation) {
        this.periodePrestation = periodePrestation;
    }

    public void setTextImpot(Boolean textImpot) {
        this.textImpot = textImpot;
    }

    public void setTypeDeclaration(String typeDeclaration) {
        this.typeDeclaration = typeDeclaration;
    }

    /**
     * @param typeDocument
     *            the typeDocument to set
     */
    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

}
