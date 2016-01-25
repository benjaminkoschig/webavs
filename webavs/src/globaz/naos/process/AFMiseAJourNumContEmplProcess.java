package globaz.naos.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.controleEmployeur.AFControleEmployeur;
import globaz.naos.db.controleEmployeur.AFControleEmployeurManager;

/**
 * Process pour la facturation des Cotisations Personnelles, Paritaires
 * 
 * @author: mmu, sau
 */

public final class AFMiseAJourNumContEmplProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = "";

    /**
     * Constructeur de AFProcessFacturation.
     */
    public AFMiseAJourNumContEmplProcess() {
        super();
    }

    /**
     * Constructeur de AFProcessFacturation.
     * 
     * @param parent
     *            globaz.framework.process.FWProcess
     */
    public AFMiseAJourNumContEmplProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Nettoyage après erreur ou exécution.
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Process de Facturation.
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {

        try {
            mettreAJour();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
            getTransaction().addErrors(e.getMessage());
            JadeLogger.error(this, e);
        }
        return !isOnError();
    }

    public String getAnnee() {
        return annee;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        // Déterminer l'objet du message en fonction du code erreur
        String obj = "";

        // if (getMemoryLog().hasErrors())
        // obj = FWMessage.getMessageFromId("5031")+ " " + getIdPassage();
        // else
        // obj = FWMessage.getMessageFromId("5030")+ " " + getIdDecision();
        // Restituer l'objet
        return obj;
    }

    private String getNouveauNumeroRapport(BTransaction transaction, AFControleEmployeur controle) throws Exception {
        String nouveauNumeroRapport = "";
        nouveauNumeroRapport = getAnnee() + "." + controle.incCounter(transaction, "0", getAnnee()) + ".1";
        return nouveauNumeroRapport;
    }

    private String getNumeroIncrement(BTransaction cursorTransaction, String nouveauNumRapport) {
        String racine = nouveauNumRapport.substring(0, 10);
        int incrementDoublon = JadeStringUtil.toInt(nouveauNumRapport.substring(11, 12));
        return racine + "." + "" + (incrementDoublon + 1);
    }

    /**
     * Renvoie la Job Queue à utiliser pour soumettre le process.
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Traitement de l'affiliation Création de la facturation périodique, personnel, paritaire ou annuel au 30 juin.
     * -------------------------------- ---------------------------------------------------- Ne prendre que les
     * affiliations concernées par le passage et qui n'ont pas été traitées
     * 
     * @param passage
     * @return
     */
    private boolean mettreAJour() {

        getMemoryLog().logMessage("Process Started", FWMessage.INFORMATION, this.getClass().getName());
        AFControleEmployeurManager manager = new AFControleEmployeurManager();
        BTransaction cursorTransaction = null;
        // BStatement statement = null;

        try {
            manager.setSession(getSession());
            manager.setForAnnee(getAnnee());
            manager.setForNumRapportNonVide(true);
            manager.setOrderBy("MALNAF ASC, PSPY DESC");
            // ************************************************************
            // Création du cursorTransaction
            // ************************************************************
            cursorTransaction = (BTransaction) getSession().newTransaction();
            cursorTransaction.openTransaction();
            manager.find(cursorTransaction, BManager.SIZE_NOLIMIT);
            // statement = manager.cursorOpen(cursorTransaction);

            // ************************************************************
            // Parcourir toutes les Cotisations a facturer
            // ************************************************************
            AFControleEmployeur controle = new AFControleEmployeur();
            AFControleEmployeur controlePrecedent = null;

            // while ((controle = (AFControleEmployeur)
            // manager.cursorReadNext(statement)) != null) {
            for (int i = 0; i < manager.size(); i++) {
                controle = (AFControleEmployeur) manager.getEntity(i);
                if (!JadeStringUtil.isBlankOrZero(controle.getDateDebutControle())
                        && !JadeStringUtil.isBlankOrZero(controle.getDateFinControle())
                        && !JadeStringUtil.isBlankOrZero(controle.getGenreControle())) {
                    if ((controlePrecedent != null)
                            && controlePrecedent.getNumAffilie().equals(controle.getNumAffilie())
                            && controlePrecedent.getDateDebutControle().equals(controle.getDateDebutControle())
                            && controlePrecedent.getDateFinControle().equals(controle.getDateFinControle())) {
                        controlePrecedent.setFlagDernierRapport(new Boolean(false));
                        controlePrecedent.update();
                        controle.setNouveauNumRapport(getNumeroIncrement(cursorTransaction,
                                controlePrecedent.getNouveauNumRapport()));
                        controle.setFlagDernierRapport(new Boolean(true));
                        controle.update(cursorTransaction);
                    } else {
                        controle.setNouveauNumRapport(getNouveauNumeroRapport(cursorTransaction, controle));
                        controle.setFlagDernierRapport(new Boolean(true));
                        controle.update(cursorTransaction);
                    }
                    controlePrecedent = controle;
                    if (!cursorTransaction.hasErrors()) {
                        cursorTransaction.commit();
                    } else {
                        getMemoryLog().logMessage(cursorTransaction.getErrors().toString(), FWMessage.FATAL,
                                controle.getNumAffilie());
                        cursorTransaction.rollback();
                        cursorTransaction.clearErrorBuffer();
                        // break;
                    }
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            // Fait remonter l'erreur au processus parent
            getTransaction().addErrors(e.getMessage());
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            JadeLogger.error(this, e);

            try {
                getTransaction().rollback();
            } catch (Exception f) {
                getMemoryLog().logMessage(f.getMessage(), FWMessage.FATAL, this.getClass().getName());
                JadeLogger.error(this, f);
            }
            return false;
        } finally {
            // try {
            // if (statement != null) {
            // try {
            // manager.cursorClose(statement);
            // } finally {
            // statement.closeStatement();
            // }
            // }
            // } catch (Exception g) {
            // getMemoryLog().logMessage(g.getMessage(),FWMessage.FATAL,getClass().getName());
            // JadeLogger.error(this, g);
            // }
            if ((cursorTransaction != null) && (cursorTransaction.isOpened())) {
                try {
                    cursorTransaction.closeTransaction();
                } catch (Exception e) {
                    JadeLogger.error(this, e);
                }
            }
        }
        getMemoryLog().logMessage("Process Terminated", FWMessage.INFORMATION, this.getClass().getName());
        return !isOnError();
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

}
