package ch.globaz.pegasus.business.models.home;

import globaz.jade.persistence.model.JadeComplexModel;

public class PrixChambre extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimplePrixChambre simplePrixChambre = null;
    private TypeChambre typeChambre = null;

    public PrixChambre() {
        super();
        simplePrixChambre = new SimplePrixChambre();
        typeChambre = new TypeChambre();
    }

    public String getPeriode() {
        return simplePrixChambre.getDateDebut().trim() + " - " + simplePrixChambre.getDateFin().trim();

    }

    @Override
    public String getId() {
        return simplePrixChambre.getIdPrixChambre();
    }

    /**
     * @return the prixChambre
     */
    public SimplePrixChambre getSimplePrixChambre() {
        return simplePrixChambre;
    }

    @Override
    public String getSpy() {
        return simplePrixChambre.getSpy();
    }

    /**
     * @return the typeChambre
     */
    public TypeChambre getTypeChambre() {
        return typeChambre;
    }

    @Override
    public void setId(String id) {
        simplePrixChambre.setIdPrixChambre(id);

    }

    /**
     * @param prixChambre
     *            the prix Chambre to set
     */
    public void setSimplePrixChambre(SimplePrixChambre prixChambre) {
        simplePrixChambre = prixChambre;
    }

    @Override
    public void setSpy(String spy) {
        simplePrixChambre.setSpy(spy);
    }

    /**
     * @param typeChambre
     *            the typeChambre to set
     */
    public void setTypeChambre(TypeChambre typeChambre) {
        this.typeChambre = typeChambre;
        simplePrixChambre.setIdTypeChambre(typeChambre.getId());
    }

}
