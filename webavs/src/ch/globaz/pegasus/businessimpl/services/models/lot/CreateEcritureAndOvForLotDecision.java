package ch.globaz.pegasus.businessimpl.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;

class CreateEcritureAndOvForLotDecision extends PCLotBusinessAbstract {

    @Override
    public void comptabiliserLot(SimpleLot lot, String idOrganeExecution, String numeroOG, String libelleJournal,
            String dateValeur, String dateEchance) throws ComptabiliserLotException, JadePersistenceException {
        // TODO Auto-generated method stub

    }

    // private Map<String, APIRubrique> cacheRubrique = new HashMap<String, APIRubrique>();
    // private String dateEchance = null;
    //
    // protected void addEcriture(String libelle, String codeDebitCredit, CompteAnnexeSimpleModel compteAnnexe,
    // SectionSimpleModel section, String dateValeur, APIRubrique rubrique, String montant,
    // JournalConteneur conteneur, OrdreVersementForList ov) {
    //
    // JadeThread.logInfo(this.getClass().getName(),
    // this.descEriture(codeDebitCredit, compteAnnexe, rubrique, section, montant, ov));
    //
    // conteneur.addEcriture("", codeDebitCredit, compteAnnexe.getIdCompteAnnexe(), section.getIdSection(),
    // dateValeur, rubrique.getIdRubrique(), montant);
    // }
    //
    // private BigDecimal addEcritureAllResitution(OrdreVersementPrepare ovsPrepare, String dateValeur,
    // JournalConteneur conteneur, CompteAnnexeSimpleModel compteAnnexeBeneficaireSimpleModel,
    // SectionSimpleModel sectionDecisionPC) throws ComptabiliserLotException,
    // JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
    //
    // BigDecimal montantRestitution = new BigDecimal(0);
    //
    // // Récupération de la section ou création
    // SectionSimpleModel sectionRestitution = this.getSection(compteAnnexeBeneficaireSimpleModel,
    // APISection.ID_TYPE_SECTION_RESTITUTION, APISection.ID_CATEGORIE_SECTION_RESTITUTIONS, dateValeur,
    // conteneur);
    //
    // // Pour un couple à 2 rente principale on met tout sur le compte annexe du bénéficiaire
    // CompteAnnexeSimpleModel compteAnnexeBeneficaireConjSimpleModel = compteAnnexeBeneficaireSimpleModel;
    // String montantPcDue = "0";
    //
    // if (ovsPrepare.getRestitutionsRequerant().size() > 0) {
    // if ((ovsPrepare.getOvRequerant() != null) && (ovsPrepare.getOvRequerant().size() > 0)) {
    // montantPcDue = ovsPrepare.getTotalMontantRequerant().toString();
    // }
    // for (OrdreVersementForList ov : ovsPrepare.getRestitutionsRequerant()) {
    // montantRestitution = montantRestitution.add(this.addEcritureForRestitution(dateValeur, conteneur,
    // montantPcDue, ov, compteAnnexeBeneficaireSimpleModel, sectionRestitution, sectionDecisionPC));
    // }
    // }
    //
    // if (ovsPrepare.getRestitutionsConjoint().size() > 0) {
    // montantPcDue = "0";
    // // Récupération de la section ou création
    //
    // if ((ovsPrepare.getOvConjoint() != null) && (ovsPrepare.getOvConjoint().size() > 0)) {
    // montantPcDue = ovsPrepare.getTotalMontantConjoint().toString();
    // }
    // // ICI on traite le passage d'un couple séparé par la maladie à un couple à Domicile.
    // // Comme avant on avait 2 décisions distinct on avait donc 2 comptes annexes distinct. La restituions doit
    // // être
    // // répartie sur les bons comptes annexes.
    //
    // if (IPCOrdresVersements.CS_PASSAGE_SEPARE_MALADIE_A_DOM.equals(ovsPrepare.getRestitutionsConjoint().get(0)
    // .getSimpleOrdreVersement().getCsPassage())) {
    //
    // PersonneEtendueComplexModel pers = TIBusinessServiceLocator.getPersonneEtendueService().read(
    // ovsPrepare.getRestitutionsConjoint().get(0).getSimpleOrdreVersement().getIdTiers());
    //
    // compteAnnexeBeneficaireConjSimpleModel = CABusinessServiceLocator.getCompteAnnexeService()
    // .getCompteAnnexe(null,
    // ovsPrepare.getRestitutionsConjoint().get(0).getSimpleOrdreVersement().getIdTiers(),
    // IntRole.ROLE_RENTIER, pers.getPersonneEtendue().getNumAvsActuel(), true);
    //
    // String numSection = BSessionUtil.getSessionFromThreadContext().getCode(
    // APISection.ID_CATEGORIE_SECTION_DECISION_PC);
    //
    // sectionDecisionPC = this.getSection(compteAnnexeBeneficaireConjSimpleModel, numSection,
    // APISection.ID_CATEGORIE_SECTION_DECISION_PC, dateValeur, conteneur);
    //
    // sectionRestitution = this.getSection(compteAnnexeBeneficaireConjSimpleModel,
    // APISection.ID_TYPE_SECTION_RESTITUTION, APISection.ID_CATEGORIE_SECTION_RESTITUTIONS,
    // dateValeur, conteneur);
    // }
    // for (OrdreVersementForList ov : ovsPrepare.getRestitutionsConjoint()) {
    // montantRestitution = montantRestitution.add(this
    // .addEcritureForRestitution(dateValeur, conteneur, montantPcDue, ov,
    // compteAnnexeBeneficaireConjSimpleModel, sectionRestitution, sectionDecisionPC));
    // }
    // }
    //
    // return montantRestitution;
    // }
    //
    // public BigDecimal addEcritureAndOrdresVerementsNew(List<OrdreVersementPrepare> listOV, String dateValeur,
    // JournalConteneur conteneur) throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
    // JadeApplicationException {
    //
    // Map<String, List<OrdreVersementPrepare>> listPrestationOv = JadeListUtil.groupBy(listOV,
    // new Key<OrdreVersementPrepare>() {
    // public String exec(OrdreVersementPrepare e) {
    // if ((e.getOvRequerant() != null) && (e.getOvRequerant().size() > 0)) {
    // return e.getOvRequerant().get(0).getSimpleOrdreVersement().getIdPrestation();
    // } else if ((e.getRestitutionsRequerant() != null) && (e.getRestitutionsRequerant().size() > 0)) {
    // return e.getRestitutionsRequerant().get(0).getSimpleOrdreVersement().getIdPrestation();
    // } else if ((e.getOvConjoint() != null) && (e.getOvConjoint().size() > 0)) {
    // return e.getOvConjoint().get(0).getSimpleOrdreVersement().getIdPrestation();
    // } else {
    // return e.getRestitutionsRequerant().get(0).getSimpleOrdreVersement().getIdPrestation();
    // }
    // }
    // });
    //
    // BigDecimal totalPrestation = new BigDecimal(0);
    //
    // for (Entry<String, List<OrdreVersementPrepare>> ovPrest : listPrestationOv.entrySet()) {
    // OrdreVersementForList ovRequerant = null;
    // if (ovPrest.getValue().get(0).getOvRequerant().size() > 0) {
    // ovRequerant = ovPrest.getValue().get(0).getOvRequerant().get(0);
    // }
    // if (ovRequerant == null) {
    // ovRequerant = ovPrest.getValue().get(0).getRestitutionsRequerant().get(0);
    // }
    // BigDecimal montantPrestation = new BigDecimal(ovRequerant.getMontantPresation());
    // totalPrestation = totalPrestation.add(montantPrestation);
    //
    // JadeThread.logInfo(this.getClass().getName(),
    // "Ecritures pour la prestation n° " + ovPrest.getKey() + " - " + ovRequerant.getNumAvs() + "/"
    // + ovRequerant.getDesignationRequerant1() + " / " + ovRequerant.getDesignationRequerant2()
    // + " | " + new FWCurrency(ovRequerant.getMontantPresation()).toStringFormat());
    //
    // CompteAnnexeSimpleModel compteAnnexeBeneficaireSimpleModel = CABusinessServiceLocator
    // .getCompteAnnexeService().getCompteAnnexe(null, ovRequerant.getIdTiersRequerant(),
    // IntRole.ROLE_RENTIER, ovRequerant.getNumAvs(), true);
    //
    // String numSection = BSessionUtil.getSessionFromThreadContext().getCode(
    // APISection.ID_CATEGORIE_SECTION_DECISION_PC);
    //
    // SectionSimpleModel sectionDecisionPC = this.getSection(compteAnnexeBeneficaireSimpleModel, numSection,
    // APISection.ID_CATEGORIE_SECTION_DECISION_PC, dateValeur, conteneur);
    //
    // BigDecimal montantDetteEnCompta = new BigDecimal(0);
    //
    // for (OrdreVersementPrepare ov : ovPrest.getValue()) {
    // for (Entry<String, List<OrdreVersementForList>> entryDetteOv : ov.getMapDettes().entrySet()) {
    //
    // if (IREOrdresVersements.CS_TYPE_DETTE.equals(entryDetteOv.getKey())) {
    // for (OrdreVersementForList detteOV : entryDetteOv.getValue()) {
    // montantDetteEnCompta = this.createEcritureForDetteEnComptat(dateValeur, conteneur,
    // compteAnnexeBeneficaireSimpleModel, sectionDecisionPC, montantDetteEnCompta,
    // detteOV.getSimpleOrdreVersement());
    // }
    // } else if (IREOrdresVersements.CS_TYPE_TIERS.equals(entryDetteOv.getKey())) {
    // for (OrdreVersementForList detteOV : entryDetteOv.getValue()) {
    // this.addOrdreVersement(detteOV, sectionDecisionPC, compteAnnexeBeneficaireSimpleModel,
    // conteneur, detteOV.getSimpleOrdreVersement().getMontant());
    // }
    // } else if (IREOrdresVersements.CS_TYPE_IMPOT_SOURCE.equals(entryDetteOv.getKey())) {
    // for (OrdreVersementForList detteOV : entryDetteOv.getValue()) {
    // this.addOrdreVersement(detteOV, sectionDecisionPC, compteAnnexeBeneficaireSimpleModel,
    // conteneur, detteOV.getSimpleOrdreVersement().getMontant());
    // }
    // } else if (IREOrdresVersements.CS_TYPE_ASSURANCE_SOCIALE.equals(entryDetteOv.getKey())) {
    // for (OrdreVersementForList detteOV : entryDetteOv.getValue()) {
    // this.addOrdreVersement(detteOV, sectionDecisionPC, compteAnnexeBeneficaireSimpleModel,
    // conteneur, detteOV.getSimpleOrdreVersement().getMontant());
    // }
    // }
    //
    // }
    //
    // this.addEcritureForAllocationNoel(dateValeur, conteneur, compteAnnexeBeneficaireSimpleModel,
    // sectionDecisionPC, ov.getAllocationNoels());
    //
    // this.addEcritureAllResitution(ov, dateValeur, conteneur, compteAnnexeBeneficaireSimpleModel,
    // sectionDecisionPC);
    //
    // this.addEcritureForBeneficiare(dateValeur, ov, conteneur, compteAnnexeBeneficaireSimpleModel,
    // sectionDecisionPC);
    // }
    // }
    // return totalPrestation;
    // }
    //
    // private void addEcritureForAllocationNoel(String dateValeur, JournalConteneur conteneur,
    // CompteAnnexeSimpleModel compteAnnexeBeneficaireSimpleModel, SectionSimpleModel sectionDecisionPC,
    // List<OrdreVersementForList> ovs) throws JadeApplicationException, ComptabiliserLotException,
    // JadeApplicationServiceNotAvailableException, JadePersistenceException {
    // for (OrdreVersementForList ovAllocation : ovs) {
    // String idRubrique = HandlerRubrique.determineCsNormal(ovAllocation.getSimpleOrdreVersement());
    //
    // APIRubrique rubrique = this.findRubrique(idRubrique);
    //
    // this.addEcriture("", APIEcriture.CREDIT, compteAnnexeBeneficaireSimpleModel, sectionDecisionPC, dateValeur,
    // rubrique, ovAllocation.getSimpleOrdreVersement().getMontant().toString(), conteneur, ovAllocation);
    //
    // this.addOrdreVersement(ovAllocation, sectionDecisionPC, compteAnnexeBeneficaireSimpleModel, conteneur,
    // ovAllocation.getSimpleOrdreVersement().getMontant().toString());
    //
    // }
    //
    // }
    //
    // private void addEcritureForBeneficiare(String dateValeur, OrdreVersementPrepare ov, JournalConteneur conteneur,
    // CompteAnnexeSimpleModel compteAnnexeBeneficaireSimpleModel, SectionSimpleModel sectionDecisionPC)
    // throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
    //
    // // si le requérant est définit à null cela signifie que l'on n'a que de la restitution
    // if ((ov.getOvRequerant() != null) && (ov.getOvRequerant().size() > 0)) {
    //
    // // Si il reste de l'argent on paye le bénéficiaire.
    // if (ov.getMontantAdisposition().signum() == 1) {
    // this.addOrdreVersement(ov.getOvRequerant().get(0), sectionDecisionPC,
    // compteAnnexeBeneficaireSimpleModel, conteneur, ov.getMontantRequerant().toString());
    //
    // if ((ov.getOvConjoint() != null) && (ov.getOvConjoint().size() > 0)
    // && (ov.getOvConjoint().get(0).getSimpleOrdreVersement() != null)) {
    // this.addOrdreVersement(ov.getOvConjoint().get(0), sectionDecisionPC,
    // compteAnnexeBeneficaireSimpleModel, conteneur, ov.getMontantConjoint().toString());
    // }
    //
    // }
    //
    // // gestion des écritures pour le requérant
    // this.createEcritureForbenficaire(dateValeur, ov, conteneur, compteAnnexeBeneficaireSimpleModel,
    // sectionDecisionPC, ov.getOvRequerant());
    //
    // // gestion des écritures pour le conjoint
    // this.createEcritureForbenficaire(dateValeur, ov, conteneur, compteAnnexeBeneficaireSimpleModel,
    // sectionDecisionPC, ov.getOvConjoint());
    //
    // }
    // }
    //
    // private BigDecimal addEcritureForRestitution(String dateValeur, JournalConteneur conteneur, String montantPcDue,
    // OrdreVersementForList ovRestitution, CompteAnnexeSimpleModel compteAnnexeBeneficaireSimpleModel,
    // SectionSimpleModel sectionRestitution, SectionSimpleModel sectionDecisionPC)
    // throws ComptabiliserLotException, JadePersistenceException, JadeApplicationException,
    // JadeApplicationServiceNotAvailableException {
    //
    // BigDecimal montantResitution = new BigDecimal(0);
    // BigDecimal montantBeneficiaire = new BigDecimal(montantPcDue);
    //
    // if (ovRestitution != null) {
    // montantResitution = new BigDecimal(ovRestitution.getSimpleOrdreVersement().getMontant());
    //
    // if (montantResitution.signum() == 1) {
    // String csRubriqueRestitution = HandlerRubrique.determineCsRestitution(ovRestitution
    // .getSimpleOrdreVersement());
    //
    // APIRubrique rubrique = this.findRubrique(APIReferenceRubrique.COMPENSATION_RENTES);
    //
    // BigDecimal montantRestitutionMin = montantResitution.min(montantBeneficiaire);
    // APIRubrique rubriqueRestitution = this.findRubrique(csRubriqueRestitution);
    //
    // // si il n'y pas d'argent on ne créer pas d'ecriture car il n'y rien a compenser
    // if (montantRestitutionMin.signum() != 0) {
    // // compensation
    // this.addEcriture("", APIEcriture.CREDIT, compteAnnexeBeneficaireSimpleModel, sectionRestitution,
    // dateValeur, rubrique, montantRestitutionMin.toString(), conteneur, ovRestitution);
    //
    // this.addEcriture("", APIEcriture.DEBIT, compteAnnexeBeneficaireSimpleModel, sectionDecisionPC,
    // dateValeur, rubrique, montantRestitutionMin.toString(), conteneur, ovRestitution);
    // }
    // // restitution
    // this.addEcriture("", APIEcriture.DEBIT, compteAnnexeBeneficaireSimpleModel, sectionRestitution,
    // dateValeur, rubriqueRestitution, ovRestitution.getSimpleOrdreVersement().getMontant(),
    // conteneur, ovRestitution);
    // }
    //
    // }
    // return montantResitution.abs();
    // }
    //
    // private JournalConteneur addOrdreVersement(OrdreVersementForList ov, SectionSimpleModel section,
    // CompteAnnexeSimpleModel compteAnnexeSimpleModel, JournalConteneur conteneur, String montant)
    // throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
    //
    // SimpleOrdreVersement simpleOrdreVersement = ov.getSimpleOrdreVersement();
    // String desc = "";
    // if (!JadeStringUtil.isEmpty(ov.getDesignationRequerant1())) {
    // desc = "(" + ov.getDesignationOv1() + " " + ov.getDesignationOv2() + ")";
    // }
    //
    // AdresseTiersDetail adr = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
    // simpleOrdreVersement.getIdTiersAdressePaiement(), true, simpleOrdreVersement.getIdDomaineApplication(),
    // conteneur.getJournalModel().getDateValeurCG(), null);
    //
    // JadeThread.logInfo(this.getClass().getName(), " -- OV " + compteAnnexeSimpleModel.getDescription() + desc
    // + " | " + section.getIdExterne() + " | montant: " + montant);
    //
    // if (adr.getFields() != null) {
    //
    // String idAressePaimentUnique = adr.getFields().get(AdresseTiersDetail.ADRESSEP_ID_AVOIR_PAIEMENT_UNIQUE);
    // conteneur.addOrdreVersement(conteneur.getJournalModel().getIdJournal(),
    // compteAnnexeSimpleModel.getIdCompteAnnexe(), section.getIdSection(), idAressePaimentUnique,
    // this.dateEchance, montant, "CHF", "CHF", APIOperationOrdreVersement.VIREMENT,
    // CAOrdreGroupe.NATURE_RENTES_AVS_AI, ov.getNumAvs() + " Prestation complémentaire du "
    // + this.dateEchance + " - " + compteAnnexeSimpleModel.getDescription());
    // } else {
    // PersonneEtendueComplexModel tier = TIBusinessServiceLocator.getPersonneEtendueService().read(
    // simpleOrdreVersement.getIdTiersAdressePaiement());
    // throw new ComptabiliserLotException("Unable to find the adresse paiement for "
    // + tier.getPersonneEtendue().getNumAvsActuel() + " " + tier.getTiers().getDesignation1() + " "
    // + tier.getTiers().getDesignation2() + "(id:" + simpleOrdreVersement.getIdTiersAdressePaiement()
    // + ")" + "Type OV: "
    // + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(simpleOrdreVersement.getCsType()));
    // }
    //
    // return conteneur;
    // }
    //
    // @Override
    // public void comptabiliserLot(SimpleLot lot, String idOrganeExecution, String numeroOG, String libelleJournal,
    // String dateValeur, String dateEchance) throws ComptabiliserLotException, JadePersistenceException {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void comptabiliserLot(SimpleLot lot, String idOrganeExecution, String numeroOG, String libelleJournal,
    // String dateValeur, String dateEchance) throws ComptabiliserLotException, JadePersistenceException {
    //
    // JournalConteneur conteneur = null;
    //
    // boolean hasException = false;
    // try {
    // JADate dateComptable = new JADate(dateValeur);
    // JADate dateDernierPmt = new JADate(PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt());
    //
    // if ((dateComptable.getMonth() != dateDernierPmt.getMonth())
    // || (dateComptable.getYear() != dateDernierPmt.getYear())) {
    // throw new ComptabiliserLotException(
    // "La date de valeur comptable doit être dans le même mois que le dernier pmt mensuel.");
    // }
    // // simpleLot = CorvusServiceLocator.getLotService().read(idLot);
    //
    // lot.setCsEtat(IRELot.CS_ETAT_LOT_EN_TRAITEMENT);
    // CorvusServiceLocator.getLotService().update(lot);
    //
    // if (IRELot.CS_ETAT_LOT_VALIDE.equals(lot.getCsEtat())) {
    // throw new ComptabiliserLotException("Impossible de valider un lot déja vlidé");
    // }
    // this.dateEchance = dateEchance;
    //
    // if (libelleJournal == null) {
    // libelleJournal = lot.getDescription();
    // }
    //
    // try {
    // conteneur = this.createJournal(lot.getIdLot(), libelleJournal, dateValeur, dateEchance);
    // } catch (Throwable e) {
    // hasException = true;
    // throw new ComptabiliserLotException("Unable to create the journal", e);
    // }
    //
    // if (!JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
    //
    // CABusinessServiceLocator.getJournalService().comptabilise(conteneur.getJournalModel());
    //
    // lot.setCsEtat(IRELot.CS_ETAT_LOT_VALIDE);
    //
    // } else {
    // hasException = true;
    // JadeThread.logError(this.getClass().getName(), "pc.lot.comptablisation.erreurs");
    // }
    //
    // } catch (JadeApplicationServiceNotAvailableException e) {
    // hasException = true;
    // throw new ComptabiliserLotException("Unable to access service for the journal", e);
    // } catch (JadeApplicationException e) {
    // hasException = true;
    // throw new ComptabiliserLotException("Unable to access create the journal and operation", e);
    // } catch (JAException e) {
    // hasException = true;
    // throw new ComptabiliserLotException("Unable to create the date", e);
    // } finally {
    // try {
    //
    // if (hasException) {
    // lot.setCsEtat(IRELot.CS_ETAT_LOT_ERREUR);
    // } else {
    // lot.setCsEtat(IRELot.CS_ETAT_LOT_VALIDE);
    // }
    // if (conteneur != null) {
    // lot.setIdJournalCA(conteneur.getJournalModel().getIdJournal());
    // }
    // CorvusServiceLocator.getLotService().update(lot);
    //
    // } catch (LotException e) {
    // throw new ComptabiliserLotException("Unable to update the lot", e);
    // } catch (JadeApplicationServiceNotAvailableException e) {
    // throw new ComptabiliserLotException("Unable to update the lot", e);
    // }
    // }
    // }
    //
    // private void createEcritureForbenficaire(String dateValeur, OrdreVersementPrepare ov, JournalConteneur conteneur,
    // CompteAnnexeSimpleModel compteAnnexeBeneficaireSimpleModel, SectionSimpleModel sectionDecisionPC,
    // List<OrdreVersementForList> listOv) throws JadeApplicationException, ComptabiliserLotException {
    // for (OrdreVersementForList o : listOv) {
    // // Ajout de l'écriture Pc à verser
    // if (new BigDecimal(o.getSimpleOrdreVersement().getMontant()).signum() == 1) {
    // String idRubrique = HandlerRubrique.determineCsNormal(o.getSimpleOrdreVersement());
    //
    // APIRubrique rubrique = this.findRubrique(idRubrique);
    //
    // this.addEcriture("", APIEcriture.CREDIT, compteAnnexeBeneficaireSimpleModel, sectionDecisionPC,
    // dateValeur, rubrique, o.getSimpleOrdreVersement().getMontant().toString(), conteneur, o);
    // }
    // }
    // }
    //
    // private BigDecimal createEcritureForDetteEnComptat(String dateValeur, JournalConteneur conteneur,
    // CompteAnnexeSimpleModel compteAnnexeBeneficaireSimpleModel, SectionSimpleModel sectionDecisionPC,
    // BigDecimal montantDetteEnCompta, SimpleOrdreVersement simpleOrdreVersement)
    // throws ComptabiliserLotException {
    // String montantDetteCa = null;
    // if (simpleOrdreVersement.getIsCompense()) {
    // if (JadeStringUtil.isBlankOrZero((simpleOrdreVersement.getMontantDetteModifier()))) {
    // montantDetteCa = simpleOrdreVersement.getMontant();
    // } else {
    // montantDetteCa = simpleOrdreVersement.getMontantDetteModifier();
    // }
    // montantDetteEnCompta = montantDetteEnCompta.add(new BigDecimal(montantDetteCa));
    //
    // APIRubrique rubriqueCompensation = this.findRubrique(APIReferenceRubrique.COMPENSATION_RENTES);
    //
    // SectionSimpleModel section = new SectionSimpleModel();
    // section.setId(simpleOrdreVersement.getIdSectionDetteEnCompta());
    //
    // this.addEcriture("", APIEcriture.CREDIT, compteAnnexeBeneficaireSimpleModel, section, dateValeur,
    // rubriqueCompensation, montantDetteCa, conteneur, null);
    //
    // // rubrique compensation
    // this.addEcriture("", APIEcriture.DEBIT, compteAnnexeBeneficaireSimpleModel, sectionDecisionPC, dateValeur,
    // rubriqueCompensation, montantDetteCa, conteneur, null);
    //
    // }
    // return montantDetteEnCompta;
    // }
    //
    // private JournalSimpleModel createJournal(String libelleJournal, String dateValeur)
    // throws JadeApplicationServiceNotAvailableException, ComptabiliserLotException {
    // try {
    // try {
    // return CABusinessServiceLocator.getJournalService().createJournal(libelleJournal, dateValeur);
    // } catch (JadePersistenceException e) {
    // throw new ComptabiliserLotException("Unable to create the journal (persistence)", e);
    // }
    // } catch (JadeApplicationException e) {
    // throw new ComptabiliserLotException("Unable to create the journal", e);
    // }
    // }
    //
    // private JournalConteneur createJournal(String idLot, String libelleJournal, String dateValeur, String
    // dateEchance)
    // throws JadeApplicationException, JadePersistenceException, ComptabiliserLotException {
    // JournalConteneur conteneur = new JournalConteneur();
    // JournalSimpleModel journal = this.createJournal(libelleJournal, dateValeur);
    // conteneur.AddJournal(journal);
    //
    // List<OrdreVersementPrepare> listOV = PegasusServiceLocator.getOrdreVersementService().preparerOvForComptat(
    // idLot);
    // BigDecimal totalPrestation = new BigDecimal(0);
    //
    // totalPrestation = this.addEcritureAndOrdresVerementsNew(listOV, dateValeur, conteneur);
    //
    // // creer le journal et les operations en comptat
    // CABusinessServiceLocator.getJournalService().createJournalAndOperations(conteneur);
    //
    // BigDecimal totalJournal = new BigDecimal(JANumberFormatter.deQuote(CABusinessServiceLocator.getJournalService()
    // .getSommeEcritures(conteneur.getJournalModel())));
    // // vérification du montant du journal
    // String[] param = new String[2];
    // param[0] = new FWCurrency(totalPrestation.toString()).toStringFormat();
    // param[1] = new FWCurrency(totalJournal.toString()).toStringFormat();
    // if (totalPrestation.compareTo(totalJournal.negate()) != 0) {
    // JadeThread.logError(this.getClass().getName(), "pegasus.lot.comptablisation.osiris.differenceMontant",
    // param);
    // }
    // return conteneur;
    // }
    //
    // private String descEriture(String debitCredit, CompteAnnexeSimpleModel compteAnnexe, APIRubrique rubrique,
    // SectionSimpleModel sectionSimpleModel, String montant, OrdreVersementForList ov) {
    // String dc = "C ";
    // String descPersonne = "";
    // if (ov != null) {
    // descPersonne = "(" + ov.getDesignationOv1() + " " + ov.getDesignationOv2() + ")";
    // }
    // if (APIEcriture.DEBIT.equals(debitCredit)) {
    // dc = "D ";
    // }
    // String id = sectionSimpleModel.getIdExterne();
    // if (JadeStringUtil.isEmpty(id)) {
    // id = sectionSimpleModel.getId();
    // }
    // return " -- " + dc + " | compteAnnexe: " + compteAnnexe.getDescription() + descPersonne + "("
    // + compteAnnexe.getIdCompteAnnexe() + ") | " + id + " | " + rubrique.getDescription() + " | " + montant;
    //
    // }
    //
    // private APIRubrique findRubrique(String idCodeReferenceRubrique) throws ComptabiliserLotException {
    // APIRubrique rubrique = null;
    //
    // if (JadeStringUtil.isIntegerEmpty(idCodeReferenceRubrique)) {
    // throw new ComptabiliserLotException("Unable to findRubrique the  idCodeReferenceRubrique is empty!");
    // }
    // if (this.cacheRubrique.get(idCodeReferenceRubrique) == null) {
    //
    // APIReferenceRubrique referenceRubrique;
    // try {
    // referenceRubrique = (APIReferenceRubrique) PRSession.connectSession(
    // BSessionUtil.getSessionFromThreadContext(), "OSIRIS").getAPIFor(APIReferenceRubrique.class);
    // } catch (Exception e) {
    // throw new ComptabiliserLotException("Technical exception, error to retrieve the reference rubrique", e);
    // }
    //
    // rubrique = referenceRubrique.getRubriqueByCodeReference(idCodeReferenceRubrique);
    // if (rubrique == null) {
    // throw new ComptabiliserLotException("No rubrique was found with this reférenceRubrique: "
    // + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(idCodeReferenceRubrique) + " "
    // + idCodeReferenceRubrique);
    // }
    // this.cacheRubrique.put(idCodeReferenceRubrique, rubrique);
    // }
    // return this.cacheRubrique.get(idCodeReferenceRubrique);
    //
    // }
    //
    // private SectionSimpleModel getSection(CompteAnnexeSimpleModel compteAnnexe, String typeSection,
    // String categorieSection, String dateValeur, JournalConteneur conteneur) throws ComptabiliserLotException {
    // SectionSimpleModel sectionRestitution = null;
    // String idExterne;
    //
    // try {
    // idExterne = CABusinessServiceLocator
    // .getSectionService()
    // .creerNumeroSectionUnique(compteAnnexe.getIdRole(), compteAnnexe.getIdExterneRole(), typeSection,
    // dateValeur.substring(6), categorieSection).getIdExterne();
    //
    // sectionRestitution = CABusinessServiceLocator.getSectionService().getSectionByIdExterne(
    // compteAnnexe.getIdCompteAnnexe(), typeSection, idExterne, conteneur.getJournalModel());
    // } catch (JadeApplicationServiceNotAvailableException e) {
    // throw new ComptabiliserLotException("Unable to find the section ", e);
    // } catch (JadePersistenceException e) {
    // throw new ComptabiliserLotException("Unable to find the section ", e);
    // } catch (JadeApplicationException e) {
    // throw new ComptabiliserLotException("Unable to find the section ", e);
    // }
    //
    // return sectionRestitution;
    // }
}
