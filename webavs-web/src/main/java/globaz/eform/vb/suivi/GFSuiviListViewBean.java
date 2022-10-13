package globaz.eform.vb.suivi;

import ch.globaz.common.util.NSSUtils;
import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.eform.business.search.GFDaDossierSearch;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import globaz.eform.translation.CodeSystem;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import org.apache.commons.lang3.StringUtils;

public class GFSuiviListViewBean  extends BJadePersistentObjectListViewBean {

    private GFDaDossierSearch suiviSearch;

    public GFSuiviListViewBean() {
        super();
        suiviSearch = new GFDaDossierSearch();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return suiviSearch;
    }

    @Override
    public void find() throws Exception {
        suiviSearch.setWhereKey("suivi");
        suiviSearch = GFEFormServiceLocator.getGFDaDossierDBService().search(suiviSearch);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < suiviSearch.getSize() ? new GFSuiviViewBean(
                (GFDaDossierModel) suiviSearch.getSearchResults()[idx]) : new GFSuiviViewBean();
    }

    public String getLikeNss() {
        if (StringUtils.isEmpty(suiviSearch.getLikeNss()) || !NSSUtils.checkNSS(suiviSearch.getLikeNss())) {
            return suiviSearch.getLikeNss();
        } else {
            return NSSUtils.formatNss(suiviSearch.getLikeNss());
        }
    }

    public void setLikeNss(String likeNss) {
        if (StringUtils.isBlank(likeNss)) {
            suiviSearch.setLikeNss(null);
        } else {
            suiviSearch.setLikeNss(NSSUtils.unFormatNss(likeNss));
        }
    }

    public String getByCaisse() {
        try {
            if (!StringUtils.isBlank(suiviSearch.getByCaisse())) {
                AdministrationSearchComplexModel search = new AdministrationSearchComplexModel();
                search.setForCodeAdministration(suiviSearch.getByCaisse());
                search.setForGenreAdministration(CodeSystem.GENRE_ADMIN_CAISSE_COMP);

                search = TIBusinessServiceLocator.getAdministrationService().find(search);

                if (search.getSearchResults().length == 1) {
                    AdministrationComplexModel complexModel = (AdministrationComplexModel) search.getSearchResults()[0];
                    return complexModel.getAdmin().getCodeAdministration() + " - " +
                            complexModel.getTiers().getDesignation1() + " " +
                            complexModel.getTiers().getDesignation2();
                }
            }

            return "";
        } catch (JadePersistenceException | JadeApplicationException e) {
            throw new RuntimeException(e);
        }
    }

    public void setByCaisse(String byCaisse) {
        if (StringUtils.isBlank(byCaisse)) {
            suiviSearch.setByCaisse(byCaisse);
        } else {
            suiviSearch.setByCaisse(byCaisse.split(" - ")[0]);
        }
    }

    public String getByIdTierAdministration() {
        return suiviSearch.getByIdTierAdministration();
    }

    public void setByIdTierAdministration(String idTierAdministration) {
        suiviSearch.setByIdTierAdministration(idTierAdministration);
    }

    public String getByType() {
        return suiviSearch.getByType();
    }

    public void setByType(String byType) {
        suiviSearch.setByType(byType);
    }

    public String getByStatus() {
        return suiviSearch.getByStatus();
    }

    public void setByStatus(String byStatus) {
        suiviSearch.setByStatus(byStatus);
    }

    public String getByGestionnaire() {
        return suiviSearch.getByGestionnaire();
    }

    public void setByGestionnaire(String byGestionnaire) {
        suiviSearch.setByGestionnaire(byGestionnaire);
    }
}
