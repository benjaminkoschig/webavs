package globaz.aquila.process;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
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
import globaz.osiris.db.comptes.CACompteur;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.db.irrecouvrable.CARecouvrementCi;
import globaz.osiris.db.irrecouvrable.CARecouvrementKeyPosteContainer;
import globaz.osiris.db.irrecouvrable.CARecouvrementPoste;
import globaz.osiris.exceptions.CATechnicalException;
import globaz.osiris.utils.CAUtil;
import globaz.pavo.api.ICIEcriture;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.db.inscriptions.CIJournalManager;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.toolbox.CPToolBox;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Processus permettant de comptabiliser les montants de recouvrement
 * 
 * @author sch
 */
public class COProcessComptabiliserRecouvrement extends BProcess {
    private static final long serialVersionUID = -4505422917293499450L;
    int anneeLimite = 0;
    private String idCompteAnnexe;
    private String idCompteIndividuelAffilie;
    private String libelleMail;
    private BigDecimal montantARecouvrir;
    private Map<Integer, CARecouvrementCi> recouvrementCiMap;
    private Map<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPostesMap;
    private boolean wantTraiterRecouvrementCi;
    private List<String> idSectionsList;

    private static CACompteAnnexe retrieveCompteAnnexe(String idCompteAnnexe, BSession session) throws Exception {
        if (JadeStringUtil.isBlank(idCompteAnnexe)) {
            throw new IllegalArgumentException("unable to load compteAnnexe, the idCompteAnnexe is blank");
        }

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(session);
        compteAnnexe.setIdCompteAnnexe(idCompteAnnexe);
        compteAnnexe.retrieve();

        return compteAnnexe;
    }

    private static CICompteIndividuel retrieveCompteIndividuel(String compteIndividuelId, BSession sessionPavo)
            throws Exception {
        if (JadeStringUtil.isBlank(compteIndividuelId)) {
            throw new IllegalArgumentException("unable to load compteIndividuel, the compteIndividuelId is blank");
        }

        CICompteIndividuel compteIndividuel = new CICompteIndividuel();
        compteIndividuel.setSession(sessionPavo);
        compteIndividuel.setCompteIndividuelId(compteIndividuelId);
        compteIndividuel.retrieve();

        return compteIndividuel;
    }

    private static CASection retrieveSection(String idSection, BSession session) throws Exception {
        if (JadeStringUtil.isBlank(idSection)) {
            throw new IllegalArgumentException("unable to loadSection, the idSection is blank");
        }

        CASection section = new CASection();
        section.setSession(session);
        section.setIdSection(idSection);
        section.retrieve();

        return section;
    }

    public COProcessComptabiliserRecouvrement() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        BSession sessionOsiris = createSessionOsiris(getSession());
        APIGestionComptabiliteExterne gestionComptaExterne = (APIGestionComptabiliteExterne) sessionOsiris
                .getAPIFor(APIGestionComptabiliteExterne.class);

        // chargement du compte annexe
        CACompteAnnexe compteAnnexe = COProcessComptabiliserRecouvrement.retrieveCompteAnnexe(idCompteAnnexe,
                sessionOsiris);
        if (wantTraiterRecouvrementCi) {
            // Recherche de l'année limite d'inscription CI - Paramètre
            if (JadeStringUtil.isIntegerEmpty(Integer.toString(anneeLimite))) {
                anneeLimite = Integer.parseInt(CPToolBox.anneeLimite(getTransaction()));
            }
            if (!checkJournalAnneeLimiteExistant(anneeLimite)) {
                throw new Exception("Le journal CI de type cotisation personnelle n'existe pas pour l'année limite");
            }
        }

        // parcours de tous les postes pour la mise à jour des compteurs
        actualiserCompteurs(getTransaction());

        // création du journal de comptabilité
        List<Integer> anneeATraiterList = determinerAnneeATraiter();
        libelleMail = creerLibelle(compteAnnexe, anneeATraiterList);
        String libelleJournal;
        if (libelleMail.length() > 40) {
            libelleJournal = libelleMail.substring(0, 39);
        } else {
            libelleJournal = new String(libelleMail);
        }
        creerJournalComptabiliteAuxiliaire(gestionComptaExterne, libelleJournal);

        // déterminer l'id de la caisse prof
        String idCaisseProf = determinerIdCaisseProf(sessionOsiris);

        if (JadeStringUtil.isBlankOrZero(idCaisseProf)) {
            String annee = String.valueOf(determinerAnneeMax(anneeATraiterList));
            idCaisseProf = findIdCaisseProfFromSectionAnnee(sessionOsiris, annee);
        }

        // Préparer la section de recouvrement
        CASection sectionRecouvrement = null;
        if (!isIncrementerNumSectionRecouvrement()) {
            sectionRecouvrement = chargerSectionRecouvrement(sessionOsiris, compteAnnexe, anneeATraiterList);
        }
        if (sectionRecouvrement == null) {
            sectionRecouvrement = creerSectionRecouvrement(sessionOsiris, gestionComptaExterne, compteAnnexe,
                    anneeATraiterList, idCaisseProf);
        }

        // parcours de tous les postes
        for (Map.Entry<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPosteEntry : recouvrementPostesMap
                .entrySet()) {
            CARecouvrementPoste recouvrementPoste = recouvrementPosteEntry.getValue();
            BigDecimal montantRecouvrement = recouvrementPoste.getRecouvrement();

            if (montantRecouvrement.signum() != 0) {
                // ajouter l'écriture à la section Recouvrement
                ajouterEcritureDansSection(gestionComptaExterne, sectionRecouvrement.getIdSection(),
                        recouvrementPoste.getIdRubriqueRecouvrement(), montantRecouvrement.toString(),
                        APIEcriture.DEBIT, APIOperation.CAECRITURE, "", recouvrementPoste.getAnnee());
            }
        }

        // compensation des sections
        // récupération de la rubrique de compensation irrécouvrable
        APIReferenceRubrique referenceRubrique = (APIReferenceRubrique) sessionOsiris
                .getAPIFor(APIReferenceRubrique.class);
        String idRubriqueCompensation = "";

        if (referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.COMPENSATION_IRRECOUVRABLE) != null) {
            idRubriqueCompensation = referenceRubrique.getRubriqueByCodeReference(
                    APIReferenceRubrique.COMPENSATION_IRRECOUVRABLE).getIdRubrique();
        } else {
            getTransaction().addErrors(
                    sessionOsiris.getLabel("CODE_REFERENCE_NON_ATTRIBUE") + " "
                            + APIReferenceRubrique.COMPENSATION_IRRECOUVRABLE);
        }

        BigDecimal soldeTotalARecouvrir = montantARecouvrir.abs();
        BigDecimal soldeRestantACompenser = soldeTotalARecouvrir;
        BigDecimal soldeACompenser;

        // Recharge les sections selectionnée par l'utilisateur et les renvois en objet dans une ordre idexterne et
        // date.
        CASectionManager sections = chargeEtTriSections(idSectionsList, sessionOsiris);
        // créer les écritures de compensation
        @SuppressWarnings("unchecked")
        Iterator<CASection> iteratorSections = sections.iterator();

        while (iteratorSections.hasNext()) {
            CASection section = iteratorSections.next();
            // Si le solde restant est égal ou inférieure à 0.-, on sort de la boucle.
            if (soldeRestantACompenser.compareTo(new BigDecimal(0)) != 1) {
                break;
            }

            if (JadeStringUtil.isBlankOrZero(section.getSolde())) {
                // Si le solde de la section est vide, null ou vaut 0, on passe à la section suivante.
                continue;
            }

            // Récupération du solde de la section
            BigDecimal soldeSection = new BigDecimal(section.getSolde()).negate();

            // Si le solde restant est inférieure au solde de la section, il faut déduire le montant du solde
            // restant et non pas de la section.
            if (soldeRestantACompenser.compareTo(soldeSection) == -1) {
                soldeACompenser = soldeRestantACompenser;
            } else {
                soldeACompenser = soldeSection;
            }

            ajouterEcrituresCompensation(gestionComptaExterne, section, sectionRecouvrement, idRubriqueCompensation,
                    soldeACompenser.toString());

            // Montant restant à compenser - le solde de la section que l'on vient de compenser.
            soldeRestantACompenser = soldeRestantACompenser.subtract(soldeACompenser);
        }

        // Traiter recouvrements CI
        if (wantTraiterRecouvrementCi) {
            passerInscriptionsCi();
        }

        // comptabilisation du journal si pas d'erreurs
        if (getTransaction().hasErrors()) {
            setSendMailOnError(true);
            getTransaction().rollback();
            return false;
        } else {
            gestionComptaExterne.comptabiliser();
            getTransaction().commit();
            return true;
        }
    }

    /**
     * @param sessionOsiris
     * @param gestionComptaExterne
     * @param compteAnnexe
     * @param anneeATraiterList
     * @param idCaisseProf
     * @return
     * @throws Exception
     */
    private CASection creerSectionRecouvrement(BSession sessionOsiris,
            APIGestionComptabiliteExterne gestionComptaExterne, CACompteAnnexe compteAnnexe,
            List<Integer> anneeATraiterList, String idCaisseProf) throws Exception {
        CASection sectionRecouvrement;
        Integer annee = determinerAnneeMax(anneeATraiterList);
        sectionRecouvrement = (CASection) creerSectionRecouvrement(sessionOsiris, gestionComptaExterne, compteAnnexe,
                annee, idCaisseProf);
        return sectionRecouvrement;
    }

    /**
     * Cette méthode retourne une section de recouvrement, si section non trouvée, retourne null
     * 
     * @param sessionOsiris
     * @param compteAnnexe
     * @param anneeATraiterList
     * @param idCaisseProf
     * @return CASection, null si section non trouvée
     * @throws Exception
     */
    private CASection chargerSectionRecouvrement(BSession sessionOsiris, CACompteAnnexe compteAnnexe,
            List<Integer> anneeATraiterList) throws Exception {
        if (compteAnnexe == null || anneeATraiterList == null) {
            throw new IllegalArgumentException("Check method parameters");
        }
        CASection sectionRecouvrement = null;
        Integer annee = determinerAnneeMax(anneeATraiterList);
        CASectionManager sectionRecouvrementManager = new CASectionManager();
        sectionRecouvrementManager.setSession(sessionOsiris);
        sectionRecouvrementManager.setForIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
        sectionRecouvrementManager.setForIdTypeSection(APISection.ID_TYPE_SECTION_DECOMPTE_COTISATION);
        sectionRecouvrementManager.setLikeIdExterne(annee.toString() + "25000");
        sectionRecouvrementManager.find(BManager.SIZE_NOLIMIT);
        if (!sectionRecouvrementManager.isEmpty()) {
            sectionRecouvrement = (CASection) sectionRecouvrementManager.getFirstEntity();
        }
        return sectionRecouvrement;

    }

    /**
     * Cette methode permet d'actualiser les compteurs avec les modifications effectuées manuellement. Si les compteurs
     * n'existent pas ils sont créés.
     * Pour ce faire, on parcourt les recouvrementPoste
     * 
     * @throws CATechnicalException
     * @throws Exception
     */
    private void actualiserCompteurs(BTransaction transaction) throws CATechnicalException, Exception {
        for (Map.Entry<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPosteEntry : recouvrementPostesMap
                .entrySet()) {
            CARecouvrementPoste recouvrementPoste = recouvrementPosteEntry.getValue();
            if (recouvrementPoste.getValeurInitialeCotAmortie().compareTo(
                    recouvrementPoste.getCumulCotisationAmortieCorrigee()) != 0) {
                CACompteur compteurAmort = CAUtil.getCompteur(idCompteAnnexe, recouvrementPoste.getAnnee(),
                        recouvrementPoste.getIdRubriqueIrrecouvrable(), getSessionOsiris());
                BigDecimal cumulCotisation = new BigDecimal(0);
                BigDecimal valeurInitialeCot = new BigDecimal(0);
                cumulCotisation = cumulCotisation.add(recouvrementPoste.getCumulCotisationAmortieCorrigee());
                valeurInitialeCot = recouvrementPoste.getValeurInitialeCotAmortie();
                BigDecimal cumulCotisationAmortCorrige = recouvrementPoste.getCumulCotisationAmortieCorrigee()
                        .subtract(recouvrementPoste.getCumulCotisationAmortie());
                valeurInitialeCot = valeurInitialeCot.add(cumulCotisationAmortCorrige);
                if (compteurAmort != null) {
                    // Mise à jour des compteurs d'amortissement
                    updateCompteur(compteurAmort, cumulCotisation.negate().toString(), valeurInitialeCot.negate()
                            .toString(), transaction);

                } else {
                    // Création du compteur d'amortissement
                    creerCompteur(idCompteAnnexe, recouvrementPoste.getIdRubriqueIrrecouvrable(), recouvrementPoste
                            .getAnnee().toString(), cumulCotisation.negate().toString(), valeurInitialeCot.negate()
                            .toString(), transaction);
                }
            }
            if (recouvrementPoste.getValeurInitialeCotRecouvrement().compareTo(
                    recouvrementPoste.getCumulRecouvrementCotisationAmortieCorrigee()) != 0) {
                CACompteur compteurRecouvrement = CAUtil.getCompteur(idCompteAnnexe, recouvrementPoste.getAnnee(),
                        recouvrementPoste.getIdRubriqueRecouvrement(), getSessionOsiris());
                BigDecimal cumulCotisation = new BigDecimal(0);
                BigDecimal valeurInitialeCot = new BigDecimal(0);
                cumulCotisation = cumulCotisation
                        .add(recouvrementPoste.getCumulRecouvrementCotisationAmortieCorrigee());
                valeurInitialeCot = recouvrementPoste.getValeurInitialeCotAmortie();
                BigDecimal cumulRecouvrementCotisationAmortCorrige = recouvrementPoste
                        .getCumulRecouvrementCotisationAmortieCorrigee().subtract(
                                recouvrementPoste.getCumulRecouvrementCotisationAmortie());
                valeurInitialeCot = valeurInitialeCot.add(cumulRecouvrementCotisationAmortCorrige);
                if (compteurRecouvrement != null) {
                    // Mise à jour des compteurs de recouvrement
                    updateCompteur(compteurRecouvrement, cumulCotisation.toString(), valeurInitialeCot.toString(),
                            transaction);

                } else {
                    // Création du compteur de recouvrement
                    creerCompteur(idCompteAnnexe, recouvrementPoste.getIdRubriqueRecouvrement(), recouvrementPoste
                            .getAnnee().toString(), cumulCotisation.toString(), valeurInitialeCot.toString(),
                            transaction);
                }
            }
        }
    }

    /**
     * Ajout d'une écriture d'extourne dans les CI
     * 
     * @param sessionPavo
     * @param noAffilie
     * @param noAVS
     * @param annee
     * @param montantAInscrireCI
     * @param genreEcriture
     * @throws Exception
     */
    private void ajouterEcritureCi(BSession sessionPavo, String noAffilie, String noAVS, String annee,
            BigDecimal montantAInscrireCI, String genreEcriture, String idJournal) throws Exception {
        ICIEcriture ecriture = (ICIEcriture) sessionPavo.getAPIFor(ICIEcriture.class);
        ecriture.setGre(genreEcriture);
        ecriture.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
        ecriture.setMoisDebut("99");
        ecriture.setMoisFin("99");
        ecriture.setAnnee(annee);
        ecriture.setAvs(noAVS);
        ecriture.setIdJournal(idJournal);
        ecriture.setMontant(String.valueOf(montantAInscrireCI.longValue()));
        ecriture.setCode(ICIEcriture.CS_CODE_AMORTISSEMENT);
        ecriture.setEmployeurPartenaire(noAffilie);

        ecriture.add(getTransaction());
    }

    private APIEcriture ajouterEcritureDansSection(final APIGestionComptabiliteExterne gestionComptaExterne,
            final String idSection, final String idRubrique, final String montant, final String codeDebitCredit,
            final String typeOperation, final String libelle, final Integer annee) throws Exception {
        APIEcriture ecriture = gestionComptaExterne.createEcriture();
        ecriture.setIdCompteAnnexe(idCompteAnnexe);
        ecriture.setIdSection(idSection);
        ecriture.setDate(JACalendar.todayJJsMMsAAAA());
        ecriture.setIdCompte(idRubrique);
        ecriture.setMontant(montant);
        ecriture.setCodeDebitCredit(codeDebitCredit);
        ecriture.setIdTypeOperation(typeOperation);
        ecriture.setLibelle(libelle);
        if (CAUtil.isRubriqueAvecCompteur(idRubrique, getSession())) {
            ecriture.setAnneeCotisation(annee.toString());
        }
        gestionComptaExterne.addOperation(ecriture);
        return ecriture;
    }

    private void ajouterEcrituresCompensation(APIGestionComptabiliteExterne compta, CASection section,
            APISection sectionRecouvrement, String idRubriqueCompensation, String montant) throws Exception {
        String libelleRecouv = " -> " + sectionRecouvrement.getIdExterne();
        String libelle = " -> " + section.getIdExterne();

        // mise a 0 de la section
        ajouterEcritureDansSection(compta, section.getIdSection(), idRubriqueCompensation, montant, APIEcriture.DEBIT,
                APIOperation.CAECRITURECOMPENSATION, libelleRecouv, 0);
        // on balance la même chose sur la section Recouvrement
        ajouterEcritureDansSection(compta, sectionRecouvrement.getIdSection(), idRubriqueCompensation, montant,
                APIEcriture.CREDIT, APIOperation.CAECRITURECOMPENSATION, libelle, 0);
    }

    private CIJournal chargerJournalAnneeLimite(int anneeLimite) throws Exception {
        CIJournal journalAnneeLimite = null;
        CIJournalManager journalCIManager = new CIJournalManager();
        journalCIManager.setSession(getSessionPavo());
        journalCIManager.setForAnneeCotisation("" + anneeLimite);
        journalCIManager.setForIdTypeInscription(CIJournal.CS_COTISATIONS_PERSONNELLES);
        journalCIManager.find(getTransaction());
        if (!journalCIManager.isEmpty()) {
            // prendre le premier journal du manager
            journalAnneeLimite = (CIJournal) journalCIManager.getFirstEntity();
        } else {
            throw new Exception("Le journal de type cotisations personnelles n'existe pas pour l'année limite");
        }
        return journalAnneeLimite;
    }

    private boolean checkJournalAnneeLimiteExistant(int anneeLimite) throws Exception {
        CIJournalManager journalCIManager = new CIJournalManager();
        journalCIManager.setSession(getSessionPavo());
        journalCIManager.setForAnneeCotisation("" + anneeLimite);
        journalCIManager.setForIdTypeInscription(CIJournal.CS_COTISATIONS_PERSONNELLES);
        journalCIManager.find(getTransaction());
        if (!journalCIManager.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private BSession createSessionOsiris(BSession session) throws Exception {
        BSession sessionOsiris;
        sessionOsiris = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS).newSession();
        session.connectSession(sessionOsiris);
        return sessionOsiris;
    }

    /**
     * Cette méthode crée un nouveau compteur
     * 
     * @param idCompteAnnexe
     * @param idRubrique
     * @param annee
     * @param cumulCotisation
     * @param valeurInitialCot
     * @param transaction
     * @return CACompteur
     * @throws Exception
     */
    private CACompteur creerCompteur(String idCompteAnnexe, String idRubrique, String annee, String cumulCotisation,
            String valeurInitialeCot, BTransaction transaction) throws Exception {
        CACompteur compteur = new CACompteur();
        compteur.setSession(getSessionOsiris());
        compteur.setIdCompteAnnexe(idCompteAnnexe);
        compteur.setIdRubrique(idRubrique);
        compteur.setAnnee(annee);
        compteur.setCumulCotisation(cumulCotisation);
        compteur.setValeurInitialeCot(valeurInitialeCot);
        compteur.add(transaction);
        return compteur;
    }

    /**
     * Cette méthode permet de mettre à jour un compteur
     * 
     * @param compteur
     * @param cumulCotisation
     * @param valeurInitialCot
     * @param transaction
     * @return CACompteur
     * @throws Exception
     */
    private CACompteur updateCompteur(CACompteur compteur, String cumulCotisation, String valeurInitialeCot,
            BTransaction transaction) throws Exception {
        compteur.setSession(getSessionOsiris());
        compteur.setCumulCotisation(cumulCotisation);
        compteur.setValeurInitialeCot(valeurInitialeCot);
        compteur.update(transaction);
        return compteur;
    }

    private CIJournal creerJournalCI(BSession sessionPavo, String libelle) throws Exception {
        CIJournal journal = new CIJournal();
        journal.setSession(sessionPavo);
        journal.setIdTypeInscription(CIJournal.CS_DECISION_COT_PERS);
        journal.setLibelle(libelle);
        journal.setDate(JACalendar.todayJJsMMsAAAA());
        journal.setProprietaire(getSession().getUserId());
        journal.setIdEtat(CIJournal.CS_OUVERT);
        journal.add();
        return journal;
    }

    private void creerJournalComptabiliteAuxiliaire(APIGestionComptabiliteExterne gestionComptaExterne,
            String libelleJournal) {
        gestionComptaExterne.setProcess(this);
        gestionComptaExterne.setTransaction(getTransaction());
        gestionComptaExterne.setSendCompletionMail(true);
        gestionComptaExterne.setDateValeur(JACalendar.todayJJsMMsAAAA());
        gestionComptaExterne.setEMailAddress(getEMailAddress());
        gestionComptaExterne.setLibelle(libelleJournal);
        gestionComptaExterne.createJournal();
    }

    private String creerLibelle(APICompteAnnexe compteAnnexe, List<Integer> anneeATraiterList) {
        String role = compteAnnexe.getRole().getDescription();
        String numeroAffilie = compteAnnexe.getIdExterneRole();
        String description = compteAnnexe.getDescription();
        String libelle = getSession().getLabel("RECOUVREMENT_PREFIXE_LIBELLE_JOURNAL");
        libelle += " " + role;
        libelle += " " + numeroAffilie;
        libelle += " " + description;
        for (Integer annee : anneeATraiterList) {
            if (Integer.signum(annee) != 0) {
                libelle += ", " + annee;
            }
        }

        return libelle;
    }

    private APISection creerSectionRecouvrement(BSession sessionOsiris,
            APIGestionComptabiliteExterne gestionComptaExterne, APICompteAnnexe compteAnnexe, Integer annee,
            String idCaisseProf) throws Exception {
        String idExterneSectionRecouvrement = CAUtil.creerNumeroSectionUnique(sessionOsiris, getTransaction(),
                compteAnnexe.getIdRole(), compteAnnexe.getIdExterneRole(), "1", annee.toString(),
                APISection.ID_CATEGORIE_SECTION_RECOUVREMENT_IRRECOUVRABLE);

        APISection sectionRecouvrement = gestionComptaExterne.createSection(null, idCompteAnnexe,
                APISection.ID_TYPE_SECTION_DECOMPTE_COTISATION, idExterneSectionRecouvrement, null, null,
                Boolean.FALSE, idCaisseProf);

        return sectionRecouvrement;
    }

    /**
     * Retourne la liste des années présentes dans les postes. Chaque année n'apparait qu'une seule fois
     * 
     * @return
     * @throws Exception
     */
    private List<Integer> determinerAnneeATraiter() throws Exception {
        List<Integer> anneeATraiterList = new ArrayList<Integer>();
        // parcours de tous les postes
        for (Map.Entry<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPosteEntry : recouvrementPostesMap
                .entrySet()) {
            CARecouvrementPoste poste = recouvrementPosteEntry.getValue();
            if (!anneeATraiterList.contains(poste.getAnnee())) {
                anneeATraiterList.add(poste.getAnnee());
            }
        }

        return anneeATraiterList;
    }

    /**
     * Retourne l'année max contenue dans la liste passée en paramètre
     * 
     * @param anneeList
     * @return
     * @throws Exception
     */
    private Integer determinerAnneeMax(List<Integer> anneeList) throws Exception {
        Integer anneeMax = new Integer(-1);
        // parcours la liste des années
        for (Integer annee : anneeList) {
            if (annee.compareTo(anneeMax) > 0) {
                anneeMax = annee;
            }
        }

        if (anneeMax.intValue() <= 0) {
            return new Integer(JACalendar.getYear(JACalendar.today().toString()));
        }

        return anneeMax;
    }

    private String determinerIdCaisseProf(BSession sessionOsiris) throws Exception {
        String idCaisseProf = "0";
        for (String idSection : idSectionsList) {
            CASection section = COProcessComptabiliserRecouvrement.retrieveSection(idSection, sessionOsiris);
            if (!JadeStringUtil.isBlankOrZero(idCaisseProf)) {
                break;
            }
            if (!section.isNew()) {
                idCaisseProf = section.getIdCaisseProfessionnelle();
            }
        }
        return idCaisseProf;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            // Hack pour recupérer le message de la transaction et remettre la transaction en erreur car le getErrors
            // vide le buffer
            String message = new String(getSession().getErrors().toString());
            getSession().addError(message);
            return getSession().getLabel("IRRECOUVRABLE_PROCESS_ERROR") + " " + message;
        } else {
            return libelleMail + "\n" + getSession().getLabel("IRRECOUVRABLE_PROCESS_SUCCESS");
        }
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdCompteIndividuelAffilie() {
        return idCompteIndividuelAffilie;
    }

    /**
     * @return the recouvrementCiMap
     */
    public Map<Integer, CARecouvrementCi> getRecouvrementCiMap() {
        return recouvrementCiMap;
    }

    public Map<CARecouvrementKeyPosteContainer, CARecouvrementPoste> getRecouvrementPostesMap() {
        return recouvrementPostesMap;
    }

    /**
     * @return
     * @throws Exception
     */
    private BSession getSessionOsiris() throws Exception {
        BSession osirisSession = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS)
                .newSession(getSession());
        return osirisSession;
    }

    /**
     * @return
     * @throws Exception
     */
    private BSession getSessionPavo() throws Exception {
        BSession pavoSession = (BSession) GlobazSystem.getApplication(CIApplication.DEFAULT_APPLICATION_PAVO)
                .newSession(getSession());
        return pavoSession;
    }

    public boolean getWantTraiterRecouvrementCi() {
        return wantTraiterRecouvrementCi;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    private void passerInscriptionsCi() throws Exception {
        CIJournal journalAnneeLimite = null;
        CIJournal journalLot = null;

        for (Map.Entry<Integer, CARecouvrementCi> recouvrementCiEntry : recouvrementCiMap.entrySet()) {
            CARecouvrementCi recouvrementCi = recouvrementCiEntry.getValue();

            if (recouvrementCi.getMontantRecouvrement().signum() == 0) {
                continue;
            }

            // détermination du genre d'écriture
            if (recouvrementCi.getGenreDecision() == null) {
                throw new Exception("Le genre de décision ne peut pas être null pour le recouvrement CI");
            }
            String genreEcriture = null;
            if (recouvrementCi.getGenreDecision().equals(CPDecision.CS_INDEPENDANT)) {
                genreEcriture = "03";
            } else if (recouvrementCi.getGenreDecision().equals(CPDecision.CS_NON_ACTIF)) {
                genreEcriture = "04";
            } else if (recouvrementCi.getGenreDecision().equals(CPDecision.CS_TSE)) {
                genreEcriture = "02";
            } else if (recouvrementCi.getGenreDecision().equals(CPDecision.CS_RENTIER)) {
                genreEcriture = "07";
            } else if (recouvrementCi.getGenreDecision().equals(CPDecision.CS_AGRICULTEUR)) {
                genreEcriture = "09";
            } else if (recouvrementCi.getGenreDecision().equals(CPDecision.CS_ETUDIANT)) {
                genreEcriture = "04";
            } else {
                throw new Exception("Le genre de l'écriture CI ne peut pas être déterminé pour le genre de décision :"
                        + recouvrementCi.getGenreDecision());
            }

            String idJournal = null;
            if (recouvrementCi.getAnnee() == anneeLimite) {
                if (journalAnneeLimite == null) {
                    journalAnneeLimite = chargerJournalAnneeLimite(anneeLimite);
                }
                idJournal = journalAnneeLimite.getIdJournal();
            } else {
                if (journalLot == null) {
                    journalLot = creerJournalCI(getSessionPavo(), "recouvrement CI ");
                }
                idJournal = journalLot.getIdJournal();
            }
            CICompteIndividuel compteIndividuel = COProcessComptabiliserRecouvrement.retrieveCompteIndividuel(
                    idCompteIndividuelAffilie, getSessionPavo());
            CACompteAnnexe compteannexe = COProcessComptabiliserRecouvrement.retrieveCompteAnnexe(idCompteAnnexe,
                    getSessionOsiris());

            ajouterEcritureCi(getSessionPavo(), compteannexe.getIdExterneRole(), compteIndividuel.getNumeroAvs(),
                    recouvrementCi.getAnnee().toString(), recouvrementCi.getMontantRecouvrement(), genreEcriture,
                    idJournal);
        }
        // Mise à jour du total inscriptions CI du journal
        if (journalAnneeLimite != null) {
            getTransaction().disableSpy();
            journalAnneeLimite.updateInscription(getTransaction());
            getTransaction().enableSpy();
        }
        if (journalLot != null) {
            getTransaction().disableSpy();
            journalLot.updateInscription(getTransaction());
            getTransaction().enableSpy();
            journalLot.comptabiliser("", "", getTransaction(), this);
        }
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * @param idCompteIndividuelAffilie
     *            the idCompteIndividuelAffilie to set
     */
    public void setIdCompteIndividuelAffilie(String idCompteIndividuelAffilie) {
        this.idCompteIndividuelAffilie = idCompteIndividuelAffilie;
    }

    public void setRecouvrementCiMap(Map<Integer, CARecouvrementCi> recouvrementCiMap) {
        this.recouvrementCiMap = recouvrementCiMap;
    }

    public void setRecouvrementPostesMap(Map<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPostesMap) {
        this.recouvrementPostesMap = recouvrementPostesMap;
    }

    public void setWantTraiterRecouvrementCi(boolean wantTraiterRecouvrementCi) {
        this.wantTraiterRecouvrementCi = wantTraiterRecouvrementCi;
    }

    public List<String> getIdSectionsList() {
        return idSectionsList;
    }

    public void setIdSectionsList(List<String> idSectionsList) {
        this.idSectionsList = idSectionsList;
    }

    public BigDecimal getMontantARecouvrir() {
        return montantARecouvrir;
    }

    public void setMontantARecouvrir(BigDecimal montantARecouvrir) {
        this.montantARecouvrir = montantARecouvrir;
    }

    private CASectionManager chargeEtTriSections(List<String> idSections, BSession sessionOsiris) throws Exception {

        CASectionManager sectionManager = new CASectionManager();
        sectionManager.setSession(sessionOsiris);
        sectionManager.setForIdSectionIn(idSections);
        sectionManager.setForSelectionTri(CASectionManager.ORDER_IDEXTERNE_DATE_ASC);
        sectionManager.find();

        return sectionManager;
    }

    /**
     * Cette méthode permet d'indiquer si le numéro de section de recouvrement doit être incrémenté ou non
     * 
     * @return true si le numéro de section de recouvrement doit être incrémenté
     */
    public boolean isIncrementerNumSectionRecouvrement() {
        return CAApplication.getApplicationOsiris().getCAParametres().isIncrementerNumSectionRecouvrement();
    }

    private String findIdCaisseProfFromSectionAnnee(BSession sessionOsiris, String annee) throws Exception {

        String idCaisseProf = "0";

        CASectionManager sectionManager = new CASectionManager();
        sectionManager.setSession(sessionOsiris);
        sectionManager.setForIdCompteAnnexe(idCompteAnnexe);
        sectionManager.setLikeIdExterne(annee);
        sectionManager.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < sectionManager.size(); i++) {
            CASection section = (CASection) sectionManager.getEntity(i);
            idCaisseProf = section.getIdCaisseProfessionnelle();

            if (!JadeStringUtil.isBlankOrZero(idCaisseProf)) {
                break;
            }
        }

        return idCaisseProf;

    }

}
