package br.edu.utfpr.sistemarquivos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReader {

    public static void read(Path path, String[] parameters) throws IOException{
        // TODO implementar a leitura dos arquivos do PATH aqui

        if (parameters.length == 1) {
            throw new UnsupportedOperationException("Erro: Comando SHOW requer: nome de arquivo!");
        }

        String fileName = parameters[1];
        Path filePath = path.resolve(fileName);

        if (Files.isDirectory(filePath)) {
            throw new UnsupportedOperationException("Erro: '" + fileName + "' é um diretório. Comando apenas para arquivos!");
        }

        if (Files.exists(filePath)) {
            fileName = filePath.getFileName().toString();
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
            if (extension.equalsIgnoreCase("txt")) {
                Files.lines(filePath).forEach(System.out::println);
            } else {
                throw new UnsupportedOperationException("Erro: o arquivo '" + fileName + "' não possui extensão .txt!");
            }
        } else {
            throw new UnsupportedOperationException("Erro: o arquivo '" + fileName + "' não foi encontrado!");
        }
    }
}
