package ch.globaz.eform.business.search;

import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import globaz.jade.persistence.model.JadeSearchSimpleModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GFAdministrationSearch extends JadeSearchSimpleModel {
    private String forGenreAdministration;
    private String forCodeAdministrationLike;
    private String forDesignation1Like;
    private String forSedexIdLike;
    private String notNull = "false";

    public GFAdministrationSearch() {
        super(JadeSearchSimpleModel.SIZE_NOLIMIT);
    }

    @Override
    public Class whichModelClass() {
        return AdministrationComplexModel.class;
    }
}
