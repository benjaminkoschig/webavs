/*
 * Créé le 18 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.process.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.communications.CPLienCommunicationsPlausi;
import globaz.phenix.db.communications.CPLienCommunicationsPlausiManager;
import globaz.phenix.db.communications.CPParametrePlausibilite;
import globaz.phenix.db.communications.CPParametrePlausibiliteManager;
import globaz.phenix.db.communications.CPReglePlausibilite;
import globaz.phenix.db.communications.CPReglePlausibiliteManager;
import globaz.phenix.interfaces.ICommunicationRetour;
import globaz.phenix.process.communications.plausibiliteImpl.CPGenericReglePlausibilite;

/**
 * @author mmu Process qui retourMethode les règles chargementPlausibilites sur un journal ou sur une communication
 *         (dépend selon idJournalRetour ou bien idCommunicationRetour est renseigné)
 */
public class CPProcessValiderPlausibilite extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String VALIDER_COMMUNICATION = "2";

    public final static String VALIDER_JOURNAL = "1";

    // et l'id de la plausibilité
    private CPGenericReglePlausibilite[] chargementPlausibilites = null;
    private ICommunicationRetour communicationRetour;
    private String declenchement = "";

    private String idJournalRetour;

    private CPJournalRetour journal;

    private String retourMethode = ""; // Contient la valeur du niveau d'erreur

    private boolean wantMajBackup = false;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        boolean successful = true;
        try {
            getJournal();
        } catch (Exception e) {
            successful = false;
        }
        if (successful && (journal != null)) {
            // Instancie toutes les règle de plausibilités avec leurs méthodes
            setChargementPlausibilites(this.chargementPlausibilites(getDeclenchement(), journal.getCanton()));
        } else {
            // Instancie toutes les règle de plausibilités avec leurs méthodes
            setChargementPlausibilites(this.chargementPlausibilites(getDeclenchement()));
        }
        successful = validerCommunicationRetour();

        return successful;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        if (getCommunicationRetour() == null) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(getSession().getLabel("PROCVALPLAU_NO_IDCOMM"));
            abort();
        }
    }

    /**
     * Renvoie sous forme de vecteur les instances des regles de chargementPlausibilites triees par ordre alphabetique
     * 
     * @return
     */
    private CPGenericReglePlausibilite[] chargementPlausibilites(String declenchement) {
        return this.chargementPlausibilites(declenchement, "");
    }

    /**
     * Renvoie sous forme de vecteur les instances des regles de chargementPlausibilites triees par ordre alphabetique
     * 
     * @return
     */
    private CPGenericReglePlausibilite[] chargementPlausibilites(String declenchement, String canton) {
        if (getChargementPlausibilites() == null) {
            // RECHERCHE DES REGLES DE PLAUSIBILITES ACTIVES
            CPReglePlausibiliteManager plausiManager = new CPReglePlausibiliteManager();
            plausiManager.setSession(getSession());
            plausiManager.setForActif(new Boolean(true));
            plausiManager.setForDeclenchement(getDeclenchement());
            plausiManager.setForCanton(canton);
            try {
                plausiManager.find();
            } catch (Exception e) {
                getMemoryLog().logMessage(
                        getSession().getLabel("PROCVALPLAU_ERROR_PLAUSIREGLE_NOT_FOUND") + e.getMessage(),
                        FWMessage.ERREUR, this.getClass().getName());
            }

            int nbPlausi = plausiManager.getSize();

            // Instancie les règles de plausibilités dans un vecteur en lui et
            // determine les méthodes/paramêtres qui seront executés pour la
            // validation
            CPGenericReglePlausibilite[] instPlausiList = new CPGenericReglePlausibilite[nbPlausi];
            int totalInstPlausi = 0;
            int i = 0;
            while (i < nbPlausi) {
                CPReglePlausibilite regle = (CPReglePlausibilite) plausiManager.getEntity(i);
                try {
                    CPGenericReglePlausibilite regleInstance = instanciatePlausi(regle);
                    instPlausiList[totalInstPlausi] = regleInstance;
                    totalInstPlausi++;
                } catch (Exception e) {
                    e.printStackTrace();
                    getMemoryLog()
                            .logMessage(
                                    FWMessageFormat.format(
                                            getSession().getLabel("PROCVALPLAU_ERROR_PLAUSIREGLE_VALIDATION"),
                                            regle.getClasspath())
                                            + e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
                } finally {
                    i++;
                }
            }

            if (totalInstPlausi == 0) {
                getMemoryLog().logMessage(getSession().getLabel("PROCVALPLAU_NO_PAUSIREGLE_APPLIED"),
                        FWMessage.INFORMATION, this.getClass().getName());
            }
            return instPlausiList;
        } else {
            return getChargementPlausibilites();
        }
    }

    public CPGenericReglePlausibilite[] getChargementPlausibilites() {
        return chargementPlausibilites;
    }

    /*
     * Vérifie que la communication n'a pas d'erreur de réception
     */
    /*
     * private boolean hasReceptionError(ICommunicationRetour communication) { FWLog log =
     * communication.getLog(getTransaction()); if (log != null && FWMessage.ERREUR.equals(log.getErrorLevel())) {
     * Enumeration enumLog = log.getMessagesToEnumeration(); while (enumLog.hasMoreElements()) { FWMessage msg =
     * (FWMessage) enumLog.nextElement(); if (CPCommunicationFiscaleRetourViewBean
     * .LOG_SOURCE_RECEPTION.equals(msg.getMessageId())) { if (FWMessage.ERREUR.equals(msg.getTypeMessage())) { return
     * true; } } } } return false; }
     */

    /**
     * Fixe la transaction à utiliser par ^les plausibilités pour valider un lot de communications
     * 
     * @param chargementPlausibilites
     * @param nbPlausiInst
     * @param transaction
     */
    /*
     * private void setTransaction(CPGenericReglePlausibilite[] plausibilites, int nbPlausiInst, BITransaction
     * transaction) { for (int i = 0; i < nbPlausiInst; i++) { plausibilites[i].setTransaction(transaction); } }
     */

    /**
     * @return
     */
    public ICommunicationRetour getCommunicationRetour() {
        return communicationRetour;
    }

    public String getDeclenchement() {
        return declenchement;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("PROCVALPLAU_EMAIL_OBJECT_COMM");
    }

    /**
     * @return
     */
    public String getIdJournalRetour() {
        return idJournalRetour;
    }

    /**
     * @return le journalRetour ou une exception en cas d'erreur
     */
    public CPJournalRetour getJournal() throws Exception {
        if (journal == null) {
            journal = new CPJournalRetour();
            journal.setISession(getISession());
            if (!JadeStringUtil.isEmpty(idJournalRetour)) {
                journal.setIdJournalRetour(idJournalRetour);
            } else {
                journal.setIdJournalRetour(communicationRetour.getIdJournalRetour());
            }
            journal.retrieve();
        }
        return journal;
    }

    /*
     * private void removeLogValidation(String idJournal) throws Exception { // Valide les plausibilités sur chaque
     * communication CPCommunicationFiscaleRetourManager comManager = new CPCommunicationFiscaleRetourManager();
     * comManager.setSession(getSession()); comManager.setForIdJournalRetour(idJournal); BStatement statement =
     * comManager.cursorOpen(getTransaction()); CPCommunicationFiscaleRetourViewBean communication = null; while
     * ((communication = (CPCommunicationFiscaleRetourViewBean) comManager.cursorReadNext(statement)) != null &&
     * !communication.isNew()) {communication.removeLogs(CPCommunicationFiscaleRetourViewBean. LOG_SOURCE_VALIDATION,
     * getTransaction()); } }
     */

    /**
     * determine le nb total de règles de chargementPlausibilites
     * 
     * @param chargementPlausibilites
     * @return
     */
    private int getNbPlausiInst(CPGenericReglePlausibilite[] plausibilites) {
        int nbPlausiInst = 0;
        for (int i = 0; i < plausibilites.length; i++) {
            if (plausibilites[i] != null) {
                nbPlausiInst++;
            }
        }
        return nbPlausiInst;
    }

    public String getRetourMethode() {
        return retourMethode;
    }

    /**
     * Instance la règle de plausibilité enregistrée comme CPReglePlausibilite, et retourMethode les parametres actifs
     * par ordre de priorité
     * 
     * @param regle
     */
    private CPGenericReglePlausibilite instanciatePlausi(CPReglePlausibilite regle) throws Exception {
        // Instancie la regle de plausibilite
        String classpath = regle.getClasspath();
        Class<?> implClass = Class.forName(classpath);
        CPGenericReglePlausibilite instance = (CPGenericReglePlausibilite) implClass.newInstance();

        // fixe le context à l'instance
        instance.setProcess(this);
        instance.setSession(getSession());
        instance.setJournal(getJournal());

        // Retrouve les parametres (méthode) associés à la regle de plausibilite
        CPParametrePlausibiliteManager paraManager = new CPParametrePlausibiliteManager();
        paraManager.setSession(getSession());
        paraManager.setForIdPlausibilite(regle.getIdPlausibilite());
        paraManager.setForActif(new Boolean(true));
        paraManager.find();

        int nbParam = paraManager.getSize();

        // On ajoute a l'instance les méthodes que l'on souhaite executer
        for (int i = 0; i < nbParam; i++) {
            CPParametrePlausibilite param = (CPParametrePlausibilite) paraManager.getEntity(i);
            String nomMethod = param.getNomCle();
            String param1 = param.getTypeMessage();
            String description = "";
            if (getSession().getIdLangueISO().equalsIgnoreCase("IT")) {
                description = param.getDescription_it();
            } else if (getSession().getIdLangueISO().equalsIgnoreCase("DE")) {
                description = param.getDescription_de();
            } else if (getSession().getIdLangueISO().equalsIgnoreCase("FR")) {
                description = param.getDescription_fr();
            }
            instance.addMethod(nomMethod, param1, param.getIdParametre(), description);
        }

        return instance;

    }

    public boolean isWantMajBackup() {
        return wantMajBackup;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void resetValide() {
        retourMethode = "";
    }

    /**
     * @param chargementPlausibilites
     */
    public void setChargementPlausibilites(CPGenericReglePlausibilite[] plausibilites) {
        chargementPlausibilites = plausibilites;
    }

    /**
     * @param retour
     */
    public void setCommunicationRetour(ICommunicationRetour retour) {
        communicationRetour = retour;
    }

    public void setDeclenchement(String declenchement) {
        this.declenchement = declenchement;
    }

    /**
     * @param string
     */
    public void setIdJournalRetour(String string) {
        idJournalRetour = string;
    }

    public void setJournal(CPJournalRetour journal) {
        this.journal = journal;
    }

    public void setRetourMethode(String retourMethode) {
        this.retourMethode = retourMethode;
    }

    public void setWantMajBackup(boolean wantMajBackup) {
        this.wantMajBackup = wantMajBackup;
    }

    /**
     * @return
     */
    private boolean validerCommunicationRetour() {
        boolean successful = true;
        try {
            int nbPlausiInst = getNbPlausiInst(getChargementPlausibilites());
            // Recherche la communication
            CPLienCommunicationsPlausiManager lienManager = new CPLienCommunicationsPlausiManager();
            lienManager.setSession(getSession());
            lienManager.setForIdCommunication(getCommunicationRetour().getIdRetour());
            lienManager.find();
            for (int i = 0; i < lienManager.size(); i++) {
                CPLienCommunicationsPlausi entity = ((CPLienCommunicationsPlausi) lienManager.getEntity(i));
                entity.delete();
            }
            resetValide();
            String retour = "";
            getCommunicationRetour().setStatus("");
            /* execute toutes les règles de plausibilités sur la communication */
            for (int i = 0; (i < nbPlausiInst)
                    && !CPParametrePlausibilite.CS_MSG_ERREUR_CRITIQUE.equalsIgnoreCase(getRetourMethode()); i++) {
                retour = chargementPlausibilites[i].validate(getCommunicationRetour(), declenchement);
                if (!JadeStringUtil.isEmpty(retour)
                        && !retourMethode.equalsIgnoreCase(CPCommunicationFiscaleRetourViewBean.CS_ERREUR)
                        && !CPParametrePlausibilite.CS_MSG_ERREUR.equalsIgnoreCase(getRetourMethode())) {
                    setRetourMethode(retour);
                }
            }
            // Mise à jour du statut de la communication
            if (JadeStringUtil.isBlankOrZero(getCommunicationRetour().getStatus())) {
                if (!JadeStringUtil.isEmpty(getRetourMethode())) {
                    if (CPParametrePlausibilite.CS_MSG_AVERTISSEMENT.equalsIgnoreCase(getRetourMethode())) {
                        if (CPReglePlausibilite.CS_AVANT_GENERATION.equalsIgnoreCase(getDeclenchement())) {
                            getCommunicationRetour().setStatus(CPCommunicationFiscaleRetourViewBean.CS_AVERTISSEMENT);
                        } else {
                            getCommunicationRetour().setStatus(CPCommunicationFiscaleRetourViewBean.CS_A_CONTROLER);
                        }
                    } else if (CPParametrePlausibilite.CS_MSG_ERREUR.equalsIgnoreCase(getRetourMethode())
                            || CPParametrePlausibilite.CS_MSG_ERREUR_CRITIQUE.equalsIgnoreCase(getRetourMethode())) {
                        getCommunicationRetour().setStatus(CPCommunicationFiscaleRetourViewBean.CS_ERREUR);
                    }
                } else {
                    if (CPReglePlausibilite.CS_AVANT_GENERATION.equalsIgnoreCase(getDeclenchement())) {
                        getCommunicationRetour().setStatus(CPCommunicationFiscaleRetourViewBean.CS_RECEPTIONNE);
                    } else {
                        getCommunicationRetour().setStatus(CPCommunicationFiscaleRetourViewBean.CS_SANS_ANOMALIE);
                    }
                }
            }
            CPCommunicationFiscaleRetourViewBean entity = new CPCommunicationFiscaleRetourViewBean();
            entity.setSession(getSession());
            entity.setIdRetour(getCommunicationRetour().getIdRetour());
            entity.retrieve(getTransaction());
            // Comme le conjoint est traité em même temps que celui qui a reçu
            // la comm. fiscale
            // => ne pas mettre à jour car sinon le conjoint remplace le
            // titulaire de la com.fiscale
            if (!entity.isNew()) {
                if (!entity.getIdConjoint().equals(getCommunicationRetour().getIdTiers())
                        || JadeStringUtil.isBlankOrZero(entity.getIdTiers())) {
                    entity.setStatus(getCommunicationRetour().getStatus());
                    entity.setIdLog(getCommunicationRetour().getIdLog());
                    // if
                    // (CPReglePlausibilite.CS_AVANT_GENERATION.equalsIgnoreCase(getDeclenchement()))
                    // {
                    entity.setIdTiers(getCommunicationRetour().getIdTiers());
                    entity.setIdAffiliation(getCommunicationRetour().getIdAffiliation());
                    entity.setIdAffiliationConjoint(getCommunicationRetour().getIdAffiliationConjoint());
                    entity.setIdConjoint(getCommunicationRetour().getIdConjoint());
                    entity.setIdIfd(getCommunicationRetour().getIdIfd());
                    entity.setIdCommunication(getCommunicationRetour().getIdCommunication());
                    entity.setChangementGenre(getCommunicationRetour().getChangementGenre());
                    entity.setChangementGenreConjoint(getCommunicationRetour().getChangementGenreConjoint());
                    entity.setGenreAffilie(getCommunicationRetour().getGenreAffilie());
                    entity.setGenreConjoint(getCommunicationRetour().getGenreConjoint());
                    entity.setIdParametrePlausi(getCommunicationRetour().getIdParametrePlausi());
                    entity.setGenreTaxation(getCommunicationRetour().getGenreTaxation());
                    entity.setFortune(getCommunicationRetour().getFortune());
                    entity.setDebutExercice1(getCommunicationRetour().getDebutExercice1());
                    entity.setFinExercice1(getCommunicationRetour().getFinExercice1());
                    entity.setRevenu1(getCommunicationRetour().getRevenu1());
                    entity.setRevenu2(getCommunicationRetour().getRevenu2());
                    entity.setAnnee1(getCommunicationRetour().getAnnee1());
                    entity.setCapital(getCommunicationRetour().getCapital());
                    entity.setTri(getCommunicationRetour().getTri());
                    // }
                    entity.setWantControleSpy(false);
                    entity.update(getTransaction());
                    if (isWantMajBackup()) {
                        entity.setForBackup(true);
                        entity.update(getTransaction());
                    }
                }
            }
            // permet de commiter lorsque l'on modifie les com. fisc. à l'écran
            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
            }
        } catch (Exception e) {
            successful = false;
            getMemoryLog().logMessage(getSession().getLabel("PROCVALPLAU_ERROR_UNABLE_TO_VAL_COM") + e.getMessage(),
                    FWMessage.ERREUR, "CPProcessValiderPlausibilite");
            getMemoryLog().logMessage("\t\t\t" + e.getMessage(), FWMessage.ERREUR, "CPProcessValiderPlausibilite");
        }
        return successful;

    }

}
