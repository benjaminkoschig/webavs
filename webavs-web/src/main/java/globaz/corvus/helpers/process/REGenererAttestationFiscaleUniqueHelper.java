package globaz.corvus.helpers.process;

import globaz.corvus.topaz.REAttestationFiscaleUniqueOO;
import globaz.corvus.vb.process.REGenererAttestationFiscaleUniqueViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import java.util.List;

public class REGenererAttestationFiscaleUniqueHelper extends PRAbstractHelper {

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            REGenererAttestationFiscaleUniqueViewBean glabViewBean = (REGenererAttestationFiscaleUniqueViewBean) viewBean;

            List<String> listeAttAssure = JadeStringUtil.tokenize(glabViewBean.getAttTable_assure(), "¢");
            List<String> listeAttPeriode = JadeStringUtil.tokenize(glabViewBean.getAttTable_periode(), "¢");
            List<String> listeAttMontant = JadeStringUtil.tokenize(glabViewBean.getAttTable_montant(), "¢");
            List<String> listeAssure = JadeStringUtil.tokenize(glabViewBean.getTable_assure(), "¢");
            List<String> listePeriode = JadeStringUtil.tokenize(glabViewBean.getTable_periode(), "¢");
            List<String> listeMontant = JadeStringUtil.tokenize(glabViewBean.getTable_montant(), "¢");

            REAttestationFiscaleUniqueOO process = new REAttestationFiscaleUniqueOO();

            process.setListeattAssure(listeAttAssure);
            process.setListeattPeriode(listeAttPeriode);
            process.setListeattMontant(listeAttMontant);
            process.setListeAssure(listeAssure);
            process.setListePeriode(listePeriode);
            process.setListeMontant(listeMontant);

            process.setAnneeAttestations(glabViewBean.getAnneeAttestations());
            process.setNSS(glabViewBean.getNSS());
            process.setDateImpressionAttJJMMAAA(glabViewBean.getDateImpressionAttJJMMAAA());
            process.setAdresseEmail(glabViewBean.getEMailAddress());
            process.setIsSendToGed(glabViewBean.getIsSendToGed().booleanValue());
            process.setIdTiers(glabViewBean.getIdTiers());
            process.setIdTiersBaseCalcul(glabViewBean.getIdTiersBaseCalcul());
            process.setCodeIsoLangue(glabViewBean.getCodeIsoLangue());
            process.setSession((BSession) session);
            process.setTraiterPar(glabViewBean.getTraiterPar());
            process.setContact(glabViewBean.getContact());
            process.setTelephone(glabViewBean.getTelephone());

            process.setAdresse(glabViewBean.getAdresse());
            process.setConcerne(glabViewBean.getConcerne());
            process.setSousConcerne(glabViewBean.getSousConcerne());
            process.setTitre(glabViewBean.getTitre());
            process.setPara1(glabViewBean.getPara1());
            process.setAssure(glabViewBean.getAssure());
            process.setPeriode(glabViewBean.getPeriode());
            process.setMontant(glabViewBean.getMontant());
            process.setTotal(glabViewBean.getTotal());
            process.setChf(glabViewBean.getChf());
            process.setTitreAPI(glabViewBean.getTitreAPI());
            process.setParaAPI(glabViewBean.getParaAPI());
            process.setSalutation(glabViewBean.getSalutation());
            process.setSiganture(glabViewBean.getSignature());

            BProcessLauncher.start(process, false);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}