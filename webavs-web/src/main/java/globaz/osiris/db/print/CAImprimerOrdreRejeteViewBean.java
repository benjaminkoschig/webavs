package globaz.osiris.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.osiris.db.ordres.sepa.CAListOrdreRejeteProcess;

public class CAImprimerOrdreRejeteViewBean extends CAListOrdreRejeteProcess implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idOrdreGroupe = new String();

    /**
     * Sets the idOrdreGroupe.
     * 
     * @param idOrdreGroupe
     *            The idOrdreGroupe to set
     */
    @Override
    public void setIdOrdreGroupe(String idOrdreGroupe) {
        this.idOrdreGroupe = idOrdreGroupe;
        super.setIdOrdreGroupe(idOrdreGroupe);

    }

    /**
     * Returns the idOrdreGroupe.
     * 
     * @return String
     */
    public String getIdOrdreGroupe() {
        return idOrdreGroupe;
    }

    public String getEmailAdress() {
        return super.getEmail();
    }

    public void setEmailAdress(String emailAdress) {
        super.setEmail(emailAdress);
    }
}
