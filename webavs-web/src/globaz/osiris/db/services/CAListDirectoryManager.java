package globaz.osiris.db.services;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.BISession;
import globaz.jade.common.Jade;
import globaz.osiris.application.CAApplication;
import java.io.File;
import java.io.Serializable;
import java.util.Vector;

public class CAListDirectoryManager implements Serializable, FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Vector _container = new Vector();
    private String _directory = "/work/";
    private FWServlet _servlet;
    private BISession session;

    public boolean canDoNext() {
        return false;
    }

    public boolean canDoPrev() {
        return false;
    }

    /**
     * Vide le container
     */
    public void clear() {
        _container.clear();
    }

    /**
     * Charge le container
     * 
     * @exception java.lang.Exception
     *                si le chargement a échoué
     */
    public void find() throws Exception {
        try {
            File realPath = new File(getFullPath());
            File[] files = realPath.listFiles();
            int x = 0;
            while (x < files.length) {
                CADirectory bean = new CADirectory(files[x]);
                _container.add(bean);
                x++;
            }
        } catch (SecurityException ex) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(ex.getMessage());
        } finally {
        }
    }

    public int getCount() {
        return 1;
    }

    /**
     * Retourne le répertoire où se trouve la liste des fichier
     * 
     * @return java.lang.String le repertoire parent.
     */
    public String getDirectory() {
        return _directory;
    }

    public CADirectory getFile(int idx) {
        return (CADirectory) _container.get(idx);
    }

    public String getFullPath() {
        return Jade.getInstance().getHomeDir() + "/" + CAApplication.DEFAULT_OSIRIS_ROOT + _directory;
    }

    /**
     * Renvoie la session en cours
     * 
     * @return la session en cours
     */
    @Override
    public BISession getISession() {
        return session;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    @Override
    public String getMessage() {
        return "";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    @Override
    public String getMsgType() {
        return FWViewBeanInterface.OK;
    }

    public int getOffset() {
        return -1;
    }

    /**
     * Returns the _servlet.
     * 
     * @return FWServlet
     */
    public FWServlet getServlet() {
        return _servlet;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.08.2002 11:39:58)
     * 
     * @return int
     */
    @Override
    public int getSize() {
        return _container.size();
    }

    /**
     * Détermine si le bean a le droit de lecture (READ)
     * 
     * @return true si le bean a le droit de lecture (READ), false sinon
     */
    public boolean hasRightRead() {
        return true;
    }

    /**
     * Indique si le container est vide
     * 
     * @return true si le container est vide, false sinon
     */
    public boolean isEmpty() {
        return _container.isEmpty();
    }

    public void setDirectory(String newDirectory) {
        _directory = newDirectory;
    }

    /**
     * Modifie la session en cours
     * 
     * @param newISession
     *            la nouvelle session
     */
    @Override
    public void setISession(BISession newSession) {
        session = newSession;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2002 09:33:53)
     * 
     * @param message
     *            java.lang.String
     * @deprecated
     */
    @Override
    @Deprecated
    public void setMessage(String message) {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2002 09:33:53)
     * 
     * @param message
     *            java.lang.String
     * @deprecated
     */
    @Override
    @Deprecated
    public void setMsgType(String msgType) {
    }

    /**
     * Sets the _servlet.
     * 
     * @param _servlet
     *            The _servlet to set
     */
    public void setServlet(FWServlet servlet) {
        _servlet = servlet;
    }

    /**
     * Renvoie le nombre d'entités contenues dans le container
     * 
     * @return le nombre d'entités contenues dans le container
     */
    public int size() {
        return _container.size();
    }
}