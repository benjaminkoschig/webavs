package globaz.hercule.db.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BIPersistentObject;
import globaz.hercule.process.CEListeControlesAEffectuerProcess;

/**
 * @author hpe
 * @since Créé le 14 févr. 07
 */
public class CEControlesAEffectuerViewBean extends CEListeControlesAEffectuerProcess implements FWViewBeanInterface,
        BIPersistentObject {

    private static final long serialVersionUID = -8391526121818485527L;

    private String annee = "";
    private String anneeCptr = "";
    private String genreControle = "";
    private Boolean isAvecReviseur = null;
    private String masseSalA = "";
    private String masseSalDe = "";
    private String typeAdresse = "";

    public CEControlesAEffectuerViewBean() throws Exception {
    }

    @Override
    public void add() throws Exception {

    }

    @Override
    public void delete() throws Exception {

    }

    /**
     * @return
     */
    @Override
    public String getAnnee() {
        return annee;
    }

    @Override
    public String getAnneeCptr() {
        return anneeCptr;
    }

    /**
     * @return
     */
    @Override
    public String getGenreControle() {
        return genreControle;
    }

    @Override
    public String getId() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public Boolean getIsAvecReviseur() {
        return isAvecReviseur;
    }

    /**
     * @return
     */
    public String getMasseSalA() {
        return masseSalA;
    }

    /**
     * @return
     */
    public String getMasseSalDe() {
        return masseSalDe;
    }

    @Override
    public String getTypeAdresse() {
        return typeAdresse;
    }

    @Override
    public void retrieve() throws Exception {

    }

    /**
     * @param string
     */
    @Override
    public void setAnnee(String string) {
        annee = string;
    }

    @Override
    public void setAnneeCptr(String anneeCptr) {
        this.anneeCptr = anneeCptr;
    }

    /**
     * @param string
     */
    @Override
    public void setGenreControle(String string) {
        genreControle = string;
    }

    @Override
    public void setId(String newId) {

    }

    /**
     * @param boolean1
     */
    @Override
    public void setIsAvecReviseur(Boolean boolean1) {
        isAvecReviseur = boolean1;
    }

    /**
     * @param string
     */
    public void setMasseSalA(String string) {
        masseSalA = string;
    }

    /**
     * @param string
     */
    public void setMasseSalDe(String string) {
        masseSalDe = string;
    }

    @Override
    public void setTypeAdresse(String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }

    @Override
    public void update() throws Exception {

    }

}
