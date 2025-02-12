package com.ontariotechu.sofe3980U;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BinaryController {

    @GetMapping("/")
    public String calculator(@RequestParam(name="operand1", required=false, defaultValue="") String operand1, 
                             Model model) {
        model.addAttribute("operand1", operand1);
        model.addAttribute("operand1Focused", !operand1.isEmpty());
        return "calculator";
    }

    @PostMapping("/")
    public String calculate(@RequestParam(name="operand1", required=false, defaultValue="") String operand1,
                            @RequestParam(name="operator", required=false, defaultValue="") String operator,
                            @RequestParam(name="operand2", required=false, defaultValue="") String operand2,
                            Model model) {
        Binary num1 = new Binary(operand1);
        Binary num2 = new Binary(operand2);
        Binary result = null;
        
        try {
            switch(operator) {
                case "+":
                    result = Binary.add(num1, num2);
                    break;
                case "*":
                    result = Binary.multiply(num1, num2);
                    break;
                case "&":
                    result = Binary.and(num1, num2);
                    break;
                case "|":
                    result = Binary.implication(num1, num2);
                    break;
                default:
                    model.addAttribute("error", "Invalid operator: " + operator);
                    return "error";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        
        model.addAttribute("operand1", operand1);
        model.addAttribute("operator", operator);
        model.addAttribute("operand2", operand2);
        model.addAttribute("result", result.getValue());
        return "result";
    }
}
