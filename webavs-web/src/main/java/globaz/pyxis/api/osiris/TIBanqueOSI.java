package globaz.pyxis.api.osiris;

import globaz.osiris.external.IntBanque;
import globaz.pyxis.db.tiers.TIBanqueViewBean;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (12.02.2003 15:01:10)
 * 
 * @author: Administrator
 */
public class TIBanqueOSI implements IntBanque {
    private TIBanqueViewBean banque = new TIBanqueViewBean();

    /**
     * Commentaire relatif au constructeur TIBanqueOSI.
     */
    public TIBanqueOSI() {
        super();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.11.2001 14:55:58)
     * 
     * @return java.lang.String
     */
    @Override
    public String getClearing() {
        return banque.getClearing();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.11.2001 14:56:58)
     * 
     * @return java.lang.String
     */
    @Override
    public String getCodeSwift() {
        return banque.getCodeSwift();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntBanque#getCodeSwiftWithoutSpaces()
     */
    @Override
    public String getCodeSwiftWithoutSpaces() {
        String codeSwiftToFormat = getCodeSwift();

        StringBuffer buffer = new StringBuffer();
        int index = -1;
        while ((index = codeSwiftToFormat.indexOf(" ")) != -1) {
            buffer.append(codeSwiftToFormat.substring(0, index));
            codeSwiftToFormat = codeSwiftToFormat.substring(index + " ".length());
        }
        buffer.append(codeSwiftToFormat);
        return buffer.toString();
    }

    /**
     * Renvoie l'id unique de l'entit�
     * 
     * @return l'id unique de l'entit�
     */
    @Override
    public String getId() {
        return banque.getId();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.11.2001 14:55:40)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdBanque() {
        return banque.getIdTiersBanque();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.11.2001 07:40:04)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdTiers() {
        return banque.getIdTiers();
    }

    /**
     * Renvoie la session en cours
     * 
     * @return la session en cours
     */
    @Override
    public globaz.globall.api.BISession getISession() {
        return banque.getISession();
    }

    /**
     * Renvoie la date de derni�re modification de l'objet (format DD.MM.YYYY).
     * 
     * @return la date de derni�re modification de l'objet, null si pas disponible
     */
    @Override
    public final String getLastModifiedDate() {
        if (banque.getSpy() == null) {
            return null;
        }
        return banque.getSpy().getDate();
    }

    /**
     * Renvoie l'heure de derni�re modification de l'objet (format HH:MM:SS).
     * 
     * @return l'heure de derni�re modification de l'objet, null si pas disponible
     */
    @Override
    public final String getLastModifiedTime() {
        if (banque.getSpy() == null) {
            return null;
        }
        return banque.getSpy().getTime();
    }

    /**
     * Renvoie l'id du dernier utilisateur qui a modifi� l'objet.
     * 
     * @return l'id du dernier utilisateur qui a modifi� l'objet, null si pas disponible
     */
    @Override
    public final String getLastModifiedUser() {
        if (banque.getSpy() == null) {
            return null;
        }
        return banque.getSpy().getUser();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.11.2001 14:56:19)
     * 
     * @return java.lang.String
     */
    @Override
    public String getNumCCP() {
        return banque.getNumCcpBanque();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.11.2001 16:05:23)
     * 
     * @return globaz.interfaceext.tiers.IntTiers
     */
    @Override
    public globaz.osiris.external.IntTiers getTiers() {
        try {
            TITiersOSI tiers = new TITiersOSI();
            tiers.setISession(getISession());
            tiers.retrieve(getIdTiers());
            return tiers;
        } catch (Exception e) {
            System.out.println("TIBanqueOSI.getTiers(): Exception raised: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Indique si l'entit� est nouvelle (i.e. n'existe pas dans la BD)
     * 
     * @return true si l'entit� n'existe pas dans la BD; false sinon
     */
    @Override
    public boolean isNew() {
        return banque.isNew();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.11.2001 15:08:22)
     */
    @Override
    public void retrieve(globaz.globall.api.BITransaction transaction, String idBanque) throws Exception {
        banque.setIdTiersBanque(idBanque);
        banque.retrieve(transaction);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.11.2001 15:08:22)
     */
    @Override
    public void retrieve(String idBanque) throws Exception {
        retrieve(null, idBanque);
    }

    /**
     * Modifie la session en cours
     * 
     * @param newISession
     *            la nouvelle session
     */
    @Override
    public void setISession(globaz.globall.api.BISession newSession) {
        banque.setISession(newSession);
    }
}
