package bank;

import bank.pekao.PekaoBankService;
import bank.pkobp.service.BankService;
import bank.pkobp.service.PKOBPBankService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankLoginFacadeTest {

    @InjectMocks
    @Spy
    private BankLoginFacade bankLoginFacade;

    @Nested
    class DetermineBankTypeTest {

        @Test
        void shouldDetermineBankTypePKOBP() {
            final var captor = ArgumentCaptor.forClass(BankService.class);

            bankLoginFacade.determineBankType("PKO BP");

            verify(bankLoginFacade).performLoginAndGetAccountData(captor.capture(), eq("PKO BP"));
            assertThat(captor.getValue()).isInstanceOf(PKOBPBankService.class);
        }

        @Test
        void shouldDetermineBankTypePEKAO() {
            final var captor = ArgumentCaptor.forClass(BankService.class);

            bankLoginFacade.determineBankType("PEKAO");

            verify(bankLoginFacade).performLoginAndGetAccountData(captor.capture(), eq("PEKAO"));
            assertThat(captor.getValue()).isInstanceOf(PekaoBankService.class);
        }

        @Test
        void shouldDetermineBankTypeInvalid() {
            bankLoginFacade.determineBankType("INVALID");

            verify(bankLoginFacade, never()).performLoginAndGetAccountData(any(BankService.class), anyString());
        }

    }

    @Nested
    class PerformLoginAndGetAccountDataTest {

    }

}