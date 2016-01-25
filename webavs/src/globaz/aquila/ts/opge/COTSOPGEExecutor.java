package globaz.aquila.ts.opge;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.journal.COElementJournalBatch;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COExtraitCompteManager;
import globaz.aquila.db.rdp.CORequisitionPoursuiteUtil;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.ts.COTSExecutor;
import globaz.aquila.ts.opge.file.COOPGEFileCommonPart;
import globaz.aquila.ts.opge.utils.COTSOPGEUtils;
import globaz.aquila.ts.rules.COTSOPGERule;
import globaz.aquila.ts.rules.utils.COOPGESpellChecker;
import globaz.aquila.ts.utils.COTSTiersUtils;
import globaz.framework.filetransfer.FWAsciiFileFieldDescriptor;
import globaz.framework.filetransfer.FWAsciiFileRecordDescriptor;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.extrait.CAExtraitCompte;
import globaz.osiris.db.interet.util.CAInteretUtil;
import globaz.osiris.external.IntTiers;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.tiers.TITiers;
import java.math.BigDecimal;
import java.util.StringTokenizer;

/**
 * Créé le contenu, pour un contentieux, pour le fichier RDP Genève.
 * 
 * @author DDA
 */
public class COTSOPGEExecutor implements COTSExecutor {
    public static final String BLANK = " ";

    private static final String CS_DOCTEUR = "19120006";
    private static final String CS_MADEMOISELLE = "19120005";

    private static final String CS_MAITRE = "19120008";
    private static final String KEY_ADRESSE = "ADRESSE";

    private static final String KEY_CODE_ANCIENNE_POURSUITE = "CODE_ANCIENNE_POURSUITE";
    private static final String KEY_CODE_POLITESSSE_DEBITEUR = "CODE_POLITESSSE_DEBITEUR";
    private static final String KEY_DATE = "DATE";
    private static final String KEY_LIBELLE = "LIBELLE";
    private static final String KEY_MONTANT = "MONTANT";

    private static final String KEY_NOM = "NOM";

    private static final String KEY_NPA_LOCALITE = "NPA_LOCALITE";
    private static final String KEY_NUMERO_SEQUESTRE = "NUMERO_SEQUESTRE";
    private static final String KEY_SUITE_NOM = "SUITE_NOM";

    private static final String KEY_TAUX = "TAUX";
    private static final String KEY_TEXTE_DEBITEUR = "TEXTE_DEBITEUR";
    private static final String KEY_TYPE_POURSUITE = "TYPE_POURSUITE";
    public static final String LABEL_DEBITEUR_ETR_ART_50 = "DEBITEUR_ETR_ART_50";
    public static final String LABEL_DEBITEUR_ETR_NOTIFICATION = "DEBITEUR_ETR_NOTIFICATION";
    private static final String LABEL_RDP_LIBELLE_PRIVILEGE_LEGAL_REQUIS = "RDP_LIBELLE_PRIVILEGE_LEGAL_REQUIS";
    private static final int LENGTH_LIBELLE_CREANCE = 70;
    // N'est pas indiqué dans la documentation, restiction fournit par FER Mme.
    // Quenet
    public static final int LENGTH_MAX_TEXTE_DEBITEUR = 35;
    public static final int LENGTH_NOM = 24;
    public static final int LENGTH_NPA_LOCALITE = 24;
    public static final int LENGTH_RUE_NUMERO = 24;
    public static final int LENGTH_SUITE_NOM = 24;
    public static final int LENGTH_TEXTE_DEBITEUR = 72;

    public static final String LINE_BREAK = "\r\n";

    private static final String NUMERO_SEQUESTRE_PAR_DEFAUT = "00000000";
    private static final String TITRE_DOCTEUR = "R";
    private static final String TITRE_MADAME = "D";
    private static final String TITRE_MADEMOISELLE = "L";

    private static final String TITRE_MAITRE = "T";

    private static final String TITRE_MONSIEUR = "M";

    private static final String TYPE_POURSUITE_ORDINAIRE = "O";

    private String ecrituresFromDate = "0";
    private String forNotIdJournalCreance = "0";

    /**
     * Ajoute l'adresse et la localité.
     * 
     * @param session
     * @param adresseDataSource
     * @param record
     * @throws Exception
     */
    private void addAdresse(BSession session, TIAdresseDataSource adresseDataSource, FWAsciiFileRecordDescriptor record)
            throws Exception {
        String rueEtNumeroRue = COTSTiersUtils.convertSpecialChars(adresseDataSource.rue + COTSOPGEExecutor.BLANK
                + adresseDataSource.numeroRue);

        if (rueEtNumeroRue.length() > COTSOPGEExecutor.LENGTH_RUE_NUMERO) {
            throw new Exception(session.getLabel(COTSOPGERule.LABEL_TIERS_RUE_NUMERO_TROP_LONG));
        }
        record.setFieldValue(COTSOPGEExecutor.KEY_ADRESSE, rueEtNumeroRue);

        String npaEtLocalite = COTSTiersUtils.convertSpecialChars(adresseDataSource.localiteNpa
                + COTSOPGEExecutor.BLANK + adresseDataSource.localiteNom);

        if (npaEtLocalite.length() > COTSOPGEExecutor.LENGTH_NPA_LOCALITE) {
            throw new Exception(session.getLabel(COTSOPGERule.LABEL_TIERS_NPA_LOCALITE_TROP_LONG));
        }
        record.setFieldValue(COTSOPGEExecutor.KEY_NPA_LOCALITE, npaEtLocalite);
    }

    /**
     * Ajout du nom au record en fonction de designation1 et designation2 du tiers (morale ou physique déjà géré).
     * 
     * @param session
     * @param transaction
     * @param record
     * @param contentieux
     * @param adresseDataSource
     * @throws Exception
     */
    private void addNom(BSession session, BTransaction transaction, FWAsciiFileRecordDescriptor record,
            COContentieux contentieux, TIAdresseDataSource adresseDataSource) throws Exception {
        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne1)
                && !COOPGESpellChecker.isWellFormated(session, contentieux.getCompteAnnexe().getTiers(),
                        adresseDataSource.fullLigne1)) {
            throw new Exception(session.getLabel(COTSOPGERule.LABEL_TIERS_CARACTERES_NON_SUPPORTES) + " ("
                    + adresseDataSource.fullLigne1 + ")");
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne2)
                && !COOPGESpellChecker.isWellFormated(session, contentieux.getCompteAnnexe().getTiers(),
                        adresseDataSource.fullLigne2)) {
            throw new Exception(session.getLabel(COTSOPGERule.LABEL_TIERS_CARACTERES_NON_SUPPORTES) + " ("
                    + adresseDataSource.fullLigne2 + ")");
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne3)
                && !COOPGESpellChecker.isWellFormated(session, contentieux.getCompteAnnexe().getTiers(),
                        adresseDataSource.fullLigne3)) {
            throw new Exception(session.getLabel(COTSOPGERule.LABEL_TIERS_CARACTERES_NON_SUPPORTES) + " ("
                    + adresseDataSource.fullLigne3 + ")");
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne4)
                && !COOPGESpellChecker.isWellFormated(session, contentieux.getCompteAnnexe().getTiers(),
                        adresseDataSource.fullLigne4)) {
            throw new Exception(session.getLabel(COTSOPGERule.LABEL_TIERS_CARACTERES_NON_SUPPORTES) + " ("
                    + adresseDataSource.fullLigne4 + ")");
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne1)
                && COServiceLocator.getTiersService().isPersonnePhysique(session,
                        contentieux.getCompteAnnexe().getTiers())
                && !COOPGESpellChecker.containsName(adresseDataSource.fullLigne1, adresseDataSource.tiersLigne1)) {
            throw new Exception(session.getLabel(COTSOPGERule.LABEL_LIGNE_IMPOSSIBLE_TROUVER_NOM));
        }

        record.setFieldValue(COTSOPGEExecutor.KEY_NOM,
                COTSTiersUtils.getNomOuRaisonSociale(session, contentieux, adresseDataSource));
        record.setFieldValue(COTSOPGEExecutor.KEY_SUITE_NOM,
                COTSTiersUtils.getSuiteNomOuRaisonSociale(session, contentieux, adresseDataSource));
    }

    /**
     * Créé le contenu, pour un contentieux, pour le fichier RDP Genève.
     */
    @Override
    public String execute(BSession session, BTransaction transaction, COElementJournalBatch element) throws Exception {
        COOPGEFileCommonPart common = new COOPGEFileCommonPart();
        common.init(session, transaction, element.getContentieux());

        StringBuffer sb = new StringBuffer();
        sb.append(getIdentiteDuDebiteur(session, transaction, element.getContentieux(), common).toUpperCase());
        sb.append(COTSOPGEExecutor.LINE_BREAK);
        // Line break ajouté en fin de méthode
        sb.append(getTexteDuDebiteur(session, transaction, element.getContentieux(), common).toUpperCase());
        sb.append(getCreancier(session, transaction, common).toUpperCase());
        sb.append(COTSOPGEExecutor.LINE_BREAK);
        sb.append(getNumeroReference(session, transaction, element.getContentieux(), common).toUpperCase());
        sb.append(COTSOPGEExecutor.LINE_BREAK);
        sb.append(getMontantCreanceSoumis(transaction, element.getContentieux(), common).toUpperCase());
        sb.append(getMontantCreanceNonSoumis(transaction, element.getContentieux(), common).toUpperCase());
        sb.append(getTaxeCreance(transaction, element, common).toUpperCase());
        sb.append(getPrivilegeLegalRequis(session, transaction, element.getContentieux(), common).toUpperCase());
        sb.append(COTSOPGEExecutor.LINE_BREAK);
        sb.append(getRemarqueCreance(session, transaction, element, common).toUpperCase());
        sb.append(getImputations(session, transaction, element.getContentieux(), common).toUpperCase());

        return sb.toString();
    }

    /**
     * Return/construit l'identité du créancier.
     * 
     * @param session
     * @param transaction
     * @param common
     * @return
     * @throws Exception
     */
    private String getCreancier(BSession session, BTransaction transaction, COOPGEFileCommonPart common)
            throws Exception {
        return common.getCommonPart(session, transaction, COOPGEFileCommonPart.TYPE_STRUCTURE_IDENTITE_CREANCIER)
                + common.getNumeroTiersExpediteur(session);
    }

    /**
     * Return/construit l'identité du débiteur.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @param common
     * @return
     * @throws Exception
     */
    private String getIdentiteDuDebiteur(BSession session, BTransaction transaction, COContentieux contentieux,
            COOPGEFileCommonPart common) throws Exception {
        TIAdresseDataSource adresseDataSource = COServiceLocator.getTiersService().getAdresseDataSource(session,
                contentieux.getCompteAnnexe().getTiers(), contentieux.getCompteAnnexe().getIdExterneRole(),
                JACalendar.today().toString());
        if (JadeStringUtil.isBlank(adresseDataSource.fullLigne1)) {
            throw new Exception(session.getLabel(COTSOPGERule.LABEL_TIERS_NOM_OBLIGATOIRE));
        }

        if (JadeStringUtil.isBlank(adresseDataSource.localiteNpa)) {
            throw new Exception(session.getLabel(COTSOPGERule.LABEL_TIERS_NPA_OBLIGATOIRE));
        }

        if (!TIPays.CODE_ISO_CH.equals(adresseDataSource.paysIso)) {
            throw new Exception(session.getLabel(COTSOPGERule.LABEL_TIERS_ADRESSE_NON_SUISSE));
        }

        FWAsciiFileRecordDescriptor record = new FWAsciiFileRecordDescriptor();
        record.setLength(107);
        int pos = 1;
        record.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COTSOPGEExecutor.KEY_TYPE_POURSUITE,
                FWAsciiFileFieldDescriptor.STRING, 1, 0, pos));
        pos += 1;
        record.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COTSOPGEExecutor.KEY_NUMERO_SEQUESTRE,
                FWAsciiFileFieldDescriptor.INTEGER, 8, 0, pos));
        pos += 8;
        record.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COTSOPGEExecutor.KEY_CODE_ANCIENNE_POURSUITE,
                FWAsciiFileFieldDescriptor.STRING, 1, 0, pos));
        pos += 1;
        record.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COTSOPGEExecutor.KEY_CODE_POLITESSSE_DEBITEUR,
                FWAsciiFileFieldDescriptor.STRING, 1, 0, pos));
        pos += 1;
        record.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COTSOPGEExecutor.KEY_NOM,
                FWAsciiFileFieldDescriptor.STRING, COTSOPGEExecutor.LENGTH_NOM, 0, pos));
        pos += COTSOPGEExecutor.LENGTH_NOM;
        record.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COTSOPGEExecutor.KEY_SUITE_NOM,
                FWAsciiFileFieldDescriptor.STRING, COTSOPGEExecutor.LENGTH_SUITE_NOM, 0, pos));
        pos += COTSOPGEExecutor.LENGTH_SUITE_NOM;
        record.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COTSOPGEExecutor.KEY_ADRESSE,
                FWAsciiFileFieldDescriptor.STRING, 24, 0, pos));
        pos += 24;
        record.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COTSOPGEExecutor.KEY_NPA_LOCALITE,
                FWAsciiFileFieldDescriptor.STRING, 24, 0, pos));
        pos += 24;

        record.setFieldValue(COTSOPGEExecutor.KEY_TYPE_POURSUITE, COTSOPGEExecutor.TYPE_POURSUITE_ORDINAIRE);
        record.setFieldValue(COTSOPGEExecutor.KEY_NUMERO_SEQUESTRE, COTSOPGEExecutor.NUMERO_SEQUESTRE_PAR_DEFAUT);
        record.setFieldValue(COTSOPGEExecutor.KEY_CODE_ANCIENNE_POURSUITE, COTSOPGEExecutor.BLANK);
        record.setFieldValue(COTSOPGEExecutor.KEY_CODE_POLITESSSE_DEBITEUR, getTitre(adresseDataSource));

        addNom(session, transaction, record, contentieux, adresseDataSource);

        addAdresse(session, adresseDataSource, record);

        return common.getCommonPart(session, transaction, COOPGEFileCommonPart.TYPE_STRUCTURE_IDENTITE_DEBITEUR)
                + record.createRecord(session);
    }

    /**
     * Return/construit les imputations.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @param common
     * @return
     * @throws Exception
     */
    private String getImputations(BSession session, BTransaction transaction, COContentieux contentieux,
            COOPGEFileCommonPart common) throws Exception {
        COExtraitCompteManager extraitCompteManager = COTSOPGEUtils.getExtraitCompteManager(session, contentieux,
                forNotIdJournalCreance, ecrituresFromDate);

        extraitCompteManager.find(transaction, BManager.SIZE_NOLIMIT);

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < extraitCompteManager.size(); ++i) {
            CAExtraitCompte extraitCompte = (CAExtraitCompte) extraitCompteManager.getEntity(i);

            FWCurrency montant = new FWCurrency(extraitCompte.getMontant());

            if (montant.isNegative() && !montant.isZero()
                    && JadeStringUtil.isIntegerEmpty(extraitCompte.getProvenancePmt())) {
                montant.abs();

                FWAsciiFileRecordDescriptor record = getRecordImputations();
                record.setFieldValue(COTSOPGEExecutor.KEY_MONTANT, montant.toString());
                record.setFieldValue(COTSOPGEExecutor.KEY_DATE, extraitCompte.getDate());
                sb.append(common.getCommonPart(session, transaction, COOPGEFileCommonPart.TYPE_STRUCTURE_IMPUTATION)
                        + record.createRecord(session));
                sb.append(COTSOPGEExecutor.LINE_BREAK);
            }
        }

        return sb.toString();
    }

    /**
     * Return une ligne de texte du débiteur avec le texte passé en paramètre. Max 35 caractères.
     * 
     * @param session
     * @param transaction
     * @param common
     * @param texte
     * @return
     * @throws Exception
     */
    private String getLigneTexteDuDebiteur(BSession session, BTransaction transaction, COOPGEFileCommonPart common,
            String texte) throws Exception {
        texte = COTSTiersUtils.convertSpecialChars(texte);

        if (!JadeStringUtil.isBlank(texte) && !COOPGESpellChecker.isLengthTexteComplementAllowed(texte)) {
            throw new Exception(session.getLabel(COTSOPGERule.LABEL_TIERS_LIGNE_TROP_LONG) + " (" + texte + ")");
        }

        FWAsciiFileRecordDescriptor record = getRecordTexteDuDebiteur();

        if (texte.length() < COTSOPGEExecutor.LENGTH_TEXTE_DEBITEUR) {
            record.setFieldValue(COTSOPGEExecutor.KEY_TEXTE_DEBITEUR, texte);
        } else {
            record.setFieldValue(COTSOPGEExecutor.KEY_TEXTE_DEBITEUR,
                    texte.substring(0, COTSOPGEExecutor.LENGTH_TEXTE_DEBITEUR));
        }

        return common.getCommonPart(session, transaction, COOPGEFileCommonPart.TYPE_STRUCTURE_TEXTE_DEBITEUR)
                + record.createRecord(session);
    }

    /**
     * Return une ligne de texte du débiteur avec le texte passé en paramètre, max 72 caractères sinon trim.
     * 
     * @param session
     * @param transaction
     * @param common
     * @param texte
     * @return
     * @throws Exception
     */
    private String getLigneTexteDuDebiteurWithoutLengthRestriction(BSession session, BTransaction transaction,
            COOPGEFileCommonPart common, String texte) throws Exception {
        FWAsciiFileRecordDescriptor record = getRecordTexteDuDebiteur();

        texte = COTSTiersUtils.convertSpecialChars(texte);

        if (texte.length() < COTSOPGEExecutor.LENGTH_TEXTE_DEBITEUR) {
            record.setFieldValue(COTSOPGEExecutor.KEY_TEXTE_DEBITEUR, texte);
        } else {
            record.setFieldValue(COTSOPGEExecutor.KEY_TEXTE_DEBITEUR,
                    texte.substring(0, COTSOPGEExecutor.LENGTH_TEXTE_DEBITEUR));
        }

        return common.getCommonPart(session, transaction, COOPGEFileCommonPart.TYPE_STRUCTURE_TEXTE_DEBITEUR)
                + record.createRecord(session);
    }

    /**
     * @param session
     * @param transaction
     * @param contentieux
     * @param common
     * @return
     * @throws Exception
     */
    private String getMontantCreanceNonSoumis(BTransaction transaction, COContentieux contentieux,
            COOPGEFileCommonPart common) throws Exception {
        String[] soldeInitial = CORequisitionPoursuiteUtil.getSoldeSectionInitial(transaction.getSession(),
                contentieux.getIdSection());
        String montant = soldeInitial[0];
        String montantSoumis = CORequisitionPoursuiteUtil.getMontantCreanceSoumis(transaction, contentieux);
        String montantNonSoumis = (new BigDecimal(montant)).subtract(new BigDecimal(montantSoumis)).toString();

        forNotIdJournalCreance = soldeInitial[1];
        ecrituresFromDate = soldeInitial[2];

        if (!JadeStringUtil.isDecimalEmpty(montantNonSoumis)) {
            FWAsciiFileRecordDescriptor record = getRecordStructureMontantInteretTitreCreance();

            record.setFieldValue(COTSOPGEExecutor.KEY_MONTANT, montantNonSoumis);
            record.setFieldValue(COTSOPGEExecutor.KEY_DATE, COTSOPGEExecutor.NUMERO_SEQUESTRE_PAR_DEFAUT);

            record.setFieldValue(COTSOPGEExecutor.KEY_TAUX, "0.00");

            String libelle = CORequisitionPoursuiteUtil.getLibelleCreance(transaction.getSession(), contentieux);

            libelle = JadeStringUtil.convertSpecialChars(libelle);
            if (libelle.length() > COTSOPGEExecutor.LENGTH_LIBELLE_CREANCE) {
                record.setFieldValue(COTSOPGEExecutor.KEY_LIBELLE,
                        libelle.substring(0, COTSOPGEExecutor.LENGTH_LIBELLE_CREANCE));
            } else {
                record.setFieldValue(COTSOPGEExecutor.KEY_LIBELLE, libelle);
            }

            StringBuffer montantCreance = new StringBuffer();
            montantCreance.append(common.getCommonPart(transaction.getSession(), transaction,
                    COOPGEFileCommonPart.TYPE_STRUCTURE_MONTANT_INTERET_TITRE_CREANCE)
                    + record.createRecord(transaction.getSession()));

            if (libelle.length() > COTSOPGEExecutor.LENGTH_LIBELLE_CREANCE) {
                montantCreance.append(COTSOPGEExecutor.LINE_BREAK);
                montantCreance.append(getSuiteTexteCreance(transaction.getSession(), transaction, common,
                        libelle.substring(COTSOPGEExecutor.LENGTH_LIBELLE_CREANCE)));
            }

            return montantCreance.toString() + COTSOPGEExecutor.LINE_BREAK;
        } else {
            return "";
        }
    }

    /**
     * Return/construit le record pour le montant de la créance.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @param common
     * @return
     * @throws Exception
     */
    private String getMontantCreanceSoumis(BTransaction transaction, COContentieux contentieux,
            COOPGEFileCommonPart common) throws Exception {
        String montantSoumis = CORequisitionPoursuiteUtil.getMontantCreanceSoumis(transaction, contentieux);

        if (!JadeStringUtil.isDecimalEmpty(montantSoumis)) {
            FWAsciiFileRecordDescriptor record = getRecordStructureMontantInteretTitreCreance();

            record.setFieldValue(COTSOPGEExecutor.KEY_MONTANT, montantSoumis);
            record.setFieldValue(COTSOPGEExecutor.KEY_DATE, CORequisitionPoursuiteUtil.getDateDebutInteretsTardifs(
                    transaction.getSession(), transaction, contentieux));

            record.setFieldValue(COTSOPGEExecutor.KEY_TAUX, "" + getTaux(transaction, contentieux));

            String libelle = CORequisitionPoursuiteUtil.getLibelleCreance(transaction.getSession(), contentieux);

            libelle = JadeStringUtil.convertSpecialChars(libelle);
            if (libelle.length() > COTSOPGEExecutor.LENGTH_LIBELLE_CREANCE) {
                record.setFieldValue(COTSOPGEExecutor.KEY_LIBELLE,
                        libelle.substring(0, COTSOPGEExecutor.LENGTH_LIBELLE_CREANCE));
            } else {
                record.setFieldValue(COTSOPGEExecutor.KEY_LIBELLE, libelle);
            }

            StringBuffer montantCreance = new StringBuffer();
            montantCreance.append(common.getCommonPart(transaction.getSession(), transaction,
                    COOPGEFileCommonPart.TYPE_STRUCTURE_MONTANT_INTERET_TITRE_CREANCE)
                    + record.createRecord(transaction.getSession()));

            if (libelle.length() > COTSOPGEExecutor.LENGTH_LIBELLE_CREANCE) {
                montantCreance.append(COTSOPGEExecutor.LINE_BREAK);
                montantCreance.append(getSuiteTexteCreance(transaction.getSession(), transaction, common,
                        libelle.substring(COTSOPGEExecutor.LENGTH_LIBELLE_CREANCE)));
            }

            return montantCreance.toString() + COTSOPGEExecutor.LINE_BREAK;
        } else {
            return "";
        }
    }

    /**
     * Return le numéro de référence.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @param common
     * @return
     * @throws Exception
     */
    private String getNumeroReference(BSession session, BTransaction transaction, COContentieux contentieux,
            COOPGEFileCommonPart common) throws Exception {
        return common.getNumeroReference(session, transaction).trim();
    }

    /**
     * Return l'information (en suite de créance) obligatoire du privilege légal.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @param common
     * @return
     * @throws Exception
     */
    private String getPrivilegeLegalRequis(BSession session, BTransaction transaction, COContentieux contentieux,
            COOPGEFileCommonPart common) throws Exception {
        return getSuiteTexteCreance(session, transaction, common,
                session.getLabel(COTSOPGEExecutor.LABEL_RDP_LIBELLE_PRIVILEGE_LEGAL_REQUIS));
    }

    /**
     * Return le record qui contiendra les imputations.
     * 
     * @return
     * @throws Exception
     */
    private FWAsciiFileRecordDescriptor getRecordImputations() throws Exception {
        FWAsciiFileRecordDescriptor record = new FWAsciiFileRecordDescriptor();
        record.setLength(18);
        int pos = 1;
        record.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COTSOPGEExecutor.KEY_MONTANT,
                FWAsciiFileFieldDescriptor.NUMBER_FIXED_DECIMALS, 12, 2, pos));
        pos += 12;
        record.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COTSOPGEExecutor.KEY_DATE,
                FWAsciiFileFieldDescriptor.DATE_JMA, 6, 0, pos));
        pos += 6;
        return record;
    }

    /**
     * Return le record qui contiendra les informations sur le montant et intérêts de la créance.
     * 
     * @return
     * @throws Exception
     */
    private FWAsciiFileRecordDescriptor getRecordStructureMontantInteretTitreCreance() throws Exception {
        FWAsciiFileRecordDescriptor record = new FWAsciiFileRecordDescriptor();
        record.setLength(93);
        int pos = 1;
        record.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COTSOPGEExecutor.KEY_MONTANT,
                FWAsciiFileFieldDescriptor.NUMBER_FIXED_DECIMALS, 12, 2, pos));
        pos += 12;
        record.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COTSOPGEExecutor.KEY_DATE,
                FWAsciiFileFieldDescriptor.DATE_JMA, 6, 0, pos));
        pos += 6;
        record.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COTSOPGEExecutor.KEY_TAUX,
                FWAsciiFileFieldDescriptor.NUMBER_FIXED_DECIMALS, 5, 3, pos));
        pos += 5;
        record.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COTSOPGEExecutor.KEY_LIBELLE,
                FWAsciiFileFieldDescriptor.STRING, 70, 0, pos));
        return record;
    }

    /**
     * Return le record qui contiendra les textes du débiteurs.
     * 
     * @return
     * @throws Exception
     */
    private FWAsciiFileRecordDescriptor getRecordTexteDuDebiteur() throws Exception {
        FWAsciiFileRecordDescriptor record = new FWAsciiFileRecordDescriptor();
        record.setLength(72);
        int pos = 1;
        record.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COTSOPGEExecutor.KEY_TEXTE_DEBITEUR,
                FWAsciiFileFieldDescriptor.STRING, COTSOPGEExecutor.LENGTH_TEXTE_DEBITEUR, 0, pos));
        pos += 72;

        return record;
    }

    /**
     * Ajoute la remarque de l'élément du batch comme supplément à la créance.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @param common
     * @return
     * @throws Exception
     */
    private String getRemarqueCreance(BSession session, BTransaction transaction, COElementJournalBatch element,
            COOPGEFileCommonPart common) throws Exception {
        StringBuffer result = new StringBuffer("");

        if (!JadeStringUtil.isIntegerEmpty(element.getIdRemarque())) {
            StringTokenizer st = new StringTokenizer(element.getRemarque().getTexte(), COTSOPGEExecutor.LINE_BREAK);
            while (st.hasMoreTokens()) {
                String tmp = COTSTiersUtils.convertSpecialChars(st.nextToken());

                if (tmp.length() > COTSOPGEExecutor.LENGTH_TEXTE_DEBITEUR) {
                    throw new Exception(session.getLabel(COTSOPGERule.LIGNE_REMARQUE_TROP_LONGUE) + " (" + tmp + ")");
                }

                result.append(getSuiteTexteCreance(session, transaction, common, tmp) + COTSOPGEExecutor.LINE_BREAK);
            }

        }

        return result.toString();
    }

    /**
     * Return la suite du texte de la créance.
     * 
     * @param session
     * @param transaction
     * @param common
     * @param libelle
     * @return
     * @throws Exception
     */
    private String getSuiteTexteCreance(BSession session, BTransaction transaction, COOPGEFileCommonPart common,
            String libelle) throws Exception {
        FWAsciiFileRecordDescriptor record = new FWAsciiFileRecordDescriptor();
        record.setLength(72);
        record.addFieldDescriptor(new FWAsciiFileFieldDescriptor(COTSOPGEExecutor.KEY_LIBELLE,
                FWAsciiFileFieldDescriptor.STRING, 72, 0, 1));
        record.setFieldValue(COTSOPGEExecutor.KEY_LIBELLE, libelle);

        return common.getCommonPart(session, transaction, COOPGEFileCommonPart.TYPE_STRUCTURE_TEXTE_CREANCE)
                + record.createRecord(session);
    }

    /**
     * Retourne le taux d'intérêt. (FWPARP, OSIRIS dès 20010101)
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @return
     * @throws Exception
     */
    private String getTaux(BTransaction transaction, COContentieux contentieux) throws Exception {
        return JAUtil.formatDecimal(
                new BigDecimal(CAInteretUtil.getTaux(transaction, CORequisitionPoursuiteUtil
                        .getDateDebutInteretsTardifs(transaction.getSession(), transaction, contentieux))), 2);
    }

    /**
     * Return/construit le record pour les taxes de la créance.
     * 
     * @param session
     * @param transaction
     * @param element
     * @param common
     * @return
     * @throws Exception
     */
    private String getTaxeCreance(BTransaction transaction, COElementJournalBatch element, COOPGEFileCommonPart common)
            throws Exception {
        COExtraitCompteManager extraitCompteManager = COTSOPGEUtils.getExtraitCompteManager(transaction.getSession(),
                element.getContentieux(), forNotIdJournalCreance, ecrituresFromDate);

        extraitCompteManager.find(transaction, BManager.SIZE_NOLIMIT);

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < extraitCompteManager.size(); ++i) {
            CAExtraitCompte extraitCompte = (CAExtraitCompte) extraitCompteManager.getEntity(i);

            FWCurrency montant = new FWCurrency(extraitCompte.getMontant());

            if (montant.isPositive()
                    && !montant.isZero()
                    && !COTSOPGEUtils.isRubriqueSoumise(transaction.getSession(), transaction,
                            extraitCompte.getIdRubrique())
                    && !CORequisitionPoursuiteUtil.isLineBlocked(transaction,
                            ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE, extraitCompte.getIdRubrique())) {
                FWAsciiFileRecordDescriptor record = getRecordStructureMontantInteretTitreCreance();

                record.setFieldValue(COTSOPGEExecutor.KEY_MONTANT, extraitCompte.getMontant());
                record.setFieldValue(COTSOPGEExecutor.KEY_DATE, COTSOPGEExecutor.NUMERO_SEQUESTRE_PAR_DEFAUT);
                record.setFieldValue(COTSOPGEExecutor.KEY_TAUX, "0.00");

                String description = COTSTiersUtils.convertSpecialChars(COTSOPGEUtils.getDescriptionExtraitCompte(
                        transaction.getSession(), extraitCompte, element.getIdTransition()));
                if (description.length() > COTSOPGEExecutor.LENGTH_LIBELLE_CREANCE) {
                    record.setFieldValue(COTSOPGEExecutor.KEY_LIBELLE,
                            description.substring(0, COTSOPGEExecutor.LENGTH_LIBELLE_CREANCE));
                } else {
                    record.setFieldValue(COTSOPGEExecutor.KEY_LIBELLE, description);
                }

                StringBuffer taxeCreance = new StringBuffer();
                taxeCreance.append(common.getCommonPart(transaction.getSession(), transaction,
                        COOPGEFileCommonPart.TYPE_STRUCTURE_MONTANT_INTERET_TITRE_CREANCE)
                        + record.createRecord(transaction.getSession()));
                taxeCreance.append(COTSOPGEExecutor.LINE_BREAK);

                sb.append(taxeCreance);

                // Gere la suite du texte creance
                if (description.length() > COTSOPGEExecutor.LENGTH_LIBELLE_CREANCE) {
                    sb.append(getSuiteTexteCreance(transaction.getSession(), transaction, common,
                            description.substring(COTSOPGEExecutor.LENGTH_LIBELLE_CREANCE)));
                    sb.append(COTSOPGEExecutor.LINE_BREAK);
                }
            }
        }

        return sb.toString();
    }

    /**
     * Return/construit le complément de texte du débiteur.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @param common
     * @return
     * @throws Exception
     */
    private String getTexteDuDebiteur(BSession session, BTransaction transaction, COContentieux contentieux,
            COOPGEFileCommonPart common) throws Exception {
        String result = "";

        TIAdresseDataSource adresseDataSource = COServiceLocator.getTiersService().getAdresseDataSourceComplementRDP(
                session, contentieux.getCompteAnnexe().getTiers(), contentieux.getCompteAnnexe().getIdExterneRole(),
                JACalendar.today().toString());
        if (adresseDataSource != null) {
            result = getTexteDuDebiteurFromDataSource(session, transaction, contentieux, common, result,
                    adresseDataSource);
        } else {
            TIAdresseDataSource adresseDataSourceDomicile = COServiceLocator.getTiersService()
                    .getAdresseDataSourceDomicileStandard(session, contentieux.getCompteAnnexe().getTiers(),
                            contentieux.getCompteAnnexe().getIdExterneRole(), JACalendar.today().toString());
            TIAdresseDataSource adresseDataSourceDebiteur = COServiceLocator.getTiersService().getAdresseDataSource(
                    session, contentieux.getCompteAnnexe().getTiers(),
                    contentieux.getCompteAnnexe().getIdExterneRole(), JACalendar.today().toString());

            if ((adresseDataSourceDomicile != null) && !TIPays.CODE_ISO_CH.equals(adresseDataSourceDomicile.paysIso)) {
                result = getTexteDuDebiteurEtranger(session, transaction, contentieux, common, result,
                        adresseDataSourceDomicile, adresseDataSourceDebiteur);
            } else if (COTSTiersUtils.needTexteDuDebiteurAutomatique(session, contentieux, adresseDataSourceDebiteur)) {
                result = getTexteDuDebiteurFromDataSource(session, transaction, contentieux, common, result,
                        adresseDataSourceDebiteur);
            }
        }

        return result;
    }

    /**
     * Ajoute le texte du débiteur (zone 11) formatté pour une adresse français en y metionnant le texte suivant : <br/>
     * DEBITEUR POURSUIVABLE EN SUISSE EN VERTU DE L'ART. 50 AL. 1 LP <br/>
     * POUR NOTIFICATION A SON DOMICILE PROFESSIONNEL
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @param common
     * @param result
     * @param adresseDataSourceDomicile
     * @param adresseDataSourceDebiteur
     * @return
     * @throws Exception
     */
    private String getTexteDuDebiteurEtranger(BSession session, BTransaction transaction, COContentieux contentieux,
            COOPGEFileCommonPart common, String result, TIAdresseDataSource adresseDataSourceDomicile,
            TIAdresseDataSource adresseDataSourceDebiteur) throws Exception {
        if (JadeStringUtil.isBlank(adresseDataSourceDomicile.fullLigne1)) {
            throw new Exception(session.getLabel(COTSOPGERule.LABEL_TIERS_NOM_OBLIGATOIRE));
        }

        if (JadeStringUtil.isBlank(adresseDataSourceDomicile.localiteNpa)) {
            throw new Exception(session.getLabel(COTSOPGERule.LABEL_TIERS_NPA_OBLIGATOIRE));
        }

        if (JadeStringUtil.isBlank(adresseDataSourceDebiteur.localiteNpa)) {
            throw new Exception(session.getLabel(COTSOPGERule.LABEL_TIERS_NPA_OBLIGATOIRE));
        }

        String codePolitesse = "";
        if (!IntTiers.OSIRIS_PERSONNE_MORALE.equals(contentieux.getCompteAnnexe().getTiers().getTypeTiers())) {
            codePolitesse = COServiceLocator.getTiersService().getFormulePolitesse(session,
                    contentieux.getCompteAnnexe().getTiers(), session.getIdLangueISO())
                    + COTSOPGEExecutor.BLANK;
        }

        result += getLigneTexteDuDebiteurWithoutLengthRestriction(session, transaction, common, codePolitesse
                + COTSTiersUtils.getLigneCumule(adresseDataSourceDomicile, COTSOPGEExecutor.BLANK))
                + COTSOPGEExecutor.LINE_BREAK;

        if (!JadeStringUtil.isBlank(adresseDataSourceDomicile.rue)
                || !JadeStringUtil.isBlank(adresseDataSourceDomicile.numeroRue)) {
            String tmp = COTSTiersUtils.convertSpecialChars(adresseDataSourceDomicile.rue + COTSOPGEExecutor.BLANK
                    + adresseDataSourceDomicile.numeroRue + "," + COTSOPGEExecutor.BLANK
                    + COTSTiersUtils.limitNpaEtranger(adresseDataSourceDomicile.localiteNpa) + COTSOPGEExecutor.BLANK
                    + adresseDataSourceDomicile.localiteNom + "," + COTSOPGEExecutor.BLANK
                    + adresseDataSourceDomicile.paysNom);
            result += getLigneTexteDuDebiteurWithoutLengthRestriction(session, transaction, common, tmp)
                    + COTSOPGEExecutor.LINE_BREAK;
        }

        result += getLigneTexteDuDebiteurWithoutLengthRestriction(session, transaction, common,
                session.getLabel(COTSOPGEExecutor.LABEL_DEBITEUR_ETR_ART_50))
                + COTSOPGEExecutor.LINE_BREAK;

        result += getLigneTexteDuDebiteurWithoutLengthRestriction(session, transaction, common,
                session.getLabel(COTSOPGEExecutor.LABEL_DEBITEUR_ETR_NOTIFICATION))
                + COTSOPGEExecutor.LINE_BREAK;

        if (!JadeStringUtil.isBlank(adresseDataSourceDebiteur.rue)
                || !JadeStringUtil.isBlank(adresseDataSourceDebiteur.numeroRue)) {
            String rueEtNumero = COTSTiersUtils.convertSpecialChars(adresseDataSourceDebiteur.rue
                    + COTSOPGEExecutor.BLANK + adresseDataSourceDebiteur.numeroRue);
            result += getLigneTexteDuDebiteur(session, transaction, common, rueEtNumero) + COTSOPGEExecutor.LINE_BREAK;
        }

        String npaEtLocalite = COTSTiersUtils.convertSpecialChars(adresseDataSourceDebiteur.localiteNpa
                + COTSOPGEExecutor.BLANK + adresseDataSourceDebiteur.localiteNom);
        result += getLigneTexteDuDebiteur(session, transaction, common, npaEtLocalite) + COTSOPGEExecutor.LINE_BREAK;

        return result;
    }

    /**
     * Construit le complément du texte du débiteur à partir de l'adresseDataSource passé en paramètre.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @param common
     * @param result
     * @param adresseDataSource
     * @return
     * @throws Exception
     */
    private String getTexteDuDebiteurFromDataSource(BSession session, BTransaction transaction,
            COContentieux contentieux, COOPGEFileCommonPart common, String result, TIAdresseDataSource adresseDataSource)
            throws Exception {
        if (JadeStringUtil.isBlank(adresseDataSource.fullLigne1)) {
            throw new Exception(session.getLabel(COTSOPGERule.LABEL_TIERS_NOM_OBLIGATOIRE));
        }

        if (JadeStringUtil.isBlank(adresseDataSource.localiteNpa)) {
            throw new Exception(session.getLabel(COTSOPGERule.LABEL_TIERS_NPA_OBLIGATOIRE));
        }

        if (!IntTiers.OSIRIS_PERSONNE_MORALE.equals(contentieux.getCompteAnnexe().getTiers().getTypeTiers())) {
            result += getLigneTexteDuDebiteur(session, transaction, common, COServiceLocator.getTiersService()
                    .getFormulePolitesse(session, contentieux.getCompteAnnexe().getTiers(), session.getIdLangueISO()))
                    + COTSOPGEExecutor.LINE_BREAK;
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne1)) {
            result += getLigneTexteDuDebiteur(session, transaction, common, adresseDataSource.fullLigne1)
                    + COTSOPGEExecutor.LINE_BREAK;
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne2)) {
            result += getLigneTexteDuDebiteur(session, transaction, common, adresseDataSource.fullLigne2)
                    + COTSOPGEExecutor.LINE_BREAK;
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne3)) {
            result += getLigneTexteDuDebiteur(session, transaction, common, adresseDataSource.fullLigne3)
                    + COTSOPGEExecutor.LINE_BREAK;
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne4)) {
            result += getLigneTexteDuDebiteur(session, transaction, common, adresseDataSource.fullLigne4)
                    + COTSOPGEExecutor.LINE_BREAK;
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.rue) || !JadeStringUtil.isBlank(adresseDataSource.numeroRue)) {
            String rueEtNumero = COTSTiersUtils.convertSpecialChars(adresseDataSource.rue + COTSOPGEExecutor.BLANK
                    + adresseDataSource.numeroRue);
            result += getLigneTexteDuDebiteur(session, transaction, common, rueEtNumero) + COTSOPGEExecutor.LINE_BREAK;
        }

        String npaEtLocalite = COTSTiersUtils.convertSpecialChars(adresseDataSource.localiteNpa
                + COTSOPGEExecutor.BLANK + adresseDataSource.localiteNom);
        result += getLigneTexteDuDebiteur(session, transaction, common, npaEtLocalite) + COTSOPGEExecutor.LINE_BREAK;

        return result;
    }

    /**
     * Return le titre (salutation) pour le tiers.
     * 
     * @see
     * @param adresseDataSource
     * @return
     */
    private String getTitre(TIAdresseDataSource adresseDataSource) {
        String titre = COTSOPGEExecutor.BLANK;
        if (!JadeStringUtil.isBlank(adresseDataSource.csTitre)) {
            if (adresseDataSource.csTitre.equals(TITiers.CS_MONSIEUR)) {
                titre = COTSOPGEExecutor.TITRE_MONSIEUR;
            } else if (adresseDataSource.csTitre.equals(TITiers.CS_MADAME)) {
                titre = COTSOPGEExecutor.TITRE_MADAME;
            } else if (adresseDataSource.csTitre.equals(COTSOPGEExecutor.CS_MADEMOISELLE)) {
                titre = COTSOPGEExecutor.TITRE_MADEMOISELLE;
            } else if (adresseDataSource.csTitre.equals(COTSOPGEExecutor.CS_DOCTEUR)) {
                titre = COTSOPGEExecutor.TITRE_DOCTEUR;
            } else if (adresseDataSource.csTitre.equals(COTSOPGEExecutor.CS_MAITRE)) {
                titre = COTSOPGEExecutor.TITRE_MAITRE;
            }
        }

        return titre;
    }
}
