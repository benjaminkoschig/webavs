package globaz.vulpecula.vb.decomptenouveau;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import java.util.Collection;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.decompte.DecompteService;
import ch.globaz.vulpecula.businessimpl.services.decompte.GenererDecompteProcessor;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.NumeroDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.external.exceptions.ViewException;
import ch.globaz.vulpecula.util.CodeSystem;
import ch.globaz.vulpecula.util.CodeSystemUtil;
import ch.globaz.vulpecula.web.servlet.PTConstants;
import com.google.common.base.Preconditions;

/**
 * ViewBean utilisé dans la page JSP "decomptenouveau_de.jsp"
 * 
 * @since Web@BMS 0.01.01
 */
public class PTDecomptenouveauViewBean extends BJadePersistentObjectViewBean {
    private String dateDebut = null;
    private String dateFin = null;
    private Decompte decompte = new Decompte();
    private String idEmployeur = "";
    private String designationEmployeur = "";
    private boolean withPostes = false;
    private String email = "";

    private DecompteService decompteService = VulpeculaServiceLocator.getDecompteService();

    public String getEmail() {
        if (JadeStringUtil.isEmpty(email)) {
            email = ((BSession) getISession()).getUserEMail();
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isWithPostes() {
        return withPostes;
    }

    public void setWithPostes(boolean withPostes) {
        this.withPostes = withPostes;
    }

    public String getDesignationEmployeur() {
        return designationEmployeur;
    }

    public void setDesignationEmployeur(final String designationEmployeur) {
        this.designationEmployeur = designationEmployeur;
    }

    public Decompte getDecompte() {
        return decompte;
    }

    public void setTypeDecompte(final String typeDecompte) {
        decompte.setType(TypeDecompte.fromValue(typeDecompte));
    }

    public void setIdEmployeur(final String idEmployeur) {
        this.idEmployeur = idEmployeur;
        Employeur employeur = new Employeur();
        employeur.setId(idEmployeur);
        decompte.setEmployeur(employeur);
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setPeriodeDebut(final String periodeDebut) {
        dateDebut = periodeDebut;
    }

    public void setPeriodeFin(final String periodeFin) {
        dateFin = periodeFin;
        setPeriode();
    }

    public void setPeriode() {
        if (!JadeStringUtil.isEmpty(dateDebut) && !JadeStringUtil.isEmpty(dateFin)) {
            PeriodeMensuelle periode = new PeriodeMensuelle(dateDebut, dateFin);
            decompte.setPeriode(periode);
        }
    }

    public void setNumeroDecompte(String numeroDecompte) {
        if (!JadeStringUtil.isEmpty(numeroDecompte)) {
            decompte.setNumeroDecompte(new NumeroDecompte(numeroDecompte));
        }
    }

    public void setDateDecompte(final String dateDecompte) {
        if (!JadeStringUtil.isEmpty(dateDecompte)) {
            decompte.setDateEtablissement(new Date(dateDecompte));
        }
    }

    @Override
    public void add() throws ViewException, JadePersistenceException {
        Preconditions.checkNotNull(decompte.getDateEtablissement());
        Preconditions.checkNotNull(decompte.getPeriode());

        if (TypeDecompte.SANS_TRAVAILLEUR.equals(decompte.getType())) {
            decompteService.genererDecomptesSansTravailleurs(decompte.getDateEtablissement(), decompte.getPeriode(),
                    email);
        } else {
            try {
                if (TypeDecompte.SPECIAL.equals(decompte.getType())
                        || TypeDecompte.CONTROLE_EMPLOYEUR.equals(decompte.getType())) {
                    decompte.setDateReception(decompte.getDateEtablissement());
                }

                GenererDecompteProcessor genererDecompteProcess = new GenererDecompteProcessor();
                decompte = genererDecompteProcess.genererDecompteVideManuel(decompte, withPostes, email);
            } catch (UnsatisfiedSpecificationException e) {
                throw new ViewException(e);
            }
        }
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(final String newId) {
    }

    @Override
    public void update() throws Exception {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public void delete() throws Exception {
    }

    public String getCsControleEmployeur() {
        return TypeDecompte.CONTROLE_EMPLOYEUR.getValue();
    }

    public String getCsSpecial() {
        return TypeDecompte.SPECIAL.getValue();
    }

    public String getCsSansTravailleur() {
        return TypeDecompte.SANS_TRAVAILLEUR.getValue();
    }

    public String getNumCT() {
        return NumeroDecompte.CONTROLE_EMPLOYEUR;
    }

    public Collection<CodeSystem> getTypesDecomptes() {
        return CodeSystemUtil.getCodesSystemesForFamille(PTConstants.CS_GROUPE_TYPE_DECOMPTES);
    }

    public String getValidationTousChampsRemplies() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("VALIDATION_TOUS_CHAMPS_REMPLIES");
    }
}