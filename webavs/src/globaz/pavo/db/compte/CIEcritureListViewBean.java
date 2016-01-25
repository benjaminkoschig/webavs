package globaz.pavo.db.compte;

/**
 * Insérez la description du type ici. Date de création : (12.11.2002 17:03:24)
 * 
 * @author: Administrator
 */
public class CIEcritureListViewBean extends CIEcritureManager implements globaz.framework.bean.FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur CIEcritureListViewBean.
     */
    public CIEcritureListViewBean() {
        super();
        // on active le control pour la sécurité CI par rapport à une
        // affiliation
        setCacherEcritureProtege(1);// 1=true, 0=false
    }

    public String getAnnee(int pos) {
        CIEcriture entity = (CIEcriture) getEntity(pos);
        return entity.getAnnee();
    }

    public String getBrancheEconomique(int pos) {
        CIEcriture entity = (CIEcriture) getEntity(pos);
        return entity.getBrancheEconomique();
    }

    public String getCaisseChomage(int pos) {
        CIEcriture entity = (CIEcriture) getEntity(pos);
        return entity.getCaisseChomage();
    }

    public String getCode(int pos) {
        CIEcriture entity = (CIEcriture) getEntity(pos);
        return entity.getCode();
    }

    public String getCodeSpecial(int pos) {
        CIEcriture entity = (CIEcriture) getEntity(pos);
        return entity.getCodeSpecial();
    }

    public String getEcritureId(int pos) {
        CIEcriture entity = (CIEcriture) getEntity(pos);
        return entity.getEcritureId();
    }

    public String getEmployeur(int pos) {
        CIEcriture entity = (CIEcriture) getEntity(pos);
        return entity.getEmployeur();
    }

    public String getExtourne(int pos) {
        CIEcriture entity = (CIEcriture) getEntity(pos);
        return entity.getExtourne();
    }

    public String getGenreEcriture(int pos) {
        CIEcriture entity = (CIEcriture) getEntity(pos);
        return entity.getGenreEcriture();
    }

    public String getIdRemarque(int pos) {
        CIEcriture entity = (CIEcriture) getEntity(pos);
        return entity.getIdRemarque();
    }

    public String getIdTypeCompte(int pos) {
        CIEcriture entity = (CIEcriture) getEntity(pos);
        return entity.getIdTypeCompte();

    }

    public String getMoisDebut(int pos) {
        CIEcriture entity = (CIEcriture) getEntity(pos);
        return entity.getMoisDebut();
    }

    public String getMoisFin(int pos) {
        CIEcriture entity = (CIEcriture) getEntity(pos);
        return entity.getMoisFin();
    }

    public String getPartBta(int pos) {
        CIEcriture entity = (CIEcriture) getEntity(pos);
        return entity.getPartBta();
    }

    public String getPartenaireId(int pos) {
        CIEcriture entity = (CIEcriture) getEntity(pos);
        return entity.getPartenaireId();
    }

    public String getParticulier(int pos) {
        CIEcriture entity = (CIEcriture) getEntity(pos);
        return entity.getParticulier();
    }
}
