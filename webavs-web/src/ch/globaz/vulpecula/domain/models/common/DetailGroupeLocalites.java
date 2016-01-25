package ch.globaz.vulpecula.domain.models.common;

import java.util.List;

public class DetailGroupeLocalites {
    // Codes systèmes respectifs pour le district et la région
    public final static String CS_REGION = "520001";
    public final static String CS_DISTRICT = "520002";
    private List<DetailGroupeLocalite> liste;

    public DetailGroupeLocalites(List<DetailGroupeLocalite> liste) {
        this.liste = liste;
    }

    /**
     * Itère sur tous les résultats contenu dans la liste et affecte le résultat à detailRegion quand typeGroupe est
     * égale à la constante CS_REGION
     * 
     * @return un objet DetailGroupeLocalite qui contient la région de la localité
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
     * Itère sur tous les résultats contenu dans la liste et affecte le résultat à detailDistrict quand typeGroupe est
     * égale à la constante CS_DISTRICT
     * 
     * @return un objet DetailGroupeLocalite qui contient le district de la localité
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
