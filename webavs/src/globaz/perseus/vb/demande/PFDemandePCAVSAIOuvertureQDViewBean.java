/**
 * 
 */
package globaz.perseus.vb.demande;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.perseus.utils.PFUserHelper;
import java.util.ArrayList;
import java.util.Date;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.situationfamille.Enfant;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author JSI
 * 
 */
public class PFDemandePCAVSAIOuvertureQDViewBean extends BJadePersistentObjectViewBean {

    private Demande demande;

    public PFDemandePCAVSAIOuvertureQDViewBean() {
        super();
        demande = new Demande();
    }

    public PFDemandePCAVSAIOuvertureQDViewBean(Demande demande) {
        super();
        this.demande = demande;
    }

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    public Demande getDemande() {
        return demande;
    }

    public String getDemandeId() {
        return demande.getId();
    }

    public String getDetailEnfants() throws Exception {
        String htmlCode = "";
        ArrayList<Enfant> enfants = (ArrayList<Enfant>) PerseusServiceLocator.getDemandeService().getListEnfants(
                demande);
        for (Enfant enfant : enfants) {
            if (JadeDateUtil.getNbYearsBetween(enfant.getMembreFamille().getPersonneEtendue().getPersonne()
                    .getDateNaissance(), JadeDateUtil.getGlobazFormattedDate(new Date())) < 16) {
                if (!JadeStringUtil.isBlank(htmlCode)) {
                    htmlCode += ", ";
                }
                htmlCode += enfant.getMembreFamille().getPersonneEtendue().getTiers().getDesignation1();
                htmlCode += " " + enfant.getMembreFamille().getPersonneEtendue().getTiers().getDesignation2();
            }
        }
        return htmlCode;
    }

    public String getDetailRequerant() {
        return PFUserHelper.getDetailAssure((BSession) getISession(), demande.getSituationFamiliale().getRequerant()
                .getMembreFamille().getPersonneEtendue());
    }

    @Override
    public String getId() {
        return demande.getId();
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(demande.getSpy());
    }

    @Override
    public void retrieve() throws Exception {
        demande = PerseusServiceLocator.getDemandeService().read(getId());
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    @Override
    public void setId(String idDemande) {
        demande.setId(idDemande);
    }

    @Override
    public void update() throws Exception {
        PerseusServiceLocator.getQDAnnuelleService().createSpecificFraisGarde(demande, "0",
                demande.getSimpleDemande().getDateDebut().substring(6));
    }

}
