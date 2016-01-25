/**
 * 
 */
package globaz.perseus.vb.lot;

import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.perseus.helpers.lot.PFImprimerDecisionFactureHelper;
import globaz.perseus.utils.PFUserHelper;
import globaz.prestation.utils.ged.PRGedUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.web.application.PFApplication;

/**
 * @author MBO
 * 
 */
public class PFImprimerDecisionFactureViewBean extends BJadePersistentObjectViewBean {

    private String adrMail = "";
    private String agencesSelectionne = null;
    private String caisse = null;
    private String dateDocument = "";
    private String idLot = null;
    private String isSendToGed = null;
    private Map<String, String> listeAgence = null;

    private Lot lot = null;

    @Override
    public void add() throws Exception {

    }

    @Override
    public void delete() throws Exception {

    }

    public String getAdrMail() {
        return adrMail;
    }

    public List<String> getAgencesSelectionne() {
        List<String> listeDAgence = new ArrayList<String>();
        listeDAgence = Arrays.asList(agencesSelectionne.split(";"));

        return listeDAgence;
    }

    public String getCaisse() {
        return caisse;
    }

    // Methode permettant d'obtenir la date du jour pour le document
    public String getDateDocument() {
        if (JadeStringUtil.isEmpty(dateDocument)) {
            dateDocument = getDateDocumentDefault();
        }
        return dateDocument;
    }

    // Methode qui permet d'obtenir tous les jeudi de chaque semaine, en fonction de la date du jour.
    public String getDateDocumentDefault() {
        Calendar c = Calendar.getInstance();
        if (Calendar.THURSDAY != c.get(Calendar.DAY_OF_WEEK)) {
            while (Calendar.THURSDAY != c.get(Calendar.DAY_OF_WEEK)) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        return JadeDateUtil.getGlobazFormattedDate(c.getTime());
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIsSendToGed() {
        return isSendToGed;
    }

    public Map<String, String> getListeAgence() {
        return listeAgence;
    }

    public Lot getLot() {
        return lot;
    }

    // TODO Récupération de la session user
    protected BSession getSession() {
        return BSessionUtil.getSessionFromThreadContext();
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public void init() {
        try {
            setLot(new Lot());
            getLot().setId(idLot);
            setLot((Lot) JadePersistenceManager.read(getLot()));
            getDateDocumentDefault();

            listeAgence = PFUserHelper.getUsermap(PFApplication.DEFAULT_APPLICATION_PERSEUS,
                    PFApplication.PROPERTY_GROUPE_PERSEUS_AGENCE);

        } catch (Exception e) {
            JadeThread.logError(PFImprimerDecisionFactureHelper.class.getName(),
                    "perseus.vb.lot.pfimprimerdecisionfactureviewbean" + e.getMessage());
        }
    }

    public boolean isSendToGed(String csCaisse) {
        if (null == csCaisse) {
            return false;
        } else {
            return PRGedUtils.isDocumentInGed(IPRConstantesExternes.PCF_FACTURE_IMPRIMER_DECISION_FACTURE, csCaisse,
                    getSession());
        }
    }

    @Override
    public void retrieve() throws Exception {
        init();

    }

    public void setAdrMail(String adrMail) {
        this.adrMail = adrMail;
    }

    public void setAgencesSelectionne(String agencesSelectionne) {
        this.agencesSelectionne = agencesSelectionne;
    }

    public void setCaisse(String caisse) {
        this.caisse = caisse;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    @Override
    public void setId(String newId) {

    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIsSendToGed(String isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    @Override
    public void update() throws Exception {

    }

}
