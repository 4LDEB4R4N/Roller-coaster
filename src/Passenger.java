public class Passenger implements Runnable
{
    private int id;
    private RollerCoaster RC;

    public Passenger
    (
        RollerCoaster RC,
        int id
    ){
        this.RC = RC;
        this.id = id;
    }

    public void run()
    {
        try
        {
			RC.Load(id);
			RC.Unload(id);
		}
        catch (InterruptedException e)
        {
			e.printStackTrace();
		}
    }
    
}
