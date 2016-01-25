package globaz.vulpecula.vb.postetravail;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.util.List;
import java.util.Vector;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.registre.Convention;

public class PTEmployeurViewBean extends BJadePersistentObjectViewBean {
    public class SearchModel {
        private String likeNumeroAffilie;
        private String likeRaisonSocialeUpper;
        private String forIdConvention;

        public final String getLikeNumeroAffilie() {
            return likeNumeroAffilie;
        }

        public final void setLikeNumeroAffilie(String likeNumeroAffilie) {
            this.likeNumeroAffilie = likeNumeroAffilie;
        }

        public final String getLikeRaisonSocialeUpper() {
            return likeRaisonSocialeUpper;
        }

        public final void setLikeRaisonSocialeUpper(String likeRaisonSocialeUpper) {
            this.likeRaisonSocialeUpper = likeRaisonSocialeUpper;
        }

        public final String getForIdConvention() {
            return forIdConvention;
        }

        public final void setForIdConvention(String forIdConvention) {
            this.forIdConvention = forIdConvention;
        }
    }

    private SearchModel searchModel = new SearchModel();

    public String getProtege() {
        return protege;
    }

    public String getProtegeLibelle() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("PAS_DROIT_EMPLOYEUR");
    }

    public void setProtege(String protege) {
        this.protege = protege;
    }

    private String idAssociationProfessionnelle = null;
    private String idAssocProfSection = null;
    private String protege = null;

    private List<Convention> conventions;

    public PTEmployeurViewBean() {
        super();
        idAssociationProfessionnelle = "";
        idAssocProfSection = "";
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

    public String getIdAssociationProfessionnelle() {
        return idAssociationProfessionnelle;
    }

    public String getIdAssocProfSection() {
        return idAssocProfSection;
    }

    public Vector<String[]> getListeConvention() {
        Vector<String[]> listeConvention = null;
        try {
            listeConvention = new Vector<String[]>();
        } catch (Exception e) {
            e.printStackTrace();
            listeConvention = new Vector<String[]>();
        }
        return listeConvention;
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        conventions = VulpeculaRepositoryLocator.getConventionRepository().findAll();
    }

    @Override
    public void setId(final String newId) {
    }

    public void setIdAssociationProfessionnelle(final String idAssociationProfessionnelle) {
        this.idAssociationProfessionnelle = idAssociationProfessionnelle;
    }

    public void setIdAssocProfSection(final String idAssocProfSection) {
        this.idAssocProfSection = idAssocProfSection;
    }

    @Override
    public void update() throws Exception {
        throw new UnsupportedOperationException();
    }

    public List<Convention> getConventions() {
        return conventions;
    }

    public final SearchModel getSearchModel() {
        return searchModel;
    }
}
