package ch.globaz.eform.business.search;

import ch.globaz.eform.business.models.GFDaDossierModel;
import globaz.jade.persistence.model.JadeSearchSimpleModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GFDaDossierSearch extends JadeSearchSimpleModel {
    private String byId = null;
    private String byMessageId = null;
    private String byNss = null;
    private String byCaisse;
    private String byType;
    private String byStatus;
    private String byGestionnaire;

    public GFDaDossierSearch() {
        super(JadeSearchSimpleModel.SIZE_NOLIMIT);
    }

    @Override
    public Class whichModelClass() {
        return GFDaDossierModel.class;
    }
}
