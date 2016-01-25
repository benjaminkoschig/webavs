package globaz.corvus.vb.ordresversements;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import ch.globaz.corvus.domaine.Decision;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.corvus.domaine.SoldePourRestitution;
import ch.globaz.corvus.domaine.constantes.TypeSoldePourRestitution;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;

public class REGestionRestitutionAjaxViewBean implements FWViewBeanInterface, IREOrdreVersementAjaxViewBean,
        FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Collection<JadeCodeSysteme> codesSystemeTypeSoldePourRestitution;
    private Decision decision;
    private String message;
    private String msgType;
    private BSession session;
    private SoldePourRestitution soldePourRestitution;

    public REGestionRestitutionAjaxViewBean() {
        super();

        decision = new Decision();
        codesSystemeTypeSoldePourRestitution = new ArrayList<JadeCodeSysteme>();
        message = "";
        msgType = FWViewBeanInterface.OK;
        session = null;
        soldePourRestitution = new SoldePourRestitution();
    }

    public String getBeneficiairePrestationAccordeePrincipale() {
        RenteAccordee renteAccordee = decision.getRenteAccordeePrincipale();
        StringBuilder detailBeneficiaire = new StringBuilder();
        detailBeneficiaire.append(renteAccordee.getBeneficiaire().getNom()).append(" ")
                .append(renteAccordee.getBeneficiaire().getPrenom());
        return detailBeneficiaire.toString();
    }

    public Collection<JadeCodeSysteme> getCodesSystemeTypeSoldePourRestitution() {
        return codesSystemeTypeSoldePourRestitution;
    }

    public Integer getCodeSystemeRestitution() {
        return TypeSoldePourRestitution.RESTITUTION.getCodeSysteme();
    }

    public Integer getCodeSystemeRetenueMensuelle() {
        return TypeSoldePourRestitution.RETENUES.getCodeSysteme();
    }

    public String getCsTypeSoldePourRestitution() {
        return soldePourRestitution.getType().getCodeSysteme().toString();
    }

    public Decision getDecision() {
        return decision;
    }

    public Long getIdDecision() {
        return decision.getId();
    }

    public Long getIdSoldePourRestitution() {
        return soldePourRestitution.getId();
    }

    @Override
    public BISession getISession() {
        return session;
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return null;
    }

    public Map<String, String> getMapCodesSystemeTypeSoldePourRestitution() {
        Langues langueUtilisateur = Langues.getLangueDepuisCodeIso(session.getIdLangueISO());
        Map<String, String> codesSystemesAvecTraduction = new HashMap<String, String>();

        for (JadeCodeSysteme unCodeSysteme : codesSystemeTypeSoldePourRestitution) {
            codesSystemesAvecTraduction.put(unCodeSysteme.getIdCodeSysteme(),
                    unCodeSysteme.getTraduction(langueUtilisateur));
        }
        return codesSystemesAvecTraduction;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public BigDecimal getMontantARecouvrer() {
        return soldePourRestitution.getMontantRestitution();
    }

    public BigDecimal getMontantPrestationAccordeePrincipale() {
        return decision.getRenteAccordeePrincipale().getMontant();
    }

    public BigDecimal getMontantRetenueMensuelle() {
        return soldePourRestitution.getMontantRetenueMensuelle();
    }

    @Override
    public String getMsgType() {
        return msgType;
    }

    @Override
    public String getProvenance() {
        return "Restitution";
    }

    public SoldePourRestitution getSoldePourRestitution() {
        return soldePourRestitution;
    }

    public TypeSoldePourRestitution getTypeSoldePourRestitution() {
        return soldePourRestitution.getType();
    }

    @Override
    public boolean hasList() {
        return false;
    }

    @Override
    public Iterator<?> iterator() {
        return null;
    }

    public void setCodesSystemeTypeSoldePourRestitution(Collection<JadeCodeSysteme> codesSystemeTypeSoldePourRestitution) {
        this.codesSystemeTypeSoldePourRestitution = codesSystemeTypeSoldePourRestitution;
    }

    public void setCsTypeSoldePourRestitution(String csTypeSoldePourRestitution) {
        soldePourRestitution.setType(TypeSoldePourRestitution.parse(csTypeSoldePourRestitution));
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    @Override
    public void setGetListe(boolean getListe) {
    }

    public void setIdDecision(String idDecision) {
        if (JadeStringUtil.isDigit(idDecision)) {
            decision.setId(Long.parseLong(idDecision));
        }
    }

    public void setIdSoldePourRestitution(String idSoldePourRestitution) {
        if (JadeStringUtil.isDigit(idSoldePourRestitution)) {
            soldePourRestitution.setId(Long.parseLong(idSoldePourRestitution));
        }
    }

    @Override
    public void setISession(BISession newSession) {
        session = (BSession) newSession;
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public void setMontantRetenueMensuelle(String montantRetenueMensuelle) {
        soldePourRestitution.setMontantRetenueMensuelle(new BigDecimal(montantRetenueMensuelle.replaceAll("'", "")));
    }

    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setSoldePourRestitution(SoldePourRestitution soldePourRestitution) {
        this.soldePourRestitution = soldePourRestitution;
    }
}
