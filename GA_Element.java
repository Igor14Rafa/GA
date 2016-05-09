import java.util.Random;

public class GA_Element implements Comparable<GA_Element>{
	private String valor;
	private double fator_avaliacao = 0;
	private double penalização = 0;
	private double avaliacao;
	
	protected void inicializaElemento(int tamanho) {
		int i;
		this.valor="";
		for(i = 0;i < tamanho;++i) {
			if (Math.random() < 0.5) {
				this.valor = this.valor + "0";
			} else {
				this.valor = this.valor + "1";
			}
		}
	}

	public void penalizacao(double x, double y){
		int fator = 10;
		double score = 300 - (x + y);
		if ((x + y) != 300)
			this.penalização = (score * score) * fator; 
	}

	/****************/
	/* Construtores */
	/****************/

	public GA_Element(String novoValor) {
		this.valor = novoValor;
	}	

	public GA_Element(int tamanho) {
		inicializaElemento(tamanho);
	}

	public GA_Element(){
		this(20);
	}

	/************************/
	/* Operadores Geneticos */
	/************************/

	public void mutacao(double taxa) {
		int tamanho = this.valor.length();
		Random gen = new Random();
		char aux;
		char aux_value[] = this.valor.toCharArray();
			
		for(int i = 0; i < (int)(tamanho * taxa); i++) {
			int index = gen.nextInt(tamanho);
			aux = this.valor.charAt(index);
			if (aux == '1') {
				aux_value[index] = '0'; 
			} else {
				aux_value[index] = '1';
			}
		}
		
		this.valor = String.copyValueOf(aux_value);
	}

	public GA_Element crossoverUmPonto(GA_Element outroPai,double taxa_crossover)  {
		String aux1;	   
		GA_Element retorno = null;
		int pontoCorte = (new Double(taxa_crossover*this.valor.length())).intValue();
		if (Math.random() < 0.5) {		
			aux1 = this.valor.substring(0,pontoCorte)+outroPai.getValor().substring(pontoCorte,outroPai.getValor().length());
		} else {		
			aux1 = outroPai.getValor().substring(0,pontoCorte)+this.valor.substring(pontoCorte,this.valor.length());
		}

		try {
			retorno=(GA_Element) outroPai.getClass().newInstance();
			retorno.setValor(aux1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return(retorno);

	}


	public GA_Element crossoverDoisPontos(GA_Element outroPai,double taxa_crossover) {
		String aux1 = "";
		GA_Element retorno = null;
		int pontoCorte1 = (new Double(taxa_crossover * (this.valor.length() - 1))).intValue();
		int pontoCorte2 = (new Double(taxa_crossover * (this.valor.length() - (pontoCorte1 + 1)))).intValue();
		pontoCorte2 += pontoCorte1;
		if (Math.random() < 0.5) {
			aux1 = this.valor.substring(0, pontoCorte1);
			aux1 = aux1 + outroPai.getValor().substring(pontoCorte1, pontoCorte2);
			aux1 = aux1 + this.valor.substring(pontoCorte2, this.valor.length());
		} 
		else {
			aux1 = outroPai.getValor().substring(0, pontoCorte1);
			aux1 = aux1 + this.valor.substring(pontoCorte1, pontoCorte2);
			aux1 = aux1 + outroPai.getValor().substring(pontoCorte2,outroPai.getValor().length());
		}
		try {
			retorno = (GA_Element) outroPai.getClass().newInstance();
			retorno.setValor(aux1);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}

	public GA_Element crossoverUniforme(GA_Element outroPai, double taxa_crossover) {
		String aux1 = "";
		GA_Element retorno = new GA_Element();
		retorno.setValor(this.getValor());
		int i;
		for (i = 0; i < this.valor.length(); i++) {
			if (java.lang.Math.random() < taxa_crossover) {
				aux1 = aux1 + this.valor.substring(i, i + 1);
			} 
			else {
				aux1 = aux1 + outroPai.getValor().substring(i, i + 1);
			}
		}
		try {
			retorno = (GA_Element) outroPai.getClass().newInstance();
			retorno.setValor(aux1);
		} 
		catch (Exception e) {
		}
		return (retorno);
	}

	/********************************/
	/* Metodos Basicos de Classe    */
	/********************************/

	public String toString() {
		return("String: "+this.getValor() +"\nAvaliacao: "+this.getFator_avaliacao());
	}

	public int compareTo(GA_Element compared) {
		// TODO Auto-generated method stub
		if(this.getAvaliacao() < compared.getAvaliacao()) 
			return 1;
		if(this.getAvaliacao() > compared.getAvaliacao())
			return -1;
		return 0;
	}
	
	public double getFator_avaliacao() {
		return fator_avaliacao;
	}

	public void setFator_avaliacao(double fator_avaliacao) {
		this.fator_avaliacao = fator_avaliacao;
	}

	public String getValor() {
		return(this.valor);
	}

	public void setValor(String aux) {
		this.valor = aux;
	}

	public double getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(double avaliacao) {
		this.avaliacao = avaliacao;
	}

	public double getPenalização() {
		return penalização;
	}

	public void setPenalização(double penalização) {
		this.penalização = penalização;
	}
}