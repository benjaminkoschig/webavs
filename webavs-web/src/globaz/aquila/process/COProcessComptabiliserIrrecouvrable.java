package globaz.aquila.process;

import globaz.aquila.db.irrecouvrable.COKeyIdSection;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
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
import globaz.osiris.db.irrecouvrable.CAAmortissementCi;
import globaz.osiris.db.irrecouvrable.CAKeyPosteContainer;
import globaz.osiris.db.irrecouvrable.CALigneDePoste;
import globaz.osiris.db.irrecouvrable.CAPoste;
import globaz.osiris.translation.CACodeSystem;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Processus permettant de comptabiliser les montants irrécouvrables
 * 
 * @author bjo
 */
public class COProcessComptabiliserIrrecouvrable extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Map<Integer, CAAmortissementCi> amortissementCiMap;
    int anneeLimite = 0;
    private String idCompteAnnexe;
    private String idCompteIndividuelAffilie;
    private String libelleMail;
    private Map<CAKeyPosteContainer, CAPoste> postesMap;
    private Boolean wantTraiterAmortissementCi;

    private static CACompteAnnexe retrieveCompteAnnexe(final String idCompteAnnexe, final BSession session)
            throws Exception {
        if (JadeStringUtil.isBlank(idCompteAnnexe)) {
            throw new IllegalArgumentException("unable to load compteAnnexe, the idCompteAnnexe is blank");
        }

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(session);
        compteAnnexe.setIdCompteAnnexe(idCompteAnnexe);
        compteAnnexe.retrieve();

        return compteAnnexe;
    }

    /**
     * Attention cette méthode ne doit être utilisée que dans le but de retrouver l'affiliation pour le passage de
     * l'écriture CI.
     * 
     * @param numAffilie
     * @param annee
     * @param session
     * @return
     * @throws Exception
     */
    private static AFAffiliation retrieveAffiliationForAjoutEcritureCi(final String numAffilie, final String annee,
            final BSession session) throws Exception {
        AFAffiliation affiliation = null;

        if (JadeStringUtil.isBlank(numAffilie)) {
            throw new Exception("numAffilie can't be blank");
        }

        if (JadeStringUtil.isBlank(annee)) {
            throw new Exception("annee can't be blank");
        }

        AFAffiliationManager affiliationManager = new AFAffiliationManager();
        affiliationManager.setForAffilieNumero(numAffilie);
        affiliationManager.setForTypesAffPersonelles();
        affiliationManager.setSession(session);
        affiliationManager.find();

        if (affiliationManager.size() <= 0) {
            throw new Exception("no affiliation retrieve for numero " + numAffilie);
        } else if (affiliationManager.size() == 1) {
            affiliation = (AFAffiliation) affiliationManager.getFirstEntity();
        } else {
            // itération sur chaque affiliation pour trouver celle qui doit être retournée
            AFAffiliation affiliationCourante = null;
            int anneeInt = Integer.parseInt(annee);
            for (int i = 0; i < affiliationManager.size(); i++) {
                affiliationCourante = (AFAffiliation) affiliationManager.getEntity(i);

                // si l'affiliation n'est pas valide on ne la prend pas en compte
                if (!isAffiliationValide(affiliationCourante)) {
                    continue;
                }

                // si l'année de début d'affilition est plus grande que l'année on prend pas en compte
                int anneeDebutAffiliation = new JADate(affiliationCourante.getDateDebut()).getYear();
                if (anneeInt < anneeDebutAffiliation) {
                    continue;
                }

                // si l'année de fin d'affiliation est renseignée et qu'elle est plus petite que l'année on ne prend pas
                // en compte
                if (!JAUtil.isDateEmpty(affiliationCourante.getDateFin())) {
                    int anneeFinAffiliation = new JADate(affiliationCourante.getDateFin()).getYear();
                    if (anneeInt > anneeFinAffiliation) {
                        continue;
                    }
                }

                affiliation = affiliationCourante;
            }
            // si aucune affiliation ne correspond au critère on lève une exception
            if (affiliation == null) {
                throw new Exception("unable to determine the affiliation to use for numero" + numAffilie);
            }
        }

        return affiliation;
    }

    private static boolean isAffiliationValide(final AFAffiliation affiliation) {
        if (affiliation.getDateDebut().equals(affiliation.getDateFin())) {
            return false;
        } else {
            return true;
        }
    }

    private static CICompteIndividuel retrieveCompteIndividuel(final String compteIndividuelId,
            final BSession sessionPavo) throws Exception {
        if (JadeStringUtil.isBlank(compteIndividuelId)) {
            throw new IllegalArgumentException("unable to load compteIndividuel, the compteIndividuelId is blank");
        }

        CICompteIndividuel compteIndividuel = new CICompteIndividuel();
        compteIndividuel.setSession(sessionPavo);
        compteIndividuel.setCompteIndividuelId(compteIndividuelId);
        compteIndividuel.retrieve();

        return compteIndividuel;
    }

    private static CASection retrieveSection(final String idSection, final BSession session) throws Exception {
        if (JadeStringUtil.isBlank(idSection)) {
            throw new IllegalArgumentException("unable to loadSection, the idSection is blank");
        }

        CASection section = new CASection();
        section.setSession(session);
        section.setIdSection(idSection);
        section.retrieve();

        return section;
    }

    public COProcessComptabiliserIrrecouvrable() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        Map<COKeyIdSection, BigDecimal> montantSoldeSectionMap = new HashMap<COKeyIdSection, BigDecimal>();
        BSession sessionOsiris = createSessionOsiris(getSession());
        APIGestionComptabiliteExterne gestionComptaExterne = (APIGestionComptabiliteExterne) sessionOsiris
                .getAPIFor(APIGestionComptabiliteExterne.class);

        // chargement du compte annexe
        CACompteAnnexe compteAnnexe = COProcessComptabiliserIrrecouvrable.retrieveCompteAnnexe(idCompteAnnexe,
                sessionOsiris);
        if (wantTraiterAmortissementCi) {
            // Recherche de l'année limite d'inscription CI - Paramètre
            if (JadeStringUtil.isIntegerEmpty(Integer.toString(anneeLimite))) {
                anneeLimite = Integer.parseInt(CPToolBox.anneeLimite(getTransaction()));
            }
            if (!checkJournalAnneeLimiteExistant(anneeLimite)) {
                throw new Exception("Le journal CI de type cotisation personnelle n'existe pas pour l'année limite");
            }
        }

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

        // création de la section irrécouvrable
        Integer annee = determinerAnneeMax(anneeATraiterList);
        CASection sectionIrrecouvrable = (CASection) creerSectionIrrecouvrable(sessionOsiris, gestionComptaExterne,
                compteAnnexe, annee, idCaisseProf);

        // parcours de tous les postes
        for (Map.Entry<CAKeyPosteContainer, CAPoste> posteEntry : postesMap.entrySet()) {
            CAPoste poste = posteEntry.getValue();
            BigDecimal montantSoldeTotalPoste = new BigDecimal(0);
            Integer anneePoste = poste.getAnnee();

            // parcours de toutes les lignes du poste
            List<CALigneDePoste> ligneDePosteList = poste.getLignesDePosteList();
            for (CALigneDePoste ligneDePoste : ligneDePosteList) {
                // montant solde pour la ligne
                BigDecimal montantSoldeLigne = ligneDePoste.getMontantDu().subtract(ligneDePoste.getMontantAffecte());

                // calcul du solde total pour le poste
                montantSoldeTotalPoste = montantSoldeTotalPoste.add(montantSoldeLigne);

                if (montantSoldeTotalPoste.signum() != 0) {
                    COKeyIdSection keyIdSection = new COKeyIdSection(ligneDePoste.getIdSection());
                    if (montantSoldeSectionMap.containsKey(keyIdSection)) {
                        BigDecimal montantSoldeSection = montantSoldeSectionMap.get(keyIdSection);
                        montantSoldeSection = montantSoldeSection.add(montantSoldeLigne);
                        montantSoldeSectionMap.put(keyIdSection, montantSoldeSection);
                    } else {
                        montantSoldeSectionMap.put(keyIdSection, montantSoldeLigne);
                    }
                }
            }

            if (montantSoldeTotalPoste.signum() != 0) {
                // ajouter l'écriture à la section irrécouvrable
                ajouterEcritureDansSection(gestionComptaExterne, sectionIrrecouvrable.getIdSection(),
                        poste.getIdRubriqueIrrecouvrable(), montantSoldeTotalPoste.toString(), APIEcriture.CREDIT,
                        APIOperation.CAECRITURE, "", anneePoste);
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

        // créer les écritures de compensation
        for (Map.Entry<COKeyIdSection, BigDecimal> montantSoldeSectionEntry : montantSoldeSectionMap.entrySet()) {
            COKeyIdSection keyIdSection = montantSoldeSectionEntry.getKey();
            String idSection = keyIdSection.getIdSection();
            BigDecimal montantSoldeSection = montantSoldeSectionEntry.getValue();

            CASection section = COProcessComptabiliserIrrecouvrable.retrieveSection(idSection, sessionOsiris);
            ajouterMotifIrrecSection(sessionOsiris, section);

            if (!JadeStringUtil.isBlankOrZero(section.getSolde())) {
                ajouterEcrituresCompensation(gestionComptaExterne, section, sectionIrrecouvrable,
                        idRubriqueCompensation, section.getSolde());
            }
        }

        // mise à jour du compte annexe avec le motif irrécouvrable
        ajouterMotifIrrecCompteAnnexe(sessionOsiris, compteAnnexe);

        // Traiter amortissements CI
        if (wantTraiterAmortissementCi) {
            passerInscriptionsCi();
        }

        // comptabilisation du journal si pas d'erreurs
        if (getTransaction().hasErrors()) {
            setSendMailOnError(true);
            getTransaction().rollback();
        } else {
            gestionComptaExterne.comptabiliser();
            getTransaction().commit();
        }

        return true;
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
    private void ajouterEcritureCi(final BSession sessionPavo, final String numAffilie, final String noAVS,
            final String annee, final BigDecimal montantAInscrireCI, final String genreEcriture, final String idJournal)
            throws Exception {

        // récupération de l'affilié
        AFAffiliation affiliation = retrieveAffiliationForAjoutEcritureCi(numAffilie, annee, sessionPavo);
        String idAffilie = affiliation.getAffiliationId();

        CIEcriture ecriture = new CIEcriture();
        ecriture.setSession(sessionPavo);
        ecriture.setGre(genreEcriture);
        ecriture.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
        ecriture.setMoisDebut("99");
        ecriture.setMoisFin("99");
        ecriture.setAnnee(annee);
        ecriture.setAvs(noAVS);
        ecriture.setIdJournal(idJournal);
        ecriture.setMontant(String.valueOf(montantAInscrireCI.longValue()));
        ecriture.setCode(ICIEcriture.CS_CODE_AMORTISSEMENT);
        ecriture.setEmployeur(idAffilie);
        ecriture.add();
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

    private void ajouterEcrituresCompensation(final APIGestionComptabiliteExterne compta, final CASection section,
            final APISection sectionIrrecouvrable, final String idRubriqueCompensation, final String montant)
            throws Exception {
        String libelleIrr = " -> " + section.getIdExterne();
        String libelle = " -> " + sectionIrrecouvrable.getIdExterne();

        // mise a 0 de la section
        ajouterEcritureDansSection(compta, section.getIdSection(), idRubriqueCompensation, montant, APIEcriture.CREDIT,
                APIOperation.CAECRITURECOMPENSATION, libelle, 0);
        // on balance la même chose sur la section irrecouvrable
        ajouterEcritureDansSection(compta, sectionIrrecouvrable.getIdSection(), idRubriqueCompensation, montant,
                APIEcriture.DEBIT, APIOperation.CAECRITURECOMPENSATION, libelleIrr, 0);
    }

    private void ajouterMotifIrrecCompteAnnexe(final BSession sessionOsiris, final APICompteAnnexe compteAnnexe)
            throws Exception {
        if (!compteAnnexe.getContEstBloque().booleanValue()) {
            CACompteAnnexe compte = new CACompteAnnexe();
            compte.setSession(sessionOsiris);
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
            motif.setSession(sessionOsiris);
            motif.setIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
            motif.setIdMotifBlocage(CACodeSystem.CS_IRRECOUVRABLE);
            motif.setDateDebut(JACalendar.todayJJsMMsAAAA());
            motif.setDateFin("31.12.2999");
            motif.add(getTransaction());
        }
    }

    /**
     * Ajoute le motif de blocage du contentieux d'irrecouvrable à la section.
     * 
     * @param sessionOsiris
     * @param section
     * @throws Exception
     */
    private void ajouterMotifIrrecSection(final BSession sessionOsiris, final CASection section) throws Exception {
        if (!section.getContentieuxEstSuspendu().booleanValue()) {
            section.setContentieuxEstSuspendu(Boolean.TRUE);
            section.update(getTransaction());
        }

        if (!section.hasMotifContentieuxForYear(CACodeSystem.CS_IRRECOUVRABLE,
                String.valueOf(JACalendar.today().getYear()))) {
            CAMotifContentieux motif = new CAMotifContentieux();
            motif.setSession(sessionOsiris);
            motif.setIdSection(section.getIdSection());
            motif.setIdMotifBlocage(CACodeSystem.CS_IRRECOUVRABLE);
            motif.setDateDebut(JACalendar.todayJJsMMsAAAA());
            motif.setDateFin("31.12.2999");
            motif.add(getTransaction());
        }
    }

    private CIJournal chargerJournalAnneeLimite(final int anneeLimite) throws Exception {
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

    private boolean checkJournalAnneeLimiteExistant(final int anneeLimite) throws Exception {
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

    private BSession createSessionOsiris(final BSession session) throws Exception {
        BSession sessionOsiris;
        sessionOsiris = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS).newSession();
        session.connectSession(sessionOsiris);
        return sessionOsiris;
    }

    private CIJournal creerJournalCI(final BSession sessionPavo, final String libelle) throws Exception {
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

    private void creerJournalComptabiliteAuxiliaire(final APIGestionComptabiliteExterne gestionComptaExterne,
            final String libelleJournal) {
        gestionComptaExterne.setProcess(this);
        gestionComptaExterne.setTransaction(getTransaction());
        gestionComptaExterne.setSendCompletionMail(true);
        gestionComptaExterne.setDateValeur(JACalendar.todayJJsMMsAAAA());
        gestionComptaExterne.setEMailAddress(getEMailAddress());
        gestionComptaExterne.setLibelle(libelleJournal);
        gestionComptaExterne.createJournal();
    }

    private String creerLibelle(final APICompteAnnexe compteAnnexe, final List<Integer> anneeATraiterList) {
        String role = compteAnnexe.getRole().getDescription();
        String numeroAffilie = compteAnnexe.getIdExterneRole();
        String description = compteAnnexe.getDescription();
        String libelle = getSession().getLabel("IRRECOUVRABLE_PREFIXE_LIBELLE_JOURNAL");
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

    private APISection creerSectionIrrecouvrable(final BSession sessionOsiris,
            final APIGestionComptabiliteExterne gestionComptaExterne, final APICompteAnnexe compteAnnexe,
            final Integer annee, final String idCaisseProf) throws Exception {
        String idExterneSectionIrrecouvrable = CAUtil.creerNumeroSectionUnique(sessionOsiris, getTransaction(),
                compteAnnexe.getIdRole(), compteAnnexe.getIdExterneRole(), "1", annee.toString(),
                APISection.ID_CATEGORIE_SECTION_IRRECOUVRABLE);

        APISection sectionIrrecouvrable = gestionComptaExterne.createSection(null, idCompteAnnexe,
                APISection.ID_TYPE_SECTION_DECOMPTE_COTISATION, idExterneSectionIrrecouvrable, null, null,
                Boolean.FALSE, idCaisseProf);

        return sectionIrrecouvrable;
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
        for (Map.Entry<CAKeyPosteContainer, CAPoste> posteEntry : postesMap.entrySet()) {
            CAPoste poste = posteEntry.getValue();
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
    private Integer determinerAnneeMax(final List<Integer> anneeList) throws Exception {
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

    private String determinerIdCaisseProf(final BSession sessionOsiris) throws Exception {
        String idCaisseProf = null;
        // parcours de tous les postes
        for (Map.Entry<CAKeyPosteContainer, CAPoste> posteEntry : postesMap.entrySet()) {
            CAPoste poste = posteEntry.getValue();

            // parcours de toutes les lignes du poste
            List<CALigneDePoste> ligneDePosteList = poste.getLignesDePosteList();
            for (CALigneDePoste ligneDePoste : ligneDePosteList) {
                if (JadeStringUtil.isBlank(idCaisseProf)) {
                    CASection section = COProcessComptabiliserIrrecouvrable.retrieveSection(
                            ligneDePoste.getIdSection(), sessionOsiris);
                    if (!JadeStringUtil.isBlank(section.getIdCaisseProfessionnelle())) {
                        idCaisseProf = section.getIdCaisseProfessionnelle();
                    }
                }
            }
        }

        // si l'idCaisseProf n'a pas pu être déterminé on met la valeur 0
        if (JadeStringUtil.isBlank(idCaisseProf)) {
            idCaisseProf = "0";
        }

        return idCaisseProf;
    }

    /**
     * @return the amortissementCiMap
     */
    public Map<Integer, CAAmortissementCi> getAmortissementCiMap() {
        return amortissementCiMap;
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

    public Map<CAKeyPosteContainer, CAPoste> getPostesMap() {
        return postesMap;
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

    public Boolean getWantTraiterAmortissementCi() {
        return wantTraiterAmortissementCi;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        // Bug 8947
        return GlobazJobQueue.UPDATE_LONG;
    }

    private void passerInscriptionsCi() throws Exception {
        CIJournal journalAnneeLimite = null;
        CIJournal journalLot = null;

        for (Map.Entry<Integer, CAAmortissementCi> amortissementCiEntry : amortissementCiMap.entrySet()) {
            CAAmortissementCi amortissementCi = amortissementCiEntry.getValue();

            if (amortissementCi.getMontantAmortissement().signum() == 0) {
                continue;
            }

            // détermination du genre d'écriture
            if (amortissementCi.getGenreDecision() == null) {
                throw new Exception("Le genre de décision ne peut pas être null pour l'amortissement CI");
            }
            String genreEcriture = null;
            if (amortissementCi.getGenreDecision().equals(CPDecision.CS_INDEPENDANT)) {
                genreEcriture = "13";
            } else if (amortissementCi.getGenreDecision().equals(CPDecision.CS_NON_ACTIF)) {
                genreEcriture = "14";
            } else if (amortissementCi.getGenreDecision().equals(CPDecision.CS_TSE)) {
                genreEcriture = "12";
            } else if (amortissementCi.getGenreDecision().equals(CPDecision.CS_RENTIER)) {
                genreEcriture = "17";
            } else if (amortissementCi.getGenreDecision().equals(CPDecision.CS_AGRICULTEUR)) {
                genreEcriture = "19";
            } else if (amortissementCi.getGenreDecision().equals(CPDecision.CS_ETUDIANT)) {
                genreEcriture = "14";
            } else {
                throw new Exception("Le genre de l'écriture CI ne peut pas être déterminé pour le genre de décision :"
                        + amortissementCi.getGenreDecision());
            }

            String idJournal = null;
            if (amortissementCi.getAnnee() == anneeLimite) {
                if (journalAnneeLimite == null) {
                    journalAnneeLimite = chargerJournalAnneeLimite(anneeLimite);
                }
                idJournal = journalAnneeLimite.getIdJournal();
            } else {
                if (journalLot == null) {
                    journalLot = creerJournalCI(getSessionPavo(), "Amortissement CI ");
                }
                idJournal = journalLot.getIdJournal();
            }
            CICompteIndividuel compteIndividuel = COProcessComptabiliserIrrecouvrable.retrieveCompteIndividuel(
                    idCompteIndividuelAffilie, getSessionPavo());
            CACompteAnnexe compteannexe = COProcessComptabiliserIrrecouvrable.retrieveCompteAnnexe(idCompteAnnexe,
                    getSessionOsiris());

            ajouterEcritureCi(getSessionPavo(), compteannexe.getIdExterneRole(), compteIndividuel.getNumeroAvs(),
                    amortissementCi.getAnnee().toString(), amortissementCi.getMontantAmortissement(), genreEcriture,
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

    public void setAmortissementCiMap(final Map<Integer, CAAmortissementCi> amortissementCiMap) {
        this.amortissementCiMap = amortissementCiMap;
    }

    public void setIdCompteAnnexe(final String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * @param idCompteIndividuelAffilie
     *            the idCompteIndividuelAffilie to set
     */
    public void setIdCompteIndividuelAffilie(final String idCompteIndividuelAffilie) {
        this.idCompteIndividuelAffilie = idCompteIndividuelAffilie;
    }

    public void setPostesMap(final Map<CAKeyPosteContainer, CAPoste> postesMap) {
        this.postesMap = postesMap;
    }

    public void setWantTraiterAmortissementCi(final Boolean wantTraiterAmortissementCi) {
        this.wantTraiterAmortissementCi = wantTraiterAmortissementCi;
    }
}
