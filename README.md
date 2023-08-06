Corrida de Motocicletas 

Objetivo:

Exercitar o conceito e a prática de Race Condition e Critical Sections através do uso de sincronismo de métodos e/ou objetos para simular uma corrida de motos onde o ganhador é aquele que somar mais pontos ao acessar a região crítica (ou linha de chegada).

Requisitos:

1. (10 pts) O programa deve criar no mínimo 10 threads que vão representar cada competidor, cada competidor deve usar o seguinte nome: Competidor #N (onde N é o número da thread)

2. (10 pts) Haverá no mínimo 10 corridas

3. (10 pts) Uma corrida só inicia após o fim da corrida anterior, ou seja, quando todos os competidores cruzarem a linha de chegada (área crítica)

4. (30 40 pts) O placar da nossa corrida será contabilizado toda vez que um corredor cruzar a linha de chegada (área crítica), o competidor receberá pontos de acordo com sua posição de chegada ou seja: competidor 3 chegou primeiro na área sincronizada, recebe 10 pontos, o competidor 5 chegou em segundo, recebe 9 pontos, e assim sucessivamente até que o ultimo a cruzar a linha de chegada ganhe 1 ponto

5. (10 pts) As 10 corridas formam um campeonato, ganhará aquele que somar o maior número de pontos no campeonato

6. (10 pts) Ao final do campeonato deverá ser apresentado o placar com o total dos pontos e o pódio, nesta estrutura:

podio

Critérios de aceite e avaliação do exercício:

1. Cada requisito possui um peso que compõe 80 pontos no total, cumpra todos para ter nota máxima, cumpra alguns e sua nota será aplicada conforme cada requisito implementado.

2. Estrutura do código (uso das regras de escrita da linguagem, estrutura) e parametrização dos valores de corridas e competidores (sim, eu posso querer ter um campeonato com 20 participantes e 30 corridas) terão peso de 10 pontos

3. Entrega no prazo, 10 pontos

Importante:

- Cópias e/ou exercícios duplicados/idênticos serão descartados, apenas os pontos da entrega (se feita no prazo) serão contabilizados.

- Faça o programa todo em apenas uma classe, utilize recursos como classes aninhadas para organização, caso envie o projeto e/ou PDF's ou vários arquivos, sua atividade não será avaliada e o ato do reenvio da mesma incorrerá em desconto por atraso

- Envie APENAS o arquivo .java do programa, nada mais.

- O objetivo da atividade também é parte da avaliação!

- Envios com atraso tem o desconto da pontualidade (acima, item 3) e desconto de 1 ponto por dia de atraso, por exemplo: se vc atrasar 1 dia, terá 1 ponto de desconto por atraso + 10 da pontualidade, totalizando 11 pontos a menos na nota.
