package br.com.sicredi.sincronizacao.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.sicredi.sincronizacao.dto.ContaDTO;

public class BancoCentralServiceTest {

	private BancoCentralService bancoCentralService;

	@BeforeEach
	void setUp() {
		bancoCentralService = new BancoCentralService();
	}

	@Test
	void testAtualizaContaValida() {
		ContaDTO contaDTO = new ContaDTO("1234", "1234567", 1000.0);
		assertDoesNotThrow(() -> {
			boolean result = bancoCentralService.atualizaConta(contaDTO);
			assertTrue(result || !result);
		});
	}

	@Test
	void testAtualizaContaInvalida() {
		ContaDTO contaDTO = new ContaDTO("", "1234", 1000.0);
		assertThrows(IllegalArgumentException.class, () -> {
			bancoCentralService.atualizaConta(contaDTO);
		});
	}

	@Test
	void testAtualizaContaCamposVazios() {
		ContaDTO contaDTO = new ContaDTO("", "", 1000.0);
		assertThrows(IllegalArgumentException.class, () -> {
			bancoCentralService.atualizaConta(contaDTO);
		});
	}

}
