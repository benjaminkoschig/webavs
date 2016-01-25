package globaz.aquila.process;

import globaz.aquila.db.irrecouvrables.CODetailSection;
import globaz.aquila.db.irrecouvrables.COPoste;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.contentieux.CAMotifContentieux;
import globaz.osiris.translation.CACodeSystem;
import globaz.osiris.utils.CAUtil;
import globaz.pavo.api.ICIEcriture;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class COProcessValiderIrrecouvrable extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // constantes de phénix
    private final static String CS_INDEPENDANT = "602001";

    private String annee = "";
    private Boolean extournerCI = null;
    private String idCaisseProfessionnelle = "";
    private String idCompteAnnexe = null;
    private String idExterneRole = "";
    private List /* String */<String> idSections = null;

    private String libelleJournal = "";

    private Map /* String -> COPoste */<String, COPoste> postes = null;

    /**
     * Constructor for COProcessValiderIrrecouvrable.
     */
    public COProcessValiderIrrecouvrable() {
        super();
    }

    /**
     * Constructor for COProcessValiderIrrecouvrable.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public COProcessValiderIrrecouvrable(BProcess parent) {
        super(parent);
    }

    /**
     * Constructor for COProcessValiderIrrecouvrable.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public COProcessValiderIrrecouvrable(BSession session) {
        super(session);
    }

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
        BSession caSession = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS)
                .newSession();
        getSession().connectSession(caSession);

        APIGestionComptabiliteExterne compta = (APIGestionComptabiliteExterne) caSession
                .getAPIFor(APIGestionComptabiliteExterne.class);

        compta.setProcess(this);
        compta.setTransaction(getTransaction());
        compta.setSendCompletionMail(true);
        compta.setDateValeur(JACalendar.todayJJsMMsAAAA());
        compta.setEMailAddress(getEMailAddress());
        compta.setLibelle(libelleJournal);

        compta.createJournal();

        // Map qui va permettre pour chaque idSection d'avoir le total des
        // montants irrecouvrables.
        Map<String, FWCurrency> totauxIrrecouvrables = new HashMap<String, FWCurrency>();

        // création de la section d'irrécouvrabilité
        // récupération de l'idExterneRole (on en a besoin pour crééer
        // l'idExterne de la section)
        APICompteAnnexe compteAnnexe = compta.getCompteAnnexeById(idCompteAnnexe);
        setIdExterneRole(compteAnnexe.getIdExterneRole());

        addMotifIrrecCompteAnnexe(caSession, compteAnnexe);

        // création de l'idExterne de la section
        String idExterne = CAUtil.creerNumeroSectionUnique(caSession, getTransaction(), compteAnnexe.getIdRole(),
                idExterneRole, "1", getAnnee(), APISection.ID_CATEGORIE_SECTION_IRRECOUVRABLE);

        APISection sectionIrrecouvrable = compta.createSection(null, idCompteAnnexe,
                APISection.ID_TYPE_SECTION_DECOMPTE_COTISATION, idExterne, null, null, Boolean.FALSE,
                getIdCaisseProfessionnelle());

        // Map qui va servir à garder les montants par année pour l'inscription
        // dans les CI
        SortedMap<String, BigDecimal> montantParAnnee = new TreeMap<String, BigDecimal>();

        // pour avoir le total versé pour les cot. pers.
        BigDecimal montantTotalVerse = new BigDecimal(0);

        Iterator<COPoste> iteratorPostes = postes.values().iterator();

        while (iteratorPostes.hasNext()) {
            COPoste poste = iteratorPostes.next();

            if (!JadeStringUtil.isDecimalEmpty(poste.getMontantIrrecouvrable().toString())) {
                // il n'y a que des rubriques irrecouvrables standards, pas
                // besoin de masse ou de taux
                addEcriture(compta, sectionIrrecouvrable.getIdSection(), poste.getIdRubriqueIrrecouvrable(), poste
                        .getMontantIrrecouvrable().toString(), APIEcriture.CREDIT, APIOperation.CAECRITURE, "");
            }

            // total des montants versés
            // montantTotalVerse = montantTotalVerse.add(poste.getMontantTotalVerse());
            if (poste.isCotisationsPersonnelles()) {
                montantTotalVerse = montantTotalVerse.add(poste.getMontantTotalVerse());
            }

            // Pour chaque poste, on va regarder le montant ventilé et mettre à
            // jour la section correspondante.
            // On va aussi stocker la valeur du montant ventilé pour la section
            // afin de faire la compensation plus tard
            // en une seule fois et pas avec plusieurs écritures.
            // On va aussi faire la somme des montants par année pour les
            // inscriptions aux CI.
            Iterator iteratorDetailSections = poste.getDetailsSections().iterator();
            while (iteratorDetailSections.hasNext()) {
                CODetailSection detailSection = (CODetailSection) iteratorDetailSections.next();

                // ajout du montant ventilé a la map qui stocke le total
                addTotalIrrecouvrable(totauxIrrecouvrables, detailSection);

                // pour inscription CI
                if (poste.isCotisationsPersonnelles()) {
                    montantParAnnee = calculMontantParAnnee(montantParAnnee, detailSection);
                }

            }
            // il faut inscrire aux CI dans le cas de cotisations personnelles
            // un montant négatif correspondant
            // au revenu durant l'année pour lequel les cotisations n'ont pas
            // été payées
        }

        // compensation des sections
        // récupération de la l'id de la rubrique de compensation
        APIReferenceRubrique referenceRubrique = (APIReferenceRubrique) caSession.getAPIFor(APIReferenceRubrique.class);
        String idRubriqueCompensation = "";
        if (referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.COMPENSATION_IRRECOUVRABLE) != null) {
            idRubriqueCompensation = referenceRubrique.getRubriqueByCodeReference(
                    APIReferenceRubrique.COMPENSATION_IRRECOUVRABLE).getIdRubrique();
        } else {
            getTransaction().addErrors(
                    caSession.getLabel("CODE_REFERENCE_NON_ATTRIBUE") + " "
                            + APIReferenceRubrique.COMPENSATION_IRRECOUVRABLE);
        }

        Iterator<String> iteratorIdsSections = totauxIrrecouvrables.keySet().iterator();
        while (iteratorIdsSections.hasNext()) {
            String idSection = iteratorIdsSections.next();
            CASection section = new CASection();
            section.setSession(caSession);
            section.setIdSection(idSection);
            section.retrieve(getTransaction());

            String solde = section.getSolde();

            addMotifIrrecSection(caSession, section);

            if (!JadeStringUtil.isBlankOrZero(solde)) {
                addEcrituresCompensation(compta, section, sectionIrrecouvrable, idRubriqueCompensation, solde);
            }
        }

        // inscription aux CI
        // this.inscriptionAuxCI(compteAnnexe, montantParAnnee, montantTotalVerse);

        if (getTransaction().hasErrors()) {
            setSendMailOnError(true);
            getTransaction().rollback();
        } else {
            compta.comptabiliser();
            getTransaction().commit();
        }

        return false;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        super._validate();
    }

    /**
     * @param compta
     * @param idSection
     * @param idRubrique
     * @param solde
     * @param codeDebitCredit
     * @param typeOperation
     * @return
     */
    private APIEcriture addEcriture(APIGestionComptabiliteExterne compta, String idSection, String idRubrique,
            String solde, String codeDebitCredit, String typeOperation, String libelle) {
        APIEcriture ecriture = compta.createEcriture();
        ecriture.setIdCompteAnnexe(idCompteAnnexe);
        ecriture.setIdSection(idSection);
        ecriture.setDate(JACalendar.todayJJsMMsAAAA());
        ecriture.setIdCompte(idRubrique);
        ecriture.setMontant(solde);
        ecriture.setCodeDebitCredit(codeDebitCredit);
        ecriture.setIdTypeOperation(typeOperation);
        ecriture.setLibelle(libelle);
        compta.addOperation(ecriture);
        return ecriture;
    }

    /**
     * Ajout d'une écriture d'extourne dans les CI
     * 
     * @param sessionPavo
     * @param noAffilie
     * @param noAVS
     * @param annee
     * @param montantAInscrireCI
     * @param gre
     * @throws Exception
     */
    private void addEcritureCI(BSession sessionPavo, String noAffilie, String noAVS, String annee,
            BigDecimal montantAInscrireCI, String gre) throws Exception {
        ICIEcriture ecriture = (ICIEcriture) sessionPavo.getAPIFor(ICIEcriture.class);
        ecriture.setGre(gre);
        ecriture.setIdTypeCompte(ICIEcriture.CS_CI);
        ecriture.setMoisDebut("99");
        ecriture.setMoisFin("99");
        ecriture.setAnnee(annee);
        ecriture.setAvs(noAVS);
        ecriture.setMontant(String.valueOf(montantAInscrireCI.longValue()));
        ecriture.setCode(ICIEcriture.CS_CODE_AMORTISSEMENT);
        ecriture.setEmployeurPartenaire(noAffilie);

        ecriture.add(getTransaction());
    }

    /**
     * Ajoutes les écritures de compensation pour le section et pour la section d'irrécouvrable.
     * 
     * @param compta
     * @param section
     * @param sectionIrrecouvrable
     * @param idRubriqueCompensation
     * @param solde
     */
    private void addEcrituresCompensation(APIGestionComptabiliteExterne compta, CASection section,
            APISection sectionIrrecouvrable, String idRubriqueCompensation, String solde) {
        String libelleIrr = " -> " + section.getIdExterne();
        String libelle = " -> " + sectionIrrecouvrable.getIdExterne();

        // mise a 0 de la section
        addEcriture(compta, section.getIdSection(), idRubriqueCompensation, solde, APIEcriture.CREDIT,
                APIOperation.CAECRITURECOMPENSATION, libelle);
        // on balance la même chose sur la section irrecouvrable
        addEcriture(compta, sectionIrrecouvrable.getIdSection(), idRubriqueCompensation, solde, APIEcriture.DEBIT,
                APIOperation.CAECRITURECOMPENSATION, libelleIrr);
    }

    /**
     * Ajoute le motif de blocage du contentieus d'irrecouvrable au compte annexe.
     * 
     * @param caSession
     * @param compteAnnexe
     * @throws Exception
     */
    private void addMotifIrrecCompteAnnexe(BSession caSession, APICompteAnnexe compteAnnexe) throws Exception {
        if (!compteAnnexe.getContEstBloque().booleanValue()) {
            CACompteAnnexe compte = new CACompteAnnexe();
            compte.setSession(caSession);
            compte.setIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
            compte.retrieve(getTransaction());

            if (!compte.isNew() && !getTransaction().hasErrors()) {
                compte.setContEstBloque(Boolean.TRUE);
                compte.update(getTransaction());
            }
        }

        if (!compteAnnexe.hasMotifContentieuxForYear(CACodeSystem.CS_IRRECOUVRABLE,
                String.valueOf(JACalendar.today().getYear()))) {
            CAMotifContentieux motif = new CAMotifContentieux();
            motif.setSession(caSession);
            motif.setIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
            motif.setIdMotifBlocage(CACodeSystem.CS_IRRECOUVRABLE);
            motif.setDateDebut(JACalendar.todayJJsMMsAAAA());
            motif.setDateFin("31.12.2999");
            motif.add(getTransaction());
        }
    }

    /**
     * Ajoute le motif de blocage du contentieus d'irrecouvrable à la section.
     * 
     * @param caSession
     * @param section
     * @throws Exception
     */
    private void addMotifIrrecSection(BSession caSession, CASection section) throws Exception {
        if (!section.getContentieuxEstSuspendu().booleanValue()) {
            section.setContentieuxEstSuspendu(Boolean.TRUE);
            section.update(getTransaction());
        }

        if (!section.hasMotifContentieuxForYear(CACodeSystem.CS_IRRECOUVRABLE,
                String.valueOf(JACalendar.today().getYear()))) {
            CAMotifContentieux motif = new CAMotifContentieux();
            motif.setSession(caSession);
            motif.setIdSection(section.getIdSection());
            motif.setIdMotifBlocage(CACodeSystem.CS_IRRECOUVRABLE);
            motif.setDateDebut(JACalendar.todayJJsMMsAAAA());
            motif.setDateFin("31.12.2999");
            motif.add(getTransaction());
        }
    }

    /**
     * @param totauxIrrecouvrables
     * @param detailSection
     */
    private void addTotalIrrecouvrable(Map<String, FWCurrency> totauxIrrecouvrables, CODetailSection detailSection) {
        FWCurrency montantACompenser = totauxIrrecouvrables.get(detailSection.getIdSection());

        if (montantACompenser == null) {
            totauxIrrecouvrables.put(detailSection.getIdSection(), new FWCurrency(detailSection
                    .getMontantIrrecouvrable().toString()));
        } else {
            montantACompenser.add(new FWCurrency(detailSection.getMontantIrrecouvrable().toString()));
        }
    }

    /**
     * @param montantParAnnee
     * @param detailSection
     */
    private SortedMap<String, BigDecimal> calculMontantParAnnee(SortedMap<String, BigDecimal> montantParAnnee,
            CODetailSection detailSection) {
        Iterator iteratorMontantParAnneeDetailSection = detailSection.getMontantParAnnee().keySet().iterator();

        while (iteratorMontantParAnneeDetailSection.hasNext()) {
            String annee = (String) iteratorMontantParAnneeDetailSection.next();
            BigDecimal montant = detailSection.getMontantParAnnee().get(annee);

            if (montantParAnnee.containsKey(annee)) {
                montantParAnnee.put(annee, (montantParAnnee.get(annee)).add(montant));
            } else {
                montantParAnnee.put(annee, montant);
            }
        }
        return montantParAnnee;
    }

    /**
     * @return the annee
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        String msg = getSession().getLabel("AQUILA_MAIL_IRREC");
        if (!JadeStringUtil.isBlankOrZero(getIdExterneRole())) {
            msg = msg + " - " + getIdExterneRole();
        }
        return msg;
    }

    public Boolean getExtournerCI() {
        return extournerCI;
    }

    /**
     * @return the idCaisseProfessionnelle
     */
    public String getIdCaisseProfessionnelle() {
        return idCaisseProfessionnelle;
    }

    /**
     * getter pour l'attribut id compte annexe
     * 
     * @return la valeur courante de l'attribut id compte annexe
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdExterneRole() {
        return idExterneRole;
    }

    /**
     * getter pour l'attribut id sections
     * 
     * @return la valeur courante de l'attribut id sections
     */
    public List<String> getIdSections() {
        return idSections;
    }

    /**
     * getter pour l'attribut libelle journal
     * 
     * @return la valeur courante de l'attribut libelle journal
     */
    public String getLibelleJournal() {
        return libelleJournal;
    }

    /**
     * @param idTiers
     * @param numAvsActuel
     * @param noAVS
     * @param annee
     * @return le numéro AVS correspondant à l'année indiquée ou numAvsActuel
     * @throws Exception
     */
    private String getNumAVS(String idTiers, String numAvsActuel, String annee) throws Exception {
        String noAVS = "";
        if (BSessionUtil.compareDateFirstLower(getSession(), "01.01" + annee, getSession().getApplication()
                .getProperty("nnss.dateProduction"))) {
            TIHistoriqueAvs hist = new TIHistoriqueAvs();
            hist.setSession(getSession());
            try {
                noAVS = hist.findPrevKnownNumAvs(idTiers, "31.12." + annee);
                if (JadeStringUtil.isEmpty(noAVS)) {
                    noAVS = hist.findNextKnownNumAvs(idTiers, "01.01." + annee);
                }
            } catch (Exception e) {
                noAVS = "";
            }
        }
        // Si aucun n° trouvé dans historique ou NNSS => prendre l'actuel n° avs
        if (JadeStringUtil.isEmpty(noAVS)) {
            noAVS = numAvsActuel;
        }
        return noAVS;
    }

    /**
     * getter pour l'attribut postes
     * 
     * @return la valeur courante de l'attribut postes
     */
    public Map<String, COPoste> getPostes() {
        return postes;
    }

    /**
     * Inscription aux CI des écritures d'extournes.<br />
     * Il faut inscrire aux CI dans le cas de cotisations personnelles un montant négatif correspondant <br />
     * au revenu durant l'année pour lequel les cotisations n'ont pas été payées.
     * 
     * @param compteAnnexe
     * @param montantParAnnee
     * @param montantTotalVerse
     * @throws RemoteException
     * @throws Exception
     * @throws FWSecurityLoginException
     */
    // private void inscriptionAuxCI(APICompteAnnexe compteAnnexe, SortedMap<String, BigDecimal> montantParAnnee,
    // BigDecimal montantTotalVerse) throws RemoteException, Exception, FWSecurityLoginException {
    // // recup de l'idTiers et du no affilié
    // String idTiers = compteAnnexe.getIdTiers();
    // String noAffilie = compteAnnexe.getIdExterneRole();
    // String numAvsActuel = compteAnnexe.getTiers().getNumAvsActuel();
    // compteAnnexe = null;
    //
    // // on doit inscrire aux CI par année, en commençant par la plus
    // // ancienne.
    // Iterator<String> iteratorMontantParAnnee = montantParAnnee.keySet().iterator();
    //
    // // session PAVO
    // BSession sessionPavo = (BSession) GlobazSystem.getApplication(COApplication.DEFAULT_APPLICATION_PAVO)
    // .newSession();
    // this.getSession().connectSession(sessionPavo);
    //
    // String noAVS = "";
    // while (iteratorMontantParAnnee.hasNext()) {
    // String annee = iteratorMontantParAnnee.next();
    // // récuperation du numero AVS
    // noAVS = this.getNumAVS(idTiers, numAvsActuel, annee);
    // String[][] revenus = CPToolBox.getRevenuPourInscriptionIrrec(this.getSession(), annee, idTiers);
    //
    // if (revenus.length != 1) {
    // this.getMemoryLog().logMessage(this.getSession().getLabel("AQUILA_IMPOSSIBLE_CI") + annee,
    // FWViewBeanInterface.WARNING, this.getClass().getName());
    // } else {
    // BigDecimal revenuCI = new BigDecimal(JANumberFormatter.deQuote(revenus[0][0]));
    // BigDecimal cotisationsDues = new BigDecimal(JANumberFormatter.deQuote(revenus[0][1]));
    // BigDecimal montantExtourneCI = null;
    //
    // if (JadeStringUtil.isBlankOrZero(revenuCI.toString())
    // || JadeStringUtil.isBlankOrZero(cotisationsDues.toString())) {
    // this.getMemoryLog().logMessage(this.getSession().getLabel("AQUILA_REVENUCI_OU_COTI_ABSENT"),
    // FWViewBeanInterface.WARNING, this.getClass().getName());
    // } else {
    // if (montantTotalVerse.compareTo(cotisationsDues) < 0) { // Si
    // // montantTotalVerse
    // // <
    // // cotisationsDues
    // // Ce montant correspond au revenu pour lequel les
    // // cotisations n'ont pas pu être payées
    // BigDecimal montantIrrec = (montantParAnnee.get(annee)).subtract(montantTotalVerse);
    //
    // if ((montantIrrec.signum() == 1) && (montantIrrec.compareTo(cotisationsDues) <= 0)) {
    // // Calcul montant à retirer au CI = -(revenu *
    // // montantIrrec / cotiDues)
    // // montantAInscrireCI =
    // // JANumberFormatter.round(revenu.multiply(montantIrrec).divide(cotisationsDues,
    // // 2, BigDecimal.ROUND_HALF_EVEN).negate(), 0.05, 2,
    // // JANumberFormatter.NEAR);
    // montantExtourneCI = JANumberFormatter.round(
    // revenuCI.multiply(montantIrrec)
    // .divide(cotisationsDues, 2, BigDecimal.ROUND_HALF_EVEN).negate(), 0.05, 2,
    // JANumberFormatter.NEAR);
    // } else {
    // this.getMemoryLog().logMessage(
    // this.getSession().getLabel("AQUILA_MNT_IRREC_FAUX") + "\n[annee=" + annee
    // + " ; montantIrrec=" + montantIrrec + " ; cotisationsDues="
    // + cotisationsDues + "]", FWViewBeanInterface.WARNING,
    // this.getClass().getName());
    // }
    // }
    // if (montantExtourneCI != null) {
    // // code groupe ecriture
    // String gre = "";
    // if (revenus[0][2].equals(COProcessValiderIrrecouvrable.CS_INDEPENDANT)) {
    // gre = "13";
    // } else {
    // gre = "14";
    // }
    //
    // // Bug 5456 ajouter une case à cocher sur l'écran du traitement des irrécouvrables
    // // afin de laisser le choix d'effectuer ou non l'extourne au CI.
    // // Indiquer le montant extourné au CI dans le mail
    // this.getMemoryLog().logMessage(
    // "(" + annee + ") " + this.getSession().getLabel("AQUILA_MONTANT_EXTOURNE_CI")
    // + montantExtourneCI.toString(), FWMessage.INFORMATION,
    // this.getClass().getName());
    // if (this.isExtournerCI()) {
    // this.getMemoryLog().logMessage(
    // this.getSession().getLabel("AQUILA_MSG_MONTANT_CI_EXTOUNRE"),
    // FWMessage.INFORMATION, this.getClass().getName());
    // } else {
    // this.getMemoryLog().logMessage(
    // this.getSession().getLabel("AQUILA_MSG_MONTANT_CI_PAS_EXTOUNRE"),
    // FWMessage.INFORMATION, this.getClass().getName());
    // }
    //
    // if (this.isExtournerCI()) {
    // this.addEcritureCI(sessionPavo, noAffilie, noAVS, annee, montantExtourneCI, gre);
    // }
    // }
    // // mise à niveau du montant montantTotalATraiter pour les
    // // prochaines itérations
    // montantTotalVerse = montantTotalVerse.add(cotisationsDues.negate());
    // // Bug 7644
    // if (montantTotalVerse.signum() == -1) {
    // montantTotalVerse = new BigDecimal(0);
    // }
    // }
    // }
    // }
    // }

    /**
     * @return the extournerCI
     */
    public Boolean isExtournerCI() {
        return extournerCI;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * @param annee
     *            the annee to set
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /**
     * @param extournerCI
     *            the extournerCI to set
     */
    public void setExtournerCI(Boolean extournerCI) {
        this.extournerCI = extournerCI;
    }

    /**
     * @param idCaisseProfessionnelle
     *            the idCaisseProfessionnelle to set
     */
    public void setIdCaisseProfessionnelle(String idCaisseProfessionnelle) {
        this.idCaisseProfessionnelle = idCaisseProfessionnelle;
    }

    /**
     * Sets the idCompteAnnexe.
     * 
     * @param idCompteAnnexe
     *            The idCompteAnnexe to set
     */
    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdExterneRole(String idExterneRole) {
        this.idExterneRole = idExterneRole;
    }

    /**
     * Sets the idSections.
     * 
     * @param idSections
     *            The idSections to set
     */
    public void setIdSections(List<String> idSections) {
        this.idSections = idSections;
    }

    /**
     * setter pour l'attribut libelle journal
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setLibelleJournal(String string) {
        libelleJournal = string;
    }

    /**
     * Sets the postes.
     * 
     * @param postes
     *            The postes to set
     */
    public void setPostes(Map<String, COPoste> postes) {
        this.postes = postes;
    }

}
