package calculos;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import price.Decimal;
import price.ValorMonetario;
import price.ValorTaxa;

public class NumerosDecimais {

	@Test
	public void valorMonetarioTemDoisDigitos() {
		ValorMonetario dezReaisVirgula52 = new ValorMonetario(10.52);
		
		assertTrue(dezReaisVirgula52.getValor().equals(10.52));
	}
	
	@Test
	public void valorTaxaTemQuatroDigitos() {
		ValorTaxa dezVirgula5234 = new ValorTaxa(10.5234);
		
		assertTrue(dezVirgula5234.getValor().equals(10.5234));
	}
	
	@Test
	public void operacaoMantemPrecisao() {
		Decimal umValor = new Decimal(10.43, 2);
		Decimal outroValor = new Decimal(10.21, 2);
		
		Decimal valorSomado = umValor.soma(outroValor);
		
		assertTrue(valorSomado.getValor().equals(20.64));
	}
	
	@Test
	public void operacaoAssumeMaiorPrecisao() {
		Decimal umValor = new Decimal(10.43, 2);
		Decimal outroValor = new Decimal(10.2133, 4);
		
		Decimal valorSomado = umValor.soma(outroValor);
		
		assertTrue(valorSomado.getValor().equals(20.6433));
	}
	
}
