package globaz.orion.vb.pucs;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.orion.vb.EBAbstractViewBean;
import java.util.List;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.businessimpl.services.pucs.PucsServiceImpl;

public class EBPucsFileViewBean extends EBAbstractViewBean {
    private String id;
    private String provenance;
    private PucsFile pucsFile;
    private boolean hasParticulariteCodeBlocage;
    private boolean hasParticulariteFichePartiel;
    private boolean hasRightAccesSecurity = false;
    private String idAffiliation;

    public EBPucsFileViewBean() {
        super();
        pucsFile = new PucsFile();
    }

    public EBPucsFileViewBean(PucsFile pucsFile, List<String> particularites, AFAffiliation afAffiliation) {
        super();
        this.pucsFile = pucsFile;
        id = pucsFile.getIdDb();
        if (afAffiliation != null) {
            idAffiliation = afAffiliation.getAffiliationId();
        }
        if (!pucsFile.getProvenance().isSwissDec()) {
            pucsFile.setLock(!PucsServiceImpl.userHasRight(afAffiliation, BSessionUtil.getSessionFromThreadContext()));
        }
        hasRightAccesSecurity = !pucsFile.isLock();

        if (particularites != null) {
            hasParticulariteCodeBlocage = particularites.contains(CodeSystem.PARTIC_AFFILIE_CODE_BLOCAGE_DECFINAL);
            hasParticulariteFichePartiel = particularites.contains(CodeSystem.PARTIC_AFFILIE_FICHE_PARTIELLE);
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
