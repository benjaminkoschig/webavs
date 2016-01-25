package globaz.hercule.process.facturation;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.hercule.application.CEApplication;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.leo.constantes.ILEConstantes;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.lupus.db.journalisation.LUJournalViewBean;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAPassage;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.utils.CAUtil;
import globaz.pavo.application.CIApplication;
import globaz.pyxis.db.tiers.TIRole;
import java.util.ArrayList;

/**
 * Processus de facturation des taxes de sommation pour la non remise des déclarations structurés
 * 
 * @author JPA
 */
public final class CEProcessFacturationTaxeSommation extends BProcess {
    private static final long serialVersionUID = -6135418093936998833L;
    private boolean auMoinsUneErreur = false;
    private BProcess context = null;
    private String idModuleFacturation = "";
    private String libelleAfact = null;
    private ArrayList<String> listeLabels = null;
    private String montant = null;
    private globaz.musca.db.facturation.FAPassage passage = null;
    private String roleAffilie = "";
    private String rubrique = null;
    private BSession sessionOsiris = null;

    /**
     * Commentaire relatif au constructeur DSProcessFacturationDeclarationSalaire.
     */
    public CEProcessFacturationTaxeSommation() {
        super();
    }

    /**
     * Commentaire relatif au constructeur DSProcessFacturationDeclarationSalaire.
     * 
     * @param parent
     *            globaz.framework.process.FWProcess
     */
    public CEProcessFacturationTaxeSommation(BProcess parent) {
        super(parent);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Cette méthode exécute le processus de facturation des déclarations de salaires. Date de création : (14.02.2002
     * 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        try {
            remplirLabels();
        } catch (Exception e1) {
            getMemoryLog().logMessage("Impossible de retrouver les labels des afacts !", FWMessage.ERREUR, "");
            return false;
        }

        LUJournalListViewBean managerSommation = null;
        try {
            managerSommation = returnSommation();
        } catch (Exception e) {
            getMemoryLog()
                    .logMessage("Erreur lors de la récupération des sommations", FWMessage.ERREUR, e.getMessage());
            return false;
        }

        for (int i = 0; i < managerSommation.getSize(); i++) {
            try {
                LUJournalViewBean entity = (LUJournalViewBean) managerSommation.getEntity(i);

                // REcherche de l'affiliation
                AFAffiliation affiliation = getAffiliation(entity.getLibelle(), getAnneePassagePrecedente());

                // On test si il y a une particularité de l'affiliation pour
                // l'année
                if (!AFParticulariteAffiliation.existeParticularite(getTransaction(), affiliation.getAffiliationId(),
                        CodeSystem.PARTIC_AFFILIE_EXEMPT_AMENDE_DEC_STRUCT, "31.12." + getAnneePassagePrecedente())) {
                    if (!isDejaTaxe(entity)) {
                        // Recherche de l'entête de facture
                        FAEnteteFacture entete = rechercheEnteteFactureExistante(entity);
                        if (entete == null) {
                            // Si elle existe pas on la crée
                            entete = nouvelleEntete(entity, getTransaction());
                        }
                        // on génère les afacts
                        generateAfact(entete, affiliation, getTransaction());
                    }
                    if (!getTransaction().hasErrors()) {
                        getTransaction().commit();
                    } else {
                        getTransaction().rollback();
                    }
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "");

                try {
                    getTransaction().rollback();
                } catch (Exception e1) {
                    JadeLogger.error(this, e1);
                }
                auMoinsUneErreur = true;
            }
        }

        if (auMoinsUneErreur) {
            return false;
        }

        return true;
    }

    @Override
    protected void _validate() throws Exception {
        if (getRubrique() == null) {
            getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("RUBRIQUE_NUL"));
        }
        if (getMontant() == null) {
            getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("MONTANT_NUL"));
        }
        if (getPassage() == null) {
            getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("PASSAGE_NUL"));
        }
        super._validate();
    }

    private void generateAfact(FAEnteteFacture enteteFacture, AFAffiliation affiliation, BTransaction transaction)
            throws Exception {

        if (enteteFacture == null) {
            throw new Exception(CEProcessFacturationTaxeSommation.class.getName()
                    + ".generateAfact() \nImpossible de générér une afact pour une entête inexistante");
        }

        if (affiliation == null) {
            throw new Exception(CEProcessFacturationTaxeSommation.class.getName()
                    + ".generateAfact() \nAffiliation is null or empty");
        }

        FAAfact afact = new FAAfact();
        afact.setIdEnteteFacture(enteteFacture.getIdEntete());
        afact.setISession(getSession());
        afact.setIdPassage(passage.getIdPassage());
        afact.setNumCaisse(AFAffiliationUtil.getIdCaissePrincipale(affiliation, true, enteteFacture
                .getIdExterneFacture().substring(0, 4)));
        // Choix du module
        afact.setIdModuleFacturation(getIdModuleFacturation());
        afact.setIdTypeAfact(FAAfact.CS_AFACT_STANDART);
        afact.setIdRubrique(getRubrique());
        afact.setLibelle(getLibelle(affiliation.getTiers().getLangueIso()));
        afact.setMontantFacture(getMontant());
        afact.add(transaction);

    }

    private AFAffiliation getAffiliation(String numeroAffilie, String annee) throws Exception {
        CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                CIApplication.DEFAULT_APPLICATION_PAVO);
        AFAffiliation affiliation = application.getAffilieByNo((BSession) getISession(), numeroAffilie, true, false,
                "", "", annee, "", "");
        if (affiliation == null) {
            throw new Exception(CEProcessFacturationTaxeSommation.class.getName() + "\n"
                    + ((BSession) getISession()).getLabel("AUCUNE_AFFILIATION_EXISTE") + " (" + numeroAffilie + " / "
                    + annee + ")");
        }

        return affiliation;
    }

    private String getAnneePassage() {
        return getPassage().getDatePeriode().substring(3, 7);
    }

    private String getAnneePassagePrecedente() {
        return String.valueOf(Integer.valueOf(getPassage().getDatePeriode().substring(3, 7)) - 1);
    }

    public BProcess getContext() {
        return context;
    }

    /**
     * Cette méthode détermine l'objet du message en fonction du code d'erreur
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        // Déterminer l'objet du message en fonction du code erreur
        String obj;
        obj = "";
        if (getMemoryLog().hasErrors()) {
            obj = getSession().getLabel("5031");
        } else {
            obj = getSession().getLabel("5030");
        } // Restituer l'objet
        return obj;
    }

    private String getIdExterneFacture() {
        return JACalendar.format(JACalendar.todayJJsMMsAAAA(), JACalendar.FORMAT_YYYY) + getMoisPassage() + "000";
    }

    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    private String getLibelle(String langueIso) throws Exception {
        return getSession().getApplication().getLabel("LIBELLE_AMENDES_DS_STRUCT", langueIso);
    }

    /**
     * Retourne le libellé de l'afact
     * 
     * @return libelleAfact
     */
    public String getLibelleAfact() {
        return libelleAfact;
    }

    private String getMoisPassage() {
        return getPassage().getDatePeriode().substring(0, 2);
    }

    // Retourne le montant de l'amende (stocké dans la table fwparp)
    private String getMontant() {
        if (montant == null) {
            try {
                montant = String.valueOf((int) JadeStringUtil.parseDouble(
                        FWFindParameter.findParameter(getTransaction(), "3000001", "montant", "0", "0", 2), 0.0));
            } catch (Exception e) {
                getMemoryLog().logMessage("", FWMessage.ERREUR, e.getMessage());
                return null;
            }
        }
        return montant;
    }

    /**
     * Renvoie le passage
     * 
     * @return
     */
    public FAPassage getPassage() {
        return passage;
    }

    /**
     * Retourne le role de l'affilié
     * 
     * @return
     */
    public String getRoleAffilie() {
        return roleAffilie;
    }

    // Retourne l'id de la rubrique à utiliser (stocké dans la table fwparp )
    private String getRubrique() {
        if (rubrique == null) {
            try {
                rubrique = String.valueOf((int) JadeStringUtil.parseDouble(
                        FWFindParameter.findParameter(getTransaction(), "3000002", "rubrique", "0", "0", 2), 0.0));
            } catch (Exception e) {
                getMemoryLog().logMessage("", FWMessage.ERREUR, e.getMessage());
                return null;
            }
        }
        return rubrique;
    }

    private BSession getSessionOsiris() {
        if (sessionOsiris == null) {
            try {
                sessionOsiris = new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS);
                getSession().connectSession(sessionOsiris);
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }

        }
        return sessionOsiris;
    }

    private boolean isDejaTaxe(LUJournalViewBean entity) throws Exception {
        CACompteAnnexe cptAnnexe = new CACompteAnnexe();
        cptAnnexe.setSession(getSession());
        cptAnnexe.setAlternateKey(1);
        cptAnnexe.setIdExterneRole(entity.getLibelle());
        cptAnnexe
                .setIdRole(CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(getSession().getApplication()));
        cptAnnexe.retrieve();

        if ((cptAnnexe != null) && !cptAnnexe.isNew()) {
            CAOperationManager op = new CAOperationManager();
            op.setSession(getSession());
            op.setForIdCompteAnnexe(cptAnnexe.getIdCompteAnnexe());
            op.setForIdCompte(getRubrique());
            op.setForEtat(APIOperation.ETAT_COMPTABILISE);
            op.find(getTransaction(), BManager.SIZE_NOLIMIT);
            for (int i = 0; i < op.size(); i++) {
                CAOperation opEntity = (CAOperation) op.get(i);
                CASection sec = opEntity.getSection();
                String anneeFac = sec.getDateDebutPeriode();
                int anneeFacture = 0;
                int anneeEnCours = 0;
                JADate date = new JADate(anneeFac);
                anneeFacture = date.getYear();
                anneeEnCours = Integer.valueOf(getAnneePassage());
                if ((anneeEnCours == anneeFacture) && listeLabels.contains(opEntity.getLibelle())) {
                    return true;
                }
            }

        }
        return false;

    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    private FAEnteteFacture nouvelleEntete(LUJournalViewBean entity, BTransaction transaction) throws Exception {
        try {
            FAEnteteFacture nouveauEntFacture = new FAEnteteFacture();
            nouveauEntFacture.setSession(getSession());
            nouveauEntFacture.setIdPassage(getPassage().getIdPassage());
            nouveauEntFacture.setIdTiers(entity.getIdDestinataire());
            nouveauEntFacture.setIdExterneRole(entity.getLibelle());
            // On charge le sous type d'après le mois
            String mois = getMoisPassage();
            String idSousType = "";
            idSousType = returnIdSousType(mois);
            nouveauEntFacture.setIdSousType(idSousType);
            String role = CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(getSession().getApplication());
            nouveauEntFacture.setIdRole(role);
            nouveauEntFacture.setIdExterneFacture(getIdExterneFacture());
            String numFacture = "";
            numFacture = CAUtil.creerNumeroSectionUnique(getSessionOsiris(), transaction, role, entity.getLibelle(),
                    nouveauEntFacture.getIdTypeFacture(), getAnneePassage(), nouveauEntFacture.getIdSousType());

            nouveauEntFacture.setIdExterneFacture(numFacture);

            nouveauEntFacture.initDefaultPlanValue(role);
            nouveauEntFacture.add(transaction);
            return nouveauEntFacture;
        } catch (Exception e) {
            transaction.addErrors("nouvelle entête : " + e.getMessage());
            return null;
        }
    }

    private FAEnteteFacture rechercheEnteteFactureExistante(LUJournalViewBean entity) throws Exception {
        FAEnteteFactureManager entFactureManager = new FAEnteteFactureManager();
        entFactureManager.setISession(getSession());
        entFactureManager.setForIdTiers(entity.getIdDestinataire());
        entFactureManager.setForIdPassage(passage.getIdPassage());
        entFactureManager.setForIdExterneRole(entity.getLibelle());
        entFactureManager.setForIdExterneFacture(getIdExterneFacture());
        entFactureManager.setForIdRole(CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(
                getSession().getApplication()));
        entFactureManager.find(getTransaction());
        if (entFactureManager.size() > 0) {
            return (FAEnteteFacture) entFactureManager.getFirstEntity();
        } else {
            return null;
        }
    }

    private void remplirLabels() throws Exception {
        listeLabels = new ArrayList<String>();
        listeLabels.add(getSession().getApplication().getLabel("LIBELLE_AMENDES_DS_STRUCT", "fr"));
        listeLabels.add(getSession().getApplication().getLabel("LIBELLE_AMENDES_DS_STRUCT", "de"));
        listeLabels.add(getSession().getApplication().getLabel("LIBELLE_AMENDES_DS_STRUCT", "it"));
    }

    private String returnIdSousType(String mois) {
        String idSousType = "";
        if (mois.equals("01")) {
            idSousType = APISection.ID_CATEGORIE_SECTION_DECOMPTE_JANVIER;
        } else if (mois.equals("02")) {
            idSousType = APISection.ID_CATEGORIE_SECTION_DECOMPTE_FEVRIER;
        } else if (mois.equals("03")) {
            idSousType = APISection.ID_CATEGORIE_SECTION_DECOMPTE_MARS;
        } else if (mois.equals("04")) {
            idSousType = APISection.ID_CATEGORIE_SECTION_DECOMPTE_AVRIL;
        } else if (mois.equals("05")) {
            idSousType = APISection.ID_CATEGORIE_SECTION_DECOMPTE_MAI;
        } else if (mois.equals("06")) {
            idSousType = APISection.ID_CATEGORIE_SECTION_DECOMPTE_JUIN;
        } else if (mois.equals("07")) {
            idSousType = APISection.ID_CATEGORIE_SECTION_DECOMPTE_JUILLET;
        } else if (mois.equals("08")) {
            idSousType = APISection.ID_CATEGORIE_SECTION_DECOMPTE_AOUT;
        } else if (mois.equals("09")) {
            idSousType = APISection.ID_CATEGORIE_SECTION_DECOMPTE_SEPTEMBRE;
        } else if (mois.equals("10")) {
            idSousType = APISection.ID_CATEGORIE_SECTION_DECOMPTE_OCTOBRE;
        } else if (mois.equals("11")) {
            idSousType = APISection.ID_CATEGORIE_SECTION_DECOMPTE_NOVEMBRE;
        } else if (mois.equals("12")) {
            idSousType = APISection.ID_CATEGORIE_SECTION_DECOMPTE_DECEMBRE;
        }
        return idSousType;
    }

    private LUJournalListViewBean returnSommation() throws Exception {
        LUJournalListViewBean viewBean = new LUJournalListViewBean();
        LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                CEApplication.DEFAULT_APPLICATION_HERCULE);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE,
                String.valueOf(Integer.parseInt(getAnneePassage()) - 1));

        // provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, this.getAnneePassage());
        viewBean.setISession(getISession());
        viewBean.setProvenance(provenanceCriteres);
        viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_DEF_FORMULE_GROUPE);
        viewBean.setForValeurCodeSysteme(ILEConstantes.CS_DS_ST_SOMMATION);
        viewBean.find(getTransaction(), BManager.SIZE_NOLIMIT);
        return viewBean;
    }

    public void setContext(BProcess context) {
        this.context = context;
    }

    public void setIdModuleFacturation(String idModuleFacturation) {
        this.idModuleFacturation = idModuleFacturation;
    }

    /**
     * Sette le libelleAfact
     * 
     * @param string
     */
    public void setLibelleAfact(String string) {
        libelleAfact = string;
    }

    /**
     * Sette le passage
     * 
     * @param passage
     */
    public void setPassage(FAPassage passage) {
        this.passage = passage;
    }

    /**
     * Sette le role de l'affilié
     * 
     * @param string
     */
    public void setRoleAffilie(String string) {
        roleAffilie = string;
    }

}