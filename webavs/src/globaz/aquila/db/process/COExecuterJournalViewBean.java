package globaz.aquila.db.process;

import globaz.aquila.vb.COAbstractViewBeanSupport;
import globaz.framework.bean.FWViewBeanInterface;
import java.rmi.RemoteException;

public class COExecuterJournalViewBean extends COAbstractViewBeanSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String email;
    private String idJournal;

    /**
     * @throws Exception
     */
    public COExecuterJournalViewBean() throws Exception {
        super();
    }

    /**
     * getter pour l'attribut email.
     * 
     * @return la valeur courante de l'attribut email
     */
    public String getEMailAddress() {
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

    public String getIdJournal() {
        return idJournal;
    }

    /**
     * setter pour l'attribut email.
     * 
     * @param email
     *            une nouvelle valeur pour cet attribut
     */
    public void setEMailAddress(String email) {
        this.email = email;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }
}
