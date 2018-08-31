package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.pyxis.business.model.AdministrationSimpleModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSimpleModel;
import ch.globaz.pyxis.business.model.PersonneSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurSimpleModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailSimpleModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurSimpleModel;

/**
 * @author Arnaud Geiser (AGE) | Créé le 20 févr. 2014
 * 
 */
public class LigneDecompteComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = 1L;

    private LigneDecompteSimpleModel ligneDecompteSimpleModel;

    // PosteTravailComplexModel
    private PosteTravailSimpleModel posteTravailSimpleModel;
    private EmployeurSimpleModel employeurSimpleModel;
    private AffiliationSimpleModel affiliationSimpleModel;
    private TiersSimpleModel employeurTiersSimpleModel;
    private AdministrationSimpleModel conventionAdministrationSimpleModel;
    private TiersSimpleModel conventionTiersSimpleModel;
    private HistoriqueDecompteSimpleModel historiqueDecompteSimpleModel;
    // TravailleurComplexModel
    private TravailleurSimpleModel travailleurSimpleModel;
    private TiersSimpleModel travailleurTiersSimpleModel;
    private PersonneSimpleModel travailleurPersonneSimpleModel;
    private PersonneEtendueSimpleModel travailleurPersonneEtendueSimpleModel;
    private PassageModel passageModel;

    private DecompteSimpleModel decompteSimpleModel;

    public LigneDecompteComplexModel() {
        ligneDecompteSimpleModel = new LigneDecompteSimpleModel();

        posteTravailSimpleModel = new PosteTravailSimpleModel();
        employeurSimpleModel = new EmployeurSimpleModel();
        affiliationSimpleModel = new AffiliationSimpleModel();
        employeurTiersSimpleModel = new TiersSimpleModel();
        conventionAdministrationSimpleModel = new AdministrationSimpleModel();
        conventionTiersSimpleModel = new TiersSimpleModel();

        travailleurSimpleModel = new TravailleurSimpleModel();
        travailleurTiersSimpleModel = new TiersSimpleModel();
        travailleurPersonneSimpleModel = new PersonneSimpleModel();
        travailleurPersonneEtendueSimpleModel = new PersonneEtendueSimpleModel();

        passageModel = new PassageModel();

        decompteSimpleModel = new DecompteSimpleModel();
        historiqueDecompteSimpleModel = new HistoriqueDecompteSimpleModel();
    }

    public PassageModel getPassageModel() {
        return passageModel;
    }

    public void setPassageModel(PassageModel passageModel) {
        this.passageModel = passageModel;
    }

    public LigneDecompteSimpleModel getLigneDecompteSimpleModel() {
        return ligneDecompteSimpleModel;
    }

    public void setLigneDecompteSimpleModel(LigneDecompteSimpleModel ligneDecompteSimpleModel) {
        this.ligneDecompteSimpleModel = ligneDecompteSimpleModel;
    }

    public PosteTravailSimpleModel getPosteTravailSimpleModel() {
        return posteTravailSimpleModel;
    }

    public void setPosteTravailSimpleModel(PosteTravailSimpleModel posteTravailSimpleModel) {
        this.posteTravailSimpleModel = posteTravailSimpleModel;
    }

    public EmployeurSimpleModel getEmployeurSimpleModel() {
        return employeurSimpleModel;
    }

    public void setEmployeurSimpleModel(EmployeurSimpleModel employeurSimpleModel) {
        this.employeurSimpleModel = employeurSimpleModel;
    }

    public AffiliationSimpleModel getAffiliationSimpleModel() {
        return affiliationSimpleModel;
    }

    public void setAffiliationSimpleModel(AffiliationSimpleModel affiliationSimpleModel) {
        this.affiliationSimpleModel = affiliationSimpleModel;
    }

    public TiersSimpleModel getEmployeurTiersSimpleModel() {
        return employeurTiersSimpleModel;
    }

    public void setEmployeurTiersSimpleModel(TiersSimpleModel employeurTiersSimpleModel) {
        this.employeurTiersSimpleModel = employeurTiersSimpleModel;
    }

    public AdministrationSimpleModel getConventionAdministrationSimpleModel() {
        return conventionAdministrationSimpleModel;
    }

    public void setConventionAdministrationSimpleModel(AdministrationSimpleModel conventionAdministrationSimpleModel) {
        this.conventionAdministrationSimpleModel = conventionAdministrationSimpleModel;
    }

    public TiersSimpleModel getConventionTiersSimpleModel() {
        return conventionTiersSimpleModel;
    }

    public void setConventionTiersSimpleModel(TiersSimpleModel conventionTiersSimpleModel) {
        this.conventionTiersSimpleModel = conventionTiersSimpleModel;
    }

    public TravailleurSimpleModel getTravailleurSimpleModel() {
        return travailleurSimpleModel;
    }

    public void setTravailleurSimpleModel(TravailleurSimpleModel travailleurSimpleModel) {
        this.travailleurSimpleModel = travailleurSimpleModel;
    }

    public TiersSimpleModel getTravailleurTiersSimpleModel() {
        return travailleurTiersSimpleModel;
    }

    public void setTravailleurTiersSimpleModel(TiersSimpleModel travailleurTiersSimpleModel) {
        this.travailleurTiersSimpleModel = travailleurTiersSimpleModel;
    }

    public PersonneSimpleModel getTravailleurPersonneSimpleModel() {
        return travailleurPersonneSimpleModel;
    }

    public void setTravailleurPersonneSimpleModel(PersonneSimpleModel travailleurPersonneSimpleModel) {
        this.travailleurPersonneSimpleModel = travailleurPersonneSimpleModel;
    }

    public PersonneEtendueSimpleModel getTravailleurPersonneEtendueSimpleModel() {
        return travailleurPersonneEtendueSimpleModel;
    }

    public void setTravailleurPersonneEtendueSimpleModel(
            PersonneEtendueSimpleModel travailleurPersonneEtendueSimpleModel) {
        this.travailleurPersonneEtendueSimpleModel = travailleurPersonneEtendueSimpleModel;
    }

    public DecompteSimpleModel getDecompteSimpleModel() {
        return decompteSimpleModel;
    }

    public void setDecompteSimpleModel(DecompteSimpleModel decompteSimpleModel) {
        this.decompteSimpleModel = decompteSimpleModel;
    }

    public HistoriqueDecompteSimpleModel getHistoriqueDecompteSimpleModel() {
        return historiqueDecompteSimpleModel;
    }

    public void setHistoriqueDecompteSimpleModel(HistoriqueDecompteSimpleModel historiqueDecompteSimpleModel) {
        this.historiqueDecompteSimpleModel = historiqueDecompteSimpleModel;
    }

    @Override
    public String getId() {
        return ligneDecompteSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return ligneDecompteSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        ligneDecompteSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        ligneDecompteSimpleModel.setSpy(spy);
    }
}
