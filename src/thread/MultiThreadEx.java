package thread;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadEx {

	public static void main(String[] args) {
		List list = new ArrayList();
		
//		for(int i=0;i<=9;i++)
//		{
//			System.out.print(i);
//		}
		
//		System.out.println();
//		for(char c='a';c<='z';c++){
//			System.out.print(c);
//		}
		
		Thread t1=new AlphabetThread(list);       //서로 list객체를 공유하고 있음. 
		Thread t2=new DigitThread(list);
		Thread t3 = new DigitThread(list);
		Thread t4=new Thread(new UpperCaseAlphabetThread()); //runnable을 받아서 다시 정의하는것
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
	}
	

}
