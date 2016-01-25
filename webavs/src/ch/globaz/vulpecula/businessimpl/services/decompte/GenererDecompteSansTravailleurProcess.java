package ch.globaz.vulpecula.businessimpl.services.decompte;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.employeur.EmployeurService;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.external.BProcessWithContext;

public class GenererDecompteSansTravailleurProcess extends BProcessWithContext {
    private Date dateEtablissement;
    private PeriodeMensuelle periodeMensuelle;
    private String email;

    private final EmployeurService employeurService = VulpeculaServiceLocator.getEmployeurService();

    private GenererDecompteProcessor genererDecompteProcessor;

    /**
     * UTILISER POUR LA DESERIALISATION PAR LE FRAMEWORK
     */
    public GenererDecompteSansTravailleurProcess() {
        genererDecompteProcessor = new GenererDecompteProcessor();
    }

    public GenererDecompteSansTravailleurProcess(Date dateEtablissement, PeriodeMensuelle periodeMensuelle, String email) {
        this();
        this.dateEtablissement = dateEtablissement;
        this.periodeMensuelle = periodeMensuelle;
        this.email = email;
    }

    private static final long serialVersionUID = -4534300339921184140L;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        List<Employeur> employeurs = employeurService.findEmployeursSansPostesAvecEdition(
                periodeMensuelle.getPeriodeDebut(), periodeMensuelle.getPeriodeFin());
        genererDecomptes(employeurs, dateEtablissement, periodeMensuelle);
        return true;
    }

    private void genererDecomptes(List<Employeur> employeurs, Date dateEtablissement, PeriodeMensuelle periode)
            throws JadePersistenceException, UnsatisfiedSpecificationException {
        List<Decompte> decomptes = new ArrayList<Decompte>();
        for (Employeur employeur : employeurs) {
            Decompte decompte = new Decompte();
            decompte.setType(TypeDecompte.PERIODIQUE);
            decompte.setPeriode(periode);
            decompte.setDateEtablissement(dateEtablissement);
            decompte.setEmployeur(employeur);
            decomptes.add(genererDecompteProcessor.genererDecompteVideManuel(decompte));
        }

        if (decomptes.size() > 0) {
            ImprimerDecomptesProcess impressionDecomptes = ImprimerDecomptesProcess.createWithDecomptes(decomptes);
            impressionDecomptes.setEMailAddress(email);
            impressionDecomptes.setSession(getSession());
            impressionDecomptes.setSendCompletionMail(false);
            impressionDecomptes.start();
        }
    }

    public final Date getDateEtablissement() {
        return dateEtablissement;
    }

    public final void setDateEtablissement(Date dateEtablissement) {
        this.dateEtablissement = dateEtablissement;
    }

    public final PeriodeMensuelle getPeriodeMensuelle() {
        return periodeMensuelle;
    }

    public final void setPeriodeMensuelle(PeriodeMensuelle periodeMensuelle) {
        this.periodeMensuelle = periodeMensuelle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

}
