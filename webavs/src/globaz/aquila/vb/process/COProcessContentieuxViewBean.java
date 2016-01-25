/*
 * Créé le 16 janv. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.vb.process;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.vb.COAbstractViewBeanSupport;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class COProcessContentieuxViewBean extends COAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * <H1>Description</H1>
     * <p>
     * DOCUMENT ME!
     * </p>
     * 
     * @author vre
     */
    public class EtapeOption {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        private COEtape etape;
        private boolean etapeDependCreer;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Creates a new EtapeOption object.
         * 
         * @param etape
         *            DOCUMENT ME!
         * @param etapeDependCreer
         *            DOCUMENT ME!
         */
        public EtapeOption(COEtape etape, boolean etapeDependCreer) {
            this.etape = etape;
            this.etapeDependCreer = etapeDependCreer;
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * DOCUMENT ME!
         * 
         * @return DOCUMENT ME!
         */
        public COEtape getEtape() {
            return etape;
        }

        /**
         * DOCUMENT ME!
         * 
         * @return DOCUMENT ME!
         */
        public boolean isEtapeCreer() {
            return (idSequenceToIdEtapeCreer != null)
                    && etape.getIdEtape().equals(idSequenceToIdEtapeCreer.get(etape.getIdSequence()));
        }

        /**
         * DOCUMENT ME!
         * 
         * @return DOCUMENT ME!
         */
        public boolean isEtapeDependCreer() {
            return etapeDependCreer;
        }
    }

    private String beforeNoAffilie = "";
    private String dateDelaiPaiement = "";
    private String dateReference = JACalendar.todayJJsMMsAAAA();
    private String dateSurDocument = JACalendar.todayJJsMMsAAAA();
    private String eMailAddress = "";
    private Boolean executeTraitementSpecifique = new Boolean(false);
    private String forIdCategorie = "";
    private String forIdGenreCompte = "";
    private String forIdSequence = "";
    private String fromNoAffilie = "";
    private List idRoles;
    private HashMap idSequenceToIdEtapeCreer;
    private HashMap idSequenceToListEtapeOption;
    private List idTypesSections;
    private Boolean imprimerDocument = Boolean.TRUE;
    private Boolean imprimerJournalContentieuxExcelml = Boolean.FALSE;
    private Boolean imprimerListeDeclenchement = Boolean.TRUE;
    private Boolean imprimerListePourOP = Boolean.FALSE;
    private String libelleJournal = "";
    private Boolean modePrevisionnel = Boolean.TRUE;
    private List roles;
    private HashMap selections = new HashMap();
    private String selectionTriListeCA = "";
    private String selectionTriListeSection = "";
    private List sequences;
    private List typesSections;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private String userIdCollaborateur = "";

    /**
     * Crée une nouvelle instance de la classe COProcessContentieuxViewBean.
     */
    public COProcessContentieuxViewBean() {
        super();
    }

    /**
     * @param etape
     *            DOCUMENT ME!
     * @param etapeDependCreer
     *            DOCUMENT ME!
     */
    public void addEtape(COEtape etape, boolean etapeDependCreer) {
        if (idSequenceToListEtapeOption == null) {
            idSequenceToListEtapeOption = new HashMap();
        }

        List etapesOptions = (List) idSequenceToListEtapeOption.get(etape.getIdSequence());

        if (etapesOptions == null) {
            etapesOptions = new LinkedList();
            idSequenceToListEtapeOption.put(etape.getIdSequence(), etapesOptions);
        }

        etapesOptions.add(new EtapeOption(etape, etapeDependCreer));

        if (ICOEtape.CS_CONTENTIEUX_CREE.equals(etape.getLibEtape())) {
            if (idSequenceToIdEtapeCreer == null) {
                idSequenceToIdEtapeCreer = new HashMap();
            }

            idSequenceToIdEtapeCreer.put(etape.getIdSequence(), etape.getIdEtape());
        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param idSequence
     *            DOCUMENT ME!
     * @param detailEtapes
     *            DOCUMENT ME!
     */
    public void addSelection(String idSequence, String[] detailEtapes) {
        selections.put(idSequence, new COSelection(idSequence, detailEtapes));
    }

    /**
	 */
    public void clearSelections() {
        selections.clear();
    }

    /**
     * getter pour l'attribut before no affilie.
     * 
     * @return la valeur courante de l'attribut before no affilie
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
     * getter pour l'attribut EMail address.
     * 
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        if (JadeStringUtil.isEmpty(eMailAddress)) {
            eMailAddress = ((BSession) getISession()).getUserEMail();
        }

        return eMailAddress;
    }

    /**
     * getter pour l'attribut idSequenceToListEtapeOption.
     * 
     * @param idSequence
     *            DOCUMENT ME!
     * @return la valeur courante de l'attribut idSequenceToListEtapeOption
     */
    public List getEtapesOptions(String idSequence) {
        return (List) idSequenceToListEtapeOption.get(idSequence);
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
     * DOCUMENT ME!
     * 
     * @param idSequence
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public String getIdEtapeCreer(String idSequence) {
        return (idSequenceToIdEtapeCreer != null) ? (String) idSequenceToIdEtapeCreer.get(idSequence) : "";
    }

    /**
     * getter pour l'attribut id roles.
     * 
     * @return la valeur courante de l'attribut id roles
     */
    public List getIdRoles() {
        return idRoles;
    }

    /**
     * getter pour l'attribut id types sections.
     * 
     * @return la valeur courante de l'attribut id types sections
     */
    public List getIdTypesSections() {
        return idTypesSections;
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
     * getter pour l'attribut modePrevisionnel.
     * 
     * @return la valeur courante de l'attribut modePrevisionnel
     */
    public Boolean getModePrevisionnel() {
        return modePrevisionnel;
    }

    /**
     * getter pour l'attribut roles.
     * 
     * @return la valeur courante de l'attribut roles
     */
    public List getRoles() {
        return roles;
    }

    /**
     * getter pour l'attribut selection.
     * 
     * @return la valeur courante de l'attribut selection
     */
    public Map getSelection() {
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
     * getter pour l'attribut sequences.
     * 
     * @return la valeur courante de l'attribut sequences
     */
    public List getSequences() {
        return sequences;
    }

    /**
     * getter pour l'attribut types sections.
     * 
     * @return la valeur courante de l'attribut types sections
     */
    public List getTypesSections() {
        return typesSections;
    }

    /**
     * @return the userIdCollaborateur
     */
    public String getUserIdCollaborateur() {
        return userIdCollaborateur;
    }

    public boolean isExecuteTraitementSpecifique() {
        return getExecuteTraitementSpecifique().booleanValue();
    }

    /**
     * getter pour l'attribut selected etape.
     * 
     * @param idSequence
     *            DOCUMENT ME!
     * @param idEtape
     *            DOCUMENT ME!
     * @return la valeur courante de l'attribut selected etape
     */
    public boolean isSelectedEtape(String idSequence, String idEtape) {
        COSelection selection = (COSelection) selections.get(idSequence);

        return (selection != null) && selection.getDetailEtapes().contains(idEtape);
    }

    /**
     * getter pour l'attribut selected id role.
     * 
     * @param idRole
     *            DOCUMENT ME!
     * @return la valeur courante de l'attribut selected id role
     */
    public boolean isSelectedIdRole(String idRole) {
        return (idRoles != null) && idRoles.contains(idRole);
    }

    /**
     * getter pour l'attribut selected id type section.
     * 
     * @param idTypeSection
     *            DOCUMENT ME!
     * @return la valeur courante de l'attribut selected id type section
     */
    public boolean isSelectedIdTypeSection(String idTypeSection) {
        return (idTypesSections != null) && idTypesSections.contains(idTypeSection);
    }

    /**
     * setter pour l'attribut before no affilie.
     * 
     * @param beforeNoAffilie
     *            une nouvelle valeur pour cet attribut
     */
    public void setBeforeNoAffilie(String beforeNoAffilie) {
        this.beforeNoAffilie = beforeNoAffilie;
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
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateReference(String string) {
        dateReference = string;
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

    /**
     * setter pour l'attribut EMail address.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
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
     * setter pour l'attribut id roles.
     * 
     * @param list
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdRoles(List list) {
        idRoles = list;
    }

    /**
     * setter pour l'attribut id types sections.
     * 
     * @param list
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTypesSections(List list) {
        idTypesSections = list;
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
     * setter pour l'attribut modePrevisionnel.
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setModePrevisionnel(Boolean boolean1) {
        modePrevisionnel = boolean1;
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
     * setter pour l'attribut sequences.
     * 
     * @param list
     *            une nouvelle valeur pour cet attribut
     */
    public void setSequences(List list) {
        sequences = list;
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
     * @param userIdCollaborateur
     *            the userIdCollaborateur to set
     */
    public void setUserIdCollaborateur(String userIdCollaborateur) {
        this.userIdCollaborateur = userIdCollaborateur;
    }

    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public boolean validate() {
        if (JadeStringUtil.isEmpty(eMailAddress)) {
            _addError(((BSession) getISession()).getLabel("AQUILA_ERR_CO042_DESTINATAIRE_PAS_SELECTIONNE"));
        }

        if (!modePrevisionnel.booleanValue() && JadeStringUtil.isEmpty(libelleJournal)) {
            _addError(((BSession) getISession()).getLabel("AQUILA_ERR_NOM_JOURNAL"));
        }

        if ((idRoles == null) || idRoles.isEmpty()) {
            _addError(((BSession) getISession()).getLabel("AQUILA_ROLE_NON_RENSEIGNE"));
        }

        if ((idTypesSections == null) || idTypesSections.isEmpty()) {
            _addError(((BSession) getISession()).getLabel("AQUILA_TYPE_SECTION_NON_RENSEIGNE"));
        }

        if ((selections == null) || selections.isEmpty()) {
            _addError(((BSession) getISession()).getLabel("AQUILA_TRANSITION_NON_RENSEIGNE"));
        }

        if (!JadeDateUtil.isGlobazDate(dateSurDocument)) {

            _addError(((BSession) getISession()).getLabel("AQUILA_DATE_SUR_DOCUMENT_NON_RESEIGNE"));
        }

        return getMsgType().equals(FWViewBeanInterface.OK);
    }
}
