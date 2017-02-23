package globaz.osiris.db.ordres.sepa;

import globaz.framework.util.FWCurrency;
import java.util.ArrayList;
import java.util.List;

/**
 * Groupe de Pojo reprenant les infos supplémentaire à persister depuis le camt054, il sert de regroupement des
 * transactions pas B-level et adherent et nécessitant un nouveau journal
 * 
 * @author cel
 * 
 */
public class CACamt054GroupTransaction {
    private FWCurrency ctrlAmount;
    private String crdtDbtIndicator;
    private int nbTransactions;
    private String noAdherent;
    private String status;
    private String domainCode;
    private String familyCode;
    private String subFamilyCode;

    private List<CACamt054Transaction> listTransactions;

    public CACamt054GroupTransaction() {
        listTransactions = new ArrayList<CACamt054Transaction>();
    }

    public FWCurrency getCtrlAmount() {
        return ctrlAmount;
    }

    public void setCtrlAmount(FWCurrency ctrlAmount) {
        this.ctrlAmount = ctrlAmount;
    }

    public List<CACamt054Transaction> getListTransactions() {
        return listTransactions;
    }

    public void setListTransactions(List<CACamt054Transaction> listTransactions) {
        this.listTransactions = listTransactions;
    }

    public int getNbTransactions() {
        return nbTransactions;
    }

    public void setNbTransactions(int nbTransactions) {
        this.nbTransactions = nbTransactions;
    }

    public String getNoAdherent() {
        return noAdherent;
    }

    public void setNoAdherent(String noAdherent) {
        this.noAdherent = noAdherent;
    }

    public void setCrdtDbtIndicator(String crdtDbtIndicator) {
        this.crdtDbtIndicator = crdtDbtIndicator;
    }

    public String getCrdtDbtIndicator() {
        return crdtDbtIndicator;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setSubFamilyCode(String subFamilyCode) {
        this.subFamilyCode = subFamilyCode;
    }

    public void setFamilyCode(String familyCode) {
        this.familyCode = familyCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public String getSubFamilyCode() {
        return subFamilyCode;
    }

    public String getFamilyCode() {
        return familyCode;
    }

    public String getDomainCode() {
        return domainCode;
    }

}
