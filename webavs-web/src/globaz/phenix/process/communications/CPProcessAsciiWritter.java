/*
 * Créé le 15 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.process.communications;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichageManager;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.process.communications.envoiWritterImpl.CPGEWritter;
import globaz.phenix.process.communications.envoiWritterImpl.CPJuWritter;
import globaz.phenix.process.communications.envoiWritterImpl.CPNeWritter;
import globaz.phenix.process.communications.envoiWritterImpl.CPVDWritter;
import globaz.phenix.process.communications.envoiWritterImpl.CPVsWritter;
import globaz.phenix.translation.CodeSystem;
import globaz.phenix.util.Constante;
import globaz.pyxis.constantes.IConstantes;

/**
 * <H1>Description</H1>
 * 
 * @author MAR
 *         <p>
 *         Cette classe écrit un fichier ASCII avec des informations sur des contribuables pour différents cantons
 *         </p>
 */

public class CPProcessAsciiWritter extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int CS_ALLEMAND = 503002;
    // private long progressCounter = -1;
    // initialisation pour CS langue tiers (TITIERS)
    public static final int CS_FRANCAIS = 503001;
    public static final int CS_ITALIEN = 503004;
    public static final String NUMERO_CAISSE = "NUMEROCAISSE";
    private AFAffiliationManager affMng = null;
    private String anneeDecision = "";
    private String dateEnvoi = "";
    private Boolean dateEnvoiVide = new Boolean(false);
    private Boolean demandeAnnulee = new Boolean(false);
    private String forCanton = "";
    private String forGenreAffilie = "";
    private String idCommunication = "";
    private String idIfd = "";
    // private BStatement statement = null;
    private CPCommunicationFiscaleAffichageManager manager = null;
    private int nbCommunication = 0;
    private Boolean withAnneeEnCours = new Boolean(false);

    /**
	 * 
	 */
    public CPProcessAsciiWritter() {
        super();
    }

    /**
     * @param parent
     */
    public CPProcessAsciiWritter(BProcess parent) {
        super(parent);
    }

    /**
     * @param session
     */
    public CPProcessAsciiWritter(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
        if ((getTransaction() != null) && (getTransaction().isOpened())) {
            try {
                getTransaction().closeTransaction();
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        setState(Constante.FWPROCESS_MGS_220);
    }

    @Override
    protected boolean _executeProcess() {
        try {
            String filename = null;
            if (CPDecision.CS_FICHIER_CENTRAL.equalsIgnoreCase(getForGenreAffilie())) {
                affMng = new AFAffiliationManager();
                affMng.setSession(getSession());
                affMng.setForTypeAffiliation(new String[] { globaz.naos.translation.CodeSystem.TYPE_AFFILI_FICHIER_CENT });
                affMng.setForAnnee(getAnneeDecision());
                affMng.setOrderBy(AFAffiliationManager.ORDER_AFFILIENUMERO);
                nbCommunication = affMng.getCount(getTransaction());
            } else {
                managerCommunication();
            }
            if (nbCommunication > 0) {
                if (IConstantes.CS_LOCALITE_CANTON_NEUCHATEL.equalsIgnoreCase(getForCanton())) {
                    CPNeWritter fichier = new CPNeWritter(getSession());
                    fichier.setProcessAppelant(this);
                    filename = fichier.create(manager, getTransaction());
                } else if (IConstantes.CS_LOCALITE_CANTON_JURA.equalsIgnoreCase(getForCanton())) {
                    CPJuWritter fichier = new CPJuWritter(getSession());
                    fichier.setProcessAppelant(this);
                    filename = fichier.create(manager, getTransaction());
                } else if (IConstantes.CS_LOCALITE_CANTON_GENEVE.equalsIgnoreCase(getForCanton())) {
                    CPGEWritter fichier = new CPGEWritter(getSession());
                    fichier.setProcessAppelant(this);
                    filename = fichier.create(manager, getTransaction());
                } else if (IConstantes.CS_LOCALITE_CANTON_VAUD.equalsIgnoreCase(getForCanton())) {
                    CPVDWritter fichier = new CPVDWritter(getSession());
                    fichier.setProcessAppelant(this);
                    filename = fichier.create(manager, getTransaction());
                } else if (IConstantes.CS_LOCALITE_CANTON_VALAIS.equalsIgnoreCase(getForCanton())) {
                    CPVsWritter fichier = new CPVsWritter(getSession());
                    fichier.setProcessAppelant(this);
                    if (!CPDecision.CS_FICHIER_CENTRAL.equalsIgnoreCase(getForGenreAffilie())) {
                        filename = fichier.create(manager, getTransaction());
                    } else {
                        filename = fichier.createFichierCentral(affMng, getAnneeDecision(), getTransaction());
                    }
                }
                // Pour VD le nombre de communication peut varier car on envoie
                // pas les n° de contribuable à vide
                // Le renseignement est à la fin dans le fichier.
                if ((manager != null) && !IConstantes.CS_LOCALITE_CANTON_VAUD.equalsIgnoreCase(manager.getForCanton())) {
                    getMemoryLog().logMessage(
                            "\n\nLe fichier attaché contient " + nbCommunication + " communications fiscales.\n\n",
                            globaz.framework.util.FWMessage.INFORMATION, this.getClass().getName());
                }
                if (filename != null) {
                    JadePublishDocument publishDoc = new JadePublishDocument(filename, createDocumentInfo());
                    this.registerAttachedDocument(publishDoc);
                }
            } else {
                getMemoryLog().logMessage("\nIl n'y aucune communication fiscale à envoyer.",
                        globaz.framework.util.FWMessage.INFORMATION, this.getClass().getName());
            }
            return true;
        } catch (Exception e) {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0143"));
            return false;
        }
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate() throws Exception {
        if (IConstantes.CS_LOCALITE_CANTON_GENEVE.equalsIgnoreCase(getForCanton())) {
            if (JadeStringUtil.isEmpty(getAnneeDecision())) {
                // _addError(getTransaction(),
                // "L'année est obligatoire pour le canton " +
                // CodeSystem.getLibelle(getSession(), getForCanton()) + ". ");
            }
        }
        // denamnde annulé, autorisé seulement pour GE et VD
        if (Boolean.TRUE.equals(getDemandeAnnulee())) {
            if (!IConstantes.CS_LOCALITE_CANTON_GENEVE.equalsIgnoreCase(getForCanton())
                    && !IConstantes.CS_LOCALITE_CANTON_VAUD.equalsIgnoreCase(getForCanton())) {
                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0144"));
            }
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    /**
     * @return
     */
    public String getAnneeDecision() {
        return anneeDecision;
    }

    /**
     * @return
     */
    public String getDateEnvoi() {
        return dateEnvoi;
    }

    /**
     * @return
     */
    public Boolean getDateEnvoiVide() {
        return dateEnvoiVide;
    }

    /**
     * @return
     */
    public Boolean getDemandeAnnulee() {
        return demandeAnnulee;
    }

    @Override
    protected String getEMailObject() {
        String desc = "";
        try {
            if (!JadeStringUtil.isEmpty(getForCanton())) {
                desc += " - " + CodeSystem.getCodeUtilisateur(getSession(), getForCanton());
            }
        } catch (Exception e) {
            desc += "";
        }
        try {
            if (!JadeStringUtil.isEmpty(getAnneeDecision())) {
                desc += " - " + getAnneeDecision();
            }
        } catch (Exception e) {
            desc += "";
        }
        try {
            if (!JadeStringUtil.isEmpty(getForGenreAffilie())) {
                desc += " - " + CodeSystem.getCodeUtilisateur(getSession(), getForGenreAffilie());
            }
        } catch (Exception e) {
            desc += "";
        }
        if (isAborted() && (getMemoryLog().hasErrors()) && isOnError()) {
            return "La création du fichier des communications fiscales a échouée";
        } else {
            return "Fichier des communications fiscales" + desc;
        }
    }

    /**
     * @return
     */
    public String getForCanton() {
        return forCanton;
    }

    /**
     * @return
     */
    public String getForGenreAffilie() {
        return forGenreAffilie;
    }

    /**
     * @return
     */
    public String getIdCommunication() {
        return idCommunication;
    }

    /**
     * @return
     */
    public String getIdIfd() {
        return idIfd;
    }

    public Boolean getWithAnneeEnCours() {
        return withAnneeEnCours;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    protected void managerCommunication() throws Exception {
        manager = new CPCommunicationFiscaleAffichageManager();
        manager.setSession(getSession());
        manager.setDemandeAnnulee(getDemandeAnnulee());
        if (getDemandeAnnulee().equals(Boolean.FALSE)) {
            manager.setExceptRetour(Boolean.TRUE);
            manager.setExceptComptabilise(Boolean.TRUE);
        }
        if (JadeStringUtil.isEmpty(getDateEnvoi())) {
            manager.setDateEnvoiVide(getDateEnvoiVide());
        } else {
            manager.setDateEnvoiVide(Boolean.FALSE);
            if (getDemandeAnnulee().equals(Boolean.FALSE)) {
                manager.setForDateEnvoi(getDateEnvoi());
            } else {
                manager.setForDateEnvoiAnnulation(getDateEnvoi());
            }
        }
        // Si Indépendant de sélectionné => IND, TSE, AGR, REN donc différent de
        // NON ACTIF...
        if (!JadeStringUtil.isEmpty(getForGenreAffilie())) {
            if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(getForGenreAffilie())) {
                manager.setNotInGenreAffilie(CPDecision.CS_NON_ACTIF + ", " + CPDecision.CS_ETUDIANT);
            } else {
                manager.setInGenreAffilie(CPDecision.CS_NON_ACTIF + ", " + CPDecision.CS_ETUDIANT);
            }
        }
        manager.setForCanton(getForCanton()); // Zone d'écran
        manager.setForAnneeDecision(getAnneeDecision());
        manager.setForIdCommunication(getIdCommunication());
        if (IConstantes.CS_LOCALITE_CANTON_JURA.equalsIgnoreCase(manager.getForCanton())) {
            manager.setExceptNumContibuableVide(Boolean.TRUE);
        }
        if (IConstantes.CS_LOCALITE_CANTON_GENEVE.equalsIgnoreCase(manager.getForCanton())) {
            manager.orderByNom();
            manager.orderByPrenom();
        } else {
            manager.orderByNumAffilie();
        }
        if (IConstantes.CS_LOCALITE_CANTON_VAUD.equalsIgnoreCase(manager.getForCanton())) {
            manager.setExceptNumContibuableVide(Boolean.TRUE);
        }
        // Avec année encours
        if (JadeStringUtil.isEmpty(getAnneeDecision())) {
            manager.setWithAnneeEnCours(getWithAnneeEnCours());
        } else {
            manager.setWithAnneeEnCours(Boolean.FALSE);
        }
        nbCommunication = manager.getCount(getTransaction());
    }

    /**
     * @param string
     */
    public void setAnneeDecision(String string) {
        anneeDecision = string;
    }

    /**
     * @param string
     */
    public void setDateEnvoi(String string) {
        dateEnvoi = string;
    }

    /**
     * @param boolean1
     */
    public void setDateEnvoiVide(Boolean boolean1) {
        dateEnvoiVide = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setDemandeAnnulee(Boolean boolean1) {
        demandeAnnulee = boolean1;
    }

    /**
     * @param string
     */
    public void setForCanton(String string) {
        forCanton = string;
    }

    /**
     * @param string
     */
    public void setForGenreAffilie(String string) {
        forGenreAffilie = string;
    }

    /**
     * @param string
     */
    public void setIdCommunication(String string) {
        idCommunication = string;
    }

    /**
     * @param string
     */
    public void setIdIfd(String string) {
        idIfd = string;
    }

    public void setWithAnneeEnCours(Boolean withAnneeEnCours) {
        this.withAnneeEnCours = withAnneeEnCours;
    }

}
