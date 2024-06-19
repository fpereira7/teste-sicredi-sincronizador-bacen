package br.com.sicredi.sincronizacao;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import br.com.sicredi.sincronizacao.service.SincronizacaoService;

@SpringBootApplication
public class SincronizadorBacen implements CommandLineRunner{

	@Autowired
	private SincronizacaoService sincronizacaoService;

	public static void main(String[] args) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
		SpringApplication.run(SincronizadorBacen.class, args);

	}

	@Override
    public void run(String... args) throws Exception {
		String fileName = args.length > 0 ? args[0] : "src/main/resources/teste.csv";
		sincronizacaoService.syncAccounts(fileName);
    }
	
	
}
