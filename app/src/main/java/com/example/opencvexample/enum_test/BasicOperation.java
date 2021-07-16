package com.example.opencvexample.enum_test;

public enum BasicOperation implements Operation{
    PULS("+"){
        @Override
        public double apply(double x, double y) {
            return x + y;
        }

    },

    MINUS("-"){
        @Override
        public double apply(double x, double y) {
            return x - y;
        }
    },
    TIMES("*"){
        @Override
        public double apply(double x, double y) {
            return x * y;
        }
    },
    DIVIDE("/"){
        @Override
        public double apply(double x, double y) {
            return x / y;
        }
    };

    private String oper = "";
    BasicOperation(String operation) {
        oper = operation;
    }
    public static double dealOperation(BasicOperation basicOperation,double x, double y){
        return basicOperation.apply(x,y);
    }
    public String getOper() {
        return oper;
    }

    @Override
    public String toString() {
        return "BasicOperation{" +
                "oper='" + oper + '\'' +
                '}';
    }
}
