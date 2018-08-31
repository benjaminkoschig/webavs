package ch.globaz.vulpecula.process.revision;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServiceMilitaire;
import ch.globaz.vulpecula.domain.repositories.decompte.TravailleurPourRevision;
import ch.globaz.vulpecula.external.models.osiris.CompteAnnexe;
import ch.globaz.vulpecula.process.prestations.PrestationsListExcel;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

public class ListRevisionRecapExcel extends PrestationsListExcel {
    TreeMap<String, TravailleurPourRevision> listeTravailleur = null;
    private static final String SHEET_TITLE = "recap";
    private String idEmployeur;
    private String annee;
    private CompteAnnexe cptAnnexe;

    public ListRevisionRecapExcel(BSession session, String filenameRoot, String documentTitle, CompteAnnexe ca) {
        super(session, filenameRoot, documentTitle);
        cptAnnexe = ca;
        HSSFSheet sheet = createSheet(SHEET_TITLE);

        sheet.setFitToPage(true);
        HSSFPrintSetup ps = sheet.getPrintSetup();
        ps.setFitWidth( (short) 1);
        ps.setFitHeight( (short) 1);
        sheet.setAutobreaks(true);

        short widthCol = 2500;
        sheet.setColumnWidth((short) 0,  (short) 3333);
        sheet.setColumnWidth((short) 1, (short) 8000);
        sheet.setColumnWidth((short) 2, (short) 3333);
        sheet.setColumnWidth((short) 3, (short) 2000);
        sheet.setColumnWidth((short) 4, (short) 2000);
        sheet.setColumnWidth((short) 5, (short) 3000);
        sheet.setColumnWidth((short) 6, (short) 3000);
        sheet.setColumnWidth((short) 7, (short) 3000);
        sheet.setColumnWidth((short) 8, (short) 3000);
        sheet.setColumnWidth((short) 9, widthCol);
        sheet.setColumnWidth((short) 11, (short) 3000);
        sheet.setColumnWidth((short) 12, widthCol);
        sheet.setColumnWidth((short) 13, widthCol);
        sheet.setColumnWidth((short) 14, widthCol);
        sheet.setColumnWidth((short) 15, widthCol);
        sheet.setColumnWidth((short) 16, widthCol);
        sheet.setColumnWidth((short) 17, widthCol);
        sheet.setColumnWidth((short) 18, widthCol);
    }

    @Override
    public void createContent() {
        initPage(true);
        createRow();
        createCriteresEmployeur();
        createRow(2);
        createTable();
    }

    private void createCriteresEmployeur() {
        createCell(getListName());
        createRow(2);
        createCell(getSession().getLabel("LISTE_RECAP_EMPLOYEUR_SOLDEOUVERT"), getStyleGras());
        /*
         * Verification qu'un solde existe dans notre compte annexe. Les cas radiés ne possèdent pas de compte annexe, et donc pas de solde.
         */
        createCell(((cptAnnexe.getSolde()!=null) ? cptAnnexe.getSolde() : new Montant(0)), getStyleMontantTotal());
        createRow(2);
        Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findById(getIdEmployeur());
        createCell(employeur.getConvention().getDesignation(), getStyleGras());
        createCell(employeur.getRaisonSociale(), getStyleGras());
        createCell(employeur.getAffilieNumero(), getStyleGras());

    }

    private void createTable() {
        Qualification qualif = null;

        // TODO made it as Label please
        createCell("N. Poste", getStyleListLeftNone());
        createCell("Nom, prénom", getStyleListLeftNone());
        createCell("Qualification", getStyleListRightNone());
        createCell("Taux CP", getStyleListRightNone());
        createCell("Année", getStyleListRightNone());
        createCell("Salaire base", getStyleListRightNone());
        createCell("Vac. grat. AJ", getStyleListRightNone());
        createCell("Salaire AF", getStyleListRightNone());
        createCell("Salaire AVS", getStyleListRightNone());
        createCell(getSession().getLabel("LISTE_REVISION_TOTAL_RECAP"),getStyleListRightNone());
        createCell(getSession().getLabel("LISTE_REVISION_DIFFERENCE"),getStyleListRightNone());
        createCell(getSession().getLabel("LISTE_REVISION_DESCRIPTION"),getStyleListRightNone());
        createCell("CPP", getStyleListRightNone());
        createCell(getSession().getLabel("LISTE_REVISION_APG_MILITAIRE"),getStyleListRightNone());
        createCell(getSession().getLabel("LISTE_REVISION_COMPL_SM"),getStyleListRightNone());
        createCell(getSession().getLabel("LISTE_REVISION_TOTAL_APG_SM"),getStyleListRightNone());
        createCell(getSession().getLabel("LISTE_REVISION_AJ"),getStyleListRightNone());
        createCell(getSession().getLabel("LISTE_CONGE_PAYE"),getStyleListRightNone());

        Montant montantTotalBase = Montant.ZERO;
        Montant montantTotalCP = Montant.ZERO;
        Montant montantTotalAF = Montant.ZERO;
        Montant montantTotalAVS = Montant.ZERO;
        Montant montantTotalCPP = Montant.ZERO;
        Montant montantTotalAPGMilitaire = Montant.ZERO;
        Montant montantTotalComplSM = Montant.ZERO;
        Montant montantTotalApgSM = Montant.ZERO;
        Montant montantTotalAJ = Montant.ZERO;
        Montant montantTotalCongePaye = Montant.ZERO;


        for (Map.Entry<String, TravailleurPourRevision> travailleurRevision : listeTravailleur.entrySet()) {
            String idPoste = "";
            Montant montatApgComplSM = Montant.ZERO;
            TravailleurPourRevision trav = travailleurRevision.getValue();
            PosteTravail poste = null;
            createRow();
            if (trav.getTravailleur().getPostesTravail() != null) {
                for (PosteTravail pt : trav.getTravailleur().getPostesTravail()) {
                    if (pt.getIdEmployeur().equals(idEmployeur)) {
                        idPoste = pt.getId();
                        poste = pt;
                        if (pt.getQualification() != null) {                           
                            qualif = pt.getQualification();
                        }
                    }
                }
            }

            createCell(idPoste);
            createCell(trav.getTravailleur().getNomPrenomTravailleur(), getStyleListLeftNone());

            if (qualif != null) {
                createCell(getCode(qualif.getValue()), getStyleListRightNone());
            } else {
                createCell(" - ");
            }

            List<AdhesionCotisationPosteTravail> adhesions = new ArrayList<AdhesionCotisationPosteTravail>();
            boolean posteHasDateFin = true;
            if(JadeStringUtil.isBlankOrZero(poste.getPeriodeActivite().getDateFinAsSwissValue())) {
                adhesions = VulpeculaRepositoryLocator.getAdhesionCotisationPosteRepository().findByIdPosteTravail(poste.getId());
                posteHasDateFin = false;
            }else {
                adhesions = VulpeculaRepositoryLocator.getAdhesionCotisationPosteRepository().findByIdPosteTravail(poste.getId(), poste.getPeriodeActivite().getDateFin());
            }

            Taux tauxToPrint;
            if(posteHasDateFin) {
                tauxToPrint = getTauxToPrint(adhesions, poste.getPeriodeActivite().getDateFin());
            }else {
                String dateRef = "31.12." + annee;
                tauxToPrint = getTauxToPrint(adhesions, new Date(dateRef));
            }

            createCell((tauxToPrint.doubleValue() / 100), getStylePourcentNone());
            createCell(getAnnee(), getStyleListRightNone());
            montantTotalBase = montantTotalBase.add(trav.getMontantBase());
            createCell(trav.getMontantBase(), getStyleMontantNone());
            montantTotalCP = montantTotalCP.add(trav.getMontantCP());
            createCell(trav.getMontantCP(), getStyleMontantNone());
            montantTotalAF = montantTotalAF.add(trav.getMontantAF());
            createCell(trav.getMontantAF(), getStyleMontantNone());
            montantTotalAVS = montantTotalAVS.add(trav.getMontantAVS());
            createCell(trav.getMontantAVS(), getStyleMontantNone());
            createCell("");
            createCell("");
            createCell("");
            montantTotalCPP = montantTotalCPP.add(trav.getMontantCPP());
            createCell(trav.getMontantCPP(), getStyleMontantNone());
            montantTotalAPGMilitaire = montantTotalAPGMilitaire.add(computeMontantAPGMilitaire(trav));
            montatApgComplSM = montatApgComplSM.add(computeMontantAPGMilitaire(trav));
            createCell(computeMontantAPGMilitaire(trav),getStyleMontantNone());
            montantTotalComplSM = montantTotalComplSM.add(computeMontantComplSM(trav));
            montatApgComplSM = montatApgComplSM.add(computeMontantComplSM(trav));
            createCell(computeMontantComplSM(trav), getStyleMontantNone());
            montantTotalApgSM = montantTotalApgSM.add(montatApgComplSM);
            createCell(montatApgComplSM, getStyleMontantNone());
            montantTotalAJ = montantTotalAJ.add(computeMontantAJ(trav));
            createCell(computeMontantAJ(trav), getStyleMontantNone());
            montantTotalCongePaye = montantTotalCongePaye.add(computeMontantCP(trav));
            createCell(computeMontantCP(trav), getStyleMontantNone());
        }
        createRow();
        createRow();
        createCell("");
        createCell(getSession().getLabel("LISTE_REVISION_TOTAL"));
        createCell("");
        createCell("");
        createCell("");
        createCell(montantTotalBase, getStyleMontantNone());
        createCell(montantTotalCP, getStyleMontantNone());
        createCell(montantTotalAF, getStyleMontantNone());
        createCell(montantTotalAVS, getStyleMontantNone());
        createCell("");
        createCell("");
        createCell("");
        createCell(montantTotalCPP,getStyleMontantNone());
        createCell(montantTotalAPGMilitaire, getStyleMontantNone());
        createCell(montantTotalComplSM, getStyleMontantNone());
        createCell(montantTotalApgSM, getStyleMontantNone());
        createCell(montantTotalAJ, getStyleMontantNone());
        createCell(montantTotalCongePaye,getStyleMontantNone());
    }

    private Taux getTauxToPrint(List<AdhesionCotisationPosteTravail> adhesions, Date dateRef) {
        Taux taux = new Taux(0);       
        for(AdhesionCotisationPosteTravail adhesion : adhesions) {
            if(!TypeAssurance.COTISATION_AC2.equals(adhesion.getTypeAssurance()) && !TypeAssurance.CONGES_PAYES.equals(adhesion.getTypeAssurance()) && adhesion.isActif(dateRef)) {
                Taux retrieved = VulpeculaServiceLocator.getCotisationService().findTaux(adhesion.getIdCotisation(), dateRef);          
                taux = taux.addTaux(retrieved);
            }         
        }
        return taux;     
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_REVISION_TYPE_NUMBER;
    }

    @Override
    public String getListName() {
        return getSession().getLabel("LISTE_TRAVAILLEUR_REVISION");
    }

    public void setListeTravailleur(TreeMap<String, TravailleurPourRevision> listeTravailleur) {
        this.listeTravailleur = listeTravailleur;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    //    private Montant computeMontantCPP(TravailleurPourRevision travailleur) {
    //        Montant totalCPP = Montant.ZERO;
    //        if(!travailleur.getDecomptesSalaireCPP().isEmpty()) {
    //            for(DecompteSalaire ds : travailleur.getDecomptesSalaireCPP()) {
    //                totalCPP = totalCPP.add(ds.getSalaireTotal());
    //            }
    //            return totalCPP;           
    //        }
    //        return totalCPP;
    //    }

    private Montant computeMontantAPGMilitaire(TravailleurPourRevision travailleur) {
        Montant totalApgMilitaire = Montant.ZERO;
        if(!travailleur.getListeSM().isEmpty()) {
            for (ServiceMilitaire sm : travailleur.getListeSM()) {
                totalApgMilitaire = totalApgMilitaire.add(sm.getVersementAPG());    
            }
            return totalApgMilitaire;
        }
        return totalApgMilitaire;       
    }

    private Montant computeMontantComplSM(TravailleurPourRevision travailleur) {
        Montant complSM = Montant.ZERO;
        if(!travailleur.getListeSM().isEmpty()) {
            for (ServiceMilitaire sm : travailleur.getListeSM()) {
                complSM = complSM.add(sm.getMontantBrut());    
            }
            return complSM;
        }
        return complSM;       
    }

    //    private Montant computeMontantTotalApgSM(TravailleurPourRevision travailleur) {
    //        Montant totalApgSM = Montant.ZERO;
    //        if(!travailleur.getListeSM().isEmpty()) {
    //            for (ServiceMilitaire sm : travailleur.getListeSM()) {
    //                totalApgSM = totalApgSM.add(sm.getMontantCouvertureAPG());    
    //            }
    //            return totalApgSM;
    //        }
    //        return totalApgSM;       
    //    }

    private Montant computeMontantAJ(TravailleurPourRevision travailleur) {
        Montant totalAJ = Montant.ZERO;
        if(!travailleur.getListeAJ().isEmpty()) {
            for(AbsenceJustifiee aj : travailleur.getListeAJ()) {
                totalAJ = totalAJ.add(aj.getMontantBrut());
            }
            return totalAJ;
        }
        return totalAJ;
    }

    private Montant computeMontantCP(TravailleurPourRevision travailleur) {
        Montant totalCP = Montant.ZERO;
        if(!travailleur.getListeCP().isEmpty()) {
            for(CongePaye cp : travailleur.getListeCP()) {
                totalCP = totalCP.add(cp.getMontantNet());
            }
            return totalCP;          
        }
        return totalCP; 
    }


}
