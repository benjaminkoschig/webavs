/*
 * Créé le 22 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.helpers.statOfas;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.naos.db.statOfas.AFStatistiquesOfasViewBean;
import globaz.naos.process.statOfas.AFStatistiquesOfasProcess;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFStatistiquesOfasHelper extends FWHelper {
    public final static String TYPE_ADRESSE_COURRIER = "508001";

    public final static String TYPE_ADRESSE_DOMICILE = "508008";

    public static boolean isNumericIntegerPositifFixedSize(String theNumeric, int size) {

        return JadeNumericUtil.isIntegerPositif(theNumeric) && (theNumeric.length() == size);

    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFAffiliationNAIndSansCIHelper.
     */
    public AFStatistiquesOfasHelper() {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            AFStatistiquesOfasViewBean asViewBean = (AFStatistiquesOfasViewBean) viewBean;
            if (!AFStatistiquesOfasHelper.isNumericIntegerPositifFixedSize(asViewBean.getForAnnee(), 4)) {
                throw new IllegalArgumentException(((BSession) session).getLabel("STATISTIQUE_OFAS_YEAR_REQUIRED"));
            }
            AFStatistiquesOfasProcess process = new AFStatistiquesOfasProcess();

            process.setSession((BSession) session);
            process.setAnnee(asViewBean.getForAnnee());
            process.setEMailAddress(asViewBean.getEmail());
            process.setIdTypeAdresse(asViewBean.getIdTypeAdresse());
            process.start();

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
