package globaz.aries.vb.decisioncgas;

import globaz.aries.vb.ARAbstractDefaultViewBean;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import ch.globaz.naos.business.constantes.AFAffiliationType;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;

public class ARDecisionCgasSearchViewBean extends ARAbstractDefaultViewBean {

    private String forNumAffilie;
    private String idAffilie;
    private String nomAffilie;
    private String typesAffForWidgetString;

    public ARDecisionCgasSearchViewBean() {
        // ATTENTION, les types doivent être séparés par un "_"
        setTypesAffForWidgetString(new String(CodeSystem.TYPE_AFFILI_CGAS_EMPLOYEUR + "_"
                + CodeSystem.TYPE_AFFILI_CGAS_INDEPENDANT));
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    @Override
    public String getId() {
        return null;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getNomAffilie() {
        return nomAffilie;
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public String getTypesAffForWidgetString() {
        return typesAffForWidgetString;
    }

    @Override
    public void retrieve() throws Exception {
        // si un affilié est séléctionné en session on le charge
        if (!JadeStringUtil.isBlankOrZero(idAffilie)) {
            AffiliationSimpleModel affiliation = AFBusinessServiceLocator.getAffiliationService().read(idAffilie);
            idAffilie = "";
            if (AFAffiliationType.isTypeCGAS(affiliation.getTypeAffiliation())) {
                idAffilie = affiliation.getAffiliationId();
                forNumAffilie = affiliation.getAffilieNumero();
                nomAffilie = affiliation.getRaisonSocialeCourt();
            }
        }
    }

    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

    @Override
    public void setId(String newId) {
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setNomAffilie(String nomAffilie) {
        this.nomAffilie = nomAffilie;
    }

    public void setTypesAffForWidgetString(String typesAffForWidgetString) {
        this.typesAffForWidgetString = typesAffForWidgetString;
    }

}
