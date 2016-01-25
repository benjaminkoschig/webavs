package ch.globaz.perseus.business.statsmensuelles;

/**
 * @author RCO
 * 
 */
public class Administration implements Comparable<Administration> {

    String idAdministration = null;
    String nomAdminstration = null;

    /**
     * @param idAdministration Identifient de l'administration
     * @param nomAdminstration Nom de l'administration
     */
    public Administration(String idAdministration, String nomAdminstration) {
        this.idAdministration = idAdministration;
        this.nomAdminstration = nomAdminstration;
    }

    /**
     * @return L'identifiant de l'administration
     */
    public String getIdAdministration() {
        return idAdministration;
    }

    /**
     * @return Le nom de l'administration
     */
    public String getNomAdminstration() {
        return nomAdminstration;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Administration o) {
        int result = nomAdminstration.compareTo(o.getNomAdminstration());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Administration [idAdministration=" + idAdministration + ", nomAdminstration=" + nomAdminstration + "]";
    }

}
