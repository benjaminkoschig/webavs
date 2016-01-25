package globaz.vulpecula.vb.postetravail;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;

public class PTTravailleurViewBean extends BJadePersistentObjectViewBean {
    public class SearchModel {
        private String forIdTravailleur;
        private String likeNom;
        private String likePrenom;
        private String forNumAvs;
        private String forDateNaissance;

        public final String getForIdTravailleur() {
            return forIdTravailleur;
        }

        public final void setForIdTravailleur(String forIdTravailleur) {
            this.forIdTravailleur = forIdTravailleur;
        }

        public final String getLikeNom() {
            return likeNom;
        }

        public final void setLikeNom(String likeNom) {
            this.likeNom = likeNom;
        }

        public final String getLikePrenom() {
            return likePrenom;
        }

        public final void setLikePrenom(String likePrenom) {
            this.likePrenom = likePrenom;
        }

        public final String getForNumAvs() {
            return forNumAvs;
        }

        public final void setForNumAvs(String forNumAvs) {
            this.forNumAvs = forNumAvs;
        }

        public final String getForDateNaissance() {
            return forDateNaissance;
        }

        public final void setForDateNaissance(String forDateNaissance) {
            this.forDateNaissance = forDateNaissance;
        }
    }

    String protege = null;
    private SearchModel searchModel = new SearchModel();

    public String getProtege() {
        return protege;
    }

    public String getProtegeLibelle() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("PAS_DROIT_TRAVAILLEUR");
    }

    public void setProtege(String protege) {
        this.protege = protege;
    }

    public PTTravailleurViewBean() {
        super();
    }

    @Override
    public void add() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(String newId) {
    }

    @Override
    public void update() throws Exception {
        throw new UnsupportedOperationException();
    }

    public SearchModel getSearchModel() {
        return searchModel;
    }
}
