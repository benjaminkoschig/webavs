package globaz.naos.db.statOfas;

import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;
import globaz.naos.helpers.statOfas.AFStatistiquesOfasHelper;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFStatistiquesOfasViewBean extends AFAbstractViewBean {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String email;
    private String forAnnee;
    private String idTypeAdresse;
    private Vector<String[]> vectorTypeAdresse;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public AFStatistiquesOfasViewBean() throws Exception {
        vectorTypeAdresse = new Vector<String[]>();
    }

    /**
     * @param session
     */
    public AFStatistiquesOfasViewBean(BSession session) {
        vectorTypeAdresse = new Vector<String[]>();
    }

    /**
     * getter pour l'attribut email
     * 
     * @return la valeur courante de l'attribut email
     */
    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    /**
     * getter pour l'attribut from date.
     * 
     * @return la valeur courante de l'attribut from date
     */
    public String getForAnnee() {
        return forAnnee;

    }

    public String getIdTypeAdresse() {
        return idTypeAdresse;
    }

    public Vector<String[]> getVectorTypeAdresse() {
        return vectorTypeAdresse;
    }

    @Override
    public void retrieve() throws Exception {

        FWParametersSystemCode codeSystemTypeAdresseDomicile = new FWParametersSystemCode();
        codeSystemTypeAdresseDomicile.setSession(getSession());
        codeSystemTypeAdresseDomicile.getCode(AFStatistiquesOfasHelper.TYPE_ADRESSE_DOMICILE);

        vectorTypeAdresse.add(new String[] { codeSystemTypeAdresseDomicile.getIdCode(),
                codeSystemTypeAdresseDomicile.getCurrentCodeUtilisateur().getLibelle() });

        FWParametersSystemCode codeSystemTypeAdresseCourrier = new FWParametersSystemCode();
        codeSystemTypeAdresseCourrier.setSession(getSession());
        codeSystemTypeAdresseCourrier.getCode(AFStatistiquesOfasHelper.TYPE_ADRESSE_COURRIER);
        vectorTypeAdresse.add(new String[] { codeSystemTypeAdresseCourrier.getIdCode(),
                codeSystemTypeAdresseCourrier.getCurrentCodeUtilisateur().getLibelle() });
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * setter pour l'attribut email
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEmail(String string) {
        email = string;
    }

    /**
     * setter pour l'attribut from date.
     * 
     * @param fromDate
     *            une nouvelle valeur pour cet attribut
     */
    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setIdTypeAdresse(String idTypeAdresse) {
        this.idTypeAdresse = idTypeAdresse;
    }

    public void setVectorTypeAdresse(Vector<String[]> vectorTypeAdresse) {
        this.vectorTypeAdresse = vectorTypeAdresse;
    }

}
