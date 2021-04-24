package com.diego.conversormoedas;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;


@Controller
public class IndexController {

    private String mensagem = "";


    @GetMapping("/")
    public String index(Model model) {
        Elementos elementos = new Elementos();
        model.addAttribute("elementos", elementos);
        model.addAttribute("mensagem", mensagem);
        return "index";
    }

    @PostMapping("converter")
    public String converter(Elementos elementos) {

        RestTemplate rest = new RestTemplate();

        ResponseEntity<CambioMoeda[]> response = rest.getForEntity("https://economia.awesomeapi.com.br/USD-BRL/1", CambioMoeda[].class);

        CambioMoeda[] cambios = response.getBody();

        if (cambios.length == 0) {
            mensagem = "Não existe nenhuma cotação";
            return "redirect:/";
        }

        CambioMoeda cambio = cambios[0];

        Double cotacao = Double.valueOf(cambio.getBid());

        String Valor = elementos.getMoedaA();
        Double novoValor = elementos.getMoedaB();

        if (Valor.equalsIgnoreCase("USD-BRL")) {
            Double valorConvertido = novoValor * cotacao;
            mensagem = formatar(novoValor) + " dólares  é " + formatar(valorConvertido) +" em reais";
        }
        else {
            Double valorConvertido = novoValor / cotacao;
            mensagem = formatar(novoValor) + " reais  é  " + formatar(valorConvertido) + " em dólares";
        }
        return "redirect:/";

    }

    private String formatar(Double numero) {
        return String.format("%.2f", numero);
    }


}
