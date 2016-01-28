package globaz.cygnus.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * @author PBA
 */
public class RFListeContributionsAssistanceAIViewBean extends PRAbstractViewBeanSupport {

    public static enum TypePeriodeCouverte {
        EnCoursAu("enCoursAu"),
        EnCoursDurant("enCoursDurant"),
        SansPeriode("sansPeriode");

        public static TypePeriodeCouverte getValeurPourString(String cle) {
            if (EnCoursAu.valeurPourBoutonRadio.equals(cle)) {
                return EnCoursAu;
            } else if (EnCoursDurant.valeurPourBoutonRadio.equals(cle)) {
                return EnCoursDurant;
            } else if (SansPeriode.valeurPourBoutonRadio.equals(cle)) {
                return SansPeriode;
            }
            return null;
        }

        private String valeurPourBoutonRadio;

        private TypePeriodeCouverte(String valeurPourBoutonRadio) {
            this.valeurPourBoutonRadio = valeurPourBoutonRadio;
        }

        public String getValeurPourBoutonRadio() {
            return valeurPourBoutonRadio;
        }
    };

    private String adresseEmail;
    private TypePeriodeCouverte choixTypePeriode;
    private String dateEnCoursAuLigne1;
    private String dateEnCoursAuLigne2;
    private String dateEnCoursDu;

    public RFListeContributionsAssistanceAIViewBean() {
        super();

        adresseEmail = "";
        choixTypePeriode = TypePeriodeCouverte.EnCoursAu;
        dateEnCoursAuLigne1 = "";
        dateEnCoursAuLigne2 = "";
        dateEnCoursDu = "";
    }

    public String getAdresseEmail() {
        return adresseEmail;
    }

    public TypePeriodeCouverte getChoixTypePeriode() {
        return choixTypePeriode;
    }

    public String getDateEnCoursAuLigne1() {
        return dateEnCoursAuLigne1;
    }

    public String getDateEnCoursAuLigne2() {
        return dateEnCoursAuLigne2;
    }

    public String getDateEnCoursDu() {
        return dateEnCoursDu;
    }

    public void setAdresseEmail(String adresseEmail) {
        this.adresseEmail = adresseEmail;
    }

    public void setChoixTypePeriode(String choixTypePeriode) {
        this.choixTypePeriode = TypePeriodeCouverte.getValeurPourString(choixTypePeriode);
    }

    public void setDateEnCoursAuLigne1(String dateEnCoursAu) {
        dateEnCoursAuLigne1 = dateEnCoursAu;
    }

    public void setDateEnCoursAuLigne2(String dateEnCoursAu) {
        dateEnCoursAuLigne2 = dateEnCoursAu;
    }

    public void setDateEnCoursDu(String dateEnCoursDu) {
        this.dateEnCoursDu = dateEnCoursDu;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
