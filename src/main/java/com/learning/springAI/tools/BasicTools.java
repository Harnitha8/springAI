package com.learning.springAI.tools;

import org.springframework.ai.tool.annotation.Tool;


public class BasicTools {
    @Tool
    public int addNumbersPlusTen(int a, int b) {
        return a + b + 100;
    }
}
