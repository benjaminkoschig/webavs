package globaz.hermes.print.itext;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import globaz.hermes.print.itext.util.HEExtraitComparator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class HEExtraitAnnonceAssureBean implements Serializable, Cloneable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Hashtable m_container = new Hashtable();
    String m_date = JACalendar.todayJJsMMsAAAA();
    // String m_caisseTexte = "";
    String m_dateNaissance = "";
    String m_etatOrigine = "";
    String m_idAnnonce11 = "";
    boolean m_isExtraitComplementaire = false;
    String m_motif = "";
    String m_nomPrenom = "";
    String m_numeroAnnonce = "";
    String m_numeroAvs = "";
    String m_referenceInterne = "";
    String m_utilisateur = "";
    // Pour Extrait avec motif 71,81,78,85
    // private Hashtable m_motifContainer = new Hashtable();
    private String numeroCaisseCI;
    private String referenceUnique;

    public HEExtraitAnnonceAssureBean() {
    }

    public HEExtraitAnnonceAssureBean(HEAnnoncesViewBean entity) throws Exception {
        setHeader(entity);
    }

    public void clearExtrait() {
        m_container.clear();
    }

    // private void testArray(ArrayList list) {
    // Iterator it = list.iterator();
    // HEExtraitAnnonceBean bean = null;
    // while (it.hasNext()) {
    // bean = (HEExtraitAnnonceBean) it.next();
    // System.out.println(bean.getNumeroCaisse() + " " + bean.getAnneeCotti() +
    // " " + bean.getKey());
    // }
    // }
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Collection getCollection() {
        ArrayList list = new ArrayList();
        list.addAll(m_container.values());
        // System.out.println("************************* BEFORE *****************");
        // testArray(list);
        Collections.sort((List) list, (new HEExtraitComparator()));
        // System.out.println("************************* AFTER *****************");
        // testArray(list);
        return list;
    }

    public HEExtraitAnnonceBean getExtrait(String periode) {
        return (HEExtraitAnnonceBean) m_container.get(periode);
    }

    public String getIdAnnonce11() {
        return m_idAnnonce11;
    }

    /**
     * Returns the motif.
     * 
     * @return String
     */
    public String getMotif() {
        return m_motif;
    }

    public String getNomPrenom() {
        return m_nomPrenom;
    }

    /**
     * Returns the numeroAnnonce.
     * 
     * @return String
     */
    public String getNumeroAnnonce() {
        return m_numeroAnnonce;
    }

    public String getNumeroAvs() {
        return m_numeroAvs;
    }

    /**
     * Method getNumeroCaisseCI.
     * 
     * @return String
     */
    public String getNumeroCaisseCI() {
        return numeroCaisseCI;
    }

    /**
     * Returns the referenceInterne.
     * 
     * @return String
     */
    public String getReferenceInterne() {
        return m_referenceInterne;
    }

    /**
     * @return
     */
    public String getReferenceUnique() {
        return referenceUnique;
    }

    public String getUtilisateur() {
        return m_utilisateur;
    }

    /**
     * @return
     */
    public boolean isExtraitComplementaire() {
        return m_isExtraitComplementaire;
    }

    /**
     * Sets the lieuDate.
     * 
     * @param lieuDate
     *            The lieuDate to set
     */
    public void setdate(String lieuDate) {
        m_date = lieuDate;
    }

    // /**
    // * Sets the caisseTexte.
    // * @param caisseTexte The caisseTexte to set
    // */
    // public void setCaisseTexte(String caisseTexte) {
    // String idTxtCaisse = caisseTexte;
    // if (idTxtCaisse == null || "null".equals(idTxtCaisse)) {
    // idTxtCaisse = "n/a";
    // }
    // this.m_caisseTexte =
    // FWMessageFormat.format("Eidgenössische Ausgleichskasse {0}\nCaisse fédérale de compensation {0}\nCassa federale di compensazione {0}",
    // idTxtCaisse);
    // }
    /**
     * Sets the dateNaissance.
     * 
     * @param dateNaissance
     *            The dateNaissance to set
     */
    public void setDateNaissance(String dateNaissance) {
        m_dateNaissance = JACalendar.format(dateNaissance, JACalendar.FORMAT_DDsMMsYYYY);
        // this.m_dateNaissance = dateNaissance;
    }

    /**
     * Sets the etatOrigine.
     * 
     * @param etatOrigine
     *            The etatOrigine to set
     */
    public void setEtatOrigine(String etatOrigine) {
        m_etatOrigine = etatOrigine;
    }

    public void setExtrait(HEExtraitAnnonceBean bean) {
        m_container.put(bean.getKey(), bean);
    }

    /**
     * @param b
     */
    public void setExtraitComplementaire(boolean b) {
        m_isExtraitComplementaire = b;
    }

    public void setHeader(HEAnnoncesViewBean entity) throws Exception {
        setNomPrenom(entity.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF));
        setEtatOrigine(entity.getField(IHEAnnoncesViewBean.ETAT_ORIGINE));
        setNumeroAvs(entity.getField(IHEAnnoncesViewBean.NUMERO_ASSURE), entity.getNumeroAvsNNSS());
        setNumeroAnnonce(entity.getField(IHEAnnoncesViewBean.NUMERO_ANNONCE));
        try {
            int anneeCourante = (JACalendar.today().getYear() % 100);
            JADate date = new JADate(entity.getField(IHEAnnoncesViewBean.DATE_NAISSANCE_1_JJMMAA), anneeCourante);
            setDateNaissance(JACalendar.format(date, JACalendar.FORMAT_DDsMMsYYYY));
        } catch (Exception e) {
        }
    }

    public void setIdAnnonce11(String idAnnonce) {
        m_idAnnonce11 = idAnnonce;
    }

    /**
     * Sets the motif.
     * 
     * @param motif
     *            The motif to set
     */
    public void setMotif(String motif) {
        m_motif = motif;
    }

    /**
     * Sets the nomPrenom.
     * 
     * @param nomPrenom
     *            The nomPrenom to set
     */
    public void setNomPrenom(String nomPrenom) {
        if (m_nomPrenom.trim().length() == 0) {
            m_nomPrenom = nomPrenom;
        }
    }

    /**
     * Sets the numeroAnnonce.
     * 
     * @param numeroAnnonce
     *            The numeroAnnonce to set
     */
    public void setNumeroAnnonce(String numeroAnnonce) {
        if (!JAUtil.isStringEmpty(numeroAnnonce)) {
            m_numeroAnnonce = numeroAnnonce;
        }
    }

    /**
     * Sets the numeroAvs.
     * 
     * @param numeroAvs
     *            The numeroAvs to set
     */
    public void setNumeroAvs(String numeroAvs, String IsNNSS) {

        // Modif NNSS
        // this.m_numeroAvs = AVSUtils.formatAVS8Or9(numeroAvs);
        m_numeroAvs = globaz.commons.nss.NSUtil.formatAVSNew(numeroAvs, IsNNSS.equals("true"));

        // // Si le numéro AVS est un numéro à moins de 10 chiffres on enlève
        // les 0 à la fin
        // if (numeroAvs.trim().length() < 10) {
        // this.m_numeroAvs =
        // JAUtil.formatAvs(CIUtil.removeZeroFromAvs(numeroAvs.trim()));
        // }
        // ALD : suppression car impossible avec NNSS !
        // try {
        // this.setDateNaissance(AVSUtils.getBirthDateFromAVS(numeroAvs));
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }

    /**
     * Method setNumeroCaisseCI.
     * 
     * @param string
     */
    public void setNumeroCaisseCI(String num) {
        numeroCaisseCI = num;
    }

    /**
     * Sets the referenceInterne.
     * 
     * @param referenceInterne
     *            The referenceInterne to set
     */
    public void setReferenceInterne(String referenceInterne) {
        m_referenceInterne = referenceInterne;
    }

    /**
     * @param string
     */
    public void setReferenceUnique(String string) {
        referenceUnique = string;
    }

    public void setUtilisateur(String utilisateur) {
        m_utilisateur = utilisateur;
    }

    public int size() {
        return m_container.size();
    }

}