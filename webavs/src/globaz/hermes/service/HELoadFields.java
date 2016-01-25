package globaz.hermes.service;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceLotListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.db.parametrage.HEChampannonceListViewBean;
import globaz.hermes.db.parametrage.HEChampannonceViewBean;
import globaz.hermes.db.parametrage.HEParametrageannonce;
import globaz.hermes.db.parametrage.HEParametrageannonceManager;
import globaz.hermes.utils.HENNSSUtils;
import globaz.hermes.utils.HEUtil;
import globaz.hermes.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class HELoadFields {
    /**
     * @param transaction
     * @param limiteNbDemandes
     * @param listArc
     * @param listeReferences
     * @param outputAnnonceListViewBean
     * @return
     * @throws Exception
     */
    public IHEOutputAnnonce[] extraireAnnoncesAndLoadfields(BTransaction transaction, Integer limiteNbDemandes,
            HEOutputAnnonceLotListViewBean outputAnnonceListViewBean) throws Exception {
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
                outputAnnonceViewBean = loadFields(outputAnnonceViewBean, champAnnonceM,
                        outputAnnonceViewBean.getChampEnregistrement());
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
     * @param line
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
