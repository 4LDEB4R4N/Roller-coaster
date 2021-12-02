public class Cart implements Runnable
{

	private int id;
	private RollerCoaster RC;
	
	public Cart
    (
        RollerCoaster RC,
        int id
    ){
		this.RC = RC;
		this.id = id;
	}
	
	public void run ()
    {
		try
        {
			while(RC.Load(id))
            {
				Thread.sleep(1000);
				Thread.sleep(10000);
				RC.Unload(id);
				Thread.sleep(1000);
			}
		}
        catch (InterruptedException e) 
        {
			e.printStackTrace();
		}	
		
	}
    
}
