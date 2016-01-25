package globaz.aquila.ts.opge.file;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.ts.opge.file.parameters.COOPGEParameter;
import globaz.aquila.ts.opge.file.parameters.COOPGEParameterManager;
import globaz.fer.format.FERNumAffilie;
import globaz.framework.filetransfer.FWAsciiFileFieldDescriptor;
import globaz.framework.filetransfer.FWAsciiFileRecordDescriptor;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.external.IntRole;
import java.util.Calendar;

public class COOPGEFileCommonPart {

    private static final int COMMON_PART_LENGTH = 18;
    private static final String KEY_NUMERO_CREANCE = "NUMERO_CREANCE";
    private static final String KEY_NUMERO_DE_POURSUITE = "NUMERO_DE_POURSUITE";
    private static final String KEY_NUMERO_LIGNE_TEXTE_MEME_CREANCE = "NUMERO_LIGNE_TEXTE_MEME_CREANCE";
    private static final String KEY_NUMERO_TIERS_EXPEDITEUR = "NUMERO_TIERS_EXPEDITEUR";

    private static final String KEY_TYPE_STRUCTURE = "TYPE_STRUCTURE";
    private static final String LABEL_ERREUR_NUMERO_TIERS_EXPEDITEUR_NON_INITIALISE = "ERREUR_NUMERO_TIERS_EXPEDITEUR_NON_INITIALISE";

    private static final String LABEL_NUMERO_POURSUITE_MAX_ATTEINT = "NUMERO_POURSUITE_MAX_ATTEINT";
    private static final String LABEL_NUMERO_POURSUITE_NON_INITIALISE = "NUMERO_POURSUITE_NON_INITIALISE";

    private static final String LABEL_NUMERO_POURSUITE_NON_RENSEIGNE = "NUMERO_POURSUITE_NON_RENSEIGNE";
    private static final String LABEL_NUMERO_POURSUITE_SAVE_ERROR = "NUMERO_POURSUITE_SAVE_ERROR";

    private static final String LABEL_RDP_COTISATIONS_PARITAIRES = "RDP_COTISATIONS_PARITAIRES";
    private static final String LABEL_RDP_COTISATIONS_PERSONNELLES = "RDP_COTISATIONS_PERSONNELLES";

    private static final String PARAM_NUMTIEREXPPARITAIRE = "NUMTIEREXP";
    private static final String PARAM_NUMTIEREXPPERSONNEL = "NUMEXPPER";
    private static final int REFERENCE_PART_LENGTH = 14;
    public static final String TYPE_STRUCTURE_IDENTITE_CREANCIER = "45";
    public static final String TYPE_STRUCTURE_IDENTITE_DEBITEUR = "10";

    public static final String TYPE_STRUCTURE_IDENTITE_POSTALE_CREANCIER = "46";
    public static final String TYPE_STRUCTURE_IDENTITE_POSTALE_MARI = "40";
    public static final String TYPE_STRUCTURE_IDENTITE_TEXTE_MARI = "41";
    public static final String TYPE_STRUCTURE_IMPUTATION = "80";
    public static final String TYPE_STRUCTURE_MANDATAIRE_DEBITEUR = "30";
    public static final String TYPE_STRUCTURE_MONTANT_INTERET_TITRE_CREANCE = "70";
    public static final String TYPE_STRUCTURE_NUMERO_CONJOINT = "90";
    public static final String TYPE_STRUCTURE_NUMERO_MANDATAIRE_CREANCIER = "50";
    public static final String TYPE_STRUCTURE_NUMERO_REFERENCE_POURSUITE_CREANCIER = "60";
    public static final String TYPE_STRUCTURE_POURSUITE_CONJOINTE_SOLIDAIRE = "91";
    public static final String TYPE_STRUCTURE_TEXTE_CREANCE = "71";
    public static final String TYPE_STRUCTURE_TEXTE_DEBITEUR = "11";
    public static final String TYPE_STRUCTURE_TEXTE_MANDATAIRE_DEBITEUR = "31";
    public static final String TYPE_STRUCTURE_TEXTE_TUTEUR_DEBITEUR = "21";
    public static final String TYPE_STRUCTURE_TUTEUR_DEBITEUR = "20";
    private static final char ZERO_CHAR = '0';
    private static final String ZEROZERO = "00";

    private int countNumeroCreance = 0;

    private int countNumeroLigneAdresse = 0;
    private int countNumeroLigneTexteMemeCreance = 0;
    private String lastTypeStructure;

    private String numeroPoursuite = null;

    private String numeroTiersExpediteur = null;
    private FWAsciiFileRecordDescriptor recordCommon = new FWAsciiFileRecordDescriptor();
    private String referenceCreancier = null;

    /**
     * Retourne la partie commune en fonction de l'initialisation et des paramètres.
     * 
     * @param session
     * @param transaction
     * @param typeStructure
     * @return la partie commune en fonction de l'initialisation et des paramètres.
     * @throws Exception
     */
    public String getCommonPart(BSession session, BTransaction transaction, String typeStructure) throws Exception {
        validate(session);

        recordCommon.setFieldValue(COOPGEFileCommonPart.KEY_NUMERO_TIERS_EXPEDITEUR, numeroTiersExpediteur);
        recordCommon.setFieldValue(COOPGEFileCommonPart.KEY_NUMERO_DE_POURSUITE, numeroPoursuite);
        recordCommon.setFieldValue(COOPGEFileCommonPart.KEY_TYPE_STRUCTURE, typeStructure);
        recordCommon.setFieldValue(COOPGEFileCommonPart.KEY_NUMERO_CREANCE, getNumeroCreance(typeStructure));
        recordCommon.setFieldValue(COOPGEFileCommonPart.KEY_NUMERO_LIGNE_TEXTE_MEME_CREANCE,
                getNumeroLigneTexteMemeCreance(typeStructure));

        lastTypeStructure = typeStructure;

        return recordCommon.createRecord(session);
    }

    /**
     * Retourne le dernier numéro de poursuite effectué cette année.
     * 
     * @param session
     * @param transaction
     * @return
     * @throws Exception
     */
    private COOPGEParameter getLastPoursuite(BSession session, BTransaction transaction) throws Exception {
        COOPGEParameterManager mgr = new COOPGEParameterManager();
        mgr.setSession(session);
        mgr.setDefaultParameter();

        mgr.find(transaction);

        if (mgr.hasErrors() || mgr.isEmpty()) {
            throw new Exception(session.getLabel(COOPGEFileCommonPart.LABEL_NUMERO_POURSUITE_SAVE_ERROR));
        }

        return (COOPGEParameter) mgr.getFirstEntity();
    }

    /**
     * Return le numéro de créance
     * 
     * @see TYPE_STRUCTURE_MONTANT_INTERET_TITRE_CREANCE
     * @param typeStructure
     * @return
     */
    private String getNumeroCreance(String typeStructure) {
        if (typeStructure.equals(COOPGEFileCommonPart.TYPE_STRUCTURE_MONTANT_INTERET_TITRE_CREANCE)) {
            if (!lastTypeStructure.equals(COOPGEFileCommonPart.TYPE_STRUCTURE_MONTANT_INTERET_TITRE_CREANCE)) {
                countNumeroLigneTexteMemeCreance = 0;
            }
            countNumeroCreance++;

            return JadeStringUtil.rightJustify("" + countNumeroCreance, 2, COOPGEFileCommonPart.ZERO_CHAR);
        } else if (typeStructure.equals(COOPGEFileCommonPart.TYPE_STRUCTURE_TEXTE_CREANCE)) {
            return JadeStringUtil.rightJustify("" + countNumeroCreance, 2, COOPGEFileCommonPart.ZERO_CHAR);
        } else {
            return COOPGEFileCommonPart.ZEROZERO;
        }
    }

    /**
     * Retourne le numéro de poursuite.
     * 
     * @param session
     * @param transaction
     * @return
     * @throws Exception
     */
    private String getNumeroDePoursuite(BSession session, BTransaction transaction) throws Exception {
        COOPGEParameter element = getLastPoursuite(session, transaction);
        int lastNumeroPoursuiteValue = Integer.parseInt(element.getValeurAlphaParametre().substring(2,
                element.getValeurAlphaParametre().length()));
        int maxNumeroPoursuite = Integer.parseInt(FWFindParameter.findParameter(transaction, "0",
                COOPGEParameter.PARAM_NUMERO_PLAGE_FIN_POURSUITE, "0", "0", 0));

        lastNumeroPoursuiteValue++;

        if (lastNumeroPoursuiteValue < maxNumeroPoursuite) {
            return updateNumeroPoursuite(session, transaction, element, lastNumeroPoursuiteValue);
        } else {
            Calendar today = Calendar.getInstance();
            String actualYear = "" + today.get(Calendar.YEAR);

            if (!actualYear.substring(2).equals(element.getValeurAlphaParametre().substring(0, 2))) {
                int minNumeroPoursuiteValue = Integer.parseInt(FWFindParameter.findParameter(transaction, "0",
                        COOPGEParameter.PARAM_NUMERO_PLAGE_DEBUT_POURSUITE, "0", "0", 0));

                return updateNumeroPoursuite(session, transaction, element, minNumeroPoursuiteValue);
            } else {
                throw new Exception(session.getLabel(COOPGEFileCommonPart.LABEL_NUMERO_POURSUITE_MAX_ATTEINT));
            }
        }

    }

    /**
     * Return le numéro de ligne lors d'une même créance
     * 
     * @see TYPE_STRUCTURE_TEXTE_CREANCE
     * @param typeStructure
     * @return
     */
    private String getNumeroLigneTexteMemeCreance(String typeStructure) {
        if (typeStructure.equals(COOPGEFileCommonPart.TYPE_STRUCTURE_TEXTE_CREANCE)) {
            countNumeroLigneTexteMemeCreance++;

            return JadeStringUtil
                    .rightJustify("" + countNumeroLigneTexteMemeCreance, 2, COOPGEFileCommonPart.ZERO_CHAR);
        } else if (typeStructure.equals(COOPGEFileCommonPart.TYPE_STRUCTURE_MONTANT_INTERET_TITRE_CREANCE)) {
            return JadeStringUtil
                    .rightJustify("" + countNumeroLigneTexteMemeCreance, 2, COOPGEFileCommonPart.ZERO_CHAR);
        } else if (typeStructure.equals(COOPGEFileCommonPart.TYPE_STRUCTURE_TEXTE_DEBITEUR)) {
            countNumeroLigneAdresse++;

            return JadeStringUtil.rightJustify("" + countNumeroLigneAdresse, 2, COOPGEFileCommonPart.ZERO_CHAR);
        } else {
            return COOPGEFileCommonPart.ZEROZERO;
        }
    }

    /**
     * Renvois la structure de référence du créancier
     * 
     * @param session
     * @param transaction
     * @return
     * @throws Exception
     */
    public String getNumeroReference(BSession session, BTransaction transaction) throws Exception {
        validate(session);

        FWAsciiFileRecordDescriptor record = new FWAsciiFileRecordDescriptor();
        record.setLength(COOPGEFileCommonPart.REFERENCE_PART_LENGTH);
        int pos = 1;
        record.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COOPGEFileCommonPart.KEY_NUMERO_TIERS_EXPEDITEUR,
                FWAsciiFileFieldDescriptor.INTEGER, 4, 0, pos));
        pos += 4;
        record.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COOPGEFileCommonPart.KEY_NUMERO_DE_POURSUITE,
                FWAsciiFileFieldDescriptor.INTEGER, 8, 0, pos));
        pos += 8;
        record.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COOPGEFileCommonPart.KEY_TYPE_STRUCTURE,
                FWAsciiFileFieldDescriptor.INTEGER, 2, 0, pos));
        pos += 2;

        record.setFieldValue(COOPGEFileCommonPart.KEY_NUMERO_TIERS_EXPEDITEUR, numeroTiersExpediteur);
        record.setFieldValue(COOPGEFileCommonPart.KEY_NUMERO_DE_POURSUITE, numeroPoursuite);
        record.setFieldValue(COOPGEFileCommonPart.KEY_TYPE_STRUCTURE,
                COOPGEFileCommonPart.TYPE_STRUCTURE_NUMERO_REFERENCE_POURSUITE_CREANCIER);

        return record.createRecord(session) + COOPGEFileCommonPart.ZEROZERO + COOPGEFileCommonPart.ZEROZERO
                + referenceCreancier;
    }

    /**
     * Return le numéro du tiers éxpéditeur (doit-être initialisé).
     * 
     * @param session
     * @return
     * @throws Exception
     */
    public String getNumeroTiersExpediteur(BSession session) throws Exception {
        if (numeroTiersExpediteur == null) {
            throw new Exception(
                    session.getLabel(COOPGEFileCommonPart.LABEL_ERREUR_NUMERO_TIERS_EXPEDITEUR_NON_INITIALISE));
        }

        return JadeStringUtil.rightJustify("" + numeroTiersExpediteur, 4, COOPGEFileCommonPart.ZERO_CHAR);
    }

    /**
     * Initialisation de la partie "commune" de chaque record.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @throws Exception
     */
    public void init(BSession session, BTransaction transaction, COContentieux contentieux) throws Exception {
        recordCommon.setLength(COOPGEFileCommonPart.COMMON_PART_LENGTH);
        int pos = 1;
        recordCommon.addFieldDescriptor(new FWAsciiFileFieldDescriptor(
                COOPGEFileCommonPart.KEY_NUMERO_TIERS_EXPEDITEUR, FWAsciiFileFieldDescriptor.INTEGER, 4, 0, pos));
        pos += 4;
        recordCommon.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COOPGEFileCommonPart.KEY_NUMERO_DE_POURSUITE,
                FWAsciiFileFieldDescriptor.INTEGER, 8, 0, pos));
        pos += 8;
        recordCommon.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COOPGEFileCommonPart.KEY_TYPE_STRUCTURE,
                FWAsciiFileFieldDescriptor.INTEGER, 2, 0, pos));
        pos += 2;
        recordCommon.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COOPGEFileCommonPart.KEY_NUMERO_CREANCE,
                FWAsciiFileFieldDescriptor.INTEGER, 2, 0, pos));
        pos += 2;
        recordCommon
                .addFieldDescriptor(new FWAsciiFileFieldDescriptor(
                        COOPGEFileCommonPart.KEY_NUMERO_LIGNE_TEXTE_MEME_CREANCE, FWAsciiFileFieldDescriptor.INTEGER,
                        2, 0, pos));

        numeroPoursuite = getNumeroDePoursuite(session, transaction);
        saveNumeroPoursuite(session, transaction, contentieux);

        referenceCreancier = new FERNumAffilie().unformat(contentieux.getCompteAnnexe().getIdExterneRole());

        if (contentieux.getCompteAnnexe().getIdRole().equals(IntRole.ROLE_AFFILIE_PARITAIRE)) {
            referenceCreancier += " " + session.getLabel(COOPGEFileCommonPart.LABEL_RDP_COTISATIONS_PARITAIRES);
            numeroTiersExpediteur = FWFindParameter.findParameter(transaction, "0",
                    COOPGEFileCommonPart.PARAM_NUMTIEREXPPARITAIRE, "0", "0", 0);
        } else if (contentieux.getCompteAnnexe().getIdRole().equals(IntRole.ROLE_AFFILIE_PERSONNEL)) {
            referenceCreancier += " " + session.getLabel(COOPGEFileCommonPart.LABEL_RDP_COTISATIONS_PERSONNELLES);
            numeroTiersExpediteur = FWFindParameter.findParameter(transaction, "0",
                    COOPGEFileCommonPart.PARAM_NUMTIEREXPPERSONNEL, "0", "0", 0);
        }

        validate(session);
    }

    /**
     * Sauvegard du numéro de poursuite (généré) dans la section.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @throws Exception
     */
    private void saveNumeroPoursuite(BSession session, BTransaction transaction, COContentieux contentieux)
            throws Exception {
        contentieux.getSection().setSession(session);
        contentieux.getSection().setNumeroPoursuite(numeroPoursuite);
        contentieux.getSection().update(transaction);
    }

    /**
     * Mise à jour du numéro de poursuite actuel dans FWPARP.
     * 
     * @param session
     * @param transaction
     * @param element
     * @param numeroPoursuite
     * @return
     * @throws Exception
     */
    private String updateNumeroPoursuite(BSession session, BTransaction transaction, COOPGEParameter element,
            int numeroPoursuite) throws Exception {
        element.setSession(session);

        Calendar today = Calendar.getInstance();
        String actualYear = "" + today.get(Calendar.YEAR);

        String updateValue = actualYear.substring(2) + JadeStringUtil.rightJustify("" + numeroPoursuite, 6, '0');

        element.setValeurAlphaParametre(updateValue);
        element.update(transaction);

        if (element.hasErrors() || element.isNew()) {
            throw new Exception(session.getLabel(COOPGEFileCommonPart.LABEL_NUMERO_POURSUITE_NON_RENSEIGNE));
        }

        return updateValue;
    }

    /**
     * Test de l'initialisation du numéro de l'expéditeur et du numéro de poursuite. Si erreur throws Exception.
     * 
     * @param session
     * @throws Exception
     */
    private void validate(BSession session) throws Exception {
        if (numeroPoursuite == null) {
            throw new Exception(session.getLabel(COOPGEFileCommonPart.LABEL_NUMERO_POURSUITE_NON_INITIALISE));
        }

        getNumeroTiersExpediteur(session);
    }
}
