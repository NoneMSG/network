package thread;

public class UpperCaseAlphabetThread extends UpperCaseAlphabet implements Runnable {

	@Override
	public void run() {
		print();
	}

}
