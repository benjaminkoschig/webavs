/*
 * Créé le 14 févr. 07
 */
package globaz.naos.itext.controleEmployeur;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.controleEmployeur.AFControleEmployeur;
import globaz.naos.db.controleEmployeur.AFControleEmployeurManager;
import globaz.naos.db.controleEmployeur.AFControlesAEffectuer;
import globaz.naos.db.controleEmployeur.AFControlesAEffectuerManager;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.CommonProperties;
import globaz.webavs.common.ICommonConstantes;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author hpe
 * 
 */
public class AFListeControlesAEffectuerExcel {

    // Classe interne qui défini les clés pour le tri par canton et type de
    // prestations
    private class Key {
        public String idAffilie;

        /**
         * (non-Javadoc)
         * 
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         * 
         * @param o
         *            DOCUMENT ME!
         * 
         * @return DOCUMENT ME!
         */
        public int compareTo(Object o) {
            Key key = (Key) o;

            if (idAffilie.compareTo(key.idAffilie) != 0) {
                return idAffilie.compareTo(key.idAffilie);
            } else {
                return 0;
            }
        }

        /**
         * @param obj
         *            DOCUMENT ME!
         * 
         * @return DOCUMENT ME!
         */
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Key)) {
                return false;
            }
            Key key = (Key) obj;
            return ((key.idAffilie.equals(idAffilie)));
        }

        /**
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         * 
         * @return DOCUMENT ME!
         */
        @Override
        public int hashCode() {
            return (idAffilie).hashCode();
        }
    }

    public final static String NUM_REF_INFOROM_LISTE_CONTROLE_AEFFECTUER = "0208CAF";
    private static final String PAGE_SEPARATOR = "/";
    private String annee = new String();
    private int compt = 0;
    private AFControlesAEffectuer controlePrecedent = null;

    private AFControlesAEffectuer controlePrecedent_2 = null;
    private String date_impression = new String();
    private String genreControle = new String();
    private Boolean isAvecReviseur = null;

    Map map = new Hashtable();
    private String masseSalA = new String();

    private String masseSalDe = new String();
    private int nbLigne = 0;
    BProcess parent = null;
    private BProcess processAppelant = null;
    private BSession session;

    private HSSFSheet sheet1;
    private String typeAdresse = new String();
    private String user = new String();
    private boolean verifAfter = false;

    private HSSFWorkbook wb;

    // créé la feuille xls
    public AFListeControlesAEffectuerExcel(BSession session, BProcess parent) {
        this.session = session;
        wb = new HSSFWorkbook();
        this.parent = parent;
        setDate_impression(JACalendar.today().getDay() + "." + JACalendar.today().getMonth() + "."
                + JACalendar.today().getYear());
    }

    /**
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    public String getAnnee1() {
        return String.valueOf(Integer.parseInt(getAnnee()) - 1);
    }

    public String getAnnee2() {
        return String.valueOf(Integer.parseInt(getAnnee()) - 2);
    }

    public String getAnnee3() {
        return String.valueOf(Integer.parseInt(getAnnee()) - 3);
    }

    public String getAnnee4() {
        return String.valueOf(Integer.parseInt(getAnnee()) - 4);
    }

    /**
     * @return
     */
    public String getDate_impression() {
        return date_impression;
    }

    /**
     * @return
     */
    public String getGenreControle() {
        return genreControle;
    }

    public Boolean getIsAvecReviseur() {
        return isAvecReviseur;
    }

    /**
     * @return
     */
    public String getMasseSalA() {
        return masseSalA;
    }

    /**
     * @return
     */
    public String getMasseSalDe() {
        return masseSalDe;
    }

    /*
     * méthode pour gérer le fichier en sortie
     */
    public String getOutputFile() {
        try {
            File f = File.createTempFile(" " + getDate_impression() + " - ", ".xls");
            f.deleteOnExit();
            FileOutputStream out = new FileOutputStream(f);
            wb.write(out);
            out.close();
            return f.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return "";
        }
    }

    /**
     * @return
     */
    public BProcess getProcessAppelant() {
        return processAppelant;
    }

    /**
     * @return
     */
    public BSession getSession() {
        return session;
    }

    public String getTypeAdresse() {
        return typeAdresse;
    }

    /**
     * @return
     */
    public String getUser() {
        return user;
    }

    /**
     * initialisation de la feuille xls
     */
    public HSSFSheet populateSheetListe(AFControlesAEffectuerManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = null;
        AFControlesAEffectuer controlesAEffectuer = null;
        // style
        HSSFCellStyle styleAlignLeft = setStyleAlignLeft();
        HSSFCellStyle styleAlignLeftWrap = setStyleAlignLeftWrap();
        HSSFCellStyle styleAlignCenter = setStyleAlignCenter();
        HSSFCellStyle styleAlignRight = setStyleAlignRight();

        // création de la page
        setSheetListe();

        statement = manager.cursorOpen(transaction);

        int debutTableauDonnes = 14;
        try {
            // parcours du manager et remplissage des cell
            while (((controlesAEffectuer = (AFControlesAEffectuer) manager.cursorReadNext(statement)) != null)
                    && (!controlesAEffectuer.isNew())) {

                if (parent.isAborted()) {
                    break;
                }
                if (controlesAEffectuer != null) {
                    // Définition de la périodicité
                    int periodicite = 0;

                    // on vérifie que le cas est bien le dernier control pour
                    // cet affilié. si oui on ajoute la ligne, sinon on ne prend
                    // pas le cas.
                    if ((controlePrecedent != null) && verifAfter
                            && !controlePrecedent.getIdAffilie().equals(controlesAEffectuer.getIdAffilie())) {
                        Key k = new Key();
                        k.idAffilie = controlesAEffectuer.getIdAffilie();
                        if (((!isAvecReviseur.booleanValue() && JadeStringUtil.isEmpty(controlePrecedent
                                .getVisaReviseur())) || (isAvecReviseur.booleanValue()))
                                && !(!map.containsKey(k.idAffilie))) {
                            AFAffiliation affiliePrecedent = new AFAffiliation();
                            affiliePrecedent.setId(controlePrecedent.getIdAffilie());
                            affiliePrecedent.setIdTiers(controlePrecedent.getIdTiers());
                            affiliePrecedent.setSession(getSession());
                            affiliePrecedent.retrieve();

                            map.put(controlePrecedent.getIdAffilie(), "");
                            nbLigne++;

                            // Reprise des données du tiers
                            TITiersViewBean tiersPrecedent = affiliePrecedent.getTiers();

                            // Récupération du dernier control pour cet affilié
                            String dernierControlDate = "-";
                            String dernierControlReviseur = "-";
                            AFControleEmployeurManager dernierControlManager = new AFControleEmployeurManager();
                            dernierControlManager.setSession(getSession());
                            dernierControlManager.setForAffiliationId(controlePrecedent.getIdAffilie());
                            dernierControlManager.setForLastControlEffectif(true);
                            dernierControlManager.find();
                            if (dernierControlManager.size() != 0) {
                                AFControleEmployeur dernierControlEffectif = (AFControleEmployeur) dernierControlManager
                                        .getFirstEntity();
                                if (!JadeStringUtil.isEmpty(dernierControlEffectif.getDateEffective())) {
                                    dernierControlDate = dernierControlEffectif.getDateEffective().substring(3);
                                }
                                if (!JadeStringUtil.isEmpty(dernierControlEffectif.getControleurVisa())) {
                                    dernierControlReviseur = dernierControlEffectif.getControleurVisa();
                                }
                            }
                            debutTableauDonnes++;
                            HSSFRow row = sheet1.createRow(debutTableauDonnes);
                            HSSFCellStyle myStyle = wb.createCellStyle();
                            HSSFCell cell;

                            // Reprise des données du tiers
                            TITiersViewBean tiers = affiliePrecedent.getTiers();
                            TIAdresseDataSource adresseTiers = new TIAdresseDataSource();
                            if (getTypeAdresse().equals("courrier")) {
                                adresseTiers = tiers
                                        .getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                                                ICommonConstantes.CS_APPLICATION_COTISATION,
                                                JACalendar.todayJJsMMsAAAA(), true);
                            } else {
                                adresseTiers = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                                        IConstantes.CS_APPLICATION_DEFAUT, JACalendar.todayJJsMMsAAAA(), true);
                            }
                            if (adresseTiers != null) {
                                Hashtable adresse = adresseTiers.getData();
                                // N° Affilié
                                cell = row.createCell((short) 0);
                                cell.setCellValue(affiliePrecedent.getAffilieNumero());
                                myStyle = styleAlignLeftWrap;
                                cell.setCellStyle(myStyle);

                                // Nom
                                cell = row.createCell((short) 1);
                                cell.setCellValue(tiers.getNom());
                                myStyle = styleAlignLeftWrap;
                                cell.setCellStyle(myStyle);

                                // Rue
                                cell = row.createCell((short) 2);
                                cell.setCellValue((String) adresse.get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE));
                                myStyle = styleAlignLeftWrap;
                                cell.setCellStyle(myStyle);

                                // numéro
                                cell = row.createCell((short) 3);
                                cell.setCellValue((String) adresse.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO));
                                myStyle = styleAlignLeftWrap;
                                cell.setCellStyle(myStyle);

                                // Case Postale
                                cell = row.createCell((short) 4);
                                cell.setCellValue((String) adresse
                                        .get(TIAbstractAdresseDataSource.ADRESSE_VAR_CASE_POSTALE));
                                myStyle = styleAlignLeftWrap;
                                cell.setCellStyle(myStyle);

                                // NPA
                                cell = row.createCell((short) 5);
                                cell.setCellValue((String) adresse.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA));
                                myStyle = styleAlignLeftWrap;
                                cell.setCellStyle(myStyle);

                                // Localité
                                cell = row.createCell((short) 6);
                                cell.setCellValue((String) adresse
                                        .get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE));
                                myStyle = styleAlignLeftWrap;
                                cell.setCellStyle(myStyle);

                                // Dernier contrôles
                                cell = row.createCell((short) 7);
                                cell.setCellValue(dernierControlDate);
                                myStyle = styleAlignLeftWrap;
                                cell.setCellStyle(myStyle);

                                // Commentaires
                                cell = row.createCell((short) 8);
                                cell.setCellValue(" ");
                                myStyle = styleAlignLeftWrap;
                                cell.setCellStyle(myStyle);

                                // Masse sur 5 ans -1
                                cell = row.createCell((short) 9);
                                cell.setCellValue(controlesAEffectuer.getMontantMasse_1());
                                myStyle = styleAlignRight;
                                cell.setCellStyle(myStyle);

                                // Masse sur 5 ans -2
                                cell = row.createCell((short) 10);
                                cell.setCellValue(controlesAEffectuer.getMontantMasse_2());
                                myStyle = styleAlignRight;
                                cell.setCellStyle(myStyle);

                                // Masse sur 5 ans -3
                                cell = row.createCell((short) 11);
                                cell.setCellValue(controlesAEffectuer.getMontantMasse_3());
                                myStyle = styleAlignRight;
                                cell.setCellStyle(myStyle);

                                // Masse sur 5 ans -4
                                cell = row.createCell((short) 12);
                                cell.setCellValue(controlesAEffectuer.getMontantMasse_4());
                                myStyle = styleAlignRight;
                                cell.setCellStyle(myStyle);

                                // Réviseur
                                cell = row.createCell((short) 13);
                                cell.setCellValue(dernierControlReviseur);
                                myStyle = styleAlignLeftWrap;
                                cell.setCellStyle(myStyle);
                            } else {
                                // N° Affilié
                                cell = row.createCell((short) 0);
                                cell.setCellValue(affiliePrecedent.getAffilieNumero());
                                myStyle = styleAlignLeftWrap;
                                cell.setCellStyle(myStyle);

                                // Nom
                                cell = row.createCell((short) 1);
                                cell.setCellValue(tiers.getNom());
                                myStyle = styleAlignLeftWrap;
                                cell.setCellStyle(myStyle);

                                // Rue
                                cell = row.createCell((short) 2);
                                cell.setCellValue("");
                                myStyle = styleAlignLeftWrap;
                                cell.setCellStyle(myStyle);

                                // Numéro
                                cell = row.createCell((short) 3);
                                cell.setCellValue("");
                                myStyle = styleAlignLeftWrap;
                                cell.setCellStyle(myStyle);

                                // Case Postale
                                cell = row.createCell((short) 4);
                                cell.setCellValue("");
                                myStyle = styleAlignLeftWrap;
                                cell.setCellStyle(myStyle);

                                // NPA
                                cell = row.createCell((short) 5);
                                cell.setCellValue("");
                                myStyle = styleAlignLeftWrap;
                                cell.setCellStyle(myStyle);

                                // Localité
                                cell = row.createCell((short) 6);
                                cell.setCellValue("");
                                myStyle = styleAlignLeftWrap;
                                cell.setCellStyle(myStyle);

                                // Dernier contrôles
                                cell = row.createCell((short) 7);
                                cell.setCellValue(dernierControlDate);
                                myStyle = styleAlignLeftWrap;
                                cell.setCellStyle(myStyle);

                                // Commentaires
                                cell = row.createCell((short) 8);
                                cell.setCellValue(" ");
                                myStyle = styleAlignLeftWrap;
                                cell.setCellStyle(myStyle);

                                // Masse sur 5 ans -1
                                cell = row.createCell((short) 9);
                                cell.setCellValue(controlesAEffectuer.getMontantMasse_1());
                                myStyle = styleAlignRight;
                                cell.setCellStyle(myStyle);

                                // Masse sur 5 ans -2
                                cell = row.createCell((short) 10);
                                cell.setCellValue(controlesAEffectuer.getMontantMasse_2());
                                myStyle = styleAlignRight;
                                cell.setCellStyle(myStyle);

                                // Masse sur 5 ans -3
                                cell = row.createCell((short) 11);
                                cell.setCellValue(controlesAEffectuer.getMontantMasse_3());
                                myStyle = styleAlignRight;
                                cell.setCellStyle(myStyle);

                                // Masse sur 5 ans -4
                                cell = row.createCell((short) 12);
                                cell.setCellValue(controlesAEffectuer.getMontantMasse_4());
                                myStyle = styleAlignRight;
                                cell.setCellStyle(myStyle);

                                // Réviseur
                                cell = row.createCell((short) 13);
                                cell.setCellValue(dernierControlReviseur);
                                myStyle = styleAlignLeftWrap;
                                cell.setCellStyle(myStyle);
                            }
                            parent.incProgressCounter();
                        }
                    }
                    // Début des testes permettant de définir si l'entité doit
                    // être ou prise en compte
                    verifAfter = false;

                    if (JadeStringUtil.isBlankOrZero(controlesAEffectuer.getPeriodiciteAff())) {
                        periodicite = Integer.parseInt(getSession().getApplication().getProperty(
                                "periodiciteControleEmployeurCaisse", "0"));
                    } else {
                        periodicite = Integer.parseInt(controlesAEffectuer.getPeriodiciteAff());
                    }

                    // on prend l'entité si 1 ou 2 ou 3 ou 4 est vrai
                    if (
                    // 1 annee date prevue = annee
                    (controlesAEffectuer.getDatePrevue().startsWith(getAnnee()))
                    // 1 annee date prevue <= annee && date effective = vide
                    // (Integer.parseInt(controlesAEffectuer.getDatePrevue().substring(0,4))<=Integer.parseInt(getAnnee())
                    // &&
                    // JadeStringUtil.isBlankOrZero((controlesAEffectuer.getDateEffective())))

                            ||

                            // 2 annee fin du control = annee-(periode-1)
                            ((controlesAEffectuer.getDateFinControle().startsWith(String.valueOf(Integer
                                    .parseInt(getAnnee()) - periodicite - 1))) ? verifAfter = true : false)

                            ||

                            // 3 annee fin d'affiliation = annee-1
                            (controlesAEffectuer.getAffDateFin().startsWith(getAnnee1()))

                            ||

                            //
                            // 4 date precedent control = vide && annee debut
                            // affiliation = annee-periode && (date fin
                            // affiliation = vide || date fin affiliation >=
                            // 01.01.annee)
                            (JadeStringUtil.isEmpty(controlesAEffectuer.getDatePrecControle())
                                    && (Integer.parseInt(controlesAEffectuer.getAffDateDebut().substring(0, 4)) == Integer
                                            .parseInt(getAnnee()) - periodicite) && ((JadeStringUtil
                                    .isBlankOrZero(controlesAEffectuer.getAffDateFin())) || (Integer
                                    .parseInt(controlesAEffectuer.getAffDateFin()) >= Integer.parseInt(getAnnee()
                                    + "0101"))))

                            ||

                            // 5 Si aucun contrôle n'a été effectué et que la
                            // date d'affiliation est <= annee-periode
                            (JadeStringUtil.isEmpty(controlesAEffectuer.getIdControle()) && (Integer
                                    .parseInt(controlesAEffectuer.getAffDateFin()) <= Integer.parseInt(getAnnee())
                                    - periodicite))) {
                        if ((!isAvecReviseur.booleanValue() && JadeStringUtil.isEmpty(controlesAEffectuer
                                .getVisaReviseur())) || (isAvecReviseur.booleanValue())) {
                            // si on prend le cas
                            Key k = new Key();
                            k.idAffilie = controlesAEffectuer.getIdAffilie();

                            // permet de ne pas mettre 2x le même affilié dans
                            // la liste
                            if (!map.containsKey(k.idAffilie)) {

                                compt++;
                                parent.setProgressDescription(controlesAEffectuer.getNumAffilie() + " <br>");
                                if ((parent != null) && parent.isAborted()) {
                                    parent.setProgressDescription("Traitement interrompu<br> sur l'affilié : "
                                            + controlesAEffectuer.getNumAffilie() + " <br>");

                                } else {
                                    // dans le cas ou le test 2 passe, il est
                                    // nécessaire de vérifier que le cas en
                                    // question est bien le dernier control pour
                                    // cet affilié.
                                    // on ajoute donc pas tout de suite la
                                    // ligne. Si il d'une des clauses 1,3 ou 4
                                    // on ajoute la ligne directement.
                                    if (!verifAfter) {
                                        map.put(controlesAEffectuer.getIdAffilie(), "");
                                        nbLigne++;
                                        // Reprise des données de l'affilié pour
                                        // remplissage des cellules
                                        AFAffiliation affilie = new AFAffiliation();
                                        affilie.setId(controlesAEffectuer.getIdAffilie());
                                        affilie.setIdTiers(controlesAEffectuer.getIdTiers());
                                        affilie.setSession(getSession());
                                        affilie.retrieve();
                                        // Reprise des données du tiers
                                        TITiersViewBean tiers = affilie.getTiers();

                                        // Récupération du dernier control pour
                                        // cet affilié
                                        String dernierControlDate = "-";
                                        String dernierControlReviseur = "-";
                                        AFControleEmployeurManager dernierControlManager = new AFControleEmployeurManager();
                                        dernierControlManager.setSession(getSession());
                                        dernierControlManager.setForAffiliationId(controlesAEffectuer.getIdAffilie());
                                        dernierControlManager.setForLastControlEffectif(true);
                                        dernierControlManager.find();
                                        if (dernierControlManager.size() != 0) {
                                            AFControleEmployeur dernierControlEffectif = (AFControleEmployeur) dernierControlManager
                                                    .getFirstEntity();
                                            if (!JadeStringUtil.isEmpty(dernierControlEffectif.getDateEffective())) {
                                                dernierControlDate = dernierControlEffectif.getDateEffective()
                                                        .substring(3);
                                            }
                                            if (!JadeStringUtil.isEmpty(dernierControlEffectif.getControleurVisa())) {
                                                dernierControlReviseur = dernierControlEffectif.getControleurVisa();
                                            }
                                        }
                                        debutTableauDonnes++;
                                        HSSFRow row = sheet1.createRow(debutTableauDonnes);
                                        HSSFCellStyle myStyle = wb.createCellStyle();
                                        HSSFCell cell;

                                        // Reprise des données du tiers
                                        TIAdresseDataSource adresseTiers = new TIAdresseDataSource();
                                        if (getTypeAdresse().equals("courrier")) {
                                            adresseTiers = tiers.getAdresseAsDataSource(
                                                    IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                                                    ICommonConstantes.CS_APPLICATION_COTISATION,
                                                    JACalendar.todayJJsMMsAAAA(), true);
                                        } else {
                                            adresseTiers = tiers.getAdresseAsDataSource(
                                                    IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                                                    IConstantes.CS_APPLICATION_DEFAUT, JACalendar.todayJJsMMsAAAA(),
                                                    true);
                                        }
                                        if (adresseTiers != null) {
                                            Hashtable adresse = adresseTiers.getData();
                                            // N° Affilié
                                            cell = row.createCell((short) 0);
                                            cell.setCellValue(affilie.getAffilieNumero());
                                            myStyle = styleAlignLeftWrap;
                                            cell.setCellStyle(myStyle);

                                            // Nom
                                            cell = row.createCell((short) 1);
                                            cell.setCellValue(tiers.getNom());
                                            myStyle = styleAlignLeftWrap;
                                            cell.setCellStyle(myStyle);

                                            // Rue
                                            cell = row.createCell((short) 2);
                                            cell.setCellValue((String) adresse
                                                    .get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE));
                                            myStyle = styleAlignLeftWrap;
                                            cell.setCellStyle(myStyle);

                                            // numéro
                                            cell = row.createCell((short) 3);
                                            cell.setCellValue((String) adresse
                                                    .get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO));
                                            myStyle = styleAlignLeftWrap;
                                            cell.setCellStyle(myStyle);

                                            // Case Postale
                                            cell = row.createCell((short) 4);
                                            cell.setCellValue((String) adresse
                                                    .get(TIAbstractAdresseDataSource.ADRESSE_VAR_CASE_POSTALE));
                                            myStyle = styleAlignLeftWrap;
                                            cell.setCellStyle(myStyle);

                                            // NPA
                                            cell = row.createCell((short) 5);
                                            cell.setCellValue((String) adresse
                                                    .get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA));
                                            myStyle = styleAlignLeftWrap;
                                            cell.setCellStyle(myStyle);

                                            // Localité
                                            cell = row.createCell((short) 6);
                                            cell.setCellValue((String) adresse
                                                    .get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE));
                                            myStyle = styleAlignLeftWrap;
                                            cell.setCellStyle(myStyle);

                                            // Dernier contrôles
                                            cell = row.createCell((short) 7);
                                            cell.setCellValue(dernierControlDate);
                                            myStyle = styleAlignLeftWrap;
                                            cell.setCellStyle(myStyle);

                                            // Commentaires
                                            cell = row.createCell((short) 8);
                                            cell.setCellValue(" ");
                                            myStyle = styleAlignLeftWrap;
                                            cell.setCellStyle(myStyle);

                                            // Masse sur 5 ans -1
                                            cell = row.createCell((short) 9);
                                            cell.setCellValue(controlesAEffectuer.getMontantMasse_1());
                                            myStyle = styleAlignRight;
                                            cell.setCellStyle(myStyle);

                                            // Masse sur 5 ans -2
                                            cell = row.createCell((short) 10);
                                            cell.setCellValue(controlesAEffectuer.getMontantMasse_2());
                                            myStyle = styleAlignRight;
                                            cell.setCellStyle(myStyle);

                                            // Masse sur 5 ans -3
                                            cell = row.createCell((short) 11);
                                            cell.setCellValue(controlesAEffectuer.getMontantMasse_3());
                                            myStyle = styleAlignRight;
                                            cell.setCellStyle(myStyle);

                                            // Masse sur 5 ans -4
                                            cell = row.createCell((short) 12);
                                            cell.setCellValue(controlesAEffectuer.getMontantMasse_4());
                                            myStyle = styleAlignRight;
                                            cell.setCellStyle(myStyle);

                                            // Réviseur
                                            cell = row.createCell((short) 13);
                                            cell.setCellValue(dernierControlReviseur);
                                            myStyle = styleAlignLeftWrap;
                                            cell.setCellStyle(myStyle);
                                        } else {
                                            // N° Affilié
                                            cell = row.createCell((short) 0);
                                            cell.setCellValue(affilie.getAffilieNumero());
                                            myStyle = styleAlignLeftWrap;
                                            cell.setCellStyle(myStyle);

                                            // Nom
                                            cell = row.createCell((short) 1);
                                            cell.setCellValue(tiers.getNom());
                                            myStyle = styleAlignLeftWrap;
                                            cell.setCellStyle(myStyle);

                                            // Rue
                                            cell = row.createCell((short) 2);
                                            cell.setCellValue("");
                                            myStyle = styleAlignLeftWrap;
                                            cell.setCellStyle(myStyle);

                                            // numéro
                                            cell = row.createCell((short) 3);
                                            cell.setCellValue("");
                                            myStyle = styleAlignLeftWrap;
                                            cell.setCellStyle(myStyle);

                                            // Case Postale
                                            cell = row.createCell((short) 4);
                                            cell.setCellValue("");
                                            myStyle = styleAlignLeftWrap;
                                            cell.setCellStyle(myStyle);

                                            // NPA
                                            cell = row.createCell((short) 5);
                                            cell.setCellValue("");
                                            myStyle = styleAlignLeftWrap;
                                            cell.setCellStyle(myStyle);

                                            // Localité
                                            cell = row.createCell((short) 6);
                                            cell.setCellValue("");
                                            myStyle = styleAlignLeftWrap;
                                            cell.setCellStyle(myStyle);

                                            // Dernier contrôles
                                            cell = row.createCell((short) 7);
                                            cell.setCellValue(dernierControlDate);
                                            myStyle = styleAlignLeftWrap;
                                            cell.setCellStyle(myStyle);

                                            // Commentaires
                                            cell = row.createCell((short) 8);
                                            cell.setCellValue(" ");
                                            myStyle = styleAlignLeftWrap;
                                            cell.setCellStyle(myStyle);

                                            // Masse sur 5 ans -1
                                            cell = row.createCell((short) 9);
                                            cell.setCellValue(controlesAEffectuer.getMontantMasse_1());
                                            myStyle = styleAlignRight;
                                            cell.setCellStyle(myStyle);

                                            // Masse sur 5 ans -2
                                            cell = row.createCell((short) 10);
                                            cell.setCellValue(controlesAEffectuer.getMontantMasse_2());
                                            myStyle = styleAlignRight;
                                            cell.setCellStyle(myStyle);

                                            // Masse sur 5 ans -3
                                            cell = row.createCell((short) 11);
                                            cell.setCellValue(controlesAEffectuer.getMontantMasse_3());
                                            myStyle = styleAlignRight;
                                            cell.setCellStyle(myStyle);

                                            // Masse sur 5 ans -4
                                            cell = row.createCell((short) 12);
                                            cell.setCellValue(controlesAEffectuer.getMontantMasse_4());
                                            myStyle = styleAlignRight;
                                            cell.setCellStyle(myStyle);

                                            // Réviseur
                                            cell = row.createCell((short) 13);
                                            cell.setCellValue(dernierControlReviseur);
                                            myStyle = styleAlignLeftWrap;
                                            cell.setCellStyle(myStyle);
                                        }

                                        // // N° Affilié + nom
                                        // cell = row.createCell((short)0);
                                        // cell.setCellValue(affilie.getAffilieNumero()
                                        // + " - " + tiers.getNom());
                                        // myStyle = styleAlignLeftWrap;
                                        // cell.setCellStyle(myStyle);
                                        //
                                        // // Localité
                                        // cell = row.createCell((short)1);
                                        // cell.setCellValue(tiers.getLocalite());
                                        // myStyle = styleAlignLeftWrap;
                                        // cell.setCellStyle(myStyle);
                                        //
                                        // // Dernier contrôles
                                        // cell = row.createCell((short)2);
                                        // cell.setCellValue(controlesAEffectuer.getDatePrecControle());
                                        // myStyle = styleAlignLeftWrap;
                                        // cell.setCellStyle(myStyle);
                                        //
                                        // // Commentaires
                                        // cell = row.createCell((short)3);
                                        // cell.setCellValue(" ");
                                        // myStyle = styleAlignLeftWrap;
                                        // cell.setCellStyle(myStyle);
                                        //
                                        // // Masse sur 5 ans -1
                                        // cell = row.createCell((short)4);
                                        // cell.setCellValue(controlesAEffectuer.getMontantMasse_1());
                                        // myStyle = styleAlignRight;
                                        // cell.setCellStyle(myStyle);
                                        //
                                        // // Masse sur 5 ans -2
                                        // cell = row.createCell((short)5);
                                        // cell.setCellValue(controlesAEffectuer.getMontantMasse_2());
                                        // myStyle = styleAlignRight;
                                        // cell.setCellStyle(myStyle);
                                        //
                                        // // Masse sur 5 ans -3
                                        // cell = row.createCell((short)6);
                                        // cell.setCellValue(controlesAEffectuer.getMontantMasse_3());
                                        // myStyle = styleAlignRight;
                                        // cell.setCellStyle(myStyle);
                                        //
                                        // // Masse sur 5 ans -4
                                        // cell = row.createCell((short)7);
                                        // cell.setCellValue(controlesAEffectuer.getMontantMasse_4());
                                        // myStyle = styleAlignRight;
                                        // cell.setCellStyle(myStyle);
                                        //
                                        // // Réviseur
                                        // cell = row.createCell((short)8);
                                        // cell.setCellValue(controlesAEffectuer.getVisaReviseur());
                                        // myStyle = styleAlignLeftWrap;
                                        // cell.setCellStyle(myStyle);

                                        parent.incProgressCounter();
                                    }
                                }
                            }
                        }
                    }

                }
                controlePrecedent_2 = controlePrecedent;
                controlePrecedent = controlesAEffectuer;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            manager.cursorClose(statement);
        }
        // on garde des références sur les 2 entités précédentes

        return sheet1;
    }

    /**
     * @param string
     */
    public void setAnnee(String string) {
        annee = string;
    }

    /**
     * @param string
     */
    public void setDate_impression(String string) {
        date_impression = string;
    }

    /**
     * défini une police Arial gras
     * 
     * @param wb
     * @return
     */
    private HSSFFont setFontBold(HSSFWorkbook wb) {
        HSSFFont fontBold = wb.createFont();
        fontBold.setFontName(HSSFFont.FONT_ARIAL);
        fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        return fontBold;
    }

    /**
     * @param string
     */
    public void setGenreControle(String string) {
        genreControle = string;
    }

    public void setIsAvecReviseur(Boolean isAvecReviseur) {
        this.isAvecReviseur = isAvecReviseur;
    }

    /**
     * @param string
     */
    public void setMasseSalA(String string) {
        masseSalA = string;
    }

    /**
     * @param string
     */
    public void setMasseSalDe(String string) {
        masseSalDe = string;
    }

    /**
     * @param process
     */
    public void setProcessAppelant(BProcess process) {
        processAppelant = process;
    }

    /**
     * @param session
     */
    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * Création de la page
     * 
     * @return
     */
    private void setSheetListe() {
        sheet1 = wb.createSheet(getSession().getLabel("NAOS_FICHIER_CONTROLES_AEFFECTUER"));
        sheet1 = setTitleRowListe(wb, sheet1);

        // marges (unité en pouces)
        sheet1.setMargin(HSSFSheet.LeftMargin, 0);
        sheet1.setMargin(HSSFSheet.RightMargin, 0);
        sheet1.setMargin(HSSFSheet.TopMargin, 0.6);
        sheet1.setMargin(HSSFSheet.BottomMargin, 0.6);

        HSSFPrintSetup ps = sheet1.getPrintSetup();

        // format
        ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);

        // orientation
        ps.setLandscape(true);

        // marges en-tête/pied de page
        ps.setHeaderMargin(0);
        ps.setFooterMargin(0);

        // en-tête
        HSSFHeader header = sheet1.getHeader();

        // pied de page
        HSSFFooter footer = sheet1.getFooter();
        // footer.setRight("Page " + HSSFFooter.page() + "/" +
        // HSSFFooter.numPages());
        // footer.setLeft(getClass().getName().substring(getClass().getName().lastIndexOf('.')
        // + 1) + " - " + getDate_impression() + " - " +
        // getSession().getUserName());
        footer.setRight(getSession().getLabel("NAOS_FICHIER_PAGE") + HSSFFooter.page()
                + AFListeControlesAEffectuerExcel.PAGE_SEPARATOR + HSSFFooter.numPages());
        footer.setLeft(getSession().getLabel("NAOS_FICHIER_CACPAGE") + " : "
                + AFListeControlesAEffectuerExcel.NUM_REF_INFOROM_LISTE_CONTROLE_AEFFECTUER + " - "
                + this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.') + 1) + " - "
                + getDate_impression() + " - " + getSession().getUserName());

        // définition de la taille des cell
        sheet1.setColumnWidth((short) 0, (short) 5500);
        sheet1.setColumnWidth((short) 1, (short) 10000);
        sheet1.setColumnWidth((short) 2, (short) 6500);
        sheet1.setColumnWidth((short) 3, (short) 3500);
        sheet1.setColumnWidth((short) 4, (short) 4500);
        sheet1.setColumnWidth((short) 5, (short) 3500);
        sheet1.setColumnWidth((short) 6, (short) 5500);
        sheet1.setColumnWidth((short) 7, (short) 4500);
        sheet1.setColumnWidth((short) 8, (short) 6500);
        sheet1.setColumnWidth((short) 9, (short) 4500);
        sheet1.setColumnWidth((short) 10, (short) 4500);
        sheet1.setColumnWidth((short) 11, (short) 4500);
        sheet1.setColumnWidth((short) 12, (short) 4500);
        sheet1.setColumnWidth((short) 13, (short) 3500);

    }

    /**
     * alignement au centre
     * 
     * @return
     */
    private HSSFCellStyle setStyleAlignCenter() {
        HSSFCellStyle styleAlignCenter = wb.createCellStyle();
        styleAlignCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleAlignCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleAlignCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleAlignCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleAlignCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        return styleAlignCenter;
    }

    /**
     * alignement à gauche avec
     * 
     * @return
     */
    private HSSFCellStyle setStyleAlignLeft() {
        HSSFCellStyle styleAlignLeft = wb.createCellStyle();
        styleAlignLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleAlignLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleAlignLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleAlignLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleAlignLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        return styleAlignLeft;
    }

    /**
     * alignement à gauche avec WrapText
     * 
     * @return
     */
    private HSSFCellStyle setStyleAlignLeftWrap() {
        HSSFCellStyle styleAlignLeftWrap = wb.createCellStyle();
        styleAlignLeftWrap.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleAlignLeftWrap.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleAlignLeftWrap.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleAlignLeftWrap.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleAlignLeftWrap.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        styleAlignLeftWrap.setWrapText(true);
        return styleAlignLeftWrap;
    }

    /**
     * alignement à droite
     * 
     * @return
     */
    private HSSFCellStyle setStyleAlignRight() {
        HSSFCellStyle styleAlignRight = wb.createCellStyle();
        styleAlignRight.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleAlignRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleAlignRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleAlignRight.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleAlignRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        return styleAlignRight;
    }

    /**
     * défini le style des titres des colonnes
     * 
     * @param wb
     * @param fontBold
     * @return
     */
    private HSSFCellStyle setStyleTitreCol(HSSFWorkbook wb, HSSFFont fontBold) {
        HSSFCellStyle styleTitreCol = wb.createCellStyle();
        styleTitreCol.setFont(fontBold);
        styleTitreCol.setWrapText(true);
        styleTitreCol.setFont(fontBold);
        styleTitreCol.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleTitreCol.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        styleTitreCol.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        styleTitreCol.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        styleTitreCol.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        return styleTitreCol;
    }

    /**
     * défini le style des titres des critères de sélection
     * 
     * @param wb
     * @param fontBold
     * @return
     */
    private HSSFCellStyle setStyleTitreCritere(HSSFWorkbook wb, HSSFFont fontBold) {
        HSSFCellStyle styleTitreCritere = wb.createCellStyle();
        styleTitreCritere.setFont(fontBold);
        styleTitreCritere.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        return styleTitreCritere;
    }

    /**
     * méthode pour la création du style de la feuille" (entêtes, des bordures...)
     */
    private HSSFSheet setTitleRowListe(HSSFWorkbook wb, HSSFSheet sheet) {
        int compteurCritere = 0;

        final String numAffilie = getSession().getLabel("NAOS_COLONNE_NUMAFFILIE");
        final String nom = getSession().getLabel("NAOS_COLONNE_NOM");
        final String rue = getSession().getLabel("NAOS_COLONNE_RUE");
        final String casePostale = getSession().getLabel("NAOS_COLONNE_CASE_POSTALE");
        final String npa = getSession().getLabel("NAOS_COLONNE_NPA");
        final String localite = getSession().getLabel("NAOS_COLONNE_LOCALITE");
        final String dernierControle = getSession().getLabel("NAOS_COLONNE_DERCONTROLE");
        final String commentaires = getSession().getLabel("NAOS_COLONNE_COMMENTAIRES");
        final String masseMontant1 = getSession().getLabel("NAOS_COLONNE_MASSE") + " " + getAnnee1();
        final String masseMontant2 = getSession().getLabel("NAOS_COLONNE_MASSE") + " " + getAnnee2();
        final String masseMontant3 = getSession().getLabel("NAOS_COLONNE_MASSE") + " " + getAnnee3();
        final String masseMontant4 = getSession().getLabel("NAOS_COLONNE_MASSE") + " " + getAnnee4();
        final String reviseur = getSession().getLabel("NAOS_COLONNE_REVISEUR");
        final String numero = "Numéro";

        final String[] COL_TITLES = { numAffilie, nom, rue, numero, casePostale, npa, localite, dernierControle,
                commentaires, masseMontant1, masseMontant2, masseMontant3, masseMontant4, reviseur };

        HSSFRow row = null;
        HSSFCell c;

        // création du style pour le titre de la page
        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 300);
        font2.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);

        row = sheet.createRow(1);
        c = row.createCell((short) 0);
        c.setCellValue(getSession().getLabel("NAOS_EMAIL_OBJECT_CONTROLES_AEFFECTUER"));
        c.setCellStyle(style2);
        // critères de sélection
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font3.setFontHeight((short) 200);
        style3.setFont(font3);
        // row = sheet.createRow(3);
        row = sheet.createRow(4);
        // création de l'entête
        try {
            row = sheet.createRow(3);
            c = row.createCell((short) 0);
            c.setCellValue("");
            c.setCellStyle(style3);
            c = row.createCell((short) 0);
            c.setCellValue(session.getLabel("DATE_IMPRESSION"));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(getDate_impression());
            c.setCellStyle(style3);
            row = sheet.createRow(4);
            c = row.createCell((short) 0);
            c.setCellValue("");
            c.setCellStyle(style3);
            c = row.createCell((short) 0);
            c.setCellValue(getSession().getLabel("NAOS_CONTROLES_NOCAISSE"));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(getSession().getApplication().getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE));
            c.setCellStyle(style3);
            row = sheet.createRow(5);
            c = row.createCell((short) 0);
            c.setCellValue("");
            c.setCellStyle(style3);
            c = row.createCell((short) 0);
            c.setCellValue(getSession().getLabel("NAOS_CONTROLES_TYPE"));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(getSession().getCodeLibelle(getGenreControle()));
            c.setCellStyle(style3);
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellValue("");
            c.setCellStyle(style3);
            c = row.createCell((short) 0);
            c.setCellValue(getSession().getLabel("NAOS_COLONNE_AVEC_REVISEUR"));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(getIsAvecReviseur().booleanValue() ? getSession().getLabel("NAOS_COLONNE_OUI")
                    : getSession().getLabel("NAOS_COLONNE_NON"));
            c.setCellStyle(style3);
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellValue("");
            c.setCellStyle(style3);
            c = row.createCell((short) 0);
            c.setCellValue(getSession().getLabel("NAOS_COLONNE_DOMAINE_ADRESSE"));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(getTypeAdresse());
            c.setCellStyle(style3);
            // row = sheet.createRow(6);
            // c = row.createCell((short)0);
            // c.setCellValue("");
            // c.setCellStyle(style3);
            // c = row.createCell((short)0);
            // c.setCellValue("Génération du contrôle d'employeur ?");
            // c.setCellStyle(style3);
            // c = row.createCell((short)1);
            // c.setCellValue(getIsGenerationControleEmployeur().booleanValue()?"Oui":"Non");
            // c.setCellStyle(style3);
            // row = sheet.createRow(7);
            // c = row.createCell((short)0);
            // c.setCellValue("");
            // c.setCellStyle(style3);
            // c = row.createCell((short)0);
            // c.setCellValue("Tenir compte de la périodicité de la caisse ?");
            // c.setCellStyle(style3);
            // c = row.createCell((short)1);
            // c.setCellValue(getIsTenirComptePerioCaisse().booleanValue()?"Oui":"Non");
            // c.setCellStyle(style3);
            // row = sheet.createRow(8);
            // c = row.createCell((short)0);
            // c.setCellValue("");
            // c.setCellStyle(style3);
            // c = row.createCell((short)0);
            // c.setCellValue("Tenir compte de la périodicité de l'affilié ?");
            // c.setCellStyle(style3);
            // c = row.createCell((short)1);
            // c.setCellValue(getIsTenirComptePerioAffilie().booleanValue()?"Oui":"Non");
            // c.setCellStyle(style3);
            row = sheet.createRow(9);
            c = row.createCell((short) 0);
            c.setCellValue("");
            c.setCellStyle(style3);
            c = row.createCell((short) 0);
            c.setCellValue(getSession().getLabel("NAOS_COLONNE_MASSE_SALARIALE"));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(getSession().getLabel("NAOS_COLONNE_DE") + " " + getMasseSalDe() + " "
                    + getSession().getLabel("NAOS_COLONNE_A") + " " + getMasseSalA());
            c.setCellStyle(style3);
            row = sheet.createRow(10);
            c = row.createCell((short) 0);
            c.setCellValue("");
            c.setCellStyle(style3);
            c = row.createCell((short) 0);
            c.setCellValue(getSession().getLabel("NAOS_COLONNE_POUR_ANNEE"));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(getAnnee());
            c.setCellStyle(style3);

        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }

        // let's use a nifty font for the title
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);

        row = sheet.createRow(13);

        HSSFFont fontBold = setFontBold(wb);

        HSSFCellStyle styleTitreCol = setStyleTitreCol(wb, fontBold);

        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(COL_TITLES[i]);
            c.setCellStyle(styleTitreCol);
        }
        return sheet;
    }

    public void setTypeAdresse(String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }

    /**
     * @param string
     */
    public void setUser(String string) {
        user = string;
    }
}