package globaz.osiris.db.ordres;

import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.parser.IntReferenceBVRParser;
import java.math.BigDecimal;

/**
 * Date de création : (08.02.2007)
 * 
 * @author sel
 */
public class CARefAncienBVRParser implements IntReferenceBVRParser {
    private String idCompteAnnexe = "";

    private String idExterneRole = "";
    private String idExterneSection = "";
    private String idRole = "";
    private String idSection = "";
    private String idTypeSection = "";
    private String reference = "";
    private BISession session;

    /**
     * Constructor. Retrouve les index de la référence du BVR depuis OSIRIS.properties.
     */
    public CARefAncienBVRParser() {
        super();
    }

    /**
     * @see globaz.osiris.parser.IntReferenceBVRParser#getIdCompteAnnexe()
     */
    @Override
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return
     */
    public String getIdExterneRole() {
        return idExterneRole;
    }

    /**
     * @return
     */
    @Override
    public String getIdExterneSection() {
        return idExterneSection;
    }

    @Override
    public String getIdPlanPaiement() {
        return null;
    }

    /**
     * @return
     */
    public String getIdRole() {
        return idRole;
    }

    /**
     * @see globaz.osiris.parser.IntReferenceBVRParser#getIdSection()
     */
    @Override
    public String getIdSection() {
        return idSection;
    }

    /**
     * @return
     */
    @Override
    public String getIdTypeSection() {
        return idTypeSection;
    }

    /**
     * @see globaz.osiris.parser.IntReferenceBVRParser#getISession()
     */
    @Override
    public BISession getISession() {
        return session;
    }

    /**
     * Initialise les variables membres de la classe.
     */
    private void initMembres() {
        // Initialiser
        String ZERO = "0";
        idCompteAnnexe = ZERO;
        idSection = ZERO;
        reference = ZERO;
        idRole = ZERO;
        idExterneRole = ZERO;
        idExterneSection = ZERO;
        idTypeSection = ZERO;
    }

    /**
     * @return
     */
    @Override
    public boolean isPlanPaiement() {
        return false;
    }

    protected void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    protected void setIdExterneRole(String idExterneRole) {
        this.idExterneRole = idExterneRole;
    }

    protected void setIdExterneSection(String idExterneSection) {
        this.idExterneSection = idExterneSection;
    }

    protected void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    protected void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    protected void setIdTypeSection(String idTypeSection) {
        this.idTypeSection = idTypeSection;
    }

    /**
     * @see globaz.osiris.parser.IntReferenceBVRParser#setISession(globaz.globall.api.BISession)
     */
    @Override
    public void setISession(BISession newSession) throws Exception {
        session = newSession;
    }

    /**
     * @see globaz.osiris.parser.IntReferenceBVRParser#setReference(java.lang.String, globaz.globall.db.BSession)
     */
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
        }
    }

    @Override
    public boolean isModeCreditBulletinNeutre() {
        return false;
    }
}
