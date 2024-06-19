package br.com.sicredi.sincronizacao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.sicredi.sincronizacao.dto.ContaDTO;
import br.com.sicredi.sincronizacao.dto.ContaResponseDTO;

@SpringBootTest
@ActiveProfiles("test")
public class SincronizacaoServiceTest {

	private SincronizacaoService sincronizacaoService;
	
	@BeforeEach
	void setUp() {
		sincronizacaoService = new SincronizacaoService();
	}

	@Test
	void testGerarCsv() throws IOException {
		String nomeArquivoOriginal = "test.csv";
		String nomeArquivoGerado = nomeArquivoOriginal.replaceAll(".csv", "") + "_response.csv";

		// Dados para o arquivo csv
		List<ContaResponseDTO> contas = Arrays.asList(new ContaResponseDTO("9444", "21382-2", 880.2, true),
				new ContaResponseDTO("8989", "35960-6", 617.71, true));

		sincronizacaoService.gerarCsv(nomeArquivoOriginal, contas);

		try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivoGerado))) {
			// Verifica cabeçalho
			String cabecalho = br.readLine();
			assertEquals("agencia,conta,saldo,status", cabecalho);

			// Verifica a segunda linha - primeiro registro
			String linha1 = br.readLine();
			assertEquals("9444,21382-2,880.2,true", linha1);

			// Verifica a terceira linha - segundo registro
			String linha2 = br.readLine();
			assertEquals("8989,35960-6,617.71,true", linha2);

			// Verifica se não há mais linhas
			assertNull(br.readLine());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Erro ao ler o arquivo gerado");
		} finally {
			// Limpa o arquivo gerado após o teste
			Files.deleteIfExists(Paths.get(nomeArquivoGerado));
		}
	}

	@Test
    void testLerCsv() throws IOException {
        String csvContent = "agencia,conta,saldo\n9444,21382-2,880.2\n8989,35960-6,617.71";
        
        try (Reader reader = new StringReader(csvContent)) {
            List<ContaDTO> contas = sincronizacaoService.lerCsv(reader);
            assertEquals(2, contas.size());
            assertEquals("9444", contas.get(0).getAgencia());
            assertEquals("8989", contas.get(1).getAgencia());
        }
    }

}
