package globaz.perseus.vb.facture;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.perseus.business.constantes.IPFConstantes;
import ch.globaz.perseus.business.models.qd.CSTypeQD;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFValidationViewBean extends BJadePersistentObjectViewBean implements FWAJAXViewBeanInterface {

    private Map<String, String> type = new HashMap<String, String>();
    private Vector agence = new Vector();
    private String adresseMail;
    private String factureSelected;
    private boolean isPaiementOKPourValidation = true;

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isPaiementOKPourValidation() {
        return isPaiementOKPourValidation;
    }

    public void setPaiementOKPourValidation(boolean isPaiementOKPourValidation) {
        this.isPaiementOKPourValidation = isPaiementOKPourValidation;
    }

    @Override
    public void retrieve() throws Exception {

        setPaiementOKPourValidation(PerseusServiceLocator.getPmtMensuelService().isValidationDecisionAuthorise());

        List<JadeCodeSysteme> codes = JadeBusinessServiceLocator.getCodeSystemeService().getFamilleCodeSysteme(
                "PFTYPEQD");

        for (JadeCodeSysteme code : codes) {
            if (!CSTypeQD.FRAIS_MALADIE.getCodeSystem().equals(code.getIdCodeSysteme())) {
                type.put(code.getIdCodeSysteme(), code.getTraduction(Langues.getLangueDepuisCodeIso(BSessionUtil
                        .getSessionFromThreadContext().getIdLangueISO())));
            }
        }

        List<JadeCodeSysteme> codesCaisse = JadeBusinessServiceLocator.getCodeSystemeService().getFamilleCodeSysteme(
                IPFConstantes.CSGROUP_CAISSE);
        // = PerseusServiceLocator.getTypesSoinsRentePontService().getMapSurTypes(getISession());
        // agence = PFAgenceCommunaleHelper.getListAdministration(IPFConstantes.CS_AGENCE_COMMUNALE);
        agence.add(new String[] { "", "" });
        for (JadeCodeSysteme codeCaisse : codesCaisse) {
            agence.add(new String[] {
                    codeCaisse.getIdCodeSysteme(),
                    codeCaisse.getTraduction(Langues.getLangueDepuisCodeIso(BSessionUtil.getSessionFromThreadContext()
                            .getIdLangueISO())) });
        }
    }

    public Vector getAgences() {
        return agence;
    }

    public Map<String, String> getType() {
        return type;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    public String getAdresseMail() {
        return adresseMail;
    }

    public void setFactureSelected(String factureSelected) {
        this.factureSelected = factureSelected;
    }

    public String getFactureSelected() {
        return factureSelected;
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return null;
    }

    @Override
    public boolean hasList() {
        return false;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public void setGetListe(boolean getListe) {

    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {

    }
}
