package price;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ValorDecimal {

	public static final ValorDecimal UM = new ValorDecimal(1);
	public static final ValorDecimal CEM = new ValorDecimal(100);

	private final RoundingMode ARREDONDAMENTO_PADRAO = RoundingMode.HALF_EVEN;
	private final Integer PRECISAO_PADRAO = 10; 
	
	private final Double valor;
	private final Integer precisao;

	public ValorDecimal(ValorDecimal decimal) {
		this(decimal.getValor());
	}

	public ValorDecimal(Double valor) {
		
		this.valor = valor; 
		this.precisao = PRECISAO_PADRAO;
	}
	
	public ValorDecimal(Integer valor) {
		
		this.valor = valor.doubleValue();
		this.precisao = 0;
	}
	
	public ValorDecimal(Double valor, Integer precisao) {
		
		this.valor = valor; 
		this.precisao = precisao;
	}
	
	public ValorDecimal soma(ValorDecimal outroValor) {
		
		Double valorSoma = this.getValor() + outroValor.getValor();
	
		return new ValorDecimal(valorSoma);
	}
	
	public ValorDecimal subtrai(ValorDecimal outroValor) {
		
		Double valorSubtracao = this.getValor() - outroValor.getValor();
	
		return new ValorDecimal(valorSubtracao);
	}
	
	public ValorDecimal multiplicaPor(ValorDecimal outroValor) {
		
		Double valorSubtracao = this.getValor() * outroValor.getValor();
	
		return new ValorDecimal(valorSubtracao);
	}
	
	public ValorDecimal dividePor(ValorDecimal outroValor) {
		
		Double valorSubtracao = this.getValor() / outroValor.getValor();
	
		return new ValorDecimal(valorSubtracao);
	}
	
	public ValorDecimal potenciaDe(ValorDecimal outroValor) {
		
		Double valorSubtracao = Math.pow(this.getValor(), outroValor.getValor());
	
		return new ValorDecimal(valorSubtracao);
	}
	
	public Double getValor() {
		
		BigDecimal valorComPrecisao = new BigDecimal(this.valor.toString())
				.setScale(this.getPrecisao(), ARREDONDAMENTO_PADRAO);
		
		return valorComPrecisao.doubleValue();
	}

	private Integer getPrecisao() {
		return precisao;
	}

	public ValorMonetario valorMonetario() {
		return new ValorMonetario(this);
	}

	public ValorTaxa valorTaxa() {
		return new ValorTaxa(this);
	}
}
