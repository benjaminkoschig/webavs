/*
 * Créé le 2 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.process;

import globaz.babel.api.ICTDocument;
import globaz.draco.print.itext.Doc2_preImpressionDeclaration;
import globaz.framework.printing.itext.export.FWIExportManager;
import globaz.framework.printing.itext.types.FWIDocumentType;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.leo.application.LEApplication;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.data.LEEnvoiDataSource;
import globaz.leo.db.envoi.LEEtapesSuivantesListViewBean;
import globaz.leo.db.envoi.LEEtapesSuivantesViewBean;
import globaz.leo.process.generation.ILEGeneration;
import globaz.leo.process.handler.LEDocumentHandler;
import globaz.leo.process.handler.LEEnvoiHandler;
import globaz.leo.process.handler.LEJournalHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRExporterParameter;
import org.apache.commons.io.FileUtils;
import ch.globaz.common.document.babel.CatalogueTexteLoader;
import ch.globaz.common.document.babel.TexteGiverBabelWithVariables;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.orion.businessimpl.services.ServicesProviders;
import ch.globaz.xmlns.eb.mail.FileExtension;

/**
 * @author ald
 */
public class LEGenererEtapesSuivantes extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String categorie = new String();
    private String commentaire;
    private ArrayList csFormule = new ArrayList();
    private String dateImpression;
    private String datePriseEnCompte;
    private FWIExportManager export;
    private boolean isSimulation;
    private int nbDoc = 0;
    private int nbreMaxPageParDoc = 500;
    private ArrayList orderBy;
    private String orderBy1;
    private String orderBy2;
    private boolean sendMailToAffiliee = false;
    private boolean wantOrder = false;
    private Map<String, String> mapNumerosAffilieMail = new HashMap<String, String>();
    private List<String> listEmailEtNumAffDesRappelsEnvoyes = new ArrayList<String>();
    private Collection<String> forNumerosAffilie = new ArrayList<String>();

    @Override
    protected void _executeCleanUp() {
        export.deleteAll();
    }

    @Override
    protected boolean _executeProcess() {
        String idLot;
        try {
            LEEtapesSuivantesListViewBean listeEtapes = new LEEtapesSuivantesListViewBean();
            listeEtapes.setSession(getSession());
            listeEtapes.setDatePriseEnCompte(datePriseEnCompte);
            // listeEtapes.setOrderByProvenance(orderBy);
            listeEtapes.setOrderBy1(getOrderBy1());
            listeEtapes.setOrderBy2(getOrderBy2());
            listeEtapes.setForCsFormule(getCsFormule());
            listeEtapes.setForCategories(getCategorie());
            listeEtapes.setForNumerosAffilie(forNumerosAffilie);
            /*--------------------------------------------------------------------
            Initialiser la progression du process
            ---------------------------------------------------------------------
             */
            listeEtapes.setWantOrderBy(false);
            nbDoc = listeEtapes.getCount(getTransaction()); // provisoire
            if (nbDoc > 0) {
                setProgressScaleValue(nbDoc);
            } else {
                setProgressScaleValue(1);
            }
            /*********************************************************/
            // init la génération
            listeEtapes.setWantOrderBy(isWantOrder());
            BStatement statement = listeEtapes.cursorOpen(getTransaction());
            LEEtapesSuivantesViewBean envoiCrt = null;
            LEEnvoiHandler envoi = new LEEnvoiHandler();
            LEDocumentHandler docHandler = new LEDocumentHandler();
            LEEnvoiDataSource envoiDS;
            // init l'objet exporter
            export = new FWIExportManager();
            export.setExportApplicationRoot(LEApplication.APPLICATION_LEO_REP);
            export.setExportFileName("ResultEtapesSuivantes");
            export.setExportFileType(FWIDocumentType.PDF);
            export.setExporterOutline(JRExporterParameter.OUTLINE_NONE);
            // init un nouveau lot pour grouper le traitement
            idLot = envoi.addNewLot(getSession(), getTransaction(), getCommentaire(),
                    ILEConstantes.CS_LOT_TYPE_ETAPESUIVANTE);
            // init un nouveau compteur
            int nbreEtape = 0;
            // on charge tous les paramètres pour chaque envoi

            List<JadePublishDocument> jadePublishDocuments = new ArrayList<JadePublishDocument>();
            TexteGiverBabelWithVariables textGiver = null;
            sendMailToAffiliee = !forNumerosAffilie.isEmpty();
            if (sendMailToAffiliee) {
                CatalogueTexteLoader loader = new CatalogueTexteLoader(getSession());
                Map<Langues, ICTDocument> map = loader.loadICTDocumentByDomaineTypeDocAndName(
                        Doc2_preImpressionDeclaration.CS_DOMAINE, Doc2_preImpressionDeclaration.CS_RAPPEL_E_BUSINESS,
                        "Rappel e-business");
                textGiver = new TexteGiverBabelWithVariables(map, getSession());
            }

            while ((envoiCrt = (LEEtapesSuivantesViewBean) listeEtapes.cursorReadNext(statement)) != null) {
                if (isAborted()) {
                    getTransaction().rollback();
                    return false;
                }
                // charger les données relatives au document a imprimer
                try {

                    envoiDS = envoi.chargerDonnees(envoiCrt.getIdJournalisation(), envoiCrt.getEtapeSuivante(),
                            getSession(), getTransaction());

                } catch (Exception e) {
                    getMemoryLog()
                            .logMessage(
                                    "Erreur survenue lors du chargement des données pour l'envoi id = "
                                            + envoiCrt.getIdEnvoi(), FWMessage.INFORMATION, "Etapes suivante processus");
                    getMemoryLog().logMessage(e.getMessage(), FWMessage.INFORMATION, "Etapes suivante processus");
                    JadeLogger
                            .info("Erreur survenue lors du chargement des données pour l'envoi id = "
                                    + envoiCrt.getIdEnvoi(), e);
                    continue;
                }
                try {
                    // génère le document
                    // si un document est attaché à l'étape BZ 6701
                    ILEGeneration generateur = docHandler.chargeGenerateurDocument(envoiDS, getSession());
                    if (generateur != null) {
                        generateur.setParent(this);
                        generateur.setDateImpression(getDateImpression());
                        generateur.setPublishDocument(false);
                        generateur.executeProcess();

                        if (sendMailToAffiliee) {
                            for (Object object : getAttachedDocuments()) {
                                JadePublishDocument doc = (JadePublishDocument) object;
                                jadePublishDocuments.add(doc);
                                String mail = mapNumerosAffilieMail.get(envoiCrt.getLibelle());
                                if (mail != null && !mail.isEmpty()) {
                                    if (!isSimulation) {
                                        sendMail(envoiCrt, textGiver, doc, mail);
                                    }
                                    String numAffilie = envoiCrt.getLibelle();
                                    listEmailEtNumAffDesRappelsEnvoyes.add(numAffilie + ":" + mail);
                                }

                            }
                            getAttachedDocuments().clear();
                        }
                    }
                } catch (Exception e) {
                    // erreur dans la génération
                    String param = envoiDS.getParamEnvoi() != null ? envoiDS.getParamEnvoi().toString()
                            : "Aucun paramètre, id envoi=" + envoiCrt.getIdEnvoi();
                    getMemoryLog().logMessage(
                            "Erreur survenue lors de la génération du document type ="
                                    + envoiDS.getField(LEEnvoiDataSource.CS_ETAPE_SUIVANTE) + param,
                            FWMessage.INFORMATION, "Etapes suivante processus");
                    getMemoryLog().logMessage(e.getMessage(), FWMessage.INFORMATION, "Etapes suivante processus");
                    JadeLogger.info(
                            "Erreur survenue lors de la génération du document type ="
                                    + envoiDS.getField(LEEnvoiDataSource.CS_ETAPE_SUIVANTE) + param, e);
                    continue;
                }
                try {
                    // on génère l'événement si on est pas en simulation
                    if (!getSimulation()) {
                        envoi.genererEnvoi(idLot, getDateImpression(), envoiDS, getSession(), getTransaction());
                        nbreEtape++;
                        getTransaction().commit();
                    }
                } catch (Exception e) {
                    String param = envoiDS.getParamEnvoi() != null ? envoiDS.getParamEnvoi().toString()
                            : "Aucun paramètre, id envoi=" + envoiCrt.getIdEnvoi();
                    getMemoryLog().logMessage(
                            "Erreur survenue lors de la génération de "
                                    + envoiDS.getField(LEEnvoiDataSource.CS_ETAPE_SUIVANTE) + param,
                            FWMessage.INFORMATION, "Etapes suivante processus");
                    getMemoryLog().logMessage(e.getMessage(), FWMessage.INFORMATION, "Etapes suivante processus");
                    JadeLogger.info(
                            "Erreur survenue lors de la génération de "
                                    + envoiDS.getField(LEEnvoiDataSource.CS_ETAPE_SUIVANTE) + param, e);
                    LEJournalHandler journalHandler = new LEJournalHandler();
                    journalHandler.annulerLot(idLot, getSession(), getTransaction());
                    continue;
                }
                try {
                    if (nbreMaxPageParDoc <= export.size()) {
                        export.exportReport();
                        this.registerAttachedDocument(export.getExportNewFilePath());
                        export.deleteAll();
                    }
                    incProgressCounter();
                } catch (Exception e) {
                    getMemoryLog().logMessage(e.getMessage(), FWMessage.INFORMATION, "Etapes suivante processus");
                    getMemoryLog().logMessage("Echec de génération du document, recommencer la génération",
                            FWMessage.INFORMATION, "Etapes suivante processus");
                    JadeLogger.info("Echec de génération du document, recommencer la génération", e);
                    LEJournalHandler journalHandler = new LEJournalHandler();
                    journalHandler.annulerLot(idLot, getSession(), getTransaction());
                }
            }
            if (sendMailToAffiliee) {
                getAttachedDocuments().addAll(jadePublishDocuments);
            }
            listeEtapes.cursorClose(statement);
            // imprimer l'ensemble des documents
            if (nbDoc > 0) {
                try {
                    String nbDocument = "500";
                    try {
                        nbDocument = ((LEApplication) getSession().getApplication()).getProperty(
                                "nbDocumentPourFusion", nbDocument);
                    } catch (Exception e) {
                        nbDocument = "100";
                    }
                    try {
                        JadePublishDocumentInfo docInfo = createDocumentInfo();
                        docInfo.setDocumentTypeNumber(retrieveDocumentTypeNumber());

                        this.mergePDF(docInfo, getSimulation(), Integer.parseInt(nbDocument), false, null);
                    } catch (Exception e) {
                        getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL,
                                "globaz.leo.process.LEGenererEtapesSuivantes");
                    }
                } catch (Exception e) {
                    getMemoryLog().logMessage(e.getMessage(), FWMessage.INFORMATION, "Etapes suivante processus");
                    getMemoryLog().logMessage("Echec de génération du document, recommencer la génération",
                            FWMessage.INFORMATION, "Etapes suivante processus");
                    JadeLogger.info("Echec de génération du document, recommencer la génération", e);
                    LEJournalHandler journalHandler = new LEJournalHandler();
                    journalHandler.annulerLot(idLot, getSession(), getTransaction());
                }
            } else {
                getMemoryLog().logMessage("Aucun document trouvé pour les critères sélectionnés.",
                        FWMessage.INFORMATION, "Processus : Générer étapes suivantes");
            }
            // 6701 : log du nombre de document
            getMemoryLog().logMessage(getSession().getLabel("NBRE_ETAPE") + nbreEtape, FWMessage.INFORMATION,
                    "Etapes suivante processus");
            return true;
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.INFORMATION, "Etapes suivante processus");
            JadeLogger.error(this, e);
            this._addError(getTransaction(), e.getMessage());
            // une erreur est survenue, on doit supprimer tout ce qu'on a
            // ajouter
            //
            e.printStackTrace();
            return false;
        }
    }

    private void sendMail(LEEtapesSuivantesViewBean envoiCrt, TexteGiverBabelWithVariables textGiver,
            JadePublishDocument doc, String mail) throws Exception, FileNotFoundException, IOException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("dateRappel", envoiCrt.getDateRappel());
        map.put("destinataire", envoiCrt.getDestinataire());
        map.put("name", envoiCrt.getTiers().getNom());
        map.put("numAffilie", envoiCrt.getLibelle());
        map.put("lien", "{lien}");
        textGiver.setLangue(envoiCrt.getTiers().getLangue());
        byte[] file = FileUtils.readFileToByteArray(new File(doc.getDocumentLocation()));
        ServicesProviders.mailServiceProvide(getSession()).sendMailWithFile(mail,
                textGiver.resolveText(TexteMailRappel.SUBJECT, map), textGiver.resolveText(TexteMailRappel.BODY, map),
                envoiCrt.getLibelle(), "0091CDS", file, envoiCrt.getLibelle().replace(".", "") + "0091CDS.pdf",
                FileExtension.PDF);

    }

    @Override
    protected void _validate() throws Exception {

        // Date obligatoire
        if (JadeStringUtil.isBlank(getDatePriseEnCompte())) {
            this._addError(getTransaction(), getSession().getLabel("OBLIGATOIRE_DATE"));
        }

        // Catégorie obligatoire
        if (JadeStringUtil.isBlank(getCategorie())) {
            this._addError(getTransaction(), getSession().getLabel("OBLIGATOIRE_CATEGORIE"));
        }

        // Formule obligatoire
        if ((getCsFormule().size() == 0) || JadeStringUtil.isBlank("" + getCsFormule().get(0))) {
            this._addError(getTransaction(), getSession().getLabel("OBLIGATOIRE_FORMULE"));
        }

    }

    /**
     * @return
     */
    public String getCategorie() {
        return categorie;
    }

    /**
     * @return
     */
    public String getCommentaire() {
        return commentaire;
    };

    /**
     * @return
     */
    public ArrayList getCsFormule() {
        return csFormule;
    }

    /**
     * @return
     */
    public String getDateImpression() {
        return dateImpression;
    }

    /**
     * @return
     */
    public String getDatePriseEnCompte() {
        return datePriseEnCompte;
    }

    @Override
    protected String getEMailObject() {
        return "Résultat du processus générer les étapes suivantes";
    }

    public int getNbDoc() {
        return nbDoc;
    }

    /**
     * @return
     */
    public ArrayList getOrderBy() {
        return orderBy;
    }

    /**
     * @return
     */
    public String getOrderBy1() {
        return orderBy1;
    }

    /**
     * @return
     */
    public String getOrderBy2() {
        return orderBy2;
    }

    /**
     * @return
     */
    public boolean getSimulation() {
        return isSimulation;
    }

    /**
     * @return
     */
    public boolean isWantOrder() {
        return wantOrder;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    private String retrieveDocumentTypeNumber() {

        // On ne récupère le document type number uniquement si la formule et la catégorie sont précisés
        if ((getCsFormule().size() == 1) && (getCsFormule().get(0) != "")
                && (JadeStringUtil.isBlank(getCategorie()) == false)) {
            List<?> originalDocumentList = getAttachedDocuments();
            if (originalDocumentList.size() > 0) {
                JadePublishDocument publishDoc = (JadePublishDocument) originalDocumentList.get(0);
                JadePublishDocumentInfo docInfoTemp = publishDoc.getPublishJobDefinition().getDocumentInfo();
                return docInfoTemp.getDocumentTypeNumber();
            }
        }

        return "";
    }

    /**
     * @param string
     */
    public void setCategorie(String string) {
        categorie = string;
    }

    /**
     * @param string
     */
    public void setCommentaire(String string) {
        commentaire = string;
    }

    /**
     * @param list
     */
    public void setCsFormule(ArrayList list) {
        csFormule = list;
    }

    /**
     * @param string
     */
    public void setDateImpression(String string) {
        dateImpression = string;
    }

    /**
     * @param string
     */
    public void setDatePriseEnCompte(String string) {
        datePriseEnCompte = string;
    }

    public void setNbDoc(int nbDoc) {
        this.nbDoc = nbDoc;
    }

    /**
     * @param list
     */
    public void setOrderBy(ArrayList list) {
        orderBy = list;
    }

    /**
     * @param string
     */
    public void setOrderBy1(String string) {
        orderBy1 = string;
    }

    /**
     * @param string
     */
    public void setOrderBy2(String string) {
        orderBy2 = string;
    }

    /**
     * @param b
     */
    public void setSimulation(boolean b) {
        isSimulation = b;
    }

    /**
     * @param b
     */
    public void setWantOrder(boolean b) {
        wantOrder = b;
    }

    public void setMapNumerosAffilieMail(Map<String, String> mapNumerosAffilieMail) {
        this.mapNumerosAffilieMail = mapNumerosAffilieMail;
    }

    public void setSendMailToAffiliee(boolean sendMailToAffiliee) {
        this.sendMailToAffiliee = sendMailToAffiliee;
    }

    public Collection<String> getForNumerosAffilie() {
        return forNumerosAffilie;
    }

    public void setForNumerosAffilie(Collection<String> forNumerosAffilie) {
        this.forNumerosAffilie = forNumerosAffilie;
    }

    public boolean isSendMailToAffiliee() {
        return sendMailToAffiliee;
    }

    public Map<String, String> getMapNumerosAffilieMail() {
        return mapNumerosAffilieMail;
    }

    public List<String> getListEmailEtNumAffDesRappelsEnvoyes() {
        return listEmailEtNumAffDesRappelsEnvoyes;
    }
}
