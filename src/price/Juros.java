package price;

public class Juros {

	private ValorTaxa taxa;
	private PeriodicidadeTaxa periodicidade;
	
	public Juros(ValorTaxa taxa, PeriodicidadeTaxa periodicidade) {
		this.setTaxa(taxa);
		this.setPeriodicidade(periodicidade);
	}

	public PeriodicidadeTaxa getPeriodicidade() {
		return periodicidade;
	}

	public void setPeriodicidade(PeriodicidadeTaxa periodicidade) {
		this.periodicidade = periodicidade;
	}

	public void setTaxa(ValorTaxa taxa) {
		this.taxa = taxa;
	}

	public ValorTaxa getTaxa() {
		return this.taxa;
	}

}
