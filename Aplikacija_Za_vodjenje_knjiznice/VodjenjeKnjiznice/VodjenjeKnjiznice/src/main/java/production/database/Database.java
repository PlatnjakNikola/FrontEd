package production.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.enums.DramskaDjela;
import production.enums.EpskaDjela;
import production.enums.LirskaDjela;
import production.enums.OstalaDjela;
import production.model.*;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    public static Boolean activeConnectionWithDatabase = Boolean.FALSE;

    /**
     * afunction for connecting with database
     * @return conection to database
     * @throws SQLException when something goes wrong with database
     * @throws IOException when something goes wrong with database
     */
    public synchronized static Connection connectToDatabase() throws SQLException, IOException {
        Properties configuration = new Properties();
        configuration.load(new FileReader("dat/database.properties"));

        String databaseURL = configuration.getProperty("databaseUrl");
        String databaseUsername = configuration.getProperty("databaseUsername");
        String databasePassword = configuration.getProperty("databasePassword");


        return DriverManager
                .getConnection(databaseURL, databaseUsername, databasePassword);
    }


// naredbe za autora

    /**
     * function for getting all the authorf from database
     * @return list of authors
     * @throws SQLException when something goes wrong with database
     * @throws IOException when something goes wrong with database
     */
    public static List<Authors> getAllAuthorsFromDatabase() throws SQLException, IOException {
        Connection veza = connectToDatabase();
        List<Authors> authorsList = new ArrayList<>();

        Statement sqlStatement = veza.createStatement();
        ResultSet authorResultSet = sqlStatement.executeQuery("SELECT  * FROM AUTORI WHERE 1 = 1");

        while(authorResultSet.next()){
            Authors newAuthor = getAuthorFromResultSet(authorResultSet);
            authorsList.add(newAuthor);
        }
        veza.close();
        return(authorsList);
    }

    /**
     *  function that returns author from database result
     * @param authorResultSet value from database
     * @return aauthor from database result
     * @throws SQLException when something goes wrong with database
     */
    private static Authors getAuthorFromResultSet(ResultSet authorResultSet)throws SQLException{

        long authorId = authorResultSet.getLong("IDAUTOR");
        String name = authorResultSet.getString("IME");
        String surname = authorResultSet.getString("PREZIME");
        LocalDate dateOfBirth = authorResultSet.getDate("DATUM_RODJENJA").toLocalDate();

        return new Authors.Builder(authorId)
                .withIme(name)
                .withSurname(surname)
                .withDateOfBirth(dateOfBirth)
                .build();
    }

    /**
     * function that get author from database using id
     * @param id of author
     * @return author form database
     * @throws SQLException when something goes wrong with database
     * @throws IOException when something goes wrong with database
     */
    public static Authors getAuthorFromDatabase(long id) throws SQLException, IOException {
        Connection veza = connectToDatabase();
        StringBuilder sqlUpit = new StringBuilder(
                "SELECT * FROM AUTORI WHERE 1 = 1 AND IDAUTOR = " + id);

        Statement upit = veza.createStatement();
        ResultSet resultSet = upit.executeQuery(sqlUpit.toString());
        resultSet.next();
        long idAutor = resultSet.getLong("IDAUTOR");
        String name = resultSet.getString("IME");
        String surname = resultSet.getString("PREZIME");
        LocalDate dateOfBirth = resultSet.getDate("DATUM_RODJENJA").toLocalDate();
        veza.close();
        return new Authors.Builder(idAutor)
                .withIme(name)
                .withSurname(surname)
                .withDateOfBirth(dateOfBirth)
                .build();
    }

    /**
     * method that inserts author to database
     * @param author that needs to be inserted to database
     * @throws SQLException when something goes wrong with database
     * @throws IOException when something goes wrong with database
     */
    public static void insertNewAuthorToDatabase(Authors author) throws SQLException, IOException {
        Connection veza = connectToDatabase();
        PreparedStatement stmt = veza.prepareStatement(
                "INSERT INTO AUTORI (IME, PREZIME, DATUM_RODJENJA) " +
                        "VALUES(?, ?, ?)");
        stmt.setString(1,author.getName());
        stmt.setString(2,author.getSurname());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        stmt.setString(3,author.getDateOfBirth().format(formatter));
        stmt.executeUpdate();
        veza.close();
    }

    /**
     * method that edit author in database
     * @param authorToUpdate that is updated
     * @throws SQLException when something goes wrong with database
     * @throws IOException when something goes wrong with database
     */
    public static void updateAuthorToDatabase(Authors authorToUpdate) throws SQLException, IOException{
        Connection connection = connectToDatabase();
        PreparedStatement updateAuthor =
                connection.prepareStatement(
                        "UPDATE AUTORI SET IME = ?, PREZIME = ?, DATUM_RODJENJA = ? WHERE IDAUTOR = ?");

        updateAuthor.setString(1, authorToUpdate.getName());
        updateAuthor.setString(2, authorToUpdate.getSurname());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        updateAuthor.setString(3, authorToUpdate.getDateOfBirth().format(formatter));
        updateAuthor.setLong(4, authorToUpdate.getId());

        updateAuthor.executeUpdate();

        connection.close();
    }

    //----------------------------------------------------------------------------------------------------------------

    /**
     * method that gets all departments from database
     * @return list of departments
     * @throws SQLException when something goes wrong with database
     * @throws IOException when something goes wrong with database
     */
    public static List<Odjel> getAllOdjelFromDatabase() throws SQLException, IOException {
        Connection veza = connectToDatabase();
        List<Odjel> odjelList = new ArrayList<>();

        Statement sqlStatement = veza.createStatement();
        ResultSet odjelResultSet = sqlStatement.executeQuery("SELECT * FROM ODJEL");

        while(odjelResultSet.next()){
            Odjel newOdjel = getOdjelFromResultSet(odjelResultSet);
            odjelList.add(newOdjel);
        }
        veza.close();
        return(odjelList);
    }

    /**
     * method that return Department from database result
     * @param odjelResultSet database result
     * @return ddepartment
     * @throws SQLException when something goes wrong with database
     */
    private static Odjel getOdjelFromResultSet(ResultSet odjelResultSet)throws SQLException{

        long odjelId = odjelResultSet.getLong("IDODJEL");
        String name = odjelResultSet.getString("IMEODJEL");
        String genre = odjelResultSet.getString("GENRE");

        if(genre.equals(EpskaDjela.svaEpskaDjela().toString())){
            return new Odjel(odjelId, name, "epika");
        }
        else if(genre.equals(DramskaDjela.svaDramskaDjela().toString())){
            return new Odjel(odjelId, name, "drama");
        }
        else if(genre.equals(LirskaDjela.svaLirskaDjela().toString())){
            return new Odjel(odjelId, name, "lirika");
        }
        else{
            return new Odjel(odjelId, name, "ostala");
        }
    }

    /**
     * function that insert new department to database
     * @param odjel that needs to be inserted
     * @throws SQLException when something goes wrong with database
     * @throws IOException when something goes wrong with database
     */
    public static void insertNewOdjelToDatabase(Odjel odjel) throws SQLException, IOException {
        Connection veza = connectToDatabase();
        PreparedStatement stmt = veza.prepareStatement(
                "INSERT INTO ODJEL (IMEODJEL, GENRE) VALUES(?, ?)");
        stmt.setString(1,odjel.getNaziv());
        stmt.setString(2,odjel.getKnjige().toString());
        stmt.executeUpdate();
        veza.close();
    }

    /**
     * method that updates department in database
     * @param odjelToUpdate department that needs to be updated
     * @throws SQLException when something goes wrong with database
     * @throws IOException when something goes wrong with database
     */
    public static void updateOdjelToDatabase(Odjel odjelToUpdate) throws SQLException, IOException{
        Connection connection = connectToDatabase();
        PreparedStatement updateOdjel =
                connection.prepareStatement(
                        "UPDATE ODJEL SET IMEODJEL = ?, GENRE = ? WHERE IDODJEL = ?");

        updateOdjel.setString(1, odjelToUpdate.getNaziv());
        updateOdjel.setString(2, odjelToUpdate.getKnjige().toString());
        updateOdjel.setLong(3, odjelToUpdate.getId());
        updateOdjel.executeUpdate();
        connection.close();
    }

    /**
     * method that returns specific department using id
     * @param id of department
     * @return the adepartment
     * @throws SQLException when something goes wrong with database
     * @throws IOException when something goes wrong with database
     */
    public static Odjel getOdjelFromDatabase(long id) throws SQLException, IOException {
        Connection veza = connectToDatabase();
        StringBuilder sqlUpit = new StringBuilder(
                "SELECT * FROM ODJEL WHERE 1 = 1 AND IDODJEL = " + id);
        Statement upit = veza.createStatement();
        ResultSet resultSet = upit.executeQuery(sqlUpit.toString());
        resultSet.next();
        long idOdjel = resultSet.getLong("IDODJEL");
        String name = resultSet.getString("IMEODJEL");
        String genre = resultSet.getString("GENRE");
        veza.close();
        return new Odjel(idOdjel, name, genre);
    }


    //----------------------------------------------------------------------------------------------------------------

    /**
     * method that returns books of type DRAMA
     * @return list of books that are object of type DRAMA
     * @throws SQLException when something goes wrong with database
     * @throws IOException when something goes wrong with database
     */
    public static List<Drama> getAllDramasFromDatabase() throws SQLException, IOException {
        Connection veza = connectToDatabase();
        List<Drama> dramaList = new ArrayList<>();

        Statement sqlStatement = veza.createStatement();
        ResultSet dramaResultSet = sqlStatement.executeQuery("SELECT  * FROM DRAMA");

        while(dramaResultSet.next()){
            Drama newdrama = getDramaFromResultSet(dramaResultSet);
            dramaList.add(newdrama);
        }
        veza.close();
        return(dramaList);
    }
    private static Drama getDramaFromResultSet(ResultSet resultSet)throws SQLException{
        long id = resultSet.getLong("IDDRAMA");
        long idAutor = resultSet.getLong("IDAUTOR");
        String ime = resultSet.getString("IMEKNJIGE");
        LocalDate datumIzdavanja = resultSet.getDate("DATUMIZDAVANJA").toLocalDate();
        String opisKnjige = resultSet.getString("OPISKNJIGE");
        long idOdjel = resultSet.getLong("IDODJEL");
        String djelo = resultSet.getString("DJELO");

        try {
            Authors autor = getAuthorFromDatabase(idAutor);
            Odjel odjel = getOdjelFromDatabase(idOdjel);

            return new Drama(id, autor, ime, datumIzdavanja, opisKnjige, odjel, djelo);

        } catch (IOException e) {
            logger.info(e.getMessage(), e);
            System.out.println("Pogreska spajanja na bazu.");
            e.printStackTrace();
        }
        return null;
    }

    //----------------------------------------------------------------------------------------------------------------
    //NAREDBE ZA EPIKU
    /**
     * method that returns books of type EPIKA
     * @return list of books that are object of type EPIKA
     * @throws SQLException when something goes wrong with database
     * @throws IOException when something goes wrong with database
     */
    public static List<Epika> getAllEpikasFromDatabase() throws SQLException, IOException {
        Connection veza = connectToDatabase();
        List<Epika> epikaList = new ArrayList<>();

        Statement sqlStatement = veza.createStatement();
        ResultSet epikaResultSet = sqlStatement.executeQuery("SELECT  * FROM EPIKA");

        while(epikaResultSet.next()){
            Epika newEpika = getEpikaFromResultSet(epikaResultSet);
            epikaList.add(newEpika);
        }
        veza.close();
        return(epikaList);
    }
    private static Epika getEpikaFromResultSet(ResultSet resultSet)throws SQLException{

        long id = resultSet.getLong("IDEPIKA");
        long idAutor = resultSet.getLong("IDAUTOR");
        String ime = resultSet.getString("IMEKNJIGE");
        LocalDate datumIzdavanja = resultSet.getDate("DATUMIZDAVANJA").toLocalDate();
        String opisKnjige = resultSet.getString("OPISKNJIGE");
        long idOdjel = resultSet.getLong("IDODJEL");
        String djelo = resultSet.getString("DJELO");

        try {
            Authors autor = getAuthorFromDatabase(idAutor);
            Odjel odjel = getOdjelFromDatabase(idOdjel);

            return new Epika(id, autor, ime, datumIzdavanja, opisKnjige, odjel, djelo);

        } catch (IOException e) {
            logger.info(e.getMessage(), e);
            System.out.println("Pogreska spajanja na bazu.");
            e.printStackTrace();
        }
        return null;

    }

    //----------------------------------------------------------------------------------------------------------------
    //METODE ZA LIRIKU
    /**
     * method that returns books of type LIRIKA
     * @return list of books that are object of type LIRIKA
     * @throws SQLException when something goes wrong with database
     * @throws IOException when something goes wrong with database
     */
    public static List<Lirika> getAllLirikasFromDatabase() throws SQLException, IOException {
        Connection veza = connectToDatabase();
        List<Lirika> lirikaList = new ArrayList<>();

        Statement sqlStatement = veza.createStatement();
        ResultSet lirikaResultSet = sqlStatement.executeQuery("SELECT  * FROM LIRIKA");

        while(lirikaResultSet.next()){
            Lirika newLirika = getLirikaFromResultSet(lirikaResultSet);
            lirikaList.add(newLirika);
        }
        veza.close();
        return(lirikaList);
    }
    private static Lirika getLirikaFromResultSet(ResultSet resultSet)throws SQLException{

        long id = resultSet.getLong("IDLIRIKA");
        long idAutor = resultSet.getLong("IDAUTOR");
        String ime = resultSet.getString("IMEKNJIGE");
        LocalDate datumIzdavanja = resultSet.getDate("DATUMIZDAVANJA").toLocalDate();
        String opisKnjige = resultSet.getString("OPISKNJIGE");
        long idOdjel = resultSet.getLong("IDODJEL");
        String djelo = resultSet.getString("DJELO");

        try {
            Authors autor = getAuthorFromDatabase(idAutor);
            Odjel odjel = getOdjelFromDatabase(idOdjel);

            return new Lirika(id, autor, ime, datumIzdavanja, opisKnjige, odjel, djelo);

        } catch (IOException e) {
            logger.info(e.getMessage(), e);
            System.out.println("Pogreska spajanja na bazu.");
            e.printStackTrace();
        }
        return null;
    }

    //----------------------------------------------------------------------------------------------------------------
    //naredbe za ostala djela
    /**
     * method that returns books of type OSTALADJELA
     * @return list of books that are object of type OSTALADJELA
     * @throws SQLException when something goes wrong with database
     * @throws IOException when something goes wrong with database
     */
    public static List<OstalaKnjizevnaDjela> getAllOstalaKnjizevnaDjelaFromDatabase() throws SQLException, IOException {
        Connection veza = connectToDatabase();
        List<OstalaKnjizevnaDjela> ostalaKnjizevnaDjelaList = new ArrayList<>();

        Statement sqlStatement = veza.createStatement();
        ResultSet ostalaKnjizevnaDjelaResultSet = sqlStatement.executeQuery("SELECT  * FROM OSTALADJELA");

        while(ostalaKnjizevnaDjelaResultSet.next()){
            OstalaKnjizevnaDjela newDjela = getOstalaKnjizevnaDjelaFromResultSet(ostalaKnjizevnaDjelaResultSet);
            ostalaKnjizevnaDjelaList.add(newDjela);
        }
        veza.close();
        return(ostalaKnjizevnaDjelaList);
    }
    private static OstalaKnjizevnaDjela getOstalaKnjizevnaDjelaFromResultSet(ResultSet resultSet)throws SQLException{

        long id = resultSet.getLong("IDOSTALA");
        long idAutor = resultSet.getLong("IDAUTOR");
        String ime = resultSet.getString("IMEKNJIGE");
        LocalDate datumIzdavanja = resultSet.getDate("DATUMIZDAVANJA").toLocalDate();
        String opisKnjige = resultSet.getString("OPISKNJIGE");
        long idOdjel = resultSet.getLong("IDODJEL");
        String djelo = resultSet.getString("DJELO");

        try {
            Authors autor = getAuthorFromDatabase(idAutor);
            Odjel odjel = getOdjelFromDatabase(idOdjel);
            return new OstalaKnjizevnaDjela(id, autor, ime, datumIzdavanja, opisKnjige, odjel, djelo);

        } catch (IOException e) {
            logger.info(e.getMessage(), e);
            System.out.println("Pogreska spajanja na bazu.");
            e.printStackTrace();
        }
        return null;
    }


    /**
     * method that inserts book into database
     * @param knjizevnoDjelo book that needs to be inserted
     * @param <T> is generic type that can replace other objects
     * @throws SQLException when something goes wrong with database
     * @throws IOException when something goes wrong with database
     */
    public static <T extends Knjiga> void insertNewKnjizevnaDjelaToDatabase(T knjizevnoDjelo) throws SQLException, IOException {
        Connection veza = connectToDatabase();
        PreparedStatement stmt;

        if(knjizevnoDjelo instanceof Drama){
            stmt = veza.prepareStatement(
                    "INSERT INTO DRAMA (IDAUTOR, IMEKNJIGE, DATUMIZDAVANJA, OPISKNJIGE, IDODJEL, DJELO) VALUES(?, ?, ?, ?, ?, ?)");
            stmt.setString(6, ((Drama) knjizevnoDjelo).getDramskoDjelo().toString());
        }
        else if(knjizevnoDjelo instanceof Epika){
            stmt = veza.prepareStatement(
                    "INSERT INTO EPIKA (IDAUTOR, IMEKNJIGE, DATUMIZDAVANJA, OPISKNJIGE, IDODJEL, DJELO) VALUES(?, ?, ?, ?, ?, ?)");
            stmt.setString(6, ((Epika) knjizevnoDjelo).getEpskoDjelo().toString());
        }
        else if(knjizevnoDjelo instanceof Lirika){
            stmt = veza.prepareStatement(
                    "INSERT INTO LIRIKA (IDAUTOR, IMEKNJIGE, DATUMIZDAVANJA, OPISKNJIGE, IDODJEL, DJELO) VALUES(?, ?, ?, ?, ?, ?)");
            stmt.setString(6, ((Lirika) knjizevnoDjelo).getLirskoDjelo().toString());
        }
        else{
            stmt = veza.prepareStatement(
                    "INSERT INTO OSTALADJELA (IDAUTOR, IMEKNJIGE, DATUMIZDAVANJA, OPISKNJIGE, IDODJEL, DJELO) VALUES(?, ?, ?, ?, ?, ?)");
            stmt.setString(6, ((OstalaKnjizevnaDjela) knjizevnoDjelo).getDjelo().toString());
        }

        stmt.setLong(1, knjizevnoDjelo.getAutor().getId());
        stmt.setString(2,knjizevnoDjelo.getImeKnjige());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        stmt.setString(3,knjizevnoDjelo.getDatumIzdavanja().format(formatter));
        stmt.setString(4, knjizevnoDjelo.getOpisKnjige());
        stmt.setLong(5, knjizevnoDjelo.getOdjel().getId());

        stmt.executeUpdate();

        veza.close();
    }

    /**
     * method that deletes book from database
     * @param knjizevnoDjelo book that needs to be deleted
     * @param <T> is generic type that can replace other objects
     * @throws SQLException when something goes wrong with database
     * @throws IOException when something goes wrong with database
     */
    public static <T extends Knjiga> void deleteKnjizevnaDjelafromDatabase(T knjizevnoDjelo) throws SQLException, IOException {
        Connection veza = connectToDatabase();
        PreparedStatement stmt;

        if(knjizevnoDjelo instanceof Drama){
            stmt = veza.prepareStatement(
                    "DELETE FROM DRAMA WHERE IDDRAMA = ?");
        }
        else if(knjizevnoDjelo instanceof Epika){
            stmt = veza.prepareStatement(
                    "DELETE FROM EPIKA WHERE IDEPIKA = ?");
        }
        else if(knjizevnoDjelo instanceof Lirika){
            stmt = veza.prepareStatement(
                    "DELETE FROM LIRKIA WHERE IDLIRIKA = ?");
        }
        else{
            stmt = veza.prepareStatement(
                    "DELETE FROM OSTALADJELA WHERE IDOSTALA = ?");
        }

        stmt.setLong(1, knjizevnoDjelo.getId());
        stmt.executeUpdate();

        veza.close();
    }

    /**
     * method that updates book from database
     * @param objectToUpdate book that needs to be updated
     * @param <T> is generic type that can replace other objects
     * @throws SQLException when something goes wrong with database
     * @throws IOException when something goes wrong with database
     */
    public static <T extends Knjiga> void updateKnjizevnaDjelaToDatabase(T objectToUpdate)throws SQLException, IOException{
        PreparedStatement updateQuery;
        Connection connection = connectToDatabase();
        if(objectToUpdate instanceof Drama){
            updateQuery = connection.prepareStatement(
                    "UPDATE DRAMA SET IDAUTOR = ?, IMEKNJIGE = ?, DATUMIZDAVANJA = ?, OPISKNJIGE = ?, IDODJEL = ?, DJELO = ? WHERE IDDRAMA = ?");

            updateQuery.setString(6, ((Drama) objectToUpdate).getDramskoDjelo().toString());
        }
        else if(objectToUpdate instanceof Epika){
            updateQuery = connection.prepareStatement(
                    "UPDATE EPIKA SET IDAUTOR = ?, IMEKNJIGE = ?, DATUMIZDAVANJA = ?, OPISKNJIGE = ?, IDODJEL = ?, DJELO = ? WHERE IDEPIKA = ?");

            updateQuery.setString(6, ((Epika) objectToUpdate).getEpskoDjelo().toString());
        }
        else if(objectToUpdate instanceof Lirika){
            updateQuery = connection.prepareStatement(
                    "UPDATE LIRIKA SET IDAUTOR = ?, IMEKNJIGE = ?, DATUMIZDAVANJA = ?, OPISKNJIGE = ?, IDODJEL = ?, DJELO = ? WHERE IDLIRIKA = ?");

            updateQuery.setString(6, ((Lirika) objectToUpdate).getLirskoDjelo().toString());
        }
        else{
            updateQuery = connection.prepareStatement(
                            "UPDATE OSTALADJELA SET IDAUTOR = ?, IMEKNJIGE = ?, DATUMIZDAVANJA = ?, OPISKNJIGE = ?, IDODJEL = ?, DJELO = ? WHERE IDOSTALA = ?");

            updateQuery.setString(6, ((OstalaKnjizevnaDjela) objectToUpdate).getDjelo().toString());
        }

        updateQuery.setLong(1, objectToUpdate.getAutor().getId());
        updateQuery.setString(2, objectToUpdate.getImeKnjige());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        updateQuery.setString(3, objectToUpdate.getDatumIzdavanja().format(formatter));
        updateQuery.setString(4, objectToUpdate.getOpisKnjige());
        updateQuery.setLong(5, objectToUpdate.getOdjel().getId());
        updateQuery.setLong(7, objectToUpdate.getId());
        updateQuery.executeUpdate();

        connection.close();
    }


    /**
     * method that get all books from database
     * @return list of books from database
     * @throws SQLException when something goes wrong with database
     * @throws IOException when something goes wrong with database
     */
    public static List<Knjiga> getAllBookFromDatabase() throws SQLException, IOException {

        return Stream.of(getAllOstalaKnjizevnaDjelaFromDatabase(), getAllDramasFromDatabase(), getAllEpikasFromDatabase(), getAllLirikasFromDatabase())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}
