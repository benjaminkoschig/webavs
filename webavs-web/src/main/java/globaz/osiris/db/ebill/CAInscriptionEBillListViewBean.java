package globaz.osiris.db.ebill;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CAInscriptionEBillListViewBean extends CAInscriptionEBillManager implements FWListViewBeanInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(CAInscriptionEBillListViewBean.class);

    public String getNomPrenomOuEntrepriseInscription(CAInscriptionEBill _inscriptionEBill) {
        if (!JadeStringUtil.isEmpty(_inscriptionEBill.getEntreprise())) {
            return _inscriptionEBill.getEntreprise();
        } else {
            return _inscriptionEBill.getNom() + " " + _inscriptionEBill.getPrenom();
        }
    }

    public String getNomWebAVS(BSession bSession, CAInscriptionEBill _inscriptionEBill) {
        String nomWebAVS = "";
        String numAffilie = _inscriptionEBill.getChampNumeroAffilie();
        try {
            if (!JadeStringUtil.isBlankOrZero(numAffilie)) {
                AFAffiliationManager affManager = new AFAffiliationManager();
                affManager.setSession(bSession);
                affManager.setForAffilieNumero(numAffilie);
                affManager.find(BManager.SIZE_USEDEFAULT);
                if (affManager.size() > 0) {
                    AFAffiliation aff = (AFAffiliation) affManager.getFirstEntity();
                    String nomTiers = aff.getTiersNom();
                    if (nomTiers != null) {
                        nomWebAVS = nomTiers;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Une erreur est intervenue lors de la recherche du nom avec le numéro d'affilié : " + numAffilie, e);
        }

        return nomWebAVS;
    }
}
