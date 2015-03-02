package com.lili.lifecycle;

import java.util.Random;

/**
 * Created by lili on 2015/3/2.
 * 测试打成可执行的jar包
 * java -jar starter-1.0.jar
 */
public class HelloWorld {
	public static void main(String args[]) {
		System.out.println("hello world, I'm from jar...");
		Random random = new Random();
		for(int i=0;i<10; i++) {
			int j  = random.nextInt(100); //产生小于100的随机数
			System.out.println(j);
		}
	}

}
