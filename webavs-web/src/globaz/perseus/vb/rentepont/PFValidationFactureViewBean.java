package globaz.perseus.vb.rentepont;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.perseus.utils.PFAgenceCommunaleHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.perseus.business.constantes.IPFConstantes;
import ch.globaz.perseus.business.models.qd.CSTypeQD;

public class PFValidationFactureViewBean extends BJadePersistentObjectViewBean {

    private Map<String, String> type = new HashMap<String, String>();
    private Vector agence = new Vector();

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

    @Override
    public void retrieve() throws Exception {

        List<JadeCodeSysteme> codes = JadeBusinessServiceLocator.getCodeSystemeService().getFamilleCodeSysteme(
                "PFTYPEQD");

        for (JadeCodeSysteme code : codes) {
            if (!CSTypeQD.FRAIS_MALADIE.getCodeSystem().equals(code.getIdCodeSysteme())) {
                type.put(code.getIdCodeSysteme(), code.getTraduction(Langues.getLangueDepuisCodeIso(BSessionUtil
                        .getSessionFromThreadContext().getIdLangueISO())));
            }
        }
        // = PerseusServiceLocator.getTypesSoinsRentePontService().getMapSurTypes(getISession());
        agence = PFAgenceCommunaleHelper.getListAdministration(IPFConstantes.CS_AGENCE_COMMUNALE);
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

}
