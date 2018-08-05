
public class TrainCondition {

	int stationNumber;
	int wait;
	int signal;
	
	public TrainCondition(int stationNumber) {
		this.stationNumber = stationNumber;
	}
	
	public void cond_init() {
		wait = 0;
		signal = 0;
	}
	
	public void cond_wait(Lock lock) {
		wait += 1;
		//Interface.getInstance().updateWaitingAmount(stationNumber, wait);
		while(signal <= 0) {
			//Interface.getInstance().updateMonitorLock(stationNumber, false);
			lock.lock_release();
			try {
				synchronized(this) {
					wait();
				}
			} catch (InterruptedException e) {}
			lock.lock_acquire();
			//Interface.getInstance().updateMonitorLock(stationNumber, true);
		}
		wait -= 1;
		//Interface.getInstance().updateWaitingAmount(stationNumber, wait);
		signal -= 1;
		synchronized(this) {
			notifyAll();
		}	
	}
	
	public void cond_signal(Lock lock) {
		if(wait > 0) {
			signal = 1;
			//Interface.getInstance().updateMonitorLock(stationNumber, false);
			synchronized(this) {
				notify();
			}			
			lock.lock_release();
		}
	}
	
	public void cond_broadcast(Lock lock) {
		if(wait > 0) {
			signal = wait;
			//Interface.getInstance().updateMonitorLock(stationNumber, false);
			synchronized(this) {
				notifyAll();
			}			
			lock.lock_release();
		}
	}	
	
	public void cond_check(Lock lock) {
		lock.lock_release();
		if(signal > 0) {
			try {
				synchronized(this) {
					wait();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		lock.lock_acquire();
	}

	
}
