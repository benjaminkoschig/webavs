package globaz.cygnus.process.importationSecutel.miseAJourFichierSourceStep4;

import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityDataFind;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;
import com.google.gson.Gson;
import globaz.cygnus.api.motifsRefus.IRFMotifsRefus;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemande;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemandeManager;
import globaz.cygnus.process.RFImportDemandesCmsData;
import globaz.cygnus.process.importationSecutel.RFProcessImportationSecutelEnum;
import globaz.cygnus.utils.RFMyBigDecimal;
import globaz.cygnus.utils.RFUtils;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RFImportDemandesCmsMajFichierSourceHandler implements JadeProcessEntityInterface,
        JadeProcessEntityNeedProperties, JadeProcessEntityDataFind<RFProcessImportationSecutelEnum> {

    public enum PositionsAMettreAJourFichierSourceEnum {

        POSITION_DERNIERE_LIGNE(60, 69),
        POSITION_LIGNE_COURANTE(99, 108);

        private int debut;
        private int fin;

        PositionsAMettreAJourFichierSourceEnum(int debut, int fin) {
            this.debut = debut;
            this.fin = fin;
        }

        public int getDebut() {
            return debut;
        }

        public int getFin() {
            return fin;
        }
    }

    private static String CARACTER_FORMAT_MONTANT = "0";
    private static String CODE_LIGNE_TRAITEE_PAR_CAVS = "2";
    private static String MONTANT_ZERO = "0.00";
    public static final int NOMBRE_DE_POSITIONS_FORMAT_DERNIERE_LIGNE = 10;
    private static int NOMBRE_DE_POSITIONS_FORMAT_LIGNE_COURANTE = 7;

    public static String getMontantFormatteSurNPositionsDontDeuxDecimales(String montantAPayerNonFormatte,
            int nombreDePositions) {

        StringBuffer montantFormatteBfr = new StringBuffer();

        if (!JadeStringUtil.isBlankOrZero(montantAPayerNonFormatte)) {

            String[] partieEntiereEtDecimale = montantAPayerNonFormatte.split("\\.");

            StringBuffer prefixPartieEntiereBfr = new StringBuffer();

            for (int i = partieEntiereEtDecimale[0].length(); i < (nombreDePositions - 2); i++) {
                prefixPartieEntiereBfr.append(RFImportDemandesCmsMajFichierSourceHandler.CARACTER_FORMAT_MONTANT);
            }

            montantFormatteBfr.append(prefixPartieEntiereBfr.toString());
            montantFormatteBfr.append(partieEntiereEtDecimale[0]);
            montantFormatteBfr.append(partieEntiereEtDecimale[1]);

        } else {
            for (int i = 0; i < nombreDePositions; i++) {
                montantFormatteBfr.append(RFImportDemandesCmsMajFichierSourceHandler.CARACTER_FORMAT_MONTANT);
            }
        }

        return montantFormatteBfr.toString();
    }

    private String codeErreur = "";
    private JadeProcessEntity entity = null;
    private RFImportDemandesCmsData entityData = null;
    private String idDemande = "";
    private Map<String, String> lignes = null;
    private List<String[]> logsList = null;
    private RFMyBigDecimal montantAccepteTotal = null;

    @SuppressWarnings("unused")
    private Map<Enum<?>, String> properties = null;

    public RFImportDemandesCmsMajFichierSourceHandler(Map<String, String> lignes, List<String[]> logsList,
                                                      RFMyBigDecimal montantAccepteTotal) {
        super();
        this.lignes = lignes;
        this.logsList = logsList;
        this.montantAccepteTotal = montantAccepteTotal;
    }

    private String getMontantIdMotifDeRefus(String idDemande, BSession session) throws Exception {

        Map<String, String[]> idsMotifDeRefusSysteme = RFUtils.getIdsMotifDeRefusSysteme(
                BSessionUtil.getSessionFromThreadContext(), null);
        String idMotifDeRefusSysteme = idsMotifDeRefusSysteme.get(IRFMotifsRefus.ID_MAXIMUM_N_FRANC_PAR_ANNEE)[0];

        RFAssMotifsRefusDemandeManager rfAssMotRefDemMgr = new RFAssMotifsRefusDemandeManager();
        rfAssMotRefDemMgr.setSession(session);
        rfAssMotRefDemMgr.setForIdDemande(idDemande);
        rfAssMotRefDemMgr.changeManagerSize(0);
        rfAssMotRefDemMgr.find();

        if (rfAssMotRefDemMgr.size() > 0) {
            Iterator<RFAssMotifsRefusDemande> rfAssMotRefItr = rfAssMotRefDemMgr.iterator();
            while (rfAssMotRefItr.hasNext()) {

                RFAssMotifsRefusDemande rfAssMotRefDem = rfAssMotRefItr.next();
                if (rfAssMotRefDem != null) {
                    if (rfAssMotRefDem.getIdMotifsRefus().equals(idMotifDeRefusSysteme)) {
                        return rfAssMotRefDem.getMntMotifsDeRefus();
                    }
                } else {
                    throw new Exception(
                            "RFImportDemandesCmsMajFichierSourceHandler.isCodeErreurSelonIdMotifDeRefus()] Demande null");
                }
            }

            return "";
        } else {
            return "";
        }
    }

    /**
     * Methode pour faire un retrieve sur la demande concernée
     * 
     * @param idDataDemande
     * @return
     */
    private RFDemande loadDemandeParId(String idDataDemande, BSession session) {
        try {
            RFDemande rfDemande = new RFDemande();
            rfDemande.setSession(session);
            rfDemande.setIdDemande(idDataDemande);
            rfDemande.retrieve();

            if (!rfDemande.isNew()) {
                return rfDemande;
            } else {
                throw new IllegalArgumentException(
                        "[RFPreparerDemandesSecutelService.loadDemandeParId()]  Impossible de retrouver la demande avec son id");
            }

        } catch (Exception e) {
            throw new IllegalArgumentException(e + " : Impossible de retrouver la demande avec son id");
        }
    }

    private void logErreurs(String source, String message) throws JadeNoBusinessLogSessionError {
        JadeThread.logError(source, message);
        RFUtils.ajouterLogImportationsSecutel(JadeBusinessMessageLevels.ERROR, entityData.getNumeroLigne(),
                entityData.getNssBeneficiaire(), message,  logsList);
    }

    private void majLigne(String numeroLigne, String ligne, String montantAPayerNonFormatte, String codeTraitement) {

        try {
            StringBuffer nouvelleLigneStB = new StringBuffer();

            nouvelleLigneStB.append(ligne.substring(0,
                    PositionsAMettreAJourFichierSourceEnum.POSITION_LIGNE_COURANTE.getDebut() - 1));
            nouvelleLigneStB.append(RFImportDemandesCmsMajFichierSourceHandler.CODE_LIGNE_TRAITEE_PAR_CAVS);
            nouvelleLigneStB.append(RFImportDemandesCmsMajFichierSourceHandler
                    .getMontantFormatteSurNPositionsDontDeuxDecimales(montantAPayerNonFormatte,
                            RFImportDemandesCmsMajFichierSourceHandler.NOMBRE_DE_POSITIONS_FORMAT_LIGNE_COURANTE));
            nouvelleLigneStB.append(codeTraitement);
            nouvelleLigneStB.append(ligne.substring(
                    PositionsAMettreAJourFichierSourceEnum.POSITION_LIGNE_COURANTE.getFin() - 1, ligne.length()));

            majMontantTotal(montantAPayerNonFormatte);

            lignes.put(numeroLigne, nouvelleLigneStB.toString());

        } catch (Exception e) {
            lignes.put(numeroLigne, ligne);
        }
    }

    private void majLigneDemandeNonImportee() {

        majLigne(entityData.getNumeroLigne(), entityData.getLigne(),
                RFImportDemandesCmsMajFichierSourceHandler.MONTANT_ZERO, codeErreur);
    }

    private void majLigneSelonCalculDemande() throws Exception, JadeNoBusinessLogSessionError, JadePersistenceException {

        RFDemande rfDemande = loadDemandeParId(idDemande, BSessionUtil.getSessionFromThreadContext());

        // Ajout du montant DSAS au montant à payer
        String montantDsas = getMontantIdMotifDeRefus(idDemande, BSessionUtil.getSessionFromThreadContext());
        BigDecimal montantDsasPlusMontantAPayerBigDec = new BigDecimal("0");
        if (!JadeStringUtil.isBlankOrZero(rfDemande.getMontantAPayer())) {
            montantDsasPlusMontantAPayerBigDec = new BigDecimal(rfDemande.getMontantAPayer());
        }
        if (!JadeStringUtil.isBlankOrZero(montantDsas)) {
            montantDsasPlusMontantAPayerBigDec = montantDsasPlusMontantAPayerBigDec.add(new BigDecimal(montantDsas));
        }

        // rfDemande.setMontantAPayer(montantDsasPlusMontantAPayerBigDec.toString());

        majLigne(entityData.getNumeroLigne(), entityData.getLigne(), montantDsasPlusMontantAPayerBigDec.toString(),
                codeErreur);
    }

    private void majMontantTotal(String montantAPayerNonFormatte) {
        montantAccepteTotal.setValeur(montantAccepteTotal.getValeur().add(new BigDecimal(montantAPayerNonFormatte)));
    }

    /**
     * Mise à jour du fichier source ligne par ligne
     */
    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {

        // TODO: A vérifier: l'ordre des lignes est-il respecté par la population???
        try {

            if (siPasDerniereNiPremiereLigne()) {

                if (siDemandePasImportee()) {
                    majLigneDemandeNonImportee();
                } else {
                    majLigneSelonCalculDemande();
                }

            } else {
                lignes.put(entityData.getNumeroLigne(), entityData.getLigne());
            }

        } catch (Exception e) {
            logErreurs("RFImportDemandesCmsMajFichierSourceHandler.run()", e.getMessage());
            throw new JadePersistenceException(e.getMessage() + " [RFImportDemandesCmsMajFichierSourceHandler.run()]");
        }

    }

    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {

        if (entity != null) {
            this.entity = entity;

            Gson gson = new Gson();
            entityData = gson.fromJson(this.entity.getValue1(), RFImportDemandesCmsData.class);
        } else {
            JadeThread.logError("RFImportDemandesCmsMajFichierSourceHandler.setCurrentEntity()", "Entity is null");
        }

    }

    @Override
    public void setData(Map<RFProcessImportationSecutelEnum, String> hashMap) {
        if (null != hashMap) {
            idDemande = hashMap.get(RFProcessImportationSecutelEnum.ID_DEMANDE);
            codeErreur = hashMap.get(RFProcessImportationSecutelEnum.CODE_ERREUR);
        }
    }

    @Override
    public void setProperties(Map<Enum<?>, String> map) {
        properties = map;
    }

    private boolean siDemandePasImportee() {
        return JadeStringUtil.isBlankOrZero(idDemande);
    }

    private boolean siPasDerniereNiPremiereLigne() {
        return !(entityData.isDerniereLigne() || entityData.isPremiereLigne());
    }

}
