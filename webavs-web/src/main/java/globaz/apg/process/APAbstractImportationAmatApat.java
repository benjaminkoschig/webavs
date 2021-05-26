package globaz.apg.process;

import apg.amatapat.*;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pyxis.business.model.PaysSearchSimpleModel;
import ch.globaz.pyxis.business.model.PaysSimpleModel;
import ch.globaz.pyxis.domaine.EtatCivil;
import globaz.apg.db.droits.APEmployeur;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.properties.APProperties;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
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
import globaz.pyxis.db.tiers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Objects;

public abstract class APAbstractImportationAmatApat implements IAPImportationAmatApat {

    protected LinkedList<String> errors;
    protected LinkedList<String> infos;
    private static final Logger LOG = LoggerFactory.getLogger(APAbstractImportationAmatApat.class);
    protected String typeDemande;
    protected static final String FORM_INDEPENDANT = "FORM_INDEPENDANT";
    protected static final String FORM_SALARIE = "FORM_SALARIE";
    private static final String BENEFICIAIRE_MERE = "MERE";
    private static final String BENEFICIAIRE_PERE = "PERE";
    private static final String BENEFICIAIRE_EMPLOYEUR = "EMPLOYEUR";

    protected APAbstractImportationAmatApat(LinkedList<String> err, LinkedList<String> inf){
        errors = err;
        infos = inf;
    }

    @Override
    public void createRoleApgTiers(String idTiers, BSession bsession) throws Exception {
        TIRoleManager roleManager = new TIRoleManager();
        roleManager.setSession(bsession);
        roleManager.setForIdTiers(idTiers);
        BStatement statement = null;
        BTransaction trans = null;
        try {
            trans = (BTransaction) bsession.newTransaction();
            if(!trans.isOpened()){
                trans.openTransaction();
            }
            statement = roleManager.cursorOpen(bsession.getCurrentThreadTransaction());
            TIRole role;
            boolean isRolePresent = false;

            while ((role = (TIRole) roleManager.cursorReadNext(statement)) != null) {
                if (IntRole.ROLE_APG.equals(role.getRole())) {
                    isRolePresent = true;
                }
            }

            if (!isRolePresent) {
                // on ajoute le rôle APG au Tier si il ne l'a pas deja
                ITIRole newRole = (ITIRole) bsession.getAPIFor(ITIRole.class);
                newRole.setIdTiers(idTiers);
                newRole.setISession(PRSession.connectSession(bsession, TIApplication.DEFAULT_APPLICATION_PYXIS));
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
    public void createContact(PRTiersWrapper tiers, String email, BSession bsession) throws Exception {
        BTransaction trans = null;
        try {
            trans = (BTransaction) bsession.newTransaction();
            if (!trans.isOpened()) {
                trans.openTransaction();
            }


            if (!hasEmail(email, tiers)) {
                // Création du contact
                TIContact contact = new TIContact();
                contact.setSession(bsession);
                contact.setNom(tiers.getNom());
                contact.setPrenom(tiers.getPrenom());
                contact.add(trans);

                // Création du moyen de communication
                TIMoyenCommunication moyenCommunication = new TIMoyenCommunication();
                moyenCommunication.setSession(bsession);
                moyenCommunication.setMoyen(email);
                moyenCommunication.setTypeCommunication(TIMoyenCommunication.EMAIL);
                moyenCommunication.setIdContact(contact.getIdContact());
                moyenCommunication.setIdApplication(typeDemande.equals(IPRDemande.CS_TYPE_PATERNITE)
                                                        ? APProperties.DOMAINE_ADRESSE_APG_PATERNITE.getValue()
                                                        : IPRConstantesExternes.TIERS_CS_DOMAINE_MATERNITE);
                moyenCommunication.add(trans);

                // Lien du contact avec le tiers
                TIAvoirContact avoirContact = new TIAvoirContact();
                avoirContact.setSession(bsession);
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

    @Override
    public PRTiersWrapper createTiers(InsuredPerson assure, String codeNpa, BSession bsession, boolean isWomen) throws Exception {

        // les noms et prenoms doivent être renseignés pour insérer un nouveau tiers
        if (JadeStringUtil.isEmpty(assure.getOfficialName()) || JadeStringUtil.isEmpty(assure.getFirstName())) {
            bsession.addError(bsession.getLabel("APAbstractDroitPHelper.ERREUR_NOM_OU_PRENOM_INVALIDE"));
            return null;
        }

        // date naissance obligatoire pour inserer
        if (JAUtil.isDateEmpty(tranformGregDateToGlobDate(assure.getDateOfBirth()))) {
            bsession.addError(bsession.getLabel("DATE_NAISSANCE_INCORRECTE"));
            return null;
        }

        // recherche du canton si le npa est renseigné
        String canton = "";
        if (!JadeStringUtil.isIntegerEmpty(codeNpa)) {
            try {
                canton = PRTiersHelper.getCanton(bsession, codeNpa);

                if (canton == null) {
                    // canton non trouvé
                    canton = "";
                }
            } catch (Exception e1) {
                bsession.addError(bsession.getLabel("CANTON_INTROUVABLE"));
            }
        }

        // insertion du tiers
        // si son numero AVS est suisse on lui met suisse comme pays, sinon on
        // lui met un pays bidon qu'on pourrait
        // interpreter comme "etranger"
        PRAVSUtils avsUtils = PRAVSUtils.getInstance(assure.getVn());

        String idTiers = PRTiersHelper.addTiers(bsession, assure.getVn(), assure.getOfficialName(),
                assure.getFirstName(), isWomen ? ITIPersonne.CS_FEMME : ITIPersonne.CS_HOMME, tranformGregDateToGlobDate(assure.getDateOfBirth()),
                "",
                avsUtils.isSuisse(assure.getVn()) ? PRTiersHelper.ID_PAYS_SUISSE : PRTiersHelper.ID_PAYS_BIDON, canton, "",
                getSituationMarital(assure));

        return PRTiersHelper.getTiersParId(bsession, idTiers);
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

    @Override
    public PRDemande createDemande(String idTiers, BSession bsession) {
        PRDemande retValue = null;
        try {
            PRDemandeManager mgr = new PRDemandeManager();
            mgr.setSession(bsession);
            mgr.setForIdTiers(JadeStringUtil.isEmpty(idTiers) ? PRDemande.ID_TIERS_DEMANDE_BIDON : idTiers);
            mgr.setForTypeDemande(typeDemande);
            mgr.find(BManager.SIZE_NOLIMIT);

            if (mgr.isEmpty()) {
                retValue = new PRDemande();
                retValue.setIdTiers(idTiers);
                retValue.setEtat(IPRDemande.CS_ETAT_OUVERT);
                retValue.setTypeDemande(typeDemande);
                retValue.setSession(bsession);
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

    /**
     * Création de l'ID de la caisse lié au droit.
     *
     * @return l'id de la caisse.
     */
    protected String creationIdCaisse() {
        try {
            final String noCaisse = CommonProperties.KEY_NO_CAISSE.getValue();
            final String noAgence = CommonProperties.NUMERO_AGENCE.getValue();
            return noCaisse + noAgence;
        } catch (final PropertiesException exception) {
            errors.add("Impossible de récupérer les propriétés n° caisse et n° agence");
            LOG.error("APImportationAPGAmatApat#creationIdCaisse : A fatal exception was thrown when accessing to the CommonProperties", exception);
        }
        return null;
    }

    @Override
    public void createSituationProfessionnel(Content content, String idDroit, BTransaction transaction, BSession bsession) {
        switch (content.getFormType()) {
            case FORM_INDEPENDANT:
                creationSituationProIndependant(content, idDroit, transaction, bsession);
                break;
            case FORM_SALARIE:
                creationSituationProEmploye(content, idDroit, transaction, bsession);
                break;
            default:
                break;
        }
    }

    private void creationSituationProEmploye(Content content, String idDroit, BTransaction transaction, BSession bsession) {
        Salary salaire = content.getProvidedByEmployer().getSalary();
        LastIncome inCome = salaire.getLastIncome();
        String salaireMensuel = null;
        MainEmployer mainEmployeur = content.getMainEmployer();
        if(inCome != null) {
            salaireMensuel = String.valueOf(salaire.getLastIncome().getAmount());
        }
        try {
            AFAffiliation affiliation = findAffiliationByNumero(mainEmployeur.getAffiliateID(), bsession);
            if (affiliation != null) {
                APEmployeur emp = new APEmployeur();
                emp.setSession(bsession);
                emp.setIdTiers(affiliation.getIdTiers());
                emp.setIdAffilie(affiliation.getAffiliationId());
                emp.add(transaction);

                APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();
                situationProfessionnelle.setSession(bsession);
                situationProfessionnelle.setIdDroit(idDroit);
                situationProfessionnelle.setIdEmployeur(emp.getIdEmployeur());
                situationProfessionnelle.setIsIndependant(false);
                situationProfessionnelle.setIsVersementEmployeur(isVersementEmployeur(content));
                if(salaireMensuel != null) {
                    situationProfessionnelle.setSalaireMensuel(salaireMensuel);
                }

                // Vague 2 - Si le salarié est payé sur 13 mois
                // On ajoute son 13eme mois sans une autre rémunération annuelle
                if (inCome != null && inCome.isHasThirteenthMonth()) {
                    situationProfessionnelle.setAutreRemuneration(salaireMensuel);
                    situationProfessionnelle.setPeriodiciteAutreRemun(IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE);
                }

                situationProfessionnelle.wantCallValidate(false);
                situationProfessionnelle.add(transaction);
            }
        } catch (Exception e) {
            errors.add("Erreur rencontré lors de la création de la situation professionnelle pour l'assuré ");
            LOG.error("APImportationAPGPandemie#creerSituationProf : Erreur rencontré lors de la création de la situation professionnelle pour l'assuré ", e);
            if (isJadeThreadError()) {
                addJadeThreadErrorToListError("Situation Professionnelle Employé");
                // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans l'ajout de la situation prof
                JadeThread.logClear();
            }
        }
    }

    private void creationSituationProIndependant(Content content, String idDroit, BTransaction transaction, BSession bsession) {
        String masseAnnuel = "0";
        MainEmployer mainEmployeur = content.getMainEmployer();

        try {
            // TODO: Controler dans pandémie la gestion des indépendants
            // TODO: Controler si l'affiliation est à récupérer via l'employeur ou le tiers.
            AFAffiliation affiliation = findAffiliationByNumero(mainEmployeur.getAffiliateID(), bsession);
            if (affiliation != null) {
                APEmployeur emp = new APEmployeur();
                emp.setSession(bsession);
                emp.setIdTiers(affiliation.getIdTiers());
                emp.setIdAffilie(affiliation.getAffiliationId());
                emp.add(transaction);

                // on cherche la decision
                final CPDecisionManager decision = new CPDecisionManager();
                BSession sessionPhenix = new BSession("PHENIX");
                bsession.connectSession(sessionPhenix);

                decision.setSession(sessionPhenix);
                decision.setForIdAffiliation(affiliation.getAffiliationId());
                decision.setForIsActive(true);
                decision.setForAnneeDecision(String.valueOf(Date.getCurrentYear() - 1));
                decision.find(BManager.SIZE_NOLIMIT);

                // on cherche les données calculées en fonction de la
                // decision
                if (decision.size() > 0) {

                    final ICPDonneesCalcul donneesCalcul = (ICPDonneesCalcul) bsession
                            .getAPIFor(ICPDonneesCalcul.class);
                    donneesCalcul.setISession(PRSession.connectSession(bsession, "PHENIX"));

                    final Hashtable<Object, Object> parms = new Hashtable<>();
                    parms.put(ICPDonneesCalcul.FIND_FOR_ID_DECISION, ((CPDecision) decision.getEntity(0)).getIdDecision());
                    parms.put(ICPDonneesCalcul.FIND_FOR_ID_DONNEES_CALCUL, ICPDonneesCalcul.CS_REV_NET);

                    final ICPDonneesCalcul[] donneesCalculs = donneesCalcul.findDonneesCalcul(parms);

                    if ((donneesCalculs != null) && (donneesCalculs.length > 0)) {
                        masseAnnuel = donneesCalculs[0].getMontant();
                    }
                }

                APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();
                situationProfessionnelle.setSession(bsession);
                situationProfessionnelle.setIdDroit(idDroit);
                situationProfessionnelle.setIdEmployeur(emp.getIdEmployeur());
                situationProfessionnelle.setIsIndependant(true);
                situationProfessionnelle.setIsVersementEmployeur(isVersementEmployeur(content));
                situationProfessionnelle.setRevenuIndependant(masseAnnuel);

                situationProfessionnelle.wantCallValidate(false);
                situationProfessionnelle.add(transaction);
            }
        } catch (Exception e) {
            errors.add("Erreur rencontré lors de la création de la situation professionnelle pour l'assuré ");
            LOG.error("APImportationAPGAmatApat#creerSituationProf : Erreur rencontré lors de la création de la situation professionnelle pour l'assuré ", e);
            if (isJadeThreadError()) {
                addJadeThreadErrorToListError("Situation Professionnelle Employé");
                // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans l'ajout de la situation prof
                JadeThread.logClear();
            }
        }

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
     * @param methodeSource
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
     * @throws Exception
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
