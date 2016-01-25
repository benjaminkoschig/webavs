/*
 * Created on Jul 3, 2006
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 * Comments
 */
package globaz.naos.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.annonceAffilie.AFAnnonceAffilie;
import globaz.naos.db.annonceAffilie.AFAnnonceAffilieDistinctManager;
import globaz.naos.db.annonceAffilie.AFAnnonceAffilieManager;
import globaz.naos.itext.AFAvisMutationDocument;
import globaz.naos.itext.affiliation.AFBordereauMutation_Doc;
import globaz.naos.itext.affiliation.AFCodeSystemMediator;
import globaz.naos.itext.affiliation.AFFicheCartotheque_Doc;
import globaz.naos.process.fichierCentral.AFExportFichierCentralXmlParser;
import globaz.webavs.common.CommonProperties;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Imprime les annonces des affiliers dont il y a eu un changement
 * 
 * @author Alexandre Cuva
 * 
 *         To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code
 *         Generation&gt;Code and Comments
 */
public class AFImpressionAnnonceBatch extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String affiliationId = "";

    private String dateAnnonce = "";

    public AFImpressionAnnonceBatch() {
        super();
    }

    public AFImpressionAnnonceBatch(BProcess parent) {
        super(parent);
    }

    public AFImpressionAnnonceBatch(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        // Init common properties
        BStatement statement = null;
        AFAnnonceAffilie entity = null;
        String numAffilie = null;
        Map avisMutationMap = new Hashtable();
        Map bordereauMap = new Hashtable();
        Map cartothequeMap = new Hashtable();
        Map agenceCartotheque = new TreeMap();
        Map agenceCartothequePrive = new TreeMap();

        AFCodeSystemMediator mediator = new AFCodeSystemMediator(getSession(), getTransaction());
        AFAnnonceAffilieDistinctManager manager = new AFAnnonceAffilieDistinctManager();
        AFAnnonceAffilieDistinctManager cloneManager;
        manager.setSession(getSession());
        if (!JadeStringUtil.isIntegerEmpty(getAffiliationId())) {
            manager.setForAffiliationId(getAffiliationId());
        }
        if (!JadeStringUtil.isEmpty(getDateAnnonce())) {
            manager.setForDateAnnonce(getDateAnnonce());
            manager.forTraitement("2");
        } else {
            manager.forTraitement("1");
        }

        cloneManager = (AFAnnonceAffilieDistinctManager) manager.clone();
        cloneManager.setUseParent(true);
        long count = 0;
        try {
            statement = manager.cursorOpen(getTransaction());

            while ((entity = (AFAnnonceAffilie) manager.cursorReadNext(statement)) != null) {
                numAffilie = entity.getAffiliationId();
                if (!JadeStringUtil.isIntegerEmpty(numAffilie)) {
                    mediator.check(numAffilie, getDateAnnonce());
                    if (mediator.isAvisMutation() && (mediator.getAvisModificationList().size() > 0)) {
                        // System.out.println("ajout avis mutation pour "+numAffilie);
                        avisMutationMap.put(numAffilie, mediator.getAvisModificationList());
                    }
                    if (mediator.isBordereauMutation() && (mediator.getBordereauModificationList().size() > 0)) {
                        // System.out.println("ajout border pour bordereau"+numAffilie);
                        bordereauMap.put(numAffilie, mediator.getBordereauModificationList());
                    }
                    if (mediator.isCartoTheque() && (mediator.getCartoModificationList().size() > 0)) {
                        // System.out.println("ajout carthoteque pour "+numAffilie);
                        cartothequeMap.put(numAffilie, mediator.getCartoModificationList());
                        // recherche de l'agence pour les tris
                        String agence = getAgence(numAffilie, dateAnnonce, AFAffiliation.LIEN_AGENCE);
                        // System.out.println("  agence comm "+agence);
                        if (agence != null) {
                            addAffToAgence(numAffilie, agenceCartotheque, agence);
                        }
                        // recherche de l'agence privé pour réimpression
                        agence = getAgence(numAffilie, dateAnnonce, AFAffiliation.LIEN_AGENCE_PRIVE);
                        // System.out.println("  agence comm privée "+agence);
                        if (agence != null) {
                            addAffToAgence(numAffilie, agenceCartothequePrive, agence);
                        }
                    }
                    if (JadeLogger.isDebugEnabled()) {
                        count++;
                    }
                    if (JadeLogger.isDebugEnabled() && (count > 10)) {
                        break;
                    }
                }

            }
            long cntTotal = avisMutationMap.size() + bordereauMap.size() + cartothequeMap.size();
            setProgressScaleValue(cntTotal + (2 * cntTotal / 100));

            // Create the documents
            if (!isAborted()) {
                printAvisMuttation(avisMutationMap);
            }
            // mergePDF(createDocumentInfo(), false, 0, true, null);
            if (!isAborted()) {
                printBordereauMuttation(bordereauMap);
            }
            // mergePDF(createDocumentInfo(), false, 0, true, null);
            if (!isAborted()) {
                printCartotheque(cartothequeMap, agenceCartotheque);
            }
            // mergePDF(createDocumentInfo(), false, 0, true, null);
            if (!isAborted()) {
                printCartotheque(cartothequeMap, agenceCartothequePrive);
            }
            // Merge created document
            this.mergePDF(createDocumentInfo(), true, 0, false, null);
            setProgressCounter(getProgressCounter() + cntTotal / 100);

            // Close any Annonce made
            if (!JadeLogger.isDebugEnabled() && !isAborted()) {
                closeAnnonces(avisMutationMap.keySet(), bordereauMap.keySet(), cartothequeMap.keySet(),
                        getDateAnnonce());
            }
            setProgressCounter(getProgressCounter() + cntTotal / 100);
            return true;
        } catch (Throwable t) {
            this._addError("erreur durant la génération des documents: " + t.getMessage());
            abort();
            return false;
        } finally {
            if (statement != null) {
                manager.cursorClose(statement);
                statement = null;
            }
        }
    }

    private void addAffToAgence(String numAffilie, Map agenceCartotheque, String agence) {
        if (agenceCartotheque.containsKey(agence)) {
            ((List) agenceCartotheque.get(agence)).add(numAffilie);
        } else {
            ArrayList list = new ArrayList();
            list.add(numAffilie);
            agenceCartotheque.put(agence, list);
        }
    }

    private void closeAnnonces(Collection avisMutationSet, Collection bordereauSet, Collection cartothequeSet,
            String date) throws Exception {

        // Create the unique list
        Set affilieIDList = new HashSet();
        affilieIDList.addAll(avisMutationSet);
        affilieIDList.addAll(bordereauSet);
        affilieIDList.addAll(cartothequeSet);

        // Instance of AFAnnonceAffilie
        AFAnnonceAffilie entity = null;
        AFAnnonceAffilieManager manager = new AFAnnonceAffilieManager();
        manager.setSession(getSession());
        if (!JadeStringUtil.isEmpty(date)) {
            manager.setForDateAnnonce(date);
        } else {
            manager.forTraitement("1");
        }
        if (!affilieIDList.isEmpty()) {
            Iterator it = affilieIDList.iterator();
            while (it.hasNext()) {
                manager.setForAffiliationId((String) it.next());
                manager.find();
                Iterator itAnnonce = manager.iterator();
                while (itAnnonce.hasNext()) {
                    entity = (AFAnnonceAffilie) itAnnonce.next();
                    if (JadeStringUtil.isEmpty(date)) {
                        // mise à jour de l'annonce si nouvelle impression
                        entity.setTraitement(Boolean.FALSE);
                        entity.setDateAnnonce(JACalendar.todayJJsMMsAAAA());
                        entity.update();
                    }
                }
            }
        }
    }

    public String getAffiliationId() {
        return affiliationId;
    }

    private String getAgence(String numAffilie, String date, String typeAgence) throws Exception {
        String code = "0";

        AFAffiliation aff = new AFAffiliation();
        aff.setSession(getSession());
        aff.setAffiliationId(numAffilie);
        aff.retrieve();
        if (!aff.isNew()) {
            code = aff.getAgenceComNum(null, date, typeAgence);
            if (JadeStringUtil.isEmpty(code)) {
                if (AFAffiliation.LIEN_AGENCE_PRIVE.equals(typeAgence)) {
                    return null;
                }
                code = "0";
            }
            if (code.equals("9998") || code.equals("9999")) {
                // agence fictive, on ne retiens pas l'affilié
                return null;
            }
            if (code.equals("6711")) {
                // si agence delémont, ne prendre en compte que les radiés
                if (JadeStringUtil.isEmpty(aff.getDateFin())) {
                    // si pas radié, ne pas imprimer
                    return null;
                }
            }
        }
        return code;
    }

    /**
     * @return
     */
    public String getDateAnnonce() {
        return dateAnnonce;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("NAOS_IMPRESSION_MUTATION_SUJET");
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    private void printAvisMuttation(Map pNumAffilie) throws Throwable {
        if (!pNumAffilie.isEmpty()) {
            AFAvisMutationDocument doc;
            Iterator it = pNumAffilie.keySet().iterator();
            // gestion électronique
            String noCaisse = (getSession().getApplication()).getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE);
            AFExportFichierCentralXmlParser exporter = new AFExportFichierCentralXmlParser(noCaisse.trim());
            while (it.hasNext()) {
                if (!isAborted()) {
                    doc = new AFAvisMutationDocument();
                    doc.setParent(this);
                    doc.setIdAffiliation((String) it.next());
                    doc.setModifications((List) pNumAffilie.get(doc.getIdAffiliation()));
                    // exporter xml pour annonces
                    doc.setExporterXML(exporter);
                    // DGI déplacé dans AFAvisMutationDocument
                    // doc.setSelectionImpression((String)pNumAffilie.get(doc.getIdAffiliation()));
                    doc.executeProcess();
                    setProgressCounter(getProgressCounter() + 1);
                }
            }
        }
    }

    private void printBordereauMuttation(Map pNumAffilie) throws Throwable {
        if (!pNumAffilie.isEmpty()) {
            AFBordereauMutation_Doc doc;
            Iterator it = pNumAffilie.keySet().iterator();
            while (it.hasNext()) {
                if (!isAborted()) {
                    doc = new AFBordereauMutation_Doc();
                    doc.setParent(this);
                    doc.setAffiliationId((String) it.next());
                    doc.setDisplayFields((List) pNumAffilie.get(doc.getAffiliationId()));
                    doc.executeProcess();
                    setProgressCounter(getProgressCounter() + 1);
                }
            }
        }
    }

    private void printCartotheque(Map pNumAffilie, Map agenceCartotheque) throws Throwable {
        if (!agenceCartotheque.isEmpty()) {
            AFFicheCartotheque_Doc doc;
            Iterator itAgence = agenceCartotheque.keySet().iterator();
            // itération sur les agences
            while (itAgence.hasNext()) {
                if (!isAborted()) {
                    String code = (String) itAgence.next();
                    // System.out.println("Impression cartho pour agence "+code);
                    List idAffs = (List) agenceCartotheque.get(code);
                    Iterator it = idAffs.iterator();
                    while (it.hasNext()) {
                        doc = new AFFicheCartotheque_Doc();
                        doc.setParent(this);
                        doc.setAffiliationId((String) it.next());
                        doc.setDisplayFields((List) pNumAffilie.get(doc.getAffiliationId()));
                        doc.executeProcess();
                        setProgressCounter(getProgressCounter() + 1);
                    } // pour tous les affiliés de l'agence
                }
            } // pour toutes les agences trouvées
        }
    }

    /**
     * @param string
     */
    public void setAffiliationId(String string) {
        affiliationId = string;
    }

    /**
     * @param string
     */
    public void setDateAnnonce(String string) {
        dateAnnonce = string;
    }
}
