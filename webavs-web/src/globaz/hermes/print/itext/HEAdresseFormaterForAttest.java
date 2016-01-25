package globaz.hermes.print.itext;

import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.formater.ITIAdresseFormater;

public class HEAdresseFormaterForAttest implements ITIAdresseFormater {
    public static String CS_ADRESSE_EXPLOITATION = "508021";

    private StringBuffer toReturn = new StringBuffer();

    @Override
    public String format(TIAbstractAdresseDataSource src) {

        if (!JadeStringUtil.isBlank(src.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_TITRE))) {
            toReturn.append(src.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_TITRE) + " ");
        }

        if (!JadeStringUtil.isBlank(src.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_D1))) {
            toReturn.append(src.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_D1) + " ");
        }

        if (!JadeStringUtil.isBlank(src.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_D2))) {
            toReturn.append(src.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_D2) + " ");
        }
        if (!JadeStringUtil.isBlank(src.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_D3))) {
            toReturn.append(src.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_D3) + " ");
        }
        if (!JadeStringUtil.isBlank(src.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_D4))) {
            toReturn.append(src.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_D4) + " ");
        }
        // On substring l'espace en trop
        if (toReturn.length() > 1) {
            toReturn = new StringBuffer(toReturn.substring(0, toReturn.length() - 1));
        }
        String adresse = src.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE) + " "
                + src.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);

        if (!JadeStringUtil.isBlank(adresse)) {
            toReturn.append(", " + adresse);
        }
        String casePostale = src.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_CASE_POSTALE);
        if (!JadeStringUtil.isBlank(casePostale)) {
            toReturn.append(", " + casePostale);
        }
        String localite = src.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA) + " "
                + src.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
        if (!JadeStringUtil.isBlank(localite)) {
            toReturn.append(", " + localite);
        }

        return toReturn.toString();
    }

}
