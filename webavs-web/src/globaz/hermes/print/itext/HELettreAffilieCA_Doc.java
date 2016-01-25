package globaz.hermes.print.itext;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.babel.HECTConstantes;
import globaz.hermes.db.access.HEInfos;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.print.itext.util.HECAComparator;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author Alexandre Cuva, 1-mars-2005
 * @version $Id: HELettreAffilieCA_Doc.java,v 1.18 2009/07/27 10:19:34 ald Exp $
 */
public class HELettreAffilieCA_Doc extends HEDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CHAMP_CORPS = "P_CORPS";
    public final static String CHAMP_CORPS2 = "P_CORPS2";
    public final static String CHAMP_DATE_NAISSANCE = "P_DATE_NAISSANCE";
    public final static String CHAMP_N_ASSURE = "P_N_ASSURE";
    public final static String CHAMP_NOM_PRENOM = "P_NOM_PRENOM";
    public final static String CHAMP_POLITESSE = "P_POLITESSE";
    public final static String CHAMP_SIGNATURE = "P_SIGN_LETTRE";
    /** paramètre du document */
    public final static String CHAMP_TITRE = "P_TITRE";
    public final static int POSITION_DETAIL = 4;
    /** Le nom du modèle */
    private static final String TEMPLATE_NAME = "HERMES_LETTRE_CA";

    private TITiersViewBean affilie = null;
    private boolean archivage = false;
    /*****************************/
    private String idLot;

    /**
     * Initialise le document
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public HELettreAffilieCA_Doc() throws Exception {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeExecuteReport ()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        setTemplateFile(TEMPLATE_NAME);
        try {
            HEOutputAnnonceListViewBean annoncesCA = new HEOutputAnnonceListViewBean();
            annoncesCA.setForStatut(IHEAnnoncesViewBean.CS_TERMINE);
            annoncesCA.setForMotifLettreAcc("true");
            annoncesCA.setSession(getSession());
            annoncesCA.setForIdLot(getIdLot());
            annoncesCA.setIsArchivage(isArchivage());
            annoncesCA.setLikeEnregistrement("2001");
            annoncesCA.find(getTransaction(), BManager.SIZE_NOLIMIT);
            ArrayList affiliesList = getAffiliesList(annoncesCA);
            if (affiliesList != null && affiliesList.size() > 0) {
                addAllEntities(affiliesList);
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "HELettreAffilieCA_Doc.beforeExecuteReport()");
        }
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {

        // Set du numéro de document INFOROM
        if (getDocumentInfo() != null) {
            getDocumentInfo().setDocumentTypeNumber("0010CCI");
        }

        // Récupération des données
        ArrayList annoncesList = (ArrayList) currentEntity();

        String numeroAffilie = null;
        if (annoncesList != null && annoncesList.size() > 0) {
            numeroAffilie = ((HEOutputAnnonceViewBean) annoncesList.iterator().next()).getNumeroAffilie();
            affilie = getAffilie(numeroAffilie);
        }
        if (affilie == null) {
            throw new Exception("Impossible de retrouver l'affilié :" + numeroAffilie);
        }
        _setLangueFromAffilie(affilie);

        // Gestion du modèle et du titre
        setTemplateFile(TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("HERMES_LETTRE_AFFILIE"));
        // Gestion de l'en-tête/pied de page/signature
        _handleHeaders(affilie, numeroAffilie, true);

        // ligne Concerne
        if (annoncesList.size() > 1) {
            loadCatTexte(_getLangue(), HECTConstantes.CS_TYPE_LETTRE_AFFILIE,
                    HECTConstantes.NOM_DOC_LETTRE_AFFILIE_PLUS);
        } else { // single
            loadCatTexte(_getLangue(), HECTConstantes.CS_TYPE_LETTRE_AFFILIE, HECTConstantes.NOM_DOC_LETTRE_AFFILIE_1);
        }
        if (annoncesList != null && annoncesList.size() > 0) {
            // Renseigne les lignes dans le document à partir de la liste triées
            ArrayList liste = new ArrayList();
            String prevRefUnique = "";
            for (Iterator iter = annoncesList.iterator(); iter.hasNext();) {
                HEOutputAnnonceViewBean annonce = (HEOutputAnnonceViewBean) iter.next();
                // Si plusieurs 20 pour la même annonce, prendre le dernier
                // à référence unique égale, prendre le dernier des deux
                String refUnique = annonce.getRefUnique();
                if (prevRefUnique.equals(refUnique)) {
                    liste.remove(liste.size() - 1);
                }
                prevRefUnique = refUnique;
                // Ajoute la ligne dans le document
                HashMap map = new HashMap();
                // map.put(HEParameter.F1, annonce.getNumeroAffilie());
                map.put(HEParameter.F2, annonce.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF));
                // modif NNSS
                // map.put(HEParameter.F3,
                // JAUtil.formatAvs(annonce.getNumeroAVS()));
                map.put(HEParameter.F3, globaz.commons.nss.NSUtil.formatAVSNew(annonce.getNumeroAVS(), annonce
                        .getNumeroAvsNNSS().equals("true")));

                // modif NNSS
                // map.put(HEParameter.F4,
                // DateUtils.convertDate(annonce.getField(HEAnnoncesViewBean.DATE_NAISSANCE_1_JJMMAA),
                // DateUtils.JJMMAA, DateUtils.JJMMAAAA_DOTS));

                // map.put(HEParameter.F5, annonce.getMotif());
                liste.add(map);
            }
            if (liste.size() > 0) {
                setDataSource(liste);
            }
        }
    }

    /**
     * @param affilieNumero
     */
    private TITiersViewBean getAffilie(String affilieNumero) throws Exception {
        TITiersViewBean tiers = null;
        AFAffiliationManager afManager = new AFAffiliationManager();
        afManager.setSession(getSession());
        afManager.setForAffilieNumero(affilieNumero);
        afManager.find(getTransaction());
        for (int i = 0; i < afManager.size(); i++) {
            AFAffiliation af = (AFAffiliation) afManager.getEntity(i);
            tiers = af.getTiers();
        }
        return tiers;
    }

    /**
     * @param annonces
     */
    public ArrayList getAffiliesList(HEOutputAnnonceListViewBean annonces) throws Exception {
        ArrayList affiliesList = new ArrayList();
        if (annonces != null && annonces.size() > 0) {
            // Crée une liste triées des annonces à renseigner dans les
            // documents
            ArrayList annoncesAssuresListeTriee = new ArrayList();
            for (Iterator iter = annonces.iterator(); iter.hasNext();) {
                HEOutputAnnonceViewBean annonce = (HEOutputAnnonceViewBean) iter.next();
                // Pour avoir que les codes application 20
                if (annonce != null) {
                    HEInfos infos = getInfoAssure(annonce.getRefUnique(), HEInfos.CS_NUMERO_AFFILIE);
                    if (infos != null) {
                        annonce.setNumeroAffilie(infos.getLibInfo());
                        annonce.setNumeroAVS(annonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE));
                        annoncesAssuresListeTriee.add(annonce);
                    } else {
                        getMemoryLog().logMessage(
                                "Impossible de charger le numéro d'affilié pour la référence :"
                                        + annonce.getRefUnique(), "", "");
                    }
                }
            }
            Collections.sort((List) annoncesAssuresListeTriee, new HECAComparator());
            // Regroupement par affilié
            affiliesList = new ArrayList();
            ArrayList annoncesList = new ArrayList();
            String prevNumeroAffilie = "";
            for (Iterator iter = annoncesAssuresListeTriee.iterator(); iter.hasNext();) {
                HEOutputAnnonceViewBean annonce = (HEOutputAnnonceViewBean) iter.next();
                if (!annonce.getNumeroAffilie().equals(prevNumeroAffilie)) {
                    annoncesList = new ArrayList();
                    affiliesList.add(annoncesList);
                }
                prevNumeroAffilie = annonce.getNumeroAffilie();
                annoncesList.add(annonce);
            }
        }
        return affiliesList;
    }

    /**
     * @return
     */
    public String getIdLot() {
        return idLot;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hermes.print.itext.HEDocumentManager#getNbLevel()
     */
    @Override
    public int getNbLevel() {
        return HECTConstantes.NB_LEVEL_LETTRE_AFFILIE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hermes.print.itext.HEDocumentManager#getNiveauPourDetail()
     */
    @Override
    public int getNiveauPourDetail() {
        return POSITION_DETAIL;
    }

    private String[] getParams() throws Exception {
        String[] s = new String[2];
        s[0] = affilie == null ? "" : affilie.getFormulePolitesse(null);
        s[1] = affilie == null ? "" : affilie.getFormulePolitesse(null);
        return s;
    }

    /**
     * @return
     */
    public boolean isArchivage() {
        return archivage;
    }

    /**
     * @param b
     */
    public void setArchivage(boolean b) {
        archivage = b;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hermes.print.itext.HEDocumentManager#setFieldToCatTexte(int, java.lang.String)
     */
    @Override
    public void setFieldToCatTexte(int i, String value) throws Exception {
        if (JadeStringUtil.isEmpty(value)) {
            value = " ";
        }
        switch (i) {
            case 1:
                setParametres(CHAMP_TITRE, value);
                break;
            case 2:
                setParametres(CHAMP_POLITESSE, format(value, getParams()));
                break;
            case 3:
                setParametres(CHAMP_CORPS, value);
                break;
            case 5:
                setParametres(CHAMP_CORPS2, format(value, getParams()));
                break;
            case 6:
                setParametres(CHAMP_SIGNATURE, value);
                break;
        }
    }

    /**
     * @param string
     */
    public void setIdLot(String string) {
        idLot = string;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hermes.print.itext.HEDocumentManager#setPositionToCatTexte()
     */
    @Override
    protected void setPositionToCatTexte(String position, String value) {
        switch (Integer.parseInt(position)) {
            case 1:
                setParametres(CHAMP_NOM_PRENOM, value);
                break;
            case 2:
                setParametres(CHAMP_N_ASSURE, value);
                break;
            case 3:
                setParametres(CHAMP_DATE_NAISSANCE, value);
                break;
        }
    }

}
