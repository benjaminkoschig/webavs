/**
 * class CPListeDecisionsAvecMiseEnCompteViewBean écrit le 19/01/05 par JPA
 * 
 * class ViewBean pour les décisions avec mise en compte
 * 
 * @author JPA
 **/
package globaz.phenix.db.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BProcess;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.process.listes.CPProcessListeMontantsCotisationsDifferents;

public class CPListeMontantsCotisationsDifferentsViewBean extends CPProcessListeMontantsCotisationsDifferents implements
        BIPersistentObject, FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Déclarations
    private String annee = "";
    private int annee2;
    private String id = null;
    private int mois;
    private String trimestre = "";

    public CPListeMontantsCotisationsDifferentsViewBean() throws Exception {
        super();
        // Calcul du trimestre
        JADate date = JACalendar.today();
        annee2 = date.getYear();
        mois = date.getMonth();
        setAnnee(String.valueOf(annee2));
        if ((mois >= 1) && (mois <= 3)) {
            setTrimestre("31-03-" + annee);
        }
        if ((mois >= 4) && (mois <= 6)) {
            setTrimestre("30-06-" + annee);
        }
        if ((mois >= 7) && (mois <= 9)) {
            setTrimestre("30-09-" + annee);
        }
        if ((mois >= 10) && (mois <= 12)) {
            setTrimestre("31-12-" + annee);
        }
    }

    // Constructeurs
    public CPListeMontantsCotisationsDifferentsViewBean(BProcess parent) throws Exception {
        super(parent);
    }

    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getSession().getLabel("CP_MSG_0145"));
        }
    }

    // Méthodes BIPersistentObject
    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    // Getter
    public String getAnnee() {
        return annee;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getTrimestre() {
        return trimestre;
    }

    @Override
    public void retrieve() throws Exception {
    }

    // Setter
    public void setAnnee(String string) {
        annee = string;
    }

    @Override
    public void setId(String newId) {
        id = newId;
    }

    public void setTrimestre(String string) {
        trimestre = string;
    }

    @Override
    public void update() throws Exception {
    }
}
