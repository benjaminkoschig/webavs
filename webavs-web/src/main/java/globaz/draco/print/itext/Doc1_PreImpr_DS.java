/*
 * Créé le 27 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.draco.print.itext;

import globaz.commons.nss.NSUtil;
import globaz.draco.application.DSApplication;
import globaz.draco.db.preimpression.DSPreImpressionDeclarationListViewBean;
import globaz.draco.db.preimpression.DSPreImpressionViewBean;
import globaz.globall.db.BProcess;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.util.CIUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;

/**
 * Set dans les champs de la liste de pré-impression des déclarations de salaire les valeurs du Manager
 * 
 * @author sda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class Doc1_PreImpr_DS extends DSPreImpressionDeclarationListViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List container = null;
    private Boolean convertnnss = new Boolean(false);
    // private CICompteIndividuel entity = null;
    private DSPreImpressionViewBean entity = null;
    private DSPreImpressionViewBean entitySave = null;
    int i = 0;
    private Boolean imprimerVide = new Boolean(false);
    private Boolean isAf = new Boolean(false);
    // CIExceptions excp;
    private Iterator itcontainer = null;
    private boolean lastRetraite = false;
    private BProcess process = null;
    int report = 9;
    int sizeForRentier = 0;

    // private boolean isAucunAssure= false;
    public Doc1_PreImpr_DS() {
        super();
    }

    public Collection getCollectionData() throws JRException {

        if (container == null) {
            i = 0;
            report = 8;
            container = new ArrayList();
            // Spécifique AGLSNE, impression vide

            if (imprimerVide.booleanValue() && !isAf.booleanValue()) {
                Map map = new HashMap();
                if (size() < 8) {
                    for (int i = size(); i < 8; i++) {

                        map.put("COL_1", "");
                        map.put("COL_2", "");
                        map.put("COL_3", "");
                        map.put("COL_4", "");
                        map.put("COL_5", "");
                        map.put("COL_7", "");
                        map.put("COL_8", "");
                        map.put("COL_9", "");
                        container.add(map);
                    }
                }
                if (size() > 8) {
                    int res = size() - 8;
                    int res2 = res / 16;
                    int total = res2 * 16;
                    int resFinal = res - total;

                    for (int i = resFinal; i < 16; i++) {
                        map.put("COL_1", "");
                        map.put("COL_2", "");
                        map.put("COL_3", "");
                        map.put("COL_4", "");
                        map.put("COL_5", "");
                        map.put("COL_7", "");
                        map.put("COL_8", "");
                        map.put("COL_9", "");
                        container.add(map);
                    }
                }
                return container;

            } else if (isAf.booleanValue()) {

                for (int i = 0; i < 25; i++) {
                    Map map = new HashMap();
                    if (i == 8) {
                        map.put("COL_1", "");
                        map.put("COL_2", "REPORT");
                        map.put("COL_3", "");
                        map.put("COL_4", "");
                        map.put("COL_5", "");
                        map.put("COL_7", "");
                        map.put("COL_8", "");
                        map.put("COL_9", "");
                    } else {
                        map.put("COL_1", "");
                        map.put("COL_2", "");
                        map.put("COL_3", "");
                        map.put("COL_4", "");
                        map.put("COL_5", "");
                        map.put("COL_7", "");
                        map.put("COL_8", "");
                        map.put("COL_9", "");
                    }
                    container.add(map);
                }

                return container;
            }

            while (next()) {
                try {
                } catch (Exception e) {
                    // TODO Bloc catch auto-généré
                    e.printStackTrace();
                }
                Map map = new HashMap();
                map.put("COL_1", getFieldValue("COL_1"));
                map.put("COL_2", getFieldValue("COL_2"));
                map.put("COL_3", getFieldValue("COL_3"));
                map.put("COL_4", getFieldValue("COL_4"));
                map.put("COL_5", getFieldValue("COL_5"));
                map.put("COL_6", getFieldValue("COL_6"));
                map.put("COL_7", getFieldValue("COL_7"));
                map.put("COL_8", getFieldValue("COL_8"));
                map.put("COL_9", getFieldValue("COL_9"));

                container.add(map);
                i++;
                if ((i == report) && (sizeForRentier != report)) {
                    Map map2 = new HashMap();
                    map2.put("COL_1", "");
                    map2.put("COL_2", "REPORT");
                    map2.put("COL_3", "");
                    map2.put("COL_4", "");
                    map2.put("COL_5", "");
                    map2.put("COL_6", "");
                    map2.put("COL_7", "");
                    map2.put("COL_8", "");
                    map2.put("COL_9", "");

                    container.add(map2);
                    report = report + 16;
                }
            }
            if (!next()) {
                Map map = new HashMap();
                if (sizeForRentier < 8) {
                    for (int i = sizeForRentier; i < 8; i++) {

                        map.put("COL_1", "");
                        map.put("COL_2", "");
                        map.put("COL_3", "");
                        map.put("COL_4", "");
                        map.put("COL_5", "");
                        map.put("COL_7", "");
                        map.put("COL_8", "");
                        map.put("COL_9", "");
                        container.add(map);
                    }
                }
                if (sizeForRentier > 8) {
                    int res = sizeForRentier - 8;
                    int res2 = res / 16;
                    int total = res2 * 16;
                    int resFinal = res - total;

                    for (int i = resFinal; i < 16; i++) {
                        map.put("COL_1", "");
                        map.put("COL_2", "");
                        map.put("COL_3", "");
                        map.put("COL_4", "");
                        map.put("COL_5", "");
                        map.put("COL_7", "");
                        map.put("COL_8", "");
                        map.put("COL_9", "");
                        container.add(map);
                    }
                }
            }
            /*
             * } else if (isAucunAssure){ Map map = new HashMap(); if(size()<8){ for(int i=size();i<8;i++){
             * 
             * map.put("COL_1", ""); map.put("COL_2", ""); map.put("COL_3", ""); map.put("COL_4", ""); map.put("COL_5",
             * ""); container.add(map); }} if(size()>8){ int res =size()-8; int res2=res/16; int total = res2*16; int
             * resFinal=res-total;
             * 
             * for(int i=resFinal;i<16;i++){ map.put("COL_1", ""); map.put("COL_2", ""); map.put("COL_3", "");
             * map.put("COL_4", ""); map.put("COL_5", ""); container.add(map); } }
             */
        }
        return container;
    }

    public Boolean getConvertnnss() {
        return convertnnss;
    }

    private Object getFieldValue(String fieldName) throws JRException {
        // Verify si le compteIndividuel change -> nouveau document pour
        // l'impression de liste de document
        try {
            // retourne chaque champ
            if (fieldName.equals("COL_1")) {
                String NNSS = "";
                if (getConvertnnss().booleanValue()) {
                    if (entity.getNumeroAvs().length() <= 11) {
                        NNSS = NSUtil.returnNNSS(getSession(), entity.getNumeroAvs());
                    }
                    if (JadeStringUtil.isBlankOrZero(NNSS)) {
                        NNSS = entity.getNumeroAvs();
                    }

                } else {
                    NNSS = entity.getNumeroAvs();
                }
                return NSUtil.formatAVSUnknown(NNSS);
            }
            if (fieldName.equals("COL_2")) {
                return entity.getNomPrenom();
            }
            if (fieldName.equals("COL_3")) {
                if (JACalendar.getDay(CIUtil.formatDate(entity.getDateEngagement())) == 0) {
                    return "";
                } else {
                    return String.valueOf(JACalendar.getDay(CIUtil.formatDate(entity.getDateEngagement())));
                }
            }
            if (fieldName.equals("COL_4")) {
                if (JACalendar.getMonth(CIUtil.formatDate(entity.getDateEngagement())) == 0) {
                    return "";
                } else {
                    return String.valueOf(JACalendar.getMonth(CIUtil.formatDate(entity.getDateEngagement())));
                }
            }
            // On teste si la date de licenciement se termine bien dans l'année
            // en cours. Si ce n'est pas le cas on n'imprime pas la date de
            // licenciement
            if (fieldName.equals("COL_5")) {
                if ((JACalendar.getDay(CIUtil.formatDate(entity.getDateLicenciement())) == 0)
                        || (Integer.parseInt(String.valueOf(JACalendar.getYear(CIUtil.formatDate(entity
                                .getDateLicenciement())))) > Integer.parseInt(getAnnee()))) {
                    return "";
                } else {
                    return String.valueOf(JACalendar.getDay(CIUtil.formatDate(entity.getDateLicenciement())));
                }
            }
            if (fieldName.equals("COL_6")) {
                if ((JACalendar.getMonth(CIUtil.formatDate(entity.getDateLicenciement())) == 0)
                        || (Integer.parseInt(String.valueOf(JACalendar.getYear(CIUtil.formatDate(entity
                                .getDateLicenciement())))) > Integer.parseInt(getAnnee()))) {
                    return "";
                } else {
                    return String.valueOf(JACalendar.getMonth(CIUtil.formatDate(entity.getDateLicenciement())));
                }
            }
            if (fieldName.equals("COL_7")) {
                if (JadeStringUtil.isBlankOrZero(entity.getCategoriePersonnel())) {
                    return "";
                } else {
                    try {
                        return getSession().getCode(entity.getCategoriePersonnel());

                    } catch (Exception e) {
                        return "";
                    }
                }
            }
            if (fieldName.equals("COL_8")) {
                return getAnnee();
            }
            DSApplication application = (DSApplication) getSession().getApplication();
            if (fieldName.equals("COL_9")) {
                return application.getCantonDefaut();
            }

        } catch (Exception e) {
            // TODO Bloc catch auto-généré
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @return
     */
    public Boolean getImprimerVide() {
        return imprimerVide;
    }

    public Boolean getIsAf() {
        return isAf;
    }

    /**
     * remet it container à null
     * 
     * @see net.sf.jasperreports.engine.JRRewindableDataSource#moveFirst()
     */
    public void moveFirst() throws JRException {
        itcontainer = null;
    }

    public boolean next() throws net.sf.jasperreports.engine.JRException {
        entitySave = entity;
        entity = null;

        try {
            if (itcontainer == null) {
                this.find(0);
                itcontainer = getContainer().iterator();
                sizeForRentier = size();
                // if (!itcontainer.hasNext()){
                // isAucunAssure = true;
                // }
            }
            // Si c'est un rentier, on retourne deux fois l'entité
            if (entitySave != null) {

                if (CIUtil.isAnneeRetraite(new JADate(entitySave.getDateNaissance()), entitySave.getSexe(),
                        Integer.parseInt(getAnnee()))) {
                    if (!lastRetraite) {
                        lastRetraite = true;
                        entity = entitySave;
                        entity.setDateEngagement("00.00." + getAnnee());
                        sizeForRentier++;
                    } else {
                        lastRetraite = false;
                        entitySave = null;
                    }

                }
            }
            // lit le nouveau entity
            if (itcontainer.hasNext() && (lastRetraite != true)) {
                entity = (DSPreImpressionViewBean) itcontainer.next();
                // entitySave = null;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // vrai : il existe une entity, faux fin du select
        if (((process != null) && process.isAborted())) {
            return false;
        } else {
            return (entity != null);
        }

    }

    /**
     * remet container à null
     * 
     * @param list
     */
    public void setContainer() {
        container = null;
    }

    public void setConvertnnss(Boolean convertnnss) {
        this.convertnnss = convertnnss;
    }

    /**
     * @param boolean1
     */
    public void setImprimerVide(Boolean boolean1) {
        imprimerVide = boolean1;
    }

    public void setIsAf(Boolean isAf) {
        this.isAf = isAf;
    }

}
