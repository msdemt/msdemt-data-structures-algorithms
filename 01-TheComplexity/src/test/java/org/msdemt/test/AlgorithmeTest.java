package org.msdemt.test;

public class AlgorithmeTest {


    //计算a与b的和
    public static int plus(int a, int b){
        return a + b;
    }

    //计算1+2+3+...+n的和
    public static int sum(int n){
        int result = 0;
        for(int i=1; i<=n; i++){
            result += i;
        }
        return result;
    }

}
