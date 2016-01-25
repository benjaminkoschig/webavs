package globaz.aquila.vb.process;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.vb.COAbstractViewBeanSupport;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import java.rmi.RemoteException;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class COProcessMuterDelaiViewBean extends COAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private COContentieux contentieux;
    private String dateDocument;
    private String email;
    private Boolean paraEcheanceAffiche = Boolean.FALSE;
    private Boolean paraInteretsMoratoiresAffiche = Boolean.FALSE;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut contentieux.
     * 
     * @return la valeur courante de l'attribut contentieux
     */
    public COContentieux getContentieux() {
        return contentieux;
    }

    /**
     * getter pour l'attribut date document.
     * 
     * @return la valeur courante de l'attribut date document
     */
    public String getDateDocument() {
        if (dateDocument == null) {
            dateDocument = JACalendar.todayJJsMMsAAAA();
        }

        return dateDocument;
    }

    /**
     * getter pour l'attribut email.
     * 
     * @return la valeur courante de l'attribut email
     */
    public String getEmail() {
        if (email == null) {
            try {
                email = getISession().getUserEMail();
            } catch (RemoteException e) {
                e.printStackTrace();
                setMessage(e.getMessage());
                setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        return email;
    }

    /**
     * getter pour l'attribut para echeance affiche.
     * 
     * @return la valeur courante de l'attribut para echeance affiche
     */
    public Boolean getParaEcheanceAffiche() {
        return paraEcheanceAffiche;
    }

    /**
     * getter pour l'attribut para interets moratoires affiche.
     * 
     * @return la valeur courante de l'attribut para interets moratoires affiche
     */
    public Boolean getParaInteretsMoratoiresAffiche() {
        return paraInteretsMoratoiresAffiche;
    }

    /**
     * getter pour l'attribut prochaine date declenchement.
     * 
     * @return la valeur courante de l'attribut prochaine date declenchement
     */
    public String getProchaineDateDeclenchement() {
        return contentieux.getProchaineDateDeclenchement();
    }

    /**
     * setter pour l'attribut contentieux.
     * 
     * @param contentieux
     *            une nouvelle valeur pour cet attribut
     */
    public void setContentieux(COContentieux contentieux) {
        this.contentieux = contentieux;
    }

    /**
     * setter pour l'attribut date document.
     * 
     * @param dateDocument
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    /**
     * setter pour l'attribut email.
     * 
     * @param email
     *            une nouvelle valeur pour cet attribut
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * setter pour l'attribut para echeance affiche.
     * 
     * @param paraEcheanceAffiche
     *            une nouvelle valeur pour cet attribut
     */
    public void setParaEcheanceAffiche(Boolean paraEcheanceAffiche) {
        this.paraEcheanceAffiche = paraEcheanceAffiche;
    }

    /**
     * setter pour l'attribut para interets moratoires affiche.
     * 
     * @param paraInteretsMoratoiresAffiche
     *            une nouvelle valeur pour cet attribut
     */
    public void setParaInteretsMoratoiresAffiche(Boolean paraInteretsMoratoiresAffiche) {
        this.paraInteretsMoratoiresAffiche = paraInteretsMoratoiresAffiche;
    }
}
