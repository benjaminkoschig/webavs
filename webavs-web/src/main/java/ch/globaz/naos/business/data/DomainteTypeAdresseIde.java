/**
 * 
 */
package ch.globaz.naos.business.data;

import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;

/**
 * Classe représentant le duo domaine / type renseigné dans les propriétés pour les cascades IDE
 * 
 * @author est
 */
public class DomainteTypeAdresseIde {

    private TIAdresseDataSource adresse;
    private Boolean adresseTrouvee;
    private String domaine;
    private String type;

    public DomainteTypeAdresseIde() {
        setAdresseTrouvee(false);
    }

    public TIAdresseDataSource getAdresse() {
        return adresse;
    }

    public String getDomaine() {
        return domaine;
    }

    public String getType() {
        return type;
    }

    public Boolean getAdresseTrouvee() {
        return adresseTrouvee;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAdresse(TIAdresseDataSource adresse) {
        this.adresse = adresse;
    }

    public void setAdresseTrouvee(Boolean adresseTrouvee) {
        this.adresseTrouvee = adresseTrouvee;
    }

    @Override
    public String toString() {

        return "(" + domaine + "," + type + ") - " + adresse.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE)
                + ", " + adresse.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
    }

}