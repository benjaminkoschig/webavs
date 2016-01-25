package globaz.pavo.db.bta;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import globaz.pavo.vb.CIAbstractPersistentViewBean;

public class CIDecisionBtaViewBean extends CIAbstractPersistentViewBean implements FWViewBeanInterface {
    private String annee = JACalendar.todayJJsMMsAAAA().substring(6);

    public CIDecisionBtaViewBean() throws Exception {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public String getAnnee() {
        return annee;
    }

    public CIDossierBta getDossierBta() throws Exception {
        CIDossierBta dossierBta = new CIDossierBta();
        dossierBta.setSession(getSession());
        dossierBta.setIdDossierBta(getId());
        dossierBta.retrieve();

        return dossierBta;
    }

    public boolean isDossierOuvert() {
        boolean dossierOuvert = true;
        try {
            CIDossierBta dossierBta = getDossierBta();
            if (!dossierBta.getEtatDossier().equals(CIDossierBta.CS_ETAT_OUVERT)) {
                dossierOuvert = false;
            } else {
                dossierOuvert = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dossierOuvert;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }
}
