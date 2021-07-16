package com.example.opencvexample.enum_test;

public enum OverTimeCalculate {
    MONDAY,TUESDAY,WEDNESDAY,TURSDAY,FRIDAY,
    SATURDAY(DayType.Weekend),SUNDAY(DayType.Weekend);

    private DayType dayType;
    OverTimeCalculate(DayType d){
        this.dayType = d;
    }

    OverTimeCalculate() {
        dayType = DayType.Weekday;
    }

    public double calculate(int overHours,int baseNum){
        double result;
        result = dayType.calculate(overHours,baseNum);
        return result;
    }
    public enum DayType{
         Weekday{
             @Override
             double calculateOut(int overHours, int baseNum) {
                 return baseNum * overHours * 1.5;
             }
         },Weekend{
            @Override
            double calculateOut(int overHours, int baseNum) {
                return baseNum * overHours * 2.0;
            }
        };
         abstract double calculateOut(int overHours,int baseNum);
         public double calculate(int overHours,int baseNum){
             return calculateOut(overHours,baseNum);
         }
    }
}
