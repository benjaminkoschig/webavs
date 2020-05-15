package globaz.apg.excel;

import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.vulpecula.util.I18NUtil;
import globaz.apg.db.liste.APListePandemieControleModel;
import globaz.apg.db.liste.APListePandemieSuiviDossierModel;
import globaz.apg.process.APListePandemieControleProcess;
import globaz.corvus.excel.REAbstractListExcel;
import globaz.globall.db.BSession;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.log.JadeLogger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import java.sql.SQLException;
import java.text.Normalizer;
import java.util.*;

public class APListePandemieSuiviDossierExcel  extends REAbstractListExcel {
    Map<String, String> listCSSexe;
    Map<String, String> listCSEtatCivil;
    Map<String, String> listCSPays;
    Map<String, String> listCSEtatDemande;
    Map<String, String> listCSTypeDroit;
    Map<String,String> listCSCatEtablissement;
    Map<String,String> listCSMotifGarde;
    Map<String,String> listCSQuarantaineOrdonnee;
    Map<String,String> listCSEtatDroits;
    private HSSFCellStyle styleHeader;

    List<APListePandemieSuiviDossierModel> listSuivi;
    public final static String LABEL_TITRE_DOCUMENT = "DOC_PANDEMIE_SUIVI_DOSSIER";
    private static final String HEADER_COL_1 = "DOC_LISTE_SUIVI_USERNAME";
    private static final String HEADER_COL_2 = "DOC_LISTE_SUIVI_NOM_GESTIONNAIRE";
    private static final String HEADER_COL_3 = "DOC_LISTE_SUIVI_PRENOM_GESTIONNAIRE";
    private static final String HEADER_COL_4 = "DOC_LISTE_SUIVI_NUMERO_DEMANDE";
    private static final String HEADER_COL_5 = "DOC_LISTE_SUIVI_NSS";
    private static final String HEADER_COL_6 = "DOC_LISTE_SUIVI_NOM";
    private static final String HEADER_COL_7 = "DOC_LISTE_SUIVI_PRENOM";
    private static final String HEADER_COL_8 = "DOC_LISTE_SUIVI_DATE_NAISSANCE";
    private static final String HEADER_COL_9 = "DOC_LISTE_SUIVI_SEXE";
    private static final String HEADER_COL_10 = "DOC_LISTE_SUIVI_ETAT_CIVL";
    private static final String HEADER_COL_11 = "DOC_LISTE_SUIVI_PAYS";
    private static final String HEADER_COL_12 = "DOC_LISTE_SUIVI_NUMERO_DROIT";
    private static final String HEADER_COL_13 = "DOC_LISTE_SUIVI_ETAT_DEMANDE";
    private static final String HEADER_COL_14 = "DOC_LISTE_SUIVI_TYPE_DROIT";
    private static final String HEADER_COL_15 = "DOC_LISTE_SUIVI_CAT_ETABLI";
    private static final String HEADER_COL_16 = "DOC_LISTE_SUIVI_MOTIF_GARDE";
    private static final String HEADER_COL_17 = "DOC_LISTE_SUIVI_QUARANTAINE_ORDONNEE";
    private static final String HEADER_COL_18 = "DOC_LISTE_SUIVI_DATE_DEBUT_MANIF";
    private static final String HEADER_COL_19 = "DOC_LISTE_SUIVI_DATE_FIN_MANIF";
    private static final String HEADER_COL_20 = "DOC_LISTE_SUIVI_DATE_RECEPTION";
    private static final String HEADER_COL_21 = "DOC_LISTE_SUIVI_DATE_DEPOT_DROIT";
    private static final String HEADER_COL_22 = "DOC_LISTE_SUIVI_DATE_DEBUT_DROIT";
    private static final String HEADER_COL_23 = "DOC_LISTE_SUIVI_DATE_FIN_DROIT";
    private static final String HEADER_COL_24 = "DOC_LISTE_SUIVI_ETAT_DROIT";
    private static final String HEADER_COL_25 = "DOC_LISTE_SUIVI_DATE_EXPORT";


    /**
     * @param session
     */
    public APListePandemieSuiviDossierExcel(BSession session) {
        super(session,"APListePandemieSuiviDossierExcel", session.getLabel(APListePandemieSuiviDossierExcel.LABEL_TITRE_DOCUMENT));
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            styleHeader = getWorkbook().createCellStyle();
            styleHeader.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            styleHeader.setFont(getFontBold());
            listCSSexe = getCSListeWithLibelle("PYSEXE");
            listCSEtatCivil = getCSListeWithLibelle("PYETATCIVI");
            listCSPays = getListCSPays();
            listCSEtatDemande = getCSListeWithLibelle("PRETADEMAN");
            listCSTypeDroit = getCSListeWithCodeLibelle("APGENSERVI");
            listCSCatEtablissement = getCSListeWithLibelle("APPANCATEN");
            listCSMotifGarde = getCSListeWithLibelle("APPANMOTGA");
            listCSQuarantaineOrdonnee = getCSListeWithLibelle("LEOUINON");
            listCSEtatDroits = getCSListeWithLibelle("APETADROIT");

        } catch (SQLException ex) {
            JadeLogger.error(APListePandemieSuiviDossierExcel.class, ex.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

    }


    public void creerDocument() {
        createSuiviSheet();
    }

    private void createSuiviSheet() {
        createSheet(getSession().getLabel("DOC_LISTE_SUIVI_DOSSIER"));
        initPage(true);
        createHeaderRows();
        if(!listSuivi.isEmpty()){
            createDataRows();
        }
        for (int i = 0; i < 25; i++) {
            currentSheet.autoSizeColumn((short) i);
        }
    }

    private void createDataRows() {
        Collections.sort(listSuivi, new Comparator<APListePandemieSuiviDossierModel>() {
            @Override
            public int compare(APListePandemieSuiviDossierModel t1, APListePandemieSuiviDossierModel t2) {
                String nomPrenomT1 = Normalizer.normalize( t1.getNom()+t1.getPrenom(), Normalizer.Form.NFD);
                String nomPrenomT2 = Normalizer.normalize( t2.getNom()+t2.getPrenom(), Normalizer.Form.NFD);
                if(nomPrenomT1.compareTo(nomPrenomT2) == 0){
                    Double nb1 = Double.valueOf(t1.getNss().trim().replaceAll("\\.", ""));
                    Double nb2 = Double.valueOf(t2.getNss().trim().replaceAll("\\.", ""));
                    if (nb1.compareTo(nb2) == 0) {
                        nb1 = Double.valueOf(t1.getDateDepotdroit().trim().replaceAll("\\.", ""));
                        nb2 = Double.valueOf(t2.getDateDepotdroit().trim().replaceAll("\\.", ""));
                        if (nb1.compareTo(nb2) == 0) {
                            nb1 = Double.valueOf(t1.getDateDebutDroit().trim().replaceAll("\\.", ""));
                            nb2 = Double.valueOf(t2.getDateDebutDroit().trim().replaceAll("\\.", ""));
                        }
                    }
                    nb1.compareTo(nb2);
                }
                return nomPrenomT1.compareTo(nomPrenomT2);

            }
        });
        for (APListePandemieSuiviDossierModel model : listSuivi) {
            createRow();
            this.createCell(model.getUserName());
            this.createCell(model.getNomGestionnaire());
            this.createCell(model.getPrenomGestionnaire());
            this.createCell(model.getNumeroDemande());
            this.createCell(model.getNss());
            this.createCell(model.getNom());
            this.createCell(model.getPrenom());
            this.createCell(model.getDateNaissance());
            this.createCell(listCSSexe.get(model.getSexe()));
            this.createCell(listCSEtatCivil.get(model.getEtatCivil()));
            this.createCell(listCSPays.get(model.getPays()));
            this.createCell(model.getIdDroit());
//            this.createCell(listCSEtatDemande.get(model.getEtatDemande()));
            this.createCell(listCSTypeDroit.get(model.getTypeDroit()));
            this.createCell(listCSCatEtablissement.get(model.getCategorieEtablissement()));
            this.createCell(listCSMotifGarde.get(model.getMotifGarde()));
            this.createCell(listCSQuarantaineOrdonnee.get(model.getQuarantaineOrdonne()));
            this.createCell(model.getDateDebutManif());
            this.createCell(model.getDateFinManif());
            this.createCell(model.getDateReception());
            this.createCell(model.getDateDepotdroit());
            this.createCell(model.getDateDebutDroit());
            this.createCell(model.getDateFinDroit());
            this.createCell(listCSEtatDroits.get(model.getEtatDroit()));
            this.createCell(model.getDateExport());
        }
    }

    private void createHeaderRows() {
        createRow();
        this.createCell(getSession().getLabel(HEADER_COL_1), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_2), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_3), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_4), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_5), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_6), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_7), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_8), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_9), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_10), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_11), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_12), styleHeader);
//        this.createCell(getSession().getLabel(HEADER_COL_13), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_14), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_15), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_16), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_17), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_18), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_19), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_20), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_21), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_22), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_23), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_24), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_25), styleHeader);
    }
    private Map<String,String> getCSListeWithCodeLibelle(String nomFamille){
        Map<String, String> resultTrie = new HashMap<String, String>();
        try {
            List<JadeCodeSysteme> codes = JadeBusinessServiceLocator.getCodeSystemeService()
                    .getFamilleCodeSysteme(nomFamille);
            for (JadeCodeSysteme code : codes) {
                Langues langue = I18NUtil.getLangues();
                String codeValue = code.getCodeUtilisateur(langue);
                String libelleValue = code.getTraduction(langue);
                resultTrie.put(code.getIdCodeSysteme(), codeValue + " " + libelleValue);
            }
        } catch (Exception ex) {
            JadeLogger.error(APListePandemieControleExcel.class, ex.getMessage());
        }
        return resultTrie;
    }
    private Map<String,String> getCSListeWithLibelle(String nomFamille){
        Map<String, String> resultTrie = new HashMap<String, String>();
        try {
            List<JadeCodeSysteme> codes = JadeBusinessServiceLocator.getCodeSystemeService()
                    .getFamilleCodeSysteme(nomFamille);
            for (JadeCodeSysteme code : codes) {
                Langues langue = I18NUtil.getLangues();
                String libelleValue = code.getTraduction(langue);
                resultTrie.put(code.getIdCodeSysteme(), libelleValue);
            }
        } catch (Exception ex) {
            JadeLogger.error(APListePandemieControleExcel.class, ex.getMessage());
        }
        return resultTrie;
    }



    private Map<String, String> getListCSPays() {
        Map<String, String> resultTrie = new HashMap<String, String>();
        try {
            List<JadeCodeSysteme> codes = JadeBusinessServiceLocator.getCodeSystemeService()
                    .getFamilleCodeSysteme("CIPAYORI");
            for (JadeCodeSysteme code : codes) {
                Langues langue = I18NUtil.getLangues();
                String codeValue = code.getCodeUtilisateur(langue);
                String libelleValue = code.getTraduction(langue);
                resultTrie.put(codeValue, libelleValue);
            }
        } catch (Exception ex) {
            JadeLogger.error(APListePandemieControleExcel.class, ex.getMessage());
        }
        return resultTrie;
    }

    public List<APListePandemieSuiviDossierModel> getListSuivi() {
        return listSuivi;
    }

    public void setListSuivi(List<APListePandemieSuiviDossierModel> listSuivi) {
        this.listSuivi = listSuivi;
    }
}
