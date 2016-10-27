package ch.globaz.orion.ws.cotisation;

import java.util.ArrayList;
import java.util.List;

public class DecompteMensuel {

    private String numeroAffilie;
    private String idAffilie;
    private String moisDecompte;
    private String anneeDecompte;
    private List<DecompteMensuelLine> linesDecompte;
    private boolean dejaEtabli;

    public DecompteMensuel() {
        // vide pour génération jAXWS
    }

    public DecompteMensuel(DecompteMensuelBuilder builder) {
        numeroAffilie = builder.getNumeroAffilie();
        idAffilie = builder.getIdAffilie();
        moisDecompte = builder.getMoisDecompte();
        anneeDecompte = builder.getAnneeDecompte();
        linesDecompte = builder.getLinesDecompte();
        dejaEtabli = false;
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

    public void addAllLines(List<DecompteMensuelLine> lines) {
        linesDecompte = new ArrayList<DecompteMensuelLine>(lines);
    }

    public List<DecompteMensuelLine> getLinesDecompte() {
        return new ArrayList<DecompteMensuelLine>(linesDecompte);
    }

    public String getAnneeDecompte() {
        return anneeDecompte;
    }

    public boolean isDejaEtabli() {
        return dejaEtabli;
    }

    public void setDejaEtabli(boolean dejaEtabli) {
        this.dejaEtabli = dejaEtabli;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setMoisDecompte(String moisDecompte) {
        this.moisDecompte = moisDecompte;
    }

    public void setAnneeDecompte(String anneeDecompte) {
        this.anneeDecompte = anneeDecompte;
    }

    public void setLinesDecompte(List<DecompteMensuelLine> linesDecompte) {
        this.linesDecompte = new ArrayList<DecompteMensuelLine>(linesDecompte);
    }

}
