package globaz.prestation.interfaces.tiers;

import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.formater.ITIAdresseFormater;
import java.util.Hashtable;

/**
 * Classe permettant le formattage d'une adresse de copie "Copie a :"
 * 
 * Retourne l'adresse sur plusieurs lignes: Nom Prénom Rue noRue (si la rue est renseignée) Case Postale (si la case
 * postale est renseignée) NPA Localité
 * 
 * Utilisé pour les transferts de caisses.
 * 
 * @author PCA
 * 
 */
public class PRTiersAdresseCopyFormater06 implements ITIAdresseFormater {

    /**
     * Constructeur
     */
    public PRTiersAdresseCopyFormater06() {
        super();
    }

    @Override
    public String format(TIAbstractAdresseDataSource dataSource) {
        if (dataSource == null) {
            return "";
        }
        Hashtable data = dataSource.getData();
        StringBuffer adresse = new StringBuffer();

        adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D1)).append(" ");
        adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D2)).append("\n");
        ;

        if ((data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE).toString().length() > 1)) {
            adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE)).append(" ");
            adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO)).append("\n");
        }

        if ((data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_CASE_POSTALE).toString().length() > 1)) {
            adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_CASE_POSTALE)).append("\n");
        }

        adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA)).append(" ");
        adresse.append(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE));
        return adresse.toString();
    }
}