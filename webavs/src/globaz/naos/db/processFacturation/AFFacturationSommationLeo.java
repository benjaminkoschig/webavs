/*
 * Créé le 27 avr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.db.processFacturation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.log.JadeLogger;
import globaz.leo.application.LEApplication;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.data.LEEnvoiDataSource;
import globaz.leo.db.envoi.LEEtapesSuivantesListViewBean;
import globaz.leo.db.envoi.LEEtapesSuivantesViewBean;
import globaz.leo.process.handler.LEEnvoiHandler;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.osiris.api.APISection;

/**
 * @author ald
 * 
 *         Classe permettant de facturer une amende à tous les envois se trouvant à une étape passée en paramètres A
 *         renseigner pour que le process fonctionne correctement: - le passage de facturation - la rubrique sur
 *         laquelle doit être facturée l'amende - le libellé que doit avoir l'afact - les paramètres que doit avoir
 *         l'envoi pour lequel il faut générer l'amende - la fin de l'id externe facture pour permettre de construire le
 *         numéro de facture
 * 
 */
public class AFFacturationSommationLeo extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String categorieSection = "";
    BProcess context = null;
    String libelleAfact = null;
    /** 
	 * 
	 */
    LEEtapesSuivantesListViewBean listeEtapes = null;
    String montant = "";
    FAPassage passage = null;
    String roleAffilie = "";
    String rubrique = "";
    String typeFacture = "";

    public AFFacturationSommationLeo() {
        super();
    }

    /**
     * @param parent
     */
    public AFFacturationSommationLeo(BProcess parent) {
        super(parent);
    }

    /**
     * @param session
     */
    public AFFacturationSommationLeo(BSession session) {
        super(session);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /* Execute le processus */
    @Override
    protected boolean _executeProcess() throws Exception {
        BTransaction readTransaction = null;
        BStatement statement = null;
        try {
            // Création d'une session LEO
            BSession sessionLeo = (BSession) GlobazSystem.getApplication(LEApplication.DEFAULT_APPLICATION_LEO)
                    .newSession();
            String idLot = "";
            readTransaction = (BTransaction) getSession().newTransaction();
            readTransaction.openTransaction();
            statement = listeEtapes.cursorOpen(readTransaction);
            LEEtapesSuivantesViewBean envoiCrt = null;
            LEEnvoiHandler envoiEtape = new LEEnvoiHandler();
            LEEnvoiDataSource envoi;
            // init un nouveau lot pour grouper le traitement
            idLot = envoiEtape.addNewLot(sessionLeo, getTransaction(), "", ILEConstantes.CS_LOT_TYPE_ETAPESUIVANTE);

            if (getCategorieSection() == null) {
                getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("CATEGORIE_SECTION_NUL"));
            }
            if (getRubrique() == null) {
                getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("RUBRIQUE_NUL"));
            }
            if (getMontant() == null) {
                getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("MONTANT_NUL"));
            }
            if (getTypeFacture() == null) {
                getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("TYPE_FACTURE_NUL"));
            }
            if (getRoleAffilie() == null) {
                getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("ROLE_AFFILIE_NUL"));
            }
            while ((envoiCrt = (LEEtapesSuivantesViewBean) listeEtapes.cursorReadNext(statement)) != null) {
                try {
                    genererAfact(envoiCrt, getTransaction());
                    if (!getTransaction().hasErrors()) {
                        envoi = envoiEtape.chargerDonnees(envoiCrt.getIdJournalisation(), envoiCrt.getEtapeSuivante(),
                                sessionLeo, getTransaction());
                        envoiEtape.genererEnvoi(idLot, JACalendar.today().toString(), envoi, sessionLeo,
                                getTransaction());
                    }
                    // S'il n'y as pas d'exceptions générée on commit l'amende
                    // courante
                    if (getTransaction().hasErrors()) {
                        if (getContext() != null) {
                            getContext().getMemoryLog().logMessage(
                                    "Erreur de génération de l''amende pour la sommation du numéro affilié :"
                                            + envoiCrt.getLibelle(), FWViewBeanInterface.WARNING, "Facturation amende");
                        } else {
                            JadeLogger.info(this,
                                    "Erreur de génération de l''amende pour la sommation du numéro affilié :"
                                            + envoiCrt.getLibelle());
                        }
                        getTransaction().rollback();
                        getTransaction().clearErrorBuffer();
                    } else {
                        getTransaction().commit();
                    }
                } catch (Exception e) {
                    if (getContext() != null) {
                        getContext().getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.WARNING,
                                "Facturation amende");
                    } else {
                        JadeLogger.info(this, e.getMessage());
                    }
                    getTransaction().rollback();
                    getTransaction().clearErrorBuffer();
                }
            }
        } finally {
            try {
                if (statement != null) {
                    try {
                        listeEtapes.cursorClose(statement);
                    } finally {
                        statement.closeStatement();
                    }
                }
            } finally {
                if (readTransaction != null) {
                    readTransaction.closeTransaction();
                }
            }
        }
        return true;
    }

    private void generateAfact(FAEnteteFacture enteteFacture, BTransaction transaction) throws Exception {
        if (enteteFacture != null) {

            FAAfact afact = new FAAfact();
            afact.setIdEnteteFacture(enteteFacture.getIdEntete());
            afact.setISession(getSession());
            afact.setIdPassage(passage.getIdPassage());
            // Choix du module
            afact.setIdModuleFacturation(globaz.musca.external.ServicesFacturation.getIdModFacturationByType(
                    getSession(), getTransaction(), FAModuleFacturation.CS_MODULE_AFACT));
            afact.setIdTypeAfact(FAAfact.CS_AFACT_STANDART);
            afact.setIdRubrique(getRubrique());
            afact.setLibelle(getLibelleAfact());
            afact.setMontantFacture(getMontant());
            afact.add(transaction);
        } else {
            throw new Exception("Impossible de générér une afact pour une entête inexistante");
        }
    }

    private void genererAfact(LEEtapesSuivantesViewBean envoiCrt, BTransaction transaction) throws Exception {
        // Si le type de journal est périodique la facturation de l'amende se
        // fait de façon différente
        if (passage != null) {
            // On facture l'amende uniquement s'il y a déjà une en-tête de
            // facture pour l'affilié
            // ou si l'affilié est radié si il n'existe pas d'entête
            FAEnteteFacture entFacture = getEnteteFacture(envoiCrt.getLibelle(), passage);
            if (entFacture != null) {
                // On a trouvé une entête existente dans le passage de
                // facturation
                // On sette l'afact avec les données pour facturer l'amende

                generateAfact(entFacture, transaction);
            } else {
                if (!FAPassage.CS_TYPE_PERIODIQUE.equals(passage.getIdTypeFacturation())) {
                    generateAfact(getNouvelleEnteteFacture(envoiCrt, transaction), transaction);
                } else {
                    // On a pas trouvé d'entête existante, c'est une facturation
                    // périodique donc on ne la facture pas
                    throw new Exception("Facturation périodique aucune entête trouvée donc pas facturé :"
                            + envoiCrt.getLibelle());
                }
            }
        }
    }

    /**
     * retourne la catégorie de section
     * 
     * @return categorieSection
     */
    public String getCategorieSection() {
        return categorieSection;
    }

    public BProcess getContext() {
        return context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    /*
     * Cette méthode permet de sortir l'en-tête de facture
     */
    private FAEnteteFacture getEnteteFacture(String numAffilie, FAPassage passage) throws Exception {
        FAEnteteFactureManager entFactureManager = new FAEnteteFactureManager();
        entFactureManager.setSession(getSession());
        entFactureManager.setForIdExterneRole(numAffilie);
        entFactureManager.setForIdSousType(APISection.ID_CATEGORIE_SECTION_DECISION_DE_TAXATION_OFFICE);
        entFactureManager.setForIdPassage(passage.getIdPassage());
        entFactureManager.find(getTransaction());
        if (entFactureManager.size() > 0) {
            return (FAEnteteFacture) entFactureManager.getFirstEntity();

        }
        entFactureManager.setForIdSousType(APISection.ID_CATEGORIE_SECTION_DECOMPTE_FINAL);
        if (entFactureManager.size() > 0) {
            return (FAEnteteFacture) entFactureManager.getFirstEntity();
        }
        return null;
    }

    /**
     * Retourne le libellé de l'afact
     * 
     * @return libelleAfact
     */
    public String getLibelleAfact() {
        return libelleAfact;
    }

    /**
     * Renvoie le listeEtapes
     * 
     * @return listeEtapes
     */
    public LEEtapesSuivantesListViewBean getListeEtapes() {
        return listeEtapes;
    }

    /**
     * Retourne le montant
     * 
     * @return montant
     */
    public String getMontant() {
        return montant;
    }

    private FAEnteteFacture getNouvelleEnteteFacture(LEEtapesSuivantesViewBean envoiCrt, BTransaction transaction)
            throws Exception {
        FAEnteteFacture nouveauEntFacture = new FAEnteteFacture();
        nouveauEntFacture.setSession(getSession());
        nouveauEntFacture.setIdPassage(passage.getIdPassage());
        nouveauEntFacture.setIdTiers(envoiCrt.getIdDestinataire());
        nouveauEntFacture.setIdExterneRole(envoiCrt.getLibelle());
        nouveauEntFacture.setIdRole(roleAffilie);
        nouveauEntFacture.setIdTypeFacture(getTypeFacture());
        nouveauEntFacture.setIdExterneFacture(JACalendar.format(JACalendar.todayJJsMMsAAAA(), JACalendar.FORMAT_YYYY)
                + getCategorieSection());
        // DGI init plan
        nouveauEntFacture.initDefaultPlanValue(roleAffilie);
        nouveauEntFacture.add(transaction);
        return nouveauEntFacture;
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

    /**
     * retourne l'id de la rubrique
     * 
     * @return rubrique
     */
    public String getRubrique() {
        return rubrique;
    }

    /**
     * retourne le type de la facture
     * 
     * @return typeFacture
     */
    public String getTypeFacture() {
        return typeFacture;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    /**
     * sette la categorie de section
     * 
     * @param string
     */
    public void setCategorieSection(String string) {
        categorieSection = string;
    }

    public void setContext(BProcess context) {
        this.context = context;
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
     * Sette le listeEtapes
     * 
     * @param bean
     */
    public void setListeEtapes(LEEtapesSuivantesListViewBean bean) {
        listeEtapes = bean;
    }

    /**
     * sette le montant
     * 
     * @param string
     */
    public void setMontant(String string) {
        montant = string;
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

    /**
     * sette la rubrique
     * 
     * @param string
     */
    public void setRubrique(String string) {
        rubrique = string;
    }

    /**
     * sette le type de facture
     * 
     * @param string
     */
    public void setTypeFacture(String string) {
        typeFacture = string;
    }

}
