package ch.globaz.pegasus.business.models.habitat;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.droit.VersionDroit;

public class TaxeJournaliereHomeDroit extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader = null;
    private SimpleTaxeJournaliereHome simpleTaxeJournaliereHome = null;
    private VersionDroit versionDroit;

    public TaxeJournaliereHomeDroit() {
        versionDroit = new VersionDroit();
        simpleTaxeJournaliereHome = new SimpleTaxeJournaliereHome();
        simpleDonneeFinanciereHeader = new SimpleDonneeFinanciereHeader();
    }

    @Override
    public String getId() {
        return simpleTaxeJournaliereHome.getId();
    }

    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return simpleDonneeFinanciereHeader;
    }

    public SimpleTaxeJournaliereHome getSimpleTaxeJournaliereHome() {
        return simpleTaxeJournaliereHome;
    }

    @Override
    public String getSpy() {
        return simpleTaxeJournaliereHome.getSpy();
    }

    public VersionDroit getVersionDroit() {
        return versionDroit;
    }

    @Override
    public void setId(String id) {
        simpleTaxeJournaliereHome.setId(id);
    }

    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        this.simpleDonneeFinanciereHeader = simpleDonneeFinanciereHeader;
    }

    public void setSimpleTaxeJournaliereHome(SimpleTaxeJournaliereHome simpleTaxeJournaliereHome) {
        this.simpleTaxeJournaliereHome = simpleTaxeJournaliereHome;
    }

    @Override
    public void setSpy(String spy) {
        simpleTaxeJournaliereHome.setSpy(spy);
    }

    public void setVersionDroit(VersionDroit versionDroit) {
        this.versionDroit = versionDroit;
    }
}
