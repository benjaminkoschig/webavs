package globaz.ccvd.osiris;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.ordres.CARefAncienBVRParser;
import globaz.osiris.db.ordres.CARefBVRParser;
import globaz.osiris.parser.IntReferenceBVRParser;
import globaz.pyxis.db.tiers.TIRole;

public class CARefCCVDBVRParser extends CARefAncienBVRParser {
    IntReferenceBVRParser standardParser = new CARefBVRParser();

    /**
     * Default constructor
     */
    public CARefCCVDBVRParser() {
        super();
    }

    /**
     * @see globaz.osiris.db.ordres.CARefAncienBVRParser#setReference(java.lang.String, globaz.globall.db.BSession,
     *      java.lang.String)
     */
    @Override
    public void setReference(String numeroReference, BSession session, String numInterneLsv) throws Exception {
        // Appel de la méthode de la super classe
        super.setReference(numeroReference, session, numInterneLsv);
        // Si l'idexterne n'a pas été trouvé
        if (JadeStringUtil.isIntegerEmpty(getIdExterneRole()) && !JadeStringUtil.isEmpty(numeroReference)) {
            // Tentative avec le NE position 9-22 du no de réf. BVR
            String NE = numeroReference.substring(8, 11) + "/" + numeroReference.substring(11, 22);
            String section = numeroReference.substring(22, 26);
            AFAffiliationManager manager = new AFAffiliationManager();
            manager.setSession(session);
            manager.setForAncienNumero(NE);
            manager.find();
            AFAffiliation affilie = (AFAffiliation) manager.getFirstEntity();
            if (affilie != null) {
                setIdExterneRole(affilie.getAffilieNumero());
                setIdRole(TIRole.CS_AFFILIE);
                setIdExterneSection(section);
                setIdTypeSection(APISection.ID_TYPE_SECTION_ANCIENNE_FACTURE);
                // Charger compte annexe (section n'existe pas)
                CACompteAnnexeManager caManager = new CACompteAnnexeManager();
                caManager.setSession(session);
                caManager.setForIdRole(getIdRole());
                caManager.setForIdExterneRole(getIdExterneRole());
                caManager.find();
                CACompteAnnexe ca = (CACompteAnnexe) caManager.getFirstEntity();
                if (ca != null) {
                    setIdCompteAnnexe(ca.getIdCompteAnnexe());
                }
            }
        }
        // Dernière tentative avec parser standard si rien trouvé
        if (JadeStringUtil.isIntegerEmpty(getIdCompteAnnexe()) && !JadeStringUtil.isEmpty(numeroReference)
                && numeroReference.startsWith("02")) {
            try {
                // On parse en standard
                standardParser.setISession(session);
                standardParser.setReference(numeroReference, session, numInterneLsv);
                // Compte trouvé
                if (!JadeStringUtil.isIntegerEmpty(standardParser.getIdCompteAnnexe())) {
                    setIdCompteAnnexe(standardParser.getIdCompteAnnexe());
                    setIdSection(standardParser.getIdSection());
                }
            } catch (Exception e) {
            }
        }
    }
}
