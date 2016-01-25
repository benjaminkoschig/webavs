package globaz.osiris.process.importoperations;

import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.adhesion.AFAdhesion;
import globaz.naos.db.adhesion.AFAdhesionManager;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APICompteCourant;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperationOrdreVersement;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAReferenceRubrique;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.external.IntRole;
import java.rmi.RemoteException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CAImportOperationsUtils {
    private static final String COMPENSATION_FIRST_SECTION_RIGHT_PART = "000";

    private static final int COMPENSATION_SECTION_INCREMENT_IDEXTERNE = 1;
    private static final int FIRST_ITEM = 0;
    private static final String LABEL_COMPTE_ANNEXE_NON_CHARGE = "7096";
    private static final String LABEL_COMPTECOURANT_NON_RESOLU = "5039";
    private static final String LABEL_IDENTIFIANT_TIERS_NON_RENSEIGNE = "5045";
    private static final String LABEL_RUBRIQUE_NON_RESOLU = "5016";

    private static final String LABEL_SECTION_NON_RECUPEREE = "7231";

    private static final String LABEL_TYPE_SECTION_INCONNU = "5015";

    private static final String TAG_ANNEECOTISATION = "anneecotisation";
    private static final String TAG_AUXILIAIRE = "auxiliaire";
    private static final String TAG_BLOQUERVERSEMENT = "bloquerversement";
    private static final String TAG_CODEDEBITCREDIT = "codedebitcredit";
    private static final String TAG_CODEISOMONNAIEBONIFICATION = "codeisomonnaiebonification";
    private static final String TAG_CODEISOMONNAIEDEPOT = "codeisomonnaiedepot";
    private static final String TAG_COMPENSATION = "compensation";

    private static final String TAG_DATE = "date";
    private static final String TAG_DATEDEPOT = "datedepot";
    private static final String TAG_DATEINSCRIPTION = "dateinscription";
    private static final String TAG_DATETRAITEMENT = "datetraitement";
    private static final String TAG_ECRITURE = "ecriture";
    private static final String TAG_GENRETRANSACTION = "genretransaction";
    private static final String TAG_IDADRESSEPAIEMENT = "idadressepaiement";
    private static final String TAG_IDCAISSEPROFSECTION = "idcaisseprofsection";
    private static final String TAG_IDCOMPTEANNEXE = "idcompteannexe";
    private static final String TAG_IDCOMPTECOURANT = "idcomptecourant";
    private static final String TAG_IDEXTERNECOMPTECOURANT = "idexternecomptecourant";
    private static final String TAG_IDEXTERNEROLE = "idexternerole";
    private static final String TAG_IDEXTERNERUBRIQUE = "idexternerubrique";
    private static final String TAG_IDEXTERNESECTION = "idexternesection";
    private static final String TAG_IDORGANEEXECUTION = "idorganeexecution";
    private static final String TAG_IDROLE = "idrole";
    private static final String TAG_IDRUBRIQUE = "idrubrique";
    private static final String TAG_IDSECTION = "idsection";
    private static final String TAG_IDTYPESECTION = "idtypesection";
    private static final String TAG_LIBELLE = "libelle";
    private static final String TAG_MASSE = "masse";
    private static final String TAG_MONTANT = "montant";
    private static final String TAG_MOTIF = "motif";
    private static final String TAG_NATUREORDRE = "natureordre";
    private static final String TAG_ORDREVERSEMENT = "ordreversement";
    private static final String TAG_PAIEMENT = "paiement";
    private static final String TAG_PAIEMENTAUXILIAIRE = "paiementauxiliaire";
    private static final String TAG_PAIEMENTBVR = "paiementbvr";
    private static final String TAG_PIECECOMPTABLE = "piececomptable";
    private static final String TAG_QUITTANCER = "quittancer";
    private static final String TAG_RECOUVREMENT = "recouvrement";
    private static final String TAG_REFERENCEBVR = "referencebvr";
    private static final String TAG_REFERENCEINTERNE = "referenceinterne";
    private static final String TAG_TAUX = "taux";
    private static final String TAG_TYPEBVR = "typebvr";

    private static final String TAG_TYPEVIREMENT = "typevirement";

    private static final String TAG_VALUE = "value";

    private static final String TAG_VALUE_CREDIT = "credit";
    private static final String TAG_VALUE_DEBIT = "debit";
    private static final String TAG_VALUE_EXTOURNECREDIT = "extournecredit";
    private static final String TAG_VALUE_EXTOURNEDEBIT = "extournedebit";
    private static final String TAG_VALUE_TRUE = "true";

    /**
     * Créer une section de compensation.
     * 
     * @param session
     * @param transaction
     * @param idCompteAnnexe
     * @param journal
     * @param newIdExterneSection
     * @param idTypeSectionValue
     * @return
     * @throws Exception
     */
    private static String createSectionCompensation(BSession session, BTransaction transaction, String idCompteAnnexe,
            CAJournal journal, String newIdExterneSection, String idTypeSectionValue) throws Exception {
        CASection newSection = new CASection();
        newSection.setSession(session);
        newSection.setIdCompteAnnexe(idCompteAnnexe);
        newSection.setIdExterne(newIdExterneSection);
        newSection.setIdTypeSection(idTypeSectionValue);
        newSection.setIdJournal(journal.getIdJournal());
        newSection.setDateSection(journal.getDateValeurCG());
        newSection.add(transaction);

        if (newSection.hasErrors()) {
            throw new Exception(newSection.getErrors().toString());
        }

        return newSection.getIdSection();
    }

    /**
     * Retourne l'année de cotisation.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getAnneeCotisation(Element parent) throws Exception {
        return CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_ANNEECOTISATION));
    }

    /**
     * Retourne les opérations auxiliaires spécifiées dans le document xml.
     * 
     * @param doc
     * @return
     */
    public static NodeList getAuxiliaires(Document doc) {
        return doc.getDocumentElement().getElementsByTagName(CAImportOperationsUtils.TAG_AUXILIAIRE);
    }

    /**
     * Retourne le premier enfant correspondant au tag souhaité.
     * 
     * @param parent
     * @param tag
     * @return
     * @throws Exception
     */
    private static Element getChildElement(Element parent, String tag) throws Exception {
        return (Element) parent.getElementsByTagName(tag).item(CAImportOperationsUtils.FIRST_ITEM);
    }

    /**
     * Retourne le code debit/credit DB correspondant à la valeur par défaut du doc xml.
     * 
     * @param session
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getCodeDebitCredit(BSession session, Element parent) throws Exception {
        String codeDebitCredit = CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_CODEDEBITCREDIT));
        if (codeDebitCredit.equals(CAImportOperationsUtils.TAG_VALUE_DEBIT)) {
            return APIEcriture.DEBIT;
        } else if (codeDebitCredit.equals(CAImportOperationsUtils.TAG_VALUE_CREDIT)) {
            return APIEcriture.CREDIT;
        } else if (codeDebitCredit.equals(CAImportOperationsUtils.TAG_VALUE_EXTOURNEDEBIT)) {
            return APIEcriture.EXTOURNE_DEBIT;
        } else if (codeDebitCredit.equals(CAImportOperationsUtils.TAG_VALUE_EXTOURNECREDIT)) {
            return APIEcriture.EXTOURNE_CREDIT;
        } else {
            throw new Exception(session.getLabel("5117"));
        }
    }

    /**
     * Retourne le code iso de la monnaie de bonification. éxemple : CHF.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getCodeISOMonnaieBonification(Element parent) throws Exception {
        String codeIso = CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_CODEISOMONNAIEBONIFICATION));
        if (!JadeStringUtil.isBlank(codeIso)) {
            return codeIso.toUpperCase();
        } else {
            return null;
        }
    }

    /**
     * Retourne le code iso de la monnaie de dépot. éxemple : CHF.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getCodeISOMonnaieDepot(Element parent) throws Exception {
        String codeIso = CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_CODEISOMONNAIEDEPOT));
        if (!JadeStringUtil.isBlank(codeIso)) {
            return codeIso.toUpperCase();
        } else {
            return null;
        }
    }

    /**
     * Retourne les écritures de compensation spécifiées dans le document xml.
     * 
     * @param doc
     * @return
     */
    public static NodeList getCompensations(Document doc) {
        return doc.getDocumentElement().getElementsByTagName(CAImportOperationsUtils.TAG_COMPENSATION);
    }

    /**
     * Retourne la date de valeur.
     * 
     * @param parent
     * @param journal
     * @return La date spécifié dans le doc xml. Si vide la date de valeur CG du journal est retournée.
     * @throws Exception
     */
    public static String getDate(Element parent, CAJournal journal) throws Exception {
        Element date = CAImportOperationsUtils.getChildElement(parent, CAImportOperationsUtils.TAG_DATE);
        if (!JadeStringUtil.isBlank(CAImportOperationsUtils.getValue(date))) {
            new JADate(CAImportOperationsUtils.getValue(date));

            return CAImportOperationsUtils.getValue(date);
        } else {
            return journal.getDateValeurCG();
        }
    }

    /**
     * Retourne la date de dépot.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getDateDepot(Element parent) throws Exception {
        return CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_DATEDEPOT));
    }

    /**
     * Retourne la date d'inscription.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getDateInscription(Element parent) throws Exception {
        return CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_DATEINSCRIPTION));
    }

    /**
     * Retourne la date de traitement.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getDateTraitement(Element parent) throws Exception {
        return CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_DATETRAITEMENT));
    }

    /**
     * Retourne les écritures spécifiées dans le document xml.
     * 
     * @param doc
     * @return
     */
    public static NodeList getEcritures(Document doc) {
        return doc.getDocumentElement().getElementsByTagName(CAImportOperationsUtils.TAG_ECRITURE);
    }

    /**
     * Retourne le premier enfant paiement. Utilisé pour les paiementsBVR.
     * 
     * @param e
     * @return
     */
    public static Element getFirstPaiement(Element e) {
        return (Element) e.getElementsByTagName(CAImportOperationsUtils.TAG_PAIEMENT).item(
                CAImportOperationsUtils.FIRST_ITEM);
    }

    /**
     * Retourne le genre de tranasaction du paiement bvr.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getGenreTransaction(Element parent) throws Exception {
        return CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_GENRETRANSACTION));
    }

    /**
     * Retourne l'id de l'adresse de paiement de l'ordre de versement.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getIdAdressePaiement(Element parent) throws Exception {
        return CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_IDADRESSEPAIEMENT));
    }

    /**
     * @param session
     * @param idCaisse
     * @param compteAnnexe
     * @throws RemoteException
     * @throws Exception
     * @throws FWSecurityLoginException
     */
    private static String getIdCAisseFromAffiliation(BSession session, String idCaisse, CACompteAnnexe compteAnnexe)
            throws RemoteException, Exception, FWSecurityLoginException {
        BSession sessionAffiliation = (BSession) GlobazSystem.getApplication("NAOS").newSession();
        session.connectSession(sessionAffiliation);

        AFAdhesionManager adhmanager = new AFAdhesionManager();
        adhmanager.setSession(sessionAffiliation);
        adhmanager.setForAffiliationId(compteAnnexe.getRole().getAffiliation().getAffiliationId());
        adhmanager.find();
        for (int i = 0; i < adhmanager.size(); i++) {
            AFAdhesion adh = (AFAdhesion) adhmanager.getEntity(i);
            if (adh.getPlanCaisse().getAdministrationNo().trim().equals(idCaisse)) {
                return adh.getPlanCaisse().getIdTiers();
            }
        }
        return null;
    }

    /**
     * Retourne l'identifiant de la Caisse Professionnelle fournie dans l'importation des données. Vérifie que la
     * rubrique liée est renseignée et que si elle gère une caisse professionnelle, la caisse n'est liée qu'à un seul
     * tier.
     * 
     * @return id de la caisse professionnelle ou null
     * @throws Exception
     */
    public static String getIdCaisseProfessionnelle(BSession session, Element parent) throws Exception {
        String idCaisse = CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_IDCAISSEPROFSECTION));
        CARubrique rubrique = null;
        try {
            rubrique = CAImportOperationsUtils.getRubrique(session, parent);
        } catch (Exception e) {
            // L'identifiant de la rubrique est mal renseigné
            throw new Exception(session.getLabel("IMPORT_RUBRIQUE_INEXISTANTE") + " - " + e.toString());
            // return null;
        }

        // Si l'utilisateur demande à gérer la caisse prof depuis la rubrique...
        if (rubrique.isUseCaissesProf()) {
            if (idCaisse == null) {
                throw new Exception(session.getLabel("IMPORT_NOID") + " : " + rubrique.getIdExterne());
            }
            // Vérifier que la caisse professionnelle est liee à un seul tiers
            try {
                Element idCompteAnnexe = CAImportOperationsUtils.getChildElement(parent,
                        CAImportOperationsUtils.TAG_IDCOMPTEANNEXE);

                if (JadeStringUtil.isIntegerEmpty(CAImportOperationsUtils.getValue(idCompteAnnexe))) {
                    Element idExterneRole = CAImportOperationsUtils.getChildElement(parent,
                            CAImportOperationsUtils.TAG_IDEXTERNEROLE);
                    Element idRole = CAImportOperationsUtils
                            .getChildElement(parent, CAImportOperationsUtils.TAG_IDROLE);

                    CACompteAnnexe compteAnnexe = new CACompteAnnexe();
                    compteAnnexe.setSession(session);
                    compteAnnexe.setIdExterneRole(CAImportOperationsUtils.getValue(idExterneRole));
                    compteAnnexe.setIdRole(CAImportOperationsUtils.getValue(idRole));
                    compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                    compteAnnexe.retrieve();

                    if ((compteAnnexe.getRole() != null) && (compteAnnexe.getRole().getAffiliation() == null)) {
                        throw new Exception(session.getLabel("IMPORT_NO_AFFILIATION") + " : "
                                + compteAnnexe.getIdExterneRole());
                    }

                    return CAImportOperationsUtils.getIdCAisseFromAffiliation(session, idCaisse, compteAnnexe);
                }
                return null;
            } catch (Exception e) {
                throw new Exception(session.getLabel("IMPORT_CAISSE_PROF_NON_DISPO") + " idCaisse : " + idCaisse
                        + " - " + e.toString());
            }
        } else {
            if (idCaisse != null) {
                throw new Exception(session.getLabel("IMPORT_RUBRIQUE_SANS_CAISSE_PROF") + " " + idCaisse + " - "
                        + rubrique.getIdExterne());
            } else {
                return null;
            }
        }

        // throw new Exception(session.getLabel("IMPORT_CAISSE_PROF_PROBLEME") +
        // " idCaisse : " + idCaisse);
        // return null;
    }

    /**
     * Retroune l'idCompteAnnexe. Si l'idCompteAnnexe n'est pas renseigné alors le compte annexe sera résolu grâce à la
     * clef alternée (idExterneRole et idRole).<br/>
     * Si quittancer le compte annexe sera créé si non résolu.
     * 
     * @param session
     * @param transaction
     * @param parent
     * @param journal
     * @param compteAnnexeGenre
     * @return
     * @throws Exception
     */
    public static String getIdCompteAnnexe(BSession session, BTransaction transaction, Element parent,
            CAJournal journal, String compteAnnexeGenre) throws Exception {
        Element idCompteAnnexe = CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_IDCOMPTEANNEXE);

        if (JadeStringUtil.isIntegerEmpty(CAImportOperationsUtils.getValue(idCompteAnnexe))) {
            Element idExterneRole = CAImportOperationsUtils.getChildElement(parent,
                    CAImportOperationsUtils.TAG_IDEXTERNEROLE);
            Element idRole = CAImportOperationsUtils.getChildElement(parent, CAImportOperationsUtils.TAG_IDROLE);

            CACompteAnnexe compteAnnexe = new CACompteAnnexe();
            compteAnnexe.setSession(session);

            compteAnnexe.setIdExterneRole(CAImportOperationsUtils.getValue(idExterneRole));
            compteAnnexe.setIdRole(CAImportOperationsUtils.getValue(idRole));
            compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);

            compteAnnexe.retrieve();

            if (compteAnnexe.isNew()) {
                if ((!JadeStringUtil.isBlank(CAImportOperationsUtils.getValue(idExterneRole)))
                        && (!JadeStringUtil.isBlank(CAImportOperationsUtils.getValue(idRole)))
                        && (CAImportOperationsUtils.isQuittancer(parent))) {
                    CAApplication currentApplication = CAApplication.getApplicationOsiris();
                    IntRole role = (IntRole) GlobazServer.getCurrentSystem()
                            .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                            .getImplementationFor(session, IntRole.class);
                    role.retrieve(CAImportOperationsUtils.getValue(idRole),
                            CAImportOperationsUtils.getValue(idExterneRole));

                    if (role.isNew()) {
                        throw new Exception(
                                session.getLabel(CAImportOperationsUtils.LABEL_IDENTIFIANT_TIERS_NON_RENSEIGNE));
                    }

                    compteAnnexe.setIdTiers(role.getIdTiers());
                    compteAnnexe.setAlternateKey(null);
                    compteAnnexe.setIdJournal(journal.getIdJournal());
                    compteAnnexe.setIdGenreCompte(compteAnnexeGenre);

                    compteAnnexe.add(transaction);

                    if (compteAnnexe.hasErrors()) {
                        throw new Exception(compteAnnexe.getErrors().toString());
                    }
                } else {
                    throw new Exception(session.getLabel(CAImportOperationsUtils.LABEL_COMPTE_ANNEXE_NON_CHARGE)
                            + " (KEY " + CAImportOperationsUtils.TAG_IDEXTERNEROLE + " : "
                            + CAImportOperationsUtils.getValue(idExterneRole) + ", "
                            + CAImportOperationsUtils.TAG_IDROLE + " : " + CAImportOperationsUtils.getValue(idRole)
                            + ")");
                }
            } else if (!compteAnnexe.getIdGenreCompte().equals(compteAnnexeGenre)) {
                throw new Exception(session.getLabel(CAImportOperationsUtils.LABEL_COMPTE_ANNEXE_NON_CHARGE) + " (KEY "
                        + CAImportOperationsUtils.TAG_IDEXTERNEROLE + " : "
                        + CAImportOperationsUtils.getValue(idExterneRole) + ", " + CAImportOperationsUtils.TAG_IDROLE
                        + " : " + CAImportOperationsUtils.getValue(idRole) + ")");
            }

            return compteAnnexe.getIdCompteAnnexe();
        } else {
            return CAImportOperationsUtils.getValue(idCompteAnnexe);
        }
    }

    /**
     * Retourne l'id du compte courant. Si l'id n'est pas spécifié la clef alternée sera utilisé pour résoudre le compte
     * courant.
     * 
     * @param session
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getIdCompteCourant(BSession session, Element parent) throws Exception {
        Element idCompteCourant = CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_IDCOMPTECOURANT);

        if (JadeStringUtil.isIntegerEmpty(CAImportOperationsUtils.getValue(idCompteCourant))) {
            Element idExterneCompteCourant = CAImportOperationsUtils.getChildElement(parent,
                    CAImportOperationsUtils.TAG_IDEXTERNECOMPTECOURANT);

            if (!JadeStringUtil.isBlank(CAImportOperationsUtils.getValue(idExterneCompteCourant))) {
                CACompteCourant compteCourant = new CACompteCourant();
                compteCourant.setSession(session);

                compteCourant.setIdExterne(CAImportOperationsUtils.getValue(idExterneCompteCourant));

                compteCourant.setAlternateKey(APICompteCourant.AK_IDEXTERNE);

                compteCourant.retrieve();

                if (compteCourant.isNew()) {
                    throw new Exception(session.getLabel(CAImportOperationsUtils.LABEL_COMPTECOURANT_NON_RESOLU)
                            + "KEY " + CAImportOperationsUtils.TAG_IDEXTERNECOMPTECOURANT + " : "
                            + CAImportOperationsUtils.getValue(idExterneCompteCourant));
                }

                return compteCourant.getIdCompteCourant();
            } else {
                return null;
            }
        } else {
            return CAImportOperationsUtils.getValue(idCompteCourant);
        }
    }

    /**
     * Retourne l'id de l'organe d'éxécution de l'ordre de versement.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getIdOrganeExecution(Element parent) throws Exception {
        return CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_IDORGANEEXECUTION));
    }

    /**
     * Retourne l'id de la rubrique. Si idrubrique n'est pas défini alors la rubrique sera résolu grâce à la clef
     * alternée.
     * 
     * @param session
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getIdRubrique(BSession session, Element parent) throws Exception {
        Element idRubrique = CAImportOperationsUtils.getChildElement(parent, CAImportOperationsUtils.TAG_IDRUBRIQUE);

        if (JadeStringUtil.isIntegerEmpty(CAImportOperationsUtils.getValue(idRubrique))) {
            Element idExterneRubrique = CAImportOperationsUtils.getChildElement(parent,
                    CAImportOperationsUtils.TAG_IDEXTERNERUBRIQUE);

            CARubrique rubrique = new CARubrique();
            rubrique.setSession(session);

            rubrique.setIdExterne(CAImportOperationsUtils.getValue(idExterneRubrique));

            rubrique.setAlternateKey(APIRubrique.AK_IDEXTERNE);

            rubrique.retrieve();

            if (rubrique.isNew()) {
                throw new Exception(session.getLabel(CAImportOperationsUtils.LABEL_RUBRIQUE_NON_RESOLU) + "KEY "
                        + CAImportOperationsUtils.TAG_IDEXTERNERUBRIQUE + " : "
                        + CAImportOperationsUtils.getValue(idExterneRubrique));
            }

            return rubrique.getIdRubrique();
        } else {
            return CAImportOperationsUtils.getValue(idRubrique);
        }
    }

    /**
     * Retourne l'id de la rubrique de compensation.<br/>
     * Si l'id rubrique et l'id externe rubrique ne sont pas spécifiées l'id rubrique par défaut en fonction du type de
     * section sera retourné.
     * 
     * @param session
     * @param parent
     * @param section
     * @return
     * @throws Exception
     */
    public static String getIdRubriqueCompensation(BSession session, Element parent, CASection section)
            throws Exception {
        Element idRubrique = CAImportOperationsUtils.getChildElement(parent, CAImportOperationsUtils.TAG_IDRUBRIQUE);

        if (JadeStringUtil.isIntegerEmpty(CAImportOperationsUtils.getValue(idRubrique))) {
            Element idExterneRubrique = CAImportOperationsUtils.getChildElement(parent,
                    CAImportOperationsUtils.TAG_IDEXTERNERUBRIQUE);

            if (JadeStringUtil.isIntegerEmpty(CAImportOperationsUtils.getValue(idExterneRubrique))) {
                if (section != null) {
                    CAReferenceRubrique referenceRubrique = new CAReferenceRubrique();
                    referenceRubrique.setSession(session);

                    CARubrique defaultRubrique;

                    if (section.getIdTypeSection().equals(APISection.ID_TYPE_SECTION_APG)) {
                        defaultRubrique = (CARubrique) referenceRubrique
                                .getRubriqueByCodeReference(APIReferenceRubrique.APG_FONDS_DE_COMPENSATION);
                    } else if (section.getIdTypeSection().equals(APISection.ID_TYPE_SECTION_IJAI)) {
                        defaultRubrique = (CARubrique) referenceRubrique
                                .getRubriqueByCodeReference(APIReferenceRubrique.IJAI_FONDS_DE_COMPENSATION);
                    } else if (section.getIdTypeSection().equals(APISection.ID_TYPE_SECTION_RENTE_AVS_AI)) {
                        defaultRubrique = (CARubrique) referenceRubrique
                                .getRubriqueByCodeReference(APIReferenceRubrique.RENTES_FONDS_DE_COMPENSATION);
                    } else if (section.getIdTypeSection().equals(APISection.ID_TYPE_SECTION_AF)) {
                        defaultRubrique = (CARubrique) referenceRubrique
                                .getRubriqueByCodeReference(APIReferenceRubrique.AF_FONDS_DE_COMPENSATION);
                    } else if (section.getIdTypeSection().equals(APISection.ID_TYPE_SECTION_DECOMPTE_COTISATION)) {
                        defaultRubrique = (CARubrique) referenceRubrique
                                .getRubriqueByCodeReference(APIReferenceRubrique.RUBRIQUE_DE_LISSAGE);
                    } else {
                        throw new Exception(session.getLabel(CAImportOperationsUtils.LABEL_TYPE_SECTION_INCONNU));
                    }

                    if (defaultRubrique == null) {
                        throw new Exception(session.getLabel(CAImportOperationsUtils.LABEL_RUBRIQUE_NON_RESOLU)
                                + "KEY " + CAImportOperationsUtils.TAG_IDEXTERNERUBRIQUE + " : "
                                + CAImportOperationsUtils.getValue(idExterneRubrique));
                    }

                    return defaultRubrique.getIdRubrique();
                } else {
                    return null;
                }
            } else {
                CARubrique rubrique = new CARubrique();
                rubrique.setSession(session);

                rubrique.setIdExterne(CAImportOperationsUtils.getValue(idExterneRubrique));

                rubrique.setAlternateKey(APIRubrique.AK_IDEXTERNE);

                rubrique.retrieve();

                if (rubrique.isNew()) {
                    throw new Exception(session.getLabel(CAImportOperationsUtils.LABEL_RUBRIQUE_NON_RESOLU) + "KEY "
                            + CAImportOperationsUtils.TAG_IDEXTERNERUBRIQUE + " : "
                            + CAImportOperationsUtils.getValue(idExterneRubrique));
                }

                return rubrique.getIdRubrique();
            }
        } else {
            return CAImportOperationsUtils.getValue(idRubrique);
        }
    }

    /**
     * Retourne l'idSection. Si idSection n'est pas défini alors la section sera résolu grâce au compte annexe.<br/>
     * Si quittancer la section sera créée.
     * 
     * @param session
     * @param transaction
     * @param parent
     * @param idCompteAnnexe
     * @param journal
     * @return
     * @throws Exception
     */
    public static String getIdSection(BSession session, BTransaction transaction, Element parent,
            String idCompteAnnexe, CAJournal journal) throws Exception {
        Element idSection = CAImportOperationsUtils.getChildElement(parent, CAImportOperationsUtils.TAG_IDSECTION);

        if (JadeStringUtil.isIntegerEmpty(CAImportOperationsUtils.getValue(idSection))) {
            Element idExterneSection = CAImportOperationsUtils.getChildElement(parent,
                    CAImportOperationsUtils.TAG_IDEXTERNESECTION);
            Element idTypeSection = CAImportOperationsUtils.getChildElement(parent,
                    CAImportOperationsUtils.TAG_IDTYPESECTION);

            if (JadeStringUtil.isIntegerEmpty(CAImportOperationsUtils.getValue(idTypeSection))) {
                throw new Exception(session.getLabel(CAImportOperationsUtils.LABEL_TYPE_SECTION_INCONNU));
            }

            CASection section = CAImportOperationsUtils.getSection(session, idCompteAnnexe, idExterneSection,
                    idTypeSection);

            if (section.isNew()) {
                if (CAImportOperationsUtils.isQuittancer(parent)) {
                    section.setAlternateKey(null);
                    section.setIdJournal(journal.getIdJournal());
                    section.setDateSection(journal.getDateValeurCG());
                    section.add(transaction);
                } else {
                    throw new Exception(session.getLabel(CAImportOperationsUtils.LABEL_SECTION_NON_RECUPEREE)
                            + " (KEY : " + CAImportOperationsUtils.TAG_IDEXTERNESECTION + " : "
                            + CAImportOperationsUtils.getValue(idExterneSection) + ", KEY "
                            + CAImportOperationsUtils.TAG_IDTYPESECTION + " : "
                            + CAImportOperationsUtils.getValue(idTypeSection) + ", KEY "
                            + CAImportOperationsUtils.TAG_IDCOMPTEANNEXE + " : " + idCompteAnnexe + ")");
                }
            }

            return section.getIdSection();
        } else {
            return CAImportOperationsUtils.getValue(idSection);
        }
    }

    /**
     * Retourne l'idSection pour la compensation.<br/>
     * Si nécessaire la section sera créée.
     * 
     * @param session
     * @param transaction
     * @param parent
     * @param idCompteAnnexe
     * @param journal
     * @return
     * @throws Exception
     */
    public static String getIdSectionCompensation(BSession session, BTransaction transaction, Element parent,
            String idCompteAnnexe, CAJournal journal) throws Exception {
        Element idExterneSection = CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_IDEXTERNESECTION);
        Element idTypeSection = CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_IDTYPESECTION);

        if (JadeStringUtil.isIntegerEmpty(CAImportOperationsUtils.getValue(idTypeSection))) {
            throw new Exception(session.getLabel(CAImportOperationsUtils.LABEL_TYPE_SECTION_INCONNU));
        }

        if (!JadeStringUtil.isBlank(CAImportOperationsUtils.getValue(idExterneSection))) {
            CASection section = CAImportOperationsUtils.getSection(session, idCompteAnnexe, idExterneSection,
                    idTypeSection);

            return section.getIdSection();
        } else if (!CAImportOperationsUtils.getValue(idTypeSection).equals(
                APISection.ID_TYPE_SECTION_DECOMPTE_COTISATION)) {
            String newIdExterneSection = "" + JACalendar.today().getYear();
            String idTypeSectionValue = CAImportOperationsUtils.getValue(idTypeSection);

            if (idTypeSectionValue.equals(APISection.ID_TYPE_SECTION_APG)) {
                newIdExterneSection += APISection.CATEGORIE_SECTION_APG;
            } else if (idTypeSectionValue.equals(APISection.ID_TYPE_SECTION_IJAI)) {
                newIdExterneSection += APISection.CATEGORIE_SECTION_IJAI;
            } else if (idTypeSectionValue.equals(APISection.ID_TYPE_SECTION_RENTE_AVS_AI)) {
                newIdExterneSection += APISection.CATEGORIE_SECTION_RENTES;
            } else if (idTypeSectionValue.equals(APISection.ID_TYPE_SECTION_AF)) {
                newIdExterneSection += APISection.CATEGORIE_SECTION_AF;
            } else {
                throw new Exception(session.getLabel(CAImportOperationsUtils.LABEL_TYPE_SECTION_INCONNU));
            }

            CASectionManager manager = new CASectionManager();
            manager.setSession(session);
            manager.setForIdCompteAnnexe(idCompteAnnexe);
            manager.setLikeIdExterne(newIdExterneSection);
            manager.setForIdTypeSection(idTypeSectionValue);
            manager.setOrderBy(CASectionManager.ORDER_IDEXTERNE_DESCEND);

            manager.find(transaction);

            if (manager.isEmpty()) {
                newIdExterneSection += CAImportOperationsUtils.COMPENSATION_FIRST_SECTION_RIGHT_PART;

                return CAImportOperationsUtils.createSectionCompensation(session, transaction, idCompteAnnexe, journal,
                        newIdExterneSection, idTypeSectionValue);
            } else {
                if (CAImportOperationsUtils.getValue(
                        CAImportOperationsUtils.getChildElement(parent, CAImportOperationsUtils.TAG_IDROLE)).equals(
                        IntRole.ROLE_AFFILIE)) {
                    return ((CASection) manager.getFirstEntity()).getIdSection();
                } else {
                    int lastIdExterneSection = Integer.parseInt(((CASection) manager.getFirstEntity()).getIdExterne());
                    newIdExterneSection = ""
                            + (lastIdExterneSection + CAImportOperationsUtils.COMPENSATION_SECTION_INCREMENT_IDEXTERNE);

                    return CAImportOperationsUtils.createSectionCompensation(session, transaction, idCompteAnnexe,
                            journal, newIdExterneSection, idTypeSectionValue);
                }
            }
        } else {
            return null;
        }
    }

    /**
     * Retourne le libellé de l'opération.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getLibelle(Element parent) throws Exception {
        return CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_LIBELLE));
    }

    /**
     * Retourne la masse (liée au taux).
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getMasse(Element parent) throws Exception {
        return CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_MASSE));
    }

    /**
     * Retourne le montant.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getMontant(Element parent) throws Exception {
        return CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_MONTANT));
    }

    /**
     * Retourne le motif du versement.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getMotif(Element parent) throws Exception {
        return CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_MOTIF));
    }

    /**
     * Retourne la nature de l'ordre de versement. Exemple : 209001.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getNatureOrdre(Element parent) throws Exception {
        return CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_NATUREORDRE));
    }

    /**
     * Retourne les ordres de versement spécifiés dans le document xml.
     * 
     * @param doc
     * @return
     */
    public static NodeList getOrdresVersement(Document doc) {
        return doc.getDocumentElement().getElementsByTagName(CAImportOperationsUtils.TAG_ORDREVERSEMENT);
    }

    /**
     * Retourne les opérations paiement auxiliaires spécifiées dans le document xml.
     * 
     * @param doc
     * @return
     */
    public static NodeList getPaiementAuxiliaires(Document doc) {
        return doc.getDocumentElement().getElementsByTagName(CAImportOperationsUtils.TAG_PAIEMENTAUXILIAIRE);
    }

    /**
     * Retourne les paiements spécifiés dans le document xml.
     * 
     * @param doc
     * @return
     */
    public static NodeList getPaiements(Document doc) {
        return doc.getDocumentElement().getElementsByTagName(CAImportOperationsUtils.TAG_PAIEMENT);
    }

    /**
     * Retourne les paiementsBVR spécifiés dans le document xml.
     * 
     * @param doc
     * @return
     */
    public static NodeList getPaiementsBVR(Document doc) {
        return doc.getDocumentElement().getElementsByTagName(CAImportOperationsUtils.TAG_PAIEMENTBVR);
    }

    /**
     * Retourne le numéro de pièce comptable.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getPieceComptable(Element parent) throws Exception {
        return CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_PIECECOMPTABLE));
    }

    /**
     * Retourne les recouvrements spécifiés dans le document xml.
     * 
     * @param doc
     * @return
     */
    public static NodeList getRecouvrements(Document doc) {
        return doc.getDocumentElement().getElementsByTagName(CAImportOperationsUtils.TAG_RECOUVREMENT);
    }

    /**
     * Retourne la référence du virement BVR.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getReferenceBVR(Element parent) throws Exception {
        return CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_REFERENCEBVR));
    }

    /**
     * Retourne la référence interne.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getReferenceInterne(Element parent) throws Exception {
        return CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_REFERENCEINTERNE));
    }

    /**
     * Retourne la rubrique. Si idrubrique n'est pas défini alors la rubrique sera résolu grâce à la clef alternée.
     * 
     * @param session
     * @param parent
     * @return
     * @throws Exception
     */
    public static CARubrique getRubrique(BSession session, Element parent) throws Exception {
        Element idRubrique = CAImportOperationsUtils.getChildElement(parent, CAImportOperationsUtils.TAG_IDRUBRIQUE);

        CARubrique rubrique = new CARubrique();
        rubrique.setSession(session);

        rubrique.setAlternateKey(APIRubrique.AK_IDEXTERNE);

        if (JadeStringUtil.isIntegerEmpty(CAImportOperationsUtils.getValue(idRubrique))) {
            Element idExterneRubrique = CAImportOperationsUtils.getChildElement(parent,
                    CAImportOperationsUtils.TAG_IDEXTERNERUBRIQUE);

            rubrique.setIdExterne(CAImportOperationsUtils.getValue(idExterneRubrique));

            rubrique.retrieve();

            if (rubrique.isNew()) {
                throw new Exception(session.getLabel(CAImportOperationsUtils.LABEL_RUBRIQUE_NON_RESOLU) + "KEY "
                        + CAImportOperationsUtils.TAG_IDEXTERNERUBRIQUE + " : "
                        + CAImportOperationsUtils.getValue(idExterneRubrique));
            }

        } else {
            rubrique.setIdRubrique(CAImportOperationsUtils.getValue(idRubrique));
            rubrique.retrieve();
        }
        return rubrique;
    }

    /**
     * Retrouve la section en fonction de l'idCompteAnnexe, de l'idExterne et du type de section.
     * 
     * @param session
     * @param idCompteAnnexe
     * @param idExterneSection
     * @param idTypeSection
     * @return
     * @throws Exception
     */
    private static CASection getSection(BSession session, String idCompteAnnexe, Element idExterneSection,
            Element idTypeSection) throws Exception {
        CASection section = new CASection();
        section.setSession(session);

        section.setIdCompteAnnexe(idCompteAnnexe);
        section.setIdExterne(CAImportOperationsUtils.getValue(idExterneSection));
        section.setIdTypeSection(CAImportOperationsUtils.getValue(idTypeSection));

        section.setAlternateKey(CASection.AK_IDEXTERNE);

        section.retrieve();

        return section;
    }

    /**
     * Retourne le taux (lié à la masse).
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getTaux(Element parent) throws Exception {
        return CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_TAUX));
    }

    /**
     * Retourne le type de virement.
     * 
     * @param parent
     * @return APIOperationOrdreVersement.BVR ou APIOperationOrdreVersement.VIREMENT sinon null
     * @throws Exception
     */
    public static String getTypeVirement(Element parent) throws Exception {
        Element typeVirement = CAImportOperationsUtils
                .getChildElement(parent, CAImportOperationsUtils.TAG_TYPEVIREMENT);

        if (typeVirement == null) {
            if (CAImportOperationsUtils.getChildElement(parent, CAImportOperationsUtils.TAG_TYPEBVR) != null) {
                return APIOperationOrdreVersement.BVR;
            } else {
                return null;
            }
        } else {
            return APIOperationOrdreVersement.VIREMENT;
        }
    }

    /**
     * Retourne la valeur de l'attribut.
     * 
     * @param e
     * @return
     * @throws Exception
     */
    private static String getValue(Element e) throws Exception {
        if (e != null) {
            return e.getAttribute(CAImportOperationsUtils.TAG_VALUE);
        } else {
            return null;
        }
    }

    /**
     * L'ordre de versement doit-il être bloqué ?
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static Boolean isBloque(Element parent) throws Exception {
        String bloque = CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_BLOQUERVERSEMENT));

        if ((!JadeStringUtil.isBlank(bloque)) && (bloque.equals(CAImportOperationsUtils.TAG_VALUE_TRUE))) {
            return new Boolean(true);
        } else {
            return new Boolean(false);
        }
    }

    /**
     * L'opération doit-elle être quittancer ?
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static boolean isQuittancer(Element parent) throws Exception {
        String quittancer = CAImportOperationsUtils.getValue(CAImportOperationsUtils.getChildElement(parent,
                CAImportOperationsUtils.TAG_QUITTANCER));

        return ((!JadeStringUtil.isBlank(quittancer)) && (quittancer.equals(CAImportOperationsUtils.TAG_VALUE_TRUE)));
    }
}
