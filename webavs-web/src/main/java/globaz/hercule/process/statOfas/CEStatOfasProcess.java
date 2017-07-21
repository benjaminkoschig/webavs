package globaz.hercule.process.statOfas;

import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.hercule.db.ICEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEControleEmployeurManager;
import globaz.hercule.db.reviseur.CEReviseur;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Processus de génération des statistiques OFAS
 * 
 * @author bjo
 * 
 */
public class CEStatOfasProcess extends BProcess {
    private static final long serialVersionUID = -6085592428871168582L;
    private String annee;

    public CEStatOfasProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        boolean processState = true;
        setSendCompletionMail(true);
        System.out.println("début du processus de génération des statistiques OFAS");
        // Création de la structure initiale (point 1.1 -> 2.9)
        CEStatOfasStructure statOfasStructure = new CEStatOfasStructure();

        // Création de la structure2 initiale (point 3.1)
        CEStatOfasStructure2 statOfasStructure2 = new CEStatOfasStructure2();

        // Création de l'objet permettant de gérer le point 3.3
        CEStatOfasNbEmpCat nbEmpCat = new CEStatOfasNbEmpCat();

        // Recherche des contrôle d'employeur pour l'année de la statistique
        CEControleEmployeurManager controlEmployeurManager = new CEControleEmployeurManager();
        controlEmployeurManager.setSession(getSession());
        controlEmployeurManager.setForAnneeEffectuee(getAnnee());
        controlEmployeurManager.setForActif(true);
        try {
            controlEmployeurManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            this._addError(getSession().getLabel("STAT_OFAS_FIND_ERREUR") + e.getMessage());
            getMemoryLog().logMessage(getSession().getLabel("STAT_OFAS_FIND_ERREUR"), FWMessage.FATAL,
                    this.getClass().toString());
            processState = false;
        }
        if (controlEmployeurManager.size() > 0) {
            for (int i = 0; i < controlEmployeurManager.size(); i++) {
                CEControleEmployeur controlEmployeur = (CEControleEmployeur) controlEmployeurManager.getEntity(i);
                try {
                    // Calcul du nombre d'année passée depuis le précédent controle
                    String nbAnneeDepuisDernierControl = getNbAnneeDernierControle(controlEmployeur);

                    // Recherche de la catégorie de la masse salariale
                    String anneeFinControl = JADate.getYear(controlEmployeur.getDateFinControle()).toString();
                    String categorieSalariale = getCategorieMasseSalariale(controlEmployeur, anneeFinControl);

                    // Recherche du type de résultat du contrôle (paiement d'arriérés, remboursement ou sans
                    // particularité)
                    String[] resultatControleAndMasse = getResultatControlAndMasse(controlEmployeur);

                    // Insertion des résultats dans la structure
                    statOfasStructure.addCas(nbAnneeDepuisDernierControl, categorieSalariale,
                            resultatControleAndMasse[0]);
                    statOfasStructure.addMasseSalariale(nbAnneeDepuisDernierControl, categorieSalariale,
                            resultatControleAndMasse[0], new FWCurrency(resultatControleAndMasse[1]));

                    // Recherche du type de réviseur et ajout du cas à la statistique (point 3.1) et ajout du cas
                    String typeDeReviseur = getTypeReviseur(controlEmployeur);
                    statOfasStructure2.addCas(categorieSalariale, typeDeReviseur);
                } catch (Exception e) {
                    this._addError(getSession().getLabel("STAT_OFAS_STAT_ERREUR") + e.getMessage());
                    getMemoryLog().logMessage(
                            getSession().getLabel("STAT_OFAS_STAT_ERREUR") + " : " + controlEmployeur.getNumAffilie(),
                            FWMessage.INFORMATION, this.getClass().toString());
                }
            }

            // Recherche des employeurs actifs
            try {
                List<CEStatOfasEmployeurActifBean> listEmployeurActif = getEmployeurActifs();
                for (CEStatOfasEmployeurActifBean emp : listEmployeurActif) {
                    try {
                        if ((emp.getCumulMasse() == null) || (emp.getCumulMasse() <= 0)) {
                            nbEmpCat.addCas("0");
                        } else {
                            String categorie = CEControleEmployeurService.findCategorie(emp.getCumulMasse());
                            nbEmpCat.addCas(categorie);
                        }
                    } catch (Exception e) {
                        this._addError(getSession().getLabel("STAT_OFAS_STAT_ERREUR") + e.getMessage());
                        getMemoryLog().logMessage(
                                getSession().getLabel("STAT_OFAS_STAT_ERREUR") + " : " + emp.getNumeroAffilie(),
                                FWMessage.INFORMATION, this.getClass().toString());
                    }
                }
            } catch (Exception e1) {
                this._addError(getSession().getLabel("STAT_OFAS_EMP_ACTIF_ERREUR") + e1.getMessage());
                getMemoryLog().logMessage(getSession().getLabel("STAT_OFAS_EMP_ACTIF_ERREUR"), FWMessage.FATAL,
                        this.getClass().toString());
                processState = false;
            }

            // Création du fichier XML
            try {
                String path = CEStatOfasXmlFile.createFile(getSession(), statOfasStructure, statOfasStructure2,
                        nbEmpCat);
                // Publication du fichier
                publishStatOfasXmlFile(path);
            } catch (Exception e) {
                this._addError(getSession().getLabel("STAT_OFAS_GEN_ERREUR") + e.getMessage());
                getMemoryLog().logMessage(getSession().getLabel("STAT_OFAS_GEN_ERREUR"), FWMessage.FATAL,
                        this.getClass().toString());
                processState = false;
            }
        } else {
            // information dans le mail mais processus en succès
            getMemoryLog().logMessage(getSession().getLabel("STAT_OFAS_INFO_NO_CE"), FWMessage.INFORMATION,
                    this.getClass().toString());
        }
        System.out.println("fin du processus de génération des statistiques OFAS");
        return processState;
    }

    public String getAnnee() {
        return annee;
    }

    /**
     * Retourne une catégorie salariale en fonction d'un controle et d'une année
     * 
     * @param controlEmployeur
     * @param annee
     * @return
     * @throws HerculeException
     * @throws Exception
     */
    private String getCategorieMasseSalariale(CEControleEmployeur controlEmployeur, String annee)
            throws HerculeException, Exception {
        String categorieSalariale = CEControleEmployeurService.findCategorieMasse(getSession(), controlEmployeur
                .getAffiliation().getAffilieNumero(), annee, annee, 0);
        if (ICEControleEmployeur.CATEGORIE_MASSE_1.equals(categorieSalariale)) {
            categorieSalariale = CEStatOfasStructure.CATEGORIE_1;
        }
        return categorieSalariale;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("STAT_OFAS_ERREUR");
        } else {
            return getSession().getLabel("STAT_OFAS_OK");
        }
    }

    /**
     * Retourne la liste des employeurs actifs au 31.12 (pour le point 3.3 de la statistiques)
     * 
     * @return
     * @throws Exception
     */
    private List<CEStatOfasEmployeurActifBean> getEmployeurActifs() throws Exception {
        int annee = Integer.valueOf(getAnnee()).intValue();
        int anneePrecedente = annee - 1;
        int param1 = anneePrecedente;
        int param2 = new Integer(annee + "0101").intValue();
        int param3 = new Integer(annee + "1231").intValue();
        int param4 = new Integer(anneePrecedente + "1231").intValue();

        List<CEStatOfasEmployeurActifBean> listEmployeurActifBean = new ArrayList<CEStatOfasEmployeurActifBean>();
        BPreparedStatement psEmployeurActif = new BPreparedStatement(getTransaction());

        try {
            psEmployeurActif.prepareStatement(getSqlEmployeurActif());
            psEmployeurActif.clearParameters();
            psEmployeurActif.setInt(1, param1);
            psEmployeurActif.setInt(2, param2);
            psEmployeurActif.setInt(3, param3);
            psEmployeurActif.setInt(4, param4);
            ResultSet resultEmployeurActif = psEmployeurActif.executeQuery();
            while (resultEmployeurActif.next()) {
                CEStatOfasEmployeurActifBean empActifBean = new CEStatOfasEmployeurActifBean();
                empActifBean.setNumeroAffilie(resultEmployeurActif.getString(1));
                empActifBean.setDateDebutAffiliation(resultEmployeurActif.getInt(2));
                empActifBean.setDateFinAffiliation(resultEmployeurActif.getInt(3));
                empActifBean.setDateDebutCotisation(resultEmployeurActif.getInt(4));
                empActifBean.setDateFinCotisation(resultEmployeurActif.getInt(5));
                empActifBean.setCumulMasse(resultEmployeurActif.getDouble(6));
                listEmployeurActifBean.add(empActifBean);
            }
        } catch (Exception e) {
            JadeLogger
                    .error(CEStatOfasProcess.class,
                            "Une erreur s'est produite lors de l'exécution de la requête pour les stats sur les employeurs actifs");
            throw new Exception(
                    "Une erreur s'est produite lors de l'exécution de la requête pour les stats sur les employeurs actifs",
                    e);
        } finally {
            if (psEmployeurActif != null) {
                psEmployeurActif.closePreparedStatement();
                psEmployeurActif.closeStatement();
            }
        }

        return listEmployeurActifBean;
    }

    /**
     * Retourne le nombre d'annee écoulée depuis le dernier controle. Si il s'agit du premier controle la valeur 1 est
     * retournée, si le nombre dépasse 9 la méthode retourne 1
     * 
     * @return
     * @throws Exception
     */
    public String getNbAnneeDernierControle(CEControleEmployeur controlEmployeur) throws Exception {
        CEControleEmployeur precedentControl = CEUtils.recherchePrecedentControle(controlEmployeur.getAffiliationId(),
                controlEmployeur.getIdControleEmployeur(), getSession());
        if (precedentControl != null) {
            String anneePrecedentControl = JADate.getYear(precedentControl.getDateEffective()).toString();
            int anneePrecedentControlInt = Integer.valueOf(anneePrecedentControl);
            int anneeStatInt = Integer.valueOf(getAnnee());
            int nbAnneeDernierControl = anneeStatInt - anneePrecedentControlInt;
            // si la soustraction donne zero on retourne 1 année
            if (nbAnneeDernierControl <= 0) {
                return "1";
            }
            // si la soustraction donne une valeur supérieure à 9 on insérère le cas dans l'année 1
            else if (nbAnneeDernierControl > 9) {
                return "9";
            }
            // si la valeur se trouve entre 1 et 9 on retourne le nombre calculé
            else {
                return Integer.toString(nbAnneeDernierControl);
            }
        } else {
            return "1";
        }
    }

    /**
     * Retourne le résultat d'un contrôle d'employeur (paiement d'arriérés, remboursement ou sans particularité) ainsi
     * que la masse salariale associée. [0]=résultat du contrôle, [1]=masse salariale
     * 
     * @param controlEmployeur
     * @return
     * @throws Exception
     */
    public String[] getResultatControlAndMasse(CEControleEmployeur controlEmployeur) throws Exception {
        String[] resultatAndMasse = new String[2];
        DSDeclarationListViewBean declarationManager = new DSDeclarationListViewBean();
        declarationManager.setSession(getSession());
        declarationManager.setForTypeDeclaration(DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR);
        declarationManager.setForIdControlEmployeur(controlEmployeur.getIdControleEmployeur());
        declarationManager.find(getTransaction());
        if (declarationManager.size() > 0) {
            Double masseSalaireAvsDoubleTotale = new Double(0);
            for (int i = 0; i < declarationManager.size(); i++) {
                DSDeclarationViewBean declaration = (DSDeclarationViewBean) declarationManager.get(i);
                String masseSalaireAvs = declaration.getMasseSalTotal().replace("'", "");
                if (JadeStringUtil.isBlank(masseSalaireAvs)) {
                    // salaire null ou vide
                    masseSalaireAvs = "0";
                }
                masseSalaireAvsDoubleTotale += Double.parseDouble(masseSalaireAvs);
            }
            if (masseSalaireAvsDoubleTotale.doubleValue() > 0) {
                resultatAndMasse[0] = CEStatOfasStructure.RESULTAT_CONTROL_ARRIERE;
                resultatAndMasse[1] = masseSalaireAvsDoubleTotale.toString();
                return resultatAndMasse;

            } else {
                resultatAndMasse[0] = CEStatOfasStructure.RESULTAT_CONTROL_REMBOURSEMENT;
                resultatAndMasse[1] = masseSalaireAvsDoubleTotale.toString();
                return resultatAndMasse;
            }
        } else {
            resultatAndMasse[0] = CEStatOfasStructure.RESULTAT_CONTROL_SANS_PARTICULARITE;
            resultatAndMasse[1] = "0";
            return resultatAndMasse;
        }
    }

    private String getSqlEmployeurActif() {
        String schema = Jade.getInstance().getDefaultJdbcSchema();

        StringBuffer sql = new StringBuffer();
        sql.append("select malnaf,maddeb,madfin,meddeb,medfin,sum(cumulmasse) from ");
        sql.append(schema + ".afaffip aff ");
        sql.append("inner join " + schema + ".afplafp pla on (aff.maiaff=pla.maiaff and pla.MUBINA = '2') ");
        sql.append("inner join "
                + schema
                + ".afcotip cot on (pla.muipla=cot.muipla and cot.MEBMER in (2,0) and cot.INACTIVE = '2' and cot.METMOT <> 803037) ");
        sql.append("inner join " + schema + ".afassup ass on (cot.mbiass=ass.mbiass) ");
        sql.append("left join "
                + schema
                + ".cacptap ca on (aff.htitie=ca.idtiers and aff.malnaf=ca.idexternerole and ca.idrole in (517002, 517039)) ");
        sql.append("left join "
                + schema
                + ".cacptrp cr on (ca.idcompteannexe=cr.IDCOMPTEANNEXE and cr.ANNEE = ? and cr.idrubrique = ass.mbirub) ");
        sql.append("where maddeb < ? ");
        sql.append("and (madfin = 0 or madfin > ?) ");
        sql.append("and ? between meddeb and case when medfin = 0 then 99999999 else medfin end ");
        sql.append("and ass.mbtgen=801001 ");
        sql.append("and ass.mbttyp=812001 ");
        sql.append("group by malnaf,maddeb,madfin,meddeb,medfin");

        return sql.toString();
    }

    /**
     * Retourne le type de réviseur pour la statistique en fonction du control d'employeur
     * 
     * @param controlEmployeur
     * @return
     * @throws Exception
     */
    public String getTypeReviseur(CEControleEmployeur controlEmployeur) throws Exception {
        // Recherche du réviseur
        String idReviseur = controlEmployeur.getIdReviseur();
        if (!JadeStringUtil.isBlank(idReviseur)) {
            CEReviseur reviseur = new CEReviseur();
            reviseur.setSession(getSession());
            reviseur.setIdReviseur(idReviseur);
            reviseur.retrieve(getTransaction());
            if ((reviseur != null) && !JadeStringUtil.isBlank(reviseur.getTypeReviseur())) {
                if (reviseur.getTypeReviseur().equals(CEReviseur.TYPE_REV_EXT_SUVA)) {
                    return CEStatOfasStructure2.REVISEUR_SUVA;
                } else if (reviseur.getTypeReviseur().equals(CEReviseur.TYPE_REV_EXT_RSA)) {
                    return CEStatOfasStructure2.REVISEUR_RSA;
                } else if (reviseur.getTypeReviseur().equals(CEReviseur.TYPE_REV_EXT_SANS_SUVA)) {
                    return CEStatOfasStructure2.REVISEUR_AUTRE;
                } else if (reviseur.getTypeReviseur().equals(CEReviseur.TYPE_REV_INTERNE)) {
                    return CEStatOfasStructure2.REVISEUR_INTERNE;
                } else {
                    return CEStatOfasStructure2.REVISEUR_AUTRE;
                }
            } else {
                // si le réviseur n'est pas connu on le met dans la rubrique "autre"
                return CEStatOfasStructure2.REVISEUR_AUTRE;
            }
        } else {
            return CEStatOfasStructure2.REVISEUR_AUTRE;
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Publication du fichier XML
     * 
     * @param path
     */
    private void publishStatOfasXmlFile(String path) {
        try {
            // Publication du fichier
            if (!JadeStringUtil.isBlank(path)) {
                // on utilise le docinfo ainsi que le mail du processus
                this.registerAttachedDocument(null, path);
            } else {
                JadeThread.logError(this.getClass().toString(), "The pathFile is not valid ");
            }
        } catch (Exception e) {
            JadeThread.logError(this.getClass().toString(), "Unable to publish file ! " + e.getMessage());
        }
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

}
