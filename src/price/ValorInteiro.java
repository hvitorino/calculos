package price;

public class ValorInteiro extends Decimal {
	
	private static int PRECISAO_PADRAO = 2;
	
	public ValorInteiro(Integer valor) {
		super(valor.doubleValue(), PRECISAO_PADRAO);
	}
}
