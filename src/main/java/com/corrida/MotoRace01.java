package com.corrida;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

class MotoRace {
	private static final int NUM_THREADS = 10;
	private static final int NUM_RACES = 10;
	private static int[] placar = new int[NUM_THREADS];
	private static int[] pontuacaoCampeonato = new int[NUM_THREADS];
	public static void main(String[] args) {
		for (int i = 1; i <= NUM_RACES; i++) {
			System.out.println("\n========== Corrida #" + i + " ==========");

			// ambos utilizados para sincronizar a largada e chegada dos competidores com CountDownLatch
			// lembrando que por ser um atributo da classe Competidor, mais abaixo será carregado no construtor 
			CountDownLatch startLatch = new CountDownLatch(1);
			CountDownLatch finishLatch = new CountDownLatch(NUM_THREADS);
			List<Thread> newThreads = new ArrayList<Thread>();
			Competidor[] competidores = new Competidor[NUM_THREADS];

			// cria as threads dos competidores e os objs
			for (int j = 1; j <= NUM_THREADS; j++) {
				// na chamada do construtor, cada competidor é inicializado com a mesma
				// instância de startLatch e finishLatch para sincronizar a largada e a chegada
				Competidor competidor = new Competidor(j, startLatch, finishLatch);
				// faz a associação entre a thread e o obj competidor e mais tarde cria a lista
				// de largada
				Thread thread = new Thread(competidor);
				thread.setName("Competidor #" + j);
				competidores[j - 1] = competidor;
				newThreads.add(thread);
			}

			// Inicia a contagem regressiva para a largada dos competidores
			startLatch.countDown();

			for (Thread thread : newThreads) {
				thread.start();
			}

			try {
				// Aguarda a finalização de todas as threads dos competidores
				finishLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			ordenarCompetidoresPorChegada(competidores);

			atribuirPontos(competidores);

			System.out.println("\n===== Resultados da Corrida #" + i + " =====");
			mostrarPlacar();
			mostrarPodio();
			zerarPlacar();
		}

		System.out.println("\n===== Pódio Final do Campeonato =====");
		mostrarPodioFinal();
	}

	private static synchronized void adicionarPontos(int competidor, int pontos) {
		placar[competidor - 1] += pontos;
		pontuacaoCampeonato[competidor - 1] += pontos;
	}

	private static synchronized void atribuirPontos(Competidor[] competidores) {
		int pontos = NUM_THREADS;
		for (Competidor competidor : competidores) {
			adicionarPontos(competidor.getNumero(), pontos);
			pontos--;
		}
	}

	private static synchronized void mostrarPlacar() {
		for (int i = 0; i < NUM_THREADS; i++) {
			System.out.println("Competidor #" + (i + 1) + " somou: " + placar[i] + " pontos");
		}
	}

	private static void zerarPlacar() {
		for (int i = 0; i < NUM_THREADS; i++) {
			placar[i] = 0;
		}
	}

	private static void mostrarPodio() {
		System.out.println("\n===== Pódio da Corrida =====");
		for (int i = 0; i < 3; i++) {
			int index = getMaxIndex(placar);
			System.out.println((i + 1) + "º Lugar: Competidor #" + (index + 1) + " - " + placar[index] + " pontos.");
			placar[index] = -1;
		}
	}

	private static void ordenarCompetidoresPorChegada(Competidor[] competidores) {
		for (int i = 0; i < NUM_THREADS - 1; i++) {
			for (int j = i + 1; j < NUM_THREADS; j++) {
				if (competidores[i].getOrdemChegada() > competidores[j].getOrdemChegada()) {
					Competidor temp = competidores[i];
					competidores[i] = competidores[j];
					competidores[j] = temp;
				}
			}
		}
	}

	private static int getMaxIndex(int[] arr) {
		int maxIndex = 0;
		int maxVal = arr[0];

		for (int i = 1; i < arr.length; i++) {
			if (arr[i] > maxVal) {
				maxVal = arr[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	private static void mostrarPodioFinal() {
		Competidor[] competidores = new Competidor[NUM_THREADS];
		for (int i = 0; i < NUM_THREADS; i++) {
			Competidor competidor = new Competidor(i + 1, null, null);
			competidor.setPontuacao(pontuacaoCampeonato[i]);
			competidores[i] = competidor;
		}

		ordenarCompetidoresPorPontuacao(competidores);

		for (int i = 0; i < 3; i++) {
			System.out.println("Competidor #" + competidores[i].getNumero() + " - com "
					+ competidores[i].getPontuacao()+ " pontos.");					
		}
		System.out.println("\n-----------------------------------------");
		System.out.println("\n===== Tabela de Pontos =====");
		
		for (int i = 0; i < NUM_THREADS; i++) {
			System.out.println("Competidor #" + competidores[i].getNumero() + " - com "
					+ competidores[i].getPontuacao()+ " pontos.");					
		}
	}

	private static void ordenarCompetidoresPorPontuacao(Competidor[] competidores) {
		for (int i = 0; i < NUM_THREADS - 1; i++) {
			for (int j = i + 1; j < NUM_THREADS; j++) {
				if (competidores[i].getPontuacao() < competidores[j].getPontuacao()) {
					Competidor temp = competidores[i];
					competidores[i] = competidores[j];
					competidores[j] = temp;
				}
			}
		}
	}

	public static class Competidor implements Runnable {
		private int numero;
		private int ordemChegada;
		private int pontuacao;
		private Random random = new Random();
		private CountDownLatch startLatch;
		private CountDownLatch finishLatch;

		public Competidor(int numero, CountDownLatch startLatch, CountDownLatch finishLatch) {
			this.numero = numero;
			this.startLatch = startLatch;
			this.finishLatch = finishLatch;
		}

		public int getNumero() {
			return numero;
		}

		public int getOrdemChegada() {
			return ordemChegada;
		}

		public int getPontuacao() {
			return pontuacao;
		}

		public void setPontuacao(int pontuacao) {
			this.pontuacao = pontuacao;
		}

		public void run() {
			try {
				// Espera pela contagem regressiva da largada
				startLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// O objetivo é simular uma variação de tempo realista para 
			// cada competidor cruzar a linha de chegada, tornando a corrida imprevisível.
			int tempo = random.nextInt(1000) + 100;
			//range otimista da duração da corrida: 100ms; pessimista: 1100ms 
			try {
				Thread.sleep(tempo);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			synchronized (MotoRace.class) {
				// competidores que ainda não cruzaram a linha de chegada
				ordemChegada = (int) finishLatch.getCount();
			}

			System.out.println(Thread.currentThread().getName() + " cruzou a linha de chegada.");

			// Indica que o competidor terminou a corrida
			finishLatch.countDown();
		}
	}
}