package calculos;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import price.ValorTaxa;

public class ValoresTaxa {

	@Test
	public void possuemPrecisao4() {
		ValorTaxa taxa = new ValorTaxa(10.1201);
		
		assertEquals(new Double(10.1201), taxa.getValor());
	}
}
