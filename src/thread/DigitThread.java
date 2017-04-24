package thread;

import java.util.List;

public class DigitThread extends Thread {
	private List list;
	
	public DigitThread(List list){
		this.list = list;
	}
	@Override
	public void run(){
		for(int i = 0 ; i  < 9 ; ++i){
			System.out.print(i);
			//list.add(i);
			synchronized(list){ //동기화를 통해서 해당 객체의 작업이 끝날때 까지 다른 동기화 함수가 블락킹상태가 된다
				list.add(i);	
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
