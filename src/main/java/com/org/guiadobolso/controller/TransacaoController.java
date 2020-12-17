package com.org.guiadobolso.controller;

import com.org.guiadobolso.model.Transacao;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TransacaoController {

    private int numTransacoes;
    private ArrayList<Transacao> transacaoList;
    private int id;
    private int ano;
    private int mes;


    // Realiza as validações do que o usuario digitou, se esta de acordo com as regras do contrato. Senao, envia erro para a view.
    public int getId() {
        return id;
    }
    public void setId(int id) throws IllegalArgumentException {
        if(id >= 1000 && id <= 100000) {
            this.id = id;
        }else{
            throw new IllegalArgumentException("O ID deve estar entre 1.000 e 100.000");
        }

    }
    public int getAno() {
        return ano;
    }
    public void setAno(int ano) throws IllegalArgumentException {
        if(ano >= 1900 && ano <= 2099) {
            this.ano = ano;
        }else{
            throw new IllegalArgumentException("O ano deve estar entre 1900 e 2099.");
        }
    }
    public int getMes() {
        return mes;
    }
    public void setMes(int mes) throws IllegalArgumentException {
        if(mes >= 1 && mes <= 12) {
            this.mes = mes;
        }else{
            throw new IllegalArgumentException("O mes deve estar entre 1 e 12.");
        }

    }
    public List<Transacao> getTransacaoList() {
        return transacaoList;
    }

    public void setTransacaoList() throws ParseException {
        //Vai iniciar o array de transacao
        transacaoList = new ArrayList();

        for (int i = 0; i < this.numTransacoes; i++) {
            String descricao = gerarDescricaoAleatoria();

            long data = gerarDataAleatoria();

            int valor = gerarValor(i);

            Transacao minhaTransacao = new Transacao();
            minhaTransacao.setData(data);
            minhaTransacao.setDescricao(descricao);
            minhaTransacao.setIndice(i);
            minhaTransacao.setValor(valor);

            transacaoList.add(minhaTransacao);
        }

    }




    // Define o número de transações que vai exibir, com base no mes e no primeiro id do usuario
    public void setNumeroTransacoes(){

        double e = Math.log10(Math.abs((long) this.id));
        int vlr = Double.valueOf(Math.pow(10.0, e - Math.floor(e))).intValue();
        this.numTransacoes = this.mes * vlr;
    }

    public int getNumeroTransacoes() {
        return this.numTransacoes;
    }

        //  Gera descricao aleatoria, alternando entre vogal e consoante e espaços, afim de gerar uma descrição legível
        private String gerarDescricaoAleatoria(){
            Random r = new Random();
            // aleatorio entre 10 e 60 caracteres
            int numStrings = r.ints(10, (60 + 1)).limit(1).findFirst().getAsInt();

            String vogais[] = {"a","e","i","o","u"};
            String consoantes[] = {"b","c","d","f","g","j","l","m","n","p","q"," "};
            String palavra = "";
            boolean isVogal = true;
            for (int i = 0; i < numStrings; i++) {
                if(isVogal){
                    int pos = r.nextInt(vogais.length - 0);
                    palavra = palavra +(vogais[pos]);
                    isVogal = false;

                }else{
                    int pos = r.nextInt(consoantes.length - 0);
                    palavra = palavra +(consoantes[pos]);
                    isVogal = true;
                }

            }

            return palavra;
        }

    //    Função pega o ano e o mes, e gera um dia aleatorio dentro desse range
    private long gerarDataAleatoria() throws ParseException {
        long timestamp = 0;

        LocalDate dataInicial = LocalDate.of(this.ano, this.mes, 1); //data inicial
        long start = dataInicial.toEpochDay();

        int maxDay = 30;
        // se o mes for fevereiro, o dia vai ate 28
        if(this.mes == 2){
            maxDay = 28;
        }
        LocalDate dataFinal = LocalDate.of(this.ano, this.mes, maxDay); //data final
        long end = dataFinal.toEpochDay();


        long dataAleatoria = ThreadLocalRandom.current().longs(start, end).findAny().getAsLong();


            timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.ofEpochDay(dataAleatoria).toString()).getTime();


        return timestamp;
    }


    public Integer gerarValor(int indice){
        int valor = this.id * (indice + 1) + (this.id - this.mes);

        if(valor <= -9999999 || valor >= 9999999){
            valor = this.id - indice * this.mes;
        }

        return valor;
    }
}
