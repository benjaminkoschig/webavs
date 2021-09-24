/*
 * Crée le 6 septembre 2006
 */
package globaz.ij.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JAException;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.api.prestations.IIJRepartitionPaiements;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prestations.IJCotisation;
import globaz.ij.db.prestations.IJCotisationManager;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJRepartitionJointPrestation;
import globaz.ij.db.prestations.IJRepartitionJointPrestationJointLotManager;
import globaz.ij.db.prestations.IJRepartitionPaiements;
import globaz.ij.db.prestations.IJRepartitionPaiementsManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.itext.IJAttestations;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author hpe
 * 
 */
public class IJGenererAttestationsProcess extends BProcess {

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
        public String idBaseInd = "";
        public Set idsRPVentilations = new TreeSet();
        public BigDecimal montantTotal = new BigDecimal(0);
        public BigDecimal montantVentilations = new BigDecimal(0);
        public BigDecimal totalMontantCotisations = new BigDecimal(0);
        public BigDecimal totalMontantIJ = new BigDecimal(0);
        public BigDecimal totalMontantImpotSource = new BigDecimal(0);

        @Override
        public int compareTo(Object o) {
            if (o instanceof AttestationsInfos) {
                return Integer.parseInt(dateDebut) - Integer.parseInt(((AttestationsInfos) o).dateDebut);
            } else {
                return -1;
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
            if ((obj != null) && (obj instanceof Key)) {
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
    private String idBaseInd = "";
    private Boolean isGenerationUnique = Boolean.TRUE;
    private Boolean isSendToGed = Boolean.FALSE;

    TreeMap map = new TreeMap();

    private JadePublishDocumentInfo mergedDocInfo = null;
    private String montantTotal = "";
    private String NSS = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private PRTiersWrapper tiers;

    private String totalMontantIJ = "";

    /**
     * Crée une nouvelle instance de la classe IJGenererAttestationsProcess.
     */
    public IJGenererAttestationsProcess() {
        super();
    }

    // ~ Methods
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJGenererAttestationsProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public IJGenererAttestationsProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe IJGenererAttestationsProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public IJGenererAttestationsProcess(BSession session) {
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

        IJRepartitionJointPrestationJointLotManager mgr = new IJRepartitionJointPrestationJointLotManager();

        // Prendre les répartitions qui ont été comptabilisées durant l'année
        // passée en paramètre

        mgr.setSession(getSession());
        mgr.setForDateDebutComptaLot(dateDebut);
        mgr.setForDateFinComptaLot(dateFin);
        mgr.setForEtatPrestation(IIJPrestation.CS_DEFINITIF);
        mgr.setOrderBy(IJPrestation.FIELDNAME_DATEDEBUT + "," + IJPrestation.FIELDNAME_DATEFIN);

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

        IJRepartitionJointPrestation repPres = null;

        int nbRep = 0;

        while ((repPres = (IJRepartitionJointPrestation) (mgr.cursorReadNext(statement))) != null) {

            nbRep++;

            // Reprendre chaque répartition
            IJRepartitionPaiements rep = new IJRepartitionPaiements();
            rep.setIdRepartitionPaiement(repPres.getIdRepartitionPaiement());
            rep.setSession(getSession());
            rep.retrieve();

            // recherche le prononce
            IJPrononce prononce = new IJPrononce();
            prononce.setSession(getSession());
            prononce.setIdPrononce(repPres.getIdPrononce());
            prononce.retrieve();

            // Si pas une ventilation,
            // et (un payement direct ou un payement a l'employeur pour un
            // independant)
            if (JadeStringUtil.isIntegerEmpty(rep.getIdParent())
                    && (IIJRepartitionPaiements.CS_PAIEMENT_DIRECT.equals(rep.getTypePaiement()) || IIJPrononce.CS_INDEPENDANT
                            .equals(prononce.getCsStatutProfessionnel()))) {

                montantTotal = rep.getMontantRestant();
                totalMontantIJ = rep.getMontantBrut();
                idBaseInd = repPres.getIdBaseIndemnisation();

                IJPrestation prest = new IJPrestation();
                prest.setSession(getSession());
                prest.setIdPrestation(rep.getIdPrestation());
                prest.retrieve(getTransaction());

                /*
                 * IJBaseIndemnisation bi = new IJBaseIndemnisation(); bi.setSession(getSession());
                 * bi.setIdBaseIndemisation(prest.getIdBaseIndemnisation()); bi.retrieve(getTransaction());
                 * 
                 * boolean isVentilationATraiter = true; //On ne traite pas les ventilations pour des bases
                 * d'indemnistations annulées !!! if (IIJBaseIndemnisation.CS_ANNULE.equals(bi.getCsEtat())) { continue;
                 * //isVentilationATraiter = false; }
                 */
                Set idsVentilation = new TreeSet();
                FWCurrency montantVentilation = new FWCurrency(0);
                IJRepartitionPaiementsManager repartitions = new IJRepartitionPaiementsManager();
                repartitions.setForIdParent(rep.getIdRepartitionPaiement());
                repartitions.setNotForIdRepartitionPaiement(rep.getIdRepartitionPaiement());
                repartitions.setSession(getSession());

                repartitions.find(getTransaction());

                for (int i = 0; i < repartitions.size(); i++) {
                    IJRepartitionPaiements elm = (IJRepartitionPaiements) repartitions.get(i);

                    if (!idsVentilation.contains(elm.getIdRepartitionPaiement())) {
                        montantVentilation.add(elm.getMontantVentile());
                        idsVentilation.add(elm.getIdRepartitionPaiement());
                    }
                }

                // Reprendre les cotisations pour la répartition
                IJCotisationManager cotMan = new IJCotisationManager();
                cotMan.setForIdRepartitionPaiements(rep.getIdRepartitionPaiement());
                cotMan.setSession(getSession());
                cotMan.find();

                FWCurrency totalMontantCotisations = new FWCurrency();
                FWCurrency totalMontantImpotSource = new FWCurrency();

                for (Iterator iterator = cotMan.iterator(); iterator.hasNext();) {
                    IJCotisation cot = (IJCotisation) iterator.next();

                    if (cot.getIsImpotSource().booleanValue()) {
                        totalMontantImpotSource.add(cot.getMontant());
                    } else {
                        totalMontantCotisations.add(cot.getMontant());
                    }

                }

                boolean isRestitution = false;
                if (Double.parseDouble(totalMontantIJ) >= 0) {
                    isRestitution = false;
                } else {
                    isRestitution = true;
                }

                if (((Double.parseDouble(totalMontantIJ) != 0) && ((!totalMontantCotisations.isZero()) || (!totalMontantImpotSource
                        .isZero())))
                        && ((((!isRestitution) && (totalMontantCotisations.isNegative())) || ((isRestitution) && (totalMontantCotisations
                                .isPositive()))) || ((!isRestitution) && (totalMontantImpotSource.isNegative())) || ((isRestitution) && (totalMontantImpotSource
                                .isPositive())))) {

                    // Création de la clé
                    Key k = new Key();
                    k.idTiers = rep.getIdTiers();

                    // Si la clé est encore inexistante
                    if (!map.containsKey(k)) {

                        createAttestationInfoAndPutInMap(prest, idsVentilation, montantVentilation, totalMontantCotisations, totalMontantImpotSource, k);

                    } else { // si la clé existe déjà

                        putAttestationInfoInList(prest, idsVentilation, montantVentilation, totalMontantCotisations, totalMontantImpotSource, k);

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

        // génère les attestations originales
        createAttestation(annee, dateDebut, dateFin);

        return true;
    }

    private void putAttestationInfoInList(IJPrestation prest, Set idsVentilation, FWCurrency montantVentilation, FWCurrency totalMontantCotisations, FWCurrency totalMontantImpotSource, Key k) throws JAException {
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

            // Donc si les dates sont identiques
            if (/* (ai.idsRPVentilations.size() == 0) && */dateDebutPrest.equals(dateDebutAi)
                    && dateFinPrest.equals(dateFinAi)) {

                // on ajoute simplement tous les montants à
                // l'objet ai
                ai.idBaseInd = idBaseInd;
                ai.dateDebut = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(dateDebutPrest);
                ai.dateFin = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(dateFinPrest);
                ai.totalMontantIJ = ai.totalMontantIJ.add(new BigDecimal(totalMontantIJ.toString()));
                ai.totalMontantCotisations = ai.totalMontantCotisations.add(new BigDecimal(
                        totalMontantCotisations.toString()));
                ai.totalMontantImpotSource = ai.totalMontantImpotSource.add(new BigDecimal(
                        totalMontantImpotSource.toString()));
                ai.montantTotal = ai.montantTotal.add(new BigDecimal(montantTotal.toString()));
                ai.montantVentilations = ai.montantVentilations.add(new BigDecimal(montantVentilation
                        .toString()));

                isFusion = true;

            }
        }

        if (!isFusion) {

            AttestationsInfos ai1 = new AttestationsInfos();

            ai1.idBaseInd = idBaseInd;
            ai1.dateDebut = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(prest.getDateDebut());
            ai1.dateFin = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(prest.getDateFin());
            ai1.totalMontantIJ = ai1.totalMontantIJ.add(new BigDecimal(totalMontantIJ.toString()));
            ai1.totalMontantCotisations = ai1.totalMontantCotisations.add(new BigDecimal(
                    totalMontantCotisations.toString()));
            ai1.totalMontantImpotSource = ai1.totalMontantImpotSource.add(new BigDecimal(
                    totalMontantImpotSource.toString()));
            ai1.montantTotal = ai1.montantTotal.add(new BigDecimal(montantTotal.toString()));
            ai1.montantVentilations = ai1.montantVentilations.add(new BigDecimal(montantVentilation
                    .toString()));
            ai1.idsRPVentilations = idsVentilation;

            list.add(ai1);
        }
    }

    private void createAttestationInfoAndPutInMap(IJPrestation prest, Set idsVentilation, FWCurrency montantVentilation, FWCurrency totalMontantCotisations, FWCurrency totalMontantImpotSource, Key k) throws JAException {
        // On crée un objet
        AttestationsInfos ai = new AttestationsInfos();

        ai.idBaseInd = idBaseInd;
        ai.dateDebut = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(prest.getDateDebut());
        ai.dateFin = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(prest.getDateFin());
        ai.totalMontantIJ = ai.totalMontantIJ.add(new BigDecimal(totalMontantIJ.toString()));
        ai.totalMontantCotisations = ai.totalMontantCotisations.add(new BigDecimal(
                totalMontantCotisations.toString()));
        ai.totalMontantImpotSource = ai.totalMontantImpotSource.add(new BigDecimal(
                totalMontantImpotSource.toString()));
        ai.montantTotal = ai.montantTotal.add(new BigDecimal(montantTotal.toString()));
        ai.montantVentilations = ai.montantVentilations.add(new BigDecimal(montantVentilation
                .toString()));

        ai.idsRPVentilations = idsVentilation;
        // Comme la clé est inexistante, on crée la liste
        // d'objet
        ArrayList list = new ArrayList();
        list.add(ai);

        // On insère la clé et la liste dans la map
        map.put(k, list);
    }

    private void createAttestation(String annee, String dateDebut, String dateFin) throws Exception {
        IJAttestations attestations = new IJAttestations(getSession());
        attestations.setAttestationsMap(map);
        attestations.setDateDebut(dateDebut);
        attestations.setDateFin(dateFin);
        attestations.setAnnee(annee);
        attestations.setParent(this);
        attestations.setTailleLot(1);
        attestations.setIsSendToGED(getIsSendToGed());
        attestations.setIsGenerationUnique(isGenerationUnique);
        attestations.executeProcess();
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

}
