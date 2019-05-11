package com.company;

import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

public class Main {

    public static void main(String[] args) {
	    System.out.println("Hello world");


        double[] values = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

        Instance instance = new DenseInstance(values);
    }
}
