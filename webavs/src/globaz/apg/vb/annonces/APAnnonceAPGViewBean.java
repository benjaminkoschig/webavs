package globaz.apg.vb.annonces;

import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.db.annonces.APAnnonceAPG;
import globaz.apg.db.annonces.APAnnonceAPGManager;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.framework.bean.FWViewBeanInterface;

/**
 * @author DVH
 */
public class APAnnonceAPGViewBean extends APAnnonceAPG implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idTiers;
    private String idDroit;
    private String idPrestation;
    private String typePrestation;

    public APAnnonceAPGViewBean() {
        super();

        idTiers = "";
    }

    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        super._readProperties(statement);
        idDroit = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
    }

    public String getIdTiers() {
        return idTiers;
    }

    /**
     * Si une annonce enfant dans l'état "Errone" existe pour une annonce en état "Envoyé", interdire le bouton
     * "Modifier". Dans les autres cas on peut modifier
     * 
     * @return
     */
    public boolean isModifiable() {

        if (IAPAnnonce.CS_OUVERT.equals(getEtat()) || IAPAnnonce.CS_VALIDE.equals(getEtat())
                || IAPAnnonce.CS_ERRONE.equals(getEtat())) {

            return true;

        } else if (IAPAnnonce.CS_ENVOYE.equals(getEtat())) {

            APAnnonceAPGManager aManager = new APAnnonceAPGManager();
            aManager.setSession(getSession());
            aManager.setForIdParent(getIdAnnonce());
            aManager.setForEtat(IAPAnnonce.CS_ERRONE);
            try {
                return aManager.getCount() == 0;

            } catch (Exception e) {
                // on ne fait rien
            }
        }
        return false;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public final String getIdDroit() {
        return idDroit;
    }

    public final void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public final String getTypePrestation() {
        return typePrestation;
    }

    public final void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }

    public final String getIdPrestation() {
        return idPrestation;
    }

    public final void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

}
