package ch.globaz.pegasus.business.models.process.adaptation;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

public class DonneeFinanciereSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateValable;
    private String forIdDroit;
    private Collection<String> forIdMembreFamilleSFIn;
    private String forNoVersion;

    public String getForDateValable() {
        return forDateValable;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public Collection<String> getForIdMembreFamilleSFIn() {
        return forIdMembreFamilleSFIn;
    }

    public String getForNoVersion() {
        return forNoVersion;
    }

    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForIdMembreFamilleSFIn(Collection<String> forIdMembreFamilleSFIn) {
        this.forIdMembreFamilleSFIn = forIdMembreFamilleSFIn;
    }

    public void setForNoVersion(String forNoVersion) {
        this.forNoVersion = forNoVersion;
    }

    @Override
    public Class<DonneeFinanciere> whichModelClass() {
        return DonneeFinanciere.class;
    }

}
