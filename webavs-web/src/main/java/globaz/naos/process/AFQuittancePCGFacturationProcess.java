/*
 * Créé le 28 avr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.ServicesFacturation;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.beneficiairepc.AFJournalQuittance;
import globaz.naos.db.beneficiairepc.AFQuittance;
import globaz.naos.db.beneficiairepc.AFQuittanceManager;
import globaz.naos.db.beneficiairepc.AFQuittanceViewBean;
import globaz.naos.db.processFacturation.AFProcessFacturationManager;
import globaz.naos.db.processFacturation.AFProcessFacturationViewBean;
import globaz.naos.db.releve.AFApercuReleveLineFacturation;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.utils.CAUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sda Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFQuittancePCGFacturationProcess extends AFQuittancePCGProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public class MassesDec {
        public String masseAC;
        public String masseAVS;
    }

    private List<AFApercuReleveLineFacturation> cotisationList = new ArrayList<AFApercuReleveLineFacturation>();
    private String idPassageFacturation = "";
    private BSession sessionMusca = null;
    private BSession sessionOsiris = null;

    private String taux = "";

    private Boolean wantSuppressionEtatGenere = Boolean.FALSE;

    public AFQuittancePCGFacturationProcess() {
        super();
    }

    /**
     * @param parent
     */
    public AFQuittancePCGFacturationProcess(BProcess parent) {
        super(parent);
    }

    /**
     * @param session
     */
    public AFQuittancePCGFacturationProcess(BSession session) {
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            AFJournalQuittance journalQuittance = new AFJournalQuittance();
            journalQuittance.setSession(getSession());
            journalQuittance.setIdJournalQuittance(getIdJournalQuittances());
            journalQuittance.retrieve();
            setAnneeJournal(Integer.valueOf(journalQuittance.getAnnee()).intValue());

            FAPassage passage = null;
            double totalControle = 0.0;
            // Création de la transaction
            passage = retrievePassageFacturation(getIdPassageFacturation(), getTransaction());
            if (wantSuppressionEtatGenere) {
                // Supression de l'éventuelle facturation déjà générée
                deleteFacturationDuPassage(getTransaction());
            }
            // On groupe par bénéficiaire
            AFQuittanceManager manager = initManagerQuittanceATraiter();
            setProgressScaleValue(manager.getSize());
            for (int i = 0; i < manager.getSize(); i++) {
                incProgressCounter();
                totalControle = 0.0;
                setAffiliationLue(null);
                AFQuittance quittance = (AFQuittance) manager.getEntity(i);
                // On va rechercher toutes les quittances pour chaque bénéficiaire et par année
                AFQuittanceManager quittanceManager = rechercheQuittancesBeneficiaire(wantSuppressionEtatGenere,
                        quittance, getTransaction(), false);
                if (checkQuittancesBeneficiaire(getTransaction(), quittanceManager) == true) {
                    // On va grouper les quittances par aide de ménage et par période continue
                    List<AFQuittance> listQuittance = grouperQuittanceParBeneficiaireSiContinuite(getCi(),
                            quittanceManager, getTransaction(), false);
                    if ((getTransaction().hasErrors() == false) && (listQuittance != null)) {
                        for (int j = 0; j < listQuittance.size(); j++) {
                            AFQuittance quittanceGroupe = listQuittance.get(j);
                            // Détermination du montant CI (utilisé aussi pour la facturation)
                            determinerMontantCI(quittanceGroupe, getTransaction(), getTaux());
                            // Calcul montant effectif et si l'affilié est soumis à l'avs
                            quittanceGroupe = determinerQuittanceAvecMontantEffectif(getCi(), quittanceGroupe,
                                    getTransaction());
                            if (getTransaction().hasErrors() == false) {
                                if (Double.valueOf(quittanceGroupe.getMontantEffectif()).doubleValue() > 0.0) {
                                    totalControle += Double.valueOf(quittanceGroupe.getMontantEffectif()).doubleValue();
                                    // FACTU
                                    creationLignesFactu(quittanceGroupe, passage, getTransaction());
                                }
                            }
                        }
                    }
                }
                if (getTransaction().hasErrors() || (totalControle <= 0.0)) {
                    String messageErreur = getTransaction().getErrors().toString();
                    if (JadeStringUtil.isEmpty(messageErreur)) {
                        if (totalControle <= 0.0) {
                            messageErreur += "\n" + getSession().getLabel("MONTANT_INF_FRANCHISE");
                        }
                    }
                    if (messageErreur.length() > 254) {
                        messageErreur = messageErreur.substring(0, 254);
                    }
                    getTransaction().clearErrorBuffer();
                    getTransaction().rollback();
                    // Quittancer les erreurs
                    quittancerEtat(getTransaction(), quittance, messageErreur, false, "");
                } else {
                    quittancerEtat(getTransaction(), quittance, "", false, getTaux());
                }
                getTransaction().commit();
                // }
            }
            updateEtatJournalQuittance(getTransaction(), journalQuittance);
        } catch (Exception e) {
            getTransaction().rollback();
        } finally {
            // On imprime les erreurs
            imprimerErreurs(AFQuittanceViewBean.ETAT_ERREUR_FACTU);
        }
        return true;
    }

    public void addCotisation(AFApercuReleveLineFacturation newCotisation) {
        // si la cotisation se trouve déjà dans la liste (même assurance, même
        // plan), il s'agit d'un historique
        // il ne faut pas en tenir compte, voire l'adapter avec les bonne valeur

        boolean found = false;
        for (int i = 0; (i < cotisationList.size()) && !found; i++) {
            AFApercuReleveLineFacturation line = cotisationList.get(i);
            if (line.getAssuranceId().equals(newCotisation.getAssuranceId())
                    && line.getIdPlan().equals(newCotisation.getIdPlan())) {
                found = true;
            }
        }
        if (!found) {
            cotisationList.add(newCotisation);
        }
    }

    private void creationLignesFactu(AFQuittance quittance, FAPassage passage, BTransaction transaction)
            throws Exception {

        AFProcessFacturationManager manager = new AFProcessFacturationManager();
        manager.setSession(getSession());
        manager.setFromDate(quittance.getPeriodeDebut());
        manager.setToDate(quittance.getPeriodeFin());
        manager.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
        manager.setForAffilieNumero(getAffiliationLue().getAffilieNumero());
        manager.setNotInCodeFacturation(CodeSystem.CODE_FACTU_MONTANT_LIBRE);
        // manager.setForPlanAffiliationId();
        manager.find(transaction);
        for (int i = 0; i < manager.size(); i++) {
            AFProcessFacturationViewBean donnees = (AFProcessFacturationViewBean) manager.getEntity(i);
            // Vu les montants faibles il n'y aura jamais d'AC2 - Inutile de checker par rapport au plafond
            if (!CodeSystem.TYPE_ASS_COTISATION_AC2.equals(donnees.getAssurance().getTypeAssurance())) {
                FAEnteteFacture entete = getEnteteFacture(getAffiliationLue().getAffilieNumero(), passage, transaction);
                if (entete != null) {
                    genererAfact(entete, donnees, quittance, transaction);
                } else {
                    genererAfact(nouvelleEntete(passage.getIdPassage(), quittance, transaction), donnees, quittance,
                            transaction);
                }
            }
        }
    }

    protected void deleteFacturationDuPassage(BTransaction transaction) throws Exception {
        FAAfactManager afacMng = new FAAfactManager();
        afacMng.setSession(getSession());
        afacMng.setForIdPassage(getIdPassageFacturation());
        afacMng.setWantOnlyFileAFact(Boolean.TRUE);
        afacMng.delete(transaction);
        FAEnteteFactureManager entfMng = new FAEnteteFactureManager();
        entfMng.setSession(getSession());
        entfMng.setForIdPassage(getIdPassageFacturation());
        entfMng.setWantOnlyFileEntete(Boolean.TRUE);
        entfMng.delete(transaction);
        // Commit de la suppression. Ne peut être fait après car il est possible que la première
        // quittance soit en erreur et dans ce cas il faut quand même supprimer les anciennes factures.
        if (transaction.hasErrors() == false) {
            transaction.commit();
        }

    }

    private void genererAfact(FAEnteteFacture entete, AFProcessFacturationViewBean donnees, AFQuittance quittance,
            BTransaction transaction) throws Exception {
        if (!transaction.hasErrors()) {
            try {

                FAAfact lineFacture = new FAAfact();
                lineFacture.setSession(getSessionMusca());
                lineFacture.setIdEnteteFacture(entete.getIdEntete());
                lineFacture.setIdPassage(entete.getIdPassage());

                // IdModuleFacturation
                String idModuleFacturation = ServicesFacturation.getIdModFacturationByType(getSession(), transaction,
                        FAModuleFacturation.CS_MODULE_BENEFICIAIRE_PC);
                if (!JadeStringUtil.isEmpty(idModuleFacturation)) {
                    lineFacture.setIdModuleFacturation(idModuleFacturation);
                    lineFacture.setIdTypeAfact(FAAfact.CS_AFACT_TABLEAU);
                    lineFacture.setNonImprimable(Boolean.FALSE);
                    lineFacture.setNonComptabilisable(Boolean.FALSE);
                    lineFacture.setAQuittancer(Boolean.FALSE);
                    lineFacture.setAnneeCotisation(quittance.getAnnee());
                    lineFacture.setIdRubrique(donnees.getAssuranceRubriqueId());
                    lineFacture.setDebutPeriode(quittance.getPeriodeDebut());
                    lineFacture.setFinPeriode(quittance.getPeriodeFin());
                    lineFacture.setMasseFacture(Double.toString(JANumberFormatter.round(
                            Double.valueOf(quittance.getMontantEffectif()).doubleValue(), 0.05, 2,
                            JANumberFormatter.NEAR)));
                    lineFacture.setMasseInitiale(Double.toString(JANumberFormatter.round(
                            Double.valueOf(quittance.getMontantEffectif()).doubleValue(), 0.05, 2,
                            JANumberFormatter.NEAR)));
                    // On recherche l'assurance
                    AFAssurance assurance = new AFAssurance();
                    assurance.setSession(getSession());
                    assurance.setAssuranceId(donnees.getAssuranceId());
                    assurance.retrieve(transaction);

                    AFTauxAssurance taux = assurance.getTaux(lineFacture.getDebutPeriode());
                    if (taux == null) {
                        assurance.getTaux(lineFacture.getFinPeriode());
                    }
                    if ("100".equals(taux.getFraction())) {
                        lineFacture.setTauxFacture(taux.getTauxSansFraction());
                        lineFacture.setTauxInitial(taux.getTauxSansFraction());
                    } else {
                        transaction
                                .addErrors(("Taux de " + taux.getFraction() + " non géré pour l'affilié : " + quittance
                                        .getNumAffilieBeneficiaire()));
                    }
                    lineFacture.add(transaction);
                } else {
                    transaction.addErrors("id module facturation non trouvé");
                }
            } catch (Exception e) {
                transaction.addErrors("générer afact : " + e.getMessage());
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return "facturation des quittances PCG";
    }

    private FAEnteteFacture getEnteteFacture(String numAffilie, FAPassage passage, BTransaction transaction)
            throws Exception {
        FAEnteteFactureManager entFactureManager = new FAEnteteFactureManager();
        entFactureManager.setSession(getSession());
        entFactureManager.setForIdExterneRole(numAffilie);
        entFactureManager.setForIdSousType(APISection.ID_CATEGORIE_SECTION_DECOMPTE_PCG);
        entFactureManager.setForIdPassage(passage.getIdPassage());
        entFactureManager.find(transaction);
        if (entFactureManager.size() > 0) {
            return (FAEnteteFacture) entFactureManager.getFirstEntity();

        }
        return null;
    }

    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    private BSession getSessionMusca() {
        if (sessionMusca == null) {
            try {
                sessionMusca = new BSession(FAApplication.DEFAULT_APPLICATION_MUSCA);
                getSession().connectSession(sessionMusca);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return sessionMusca;
    }

    private BSession getSessionOsiris() {
        if (sessionOsiris == null) {
            try {
                sessionOsiris = new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS);
                getSession().connectSession(sessionOsiris);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return sessionOsiris;
    }

    public String getTaux() {
        return taux;
    }

    public Boolean getWantSuppressionEtatGenere() {
        return wantSuppressionEtatGenere;
    }

    protected AFQuittanceManager initManagerQuittanceATraiter() throws Exception {
        AFQuittanceManager manager = new AFQuittanceManager();
        manager.setSession(getSession());
        manager.setSelectionBeneficiairePC(true);

        manager.setForIdJournalQuittance(getIdJournalQuittances());
        if (wantSuppressionEtatGenere) {
            // Suppression des afacts générés
            manager.setInEtat(AFQuittanceViewBean.ETAT_ERREUR_FACTU + ", " + AFQuittanceViewBean.ETAT_OUVERT + ", "
                    + AFQuittanceViewBean.ETAT_FACTURE);
        } else {
            manager.setInEtat(AFQuittanceViewBean.ETAT_ERREUR_FACTU + ", " + AFQuittanceViewBean.ETAT_OUVERT);
        }
        manager.changeManagerSize(BManager.SIZE_NOLIMIT);
        manager.find(getTransaction());
        return manager;
    }

    private FAEnteteFacture nouvelleEntete(String idPassage, AFQuittance quittance, BTransaction transaction)
            throws Exception {
        try {
            String role = CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(getSession().getApplication());

            FAEnteteFacture nouveauEntFacture = new FAEnteteFacture();
            nouveauEntFacture.setSession(getSession());
            nouveauEntFacture.setIdPassage(idPassage);
            nouveauEntFacture.setIdTiers(quittance.getIdTiers());
            nouveauEntFacture.setIdExterneRole(getAffiliationLue().getAffilieNumero());
            nouveauEntFacture.setIdSousType(APISection.ID_CATEGORIE_SECTION_DECOMPTE_PCG);
            nouveauEntFacture.setDateReceptionDS(quittance.getPeriodeDebut());
            nouveauEntFacture.setIdRole(role);
            // Recherche du n° de facture
            String numFacture = "";
            numFacture = CAUtil.creerNumeroSectionUnique(getSessionOsiris(), transaction, role,
                    quittance.getNumAffilieBeneficiaire(), nouveauEntFacture.getIdTypeFacture(), quittance.getAnnee(),
                    nouveauEntFacture.getIdSousType());

            nouveauEntFacture.setIdExterneFacture(numFacture);

            nouveauEntFacture.initDefaultPlanValue(role);
            nouveauEntFacture.add(transaction);
            return nouveauEntFacture;
        } catch (Exception e) {
            transaction.addErrors("nouvelle entête : " + e.getMessage());
            return null;
        }
    }

    private FAPassage retrievePassageFacturation(String idPassage, BTransaction transaction) {
        // ouvrir un nouveau passage
        FAPassage myPassage = new FAPassage();
        myPassage.setSession(getSession());
        myPassage.setIdPassage(idPassage);
        try {
            myPassage.retrieve(transaction);
        } catch (Exception e) {
            transaction.addErrors("recherche passage facturation : " + e.getMessage());
            return null;
        }
        return myPassage;
    }

    public void setIdPassageFacturation(String idPassageFacturation) {
        this.idPassageFacturation = idPassageFacturation;
    }

    public void setTaux(String taux) {
        this.taux = taux;
    }

    public void setWantSuppressionEtatGenere(Boolean wantSuppressionEtatGenere) {
        this.wantSuppressionEtatGenere = wantSuppressionEtatGenere;
    }
}
