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
    private String likeNss = null;
    private String byCaisse = null;
    private String byIdTierAdministration = null;
    private String byType = null;
    private String byStatus = null;
    private String byGestionnaire = null;
    private String byYourBusinessRefId = null;
    private String byOurBusinessRefId = null;

    public GFDaDossierSearch() {
        super(JadeSearchSimpleModel.SIZE_NOLIMIT);
    }

    @Override
    public Class whichModelClass() {
        return GFDaDossierModel.class;
    }
}
