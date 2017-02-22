/*
 * Créé le 15.09.2006
 */
package globaz.apg.helpers.process;

import globaz.apg.vb.process.APListeTaxationsDefinitivesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.phenix.itext.taxation.definitive.CPListeTaxationDefinitiveXlsPdf;
import globaz.phenix.itext.taxation.definitive.ListTaxationsDefinitivesCriteria;

/**
 * 
 * @author mmo
 */
public class APListeTaxationsDefinitivesHelper extends FWHelper {

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // NOTHING TO DO
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        APListeTaxationsDefinitivesViewBean vb = (APListeTaxationsDefinitivesViewBean) viewBean;

        ListTaxationsDefinitivesCriteria criteria = new ListTaxationsDefinitivesCriteria();
        criteria.setAnneeDroit(vb.getAnneeDroit());
        criteria.setAnneeTaxationCP(vb.getAnneeTaxationCP());
        criteria.setDateDebutDecisionsCP(vb.getDateDebutDecisionsCP());
        criteria.setDateDebutDecompte(vb.getDateDebutDecompte());
        criteria.setDateFinDecisionsCP(vb.getDateFinDecisionsCP());
        criteria.setDateFinDecompte(vb.getDateFinDecompte());
        criteria.setEndWithNoAffilie(vb.getEndWithNoAffilie());
        criteria.setInclureAffilieRadie(vb.getInclureAffilieRadie());
        criteria.setStartWithNoAffilie(vb.getStartWithNoAffilie());
        criteria.setNoPassage(null);

        CPListeTaxationDefinitiveXlsPdf process = new CPListeTaxationDefinitiveXlsPdf();
        process.setSession((BSession) session);
        process.setCriteria(criteria);
        process.setEMailAddress(vb.geteMailAddress());
        process.start();
    }
}
