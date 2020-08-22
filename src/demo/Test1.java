package demo;

import java.util.Scanner;

public class Test1 {
    public static void main(String[] args) {
        //System.out.println(ln(3));
        Scanner sc = new Scanner(System.in);
        double a = sc.nextDouble();
        System.out.println(a);
    }

    public static int mystery(int a, int b) {
        if (b == 0) return 1;
        if (b % 2 == 0) return mystery(a * a, b / 2);
        else return mystery(a * a, b / 2) * a;
    }
/*
  2,9         2,7
  (4,4)*2;    (4,3)*2
  (8,2)*2;     (8,1)*2*4
  (16,1)*2*16   2*4*8
  2*16  
*/


    //����ƽ����
    public static double sqrt(double c) {
        if (c < 0) {
            return Double.NaN;
        }
        double err = 1e-15;
        double t = c;
        while (Math.abs(t - c / t) > err * t) {
            t = (c / t + t) / 2.0;
            System.out.println(t);
        }
        return t;
    }

    // public static int ln(int n){
    //     int s = 1;
    //     for(int i=1;i<=n;++i){
    //         s*=i;
    //     }
    // }
    public static double epow(double a) {
        double b = 1;
        while (--a > 0) {
            b *= Math.E;
        }
        return b;
    }

    public static double mypow(double a, int b) {
        double temp = a;
        while (--b > 0) {
            a *= temp;
        }
        return a;
    }

    public static double ln(double a) {
        if (a == 1) return 0;
        return ln(a - 1) + Math.log(a);
    }
}