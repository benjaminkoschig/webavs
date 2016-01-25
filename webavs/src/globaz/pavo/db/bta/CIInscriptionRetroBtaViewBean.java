package globaz.pavo.db.bta;

import globaz.commons.nss.NSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIRassemblementOuverture;
import globaz.pavo.db.compte.CIRassemblementOuvertureManager;
import globaz.pavo.util.CIUtil;
import globaz.pavo.vb.CIAbstractPersistentViewBean;
import java.util.ArrayList;
import java.util.TreeMap;

public class CIInscriptionRetroBtaViewBean extends CIAbstractPersistentViewBean implements FWViewBeanInterface {

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public TreeMap getInscriptionsCiToDoVb() {
        TreeMap anneeListRequerant = new TreeMap();// clé=année, valeur=tableau
        // de requérants
        String sexeRequerantCi = "";

        // Retrouver les informations sur le dossier
        CIDossierBta dossierBta = new CIDossierBta();
        dossierBta.setSession(getSession());
        dossierBta.setIdDossierBta(getId());
        try {
            dossierBta.retrieve();
            if (!dossierBta.getEtatDossier().equals(CIDossierBta.CS_ETAT_OUVERT)) {
                TreeMap t = new TreeMap();
                t.put("erreur", getSession().getLabel("MSG_ERREUR_DOSSIER_NON_OUVERT"));
                return t;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // récupération des requerants du dossier
        CIRequerantBtaManager requerantManager = new CIRequerantBtaManager();
        requerantManager.setSession(getSession());
        requerantManager.setForIdDossierBta(getId());
        // requerantManager.setForDateInscriptionRetroFlag("nullOrZero");//recherche
        // que les requerants non flagé
        try {
            requerantManager.find();

            // 1er parcours des requérants pour voir si il y a déjà eu des
            // inscriptions CI pour le dossier
            // si oui on ne peut plus passer de retro-actifs
            int anneeFlagMax = 0;
            for (int i = 0; i < requerantManager.size(); i++) {
                CIRequerantBta requerant = (CIRequerantBta) requerantManager.getEntity(i);
                if (!JadeStringUtil.isBlank(requerant.getDateInscriptionRetroFlag())) {
                    int anneeFlag = JADate.getYear(requerant.getDateInscriptionRetroFlag()).intValue();
                    if (anneeFlag > anneeFlagMax) {
                        anneeFlagMax = anneeFlag;
                    }
                }
            }
            if (anneeFlagMax != 0) {
                TreeMap t = new TreeMap();
                t.put("erreur", getSession().getLabel("MSG_ERREUR_RETRO_IMPOSSIBLE"));
                return t;
            }

            // parcours des requérants
            for (int i = 0; i < requerantManager.size(); i++) {
                int nbMaxAnneeRetro = 5;
                String dateCloture = "";// date de cloture du CI si existe
                String anneeEnCours = JADate.getYear(JACalendar.todayJJsMMsAAAA()).toString();
                String annee = String.valueOf(Integer.parseInt(anneeEnCours) - 1); // annee
                // =
                // annee
                // en
                // cours
                // -1
                int anneeInt = Integer.parseInt(annee);

                // Récupération du requérant
                CIRequerantBta requerant = (CIRequerantBta) requerantManager.getEntity(i);

                // rechercher du CI du requérant
                CICompteIndividuelManager compteIndManager = new CICompteIndividuelManager();
                compteIndManager.setSession(getSession());
                compteIndManager.setForNumeroAvs(NSUtil.unFormatAVS(requerant.getNumeroNnssRequerant()));
                compteIndManager.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);// pour
                // ne
                // prendre
                // que
                // les
                // CI
                // ouverts
                compteIndManager.find();
                if (compteIndManager.size() > 0) {
                    // Récupération du CI
                    CICompteIndividuel compteInd = (CICompteIndividuel) compteIndManager.getFirstEntity();
                    sexeRequerantCi = compteInd.getSexe();

                    // recherche si il existe une date de cloture
                    CIRassemblementOuvertureManager rassemblementManager = new CIRassemblementOuvertureManager();
                    rassemblementManager.setSession(getSession());
                    rassemblementManager.setForCompteIndividuelId(compteInd.getCompteIndividuelId());
                    rassemblementManager.find();
                    if (rassemblementManager.size() > 0) {
                        for (int k = 0; k < rassemblementManager.size(); k++) {
                            CIRassemblementOuverture rassemblement = (CIRassemblementOuverture) rassemblementManager
                                    .getEntity(k);
                            // si le motif est de type 71 ou 81 on est dans le
                            // cas d'un rentier
                            if ((rassemblement.getMotifArc().equals("71") || rassemblement.getMotifArc().equals("81"))
                                    && JadeStringUtil.isBlank(rassemblement.getDateRevocation())) {
                                dateCloture = rassemblement.getDateCloture();
                            }
                        }
                    }
                } else {
                    // si le CI d'un requerant n'est pas trouvé
                    TreeMap t = new TreeMap();
                    t.put("erreur",
                            getSession().getLabel("MSG_ERREUR_CI_INTROUVABLE") + " "
                                    + requerant.getNumeroNnssRequerant());
                    return t;
                }

                // si la demande du requerant est acceptée (c'est à dire date
                // debut!=date fin)
                if (!requerant.getDateDebut().equals(requerant.getDateFin())) {

                    // définition de l'annee et du nbMaxAnneeRetro en fonction
                    // du cas en présence

                    // si requerant a une date de fin et une date de cloture
                    if (!JadeStringUtil.isBlank(requerant.getDateFin()) && !JadeStringUtil.isBlank(dateCloture)) {
                        int anneeIntTemp1 = JADate.getYear(requerant.getDateFin()).intValue();
                        int anneeIntTemp2 = JADate.getYear(dateCloture).intValue() - 1;
                        if (anneeIntTemp1 < anneeIntTemp2) {
                            anneeInt = JADate.getYear(requerant.getDateFin()).intValue();
                            nbMaxAnneeRetro = nbMaxAnneeRetro - (Integer.parseInt(annee) - anneeInt);
                            annee = JADate.getYear(requerant.getDateFin()).toString();
                        } else {
                            anneeInt = JADate.getYear(dateCloture).intValue();
                            anneeInt = anneeInt - 1;
                            nbMaxAnneeRetro = nbMaxAnneeRetro - (Integer.parseInt(annee) - anneeInt);
                            annee = String.valueOf(anneeInt);
                        }
                    }
                    // si requerant a que 1 date de fin
                    else if (!JadeStringUtil.isBlank(requerant.getDateFin())) {
                        anneeInt = JADate.getYear(requerant.getDateFin()).intValue();
                        nbMaxAnneeRetro = nbMaxAnneeRetro - (Integer.parseInt(annee) - anneeInt);
                        annee = JADate.getYear(requerant.getDateFin()).toString();
                    }
                    // si requerant a que 1 date de cloture rassemblement
                    else if (!JadeStringUtil.isBlank(dateCloture)) {
                        anneeInt = JADate.getYear(dateCloture).intValue();
                        anneeInt = anneeInt - 1;
                        nbMaxAnneeRetro = nbMaxAnneeRetro - (Integer.parseInt(annee) - anneeInt);
                        annee = String.valueOf(anneeInt);
                    }

                    // Remplissage du Treemap
                    while (anneeInt >= JADate.getYear(requerant.getDateDebut()).intValue() && nbMaxAnneeRetro > 0) {
                        // si annee pas contenue dans le treemap alors insertion
                        // de l'année et d'un tableau contenant le requerant
                        if (!anneeListRequerant.containsKey(annee)) {
                            if (isMajeur(new JADate(requerant.getDateNaissanceRequerant()), Integer.parseInt(annee))
                                    && !CIUtil.isRetraite(new JADate(requerant.getDateNaissanceRequerant()),
                                            sexeRequerantCi, anneeInt)) {
                                // création d'une list de requerant et insertion
                                // du requerant en cours (si pas en age de
                                // retraite et majeur)
                                ArrayList requerants = new ArrayList();
                                requerants.add(requerant);
                                anneeListRequerant.put(annee, requerants);
                            }
                        } else {
                            // récupération du tableau de requerants
                            // correspondant à l'année et ajout de requerant en
                            // cours (si pas en age de retraite et majeur)
                            if (isMajeur(new JADate(requerant.getDateNaissanceRequerant()), Integer.parseInt(annee))
                                    && !CIUtil.isRetraite(new JADate(requerant.getDateNaissanceRequerant()),
                                            sexeRequerantCi, anneeInt)) {
                                ArrayList requerants = (ArrayList) anneeListRequerant.get(annee);
                                requerants.add(requerant);
                            }
                        }
                        anneeInt--;
                        nbMaxAnneeRetro--;
                        annee = String.valueOf(anneeInt);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (anneeListRequerant.size() < 1) {
            TreeMap t = new TreeMap();
            t.put("erreur", getSession().getLabel("MSG_AUCUNE_INSCRIPTION_RETRO"));
            return t;
        }

        return anneeListRequerant;
    }

    public boolean isMajeur(JADate dateNaissance, int annee) {
        boolean majeur = false;
        int anneeDroitBta = dateNaissance.getYear() + 18;
        if (annee >= anneeDroitBta) {
            majeur = true;
        }

        return majeur;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
