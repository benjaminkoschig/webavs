package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Operations {
    private BigDecimal controlAmount = new BigDecimal(0);
    private List<Ecriture> ecritures = new ArrayList<Ecriture>();
    private List<OrdreVersementCompta> ordresVersements = new ArrayList<OrdreVersementCompta>();

    public void addAllEcritures(List<Ecriture> ecritures) {
        this.ecritures.addAll(ecritures);
    }

    public void addAllOVs(List<OrdreVersementCompta> ovs) {
        ordresVersements.addAll(ovs);
    }

    public void addEcriture(Ecriture ecriture) {
        ecritures.add(ecriture);
    }

    public void addOV(OrdreVersementCompta ov) {
        ordresVersements.add(ov);
    }

    public BigDecimal getControlAmount() {
        return controlAmount;
    }

    public List<Ecriture> getEcritures() {
        return Collections.unmodifiableList(ecritures);
    }

    public List<OrdreVersementCompta> getOrdresVersements() {
        return Collections.unmodifiableList(ordresVersements);
    }

    public void setControlAmount(BigDecimal controlAmount) {
        this.controlAmount = controlAmount;
    }

    public void setEcritures(List<Ecriture> ecritures) {
        this.ecritures = ecritures;
    }

    public void setOrdresVersements(List<OrdreVersementCompta> ordresVersements) {
        this.ordresVersements = ordresVersements;
    }

}
