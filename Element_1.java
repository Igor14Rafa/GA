public class Element_1 extends GA_Element {

	private float converteBinario(int inicio,int fim) {
		
		int i;
		float aux = 0;
		String s = this.getValor();
		for(i = inicio;i <= fim;i++) {
			aux *= 2;
			if (s.substring(i,i + 1).equals("1")) {
				aux += 1;
			}
		}
		return(aux);
	}

	public double calculaFatorAvaliacao(){
		
		double x = this.converteBinario(0,9);
		double y = this.converteBinario(10,19);

		x = 0 + x * ((1000 - 0)/(Math.pow(2, 10) - 1));
		y = 0 + y * ((700 - 0)/(Math.pow(2, 10) - 1));

		this.penalizacao(x,y);

		double fator_x = 0.01*(x*x) - 10*x + 5000;
		double fator_y = 0.005*(y*y) - 3*y + 500;

		this.setFator_avaliacao(fator_x + fator_y);
		return this.getFator_avaliacao();
	}

	public Element_1(int tamanho) {
		super(tamanho);
	}

	public Element_1() {
		super(20);
	}

	public Element_1(String novoValor) {
		setValor(novoValor);
	}

	public String toString() {
		return("String: x = "+(this.converteBinario(0,9)*0.97751710654936461388074291300098)+" y = "+(this.converteBinario(10,19)*0.68426197458455522971652003910068)+"\nValor Função: "+this.getFator_avaliacao());
	}
}
