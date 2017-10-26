package ch.globaz.pegasus.rpc.plausi;

import ch.globaz.pegasus.rpc.domaine.RetourAnnonce;
import ch.globaz.simpleoutputlist.annotation.Column;

public class PlausiBeanRetour {
    private final String businessCaseIdRPC;
    private final String nss;
    private final String idDecision;
    private final RetourAnnonce plausis;

    public PlausiBeanRetour(String businessCaseIdRPC, String nss, String idDecision, RetourAnnonce plausis) {
        this.businessCaseIdRPC = businessCaseIdRPC;
        this.nss = nss;
        this.idDecision = idDecision;
        this.plausis = plausis;
    }

    @Column(name = "BusinessCaseIdRPC")
    public String getBusinessCaseIdRPC() {
        return businessCaseIdRPC;
    }

    @Column(name = "Nss")
    public String getNss() {
        return nss;
    }

    @Column(name = "IdDecision")
    public String getIdDecision() {
        return idDecision;
    }

    @Column(name = "Category")
    public String getCategory() {
        if (plausis.getCategorie() != null) {
            return plausis.getCategorie().toString();
        }
        return null;
    }

    @Column(name = "IdPlausi")
    public String getId() {
        return plausis.getCodePlausi();
    }

    @Column(name = "Type")
    public String getType() {
        return plausis.getType().toString();
    }

}
