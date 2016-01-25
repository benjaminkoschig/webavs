package globaz.lyra.process;

import ch.globaz.utils.excel.ExcelJob;

public abstract class LYAbstractExcelEcheanceProcess<T extends LYAbstractExcelGenerator> extends
        LYAbstractEcheanceProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ExcelJob<T> excelJob;

    public LYAbstractExcelEcheanceProcess() {
        super();
    }

    protected abstract ExcelJob<T> getExcelJob();

    @Override
    protected void runProcess() throws Exception {
        this.excelJob = this.getExcelJob();
        if (this.excelJob != null) {
            this.excelJob.run();
        }
    }
}
