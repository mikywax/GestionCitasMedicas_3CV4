package com.citasmedicas.functional_logic;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class RefundPolicyTest {

    private final BigDecimal pagoInicial = new BigDecimal("1000.00");
    private final LocalDate fechaCita = LocalDate.of(2026, 10, 10);

    @Test
    void testP01_CancelacionMayorA3Dias() {
        LocalDate fechaCancelacion = LocalDate.of(2026, 10, 1);
        BigDecimal esperado = new BigDecimal("1000.00");
        BigDecimal obtenido = RefundPolicy.processRefund.apply(fechaCancelacion, fechaCita, pagoInicial);
        assertEquals(0, esperado.compareTo(obtenido));
    }

    @Test
    void testP02_CancelacionExacta3Dias() {
        LocalDate fechaCancelacion = LocalDate.of(2026, 10, 7);
        BigDecimal esperado = new BigDecimal("1000.00");
        BigDecimal obtenido = RefundPolicy.processRefund.apply(fechaCancelacion, fechaCita, pagoInicial);
        assertEquals(0, esperado.compareTo(obtenido));
    }

    @Test
    void testP03_CancelacionExacta2Dias() {
        LocalDate fechaCancelacion = LocalDate.of(2026, 10, 8);
        BigDecimal esperado = new BigDecimal("500.00"); // 50%
        BigDecimal obtenido = RefundPolicy.processRefund.apply(fechaCancelacion, fechaCita, pagoInicial);
        assertEquals(0, esperado.compareTo(obtenido));
    }

    @Test
    void testP04_CancelacionCon1Dia() {
        LocalDate fechaCancelacion = LocalDate.of(2026, 10, 9);
        BigDecimal esperado = BigDecimal.ZERO; // 0%
        BigDecimal obtenido = RefundPolicy.processRefund.apply(fechaCancelacion, fechaCita, pagoInicial);
        assertEquals(0, esperado.compareTo(obtenido));
    }

    @Test
    void testP05_CancelacionMismoDia() {
        LocalDate fechaCancelacion = LocalDate.of(2026, 10, 10);
        BigDecimal esperado = BigDecimal.ZERO; // 0%
        BigDecimal obtenido = RefundPolicy.processRefund.apply(fechaCancelacion, fechaCita, pagoInicial);
        assertEquals(0, esperado.compareTo(obtenido));
    }

    @Test
    void testP06_CancelacionTardiaPostCita() {
        LocalDate fechaCancelacion = LocalDate.of(2026, 10, 11);
        BigDecimal esperado = BigDecimal.ZERO; // Días negativos, 0%
        BigDecimal obtenido = RefundPolicy.processRefund.apply(fechaCancelacion, fechaCita, pagoInicial);
        assertEquals(0, esperado.compareTo(obtenido));
    }

    @Test
    void testP07_MontoPagoCero() {
        LocalDate fechaCancelacion = LocalDate.of(2026, 10, 1);
        BigDecimal pagoCero = BigDecimal.ZERO;
        BigDecimal esperado = BigDecimal.ZERO;
        BigDecimal obtenido = RefundPolicy.processRefund.apply(fechaCancelacion, fechaCita, pagoCero);
        assertEquals(0, esperado.compareTo(obtenido));
    }

    @Test
    void testP08_CancelacionMesesPrevios() {
        LocalDate fechaCancelacion = LocalDate.of(2026, 8, 10);
        BigDecimal pago = new BigDecimal("500.00");
        BigDecimal esperado = new BigDecimal("500.00"); // 100%
        BigDecimal obtenido = RefundPolicy.processRefund.apply(fechaCancelacion, fechaCita, pago);
        assertEquals(0, esperado.compareTo(obtenido));
    }

    @Test
    void testP09_CancelacionDatosVacios() {
        assertThrows(NullPointerException.class, () -> {
            RefundPolicy.processRefund.apply(null, fechaCita, pagoInicial);
        });
    }

    @Test
    void testP10_MontoInvalidoNegativo() {
        LocalDate fechaCancelacion = LocalDate.of(2026, 10, 1);
        BigDecimal pagoNegativo = new BigDecimal("-500.00");
        BigDecimal esperado = new BigDecimal("-500.00");
        BigDecimal obtenido = RefundPolicy.processRefund.apply(fechaCancelacion, fechaCita, pagoNegativo);
        assertEquals(0, esperado.compareTo(obtenido));
    }
}