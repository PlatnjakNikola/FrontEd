package production.threads;

import production.database.Database;
import production.model.Odjel;

import java.io.IOException;
import java.sql.SQLException;

public class EditDepartmentThread implements Runnable{
    private Odjel odjel;

    public  EditDepartmentThread(Odjel odjel){
        this.odjel = odjel;
    }

    @Override
    public void run(){
        try{
            ConnectionWithDatabase();
        }catch(IOException | SQLException e){
            e.printStackTrace();
        }
    }

    public synchronized void ConnectionWithDatabase()throws IOException, SQLException{
        while(Database.activeConnectionWithDatabase){
            try{
                System.out.println("CEKANJE NA RED ZA POVEZIVANJE S BAZOM");
                wait();
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
        }
        Database.activeConnectionWithDatabase = Boolean.TRUE;

        Database.updateOdjelToDatabase(odjel);

        Database.activeConnectionWithDatabase = Boolean.FALSE;
        notifyAll();
    }

}
