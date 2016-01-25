package ch.globaz.pegasus.businessimpl.tests.factory;

import java.util.Map;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.vo.pcaccordee.PcaDecompte;
import ch.globaz.pegasus.businessimpl.tests.pcAccordee.DonneeForDonneeFinanciere;
import ch.globaz.pegasus.businessimpl.tests.retroAndOv.DonneeUtilsForTestRetroAndOV;

public class PegasusTestFactory {
    public static final String CONJOINT = "conjoint";
    public static final String REQUERANT = "requerant";

    /**
     * Créer un droit initial et ajoute la localité dans les données personnel
     * 
     * @param nssRequerant
     * @param dateDepotDemande
     * @param map
     * @return
     * @throws Exception
     */
    public static DonneeForDonneeFinanciere createDroitInitialAndUdateDonnesPersonnel(String nssRequerant,
            String dateDepotDemande, Map<String, Object> map) throws Exception {
        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDossierAndDemandeAndDroitInitial(
                nssRequerant, dateDepotDemande);
        // Ajout la localité le noiremont
        DonneeUtilsForTestRetroAndOV.updateAllDonneesPersonnel(data.getDroit(), "436");

        map.put(DonneeForDonneeFinanciere.class.getSimpleName(), data);
        return data;
    }

    public static PcaDecompte createPcaConjointAvs(String dateDebut, String dateFin) {
        return PegasusTestFactory.createPcaConjointAvs(dateDebut, dateFin, "0");
    }

    public static PcaDecompte createPcaConjointAvs(String dateDebut, String dateFin, String montant) {
        return PegasusTestFactory.createPcaConjointAvs(dateDebut, dateFin, montant, null);
    }

    public static PcaDecompte createPcaConjointAvs(String dateDebut, String dateFin, String montant, String idPCa) {
        PcaDecompte pcaReq = new PcaDecompte();
        pcaReq.setIdPCAccordee("10");
        pcaReq.setIdTiersBeneficiaire("1");
        pcaReq.setIdTiersAdressePaiement("11");
        pcaReq.setMontantPCMensuelle(montant);
        pcaReq.setCsRoleBeneficiaire(IPCDroits.CS_ROLE_FAMILLE_CONJOINT);
        pcaReq.setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_CALCULE);
        pcaReq.setCsGenrePC(IPCPCAccordee.CS_GENRE_PC_HOME);
        pcaReq.setCsTypePC(IPCPCAccordee.CS_TYPE_PC_VIELLESSE);
        pcaReq.setDateDebut(dateDebut);
        pcaReq.setDateFin(dateFin);
        pcaReq.setSousCodePresation(null);
        pcaReq.setIdPCAccordee(idPCa);
        return pcaReq;
    }

    public static PcaDecompte createPcaDom2RAvs(String dateDebut, String dateFin, String montant) {
        PcaDecompte pca = PegasusTestFactory.createPcaRequerantAvs(dateDebut, dateFin, montant);
        pca.setIdTiersAdressePaiementConjoint("21");
        return pca;
    }

    public static PcaDecompte createPcaRequerantAvs(String dateDebut, String dateFin) {
        return PegasusTestFactory.createPcaRequerantAvs(dateDebut, dateFin, "0");
    }

    public static PcaDecompte createPcaRequerantAvs(String dateDebut, String dateFin, String montant) {
        return PegasusTestFactory.createPcaRequerantAvs(dateDebut, dateFin, montant, null);
    }

    public static PcaDecompte createPcaRequerantAvs(String dateDebut, String dateFin, String montant, String idPca) {
        PcaDecompte pcaReq = new PcaDecompte();
        pcaReq.setIdPCAccordee("20");
        pcaReq.setIdTiersBeneficiaire("2");
        pcaReq.setIdTiersAdressePaiement("21");
        pcaReq.setMontantPCMensuelle(montant);
        pcaReq.setCsRoleBeneficiaire(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
        pcaReq.setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_CALCULE);
        pcaReq.setCsGenrePC(IPCPCAccordee.CS_GENRE_PC_HOME);
        pcaReq.setCsTypePC(IPCPCAccordee.CS_TYPE_PC_VIELLESSE);
        pcaReq.setDateDebut(dateDebut);
        pcaReq.setDateFin(dateFin);
        pcaReq.setIdPCAccordee(idPca);
        pcaReq.setSousCodePresation(null);
        return pcaReq;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getDataConjoint(Map<String, Object> map, Class<T> t) {
        return (T) PegasusTestFactory.getDataMap((Map) map.get(PegasusTestFactory.CONJOINT), t);
    }

    //
    @SuppressWarnings("unchecked")
    public static <T> T getDataMap(Map<String, Object> map, Class<T> t) {
        return (T) map.get(t.getSimpleName());
    }

    @SuppressWarnings("unchecked")
    public static <T> T getDataRequerant(Map<String, Object> map, Class<T> t) {
        return (T) PegasusTestFactory.getDataMap((Map) map.get(PegasusTestFactory.REQUERANT), t);
    }

}
