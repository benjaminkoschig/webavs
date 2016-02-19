package globaz.vulpecula.vb.registre;

import globaz.globall.db.BSpy;
import java.util.List;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class PTDetailParamCotiAPViewBean extends BJadeSearchObjectELViewBean {

    private CotisationAssociationProfessionnelle cotiAP = new CotisationAssociationProfessionnelle();
    private List<Administration> associationsProfessionnelles;
    private String idCotisationAP = "";

    public String getIdCotisationAP() {
        return idCotisationAP;
    }

    public void setIdCotisationAP(String idCotisationAP) {
        this.idCotisationAP = idCotisationAP;
    }

    public String getTitre() {
        // TODO label
        return idCotisationAP.isEmpty() ? "Nouvelle Cotisation Associtation Professionnelle"
                : "Détail de la cotisation \"" + cotiAP.getLibelle() + "\" - "
                        + cotiAP.getAssociationProfessionnelle().getDesignation1() + " "
                        + cotiAP.getAssociationProfessionnelle().getDesignation2();
    }

    public CotisationAssociationProfessionnelle getCotiAP() {
        return cotiAP;
    }

    public void setCotiAP(CotisationAssociationProfessionnelle cotiAP) {
        this.cotiAP = cotiAP;
    }

    public String getLibelleFR() {
        return cotiAP.getLibelleFR();
    }

    public void setLibelleFR(String libelleFR) {
        cotiAP.setLibelleFR(libelleFR);
    }

    public String getLibelleDE() {
        return cotiAP.getLibelleDE();
    }

    public void setLibelleDE(String libelleDE) {
        cotiAP.setLibelleDE(libelleDE);
    }

    public String getLibelleIT() {
        return cotiAP.getLibelleIT();
    }

    public void setLibelleIT(String libelleIT) {
        cotiAP.setLibelleIT(libelleIT);
    }

    public String getLibelle() {
        return cotiAP.getLibelle();
    }

    public void setLibelle(String libelle) {
        cotiAP.setLibelle(libelle);
    }

    public String getGenre() {
        GenreCotisationAssociationProfessionnelle cotiGenre = GenreCotisationAssociationProfessionnelle.MEMBRE;
        if (cotiAP != null && cotiAP.getGenre() != null) {
            cotiGenre = cotiAP.getGenre();
        }
        return cotiGenre.getValue();
    }

    public void setGenre(String libelle) {
        cotiAP.setGenre(GenreCotisationAssociationProfessionnelle.fromValue(libelle));
    }

    public String getIdAssociationProfessionnelle() {
        String idAP = "0";
        if (cotiAP != null && cotiAP.getAssociationProfessionnelle() != null
                && cotiAP.getIdAssociationProfessionnelle() != null) {
            idAP = cotiAP.getIdAssociationProfessionnelle();
        }
        return idAP;
    }

    public void setIdAssociationProfessionnelle(String administrationId) {
        Administration admini = new Administration();
        admini.setId(administrationId);
        cotiAP.setAssociationProfessionnelle(admini);
    }

    public String getMasseSalarialeDefaut() {
        return cotiAP.getMasseSalarialeDefaut().getValue();
    }

    public void setMasseSalarialeDefaut(String taux) {
        Taux masseSalarialeDefaut = new Taux(taux);
        cotiAP.setMasseSalarialeDefaut(masseSalarialeDefaut);
    }

    public PTDetailParamCotiAPViewBean() {
        super();
    }

    public List<Administration> getAssociationsProfessionnelles() {
        return associationsProfessionnelles;
    }

    public void setAssociationsProfessionnelles(List<Administration> associationsProfessionnelles) {
        this.associationsProfessionnelles = associationsProfessionnelles;
    }

    @Override
    public void add() throws Exception {
        cotiAP = VulpeculaRepositoryLocator.getCotisationAssociationProfessionnelleRepository().create(cotiAP);
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
    public void update() throws Exception {
        cotiAP = VulpeculaRepositoryLocator.getCotisationAssociationProfessionnelleRepository().update(cotiAP);
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
}
