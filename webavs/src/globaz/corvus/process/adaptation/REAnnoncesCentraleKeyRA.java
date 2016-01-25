package globaz.corvus.process.adaptation;

import java.util.Hashtable;
import ch.admin.ofit.anakin.donnee.AnnonceAbstraite;

public class REAnnoncesCentraleKeyRA {

    private AnnonceAbstraite annAbsRevalorisee;
    private String cleRAAnnoncesAdaptation;
    private String idRA;
    private Hashtable listeErreurs;

    public REAnnoncesCentraleKeyRA() {
        super();
    }

    public AnnonceAbstraite getAnnAbsRevalorisee() {
        return annAbsRevalorisee;
    }

    public String getCleRAAnnoncesAdaptation() {
        return cleRAAnnoncesAdaptation;
    }

    public String getIdRA() {
        return idRA;
    }

    public Hashtable getListeErreurs() {
        return listeErreurs;
    }

    public void setAnnAbsRevalorisee(AnnonceAbstraite annAbsRevalorisee) {
        this.annAbsRevalorisee = annAbsRevalorisee;
    }

    public void setCleRAAnnoncesAdaptation(String cleRAAnnoncesAdaptation) {
        this.cleRAAnnoncesAdaptation = cleRAAnnoncesAdaptation;
    }

    public void setIdRA(String idRA) {
        this.idRA = idRA;
    }

    public void setListeErreurs(Hashtable listeErreurs) {
        this.listeErreurs = listeErreurs;
    }

}
