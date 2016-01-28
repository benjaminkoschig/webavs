package globaz.pavo.db.inscriptions;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.parameters.FWParametersCode;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.pavo.print.list.CIJournal_Doc;
import java.util.Collection;
import java.util.Vector;

/**
 * Insérez la description du type ici. Date de création : (21.01.2003 08:24:32)
 * 
 * @author: David Girardin
 */
public class CIJournalImprimerViewBean extends CIJournal_Doc implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String mailAddress;

    public CIJournalImprimerViewBean() throws Exception {

    }

    public CIJournalImprimerViewBean(globaz.globall.db.BSession session) throws Exception {
        super(session);
    }

    public Vector getChampsOrderedAsCodeSystem(String type, String group, Collection exceptValue, boolean wantBlank) {

        boolean isExceptValue = true;
        Vector vList = new Vector();
        String[] list = new String[2];

        if (exceptValue == null || exceptValue.size() == 0) {
            isExceptValue = false;
        }

        try {
            FWParametersSystemCodeManager manager = new FWParametersSystemCodeManager();

            manager.setForIdTypeCode(type);
            manager.setForIdGroupe(group);
            manager.setSession(getSession());
            manager.find(BManager.SIZE_NOLIMIT);
            vList = new Vector(manager.size());

            // ajout d'une ligne vide
            if (wantBlank) {
                list = new String[2];
                list[0] = "";
                list[1] = "";
                vList.add(list);
            }

            for (int i = 0; i < manager.size(); i++) {
                list = new String[2];
                FWParametersSystemCode entity = (FWParametersSystemCode) manager.getEntity(i);

                String code = entity.getCurrentCodeUtilisateur().getIdCodeSysteme();

                // Passe au code suivant, si le code se trouve dans la liste des
                // valeurs à bypasser
                if (isExceptValue && exceptValue.contains(code)) {
                    continue;
                }

                list[0] = code;
                list[1] = entity.getCurrentCodeUtilisateur().getLibelle();
                vList.add(list);
            }
        } catch (Exception e) {
            // si probleme, retourne list vide.
        }

        // Sort the elements (bubble sort)
        String first = "", second = "";
        for (int i = 0; i < vList.size(); i++) {
            first = ((String[]) vList.elementAt(i))[1];
            for (int j = i + 1; j < vList.size(); j++) {
                second = ((String[]) vList.elementAt(j))[1];
                if (second.toUpperCase().trim().compareTo(first.toUpperCase().trim()) < 1) {
                    Object o2 = vList.elementAt(j);
                    vList.remove(o2);
                    vList.add(i, o2);
                    first = ((String[]) vList.elementAt(i))[1];
                }
            }
        }
        return vList;
    }

    /**
     * Returns the mailAddress.
     * 
     * @return String
     */
    @Override
    public String getMailAddress() {
        return mailAddress;
    }

    public String getTypeJournauxAsString() {
        FWParametersSystemCodeManager csTypeJournalManager = new FWParametersSystemCodeManager();
        csTypeJournalManager.setForIdTypeCode("10300001");
        csTypeJournalManager.setForIdGroupe("CITYPINS");
        csTypeJournalManager.setSession(getSession());
        csTypeJournalManager.changeManagerSize(BManager.SIZE_NOLIMIT);
        try {
            csTypeJournalManager.find(getTransaction());
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < csTypeJournalManager.size(); i++) {
            result.append("<option value='");
            result.append(((FWParametersCode) csTypeJournalManager.getEntity(i)).getIdCode());
            result.append("'>");
            result.append(((FWParametersCode) csTypeJournalManager.getEntity(i)).getCodeUtilisateur(
                    getSession().getIdLangue()).getLibelle());
            result.append("</option>");

        }
        return result.toString();

    }

    /**
     * Sets the mailAddress.
     * 
     * @param mailAddress
     *            The mailAddress to set
     */
    @Override
    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

}
