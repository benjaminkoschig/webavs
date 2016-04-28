/*
 * Cr�� le 27 juin 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.naos.db.affiliation;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.log.JadeLogger;
import globaz.naos.api.IAFAdhesion;
import globaz.naos.db.adhesion.AFAdhesion;
import globaz.naos.db.adhesion.AFAdhesionManager;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.lienAffiliation.AFLienAffiliation;
import globaz.naos.db.lienAffiliation.AFLienAffiliationManager;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.db.planAffiliation.AFPlanAffiliationManager;
import globaz.naos.db.processFacturation.AFProcessFacturationManager;
import globaz.naos.db.processFacturation.AFProcessFacturationViewBean;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationManager;
import globaz.naos.exceptions.AFIdeNumberNoMatchException;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.api.ITIAdministration;
import globaz.pyxis.api.helper.ITIAdministrationHelper;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.webavs.common.ICommonConstantes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Classe regroupant diff�rentes m�thodes utilitaire sur une affiliation donn�e
 * 
 * @author dgi
 */
public class AFAffiliationUtil {
    public static final String DOMAINE_RECOUVREMENT = "519012";
    // domaines
    public static final String DOMAINE_REMBOURSEMENT = "519010";
    public static final String DOMAINE_STANDARD = "519004";
    private static final String TYPE_ALLOC_AF_COL_AGRI = "C";
    private static final String TYPE_ALLOC_AF_PAYSAN = "P";
    private static final String TYPE_ALLOC_AF_SALARIE = "S";
    private static final String TYPE_ALLOC_AF_TRA_AGRI = "T";
    private static final String TYPE_EXPLOITATION = "508021";

    /**
     * Ajoute un nombre de jour � une date et retourne une nouvelle date au format dd.mm.yyyy
     * 
     * @param date
     * @param daysToAdd
     * @return
     */
    public static String addDaysToDate(JADate date, int daysToAdd) throws Exception {
        JACalendar cal = new JACalendarGregorian();
        JADate newDate = cal.addDays(date, daysToAdd);
        return JACalendar.format(newDate);
    }

    /**
     * 
     * d�sactive la g�n�ration du suivi LAA/LPP
     * d�sactive les appels aux external services
     * d�sactive les appels aux m�thodes _before* et _after* de BEntity
     * d�sactive les appels � la m�thode _validate de BEntity
     */
    public static void disableExtraProcessingForAffiliation(AFAffiliation affiliation) {

        affiliation.setWantGenerationSuiviLAALPP(false);
        affiliation.wantCallExternalServices(false);
        affiliation.wantCallMethodAfter(false);
        affiliation.wantCallMethodBefore(false);
        affiliation.wantCallValidate(false);

    }

    /**
     * Ajoute un nombre de jour � une date et retourne une nouvelle date au format dd.mm.yyyy
     * 
     * @param date
     * @param daysToAdd
     * @return
     */
    public static String addDaysToDate(JADate date, String daysToAdd) throws Exception {

        int _int;

        try {
            _int = Double.valueOf(daysToAdd).intValue();
        } catch (Exception e) {
            _int = 0;
        }

        return AFAffiliationUtil.addDaysToDate(date, _int);
    }

    /**
     * Ajoute un nombre de jour � une date et retourne une nouvelle date au format dd.mm.yyyy
     * 
     * @param date
     * @param daysToAdd
     * @return
     */
    public static String addDaysToDate(String _date, String daysToAdd) throws Exception {
        JADate date = new JADate(_date);
        return AFAffiliationUtil.addDaysToDate(date, daysToAdd);
    }

    public static List<AFAffiliation> loadAffiliationUsingNumeroIde(BSession session, String numeroIde,
            boolean isAnnoncePassive) throws Exception {

        if (JadeStringUtil.isBlankOrZero(numeroIde)) {
            throw new Exception(
                    "loadAffiliationUsingNumeroIde : unable to load affiliation using numero ide without numero ide");
        }

        AFAffiliationManager affiliationManager = new AFAffiliationManager();
        affiliationManager.setSession(session);
        affiliationManager.setForNumeroIde(numeroIde);
        affiliationManager.find(BManager.SIZE_NOLIMIT);

        if (affiliationManager.getSize() <= 0) {

            // Il est possible qu'une caisse de compensation soit enregistr�e passivement � une entit� IDE sans pour
            // autant avoir une affiliation correspondante
            if (isAnnoncePassive) {
                return null;
            }

            throw new AFIdeNumberNoMatchException(
                    session.getLabel("NAOS_AFFILIATION_UTIL_LOAD_AFFILIATION_USING_NUMERO_IDE_ERREUR_PAS_AFFILIATION_TROUVEE"));
        } else {

            List<AFAffiliation> listAffiliation = new ArrayList<AFAffiliation>();

            for (int i = 0; i < affiliationManager.getSize(); i++) {
                listAffiliation.add((AFAffiliation) affiliationManager.getEntity(i));
            }

            return listAffiliation;
        }

    }

    public static List<AFAffiliation> loadAffiliationUsingNumeroIdeForCheckMultiAff(BSession session, String numeroIde,
            String idAffiliationNotMe) throws Exception {

        if (JadeStringUtil.isBlankOrZero(numeroIde)) {
            throw new Exception(
                    "loadAffiliationUsingNumeroIde : unable to load affiliation using numero ide without numero ide");
        }

        AFAffiliationManager affiliationManager = new AFAffiliationManager();
        affiliationManager.setSession(session);
        affiliationManager.setForNumeroIde(numeroIde);
        affiliationManager.find(BManager.SIZE_NOLIMIT);

        if (affiliationManager.getSize() >= 1) {

            List<AFAffiliation> listAffiliation = new ArrayList<AFAffiliation>();

            for (int i = 0; i < affiliationManager.getSize(); i++) {

                AFAffiliation aAffiliation = (AFAffiliation) affiliationManager.getEntity(i);
                if (!idAffiliationNotMe.equalsIgnoreCase(aAffiliation.getAffiliationId())) {
                    listAffiliation.add(aAffiliation);
                }
            }

            if (listAffiliation.size() < 1) {
                return null;
            }

            return listAffiliation;
        }

        return null;

    }

    /**
     * Retourne l'adresse d'exploitation de l'affiliation
     * 
     * @param affToUse
     *            l'affiliation
     * @return l'adresse d'exploitation de l'affiliation
     */
    public static TIAdresseDataSource getAdresse(AFAffiliation affToUse) throws Exception {
        return affToUse.getTiers().getAdresseAsDataSource(AFAffiliationUtil.TYPE_EXPLOITATION,
                IConstantes.CS_APPLICATION_DEFAUT, JACalendar.todayJJsMMsAAAA(), true);
    }

    /**
     * Renvoie le domaine � utiliser pour une type d'adresse donn� (courrier, remboursement ou recouvrement) sp�cifi�
     * dans le/les plans de l'affiliation.
     * 
     * @param affToUse
     *            l'affiliation concern�e
     * @param domaine
     *            le domaine recherch� (DOMAINE_STANDARD, DOMAINE_REMBOURSEMENT ou DOMAINE_RECOUVREMENT)
     * @return le domaine � utiliser ou "0" si non sp�cifi� ou diff�rents par plan
     * @throws Exception
     */
    public static String getAdresseDomainePlan(AFAffiliation affToUse, String domaine, String genre) throws Exception {
        if ((affToUse == null) || (affToUse.getSession() == null)) {
            return "0";
        }
        String idPlan = null;
        AFProcessFacturationManager affiliationMana = new AFProcessFacturationManager();
        affiliationMana.setSession(affToUse.getSession());
        affiliationMana.setForIdAffiliation(affToUse.getAffiliationId());
        if (CaisseHelperFactory.CS_AFFILIE_PARITAIRE.equals(genre)) {
            affiliationMana.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
        } else if (CaisseHelperFactory.CS_AFFILIE_PERSONNEL.equals(genre)) {
            affiliationMana.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
        }
        affiliationMana.find();
        if (affiliationMana.size() > 0) {
            idPlan = ((AFProcessFacturationViewBean) affiliationMana.getFirstEntity()).getIdPlanAffiliation();
        }
        // recherche des plans
        // si plusieurs plan, tester si domaine commun, vide sinon
        AFPlanAffiliationManager planAffManager = new AFPlanAffiliationManager();
        planAffManager.setSession(affToUse.getSession());
        planAffManager.setForAffiliationId(affToUse.getAffiliationId());
        if (!JadeStringUtil.isIntegerEmpty(idPlan)) {
            planAffManager.setForPlanAffiliationId(idPlan);
        }
        planAffManager.find();
        String domaineResult = "0";
        for (int i = 0; i < planAffManager.size(); i++) {
            AFPlanAffiliation planAffiliation = (AFPlanAffiliation) planAffManager.getEntity(i);
            String domainePlan = "0";
            if (ICommonConstantes.CS_APPLICATION_COTISATION.equals(domaine)
                    || AFAffiliationUtil.DOMAINE_STANDARD.equals(domaine)) {
                domainePlan = planAffiliation.getDomaineCourrier();
            } else if (AFAffiliationUtil.DOMAINE_REMBOURSEMENT.equals(domaine)) {
                domainePlan = planAffiliation.getDomaineRemboursement();
            } else if (AFAffiliationUtil.DOMAINE_RECOUVREMENT.equals(domaine)) {
                domainePlan = planAffiliation.getDomaineRecouvrement();
            }
            if (!domaineResult.equals(domainePlan)) {
                if (JadeStringUtil.isIntegerEmpty(domaineResult)) {
                    domaineResult = domainePlan;
                } else {
                    return "0";
                }
            }
        }
        return domaineResult;
    }

    /**
     * Retourne l'adresse d'exploitation de l'affiliation
     * 
     * @param affToUse
     *            l'affiliation
     * @return l'adresse d'exploitation de l'affiliation
     */
    public static TIAvoirAdresse getAdresseExploitation(AFAffiliation affToUse) {
        // rechercher l'adresse de l'affilie
        TIAvoirAdresse adresseCriteres = new TIAvoirAdresse();
        adresseCriteres.setIdTiers(affToUse.getIdTiers());
        adresseCriteres.setSession(affToUse.getSession());
        adresseCriteres.setIdExterne(affToUse.getAffilieNumero());
        adresseCriteres.setIdApplication(IConstantes.CS_APPLICATION_DEFAUT);
        // recherche adresse d'exploitation
        adresseCriteres.setTypeAdresse(AFAffiliationUtil.TYPE_EXPLOITATION);
        TIAvoirAdresse exploitation = adresseCriteres.findCurrentRelation();
        if (exploitation == null) {
            // recherche sans no d'affili�
            adresseCriteres.setIdExterne("");
            exploitation = adresseCriteres.findCurrentRelation();
            if (exploitation == null) {
                // recherche courrier domaine cotisation
                adresseCriteres.setIdExterne(affToUse.getAffilieNumero());
                adresseCriteres.setIdApplication(ICommonConstantes.CS_APPLICATION_COTISATION);
                adresseCriteres.setTypeAdresse(IConstantes.CS_AVOIR_ADRESSE_COURRIER);
                exploitation = adresseCriteres.findCurrentRelation();
                if (exploitation == null) {
                    // recherche sans no d'affili�
                    adresseCriteres.setIdExterne("");
                    exploitation = adresseCriteres.findCurrentRelation();
                    if (exploitation == null) {
                        // Domaine standard
                        adresseCriteres.setIdExterne(affToUse.getAffilieNumero());
                        adresseCriteres.setIdApplication(IConstantes.CS_APPLICATION_DEFAUT);
                        adresseCriteres.setTypeAdresse(IConstantes.CS_AVOIR_ADRESSE_COURRIER);
                        exploitation = adresseCriteres.findCurrentRelation();
                        if (exploitation == null) {
                            // recherche sans no d'affili�
                            adresseCriteres.setIdExterne("");
                            exploitation = adresseCriteres.findCurrentRelation();
                        }
                    }
                }
            }
        }
        // recherche domicile
        adresseCriteres.setIdApplication(IConstantes.CS_APPLICATION_DEFAUT);
        adresseCriteres.setTypeAdresse(IConstantes.CS_AVOIR_ADRESSE_DOMICILE);
        adresseCriteres.setIdExterne(affToUse.getAffilieNumero());
        TIAvoirAdresse domicile = adresseCriteres.findCurrentRelation();
        if (domicile == null) {
            // recherche dom sans no d'affili�
            adresseCriteres.setIdExterne("");
            domicile = adresseCriteres.findCurrentRelation();
        }

        if ((CodeSystem.TYPE_AFFILI_EMPLOY.equals(affToUse.getTypeAffiliation()))
                || (CodeSystem.TYPE_AFFILI_LTN.equals(affToUse.getTypeAffiliation()) || (CodeSystem.TYPE_AFFILI_EMPLOY_D_F
                        .equals(affToUse.getTypeAffiliation())))) {
            // si employeur, utiliser domicile si existante, exploitation sinon
            if (domicile != null) {
                return domicile;
            } else {
                return exploitation;
            }
        } else {
            // pour IND, NA, etc, utiliser exploitation, domicile sinon
            if (exploitation == null) {
                return domicile;
            } else {
                return exploitation;
            }
        }
    }

    /**
     * R�cup�ration de toutes les affiliations pour un num�ro d'affili� donn�
     * 
     * @param session Une session de type BSession
     * @param numAffilie Un num�ro d'affili�
     * @return Une liste d'affiliation
     * @throws Exception
     */
    public static List<AFAffiliation> findAffiliations(BSession session, String numAffilie) throws Exception {

        List<AFAffiliation> lstAffiliation = new ArrayList<AFAffiliation>();

        if (session == null) {
            throw new Exception("Impossible de r�cup�rer les affiliations. La session est null");
        }

        if (!JadeStringUtil.isEmpty(numAffilie)) {
            AFAffiliationManager manager = new AFAffiliationManager();
            manager.setSession(session);
            manager.setForAffilieNumero(numAffilie);

            try {
                manager.find(BManager.SIZE_NOLIMIT);
            } catch (Exception e) {
                throw new Exception("Impossible de r�cup�rer les affiliations pour le num�ro d'affili� : " + numAffilie
                        + ")", e);
            }

            for (int i = 0; i < manager.size(); i++) {
                AFAffiliation aff = (AFAffiliation) manager.getEntity(i);
                lstAffiliation.add(aff);
            }
        }

        return lstAffiliation;
    }

    public static AFAffiliation getAffiliation(String idAffiliation, BSession session) throws Exception {
        if (idAffiliation != null) {
            AFAffiliation affiliation = new AFAffiliation();
            affiliation.setSession(session);
            affiliation.setAffiliationId(idAffiliation);
            try {
                affiliation.retrieve();
            } catch (Exception e) {
                throw new Exception("Impossible de r�cup�rer l'affiliation pour l'id : " + idAffiliation + ")", e);
            }
            return affiliation;
        }
        return null;
    }

    /**
     * Retourne l'affiliation � laquelle les cotisations AF sont factur�es.<br>
     * Si l'affiliation donn�e en param�tre contient une cotisation AF (AF factur�s), cette m�me affilition sera
     * retourn�e.<br>
     * Par contre, si la cotisation AF contient l'indication qu'elle est factur� � la maison m�re, le lien de type
     * "Succursale de" est recherch� afin de retrouver l'affiliation maison m�re.<br>
     * Dans le cas o� le lien "Succursale de" n'est pas applicable, il est �galement possible d'utiliser le type de lien
     * "D�compte AF sous" que cette m�thode tentera �galement d'utiliser pour rechercher l'affiliation de facturation.
     * 
     * @param affToUse
     *            l'affiliation concern�e (succursale).
     * @param date
     *            la date utilis�e pour la recherche.
     * @return l'affiliation � laquelle les cotisations AF sont factur�es ou null si l'affiliation n'est pas active
     * @exception eexception
     *                si un erreur survient lors de la recherche
     */
    public static AFAffiliation getAffiliationFacturationAF(AFAffiliation affToUse, String date) throws Exception {
        if ((affToUse.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_EMPLOY))
                || (affToUse.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_LTN))
                || (affToUse.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_EMPLOY_D_F))) {
            // type employeur, tester si succursale
            return AFAffiliationUtil.getAffiliationFacturationAF(affToUse, date,
                    AFAffiliationUtil.TYPE_ALLOC_AF_SALARIE);
        } else {
            // sinon simplement le test sur la validit� de l'affiliation
            return AFAffiliationUtil.getAffiliationFacturationAF(affToUse, date, null);
        }
    }

    /**
     * Retourne l'affiliation � laquelle les cotisations AF sont factur�es.<br>
     * Si l'affiliation donn�e en param�tre contient une cotisation AF (AF factur�s), cette m�me affilition sera
     * retourn�e.<br>
     * Par contre, si la cotisation AF contient l'indication qu'elle est factur� � la maison m�re, le lien de type
     * "Succursale de" est recherch� afin de retrouver l'affiliation maison m�re.<br>
     * Dans le cas o� le lien "Succursale de" n'est pas applicable, il est �galement possible d'utiliser le type de lien
     * "D�compte AF sous" que cette m�thode tentera �galement d'utiliser pour rechercher l'affiliation de facturation.
     * 
     * @param affToUse
     *            l'affiliation concern�e (succursale).
     * @param date
     *            la date utilis�e pour la recherche.
     * @param typeAllocataire
     *            type d'allocataire (salari�,non actif,paysan,collaborateur agricole, travailleur agricole)
     * @return l'affiliation � laquelle les cotisations AF sont factur�es ou null si l'affiliation n'est pas active
     * @exception eexception
     *                si un erreur survient lors de la recherche
     */
    public static AFAffiliation getAffiliationFacturationAF(AFAffiliation affToUse, String date, String typeAllocataire)
            throws Exception {
        // pr�-conditions
        if ((affToUse == null) || (affToUse.getSession() == null) || JadeStringUtil.isEmpty(date)) {
            return null;
        }

        // 1) pour tous les type d'affiliation, tester si la date donn�e est
        // dans la p�riode d'affiliation
        if (JadeStringUtil.isEmpty(affToUse.getDateFin())) {
            // affiliation ouverte
            if (!BSessionUtil.compareDateFirstLowerOrEqual(affToUse.getSession(), affToUse.getDateDebut(), date)) {
                // la date donn�e est avant le d�but d'affiliation
                affToUse.getSession().addWarning(affToUse.getSession().getLabel("NAOS_AF_PAS_ACTIVE"));
                return null;
            }
        } else {
            // affiliation radi�e
            if (!BSessionUtil.compareDateBetweenOrEqual(affToUse.getSession(), affToUse.getDateDebut(),
                    affToUse.getDateFin(), date)) {
                // la date donn�e ne se trouve pas dans la p�riode d'affiliation
                affToUse.getSession().addWarning(affToUse.getSession().getLabel("NAOS_AF_PAS_ACTIVE"));
                return null;
            }
        }

        // 2) pour les employeurs, recherche les cotisation AF et tester si la
        // date donn�e est bien dans
        // la p�riode de l'assurance et qu'une masse salariale est saisie
        if (AFAffiliationUtil.TYPE_ALLOC_AF_SALARIE.equals(typeAllocataire)) {
            // recherche des coti AF
            AFCotisationManager cotisMgr = new AFCotisationManager();
            cotisMgr.setForDate(date);
            cotisMgr.setForAffiliationId(affToUse.getAffiliationId());
            cotisMgr.setSession(affToUse.getSession());
            cotisMgr.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AF);
            cotisMgr.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
            cotisMgr.find();
            if (cotisMgr.size() == 0) {
                // aucune coti AF
                affToUse.getSession().addWarning(affToUse.getSession().getLabel("NAOS_AF_AUCUNE_COTI"));
                return null;
            } else {
                // coti AF trouv�es
                for (int i = 0; i < cotisMgr.size(); i++) {
                    AFCotisation coti = (AFCotisation) cotisMgr.getEntity(i);
                    if (coti.getMaisonMere().booleanValue()) {
                        // factur�e � la maison m�re
                        // recherche du lien succursale de et de la maison m�re
                        // TODO effectuer la recherche sur le type de lien
                        // "D�compte AF sous" si la maison m�re n'est pas
                        // d�finie
                        AFAffiliation mMere;
                        if ((mMere = AFAffiliationUtil.isSuccursale(affToUse, date)) != null) {
                            // test sur la maison m�re
                            if (!AFAffiliationUtil.isActifAF(mMere, date, typeAllocataire)) {
                                // effacer les erreur de l'appel r�cursif
                                affToUse.getSession().getErrors();
                                // ajouter l'erreur
                                affToUse.getSession().addWarning(
                                        affToUse.getSession().getLabel("NAOS_AF_AUCUNE_COTI_MM"));
                                return null;
                            }
                            return mMere;
                        } else {
                            // indiqu� comme factur� � la maison m�re mais
                            // celle-ci n'est pas d�finie -> erreur
                            affToUse.getSession().addWarning(affToUse.getSession().getLabel("NAOS_AF_MM_NON_DEFINIE"));
                            return null;
                        }
                    } else {
                        // pas une succursale
                        // tester la masse salariale si factur� par accompte,
                        // sinon ok
                        if (!affToUse.isReleveParitaire().booleanValue()
                                || (!affToUse.getCodeFacturation().equals(CodeSystem.CODE_FACTU_MONTANT_LIBRE))) {

                            if (AFAffiliationUtil.isMaisonMere(affToUse, date) != null) {

                            } else {
                                if (JadeStringUtil.isIntegerEmpty(coti.getMasseAnnuelle())) {
                                    // aucune masse saisie
                                    affToUse.getSession().addWarning(
                                            affToUse.getSession().getLabel("NAOS_AF_AUCUNE_MASSE"));
                                    return null;
                                }
                            }
                        }
                    }
                } // pour toutes les cotisations
            }
        }

        // 3) pour les petits paysans, leurs collaborateurs et/ou leurs
        // travaileurs, tester la branche �conomique � 01 (paysan)
        if (AFAffiliationUtil.TYPE_ALLOC_AF_PAYSAN.equals(typeAllocataire)
                && AFAffiliationUtil.TYPE_ALLOC_AF_COL_AGRI.equals(typeAllocataire)
                && AFAffiliationUtil.TYPE_ALLOC_AF_TRA_AGRI.equals(typeAllocataire)) {
            if (!CodeSystem.BRANCHE_ECO_AGRICULTURE.equals(affToUse.getBrancheEconomique())) {
                // br eco diff�rente d'agriculture -> erreur
                affToUse.getSession().addWarning(affToUse.getSession().getLabel("NAOS_AF_BRANCHE_ECO"));
                return null;
            }
        }

        // 4) pour les petits paysan, tester si des cot. pers AVS sont bien
        // pr�sentent
        if (AFAffiliationUtil.TYPE_ALLOC_AF_PAYSAN.equals(typeAllocataire)) {
            // recherche des coti AF
            AFCotisationManager cotisMgr = new AFCotisationManager();
            cotisMgr.setForDate(date);
            cotisMgr.setForAffiliationId(affToUse.getAffiliationId());
            cotisMgr.setSession(affToUse.getSession());
            cotisMgr.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
            cotisMgr.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
            cotisMgr.find();
            if (cotisMgr.size() == 0) {
                // aucune coti AVS
                affToUse.getSession().addWarning(affToUse.getSession().getLabel("NAOS_AF_AUCUNE_COTI_AVS"));
                return null;
            }
        }

        // 5) pour les travailleurs agricoles, tester si coti LFA/LJA pr�sentes
        if (AFAffiliationUtil.TYPE_ALLOC_AF_TRA_AGRI.equals(typeAllocataire)) {
            // recherche des coti LFA/LJA
            // TODO ajouter un nouveau cs pour le type d'assurance LFA/LFA afin
            // de pouvoir les retrouver
            /*
             * AFCotisationJAssuranceManager cotisMgr = new AFCotisationJAssuranceManager(); cotisMgr.setForDate(date);
             * cotisMgr.setForAffiliationId(affToUse.getAffiliationId()); cotisMgr.setSession(affToUse.getSession());
             * cotisMgr.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_);
             * cotisMgr.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL); cotisMgr.find(); if(cotisMgr.size()==0){
             * // aucune coti LFA/LJA return false; }
             */
        }
        // si aucun probl�mes rencontr�s, ok
        return affToUse;
    }

    /**
     * Retourne l'affiliation � laquelle les cotisations AF sont factur�es.<br>
     * Si l'affiliation donn�e en param�tre contient une cotisation AF (AF factur�s), cette m�me affilition sera
     * retourn�e.<br>
     * Par contre, si la cotisation AF contient l'indication qu'elle est factur� � la maison m�re, le lien de type
     * "Succursale de" est recherch� afin de retrouver l'affiliation maison m�re.<br>
     * Dans le cas o� le lien "Succursale de" n'est pas applicable, il est �galement possible d'utiliser le type de lien
     * "D�compte AF sous" que cette m�thode tentera �galement d'utiliser pour rechercher l'affiliation de facturation.
     * 
     * @param affToUse
     *            l'affiliation concern�e (succursale).
     * @param date
     *            la date utilis�e pour la recherche.
     * @return l'affiliation � laquelle les cotisations AF sont factur�es ou null si inconnu.
     * @exception exception
     *                si une erreur survient lors de la recherche.
     */
    public static AFAffiliation getAffiliationFacturationAFOld(AFAffiliation affToUse, String date) throws Exception {
        // pr�-conditions
        if ((affToUse == null) || (affToUse.getSession() == null) || JadeStringUtil.isEmpty(date)) {
            return null;
        }
        // recherche d'une cotisation AF
        AFCotisationManager cotisMgr = new AFCotisationManager();
        cotisMgr.setForDate(date);
        cotisMgr.setForAffiliationId(affToUse.getAffiliationId());
        cotisMgr.setSession(affToUse.getSession());
        cotisMgr.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AF);
        // n'est-ce pas application qu'aux employeurs? � voir
        // cotisMgr.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
        cotisMgr.find();
        if (cotisMgr.size() == 0) {
            // aucune coti AF de factur�e
            return null;
        } else {
            // test de la notion "factur� � la maison m�re
            // en supposant que soit toutes les cotisations sont factur�es � la
            // maison m�re
            // soit aucune, effectuer le test sur la premi�re coti
            if (((AFCotisation) cotisMgr.getFirstEntity()).getMaisonMere().booleanValue()) {
                // factur�e � la maison m�re
                // recherche du lien succursale de et de la maison m�re
                // TODO effectuer la recherche sur le type de lien
                // "D�compte AF sous" si la maison m�re n'est pas d�finie
                return AFAffiliationUtil.isSuccursale(affToUse, date);
            } // sinon, retourne l'affilition donn�e
        }
        return affToUse;
    }

    /**
     * Retourne une caisse externe demand� si l'affiliation contient une telle information
     * 
     * @param affToUse
     *            l'affiliation � tester
     * @param anneeDate
     *            l'ann�e recherch�e au format aaaa ou une date pr�cise au foramt jj.mm.aaaa. Date du jour is null.
     *            Note: pour affiliation radi�e, la date de radiation est utilis�e afin de retourner la derni�re caisse
     *            active
     * @param genreCaisse
     *            LAA ou LPP
     * @return un objet de type AFSuiviCaisseAffiliation si une caisse externe est trouv�e, null sinon
     * @throws Exception
     *             si une erreur survient
     */
    public static AFSuiviCaisseAffiliation getCaisseExterne(AFAffiliation affToUse, String anneeDate, String genreCaisse)
            throws Exception {
        AFSuiviCaisseAffiliationManager mgr = new AFSuiviCaisseAffiliationManager();
        mgr.setSession(affToUse.getSession());
        mgr.setForAffiliationId(affToUse.getAffiliationId());
        mgr.setForGenreCaisse(genreCaisse);
        if (anneeDate == null) {
            if (JadeStringUtil.isEmpty(affToUse.getDateFin())) {
                // affiliation ouverte, recherche sur la date du jour
                mgr.setForDate(JACalendar.todayJJsMMsAAAA());
            } else {
                // affiliation radi�e, recherche sur la derni�re active (date de
                // radiation)
                mgr.setForDate(affToUse.getDateFin());
            }
        } else if (anneeDate.trim().length() == 4) {
            // ann�e
            mgr.setForAnneeActive(anneeDate);
        } else {
            // jour pr�cis jj.mm.aaaa
            mgr.setForDate(anneeDate);
        }
        mgr.find();
        if (mgr.size() != 0) {
            return (AFSuiviCaisseAffiliation) mgr.getFirstEntity();
        }
        return null;
    }

    /**
     * Retourne la caisse AF externe de l'affiliation
     * 
     * @param affToUse
     *            l'affiliation � tester
     * @param anneeDate
     *            l'ann�e concern�e au format aaaa ou une date pr�cise au foramt jj.mm.aaaa. Date du jour is null. Note:
     *            pour affiliation radi�e, la date de radiation est utilis�e afin de retourner la derni�re caisse active
     * @return un objet de type AFSuiviCaisseAffiliation si une caisse externe est trouv�e, null sinon
     * @throws Exception
     *             si une erreur survient
     */
    public static AFSuiviCaisseAffiliation getCaissseAF(AFAffiliation affToUse, String anneeDate) throws Exception {
        return AFAffiliationUtil.getCaisseExterne(affToUse, anneeDate, CodeSystem.GENRE_CAISSE_AF);
    }

    /**
     * Retourne la caisse AVS externe de l'affiliation
     * 
     * @param affToUse
     *            l'affiliation � tester
     * @param anneeDate
     *            l'ann�e concern�e au format aaaa ou une date pr�cise au foramt jj.mm.aaaa. Date du jour is null. Note:
     *            pour affiliation radi�e, la date de radiation est utilis�e afin de retourner la derni�re caisse active
     * @return un objet de type AFSuiviCaisseAffiliation si une caisse externe est trouv�e, null sinon
     * @throws Exception
     *             si une erreur survient
     */
    public static AFSuiviCaisseAffiliation getCaissseAVS(AFAffiliation affToUse, String anneeDate) throws Exception {
        return AFAffiliationUtil.getCaisseExterne(affToUse, anneeDate, CodeSystem.GENRE_CAISSE_AVS);
    }

    /**
     * Retourne le canton d�fini pour les AF. Si le canton est sp�cifi� dans la cotisation AF, celui-ci est retourn�.
     * Sinon, c'est le canton de l'adresse de l'affili� qui est utilis�
     * 
     * @param affToUse
     *            l'affiliation � interroger
     * @param date
     *            la date utilis�e pour la recherche
     * @return le canton d�fini pour les AF
     * @throws Exception
     *             si une erreur survient lors de la recherche.
     */
    public static String getCantonAF(AFAffiliation affToUse, String date) throws Exception {
        String canton = AFAffiliationUtil.getCantonAFCS(affToUse, date);
        if (!JadeStringUtil.isEmpty(canton)) {
            canton = affToUse.getSession().getCode(canton);
        }
        if (JadeStringUtil.isEmpty(canton)) {
            // aucune information trouv�e, rechercher le canton dans l'adresse
            // du tiers
            TIAdresseDataSource ds = new TIAdresseDataSource();
            ds.setSession(affToUse.getSession());
            ds.load(AFAffiliationUtil.getAdresseExploitation(affToUse));
            canton = ds.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_CANTON_COURT);
        }
        return canton;
    }

    /**
     * Retourne le code syst�me du canton d�fini pour les AF.
     * 
     * @param affToUse
     *            l'affiliation � interroger
     * @param date
     *            la date utilis�e pour la recherche
     * @return le code syst�me du canton d�fini pour les AF
     * @throws Exception
     *             si une erreur survient lors de la recherche.
     */
    public static String getCantonAFCS(AFAffiliation affToUse, String date) throws Exception {
        // pr�-conditions
        if ((affToUse == null) || (affToUse.getSession() == null) || JadeStringUtil.isEmpty(date)) {
            return null;
        }
        String codeCanton = "";
        // recherche d'une cotisation AF
        AFCotisationManager cotisMgr = new AFCotisationManager();
        cotisMgr.setForDate(date);
        cotisMgr.setForAffiliationId(affToUse.getAffiliationId());
        cotisMgr.setSession(affToUse.getSession());
        cotisMgr.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AF);
        cotisMgr.find();
        if (cotisMgr.size() == 1) {
            // une cotisation AF existe
            codeCanton = ((AFCotisation) cotisMgr.getFirstEntity()).getAssurance().getAssuranceCanton();
        }
        return codeCanton;
    }

    /**
     * Retourne le code syst�me du canton d�fini pour les AF.
     * 
     * @param affToUse
     *            l'affiliation � interroger
     * @param date
     *            la date utilis�e pour la recherche
     * @return le code syst�me du canton d�fini pour les AF
     * @throws Exception
     *             si une erreur survient lors de la recherche.
     */
    public static String getCantonAFCSForDS(AFAffiliation affToUse, String date) throws Exception {
        // pr�-conditions
        if ((affToUse == null) || (affToUse.getSession() == null) || JadeStringUtil.isEmpty(date)) {
            return null;
        }
        String codeCanton = "";
        // recherche d'une cotisation AF
        AFCotisationManager cotisMgr = new AFCotisationManager();
        cotisMgr.setForDate(date);
        cotisMgr.setForAffiliationId(affToUse.getAffiliationId());
        cotisMgr.setSession(affToUse.getSession());
        cotisMgr.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AF);
        cotisMgr.find();
        if (cotisMgr.size() > 0) {
            // une cotisation AF existe
            codeCanton = ((AFCotisation) cotisMgr.getFirstEntity()).getAssurance().getAssuranceCanton();
        }
        return codeCanton;
    }

    public static String getCantonAFForDS(AFAffiliation affToUse, String date) throws Exception {
        String codeCanton = AFAffiliationUtil.getCantonAFCS(affToUse, date);
        if (JadeStringUtil.isEmpty(codeCanton)) {
            // aucune information trouv�e, rechercher le canton dans l'adresse
            // du tiers
            TIAdresseDataSource ds = new TIAdresseDataSource();
            ds.setSession(affToUse.getSession());
            ds.load(AFAffiliationUtil.getAdresseExploitation(affToUse));
            codeCanton = ds.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_CANTON_ID);
        }
        return codeCanton;
    }

    /**
     * Recherche la date de d�but d'une assurance donn�e, pour une affiliation donn�e
     * 
     * @param affToUse
     *            l'affiliation � tester
     * @param date
     *            la date � laquelle l'assurance recherch�e serait active ou null si date du jour. Note: pour
     *            affiliation radi�e, la date de radiation est utilis�e afin de retourner la derni�re assurance active
     * @param assuranceId
     *            l'ID de l'assurance � rechercher
     * @return la date de d�but de la cotisation sp�cifi�e si trouv�e au format jj.mm.aaaa, null sinon
     * @throws Exception
     *             si une erreur survient
     */
    public static String getDateDebutCotisations(AFAffiliation affToUse, String date, String assuranceId)
            throws Exception {
        // recherche des cotisations
        AFCotisationManager cotisMgr = new AFCotisationManager();
        cotisMgr.setSession(affToUse.getSession());
        if (JadeStringUtil.isEmpty(date)) {
            if (JadeStringUtil.isEmpty(affToUse.getDateFin())) {
                // affiliation ouverte, recherche sur la date du jour
                cotisMgr.setForDate(JACalendar.todayJJsMMsAAAA());
            } else {
                // affiliation radi�e, recherche sur la derni�re active (date de
                // radiation)
                cotisMgr.setForDate(affToUse.getDateFin());
            }

        } else {
            cotisMgr.setForDate(date);
        }
        cotisMgr.setForAffiliationId(affToUse.getAffiliationId());
        cotisMgr.setForAssuranceId(assuranceId);
        cotisMgr.find();
        return AFAffiliationUtil.searchDateBefore(cotisMgr);
    }

    /**
     * Recherche la date de d�but d'une assurance donn�e, pour une affiliation donn�e
     * 
     * @param affToUse
     *            l'affiliation � tester
     * @param date
     *            la date � laquelle l'assurance recherch�e serait active ou null si date du jour. Note: pour
     *            affiliation radi�e, la date de radiation est utilis�e afin de retourner la derni�re assurance active
     * @param typeAssurance
     *            type de l'assurance (CodeSystem.TYPE_ASS_COTISATION_AVS_AI, CodeSystem.TYPE_ASS_COTISATION_AF, ...)
     * @param genreAssurance
     *            le genre de l'assurance (CodeSystem.GENRE_ASS_PARITAIRE ou CodeSystem.GENRE_ASS_PERSONNEL)
     * @return la date de d�but de la cotisation sp�cifi�e si trouv�e au format jj.mm.aaaa, null sinon
     * @throws Exception
     *             si une erreur survient
     */
    public static String getDateDebutCotisations(AFAffiliation affToUse, String date, String typeAssurance,
            String genreAssurance) throws Exception {
        // recherche des cotisations
        AFCotisationManager cotisMgr = new AFCotisationManager();
        cotisMgr.setSession(affToUse.getSession());
        if (JadeStringUtil.isEmpty(date)) {
            if (JadeStringUtil.isEmpty(affToUse.getDateFin())) {
                // affiliation ouverte, recherche sur la date du jour
                cotisMgr.setForDate(JACalendar.todayJJsMMsAAAA());
            } else {
                // affiliation radi�e, recherche sur la derni�re active (date de
                // radiation)
                cotisMgr.setForDate(affToUse.getDateFin());
            }

        } else {
            cotisMgr.setForDate(date);
        }
        cotisMgr.setForAffiliationId(affToUse.getAffiliationId());
        cotisMgr.setForTypeAssurance(typeAssurance);
        cotisMgr.setForGenreAssurance(genreAssurance);
        cotisMgr.find();
        return AFAffiliationUtil.searchDateBefore(cotisMgr);

        /*
         * String dateResult = null; for(int i=0;i<cotisMgr.size();i++) { // si plusieurs cotisation existent (par/pers)
         * on recherche la date la plus ancienne AFCotisation cot = (AFCotisation)cotisMgr.getEntity(i);
         * if(dateResult==null || BSessionUtil.compareDateFirstLower(affToUse.getSession
         * (),cot.getDateDebut(),dateResult)) { // premi�re cotisation ou date plus ancienne dateResult =
         * cot.getDateDebut(); } } return dateResult;
         */
    }

    /**
     * Recherche la date de d�but de l'assurance AF, pour une affiliation donn�e
     * 
     * @param affToUse
     *            l'affiliation � tester
     * @param date
     *            la date � laquelle l'assurance recherch�e serait active ou null si date du jour. Note: pour
     *            affiliation radi�e, la date de radiation est utilis�e afin de retourner la derni�re assurance active
     * @param genreAssurance
     *            le genre de l'assurance (CodeSystem.GENRE_ASS_PARITAIRE ou CodeSystem.GENRE_ASS_PERSONNEL)
     * @return la date de d�but de la cotisation sp�cifi�e si trouv�e au format jj.mm.aaaa, null sinon
     * @throws Exception
     *             si une erreur survient
     */
    public static String getDateDebutCotisationsAF(AFAffiliation affToUse, String date, String genreAssurance)
            throws Exception {
        return AFAffiliationUtil.getDateDebutCotisations(affToUse, date, CodeSystem.TYPE_ASS_COTISATION_AF,
                genreAssurance);
    }

    /**
     * Recherche la date de d�but de l'assurance AVS, pour une affiliation donn�e
     * 
     * @param affToUse
     *            l'affiliation � tester
     * @param date
     *            la date � laquelle l'assurance recherch�e serait active ou null si date du jour. Note: pour
     *            affiliation radi�e, la date de radiation est utilis�e afin de retourner la derni�re assurance active
     * @param genreAssurance
     *            le genre de l'assurance (CodeSystem.GENRE_ASS_PARITAIRE ou CodeSystem.GENRE_ASS_PERSONNEL)
     * @return la date de d�but de la cotisation sp�cifi�e si trouv�e au format jj.mm.aaaa, null sinon
     * @throws Exception
     *             si une erreur survient
     */
    public static String getDateDebutCotisationsAVS(AFAffiliation affToUse, String date, String genreAssurance)
            throws Exception {
        return AFAffiliationUtil.getDateDebutCotisations(affToUse, date, CodeSystem.TYPE_ASS_COTISATION_AVS_AI,
                genreAssurance);
    }

    public static String getDateSituation(AFAffiliation affToUse) {
        String today = JACalendar.todayJJsMMsAAAA();
        // pr�-conditions
        if ((affToUse == null) || (affToUse.getSession() == null)) {
            return today;
        }
        try {
            if (BSessionUtil.compareDateFirstGreater(affToUse.getSession(), affToUse.getDateDebut(), today)) {
                return affToUse.getDateDebut();
            } else if (!JadeStringUtil.isEmpty(affToUse.getDateFin())
                    && BSessionUtil.compareDateFirstLower(affToUse.getSession(), affToUse.getDateFin(), today)) {
                return affToUse.getDateFin();
            } else {
                return today;
            }
        } catch (Exception ex) {
            return today;
        }
    }

    public static final AFAffiliation getEmployeur(AFAffiliation unEmploye, String uneDate) {
        AFLienAffiliationManager lienAffiliationManager = new AFLienAffiliationManager();
        lienAffiliationManager.setSession(unEmploye.getSession());
        lienAffiliationManager.setForAffiliationId(unEmploye.getAffiliationId());
        lienAffiliationManager.setWantLienInverse(false);
        if (JadeStringUtil.isEmpty(uneDate)) {
            lienAffiliationManager.setForDate(JACalendar.todayJJsMMsAAAA());
        } else {
            lienAffiliationManager.setForDate(uneDate);
        }
        try {
            lienAffiliationManager.find();
        } catch (Exception e) {
            // TODO Mieuuux j'ai dit!
            e.printStackTrace();
        }
        for (Iterator<AFLienAffiliation> iterator = lienAffiliationManager.iterator(); iterator.hasNext();) {
            AFLienAffiliation tmpLienAffiliation = iterator.next();
            AFAffiliation parent = new AFAffiliation();
            parent.setSession(unEmploye.getSession());
            parent.setAffiliationId(tmpLienAffiliation.getAff_AffiliationId());
            try {
                parent.retrieve();
            } catch (Exception e) {
                // TODO G�rer un peu mieux, �videmment
                e.printStackTrace();
            }
            if (CodeSystem.TYPE_AFFILI_EMPLOY.equals(parent.getTypeAffiliation())) {
                return parent;
            }
        }
        // TODO p�ter une exception
        return null;
    }

    /**
     * Retourne l'id de la caisse m�tier principale de l'affiliation
     * 
     * @param affToUse
     *            l'affiliation
     * @param isParitaire
     *            s'il s'agit d'un traitement paritaire
     * @param anneeCotisation
     *            l'ann�e de cotisation
     * @return l'id de la caisse m�tier ou vide is inexistante
     */
    public static String getIdCaissePrincipale(AFAffiliation affToUse, boolean isParitaire, String dateOuAnneeCotisation)
            throws Exception {
        // pr�-conditions
        if ((affToUse == null) || (affToUse.getSession() == null)
                || JadeStringUtil.isBlankOrZero(dateOuAnneeCotisation)) {
            return "";
        }
        String dateValeur = "";
        if (isParitaire) {
            // Construction d'une date au 1er. janvier de l'ann�e indiqu�e
            dateValeur = "01.01." + dateOuAnneeCotisation;
        } else {
            dateValeur = dateOuAnneeCotisation;
        }
        // Au plus t�t la date d'affiliation
        if (BSessionUtil.compareDateFirstLower(affToUse.getSession(), dateValeur, affToUse.getDateDebut())) {
            dateValeur = affToUse.getDateDebut();
        }
        // Chargement du manager
        AFAdhesionManager mgr = new AFAdhesionManager();
        mgr.setSession(affToUse.getSession());
        mgr.setForAffiliationId(affToUse.getAffiliationId());

        mgr.setOrder("MRDDEB DESC");

        if (isParitaire) {
            mgr.setForDateValeur(dateValeur);
            mgr.setForTypeAdhesion(IAFAdhesion.ADHESION_CAISSE);
        } else {
            mgr.setForDateValeur(dateValeur);
            mgr.setForTypeAdhesion(IAFAdhesion.ADHESION_CAISSE_PRINCIPALE);
        }

        mgr.find();
        // Deuxi�me tentative avec caisse principale
        if (mgr.isEmpty() && (isParitaire)) {
            mgr.setForDateValeur(dateValeur);
            mgr.setForTypeAdhesion(IAFAdhesion.ADHESION_CAISSE_PRINCIPALE);
            mgr.find();
        }
        // troisi�me tentative

        if (mgr.isEmpty()) {
            mgr.setForDateValeur("");
            if (isParitaire) {
                mgr.setForDateDebutLower("31.12." + dateOuAnneeCotisation);
            } else {
                mgr.setForDateDebutLower("31.12." + (JADate.getYear(dateOuAnneeCotisation)).toString());
            }
            mgr.find();
        }

        if (mgr.isEmpty()) {
            return "";
        } else {
            TIAdministrationViewBean caisse = ((AFAdhesion) mgr.getFirstEntity()).getAdministrationCaisse();
            if (caisse == null) {
                return "";
            } else {
                return caisse.getIdTiersAdministration();
            }
        }
    }

    /**
     * Renvoie le num�ro de la caisse AVS
     * 
     * @param affToUse
     *            l'affiliation � interroger
     * @param anneeDate
     *            l'ann�e concern�e
     * @return le num�ro de la caisse AVS (la caisse elle-m�me ou caisse externe)
     * @throws Exception
     *             si une exception survient
     */
    public static ITIAdministration getNoCaisseAVS(AFAffiliation affToUse, String anneeDate) throws Exception {
        AFSuiviCaisseAffiliation suivi = AFAffiliationUtil.getCaisseExterne(affToUse, anneeDate,
                CodeSystem.GENRE_CAISSE_AVS);
        if (suivi == null) {
            ITIAdministration admin = CaisseHelperFactory.getInstance().getAdministrationCaisse(affToUse.getSession());
            if (admin != null) {
                return admin;
            }
        } else {
            return new ITIAdministrationHelper(suivi.getAdministration().toValueObject());
        }
        return null;
    }

    public static String getRoleParInd(AFAffiliation affiliation) {
        String role = "";
        if (affiliation != null) {
            role = CodeSystem.ROLE_AFFILIE;
            String type = affiliation.getTypeAffiliation();
            if ((CodeSystem.TYPE_AFFILI_EMPLOY.equals(type)) || (CodeSystem.TYPE_AFFILI_LTN.equals(type))) {
                // Paritaire
                role = CodeSystem.ROLE_AFFILIE_PARITAIRE;
            } else if ((CodeSystem.TYPE_AFFILI_INDEP.equals(type)) || (CodeSystem.TYPE_AFFILI_TSE.equals(type))
                    || (CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE.equals(type))
                    || (CodeSystem.TYPE_AFFILI_NON_ACTIF.equals(type))
                    || (CodeSystem.TYPE_AFFILI_SELON_ART_1A.equals(type))) {
                // Indep / NA
                role = CodeSystem.ROLE_AFFILIE_PERSONNEL;
            }
        }
        return role;
    }

    /**
     * Recherche la taxation principale. Si un lien de type "Tax� sous" existe pour l'affili� donn�, l'affiliation
     * principale est retourn�e. Sinon, l'affiliation donn�e est retourn�e.
     * 
     * @param affToUse
     *            l'affiliation � tester
     * @param date
     *            date � laquelle le test doit �tre effectu� (les liaisons sont historis�es) ou null pour effectuer une
     *            recherche sur la date du jour
     * @return object du type AFAffiliation repr�sentant l'affiliation principal ou null si affToUse est null
     * @throws Exception
     *             si une erreur survient
     */
    public static AFAffiliation getTaxationPrincipale(AFAffiliation affToUse, String date) throws Exception {
        if (affToUse != null) {
            AFLienAffiliationManager lienMgr = new AFLienAffiliationManager();
            lienMgr.setSession(affToUse.getSession());
            lienMgr.setForAff_AffiliationId(affToUse.getAffiliationId());
            lienMgr.setForTypeLien(CodeSystem.TYPE_LIEN_TAXE_SOUS);
            if (JadeStringUtil.isEmpty(date)) {
                lienMgr.setForDate(JACalendar.todayJJsMMsAAAA());
            } else {
                lienMgr.setForDate(date);
            }
            lienMgr.find();
            if (lienMgr.size() == 0) {
                return affToUse;
            } else {
                return (AFAffiliation) lienMgr.getFirstEntity();
            }
        }
        return affToUse;
    }

    /**
     * Test si l'affiliation contient une information sur une caisse externe
     * 
     * @param affToUse
     *            l'affiliation � tester
     * @param anneeDate
     *            l'ann�e recherch�e au format aaaa ou une date pr�cise au foramt jj.mm.aaaa. Date du jour is null.
     * @param genreCaisse
     *            LAA, LPP, AVS ou AF
     * @return true si caisse trouv�e
     * @throws Exception
     *             si une erreur survient
     */
    public static boolean hasCaisseExterne(AFAffiliation affToUse, String anneeDate, String genreCaisse)
            throws Exception {
        AFSuiviCaisseAffiliationManager mgr = new AFSuiviCaisseAffiliationManager();
        mgr.setSession(affToUse.getSession());
        mgr.setForAffiliationId(affToUse.getAffiliationId());
        mgr.setForGenreCaisse(genreCaisse);
        if (anneeDate == null) {
            // si null date du jour
            mgr.setForDate(JACalendar.todayJJsMMsAAAA());
        } else if (anneeDate.trim().length() == 4) {
            // ann�e
            mgr.setForAnneeActive(anneeDate);
        } else {
            // jour pr�cis jj.mm.aaaa
            mgr.setForDate(anneeDate);
        }
        if (mgr.getCount() != 0) {
            return true;
        }
        return false;
    }

    /**
     * Test si l'affiliation contient une information sur la caisse LAA
     * 
     * @param affToUse
     *            l'affiliation � tester
     * @param anneeDate
     *            l'ann�e concern�e au format aaaa ou une date pr�cise au foramt jj.mm.aaaa. Date du jour is null.
     * @return true si caisse trouv�e
     * @throws Exception
     *             si une erreur survient
     */
    public static boolean hasCaissseLAA(AFAffiliation affToUse, String anneeDate) throws Exception {
        return AFAffiliationUtil.hasCaisseExterne(affToUse, anneeDate, CodeSystem.GENRE_CAISSE_LAA);
    }

    /**
     * Test si l'affiliation contient une information sur la caisse LPP
     * 
     * @param affToUse
     *            l'affiliation � tester
     * @param anneeDate
     *            l'ann�e concern�e au format aaaa ou une date pr�cise au foramt jj.mm.aaaa. Date du jour is null.
     * @return true si caisse trouv�e
     * @throws Exception
     *             si une erreur survient
     */
    public static boolean hasCaissseLPP(AFAffiliation affToUse, String anneeDate) throws Exception {
        return AFAffiliationUtil.hasCaisseExterne(affToUse, anneeDate, CodeSystem.GENRE_CAISSE_LPP);
    }

    public static boolean hasCotPersActif(AFAffiliation affToUse, String dateDebut, String dateFin) throws Exception {
        // pr�-conditions
        if ((affToUse == null) || (affToUse.getSession() == null) || JadeStringUtil.isEmpty(dateDebut)
                || JadeStringUtil.isEmpty(dateFin)) {
            return false;
        }
        AFCotisationManager cotisMgr = new AFCotisationManager();
        cotisMgr.setDateDebutLessEqual(dateFin); // Doit �tre plus petit ou �gal que la date de fin de d�cision
        cotisMgr.setDateFinGreaterEqual(dateDebut); // Doit �tre plus grand ou �gal que la date de d�but de d�cision
        cotisMgr.setForAffiliationId(affToUse.getAffiliationId());
        cotisMgr.setForDateDifferente(Boolean.TRUE);
        cotisMgr.setSession(affToUse.getSession());
        cotisMgr.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
        cotisMgr.find();
        if (cotisMgr.size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * Recherche si l'affili� emploie du personnel de maison (affiliation li�e � l'affiliation donn�e) La recherche
     * s'effectue sur les liaisons entre affiliaton de type "a du personnel de maison"
     * 
     * @param affToUse
     *            l'affiliation � tester
     * @return un object de type AFAffiliation utilis� pour le personnel de maison li�e � l'affiliation donn�e ou null
     *         si inexistant
     * @throws Exception
     *             si une erreur survient
     */
    public static AFAffiliation hasPersonnelMaison(AFAffiliation affToUse, String date) throws Exception {
        if (affToUse != null) {
            // recherche des liaisons
            AFLienAffiliationManager lienAffiliationManager = new AFLienAffiliationManager();
            lienAffiliationManager.setSession(affToUse.getSession());
            lienAffiliationManager.setForAff_AffiliationId(affToUse.getAffiliationId());
            lienAffiliationManager.setForTypeLien(CodeSystem.TYPE_LIEN_PERSONNEL_MAISON);
            if (JadeStringUtil.isEmpty(date)) {
                lienAffiliationManager.setForDate(JACalendar.todayJJsMMsAAAA());
            } else {
                lienAffiliationManager.setForDate(date);
            }
            lienAffiliationManager.find();
            if (lienAffiliationManager.size() > 0) {
                // lien trouv�, recherche de l'affiliation qui contient le
                // personnel de maison
                AFAffiliation pm = new AFAffiliation();
                pm.setSession(affToUse.getSession());
                pm.setAffiliationId(((AFLienAffiliation) lienAffiliationManager.getFirstEntity()).getAffiliationId());
                pm.retrieve();
                if (!pm.isNew()) {
                    return pm;
                }
            }
        }
        // sinon null
        return null;
    }

    /**
     * Recherche si l'affiliation donn�e � des succursales et si oui retoune l'affiliation de la maison m�re La
     * recherche s'effectue sur les liaisons entre affiliaton de type "est succursale de"
     * 
     * @param affToUse
     *            l'affiliation � tester
     * @param date
     *            date � laquelle le test doit �tre effectu� (les liaisons sont historis�es) ou null pour effectuer une
     *            recherche sur la date du jour
     * @return un object de type AFAffiliation si une maison m�re est d�finie, null sinon
     * @throws Exception
     *             si une erreur survient
     */
    public static boolean hasSuccursale(AFAffiliation affToUse, String date) throws Exception {
        if (affToUse != null) {
            // recherche des liaisons
            AFLienAffiliationManager lienAffiliationManager = new AFLienAffiliationManager();
            lienAffiliationManager.setSession(affToUse.getSession());
            lienAffiliationManager.setForAff_AffiliationId(affToUse.getAffiliationId());
            lienAffiliationManager.setForTypeLien(CodeSystem.TYPE_LIEN_SUCCURSALE);
            if (JadeStringUtil.isEmpty(date)) {
                lienAffiliationManager.setForDate(JACalendar.todayJJsMMsAAAA());
            } else {
                lienAffiliationManager.setForDate(date);
            }
            lienAffiliationManager.find();
            if (lienAffiliationManager.size() > 0) {
                return true;
            }
        }
        // sinon null
        return false;
    }

    /**
     * Permet d'initialiser un JadeThreadContext � partir d'une session (utilis� pour appeler un service utilisant la
     * nouvelle persistence par exemple)
     * 
     * @param session
     * @return
     * @throws Exception
     */
    public static JadeThreadContext initContext(BSession session) throws Exception {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);
        return context;
    }

    /**
     * Indique si l'affili� donn� est active pour les prestations AF
     * 
     * @param affToUse
     *            l'affiliation � tester. Revoie false si l'affiliation est null ou si aucune session n'est connect�e
     * @param date
     *            date � laquelle le test doit s'effectuer. Renvoie false si non rensign�e
     * @param typeAllocataire
     *            type d'allocataire (salari�,non actif,paysan,collaborateur agricole, travailleur agricole)
     * @return true si l'affili� donn�es est active, false sinon
     * @exception exception
     *                si un erreur survient lors de la recherche
     */
    public static boolean isActifAF(AFAffiliation affToUse, String date, String typeAllocataire) throws Exception {
        return AFAffiliationUtil.getAffiliationFacturationAF(affToUse, date, typeAllocataire) != null;
    }

    /**
     * Indique si l'affili� donn� est valide pour les prestations AF
     * 
     * @param affToUse
     *            l'affiliation � tester. Revoie false si l'affiliation est null ou si aucune session n'est connect�e
     * @param date
     *            date � laquelle le test doit s'effectuer. Renvoie false si non rensign�e
     * @param typeAllocataire
     *            type d'allocataire (salari�,non actif,paysan,collaborateur agricole, travailleur agricole)
     * @return true si l'affili� donn�es est valide, false sinon
     * @exception exception
     *                si un erreur survient lors de la recherche
     */
    public static boolean isActifAFOld(AFAffiliation affToUse, String date, String typeAllocataire) throws Exception {
        // pr�-conditions
        if ((affToUse == null) || (affToUse.getSession() == null) || JadeStringUtil.isEmpty(date)) {
            return false;
        }

        // 1) pour tous les type d'affiliation, tester si la date donn�e est
        // dans la p�riode d'affiliation
        if (JadeStringUtil.isEmpty(affToUse.getDateFin())) {
            // affiliation ouverte
            if (!BSessionUtil.compareDateFirstLowerOrEqual(affToUse.getSession(), affToUse.getDateDebut(), date)) {
                // la date donn�e est avant le d�but d'affiliation
                return false;
            }
        } else {
            // affiliation radi�e
            if (!BSessionUtil.compareDateBetweenOrEqual(affToUse.getSession(), affToUse.getDateDebut(),
                    affToUse.getDateFin(), date)) {
                // la date donn�e ne se trouve pas dans la p�riode d'affiliation
                return false;
            }
        }

        // 2) pour les employeurs, recherche les cotisation AF et tester si la
        // date donn�e est bien dans
        // la p�riode de l'assurance et qu'une masse salariale est saisie
        if (AFAffiliationUtil.TYPE_ALLOC_AF_SALARIE.equals(typeAllocataire)) {
            // recherche des coti AF
            AFCotisationManager cotisMgr = new AFCotisationManager();
            cotisMgr.setForDate(date);
            cotisMgr.setForAffiliationId(affToUse.getAffiliationId());
            cotisMgr.setSession(affToUse.getSession());
            cotisMgr.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AF);
            cotisMgr.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
            cotisMgr.find();
            if (cotisMgr.size() == 0) {
                // aucune coti AF
                return false;
            } else {
                // coti AF trouv�es
                for (int i = 0; i < cotisMgr.size(); i++) {
                    AFCotisation coti = (AFCotisation) cotisMgr.getEntity(i);
                    if (coti.getMaisonMere().booleanValue()) {
                        // factur�e � la maison m�re
                        // recherche du lien succursale de et de la maison m�re
                        // TODO effectuer la recherche sur le type de lien
                        // "D�compte AF sous" si la maison m�re n'est pas
                        // d�finie
                        AFAffiliation mMere;
                        if ((mMere = AFAffiliationUtil.isSuccursale(affToUse, date)) != null) {
                            // test sur la maison m�re
                            if (!AFAffiliationUtil.isActifAF(mMere, date, typeAllocataire)) {
                                return false;
                            }
                        } else {
                            // indiqu� comme factur� � la maison m�re mais
                            // celle-ci n'est pas d�finie -> erreur
                            return false;
                        }
                    } else {
                        // pas une succursale
                        // tester la masse salariale si factur� par accompte,
                        // sinon ok
                        if (!affToUse.isReleveParitaire().booleanValue()) {
                            if (JadeStringUtil.isIntegerEmpty(coti.getMasseAnnuelle())) {
                                // aucune masse saisie
                                return false;
                            }
                        }
                    }
                } // pour toutes les cotisations
                  // si aucun probl�mes rencontr�s, ok
                return true;
            }
        }

        // 3) pour les petits paysans, leurs collaborateurs et/ou leurs
        // travaileurs, tester la branche �conomique � 01 (paysan)
        if (AFAffiliationUtil.TYPE_ALLOC_AF_PAYSAN.equals(typeAllocataire)
                && AFAffiliationUtil.TYPE_ALLOC_AF_COL_AGRI.equals(typeAllocataire)
                && AFAffiliationUtil.TYPE_ALLOC_AF_TRA_AGRI.equals(typeAllocataire)) {
            if (!CodeSystem.BRANCHE_ECO_AGRICULTURE.equals(affToUse.getBrancheEconomique())) {
                // br eco diff�rente d'agriculture -> erreur
                return false;
            }
        }

        // 4) pour les petits paysan, tester si des cot. pers AVS sont bien
        // pr�sentent
        if (AFAffiliationUtil.TYPE_ALLOC_AF_PAYSAN.equals(typeAllocataire)) {
            if (!CodeSystem.TYPE_AFFILI_BENEF_AF.equals(affToUse.getTypeAffiliation())) {
                // recherche des coti AF
                AFCotisationManager cotisMgr = new AFCotisationManager();
                cotisMgr.setForDate(date);
                cotisMgr.setForAffiliationId(affToUse.getAffiliationId());
                cotisMgr.setSession(affToUse.getSession());
                cotisMgr.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
                cotisMgr.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
                cotisMgr.find();
                if (cotisMgr.size() == 0) {
                    // aucune coti AVS
                    return false;
                }
            }
        }

        // 5) pour les travailleurs agricoles, tester si coti LFA/LJA pr�sentes
        if (AFAffiliationUtil.TYPE_ALLOC_AF_TRA_AGRI.equals(typeAllocataire)) {
            // recherche des coti LFA/LJA
            // TODO ajouter un nouveau cs pour le type d'assurance LFA/LFA afin
            // de pouvoir les retrouver
            /*
             * AFCotisationJAssuranceManager cotisMgr = new AFCotisationJAssuranceManager(); cotisMgr.setForDate(date);
             * cotisMgr.setForAffiliationId(affToUse.getAffiliationId()); cotisMgr.setSession(affToUse.getSession());
             * cotisMgr.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_);
             * cotisMgr.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL); cotisMgr.find(); if(cotisMgr.size()==0){
             * // aucune coti LFA/LJA return false; }
             */
        }

        return true;
    }

    /**
     * Recherche si l'affiliation donn�e est un associ� et si oui retoune l'affiliation SNC La recherche s'effectue sur
     * les liaisons entre affiliaton de type "est associ� de"
     * 
     * @param affToUse
     *            l'affiliation � tester
     * @param date
     *            date � laquelle le test doit �tre effectu� (les liaisons sont historis�es) ou null pour effectuer une
     *            recherche sur la date du jour
     * @return un object de type AFAffiliation si la SNC est d�finie, null sinon
     * @throws Exception
     *             si une erreur survient
     */
    public static AFAffiliation isAssocie(AFAffiliation affToUse, String date) throws Exception {
        return AFAffiliationUtil.isEnfantsLien(affToUse, date, CodeSystem.TYPE_LIEN_ASSOCIE);
    }

    /**
     * Indique si l'affili� est de type associ� ou commanditaire
     * 
     * @param affToUse
     *            l'affiliation concern�e
     * @return true si l'affili� est de type associ� ou commanditaire
     */
    public static boolean isAssocieCommanditaire(AFAffiliation affToUse) {
        if (affToUse != null) {
            if (!JadeStringUtil.isIntegerEmpty(affToUse.getTypeAssocie())) {
                // le champ associ�/commanditaire est rensign�
                return true;
            }
        }
        // sinon false
        return false;
    }

    /**
     * Recherche si l'affiliation donn�e est l'enfant d'une affiliation parente La recherche s'effectue sur les liaisons
     * entre affiliaton de type "est succursale de"
     * 
     * @param affToUse
     *            l'affiliation � tester
     * @param date
     *            date � laquelle le test doit �tre effectu� (les liaisons sont historis�es) ou null pour effectuer une
     *            recherche sur la date du jour
     * @param typeLien
     *            le type de liaison � tester
     * @return un object de type AFAffiliation si l'affiliation parente est d�finie, null sinon
     * @throws Exception
     *             si une erreur survient
     */
    private static AFAffiliation isEnfantsLien(AFAffiliation affToUse, String date, String typeLien) throws Exception {
        if (affToUse != null) {
            // recherche des liaisons
            AFLienAffiliationManager lienAffiliationManager = new AFLienAffiliationManager();
            lienAffiliationManager.setSession(affToUse.getSession());
            lienAffiliationManager.setForAffiliationId(affToUse.getAffiliationId());
            lienAffiliationManager.setForTypeLien(typeLien);
            if (JadeStringUtil.isEmpty(date)) {
                lienAffiliationManager.setForDate(JACalendar.todayJJsMMsAAAA());
            } else {
                lienAffiliationManager.setForDate(date);
            }
            lienAffiliationManager.find();
            if (lienAffiliationManager.size() > 0) {
                // lien trouv�, recherche de l'affiliation "maison m�re"
                AFAffiliation parent = new AFAffiliation();
                parent.setSession(affToUse.getSession());
                parent.setAffiliationId(((AFLienAffiliation) lienAffiliationManager.getFirstEntity())
                        .getAff_AffiliationId());
                parent.retrieve();
                if (!parent.isNew()) {
                    return parent;
                }
            }
        }
        // sinon null
        return null;
    }

    public static AFAffiliation isMaisonMere(AFAffiliation affToUse, String date) throws Exception {
        return AFAffiliationUtil.isRechercheMaisonMere(affToUse, date, CodeSystem.TYPE_LIEN_SUCCURSALE);
    }

    private static AFAffiliation isRechercheMaisonMere(AFAffiliation affToUse, String date, String typeLien)
            throws Exception {
        if (affToUse != null) {
            // recherche des liaisons
            AFLienAffiliationManager lienAffiliationManager = new AFLienAffiliationManager();
            lienAffiliationManager.setSession(affToUse.getSession());
            lienAffiliationManager.setForAffiliationId(affToUse.getAffiliationId());
            lienAffiliationManager.setForTypeLien(typeLien);
            lienAffiliationManager.setWantLienInverse(true);
            if (JadeStringUtil.isEmpty(date)) {
                lienAffiliationManager.setForDate(JACalendar.todayJJsMMsAAAA());
            } else {
                lienAffiliationManager.setForDate(date);
            }
            lienAffiliationManager.find();
            if (lienAffiliationManager.size() > 0) {
                AFAffiliation maisonMere = new AFAffiliation();
                maisonMere.setSession(affToUse.getSession());
                maisonMere.setAffiliationId(((AFLienAffiliation) lienAffiliationManager.getFirstEntity())
                        .getAff_AffiliationId());
                maisonMere.retrieve();
                if (!maisonMere.isNew()) {
                    return maisonMere;
                }
            } else {
                return null;
            }

        }
        // sinon null
        return null;
    }

    /**
     * Recherche si l'affiliation donn�e est une succursale et si oui retoune l'affiliation de la maison m�re La
     * recherche s'effectue sur les liaisons entre affiliaton de type "est succursale de"
     * 
     * @param affToUse
     *            l'affiliation � tester
     * @param date
     *            date � laquelle le test doit �tre effectu� (les liaisons sont historis�es) ou null pour effectuer une
     *            recherche sur la date du jour
     * @return un object de type AFAffiliation si une maison m�re est d�finie, null sinon
     * @throws Exception
     *             si une erreur survient
     */
    public static AFAffiliation isSuccursale(AFAffiliation affToUse, String date) throws Exception {
        return AFAffiliationUtil.isEnfantsLien(affToUse, date, CodeSystem.TYPE_LIEN_SUCCURSALE);
    }

    /**
     * Indique si l'affili� donn� repr�sente la taxation principale
     * 
     * @param affToUse
     *            l'affiliation � tester
     * @param date
     *            date � laquelle le test doit �tre effectu� (les liaisons sont historis�es) ou null pour effectuer une
     *            recherche sur la date du jour
     * @return true si l'affili� donn� repr�sente la taxation principale
     * @throws Exception
     *             si une erreur survient
     */
    public static boolean isTaxationPrincipale(AFAffiliation affToUse, String date) throws Exception {
        AFLienAffiliationManager lienMgr = new AFLienAffiliationManager();
        lienMgr.setSession(affToUse.getSession());
        lienMgr.setForAff_AffiliationId(affToUse.getAffiliationId());
        lienMgr.setForTypeLien(CodeSystem.TYPE_LIEN_TAXE_SOUS);
        if (JadeStringUtil.isEmpty(date)) {
            lienMgr.setForDate(JACalendar.todayJJsMMsAAAA());
        } else {
            lienMgr.setForDate(date);
        }
        if (lienMgr.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Indique si l'affili� donn� est tax� sous une autre affiliation
     * 
     * @param affToUse
     *            l'affiliation � tester
     * @param date
     *            date � laquelle le test doit �tre effectu� (les liaisons sont historis�es) ou null pour effectuer une
     *            recherche sur la date du jour
     * @return true si l'affili� donn� est tax� sous une autre affiliation
     * @throws Exception
     *             si une erreur survient
     */
    public static boolean isTaxeSous(AFAffiliation affToUse, String date) throws Exception {
        AFLienAffiliationManager lienMgr = new AFLienAffiliationManager();
        lienMgr.setSession(affToUse.getSession());
        lienMgr.setForAffiliationId(affToUse.getAffiliationId());
        lienMgr.setForTypeLien(CodeSystem.TYPE_LIEN_TAXE_SOUS);
        if (JadeStringUtil.isEmpty(date)) {
            lienMgr.setForDate(JACalendar.todayJJsMMsAAAA());
        } else {
            lienMgr.setForDate(date);
        }
        if (lienMgr.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static AFAffiliation loadAffiliation(BSession session, String idTiers, String numAff, String noDecompte,
            String role) throws Exception {
        if ((session == null) || JadeStringUtil.isEmpty(idTiers) || JadeStringUtil.isEmpty(numAff)
                || JadeStringUtil.isEmpty(noDecompte)) {
            return null;
        }
        AFAffiliationManager affMgr = new AFAffiliationManager();
        affMgr.setSession(session);
        affMgr.setForAffilieNumero(numAff);
        affMgr.setForIdTiers(idTiers);
        if (!JadeStringUtil.isBlankOrZero(role)) {
            if (role.equals(CodeSystem.ROLE_AFFILIE_PARITAIRE)) {
                affMgr.setForTypesAffParitaires();
            } else if (role.equals(CodeSystem.ROLE_AFFILIE_PERSONNEL)) {
                affMgr.setForTypesAffPersonelles();
            }
        }

        if (noDecompte.length() > 4) {
            affMgr.setForDateDebutAffLowerOrEqualTo("31.12." + noDecompte.substring(0, 4));
            affMgr.setFromDateFin("01.01." + noDecompte.substring(0, 4));
        }
        affMgr.find();
        if (affMgr.size() > 0) {
            return (AFAffiliation) affMgr.getEntity(affMgr.size() - 1);
        }
        if (affMgr.size() == 0) {
            // affMgr.setForDateDebutAffLowerOrEqualTo("");
            affMgr.setFromDateFin("");
            affMgr.setOrder("MADDEB DESC");
            affMgr.find();
            if (affMgr.size() > 0) {
                return (AFAffiliation) affMgr.getFirstEntity();
            } else {
                new Exception("L'affiliation n'a pas pu �tre charg�e ! " + numAff);
            }
        }
        return null;
    }

    /**
     * Rechercher les diff�rentes caisses externes li�es � l'affiliation donn�e
     * 
     * @throws Exception
     */
    public static List<AFSuiviCaisseAffiliation> retrieveSuiviCaisse(AFAffiliation affToUse) throws Exception {
        List<AFSuiviCaisseAffiliation> suiviCaisseList = new ArrayList<AFSuiviCaisseAffiliation>();
        if (!JadeStringUtil.isIntegerEmpty(affToUse.getAffiliationId())) {
            AFSuiviCaisseAffiliationManager manager = new AFSuiviCaisseAffiliationManager();
            manager.setSession(affToUse.getSession());
            manager.setForAffiliationId(affToUse.getAffiliationId());
            manager.find();
            String lastGenre = null;

            for (int i = 0; i < manager.size(); i++) {
                AFSuiviCaisseAffiliation suiviCaisse = (AFSuiviCaisseAffiliation) manager.get(i);

                if ((lastGenre == null) || !lastGenre.equals(suiviCaisse.getGenreCaisse())
                        || JadeStringUtil.isIntegerEmpty(suiviCaisse.getDateFin())) {
                    suiviCaisseList.add(suiviCaisse);
                    lastGenre = suiviCaisse.getGenreCaisse();
                }
            }
        }
        return suiviCaisseList;
    }

    private static String searchDateBefore(AFCotisationManager cotisMgr) throws Exception {
        String dateResult = null;
        if (cotisMgr != null) {
            for (int i = 0; i < cotisMgr.size(); i++) {
                // si plusieurs cotisation existent (par/pers) on recherche la
                // date la plus ancienne
                AFCotisation cot = (AFCotisation) cotisMgr.getEntity(i);
                if ((dateResult == null)
                        || BSessionUtil.compareDateFirstLower(cotisMgr.getSession(), cot.getDateDebut(), dateResult)) {
                    // premi�re cotisation ou date plus ancienne
                    dateResult = cot.getDateDebut();
                }
            }
            if (dateResult != null) {
                // appel r�cursif
                JACalendar cal = new JACalendarGregorian();
                cotisMgr.setForDate(cal.addDays(new JADate(dateResult), -1).toStr("."));
                cotisMgr.find();
                String date = AFAffiliationUtil.searchDateBefore(cotisMgr);
                if (date != null) {
                    dateResult = date;
                }
            }
        }
        return dateResult;
    }

    // variable
    private AFAffiliation affiliation = null;

    /**
     * Constructeur
     * 
     * @param affToUse
     *            l'affiliation � utiliser
     */
    public AFAffiliationUtil(AFAffiliation affToUse) {
        affiliation = affToUse;
    }

    /**
     * Renvoie le domaine � utiliser pour l'adresse d'expedition de la facture
     * 
     * @return le domaine � utiliser pour l'adresse d'expedition de la facture
     * @throws Exception
     *             si une exception survient dans l'interrogation
     */
    public String getAdresseDomaineCourrier(String genreAffiliation) throws Exception {
        return AFAffiliationUtil.getAdresseDomainePlan(affiliation, ICommonConstantes.CS_APPLICATION_COTISATION,
                genreAffiliation);
    }

    /**
     * Renvoie le domaine � utiliser pour l'adresse de recouvrement de la facture
     * 
     * @return le domaine � utiliser pour l'adresse de recouvrement de la facture
     * @throws Exception
     *             si une exception survient dans l'interrogation
     */
    public String getAdresseDomaineRecouvrement(String genreAffiliation) throws Exception {
        return AFAffiliationUtil.getAdresseDomainePlan(affiliation, AFAffiliationUtil.DOMAINE_RECOUVREMENT,
                genreAffiliation);
    }

    /**
     * Renvoie le domaine � utiliser pour l'adresse de remboursement de la facture
     * 
     * @return le domaine � utiliser pour l'adresse de remboursement de la facture
     * @throws Exception
     *             si une exception survient dans l'interrogation
     */
    public String getAdresseDomaineRemboursement(String genreAffiliation) throws Exception {
        return AFAffiliationUtil.getAdresseDomainePlan(affiliation, AFAffiliationUtil.DOMAINE_REMBOURSEMENT,
                genreAffiliation);
    }

    public String getNumCaisseAdhesionFromGenre(AFAffiliation affiliation, BSession session, String genreCaisse,
            String dateValeur) {
        try {
            AFAdhesionManager mgrAdhesion = new AFAdhesionManager();
            mgrAdhesion.setSession(session);
            mgrAdhesion.setForAffiliationId(affiliation.getAffiliationId());
            mgrAdhesion.setForDateValeur(dateValeur);
            mgrAdhesion.find(BManager.SIZE_NOLIMIT);
            AFAdhesion adhesion = null;
            TIAdministrationViewBean vbAdministrationTier = null;
            String numCaisse = null;
            for (int i = 1; i <= mgrAdhesion.size(); i++) {
                adhesion = (AFAdhesion) mgrAdhesion.getEntity(i - 1);
                if (adhesion != null) {
                    vbAdministrationTier = adhesion.getAdministrationCaisse();
                    if ((vbAdministrationTier != null)
                            && genreCaisse.equalsIgnoreCase(vbAdministrationTier.getGenreAdministration())) {
                        numCaisse = vbAdministrationTier.getCodeAdministration();
                    }
                }
            }
            return numCaisse;
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return null;
        }
    }

    /**
     * Renvoie le num�ro de la caisse AVS
     * 
     * @param affToUse
     *            l'affiliation � interroger
     * @param anneeDate
     *            l'ann�e concern�e
     * @return le num�ro de la caisse AVS (la caisse elle-m�me ou caisse externe)
     * @throws Exception
     *             si une exception survient
     */
    public String getNumCaisseFromGenre(AFAffiliation affiliation, String anneeDate, String genre) throws Exception {
        AFSuiviCaisseAffiliation suivi = AFAffiliationUtil.getCaisseExterne(affiliation, anneeDate, genre);
        if (suivi == null) {
            ITIAdministration admin = CaisseHelperFactory.getInstance().getAdministrationCaisse(
                    affiliation.getSession());
            if (admin != null) {
                return admin.getCodeAdministration();
            }
        } else {
            return suivi.getAdministration().getCodeAdministration();
        }
        return null;
    }

    /**
     * Indique si l'affili� est de type associ� ou commanditaire
     * 
     * @return true si l'affili� est de type associ� ou commanditaire
     */
    public boolean isAssocieCommanditaire() {
        return AFAffiliationUtil.isAssocieCommanditaire(affiliation);
    }

    /**
     * Retourne si le plan est bloqu� et donc � imprimer s�paremment.<br>
     * Si l'affili� contient plusieurs plans, cet affili� est bloqu� si au moins un plan est bloqu�.
     * 
     * @return si l'affili� est bloqu� ou non
     * @throws Exception
     *             si une exception survient dans l'interrogation
     */
    public Boolean isPlanBloque() throws Exception {
        if (affiliation == null) {
            return new Boolean(false);
        }
        // recherche des plans
        // si plusieurs plan, retourner blocage si au moins un bloqu�
        AFPlanAffiliationManager planAffManager = new AFPlanAffiliationManager();
        planAffManager.setSession(affiliation.getSession());
        planAffManager.setForAffiliationId(affiliation.getAffiliationId());
        planAffManager.find();
        boolean result = false;
        for (int i = 0; i < planAffManager.size(); i++) {
            AFPlanAffiliation planAffiliation = (AFPlanAffiliation) planAffManager.getEntity(i);
            result |= planAffiliation.isBlocageEnvoi().booleanValue();
        }
        return new Boolean(result);
    }
}