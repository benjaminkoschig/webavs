package ch.globaz.orion.ws.cotisation;

import java.math.BigDecimal;
import ch.globaz.orion.ws.enums.TypeDecisionAcompteInd;

public class InfosDerniereDecisionActive {

    BigDecimal resultatNet = BigDecimal.ZERO;
    BigDecimal capitalInvesti = BigDecimal.ZERO;
    TypeDecisionAcompteInd typeDecisionAcompte;

    public BigDecimal getResultatNet() {
        return resultatNet;
    }

    public void setResultatNet(BigDecimal resultatNet) {
        this.resultatNet = resultatNet;
    }

    public BigDecimal getCapitalInvesti() {
        return capitalInvesti;
    }

    public void setCapitalInvesti(BigDecimal capitalInvesti) {
        this.capitalInvesti = capitalInvesti;
    }

    public TypeDecisionAcompteInd getTypeDecisionAcompte() {
        return typeDecisionAcompte;
    }

    public void setTypeDecisionAcompte(TypeDecisionAcompteInd typeDecisionAcompte) {
        this.typeDecisionAcompte = typeDecisionAcompte;
    }
}
