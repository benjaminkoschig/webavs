package globaz.al.vb.decision;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import ch.globaz.al.utils.ALGestionnaireUtils;
import globaz.fx.user.business.bean.FXModuleUser;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;

public class ALDecisionFileAttenteViewBean extends BJadePersistentObjectViewBean {

    public static final String[] TOUS = { "TOUS", "TOUS" };

    List<FXModuleUser> alUsers;
    private String idGestionnaireSelectionne;
    private String dateImpression = null;
    private boolean insertionGed;
    private String email = null;

    public ALDecisionFileAttenteViewBean() {
        super();
    }

    /**
     * @return the idGestionnaireSelectionne
     */
    public String getIdGestionnaireSelectionne() {
        return idGestionnaireSelectionne;
    }

    /**
     * @param idGestionnaireSelectionne the idGestionnaireSelectionne to set
     */
    public void setUtilisateurSelectionne(String visaUtilisateurSelectionne) {
        idGestionnaireSelectionne = visaUtilisateurSelectionne;
    }

    /**
     * @return the dateImpression
     */
    public String getDateImpression() {
        return dateImpression;
    }

    /**
     * @param dateImpression the dateImpression to set
     */
    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    /**
     * @return the insertionGed
     */
    public boolean isInsertionGed() {
        return insertionGed;
    }

    /**
     * @param insertionGed the insertionGed to set
     */
    public void setInsertionGed(boolean insertionGed) {
        this.insertionGed = insertionGed;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public Vector getAlUsers() {
        Vector<String[]> values = ALGestionnaireUtils.convertForFWSelect(alUsers);
        // Tri des éléments
        Collections.sort(values, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return o1[0].toLowerCase().compareTo(o2[0].toLowerCase());
            }
        });
        values.add(0, TOUS);

        return values;
    }

    public void setAlUsers(List<FXModuleUser> alUsers) {
        this.alUsers = alUsers;
    }


    @Override
    public void add() throws Exception {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public void delete() throws Exception {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public String getId() {
        return "";
    }

    @Override
    public void retrieve() throws Exception {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public void setId(String newId) {
        // throw new RuntimeException("Not implemented !");
    }

    @Override
    public void update() throws Exception {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public BSpy getSpy() {
        return null;
    }
}
