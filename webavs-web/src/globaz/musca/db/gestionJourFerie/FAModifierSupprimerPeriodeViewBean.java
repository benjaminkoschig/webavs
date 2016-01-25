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
public class FAModifierSupprimerPeriodeViewBean implements BIPersistentObject, FWViewBeanInterface {

    /**
     * Constantes
     */
    public static final String OPERATION_MODIFIER = "Modifier";
    public static final String OPERATION_SUPPRIMER = "Supprimer";

    private ArrayList<String> domaineFerie = new ArrayList<String>();
    private String email = "";
    private String libelle = "";
    private ArrayList<FAGestionJourFerie> listFerieAModifierSupprimer = new ArrayList<FAGestionJourFerie>();

    private String listIdFerieAModifierSupprimer = "";
    private String message = "";
    private String messageType = FWViewBeanInterface.OK;
    private String operationARealiser = "";
    /**
     * Attributs
     */
    private BISession session;

    /**
     * Constructeur
     */
    public FAModifierSupprimerPeriodeViewBean() throws Exception {
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public void fillListFerieAModifierSupprimer() throws Exception {
        if (!JadeStringUtil.isBlankOrZero(getListIdFerieAModifierSupprimer())) {
            FAGestionJourFerieManager mgrJourFerie = new FAGestionJourFerieManager();
            mgrJourFerie.setSession(getSession());
            mgrJourFerie.setInIdJour(getListIdFerieAModifierSupprimer());
            mgrJourFerie.find();

            for (int i = 0; i < mgrJourFerie.size(); i++) {
                listFerieAModifierSupprimer.add((FAGestionJourFerie) mgrJourFerie.getEntity(i));
            }
        }
    }

    /**
     * Méthodes
     */

    public String getContenuForTextareaJourTraite() {
        StringBuffer texte = new StringBuffer();

        for (FAGestionJourFerie entityJourFerie : listFerieAModifierSupprimer) {
            texte.append(entityJourFerie.getDateJour());
            texte.append("\n");
        }

        return texte.toString();
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

    public ArrayList<FAGestionJourFerie> getListFerieAModifierSupprimer() {
        return listFerieAModifierSupprimer;
    }

    /**
     * GETTER
     */

    public String getListIdFerieAModifierSupprimer() {
        return listIdFerieAModifierSupprimer;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMsgType() {
        return messageType;
    }

    public String getOperationARealiser() {
        return operationARealiser;
    }

    public BSession getSession() {
        return (BSession) session;
    }

    @Override
    public void retrieve() throws Exception {
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

    /**
     * SETTER
     */

    public void setListIdFerieAModifierSupprimer(String newListIdFerieAModifierSupprimer) {
        listIdFerieAModifierSupprimer = newListIdFerieAModifierSupprimer;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setMsgType(String msgType) {
        messageType = msgType;
    }

    public void setOperationARealiser(String newOperationARealiser) {
        operationARealiser = newOperationARealiser;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
