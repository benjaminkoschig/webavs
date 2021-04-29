package ch.globaz.common.exceptions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ExceptionsTest {
    private ThrowingConsumer<String, CommonTechnicalException> throwingConsumer;
    private ThrowingRunnable<CommonTechnicalException> throwingRunnable;
    private ThrowingSupplier<String, CommonTechnicalException> throwingSupplier;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.throwingConsumer = Mockito.mock(ThrowingConsumer.class);
        this.throwingRunnable = Mockito.mock(ThrowingRunnable.class);
        this.throwingSupplier = Mockito.mock(ThrowingSupplier.class);
    }

    @Test
    public void checkedToUnChecked_consumerApplicationRemoteCallException_TechnicalException() {
        Mockito.doThrow(CommonTechnicalException.class).when(this.throwingConsumer).accept(Mockito.anyString());
        assertThatThrownBy(() -> Exceptions.checkedToUnChecked(this.throwingConsumer).accept("test"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void checkedToUnCheckedWithMessage_consumerApplicationRemoteCallException_TechnicalException() {
        Mockito.doThrow(CommonTechnicalException.class).when(this.throwingConsumer).accept(Mockito.anyString());
        assertThatThrownBy(() -> Exceptions.checkedToUnChecked(this.throwingConsumer, "Error message").accept("test"))
                .isInstanceOf(RuntimeException.class).hasMessageContaining("Error message");
    }

    @Test
    public void throwingUncheckedExceptioWithMessage_consumerOk_ok() {
        assertThatCode(() -> Exceptions.checkedToUnChecked(this.throwingConsumer, "Error message").accept("test")).doesNotThrowAnyException();
    }

    @Test
    public void checkedToUnChecked_consumerOk_ok() {
        assertThatCode(() -> Exceptions.checkedToUnChecked(this.throwingConsumer).accept("test")).doesNotThrowAnyException();
    }

    @Test
    public void checkedToUnChecked_runnableApplicationRemoteCallException_TechnicalException() {

        Mockito.doThrow(CommonTechnicalException.class).when(this.throwingRunnable).run();
        assertThatThrownBy(() -> Exceptions.checkedToUnChecked(this.throwingRunnable, CommonTechnicalException.class))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void checkedToUnChecked_runnableTestException_TechnicalException() {

        Mockito.doThrow(TestException.class).when(this.throwingRunnable).run();
        assertThatThrownBy(() -> Exceptions.checkedToUnChecked(this.throwingRunnable, CommonTechnicalException.class))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void checkedToUnCheckedWithMessage_runnableTestException_TechnicalException() {

        Mockito.doThrow(TestException.class).when(this.throwingRunnable).run();
        assertThatThrownBy(() -> Exceptions.checkedToUnChecked(this.throwingRunnable, "Message Error"))
                .isInstanceOf(RuntimeException.class).hasMessageContaining("Message Error");
    }

    @Test
    public void checkedToUnChecked_runnableSecondTestException_TechnicalException() {

        Mockito.doThrow(SecondTestException.class).when(this.throwingRunnable).run();
        assertThatThrownBy(() -> Exceptions.checkedToUnChecked(this.throwingRunnable, CommonTechnicalException.class))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void checkedToUnCheckedWithMessage_runnableOk_ok() {
        assertThatCode(() -> Exceptions.checkedToUnChecked(this.throwingRunnable, "Error message"))
                .doesNotThrowAnyException();
    }

    @Test
    public void checkedToUnChecked_runnableOk_ok() {
        assertThatCode(() -> Exceptions.checkedToUnChecked(this.throwingRunnable, CommonTechnicalException.class))
                .doesNotThrowAnyException();
    }

    @Test
    public void checkedToUnChecked_supplierApplicationRemoteCallException_TechnicalException() {

        Mockito.doThrow(CommonTechnicalException.class).when(this.throwingSupplier).get();
        assertThatThrownBy(() -> Exceptions.checkedToUnChecked(this.throwingSupplier, CommonTechnicalException.class))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void checkedToUnChecked_supplierTestException_TechnicalException() {

        Mockito.doThrow(TestException.class).when(this.throwingSupplier).get();
        assertThatThrownBy(() -> Exceptions.checkedToUnChecked(this.throwingSupplier, CommonTechnicalException.class))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void checkedToUnChecked_supplierSecondTestException_TechnicalException() {

        Mockito.doThrow(SecondTestException.class).when(this.throwingSupplier).get();
        assertThatThrownBy(() -> Exceptions.checkedToUnChecked(this.throwingSupplier, CommonTechnicalException.class))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void checkedToUnCheckedWithMessage_supplierTestException_TechnicalException() {

        Mockito.doThrow(TestException.class).when(this.throwingSupplier).get();
        assertThatThrownBy(() -> Exceptions.checkedToUnChecked(this.throwingSupplier, "Error message"))
                .isInstanceOf(RuntimeException.class).hasMessageContaining("Error message");
    }

    @Test
    public void checkedToUnCheckedWithMessge_supplierOk_ok() throws CommonTechnicalException {

        Mockito.when(this.throwingSupplier.get()).thenReturn("test");
        assertThat(Exceptions.checkedToUnChecked(this.throwingSupplier, "Error message")).isNotNull();
    }

    @Test
    public void checkedToUnChecked_supplierOk_ok() throws CommonTechnicalException {

        Mockito.when(this.throwingSupplier.get()).thenReturn("test");
        assertThat(Exceptions.checkedToUnChecked(this.throwingSupplier, CommonTechnicalException.class)).isNotNull();
    }

    @Test
    public void generateException_technicalException_ok() {
        assertThat(Exceptions.generateException(SecondTestException.class, new SecondTestException())).isInstanceOf(CommonTechnicalException.class);
    }

    @Test
    public void generateException_runtimeException_ok() {
        assertThat(Exceptions.generateException(SecondTestException.class, new ThirdTestException())).isInstanceOf(RuntimeException.class);
    }

    private static class TestException extends CommonTechnicalException {

        private static final long serialVersionUID = 0;

        public TestException() {
            super("test");
        }
    }

    private static class SecondTestException extends RuntimeException {

        private static final long serialVersionUID = 0;

        public SecondTestException() {
            super("test");
        }
    }

    private static class ThirdTestException extends RuntimeException {

        private static final long serialVersionUID = 0;

        public ThirdTestException() {
            super("test");
        }
    }
}
