package com.example.requisicaoemprestimo.domain.usecases;

import com.example.requisicaoemprestimo.domain.models.Emprestimo;
import com.example.requisicaoemprestimo.domain.models.ResultadoAnalise;
import com.example.requisicaoemprestimo.domain.models.ResultadoRequisicao;
import com.example.requisicaoemprestimo.domain.ports.IAnaliseProxy;
import com.example.requisicaoemprestimo.domain.ports.IEmprestimoRepository;
import com.example.requisicaoemprestimo.domain.ports.ITesourariaProxy;
import com.example.requisicaoemprestimo.infrastructure.repositories.EmprestimoRepository;
import com.example.requisicaoemprestimo.infrastructure.repositories.RepositoryFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RequisicaoEmprestimoUseCase {

    private IEmprestimoRepository repository;
    private IAnaliseProxy analiseProxy;
    private ITesourariaProxy tesourariaProxy;

    public RequisicaoEmprestimoUseCase(IAnaliseProxy analiseProxy, ITesourariaProxy tesourariaProxy) {
        this.repository = RepositoryFactory.getInstance();
        this.analiseProxy = analiseProxy;
        this.tesourariaProxy = tesourariaProxy;
    }

    public Emprestimo executar(UUID usuarioUuid, double valor, int quantidadeParcelas){
        var emprestimo = new Emprestimo(usuarioUuid, valor, quantidadeParcelas);
        var request = validarEmprestimo(emprestimo);

        if (!request.isAprovado()){
            emprestimo.setResultadoRequisicao(request);
            return emprestimo;
        }

        try {
            this.repository.add(emprestimo);

            var resultadoAnalise = this.analiseProxy.solicitarAnaliseDeCredito(emprestimo);
            emprestimo.setResultadoAnalise(resultadoAnalise);
            this.repository.update(emprestimo);

            var resultadoTesouraria = this.tesourariaProxy.solicitarLiberacaoDaTesouraria(emprestimo);
            emprestimo.setResultadoTesouraria(resultadoTesouraria);
            this.repository.update(emprestimo);

            return emprestimo;
        }catch (Exception ex){
            emprestimo.setResultadoRequisicao(
                    new ResultadoRequisicao(false, new String[]{"FAILS", ex.getMessage()}));

            return emprestimo;
        }
    }

    private ResultadoRequisicao validarEmprestimo(Emprestimo emprestimo){
        var errors = new ArrayList<String>();

        if (emprestimo.getUsuario() == "")
            errors.add("CÓDIGO DO CLIENTE NÃO INFORMADO");

        if (emprestimo.getValorSolicitado() < 0)
            errors.add("VALOR NÃO INFORMADO");

        if (emprestimo.getQuantidadeParcelasSolicitadas() < 0)
            errors.add("QUANTIDADE PARCELAS NÃO INFORMADA");

        if (errors.size() > 0) {
            var resultado = new ResultadoRequisicao(false, (String[]) errors.toArray());
            return resultado;
        }
        return new ResultadoRequisicao();
    }
}