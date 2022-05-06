package ch.globaz.eform.process.container;

import ch.globaz.eform.constant.GFStatusEForm;
import ch.globaz.eform.constant.GFTypeEForm;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;
import globaz.commons.nss.NSUtil;
import globaz.eform.helpers.formulaire.GFFormulaireHelper;
import globaz.globall.db.BSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GFFFormulaireReceiveContainer {
    private GFTypeEForm subject;
    private String messageId;
    private String date;
    private GFStatusEForm status;
    private String beneficiaireNss;
    private String beneficiaireNom;
    private String beneficiairePrenom;
    private String beneficiaireDateNaissance;
    private String userGestionnaire;

    private BSession session;


    public GFFFormulaireReceiveContainer(BSession session) {
        this.session = session;
    }

    @Column(name = "COL_EXCEL_TYPE", order = 1)
    @ColumnStyle(align = Align.LEFT)
    public String getType() {
        return subject.getCodeEForm();
    }

    @Column(name = "COL_EXCEL_NOM_FORMULAIRE", order = 2)
    @ColumnStyle(align = Align.LEFT)
    public String getNomFormulaire() {
        return subject.getDesignation(session);
    }

    @Column(name = "COL_EXCEL_ID", order = 3)
    @ColumnStyle(align = Align.LEFT)
    public String getMessageId() {
        return messageId;
    }

    @Column(name = "COL_EXCEL_DATE", order = 4)
    @ColumnStyle(align = Align.RIGHT)
    public String getDate() {
        return date;
    }

    @Column(name = "COL_EXCEL_STATUS", order = 5)
    @ColumnStyle(align = Align.LEFT)
    public String getStatus() {
        return status.getDesignation(session);
    }

    @Column(name = "COL_EXCEL_NOM", order = 6)
    @ColumnStyle(align = Align.LEFT)
    public String getBeneficiaireNom() {
        return beneficiaireNom;
    }

    @Column(name = "COL_EXCEL_PRENOM", order = 7)
    @ColumnStyle(align = Align.LEFT)
    public String getBeneficiairePrenom() {
        return beneficiairePrenom;
    }

    @Column(name = "COL_EXCEL_NSS", order = 8)
    @ColumnStyle(align = Align.LEFT)
    public String getBeneficiaireNss() {
        return NSUtil.formatAVSUnknown(beneficiaireNss);
    }

    @Column(name = "COL_EXCEL_DATE_NAISSANCE", order = 9)
    @ColumnStyle(align = Align.RIGHT)
    public String getBeneficiaireDateNaissance() {
        return beneficiaireDateNaissance;
    }

    @Column(name = "COL_EXCEL_GESTIONNAIRE", order = 10)
    @ColumnStyle(align = Align.LEFT)
    public String getUserGestionnaire() {
        try {
            return GFFormulaireHelper.getGestionnaireDesignation(userGestionnaire);
        } catch (Exception e) {
            LOG.error("erreur dans la récupération de la désignation du gestionnaire", e);
        }
        return userGestionnaire;
    }

    public void setSubject(GFTypeEForm subject) {
        this.subject = subject;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStatus(GFStatusEForm status) {
        this.status = status;
    }

    public void setBeneficiaireNss(String beneficiaireNss) {
        this.beneficiaireNss = beneficiaireNss;
    }

    public void setBeneficiaireNom(String beneficiaireNom) {
        this.beneficiaireNom = beneficiaireNom;
    }

    public void setBeneficiairePrenom(String beneficiairePrenom) {
        this.beneficiairePrenom = beneficiairePrenom;
    }

    public void setBeneficiaireDateNaissance(String beneficiaireDateNaissance) {
        this.beneficiaireDateNaissance = beneficiaireDateNaissance;
    }

    public void setUserGestionnaire(String userGestionnaire) {
        this.userGestionnaire = userGestionnaire;
    }
}
