package globaz.osiris.db.ordres;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.utils.CAReferenceBVR;
import globaz.osiris.external.IntRole;
import globaz.osiris.parser.IntReferenceBVRParser;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import ch.globaz.common.sql.QueryExecutor;

/**
 * Date de création : (18.02.2002 15:33:30)
 * 
 * @author sch
 */
public class CARefBVRParserTemp implements IntReferenceBVRParser {
    private String idCompteAnnexe = new String();
    private String idExterneRole = new String();
    private String idExterneSection = new String();
    private String idPlanPaiement = new String();
    private String idRole = new String();
    private String idSection = new String();
    private String idTiers = new String();
    private String idTypeSection = new String();
    private int lenIdExterneRole = 0;
    private int lenIdExterneRoleOld = 0;
    private int lenIdExterneSection = 0;
    private int lenIdRole = 0;
    private int lenNoPoste = 0;
    private int lenTypePlan = 0;
    private int lenTypeSection = 0;
    private boolean planPaiement = false;
    private String planPaiementIdentifier;
    private int posIdExterneRole = 0;
    private int posIdExterneRoleOld = 0;
    private int posIdExterneRoleOld2 = 0;
    private int posIdExterneSection = 0;
    private int posIdRole = 0;
    private int posIdRoleOld = 0;
    private int posNoPoste = 0;
    private int posTypePlan = 0;
    private int posTypeSection = 0;
    private String reference = new String();
    private IntRole role = null;
    private BISession session;
    private int valIdRole = 0;
    private int valIdRoleOld = 0;
    private int valTypeSection = 0;
    private int valTypeSectionOld = 0;
    private String modeBulletinNeutre = new String();

    /**
     * Constructor. Retrouve les index de la référence du BVR depuis OSIRIS.properties.
     */
    public CARefBVRParserTemp() {
        super();

        // Charger les paramètres du parser
        try {
            posIdExterneRole = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.POS_ID_EXTERNE_ROLE));
            lenIdExterneRole = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_ID_EXTERNE_ROLE));
            posIdRole = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.POS_ID_ROLE));
            posIdRoleOld = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.POS_OLD_ID_ROLE));
            lenIdRole = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_ID_ROLE));
            valIdRole = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.VAL_ID_ROLE));
            posIdExterneSection = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.POS_ID_PLAN));
            lenIdExterneSection = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_ID_PLAN));
            posTypeSection = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.POS_TYPE_SECTION));
            lenTypeSection = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_TYPE_SECTION));
            valTypeSection = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.VAL_TYPE_SECTION));
            valTypeSectionOld = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.VAL_OLD_TYPE_SECTION));
            posIdExterneRoleOld = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.POS_OLD_ID_EXTERNE_ROLE));
            posIdExterneRoleOld2 = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.POS_OLD2_ID_EXTERNE_ROLE));
            lenIdExterneRoleOld = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_OLD_ID_EXTERNE_ROLE));
            posNoPoste = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.POS_OLD_NO_POSTE));
            lenNoPoste = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_OLD_NO_POSTE));
            valIdRoleOld = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.VAL_OLD_ID_ROLE));
            posTypePlan = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.POS_TYPE_PLAN));
            lenTypePlan = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_TYPE_PLAN));
            planPaiementIdentifier = CAApplication.getApplicationOsiris().getProperty(IntReferenceBVRParser.IDENT_PLAN);

            // Si le tiers n'est pas instancié
            if (role == null) {
                CAApplication currentApplication = CAApplication.getApplicationOsiris();
                BISession session = currentApplication.newSession();
                role = (IntRole) globaz.globall.db.GlobazServer.getCurrentSystem()
                        .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                        .getImplementationFor(session, IntRole.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * Retourne l'idExterneRole (correspond au numéro d'affilié)
     * 
     * @return
     */
    public String getIdExterneRole() {
        return idExterneRole;
    }

    /**
     * Retourne l'id externe de la section
     * 
     * @return
     */
    @Override
    public String getIdExterneSection() {
        return idExterneSection;
    }

    @Override
    public String getIdPlanPaiement() {
        return idPlanPaiement;
    }

    /**
     * Retourne l'id du role
     * 
     * @return
     */
    public String getIdRole() {
        return idRole;
    }

    @Override
    public String getIdSection() {
        return idSection;
    }

    /**
     * Retourne l'id du tiers
     * 
     * @return
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * Retourne l'id du type de section
     * 
     * @return
     */
    @Override
    public String getIdTypeSection() {
        return idTypeSection;
    }

    @Override
    public BISession getISession() {
        return session;
    }

    /**
     * Retourne le role
     * 
     * @return
     */
    public IntRole getRole() {
        return role;
    }

    @Override
    public boolean isModeCreditBulletinNeutre() {
        String modeParDefaut = CAApplication.getApplicationOsiris().getCAParametres().getModeParDefautBulletinNeutre();
        return CACompteAnnexe.CS_BN_CREDIT.equals(modeBulletinNeutre)
                || (CACompteAnnexe.CS_BN_DEFAUT.equals(modeBulletinNeutre) && CACompteAnnexe.CS_BN_CREDIT
                        .equals(modeParDefaut));
    }

    /**
     * Initialise les variables membres de la classe.
     */
    private void initMembres() {
        // Initialiser
        String ZERO = "0";
        idCompteAnnexe = ZERO;
        idPlanPaiement = ZERO;
        idSection = ZERO;
        reference = ZERO;
        idTiers = ZERO;
        idRole = ZERO;
        idExterneRole = ZERO;
        idExterneSection = ZERO;
        idTypeSection = ZERO;
    }

    @Override
    public boolean isPlanPaiement() {
        return planPaiement;
    }

    /**
     * renseigne l'idRole et l'idExterneRole pour les LSV qui ont l'idCompteAnnexe dans la référence
     */
    private void parseCompteAnnexeByIdCompteAnnexe() {

        String sIdCompteAnnexe = reference.substring(posIdExterneRole + lenIdExterneRole - 8, posIdExterneRole
                + lenIdExterneRole - 1);

        // Instancier un compte annexe
        CACompteAnnexe cpt = new CACompteAnnexe();
        cpt.setISession(getISession());
        cpt.setIdCompteAnnexe(sIdCompteAnnexe);

        // Tenter de récupérer le compte annexe
        try {
            cpt.retrieve();
        } catch (Exception e) {
        }

        // Si trouvé
        if (!cpt.hasErrors() && !cpt.isNew()) {
            idCompteAnnexe = cpt.getIdCompteAnnexe();
            idExterneRole = cpt.getIdExterneRole();
        }
    }

    /**
     * renseigne l'idRole et l'idExterneRole
     */
    private void parseIdCompteAnnexe() {
        // Sortir si role ko
        if (role == null) {
            return;
        }

        // Si l'id du rôle est forcée
        if (valIdRole != 0) {
            idRole = String.valueOf(valIdRole);
        } else {
            // Sinon, on l'extrait du numéro de référence
            idRole = reference.substring(posIdRole - 1, posIdRole + lenIdRole - 1);
            idRole = IntRole.ROLE_AFFILIE.substring(0, IntRole.ROLE_AFFILIE.length() - 2) + idRole;
        }

        // Extraire l'id externe et le formatter
        BigInteger bIdExterneRole = new BigInteger(reference.substring(posIdExterneRole - 1, posIdExterneRole
                + lenIdExterneRole - 1));
        String sIdExterneRole = bIdExterneRole.toString();
        if (idRole.equals(CaisseHelperFactory.CS_AFFILIE_PARITAIRE)
                || idRole.equals(CaisseHelperFactory.CS_AFFILIE_PERSONNEL)
                || idRole.equals(IntRole.ROLE_ADMINISTRATEUR)) {
            String format = CAApplication.getApplicationOsiris().getCAParametres().getFormatAdminNumAffilie();
            if (!JadeStringUtil.isBlank(format)) {
                format = JadeStringUtil.removeChar(format, '.');
                format = JadeStringUtil.removeChar(format, '-');
                if (sIdExterneRole.length() < format.length()) {
                    sIdExterneRole = JadeStringUtil.fillWithZeroes(sIdExterneRole, format.length());

                }
            }
        }
        idExterneRole = role.formatIdExterneRole(idRole, sIdExterneRole);

        // Instancier un compte annexe
        CACompteAnnexe cpt = new CACompteAnnexe();
        cpt.setISession(getISession());
        cpt.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
        cpt.setIdRole(idRole);
        cpt.setIdExterneRole(idExterneRole);

        // Tenter de récupérer le compte annexe
        try {
            cpt.retrieve();
        } catch (Exception e) {
        }

        // Si trouvé
        if (!cpt.hasErrors() && !cpt.isNew()) {
            idCompteAnnexe = cpt.getIdCompteAnnexe();
            modeBulletinNeutre = cpt.getModeBulletinNeutre();
        }
    }

    /**
     * renseigne l'idRole et l'idExterneRole pour les anciennes factures
     */
    private void parseIdCompteAnnexeAncien() {
        // future : posIdRole = 1 (7)
        // posIdExterneRole = 3 (9)
        // lenIdExterneRole = 13 (7)

        // Sortir si role ko
        if (role == null) {
            return;
        }

        // Si l'id du rôle est forcée
        if (valIdRole != 0) {
            idRole = String.valueOf(valIdRole);
        } else {
            // Sinon, on l'extrait du numéro de référence
            idRole = reference.substring(posIdRoleOld - 1, posIdRoleOld + lenIdRole - 1);
            idRole = IntRole.ROLE_AFFILIE.substring(0, IntRole.ROLE_AFFILIE.length() - 2) + idRole;
        }

        // Extraire l'id externe et le formatter
        BigInteger bIdExterneRole = new BigInteger(reference.substring(posIdExterneRoleOld2 - 1, posIdExterneRoleOld2
                + lenIdExterneRoleOld - 1));
        idExterneRole = role.formatIdExterneRole(idRole, bIdExterneRole.toString());

        // Instancier un compte annexe
        CACompteAnnexe cpt = new CACompteAnnexe();
        cpt.setISession(getISession());
        cpt.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
        cpt.setIdRole(idRole);
        cpt.setIdExterneRole(idExterneRole);

        // Tenter de récupérer le compte annexe
        try {
            cpt.retrieve();
        } catch (Exception e) {
        }

        // Si trouvé
        if (!cpt.hasErrors() && !cpt.isNew()) {
            idCompteAnnexe = cpt.getIdCompteAnnexe();
        }
    }

    /**
     * Cette méthode permet de récupérer l'idCompteAnnexe de la ligne de référence (anciennes factures)
     */
    private void parseIdCompteAnnexeOld() {
        // Sortir si role ko
        if (role == null) {
            return;
        }

        // On force le rôle
        idRole = String.valueOf(valIdRoleOld);

        // Extraire l'id externe et le formatter
        BigInteger bIdExterneRoleOld = new BigInteger(reference.substring(posIdExterneRoleOld - 1, posIdExterneRoleOld
                + lenIdExterneRoleOld - 1));
        idExterneRole = role.formatIdExterneRole(idRole, bIdExterneRoleOld.toString());

        // Instancier un compte annexe
        CACompteAnnexe cpt = new CACompteAnnexe();
        cpt.setISession(getISession());
        cpt.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
        cpt.setIdRole(idRole);
        cpt.setIdExterneRole(idExterneRole);

        // Tenter de récupérer le compte annexe
        try {
            cpt.retrieve();
        } catch (Exception e) {
        }

        // Si trouvé
        if (!cpt.hasErrors() && !cpt.isNew()) {
            idCompteAnnexe = cpt.getIdCompteAnnexe();
        }
    }

    /**
     * Cette méthode permet de récupérer l'idPlanPaiement de la ligne de référence
     */
    private void parseIdEcheancePlan() {
        if (isPlanPaiement()) {
            idPlanPaiement = reference
                    .substring(posIdExterneSection - 1, posIdExterneSection + lenIdExterneSection - 1);
        }
    }

    /**
     * Cette méthode permet de récupérer l'idTypeSection de la ligne de référence
     */
    private void parseIdSection() {
        if (!isPlanPaiement()) {
            if (valTypeSection != 0) {
                // Si le type de section est forcé
                idTypeSection = String.valueOf(valTypeSection);
            } else {
                // Sinon, on l'extrait du numéro de référence
                idTypeSection = reference.substring(posTypeSection - 1, posTypeSection + lenTypeSection - 1);
            }
            idTypeSection = new BigInteger(idTypeSection).toString();

            // Extraire l'id externe
            idExterneSection = reference.substring(posIdExterneSection - 1, posIdExterneSection + lenIdExterneSection
                    - 1);

            // Instancier une nouvelle section
            CASection sec = new CASection();
            sec.setISession(getISession());
            sec.setAlternateKey(CASection.AK_IDEXTERNE);
            sec.setIdTypeSection(idTypeSection);
            sec.setIdExterne(idExterneSection);
            sec.setIdCompteAnnexe(getIdCompteAnnexe());

            // Tenter de récupérer le compte annexe
            try {
                sec.retrieve();
            } catch (Exception e) {
            }

            // Nouvelle tentative en supprimant les leading zero pour ancienne
            // facture
            if (sec.isNew() && idExterneSection.startsWith("0")) {
                try {
                    String s = JadeStringUtil.stripLeading(idExterneSection, '0');
                    sec.setIdExterne(s);
                    sec.retrieve();
                } catch (Exception e) {
                }
            }

            // S'il y a qqch, on prend
            if (!sec.hasErrors() && !sec.isNew()) {
                idSection = sec.getIdSection();
            }
        }
    }

    /**
     * Cette méthode permet de récupérer le numéro de poste de la ligne de référence
     */
    private void parseNoPoste() {
        // Le type de section est forcé
        idTypeSection = String.valueOf(valTypeSectionOld);
        // Instancier une nouvelle section
        CASection sec = new CASection();
        sec.setISession(getISession());
        sec.setAlternateKey(CASection.AK_IDEXTERNE);
        sec.setIdTypeSection(idTypeSection);
        // On attribue le siècle devant le numéro de décompte
        String sSiecle = "20";
        if (Integer.parseInt(reference.substring(posNoPoste - 1, posNoPoste + 1)) > 50) {
            sSiecle = "19";
        }
        sec.setIdExterne(sSiecle + reference.substring(posNoPoste - 1, posNoPoste + lenNoPoste - 1));
        sec.setIdCompteAnnexe(getIdCompteAnnexe());

        // Tenter de récupérer le compte annexe
        try {
            sec.retrieve();
        } catch (Exception e) {
        }

        // S'il y a qqch, on prend
        if (!sec.hasErrors() && !sec.isNew()) {
            idSection = sec.getIdSection();
        }
    }

    /**
     * Cette méthode permet de récupérer le Type de Plan de la ligne de référence
     */
    private void parseTypePlan() {
        planPaiement = reference.substring(posTypePlan - 1, posTypePlan + lenTypePlan - 1).equals(
                planPaiementIdentifier);
    }

    /**
     * @see globaz.osiris.parser.IntReferenceBVRParser#setISession(globaz.globall.api.BISession)
     */
    @Override
    public void setISession(BISession newSession) throws Exception {
        session = newSession;
    }

    /**
     * @param newLenIdExterneRole
     */
    public void setLenIdExterneRole(int newLenIdExterneRole) {
        lenIdExterneRole = newLenIdExterneRole;
    }

    /**
     * @param newLenIdExterneSection
     */
    public void setLenIdExterneSection(int newLenIdExterneSection) {
        lenIdExterneSection = newLenIdExterneSection;
    }

    /**
     * @param newLenIdRole
     */
    public void setLenIdRole(int newLenIdRole) {
        lenIdRole = newLenIdRole;
    }

    public void setLenTypeSection(int newLenTypeSection) {
        lenTypeSection = newLenTypeSection;
    }

    /**
     * @param newPosIdExterneRole
     */
    public void setPosIdExterneRole(int newPosIdExterneRole) {
        posIdExterneRole = newPosIdExterneRole;
    }

    /**
     * @param newPosIdExterneSection
     */
    public void setPosIdExterneSection(int newPosIdExterneSection) {
        posIdExterneSection = newPosIdExterneSection;
    }

    /**
     * @param newPosIdRole
     */
    public void setPosIdRole(int newPosIdRole) {
        posIdRole = newPosIdRole;
    }

    /**
     * @param newPosTypeSection
     */
    public void setPosTypeSection(int newPosTypeSection) {
        posTypeSection = newPosTypeSection;
    }

    @Override
    public void setReference(String numeroReference, BSession session, String numInterneLsv) throws Exception {
        initMembres();

        // Vérifier le numéro de référence
        if (JadeStringUtil.isBlank(numeroReference)) {
            throw new Exception(session.getLabel("5306"));
        }

        // Vérifier si numérique et stocker sur 27 positions
        try {
            reference = JadeStringUtil.rightJustifyInteger((new BigDecimal(numeroReference.trim())).toString(), 27);
        } catch (Exception e) {
            throw new Exception(session.getLabel("5306"));
        }
        // Contrôler s'il y a des zéro
        String testRef = reference.substring(0, 12);
        String testRefIdRole = reference.substring(0, lenIdRole);
        String testRefLsv = reference.substring(0, 6);
        String testLsvSpec = reference.substring(6, 8);

        if (JadeStringUtil.isBlankOrZero(numInterneLsv)) {
            numInterneLsv = "000000";
        }

        // Récupérer le compte annexe
        if (!testRef.equals("000000000000")) {
            if (testLsvSpec.equals("99") && testRefLsv.equals(numInterneLsv.substring(0, 6))) {
                parseCompteAnnexeByIdCompteAnnexe();
            } else if (CAReferenceBVR.IDENTIFIANT_REF_IDCOMPTEANNEXE.equals(testRefIdRole)) {
                idCompteAnnexe = parseIdCompteAnnexeForRefIdCompteAnnexe(reference);
            } else if (!testRefIdRole.equals("00") && !testRefLsv.equals(numInterneLsv.substring(0, 6))) {
                parseIdCompteAnnexe();
            } else {
                parseIdCompteAnnexeAncien();
            }
            parseTypePlan();
            parseIdSection();

            retrieveFieldEmpty();

            parseIdEcheancePlan();
        } else {
            // Ancienne facture
            parseIdCompteAnnexeOld();
            parseNoPoste();
        }
    }

    /**
     * Methode permettant la recherche idSection et idCompteAnnexe si ils n'ont pas été trouvés.
     * 
     */
    private void retrieveFieldEmpty() {
        if (JadeStringUtil.isDecimalEmpty(idSection) && JadeStringUtil.isDecimalEmpty(idCompteAnnexe)) {

            //@formatter:off
            String req = " SELECT ca.IDCOMPTEANNEXE, se.IDSECTION "
                    + "    FROM schema.cacptap ca  "
                    + "    inner join schema.casectp se on ca.idcompteannexe = se.idcompteannexe "
                    + "    where idrole = " + idRole 
                    + "    and idexternerole like '" + idExterneRole + "%'"
                    + "    and se.idexterne = '" + idExterneSection + "'";
          //@formatter:on

            List<CARefBVRParserCompteAnnexe> compteannexe = QueryExecutor.execute(req,
                    CARefBVRParserCompteAnnexe.class, (BSession) getISession());

            if (compteannexe.size() == 1) {
                idCompteAnnexe = compteannexe.get(0).getIdCompteAnnexe();
                idSection = compteannexe.get(0).getIdSection();
            }

        }
    }

    private String parseIdCompteAnnexeForRefIdCompteAnnexe(String reference) {
        BigInteger bIdExterneRole = new BigInteger(reference.substring(posIdExterneRole - 1, posIdExterneRole
                + lenIdExterneRole - 1));
        return bIdExterneRole.toString();
    }

    /**
     * @param newRole
     */
    public void setRole(IntRole newRole) {
        role = newRole;
    }

    /**
     * @param newValIdRole
     */
    public void setValIdRole(int newValIdRole) {
        valIdRole = newValIdRole;
    }

    /**
     * @param newValTypeSection
     */
    public void setValTypeSection(int newValTypeSection) {
        valTypeSection = newValTypeSection;
    }
}
