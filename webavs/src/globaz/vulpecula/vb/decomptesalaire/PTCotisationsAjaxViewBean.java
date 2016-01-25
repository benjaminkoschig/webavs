package globaz.vulpecula.vb.decomptesalaire;

import globaz.vulpecula.vb.PTAjaxDisplayViewBean;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.web.views.decompte.CotisationDecompteContainer;
import ch.globaz.vulpecula.web.views.decompte.CotisationDecompteViewService;

public class PTCotisationsAjaxViewBean extends PTAjaxDisplayViewBean {
    private static final long serialVersionUID = 8105289757404663888L;

    private String idDecompteSalaire;

    private Decompte decompte;
    private CotisationDecompteContainer cotisationDecompteContainer;
    private Montant masseSalariale;
    private Montant masseAC2;
    private Montant masseFranchise;

    @Override
    public void retrieve() throws Exception {
        decompte = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findById(idDecompteSalaire).getDecompte();
        // Si la masse salariale est présente, c'est que l'on demande un nouvel affichage, soit un nouveau calcul
        if (!masseSalariale.isZero()) {
            cotisationDecompteContainer = new CotisationDecompteViewService().getCotisationDecompteView(
                    idDecompteSalaire, masseSalariale, masseAC2, masseFranchise);
        } else {
            cotisationDecompteContainer = new CotisationDecompteViewService()
                    .getCotisationDecompteViewForReaffichage(idDecompteSalaire);
        }
    }

    public CotisationDecompteContainer getCotisationDecompteContainer() {
        return cotisationDecompteContainer;
    }

    public String getIdDecompteSalaire() {
        return idDecompteSalaire;
    }

    public void setIdDecompteSalaire(final String idDecompteSalaire) {
        this.idDecompteSalaire = idDecompteSalaire;
    }

    public boolean isEditable() {
        return decompte.isEditable();
    }

    public void setMasseSalariale(Montant masseSalariale) {
        this.masseSalariale = masseSalariale;
    }

    public Montant getMasseSalariale() {
        return masseSalariale;
    }

    public void setMasseAC2(Montant masseAC2) {
        this.masseAC2 = masseAC2;
    }

    public void setMasseFranchise(Montant masseFranchise) {
        this.masseFranchise = masseFranchise;
    }
}
