package thread;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadEx {

	public static void main(String[] args) {
		List list = new ArrayList();
		
//		for(int i = 0 ; i  < 9 ; ++i){
//			System.out.print(i);
//		}
		//single thread는 순차적으로 작업을 처리한다.
//		for(char c ='a' ; c < 'z' ; ++c){
//			System.out.print(c);
//		}
		
		Thread t1 = new AlphabetThread(list);
		Thread t2 = new DigitThread(list);
		Thread t3 = new DigitThread(list);
		Thread t4 = new Thread(new UpperCaseAlphabetThread()); //runnable 인터페이스 구현한 객체를 넣어줘도 된다.
		t1.start();
		t2.start();
		t3.start();
		t4.start();
	}
}
