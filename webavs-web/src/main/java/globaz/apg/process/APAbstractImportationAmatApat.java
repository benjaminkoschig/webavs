package globaz.apg.process;

import apg.amatapat.Content;
import apg.amatapat.InsuredPerson;
import apg.amatapat.MainEmployer;
import apg.amatapat.Salary;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pyxis.domaine.EtatCivil;
import globaz.apg.db.droits.APEmployeur;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.properties.APProperties;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessage;
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
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.tiers.*;
import org.slf4j.Logger;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Objects;

public abstract class APAbstractImportationAmatApat implements IAPImportationAmatApat {

    protected LinkedList<String> errors;
    protected LinkedList<String> infos;
    protected Logger LOG;
    protected String typeDemande;
    protected static final String FORM_INDEPENDANT = "FORM_INDEPENDANT";
    protected static final String FORM_SALARIE = "FORM_SALARIE";
    private static final String BENEFICIAIRE_MERE = "MERE";
    private static final String BENEFICIAIRE_PERE = "PERE";
    private static final String BENEFICIAIRE_EMPLOYEUR = "EMPLOYEUR";

    public APAbstractImportationAmatApat(LinkedList<String> err, LinkedList<String> inf, Logger log){
        LOG = log;
        errors = err;
        infos = inf;
    }

    @Override
    public void createRoleApgTiers(String idTiers, BSession bsession) throws Exception {
        TIRoleManager roleManager = new TIRoleManager();
        roleManager.setSession(bsession);
        roleManager.setForIdTiers(idTiers);
        BStatement statement = null;
        BTransaction trans = (BTransaction) bsession.newTransaction();
        trans.openTransaction();
        try {
            statement = roleManager.cursorOpen(bsession.getCurrentThreadTransaction());
            TIRole role = null;
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
                newRole.add(bsession.getCurrentThreadTransaction());
            }
            trans.commit();
        } catch (Exception e) {
            errors.add("Une erreur s'est produite dans la création du rôle du tiers : "+idTiers);
            LOG.error("ImportAPGPandemie#creationRoleApgTiers - Une erreur s'est produite dans la création du rôle du tiers : "+idTiers);
            if (trans != null) {
                trans.rollback();
            }
            if(isJadeThreadError()){
//                addJadeThreadErrorToListInfos("Rôle");
                // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans l'ajout du rôle
                JadeThread.logClear();
            }
            throw new Exception("ImportAPGPandemie#creationRoleApgTiers - Une erreur s'est produite dans la création du rôle du tiers : "+idTiers);
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
                trans.closeTransaction();
            }
        }
    }

    @Override
    public void createContact(PRTiersWrapper tiers, String email, BSession bsession) throws Exception {

        try {
            if (!JadeStringUtil.isBlankOrZero(email)) {
                TIAvoirContactManager avoirContactManager = new TIAvoirContactManager();
                avoirContactManager.setForIdTiers(tiers.getIdTiers());
                avoirContactManager.find(BManager.SIZE_NOLIMIT);

                boolean hasEmail = false;
                if (!avoirContactManager.getContainer().isEmpty()) {
                    for (int i = 0; i < avoirContactManager.getContainer().size(); i++) {
                        TIAvoirContact avoirContact = (TIAvoirContact) avoirContactManager.get(i);

                        TIMoyenCommunicationManager moyenCommunicationManager = new TIMoyenCommunicationManager();
                        moyenCommunicationManager.setForIdContact(avoirContact.getIdContact());
                        moyenCommunicationManager.setForMoyenLike(TIMoyenCommunication.EMAIL);

                        if (!moyenCommunicationManager.isEmpty()) {
                            hasEmail = true;
                            break;
                        }
                    }
                }

                if (!hasEmail) {

                    // Création du contact
                    TIContact contact = new TIContact();
                    contact.setSession(bsession);
                    contact.setNom(tiers.getNom());
                    contact.setPrenom(tiers.getPrenom());
                    contact.add(bsession.getCurrentThreadTransaction());

                    // Création du moyen de communication
                    TIMoyenCommunication moyenCommunication = new TIMoyenCommunication();
                    moyenCommunication.setSession(bsession);
                    moyenCommunication.setMoyen(email);
                    moyenCommunication.setTypeCommunication(TIMoyenCommunication.EMAIL);
                    moyenCommunication.setIdContact(contact.getIdContact());
                    moyenCommunication.setIdApplication(APProperties.DOMAINE_ADRESSE_APG_PANDEMIE.getValue());
                    moyenCommunication.add();

                    // Lien du contact avec le tiers
                    TIAvoirContact avoirContact = new TIAvoirContact();
                    avoirContact.setSession(bsession);
                    avoirContact.setIdTiers(tiers.getIdTiers());
                    avoirContact.setIdContact(contact.getIdContact());
                    avoirContact.add();
                } else {
                    // TODO : message pour informer que le contact n'a pas été mis à jour
                }

            }

        } catch (Exception e) {
            errors.add("Une erreur est survenue lors de la création du contact pour l'id tiers : " + tiers.getNSS() + " - " + email);
            LOG.error("APImportationAPGPandemie#createContact : Une erreur est survenue lors de la création du contact pour l'id tiers " + tiers.getNSS(), e);
            throw new Exception("APImportationAPGPandemie#createContact : Une erreur est survenue lors de la création du contact pour l'id tiers " + tiers.getNSS());
        }
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
                avsUtils.isSuisse(assure.getVn()) ? TIPays.CS_SUISSE : PRTiersHelper.ID_PAYS_BIDON, canton, "",
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
    public PRDemande createDemande(String idTiers, BSession bsession) throws Exception {
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
            // TODO : refacto
            if (isJadeThreadError()) {
                addJadeThreadErrorToListError("Demande");
            }
            throw new Exception("Erreur lors de la création de la demande du droit ", e);
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
            LOG.error("APImportationAPGPandemie#creationIdCaisse : A fatal exception was thrown when accessing to the CommonProperties " + exception);
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
        String salaireMensuel = String.valueOf(salaire.getLastIncome().getAmount());
        MainEmployer mainEmployeur = content.getMainEmployer();
        boolean isVersementEmployeur;
        switch (content.getPaymentContact().getBeneficiaryType()) {
            case BENEFICIAIRE_EMPLOYEUR:
                isVersementEmployeur = true;
                break;
            case BENEFICIAIRE_MERE:
            case BENEFICIAIRE_PERE:
            default:
                isVersementEmployeur = false;
                break;
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
                situationProfessionnelle.setIsVersementEmployeur(isVersementEmployeur);
                situationProfessionnelle.setSalaireMensuel(salaireMensuel);

                // Vague 2 - Si le salarié est payé sur 13 mois
                // On ajoute son 13eme mois sans une autre rémunération annuelle
                if (salaire.getLastIncome().isHasThirteenthMonth()) {
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
//                JadeThread.logClear();
            }
        }
    }

    private void creationSituationProIndependant(Content content, String idDroit, BTransaction transaction, BSession bsession) {
        String masseAnnuel = "0";
        MainEmployer mainEmployeur = content.getMainEmployer();
        boolean isVersementEmployeur;
        switch (content.getPaymentContact().getBeneficiaryType()) {
            case BENEFICIAIRE_EMPLOYEUR:
                isVersementEmployeur = true;
                break;
            case BENEFICIAIRE_MERE:
            default:
                isVersementEmployeur = false;
                break;
        }

        try {
            AFAffiliation affiliation = findAffiliationByNumero(mainEmployeur.getAffiliateID(), bsession);
            if (affiliation != null) {
                APEmployeur emp = new APEmployeur();
                emp.setSession(bsession);
                emp.setIdTiers(affiliation.getIdTiers());
                emp.setIdAffilie(affiliation.getAffiliationId());
                emp.add(transaction);

                // on cherche la decision
                final CPDecisionManager decision = new CPDecisionManager(); //(CPDecision) bsession.getAPIFor(CPDecision.class);
                BSession sessionPhenix = new BSession("PHENIX");
                bsession.connectSession(sessionPhenix);

                decision.setSession(sessionPhenix);
                decision.setForIdAffiliation(affiliation.getAffiliationId());
                decision.setForIsActive(true);
                // TODO : année prise en compte.
//                decision.setForAnneeDecision(ANNEE_PRISE_COMPTE_SALAIRE);
                decision.find(BManager.SIZE_NOLIMIT);

                // on cherche les données calculées en fonction de la
                // decision
                if ((decision != null) && (decision.size() > 0)) {

                    final ICPDonneesCalcul donneesCalcul = (ICPDonneesCalcul) bsession
                            .getAPIFor(ICPDonneesCalcul.class);
                    donneesCalcul.setISession(PRSession.connectSession(bsession, "PHENIX"));

                    final Hashtable<Object, Object> parms = new Hashtable<Object, Object>();
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
                situationProfessionnelle.setIsVersementEmployeur(isVersementEmployeur);
                // TODO : comment obtenir le salaire
                situationProfessionnelle.setRevenuIndependant(masseAnnuel);

                situationProfessionnelle.wantCallValidate(false);
                situationProfessionnelle.add(transaction);
            }
        } catch (Exception e) {
            errors.add("Erreur rencontré lors de la création de la situation professionnelle pour l'assuré ");
            LOG.error("APImportationAPGPandemie#creerSituationProf : Erreur rencontré lors de la création de la situation professionnelle pour l'assuré ", e);
            if (isJadeThreadError()) {
                addJadeThreadErrorToListError("Situation Professionnelle Employé");
                // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans l'ajout de la situation prof
//                JadeThread.logClear();
            }
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
        LOG.error(methodeSource+": ");
        errors.add("Concerne: "+methodeSource+"\n");
        for (int i = 0; (messages != null) && (i < messages.length); i++) {
            LOG.error("-->Erreur: "+messages[i].getContents(null));
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
        boolean isSoumisImpotSource = false;
        if (Objects.nonNull(content.getProvidedByEmployer()) && Objects.nonNull(content.getProvidedByEmployer().getSalary())) {
            return content.getProvidedByEmployer().getSalary().isWithholdingTax();
        }
        return isSoumisImpotSource;
    }
}
