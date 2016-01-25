/*
 * Cr�� le 10 novembre 2010
 */
package globaz.cygnus.services.preparerDecision;

import globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins;
import globaz.cygnus.db.qds.RFQdPrincipaleJointDossier;
import globaz.cygnus.db.qds.RFQdPrincipaleJointDossierManager;
import globaz.cygnus.services.RFRetrieveNumeroDecisionService;
import globaz.cygnus.utils.RFUtils;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author jje
 * 
 *         G�n�re les d�cisions entit�s
 */
public class RFGenererDecisionsEntitesService {

    private String anneeCourante = "";
    Map<String, RFImputationDemandesData> cDemandes = null;
    private Set<RFDecisionData> decisions = null;
    private String idExecutionProcess = "";
    private Map<String, Set<String[]>> idQdIdsDossierMap = null;// IdQd,
    // idsDossier
    private String processusGestionnaire = "";
    private BSession session = null;
    private BITransaction transaction = null;

    public RFGenererDecisionsEntitesService(String processusGestionnaire,
            Map<String, RFImputationDemandesData> cDemandes, String idExecutionProcess, BSession session,
            BITransaction transaction) {
        super();
        // on set l'ann�e courante
        anneeCourante = JACalendar.format(JACalendar.today(), JACalendar.FORMAT_YYYY);
        idQdIdsDossierMap = new HashMap<String, Set<String[]>>();
        decisions = new LinkedHashSet<RFDecisionData>();
        this.processusGestionnaire = processusGestionnaire;
        this.transaction = transaction;
        this.cDemandes = cDemandes;
        this.session = session;
        this.idExecutionProcess = idExecutionProcess;

    }

    private void ajoutDemande(RFImputationDemandesData demandeCourante) throws Exception {

        if (decisions == null) {
            decisions = new LinkedHashSet<RFDecisionData>();
        }

        boolean hasDecision = false;

        for (RFDecisionData decisionCourante : decisions) {
            if (isDemandeDansDecison(demandeCourante, decisionCourante)) {
                decisionCourante.getIdDemandes().add(demandeCourante.getIdDemande());
                hasDecision = true;
                break;
            }
        }

        if (!hasDecision) {
            decisions.add(getNouvelleDecision(demandeCourante));
        }

    }

    /**
     * 
     * Regroupe les demandes pour cr�er les entit�s d�cisions
     * 
     * @param dates
     * @param session
     * @return Object[LinkedHashSet<RFDecisionData>,HashMap<String, Set<String[]>>]
     * @throws Exception
     */
    public Object[] genererDecisionsEntite() throws Exception {

        // Cr�ation d'un tableau contenant les idsQd
        Set<String> idsQd = new HashSet<String>();
        for (String key : cDemandes.keySet()) {
            RFImputationDemandesData demandeCourante = cDemandes.get(key);
            if (!JadeStringUtil.isBlankOrZero(demandeCourante.getIdQdPrincipale())) {
                idsQd.add(demandeCourante.getIdQdPrincipale());
            }
        }

        // Ajout des ids dosssiers concern�s par les Qds
        if (idsQd.size() > 0) {
            RFQdPrincipaleJointDossierManager rfQdPriJoiDosMgr = new RFQdPrincipaleJointDossierManager();
            rfQdPriJoiDosMgr.setSession(session);
            rfQdPriJoiDosMgr.setForIdsQd(idsQd);
            rfQdPriJoiDosMgr.changeManagerSize(0);
            rfQdPriJoiDosMgr.find(transaction);

            Iterator<RFQdPrincipaleJointDossier> rfQdPriJoiDosItr = rfQdPriJoiDosMgr.iterator();
            while (rfQdPriJoiDosItr.hasNext()) {
                RFQdPrincipaleJointDossier rfQdPriJoiDos = rfQdPriJoiDosItr.next();
                if (null != rfQdPriJoiDos) {
                    if (idQdIdsDossierMap.containsKey(rfQdPriJoiDos.getIdQdPrincipale())) {
                        Set<String[]> idsDossierSet = idQdIdsDossierMap.get(rfQdPriJoiDos.getIdQdPrincipale());
                        idsDossierSet.add(new String[] { rfQdPriJoiDos.getIdDossier(), rfQdPriJoiDos.getIdTiers(),
                                rfQdPriJoiDos.getCsTypeRelation() });
                    } else {
                        Set<String[]> idsDossierSet = new HashSet<String[]>();
                        idsDossierSet.add(new String[] { rfQdPriJoiDos.getIdDossier(), rfQdPriJoiDos.getIdTiers(),
                                rfQdPriJoiDos.getCsTypeRelation() });
                        idQdIdsDossierMap.put(rfQdPriJoiDos.getIdQdPrincipale(), idsDossierSet);
                    }
                }
            }
        }

        Collection<RFImputationDemandesData> demandes = cDemandes.values();
        for (RFImputationDemandesData demandeCourante : demandes) {
            ajoutDemande(demandeCourante);
            // System.out.println("idDemande: " + demandeCourante.idDemande + " dateDemande: "
            // + demandeCourante.dateDemande + " idQdP:" + demandeCourante.idQdPrincipale);
        }

        Object[] retour = new Object[] { decisions, idQdIdsDossierMap };

        return retour;
    }

    private RFDecisionData getNouvelleDecision(RFImputationDemandesData demandeCourante) throws Exception {

        RFDecisionData nouvelleDecision = new RFDecisionData();

        nouvelleDecision.setAnneeQD(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(demandeCourante.getDateDemande()));
        nouvelleDecision.setIdAdressePaiement(demandeCourante.getIdAdressePaiement());
        // L'id adresse domicile est m�j lors de la persistance
        nouvelleDecision.setIdAdresseDomicile("");
        nouvelleDecision.setNumeroDecision(getNumeroDecison());
        nouvelleDecision.setIdPreparePar(processusGestionnaire);
        nouvelleDecision.setIdGestionnaire(processusGestionnaire);
        nouvelleDecision.setCodeTypeDeSoin(demandeCourante.getCodeTypeDeSoin());
        nouvelleDecision.setCodeSousTypeDeSoin(demandeCourante.getCodeSousTypeDeSoin());
        nouvelleDecision.setIdExecutionProcess(idExecutionProcess);

        if (!JadeStringUtil.isBlankOrZero(demandeCourante.getIdQdPrincipale())) {
            nouvelleDecision.setIdQdPrincipale(demandeCourante.getIdQdPrincipale());

            // On set les ids des dossiers li�s � la Qd
            Set<String[]> idsDossierSet = idQdIdsDossierMap.get(demandeCourante.getIdQdPrincipale());

            if ((null != idsDossierSet) && (idsDossierSet.size() > 0)) {
                for (String[] idDossier : idsDossierSet) {
                    nouvelleDecision.getIdsDossier().put(idDossier[0], new String[] { idDossier[0], idDossier[1] });
                }
            } else {
                throw new Exception("Impossible de retrouver les dossiers de la Qd");
            }
        } else {
            nouvelleDecision.getIdsDossier().put(
                    demandeCourante.getIdDossier(),
                    new String[] { demandeCourante.getIdDossier(),
                            RFUtils.getIdTiersFromIdDossier(demandeCourante.getIdDossier(), session) });
        }

        nouvelleDecision.getIdDemandes().add(demandeCourante.getIdDemande());

        if (demandeCourante.isPaiementMensuel()) {
            nouvelleDecision.setPaiementMensuel(true);
        }

        return nouvelleDecision;
    }

    /**
     * G�n�re un nouveau num�ro de d�cision d�stin� � l'assur� (d�cision papier)
     * 
     * @return num�ro d�cison
     */
    private String getNumeroDecison() throws Exception {

        RFRetrieveNumeroDecisionService rfRetNumDecSer = new RFRetrieveNumeroDecisionService((BTransaction) transaction);
        return rfRetNumDecSer.getNumeroDecision(anneeCourante);

    }

    private boolean isCodeTypeDeSoinDecisionUnique(String codeTypeDeSoinDecision, String codeSousTypeDeSoinDecision,
            String codeSousTypeDeSoinDemandeCourante, String codeTypeDeSoinDemandeCourante) {
        // Si le code type de soin de la d�cision est �gale � celui de la demande courante (mis � part pour le code
        // 12, attention au 12.7) on ajoute la demande � la nouvelle d�cision. Ce premier test permet d'ajouter une
        // demande unique �
        // une d�cision unique Ex: d�c. courante => 15.1, demande => 15.2 -> m�me d�cision bien que le code 15 soit
        // unique
        boolean isMemeDecisionSelonCodesDecisionCourante = false;
        if (codeTypeDeSoinDecision.equals(codeTypeDeSoinDemandeCourante)) {
            if (codeTypeDeSoinDecision.equals(IRFCodeTypesDeSoins.TYPE_12_STRUCTURE_ET_SEJOURS)) {

                if (codeSousTypeDeSoinDemandeCourante.equals(codeSousTypeDeSoinDecision)) {
                    isMemeDecisionSelonCodesDecisionCourante = true;
                } else {
                    if (codeSousTypeDeSoinDecision
                            .equals(IRFCodeTypesDeSoins.SOUS_TYPE_12_7_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE)
                            || codeSousTypeDeSoinDemandeCourante
                                    .equals(IRFCodeTypesDeSoins.SOUS_TYPE_12_7_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE)) {
                        isMemeDecisionSelonCodesDecisionCourante = false;
                    } else {
                        isMemeDecisionSelonCodesDecisionCourante = true;
                    }
                }
            } else {
                isMemeDecisionSelonCodesDecisionCourante = true;
            }
        } else {
            isMemeDecisionSelonCodesDecisionCourante = false;
        }

        // Sinon on test si la demande est unique
        if (!isMemeDecisionSelonCodesDecisionCourante) {
            // Si la d�cision � un code unique on ne peut plus lui ajouter d'autres demandes
            if (isCodeTypeDeSoinUnique(codeTypeDeSoinDecision, codeSousTypeDeSoinDecision)) {
                return false;
            } else {
                // Sinon on regarde si la demande � un code unique
                if (isCodeTypeDeSoinUnique(codeTypeDeSoinDemandeCourante, codeSousTypeDeSoinDemandeCourante)) {
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    /**
     * Retourne vrai si le sous type de soin fait l'objet d'une d�cision unique
     * 
     * @param codeTypeDeSoin
     * @param CodeSousTypeDeSoin
     * @return
     */
    private boolean isCodeTypeDeSoinUnique(String codeTypeDeSoin, String CodeSousTypeDeSoin) {

        return (codeTypeDeSoin.equals(IRFCodeTypesDeSoins.TYPE_15_FRAIS_DE_TRAITEMENT_DENTAIRE)
                || (codeTypeDeSoin.equals(IRFCodeTypesDeSoins.TYPE_12_STRUCTURE_ET_SEJOURS) && CodeSousTypeDeSoin
                        .equals(IRFCodeTypesDeSoins.SOUS_TYPE_12_7_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE)) || (codeTypeDeSoin
                .equals(IRFCodeTypesDeSoins.TYPE_13_MAINTIEN_A_DOMICILE) && CodeSousTypeDeSoin
                .equals(IRFCodeTypesDeSoins.SOUS_TYPE_13_6_AIDE_AU_MENAGE_AVANCE)));

    }

    /**
     * Test si la demande est comprise dans la d�cison selon les crit�res suivant: - annee - gestionnaire - adresse de
     * paiement
     * 
     */
    private boolean isDemandeDansDecison(RFImputationDemandesData demandeCourante, RFDecisionData decision)
            throws Exception {

        return (isMemeAdressePaiement(decision.getIdAdressePaiement(), demandeCourante.getIdAdressePaiement())
                &&

                isMemeGestionnaire(decision.getIdGestionnaire(), demandeCourante.getIdGestionnaire())
                &&

                isMemeAnnee(decision.getAnneeQD(),
                        PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(demandeCourante.getDateDemande()))
                &&

                isCodeTypeDeSoinDecisionUnique(decision.getCodeTypeDeSoin(), decision.getCodeSousTypeDeSoin(),
                        demandeCourante.getCodeSousTypeDeSoin(), demandeCourante.getCodeTypeDeSoin()) &&

                isMemeQd(demandeCourante, decision) && isPaiementMensuel(demandeCourante, decision));

    }

    private boolean isMemeAdressePaiement(String idAdressePaiementDecision, String idAdressePaiementDemande) {
        return idAdressePaiementDecision.equals(idAdressePaiementDemande);
    }

    private boolean isMemeAnnee(String anneeDecision, String anneeDemande) {
        return anneeDecision.equals(anneeDemande);
    }

    private boolean isMemeGestionnaire(String idGestionnaireDecision, String idGestionnaireDemande) {
        return idGestionnaireDecision.equals(idGestionnaireDemande);
    }

    private boolean isMemeQd(RFImputationDemandesData demandeCourante, RFDecisionData decision) throws Exception {

        if (!JadeStringUtil.isBlankOrZero(demandeCourante.getIdQdPrincipale())) {
            // On test selon les idDossiers de la QdPrincipale

            if (!JadeStringUtil.isBlankOrZero(decision.getIdQdPrincipale())
                    && !decision.getIdQdPrincipale().equals(demandeCourante.getIdQdPrincipale())) {
                return false;
            }

            Set<String[]> idsDossierSet = idQdIdsDossierMap.get(demandeCourante.getIdQdPrincipale());

            if ((null != idsDossierSet) && (idsDossierSet.size() > 0)) {

                boolean isMemeQd = false;

                for (String idDossier[] : idsDossierSet) {
                    if (decision.getIdsDossier().containsKey(idDossier[0])) {
                        isMemeQd = true;
                        break;
                    }
                }

                // Si la d�cision n'a pas de Qd principale et la nouvelle demande en poss�de une, on met � jour les ids
                // dossier concernant la d�cision
                if (isMemeQd && JadeStringUtil.isBlankOrZero(decision.getIdQdPrincipale())) {
                    for (String idDossier[] : idsDossierSet) {
                        decision.getIdsDossier().put(idDossier[0], new String[] { idDossier[0], idDossier[1] });
                    }
                    decision.setIdQdPrincipale(demandeCourante.getIdQdPrincipale());
                }

                return isMemeQd;
            } else {
                throw new Exception("Impossible de retrouver les dossiers de la Qd");
            }

        } else {
            return decision.getIdsDossier().containsKey(demandeCourante.getIdDossier());
        }
    }

    private boolean isPaiementMensuel(RFImputationDemandesData demandeCourante, RFDecisionData decision) {

        if (decision.isPaiementMensuel()) {
            return false;
        } else {
            return !demandeCourante.isPaiementMensuel();
        }
    }

}
