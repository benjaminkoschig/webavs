package globaz.osiris.print.itext.list;

import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JANumberFormatter;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.utils.CAAdressePaiementFormatter;
import java.math.BigDecimal;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CAIListOrdreGroupeRecapBean {

    private boolean empty = true;
    private CAAdressePaiementFormatter fmtAdPmt = null;
    private String m_genreVirement = new String();
    private BigDecimal m_montantGroupe = new BigDecimal(0);
    private int m_nombreCas = 0;

    public CAIListOrdreGroupeRecapBean() {
    }

    public CAIListOrdreGroupeRecapBean(CAOperation entity) {
        add(entity);
    }

    /**
     * Cette méthode formate l'adresse de paiement
     */
    private void _formaterAdressePaiement(CAOperation entity) {
        // Récupérer l'ordre de versement
        // CAOperationOrdreVersement oper = (CAOperationOrdreVersement) entity;

        // Formatter l'adresse de paiement
        try {
            fmtAdPmt = new CAAdressePaiementFormatter(entity.getAdressePaiement());
        } catch (Exception e) {
            fmtAdPmt = new CAAdressePaiementFormatter(null);
        }

    }

    /**
     * Cette méthode récupère le genre de virement
     * 
     * @return java.lang.String
     */
    private String _getGenreVirement(CAOperation entity) {
        try {
            if (entity.getAdressePaiement() != null) {
                _formaterAdressePaiement(entity);

                FWParametersUserCode uc = new FWParametersUserCode();
                uc.setSession(entity.getSession());
                uc.setIdCodeSysteme("21400" + fmtAdPmt.getTypeAdresse());
                uc.setIdLangue(entity.getSession().getIdLangue());
                uc.retrieve();
                return uc.getLibelle();
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Cette méthode permet d'ajouter des valeurs dans le bean
     */
    public void add(CAOperation entity) {
        try {
            setGenreVirement(_getGenreVirement(entity));
            setNombreCas(1);
            setMontantGroupe(new BigDecimal(JANumberFormatter.deQuote(entity.getMontant())));
            setEmpty(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retourne le genre de virement.
     * 
     * @return String
     */
    public String getCOL_10() {
        return getGenreVirement();
    }

    /**
     * Returns the m_nombreCas.
     * 
     * @return Integer
     */
    public Integer getCOL_11() {
        return getNombreCas();
    }

    /**
     * Returns the m_montantGroupe.
     * 
     * @return BigDedimal
     */
    public BigDecimal getCOL_12() {
        return getMontantGroupe();
    }

    /**
     * Retourne le genre de virement (m_genreVirement).
     * 
     * @return String
     */
    protected String getGenreVirement() {
        return m_genreVirement;
    }

    /**
     * Returns the m_montantGroupe.
     * 
     * @return BigDecimal
     */
    protected BigDecimal getMontantGroupe() {
        return m_montantGroupe;
    }

    /**
     * Returns the m_nombreCas.
     * 
     * @return Integer
     */
    protected Integer getNombreCas() {
        return new Integer(m_nombreCas);
    }

    /**
     * Returns the empty.
     * 
     * @return boolean
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * Sets the empty.
     * 
     * @param empty
     *            The empty to set
     */
    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    /**
     * Sets the genreVirement.
     * 
     * @param genreVirement
     *            The genreVirement to set
     */
    protected void setGenreVirement(String genreVirement) {
        m_genreVirement = genreVirement;
        setEmpty(false);
    }

    /**
     * Sets the m_montantGroupe.
     * 
     * @param m_montantGroupe
     *            The m_montantGroupe to set
     */
    protected void setMontantGroupe(BigDecimal montantGroupe) {
        if (m_montantGroupe.equals(new BigDecimal(0))) {
            m_montantGroupe = montantGroupe;
        } else {
            m_montantGroupe = m_montantGroupe.add(montantGroupe);
        }
        setEmpty(false);

    }

    /**
     * Sets the m_nombreCas.
     * 
     * @param m_nombreCas
     *            The m_nombreCas to set
     */
    protected void setNombreCas(int nombreCas) {
        if (m_nombreCas == 0) {
            m_nombreCas = nombreCas;
        } else {
            m_nombreCas = m_nombreCas + nombreCas;
        }
        setEmpty(false);
    }

}