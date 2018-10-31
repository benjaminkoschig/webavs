/**
 *
 */
package ch.globaz.amal.businessimpl.services.models.detailfamille;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import ch.globaz.amal.business.calcul.CalculsSubsidesContainer;
import ch.globaz.amal.business.calcul.CalculsTaxationContainer;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.envois.AMEnvoiData;
import ch.globaz.amal.business.envois.AMEnvoiDataFactory;
import ch.globaz.amal.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.amal.business.exceptions.models.controleurEnvoi.ControleurEnvoiException;
import ch.globaz.amal.business.exceptions.models.controleurJob.ControleurJobException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.exceptions.models.documents.DocumentException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.exceptions.models.parametreModel.ParametreModelException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.annonce.SimpleAnnonce;
import ch.globaz.amal.business.models.annonce.SimpleAnnonceSearch;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.contribuable.SimpleContribuable;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurAnnonceDetail;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurAnnonceDetailSearch;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetail;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetailSearch;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatus;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatusSearch;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJob;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJobSearch;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.business.models.documents.SimpleDocument;
import ch.globaz.amal.business.models.documents.SimpleDocumentSearch;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplication;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplicationSearch;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplex;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplexSearch;
import ch.globaz.amal.business.models.revenu.Revenu;
import ch.globaz.amal.business.models.revenu.RevenuFullComplex;
import ch.globaz.amal.business.models.revenu.RevenuFullComplexSearch;
import ch.globaz.amal.business.models.revenu.RevenuHistorique;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplexSearch;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueSearch;
import ch.globaz.amal.business.models.revenu.RevenuSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.models.detailfamille.DetailFamilleService;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.utils.AMGestionTiers;
import ch.globaz.envoi.business.exceptions.models.parametrageEnvoi.FormuleListException;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleList;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleListSearch;
import ch.globaz.envoi.business.services.ENServiceLocator;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.parameters.FWParametersCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JADate;
import globaz.globall.util.JAVector;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.ged.target.JadeGedTargetProperties;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.model.common.Node;
import globaz.op.common.model.document.Document;
import globaz.op.wordml.model.document.WordmlDocument;
import globaz.op.wordml.model.element.WWordDocument;

/**
 * @author DHI
 *
 */
public class DetailFamilleServiceImpl implements DetailFamilleService {

    /**
     * Contrôle la validité du supplément famille accordé
     *
     * @param newSubside
     * @param newSubsides
     * @return
     */
    private SimpleDetailFamille checkSupplementFamille(SimpleDetailFamille newSubside,
            ArrayList<SimpleDetailFamille> calculSubsides) {

        ArrayList<SimpleDetailFamille> subsidesEnfants = new ArrayList<SimpleDetailFamille>();
        boolean needSuppl = true;
        boolean oneChildIsOK = false;

        // ----------------------------------------------------------------------------------------------------
        // 1) Recherche dans les subsides du calcul en cours les subsides avec les mêmes périodes de début et fin
        // qui appartiennent à des enfants
        // ----------------------------------------------------------------------------------------------------
        for (SimpleDetailFamille subsideEnfant : calculSubsides) {
            // check que cela ne soit pas le subside courant du père/mère
            if (!subsideEnfant.getIdFamille().equals(newSubside.getIdFamille())) {
                String idFamilleEnfant = subsideEnfant.getIdFamille();
                try {
                    SimpleFamille familleEnfant = AmalImplServiceLocator.getSimpleFamilleService()
                            .read(idFamilleEnfant);
                    if (familleEnfant.isEnfant()) {
                        subsidesEnfants.add(subsideEnfant);
                        if (newSubside.getFinDroit() == null) {
                            newSubside.setFinDroit("");
                        }
                        if (subsideEnfant.getFinDroit() == null) {
                            subsideEnfant.setFinDroit("");
                        }
                        // Contrôle des périodes
                        if (subsideEnfant.getDebutDroit().equals(newSubside.getDebutDroit())
                                && subsideEnfant.getFinDroit().equals(newSubside.getFinDroit())) {
                            // contrôle si pas refus et document différent de ATENFx
                            if (subsideEnfant.getRefus() == false) {
                                if (subsideEnfant.getNoModeles()
                                        .equals(IAMCodeSysteme.AMDocumentModeles.ATENF1.getValue())) {
                                    needSuppl = false;
                                } else if (subsideEnfant.getNoModeles()
                                        .equals(IAMCodeSysteme.AMDocumentModeles.ATENF2.getValue())) {
                                    needSuppl = false;
                                } else if (subsideEnfant.getNoModeles()
                                        .equals(IAMCodeSysteme.AMDocumentModeles.ATENF8.getValue())) {
                                    needSuppl = false;
                                } else if (JadeStringUtil.isBlankOrZero(subsideEnfant.getNoModeles())) {
                                    needSuppl = false;
                                } else {
                                    oneChildIsOK = true;
                                }
                            } else {
                                needSuppl = false;
                            }
                        } else {
                            needSuppl = false;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    needSuppl = false;
                }
            }
        }

        // ----------------------------------------------------------------------------------------------------
        // 2) Recherche dans les subsides existants les subsides avec les mêmes périodes de début et fin
        // qui appartiennent à des enfants
        // ----------------------------------------------------------------------------------------------------
        if (subsidesEnfants.size() <= 0) {
            SimpleDetailFamilleSearch searchEnfants = new SimpleDetailFamilleSearch();
            searchEnfants.setForDebutDroit(newSubside.getDebutDroit());
            searchEnfants.setForFinDroit(newSubside.getFinDroit());
            searchEnfants.setForCodeActif(true);
            searchEnfants.setForAnneeHistorique(newSubside.getAnneeHistorique());
            searchEnfants.setForIdContribuable(newSubside.getIdContribuable());
            try {
                searchEnfants = AmalImplServiceLocator.getSimpleDetailFamilleService().search(searchEnfants);
                for (int iEnfant = 0; iEnfant < searchEnfants.getSize(); iEnfant++) {
                    SimpleDetailFamille subsideEnfant = (SimpleDetailFamille) searchEnfants.getSearchResults()[iEnfant];
                    // check que cela ne soit pas le subside courant du père/mère
                    if (!subsideEnfant.getIdFamille().equals(newSubside.getIdFamille())) {
                        String idFamilleEnfant = subsideEnfant.getIdFamille();
                        try {
                            SimpleFamille familleEnfant = AmalImplServiceLocator.getSimpleFamilleService()
                                    .read(idFamilleEnfant);
                            if (familleEnfant.isEnfant()) {
                                subsidesEnfants.add(subsideEnfant);
                                // Contrôle des périodes
                                if (subsideEnfant.getDebutDroit().equals(newSubside.getDebutDroit())
                                        && subsideEnfant.getFinDroit().equals(newSubside.getFinDroit())) {
                                    // contrôle si pas refus et document différent de ATENFx
                                    if (subsideEnfant.getRefus() == false) {
                                        if (subsideEnfant.getNoModeles()
                                                .equals(IAMCodeSysteme.AMDocumentModeles.ATENF1.getValue())) {
                                            needSuppl = false;
                                        } else if (subsideEnfant.getNoModeles()
                                                .equals(IAMCodeSysteme.AMDocumentModeles.ATENF2.getValue())) {
                                            needSuppl = false;
                                        } else if (subsideEnfant.getNoModeles()
                                                .equals(IAMCodeSysteme.AMDocumentModeles.ATENF8.getValue())) {
                                            needSuppl = false;
                                        } else if (JadeStringUtil.isBlankOrZero(subsideEnfant.getNoModeles())) {
                                            needSuppl = false;
                                        } else {
                                            oneChildIsOK = true;
                                        }
                                    } else {
                                        needSuppl = false;
                                    }
                                } else {
                                    needSuppl = false;
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            needSuppl = false;
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                needSuppl = false;
            }
        }

        // ----------------------------------------------------------------------------------------------------
        // 3) Ne devrait jamais arrivé. Si aucun subside enfant trouvé, suppl à 0
        // ----------------------------------------------------------------------------------------------------
        if (!oneChildIsOK) {
            newSubside.setSupplExtra("0");
        }

        return newSubside;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.amal.business.services.models.detailFamille.DetailFamilleService#count(ch.globaz.amal.business.models
     * .detailfamille.SimpleDetailFamille)
     */
    @Override
    public int count(SimpleDetailFamilleSearch search) throws DetailFamilleException, JadePersistenceException {
        if (search == null) {
            throw new DetailFamilleException("Unable to search dossier, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.amal.business.services.models.detailFamille.DetailFamilleService#create(ch.globaz.amal.business.models
     * .detailfamille.SimpleDetailFamille)
     */
    @Override
    public SimpleDetailFamille create(SimpleDetailFamille detailFamille)
            throws JadePersistenceException, DetailFamilleException {
        if (detailFamille == null) {
            throw new DetailFamilleException("Unable to create detailFamille the given model is null!");
        }
        try {
            return AmalImplServiceLocator.getSimpleDetailFamilleService().create(detailFamille);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DetailFamilleException("Service not available - " + e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.amal.business.services.models.detailFamille.DetailFamilleService#delete(ch.globaz.amal.business.models
     * .detailfamille.SimpleDetailFamille)
     */
    @Override
    public SimpleDetailFamille delete(SimpleDetailFamille detailFamille) throws DetailFamilleException,
            JadePersistenceException, DocumentException, JadeApplicationServiceNotAvailableException {
        if (detailFamille == null) {
            throw new DetailFamilleException("Unable to delete detailFamille the given model is null!");
        }
        return AmalImplServiceLocator.getSimpleDetailFamilleService().delete(detailFamille);
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.models.detailFamille.DetailFamilleService#essaiAjax(String)
     */
    @Override
    public ArrayList<String> essaiAjax(String idDetailFamille) {
        ArrayList<String> returnList = new ArrayList<String>();
        returnList.add("1-Hello ajax " + idDetailFamille);
        returnList.add("2-Hello ajax " + idDetailFamille);
        return returnList;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.amal.business.services.models.detailfamille.DetailFamilleService#generateDocumentCorrespondance(java
     * .lang.String, java.lang.String)
     */
    @Override
    public HashMap<String, Object> generateDocumentCorrespondance(String idDossier, String idFormule) {
        // ---------------------------------------------------------------
        // Préparation de la hashmap de retour
        // ---------------------------------------------------------------
        HashMap<String, Object> returnMap = new HashMap<String, Object>();
        String msgError = "";
        String msgWarning = "";
        // --------------------------------------------------------------------
        // Récupération des attributs de la formule
        // --------------------------------------------------------------------
        boolean bFormuleWord = false;
        Boolean bError = false;
        FormuleList currentFormule = null;
        try {
            FormuleListSearch formuleListSearch = new FormuleListSearch();
            formuleListSearch.setForIdFormule(idFormule);
            formuleListSearch.setDefinedSearchSize(0);
            formuleListSearch = ENServiceLocator.getFormuleListService().search(formuleListSearch);

            if (formuleListSearch.getSize() > 0) {
                currentFormule = (FormuleList) formuleListSearch.getSearchResults()[0];
                if (currentFormule.getDefinitionformule().getCsManuAuto().equals("42001100")) {
                    bFormuleWord = true;
                }
            } else {
                msgError = "Cannot retrieve formule parametres for id : " + idFormule;
                bError = true;
            }
        } catch (Exception ex) {
            msgError = "Exception raised when trying to generate the document : " + ex.toString();
            ex.printStackTrace();
            JadeThread.logError(toString(), ex.getMessage());
            bError = true;
        }

        if (!bError) {
            if (bFormuleWord) {
                // --------------------------------------------------------------------
                // DOCUMENT CORRESPONDANCE WORD
                // --------------------------------------------------------------------
                returnMap = generateWordFile(idDossier, idFormule, currentFormule.getCsDocument());
            } else {
                try {
                    // --------------------------------------------------------------------
                    // DOCUMENT CORRESPONDANCE TOPAZ
                    // --------------------------------------------------------------------
                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss");
                    String csDateComplete = sdf.format(date);
                    AmalServiceLocator.getDetailFamilleService().writeInJobTable(csDateComplete,
                            currentFormule.getCsDocument(), IAMCodeSysteme.AMJobType.JOBMANUALQUEUED.getValue(),
                            idDossier, 0);
                } catch (Exception ex) {
                    msgError = "Exception raised when trying to generate topaz document : " + ex.toString();
                    ex.printStackTrace();
                    JadeThread.logError(toString(), ex.getMessage());
                    bError = true;
                }
                if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.WARN)) {
                    for (int iMessage = 0; iMessage < JadeThread
                            .logMessagesOfLevel(JadeBusinessMessageLevels.WARN).length; iMessage++) {
                        msgWarning += "\n" + JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.WARN)[iMessage]
                                .getContents(JadeThread.currentLanguage());
                    }
                }
                if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
                    for (int iMessage = 0; iMessage < JadeThread
                            .logMessagesOfLevel(JadeBusinessMessageLevels.ERROR).length; iMessage++) {
                        msgError += "\n" + JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.ERROR)[iMessage]
                                .getContents(JadeThread.currentLanguage());
                    }
                    try {
                        JadeThread.rollbackSession();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        JadeThread.commitSession();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                JadeThread.logClear();
                returnMap.put("warning", msgWarning);
                returnMap.put("error", msgError);
                returnMap.put("document", "");
            }
            return returnMap;
        } else {
            if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.WARN)) {
                for (int iMessage = 0; iMessage < JadeThread
                        .logMessagesOfLevel(JadeBusinessMessageLevels.WARN).length; iMessage++) {
                    msgWarning += "\n" + JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.WARN)[iMessage]
                            .getContents(JadeThread.currentLanguage());
                }
            }
            if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
                for (int iMessage = 0; iMessage < JadeThread
                        .logMessagesOfLevel(JadeBusinessMessageLevels.ERROR).length; iMessage++) {
                    msgError += "\n" + JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.ERROR)[iMessage]
                            .getContents(JadeThread.currentLanguage());
                }
            }
            JadeThread.logClear();
            returnMap.put("warning", msgWarning);
            returnMap.put("error", msgError);
            return returnMap;
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.models.detailFamille.DetailFamilleService#generateSubside(ch.globaz
     * .amal.business.models .detailfamille.SimpleDetailFamille)
     */
    @Override
    public void generateSubside(CalculsSubsidesContainer calculs, Boolean isRecalculUnfavorable)
            throws DetailFamilleException, JadePersistenceException, RevenuException,
            JadeApplicationServiceNotAvailableException, DocumentException, ControleurEnvoiException, AnnonceException,
            ControleurJobException, FamilleException, ParametreModelException, FormuleListException {

        this.generateSubside(calculs, IAMCodeSysteme.AMJobType.JOBMANUALQUEUED.getValue(), isRecalculUnfavorable);

    }

    @Override
    public void generateSubside(CalculsSubsidesContainer calculs, String csTypeJob)
            throws DetailFamilleException, JadePersistenceException, RevenuException,
            JadeApplicationServiceNotAvailableException, DocumentException, ControleurEnvoiException, AnnonceException,
            ControleurJobException, FamilleException, ParametreModelException, FormuleListException {
        this.generateSubside(calculs, csTypeJob, false);
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.models.detailFamille.DetailFamilleService#generateSubside(ch.globaz
     * .amal.business.models .detailfamille.SimpleDetailFamille)
     */
    @Override
    public void generateSubside(CalculsSubsidesContainer calculs, String csTypeJob, Boolean isRecalculUnfavorable)
            throws DetailFamilleException, JadePersistenceException, RevenuException,
            JadeApplicationServiceNotAvailableException, DocumentException, ControleurEnvoiException, AnnonceException,
            ControleurJobException, FamilleException, ParametreModelException, FormuleListException {

        // ------------------------------------------------------------------------------------------
        // 1) CHECK TYPE DEMANDE
        // SI TYPE DEMANDE == 'D' 'S' >> CHECK SI REVENU EXISTANT, SINON CREATION
        // ------------------------------------------------------------------------------------------
        if (!JadeStringUtil.isEmpty(calculs.getTypeDemande())
                && !calculs.getTypeDemande().equals(IAMCodeSysteme.AMTypeDemandeSubside.ASSISTE.getValue())
                && !calculs.getTypeDemande().equals(IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue())) {
            generateSubsideStep1Revenu(calculs, isRecalculUnfavorable);
        }

        // ------------------------------------------------------------------------------------------
        // 2) CHECK SUBSIDE EXISTANT AVEC MEME ANNEE HISTORIQUE ET DEBUT DROIT
        // SI TROUVE, EDITION ET ANNONCE CAISSE A FALSE, SINON CREATION
        // ------------------------------------------------------------------------------------------
        ArrayList<SimpleDetailFamille> newSubsides = new ArrayList<SimpleDetailFamille>();
        for (int iSubside = 0; iSubside < calculs.getSubsides().size(); iSubside++) {
            SimpleDetailFamille currentSubside = calculs.getSubsides().get(iSubside);

            SimpleDetailFamille newSubside = generateSubsideStep2Item(currentSubside);

            // ------------------------------------------------------------------------------------------
            // 2B) CONTROLE DU SUPPLEMENT FAMILLE ET RETRAIT SI NECESSAIRE ?
            // ------------------------------------------------------------------------------------------
            if (!JadeStringUtil.isBlankOrZero(newSubside.getSupplExtra())) {
                newSubside = checkSupplementFamille(newSubside, calculs.getSubsides());
                newSubside = update(newSubside);
            }
            newSubsides.add(newSubside);
        }

        // ------------------------------------------------------------------------------------------
        // 3) DOCUMENT A METTRE EN QUEUE, PAS DANS LE SUBSIDE
        // ------------------------------------------------------------------------------------------
        // Récupération de la date du jour
        Calendar currentCalendar = Calendar.getInstance();
        Date date = currentCalendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss");
        String dateJourComplete = sdf.format(date);

        // HashMap de calcul du no de groupe
        // DECMST1,idDetailFamille
        HashMap<String, ArrayList<String>> allDocs = new HashMap<String, ArrayList<String>>();
        for (int iCurrentSubside = 0; iCurrentSubside < calculs.getSubsides().size(); iCurrentSubside++) {
            SimpleDetailFamille currentSubside = calculs.getSubsides().get(iCurrentSubside);
            if (!JadeStringUtil.isEmpty(currentSubside.getNoModeles())) {
                String currentKey = currentSubside.getNoModeles();
                String idDetailFamille = "";
                for (int iNewSubside = 0; iNewSubside < newSubsides.size(); iNewSubside++) {
                    SimpleDetailFamille newSubside = newSubsides.get(iNewSubside);
                    if (newSubside.getIdFamille().equals(currentSubside.getIdFamille())) {
                        idDetailFamille = newSubside.getIdDetailFamille();
                    }
                }
                if (!JadeStringUtil.isEmpty(idDetailFamille)) {
                    if (!allDocs.containsKey(currentKey)) {
                        ArrayList<String> ids = new ArrayList<String>();
                        ids.add(idDetailFamille);
                        allDocs.put(currentKey, ids);
                    } else {
                        allDocs.get(currentKey).add(idDetailFamille);
                    }
                }
            }
        }
        // Génération des numéros de groupes
        // Hashmap idDetailFamille, noGroupe
        HashMap<String, String> allDocsNoGroupe = new HashMap<String, String>();
        Iterator<String> allDocIterator = allDocs.keySet().iterator();
        int iCurrentDoc = 0;
        while (allDocIterator.hasNext()) {
            String currentDoc = allDocIterator.next();
            int iLimiteParPage = 6;
            iCurrentDoc++;
            // Récupération de la limite par page par document
            // ---------------------------------------------------------------
            try {
                FormuleListSearch formuleListSearch = new FormuleListSearch();
                // en fait, for libelle is for cs document...
                formuleListSearch.setForlibelle(currentDoc);
                formuleListSearch.setDefinedSearchSize(0);
                formuleListSearch = ENServiceLocator.getFormuleListService().search(formuleListSearch);
                if (formuleListSearch.getSize() == 1) {
                    FormuleList formule = (FormuleList) formuleListSearch.getSearchResults()[0];
                    // Recherche des paramètres
                    ParametreModelComplexSearch modelParametreSearch = new ParametreModelComplexSearch();
                    modelParametreSearch.setForIdFormule(formule.getId());
                    modelParametreSearch.setWhereKey("basic");
                    modelParametreSearch = AmalServiceLocator.getParametreModelService().search(modelParametreSearch);
                    if (modelParametreSearch.getSize() == 1) {
                        ParametreModelComplex model = (ParametreModelComplex) modelParametreSearch
                                .getSearchResults()[0];
                        if (model.getSimpleParametreModel().getDocumentAvecListe()) {
                            String nbElements = model.getSimpleParametreModel().getNombreElementListe();
                            iLimiteParPage = Integer.parseInt(nbElements);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            // Compte le nombre de subside concerné et set la limite par page
            ArrayList<String> subsidesDoc = allDocs.get(currentDoc);
            if (subsidesDoc.size() >= iLimiteParPage) {
                int iNbPage = subsidesDoc.size() / iLimiteParPage;
                if ((subsidesDoc.size() % iLimiteParPage) != 0) {
                    iNbPage++;
                }
                iLimiteParPage = subsidesDoc.size() / iNbPage;
                if ((subsidesDoc.size() % iNbPage) != 0) {
                    iLimiteParPage++;
                }
            }
            int iNoGroupe = ((currentCalendar.get(Calendar.HOUR) * 10000000)
                    + (currentCalendar.get(Calendar.MINUTE) * 100000) + (currentCalendar.get(Calendar.SECOND) * 1000)
                    + currentCalendar.get(Calendar.MILLISECOND));

            int incNoGroupe = 1;
            for (int iNewDetailFamille = 0; iNewDetailFamille < subsidesDoc.size(); iNewDetailFamille++) {
                if ((iNewDetailFamille != 0) && ((iNewDetailFamille % iLimiteParPage) == 0)) {
                    incNoGroupe++;
                }
                if (!allDocsNoGroupe.containsKey(subsidesDoc.get(iNewDetailFamille))) {
                    String noFinalGroupe = "" + ((iCurrentDoc * 10000000) + (incNoGroupe * 100000000) + iNoGroupe);
                    // noFinalGroupe += incNoGroupe;
                    // noFinalGroupe += noGroupe;
                    allDocsNoGroupe.put(subsidesDoc.get(iNewDetailFamille), noFinalGroupe);
                }
            }
        }

        // Inscription du document au dossier - status généré (temporaire)
        for (int iNewSubside = 0; iNewSubside < newSubsides.size(); iNewSubside++) {
            // Subsides créés ou lu
            SimpleDetailFamille newSubside = newSubsides.get(iNewSubside);
            for (int iCurrentSubside = 0; iCurrentSubside < calculs.getSubsides().size(); iCurrentSubside++) {
                // Subsides transmis en paramètre (résultat du calcul), récup no document
                SimpleDetailFamille currentSubside = calculs.getSubsides().get(iCurrentSubside);
                if (newSubside.getIdFamille().equals(currentSubside.getIdFamille())) {

                    // Inscription du document
                    generateSubsideStep3Documents(dateJourComplete, currentSubside, newSubside,
                            allDocsNoGroupe.get(newSubside.getIdDetailFamille()), csTypeJob);

                }
            }
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.amal.business.services.models.detailfamille.DetailFamilleService#generateSubsidesFromAttribution(java
     * .lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Boolean, java.lang.String)
     */
    @Override
    public void generateSubsidesFromAttribution(String idContribuable, String anneeHistorique, String typeDemande,
            String idRevenu, Boolean revenuIsTaxation, String allSubsidesAsString) {
        try {
            CalculsSubsidesContainer calculsSubsidesContainer = new CalculsSubsidesContainer();
            // //Remplace les set que faisait le submit////
            calculsSubsidesContainer.setIdContribuable(idContribuable);
            calculsSubsidesContainer.setAnneeHistorique(anneeHistorique);
            calculsSubsidesContainer.setTypeDemande(typeDemande);
            calculsSubsidesContainer.setIdRevenu(idRevenu);
            calculsSubsidesContainer.setRevenuIsTaxation(revenuIsTaxation);
            // ///
            calculsSubsidesContainer.setAllSubsidesAsString(allSubsidesAsString);
            calculsSubsidesContainer.parseAllSubsidesAsStringAndGenerateSubsides();
            // Lorsqu'on provient de l'écran Attribution, on considère que le revenu est de toute facon favorable pour
            // ne pas empêcher le changement de taxation
            Boolean isRecalculUnfavorable = false;
            this.generateSubside(calculsSubsidesContainer, isRecalculUnfavorable);
        } catch (Exception e) {
            JadeThread.logError("Erreur", e.getMessage());
        }
    }

    /**
     * 1ère étape d'enregistrement du subside - revenu
     *
     * @param calculs
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws RevenuException
     */
    private void generateSubsideStep1Revenu(CalculsSubsidesContainer calculs, Boolean isRecalculUnfavorable)
            throws RevenuException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // ----------------------------------------------------------------
        // Contrôle si taxation en paramètre
        // ----------------------------------------------------------------
        if (calculs.getRevenuIsTaxation()) {
            // Récupération de la taxation
            // ----------------------------------------------------------------
            RevenuFullComplex taxation = AmalServiceLocator.getRevenuService().readFullComplex(calculs.getIdRevenu());
            // Recherche du revenu historique actif pour l'année de calcul
            // ----------------------------------------------------------------
            RevenuHistoriqueComplexSearch revenuSearch = new RevenuHistoriqueComplexSearch();
            revenuSearch.setForIdContribuable(calculs.getIdContribuable());
            revenuSearch.setForAnneeHistorique(calculs.getAnneeHistorique());
            revenuSearch.setForRevenuActif(true);
            revenuSearch = AmalServiceLocator.getRevenuService().search(revenuSearch);
            if (revenuSearch.getSize() == 1) {
                RevenuHistoriqueComplex revenuHistorique = (RevenuHistoriqueComplex) revenuSearch.getSearchResults()[0];
                // Si le calcul est favorable OU si c'est une taxation provisoire
                if (!isRecalculUnfavorable || IAMCodeSysteme.AMTaxationType.PROVISOIRE.getValue()
                        .equals(revenuHistorique.getRevenuFullComplex().getSimpleRevenu().getTypeTaxation())) {
                    // Mise à jour avec la taxation
                    revenuHistorique.setRevenuFullComplex(taxation);
                    revenuHistorique = AmalServiceLocator.getRevenuService().update(revenuHistorique);
                } else {
                    RevenuHistoriqueComplex revenuHistoriqueUnfavorable = new RevenuHistoriqueComplex();
                    revenuHistoriqueUnfavorable.setRevenuFullComplex(taxation);
                    revenuHistoriqueUnfavorable.getSimpleRevenuHistorique()
                            .setAnneeHistorique(calculs.getAnneeHistorique());
                    revenuHistoriqueUnfavorable.getSimpleRevenuHistorique()
                            .setIdContribuable(calculs.getIdContribuable());
                    revenuHistoriqueUnfavorable.getSimpleRevenuHistorique().setCodeActif(false);
                    revenuHistoriqueUnfavorable.getSimpleRevenuHistorique().setIsRecalcul(true);
                    revenuHistoriqueUnfavorable.getSimpleRevenuHistorique().setIdRevenu(taxation.getId());
                    revenuHistoriqueUnfavorable = AmalServiceLocator.getRevenuService()
                            .create(revenuHistoriqueUnfavorable);
                }
            } else if (revenuSearch.getSize() == 0) {
                // Création du revenu historique
                // ----------------------------------------------------------------
                RevenuHistoriqueComplex revenuHistorique = new RevenuHistoriqueComplex();
                revenuHistorique.setRevenuFullComplex(taxation);
                revenuHistorique.getSimpleRevenuHistorique().setAnneeHistorique(calculs.getAnneeHistorique());
                revenuHistorique.getSimpleRevenuHistorique().setIdContribuable(calculs.getIdContribuable());
                revenuHistorique.getSimpleRevenuHistorique().setCodeActif(true);
                revenuHistorique.getSimpleRevenuHistorique().setIsRecalcul(isRecalculUnfavorable);
                revenuHistorique.getSimpleRevenuHistorique().setIdRevenu(taxation.getId());
                revenuHistorique = AmalServiceLocator.getRevenuService().create(revenuHistorique);
            } else {
                // Exception
                // ----------------------------------------------------------------
                throw new RevenuException(
                        "Plus de 1 revenu historique actif trouvé pour l'année " + calculs.getAnneeHistorique());
            }

        }
    }

    /**
     * 2ème étape d'enregistrement du subside - detailfamille (subside)
     *
     * @param subside
     * @throws JadePersistenceException
     * @throws DetailFamilleException
     */
    private SimpleDetailFamille generateSubsideStep2Item(SimpleDetailFamille subside)
            throws DetailFamilleException, JadePersistenceException {

        // ----------------------------------------------------------------
        // Subside updaté ou créé
        // ----------------------------------------------------------------
        SimpleDetailFamille newDetailFamille = null;
        // ----------------------------------------------------------------
        // Recherche d'un subside potentiel existant pour l'année + date debut + idFamille
        // ----------------------------------------------------------------
        SimpleDetailFamilleSearch currentSearch = new SimpleDetailFamilleSearch();
        currentSearch.setForAnneeHistorique(subside.getAnneeHistorique());
        currentSearch.setForDebutDroit(subside.getDebutDroit());
        currentSearch.setForIdFamille(subside.getIdFamille());
        currentSearch.setForCodeActif(true);
        currentSearch = this.search(currentSearch);
        if (currentSearch.getSize() == 1) {
            // ----------------------------------------------------------------
            // Edition de l'existant
            // ----------------------------------------------------------------
            newDetailFamille = (SimpleDetailFamille) currentSearch.getSearchResults()[0];
            newDetailFamille.setCodeActif(true);
            newDetailFamille.setAnnonceCaisseMaladie(false);
            newDetailFamille.setDateAnnonceCaisseMaladie("");
            newDetailFamille.setCodeForcer(false);
            newDetailFamille.setMontantContribution(subside.getMontantContribution());
            newDetailFamille.setSupplExtra(subside.getSupplExtra());
            newDetailFamille.setFinDroit(subside.getFinDroit());
            newDetailFamille.setNoCaisseMaladie(subside.getNoCaisseMaladie());
            newDetailFamille.setMontantPrimeAssurance("");
            newDetailFamille.setMontantExact("");
            newDetailFamille.setRefus(subside.getRefus());
            newDetailFamille.setTypeDemande(subside.getTypeDemande());
            newDetailFamille.setNoAssure(subside.getNoAssure());

            if (IAMCodeSysteme.AMDocumentModeles.ACREP10.getValue().equals(subside.getNoModeles())
                    || IAMCodeSysteme.AMDocumentModeles.ACREP11.getValue().equals(subside.getNoModeles())
                    || IAMCodeSysteme.AMDocumentModeles.ACREP12.getValue().equals(subside.getNoModeles())
                    || IAMCodeSysteme.AMDocumentModeles.ACREP13.getValue().equals(subside.getNoModeles())) {
                newDetailFamille.setDateRecepDemandeRecalcul(subside.getDateRecepDemande());
            } else {
                newDetailFamille.setDateRecepDemande(subside.getDateRecepDemande());
            }

            newDetailFamille = update(newDetailFamille);
        } else if (currentSearch.getSize() == 0) {
            // ----------------------------------------------------------------
            // Création d'un nouveau subside
            // ----------------------------------------------------------------
            newDetailFamille = subside;
            newDetailFamille.setCodeActif(true);
            newDetailFamille.setAnnonceCaisseMaladie(false);
            String oldNoModele = newDetailFamille.getNoModeles();
            newDetailFamille.setNoModeles("");

            if (IAMCodeSysteme.AMDocumentModeles.ACREP10.getValue().equals(oldNoModele)
                    || IAMCodeSysteme.AMDocumentModeles.ACREP11.getValue().equals(oldNoModele)
                    || IAMCodeSysteme.AMDocumentModeles.ACREP12.getValue().equals(oldNoModele)
                    || IAMCodeSysteme.AMDocumentModeles.ACREP13.getValue().equals(oldNoModele)) {
                newDetailFamille.setDateRecepDemandeRecalcul(subside.getDateRecepDemande());
            }

            newDetailFamille = create(newDetailFamille);
            newDetailFamille.setNoModeles(oldNoModele);
        } else {
            // ----------------------------------------------------------------
            // Exception, plus de 1 subside trouvé
            // ----------------------------------------------------------------
            throw new DetailFamilleException("Plus de 1 élément trouvé pour l'élément " + subside.getAnneeHistorique()
                    + " - " + subside.getIdFamille());
        }
        // ----------------------------------------------------------------
        // Clôture du précédent si existant dans la même année encore ouvert
        // ----------------------------------------------------------------
        SimpleDetailFamilleSearch previousSearch = new SimpleDetailFamilleSearch();
        previousSearch.setForAnneeHistorique(subside.getAnneeHistorique());
        previousSearch.setForIdFamille(subside.getIdFamille());
        previousSearch.setForCodeActif(true);
        previousSearch = this.search(previousSearch);
        for (int iPrevious = 0; iPrevious < previousSearch.getSize(); iPrevious++) {
            SimpleDetailFamille previousDetail = (SimpleDetailFamille) previousSearch.getSearchResults()[iPrevious];
            // Subsides qui ne sont pas le subside courant
            if (!previousDetail.getId().equals(newDetailFamille.getId())) {
                // subsides qui n'ont pas de date de fin
                if (JadeStringUtil.isBlankOrZero(previousDetail.getFinDroit())) {
                    String oldEnd = newDetailFamille.getDebutDroit();
                    try {
                        BigDecimal previousStartMonth = JADate.getMonth(previousDetail.getDebutDroit());
                        BigDecimal newStartMonth = JADate.getMonth(newDetailFamille.getDebutDroit());
                        // subsides qui sont antérieurs, les postérieurs ne sont pas fermés
                        if (previousStartMonth.intValue() >= newStartMonth.intValue()) {
                            continue;
                        }
                        BigDecimal endMonth = JADate.getMonth(oldEnd);
                        int iEndMonth = endMonth.intValue() - 1;
                        String sEndMonth = JadeStringUtil.fillWithZeroes(String.valueOf(iEndMonth), 2);
                        oldEnd = "" + sEndMonth + "." + JADate.getYear(oldEnd);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        continue;
                    }
                    previousDetail.setFinDroit(oldEnd);
                    previousDetail = update(previousDetail);

                    // BZ9246 : Interruption du subside en cours d'année, création d'une interruption
                    try {
                        AmalImplServiceLocator.getAnnoncesRPService().initAnnonceInterruption(
                                previousDetail.getIdContribuable(), previousDetail.getIdDetailFamille(),
                                previousDetail.getCodeActif());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        return newDetailFamille;
    }

    /**
     * 3ème étape d'enregistrement du subside - documents
     *
     * @param subside
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws ControleurJobException
     * @throws AnnonceException
     * @throws ControleurEnvoiException
     * @throws DetailFamilleException
     * @throws DocumentException
     * @throws ParametreModelException
     * @throws FormuleListException
     */
    private void generateSubsideStep3Documents(String dateWellFormatted, SimpleDetailFamille currentSubside,
            SimpleDetailFamille newSubside, String noGroupe, String csTypeJob)
            throws DocumentException, DetailFamilleException, ControleurEnvoiException, AnnonceException,
            ControleurJobException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            ParametreModelException, FormuleListException {

        if (!JadeStringUtil.isEmpty(currentSubside.getNoModeles())) {
            int iNoGroupe = 0;
            if (!JadeStringUtil.isEmpty(noGroupe)) {
                try {
                    iNoGroupe = Integer.parseInt(noGroupe);
                } catch (Exception ex) {
                    iNoGroupe = 0;
                }
            }
            // ATTRIBUTION DU NUMERO DE GROUPE EN FONCTION DES PROPRIETES DU DOCUMENT
            // SI DOCUMENT AVEC LISTE >> OUI, AUTREMENT NON
            // ---------------------------------------------------------------
            FormuleListSearch formuleListSearch = new FormuleListSearch();
            // en fait, for libelle is for cs document...
            formuleListSearch.setForlibelle(currentSubside.getNoModeles());
            formuleListSearch.setDefinedSearchSize(0);
            formuleListSearch = ENServiceLocator.getFormuleListService().search(formuleListSearch);
            if (formuleListSearch.getSize() == 1) {
                FormuleList formule = (FormuleList) formuleListSearch.getSearchResults()[0];
                // Recherche des paramètres
                ParametreModelComplexSearch modelParametreSearch = new ParametreModelComplexSearch();
                modelParametreSearch.setForIdFormule(formule.getId());
                modelParametreSearch.setWhereKey("basic");
                modelParametreSearch = AmalServiceLocator.getParametreModelService().search(modelParametreSearch);
                if (modelParametreSearch.getSize() == 1) {
                    ParametreModelComplex model = (ParametreModelComplex) modelParametreSearch.getSearchResults()[0];
                    if (!model.getSimpleParametreModel().getDocumentAvecListe()) {
                        iNoGroupe = 0;
                    }
                } else {
                    iNoGroupe = 0;
                }
            } else {
                iNoGroupe = 0;
            }

            // Enregistrement dans les tables
            // ---------------------------------------------------------------
            writeInJobTable(dateWellFormatted, currentSubside.getNoModeles(), csTypeJob,
                    newSubside.getIdDetailFamille(), iNoGroupe);
        }
    }

    private HashMap<String, Object> generateWordFile(String idDossier, String idFormule, String csFormule) {
        // ---------------------------------------------------------------
        // Préparation de la hashmap de retour
        // ---------------------------------------------------------------
        HashMap<String, Object> returnMap = new HashMap<String, Object>();
        String msgError = "";
        String msgWarning = "";
        // --------------------------------------------------------------------
        // Récupération de la date du jour
        // --------------------------------------------------------------------
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss");
        SimpleDateFormat sdfShort = new SimpleDateFormat("dd.MM.yyyy");
        String csDateComplete = sdf.format(date);
        String csDateShort = sdfShort.format(date);
        // ---------------------------------------------------------------
        // Etape 1 - Création du document
        // ---------------------------------------------------------------
        Document currentDoc = null;
        try {
            currentDoc = generateWordFileStep1(idDossier, idFormule);
        } catch (Exception ex) {
            msgError = "Exception raised when trying to generate the word document : " + ex.toString();
            JadeThread.logError(toString(), ex.getMessage());
            currentDoc = null;
        }
        if (currentDoc != null) {
            // ---------------------------------------------------------------
            // Etape 2 - Enregistrement dans persistence
            // ---------------------------------------------------------------
            Boolean bError = generateWordFileStep2(currentDoc, idDossier, idFormule, csFormule, csDateComplete);
            if (!bError) {
                // ---------------------------------------------------------------
                // Etape 3 - Copie s/ répertoire partagé - effacement persistence
                // ---------------------------------------------------------------
                bError = generateWordFileStep3(currentDoc, idDossier, idFormule, csFormule, csDateComplete);
                if (!bError) {
                    // ---------------------------------------------------------------
                    // Etape 4 - Enregistrement dans la table des jobs/envois
                    // ---------------------------------------------------------------
                    bError = generateWordFileStep4(idDossier, idFormule, csFormule, csDateShort, csDateComplete);
                    if (bError) {
                        msgWarning = "Unable to write the envoi into the job/envoi tables.\nAutomatic features are disabled : ";
                        msgWarning += "\n- Edition is not possible";
                        msgWarning += "\n- No archiving inside the folder";
                        msgWarning += "\n- No GED insertion";
                        msgWarning += "\n- No Journalisation";
                        // Save the doc into the hashmap
                        returnMap.put("document", "");// currentDoc);
                    } else {
                        // Save the link into the hashmap
                        returnMap.put("document", getInteractivDocumentFilePathFromClient(csDateComplete, idDossier,
                                idFormule, csFormule));
                    }
                } else {
                    msgWarning = "The word file cannot be saved on the server (SMB).\nAutomatic features are disabled : ";
                    msgWarning += "\n- Edition is not possible";
                    msgWarning += "\n- No archiving inside the folder";
                    msgWarning += "\n- No GED insertion";
                    msgWarning += "\n- No Journalisation";
                    // Save the doc into the hashmap
                    returnMap.put("document", "");// currentDoc);
                }
            } else {
                msgWarning = "The word file cannot be saved on the server (persistence).\nAutomatic features are disabled : ";
                msgWarning += "\n- Edition is not possible";
                msgWarning += "\n- No archiving inside the folder";
                msgWarning += "\n- No GED insertion";
                msgWarning += "\n- No Journalisation";
                // Save the doc into the hashmap
                returnMap.put("document", "");// currentDoc);
            }
        } else {
            msgError = "Unable to create a Word Document for dossier " + idDossier + ", formule : " + idFormule;
            // document empty
            returnMap.put("document", "");
        }
        // ---------------------------------------------------------------
        // Si errors, clé file contient le fichier xml ou rien
        // Si ok, clé file contient le chemin du fichier
        // ---------------------------------------------------------------
        if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.WARN)) {
            for (int iMessage = 0; iMessage < JadeThread
                    .logMessagesOfLevel(JadeBusinessMessageLevels.WARN).length; iMessage++) {
                msgWarning += "\n" + JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.WARN)[iMessage]
                        .getContents(JadeThread.currentLanguage());
            }
        }
        if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            for (int iMessage = 0; iMessage < JadeThread
                    .logMessagesOfLevel(JadeBusinessMessageLevels.ERROR).length; iMessage++) {
                msgError += "\n" + JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.ERROR)[iMessage]
                        .getContents(JadeThread.currentLanguage());
            }
        }
        JadeThread.logClear();
        returnMap.put("warning", msgWarning);
        returnMap.put("error", msgError);
        return returnMap;
    }

    /**
     * Création du document en fonction d'un idDossier et d'un idFormule
     *
     * @param idDossier
     * @param idFormule
     * @return WordMLDocument ou null si erreurs
     * @throws Exception
     * @throws JadeApplicationServiceNotAvailableException
     */
    private Document generateWordFileStep1(String idDossier, String idFormule)
            throws JadeApplicationServiceNotAvailableException, Exception {
        // Préparation de la map nom-classe, id
        HashMap<Object, Object> map = prepareMapIdForFusion(idDossier);

        // Préparation de l'EnvoiData
        AMEnvoiData currentEnvoiData = AMEnvoiDataFactory.getAMEnvoiData(map, idFormule);

        // Création du document
        Object returnDocument = ENServiceLocator.getDocumentMergeService().createDocument(currentEnvoiData);
        if (returnDocument instanceof WordmlDocument) {
            return (Document) returnDocument;
        } else {
            return null;
        }
    }

    /**
     * Enregistrement du document en persistence
     *
     * @param currentDoc
     * @param idDossier
     * @param idFormule
     * @param csDateComplete
     * @return true si des erreurs ont été rencontrées
     */
    private Boolean generateWordFileStep2(Document currentDoc, String idDossier, String idFormule, String csFormule,
            String csDateComplete) {
        // -------------------------------------------------
        // Save the file on the server web - persistence dir
        // -------------------------------------------------
        BufferedWriter out = null;
        String path = Jade.getInstance().getPersistenceDir();
        String filePath = path + getInteractivDocumentFileName(csDateComplete, idDossier, idFormule, csFormule);
        Boolean bError = false;
        try {
            // check the path and create it if needed
            new File(path).mkdirs();
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF8"));
            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
            out.newLine();
            StringWriter currentWriter = new StringWriter();
            currentDoc.toXml(currentWriter);
            out.write(currentWriter.toString());
            out.flush();
        } catch (Exception ex) {
            bError = true;
            JadeThread.logError(toString(), "Error Writer Tempory file wordExport.doc" + ex.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    bError = true;
                    JadeThread.logError(toString(),
                            "Error closing Writer Tempory file wordExport.doc" + e.getMessage());
                }
            }
        }
        return bError;
    }

    /**
     * Copy du document dans le répertoire de travail des utilisateurs
     *
     * @param currentDoc
     * @param idDossier
     * @param idFormule
     * @param csDateComplete
     * @return true si des erreurs ont été rencontrées
     */
    private Boolean generateWordFileStep3(Document currentDoc, String idDossier, String idFormule, String csFormule,
            String csDateComplete) {
        Boolean bError = false;
        String pathSource = Jade.getInstance().getPersistenceDir();
        String filePathSource = pathSource
                + getInteractivDocumentFileName(csDateComplete, idDossier, idFormule, csFormule);
        String filePathTarget = "";

        SimpleParametreApplicationSearch paramSearch = new SimpleParametreApplicationSearch();
        paramSearch
                .setForCsTypeParametre(IAMCodeSysteme.AMParametreApplication.CHEMIN_EXPORT_WORD_SERVER_WAS.getValue());
        try {
            paramSearch = AmalServiceLocator.getSimpleParametreApplicationService().search(paramSearch);
            if (paramSearch.getSize() == 1) {
                SimpleParametreApplication paramApplication = new SimpleParametreApplication();
                paramApplication = (SimpleParametreApplication) paramSearch.getSearchResults()[0];
                filePathTarget = paramApplication.getValeurParametre();
                filePathTarget += getInteractivDocumentFileName(csDateComplete, idDossier, idFormule, csFormule);
            } else {
                filePathTarget = "";
            }
        } catch (Exception ex) {
            bError = true;
            ex.printStackTrace();
            JadeThread.logError(toString(), "Error getting URL from client " + filePathSource + " - " + filePathTarget
                    + " >> " + ex.toString());
        }

        // copie
        try {
            if (!JadeStringUtil.isEmpty(filePathTarget)) {
                JadeFsFacade.copyFile(filePathSource, filePathTarget);
            } else {
                bError = true;
            }
        } catch (Exception e) {
            bError = true;
            JadeThread.logError(toString(), "Error copying doc file to Shared URL " + filePathSource + " - "
                    + filePathTarget + " >> " + e.toString());
        }
        // efface le fichier source
        try {
            JadeFsFacade.delete(filePathSource);
        } catch (Exception ex) {
            JadeThread.logError(toString(),
                    "Error deleting doc file from persistence " + filePathSource + " >> " + ex.toString());
        }
        return bError;
    }

    /**
     * Ecriture du document généré en db pour trace et mise en GED postérieure
     *
     * @param idDossier
     * @param idFormule
     * @param csDateShort
     * @param csDateComplete
     * @return true si des erreurs ont été rencontrées
     */
    private Boolean generateWordFileStep4(String idDossier, String idFormule, String csFormule, String csDateShort,
            String csDateComplete) {
        Boolean bError = false;
        try {
            AmalServiceLocator.getDetailFamilleService().writeInJobTable(csDateComplete, csFormule,
                    IAMCodeSysteme.AMJobType.JOBMANUALEDITED.getValue(), idDossier, 0);
        } catch (Exception ex) {
            JadeThread.logError(toString(), "Error when creating the job interactiv document");
            bError = true;
            ex.printStackTrace();
        }
        try {
            if (bError) {
                JadeThread.rollbackSession();
            } else {
                JadeThread.commitSession();
            }
        } catch (Exception ex) {
            bError = true;
            JadeThread.logError(toString(), "Error when committing/rollbacking envoi/job");
        }
        return bError;
    }

    private void generateWordFileStepSignature(WWordDocument currentDoc) {
        if (generateWordFileStepSignatureElementsIterator(currentDoc.getElementsIterator())) {
            JadeLogger.info(null, "I found it");
        } else {
            JadeLogger.info(null, "I did'nt find it");
        }
    }

    private boolean generateWordFileStepSignatureElementsIterator(Iterator<Node> currentIterator) {
        boolean bFound = false;
        while (currentIterator.hasNext()) {
            Node currentNode = currentIterator.next();
            if ((currentNode != null) && (currentNode.getContent() != null)) {
                if (currentNode.getContent().indexOf("SIGNATURE_RIGHT_IMAGE") >= 0) {
                    JadeLogger.info(null, "******************************************************************");
                    JadeLogger.info(null, "FOUND SIGNATURE_RIGHT_IMAGE");
                    JadeLogger.info(null, "******************************************************************");
                    // -------------------------------------------------------------------
                    // Ajout des signets pour les signatures (wordml principalement)
                    // -------------------------------------------------------------------
                    try {
                        // Recherche du chemin de base du document
                        // Ajout du sous-répertoire Signatures
                        // récupération du visa de l'utilisateur (jupac)
                        // ajout de .txt et .png
                        String userReference = BSessionUtil.getSessionFromThreadContext().getUserName();
                        if (userReference != null) {
                            // récupération du chemin de base
                            SimpleParametreApplicationSearch paramSearch = new SimpleParametreApplicationSearch();
                            paramSearch.setForCsTypeParametre(
                                    IAMCodeSysteme.AMParametreApplication.CHEMIN_EXPORT_WORD_CLIENT.getValue());
                            paramSearch = AmalServiceLocator.getSimpleParametreApplicationService().search(paramSearch);
                            if (paramSearch.getSize() == 1) {
                                String currentPath = ((SimpleParametreApplication) paramSearch.getSearchResults()[0])
                                        .getValeurParametre();
                                String pathImage = /* currentPath */".\\\\" + "Signatures\\\\" + userReference + ".png";
                                String pathTexte = currentPath + "Signatures\\" + userReference + ".txt";
                                // currentNode.setContent(" INCLUDEPICTURE \"" + pathImage + "\" \\* MERGEFORMAT ");
                                currentNode.setContent(" INCLUDEPICTURE \"" + pathImage + "\" ");
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    bFound = true;
                    break;
                } else {
                    bFound = generateWordFileStepSignatureElementsIterator(currentNode.getElementsIterator());
                    if (bFound == true) {
                        break;
                    }
                }
            }
        }
        return bFound;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.amal.business.services.models.detailfamille.DetailFamilleService#
     * getAvailableDocumentsListCorrespondance
     * (java.lang.String)
     */
    @Override
    public ArrayList<ParametreModelComplex> getAvailableDocumentsListCorrespondance(String idDossier) {

        // Préparation de l'arraylist
        ArrayList<ParametreModelComplex> allTemplates = new ArrayList<ParametreModelComplex>();

        ParametreModelComplexSearch searchModel = new ParametreModelComplexSearch();
        searchModel.setWhereKey("basic");
        searchModel.setDefinedSearchSize(0);
        try {
            searchModel = AmalServiceLocator.getParametreModelService().search(searchModel);
            for (int iCurrentModel = 0; iCurrentModel < searchModel.getSize(); iCurrentModel++) {
                ParametreModelComplex currentModel = (ParametreModelComplex) searchModel
                        .getSearchResults()[iCurrentModel];
                if (currentModel.getSimpleParametreModel().getVisibleCorrespondance()) {
                    allTemplates.add(currentModel);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Retourne l'arraylist remplie
        return allTemplates;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.models.contribuable.ContribuableService #read(java .lang.String)
     */
    public AdministrationComplexModel getCaisseMaladie(String idTiers) throws JadePersistenceException {
        if (JadeStringUtil.isBlankOrZero(idTiers)) {
            return null;
        }

        AdministrationComplexModel admin = null;

        try {
            admin = TIBusinessServiceLocator.getAdministrationService().read(idTiers);
        } catch (Exception e) {
            JadeLogger.error(this,
                    "Error getting administration information, idTiers : " + idTiers + " - " + e.getMessage());
        }

        return admin;

    }

    @Override
    public Properties getGEDPublishProperties(SimpleDetailFamille detailFamille,
            SimpleControleurEnvoiStatus statusEnvoi) {

        // ----------------------------------------
        // Test des informations d'entrées
        // ----------------------------------------
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();
        SimpleDocument currentDocument = null;
        try {
            currentDocument = AmalImplServiceLocator.getSimpleDocumentService().read(statusEnvoi.getIdEnvoi());
        } catch (Exception e) {
            JadeLogger.error(this,
                    "Exception getting the document " + statusEnvoi.getIdEnvoi() + " >> " + e.toString());
            e.printStackTrace();
        }
        SimpleContribuable contribuable = null;
        Contribuable currentContribuable = null;
        try {
            contribuable = AmalImplServiceLocator.getSimpleContribuableService()
                    .read(detailFamille.getIdContribuable());
            currentContribuable = AmalServiceLocator.getContribuableService().read(detailFamille.getIdContribuable());
        } catch (Exception e) {
            JadeLogger.error(this,
                    "Exception getting the contribuable " + detailFamille.getIdContribuable() + " >> " + e.toString());
            e.printStackTrace();
        }
        // ----------------------------------------
        // Informations tiers, contribuable, user
        // ----------------------------------------
        String description = "";
        String creationDate = "";
        String noContribuable = "";
        String idTiers = "";
        if (currentDocument != null) {
            // Récupération du code utilisateur
            description = currentSession.getCodeLibelle(currentDocument.getNumModele());
            creationDate = currentDocument.getDateEnvoi();
        }
        if (contribuable != null) {
            if (currentContribuable != null) {
                // Informations depuis Tiers
                try {
                    idTiers = currentContribuable.getPersonneEtendue().getTiers().getIdTiers();
                    noContribuable = currentContribuable.getPersonneEtendue().getPersonneEtendue()
                            .getNumContribuableActuel();
                    noContribuable = JadeStringUtil.removeChar(noContribuable, '.');
                    noContribuable = JadeStringUtil.removeChar(noContribuable, '/');
                } catch (Exception ex) {
                    ex.printStackTrace();
                    idTiers = contribuable.getIdTier();
                    noContribuable = contribuable.getNoContribuable();
                }
                if (JadeStringUtil.isEmpty(idTiers)) {
                    idTiers = contribuable.getIdTier();
                }
                if (JadeStringUtil.isEmpty(noContribuable)) {
                    noContribuable = contribuable.getNoContribuable();
                }
            } else {
                // Informations depuis simplecontribuable
                idTiers = contribuable.getIdTier();
                noContribuable = contribuable.getNoContribuable();
            }
        }

        // ----------------------------------------------------------------------------
        // Récupération du type de GED
        // ----------------------------------------------------------------------------
        String gedDocumentType = "840";
        String gedDocumentTypeLibelle = "Correspondance";
        ParametreModelComplexSearch searchModel = new ParametreModelComplexSearch();
        searchModel.setWhereKey("basic");
        searchModel.setDefinedSearchSize(0);
        try {
            searchModel = AmalServiceLocator.getParametreModelService().search(searchModel);
            for (int iCurrentModel = 0; iCurrentModel < searchModel.getSize(); iCurrentModel++) {
                ParametreModelComplex currentModel = (ParametreModelComplex) searchModel
                        .getSearchResults()[iCurrentModel];
                if (currentDocument.getNumModele()
                        .equals(currentModel.getSimpleParametreModel().getCodeSystemeFormule())) {
                    gedDocumentType = currentSession.getCode(currentModel.getSimpleParametreModel().getTypeGed());
                    gedDocumentTypeLibelle = currentSession
                            .getCodeLibelle(currentModel.getSimpleParametreModel().getTypeGed());
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // ----------------------------------------------------------------------------
        // Récupération des propriétés
        // ----------------------------------------------------------------------------
        Properties props = new Properties();
        props.setProperty(JadeGedTargetProperties.DOCUMENT_TYPE, gedDocumentTypeLibelle);
        props.setProperty(JadeGedTargetProperties.DOCUMENT_TYPE_NUMBER, gedDocumentType);
        props.setProperty(JadeGedTargetProperties.SERVICE, "MAL");

        props.setProperty(JadeGedTargetProperties.DESCRIPTION, description);
        props.setProperty(JadeGedTargetProperties.CREATION_DATE, creationDate);
        props.setProperty("numero.contribuable", noContribuable);
        props.setProperty("subside.anneeHistorique", detailFamille.getAnneeHistorique());

        // propriété pyxis.tiers.numero.avs.non.formatte récupérée
        try {
            JadePublishDocumentInfo documentInfo = new JadePublishDocumentInfo();
            TIDocumentInfoHelper.fill(documentInfo, idTiers, AMGestionTiers.CS_TYPE_COURRIER,
                    AMGestionTiers.CS_DOMAINE_AMAL, currentSession, "", "", "", "");
            props = JadeUtil.mergeProperties(documentInfo.getDocumentProperties(), props);
        } catch (Exception e) {
            JadeLogger.error(this, "Error Generating filling GED properties TIDocumentInfoHelper >> " + e.toString());
        }

        return props;

    }

    @Override
    public Properties getGEDPublishProperties(String idDetailFamille, String idStatusEnvoi) {
        SimpleDetailFamille detailFamille = null;
        try {
            detailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService().read(idDetailFamille);
        } catch (Exception e) {
            JadeLogger.error(this, "Exception getting the publish info for id detail famille " + idDetailFamille
                    + " >> " + e.toString());
        }
        SimpleControleurEnvoiStatus envoiStatus = null;
        try {
            envoiStatus = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().read(idStatusEnvoi);
        } catch (Exception e) {
            JadeLogger.error(this,
                    "Exception getting the publish info for status Envoi " + idStatusEnvoi + " >> " + e.toString());
        }
        if ((detailFamille != null) && (envoiStatus != null)) {
            return this.getGEDPublishProperties(detailFamille, envoiStatus);
        } else {
            JadeLogger.error(this, "GED Publish info will failed for detail famille " + idDetailFamille);
            return null;
        }
    }

    @Override
    public ArrayList<ComplexControleurAnnonceDetail> getHistoriqueAnnonces(String idDetailFamille) {
        ComplexControleurAnnonceDetailSearch currentControleurAnnonceSearch = new ComplexControleurAnnonceDetailSearch();
        ArrayList<ComplexControleurAnnonceDetail> returnArray = new ArrayList<ComplexControleurAnnonceDetail>();
        try {
            SimpleDetailFamille currentDetailFamille = read(idDetailFamille);
            if (currentDetailFamille.getIdFamille() != null) {
                // Remplissage du container à la main, performance issue
                SimpleAnnonceSearch annonceSearch = new SimpleAnnonceSearch();
                annonceSearch.setForIdDetailFamille(currentDetailFamille.getIdDetailFamille());
                annonceSearch = AmalImplServiceLocator.getSimpleAnnonceService().search(annonceSearch);
                JadeAbstractModel[] searchResults = new JadeAbstractModel[annonceSearch.getSize()];
                for (int iAnnonce = 0; iAnnonce < annonceSearch.getSize(); iAnnonce++) {
                    SimpleAnnonce annonce = (SimpleAnnonce) annonceSearch.getSearchResults()[iAnnonce];
                    SimpleControleurEnvoiStatusSearch statusSearch = new SimpleControleurEnvoiStatusSearch();
                    statusSearch.setForIdEnvoi(annonce.getIdDetailAnnonce());
                    statusSearch = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().search(statusSearch);
                    for (int iStatus = 0; iStatus < statusSearch.getSize(); iStatus++) {
                        SimpleControleurEnvoiStatus status = (SimpleControleurEnvoiStatus) statusSearch
                                .getSearchResults()[iStatus];
                        ComplexControleurAnnonceDetail detail = new ComplexControleurAnnonceDetail();
                        detail.setSimpleAnnonce(annonce);
                        detail.setSimpleEnvoiStatus(status);
                        searchResults[iAnnonce] = detail;
                        returnArray.add(detail);
                    }
                }
                currentControleurAnnonceSearch.setSearchResults(searchResults);
            }
        } catch (Exception ex) {
            JadeLogger.error(this, "Error loading Historique envois-annonces :" + ex.toString());
            ex.printStackTrace();
            return new ArrayList<ComplexControleurAnnonceDetail>();
        }
        return returnArray;
    }

    @Override
    public ArrayList<ComplexControleurEnvoiDetail> getHistoriqueEnvois(String idDetailFamille) {
        ComplexControleurEnvoiDetailSearch currentControleurEnvoiSearch = new ComplexControleurEnvoiDetailSearch();
        ArrayList<ComplexControleurEnvoiDetail> returnArray = new ArrayList<ComplexControleurEnvoiDetail>();
        try {
            SimpleDetailFamille currentDetailFamille = read(idDetailFamille);
            if (currentDetailFamille.getIdFamille() != null) {
                // Remplissage du container à la main, performance issue
                SimpleDocumentSearch documentSearch = new SimpleDocumentSearch();
                documentSearch.setForIdDetailFamille(currentDetailFamille.getIdDetailFamille());
                documentSearch.setOrderKey("orderByDateEnvoiDesc_idDetailEnvoiDocumentDesc");
                documentSearch = AmalImplServiceLocator.getSimpleDocumentService().search(documentSearch);
                JadeAbstractModel[] searchResults = new JadeAbstractModel[documentSearch.getSize()];
                for (int iDocument = 0; iDocument < documentSearch.getSize(); iDocument++) {
                    SimpleDocument document = (SimpleDocument) documentSearch.getSearchResults()[iDocument];
                    SimpleControleurEnvoiStatusSearch statusSearch = new SimpleControleurEnvoiStatusSearch();
                    statusSearch.setForIdEnvoi(document.getIdDetailEnvoiDocument());
                    statusSearch = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().search(statusSearch);
                    for (int iStatus = 0; iStatus < statusSearch.getSize(); iStatus++) {
                        SimpleControleurEnvoiStatus status = (SimpleControleurEnvoiStatus) statusSearch
                                .getSearchResults()[iStatus];
                        SimpleControleurJob currentJob = AmalImplServiceLocator.getSimpleControleurJobService()
                                .read(status.getIdJob());
                        ComplexControleurEnvoiDetail detail = new ComplexControleurEnvoiDetail();
                        detail.setDateEnvoi(document.getDateEnvoi());
                        detail.setStatusEnvoi(status.getStatusEnvoi());
                        detail.setIdDetailFamille(currentDetailFamille.getIdDetailFamille());
                        detail.setIdStatus(status.getIdStatus());
                        detail.setIdJob(status.getIdJob());
                        detail.setLibelleEnvoi(document.getLibelleEnvoi());
                        detail.setNumModele(document.getNumModele());
                        detail.setTypeJob(currentJob.getTypeJob());
                        searchResults[iDocument] = detail;
                        returnArray.add(detail);
                    }
                }
                currentControleurEnvoiSearch.setSearchResults(searchResults);
            }
        } catch (Exception ex) {
            JadeLogger.error(this, "Error loading Historique envois-annonces :" + ex.toString());
            ex.printStackTrace();
            return new ArrayList<ComplexControleurEnvoiDetail>();
        }
        return returnArray;
    }

    @Override
    public String getIdFormuleFromNoFormule(String noFormule)
            throws FormuleListException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        FormuleListSearch formuleListSearch = new FormuleListSearch();
        // en fait, for libelle is for cs document...
        formuleListSearch.setForlibelle(noFormule);
        formuleListSearch.setDefinedSearchSize(0);
        formuleListSearch = ENServiceLocator.getFormuleListService().search(formuleListSearch);

        String idFormule = null;
        if (formuleListSearch.getSize() == 1) {
            FormuleList formule = (FormuleList) formuleListSearch.getSearchResults()[0];
            idFormule = formule.getId();
        }
        return idFormule;

    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.models.detailFamille.DetailFamilleService#getInteractivDocumentFileName()
     */
    @Override
    public String getInteractivDocumentFileName(String csDateComplete, String idDetailFamille, String idFormule,
            String csFormule) {
        // -------------------------------------------------------------
        // Nom du document : 21.10.2011_14-01-35_669424_42000013.doc
        // -------------------------------------------------------------
        String fileName = "";
        fileName += csDateComplete + "_";
        fileName += idDetailFamille + "_";
        fileName += csFormule;
        fileName += ".doc";
        return fileName;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.models.detailFamille.DetailFamilleService#getInteractivDocumentFileName()
     */
    @Override
    public String getInteractivDocumentFilePathFromClient(String csDateComplete, String idDetailFamille,
            String idFormule, String csFormule) {
        String filePathTarget = "";
        SimpleParametreApplicationSearch paramSearch = new SimpleParametreApplicationSearch();
        paramSearch.setForCsTypeParametre(IAMCodeSysteme.AMParametreApplication.CHEMIN_EXPORT_WORD_CLIENT.getValue());
        try {
            paramSearch = AmalServiceLocator.getSimpleParametreApplicationService().search(paramSearch);
            if (paramSearch.getSize() == 1) {
                SimpleParametreApplication paramApplication = new SimpleParametreApplication();
                paramApplication = (SimpleParametreApplication) paramSearch.getSearchResults()[0];
                filePathTarget = paramApplication.getValeurParametre();
                filePathTarget += getInteractivDocumentFileName(csDateComplete, idDetailFamille, idFormule, csFormule);
            } else {
                filePathTarget = "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JadeThread.logError(toString(), "Error getting URL from client " + filePathTarget + " - " + filePathTarget
                    + " >> " + ex.toString());
        }
        return filePathTarget;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.models.detailFamille.DetailFamilleService#getInteractivDocumentFileName()
     */
    @Override
    public String getInteractivDocumentFilePathFromJobServer(String csDateComplete, String idDetailFamille,
            String idFormule, String csFormule) {
        String filePathTarget = "";
        SimpleParametreApplicationSearch paramSearch = new SimpleParametreApplicationSearch();
        paramSearch
                .setForCsTypeParametre(IAMCodeSysteme.AMParametreApplication.CHEMIN_EXPORT_WORD_SERVER_JOB.getValue());
        try {
            paramSearch = AmalServiceLocator.getSimpleParametreApplicationService().search(paramSearch);
            if (paramSearch.getSize() == 1) {
                SimpleParametreApplication paramApplication = new SimpleParametreApplication();
                paramApplication = (SimpleParametreApplication) paramSearch.getSearchResults()[0];
                filePathTarget = paramApplication.getValeurParametre();
                filePathTarget += getInteractivDocumentFileName(csDateComplete, idDetailFamille, idFormule, csFormule);
            } else {
                filePathTarget = "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JadeThread.logError(toString(), "Error getting URL from job server " + filePathTarget + " - "
                    + filePathTarget + " >> " + ex.toString());
        }
        return filePathTarget;
    }

    @Override
    public ArrayList<String> getListeCMCalcul(String empty) {
        ArrayList<String> returnCM = new ArrayList<String>();
        try {
            AdministrationSearchComplexModel allCMSearch = new AdministrationSearchComplexModel();
            allCMSearch.setForGenreAdministration(AMGestionTiers.CS_TYPE_CAISSE_MALADIE);
            allCMSearch = TIBusinessServiceLocator.getAdministrationService().find(allCMSearch);
            for (int iCaisse = 0; iCaisse < allCMSearch.getSize(); iCaisse++) {
                AdministrationComplexModel caisse = (AdministrationComplexModel) allCMSearch
                        .getSearchResults()[iCaisse];
                if ((caisse.getTiers().getInactif() == true) || caisse.getTiers().get_inactif().equals("1")) {
                    continue;
                }
                returnCM.add(caisse.getTiers().getDesignation1() + "|" + caisse.getId() + "|"
                        + caisse.getAdmin().getCodeAdministration());

            }
        } catch (Exception e) {
            JadeLogger.error(this, "Error loading administration " + e.getMessage());
        }

        Collections.sort(returnCM);
        return returnCM;
    }

    @Override
    public ArrayList<String> getListeDocumentCalcul(String empty) {
        ArrayList<String> returnArray = new ArrayList<String>();

        BSession currentSession = BSessionUtil.getSessionFromThreadContext();
        FWParametersSystemCodeManager cm = new FWParametersSystemCodeManager();
        cm.setSession(currentSession);
        cm.setForIdGroupe("AMMODELES");
        cm.setForIdLangue(currentSession.getIdLangue());
        try {
            cm.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((cm == null) || (cm.getContainer() == null)) {
            return returnArray;
        }
        JAVector containerCS = cm.getContainer();

        // Check if code system is found in parametre document
        ParametreModelComplexSearch modelSearch = new ParametreModelComplexSearch();
        modelSearch.setWhereKey("basic");
        modelSearch.setDefinedSearchSize(0);
        try {
            modelSearch = AmalServiceLocator.getParametreModelService().search(modelSearch);

            for (int iModel = 0; iModel < modelSearch.getSize(); iModel++) {
                ParametreModelComplex currentModel = (ParametreModelComplex) modelSearch.getSearchResults()[iModel];
                String documentCodeSysteme = currentModel.getFormuleList().getDefinitionformule().getCsDocument();
                // Check si document manuel...
                if (currentModel.getFormuleList().getDefinitionformule().getCsManuAuto().equals("42001100")) {
                    continue;
                }
                // Check si document visible dans attribution
                if (!currentModel.getSimpleParametreModel().getVisibleAttribution()) {
                    continue;
                }

                for (Iterator it = containerCS.iterator(); it.hasNext();) {
                    FWParametersCode code = (FWParametersCode) it.next();
                    // Code système
                    String codeSysteme = code.getId();
                    if (documentCodeSysteme.equals(codeSysteme)) {
                        // CU
                        String codeUtilisateur = code.getCodeUtilisateur(currentSession.getIdLangue())
                                .getCodeUtilisateur();
                        // Libelle Short
                        String codeLibelleShort = code.getLibelle().trim();
                        // Libelle Long
                        String codeLibelle = code.getCodeUtilisateur(currentSession.getIdLangue()).getLibelle().trim();
                        String returnedString = codeSysteme + "|" + codeUtilisateur + "|" + codeLibelleShort + "|"
                                + codeLibelle;
                        // Ajout des type A, C, P, S
                        if (currentModel.getSimpleParametreModel().getDocumentAssiste()) {
                            returnedString += "|A";
                        } else {
                            returnedString += "|-";
                        }
                        if (currentModel.getSimpleParametreModel().getDocumentContribuable()) {
                            returnedString += "|C";
                        } else {
                            returnedString += "|-";
                        }
                        if (currentModel.getSimpleParametreModel().getDocumentPC()) {
                            returnedString += "|P";
                        } else {
                            returnedString += "|-";
                        }
                        if (currentModel.getSimpleParametreModel().getDocumentSourcier()) {
                            returnedString += "|S";
                        } else {
                            returnedString += "|-";
                        }
                        returnArray.add(returnedString);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Collections.sort(returnArray);
        return returnArray;

    }

    /**
     * @param id
     *            ID du code système
     *
     * @return libelle général du code système correspondant
     *
     */
    @Override
    public ArrayList<String> getListeTypeDemandeCalcul(String empty) {

        ArrayList<String> returnArray = new ArrayList<String>();

        BSession currentSession = BSessionUtil.getSessionFromThreadContext();
        FWParametersSystemCodeManager cm = new FWParametersSystemCodeManager();
        cm.setSession(currentSession);
        cm.setForIdGroupe("AMTYDE");
        cm.setForIdLangue(currentSession.getIdLangue());
        try {
            cm.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((cm == null) || (cm.getContainer() == null)) {
            return returnArray;
        }
        JAVector containerCS = cm.getContainer();

        for (Iterator it = containerCS.iterator(); it.hasNext();) {
            FWParametersCode code = (FWParametersCode) it.next();
            // Code système
            String codeSysteme = code.getId();
            // CU
            String codeUtilisateur = code.getCodeUtilisateur(currentSession.getIdLangue()).getCodeUtilisateur();
            // Libelle Short
            String codeLibelleShort = code.getLibelle().trim();
            // Libelle Long
            String codeLibelle = code.getCodeUtilisateur(currentSession.getIdLangue()).getLibelle().trim();
            returnArray.add(codeSysteme + "|" + codeUtilisateur + "|" + codeLibelleShort + "|" + codeLibelle);
        }
        return returnArray;
    }

    @Override
    public ArrayList<RevenuHistorique> getListRevenus(String idContribuable, String annee)
            throws DetailFamilleException, JadePersistenceException, RevenuException,
            JadeApplicationServiceNotAvailableException {
        ArrayList<RevenuHistorique> returnArray = new ArrayList<RevenuHistorique>();

        RevenuHistoriqueSearch revenuHistoriqueSearch = new RevenuHistoriqueSearch();
        revenuHistoriqueSearch.setForIdContribuable(idContribuable);
        revenuHistoriqueSearch.setForAnneeHistorique(annee);
        revenuHistoriqueSearch.setForRevenuActif(true);
        revenuHistoriqueSearch = (RevenuHistoriqueSearch) AmalServiceLocator.getRevenuService()
                .search(revenuHistoriqueSearch);

        if (revenuHistoriqueSearch.getSize() > 0) {
            RevenuHistorique revenuHistorique = (RevenuHistorique) revenuHistoriqueSearch.getSearchResults()[0];
            BSession currentSession = BSessionUtil.getSessionFromThreadContext();
            String typeTaxation = revenuHistorique.getTypeTaxation();
            String etatCivil = revenuHistorique.getEtatCivil();
            String typeSource = revenuHistorique.getTypeSource();
            revenuHistorique.setTypeTaxationLibelle(currentSession.getCodeLibelle(typeTaxation));
            revenuHistorique.setEtatCivilLibelle(currentSession.getCodeLibelle(etatCivil));
            revenuHistorique.setTypeSourceLibelle(currentSession.getCodeLibelle(typeSource));
            returnArray.add(revenuHistorique);
        }

        return returnArray;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.models.detailFamille.DetailFamilleService#getListSubsidesCalculAjax
     */
    @Override
    public CalculsSubsidesContainer getListSubsidesCalculAjax(HashMap<?, ?> values) {
        String idContribuable = values.get("idContribuable").toString();
        String anneeHistorique = values.get("anneeHistorique").toString();
        String typeDemande = values.get("typeDemande").toString();
        String idRevenu = values.get("idRevenu").toString();
        String revenuIsTaxationString = values.get("revenuIsTaxation").toString();
        boolean revenuIsTaxation = Boolean.parseBoolean(revenuIsTaxationString);
        return new CalculsSubsidesContainer(idContribuable, anneeHistorique, typeDemande, idRevenu, revenuIsTaxation);
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.models.detailFamille.DetailFamilleService#getListTaxations
     */
    @Override
    public ArrayList<Revenu> getListTaxations(String idContribuable, String annee) throws DetailFamilleException,
            JadePersistenceException, RevenuException, JadeApplicationServiceNotAvailableException {

        // prepare the return array
        ArrayList<Revenu> returnArray = new ArrayList<Revenu>();

        int iMaxAnnee = 0;
        int iMinAnnee = 0;

        // find the annee taxation for the specific annee (annee -2)
        try {
            iMaxAnnee = Integer.parseInt(annee);
            iMinAnnee = iMaxAnnee - 2;
            for (; (iMinAnnee <= iMaxAnnee) && (iMinAnnee != 0); iMinAnnee++) {
                RevenuSearch revenuSearch = new RevenuSearch();
                revenuSearch.setForIdContribuable(idContribuable);
                revenuSearch.setForAnneeTaxation("" + iMinAnnee);
                revenuSearch = AmalServiceLocator.getRevenuService().search(revenuSearch);
                for (int iTaxation = 0; iTaxation < revenuSearch.getSize(); iTaxation++) {
                    Revenu revenu = (Revenu) revenuSearch.getSearchResults()[iTaxation];
                    returnArray.add(revenu);
                }
            }
        } catch (Exception ex) {
            JadeLogger.error(this, "Error getting taxation list : " + ex.toString());
        }
        // Return the results
        return returnArray;

    }

    @Override
    public ArrayList<Revenu> getListTaxationsAjax(HashMap<?, ?> values) throws DetailFamilleException, RevenuException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return getListTaxations(values.get("idContribuable").toString(), values.get("anneeHistorique").toString());
    }

    @Override
    public ArrayList<CalculsTaxationContainer> getListTaxationsCalcul(String idContribuable, String annee) {
        // prepare the return array
        ArrayList<CalculsTaxationContainer> returnArray = new ArrayList<CalculsTaxationContainer>();

        int iMaxAnnee = 0;
        int iMinAnnee = 0;

        // find the annee taxation for the specific annee (annee -2)
        try {
            iMaxAnnee = Integer.parseInt(annee);
            iMinAnnee = iMaxAnnee - 2;
            BSession currentSession = BSessionUtil.getSessionFromThreadContext();
            for (; (iMinAnnee <= iMaxAnnee) && (iMinAnnee != 0); iMinAnnee++) {
                RevenuFullComplexSearch revenuSearch = new RevenuFullComplexSearch();
                revenuSearch.setForIdContribuable(idContribuable);
                revenuSearch.setForAnneeTaxation("" + iMinAnnee);
                revenuSearch = (RevenuFullComplexSearch) AmalServiceLocator.getRevenuService().search(revenuSearch);
                for (int iTaxation = 0; iTaxation < revenuSearch.getSize(); iTaxation++) {
                    RevenuFullComplex taxation = (RevenuFullComplex) revenuSearch.getSearchResults()[iTaxation];
                    CalculsTaxationContainer taxationCalcul = new CalculsTaxationContainer(annee, taxation);
                    // load libelle
                    String typeSource = taxationCalcul.getTaxation().getSimpleRevenu().getTypeSource();
                    String typeTaxation = taxationCalcul.getTaxation().getSimpleRevenu().getTypeTaxation();
                    String etatCivil = taxationCalcul.getTaxation().getSimpleRevenu().getEtatCivil();
                    taxationCalcul.getTaxation().getSimpleRevenu()
                            .setTypeSourceLibelle(currentSession.getCodeLibelle(typeSource));
                    taxationCalcul.getTaxation().getSimpleRevenu()
                            .setTypeTaxationLibelle(currentSession.getCodeLibelle(typeTaxation));
                    taxationCalcul.getTaxation().getSimpleRevenu()
                            .setEtatCivilLibelle(currentSession.getCodeLibelle(etatCivil));

                    // add result to the array
                    returnArray.add(taxationCalcul);
                }
            }
        } catch (Exception ex) {
            JadeLogger.error(this, "Error getting taxation list : " + ex.toString());
        }
        // Return the results
        return returnArray;
    }

    @Override
    public ArrayList<CalculsTaxationContainer> getListTaxationsCalculAjax(HashMap<?, ?> values) {
        return getListTaxationsCalcul(values.get("idContribuable").toString(),
                values.get("anneeHistorique").toString());
    }

    /**
     * Préparation du tableau d'id nécessaire au merge du document wordml
     *
     * @param currentDetail
     *            information du document courant
     * @return map renseignée
     */
    private HashMap<Object, Object> prepareMapIdForFusion(String idDetailFamille) {
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        SimpleDetailFamille currentDetailFamille = null;
        try {
            currentDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService().read(idDetailFamille);
            String anneeHistorique = currentDetailFamille.getAnneeHistorique();
            String idContribuable = currentDetailFamille.getIdContribuable();
            String idFamille = currentDetailFamille.getIdFamille();
            map.put(Contribuable.class.getName(), idContribuable);
            map.put(SimpleDetailFamille.class.getName(), idDetailFamille);
            map.put(SimpleFamille.class.getName(), idFamille);
            // Renseigne information de revenu lié
            RevenuHistoriqueSearch revenuSearch = new RevenuHistoriqueSearch();
            revenuSearch.setForAnneeHistorique(anneeHistorique);
            revenuSearch.setForRevenuActif(true);
            revenuSearch.setForIdContribuable(idContribuable);
            try {
                revenuSearch = (RevenuHistoriqueSearch) AmalServiceLocator.getRevenuService().search(revenuSearch);
                if (revenuSearch.getSize() > 0) {
                    RevenuHistorique revenu = (RevenuHistorique) revenuSearch.getSearchResults()[0];
                    map.put(RevenuHistoriqueComplex.class.getName(), revenu.getId());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            BSession currentSession = BSessionUtil.getSessionFromThreadContext();
            map.put(currentSession.getClass().getName(), currentSession.getUserId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;

    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.models.detailFamille.DetailFamilleService#read(java.lang.String)
     */
    @Override
    public SimpleDetailFamille read(String idDetailFamille) throws DetailFamilleException, JadePersistenceException {
        if (idDetailFamille == null) {
            throw new DetailFamilleException("Unable to read detailFamille the given id is null!");
        }
        try {
            return AmalImplServiceLocator.getSimpleDetailFamilleService().read(idDetailFamille);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DetailFamilleException("Service not available - " + e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.amal.business.services.models.detailFamille.DetailFamilleService#search(ch.globaz.amal.business.models
     * .detailfamille.SimpleDetailFamille)
     */
    @Override
    public SimpleDetailFamilleSearch search(SimpleDetailFamilleSearch detailFamilleSearch)
            throws JadePersistenceException, DetailFamilleException {
        if (detailFamilleSearch == null) {
            throw new DetailFamilleException("Unable to search dossier, the search model passed is null!");
        }
        try {
            return AmalImplServiceLocator.getSimpleDetailFamilleService().search(detailFamilleSearch);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DetailFamilleException("Service not available - " + e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.amal.business.services.models.detailFamille.DetailFamilleService#search(ch.globaz.amal.business.models
     * .detailfamille.SimpleDetailFamille)
     */
    @Override
    public SimpleDocumentSearch search(SimpleDocumentSearch simpleDocumentSearch)
            throws JadePersistenceException, DocumentException {
        if (simpleDocumentSearch == null) {
            throw new DocumentException("Unable to search document, the search model passed is null!");
        }
        return (SimpleDocumentSearch) JadePersistenceManager.search(simpleDocumentSearch);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.amal.business.services.models.detailFamille.DetailFamilleService#update(ch.globaz.amal.business.models
     * .detailfamille.SimpleDetailFamille)
     */
    @Override
    public SimpleDetailFamille update(SimpleDetailFamille detailFamille)
            throws DetailFamilleException, JadePersistenceException {
        if (detailFamille == null) {
            throw new DetailFamilleException("Unable to update detailFamille the given model is null!");
        }
        try {
            return AmalImplServiceLocator.getSimpleDetailFamilleService().update(detailFamille);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DetailFamilleException("Service not available - " + e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.models.detailFamille.DetailFamilleService#writeInJobTable(ch.globaz
     * .amal.business.models .detailfamille.SimpleDetailFamille)
     */
    @Override
    public void writeInJobTable(String dateJourComplete, String csModele, String csJobType, String idDetailFamille,
            int noGroupe)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, DocumentException,
            DetailFamilleException, ControleurEnvoiException, AnnonceException, ControleurJobException {

        if (!JadeStringUtil.isBlankOrZero(idDetailFamille)) {
            // Récupération de la date du jour
            String csDateDuJour = dateJourComplete.split("_")[0];
            String csHeureDuJour = dateJourComplete.split("_")[1];

            BSession currentSession = BSessionUtil.getSessionFromThreadContext();
            String currentUser = currentSession.getUserId();

            // ---------------------------------------------------
            // 1) Création du document dans MAENVDOC
            // ---------------------------------------------------
            SimpleDocument documentToAdd = new SimpleDocument();
            documentToAdd.setDateEnvoi(csDateDuJour);
            documentToAdd.setIdDetailFamille(idDetailFamille);
            if (csJobType.equals(IAMCodeSysteme.AMJobType.JOBMANUALEDITED.getValue())) {
                documentToAdd.setLibelleEnvoi(csHeureDuJour + " | Edité MS Word");
            } else if (csJobType.equals(IAMCodeSysteme.AMJobType.JOBMANUALQUEUED.getValue())) {
                documentToAdd.setLibelleEnvoi(csHeureDuJour + " | En file d'attente");
            } else {
                documentToAdd.setLibelleEnvoi(csHeureDuJour + " | Reprise");
            }
            documentToAdd.setNumModele(csModele);
            documentToAdd = AmalImplServiceLocator.getSimpleDocumentService().create(documentToAdd, csJobType);
            if ((documentToAdd.isNew() == true) || csJobType.equals("")) {
                return;
            }
            // ---------------------------------------------------
            // 2) Inscription dans MACTLJOB
            // ---------------------------------------------------
            // Récupération de la date du jour
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            String csDateDuJourShort = sdf.format(date);

            SimpleControleurJob jobToAdd = null;
            // Cherche si un job du jour, de type csJobType, est existant. Si non, création
            SimpleControleurJobSearch jobSearch = new SimpleControleurJobSearch();
            jobSearch.setForDateJob(csDateDuJourShort);
            jobSearch.setForTypeJob(csJobType);
            jobSearch.setForUserJob(currentUser);
            jobSearch.setForNotStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.SENT.getValue());
            if (csJobType.equals(IAMCodeSysteme.AMJobType.JOBPROCESS.getValue())) {
                jobSearch.setForSubTypeJob(csModele);
            }
            jobSearch = AmalImplServiceLocator.getSimpleControleurJobService().search(jobSearch);
            if (csJobType.equals(IAMCodeSysteme.AMJobType.JOBMANUALEDITED.getValue())
                    || csJobType.equals(IAMCodeSysteme.AMJobType.JOBMANUALQUEUED.getValue())) {
                // ---------------------------------------------------
                // NON-REPRISES
                // ---------------------------------------------------
                if (jobSearch.getSize() >= 1) {
                    jobToAdd = (SimpleControleurJob) jobSearch.getSearchResults()[0];
                } else {
                    jobToAdd = new SimpleControleurJob();
                    jobToAdd.setDateJob(csDateDuJourShort);
                    if (csJobType.equals(IAMCodeSysteme.AMJobType.JOBMANUALEDITED.getValue())) {
                        jobToAdd.setDescriptionJob("Travaux journaliers - MS Word");
                        jobToAdd.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.MANUALGENERATED.getValue());
                    } else if (csJobType.equals(IAMCodeSysteme.AMJobType.JOBMANUALQUEUED.getValue())) {
                        jobToAdd.setDescriptionJob("Travaux journaliers - Queue");
                        jobToAdd.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.AUTOGENERATED.getValue());
                    } else {
                        jobToAdd.setDescriptionJob("Reprise - Queue");
                        jobToAdd.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.AUTOGENERATED.getValue());
                        jobToAdd.setSubTypeJob(csModele);
                    }
                    jobToAdd.setTypeJob(csJobType);
                    jobToAdd.setUserJob(currentUser);
                    jobToAdd = AmalImplServiceLocator.getSimpleControleurJobService().create(jobToAdd);
                }
            } else {
                // ---------------------------------------------------
                // REPRISES
                // ---------------------------------------------------
                boolean bCreateANewJob = false;
                if (jobSearch.getSize() == 0) {
                    bCreateANewJob = true;
                } else {
                    int iLastIdJob = 0;
                    SimpleControleurJob lastJob = null;
                    for (int iJob = 0; iJob < jobSearch.getSize(); iJob++) {
                        SimpleControleurJob currentJob = (SimpleControleurJob) jobSearch.getSearchResults()[iJob];
                        int iCurrentIdJob = Integer.parseInt(currentJob.getIdJob());
                        if (iCurrentIdJob > iLastIdJob) {
                            iLastIdJob = iCurrentIdJob;
                            lastJob = currentJob;
                        }
                    }
                    // Check si on peut couper par 1000
                    SimpleControleurEnvoiStatusSearch countSearch = new SimpleControleurEnvoiStatusSearch();
                    countSearch.setForIdJob(lastJob.getIdJob());
                    int currentCount = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService()
                            .count(countSearch);
                    if (currentCount > 999) {
                        bCreateANewJob = true;
                    } else {
                        jobToAdd = lastJob;
                        bCreateANewJob = false;
                    }
                }
                if (bCreateANewJob) {
                    jobToAdd = new SimpleControleurJob();
                    jobToAdd.setDateJob(csDateDuJourShort);
                    if (csJobType.equals(IAMCodeSysteme.AMJobType.JOBMANUALEDITED.getValue())) {
                        jobToAdd.setDescriptionJob("Travaux journaliers - MS Word");
                        jobToAdd.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.MANUALGENERATED.getValue());
                    } else if (csJobType.equals(IAMCodeSysteme.AMJobType.JOBMANUALQUEUED.getValue())) {
                        jobToAdd.setDescriptionJob("Travaux journaliers - Queue");
                        jobToAdd.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.AUTOGENERATED.getValue());
                    } else {
                        jobToAdd.setDescriptionJob("Reprise - Queue");
                        jobToAdd.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.AUTOGENERATED.getValue());
                        jobToAdd.setSubTypeJob(csModele);
                    }
                    jobToAdd.setTypeJob(csJobType);
                    jobToAdd.setUserJob(currentUser);
                    jobToAdd = AmalImplServiceLocator.getSimpleControleurJobService().create(jobToAdd);
                }
            }
            // ---------------------------------------------------
            // 3) Inscription dans MACTLSTS
            // ---------------------------------------------------
            if ((jobToAdd != null) && (jobToAdd.isNew() == false)) {
                String csIdJobDuJour = jobToAdd.getIdJob();

                // Création du status du document
                SimpleControleurEnvoiStatus statusToAdd = new SimpleControleurEnvoiStatus();
                statusToAdd.setIdAnnonce("-1");
                statusToAdd.setIdEnvoi(documentToAdd.getIdDetailEnvoiDocument());
                statusToAdd.setIdJob(csIdJobDuJour);
                if (csJobType.equals(IAMCodeSysteme.AMJobType.JOBPROCESS.getValue())) {
                    statusToAdd.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.AUTOGENERATED.getValue());
                } else {
                    statusToAdd.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.MANUALGENERATED.getValue());
                }
                statusToAdd.setTypeEnvoi(IAMCodeSysteme.AMDocumentType.ENVOI.getValue());
                statusToAdd = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().create(statusToAdd);

                // Mise à jour du groupe
                statusToAdd.setNoGroupe("" + noGroupe);
                statusToAdd = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().update(statusToAdd);

                // Génération du status du job
                String newStatusJob = AmalServiceLocator.getControleurEnvoiService()
                        .generateStatus(jobToAdd.getIdJob());
                jobToAdd.setStatusEnvoi(newStatusJob);
                AmalImplServiceLocator.getSimpleControleurJobService().update(jobToAdd);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.amal.business.services.models.detailFamille.DetailFamilleService#writeInteractiveDocument(ch.globaz
     * .amal.business.models .detailfamille.SimpleDetailFamille)
     */
    @Override
    public String writeInteractivDocument(String fileName, Document documentToWrite) {

        // -------------------------------------------------
        // Save the file on the server web - persistence dir
        // -------------------------------------------------
        BufferedWriter out = null;
        String path = Jade.getInstance().getPersistenceDir();
        String filePath = path + fileName;
        try {
            // check the path and create it if needed
            new File(path).mkdirs();
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF8"));
            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
            out.newLine();
            StringWriter currentWriter = new StringWriter();
            documentToWrite.toXml(currentWriter);
            out.write(currentWriter.toString());
            out.flush();
        } catch (Exception ex) {
            JadeLogger.error(this, "Error Writer Tempory file wordExport.doc" + ex.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    JadeLogger.error(this, "Error closing Writer Tempory file wordExport.doc" + e.getMessage());
                }
            }
        }

        // ------------------------------------------
        // Copy file to the GED export server url
        // ------------------------------------------
        String gedURL = "";
        Boolean isGedCopyOK = false;
        try {
            // gedURL = this.getGEDExportServerURL();
            gedURL = "";
            if (!JadeStringUtil.isEmpty(gedURL)) {
                JadeFsFacade.copyFile(filePath, gedURL + fileName);
                isGedCopyOK = true;
            }
        } catch (Exception e) {
            JadeLogger.error(this, "Error copying doc file to Ged URL " + gedURL + fileName + " >> " + e.toString());
        }

        // ------------------------------------------
        // Return the filePath to open
        // ------------------------------------------
        if (!isGedCopyOK) {
            path = Jade.getInstance().getRootUrl();
            filePath = path + "persistence/" + fileName;
        } else {
            try {
                // gedURL = this.getGEDExportClientURL();
                gedURL = null;
                filePath = gedURL + fileName; // provoque exception si gedURL == null
            } catch (Exception ex) {
                JadeLogger.error(this,
                        "Error getting doc file from Ged URL " + gedURL + fileName + " >> " + ex.toString());
                path = Jade.getInstance().getRootUrl();
                filePath = path + "persistence/" + fileName;
            }
        }

        return filePath;
    }

}