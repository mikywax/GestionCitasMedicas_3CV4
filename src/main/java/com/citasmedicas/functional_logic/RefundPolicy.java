package com.citasmedicas.functional_logic;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.function.Function;

public class RefundPolicy {

    // Función pura 1: Calcula los días de diferencia
    public static final BiFunction<LocalDate, LocalDate, Long> calculateDaysDifference = 
        (cancelDate, apptDate) -> ChronoUnit.DAYS.between(cancelDate, apptDate);

    // Función pura 2: Define el factor de reembolso según las políticas
    public static final Function<Long, BigDecimal> getRefundFactor = 
        (days) -> {
            if (days >= 3) return BigDecimal.ONE;               // 100%
            if (days == 2) return new BigDecimal("0.5");        // 50%
            return BigDecimal.ZERO;                             // 0%
        };

    // Función pura 3: Aplica el factor al costo total
    public static final BiFunction<Long, BigDecimal, BigDecimal> calculateAmount = 
        (days, totalPaid) -> getRefundFactor.apply(days).multiply(totalPaid);

    // Función principal (Composición)
    public static final ReembolsoRequest processRefund = 
        (cancelDate, apptDate, totalPaid) -> {
            Long days = calculateDaysDifference.apply(cancelDate, apptDate);
            return calculateAmount.apply(days, totalPaid);
        };

    // Interfaz funcional para la composición final
    @FunctionalInterface
    public interface ReembolsoRequest {
        BigDecimal apply(LocalDate cancelDate, LocalDate apptDate, BigDecimal totalPaid);
    }
}