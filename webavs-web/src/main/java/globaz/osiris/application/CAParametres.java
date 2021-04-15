package globaz.osiris.application;

import globaz.globall.db.*;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.JadeLoggerUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.webavs.common.CommonProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * CAParametres contient les paramètres spécifiques à une instance de l'application OSIRIS
 * 
 * @author: E.Fleury
 */
public class CAParametres {
    public final static int BVR_DATE_DEPOT = 1;
    public final static int BVR_DATE_INSCRIPTION = 2;
    public final static int BVR_DATE_TRAITEMENT = 0;

    private static final int DEFAULT_DELAI = 10;
    private static final int DELAI_PMT_BVR = 5; // jours
    private static int delaiEcriture = 0;
    private static int delaiPmtBvr = 0;

    private static int delaiRappel = 0;

    private static int delaiSuspension = 0;
    private static int limiteImpressionEcrituresJournal = 10000;

    private static String montant = null;
    private static final int MONTANT_MAX_DELTA_INTERET_IMPUTER = 1000;
    private static final String MONTANT_MINIME = "2";
    private static int montantMaxDeltaInteretCalcule = 0;
    private static final int NB_DAYS_MAX = 14; // 14 jours
    private static final int TAUX_MARGE_MAX_DELTA_INTERET_CALCULE_OP = 5;
    private static int tauxMargeDeltaInteretCalculeOP = 0;

    /**
     * Retourne le delai en jour pour la date valeur de la table de paramètres (FWPARAP)<br>
     * Si le paramètre n'est pas définit, renvoit par défaut 14.<br>
     * Utilisé dans CAEcriture et CAProcessContentieux
     * 
     * @author: sel Créé le : 16 janv. 07
     * @return le delai date valeur de FWPARAP sinon 14
     * @throws Exception
     */
    public static int getDelaiDateValeur(BTransaction transaction) throws NumberFormatException {
        if (CAParametres.delaiEcriture == 0) {
            try {
                CAParametres.delaiEcriture = Integer.parseInt(FWFindParameter.findParameter(transaction, "0",
                        "DELAIDATE", "0", "0", 0));
            } catch (Exception e) {
                CAParametres.delaiEcriture = CAParametres.NB_DAYS_MAX;
            }
        }
        return CAParametres.delaiEcriture;
    }

    /**
     * Retourne le delai en jour pour un paiement BVR<br>
     * Si le paramètre n'est pas définit, renvoit par défaut 5.<br>
     * 
     * @return le delai en jour pour un paiement BVR.
     */
    public static int getDelaiPmtBvr(BTransaction transaction) {
        if (CAParametres.delaiPmtBvr == 0) {
            try {
                CAParametres.delaiPmtBvr = Integer.parseInt(FWFindParameter.findParameter(transaction, "0",
                        "DELAIPTBVR", "0", "0", 0));
            } catch (Exception e) {
                CAParametres.delaiPmtBvr = CAParametres.DELAI_PMT_BVR;
            }
        }
        return CAParametres.delaiPmtBvr;
    }

    /**
     * Retourne le delai en jour pour le rappel des sursis au paiement non respectés<br>
     * Si le paramètre n'est pas définit, renvoit par défaut 10.<br>
     * 
     * @return le delai en jour pour le rappel
     */
    public static int getDelaiRappel(BTransaction transaction) {
        if (CAParametres.delaiRappel == 0) {
            try {
                CAParametres.delaiRappel = Integer.parseInt(FWFindParameter.findParameter(transaction, "0",
                        "DELAIRAPP", "0", "0", 0));
            } catch (Exception e) {
                CAParametres.delaiRappel = CAParametres.DEFAULT_DELAI;
            }
        }
        return CAParametres.delaiRappel;
    }

    /**
     * Retourne le delai en jour pour la suspension des sursis au paiement non respectés<br>
     * Si le paramètre n'est pas définit, renvoit par défaut 10.<br>
     * 
     * @return le delai en jour pour la suspension
     */
    public static int getDelaiSuspension(BTransaction transaction) {
        if (CAParametres.delaiSuspension == 0) {
            try {
                CAParametres.delaiSuspension = Integer.parseInt(FWFindParameter.findParameter(transaction, "0",
                        "DELAISUSP", "0", "0", 0));
            } catch (Exception e) {
                CAParametres.delaiSuspension = CAParametres.DEFAULT_DELAI;
            }
        }
        return CAParametres.delaiSuspension;
    }

    /**
     * @param transaction
     * @return le montant max tolérée de différence entre l'interet calculé par l'appli et l'interet de l'OP
     * @throws NumberFormatException
     */
    public static int getMontantMaxDeltaInteretCalcule(BTransaction transaction) throws NumberFormatException {
        if (CAParametres.montantMaxDeltaInteretCalcule == 0) {
            try {
                CAParametres.montantMaxDeltaInteretCalcule = Integer.parseInt(FWFindParameter.findParameter(
                        transaction, "0", "MTDIFMAXIM", "0", "0", 0));
            } catch (Exception e) {
                CAParametres.montantMaxDeltaInteretCalcule = CAParametres.MONTANT_MAX_DELTA_INTERET_IMPUTER;
            }
        }
        return CAParametres.montantMaxDeltaInteretCalcule;
    }

    /**
     * Retourne le montant minime<br>
     * Si le paramètre n'est pas définit, renvoit par défaut 2.<br>
     * 
     * @author: sel Créé le : 18 janv. 07
     * @return le montant minime
     */
    public static String getMontantMinime(BTransaction transaction) {
        if (CAParametres.montant == null) {
            try {
                CAParametres.montant = FWFindParameter.findParameter(transaction, "0", "MNTMINIME", "0", "0", 0);
            } catch (Exception e) {
                CAParametres.montant = CAParametres.MONTANT_MINIME;
            }
        }
        return CAParametres.montant;
    }

    /**
     * @param transaction
     * @return la marge tolérée de différence entre l'interet calculé par l'appli et l'interet de l'OP
     * @throws NumberFormatException
     */
    public static int getTauxMargeDeltaInteretCalcule(BTransaction transaction) throws NumberFormatException {
        if (CAParametres.tauxMargeDeltaInteretCalculeOP == 0) {
            try {
                CAParametres.tauxMargeDeltaInteretCalculeOP = Integer.parseInt(FWFindParameter.findParameter(
                        transaction, "0", "TXMARGIMOP", "0", "0", 0));
            } catch (Exception e) {
                CAParametres.tauxMargeDeltaInteretCalculeOP = CAParametres.TAUX_MARGE_MAX_DELTA_INTERET_CALCULE_OP;
            }
        }
        return CAParametres.tauxMargeDeltaInteretCalculeOP;
    }

    private CAApplication caApplication;

    private Properties msg = null;

    /**
     * Constructeur du type CAParametres
     * 
     * @param application
     *            l'application
     */
    public CAParametres(CAApplication application) {
        super();

        caApplication = application;
    }

    public int getAncienNoAdherentBVR() {
        return Integer.parseInt(caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_ANCIEN_NO_ADHERENT_BVR, "0"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.12.2001 09:42:12)
     * 
     * @return String
     */
    public String getApplicationExterne() {
        return caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_EXTERNAL_APPLICATION, null);
    }

    /**
     * Cette méthode permet de récupérer la propriété qui indique quelle date doit être prise en compte en tant que date
     * de valeur lors du traitement des BVR 0 = Date de traitement (par défaut) 1 = Date de dépôt 2 = Date d'inscription
     * 
     * @return int de la valeur de la date à prendre en compte (0 par défaut)
     */
    public int getDateValeurBVR() {
        return Integer.parseInt(caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_DATE_VALEUR_BVR, "0"));
    }

    /**
     * Représente le format d'un nuéméro d'affilié. Définit dans osiris.properties.
     * 
     * @return le format d'un nuéméro d'affilié.
     */
    public String getFormatAdminNumAffilie() {
        return caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_FORMAT_ADMIN_NUM_AFFILIE, "");
    }

    /**
     * Retourne le nombre max. des écritures du journal à imprimer
     * 
     * @return
     */
    public int getLimiteImpressionEcritureJournal() {
        try {
            return Integer.parseInt(caApplication.getProperty(
                    CAApplication.PROPERTY_LIMITE_IMPRESSION_ECRITURES_JOURNAL, "10000"));
        } catch (Exception e) {
            return CAParametres.limiteImpressionEcrituresJournal;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2002 13:40:59)
     * 
     * @return String
     */
    public String getMessage(String applicationId, String messageId, String codeISOLangue) {
        String UNKNOWN = "Unknown message " + messageId;
        String str = UNKNOWN;

        // Récupérer le bundle si inconnu
        if (msg == null) {
            msg = new Properties();
            try {
                java.io.InputStream is = globaz.globall.db.GlobazServer.getCurrentSystem()
                        .getApplication(applicationId).getClass().getResourceAsStream("/osiris.messages");
                msg.load(is);
            } catch (Exception e) {
                System.out.println("Can't read the messages file. Make sure osiris.messages is in the CLASSPATH");
                e.printStackTrace();
                return UNKNOWN;
            }
        }

        // Récupérer le message dans le fichier
        if (msg != null) {
            str = msg.getProperty(messageId + "_" + codeISOLangue.toUpperCase());
            if (str == null) {
                str = UNKNOWN;
            }
        }

        // Fournir le message
        return str;
    }

    /**
     * @return la veleur par defaut du mode de traitement des bulletins neutres
     */
    public String getModeParDefautBulletinNeutre() {
        try {
            String mode = caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_MODE_TRAITEMENT_BULLETIN_NEUTRE, null);
            if (mode == null || JadeStringUtil.isBlankOrZero(mode)) {
                return CACompteAnnexe.CS_BN_INACTIF;
            } else {
                return mode;
            }
        } catch (Exception e) {
            return CACompteAnnexe.CS_BN_INACTIF;
        }
    }

    /**
     * Paramètre indiquant s'il faut renseigner par défaut le critère de recherche "Propriétaire" dans l'apercu des
     * journaux ou pas<br>
     * 
     * @return the apercuJournauxUser
     */
    public boolean isApercuJournauxUser() {
        return Boolean.valueOf(caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_APERCU_JOURNAUX_USER, "false"))
                .booleanValue();
    }

    public boolean isBulletinNeutre() {
        return Boolean.valueOf(caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_BULLETIN_NEUTRE, "false"))
                .booleanValue();
    }

    public boolean isCheckMontantARembourser() {
        return Boolean.valueOf(
                caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_CHECK_MONTANT_A_REMBOURSER, "true"))
                .booleanValue();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 15:13:23)
     * 
     * @return boolean
     */
    public boolean isComptabiliteAvs() {
        return Boolean.valueOf(caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_COMPTABILITE_AVS, "false"))
                .booleanValue();
    }

    /**
     * @return the confidentiel
     */
    public boolean isConfidentiel() {
        return Boolean.valueOf(caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_IMPRESSIONCONFIDENTIEL, "false"))
                .booleanValue();
    }

    /**
     * Cette méthode détermine s'il faut utiliser le contentieux d'Osiris ou le contentieux externe Aquila
     * 
     * @return boolean true si contentieux externe, false si propriété false ou non trouvée
     */
    public boolean isContentieuxAquila() {
        return Boolean.valueOf(caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_CONTENTIEUX_AQUILA, "false"))
                .booleanValue();
    }

    /**
     * @return the contentieuxAvsUniquement
     */
    public boolean isContentieuxAvsUniquement() {
        return Boolean.valueOf(caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_CTX_AVS_UNIQUEMENT, "false"))
                .booleanValue();
    }

    /**
     * Cette méthode indique si lors de la comptabilisation des journaux, l'impression des bulletins de solde doit être
     * effectuée ou non
     * 
     * @return boolean impressionBulletinSoldeApresComptabilisation (defaut = false)
     */
    public boolean isImpressionBulletinSoldeApresComptabilisation() {
        return Boolean.valueOf(
                caApplication.getProperty(CAApplication.PROPERTY_IMPRESSION_BULLETIN_SOLDE_APRES_COMPTABILISATION,
                        "false")).booleanValue();
    }

    /**
     * Cette méthode permet de setter la propriété qui indique le montant minime qui détermine si un bulletin de solde
     * doit être imprimé ou non lors du traitement automatique
     * 
     * @param bulletinSoldeMontantMinime
     */

    public boolean isInteretRemuneratoireActif() {
        return Boolean.valueOf(
                caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_INTERET_REMUNERATOIRE_ACTIF, "true"))
                .booleanValue();
    }

    /**
     * Cette méthode détermine s'il faut mettre les intérêts sur cotisations arriérées sur une nouvelle section au lieu
     * de les mettre dans la section ou ils sont calculés
     * 
     * @return boolean true si intérêts sur section séparée, false si propriété false ou non trouvée
     */
    public boolean isInteretSurCotArrSurSectSeparee() {
        return Boolean.valueOf(
                caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_INTERETSURCOTARRSURSECTIONSEPAREE, "false"))
                .booleanValue();
    }

    /**
     * Cette méthode détermine si la caisse utilise ou pas les rappels sur les sursis au paiement
     * 
     * @return the rappelSurPlan
     */
    public boolean isRappelSurPlan() {
        return Boolean.valueOf(caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_RAPPEL_SUR_PLAN, "false"))
                .booleanValue();
    }

    /**
     * Cette méthode détermine si le numéro de la section de recouvrement doit être incrémenté ou non
     * 
     * @return the incrementerNumSectionRecouvrement
     */
    public boolean isIncrementerNumSectionRecouvrement() {
        return Boolean.valueOf(
                caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_INCREMENTER_NUM_SECTION_RECOUVREMENT, "true"))
                .booleanValue();
    }

    /**
     * @return the requisitionALAdresseDomicile
     */
    public boolean isRequisitionALAdresseDomicile() {
        return Boolean.valueOf(
                caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_REQUISITION_A_ADRESSE_DOMICILE, "false"))
                .booleanValue();
    }

    public boolean isModeParDefautBulletinNeutreisInactif() {
        return getModeParDefautBulletinNeutre().equalsIgnoreCase(CACompteAnnexe.CS_BN_INACTIF);
    }

    public boolean isModeRecalculSoldesSectionsCompteAnnexes() {
        return Boolean.valueOf(caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_RECALCUL_SOLDES, "true"))
                .booleanValue();
    }

    /// Proprieté à utiliser pour la validation de la chaine complete
    /// eBill = ACTIVE
    /// Caisse avec droits d'utiliser eBill
    public boolean isEbill(BSession session){
        // Flag générale eBill (ON/OFF)
        boolean isEbill = getPropertyEbillActive();
        if(isEbill){
            // Vérifier que la caisse avs est dans la liste crypté (Applications->Administration->Plages de valeurs -> OSIRIS + EBILLACNT
            String noCaisse = caApplication.getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE, "");
            List<String> getListCaisses = null;
            try {
                getListCaisses = getListCaissesEBill(session);
            } catch (Exception e) {
                JadeLogger.warn(this, e.getMessage());
            }
            isEbill = getListCaisses.contains(noCaisse);
        }
        return isEbill;
    }

    private List<String> getListCaissesEBill(BSession session) throws Exception {
        List<String> listCaissesEbill = new ArrayList<>();
        FWFindParameterManager mgr = getPlageValeurEbill(session);
        for(int idx = 0; idx < mgr.size(); idx++){
            FWFindParameter param = (FWFindParameter) mgr.get(idx);
            if(!JadeStringUtil.isBlank(param.getValeurAlphaParametre())){
                listCaissesEbill.add(JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(param.getValeurAlphaParametre()));
            }
        }
        return listCaissesEbill;
    }

    private FWFindParameterManager getPlageValeurEbill(BSession session) throws Exception {
        FWFindParameterManager mgr = new FWFindParameterManager();
        mgr.setSession(session);
        mgr.setIdApplParametre(caApplication.getName());
        mgr.setIdCleDiffere(CAApplication.PROPERTY_OSIRIS_PLAGE_VALEURS_EBILL);
        mgr.find(BManager.SIZE_NOLIMIT);
        return mgr;
    }

    public boolean getPropertyEbillActive(){
        return Boolean.valueOf(caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_EBILL_ACTIVE, "false"))
                .booleanValue();
    }

    public String getEbillBillerId(){
        return caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_EBILL_BILLER_ID, "");
    }

    public List<String> getEbillNotificationEmails(){
        List<String> emailList = new ArrayList<>();
        String emails = caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_EBILL_EMAILS, "");
        String sep = ";";
        if(emails.contains(",")){
            sep = ",";
        } else if(emails.contains(":")){
            sep = ":";
        }
        String[] splitEmails = emails.split(sep);
        for(String email : splitEmails){
            emailList.add(email);
        }
        return emailList;
    }

}
