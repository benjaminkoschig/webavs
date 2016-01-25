package globaz.ij.itext;

import globaz.babel.api.ICTDocument;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.framework.printing.itext.types.FWITemplateType;
import globaz.framework.util.FWMessage;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.ij.api.codesystem.IIJCatalogueTexte;
import globaz.ij.application.IJApplication;
import globaz.ij.db.prononces.IJPrononce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.external.IntRole;
import globaz.prestation.application.PRAbstractApplication;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRStringUtils;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class IJMoyensDroit extends FWIDocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String CTD_DONNEES_TRIBUNAL = "{donneesTribunal}";
    private static final String FICHIER_MODELE_MOYENS_DROIT = "IJ_MOYENS_DROIT";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String codeIsoLangue = "";

    private ICTDocument document;
    private ICTDocument documentHelper;
    private boolean isCopieDocument = false;
    private boolean isPetiteIJ = false;

    private LinkedList lignes = new LinkedList();
    private int nbDocument = 0;
    private IJPrononce prononce = null;
    PRTiersWrapper tiers;

    /**
     * Set les proprietes du JadePublishDocumentInfo pour archivage du document dans la GED
     */
    @Override
    public void afterPrintDocument() {

        JadePublishDocumentInfo docInfo = getDocumentInfo();
        String anneeMoyenDroit = null;

        try {
            anneeMoyenDroit = JADate.getYear(JACalendar.todayJJsMMsAAAA()).toString();

            if (!isCopieDocument) {
                docInfo.setArchiveDocument(true);
            } else {
                docInfo.setArchiveDocument(false);
            }
            docInfo.setPublishDocument(false);
            docInfo.setDocumentProperty("annee", anneeMoyenDroit);
            docInfo.setDocumentType(IPRConstantesExternes.DECISION_MOYENS_DE_DROIT);
            docInfo.setDocumentTypeNumber(IPRConstantesExternes.DECISION_MOYENS_DE_DROIT);

            TIDocumentInfoHelper.fill(docInfo, tiers.getIdTiers(), getSession(), IntRole.ROLE_IJAI, "", "");

            super.afterPrintDocument();
        } catch (JAException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "IJMoyenDroit");
            abort();
            e.printStackTrace();
        }

    }

    @Override
    public void beforeBuildReport() throws FWIException {

        // Reprise ou remise à zéro des autres paramètres
        Map parametres = getImporter().getParametre();

        if (parametres == null) {
            parametres = new HashMap();
            getImporter().setParametre(parametres);
        } else {
            parametres.clear();
        }

        setDocumentTitle("Moyens de droit");

        // Création du document
        // ------------------------------------------------------------------------------------------------------
        StringBuffer buffer = new StringBuffer();

        // Recherche des données du tribunal (Tribunal du canton de domicile)
        String donnesTribunal = "";

        try {
            if ("true".equals(PRAbstractApplication.getApplication(IJApplication.DEFAULT_APPLICATION_IJ).getProperty(
                    IJApplication.IS_CAISSE_CANTONALE))) {

                donnesTribunal = PRTiersHelper.getAdresseTribunalPourOfficeAI(getSession(), prononce.getOfficeAI(),
                        tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));

            } else {

                donnesTribunal = PRTiersHelper.getAdresseTribunalPourTiers(getSession(), tiers);
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "IJMoyenDroit");
            abort();
        }

        if (!JadeStringUtil.isBlankOrZero(donnesTribunal)
                && !PRTiersHelper.AUCUN_TRIBUNAL_TROUVE.equals(donnesTribunal)) {
            buffer.append(PRStringUtils.replaceString(document.getTextes(1).getTexte(1).getDescription(),
                    IJMoyensDroit.CTD_DONNEES_TRIBUNAL, donnesTribunal));
        } else {
            buffer.append(document.getTextes(1).getTexte(1).getDescription());
            getMemoryLog().logMessage(PRTiersHelper.AUCUN_TRIBUNAL_TROUVE, FWMessage.ERREUR, "IJMoyenDroit");
            abort();
        }

        buffer.append(document.getTextes(2).getTexte(1).getDescription());

        if (!isPetiteIJ()) {
            buffer.append(document.getTextes(3).getTexte(1).getDescription());
        }

        buffer.append(document.getTextes(4).getTexte(1).getDescription());

        buffer.append(document.getTextes(5).getTexte(1).getDescription());

        parametres.put("PARAM_TEXTE", buffer.toString());

    }

    @Override
    public void beforeExecuteReport() throws FWIException {

        try {

            try {
                // le modele
                String extensionModelCaisse = getSession().getApplication().getProperty("extensionModelITextCaisse");
                if (!JadeStringUtil.isEmpty(extensionModelCaisse)) {
                    setTemplateFile(IJMoyensDroit.FICHIER_MODELE_MOYENS_DROIT + extensionModelCaisse);
                    FWIImportManager im = getImporter();
                    File sourceFile = new File(im.getImportPath() + im.getDocumentTemplate()
                            + FWITemplateType.TEMPLATE_JASPER.toString());
                    if ((sourceFile != null) && sourceFile.exists()) {
                        ;
                    } else {
                        setTemplateFile(IJMoyensDroit.FICHIER_MODELE_MOYENS_DROIT);
                    }
                } else {
                    setTemplateFile(IJMoyensDroit.FICHIER_MODELE_MOYENS_DROIT);
                }
            } catch (Exception e) {
                setTemplateFile(IJMoyensDroit.FICHIER_MODELE_MOYENS_DROIT);
            }

            codeIsoLangue = getSession().getCode(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
            codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);

            // chargement du catalogue de texte
            documentHelper = PRBabelHelper.getDocumentHelper(getISession());
            documentHelper.setCsDomaine(IIJCatalogueTexte.CS_IJ);
            documentHelper.setCsTypeDocument(IIJCatalogueTexte.CS_MOYENS_DROIT);
            documentHelper.setDefault(Boolean.TRUE);
            documentHelper.setActif(Boolean.TRUE);

            documentHelper.setCodeIsoLangue(codeIsoLangue);

            ICTDocument[] documents = documentHelper.load();

            if ((documents == null) || (documents.length == 0)) {
                getMemoryLog().logMessage("impossible de charger le catalogue de texte", FWMessage.ERREUR,
                        "Moyens de droit");
                abort();
            } else {
                document = documents[0];
            }

        } catch (Exception e) {
            throw new FWIException("impossible de charger le catalogue de texte pour les moyens de droit", e);
        }

    }

    @Override
    public void createDataSource() throws Exception {
        lignes = new LinkedList();
        lignes.add("");
        this.setDataSource(lignes);
    }

    public IJPrononce getPrononce() {
        return prononce;
    }

    public PRTiersWrapper getTiers() {
        return tiers;
    }

    public boolean isPetiteIJ() {
        return isPetiteIJ;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public boolean next() throws FWIException {
        if (nbDocument == 0) {
            nbDocument++;
            return true;
        } else {
            return false;
        }
    }

    public void setIsCopieDocument(boolean isCopieDocument) {
        this.isCopieDocument = isCopieDocument;
    }

    public void setPetiteIJ(boolean isPetiteIJ) {
        this.isPetiteIJ = isPetiteIJ;
    }

    public void setPrononce(IJPrononce prononce) {
        this.prononce = prononce;
    }

    public void setTiers(PRTiersWrapper tiers) {
        this.tiers = tiers;
    }

}
