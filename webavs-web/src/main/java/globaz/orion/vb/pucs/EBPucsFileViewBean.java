package globaz.orion.vb.pucs;

import ch.globaz.common.util.Dates;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.businessimpl.services.pucs.PucsServiceImpl;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.services.AFAffiliationServices;
import globaz.naos.translation.CodeSystem;
import globaz.orion.vb.EBAbstractViewBean;

import java.util.List;
import java.util.Optional;

public class EBPucsFileViewBean extends EBAbstractViewBean {
    private String id;
    private String provenance;
    private PucsFile pucsFile;
    private boolean hasParticulariteCodeBlocage;
    private boolean hasParticulariteFichePartiel;
    private List<AFParticulariteAffiliation> particularites;
    private boolean hasRightAccesSecurity = false;
    private String idAffiliation;

    public EBPucsFileViewBean() {
        super();
        pucsFile = new PucsFile();
    }

    public EBPucsFileViewBean(PucsFile pucsFile, List<AFParticulariteAffiliation> particularites, AFAffiliation afAffiliation) {
        super();
        this.pucsFile = pucsFile;
        id = pucsFile.getIdDb();
        if (afAffiliation != null) {
            idAffiliation = afAffiliation.getAffiliationId();
        }

        // Gestion du code sÚcuritÚ !!
        pucsFile.setLock(!PucsServiceImpl.userHasRight(afAffiliation, BSessionUtil.getSessionFromThreadContext()));
        if (!pucsFile.isLock()) {
            pucsFile.setLock(!AFAffiliationServices.hasRightAccesSecurity(pucsFile.getCodeSecuriteCi(),
                    BSessionUtil.getSessionFromThreadContext()));
        }
        hasRightAccesSecurity = !pucsFile.isLock();

        this.particularites = particularites;

        // Gestion des particularitÚs.
        if (particularites != null) {
            hasParticulariteCodeBlocage = particularites.stream()
                    .anyMatch(par -> CodeSystem.PARTIC_AFFILIE_CODE_BLOCAGE_DECFINAL.equals(par.getParticularite()) &&
                            Dates.toDate(par.getDateDebut()).getYear() <= Integer.parseInt(pucsFile.getAnneeDeclaration()));
            hasParticulariteFichePartiel = particularites.stream()
                    .anyMatch(par -> CodeSystem.PARTIC_AFFILIE_FICHE_PARTIELLE.equals(par.getParticularite()) &&
                            Dates.toDate(par.getDateDebut()).getYear() <= Integer.parseInt(pucsFile.getAnneeDeclaration()));
        }
    }

    public Optional<AFParticulariteAffiliation> getParticularite() {
        Optional<AFParticulariteAffiliation> particularite = Optional.empty();
        if(hasParticulariteCodeBlocage) {
            particularite = particularites.stream().filter(p -> CodeSystem.PARTIC_AFFILIE_CODE_BLOCAGE_DECFINAL.equals(p.getParticularite())).findFirst();
        } else if(hasParticulariteFichePartiel) {
            particularite = particularites.stream().filter(p -> CodeSystem.PARTIC_AFFILIE_FICHE_PARTIELLE.equals(p.getParticularite())).findFirst();
        }
        return particularite;
        }

    public String getParticulariteId() {
        Optional<AFParticulariteAffiliation> particularite = getParticularite();
        return particularite.isPresent() ? particularite.get().getParticulariteId() : null;
    }

    @Override
    public void add() throws Exception {
        // nothing
    }

    public void changeUser() throws Exception {
        // OrionServiceLocator.getPucsService().changePucsOwner(this.id, this.provenance);
    }

    @Override
    public void delete() throws Exception {
        // nothing
    }

    @Override
    public String getId() {
        return id;
    }

    public String getProvenance() {
        return provenance;
    }

    public PucsFile getPucsFile() {
        return pucsFile;
    }

    public BSpy getSpy() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        // nothing
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    @Override
    public void update() throws Exception {
        // nothing
    }

    public Boolean hasParticularite() {
        return hasParticulariteFichePartiel || hasParticulariteCodeBlocage;
    }

    public boolean isVisible() {
        return hasRightAccesSecurity;
    }

    public boolean hasLockParticularite() {
        return hasParticulariteFichePartiel || hasParticulariteCodeBlocage;
    }

    public boolean hasLock() {
        return !hasRightAccesSecurity || hasParticulariteFichePartiel || hasParticulariteCodeBlocage;
    }

    public String getMessageLock() {
        String message = "";
        if (!hasRightAccesSecurity) {
            message = BSessionUtil.getSessionFromThreadContext().getLabel("NIVEAU_SECURITE_INSUFFISANT") + " ";
        }
        if (hasParticulariteFichePartiel) {
            message = message
                    + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                            CodeSystem.PARTIC_AFFILIE_FICHE_PARTIELLE) + " ";

        }
        if (hasParticulariteCodeBlocage) {
            message = message
                    + " "
                    + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                            CodeSystem.PARTIC_AFFILIE_CODE_BLOCAGE_DECFINAL);

        }
        return message;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

}
