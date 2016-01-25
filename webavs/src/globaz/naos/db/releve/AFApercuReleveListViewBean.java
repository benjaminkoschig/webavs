/*
 * Créé le 18 avr. 05
 */
package globaz.naos.db.releve;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Le listViewBean de l'entité Relevé.
 * 
 * @author jts, sau 18 avr. 05 14:23:03
 */
public class AFApercuReleveListViewBean extends AFApercuReleveManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getAffilieNumero(int index) {
        return ((AFApercuReleve) getEntity(index)).getAffilieNumero();
    }

    public String getCollaborateur(int index) {
        return ((AFApercuReleve) getEntity(index)).getCollaborateur();
    }

    /*
     * public String getIdEnteteFacture(int index) { return ((AFApercuReleve)getEntity(index)).getIdEnteteFacture(); }
     */
    public String getDateDebut(int index) {
        return ((AFApercuReleve) getEntity(index)).getDateDebut();
    }

    public String getDateFin(int index) {
        return ((AFApercuReleve) getEntity(index)).getDateFin();
    }

    public String getEtat(int index) {
        return ((AFApercuReleve) getEntity(index)).getEtat();
    }

    public String getIdExterneFacture(int index) {
        return ((AFApercuReleve) getEntity(index)).getIdExterneFacture();
    }

    public String getIdReleve(int index) {
        return ((AFApercuReleve) getEntity(index)).getIdReleve();
    }
}
