package price;

public class ValorInteiro extends Decimal {
	
	private static int PRECISAO_PADRAO = 0;
	
	public ValorInteiro(Integer valor) {
		super(valor.doubleValue(), PRECISAO_PADRAO);
	}
	
	public ValorInteiro(Decimal decimal) {
		super(decimal.getValor(), PRECISAO_PADRAO);
	}
}
