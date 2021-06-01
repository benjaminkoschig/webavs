package globaz.apg.eformulaire;

import apg.amatapat.*;
import apg.pandemie.InsuredAddress;
import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.vo.donneeFinanciere.IbanCheckResultVO;
import ch.globaz.pegasus.businessimpl.services.process.allocationsNoel.AdressePaiementPrimeNoelService;
import ch.globaz.pyxis.business.model.*;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.pyxis.domaine.EtatCivil;
import globaz.apg.db.droits.APEmployeur;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.properties.APProperties;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.*;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
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

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Hashtable;
import java.util.List;
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

    protected final List<String> errors;
    protected final List<String> infos;
    protected final BSession bSession;
    private final String typeDemande;

    @Override
    public void createRoleApgTiers(String idTiers) throws Exception {
        TIRoleManager roleManager = new TIRoleManager();
        roleManager.setSession(bSession);
        roleManager.setForIdTiers(idTiers);
        BStatement statement = null;
        BTransaction trans = null;
        try {
            trans = (BTransaction) bSession.newTransaction();
            if(!trans.isOpened()){
                trans.openTransaction();
            }
            statement = roleManager.cursorOpen(bSession.getCurrentThreadTransaction());
            TIRole role;
            boolean isRolePresent = false;

            while ((role = (TIRole) roleManager.cursorReadNext(statement)) != null) {
                if (IntRole.ROLE_APG.equals(role.getRole())) {
                    isRolePresent = true;
                }
            }

            if (!isRolePresent) {
                // on ajoute le rôle APG au Tier si il ne l'a pas deja
                ITIRole newRole = (ITIRole) bSession.getAPIFor(ITIRole.class);
                newRole.setIdTiers(idTiers);
                newRole.setISession(PRSession.connectSession(bSession, TIApplication.DEFAULT_APPLICATION_PYXIS));
                newRole.setRole(IntRole.ROLE_APG);
                newRole.add(trans);
            }
            trans.commit();
        } catch (Exception e) {
            errors.add("Une erreur s'est produite dans la création du rôle du tiers : "+idTiers);
            LOG.error("ImportAPGAmatApat#creationRoleApgTiers - Une erreur s'est produite dans la création du rôle du tiers : {}", idTiers);
            if (trans != null) {
                trans.rollback();
            }
            if(isJadeThreadError()){
                addJadeThreadErrorToListError("Rôle");
                JadeThread.logClear();
            }
        } finally {
            try {
                if (statement != null) {
                    try {
                        roleManager.cursorClose(statement);
                    } finally {
                        statement.closeStatement();
                    }
                }
            } finally {
                if (trans != null) {
                    trans.closeTransaction();
                }
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


            if (!hasEmail(email, tiers)) {
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
                moyenCommunication.setIdApplication(typeDemande.equals(IPRDemande.CS_TYPE_PATERNITE)
                                                        ? APProperties.DOMAINE_ADRESSE_APG_PATERNITE.getValue()
                                                        : IPRConstantesExternes.TIERS_CS_DOMAINE_MATERNITE);
                moyenCommunication.add(trans);

                // Lien du contact avec le tiers
                TIAvoirContact avoirContact = new TIAvoirContact();
                avoirContact.setSession(bSession);
                avoirContact.setIdTiers(tiers.getIdTiers());
                avoirContact.setIdContact(contact.getIdContact());
                avoirContact.add(trans);
                trans.commit();
            } else {
                errors.add("Une erreur est survenue lors de la création du contact pour l'id tiers : " + tiers.getNSS() + " - " + email);
                LOG.error("APImportationAPGAmatApat#createContact : Une erreur est survenue lors de la création du contact pour l'id tiers {}", tiers.getNSS());
            }
        } catch (Exception e) {
            if(trans != null) {
                trans.rollback();
            }
            errors.add("Une erreur est survenue lors de la création du contact pour l'id tiers : " + tiers.getNSS() + " - " + email);
            LOG.error("APImportationAPGAmatApat#createContact : Une erreur est survenue lors de la création du contact pour l'id tiers {}", tiers.getNSS(), e);
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
                getSituationMarital(assure));

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
            errors.add("Erreur dans la création de la demande du droit (idTiers : " + idTiers + ")");
            LOG.error("Erreur lors de la création de la demande du droit ", e.getStackTrace());
            if (isJadeThreadError()) {
                addJadeThreadErrorToListError("Demande");
                JadeThread.logClear();
            }
        }
        return retValue;
    }

    @Override
    public void createSituationProfessionnel(Content content, String idDroit, BTransaction transaction) {
        switch (content.getFormType()) {
            case FORM_INDEPENDANT:
                creationSituationProIndependant(content, idDroit, transaction);
                break;
            case FORM_SALARIE:
                creationSituationProEmploye(content, idDroit, transaction);
                break;
            default:
                break;
        }
    }

    private String getSituationMarital(InsuredPerson assure){
        switch(assure.getMaritalStatus())
        {
            case "MARRIED":
                return String.valueOf(EtatCivil.MARIE.getCodeSysteme());
            case "DIVORCED":
                return String.valueOf(EtatCivil.DIVORCE.getCodeSysteme());
            case "WIDOW":
                return String.valueOf(EtatCivil.VEUF.getCodeSysteme());
            default:
                return String.valueOf(EtatCivil.CELIBATAIRE.getCodeSysteme());
        }
    }

    protected boolean hasEmail(String email, PRTiersWrapper tiers ) throws Exception {
        if (!JadeStringUtil.isBlankOrZero(email)) {
            TIAvoirContactManager avoirContactManager = new TIAvoirContactManager();
            avoirContactManager.setForIdTiers(tiers.getIdTiers());
            avoirContactManager.find(BManager.SIZE_NOLIMIT);

            if (!avoirContactManager.getContainer().isEmpty()) {
                for (int i = 0; i < avoirContactManager.getContainer().size(); i++) {
                    TIAvoirContact avoirContact = (TIAvoirContact) avoirContactManager.get(i);

                    TIMoyenCommunicationManager moyenCommunicationManager = new TIMoyenCommunicationManager();
                    moyenCommunicationManager.setForIdContact(avoirContact.getIdContact());
                    moyenCommunicationManager.setForMoyenLike(TIMoyenCommunication.EMAIL);

                    if (!moyenCommunicationManager.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void creationSituationProEmploye(Content content, String idDroit, BTransaction transaction) {
        Salary salaire = content.getProvidedByEmployer().getSalary();

        String salaireMensuel;
        MainEmployer mainEmployeur = content.getMainEmployer();

        try {
            AFAffiliation affiliation = findAffiliationByNumero(mainEmployeur.getAffiliateID(), bSession);
            if (affiliation != null) {
                APEmployeur emp = new APEmployeur();
                emp.setSession(bSession);
                emp.setIdTiers(affiliation.getIdTiers());
                emp.setIdAffilie(affiliation.getAffiliationId());
                emp.add(transaction);

                APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();
                situationProfessionnelle.setSession(bSession);
                situationProfessionnelle.setIdDroit(idDroit);
                situationProfessionnelle.setIdEmployeur(emp.getIdEmployeur());
                situationProfessionnelle.setIsIndependant(false);
                situationProfessionnelle.setIsVersementEmployeur(isVersementEmployeur(content));
                LastIncome lastInCome = salaire.getLastIncome();
                if(lastInCome != null) {
                    salaireMensuel = String.valueOf(lastInCome.getAmount());
                    if (salaireMensuel != null) {
                        situationProfessionnelle.setSalaireMensuel(salaireMensuel);
                        // Vague 2 - Si le salarié est payé sur 13 mois
                        // On ajoute son 13eme mois sans une autre rémunération annuelle
                        if (lastInCome.isHasThirteenthMonth()) {
                            situationProfessionnelle.setAutreRemuneration(salaireMensuel);
                            situationProfessionnelle.setPeriodiciteAutreRemun(IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE);
                        }
                    }
                }


                situationProfessionnelle.wantCallValidate(false);
                situationProfessionnelle.add(transaction);
            }
        } catch (Exception e) {
            infos.add("Erreur rencontré lors de la création de la situation professionnelle pour l'assuré. Aucune situation professionnelle ne sera créée pour ce droit.");
            LOG.error("APImportationAPGPandemie#creerSituationProf : Erreur rencontré lors de la création de la situation professionnelle pour l'assuré ", e);
            if (isJadeThreadError()) {
                addJadeThreadErrorToListError("Situation Professionnelle Employé");
                // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans l'ajout de la situation prof
                JadeThread.logClear();
            }
        }
    }

    private void creationSituationProIndependant(Content content, String idDroit, BTransaction transaction) {
        String masseAnnuel = "0";
        MainEmployer mainEmployeur = content.getMainEmployer();

        try {
            // TODO JJO 27.05.2021:  Controler dans pandémie la gestion des indépendants
            // TODO JJO 27.05.2021: Controler si l'affiliation est à récupérer via l'employeur ou le tiers.
            AFAffiliation affiliation = findAffiliationByNumero(mainEmployeur.getAffiliateID(), bSession);
            if (affiliation != null) {
                APEmployeur emp = new APEmployeur();
                emp.setSession(bSession);
                emp.setIdTiers(affiliation.getIdTiers());
                emp.setIdAffilie(affiliation.getAffiliationId());
                emp.add(transaction);

                // on cherche la decision
                final CPDecisionManager decision = new CPDecisionManager();
                BSession sessionPhenix = new BSession("PHENIX");
                bSession.connectSession(sessionPhenix);

                decision.setSession(sessionPhenix);
                decision.setForIdAffiliation(affiliation.getAffiliationId());
                decision.setForIsActive(true);
                decision.setForAnneeDecision(String.valueOf(Date.getCurrentYear() - 1));
                decision.find(BManager.SIZE_NOLIMIT);

                // on cherche les données calculées en fonction de la
                // decision
                if (decision.size() > 0) {

                    final ICPDonneesCalcul donneesCalcul = (ICPDonneesCalcul) bSession
                            .getAPIFor(ICPDonneesCalcul.class);
                    donneesCalcul.setISession(PRSession.connectSession(bSession, "PHENIX"));

                    final Hashtable<Object, Object> parms = new Hashtable<>();
                    parms.put(ICPDonneesCalcul.FIND_FOR_ID_DECISION, ((CPDecision) decision.getEntity(0)).getIdDecision());
                    parms.put(ICPDonneesCalcul.FIND_FOR_ID_DONNEES_CALCUL, ICPDonneesCalcul.CS_REV_NET);

                    final ICPDonneesCalcul[] donneesCalculs = donneesCalcul.findDonneesCalcul(parms);

                    if ((donneesCalculs != null) && (donneesCalculs.length > 0)) {
                        masseAnnuel = donneesCalculs[0].getMontant();
                    }
                }

                APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();
                situationProfessionnelle.setSession(bSession);
                situationProfessionnelle.setIdDroit(idDroit);
                situationProfessionnelle.setIdEmployeur(emp.getIdEmployeur());
                situationProfessionnelle.setIsIndependant(true);
                situationProfessionnelle.setIsVersementEmployeur(isVersementEmployeur(content));
                situationProfessionnelle.setRevenuIndependant(masseAnnuel);

                situationProfessionnelle.wantCallValidate(false);
                situationProfessionnelle.add(transaction);
            }
        } catch (Exception e) {
            infos.add("Erreur rencontré lors de la création de la situation professionnelle pour l'assuré. Aucune situation professionnelle ne sera créée pour ce droit.");
            LOG.error("APImportationAPGAmatApat#creerSituationProf : Erreur rencontré lors de la création de la situation professionnelle pour l'assuré ", e);
            if (isJadeThreadError()) {
                addJadeThreadErrorToListError("Situation Professionnelle Employé");
                // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans l'ajout de la situation prof
                JadeThread.logClear();
            }
        }
    }

    @Override
    public void createAdresses(PRTiersWrapper tiers, AddressType adresseAssure, PaymentContact adressePaiement, String npa) {
        try {
            String domaine = typeDemande.equals(IPRDemande.CS_TYPE_PATERNITE)
                    ? APProperties.DOMAINE_ADRESSE_APG_PATERNITE.getValue()
                    : IPRConstantesExternes.TIERS_CS_DOMAINE_MATERNITE;

            AdresseTiersDetail adresseCourrier = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(tiers.getIdTiers(), false, new Date().getSwissValue(), domaine,
                        CS_TYPE_COURRIER, "");
            AdresseComplexModel adresseDomicile = null;
            PersonneEtendueSearchComplexModel searchTiers = new PersonneEtendueSearchComplexModel();
            searchTiers.setForIdTiers(tiers.getIdTiers());
            PersonneEtendueSearchComplexModel personneEtendueSearch = TIBusinessServiceLocator.getPersonneEtendueService().find(searchTiers);

            if (personneEtendueSearch.getNbOfResultMatchingQuery() == 1) {
                if(adresseCourrier.getFields() == null) {
                    PersonneEtendueComplexModel personneEtendueComplexModel = (PersonneEtendueComplexModel) personneEtendueSearch.getSearchResults()[0];
                    adresseDomicile = createAdresseCourrier(personneEtendueComplexModel, adresseAssure, domaine, npa);
                    if (adresseDomicile != null && !adresseDomicile.isNew()) {
                        createAdressePaiement(adresseDomicile, tiers.getIdTiers(), adressePaiement, domaine);
                    }
                }
            }

        } catch (Exception e) {
            infos.add("Un problème a été rencontré lors de la création des adresses pour l'assuré suivant"+tiers.getNSS());
            LOG.error("APImportationAMAT-APAT#createAdresseAMAT-APAT : Erreur rencontré lors de la création adresses pour l'assuré", e);
        }
    }

    private AdresseComplexModel createAdresseCourrier(PersonneEtendueComplexModel personneEtendueComplexModel, AddressType adresseAssure, String domainePandemie, String npa) throws JadeApplicationException, JadePersistenceException {
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
        AdresseComplexModel adresse =  TIBusinessServiceLocator.getAdresseService().addAdresse(adresseComplexModel, domainePandemie,
                CS_TYPE_COURRIER, false);
        if(!isJadeThreadError()){
            return adresse;
        }else{
            addJadeThreadErrorToListError("Adresse Courrier");
            // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans l'adresse
            JadeThread.logClear();
            return null;
        }
    }

    private void createAdressePaiement(AdresseComplexModel adresseComplexModel, String idTiers, PaymentContact adressePaiementXml, String domaine) throws Exception {
        TIAdressePaiement adressePaiement = new TIAdressePaiement();
        adressePaiement.setIdTiersAdresse(idTiers);
        adressePaiement.setIdAdresse(adresseComplexModel.getAdresse().getId());

        if (!Objects.isNull(adressePaiementXml.getBankAccount().getIban())) {
            String iban = unformatIban(adressePaiementXml.getBankAccount().getIban());
            if(checkChIban(iban).getIsValidChIban()) {
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
            }else {
                infos.add("Paiement adresse non créée : IBAN non valide : " + iban);
            }
        }
        if(isJadeThreadError()){
            addJadeThreadErrorToListError("Adresse Paiement");
            // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans l'adresse
            JadeThread.logClear();
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
     * Ajout des erreurs blocantes
     * @param methodeSource: method that calls it.
     */
    protected void addJadeThreadErrorToListError(String methodeSource) {
        JadeBusinessMessage[] messages = JadeThread.logMessages();
        LOG.error("{}: ", methodeSource);
        errors.add("Concerne: "+methodeSource+"\n");
        for (int i = 0; (messages != null) && (i < messages.length); i++) {
            LOG.error("-->Erreur: {}", messages[i].getContents(null));
            errors.add("-->Erreur: "+messages[i].getContents(null));
        }
        errors.add("\n");
    }

    protected boolean isJadeThreadError(){
        if(JadeThread.logMessages() != null) {
            return JadeThread.logMessages().length > 0;
        }
        return false;
    }

    /**
     * Récupération de l'affiliation par son numéro.
     *
     * @param numeroAffiliate : le numéro d'affilié.
     * @return l'affiliation si elle a été trouvée.
     * @throws Exception: Exception pouvant être levée lors de la recherche par l' affiliation manager
     */
    protected AFAffiliation findAffiliationByNumero(String numeroAffiliate, BSession bsession) throws Exception {
        AFAffiliationManager manager = new AFAffiliationManager();
        manager.setSession(bsession);
        manager.setForAffilieNumero(numeroAffiliate);
        manager.find(BManager.SIZE_NOLIMIT);
        if (manager.size() > 0) {
            return (AFAffiliation) manager.getFirstEntity();
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
