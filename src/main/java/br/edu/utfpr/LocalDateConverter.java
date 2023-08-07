package br.edu.utfpr;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter extends AbstractBeanField<String, LocalDate> {

//    @Override
//    protected LocalDate convert(String value) {
//        // TODO implementar a conversao de datas aqui
//        return null;
//    }
//}
    @Override
    protected LocalDate convert(String value) throws CsvDataTypeMismatchException {
        try {
            // Defina o formato da data presente no arquivo CSV (dd/MM/yyyy)
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            // Faz a conversão da string para LocalDate usando o DateTimeFormatter
            return LocalDate.parse(value, dateFormatter);
        } catch (Exception e) {
            // Caso ocorra algum erro na conversão, lance uma exceção CsvDataTypeMismatchException
            System.out.println("Error: "+e.getMessage());
            throw new CsvDataTypeMismatchException(e.getMessage());
        }
    }
}