package globaz.lynx.utils;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAHolidays;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.utils.CADateUtil;
import globaz.pyxis.adresse.datasource.TIAbstractAdressePaiementDataSource;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class LXUtils {

    private static JACalendarGregorian calendarWithHolidays;
    private static final String FILE_HOLIDAYS_XML = "/holidays.xml";

    /**
     * @param dataSource
     * @return
     */
    public static String formateBanque(TIAdressePaiementDataSource dataSource) {
        StringBuffer temp = new StringBuffer();

        temp.append(dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_BANQUE_D1));
        temp.append(" ");
        temp.append(dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_BANQUE_D2));
        temp.append("\n");
        temp.append(dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_BANQUE_NPA));
        temp.append(" ");
        temp.append(dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_BANQUE_LOCALITE));

        return temp.toString();
    }

    /**
     * Test si jour tombe sur un week end ou un jour férié. Si oui incrément de 1 le jour.
     * 
     * @param myDate
     * @return
     */
    public static JADate getDateOuvrable(JADate myDate) throws Exception {
        if (LXUtils.calendarWithHolidays == null) {
            URL url = new CADateUtil().getClass().getResource(LXUtils.FILE_HOLIDAYS_XML);

            if (url != null) {
                File f = new File(url.getFile());
                LXUtils.calendarWithHolidays = new JACalendarGregorian(new JAHolidays(f.getPath()));
            } else {
                LXUtils.calendarWithHolidays = new JACalendarGregorian();
            }
        }

        return LXUtils.calendarWithHolidays.getNextWorkingDay(myDate);
    }

    /**
     * Permet de retourner le montant en négatif
     * 
     * @param montant
     * @return
     */
    public static String getMontantNegatif(String montant) {
        FWCurrency currency = new FWCurrency();
        currency.add(montant);

        if (currency.isPositive()) {
            currency.negate();
        }

        return currency.toString();
    }

    /**
     * Permet de retourner le montant en positif
     * 
     * @param montant
     * @return
     */
    public static String getMontantPositif(String montant) {
        FWCurrency currency = new FWCurrency();
        currency.add(montant);

        if (currency.isNegative()) {
            currency.negate();
        }

        return currency.toString();
    }

    /**
     * Permet la concatenation de la designation1 (nom) et la designation2 (prenom) d'un tiers
     * 
     * @param designation1
     * @param designation2
     */
    public static String getNomComplet(String designation1, String designation2) {
        String d1 = designation1;
        String d2 = designation2;

        if (d1 == null) {
            d1 = "";
        } else {
            d1 = d1.trim();
        }

        if (d2 == null) {
            d2 = "";
        } else {
            d2 = d2.trim();
        }

        String tmp = d1;
        if (!JadeStringUtil.isBlank(d2)) {

            if (!JadeStringUtil.isBlank(d1)) {
                tmp += " ";
            }
            tmp += d2;
        }

        return tmp;
    }

    /**
     * Permet la construction d'une condition multiple lors d'une reques SQL<br>
     * <br>
     * Exemple : <br>
     * Select * from LX... Where idOperation in ( x1, x2, x3, ... )<br>
     * 
     * @param nameField
     * @param values
     * @return
     */
    public static String getWhereValueMultiple(String nameField, ArrayList<String> values) {

        if (JadeStringUtil.isBlank(nameField) || (values == null) || (values.size() < 1)) {
            return "";
        }

        StringBuffer tmp = new StringBuffer(nameField + " in (");

        for (int i = 0; i < values.size(); i++) {
            tmp.append("").append(values.get(i));

            if (i < values.size() - 1) {
                tmp.append(", ");
            }
        }

        tmp.append(") ");

        return tmp.toString();
    }

    /**
     * Constructeur
     */
    protected LXUtils() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }
}
