package calculos;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import price.ValorDecimal;
import price.ValorMonetario;

public class ValoresDecimais {

	@Test
	public void possuemPrecisao10() {
		ValorDecimal precisao = new ValorDecimal(0.0987654321);
		
		assertEquals(new Double(0.0987654321), precisao.getValor());
	}
	
	@Test
	public void valoresParesSaoArredondadosParaBaixo() {
		ValorMonetario valor = new ValorMonetario(10.125);
		
		assertEquals(new Double(10.12), valor.getValor());
	}
	
	@Test
	public void valoresImparesSaoArredondadosPraCima() {
		ValorMonetario valor = new ValorMonetario(10.135);
		
		assertEquals(new Double(10.14), valor.getValor());
	}
	
}
