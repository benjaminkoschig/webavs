package ch.globaz.vulpecula.domain.models.common;

import java.util.List;

public class DetailGroupeLocalites {
    // Codes syst�mes respectifs pour le district et la r�gion
    public final static String CS_REGION = "520001";
    public final static String CS_DISTRICT = "520002";
    private List<DetailGroupeLocalite> liste;

    public DetailGroupeLocalites(List<DetailGroupeLocalite> liste) {
        this.liste = liste;
    }

    /**
     * It�re sur tous les r�sultats contenu dans la liste et affecte le r�sultat � detailRegion quand typeGroupe est
     * �gale � la constante CS_REGION
     * 
     * @return un objet DetailGroupeLocalite qui contient la r�gion de la localit�
     */
    public DetailGroupeLocalite getRegion() {
        DetailGroupeLocalite detailRegion = null;
        for (DetailGroupeLocalite detail : liste) {
            if (detail != null) {
                if (CS_REGION.equals(detail.getTypeGroupeDetailLocalite())) {
                    detailRegion = detail;
                }
            }
        }
        return detailRegion;
    }

    /**
     * It�re sur tous les r�sultats contenu dans la liste et affecte le r�sultat � detailDistrict quand typeGroupe est
     * �gale � la constante CS_DISTRICT
     * 
     * @return un objet DetailGroupeLocalite qui contient le district de la localit�
     */
    public DetailGroupeLocalite getDistrict() {
        DetailGroupeLocalite detailDistrict = null;
        for (DetailGroupeLocalite detail : liste) {
            if (detail != null) {
                if (CS_DISTRICT.equals(detail.getTypeGroupeDetailLocalite())) {
                    detailDistrict = detail;
                }
            }
        }
        return detailDistrict;
    }

}
