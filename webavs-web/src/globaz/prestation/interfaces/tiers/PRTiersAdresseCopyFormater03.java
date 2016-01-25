package globaz.prestation.interfaces.tiers;

import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.formater.ITIAdresseFormater;
import java.util.Hashtable;

/**
 * Classe permettant le formattage de l'adresse des offices AI dans les en-têtes AI (décision rentes et IJAI)
 * 
 * Format souhaité :
 * 
 * Office AI du canton de Neuchâtel Case postale 2183, 2302 La Chaux-de-Fonds
 * 
 * Avec gestion des virgules si adresse vide
 * 
 * Utilisé pour les décision de rentes et IJAI
 * 
 * @author HPE
 * 
 */
public class PRTiersAdresseCopyFormater03 implements ITIAdresseFormater {

    /**
     * Constructeur
     */
    public PRTiersAdresseCopyFormater03() {
        super();
    }

    @Override
    public String format(TIAbstractAdresseDataSource dataSource) {
        if (dataSource == null) {
            return "";
        }
        Hashtable data = dataSource.getData();
        StringBuffer adresse = new StringBuffer();

        // Nom principal
        if (data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D1).toString().length() > 0) {
            adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D1));
        }

        if (data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D2).toString().length() > 0) {
            adresse.append(" ");
            adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D2));
        }

        if (data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D3).toString().length() > 0) {
            adresse.append(" ");
            adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D3));
        }

        if (adresse.length() > 0) {
            adresse.append("\n");
        }

        if (data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE).toString().length() > 0) {
            adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE));
            adresse.append(", ");
        } else if (data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_CASE_POSTALE).toString().length() > 0
                && !JadeStringUtil.isBlank(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_CASE_POSTALE).toString())) {
            adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_CASE_POSTALE));
            adresse.append(", ");
        }

        if (data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA).toString().length() > 0) {
            adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA));
            adresse.append(" ");
        }

        if (data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE).toString().length() > 0) {
            adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE));
        }

        return adresse.toString();
    }
}
