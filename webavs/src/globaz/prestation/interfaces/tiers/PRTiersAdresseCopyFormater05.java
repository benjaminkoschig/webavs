package globaz.prestation.interfaces.tiers;

import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.formater.ITIAdresseFormater;
import java.util.Hashtable;

/**
 * Classe permettant le formattage d'une adresse
 * 
 * Retourne l'adresse sur une seule ligne: Nom Prénom
 * 
 * Utilisé pour les décision de rentes
 * 
 * @author HPE
 * 
 */
public class PRTiersAdresseCopyFormater05 implements ITIAdresseFormater {

    /**
     * Constructeur
     */
    public PRTiersAdresseCopyFormater05() {
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

        return adresse.toString();
    }
}
