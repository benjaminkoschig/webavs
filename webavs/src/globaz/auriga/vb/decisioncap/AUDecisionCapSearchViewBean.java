package globaz.auriga.vb.decisioncap;

import globaz.auriga.vb.AUAbstractDefaultViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import ch.globaz.naos.business.constantes.AFAffiliationType;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;

public class AUDecisionCapSearchViewBean extends AUAbstractDefaultViewBean {

    private String forNumAffilie;
    private String idAffilie;
    private String listTypeAffForWidgetString;
    private String nomAffilie;

    public AUDecisionCapSearchViewBean() {
        // ATTENTION, les types doivent être séparés par un "_"
        setListTypeAffForWidgetString(new String(CodeSystem.TYPE_AFFILI_CAP_EMPLOYEUR + "_"
                + CodeSystem.TYPE_AFFILI_CAP_INDEPENDANT));
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getListTypeAffForWidgetString() {
        return listTypeAffForWidgetString;
    }

    public String getNomAffilie() {
        return nomAffilie;
    }

    @Override
    public void retrieve() throws Exception {
        // si un affilié est séléctionné en session on le charge
        if (!JadeStringUtil.isBlankOrZero(idAffilie)) {
            AffiliationSimpleModel affiliation = AFBusinessServiceLocator.getAffiliationService().read(idAffilie);
            idAffilie = "";
            if (AFAffiliationType.isTypeCAP(affiliation.getTypeAffiliation())) {
                idAffilie = affiliation.getAffiliationId();
                forNumAffilie = affiliation.getAffilieNumero();
                nomAffilie = affiliation.getRaisonSocialeCourt();
            }

        }
    }

    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setListTypeAffForWidgetString(String listTypeAffForWidgetString) {
        this.listTypeAffForWidgetString = listTypeAffForWidgetString;
    }

    public void setNomAffilie(String nomAffilie) {
        this.nomAffilie = nomAffilie;
    }

}
