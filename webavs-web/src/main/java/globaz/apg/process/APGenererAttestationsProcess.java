/*
 * Crée le 21 septembre 2006
 */
package globaz.apg.process;

import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.api.prestation.IAPRepartitionPaiements;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.prestation.*;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.itext.APAttestations;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author hpe
 * 
 */
public class APGenererAttestationsProcess extends BProcess {

    // ~ Instance fiels
    // -------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // Classe interne pour les infos repris des requêtes pour le tri par tiers
    public class AttestationsInfos implements Comparable {
        public String dateDebut = "";
        public String dateFin = "";
        public Set idsRPVentilations = new TreeSet();
        public String idTiers = "";
        public boolean isMaternite = false;
        public BigDecimal montantTotal = new BigDecimal(0);
        public BigDecimal montantVentilations = new BigDecimal(0);
        public BigDecimal totalMontantAPG = new BigDecimal(0);
        public BigDecimal totalMontantCotisations = new BigDecimal(0);
        public BigDecimal totalMontantImpotSource = new BigDecimal(0);

        @Override
        public int compareTo(Object o) {
            AttestationsInfos ai = (AttestationsInfos) o;

            if (dateDebut.compareTo(ai.dateDebut) != 0) {
                return dateDebut.compareTo(ai.dateDebut);
            } else if (dateFin.compareTo(ai.dateFin) != 0) {
                return dateFin.compareTo(ai.dateFin);
            } else {
                return 0;
            }
        }

    }

    // Classe interne qui défini les clés pour le tri par idTiers
    public class Key implements Comparable {
        public String idTiers;

        @Override
        public int compareTo(Object o) {
            if (o instanceof Key) {
                return Integer.parseInt(idTiers) - Integer.parseInt(((Key) o).idTiers);
            } else {
                return -1;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj != null && obj instanceof Key) {
                return (Integer.parseInt(idTiers) - Integer.parseInt(((Key) obj).idTiers) == 0);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Integer.parseInt(idTiers);
        }
    }

    private static final String ORDER_PRINTING_BY = "orderPrintingBy";
    private String annee = "";
    private String eMailObject = "";
    // private String montantVentilation = "";
    private String idTiers = "";
    private Boolean isGenerationUnique = Boolean.TRUE;
    private Boolean isSendToGed = Boolean.FALSE;

    TreeMap map = new TreeMap();

    private JadePublishDocumentInfo mergedDocInfo = null;
    private String montantTotal = "";
    private String NSS = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private PRTiersWrapper tiers;

    private String totalMontantAPG = "";

    private String typePrestation = "";

    /**
     * Crée une nouvelle instance de la classe APGenererAttestationsProcess.
     */
    public APGenererAttestationsProcess() {
        super();
    }

    // ~ Methods
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APGenererAttestationsProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public APGenererAttestationsProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe APGenererAttestationsProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public APGenererAttestationsProcess(BSession session) {
        super(session);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        // Reprendre l'année setter par l'utilisateur et transformer en :
        // --> 1.1.XXXX pour dateDebut
        // --> 31.12.XXXX pour dateFin
        String annee = getAnnee();
        String dateDebut = annee + "0101";
        String dateFin = annee + "1231";

        APRepartitionJointPrestationJointLotDemandeManager mgr = new APRepartitionJointPrestationJointLotDemandeManager();

        // Prendre les répartitions qui ont été comptabilisées durant l'année
        // passée en paramètre

        mgr.setSession(getSession());
        mgr.setForDateDebutComptaLot(dateDebut);
        mgr.setForDateFinComptaLot(dateFin);
        mgr.setForEtatPrestation(IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF);
        mgr.setForTypeDemande(typePrestation);
        mgr.setOrderBy(APPrestation.FIELDNAME_DATEDEBUT + "," + APPrestation.FIELDNAME_DATEFIN);

        List<String> typeDePrestations = new ArrayList<>();

        if (getIsGenerationUnique().booleanValue()) {
            tiers = PRTiersHelper.getTiers(getSession(), getNSS());
            if (null == tiers) {
                getMemoryLog().logMessage(getSession().getLabel("ERREUR_TIERS_PAS_TROUVE") + " " + getNSS(),
                        FWViewBeanInterface.ERROR, "APGenererAttestationsProcess");
                setEMailObject(getSession().getLabel("EMAIL_OBJECT_ATT_FISCALES_1"));
                abort();
                return false;
            }
            mgr.setForIdTiers(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        }

        BStatement statement = mgr.cursorOpen(getTransaction());

        APRepartitionJointPrestation repPres = null;

        int nbRep = 0;

        while ((repPres = (APRepartitionJointPrestation) (mgr.cursorReadNext(statement))) != null) {

            nbRep++;

            // Reprendre chaque répartition
            APRepartitionPaiements rep = new APRepartitionPaiements();
            rep.setIdRepartitionBeneficiairePaiement(repPres.getIdRepartitionBeneficiairePaiement());
            rep.setSession(getSession());
            rep.retrieve();

            // recherche la sit. pro.
            APSituationProfessionnelle sitPro = new APSituationProfessionnelle();
            sitPro.setSession(getSession());
            sitPro.setIdSituationProf(rep.getIdSituationProfessionnelle());
            sitPro.retrieve();

            // Si pas une ventilation,
            // et (un payement direct ou un payement a l'employeur pour un
            // independant)
            if (JadeStringUtil.isIntegerEmpty(rep.getIdParent())
                    && (IAPRepartitionPaiements.CS_PAIEMENT_DIRECT.equals(rep.getTypePaiement()) || sitPro
                            .getIsIndependant().booleanValue())) {

                montantTotal = rep.getMontantRestant();
                totalMontantAPG = rep.getMontantBrut();
                idTiers = repPres.getIdTiers();

                // Rechercher les ventilations pour cette répartition
                APRepartitionPaiementsManager repartitions = new APRepartitionPaiementsManager();

                repartitions.setForIdParent(rep.getIdRepartitionBeneficiairePaiement());
                repartitions.setNotForIdRepartition(rep.getIdRepartitionBeneficiairePaiement());
                repartitions.setSession(getSession());
                // montantVentilation = (repartitions.getSum("VIMMOV",
                // getTransaction())).toString();

                APPrestation prest = new APPrestation();
                prest.setSession(getSession());
                prest.setIdPrestationApg(rep.getIdPrestationApg());
                prest.retrieve(getTransaction());

                Set idsVentilation = new TreeSet();
                FWCurrency montantVentilation = new FWCurrency(0);
                repartitions.find(getTransaction());
                for (int i = 0; i < repartitions.size(); i++) {
                    APRepartitionPaiements elm = (APRepartitionPaiements) repartitions.get(i);

                    if (!idsVentilation.contains(elm.getIdRepartitionBeneficiairePaiement())) {
                        montantVentilation.add(elm.getMontantVentile());
                        idsVentilation.add(elm.getIdRepartitionBeneficiairePaiement());
                    }
                }

                // Reprendre les cotisations pour la répartition
                APCotisationManager cotMan = new APCotisationManager();
                cotMan.setForIdRepartitionBeneficiairePaiement(rep.getIdRepartitionBeneficiairePaiement());
                cotMan.setSession(getSession());
                cotMan.find();

                FWCurrency totalMontantCotisations = new FWCurrency();
                FWCurrency totalMontantImpotSource = new FWCurrency();

                for (Iterator iterator = cotMan.iterator(); iterator.hasNext();) {
                    APCotisation cot = (APCotisation) iterator.next();

                    if (cot.getType().equals(APCotisation.TYPE_IMPOT)) {
                        totalMontantImpotSource.add(cot.getMontant());
                    } else {
                        totalMontantCotisations.add(cot.getMontant());
                    }

                }

                boolean isRestitution = false;
                if (Double.parseDouble(totalMontantAPG) >= 0) {
                    isRestitution = false;
                } else {
                    isRestitution = true;
                }

                if (((Double.parseDouble(totalMontantAPG) != 0) &&
                        ((!totalMontantCotisations.isZero())
                                || (!totalMontantImpotSource.isZero())
                                || APTypeDePrestation.LAMAT.isCodeSystemEqual(prest.getGenre())))
                        && ((((!isRestitution) && (totalMontantCotisations.isNegative())) || ((isRestitution) && (totalMontantCotisations
                                .isPositive()))) || ((!isRestitution) && (totalMontantImpotSource.isNegative())) || ((isRestitution) && (totalMontantImpotSource
                                .isPositive()) || APTypeDePrestation.LAMAT.isCodeSystemEqual(prest.getGenre())))) {

                    // Création de la clé
                    Key k = new Key();
                    k.idTiers = rep.getIdTiers();

                    // Si la clé est encore inexistante
                    if (!map.containsKey(k)) {

                        // On crée un objet
                        AttestationsInfos ai = new AttestationsInfos();

                        ai.idTiers = idTiers;
                        ai.dateDebut = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(prest.getDateDebut());
                        ai.dateFin = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(prest.getDateFin());
                        ai.totalMontantAPG = ai.totalMontantAPG.add(new BigDecimal(totalMontantAPG.toString()));
                        ai.totalMontantCotisations = ai.totalMontantCotisations.add(new BigDecimal(
                                totalMontantCotisations.toString()));
                        ai.totalMontantImpotSource = ai.totalMontantImpotSource.add(new BigDecimal(
                                totalMontantImpotSource.toString()));
                        ai.montantTotal = ai.montantTotal.add(new BigDecimal(montantTotal.toString()));
                        ai.montantVentilations = ai.montantVentilations.add(new BigDecimal(montantVentilation
                                .toString()));
                        ai.isMaternite = IPRDemande.CS_TYPE_MATERNITE.equals(typePrestation);

                        ai.idsRPVentilations = idsVentilation;
                        // Comme la clé est inexistante, on crée la liste
                        // d'objet
                        ArrayList list = new ArrayList();
                        list.add(ai);

                        // On insère la clé et la liste dans la map
                        map.put(k, list);

                        // si la clé existe déjà
                    } else {

                        // On récupère la liste
                        ArrayList list = (ArrayList) map.get(k);

                        ArrayList listCopy = new ArrayList();
                        listCopy.addAll(list);

                        boolean isFusion = false;

                        // On itère sur les objets dans la liste
                        for (Iterator iterator = listCopy.iterator(); iterator.hasNext();) {
                            AttestationsInfos ai = (AttestationsInfos) iterator.next();

                            ai.idsRPVentilations.addAll(idsVentilation);
                            // Dans un premier temps, on regroupe uniquement les
                            // périodes identiques

                            // date de l'objet en cours d'itération
                            String dateDebutAi = PRDateFormater.formatDateFrom(ai.dateDebut);
                            String dateFinAi = PRDateFormater.formatDateFrom(ai.dateFin);

                            // date de l'objet à fusionner ou ajouter
                            String dateDebutPrest = prest.getDateDebut();
                            String dateFinPrest = prest.getDateFin();

                            // Donc si les dates sont identiques...
                            if (/* ai.idsRPVentilations.size()>0 && */dateDebutPrest.equals(dateDebutAi)
                                    && dateFinPrest.equals(dateFinAi)) {

                                // on ajoute simplement tous les montants à
                                // l'objet ai
                                ai.idTiers = idTiers;
                                ai.dateDebut = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(dateDebutPrest);
                                ai.dateFin = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(dateFinPrest);
                                ai.totalMontantAPG = ai.totalMontantAPG.add(new BigDecimal(totalMontantAPG.toString()));
                                ai.totalMontantCotisations = ai.totalMontantCotisations.add(new BigDecimal(
                                        totalMontantCotisations.toString()));
                                ai.totalMontantImpotSource = ai.totalMontantImpotSource.add(new BigDecimal(
                                        totalMontantImpotSource.toString()));
                                ai.montantTotal = ai.montantTotal.add(new BigDecimal(montantTotal.toString()));
                                ai.montantVentilations = ai.montantVentilations.add(new BigDecimal(montantVentilation
                                        .toString()));
                                ai.isMaternite = IPRDemande.CS_TYPE_MATERNITE.equals(typePrestation);

                                // Pas besoin de l'ajouter dans la liste, car
                                // modifié en temps réel !

                                // Sinon on crée un nouvel objet

                                isFusion = true;

                            }
                        }

                        if (!isFusion) {

                            AttestationsInfos ai1 = new AttestationsInfos();

                            ai1.idTiers = idTiers;
                            ai1.dateDebut = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(prest.getDateDebut());
                            ai1.dateFin = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(prest.getDateFin());
                            ai1.totalMontantAPG = ai1.totalMontantAPG.add(new BigDecimal(totalMontantAPG.toString()));
                            ai1.totalMontantCotisations = ai1.totalMontantCotisations.add(new BigDecimal(
                                    totalMontantCotisations.toString()));
                            ai1.totalMontantImpotSource = ai1.totalMontantImpotSource.add(new BigDecimal(
                                    totalMontantImpotSource.toString()));
                            ai1.montantTotal = ai1.montantTotal.add(new BigDecimal(montantTotal.toString()));
                            ai1.montantVentilations = ai1.montantVentilations.add(new BigDecimal(montantVentilation
                                    .toString()));
                            ai1.isMaternite = IPRDemande.CS_TYPE_MATERNITE.equals(typePrestation);
                            ai1.idsRPVentilations = idsVentilation;
                            list.add(ai1);

                        }

                    }
                }
            }
        }

        if (nbRep == 0) {
            if (getIsGenerationUnique().booleanValue()) {
                getMemoryLog().logMessage(getSession().getLabel("ERREUR_AUCUNE_ATT_POUR_TIERS"),
                        FWViewBeanInterface.WARNING, "APGenererAttestationsFiscales");
                setEMailObject(getSession().getLabel("EMAIL_OBJECT_ATT_FISCALES_2") + " "
                        + tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                        + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
            } else {
                getMemoryLog().logMessage(getSession().getLabel("ERREUR_AUCUNE_ATT_POUR_ANNEE"),
                        FWViewBeanInterface.WARNING, "APGenererAttestationsFiscales");
                setEMailObject(getSession().getLabel("EMAIL_OBJECT_ATT_FISCALES_3") + " " + getAnnee());

            }
        }

        APAttestations attestations = new APAttestations(getSession());
        attestations.setAttestationsMap(map);
        attestations.setDateDebut(dateDebut);
        attestations.setDateFin(dateFin);
        attestations.setAnnee(annee);
        attestations.setParent(this);
        attestations.setTailleLot(1);
        attestations.setIsSendToGED(getIsSendToGed());
        attestations.setType(typePrestation);
        attestations.executeProcess();

        // mergedDocInfo = createDocumentInfo();
        //
        // if (getIsGenerationUnique().booleanValue()){
        // mergedDocInfo.setDocumentSubject("Attestation pour "
        // +tiers.getProperty(PRTiersWrapper.PROPERTY_NOM)+" "
        // +tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM)+
        // " imprimée avec succès"
        // );
        // } else {
        // mergedDocInfo.setDocumentSubject("Attestations fiscales pour l'année "+annee);
        // }
        //
        // mergePDF(mergedDocInfo,
        // true,
        // 100,
        // false,
        // ORDER_PRINTING_BY);

        return true;

    }

    public String getAnnee() {
        return annee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return eMailObject;
    }

    /**
     * @return
     */
    public Boolean getIsGenerationUnique() {
        return isGenerationUnique;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    /**
     * @return
     */
    public String getNSS() {
        return NSS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setEMailObject(String mailObject) {
        eMailObject = mailObject;
    }

    /**
     * @param boolean1
     */
    public void setIsGenerationUnique(Boolean boolean1) {
        isGenerationUnique = boolean1;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    /**
     * @param string
     */
    public void setNSS(String string) {
        NSS = string;
    }

    public String getTypePrestation() {
        return typePrestation;
    }

    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }
}
