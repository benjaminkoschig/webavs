package globaz.apg.process;

import apg.amatapat.*;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.process.ProcessMailUtils;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.pyxis.domaine.EtatCivil;
import ch.globaz.simpleoutputlist.exception.TechnicalException;
import com.google.common.base.Throwables;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.db.droits.*;
import globaz.apg.properties.APProperties;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.naos.db.affiliation.AFAffiliation;
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
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.tiers.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class APImportationAPGAmatApat extends APAbstractImportationAPG {

    private static final Logger LOG = LoggerFactory.getLogger(APImportationAPGAmatApat.class);

    private static final String AMAT_TYPE = "AMAT";
    private static final String APAT_TYPE = "APAT";
    private static final String FORM_INDEPENDANT = "FORM_INDEPENDANT";
    private static final String FORM_SALARIE = "FORM_SALARIE";
    private static final String BENEFICIAIRE_MERE = "MERE";
    private static final String BENEFICIAIRE_EMPLOYEUR = "EMPLOYEUR";

    private final List<String> fichiersTraites = new ArrayList<>();
    private final List<String> fichiersNonTraites = new ArrayList<>();

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            LOG.info("Start Import - Import des demandes APG AMAT ou APAT.");
            initBsession();
            // Pour ne pas envoyer de double mail, un mail d'erreur de validation est déjà généré
            this.setSendCompletionMail(false);
            this.setSendMailOnError(false);
            importFiles();
            generationProtocol();
        } catch (Exception e) {
            setReturnCode(-1);
            errors.add("erreur fatale : " + Throwables.getStackTraceAsString(e));
            try {
                generationProtocol();
            } catch (Exception e1) {
                throw new TechnicalException("Problème à l'envoi du mail", e1);
            }
            throw new TechnicalException("Erreur dans le process d'import", e);
        } finally {
            closeBsession();
        }
        LOG.info("Fin du process d'importation.");
        return true;
    }


    /**
     * Méthode permettant de générer le bilan du traitement et l'envoi du mail récapitulatif.
     *
     * @throws Exception : exception envoyée si un problème intervient lors de l'envoi du mail.
     */
    private void generationProtocol() {

        StringBuilder corpsCaisse = new StringBuilder();

        corpsCaisse.append(buildBlocMessage(fichiersTraites, "Fichiers traités : ", "; "));

        corpsCaisse.append(buildBlocMessage(fichiersNonTraites, "Fichiers non traités :", "; "));

        corpsCaisse.append(buildBlocMessage(infos, "Infos :", "\n"));

        corpsCaisse.append(buildBlocMessage(errors, "Erreurs :", "\n"));

        List<String> mails = getListEMailAddressTechnique(APProperties.EMAIL_AMAT_APAT);
        LOG.info("Envoi mail à l'adresse de la caisse " + mails.toString());
        ProcessMailUtils.sendMail(mails, getEMailObjectCaisse(), corpsCaisse.toString(), new ArrayList<>());

    }

    /**
     * Construit un bloc du mail.
     *
     * @param liste     la liste des infos
     * @param texte     le texte à saisir
     * @param separator le seprateur
     * @return un bloc du mail.
     */
    private String buildBlocMessage(List<String> liste, String texte, String separator) {
        StringBuilder bloc = new StringBuilder();
        if (!liste.isEmpty()) {
            bloc.append(texte).append(liste.size()).append("\n");
            for (String each : liste) {
                bloc.append(each).append(separator);
            }
            bloc.append("\n");
        }
        return bloc.toString();
    }

    /**
     * Récupérer l'objet du mail.
     *
     * @return l'objet du mail
     */
    private String getEMailObjectCaisse() {
        return "Importation AMAT-APAT : " + nbTraites + " fichier(s) traité(s)";
    }


    /**
     * Traitement des fichiers AMAT
     */
    private void importFiles() throws Exception {
        try {
            demandeFolder = APProperties.DEMANDES_AMAT_APAT_FOLDER.getValue();
            storageFolder = APProperties.STORAGE_AMAT_APAT_FOLDER.getValue();

            if (!JadeStringUtil.isBlankOrZero(demandeFolder) && !JadeStringUtil.isBlankOrZero(storageFolder)) {
                List<String> repositoryDemandesAmatApat = JadeFsFacade.getFolderChildren(demandeFolder);
                backupFolder = new File(storageFolder).getAbsolutePath() + BACKUP_FOLDER;
                errorsFolder = new File(storageFolder).getAbsolutePath() + ERRORS_FOLDER;
                for (String nomFichierDistant : repositoryDemandesAmatApat) {
                    if (MAX_TREATMENT > nbTraites) {
                        importFile(nomFichierDistant);
                    }
                }
                LOG.info("Nombre de dossiers traités : " + nbTraites);
            } else {
                LOG.warn("Les propriétés AMAT APAT ne sont pas définis.");
                errors.add("Import impossible : les propriétés AMAT-APAT ne sont pas définis.");
            }
        } catch (Exception e) {
            errors.add("erreur fatale : " + Throwables.getStackTraceAsString(e));
            LOG.error("Erreur lors de l'importation des fichiers", e);
            throw new Exception("Erreur lors de l'importation des fichiers", e);
        }
    }

    /**
     * Copie le fichier XML en local, effectue le traitement d'importion, sauvegarde le fichier
     * dans le dossier backup, supprime le fichier de base
     *
     * @param nomFichier
     * @throws IOException
     */
    private void importFile(String nomFichier) throws IOException {
        if (nomFichier.endsWith(XML_EXTENSION)) {
            try {
                LOG.info("Démarrage de l'import du fichier : " + nomFichier);
                String nameOriginalFile = FilenameUtils.getName(nomFichier);
                String nameOriginalFileWithoutExt = FilenameUtils.removeExtension(nameOriginalFile);

                boolean isTraitementSuccess = false;
                // Parsing du fichier XML
                Message message = getMessageFromFile                                                                                                                                                                                                                                                                                                                                                                                                                                                     (nomFichier) ;
                if (message != null) {
                    isTraitementSuccess = creationDroitGlobal(message.getContent());

                    if (isTraitementSuccess) {
                        fichiersTraites.add(nameOriginalFile);
                    } else {
                        fichiersNonTraites.add(nameOriginalFile);
                    }
                    // TODO : si traitement en succès --> sauvegarde du fichier
//                    if (isTraitementSuccess) {
//                        for (String nss : nssTraites) {
//                            savingFileInDb(nss, destTmpXMLPath, state);
//                            infos.add("Traitement du fichier suivant réussi : " + nameOriginalZipFile);
//                            infos.add("Assuré(s) concerné(s) : " + nss);
//                        }
//                    }
                } else {
                    errors.add("Le fichier XML ne peut pas être traité : " + nameOriginalFileWithoutExt);
                }

                // on déplace les fichiers traités.
                LOG.info("Déplacer le fichier : " + nameOriginalFileWithoutExt);
                movingFile(nomFichier, nameOriginalFile, isTraitementSuccess);
            } catch (Exception e) {
                LOG.error("Erreur lors du traitement du fichier. ", e);
                throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
            } finally {
                nbTraites++;
                closeBsession();
            }
        }

    }

    private void savingFileInDb(String nss, String pathFile, String state) throws Exception {
        BTransaction transaction = null;
        FileInputStream fileToStore = null;
        try {
            transaction = (BTransaction) bsession.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }
            if (transaction.hasErrors()) {
                LOG.error("Des erreurs ont été trouvés dans la transaction. : ", transaction.getErrors());
                transaction.clearErrorBuffer();
            }
            fileToStore = new FileInputStream(pathFile);
            APImportationAPGPandemieHistorique importData = new APImportationAPGPandemieHistorique();
            importData.setSession(bsession);
            importData.setEtatDemande(state);
            importData.setNss(nss);
            importData.setXmlFile(fileToStore);
            importData.add();
            if (!hasError(bsession, transaction)) {
                transaction.commit();
            } else {
                transaction.rollback();
            }
        } catch (FileNotFoundException e) {
            LOG.error("Fichier non trouvé : " + pathFile, e);
        } catch (Exception e) {
            errors.add("Erreur lors de l'update HistoriqueAPGPandemie " + e.getMessage());
            LOG.error("Erreur lors de l'update HistoriqueAPGPandemie : ", e);
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            try {
                if (!(Objects.isNull(fileToStore))) {
                    fileToStore.close();
                }
                if (transaction != null) {
                    transaction.closeTransaction();
                }

            } catch (IOException e) {
                LOG.error("Impossible de cloture le fichier", e);
            }
        }
    }

    private Message getMessageFromFile(String destPath) {
        try {
            LOG.info("Lecture du fichier.");
            String tmpLocalWorkFile = JadeFsFacade.readFile(destPath);
            File fichier = new File(tmpLocalWorkFile);
            if (fichier.isFile()) {
                JAXBContext jaxbContext = JAXBContext.newInstance(Message.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                return (Message) unmarshaller.unmarshal(fichier);
            }
        } catch (JadeServiceLocatorException | JadeClassCastException | JadeServiceActivatorException | JAXBException e) {
            errors.add("Impossible de parser le fichier XML : " + destPath + "(" + e.getMessage() + ")");
            LOG.error("Erreur lors de l'importation du fichier " + destPath, e);
        }
        return null;
    }

    private boolean creationDroitGlobal(Content content) throws Exception {
        BTransaction transaction = (BTransaction) bsession.newTransaction();
        if (!transaction.isOpened()) {
            transaction.openTransaction();
        }

        boolean isEmployee = false;
        boolean isIndependant = false;
        InsuredPerson assure = content.getInsuredPerson();
        AddressType adresseAssure = content.getInsuredAddress();
        switch (content.getFormType()) {
            case FORM_INDEPENDANT:
                isIndependant = true;
                break;
            case FORM_SALARIE:
                isEmployee = true;
                break;
            default:
                break;
        }

        Activities activitesProfessionnelles = content.getActivities();
        MainEmployer employeur = content.getMainEmployer();
        FamilyMembers membresFamille = content.getFamilyMembers();

        String npaFormat = formatNPA(getZipCode(adresseAssure));

        PRTiersWrapper tiers = getTiersByNss(assure.getVn());
        // TODO : identifier quand il faut créer un tiers.
        if (Objects.isNull(tiers)) {
            tiers = creationTiers(assure, npaFormat);
        }

        creationRoleApgTiers(tiers.getIdTiers());

        createContent(tiers, assure.getEmail());


        if (StringUtils.equals(AMAT_TYPE, content.getAmatApatType())) {


            PRDemande demande = creationDemande(tiers.getIdTiers());

            APDroitMaternite droitMat = creationDroit(content, npaFormat, demande, transaction);


            creationSituationFamilialeAmat(membresFamille, droitMat.getIdDroit(), transaction);

            // TODO création situation pro
            if (isEmployee) {
                creationSituationProEmployeAmat(content, droitMat.getIdDroit(), transaction);
            } else if (isIndependant) {
                creationSituationProIndependantAmat(content, droitMat.getIdDroit(), transaction);
            }
            nssTraites.add(tiers.getNSS());

        } else if (StringUtils.equals(APAT_TYPE, content.getAmatApatType())) {
            // TODO : création du droit paternité
            APDroitPaternite newDroit = new APDroitPaternite();
            newDroit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ENREGISTRE);
        }  else{
            transaction.rollback();
            LOG.warn("Type de droit non reconnu : "+ content.getAmatApatType());
            return false;
        }

        if (!hasError(bsession, transaction)) {
            transaction.commit();
            LOG.info("Traitement en succès...");
            return true;
        }else{
            transaction.rollback();
            errors.add("Un problème est survenu lors de la création du droit pour cet assuré : "+ assure.getVn());
            LOG.error("Erreur lors de la création du droit\nSession errors : "
                    +bsession.getErrors()+"\nTransactions errors : "+transaction.getErrors());
            return false;
        }

    }

    private PRTiersWrapper creationTiers(InsuredPerson assure, String codeNpa) throws Exception {

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
                assure.getFirstName(), ITIPersonne.CS_FEMME, tranformGregDateToGlobDate(assure.getDateOfBirth()),
                "",
                avsUtils.isSuisse(assure.getVn()) ? TIPays.CS_SUISSE : PRTiersHelper.ID_PAYS_BIDON, canton, "",
                String.valueOf(EtatCivil.CELIBATAIRE.getCodeSysteme()));

        return PRTiersHelper.getTiersParId(bsession, idTiers);
    }

    private void createContent(PRTiersWrapper tiers, String email) throws Exception {
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

    private void creationSituationProEmployeAmat(Content content, String idDroit, BTransaction transaction) {
        Salary salaire = content.getProvidedByEmployer().getSalary();
        String salaireMensuel = String.valueOf(salaire.getLastIncome().getAmount());
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
            AFAffiliation affiliation = findAffiliationByNumero(mainEmployeur.getAffiliateID());
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

    private void creationSituationProIndependantAmat(Content content, String idDroit, BTransaction transaction) {
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
            AFAffiliation affiliation = findAffiliationByNumero(mainEmployeur.getAffiliateID());
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

    private APDroitMaternite creationDroit(Content content, String npaFormat, PRDemande demande, BTransaction transaction) {
        APDroitMaternite newDroit = new APDroitMaternite();
        try {
            // TODO : Création du droit maternité
            newDroit.setIdDemande(demande.getIdDemande());
            newDroit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ENREGISTRE);
            newDroit.setGenreService(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE);
            newDroit.setReference(content.getReferenceData());
            newDroit.setIdCaisse(creationIdCaisse());
            newDroit.setNpa(npaFormat);
            // TODO : récupérer code du pays
            newDroit.setPays("100");

            // Récupération de la date de naissance du dernier enfant -> date de début du droit
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
            newDroit.setDateDebutDroit(JadeDateUtil.getGlobazFormattedDate(dateNaissance));
            Date date = new Date();
            newDroit.setDateDepot(date.getSwissValue());
            newDroit.setDateReception(date.getSwissValue());

            boolean isSoumisImpotSource = false;
            if (Objects.nonNull(content.getProvidedByEmployer()) && Objects.nonNull(content.getProvidedByEmployer().getSalary())) {
                isSoumisImpotSource = content.getProvidedByEmployer().getSalary().isWithholdingTax();
            }
            newDroit.setIsSoumisImpotSource(isSoumisImpotSource);

            newDroit.setSession(bsession);
            newDroit.add(transaction);

        } catch (Exception e) {
            errors.add("Une erreur s'est produite lors de la création du droit maternité " + e.getMessage());
            LOG.error("Une erreur s'est produite lors de la création du droit : ", e);
        }

        return newDroit;
    }


    /**
     * Création de la situation familiale pour la maternité.
     */
    private void creationSituationFamilialeAmat(FamilyMembers membresFamille, String idDroit, BTransaction transaction) {
        try {
            for (Child child : membresFamille.getChildren().getChild()) {
                APSituationFamilialeMat enfant = new APSituationFamilialeMat();
                enfant.setNom(child.getOfficialName());
                enfant.setPrenom(child.getFirstName());
                enfant.setDateNaissance(tranformGregDateToGlobDate(child.getDateOfBirth()));
                enfant.setIdDroitMaternite(idDroit);
                enfant.setType(IAPDroitMaternite.CS_TYPE_ENFANT);
                enfant.setSession(bsession);
                enfant.add(transaction);
            }
        } catch (Exception e) {
            errors.add("Impossible de créer la situation familiale ");
            LOG.error("Erreur lors de la création de la situation familiale ", e);
        }
    }

    private PRDemande creationDemande(String idTiers) throws Exception {
        PRDemande retValue = null;
        String typeDemande = IPRDemande.CS_TYPE_MATERNITE;

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
     * @param adresseAssure
     * @return
     */
    private String getZipCode(AddressType adresseAssure) {
        String npaTrouve = "";
        String zipCodeTown = adresseAssure.getZipCodeTown();

        String[] zipCodeTownSplitted = StringUtils.split(zipCodeTown, ",");

        return zipCodeTownSplitted[0].trim();
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }
}
