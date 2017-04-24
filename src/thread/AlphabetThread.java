package thread;

import java.util.List;

public class AlphabetThread extends Thread {
	

	private List list;     //벡터를 사용하지 않으면 매우 위험해짐
	
	public AlphabetThread(List list){
		this.list=list;
	}

	@Override
	public void run() {
		for(char c='a';c<='z';c++){
			System.out.print(c);
			synchronized (list) {    //멈춰서 작업을 하지 않고 돌고있는 와중에 작업을 하기 때문에 synchronized처리를 해준다.
				list.add(c);
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}




}
