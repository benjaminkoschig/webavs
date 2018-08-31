package globaz.vulpecula.vb.ctrlemployeur;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.controleemployeur.ControleEmployeur;
import ch.globaz.vulpecula.domain.models.controleemployeur.TypeControle;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.repositories.controleemployeur.ControleEmployeurRepository;
import ch.globaz.vulpecula.util.CodeSystem;
import ch.globaz.vulpecula.util.CodeSystemUtil;
import ch.globaz.vulpecula.web.servlet.PTConstants;

public class PTControleEmployeurViewBean extends BJadePersistentObjectViewBean {
    private ControleEmployeur controleEmployeur = new ControleEmployeur();

    private ControleEmployeurRepository controleEmployeurRepository = VulpeculaRepositoryLocator
            .getControleEmployeurRepository();

    @Override
    public void add() throws Exception {
        controleEmployeurRepository.create(controleEmployeur);
    }

    @Override
    public void delete() throws Exception {
        controleEmployeurRepository.delete(controleEmployeur);
    }

    @Override
    public String getId() {
        return controleEmployeur.getId();
    }

    @Override
    public void retrieve() throws Exception {
        controleEmployeur = controleEmployeurRepository.findById(getId());
    }

    @Override
    public void setId(String id) {
        controleEmployeur.setId(id);
    }

    @Override
    public void update() throws Exception {
        controleEmployeurRepository.update(controleEmployeur);
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(controleEmployeur.getSpy());
    }

    public Employeur getEmployeur() {
        return controleEmployeur.getEmployeur();
    }

    public void setEmployeur(Employeur employeur) {
        controleEmployeur.setEmployeur(employeur);
    }

    public void setDateControle(String dateControle) {
        if (Date.isValid(dateControle)) {
            controleEmployeur.setDateControle(new Date(dateControle));
        } else {
            controleEmployeur.setDateControle(null);
        }
    }

    public String getDateControle() {
        return controleEmployeur.getDateControleAsSwissValue();
    }

    public void setMontant(String montant) {
        controleEmployeur.setMontant(new Montant(montant));
    }

    public String getMontant() {
        return controleEmployeur.getMontantAsValue();
    }

    public void setDateAu(String dateAu) {
        if (Date.isValid(dateAu)) {
            controleEmployeur.setDateAu(new Date(dateAu));
        } else {
            controleEmployeur.setDateAu(null);
        }
    }

    public String getDateAu() {
        return controleEmployeur.getDateAuAsSwissValue();
    }

    public void setNumeroMeroba(String numeroMeroba) {
        controleEmployeur.setNumeroMeroba(numeroMeroba);
    }

    public String getNumeroMeroba() {
        return controleEmployeur.getNumeroMeroba();
    }

    public void setType(String type) {
        if (TypeControle.isValid(type)) {
            controleEmployeur.setType(TypeControle.fromValue(type));
        } else {
            controleEmployeur.setType(null);
        }
    }

    public String getType() {
        return controleEmployeur.getTypeAsValue();
    }

    public String getReviseur() {
        return controleEmployeur.getIdUtilisateur();
    }

    public void setReviseur(String reviseur) {
        controleEmployeur.setIdUtilisateur(reviseur);
    }

    public boolean isAutresMesures() {
        return controleEmployeur.isAutresMesures();
    }

    public boolean getAutresMesures() {
        return controleEmployeur.isAutresMesures();
    }

    public void setAutresMesures(boolean autresMesures) {
        controleEmployeur.setAutresMesures(autresMesures);
    }

    public List<CodeSystem> getTypes() {
        return CodeSystemUtil.getCodesSystemesForFamille(PTConstants.CS_GROUPE_TYPE_CTRL);
    }

    public String getTypeControleDefaut() {
        return TypeControle.PERIODIQUE.getValue();
    }
}
