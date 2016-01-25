package globaz.orion.vb.pucs;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.jade.common.JadeCodingUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.services.AFAffiliationServices;
import globaz.naos.translation.CodeSystem;
import globaz.orion.vb.EBAbstractViewBean;
import java.util.Arrays;
import java.util.List;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.businessimpl.services.pucs.EtatSwissDecPucsFile;
import ch.globaz.orion.businessimpl.services.pucs.PucsServiceImpl;

public class EBPucsFileViewBean extends EBAbstractViewBean {
    private String id = null;
    private String provenance = null;
    private PucsFile pucsFile = null;
    private Boolean hasParticulariteCodeBlocage = null;
    private Boolean hasParticulariteFichePartiel = null;
    private boolean hasRightAccesSecurity = false;

    public EBPucsFileViewBean() {
        super();
        pucsFile = new PucsFile();
    }

    public EBPucsFileViewBean(PucsFile pucsFile) {
        super();
        this.pucsFile = pucsFile;
        if (!pucsFile.getProvenance().isSwissDec()) {
            pucsFile.setLock(!PucsServiceImpl.userHasRight(pucsFile, EtatSwissDecPucsFile.A_TRAITER,
                    BSessionUtil.getSessionFromThreadContext()));
        }
        hasRightAccesSecurity = !pucsFile.isLock();
        try {
            hasParticulariteCodeBlocage = hasParticularite(CodeSystem.PARTIC_AFFILIE_CODE_BLOCAGE_DECFINAL);
            hasParticulariteFichePartiel = hasParticularite(CodeSystem.PARTIC_AFFILIE_FICHE_PARTIELLE);

        } catch (Exception e) {
            JadeCodingUtil.catchException(this, "EBPucsFileViewBean", e);
        }
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

    public boolean hasLock() {
        return (!hasRightAccesSecurity || hasParticulariteFichePartiel || hasParticulariteCodeBlocage);
    }

    private boolean hasParticularite(String genreParticularite) throws Exception {
        List<AFAffiliation> affiliations = AFAffiliationServices.searchAffiliationByNumeros(
                Arrays.asList(pucsFile.getNumeroAffilie()), BSessionUtil.getSessionFromThreadContext());
        if (!affiliations.isEmpty()) {
            return AFParticulariteAffiliation.existeParticularite(BSessionUtil.getSessionFromThreadContext(),
                    affiliations.get(0).getId(), genreParticularite);
        } else {
            return false;
        }
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

}
