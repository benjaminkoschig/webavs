package globaz.vulpecula.vb.is;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.vulpecula.business.services.travailleur.TravailleurServiceCRUD;

public class PTPrimeNaissanceAFViewBean extends BJadeSearchObjectELViewBean {
    private String email;
    private String nomEnfant;
    private String dateNaissance;
    private String idTravailleur;
    private String descriptionTravailleur;

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public String getEmail() {
        if (JadeStringUtil.isEmpty(email)) {
            return JadeThread.currentUserEmail();
        } else {
            return email;
        }
    }

    public final void setEmail(String email) {
        this.email = email;
    }

    public final String getNomEnfant() {
        return nomEnfant;
    }

    public final void setNomEnfant(String nomEnfant) {
        this.nomEnfant = nomEnfant;
    }

    public final String getDateNaissance() {
        return dateNaissance;
    }

    public final void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public final String getIdTravailleur() {
        return idTravailleur;
    }

    public final void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    public final String getDescriptionTravailleur() {
        return descriptionTravailleur;
    }

    public final void setDescriptionTravailleur(String descriptionTravailleur) {
        this.descriptionTravailleur = descriptionTravailleur;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String arg0) {
    }

    public String getTravailleurServiceCRUD() {
        return TravailleurServiceCRUD.class.getName();
    }

    public String getMessageTousChampsRemplis() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("VALIDATION_TOUS_CHAMPS_REMPLIES");
    }

}
