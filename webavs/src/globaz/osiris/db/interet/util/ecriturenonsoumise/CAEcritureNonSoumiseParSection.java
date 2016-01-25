package globaz.osiris.db.interet.util.ecriturenonsoumise;

import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.utils.CADateUtil;

public class CAEcritureNonSoumiseParSection extends CAEcritureNonSoumise {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String ONZE_JANVIER = "11.01.";
    private static final String PREMIER_JANVIER = "01.01.";

    private String categorieSection;
    private String idSection;

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        setIdSection(statement.dbReadNumeric(CAOperation.FIELD_IDSECTION));
        setCategorieSection(statement.dbReadNumeric(CASection.FIELD_CATEGORIESECTION));
    }

    public String getCategorieSection() {
        return categorieSection;
    }

    public String getIdSection() {
        return idSection;
    }

    private String getYearDate() throws Exception {
        return "" + new JADate(getDate()).getYear();
    }

    /**
     * Retourne l'année sur laquelle le paiment s'applique. Si ce dernier est avant le 10 janvier, il s'applique sur
     * l'année précédente.
     * 
     * @param anneeCotisationEntete
     * @return
     * @throws Exception
     */
    public String getYearKey(String anneeCotisationEntete) throws Exception {
        if (Integer.parseInt(getYearDate()) >= Integer.parseInt(anneeCotisationEntete)) {
            JACalendarGregorian cal = new JACalendarGregorian();

            if (!APISection.ID_CATEGORIE_SECTION_DECOMPTE_DECEMBRE.equals(getCategorieSection())
                    && !APISection.ID_CATEGORIE_SECTION_DECISION_4TRIMESTRE.equals(getCategorieSection())
                    && !APISection.ID_CATEGORIE_SECTION_DECISION_ANNUELLE.equals(getCategorieSection())) {
                JADate dateValeur = CADateUtil.getDateOuvrable(new JADate(getDate()));
                JADate dateMax = CADateUtil.getDateOuvrable(new JADate(CAEcritureNonSoumiseParSection.PREMIER_JANVIER
                        + JACalendar.today().getYear()));

                if (cal.compare(dateMax, dateValeur) == JACalendar.COMPARE_FIRSTUPPER) {
                    return getYearDate();
                } else {
                    return null;
                }
            }

            JADate dateValeur = CADateUtil.getDateOuvrable(new JADate(getDate()));
            JADate dateMax = CADateUtil.getDateOuvrable(new JADate(CAEcritureNonSoumiseParSection.ONZE_JANVIER
                    + getYearDate()));

            if (cal.compare(dateMax, dateValeur) == JACalendar.COMPARE_FIRSTUPPER) {
                return "" + cal.addYears(dateValeur, -1).getYear();
            } else {
                return getYearDate();
            }
        } else {
            return getYearDate();
        }
    }

    public void setCategorieSection(String categorieSection) {
        this.categorieSection = categorieSection;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

}
