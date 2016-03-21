package globaz.naos.process.taxeCo2;

import globaz.globall.db.BProcess;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.db.taxeCo2.AFTaxeCo2;
import globaz.naos.db.taxeCo2.AFTaxeCo2Manager;
import globaz.naos.listes.excel.util.AFExcelmlUtils;
import globaz.naos.listes.excel.util.IAFListeColumns;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import globaz.webavs.common.CommonExcelmlContainer;
import globaz.webavs.common.op.CommonExcelDataContainer;
import globaz.webavs.common.op.CommonExcelDataContainer.CommonLine;
import globaz.webavs.common.op.CommonExcelDocumentParser;
import globaz.webavs.common.op.CommonExcelFilterNotSupportedException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author JPA
 * @since 01 juillet 2010
 */
public class AFReinjectionTaxeCo2Process extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String NUM_INFOROM_NAME_SPACE = "headerNumInforom";
    private String fileName = null;
    private boolean isAtLessOneError = false;
    private boolean status = true;

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        setSendMailOnError(true);

        String headerAnnee = "";
        CommonExcelmlContainer errorContainer = new CommonExcelmlContainer();
        CommonExcelDataContainer container = null;
        String path = Jade.getInstance().getHomeDir() + "work/" + getFileName();

        // copy du fichier dans le répertoire work pour le traiter
        JadeFsFacade.copyFile("jdbc://" + Jade.getInstance().getDefaultJdbcSchema() + "/" + getFileName(), path);

        // parse du document, retourne un container avec toutes les entrées et
        // valueres trouvées dans le document
        try {
            container = CommonExcelDocumentParser.parseWorkBook(AFExcelmlUtils.loadPath(path));

        } catch (CommonExcelFilterNotSupportedException e) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_FICHIER_REINJECTION_FILTRE"));
            String messageInformation = "Unsupported filtered Excel file injection throw exception in Parser due to file :"
                    + getFileName() + "\n";
            messageInformation += AFUtil.stack2string(e);
            AFUtil.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());
            status = false;
        } catch (Exception e) {

            this._addError(getTransaction(), getSession().getLabel("ERREUR_FICHIER_REINJECTION"));

            String messageInformation = "Nom du fichier : " + getFileName() + "\n";
            messageInformation += AFUtil.stack2string(e);

            AFUtil.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

            status = false;
        }

        // Si le fichier a pu etre parsé, on continu
        if (status) {
            try {
                String numInforom = container.getHeaderValue(AFReinjectionTaxeCo2Process.NUM_INFOROM_NAME_SPACE);

                if (JadeStringUtil.isEmpty(numInforom)) {
                    getTransaction().addErrors(getSession().getLabel("NUM_INFOROM_NOT_FOUND"));
                    isAtLessOneError = true;
                    return false;
                } else if (!(numInforom.equals(AFListeExcelTaxeCo2Process.NUMERO_INFOROM) || numInforom
                        .equals(AFListeExcelRadieTaxeCo2Process.NUMERO_INFOROM))) {
                    getTransaction().addErrors(getSession().getLabel("NUM_INFOROM_NOT_IMPLEMENTED"));
                    isAtLessOneError = true;
                    return false;
                }

                Iterator<CommonLine> lines = container.returnLinesIterator();
                setProgressScaleValue(container.getSize());

                // On récupère les infos de l'entête
                headerReinjectionTaxeCo2(errorContainer, container, numInforom);

                // On itère sur chaque ligne
                while (lines.hasNext()) {
                    incProgressCounter();

                    CommonLine line = lines.next();
                    HashMap<String, String> lineMap = line.returnLineHashMap();

                    // On test quelle liste on doit réinjecter
                    reinjectionTaxeCo2(errorContainer, getTransaction(), line, lineMap);

                    if (isAborted()) {
                        return false;
                    }
                }

                if (isAtLessOneError) {
                    // On imprime la liste des erreurs suivant le document que l'on traite
                    if (numInforom.equals(AFListeExcelRadieTaxeCo2Process.NUMERO_INFOROM)) {
                        impressionListeRadieTaxeCo2(errorContainer);
                    } else if (numInforom.equals(AFListeExcelTaxeCo2Process.NUMERO_INFOROM)) {
                        impressionListeTaxeCo2(errorContainer);
                    }
                }
            } catch (Exception e) {

                this._addError(getTransaction(), getSession().getLabel("ERREUR_REINJECTION"));

                String messageInformation = "Nom du fichier : " + getFileName() + "\n";
                messageInformation += AFUtil.stack2string(e);

                AFUtil.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

                status = false;
            }
        }

        return status;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || !status) {
            return getSession().getLabel("REINJECTION_SUJET_ERROR");
        } else {
            if (isAtLessOneError) {
                return getSession().getLabel("REINJECTION_SUJET");
            } else {
                return getSession().getLabel("REINJECTION_SUJET_OK");
            }
        }
    }

    public String getFileName() {
        return fileName;
    }

    private void headerReinjectionTaxeCo2(CommonExcelmlContainer errorContainer, CommonExcelDataContainer container,
            String numInforom) {
        if (!JadeStringUtil.isEmpty(numInforom)) {
            errorContainer.put(IAFListeColumns.HEADER_NUM_INFOROM, numInforom);
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(IAFListeColumns.HEADER_NOM_LISTE))) {
            errorContainer.put(IAFListeColumns.HEADER_NOM_LISTE,
                    container.getHeaderValue(IAFListeColumns.HEADER_NOM_LISTE));
        } else {
            errorContainer.put(IAFListeColumns.HEADER_NOM_LISTE, "");
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(IAFListeColumns.HEADER_ANNEE))) {
            errorContainer.put(IAFListeColumns.HEADER_ANNEE, container.getHeaderValue(IAFListeColumns.HEADER_ANNEE));
        } else {
            errorContainer.put(IAFListeColumns.HEADER_ANNEE, "");
        }
    }

    private void impressionListeRadieTaxeCo2(CommonExcelmlContainer errorContainer) throws Exception, IOException {
        // On imprime le document avec les lignes en erreur
        // On génère le doc
        String nomDoc = getSession().getLabel("LISTE_RADIE_TAXE_CO2");
        String docPath = AFExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                + AFListeExcelRadieTaxeCo2Process.MODEL_REINJECTION_NAME, nomDoc, errorContainer);
        publicationDocument(nomDoc, docPath);
    }

    private void impressionListeTaxeCo2(CommonExcelmlContainer errorContainer) throws Exception, IOException {
        // On imprime le document avec les lignes en erreur
        // On génère le doc
        String nomDoc = getSession().getLabel("LISTE_TAXE_CO2");
        String docPath = AFExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                + AFListeExcelTaxeCo2Process.MODEL_REINJECTION_NAME, nomDoc, errorContainer);
        publicationDocument(nomDoc, docPath);
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    private void publicationDocument(String nomDoc, String docPath) throws IOException {
        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        this.registerAttachedDocument(docInfo, docPath);
    }

    private AFTaxeCo2 rechercheTaxeExistante(String idTaxe, BTransaction transaction) throws Exception {

        AFTaxeCo2 taxe = new AFTaxeCo2();

        AFTaxeCo2Manager taxeMana = new AFTaxeCo2Manager();
        taxeMana.setSession(getSession());
        taxeMana.setForTaxeCo2Id(idTaxe);
        taxeMana.find(transaction);

        if (taxeMana.size() <= 0) {
            // Le controle n'est pas trouvé, on retourne null
            taxe = null;
        } else {
            taxe = (AFTaxeCo2) taxeMana.getFirstEntity();
        }

        return taxe;
    }

    private void reinjectionTaxeCo2(CommonExcelmlContainer errorContainer, BTransaction transaction, CommonLine line,
            HashMap<String, String> lineMap) throws Exception {
        // On recherche si la taxe existe
        // d'après l'id de la taxe
        String idTaxe = returnValeurHashMapWithNumAffilie(IAFListeColumns.ID_TAXE, lineMap, transaction, "");
        String etat = returnValeurHashMapWithNumAffilie(IAFListeColumns.ETAT, lineMap, transaction, "");

        if (!transaction.hasErrors()) {
            AFTaxeCo2 taxe = rechercheTaxeExistante(idTaxe, transaction);

            if (taxe != null) {
                if (etat.equals(getSession().getLabel("ETAT_TAXE_CO2_ABANDONNE"))) {
                    taxe.setEtat(CodeSystem.ETAT_TAXE_CO2_ABANDONNE);
                } else if (etat.equals(getSession().getLabel("ETAT_TAXE_CO2_FACTURE"))) {
                    taxe.setEtat(CodeSystem.ETAT_TAXE_CO2_FACTURE);
                } else {
                    taxe.setEtat(CodeSystem.ETAT_TAXE_CO2_A_TRAITER);
                }
                taxe.update(transaction);

            } else if (!transaction.hasErrors()) {
                AFUtil.fillNaosContainerWithAFLine(errorContainer, line, IAFListeColumns.listeNoms, transaction
                        .getErrors().toString(), IAFListeColumns.ERREUR);
                transaction.clearErrorBuffer();
                transaction.rollback();
                isAtLessOneError = true;
            }
        }

        // Commit si aucune erreur est présente, sinon on le notifie
        if (transaction.hasErrors()) {
            AFUtil.fillNaosContainerWithAFLine(errorContainer, line, IAFListeColumns.listeNoms, transaction.getErrors()
                    .toString(), IAFListeColumns.ERREUR);
            transaction.clearErrorBuffer();
            transaction.rollback();
            isAtLessOneError = true;
        } else {
            transaction.commit();
        }
    }

    private String returnValeurHashMap(String valeur, HashMap<String, String> lineMap, BTransaction transaction) {
        if (lineMap.containsKey(valeur)) {
            return lineMap.get(valeur);
        } else {
            transaction.addErrors(valeur + " " + getSession().getLabel("DOIT_ETRE_RENSEIGNE"));
            return "";
        }
    }

    private String returnValeurHashMapCouldBeEmpty(String valeur, HashMap<String, String> lineMap) {
        if (lineMap.containsKey(valeur)) {
            return lineMap.get(valeur);
        } else {
            return "";
        }
    }

    // *******************************************************
    // Getter
    // ***************************************************

    private String returnValeurHashMapWithNumAffilie(String valeur, HashMap<String, String> lineMap,
            BTransaction transaction, String numAffilie) {
        String valeurRetour = "";

        if (lineMap.containsKey(valeur)) {
            valeurRetour = lineMap.get(valeur);
        } else {
            transaction.addErrors(valeur + " " + getSession().getLabel("DOIT_ETRE_RENSEIGNE_POUR") + " " + numAffilie);
        }

        return valeurRetour;
    }

    // *******************************************************
    // Setter
    // ***************************************************

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
