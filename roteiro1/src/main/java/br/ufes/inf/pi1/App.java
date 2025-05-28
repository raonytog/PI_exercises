package br.ufes.inf.pi1;

import java.io.Reader;
import java.util.List;
import java.io.FileReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class App {
    public static void main(String[] args) throws Exception {
        List<Pais> paises = new java.util.LinkedList<>();
        String ano = "2020";
        String arq = "expec_vida.csv";
        int sex = 1; // 1 para masculino, 2 para feminino
        // Leitura do arquivo CSV
        Reader in = new FileReader(arq);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
        for (CSVRecord record : records) {
            if(record.get(2).equals(ano)){
                String nome = record.get(0);
                Double expc = 0.0;
                if(sex == 1) {
                    expc = Double.parseDouble(record.get(3));
                } else if (sex == 2) {
                   expc = Double.parseDouble(record.get(4));
                }
                
                Pais pais = new Pais(nome, expc);
                paises.add(pais);
            }
        }
        in.close();
        paises.sort(new ComparaPais());
        // Impressão dos países ordenados
        for (Pais pais : paises) {
            System.out.println(pais.getNome() + " - " + pais.getExpecVida());
        }
    }
}
