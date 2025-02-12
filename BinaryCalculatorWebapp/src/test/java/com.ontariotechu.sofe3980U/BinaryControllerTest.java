package com.ontariotechu.sofe3980U;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.junit.runner.RunWith;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.test.context.junit4.*;

import static org.hamcrest.Matchers.containsString;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BinaryController.class)
public class BinaryControllerTest {

    @Autowired
    private MockMvc mvc;

   
    @Test
    public void getDefault() throws Exception {
        this.mvc.perform(get("/"))//.andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("calculator"))
			.andExpect(model().attribute("operand1", ""))
			.andExpect(model().attribute("operand1Focused", false));
    }
	
	    @Test
    public void getParameter() throws Exception {
        this.mvc.perform(get("/").param("operand1","111"))
            .andExpect(status().isOk())
            .andExpect(view().name("calculator"))
			.andExpect(model().attribute("operand1", "111"))
			.andExpect(model().attribute("operand1Focused", true));
    }
	@Test
	    public void postParameter() throws Exception {
        this.mvc.perform(post("/").param("operand1","111").param("operator","+").param("operand2","111"))//.andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
			.andExpect(model().attribute("result", "1110"))
			.andExpect(model().attribute("operand1", "111"));
    }
    ////////////////////////////////////////////////////////////////////////
    @Test
    public void multiplyTest() throws Exception {
        // Example: 101 (5 in decimal) * 11 (3 in decimal) = 1111 (15 in decimal)
        this.mvc.perform(post("/")
                .param("operand1", "101")
                .param("operator", "*")
                .param("operand2", "11"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "1111"))
            .andExpect(model().attribute("operand1", "101"));
    }
    
    @Test
    public void andTest() throws Exception {
        // Example: 1010 (10) & 1100 (12) should yield 1000 (8)
        this.mvc.perform(post("/")
                .param("operand1", "1010")
                .param("operator", "&")
                .param("operand2", "1100"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "1000"))
            .andExpect(model().attribute("operand1", "1010"));
    }
    
    @Test
    public void implicationTest() throws Exception {
        // For operator I, we'll assume a bitwise implication:
        // For each bit, result = (NOT bit of operand1) OR (bit of operand2).
        // Example for operands "1010" and "1100":
        //  Bit-by-bit: (1 ->0 OR 1 = 1), (0 ->1 OR 1 = 1), (1 ->0 OR 0 = 0), (0 ->1 OR 0 = 1)
        //  Expected result: "1101"
        this.mvc.perform(post("/")
                .param("operand1", "1010")
                .param("operator", "|")
                .param("operand2", "1100"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "1101"))
            .andExpect(model().attribute("operand1", "1010"));
    }
    @Test
    public void implicationAllZerosTest() throws Exception {
        // 0 I 1010: "0" is padded to "0000" (if operand2 is 4 digits "1010") so every bit: 0->1; expected "1111"
        this.mvc.perform(post("/")
                .param("operand1", "0")
                .param("operator", "|")
                .param("operand2", "1010"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "1111"));
    }

    @Test
    public void implicationAllOnesTest() throws Exception {
        // 1111 I 1010: when operand1 is all ones, the result should equal operand2 (i.e. "1010")
        this.mvc.perform(post("/")
                .param("operand1", "1111")
                .param("operator", "|")
                .param("operand2", "1010"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "1010"));
    }

    @Test
    public void implicationDifferentLengthsTest() throws Exception {
        // 101 I 1100: pad "101" to "0101" vs "1100"
        // Calculation: index0: 0->1; index1: 1->1; index2: 0->1; index3: 1->0; result "1110"
        this.mvc.perform(post("/")
                .param("operand1", "101")
                .param("operator", "|")
                .param("operand2", "1100"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "1110"));
    }
    @Test
    public void andWithZeroTest() throws Exception {
        // 1010 & 0 = 0
        this.mvc.perform(post("/")
                .param("operand1", "1010")
                .param("operator", "&")
                .param("operand2", "0"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "0"));
    }

    @Test
    public void andDifferentLengthsTest() throws Exception {
        // 101 (5) & 1100 (12)
        // "101" padded to "0101" and "1100" yields bitwise AND "0101" & "1100" = "0100", then trimmed to "100"
        this.mvc.perform(post("/")
                .param("operand1", "101")
                .param("operator", "&")
                .param("operand2", "1100"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "100"));

    
}

@Test
public void multiplyZeroTest() throws Exception {
    // 0 * 1010 = 0
    this.mvc.perform(post("/")
            .param("operand1", "0")
            .param("operator", "*")
            .param("operand2", "1010"))
        .andExpect(status().isOk())
        .andExpect(view().name("result"))
        .andExpect(model().attribute("result", "0"));
}

@Test
public void multiplyLeadingZerosTest() throws Exception {
    // "00101" * "00011" should be equivalent to "101" * "11" = "1111"
    this.mvc.perform(post("/")
            .param("operand1", "00101")
            .param("operator", "*")
            .param("operand2", "00011"))
        .andExpect(status().isOk())
        .andExpect(view().name("result"))
        .andExpect(model().attribute("result", "1111"));

}


}