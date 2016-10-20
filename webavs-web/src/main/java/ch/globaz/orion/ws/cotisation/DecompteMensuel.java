package ch.globaz.orion.ws.cotisation;

import java.util.ArrayList;
import java.util.List;

public class DecompteMensuel {

    private String numeroAffilie;
    private String idAffilie;
    private String moisDecompte;
    private String anneeDecompte;
    private List<DecompteMensuelLine> linesDecompte;
    private boolean dejeEtabli;

    public DecompteMensuel() {
        super();
    }

    public DecompteMensuel(String numeroAffilie, String idAffilie, String moisDecompte) {
        super();
        this.numeroAffilie = numeroAffilie;
        this.idAffilie = idAffilie;
        this.moisDecompte = moisDecompte;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public String getMoisDecompte() {
        return moisDecompte;
    }

    public void setMoisDecompte(String moisDecompte) {
        this.moisDecompte = moisDecompte;
    }

    public void addDecompteMensuelLine(DecompteMensuelLine line) {
        if (linesDecompte == null) {
            linesDecompte = new ArrayList<DecompteMensuelLine>();
        }

        linesDecompte.add(line);
    }

    public List<DecompteMensuelLine> getLinesDecompte() {
        return new ArrayList<DecompteMensuelLine>(linesDecompte);
    }

    public String getAnneeDecompte() {
        return anneeDecompte;
    }

    public void setAnneeDecompte(String anneeDecompte) {
        this.anneeDecompte = anneeDecompte;
    }

    public boolean isDejeEtabli() {
        return dejeEtabli;
    }

    public void setDejeEtabli(boolean dejeEtabli) {
        this.dejeEtabli = dejeEtabli;
    }

}
