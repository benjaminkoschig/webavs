package globaz.prestation.interfaces.tiers;

import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.formater.ITIAdresseFormater;
import java.util.Hashtable;

/**
 * Classe permettant le formattage d'une adresse de copie "Copie à :"
 * 
 * Retourne l'adresse sur une seule ligne: Nom Prénom, NPA Localité
 * 
 * Utilisé pour les décision de rentes
 * 
 * @author HPE
 * 
 */
public class PRTiersAdresseCopyFormater02 implements ITIAdresseFormater {

    /**
     * Constructeur
     */
    public PRTiersAdresseCopyFormater02() {
        super();
    }

    @Override
    public String format(TIAbstractAdresseDataSource dataSource) {
        if (dataSource == null) {
            return "";
        }
        Hashtable data = dataSource.getData();
        StringBuffer adresse = new StringBuffer();

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

        if (data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D4).toString().length() > 0) {
            adresse.append(" ");
            adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D4));
        }

        if (data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D1).toString().length()
                + data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D2).toString().length()
                + data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D3).toString().length()
                + data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D4).toString().length() > 0) {

            adresse.append(",");
        }

        if (data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA).toString().length() > 0) {
            adresse.append(" ");
            adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA));
        }

        if (data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE).toString().length() > 0) {
            adresse.append(" ");
            adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE));
        }

        return adresse.toString();
    }
}
