package com.org.guiadobolso;

import com.org.guiadobolso.controller.TransacaoController;
import com.org.guiadobolso.model.Transacao;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/*")
public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private TransacaoController transacaoController;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String uri[] = request.getRequestURI().split("/");

            /*
            [0] / do diretorio root
            [1] path do contexto da aplicação
            [2] é o id
            [3] é a palavra 'transacao'
            [4] é o ano
            [5] é o mês
             */

            String idRequest = uri[2];
            String anoRequest = uri[4];
            String mesRequest = uri[5];

            // verifica se esta explicitamente escrito transacoes, conforme contrato definido, após o id informado
            if(!uri[3].equals("transacoes")){
                throw new IllegalArgumentException("Digite apenas os campos no formato /<id>/transacoes/<ano>/<mes>.");
            }
            // verifica se não existe argumentos demais
            if(uri.length > 6){
                throw new IllegalArgumentException("Digite apenas os campos no formato /<id>/transacoes/<ano>/<mes>.");
            }

            /*
            Pode-se também utilizar atraves de parametros (?&id=X&ano=Y&mes=Z)
             */

            //String idRequest = request.getParameter("id");
            int id = Integer.parseInt(idRequest);

           // String anoRequest = request.getParameter("ano");
            int ano = Integer.parseInt(anoRequest);

           // String mesRequest = request.getParameter("mes");
            int mes = Integer.parseInt(mesRequest);


            // responsável por manipular as transacoes
            criarController(id,mes,ano);

            // monta a resposta
            exibirDados(response);


        }catch (NumberFormatException e){
            // verificação se todos os inputs foram digitados pelo usuario
            response.sendError(response.SC_BAD_REQUEST, "Campos obrigatorios nao incluidos.");
        }catch (IllegalArgumentException e){
            // verificação se os inputs digitados estão em conformidade
            response.sendError(response.SC_BAD_REQUEST, e.getMessage());
        }catch (ParseException e){
            response.sendError(response.SC_BAD_REQUEST, "Erro na conversao da data.");
        }catch (IOException e){
            //erro no servlet (PrintWriter)
            response.sendError(response.SC_INTERNAL_SERVER_ERROR, "Erro na exibição dos dados");
        }catch (Exception e){
        response.sendError(response.getStatus(), "Erro no processamento dos campos.");
    }
    }

    private void criarController(int id, int mes, int ano) throws ParseException {
        transacaoController = new TransacaoController();

        transacaoController.setId(id);
        transacaoController.setMes(mes);
        transacaoController.setAno(ano);
        transacaoController.setNumeroTransacoes();

        transacaoController.setTransacaoList();
    }


    public void exibirDados(HttpServletResponse response) throws IOException {

        response.setContentType("application/json");

        PrintWriter out = response.getWriter();


        out.println("[");
        // iterar sobre o array de transação para exibir os dados na tela.
        for (int i = 0; i < transacaoController.getNumeroTransacoes(); i++) {
            out.println("{");

            out.println("descricao: " +transacaoController.getTransacaoList().get(i).getDescricao());
            out.println("data: " +transacaoController.getTransacaoList().get(i).getData());
            out.println("valor: " +transacaoController.getTransacaoList().get(i).getValor());

            out.println("}");

            // acrescenta virgula apos cada transacao, exceto se for o último da lista
            if(transacaoController.getNumeroTransacoes() > 0 && i != transacaoController.getNumeroTransacoes()-1)  {
                out.print(",");

            }

        }
        out.println("]");

        out.flush();
    }



}
