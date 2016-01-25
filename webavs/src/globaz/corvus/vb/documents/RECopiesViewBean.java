package globaz.corvus.vb.documents;

import globaz.babel.db.copies.CTCopies;
import globaz.corvus.application.REApplication;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater06;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

public class RECopiesViewBean extends CTCopies implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Object[] METHODES_SEL_COPIES_A = new Object[] { new String[] { "idTiersCopiesDepuisPyxis",
            "idTiers" }
    // ,
    // new String[] {
    // "nomTiers", "nom"
    // },
    // new String[] {
    // "codeIsoLangueDocument", "langue"
    // }
    };

    // private String nomTiers = "";
    // private String codeIsoLangueDocument = "";
    private boolean isRetourDepuisPyxis = false;

    public Object[] getMethodesSelectionCopiesA() {
        return RECopiesViewBean.METHODES_SEL_COPIES_A;
    }

    public String getTiersCopiesAAdresseAsStringHtml(BSession session) {

        if (JadeStringUtil.isBlankOrZero(getIdTiersCopieA())) {
            return "";
        }

        try {
            // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
            // se trouvant dans le fichier corvus.properties
            return PRTiersHelper.getAdresseCourrierFormateeRente(session, getIdTiersCopieA(),
                    REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", new PRTiersAdresseCopyFormater06(),
                    JACalendar.todayJJsMMsAAAA()).replaceAll("[\n]", "<br/>");

        }

        catch (Exception e) {
            return "Tiers adresse not found. idTiers = " + getIdTiersCopieA();
        }

    }

    public String getTiersCopiesAAdresseFormattee() throws Exception {
        if (!JadeStringUtil.isBlankOrZero(getIdTiersCopieA())) {
            // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
            // se trouvant dans le fichier corvus.properties
            return PRTiersHelper.getAdresseCourrierFormateeRente(getISession(), getIdTiersCopieA(),
                    REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");
        } else {
            return "";
        }
    }

    public String getTiersCopiesAInfo(BSession session) {
        try {

            PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, getIdTiersCopieA());

            if (tw == null) {
                tw = PRTiersHelper.getAdministrationParId(session, getIdTiersCopieA());
            }

            String nss = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

            if (JadeStringUtil.isBlankOrZero(nss)) {
                return tw.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                        + tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
            } else {
                return nss + " - " + tw.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                        + tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
            }
        } catch (Exception e) {
            return "Tiers not found. idTiers = " + getIdTiersCopieA();
        }
    }

    public String getTiersRequerantInfo(BSession session) {
        try {
            PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, getIdTiersRequerant());
            return tw.getDescription(session);
        } catch (Exception e) {
            return "Tiers not found. idTiers = " + getIdTiersRequerant();
        }
    }

    public boolean isRetourDepuisPyxis() {
        return isRetourDepuisPyxis;
    }

    public void setIdTiersCopiesDepuisPyxis(String idTiers) {
        setIdTiersCopieA(idTiers);
        isRetourDepuisPyxis = true;
    }

    public void setRetourDepuisPyxis(boolean isRetourDepuisPyxis) {
        this.isRetourDepuisPyxis = isRetourDepuisPyxis;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
