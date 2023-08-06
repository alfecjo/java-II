package br.edu.utfpr;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class SalesReader {

    private final List<Sale> sales;

    public SalesReader(String salesFile) {

        final var dataStream = ClassLoader.getSystemResourceAsStream(salesFile);

        if (dataStream == null) {
            throw new IllegalStateException("File not found or is empty");
        }

        final var builder = new CsvToBeanBuilder<Sale>(new InputStreamReader(dataStream, StandardCharsets.UTF_8));

        sales = builder
                .withType(Sale.class)
                .withSeparator(';')
                .build()
                .parse();
    }
    public BigDecimal totalOfCompletedSales() {
        return sales.stream()
                .filter(sale -> Sale.Status.COMPLETED == sale.getStatus())
                .map(Sale::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public BigDecimal totalOfCancelledSales() {
        return sales.stream()
                .filter(sale -> Sale.Status.CANCELLED == sale.getStatus())
                .map(Sale::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public Optional<Sale> mostRecentCompletedSale() {
        return sales.stream()
                .filter(sale -> Sale.Status.COMPLETED == sale.getStatus())
                .max(Comparator.comparing(Sale::getSaleDate));
    }
    public long daysBetweenFirstAndLastCancelledSale() {
        Optional<Sale> firstCancelledSale = sales.stream()
                .filter(sale -> Sale.Status.CANCELLED == sale.getStatus())
                .min(Comparator.comparing(Sale::getSaleDate));

        Optional<Sale> lastCancelledSale = sales.stream()
                .filter(sale -> Sale.Status.CANCELLED == sale.getStatus())
                .max(Comparator.comparing(Sale::getSaleDate));

        if (firstCancelledSale.isPresent() && lastCancelledSale.isPresent()) {
            Sale firstSale = firstCancelledSale.get();
            Sale lastSale = lastCancelledSale.get();
            return ChronoUnit.DAYS.between(firstSale.getSaleDate(), lastSale.getSaleDate());
        } else {
            // Caso não haja vendas canceladas, retornamos 0 ou outro valor que faça sentido para o contexto.
            return 0;
        }
    }
    public BigDecimal totalCompletedSalesBySeller(String sellerName) {
        return sales.stream()
                .filter(sale -> Sale.Status.COMPLETED == sale.getStatus() && sellerName.equals(sale.getSeller()))
                .map(Sale::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public long countAllSalesByManager(String managerName) {
        return sales.stream()
                .filter(sale -> managerName.equals(sale.getManager()))
                .count();
    }
    public BigDecimal totalSalesByStatusAndMonth(Sale.Status status, Month... months) {
        return sales.stream()
                .filter(sale -> status == sale.getStatus() && containsMonth(sale.getSaleDate().getMonth(), months))
                .map(Sale::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    private boolean containsMonth(Month month, Month[] months) {
        for (Month m : months) {
            if (month == m) {
                return true;
            }
        }
        return false;
    }
    public Map<String, Long> countCompletedSalesByDepartment() {
        return sales.stream()
                .filter(sale -> Sale.Status.COMPLETED == sale.getStatus())
                .collect(Collectors.groupingBy(Sale::getDepartment, Collectors.counting()));
    }
    public Map<Integer, Map<String, Long>> countCompletedSalesByPaymentMethodAndGroupingByYear() {
        return sales.stream()
                .filter(sale -> Sale.Status.COMPLETED == sale.getStatus())
                .collect(Collectors.groupingBy(
                        sale -> sale.getSaleDate().getYear(),
                        Collectors.groupingBy(Sale::getPaymentMethod, Collectors.counting())
                ));
    }
    public Map<String, BigDecimal> top3BestSellers() {
        return sales.stream()
                .filter(sale -> Sale.Status.COMPLETED == sale.getStatus())
                .collect(Collectors.groupingBy(
                        Sale::getSeller,
                        Collectors.reducing(BigDecimal.ZERO, Sale::getValue, BigDecimal::add)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    }
}
