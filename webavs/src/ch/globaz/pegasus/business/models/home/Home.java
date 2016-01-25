package ch.globaz.pegasus.business.models.home;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.Adresse;

public class Home extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // private AdministrationAdresseComplexModel adminHome = null;
    // private PersonneEtendueAdresseComplexModel tiersHome = null;
    private Adresse adresse = null;
    private SimpleHome simpleHome = null;

    public Home() {
        super();
        adresse = new Adresse();
        simpleHome = new SimpleHome();
    }

    public Adresse getAdresse() {
        return adresse;
    }

    @Override
    public String getId() {
        return simpleHome.getIdHome();
    }

    /**
     * @return the home
     */
    public SimpleHome getSimpleHome() {
        return simpleHome;
    }

    @Override
    public String getSpy() {
        return simpleHome.getSpy();
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    @Override
    public void setId(String id) {
        simpleHome.setIdHome(id);
    }

    /**
     * @param home the home to set
     */
    public void setSimpleHome(SimpleHome home) {
        simpleHome = home;
    }

    @Override
    public void setSpy(String spy) {
        simpleHome.setSpy(spy);
    }

}
