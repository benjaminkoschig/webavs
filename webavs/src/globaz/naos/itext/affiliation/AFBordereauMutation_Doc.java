/*
 * Créé le 11 mai 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.itext.affiliation;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.adhesion.AFAdhesion;
import globaz.naos.db.adhesion.AFAdhesionManager;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.lienAffiliation.AFLienAffiliation;
import globaz.naos.db.lienAffiliation.AFLienAffiliationJoin;
import globaz.naos.db.lienAffiliation.AFLienAffiliationJoinManager;
import globaz.naos.db.lienAffiliation.AFLienAffiliationManager;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliationManager;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import globaz.pyxis.adresse.formater.TIAdresseFormater;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TIAvoirContact;
import globaz.pyxis.db.tiers.TICompositionTiers;
import globaz.pyxis.db.tiers.TICompositionTiersManager;
import globaz.pyxis.db.tiers.TIMoyenCommunication;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.ICommonConstantes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Classe permettant d'imprimer le bordereau de mutation
 * 
 * @author sda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFBordereauMutation_Doc extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int CASE_ADMINISTRATEUR = 26;
    public static final int CASE_ADRESSE_COURRIER = 18;
    public static final int CASE_ADRESSE_DOMICILE = 19;
    public static final int CASE_ADRESSE_PROFESSIONNELLE = 20;
    public static final int CASE_ASSOCIATIONS = 17;
    public static final int CASE_AVEC_PERSONNEL = 24;
    public static final int CASE_CAISSE_PROF = 5;
    public static final int CASE_CODE_INDEPENDANT = 4;
    public static final int CASE_COTISATIONS = 33;
    public static final int CASE_DATE_NAISSANCE = 11;
    public static final int CASE_DECLARE_PERSONNEL = 21;
    public static final int CASE_ETAT_CIVIL = 9;
    public static final int CASE_NATIONALITE = 12;
    public static final int CASE_NOM = 14;
    public static final int CASE_NUM_AFFILIE = 6;
    public static final int CASE_NUM_AVS = 16;
    public static final int CASE_NUM_FAX = 31;
    public static final int CASE_NUM_NATEL = 32;
    public static final int CASE_NUM_TELEPHONE = 30;
    public static final int CASE_OBSERVATIONS = 34;
    public static final int CASE_PERS_JURIDIQUE = 13;
    public static final int CASE_PERSONNEL_DECLARE_PAR = 22;
    public static final int CASE_PP_PARITAIRES = 28;
    public static final int CASE_PP_PERSONNELLE = 29;
    public static final int CASE_PRENOM = 15;
    // Constantes pour le switch
    public static final int CASE_RAISON_SOCIALE = 3;
    public static final int CASE_SANS_ACTIVITE_LUCRATIVE = 27;
    public static final int CASE_SANS_PERSONNEL = 25;
    public static final int CASE_SEXE = 10;
    public static final int CASE_SOCIETE_IMMOBILIERE = 23;
    public static final int CASE_TAXE = 7;
    // Constantes pour une date de fin
    public static final String DATE_FIN = "01.01.2999";
    // Constantes pour le type de lien désiré pour le déclare personnel
    public static final String DECLARE_PERSONNEL_DE = "1";
    // Numéro du document
    private static final String DOC_NO = "0112CAF";
    // Nom du champ gauche du tableau
    public static final String F_LEFT = "F_LEFT";
    // Non du champ droit du tableau
    public static final String F_RIGHT = "F_RIGHT";
    // Nom du document
    private static final String FILE_TITLE = "Bordereau de mutation";
    // Constante permettant d'afficher une ligne vide
    public static final String LIGNEVIDE = "<style forecolor=\"#FFFFFF\">.</style>";
    // Constante pour le niveau des en-tête du document
    public static final int NIVEAU_ENTETE = 1;
    // Constante pour le niveau qui contiens les libellés du tableau
    public static final int NIVEAU_LIBELLE = 2;
    // Nom template
    public static final String NOM_TEMPLATE = "NAOS_BORDEREAU_MUTATION";
    // Nom du champ contenant la date du document
    public static final String P_DATE = "P_DATE";
    // Non du champ contenant le nom du fichier
    public static final String P_FICHIER = "P_FICHIER";
    // Nom du champ contenant le numéro du document
    public static final String P_NUMERO = "P_NUMERO";
    // Nom du champ contenant le nom de la personne de référence;
    public static final String P_PERSONNE_REFERENCE = "P_PERSONNE_REFERENCE";
    // Nom du champ contenant le titre du document
    public static final String P_TITLE = "P_TITLE";
    public static final String PERSONNEL_DECLARE_PAR = "2";
    // Constante pour la position à afficher pour le fichier
    public static final int POSITION_FICHIER = 1;
    // Constante pour la position du texte à afficher pour la personne de
    // reference
    public static final int POSITION_PERSONNE_REFERENCE = 3;

    // Constante pour la position du texte à afficher si c'est taxé par
    public static final int POSITION_TAXEPAR = 8;
    // Constante pour la position du texte à afficher si c'est taxé sous
    public static final int POSITION_TAXESOUS = 7;
    // Constante pour la position à afficher pour le titre
    public static final int POSITION_TITRE = 2;
    // Constante du type de lien associations
    public static final String TYPE_LIEN_ASSOCIATION = "19120068";

    private static StringBuffer _buildBuffer(String sep, String[] values) {
        StringBuffer buffer = new StringBuffer();
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                if (!JadeStringUtil.isEmpty(values[i])) {
                    if (buffer.length() > 0) {
                        buffer.append(sep);
                    }
                    buffer.append(values[i]);
                }
            }
        }
        return buffer;
    }

    private AFAffiliation affiliation = null;
    private String affiliationId = "";
    private String dateSituation = "";
    private List<String> displayFieds = new ArrayList<String>();
    private ICTDocument[] document = null;
    private boolean isFirst = true;
    private String observations = "";

    private String responsable = "";

    public AFBordereauMutation_Doc() {
        super();
    }

    /**
     * @param parent
     * @param rootApplication
     * @param fileName
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public AFBordereauMutation_Doc(BProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    /**
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public AFBordereauMutation_Doc(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeBuildReport ()
     */
    @Override
    public void beforeBuildReport() throws FWIException {
        try {
            // On sette le nom du fichier
            super.setParametres(AFBordereauMutation_Doc.P_FICHIER,
                    getTexte(AFBordereauMutation_Doc.NIVEAU_ENTETE, AFBordereauMutation_Doc.POSITION_FICHIER));
            // On sette le titre du fichier
            super.setParametres(AFBordereauMutation_Doc.P_TITLE,
                    getTexte(AFBordereauMutation_Doc.NIVEAU_ENTETE, AFBordereauMutation_Doc.POSITION_TITRE));
            super.setParametres(
                    AFBordereauMutation_Doc.P_PERSONNE_REFERENCE,
                    getTexte(AFBordereauMutation_Doc.NIVEAU_ENTETE, AFBordereauMutation_Doc.POSITION_PERSONNE_REFERENCE)
                            + getResponsable());
            // super.setParametres(P_PERSONNE_REFERENCE, "Hello");
            // On sette la date du fichier
            super.setParametres(AFBordereauMutation_Doc.P_DATE, getDateSituation());
        } catch (Exception e) {
            getMemoryLog().logMessage("", FWMessage.ERREUR, e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeExecuteReport ()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        // Récupération du catalogue à utiliser
        document = getICTDocument();
        affiliation = getAffiliation(getAffiliationId());
        super.setDocumentTitle(getSession().getLabel("TITRE_DOC_BORDEREAU") + " " + affiliation.getAffilieNumero());
        // date de situation si pas saisie
        if (JadeStringUtil.isEmpty(getDateSituation())) {
            setDateSituation(AFAffiliationUtil.getDateSituation(affiliation));
        }
        // Sette du nom du template
        setTemplateFile(AFBordereauMutation_Doc.NOM_TEMPLATE);
        setFileTitle(AFBordereauMutation_Doc.FILE_TITLE);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        setControleTransaction(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource ()
     */
    @Override
    public void createDataSource() throws Exception {
        fillDocInfo();
        LinkedList<HashMap<String, String>> dataSource = new LinkedList<HashMap<String, String>>();
        HashMap<String, String> champs = null;
        // On parcours toutes les positions du catalogue pour insérer une ligne
        // à chaque position du niveau 2
        for (int i = 1; i <= document[0].getTextes(AFBordereauMutation_Doc.NIVEAU_LIBELLE).size(); i++) {
            champs = new HashMap<String, String>();
            // Les deux premières positions correspondent au en-tête de colonne.
            // Elles doivent donc êtres affichées
            // les deux sur la première ligne
            if (i < 3) {
                champs.put(AFBordereauMutation_Doc.F_LEFT, getTexte(AFBordereauMutation_Doc.NIVEAU_LIBELLE, i));
                i++;
                champs.put(AFBordereauMutation_Doc.F_RIGHT, getTexte(AFBordereauMutation_Doc.NIVEAU_LIBELLE, i));
            } else if (i == AFBordereauMutation_Doc.CASE_TAXE) {
                // Dans le cas de la ligne du taxé on va regarder si le type de
                // lien ("taxe sous" ou "taxe par").
                AFLienAffiliation lienAffiliation = getLienTaxeSous(affiliationId);
                // Si le manager n'est pas vide, c'est qu'on a un lien de type
                // taxe sous
                if (lienAffiliation != null) {
                    champs.put(AFBordereauMutation_Doc.F_LEFT, getTaxeSousOrTaxeParLEFT(true));
                } else {
                    // Sinon c'est un lien de type taxe par
                    champs.put(AFBordereauMutation_Doc.F_LEFT, getTaxeSousOrTaxeParLEFT(false));
                }
                champs.put(AFBordereauMutation_Doc.F_RIGHT, getTaxeSousOrTaxeParRIGHT(lienAffiliation));
                // On incrémente l'indice pour sauter la ligne du taxe par
                // (étant donné qu'on a deux positions pour ce cas)
                i++;
            } else {
                // Pour les autres positions on fait une ligne par position
                champs.put(AFBordereauMutation_Doc.F_LEFT, getTexte(AFBordereauMutation_Doc.NIVEAU_LIBELLE, i));
                // Pour la valeur de droite on affiche une ligne vide dans le
                // cas où le setValeur retourne une valeur vide ou nulle
                if (JadeStringUtil.isBlank(setValeur(i))) {
                    champs.put(AFBordereauMutation_Doc.F_RIGHT, AFBordereauMutation_Doc.LIGNEVIDE);
                } else {
                    champs.put(AFBordereauMutation_Doc.F_RIGHT, setValeur(i));
                }
            }
            dataSource.add(champs);
        }
        this.setDataSource(dataSource);
    } /*
       * (non-Javadoc)
       * 
       * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
       */

    private boolean doIneedToPrintThis(String codeSystem) {
        if (displayFieds.isEmpty() || displayFieds.contains(CodeSystem.CHAMPS_MOD_CREATION_AFFILIE)) {
            return true;
        }
        if (displayFieds.contains(codeSystem)) {
            return true;
        }
        if (CodeSystem.CHAMPS_MOD_CREATION_COTI.equals(codeSystem)) {
            return displayFieds.contains(CodeSystem.CHAMPS_MOD_DATE_DEBUT_COTI)
                    || displayFieds.contains(CodeSystem.CHAMPS_MOD_DATE_FIN_COTI);
        }
        return false;
    }

    // Execute le traitement en cas de traitement par la caisse
    private String executeTraitementTaxeParCaisse() throws Exception {
        AFApplication application = (AFApplication) globaz.globall.db.GlobazServer.getCurrentSystem().getApplication(
                AFApplication.DEFAULT_APPLICATION_NAOS);
        return application.getNumCaisse();
    }

    // Execute le traitement en cas de traitement par la caisse du suivi des
    // caisses
    private String executeTraitementTaxeParCaisseSuiviCaisse(AFSuiviCaisseAffiliation suiviCaisse) throws Exception {
        TIAdministrationViewBean administration = new TIAdministrationViewBean();
        administration.setSession(getSession());
        administration.setIdTiersAdministration(suiviCaisse.getIdTiersCaisse());
        administration.retrieve();
        if (administration.isNew()) {
            getMemoryLog().logMessage("erreur", FWMessage.ERREUR, "");
            return "";
        } else {
            return administration.getCodeAdministration();
        }
    }

    // Execute le traitement en cas de taxe sous
    private String executeTraitementTaxeSous(AFLienAffiliation lienAffiliation) throws Exception {
        return getAffilieNumero(lienAffiliation.getAff_AffiliationId());
    }

    /**
     * Après l'impression d'un document
     */
    private void fillDocInfo() {
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", affiliation.getAffilieNumero());
        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(affiliation.getAffilieNumero()));
            String role = AFAffiliationUtil.getRoleParInd(affiliation);
            TIDocumentInfoHelper.fill(getDocumentInfo(), affiliation.getIdTiers(), getSession(), role,
                    affiliation.getAffilieNumero(), affilieFormater.unformat(affiliation.getAffilieNumero()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", affiliation.getAffilieNumero());
        }
        getDocumentInfo().setDocumentProperty("annee", String.valueOf(JACalendar.today().getYear()));
        getDocumentInfo().setArchiveDocument(true);
        getDocumentInfo().setDocumentTypeNumber(AFBordereauMutation_Doc.DOC_NO);
    }

    // Permet de récupérer l'affiliation en lui passant l'id de l'affiliation
    private AFAffiliation getAffiliation(String idAffiliation) {
        try {
            AFAffiliation aff = new AFAffiliation();
            aff.setSession(getSession());
            aff.setAffiliationId(idAffiliation);
            aff.retrieve();
            if (aff.isNew()) {
                getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("2050"));
                return null;
            }
            return aff;
        } catch (Exception e) {
            getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("ERREUR_RECUPERATION_AFFILIATION"));
            return null;
        }
    }

    /**
     * @return
     */
    public String getAffiliationId() {
        return affiliationId;
    }

    // Retourne le numéro d'affilié en lui passant l'id affiliation en
    // paramètres
    private String getAffilieNumero(String idAffiliation) throws Exception {
        AFAffiliation aff = new AFAffiliation();
        aff.setSession(getSession());
        aff.setAffiliationId(idAffiliation);
        aff.retrieve();
        if (aff.isNew()) {
            getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("2050"));
            return null;
        } else {
            return aff.getAffilieNumero();
        }
    }

    public String getAssociations(TITiersViewBean tiers) {
        StringBuffer result = new StringBuffer();
        try {
            TICompositionTiersManager mgr = new TICompositionTiersManager();
            mgr.setSession(getSession());
            mgr.setForIdTiersEnfant(tiers.getIdTiers());
            mgr.setForTypeLien(AFBordereauMutation_Doc.TYPE_LIEN_ASSOCIATION);
            mgr.setForDateEntreDebutEtFin(AFUtil.getDateSituation(affiliation));
            mgr.find();
            for (int i = 0; i < mgr.size(); i++) {
                TICompositionTiers lien = (TICompositionTiers) mgr.getEntity(i);
                if (result.length() > 0) {
                    result.append("\n");
                }
                TITiers t = new TITiers();
                t.setSession(getSession());
                t.setIdTiers(lien.getIdTiersParent());
                result.append(t.getNomEtNumero());

            }
        } catch (Exception ex) {
            // return vide
            return "";
        }
        return result.toString();
    }

    // Retourne la cotisation la plus récente
    private AFCotisation getAssuranceAVSAIAPGPersonnelle(String idAffiliation) throws Exception {
        String idAssurance = getIdAssuranceAVSAIAPGPersonnelle();
        AFCotisationManager cotisations = new AFCotisationManager();
        cotisations.setSession(getSession());
        // On sette l'id d'affiliation
        cotisations.setForAffiliationId(idAffiliation);
        cotisations.setForAssuranceId(idAssurance);
        cotisations.find();
        // Si on a des cotisations, on va regarder quelle est la cotisation la
        // plus récente
        if (cotisations.size() > 0) {
            // initialisation de la cotisation à null
            AFCotisation cotisation = null;
            // Initialisation de plus grand avec la valeur de la première
            // cotisation
            AFCotisation cotiPlusGrand = (AFCotisation) cotisations.getFirstEntity();

            for (int i = 0; i < cotisations.size(); i++) {
                // On initialise la cotisation avec la valeur de l'id en cours
                cotisation = (AFCotisation) cotisations.getEntity(i);
                // Dans le cas d'une date vide, on sette une date bidon afin que
                // la
                // comparaison fonctionne
                String dateFinCotisation = cotisation.getDateFin();
                String dateFinCotisationPlusGrand = cotiPlusGrand.getDateFin();
                if (JadeStringUtil.isBlank(cotisation.getDateFin())) {
                    dateFinCotisation = AFBordereauMutation_Doc.DATE_FIN;
                }
                if (JadeStringUtil.isBlank(cotiPlusGrand.getDateFin())) {
                    dateFinCotisationPlusGrand = AFBordereauMutation_Doc.DATE_FIN;
                }
                // On compare la valeur actuelle avec la valeur la plus grande
                if (BSessionUtil.compareDateFirstGreater(getSession(), dateFinCotisation, dateFinCotisationPlusGrand)) {
                    // Dans le cas ou cotisation est plus grand que la valeur la
                    // plus grande on sette la valeur
                    // de cotisation dans cotiPlusGrand
                    cotiPlusGrand = cotisation;
                }
            }
            return cotiPlusGrand;
        } else {
            return null;
        }
    }

    // Permet de récupérer le lien avec le contact
    private TIAvoirContact getAvoirContact(TIAvoirContact avoirContact) throws Exception {
        avoirContact = new TIAvoirContact();
        avoirContact.setSession(getSession());
        avoirContact.setIdTiers(affiliation.getIdTiers());
        avoirContact.setIdContact(affiliation.getIdTiers());
        avoirContact.retrieve();
        return avoirContact;
    }

    /*
     * Affichage de toutes les caisse prof auxquelles l'affilié adhère (ou a adhéré) oca
     */
    public String getCaisseProf() {
        StringBuffer result = new StringBuffer();
        try {
            AFAdhesionManager mgr = new AFAdhesionManager();
            mgr.setSession(getSession());
            mgr.setForAffiliationId(getAffiliationId());
            mgr.changeManagerSize(0);
            mgr.find();
            Set<String> caissesProf = new TreeSet<String>();
            for (int i = 0; i < mgr.size(); i++) {
                AFAdhesion adh = (AFAdhesion) mgr.getEntity(i);
                caissesProf.add(adh.getAdministrationCaisse().getCodeAdministration());
            }
            String[] num = caissesProf.toArray(new String[caissesProf.size()]);
            result = AFBordereauMutation_Doc._buildBuffer(", ", num);
        } catch (Exception ex) {
            return "?";
        }
        return result.toString();
    }

    // Permet de récupérer les cotisations de l'affilié
    private StringBuffer getCotisations() throws Exception {
        StringBuffer res = new StringBuffer();
        // Recherche de toutes les cotisations de l'affilié
        AFCotisationManager cotisations = new AFCotisationManager();
        cotisations.setSession(getSession());
        // On sette l'id d'affiliation
        cotisations.setForAffiliationId(getAffiliationId());
        // On lui sette l'année en cours pour avoir les cotisations actives
        // l'année du lancement du document
        /*
         * cotisations.setForAnneeActive( String.valueOf(JACalendar.getYear(JACalendar.todayJJsMMsAAAA())));
         */
        // String forDate = "";
        // if(isAffiliationOuvert() || affiliation == null) {
        // forDate = getDateSituation();
        // } else {
        // forDate = affiliation.getDateFin();
        // }
        // cotisations.setForDate(forDate);
        cotisations.changeManagerSize(0);
        cotisations.find();

        Map<String, String[]> assurances = new LinkedHashMap<String, String[]>();

        // Pour toutes les cotisations trouvées on va lister chaque cotisation
        // avec sa date de début et de fin
        // sans tenir comptes des exceptions
        for (int i = 0; i < cotisations.size(); i++) {
            // On récupère la cotisation
            AFCotisation cotisation = (AFCotisation) cotisations.getEntity(i);
            String libelleAssurance = cotisation.getAssurance().getAssuranceLibelleCourt();
            // ne pas tenir compte des cotisations "exceptions"
            if (!CodeSystem.MOTIF_FIN_EXCEPTION.equals(cotisation.getMotifFin())) {
                String[] dates = assurances.get(libelleAssurance);

                if (dates == null) {
                    assurances.put(libelleAssurance,
                            new String[] { cotisation.getDateDebut(), cotisation.getDateFin() });
                } else {

                    String dateFin = cotisation.getDateFin();
                    if (JadeStringUtil.isEmpty(dateFin)) {
                        dateFin = "31.12.9999"; // 0 et vide sont considérés
                        // comme la plus grande des
                        // dates possible...
                    }

                    // Si la date de fin de la cotisation courante est plus
                    // grande que celle que j'ai dans ma liste,
                    // je met a jour ma liste
                    if (BSessionUtil.compareDateFirstGreater(getSession(), dateFin, dates[1])) {
                        assurances.put(libelleAssurance, new String[] { dates[0], cotisation.getDateFin() });
                    }

                    // Si la date de début de la cotisation courante est plus
                    // petit que celle que j'ai dans ma liste,
                    // je met a jour ma liste
                    if (BSessionUtil.compareDateFirstLower(getSession(), cotisation.getDateDebut(), dates[0])) {
                        assurances.put(libelleAssurance, new String[] { cotisation.getDateDebut(), dates[1] });
                    }
                }
            } // fin exception
        }

        /*
         * Construction du résultat final contenant 1x chaque assurance, avec la plus petite et la plus grande des dates
         * Trouvée au travers des cotisations
         */

        for (Iterator<String> it = assurances.keySet().iterator(); it.hasNext();) {
            String assurance = it.next();
            String dateDeb = (assurances.get(assurance))[0];
            String dateFin = (assurances.get(assurance))[1];
            if (res.length() > 0) {
                res.append("\n");
            }
            res.append("<style isBold='true'>" + assurance + "</style>");
            res.append("\nDébut: ");
            res.append(dateDeb);
            res.append(" / Fin: ");
            res.append(dateFin);
        }

        return res;
    }

    /**
     * @return
     */
    public String getDateSituation() {
        return dateSituation;
    }

    // Retourne le numéro d'affilié pour les relation de type
    // "personnel déclaré par" et "déclare personnel de"
    // En passant le type désiré
    private String getDeclarationPersonnel(String type) throws Exception {
        AFLienAffiliationJoinManager liens = new AFLienAffiliationJoinManager();
        liens.setSession(getSession());
        // Dans le cas d'une relation de type "personnel déclaré par" on va
        // rechercher tous les enregistrements
        // qui on l'id affiliation de l'affilié dans la colonne père c'est à
        // dire celle de où par le lien
        if (type.equals(AFBordereauMutation_Doc.PERSONNEL_DECLARE_PAR)) {
            liens.setForIdAffiliationParent(affiliation.getAffiliationId());
        } else {
            // Dans le cas contraire, on va regarder les enregistrements qui ont
            // l'id d'affiliation de l'affilié
            // dans la colonne fils c'est à dire de celle où arrive le lien
            liens.setForIdAffiliationEnfant(affiliation.getAffiliationId());
        }
        liens.setForTypeLien(CodeSystem.TYPE_LIEN_PERSONNEL_DECLARE);
        if (isAffiliationOuvert() || (affiliation == null)) {
            liens.setForDateEntreDebutEtFin(getDateSituation());
        } else {
            liens.setForDateEntreDebutEtFin(affiliation.getDateFin());
        }
        liens.changeManagerSize(0);
        liens.find();
        String res = "";

        for (int i = 0; i < liens.size(); i++) {
            AFLienAffiliationJoin lien = (AFLienAffiliationJoin) liens.getEntity(i);
            // Dans le cas d'une relation de type "personnel déclaré par", on
            // affiche le numéro d'affilié
            // du fils
            if (type.equals(AFBordereauMutation_Doc.PERSONNEL_DECLARE_PAR)) {
                res += lien.getNumeroAffilieEnfant();
            } else {
                // Dans le cas contraire on affiche le numéro d'affilié du père
                res += lien.getNumeroAffilieParent();
            }
            // Si on est pas au dernier affilié, on sépare les deux affiliés par
            // des ","
            if (i + 1 < liens.size()) {
                res += ",  ";
            }
            if (i % 4 == 3) {
                res += "\n";
            }
        }
        return res;
    }

    /**
     * 
     * @return
     */
    public List<String> getDisplayFields() {
        return displayFieds;
    }

    private ICTDocument[] getICTDocument() {
        ICTDocument res[] = null;
        ICTDocument document = null;
        try {
            document = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
            document.setISession(getSession());
            // On sette le domaine
            document.setCsDomaine(CodeSystem.DOMAINE_CAT_AFF);
            // On sette le type de document
            document.setCsTypeDocument(CodeSystem.BORDEREAU_MUTATION);
            // On dit que le document doit être actif pour être sélectionné
            document.setActif(new Boolean(true));
            // On dit qu'on prend le document qui est par défaut
            document.setDefault(new Boolean(true));
            res = document.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWMessage.ERREUR, "Error while getting document");
        }
        return res;
    }

    // Retourne une String contenant l'id de l'assurance concernée par le type
    // passé en paramètres
    private String getIdAssuranceAVSAIAPGPersonnelle() throws Exception {
        String res = "";
        AFAssuranceManager assuranceManager = new AFAssuranceManager();
        assuranceManager.setSession(getSession());
        assuranceManager.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
        assuranceManager.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
        assuranceManager.find();
        if (assuranceManager.size() > 0) {
            AFAssurance ass = (AFAssurance) assuranceManager.getFirstEntity();
            res = ass.getAssuranceId();
        }
        return res;
    }

    // Retourne le lien de taxe sous plus grand
    private AFLienAffiliation getLienTaxeSous(String idAffiliation) throws Exception {
        AFLienAffiliationManager lienAffiliationManager = new AFLienAffiliationManager();
        lienAffiliationManager.setSession(getSession());
        lienAffiliationManager.setForAffiliationId(idAffiliation);
        lienAffiliationManager.setForTypeLien(CodeSystem.TYPE_LIEN_TAXE_SOUS);
        // lienAffiliationManager.setForDate(JACalendar.todayJJsMMsAAAA());
        lienAffiliationManager.find();
        if (lienAffiliationManager.size() > 0) {
            AFLienAffiliation lien = null;
            AFLienAffiliation lienPlusGrand = (AFLienAffiliation) lienAffiliationManager.getFirstEntity();
            for (int i = 0; i < lienAffiliationManager.size(); i++) {
                // On initialise le lien avec la valeur de l'id en cours
                lien = (AFLienAffiliation) lienAffiliationManager.getEntity(i);
                // Dans le cas d'une date vide, on sette une date bidon afin que
                // la
                // comparaison fonctionne
                String dateFinLienAffiliation = lien.getDateFin();
                String dateFinLienAffiliationPlusGrand = lienPlusGrand.getDateFin();
                if (JadeStringUtil.isBlank(lien.getDateFin())) {
                    dateFinLienAffiliation = AFBordereauMutation_Doc.DATE_FIN;
                }
                if (JadeStringUtil.isBlank(lienPlusGrand.getDateFin())) {
                    dateFinLienAffiliationPlusGrand = AFBordereauMutation_Doc.DATE_FIN;
                }
                // On compare la valeur actuelle avec la valeur la plus grande
                if (BSessionUtil.compareDateFirstGreater(getSession(), dateFinLienAffiliation,
                        dateFinLienAffiliationPlusGrand)) {
                    // Dans le cas ou lien est plus grand que la valeur la plus
                    // grande on sette la valeur
                    // de lien dans lienPlusGrand
                    lienPlusGrand = lien;
                }
            }
            return lienPlusGrand;
        } else {
            return null;
        }
    }

    // Permet de récupérer le moyen de cummunication en lui passant en
    // paramètres le type de communication désiré
    private String getMoyenCommunication(String idTypeCommunication) throws Exception {
        TIAvoirContact avoirContact = null;
        avoirContact = getAvoirContact(avoirContact);
        // Si on a pas de contact c'est qu'on a pas de numéro de téléphone donc
        // on retourne vide
        if (avoirContact.isNew()) {
            return "";
        } else {
            // Dans le cas où il y a un contact, on va rechercher le moyen de
            // communication
            TIMoyenCommunication moyenCommunication = new TIMoyenCommunication();
            moyenCommunication.setSession(getSession());
            moyenCommunication.setIdContact(avoirContact.getIdContact());
            moyenCommunication.setIdApplication(IConstantes.CS_APPLICATION_DEFAUT);
            moyenCommunication.setTypeCommunication(idTypeCommunication);
            moyenCommunication.retrieve();
            // S'il n'y a pas de moyen de communication, on retourne vide
            if (moyenCommunication.isNew()) {
                return "";
            } else {
                // Sinon on retourne le moyen de communication
                return moyenCommunication.getMoyen();
            }
        }
    }

    /**
     * @return
     */
    public String getObservations() {
        return observations;
    }

    // Retourne la particularité passée en paramètres ("Sans personnel dès le"
    // ou "Administrateurs")
    private String getParticularite(String typeParticularite) throws Exception {
        String res = "";
        AFParticulariteAffiliationManager particulariteManager = new AFParticulariteAffiliationManager();
        particulariteManager.setSession(getSession());
        particulariteManager.setForAffiliationId(affiliation.getAffiliationId());
        particulariteManager.setForParticularite(typeParticularite);
        if (isAffiliationOuvert() || (affiliation == null)) {
            particulariteManager.setForDate(getDateSituation());
        } else {
            particulariteManager.setForDate(affiliation.getDateFin());
        }
        particulariteManager.find();
        AFParticulariteAffiliation particularite = null;
        for (int i = 0; i < particulariteManager.size(); i++) {
            particularite = (AFParticulariteAffiliation) particulariteManager.getEntity(i);
            res = particularite.getDateDebut();
        }
        return res;
    }

    /**
     * Retourne l'alias du responable Date de création : (02.05.2003 11:59:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String getResponsable() {
        // Si responsable est vide, on charge le responsable
        if (JadeStringUtil.isBlank(responsable)) {
            responsable = "";
            try {
                JadeUser user = JadeAdminServiceLocatorProvider.getLocator().getUserService()
                        .load(getSession().getUserId());
                if (user != null) {
                    responsable = user.getFirstname() + " " + user.getLastname();
                }
            } catch (Exception e) {
                // si on ne trouve pas de responsable, ce n'est pas grave...
            }
        }
        return responsable;
    }

    // Retourne le lien de suivi des caisses le plus récent
    private AFSuiviCaisseAffiliation getSuiviCaisse(String genreCaisse) throws Exception {
        AFSuiviCaisseAffiliationManager suiviCaisseManager = new AFSuiviCaisseAffiliationManager();
        suiviCaisseManager.setSession(getSession());
        suiviCaisseManager.setForAffiliationId(affiliationId);
        suiviCaisseManager.setForGenreCaisse(genreCaisse);
        /*
         * suiviCaisseManager.setForAnneeActive( String.valueOf(JACalendar.getYear(JACalendar.todayJJsMMsAAAA())));
         */// suiviCaisseManager.setForDate(JACalendar.todayJJsMMsAAAA());
        suiviCaisseManager.find();
        if (suiviCaisseManager.size() > 0) {
            AFSuiviCaisseAffiliation suiviCaisse = null;
            AFSuiviCaisseAffiliation suiviCaissePlusGrand = (AFSuiviCaisseAffiliation) suiviCaisseManager
                    .getFirstEntity();
            for (int i = 0; i < suiviCaisseManager.size(); i++) {
                // On initialise le lien avec la valeur de l'id en cours
                suiviCaisse = (AFSuiviCaisseAffiliation) suiviCaisseManager.getEntity(i);
                // Dans le cas d'une date vide, on sette une date bidon afin que
                // la
                // comparaison fonctionne
                String dateFinLienAffiliation = suiviCaisse.getDateFin();
                String dateFinLienAffiliationPlusGrand = suiviCaissePlusGrand.getDateFin();
                if (JadeStringUtil.isBlank(suiviCaisse.getDateFin())) {
                    dateFinLienAffiliation = AFBordereauMutation_Doc.DATE_FIN;
                }
                if (JadeStringUtil.isBlank(suiviCaissePlusGrand.getDateFin())) {
                    dateFinLienAffiliationPlusGrand = AFBordereauMutation_Doc.DATE_FIN;
                }
                // On compare la valeur actuelle avec la valeur la plus grande
                if (BSessionUtil.compareDateFirstGreater(getSession(), dateFinLienAffiliation,
                        dateFinLienAffiliationPlusGrand)) {
                    // Dans le cas ou le suivi est plus grand que la valeur la
                    // plus grande on sette la valeur
                    // de suiviCaisse dans suiviCaissePlusGrand
                    suiviCaissePlusGrand = suiviCaisse;
                }
            }
            return suiviCaissePlusGrand;
        } else {
            return null;
        }
    }

    // Renvoie le bon texte selon le fait qu'il y a ou non un lien de type
    // "taxe sous"
    private String getTaxeSousOrTaxeParLEFT(boolean taxeSous) throws Exception {
        // On initialise le résultat à vide
        String res = AFBordereauMutation_Doc.LIGNEVIDE;
        // Dans le cas ou on a un lien de type "taxé sous", on va lui passer le
        // texte du niveau 2 position 5
        if (taxeSous) {
            res = getTexte(AFBordereauMutation_Doc.NIVEAU_LIBELLE, AFBordereauMutation_Doc.POSITION_TAXESOUS);
        } else {
            // Dans le cas contraire, on lui passera le texte du niveau 2
            // position 6
            res = getTexte(AFBordereauMutation_Doc.NIVEAU_LIBELLE, AFBordereauMutation_Doc.POSITION_TAXEPAR);
        }
        return res;
    }

    // Renvoie les bonnes données selon si le lien est de type "taxe sous" ou
    // "taxe par"
    private String getTaxeSousOrTaxeParRIGHT(AFLienAffiliation lienAffiliation) throws Exception {
        // On initialise le résultat à vide
        StringBuffer res = new StringBuffer(AFBordereauMutation_Doc.LIGNEVIDE);
        AFSuiviCaisseAffiliation suiviCaisse = getSuiviCaisse(CodeSystem.GENRE_CAISSE_AVS);
        AFCotisation cotisation = getAssuranceAVSAIAPGPersonnelle(affiliationId);

        // Cas ou suivi des caisses est null est tout les autres remplis
        if ((suiviCaisse == null) && (cotisation != null) && (lienAffiliation != null)) {
            if (isLienTaxeSousOuvert(lienAffiliation)) {
                res.append(executeTraitementTaxeSous(lienAffiliation));
            } else if (isCotisationOuvert(cotisation)) {
                res.append(executeTraitementTaxeParCaisse());
            }
            // Cas ou lien affiliation est nul et tous les autres remplis
        } else if ((suiviCaisse != null) && (cotisation != null) && (lienAffiliation == null)) {
            if (isCotisationOuvert(cotisation)) {
                res.append(executeTraitementTaxeParCaisse());
            } else if (isSuiviCaisseOuvert(suiviCaisse)) {
                res.append(executeTraitementTaxeParCaisseSuiviCaisse(suiviCaisse));
            }
            // Cas où cotisation est nul et tous les autres remplis
        } else if ((suiviCaisse != null) && (cotisation == null) && (lienAffiliation != null)) {
            if (isLienTaxeSousOuvert(lienAffiliation)) {
                res.append(executeTraitementTaxeSous(lienAffiliation));
            } else if (isSuiviCaisseOuvert(suiviCaisse)) {
                res.append(executeTraitementTaxeParCaisseSuiviCaisse(suiviCaisse));
            }
            // Cas ou uniquement cotisation est rempli
        } else if ((cotisation != null) && (suiviCaisse == null) && (lienAffiliation == null)) {
            if (isCotisationOuvert(cotisation)) {
                res.append(executeTraitementTaxeParCaisse());
            }
            // Cas ou uniquement suiviCaisse est rempli
        } else if ((cotisation == null) && (suiviCaisse != null) && (lienAffiliation == null)) {
            if (isSuiviCaisseOuvert(suiviCaisse)) {
                res.append(executeTraitementTaxeParCaisseSuiviCaisse(suiviCaisse));
            }
            // Cas ou uniquement lienAffiliation est rempli
        } else if ((cotisation == null) && (suiviCaisse == null) && (lienAffiliation != null)) {
            if (isLienTaxeSousOuvert(lienAffiliation)) {
                res.append(executeTraitementTaxeSous(lienAffiliation));
            }
            // Cas ou tout est rempli
        } else if ((cotisation != null) && (lienAffiliation != null) && (suiviCaisse != null)) {
            if (isLienTaxeSousOuvert(lienAffiliation)
                    || (isAffiliationOuvert()
                            && isDateFirstPlusGrand(lienAffiliation.getDateFin(), cotisation.getDateFin()) && isDateFirstPlusGrand(
                                lienAffiliation.getDateFin(), suiviCaisse.getDateFin()))) {
                res.append(executeTraitementTaxeSous(lienAffiliation));
            } else if (isCotisationOuvert(cotisation)
                    || ((!isCotisationOuvert(cotisation)) && !isAffiliationOuvert()
                            && isDateFirstPlusGrand(cotisation.getDateFin(), lienAffiliation.getDateFin()) && isDateFirstPlusGrand(
                                cotisation.getDateFin(), suiviCaisse.getDateFin()))) {
                res.append(executeTraitementTaxeParCaisse());
            } else if ((!isCotisationOuvert(cotisation) && isSuiviCaisseOuvert(suiviCaisse))
                    || (!isCotisationOuvert(cotisation) && !isSuiviCaisseOuvert(suiviCaisse) && !isAffiliationOuvert()
                            && isDateFirstPlusGrand(suiviCaisse.getDateFin(), lienAffiliation.getDateFin()) && isDateFirstPlusGrand(
                                suiviCaisse.getDateFin(), cotisation.getDateFin()))) {
                res.append(executeTraitementTaxeParCaisseSuiviCaisse(suiviCaisse));
            }
        }

        return res.toString();
    }

    /**
     * Retourne le texte à un niveau donné
     * 
     * @param niveau
     * @return
     * @throws Exception
     */
    private String getTexte(int niveau, int position) throws Exception {
        String resString = "";
        ICTTexte texte = null;
        if (document == null) {
            getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
        } else {
            ICTListeTextes listeTextes = null;
            try {
                listeTextes = document[0].getTextes(niveau);
            } catch (Exception e3) {
                getMemoryLog().logMessage(e3.toString(), FWMessage.ERREUR,
                        getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
            }
            if (listeTextes == null) {
                getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE"), FWMessage.ERREUR, "");
            } else {
                texte = listeTextes.getTexte(position);
                resString = texte.toString();
            }
        }
        return resString;
    }

    // Permet de récupérer le tiers
    private TITiersViewBean getTiers() throws Exception {
        TITiersViewBean tiers = new TITiersViewBean();
        tiers.setSession(getSession());
        tiers.setIdTiers(affiliation.getIdTiers());
        tiers.retrieve();
        if (tiers.isNew()) {
            getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("TIERS_NON_TROUVE"));
        }
        return tiers;
    }

    // Retourne la valeur du code système
    private String getValeurCodeSysteme(String idCodeSysteme) throws Exception {
        FWParametersUserCode code = new FWParametersUserCode();
        code.setSession(getSession());
        // On sette l'id du code système
        code.setIdCodeSysteme(idCodeSysteme);
        // On sette la langue d'affichage du libellé du code système
        code.setIdLangue(getSession().getIdLangue());
        code.retrieve();
        // Dans le cas où on trouve pas de code système on affiche une erreur
        if (code.isNew()) {
            getMemoryLog().logMessage("", FWMessage.ERREUR,
                    getSession().getLabel("CODE_SYSTEM_NOT_FOUND + n°" + affiliation.getAffilieNumero()));
        }
        return code.getLibelle();
    }

    // Teste si l'affiliation est ouverte
    private boolean isAffiliationOuvert() throws Exception {
        String date = "";
        if (JadeStringUtil.isBlank(affiliation.getDateFin())) {
            date = AFBordereauMutation_Doc.DATE_FIN;
        } else {
            date = affiliation.getDateFin();
        }
        return BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), date, getDateSituation());
    }

    // Teste si le lien cotisation est ouvert
    private boolean isCotisationOuvert(AFCotisation cotisation) throws Exception {
        String date = "";
        if (JadeStringUtil.isBlank(cotisation.getDateFin())) {
            date = AFBordereauMutation_Doc.DATE_FIN;
        } else {
            date = cotisation.getDateFin();
        }
        return BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), date, getDateSituation());
    }

    // Teste si la première date est plus grande que la deuxième
    private boolean isDateFirstPlusGrand(String dateFirst, String dateSecond) throws Exception {
        String dateFirstFormated = "";
        String dateSecondFormated = "";
        if (JadeStringUtil.isBlank(dateFirst)) {
            dateFirstFormated = AFBordereauMutation_Doc.DATE_FIN;
        } else {
            dateFirstFormated = dateFirst;
        }
        if (JadeStringUtil.isBlank(dateSecond)) {
            dateSecondFormated = AFBordereauMutation_Doc.DATE_FIN;
        } else {
            dateSecondFormated = dateSecond;
        }
        return BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), dateFirstFormated, dateSecondFormated);
    }

    // Teste si le lien taxé sous est ouvert
    private boolean isLienTaxeSousOuvert(AFLienAffiliation lienAffiliation) throws Exception {
        String date = "";
        if (JadeStringUtil.isBlank(lienAffiliation.getDateFin())) {
            date = AFBordereauMutation_Doc.DATE_FIN;
        } else {
            date = lienAffiliation.getDateFin();
        }
        return BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), date, getDateSituation());
    }

    // Teste si le lien suiviCaisse est ouvert
    private boolean isSuiviCaisseOuvert(AFSuiviCaisseAffiliation suiviCaisse) throws Exception {
        String date = "";
        if (JadeStringUtil.isBlank(suiviCaisse.getDateFin())) {
            date = AFBordereauMutation_Doc.DATE_FIN;
        } else {
            date = suiviCaisse.getDateFin();
        }
        return BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), date, getDateSituation());
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public boolean next() throws FWIException {
        if (isFirst) {
            isFirst = false;
            return true;
        } else {
            return false;
        }
    } /*
       * (non-Javadoc)
       * 
       * @see globaz.globall.db.BProcess#jobQueue()
       */

    /**
     * @param string
     */
    public void setAffiliationId(String string) {
        affiliationId = string;
    }

    /**
     * @param string
     */
    public void setDateSituation(String string) {
        dateSituation = string;
    }

    /**
     * 
     * @param value
     */
    public void setDisplayFields(Collection<? extends String> value) {
        if (displayFieds == null) {
            displayFieds = new ArrayList<String>();
        }
        displayFieds.clear();
        displayFieds.addAll(value);
    }

    /**
     * 
     * @param fields
     */
    public void setDisplayFields(String fields) {
        if (displayFieds == null) {
            displayFieds = new ArrayList<String>();
        }
        displayFieds.add(fields);
    }

    /**
     * @param string
     */
    public void setObservations(String string) {
        observations = string;
    }

    // Permet de setter la valeur de la rubrique en lui passant le numéro de la
    // ligne en paramètres
    private String setValeur(int numLigne) throws Exception {
        String res = null;
        TITiersViewBean tiers = getTiers();
        // On regarde à quoi correspond le numéro de la ligne
        switch (numLigne) {
        // Cas où le numéro de ligne correspond à la ligne pour la raison
        // sociale
            case CASE_RAISON_SOCIALE:
                res = affiliation.getRaisonSociale();
                break;
            // Cas où le numéro de ligne correspond à la ligne du code indépendant
            case CASE_CODE_INDEPENDANT:
                if (affiliation.getPersonnaliteJuridique().equals(CodeSystem.PERS_JURIDIQUE_RAISON_INDIVIDUELLE)
                        && (JadeStringUtil.isEmpty(affiliation.getTypeAssocie()) || affiliation.getTypeAssocie()
                                .equals("0"))) {
                    res = getSession().getLabel("RAISON_INDIVIDUELLE");
                } else if (!JadeStringUtil.isEmpty(affiliation.getTypeAssocie())
                        && !affiliation.getTypeAssocie().equals("0")) {
                    res = getValeurCodeSysteme(affiliation.getTypeAssocie());
                }
                break;
            // Cas où le numéro de ligne correspond à la ligne pour le numéro
            // d'affilié
            case CASE_NUM_AFFILIE:
                res = affiliation.getAffilieNumero();
                break;
            // Cas où le numéro de ligne correspond à à la ligne pour l'état civil
            case CASE_ETAT_CIVIL:
                // On recherche la valeur du code système uniquement si ce dernier
                // est différent de 0
                if (!tiers.getEtatCivil().equals("0")) {
                    res = getValeurCodeSysteme(tiers.getEtatCivil());
                }
                break;
            // Cas où le numéro de ligne correspond à la ligne pour le sexe
            case CASE_SEXE:
                // On recherche la valeur du code système uniquement si ce dernier
                // est différent de 0
                if (!tiers.getSexe().equals("0")) {
                    res = getValeurCodeSysteme(tiers.getSexe());
                }
                break;
            // Cas où le numéro de ligne correspond à la ligne pour la date de
            // naissance
            case CASE_DATE_NAISSANCE:
                res = tiers.getDateNaissance();
                break;
            // Cas où le numéro de ligne correspond à la ligne pour la nationalité
            case CASE_NATIONALITE:
                res = tiers.getNomPays();
                break;
            // Cas où le numéro de ligne correspond à la ligne pour la personnalité
            // juridique
            case CASE_PERS_JURIDIQUE:
                res = getValeurCodeSysteme(affiliation.getPersonnaliteJuridique());
                break;
            // Cas où le numéro de ligne correspond à la ligne pour le nom
            case CASE_NOM:
                res = tiers.getDesignation1();
                break;
            // Cas où le numéro de ligne correspond à la ligne pour le prénom
            case CASE_PRENOM:
                res = tiers.getDesignation2();
                break;
            // Cas où le numéro de ligne correspond à la ligne pour le numéro AVS
            case CASE_NUM_AVS:
                res = tiers.getNumAvsActuel();
                break;
            // Cas où le numéro de ligne correspond à la ligne pour l'adresse de
            // courrier
            case CASE_ADRESSE_COURRIER:
                if (doIneedToPrintThis(CodeSystem.CHAMPS_MOD_ADRESSE_COURRIER)) {
                    // Domaine COTISATIONS
                    res = tiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                            ICommonConstantes.CS_APPLICATION_COTISATION,
                            affiliation != null ? affiliation.getAffilieNumero() : null, getDateSituation(),
                            new TIAdresseFormater(), false, null);
                    if (JadeStringUtil.isEmpty(res)) {
                        // aucune adresse avec le no, recherche sans no
                        res = tiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                                ICommonConstantes.CS_APPLICATION_COTISATION, "", getDateSituation(),
                                new TIAdresseFormater(), false, null);
                    }
                    // Domaine par défaut
                    res = tiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                            IConstantes.CS_APPLICATION_DEFAUT, affiliation != null ? affiliation.getAffilieNumero()
                                    : null, getDateSituation(), new TIAdresseFormater(), false, null);
                    if (JadeStringUtil.isEmpty(res)) {
                        // aucune adresse avec le no, recherche sans no
                        res = tiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                                IConstantes.CS_APPLICATION_DEFAUT, "", getDateSituation(), new TIAdresseFormater(),
                                false, null);
                    }
                    if (res != null) {
                        res = res.trim();
                    }
                }
                break;
            // Cas où le numéro de ligne correspond à la ligne pour l'adresse de
            // domicile
            case CASE_ADRESSE_DOMICILE:
                if (doIneedToPrintThis(CodeSystem.CHAMPS_MOD_ADRESSE_DOMICILE)) {
                    res = tiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                            IConstantes.CS_APPLICATION_DEFAUT, affiliation != null ? affiliation.getAffilieNumero()
                                    : null, getDateSituation(), new TIAdresseFormater(), false, null);
                    if (JadeStringUtil.isEmpty(res)) {
                        // aucune adresse avec le no, recherche sans no
                        res = tiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                                IConstantes.CS_APPLICATION_DEFAUT, "", getDateSituation(), new TIAdresseFormater(),
                                false, null);
                    }
                    if (res != null) {
                        res = res.trim();
                    }

                }
                break;
            // Cas où le numéro de ligne correspond à la ligne pour l'adresse
            // professionnelle
            case CASE_ADRESSE_PROFESSIONNELLE:
                if (doIneedToPrintThis(CodeSystem.CHAMPS_MOD_ADRESSE_PROFES)) {
                    res = tiers.getAdresseAsString(CodeSystem.ADRESSE_PROFESSIONNELLE,
                            IConstantes.CS_APPLICATION_DEFAUT, affiliation != null ? affiliation.getAffilieNumero()
                                    : null, getDateSituation(), new TIAdresseFormater(), false, null);
                    if (JadeStringUtil.isEmpty(res)) {
                        // aucune adresse avec le no, recherche sans no
                        res = tiers.getAdresseAsString(CodeSystem.ADRESSE_PROFESSIONNELLE,
                                IConstantes.CS_APPLICATION_DEFAUT, "", getDateSituation(), new TIAdresseFormater(),
                                false, null);
                    }
                    if (res != null) {
                        res = res.trim();
                    }

                }
                break;
            // Cas où le numéro de ligne correspond à la ligne pour le
            // déclarePersonnel
            case CASE_DECLARE_PERSONNEL:
                res = getDeclarationPersonnel(AFBordereauMutation_Doc.DECLARE_PERSONNEL_DE);
                break;
            // Cas où le numéro de ligne correspond à la ligne pour le
            // personnelDeclarePar
            case CASE_PERSONNEL_DECLARE_PAR:
                res = getDeclarationPersonnel(AFBordereauMutation_Doc.PERSONNEL_DECLARE_PAR);
                break;
            // Cas où le numéro de ligne correspond à la ligne pour la société
            // immoblière
            case CASE_SOCIETE_IMMOBILIERE:
                // Si la personnalité juridique est une société immobilière on met
                // oui
                if (affiliation.getPersonnaliteJuridique().equals(CodeSystem.PERS_JURIDIQUE_SOCIETE_IMMOBILIERE)) {
                    res = getSession().getLabel("NAOS_LIBELLE_OUI");
                } else {
                    // Sinon on lui met non
                    res = getSession().getLabel("NAOS_LIBELLE_NON");
                }
                break;
            // Cas où le numéro de ligne correspond à la ligne pour le avecPersonnel
            case CASE_AVEC_PERSONNEL:
                res = getParticularite(CodeSystem.AVEC_PERSONNEL);
                break;
            // Cas où le numéro de ligne correspond à la ligne pour le sansPersonnel
            case CASE_SANS_PERSONNEL:
                res = getParticularite(CodeSystem.PARTIC_AFFILIE_SANS_PERSONNEL);
                break;
            // Cas où le numéro de ligne correspond à la ligne pour l'administrateur
            case CASE_ADMINISTRATEUR:
                res = getParticularite(CodeSystem.ADMINISTRATEUR);
                break;
            // Cas où le numéro de ligne correspond à la ligne pour l'activité
            // lucrative
            case CASE_SANS_ACTIVITE_LUCRATIVE:
                // Dans le cas où la personnalité juridique est NA on passe activité
                // lucrative à oui
                if (affiliation.getPersonnaliteJuridique().equals(CodeSystem.PERS_JURIDIQUE_NA)) {
                    res = getSession().getLabel("NAOS_LIBELLE_OUI");
                } else {
                    // Dans le cas contraire on lui met la valeur non
                    res = getSession().getLabel("NAOS_LIBELLE_NON");
                }
                break;

            case CASE_PP_PARITAIRES:
                res = getParticularite(CodeSystem.PARTIC_AFFILIE_PP_PAR);
                break;
            case CASE_PP_PERSONNELLE:
                res = getParticularite(CodeSystem.PARTIC_AFFILIE_PP_PERS);
                break;

            // Cas où le numéro de ligne correspond à la ligne pour le numéro de
            // téléphone
            case CASE_NUM_TELEPHONE:
                res = getMoyenCommunication(TIMoyenCommunication.PROFESSIONNEL);
                break;
            // Cas où le numéro de ligne correspond à la ligne pour le numéro de fax
            case CASE_NUM_FAX:
                res = getMoyenCommunication(TIMoyenCommunication.FAX);
                break;
            // Cas où le numéro de ligne correspond à la ligne pour le numéro du
            // téléphone portable
            case CASE_NUM_NATEL:
                res = getMoyenCommunication(TIMoyenCommunication.PORTABLE);
                break;
            // Cas où le numéro de ligne correspond à la ligne pour les cotisations
            case CASE_COTISATIONS:
                if (doIneedToPrintThis(CodeSystem.CHAMPS_MOD_CREATION_COTI)) {
                    res = getCotisations().toString();
                }
                break;
            case CASE_OBSERVATIONS:
                res = getObservations();
                break;
            case CASE_CAISSE_PROF:
                res = getCaisseProf();
                break;
            case CASE_ASSOCIATIONS:
                res = getAssociations(tiers);
                break;

        }
        return res;
    }

}
