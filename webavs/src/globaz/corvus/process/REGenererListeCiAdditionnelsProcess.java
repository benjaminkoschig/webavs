package globaz.corvus.process;

import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.db.ci.REListeCiAdditionnelsManager;
import globaz.corvus.db.ci.TypeListeCiAdditionnels;
import globaz.corvus.excel.REListeExcelCIEnAttenteDeCIAdditionnel;
import globaz.corvus.excel.REListeExcelCiAdditionnels;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;

/**
 * Ce processus génère la listes des CI additionnels soit la liste :</br> - des CI additionnels
 * <strong>Réceptionné</strong></br> - des CI additionnels <strong>Traité</strong></br> - des CI additionnels
 * <strong>Non Traité</strong></br> - des CI en attente de CI additionnels <strong>Réceptionné</strong></br> - des CI en
 * attente de CI additionnels <strong>Traité</strong></br> - des CI en attente de CI additionnels
 * <strong>Tous</strong></br>
 * 
 * @author lga
 */
public class REGenererListeCiAdditionnelsProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut = "";
    private String dateDernierPaiement;
    private String dateFin = "";
    private TypeListeCiAdditionnels genreCiAdd = null;
    private String noAgence;
    private String noCaisse;
    private boolean ajouterCommunePolitique;

    public REGenererListeCiAdditionnelsProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            // Récupération de la liste des ci additionnels
            REListeCiAdditionnelsManager manager = new REListeCiAdditionnelsManager();
            manager.setSession(getSession());
            manager.setForDateDebut(getDateDebut());
            manager.setForDateFin(getDateFin());
            manager.setForGenreCiAdd(getGenreCiAdd());
            manager.find(BManager.SIZE_NOLIMIT);

            switch (getGenreCiAdd()) {
                case RECEPTIONNES:
                case TRAITES:
                case NON_TRAITES:
                    genererListeExcelCiAdditionnels(manager);
                    break;

                case ATTENTE_CI_ADD_TOUS:
                case ATTENTE_CI_ADD_PROVISOIRE:
                case ATTENTE_CI_ADD_TRAITE:
                    genererListeExcelCIEnAttenteDeCIAdditionnel(manager);
                    break;

                default:
                    throw new Exception(
                            "Internal error : the type of Excel CI list is not defined, can not build the list. TypeListeCiAdditionnels : ["
                                    + getGenreCiAdd() + "]");
            }
            return true;

        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return false;
        }
    }

    @Override
    protected void _validate() throws Exception {
        validateProcess();
    }

    /**
     * Génère la liste Excel des CIs additionnels
     * 
     * @param manager
     * @throws Exception
     */
    private void genererListeExcelCiAdditionnels(REListeCiAdditionnelsManager manager) throws Exception {
        // Création du document
        REListeExcelCiAdditionnels excelDocA = new REListeExcelCiAdditionnels(getSession());
        excelDocA.setDateDebut(getDateDebut());
        excelDocA.setDateFin(getDateFin());
        excelDocA.setDateDernierPaiement(getDateDernierPaiement());
        excelDocA.setGenreCiAdd(getGenreCiAdd());
        excelDocA.setNoCaisse(getNoCaisse());
        excelDocA.setNoAgence(getNoAgence());
        excelDocA.setAjouterCommunePolitique(getAjouterCommunePolitique());        
        excelDocA.populateSheetListe(manager, getTransaction());

        // Création du doc info
        JadePublishDocumentInfo documentInfo = new JadePublishDocumentInfo();
        documentInfo.setOwnerEmail(getEMailAddress());
        documentInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
        documentInfo.setDocumentTitle(getSession().getLabel(excelDocA.getDocumentTitleKey()));
        documentInfo.setDocumentSubject(getSession().getLabel(excelDocA.getDocumentTitleKey()));
        documentInfo.setArchiveDocument(false);
        documentInfo.setPublishDocument(true);
        documentInfo.setDocumentType(IRENoDocumentInfoRom.LISTE_CI_ADDITIONNELS);
        documentInfo.setDocumentTypeNumber(IRENoDocumentInfoRom.LISTE_CI_ADDITIONNELS);

        // Publication
        this.registerAttachedDocument(documentInfo, excelDocA.getOutputFile());
    }

    /**
     * Génère la liste Excel des CIs en attentes de CIs additionnels
     * 
     * @param manager
     * @throws Exception
     */
    private void genererListeExcelCIEnAttenteDeCIAdditionnel(REListeCiAdditionnelsManager manager) throws Exception {
        // Création du document
        REListeExcelCIEnAttenteDeCIAdditionnel excelDocB = new REListeExcelCIEnAttenteDeCIAdditionnel(getSession());
        excelDocB.setDateDebut(getDateDebut());
        excelDocB.setDateFin(getDateFin());
        excelDocB.setDateDernierPaiement(getDateDernierPaiement());
        excelDocB.setGenreCiAdd(getGenreCiAdd());
        excelDocB.setNoCaisse(getNoCaisse());
        excelDocB.setNoAgence(getNoAgence());
        excelDocB.setAjouterCommunePolitique(getAjouterCommunePolitique());
        excelDocB.populateSheetListe(manager, getTransaction());

        // Création du doc info
        JadePublishDocumentInfo documentInfo = new JadePublishDocumentInfo();
        documentInfo.setOwnerEmail(getEMailAddress());
        documentInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
        documentInfo.setDocumentTitle(getSession().getLabel(excelDocB.getDocumentTitleKey()));
        documentInfo.setDocumentSubject(getSession().getLabel(excelDocB.getDocumentTitleKey()));
        documentInfo.setArchiveDocument(false);
        documentInfo.setPublishDocument(true);
        documentInfo.setDocumentType(IRENoDocumentInfoRom.LISTE_CI_ATTENTE_CI_ADDITIONNELS);
        documentInfo.setDocumentTypeNumber(IRENoDocumentInfoRom.LISTE_CI_ATTENTE_CI_ADDITIONNELS);

        // Publication
        this.registerAttachedDocument(documentInfo, excelDocB.getOutputFile());
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public final String getDateDernierPaiement() {
        return dateDernierPaiement;
    }

    public String getDateFin() {
        return dateFin;
    }

    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("EMAIL_OBJECT_GENERATION_CI_ADD_ERREUR");
        } else {
            return getSession().getLabel("EMAIL_OBJECT_GENERATION_CI_ADD_SUCCES");
        }
    }

    public TypeListeCiAdditionnels getGenreCiAdd() {
        return genreCiAdd;
    }

    public final String getNoAgence() {
        return noAgence;
    }

    public final String getNoCaisse() {
        return noCaisse;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public final void setDateDernierPaiement(String dateDernierPaiement) {
        this.dateDernierPaiement = dateDernierPaiement;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setGenreCiAdd(TypeListeCiAdditionnels genreCiAdd) {
        this.genreCiAdd = genreCiAdd;
    }

    public final void setNoAgence(String noAgence) {
        this.noAgence = noAgence;
    }

    public final void setNoCaisse(String noCaisse) {
        this.noCaisse = noCaisse;
    }

    private String translate(String labelKey) throws Exception {
        if (JadeStringUtil.isEmpty(labelKey)) {
            throw new Exception("Empty labelKey received for translation");
        } else if (getSession() == null) {
            throw new Exception("No session founded for labelKey [" + labelKey + "] translation");
        } else {
            return getSession().getLabel(labelKey);
        }
    }

    /**
     * Valid que le process soit correctement renseigné avant son exécution
     * 
     * @throws Exception
     *             Lance une Exception en cas d'erreur
     */
    public void validateProcess() throws Exception {
        if (!JadeDateUtil.isGlobazDate(getDateDebut())) {
            if (!JadeStringUtil.isBlankOrZero(getDateDebut())
                    || !TypeListeCiAdditionnels.NON_TRAITES.equals(getGenreCiAdd())) {
                String message = translate("GENERER_LISTE_CI_ADD_DATE_DEBUT_VIDE");
                throw new Exception(message);
            }

        }
        if (!JadeDateUtil.isGlobazDate(getDateFin())) {
            if (!JadeStringUtil.isBlankOrZero(getDateFin())
                    || !TypeListeCiAdditionnels.NON_TRAITES.equals(getGenreCiAdd())) {
                String message = translate("GENERER_LISTE_CI_ADD_DATE_FIN_VIDE");
                throw new Exception(message);
            }

        }
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            String message = translate("GENERER_LISTE_CI_ADD_EMAIL_VIDE");
            throw new Exception(message);
        }
        if (JadeStringUtil.isEmpty(getDateDernierPaiement())) {
            String message = translate("GENERER_LISTE_CI_ADD_DATE_DERNIER_PAIEMENT_VIDE");
            throw new Exception(message);
        }
        if (JadeStringUtil.isEmpty(getNoCaisse())) {
            String message = translate("GENERER_LISTE_CI_ADD_NUMERO_CAISSE_VIDE");
            throw new Exception(message);
        }
        if (JadeStringUtil.isEmpty(getNoAgence())) {
            String message = translate("GENERER_LISTE_CI_ADD_NUMERO_AGENCE_VIDE");
            throw new Exception(message);
        }
        if (getGenreCiAdd() == null) {
            String message = translate("GENERER_LISTE_CI_ADD_GENRE_CI_ADD_VIDE");
            throw new Exception(message);
        }
    }

    public boolean getAjouterCommunePolitique() {
        return ajouterCommunePolitique;
    }

    public void setAjouterCommunePolitique(boolean ajouterCommunePolitique) {
        this.ajouterCommunePolitique = ajouterCommunePolitique;
    }

}
