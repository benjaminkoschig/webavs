package ch.globaz.al.business.constantes;

/**
 * Constantes liées aux traitements RAFam
 *
 * @author jts
 *
 */
public interface ALConstRafam {

    public static final String ACTION_1 = "1";
    public static final String DATE_DEBUT_MINIMUM = "01.01.1980";
    public static final String DATE_FIN_MINIMUM = "31.12.2010";
    public static final String DATE_MAXIMUM_RAFAM = "31.12.2099";
    public static final String DELAY_INSIGNIFICANCE = "al.rafam.delayInsignificance";
    public static final String EXPORT_IS_ENABLED = "al.rafam.exportIsEnabled";
    public static final String IMPORT_REGISTER_STATUS_DELEGATE_IS_ENABLED = "al.rafam.importRegisterStatusDelegateIsEnabled";
    public static final String IMPORT_REGISTER_STATUS_IS_ENABLED = "al.rafam.importRegisterStatusIsEnabled";
    public static final int INTERNAL_OFFICE_REFERENCE_MAX_LENGH = 36;
    public static final Object INTERNAL_OFFICE_REFERENCE_SEPARATOR = "-";
    public static final String MANUFACTURER = "GLOBAZ SA";
    public static final String MESSAGE_ED_DATA_BLANK = "non defini";
    public static final int MESSAGE_ED_NAME_MAX_LENGTH = 50;
    public static final String MESSAGE_ED_NOERROR_CODE = "050";
    public static final String MESSAGE_TYPE = "al.rafam.messageType";
    public static final String MINOR_VERSION_XSD = "0";
    public static final String PREFIX_ANNONCE_CAISSE = "C";
    public static final String PREFIX_INTERNAL_OFFICE_REFERENCE_CAISSE = "C";
    public static final String PREFIX_INTERNAL_OFFICE_REFERENCE_ED = "ED";
    public static final String PRODUCT_NAME = "Web@AVS";
    public static final String PRODUCT_VERSION = "1-14-00";
    public static final String RAFAM_CONTACT_EMAIL = "al.rafamContactEmail";
    public static final String RAFAM_CONTACT_TECHNIQUE_EMAIL = "al.rafamContactTechniqueEmail";
    public static final String VERSION_ANNONCES_XSD_4_1 = "al.generation.annonces.xsd.4.1";
    public static final String MINOR_VERSION_XSD_4_1 = "1";

    public static final String RAFAM_IS_MULTI_CAISSE = "rafam.isMultiCaisse";
    public static final String RAFAM_OFFICE_IDENTIFIER = "rafam.officeIdentifier";
    public static final String RAFAM_OFFICE_BRANCH = "rafam.officeBranch";
    public static final String RAFAM_LEGAL_OFFICE = "rafam.legalOffice";

    public static final String RECIPIENT_ID = "al.rafam.recipientId";
    public static final String SUB_MESSAGE_TYPE_0 = "0";
    public static final String SUBJECT_ANNONCE_RAFAM = "Annonces RAFam";
    public static final String SUBJECT_ANNONCE_RAFAM_DELEGUE = "Annonces RAFam - employeur(s) délégué(s)";
    public static final String UPI_UNKNOWN_NSS = "756.9999.9999.99";
    
    public static final String PATTERN_LEGAL_OFFICE = "^[0-9]{3}\\.[0-9]{3}|ALK\\.[0-9]{3}$";
}
