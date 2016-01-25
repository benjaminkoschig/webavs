package ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul;

/**
 * Encapsulation de la relation idPersonne <-> idTypeDechambre pour un home Correction BZ 9142 (1.12.03) --> taxes
 * journlières non trouvé
 * 
 * @author sce
 * 
 */
public class PersonneTypeChambre {

    private String idPersonne;
    private String idTypeChambre;

    public PersonneTypeChambre(String idPersonne, String idTypeChambre) {
        super();
        this.idPersonne = idPersonne;
        this.idTypeChambre = idTypeChambre;

        if ((this.idPersonne == null) || (this.idPersonne.length() == 0)) {
            throw new IllegalArgumentException("The id personne passed cannot be null or empty [" + this.idPersonne
                    + "]");
        }
        if (this.idTypeChambre == null) {
            throw new IllegalArgumentException("The id type chambre passed cannot be null or empty ["
                    + this.idTypeChambre + "]");
        }
    }

    public String getIdPersonne() {
        return idPersonne;
    }

    public String getIdTypeChambre() {
        return idTypeChambre;
    }

    @Override
    public String toString() {
        return "PersonneTypeChambre [idPersonne=" + idPersonne + ", idTypeChambre=" + idTypeChambre + "]";
    }

}
