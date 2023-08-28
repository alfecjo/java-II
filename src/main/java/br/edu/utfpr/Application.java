package br.edu.utfpr;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;

public class Application {

    public static final String SELLER = "Adriana Gomes";
    public static final String MANAGER = "Elenice Mendes";

    public Application() {
        startReading();
    }

    private void startReading() {

        final SalesReader reader = new SalesReader("dados.csv");

//        final var totalOfCompletedSales = reader.totalOfCompletedSales();
//        System.out.printf("O valor total de vendas completadas foi de %s%n", toCurrency(totalOfCompletedSales));
//
//        final var totalOfCancelledSales = reader.totalOfCancelledSales();
//        System.out.printf("O valor total de vendas canceladas foi de %s%n", toCurrency(totalOfCancelledSales));
//
//        reader.mostRecentCompletedSale()
//                .ifPresent(sale -> System.out.printf("Venda mais recente foi realizada em %s%n", sale.getSaleDate()));
//
//        final var daysBetweenFirstAndLastSale = reader.daysBetweenFirstAndLastCancelledSale();
//        System.out.printf("Se passaram %s dias entre a primeira e a ultima venda cancelada%n", daysBetweenFirstAndLastSale);
//
//        final var totalSalesBySeller = reader.totalCompletedSalesBySeller(SELLER);
//        System.out.printf("A vendedora %s totalizou %s em vendas%n", SELLER, toCurrency(totalSalesBySeller));
//
//        final var countOfSalesByManager = reader.countAllSalesByManager(MANAGER);
//        System.out.printf("A equipe do gerente %s realizou %s vendas%n", MANAGER, countOfSalesByManager);
//
//        final var totalSalesByStatusAndMonth = reader.totalSalesByStatusAndMonth(Sale.Status.COMPLETED, Month.JULY, Month.SEPTEMBER);
//        System.out.printf("As venda com o status no mes indicado somaram %s%n", toCurrency(totalSalesByStatusAndMonth));
//
//        System.out.println("-------------------");
//        System.out.println("Contagem de vendas por departamento\n");
//
//        final var salesCountByDepartment = reader.countCompletedSalesByDepartment();
//        salesCountByDepartment.forEach((key, value) -> System.out.printf("Departamento %s teve %s vendas", key, value).println());
//
//        System.out.println("-------------------");
//        System.out.println("Contagem de vendas por meio de pagamento agrupadas por ano\n");
//
//        final var salesCountByPaymentMethodByYear = reader.countCompletedSalesByPaymentMethodAndGroupingByYear();
//        salesCountByPaymentMethodByYear.keySet()
//                .forEach(year -> {
//                    System.out.println("> No ano de " + year);
//                    salesCountByPaymentMethodByYear.get(year)
//                            .forEach((key, value) -> System.out.printf("Meio de pagamento %s teve %s vendas", key, value).println());
//                });
//
//        System.out.println("-------------------");
//        System.out.println("Top 3 de vendedores\n");
//
//        final var topThreeSellers = reader.top3BestSellers();
//        topThreeSellers.forEach((key, value) -> System.out.printf("%s com %s em vendas", key, this.toCurrency(value)).println());


        System.out.println(" 01.\tSelecione o nome dos 3 piores vendedores, aqueles que venderam menos (em valores) durante todos os anos.");
        final var bottom3WorstSellers = reader.bottom3WorstSellers();
        bottom3WorstSellers.forEach((key, value) -> System.out.printf("%s com %s em vendas por valor", key, this.toCurrency(value)).println());
        System.out.println('\n');

//        List<String> worstSellersCompleted = reader.bottom3WorstSellersNumVendasCompleted();
//        System.out.println("Os 3 piores vendedores vendas completas são: " + worstSellersCompleted);
//        System.out.println('\n');
//
//        List<String> worstSellers = reader.bottom3WorstSellersNumVendasCompletedCancelled();
//        System.out.println("Os 3 piores vendedores vendas completas e canceladas são: " + worstSellers);
//        System.out.println('\n');

        System.out.println("02.\tQual o total de vendas caso os seguintes filtros fossem utilizados: vendas completas, no estado do paraná, nos meses de janeiro, junho e dezembro");
        final var totalSalesByStatusAndMonthPr = reader.totalSalesByStatusAndMonthAndStatePr(Sale.Status.COMPLETED, "PR", Month.JANUARY, Month.JUNE, Month.DECEMBER);
        System.out.printf("As vendas completas, no estado do paraná, nos meses de janeiro, junho e dezembro %s%n", toCurrency(totalSalesByStatusAndMonthPr));
        System.out.println('\n');

        System.out.println("03.\tEntre 12/05/2012 e 29/07/2012 a vendedora Rosângela Almeida realizou quantas vendas?");
        LocalDate startDate = LocalDate.of(2012, Month.MAY, 12);
        LocalDate endDate = LocalDate.of(2012, Month.JULY, 29);
        long numberOfSales = reader.countCompletedSalesBySellerInPeriod("Rosângela Almeida", startDate, endDate);
        System.out.println("Número de vendas de Rosângela Almeida entre 12/05/2012 e 29/07/2012: " + numberOfSales);
        System.out.println('\n');


        System.out.println("04.\tEm qual ano tivemos R$ 3.689.379,28 em vendas?");
        Map<Integer, BigDecimal> salesByYear = reader.totalSalesByYear();
        BigDecimal targetAmount = new BigDecimal("3689379.28");

        for (Map.Entry<Integer, BigDecimal> entry : salesByYear.entrySet()) {
            if (entry.getValue().compareTo(targetAmount) == 0) {
                System.out.printf("No ano de %d, as vendas totalizaram R$ %s%n", entry.getKey(), toCurrency(entry.getValue()));
            }
        }
        System.out.println('\n');

        System.out.println("05.\tNa equipe de qual gerente houveram mais cancelamentos de vendas entre 2013 e 2014?");
        String managerWithMostCancellations = reader.managerWithMostCancelledSalesBetween2013And2014();
        System.out.println("O gerente com mais cancelamentos entre 2013 e 2014 é: " + managerWithMostCancellations);
    }

    private String toCurrency(BigDecimal value) {
        return NumberFormat.getInstance().format(value);
    }

    public static void main(String[] args) {
        new Application();
    }
}
