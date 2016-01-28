package ch.globaz.perseus.business.models.donneesfinancieres;

import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Revenu extends DonneeFinanciere {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleDonneeFinanciereSpecialisation simpleDonneeFinanciereSpecialisation = null;
    private RevenuType type = null;

    public Revenu() {
        super();
        simpleDonneeFinanciereSpecialisation = new SimpleDonneeFinanciereSpecialisation();
    }

    public Revenu(RevenuType type) {
        this();
        this.setType(type);
    }

    /**
     * Permet de copier les éléments d'un Revenu dans une autre sans changer d'objet
     * 
     * @param Revenu
     *            à copier dans l'objet
     */
    public void copy(Revenu revenu) {
        super.copy(revenu);
        simpleDonneeFinanciereSpecialisation = revenu.getSimpleDonneeFinanciereSpecialisation();
        type = revenu.getTypeAsEnum();
        // this.setNew(revenu.isNew());
    }

    /**
     * @return the avec13eme
     */
    public Boolean getAvec13eme() {
        return simpleDonneeFinanciereSpecialisation.getAvec13eme();
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return simpleDonneeFinanciereSpecialisation.getDateDebut();
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return simpleDonneeFinanciereSpecialisation.getDateFin();
    }

    /**
     * Retourn directement une liste des rentes pour les cases à cocher
     * 
     * @return the typeRentes
     */
    public List<String> getListCsTypeRentes() {
        ArrayList<String> list = new ArrayList<String>();
        if (!JadeStringUtil.isEmpty(simpleDonneeFinanciereSpecialisation.getListCsTypeRentes())) {
            StringTokenizer strToken = new StringTokenizer(simpleDonneeFinanciereSpecialisation.getListCsTypeRentes(),
                    ",");
            while (strToken.hasMoreElements()) {
                list.add(strToken.nextToken());
            }
        }
        return list;
    }

    /**
     * @return the plusieursEmployeurs
     */
    public Boolean getPlusieursEmployeurs() {
        return simpleDonneeFinanciereSpecialisation.getPlusieursEmployeurs();
    }

    /**
     * @return the simpleDonneeFinanciereSpecialisation
     */
    public SimpleDonneeFinanciereSpecialisation getSimpleDonneeFinanciereSpecialisation() {
        return simpleDonneeFinanciereSpecialisation;
    }

    /**
     * @return the tauxOccupation
     */
    public String getTauxOccupation() {
        return simpleDonneeFinanciereSpecialisation.getTauxOccupation();
    }

    /**
     * @return the type
     */
    public String getType() {
        if (type == null) {
            return getSimpleDonneeFinanciere().getType();
        } else {
            return type.toString();
        }
    }

    /**
     * @return the type as an enum
     */
    public RevenuType getTypeAsEnum() {
        if (type == null) {
            return RevenuType.getTypeFromId(getSimpleDonneeFinanciere().getType());
        } else {
            return type;
        }
    }

    /**
     * @param avec13eme
     *            the avec13eme to set
     */
    public void setAvec13eme(Boolean avec13eme) {
        simpleDonneeFinanciereSpecialisation.setAvec13eme(avec13eme);
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        simpleDonneeFinanciereSpecialisation.setDateDebut(dateDebut);
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(String dateFin) {
        simpleDonneeFinanciereSpecialisation.setDateFin(dateFin);
    }

    /**
     * @param typeRentes
     *            the typeRentes to set
     */
    public void setListCsTypeRentes(List<String> listCsTypeRentes) {
        boolean isFirst = true;
        String listeComma = "";
        for (String str : listCsTypeRentes) {
            if (isFirst) {
                listeComma += str;
                isFirst = false;
            } else {
                listeComma += "," + str;
            }
        }

        simpleDonneeFinanciereSpecialisation.setListCsTypeRentes(listeComma);
    }

    /**
     * @param plusieursEmployeurs
     *            the plusieursEmployeurs to set
     */
    public void setPlusieursEmployeurs(Boolean plusieursEmployeurs) {
        simpleDonneeFinanciereSpecialisation.setPlusieursEmployeurs(plusieursEmployeurs);
    }

    /**
     * @param simpleDonneeFinanciereSpecialisation
     *            the simpleDonneeFinanciereSpecialisation to set
     */
    public void setSimpleDonneeFinanciereSpecialisation(
            SimpleDonneeFinanciereSpecialisation simpleDonneeFinanciereSpecialisation) {
        this.simpleDonneeFinanciereSpecialisation = simpleDonneeFinanciereSpecialisation;
    }

    /**
     * @param tauxOccupation
     *            the tauxOccupation to set
     */
    public void setTauxOccupation(String tauxOccupation) {
        simpleDonneeFinanciereSpecialisation.setTauxOccupation(tauxOccupation);
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(RevenuType type) {
        this.type = type;
        getSimpleDonneeFinanciere().setType(type.toString());
    }

    /**
     * @param type
     *            Id type venant de la base de donnée
     */
    public void setType(String idType) {
        getSimpleDonneeFinanciere().setType(idType);
        type = RevenuType.getTypeFromId(idType);
    }

}
