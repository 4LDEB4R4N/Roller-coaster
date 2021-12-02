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
            System.out.println(MessageFormat.format("Passageiro {0} chegou na fila", i));
			passenger.start();
        }
    }

    public boolean Load(int id)
    {
        return true;
    }

    public void Unload(int id)
    {

    }






    
}
