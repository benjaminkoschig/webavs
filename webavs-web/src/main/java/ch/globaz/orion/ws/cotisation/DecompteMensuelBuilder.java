package ch.globaz.orion.ws.cotisation;

import java.util.ArrayList;
import java.util.List;

public final class DecompteMensuelBuilder {

    private String numeroAffilie;
    private String idAffilie;
    private String moisDecompte;
    private String anneeDecompte;
    private List<DecompteMensuelLine> linesDecompte;

    public DecompteMensuelBuilder() {
        super();
    }

    public DecompteMensuelBuilder withNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
        return this;
    }

    public DecompteMensuelBuilder withIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
        return this;
    }

    public DecompteMensuelBuilder withMoisDecompte(String moisDecompte) {
        this.moisDecompte = moisDecompte;
        return this;
    }

    public DecompteMensuelBuilder withAnneeDecompte(String anneeDecompte) {
        this.anneeDecompte = anneeDecompte;
        return this;
    }

    public DecompteMensuelBuilder withLinesDecompte(List<DecompteMensuelLine> linesDecompte) {
        this.linesDecompte = new ArrayList<DecompteMensuelLine>(linesDecompte);
        return this;
    }

    public DecompteMensuel build() {
        return new DecompteMensuel(this);
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getMoisDecompte() {
        return moisDecompte;
    }

    public String getAnneeDecompte() {
        return anneeDecompte;
    }

    public List<DecompteMensuelLine> getLinesDecompte() {
        return linesDecompte;
    }
}
