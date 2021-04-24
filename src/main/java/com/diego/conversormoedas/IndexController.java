package com.diego.conversormoedas;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Controller
public class IndexController {
    double real;
    double dolar;
    double cotacao = 5;

    @GetMapping
    public String index(Model model){
        RestTemplate rest = new RestTemplate();

        List<Map> resp = (List<Map>) rest.getForObject("https://economia.awesomeapi.com.br/USD-BRL/", List.class);

        Map map = resp.get(0);
        String bid = (String)map.get("bid");
        cotacao = Double.valueOf(bid);
        model.addAttribute("real", real);
        model.addAttribute("cotacao", cotacao);
        model.addAttribute( "dolar", dolar);
        model.addAttribute("formularioA",new FormularioA());
        model.addAttribute("formularioB",new FormularioB());
        return "index";
    }

    @PostMapping("/converter")
    public String converter(FormularioA formularioA){
        int d = Integer.valueOf((int) formularioA.valor1);
        dolar = d * cotacao;
        return "redirect:/";
    }

    @PostMapping("/converter2")
    public String converter2 (FormularioB formularioB){
        int r = Integer.valueOf((int) formularioB.valor2);
        real = (r / cotacao);
        return "redirect:/";
    }
}
