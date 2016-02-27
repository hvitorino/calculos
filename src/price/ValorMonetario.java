package price;

public class ValorMonetario extends ValorDecimal {

	private static int PRECISAO_PADRAO = 2;
	
	public ValorMonetario(Double valor) {
		super(valor, PRECISAO_PADRAO);
	}
	
	public ValorMonetario(Double valor, Integer precisao) {
		super(valor, precisao);
	}
	
	public ValorMonetario(ValorDecimal decimal) {
		super(decimal.getValor(), PRECISAO_PADRAO);
	}
	
}
