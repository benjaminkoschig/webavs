package globaz.osiris.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.ordres.format.opt.CAIListOrdreGroupeLite;
import globaz.osiris.print.itext.list.CAIListOrdreGroupe;
import globaz.osiris.process.journal.CAUtilsJournal;

/**
 * Cr�ation d'un ordre DTA Date de cr�ation : (08.02.2002 11:17:52)
 * 
 * @author: Administrator
 */
public class CAProcessOrdre extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean comptabiliserOrdre = true;
    private boolean echoToConsole = false;
    private String fileName = new String();
    private boolean genererFichierEchange = true;
    private String idOrdreGroupe = new String();
    private String idTypeOperation = new String();
    private Boolean imprimerJournal = new Boolean(true);
    private boolean insertNewLine = false;
    private CAOrdreGroupe ordreGroupe = null;
    private String typeImpression = "pdf";

    /**
     * Commentaire relatif au constructeur CAOrdreDTA.
     */
    public CAProcessOrdre() {
        super();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (29.04.2002 11:10:48)
     * 
     * @param parent
     *            BProcess
     */
    public CAProcessOrdre(BProcess parent) {
        super(parent);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.02.2002 15:01:51)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Ex�cution de l'ordre Date de cr�ation : (08.02.2002 11:25:48)
     * 
     * @return vrai si la cr�ation s'est bien d�roul�e
     */
    @Override
    protected boolean _executeProcess() {

        // V�rifier l'ordre group�
        if (JadeStringUtil.isIntegerEmpty(getIdOrdreGroupe())) {
            getMemoryLog().logMessage("5200", null, FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        // Sous controle d'exceptions
        try {
            // Instancier un ordre group�
            ordreGroupe = new CAOrdreGroupe();

            // Charger l'ordre
            ordreGroupe.setSession(getSession());
            ordreGroupe.setIdOrdreGroupe(getIdOrdreGroupe());
            ordreGroupe.retrieve(getTransaction());
            if (ordreGroupe.isNew() || ordreGroupe.hasErrors()) {
                getMemoryLog().logStringBuffer(ordreGroupe.getSession().getErrors(), ordreGroupe.getClass().getName());
                getMemoryLog().logMessage("5205", null, FWMessage.FATAL, this.getClass().getName());
                return false;
            }

            if (!ordreGroupe.getEtat().equals(CAOrdreGroupe.GENERE)
                    && !new CAUtilsJournal().isPeriodeComptableOuverte(getSession(), getTransaction(),
                            ordreGroupe.getDateEcheance())) {
                return false;
            }

            // Partager le log
            ordreGroupe.setMemoryLog(getMemoryLog());

            // Indiquer que l'on d�marre la cr�ation d'un ordre
            ordreGroupe.beforeExecuteOrdre(this);

            // Sortie si abort
            if (isAborted()) {
                return false;
            }

            // Mise � jour
            ordreGroupe.update(getTransaction());
            if (ordreGroupe.isNew() || ordreGroupe.hasErrors()) {
                getMemoryLog().logStringBuffer(ordreGroupe.getSession().getErrors(), ordreGroupe.getClass().getName());
                getMemoryLog().logMessage("5205", null, FWMessage.FATAL, this.getClass().getName());
                return false;
            }

            // Sortie si abort
            if (isAborted()) {
                return false;
            }

            // Ex�cuter l'ordre group�
            ordreGroupe.executeOrdre(this);

            // Sortie si abort
            if (isAborted()) {
                return false;
            }

            // Imprimer le journal
            if (getImprimerJournal().booleanValue() && !isOnError()) {
                setState(getSession().getLabel("6114"));
                getTransaction().commit();

                if (CAOrdreGroupe.isForceOPAEV1(getSession())) {
                    /*
                     * Permet de lancer la version1 en cas de probl�me... Pourrais bien disparaitre une fois la version
                     * "Lite" bien rod�e... vers la version 1-7 �ventuellement...
                     */
                    CAIListOrdreGroupe report = new CAIListOrdreGroupe(this);
                    report.setIdOrdreGroupe(getIdOrdreGroupe());
                    report.setIdTypeOperation(getIdTypeOperation());
                    report.setTypeImpression(getTypeImpression());
                    report.executeProcess();
                } else {
                    CAIListOrdreGroupeLite report = new CAIListOrdreGroupeLite(this);
                    report.setIdOrdreGroupe(getIdOrdreGroupe());
                    report.setIdTypeOperation(getIdTypeOperation());
                    report.setTypeImpression(getTypeImpression());
                    report.executeProcess();
                }

            }

            // R�cup�rer les exceptions
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }

        // Fin de la proc�dure
        return !isOnError();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 17:31:08)
     * 
     * @return boolean
     */
    public boolean getComptabiliserOrdre() {
        return comptabiliserOrdre;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 13:38:22)
     * 
     * @return boolean
     */
    public boolean getEchoToConsole() {
        return echoToConsole;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.02.2002 14:23:55)
     * 
     * @return String
     */
    @Override
    protected String getEMailObject() {

        // D�terminer l'objet du message en fonction du code erreur
        String obj;
        if (getMemoryLog().getErrorLevel().compareTo(FWMessage.ERREUR) >= 0) {
            obj = getSession().getLabel("5223");
        } else {
            obj = getSession().getLabel("5224");
        }

        String warningMsg = getSession().getWarnings().toString();
        if (!JadeStringUtil.isBlank(warningMsg)) {
            obj = warningMsg + " - " + obj;
        }

        obj = obj + " " + ordreGroupe.getMotif() + " (" + getIdOrdreGroupe() + ") ";

        // Restituer l'objet
        return obj;

    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 14:29:21)
     * 
     * @return String
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 17:32:29)
     * 
     * @return boolean
     */
    public boolean getGenererFichierEchange() {
        return genererFichierEchange;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 14:06:21)
     * 
     * @return String
     */
    public String getIdOrdreGroupe() {
        return idOrdreGroupe;
    }

    /**
     * @return
     */
    public String getIdTypeOperation() {
        return idTypeOperation;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (29.04.2002 13:17:13)
     * 
     * @return Boolean
     */
    public Boolean getImprimerJournal() {
        return imprimerJournal;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (11.02.2002 16:24:22)
     * 
     * @return boolean
     */
    public boolean getInsertNewLine() {
        return insertNewLine;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (02.04.2002 11:43:20)
     */
    public CAOrdreGroupe getOrdreGroupe() {
        if (ordreGroupe == null) {
            ordreGroupe = new CAOrdreGroupe();
            ordreGroupe.setISession(getSession());
            ordreGroupe.setIdOrdreGroupe(idOrdreGroupe);
            try {
                ordreGroupe.retrieve();
                if (ordreGroupe.isNew()) {
                    ordreGroupe = null;
                }
            } catch (Exception e) {
                ordreGroupe = null;
            }
        }

        return ordreGroupe;
    }

    public String getTypeImpression() {
        return typeImpression;
    }

    /**
     * Method jobQueue. Cette m�thode d�finit la nature du traitement s'il s'agit d'un processus qui doit-�tre lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 17:31:08)
     * 
     * @param newComptabiliserOrdre
     *            boolean
     */
    public void setComptabiliserOrdre(boolean newComptabiliserOrdre) {
        comptabiliserOrdre = newComptabiliserOrdre;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 13:38:22)
     * 
     * @param newEchoToConsole
     *            boolean
     */
    public void setEchoToConsole(boolean newEchoToConsole) {
        echoToConsole = newEchoToConsole;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 14:29:21)
     * 
     * @param newFileName
     *            String
     */
    public void setFileName(String newFileName) {
        fileName = newFileName;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 17:32:29)
     * 
     * @param newGenererFichierEchange
     *            boolean
     */
    public void setGenererFichierEchange(boolean newGenererFichierEchange) {
        genererFichierEchange = newGenererFichierEchange;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 14:06:21)
     * 
     * @param newIdOrdreGroupe
     *            String
     */
    public void setIdOrdreGroupe(String newIdOrdreGroupe) {
        idOrdreGroupe = newIdOrdreGroupe;
    }

    /**
     * @param string
     */
    public void setIdTypeOperation(String string) {
        idTypeOperation = string;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (29.04.2002 13:17:13)
     * 
     * @param newImprimerJournal
     *            Boolean
     */
    public void setImprimerJournal(Boolean newImprimerJournal) {
        imprimerJournal = newImprimerJournal;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (11.02.2002 16:24:22)
     * 
     * @param newInsertNewLine
     *            boolean
     */
    public void setInsertNewLine(boolean newInsertNewLine) {
        insertNewLine = newInsertNewLine;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }

}
