package br.edu.utfpr.sistemarquivos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;

public enum Command {
    /* OK!!! */
    LIST() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("LIST") || commands[0].startsWith("list");
        }

        @Override
        Path execute(Path path) throws IOException {

            // TODO implementar conforme enunciado

            Files.list(path)
                    .map(Path::getFileName)
                    .forEach(System.out::println);

            return path;
        }
    },

    /* OK!!! */
    SHOW() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("SHOW") || commands[0].startsWith("show");
        }

        @Override
        Path execute(Path path) throws IOException {

            // TODO implementar conforme enunciado
            FileReader.read(path, parameters);
            return path;
        }
    },

    /* OK!!! */
    BACK() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("BACK") || commands[0].startsWith("back");
        }

        @Override
        Path execute(Path path) {

            // TODO implementar conforme enunciado

            if (path.endsWith("hd")) {
                throw new UnsupportedOperationException("Erro: Já está no diretório raiz!");
            } else {
                return Paths.get(Application.ROOT);
            }
        }
    },

    /* OK!!! */
    OPEN() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("OPEN") || commands[0].startsWith("open");
        }

        @Override
        Path execute(Path path) {

            // TODO implementar conforme enunciado

            if (parameters.length == 1) {
                throw new UnsupportedOperationException("Comando OPEN requer um nome de diretório como argumento!");
            }

            String directoryName = parameters[1];
            Path newDirectoryPath = path.resolve(directoryName);

            System.out.println(parameters[1] + ">...");

            if (!Files.isDirectory(newDirectoryPath)) {
                throw new UnsupportedOperationException("Erro: '" + directoryName + "' não é um diretório válido!");
            }
            path = newDirectoryPath;

            return path;
        }
    },

    /* OK!!! */
    DETAIL() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("DETAIL") || commands[0].startsWith("detail");
        }

        @Override
        Path execute(Path path) throws IOException {

            // TODO implementar conforme enunciado

            if (parameters.length == 1) {
                throw new UnsupportedOperationException("Comando DETAIL requer argumento!");
            }

            String fileName = parameters[1];
            Path filePath = path.resolve(fileName);

            if (!Files.exists(filePath)) {
                throw new UnsupportedOperationException("Erro: o arquivo '" + fileName + "' não foi encontrado!");
            }

            BasicFileAttributeView fileAttributeView = Files.getFileAttributeView(filePath, BasicFileAttributeView.class);
            BasicFileAttributes fileAttributes = fileAttributeView.readAttributes();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            System.out.println("Detalhes do " + (fileAttributes.isDirectory() ? "Diretório '" : "Arquivo '") + filePath.getFileName() + "':");
            if (!fileAttributes.isDirectory()) {
                System.out.println("Tamanho: " + fileAttributes.size() + " bytes");
            }
            System.out.println("Data de Criação: " + dateFormat.format(fileAttributes.creationTime().toMillis()));
            System.out.println("Último Acesso: " + dateFormat.format(fileAttributes.lastAccessTime().toMillis()));
            return path;
        }
    },

    /* OK!!! */
    EXIT() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("EXIT") || commands[0].startsWith("exit");
        }

        @Override
        Path execute(Path path) {
            System.out.print("Saindo... \nObrigado por adquirir o software da BigBlue!!!\n");
            return path;
        }

        @Override
        boolean shouldStop() {
            return true;
        }
    };

    abstract Path execute(Path path) throws IOException;

    abstract boolean accept(String command);

    void setParameters(String[] parameters) {
    }

    boolean shouldStop() {
        return false;
    }

    public static Command parseCommand(String commandToParse) {

        if (commandToParse.isBlank()) {
            throw new UnsupportedOperationException("Type something...");
        }

        final var possibleCommands = values();

        for (Command possibleCommand : possibleCommands) {
            if (possibleCommand.accept(commandToParse)) {
                possibleCommand.setParameters(commandToParse.split(" "));
                return possibleCommand;
            }
        }

        throw new UnsupportedOperationException("Can't parse command [%s]".formatted(commandToParse));
    }
}
