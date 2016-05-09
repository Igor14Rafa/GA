import java.util.Collections;
import java.util.Vector;

public class GA_Basic {
	private Vector<Element_1> populacao;
	private double somaAvaliacao, somaFatorAvaliacao;
	private double taxa_mutacao,taxa_crossover, pressao_selecao, chance_crossover, chance_mutacao, phi, elitismo;
	private Vector<Element_1> nova_populacao;
	private int num_ger,tam_pop, tam_nova_pop;

	private double somaFatorAvaliacao() {
		int i;
		double sum = 0;
		this.setSomaFatorAvaliacao(0);
		for(i = 0; i < this.populacao.size(); i++) {
			sum += this.populacao.get(i).calculaFatorAvaliacao();
		}
		this.setSomaFatorAvaliacao(sum);
		return this.getSomaFatorAvaliacao();
	}

	private double SomaValorFuncao(){
		double sum_funct = 0;
		for(int i = 0;i < this.populacao.size(); i++)
			sum_funct += this.populacao.get(i).getFator_avaliacao();
		return sum_funct;
	}

	public void inicializaPopulacao() {

		this.populacao = new Vector<Element_1>();
		Vector<Element_1> pop_selected = new Vector<Element_1>();
		Vector<Element_1> aux_selected = new Vector<Element_1>();

		tam_nova_pop = (int)(tam_pop * phi);
		int tam_elitismo = (int)(tam_pop * elitismo);

		for(int i = 0; i < tam_pop; i++) 
			this.populacao.add(new Element_1()); 

		this.avaliaTodos();
		Collections.sort(this.populacao);

		for(int i = 0; i < (tam_pop * elitismo); i++)
			pop_selected.add(this.getPopulacao().get(i));

		for(int i = 0; i < (tam_pop * pressao_selecao); i++)
			aux_selected.add(this.populacao.get(i));

		for(int i = 0; i < (tam_nova_pop - tam_elitismo) ; i++){
			pop_selected.add(aux_selected.get(this.torneio(16, aux_selected, aux_selected.size())));
		}

		this.populacao.removeAllElements();
		this.populacao.addAll(pop_selected);

	}

	public void calculaAvaliacaoIndividual(){
		int i;
		double avaliacao_aux,penalizacao_aux = 0;
		for(i = 0;i < this.populacao.size();i++){
			avaliacao_aux = this.populacao.elementAt(i).getFator_avaliacao();
			penalizacao_aux = this.populacao.elementAt(i).getPenalização();
			this.populacao.elementAt(i).setAvaliacao(this.getSomaFatorAvaliacao()/(avaliacao_aux + penalizacao_aux));
		}
	}

	public double calculaSomaAvaliacao(){
		int i;
		this.somaAvaliacao = 0;
		this.somaFatorAvaliacao();
		this.calculaAvaliacaoIndividual();
		for(i = 0;i < this.populacao.size();i++){
			this.somaAvaliacao += this.populacao.get(i).getAvaliacao();
		}
		return this.getSomaAvaliacao();

	}

	public int roleta() {
		int i;
		double aux = 0;
		double limite = Math.random() * this.somaAvaliacao;	

		for(i = 0;( (i < this.populacao.size()) && (aux < limite) );i++) {
			aux += ((GA_Element) this.populacao.get(i)).getAvaliacao();
		}

		i--;	
//		System.out.println("Escolhi o elemento de indice " + i);
		return(i);
	}

	public int torneio(int size_tournament, Vector<Element_1> population, int lenght_tournament){
		int i, pai = 0;

		double av_melhor = 0;
		for(i = 0;i < size_tournament;i++){
			int index = (int)(lenght_tournament * Math.random());
//			System.out.println("Adicionei o elemento :" + population.get(index).getValor() + " " + population.get(index).getFator_avaliacao() + " ao torneio");
			if(population.get(index).getAvaliacao() > av_melhor){
				pai = index;
				av_melhor = population.get(index).getAvaliacao();
			}
		}
//		System.out.println("Selecionei o pai " + population.get(pai).getValor());
		return pai;
	}

	public void geracao() {

		this.nova_populacao = new Vector<>();

		//Produzo a próxima geração
		GA_Element pai1,pai2, filho1, filho2;

		int i;
		int lenght_tournament = (int)(tam_nova_pop * pressao_selecao); 
//		System.out.println("Calculando nova geracao...\nFazendo a selecao dos pais\n");
		for(i = 0; i < tam_nova_pop; i++) {
			
			pai1 = (GA_Element)this.populacao.get(this.torneio(16, this.populacao, lenght_tournament));
			pai2 = (GA_Element)this.populacao.get(this.torneio(16, this.populacao, lenght_tournament));
			
			if(Math.random() < this.chance_crossover){
				filho1 = pai1.crossoverDoisPontos(pai2,this.taxa_crossover);
//				System.out.println("Pais: " + pai1.getValor() + " " + pai2.getValor() + "\nVou adicionar o filho " + filho1.getValor() + "\n");
				filho2 = pai2.crossoverDoisPontos(pai1,this.taxa_crossover);
//				System.out.println("Pais: " + pai2.getValor() + " " + pai1.getValor() + "\nVou adicionar o filho " + filho2.getValor() + "\n");

				if(Math.random() < this.chance_mutacao){	
					filho1.mutacao(taxa_mutacao);
					filho2.mutacao(taxa_mutacao);
				}

				this.nova_populacao.add((Element_1)filho1);
				this.nova_populacao.add((Element_1)filho2);
			}
			
			else{

				this.nova_populacao.add((Element_1)pai1);
				this.nova_populacao.add((Element_1)pai2);
			}
		}
	}

	public void moduloPopulacao() {

		this.populacao.removeAllElements();
		this.populacao.addAll(nova_populacao);	
	}

	public int determinaMelhor() {

		int i,ind_melhor = 0;
		GA_Element aux;
		double aval_melhor = ((GA_Element)this.populacao.get(0)).getAvaliacao();
		for(i = 1; i < this.populacao.size(); i++) {
			aux = (GA_Element)this.populacao.get(i);		
			if (aux.getAvaliacao() > aval_melhor) {
				aval_melhor = aux.getAvaliacao();
				ind_melhor = i;
			}
		}
		return(ind_melhor);
	}

	private double media(){
//		System.out.println(this.getSomaFatorAvaliacao() + " " + this.populacao.size() + " " + this.getSomaFatorAvaliacao() / this.populacao.size());
		return (this.getSomaAvaliacao()/this.populacao.size());
	}

	public void avaliaTodos() {		
//		System.out.println("Avaliando todos...\n");
		this.setSomaAvaliacao(this.calculaSomaAvaliacao());
//		System.out.println("A soma das avaliacoes eh " + this.getSomaAvaliacao());
	}

	public int executa() {

		int i;
		double c_media = 0;
		this.inicializaPopulacao();
		for (i = 0; i < this.num_ger; i++) {
//			System.out.println("Geracao " + i + "\n");
//			System.out.println(this.chance_mutacao + " " + this.chance_crossover + " " + this.tam_pop + " " + this.pressao_selecao + "\n");
//			System.out.println("\n" + this.media() +  " " + Math.abs(this.media() + 3400) +  "\n");

			this.avaliaTodos();

//			if(Math.abs(this.populacao.get(this.determinaMelhor()).getFator_avaliacao() - 3400.0) < 0.97) {
			if(this.populacao.get(this.determinaMelhor()).getAvaliacao() - this.media() < (this.populacao.get(this.determinaMelhor()).getAvaliacao() * 0.1)){ 
				System.out.println("Ótimo Encontrado!!!");
				System.out.println(this.media());
				System.out.println(this.populacao.get(this.determinaMelhor()).toString());
				System.out.println("Optimum Setup: " + this.chance_mutacao + " " + this.chance_crossover + " " + this.tam_pop + " " + this.pressao_selecao);
				return 1;
			}

			this.geracao();
			this.moduloPopulacao();

		}

		return 0;
	}

	/**
	 * Construtores
	 */

	public GA_Basic(int num_ger,int tam_pop, double tx_mut, double tx_cross, double pres_sel, double chc_cross, double chc_mut, double phi, double elitismo) {
		setNumero_geracoes(num_ger);
		setTamanho_populacao(tam_pop);
		setTaxa_mutacao(tx_mut);
		setTaxa_crossover(tx_cross);
		setPressao_selecao(pres_sel);
		setChance_crossover(chc_cross);
		setChance_mutacao(chc_mut);
		setPhi(phi);
		setElitismo(elitismo);
	}

	public GA_Basic(int num_ger,int tam_pop, double tx_mut, double tx_cross, double pres_sel) {
		setNumero_geracoes(num_ger);
		setTamanho_populacao(tam_pop);
		setTaxa_mutacao(tx_mut);
		setTaxa_crossover(tx_cross);
		setPressao_selecao(pres_sel);
	}

	public GA_Basic(int num_geracoes,int tam_populacao, double taxa_crossover, double pres_sel) {
		setNumero_geracoes(num_geracoes);
		setTamanho_populacao(tam_populacao);
		setTaxa_crossover(taxa_crossover);
		setPressao_selecao(pres_sel);
	}

	public Vector<Element_1> getPopulacao() {
		return populacao;
	}

	public void setPopulacao(Vector<Element_1> populacao) {
		this.populacao = populacao;
	}

	public double getChance_mutacao() {
		return taxa_mutacao;
	}

	public void setChance_mutacao(double chance_mutacao) {
		this.taxa_mutacao = chance_mutacao;
	}

	public Vector<Element_1> getNova_populacao() {
		return nova_populacao;
	}

	public void setNova_populacao(Vector<Element_1> nova_populacao) {
		this.nova_populacao = nova_populacao;
	}

	public int getNumero_geracoes() {
		return num_ger;
	}

	public void setNumero_geracoes(int numero_geracoes) {
		this.num_ger = numero_geracoes;
	}

	public int getTamanho_populacao() {
		return tam_pop;
	}

	public void setTamanho_populacao(int tamanho_populacao) {
		this.tam_pop = tamanho_populacao;
	}

	public double getSomaFatorAvaliacao() {
		return somaFatorAvaliacao;
	}

	public void setSomaFatorAvaliacao(double somaFatorAvaliacao) {
		this.somaFatorAvaliacao = somaFatorAvaliacao;
	}

	public double getSomaAvaliacao() {
		return somaAvaliacao;
	}	

	public void setSomaAvaliacao(double somaAvaliacao) {
		this.somaAvaliacao = somaAvaliacao;
	}

	public double getPressao_selecao() {
		return pressao_selecao;
	}

	public void setPressao_selecao(double pressao_selecao) {
		this.pressao_selecao = pressao_selecao;
	}

	public double getTaxa_mutacao() {
		return taxa_mutacao;
	}

	public void setTaxa_mutacao(double taxa_mutacao) {
		this.taxa_mutacao = taxa_mutacao;
	}

	public double getTaxa_crossover() {
		return taxa_crossover;
	}

	public void setTaxa_crossover(double taxa_crossover) {
		this.taxa_crossover = taxa_crossover;
	}

	public double getChance_crossover() {
		return chance_crossover;
	}

	public void setChance_crossover(double chance_crossover) {
		this.chance_crossover = chance_crossover;
	}

	public double getPhi() {
		return phi;
	}

	public void setPhi(double phi) {
		this.phi = phi;
	}

	public double getElitismo() {
		return elitismo;
	}

	public void setElitismo(double elitismo) {
		this.elitismo = elitismo;
	}
}
