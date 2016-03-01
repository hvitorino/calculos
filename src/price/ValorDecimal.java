package price;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ValorDecimal {

	public static final ValorDecimal UM = new ValorDecimal(1);
	public static final ValorDecimal CEM = new ValorDecimal(100);

	private final RoundingMode ARREDONDAMENTO_PADRAO = RoundingMode.HALF_EVEN;
	private final Integer PRECISAO_PADRAO = 20; 
	
	private final Double valorOperacional;
	private final Integer precisao;

	public ValorDecimal(ValorDecimal decimal) {
		this(decimal.getValorOperacional());
	}

	public ValorDecimal(Double valor) {
		
		this.valorOperacional = valor; 
		this.precisao = PRECISAO_PADRAO;
	}
	
	public ValorDecimal(Integer valor) {
		
		this.valorOperacional = valor.doubleValue();
		this.precisao = 0;
	}
	
	public ValorDecimal(Double valor, Integer precisao) {
		
		this.valorOperacional = valor; 
		this.precisao = precisao;
	}
	
	public ValorDecimal soma(ValorDecimal outroValor) {
		
		Double valorSoma = this.getValorOperacional() + outroValor.getValorOperacional();
	
		return new ValorDecimal(valorSoma);
	}
	
	public ValorDecimal subtrai(ValorDecimal outroValor) {
		
		Double valorSubtracao = this.getValorOperacional() - outroValor.getValorOperacional();
	
		return new ValorDecimal(valorSubtracao);
	}
	
	public ValorDecimal multiplicaPor(ValorDecimal outroValor) {
		
		Double valorSubtracao = this.getValorOperacional() * outroValor.getValorOperacional();
	
		return new ValorDecimal(valorSubtracao);
	}
	
	public ValorDecimal dividePor(ValorDecimal outroValor) {
		
		Double valorSubtracao = this.getValorOperacional() / outroValor.getValorOperacional();
	
		return new ValorDecimal(valorSubtracao);
	}
	
	public ValorDecimal potenciaDe(ValorDecimal outroValor) {
		
		Double valorSubtracao = Math.pow(this.getValorOperacional(), outroValor.getValorOperacional());
	
		return new ValorDecimal(valorSubtracao);
	}
	
	public Double getValor() {
		
		BigDecimal valorComPrecisao = new BigDecimal(this.valorOperacional.toString())
				.setScale(this.getPrecisao(), ARREDONDAMENTO_PADRAO);
		
		return valorComPrecisao.doubleValue();
	}
	
	public ValorMonetario valorMonetario() {
		return new ValorMonetario(this);
	}

	public ValorTaxa valorTaxa() {
		return new ValorTaxa(this);
	}
	
	public ValorTaxa valorInteiro() {
		return new ValorTaxa(this);
	}

	private Double getValorOperacional() {
		return valorOperacional;
	}
	
	private Integer getPrecisao() {
		return precisao;
	}
}
