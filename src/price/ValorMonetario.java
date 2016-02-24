package price;

public class ValorMonetario extends Decimal {

	private static int PRECISAO_PADRAO = 2;
	
	public ValorMonetario(Double valor) {
		super(valor, PRECISAO_PADRAO);
	}
	
	public ValorMonetario(Double valor, Integer precisao) {
		super(valor, precisao);
	}
	
}
