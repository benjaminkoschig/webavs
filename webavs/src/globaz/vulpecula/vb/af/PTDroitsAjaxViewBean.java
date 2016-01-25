package globaz.vulpecula.vb.af;

import globaz.framework.bean.JadeAbstractAjaxFindViewBean;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.List;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.services.PTAFServices;
import com.google.common.base.Preconditions;

public class PTDroitsAjaxViewBean extends JadeAbstractAjaxFindViewBean {
    private static final long serialVersionUID = 7512364152349448879L;

    private List<DroitComplexModel> droits;

    private String idTiers;

    @Override
    public void find() throws Exception {
        Preconditions.checkArgument(!JadeNumericUtil.isEmptyOrZero(idTiers));

        droits = PTAFServices.getDroitsOrdresForIdTiers(idTiers);
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return new DroitComplexModel();
    }

    @SuppressWarnings("serial")
    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return new JadeAbstractSearchModel() {
            @SuppressWarnings("rawtypes")
            @Override
            public Class whichModelClass() {
                return null;
            }
        };
    }

    public List<DroitComplexModel> getList() {
        return droits;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    // WARNING : Websphere s'attend à une signature recevant un object en paramètre
    // A ne pas supprimer !
    public boolean isActif(Object droit) {
        return isActif((DroitComplexModel) droit);
    }

    public boolean isActif(DroitComplexModel droit) {
        Periode periode = new Periode(droit.getDroitModel().getDebutDroit(), droit.getDroitModel().getFinDroitForcee());
        return periode.isActif();
    }

    @Override
    public void initList() {
    }

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    @Override
    public void retrieve() throws Exception {
        Preconditions.checkArgument(!JadeNumericUtil.isEmptyOrZero(idTiers));

        droits = PTAFServices.getDroitsOrdresForIdTiers(idTiers);
    }

    @Override
    public void update() throws Exception {
    }

}
