/*
 * Créé le 18 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.process.communications.plausibiliteImpl;

import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.communications.CPLienCommunicationsPlausi;
import globaz.phenix.db.communications.CPParametrePlausibilite;
import globaz.phenix.db.communications.CPSedexConjoint;
import globaz.phenix.db.communications.CPSedexContribuable;
import globaz.phenix.db.communications.CPSedexDonneesBase;
import globaz.phenix.db.communications.CPSedexDonneesCommerciales;
import globaz.phenix.db.communications.CPSedexDonneesPrivees;
import globaz.phenix.interfaces.ICommunicationRetour;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.lang.reflect.Method;
import java.util.Vector;

/**
 * @author mmu
 * 
 *         Super-classe de toutes les règle de plausibilité. Une plausibilité est executée en utilisant validate(), la
 *         plausi concrête doit implementer les methodes qu'ont déclaré le CPParametrePlausibilite avec la communication
 *         et 2 String en parametre, et retourner un Boolean ex: protected Boolean
 *         isRadie(CPCommunicationFiscaleRetourViewBean com, String s1, String s2)
 */
public abstract class CPGenericReglePlausibilite {
    private ICommunicationRetour communicationRetour;
    private Vector<String> description = new Vector<String>(50);

    private Vector<String> idParam = new Vector<String>(50);

    // Objets business requis pour executer les plausibilites
    private CPJournalRetour journal;

    private Vector<Method> methods = new Vector<Method>(10);
    private int nbMethod = 0;
    private Vector<String> niveauMessage = new Vector<String>(50);
    private BProcess process;
    private String retourMethode = null;
    private CPSedexConjoint sedexConjoint;
    private CPSedexContribuable sedexContribuable;

    private CPSedexDonneesBase sedexDonneesBases;

    private CPSedexDonneesCommerciales sedexDonneesCommerciales;

    private CPSedexDonneesPrivees sedexDonneesPrivees;

    // Objets contextuels
    private BSession session;

    private TITiersViewBean tiers;

    private TITiersViewBean tiersProvisoire;

    private BITransaction transaction;

    /**
     * Permet d'ajouter un parametre/methode à une règle de plausibilite à executer lors de la validation Les methodes
     * seront executées dans l'ordre dans lequel elles ont été insérées
     * 
     * @param nomMethod
     * @param param1
     * @param param2
     */
    public void addMethod(String nomMethod, String param1, String param2, String param3) {
        try {
            Method method = this.getClass().getMethod(nomMethod,
                    new Class[] { String.class, String.class, String.class });
            methods.add(nbMethod, method);
            niveauMessage.add(nbMethod, param1);
            idParam.add(nbMethod, param2);
            description.add(nbMethod, param3);
            nbMethod++;
        } catch (Exception e) {
            process.getMemoryLog().logMessage(
                    FWMessageFormat.format(getSession().getLabel("GENPLAUSIREGLE_ERROR_UNABLE_USE_METHOD"), nomMethod)
                            + this.getClass().getName(), FWMessage.ERREUR, this.getClass().getName());

        }
    }

    /**
     * Enregistre un message dans la communication
     * 
     * @param message
     * @param type
     *            : provenant de FWMessage (INFORMATION, AVERTISSEMENT, ERREUR)
     * @throws Exception
     */
    protected void ajouterErreur(String idPlausi) throws Exception {
        CPLienCommunicationsPlausi lien = new CPLienCommunicationsPlausi();
        lien.setSession(getSession());
        lien.setIdCommunication(communicationRetour.getIdRetour());
        lien.setIdPlausibilite(idPlausi);
        lien.add(transaction);
    }

    /**
     * @return
     */
    public ICommunicationRetour getCommunicationRetour() {
        return communicationRetour;
    }

    /**
     * @return
     */
    public CPJournalRetour getJournal() {
        return journal;
    }

    /**
     * @return
     */
    public BProcess getProcess() {
        return process;
    }

    public String getRetourMethode() {
        return retourMethode;
    }

    public CPSedexConjoint getSedexConjoint() {
        return sedexConjoint;
    }

    public CPSedexContribuable getSedexContribuable() {
        return sedexContribuable;
    }

    public CPSedexDonneesBase getSedexDonneesBases() {
        return sedexDonneesBases;
    }

    public CPSedexDonneesCommerciales getSedexDonneesCommerciales() {
        return sedexDonneesCommerciales;
    }

    public CPSedexDonneesPrivees getSedexDonneesPrivees() {
        return sedexDonneesPrivees;
    }

    /**
     * @return
     */
    public BSession getSession() {
        return session;
    }

    public TITiersViewBean getTiers() {
        return tiers;
    }

    public TITiersViewBean getTiersProvisoire() {
        return tiersProvisoire;
    }

    /**
     * @return
     */
    public BITransaction getTransaction() {
        return transaction;
    }

    /**
     * @param retour
     */
    public void setCommunicationRetour(ICommunicationRetour retour) {
        communicationRetour = retour;
    }

    /**
     * @param retour
     */
    public void setJournal(CPJournalRetour retour) {
        journal = retour;
    }

    /**
     * @param process
     */
    public void setProcess(BProcess process) {
        this.process = process;
    }

    public void setRetourMethode(String retourMethode) {
        this.retourMethode = retourMethode;
    }

    public void setSedexConjoint(CPSedexConjoint sConjoint) {
        sedexConjoint = sConjoint;
    }

    public void setSedexContribuable(CPSedexContribuable sContribuable) {
        sedexContribuable = sContribuable;
    }

    public void setSedexDonneesBases(CPSedexDonneesBase sDonneesBases) {
        sedexDonneesBases = sDonneesBases;
    }

    public void setSedexDonneesCommerciales(CPSedexDonneesCommerciales sDonneesCommerciales) {
        sedexDonneesCommerciales = sDonneesCommerciales;
    }

    public void setSedexDonneesPrivees(CPSedexDonneesPrivees sDonneesPrivees) {
        sedexDonneesPrivees = sDonneesPrivees;
    }

    /**
     * @param session
     */
    public void setSession(BSession bSession) {
        session = bSession;
    }

    public void setTiers(TITiersViewBean tiers) {
        this.tiers = tiers;
    }

    public void setTiersProvisoire(TITiersViewBean tiersProvisoire) {
        this.tiersProvisoire = tiersProvisoire;
    }

    /**
     * @param transaction
     */
    public void setTransaction(BITransaction transaction) {
        this.transaction = transaction;
    }

    /**
     * executes les méthode de la règle de plausibilite
     * 
     */
    public String validate(ICommunicationRetour communication, String declench) throws Exception {
        setRetourMethode("");
        // la communication est mise dans le contexte de la méthode
        setCommunicationRetour(communication);
        if ((journal != null) && journal.getCanton().equalsIgnoreCase(CPJournalRetour.CS_CANTON_SEDEX)) {
            // Extraction des données du contribuable
            sedexContribuable = new CPSedexContribuable();
            sedexContribuable.setSession(getSession());
            sedexContribuable.setIdRetour(getCommunicationRetour().getIdRetour());
            sedexContribuable.retrieve();
            // Extraction des données de base du contribuable
            sedexDonneesBases = new CPSedexDonneesBase();
            sedexDonneesBases.setSession(getSession());
            sedexDonneesBases.setIdRetour(getCommunicationRetour().getIdRetour());
            sedexDonneesBases.retrieve();
            // Extraction des données privées du contribuable
            sedexDonneesPrivees = new CPSedexDonneesPrivees();
            sedexDonneesPrivees.setSession(getSession());
            sedexDonneesPrivees.setIdRetour(getCommunicationRetour().getIdRetour());
            sedexDonneesPrivees.retrieve();
            // Extraction des données commerciale du contribuable
            sedexDonneesCommerciales = new CPSedexDonneesCommerciales();
            sedexDonneesCommerciales.setSession(getSession());
            sedexDonneesCommerciales.setIdRetour(getCommunicationRetour().getIdRetour());
            sedexDonneesCommerciales.retrieve();
            // Extraction des données du conjoint
            sedexConjoint = new CPSedexConjoint();
            sedexConjoint.setSession(getSession());
            sedexConjoint.setIdRetour(getCommunicationRetour().getIdRetour());
            sedexConjoint.retrieve();
            /*
             * TODO Extraction des données de base du conjoint Extraction des données privées du conjoint Extraction des
             * données commerciale du conjoint
             */
        }
        // execute toutes les méthodes de la règle, une après l'autre
        for (int i = 0; (i < nbMethod)
                && !CPParametrePlausibilite.CS_MSG_ERREUR_CRITIQUE.equalsIgnoreCase(getRetourMethode()); i++) {
            Method met = methods.get(i);
            String niveau = niveauMessage.get(i);
            String idParamPlausi = idParam.get(i);
            String descriptionPlausi = description.get(i);
            try {
                String cErreur = (String) met.invoke(this, new Object[] { niveau, idParamPlausi, descriptionPlausi });
                if (!JadeStringUtil.isEmpty(cErreur)
                        && JadeStringUtil.isBlankOrZero(getCommunicationRetour().getIdParametrePlausi())) {
                    getCommunicationRetour().setIdParametrePlausi(idParamPlausi);
                }
                // si le code erreur est blanc ou est en erreur on ne le set pas
                if (!JadeStringUtil.isEmpty(cErreur)
                        && !getRetourMethode().equalsIgnoreCase(CPParametrePlausibilite.CS_MSG_ERREUR)
                        && !getRetourMethode().equalsIgnoreCase(CPParametrePlausibilite.CS_MSG_ERREUR_CRITIQUE)) {
                    setRetourMethode(cErreur);
                }
            } catch (Exception e) {
                process.getMemoryLog().logMessage(
                        FWMessageFormat.format(getSession().getLabel("GENPLAUSIREGLE_ERROR_UNABLE_USE_METHOD"),
                                met.getName())
                                + e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
            }
        }
        return getRetourMethode();
    }

}
