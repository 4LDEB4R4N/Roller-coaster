import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RollerCoaster 
{
    private int passengerNumber;
    private int cartNumber;
    private int cartCapacity;

    //Control
    private ReentrantLock loadLock;
    private ReentrantLock unloadLock;
    private Condition load;
    private Condition unload;
	private Condition full;
    private Condition empty;

    private int queue = 0;

    private int[] passengerInCart;
	private boolean[] availableCart;

    private int nextCartToLoad = 0;
	private int nextCartToUnload = 0;

    private int cartCont;
	private int passengerCont;

    //Other
    private long waitTime;
    private long totalWaitTime = 0;
    private long minWaitTime = 0;
    private long maxWaitTime = 99;
	private long dayBgn;
	private long dayEnd;
	private long dayDuration;

	private int[] rides;

    public RollerCoaster
    (
        int passengerNumber,
        int cartNumber,
        int cartCapacity
    ){
        this.passengerNumber = passengerNumber;
        this.cartNumber      = cartNumber;
        this.cartCapacity    = cartCapacity;

        this.loadLock = new ReentrantLock();
        this.load     = this.loadLock.newCondition();
        this.full     = this.loadLock.newCondition();

        this.unloadLock = new ReentrantLock();
        this.unload     = this.unloadLock.newCondition();
        this.empty      = this.unloadLock.newCondition();

        this.passengerInCart = new int[cartCapacity];
        this.availableCart = new boolean[cartNumber];

        this.passengerCont  = passengerNumber;
        this.cartCont       = cartNumber; 
    }

    private void CreateCarts()
    {
        int i;
        for(i = 0; i > cartNumber; i++)
        {
            Thread cart = new Thread(new Cart(this, i));
            System.out.println(MessageFormat.format("cart {0} created", i));
            cart.start();
        }
    }
    
    private void CreatePassengers () throws InterruptedException
    {
        Random arriveTime = new Random();
        float arriveTimeSec;

        int i;
        for(i = 0; i < passengerNumber; i++)
        {
            arriveTimeSec = (arriveTime.nextFloat() * 2) + 1;
            Thread.sleep((long)(arriveTimeSec * 1000));
            Thread passenger = new Thread(new Passenger(this, i));
            System.out.println(MessageFormat.format("Passenger {0} arrived", i));
			passenger.start();
        }
    }

    public void WaitLoad(int id) throws InterruptedException
    {
        loadLock.lock();

        long bgn;
        long end;

        bgn = System.currentTimeMillis();

        if (!availableCart[nextCartToLoad] || passengerInCart[nextCartToLoad] == cartCapacity)
        {
			queue++;
			System.out.println(MessageFormat.format("Passenger {0} is wainting for a cart", id));
			load.awaitUninterruptibly();
			queue--;                             
		}

        end = System.currentTimeMillis();

        waitTime = end - bgn;
		totalWaitTime += waitTime;
		
		if (waitTime > maxWaitTime) maxWaitTime = waitTime;
		if (waitTime < minWaitTime) maxWaitTime = waitTime;

        passengerInCart[nextCartToLoad]++;
		
		System.out.println(MessageFormat.format("Passenger {0} esta embarcando no carrinho {1}", id, nextCartToLoad));
		
		if (passengerInCart[nextCartToLoad] == 4) { 
			availableCart[nextCartToLoad] = false;
			nextCartToLoad = (nextCartToLoad + 1) % cartNumber;
			Thread.sleep(1000); 
			System.out.println("\nOs 4 passageiros embarcaram\n");
			full.signal();
		}
		
        loadLock.unlock();
    }

    public boolean Load(int id)
    {
        loadLock.lock();
        loadLock.unlock();
        return true;
    }


    public void WaitUnload(int id)
    {
        unloadLock.lock();
        unload.awaitUninterruptibly();
        unloadLock.unlock();
    }

    public void Unload(int id)
    {

    }

    





    
}
