package thread;

import java.util.List;

public class AlphabetThread extends Thread{
	private List list;
	
	public AlphabetThread(List list){
		this.list=list;
	}
	@Override
	public void run() {
		for(char c = 'a' ; c < 'z'; ++c){
			System.out.print(c);
			synchronized(list){
				list.add(c);	
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
