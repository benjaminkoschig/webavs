package globaz.prestation.interfaces.tiers;

import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.formater.ITIAdresseFormater;
import java.util.Hashtable;

/**
 * Classe permettant le formattage d'une adresse de copie "Copie a :"
 * 
 * Retourne l'adresse sur une seule ligne: Titre Nom Prénom, Rue noRue, NPA Localité
 * 
 * Utilisé pour les décisions maternité
 * 
 * @author FGO
 * 
 */
public class PRTiersAdresseCopyFormater01 implements ITIAdresseFormater {

    /**
     * Constructeur
     */
    public PRTiersAdresseCopyFormater01() {
        super();
    }

    @Override
    public String format(TIAbstractAdresseDataSource dataSource) {
        if (dataSource == null) {
            return "";
        }
        Hashtable data = dataSource.getData();
        StringBuffer adresse = new StringBuffer();

        adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_TITRE)).append(" ");
        adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D1)).append(" ");
        adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D2)).append(", ");
        adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE)).append(" ");
        adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO)).append(", ");
        adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA)).append(" ");
        adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE)).append(" ");
        return adresse.toString();
    }
}
