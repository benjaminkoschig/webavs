package globaz.osiris.parser;

import globaz.globall.api.BISession;
import globaz.globall.db.BSession;

/**
 * Insérez la description du type ici. Date de création : (18.02.2002 15:43:00)
 * 
 * @author: Administrator
 */
public interface IntReferenceBVRParser {
    public static String IDENT_PLAN = "osiris.class.CAReferenceBVRParser.planPaiementIdentifier";
    public static String LEN_CFC_ID_EXTERNE_ROLE = "osiris.class.CAReferenceBVRParserCFC.lenIdExterneRole";
    public static String LEN_CFC_NO_POSTE = "osiris.class.CAReferenceBVRParserCFC.lenNoPoste";
    public static String LEN_ID_EXTERNE_ROLE = "osiris.class.CAReferenceBVRParser.lenIdExterneRole";
    public static String LEN_ID_PLAN = "osiris.class.CAReferenceBVRParser.lenIdExterneSection";
    public static String LEN_ID_ROLE = "osiris.class.CAReferenceBVRParser.lenIdRole";
    public static String LEN_OLD_ID_EXTERNE_ROLE = "osiris.class.CAReferenceBVRParserOLD.lenIdExterneRole";
    public static String LEN_OLD_NO_POSTE = "osiris.class.CAReferenceBVRParserOLD.lenNoPoste";
    public static String LEN_TYPE_PLAN = "osiris.class.CAReferenceBVRParser.lenTypePlan";
    public static String LEN_TYPE_SECTION = "osiris.class.CAReferenceBVRParser.lenTypeSection";
    public static String POS_CFC_ID_EXTERNE_ROLE = "osiris.class.CAReferenceBVRParserCFC.posIdExterneRole";
    public static String POS_CFC_NO_POSTE = "osiris.class.CAReferenceBVRParserCFC.posNoPoste";
    public static String POS_ID_EXTERNE_ROLE = "osiris.class.CAReferenceBVRParser.posIdExterneRole";
    public static String POS_ID_PLAN = "osiris.class.CAReferenceBVRParser.posIdExterneSection";
    public static String POS_ID_ROLE = "osiris.class.CAReferenceBVRParser.posIdRole";
    public static String POS_OLD_ID_EXTERNE_ROLE = "osiris.class.CAReferenceBVRParserOLD.posIdExterneRole";
    public static String POS_OLD_ID_ROLE = "osiris.class.CAReferenceBVRParserOLD.posIdRole";
    public static String POS_OLD_NO_POSTE = "osiris.class.CAReferenceBVRParserOLD.posNoPoste";
    public static String POS_OLD2_ID_EXTERNE_ROLE = "osiris.class.CAReferenceBVRParserOLD2.posIdExterneRole";
    public static String POS_TYPE_PLAN = "osiris.class.CAReferenceBVRParser.posTypePlan";
    public static String POS_TYPE_SECTION = "osiris.class.CAReferenceBVRParser.posTypeSection";
    public static String VAL_CFC_ID_ROLE = "osiris.class.CAReferenceBVRParserCFC.valIdRole";
    public static String VAL_CFC_TYPE_SECTION = "osiris.class.CAReferenceBVRParserCFC.valTypeSection";
    public static String VAL_ID_ROLE = "osiris.class.CAReferenceBVRParser.valIdRole";
    public static String VAL_OLD_ID_ROLE = "osiris.class.CAReferenceBVRParserOLD.valIdRole";
    public static String VAL_OLD_TYPE_SECTION = "osiris.class.CAReferenceBVRParserOLD.valTypeSection";
    public static String VAL_TYPE_SECTION = "osiris.class.CAReferenceBVRParser.valTypeSection";
    public static String VAL_NUM_BANQUE = "osiris.class.CARefBVRBanqueParser.valNumBanque";

    /**
     * Retourne l'id du compte annexe
     * 
     * @return
     */
    public String getIdCompteAnnexe();

    /**
     * Retourne l'id du plan de paiement.
     * 
     * @return
     */
    public String getIdPlanPaiement();

    /**
     * Retourne l'id externe de la section
     * 
     * @return
     */
    public String getIdExterneSection();

    /**
     * Retourne l'id type section
     * 
     * @return
     */
    public String getIdTypeSection();

    /**
     * Retourne l'id de la section
     * 
     * @return
     */
    public String getIdSection();

    /**
     * Retourne la session
     * 
     * @return
     */
    public BISession getISession();

    /**
     * Indique si le numéro de référence fait référence à un plan de paiement
     * 
     * @return
     */
    public boolean isPlanPaiement();

    /**
     * Indique pour les bulletins neutre si le mode de compensation est crédit
     * 
     * @return
     */
    public boolean isModeCreditBulletinNeutre();

    /**
     * Setter la session
     * 
     * @param newSession
     * @throws Exception
     */
    public void setISession(BISession newSession) throws Exception;

    /**
     * Setter la référence
     * 
     * @param numeroReference
     * @param session
     * @param numInterneLsv
     * @throws Exception
     */
    void setReference(String numeroReference, BSession session, String numInterneLsv) throws Exception;
}
