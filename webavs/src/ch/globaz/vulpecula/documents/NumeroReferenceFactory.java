package ch.globaz.vulpecula.documents;

import globaz.osiris.application.CAApplication;
import globaz.osiris.parser.IntReferenceBVRParser;
import org.apache.commons.lang.StringUtils;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazBusinessException;
import ch.globaz.vulpecula.domain.models.common.NumeroReference;
import ch.globaz.vulpecula.domain.models.decompte.NumeroDecompte;
import ch.globaz.vulpecula.external.models.osiris.TypeSection;
import ch.globaz.vulpecula.external.models.pyxis.Role;

/**
 * Génère un numéro de référence
 * 
 * @since WebBMS 0.6
 */
public class NumeroReferenceFactory {
    private static final int MAX_LENGTH_REFERENCE = 26; // +1 du modulo de contrôle = les 27 positions
    private static int lengthIdRole = Integer.MIN_VALUE;
    private static int lengthIdExterneRole = Integer.MIN_VALUE;
    private static int lengthRefFacture = Integer.MIN_VALUE;
    private static int lengthTypeFacture = Integer.MIN_VALUE;
    private static String valNumBanque = "";

    /**
     * @param role
     * @param idExterneRole
     * @param typeSection
     * @param numeroDecompte
     * @return
     * @throws Exception
     */
    public static NumeroReference createNumeroReference(final Role role, final String idExterneRole,
            final TypeSection typeSection, final NumeroDecompte numeroDecompte) throws Exception {

        checkIsNull(role, idExterneRole, typeSection, numeroDecompte);
        init();

        String sRole = StringUtils.leftPad(removeNotLetterNotDigit(role.getValue()), lengthIdRole, '0');

        String sIdExterneRole = removeNotLetterNotDigit(idExterneRole.split("-")[0]);
        int iIdExterneRole = Integer.parseInt(sIdExterneRole);
        sIdExterneRole = StringUtils.leftPad(String.valueOf(iIdExterneRole), lengthIdExterneRole, '0');
        String sTypeSection = StringUtils.leftPad(removeNotLetterNotDigit(typeSection.getValue()), lengthTypeFacture,
                '0');
        String sNumeroDecompte = StringUtils.leftPad(removeNotLetterNotDigit(numeroDecompte.getValue()),
                lengthRefFacture);

        // idRole sur 2 positions
        if (sRole.length() > lengthIdRole) {
            sRole = sRole.substring(4, 6);
        }

        checkIfTooLong(sRole, sIdExterneRole, sTypeSection, sNumeroDecompte);

        return new NumeroReference(buildReferenceBVR(sRole, sIdExterneRole, sTypeSection, sNumeroDecompte));

    }

    /**
     * Construit un numéro de référence BVR de Globaz sans le modulo de contrôle
     * 
     * @param sRole
     * @param sIdExterneRole
     * @param sTypeSection
     * @param sNumeroDecompte
     * @throws Exception si le numéro de référence est trop long
     */
    private static String buildReferenceBVR(final String sRole, final String sIdExterneRole, final String sTypeSection,
            final String sNumeroDecompte) throws Exception {

        StringBuffer ref = new StringBuffer();
        ref.append(valNumBanque);
        ref.append(sRole);
        ref.append(sIdExterneRole);
        ref.append(sTypeSection);
        ref.append(sNumeroDecompte);

        if (ref.length() > MAX_LENGTH_REFERENCE) {
            throw new GlobazBusinessException(ExceptionMessage.NUM_REFERENCE_TROP_LONG);
        }
        return ref.toString();
    }

    private static void init() {
        if (lengthIdRole == Integer.MIN_VALUE) {
            lengthIdRole = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_ID_ROLE));
        }

        if (lengthIdExterneRole == Integer.MIN_VALUE) {
            lengthIdExterneRole = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_ID_EXTERNE_ROLE));
        }

        if (lengthRefFacture == Integer.MIN_VALUE) {
            lengthRefFacture = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_ID_PLAN));
        }

        if (lengthTypeFacture == Integer.MIN_VALUE) {
            lengthTypeFacture = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_TYPE_PLAN));
        }

        if (valNumBanque == null || valNumBanque.length() == 0) {
            valNumBanque = CAApplication.getApplicationOsiris().getProperty(IntReferenceBVRParser.VAL_NUM_BANQUE, "");
        }
    }

    /**
     * @param role
     * @param idExterneRole
     * @param typeSection
     * @param numeroDecompte
     */
    private static void checkIsNull(final Role role, final String idExterneRole, final TypeSection typeSection,
            final NumeroDecompte numeroDecompte) {
        if (role == null) {
            throw new NullPointerException("Role is null !");
        }

        if (idExterneRole == null) {
            throw new NullPointerException("IdExterneRole is null !");
        }

        if (typeSection == null) {
            throw new NullPointerException("TypeSection is null !");
        }

        if (numeroDecompte == null) {
            throw new NullPointerException("NumeroDecompte is null !");
        }
    }

    /**
     * Supprime tout caractère qui n'est pas une lettre ou un chiffre
     * 
     * @author: sel Créé le : 19 déc. 06
     * @param val
     * @return une chaine contenant que des lettres et des chiffres
     */
    private static String removeNotLetterNotDigit(final String val) {
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < val.length(); i++) {
            char c = val.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                strBuf.append(c);
            }
        }
        return strBuf.toString();
    }

    /**
     * @param sRole
     * @param sIdExterneRole
     * @param sTypeSection
     * @param sNumeroDecompte
     */
    private static void checkIfTooLong(final String sRole, final String sIdExterneRole, final String sTypeSection,
            final String sNumeroDecompte) {
        if (lengthIdRole < sRole.length()) {
            throw new IllegalArgumentException("Role is too long !");
        }

        if (lengthIdExterneRole < sIdExterneRole.length()) {
            throw new IllegalArgumentException("IdExterneRole too long !");
        }

        if (lengthTypeFacture < sTypeSection.length()) {
            throw new IllegalArgumentException("TypeSection too long !");
        }

        if (lengthRefFacture < sNumeroDecompte.length()) {
            throw new IllegalArgumentException("NumeroDecompte too long !");
        }
    }
}
