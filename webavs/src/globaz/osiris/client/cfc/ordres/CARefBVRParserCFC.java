package globaz.osiris.client.cfc.ordres;

import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.external.IntRole;
import globaz.osiris.parser.IntReferenceBVRParser;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (18.02.2002 15:33:30)
 * 
 * @author: Administrator
 */
public class CARefBVRParserCFC implements IntReferenceBVRParser {
    private java.lang.String idCompteAnnexe = new String();
    private java.lang.String idEcheancePlan = new String();
    private java.lang.String idExterneRole = new String();
    private java.lang.String idExterneSection = new String();
    private java.lang.String idRole = new String();
    private java.lang.String idSection = new String();
    private java.lang.String idTiers = new String();
    private java.lang.String idTypeSection = new String();
    private int lenIdExterneRole = 0;
    private int lenIdExterneRoleOld = 0;
    private int lenIdExterneSection = 0;
    private int lenIdRole = 0;
    private int lenNoPoste = 0;
    private int lenTypeSection = 0;
    private int posIdExterneRole = 0;
    private int posIdExterneRoleOld = 0;
    private int posIdExterneSection = 0;
    private int posIdRole = 0;
    private int posNoPoste = 0;
    private int posTypeSection = 0;
    private java.lang.String reference = new String();
    private IntRole role = null;
    private BISession session;
    private int valIdRole = 0;
    private int valIdRoleOld = 0;
    private int valTypeSection = 0;
    private int valTypeSectionOld = 0;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (19.02.2002 09:26:42)
     */
    public CARefBVRParserCFC() {
        super();

        // Charger les param�tres du parser
        try {
            posIdExterneRole = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.POS_ID_EXTERNE_ROLE));
            lenIdExterneRole = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_ID_EXTERNE_ROLE));
            posIdRole = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.POS_ID_ROLE));
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
                    IntReferenceBVRParser.VAL_CFC_TYPE_SECTION));
            posIdExterneRoleOld = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.POS_CFC_ID_EXTERNE_ROLE));
            lenIdExterneRoleOld = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_CFC_ID_EXTERNE_ROLE));
            posNoPoste = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.POS_CFC_NO_POSTE));
            lenNoPoste = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_CFC_NO_POSTE));
            valIdRoleOld = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.VAL_CFC_ID_ROLE));

            // Si le tiers n'est pas instanci�
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

    /**
     * Cette m�thode permet de r�cup�rer l'idCompteAnnexe de la ligne de r�f�rence
     */
    private void _parseIdCompteAnnexe() {

        // Sortir si role ko
        if (role == null) {
            return;
        }

        // Si l'id du r�le est forc�e
        if (valIdRole != 0) {
            idRole = String.valueOf(valIdRole);
            // Sinon, on l'extrait du num�ro de r�f�rence
        } else {
            idRole = reference.substring(posIdRole - 1, posIdRole + lenIdRole - 1);
        }

        // Extraire l'id externe et le formatter
        BigInteger bIdExterneRole = new BigInteger(reference.substring(posIdExterneRole - 1, posIdExterneRole
                + lenIdExterneRole - 1));
        idExterneRole = role.formatIdExterneRole(idRole, bIdExterneRole.toString());

        // Instancier un compte annexe
        CACompteAnnexe cpt = new CACompteAnnexe();
        cpt.setISession(getISession());
        cpt.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
        cpt.setIdRole(idRole);
        cpt.setIdExterneRole(idExterneRole);

        // Tenter de r�cup�rer le compte annexe
        try {
            cpt.retrieve();
        } catch (Exception e) {
        }

        // Si trouv�
        if (!cpt.isNew() && !cpt.hasErrors()) {
            idCompteAnnexe = cpt.getIdCompteAnnexe();
        }
    }

    /**
     * Cette m�thode permet de r�cup�rer l'idCompteAnnexe de la ligne de r�f�rence (anciennes factures)
     */
    private void _parseIdCompteAnnexeOld() {

        // Sortir si role ko
        if (role == null) {
            return;
        }

        // On force le r�le
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

        // Tenter de r�cup�rer le compte annexe
        try {
            cpt.retrieve();
        } catch (Exception e) {
        }

        // Si trouv�
        if (!cpt.isNew() && !cpt.hasErrors()) {
            idCompteAnnexe = cpt.getIdCompteAnnexe();
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (20.02.2002 16:30:35)
     */
    private void _parseIdEcheancePlan() {
    }

    /**
     * Cette m�thode permet de r�cup�rer l'idSection de la ligne de r�f�rence
     */
    private void _parseIdSection() {

        // Si le type de section est forc�
        if (valTypeSection != 0) {
            idTypeSection = String.valueOf(valTypeSection);
            // Sinon, on l'extrait du num�ro de r�f�rence
        } else {
            idTypeSection = reference.substring(posTypeSection - 1, posTypeSection + lenTypeSection - 1);
        }
        idTypeSection = new BigInteger(idTypeSection).toString();

        // Extraire l'id externe
        idExterneSection = reference.substring(posIdExterneSection - 1, posIdExterneSection + lenIdExterneSection - 1);

        // Instancier une nouvelle section
        CASection sec = new CASection();
        sec.setISession(getISession());
        sec.setAlternateKey(CASection.AK_IDEXTERNE);
        sec.setIdTypeSection(idTypeSection);
        sec.setIdExterne(idExterneSection);
        sec.setIdCompteAnnexe(getIdCompteAnnexe());

        // Tenter de r�cup�rer le compte annexe
        try {
            sec.retrieve();
        } catch (Exception e) {
        }

        // S'il y a qqch, on prend
        if (!sec.isNew() && !sec.hasErrors()) {
            idSection = sec.getIdSection();
        }
    }

    /**
     * Cette m�thode permet de r�cup�rer le num�ro de poste de la ligne de r�f�rence
     */
    private void _parseNoPoste() {
        // Le type de section est forc�
        idTypeSection = String.valueOf(valTypeSectionOld);
        // Instancier une nouvelle section
        CASection sec = new CASection();
        sec.setISession(getISession());
        sec.setAlternateKey(CASection.AK_IDEXTERNE);
        sec.setIdTypeSection(idTypeSection);
        sec.setIdExterne(reference.substring(posNoPoste - 1, posNoPoste + lenNoPoste - 1));
        sec.setIdCompteAnnexe(getIdCompteAnnexe());

        // Tenter de r�cup�rer le compte annexe
        try {
            sec.retrieve();
        } catch (Exception e) {
        }

        // S'il y a qqch, on prend
        if (!sec.isNew() && !sec.hasErrors()) {
            idSection = sec.getIdSection();
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.02.2002 15:54:32)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdExterneRole() {
        return idExterneRole;
    }

    @Override
    public String getIdExterneSection() {
        return idExterneSection;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.02.2002 16:20:52)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdPlanPaiement() {
        return idEcheancePlan;
    }

    public String getIdRole() {
        return idRole;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.02.2002 15:54:47)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdSection() {
        return idSection;
    }

    public String getIdTiers() {
        return idTiers;
    }

    @Override
    public String getIdTypeSection() {
        return idTypeSection;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.02.2002 15:43:18)
     * 
     * @return java.lang.String
     */
    @Override
    public BISession getISession() {
        return session;
    }

    public IntRole getRole() {
        return role;
    }

    @Override
    public boolean isModeCreditBulletinNeutre() {
        return false;
    }

    /**
     * @see globaz.osiris.parser.IntReferenceBVRParser#isPlanPaiement()
     */
    @Override
    public boolean isPlanPaiement() {
        return false;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (19.02.2002 09:27:14)
     * 
     * @param numeroReference
     *            java.lang.String
     */
    @Override
    public void setISession(BISession newSession) throws Exception {
        session = newSession;
    }

    public void setLenIdExterneRole(int newLenIdExterneRole) {
        lenIdExterneRole = newLenIdExterneRole;
    }

    public void setLenIdExterneSection(int newLenIdExterneSection) {
        lenIdExterneSection = newLenIdExterneSection;
    }

    public void setLenIdRole(int newLenIdRole) {
        lenIdRole = newLenIdRole;
    }

    public void setLenTypeSection(int newLenTypeSection) {
        lenTypeSection = newLenTypeSection;
    }

    public void setPosIdExterneRole(int newPosIdExterneRole) {
        posIdExterneRole = newPosIdExterneRole;
    }

    public void setPosIdExterneSection(int newPosIdExterneSection) {
        posIdExterneSection = newPosIdExterneSection;
    }

    public void setPosIdRole(int newPosIdRole) {
        posIdRole = newPosIdRole;
    }

    public void setPosTypeSection(int newPosTypeSection) {
        posTypeSection = newPosTypeSection;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (19.02.2002 09:45:05)
     * 
     * @param numeroReference
     *            java.lang.String
     */
    @Override
    public void setReference(String numeroReference, globaz.globall.db.BSession session, String numInterneLsv)
            throws Exception {

        // Initialiser
        String ZERO = "0";
        idCompteAnnexe = ZERO;
        idEcheancePlan = ZERO;
        idSection = ZERO;
        reference = ZERO;
        idTiers = ZERO;
        idRole = ZERO;
        idExterneRole = ZERO;
        idExterneSection = ZERO;
        idTypeSection = ZERO;

        // V�rifier le num�ro de r�f�rence
        if (JadeStringUtil.isBlank(numeroReference)) {
            throw new Exception(session.getLabel("5306"));
        }

        // V�rifier si num�rique et stocker sur 27 positions
        try {
            reference = JadeStringUtil.rightJustifyInteger((new BigDecimal(numeroReference.trim())).toString(), 27);
        } catch (Exception e) {
            throw new Exception(session.getLabel("5306"));
        }
        // Contr�ler s'il y a des z�ro
        String testRef = reference.substring(0, 12);
        // Nouvelle facture
        if (!testRef.equals("000000000000")) {
            // R�cup�rer le compte annexe
            _parseIdCompteAnnexe();
            _parseIdSection();
            _parseIdEcheancePlan();
            // Ancienne facture
        } else {
            _parseIdCompteAnnexeOld();
            _parseNoPoste();
        }
    }

    public void setRole(IntRole newRole) {
        role = newRole;
    }

    public void setValIdRole(int newValIdRole) {
        valIdRole = newValIdRole;
    }

    public void setValTypeSection(int newValTypeSection) {
        valTypeSection = newValTypeSection;
    }
}
