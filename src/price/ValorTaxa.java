package price;

public class ValorTaxa extends Decimal {

	private static int PRECISAO_PADRAO = 4;
	
	public ValorTaxa(Double valor) {
		super(valor, PRECISAO_PADRAO);
	}
	
	public ValorTaxa(Double valor, Integer precisao) {
		super(valor, precisao);
	}
	
}
