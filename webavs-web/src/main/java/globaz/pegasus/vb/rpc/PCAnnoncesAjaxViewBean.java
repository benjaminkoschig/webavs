package globaz.pegasus.vb.rpc;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.vb.JadeAbstractAjaxFindRawSQLForDomainCommon;
import ch.globaz.pegasus.business.PegasusRepositoryLocator;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce.AnnonceConverter;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce.AnnonceSearch;
import ch.globaz.pegasus.rpc.domaine.CodeTraitement;
import ch.globaz.pegasus.rpc.domaine.EtatAnnonce;
import ch.globaz.pegasus.rpc.domaine.PersonneAnnonceRpc;

public class PCAnnoncesAjaxViewBean extends JadeAbstractAjaxFindRawSQLForDomainCommon<PCPersonneAnnonceBean> {

    private PCPersonneAnnonceBean personneAnnonceRpc = new PCPersonneAnnonceBean();
    private static final long serialVersionUID = -2421832289586898272L;
    private String nss;
    private String nom;
    private String prenom;
    private String etat;
    private String codeTraitement;
    private String periodeDateDebut;
    private String periodeDateFin;
    private String order;
    private boolean rechercheFamille;

    public List<PCPersonneAnnonceBean> getAnnonces() {
        return getList();
    }

    @Override
    public List<PCPersonneAnnonceBean> findBySQL() {
        AnnonceSearch annonceSearch = convertAnnonceSearch();
        List<PersonneAnnonceRpc> annonces = PegasusRepositoryLocator.getAnnonceRepository().findPersonneAnnonce(
                annonceSearch);
        List<PCPersonneAnnonceBean> annonceBeans = new ArrayList<PCPersonneAnnonceBean>();
        for (PersonneAnnonceRpc annonce : annonces) {
            annonceBeans.add(new PCPersonneAnnonceBean(annonce.getAnnonceRpc(), annonce.getPersonne(), getISession()));
        }
        return annonceBeans;
    }

    private AnnonceSearch convertAnnonceSearch() {
        AnnonceSearch annonceSearch = new AnnonceSearch();
        if (codeTraitement != null && !codeTraitement.isEmpty()) {
            CodeTraitement code = AnnonceConverter.toEnum(codeTraitement);
            annonceSearch.setCodeTraitement(code);
        }

        if (etat != null && !etat.isEmpty()) {
            EtatAnnonce etatEnum = AnnonceConverter.toEnum(etat);
            annonceSearch.setEtat(etatEnum);
        }

        annonceSearch.setOrder(order);
        annonceSearch.setNom(nom);
        annonceSearch.setNss(nss);
        annonceSearch.setPrenom(prenom);
        annonceSearch.setRechercheFamille(rechercheFamille);
        if (periodeDateDebut != null && !periodeDateDebut.isEmpty()) {
            annonceSearch.setPeriodeDateDebut(new Date(periodeDateDebut));
        }
        if (periodeDateFin != null && !periodeDateFin.isEmpty()) {
            annonceSearch.setPeriodeDateFin(new Date(periodeDateFin));
        }
        return annonceSearch;
    }

    @Override
    public int nbOfResultMathingQuery() {
        return getSearchModel().getNbOfResultMatchingQuery();
    }

    @Override
    public PCPersonneAnnonceBean getEntity() {
        if (personneAnnonceRpc == null) {
            personneAnnonceRpc = new PCPersonneAnnonceBean();
        }
        return personneAnnonceRpc;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public boolean getRechercheFamille() {
        return rechercheFamille;
    }

    public void setRechercheFamille(boolean rechercheFamille) {
        this.rechercheFamille = rechercheFamille;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getCodeTraitement() {
        return codeTraitement;
    }

    public void setCodeTraitement(String codeTraitement) {
        this.codeTraitement = codeTraitement;
    }

    public String getPeriodeDateDebut() {
        return periodeDateDebut;
    }

    public void setPeriodeDateDebut(String periodeDateDebut) {
        this.periodeDateDebut = periodeDateDebut;
    }

    public String getPeriodeDateFin() {
        return periodeDateFin;
    }

    public void setPeriodeDateFin(String periodeDateFin) {
        this.periodeDateFin = periodeDateFin;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

}