package ch.globaz.vulpecula.ws.bean;

import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersUserCode;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;

/**
 * 
 * @since WebBMS 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class QualificationEbu {
    private Qualification qualification;
    private String code;
    private String libelle;
    private String libelleDe;
    private String libelleIt;
    private boolean conventionCollective;

    public QualificationEbu() {
        // Constructeur par defaut obligatoire pour le bon fonctionnement du framework
    }

    public QualificationEbu(BSession session, Qualification qualification, boolean conventionCollective) {
        this.qualification = qualification;
        this.conventionCollective = conventionCollective;
        code = session.getCode(qualification.getValue());
        libelle = returnCodeForLangue(session, qualification.getValue(), "F").getLibelle();
        libelleDe = returnCodeForLangue(session, qualification.getValue(), "D").getLibelle();
        libelleIt = returnCodeForLangue(session, qualification.getValue(), "I").getLibelle();
    }

    private FWParametersUserCode returnCodeForLangue(BSession sesssion, String idCode, String langue) {

        FWParametersUserCode userCode = new FWParametersUserCode();
        userCode.setSession(sesssion);
        userCode.setIdCodeSysteme(idCode);
        userCode.setIdLangue(langue);
        try {
            userCode.retrieve();
            if (!userCode.isNew()) {
                return userCode;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }

}
