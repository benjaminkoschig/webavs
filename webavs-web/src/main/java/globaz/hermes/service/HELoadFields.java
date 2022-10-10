package globaz.hermes.service;

import ch.globaz.pegasus.business.exceptions.models.process.AdaptationException;
import globaz.corvus.db.annonces.REAnnonce61;
import globaz.corvus.db.annonces.REAnnonce61Manager;
import globaz.globall.db.*;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEInputAnnonceViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceLotListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.db.parametrage.HEChampannonceListViewBean;
import globaz.hermes.db.parametrage.HEChampannonceViewBean;
import globaz.hermes.db.parametrage.HEParametrageannonce;
import globaz.hermes.db.parametrage.HEParametrageannonceManager;
import globaz.hermes.utils.HENNSSUtils;
import globaz.hermes.utils.HEUtil;
import globaz.hermes.utils.StringUtils;
import globaz.prestation.acor.web.mapper.PRConverterUtils;

import java.util.ArrayList;
import java.util.List;

public class HELoadFields {
    /**
     * @param transaction
     * @param limiteNbDemandes
     * @param outputAnnonceListViewBean
     * @param isReceptionCentrale
     * @return
     * @throws Exception
     */
    public IHEOutputAnnonce[] extraireAnnoncesAndLoadfields(BTransaction transaction, Integer limiteNbDemandes,
            HEOutputAnnonceLotListViewBean outputAnnonceListViewBean, boolean isReceptionCentrale) throws Exception {
        ArrayList listeReferences = new ArrayList();
        List listArc = new ArrayList();
        BStatement statement = outputAnnonceListViewBean.cursorOpen(transaction);
        HEOutputAnnonceViewBean outputAnnonceViewBean = null;
        int index = 0;
        String lastRefUnique = "";
        HEParametrageannonceManager paramM = new HEParametrageannonceManager(getSession());
        HEParametrageannonce param = new HEParametrageannonce(getSession());
        // la liste des champs
        HEChampannonceListViewBean champAnnonceM = new HEChampannonceListViewBean(getSession());
        String codeApp = getCodeApp("61");
        String codeEnr = getCodeEnr(codeApp, "6101");

        param = loadParametrage(codeApp, codeEnr, paramM);
        champAnnonceM = loadChamps(champAnnonceM, param.getIdParametrageAnnonce());

        while ((outputAnnonceViewBean = (HEOutputAnnonceViewBean) outputAnnonceListViewBean.cursorReadNext(statement)) != null) {
            if ((limiteNbDemandes.intValue() > 0) && (index >= limiteNbDemandes.intValue())) {
                break;
            } else {

                // this.loadFields(champAnnonceM, outputAnnonceViewBean.getChampEnregistrement());
                // outputAnnonceViewBean.putAll(m);
                if (isReceptionCentrale) {
                    outputAnnonceViewBean = loadFieldsReceptionCentrale(outputAnnonceViewBean);
                } else {
                    outputAnnonceViewBean = loadFields(outputAnnonceViewBean, champAnnonceM,
                            outputAnnonceViewBean.getChampEnregistrement());
                }
                listArc.add(outputAnnonceViewBean);
                listeReferences.add(outputAnnonceViewBean.getRefUnique());
                if (!lastRefUnique.equals(outputAnnonceViewBean.getRefUnique())) {
                    index++;
                }
            }
            lastRefUnique = outputAnnonceViewBean.getRefUnique();
        }
        outputAnnonceListViewBean.cursorClose(statement);
        // valide les données extraites pour le prochain appel (set date dans
        // RNDECP)
        // HEUtil.commitTreatReference(listeReferences, this.getSession(), transaction);
        // retourne le tableau des données
        return (IHEOutputAnnonce[]) listArc.toArray(new IHEOutputAnnonce[listArc.size()]);
    }

    /**
     * Method getCodeApp.
     * 
     * @param line
     */
    private String getCodeApp(String line) {
        return line.substring(0, 2);
    }

    /**
     * Method getCodeEnr.
     * 
     * @param line
     * @return String
     */
    private String getCodeEnr(String codeApp, String line) {

        if (codeApp.equals("38") || codeApp.equals("39")) {
            return line.substring(2, 5);
        } else {
            return line.substring(2, 4);
        }
    }

    private String getCSCodeApp(String codeApp) throws Exception {
        // FWParametersSystemCodeManager csCodeAppM = ((HEApplication)
        // getSession().getApplication()).getCsCodeApplicationListe(getSession());
        FWParametersSystemCodeManager csCodeAppM = new FWParametersSystemCodeManager();
        csCodeAppM.setForIdGroupe("HECODAPP");
        csCodeAppM.setForIdTypeCode("11100001");
        csCodeAppM.setSession(getSession());
        csCodeAppM.setForCodeUtilisateur(codeApp);
        csCodeAppM.find(getSession().getCurrentThreadTransaction());

        FWParametersSystemCode csCodeAppE = (FWParametersSystemCode) csCodeAppM.getFirstEntity();

        if (csCodeAppE == null) {
            throw new Exception("Erreur critique, impossible de récupérer le code système du code application :"
                    + codeApp);
        }

        return csCodeAppE.getIdCode();
    }

    /**
     * Renvoie la session en cours.
     * 
     * @return la session en cours
     */
    public final BSession getSession() {
        BSession contextSession = BSessionUtil.getSessionFromThreadContext();
        if (contextSession != null) {
            return contextSession;
        }
        return contextSession;
    }

    /**
     * Method loadChamps. Charge les champs d'une annonce
     * 
     * @param champAnnonceM
     * @param idParametrage
     * @return HEChampannonceListViewBean
     * @throws Exception
     */
    private HEChampannonceListViewBean loadChamps(HEChampannonceListViewBean champAnnonceM, String idParametrage)
            throws Exception {

        if (!champAnnonceM.getForIdParametrageAnnonce().equals(idParametrage)) {
            champAnnonceM.setForIdParametrageAnnonce(idParametrage);
            champAnnonceM.find(getSession().getCurrentThreadTransaction());
        }

        return champAnnonceM;
    }

    /**
     * Method loadFields. Charge les valeurs des champs
     *
     * @param annonce
     * @param annonce61
     * @return HEInputAnnonceViewBean
     */
    public static HEInputAnnonceViewBean loadFields(HEInputAnnonceViewBean annonce, REAnnonce61 annonce61) {
        annonce.put(IHEAnnoncesViewBean.CODE_APPLICATION, annonce61.getCodeApplication());
        annonce.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, annonce61.getCodeEnregistrement01());
        annonce.put(IHEAnnoncesViewBean.RESERVE_A_BLANC, "");
        annonce.put(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT, annonce61.getNss());
        annonce.put(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT, annonce61.getCodeRetour());
        annonce.put(IHEAnnoncesViewBean.OBSERVATION_1_CENTRALE, annonce61.getObservation());
        annonce.put(IHEAnnoncesViewBean.CS_CANTON_ETAT_DOMICILE, annonce61.getCantonEtatDomicile());
        annonce.put(IHEAnnoncesViewBean.CS_ETAT_CIVIL_AYANT_DROIT, annonce61.getEtatCivil());
        annonce.put(IHEAnnoncesViewBean.CS_MONTANT_ALLOC_INIT_TRAVAIL_PERIODE_2, "");
        annonce.put(IHEAnnoncesViewBean.CS_REFUGIE, annonce61.getIsRefugie());
        annonce.put(IHEAnnoncesViewBean.CS_DEBUT_DU_DROIT_MMAA, annonce61.getDebutDroit());
        annonce.put(IHEAnnoncesViewBean.CS_MENSUALITE_PRESTATION_FRANCS, annonce61.getAncienMontant());
        annonce.put(IHEAnnoncesViewBean.CS_MENSUALITE_RENTE_ORDINAIRE_REMPLACEE_FRANCS, annonce61.getNouveauMontant());
        annonce.put(IHEAnnoncesViewBean.CS_FIN_DU_DROIT_MMAA, annonce61.getFinDroit());
        annonce.put(IHEAnnoncesViewBean.CS_MOIS_DU_RAPPORT, annonce61.getMoisRapport());
        annonce.put(IHEAnnoncesViewBean.CS_CODE_DE_MUTATION, annonce61.getCodeMutation());
        annonce.put(IHEAnnoncesViewBean.CS_OFFICEAI_COMPETENT_AYANT_DROIT, "");
        annonce.put(IHEAnnoncesViewBean.CS_DEGREINVALIDITE_AYANT_DROIT, annonce61.getDegreInvalidite());
        annonce.put(IHEAnnoncesViewBean.CS_CODEINFIRMITE_AYANT_DROIT, "");
        annonce.put(IHEAnnoncesViewBean.CS_CODE_SURVIVANT_INVALIDE, "");
        annonce.put(IHEAnnoncesViewBean.CS_FRACTION_DE_LA_RENTE, annonce61.getFractionRente());
        annonce.put(IHEAnnoncesViewBean.PC_NUMERO_OFFICE_PC, annonce61.getNumeroCaisse());
        annonce.put(IHEAnnoncesViewBean.PC_NUMERO_AGENCE_PC, annonce61.getNumeroAgence());
        annonce.put(IHEAnnoncesViewBean.PC_REFERENCE_INTERNE_OFFICE_PC, annonce61.getReferenceCaisseInterne());
        annonce.put(IHEAnnoncesViewBean.PC_NUMERO_CAISSE_QUI_VERSE_LA_PRESTATION, annonce61.getNumeroCaisse());
        annonce.put(IHEAnnoncesViewBean.PC_NUMERO_AGENCE_QUI_VERSE_LA_PRESTATION, annonce61.getNumeroAgence());
        annonce.put(IHEAnnoncesViewBean.PC_GENRE_DE_PRESTATION, annonce61.getGenrePrestation());
        return annonce;
    }

    /**
     * Method loadFields. Charge les valeurs des champs
     *
     * @param annonce
     * @return HEInputAnnonceViewBean
     * @throws AdaptationException
     */
    private HEOutputAnnonceViewBean loadFieldsReceptionCentrale(HEOutputAnnonceViewBean annonce) throws AdaptationException {

        REAnnonce61Manager annonce61Manager = new REAnnonce61Manager();
        annonce61Manager.setForDateAnnonce(PRConverterUtils.formatddMMAAAAToAAAAMMdd(annonce.getDateAnnonce()));
        annonce61Manager.setForNss(annonce.getNumeroAVS());
        try {
            annonce61Manager.find(10);
            if (annonce61Manager.getContainer().size() > 1) {
                throw new AdaptationException("Il ne peut y avoir plus d'un enregistrement pour le NSS : " + annonce.getNumeroAVS() + " - Dans la reception de la centrale pour la date du : " + annonce.getDateAnnonce());
            }

            REAnnonce61 annonce61 = (REAnnonce61) annonce61Manager.get(0);
            annonce.put(IHEAnnoncesViewBean.CODE_APPLICATION, annonce61.getCodeApplication());
            annonce.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, annonce61.getCodeEnregistrement01());
            annonce.put(IHEAnnoncesViewBean.RESERVE_A_BLANC, "");
            annonce.put(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT, annonce61.getNss());
            annonce.put(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT, annonce61.getCodeRetour());
            annonce.put(IHEAnnoncesViewBean.OBSERVATION_1_CENTRALE, annonce61.getObservation());
            annonce.put(IHEAnnoncesViewBean.CS_CANTON_ETAT_DOMICILE, annonce61.getCantonEtatDomicile());
            annonce.put(IHEAnnoncesViewBean.CS_ETAT_CIVIL_AYANT_DROIT, annonce61.getEtatCivil());
            annonce.put(IHEAnnoncesViewBean.CS_MONTANT_ALLOC_INIT_TRAVAIL_PERIODE_2, "");
            annonce.put(IHEAnnoncesViewBean.CS_REFUGIE, annonce61.getIsRefugie());
            annonce.put(IHEAnnoncesViewBean.CS_DEBUT_DU_DROIT_MMAA, annonce61.getDebutDroit());
            annonce.put(IHEAnnoncesViewBean.CS_MENSUALITE_PRESTATION_FRANCS, annonce61.getAncienMontant());
            annonce.put(IHEAnnoncesViewBean.CS_MENSUALITE_RENTE_ORDINAIRE_REMPLACEE_FRANCS, annonce61.getNouveauMontant());
            annonce.put(IHEAnnoncesViewBean.CS_FIN_DU_DROIT_MMAA, annonce61.getFinDroit());
            annonce.put(IHEAnnoncesViewBean.CS_MOIS_DU_RAPPORT, annonce61.getMoisRapport());
            annonce.put(IHEAnnoncesViewBean.CS_CODE_DE_MUTATION, annonce61.getCodeMutation());
            annonce.put(IHEAnnoncesViewBean.CS_OFFICEAI_COMPETENT_AYANT_DROIT, "");
            annonce.put(IHEAnnoncesViewBean.CS_DEGREINVALIDITE_AYANT_DROIT, annonce61.getDegreInvalidite());
            annonce.put(IHEAnnoncesViewBean.CS_CODEINFIRMITE_AYANT_DROIT, "");
            annonce.put(IHEAnnoncesViewBean.CS_CODE_SURVIVANT_INVALIDE, "");
            annonce.put(IHEAnnoncesViewBean.CS_FRACTION_DE_LA_RENTE, annonce61.getFractionRente());
            annonce.put(IHEAnnoncesViewBean.PC_NUMERO_OFFICE_PC, annonce61.getNumeroCaisse());
            annonce.put(IHEAnnoncesViewBean.PC_NUMERO_AGENCE_PC, annonce61.getNumeroAgence());
            annonce.put(IHEAnnoncesViewBean.PC_REFERENCE_INTERNE_OFFICE_PC, annonce61.getReferenceCaisseInterne());
            annonce.put(IHEAnnoncesViewBean.PC_NUMERO_CAISSE_QUI_VERSE_LA_PRESTATION, annonce61.getNumeroCaisse());
            annonce.put(IHEAnnoncesViewBean.PC_NUMERO_AGENCE_QUI_VERSE_LA_PRESTATION, annonce61.getNumeroAgence());
            annonce.put(IHEAnnoncesViewBean.PC_GENRE_DE_PRESTATION, annonce61.getGenrePrestation());
        } catch (Exception e) {
            throw new AdaptationException("L'annonce n'a pu être mappée, contrôler le retour centrale pour le NSS : " + annonce.getNumeroAVS() + " - à la date du : " + annonce.getDateAnnonce());
        }

        return annonce;
    }

    /**
     * Method loadFields. Charge les valeurs des champs
     * 
     * @param annonce
     * @param champAnnonceM
     * @param line
     * @return HEInputAnnonceViewBean
     */
    private HEOutputAnnonceViewBean loadFields(HEOutputAnnonceViewBean annonce,
            HEChampannonceListViewBean champAnnonceM, String line) {

        for (int j = 0; j < champAnnonceM.size(); j++) {
            HEChampannonceViewBean champ = (HEChampannonceViewBean) champAnnonceM.getEntity(j);
            int debut = Integer.parseInt(champ.getDebut()) - 1;
            int fin = debut + Integer.parseInt(champ.getLongueur());
            if (champAnnonceM.size() - 1 == j) {
                fin = line.length();
            }
            // modifications ALD : par defaut annonce orpheline
            // annonce.setStatut(IHEAnnoncesViewBean.CS_A_TRAITER);

            if (HEAnnoncesViewBean.isReferenceInterne(champ.getIdChamp())) {
                annonce.put(champ.getIdChamp(), HEUtil.checkReferenceUnique(line.substring(debut, fin)));
            } else if (HEAnnoncesViewBean.isNumeroAVS(champ.getIdChamp())) {
                // pour le cas où il s'agit d'un numéro AVS, on remplit la table avec
                // sans le signe -, donc soit un numéro avs, soit un NNSS
                String num = line.substring(debut, fin);
                if (IHEAnnoncesViewBean.CS_NUMERO_ASSURE_13_POSITIONS.equals(champ.getIdChamp())) {
                    // C'est le champ de NNSS de la confirmation --> setter comme numéro AVS (Exception)
                    annonce.put(champ.getIdChamp(), line.substring(debut, fin));
                    annonce.put(champ.getIdChamp() + HENNSSUtils.PARAM_NNSS, "true");
                } else if (HENNSSUtils.isNNSSNegatif(num)) {
                    annonce.put(champ.getIdChamp(), HENNSSUtils.convertNegatifToNNSS(num));
                    annonce.put(champ.getIdChamp() + HENNSSUtils.PARAM_NNSS, "true");
                } else {
                    // Il s'agit d'un numéro AVS, donc on le met tel quel dans la map
                    annonce.put(champ.getIdChamp(), line.substring(debut, fin).trim());
                }
            } else {
                annonce.put(champ.getIdChamp(), line.substring(debut, fin).trim());
            }
        }

        return annonce;
    }

    /**
     * Method loadParametrage. Charge le parametrage
     * 
     * @param codeApp
     * @param codeEnr
     * @param paramM
     * @return HEParametrageannonce
     * @throws Exception
     */
    private HEParametrageannonce loadParametrage(String codeApp, String codeEnr, HEParametrageannonceManager paramM)
            throws Exception { // je prend le code application

        String csCodeApp = "";
        //
        // if (line.startsWith("38")) {
        //
        // if (line.substring(71, 72).equals("1")) {
        // csCodeApp = "111011";
        // } else if (line.substring(71, 72).equals("2")) {
        // csCodeApp = "111040";
        // }
        // } else {
        // csCodeApp = this.getCSCodeApp(codeApp);
        // } // je recharge seulement si C nécessaire
        csCodeApp = getCSCodeApp(codeApp);
        if (!paramM.getForIdCSCodeApplication().equals(csCodeApp)
                || !paramM.getForAfterCodeEnregistrementDebut().equals(StringUtils.unPad(codeEnr))
                || !paramM.getForBeforeCodeEnregistrementFin().equals(StringUtils.unPad(codeEnr))) {
            paramM.setForIdCSCodeApplication(csCodeApp);
            paramM.setForAfterCodeEnregistrementDebut(StringUtils.unPad(codeEnr));
            paramM.setForBeforeCodeEnregistrementFin(StringUtils.unPad(codeEnr));
        }

        paramM.find(getSession().getCurrentThreadTransaction());

        return (HEParametrageannonce) paramM.getFirstEntity();
    }

}
