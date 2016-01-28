package globaz.musca.db.gestionJourFerie;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;

/**
 * @author MMO
 * @since 27 octbore 2010
 */
public class FAPeriodeWeekendViewBean implements BIPersistentObject, FWViewBeanInterface {

    private String dateDebut = "";
    private String dateFin = "";
    private ArrayList<String> domaineFerie = new ArrayList<String>();
    private String email = "";

    private String libelle = "";
    private String message = "";
    private String messageType = FWViewBeanInterface.OK;
    private BISession session;
    private String urlFichierAImporter = "";
    private Boolean weekendOnly = new Boolean(false);

    /**
     * Constructeur
     */
    public FAPeriodeWeekendViewBean() throws Exception {
    }

    /**
     * Méthodes
     */
    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * GETTER
     */
    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public ArrayList<String> getDomaineFerie() {
        return domaineFerie;
    }

    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }
        return email;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BISession getISession() {
        return session;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMsgType() {
        return messageType;
    }

    public BSession getSession() {
        return (BSession) session;
    }

    public String getUrlFichierAImporter() {
        return urlFichierAImporter;
    }

    public Boolean getWeekendOnly() {
        return weekendOnly;
    }

    @Override
    public void retrieve() throws Exception {
    }

    /**
     * SETTER
     */
    public void setDateDebut(String newDateDebut) {
        dateDebut = newDateDebut;
    }

    public void setDateFin(String newDateFin) {
        dateFin = newDateFin;
    }

    public void setDomaineFerie(ArrayList<String> newDomaineFerie) {
        domaineFerie = newDomaineFerie;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setISession(BISession newSession) {
        session = newSession;
    }

    public void setLibelle(String newLibelle) {
        libelle = newLibelle;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setMsgType(String msgType) {
        messageType = msgType;
    }

    public void setUrlFichierAImporter(String newUrlFichierAImporter) {
        urlFichierAImporter = newUrlFichierAImporter;
    }

    public void setWeekendOnly(Boolean newWeekendOnly) {
        weekendOnly = newWeekendOnly;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
