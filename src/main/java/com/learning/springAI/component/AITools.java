package com.learning.springAI.component;

public class AITools {

        @AiFunction
        public String getWeather(String city) {
            // Normally you'd call a real API here
            return switch (city.toLowerCase()) {
                case "hyderabad" -> "30°C and sunny";
                case "mumbai" -> "27°C and rainy";
                default -> "Weather unavailable for " + city;
            };
        }

        @AiFunction
        public int addNumbers(int a, int b) {
            return a + b;
        }
}
