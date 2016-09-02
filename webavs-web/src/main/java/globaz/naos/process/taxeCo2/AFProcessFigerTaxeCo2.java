package globaz.naos.process.taxeCo2;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.taxeCo2.AFFigerTaxeCo2;
import globaz.naos.db.taxeCo2.AFFigerTaxeCo2Manager;
import globaz.naos.db.taxeCo2.AFTaxeCo2;
import globaz.naos.db.taxeCo2.AFTaxeCo2Manager;
import globaz.naos.translation.CodeSystem;
import java.util.ArrayList;
import java.util.List;

public final class AFProcessFigerTaxeCo2 extends BProcess {

    private static final long serialVersionUID = 1L;
    private String anneeMasse = null;
    private String anneeRedistribution = null;
    private String idAffiliation = null;
    private Boolean reinitialiser = new Boolean(false);

    public AFProcessFigerTaxeCo2() {
        super();
    }

    public AFProcessFigerTaxeCo2(BProcess parent) {
        super(parent);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {

        if (getAnneeMasse().equals(JACalendar.todayJJsMMsAAAA().substring(6))
                || (JadeStringUtil.toInt(getAnneeMasse()) < JadeStringUtil.toInt(JACalendar.todayJJsMMsAAAA()
                        .substring(6)) - 2)) {
            this._addError(getTransaction(), getSession().getLabel("PROCESS_FIGER_ERREUR_DATE"));
            return false;
        }

        try {
            AFTaxeCo2Manager manager = new AFTaxeCo2Manager();
            manager.setSession(getSession());
            manager.setForAnneeMasse(getAnneeMasse());
            manager.setOrder("MAIAFF DESC");
            manager.find(BManager.SIZE_NOLIMIT);

            if (manager.size() > 0) {
                if (getReinitialiser().booleanValue()) {
                    deleteTaxeCO2(manager);
                } else {
                    AFTaxeCo2 taxe = new AFTaxeCo2();
                    taxe = (AFTaxeCo2) manager.getFirstEntity();
                    if (taxe.getAnneeMasse().equals(getAnneeMasse())) {
                        setIdAffiliation(taxe.getAffiliationId());
                    }
                }
            }

            figer(getAnneeMasse());
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
            getTransaction().addErrors(e.getMessage());
            JadeLogger.error(this, e);
        }

        return !isOnError();
    }

    public void deleteTaxeCO2(AFTaxeCo2Manager manager) {

        BStatement statement = null;
        try {
            statement = manager.cursorOpen(getTransaction());
            AFTaxeCo2 taxe = null;

            // parcourir les afacts liés aux modules
            while ((taxe = (AFTaxeCo2) manager.cursorReadNext(statement)) != null) {
                // supprimer l'afact (non manuel) lié aux module si la méthode
                // avantRegeneration a échoué

                if (!taxe.isNew()) {
                    taxe.delete(getTransaction());
                    if (!taxe.hasErrors()) {
                        try {
                            getTransaction().commit();
                        } catch (Exception e) {
                            getMemoryLog().logMessage(
                                    "Impossible de supprimer la taxe pour l'affilié : " + taxe.getNumAffilie()
                                            + e.getMessage(), FWMessage.AVERTISSEMENT, this.getClass().getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        } finally {
            if (getTransaction().hasErrors()) {
                // rollback en cas d'erreur
                try {
                    getTransaction().rollback();
                } catch (Exception e) {
                    JadeLogger.error(this, e);
                }
            }
            try {
                // dans tous les cas fermer le cursor
                manager.cursorClose(statement);
                statement = null;
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
    }

    /**
     * Traitement de l'affiliation Création de la facturation périodique, personnel, paritaire ou annuel au 30 juin.
     * -------------------------------- ---------------------------------------------------- Ne prendre que les
     * affiliations concernées par le passage et qui n'ont pas été traitées
     * 
     * @param passage
     * @return
     */
    private boolean figer(String annee) {

        getMemoryLog().logMessage("Process Started", FWMessage.INFORMATION, this.getClass().getName());
        AFFigerTaxeCo2Manager manager = null;
        BTransaction cursorTransaction = null;
        BStatement statement = null;

        // Permet de creer que des lignes de taxes CO2 par année, par rubrique et par affilié
        List<String> keys = new ArrayList<String>();

        try {
            manager = new AFFigerTaxeCo2Manager();
            manager.setSession(getSession());

            // ************************************************************
            // Initialisation des paramètres pour la recherche des Cotisations
            // ************************************************************

            manager.setForAnneeMasse(getAnneeMasse());
            // manager.setForAffiliationId("232190");
            if (!JadeStringUtil.isEmpty(getIdAffiliation())) {
                manager.setFromAffiliationId(getIdAffiliation());
            }

            // ************************************************************
            // Création du cursorTransaction
            // ************************************************************
            cursorTransaction = (BTransaction) getSession().newTransaction();
            cursorTransaction.openTransaction();
            statement = manager.cursorOpen(cursorTransaction);

            // ************************************************************
            // Parcourir toutes les Cotisations a facturer
            // ************************************************************
            AFFigerTaxeCo2 donneesFiger = null;

            int progressCounter = manager.getCount();
            // setProgressScaleValue(progressCounter);
            int cpt = 0;

            // ****************************************************************
            // POUR CHAQUE COTISATION
            // ****************************************************************
            while ((donneesFiger = (AFFigerTaxeCo2) manager.cursorReadNext(statement)) != null) {
                cpt++;
                setProgressDescription(donneesFiger.getNumAffilie() + " <br>" + cpt + "/" + progressCounter + "<br>");
                if (isAborted()) {
                    setProgressDescription("Traitement interrompu<br> sur l'affilié : " + donneesFiger.getNumAffilie()
                            + " <br>" + cpt + "/" + progressCounter + "<br>");
                    if ((getParent() != null) && getParent().isAborted()) {
                        getParent().setProcessDescription(
                                "Traitement interrompu<br> sur l'affilié : " + donneesFiger.getNumAffilie() + " <br>"
                                        + cpt + "/" + progressCounter + "<br>");
                    }
                    break;
                } else {

                    String key = donneesFiger.getNumAffilie() + ";" + donneesFiger.getAnnee() + ";"
                            + donneesFiger.getIdRubrique();

                    if (!keys.contains(key)) {
                        AFTaxeCo2 ligneTaxe = new AFTaxeCo2();
                        ligneTaxe.setSession(getSession());
                        ligneTaxe.setAffiliationId(donneesFiger.getAffiliationId());
                        ligneTaxe.setAnneeMasse(donneesFiger.getAnnee());
                        ligneTaxe.setAnneeRedistribution(getAnneeRedistribution());
                        ligneTaxe.setMasse(donneesFiger.getMasse());
                        ligneTaxe.setMotifFin(donneesFiger.getMotifFin());
                        ligneTaxe.setIdRubrique(donneesFiger.getIdRubrique());
                        ligneTaxe.setEtat(CodeSystem.ETAT_TAXE_CO2_A_TRAITER);
                        ligneTaxe.add(getTransaction());

                        if (!getTransaction().hasErrors()) {
                            getTransaction().commit();
                            keys.add(key);
                        } else {
                            getMemoryLog().logMessage(getTransaction().getErrors().toString(), FWMessage.FATAL,
                                    donneesFiger.getNumAffilie());
                            getTransaction().rollback();
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Fait remonter l'erreur au processus parent
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            JadeLogger.error(this, e);
            return false;
        } finally {
            try {
                if (statement != null) {
                    try {
                        manager.cursorClose(statement);
                        cursorTransaction.rollback();
                    } finally {
                        statement.closeStatement();
                    }
                }
            } catch (Exception g) {
                getMemoryLog().logMessage(g.getMessage(), FWMessage.FATAL, this.getClass().getName());
                JadeLogger.error(this, g);
            }
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

    public String getAnneeMasse() {
        return anneeMasse;
    }

    public String getAnneeRedistribution() {
        return anneeRedistribution;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            StringBuffer buffer = new StringBuffer(getSession().getLabel("PROCESS_FIGER"));
            buffer.append(getSession().getLabel("PROCESS_FIGER_ERREUR"));
            return buffer.toString();
        } else {
            StringBuffer buffer = new StringBuffer(getSession().getLabel("PROCESS_FIGER"));
            buffer.append(getSession().getLabel("PROCESS_FIGER_SUCCES"));
            return buffer.toString();
        }
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public Boolean getReinitialiser() {
        return reinitialiser;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setAnneeMasse(String anneeMasse) {
        this.anneeMasse = anneeMasse;
    }

    public void setAnneeRedistribution(String anneeRedistribution) {
        this.anneeRedistribution = anneeRedistribution;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setReinitialiser(Boolean reinitialiser) {
        this.reinitialiser = reinitialiser;
    }

}
