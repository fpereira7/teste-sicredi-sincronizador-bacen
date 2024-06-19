package br.com.sicredi.sincronizacao.service;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import br.com.sicredi.sincronizacao.dto.ContaDTO;
import br.com.sicredi.sincronizacao.dto.ContaResponseDTO;
import br.com.sicredi.sincronizacao.timer.MeasuredExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SincronizacaoService {

	final Logger log = org.slf4j.LoggerFactory.getLogger(SincronizacaoService.class);
	
	@Autowired
	private BancoCentralService bancoCentralService;

	@MeasuredExecutionTime
	public void syncAccounts(String nomeArquivo)
			throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

		try (Reader reader = Files.newBufferedReader(Paths.get(nomeArquivo))) {

			List<ContaDTO> contas = lerCsv(reader);
			List<ContaResponseDTO> contasProcessadas = processarContas(contas);

			gerarCsv(nomeArquivo, contasProcessadas);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	List<ContaResponseDTO> processarContas(List<ContaDTO> accounts) {
		List<ContaResponseDTO> contasAtualizadas = new ArrayList<>();

		log.info("Processando dados...");

		for (ContaDTO account : accounts) {
			validarCampos(account);

			boolean isUpdated = bancoCentralService.atualizaConta(account);
			ContaResponseDTO contaResponseDTO = new ContaResponseDTO(account.getAgencia(), account.getConta(),
					account.getSaldo(), isUpdated);

			contasAtualizadas.add(contaResponseDTO);
		}
		
		log.info("Dados processados com sucesso.");

		return contasAtualizadas;
	}

	private void validarCampos(ContaDTO conta) {
		if (conta.getAgencia() == null) {
			conta.setAgencia("erro-agencia");
		}

		if (conta.getConta() == null) {
			conta.setConta("erro-conta");
		}
	}

	List<ContaDTO> lerCsv(Reader reader) {
		CsvToBean<ContaDTO> csvToBean = new CsvToBeanBuilder<ContaDTO>(reader).withType(ContaDTO.class)
				.withIgnoreLeadingWhiteSpace(true).withThrowExceptions(false).build();

		List<ContaDTO> contas = csvToBean.parse();
		return contas;
	}

	void gerarCsv(String nomeArquivoOriginal, List<ContaResponseDTO> accounts) {
		String nomeArquivoGerado = nomeArquivoOriginal.replaceAll(".csv", "") + "_response.csv";

		try (Writer writer = Files.newBufferedWriter(Paths.get(nomeArquivoGerado))) {

			ColumnPositionMappingStrategy<ContaResponseDTO> strategy = new ColumnPositionMappingStrategy<>();
			strategy.setType(ContaResponseDTO.class);
			String[] columns = new String[] { "agencia", "conta", "saldo", "status" };
			strategy.setColumnMapping(columns);

			// Construção do StatefulBeanToCsv usando o Writer
			StatefulBeanToCsv<ContaResponseDTO> beanToCsv = new StatefulBeanToCsvBuilder<ContaResponseDTO>(writer)
					.withMappingStrategy(strategy).withQuotechar(CSVWriter.NO_QUOTE_CHARACTER) 
					.build();

			// cabeçalho do arquivo csv gerado
			writer.write("agencia,conta,saldo,status\n");

			beanToCsv.write(accounts);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
