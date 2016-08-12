/*
 * Créé le 13 janv. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.process;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.print.CO00CSommationPaiement;
import globaz.aquila.print.COJournalContentieux_Doc;
import globaz.aquila.print.list.COListParOP;
import globaz.aquila.process.batch.COAmorcerContentieux;
import globaz.aquila.process.batch.COBloquerSectionsBN;
import globaz.aquila.process.batch.COEffectuerTransitions;
import globaz.aquila.process.batch.utils.COAbstractJournalContentieuxExcelml;
import globaz.aquila.process.batch.utils.COImprimerJournalContentieuxExcelml;
import globaz.aquila.process.batch.utils.COImprimerJournalContentieuxInfoComplementaireExcelml;
import globaz.aquila.process.batch.utils.COImprimerListPourOP;
import globaz.aquila.process.batch.utils.COImprimerListeDeclenchement;
import globaz.aquila.process.batch.utils.COJournalAdapterBatch;
import globaz.aquila.process.batch.utils.COProcessContentieuxUtils;
import globaz.aquila.service.cataloguetxt.cache.COCacheCatalogueFacade;
import globaz.aquila.vb.process.COSelection;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.api.APISection;
import globaz.osiris.process.journal.CAUtilsJournal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <H1>Description</H1>
 * <p>
 * Fonctionne en deux étapes:
 * </p>
 * <ol>
 * <li>Examine toutes les sections de la compta aux qui sont des candidats potentiels pour le contentieux et crée les
 * contentieux si nécessaire.</li>
 * <li>Examine tous les contentieux et effectue les transitions d'étapes si nécessaire.</li>
 * </ol>
 * <p>
 * De nombreaux critéres peuvent être transmis pour sélectionner les sections, comptes annexes, rôles des affiliés,
 * séquences ou étapes par séquences à considérer dans le processus.
 * </p>
 * <p>
 * Le process peut être lancé dans un mode appellé prévisionnel qui correspond à une sorte de simulation durant laquelle
 * aucune modification ne sera stockée dans la base.
 * </p>
 * <p>
 * Si le mode prévisionnel n'est pas enclenché, toutes les écritures comptables sont faites dans un nouveau journal de
 * type contentieux.
 * </p>
 * <p>
 * Les documents renvoyés par le process sont une liste des transitions globales et une par office.
 * </p>
 * 
 * @author vre
 */
public class COProcessContentieux extends BProcess {

    private static final String PROCESSUS_ANNULE = "Processus annulé !";

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = -1988432622027658130L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String beforeNoAffilie;
    private String dateDelaiPaiement;
    private String dateReference;
    private String dateSurDocument;

    private Boolean executeTraitementSpecifique = new Boolean(false);
    private String forIdCategorie;
    private String forIdGenreCompte;

    private String forIdSequence;
    private String fromNoAffilie;
    private Boolean imprimerDocument = Boolean.FALSE;
    private Boolean imprimerJournalContentieuxExcelml = Boolean.FALSE;
    private Boolean imprimerListeDeclenchement = Boolean.FALSE;
    private Boolean imprimerListePourOP = Boolean.FALSE;
    private String libelleJournal;
    private String listEtapes = null;
    private String listRole = null;
    private String listTypesSections = null;
    // champs de configuration du process (serilaizables)
    private Boolean previsionnel = Boolean.FALSE;

    private List /* String (idRole) */<String> roles;

    // Etapes sélectionnées
    private Map /* String (idSequence) > COSelection */<String, COSelection> selections;

    private String selectionTriListeCA;
    private String selectionTriListeSection;

    private List /* String (idTypeSection) */<String> typesSections;
    private JadeUser user = null;
    private String userIdCollaborateur;

    /**
     * Crée une nouvelle instance de la classe COProcessContentieux.
     */
    public COProcessContentieux() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe COProcessContentieux.
     * 
     * @param parent
     */
    public COProcessContentieux(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe COProcessContentieux.
     * 
     * @param session
     */
    public COProcessContentieux(BSession session) {
        super(session);
    }

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
        try {
            COCacheCatalogueFacade.getInstance().startCache(this);
            if (!new CAUtilsJournal().isPeriodeComptableOuverte(getSession(), getTransaction(), getDateSurDocument())) {
                return false;
            }

            // Traitement si exécuté en ligne de commande
            traitementListLigneCommand();

            // Si bulletin neutre :
            traitementBloquerSectionsBN();

            COJournalAdapterBatch journal = new COJournalAdapterBatch(libelleJournal, getDateSurDocument(),
                    previsionnel.booleanValue());
            COImprimerListeDeclenchement listeDeclenchement = new COImprimerListeDeclenchement(getDateSurDocument(),
                    getDateReference(), getPrevisionnel().booleanValue());
            COImprimerListPourOP listePourOP = new COImprimerListPourOP(this, getDateSurDocument(), getDateReference(),
                    getPrevisionnel().booleanValue());

            COAbstractJournalContentieuxExcelml journalContentieuxExcelml = null;

            // TODO : A chercher la propriété pour l'activation du journal avec info complémentaire
            boolean isJournalContentieuxComplementaireActive = false;
            // TODO : A faire les trois conditions pour instancier le bon journal
            if (imprimerJournalContentieuxExcelml && isJournalContentieuxComplementaireActive) {
                journalContentieuxExcelml = new COImprimerJournalContentieuxInfoComplementaireExcelml(
                        getDateReference(), getDateSurDocument(), getPrevisionnel().booleanValue(), getRoles());
            } else {
                journalContentieuxExcelml = new COImprimerJournalContentieuxExcelml(getDateReference(),
                        getDateSurDocument(), getPrevisionnel().booleanValue(), getRoles());
            }

            if (isAborted()) {
                return false;
            }

            try {
                if (!previsionnel.booleanValue()) {
                    // on crée le journal
                    journal.getJournal(getSession(), getTransaction());
                    // On valide pour être sur d'avoir un journal et qu'il ne soit pas rollbacké par une exception.
                    getTransaction().commit();
                }

                if (!isAborted()) {
                    amorcerContentieux(journal, listeDeclenchement, listePourOP, journalContentieuxExcelml);
                }

                if (!isAborted() && !COProcessContentieuxUtils.isOnlyCreerContentieux(getSession(), getSelections())) {
                    effectuerTransitions(journal, listeDeclenchement, listePourOP, journalContentieuxExcelml);
                }

                comptabiliserJournal(journal);

                if (isAborted()) {
                    getMemoryLog().logMessage(COProcessContentieux.PROCESSUS_ANNULE, FWViewBeanInterface.WARNING,
                            this.getClass().getName());
                }

                mergeTransitionsPDF();

                creerDocumentsListes(listeDeclenchement, listePourOP, journalContentieuxExcelml);

            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
                return false;
            }

        } finally {
            COCacheCatalogueFacade.getInstance().releaseCache(this);
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        // Do nothing
    }

    /**
     * @see COAmorcerContentieux#amorcerContentieux(BProcess, BSession, BTransaction)
     * @param journal
     * @param listeDeclenchement
     * @param listePourOP
     * @throws Exception
     */
    private void amorcerContentieux(COJournalAdapterBatch journal, COImprimerListeDeclenchement listeDeclenchement,
            COImprimerListPourOP listePourOP, COAbstractJournalContentieuxExcelml journalContentieuxExcelml)
            throws Exception {
        COAmorcerContentieux amorcer = new COAmorcerContentieux();

        amorcer.setDateReference(getDateReference());
        amorcer.setForIdCategorie(getForIdCategorie());
        amorcer.setForIdGenreCompte(getForIdGenreCompte());
        amorcer.setFromNoAffilie(getFromNoAffilie());
        amorcer.setBeforeNoAffilie(getBeforeNoAffilie());
        amorcer.setSelectionTriListeCA(getSelectionTriListeCA());
        amorcer.setSelectionTriListeSection(getSelectionTriListeSection());
        amorcer.setSelections(getSelections());
        amorcer.setRoles(getRoles());
        amorcer.setTypesSections(getTypesSections());
        amorcer.setDateSurDocument(getDateSurDocument());
        amorcer.setDateDelaiPaiement(getDateDelaiPaiement());

        amorcer.setJournalAdapterBatch(journal);

        amorcer.setImprimerListeDeclenchement(getImprimerListeDeclenchement().booleanValue());
        amorcer.setListeDeclenchement(listeDeclenchement);
        amorcer.setImprimerDocument(getImprimerDocument().booleanValue());
        amorcer.setImprimerJournalContentieuxExcelml(getImprimerJournalContentieuxExcelml().booleanValue());
        amorcer.setJournalContentieuxExcelml(journalContentieuxExcelml);

        amorcer.setImprimerListePourOP(getImprimerListePourOP().booleanValue());
        amorcer.setListePourOP(listePourOP);

        amorcer.setPrevisionnel(getPrevisionnel().booleanValue());

        amorcer.amorcerContentieux(this, getSession(), getTransaction());
    }

    /**
     * Comptabiliser les journaux de comptabilité auxiliaire, si nécessaire.
     * 
     * @param journal
     * @throws Exception
     */
    private void comptabiliserJournal(COJournalAdapterBatch journal) throws Exception {
        if (!previsionnel.booleanValue() && (journal.getJournalAdapter() != null)) {
            setState(getSession().getLabel("AQUILA_COMPTABILISE_JOURNAL"));
            journal.getJournal(getSession(), getTransaction()).comptabiliser(getTransaction(), this);

            if ((journal.getJournalAdapter().getJournal().getMemoryLog() != null)
                    && journal.getJournalAdapter().getJournal().getMemoryLog().hasErrors()) {
                getMemoryLog().logMessage(journal.getJournalAdapter().getJournal().getMemoryLog());
            }
        }
    }

    /**
     * Créer les listes de documents.
     * 
     * @param listeDeclenchement
     * @param listPourOP
     * @throws Exception
     */
    private void creerDocumentsListes(COImprimerListeDeclenchement listeDeclenchement,
            COImprimerListPourOP listePourOP, COAbstractJournalContentieuxExcelml journalContentieuxExcelml)
            throws Exception {
        setState(getSession().getLabel("AQUILA_CREE_DOCS_RECAP"));

        if (imprimerListeDeclenchement.booleanValue() && (listeDeclenchement.getReport() != null)) {
            setState(getSession().getLabel("6203"));

            COJournalContentieux_Doc impressionProcess = new COJournalContentieux_Doc(getSession());

            impressionProcess.setParent(this);
            impressionProcess.setSession(getSession());

            impressionProcess.setDate(getDateSurDocument());
            impressionProcess.setDateReference(getDateReference());
            impressionProcess.setModePrevisionnel(previsionnel.booleanValue());
            impressionProcess.bindData(listeDeclenchement.getReport());
            impressionProcess.setSendCompletionMail(false);
            impressionProcess.setSendMailOnError(false);

            impressionProcess.executeProcess();

            // Attache le document créé au process
            for (Iterator it = impressionProcess.getAttachedDocuments().iterator(); it.hasNext();) {
                getParent().getAttachedDocuments().add(it.next());
            }
        }

        if (imprimerListePourOP.booleanValue() && (listePourOP.getListePourOP() != null)) {
            setState(getSession().getLabel("6216"));

            listePourOP.getListePourOP().setFinalizeSheet();
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setPublishDocument(true);
            docInfo.setArchiveDocument(false);
            docInfo.setDocumentTypeNumber(COListParOP.NUMERO_REFERENCE_INFOROM);
            this.registerAttachedDocument(docInfo, listePourOP.getListePourOP().getOutputFile());
        }

        // le containerJournalContentieuxExcelml n'est jamais null car il est instancié dans le constructeur
        if (imprimerJournalContentieuxExcelml
                && (journalContentieuxExcelml.getContainerJournalContentieuxExcelml().size() >= 1)) {
            journalContentieuxExcelml.addHeaderInExcelml(getSession());
            String journalContentieuxExcelmlFilePath = journalContentieuxExcelml
                    .createFileJournalContentieuxExcelml(getSession());
            registerFileJournalContentieuxExcelml(journalContentieuxExcelmlFilePath, journalContentieuxExcelml);
        }

    }

    /**
     * Effectue les transitions pour tous les contentieux sélectionné.
     * 
     * @param journal
     * @param listeDeclenchement
     * @param listePourOP
     * @throws Exception
     */
    private void effectuerTransitions(COJournalAdapterBatch journal, COImprimerListeDeclenchement listeDeclenchement,
            COImprimerListPourOP listePourOP, COAbstractJournalContentieuxExcelml journalContentieuxExcelml)
            throws Exception {
        COEffectuerTransitions effectuer = new COEffectuerTransitions();

        effectuer.setDateReference(getDateReference());
        effectuer.setForIdCategorie(getForIdCategorie());
        effectuer.setForIdGenreCompte(getForIdGenreCompte());
        effectuer.setFromNoAffilie(getFromNoAffilie());
        effectuer.setBeforeNoAffilie(getBeforeNoAffilie());
        effectuer.setSelectionTriListeCA(getSelectionTriListeCA());
        effectuer.setSelectionTriListeSection(getSelectionTriListeSection());
        effectuer.setRoles(getRoles());
        effectuer.setTypesSections(getTypesSections());
        effectuer.setSelections(getSelections());
        effectuer.setDateSurDocument(getDateSurDocument());
        effectuer.setDateDelaiPaiement(getDateDelaiPaiement());
        effectuer.setExecuteTraitementSpecifique(isExecuteTraitementSpecifique());
        effectuer.setForIdSequence(forIdSequence);

        effectuer.setJournalAdapterBatch(journal);

        effectuer.setImprimerListeDeclenchement(getImprimerListeDeclenchement().booleanValue());
        effectuer.setListeDeclenchement(listeDeclenchement);

        effectuer.setImprimerJournalContentieuxExcelml(getImprimerJournalContentieuxExcelml().booleanValue());
        effectuer.setJournalContentieuxExcelml(journalContentieuxExcelml);

        effectuer.setImprimerListePourOP(getImprimerListePourOP().booleanValue());
        effectuer.setListePourOP(listePourOP);
        effectuer.setImprimerDocument(getImprimerDocument());

        effectuer.setPrevisionnel(getPrevisionnel().booleanValue());

        effectuer.effectuerTransitions(this, getSession(), getTransaction());
    }

    /**
     * getter pour l'attribut to no affilie.
     * 
     * @return la valeur courante de l'attribut to no affilie
     */
    public String getBeforeNoAffilie() {
        return beforeNoAffilie;
    }

    /**
     * getter pour l'attribut date delai paiement.
     * 
     * @return la valeur courante de l'attribut date delai paiement
     */
    public String getDateDelaiPaiement() {
        return dateDelaiPaiement;
    }

    /**
     * getter pour l'attribut date reference.
     * 
     * @return la valeur courante de l'attribut date reference
     */
    public String getDateReference() {
        return dateReference;
    }

    /**
     * getter pour l'attribut date sur document.
     * 
     * @return la valeur courante de l'attribut date sur document
     */
    public String getDateSurDocument() {
        return dateSurDocument;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("AQUILA_PROCESS_CONTENTIEUX");
    }

    public Boolean getExecuteTraitementSpecifique() {
        return executeTraitementSpecifique;
    }

    /**
     * getter pour l'attribut for id categorie.
     * 
     * @return la valeur courante de l'attribut for id categorie
     */
    public String getForIdCategorie() {
        return forIdCategorie;
    }

    /**
     * getter pour l'attribut for id genre compte.
     * 
     * @return la valeur courante de l'attribut for id genre compte
     */
    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    /**
     * @return the forIdSequence
     */
    public String getForIdSequence() {
        return forIdSequence;
    }

    /**
     * getter pour l'attribut from no affilie.
     * 
     * @return la valeur courante de l'attribut from no affilie
     */
    public String getFromNoAffilie() {
        return fromNoAffilie;
    }

    /**
     * getter pour l'attribut imprimer document.
     * 
     * @return la valeur courante de l'attribut imprimer document
     */
    public Boolean getImprimerDocument() {
        return imprimerDocument;
    }

    public Boolean getImprimerJournalContentieuxExcelml() {
        return imprimerJournalContentieuxExcelml;
    }

    /**
     * getter pour l'attribut imprimer liste declenchement.
     * 
     * @return la valeur courante de l'attribut imprimer liste declenchement
     */
    public Boolean getImprimerListeDeclenchement() {
        return imprimerListeDeclenchement;
    }

    /**
     * getter pour l'attribut imprimer liste pour OP.
     * 
     * @return la valeur courante de l'attribut imprimer liste pour OP
     */
    public Boolean getImprimerListePourOP() {
        return imprimerListePourOP;
    }

    /**
     * getter pour l'attribut libelle journal.
     * 
     * @return la valeur courante de l'attribut libelle journal
     */
    public String getLibelleJournal() {
        return libelleJournal;
    }

    /**
     * @return the listEtapes
     */
    public String getListEtapes() {
        return listEtapes;
    }

    /**
     * @return the listRole
     */
    public String getListRole() {
        return listRole;
    }

    /**
     * @return the listTypesSections
     */
    public String getListTypesSections() {
        return listTypesSections;
    }

    /**
     * getter pour l'attribut previsionnel.
     * 
     * @return la valeur courante de l'attribut previsionnel
     */
    public Boolean getPrevisionnel() {
        return previsionnel;
    }

    /**
     * getter pour l'attribut roles.
     * 
     * @return la valeur courante de l'attribut roles
     */
    public List<String> getRoles() {
        return roles;
    }

    /**
     * getter pour l'attribut selections.
     * 
     * @return la valeur courante de l'attribut selections
     */
    public Map<String, COSelection> getSelections() {
        return selections;
    }

    /**
     * getter pour l'attribut selection tri liste CA.
     * 
     * @return la valeur courante de l'attribut selection tri liste CA
     */
    public String getSelectionTriListeCA() {
        return selectionTriListeCA;
    }

    /**
     * getter pour l'attribut selection tri liste section.
     * 
     * @return la valeur courante de l'attribut selection tri liste section
     */
    public String getSelectionTriListeSection() {
        return selectionTriListeSection;
    }

    /**
     * getter pour l'attribut types sections.
     * 
     * @return la valeur courante de l'attribut types sections
     */
    public List<String> getTypesSections() {
        return typesSections;
    }

    /**
     * @return the user
     * @throws Exception
     */
    public JadeUser getUser() throws Exception {
        if (user == null) {
            user = JadeAdminServiceLocatorProvider.getLocator().getUserService().load(getUserIdCollaborateur());
        }
        return user;
    }

    /**
     * @return the userIdCollaborateur
     */
    public String getUserIdCollaborateur() {
        if (JadeStringUtil.isBlank(userIdCollaborateur)) {
            userIdCollaborateur = getSession().getUserId();
        }
        return userIdCollaborateur;
    }

    public boolean isExecuteTraitementSpecifique() {
        return getExecuteTraitementSpecifique().booleanValue();
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Merge les PDFs générés par les transistions.
     * 
     * @throws Exception
     */
    private void mergeTransitionsPDF() throws Exception {
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);

        // HACK sel : l''Agence de Lausanne avait un problème de document PDF qui s'écrasait car ils suppriment l'UID du
        // document. Le traitement du contentieux crée des lots de 500 dont le nom du fichier est identique.
        // Afin de gérer cette problématique, nous ajoutons en dur du TypeNumber de la sommation au doc info parent afin
        // que le framework puisse gérer les noms.
        // Le PDF final peut contenir différents documents de type différents.
        // voir Bug 5353
        docInfo.setDocumentType(CO00CSommationPaiement.NUMERO_REFERENCE_INFOROM);
        docInfo.setDocumentTypeNumber(CO00CSommationPaiement.NUMERO_REFERENCE_INFOROM);

        /*
         * Configuration dans JadePublishResourceLocator.xml Exemple de configuration :
         * 
         * <resource name="switchPrevisionel" class="globaz.jade.publish.provider.switcher.JadePublishResourceImpl" >
         * <default.resource>PdfProperty</default.resource> <properties> <property name="previsionnel" value="true">
         * <resource>PdfPropertyPrevisionnel</resource> </property> </properties> </resource>
         */
        if (getPrevisionnel().booleanValue()) {
            docInfo.setDocumentProperty("previsionnel", "true");
        } else {
            docInfo.setDocumentProperty("previsionnel", "false");
        }

        this.mergePDF(docInfo, false, 500, false, null, null);
    }

    private void registerFileJournalContentieuxExcelml(String docPath, COAbstractJournalContentieuxExcelml journal)
            throws Exception {

        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        docInfoExcel.setApplicationDomain(ICOApplication.DEFAULT_APPLICATION_AQUILA);
        docInfoExcel.setDocumentTitle(journal.getModelNameOutput());
        docInfoExcel
                .setDocumentTypeNumber(COAbstractJournalContentieuxExcelml.EXCELML_JOURNAL_CONTENTIEUX_NUMERO_INFOROM);
        docInfoExcel.setPublishDocument(true);
        docInfoExcel.setArchiveDocument(false);
        this.registerAttachedDocument(docInfoExcel, docPath);
    }

    /**
     * setter pour l'attribut to no affilie.
     * 
     * @param toNoAffilie
     *            une nouvelle valeur pour cet attribut
     */
    public void setBeforeNoAffilie(String toNoAffilie) {
        beforeNoAffilie = toNoAffilie;
    }

    /**
     * setter pour l'attribut date delai paiement.
     * 
     * @param dateDelaiPaiement
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDelaiPaiement(String dateDelaiPaiement) {
        this.dateDelaiPaiement = dateDelaiPaiement;
    }

    /**
     * setter pour l'attribut date reference.
     * 
     * @param dateReference
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateReference(String dateReference) {
        this.dateReference = dateReference;
    }

    /**
     * setter pour l'attribut date sur document.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateSurDocument(String string) {
        dateSurDocument = string;
    }

    public void setExecuteTraitementSpecifique(Boolean executeTraitementSpecifique) {
        this.executeTraitementSpecifique = executeTraitementSpecifique;
    }

    /**
     * setter pour l'attribut for id categorie.
     * 
     * @param forIdCategorie
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdCategorie(String forIdCategorie) {
        this.forIdCategorie = forIdCategorie;
    }

    /**
     * setter pour l'attribut for id genre compte.
     * 
     * @param forIdGenreCompte
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdGenreCompte(String forIdGenreCompte) {
        this.forIdGenreCompte = forIdGenreCompte;
    }

    /**
     * @param forIdSequence
     *            the forIdSequence to set
     */
    public void setForIdSequence(String forIdSequence) {
        this.forIdSequence = forIdSequence;
    }

    /**
     * setter pour l'attribut from no affilie.
     * 
     * @param fromNoAffilie
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromNoAffilie(String fromNoAffilie) {
        this.fromNoAffilie = fromNoAffilie;
    }

    /**
     * setter pour l'attribut imprimer document.
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setImprimerDocument(Boolean boolean1) {
        imprimerDocument = boolean1;
    }

    public void setImprimerJournalContentieuxExcelml(Boolean imprimerJournalContentieuxExcelml) {
        this.imprimerJournalContentieuxExcelml = imprimerJournalContentieuxExcelml;
    }

    /**
     * setter pour l'attribut imprimer liste declenchement.
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setImprimerListeDeclenchement(Boolean boolean1) {
        imprimerListeDeclenchement = boolean1;
    }

    /**
     * setter pour l'attribut imprimer liste pour OP.
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setImprimerListePourOP(Boolean boolean1) {
        imprimerListePourOP = boolean1;
    }

    /**
     * setter pour l'attribut libelle journal.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setLibelleJournal(String string) {
        libelleJournal = string;
    }

    /**
     * @param listEtapes
     *            the listEtapes to set
     */
    public void setListEtapes(String listEtapes) {
        this.listEtapes = listEtapes;
    }

    /**
     * @param listRole
     *            the listRole to set
     */
    public void setListRole(String listRole) {
        this.listRole = listRole;
    }

    /**
     * @param listTypesSections
     *            the listTypesSections to set
     */
    public void setListTypesSections(String listTypesSections) {
        this.listTypesSections = listTypesSections;
    }

    /**
     * setter pour l'attribut previsionnel.
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setPrevisionnel(Boolean boolean1) {
        previsionnel = boolean1;
    }

    /**
     * setter pour l'attribut roles.
     * 
     * @param list
     *            une nouvelle valeur pour cet attribut
     */
    public void setRoles(List list) {
        roles = list;
    }

    /**
     * setter pour l'attribut selections.
     * 
     * @param map
     *            une nouvelle valeur pour cet attribut
     */
    public void setSelections(Map map) {
        selections = map;
    }

    /**
     * setter pour l'attribut selection tri liste CA.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setSelectionTriListeCA(String string) {
        selectionTriListeCA = string;
    }

    /**
     * setter pour l'attribut selection tri liste section.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setSelectionTriListeSection(String string) {
        selectionTriListeSection = string;
    }

    /**
     * setter pour l'attribut types sections.
     * 
     * @param list
     *            une nouvelle valeur pour cet attribut
     */
    public void setTypesSections(List list) {
        typesSections = list;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(JadeUser user) {
        this.user = user;
    }

    /**
     * @param userIdCollaborateur
     *            the userIdCollaborateur to set
     */
    public void setUserIdCollaborateur(String userIdCollaborateur) {
        this.userIdCollaborateur = userIdCollaborateur;
    }

    /**
     * Bloque les sections comportant un décompte final. <br/>
     * Ce traitement est exécuté uniquement pour la séquence 4 et pour le type de section "Bulletin Neutre".<br/>
     * Ce traitement comporte un commit de la transaction en mode prévisionnel ou définitif.
     * 
     * @throws Exception
     */
    private void traitementBloquerSectionsBN() throws Exception {
        if ((selections.size() == 1) && selections.containsKey("4")
                && typesSections.contains(APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE)) {
            try {
                COBloquerSectionsBN.bloquerSectionsBN(getTransaction(), getDateReference());
                if (!getTransaction().hasErrors()) {
                    getTransaction().commit();
                } else {
                    getTransaction().rollback();
                    getMemoryLog().logMessage(
                            "Error in COBloquerSectionsBN.bloquerSectionsBN - " + getTransaction().getErrors(),
                            FWMessage.ERREUR, this.getClass().getName());
                    getTransaction().clearErrorBuffer();
                }
            } catch (Exception e) {
                getTransaction().rollback();
                getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, this.getClass().getName());
            }
        }
    }

    /**
     * Transforme les listes en format String au format List ou Map selon l'attribut
     */
    private void traitementListLigneCommand() {
        if (!JadeStringUtil.isBlank(getListEtapes())) {
            selections = new HashMap<String, COSelection>();
            selections.put(getForIdSequence(), new COSelection(getForIdSequence(), getListEtapes().split(",")));
        }

        if (!JadeStringUtil.isBlank(getListRole())) {
            roles = JadeStringUtil.tokenize(getListRole(), ",");
        }

        if (!JadeStringUtil.isBlank(getListTypesSections())) {
            typesSections = JadeStringUtil.tokenize(getListTypesSections(), ",");
        }
    }
}
