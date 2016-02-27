package calculos;

import static org.junit.Assert.*;

import org.junit.Test;

import price.ValorMonetario;

public class ValoresMonetarios {

	@Test
	public void possuemPrecisao2() {
		ValorMonetario valor = new ValorMonetario(10.1201);
		
		assertEquals(new Double(10.12), valor.getValor());
	}
}
