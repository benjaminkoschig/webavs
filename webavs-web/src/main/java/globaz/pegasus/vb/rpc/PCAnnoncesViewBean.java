package globaz.pegasus.vb.rpc;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.util.List;
import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce.AnnonceConverter;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce.LotAnnonceRepository;
import ch.globaz.pegasus.rpc.domaine.CodeTraitement;
import ch.globaz.pegasus.rpc.domaine.EtatAnnonce;
import ch.globaz.pegasus.rpc.domaine.LotRpcWithNbAnnonces;

public class PCAnnoncesViewBean extends BJadePersistentObjectViewBean {

    private final transient LotAnnonceRepository lotAnnonceRepository = new LotAnnonceRepository();

    private String defaultEtat = AnnonceConverter.toCsCode(EtatAnnonce.CORRECTION);
    private String defaultCode = AnnonceConverter.toCsCode(CodeTraitement.RETOUR_A_TRAITER);
    private String mode = null;
    private String nss = null;
    private String nom = null;
    private String prenom = null;
    private String periodDebut = null;
    private String periodFin = null;
    private String sortBy = null;
    private boolean rechercheFamille = false;

    public String getDefaultEtat() {
        return defaultEtat;
    }

    public void setDefaultEtat(String defaultEtat) {
        this.defaultEtat = defaultEtat;
    }

    public String getDefaultCode() {
        return defaultCode;
    }

    public void setDefaultCode(String defaultCode) {
        this.defaultCode = defaultCode;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPeriodDebut() {
        return periodDebut;
    }

    public void setPeriodDebut(String periodDebut) {
        this.periodDebut = periodDebut;
    }

    public String getPeriodFin() {
        return periodFin;
    }

    public void setPeriodFin(String periodFin) {
        this.periodFin = periodFin;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public boolean isRechercheFamille() {
        return rechercheFamille;
    }

    public void setRechercheFamille(boolean rechercheFamille) {
        this.rechercheFamille = rechercheFamille;
    }

    public boolean isCurentMonthGenerated() {
        boolean isGenerated = false;
        List<LotRpcWithNbAnnonces> lotWithNb = lotAnnonceRepository.searchLastsLots(1);
        for (LotRpcWithNbAnnonces currentLot : lotWithNb) {
            isGenerated = currentLot.getLot().getDateEnvoi().getMois().equals(Date.now().getMois());
        }

        return isGenerated;
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

}
