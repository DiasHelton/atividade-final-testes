package com.example.requisicaoemprestimo.unitarios;

import com.example.requisicaoemprestimo.domain.models.Emprestimo;
import com.example.requisicaoemprestimo.domain.models.ResultadoAnalise;
import com.example.requisicaoemprestimo.domain.models.ResultadoTesouraria;

import java.util.UUID;

/**
 * Classe Fixture para auxiliar nos testes
 * QUANDO NECESSÁRIO FAÇA CUSTOMIZAÇÕES AQUI
 * */
public class EmprestimoTestsFixture {
    public static Emprestimo emprestimoAprovado(double valor, int quantidadeParcelas) {
        var emprestimo = new Emprestimo(UUID.randomUUID(), valor, quantidadeParcelas);

        var analiseCredito = buildResultadoAnalise(true, new String[]{"OK"});
        var tesouraria = buildResultadoTesouraria(true, "OK", emprestimo);

        emprestimo.setResultadoAnalise(analiseCredito);
        emprestimo.setResultadoTesouraria(tesouraria);
        return emprestimo;
    }

    public static ResultadoAnalise buildResultadoAnalise(boolean aprovado, String[] resultado){
        var analiseCredito = new ResultadoAnalise(aprovado, resultado);
        return analiseCredito;
    }

    public static ResultadoTesouraria buildResultadoTesouraria(boolean aprovado, String motivo, Emprestimo emprestimo){
        var tesouraria = new ResultadoTesouraria(aprovado, motivo);
        tesouraria.setTaxaJurosMensal(100);
        tesouraria.setQuantidadeParcelas(emprestimo.getQuantidadeParcelasSolicitadas());
        tesouraria.setValorAprovado(emprestimo.getValorSolicitado());
        return tesouraria;
    }

    public static Emprestimo emprestimoAnaliseInvalida(double valor, int quantidadeParcelas){
        var emprestimo = new Emprestimo(UUID.randomUUID(), valor, quantidadeParcelas);
        
        var analiseCredito = buildResultadoAnalise(false, new String[]{"NOT OK"});
        var tesouraria = buildResultadoTesouraria(true, "OK", emprestimo);

        emprestimo.setResultadoAnalise(analiseCredito);
        emprestimo.setResultadoTesouraria(tesouraria);
        return emprestimo;
    }

    public static Emprestimo emprestimoTesourariaInvalida(double valor, int quantidadeParcelas){
        var emprestimo = new Emprestimo(UUID.randomUUID(), valor, quantidadeParcelas);
        
        var analiseCredito = buildResultadoAnalise(true, new String[]{"OK"});
        var tesouraria = buildResultadoTesouraria(false, "NOT OK", emprestimo);

        emprestimo.setResultadoAnalise(analiseCredito);
        emprestimo.setResultadoTesouraria(tesouraria);
        return emprestimo;
    }
}