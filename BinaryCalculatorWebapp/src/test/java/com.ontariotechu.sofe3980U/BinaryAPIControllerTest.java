package com.ontariotechu.sofe3980U;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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
@WebMvcTest(BinaryAPIController.class)
public class BinaryAPIControllerTest {

    @Autowired
    private MockMvc mvc;

   
    @Test
    public void add() throws Exception {
        this.mvc.perform(get("/add").param("operand1","111").param("operand2","1010"))//.andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("10001"));
    }
	@Test
    public void add2() throws Exception {
        this.mvc.perform(get("/add_json").param("operand1","111").param("operand2","1010"))//.andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.operand1").value(111))
			.andExpect(MockMvcResultMatchers.jsonPath("$.operand2").value(1010))
			.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(10001))
			.andExpect(MockMvcResultMatchers.jsonPath("$.operator").value("add"));
    }

    @Test
    public void multiply() throws Exception {
        // Test multiplication: 101 (binary for 5) * 11 (binary for 3) should be 1111 (binary for 15)
        this.mvc.perform(get("/multiply")
                .param("operand1", "101")
                .param("operand2", "11"))
            .andExpect(status().isOk())
            .andExpect(content().string("1111"));
    }
    
    @Test
    public void andOperation() throws Exception {
        // Test bitwise AND: 1010 (10) & 1100 (12) should be 1000 (8)
        this.mvc.perform(get("/and")
                .param("operand1", "1010")
                .param("operand2", "1100"))
            .andExpect(status().isOk())
            .andExpect(content().string("1000"));
    }
    
    @Test
    public void implicationOperation() throws Exception {
        // Test implication: using our rule:
        // For each bit: if operand1 is '0', result is '1'; if operand1 is '1', result is operand2's bit.
        // Example: 1010 I 1100 should yield 1101.
        this.mvc.perform(get("/implication")
                .param("operand1", "1010")
                .param("operand2", "1100"))
            .andExpect(status().isOk())
            .andExpect(content().string("1101"));
    }
    @Test
    public void implicationAllOnes() throws Exception {
        // 1111 I 1010: expected "1010"
        this.mvc.perform(get("/implication")
                .param("operand1", "1111")
                .param("operand2", "1010"))
            .andExpect(status().isOk())
            .andExpect(content().string("1010"));
    }

    @Test
    public void implicationDifferentLengths() throws Exception {
        // 101 I 1100 -> expected "1110"
        this.mvc.perform(get("/implication")
                .param("operand1", "101")
                .param("operand2", "1100"))
            .andExpect(status().isOk())
            .andExpect(content().string("1110"));
    }
    @Test
    public void andWithZero() throws Exception {
        // 1010 & 0 = 0
        this.mvc.perform(get("/and")
                .param("operand1", "1010")
                .param("operand2", "0"))
            .andExpect(status().isOk())
            .andExpect(content().string("0"));
    }

    @Test
    public void andDifferentLengths() throws Exception {
        // 101 & 1100 -> expected "100"
        this.mvc.perform(get("/and")
                .param("operand1", "101")
                .param("operand2", "1100"))
            .andExpect(status().isOk())
            .andExpect(content().string("100"));
    }

    @Test
    public void multiplyZero() throws Exception {
        // 0 * 1010 = 0
        this.mvc.perform(get("/multiply")
                .param("operand1", "0")
                .param("operand2", "1010"))
            .andExpect(status().isOk())
            .andExpect(content().string("0"));
    }

    @Test
    public void multiplyLeadingZeros() throws Exception {
        // "00101" * "00011" should equal "1111"
        this.mvc.perform(get("/multiply")
                .param("operand1", "00101")
                .param("operand2", "00011"))
            .andExpect(status().isOk())
            .andExpect(content().string("1111"));
    }

}