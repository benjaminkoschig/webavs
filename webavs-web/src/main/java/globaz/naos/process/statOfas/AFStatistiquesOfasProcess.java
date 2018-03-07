/*
 * Créé le 14 févr. 07
 */
package globaz.naos.process.statOfas;

import globaz.aquila.api.ICOEtape;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.db.tauxAssurance.AFTauxAssuranceManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TIPersonne;
import globaz.pyxis.util.TIToolBox;
import globaz.webavs.common.CommonProperties;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.naos.business.beans.StatOfasAFAgricoleBean;

/**
 * @author mar
 * 
 */

public class AFStatistiquesOfasProcess extends BProcess {

    public static final String CODE_CANTON_ETRANGER = "505027";
    public static final String CODE_PAYS_SUISSE = "100";

    public static final String CSV_STATISTIQUE_CONTROLE_LAA_LPP_OUTPUT_FILE_NAME = "StatistiqueControleLAALPP.csv";
    public static final String CSV_STATISTIQUE_OFAS_OUTPUT_FILE_NAME = "StatistiqueOfas.csv";
    public static final String KEY_016_MAP_STAT_OFAS = "016";
    public static final String KEY_018_MAP_STAT_OFAS = "018";
    public static final String KEY_031_MAP_STAT_OFAS = "031";

    public static final String KEY_032_MAP_STAT_OFAS = "032";
    public static final String KEY_033_MAP_STAT_OFAS = "033";
    public static final String KEY_035_MAP_STAT_OFAS = "035";
    public static final String KEY_036_MAP_STAT_OFAS = "036";
    public static final String KEY_037_MAP_STAT_OFAS = "037";
    public static final String KEY_038_MAP_STAT_OFAS = "038";
    public static final String KEY_039_MAP_STAT_OFAS = "039";
    public static final String KEY_044_MAP_STAT_OFAS = "044";
    public static final String KEY_045_MAP_STAT_OFAS = "045";
    public static final String KEY_055_MAP_STAT_OFAS = "055";
    public static final String KEY_056_MAP_STAT_OFAS = "056";
    public static final String KEY_057_MAP_STAT_OFAS = "057";
    public static final String KEY_058_MAP_STAT_OFAS = "058";
    public static final String KEY_061_MAP_STAT_OFAS = "061";
    public static final String KEY_062_MAP_STAT_OFAS = "062";
    public static final String KEY_063_MAP_STAT_OFAS = "063";
    public static final String KEY_066_MAP_STAT_OFAS = "066";
    public static final String KEY_067_MAP_STAT_OFAS = "067";
    public static final String KEY_068_MAP_STAT_OFAS = "068";
    public static final String KEY_069_MAP_STAT_OFAS = "069";
    public static final String KEY_070_MAP_STAT_OFAS = "070";
    public static final String KEY_071_MAP_STAT_OFAS = "071";
    public static final String KEY_075_MAP_STAT_OFAS = "075";
    public static final String KEY_082_MAP_STAT_OFAS = "082";
    public static final String KEY_083_MAP_STAT_OFAS = "083";
    public static final String KEY_084_MAP_STAT_OFAS = "084";
    public static final String KEY_085_MAP_STAT_OFAS = "085";
    public static final String KEY_086_MAP_STAT_OFAS = "086";
    public static final String KEY_087_MAP_STAT_OFAS = "087";
    public static final String KEY_088_MAP_STAT_OFAS = "088";
    public static final String KEY_090_MAP_STAT_OFAS = "090";
    public static final String KEY_092_MAP_STAT_OFAS = "092";
    public static final String KEY_093_MAP_STAT_OFAS = "093";
    public static final String KEY_094_MAP_STAT_OFAS = "094";
    public static final String KEY_096_MAP_STAT_OFAS = "096";
    public static final String KEY_102_MAP_STAT_OFAS = "102";
    public static final String KEY_103_MAP_STAT_OFAS = "103";
    public static final String KEY_104_MAP_STAT_OFAS = "104";
    public static final String KEY_105_MAP_STAT_OFAS = "105";
    public static final String KEY_106_MAP_STAT_OFAS = "106";
    public static final String KEY_107_MAP_STAT_OFAS = "107";
    public static final String KEY_108_MAP_STAT_OFAS = "108";
    public static final String KEY_109_MAP_STAT_OFAS = "109";
    public static final String KEY_110_MAP_STAT_OFAS = "110";

    public static final String KEY_111_MAP_STAT_OFAS = "111";
    public static final String KEY_112_MAP_STAT_OFAS = "112";
    public static final String KEY_113_MAP_STAT_OFAS = "113";

    public static final String KEY_114_MAP_STAT_OFAS = "114";
    public static final String KEY_115_MAP_STAT_OFAS = "115";
    public static final String KEY_116_MAP_STAT_OFAS = "116";
    public static final String KEY_117_MAP_STAT_OFAS = "117";
    public static final String KEY_118_MAP_STAT_OFAS = "118";
    public static final String KEY_119_MAP_STAT_OFAS = "119";
    public static final String KEY_120_MAP_STAT_OFAS = "120";
    public static final String KEY_121_MAP_STAT_OFAS = "121";
    public static final String KEY_122_MAP_STAT_OFAS = "122";
    public static final String KEY_123_MAP_STAT_OFAS = "123";
    public static final String KEY_124_MAP_STAT_OFAS = "124";
    public static final String KEY_125_MAP_STAT_OFAS = "125";
    public static final String KEY_126_MAP_STAT_OFAS = "126";
    public static final String KEY_127_MAP_STAT_OFAS = "127";
    public static final String KEY_128_MAP_STAT_OFAS = "128";
    public static final String KEY_140_MAP_STAT_OFAS = "140";
    public static final String KEY_182_MAP_STAT_OFAS = "182";
    public static final String KEY_184_MAP_STAT_OFAS = "184";
    public static final String KEY_186_MAP_STAT_OFAS = "186";
    public static final String KEY_187_MAP_STAT_OFAS = "187";
    public static final String KEY_192_MAP_STAT_OFAS = "192";
    public static final String KEY_193_MAP_STAT_OFAS = "193";
    public static final String KEY_306_MAP_STAT_OFAS = "306";
    public static final String KEY_307_MAP_STAT_OFAS = "307";
    public static final String KEY_308_MAP_STAT_OFAS = "308";
    public static final String KEY_309_MAP_STAT_OFAS = "309";
    public static final String KEY_310_MAP_STAT_OFAS = "310";
    public static final String KEY_321_MAP_STAT_OFAS = "321";
    public static final String KEY_322_MAP_STAT_OFAS = "322";
    public static final String KEY_601_MAP_STAT_OFAS = "601";
    public static final String KEY_602_MAP_STAT_OFAS = "602";
    public static final String KEY_603_MAP_STAT_OFAS = "603";
    public static final String KEY_604_MAP_STAT_OFAS = "604";
    public static final String KEY_605_MAP_STAT_OFAS = "605";
    public static final String KEY_606_MAP_STAT_OFAS = "606";
    public static final String KEY_607_MAP_STAT_OFAS = "607";
    public static final String KEY_608_MAP_STAT_OFAS = "608";
    public static final String KEY_609_MAP_STAT_OFAS = "609";
    public static final String KEY_610_MAP_STAT_OFAS = "610";
    public static final String KEY_611_MAP_STAT_OFAS = "611";
    public static final String KEY_612_MAP_STAT_OFAS = "612";
    public static final String KEY_613_MAP_STAT_OFAS = "613";
    public static final String KEY_614_MAP_STAT_OFAS = "614";
    public static final String KEY_615_MAP_STAT_OFAS = "615";
    public static final String KEY_616_MAP_STAT_OFAS = "616";
    public static final String KEY_617_MAP_STAT_OFAS = "617";
    public static final String KEY_618_MAP_STAT_OFAS = "618";
    public static final String KEY_619_MAP_STAT_OFAS = "619";
    public static final String KEY_620_MAP_STAT_OFAS = "620";
    public static final String KEY_621_MAP_STAT_OFAS = "621";
    public static final String KEY_622_MAP_STAT_OFAS = "622";
    public static final String KEY_623_MAP_STAT_OFAS = "623";
    public static final String KEY_624_MAP_STAT_OFAS = "624";
    public static final String KEY_625_MAP_STAT_OFAS = "625";
    public static final String KEY_626_MAP_STAT_OFAS = "626";
    public static final String KEY_627_MAP_STAT_OFAS = "627";
    public static final String KEY_628_MAP_STAT_OFAS = "628";
    public static final String KEY_629_MAP_STAT_OFAS = "629";
    public static final String KEY_630_MAP_STAT_OFAS = "630";
    public static final String KEY_631_MAP_STAT_OFAS = "631";

    public static final String KEY_632_MAP_STAT_OFAS = "632";
    public static final String KEY_633_MAP_STAT_OFAS = "633";
    public static final String KEY_634_MAP_STAT_OFAS = "634";
    public static final String KEY_635_MAP_STAT_OFAS = "635";
    public static final String KEY_636_MAP_STAT_OFAS = "636";
    public static final String KEY_637_MAP_STAT_OFAS = "637";
    public static final String KEY_638_MAP_STAT_OFAS = "638";
    public static final String KEY_639_MAP_STAT_OFAS = "639";
    public static final String KEY_640_MAP_STAT_OFAS = "640";
    public static final String KEY_641_MAP_STAT_OFAS = "641";
    public static final String KEY_642_MAP_STAT_OFAS = "642";
    public static final String KEY_643_MAP_STAT_OFAS = "643";
    public static final String KEY_644_MAP_STAT_OFAS = "644";
    public static final String KEY_645_MAP_STAT_OFAS = "645";
    public static final String KEY_646_MAP_STAT_OFAS = "646";
    public static final String KEY_647_MAP_STAT_OFAS = "647";
    public static final String KEY_648_MAP_STAT_OFAS = "648";
    public static final String KEY_649_MAP_STAT_OFAS = "649";
    public static final String KEY_650_MAP_STAT_OFAS = "650";
    public static final String KEY_651_MAP_STAT_OFAS = "651";
    public static final String KEY_652_MAP_STAT_OFAS = "652";
    public static final String KEY_653_MAP_STAT_OFAS = "653";
    public static final String KEY_654_MAP_STAT_OFAS = "654";
    public static final String KEY_655_MAP_STAT_OFAS = "655";
    public static final String KEY_656_MAP_STAT_OFAS = "656";
    public static final String KEY_657_MAP_STAT_OFAS = "657";
    public static final String KEY_658_MAP_STAT_OFAS = "658";
    public static final String KEY_659_MAP_STAT_OFAS = "659";
    public static final String KEY_660_MAP_STAT_OFAS = "660";
    public static final String KEY_661_MAP_STAT_OFAS = "661";
    public static final String KEY_662_MAP_STAT_OFAS = "662";
    public static final String KEY_663_MAP_STAT_OFAS = "663";
    public static final String KEY_664_MAP_STAT_OFAS = "664";
    public static final String KEY_665_MAP_STAT_OFAS = "665";
    public static final String KEY_666_MAP_STAT_OFAS = "666";
    public static final String KEY_667_MAP_STAT_OFAS = "667";
    public static final String KEY_668_MAP_STAT_OFAS = "668";
    public static final String KEY_669_MAP_STAT_OFAS = "669";
    public static final String KEY_670_MAP_STAT_OFAS = "670";
    public static final String KEY_671_MAP_STAT_OFAS = "671";
    public static final String KEY_672_MAP_STAT_OFAS = "672";
    public static final String KEY_673_MAP_STAT_OFAS = "673";
    public static final String KEY_674_MAP_STAT_OFAS = "674";
    public static final String KEY_675_MAP_STAT_OFAS = "675";
    public static final String KEY_676_MAP_STAT_OFAS = "676";
    public static final String KEY_677_MAP_STAT_OFAS = "677";
    public static final String KEY_678_MAP_STAT_OFAS = "678";
    public static final String KEY_683_MAP_STAT_OFAS = "683";
    public static final String KEY_684_MAP_STAT_OFAS = "684";
    public static final String KEY_685_MAP_STAT_OFAS = "685";
    public static final String KEY_686_MAP_STAT_OFAS = "686";
    public static final String KEY_687_MAP_STAT_OFAS = "687";
    public static final String KEY_688_MAP_STAT_OFAS = "688";
    public static final String KEY_689_MAP_STAT_OFAS = "689";

    public static final String KEY_690_MAP_STAT_OFAS = "690";
    public static final String KEY_691_MAP_STAT_OFAS = "691";
    public static final String KEY_692_MAP_STAT_OFAS = "692";
    public static final String KEY_693_MAP_STAT_OFAS = "693";
    public static final String KEY_694_MAP_STAT_OFAS = "694";
    public static final String KEY_695_MAP_STAT_OFAS = "695";

    public static final String KEY_696_MAP_STAT_OFAS = "696";
    public static final String KEY_701_MAP_STAT_OFAS = "701";
    public static final String KEY_703_MAP_STAT_OFAS = "703";
    public static final String KEY_704_MAP_STAT_OFAS = "704";
    public static final String KEY_705_MAP_STAT_OFAS = "705";
    public static final String KEY_707_MAP_STAT_OFAS = "707";
    public static final String KEY_708_MAP_STAT_OFAS = "708";
    public static final String KEY_709_MAP_STAT_OFAS = "709";
    public static final String KEY_710_MAP_STAT_OFAS = "710";
    public static final String KEY_711_MAP_STAT_OFAS = "711";
    public static final String KEY_712_MAP_STAT_OFAS = "712";
    public static final String KEY_715_MAP_STAT_OFAS = "715";
    public static final String KEY_716_MAP_STAT_OFAS = "716";
    public static final String KEY_726_MAP_STAT_OFAS = "726";
    public static final String KEY_730_MAP_STAT_OFAS = "730";
    public static final String KEY_731_MAP_STAT_OFAS = "731";
    public static final String KEY_732_MAP_STAT_OFAS = "732";
    public static final String KEY_733_MAP_STAT_OFAS = "733";
    public static final String KEY_734_MAP_STAT_OFAS = "734";
    public static final String KEY_735_MAP_STAT_OFAS = "735";
    public static final String KEY_736_MAP_STAT_OFAS = "736";
    public static final String KEY_737_MAP_STAT_OFAS = "737";
    public static final String KEY_738_MAP_STAT_OFAS = "738";
    public static final String KEY_739_MAP_STAT_OFAS = "739";
    public static final String KEY_740_MAP_STAT_OFAS = "740";
    public static final String KEY_741_MAP_STAT_OFAS = "741";
    public static final String KEY_742_MAP_STAT_OFAS = "742";
    public static final String KEY_743_MAP_STAT_OFAS = "743";
    public static final String KEY_744_MAP_STAT_OFAS = "744";
    public static final String KEY_745_MAP_STAT_OFAS = "745";

    public static final String KEY_746_MAP_STAT_OFAS = "746";

    public static final String KEY_747_MAP_STAT_OFAS = "747";

    public static final String KEY_748_MAP_STAT_OFAS = "748";

    public static final String KEY_749_MAP_STAT_OFAS = "749";
    public static final String KEY_750_MAP_STAT_OFAS = "750";
    public static final String KEY_751_MAP_STAT_OFAS = "751";
    public static final String KEY_752_MAP_STAT_OFAS = "752";
    public static final String KEY_753_MAP_STAT_OFAS = "753";
    public static final String KEY_754_MAP_STAT_OFAS = "754";
    public static final String KEY_755_MAP_STAT_OFAS = "755";
    public static final String KEY_756_MAP_STAT_OFAS = "756";

    public static final String KEY_757_MAP_STAT_OFAS = "757";
    public static final String KEY_758_MAP_STAT_OFAS = "758";

    public static final String KEY_759_MAP_STAT_OFAS = "759";

    public static final String KEY_760_MAP_STAT_OFAS = "760";

    public static final String KEY_761_MAP_STAT_OFAS = "761";

    public static final String KEY_762_MAP_STAT_OFAS = "762";
    public static final String KEY_763_MAP_STAT_OFAS = "763";
    public static final String KEY_764_MAP_STAT_OFAS = "764";
    public static final String KEY_765_MAP_STAT_OFAS = "765";
    public static final String KEY_766_MAP_STAT_OFAS = "766";

    public static final String KEY_767_MAP_STAT_OFAS = "767";

    public static final String KEY_768_MAP_STAT_OFAS = "768";

    public static final String KEY_769_MAP_STAT_OFAS = "769";
    public static final String KEY_770_MAP_STAT_OFAS = "770";

    public static final String KEY_771_MAP_STAT_OFAS = "771";
    public static final String KEY_772_MAP_STAT_OFAS = "772";
    public static final String KEY_773_MAP_STAT_OFAS = "773";
    public static final String KEY_774_MAP_STAT_OFAS = "774";
    public static final String KEY_775_MAP_STAT_OFAS = "775";
    public static final String KEY_776_MAP_STAT_OFAS = "776";
    public static final String KEY_777_MAP_STAT_OFAS = "777";
    public static final String KEY_778_MAP_STAT_OFAS = "778";
    public static final String KEY_779_MAP_STAT_OFAS = "779";
    public static final String KEY_780_MAP_STAT_OFAS = "780";
    public static final String KEY_781_MAP_STAT_OFAS = "781";
    public static final String KEY_782_MAP_STAT_OFAS = "782";
    public static final String KEY_783_MAP_STAT_OFAS = "783";
    public static final String KEY_784_MAP_STAT_OFAS = "784";
    public static final String KEY_785_MAP_STAT_OFAS = "785";
    public static final String KEY_786_MAP_STAT_OFAS = "786";
    public static final String KEY_787_MAP_STAT_OFAS = "787";
    public static final String KEY_788_MAP_STAT_OFAS = "788";
    public static final String KEY_789_MAP_STAT_OFAS = "789";
    public static final String KEY_790_MAP_STAT_OFAS = "790";
    public static final String KEY_791_MAP_STAT_OFAS = "791";
    public static final String KEY_792_MAP_STAT_OFAS = "792";
    public static final String KEY_793_MAP_STAT_OFAS = "793";
    public static final String KEY_794_MAP_STAT_OFAS = "794";
    public static final String KEY_795_MAP_STAT_OFAS = "795";
    public static final String KEY_796_MAP_STAT_OFAS = "796";
    public static final String KEY_797_MAP_STAT_OFAS = "797";
    public static final String KEY_798_MAP_STAT_OFAS = "798";
    public static final String KEY_799_MAP_STAT_OFAS = "799";
    public static final String KEY_AGRICULTEUR_ACCESSOIRE_MONTAGNE = "AGRICULTEUR_ACCESSOIRE_MONTAGNE";
    public static final String KEY_AGRICULTEUR_ACCESSOIRE_PLAINE = "AGRICULTEUR_ACCESSOIRE_PLAINE";
    public static final String KEY_AGRICULTEUR_PRINCIPAL_MONTAGNE = "AGRICULTEUR_PRINCIPAL_MONTAGNE";
    public static final String KEY_AGRICULTEUR_PRINCIPAL_PLAINE = "AGRICULTEUR_PRINCIPAL_PLAINE";
    public static final String KEY_EXPLOITANT_ALPAGE = "EXPLOITANT_ALPAGE";
    public static final String KEY_NOMBRE_CONTROLE_EMPLOYEUR_EFFECTUE_MAP_STAT_CONTROLE_LAA_LPP = "NOMBRE_CONTROLE_EMPLOYEUR_EFFECTUE";
    public static final String KEY_NOMBRE_CREANCE_REPARATION_DOMMAGE_MAP_INFO_HORS_STAT_OFAS = "NOMBRE_CREANCE_REPARATION_DOMMAGE";
    public static final String KEY_NOMBRE_DECLARATION_SALAIRE_RETOURNE_MAP_STAT_CONTROLE_LAA_LPP = "NOMBRE_DECLARATION_SALAIRE_RETOURNE";
    public static final String KEY_NOMBRE_QUESTIONNAIRE_LAA_RETOURNE_MAP_STAT_CONTROLE_LAA_LPP = "NOMBRE_QUESTIONNAIRE_LAA_RETOURNE";
    public static final String KEY_NOMBRE_QUESTIONNAIRE_LPP_RETOURNE_MAP_STAT_CONTROLE_LAA_LPP = "NOMBRE_QUESTIONNAIRE_LPP_RETOURNE";
    public static final String KEY_PECHEUR = "PECHEUR";
    public static final String KEY_TOTAL_CREANCE_REPARATION_DOMMAGE_MAP_INFO_HORS_STAT_OFAS = "TOTAL_CREANCE_REPARATION_DOMMAGE";
    public static final String KEY_TOTAL_REQUISITION_CONTINUER_POURSUITE_MAP_INFO_HORS_STAT_OFAS = "TOTAL_REQUISITION_CONTINUER_POURSUITE";
    public static final String KEY_TOTAL_REQUISITION_POURSUITE_MAP_INFO_HORS_STAT_OFAS = "TOTAL_REQUISITION_POURSUITE";
    public static final String KEY_TOTAL_SURSIS_PAIEMENT_MAP_INFO_HORS_STAT_OFAS = "TOTAL_SURSIS_PAIEMENT";
    public static final String KEY_TRAVAILLEUR_AGRICOLE_MONTAGNE = "TRAVAILLEUR_AGRICOLE_MONTAGNE";
    public static final String KEY_TRAVAILLEUR_AGRICOLE_PLAINE = "TRAVAILLEUR_AGRICOLE_PLAINE";
    public static final String LABEL_STATISTIQUE_OFAS_FIX_PART = "STATISTIQUE_OFAS_";

    public static final String MODE_COMPTER_AFFILIE = "MODE_COMPTER_AFFILIE";
    public static final String MODE_SOMMER_COTISATION = "MODE_SOMMER_COTISATION";
    public static final String REQUETE_AF_AGRICOLE_COL_NAME_ACTIVITE_ACCESSOIRE = "ACTIVITE_ACCESSOIRE";

    public static final String REQUETE_AF_AGRICOLE_COL_NAME_GENRE_ALLOCATION = "GENRE_ALLOCATION";
    public static final String REQUETE_AF_AGRICOLE_COL_NAME_ID_ENTETE_PRESTATION = "ID_ENTETE_PRESTATION";
    public static final String REQUETE_AF_AGRICOLE_COL_NAME_MONTANT_ALLOCATION = "MONTANT_ALLOCATION";
    public static final String REQUETE_AF_AGRICOLE_COL_NAME_NOMBRE_ALLOCATION = "NOMBRE_ALLOCATION";
    public static final String REQUETE_AF_AGRICOLE_COL_NAME_PAYS_ALLOCATAIRE = "PAYS_ALLOCATAIRE";
    public static final String REQUETE_AF_AGRICOLE_COL_NAME_PAYS_ENFANT = "PAYS_ENFANT";
    public static final String REQUETE_AF_AGRICOLE_COL_NAME_ID_ENFANT = "ID_ENFANT";
    public static final String REQUETE_AF_AGRICOLE_COL_NAME_TYPE_ACTIVITE = "TYPE_ACTIVITE";
    public static final String REQUETE_AF_AGRICOLE_COL_NAME_NUM_AFFILIE = "NUMERO_AFFILIE";
    public static final String REQUETE_AF_AGRICOLE_COL_NAME_ID_ALLOCATAIRE = "ID_ALLOCATAIRE";

    public static final String REQUETE_AF_AGRICOLE_COL_NAME_TYPE_ALLOCATION = "TYPE_ALLOCATION";
    public static final String REQUETE_AF_AGRICOLE_COL_NAME_TYPE_LOI = "TYPE_LOI";
    public static final String REQUETE_APG_COL_NAME_NOMBRE_APG = "NOMBRE_APG";
    public static final String REQUETE_COMPTAGE_AFFILIE_38_COL_NAME_CODE_CANTON = "CODE_CANTON";
    public static final String REQUETE_COMPTAGE_AFFILIE_COL_NAME_BRA_ECO = "BRA";
    public static final String REQUETE_COMPTAGE_AFFILIE_COL_NAME_CODE_CANTON = "CODE_CANTON";
    public static final String REQUETE_COMPTAGE_AFFILIE_COL_NAME_COTI_AVS_PAR = "PAR";
    public static final String REQUETE_COMPTAGE_AFFILIE_COL_NAME_COTI_AVS_PERS = "PERS";

    public static final String REQUETE_COMPTAGE_AFFILIE_COL_NAME_TYPE_AFFILIE = "TYPE_PERS";
    public static final String REQUETE_COMPTAGE_AFFILIE_OBLIGE_COTISER_AC_COL_NAME_NOMBRE_AFFILIE = "NOMBRE_AFFILIE";
    public static final String REQUETE_COMPTAGE_CALCUL_ANTICIPE_RENTE = "CALCUL_ANTICIPE_RENTE";

    public static final String REQUETE_COMPTAGE_CONTROLE_EMPLOYEUR_EFFECTUE_COL_NAME_NOMBRE_CONTROLE = "NOMBRE_CONTROLE";

    public static final String REQUETE_COMPTAGE_DOCUMENT_ENVOYE_RETOURNE_COL_NAME_NOMBRE_DOCUMENT = "NOMBRE_DOCUMENT";
    public static final String REQUETE_COMPTAGE_NOMBRE_IM_CAS_AI = "NOMBRE_IM_CAS_AI";
    public static final String REQUETE_COMPTAGE_NOMBRE_IM_CAS_AVS = "NOMBRE_IM_CAS_AVS";
    public static final String REQUETE_COMPTAGE_TOTAL_IM = "NOMBRE_TOTAL_IM";

    public static final String REQUETE_COTI_AND_NOMBRE_EMPLOYEUR_DECOMPTE_SIMPLIFIE_COL_NAME_COTISATION = "COTISATION";

    public static final String REQUETE_COTI_AND_NOMBRE_EMPLOYEUR_DECOMPTE_SIMPLIFIE_COL_NAME_DATE_FIN_AFFILIATION = "DATE_FIN_AFFILIATION";
    public static final String REQUETE_COTI_AND_NOMBRE_EMPLOYEUR_DECOMPTE_SIMPLIFIE_COL_NAME_MOTIF_FIN_AFFILIATION = "MOTIF_FIN_AFFILIATION";
    public static final String REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_ANNEE = "ANNEE";

    public static final String REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_COTISATION = "COTISATION";
    public static final String REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_DATE_DEBUT_PERIODE = "DATE_DEBUT_PERIODE";
    public static final String REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_DATE_FIN_PERIODE = "DATE_FIN_PERIODE";

    public static final String REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_DATE_NAISSANCE = "DATE_NAISSANCE";
    public static final String REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_NUMERO_AFFILIE = "NUMERO_AFFILIE";
    public static final String REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_SEXE = "SEXE";

    public static final String REQUETE_COTI_AND_NOMBRE_SALARIE_WITH_RENTE_COL_NAME_ANNEE_COTISATION = "ANNEE_COTISATION";

    public static final String REQUETE_COTI_AND_NOMBRE_SALARIE_WITH_RENTE_COL_NAME_NOMBRE_AFFILIE = "NOMBRE_AFFILIE";
    public static final String REQUETE_COTI_AND_NOMBRE_SALARIE_WITH_RENTE_COL_NAME_REVENU = "REVENU";
    public static final String REQUETE_MODE_VERSEMENT_RENTE_COL_NAME_NOMBRE_VERSEMENT = "NOMBRE_VERSEMENT";
    public static final String REQUETE_NOMBRE_EMPLOYE_DECOMPTE_SIMPLIFIE_COL_NAME_NOMBRE_EMPLOYE = "NOMBRE_EMPLOYE";
    public static final String REQUETE_NOMBRE_EMPLOYEUR_DECOMPTE_SIMPLIFIE_COL_NAME_NOMBRE_EMPLOYEUR = "NOMBRE_EMPLOYEUR";
    public static final String REQUETE_NOMBRE_EXCLUSION_DECOMPTE_SIMPLIFIE_COL_NAME_NOMBRE_EXCLUSION = "NOMBRE_EXCLUSION";

    public static final String REQUETE_RENTE_VIEILLESSE_AJOURNEE_COL_NAME_DATE_DEBUT_DROIT = "DATE_DEBUT_DROIT";
    public static final String REQUETE_RENTE_VIEILLESSE_AJOURNEE_COL_NAME_DATE_FIN_DROIT = "DATE_FIN_DROIT";
    public static final String REQUETE_RENTE_VIEILLESSE_AJOURNEE_COL_NAME_ID_TIERS_BENEFICIAIRE = "ID_TIERS_BENEFICIAIRE";

    public static final String REQUETE_REPARATION_DOMMAGE_COL_NAME_MONTANT_CREANCE = "MONTANT_CREANCE";

    public static final String REQUETE_REQUISITION_POURSUITE_COL_NAME_COTAVS = "COTAVS";

    public static final String REQUETE_REQUISITION_POURSUITE_COL_NAME_CS_LIBELLE_ETAPE = "CS_LIBELLE_ETAPE";

    public static final String REQUETE_REQUISITION_POURSUITE_COL_NAME_MONTANT_POURSUITE = "MONTANT_POURSUITE";

    public static final String REQUETE_SURSIS_PAIEMENT_COL_NAME_COTAVS = "COTAVS";

    public static final String REQUETE_SURSIS_PAIEMENT_COL_NAME_TOTAL = "TOTAL";

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String annee = null;

    private String anneeStatMoinsUn = null;
    private String anneeStatPlusUn = null;
    private String codeCantonCaisse = null;
    private String idTypeAdresse = null;
    private List<String> listCodePaysUE = null;
    private List<String> listCSVLine = null;
    private List<String> listCSVLineControleLAALPP = null;
    private List<String> listTypeActiviteAFAgriculture = null;
    private List<String> listTypeAllocationAFAgriculture = null;
    private Map<String, String> mapCodeCantonChiffreStatOfas = null;

    private Map<String, String> mapInfoHorsStatOfas = null;
    private Map<String, String> mapStatControleLAALPP = null;
    private Map<String, String> mapStatOfas = null;

    private int nombreTotalAffilieCanton = 0;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String schemaDBWithTablePrefix = null;

    public AFStatistiquesOfasProcess() {
        super();
        codeCantonCaisse = "";
        annee = "";
        anneeStatPlusUn = "";
        anneeStatMoinsUn = "";
        idTypeAdresse = "";
        schemaDBWithTablePrefix = TIToolBox.getCollection();

        listTypeActiviteAFAgriculture = new ArrayList<String>();
        listTypeActiviteAFAgriculture.add(ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE);
        listTypeActiviteAFAgriculture.add(ALCSDossier.ACTIVITE_AGRICULTEUR);
        listTypeActiviteAFAgriculture.add(ALCSDossier.ACTIVITE_EXPLOITANT_ALPAGE);
        listTypeActiviteAFAgriculture.add(ALCSDossier.ACTIVITE_PECHEUR);
        listTypeActiviteAFAgriculture.add(ALCSDossier.ACTIVITE_COLLAB_AGRICOLE);

        listTypeAllocationAFAgriculture = new ArrayList<String>();
        listTypeAllocationAFAgriculture.add(ALCSDroit.TYPE_ENF);
        listTypeAllocationAFAgriculture.add(ALCSDroit.TYPE_FORM);
        listTypeAllocationAFAgriculture.add(ALCSDroit.TYPE_MEN);

        listCodePaysUE = new ArrayList<String>();
        listCodePaysUE.add("204");
        listCodePaysUE.add("205");
        listCodePaysUE.add("206");
        listCodePaysUE.add("207");
        listCodePaysUE.add("211");
        listCodePaysUE.add("212");
        listCodePaysUE.add("214");
        listCodePaysUE.add("215");
        listCodePaysUE.add("216");
        listCodePaysUE.add("217");
        listCodePaysUE.add("218");
        listCodePaysUE.add("223");
        listCodePaysUE.add("224");
        listCodePaysUE.add("227");
        listCodePaysUE.add("228");
        listCodePaysUE.add("229");
        listCodePaysUE.add("230");
        listCodePaysUE.add("231");
        listCodePaysUE.add("232");
        listCodePaysUE.add("234");
        listCodePaysUE.add("236");
        listCodePaysUE.add("240");
        listCodePaysUE.add("242");
        listCodePaysUE.add("243");
        listCodePaysUE.add("244");
        listCodePaysUE.add("251");
        listCodePaysUE.add("260");
        listCodePaysUE.add("261");
        listCodePaysUE.add("262");

        listCSVLine = new ArrayList<String>();
        listCSVLineControleLAALPP = new ArrayList<String>();

        mapInfoHorsStatOfas = new HashMap<String, String>();
        mapInfoHorsStatOfas.put(AFStatistiquesOfasProcess.KEY_TOTAL_SURSIS_PAIEMENT_MAP_INFO_HORS_STAT_OFAS, "0");
        mapInfoHorsStatOfas.put(AFStatistiquesOfasProcess.KEY_TOTAL_REQUISITION_POURSUITE_MAP_INFO_HORS_STAT_OFAS, "0");
        mapInfoHorsStatOfas.put(
                AFStatistiquesOfasProcess.KEY_TOTAL_REQUISITION_CONTINUER_POURSUITE_MAP_INFO_HORS_STAT_OFAS, "0");
        mapInfoHorsStatOfas.put(
                AFStatistiquesOfasProcess.KEY_NOMBRE_CREANCE_REPARATION_DOMMAGE_MAP_INFO_HORS_STAT_OFAS, "0");
        mapInfoHorsStatOfas.put(AFStatistiquesOfasProcess.KEY_TOTAL_CREANCE_REPARATION_DOMMAGE_MAP_INFO_HORS_STAT_OFAS,
                "0");

        mapStatOfas = new HashMap<String, String>();

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_066_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_067_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_068_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_069_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_070_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_071_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_061_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_062_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_063_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_075_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_016_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_018_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_031_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_032_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_033_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_035_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_036_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_037_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_038_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_039_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_044_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_045_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_055_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_056_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_057_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_058_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_102_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_103_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_104_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_105_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_106_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_107_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_108_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_109_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_110_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_111_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_112_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_113_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_114_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_115_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_116_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_117_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_118_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_119_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_120_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_121_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_122_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_123_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_124_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_125_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_126_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_127_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_128_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_140_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_701_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_703_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_704_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_705_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_707_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_708_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_709_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_710_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_711_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_712_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_715_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_716_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_726_MAP_STAT_OFAS, "0");

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_082_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_182_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_601_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_602_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_603_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_084_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_605_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_606_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_607_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_604_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_083_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_608_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_609_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_611_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_612_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_613_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_610_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_615_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_616_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_617_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_614_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_618_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_619_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_620_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_085_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_184_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_621_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_622_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_623_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_087_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_625_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_626_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_627_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_624_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_086_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_628_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_629_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_631_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_632_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_633_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_630_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_635_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_636_MAP_STAT_OFAS, "0");

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_637_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_634_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_638_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_639_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_640_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_088_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_730_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_641_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_642_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_643_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_186_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_645_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_646_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_647_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_644_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_648_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_649_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_651_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_652_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_653_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_650_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_655_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_656_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_657_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_654_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_658_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_659_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_090_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_731_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_660_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_661_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_662_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_187_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_664_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_665_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_666_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_663_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_667_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_668_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_670_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_671_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_672_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_669_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_674_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_675_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_676_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_673_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_677_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_678_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_092_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_732_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_733_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_734_MAP_STAT_OFAS, "0");

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_735_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_736_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_737_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_738_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_739_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_740_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_741_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_742_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_743_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_744_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_745_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_746_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_747_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_748_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_749_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_750_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_751_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_752_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_093_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_753_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_754_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_755_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_756_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_757_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_758_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_759_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_760_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_761_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_762_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_763_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_764_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_765_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_766_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_767_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_768_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_769_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_770_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_771_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_772_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_773_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_094_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_774_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_775_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_776_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_777_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_192_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_778_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_779_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_780_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_683_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_684_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_685_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_781_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_782_MAP_STAT_OFAS, "0");

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_783_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_686_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_784_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_785_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_786_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_687_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_688_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_689_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_096_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_787_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_788_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_789_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_790_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_193_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_791_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_792_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_793_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_690_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_691_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_692_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_794_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_795_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_796_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_693_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_797_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_798_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_799_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_694_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_695_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_696_MAP_STAT_OFAS, "0");

        mapStatControleLAALPP = new HashMap<String, String>();

        mapStatControleLAALPP.put(
                AFStatistiquesOfasProcess.KEY_NOMBRE_QUESTIONNAIRE_LAA_RETOURNE_MAP_STAT_CONTROLE_LAA_LPP, "0");
        mapStatControleLAALPP.put(
                AFStatistiquesOfasProcess.KEY_NOMBRE_QUESTIONNAIRE_LPP_RETOURNE_MAP_STAT_CONTROLE_LAA_LPP, "0");
        mapStatControleLAALPP.put(
                AFStatistiquesOfasProcess.KEY_NOMBRE_DECLARATION_SALAIRE_RETOURNE_MAP_STAT_CONTROLE_LAA_LPP, "0");
        mapStatControleLAALPP.put(
                AFStatistiquesOfasProcess.KEY_NOMBRE_CONTROLE_EMPLOYEUR_EFFECTUE_MAP_STAT_CONTROLE_LAA_LPP, "0");

        mapCodeCantonChiffreStatOfas = new HashMap<String, String>();
        mapCodeCantonChiffreStatOfas.put("505019", AFStatistiquesOfasProcess.KEY_120_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505016", AFStatistiquesOfasProcess.KEY_117_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505015", AFStatistiquesOfasProcess.KEY_116_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505002", AFStatistiquesOfasProcess.KEY_103_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505013", AFStatistiquesOfasProcess.KEY_114_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505012", AFStatistiquesOfasProcess.KEY_113_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505010", AFStatistiquesOfasProcess.KEY_111_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505025", AFStatistiquesOfasProcess.KEY_126_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505008", AFStatistiquesOfasProcess.KEY_109_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505018", AFStatistiquesOfasProcess.KEY_119_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505026", AFStatistiquesOfasProcess.KEY_127_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505003", AFStatistiquesOfasProcess.KEY_104_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505024", AFStatistiquesOfasProcess.KEY_125_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505007", AFStatistiquesOfasProcess.KEY_108_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505006", AFStatistiquesOfasProcess.KEY_107_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505017", AFStatistiquesOfasProcess.KEY_118_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505014", AFStatistiquesOfasProcess.KEY_115_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505011", AFStatistiquesOfasProcess.KEY_112_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505005", AFStatistiquesOfasProcess.KEY_106_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505020", AFStatistiquesOfasProcess.KEY_121_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505021", AFStatistiquesOfasProcess.KEY_122_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505004", AFStatistiquesOfasProcess.KEY_105_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505022", AFStatistiquesOfasProcess.KEY_123_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505023", AFStatistiquesOfasProcess.KEY_124_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505009", AFStatistiquesOfasProcess.KEY_110_MAP_STAT_OFAS);
        mapCodeCantonChiffreStatOfas.put("505001", AFStatistiquesOfasProcess.KEY_102_MAP_STAT_OFAS);

    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {

        boolean success = true;
        String errorMessage = "";

        try {

            codeCantonCaisse = GlobazServer.getCurrentSystem().getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)
                    .getProperty(CommonProperties.KEY_CANTON_CAISSE);

            anneeStatMoinsUn = String.valueOf(Integer.valueOf(annee).intValue() - 1);
            anneeStatPlusUn = String.valueOf(Integer.valueOf(annee).intValue() + 1);

            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = initThreadContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            try {
                compterAffilieSommerCotisation(AFStatistiquesOfasProcess.MODE_COMPTER_AFFILIE);
            } catch (Exception e) {
                handleErrorForPartOfStat();
            }

            try {
                compterAffilieSommerCotisation(AFStatistiquesOfasProcess.MODE_SOMMER_COTISATION);
            } catch (Exception e) {
                handleErrorForPartOfStat();
            }

            try {
                compterAffilieObligeCotiserAC();
            } catch (Exception e) {
                handleErrorForPartOfStat();
            }
            try {
                compterEtSommerCotisationIndTseWithRente();
            } catch (Exception e) {
                handleErrorForPartOfStat();
            }
            try {
                compterEtSommerCotisationSalarieWithRente();
            } catch (Exception e) {
                handleErrorForPartOfStat();
            }
            try {
                compterEtSommerCotisationDecompteSimplifie();
            } catch (Exception e) {
                handleErrorForPartOfStat();
            }
            try {
                sursisPaiement();
            } catch (Exception e) {
                handleErrorForPartOfStat();
            }
            try {
                requisitionPoursuite();
            } catch (Exception e) {
                handleErrorForPartOfStat();
            }
            try {
                reparationDommage();
            } catch (Exception e) {
                handleErrorForPartOfStat();
            }
            try {
                modeVersementRente();
            } catch (Exception e) {
                handleErrorForPartOfStat();
            }
            try {
                renteVieillesseAjournee();
            } catch (Exception e) {
                handleErrorForPartOfStat();
            }
            try {
                calculAnticipeRente();
            } catch (Exception e) {
                handleErrorForPartOfStat();
            }
            try {
                calculInteretsMoratoiresPrestations();
            } catch (Exception e) {
                handleErrorForPartOfStat();
            }
            try {
                apg();
            } catch (Exception e) {
                handleErrorForPartOfStat();
            }

            try {
                afAgricole();
            } catch (Exception e) {
                handleErrorForPartOfStat();
            }

            try {
                compterNombreQuestionnaireLAARetourne();
            } catch (Exception e) {
                handleErrorForPartOfStat();
            }

            try {
                compterNombreQuestionnaireLPPRetourne();
            } catch (Exception e) {
                handleErrorForPartOfStat();
            }

            try {
                compterNombreDeclarationSalaireRetourne();
            } catch (Exception e) {
                handleErrorForPartOfStat();
            }

            try {
                compterNombreControleEmployeurEffectue();
            } catch (Exception e) {
                handleErrorForPartOfStat();
            }

            creerDocumentStatistiqueOfasCSV();

            creerDocumentStatistiqueControleLAALPPCSV();

        } catch (Exception e) {
            success = false;
            errorMessage = e.toString();
        } finally {
            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        if (!success || isAborted() || isOnError() || getSession().hasErrors()) {

            success = false;

            getMemoryLog().logMessage(errorMessage, FWMessage.FATAL, this.getClass().getName());
            this._addError(getTransaction(), errorMessage);
        }

        return success;

    }

    private void addAHorsStatistiqueLineInListCSVLine(String keyMapInfoHorsStatOfas) {

        String theLabel = getSession().getLabel(
                AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + keyMapInfoHorsStatOfas);
        String theValue = mapInfoHorsStatOfas.get(keyMapInfoHorsStatOfas);

        addALineInListCSVLine(theLabel, theValue);

    }

    private void addALineInListControleLAALPPCSVLine(String value1, String value2) {

        StringBuffer tempLine = new StringBuffer();

        tempLine.append(value1);
        tempLine.append(";");
        tempLine.append(value2);
        tempLine.append(";");
        tempLine.append(IOUtils.LINE_SEPARATOR);

        listCSVLineControleLAALPP.add(tempLine.toString());

    }

    private void addALineInListCSVLine(String value1, String value2) {

        StringBuffer tempLine = new StringBuffer();

        tempLine.append(value1);
        tempLine.append(";");
        tempLine.append(value2);
        tempLine.append(";");
        tempLine.append(IOUtils.LINE_SEPARATOR);

        listCSVLine.add(tempLine.toString());

    }

    private void addAStatistiqueLineInListCSVLine(String keyMapStatOfas) {

        addALineInListCSVLine(
                getSession().getLabel(AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + keyMapStatOfas),
                mapStatOfas.get(keyMapStatOfas));

    }

    private void addAStatistiqueLineInListCSVLine(String keyMapStatOfas, String param1) {

        String theLabel = FWMessageFormat.format(
                getSession().getLabel(AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + keyMapStatOfas),
                param1);
        addALineInListCSVLine(theLabel, mapStatOfas.get(keyMapStatOfas));

    }

    private void addAStatistiqueLineInListCSVLine(String keyMapStatOfas, String param1, String param2) {

        String theLabel = FWMessageFormat.format(
                getSession().getLabel(AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + keyMapStatOfas),
                param1, param2);
        addALineInListCSVLine(theLabel, mapStatOfas.get(keyMapStatOfas));

    }

    private void addASuiteStatistiqueLineInListCSVLine(int suiteBegin, int suiteEnd, boolean wantLeadingZero) {

        String leadingString = "";
        if (wantLeadingZero) {
            leadingString = "0";
        }

        for (int i = suiteBegin; i <= suiteEnd; i++) {
            this.addAStatistiqueLineInListCSVLine(leadingString + i);
        }

    }

    private void afAgricole() throws Exception {

        String sqlQueryAFAgricole = getSqlAFAgricoleNew();
        List<Map<String, String>> listMapResultQueryAFAgricole = executeQuery(sqlQueryAFAgricole);

        Map<String, StatOfasAFAgricoleBean> mapStatOfasAFAgricoleBean = new HashMap<String, StatOfasAFAgricoleBean>();
        mapStatOfasAFAgricoleBean.put(AFStatistiquesOfasProcess.KEY_TRAVAILLEUR_AGRICOLE_PLAINE,
                new StatOfasAFAgricoleBean());
        mapStatOfasAFAgricoleBean.put(AFStatistiquesOfasProcess.KEY_TRAVAILLEUR_AGRICOLE_MONTAGNE,
                new StatOfasAFAgricoleBean());
        mapStatOfasAFAgricoleBean.put(AFStatistiquesOfasProcess.KEY_AGRICULTEUR_PRINCIPAL_PLAINE,
                new StatOfasAFAgricoleBean());
        mapStatOfasAFAgricoleBean.put(AFStatistiquesOfasProcess.KEY_AGRICULTEUR_PRINCIPAL_MONTAGNE,
                new StatOfasAFAgricoleBean());
        mapStatOfasAFAgricoleBean.put(AFStatistiquesOfasProcess.KEY_AGRICULTEUR_ACCESSOIRE_PLAINE,
                new StatOfasAFAgricoleBean());
        mapStatOfasAFAgricoleBean.put(AFStatistiquesOfasProcess.KEY_AGRICULTEUR_ACCESSOIRE_MONTAGNE,
                new StatOfasAFAgricoleBean());
        mapStatOfasAFAgricoleBean.put(AFStatistiquesOfasProcess.KEY_EXPLOITANT_ALPAGE, new StatOfasAFAgricoleBean());
        mapStatOfasAFAgricoleBean.put(AFStatistiquesOfasProcess.KEY_PECHEUR, new StatOfasAFAgricoleBean());
        List<String> listeClesAllocsEnfants = new ArrayList<String>();
        List<String> listeClesAllocsMenages = new ArrayList<String>();
        List<String> listeClesAyantsDroits = new ArrayList<String>();
        for (Map<String, String> aMapRowResultQueryAFAgricole : listMapResultQueryAFAgricole) {
            String idEntetePrestation = aMapRowResultQueryAFAgricole
                    .get(AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_ID_ENTETE_PRESTATION);
            String typeAllocation = aMapRowResultQueryAFAgricole
                    .get(AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_TYPE_ALLOCATION);
            String typeActivite = aMapRowResultQueryAFAgricole
                    .get(AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_TYPE_ACTIVITE);
            String typeLoi = aMapRowResultQueryAFAgricole
                    .get(AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_TYPE_LOI);
            String genreAllocation = aMapRowResultQueryAFAgricole
                    .get(AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_GENRE_ALLOCATION);

            String isActiviteAccessoireString = aMapRowResultQueryAFAgricole
                    .get(AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_ACTIVITE_ACCESSOIRE);
            boolean isActiviteAccessoire = Integer.valueOf(isActiviteAccessoireString).intValue() == 1;

            String paysAllocataire = aMapRowResultQueryAFAgricole
                    .get(AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_PAYS_ALLOCATAIRE);
            String paysEnfant = aMapRowResultQueryAFAgricole
                    .get(AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_PAYS_ENFANT);
            String idEnfant = aMapRowResultQueryAFAgricole
                    .get(AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_ID_ENFANT);
            String numAff = aMapRowResultQueryAFAgricole
                    .get(AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_NUM_AFFILIE);
            String idAllocataire = aMapRowResultQueryAFAgricole
                    .get(AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_ID_ALLOCATAIRE);

            String montantAllocationString = aMapRowResultQueryAFAgricole
                    .get(AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_MONTANT_ALLOCATION);
            montantAllocationString = replaceDecimalCommaByDecimalPoint(montantAllocationString);
            double montantAllocation = Double.valueOf(montantAllocationString).doubleValue();

            boolean isTravailleurAgricolePlaine = ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE
                    .equalsIgnoreCase(typeActivite) && ALCSTarif.CATEGORIE_LFP.equalsIgnoreCase(typeLoi);
            boolean isTravailleurAgricoleMontagne = ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE
                    .equalsIgnoreCase(typeActivite) && ALCSTarif.CATEGORIE_LFM.equalsIgnoreCase(typeLoi);
            boolean isAgriculteurPrincipalPlaine = (ALCSDossier.ACTIVITE_AGRICULTEUR.equalsIgnoreCase(typeActivite) || ALCSDossier.ACTIVITE_COLLAB_AGRICOLE
                    .equalsIgnoreCase(typeActivite))
                    && ALCSTarif.CATEGORIE_LFP.equalsIgnoreCase(typeLoi)
                    && !isActiviteAccessoire;
            boolean isAgriculteurPrincipalMontagne = (ALCSDossier.ACTIVITE_AGRICULTEUR.equalsIgnoreCase(typeActivite) || ALCSDossier.ACTIVITE_COLLAB_AGRICOLE
                    .equalsIgnoreCase(typeActivite))
                    && ALCSTarif.CATEGORIE_LFM.equalsIgnoreCase(typeLoi)
                    && !isActiviteAccessoire;
            boolean isAgriculteurAccessoirePlaine = (ALCSDossier.ACTIVITE_AGRICULTEUR.equalsIgnoreCase(typeActivite) || ALCSDossier.ACTIVITE_COLLAB_AGRICOLE
                    .equalsIgnoreCase(typeActivite))
                    && ALCSTarif.CATEGORIE_LFP.equalsIgnoreCase(typeLoi)
                    && isActiviteAccessoire;
            boolean isAgriculteurAccessoireMontagne = (ALCSDossier.ACTIVITE_AGRICULTEUR.equalsIgnoreCase(typeActivite) || ALCSDossier.ACTIVITE_COLLAB_AGRICOLE
                    .equalsIgnoreCase(typeActivite))
                    && ALCSTarif.CATEGORIE_LFM.equalsIgnoreCase(typeLoi)
                    && isActiviteAccessoire;
            boolean isExploitantAlpage = ALCSDossier.ACTIVITE_EXPLOITANT_ALPAGE.equalsIgnoreCase(typeActivite);
            boolean isPecheur = ALCSDossier.ACTIVITE_PECHEUR.equalsIgnoreCase(typeActivite);

            boolean isAllocationDifferentielle = ALCSPrestation.STATUT_ADC.equalsIgnoreCase(genreAllocation)
                    || ALCSPrestation.STATUT_ADI.equalsIgnoreCase(genreAllocation);

            StatOfasAFAgricoleBean statOfasAFAgricoleBean = null;
            String keyActivite = "";
            if (isTravailleurAgricolePlaine) {
                statOfasAFAgricoleBean = mapStatOfasAFAgricoleBean
                        .get(AFStatistiquesOfasProcess.KEY_TRAVAILLEUR_AGRICOLE_PLAINE);
                keyActivite = AFStatistiquesOfasProcess.KEY_TRAVAILLEUR_AGRICOLE_PLAINE;
            } else if (isTravailleurAgricoleMontagne) {
                statOfasAFAgricoleBean = mapStatOfasAFAgricoleBean
                        .get(AFStatistiquesOfasProcess.KEY_TRAVAILLEUR_AGRICOLE_MONTAGNE);
                keyActivite = AFStatistiquesOfasProcess.KEY_TRAVAILLEUR_AGRICOLE_MONTAGNE;
            } else if (isAgriculteurPrincipalPlaine) {
                statOfasAFAgricoleBean = mapStatOfasAFAgricoleBean
                        .get(AFStatistiquesOfasProcess.KEY_AGRICULTEUR_PRINCIPAL_PLAINE);
                keyActivite = AFStatistiquesOfasProcess.KEY_AGRICULTEUR_PRINCIPAL_PLAINE;
            } else if (isAgriculteurPrincipalMontagne) {
                statOfasAFAgricoleBean = mapStatOfasAFAgricoleBean
                        .get(AFStatistiquesOfasProcess.KEY_AGRICULTEUR_PRINCIPAL_MONTAGNE);
                keyActivite = AFStatistiquesOfasProcess.KEY_AGRICULTEUR_PRINCIPAL_MONTAGNE;
            } else if (isAgriculteurAccessoirePlaine) {
                statOfasAFAgricoleBean = mapStatOfasAFAgricoleBean
                        .get(AFStatistiquesOfasProcess.KEY_AGRICULTEUR_ACCESSOIRE_PLAINE);
                keyActivite = AFStatistiquesOfasProcess.KEY_AGRICULTEUR_ACCESSOIRE_PLAINE;
            } else if (isAgriculteurAccessoireMontagne) {
                statOfasAFAgricoleBean = mapStatOfasAFAgricoleBean
                        .get(AFStatistiquesOfasProcess.KEY_AGRICULTEUR_ACCESSOIRE_MONTAGNE);
                keyActivite = AFStatistiquesOfasProcess.KEY_AGRICULTEUR_ACCESSOIRE_MONTAGNE;
            } else if (isExploitantAlpage) {
                statOfasAFAgricoleBean = mapStatOfasAFAgricoleBean.get(AFStatistiquesOfasProcess.KEY_EXPLOITANT_ALPAGE);
                keyActivite = AFStatistiquesOfasProcess.KEY_EXPLOITANT_ALPAGE;
            } else if (isPecheur) {
                statOfasAFAgricoleBean = mapStatOfasAFAgricoleBean.get(AFStatistiquesOfasProcess.KEY_PECHEUR);
                keyActivite = AFStatistiquesOfasProcess.KEY_PECHEUR;
            } else {
                // throw new CommonTechnicalException("Not implemented");
                continue;
            }

            // Création de la clé composé des informations de l'ayant droit
            String cleAyantsDroits = idAllocataire + idEntetePrestation + numAff + keyActivite;
            if (!listeClesAyantsDroits.contains(cleAyantsDroits)) {
                listeClesAyantsDroits.add(cleAyantsDroits);
                statOfasAFAgricoleBean.setNombreAyantDroit(statOfasAFAgricoleBean.getNombreAyantDroit() + 1);

                if (!AFStatistiquesOfasProcess.CODE_PAYS_SUISSE.equalsIgnoreCase(paysAllocataire)) {
                    statOfasAFAgricoleBean.setNombreAyantDroitEtranger(statOfasAFAgricoleBean
                            .getNombreAyantDroitEtranger() + 1);
                }
            }

            // Initialisation de la variable à 1 car on ne calcul plus directement le nombre depuis la requête SQL
            int nombreAllocation = 1;

            // Création de la clé composé des informations pour un enfant
            String cleAllocsEnfant = idEnfant + typeAllocation + numAff + keyActivite + genreAllocation;
            // Création de la clé ménage composé du numéro dossier et de l'activité
            String cleAllocsMenage = idEntetePrestation + typeActivite;
            if (ALCSDroit.TYPE_MEN.equals(typeAllocation) && !listeClesAllocsMenages.contains(cleAllocsMenage)) {
                listeClesAllocsMenages.add(cleAllocsMenage);
            } else if (!listeClesAllocsEnfants.contains(cleAllocsEnfant)) {
                listeClesAllocsEnfants.add(cleAllocsEnfant);
            } else {
                nombreAllocation = 0;
            }

            statOfasAFAgricoleBean.setNombreTotalAllocation(statOfasAFAgricoleBean.getNombreTotalAllocation()
                    + nombreAllocation);
            statOfasAFAgricoleBean.setMontantTotalAllocation(statOfasAFAgricoleBean.getMontantTotalAllocation()
                    + montantAllocation);
            if (isAllocationDifferentielle) {
                statOfasAFAgricoleBean.setNombreTotalAllocationDifferentielle(statOfasAFAgricoleBean
                        .getNombreTotalAllocationDifferentielle() + nombreAllocation);
                statOfasAFAgricoleBean.setMontantTotalAllocationDifferentielle(statOfasAFAgricoleBean
                        .getMontantTotalAllocationDifferentielle() + montantAllocation);
            }

            if (ALCSDroit.TYPE_ENF.equalsIgnoreCase(typeAllocation)) {

                statOfasAFAgricoleBean.setNombreTotalAllocationEnfant(statOfasAFAgricoleBean
                        .getNombreTotalAllocationEnfant() + nombreAllocation);
                statOfasAFAgricoleBean.setMontantTotalAllocationEnfant(statOfasAFAgricoleBean
                        .getMontantTotalAllocationEnfant() + montantAllocation);

                if (AFStatistiquesOfasProcess.CODE_PAYS_SUISSE.equalsIgnoreCase(paysEnfant)) {
                    statOfasAFAgricoleBean.setNombreAllocationEnfantSuisse(statOfasAFAgricoleBean
                            .getNombreAllocationEnfantSuisse() + nombreAllocation);
                    statOfasAFAgricoleBean.setMontantAllocationEnfantSuisse(statOfasAFAgricoleBean
                            .getMontantAllocationEnfantSuisse() + montantAllocation);
                } else if (listCodePaysUE.contains(paysEnfant)) {
                    statOfasAFAgricoleBean.setNombreAllocationEnfantUE(statOfasAFAgricoleBean
                            .getNombreAllocationEnfantUE() + nombreAllocation);
                    statOfasAFAgricoleBean.setMontantAllocationEnfantUE(statOfasAFAgricoleBean
                            .getMontantAllocationEnfantUE() + montantAllocation);
                } else {
                    statOfasAFAgricoleBean.setNombreAllocationEnfantHorsUE(statOfasAFAgricoleBean
                            .getNombreAllocationEnfantHorsUE() + nombreAllocation);
                    statOfasAFAgricoleBean.setMontantAllocationEnfantHorsUE(statOfasAFAgricoleBean
                            .getMontantAllocationEnfantHorsUE() + montantAllocation);
                }

            } else if (ALCSDroit.TYPE_FORM.equalsIgnoreCase(typeAllocation)) {

                statOfasAFAgricoleBean.setNombreTotalAllocationFormation(statOfasAFAgricoleBean
                        .getNombreTotalAllocationFormation() + nombreAllocation);
                statOfasAFAgricoleBean.setMontantTotalAllocationFormation(statOfasAFAgricoleBean
                        .getMontantTotalAllocationFormation() + montantAllocation);

                if (AFStatistiquesOfasProcess.CODE_PAYS_SUISSE.equalsIgnoreCase(paysEnfant)) {
                    statOfasAFAgricoleBean.setNombreAllocationFormationSuisse(statOfasAFAgricoleBean
                            .getNombreAllocationFormationSuisse() + nombreAllocation);
                    statOfasAFAgricoleBean.setMontantAllocationFormationSuisse(statOfasAFAgricoleBean
                            .getMontantAllocationFormationSuisse() + montantAllocation);
                } else if (listCodePaysUE.contains(paysEnfant)) {
                    statOfasAFAgricoleBean.setNombreAllocationFormationUE(statOfasAFAgricoleBean
                            .getNombreAllocationFormationUE() + nombreAllocation);
                    statOfasAFAgricoleBean.setMontantAllocationFormationUE(statOfasAFAgricoleBean
                            .getMontantAllocationFormationUE() + montantAllocation);
                } else {
                    statOfasAFAgricoleBean.setNombreAllocationFormationHorsUE(statOfasAFAgricoleBean
                            .getNombreAllocationFormationHorsUE() + nombreAllocation);
                    statOfasAFAgricoleBean.setMontantAllocationFormationHorsUE(statOfasAFAgricoleBean
                            .getMontantAllocationFormationHorsUE() + montantAllocation);
                }
            } else if (ALCSDroit.TYPE_MEN.equalsIgnoreCase(typeAllocation)) {

                statOfasAFAgricoleBean.setNombreAllocationMenage(statOfasAFAgricoleBean.getNombreAllocationMenage()
                        + nombreAllocation);
                statOfasAFAgricoleBean.setMontantAllocationMenage(statOfasAFAgricoleBean.getMontantAllocationMenage()
                        + montantAllocation);

            } else {
                throw new CommonTechnicalException("Not implemented");
            }

        }

        StatOfasAFAgricoleBean statOfasAFAgricoleBean = mapStatOfasAFAgricoleBean
                .get(AFStatistiquesOfasProcess.KEY_TRAVAILLEUR_AGRICOLE_PLAINE);

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_082_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAyantDroitString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_182_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAyantDroitEtrangerString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_601_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_602_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_603_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_084_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationEnfantString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_605_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_606_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_607_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_604_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationFormationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_083_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationMenageString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_608_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_609_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationDifferentielleString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_611_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_612_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_613_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_610_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationEnfantString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_615_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_616_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_617_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_614_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationFormationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_618_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationMenageString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_619_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_620_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationDifferentielleString());

        statOfasAFAgricoleBean = mapStatOfasAFAgricoleBean
                .get(AFStatistiquesOfasProcess.KEY_TRAVAILLEUR_AGRICOLE_MONTAGNE);

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_085_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAyantDroitString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_184_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAyantDroitEtrangerString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_621_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_622_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_623_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_087_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationEnfantString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_625_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_626_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_627_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_624_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationFormationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_086_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationMenageString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_628_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_629_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationDifferentielleString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_631_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_632_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_633_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_630_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationEnfantString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_635_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_636_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_637_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_634_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationFormationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_638_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationMenageString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_639_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_640_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationDifferentielleString());

        statOfasAFAgricoleBean = mapStatOfasAFAgricoleBean
                .get(AFStatistiquesOfasProcess.KEY_AGRICULTEUR_PRINCIPAL_PLAINE);

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_088_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAyantDroitString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_730_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAyantDroitEtrangerString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_641_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_642_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_643_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_186_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationEnfantString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_645_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_646_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_647_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_644_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationFormationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_648_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_649_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationDifferentielleString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_651_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_652_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_653_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_650_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationEnfantString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_655_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_656_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_657_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_654_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationFormationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_658_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_659_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationDifferentielleString());

        statOfasAFAgricoleBean = mapStatOfasAFAgricoleBean
                .get(AFStatistiquesOfasProcess.KEY_AGRICULTEUR_PRINCIPAL_MONTAGNE);

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_090_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAyantDroitString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_731_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAyantDroitEtrangerString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_660_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_661_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_662_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_187_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationEnfantString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_664_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_665_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_666_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_663_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationFormationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_667_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_668_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationDifferentielleString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_670_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_671_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_672_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_669_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationEnfantString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_674_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_675_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_676_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_673_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationFormationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_677_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_678_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationDifferentielleString());

        statOfasAFAgricoleBean = mapStatOfasAFAgricoleBean
                .get(AFStatistiquesOfasProcess.KEY_AGRICULTEUR_ACCESSOIRE_PLAINE);

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_092_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAyantDroitString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_732_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAyantDroitEtrangerString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_733_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_734_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_735_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_736_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationEnfantString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_737_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_738_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_739_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_740_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationFormationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_741_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_742_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationDifferentielleString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_743_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_744_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_745_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_746_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationEnfantString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_747_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_748_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_749_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_750_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationFormationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_751_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_752_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationDifferentielleString());

        statOfasAFAgricoleBean = mapStatOfasAFAgricoleBean
                .get(AFStatistiquesOfasProcess.KEY_AGRICULTEUR_ACCESSOIRE_MONTAGNE);

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_093_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAyantDroitString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_753_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAyantDroitEtrangerString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_754_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_755_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_756_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_757_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationEnfantString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_758_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_759_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_760_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_761_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationFormationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_762_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_763_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationDifferentielleString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_764_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_765_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_766_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_767_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationEnfantString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_768_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_769_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_770_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_771_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationFormationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_772_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_773_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationDifferentielleString());

        statOfasAFAgricoleBean = mapStatOfasAFAgricoleBean.get(AFStatistiquesOfasProcess.KEY_EXPLOITANT_ALPAGE);

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_094_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAyantDroitString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_774_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAyantDroitEtrangerString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_775_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_776_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_777_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_192_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationEnfantString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_778_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_779_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_780_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_683_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationFormationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_684_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_685_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationDifferentielleString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_781_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_782_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_783_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_686_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationEnfantString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_784_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_785_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_786_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_687_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationFormationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_688_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_689_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationDifferentielleString());

        statOfasAFAgricoleBean = mapStatOfasAFAgricoleBean.get(AFStatistiquesOfasProcess.KEY_PECHEUR);

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_096_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAyantDroitString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_787_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAyantDroitEtrangerString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_788_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_789_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_790_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationEnfantHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_193_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationEnfantString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_791_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_792_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_793_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreAllocationFormationHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_690_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationFormationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_691_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_692_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getNombreTotalAllocationDifferentielleString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_794_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_795_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_796_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationEnfantHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_693_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationEnfantString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_797_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationSuisseString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_798_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_799_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantAllocationFormationHorsUEString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_694_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationFormationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_695_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationString());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_696_MAP_STAT_OFAS,
                statOfasAFAgricoleBean.getMontantTotalAllocationDifferentielleString());

    }

    private void apg() throws Exception {
        String sqlQueryAPG = getSqlAPG();

        List<Map<String, String>> listMapResultQueryAPG = executeQuery(sqlQueryAPG);

        // La requête retourne une seule ligne
        Map<String, String> aMapRowResultQueryAPG = listMapResultQueryAPG.get(0);

        String nombreAPG = aMapRowResultQueryAPG.get(AFStatistiquesOfasProcess.REQUETE_APG_COL_NAME_NOMBRE_APG);

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_075_MAP_STAT_OFAS, nombreAPG);
    }

    private void calculAnticipeRente() throws Exception {
        String sqlQueryComptageCalculAnticipeRente = getSqlComptageCalculAnticipeRente();

        List<Map<String, String>> listMapResultQueryComptageCalculAnticipeRente = executeQuery(sqlQueryComptageCalculAnticipeRente);

        // La requête retourne une seule ligne
        Map<String, String> aMapRowResultQueryComptageCalculAnticipeRente = listMapResultQueryComptageCalculAnticipeRente
                .get(0);

        String nombreDemandesCalculAnticipeRente = aMapRowResultQueryComptageCalculAnticipeRente
                .get(AFStatistiquesOfasProcess.REQUETE_COMPTAGE_CALCUL_ANTICIPE_RENTE);

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_306_MAP_STAT_OFAS, nombreDemandesCalculAnticipeRente);
    }

    private void calculInteretsMoratoiresPrestations() throws Exception {
        // Chiffre 308 Cas AI
        String sqlQueryComptageIMCasAI = getSqlComptageIMCasAI();

        List<Map<String, String>> listMapResultQueryComptageIMCasAI = executeQuery(sqlQueryComptageIMCasAI);

        // La requête retourne une seule ligne
        Map<String, String> aMapRowResultQueryComptageIMCasAI = listMapResultQueryComptageIMCasAI.get(0);

        String nombreIMCasAI = aMapRowResultQueryComptageIMCasAI
                .get(AFStatistiquesOfasProcess.REQUETE_COMPTAGE_NOMBRE_IM_CAS_AI);

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_308_MAP_STAT_OFAS, nombreIMCasAI);

        // Chiffre 309 Cas AVS
        String sqlQueryComptageIMCasAVS = getSqlComptageIMCasAVS();

        List<Map<String, String>> listMapResultQueryComptageIMCasAVS = executeQuery(sqlQueryComptageIMCasAVS);

        // La requête retourne une seule ligne
        Map<String, String> aMapRowResultQueryComptageIMCasAVS = listMapResultQueryComptageIMCasAVS.get(0);

        String nombreIMCasAVS = aMapRowResultQueryComptageIMCasAVS
                .get(AFStatistiquesOfasProcess.REQUETE_COMPTAGE_NOMBRE_IM_CAS_AVS);

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_309_MAP_STAT_OFAS, nombreIMCasAVS);

        // Chiffre 307 = somme 308 + 309
        String nombreIMCasTotal = String.valueOf(Integer.valueOf(nombreIMCasAI) + Integer.valueOf(nombreIMCasAVS));
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_307_MAP_STAT_OFAS, nombreIMCasTotal);

        // Chiffre 310
        String sqlQueryComptageTotalIM = getSqlComptageSommeIM();

        List<Map<String, String>> listMapResultQueryComptageTotalIM = executeQuery(sqlQueryComptageTotalIM);

        // La requête retourne une seule ligne
        Map<String, String> aMapRowResultQueryComptageTotalIM = listMapResultQueryComptageTotalIM.get(0);

        String nombreTotalIMString = aMapRowResultQueryComptageTotalIM
                .get(AFStatistiquesOfasProcess.REQUETE_COMPTAGE_TOTAL_IM);

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_310_MAP_STAT_OFAS,
                new FWCurrency(nombreTotalIMString).toStringFormat());

        // Chiffre 321 et 322 (Actuellement l'application ne calcule pas d'IM pour les cas APG et MAT)
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_321_MAP_STAT_OFAS, "0");
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_322_MAP_STAT_OFAS, "0");

    }

    private void compterAffilieObligeCotiserAC() throws Exception {
        String sqlQueryComptageAffilieObligeCotiserAC = getSqlComptageAffilieObligeCotiserAC();

        List<Map<String, String>> listMapResultQueryComptageAffilieObligeCotiserAC = executeQuery(sqlQueryComptageAffilieObligeCotiserAC);

        // La requête retourne une seule ligne
        Map<String, String> aMapRowResultQueryComptageAffilieObligeCotiserAC = listMapResultQueryComptageAffilieObligeCotiserAC
                .get(0);

        String nombreAffilieObligeCotiserAC = aMapRowResultQueryComptageAffilieObligeCotiserAC
                .get(AFStatistiquesOfasProcess.REQUETE_COMPTAGE_AFFILIE_OBLIGE_COTISER_AC_COL_NAME_NOMBRE_AFFILIE);

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_140_MAP_STAT_OFAS, nombreAffilieObligeCotiserAC);
    }

    private void compterAffilieSommerCotisation(String mode) throws Exception {

        String theAnnee = "0";
        if (AFStatistiquesOfasProcess.MODE_COMPTER_AFFILIE.equalsIgnoreCase(mode)) {
            theAnnee = annee;
        } else if (AFStatistiquesOfasProcess.MODE_SOMMER_COTISATION.equalsIgnoreCase(mode)) {
            theAnnee = anneeStatMoinsUn;
        } else {
            throw new Exception("compterAffilieSommerCotisation mode : " + mode + " not implemented");
        }

        // Une majoration de CHF 1.- est faite afin de tenir compte des différences d'arrondis lorsque la cotisation
        // minimum n'est pas divisible par 12
        double cotiMinIndMajoree = Double.valueOf(
                FWFindParameter.findParameter(getTransaction(), "10500070", "COTMININDE", "01.01." + theAnnee, "", 2))
                .doubleValue();
        cotiMinIndMajoree = cotiMinIndMajoree + 1;

        String sqlQueryComptageAffilieSommeCotisation = getSqlComptageAffilieSommeCotisation(theAnnee);
        List<Map<String, String>> listMapResultQueryComptageAffilieSommeCotisation = executeQuery(sqlQueryComptageAffilieSommeCotisation);

        int nombreAffTotalCanton = 0;
        int nombreAff038 = 0;
        int nombreAff037 = 0;
        int nombreAff035 = 0;
        int nombreAff033 = 0;
        int nombreAff031 = 0;
        int nombreAff039 = 0;
        int nombreAff032 = 0;
        int nombreAff701 = 0;
        int nombreAff715 = 0;
        int nombreAff036 = 0;
        int nombreAff716 = 0;

        double coti703 = 0;
        double coti704 = 0;
        double coti705 = 0;
        double coti726 = 0;

        for (Map<String, String> aMapRowResultQueryComptageAffilieSommeCotisation : listMapResultQueryComptageAffilieSommeCotisation) {

            String cotAVSParString = aMapRowResultQueryComptageAffilieSommeCotisation
                    .get(AFStatistiquesOfasProcess.REQUETE_COMPTAGE_AFFILIE_COL_NAME_COTI_AVS_PAR);
            cotAVSParString = replaceDecimalCommaByDecimalPoint(cotAVSParString);
            double cotAVSPar = Double.valueOf(cotAVSParString).doubleValue();

            String cotAVSPersString = aMapRowResultQueryComptageAffilieSommeCotisation
                    .get(AFStatistiquesOfasProcess.REQUETE_COMPTAGE_AFFILIE_COL_NAME_COTI_AVS_PERS);
            cotAVSPersString = replaceDecimalCommaByDecimalPoint(cotAVSPersString);
            double cotAVSPers = Double.valueOf(cotAVSPersString).doubleValue();

            String typeAffilie = aMapRowResultQueryComptageAffilieSommeCotisation
                    .get(AFStatistiquesOfasProcess.REQUETE_COMPTAGE_AFFILIE_COL_NAME_TYPE_AFFILIE);
            String brancheEconomique = aMapRowResultQueryComptageAffilieSommeCotisation
                    .get(AFStatistiquesOfasProcess.REQUETE_COMPTAGE_AFFILIE_COL_NAME_BRA_ECO);
            String codeCanton = aMapRowResultQueryComptageAffilieSommeCotisation
                    .get(AFStatistiquesOfasProcess.REQUETE_COMPTAGE_AFFILIE_COL_NAME_CODE_CANTON);

            if ((cotAVSPar == 0) && (cotAVSPers == 0)) {
                nombreAffTotalCanton = ventileAffilieDansComptageParCanton(codeCanton, nombreAffTotalCanton, mode);
                nombreAff038++;
                nombreAff039++;
            }

            if ((cotAVSPar != 0) && CodeSystem.BRANCHE_ECO_PERSONNEL_MAISON.equalsIgnoreCase(brancheEconomique)) {
                nombreAffTotalCanton = ventileAffilieDansComptageParCanton(codeCanton, nombreAffTotalCanton, mode);
                nombreAff037++;
                nombreAff039++;
                nombreAff716++;
            }

            if ((cotAVSPar != 0) && (cotAVSPers == 0)
                    && !CodeSystem.BRANCHE_ECO_PERSONNEL_MAISON.equalsIgnoreCase(brancheEconomique)) {
                nombreAffTotalCanton = ventileAffilieDansComptageParCanton(codeCanton, nombreAffTotalCanton, mode);
                nombreAff037++;
                nombreAff039++;
            }

            if ((cotAVSPers != 0) && CodeSystem.TYPE_AFFILI_TSE.equalsIgnoreCase(typeAffilie)) {
                nombreAffTotalCanton = ventileAffilieDansComptageParCanton(codeCanton, nombreAffTotalCanton, mode);
                nombreAff035++;
                nombreAff039++;

                if ((cotAVSPar != 0) && !CodeSystem.BRANCHE_ECO_PERSONNEL_MAISON.equalsIgnoreCase(brancheEconomique)) {
                    nombreAff036++;
                }

            }
            if ((cotAVSPers != 0) && CodeSystem.TYPE_AFFILI_NON_ACTIF.equalsIgnoreCase(typeAffilie)) {
                nombreAffTotalCanton = ventileAffilieDansComptageParCanton(codeCanton, nombreAffTotalCanton, mode);
                nombreAff033++;
                nombreAff039++;
                coti704 = coti704 + cotAVSPers;

                if ((cotAVSPers >= 0.05) && (cotAVSPers <= cotiMinIndMajoree)) {
                    if (CodeSystem.BRANCHE_ECO_ETUDIANTS.equalsIgnoreCase(brancheEconomique)) {
                        nombreAff715++;
                        coti726 = coti726 + cotAVSPers;
                    } else {
                        nombreAff701++;
                        coti705 = coti705 + cotAVSPers;
                    }

                }

            }
            if ((cotAVSPers != 0) && !CodeSystem.TYPE_AFFILI_TSE.equalsIgnoreCase(typeAffilie)
                    && !CodeSystem.TYPE_AFFILI_NON_ACTIF.equalsIgnoreCase(typeAffilie)) {
                nombreAffTotalCanton = ventileAffilieDansComptageParCanton(codeCanton, nombreAffTotalCanton, mode);
                nombreAff031++;
                nombreAff039++;
                coti703 = coti703 + cotAVSPers;

                if ((cotAVSPar != 0) && !CodeSystem.BRANCHE_ECO_PERSONNEL_MAISON.equalsIgnoreCase(brancheEconomique)) {
                    nombreAff032++;
                }

            }
        }

        if (AFStatistiquesOfasProcess.MODE_COMPTER_AFFILIE.equalsIgnoreCase(mode)) {
            mapStatOfas.put(AFStatistiquesOfasProcess.KEY_037_MAP_STAT_OFAS, String.valueOf(nombreAff037));
            mapStatOfas.put(AFStatistiquesOfasProcess.KEY_035_MAP_STAT_OFAS, String.valueOf(nombreAff035));
            mapStatOfas.put(AFStatistiquesOfasProcess.KEY_033_MAP_STAT_OFAS, String.valueOf(nombreAff033));
            mapStatOfas.put(AFStatistiquesOfasProcess.KEY_031_MAP_STAT_OFAS, String.valueOf(nombreAff031));
            mapStatOfas.put(AFStatistiquesOfasProcess.KEY_032_MAP_STAT_OFAS, String.valueOf(nombreAff032));
            mapStatOfas.put(AFStatistiquesOfasProcess.KEY_701_MAP_STAT_OFAS, String.valueOf(nombreAff701));
            mapStatOfas.put(AFStatistiquesOfasProcess.KEY_715_MAP_STAT_OFAS, String.valueOf(nombreAff715));
            mapStatOfas.put(AFStatistiquesOfasProcess.KEY_036_MAP_STAT_OFAS, String.valueOf(nombreAff036));
            mapStatOfas.put(AFStatistiquesOfasProcess.KEY_716_MAP_STAT_OFAS, String.valueOf(nombreAff716));
        }

        if (AFStatistiquesOfasProcess.MODE_SOMMER_COTISATION.equalsIgnoreCase(mode)) {
            mapStatOfas.put(AFStatistiquesOfasProcess.KEY_703_MAP_STAT_OFAS, new FWCurrency(coti703).toStringFormat());
            mapStatOfas.put(AFStatistiquesOfasProcess.KEY_704_MAP_STAT_OFAS, new FWCurrency(coti704).toStringFormat());
            mapStatOfas.put(AFStatistiquesOfasProcess.KEY_705_MAP_STAT_OFAS, new FWCurrency(coti705).toStringFormat());
            mapStatOfas.put(AFStatistiquesOfasProcess.KEY_726_MAP_STAT_OFAS, new FWCurrency(coti726).toStringFormat());
        }

        String sqlQueryComptageAffilie038 = getSqlComptageAffilie038();
        List<Map<String, String>> listMapResultQueryComptageAffilie038 = executeQuery(sqlQueryComptageAffilie038);

        for (Map<String, String> aMapRowResultQueryComptageAffilie038 : listMapResultQueryComptageAffilie038) {
            String codeCanton = aMapRowResultQueryComptageAffilie038
                    .get(AFStatistiquesOfasProcess.REQUETE_COMPTAGE_AFFILIE_38_COL_NAME_CODE_CANTON);

            nombreAffTotalCanton = ventileAffilieDansComptageParCanton(codeCanton, nombreAffTotalCanton, mode);

            nombreAff038++;
            nombreAff039++;

        }

        if (AFStatistiquesOfasProcess.MODE_COMPTER_AFFILIE.equalsIgnoreCase(mode)) {
            mapStatOfas.put(AFStatistiquesOfasProcess.KEY_038_MAP_STAT_OFAS, String.valueOf(nombreAff038));
            mapStatOfas.put(AFStatistiquesOfasProcess.KEY_039_MAP_STAT_OFAS, String.valueOf(nombreAff039));
            mapStatOfas.put(AFStatistiquesOfasProcess.KEY_128_MAP_STAT_OFAS, String.valueOf(nombreTotalAffilieCanton));
        }

    }

    private void compterEmployeDecompteSimplifie() throws Exception {

        String sqlQueryNombreEmployeDecompteSimplifie = getSqlNombreEmployeDecompteSimplifie();

        List<Map<String, String>> listMapResultQueryNombreEmployeDecompteSimplifie = executeQuery(sqlQueryNombreEmployeDecompteSimplifie);

        // La requête retourne une seule ligne
        Map<String, String> aMapRowResultQueryNombreEmployeDecompteSimplifie = listMapResultQueryNombreEmployeDecompteSimplifie
                .get(0);

        String nombreAff708 = aMapRowResultQueryNombreEmployeDecompteSimplifie
                .get(AFStatistiquesOfasProcess.REQUETE_NOMBRE_EMPLOYE_DECOMPTE_SIMPLIFIE_COL_NAME_NOMBRE_EMPLOYE);

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_708_MAP_STAT_OFAS, nombreAff708);
    }

    private void compterEmployeurDecompteSimplifie() throws Exception {

        String sqlQueryNombreEmployeurDecompteSimplifie = getSqlNombreEmployeurDecompteSimplifie();

        List<Map<String, String>> listMapResultQueryNombreEmployeurDecompteSimplifie = executeQuery(sqlQueryNombreEmployeurDecompteSimplifie);

        // La requête retourne une seule ligne
        Map<String, String> aMapRowResultQueryNombreEmployeurDecompteSimplifie = listMapResultQueryNombreEmployeurDecompteSimplifie
                .get(0);

        String nombreAff707 = aMapRowResultQueryNombreEmployeurDecompteSimplifie
                .get(AFStatistiquesOfasProcess.REQUETE_NOMBRE_EMPLOYEUR_DECOMPTE_SIMPLIFIE_COL_NAME_NOMBRE_EMPLOYEUR);

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_707_MAP_STAT_OFAS, nombreAff707);
    }

    private void compterEtSommerCotisationDecompteSimplifie() throws Exception {
        compterEmployeurDecompteSimplifie();
        compterEmployeDecompteSimplifie();
        sommerCotisationEmployeurDecompteSimplifie();
        compterExclusionDecompteSimplifie();

    }

    private void compterEtSommerCotisationIndTseWithRente() throws Exception {

        String sqlQueryCotiAndNombreIndTseWithRente = getSqlCotiAndNombreIndTseWithRente();
        List<Map<String, String>> listMapResultQueryCotiAndNombreIndTseWithRente = executeQuery(sqlQueryCotiAndNombreIndTseWithRente);

        Map<String, Double> mapSommeCotisationParAffilie = new HashMap<String, Double>();
        for (Map<String, String> aMapRowResultQueryCotiAndNombreIndTseWithRente : listMapResultQueryCotiAndNombreIndTseWithRente) {

            String numeroAffilie = aMapRowResultQueryCotiAndNombreIndTseWithRente
                    .get(AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_NUMERO_AFFILIE);

            String dateNaissance = aMapRowResultQueryCotiAndNombreIndTseWithRente
                    .get(AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_DATE_NAISSANCE);
            dateNaissance = formatDate(dateNaissance);
            String sexe = aMapRowResultQueryCotiAndNombreIndTseWithRente
                    .get(AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_SEXE);

            String anneeCotisationString = aMapRowResultQueryCotiAndNombreIndTseWithRente
                    .get(AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_ANNEE);
            int anneeCotisation = Integer.valueOf(anneeCotisationString).intValue();
            int anneeStatistique = Integer.valueOf(annee).intValue();

            String dateFinPeriode = aMapRowResultQueryCotiAndNombreIndTseWithRente
                    .get(AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_DATE_FIN_PERIODE);
            dateFinPeriode = formatDate(dateFinPeriode);

            String dateDebutPeriode = aMapRowResultQueryCotiAndNombreIndTseWithRente
                    .get(AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_DATE_DEBUT_PERIODE);
            dateDebutPeriode = formatDate(dateDebutPeriode);

            String cotisationString = aMapRowResultQueryCotiAndNombreIndTseWithRente
                    .get(AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_COTISATION);
            cotisationString = replaceDecimalCommaByDecimalPoint(cotisationString);
            double cotisation = Double.valueOf(cotisationString).doubleValue();

            String dateDebutDroitRenteAVS = getDateDebutDroitRenteAVS(dateNaissance, sexe);

            if ((JadeDateUtil.isDateAfter(dateDebutPeriode, dateDebutDroitRenteAVS))
                    || BSessionUtil.compareDateBetweenOrEqual(getSession(), dateDebutPeriode, dateFinPeriode,
                            dateDebutDroitRenteAVS)) {
                double sommeCotisationAffilie = 0;
                if (mapSommeCotisationParAffilie.get(numeroAffilie) != null) {
                    sommeCotisationAffilie = mapSommeCotisationParAffilie.get(numeroAffilie);
                }
                sommeCotisationAffilie = sommeCotisationAffilie + cotisation;

                mapSommeCotisationParAffilie.put(numeroAffilie, sommeCotisationAffilie);
            }
        }

        int nombreAff016 = 0;
        double coti044 = 0;
        for (Map.Entry<String, Double> aEntrySommeCotisationParAffilie : mapSommeCotisationParAffilie.entrySet()) {

            if (aEntrySommeCotisationParAffilie.getValue() != 0) {
                nombreAff016++;
            }

            coti044 = coti044 + aEntrySommeCotisationParAffilie.getValue();

        }

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_044_MAP_STAT_OFAS, new FWCurrency(coti044).toStringFormat());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_016_MAP_STAT_OFAS, String.valueOf(nombreAff016));

    }

    private void compterEtSommerCotisationSalarieWithRente() throws Exception {

        String sqlQueryCotiAndNombreSalarieWithRente = getSqlCotiAndNombreSalarieWithRente();
        List<Map<String, String>> listMapResultQueryCotiAndNombreSalarieWithRente = executeQuery(sqlQueryCotiAndNombreSalarieWithRente);

        int nombreAff018 = 0;
        double coti045 = 0;
        for (Map<String, String> aMapRowResultQueryCotiAndNombreSalarieWithRente : listMapResultQueryCotiAndNombreSalarieWithRente) {

            String nombreSalarieWithRenteString = aMapRowResultQueryCotiAndNombreSalarieWithRente
                    .get(AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_SALARIE_WITH_RENTE_COL_NAME_NOMBRE_AFFILIE);

            int nombreSalarieWithRente = Integer.valueOf(nombreSalarieWithRenteString).intValue();

            String revenuString = aMapRowResultQueryCotiAndNombreSalarieWithRente
                    .get(AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_SALARIE_WITH_RENTE_COL_NAME_REVENU);
            revenuString = replaceDecimalCommaByDecimalPoint(revenuString);
            double revenu = Double.valueOf(revenuString);

            String anneeCotisation = aMapRowResultQueryCotiAndNombreSalarieWithRente
                    .get(AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_SALARIE_WITH_RENTE_COL_NAME_ANNEE_COTISATION);

            double tauxAssuranceAVS = findTauxAssuranceAVS("01.01." + anneeCotisation);
            double cotiAVS = (revenu * tauxAssuranceAVS) / 100;

            cotiAVS = JANumberFormatter.round(cotiAVS, 0.05, 2, JANumberFormatter.NEAR);

            coti045 = coti045 + cotiAVS;
            nombreAff018 = nombreAff018 + nombreSalarieWithRente;
        }

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_045_MAP_STAT_OFAS, new FWCurrency(coti045).toStringFormat());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_018_MAP_STAT_OFAS, String.valueOf(nombreAff018));

    }

    private void compterExclusionDecompteSimplifie() throws Exception {

        String sqlQueryNombreExclusionDecompteSimplifie = getSqlNombreExclusionDecompteSimplifie();

        List<Map<String, String>> listMapResultQueryNombreExclusionDecompteSimplifie = executeQuery(sqlQueryNombreExclusionDecompteSimplifie);

        // La requête retourne une seule ligne
        Map<String, String> aMapRowResultQueryNombreExclusionDecompteSimplifie = listMapResultQueryNombreExclusionDecompteSimplifie
                .get(0);

        String nombreAff712 = aMapRowResultQueryNombreExclusionDecompteSimplifie
                .get(AFStatistiquesOfasProcess.REQUETE_NOMBRE_EXCLUSION_DECOMPTE_SIMPLIFIE_COL_NAME_NOMBRE_EXCLUSION);

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_712_MAP_STAT_OFAS, nombreAff712);
    }

    private void compterNombreControleEmployeurEffectue() throws Exception {
        String sqlQueryCompterNombreControleEmployeurEffectue = getSqlComptageControleEmployeurEffectue();

        List<Map<String, String>> listMapResultQueryCompterNombreControleEmployeurEffectue = executeQuery(sqlQueryCompterNombreControleEmployeurEffectue);

        // La requête retourne une seule ligne
        Map<String, String> aMapRowResultQueryCompterNombreControleEmployeurEffectue = listMapResultQueryCompterNombreControleEmployeurEffectue
                .get(0);

        String nombreControleEmployeurEffectue = aMapRowResultQueryCompterNombreControleEmployeurEffectue
                .get(AFStatistiquesOfasProcess.REQUETE_COMPTAGE_CONTROLE_EMPLOYEUR_EFFECTUE_COL_NAME_NOMBRE_CONTROLE);

        mapStatControleLAALPP.put(
                AFStatistiquesOfasProcess.KEY_NOMBRE_CONTROLE_EMPLOYEUR_EFFECTUE_MAP_STAT_CONTROLE_LAA_LPP,
                nombreControleEmployeurEffectue);
    }

    private void compterNombreDeclarationSalaireRetourne() throws Exception {
        String sqlQueryCompterNombreDeclarationSalaireRetourne = getSqlComptageDocumentEnvoyeRetourne("6700003",
                "6200017");

        List<Map<String, String>> listMapResultQueryCompterNombreDeclarationSalaireRetourne = executeQuery(sqlQueryCompterNombreDeclarationSalaireRetourne);

        // La requête retourne une seule ligne
        Map<String, String> aMapRowResultQueryCompterNombreDeclarationSalaireRetourne = listMapResultQueryCompterNombreDeclarationSalaireRetourne
                .get(0);

        String nombreDeclarationSalaireRetourne = aMapRowResultQueryCompterNombreDeclarationSalaireRetourne
                .get(AFStatistiquesOfasProcess.REQUETE_COMPTAGE_DOCUMENT_ENVOYE_RETOURNE_COL_NAME_NOMBRE_DOCUMENT);

        mapStatControleLAALPP.put(
                AFStatistiquesOfasProcess.KEY_NOMBRE_DECLARATION_SALAIRE_RETOURNE_MAP_STAT_CONTROLE_LAA_LPP,
                nombreDeclarationSalaireRetourne);
    }

    private void compterNombreQuestionnaireLAARetourne() throws Exception {
        String sqlQueryCompterNombreQuestionnaireLAARetourne = getSqlComptageDocumentEnvoyeRetourne("6700001",
                "6200002");

        List<Map<String, String>> listMapResultQueryCompterNombreQuestionnaireLAARetourne = executeQuery(sqlQueryCompterNombreQuestionnaireLAARetourne);

        // La requête retourne une seule ligne
        Map<String, String> aMapRowResultQueryCompterNombreQuestionnaireLAARetourne = listMapResultQueryCompterNombreQuestionnaireLAARetourne
                .get(0);

        String nombreQuestionnaireLAARetourne = aMapRowResultQueryCompterNombreQuestionnaireLAARetourne
                .get(AFStatistiquesOfasProcess.REQUETE_COMPTAGE_DOCUMENT_ENVOYE_RETOURNE_COL_NAME_NOMBRE_DOCUMENT);

        mapStatControleLAALPP.put(
                AFStatistiquesOfasProcess.KEY_NOMBRE_QUESTIONNAIRE_LAA_RETOURNE_MAP_STAT_CONTROLE_LAA_LPP,
                nombreQuestionnaireLAARetourne);
    }

    private void compterNombreQuestionnaireLPPRetourne() throws Exception {
        String sqlQueryCompterNombreQuestionnaireLPPRetourne = getSqlComptageDocumentEnvoyeRetourne("6700002",
                "6200008");

        List<Map<String, String>> listMapResultQueryCompterNombreQuestionnaireLPPRetourne = executeQuery(sqlQueryCompterNombreQuestionnaireLPPRetourne);

        // La requête retourne une seule ligne
        Map<String, String> aMapRowResultQueryCompterNombreQuestionnaireLPPRetourne = listMapResultQueryCompterNombreQuestionnaireLPPRetourne
                .get(0);

        String nombreQuestionnaireLPPRetourne = aMapRowResultQueryCompterNombreQuestionnaireLPPRetourne
                .get(AFStatistiquesOfasProcess.REQUETE_COMPTAGE_DOCUMENT_ENVOYE_RETOURNE_COL_NAME_NOMBRE_DOCUMENT);

        mapStatControleLAALPP.put(
                AFStatistiquesOfasProcess.KEY_NOMBRE_QUESTIONNAIRE_LPP_RETOURNE_MAP_STAT_CONTROLE_LAA_LPP,
                nombreQuestionnaireLPPRetourne);
    }

    private void creerDocumentStatistiqueControleLAALPPCSV() throws Exception {

        creerListCSVLineControleLAALPP();

        String filePath = Jade.getInstance().getPersistenceDir()
                + JadeFilenameUtil
                        .addFilenameSuffixUID(AFStatistiquesOfasProcess.CSV_STATISTIQUE_CONTROLE_LAA_LPP_OUTPUT_FILE_NAME);

        JadeFsFacade.writeFile(getLignesInByteFormat(listCSVLineControleLAALPP), filePath);

        publierDocumentStatistiqueControleLAALPPCSV(filePath);
    }

    private void creerDocumentStatistiqueOfasCSV() throws Exception {

        creerListCSVLine();

        String filePath = Jade.getInstance().getPersistenceDir()
                + JadeFilenameUtil
                        .addFilenameSuffixUID(AFStatistiquesOfasProcess.CSV_STATISTIQUE_OFAS_OUTPUT_FILE_NAME);

        JadeFsFacade.writeFile(getLignesInByteFormat(listCSVLine), filePath);

        publierDocumentStatistiqueOfasCSV(filePath);
    }

    private void creerListCSVLine() {

        addALineInListCSVLine(
                getSession().getLabel(AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "POSTE_STATISTIQUE"),
                getSession().getLabel(AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "VALEUR"));
        addALineInListCSVLine(
                getSession().getLabel(AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "COTISATIONS"), " ");
        addALineInListCSVLine(
                getSession().getLabel(AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "AFFILIES"), " ");
        addASuiteStatistiqueLineInListCSVLine(31, 33, true);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_701_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_715_MAP_STAT_OFAS);
        addASuiteStatistiqueLineInListCSVLine(35, 37, true);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_716_MAP_STAT_OFAS);
        addASuiteStatistiqueLineInListCSVLine(38, 39, true);
        addALineInListCSVLine(
                getSession().getLabel(
                        AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "REPARTITION_AFFILIE_PAR_CANTON"),
                " ");
        addASuiteStatistiqueLineInListCSVLine(102, 127, false);

        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_128_MAP_STAT_OFAS);

        addALineInListCSVLine(
                getSession().getLabel(
                        AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "OBLIGATION_COTISER_AC"), " ");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_140_MAP_STAT_OFAS);

        addALineInListCSVLine(
                getSession().getLabel(
                        AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "COTISATION_AVS_AI_APG"), " ");
        addASuiteStatistiqueLineInListCSVLine(703, 705, false);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_726_MAP_STAT_OFAS);

        addALineInListCSVLine(
                getSession().getLabel(
                        AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART
                                + "COTISATION_AVS_AI_APG_RENTE_VIEILLESSE"), " ");
        addALineInListCSVLine(
                getSession().getLabel(
                        AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "COTISATION_INDEPENDANT_TSE"), " ");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_044_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_016_MAP_STAT_OFAS);

        addALineInListCSVLine(
                getSession()
                        .getLabel(AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "COTISATION_SALARIES"),
                " ");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_045_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_018_MAP_STAT_OFAS);

        addALineInListCSVLine(
                getSession().getLabel(AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "DECOMPTE_SIMPLIFIE"),
                " ");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_707_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_708_MAP_STAT_OFAS, anneeStatMoinsUn);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_709_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_712_MAP_STAT_OFAS);

        addALineInListCSVLine(
                getSession().getLabel(AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "SURSIS_PAIEMENT"),
                " ");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_056_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_055_MAP_STAT_OFAS);
        addAHorsStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_TOTAL_SURSIS_PAIEMENT_MAP_INFO_HORS_STAT_OFAS);

        addALineInListCSVLine(
                getSession().getLabel(
                        AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "REQUISITION_POURSUITE"), " ");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_058_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_057_MAP_STAT_OFAS);
        addAHorsStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_TOTAL_REQUISITION_POURSUITE_MAP_INFO_HORS_STAT_OFAS);

        addALineInListCSVLine(
                getSession().getLabel(
                        AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "REQUISITION_CONTINUER_POURSUITE"),
                " ");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_710_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_711_MAP_STAT_OFAS);
        addAHorsStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_TOTAL_REQUISITION_CONTINUER_POURSUITE_MAP_INFO_HORS_STAT_OFAS);

        addALineInListCSVLine(
                getSession().getLabel(AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "REPARATION_DOMMAGE"),
                " ");
        addALineInListCSVLine(
                getSession().getLabel(
                        AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART
                                + "REPARATION_DOMMAGE_PAS_DETERMINEE_AUTOMATIQUEMENT"), " ");
        addALineInListCSVLine(
                getSession().getLabel(
                        AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART
                                + "REPARATION_DOMMAGE_DONNEE_INDICATIVE"), " ");
        addAHorsStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_NOMBRE_CREANCE_REPARATION_DOMMAGE_MAP_INFO_HORS_STAT_OFAS);
        addAHorsStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_TOTAL_CREANCE_REPARATION_DOMMAGE_MAP_INFO_HORS_STAT_OFAS);

        addALineInListCSVLine(
                getSession().getLabel(AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "PRESTATION"), " ");

        addALineInListCSVLine(
                getSession().getLabel(
                        AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "MODE_VERSEMENT_RENTE"), " ");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_061_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_062_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_063_MAP_STAT_OFAS);

        addALineInListCSVLine(
                getSession().getLabel(
                        AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "RENTE_VIEILLESSE_AJOURNEE"), " ");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_066_MAP_STAT_OFAS, annee);

        int anneeInt = Integer.valueOf(annee).intValue();
        String anneeMoins1String = String.valueOf(anneeInt - 1);
        String anneeMoins2String = String.valueOf(anneeInt - 2);
        String anneeMoins3String = String.valueOf(anneeInt - 3);
        String anneeMoins4String = String.valueOf(anneeInt - 4);

        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_067_MAP_STAT_OFAS, anneeMoins1String, "1");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_068_MAP_STAT_OFAS, anneeMoins2String, "2");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_069_MAP_STAT_OFAS, anneeMoins3String, "3");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_070_MAP_STAT_OFAS, anneeMoins4String, "4");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_071_MAP_STAT_OFAS);

        addALineInListCSVLine(
                getSession().getLabel(
                        AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "CALCUL_ANTICIPE_RENTE"), " ");

        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_306_MAP_STAT_OFAS);

        addALineInListCSVLine(
                getSession().getLabel(
                        AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "INTERET_MORATOIRE_PRESTATIONS"),
                " ");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_308_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_309_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_321_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_307_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_310_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_322_MAP_STAT_OFAS);

        addALineInListCSVLine(getSession().getLabel(AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "APG"),
                " ");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_075_MAP_STAT_OFAS);

        addALineInListCSVLine(
                getSession()
                        .getLabel(
                                AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART
                                        + "ALLOCATION_FAMILIALES_AGRICULTURE"), " ");
        addALineInListCSVLine(
                getSession().getLabel(
                        AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "TRAVAILLEUR_AGRICOLE_PLAINE"), " ");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_082_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_182_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_601_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_602_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_603_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_084_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_605_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_606_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_607_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_604_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_083_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_608_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_609_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_611_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_612_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_613_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_610_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_615_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_616_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_617_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_614_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_618_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_619_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_620_MAP_STAT_OFAS);

        addALineInListCSVLine(
                getSession().getLabel(
                        AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "TRAVAILLEUR_AGRICOLE_MONTAGNE"),
                " ");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_085_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_184_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_621_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_622_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_623_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_087_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_625_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_626_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_627_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_624_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_086_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_628_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_629_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_631_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_632_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_633_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_630_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_635_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_636_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_637_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_634_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_638_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_639_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_640_MAP_STAT_OFAS);

        addALineInListCSVLine(
                getSession().getLabel(
                        AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "AGRICULTEUR_PRINCIPAL_PLAINE"),
                " ");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_088_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_730_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_641_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_642_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_643_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_186_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_645_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_646_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_647_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_644_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_648_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_649_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_651_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_652_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_653_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_650_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_655_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_656_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_657_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_654_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_658_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_659_MAP_STAT_OFAS);

        addALineInListCSVLine(
                getSession().getLabel(
                        AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "AGRICULTEUR_PRINCIPAL_MONTAGNE"),
                " ");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_090_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_731_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_660_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_661_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_662_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_187_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_664_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_665_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_666_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_663_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_667_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_668_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_670_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_671_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_672_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_669_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_674_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_675_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_676_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_673_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_677_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_678_MAP_STAT_OFAS);

        addALineInListCSVLine(
                getSession().getLabel(
                        AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "AGRICULTEUR_ACCESSOIRE_PLAINE"),
                " ");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_092_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_732_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_733_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_734_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_735_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_736_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_737_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_738_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_739_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_740_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_741_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_742_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_743_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_744_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_745_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_746_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_747_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_748_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_749_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_750_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_751_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_752_MAP_STAT_OFAS);

        addALineInListCSVLine(
                getSession().getLabel(
                        AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "AGRICULTEUR_ACCESSOIRE_MONTAGNE"),
                " ");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_093_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_753_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_754_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_755_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_756_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_757_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_758_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_759_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_760_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_761_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_762_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_763_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_764_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_765_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_766_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_767_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_768_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_769_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_770_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_771_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_772_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_773_MAP_STAT_OFAS);

        addALineInListCSVLine(
                getSession().getLabel(AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "EXPLOITANT_ALPAGE"),
                " ");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_094_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_774_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_775_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_776_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_777_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_192_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_778_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_779_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_780_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_683_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_684_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_685_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_781_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_782_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_783_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_686_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_784_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_785_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_786_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_687_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_688_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_689_MAP_STAT_OFAS);

        addALineInListCSVLine(
                getSession().getLabel(AFStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "PECHEUR"), " ");
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_096_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_787_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_788_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_789_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_790_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_193_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_791_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_792_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_793_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_690_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_691_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_692_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_794_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_795_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_796_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_693_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_797_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_798_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_799_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_694_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_695_MAP_STAT_OFAS);
        this.addAStatistiqueLineInListCSVLine(AFStatistiquesOfasProcess.KEY_696_MAP_STAT_OFAS);

    }

    private void creerListCSVLineControleLAALPP() {

        addALineInListControleLAALPPCSVLine(getSession().getLabel("STATISTIQUE_CONTROLE_LAA_LPP_POSTE_STATISTIQUE"),
                getSession().getLabel("STATISTIQUE_CONTROLE_LAA_LPP_VALEUR"));
        addALineInListControleLAALPPCSVLine(
                getSession().getLabel("STATISTIQUE_CONTROLE_LAA_LPP_NOMBRE_QUESTIONNAIRE_LAA_RETOURNE"),
                mapStatControleLAALPP
                        .get(AFStatistiquesOfasProcess.KEY_NOMBRE_QUESTIONNAIRE_LAA_RETOURNE_MAP_STAT_CONTROLE_LAA_LPP));
        addALineInListControleLAALPPCSVLine(
                getSession().getLabel("STATISTIQUE_CONTROLE_LAA_LPP_NOMBRE_QUESTIONNAIRE_LPP_RETOURNE"),
                mapStatControleLAALPP
                        .get(AFStatistiquesOfasProcess.KEY_NOMBRE_QUESTIONNAIRE_LPP_RETOURNE_MAP_STAT_CONTROLE_LAA_LPP));
        addALineInListControleLAALPPCSVLine(
                getSession().getLabel("STATISTIQUE_CONTROLE_LAA_LPP_NOMBRE_DECLARATION_SALAIRE_RETOURNEE"),
                mapStatControleLAALPP
                        .get(AFStatistiquesOfasProcess.KEY_NOMBRE_DECLARATION_SALAIRE_RETOURNE_MAP_STAT_CONTROLE_LAA_LPP));
        addALineInListControleLAALPPCSVLine(
                getSession().getLabel("STATISTIQUE_CONTROLE_LAA_LPP_NOMBRE_CONTROLE_EMPLOYEUR_EFFECTUE"),
                mapStatControleLAALPP
                        .get(AFStatistiquesOfasProcess.KEY_NOMBRE_CONTROLE_EMPLOYEUR_EFFECTUE_MAP_STAT_CONTROLE_LAA_LPP));

    }

    private List<Map<String, String>> executeQuery(String sql) throws JadePersistenceException {

        Statement stmt = null;
        ResultSet resultSet = null;
        List<Map<String, String>> results = new ArrayList<Map<String, String>>();

        try {
            stmt = JadeThread.currentJdbcConnection().createStatement();
            resultSet = stmt.executeQuery(sql);

            ResultSetMetaData md = resultSet.getMetaData();
            int columns = md.getColumnCount();

            while (resultSet.next()) {
                Map<String, String> row = new HashMap<String, String>();

                // Attention ! La première colonne du Resultset est 1 et non 0
                for (int i = 1; i <= columns; i++) {
                    row.put(md.getColumnName(i), resultSet.getString(i));
                }

                results.add(row);
            }

        } catch (SQLException e) {
            throw new JadePersistenceException(getName() + " - " + "Unable to execute query (" + sql
                    + "), a SQLException happend!", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    JadeLogger.warn(AFStatistiquesOfasProcess.class,
                            "Problem to close statement in AFStatistiquesOfasProcess, reason : " + e.toString());
                }
            }

        }

        return results;
    }

    private String extractAAAAFromDateAAAAMM(String dateAAAAMM) {
        return dateAAAAMM.substring(0, 4);
    }

    private double findTauxAssuranceAVS(String theDate) throws Exception {

        AFAssuranceManager assuranceManager = new AFAssuranceManager();
        assuranceManager.setSession(getSession());
        assuranceManager.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
        assuranceManager.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
        assuranceManager.find();

        AFAssurance assuranceEntity = (AFAssurance) assuranceManager.getFirstEntity();

        AFTauxAssuranceManager tauxManager = new AFTauxAssuranceManager();
        tauxManager.setSession(getSession());
        tauxManager.setForIdAssurance(assuranceEntity.getAssuranceId());
        tauxManager.setForGenreValeur(CodeSystem.GEN_VALEUR_ASS_TAUX);
        tauxManager.setForDate(theDate);
        tauxManager.setOrder(" MCDDEB DESC ");
        tauxManager.find();

        // Le find taux ci-dessus retourne tous les taux qui ont une date de début <= à la date reçue en paramètre
        // Les taux retournés sont ordrés par date de début décroissante
        // Le taux valable à la date reçue en paramètre est donc le premier enregistrement
        AFTauxAssurance taux = (AFTauxAssurance) tauxManager.getFirstEntity();

        return Double.valueOf(taux.getValeurTotal()).doubleValue();

    }

    /**
     * 
     * Transforme une date au format aaaammjj en une date au format jj.mm.yyyy
     */
    private String formatDate(String dateAMJ) {

        String dateJMA = dateAMJ.substring(6, 8) + "." + dateAMJ.substring(4, 6) + "." + dateAMJ.substring(0, 4);

        return dateJMA;
    }

    public String getAnnee() {
        return annee;
    }

    public String getCalculAnticipeRente(String annee, BSession session) throws Exception {
        this.annee = annee;
        setSession(session);

        String errorMessage = "";

        try {

            codeCantonCaisse = GlobazServer.getCurrentSystem().getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)
                    .getProperty(CommonProperties.KEY_CANTON_CAISSE);

            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = initThreadContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            calculAnticipeRente();

        } catch (Exception e) {

            errorMessage = e.toString();
        } finally {
            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        if (isAborted() || isOnError() || getSession().hasErrors()) {

            getMemoryLog().logMessage(errorMessage, FWMessage.FATAL, this.getClass().getName());
            this._addError(getTransaction(), errorMessage);
        }

        return mapStatOfas.get(AFStatistiquesOfasProcess.KEY_306_MAP_STAT_OFAS);
    }

    private String getDateDebutDroitRenteAVS(String dateNaissance, String sexe) throws Exception {

        TIPersonne thePersonne = new TIPersonne();

        thePersonne.setSession(getSession());
        thePersonne.setDateNaissance(dateNaissance);
        thePersonne.setSexe(sexe);

        return thePersonne.getDateAvs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("STAT_TITRE_MAIL_ERROR");
        } else {
            return getSession().getLabel("STAT_TITRE_MAIL");
        }
    }

    public String getIdTypeAdresse() {
        return idTypeAdresse;
    }

    private List<Byte> getLignesInByteFormat(List<String> lignes) {
        List<Byte> lignesInByteFormat = new ArrayList<Byte>();

        if (lignes != null) {
            for (String aLigne : lignes) {
                for (byte aByte : aLigne.getBytes()) {
                    lignesInByteFormat.add(Byte.valueOf(aByte));
                }
            }
        }

        return lignesInByteFormat;
    }

    private String getPartSqlModeVersementRente() {

        String sql = " select count(*) as "
                + AFStatistiquesOfasProcess.REQUETE_MODE_VERSEMENT_RENTE_COL_NAME_NOMBRE_VERSEMENT
                + " from schema.caorgrp og "
                + " inner join schema.caopovp ov on (og.IDORDREGROUPE = ov.IDORDREGROUPE) "
                + " inner join schema.caoperp op on (ov.IDORDRE = op.IDOPERATION) "
                + " inner join schema.tiapaip ap on (ov.IDADRESSEPAIEMENT = ap.HCIAIU) "
                + " inner join schema.tiadrpp adp on (ap.HIIAPA = adp.HIIAPA) " + " where og.DATEECHEANCE between "
                + annee + "1101 and " + annee + "1130 " + " and og.NATUREORDRESLIVRES = 209005 "
                + " and og.NUMEROOG in (1, 2) " + " and og.etat=208005 " + " and op.etat in (205002, 205006) ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    private String getSqlAFAgricole() {

        String sqlInValuesTypeActivite = getSqlInValuesFromListForNumericColumn(listTypeActiviteAFAgriculture);
        String sqlInValuesTypeAllocation = getSqlInValuesFromListForNumericColumn(listTypeAllocationAFAgriculture);

        String sql = " SELECT cstype as " + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_TYPE_ALLOCATION
                + ", enf.hnipay as " + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_PAYS_ENFANT
                + ", tia.hnipay as " + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_PAYS_ALLOCATAIRE
                + ", dos.cscaal as " + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_TYPE_ACTIVITE
                + ", cscata as " + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_TYPE_LOI
                + ", numcpt, ent.eid as " + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_ID_ENTETE_PRESTATION
                + ", eactacc as " + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_ACTIVITE_ACCESSOIRE
                + ", ent.cstatu as " + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_GENRE_ALLOCATION
                + ", sum(nmont) as " + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_MONTANT_ALLOCATION
                + ", sum(case when nmont > 0 and ((det.NVALID = " + annee + "07 and cscaal=61040006) or (det.NVALID= "
                + annee + "12 and cscaal <> 61040006)) then 1 else 0 end) as "
                + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_NOMBRE_ALLOCATION
                + " FROM SCHEMA.aldetpre det " + " LEFT OUTER JOIN SCHEMA.alentpre ent ON ent.mid = det.mid "
                + " LEFT OUTER JOIN SCHEMA.aldos dos ON dos.eid = ent.eid "
                + " LEFT OUTER JOIN SCHEMA.aldroit dro ON dro.fid = det.fid "
                + " LEFT OUTER JOIN SCHEMA.alenfant enf ON enf.cid = dro.cid "
                + " LEFT OUTER JOIN SCHEMA.titierp ti ON ti.htitie = enf.htitie "
                + " LEFT OUTER JOIN SCHEMA.alalloc alloc ON alloc.bid = dos.bid "
                + " LEFT OUTER JOIN SCHEMA.titierp tia on tia.htitie=alloc.htitie " + " WHERE dos.cscaal IN("
                + sqlInValuesTypeActivite + ") AND cstype IN(" + sqlInValuesTypeAllocation + ") AND mdvc BETWEEN "
                + annee + "0101 AND " + annee + "1231 ";

        if (isSansRestitution()) {
            sql += " AND ( numCpt not like '____.46%' ) ";
        }

        sql += " group by cstype,enf.hnipay,tia.hnipay,dos.cscaal,cscata,numcpt,ent.eid,etocc,ent.cstatu, eactacc "
                + " order by ent.eid ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    private String getSqlAFAgricoleNew() {

        String sqlInValuesTypeActivite = getSqlInValuesFromListForNumericColumn(listTypeActiviteAFAgriculture);
        String sqlInValuesTypeAllocation = getSqlInValuesFromListForNumericColumn(listTypeAllocationAFAgriculture);

        String sql = " SELECT cstype as " + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_TYPE_ALLOCATION
                + ", enf.hnipay as " + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_PAYS_ENFANT
                + ", tia.hnipay as " + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_PAYS_ALLOCATAIRE
                + ", dos.cscaal as " + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_TYPE_ACTIVITE
                + ", cscata as " + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_TYPE_LOI
                + ", numcpt, ent.eid as " + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_ID_ENTETE_PRESTATION
                + ", eactacc as " + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_ACTIVITE_ACCESSOIRE
                + ", ent.cstatu as " + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_GENRE_ALLOCATION
                + ", sum(nmont) as " + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_MONTANT_ALLOCATION
                + ", count(DISTINCT(dro.FID)) as "
                + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_NOMBRE_ALLOCATION + ", enf.cid as "
                + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_ID_ENFANT + ", dos.MALNAF as "
                + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_NUM_AFFILIE + ", alloc.BID as "
                + AFStatistiquesOfasProcess.REQUETE_AF_AGRICOLE_COL_NAME_ID_ALLOCATAIRE + " FROM SCHEMA.aldetpre det "
                + " LEFT OUTER JOIN SCHEMA.alentpre ent ON ent.mid = det.mid "
                + " LEFT OUTER JOIN SCHEMA.aldos dos ON dos.eid = ent.eid "
                + " LEFT OUTER JOIN SCHEMA.aldroit dro ON dro.fid = det.fid "
                + " LEFT OUTER JOIN SCHEMA.alenfant enf ON enf.cid = dro.cid "
                + " LEFT OUTER JOIN SCHEMA.titierp ti ON ti.htitie = enf.htitie "
                + " LEFT OUTER JOIN SCHEMA.alalloc alloc ON alloc.bid = dos.bid "
                + " LEFT OUTER JOIN SCHEMA.titierp tia on tia.htitie=alloc.htitie " + " WHERE dos.cscaal IN("
                + sqlInValuesTypeActivite + ") AND cstype IN(" + sqlInValuesTypeAllocation + ") AND mdvc BETWEEN "
                + annee + "0101 AND " + annee + "1231 ";

        if (isSansRestitution()) {
            sql += " AND ( numCpt not like '____.46%' ) ";
        }

        sql += " group by cstype,enf.hnipay,tia.hnipay,dos.cscaal,cscata,numcpt,ent.eid,etocc,ent.cstatu, eactacc, enf.cid, dos.MALNAF, alloc.BID"
                + " order by ent.eid ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    private String getSqlAPG() {

        String sql = " select count(*) as "
                + AFStatistiquesOfasProcess.REQUETE_APG_COL_NAME_NOMBRE_APG
                + " from schema.APDROIP as droit inner join schema.APSIPRP as situ_prof  on (droit.VAIDRO = situ_prof.VFIDRO) "
                + " where VADDEP between " + annee + "0101 and " + annee + "1231 and VFBCOA = '1' and VFBAEX = '1' ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    /**
     * Le chiffre 038 de la statistique OFAS contient le nombre d'affiliés pour lesquels aucune cotisation n'a été
     * enregistrée
     */
    private String getSqlComptageAffilie038() {

        String sql = " select loc.hjican as "
                + AFStatistiquesOfasProcess.REQUETE_COMPTAGE_AFFILIE_38_COL_NAME_CODE_CANTON + ", aff.htitie "
                + " from schema.afaffip aff "
                + " inner join schema.tiaadrp aad on (aff.htitie=aad.htitie and aad.hedfad = 0 and aad.hettad="
                + idTypeAdresse + " and aad.hfiapp=519004) "
                + " inner join schema.tiadrep adr on (aad.haiadr=adr.haiadr) "
                + " inner join schema.tilocap loc on (adr.HJILOC = loc.HJILOC) "
                + " inner join schema.afplafp pla on (aff.maiaff=pla.maiaff) "
                + " inner join schema.afcotip cot on (pla.muipla=cot.muipla) "
                + " inner join schema.afassup ass on (cot.mbiass=ass.mbiass) " + " where maddeb < " + anneeStatPlusUn
                + "0000 " + " and (madfin = 0 or madfin > " + annee + "1231) "
                + " and mattaf in (804001, 804002, 804004, 804005, 804006, 804011, 804012) "
                + " and ass.MBTTYP = 812001 " + " and cot.meddeb < " + anneeStatPlusUn + "0000 "
                + " and (cot.medfin = 0 or cot.medfin > " + annee + "1231) " + " and pla.mubina = '2' "
                + " and aff.htitie not in ( " + " select idtiers " + " from schema.caoperp op "
                + " inner join schema.cajourp jo on (op.idjournal=jo.idjournal) "
                + " inner join schema.cacptap ca on (op.IDCOMPTEANNEXE = ca.idcompteannexe) "
                + " inner join schema.carubrp ru on (op.idcompte=ru.idrubrique) " + " where jo.datevaleurcg between "
                + annee + "0101 and " + annee + "1231 " + " and op.etat=205002 "
                + " and ca.idrole in (517002, 517039, 517040) "
                + " and (ru.idexterne like '211_.4010.%' or ru.idexterne like '211_.4000.%') " + " group by idtiers "
                + " having sum(op.montant)<>  0 " + " ) " + " group by loc.hjican, aff.htitie ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    private String getSqlComptageAffilieObligeCotiserAC() {

        String sql = " SELECT count(*) as "
                + AFStatistiquesOfasProcess.REQUETE_COMPTAGE_AFFILIE_OBLIGE_COTISER_AC_COL_NAME_NOMBRE_AFFILIE
                + " from schema.afaffip aff " + " where maddeb < " + anneeStatPlusUn + "0000 "
                + " and (madfin = 0 or madfin > " + annee + "1231) " + " and mattaf=804011 ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    private String getSqlComptageAffilieSommeCotisation(String theAnnee) {
        String sql = " select " + " idtiers, "
                + " sum(case when ru.idexterne like '%.4010.%' then op.montant else 0 end) as "
                + AFStatistiquesOfasProcess.REQUETE_COMPTAGE_AFFILIE_COL_NAME_COTI_AVS_PAR
                + ", "
                + " sum(case when ru.idexterne like '%.4000.%' then op.montant else 0 end) as "
                + AFStatistiquesOfasProcess.REQUETE_COMPTAGE_AFFILIE_COL_NAME_COTI_AVS_PERS
                + ", "
                + "(select max(loc.hjican) "
                + "from schema.tiaadrp aad "
                + "inner join schema.tiadrep adr on (aad.HAIADR=adr.HAIADR) "
                + "inner join schema.tilocap loc on (adr.HJILOC=loc.HJILOC) "
                + "where aad.htitie=ca.idtiers "
                + "and aad.hedfad = 0 "
                + "and aad.hettad = "
                + idTypeAdresse
                + " and aad.hfiapp = 519004 "
                + ") as "
                + AFStatistiquesOfasProcess.REQUETE_COMPTAGE_AFFILIE_COL_NAME_CODE_CANTON
                + ", "
                + " (select MAX(matbra) "
                + " from schema.afaffip aff where aff.htitie=ca.idtiers and (matbra = 805038 or (matbra=805044 and mattaf=804004)) and maddeb = (select MAX(maddeb) from schema.afaffip aff2 where aff2.htitie=ca.idtiers and (matbra = 805038 or (matbra=805044 and mattaf=804004))) ) as "
                + AFStatistiquesOfasProcess.REQUETE_COMPTAGE_AFFILIE_COL_NAME_BRA_ECO
                + ", "
                + " (select MAX(mattaf) "
                + " from schema.afaffip aff where aff.htitie=ca.idtiers and mattaf in (804001, 804005, 804008, 804004, 804011) and maddeb = (select MAX(maddeb) from schema.afaffip aff2 where aff2.htitie=ca.idtiers and mattaf in (804001, 804005, 804008, 804004, 804011)) ) as "
                + AFStatistiquesOfasProcess.REQUETE_COMPTAGE_AFFILIE_COL_NAME_TYPE_AFFILIE
                + " "
                + " from schema.caoperp op "
                + " inner join schema.cajourp jo on (op.idjournal=jo.idjournal) "
                + " inner join schema.cacptap ca on (op.IDCOMPTEANNEXE = ca.idcompteannexe) "
                + " inner join schema.carubrp ru on (op.idcompte=ru.idrubrique) "
                + " where jo.datevaleurcg between "
                + theAnnee
                + "0101"
                + " and "
                + theAnnee
                + "1231"
                + " and op.etat=205002 "
                + " and ca.idrole in (517002, 517039, 517040) "
                + " and (ru.idexterne like '211_.4010.%' or ru.idexterne like '211_.4000.%') " + " group by idtiers";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    private String getSqlComptageCalculAnticipeRente() {

        String sql = " SELECT count(*) as " + AFStatistiquesOfasProcess.REQUETE_COMPTAGE_CALCUL_ANTICIPE_RENTE
                + " from schema.prinfcom " + " where wcdico > " + annee + "0000 " + " and wcdico < " + annee + "1300 "
                + " and wcmtax > 0 ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    private String getSqlComptageControleEmployeurEffectue() {

        String sql = " select count(*) as "
                + AFStatistiquesOfasProcess.REQUETE_COMPTAGE_CONTROLE_EMPLOYEUR_EFFECTUE_COL_NAME_NOMBRE_CONTROLE
                + " from schema.cecontp ce " + " where ce.MDDEFF between " + annee + "0101 and " + annee + "1231 ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    private String getSqlComptageDocumentEnvoyeRetourne(String suivi, String document) {

        String sql = " select count(*) as "
                + AFStatistiquesOfasProcess.REQUETE_COMPTAGE_DOCUMENT_ENVOYE_RETOURNE_COL_NAME_NOMBRE_DOCUMENT
                + " from schema.jojpjou jo " + " inner join schema.jojpgjo gj on (gj.JGJOID = jo.JGJOID) "
                + " inner join schema.jojpcjo cjs on (cjs.JJOUID = jo.JJOUID and cjs.JCJOTY=16000007 and cjs.JCJOVA="
                + suivi + ") "
                + " inner join schema.jojpcjo cje on (cje.jjouid = jo.JJOUID and cje.JCJOTY=16000002 and cje.JCJOVA="
                + document + ") " + " where gj.JGJORE between " + annee + "0101 and " + annee + "1231 ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    private String getSqlComptageIMCasAI() {

        String sql = "Select count(distinct(ywidem)) as "
                + AFStatistiquesOfasProcess.REQUETE_COMPTAGE_NOMBRE_IM_CAS_AI
                + " from schema.redecis as decisions "
                + "inner join schema.represt as prestations on decisions.YWIDEC = prestations.YUIDEC "
                + "inner join schema.reorver as ov on (ov.YVIPRE = prestations.YUIPRE and YVMMON != 0) "
                + "inner join schema.rederen as demande on demande.yaidem = decisions.YWIDEM "
                + "left join schema.redeapi as demande_api on (demande_api.yeirap = demande.yaidem and yattyd = 52800001) "
                + "left join schema.repeapi as periode_api on (demande_api.yeirap = periode_api.yhidem) "
                + "WHERE YWTETA=52837003 " + "AND YWDVAL<=" + annee + "1231 " + "AND YWDVAL>=" + annee + "0101 "
                + "and ov.YVTTYP = 52842010 " + "and ( " + "yattyd in (52800002) " + "or( " + "yattyd = 52800001 "
                + "and " + "yhtgdr in(52809002,52809001) " + ") " + ")";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    private String getSqlComptageIMCasAVS() {

        String sql = "Select count(distinct(ywidem)) as "
                + AFStatistiquesOfasProcess.REQUETE_COMPTAGE_NOMBRE_IM_CAS_AVS
                + " from schema.redecis as decisions "
                + "inner join schema.represt as prestations on decisions.YWIDEC = prestations.YUIDEC "
                + "inner join schema.reorver as ov on (ov.YVIPRE = prestations.YUIPRE and YVMMON != 0) "
                + "inner join schema.rederen as demande on demande.yaidem = decisions.YWIDEM "
                + "left join schema.redeapi as demande_api on (demande_api.yeirap = demande.yaidem and yattyd = 52800001) "
                + "left join schema.repeapi as periode_api on (demande_api.yeirap = periode_api.yhidem) "
                + "WHERE YWTETA=52837003 " + "AND YWDVAL<=" + annee + "1231 " + "AND YWDVAL>=" + annee + "0101 "
                + "and ov.YVTTYP = 52842010 " + "and ( " + "yattyd in (52800003,52800004) " + "or( "
                + "yattyd = 52800001 " + "and " + "yhtgdr in(52809004,52809003) " + ") " + ") ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    private String getSqlComptageSommeIM() {

        String sql = "Select sum(montant_distinct.yvmmon) as "
                + AFStatistiquesOfasProcess.REQUETE_COMPTAGE_TOTAL_IM
                + " from ( Select DISTINCT yviove, YVMMON from schema.redecis as decisions "
                + "inner join schema.represt as prestations on decisions.YWIDEC = prestations.YUIDEC "
                + "inner join schema.reorver as ov on ov.YVIPRE = prestations.YUIPRE "
                + "inner join schema.rederen as demande on demande.yaidem = decisions.YWIDEM "
                + "left join schema.redeapi as demande_api on (demande_api.yeirap = demande.yaidem and yattyd = 52800001) "
                + "left join schema.repeapi as periode_api on (demande_api.yeirap = periode_api.yhidem) "
                + "WHERE YWTETA=52837003 " + "AND YWDVAL<=" + annee + "1231 " + "AND YWDVAL>=" + annee + "0101 "
                + "and ov.YVTTYP = 52842010 " + "and ( " + "yattyd in (52800003,52800004,52800002) " + "or( "
                + "yattyd = 52800001 " + "and " + "yhtgdr in(52809004,52809003,52809002,52809001) " + ") "
                + ")) AS montant_distinct";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    private String getSqlCotiAndNombreIndTseWithRente() {

        String sql = " select idexternerole as "
                + AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_NUMERO_AFFILIE
                + ", hpdnai as "
                + AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_DATE_NAISSANCE
                + ", hptsex as " + AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_SEXE
                + ", se.DATEDEBUTPERIODE as "
                + AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_DATE_DEBUT_PERIODE
                + ", se.DATEFINPERIODE as "
                + AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_DATE_FIN_PERIODE
                + ", op.ANNEECOTISATION as "
                + AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_ANNEE
                + ", sum(montant) as "
                + AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_IND_TSE_WITH_RENTE_COL_NAME_COTISATION
                + " from schema.caoperp op " + " inner join schema.cajourp jo on (op.idjournal=jo.idjournal) "
                + " inner join schema.carubrp ru on (op.idcompte=ru.idrubrique) "
                + " inner join schema.casectp se on (op.idsection=se.idsection) "
                + " inner join schema.cacptap ca on (se.IDCOMPTEANNEXE = ca.IDCOMPTEANNEXE) "
                + " inner join schema.tipersp pr on (ca.idtiers=pr.htitie) " + " where jo.datevaleurcg between "
                + anneeStatMoinsUn + "0101 and " + anneeStatMoinsUn + "1231 " + " and op.etat=205002 "
                + " and ru.idexterne like '211_.4000%' " + " and ca.idtiers in ( "
                + " select htitie from schema.cpdecip cp " + " where iadfac between " + anneeStatMoinsUn + "0101 and "
                + anneeStatMoinsUn + "1231 " + " and cp.IATGAF = 602004 " + " group by htitie " + " ) "
                + " group by idexternerole,hpdnai,hptsex,se.DATEDEBUTPERIODE,se.DATEFINPERIODE,op.ANNEECOTISATION "
                + " having sum(montant) <> 0 " + " order by idexternerole ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;

    }

    private String getSqlCotiAndNombreSalarieWithRente() {

        String sql = " select kbnann as "
                + AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_SALARIE_WITH_RENTE_COL_NAME_ANNEE_COTISATION
                + ", sum(revenu) as "
                + AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_SALARIE_WITH_RENTE_COL_NAME_REVENU
                + ", count(*) as "
                + AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_SALARIE_WITH_RENTE_COL_NAME_NOMBRE_AFFILIE
                + " from ( "
                + " select kanavs,kbnann,sum(case when kbtext=311001 then -kbmmon else kbmmon end) as revenu "
                + " from schema.ciecrip ins " + " inner join schema.ciindip ci on (ins.kaiind=ci.kaiind) "
                + " where kbtgen = 310007 " + " and kbtspe = 312003 " + " and kblesp like '" + anneeStatMoinsUn + "%' "
                + " group by kanavs,kbnann " + " ) as temp " + " group by kbnann";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;

    }

    private String getSqlCotiEmployeurDecompteSimplifie() {

        String sql = " select sum(montant) as "
                + AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_EMPLOYEUR_DECOMPTE_SIMPLIFIE_COL_NAME_COTISATION
                + " from schema.afaffip aff "
                + " inner join schema.cacptap ca on (aff.htitie=ca.idtiers and aff.malnaf = ca.idexternerole) "
                + " inner join schema.caoperp op on (ca.IDCOMPTEANNEXE = op.IDCOMPTEANNEXE) "
                + " inner join schema.carubrp ru on (op.idcompte=ru.idrubrique) "
                + " inner join schema.cajourp jo on (op.idjournal=jo.idjournal) " + " where mattaf=804010 "
                + " and op.etat=205002 " + " and jo.datevaleurcg between " + anneeStatMoinsUn + "0101 and "
                + anneeStatMoinsUn + "1231 " + " and ru.idexterne like '211_.4010.%' ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    private String getSqlInValuesFromListForNumericColumn(List<String> listSqlInValues) {

        StringBuilder sqlInValues = new StringBuilder();

        for (String aSqlInValue : listSqlInValues) {

            if (!JadeStringUtil.isEmpty(sqlInValues.toString())) {
                sqlInValues.append(",");
            }
            sqlInValues.append(aSqlInValue);
        }

        return sqlInValues.toString();

    }

    private String getSqlModeVersementRente061() {

        String sql = getPartSqlModeVersementRente()
                + " and ((adp.HICCP = '' or adp.hiccp is null) and (adp.HINCBA = '' or adp.hincba is null))";

        return sql;
    }

    private String getSqlModeVersementRente062() {

        String sql = getPartSqlModeVersementRente() + " and (adp.HICCP <> '' or adp.HINCBA <> '') ";

        return sql;
    }

    private String getSqlNombreEmployeDecompteSimplifie() {

        String sql = " select count(*) as "
                + AFStatistiquesOfasProcess.REQUETE_NOMBRE_EMPLOYE_DECOMPTE_SIMPLIFIE_COL_NAME_NOMBRE_EMPLOYE
                + " from schema.dsdeclp ds " + " inner join schema.DSINDP li on (ds.TAIDDE = li.TAIDDE) "
                + " inner join schema.ciecrip ecr on (li.KBIECR=ecr.kbiecr) "
                + " inner join schema.ciindip ci on (ecr.kaiind=ci.kaiind) "
                + " inner join schema.fapassp pa on (ds.eaid=pa.idpassage) " + " where ds.TATTYP in (122005, 122006) "
                + " and ds.TATETA = 121003 " + " and pa.DATEFACTURATION between " + anneeStatMoinsUn + "0101 and "
                + anneeStatMoinsUn + "1231 ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;

    }

    private String getSqlNombreEmployeurDecompteSimplifie() {

        String sql = " select count(*) as "
                + AFStatistiquesOfasProcess.REQUETE_NOMBRE_EMPLOYEUR_DECOMPTE_SIMPLIFIE_COL_NAME_NOMBRE_EMPLOYEUR
                + " from schema.afaffip aff " + " where mattaf=804010 " + " and maddeb < " + annee + "0101 "
                + " and (madfin = 0 or madfin > " + annee + "1231) ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;

    }

    private String getSqlNombreExclusionDecompteSimplifie() {

        String sql = " select count(*) as "
                + AFStatistiquesOfasProcess.REQUETE_NOMBRE_EXCLUSION_DECOMPTE_SIMPLIFIE_COL_NAME_NOMBRE_EXCLUSION
                + " from schema.afaffip aff " + " where mattaf=804010 " + " and madfin between " + anneeStatMoinsUn
                + "0101 and " + anneeStatMoinsUn + "1231 " + " and matmot = " + CodeSystem.MOTIF_FIN_EXCLUSION_LTN;

        sql = replaceSchemaInSqlQuery(sql);

        return sql;

    }

    private String getSqlRenteVieillesseAjournee() {

        int anneeInt = Integer.valueOf(annee).intValue();
        String anneeMoins4String = String.valueOf(anneeInt - 4);

        String sql = " select ztitbe as "
                + AFStatistiquesOfasProcess.REQUETE_RENTE_VIEILLESSE_AJOURNEE_COL_NAME_ID_TIERS_BENEFICIAIRE
                + ", ztdddr as "
                + AFStatistiquesOfasProcess.REQUETE_RENTE_VIEILLESSE_AJOURNEE_COL_NAME_DATE_DEBUT_DROIT
                + ", ztdfdr as "
                + AFStatistiquesOfasProcess.REQUETE_RENTE_VIEILLESSE_AJOURNEE_COL_NAME_DATE_FIN_DROIT
                + " from schema.repracc "
                + " inner join schema.rereacc on ztipra = ylirac   "
                + " where  ztteta = 52820002 "
                + "    and ztdddr >= "
                + anneeMoins4String
                + "01 and ztdddr <= "
                + annee
                + "12 and ztlcpr = '10' and (yllcs1 = '08' or yllcs2 = '08' or yllcs3 = '08' or yllcs4 = '08' or yllcs5 = '08') "
                + " order by ztitbe, ztdddr desc  ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    private String getSqlReparationDommage() {

        String sql = " select idexternerole, se.idexterne, sum(montant) as "
                + AFStatistiquesOfasProcess.REQUETE_REPARATION_DOMMAGE_COL_NAME_MONTANT_CREANCE
                + " from schema.cacptap ca "
                + " inner join schema.casectp se on (ca.IDCOMPTEANNEXE = se.IDCOMPTEANNEXE) "
                + " inner join schema.caoperp op on (op.idsection=se.idsection) "
                + " inner join schema.carubrp ru on (op.IDCOMPTE = ru.idrubrique) "
                + " inner join schema.cajourp jo on (se.idjournal=jo.idjournal) "
                + " inner join schema.cajourp jo2 on (op.idjournal=jo2.idjournal) " + " where jo.datevaleurcg between "
                + annee + "0101 and " + annee + "1231 " + " and jo2.datevaleurcg between " + annee + "0101 and "
                + annee + "1231 " + " and se.CATEGORIESECTION = 227096 " + " and ru.idexterne like '200_.2740.%' "
                + " and op.etat=205002 " + " group by idexternerole,se.idexterne ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    private String getSqlRequisitionPoursuite() {
        String sql = " select idexternerole,se.idexterne,cs.pcolut, et.ODTETA as "
                + AFStatistiquesOfasProcess.REQUETE_REQUISITION_POURSUITE_COL_NAME_CS_LIBELLE_ETAPE
                + ", jo.datevaleurcg,hi.OEMSOL as "
                + AFStatistiquesOfasProcess.REQUETE_REQUISITION_POURSUITE_COL_NAME_MONTANT_POURSUITE + ", " + " ( "
                + " select sum(op.montant) " + "    from schema.caoperp op "
                + " inner join schema.carubrp ru on (op.idcompte=ru.idrubrique) "
                + " where op.idsection = se.idsection " + " and op.etat=205002 "
                + " and (ru.idexterne like '211_.4010.%' or ru.idexterne like '211_.4000.%') " + "  ) as "
                + AFStatistiquesOfasProcess.REQUETE_REQUISITION_POURSUITE_COL_NAME_COTAVS + " from schema.cohistp hi "
                + "    inner join schema.cocavsp co on (hi.OAICON = co.OAICON) "
                + " inner join schema.casectp se on (co.OAISEC = se.idsection) "
                + " inner join schema.cacptap ca on (se.IDCOMPTEANNEXE = ca.IDCOMPTEANNEXE) "
                + " inner join schema.coetapp et on (hi.odieta = et.odieta) "
                + " inner join schema.cajourp jo on (hi.OEIJRN = jo.idjournal) "
                + " inner join schema.fwcoup cs on (et.ODTACT = cs.pcosid and cs.plaide = 'F') "
                + " where jo.datevaleurcg between " + annee + "0101 and " + annee + "1231 "
                + " and et.ODTETA in (5200010, 5200001) " + "   and hi.OEBANN = '2' "
                + " and ca.idrole in (517002, 517039, 517040) " + " order by idexternerole,idexterne ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    private String getSqlSursisPaiement() {

        String sql = " select idexternerole, pl.DATE, " + " (select sum(ec.MONTANT) " + " from schema.caechpp ec "
                + " where ec.IDPLANRECOUVREMENT = pl.idplanrecouvrement " + " ) as "
                + AFStatistiquesOfasProcess.REQUETE_SURSIS_PAIEMENT_COL_NAME_TOTAL + ", " + " (select sum(op.montant) "
                + " from schema.caplcsp cs " + " inner join schema.caoperp op on (cs.idsection=op.idsection) "
                + " inner join schema.carubrp ru on (op.idcompte=ru.idrubrique) "
                + " where cs.idplanrecouvrement=pl.idplanrecouvrement " + " and op.etat=205002 "
                + " and (ru.idexterne like '211_.4010.%' or ru.idexterne like '211_.4000.%') " + " ) as "
                + AFStatistiquesOfasProcess.REQUETE_SURSIS_PAIEMENT_COL_NAME_COTAVS + " from schema.caplarp pl "
                + " inner join schema.cacptap ca on (pl.IDCOMPTEANNEXE = ca.IDCOMPTEANNEXE) "
                + " where pl.IDETAT in (226001,226003) " + " and pl.date between " + annee + "0101 and " + annee
                + "1231 " + " order by idexternerole ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    private void handleErrorForPartOfStat() {
        getTransaction().clearErrorBuffer();
        getSession().getErrors();
        JadeThread.logClear();
    }

    private JadeThreadContext initThreadContext(BSession session) throws Exception {

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

    private boolean isSansRestitution() {

        String isSansRestitutionsString = JadePropertiesService.getInstance().getProperty(
                "al.statsOfas.isSansRestitutions");
        boolean isSansRestitutions = Boolean.parseBoolean(isSansRestitutionsString);
        return isSansRestitutions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private void modeVersementRente() throws Exception {

        String sqlQueryModeVersementRente061 = getSqlModeVersementRente061();

        List<Map<String, String>> listMapResultQueryModeVersementRente061 = executeQuery(sqlQueryModeVersementRente061);

        // La requête retourne une seule ligne
        Map<String, String> aMapRowResultQueryModeVersementRente061 = listMapResultQueryModeVersementRente061.get(0);

        String nombreVersement061String = aMapRowResultQueryModeVersementRente061
                .get(AFStatistiquesOfasProcess.REQUETE_MODE_VERSEMENT_RENTE_COL_NAME_NOMBRE_VERSEMENT);
        int nombreVersement061 = Integer.valueOf(nombreVersement061String).intValue();

        String sqlQueryModeVersementRente062 = getSqlModeVersementRente062();

        List<Map<String, String>> listMapResultQueryModeVersementRente062 = executeQuery(sqlQueryModeVersementRente062);

        // La requête retourne une seule ligne
        Map<String, String> aMapRowResultQueryModeVersementRente062 = listMapResultQueryModeVersementRente062.get(0);

        String nombreVersement062String = aMapRowResultQueryModeVersementRente062
                .get(AFStatistiquesOfasProcess.REQUETE_MODE_VERSEMENT_RENTE_COL_NAME_NOMBRE_VERSEMENT);
        int nombreVersement062 = Integer.valueOf(nombreVersement062String).intValue();

        int nombreVersement063 = nombreVersement061 + nombreVersement062;

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_061_MAP_STAT_OFAS, nombreVersement061String);
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_062_MAP_STAT_OFAS, nombreVersement062String);
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_063_MAP_STAT_OFAS, String.valueOf(nombreVersement063));
    }

    private void publierDocumentStatistiqueControleLAALPPCSV(String CsvFilePath) throws Exception {

        // Publication du document
        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        docInfoExcel.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfoExcel.setDocumentTitle(AFStatistiquesOfasProcess.CSV_STATISTIQUE_CONTROLE_LAA_LPP_OUTPUT_FILE_NAME);
        docInfoExcel.setPublishDocument(true);
        docInfoExcel.setArchiveDocument(false);
        this.registerAttachedDocument(docInfoExcel, CsvFilePath);

    }

    private void publierDocumentStatistiqueOfasCSV(String CsvFilePath) throws Exception {

        // Publication du document
        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        docInfoExcel.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfoExcel.setDocumentTitle(AFStatistiquesOfasProcess.CSV_STATISTIQUE_OFAS_OUTPUT_FILE_NAME);
        docInfoExcel.setPublishDocument(true);
        docInfoExcel.setArchiveDocument(false);
        this.registerAttachedDocument(docInfoExcel, CsvFilePath);

    }

    private void renteVieillesseAjournee() throws Exception {

        String sqlQueryRenteVieillesseAjournee = getSqlRenteVieillesseAjournee();
        List<Map<String, String>> listMapResultQueryRenteVieillesseAjournee = executeQuery(sqlQueryRenteVieillesseAjournee);

        String keyAnnee = annee;
        int anneeInt = Integer.valueOf(annee).intValue();
        String keyAnneeMoins1 = String.valueOf(anneeInt - 1);
        String keyAnneeMoins2 = String.valueOf(anneeInt - 2);
        String keyAnneeMoins3 = String.valueOf(anneeInt - 3);
        String keyAnneeMoins4 = String.valueOf(anneeInt - 4);

        Map<String, String> mapNombreRenteParAnnee = new HashMap<String, String>();
        mapNombreRenteParAnnee.put(keyAnnee, "0");
        mapNombreRenteParAnnee.put(keyAnneeMoins1, "0");
        mapNombreRenteParAnnee.put(keyAnneeMoins2, "0");
        mapNombreRenteParAnnee.put(keyAnneeMoins3, "0");
        mapNombreRenteParAnnee.put(keyAnneeMoins4, "0");

        int nombreRenteTotal071 = 0;

        Map<String, String> mapIdTiersAnneeDebutRenteAjournee = new HashMap<String, String>();
        // La requête retourne uniquement les enregistrements allant de l'année de la statistique - 4 à l'année de la
        // statistique
        for (Map<String, String> aMapRowResultQueryRenteVieillesseAjournee : listMapResultQueryRenteVieillesseAjournee) {

            String idTiersBeneficiaire = aMapRowResultQueryRenteVieillesseAjournee
                    .get(AFStatistiquesOfasProcess.REQUETE_RENTE_VIEILLESSE_AJOURNEE_COL_NAME_ID_TIERS_BENEFICIAIRE);
            String anneeMois = aMapRowResultQueryRenteVieillesseAjournee
                    .get(AFStatistiquesOfasProcess.REQUETE_RENTE_VIEILLESSE_AJOURNEE_COL_NAME_DATE_DEBUT_DROIT);
            String annee = extractAAAAFromDateAAAAMM(anneeMois);
            String anneeMoisFinDroit = aMapRowResultQueryRenteVieillesseAjournee
                    .get(AFStatistiquesOfasProcess.REQUETE_RENTE_VIEILLESSE_AJOURNEE_COL_NAME_DATE_FIN_DROIT);
            String anneeFinDroit = "0";
            if (!JadeStringUtil.isBlankOrZero(anneeMoisFinDroit)) {
                anneeFinDroit = extractAAAAFromDateAAAAMM(anneeMoisFinDroit);
            }

            boolean isCasACompter = JadeStringUtil.isBlankOrZero(anneeMoisFinDroit);
            isCasACompter = isCasACompter || (this.annee + "12").equalsIgnoreCase(anneeMoisFinDroit);
            isCasACompter = isCasACompter
                    || Integer.valueOf(anneeFinDroit).intValue() > Integer.valueOf(this.annee).intValue();
            isCasACompter = isCasACompter || mapIdTiersAnneeDebutRenteAjournee.get(idTiersBeneficiaire) != null;

            if (isCasACompter) {
                mapIdTiersAnneeDebutRenteAjournee.put(idTiersBeneficiaire, annee);
            }

        }

        for (String aValue : mapIdTiersAnneeDebutRenteAjournee.values()) {

            int nombreRenteParAnnee = Integer.valueOf(mapNombreRenteParAnnee.get(aValue));
            nombreRenteParAnnee++;
            mapNombreRenteParAnnee.put(aValue, String.valueOf(nombreRenteParAnnee));
            nombreRenteTotal071++;

        }

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_066_MAP_STAT_OFAS, mapNombreRenteParAnnee.get(keyAnnee));
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_067_MAP_STAT_OFAS, mapNombreRenteParAnnee.get(keyAnneeMoins1));
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_068_MAP_STAT_OFAS, mapNombreRenteParAnnee.get(keyAnneeMoins2));
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_069_MAP_STAT_OFAS, mapNombreRenteParAnnee.get(keyAnneeMoins3));
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_070_MAP_STAT_OFAS, mapNombreRenteParAnnee.get(keyAnneeMoins4));
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_071_MAP_STAT_OFAS, String.valueOf(nombreRenteTotal071));

    }

    private void reparationDommage() throws Exception {

        String sqlQueryReparationDommage = getSqlReparationDommage();
        List<Map<String, String>> listMapResultQueryReparationDommage = executeQuery(sqlQueryReparationDommage);

        int nombreCreanceReparationDommage = 0;
        double montantTotalCreanceReparationDommage = 0;

        for (Map<String, String> aMapRowResultQueryReparationDommage : listMapResultQueryReparationDommage) {

            String montantCreanceString = aMapRowResultQueryReparationDommage
                    .get(AFStatistiquesOfasProcess.REQUETE_REPARATION_DOMMAGE_COL_NAME_MONTANT_CREANCE);
            montantCreanceString = replaceDecimalCommaByDecimalPoint(montantCreanceString);
            double montantCreance = Double.valueOf(montantCreanceString).doubleValue();

            nombreCreanceReparationDommage++;
            montantTotalCreanceReparationDommage = montantTotalCreanceReparationDommage + montantCreance;
        }

        mapInfoHorsStatOfas.put(
                AFStatistiquesOfasProcess.KEY_NOMBRE_CREANCE_REPARATION_DOMMAGE_MAP_INFO_HORS_STAT_OFAS,
                String.valueOf(nombreCreanceReparationDommage));
        mapInfoHorsStatOfas.put(AFStatistiquesOfasProcess.KEY_TOTAL_CREANCE_REPARATION_DOMMAGE_MAP_INFO_HORS_STAT_OFAS,
                new FWCurrency(montantTotalCreanceReparationDommage).toStringFormat());
    }

    private String replaceDecimalCommaByDecimalPoint(String decimalNumber) {
        if (JadeStringUtil.isBlankOrZero(decimalNumber)) {
            return "0";
        }
        return decimalNumber.replaceFirst(",", ".");
    }

    private String replaceSchemaInSqlQuery(String sqlQuery) {

        sqlQuery = sqlQuery.replaceAll("(?i)schema\\.", schemaDBWithTablePrefix);

        return sqlQuery;
    }

    private void requisitionPoursuite() throws Exception {

        String sqlQueryRequisitionPoursuite = getSqlRequisitionPoursuite();
        List<Map<String, String>> listMapResultQueryRequisitionPoursuite = executeQuery(sqlQueryRequisitionPoursuite);

        int nombreAff057 = 0;
        int nombreAff711 = 0;
        double coti058 = 0;
        double coti710 = 0;
        double totalRequisitionPoursuite = 0;
        double totalRequisitionContinuerPoursuite = 0;

        for (Map<String, String> aMapRowResultQueryRequisitionPoursuite : listMapResultQueryRequisitionPoursuite) {

            String montantPoursuiteString = aMapRowResultQueryRequisitionPoursuite
                    .get(AFStatistiquesOfasProcess.REQUETE_REQUISITION_POURSUITE_COL_NAME_MONTANT_POURSUITE);
            montantPoursuiteString = replaceDecimalCommaByDecimalPoint(montantPoursuiteString);
            double montantPoursuite = Double.valueOf(montantPoursuiteString).doubleValue();

            String cotAvsString = aMapRowResultQueryRequisitionPoursuite
                    .get(AFStatistiquesOfasProcess.REQUETE_REQUISITION_POURSUITE_COL_NAME_COTAVS);

            if (cotAvsString == null) {

                cotAvsString = "0";
            }

            cotAvsString = replaceDecimalCommaByDecimalPoint(cotAvsString);
            double cotAvs = Double.valueOf(cotAvsString).doubleValue();

            String csLibEtape = aMapRowResultQueryRequisitionPoursuite
                    .get(AFStatistiquesOfasProcess.REQUETE_REQUISITION_POURSUITE_COL_NAME_CS_LIBELLE_ETAPE);

            boolean isRequisitionPoursuite = ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE.equalsIgnoreCase(csLibEtape);
            boolean isRequisitionContinuerPoursuite = ICOEtape.CS_REQUISITION_DE_CONTINUER_LA_POURSUITE_ENVOYEE
                    .equalsIgnoreCase(csLibEtape);

            if (isRequisitionPoursuite) {
                totalRequisitionPoursuite = totalRequisitionPoursuite + montantPoursuite;
            } else if (isRequisitionContinuerPoursuite) {
                totalRequisitionContinuerPoursuite = totalRequisitionContinuerPoursuite + montantPoursuite;
            }

            if (cotAvs != 0) {

                double montantUnitaireForStat = montantPoursuite;
                if (cotAvs < montantPoursuite) {
                    montantUnitaireForStat = cotAvs;
                }

                if (isRequisitionPoursuite) {
                    nombreAff057++;
                    coti058 = coti058 + montantUnitaireForStat;
                } else if (isRequisitionContinuerPoursuite) {
                    nombreAff711++;
                    coti710 = coti710 + montantUnitaireForStat;
                } else {
                    throw new CommonTechnicalException("Not implemented");
                }

            }
        }

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_057_MAP_STAT_OFAS, String.valueOf(nombreAff057));
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_058_MAP_STAT_OFAS, new FWCurrency(coti058).toStringFormat());
        mapInfoHorsStatOfas.put(AFStatistiquesOfasProcess.KEY_TOTAL_REQUISITION_POURSUITE_MAP_INFO_HORS_STAT_OFAS,
                new FWCurrency(totalRequisitionPoursuite).toStringFormat());

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_710_MAP_STAT_OFAS, new FWCurrency(coti710).toStringFormat());
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_711_MAP_STAT_OFAS, String.valueOf(nombreAff711));
        mapInfoHorsStatOfas.put(
                AFStatistiquesOfasProcess.KEY_TOTAL_REQUISITION_CONTINUER_POURSUITE_MAP_INFO_HORS_STAT_OFAS,
                new FWCurrency(totalRequisitionContinuerPoursuite).toStringFormat());
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setIdTypeAdresse(String idTypeAdresse) {
        this.idTypeAdresse = idTypeAdresse;
    }

    private void sommerCotisationEmployeurDecompteSimplifie() throws Exception {

        String sqlQueryCotiEmployeurDecompteSimplifie = getSqlCotiEmployeurDecompteSimplifie();
        List<Map<String, String>> listMapResultQueryCotiEmployeurDecompteSimplifie = executeQuery(sqlQueryCotiEmployeurDecompteSimplifie);

        Map<String, String> aMapRowResultQueryCotiEmployeurDecompteSimplifie = listMapResultQueryCotiEmployeurDecompteSimplifie
                .get(0);

        String cotiAVSString = aMapRowResultQueryCotiEmployeurDecompteSimplifie
                .get(AFStatistiquesOfasProcess.REQUETE_COTI_AND_NOMBRE_EMPLOYEUR_DECOMPTE_SIMPLIFIE_COL_NAME_COTISATION);
        cotiAVSString = replaceDecimalCommaByDecimalPoint(cotiAVSString);
        double coti709 = Double.valueOf(cotiAVSString);

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_709_MAP_STAT_OFAS, new FWCurrency(coti709).toStringFormat());

    }

    private void sursisPaiement() throws Exception {

        String sqlQuerySursisPaiement = getSqlSursisPaiement();
        List<Map<String, String>> listMapResultQuerySursisPaiement = executeQuery(sqlQuerySursisPaiement);

        int nombreAff055 = 0;
        double coti056 = 0;
        double totalSursisPaiement = 0;

        for (Map<String, String> aMapRowResultQuerySursisPaiement : listMapResultQuerySursisPaiement) {

            String totalPlanString = aMapRowResultQuerySursisPaiement
                    .get(AFStatistiquesOfasProcess.REQUETE_SURSIS_PAIEMENT_COL_NAME_TOTAL);
            totalPlanString = replaceDecimalCommaByDecimalPoint(totalPlanString);
            double totalPlan = Double.valueOf(totalPlanString).doubleValue();

            String cotAvsString = aMapRowResultQuerySursisPaiement
                    .get(AFStatistiquesOfasProcess.REQUETE_SURSIS_PAIEMENT_COL_NAME_COTAVS);

            totalSursisPaiement = totalSursisPaiement + totalPlan;

            if (cotAvsString == null) {

                cotAvsString = "0";
            }

            cotAvsString = replaceDecimalCommaByDecimalPoint(cotAvsString);
            double cotAvs = Double.valueOf(cotAvsString).doubleValue();

            if (cotAvs != 0) {

                nombreAff055++;

                double montantUnitaireForStat = totalPlan;
                if (cotAvs < totalPlan) {
                    montantUnitaireForStat = cotAvs;
                }
                coti056 = coti056 + montantUnitaireForStat;
            }

        }

        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_055_MAP_STAT_OFAS, String.valueOf(nombreAff055));
        mapStatOfas.put(AFStatistiquesOfasProcess.KEY_056_MAP_STAT_OFAS, new FWCurrency(coti056).toStringFormat());
        mapInfoHorsStatOfas.put(AFStatistiquesOfasProcess.KEY_TOTAL_SURSIS_PAIEMENT_MAP_INFO_HORS_STAT_OFAS,
                new FWCurrency(totalSursisPaiement).toStringFormat());

    }

    private int ventileAffilieDansComptageParCanton(String codeCanton, int nombreAffilieTotalCanton, String mode) {

        // Les affiliés ayant une adresse à l'étranger ou null doivent être ventilés dans le canton de la caisse
        if (JadeStringUtil.isBlankOrZero(codeCanton)
                || AFStatistiquesOfasProcess.CODE_CANTON_ETRANGER.equalsIgnoreCase(codeCanton)) {
            codeCanton = codeCantonCaisse;
        }

        // Tant la map mapCodeCantonChiffreStatOfas que la map mapStatOfas sont initialisées dans le constructeur
        // et contiennent donc une valeur pour la clé recherchée
        String chiffreStatOfasForCodeCanton = mapCodeCantonChiffreStatOfas.get(codeCanton);

        String nombreAffilieForCanton = mapStatOfas.get(chiffreStatOfasForCodeCanton);

        nombreAffilieForCanton = String.valueOf(Integer.valueOf(nombreAffilieForCanton).intValue() + 1);

        if (AFStatistiquesOfasProcess.MODE_COMPTER_AFFILIE.equalsIgnoreCase(mode)) {
            mapStatOfas.put(chiffreStatOfasForCodeCanton, nombreAffilieForCanton);
            nombreTotalAffilieCanton++;
        }

        return nombreAffilieTotalCanton++;

    }

}
