package globaz.apg.eformulaire;

import apg.amatapat.*;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.util.JadeLogs;
import ch.globaz.pegasus.business.vo.donneeFinanciere.IbanCheckResultVO;
import ch.globaz.pegasus.businessimpl.services.process.allocationsNoel.AdressePaiementPrimeNoelService;
import ch.globaz.pyxis.business.model.*;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.pyxis.domaine.EtatCivil;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APEmployeur;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.properties.APProperties;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.osiris.external.IntRole;
import globaz.phenix.api.ICPDonneesCalcul;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.api.IPRSituationProfessionnelle;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.demandes.PRDemandeManager;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAVSUtils;
import globaz.prestation.tools.PRSession;
import globaz.pyxis.api.ITIPersonne;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressepaiement.TIAdressePaiement;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiement;
import globaz.pyxis.db.tiers.*;
import globaz.pyxis.util.TIIbanFormater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Objects;

import static ch.globaz.pyxis.business.services.AdresseService.CS_TYPE_COURRIER;

@Slf4j
@RequiredArgsConstructor
public abstract class APAbstractImportationAmatApat implements IAPImportationAmatApat {
    protected static final String FORM_INDEPENDANT = "FORM_INDEPENDANT";
    protected static final String FORM_SALARIE = "FORM_SALARIE";
    private static final String BENEFICIAIRE_MERE = "MERE";
    private static final String BENEFICIAIRE_PERE = "PERE";
    private static final String BENEFICIAIRE_EMPLOYEUR = "EMPLOYEUR";

    protected final APImportationStatusFile fileStatus;
    protected final BSession bSession;
    private final String typeDemande;
    protected final String nssImport;

    @Override
    public void createRoleApgTiers(String idTiers) throws Exception {
        TIRoleManager roleManager = new TIRoleManager();
        roleManager.setSession(bSession);
        roleManager.setForIdTiers(idTiers);
        BTransaction trans = null;
        try {
            trans = (BTransaction) bSession.newTransaction();
            if(!trans.isOpened()){
                trans.openTransaction();
            }
            roleManager.setForRole(IntRole.ROLE_APG);
            if (roleManager.getCount() == 0) {
                // on ajoute le rôle APG au Tier si il ne l'a pas deja
                ITIRole newRole = (ITIRole) bSession.getAPIFor(ITIRole.class);
                newRole.setIdTiers(idTiers);
                newRole.setISession(PRSession.connectSession(bSession, TIApplication.DEFAULT_APPLICATION_PYXIS));
                newRole.setRole(IntRole.ROLE_APG);
                newRole.add(trans);
                trans.commit();
            }

        } catch (Exception e) {
            fileStatus.addError("Une erreur s'est produite dans la création du rôle du tiers.");
            LOG.error("APAbstractImportationAmatApat#creationRoleApgTiers - Une erreur s'est produite dans la création du rôle du tiers : {}", idTiers);
            if (trans != null) {
                trans.rollback();
            }
            JadeLogs.logAndClear("createRoleApgTiers", LOG);
        } finally {

            if (trans != null) {
                trans.closeTransaction();
            }
        }
    }

    @Override
    public void createContact(PRTiersWrapper tiers, String email) throws Exception {
        BTransaction trans = null;
        try {
            trans = (BTransaction) bSession.newTransaction();
            if (!trans.isOpened()) {
                trans.openTransaction();
            }

            String dommaineApplicationId = IPRDemande.CS_TYPE_PATERNITE.equals(typeDemande)
                    ? APProperties.DOMAINE_ADRESSE_APG_PATERNITE.getValue()
                    : IPRConstantesExternes.TIERS_CS_DOMAINE_MATERNITE;
            if (!hasEmail(email, tiers, dommaineApplicationId)) {
                // Création du contact
                TIContact contact = new TIContact();
                contact.setSession(bSession);
                contact.setNom(tiers.getNom());
                contact.setPrenom(tiers.getPrenom());
                contact.add(trans);

                // Création du moyen de communication
                TIMoyenCommunication moyenCommunication = new TIMoyenCommunication();
                moyenCommunication.setSession(bSession);
                moyenCommunication.setMoyen(email);
                moyenCommunication.setTypeCommunication(TIMoyenCommunication.EMAIL);
                moyenCommunication.setIdContact(contact.getIdContact());
                moyenCommunication.setIdApplication(dommaineApplicationId);
                moyenCommunication.add(trans);

                // Lien du contact avec le tiers
                TIAvoirContact avoirContact = new TIAvoirContact();
                avoirContact.setSession(bSession);
                avoirContact.setIdTiers(tiers.getIdTiers());
                avoirContact.setIdContact(contact.getIdContact());
                avoirContact.add(trans);
                trans.commit();
            } else {
                fileStatus.addInformation("Une adresse mail a été trouvée pour le tiers dans WebAVS.");
            }
        } catch (Exception e) {
            if(trans != null) {
                trans.rollback();
            }
            fileStatus.addInformation("Une erreur est survenue lors de la création du contact pour ce tiers. Aucune adresse mail n'a été ajouté.");
            LOG.info("APAbstractImportationAmatApat#createContact - Une erreur est survenue lors de la création du contact pour l'id tiers {}", tiers.getNSS(), e);
            JadeLogs.logAndClear("createContact", LOG);
        }finally {
            if(trans != null) {
                trans.closeTransaction();
            }
        }
    }

    @Override
    public PRTiersWrapper createTiers(InsuredPerson assure, String codeNpa, boolean isWomen) throws Exception {

        // les noms et prenoms doivent être renseignés pour insérer un nouveau tiers
        if (JadeStringUtil.isEmpty(assure.getOfficialName()) || JadeStringUtil.isEmpty(assure.getFirstName())) {
            bSession.addError(bSession.getLabel("APAbstractDroitPHelper.ERREUR_NOM_OU_PRENOM_INVALIDE"));
            return null;
        }

        // date naissance obligatoire pour inserer
        if (JAUtil.isDateEmpty(tranformGregDateToGlobDate(assure.getDateOfBirth()))) {
            bSession.addError(bSession.getLabel("DATE_NAISSANCE_INCORRECTE"));
            return null;
        }

        // recherche du canton si le npa est renseigné
        String canton = "";
        if (!JadeStringUtil.isIntegerEmpty(codeNpa)) {
            try {
                canton = PRTiersHelper.getCanton(bSession, codeNpa);

                if (canton == null) {
                    // canton non trouvé
                    canton = "";
                }
            } catch (Exception e1) {
                bSession.addError(bSession.getLabel("CANTON_INTROUVABLE"));
            }
        }

        // insertion du tiers
        // si son numero AVS est suisse on lui met suisse comme pays, sinon on
        // lui met un pays bidon qu'on pourrait
        // interpreter comme "etranger"
        PRAVSUtils avsUtils = PRAVSUtils.getInstance(assure.getVn());

        String idTiers = PRTiersHelper.addTiers(bSession, assure.getVn(), assure.getOfficialName(),
                assure.getFirstName(), isWomen ? ITIPersonne.CS_FEMME : ITIPersonne.CS_HOMME, tranformGregDateToGlobDate(assure.getDateOfBirth()),
                "",
                avsUtils.isSuisse(assure.getVn()) ? PRTiersHelper.ID_PAYS_SUISSE : PRTiersHelper.ID_PAYS_BIDON, canton, "",
                convertSituationMaritale(assure));

        fileStatus.addInformation("Le tiers étant inexistant dans WebAVS, il a été ajouté.");

        return PRTiersHelper.getTiersParId(bSession, idTiers);
    }

    @Override
    public PRDemande createDemande(String idTiers) {
        PRDemande retValue = null;
        try {
            PRDemandeManager mgr = new PRDemandeManager();
            mgr.setSession(bSession);
            mgr.setForIdTiers(JadeStringUtil.isEmpty(idTiers) ? PRDemande.ID_TIERS_DEMANDE_BIDON : idTiers);
            mgr.setForTypeDemande(typeDemande);
            mgr.find(BManager.SIZE_NOLIMIT);

            if (mgr.isEmpty()) {
                retValue = new PRDemande();
                retValue.setIdTiers(idTiers);
                retValue.setEtat(IPRDemande.CS_ETAT_OUVERT);
                retValue.setTypeDemande(typeDemande);
                retValue.setSession(bSession);
                retValue.add();
            } else if (!mgr.isEmpty()) {
                retValue = (PRDemande) mgr.get(0);
            }
        } catch (Exception e) {
            fileStatus.addError("Erreur dans la création de la demande du droit de ce tiers.");
            LOG.error("APAbstractImportationAmatApat#createDemande - Erreur lors de la création de la demande du droit ", e);
            JadeLogs.logAndClear("createDemande", LOG);
        }
        return retValue;
    }

    @Override
    public void createSituationProfessionnel(Content content, APDroitLAPG droit, BTransaction transaction) {
        if(FORM_INDEPENDANT.equals(content.getFormType())){
            creationSituationProIndependant(content, droit, transaction);
        }else if(FORM_SALARIE.equals(content.getFormType())){
            creationSituationProEmploye(content, droit, transaction);
        }else{
            fileStatus.addInformation("La situation professionnelle n'a pas été ajouté. Le type de situation professionnelle non défini.");
            LOG.error("APAbstractImportationAmatApat#createSituationProfessionnelle - La situation professionnelle n'a pas été ajouté. Le type de situation professionnelle non défini.");
        }
    }

    private String convertSituationMaritale(InsuredPerson assure){
        switch(assure.getMaritalStatus())
        {
            case "MARRIED":
                return String.valueOf(EtatCivil.MARIE.getCodeSysteme());
            case "DIVORCED":
                return String.valueOf(EtatCivil.DIVORCE.getCodeSysteme());
            case "WIDOW":
                return String.valueOf(EtatCivil.VEUF.getCodeSysteme());
            case "SINGLE":
                return String.valueOf(EtatCivil.CELIBATAIRE.getCodeSysteme());
            default:
                return StringUtils.EMPTY;
        }
    }

    protected boolean hasEmail(String email, PRTiersWrapper tiers, String domaineApplicationId) throws Exception {
        if (!JadeStringUtil.isBlankOrZero(email)) {
            TIAvoirContactManager avoirContactManager = new TIAvoirContactManager();
            avoirContactManager.setForIdTiers(tiers.getIdTiers());
            avoirContactManager.find(BManager.SIZE_NOLIMIT);

            if (!avoirContactManager.getContainer().isEmpty()) {
                for (int i = 0; i < avoirContactManager.getContainer().size(); i++) {
                    TIAvoirContact avoirContact = (TIAvoirContact) avoirContactManager.get(i);

                    TIMoyenCommunicationManager moyenCommunicationManager = new TIMoyenCommunicationManager();
                    moyenCommunicationManager.setForIdContact(avoirContact.getIdContact());
                    moyenCommunicationManager.setForTypeCommunication(TIMoyenCommunication.EMAIL);
                    moyenCommunicationManager.setForIdApplication(domaineApplicationId);

                    if (moyenCommunicationManager.getCount() > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void creationSituationProEmploye(Content content, APDroitLAPG droit, BTransaction transaction) {
        Salary salaire = content.getProvidedByEmployer().getSalary();
        MainEmployer mainEmployeur = content.getMainEmployer();

        try {
            AFAffiliation affiliation = findAffiliationByNumero(mainEmployeur.getAffiliateID(), droit,false, bSession);
            if (affiliation != null) {
                APEmployeur emp = createEmployeur(affiliation, transaction);
                APSituationProfessionnelle situationProfessionnelle = createSituationProfessionnelle(droit.getIdDroit(), emp.getIdEmployeur(), content);
                situationProfessionnelle.setIsIndependant(false);
                setIncomes(salaire, situationProfessionnelle);
                situationProfessionnelle.wantCallValidate(false);
                situationProfessionnelle.add(transaction);
                String message = "La situation professionelle a été ajouté pour le tiers dans WebAVS.";
                fileStatus.addInformation(message);
                LOG.info(message);
            } else{
                fileStatus.addInformation("Le N° d'affilié de l'assuré n'a pas été trouvé. La situation professionelle n'a pas été ajouté pour le tiers dans WebAVS.");
                LOG.info("Le N° d'affilié dde l'assuré n'a pas été trouvé. La situation professionelle n'a pas été ajouté pour le tiers dans WebAVS.");
            }
        } catch (Exception e) {
            fileStatus.addInformation("Les données relatives à la situation professionnelle pour cette assuré ne sont pas valides. Aucune situation professionnelle ne sera créée pour ce droit.");
            LOG.error("APAbstractImportationAmatApat#creerSituationProEmploye - Erreur rencontré lors de la création de la situation professionnelle pour l'assuré ", e);
            JadeLogs.logAndClear("creationSituationProEmploye", LOG);
        }
    }

    private void setIncomes(Salary salaire, APSituationProfessionnelle situationProfessionnelle) {
        setLastAndOtherIncome(salaire, situationProfessionnelle);
        setHourlyIncome(salaire, situationProfessionnelle);
        setFourWeekIncome(salaire, situationProfessionnelle);
        setInKindOrGlobalIncome(salaire, situationProfessionnelle);
    }

    private void setLastAndOtherIncome(Salary salaire, APSituationProfessionnelle situationProfessionnelle) {
        LastIncome lastInCome = salaire.getLastIncome();
        BigDecimal autreRemuneration = new BigDecimal(0);
        String autreRemunerationPeriod = null;
        if(lastInCome != null && lastInCome.getAmount() != null) {
            situationProfessionnelle.setSalaireMensuel(String.valueOf(lastInCome.getAmount()));
            if (lastInCome.isHasThirteenthMonth()) {
                autreRemuneration = autreRemuneration.add(lastInCome.getAmount());
                autreRemunerationPeriod = convertPeriodicite("ANNEE");
            }
        }
        OtherIncome otherIncome = salaire.getOtherIncome();
        if(otherIncome != null && otherIncome.getAmount() != null) {
            autreRemuneration = autreRemuneration.add(otherIncome.getAmount());
            if (otherIncome.getIncomeUnit() != null) {
                autreRemunerationPeriod = convertPeriodicite(otherIncome.getIncomeUnit());
            }
        }
        if(autreRemuneration.doubleValue() > 0){
            situationProfessionnelle.setAutreRemuneration(autreRemuneration.toPlainString());
            if(autreRemunerationPeriod != null){
                situationProfessionnelle.setPeriodiciteAutreRemun(autreRemunerationPeriod);
            }
        }
    }

    private void setFourWeekIncome(Salary salaire, APSituationProfessionnelle situationProfessionnelle) {
        FourWeekIncome fourWeekIncome = salaire.getFourWeekIncome();
        if(fourWeekIncome != null && fourWeekIncome.getAmount() != null) {
            situationProfessionnelle.setAutreSalaire(String.valueOf(fourWeekIncome.getAmount()));
            if (fourWeekIncome.getIncomeUnit() != null) {
                situationProfessionnelle.setPeriodiciteAutreSalaire(convertPeriodicite(fourWeekIncome.getIncomeUnit()));
            }
        }
    }

    private void setInKindOrGlobalIncome(Salary salaire, APSituationProfessionnelle situationProfessionnelle) {
        InKindOrGlobalIncome inKindOrGlobalIncome = salaire.getInKindOrGlobalIncome();
        if(inKindOrGlobalIncome != null && inKindOrGlobalIncome.getAmount() != null) {
            situationProfessionnelle.setSalaireNature(String.valueOf(inKindOrGlobalIncome.getAmount()));
            if (inKindOrGlobalIncome.getIncomeUnit() != null) {
                situationProfessionnelle.setPeriodiciteSalaireNature(convertPeriodicite(inKindOrGlobalIncome.getIncomeUnit()));
            }
        }
    }

    private void setHourlyIncome(Salary salaire, APSituationProfessionnelle situationProfessionnelle) {
        HourlyIncome hourlyIncome = salaire.getHourlyIncome();
        if(hourlyIncome != null && hourlyIncome.getAmount() != null) {
            String salaireHoraire = String.valueOf(hourlyIncome.getAmount());
            if (salaireHoraire != null) {
                situationProfessionnelle.setSalaireHoraire(salaireHoraire);
            }
            situationProfessionnelle.setHeuresSemaine(String.valueOf(hourlyIncome.getHoursOfWorkPerWeek()));
        }
    }

    private String convertPeriodicite(String periodicite) {
        switch (periodicite) {
            case "HEURE":
                return IPRSituationProfessionnelle.CS_PERIODICITE_HEURE;
            case "MOIS":
                return IPRSituationProfessionnelle.CS_PERIODICITE_MOIS;
            case "QUATRE_SEMAINE":
                return IPRSituationProfessionnelle.CS_PERIODICITE_4_SEMAINES;
            case "ANNEE":
                return IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE;
            default:
                return StringUtils.EMPTY;
        }
    }

    private void creationSituationProIndependant(Content content, APDroitLAPG droit, BTransaction transaction) {
        String message;
        String affilieId = null;
        try {
            Activities activites = content.getActivities();
            if(activites != null && !activites.getActivity().isEmpty() && activites.getActivity().get(0).getCompanies() != null &&
                    !activites.getActivity().get(0).getCompanies().getCompany().isEmpty()){
                affilieId = activites.getActivity().get(0).getCompanies().getCompany().get(0).getAffiliateID();
            }
            if(StringUtils.isEmpty(affilieId)) {
                MainEmployer mainEmployeur = content.getMainEmployer();
                affilieId = mainEmployeur.getAffiliateID();
            }
            if(StringUtils.isNotEmpty(affilieId)) {
                AFAffiliation affiliation = findAffiliationByNumero(affilieId, droit, true, bSession);
                if (affiliation != null) {
                    APEmployeur emp = createEmployeur(affiliation, transaction);
                    APSituationProfessionnelle situationProfessionnelle = createSituationProfessionnelle(droit.getIdDroit(), emp.getIdEmployeur(), content);
                    situationProfessionnelle.setIsIndependant(true);
                    situationProfessionnelle.setRevenuIndependant(getMasseAnnuelle(affiliation));
                    situationProfessionnelle.wantCallValidate(false);
                    situationProfessionnelle.add(transaction);
                    message = "La situation professionelle a été ajouté pour le tiers dans WebAVS.";
                } else {
                    message = "Le N° d'affilié de l'employeur n'a pas été trouvé. La situation professionelle n'a pas été ajouté pour le tiers dans WebAVS.";
                }
            }
            else{
                message = "Le N° d'affilié de l'employeur n'a pas été trouvé. La situation professionelle n'a pas été ajouté pour le tiers dans WebAVS.";
            }
            fileStatus.addInformation(message);
            LOG.info(message);
        } catch (Exception e) {
            fileStatus.addInformation("Les données relatives à la situation professionnelle pour cet assuré ne sont pas valides. Aucune situation professionnelle ne sera créée pour ce droit.");
            LOG.error("APAbstractImportationAmatApat#creerSituationProfIndependant - Erreur rencontré lors de la création de la situation professionnelle pour l'assuré ", e);
            JadeLogs.logAndClear("creationSituationProIndependant", LOG);
        }
    }

    private APSituationProfessionnelle createSituationProfessionnelle(String idDroit, String idEmployeur, Content content){
        APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();
        situationProfessionnelle.setSession(bSession);
        situationProfessionnelle.setIdDroit(idDroit);
        situationProfessionnelle.setIdEmployeur(idEmployeur);
        situationProfessionnelle.setIsVersementEmployeur(isVersementEmployeur(content));

        return situationProfessionnelle;
    }

    /**
     * Créer un employeur ayant une relation 1 à 1 avec la situation professionnelle
     * @param affiliation Affilication lié à l'employeur
     * @param transaction Transaction pour ajouter l'employeur
     * @return L'employeur
     * @throws Exception
     */
    private APEmployeur createEmployeur(AFAffiliation affiliation, BTransaction transaction) throws Exception{
        APEmployeur emp = new APEmployeur();
        emp.setSession(bSession);
        emp.setIdTiers(affiliation.getIdTiers());
        emp.setIdAffilie(affiliation.getAffiliationId());
        emp.add(transaction);
        return emp;
    }

    /**
     * retrouver la masse annuelle dans les cotisations personnelles.
     * @param affiliation: Affiliation correspondant au demandeur indépendant
     * @return Masse annuelle basée sur les cotisations personnelles
     */
    private String getMasseAnnuelle(AFAffiliation affiliation) throws Exception {
        final CPDecisionManager decision = new CPDecisionManager();
        BSession sessionPhenix = new BSession("PHENIX");
        bSession.connectSession(sessionPhenix);

        decision.setSession(sessionPhenix);
        decision.setForIdAffiliation(affiliation.getAffiliationId());
        decision.setForIsActive(true);
        decision.setForAnneeDecision(String.valueOf(Date.getCurrentYear() - 1));
        decision.find(BManager.SIZE_NOLIMIT);

        if (decision.size() > 0) {

            final ICPDonneesCalcul donneesCalcul = (ICPDonneesCalcul) bSession
                    .getAPIFor(ICPDonneesCalcul.class);
            donneesCalcul.setISession(PRSession.connectSession(bSession, "PHENIX"));

            final Hashtable<Object, Object> parms = new Hashtable<>();
            parms.put(ICPDonneesCalcul.FIND_FOR_ID_DECISION, ((CPDecision) decision.getEntity(0)).getIdDecision());
            parms.put(ICPDonneesCalcul.FIND_FOR_ID_DONNEES_CALCUL, ICPDonneesCalcul.CS_REV_NET);

            final ICPDonneesCalcul[] donneesCalculs = donneesCalcul.findDonneesCalcul(parms);

            if ((donneesCalculs != null) && (donneesCalculs.length > 0)) {
                return donneesCalculs[0].getMontant();
            }
        }
        return "0";
    }

    @Override
    public void createAdresses(PRTiersWrapper tiers, AddressType adresseAssure, PaymentContact adressePaiement, String npa) {
        try {
            String domaine = IPRDemande.CS_TYPE_PATERNITE.equals(typeDemande)
                    ? APProperties.DOMAINE_ADRESSE_APG_PATERNITE.getValue()
                    : IPRConstantesExternes.TIERS_CS_DOMAINE_MATERNITE;

            AdresseTiersDetail adresseCourrier = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(tiers.getIdTiers(), false, new Date().getSwissValue(), domaine,
                        CS_TYPE_COURRIER, "");
            AdresseComplexModel adresseDomicile;
            PersonneEtendueSearchComplexModel searchTiers = new PersonneEtendueSearchComplexModel();
            searchTiers.setForIdTiers(tiers.getIdTiers());
            PersonneEtendueSearchComplexModel personneEtendueSearch = TIBusinessServiceLocator.getPersonneEtendueService().find(searchTiers);

            if (personneEtendueSearch.getNbOfResultMatchingQuery() == 1 && adresseCourrier.getFields() == null) {
                PersonneEtendueComplexModel personneEtendueComplexModel = (PersonneEtendueComplexModel) personneEtendueSearch.getSearchResults()[0];
                adresseDomicile = createAdresseCourrier(personneEtendueComplexModel, adresseAssure, domaine, npa);
                if (adresseDomicile != null && !adresseDomicile.isNew()) {
                    createAdressePaiement(adresseDomicile, tiers.getIdTiers(), adressePaiement, domaine);
                }
            }
        } catch (Exception e) {
            fileStatus.addInformation("Un problème a été rencontré lors de la création des adresses pour cet assuré.");
            LOG.error("APAbstractImportationAmatApat#createAdresses - Erreur rencontré lors de la création adresses pour l'assuré", e);
            JadeLogs.logAndClear("createAdresses", LOG);
        }
    }

    private AdresseComplexModel createAdresseCourrier(PersonneEtendueComplexModel personneEtendueComplexModel, AddressType adresseAssure, String domainePandemie, String npa) {
        try {
            personneEtendueComplexModel.getTiers();
            AdresseComplexModel adresseComplexModel = new AdresseComplexModel();
            adresseComplexModel.setTiers(personneEtendueComplexModel);
            adresseComplexModel.getAvoirAdresse().setDateDebutRelation(Date.now().getSwissValue());
            adresseComplexModel.getTiers().setId(personneEtendueComplexModel.getTiers().getId());
            adresseComplexModel.getLocalite().setNumPostal(npa);
            adresseComplexModel.getAdresse().setRue(adresseAssure.getStreetWithNr());

            // Bug Fix - Cette correction permet de corriger un bug lorsque la LigneAdresse est laissé à null
            // Le risque de réutiliser une adresse identique avec un complément d'adresse qui n'a rien à voir
            // une correction sera effectuée sur Pyxis par la suite.
            // Mise à chaine vide des champs ligneAdresse
            adresseComplexModel.getAdresse().setLigneAdresse1("");
            adresseComplexModel.getAdresse().setLigneAdresse2("");
            adresseComplexModel.getAdresse().setLigneAdresse3("");
            adresseComplexModel.getAdresse().setLigneAdresse4("");
            AdresseComplexModel adresse = TIBusinessServiceLocator.getAdresseService().addAdresse(adresseComplexModel, domainePandemie,
                    CS_TYPE_COURRIER, false);
            fileStatus.addInformation("Une nouvelle adresse de courrier a été ajoutée pour ce tiers dans WebAVS.");
            return adresse;
        } catch (Exception e) {
            fileStatus.addInformation("Un problème a été rencontré lors de la création de l'adresse de courrier pour cet assuré.");
            LOG.error("APAbstractImportationAmatApat#createAdresseCourrier - Erreur rencontré lors de la création de l'adresse de courrier pour l'assuré", e);
            JadeLogs.logAndClear("createAdresseCourrier", LOG);
            return null;
        }
    }

    private void createAdressePaiement(AdresseComplexModel adresseComplexModel, String idTiers, PaymentContact adressePaiementXml, String domaine) {
        try {
            TIAdressePaiement adressePaiement = new TIAdressePaiement();
            adressePaiement.setIdTiersAdresse(idTiers);
            adressePaiement.setIdAdresse(adresseComplexModel.getAdresse().getId());

            if (!Objects.isNull(adressePaiementXml.getBankAccount().getIban())) {
                String iban = unformatIban(adressePaiementXml.getBankAccount().getIban());
                if (checkChIban(iban).getIsValidChIban().booleanValue()) {
                    adressePaiement.setIdTiersBanque(retrieveBanque(iban).getTiersBanque().getId());
                    adressePaiement.setCode(IConstantes.CS_ADRESSE_PAIEMENT_IBAN_OK);
                    adressePaiement.setNumCompteBancaire(adressePaiementXml.getBankAccount().getIban());

                    adressePaiement.setIdMonnaie(AdressePaiementPrimeNoelService.CS_CODE_MONNAIE_FRANC_SUISSE);
                    adressePaiement.setIdPays(AdressePaiementPrimeNoelService.CS_CODE_PAYS_SUISSE);
                    adressePaiement.setSession(bSession);
                    adressePaiement.add();

                    TIAvoirPaiement avoirPaiement = new TIAvoirPaiement();
                    avoirPaiement.setIdApplication(domaine);
                    avoirPaiement.setIdAdressePaiement(adressePaiement.getIdAdressePaiement());
                    avoirPaiement.setDateDebutRelation(Date.now().getSwissValue());
                    avoirPaiement.setIdTiers(idTiers);
                    avoirPaiement.setSession(bSession);
                    avoirPaiement.add();
                    fileStatus.addInformation("Une nouvelle adresse de paiement a été ajoutée pour ce tiers dans WebAVS.");
                } else {
                    fileStatus.addInformation("Paiement adresse non créée : IBAN non valide : " + iban);
                }
            }
        } catch (Exception e) {
            fileStatus.addInformation("Un problème a été rencontré lors de la création de l'adresse de paiement pour cet assuré.");
            LOG.error("APAbstractImportationAmatApat#createAdressePaiement - Erreur rencontré lors de la création de l'adresse de paiement pour l'assuré", e);
            JadeLogs.logAndClear("createAdressePaiement", LOG);
        }
    }

    private String unformatIban(String iban) {
        TIIbanFormater ibanFormatter = new TIIbanFormater();
        return  ibanFormatter.unformat(iban);
    }

    private BanqueComplexModel retrieveBanque(String iban) throws JadeApplicationException, JadePersistenceException {
        BanqueComplexModel banque = new BanqueComplexModel();
        String noClearing = iban.substring(4, 9);

        BanqueSearchComplexModel banqueSearchModel = new BanqueSearchComplexModel();
        banqueSearchModel.setForClearing(noClearing);
        banqueSearchModel.setDefinedSearchSize(1);
        banqueSearchModel = TIBusinessServiceLocator.getBanqueService().find(banqueSearchModel);

        if (banqueSearchModel.getSize() == 1) {
            banque = (BanqueComplexModel) banqueSearchModel.getSearchResults()[0];
        }
        return banque;
    }

    public IbanCheckResultVO checkChIban(String chIban) throws JadeApplicationException {

        IbanCheckResultVO result = new IbanCheckResultVO();

        TIIbanFormater ibanFormatter = new TIIbanFormater();

        chIban = ibanFormatter.unformat(chIban);

        if ((!JadeStringUtil.isEmpty(chIban)) && chIban.startsWith("CH")) {

            result.setIsCheckable(Boolean.TRUE);

            // si l'iban est checkable, on le format meme si il n'est pas valide. ceci pour faciliter la correction
            result.setFormattedIban(ibanFormatter.format(chIban));

            if (TIBusinessServiceLocator.getIBANService().checkIBANforCH(chIban)) {
                result.setIsValidChIban(Boolean.TRUE);
            }
        }

        return result;
    }

    private boolean isVersementEmployeur(Content content){
        switch (content.getPaymentContact().getBeneficiaryType()) {
            case BENEFICIAIRE_EMPLOYEUR:
                return true;
            case BENEFICIAIRE_MERE:
            case BENEFICIAIRE_PERE:
            default:
                return false;
        }
    }

    /**
     * Formattage de la date XMLGregorianCalendar en String.
     *
     * @param date la date au format XMLGregorianCalendar
     * @return la date formatté en String
     */
    protected String tranformGregDateToGlobDate(XMLGregorianCalendar date){
        if (Objects.nonNull(date)){
            return JadeDateUtil.getGlobazFormattedDate(date.toGregorianCalendar().getTime());
        } else {
            return "";
        }

    }

    /**
     * Récupération de l'affiliation par son numéro.
     *
     * @param numeroAffiliate : le numéro d'affilié.
     * @return l'affiliation si elle a été trouvée.
     * @throws Exception: Exception pouvant être levée lors de la recherche par l' affiliation manager
     */
    protected AFAffiliation findAffiliationByNumero(String numeroAffiliate, APDroitLAPG droit, boolean isIndependant, BSession bsession) throws Exception {
        AFAffiliationManager manager = new AFAffiliationManager();
        manager.setSession(bsession);
        manager.setForAffilieNumero(numeroAffiliate);
        String[] typeAffiliations;
        if(isIndependant){
            typeAffiliations = new String[]{IAFAffiliation.TYPE_AFFILI_INDEP, IAFAffiliation.TYPE_AFFILI_INDEP_EMPLOY};
        } else {
             typeAffiliations = new String[] {IAFAffiliation.TYPE_AFFILI_EMPLOY};
        }

        manager.setForTypeAffiliation(typeAffiliations);
        manager.setForDateFin(StringUtils.EMPTY);
        manager.find(BManager.SIZE_NOLIMIT);
        if (manager.size() > 0) {
            for (Object o : manager.getContainer()) {
                AFAffiliation affiliation = (AFAffiliation) o;
                final boolean dateDebutDroitGreaterOrEqualDateDebutApg = BSessionUtil
                        .compareDateFirstGreaterOrEqual(bsession, droit.getDateDebutDroit(), affiliation.getDateDebut());
                final boolean dateDebutDroitLowerOrEqualDateFinApg = BSessionUtil
                        .compareDateFirstLowerOrEqual(bsession, droit.getDateDebutDroit(), affiliation.getDateFin());
                // si l'affiliation est en cours
                if (dateDebutDroitGreaterOrEqualDateDebutApg && (dateDebutDroitLowerOrEqualDateFinApg
                        || JadeStringUtil.isEmpty(affiliation.getDateFin()))) {
                    return affiliation;
                }
            }
        }
        return null;
    }

    protected boolean isSoumisImpotSource(Content content){
        if (Objects.nonNull(content.getProvidedByEmployer()) && Objects.nonNull(content.getProvidedByEmployer().getSalary())) {
            return content.getProvidedByEmployer().getSalary().isWithholdingTax();
        }
        return false;
    }

    protected java.util.Date getDateNaissanceDernierNee(Content content){
        Children children = content.getFamilyMembers().getChildren();
        java.util.Date dateNaissance = null;
        for (Child child : children.getChild()) {
            java.util.Date newDateNaissance = child.getDateOfBirth().toGregorianCalendar().getTime();
            if (Objects.isNull(dateNaissance)) {
                dateNaissance = newDateNaissance;
            } else {
                if (newDateNaissance.after(dateNaissance)) {
                    dateNaissance = newDateNaissance;
                }
            }
        }
        return dateNaissance;
    }

    protected String getIdPays(String codeIso) throws JadePersistenceException {
        PaysSearchSimpleModel searchModel = new PaysSearchSimpleModel();
        searchModel.setForCodeIso(codeIso);
        JadePersistenceManager.search(searchModel);
        String id = null;
        for (JadeAbstractModel monModel : searchModel.getSearchResults()) {
            PaysSimpleModel simpleModel = (PaysSimpleModel) monModel;
            id = simpleModel.getId();
        }
        return id;
    }
}
