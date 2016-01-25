/*
 * Créé le 26 oct. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hermes.process;

import globaz.commons.nss.NSUtil;
import globaz.framework.process.FWProcess;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEInputAnnonceViewBean;
import globaz.hermes.db.gestion.HELotListViewBean;
import globaz.hermes.db.gestion.HELotViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.db.gestion.HERassemblementViewBean;
import globaz.hermes.utils.DateUtils;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.db.compte.CIEcriture;
import java.util.List;

;
/**
 * @author ald Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class HEGenererCIPapier extends FWProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String etatNominatif;
    String idAttenteRetour;
    List listAnnonce38Saisie;
    String numeroAVS;

    /**
	 *
	 */
    public HEGenererCIPapier() {
        super();
    }

    /**
     * @param session
     */
    public HEGenererCIPapier(BSession session) {
        super(session);
    }

    /**
     * @param parent
     */
    public HEGenererCIPapier(FWProcess parent) {
        super(parent);
    }

    @Override
    protected void _executeCleanUp() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        try {
            if (JAUtil.isStringEmpty(getIdAttenteRetour())) {
                throw new Exception("id de l'annonce n'est pas renseigné");
            } else {
                HERassemblementViewBean annonceDepart = new HERassemblementViewBean();
                annonceDepart.setSession(getSession());
                annonceDepart.setIdAttenteRetour(getIdAttenteRetour());
                annonceDepart.retrieve();
                if (annonceDepart.isNew()) {
                    throw new Exception("Impossible de charger l'annonce de départ pour l'id :" + getIdAttenteRetour());
                }
                numeroAVS = NSUtil.formatAVSUnknown(annonceDepart.getNumAVS());
                // rechercher l'annnonce 25 de départ en relation avec l'annonce
                // à générer
                HEOutputAnnonceListViewBean listAnnonces = new HEOutputAnnonceListViewBean();
                listAnnonces.setSession(getSession());
                listAnnonces.setForCodeApplication("25");
                listAnnonces.setCodeApplication3839(false);
                listAnnonces.setForCodeEnregistrement("01");
                listAnnonces.setForRefUnique(annonceDepart.getReferenceUnique());
                listAnnonces.wantCallMethodAfterFind(false);
                listAnnonces.wantCallMethodBefore(false);
                listAnnonces.wantCallMethodBeforeFind(false);
                listAnnonces.find(getTransaction());
                if (listAnnonces.size() != 0) {
                    // le 25 est trouvé
                    HEOutputAnnonceViewBean annonce25 = (HEOutputAnnonceViewBean) listAnnonces.getFirstEntity();
                    String referenceInterneCaisse = annonce25.getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE);
                    String dateCloture = annonce25.getField(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA);
                    String dateOrdre = annonce25.getField(IHEAnnoncesViewBean.DATE_ORDRE_JJMMAA);
                    String utilisateur = annonce25.getUtilisateur();
                    String statut = annonce25.getStatut();
                    String numAvsAyantDroit = annonce25
                            .getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_DROIT_OU_PARTENAIRE);
                    String dateJJMMAA = JACalendar.today().toString();
                    dateJJMMAA = dateJJMMAA.substring(0, 4) + dateJJMMAA.substring(6);
                    String referenceUnique = annonce25.getRefUnique();
                    // compléter les 38 avec les info de l'annonce 25 trouvée
                    // positionner total, nombre ecritures et genre8Existe (pour
                    // la génération de l'annonce 39)
                    long total = 0;
                    boolean genre8Existe = false;
                    int nombreEcritures = 0;
                    for (int i = 0; i < (listAnnonce38Saisie == null ? 0 : listAnnonce38Saisie.size()); i++) {
                        HEInputAnnonceViewBean courant = (HEInputAnnonceViewBean) listAnnonce38Saisie.get(i);
                        courant.put(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE, referenceInterneCaisse);
                        courant.put(IHEAnnoncesViewBean.DATE_ORDRE_JJMMAA, dateOrdre);
                        courant.setUtilisateur(utilisateur);
                        courant.setStatut(statut);
                        courant.setRefUnique(referenceUnique);
                        // set le num AVS de l'ayant pour le 38 courant
                        // si le motif = motif de splitting, le numéro de
                        // l'ayant droit se trouve dans le 1105
                        if ("95".equals(annonceDepart.getMotif())) {
                            // cas de splitting, recherche un 05
                            HEOutputAnnonceListViewBean listAnnonces05 = new HEOutputAnnonceListViewBean(getSession());
                            listAnnonces05.setForCodeApplication("11");
                            listAnnonces05.setCodeApplication3839(false);
                            listAnnonces05.setForCodeEnregistrement("05");
                            listAnnonces05.setForRefUnique(annonceDepart.getReferenceUnique());
                            listAnnonces05.wantCallMethodAfter(false);
                            listAnnonces05.wantCallMethodAfterFind(false);
                            listAnnonces05.wantCallMethodBefore(false);
                            listAnnonces05.wantCallMethodBeforeFind(false);
                            listAnnonces05.find(getTransaction());
                            if (listAnnonces05.getSize() != 0) {
                                // le 05 est trouvé, initialiser le numero de
                                // l'assuré ayant droit du 38
                                HEOutputAnnonceViewBean annonce05 = (HEOutputAnnonceViewBean) listAnnonces05
                                        .getEntity(0);
                                annonce05.retrieve(getTransaction());
                                // to do: ald, rechercher le 05 et copier le
                                // nunméro avs de l'ayant droit
                                numAvsAyantDroit = annonce05
                                        .getField(IHEAnnoncesViewBean.NUMERO_ASSURE_CONJOINT_SPLITTING_DIVORCE);
                            }
                        }
                        courant.put(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_DROIT_OU_PARTENAIRE, numAvsAyantDroit);
                        // et si c'est un 38 de type 1, il faut encore mettre
                        // certaines informations
                        if (courant.getField(IHEAnnoncesViewBean.CODE_1_OU_2).equals("1")) {
                            nombreEcritures++;
                            courant.put(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA, dateCloture);
                            String extourne = courant.getField(IHEAnnoncesViewBean.CHIFFRE_CLE_EXTOURNES);
                            int revenuTr = (int) Double.parseDouble(courant.getField(IHEAnnoncesViewBean.REVENU));
                            if (JAUtil.isIntegerEmpty(extourne) || CIEcriture.CS_EXTOURNE_2.equals(extourne)
                                    || CIEcriture.CS_EXTOURNE_6.equals(extourne)
                                    || CIEcriture.CS_EXTOURNE_8.equals(extourne)) {
                                total += revenuTr;
                            } else {
                                total -= revenuTr;
                            }
                        }
                        genre8Existe = CIEcriture.CS_CIGENRE_8.equals(courant
                                .getField(IHEAnnoncesViewBean.CHIFFRE_CLE_GENRE_COTISATIONS));
                    }
                    // générer le 39001
                    HEInputAnnonceViewBean annonce39 = new HEInputAnnonceViewBean();
                    // initialisation du champ enregistrement
                    annonce39.put(IHEAnnoncesViewBean.CODE_APPLICATION, "39");
                    annonce39.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "001");
                    annonce39.put(IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE, annonceDepart.getSession()
                            .getApplication().getProperty("noCaisse"));
                    annonce39.put(IHEAnnoncesViewBean.NUMERO_AGENCE_COMMETTANTE, annonceDepart.getSession()
                            .getApplication().getProperty("noAgence"));
                    annonce39.put(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE, referenceInterneCaisse);
                    if (!JadeStringUtil.isBlank(numAvsAyantDroit)) {
                        annonce39.put(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_DROIT_OU_PARTENAIRE,
                                JadeStringUtil.removeChar(numAvsAyantDroit, '.'));
                    } else {
                        annonce39.put(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_DROIT_OU_PARTENAIRE,
                                JadeStringUtil.removeChar(annonceDepart.getNumAVS(), '.'));
                    }
                    annonce39.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, annonceDepart.getMotif());
                    annonce39.put(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA, dateCloture);
                    annonce39.put(IHEAnnoncesViewBean.DATE_ORDRE_JJMMAA, dateOrdre);
                    annonce39.put(IHEAnnoncesViewBean.NUMERO_CAISSE__CI,
                            StringUtils.getCaisse(annonceDepart.getNumCaisse()));
                    annonce39.put(IHEAnnoncesViewBean.NUMERO_AGENCE_CI,
                            StringUtils.getAgence(annonceDepart.getNumCaisse()));
                    annonce39.put(IHEAnnoncesViewBean.NUMERO_ASSURE,
                            JadeStringUtil.removeChar(annonceDepart.getNumAVS(), '.'));
                    annonce39.put(IHEAnnoncesViewBean.TOTAL_REVENUS, String.valueOf(Math.abs(total)));
                    annonce39.put(IHEAnnoncesViewBean.CODE_VALEUR_CHAMP_15, total < 0 ? "1" : "0");
                    annonce39.put(IHEAnnoncesViewBean.NOMBRE_INSCRIPTIONS_CI, String.valueOf(nombreEcritures));
                    annonce39.put(IHEAnnoncesViewBean.CI_ADDITIONNEL, "0");
                    // cas divorce
                    if ("95".equals(annonceDepart.getMotif()) && genre8Existe) {
                        annonce39.put(IHEAnnoncesViewBean.SPLITTING_CAS_DIVORCE, "1");
                    } else {
                        annonce39.put(IHEAnnoncesViewBean.SPLITTING_CAS_DIVORCE, "0");
                    }
                    annonce39.put(IHEAnnoncesViewBean.DATE_TRANSMISSION, dateJJMMAA);
                    // positioner des différents champs
                    annonce39.setMotif(annonceDepart.getMotif());
                    // annonce39.setNumeroCaisse(annonceDepart.getNumCaisse());
                    annonce39.setNumeroAVS(annonceDepart.getNumAVS());
                    annonce39.setSession(annonceDepart.getSession());
                    annonce39.setIdProgramme("HERMES");
                    annonce39.setUtilisateur(utilisateur);
                    annonce39.setStatut(statut);
                    annonce39.setRefUnique(referenceUnique);
                    annonce39.setTypeLot(HELotViewBean.CS_TYPE_RECEPTION);
                    /** ****fin de la génération du 39001 */
                    // ajouter les 38 et le(s) 39 en 1 seule transaction
                    // lier le 38, 39 aux attentes
                    String ref38 = "0";
                    String ref39 = "0";
                    // rechercher un lot en réception existant
                    // date par defaut = jour avant la saisie
                    String idLot = ""; // DateUtils.getDateToCurrentAMJ(-1);
                    HELotListViewBean lots = new HELotListViewBean();
                    lots.setSession(annonceDepart.getSession());
                    lots.setForType(HELotViewBean.CS_TYPE_RECEPTION);
                    lots.setForQuittance(HELotViewBean.LOT_QUITTANCE);
                    lots.setOrder(HELotListViewBean.ORDER_BY_DATE_DESC);
                    lots.find(getTransaction());
                    if (lots.size() > 0) {
                        idLot = ((HELotViewBean) lots.getFirstEntity()).getIdLot();
                    } else {
                        // cas rare, mais possible, aucun lot en réception
                        // trouvé !
                        // on va en créé un avec la date d'un jour avant la date
                        // courante
                        // ceci pour ne pas avoir de conflit avec le traitement
                        // de lot en réception
                        HELotViewBean lot = new HELotViewBean();
                        lot.setSession(annonceDepart.getSession());
                        // date d'aujourd'hui
                        lot.setDateCentrale(DateUtils.getDateToCurrentAMJ(-1));
                        lot.setDateTraitement(JACalendar.todayJJsMMsAAAA());
                        // je mets l'heure
                        lot.setHeureTraitement(JACalendar.formatTime(JACalendar.now()));
                        // l'utilisateur/programme
                        lot.setUtilisateur(annonceDepart.getSession().getUserId());
                        // c'est un envoi
                        lot.setType(HELotViewBean.CS_TYPE_RECEPTION);
                        // pas acquitté sauf si une réceptio
                        lot.setQuittance(HELotViewBean.LOT_QUITTANCE);
                        lot.setPriorite(HELotViewBean.CS_LOT_PTY_HAUTE);
                        lot.setEtat(HELotViewBean.CS_LOT_ETAT_TRAITE);
                        // je sauve le nouveau lot
                        lot.add(getTransaction());
                        idLot = lot.getIdLot();
                    }
                    HEInputAnnonceViewBean courant;
                    // tout d'abord les 38
                    for (int i = 0; i < listAnnonce38Saisie.size(); i++) {
                        courant = (HEInputAnnonceViewBean) listAnnonce38Saisie.get(i);
                        courant.setSession(getSession());
                        courant.setIdLot(idLot);
                        courant.wantCallValidate(false);
                        courant.add(getTransaction());
                        if (i == 0) {
                            ref38 = courant.getIdAnnonce();
                        }
                    }
                    // ensuite les 39
                    // générer le(s) 39
                    annonce39.setIdLot(idLot);
                    annonce39.add(getTransaction());
                    annonce39.retrieve(getTransaction());
                    ref39 = annonce39.getIdAnnonce();
                    // générer le 39-2 et l'ajouter
                    annonce39.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "2");
                    annonce39.put(IHEAnnoncesViewBean.ETAT_NOMINATIF, etatNominatif);
                    annonce39.add(getTransaction());
                    // lier le 38, 39 aux attentes
                    // d'abord l'attente du 39
                    annonceDepart.wantCallValidate(false);
                    annonceDepart.setIdAnnonceRetour(ref39);
                    annonceDepart.update(getTransaction());
                    // ensuite l'attente du 38
                    if (nombreEcritures != 0) {
                        HERassemblementViewBean attente38 = annonceDepart.getAttente38(getTransaction());
                        if (attente38 != null) {
                            attente38.wantCallValidate(false);
                            attente38.setIdAnnonceRetour(ref38);
                            attente38.update(getTransaction());
                        }
                    } else {
                        annonceDepart.supprimerAttente38(getTransaction());
                    }
                    // tester s'il reste encore des attentes pour l'annonce 11
                    // si oui changer le status de l'annonce pour la mettre en
                    // terminé
                    annonceDepart.termineAnnonce(referenceUnique, annonceDepart.getMotif(), getTransaction());
                    // ********************************************
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "HEGenererCIPapier process");
            _addError(getTransaction(), e.getMessage());
        }
        return false;
    }

    @Override
    protected String getEMailObject() {
        return FWMessageFormat.format(getSession().getLabel("HERMES_10027"), JAUtil.isStringEmpty(numeroAVS) ? ""
                : numeroAVS);
    }

    /**
     * @return
     */
    public String getEtatNominatif() {
        return etatNominatif;
    }

    /**
     * @return
     */
    public String getIdAttenteRetour() {
        return idAttenteRetour;
    }

    /**
     * @return
     */
    public List getListAnnonce38Saisie() {
        return listAnnonce38Saisie;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * @param string
     */
    public void setEtatNominatif(String string) {
        etatNominatif = string;
    }

    /**
     * @param string
     */
    public void setIdAttenteRetour(String string) {
        idAttenteRetour = string;
    }

    /**
     * @param list
     */
    public void setListAnnonce38Saisie(List list) {
        listAnnonce38Saisie = list;
    }

}
