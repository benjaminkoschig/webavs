/*
 * Créé le 8 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.process;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CAAuxiliaire;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.contentieux.CAMotifContentieux;
import globaz.osiris.process.journal.CAComptabiliserJournal;
import globaz.osiris.translation.CACodeSystem;
import java.util.Iterator;
import java.util.Map;

/**
 * <H1>Description</H1>
 * <p>
 * Processus de création d'une action en réparation de dommages.
 * </p>
 * <p>
 * Crée simplement des sous-sections auxilliaires pour chacun des administrateurs. Relie cette sous-section à la section
 * principale donnée et renseigne son montant en accord avec le montant dont l'administrateur est responsable (transmis
 * dans l'écran).
 * </p>
 * 
 * @author vre
 */
public class COProcessCreerARD extends BProcess {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = -5098880300109854649L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idSectionPrincipale;
    private CAJournal journal;
    private String libelleJournal;
    private Map montantsParAdmin;

    private CASection sectionPrincipale;
    private Map sequencesParAdmin;
    private BSession sessionOsiris;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe COProcessCreerARD.
     */
    public COProcessCreerARD() {
    }

    /**
     * Crée une nouvelle instance de la classe COProcessCreerARD.
     * 
     * @param parent
     */
    public COProcessCreerARD(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe COProcessCreerARD.
     * 
     * @param session
     */
    public COProcessCreerARD(BSession session) {
        super(session);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        BTransaction transaction = null;

        try {
            // ouvrir une nouvelle transaction
            transaction = (BTransaction) getSession().newTransaction();
            transaction.openTransaction();

            // créé le journal
            journal = createJournal(transaction);

            // creer des sous-sections pour chaque administrateur
            for (Iterator montants = montantsParAdmin.entrySet().iterator(); montants.hasNext();) {
                Map.Entry montantParAdmin = (Map.Entry) montants.next();
                String montant = (String) montantParAdmin.getValue();
                CASection ssSection = creerSousSection((String) montantParAdmin.getKey(), transaction);

                creerOperAux(ssSection, (String) montantParAdmin.getValue(), transaction);
                creerContentieux(ssSection, montant, (String) sequencesParAdmin.get(montantParAdmin.getKey()),
                        transaction);

                if (transaction.hasErrors()) {
                    this.logMessage(getSession().getLabel("AQUILA_ARD_IMPOSSIBLE_CHARGER_ADMINISTRATEUR")
                            + ssSection.getCompteAnnexe().getTiers().getPrenomNom());
                    this.logMessage(transaction.getErrors().toString());

                    break;
                } else {
                    this.logMessage(getSession().getLabel("AQUILA_ARD_CREEE_POUR_ADMINISTRATEUR") + " "
                            + ssSection.getCompteAnnexe().getTiers().getPrenomNom(), FWMessage.INFORMATION);
                }
            }

            // bloquer le contentieux pour la section principale si ce n'est pas
            // déjà le cas
            if (!sectionPrincipale().getContentieuxEstSuspendu().booleanValue()) {
                sectionPrincipale().setContentieuxEstSuspendu(Boolean.TRUE);
                sectionPrincipale().update();

                CAMotifContentieux motifContentieux = new CAMotifContentieux();

                motifContentieux.setSession(sessionOsiris());
                motifContentieux.setDateDebut(JACalendar.todayJJsMMsAAAA());
                motifContentieux.setDateFin("31.12.2999");
                motifContentieux.setIdSection(idSectionPrincipale);
                motifContentieux.setIdMotifBlocage(CACodeSystem.CS_BLOQUAGE_ARD);
                motifContentieux.add();
            }

            if (!transaction.hasErrors()) {
                transaction.commit();
                new CAComptabiliserJournal().comptabiliser(this, journal);
            } else {
                transaction.rollback();
            }
        } catch (Exception e) {
            this.logMessage(e.getMessage());
            abort();

            return false;
        } finally {
            try {
                transaction.closeTransaction();
            } catch (Exception e) {
            }
        }

        return true;
    }

    private CAJournal createJournal(BTransaction transaction) throws Exception {
        journal = new CAJournal();
        journal.setTypeJournal(CAJournal.TYPE_CONTENTIEUX);
        journal.setDateValeurCG(JACalendar.todayJJsMMsAAAA());
        journal.setLibelle(libelleJournal);

        journal.setSession(sessionOsiris());
        journal.add(transaction);

        if (journal.isNew()) {
            throw new Exception(getSession().getLabel("JOURNAL_IMPOSSIBLE_CREER_JOURNAL"));
        }

        return journal;
    }

    private COContentieux creerContentieux(CASection section, String montant, String idSequence,
            BTransaction transaction) throws Exception {
        COContentieux contentieux = new COContentieux();

        contentieux.setDateDeclenchement("");
        contentieux.setDateExecution("");
        contentieux.setDateOuverture(JACalendar.todayJJsMMsAAAA());
        contentieux.setIdCompteAnnexe(section.getIdCompteAnnexe());
        contentieux.setIdCompteAnnexePrincipal(sectionPrincipale().getIdCompteAnnexe());
        contentieux.setIdSection(section.getIdSection());
        contentieux.setMontantInitial(montant);
        contentieux.setProchaineDateDeclenchement("");
        contentieux.setUser(getSession().getUserName());
        contentieux.setIdSequence(idSequence);

        contentieux.setISession(getSession());
        contentieux.add(transaction);

        return contentieux;
    }

    private void creerOperAux(CASection section, String montant, BTransaction transaction) throws Exception {
        CAAuxiliaire auxiliaire = new CAAuxiliaire();

        auxiliaire.setIdJournal(journal.getIdJournal());
        auxiliaire.setIdCompteAnnexe(section.getIdCompteAnnexe());
        auxiliaire.setIdSection(section.getIdSection());
        auxiliaire.setDate(journal.getDateValeurCG());
        auxiliaire.setMontant(montant);
        auxiliaire.setCodeDebitCredit(APIEcriture.DEBIT);
        auxiliaire.setLibelle(getSession().getLabel("AQUILA_ARD_LIBELLE_OPERATION"));
        // auxiliaire.setQuittanceLogEcran(Boolean.TRUE);

        // SPA (09.02.06): PAS DE RUBRIQUE POUR UNE OP AUX.
        // auxiliaire.setIdCompte("");
        auxiliaire.setSession(sessionOsiris());
        auxiliaire.add(transaction);
    }

    private CASection creerSousSection(String idCompteAnnexe, BTransaction transaction) throws Exception {
        CASection section = new CASection();
        if (!sectionPrincipale().getCategorieSection().equals(APISection.ID_CATEGORIE_SECTION_REPARATION_DOMMAGES)) {
            throw new Exception(getSession().getLabel("AQUILA_ARD_CATEGORIE_SECTION_PAS_96"));
        }
        section.copyDataFromEntity(sectionPrincipale());
        section.setIdSection("");
        section.setSolde("");
        section.setPmtCmp("");
        section.setFrais("");
        section.setAmende("");
        section.setTaxes("");
        section.setInterets("");
        section.setBase("");
        section.setIdCompteAnnexe(idCompteAnnexe);
        section.setIdSectionPrincipal(idSectionPrincipale);
        section.setIdTypeSection(APISection.ID_TYPE_SECTION_ARD);
        section.setIdSequenceContentieux("0");

        section.setSession(sessionOsiris());
        section.add(transaction);

        return section;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("AQUILA_SUJETMAIL_CREER_ARD");
    }

    /**
     * getter pour l'attribut id section principale.
     * 
     * @return la valeur courante de l'attribut id section principale
     */
    public String getIdSectionPrincipale() {
        return idSectionPrincipale;
    }

    /**
     * getter pour l'attribut libelle journal.
     * 
     * @return la valeur courante de l'attribut libelle journal
     */
    public String getLibelleJournal() {
        return libelleJournal;
    }

    /**
     * getter pour l'attribut montants par admin.
     * 
     * @return la valeur courante de l'attribut montants par admin
     */
    public Map getMontantsParAdmin() {
        return montantsParAdmin;
    }

    /**
     * getter pour l'attribut sequences par admin.
     * 
     * @return la valeur courante de l'attribut sequences par admin
     */
    public Map getSequencesParAdmin() {
        return sequencesParAdmin;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * ajoute une ERROR dans le memorylog.
     * 
     * @param message
     *            DOCUMENT ME!
     */
    private void logMessage(String message) {
        this.logMessage(message, FWViewBeanInterface.ERROR);
    }

    /**
     * ajoute un message de type donné dans le memorylog.
     * 
     * @param message
     *            DOCUMENT ME!
     * @param msgType
     *            DOCUMENT ME!
     */
    private void logMessage(String message, String msgType) {
        getMemoryLog().logMessage(message, msgType, this.getClass().getName());
    }

    private CASection sectionPrincipale() throws Exception {
        if (sectionPrincipale == null) {
            sectionPrincipale = new CASection();
            sectionPrincipale.setIdSection(idSectionPrincipale);
            sectionPrincipale.setSession(sessionOsiris());
            sectionPrincipale.retrieve();

            if (sectionPrincipale.isNew()) {
                throw new Exception(getSession().getLabel("AQUILA_ARD_IMPOSSIBLE_DE_CHARGER_SECT_PRINC"));
            }
        }

        return sectionPrincipale;
    }

    private BSession sessionOsiris() throws Exception {
        if (sessionOsiris == null) {
            sessionOsiris = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS)
                    .newSession();
            getSession().connectSession(sessionOsiris);
        }

        return sessionOsiris;
    }

    /**
     * setter pour l'attribut id section principale.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSectionPrincipale(String string) {
        idSectionPrincipale = string;
    }

    /**
     * setter pour l'attribut libelle journal.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setLibelleJournal(String string) {
        libelleJournal = string;
    }

    /**
     * setter pour l'attribut montants par admin.
     * 
     * @param map
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantsParAdmin(Map map) {
        montantsParAdmin = map;
    }

    /**
     * setter pour l'attribut sequences par admin.
     * 
     * @param map
     *            une nouvelle valeur pour cet attribut
     */
    public void setSequencesParAdmin(Map map) {
        sequencesParAdmin = map;
    }
}
