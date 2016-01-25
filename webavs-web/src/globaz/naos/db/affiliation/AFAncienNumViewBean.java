package globaz.naos.db.affiliation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.servlet.AFActionAutreDossier;

/**
 * Utilise pour selectionner un autre numéro d'affilié.
 * 
 * @author: Administrator
 */
public class AFAncienNumViewBean extends AFAffiliation implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String ancienNumero = "";
    private java.lang.String codeDirection = "";

    /**
     * Contrôle le numéro d'affilié saisi et stocke le nombre d'affiliation du tiers trouvé (utilisé par la suite pour
     * le direction des écrans).
     * 
     * @param session
     *            La session actuelle
     * @throws Exception
     */
    public void _controle(BSession session) throws Exception {
        try {
            if (!JadeStringUtil.isEmpty(getAncienNumero())) {

                // Recherche du nombre d'affiliation pour ce tiers
                AFAffiliationManager affiManager = new AFAffiliationManager();
                affiManager.setSession(getSession());
                affiManager.setForAncienNumero(getAncienNumero());
                affiManager.find();

                if (affiManager.size() == 0) {
                    setMsgType(FWViewBeanInterface.ERROR);
                    setMessage(session.getLabel("1560"));
                } else {
                    if (affiManager.size() == 1) {
                        setIdTiers(((AFAffiliation) affiManager.getEntity(0)).getIdTiers());
                        setAffiliationId(((AFAffiliation) affiManager.getEntity(0)).getAffiliationId());

                        setCodeDirection(AFActionAutreDossier.CODE_ECRAN_DETAIL_AFFILIATION);
                    } else {
                        setIdTiers(((AFAffiliation) affiManager.getEntity(0)).getIdTiers());
                        setCodeDirection(AFActionAutreDossier.CODE_ECRAN_LIST_AFFILIATION);
                    }
                }
            } else {
                setMsgType(FWViewBeanInterface.ERROR);
                setMessage(session.getLabel("1570"));
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(e.getMessage());
        }
    }

    // ***********************************************
    // Getter
    // ***********************************************

    public java.lang.String getAncienNumero() {
        return ancienNumero;
    }

    public java.lang.String getCodeDirection() {
        return codeDirection;
    }

    // ***********************************************
    // Setter
    // ***********************************************

    public void setAncienNumero(java.lang.String newAncienNumero) {
        ancienNumero = newAncienNumero;
    }

    public void setCodeDirection(java.lang.String newCodeDirection) {
        codeDirection = newCodeDirection;
    }
}
