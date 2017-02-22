package ch.globaz.naos.ree.process;

import globaz.globall.db.BSessionUtil;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.naos.ree.REEProcess;
import ch.globaz.naos.ree.tools.ExecutionMode;

public class Process5053Test extends AbstractTestCaseWithContext {

    @Override
    protected String getApplicationName() {
        // TODO Auto-generated method stub
        return super.getApplicationName();
    }

    @Override
    protected String getEnvironnementName() {
        // TODO Auto-generated method stub
        return super.getEnvironnementName();
    }

    @Override
    protected String getUserName() {
        // TODO Auto-generated method stub
        return super.getUserName();
    }

    @Override
    protected String getUserPassword() {
        // TODO Auto-generated method stub
        return super.getUserPassword();
    }

    @Ignore
    @Test
    public void test() {
        REEProcess process = new REEProcess();
        process.setSession(BSessionUtil.getSessionFromThreadContext());
        process.setModeExecution(ExecutionMode._5053_AND_5054.getUserArg());
        process.run();
        System.out.println("Tested");
    }

}
