package globaz.pavo.process;

import globaz.framework.process.FWProcess;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.comparaison.CIAnomalieCI;
import globaz.pavo.db.comparaison.CIAnomalieCIManager;
import globaz.pavo.db.comparaison.CISqlCorrectionAnomalie;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIEcritureManager;
import java.util.HashMap;

public class CIComparaisonCorrigeAnomalies extends FWProcess {

    private static final long serialVersionUID = 2510948622631832178L;
    private boolean wantCorrectAnneeOuverture = true;
    private boolean wantCorrectClotureCaisse = true;
    private boolean wantCorrectClotureDate = true;
    private boolean wantCorrectClotureMotif = true;
    private boolean wantCorrectEtatOrigine = true;
    private boolean wantCorrectMotifOuverture = true;
    private boolean wantCorrectNom = true;

    private boolean wantCorrectNumeroAvsPrecedant = true;

    public CIComparaisonCorrigeAnomalies() {
        super();
    }

    /**
     * @param session
     */
    public CIComparaisonCorrigeAnomalies(BSession session) {
        super(session);
    }

    /**
     * @param parent
     */
    public CIComparaisonCorrigeAnomalies(FWProcess parent) {
        super(parent);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            CIAnomalieCIManager anomalieMgr = new CIAnomalieCIManager();
            anomalieMgr.setSession(getSession());
            anomalieMgr.setForAtraiter(CIAnomalieCI.CS_A_TRAITER);
            // anomalieMgr.setLikeNumeroAvs("13369791111");
            anomalieMgr.find(BManager.SIZE_NOLIMIT);
            int size = anomalieMgr.size();
            CIAnomalieCI anomalieSrc;
            for (int i = 0; i < size; i++) {
                anomalieSrc = (CIAnomalieCI) anomalieMgr.get(i);
                try {
                    if (!CIAnomalieCI.CS_CI_ABSENT_A_LA_CAISSE.equals(anomalieSrc.getTypeAnomalie())
                            && !CIAnomalieCI.CS_CI_ABSENT_A_ZAS.equals(anomalieSrc.getTypeAnomalie())
                            && !CIAnomalieCI.CS_CLOTURE.equals(anomalieSrc.getTypeAnomalie())) {
                        traiteAnomalie(anomalieSrc);
                        anomalieTraite(anomalieSrc.getAnomalieId());
                    } else if (CIAnomalieCI.CS_CI_ABSENT_A_ZAS.equals(anomalieSrc.getTypeAnomalie())) {
                        if (!hasEcritures(anomalieSrc)) {
                            traiteAnomalieSansEcriture(anomalieSrc);
                        } else {
                            if (anomalieSrc.getNumeroAvs().trim().length() < 11) {
                                continue;
                            }
                            genereAnnonce63(anomalieSrc);
                        }
                        anomalieTraite(anomalieSrc.getAnomalieId());

                    } else if (CIAnomalieCI.CS_CI_ABSENT_A_LA_CAISSE.equals(anomalieSrc.getTypeAnomalie())) {
                        ajouteCI(anomalieSrc);
                        anomalieTraite(anomalieSrc.getAnomalieId());
                    } else if (CIAnomalieCI.CS_CI_PRESENT_CLOTURE.equals(anomalieSrc.getTypeAnomalie())) {

                    }
                } catch (Exception e) {
                    getMemoryLog().logMessage(anomalieSrc.getNumeroAvs(), FWMessage.FATAL, "correction des entêtes");

                }
                if (!getTransaction().hasErrors()) {
                    getTransaction().commit();
                } else {
                    getMemoryLog().logMessage(
                            anomalieSrc.getNumeroAvs() + " " + getTransaction().getErrors().toString(),
                            FWMessage.INFORMATION, "correction des entêtes");
                    getTransaction().rollback();
                    getTransaction().clearErrorBuffer();

                }
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.FATAL, "correction des entêtes");

        } finally {

        }

        return !isAborted();
    }

    private void ajouteCI(CIAnomalieCI ciSrc) throws Exception {
        CICompteIndividuel ciDst = new CICompteIndividuel();
        ciDst.setSession(getSession());
        ciDst.setAccesSecurite(CICompteIndividuel.CS_ACCESS_0);
        ciDst.setRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        ciDst.setNumeroAvs(ciSrc.getNumeroAvs());
        ciDst.setNomPrenom(ciSrc.getNomPrenom());
        ciDst.setPaysOrigineId(ciSrc.getPays());
        ciDst.setCiOuvert(new Boolean(true));
        ciDst.setAnneeOuverture(ciSrc.getAnneeOuverture());
        ciDst.setDerniereCaisse(ciSrc.getDerniereCaisse());
        ciDst.setDernierMotifCloture(ciSrc.getDernierMotif());
        ciDst.setDernierMotifOuverture(ciSrc.getMotifOuverture());
        ciDst.setNumeroAvsPrecedant(ciSrc.getNumeroAvsPrecedent());
        ciDst.setDerniereCloture(ciSrc.getDateCloture());
        // ciDst.setCaisseTenantCI(ciSrc.);
        ciDst.setPlausiNumAvs(true);
        ciDst.add(getTransaction());
    }

    private void anomalieTraite(String idAnomalie) throws Exception {
        CISqlCorrectionAnomalie getSqlEntity = new CISqlCorrectionAnomalie();
        BPreparedStatement ciPrepared = new BPreparedStatement(getTransaction());

        try {
            ciPrepared.prepareStatement(getSqlEntity.getSqlTraite());
            ciPrepared.setInt(1, Integer.parseInt(CIAnomalieCI.CS_TRAITE));
            ciPrepared.setDouble(2, Double.parseDouble(idAnomalie));
            ciPrepared.execute();
        } finally {
            ciPrepared.closePreparedStatement();
        }

    }

    // méthode qui update le CI, suite aux valeurs générées dans la table
    // CIANOMP
    private void corrigeAnomalies(BTransaction transaction) {

    }

    private void genereAnnonce63(CIAnomalieCI AnomAAnonncer) throws Exception {

        CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                CIApplication.DEFAULT_APPLICATION_PAVO);
        HashMap attributs = new HashMap();
        attributs.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");
        attributs.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, "63");
        // assuré
        attributs.put(IHEAnnoncesViewBean.NUMERO_ASSURE, AnomAAnonncer.getNumeroAvs());
        // envoi
        application.annonceARC(getTransaction(), attributs, false, false);
    }

    @Override
    protected String getEMailObject() {
        if (!isOnError()) {
            return "la correction des anomalies s'est effectuée avec succès";
        } else {
            return "la correction des anomalies a echouée !";
        }

    }

    private boolean hasEcritures(CIAnomalieCI anomalie) {
        try {
            if (JAUtil.isIntegerEmpty(anomalie.getCompteIndividuelId())) {
                // si l'id CI est vide on return vrai, autant faire un 63 dans
                // le vide, que cloturer un ci qui a des écritures actives
                return true;
            }
            CIEcritureManager mgr = new CIEcritureManager();
            mgr.setEtatEcritures("actives");
            mgr.setSession(getSession());
            mgr.setForCompteIndividuelId(anomalie.getCompteIndividuelId());
            if (mgr.getCount() > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return true;
        }

    }

    /**
     * @return
     */
    public boolean isWantCorrectAnneeOuverture() {
        return wantCorrectAnneeOuverture;
    }

    /**
     * @return
     */
    public boolean isWantCorrectClotureCaisse() {
        return wantCorrectClotureCaisse;
    }

    /**
     * @return
     */
    public boolean isWantCorrectClotureDate() {
        return wantCorrectClotureDate;
    }

    /**
     * @return
     */
    public boolean isWantCorrectClotureMotif() {
        return wantCorrectClotureMotif;
    }

    /**
     * @return
     */
    public boolean isWantCorrectEtatOrigine() {
        return wantCorrectEtatOrigine;
    }

    /**
     * @return
     */
    public boolean isWantCorrectMotifOuverture() {
        return wantCorrectMotifOuverture;
    }

    /**
     * @return
     */
    public boolean isWantCorrectNom() {
        return wantCorrectNom;
    }

    /**
     * @return
     */
    public boolean isWantCorrectNumeroAvsPrecedant() {
        return wantCorrectNumeroAvsPrecedant;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * @param b
     */
    public void setWantCorrectAnneeOuverture(boolean b) {
        wantCorrectAnneeOuverture = b;
    }

    /**
     * @param b
     */
    public void setWantCorrectClotureCaisse(boolean b) {
        wantCorrectClotureCaisse = b;
    }

    /**
     * @param b
     */
    public void setWantCorrectClotureDate(boolean b) {
        wantCorrectClotureDate = b;
    }

    /**
     * @param b
     */
    public void setWantCorrectClotureMotif(boolean b) {
        wantCorrectClotureMotif = b;
    }

    /**
     * @param b
     */
    public void setWantCorrectEtatOrigine(boolean b) {
        wantCorrectEtatOrigine = b;
    }

    /**
     * @param b
     */
    public void setWantCorrectMotifOuverture(boolean b) {
        wantCorrectMotifOuverture = b;
    }

    /**
     * @param b
     */
    public void setWantCorrectNom(boolean b) {
        wantCorrectNom = b;
    }

    /**
     * @param b
     */
    public void setWantCorrectNumeroAvsPrecedant(boolean b) {
        wantCorrectNumeroAvsPrecedant = b;
    }

    /**
     * Retourne une requete en preparedStatement en fonction de l'anomalie
     * 
     * @param anomalie
     * @throws Exception
     */
    private void traiteAnomalie(CIAnomalieCI anomalie) throws Exception {
        CISqlCorrectionAnomalie getSqlEntity = new CISqlCorrectionAnomalie();
        BPreparedStatement ciPrepared = new BPreparedStatement(getTransaction());
        try {
            ciPrepared.prepareStatement(getSqlEntity.getSql(anomalie.getTypeAnomalie()));
            if (CIAnomalieCI.CS_NOM.equals(anomalie.getTypeAnomalie())) {
                ciPrepared.setString(1, anomalie.getNomPrenom());
            } else if (CIAnomalieCI.CS_ANNEE_OUVERTURE.equals(anomalie.getTypeAnomalie())) {
                ciPrepared.setInt(1, Integer.parseInt(anomalie.getAnneeOuverture()));
            } else if (CIAnomalieCI.CS_CI_PRESENT_CLOTURE.equals(anomalie.getTypeAnomalie())) {
                /****************************************************************
                 * Dans le cas d'anomalie de type présent clôturé, on n'as pas l'id ci *
                 *********************************************************/
                ciPrepared.setString(1, "1");
                CICompteIndividuelManager mgr = new CICompteIndividuelManager();
                mgr.setSession(getSession());
                mgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
                mgr.setForNumeroAvs(anomalie.getNumeroAvs());
                mgr.find();
                if (mgr.size() == 0) {
                    return;
                } else {
                    ciPrepared.setInt(2,
                            Integer.parseInt(((CICompteIndividuel) mgr.getFirstEntity()).getCompteIndividuelId()));
                }
            } else if (CIAnomalieCI.CS_NATIONNALITE.equals(anomalie.getTypeAnomalie())) {
                ciPrepared.setInt(1, Integer.parseInt(anomalie.getPays()));
            } else if (CIAnomalieCI.CS_NUMERO_AVS_ANCIEN.equals(anomalie.getTypeAnomalie())) {
                ciPrepared.setString(1, anomalie.getNumeroAvsPrecedent());
            } else if (CIAnomalieCI.CS_MOTIF_OUVERTURE.equals(anomalie.getTypeAnomalie())) {
                ciPrepared.setInt(1, Integer.parseInt(anomalie.getMotifOuverture()));
            } else {
                System.out.println(anomalie.getNumeroAvs() + " " + anomalie.getTypeAnomalie());
            }
            if (!CIAnomalieCI.CS_CI_PRESENT_CLOTURE.equals(anomalie.getTypeAnomalie())) {
                ciPrepared.setInt(2, Integer.parseInt((anomalie.getCompteIndividuelId())));
            }
            ciPrepared.execute();
        } finally {
            ciPrepared.closePreparedStatement();
        }

    }

    private void traiteAnomalieSansEcriture(CIAnomalieCI anomalie) throws Exception {
        if (!JAUtil.isIntegerEmpty(anomalie.getCompteIndividuelId())) {
            CISqlCorrectionAnomalie getSqlEntity = new CISqlCorrectionAnomalie();
            BPreparedStatement ciPrepared = new BPreparedStatement(getTransaction());

            try {
                ciPrepared.prepareStatement(getSqlEntity.getSql(CIAnomalieCI.CS_CI_PRESENT_CLOTURE));
                ciPrepared.setString(1, "2");
                ciPrepared.setDouble(2, Double.parseDouble(anomalie.getCompteIndividuelId()));
                ciPrepared.execute();
            } finally {
                ciPrepared.closePreparedStatement();
            }
        }

    }

}
