package globaz.pegasus.vb.rpc;

import globaz.corvus.utils.RETiersForJspUtils;
import globaz.globall.db.BSession;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.vb.JadeAbstractAjaxFindRawSQLForDomainCommon;
import ch.globaz.pegasus.business.PegasusRepositoryLocator;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce.AnnonceConverter;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce.AnnonceSearch;
import ch.globaz.pegasus.rpc.domaine.CodeTraitement;
import ch.globaz.pegasus.rpc.domaine.EtatAnnonce;
import ch.globaz.pegasus.rpc.domaine.PersonneAnnonceRpc;
import ch.globaz.pegasus.rpc.domaine.RetourAnnonce;
import ch.globaz.pegasus.rpc.domaine.RetourAnnonceRpc;

public class PCDetailAnnonceAjaxViewBean extends JadeAbstractAjaxFindRawSQLForDomainCommon<RetourAnnonce> {
    private static final long serialVersionUID = -1799886190688499581L;

    private RetourAnnonce retourAnnonce = new RetourAnnonce();
    private PersonneAnnonceRpc personneAnnonce = null;
    private String annonceId;
    private Integer counterId = 0;
    private Boolean updateCodeTraitement = false;

    private Boolean updateRemarque = false;
    private Boolean updateItemStatus = false;
    private Boolean changeToTraite = false;
    private String remarque;
    private String selectedItemId;

    public Boolean getUpdateCodeTraitement() {
        return updateCodeTraitement;
    }

    public void setUpdateCodeTraitement(Boolean updateCodeTraitement) {
        this.updateCodeTraitement = updateCodeTraitement;
    }

    public Boolean getUpdateItemStatus() {
        return updateItemStatus;
    }

    public void setUpdateItemStatus(Boolean updateItemStatus) {
        this.updateItemStatus = updateItemStatus;
    }

    public Boolean getChangeToTraite() {
        return changeToTraite;
    }

    public void setChangeToTraite(Boolean changeToTraite) {
        this.changeToTraite = changeToTraite;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public String getSelectedItemId() {
        return selectedItemId;
    }

    public void setSelectedItemId(String selectedItemId) {
        this.selectedItemId = selectedItemId;
    }

    public void setAnnonceId(String idAnnonce) {
        if (idAnnonce != null) {
            annonceId = idAnnonce;
            if (personneAnnonce == null) {
                personneAnnonce = LoadPersonneAnnonce(idAnnonce);
            }
        }
    }

    private PersonneAnnonceRpc LoadPersonneAnnonce(String idAnnonce) {
        AnnonceSearch annonceSearch = new AnnonceSearch();
        annonceSearch.setAnnonceId(idAnnonce);
        List<PersonneAnnonceRpc> listAnnoncePersonne = PegasusRepositoryLocator.getAnnonceRepository()
                .findPersonneAnnonce(annonceSearch);
        if (!listAnnoncePersonne.isEmpty()) {
            return listAnnoncePersonne.get(0);
        }
        return null;
    }

    public String getRequerant() {
        return RETiersForJspUtils.getInstance((BSession) getISession()).getDetailsTiers(personneAnnonce.getPersonne(),
                true, true);
    }

    public String getEtatAnnonce() {
        if (personneAnnonce.getAnnonceRpc() != null && personneAnnonce.getAnnonceRpc().getEtat() != null) {
            return AnnonceConverter.translate(personneAnnonce.getAnnonceRpc().getEtat(), (BSession) getISession());
        }
        return "";

    }

    public void setEtatAnnonceRpc(EtatAnnonce etat) {
        personneAnnonce.getAnnonceRpc().setEtat(etat);
    }

    public void setCodeTraitementAnnonceRpc(CodeTraitement codeTraitement) {
        personneAnnonce.getAnnonceRpc().setCodeTraitement(codeTraitement);
    }

    public CodeTraitement getCodeTraitementAnnonceRpc() {
        return personneAnnonce.getAnnonceRpc().getCodeTraitement();
    }

    public String getCodeTraitementValue() {
        return AnnonceConverter.toCsCode(personneAnnonce.getAnnonceRpc().getCodeTraitement());
    }

    public void setCodeTraitementValue(String codeTraitementValue) {
        personneAnnonce.getAnnonceRpc()
                .setCodeTraitement((CodeTraitement) AnnonceConverter.toEnum(codeTraitementValue));
    }

    public String getCodeTraitement() {
        return AnnonceConverter.translate(getCodeTraitementAnnonceRpc(), (BSession) getISession());
    }

    public List<RetourAnnonce> getRetoursAnnonce() {
        return getList();
    }

    public String getAnnonceId() {
        return annonceId;
    }

    public String getCounterId() {
        return counterId.toString();
    }

    @Override
    public RetourAnnonce getEntity() {
        if (retourAnnonce == null) {
            retourAnnonce = new RetourAnnonce();
        }
        return retourAnnonce;
    }

    @Override
    public List<RetourAnnonce> findBySQL() {
        AnnonceSearch annonceSearch = new AnnonceSearch();
        annonceSearch.setAnnonceId(annonceId);
        List<RetourAnnonce> retoursAnnonce = new ArrayList<RetourAnnonce>();
        List<RetourAnnonceRpc> retoursAnnonceRpc = PegasusRepositoryLocator.getRetourAnnonceRepository()
                .findRetoursAnnonce(annonceSearch);
        for (RetourAnnonceRpc bean : retoursAnnonceRpc) {

            retoursAnnonce.add(convertToRetourAnnonce(bean));
        }
        return retoursAnnonce;
    }

    private RetourAnnonce convertToRetourAnnonce(RetourAnnonceRpc bean) {
        RetourAnnonce item = new RetourAnnonce(bean.getCodePlausi(), bean.getCategorie(), bean.getType(),
                bean.getTypeViolationPlausi());
        item.setId(bean.getId());
        item.setCaseIdConflit(bean.getCaseIdConflit());
        item.setDecisionIdConflit(bean.getDecisionIdConflit());
        item.setIdLien(bean.getIdLien());
        item.setIdLienAnnonceDecision(bean.getIdLienAnnonceDecision());
        item.setNssAnnonce(bean.getNssAnnonce());
        item.setNssPersonne(bean.getNssPersonne());
        item.setOfficePC(bean.getOfficePC());
        item.setOfficePCConflit(bean.getOfficePCConflit());
        if (bean.getRemarque() != null && bean.getRemarque().contains("'")) {
            item.setRemarque(bean.getRemarque().replace("'", "&apos;"));
        } else {
            item.setRemarque(bean.getRemarque());
        }
        item.setStatus(bean.getStatus());
        item.setCategoriePlausi(bean.getCategorie());
        item.setValidFromConflit(bean.getValidFromConflit());
        item.setValidToConflit(bean.getValidToConflit());
        return item;
    }

    @Override
    public int nbOfResultMathingQuery() {
        return getSearchModel().getNbOfResultMatchingQuery();
    }

    public Boolean getUpdateRemarque() {
        return updateRemarque;
    }

    public void setUpdateRemarque(Boolean updateRemarque) {
        this.updateRemarque = updateRemarque;
    }
}