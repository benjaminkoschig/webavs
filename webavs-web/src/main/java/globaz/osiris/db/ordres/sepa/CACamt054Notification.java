package globaz.osiris.db.ordres.sepa;

import java.util.ArrayList;
import java.util.List;

/**
 * Groupe de Pojo reprenant les infos supplémentaire à persister depuis le camt054, il sert de regroupement des
 * transactions pas B-level et adherent et nécessitant un nouveau journal
 * 
 * @author cel
 * 
 */
public class CACamt054Notification {
    private String msgId;
    private String creDtTm;
    private String ntfctnId;
    private String file;
    private String identification;

    private List<CACamt054GroupTransaction> listGroupTxs;

    public CACamt054Notification() {
        listGroupTxs = new ArrayList<CACamt054GroupTransaction>();
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getCreDtTm() {
        return creDtTm;
    }

    public void setCreDtTm(String creDtTm) {
        this.creDtTm = creDtTm;
    }

    public String getNtfctnId() {
        return ntfctnId;
    }

    public void setNtfctnId(String ntfctnId) {
        this.ntfctnId = ntfctnId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public List<CACamt054GroupTransaction> getListGroupTxs() {
        return listGroupTxs;
    }

    public void setListGroupTxs(List<CACamt054GroupTransaction> listGroupTxs) {
        this.listGroupTxs = listGroupTxs;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }
}
