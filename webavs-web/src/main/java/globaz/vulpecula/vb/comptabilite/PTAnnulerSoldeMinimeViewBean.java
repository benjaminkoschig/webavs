package globaz.vulpecula.vb.comptabilite;

import globaz.vulpecula.vb.listes.PTListeProcessViewBean;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.web.util.FormUtil;

/**
 * @since WebBMS 3.1
 */
public class PTAnnulerSoldeMinimeViewBean extends PTListeProcessViewBean {
    private String dateJournal = null;
    private String libelle = null;
    private String montantMinime;
    private String typeMembre;
    private boolean simulation;

    /**
     * @return the dateJournal
     */
    public String getDateJournal() {
        return dateJournal;
    }

    /**
     * @return the libelle
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * @param dateJournal the dateJournal to set
     */
    public void setDateJournal(String dateJournal) {
        this.dateJournal = dateJournal;
    }

    /**
     * @param libelle the libelle to set
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * @return the montantMinime
     */
    public String getMontantMinime() {
        return montantMinime;
    }

    /**
     * @param montantMinime the montantMinime to set
     */
    public void setMontantMinime(String montantMinime) {
        this.montantMinime = montantMinime;
    }

    /**
     * @return the simulation
     */
    public boolean isSimulation() {
        return simulation;
    }

    /**
     * @param simulation the simulation to set
     */
    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }

    /**
     * 
     * @return La liste des codes systèmes qui représente les types membres et non membres
     */
    public Vector<String[]> getTypesMembres() {
        List<String> types = Arrays.asList(GenreCotisationAssociationProfessionnelle.MEMBRE.getValue(),
                GenreCotisationAssociationProfessionnelle.NON_MEMBRE.getValue());
        return FormUtil.getList(types);
    }

    public String getTypeMembre() {
        return typeMembre;
    }

    public void setTypeMembre(String typeMembre) {
        this.typeMembre = typeMembre;
    }

}
