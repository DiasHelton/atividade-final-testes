package com.example.requisicaoemprestimo.domain.models;

import java.util.Optional;
import java.util.UUID;

public class Emprestimo {
    private final String codigo;
    private final String usuario;
    private final double valorSolicitado;
    private double valorTotalEmprestimo;
    private final int quantidadeParcelasSolicitadas;
    private ResultadoAnalise resultadoAnalise;
    private ResultadoTesouraria resultadoTesouraria;
    private ResultadoRequisicao resultadoRequisicao;
    private Parcela[] parcelas;

    public Emprestimo(UUID usuarioUuid, double valor, int quantidadeParcelas){
        this.codigo = UUID.randomUUID().toString();
        this.usuario = usuarioUuid.toString();
        this.valorSolicitado = valor;
        this.quantidadeParcelasSolicitadas = quantidadeParcelas;
        this.setResultadoRequisicao(new ResultadoRequisicao());
        this.setResultadoAnalise(new ResultadoAnalise());
        this.setResultadoTesouraria(new ResultadoTesouraria());
        this.parcelas = new Parcela[]{};
    }

    public boolean isEmprestimoFoiAprovado(){
        return this.getResultadoRequisicao().isAprovado() &&
                this.getResultadoTesouraria().isAprovado() &&
                this.getResultadoAnalise().isAprovado();
    }

    public Optional<Parcela[]> getParcelas(){
        return this.parcelas.length > 0 ? Optional.of(this.parcelas) : Optional.empty();
    }

    public double getValorTotalEmprestimo() {
        return Parcela.round(this.valorTotalEmprestimo, 2);
    }

    public int getQuantidadeParcelasSolicitadas() {
        return quantidadeParcelasSolicitadas;
    }

    public String getCodigo() {
        return codigo;
    }

    public ResultadoAnalise getResultadoAnalise() {
        return resultadoAnalise;
    }

    public void setResultadoAnalise(ResultadoAnalise resultadoAnalise) {
        this.resultadoAnalise = resultadoAnalise;
    }

    public ResultadoTesouraria getResultadoTesouraria() {
        return resultadoTesouraria;
    }

    public void setResultadoTesouraria(ResultadoTesouraria resultadoTesouraria) {
        this.resultadoTesouraria = resultadoTesouraria;

        var parcelasCalculadas = calcularParcelas();
        if (parcelasCalculadas.isPresent())
            this.parcelas = parcelasCalculadas.get();
    }

    private Optional<Parcela[]> calcularParcelas(){
        if (!isEmprestimoFoiAprovado()) return Optional.empty();

        var parcelas = new Parcela[getResultadoTesouraria().getQuantidadeParcelas()];
        var saldoAnterior = getResultadoTesouraria().getValorAprovado();

        for (var i = 0; i < getResultadoTesouraria().getQuantidadeParcelas(); i++){
            var parcela = new Parcela(saldoAnterior, getResultadoTesouraria());

            this.valorTotalEmprestimo += parcela.getValorDaParcela();
            saldoAnterior = parcela.getSaldoResidual();
            parcelas[i] = parcela;
        }

        return Optional.of(parcelas);
    }

    public String getUsuario() {
        return usuario;
    }

    public double getValorSolicitado() {
        return this.valorSolicitado;
    }

    public ResultadoRequisicao getResultadoRequisicao() {
        return this.resultadoRequisicao;
    }

    public void setResultadoRequisicao(ResultadoRequisicao resultadoRequisicao) {
        this.resultadoRequisicao = resultadoRequisicao;
    }
}