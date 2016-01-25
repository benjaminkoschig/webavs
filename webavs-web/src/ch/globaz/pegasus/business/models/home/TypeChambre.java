package ch.globaz.pegasus.business.models.home;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class TypeChambre extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Home home = null;
    private PersonneEtendueComplexModel personneEtendue = null;
    private SimpleTypeChambre simpleTypeChambre = null;

    public TypeChambre() {
        super();
        personneEtendue = new PersonneEtendueComplexModel();
        simpleTypeChambre = new SimpleTypeChambre();
        home = new Home();
    }

    /**
     * @return the home
     */
    public Home getHome() {
        return home;
    }

    @Override
    public String getId() {
        return simpleTypeChambre.getIdTypeChambre();
    }

    /**
     * @return the personneEtendue
     */
    public PersonneEtendueComplexModel getPersonneEtendue() {
        return personneEtendue;
    }

    /**
     * @return the typeChambre
     */
    public SimpleTypeChambre getSimpleTypeChambre() {
        return simpleTypeChambre;
    }

    @Override
    public String getSpy() {
        return simpleTypeChambre.getSpy();
    }

    /**
     * @param home
     *            the home to set
     */
    public void setHome(Home home) {
        this.home = home;
        simpleTypeChambre.setIdHome(home.getId());
    }

    @Override
    public void setId(String id) {
        simpleTypeChambre.setIdTypeChambre(id);

    }

    /**
     * @param personneEtendue
     *            the personneEtendue to set
     */
    public void setPersonneEtendue(PersonneEtendueComplexModel personneEtendue) {
        this.personneEtendue = personneEtendue;
    }

    /**
     * @param typeChambre
     *            the typeChambre to set
     */
    public void setSimpleTypeChambre(SimpleTypeChambre typeChambre) {
        simpleTypeChambre = typeChambre;
    }

    @Override
    public void setSpy(String spy) {
        simpleTypeChambre.setSpy(spy);
    }

    public String getDesignationTypeChambre() {

        String designation = simpleTypeChambre.getDesignation();
        String tier = getTierDesignation();

        if (JadeStringUtil.isBlank(designation)) {
            designation = "[TypeChambre designation not found]";
        }

        if (tier != null) {
            designation += " - " + tier;
        }

        return designation;
    }

    String getTierDesignation() {
        String tier = null;
        if ((simpleTypeChambre != null) && simpleTypeChambre.getIsParticularite() && isDesignation1And2NotNull()) {
            tier = personneEtendue.getTiers().getDesignation1().trim() + ", "
                    + personneEtendue.getTiers().getDesignation2().trim();
        }
        return tier;
    }

    boolean isDesignation1And2NotNull() {
        return (null != personneEtendue.getTiers().getDesignation1())
                && (null != personneEtendue.getTiers().getDesignation2());
    }
}
