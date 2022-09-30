package com.example.requisicaoemprestimo.unitarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.requisicaoemprestimo.domain.models.Emprestimo;

public class EmprestimoTests {

    @Autowired
    EmprestimoTestsFixture fixture;

    @Test
    public void testParcelas(){
        Emprestimo emprestimo = fixture.emprestimoAprovado(100, 12);

        assertTrue(emprestimo.getParcelas().isPresent());
        assertEquals(106.50, emprestimo.getValorTotalEmprestimo());
        assertEquals(12, emprestimo.getQuantidadeParcelasSolicitadas());
    }

    @Test
    public void testeAnaliseDeCreditoInvalida(){
        Emprestimo emprestimo = fixture.emprestimoAnaliseInvalida(100, 12);
        
        assertEquals(false, emprestimo.isEmprestimoFoiAprovado());
    }

    @Test
    public void testeResultadoDaTesourariaInvalida(){
        Emprestimo emprestimo = fixture.emprestimoTesourariaInvalida(100, 12);

        assertEquals(false, emprestimo.isEmprestimoFoiAprovado());
    }

}