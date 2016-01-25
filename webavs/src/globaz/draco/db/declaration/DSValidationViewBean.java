package globaz.draco.db.declaration;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.utils.CAUtil;

public class DSValidationViewBean extends DSDeclarationViewBean implements FWViewBeanInterface {

    private static final long serialVersionUID = 5752364509545602204L;
    private String eMailAddress = "";
    private boolean isBouclementAcompteForAnneeDSExistant;
    private boolean isDSAZero;

    public DSValidationViewBean() {
        super();
        isBouclementAcompteForAnneeDSExistant = false;
        isDSAZero = false;
    }

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        super._afterRetrieve(transaction);

        /**
         * Chargement d'informations supplémentaires nécessaires au mandat InfoRom 336
         */
        try {
            CACompteAnnexe theCompteAnnexe = getCompteAnnexe();
            isBouclementAcompteForAnneeDSExistant = false;
            isDSAZero = false;

            if (DSDeclarationViewBean.CS_PRINCIPALE.equalsIgnoreCase(getTypeDeclaration()) && (theCompteAnnexe != null)
                    && !JadeStringUtil.isEmpty(getAnnee())
                    && !JadeStringUtil.isEmpty(theCompteAnnexe.getIdCompteAnnexe())) {
                isBouclementAcompteForAnneeDSExistant = CAUtil.existeSection(getSession(),
                        theCompteAnnexe.getIdCompteAnnexe(), APISection.ID_CATEGORIE_SECTION_BOUCLEMENT_ACOMPTE,
                        getAnnee());
                isDSAZero = isDSAZero();
            }

        } catch (Exception e) {
            throw new Exception("unable to load InfoRom 336 info due to : " + e.toString());
        }
    }

    /**
     * @return
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getEmailAdressEcran() {
        return getSession().getUserEMail();
    }

    public boolean getIsBouclementAcompteForAnneeDSExistant() {
        return isBouclementAcompteForAnneeDSExistant;
    }

    public boolean getIsDSAZero() {
        return isDSAZero;
    }

    public String htmlOutputChkNotImpressionDecFinalAZero() {

        if (DSDeclarationViewBean.CS_PRINCIPALE.equalsIgnoreCase(getTypeDeclaration())
                && getIsBouclementAcompteForAnneeDSExistant() && getIsDSAZero()) {
            return "<TR><TD>"
                    + getSession().getLabel("LIBELLE_CHKNOTIMPRESSIONDECFINALAZERO")
                    + "</TD><TD><input type=\"checkbox\" id=\"notImpressionDecFinalAZero\" name=\"notImpressionDecFinalAZero\" checked=\"checked\"></TD></TR>";
        }

        if (DSDeclarationViewBean.CS_PRINCIPALE.equalsIgnoreCase(getTypeDeclaration())
                && !getIsBouclementAcompteForAnneeDSExistant() && getIsDSAZero()) {
            return "<TR><TD>"
                    + getSession().getLabel("LIBELLE_CHKNOTIMPRESSIONDECFINALAZERO")
                    + "</TD><TD><input type=\"checkbox\" id=\"notImpressionDecFinalAZero\" name=\"notImpressionDecFinalAZero\"></TD></TR>";
        }

        return "";
    }

    /**
     * @param string
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

}
