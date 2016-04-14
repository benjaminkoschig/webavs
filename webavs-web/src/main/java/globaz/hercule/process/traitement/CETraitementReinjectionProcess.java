package globaz.hercule.process.traitement;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.helios.tools.TimeHelper;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEControleEmployeurManager;
import globaz.hercule.db.reviseur.CEReviseur;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.mappingXmlml.ICEListeColumns;
import globaz.hercule.process.CEEmployeurRadieProcess;
import globaz.hercule.process.CEEmployeurSansPersonnelProcess;
import globaz.hercule.process.CEListeControlesAEffectuerProcess;
import globaz.hercule.process.CEListeControlesPrevusProcess;
import globaz.hercule.process.controleEmployeur.CEDsNonRemiseProcess;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.hercule.utils.CEExcelmlUtils;
import globaz.hercule.utils.CEUtils;
import globaz.hercule.utils.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.webavs.common.CommonExcelmlContainer;
import globaz.webavs.common.op.CommonExcelDataContainer;
import globaz.webavs.common.op.CommonExcelDataContainer.CommonLine;
import globaz.webavs.common.op.CommonExcelDocumentParser;
import globaz.webavs.common.op.CommonExcelFilterNotSupportedException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author JPA
 * @since 01 juillet 2010
 */
public class CETraitementReinjectionProcess extends BProcess {

    private static final long serialVersionUID = 3931781036438170445L;
    private static final String NUM_INFOROM_NAME_SPACE = "headerNumInforom";
    private String fileName = null;
    private boolean isAtLessOneError = false;
    private boolean status = true;

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        setSendMailOnError(true);

        String headerAnnee = "";
        CommonExcelmlContainer errorContainer = new CommonExcelmlContainer();
        CommonExcelDataContainer container = null;
        String path = Jade.getInstance().getHomeDir() + "work/" + getFileName();

        // copy du fichier dans le r�pertoire work pour le traiter
        JadeFsFacade.copyFile("jdbc://" + Jade.getInstance().getDefaultJdbcSchema() + "/" + getFileName(), path);

        // parse du document, retourne un container avec toutes les entr�es et
        // valueres trouv�es dans le document
        try {
            container = CommonExcelDocumentParser.parseWorkBook(CEExcelmlUtils.loadPath(path));
        } catch (CommonExcelFilterNotSupportedException e) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_FICHIER_REINJECTION_FILTRE"));
            String messageInformation = "Unsupported filtered Excel file injection throw exception in Parser due to file :"
                    + getFileName() + "\n";
            messageInformation += CEUtils.stack2string(e);
            CEUtils.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());
            status = false;
        } catch (Exception e) {

            this._addError(getTransaction(), getSession().getLabel("ERREUR_FICHIER_REINJECTION"));

            String messageInformation = "Nom du fichier : " + getFileName() + "\n";
            messageInformation += CEUtils.stack2string(e);

            CEUtils.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

            status = false;
        }

        // Si le fichier a pu etre pars�, on continu
        if (status) {
            try {
                String numInforom = container.getHeaderValue(CETraitementReinjectionProcess.NUM_INFOROM_NAME_SPACE);

                if (JadeStringUtil.isEmpty(numInforom)) {
                    getTransaction().addErrors(getSession().getLabel("NUM_INFOROM_NOT_FOUND"));
                    isAtLessOneError = true;
                    return false;
                } else if (!(numInforom.equals(CEListeControlesAEffectuerProcess.NUMERO_INFOROM)
                        || numInforom.equals(CEEmployeurSansPersonnelProcess.NUMERO_INFOROM)
                        || numInforom.equals(CEEmployeurRadieProcess.NUMERO_INFOROM)
                        || numInforom.equals(CEDsNonRemiseProcess.NUMERO_INFOROM) || numInforom
                            .equals(CEListeControlesPrevusProcess.NUMERO_INFOROM))) {
                    getTransaction().addErrors(getSession().getLabel("NUM_INFOROM_NOT_IMPLEMENTED"));
                    isAtLessOneError = true;
                    return false;
                }

                Iterator<CommonLine> lines = container.returnLinesIterator();
                setProgressScaleValue(container.getSize());

                // Date, heure et visa
                errorContainer.put(ICEListeColumns.HEADER_DATE_VISA, TimeHelper.getCurrentTime() + " - "
                        + getSession().getUserName());
                if (numInforom.equals(CEListeControlesAEffectuerProcess.NUMERO_INFOROM)) {
                    headerReinjectionControlesAEffectuer(errorContainer, container, numInforom);
                } else if (numInforom.equals(CEEmployeurSansPersonnelProcess.NUMERO_INFOROM)) {
                    headerReinjectionEmployeurSansPersonnel(errorContainer, container, numInforom);
                } else if (numInforom.equals(CEEmployeurRadieProcess.NUMERO_INFOROM)) {
                    headerReinjectionEmployeurRadie(errorContainer, container, numInforom);
                } else if (numInforom.equals(CEDsNonRemiseProcess.NUMERO_INFOROM)) {
                    headerAnnee = headerReinjectionDsStructNonRemises(errorContainer, container, numInforom);
                } else if (numInforom.equals(CEListeControlesPrevusProcess.NUMERO_INFOROM)) {
                    headerAnnee = headerReinjectionControlesPrevus(errorContainer, container, numInforom);
                }

                // On it�re sur chaque ligne
                while (lines.hasNext()) {
                    incProgressCounter();

                    CommonLine line = lines.next();
                    HashMap<String, String> lineMap = line.returnLineHashMap();

                    // On test quelle liste on doit r�injecter
                    if (numInforom.equals(CEListeControlesAEffectuerProcess.NUMERO_INFOROM)) {
                        reinjectionControlesAEffectuer(errorContainer, getTransaction(), line, lineMap);
                    } else if (numInforom.equals(CEEmployeurSansPersonnelProcess.NUMERO_INFOROM)) {
                        reinjectionEmployeurSansPersonnel(errorContainer, getTransaction(), line, lineMap);
                    } else if (numInforom.equals(CEEmployeurRadieProcess.NUMERO_INFOROM)) {
                        reinjectionEmployeurRadie(errorContainer, getTransaction(), line, lineMap);
                    } else if (numInforom.equals(CEDsNonRemiseProcess.NUMERO_INFOROM)) {
                        reinjectionDsStructNonRemises(errorContainer, getTransaction(), line, lineMap, headerAnnee);
                    } else if (numInforom.equals(CEListeControlesPrevusProcess.NUMERO_INFOROM)) {
                        reinjectionControlesPrevus(errorContainer, getTransaction(), line, lineMap);
                    }

                    if (isAborted()) {
                        return false;
                    }
                }

                if (isAtLessOneError) {
                    // On imprime la liste suivant le document que l'on traite
                    if (numInforom.equals(CEListeControlesAEffectuerProcess.NUMERO_INFOROM)) {
                        impressionListeControlesAEffectuerReinjection(errorContainer);
                    } else if (numInforom.equals(CEEmployeurSansPersonnelProcess.NUMERO_INFOROM)) {
                        impressionListeControlesSansPersonnel(errorContainer);
                    } else if (numInforom.equals(CEEmployeurRadieProcess.NUMERO_INFOROM)) {
                        impressionListeControlesRadie(errorContainer);
                    } else if (numInforom.equals(CEDsNonRemiseProcess.NUMERO_INFOROM)) {
                        impressionListeDsNonRetourne(errorContainer);
                    } else if (numInforom.equals(CEListeControlesPrevusProcess.NUMERO_INFOROM)) {
                        impressionListeControlesPrevus(errorContainer);
                    }
                }
            } catch (Exception e) {

                this._addError(getTransaction(), getSession().getLabel("ERREUR_REINJECTION"));

                String messageInformation = "Nom du fichier : " + getFileName() + "\n";
                messageInformation += CEUtils.stack2string(e);

                CEUtils.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

                status = false;
            }
        }

        return status;
    }

    private String calculDateDebutControle(final String dateDebutControle) throws ParseException {
        String date = dateDebutControle.substring(13, 23);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date dateFormated = dateFormat.parse(date);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dateFormated);
        cal.add(Calendar.DAY_OF_MONTH, 1);

        return dateFormat.format(cal.getTime());
    }

    private boolean controleIfExisteControleNonEffectuePrevuAvecDateChevauchante(final CEControleEmployeur newControle,
            final BTransaction transaction) {
        CEControleEmployeurManager manager = new CEControleEmployeurManager();
        manager.setSession(getSession());
        manager.setForNumAffilie(newControle.getNumAffilie());
        manager.setForNotDateEffective(true);
        manager.setOrderBy(" MDDCFI DESC ");
        manager.setForActif(true);
        try {
            manager.find(transaction, BManager.SIZE_NOLIMIT);
            for (int i = 0; i < manager.size(); i++) {
                CEControleEmployeur controle = (CEControleEmployeur) manager.getEntity(i);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                if (((dateFormat.parse(newControle.getDateFinControle())).getTime() > (dateFormat.parse(controle
                        .getDateDebutControle())).getTime())
                        && ((dateFormat.parse(newControle.getDateDebutControle())).getTime() < (dateFormat
                                .parse(controle.getDateFinControle())).getTime())) {
                    // p�riode chevauchante
                    transaction.addErrors(getSession().getLabel("PERIODE_CHEVAUCHANTE"));
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            transaction.addErrors(getSession().getLabel("PERIODE_CHEVAUCHANTE"));
            return true;
        }
    }

    private JADate dateExcelToJADate(final String dateExcel) {

        JADate date = null;

        if (JadeStringUtil.isEmpty(dateExcel)) {
            return null;
        }

        try {
            date = new JADate(dateExcel.substring(8, 10) + dateExcel.substring(5, 7) + dateExcel.substring(0, 4));
        } catch (Exception e) {
            this._addError(getTransaction(), getSession().getLabel("ERROR_CONVERSION_DATE"));
            return null;
        }

        return date;
    }

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

    private void headerReinjectionControlesAEffectuer(final CommonExcelmlContainer errorContainer,
            final CommonExcelDataContainer container, final String numInforom) {
        if (!JadeStringUtil.isEmpty(numInforom)) {
            errorContainer.put(ICEListeColumns.HEADER_NUM_INFOROM, numInforom);
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICEListeColumns.HEADER_ANNEE))) {
            errorContainer.put(ICEListeColumns.HEADER_ANNEE, container.getHeaderValue(ICEListeColumns.HEADER_ANNEE));
        } else {
            errorContainer.put(ICEListeColumns.HEADER_ANNEE, "");
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICEListeColumns.HEADER_GENRE))) {
            errorContainer.put(ICEListeColumns.HEADER_GENRE, container.getHeaderValue(ICEListeColumns.HEADER_GENRE));
        } else {
            errorContainer.put(ICEListeColumns.HEADER_GENRE, "");
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICEListeColumns.HEADER_ANNEE_COMPTEUR))) {
            errorContainer.put(ICEListeColumns.HEADER_ANNEE_COMPTEUR,
                    container.getHeaderValue(ICEListeColumns.HEADER_ANNEE_COMPTEUR));
        } else {
            errorContainer.put(ICEListeColumns.HEADER_ANNEE_COMPTEUR, "");
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICEListeColumns.HEADER_MASSE_1))) {
            errorContainer
                    .put(ICEListeColumns.HEADER_MASSE_1, container.getHeaderValue(ICEListeColumns.HEADER_MASSE_1));
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICEListeColumns.HEADER_MASSE_2))) {
            errorContainer
                    .put(ICEListeColumns.HEADER_MASSE_2, container.getHeaderValue(ICEListeColumns.HEADER_MASSE_2));
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICEListeColumns.HEADER_MASSE_3))) {
            errorContainer
                    .put(ICEListeColumns.HEADER_MASSE_3, container.getHeaderValue(ICEListeColumns.HEADER_MASSE_3));
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICEListeColumns.HEADER_MASSE_4))) {
            errorContainer
                    .put(ICEListeColumns.HEADER_MASSE_4, container.getHeaderValue(ICEListeColumns.HEADER_MASSE_4));
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICEListeColumns.HEADER_MASSE_5))) {
            errorContainer
                    .put(ICEListeColumns.HEADER_MASSE_5, container.getHeaderValue(ICEListeColumns.HEADER_MASSE_5));
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICEListeColumns.HEADER_NB_CI_1))) {
            errorContainer
                    .put(ICEListeColumns.HEADER_NB_CI_1, container.getHeaderValue(ICEListeColumns.HEADER_NB_CI_1));
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICEListeColumns.HEADER_NB_CI_2))) {
            errorContainer
                    .put(ICEListeColumns.HEADER_NB_CI_2, container.getHeaderValue(ICEListeColumns.HEADER_NB_CI_2));
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICEListeColumns.HEADER_NB_CI_3))) {
            errorContainer
                    .put(ICEListeColumns.HEADER_NB_CI_3, container.getHeaderValue(ICEListeColumns.HEADER_NB_CI_3));
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICEListeColumns.HEADER_NB_CI_4))) {
            errorContainer
                    .put(ICEListeColumns.HEADER_NB_CI_4, container.getHeaderValue(ICEListeColumns.HEADER_NB_CI_4));
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICEListeColumns.HEADER_NB_CI_5))) {
            errorContainer
                    .put(ICEListeColumns.HEADER_NB_CI_5, container.getHeaderValue(ICEListeColumns.HEADER_NB_CI_5));
        }
        errorContainer.put(ICEListeColumns.HEADER_BLANK_1, "");
        errorContainer.put(ICEListeColumns.HEADER_BLANK_2, "");
        errorContainer.put(ICEListeColumns.HEADER_BLANK_3, "");
    }

    private String headerReinjectionControlesPrevus(final CommonExcelmlContainer errorContainer,
            final CommonExcelDataContainer container, final String numInforom) {
        if (!JadeStringUtil.isEmpty(numInforom)) {
            errorContainer.put(ICEListeColumns.HEADER_NUM_INFOROM, numInforom);
        }
        String headerAnnee = container.getHeaderValue(ICEListeColumns.HEADER_ANNEE);

        CEExcelmlUtils.remplirColumn(errorContainer, ICEListeColumns.HEADER_ANNEE, headerAnnee, "");
        errorContainer.put(ICEListeColumns.HEADER_BLANK_1, "");

        return headerAnnee;
    }

    private String headerReinjectionDsStructNonRemises(final CommonExcelmlContainer errorContainer,
            final CommonExcelDataContainer container, final String numInforom) {
        String headerAnnee = "";
        if (!JadeStringUtil.isEmpty(numInforom)) {
            errorContainer.put(ICEListeColumns.HEADER_NUM_INFOROM, numInforom);
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICEListeColumns.HEADER_ANNEE))) {
            headerAnnee = container.getHeaderValue(ICEListeColumns.HEADER_ANNEE);
            errorContainer.put(ICEListeColumns.HEADER_ANNEE, headerAnnee);
        } else {
            errorContainer.put(ICEListeColumns.HEADER_ANNEE, "");
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICEListeColumns.HEADER_MASSE_SALARIALE))) {
            errorContainer.put(ICEListeColumns.HEADER_MASSE_SALARIALE,
                    container.getHeaderValue(ICEListeColumns.HEADER_MASSE_SALARIALE));
        } else {
            errorContainer.put(ICEListeColumns.HEADER_MASSE_SALARIALE, "");
        }
        errorContainer.put(ICEListeColumns.HEADER_BLANK_1, "");
        errorContainer.put(ICEListeColumns.HEADER_BLANK_2, "");
        errorContainer.put(ICEListeColumns.HEADER_BLANK_3, "");
        return headerAnnee;
    }

    private void headerReinjectionEmployeurRadie(final CommonExcelmlContainer errorContainer,
            final CommonExcelDataContainer container, final String numInforom) {
        if (!JadeStringUtil.isEmpty(numInforom)) {
            errorContainer.put(ICEListeColumns.HEADER_NUM_INFOROM, numInforom);
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICEListeColumns.HEADER_COLONNE_MASSE_SALARIALE))) {
            errorContainer.put(ICEListeColumns.HEADER_COLONNE_MASSE_SALARIALE,
                    container.getHeaderValue(ICEListeColumns.HEADER_COLONNE_MASSE_SALARIALE));
        } else {
            errorContainer.put(ICEListeColumns.HEADER_COLONNE_MASSE_SALARIALE, "");
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICEListeColumns.HEADER_DATE_RADIATION))) {
            errorContainer.put(ICEListeColumns.HEADER_DATE_RADIATION,
                    container.getHeaderValue(ICEListeColumns.HEADER_DATE_RADIATION));
        } else {
            errorContainer.put(ICEListeColumns.HEADER_DATE_RADIATION, "");
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICEListeColumns.HEADER_MASSE_SALARIALE))) {
            errorContainer.put(ICEListeColumns.HEADER_MASSE_SALARIALE,
                    container.getHeaderValue(ICEListeColumns.HEADER_MASSE_SALARIALE));
        } else {
            errorContainer.put(ICEListeColumns.HEADER_MASSE_SALARIALE, "");
        }

        errorContainer.put(ICEListeColumns.HEADER_BLANK_1, "");
        errorContainer.put(ICEListeColumns.HEADER_BLANK_2, "");
        errorContainer.put(ICEListeColumns.HEADER_BLANK_3, "");
    }

    private void headerReinjectionEmployeurSansPersonnel(final CommonExcelmlContainer errorContainer,
            final CommonExcelDataContainer container, final String numInforom) {
        if (!JadeStringUtil.isEmpty(numInforom)) {
            errorContainer.put(ICEListeColumns.HEADER_NUM_INFOROM, numInforom);
        }
        if (!JadeStringUtil.isEmpty(container.getHeaderValue(ICEListeColumns.HEADER_ANNEE))) {
            errorContainer.put(ICEListeColumns.HEADER_ANNEE, container.getHeaderValue(ICEListeColumns.HEADER_ANNEE));
        } else {
            errorContainer.put(ICEListeColumns.HEADER_ANNEE, "");
        }
        errorContainer.put(ICEListeColumns.HEADER_BLANK_1, "");
        errorContainer.put(ICEListeColumns.HEADER_BLANK_2, "");
        errorContainer.put(ICEListeColumns.HEADER_BLANK_3, "");
    }

    private void impressionListeControlesAEffectuerReinjection(final CommonExcelmlContainer errorContainer)
            throws Exception, IOException {
        // On imprime le document avec les lignes en erreur
        // On g�n�re le doc
        String nomDoc = getSession().getLabel("LISTE_CONTROLE_EFFECTUER_NOM_DOCUMENT");
        String docPath = CEExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                + CEListeControlesAEffectuerProcess.MODEL_REINJECTION_NAME, nomDoc, errorContainer);
        publicationDocument(nomDoc, docPath);
    }

    private void impressionListeControlesPrevus(final CommonExcelmlContainer errorContainer) throws Exception,
            IOException {
        // On imprime le document avec les lignes en erreur
        // On g�n�re le doc
        String nomDoc = getSession().getLabel(CEListeControlesPrevusProcess.NOM_DOCUMENT);
        String docPath = CEExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                + CEListeControlesPrevusProcess.MODEL_REINJECTION_NAME, nomDoc, errorContainer);
        publicationDocument(nomDoc, docPath);
    }

    private void impressionListeControlesRadie(final CommonExcelmlContainer errorContainer) throws Exception,
            IOException {
        // On imprime le document avec les lignes en erreur
        // On g�n�re le doc
        String nomDoc = getSession().getLabel("LISTE_EMPLOYEUR_RADIE_NOM_DOCUMENT");
        String docPath = CEExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                + CEEmployeurRadieProcess.MODEL_REINJECTION_NAME, nomDoc, errorContainer);
        publicationDocument(nomDoc, docPath);
    }

    private void impressionListeControlesSansPersonnel(final CommonExcelmlContainer errorContainer) throws Exception,
            IOException {
        // On imprime le document avec les lignes en erreur
        // On g�n�re le doc
        String nomDoc = getSession().getLabel("LISTE_EMPLOYEUR_SANS_PERSONNEL_NOM_DOCUMENT");
        String docPath = CEExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                + CEEmployeurSansPersonnelProcess.MODEL_REINJECTION_NAME, nomDoc, errorContainer);
        publicationDocument(nomDoc, docPath);
    }

    private void impressionListeDsNonRetourne(final CommonExcelmlContainer errorContainer) throws Exception,
            IOException {
        // On imprime le document avec les lignes en erreur
        // On g�n�re le doc
        String nomDoc = getSession().getLabel("LISTE_DS_NON_REMISE_NOM_DOCUMENT");
        String docPath = CEExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                + CEDsNonRemiseProcess.MODEL_REINJECTION_NAME, nomDoc, errorContainer);
        publicationDocument(nomDoc, docPath);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    private void publicationDocument(final String nomDoc, final String docPath) throws IOException {
        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(CEApplication.DEFAULT_APPLICATION_HERCULE);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        this.registerAttachedDocument(docInfo, docPath);
    }

    /**
     * Recherche d'un controle existant
     * 
     * @param numAffilie
     * @param premierePeriode
     * @param dernierePeriode
     * @param transaction
     * @return
     * @throws Exception
     */
    private CEControleEmployeur rechercheControleExistant(final String numAffilie, final String premierePeriode,
            final String dernierePeriode, final BTransaction transaction) throws Exception {

        CEControleEmployeur controle = null;

        CEControleEmployeurManager manager = new CEControleEmployeurManager();
        manager.setSession(getSession());
        manager.setForNumAffilie(numAffilie);
        manager.setForDateDebutControle(premierePeriode);
        manager.setForDateFinControle(dernierePeriode);
        manager.setForNotDateEffective(true);
        manager.find(transaction, BManager.SIZE_USEDEFAULT);

        if (manager.size() > 1) {
            // Il existe plus d'un controle pour cet affili�, on log
            transaction.addErrors("Il existe plus d'un contr�le pour cet affili� : " + numAffilie);
            controle = null;
        } else if (manager.size() <= 0) {
            // Le controle n'est pas trouv�, on retourne null
            controle = null;
        } else {
            controle = (CEControleEmployeur) manager.getFirstEntity();
        }

        return controle;
    }

    private void reinjectionControlesAEffectuer(final CommonExcelmlContainer errorContainer,
            final BTransaction transaction, final CommonLine line, final HashMap<String, String> lineMap)
            throws Exception {
        // On recherche si le contr�le existe
        // d'apr�s le num. d'affili�, la 1�re p�riode, la derni�re p�riode, la
        // date effective vide
        String numAffilie = returnValeurHashMapWithNumAffilie(ICEListeColumns.NUM_AFFILIE, lineMap, transaction, "");

        JADate premierePeriode = dateExcelToJADate(returnValeurHashMapWithNumAffilie(ICEListeColumns.PREMIERE_PERIODE,
                lineMap, transaction, numAffilie));
        JADate dernierePeriode = dateExcelToJADate(returnValeurHashMapWithNumAffilie(ICEListeColumns.DERNIERE_PERIODE,
                lineMap, transaction, numAffilie));

        if (!transaction.hasErrors()) {
            CEControleEmployeur controle = rechercheControleExistant(numAffilie, premierePeriode.toStr("."),
                    dernierePeriode.toStr("."), transaction);

            if (controle != null) {
                // Comme le controle existe on va mettre � jour le r�viseur
                // On check si le r�viseur existe, sinon on renvoie un mail avec
                // les contr�les qui n'ont pas pu �tre ins�r�s!
                String visaReviseur = returnValeurHashMapWithNumAffilie(ICEListeColumns.REVISEUR, lineMap, transaction,
                        numAffilie);
                if (JadeStringUtil.isEmpty(visaReviseur)) {
                    transaction.addErrors(getSession().getLabel("REINJECTION_ERROR_CONTROLE_EXISTE_SANS_REVISEUR")
                            + " : " + numAffilie);
                }
                String idReviseur = retrieveIdReviseur(getSession(), transaction, visaReviseur);
                if (!JadeStringUtil.isBlankOrZero(idReviseur)) {
                    controle.setIdReviseur(idReviseur);
                    controle.update(transaction);
                } else {
                    transaction.addErrors(getSession().getLabel("REINJECTION_REVISEUR_INCONNU") + " : " + numAffilie);
                }

            } else if (!transaction.hasErrors()) {
                // Comme le contr�le n'est pas existant on l'ins�re en BD
                CEControleEmployeur newControle = new CEControleEmployeur();
                newControle.setSession(getSession());
                newControle
                        .setDatePrevue(dateExcelToJADate(
                                returnValeurHashMapWithNumAffilie(ICEListeColumns.DATE_PREVUE, lineMap, transaction,
                                        numAffilie)).toStr("."));
                newControle.setNumAffilie(returnValeurHashMapWithNumAffilie(ICEListeColumns.NUM_AFFILIE, lineMap,
                        transaction, numAffilie));
                newControle.setDateDebutControle(premierePeriode.toStr("."));
                newControle.setDateFinControle(dernierePeriode.toStr("."));
                String genreControleCS = CEUtils.getCsGenreControle(
                        returnValeurHashMapWithNumAffilie(ICEListeColumns.TYPE_CONTROLE, lineMap, transaction,
                                numAffilie), getSession());
                if (JadeStringUtil.isEmpty(genreControleCS)) {
                    transaction.addErrors(getSession().getLabel("GENRE_CONTROLE_INCONNU") + " : " + numAffilie);
                } else {
                    newControle.setGenreControle(genreControleCS);
                }

                // Gestion du reviseur
                String visaReviseur = returnValeurHashMapCouldBeEmpty(ICEListeColumns.REVISEUR, lineMap);
                if (!JadeStringUtil.isEmpty(visaReviseur)) {
                    String idReviseur = retrieveIdReviseur(getSession(), transaction, visaReviseur);
                    if (!JadeStringUtil.isBlankOrZero(idReviseur)) {
                        newControle.setIdReviseur(idReviseur);
                    }
                }

                // Controle
                if (!controleIfExisteControleNonEffectuePrevuAvecDateChevauchante(newControle, transaction)) {
                    newControle.add(transaction);
                }
            }
        }

        // Commit si aucune erreur est pr�sente, sinon on le notifie
        if (transaction.hasErrors()) {
            CEUtils.fillContainerWithCommonLine(errorContainer, line, ICEListeColumns.listeNoms, transaction
                    .getErrors().toString(), ICEListeColumns.ERREUR);
            transaction.clearErrorBuffer();
            transaction.rollback();
            isAtLessOneError = true;
        } else {
            transaction.commit();
        }
    }

    /**
     * R�sinjection des controles pr�vus
     * 
     * @param errorContainer
     * @param transaction
     * @param line
     * @param lineMap
     * @throws Exception
     */
    private void reinjectionControlesPrevus(final CommonExcelmlContainer errorContainer,
            final BTransaction transaction, final CommonLine line, final HashMap<String, String> lineMap)
            throws Exception {

        CEControleEmployeur controle = null;
        String visaReviseur = null;

        // Recup�ration du numero d'affili�
        String numAffilie = returnValeurHashMapWithNumAffilie(ICEListeColumns.NUM_AFFILIE, lineMap, transaction, "");

        // R�cup�ration de la p�riode de controle
        String premierePeriode = "";
        String dernierePeriode = "";
        if (!transaction.hasErrors()) {
            String periode = returnValeurHashMapWithNumAffilie(ICEListeColumns.PERIODE_CONTROLEE, lineMap, transaction,
                    numAffilie);
            String[] result = periode.split("-");
            premierePeriode = result[0];
            dernierePeriode = result[1];
        }

        // R�cup�ration du contr�le
        if (!transaction.hasErrors()) {
            controle = rechercheControleExistant(numAffilie, premierePeriode, dernierePeriode, transaction);
            if (controle == null) {
                transaction.addErrors(getSession().getLabel("REINJECTION_ERROR_CONTROLE_NON_EXISTANT") + " "
                        + numAffilie);
            }
        }

        // R�cup�ration du visa r�viseur
        if (!transaction.hasErrors()) {
            visaReviseur = returnValeurHashMapWithNumAffilie(ICEListeColumns.REVISEUR, lineMap, transaction, numAffilie);
        }

        // Si on a un controle et un r�viseur, on update
        if ((controle != null) && !transaction.hasErrors() && !JadeStringUtil.isEmpty(visaReviseur)) {
            // Comme le controle existe on va mettre � jour le r�viseur
            // On recherche le reviseur
            CEReviseur reviseur = CEControleEmployeurService.retrieveReviseur(getSession(), transaction, visaReviseur);
            if (reviseur != null) {
                controle.setIdReviseur(reviseur.getIdReviseur());
                controle.update(transaction);
            } else {
                // Le reviseur est null, on l'a pas trouv�.
                transaction.addErrors(getSession().getLabel("REINJECTION_REVISEUR_INCONNU") + " : " + numAffilie);
            }
        }

        // Commit si aucune erreur est pr�sente, sinon on le notifie
        if (transaction.hasErrors()) {
            CEUtils.fillContainerWithCommonLine(errorContainer, line, ICEListeColumns.listeNoms, transaction
                    .getErrors().toString(), ICEListeColumns.ERREUR);
            transaction.clearErrorBuffer();
            transaction.rollback();
            isAtLessOneError = true;
        } else {
            transaction.commit();
        }
    }

    private void reinjectionDsStructNonRemises(final CommonExcelmlContainer errorContainer,
            final BTransaction transaction, final CommonLine line, final HashMap<String, String> lineMap,
            final String anneeImpression) throws Exception {

        String numAffilie = returnValeurHashMapWithNumAffilie(ICEListeColumns.NUM_AFFILIE, lineMap, transaction, "");

        if (!transaction.hasErrors()) {

            // On va cr�er le contr�le
            CEControleEmployeur newControle = new CEControleEmployeur();
            newControle.setSession(getSession());

            // La date de fin de controle correspond � l'ann�e pr�vue-1
            String anneePrevue = returnValeurHashMapWithNumAffilie(ICEListeColumns.ANNEE_PREVUE, lineMap, transaction,
                    numAffilie);
            if (!JadeStringUtil.isBlankOrZero(anneePrevue)) {
                String dateFinControle = "31.12." + CEUtils.getAnneePrecedente(anneePrevue);
                newControle.setDateFinControle(dateFinControle);

                // La date de d�but de controle correspond soit � la date de
                // d'impression - 4 ou � la date de d�but d'affiliation

                int int_annee_precedente = CEUtils.transformeStringToInt(anneeImpression);
                int annee_en_arriere = 4;
                if (int_annee_precedente <= 2011) {
                    annee_en_arriere = 3;
                }

                JADate dateDebutAffiliation = dateExcelToJADate(returnValeurHashMapCouldBeEmpty(
                        ICEListeColumns.DEBUT_PERIODE_AFFILIATION, lineMap));
                newControle.setDateDebutControle("01.01."
                        + CEUtils.getAnneePrecedente(annee_en_arriere, anneeImpression));

                if (dateDebutAffiliation != null) {

                    // Si l'ann�e de d�but d'affiliation est sup�rieur � l'ann�e
                    // d'Impression on prend la date de d�but d'affiliation
                    if (dateDebutAffiliation.getYear() >= Integer.valueOf(CEUtils.getAnneePrecedente(annee_en_arriere,
                            anneeImpression))) {
                        newControle.setDateDebutControle(dateDebutAffiliation.toStr("."));
                    }
                }

                newControle.setDatePrevue("01.01." + anneePrevue);
                newControle.setGenreControle(CodeSystem.TYPE_CONTROLE_DS_NON_REMISE);

                // Comme le contr�le n'est pas existant on l'ins�re en BD
                newControle.setNumAffilie(numAffilie);

                // Gestion du reviseur
                String visaReviseur = returnValeurHashMapCouldBeEmpty(ICEListeColumns.REVISEUR, lineMap);
                if (!JadeStringUtil.isEmpty(visaReviseur)) {
                    String idReviseur = retrieveIdReviseur(getSession(), transaction, visaReviseur);
                    if (!JadeStringUtil.isBlankOrZero(idReviseur)) {
                        newControle.setIdReviseur(idReviseur);
                    } else {
                        transaction.addErrors(getSession().getLabel("REINJECTION_REVISEUR_INCONNU") + " : "
                                + visaReviseur);
                    }
                }

                // Controle
                if (!controleIfExisteControleNonEffectuePrevuAvecDateChevauchante(newControle, transaction)) {
                    newControle.add(transaction);
                }
            }
        }

        // Commit si aucune erreur est pr�sente, sinon on le notifie
        if (transaction.hasErrors()) {
            CEUtils.fillContainerWithCommonLine(errorContainer, line, ICEListeColumns.listeNoms, transaction
                    .getErrors().toString(), ICEListeColumns.ERREUR);
            transaction.clearErrorBuffer();
            transaction.rollback();
            isAtLessOneError = true;
        } else {
            transaction.commit();
        }

    }

    private void reinjectionEmployeurRadie(final CommonExcelmlContainer errorContainer, final BTransaction transaction,
            final CommonLine line, final HashMap<String, String> lineMap) throws Exception {
        String numAffilie = returnValeurHashMapWithNumAffilie(ICEListeColumns.NUM_AFFILIE, lineMap, transaction, "");
        if (!transaction.hasErrors()) {
            // On va cr�er le contr�le
            CEControleEmployeur newControle = new CEControleEmployeur();
            newControle.setSession(getSession());

            // La date de fin de controle correspond � la date de radiation qui
            // est obligatoire !
            JADate dateFinControle = dateExcelToJADate(returnValeurHashMapWithNumAffilie(
                    ICEListeColumns.DATE_RADIATION, lineMap, transaction, numAffilie));
            newControle.setDateFinControle(dateFinControle.toStr("."));

            // La date de d�but de controle correspond soit � la date de
            // derni�re p�riode control� ou � la date d'affiliation
            String dateDebutControle = returnValeurHashMapCouldBeEmpty(ICEListeColumns.PERIODE_CONTROLEE, lineMap);
            if (!JadeStringUtil.isEmpty(dateDebutControle)) {

                // La p�riode contr�l� est renseign�e, il faut donc en d�duire
                // la date de fin
                newControle.setDateDebutControle(calculDateDebutControle(dateDebutControle));
            } else {
                // Sinon on prend la date de d�but d'affiliation
                JADate dateDeb = dateExcelToJADate(returnValeurHashMapCouldBeEmpty(ICEListeColumns.DATE_AFFILIATION,
                        lineMap));
                newControle.setDateDebutControle(dateDeb.toStr("."));
            }
            String anneePrevue = returnValeurHashMapWithNumAffilie(ICEListeColumns.ANNEE_PREVUE, lineMap, transaction,
                    numAffilie);
            if (anneePrevue.length() > 4) {
                anneePrevue = anneePrevue.substring(0, 4);
            }
            newControle.setDatePrevue("01.01." + anneePrevue);
            newControle.setGenreControle(CodeSystem.TYPE_CONTROLE_EMPLOYEUR_RADIE);
            // Comme le contr�le n'est pas existant on l'ins�re en BD
            newControle.setNumAffilie(numAffilie);

            // Gestion reviseur
            String visaReviseur = returnValeurHashMapCouldBeEmpty(ICEListeColumns.REVISEUR, lineMap);
            if (!JadeStringUtil.isEmpty(visaReviseur)) {
                String idReviseur = retrieveIdReviseur(getSession(), transaction, visaReviseur);
                if (!JadeStringUtil.isBlankOrZero(idReviseur)) {
                    newControle.setIdReviseur(idReviseur);
                } else {
                    transaction.addErrors(getSession().getLabel("REINJECTION_REVISEUR_INCONNU") + " : " + visaReviseur);
                }
            }

            if (new JADate(newControle.getDateFinControle()).toInt() <= new JADate(newControle.getDateDebutControle())
                    .toInt()) {
                transaction.addErrors(getSession().getLabel("CONTROLE_EXISTANT_COUVRE_RAD") + " : " + numAffilie);
            }

            // Controle
            if (!transaction.hasErrors()) {
                if (!controleIfExisteControleNonEffectuePrevuAvecDateChevauchante(newControle, transaction)) {
                    newControle.add(transaction);
                }
            }
        }

        // Commit si aucune erreur est pr�sente, sinon on le notifie
        if (transaction.hasErrors()) {
            CEUtils.fillContainerWithCommonLine(errorContainer, line, ICEListeColumns.listeNoms, transaction
                    .getErrors().toString(), ICEListeColumns.ERREUR);
            transaction.clearErrorBuffer();
            transaction.rollback();
            isAtLessOneError = true;
        } else {
            transaction.commit();
        }

    }

    private void reinjectionEmployeurSansPersonnel(final CommonExcelmlContainer errorContainer,
            final BTransaction transaction, final CommonLine line, final HashMap<String, String> lineMap)
            throws Exception {
        String numAffilie = returnValeurHashMapWithNumAffilie(ICEListeColumns.NUM_AFFILIE, lineMap, transaction, "");
        if (!transaction.hasErrors()) {

            // On va cr�er le contr�le
            CEControleEmployeur newControle = new CEControleEmployeur();
            newControle.setSession(getSession());
            JADate dateFinControle = dateExcelToJADate(returnValeurHashMapWithNumAffilie(
                    ICEListeColumns.SANS_PERSONNEL_DEPUIS, lineMap, transaction, numAffilie));
            newControle.setDateFinControle(dateFinControle.toStr("."));

            // On r�cup�re la derni�re p�riode de contr�le + 1 ou la date de
            // d�but d'affiliation, le cas �ch�ant
            String dateDebutControle = returnValeurHashMapCouldBeEmpty(ICEListeColumns.PERIODE_CONTROLEE, lineMap);
            if (!JadeStringUtil.isEmpty(dateDebutControle)) {

                // La p�riode contr�l� est renseign�e, il faut donc en d�duire
                // la date de fin
                newControle.setDateDebutControle(calculDateDebutControle(dateDebutControle));
            } else {

                // Sinon on prend la date de d�but d'affiliation
                dateDebutControle = returnValeurHashMapCouldBeEmpty(ICEListeColumns.PERIODE_AFFILIATION, lineMap);
                newControle.setDateDebutControle(dateDebutControle.substring(0, 10));
            }
            String anneePrevue = returnValeurHashMapWithNumAffilie(ICEListeColumns.ANNEE_PREVUE, lineMap, transaction,
                    numAffilie);
            newControle.setDatePrevue("01.01." + anneePrevue);
            newControle.setGenreControle(CodeSystem.TYPE_CONTROLE_SANS_PERSONNEL);
            // Comme le contr�le n'est pas existant on l'ins�re en BD
            newControle.setNumAffilie(numAffilie);

            // Traitement du reviseur
            String visaReviseur = returnValeurHashMapCouldBeEmpty(ICEListeColumns.REVISEUR, lineMap);
            if (!JadeStringUtil.isEmpty(visaReviseur)) {
                String idReviseur = retrieveIdReviseur(getSession(), transaction, visaReviseur);
                if (!JadeStringUtil.isBlankOrZero(idReviseur)) {
                    newControle.setIdReviseur(idReviseur);
                } else {
                    transaction.addErrors(getSession().getLabel("REINJECTION_REVISEUR_INCONNU") + " : " + visaReviseur);
                }
            }

            // Controle
            if (!controleIfExisteControleNonEffectuePrevuAvecDateChevauchante(newControle, transaction)) {
                newControle.add(transaction);
            }
        }

        // Commit si aucune erreur est pr�sente, sinon on le notifie
        if (transaction.hasErrors()) {
            CEUtils.fillContainerWithCommonLine(errorContainer, line, ICEListeColumns.listeNoms, transaction
                    .getErrors().toString(), ICEListeColumns.ERREUR);
            transaction.clearErrorBuffer();
            transaction.rollback();
            isAtLessOneError = true;
        } else {
            transaction.commit();
        }

    }

    private String retrieveIdReviseur(final BSession session, final BTransaction transaction, final String visaReviseur)
            throws HerculeException {
        CEReviseur reviseur = CEControleEmployeurService.retrieveReviseur(session, transaction, visaReviseur);
        if (reviseur != null) {
            return reviseur.getIdReviseur();
        }
        return null;
    }

    private String returnValeurHashMapCouldBeEmpty(final String valeur, final HashMap<String, String> lineMap) {
        if (lineMap.containsKey(valeur)) {
            return lineMap.get(valeur);
        } else {
            return "";
        }
    }

    private String returnValeurHashMapWithNumAffilie(final String valeur, final HashMap<String, String> lineMap,
            final BTransaction transaction, final String numAffilie) {
        String valeurRetour = "";

        if (lineMap.containsKey(valeur)) {
            valeurRetour = lineMap.get(valeur);
        } else {
            transaction.addErrors(valeur + " " + getSession().getLabel("DOIT_ETRE_RENSEIGNE_POUR") + " " + numAffilie);
        }

        return valeurRetour;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

}
