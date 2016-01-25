package ch.globaz.pegasus.business.models.habitat;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.home.TypeChambre;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class TaxeJournaliereHome extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /* private PrixChambre prixChambre = null; */
    private SimpleTaxeJournaliereHome simpleTaxeJournaliereHome = null;
    private PersonneEtendueComplexModel tiersAssurenceMaladie = null;
    private TypeChambre typeChambre = null;

    public TaxeJournaliereHome() {
        super();
        simpleTaxeJournaliereHome = new SimpleTaxeJournaliereHome();
        typeChambre = new TypeChambre();
        tiersAssurenceMaladie = new PersonneEtendueComplexModel();
    }

    @Override
    public String getId() {
        return simpleTaxeJournaliereHome.getId();
    }

    /**
     * @return the simpleTaxeJournaliereHome
     */
    public SimpleTaxeJournaliereHome getSimpleTaxeJournaliereHome() {
        return simpleTaxeJournaliereHome;
    }

    @Override
    public String getSpy() {
        return simpleTaxeJournaliereHome.getSpy();
    }

    /**
     * @return the tiersAssurenceMaladie
     */
    public PersonneEtendueComplexModel getTiersAssurenceMaladie() {
        return tiersAssurenceMaladie;
    }

    /**
     * @return the typeChambre
     */
    public TypeChambre getTypeChambre() {
        return typeChambre;
    }

    @Override
    public void setId(String id) {
        simpleTaxeJournaliereHome.setId(id);
    }

    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader donneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(donneeFinanciereHeader);
        simpleTaxeJournaliereHome.setIdDonneeFinanciereHeader(donneeFinanciereHeader.getId());
    }

    /**
     * @param simpleTaxeJournaliereHome
     *            the simpleTaxeJournaliereHome to set
     */
    public void setSimpleTaxeJournaliereHome(SimpleTaxeJournaliereHome simpleTaxeJournaliereHome) {
        this.simpleTaxeJournaliereHome = simpleTaxeJournaliereHome;
    }

    @Override
    public void setSpy(String spy) {
        simpleTaxeJournaliereHome.setSpy(spy);
    }

    /**
     * @param tiersAssurenceMaladie
     *            the tiersAssurenceMaladie to set
     */
    public void setTiersAssurenceMaladie(PersonneEtendueComplexModel tiersAssurenceMaladie) {
        this.tiersAssurenceMaladie = tiersAssurenceMaladie;
    }

    /**
     * @param typeChambre
     *            the typeChambre to set
     */
    public void setTypeChambre(TypeChambre typeChambre) {
        this.typeChambre = typeChambre;
    }

}
