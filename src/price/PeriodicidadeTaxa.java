package price;

public class PeriodicidadeTaxa {

	public static final PeriodicidadeTaxa MENSAL = new PeriodicidadeTaxa(30, "M");
	public static final PeriodicidadeTaxa BIMESTRAL = new PeriodicidadeTaxa(60, "B");
	public static final PeriodicidadeTaxa TRIMESTRAL = new PeriodicidadeTaxa(90, "T");
	public static final PeriodicidadeTaxa ANUAL = new PeriodicidadeTaxa(365, "A");
	public static final PeriodicidadeTaxa ANUAL_CORRIDO = new PeriodicidadeTaxa(360, "C");
	
	private static String ABREVIACAO_DIAS_CORRIDOS = "D";
	
	public static PeriodicidadeTaxa criarPeriodicidadeDiasCorridos(Integer dias) {
		return new PeriodicidadeTaxa(dias, ABREVIACAO_DIAS_CORRIDOS);
	}
	
	public static PeriodicidadeTaxa criarPeriodicidadeDiasCorridos(ValorInteiro diasCorridosParaCalculo) {
		return new PeriodicidadeTaxa(diasCorridosParaCalculo.getValor().intValue(), ABREVIACAO_DIAS_CORRIDOS);
	}
	
	private ValorInteiro dias;
	private String abreviacao;

	private PeriodicidadeTaxa(Integer dias, String abreviacao) {
		this.setDias(new ValorInteiro(dias));
		this.setAbreviacao(abreviacao);
	}
	
	public ValorInteiro getDias() {
		return dias;
	}

	public String getAbreviacao() {
		return abreviacao;
	}

	private void setDias(ValorInteiro dias) {
		this.dias = dias;
	}
	
	private void setAbreviacao(String abreviacao) {
		this.abreviacao = abreviacao;
	}

	
	
}
