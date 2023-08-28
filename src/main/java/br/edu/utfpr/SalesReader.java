package br.edu.utfpr;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
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

        return firstCancelledSale.flatMap(firstSale ->
                lastCancelledSale.map(lastSale ->
                        ChronoUnit.DAYS.between(firstSale.getSaleDate(), lastSale.getSaleDate())
                )
        ).orElse(0L);
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


    //    01 Selecione o nome dos 3 piores vendedores, aqueles que venderam menos (em valores) durante todos os anos.
    public Map<String, BigDecimal> bottom3WorstSellers() {
        return sales.stream()
                .filter(sale -> Sale.Status.COMPLETED == sale.getStatus())
                .collect(Collectors.groupingBy(
                        Sale::getSeller,
                        Collectors.reducing(BigDecimal.ZERO, Sale::getValue, BigDecimal::add)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue())  // Ordena em ordem crescente
                .limit(3)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    }


    public List<String> bottom3WorstSellersNumVendasCompleted() {
        Map<String, Long> salesCountBySeller = sales.stream()
                .filter(sale -> Sale.Status.COMPLETED == sale.getStatus())
                .collect(Collectors.groupingBy(
                        Sale::getSeller,
                        Collectors.counting()
                ));

        return salesCountBySeller.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())  // Ordena em ordem crescente
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<String> bottom3WorstSellersNumVendasCompletedCancelled() {
        Map<String, Long> salesCountBySeller = sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getSeller,
                        Collectors.counting()
                ));

        return salesCountBySeller.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())  // Ordena em ordem crescente
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    //    02.	Qual o total de vendas caso os seguintes filtros fossem utilizados: vendas completas, no estado do paraná, nos meses de janeiro, junho e dezembro.
    public BigDecimal totalSalesByStatusAndMonthAndStatePr(Sale.Status status, String state, Month... months) {
        return sales.stream()
                .filter(sale -> status == sale.getStatus() &&
                        containsMonth(sale.getSaleDate().getMonth(), months) &&
                        state.equals(sale.getEstate()))
                .map(Sale::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean containsMonth(Month month, Month... months) {
        return Arrays.stream(months).anyMatch(m -> m == month);
    }

    //03.	Entre 12/05/2012 e 29/07/2012 a vendedora Rosângela Almeida realizou quantas vendas?
    public long countCompletedSalesBySellerInPeriod(String sellerName, LocalDate startDate, LocalDate endDate) {
        return sales.stream()
                .filter(sale -> sellerName.equals(sale.getSeller()) &&
                        sale.getSaleDate().isAfter(startDate.minusDays(1)) &&  // Inclui a data inicial
                        sale.getSaleDate().isBefore(endDate.plusDays(2)))  // Inclui a data final
                .count();
    }

    //04.	Em qual ano tivemos R$ 3.689.379.28 em vendas?
    public Map<Integer, BigDecimal> totalSalesByYear() {
        return sales.stream()
                .filter(sale -> Sale.Status.COMPLETED == sale.getStatus())
                .collect(Collectors.groupingBy(
                        sale -> sale.getSaleDate().getYear(),
                        Collectors.mapping(Sale::getValue, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }

//    05.	Na equipe de qual gerente houveram mais cancelamentos de vendas entre 2013 e 2014?
    public String managerWithMostCancelledSalesBetween2013And2014() {
        Map<String, Long> cancelledSalesByManager = sales.stream()
                .filter(sale -> Sale.Status.CANCELLED == sale.getStatus() &&
                        sale.getSaleDate().getYear() >= 2013 && sale.getSaleDate().getYear() <= 2014)
                .collect(Collectors.groupingBy(
                        Sale::getManager,
                        Collectors.counting()
                ));

        return cancelledSalesByManager.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Nenhum gerente encontrado");
    }

}
