package globaz.naos.db.ide;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BAccessBean;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BIPersistentObjectList;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.naos.util.IDEDataBean;
import globaz.naos.util.IDEServiceCallUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.codec.binary.Base64;

public class AFIdeSearchListViewBean extends BAccessBean implements FWViewBeanInterface, BIPersistentObjectList,
        FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = -8321241694105461046L;
    private List<IDEDataBean> listIdeDataBean = new ArrayList<IDEDataBean>();
    private String forNumeroIDE;
    private String forNpa;
    private String forLocalite;
    private String forRue;
    private String forNumeroRue;
    private String forRaisonSociale;
    private String forNaissance;
    private String forTypeRecherche = "";
    private String forNumeroAVS;

    private String message = "";
    private String msgType = "";

    @Override
    public void find() throws Exception {
        try {
            if (AFIDEUtil.TYPE_RECHERCHE_NUM_IDE.equalsIgnoreCase(forTypeRecherche)) {
                listIdeDataBean = IDEServiceCallUtil.searchForNumeroIDE(forNumeroIDE, getSession());
            } else if (AFIDEUtil.TYPE_RECHERCHE_RAISON_SOCIALE.equalsIgnoreCase(forTypeRecherche)) {
                listIdeDataBean = IDEServiceCallUtil.search(getForRaisonSociale(), getForNpa(), getForLocalite(),
                        getForRue(), getForNumeroRue(), getForNaissance(), getSession());
            } else if (AFIDEUtil.TYPE_RECHERCHE_AVS.equalsIgnoreCase(forTypeRecherche)) {
                listIdeDataBean = IDEServiceCallUtil.searchForNumeroAVS(getForNumeroAVS(), getSession());
            }
        } catch (Exception e) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(e.getMessage());
        }

    }

    public String getForTypeRecherche() {
        return forTypeRecherche;
    }

    public void setForTypeRecherche(String forTypeRecherche) {
        this.forTypeRecherche = forTypeRecherche;
    }

    @Override
    public void findNext() throws Exception {
        // do nothing

    }

    public void setForNumeroIDE(String forNumeroIDE) {
        this.forNumeroIDE = forNumeroIDE;
    }

    public String getForNumeroIDE() {
        return forNumeroIDE;
    }

    public String getForNpa() {
        return forNpa;
    }

    public void setForNpa(String forNpa) {
        this.forNpa = forNpa;
    }

    public String getForLocalite() {
        return forLocalite;
    }

    public void setForLocalite(String forLocalite) {
        this.forLocalite = forLocalite;
    }

    public String getForRaisonSociale() {
        return forRaisonSociale;
    }

    public String getForRaisonSocialeb64() {
        return new String(Base64.encodeBase64(forRaisonSociale.getBytes()));
    }

    public String getForNaissance() {
        return forNaissance;
    }

    public void setForNaissance(String forNaissance) {
        this.forNaissance = forNaissance;
    }

    public String getForRue() {
        return forRue;
    }

    public void setForRue(String forRue) {
        this.forRue = forRue;
    }

    public String getForNumeroRue() {
        return forNumeroRue;
    }

    public void setForNumeroRue(String forNumeroRue) {
        this.forNumeroRue = forNumeroRue;
    }

    public void setForRaisonSociale(String forRaisonSociale) {
        this.forRaisonSociale = forRaisonSociale;
    }

    public void setForNumeroAVS(String forNumeroAVS) {
        this.forNumeroAVS = forNumeroAVS;
    }

    public String getForNumeroAVS() {
        return forNumeroAVS;
    }

    /**
     * conversion Base64 : Une raison sociale peut contenir des caractères non autorisé en get
     * 
     * @param forRaisonSocialeb64
     */
    public void setForRaisonSocialeb64(String forRaisonSocialeb64) {
        forRaisonSociale = new String(Base64.decodeBase64(forRaisonSocialeb64.getBytes()));
    }

    public final boolean canDoPrev() {
        return false;
    }

    public final boolean canDoNext() {
        return false;
    }

    public final int getCount() {
        return listIdeDataBean.size();
    }

    public String getCodeNogaForIndex(int index) {
        return listIdeDataBean.get(index).getNogaCode();
    }

    public final int getOffset() {
        return 0;
    }

    @Override
    public void findPrev() throws Exception {
        // do nothing

    }

    @Override
    public BIPersistentObject get(int idx) {
        IDEDataBean bean = listIdeDataBean.get(idx);

        AFIdeSearch ideBean = new AFIdeSearch();
        ideBean.setNumeroIDE(bean.getNumeroIDE());
        ideBean.setStatut(bean.getStatut());
        ideBean.setRaisonSociale(bean.getRaisonSociale());

        return ideBean;
    }

    @Override
    public Iterator<IDEDataBean> iterator() {
        return listIdeDataBean.iterator();
    }

    @Override
    public int size() {
        return listIdeDataBean.size();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMsgType() {
        return msgType;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;

    }

    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;

    }

    public String getNaissance(int i) {
        return listIdeDataBean.get(i).getNaissance();
    }

    public String getAdresse(int i) {
        return listIdeDataBean.get(i).getAdresse();
    }

    public String getNumeroIDE(int i) {
        return AFIDEUtil.giveMeNumIdeFormatedWithPrefix(listIdeDataBean.get(i).getNumeroIDE());
    }

    public String getNumeroAVS(int i) {
        return listIdeDataBean.get(i).getNss();
    }

    public String getRaisonSociale(int i) {
        return listIdeDataBean.get(i).getRaisonSociale();
    }

    public String getStatut(int i) throws Exception {
        String codeStatutLibelle = listIdeDataBean.get(i).getStatut();
        if (!JadeStringUtil.isBlank(codeStatutLibelle)) {
            return CodeSystem.getLibelle(getSession(), codeStatutLibelle);
        } else {
            return "";
        }
    }

    @Override
    public int getSize() {
        // TODO Auto-generated method stub
        return 0;
    }

}
