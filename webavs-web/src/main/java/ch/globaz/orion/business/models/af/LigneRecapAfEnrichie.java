package ch.globaz.orion.business.models.af;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitLAPGJointDemande;
import globaz.apg.db.droits.APDroitLAPGJointDemandeManager;
import globaz.apg.summary.APSummaryMat;
import globaz.framework.db.postit.FWNoteP;
import globaz.framework.db.postit.FWNotePManager;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.summary.TISummaryInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;

public class LigneRecapAfEnrichie extends LigneRecapAf {
    private static String NOTE_TABLESOURCE = "globaz.al.vb.dossier.ALDossierViewBean";
    private List<FWNoteP> listeBlocNoteAF;
    private Integer idSelectedLine;
    private String idDossierAf;
    private String liensDossierMaternite;
    private String lienPrestationsMaternite;
    private String idTiers;
    private String idDroitMaternite;
    private String etatDossierAfLibelle;

    public void populateDataEcranDetail(int nbLignes, Integer numeroDossier) throws Exception {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        PRTiersWrapper tiers = PRTiersHelper.getTiers(session, getNss());
        idTiers = tiers.getIdTiers();
        listeBlocNoteAF = new ArrayList<FWNoteP>();
        idDossierAf = String.valueOf(numeroDossier);

        // récupération de l'état du dossier
        DossierComplexModel dossierAf = readDossierAf(String.valueOf(numeroDossier));
        setEtatDossierAfLibelle(getEtatDossierAfLibelleFromCs(session, dossierAf.getDossierModel().getEtatDossier()));

        // retrieve Bloc note AF
        listeBlocNoteAF = findBlocNote(idDossierAf);

        // retrieve id dossier maternité
        findIdDroitApgMatAndPeriodes();

        // recherche des périodes de prestation maternité
        findPeriodePrestationMaternite();

        idSelectedLine = nbLignes;
    }

    public List<FWNoteP> getListeBlocNoteAF() {
        return listeBlocNoteAF;
    }

    public void setListeBlocNoteAF(List<FWNoteP> listeBlocNoteAF) {
        this.listeBlocNoteAF = listeBlocNoteAF;
    }

    public Integer getIdSelectedLine() {
        return idSelectedLine;
    }

    public void setIdSelectedLine(Integer idSelectedLine) {
        this.idSelectedLine = idSelectedLine;
    }

    public String getIdDossierAf() {
        return idDossierAf;
    }

    public void setIdDossierAf(String idDossierAf) {
        this.idDossierAf = idDossierAf;
    }

    public String getIdDossierMaternite() {
        return liensDossierMaternite;
    }

    public void setIdDossierMaternite(String liensDossierMaternite) {
        this.liensDossierMaternite = liensDossierMaternite;
    }

    public String getLiensDossierMaternite() {
        return liensDossierMaternite;
    }

    public void setLiensDossierMaternite(String liensDossierMaternite) {
        this.liensDossierMaternite = liensDossierMaternite;
    }

    public String getLienPrestationsMaternite() {
        return lienPrestationsMaternite;
    }

    public void setLienPrestationsMaternite(String lienPrestationsMaternite) {
        this.lienPrestationsMaternite = lienPrestationsMaternite;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public String getIdDroitMaternite() {
        return idDroitMaternite;
    }

    public void setIdDroitMaternite(String idDroitMaternite) {
        this.idDroitMaternite = idDroitMaternite;
    }

    public String getEtatDossierAfLibelle() {
        return etatDossierAfLibelle;
    }

    public void setEtatDossierAfLibelle(String etatDossierAfLibelle) {
        this.etatDossierAfLibelle = etatDossierAfLibelle;
    }

    private String getEtatDossierAfLibelleFromCs(BSession session, String csEtatDossierAf) {
        return session.getCodeLibelle(csEtatDossierAf);
    }

    private DossierComplexModel readDossierAf(String numeroDossier) throws Exception {
        DossierComplexModel dossierAf = ALServiceLocator.getDossierComplexModelService().read(numeroDossier);
        return dossierAf;
    }

    private void findIdDroitApgMatAndPeriodes() throws Exception {

        APDroitLAPGJointDemandeManager manager = new APDroitLAPGJointDemandeManager();
        manager.setSession(BSessionUtil.getSessionFromThreadContext());
        manager.setForIdTiers(idTiers);
        manager.setForGenreService(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE);
        manager.setOrderBy(APDroitLAPG.FIELDNAME_DATEDEBUTDROIT + " DESC , " + APDroitLAPG.FIELDNAME_IDDROIT_LAPG
                + " DESC");
        manager.find(BManager.SIZE_NOLIMIT);

        if ((manager.size() > 0)) {
            APDroitLAPGJointDemande lastDroit = (APDroitLAPGJointDemande) manager.getFirstEntity();
            idDroitMaternite = lastDroit.getIdDroit();
        }

    }

    private void findPeriodePrestationMaternite() throws Exception {
        APSummaryMat summaryMat = new APSummaryMat();

        TISummaryInfo[] infosMat = summaryMat.getInfoForTiers(idTiers, BSessionUtil.getSessionFromThreadContext());

        if (infosMat != null) {
            lienPrestationsMaternite = infosMat[0].getText();
        }
    }

    private List<FWNoteP> findBlocNote(String idDossier) throws Exception {
        List<FWNoteP> listNotes = new ArrayList<FWNoteP>();

        FWNotePManager manager = new FWNotePManager();
        manager.setSession(BSessionUtil.getSessionFromThreadContext());
        manager.setForSourceId(idDossier);
        manager.setForTableSource(NOTE_TABLESOURCE);
        manager.setWithMemo(Boolean.TRUE);
        manager.find(BManager.SIZE_NOLIMIT);

        if (manager.size() > 0) {
            for (Iterator it = manager.iterator(); it.hasNext();) {
                FWNoteP note = (FWNoteP) it.next();
                listNotes.add(note);
            }
        }

        return listNotes;
    }
}
