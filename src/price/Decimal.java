package price;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Decimal {

	private final RoundingMode ARREDONDAMENTO_PADRAO = RoundingMode.HALF_EVEN;
	private final Integer PRECISAO_PADRAO = 10; 
	
	private final Double valor;
	private final Integer precisao;

	public Decimal(Double valor) {
		
		this.valor = valor; 
		this.precisao = PRECISAO_PADRAO;
	}
	
	public Decimal(Integer valor) {
		
		this.valor = valor.doubleValue();
		this.precisao = 0;
	}
	
	public Decimal(Double valor, Integer precisao) {
		
		this.valor = valor; 
		this.precisao = precisao;
	}
	
	public Decimal soma(Decimal outroValor) {
		
		Integer precisao = Math.max(this.getPrecisao(), outroValor.getPrecisao());
		Double valorSoma = this.getValor() + outroValor.getValor();
	
		return new Decimal(valorSoma, precisao);
	}
	
	public Decimal subtrai(Decimal outroValor) {
		
		Double valorSubtracao = this.getValor() - outroValor.getValor();
	
		return new Decimal(valorSubtracao);
	}
	
	public Decimal multiplicaPor(Decimal outroValor) {
		
		Double valorSubtracao = this.getValor() * outroValor.getValor();
	
		return new Decimal(valorSubtracao);
	}
	
	public Decimal dividePor(Decimal outroValor) {
		
		Double valorSubtracao = this.getValor() / outroValor.getValor();
	
		return new Decimal(valorSubtracao);
	}
	
	public Decimal potenciaDe(Decimal outroValor) {
		
		Double valorSubtracao = Math.pow(this.getValor(), outroValor.getValor());
	
		return new Decimal(valorSubtracao);
	}
	
	public Double getValor() {
		
		BigDecimal valorComPrecisao = new BigDecimal(this.valor.toString())
				.setScale(this.getPrecisao(), ARREDONDAMENTO_PADRAO);
		
		return valorComPrecisao.doubleValue();
	}

	private Integer getPrecisao() {
		return precisao;
	}
}
