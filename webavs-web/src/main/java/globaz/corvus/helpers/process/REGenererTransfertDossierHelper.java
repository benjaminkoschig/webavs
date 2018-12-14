package globaz.corvus.helpers.process;

import java.util.List;
import globaz.corvus.process.REGenererARC3XMLTransfertCIProcess;
import globaz.corvus.process.REGenererDemandeCalculProvisoireProcess;
import globaz.corvus.process.REGenererTransfertDossierNonValideProcess;
import globaz.corvus.process.REGenererTransfertDossierValideProcess;
import globaz.corvus.properties.REProperties;
import globaz.corvus.vb.process.REGenererTransfertDossierViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;

public class REGenererTransfertDossierHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     *
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
    }

    /**
     * (non javadoc)
     *
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REGenererTransfertDossierViewBean gldaViewBean = (REGenererTransfertDossierViewBean) viewBean;

        try {

            if (REProperties.TRANSFERT_ACTIVER_ANNONCES_XML.getBooleanValue() && hasNss(gldaViewBean.getListNss())) {

                REGenererARC3XMLTransfertCIProcess processARC = new REGenererARC3XMLTransfertCIProcess();
                processARC.setSession((BSession) session);
                processARC.setListNss(gldaViewBean.getListNss());
                processARC.setIdInfoCompl(gldaViewBean.getIdInfoCompl());
                processARC.setEMailAddress(gldaViewBean.getEMailAddress());
                processARC.setIdDemandeRente(gldaViewBean.getIdDemandeRente());
                BProcessLauncher.start(processARC, false);

            }

            if (gldaViewBean.isTransfertCaisseCompetente()) {
                REGenererTransfertDossierNonValideProcess process = new REGenererTransfertDossierNonValideProcess();
                process.setSession((BSession) session);
                process.setEMailAddress(gldaViewBean.getEMailAddress());
                process.setIdDemandeRente(gldaViewBean.getIdDemandeRente());
                process.setRemarque(gldaViewBean.getTexteRemarque());
                process.setIsSendToGed(gldaViewBean.getIsSendToGed());

                BProcessLauncher.start(process, false);

            } else if (gldaViewBean.isDemandeCalculPrevisionnel()) {
                REGenererDemandeCalculProvisoireProcess process = new REGenererDemandeCalculProvisoireProcess();
                process.setSession((BSession) session);

                process.setEMailAddress(gldaViewBean.getEMailAddress());
                process.setIdDemandeRente(gldaViewBean.getIdDemandeRente());
                process.setNomAssure(gldaViewBean.getNomAssure());
                process.setMotif(gldaViewBean.getMotif());
                process.setIsSendToGed(gldaViewBean.getIsSendToGed());

                BProcessLauncher.start(process, false);

            } else if (gldaViewBean.isTransfertCaisseCompetenteAndValidate()) {

                // Transmettre l'idDemandeRente car grace à elle, je peux
                // récuperer le num de la caisse et de l'agence
                // Dans le process, il faut que grace à l'idDemandeRente je
                // retrive un objet PRInfoCompl et grace à cet objet, je peux
                // avoir le num caisse et agence de la nouvelle caisse.
                REGenererTransfertDossierValideProcess process = new REGenererTransfertDossierValideProcess();
                process.setSession((BSession) session);
                process.setEMailAddress(gldaViewBean.getEMailAddress());
                process.setIdDemandeRente(gldaViewBean.getIdDemandeRente());
                process.setMotifTransmission(gldaViewBean.getMotifTransmission());
                process.setRemarque(gldaViewBean.getRemarqueTraEncous());
                process.setCessationPaiement(gldaViewBean.getMoisCessationPaiement());
                process.setDateEnvoi(gldaViewBean.getDateEnvoi());
                process.setIsSendToGed(gldaViewBean.getIsSendToGed());

                BProcessLauncher.start(process, false);

            }
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }

    }
    
    private boolean hasNss(List<String> list) {
        for(String nss: list) {
            if(!JadeStringUtil.isEmpty(nss)) {
                return true;
            }
        }
        return false;
    }
}
