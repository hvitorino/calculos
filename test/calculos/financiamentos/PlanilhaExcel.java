package calculos.financiamentos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;

import price.Financiamento;
import price.Parcela;

public class PlanilhaExcel {
	
	private String nomePlanilha;
	private Financiamento financiamento;

	public PlanilhaExcel(String nomePlanilha, Financiamento financiamento) {
		this.nomePlanilha = nomePlanilha;
		this.financiamento = financiamento;
	}
	
	public void salvar() {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			File file = new File("c:\\financiamentos\\" + nomePlanilha + ".csv");

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(Parcela parcela : financiamento.getParcelas()) {
				String content = new StringBuilder()
						.append(parcela.getNumero().getValor() + ";")
						.append(formatter.format(parcela.getDataVencimento()) + ";")
						.append(parcela.getPrazoEmDias().getValor() + ";")
						.append(parcela.getValor().getValor() + ";")
						.append(parcela.getValorPrincipal().getValor() + ";")
						.append(parcela.getValorJuros().getValor() + "\r\n")
						.toString();
				
				bw.write(content);
			}
			
			bw.close();
			System.out.println("Done");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}