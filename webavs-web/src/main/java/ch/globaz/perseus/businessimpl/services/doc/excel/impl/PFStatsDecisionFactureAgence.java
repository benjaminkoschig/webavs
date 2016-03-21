package ch.globaz.perseus.businessimpl.services.doc.excel.impl;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.op.common.merge.IMergingContainer;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import globaz.webavs.common.CommonExcelmlContainer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.perseus.business.exceptions.doc.DocException;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.models.qd.Facture;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import com.sun.xml.internal.fastinfoset.util.StringArray;

public class PFStatsDecisionFactureAgence extends PerseusAbstractExcelServiceImpl {

    public final static String MODEL_NAME = "statsDecisionFactureAgence.xml";
    public final static String MODEL_NAME_AGENCE = "statsDecisionFactureParAgence.xml";
    private CommonExcelmlContainer container;
    private String outPutName = "stats_decision_factures_agences";

    // Valeurs de l'entete de la feuille des statistiques
    private String processusName = null;
    private String lotName = null;
    private String utilisateur = null;
    private String dateHeure = null;
    private String mail = null;
    private String dateDocument = null;
    private List<String> listeDAgence = null;	// Le nombre d'agences définit le nombre de sous-section dans les stats
    private int nbFacturesTotal = 0;

    private String idMembreFamille = null;
    private String idDossier = null;
    private String annee = null;
    private String nss;

    private Dossier dossier = null;
    private Facture facture = null;

    private List<Facture> factures = null;
    private Map<String, List<Facture>> listeDecomptes = null;

    private boolean isStatParAgence = false;
    private int index = 0; // index pour itérer sur la liste des agences

    private Map<String, List<StatsDecisionFactureAgenceDonnee>> donneeStats;

    private boolean isStatParAgence() {
        return isStatParAgence;
    }

    private void setStatParAgence(boolean isStatParAgence) {
        this.isStatParAgence = isStatParAgence;
    }

    private int getNbFacturesTotal() {
        return nbFacturesTotal;
    }

    private void setNbFacturesTotal(int nbFacturesTotal) {
        this.nbFacturesTotal = nbFacturesTotal;
    }

    private List<Facture> getFactures() {
        return factures;
    }

    private void setFactures(List<Facture> factures) {
        this.factures = factures;
    }

    private void setAnnee(String annee) {
        this.annee = annee;
    }

    private void setIdMembreFamille(String idMembreFamille) {
        this.idMembreFamille = idMembreFamille;
    }

    private void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    private String getIdDossier() {
        return idDossier;
    }

    public PFStatsDecisionFactureAgence() {

    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    @Override
    public String getModelName() {
        if (isStatParAgence()) {
            return PFStatsDecisionFactureAgence.MODEL_NAME_AGENCE;
        } else {
            return PFStatsDecisionFactureAgence.MODEL_NAME;
        }
    }

    @Override
    public String getOutPutName() {
        return outPutName;
    }

    private void setOutPutName(String output) {
        outPutName = output;
    }

    @Override
    public IMergingContainer loadResults() throws Exception {
        container = new CommonExcelmlContainer();

        if (!isStatParAgence) {

            Calendar calendar = Calendar.getInstance();
            dateHeure = calendar.get(Calendar.DAY_OF_MONTH)
                    + "."
                    + (calendar.get(Calendar.MONTH) + 1)
                    + "."
                    + calendar.get(Calendar.YEAR)
                    + " / "
                    + calendar.get(Calendar.HOUR_OF_DAY)
                    + "h"
                    + (calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : calendar
                            .get(Calendar.MINUTE));

            donneeStats = new HashMap<String, List<StatsDecisionFactureAgenceDonnee>>();

            if (listeDecomptes != null) {
                Set keys = listeDecomptes.keySet();
                for (java.util.Iterator iterator = keys.iterator(); iterator.hasNext();) {
                    String myKey = (String) iterator.next();

                    setFactures(listeDecomptes.get(myKey));
                    nbFacturesTotal += getFactures().size();

                    // Extraction des infos de la key [idDossier, idMembreFamille, année]
                    List<String> liste = JadeStringUtil.split(myKey, ",");
                    setIdMembreFamille(liste.get(1).replace(",", ""));

                    setIdDossier(liste.get(0).replace(",", ""));
                    setDossier(PerseusServiceLocator.getDossierService().read(getIdDossier()));

                    for (Facture fac : factures) {
                        String keyIdGestionnaire = fac.getSimpleFacture().getIdGestionnaire();

                        if (listeDAgence.contains(keyIdGestionnaire)) {
                            if (!donneeStats.containsKey(keyIdGestionnaire)) {
                                donneeStats.put(keyIdGestionnaire, new ArrayList<StatsDecisionFactureAgenceDonnee>());
                            }

                            StatsDecisionFactureAgenceDonnee agenceDonnees = new StatsDecisionFactureAgenceDonnee();

                            agenceDonnees.setNoDecision(fac.getSimpleFacture().getNumDecision());
                            agenceDonnees.setMontantFacture(fac.getSimpleFacture().getMontant());

                            agenceDonnees.setDateNaissanceAyantDroit(fac.getQd().getMembreFamille()
                                    .getPersonneEtendue().getPersonne().getDateNaissance());
                            agenceDonnees.setNomAyantDroit(fac.getQd().getMembreFamille().getPersonneEtendue()
                                    .getTiers().getDesignation1()
                                    + " "
                                    + fac.getQd().getMembreFamille().getPersonneEtendue().getTiers().getDesignation2());
                            agenceDonnees.setNssAyantDroit(fac.getQd().getMembreFamille().getPersonneEtendue()
                                    .getPersonneEtendue().getNumAvsActuel());

                            agenceDonnees.setDateNaissanceRequerant(fac.getQd().getQdAnnuelle().getDossier()
                                    .getDemandePrestation().getPersonneEtendue().getPersonne().getDateNaissance());
                            agenceDonnees.setNomRequerant(fac.getQd().getQdAnnuelle().getDossier()
                                    .getDemandePrestation().getPersonneEtendue().getTiers().getDesignation1()
                                    + " "
                                    + fac.getQd().getQdAnnuelle().getDossier().getDemandePrestation()
                                            .getPersonneEtendue().getTiers().getDesignation2());
                            agenceDonnees
                                    .setNssRequerant(fac.getQd().getQdAnnuelle().getDossier().getDemandePrestation()
                                            .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel());

                            donneeStats.get(keyIdGestionnaire).add(agenceDonnees);
                        }
                    }
                }
            }
            remplireEnteteDocStatExcel();
        } else {
            remplireCorpsDocStattExcel();
        }

        return container;
    }

    /**
     * Remplit le document MODEL_NAME = "statsDecisionFactureAgence.xml"
     * Ce document est généré une et une seule fois.
     */
    private void remplireEnteteDocStatExcel() {
        container.put("processus", processusName);
        container.put("lot", lotName);
        container.put("utilisateur", utilisateur);
        container.put("dateHeure", dateHeure);
        container.put("mail", mail);
        container.put("dateDocument", dateDocument);

        for (String key : donneeStats.keySet()) {
            container.put("caisse", key);
            container.put("facturesparcaisse", String.valueOf(donneeStats.get(key).size()));
        }
        for (String key : listeDAgence) {
            if (!donneeStats.containsKey(key)) {
                container.put("caisse", key);
                container.put("facturesparcaisse", "0");
            }
        }

        container.put("nbTotalFactures", String.valueOf(nbFacturesTotal));
    }

    /**
     * Remplit le document MODEL_NAME_AGENCE = "statsDecisionFactureParAgence.xml"
     * Ce document est généré une fois par agence. Si trois agences sont sélectionnés alors on génére trois fichier.
     * Le nom de fichiers contient le nom de l'agence pour laquelle les statistiques ont été générées.
     */
    private void remplireCorpsDocStattExcel() {

        String agenceEnTraitement = listeDAgence.get(index++);
        List<StatsDecisionFactureAgenceDonnee> facturesPourUneAgence = donneeStats.get(agenceEnTraitement);

        if ((facturesPourUneAgence != null) && (!facturesPourUneAgence.isEmpty())) {

            container.put("factureAgence", agenceEnTraitement);
            container.put("nombreFacturesAgence", String.valueOf(facturesPourUneAgence.size()));

            for (StatsDecisionFactureAgenceDonnee uneFacture : facturesPourUneAgence) {

                container.put("no_decision", uneFacture.getNoDecision());
                container.put("requerant", uneFacture.getNssRequerant() + " / " + uneFacture.getNomRequerant() + " / "
                        + uneFacture.getDateNaissanceRequerant());
                container.put("beneficiaire", uneFacture.getNssAyantDroit() + " / " + uneFacture.getNomAyantDroit()
                        + " / " + uneFacture.getDateNaissanceAyantDroit());
                container.put("montant", uneFacture.getMontantFacture());
            }
        } else {
            container.put("factureAgence", agenceEnTraitement);
            container.put("nombreFacturesAgence", "0");

            container.put("no_decision", "-");
            container.put("requerant", "-");
            container.put("beneficiaire", "-");
            container.put("montant", "0");
        }
    }

    /**
     * Prépare le document MODEL_NAME_AGENCE = "statsDecisionFactureParAgence.xml"
     * 
     * @return
     */
    public String createGeneralDocAndSave() throws Exception {

        if ((listeDAgence == null)) {
            throw new DocException("Unable to execute createDoc, the idUserAgence is null !");
        }

        ExcelmlWorkbook wk = createDoc();
        setOutPutName(getOutPutName() + "_" + JadeUUIDGenerator.createStringUUID() + ".xml");

        return save(wk, getOutPutName());
    }

    /**
     * Prépare le document MODEL_NAME_AGENCE = "statsDecisionFactureParAgence.xml"
     * 
     * @return
     * @throws Exception
     */
    public StringArray createStatsParAgenceAndSave() throws Exception {
        setStatParAgence(true);
        if ((listeDAgence == null)) {
            throw new DocException("Unable to execute createDoc, the idUserAgence is null !");
        }

        StringArray listeNomFichiers = new StringArray();

        for (String uneAgence : listeDAgence) {
            ExcelmlWorkbook wk = createDoc();
            setOutPutName("statsParAgence_" + uneAgence + "_" + JadeUUIDGenerator.createStringUUID() + ".xml");
            listeNomFichiers.add(save(wk, getOutPutName()));
        }

        return listeNomFichiers;
    }

    /**
     * Retourne la session
     * 
     * @return
     */
    protected BSession getSession() {
        return BSessionUtil.getSessionFromThreadContext();
    }

    public void setDonnesPourStatsAgence(List<String> listeDAgence, Map<String, List<Facture>> listeDecomptes,
            String lotDescription, String dateDocument, String mail) {
        this.listeDAgence = listeDAgence;

        if (!listeDecomptes.isEmpty()) {
            this.listeDecomptes = listeDecomptes;
        }

        lotName = lotDescription;
        this.dateDocument = dateDocument;
        this.mail = mail;

        processusName = "Impression décision de facture";
        utilisateur = getSession().getUserFullName();
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

}
