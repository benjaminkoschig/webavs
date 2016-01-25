package ch.globaz.pegasus.business.models.habitat;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.home.TypeChambre;

public class TaxeJournaliereHomeEtendu extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DroitMembreFamilleEtendu droitMembreFamilleEtendu = null;
    private SimpleTaxeJournaliereHome simpleTaxeJournaliereHome = null;
    private TypeChambre typeChambre = null;

    public TaxeJournaliereHomeEtendu() {
        super();
        simpleTaxeJournaliereHome = new SimpleTaxeJournaliereHome();
        typeChambre = new TypeChambre();
        droitMembreFamilleEtendu = new DroitMembreFamilleEtendu();
    }

    public DroitMembreFamilleEtendu getDroitMembreFamilleEtendu() {
        return droitMembreFamilleEtendu;
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
     * @return the typeChambre
     */
    public TypeChambre getTypeChambre() {
        return typeChambre;
    }

    public void setDroitMembreFamilleEtendu(DroitMembreFamilleEtendu droitMembreFamilleEtendu) {
        this.droitMembreFamilleEtendu = droitMembreFamilleEtendu;
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
     * @param typeChambre
     *            the typeChambre to set
     */
    public void setTypeChambre(TypeChambre typeChambre) {
        this.typeChambre = typeChambre;
    }

}
