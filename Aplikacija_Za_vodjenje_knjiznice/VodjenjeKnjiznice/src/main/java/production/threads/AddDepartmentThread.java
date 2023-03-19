package production.threads;

import production.database.Database;
import production.model.Odjel;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class AddDepartmentThread implements Runnable {
    private Odjel odjel;

    public AddDepartmentThread(Odjel odjel){
        this.odjel = odjel;
    }

    @Override
    public void run(){

        connectionWithDatabase();

    }

    public synchronized void connectionWithDatabase(){
        while(Database.activeConnectionWithDatabase == true){
            try{
                System.out.println("CEKANJE NA RED ZA POVEZIVANJE S BAZOM");
                wait();
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
        }
        Database.activeConnectionWithDatabase = true;

        try{
            Database.insertNewOdjelToDatabase(odjel);
        }catch(IOException | SQLException e){
            e.printStackTrace();
        }

        Database.activeConnectionWithDatabase = false;
        notifyAll();
    }
}
