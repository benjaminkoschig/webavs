package globaz.draco.print.itext;

import globaz.commons.nss.NSUtil;
import globaz.draco.application.DSApplication;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuelles;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuellesManager;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pyxis.constantes.IConstantes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;

public class Doc1_Remplissage extends DSInscriptionsIndividuellesManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<Map<String, Object>> container = null;
    private DSDeclarationViewBean declaration = null;
    // private CICompteIndividuel entity = null;
    private DSInscriptionsIndividuelles entity = null;
    int i = 0;
    Boolean imprimerVide = new Boolean(false);
    // CIExceptions excp;
    private Iterator<?> itcontainer = null;
    private String langueForCS = "";

    int report = 9;
    double sommeCol10 = 0;
    double sommeCol11 = 0;
    double sommeCol13 = 0;
    double sommeCol14 = 0;

    // somme des montants par colonne, utilisé pour les lignes "REPORT". La
    // somme du columnfooter est calculée automatiquement dans le fichiers
    // jasper.
    double sommeCol9 = 0;

    // private boolean isAucunAssure= false;
    public Doc1_Remplissage() {
        super();
    }

    public Collection<Map<String, Object>> getCollectionData() throws JRException {
        if (JadeStringUtil.isBlankOrZero(langueForCS)) {
            try {
                AFAffiliation affEnCours = new AFAffiliation();
                affEnCours = getDeclaration().getAffiliation();
                String langueIsoTiers = affEnCours.getTiers().getLangueIso();
                if ("fr".equalsIgnoreCase(langueIsoTiers)) {
                    langueForCS = "F";
                } else if ("de".equalsIgnoreCase(langueIsoTiers)) {
                    langueForCS = "D";
                } else if ("it".equalsIgnoreCase(langueIsoTiers)) {
                    langueForCS = "I";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (container == null) {
            i = 0;
            report = 8;
            container = new ArrayList<Map<String, Object>>();
            // Spécifique AGLSNE, impression vide
            if (imprimerVide.booleanValue()) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (size() < 8) {
                    for (int i = size(); i < 8; i++) {

                        map.put("COL_1", "");
                        map.put("COL_2", "");
                        map.put("COL_3", "");
                        map.put("COL_4", "");
                        map.put("COL_5", "");
                        map.put("COL_7", "");
                        map.put("COL_8", "");
                        map.put("COL_12", "");
                        map.put("COL_15", "");
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
                        map.put("COL_12", "");
                        map.put("COL_15", "");
                        container.add(map);
                    }
                }
                return container;

            }

            while (next()) {
                try {
                } catch (Exception e) {
                    // TODO Bloc catch auto-généré
                    e.printStackTrace();
                }
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("COL_1", getFieldValue("COL_1"));
                map.put("COL_2", getFieldValue("COL_2"));
                map.put("COL_3", getFieldValue("COL_3"));
                map.put("COL_4", getFieldValue("COL_4"));
                map.put("COL_5", getFieldValue("COL_5"));
                map.put("COL_6", getFieldValue("COL_6"));
                map.put("COL_7", getFieldValue("COL_7"));
                map.put("COL_8", getFieldValue("COL_8"));
                map.put("COL_9", getFieldValue("COL_9"));
                map.put("COL_10", getFieldValue("COL_10"));
                map.put("COL_11", getFieldValue("COL_11"));
                map.put("COL_12", getFieldValue("COL_12"));
                map.put("COL_13", getFieldValue("COL_13"));
                map.put("COL_14", getFieldValue("COL_14"));
                map.put("COL_15", getFieldValue("COL_15"));

                container.add(map);
                i++;
                if ((i == report) && (size() != report)) {
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("COL_1", "");
                    map2.put("COL_2", "REPORT");
                    map2.put("COL_3", "");
                    map2.put("COL_4", "");
                    map2.put("COL_5", "");
                    map2.put("COL_6", "");
                    map2.put("COL_7", "");
                    map2.put("COL_8", "");
                    map2.put("COL_9", new Double(sommeCol9));
                    map2.put("COL_10", new Double(sommeCol10));
                    map2.put("COL_11", new Double(sommeCol11));
                    map2.put("COL_12", "");
                    map2.put("COL_13", new Double(sommeCol13));
                    map2.put("COL_14", new Double(sommeCol14));
                    map2.put("COL_15", "");

                    container.add(map2);
                    report = report + 16;
                }
            }
            if (!next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (size() < 8) {
                    for (int j = size(); j < 8; j++) {

                        map.put("COL_1", "");
                        map.put("COL_2", "");
                        map.put("COL_3", "");
                        map.put("COL_4", "");
                        map.put("COL_5", "");
                        map.put("COL_7", "");
                        map.put("COL_8", "");
                        map.put("COL_12", "");
                        map.put("COL_15", "");
                        container.add(map);
                    }
                }
                if (size() > 8) {
                    int res = size() - 8;
                    int reste = res % 16;// reste = nombre de personnes
                    // affichées sur la dernière page

                    // on remplit la différence avec des lignes vides si
                    // reste!=0
                    if (reste != 0) {
                        for (int j = reste; j < 16; j++) {
                            map.put("COL_1", "");
                            map.put("COL_2", "");
                            map.put("COL_3", "");
                            map.put("COL_4", "");
                            map.put("COL_5", "");
                            map.put("COL_7", "");
                            map.put("COL_8", "");
                            map.put("COL_12", "");
                            map.put("COL_15", "");
                            container.add(map);
                        }
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

    private DSDeclarationViewBean getDeclaration() throws Exception {
        if ((declaration == null) || declaration.isNew()) {
            declaration = new DSDeclarationViewBean();
            declaration.setIdDeclaration(getForIdDeclaration());
            declaration.setSession(getSession());
            declaration.retrieve();
        }
        if (!declaration.isNew()) {
            return declaration;
        } else {
            return null;
        }

    }

    private Object getFieldValue(String fieldName) throws JRException {
        // Verify si le compteIndividuel change -> nouveau document pour
        // l'impression de liste de document
        try {
            // retourne chaque champ
            if (fieldName.equals("COL_1")) {
                return NSUtil.formatAVSUnknown(entity.getNumeroAvs());
            }
            if (fieldName.equals("COL_2")) {
                // On ne veut pas de substr. sur le nomPrenom
                entity.setWantSubstringNomPrenom(false);
                return entity.getNomPrenom();
            }
            if (fieldName.equals("COL_3")) {
                if (!JadeStringUtil.isBlankOrZero(entity.getMoisDebut())) {
                    return entity.getJourDebut();
                } else {
                    return "";
                }

            }
            if (fieldName.equals("COL_4")) {
                return entity.getMoisDebut();
            }
            if (fieldName.equals("COL_5")) {
                if (!JadeStringUtil.isBlankOrZero(entity.getMoisFin())) {
                    return entity.getJourFin();
                } else {
                    return "";
                }

            }
            if (fieldName.equals("COL_6")) {
                return entity.getMoisFin();
            }
            if (((DSApplication) getSession().getApplication()).isCategoriePersonne()
                    && ((DSApplication) getSession().getApplication()).isCanton()) {
                if (fieldName.equals("COL_7")) {
                    return getUserCode(entity.getCategoriePerso());
                }
                if (fieldName.equals("COL_12")) {
                    return (getUserCode(entity.getCodeCanton()));
                }
            }
            if (((DSApplication) getSession().getApplication()).isCategoriePersonne()
                    && !((DSApplication) getSession().getApplication()).isCanton()) {
                if (fieldName.equals("COL_12")) {
                    return getUserCode(entity.getCategoriePerso());
                }
            }
            if (((DSApplication) getSession().getApplication()).isCanton()
                    && !((DSApplication) getSession().getApplication()).isCategoriePersonne()) {
                if (fieldName.equals("COL_12")) {
                    return getUserCode(entity.getCodeCanton());
                }
            }

            if (fieldName.equals("COL_8")) {
                if (!JadeStringUtil.isBlankOrZero(entity.getAnneeInsc())) {
                    return entity.getAnneeInsc();
                } else {
                    try {
                        return getDeclaration().getAnnee();
                    } catch (Exception e) {
                        return "";
                    }
                }
            }
            if (fieldName.equals("COL_9")) {
                if (entity.getGenreEcriture().trim().length() == 2) {
                    if (entity.getGenreEcriture().startsWith("1")) {
                        // C'est une extourne, donc montant négatif
                        sommeCol9 = sommeCol9 - new Double(entity.getMontant()).doubleValue();
                        return new Double("-" + entity.getMontant());
                    }
                    sommeCol9 = sommeCol9 + new Double(entity.getMontant()).doubleValue();
                    return new Double(entity.getMontant());
                }
            }
            if (fieldName.equals("COL_10")) {
                sommeCol10 = sommeCol10 + new Double(entity.getACI()).doubleValue();
                return new Double(entity.getACI());
            }
            if (fieldName.equals("COL_11")) {
                sommeCol11 = sommeCol11 + new Double(entity.getACII()).doubleValue();
                return new Double(entity.getACII());
            }
            if (fieldName.equals("COL_13")) {
                sommeCol13 = sommeCol13 + new Double(entity.getMontantAf()).doubleValue();
                return new Double(entity.getMontantAf());
            }
            if (fieldName.equals("COL_14")) {
                if (IConstantes.CS_LOCALITE_CANTON_GENEVE.equals(entity.getCodeCanton())) {
                    if (entity.getGenreEcriture().trim().length() == 2) {
                        if (entity.getGenreEcriture().startsWith("1")) {
                            // C'est une extourne, donc montant négatif
                            sommeCol14 = sommeCol14 - new Double(entity.getMontant()).doubleValue();
                            return new Double("-" + entity.getMontant());
                        }
                        sommeCol14 = sommeCol14 + new Double(entity.getMontant()).doubleValue();
                        return new Double(entity.getMontant());
                    }
                } else {
                    return new Double("0.00");
                }
            }
            if (fieldName.equals("COL_15")) {
                return entity.getRemarqueControle();
            }

        } catch (Exception e) {
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

    public String getLangueForCS() {
        return langueForCS;
    }

    /**
     * Retourne le CU d'un CS par rapport à la langue
     * 
     * @param idCode
     * @return
     */
    private final String getUserCode(String idCode) {
        FWParametersUserCode userCode = new FWParametersUserCode();
        userCode.setSession(getSession());
        userCode.setIdCodeSysteme(idCode);
        userCode.setIdLangue(langueForCS);
        try {
            userCode.retrieve();
            if (!userCode.isNew()) {
                return userCode.getCodeUtilisateur();
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
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

        entity = null;

        try {
            if (itcontainer == null) {
                this.find(0);
                itcontainer = getContainer().iterator();
                // if (!itcontainer.hasNext()){
                // isAucunAssure = true;
                // }
            }
            // lit le nouveau entity
            if (itcontainer.hasNext()) {
                entity = (DSInscriptionsIndividuelles) itcontainer.next();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((entity != null) && !entity.hasShowRight()) {
            JadeLogger.trace(this, "Impression ci sécurisé : " + NSUtil.formatAVSUnknown(entity.getNumeroAvs()));
            throw new JRException("Impression ci sécurisé : " + NSUtil.formatAVSUnknown(entity.getNumeroAvs()));
        }
        return (entity != null);
    }

    /**
     * remet container à null
     * 
     * @param list
     */
    public void setContainer() {
        container = null;
    }

    /**
     * @param boolean1
     */
    public void setImprimerVide(Boolean boolean1) {
        imprimerVide = boolean1;
    }

    public void setLangueForCS(String langueForCS) {
        this.langueForCS = langueForCS;
    }
}
