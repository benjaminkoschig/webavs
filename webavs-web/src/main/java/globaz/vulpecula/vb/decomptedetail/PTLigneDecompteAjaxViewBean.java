package globaz.vulpecula.vb.decomptedetail;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import ch.globaz.common.vb.JadeAbstractAjaxFindForDomain;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.decompte.CodeErreurDecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.TravailleurEbuDomain;
import ch.globaz.vulpecula.domain.repositories.Repository;
import ch.globaz.vulpecula.util.CodeSystemUtil;

/**
 * @author Arnaud Geiser (AGE) | Créé le 28 mars 2014
 * 
 */
public class PTLigneDecompteAjaxViewBean extends JadeAbstractAjaxFindForDomain<DecompteSalaire> {
    private static final long serialVersionUID = 6075910866261392089L;

    private String idDecompte;
    private String idPosteTravail;
    private String nomTravailleur;

    private DecompteSalaire entity = new DecompteSalaire();

    public PTLigneDecompteAjaxViewBean() {
        entity = new DecompteSalaire();
    }

    public void setIdLigneDecompte(final String idLigneDecompte) {
        entity.setId(idLigneDecompte);
    }

    public List<DecompteSalaire> getLignesDecompte() {
        return getList();
    }

    public void setIdDecompte(final String idDecompte) {
        this.idDecompte = idDecompte;
    }

    @Override
    public List<DecompteSalaire> findByRepository() {
        List<DecompteSalaire> lignesDecompte = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                .findWithDependencies(idDecompte, idPosteTravail, nomTravailleur);

        for (DecompteSalaire decompteSalaire : lignesDecompte) {
            if (decompteSalaire.getIdPosteTravail() == null || decompteSalaire.getIdPosteTravail().isEmpty()) {
                TravailleurEbuDomain travailleurEbu = VulpeculaRepositoryLocator.getNouveauTravailleurRepository()
                        .findByCorrelationIdWithQuittance(decompteSalaire.getCorrelationId());
                if (travailleurEbu != null) {
                    decompteSalaire.setPosteTravail(travailleurEbu.getPosteTravailFictif());
                }
            }
        }

        return lignesDecompte;
    }

    @Override
    public DecompteSalaire getEntity() {
        if (entity == null) {
            entity = new DecompteSalaire();
        }
        return entity;
    }

    @Override
    public Repository<DecompteSalaire> getRepository() {
        return VulpeculaRepositoryLocator.getDecompteSalaireRepository();
    }

    /**
     * @return the idDecompte
     */
    public String getIdDecompte() {
        return idDecompte;
    }

    public final void setIdPosteTravail(String idPosteTravail) {
        this.idPosteTravail = idPosteTravail;
    }

    public void setNomTravailleur(String nomTravailleur) {
        this.nomTravailleur = nomTravailleur;
    }

    // Pour le copain Websphere, il faut passer Object et pas l'objet typé.
    public String getTextesErreurs(Object erreurs) {
        List<String> codes = new ArrayList<String>();
        List<CodeErreurDecompteSalaire> listErreurs = (List<CodeErreurDecompteSalaire>) erreurs;
        for (CodeErreurDecompteSalaire erreur : listErreurs) {
            String libelle = CodeSystemUtil.getCodeSysteme(erreur.getCodeErreurAsValue(), Langues.Francais)
                    .getLibelle();
            codes.add(libelle);
        }
        return StringUtils.join(codes, ", ");
    }
}
