package globaz.cygnus.process.decision;

import globaz.corvus.api.lots.IRELot;
import globaz.cygnus.application.RFApplication;
import globaz.cygnus.db.paiement.RFLotJointPrestationJointOV;
import globaz.cygnus.db.paiement.RFLotJointPrestationJointOVManager;
import globaz.cygnus.db.paiement.RFPrestation;
import globaz.cygnus.services.comptabilite.RFComptabiliserMiseEnGedService;
import globaz.cygnus.services.comptabilite.RFOrdreVersementData;
import globaz.cygnus.services.comptabilite.RFPrestationData;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.prestation.tools.PRSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;

/**
 * Processus de mise en GED des décisions RFM .A pour vocation à être lancé en <b>ligne de commande</b>.
 * Utilisation en ligne de commande:<br/>
 * classname=globaz.cygnus.process.decision.RFImprimerDecisionsProcessForLot</br> user=[user]<br/>
 * password=[pass]<br/>
 * application="CYGNUS"<br/>
 * idLot=[identifian du lot, ou identifiant de plusieurs lots séparés par des ,]
 * 
 * @see globaz.pegasus.process.lot.PCComptabiliserProcess
 * @author sce
 * 
 */
public class RFImprimerDecisionsProcessForLot extends AbstractJadeJob {

    private static final long serialVersionUID = 8731586547381408153L;
    /* Liste d'identifiant effectivement à traiter */
    List<String> identifiantDeLotsATraiter = new ArrayList<String>();
    /* paramètre passé en ligne de commande, format possible: un entier, ou x,x,x, */
    private String idLot = null;

    private String idGestionnaire;
    // gfdkjshglkjdfhgkjsdf

    private String mail;

    BISession sessionCygnus = null;

    //
    public String getIdLot() {
        return idLot;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    @Override
    public String getDescription() {
        return "Process for re-gedify décision rfm with Command Line Job";
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public void run() {

        try {

            initParameters();

            for (String idLot : identifiantDeLotsATraiter) {
                generateDecisionForLot(idLot);
                // sadas
            }

        } catch (Exception e) {
            JadeLogger.error(this, "The process cannot be terminated due to an unexpected error: ");
            JadeLogger.error(this, e);
            try {
                JadeSmtpClient.getInstance().sendMail(getSession().getUserEMail(),
                        getSession().getLabel("IMPRIMER_DECISION_ERREUR"), e.getMessage(), null);
            } catch (Exception ex) {
                JadeLogger.error(this, "Problem happening when sending error messages...." + ex.getMessage());
            }
        }
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    /**
     * Validation des paramètres d'entrée<br/>
     * Consiste en la validation de l'identifiant du lot concerné
     * 
     * @throws DecisionException
     *             l'exception levé si l'id est null ou n'est pas un entier
     */
    private void validateParameters() throws DecisionException {

        if (identifiantDeLotsATraiter.size() == 0) {
            throw new DecisionException("The id(s) passed must is(are) not valid integer id(s)");
        }

    }

    /**
     * Initialisation des paraètres d'entrées
     * 
     * @throws Exception
     */
    private void initParameters() throws Exception {

        String[] identifiantsDeLot = idLot.split(",");

        for (String id : identifiantsDeLot) {
            if ((null != id) && JadeStringUtil.isDigit(id)) {
                identifiantDeLotsATraiter.add(id);
            }
        }

        mail = getSession().getUserEMail();
        // Récupération d'une session #CYGNUS pour atteindre labels + catalogues de textes des documents.
        sessionCygnus = PRSession.connectSession(getSession(), RFApplication.DEFAULT_APPLICATION_CYGNUS);

        validateParameters();
    }

    private void generateDecisionForLot(String idLot) throws Exception {
        RFComptabiliserMiseEnGedService rfComptaMiseEnGedService = new RFComptabiliserMiseEnGedService();
        rfComptaMiseEnGedService.setSession((BSession) sessionCygnus);
        rfComptaMiseEnGedService.setTransaction(getTransaction());
        rfComptaMiseEnGedService.setAdresseMail(mail);
        rfComptaMiseEnGedService.setIdGestionnaire(idGestionnaire);
        rfComptaMiseEnGedService.miseEnGedDesDecisionsDuLot(idLot, creationPrestationsOVsSet(idLot));
    }

    private RFLotJointPrestationJointOVManager recherchePrestationsOVs(String idLot) throws Exception {

        RFLotJointPrestationJointOVManager lotJointPrestationJointOVManager = new RFLotJointPrestationJointOVManager();
        lotJointPrestationJointOVManager.setSession(getSession());
        lotJointPrestationJointOVManager.setForEtatsLot(new String[] { IRELot.CS_ETAT_LOT_VALIDE });
        lotJointPrestationJointOVManager.setForLotOwner(IRELot.CS_LOT_OWNER_RFM);
        lotJointPrestationJointOVManager.setForIdLot(idLot);
        lotJointPrestationJointOVManager.setForOrderBy(RFPrestation.FIELDNAME_ID_ADRESSE_PAIEMENT + ","
                + RFPrestation.FIELDNAME_ID_TIERS_BENEFICIAIRE + "," + RFPrestation.FIELDNAME_ID_PRESTATION);
        lotJointPrestationJointOVManager.find(BManager.SIZE_NOLIMIT);

        return lotJointPrestationJointOVManager;
    }

    private Set<RFPrestationData> creationPrestationsOVsSet(String idLot) throws Exception {

        RFLotJointPrestationJointOVManager lotJointPrestationJointOVManager = recherchePrestationsOVs(idLot);

        Set<RFPrestationData> prestationsSet = new LinkedHashSet<RFPrestationData>();
        Set<RFOrdreVersementData> ordresVersementSet = new HashSet<RFOrdreVersementData>();
        String currentIdPrestation = "";
        RFLotJointPrestationJointOV itLotJointPrestationJointOV = null;
        RFLotJointPrestationJointOV itLotJointPrestationJointOVPrecedante = null;

        /*
         * on parcours le manager : pour chaque prestation on ajoute la nouvelle prestation au Set de prestation pour
         * chaque OV on ajoute une OV au Set d'OV on ajoute le Set d'OV à la dernière prestation
         */
        int i = 0;

        for (@SuppressWarnings("unchecked")
        Iterator<RFLotJointPrestationJointOV> it = lotJointPrestationJointOVManager.iterator(); it.hasNext();) {

            itLotJointPrestationJointOV = it.next();
            // on set l'idLot que l'on traite
            // idLot = itLotJointPrestationJointOV.getIdLot();
            // tant que c'est la même prestation
            if (JadeStringUtil.isEmpty(currentIdPrestation)) {

                currentIdPrestation = itLotJointPrestationJointOV.getIdPrestation();
                // ajout d'un OV
                ordresVersementSet.add(new RFOrdreVersementData(itLotJointPrestationJointOV.getIdOrdreVersement(),
                        itLotJointPrestationJointOV.getTypeVersement(), itLotJointPrestationJointOV.getNumeroFacture(),
                        itLotJointPrestationJointOV.getIdExterne(), itLotJointPrestationJointOV.getCsRole(),
                        itLotJointPrestationJointOV.getIdDomaineApplication(), itLotJointPrestationJointOV
                                .getIdTiersAdressePaiement(), itLotJointPrestationJointOV.getIdTiers(),
                        itLotJointPrestationJointOV.getMontantOrdreVersement(),
                        itLotJointPrestationJointOV.getIdRole(), itLotJointPrestationJointOV.getIdTypeDeSoin(),
                        itLotJointPrestationJointOV.getIsForcerPayement(), itLotJointPrestationJointOV.getNss(),
                        itLotJointPrestationJointOV.getNom(), itLotJointPrestationJointOV.getPrenom(),
                        itLotJointPrestationJointOV.getMontantDepassementQD(), itLotJointPrestationJointOV
                                .getIsImportation(), itLotJointPrestationJointOV.getIsCompense(),
                        itLotJointPrestationJointOV.getIdSousTypeDeSoin(), itLotJointPrestationJointOV
                                .getIdSectionOrdreVersement()));

            } else if (currentIdPrestation.equals(itLotJointPrestationJointOV.getIdPrestation())) { // mm prestation
                // ajout d'une nouvelle OV au tableau courant d'OVs
                ordresVersementSet.add(new RFOrdreVersementData(itLotJointPrestationJointOV.getIdOrdreVersement(),
                        itLotJointPrestationJointOV.getTypeVersement(), itLotJointPrestationJointOV.getNumeroFacture(),
                        itLotJointPrestationJointOV.getIdExterne(), itLotJointPrestationJointOV.getCsRole(),
                        itLotJointPrestationJointOV.getIdDomaineApplication(), itLotJointPrestationJointOV
                                .getIdTiersAdressePaiement(), itLotJointPrestationJointOV.getIdTiers(),
                        itLotJointPrestationJointOV.getMontantOrdreVersement(),
                        itLotJointPrestationJointOV.getIdRole(), itLotJointPrestationJointOV.getIdTypeDeSoin(),
                        itLotJointPrestationJointOV.getIsForcerPayement(), itLotJointPrestationJointOV.getNss(),
                        itLotJointPrestationJointOV.getNom(), itLotJointPrestationJointOV.getPrenom(),
                        itLotJointPrestationJointOV.getMontantDepassementQD(), itLotJointPrestationJointOV
                                .getIsImportation(), itLotJointPrestationJointOV.getIsCompense(),
                        itLotJointPrestationJointOV.getIdSousTypeDeSoin(), itLotJointPrestationJointOV
                                .getIdSectionOrdreVersement()));

            } else { // nouvelle prestation
                // on ajoute la tableau d'OV à l'ancienne prestation
                itLotJointPrestationJointOVPrecedante = (RFLotJointPrestationJointOV) lotJointPrestationJointOVManager
                        .getContainer().get(i - 1);

                prestationsSet
                        .add(new RFPrestationData(itLotJointPrestationJointOVPrecedante.getIdPrestation(),
                                itLotJointPrestationJointOVPrecedante.getDateMoisAnnee(),
                                itLotJointPrestationJointOVPrecedante.getMontantTotal(),
                                itLotJointPrestationJointOVPrecedante.getIdLot(), itLotJointPrestationJointOVPrecedante
                                        .getCsEtatPrestation(), ordresVersementSet,
                                itLotJointPrestationJointOVPrecedante.getTypePrestation(),
                                itLotJointPrestationJointOVPrecedante.getIdDecision(),
                                itLotJointPrestationJointOVPrecedante.getRemboursementRequerant(),
                                itLotJointPrestationJointOVPrecedante.getRemboursementConjoint(),
                                itLotJointPrestationJointOVPrecedante.getIsRI(), itLotJointPrestationJointOVPrecedante
                                        .getIsLAPRAMS(), itLotJointPrestationJointOVPrecedante.getIdAdressePaiement(),
                                itLotJointPrestationJointOVPrecedante.getIdTiersBeneficiaire()));

                currentIdPrestation = itLotJointPrestationJointOV.getIdPrestation();

                // on remet le tableau de prestation à vide
                ordresVersementSet = new HashSet<RFOrdreVersementData>();
                // ajout d'un OV
                ordresVersementSet.add(new RFOrdreVersementData(itLotJointPrestationJointOV.getIdOrdreVersement(),
                        itLotJointPrestationJointOV.getTypeVersement(), itLotJointPrestationJointOV.getNumeroFacture(),
                        itLotJointPrestationJointOV.getIdExterne(), itLotJointPrestationJointOV.getCsRole(),
                        itLotJointPrestationJointOV.getIdDomaineApplication(), itLotJointPrestationJointOV
                                .getIdTiersAdressePaiement(), itLotJointPrestationJointOV.getIdTiers(),
                        itLotJointPrestationJointOV.getMontantOrdreVersement(),
                        itLotJointPrestationJointOV.getIdRole(), itLotJointPrestationJointOV.getIdTypeDeSoin(),
                        itLotJointPrestationJointOV.getIsForcerPayement(), itLotJointPrestationJointOV.getNss(),
                        itLotJointPrestationJointOV.getNom(), itLotJointPrestationJointOV.getPrenom(),
                        itLotJointPrestationJointOV.getMontantDepassementQD(), itLotJointPrestationJointOV
                                .getIsImportation(), itLotJointPrestationJointOV.getIsCompense(),
                        itLotJointPrestationJointOV.getIdSousTypeDeSoin(), itLotJointPrestationJointOV
                                .getIdSectionOrdreVersement()));
            }
            i++;
        }
        // on doit ajouter la dernière prestation si il y en a au moins une !
        if (lotJointPrestationJointOVManager.size() > 0) {
            prestationsSet.add(new RFPrestationData(itLotJointPrestationJointOV.getIdPrestation(),
                    itLotJointPrestationJointOV.getDateMoisAnnee(), itLotJointPrestationJointOV.getMontantTotal(),
                    itLotJointPrestationJointOV.getIdLot(), itLotJointPrestationJointOV.getCsEtatPrestation(),
                    ordresVersementSet, itLotJointPrestationJointOV.getTypePrestation(), itLotJointPrestationJointOV
                            .getIdDecision(), itLotJointPrestationJointOV.getRemboursementRequerant(),
                    itLotJointPrestationJointOV.getRemboursementConjoint(), itLotJointPrestationJointOV.getIsRI(),
                    itLotJointPrestationJointOV.getIsLAPRAMS(), itLotJointPrestationJointOV.getIdAdressePaiement(),
                    itLotJointPrestationJointOV.getIdTiersBeneficiaire()));
        }

        return prestationsSet;

    }

}
