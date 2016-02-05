package ch.globaz.common.codesystem;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.jade.business.services.codesysteme.JadeCodeSystemeService;

public class CodeSystemeResolver {

    private Map<Integer, JadeCodeSysteme> codeSystemesMap = new HashMap<Integer, JadeCodeSysteme>();
    private Langues langue;
    private JadeCodeSystemeService service;
    private boolean isContextextInitlisied = false;
    private BSession session;

    public CodeSystemeResolver(Langues langue) {
        this.langue = langue;
    }

    public CodeSystemeResolver(Langues langue, BSession session) {
        this.langue = langue;
        this.session = session;

    }

    public CodeSystemeResolver(Langues langue, JadeCodeSystemeService service) {
        this.langue = langue;
        this.service = service;
    }

    public CodeSystemeResolver(String langueIso) {
        langue = Langues.getLangueDepuisCodeIso(langueIso);
    }

    public String resovleTraduction(Integer codeSysteme) {
        JadeCodeSysteme code = codeSystemesMap.get(codeSysteme);
        if (code == null) {
            return null;
        }
        return code.getTraduction(langue);
    }

    public void addAll(List<JadeCodeSysteme> codeSystemes) {
        for (JadeCodeSysteme jadeCodeSysteme : codeSystemes) {
            add(jadeCodeSysteme);
        }
    }

    public void addAllAndSerach(String... familleCode) {
        List<String> familles = Arrays.asList(familleCode);
        initContext();
        for (String famille : familles) {
            try {
                if (service == null) {
                    try {
                        service = JadeBusinessServiceLocator.getCodeSystemeService();
                    } catch (JadeApplicationServiceNotAvailableException e) {
                        throw new RuntimeException(e);
                    }
                }
                addAll(service.getFamilleCodeSysteme(famille));
            } catch (JadePersistenceException e) {
                throw new RuntimeException(e);
            } finally {
                if (isContextextInitlisied) {
                    stopContext();
                }
            }
        }
    }

    public void add(JadeCodeSysteme jadeCodeSysteme) {
        codeSystemesMap.put(Integer.valueOf(jadeCodeSysteme.getIdCodeSysteme()), jadeCodeSysteme);
    }

    private void stopContext() {
        if (session != null && service != null) {
            BSessionUtil.stopUsingContext(this);
        }
    }

    private void initContext() {
        if (session != null && service != null) {
            isContextextInitlisied = true;
            try {
                BSessionUtil.initContext(session, this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
