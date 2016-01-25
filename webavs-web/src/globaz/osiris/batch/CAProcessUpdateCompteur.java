package globaz.osiris.batch;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteur;
import globaz.osiris.db.compteur.CAUpdateCompteur;
import globaz.osiris.db.compteur.CAUpdateCompteurManager;

public class CAProcessUpdateCompteur extends BProcess {

    private static final long serialVersionUID = -1465380980513198813L;
    private String annee = new String();
    private String idCompteAnnexe = new String();
    private String idRubrique = new String();
    // Private members
    private String simulation = "true";

    private String verbose = "false";

    /**
     * Construit un nouveau process
     */
    public CAProcessUpdateCompteur() {
        super();
    }

    /**
     * Construit un nouveau process fils
     * 
     * @param parent
     */
    public CAProcessUpdateCompteur(BProcess parent) {
        super(parent);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        // Transaction
        BTransaction cursorTransaction = null;
        BStatement statement = null;
        CAUpdateCompteurManager mgr = null;
        CAUpdateCompteur entity;
        // Sous contrôle d'exception
        try {
            // Mode simulation
            if ("true".equalsIgnoreCase(getSimulation())) {
                getMemoryLog().logMessage(getSession().getLabel("MODESIMULATION"), FWMessage.INFORMATION,
                        this.getClass().getName());
            }
            // Transaction pour open
            cursorTransaction = new BTransaction(getSession());
            cursorTransaction.openTransaction();
            // Manager principal
            mgr = new CAUpdateCompteurManager();
            mgr.setSession(getSession());
            mgr.setForIdRubrique(getIdRubrique());
            if (!JadeStringUtil.isIntegerEmpty(getAnnee())) {
                mgr.setForAnnee(getAnnee());
            }
            if (!JadeStringUtil.isIntegerEmpty(getIdCompteAnnexe())) {
                mgr.setForIdCompteAnnexe(getIdCompteAnnexe());
            }
            // Count
            setProgressScaleValue(mgr.getCount(getTransaction()));
            // Ouverture du curseur
            statement = mgr.cursorOpen(cursorTransaction);
            // boucle principale
            while ((entity = (CAUpdateCompteur) mgr.cursorReadNext(statement)) != null) {
                // Incrémenter
                incProgressCounter();
                // Vérifier s'il existe un compteur
                CACompteur cptr = new CACompteur();
                cptr.setSession(getSession());
                cptr.setIdCompteAnnexe(entity.getIdCompteAnnexe());
                cptr.setIdRubrique(getIdRubrique());
                cptr.setAnnee(entity.getAnnee());
                cptr.setAlternateKey(CACompteur.AK_CPTA_RUB_ANNEE);
                cptr.retrieve(getTransaction());
                // Pas trouvé
                if (cptr.isNew()) {
                    cptr.setCumulCotisation(entity.getMontant());
                    cptr.setCumulMasse(entity.getMasse());
                    if (!"true".equalsIgnoreCase(getSimulation())) {
                        cptr.add(getTransaction());
                        // Commit
                        if (!getTransaction().hasErrors()) {
                            getTransaction().commit();
                        } else {
                            getTransaction().rollback();
                            getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());
                            return false;
                        }
                    }
                    if ("true".equalsIgnoreCase(getVerbose())) {
                        getMemoryLog().logMessage("ADD: " + entity.toMyString(), FWMessage.INFORMATION,
                                this.getClass().getName());
                    }
                    // Trouvé
                } else {
                    FWCurrency oldMontant = new FWCurrency(cptr.getCumulCotisation());
                    FWCurrency oldMasse = new FWCurrency(cptr.getCumulMasse());
                    FWCurrency newMontant = new FWCurrency(entity.getMontant());
                    FWCurrency newMasse = new FWCurrency(entity.getMasse());
                    // Si différence
                    if (!oldMasse.equals(newMasse) || !oldMontant.equals(newMontant)) {
                        cptr.setCumulCotisation(entity.getMontant());
                        cptr.setCumulMasse(entity.getMasse());
                        if (!"true".equalsIgnoreCase(getSimulation())) {
                            cptr.update(getTransaction());
                            // Commit
                            if (!getTransaction().hasErrors()) {
                                getTransaction().commit();
                            } else {
                                getTransaction().rollback();
                                getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());
                                return false;
                            }
                        }
                        if ("true".equalsIgnoreCase(getVerbose())) {
                            getMemoryLog().logMessage(
                                    "UPD : New " + entity.toMyString() + " Old " + oldMontant + "/" + oldMasse,
                                    FWMessage.INFORMATION, this.getClass().getName());
                        }
                    }

                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        } finally {
            try {
                // Clôture du curseur
                if (statement != null) {
                    try {
                        mgr.cursorClose(statement);
                    } finally {
                        statement.closeStatement();
                    }
                }
            } catch (Exception ex2) {
                getMemoryLog().logMessage(ex2.getMessage(), FWMessage.FATAL, this.getClass().getName());
            } finally {
                if (cursorTransaction != null) {
                    try {
                        if (isOnError()) {
                            cursorTransaction.rollback();
                        } else {
                            cursorTransaction.commit();
                        }
                    } catch (Exception e) {
                        getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
                    } finally {
                        try {
                            cursorTransaction.closeTransaction();
                            cursorTransaction = null;
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        // Fin du process
        return !isOnError();
    }

    @Override
    protected void _validate() throws Exception {
        // Validation générale
        super._validate();
        // Contrôle du numéro de rubrique
        if (JadeStringUtil.isIntegerEmpty(getIdRubrique())) {
            throw new Exception(getSession().getLabel("5114"));
        }
    }

    public String getAnnee() {
        return annee;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("COMPTEUR_EMAIL_FAILED");
        } else {
            return getSession().getLabel("COMPTEUR_EMAIL_SUCCESS");
        }
    }

    /**
     * @return
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return
     */
    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * @return String
     */
    public String getSimulation() {
        return simulation;
    }

    public String getVerbose() {
        return verbose;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        if ("true".equalsIgnoreCase(getSimulation())) {
            return GlobazJobQueue.READ_LONG;
        } else {
            return GlobazJobQueue.UPDATE_LONG;
        }
    }

    /**
     * @param string
     */
    public void setAnnee(String string) {
        annee = string;
    }

    /**
     * @param string
     */
    public void setIdCompteAnnexe(String string) {
        idCompteAnnexe = string;
    }

    /**
     * @param string
     */
    public void setIdRubrique(String string) {
        idRubrique = string;
    }

    /**
     * @param String
     */
    public void setSimulation(String string) {
        simulation = string;
    }

    /**
     * @param String
     *            string
     */
    public void setVerbose(String string) {
        verbose = string;
    }

}
