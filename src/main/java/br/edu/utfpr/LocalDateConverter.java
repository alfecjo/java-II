package br.edu.utfpr;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter extends AbstractBeanField<String, LocalDate> {

    @Override
    protected LocalDate convert(String value) throws CsvDataTypeMismatchException {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(value, dateFormatter);
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
            throw new CsvDataTypeMismatchException(e.getMessage());
        }
    }
}