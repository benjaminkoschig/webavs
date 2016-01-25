package globaz.orion.utils;

import globaz.commons.nss.NSUtil;
import globaz.draco.application.DSApplication;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.hercule.exception.HerculeException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.translation.CodeSystem;
import globaz.orion.process.EBDanPreRemplissage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.business.models.pucs.PucsSearchCriteria;
import ch.globaz.xmlns.eb.dan.Dan;
import ch.globaz.xmlns.eb.dan.DanStatutEnum;
import ch.globaz.xmlns.eb.dan.SexeEnum;
import ch.globaz.xmlns.eb.pucs.PucsEntrySummary;
import ch.globaz.xmlns.eb.pucs.PucsStatusEnum;
import ch.horizon.jaspe.util.JACalendar;
import ch.horizon.jaspe.util.JACalendarGregorian;
import com.google.gson.Gson;

/**
 * Classe utilitaire pour le pré-remplissage de la DAN
 * 
 * @author SCO
 * @since 01 avr. 2011
 */
public class EBDanUtils {

    public static final String LAA = "509033";
    public static final String LPP = "509012";

    public static final String PUCSDATA_ANNEE = "PUCSDATA_ANNEE";

    public static final String PUCSDATA_DATE = "PUCSDATA_DATE";
    public static final String PUCSDATA_ID = "PUCSDATA_ID";
    public static final String PUCSDATA_NB_SALAIRE = "PUCSDATA_NB_SALAIRE";

    public static final String PUCSDATA_NUM_AFFILIE = "PUCSDATA_NUM_AFFILIE";
    public static final String PUCSDATA_PROVENANCE = "PUCSDATA_PROVENANCE";
    public static final String SALAIRE_INFERIEUR_LIMITE = "SALAIRE_INFERIEUR_LIMITE";
    public static final String NOM = "NOM";
    public static final String TOTAL = "TOTAL";

    /**
     * Calcul des bornes
     * 
     * @param affManager
     */
    public static Map<String, String> calculBorneDate(String annee) {

        Map<String, String> retour = null;

        JADate datedebut = new JADate();
        JADate datefin = new JADate();
        int anneeDeclaration = 0;

        if (!JadeStringUtil.isBlankOrZero(annee)) {
            retour = new HashMap<String, String>();
            anneeDeclaration = new Integer(annee.trim()).intValue() - 1;

            datedebut.setYear(anneeDeclaration + 2);
            datedebut.setDay(01);
            datedebut.setMonth(01);
            retour.put("dateDebut", datedebut.toString());

            datefin.setYear(anneeDeclaration + 1);
            datefin.setDay(31);
            datefin.setMonth(12);
            retour.put("dateFin", datefin.toString());
        }

        return retour;
    }

    /**
     * Permet de récupérer le numéro NSS formaté
     * 
     * @param session
     * @param avsOrNss
     * @return
     */
    public static String checkAnReturnNSS(BSession session, String avsOrNss) {
        String num = null;

        if (avsOrNss != null) {

            num = NSUtil.unFormatAVS(avsOrNss);

            if (num.length() != 13) {
                num = NSUtil.returnNNSS(session, num);
            }

            num = NSUtil.formatAVSUnknown(num);
        }

        return num;
    }

    private static String converStatus(PucsStatusEnum status) {
        if (PucsStatusEnum.HANDLED == status) {
            return PucsSearchCriteria.CS_HANDLED;
        } else if (PucsStatusEnum.HANDLING == status) {
            return PucsSearchCriteria.CS_HANDLING;
        } else if (PucsStatusEnum.REJECTED == status) {
            return PucsSearchCriteria.CS_REJECTED;
        } else if (PucsStatusEnum.TO_HANDLE == status) {
            return PucsSearchCriteria.CS_TO_HANDLE;
        } else if (PucsStatusEnum.UPLOADED == status) {
            return PucsSearchCriteria.CS_UPLOADED;
        } else {
            return "";
        }
    }

    private static String converStatusDan(DanStatutEnum status) {
        if (DanStatutEnum.VALIDEE.equals(status)) {
            return PucsSearchCriteria.CS_TO_HANDLE;
        } else if (DanStatutEnum.EN_TRAITEMENT.equals(status)) {
            return PucsSearchCriteria.CS_HANDLING;
        } else {
            return "";
        }
    }

    public static String createPucsDataForProcess(PucsFile pucsFile) {

        try {
            return new Gson().toJson(pucsFile);// JavaObjectSerializer.convertObjectToHexa(pucsFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }// )new Gson().toJson(pucs);

        // return //new Gson().toJson(pucsFile);
    }

    /**
     * Recherche d'un affilié par son numéro d'affilié.<br>
     * Retourne la premiere occurence trouvée.
     * 
     * @param session
     * @param affilieNumero
     * @return AFAffiliation
     * @throws Exception
     */
    public static AFAffiliation findAffilie(BSession session, String affilieNumero, String dateDebut, String dateFin)
            throws Exception {

        if (session == null) {
            throw new HerculeException("Unabled to retrieve AFAffiliation, session is null");
        }

        if (JadeStringUtil.isBlankOrZero(affilieNumero)) {
            throw new HerculeException("Unabled to retrieve AFAffiliation, affilieNumero is null or empty");
        }

        AFAffiliationManager manager = new AFAffiliationManager();
        manager.setSession(session);
        manager.setForAffilieNumero(affilieNumero);
        manager.setFromDateFin(dateFin);
        manager.setForDateDebutAffLowerOrEqualTo(dateDebut);
        manager.setForTypesAffParitaires();

        try {
            manager.find();
        } catch (Exception e) {
            throw new Exception("Technical exception, Unabled to find AFAffiliation (affilieNumero : " + affilieNumero
                    + ")", e);
        }

        if (manager.size() > 0) {
            return (AFAffiliation) manager.getFirstEntity();
        }

        return null;
    }

    /**
     * Permet de retirer une année a l'année passé en parametre<br>
     * 
     * @param _annee
     *            L'année de référence
     * @return l'année - 1 ou null si non renseigné.
     */
    public static String getAnneePrecedente(String _annee) {

        if (JadeStringUtil.isEmpty(_annee)) {
            return null;
        }

        int annee = Integer.parseInt(_annee);
        annee -= 1;

        return Integer.toString(annee);
    }

    public static String getCanton(BSession session, String idAffiliation, String date) throws Exception {

        // pré-conditions
        if ((idAffiliation == null) || (session == null) || JadeStringUtil.isEmpty(date)) {
            return null;
        }

        String codeCanton = "";

        // recherche d'une cotisation AF
        AFCotisationManager cotisMgr = new AFCotisationManager();
        cotisMgr.setForDate(date);
        cotisMgr.setForAffiliationId(idAffiliation);
        cotisMgr.setSession(session);
        cotisMgr.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AF);

        try {
            cotisMgr.find();

        } catch (Exception e) {
            throw new Exception("Technical exception during the search of the canton : (idAffiliation : "
                    + idAffiliation + ")", e);
        }

        if (cotisMgr.size() > 0) {
            // une cotisation AF existe
            codeCanton = ((AFCotisation) cotisMgr.getFirstEntity()).getAssurance().getAssuranceCanton();
        }

        return codeCanton;
    }

    /**
     * Permet de décomposer la chaine de caractere qui contient toutes les infos d'un fichier pucs
     * 
     * @param pucsData
     *            La chaine de caractere qui contient les infos d'un fichier pucs
     * @return
     */
    public static PucsFile getDataFromPucsData(String json) {

        // PucsFile pucsFile = new Gson().fromJson(json, PucsFile.class);
        return new Gson().fromJson(json, PucsFile.class);
        // try {
        // return (PucsFile) JavaObjectSerializer.convertHexaToObject(json);
        // } catch (Exception e) {
        // throw new RuntimeException(e);
        // }
    }

    /**
     * Permet de savoir si une personne est dans son année de retraite
     * 
     * @param str_annee
     * @param sexe
     * @param dateNaissance
     * @return
     * @throws JAException
     */
    public static boolean isAnneeRetraite(String str_annee, String sexe, String dateNaissance) throws JAException {
        int ageRetraite;
        int annee = Integer.parseInt(str_annee);
        int anneeNaissance = EBDanUtils.stringDateToAnnee(dateNaissance);

        if (!EBDanPreRemplissage.SEXE_HOMME_AVS.equals(sexe)) {
            // femme
            if (annee <= 2000) {
                ageRetraite = 62;
            } else if (annee < 2005) {
                ageRetraite = 63;
            } else {
                ageRetraite = 64;
            }
        } else {
            // homme
            ageRetraite = 65;
        }
        if ((anneeNaissance + ageRetraite) == annee) {
            return true;
        }
        return false;
    }

    /**
     * Permet de récupérer le dernier jour du mois donné
     * 
     * @param date
     * @return
     * @throws ch.horizon.jaspe.model.JAException
     */
    public static int lastDayInMonth(String date) throws ch.horizon.jaspe.model.JAException {
        JACalendarGregorian cale = new JACalendarGregorian();
        int day = JACalendar.getDay(cale.lastInMonth(date));

        return day;
    }

    public static PucsFile mapDanfile(Dan pucsTemp) {
        PucsFile pucsRetour = new PucsFile();

        pucsRetour.setAnneeDeclaration(String.valueOf(pucsTemp.getAnnee()));
        pucsRetour.setCurrentStatus(EBDanUtils.converStatusDan(pucsTemp.getStatut()));
        String dateFromDan = String.valueOf(pucsTemp.getDate());
        JADate date = new JADate();
        date.setYear(Integer.parseInt(dateFromDan.substring(0, 4)));
        date.setMonth(Integer.parseInt(dateFromDan.substring(4, 6)));
        date.setDay(Integer.parseInt(dateFromDan.substring(6)));

        pucsRetour.setDateDeReception(globaz.globall.util.JACalendar.format(date, JACalendar.FORMAT_DDsMMsYYYY));
        pucsRetour.setHandlingUser(pucsTemp.getHandLingUser());
        pucsRetour.setId(String.valueOf(pucsTemp.getIdDan())); //
        pucsRetour.setNbSalaires(String.valueOf(pucsTemp.getNombreSalaire()));
        pucsRetour.setNomAffilie(pucsTemp.getAffilie().getNom());
        pucsRetour.setNumeroAffilie(pucsTemp.getAffilie().getNumeroAffilie());
        pucsRetour.setProvenance(DeclarationSalaireProvenance.DAN);
        pucsRetour.setSalaireInferieurLimite(pucsTemp.isSalairesInferieusLimite());
        FWCurrency montantAvs;
        try {
            montantAvs = new FWCurrency(pucsTemp.getMontantControle().doubleValue());
        } catch (Exception e) {
            montantAvs = new FWCurrency("0");
        }
        pucsRetour.setTotalControle(montantAvs.toStringFormat());
        pucsRetour.setNbSalaires(String.valueOf(pucsTemp.getNombreSalaire()));

        return pucsRetour;
    }

    public static PucsFile mapPucsfile(PucsEntrySummary pucsTemp) {
        PucsFile pucsRetour = new PucsFile();
        pucsRetour.setAnneeDeclaration(String.valueOf(pucsTemp.getAnnee()));
        pucsRetour.setCurrentStatus(EBDanUtils.converStatus(pucsTemp.getStatusActuelLabel()));
        pucsRetour.setDateDeReception(pucsTemp.getSoumissionLabel());
        pucsRetour.setHandlingUser(pucsTemp.getHandlingUser());
        pucsRetour.setId(String.valueOf(pucsTemp.getIdPucsEntry()));
        pucsRetour.setNomAffilie(pucsTemp.getNomAffilie());
        pucsRetour.setNumeroAffilie(pucsTemp.getNoAffilie());
        pucsRetour.setProvenance(DeclarationSalaireProvenance.PUCS);
        // Utilisé uniquement pour la DAN
        pucsRetour.setSalaireInferieurLimite(new Boolean(false));
        FWCurrency montantAvs;
        try {
            montantAvs = new FWCurrency(pucsTemp.getDefinedAVSAmount().doubleValue());
        } catch (Exception e) {
            montantAvs = new FWCurrency("0");
        }
        pucsRetour.setTotalControle(montantAvs.toStringFormat());
        pucsRetour.setNbSalaires(String.valueOf(pucsTemp.getDefinedNbOfSalaries()));

        return pucsRetour;
    }

    public static String retrieveCodeCantonOFASForAff(AFAffiliation aff, String date) throws Exception {
        String codeCanton = AFAffiliationUtil.getCantonAFCSForDS(aff, date);

        if (JadeStringUtil.isEmpty(codeCanton)) {
            // si pas trouvé, utiliser la valeur par défaut
            DSApplication application = (DSApplication) globaz.globall.db.GlobazServer.getCurrentSystem()
                    .getApplication(DSApplication.DEFAULT_APPLICATION_DRACO);
            codeCanton = application.getCodeCantonAF();
        }

        if ("505001".equals(codeCanton)) {
            return "001"; // CS_ZURICH
        } else if ("505002".equals(codeCanton)) {
            return "002";// CS_BERNE
        } else if ("505003".equals(codeCanton)) {
            return "003";// CS_LUCERNE
        } else if ("505004".equals(codeCanton)) {
            return "004";// CS_URI
        } else if ("505005".equals(codeCanton)) {
            return "005";// CS_SCHWYZ
        } else if ("505006".equals(codeCanton)) {
            return "006";// CS_OBWALD
        } else if ("505007".equals(codeCanton)) {
            return "007";// CS_NIDWALD
        } else if ("505008".equals(codeCanton)) {
            return "008";// CS_GLARIS
        } else if ("505009".equals(codeCanton)) {
            return "009";// CS_ZOUG
        } else if ("505010".equals(codeCanton)) {
            return "010";// CS_FRIBOURG
        } else if ("505011".equals(codeCanton)) {
            return "011";// CS_SOLEURE
        } else if ("505012".equals(codeCanton)) {
            return "012";// CS_BALE_VILLE
        } else if ("505013".equals(codeCanton)) {
            return "013";// CS_BALE_CAMPAGNE
        } else if ("505014".equals(codeCanton)) {
            return "014";// CS_SCHAFFOUSE
        } else if ("505015".equals(codeCanton)) {
            return "015";// CS_APPENZELL_AR
        } else if ("505016".equals(codeCanton)) {
            return "016";// CS_APPENZELL_AI
        } else if ("505017".equals(codeCanton)) {
            return "017";// CS_SAINT_GALL
        } else if ("505018".equals(codeCanton)) {
            return "018";// CS_GRISONS
        } else if ("505019".equals(codeCanton)) {
            return "019";// CS_ARGOVIE
        } else if ("505020".equals(codeCanton)) {
            return "020";// CS_THURGOVIE
        } else if ("505021".equals(codeCanton)) {
            return "021";// CS_TESSIN
        } else if ("505022".equals(codeCanton)) {
            return "022";// CS_VAUD
        } else if ("505023".equals(codeCanton)) {
            return "023";// CS_VALAIS
        } else if ("505024".equals(codeCanton)) {
            return "024";// CS_NEUCHATEL
        } else if ("505025".equals(codeCanton)) {
            return "025";// CS_GENEVE
        } else if ("505026".equals(codeCanton)) {
            return "150";// CS_JURA
        }

        return "";
    }

    /**
     * Permet de convertir le code sexe AVS dans le code sexe ebusiness
     * 
     * @param sexe
     * @return
     */
    public static SexeEnum returnCodeSexeEBusiness(String sexe) {
        if (EBDanPreRemplissage.SEXE_HOMME_AVS.equals(sexe)) {
            return SexeEnum.HOMME;
        } else {
            return SexeEnum.FEMME;
        }
    }

    /**
     * Convertion d'un "e.printStackTrace" en String
     * 
     * @param e
     * @return
     */
    public static String stack2string(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return "------\r\n" + sw.toString() + "------\r\n";
        } catch (Exception e2) {
            return "bad stack2string";
        }
    }

    public static int stringDateToAnnee(String _date) throws JAException {
        JADate date = new JADate(_date);
        return date.getYear();
    }

}
