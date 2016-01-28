package globaz.corvus.vb.ordresversements;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import ch.globaz.corvus.domaine.Decision;

/**
 * Conteneur de données pour l'écran de création d'une compensation inter-décision
 */
public class RECompensationInterDecisionAjaxViewBean implements IREOrdreVersementAjaxViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Decision decisionPonctionnee;
    private Set<Decision> decisionsPourCompensationInterDecision;
    private Long idCompensationInterDecision;
    private Long idDecisionDeficitaire;
    private Long idDecisionPonctionne;
    private Long idOrdreVersement;
    private String message;
    private BigDecimal montantCompense;
    private String msgType;
    private BSession session;
    private BigDecimal soldeDecisionDeficitaire;

    public RECompensationInterDecisionAjaxViewBean() {
        super();

        decisionPonctionnee = null;
        decisionsPourCompensationInterDecision = new HashSet<Decision>();
        idCompensationInterDecision = null;
        idDecisionDeficitaire = null;
        idDecisionPonctionne = null;
        idOrdreVersement = null;
        message = null;
        montantCompense = BigDecimal.ZERO;
        msgType = null;
        session = null;
        soldeDecisionDeficitaire = BigDecimal.ZERO;
    }

    public Decision getDecisionPonctionnee() {
        return decisionPonctionnee;
    }

    public Set<Decision> getDecisionsPourCompensationInterDecision() {
        return decisionsPourCompensationInterDecision;
    }

    public Long getIdCompensationInterDecision() {
        return idCompensationInterDecision;
    }

    public Long getIdDecisionDeficitaire() {
        return idDecisionDeficitaire;
    }

    public Long getIdDecisionPonctionne() {
        return idDecisionPonctionne;
    }

    public Long getIdOrdreVersement() {
        return idOrdreVersement;
    }

    @Override
    public BISession getISession() {
        return session;
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return null;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public BigDecimal getMontantCompense() {
        return montantCompense;
    }

    public String getMontantDisponible() {
        if (decisionPonctionnee != null) {
            return decisionPonctionnee.getSolde().add(montantCompense).abs().toString();
        } else {
            return "0.0";
        }
    }

    @Override
    public String getMsgType() {
        return msgType;
    }

    @Override
    public String getProvenance() {
        return "CID";
    }

    public BSession getSession() {
        return session;
    }

    public BigDecimal getSoldeDecisionDeficitaire() {
        return soldeDecisionDeficitaire.abs().add(montantCompense.abs()).abs();
    }

    @Override
    public boolean hasList() {
        return false;
    }

    @Override
    public Iterator<?> iterator() {
        return null;
    }

    public void setDecisionPonctionnee(Decision decisionPonctionnee) {
        this.decisionPonctionnee = decisionPonctionnee;
    }

    public void setDecisionsPourCompensationInterDecision(Set<Decision> decisionsPourCompensationInterDecision) {
        this.decisionsPourCompensationInterDecision = decisionsPourCompensationInterDecision;
    }

    @Override
    public void setGetListe(boolean getListe) {
    }

    public void setIdCompensationInterDecision(String idCompensationInterDecision) {
        if (JadeStringUtil.isDigit(idCompensationInterDecision)) {
            this.idCompensationInterDecision = Long.parseLong(idCompensationInterDecision);
        }
    }

    public void setIdDecisionDeficitaire(String idDecisionDeficitaire) {
        if (JadeStringUtil.isDigit(idDecisionDeficitaire)) {
            this.idDecisionDeficitaire = Long.parseLong(idDecisionDeficitaire);
        }
    }

    public void setIdDecisionPonctionne(String idDecisionPonctionne) {
        if (JadeStringUtil.isDigit(idDecisionPonctionne)) {
            this.idDecisionPonctionne = Long.parseLong(idDecisionPonctionne);
        }
    }

    public void setIdOrdreVersement(String idOrdreVersement) {
        this.idOrdreVersement = Long.parseLong(idOrdreVersement);
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

    public void setMontantCompense(String montantCompense) {
        if (JadeStringUtil.isBlankOrZero(montantCompense)) {
            this.montantCompense = BigDecimal.ZERO;
        } else {
            this.montantCompense = new BigDecimal(montantCompense.replace("'", ""));
        }
    }

    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setSoldeDecisionDeficitaire(BigDecimal soldeDecisionDeficitaire) {
        this.soldeDecisionDeficitaire = soldeDecisionDeficitaire;
    }
}
