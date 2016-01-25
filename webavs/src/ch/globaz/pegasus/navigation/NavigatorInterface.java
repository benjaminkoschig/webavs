package ch.globaz.pegasus.navigation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.List;
import java.util.Map;

public interface NavigatorInterface {
    public void computParmetresLink() throws JadeApplicationException, JadePersistenceException;

    // public String getKey();

    public Map<String, String> getIdsMap();

    public String getLibelle();

    public String getLink();

    public List<NavigatorInterface> getNavigatorLinks();

    public Map<String, String> getParameters();

    public void setIdsMap(Map<String, String> ids);

}
