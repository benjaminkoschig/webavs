package globaz.osiris.db.ordres;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.utils.CAReferenceBVR;
import globaz.osiris.external.IntRole;
import globaz.osiris.parser.IntReferenceBVRParser;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Parser d'une ligne BVR provenant d'une banque.
 * 
 * @since WebBMS 0.6
 */
public class CARefBVRBanqueParser implements IntReferenceBVRParser {
    private static final String ZERO = "0";
    private String idCompteAnnexe = "";
    private String idExterneRole = "";
    private String idExterneSection = "";
    private String idPlanPaiement = "";
    private String idRole = "";
    private String idSection = "";
    private String idTiers = "";
    private String idTypeSection = "";
    private int lenIdExterneRole = 0;
    private int lenIdExterneSection = 0;
    private int lenIdRole = 0;
    private int lenTypePlan = 0;
    private int lenTypeSection = 0;
    private boolean planPaiement = false;
    private String planPaiementIdentifier;
    private int posIdExterneRole = 0;
    private int posIdExterneSection = 0;
    private int posIdRole = 0;
    private int posTypePlan = 0;
    private int posTypeSection = 0;
    private String reference = "";
    private IntRole role = null;
    private BISession session;
    private int valIdRole = 0;
    private int valTypeSection = 0;
    private String modeBulletinNeutre = "";

    /**
     * Constructor. Retrouve les index de la référence du BVR depuis OSIRIS.properties.
     */
    public CARefBVRBanqueParser() {
        super();

        // Charger les paramètres du parser
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
    public void setReference(String numeroReference, BSession session, String numInterneLsv) throws Exception {
        resetMembres();

        reference = checkNumeroReference(numeroReference, session);

        if (!retrieveSectionByReferenceBVR(session)) {

            String testRefIdRole = reference.substring(posIdRole - 1, posIdRole - 1 + lenIdRole);

            // Récupérer le compte annexe
            if (CAReferenceBVR.IDENTIFIANT_REF_IDCOMPTEANNEXE.equals(testRefIdRole)) {
                idCompteAnnexe = parseIdCompteAnnexeForRefIdCompteAnnexe(reference);
            } else if (!testRefIdRole.equals("00")) {
                parseIdCompteAnnexe();
            }
            parseTypePlan();
            parseIdSection();
            parseIdEcheancePlan();

        }
    }

    /**
     * @param session
     * @throws Exception
     */
    private boolean retrieveSectionByReferenceBVR(BSession session) throws Exception {
        CASection section = new CASection();
        section.setSession(session);
        section.setAlternateKey(CASection.AK_REFERENCE_BVR);
        section.setReferenceBvr(reference);
        section.retrieve();

        // S'il y a qqch, on prend
        if (!section.hasErrors() && !section.isNew()) {
            idSection = section.getIdSection();
            idTypeSection = section.getTypeSection().getIdTypeSection();
            idExterneSection = section.getIdExterne();

            idRole = section.getCompteAnnexe().getIdRole();
            idExterneRole = section.getCompteAnnexe().getIdExterneRole();
            idCompteAnnexe = section.getIdCompteAnnexe();
            return true;
        }
        return false;
    }

    /**
     * @param numeroReference
     * @param session
     * @throws Exception
     */
    private String checkNumeroReference(String numeroReference, BSession session) throws Exception {
        // Vérifier le numéro de référence
        if (JadeStringUtil.isBlank(numeroReference)) {
            throw new Exception(session.getLabel("5306"));
        }

        // Vérifier si numérique et stocker sur 27 positions
        try {
            return JadeStringUtil.rightJustifyInteger((new BigDecimal(numeroReference.trim())).toString(), 27);
        } catch (Exception e) {
            throw new Exception(session.getLabel("5306"));
        }
    }

    /**
     * Initialise les variables membres de la classe.
     */
    private void resetMembres() {
        // Initialiser
        idCompteAnnexe = ZERO;
        idPlanPaiement = ZERO;
        idSection = ZERO;
        reference = ZERO;
        idTiers = ZERO;
        idRole = ZERO;
        idExterneRole = ZERO;
        idExterneSection = ZERO;
        idTypeSection = ZERO;

        planPaiement = false;
    }

    @Override
    public boolean isModeCreditBulletinNeutre() {
        String modeParDefaut = CAApplication.getApplicationOsiris().getCAParametres().getModeParDefautBulletinNeutre();
        return CACompteAnnexe.CS_BN_CREDIT.equals(modeBulletinNeutre)
                || (CACompteAnnexe.CS_BN_DEFAUT.equals(modeBulletinNeutre) && CACompteAnnexe.CS_BN_CREDIT
                        .equals(modeParDefaut));
    }

    /**
     * renseigne l'idRole et l'idExterneRole
     */
    private void parseIdCompteAnnexe() {
        // Sortir si role ko
        if (role == null) {
            return;
        }

        idRole = findIdRole();

        // Extraire l'id externe et le formatter
        String idExterneRoleLike = extractAndFormatIdExterneRole();

        CACompteAnnexe compteAnnexe = findCompteAnnexe(idExterneRoleLike, idRole);
        if (compteAnnexe == null) {
            // le compte annexe n'existe pas encore. Première facture pour un affilié
            // On va chercher dans l'affiliation si un affilié correspond et quel est le numéro d'affilié/idExterneRole
            // complet. Dans la référence BVR nous n'avons pas le numéro entier par manque de place.
            AFAffiliation affiliation = findAffiliation(idExterneRoleLike);
            if (affiliation != null) {
                compteAnnexe = createCompteAnnexe(affiliation, idRole);
            }
        }

        if (compteAnnexe != null) {
            idCompteAnnexe = compteAnnexe.getIdCompteAnnexe();
            modeBulletinNeutre = compteAnnexe.getModeBulletinNeutre();
        }
    }

    /**
     * @param idExterneRoleLike
     * @return
     */
    private CACompteAnnexe findCompteAnnexe(String idExterneRoleLike, String idRole) {
        CACompteAnnexeManager manager = new CACompteAnnexeManager();
        manager.setISession(getISession());
        manager.setLikeIdExterneRole(idExterneRoleLike);
        manager.setForIdRole(idRole);

        // Tenter de récupérer le compte annexe
        try {
            manager.find();
        } catch (Exception e) {
        }

        if (!manager.hasErrors() && manager.size() == 1) {
            return (CACompteAnnexe) manager.getFirstEntity();
        }

        return null;
    }

    /**
     * @return
     */
    private String extractAndFormatIdExterneRole() {
        BigInteger bIdExterneRole = new BigInteger(reference.substring(posIdExterneRole - 1, posIdExterneRole
                + lenIdExterneRole - 1));
        String sIdExterneRole = bIdExterneRole.toString();
        if (idRole.equals(CaisseHelperFactory.CS_AFFILIE_PARITAIRE)
                || idRole.equals(CaisseHelperFactory.CS_AFFILIE_PERSONNEL)
                || idRole.equals(IntRole.ROLE_ADMINISTRATEUR)) {
            if (sIdExterneRole.length() < lenIdExterneRole) {
                sIdExterneRole = JadeStringUtil.fillWithZeroes(sIdExterneRole, lenIdExterneRole);
            }
        }

        sIdExterneRole = "00" + sIdExterneRole + "00";
        idExterneRole = role.formatIdExterneRole(idRole, sIdExterneRole);
        String idExterneRoleLike = idExterneRole.substring(0, idExterneRole.lastIndexOf("-"));
        return idExterneRoleLike;
    }

    /**
     * @param idExterneRoleLike
     */
    private AFAffiliation findAffiliation(String idExterneRoleLike) {
        AFAffiliationManager afManager = new AFAffiliationManager();
        afManager.setISession(getISession());
        afManager.setLikeAffilieNumero(idExterneRoleLike);
        afManager.setForActif(true);
        try {
            afManager.find();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!afManager.hasErrors() && afManager.size() == 1) {
            return (AFAffiliation) afManager.getFirstEntity();
        }

        return null;
    }

    /**
     * @param compteAnnexe
     * @param affiliation
     * @return
     */
    private CACompteAnnexe createCompteAnnexe(AFAffiliation affiliation, String idRole) {
        CACompteAnnexe compteAnnexe = null;
        try {
            APICompteAnnexe cptAnnexe = (APICompteAnnexe) getISession().getAPIFor(APICompteAnnexe.class);
            compteAnnexe = (CACompteAnnexe) cptAnnexe.createCompteAnnexe(getISession(), null, affiliation.getIdTiers(),
                    idRole, affiliation.getAffilieNumero());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compteAnnexe;
    }

    /**
     * 
     */
    private String findIdRole() {
        String idRole = "";
        // Si l'id du rôle est forcée
        if (valIdRole != 0) {
            idRole = String.valueOf(valIdRole);
        } else {
            // Sinon, on l'extrait du numéro de référence
            idRole = reference.substring(posIdRole - 1, posIdRole + lenIdRole - 1);
            idRole = IntRole.ROLE_AFFILIE.substring(0, IntRole.ROLE_AFFILIE.length() - 2) + idRole;
        }
        return idRole;
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

    @Override
    public boolean isPlanPaiement() {
        return planPaiement;
    }

    /**
     * Cette méthode permet de récupérer le Type de Plan de la ligne de référence
     */
    private void parseTypePlan() {
        planPaiement = reference.substring(posTypePlan - 1, posTypePlan + lenTypePlan - 1).equals(
                planPaiementIdentifier);
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

    private String parseIdCompteAnnexeForRefIdCompteAnnexe(String reference) {
        BigInteger idCompteAnnexe = new BigInteger(reference.substring(posIdExterneRole - 1, posIdExterneRole
                + lenIdExterneRole - 1));
        return idCompteAnnexe.toString();
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
