package production.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.exception.NotSamePasswordException;
import production.exception.UnknownUserException;
import production.exception.UserAlreadyExistException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public non-sealed class User extends Entitet implements Hashing{
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    private static final Logger logger = LoggerFactory.getLogger(User.class);
    private String username;
    private String password;

    public User(long id, String username, String password) {
        super(id);
        this.username = username;
        this.password = hashPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password){
        this.password = hashPassword(password);
    }

    @Override
    public String toString() {
        return username + " " + password;
    }
    public void dodajUseraUDatoteku(){
        try {
            FileWriter writer = new FileWriter("dat/loginData.txt", true);
            writer.append(toString()).append("\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void dohvatiUsera() throws NotSamePasswordException {
        final String FILE_NAME = "dat/loginData.txt";
        try (BufferedReader in = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split(" ");
                if(parts[0].equals(username)){
                    if(parts[1].equals(password)){
                        return;
                    }
                    else{
                        throw new NotSamePasswordException("unijeli ste krivu lozinku");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        throw new UnknownUserException("Ne postoji ovaj user;");
    }


    public void pronalazakUsernamea(String username)throws UserAlreadyExistException{
        final String FILE_NAME = "dat/loginData.txt";
        try (BufferedReader in = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split(" ");
                if(parts[0].equals(username)){
                    throw new UserAlreadyExistException("Username već postoji u bazi");
                }
            }
        } catch (IOException e) {
            logger.error("Pogrska u radu s datotekom", e);
        }
    }
    public void usporedbaPassworda(String repeatedPassword) throws NotSamePasswordException {
        if(!password.equals(hashPassword(repeatedPassword))){
            throw new NotSamePasswordException("Niste upisali dobru lozinku.");
        }
    }

    public String hashPassword(String passwordToHash){
        byte[] hash = Hashing.digest(passwordToHash.getBytes(UTF_8), "SHA-256");
        return String.format(Hashing.bytesToHex(hash));
    }


}
