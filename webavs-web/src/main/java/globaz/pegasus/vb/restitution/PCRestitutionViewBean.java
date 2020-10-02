package globaz.pegasus.vb.restitution;

import ch.globaz.pegasus.business.exceptions.models.restitution.PCRestitutionException;
import ch.globaz.pegasus.business.models.dossier.Dossier;
import ch.globaz.pegasus.business.models.dossier.DossierSearch;
import ch.globaz.pegasus.business.models.restitution.SimpleRestitution;
import ch.globaz.pegasus.business.models.restitution.SimpleRestitutionSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;

public class PCRestitutionViewBean extends BJadePersistentObjectViewBean {

    private SimpleRestitution simpleRestitution;
    private String idDossier;
    private PersonneEtendueComplexModel personne;

    public PCRestitutionViewBean() {
        super();
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(simpleRestitution.getSpy());
    }

    @Override
    public void add() throws Exception {
        simpleRestitution = PegasusServiceLocator.getRestitutionService().create(simpleRestitution);
    }

    @Override
    public void delete() throws Exception {
    }

    @Override
    public String getId() {
        return simpleRestitution.getId();
    }

    @Override
    public void retrieve() throws Exception {
        if (JadeStringUtil.isEmpty(idDossier)) {
            throw new PCRestitutionException("Unable to read restititon, the id dossier passed is null!");
        }

        SimpleRestitutionSearch restitutionSearch = new SimpleRestitutionSearch();
        restitutionSearch.setForIdDossier(idDossier);
        PegasusServiceLocator.getRestitutionService().search(restitutionSearch);

        if (restitutionSearch.getSize() == 0) {
            simpleRestitution = new SimpleRestitution();
            simpleRestitution.setIdDossier(idDossier);
        } else if (restitutionSearch.getSize() == 1) {
            simpleRestitution = (SimpleRestitution) restitutionSearch.getSearchResults()[0];
        } else {
            throw new PCRestitutionException(
                    "Erreur lors de la récupération des la restitution : plusieurs restitutions existent pour un même dossier : " + idDossier);
        }

        DossierSearch dossierSearch = new DossierSearch();
        dossierSearch.setForIdDossier(idDossier);
        PegasusServiceLocator.getDossierService().search(dossierSearch);
        Dossier dossier;
        if (dossierSearch.getSize() == 1) {
            dossier = (Dossier) dossierSearch.getSearchResults()[0];
        } else {
            throw new PCRestitutionException(
                    "Erreur lors de la récupération du dossier : " + idDossier);
        }

        personne = dossier.getDemandePrestation().getPersonneEtendue();

    }

    @Override
    public void setId(String selectedId) {
    }

    @Override
    public void update() throws Exception {
        simpleRestitution = PegasusServiceLocator.getRestitutionService().update(simpleRestitution);
    }


    public PersonneEtendueComplexModel getPersonne() {
        return personne;
    }

    public void setSimpleRestitution(SimpleRestitution simpleRestitution) {
        this.simpleRestitution = simpleRestitution;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setPersonne(PersonneEtendueComplexModel personne) {
        this.personne = personne;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public SimpleRestitution getSimpleRestitution() {
        return simpleRestitution;
    }

}
