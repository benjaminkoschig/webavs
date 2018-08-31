package globaz.vulpecula.vb.registre;

import globaz.globall.db.BSpy;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.vulpecula.domain.models.registre.CategorieFactureAssociationProfessionnelle;

public class PTCotisationsAPViewBean extends BJadeSearchObjectELViewBean {
    public static class SearchModel {
        public String getForLibelleUpperLike() {
            return forLibelleUpperLike;
        }

        public void setForLibelleUpperLike(String forLibelleUpperLike) {
            this.forLibelleUpperLike = forLibelleUpperLike;
        }

        public String getForCodeLike() {
            return forCodeLike;
        }

        public void setForCodeLike(String forCodeLike) {
            this.forCodeLike = forCodeLike;
        }

        public String getForAssociationLike() {
            return forAssociationLike;
        }

        public void setForAssociationLike(String forAssociationLike) {
            this.forAssociationLike = forAssociationLike;
        }

        public String getForFacturerDefaut() {
            return forFacturerDefaut;
        }

        public void setForFacturerDefaut(String forCategorieFactureAPDefaut) {
            forFacturerDefaut = forCategorieFactureAPDefaut;
        }

        private String forLibelleUpperLike;
        private String forCodeLike;
        private String forAssociationLike;
        private String forFacturerDefaut = CategorieFactureAssociationProfessionnelle.A_FACTURER.getValue();
    }

    private SearchModel searchModel = new SearchModel();

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(String arg0) {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public SearchModel getSearchModel() {
        return searchModel;
    }

    public void setSearchModel(SearchModel searchModel) {
        this.searchModel = searchModel;
    }
}
