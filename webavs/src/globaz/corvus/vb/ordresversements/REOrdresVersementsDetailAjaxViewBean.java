package globaz.corvus.vb.ordresversements;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;

public class REOrdresVersementsDetailAjaxViewBean implements IREOrdreVersementAjaxViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codePrestation;
    private Collection<JadeCodeSysteme> codesSystemeTypeOrdreVersement;
    private String csTypeOrdreVersement;
    private String dateDebutDroit;
    private String dateFinDroit;
    private Long idOrdreVersement;
    private boolean isCompensationInterDecision;
    private boolean isCompense;
    private String message;
    private BigDecimal montantCompense;
    private BigDecimal montantDette;
    private String msgType;
    private String nom;
    private String prenom;
    private BSession session;

    public REOrdresVersementsDetailAjaxViewBean() {
        super();

        codePrestation = null;
        codesSystemeTypeOrdreVersement = new ArrayList<JadeCodeSysteme>();
        csTypeOrdreVersement = null;
        dateDebutDroit = null;
        dateFinDroit = null;
        idOrdreVersement = null;
        isCompensationInterDecision = false;
        isCompense = false;
        message = null;
        montantCompense = null;
        montantDette = null;
        msgType = null;
        nom = null;
        prenom = null;
        session = null;
    }

    private String getCodePrestation() {
        return codePrestation;
    }

    public Collection<JadeCodeSysteme> getCodesSystemeTypeOrdreVersement() {
        return codesSystemeTypeOrdreVersement;
    }

    private String getCsTypeOrdreVersement() {
        return csTypeOrdreVersement;
    }

    private String getDateDebutDroit() {
        return dateDebutDroit;
    }

    private String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getDetailOrdreVersement() {
        StringBuilder detail = new StringBuilder();

        String typeOrdreVersement = "";
        for (JadeCodeSysteme codeSysteme : codesSystemeTypeOrdreVersement) {
            if (codeSysteme.getIdCodeSysteme().equals(getCsTypeOrdreVersement())) {
                typeOrdreVersement = codeSysteme
                        .getTraduction(Langues.getLangueDepuisCodeIso(session.getIdLangueISO()));
            }
        }

        detail.append(typeOrdreVersement).append(" ( ");
        detail.append(getNom()).append(" ").append(getPrenom());
        detail.append(" / ");
        if (!JadeStringUtil.isBlankOrZero(getCodePrestation())) {
            detail.append(getCodePrestation()).append(" / ");
            detail.append(getDateDebutDroit()).append(" - ").append(getDateFinDroit());
        } else {
            detail.append(BSessionUtil.getSessionFromThreadContext().getLabel("MENU_OPTION_SAISIE_MANUELLE"));
        }
        detail.append(" ) ");

        return detail.toString();
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

    public BigDecimal getMontantDette() {
        return montantDette;
    }

    @Override
    public String getMsgType() {
        return msgType;
    }

    private String getNom() {
        return nom;
    }

    private String getPrenom() {
        return prenom;
    }

    @Override
    public String getProvenance() {
        return "OV";
    }

    public BSession getSession() {
        return session;
    }

    @Override
    public boolean hasList() {
        return false;
    }

    public boolean isCompensationInterDecision() {
        return isCompensationInterDecision;
    }

    public boolean isCompense() {
        return isCompense;
    }

    @Override
    public Iterator<?> iterator() {
        return null;
    }

    public void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    public void setCodesSystemeTypeOrdreVersement(Collection<JadeCodeSysteme> codesSystemeTypeOrdreVersement) {
        this.codesSystemeTypeOrdreVersement = codesSystemeTypeOrdreVersement;
    }

    public void setCompensationInterDecision(boolean isCompensationInterDecision) {
        this.isCompensationInterDecision = isCompensationInterDecision;
    }

    public void setCompense(boolean isCompense) {
        this.isCompense = isCompense;
    }

    public void setCsTypeOrdreVersement(String csTypeOrdreVersement) {
        this.csTypeOrdreVersement = csTypeOrdreVersement;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    @Override
    public void setGetListe(boolean getListe) {
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
        this.montantCompense = new BigDecimal(montantCompense.replace("'", ""));
    }

    public void setMontantDette(String montantDette) {
        this.montantDette = new BigDecimal(montantDette.replace("'", ""));
    }

    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setSession(BSession session) {
        this.session = session;
    }
}
