package globaz.osiris.application;

import globaz.globall.db.*;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.webavs.common.CommonProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * CAParametres contient les param�tres sp�cifiques � une instance de l'application OSIRIS
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
     * Retourne le delai en jour pour la date valeur de la table de param�tres (FWPARAP)<br>
     * Si le param�tre n'est pas d�finit, renvoit par d�faut 14.<br>
     * Utilis� dans CAEcriture et CAProcessContentieux
     * 
     * @author: sel Cr�� le : 16 janv. 07
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
     * Si le param�tre n'est pas d�finit, renvoit par d�faut 5.<br>
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
     * Retourne le delai en jour pour le rappel des sursis au paiement non respect�s<br>
     * Si le param�tre n'est pas d�finit, renvoit par d�faut 10.<br>
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
     * Retourne le delai en jour pour la suspension des sursis au paiement non respect�s<br>
     * Si le param�tre n'est pas d�finit, renvoit par d�faut 10.<br>
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
     * @return le montant max tol�r�e de diff�rence entre l'interet calcul� par l'appli et l'interet de l'OP
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
     * Si le param�tre n'est pas d�finit, renvoit par d�faut 2.<br>
     * 
     * @author: sel Cr�� le : 18 janv. 07
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
     * @return la marge tol�r�e de diff�rence entre l'interet calcul� par l'appli et l'interet de l'OP
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (20.12.2001 09:42:12)
     * 
     * @return String
     */
    public String getApplicationExterne() {
        return caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_EXTERNAL_APPLICATION, null);
    }

    /**
     * Cette m�thode permet de r�cup�rer la propri�t� qui indique quelle date doit �tre prise en compte en tant que date
     * de valeur lors du traitement des BVR 0 = Date de traitement (par d�faut) 1 = Date de d�p�t 2 = Date d'inscription
     * 
     * @return int de la valeur de la date � prendre en compte (0 par d�faut)
     */
    public int getDateValeurBVR() {
        return Integer.parseInt(caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_DATE_VALEUR_BVR, "0"));
    }

    /**
     * Repr�sente le format d'un nu�m�ro d'affili�. D�finit dans osiris.properties.
     * 
     * @return le format d'un nu�m�ro d'affili�.
     */
    public String getFormatAdminNumAffilie() {
        return caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_FORMAT_ADMIN_NUM_AFFILIE, "");
    }

    /**
     * Retourne le nombre max. des �critures du journal � imprimer
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.01.2002 13:40:59)
     * 
     * @return String
     */
    public String getMessage(String applicationId, String messageId, String codeISOLangue) {
        String UNKNOWN = "Unknown message " + messageId;
        String str = UNKNOWN;

        // R�cup�rer le bundle si inconnu
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

        // R�cup�rer le message dans le fichier
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
     * Param�tre indiquant s'il faut renseigner par d�faut le crit�re de recherche "Propri�taire" dans l'apercu des
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (30.01.2002 15:13:23)
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
     * Cette m�thode d�termine s'il faut utiliser le contentieux d'Osiris ou le contentieux externe Aquila
     * 
     * @return boolean true si contentieux externe, false si propri�t� false ou non trouv�e
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
     * Cette m�thode indique si lors de la comptabilisation des journaux, l'impression des bulletins de solde doit �tre
     * effectu�e ou non
     * 
     * @return boolean impressionBulletinSoldeApresComptabilisation (defaut = false)
     */
    public boolean isImpressionBulletinSoldeApresComptabilisation() {
        return Boolean.valueOf(
                caApplication.getProperty(CAApplication.PROPERTY_IMPRESSION_BULLETIN_SOLDE_APRES_COMPTABILISATION,
                        "false")).booleanValue();
    }

    /**
     * Cette m�thode permet de setter la propri�t� qui indique le montant minime qui d�termine si un bulletin de solde
     * doit �tre imprim� ou non lors du traitement automatique
     * 
     * @param bulletinSoldeMontantMinime
     */

    public boolean isInteretRemuneratoireActif() {
        return Boolean.valueOf(
                caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_INTERET_REMUNERATOIRE_ACTIF, "true"))
                .booleanValue();
    }

    /**
     * Cette m�thode d�termine s'il faut mettre les int�r�ts sur cotisations arri�r�es sur une nouvelle section au lieu
     * de les mettre dans la section ou ils sont calcul�s
     * 
     * @return boolean true si int�r�ts sur section s�par�e, false si propri�t� false ou non trouv�e
     */
    public boolean isInteretSurCotArrSurSectSeparee() {
        return Boolean.valueOf(
                caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_INTERETSURCOTARRSURSECTIONSEPAREE, "false"))
                .booleanValue();
    }

    /**
     * Cette m�thode d�termine si la caisse utilise ou pas les rappels sur les sursis au paiement
     * 
     * @return the rappelSurPlan
     */
    public boolean isRappelSurPlan() {
        return Boolean.valueOf(caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_RAPPEL_SUR_PLAN, "false"))
                .booleanValue();
    }

    /**
     * Cette m�thode d�termine si le num�ro de la section de recouvrement doit �tre incr�ment� ou non
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

    /// Propriet� � utiliser pour la validation de la chaine complete
    /// eBill = ACTIVE
    /// Caisse avec droits d'utiliser eBill
    public boolean iseBillActifEtDansListeCaisses(BSession session){
        // Flag g�n�rale eBill (ON/OFF)
        boolean eBillActif = iseBillActif();
        if(eBillActif){
            // V�rifier que la caisse avs est dans la liste crypt� (Applications->Administration->Plages de valeurs -> OSIRIS + EBILLACNT
            String noCaisse = caApplication.getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE, "");
            List<String> listeCaisseeBill = null;
            try {
                listeCaisseeBill = getListeCaisseeBill(session);
            } catch (Exception e) {
                JadeLogger.warn(this, e.getMessage());
            }
            eBillActif = listeCaisseeBill.contains(noCaisse);
        }
        return eBillActif;
    }

    private List<String> getListeCaisseeBill(BSession session) throws Exception {
        List<String> listeCaisseeBill = new ArrayList<>();
        FWFindParameterManager mgr = getPlageValeureBill(session);
        for(int idx = 0; idx < mgr.size(); idx++){
            FWFindParameter param = (FWFindParameter) mgr.get(idx);
            if(!JadeStringUtil.isBlank(param.getValeurAlphaParametre())){
                listeCaisseeBill.add(JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(param.getValeurAlphaParametre()));
            }
        }
        return listeCaisseeBill;
    }

    private FWFindParameterManager getPlageValeureBill(BSession session) throws Exception {
        FWFindParameterManager mgr = new FWFindParameterManager();
        mgr.setSession(session);
        mgr.setIdApplParametre(caApplication.getName());
        mgr.setIdCleDiffere(CAApplication.PROPERTY_OSIRIS_PLAGE_VALEURS_EBILL);
        mgr.find(BManager.SIZE_NOLIMIT);
        return mgr;
    }

    public boolean iseBillAquilaActif(){
        return Boolean.valueOf(JadePropertiesService.getInstance().getProperty(CAApplication.PROPERTY_AQUILA_EBILL_ACTIVE));
    }

    public boolean iseBillOsirisActif(){
        return Boolean.valueOf(JadePropertiesService.getInstance().getProperty(CAApplication.PROPERTY_OSIRIS_EBILL_ACTIVE));
    }

    public boolean iseBillActif(){
        return Boolean.valueOf(JadePropertiesService.getInstance().getProperty(CAApplication.PROPERTY_EBILL_ACTIVE));
    }

    public String geteBillBillerId(){
        return caApplication.getProperty(CAApplication.PROPERTY_OSIRIS_EBILL_BILLER_ID, "");
    }

    public List<String> geteBillNotificationEmails(){
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
