package globaz.osiris.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.ordres.exception.CAOGOVMaxISODepassement;
import globaz.osiris.db.ordres.exception.CAOGRegroupISODepassement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.osiris.business.constantes.CAProperties;

/**
 * Gérer l'attachement d'un ordre de Date de création : (22.02.2002 09:08:40)
 * 
 * @author: Administrator
 * @revision SCO 19 mars 2010
 */
public class CAProcessAttacherOrdre extends BProcess {
    private final static Logger logger = LoggerFactory.getLogger(CAProcessAttacherOrdre.class);
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idJournalSource = null;
    private String idOrdreGroupe = "";
    private CAOrdreGroupe ordreGroupe = null;
    private List<String> listOg = new ArrayList<String>();

    private boolean isNotLast;

    /**
     * Commentaire relatif au constructeur CAProcessAttacherOrdre.
     */
    public CAProcessAttacherOrdre() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 11:11:15)
     * 
     * @param parent
     *            BProcess
     */
    public CAProcessAttacherOrdre(BProcess parent) {
        super(parent);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {

        // Vérifier l'ordre groupé
        if (JadeStringUtil.isIntegerEmpty(getIdOrdreGroupe())) {
            getMemoryLog().logMessage("5200", null, FWMessage.FATAL, this.getClass().getName());
            return false;
        } else {
            isNotLast = true;
        }

        // Sous controle d'exceptions
        try {

            while (isNotLast) {
                // Charger l'ordre
                ordreGroupe = getOrdreGroupe();

                if (ordreGroupe == null) {
                    getMemoryLog().logStringBuffer(getTransaction().getErrors(), ordreGroupe.getClass().getName());
                    getMemoryLog().logMessage("5205", null, FWMessage.FATAL, this.getClass().getName());
                    return false;
                }

                // Partager le log
                ordreGroupe.setMemoryLog(getMemoryLog());

                // Indiquer que l'on démarre la création d'un ordre
                ordreGroupe.beforeExecuteAttacherOrdre(this);

                // Sortie si abort
                if (isAborted()) {
                    return false;
                }

                // Mise à jour
                ordreGroupe.update(getTransaction());
                if (ordreGroupe.isNew() || ordreGroupe.hasErrors()) {
                    getMemoryLog().logStringBuffer(getTransaction().getErrors(), ordreGroupe.getClass().getName());
                    getMemoryLog().logMessage("5205", null, FWMessage.FATAL, this.getClass().getName());
                    return false;
                }

                // Sortie si abort
                if (isAborted()) {
                    return false;
                }

                // Exécuter l'attachement de l'ordre groupé
                isNotLast = false;
                try {
                    ordreGroupe.executeAttacherOrdre(this, idJournalSource);
                } catch (CAOGRegroupISODepassement e) {
                    isNotLast = true;
                } catch (CAOGOVMaxISODepassement e) {
                    throw e;
                }
                if (ordreGroupe.isNew() || ordreGroupe.hasErrors()) {
                    getMemoryLog().logStringBuffer(getTransaction().getErrors(), ordreGroupe.getClass().getName());
                    getMemoryLog().logMessage("5205", null, FWMessage.FATAL, this.getClass().getName());
                    return false;
                }

                // Sortie si abort
                if (isAborted()) {
                    return false;
                }

                // Mise à jour
                ordreGroupe.update(getTransaction());
                if (ordreGroupe.isNew() || ordreGroupe.hasErrors()) {
                    getMemoryLog().logStringBuffer(getTransaction().getErrors(), ordreGroupe.getClass().getName());
                    getMemoryLog().logMessage("5205", null, FWMessage.FATAL, this.getClass().getName());
                    return false;
                }
                if (isNotLast) {
                    prepareForNext(getTransaction());
                    // TODO remonter l'info sans que ca fasse peter le process (transaction KO si dans le memory log)
                    // getMemoryLog().logMessage("SEPA_ADDITIONNAL_ORDRE_GROUPE", ordreGroupe.getIsoNumLivraison(),
                    // FWMessage.AVERTISSEMENT, this.getClass().getName());
                    listOg.add(ordreGroupe.getIsoNumLivraison());
                }
            }

            // Récupérer les exceptions
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }

        // Fin de la procédure
        return !isOnError();

    }

    /**
     * use by attacherOrdre for case where number of OV to link to OG is higter than specified propertie nbmax.ovparog
     * 
     * @param transaction current transaction
     * @throws Exception exception can occure during transaction attachement to the new entity
     */
    private void prepareForNext(BTransaction transaction) throws Exception {

        if ((1 + listOg.size()) >= Integer.parseInt(CAProperties.ISO_SEPA_MAX_MULTIOG.getValue())) {
            getMemoryLog().logMessage(
                    getSession().getLabel("SEPA_PREPARATION_MAX_MULTI_OG")
                            + CAProperties.ISO_SEPA_MAX_MULTIOG.getValue(), FWMessage.FATAL, this.getClass().getName());
        }

        CAOrdreGroupe newOrdreGroupe = new CAOrdreGroupe();
        newOrdreGroupe.setSession(getSession());
        newOrdreGroupe.setIdOrganeExecution(ordreGroupe.getIdOrganeExecution());
        newOrdreGroupe.setNumeroOG(ordreGroupe.getNumeroOG());
        newOrdreGroupe.setDateEcheance(ordreGroupe.getDateEcheance());
        newOrdreGroupe.setTypeOrdreGroupe(ordreGroupe.getTypeOrdreGroupe());
        newOrdreGroupe.setNatureOrdresLivres(ordreGroupe.getNatureOrdresLivres());
        newOrdreGroupe.setIsoGestionnaire(ordreGroupe.getIsoGestionnaire());
        newOrdreGroupe.setIsoHighPriority(ordreGroupe.getIsoHighPriority());
        newOrdreGroupe.setMotif(ordreGroupe.getMotif());

        try {
            newOrdreGroupe.add(transaction);

            if (newOrdreGroupe.hasErrors()) {
                getMemoryLog().logStringBuffer(transaction.getErrors(), CAOrdreGroupe.class.getName());
                getMemoryLog().logMessage("POG_ADD_ORDRE_GROUPE", null, FWMessage.FATAL, this.getClass().getName());
            }

        } catch (Exception e) {
            getMemoryLog().logMessage("5002", "Error in ordreGroupe.add - " + e.getMessage(), FWMessage.FATAL,
                    this.getClass().getName());
            throw e;
        }
        ordreGroupe = newOrdreGroupe;

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return String
     */
    @Override
    protected String getEMailObject() {

        // Déterminer l'objet du message en fonction du code erreur
        String obj;

        if (getMemoryLog().hasErrors()) {
            obj = getSession().getLabel("5401");
        } else {
            obj = getSession().getLabel("5400");
        }

        // Restituer l'objet
        return obj;

    }

    public String getIdJournalSource() {
        return idJournalSource;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.02.2002 09:21:41)
     * 
     * @return String
     */
    public String getIdOrdreGroupe() {
        return idOrdreGroupe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.04.2002 11:43:20)
     */
    public CAOrdreGroupe getOrdreGroupe() {
        if (ordreGroupe == null) {
            ordreGroupe = new CAOrdreGroupe();
            ordreGroupe.setISession(getSession());
            ordreGroupe.setIdOrdreGroupe(getIdOrdreGroupe());
            try {
                ordreGroupe.retrieve(getTransaction());
                if (ordreGroupe.isNew()) {
                    ordreGroupe = null;
                }
            } catch (Exception e) {
                ordreGroupe = null;
            }
        }

        return ordreGroupe;
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

    public void setIdJournalSource(String idJournalSource) {
        this.idJournalSource = idJournalSource;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.02.2002 09:21:41)
     * 
     * @param newIdOrdreGroupe
     *            String
     */
    public void setIdOrdreGroupe(String newIdOrdreGroupe) {
        idOrdreGroupe = newIdOrdreGroupe;
    }
}
