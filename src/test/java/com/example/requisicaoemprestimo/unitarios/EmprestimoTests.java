package com.example.requisicaoemprestimo.unitarios;

import com.example.requisicaoemprestimo.domain.models.Emprestimo;
import com.example.requisicaoemprestimo.domain.models.Parcela;
import com.example.requisicaoemprestimo.domain.models.ResultadoAnalise;
import com.example.requisicaoemprestimo.domain.models.ResultadoTesouraria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
public class EmprestimoTests {
    private final EmprestimoTestsFixture fixture = new EmprestimoTestsFixture();
    private Emprestimo emprestimo;

    @BeforeEach
    public void Setup()
    {
        emprestimo = fixture.emprestimoAprovado( 100.00, 12);
    }

    @Test
    public void testParcelas(){
        Optional<Parcela[]> parcelas = emprestimo.getParcelas();

        assertFalse(parcelas.isEmpty());
        ResultadoTesouraria resultadoTesouraria = new ResultadoTesouraria();
        emprestimo.setResultadoTesouraria(resultadoTesouraria);
        Optional<Parcela[]> parcelasCalculadas =  emprestimo.getParcelas();
        double sum = 0.0;
        for(Parcela p : parcelasCalculadas.get()) {
            sum += p.getValorDaParcela();
        }
        
        assertEquals(106.50, Math.round(sum * 100.0) / 100.0);
        assertEquals(12, emprestimo.getQuantidadeParcelasSolicitadas());
    }

    @Test
    public void testeAnaliseDeCreditoInvalida(){
        ResultadoAnalise resultadoAnalise = new ResultadoAnalise();
        emprestimo.setResultadoAnalise(resultadoAnalise);

        ResultadoAnalise result = emprestimo.getResultadoAnalise();
        assertFalse(result.isAprovado());
    }

    @Test
    public void testeResultadoDaTesourariaInvalida(){
        ResultadoTesouraria resultadoTesouraria = new ResultadoTesouraria();
        emprestimo.setResultadoTesouraria(resultadoTesouraria);

        ResultadoTesouraria result = emprestimo.getResultadoTesouraria();
        assertFalse(result.isAprovado());
    }
}