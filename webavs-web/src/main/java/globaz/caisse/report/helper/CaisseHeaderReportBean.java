package globaz.caisse.report.helper;

import globaz.apg.enums.APTypeDePrestation;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.bean.JadeUserDetail;
import globaz.jade.admin.user.service.JadeUserDetailService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;

/**
 * Classe : type_conteneur Description : Classe conteneur des différents champs de l'entête d'un document Date de
 * création: 11 août 04
 * 
 * @author scr
 */
public class CaisseHeaderReportBean {

    private String adresse = "";
    private boolean confidentiel = false;
    private String date = "";
    private String emailCollaborateur = "";
    private String noAffilie = "";
    private String noAvs = "";
    private String nomCollaborateur = "";
    private String noSection = "";
    private boolean recommandee = false;
    private String numeroIDE = "";
    private APTypeDePrestation typePrestation;

    private String telCollaborateur = "";

    private JadeUser user = null;

    /**
     * Constructor for CaisseHeaderReportBean.
     */
    public CaisseHeaderReportBean() {
        super();

    }

    /**
     * Returns the adresse.
     * 
     * @return String
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * Returns the date.
     * 
     * @return String
     */
    public String getDate() {
        return date;
    }

    /**
     * Returns the emailCollaborateur.
     * 
     * @return String
     */
    public String getEmailCollaborateur() {
        return emailCollaborateur;
    }

    /**
     * Returns the noAffilie.
     * 
     * @return String
     */
    public String getNoAffilie() {
        return noAffilie;
    }

    /**
     * Returns the noAvs.
     * 
     * @return String
     */
    public String getNoAvs() {
        return noAvs;
    }

    /**
     * Returns the nomCollaborateur, ou le service si il existe pour ce user un userDetail avec une clé "Service"
     * 
     * @return String
     */
    public String getNomCollaborateur() {
        return nomCollaborateur;
    }

    public String getNoSection() {
        return noSection;
    }

    public String getServiceCollaborateur() {
        JadeUserDetailService uds = JadeAdminServiceLocatorProvider.getLocator().getUserDetailService();
        String service = "";
        try {
            if (user != null) {
                JadeUserDetail serviceDetail = uds.load(user.getIdUser(), "Service");
                service = serviceDetail.getValue();
            }
        } catch (Exception e) {
            JadeCodingUtil.assertNotAccessible(this, "getNomCollaborateur",
                    "l'obtention du user détail pour la clé [Service] et le user [" + nomCollaborateur
                            + "] a provoqué l'erreur suivante : " + e.toString());
        }
        if (!JadeStringUtil.isEmpty(service)) {
            return service;
        }

        return nomCollaborateur;
    }

    /**
     * Returns the telCollaborateur.
     * 
     * @return String
     */
    public String getTelCollaborateur() {
        return telCollaborateur;
    }

    public JadeUser getUser() {
        return user;
    }

    /**
     * @return
     */
    public boolean isConfidentiel() {
        return confidentiel;
    }

    /**
     * @return
     */
    public boolean isRecommandee() {
        return recommandee;
    }

    /**
     * Sets the adresse.
     * 
     * @param adresse
     *            The adresse to set
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * @param b
     */
    public void setConfidentiel(boolean b) {
        confidentiel = b;
    }

    /**
     * Sets the date.
     * 
     * @param date
     *            The date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Sets the emailCollaborateur.
     * 
     * @param emailCollaborateur
     *            The emailCollaborateur to set
     */
    public void setEmailCollaborateur(String emailCollaborateur) {
        this.emailCollaborateur = emailCollaborateur;
    }

    /**
     * Sets the noAffilie.
     * 
     * @param noAffilie
     *            The noAffilie to set
     */
    public void setNoAffilie(String noAffilie) {
        this.noAffilie = noAffilie;
    }

    /**
     * Sets the noAvs.
     * 
     * @param noAvs
     *            The noAvs to set
     */
    public void setNoAvs(String noAvs) {
        this.noAvs = noAvs;
    }

    /**
     * Sets the nomCollaborateur.
     * 
     * @param nomCollaborateur
     *            The nomCollaborateur to set
     */
    public void setNomCollaborateur(String nomCollaborateur) {

        //

        this.nomCollaborateur = nomCollaborateur;
    }

    public void setNoSection(String noSection) {
        this.noSection = noSection;
    }

    /**
     * @param b
     */
    public void setRecommandee(boolean b) {
        recommandee = b;
    }

    /**
     * Sets the telCollaborateur.
     * 
     * @param telCollaborateur
     *            The telCollaborateur to set
     */
    public void setTelCollaborateur(String telCollaborateur) {
        this.telCollaborateur = telCollaborateur;
    }

    public void setUser(JadeUser user) {
        this.user = user;
    }

    public String getNumeroIDE() {
        return numeroIDE;
    }

    public void setNumeroIDE(String numeroIDE) {
        this.numeroIDE = numeroIDE;
    }

    public APTypeDePrestation getTypePrestation() {
        return typePrestation;
    }

    public void setTypePrestation(APTypeDePrestation typePrestation) {
        this.typePrestation = typePrestation;
    }
}
