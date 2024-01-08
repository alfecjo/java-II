# ☕ Linguagem De Programação Java II..

## Este material foi desenvolvido em resposta a disciplina 'Java II', a qual faz parte do curso de Pós Graduação em Tecnologia Java, ministrado pela Universidade Tecnológica Federal do Paraná.

🎉 Os projetos, são exercícios solicitados ao longo do curso que juntos perfazem a nota que compõem a média final.

🥋 Se você está entrando no Java agora, vou deixar um comentário apenas para orientá-lo. Este exercício, foi considerado pelo professor e os demais colegas, como HARD na disciplina Java II (_😎tirei nota máxima_), muito usado na criação de manipulação de aquivos em Java.

## Sistema de Arquivos 

😵 Objetivo:

Fixar o aprendizado da API de IO do Java através da construção de um sistema de arquivos virtual em Java

O sistema de arquivos será composto por uma aplicação de linha de comando que suporte os seguintes comandos:

1. SHOW deverá mostrar o conteúdo de um determinado arquivo, caso seja usado em diretórios, deverá ser exibido um erro
2. LIST deve listar o conteúdo do diretório atual
3. BACK deve ser usado para sair de um diretório e ir para o seu "parente", caso seja usado na raiz, um erro deve ser exibido informando que não há como ir além do diretório raiz
4. OPEN deve ser usado para abrir (ou acessar) um determinado diretório recebido como parâmetro
5. DETAIL deve ser usado para detalhar informações de um arquivo ou diretório através da classe BasicFileAttributeView do NIO2, faça uma pesquisa na documentação da linguagem e/ou no material de apoio para entender como usar esta classe

👁️‍🗨️ Entrega

Enviar apenas os arquivos FileReader.java e Command.java. Não compacte, apenas envie pelo moodle.

🕵️ Critérios de aceite e avaliação do exercício:

1. Interpretação do código faz parte da solução da atividade, utilize os recursos de debug para entender como ele funciona e fazer o que se pede
2. Há alguns TODOS dentro do código para ajudar a encontrar os locais que precisam ser feitas as implementações
3. Comandos inválidos ou que não estejam consistentes (ex.: um OPEN sem parâmetros) não devem quebrar a execução, caso o faça, haverá desconto de nota.
4. Cada comando vale 20 pontos, totalizando 100 pontos
5. Estamos em Java II, logo, espera-se um código que siga os padrões de linguagem da plataforma. O não uso dos padrões pode acarretar descontos de nota.
6. Atividades em modo rascunho não são avaliadas, verifique se realizou o envio em definitivo
7. Envios com atraso tem desconto de 1 ponto por dia de atraso.

### 📽️ Click na imagem e assista ao vídeo de apresentação do Projeto..     

[![Assista ao Vídeo de Apresentação dos Testes](https://img.youtube.com/vi/2_WtivOfl_M/maxresdefault.jpg)](https://www.youtube.com/watch?v=2_WtivOfl_M)

# Tecnologia utilizada:

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)

## Tabela de Conteúdos

- [Instalação](#Instalação)
- [Uso](#Uso)
- [Contribuição](#Contribuição)

## Instalação

1. Clone o repositório ou baixe o arquivo .zip:

```bash
git clone https://github.com/alfecjo/Java-II.git
```
## Uso

1. Execute em sua IDE de preferência. Contudo, o desenvolvimento foi feito no IntelliJ! Você pode começar com: "mvn install", no diretório raiz, que é onde se encontra o
   arquivo pom.xml. Desta forma, serão baixadas as dependências, caso seja necessário.

## Contribuição

Contribuições são bem-vindas! Se você encontrar algum problema ou tiver sugestões de melhorias, abra um problema ou envie uma solicitação pull ao repositório.

Ao contribuir para este projeto, siga o estilo de código existente, [convenções de commit](https://www.conventionalcommits.org/en/v1.0.0/), e envie suas alterações em um branch separado.

Muito obrigado!!
